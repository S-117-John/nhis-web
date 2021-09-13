package com.zebone.nhis.cn.pub.service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.cn.pub.dao.OrderPubMapper;
import com.zebone.nhis.cn.pub.vo.BdPdVo;
import com.zebone.nhis.cn.pub.vo.CnOrdVo;
import com.zebone.nhis.cn.pub.vo.PdStrockVo;
import com.zebone.nhis.cn.pub.vo.QueryCnEmyImgSign;
import com.zebone.nhis.common.module.base.transcode.SysApplog;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.scm.pub.BdPdStore;
import com.zebone.nhis.common.service.EmployeePubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;
/**
 * 医嘱公共服务（医技、手术类医嘱） 
 * @author yangxue
 *
 */
@Service
public class OrderPubService {

	@Resource
	private EmployeePubService employeePubService;
    @Resource
	private InvSettltService invSettltService;
	@Resource
	private BdSnService bdSnService;
	@Resource
	private OrderPubMapper orderPubMapper;
	@Resource
	private IpCgPubService cgService;
	@Resource
	private OrdPrintService ordPrintService;

	private  static  String FLAG_PD="1";//药品
	private  static  String FlAG_lONG="0";//长期
	/**
	 * 获取人员各类处方权标志
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> getEmpPresAuths(String param,IUser user){
		//String pkEmp = JsonUtil.readValue(param, String.class);
		String pkEmp = JsonUtil.getFieldValue(param, "pkEmp");
		return employeePubService.getEmpPresAuths(pkEmp);
	}
	/**
	 * 保存医嘱信息
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> saveOrd(String param,IUser user){
		CnOrdVo paramVo = JsonUtil.readValue(param,CnOrdVo.class);
		if(paramVo==null)
			throw new BusException("未获取到要操作的医嘱信息！");
		List<Object[]> delList = paramVo.getDelList();
		List<CnOrder> insertList = new ArrayList<CnOrder>();
		List<CnOrder> updateList = new ArrayList<CnOrder>();
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(paramVo.getOrdList()!=null&&paramVo.getOrdList().size()>0){
			for(CnOrder ord:paramVo.getOrdList()){
				if("0".equals(ord.getEuStatusOrd())){//只处理开立状态的医嘱
					if(CommonUtils.isEmptyString(ord.getPkCnord())){//新增
						setOrderProperties(ord,user);
						insertList.add(ord);
					}else{//更新
						updateList.add(ord);
					}
				}
			}
		}
		if(insertList!=null&&insertList.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), insertList);
		if(updateList!=null&&updateList.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOrder.class), updateList);
		if(delList!=null&&delList.size()>0){
			DataBaseHelper.getJdbcTemplate().batchUpdate("delete from BL_OP_DT where pk_cnord = ? ", delList);
			DataBaseHelper.getJdbcTemplate().batchUpdate("delete from BL_IP_DT where pk_cnord = ? ", delList);
			DataBaseHelper.getJdbcTemplate().batchUpdate("delete from cn_order where pk_cnord = ? ", delList);
		}

		//重新查询医嘱列表
		if("1".equals(paramVo.getFlagMedOrd())){
			result = this.queryOrdList(param, user);
		}
		//else{
		//	result.addAll(updateList);
		//	result.addAll(insertList);
		//}
		return result;
	}
	/**
	 * 签署医嘱
	 * @param param
	 * @param user
	 */
	public void signOrd(String param,IUser user){
		List<CnOrder> ordList = JsonUtil.readValue(param,new TypeReference<List<CnOrder>>(){});
		if(ordList == null || ordList.size()<=0)
			throw new BusException("未获取到要签署的医嘱信息！");
		 DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd, name_emp_ord=:nameEmpOrd,eu_status_ord = :euStatusOrd , flag_sign = :flagSign , date_sign = :dateSign ,ts=:ts   where pk_cnord = :pkCnord and eu_status_ord ='0' ", ordList);
	}
	/**
	 * 作废医嘱
	 * @param param
	 * @param user
	 */
	public void eraseOrd(String param,IUser user){
		CnOrder ord = JsonUtil.readValue(param,CnOrder.class);
		if(ord == null ||CommonUtils.isEmptyString(ord.getPkCnord()))
			throw new BusException("未获取到要作废的医嘱信息！");
		String sql = " update cn_order set flag_erase='1',flag_erase_chk='1',date_erase =:dateErase,date_erase_chk=:dateErase ,"
				+ "ts = :dateErase,pk_emp_erase=:pkEmp,name_emp_erase=:nameEmp,pk_emp_erase_chk=:pkEmp,name_erase_chk=:nameEmp,eu_status_ord='9' "
				+ " where pk_cnord = :pkCnOrd ";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("dateErase", new Date());
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		paramMap.put("nameEmp", ((User)user).getNameEmp());
		paramMap.put("pkCnOrd", ord.getPkCnord());
		DataBaseHelper.update(sql, paramMap);
		//对已执行医嘱，进行退费
		rtnCg(ord,(User)user);
	}
	/**
	 * 查询医嘱列表（自定义条件）
	 * @param param{ordsnParent,pkPv}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryOrdList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null){
			throw new BusException("未获取到查询医嘱的条件");
		}
		return orderPubMapper.queryOrderList(paramMap);
	}

	/**
	 * 设置医嘱默认属性
	 */
	private void setOrderProperties(CnOrder ord,IUser user){
		ord.setPkCnord(NHISUUID.getKeyId());
		if(ord.getOrdsn()==null){
			int ordsn = bdSnService.getSerialNo("cn_order", "ordsn", 1, user);
			ord.setOrdsn(ordsn);
			if(ord.getOrdsnParent() == null){
				ord.setOrdsnParent(ordsn);
			}
		}
		ord.setDateEnter(new Date());
		ord.setDateStart(new Date());
		ord.setDateEffe(new Date());
		ord.setDays(1L);
		ord.setDescOrd(ord.getNameOrd());
		if(CommonUtils.isEmptyString(ord.getEuAlways())){
			ord.setEuAlways("1");//默认临时医嘱
		}
		//ord.setEuPvtype("3"); 前台设置
		if(CommonUtils.isEmptyString(ord.getFlagBase())){
			ord.setFlagBase("1");//为了不发药记费，默认按基数药处理
		}
		ord.setFlagBl("1");
		ord.setFlagCp("0");
		ord.setFlagDoctor("0");
		ord.setFlagEmer("0");
		ord.setFlagErase("0");
		ord.setFlagEraseChk("0");
		ord.setFlagFirst("0");
		ord.setFlagFit("0");
		ord.setFlagMedout("0");
		ord.setFlagNote("0");
		ord.setFlagPrev("0");
		ord.setFlagPrint("0");
		ord.setFlagSelf("0");
		ord.setFlagSign("0");
		ord.setFlagStop("0");
		ord.setFlagStopChk("0");
		ord.setFlagThera("0");
		ord.setOrds(0L);
		ord.setCreateTime(new Date());
		ord.setCreator(((User)user).getPkEmp());
		ord.setDelFlag("0");
		ord.setTs(new Date());
		//ord.setCodeOrd(codeOrd);
	}
	/**
	 * 作废医嘱时退费
	 * @param ordvo
	 * @param u
	 */
	private void rtnCg(CnOrder ordvo,User u){
		if(ordvo == null || CommonUtils.isEmptyString(ordvo.getPkCnord()))
			return;
		if(!"3".equals(ordvo.getEuStatusOrd()))
			return;
		cgService.refundInBatch(constructRtnCgVo(ordvo,u));
	}
	/**
	 * 构造退费参数
	 */
	private List<RefundVo> constructRtnCgVo(CnOrder ordvo ,User u){
		String cgsql = "select pk_cgip,quan from bl_ip_dt where pk_cnord = ?";
		List<Map<String, Object>> cglist = DataBaseHelper.queryForList(cgsql, ordvo.getPkCnord());
		if(cglist==null||cglist.size()<=0)
			return null;
	    List<RefundVo> rtnlist = new ArrayList<RefundVo>();
	    for(Map<String, Object> cgmap:cglist){
	 		RefundVo vo = new RefundVo();
			vo.setNameEmp(u.getNameEmp());
			vo.setPkCgip(CommonUtils.getString(cgmap.get("pkCgip")));
			vo.setPkDept(u.getPkDept());
			vo.setPkOrg(u.getPkOrg());
			vo.setPkEmp(u.getPkEmp());
			vo.setQuanRe(CommonUtils.getDouble(cgmap.get("quan")));
			rtnlist.add(vo);
	    }
		return rtnlist;
   }

