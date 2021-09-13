package com.zebone.nhis.bl.hd.service;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zebone.nhis.arch.support.ArchUtil;
import com.zebone.nhis.bl.hd.dao.HdCgMapper;
import com.zebone.nhis.bl.pub.dao.CgQryMaintainMapper;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubPriceVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.bl.hd.vo.CostVo;
import com.zebone.nhis.bl.hd.vo.RefundVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyBl;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvHd;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.service.QueryRemainFeeService;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 透析计费服务
 * @author IBM
 *
 */
@Service
public class HdCgService {
	@Autowired
	public HdCgMapper hdCgMapper;

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
	
	@Autowired
	private OpCgPubService opCgPubService;
	
	/**
	 * 查询费用与余额
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryCostBalance(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null ||CommonUtils.isNull(map.get("pkPi"))||CommonUtils.isNull(map.get("pkPv")))
			throw new BusException("未获取到要查询的患者信息");
		//查询余额与押金
		List<Map<String,Object>> amtAcc=hdCgMapper.queryAmtAcc(map);
		//查询未结费用
		List<Map<String,Object>> useAmt=hdCgMapper.queryUseAmt(map);
		
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> mapResult=new HashMap<String,Object>();
		//账户余额=余额-押金
		Double amt1=(double)0;
		Double depo=(double)0;		
		if(amtAcc!=null && amtAcc.size()!=0 && !CommonUtils.isNull(amtAcc.get(0).get("amtAcc")))
			amt1=Double.parseDouble(amtAcc.get(0).get("amtAcc").toString());
		if(amtAcc!=null && amtAcc.size()!=0 && !CommonUtils.isNull(amtAcc.get(0).get("depo")))
			depo=Double.parseDouble(amtAcc.get(0).get("depo").toString());
		Double amt=amt1-depo;
		mapResult.put("amtAcc", amt);
		//未结费用
		Double amount=(double)0;		
		if(useAmt!=null && useAmt.size()!=0 && useAmt.get(0)!=null && !CommonUtils.isNull(useAmt.get(0).get("amount")))
			amount=Double.parseDouble(useAmt.get(0).get("amount").toString());
		mapResult.put("amount", amount);
		result.add(mapResult);
		
		return result;
	}
	
	/**
	 * 计费处理
	 * @param param
	 * @param user
	 */
	public void BillingProcessing(String param,IUser user){
		List<BlPubParamVo> blCgPubParamVos = JsonUtil.readValue(param, new TypeReference<List<BlPubParamVo>>(){});
//		if(blCgPubParamVos!=null && blCgPubParamVos.size()>0)
//			opCgPubService.blOpCgBatch(blCgPubParamVos);
		
		boolean isAllowQF=false;
		// 1.校验数据
		try {
			checkBlCgPubParamVo(blCgPubParamVos);
		} catch (BusException e) {
			throw new BusException("传入的记费数据有问题" + e.getMessage());
		}
		//计费编码  0601门诊计费
		String codeCg=ApplicationUtils.getCode("0601");
		List<BlOpDt> vosForInsert = new ArrayList<BlOpDt>();
		List<BlPubPriceVo> priceVos = new ArrayList<BlPubPriceVo>();
		Map<String,List<BlOpDt>> diagMap = new HashMap<String,List<BlOpDt>>();
		for (BlPubParamVo pvo : blCgPubParamVos) {
			BlOpDt vo = new BlOpDt();
			String pkPv = pvo.getPkPv();
			// 3. 取pv相关信息、以及物品价格信息
			PvEncounter pvvo = DataBaseHelper.queryForBean(
								"select * from pv_encounter where pk_pv = ?",
								PvEncounter.class, pkPv);
						
			String pkCgop = NHISUUID.getKeyId();
			vo.setPkCgop(pkCgop);
			vo.setSpec(pvo.getSpec());
			vo.setFlagPv(pvo.getFlagPv());
			vo.setFlagAcc("0");
			vo.setFlagRecharge("0");
			vo.setEuAdditem("0");
			vo.setRatioAdd(0.0);
			vo.setAmountAdd(0.0);
			vo.setAmountHppi(pvo.getAmount());
			vo.setPriceCost(pvo.getPrice());
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pkItem", pvo.getPkItem());
			map.put("pkOrg", pvo.getPkOrg());
			map.put("euType", Constant.OPINV);
			map.put("flagPd", pvo.getFlagPd());
			Map<String, Object> resBill = qryBillCodeByPkItem(map);
			if(resBill==null || resBill.get("code")==null)
				throw new BusException("调用门诊挂号记费时，【"+vo.getNameCg()+"】未获取到对应账单码，请检查！");
			
			if(resBill!=null){
				vo.setCodeBill(resBill.get("code")==null?"":(String)resBill.get("code"));
			}
			
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
			vo.setPkEmpApp(CommonUtils.isEmptyString(pvo.getPkEmpApp())?pvvo.getPkEmpPhy():pvo.getPkEmpApp());
			vo.setNameEmpApp(CommonUtils.isEmptyString(pvo.getNameEmpApp())?pvvo.getNameEmpPhy():pvo.getNameEmpApp());
			// 7.13 pk_org_ex pk_dept_ex
			vo.setPkOrgEx(pvo.getPkOrgEx());
			vo.setPkDeptEx(CommonUtils.isEmptyString(pvo.getPkDeptEx())?pvvo.getPkDept():pvo.getPkDeptEx());
			// 7.14 pk_dept_cg pk_emp_cg name_emp_cg
			vo.setPkDeptCg(CommonUtils.isEmptyString(pvo.getPkDeptCg())?pvvo.getPkDept():pvo.getPkDeptCg());
			vo.setPkEmpCg(CommonUtils.isEmptyString(pvo.getPkEmpCg())?UserContext.getUser().getPkEmp() : pvo.getPkEmpCg());
			vo.setNameEmpCg(CommonUtils.isEmptyString(pvo.getNameEmpCg())?UserContext.getUser().getNameEmp() : pvo.getNameEmpCg());//当前用户姓名；
			vo.setPkEmpEx(CommonUtils.isEmptyString(pvo.getPkEmpEx())?UserContext.getUser().getPkEmp() : pvo.getPkEmpEx());
			vo.setNameEmpEx(CommonUtils.isEmptyString(pvo.getNameEmpEx())?UserContext.getUser().getNameEmp() : pvo.getNameEmpEx());//当前用户姓名；
			
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
			// 7.6 结算主键 ， pk_settle 退费关联计费主键 pk_cgop_back code_cg 记费编码
			vo.setPkCgopBack(null);
			vo.setFlagSettle("0");
			vo.setCodeCg(codeCg);
			// 7.7 设置单价，数量，金额，比例等信息
			// price_org,price,quan,amount,pk_disc,ratio_disc,ratio_self,amount_pi
						// 调用价格策略接口取当前单价，原始单价 以及自付比
			List<BlOpDt> vos = new ArrayList<BlOpDt>();
			if(StringUtils.hasText(pvo.getPkItem())){
				priceVos = priceStrategyService.getItemPriceAllInfo(pvo.getPkItem(),
									pvo.getFlagPd(),pvo.getPrice(), pvvo, pvo.getDateHap());
				vos = dealPriceInfo(pvo.getQuanCg(), priceVos, vo);
			}else if(StringUtils.hasText(pvo.getPkOrd())){
				vos = getFromOrd(pvvo, pvo, vo);				
			}else if(StringUtils.hasText(pvo.getPkCnord())){
				CnOrder cnorder = DataBaseHelper.queryForBean(
									"select pk_ord from cn_order where pk_cnord = ?",
									CnOrder.class, pvo.getPkCnord());
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
			
			vosForInsert.addAll(vos);	
			
			/*// 8.此处校验所有控费的患者是否欠费
			if(pvInfoPubService.isLimiteFee(pkPv)){
				if(!isAllowQF){
					if (!queryFeeServcie.isArrearage(pkPv, pvvo.getPkInsu(),null)) {
						throw new BusException(pvvo.getNamePi() + "患者已欠费！");
					}
				}	
     		}*/
			
			// 单病种控费病人，添加控费集合
			if("2".equals(pvvo.getEuPvmode())){
				List<BlOpDt> bllist =  diagMap.get(pvvo.getPkPv());
				if(bllist == null || bllist.size()<=0){
					diagMap.put(pvvo.getPkPv(), vos);
				}else{
					bllist.addAll(vos);
					diagMap.put(pvvo.getPkPv(),bllist);
				}			
			}
				
		}
		for(BlOpDt vo : vosForInsert){
			ApplicationUtils.setDefaultValue(vo, true);
		}
		// 8,批量写入记费表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class),
						vosForInsert);
	}
	
