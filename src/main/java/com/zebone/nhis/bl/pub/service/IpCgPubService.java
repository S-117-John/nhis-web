package com.zebone.nhis.bl.pub.service;

import com.zebone.nhis.bl.pub.dao.CgQryMaintainMapper;
import com.zebone.nhis.bl.pub.support.CgProcessUtils;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.vo.*;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.service.QueryRemainFeeService;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IpCgPubService {

	@Autowired
	private PriceStrategyService priceStrategyService;

	@Autowired
	private CgQryMaintainService cgQryMaintainService;

	@Resource
	private CgQryMaintainMapper cgQryMaintainMapper;
	
	@Autowired
	private BlIpPubMapper blIpPubMapper;
	@Autowired
	private PvInfoPubService pvInfoPubService;
	@Resource
	private QueryRemainFeeService queryFeeServcie;
	@Resource
	private InvSettltService invSettltService;
	
	/**
	 * 记费服务
	 * @param param
	 * @param user
	 */
	public void savePatiCgInfo(String param, IUser user) {		
		List<BlPubParamVo> vos = JsonUtil.readValue(param, new TypeReference<List<BlPubParamVo>>(){});
		//追加患者本次就诊是否已结算的判断
		if(vos != null && vos.size() > 0)
		{
			BlPubParamVo blPubParamVo = vos.get(0);

			StringBuilder sb = new StringBuilder();
			Object[] objArray = null;
			sb.append("SELECT pe.eu_status as eustatus ");
			sb.append("FROM   pv_encounter pe ");
			sb.append("WHERE    pe.pk_pv = ? ");
			objArray = new Object[]{blPubParamVo.getPkPv()};
			
			String sql = sb.toString();
			List<Map<String, Object>> objList = DataBaseHelper.queryForList(sql, objArray);
			for(Map<String, Object> mapItem : objList) 
			{
				if(mapItem.get("eustatus") != null && "3".equals(mapItem.get("eustatus").toString()))
				{
					//患者已结算
					throw new BusException("此患者已结算，记费失败!");
				}
				break;
			}
		}
		BlPubReturnVo cgIpret= chargeIpBatch(vos,false);
		ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "saveConsumable", param,cgIpret);
		//根据前台传来的额外参数执行附加动作
		if(vos != null && vos.size() > 0)
		{
			Set<String> setPks = new HashSet<String>();
			for(BlPubParamVo item : vos)
			{
				if(item.getPkMidBlOp() != null && !"".equals(item.getPkMidBlOp()))
				{
					setPks.add(item.getPkMidBlOp());
				}
			}
			int nCountForUpdateState = setPks.size();
			if(nCountForUpdateState > 0)
			{
				int count = DataBaseHelper.execute("update mid_bl_op set fee_flag='1' where fee_flag='0' and (pk_mid_bl_op in (" + CommonUtils.convertSetToSqlInPart(setPks, "pk_mid_bl_op") + "))",new Object[] { });
				if(count < nCountForUpdateState)
				{
					throw new BusException("此患者存在待计费的数据已计费，请刷新后重试!");
				}
			}
		}
	}
	/**
	 * 查询患者可退费明细
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryChargeInfo(String param, IUser user) {
		
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String)paraMap.get("pkPv");
		String deptType = paraMap.get("type")==null?"1":(String)paraMap.get("type");
		String pkDept = UserContext.getUser().getPkDept();
		
		StringBuffer sql = new StringBuffer(" select * from ( select ord.ordsn,blip.date_cg,blip.date_hap, blip.name_cg,item.spcode, blip.price_org,blip.price,blip.spec,");
		sql.append(" blip.quan + (CASE WHEN back.quan is null THEN 0 ELSE back.quan END) as quan ,");
		sql.append(" blip.amount + (CASE WHEN back.amt is null THEN 0 ELSE back.amt END) as amount,");
		sql.append(" blip.amount + (CASE WHEN back.amt is null THEN 0 ELSE back.amt END) as oldAmount,");
		sql.append("case when blip.flag_pd='0' then item.code_hp when blip.flag_pd='1' then pd.code_hp end codeHp,");
		sql.append(" blip.pk_cgip,blip.barcode,blip.pk_dept_ex,blip.name_emp_ex from bl_ip_dt blip left outer join ");
		sql.append(" (select sum(quan) quan,sum(amount) amt,pk_cgip_back");
		sql.append(" from bl_ip_dt where flag_settle = 0 and flag_pd = 0 and  quan < 0  and pk_pv = ?");
		/** sqlServer数据库只要上面这个加了pkDeptex就导致这个索引失效，查询会很慢，我也不知道为什么，先注销了*/
		sql.append(" group by pk_cgip_back,pk_cnord) back on blip.pk_cgip=back.pk_cgip_back");
		sql.append(" left join bd_item item on blip.pk_item = item.pk_item left join bd_pd pd on blip.pk_item = pd.pk_pd ");
		sql.append(" left join cn_order ord on blip.pk_cnord = ord.pk_cnord  ");
		sql.append(" where  blip.flag_settle = 0 and blip.flag_pd = 0  and  blip.quan > 0 ");
		if("1".equals(deptType)){
			sql.append(" and blip.pk_dept_ex = ? ");
		}
		sql.append("and blip.pk_pv = ?");
		sql.append ( ") temp where temp.quan > 0 order by temp.date_cg desc");
		if("1".equals(deptType)){
			return  DataBaseHelper.queryForList(sql.toString(), pkPv,pkDept,pkPv);
		}else{
			return  DataBaseHelper.queryForList(sql.toString(), pkPv,pkPv);
		}
	}
	
	/**
	 * 查询患者可退费明细(包含药品)住院其他记账功能使用  007003004004
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryAllChargeInfo(String param, IUser user) {
		
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String)paraMap.get("pkPv");
		String deptType = paraMap.get("type")==null?"1":(String)paraMap.get("type");
		String pkDept = UserContext.getUser().getPkDept();
		
		StringBuffer sql = new StringBuffer(" select * from ( select blip.date_cg,blip.date_hap, blip.name_cg, blip.price_org,blip.price,");
		sql.append(" blip.quan + (CASE WHEN back.quan is null THEN 0 ELSE back.quan END) as quan ,");
		sql.append(" blip.amount + (CASE WHEN back.amt is null THEN 0 ELSE back.amt END) as amount,");
		sql.append(" blip.amount + (CASE WHEN back.amt is null THEN 0 ELSE back.amt END) as oldAmount,");
		sql.append("case when blip.flag_pd='0' then item.code_hp when blip.flag_pd='1' then pd.code_hp end codeHp,");
		sql.append(" blip.pk_cgip,blip.barcode,blip.pk_dept_ex,blip.name_emp_ex from bl_ip_dt blip left outer join ");
		sql.append(" (select sum(quan) quan,sum(amount) amt,pk_cgip_back");
		sql.append(" from bl_ip_dt where flag_settle = 0 and eu_bltype = '9' and  quan < 0  and pk_pv = ?");
		if("1".equals(deptType)){
			sql.append(" and pk_dept_ex = ?");
		}
		
		sql.append(" group by pk_cgip_back) back on blip.pk_cgip=back.pk_cgip_back");
		sql.append(" left join bd_item item on blip.pk_item = item.pk_item left join bd_pd pd on blip.pk_item = pd.pk_pd ");
		sql.append(" where  blip.flag_settle = 0 and blip.eu_bltype = '9' and blip.pk_pv = ? and  blip.quan > 0 ");
		if("1".equals(deptType)){
			sql.append(" and blip.pk_dept_ex = ? ");
		}
		sql.append ( ") temp where temp.quan > 0 order by temp.date_cg desc");
		if("1".equals(deptType)){
			return  DataBaseHelper.queryForList(sql.toString(), pkPv,pkDept,pkPv,pkDept);
		}else{
			return  DataBaseHelper.queryForList(sql.toString(), pkPv,pkPv);
		}
	}
	
	/**
	 * 退费服务
	 * @param param pkCgip记费主键，Quan退费数量
	 * @param user
	 */
	public void savePatiRefundInfo(String param, IUser user) {
		
		List<RefundVo> params = JsonUtil.readValue(param, new TypeReference<List<RefundVo>>(){});
		BlPubReturnVo ipcgvo= refundInBatch(params);
		ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "savaReturnConsumable", param,ipcgvo);
		
	}
    /**
     * 根据医保或患者分类更新患者费用明细
     * @param pkPv
     * @param pkHp
     * @param pkPicate
     * @param oldPkInsu 患者原主医保
     */
	public void updateBlIpDtCgRate(String pkPv,String pkHp,String pkPicate,String oldPkInsu,String oldPkPicate){
		Map<String,Object> rtn = CgProcessUtils.updateIpCg(pkPv, pkHp, pkPicate,oldPkInsu,oldPkPicate);;
		if(rtn!=null&&"true".equals(rtn.get("enable"))){
			return;
		}
		//1.医保不为空获取医保信息
		//2.患者分类不为空获取患者分类信息
		//3.查询患者对应未结算费用明细
		//4.依次调用记费策略设置自费比例、优惠比例及合计金额（不再处理单价）
		
	}
	
	/**
	 * 校验待退费项目的数量是否和可退费数量相同
	 * @param refundVo
	 * @param awQuanReList
	 * @return
	 */
	private BlIpDt checkItemQuan(RefundVo refundVo,List<BlIpDt> awQuanReList){
		BlIpDt returnVo = null;
		for(BlIpDt reVo : awQuanReList){
			boolean isSame=refundVo.getPkCgip().equals(reVo.getPkCgip());
			if (!isSame){
				continue;
			}
			if (refundVo.getQuanRe().compareTo(reVo.getQuan())==1){
				throw new BusException("【"+reVo.getNameCg()+"】超出最大可退数量："+reVo.getQuan());
			}
			//只有可退数量=本次退费数量时才需要赋值
			if(refundVo.getQuanRe().compareTo(reVo.getQuan())==0){
				returnVo = reVo;
				break;
			}
		}
		return returnVo;
	}

	public BlPubReturnVo refundInBatch(List<RefundVo> params) {
		if(params == null || params.size()<=0)
			throw new BusException("未获取到需要退费的记费信息！");
		
		BlPubReturnVo res = new BlPubReturnVo();
		Map<String,Object> paraMap = new HashMap<String,Object>();
		String codeCg = ApplicationUtils.getCode("0602");
		//1.查询记费记录
		List<BlIpDt> vos  = blIpPubMapper.QryBlDtIpByRefunds(params);
		List<BlIpDt> resVos = new ArrayList<BlIpDt>();
		
		/**优化四舍五入差额问题：根据传入的pk_cgip查询此项目有无退过费，查出已退费的quan+本次退费的quan=总quan时无需计算退费金额*/
        List<BlIpDt> awQuanReList = blIpPubMapper.qryItemAllowQuanRe(params);
        
        //排序号  code_cg+sortno唯一
        int cgSortNo = 1;

		Map<String, List<BlIpDt>> blipDtGroup = vos.stream().collect(Collectors.groupingBy(BlIpDt::getPkCgip));
		Date dateCg = new Date();

		for(int i = 0;i<params.size();i++){
			RefundVo refundVo =params.get(i);
			BlIpDt vo = null;
			String pkCgip = null;
			List<BlIpDt> list = blipDtGroup.get(refundVo.getPkCgip());
			if(CollectionUtils.isNotEmpty(list)){
				vo = new BlIpDt();
				BlIpDt origVo = list.get(0);
				pkCgip = origVo.getPkCgip();
				ApplicationUtils.copyProperties(vo, origVo);
			}

			if(vo==null){
            	throw new BusException("退费时未获取到对应的原始记费记录！");
            }
            
			//1.1设置退费主键
			vo.setPkCgipBack(pkCgip);
			vo.setPkPdapdt(null);
			vo.setPkOrdexdt(refundVo.getPkOrdexdt());
			vo.setCodeCg(codeCg);
			vo.setDateCg(dateCg);//当前日期时间；
			vo.setDateEntry(vo.getDateCg());//录入日期(同dateCg)
			vo.setPkDeptCg(UserContext.getUser().getPkDept());//当前部门；
			vo.setPkEmpCg(UserContext.getUser().getPkEmp());//当前用户
			vo.setNameEmpCg(UserContext.getUser().getNameEmp());//当前用户姓名；
			vo.setFlagSettle("0");
			vo.setPkSettle("");
			vo.setPkCgip(NHISUUID.getKeyId());
			vo.setQuan(MathUtils.mul(refundVo.getQuanRe(), -1.0));//退费数量*-1
			vo.setFlagInsu("0");
			vo.setSortno(cgSortNo++);
			vo.setNoteCg(refundVo.getNoteCg());//退费原因
			/**优化四舍五入差额问题：根据传入的pk_cgip查询此项目有无退过费，查出已退费的quan+本次退费的quan=总quan时无需计算退费金额*/
            BlIpDt resVo = null;
            if(awQuanReList!=null && awQuanReList.size()>0){
            	resVo = checkItemQuan(refundVo,awQuanReList);
            }
            //true:不需要计算金额   false:需要计算金额
            if(resVo!=null && !CommonUtils.isEmptyString(resVo.getPkCgip())){
            	//设置费用：直接获取sql查询出的可退金额, 无需计算金额
            	vo.setAmount(MathUtils.mul(resVo.getAmount(), -1.0));
            	if(resVo.getAmountHppi()!=null){
            		vo.setAmountHppi(MathUtils.mul(resVo.getAmountHppi(), -1.0));
            	}
            	if(resVo.getAmountAdd()!=null){
            		vo.setAmountAdd(MathUtils.mul(resVo.getAmountAdd(), -1.0));
            	}
            	vo.setAmountPi(MathUtils.mul(resVo.getAmountPi(), -1.0));
            }else{
            	//1.2设置费用
    			vo.setAmount(MathUtils.mul(vo.getPrice(), vo.getQuan()));
    			//amount_hppi，患者支付的医保金额，price*quan*ratio_self
    			vo.setAmountHppi(MathUtils.mul(MathUtils.mul(vo.getPrice(), vo.getQuan()),vo.getRatioSelf()));
    			if(vo.getAmountAdd()!=null)
    				vo.setAmountAdd(MathUtils.mul(MathUtils.sub(vo.getPrice(),vo.getPriceOrg()), vo.getQuan()));
    			vo.setAmountPi(MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(MathUtils.mul(vo.getPriceOrg(), MathUtils.sub(1D, vo.getRatioDisc())), vo.getQuan())));
            }
			
			//1.3
			resVos.add(vo);

		}
		for(BlIpDt vo : resVos){
	        ApplicationUtils.setDefaultValue(vo, true);
		}
		
		//添加个性化退费业务实现
		Map<String,Object> rtn = CgProcessUtils.processIpRefund(params,resVos);
		
		//3批量写入记费表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class),resVos);
		
		if(rtn!=null&&"true".equals(rtn.get("enable"))&&rtn.get("result")!=null){
			return (BlPubReturnVo)rtn.get("result");
		}
		// 9.处理返回。
		res.setBids(resVos);
		return res;
	}

	/**
	 * 当参数BL0066为1时，执行科室取开立科室，执行医生取开立医生，费用补录为病区时：执行科室取就诊科室，执行医生取主管医生
	 * @param blCgPubParamVos
	 */
	public void chargePkDeptEx(List<BlPubParamVo> blCgPubParamVos){
		Set<String> pkDeptExSet = blCgPubParamVos.parallelStream().map(BlPubParamVo::getPkDeptEx).collect(Collectors.toSet());
		pkDeptExSet.addAll(blCgPubParamVos.parallelStream().map(BlPubParamVo::getPkDeptApp).collect(Collectors.toSet()));
		Map<String, Map<String, Object>> deptMap = DataBaseHelper
				.queryListToMap("select pk_dept as key_,dt_depttype  from bd_ou_dept  where del_flag='0' and pk_dept in ("+ CommonUtils.convertSetToSqlInPart(pkDeptExSet, "pk_dept")+")");
		Set<String> pkPvSet = blCgPubParamVos.parallelStream().map(BlPubParamVo::getPkPv).collect(Collectors.toSet());
		Map<String, Map<String, Object>> pvMap = DataBaseHelper
				.queryListToMap("select pk_pv as key_,pk_dept,pk_emp_phy,name_emp_phy  from PV_ENCOUNTER  where pk_pv in ("+ CommonUtils.convertSetToSqlInPart(pkPvSet, "pk_pv")+")");
		for(BlPubParamVo vo:blCgPubParamVos){
			Map<String, Object> map = deptMap.get(vo.getPkDeptEx());
			String dtDepttype = MapUtils.getString(map,"dtDepttype");//执行科室类型
			Map<String, Object> mapApp = deptMap.get(vo.getPkDeptApp());
			String dtDepttypeApp = MapUtils.getString(mapApp,"dtDepttype");//开立科室类型
			//先判断执行科室是药房和病区
        	if(dtDepttype!=null&&("02").equals(dtDepttype.substring(0,2))||"04".equals(dtDepttype.substring(0,2))){
        		//如果是药房或者病区，则执行科室取开立科室，执行医生取开立医生
				vo.setPkDeptEx(vo.getPkDeptApp());
				vo.setPkEmpEx(vo.getPkEmpApp());
				vo.setNameEmpEx(vo.getNameEmpApp());

			}
			//如果开立科室是病区或者病区费用补录，则取患者的就诊科室和主管医生
			if(dtDepttypeApp!=null&&("02").equals(dtDepttypeApp.substring(0,2))||"04".equals(dtDepttypeApp.substring(0,2))){
				Map<String, Object> mapPv = pvMap.get(vo.getPkPv());
				vo.setPkDeptEx(MapUtils.getString(mapPv,"pkDept"));
				vo.setPkEmpEx(MapUtils.getString(mapPv,"pkEmpPhy"));
				vo.setNameEmpEx(MapUtils.getString(mapPv,"nameEmpPhy"));

			}

		}

	}


	/**
	 * 
	 * @param blCgPubParamVos
	 *            传入参数
	 * @return 必要的返回信息
	 */
	public BlPubReturnVo chargeIpBatch(List<BlPubParamVo> blCgPubParamVos,boolean isAllowQF) {
		// 1.校验数据
		try {
			checkBlCgPubParamVo(blCgPubParamVos);
		} catch (BusException e) {
			throw new BusException("传入的记费数据有问题" + e.getMessage());
		}
		//取参数BL0066是否走执行科室写开立科室和医生
		String flagPkDept = ApplicationUtils.getSysparam("BL0066", false);
		if("1".equals(flagPkDept)){
			chargePkDeptEx(blCgPubParamVos);
		}
		//先判断是否存在个性化记费策略，存在则使用个性化记费服务记费，否则使用默认服务记费
		Map<String,Object> rtn = CgProcessUtils.processIpCg(blCgPubParamVos,isAllowQF);
		if(rtn!=null&&"true".equals(rtn.get("enable"))){
			 BlPubReturnVo blretvo = rtn.get("result")==null?null:(BlPubReturnVo)rtn.get("result");
			 Map<String,Object> paramMap=new HashMap<>();
			 paramMap.put("ipDtList",blretvo.getBids() );
			 PlatFormSendUtils.sendBedcgMsg(paramMap);
			 return blretvo;
		} 
		BlPubReturnVo res = new BlPubReturnVo();
		List<BlIpDt> vosForInsert = new ArrayList<BlIpDt>();
		List<BlPubPriceVo> priceVos = new ArrayList<BlPubPriceVo>();
		String codeCg = ApplicationUtils.getCode("0602");
		// 1. 第一次循环取集合值:以及将物品价格放入map中 
		//2、获取所有的物品信息
        Map<String,List<BlIpDt>> diagMap = new HashMap<String,List<BlIpDt>>();
		
		// 7、构建记费数据
		for (BlPubParamVo pvo : blCgPubParamVos) {
			BlIpDt vo = new BlIpDt();
			String pkPv = pvo.getPkPv();
			// 3. 取pv相关信息、以及物品价格信息
			PvEncounter pvvo = DataBaseHelper.queryForBean(
					"select * from pv_encounter where pk_pv = ?",
					PvEncounter.class, pkPv);
			
			String pkCgip = NHISUUID.getKeyId();
			vo.setPkCgip(pkCgip);
			vo.setPkOrdexdt(pvo.getPkOrdexdt());
			vo.setSpec(pvo.getSpec());
			// 7.1设置各类机构，人员，部门信息
			// 7.11 pkOrg，pkPv,pkPi,
			if(StringUtils.hasText(pvo.getPkOrg())){
				vo.setPkOrg(pvo.getPkOrg());
			}else{
				vo.setPkOrg(UserContext.getUser().getPkOrg());
			}	
			vo.setPkPv(pkPv);
			vo.setPkPi(pvo.getPkPi());
			// 7.12pk_org_app,
			// pk_dept_app,pk_dept_ns_app,pk_wg,pk_emp_app,name_emp_app
			vo.setPkOrgApp(pvo.getPkOrgApp());
			vo.setPkDeptApp(pvo.getPkDeptApp());
			vo.setPkPdstdt(pvo.getPkPdstdt());
			vo.setPkDeptNsApp(CommonUtils.isEmptyString(pvo.getPkDeptNsApp())?pvvo.getPkDeptNs():pvo.getPkDeptNsApp());// 开立病区
			//vo.setPkWg(null);
			vo.setPkEmpApp(CommonUtils.isEmptyString(pvo.getPkEmpApp())?pvvo.getPkEmpPhy():pvo.getPkEmpApp());
			vo.setNameEmpApp(CommonUtils.isEmptyString(pvo.getNameEmpApp())?pvvo.getNameEmpPhy():pvo.getNameEmpApp());
			// 7.13 pk_org_ex pk_dept_ex
			vo.setPkOrgEx(pvo.getPkOrgEx());
			vo.setPkDeptEx(CommonUtils.isEmptyString(pvo.getPkDeptEx())?pvvo.getPkDept():pvo.getPkDeptEx());
			// 7.14 pk_dept_cg pk_emp_cg name_emp_cg
			vo.setPkDeptCg(CommonUtils.isEmptyString(pvo.getPkDeptCg())?pvvo.getPkDept():pvo.getPkDeptCg());
			vo.setPkEmpCg(CommonUtils.isEmptyString(pvo.getPkEmpCg())?UserContext.getUser().getPkEmp() : pvo.getPkEmpCg());
			vo.setNameEmpCg(CommonUtils.isEmptyString(pvo.getNameEmpCg())?UserContext.getUser().getNameEmp() : pvo.getNameEmpCg());//当前用户姓名；

			
			
			// 7.2 设置费用发生日期，记费日期 date_cg,date_hap
			vo.setDateCg(new Date());
			vo.setDateHap(pvo.getDateHap());
			// 7.3 如果是药品，设置pd表相关信息 pk_pd,batch_no,pk_unit_pd,pack_size, price_cost
			if("1".equals(pvo.getFlagPd())){
				dealPdInfo(vo, pvo);
			}
			
			// 7.4 设置各类标志 flag_settle flag_pd flag_insu
			vo.setFlagInsu("0");
			vo.setFlagPd(pvo.getFlagPd());
			// 7.5 处方主键，医嘱主键 pk_pres pk_cnord
			vo.setPkPres(pvo.getPkPres());
			vo.setPkCnord(pvo.getPkCnord());
			// 7.6 结算主键 ， pk_settle 退费关联计费主键 pk_cgip_back code_cg 记费编码
			vo.setPkCgipBack(null);
			vo.setFlagSettle("0");
			vo.setCodeCg(codeCg);
			vo.setInfantNo(CommonUtils.isEmptyString(pvo.getInfantNo())?"0":pvo.getInfantNo());
			// 7.7 设置单价，数量，金额，比例等信息
			// price_org,price,quan,amount,pk_disc,ratio_disc,ratio_self,amount_pi
			// 调用价格策略接口取当前单价，原始单价 以及自付比
			CnOrder cnorder = new CnOrder();
			if(StringUtils.hasText(pvo.getPkCnord())){
				cnorder = DataBaseHelper.queryForBean(
						"select * from cn_order where pk_cnord = ?",
						CnOrder.class, pvo.getPkCnord());
			}

			//8.1 医疗组设置
			if(cnorder!=null){
				vo.setPkWgOrg(cnorder.getPkWgOrg());
			}else{
				vo.setPkWgOrg(pvvo.getPkWgOrg());
			}
			vo.setPkWg(pvo.getPkWg());
			vo.setPkWgEx(pvo.getPkWgEx());

			vo.setPkCnordRl(pvo.getPkCnordRl());
			List<BlIpDt> vos = new ArrayList<BlIpDt>();
			if(StringUtils.hasText(pvo.getPkItem())||(StringUtils.hasText(pvo.getPkOrd())&&"1".equals(pvo.getFlagPd()))){
				priceVos = priceStrategyService.getItemPriceAllInfo(pvo.getPkItem(),
						pvo.getFlagPd(),pvo.getPrice(), pvvo, pvo.getDateHap());
				vos = dealPriceInfo(pvo.getQuanCg(), priceVos, vo);
			}else if(StringUtils.hasText(pvo.getPkOrd())&&!"1".equals(pvo.getFlagPd())){
				vos = getFromOrd(pvvo, pvo, vo);				
			}else if(StringUtils.hasText(pvo.getPkCnord())){
				pvo.setPkOrd(cnorder.getPkOrd());
				vos = getFromOrd(pvvo, pvo, vo);
			}
			
			if(vos.size()<=0){
				continue;
			}
			
			// 7.8 设置item表相关信息 pk_itemcate ,pk_item ,name_cg ,pk_unit spec
			if("0".equals(pvo.getFlagPd())){//非药品
				dealItemInfo(vos);
			}
			
			// 7.9 设置各类编码以及婴儿序号 code_bill,code_audit,infant_no
			BigDecimal amt = dealCodeInfo(vos);

			vosForInsert.addAll(vos);
			// 8.2 此处校验所有控费的患者是否欠费
			if(QueryRemainFeeService.isControlFee()){
				 if(pvInfoPubService.isLimiteFee(pkPv)){
					if(!isAllowQF){
						if (!queryFeeServcie.isArrearage(pkPv, pvvo.getPkInsu(),amt)) {
							throw new BusException(pvvo.getNamePi() + "患者已欠费！");
						}
					}	
	     		 }
			}
			// 8.3 单病种控费病人，添加控费集合
			if("2".equals(pvvo.getEuPvmode())){
				List<BlIpDt> bllist =  diagMap.get(pvvo.getPkPv());
				if(bllist == null || bllist.size()<=0){
					diagMap.put(pvvo.getPkPv(), vos);
				}else{
					bllist.addAll(vos);
					diagMap.put(pvvo.getPkPv(),bllist);
				}
			}
		}

		//获取开立医生考勤科室字段
        String pkDeptJob = invSettltService.getPkDept();
		for(BlIpDt vo : vosForInsert){
			ApplicationUtils.setDefaultValue(vo, true);
			vo.setPkDeptJob(pkDeptJob);
		}
		//处理单病种控费（贫困病人不控费）
		checkDiagDiv(diagMap);
		diagMap = null;//使用后置空，方便回收
		// 8,批量写入记费表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class),
				vosForInsert);
		// 9.处理返回。
		res.setBids(vosForInsert);
		return res;
	}
	
	private List<BlIpDt> getFromOrd(PvEncounter pvvo, BlPubParamVo pvo, BlIpDt vo) {
		List<BlIpDt> vos = new ArrayList<BlIpDt>();
		List<BlPubPriceVo> priceVos = new ArrayList<BlPubPriceVo>();
		Map<String,Object> tempMap = new HashMap<String,Object>();
		tempMap.put("pkOrd", pvo.getPkOrd());
		tempMap.put("pkOrg", pvo.getPkOrg());
		List<Map<String,Object>> resItemTemp = cgQryMaintainService.qryPkItemsByPkOrd(tempMap);
		if(resItemTemp!=null && resItemTemp.size()>0){
			for(Map<String,Object> map : resItemTemp){
				Double quanOrd = 1.0;
				if(map.get("quan")!=null){
					quanOrd = ((BigDecimal)map.get("quan")).doubleValue();
				}
				Double quanCg  = MathUtils.mul(quanOrd, pvo.getQuanCg());
				priceVos = priceStrategyService.getItemPriceAllInfo((String)map.get("pkItem"),
						pvo.getFlagPd(),pvo.getPrice(), pvvo, pvo.getDateHap());
				vos.addAll(dealPriceInfo(quanCg, priceVos, vo));			
			}
		}
		return vos;
	}

	/**
	 * 处理账单码，核算码，婴儿序号
	 * 
	 * @param vos
	 * @return curAmt--当前患者自付总额
	 */
	private BigDecimal dealCodeInfo(List<BlIpDt> vos) {
		String codeAudit = "";
		String codeBill = "";
		BigDecimal totalAmt = BigDecimal.ZERO;
		for (BlIpDt vo : vos) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("pkItem", vo.getPkItem());
			param.put("pkOrg", vo.getPkOrg());
			param.put("euType", Constant.IPINV);
			param.put("flagPd", vo.getFlagPd());
			Map<String, Object> resBill =  cgQryMaintainService
					.qryBillCodeByPkItem(param);
			Map<String, Object> resAccount = cgQryMaintainService
					.qryAccountCodeByPkItem(param);
			if(resBill!=null){
				codeBill = resBill.get("code")==null?"":(String)resBill.get("code") ;
			}
			if(resAccount!=null){
				codeAudit = resAccount.get("code")==null?"":(String) resAccount.get("code");
			}
			vo.setCodeAudit(codeAudit);
			vo.setCodeBill(codeBill);
			totalAmt = totalAmt.add(new BigDecimal(vo.getAmountPi()));
		}
		return totalAmt;
	}

	
	private List<BlIpDt> dealPriceInfo(double quanCg,
			List<BlPubPriceVo> priceVos, BlIpDt vo) {
		return dealPriceInfo(quanCg, priceVos, vo,false);
	}
	/**
	 * 处理价格信息的设置
	 * 
	 * @param quanCg
	 *            参数
	 * @param priceVos
	 *            价格信息
	 * @param vo
	 *            目标对象
	 * @return
	 */
	private List<BlIpDt> dealPriceInfo(double quanCg,
			List<BlPubPriceVo> priceVos, BlIpDt vo,boolean isUpdate) {
		List<BlIpDt> res = new ArrayList<BlIpDt>();
		for (BlPubPriceVo priceVo : priceVos) {			
			BlIpDt baseVo = (BlIpDt) vo.clone();
			Double quanChild = priceVo.getQuanChild().doubleValue()>0?MathUtils.mul(priceVo.getQuanChild().doubleValue(), 1.0):1.0;
			if(!isUpdate){
				baseVo.setPkCgip(NHISUUID.getKeyId());
			 }else{
				 ApplicationUtils.setDefaultValue(baseVo,false);
			 }
			baseVo.setPkItem(priceVo.getPkItem());// 拆分后的项目
			baseVo.setPkDisc(priceVo.getPkDisc());// 优惠主键
			baseVo.setPrice(priceVo.getPrice().doubleValue());// 当前单价
			baseVo.setPriceOrg(priceVo.getPriceOrg().doubleValue());// 原始单价
			baseVo.setRatioDisc(priceVo.getRatioDisc().doubleValue());// 优惠比例
			baseVo.setRatioSelf(priceVo.getRatioSelf().doubleValue());// 自费比例

			baseVo.setQuan(MathUtils.mul(quanChild, quanCg));// 数量
			baseVo.setAmount(MathUtils.mul(priceVo.getPriceOrg().doubleValue(), baseVo.getQuan()));// 金额
			
			//优惠金額，按原始单价，优惠比例计算优惠金额
			Double priceDisc = MathUtils.mul(priceVo.getPriceOrg().doubleValue(), MathUtils.sub(1.0, priceVo.getRatioDisc().doubleValue()));
			//医保报销金额，按当前单价，报销比例计算医保报销金额
			Double priceHp = MathUtils.mul(priceVo.getPrice().doubleValue(), MathUtils.sub(1.0, priceVo.getRatioSelf().doubleValue()));
			//优惠与内部医保叠加优惠后计算患者自负金额
			if(MathUtils.add(priceDisc, priceHp)>priceVo.getPriceOrg().doubleValue()){
				baseVo.setAmountPi(0.0);
			}else{
				Double priceSelf = MathUtils.sub(priceVo.getPriceOrg().doubleValue(), MathUtils.add(priceDisc, priceHp));
				baseVo.setAmountPi(MathUtils.mul(priceSelf, baseVo.getQuan()));
			}			
			res.add(baseVo);
		}
			
		return res;
	}

	/**
	 * 
	 * @param vo 目标对象
	 * @param pvo 参数对象
	 */
	private void dealPdInfo(BlIpDt vo, BlPubParamVo pvo) {
		String pkPd = pvo.getPkOrd();
		BdPd pd = blIpPubMapper.getPdInfoByPk(pkPd);
		vo.setPkItemcate(pd.getPkItemcate());
		vo.setPkPd(pkPd);
		vo.setNameCg(pd.getName());
		if(CommonUtils.isEmptyString(pvo.getSpec())){
			vo.setSpec(pd.getSpec());
		}else{
			vo.setSpec(pvo.getSpec());
		}
		vo.setBatchNo(pvo.getBatchNo());
		vo.setDateExpire(pvo.getDateExpire());
		vo.setPkUnitPd(pvo.getPkUnitPd());
		vo.setBatchNo(pvo.getBatchNo());
		vo.setPackSize(new Double(pvo.getPackSize()));
		vo.setPriceCost(pvo.getPriceCost());
		vo.setPkUnit(pvo.getPkUnitPd());
	}

	/**
	 * 处理item信息
	 * 
	 * @param vos
	 *            目标对象
	 */
	private void dealItemInfo(List<BlIpDt> vos) {
		List<BdItem> items = blIpPubMapper.getBdItemsByCon(vos);
		Map<String, BdItem> itemMap = new HashMap<String, BdItem>();
		for (BdItem item : items) {
			itemMap.put(item.getPkItem(), item);
		}

		for (BlIpDt vo : vos) {
			BdItem item = itemMap.get(vo.getPkItem());
			vo.setPkUnit(item.getPkUnit());
			vo.setPkItemcate(item.getPkItemcate());
			if(CommonUtils.isEmptyString(vo.getSpec())){
				vo.setSpec(item.getSpec());
			}
			vo.setNameCg(item.getName());
		}

	}

	/**
	 * 参数必填校验
	 * 
	 * @param vos
	 */
	private void checkBlCgPubParamVo(List<BlPubParamVo> vos) {
		if (vos.size() < 1) {
			throw new BusException("没有传入的记费数据，请检查！");
		}
		Object obj = null;
		for (BlPubParamVo vo : vos) {
			if(StringUtils.isEmpty(vo.getPkItem()) && StringUtils.isEmpty(vo.getPkCnord()) && StringUtils.isEmpty(vo.getPkOrd())){
				throw new BusException("没有传入记费项目数据，请检查！");
			}
			Set<String> fieldName = new HashSet<String>();
			fieldName.add("pkOrg");
			fieldName.add("euPvType");
			fieldName.add("pkPv");
			fieldName.add("quanCg");
			fieldName.add("pkOrgEx");
			fieldName.add("pkDeptEx");
			fieldName.add( "flagPd" );
			 if("1".equals(vo.getFlagPd())){
				fieldName.add("pkOrd");//物品使用pkOrd
				fieldName.add("batchNo");
				//fieldName.add("dateExpire");
				fieldName.add("pkUnitPd");
				fieldName.add("packSize");
				fieldName.add("priceCost");
				//fieldName.add("namePd");
			}
			for(String str :fieldName){
				obj = ReflectHelper.getFieldValue(vo,
						str);
				if (obj == null) {
					throw new BusException(str + "没有传入！");

				}
			}
		}
	}
	
	public void RechargeToInus(List<BlIpDt> vos,PvEncounter  pvvo) {
		
		List<BlIpDt> newChargeInfo = new ArrayList<BlIpDt>();
		for(BlIpDt vo : vos){
			List<BlPubPriceVo> priceVo = priceStrategyService.getItemPriceAllInfo(vo.getPkItem(),
					vo.getFlagPd(),vo.getPriceOrg(), pvvo, vo.getDateHap());
			newChargeInfo.addAll(dealPriceInfo(vo.getQuan(),priceVo,vo,true)) ;
		}
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlIpDt.class), newChargeInfo);
	}
	
	