	/**
	 * 打印医嘱时查询医嘱
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> printQryCnOrder(String param , IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		//0表示住院医生站医嘱界面不打印手术医嘱
		String sysparam = ApplicationUtils.getSysparam("CN0063", false, "请维护好系统参数CN0063！");
		map.put("isOperation", sysparam);
		//数据库类型
		String dbType = MultiDataSource.getCurDbType();
		map.put("dbType",dbType);
		//深大需求--修改
		List<Map<String,Object>> orderList = orderPubMapper.printQryCnOrder(map);

		ordPrintService.setImgSign(orderList);
		return orderList;
	}

/***
 * @Description
 * 医嘱打印根据就诊主键查询图片
 * @auther wuqiang
 * @Date 2019-11-08
 * @Param [param, user]
 * @return java.util.List<com.zebone.nhis.cn.pub.vo.QueryCnEmyImgSign>
 */
	public List<QueryCnEmyImgSign> queryCnEmyImgSignList(String param , IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		List<QueryCnEmyImgSign> cnEmyImgSigns=orderPubMapper.queryCnEmyImgSignList(pkPv);
		return  cnEmyImgSigns;

	}
	//查询医嘱关联的执行科室/同一类型的科室
	public List<Map<String, Object>> qryOrdExecDept(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>>  rtn=new ArrayList<Map<String,Object>>();
		
		if(paramMap.get("pkOrd")==null && paramMap.get("dtDepttype")!=null){
			//按科室类型查询
			String sqlStr = "select '0' as flag_def,dept.* from bd_ou_dept dept inner join bd_ou_dept_type type on type.pk_dept =  dept.pk_dept and type.del_flag='0' where type.dt_depttype = ? order by dept.SORTNO desc";
			rtn = DataBaseHelper.queryForList(sqlStr, paramMap.get("dtDepttype").toString());
		}else{
			//按医嘱对应执行科室查询
			rtn=orderPubMapper.qryOrdExecDept(paramMap);
		}
		
		return rtn;
	}