	/**
	 * 根据收费项目主键查询账单码
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryBillCodeByPkItem(Map<String, Object> mapParam) {

		StringBuffer sql = new StringBuffer();
		sql.append("select distinct invitem.code from bd_invcate_item invitem");
		sql.append(" inner join bd_invcate_itemcate cate on invitem.pk_invcateitem=cate.pk_invcateitem");
		sql.append(" inner join bd_invcate inv on invitem.pk_invcate=inv.pk_invcate ");
		if (BlcgUtil.converToTrueOrFalse(mapParam.get("flagPd").toString())) {
			sql.append(" inner join bd_pd item on cate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_pd = '"+mapParam.get("pkItem")+"' ");
		} else {
			sql.append(" inner join bd_item item on cate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_item = '"+mapParam.get("pkItem")+"' ");
		}
		sql.append(" and inv.eu_type = ? and invitem.del_flag ='0' and invitem.pk_org=? and cate.del_flag ='0' and inv.del_flag='0' and item.del_flag='0' ");
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(),
				new Object[] {mapParam.get("euType"), mapParam.get("pkOrg") });
		return mapResult;
	}
	/**
	 * 处理item信息
	 * 
	 * @param vos
	 *            目标对象
	 */
	private void dealItemInfo(List<BlOpDt> vos) {
		List<BdItem> items = hdCgMapper.getBdItemsByCon(vos);
		Map<String, BdItem> itemMap = new HashMap<String, BdItem>();
		for (BdItem item : items) {
			itemMap.put(item.getPkItem(), item);
		}

		for (BlOpDt vo : vos) {
			BdItem item = itemMap.get(vo.getPkItem());
			vo.setPkUnit(item.getPkUnit());
			vo.setPkItemcate(item.getPkItemcate());
			if(CommonUtils.isEmptyString(vo.getSpec())){
				vo.setSpec(item.getSpec());
			}
			vo.setNameCg(item.getName());
		}

	}
	