/**
 * 查询住院费用明细
 * @param param {euPvtype，codepi，namepi，pkDeptCg，pkEmpCg，dateBegin，dateEnd}
 * @return
 */
    public List<Map<String,Object>> queryBlCgIpDetail(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	return cgQryMaintainService.queryBlCgIpDetails(paramMap);
    }
    /**
     * 记费时核查是否符合单病种控费设置的费用额度（贫困病人不控费）
     * @param pvMap
     */
    public void checkDiagDiv(Map<String,List<BlIpDt>> pvMap){
    	if(pvMap == null) return;
       //遍历患者
    	for (Map.Entry<String,List<BlIpDt>> entry : pvMap.entrySet()) {
    		
    		if(CommonUtils.isEmptyString(entry.getKey()))
			{	continue;}
    		//若当前患者主医保为农合，且患者类型为贫困患者，则不控费
    		if(!pvInfoPubService.isLimiteFee(entry.getKey())){
    			continue;
    		}
    		List<BlDiagDivVo> cateList = cgQryMaintainMapper.queryItemCateList(entry.getKey());
    		if(cateList == null || cateList.size()<=0)
    			continue;
    		//1）	首先判断患者费用+本次收费金额<=单病种限价金额；
        	//2）	如果总额超限，提示用户“单病种费用超限!”,并中断记费处理；
    		BigDecimal pvTotal = cgQryMaintainMapper.getTotalFee(entry.getKey());
    		if(pvTotal == null) pvTotal = BigDecimal.ZERO;
    		List<Map<String,Object>> cateTotalList =cgQryMaintainMapper.getPvTotalFeeByCate(entry.getKey());
    		//3）	如果总额未超限，判断本次记费项目所属的费用分类合计+本次记费金额<=单病种费用分类金额（单病种限价*费用分类限额比例）；
        	//4）	如果费用分类合计金额超限，提示用户“单病种费用分类金额超限！”，并中断记费处理。
    		for(BlDiagDivVo item:cateList){
    			BigDecimal totalFee = new BigDecimal(0);
    			BigDecimal cateFee = new BigDecimal(0);
    			BigDecimal pvcateFee = new BigDecimal(0);
    			for(BlIpDt dt : entry.getValue()){
    				if(entry.getKey().equals(dt.getPkPv())){
    					totalFee = totalFee.add(new BigDecimal(dt.getAmount()));
    					if(item.getPkItemcate().equals(getFatherItemCate(dt.getPkItemcate()))){
    						cateFee = cateFee.add(new BigDecimal(dt.getAmount()));
    						entry.getValue().remove(dt);
    						if(entry.getValue() == null ||entry.getValue().size()<=0)
    							break;
    					}
    					
    				}
    			}
    			//计算已发生的该分类下的费用
    			for(Map<String,Object> cate:cateTotalList){
    	    		String pkItemcate = cate.get("pkItemcate")==null?"":cate.get("pkItemcate").toString();
    	    		if(item.getPkItemcate().equals(getFatherItemCate(pkItemcate))){
    	    			pvcateFee = pvcateFee.add(new BigDecimal(cate.get("amount").toString()));
    	    		}
    	    	}
    			//如果记费金额大于限额，或者大于费用分类限额，提示无法记费
    			if(pvTotal.add(totalFee).compareTo(item.getAmount()==null?BigDecimal.ZERO:item.getAmount())>0)
    				throw new BusException("【"+item.getNamePi()+"】的单病种费用超限!");
    			if((pvcateFee.add(cateFee)).compareTo(item.getAmount().multiply(item.getRate().divide(new BigDecimal(100))))>0)
    				throw new BusException("【"+item.getNamePi()+"】的单病种费用分类【"+item.getNameItemcate()+"】金额超限！");
    		}
    		
    	}
    	
    }
    /**
     * 递归获取费用分类的顶级分类
     * @param pkItemcate
     * @return
     */
    private String getFatherItemCate(String pkItemcate){
    	if(CommonUtils.isEmptyString(pkItemcate))
    		return null;
    	Map<String,Object> itemMap = DataBaseHelper.queryForMap("select pk_parent from bd_itemcate where pk_itemcate = ? ", new Object[]{pkItemcate});
    	if(itemMap == null || itemMap.get("pkParent")==null)
    		return pkItemcate;
    	else 
    		return getFatherItemCate(CommonUtils.getString(itemMap.get("pkParent")));
    }
    
    /**
     * 住院记费，对外直接调用方法
     * @param param
     * @param user
     * @return
     */
    public BlPubReturnVo chargeIpBatch(String param,IUser user) {
    	BlIpCgVo cgParam  = JsonUtil.readValue(param, BlIpCgVo.class);
    	if(cgParam==null) return null;
    	List<BlPubParamVo> blCgPubParamVos = cgParam.getBlCgPubParamVos();
    	boolean isAllowQF = cgParam.isAllowQF();
    	return this.chargeIpBatch(blCgPubParamVos, isAllowQF);
    }
    
    /**
     * 校验患者是否已经欠费
     * @param pvList
     * @return 返回值为欠费患者的pkPv集合
     */
    public List<String> checkPvArrears(List<String> pvList){
    	List<String> rtnPvList = new ArrayList<>();
    	if(QueryRemainFeeService.isControlFee() && pvList!=null && pvList.size()>0){
    		for(String pkPv:pvList){
	   			 //若当前患者主医保为农合，且患者类型为贫困患者，则不控费
	   			 if(pvInfoPubService.isLimiteFee(pkPv)){
	   				 if(!queryFeeServcie.isArrearage(pkPv,"",BigDecimal.ZERO)){//已欠费
	   					rtnPvList.add(pkPv);
	   				 }
	   			 }
	   		 }
    	}
    	
    	return rtnPvList;
    }
    
}