	//查询医嘱关联的执行科室/同一类型的科室---多条医嘱同时查询
	public List<Map<String, Object>> qryOrdExecDepts(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>>  rtn=new ArrayList<Map<String,Object>>();

		if(paramMap.get("pkOrd")!=null){
			rtn=orderPubMapper.qryOrdExecDepts(paramMap);
		}

		return rtn;
	}
	
	/**
	 * 保存访问记录
	 * @param param
	 * @param user
	 */
	public void saveVisitReasons(String param , IUser user){
		SysApplog sysLog=JsonUtil.readValue(param, SysApplog.class);
		if(sysLog==null)
			throw new BusException("未获取到要保存的信息！");
		
		//插入日志
		DataBaseHelper.insertBean(sysLog);
	}
	
	/**
	 * 交易号：004001005023
	 * 查询医嘱CA认证信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOrderCaInfo(String param , IUser user){
		List<String> pkList = JsonUtil.readValue(
				param,
				new TypeReference<List<String>>() {
				});
		
		List<Map<String,Object>> retList = new ArrayList<>();
		
		if(pkList!=null && pkList.size()>0){
			//过滤空字符串
			for(int i =pkList.size() - 1; i >= 0; i--){
				if(CommonUtils.isEmptyString(pkList.get(i))){
					pkList.remove(i);
				}
			}
			
			retList = orderPubMapper.qryqryOrderCaInfo(pkList);
		}
		
		return retList;
	}
	
	public List<Map<String,Object>> qryOrdMes(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		return  orderPubMapper.qryOrdMes(paramMap);
	}
	
	/***
	 * 004001005029
	 * 查询医嘱打印最大页码
	 * @param param
	 * @param user
	 * @return
	 */
	public int queryMaxPageNoForOrderPrint(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)throw new BusException("未传入有效参数");
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT MAX(t.PAGE_NO) FROM CN_ORDER_PRINT t");
		sql.append(" LEFT JOIN CN_ORDER r on r.PK_CNORD=t.PK_CNORD");
		sql.append(" WHERE r.date_enter<to_date(?,'yyyyMMddhh24miss')");
		sql.append(" and r.PK_PV=?");
        sql.append(" and t.EU_ALWAYS=?");
        sql.append(" and t.DEL_FLAG='0'");
        Integer count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class, new Object[]{paramMap.get("endTime"),paramMap.get("pkPv"),paramMap.get("euAlways")});
        return count==null? 0:count;
	}

	/**
	 * 查询医嘱科研标记
	 * @param ordsn
	 * @return
	 */
	public void upFlagKy(List<String> ordsn ){
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("pkOrds",ordsn);
		if(ordsn==null || ordsn.size()==0) return;

		StringBuffer sql= new StringBuffer();
		String dbType = MultiDataSource.getCurDbType();
		if("sqlserver".equals(dbType)){
			sql.append("update CN_ORDER set CN_ORDER.eu_ordtype=bd_ord.eu_ordtype ");
			sql.append("from CN_ORDER INNER JOIN bd_ord on CN_ORDER.PK_ORD = bd_ord.pk_ord ");
			sql.append("where CN_ORDER.ORDSN in ");
			String snS="('";
			for (String sn:
					ordsn) {
				snS = snS+ sn+"','";
			}
			snS=snS.substring(0, snS.length()-2)+")";
			sql.append(snS);
		}else{
			sql.append("update CN_ORDER a set a.eu_ordtype=(select b.eu_ordtype from bd_ord b where b.PK_ORD=a.PK_ORD) ");
			sql.append("where a.ORDSN IN ");
			String snS="('";
			for (String sn:
					ordsn) {
				snS = snS+ sn+"','";
			}
			snS=snS.substring(0, snS.length()-2)+")";
			sql.append(snS);
		}

		DataBaseHelper.update(sql.toString());
	}

	/***
	 * 医嘱项目计费信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOrcSettle(String param,IUser user){
		PdStrockVo vo = JsonUtil.readValue(param, PdStrockVo.class);
		List<String> pkCnords=vo.getPkCnords();
		if(pkCnords==null)
			throw new BusException("医嘱参数不正确");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pkCnords",pkCnords);
		List<Map<String,Object>> settleList=orderPubMapper.qryOrdSettle(map);
		List<Map<String,Object>> rtn=new ArrayList<Map<String,Object>>();
		for (String pkCnord:pkCnords){
			Map<String,Object> mapPk = getMapOrd(pkCnord,settleList);
			rtn.add(mapPk);
		}
		return  rtn;
	}

	public Map<String,Object> getMapOrd(String pkCnord,List<Map<String,Object>> settleList){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkCnord",pkCnord);
		map.put("flagSettle","0");

		if(settleList!=null && settleList.size()>0){
			for (Map<String,Object> vo : settleList){
				if(vo.get("pkCnord")!=null && pkCnord.equals(vo.get("pkCnord").toString()) ){
					String amtS=getPropValueStr(vo,"amt");
					Double amt= amtS==null? 0.0: Double.parseDouble(amtS);
					if(amt>0) map.put("flagSettle","1");
					break;
				}
			}
		}
		return map;
	}

	public List<Map<String,Object>> qryOrdAtt(String param,IUser user){
		PdStrockVo vo = JsonUtil.readValue(param, PdStrockVo.class);
		List<String> pkPds=vo.getPkPds();
		if(pkPds==null)
			throw new BusException("医嘱参数不正确！");
		String codeAttr=vo.getCodeAttr();
		if(StringUtils.isBlank(codeAttr)){
			throw new BusException("附加属性编码参数不正确！");
		}

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pkPds",pkPds);
		map.put("codeAttr",codeAttr);
		List<Map<String,Object>> pdList=orderPubMapper.qryOrdAtt(map);
		List<Map<String,Object>> rtn=new ArrayList<Map<String,Object>>();
		for (String pkPd:pkPds){
			Map<String,Object> mapPk = getMapOrdAttr(pkPd,pdList);
			rtn.add(mapPk);
		}
		return  rtn;
	}

	public Map<String,Object> getMapOrdAttr(String pkPd,List<Map<String,Object>> pdList){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkPd",pkPd);
		map.put("valAtt",null);
		if(pdList!=null && pdList.size()>0 && StringUtils.isNotEmpty(pkPd)){
			for (Map<String,Object> vo : pdList){
				if( StringUtils.isNotEmpty(MapUtils.getString(vo,"pkPd")) && pkPd.equals(MapUtils.getString(vo,"pkPd")) ){
					String valAtt=getPropValueStr(vo,"valAtt");
					map.put("valAtt",valAtt);
					break;
				}
			}
		}
		return map;
	}

	/**
	 * 取文本内容
	 * @param map
	 * @return
	 */
	public static String getPropValueStr(Map<String, Object> map,String key) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		String value="" ;
		if(map.containsKey(key)){
			Object obj=map.get(key);
			value=obj==null?"":obj.toString();
		}
		return value;
	}

	/***
	 * 校验医嘱
	 * @param orderList
	 * @return
	 */
	public boolean CheckOrd(List<CnOrder> orderList){
		boolean ifSucces=true;
		String message="";
		
		//bug 35166 【住院】需求6439，目前医嘱和记费未记录开立医生考勤科室
        String pkDeptJob = invSettltService.getPkDept();
		for (CnOrder vo :orderList){
			//公共
			if(StringUtils.isBlank(vo.getCodeFreq())){ //频次
				ifSucces=false;
				message+="医嘱【"+vo.getNameOrd()+"】频次为空请检查！";
			}

			//长期药品医嘱校验
			if(!StringUtils.isBlank(vo.getFlagDurg()) && FLAG_PD.equals(vo.getFlagDurg())){
				if(StringUtils.isBlank(vo.getCodeSupply())){ //用法
					ifSucces=false;
					message+="医嘱【"+vo.getNameOrd()+"】用法为空请检查！";
				}
			}
			vo.setPkDeptJob(pkDeptJob);
		}
		if(!ifSucces)
			throw new BusException(message);

		return ifSucces;
	}

	public List<Map<String,Object>> qryPdInd(String param,IUser user){
		PdStrockVo vo = JsonUtil.readValue(param,PdStrockVo.class);
		if(vo.getPkPds()==null || vo.getPkPds().size()==0)
			throw new BusException("未获取到物品主键！");

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pkPds",vo.getPkPds());
		map.put("pkHp",vo.getPkHp());
		List<Map<String,Object>> list = null;
		if("2".equals(vo.getPdType())){
			list = orderPubMapper.qryPdHerInd(map);
		}else{
			list = orderPubMapper.qryPdInd(map);
		}

		return list;
	}

	/**
	 * 药品开立控制倍数 004001005051
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdPdStore> qryNumLimit(String param,IUser user){
		List<PdStrockVo> vo = JsonUtil.readValue(param,new TypeReference<List<PdStrockVo>>(){});
		if(vo==null || vo.size()==0){
			throw  new BusException("请传入查询参数");
		}
		String sql=" select * from BD_PD_STORE where pk_pd=? and pk_dept=? ";

		List<BdPdStore> rtn=new ArrayList<BdPdStore>();
		for (PdStrockVo item :vo){
			BdPdStore pdStore = DataBaseHelper.queryForBean(sql, BdPdStore.class, new Object[]{item.getPkPd(),item.getPkDept()});
			if(pdStore!=null)
				rtn.add(pdStore);
		}
		return rtn;
	}

	/**
	 * 根据医嘱主键查询费用信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryBldt(String param,IUser user){
		CnOrder order=JsonUtil.readValue(param,CnOrder.class);
		if(order==null || StringUtils.isEmpty(order.getPkCnord())){
			throw new BusException("请传入需要查询的医嘱信息");
		}

		StringBuilder sql = new StringBuilder("SELECT dt.name_cg,dt.spec,dt.price,dt.quan,dt.amount, unit.name unit_cg,st.date_st,herb.quan single_quan,units.name single_unit from bl_op_dt dt ");
		sql.append(" left join bl_settle st on st.pk_settle=dt.pk_settle ");
		sql.append(" left join bd_unit unit on unit.pk_unit=dt.pk_unit  ");
		sql.append(" left join cn_ord_herb herb on dt.pk_cnord = herb.pk_cnord and dt.flag_pd = 1  and dt.pk_item = herb.pk_pd");
		sql.append(" LEFT JOIN bd_unit units ON units.pk_unit = herb.pk_unit  ");
		sql.append("where dt.pk_cnord= ? order by st.date_st ");
		List<Map<String,Object>> list=DataBaseHelper.queryForList(sql.toString(),new Object[]{order.getPkCnord()});
		return list;
	}

	/**
	 * 根据医嘱主键查询执行信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryExdt(String param,IUser user){
		CnOrder order=JsonUtil.readValue(param,CnOrder.class);
		if(order==null || StringUtils.isEmpty(order.getPkCnord())){
			throw new BusException("请传入需要查询的医嘱信息");
		}
		String sql ="";
		List<Map<String,Object>> list=null;
		if("1".equals(order.getFlagDurg())){ //药品
			StringBuffer sqlPd=new StringBuffer();
			sqlPd.append("select occ.pres_no as code_occ,pddt.quan_pack * pddt.eu_direct as quan_occ,occ.date_pres as date_plan,pddt.date_de as date_occ, ");
			sqlPd.append("emp.name_emp as name_emp_occ,occ.flag_canc,occ.flag_conf ");
			sqlPd.append("from ex_pres_occ_dt dt ");
			sqlPd.append("INNER JOIN EX_PRES_OCC occ on dt.pk_presocc = occ.pk_presocc ");
			sqlPd.append("inner join ex_pres_occ_pddt pddt on dt.pk_presoccdt = pddt.pk_presoccdt ");
			sqlPd.append("left join bd_ou_employee emp on emp.pk_emp = pddt.creator ");
			sqlPd.append("where dt.pk_cnord=? ");
			//sqlPd.append("UNION ");
			//sqlPd.append("select occ.pres_no as code_occ,rtndt.quan_pack * rtndt.eu_direct as quan_occ,occ.date_pres as date_plan,rtndt.date_de as date_occ, ");
			//sqlPd.append("emp.name_emp as name_emp_occ,occ.flag_canc,occ.flag_conf ");
			//sqlPd.append("from ex_pres_occ_dt dt ");
			//sqlPd.append("INNER JOIN EX_PRES_OCC occ on dt.pk_presocc = occ.pk_presocc ");
			//sqlPd.append("inner join ex_pres_occ_pddt pddt on dt.pk_presoccdt = pddt.pk_presoccdt ");
			//sqlPd.append("inner join ex_pres_occ_pddt rtndt on rtndt.pk_presoccdt = pddt.pk_occpddt and rtndt.eu_direct = '-1' ");
			//sqlPd.append("left join bd_ou_employee emp on emp.pk_emp = rtndt.creator ");
			//sqlPd.append("where dt.pk_cnord=? ");
			list=DataBaseHelper.queryForList(sqlPd.toString(),new Object[]{order.getPkCnord()});
		}else{
			sql="select occ.code_occ,occ.quan_occ,occ.date_plan,occ.date_occ,occ.name_emp_occ,occ.flag_canc,occ.flag_occ as flag_conf from EX_ASSIST_OCC occ where occ.pk_cnord= ?";
			list=DataBaseHelper.queryForList(sql,new Object[]{order.getPkCnord()});
		}

		return list;
	}

	public List<Map<String, Object>> qryPdHp(String param,IUser user){
		PdStrockVo vo = JsonUtil.readValue(param,PdStrockVo.class);
		if(vo==null || vo.getPkPds()==null || vo.getPkPds().size()==0 || vo.getPkHp()==null){
			return null;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pkPds",vo.getPkPds());
		map.put("pkHp",vo.getPkHp());
		List<Map<String,Object>> list = orderPubMapper.qryPdIph(map);
		return list;
	}

	/**
	 * 药理分类查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdPdVo> qryPdType(String param,IUser user){
		List<PdStrockVo> list = orderPubMapper.qryPdType();
		if(list==null || list.size()==0) return null;
		List<BdPdVo> childrenList = getChildren(list,null);

		return childrenList;
	}

	/**
	 * 查找子节点
	 * @param list
	 * @param father
	 * @return
	 */
	private List<BdPdVo> getChildren(List<PdStrockVo> list,String father){
		if(list==null || list.size()==0) return null;
		//查询所有符合的数据
		List<PdStrockVo> listC=new ArrayList<PdStrockVo>();
		for(PdStrockVo vo : list){
			//第一层级必须全为空，其他层级则需保证父节点一致
			if(StringUtils.isEmpty(father) && StringUtils.isEmpty(vo.getfCode()) || StringUtils.isNotEmpty(father) && father.equals(vo.getfCode())){
				listC.add(vo);
			}
		}
		if(listC==null || listC.size()==0) return null;
		//存在节点
		List<BdPdVo> childrenList=new ArrayList<BdPdVo>();
		for(PdStrockVo vo : listC){
			BdPdVo item=new BdPdVo();
			item.setCode(vo.getCode());
			item.setName(vo.getName());
			item.setfCode(vo.getfCode());
			item.setChildren(getChildren(list,vo.getCode()));
			childrenList.add(item);
		}
		return childrenList;
	}

	/**
	 * 用于展示药理分类下的药品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryPdByPharm(String param,IUser user){
		PdStrockVo vo = JsonUtil.readValue(param,PdStrockVo.class);
		if(vo==null || (StringUtils.isEmpty(vo.getDtPharm()) && StringUtils.isEmpty(vo.getPkPd()))){
			throw new BusException("请传入相关参数！！！");
		}

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pkPd",vo.getPkPd());
		map.put("dtPharm",vo.getDtPharm());
		map.put("pkDept",vo.getPkDept());
		List<Map<String,Object>> list = orderPubMapper.qryPd(map);
		return list;
	}
}