	private List<BlOpDt> getFromOrd(PvEncounter pvvo, BlPubParamVo pvo, BlOpDt vo) {
		List<BlOpDt> vos = new ArrayList<BlOpDt>();
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
	
	private List<BlOpDt> dealPriceInfo(double quanCg,
			List<BlPubPriceVo> priceVos, BlOpDt vo) {
		return dealPriceInfo(quanCg, priceVos, vo,false);
	}
	
	/**
	 * 处理价格信息的设置
	 * 
	 * @param pvo
	 *            参数
	 * @param priceVos
	 *            价格信息
	 * @param vo
	 *            目标对象
	 * @return
	 */
	private List<BlOpDt> dealPriceInfo(double quanCg,
			List<BlPubPriceVo> priceVos, BlOpDt vo,boolean isUpdate) {
		List<BlOpDt> res = new ArrayList<BlOpDt>();
		for (BlPubPriceVo priceVo : priceVos) {			
			//BlOpDt baseVo = (BlOpDt) vo.clone("");
			BlOpDt baseVo = (BlOpDt) vo.clone("");
			Double quanChild = priceVo.getQuanChild().doubleValue()>0?MathUtils.mul(priceVo.getQuanChild().doubleValue(), 1.0):1.0;
			if(!isUpdate){
				baseVo.setPkCgop(NHISUUID.getKeyId());
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
	 * @param vo目标对象
	 * @param pvo参数对象
	 */
	private void dealPdInfo(BlOpDt vo, BlPubParamVo pvo) {
		String pkPd = pvo.getPkItem();
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
		vo.setPackSize(new Integer(pvo.getPackSize()));
		vo.setPriceCost(pvo.getPriceCost());
		vo.setPkUnit(pvo.getPkUnitPd());
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
				fieldName.add("pkItem");
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
	
	/**
	 * 
	 * @param vo目标对象
	 * @param pvo参数对象
	 */
	private void dealPdInfo(BlIpDt vo, BlPubParamVo pvo) {
		String pkPd = pvo.getPkItem();
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
	 * 退费处理
	 * @param param
	 * @param user
	 */
	public void RefundProcessing(String param,IUser user){		
		List<RefundVo> params = JsonUtil.readValue(param, new TypeReference<List<RefundVo>>(){});
		refundInBatch(params);
	}
	
	public BlPubReturnVo refundInBatch(List<RefundVo> params) {
		if(params == null || params.size()<=0)
			throw new BusException("未获取到需要退费的记费信息！");
		BlPubReturnVo res = new BlPubReturnVo();
		Map<String,Object> paraMap = new HashMap<String,Object>();
		StringBuffer pkCgops = new StringBuffer("'");
		for(int i = 0;i<params.size();++i){
			RefundVo vo = params.get(i);
			paraMap.put(vo.getPkCgop(), vo);
			pkCgops.append(vo.getPkCgop());
			pkCgops.append("','");
		}
		//退费校验-防止并发
		pkCgops.append("'");
		String sql = "select count(1) from bl_op_dt  where  flag_settle='1' and del_flag = '0' and pk_cgop in (" + pkCgops.toString() + ")";
		Integer count = DataBaseHelper.queryForScalar(sql, Integer.class);
		if(count > 0){	
			throw new BusException("退费列表存在已收费项目，无法退费，请刷新界面！");
		}
		
		List<BlOpDt> vos  = hdCgMapper.QryBlDtOpByRefunds(params);
		List<BlOpDt> updateResVos = new ArrayList<BlOpDt>();
		Set<String> deleteResVos = new HashSet<String>();
		//退费逻辑变更-如果退费数量=计费数量，删除计费明细；退费数量<计费数量，修改已计费明细。
		for(int i = 0;i < vos.size();++i){
			BlOpDt vo = (BlOpDt) vos.get(i);
			RefundVo refundVo = (RefundVo)paraMap.get(vo.getPkCgop());
			//退费数量=计费数量
			if(MathUtils.equ(vo.getQuan(), refundVo.getQuanRe())){
				deleteResVos.add(vo.getPkCgop());
			}else if(MathUtils.compareTo(vo.getQuan(), refundVo.getQuanRe()) > 0){//退费数量<计费数量
				vo.setQuan(MathUtils.sub(vo.getQuan(), refundVo.getQuanRe()));
				vo.setAmount(MathUtils.mul(vo.getPrice(), vo.getQuan()));
				vo.setAmountHppi(MathUtils.mul(MathUtils.mul(vo.getPrice(), vo.getQuan()),vo.getRatioSelf()));
				Double temp = MathUtils.mul(vo.getAmount(), vo.getRatioDisc());
				Double amtPi = MathUtils.mul(temp, vo.getRatioSelf());
				vo.setAmountPi(amtPi);
				//vo.setAmountAdd(?);
				updateResVos.add(vo);
			}else{
				throw new BusException("退费数量有误，请检查！");
			}
		}
		
		//更新计费明细
		if(updateResVos.size()>0)
		{
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlOpDt.class),
					updateResVos);		
		}
		
		//删除计费明细
		if(deleteResVos.size()>0)
		{
			String deleteWhere = " pk_cgop in (" + ArchUtil.convertSetToSqlInPart(deleteResVos, "pk_cgop") + ")";
			DataBaseHelper.deleteBeanByWhere(new BlOpDt(), deleteWhere);
		}
		
		res.setBods(updateResVos);
		return res;
		/*	
		if(params == null || params.size()<=0)
			throw new BusException("未获取到需要退费的记费信息！");
		BlPubReturnVo res = new BlPubReturnVo();
		Map<String,Object> paraMap = new HashMap<String,Object>();
		String codeCg = ApplicationUtils.getCode("0601");
		
		for(int i = 0;i<params.size();++i){
			RefundVo vo = params.get(i);
			paraMap.put(vo.getPkCgop(), vo);
		}
		List<BlOpDt> vos  = hdCgMapper.QryBlDtOpByRefunds(params);	
		List<BlIpDt> resVos = new ArrayList<BlIpDt>();
		for(int i = 0;i<vos.size();++i){
			BlOpDt vo = (BlOpDt) vos.get(i);
			RefundVo refundVo = (RefundVo)paraMap.get(vo.getPkCgop());
			//1.1设置退费主键
			vo.setPkCgopBack(vo.getPkCgop());
			vo.setCodeCg(codeCg);
			vo.setDateCg(new Date());//当前日期时间；
			vo.setPkDeptCg(UserContext.getUser().getPkDept());//当前部门；
			vo.setPkEmpCg(UserContext.getUser().getPkEmp());//当前用户
			vo.setNameEmpCg(UserContext.getUser().getNameEmp());//当前用户姓名；
			vo.setPkCgop(NHISUUID.getKeyId());
			//1.2设置费用
			vo.setQuan(MathUtils.mul(refundVo.getQuanRe(), -1.0));//退费数量*-1
			vo.setAmount(MathUtils.mul(vo.getPrice(), vo.getQuan()));
			vo.setAmountHppi(MathUtils.mul(MathUtils.mul(vo.getPrice(), vo.getQuan()),vo.getRatioSelf()));
			//amount_pi，amount_hppi-[price_org*(1-ratio_disc)*quan]，计算结果小于0时为0；
	        //Double amt = MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(MathUtils.mul(vo.getPriceOrg(), MathUtils.sub(1D, vo.getRatioDisc())), vo.getQuan()));
			//if(MathUtils.compareTo(amt, 0D)<0){
				//vo.setAmountPi(0D);
			//}else{
				//vo.setAmountPi(amt);
			//}
			Double temp = MathUtils.mul(vo.getAmount(), vo.getRatioDisc());
			Double amtPi = MathUtils.mul(temp, vo.getRatioSelf());
			vo.setAmountPi(amtPi);
			vo.setFlagSettle("0");
			vo.setFlagInsu("0");
			
			//1.3
			resVos.add(vo);
		} 
		
		
		for(BlOpDt vo : resVos){
	        ApplicationUtils.setDefaultValue(vo, true);
		}
		//3批量写入记费表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class),
				resVos);
		
		//查询患者医保扩展属性
		String varAttr = cgQryMaintainMapper.qryHpValAttr(resVos.get(0).getPkPv());
		//当医保扩展属性‘0301’为1,操作表ins_gzgy_bl
		if(!CommonUtils.isEmptyString(varAttr) && "1".equals(varAttr)){
			List<InsGzgyBl> gyBlList = constructInsGzgyBl(resVos);	//组装计费信息到公医计费明细
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsGzgyBl.class),	//批量写入公医计费明细
							gyBlList);
		}

		// 9.处理返回。
		res.setBods(updateResVos);
		return res;
		*/
		
	}
	
	private List<InsGzgyBl> constructInsGzgyBl(List<BlOpDt> blopList){
		List<InsGzgyBl> gyBlList = new ArrayList<>();
		for(BlOpDt dt : blopList){
			InsGzgyBl gyBl = new InsGzgyBl();
			gyBl.setPkGzgybl(NHISUUID.getKeyId());
			gyBl.setPkCnord(dt.getPkCnord());		 //关联医嘱主键
			gyBl.setPkCg(dt.getPkCgop()); 	//关联的记费(bl_ip_dt)主键
			gyBl.setPkPv(dt.getPkPv()); 	//关联的患者就诊
			gyBl.setEuItemtype(dt.getFlagPd());		 //0非药品，1药品
			gyBl.setPkItem(dt.getPkItem());	//关联的收费项目或药品项目主键
			gyBl.setPkItemcate(dt.getPkItemcate());  //关联的收费项目分类
			gyBl.setNameCg(dt.getNameCg());	//收费名称
			gyBl.setSpec(dt.getSpec()); 	//规格
			gyBl.setPrice(dt.getPrice()); 	//单价
			gyBl.setQuan(dt.getQuan()); 	//数量
			gyBl.setAmount(dt.getAmount()); //总金额
			gyBl.setRatio(dt.getRatioSelf());		//患者自付比例
			gyBl.setAmountPi(dt.getAmountHppi()); 	//患者自付金额
			gyBl.setAmountHp(MathUtils.sub(gyBl.getAmount(), gyBl.getAmountPi()));	//医保支付金额
			gyBl.setAmountUnit(new Double(0)); 		//单位支付金额
			//获取当前时间yyyy-MM-dd
			Date dateHap = DateUtils.strToDate(DateUtils.getDateTime(),"yyyy-MM-dd");
			gyBl.setDateHap(dateHap);		//费用发生日期
			gyBl.setDateCg(new Date()); 	//记费日期
			
			gyBlList.add(gyBl);
		}
		
		return gyBlList;
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
		//String pkDept = UserContext.getUser().getPkDept();
		//此处修改是因为处理bug-20401【透析业务处理】记费保存写bl_op_dt表时，pd_dept_ex的值保存的是治疗区
		//String pkDept = (String)paraMap.get("pkDeptNs");
		String pkDept = (String)paraMap.get("pkDept");//20191114--pd_dept_ex保存的是透析科室
		String flagRtn = null == paraMap.get("flagRtn") ? "0" : (String)paraMap.get("flagRtn");
		StringBuffer sql = new StringBuffer(" select * from ( select blop.date_cg, blop.name_cg, blop.price_org,blop.price,blop.flag_settle,");
		sql.append(" blop.quan + (CASE WHEN back.quan is null THEN 0 ELSE back.quan END) as quan ,");
		sql.append(" blop.amount + (CASE WHEN back.amt is null THEN 0 ELSE back.amt END) as amount,");
		sql.append(" blop.amount + (CASE WHEN back.amt is null THEN 0 ELSE back.amt END) as oldAmount,");
		sql.append(" blop.pk_cgop from bl_op_dt blop left outer join ");
		sql.append(" (select sum(quan) quan,sum(amount) amt,pk_cgop_back");
		sql.append(" from bl_op_dt where  quan < 0  and pk_pv = ?");
		if("0".equals(flagRtn)){
			sql.append(" and flag_settle = 0  ");//2018-12-07 是否可退？ 不可退时添加未结算过滤，否则不添加
		}
		if("1".equals(deptType)){
			sql.append(" and pk_dept_ex = ?");
		}
		
		sql.append(" group by pk_cgop_back) back on blop.pk_cgop=back.pk_cgop_back");
		sql.append(" where blop.pk_pv = ? and  blop.quan > 0 ");
		if("1".equals(deptType)){
			sql.append(" and blop.pk_dept_ex = ? ");
		}
		if("0".equals(flagRtn)){
			sql.append(" and blop.flag_settle = 0  ");//2018-12-07 是否可退？ 不可退时添加未结算过滤，否则不添加
		}
		sql.append ( ") temp where temp.quan > 0 order by temp.date_cg desc");
		if("1".equals(deptType)){
			return  DataBaseHelper.queryForList(sql.toString(),pkPv,pkDept,pkPv,pkDept);
		}else{
			return  DataBaseHelper.queryForList(sql.toString(),pkPv,pkPv);
		  }
		
	}
	
	/**
	 * 查询费用信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryCost(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null ||CommonUtils.isNull(map.get("pkPv")))
			throw new BusException("未获取到要查询的患者信息");
		List<Map<String,Object>> result=hdCgMapper.queryVisCost(map);
		return result;
	}
	
	/**
	 * 查询结算费用分类
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> querySettlement(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null ||CommonUtils.isNull(map.get("pkPv")))
			throw new BusException("未获取到要查询的患者信息");
		List<Map<String,Object>> result=hdCgMapper.queryCostClassification(map);
		return result;
	}
	
	/**
	 * 查询结算费用明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryDetailed(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null ||CommonUtils.isNull(map.get("pkPv")))
			throw new BusException("未获取到要查询的患者信息");
		List<Map<String,Object>> result=hdCgMapper.queryCostDetailed(map);
		return result;
	}
	/**
	 * 结算处理
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> saveSettlement(String param,IUser user){
		BlSettle bl = JsonUtil.readValue(param, BlSettle.class);
		if(bl==null ||CommonUtils.isNull(bl.getPkPi())||CommonUtils.isNull(bl.getPkPv()))
			throw new BusException("未获取到要结算的信息");
		Map<String,Object> map=new HashMap<String,Object>();
		/**
		 * 判断患者是否欠费
		 */
		map.put("pkPi", bl.getPkPi());
		map.put("pkPv", bl.getPkPv());
		//查询余额与押金
		List<Map<String,Object>> amtAcc=hdCgMapper.queryAmtAcc(map);
		//查询未结费用
		List<Map<String,Object>> useAmt=hdCgMapper.queryUseAmt(map);
		//查询患者账号编码
		String sqlCode="select acc.pk_piacc,acc.pk_pi,acc.code_acc,acc.amt_acc,card.card_no,acc.eu_status from pi_acc acc inner join pi_card card on acc.pk_pi=card.pk_pi where acc.pk_pi=? and card.EU_STATUS<'2'";
		List<Map<String,Object>>  codeAccList=DataBaseHelper.queryForList(sqlCode,map.get("pkPi").toString());
		if(codeAccList.size()>0){
			if("2".equals(codeAccList.get(0).get("euStatus").toString())){
				throw new BusException("患者账户已冻结，不能结算！");
			}
			if("9".equals(codeAccList.get(0).get("euStatus").toString())){
				throw new BusException("患者账户已作废，不能结算！");
			}
		} else {
			throw new BusException("未查询到患者账户，不能结算！");
		}
				
		//账户余额=余额-押金
		Double amt1=(double)0;
		Double depo=(double)0;		
		if(amtAcc!=null && amtAcc.size()!=0 && !CommonUtils.isNull(amtAcc.get(0).get("amtAcc")))
			amt1=Double.parseDouble(amtAcc.get(0).get("amtAcc").toString());
		if(amtAcc!=null && amtAcc.size()!=0 && !CommonUtils.isNull(amtAcc.get(0).get("depo")))
			depo=Double.parseDouble(amtAcc.get(0).get("depo").toString());
		Double amt=amt1-depo;
		//未结费用
		Double amount=(double)0;		
		if(useAmt!=null && useAmt.size()!=0 && useAmt.get(0)!=null && !CommonUtils.isNull(useAmt.get(0).get("amount")))
			amount=Double.parseDouble(useAmt.get(0).get("amount").toString());

		if(amount>amt)
			throw new BusException("账户余额不足，不能结算！");
		//查询患者费用明细
		List<CostVo> result=hdCgMapper.queryPvCost(map);
		if(result!=null){			
			bl.setAmountSt(result.get(0).getAmount());
			bl.setAmountInsu(result.get(0).getAmountHppi());
			bl.setAmountAdd(result.get(0).getAmountAdd());
			bl.setAmountPi(result.get(0).getAmountPi());
		}
		
		//写表bl_settle
		DataBaseHelper.insertBean(bl);
		
		BlSettleDetail blde=new BlSettleDetail();
		blde.setPkSettle(bl.getPkSettle());
		blde.setPkInsurance(bl.getPkInsurance());
		List<Map<String,Object>> mapPayer=DataBaseHelper.queryForList("select * from bd_defdoc where CODE_DEFDOCLIST='110100' and code='4'");
		
		if(mapPayer!=null && mapPayer.size() > 0 && mapPayer.get(0).get("pkDefdoc")!=null){
			blde.setPkPayer(mapPayer.get(0).get("pkDefdoc").toString());
		}else{
			throw new BusException("缺少患者账户付款方式-分类编码：110100；字典编码：4");
		}
		blde.setAmount(Double.valueOf(bl.getAmountSt().toString()));
		//写表bl_settle_detail
		DataBaseHelper.insertBean(blde);
		
		//bl_op_dt  更新结算主键与标志
		map.put("pkSettle", bl.getPkSettle());
		String sql="update bl_op_dt set flag_settle='1',pk_settle=:pkSettle where  pk_pv=:pkPv and flag_settle='0'";
		DataBaseHelper.update(sql,map);	
		
		//生成支付信息
		BlDeposit bldepo=new BlDeposit();
		bldepo.setPkOrg(bl.getPkOrg());
		bldepo.setEuDptype("0");
		bldepo.setEuPvtype("1");
		bldepo.setEuDirect("1");
		bldepo.setPkPi(bl.getPkPi());
		bldepo.setPkPv(bl.getPkPv());
		bldepo.setAmount(bl.getAmountInsu());
		bldepo.setDtPaymode("4");
		if(codeAccList!=null && codeAccList.size()>0)
			bldepo.setPayInfo(codeAccList.get(0).get("cardNo").toString());
		bldepo.setDatePay((new Date()));
		bldepo.setPkDept(((User)user).getPkDept());
		bldepo.setPkEmpPay(((User)user).getPkEmp());
		bldepo.setNameEmpPay(((User)user).getNameEmp());
		//写表bl_deposit
		DataBaseHelper.insertBean(bldepo);
		
		//写表pi_acc
		BigDecimal amountAcc=BigDecimal.valueOf(amt1-bl.getAmountSt().doubleValue());
		map.put("amtAcc", amountAcc);
		map.put("pkPiacc", codeAccList.get(0).get("pkPiacc").toString());
		String accSql="update pi_acc set amt_acc=:amtAcc where pk_piacc=:pkPiacc ";
		DataBaseHelper.update(accSql,map);	
		
		//写表pi_acc_detail
		PiAccDetail piAcc=new PiAccDetail();
		piAcc.setPkOrg(((User)user).getPkOrg());
		piAcc.setPkPiacc(map.get("pkPiacc").toString());
		piAcc.setPkPi(bl.getPkPi());
		piAcc.setPkPv(bl.getPkPv());
		piAcc.setDateHap((new Date()));
		piAcc.setEuOptype("2");
		piAcc.setEuDirect("-1");
		piAcc.setAmount(bl.getAmountSt().negate());
		piAcc.setNote("透析结算");
		//piAcc.setAmtBalance(BigDecimal.valueOf(amt1));
		piAcc.setAmtBalance(amountAcc);
		piAcc.setPkDepopi(bldepo.getPkDepo());
		piAcc.setPkEmpOpera(((User)user).getPkEmp());
		piAcc.setNameEmpOpera(((User)user).getNameEmp());
		DataBaseHelper.insertBean(piAcc);
				
		//更新支付主键与标志
		sql="update bl_op_dt set flag_acc='1',pk_acc=:pkPiacc where  pk_pv=:pkPv and flag_settle='0'";
		DataBaseHelper.update(sql,map);
		
		
		return null;
	}
	
}
