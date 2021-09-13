package com.zebone.nhis.ma.pub.platform.receive.yh.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.service.BlPrePayService;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubPriceVo;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.receive.yh.dao.CnMsgMapper;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.PatiCgParamVo;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.AdtRegister.AdtRegisterInfo;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.CancleSettle.CancleSettleInfo;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.PatiCg.PatiCgInfo;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.PrePay.PrePayInfo;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.StrikeSettle.StrikeSettleInfo;
import com.zebone.nhis.pv.pub.vo.AdtRegParam;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

@Service
public class IPHandleService {
	@Resource
	private CommonService commonService;
	@Autowired
	private BalAccoutService balAccoutService;
	@Autowired
	private BlPrePayService blPrePayService;
	@Resource
	private BlIpPubMapper blIpPubMapper;
	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	@Autowired
	private PriceStrategyService priceStrategyService;
	@Resource
	private CnMsgMapper cnMsgMapper;
	@Resource
	private IpCgPubService ipCgPubService;
	/**
	 * 保存入院登记信息
	 * @param adtInfo
	 * @param loginUser
	 */
	@Transactional
	public void saveIpAdtRegInfo(AdtRegisterInfo adtInfo, User loginUser) {
		if("~".equals(loginUser.getPkOrg().trim())){
			loginUser.setPkOrg("89ace0e12aba439991c0eb001aaf02f7"); 
		}
		//组建入院登记接口信息
		AdtRegParam createAdtRegParam = createAdtRegParam(adtInfo,loginUser);
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson adtRegInfo = apputil.execService("PV", "PvAdtPubService", "saveAdtReg",createAdtRegParam,loginUser);
		BlDeposit bl=DataBaseHelper.queryForBean("select * from BL_DEPOSIT where pk_pv=?", BlDeposit.class, adtInfo.getINHOSPNO());
		if(bl!=null){
			if(bl.getPkPi()==null||bl.getPkPi()==""){
			PvEncounter pv=DataBaseHelper.queryForBean("select * from pv_encounter where code_pv=?", PvEncounter.class, adtInfo.getINHOSPNO());
			DataBaseHelper.update("update bl_deposit set pk_pv=?,pk_pi=? where PK_DEPO=?", pv.getPkPv(),pv.getPkPi(),bl.getPkDepo());
			}
		}
		if(adtRegInfo.getStatus()<0){
			throw new BusException(adtRegInfo.getDesc());
		}
	}
	
	/**
	 * 住院预交金退缴
	 * @param preInfo
	 * @param user
	 * @return 
	 */
	@Transactional
	public void savePrePayInfo(PrePayInfo preInfo, User user) {
		//组建住院预交金缴退信息
		BlDeposit blDeposit = createPrePayInfo(preInfo,user);

		//校验预缴金票据号是否重复
        if("1".equals(blDeposit.getEuDirect())){//收款时
        	String sql="select count(1) from bl_deposit where rept_no=? and eu_dptype='9' and del_flag='0'";
        	int count=DataBaseHelper.queryForScalar(sql, Integer.class, blDeposit.getReptNo());
            if(count>0){
            	throw new BusException("当前票据号存在重复数据，请前往【票据管理】进行维护！");
            }
        }
        
        BdOuDept dept = DataBaseHelper.queryForBean("select * from bd_ou_dept where pk_dept = ?", BdOuDept.class, blDeposit.getPkDept());
    	if(dept.getDtDepttype().equals("08")){
        	String pkEmpInv = blDeposit.getPkEmpinvoice();
        	if(CommonUtils.isEmptyString(pkEmpInv)){
        		throw new BusException("票据领用主键为空，请检查票据！");
        	}
    	}

    	if(EnumerateParameter.FOUR.equals(blDeposit.getDtPaymode())){
    		BlDepositPi dp =  new BlDepositPi();
    		dp.setPkPi(blDeposit.getPkPi());
    		dp.setEuDirect("-1".equals(blDeposit.getEuDirect())?"1":"-1");
    		dp.setAmount(blDeposit.getAmount().multiply(new BigDecimal(-1)));
    		dp.setDtPaymode("4");
    		dp.setPayInfo(blDeposit.getPayInfo());
    		
    		balAccoutService.saveMonOperation(dp, user,null,"2",dp.getDtPaymode());
    	}
    	//blPrePayService.setDefaultValue(blDeposit, true);
        DataBaseHelper.insertBean(blDeposit);        
	}
	
	/**
	 * 住院记退费
	 * @param cgInfoList
	 * @param user
	 */
	public void savePatiCgInfo(List<PatiCgInfo> cgInfoList, User user) {
		List<PatiCgParamVo> blCgPubParamVos = createPatiCgInfo(cgInfoList,user);
		List<BlIpDt> vosForInsert = new ArrayList<BlIpDt>();//记费集合
		List<RefundVo> reFund = new ArrayList<RefundVo>();//退费集合
		List<BlPubPriceVo> priceVos = new ArrayList<BlPubPriceVo>();
	    String codeCg = ApplicationUtils.getCode("0602");	
	    ExAssistOcc exAssistOcc = new ExAssistOcc();
		// 7、构建记费数据
		for (PatiCgParamVo pvo : blCgPubParamVos) {
			if("1".equals(pvo.getChargeFlag())){//记费
				BlIpDt vo = new BlIpDt();
				String pkPv = pvo.getPkPv();
				// 3. 取pv相关信息、以及物品价格信息
				PvEncounter pvvo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ?",PvEncounter.class, pkPv);	
				vo.setPkCgip(NHISUUID.getKeyId());
				vo.setPkOrdexdt(pvo.getPkOrdexdt());
				vo.setSpec(pvo.getSpec());
				// 设置各类机构，人员，部门信息 pkOrg，pkPv,pkPi,
				if(StringUtils.hasText(pvo.getPkOrg())){
					vo.setPkOrg(pvo.getPkOrg());
				}else{
					vo.setPkOrg(UserContext.getUser().getPkOrg());
				}	
				vo.setPkPv(pkPv);
				vo.setPkPi(pvo.getPkPi());
				// pk_org_app, pk_dept_app,pk_dept_ns_app,pk_wg,pk_emp_app,name_emp_app
				vo.setPkOrgApp(pvo.getPkOrgApp());
				vo.setPkDeptApp(pvo.getPkDeptApp());
				vo.setPkWg(null);
				vo.setPkEmpApp(CommonUtils.isEmptyString(pvo.getPkEmpApp())?pvvo.getPkEmpPhy():pvo.getPkEmpApp());
				// pk_org_ex pk_dept_ex
				vo.setPkOrgEx(pvo.getPkOrgEx());
				vo.setPkDeptEx(CommonUtils.isEmptyString(pvo.getPkDeptEx())?pvvo.getPkDept():pvo.getPkDeptEx());
				// pk_dept_cg pk_emp_cg name_emp_cg
				vo.setPkDeptCg(CommonUtils.isEmptyString(pvo.getPkDeptCg())?pvvo.getPkDept():pvo.getPkDeptCg());
				vo.setPkEmpCg(pvo.getPkEmpCg());

				//设置费用发生日期，记费日期 date_cg,date_hap
				vo.setDateCg(pvo.getDateHap());//---------------
				vo.setDateHap(pvo.getDateHap());
				//如果是药品，设置pd表相关信息 pk_pd,batch_no,pk_unit_pd,pack_size, price_cost
				if("1".equals(pvo.getFlagPd())){
					dealPdInfo(vo, pvo);
				}
				
				//设置各类标志 flag_settle flag_pd flag_insu
				vo.setFlagInsu("0");
				vo.setFlagPd(pvo.getFlagPd());
				//处方主键，医嘱主键 pk_pres pk_cnord
				vo.setPkPres(pvo.getPkPres());
				vo.setPkCnord(pvo.getPkCnord());
				//结算主键 ， pk_settle 退费关联计费主键 pk_cgip_back code_cg 记费编码
				vo.setPkCgipBack(null);
				vo.setFlagSettle("0");
				vo.setCodeCg(codeCg);
				vo.setInfantNo(CommonUtils.isEmptyString(pvo.getInfantNo())?"0":pvo.getInfantNo());
				//设置原始单价，数量，金额，当前单价，优惠类型，优惠比例，自费比例，患者自费等信息 
				//price_org,price,quan,amount,pk_disc,ratio_disc,ratio_self,amount_pi
				
				vo.setQuan(pvo.getQuanCg());//数量
				vo.setAmount(pvo.getAmount());//金额
				vo.setPrice(pvo.getPrice());//当前单价
				vo.setAmountPi(pvo.getAmount());
				vo.setAmountAdd(0.00);//加收金额
				vo.setAmountHppi(0.00);//医保金额_患者支付
				//设置item表相关信息 pk_itemcate ,pk_item ,name_cg ,pk_unit spec
				if("0".equals(pvo.getFlagPd())){//非药品
					BdItem bd = cnMsgMapper.getBdItemsByCon(pvo.getPkItem());
					if(bd!=null){
						vo.setPkItemcate(bd.getPkItemcate());//收费项目分类
						vo.setPkItem(pvo.getPkItem());//收费项目
						vo.setNameCg(bd.getName());//项目名称
						vo.setPkUnit(bd.getPkUnit());//单位
						vo.setSpec(bd.getSpec());//规格
					}
				}
				
				// 调用价格策略接口取当前单价，原始单价 以及自付比
				List<BlIpDt> vos = new ArrayList<BlIpDt>();
				if(StringUtils.hasText(pvo.getPkItem())||(StringUtils.hasText(pvo.getPkOrd())&&"1".equals(pvo.getFlagPd()))){
					priceVos = priceStrategyService.getItemPriceAllInfo(pvo.getPkItem(),
							pvo.getFlagPd(),pvo.getPrice(), pvvo, pvo.getDateHap());
//					vos = ipCgPubService.dealPriceInfo(pvo.getQuanCg(), priceVos, vo);
					vos = this.dealPriceInfo(pvo.getQuanCg(), priceVos, vo);
				}else if(StringUtils.hasText(pvo.getPkOrd())&&!"1".equals(pvo.getFlagPd())){
					vos = getFromOrd(pvvo, pvo, vo);				
				}else if(StringUtils.hasText(pvo.getPkCnord())){
					CnOrder cnorder = DataBaseHelper.queryForBean(
							"select pk_ord from cn_order where pk_cnord = ?",
							CnOrder.class, pvo.getPkCnord());
					pvo.setPkOrd(cnorder.getPkOrd());
					vos = getFromOrd(pvvo, pvo, vo);
				}
				
				if(vos.size()<=0){
					vosForInsert.add(vo);
					continue;
				}else{
					vosForInsert.addAll(vos);
				}
				// 更新医嘱执行单，根据医嘱主键
				DataBaseHelper.execute("update ex_order_occ set date_occ=?, pk_emp_occ=?, eu_status='1',pk_org_occ=?  where pk_cnord=? ", new Date(), pvo.getPkEmpEx(), pvo.getPkOrgEx(),  pvo.getPkCnord());
				// 更新医嘱
				DataBaseHelper.execute("update cn_order set pk_org_exec=?, pk_dept_exec=? where pk_cnord=? ", pvo.getPkOrg(), pvo.getPkDeptEx(), pvo.getPkCnord());
			
				ExOrderOcc ecOrderOcc = DataBaseHelper.queryForBean("select pk_exocc from ex_order_occ  where pk_cnord=?", ExOrderOcc.class, new Object[]{pvo.getPkCnord()});
				
				int count = DataBaseHelper.queryForScalar("select count(1) from ex_assist_occ where pk_exocc=?", Integer.class, ecOrderOcc.getPkExocc());
				
				if (count <= 0) {
					// 得到该条医嘱的数量
					CnOrder cnOrder = DataBaseHelper.queryForBean("select quan from cn_order where pk_cnord=? ", CnOrder.class, pvo.getPkCnord());
					int len = (cnOrder.getQuan()).intValue();
					for (int i = 0; i < len; i++) {
						exAssistOcc = constructExAssistOcc(vo, ecOrderOcc.getPkExocc(), user);
						DataBaseHelper.insertBean(exAssistOcc);
					}
				}
			
			}else{//退费
				//退费的话，调用已有的退费接口
				RefundVo refundVo = new RefundVo();
				refundVo.setNameEmp(pvo.getNameEmpCg());
				refundVo.setPkCgip(pvo.getPkCgip());//退费主键
				refundVo.setQuanRe(MathUtils.mul(pvo.getQuanCg(), -1.0));//数量
				reFund.add(refundVo);
			}
		}
		// 8,批量写入记费表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class),vosForInsert);	

		//退费
		if(reFund!=null && reFund.size()>0){
			ApplicationUtils apputil = new ApplicationUtils();
			ResponseJson prePayInfo = apputil.execService("PV", "ipCgPubService", "savePatiRefundInfo",reFund,user);
			if(prePayInfo.getStatus()<0){
				throw new BusException(prePayInfo.getDesc());
			}
		}
	}
	
	/**
     * 出院结算
     * @param strikeSettleInfo
     * @param user
     */
	public void strike(StrikeSettleInfo strikeSettleInfo, User user) {
		//组建出院结算信息
		Map<String, Object> param = createStrikeSettleInfo(strikeSettleInfo,user);
        String pkSettle = (String)param.get("pkSettle");
		//1.参数接收		
		User userifo = (User)user;
		
		BlDeposit fromSettle = (BlDeposit) param.get("blDeposit");
		String pkPv = param.get("pkPv").toString();
		String euSttype = param.get("euSttype").toString();
		String euStresult = param.get("euStresult").toString();
		String dateEnd = dateTrans(DateUtils.strToDate(param.get("dateEnd").toString(), "yyyy-MM-dd HH:mm:ss"));
		
		PvEncounter pvvo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ?",PvEncounter.class, pkPv);
		if(pvvo.getPkPv() == null ){
			throw new BusException("患者就诊记录为空!");
		}
		String dateBegin =  dateTrans(pvvo.getDateBegin());

		//1	基于新的记费和退费信息生成新的结算记录和结算明细，写表bl_settle（记录原结算主键）和bl_settle_detail；
		Map<String,Object> stDataMap = settleData(param, pkPv, euSttype, dateBegin, dateEnd, user, euStresult);
		List<BlSettleDetail> stDtVos = (List<BlSettleDetail>) stDataMap.get("detail");
		BlSettle stVo = (BlSettle)stDataMap.get("settle");
		stVo.setPkSettleRev(pkSettle);
		
		for(BlSettleDetail vo : stDtVos){
			vo.setPkStdt(NHISUUID.getKeyId());
		}
		DataBaseHelper.insertBean(stVo);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), stDtVos);

		//2	计算新增记费和退费金额合计，生成新的结算（eu_dptype=2）收付款记录，写表bl_deposit；
		if(fromSettle!=null){	
			fromSettle.setEuDptype("2");
			ApplicationUtils.setDefaultValue(fromSettle, true);
			fromSettle.setPkDepo(NHISUUID.getKeyId());
			fromSettle.setFlagSettle("1");
			fromSettle.setPkSettle(stVo.getPkSettle());
			fromSettle.setDateReptBack(null);
			
			DataBaseHelper.insertBean(fromSettle);				
			}
		//3	更新费用明细表bl_ip_st中新增记费和退费记录字段flag_settle=1和pk_settle=新结算主键；
		DataBaseHelper.execute("update bl_ip_dt set flag_settle = '1', pk_settle = ? where  flag_settle = '0' and pk_pv = ?",stVo.getPkSettle(),pkPv);	        
	}
	
	/**
	 * 取消结算
	 * @param csInfo
	 * @param user
	 */
	public void cancelSettle(CancleSettleInfo csInfo, User user) {
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("pkSettle", csInfo.getSETTLEMENTNO());//要取消的结算主键
		paraMap.put("date", csInfo.getOPERATTIME());//操作时间
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson adtRegInfo = apputil.execService("PV", "cancelSettleService", "cancelSettle",paraMap,user);
		if(adtRegInfo.getStatus()<0){
			throw new BusException(adtRegInfo.getDesc());
		}
		
	}
	
	/**
	 * 组建住院记费信息
	 * @param cgInfoList
	 * @param user
	 */
	private List<PatiCgParamVo> createPatiCgInfo(List<PatiCgInfo> cgInfoList, User user) {
		List<PatiCgParamVo> blList = new ArrayList<PatiCgParamVo>();
		
		//得到pk_pi,pk_pv
		PvEncounter pv = DataBaseHelper.queryForBean("select pk_pi,pk_pv from pv_encounter where code_pv=?", PvEncounter.class, new Object[]{cgInfoList.get(0).getINHOSPINDEXNO()});
		if(pv == null || pv.getPkPv()==null || "".equals(pv.getPkPv())){
			throw new BusException("患者就诊记录为空!");
		}
		String jsonParam = null;
		//开立科室
		jsonParam = createParam(user,"bd_ou_dept","pk_dept","code_dept",cgInfoList.get(0).getDEPTINDEXNOAPP());
		String pkDeptApp = commonService.getDictPkByCode(jsonParam, user);
		//执行科室
		jsonParam = createParam(user,"bd_ou_dept","pk_dept","code_dept",cgInfoList.get(0).getDEPTINDEXNOEX());
		String pkDeptEx = commonService.getDictPkByCode(jsonParam, user);
		//记费科室
		jsonParam = createParam(user,"bd_ou_dept","pk_dept","code_dept",cgInfoList.get(0).getDEPTINDEXNOCG());
		String pkDeptCg = commonService.getDictPkByCode(jsonParam, user);
		//操作人员
		String pkEmpApp = null;
		if(null != cgInfoList.get(0).getOPERATORNO() && !"".equals(cgInfoList.get(0).getOPERATORNO())){
			jsonParam = createParam(user,"bd_ou_employee","pk_emp","code_emp",cgInfoList.get(0).getOPERATORNO());
			pkEmpApp = commonService.getDictPkByCode(jsonParam, user);
		}else{
			pkEmpApp = "";
		}
		
		String NameEmp =DataBaseHelper.queryForBean("select * from bd_ou_employee where code_emp=?", String.class,cgInfoList.get(0).getOPERATORNO() );
		user.setPkDept(pkDeptEx);
		user.setPkEmp(pkEmpApp);
		user.setNameEmp(NameEmp);
		UserContext.setUser(user);
		
		for (PatiCgInfo pc : cgInfoList) {
			PatiCgParamVo bp = new PatiCgParamVo();
			bp.setPkOrg(user.getPkOrg());//机构
			bp.setEuPvType("3");//就诊类型
			if(pv != null){
				bp.setPkPi(pv.getPkPi());//患者主键
				bp.setPkPv(pv.getPkPv());//就诊主键
			}
			bp.setFlagPd(pc.getDRUGFLAG());//药品标志
			String pkItem = null;
			
			//收费项目索引号:表中没有pk_org字段--收费项目索引号是医嘱号+800000000得来的
			//先根据医嘱号得到医嘱主键，然后根据医嘱主键，在bl_ip_dt表中得到记费主键
			
			//得到医嘱号
			String orderNo = pc.getORDERNO();
			Integer ordsn = null;
			if(!"0".equals(orderNo)){
				ordsn = Integer.valueOf(orderNo).intValue() - Integer.valueOf("800000000").intValue();//得到本系统的医嘱号
			}
			//根据医嘱号得到医嘱主键
			CnOrder ord = DataBaseHelper.queryForBean("select * from cn_order where ordsn=? ", CnOrder.class, new Object[]{ordsn});
			
			if(null != pc.getDRUGFLAG() && pc.getDRUGFLAG().equals("0")){
				BdItem bditem = null;
				//如果是新记费的项目，这个值传的是0
				if(pc.getCHARGEITEMINDEXNO()!=null && !"0".equals(pc.getCHARGEITEMINDEXNO()) && !"".equals(pc.getCHARGEITEMINDEXNO())){
					Integer itemCode = Integer.valueOf(pc.getCHARGEITEMINDEXNO()).intValue() - Integer.valueOf("900000000").intValue();
					bditem = DataBaseHelper.queryForBean("select * from bd_item where code = ? and del_flag='0'", BdItem.class, new Object[]{itemCode});
				}
				else{
					bditem = DataBaseHelper.queryForBean("select * from bd_item where code = ? and del_flag='0'", BdItem.class, new Object[]{pc.getDRUGINDEXNO().split("_")[0]});
				}
				if(bditem != null){
					pkItem = bditem.getPkItem();
			    }
			}else{
				jsonParam = createParam(user,"bd_pd","pk_pd","code",pc.getDRUGINDEXNO());
				BdPd bdPd = DataBaseHelper.queryForBean("select * from bd_pd where code =? and del_flag='0'", BdPd.class, new Object[]{pc.getDRUGINDEXNO().split("_")[0]});
				bp.setPkOrd("");//物品使用
				bp.setBatchNo("~");
				bp.setPkUnitPd(bdPd.getPkUnitPack());
				bp.setPackSize(bdPd.getPackSize());
				bp.setPriceCost(Double.valueOf(pc.getTOTALMONEY()));
				pkItem = bdPd.getPkPd();
			}
			
			bp.setPkItem(pkItem);//药品的话，就是bd_pd
			bp.setPkOrgEx(user.getPkOrg());//执行机构
			bp.setPkDeptEx(pkDeptEx);//执行科室
			bp.setPkDeptApp(pkDeptApp);//开立科室
			bp.setFlagPv("0");
			if(pc.getPAYMENTTIME()!=null){
				bp.setDateHap(DateUtils.strToDate(pc.getPAYMENTTIME(), "yyyy-MM-dd HH:mm:ss"));//费用发生时间
			}else{
				bp.setDateHap(new Date());//费用发生时间
			}			
			bp.setPkDeptCg(pkDeptCg);//记费科室
			bp.setPkEmpApp(pkEmpApp);
			bp.setNameEmpCg(null);
			bp.setNameEmpApp(null);
			bp.setNameEmpEx(null);
			bp.setAmount(Double.valueOf(pc.getTOTALMONEY()));//总金额
			bp.setPrice(new BigDecimal(pc.getTOTALMONEY()).divide(new BigDecimal(pc.getDRUGAMOUNT()),4,BigDecimal.ROUND_HALF_UP).doubleValue());//单价--根据数量和金额计算
			bp.setQuanCg(Double.valueOf(pc.getDRUGAMOUNT()));//记退费数量
			if(ord != null){
				bp.setPkCnord(ord.getPkCnord());//医嘱主键
			}
			
			//根据收费项目主键以及医嘱主键确定具体是哪个记费记录
			if("-1".equals(pc.getCHARGEFLAG())){
				BlIpDt blIpDt = DataBaseHelper.queryForBean("select pk_cgip from bl_ip_dt where pk_cnord= ? and pk_item = ? ", BlIpDt.class, new Object[]{bp.getPkCnord(),bp.getPkItem()});
				if(blIpDt.getPkCgip() != null){
					bp.setPkCgip(blIpDt.getPkCgip());//记费主键
				}
			}
			bp.setChargeFlag(pc.getCHARGEFLAG());//记退费标志
			blList.add(bp);
		}
		return blList;
	}
	
	/**
	 * 组建出院结算信息
	 * @param strikeSettleInfo
	 * @param user
	 * @return 
	 */
	private Map<String, Object> createStrikeSettleInfo(StrikeSettleInfo strikeSettleInfo,User user) {
		Map<String,Object> param = new HashMap<String, Object>();
		PvEncounter pve = DataBaseHelper.queryForBean("select * from pv_encounter where code_pv=?", PvEncounter.class, new Object[]{strikeSettleInfo.getINHOSPINDEXNO()});
		if(pve.getPkPv() == null ){
			throw new BusException("患者就诊记录为空!");
		}
		String jsonParam = null;
		BlDeposit bl = new BlDeposit();
		bl.setEuPvtype("03");//就诊类型
		bl.setPkOrg(user.getPkOrg());//机构
		bl.setEuDirect("1");//收退方向
		bl.setPkPi(pve.getPkPi());//患者主键
		bl.setPkPv(pve.getPkPv());//就诊主键
		bl.setAmount(new BigDecimal(strikeSettleInfo.getSELFTOTALPAYMENT()));//交款金额
		bl.setDtPaymode(strikeSettleInfo.getMFSMETHODCODE());//收付款方式
		bl.setDatePay(DateUtils.strToDate(strikeSettleInfo.getOPERATTIME(), "yyyy-MM-dd HH:mm:ss"));//收付款日期-------
		//收付款部门
		jsonParam = createParam(user,"bd_ou_dept","pk_dept","code_dept",strikeSettleInfo.getDEPTINDEXNOAPP());
		String pkDept = commonService.getDictPkByCode(jsonParam, user);
		bl.setPkDept(pkDept);
		//收款人
		jsonParam = createParam(user,"bd_ou_employee","pk_emp","code_emp",strikeSettleInfo.getOPERATORNO());
		String pkEmpApp = commonService.getDictPkByCode(jsonParam, user);
		bl.setPkEmpPay(pkEmpApp);//收款人主键
		bl.setNameEmpPay(null);//收款人姓名
		bl.setFlagAcc("0");//帐户支付标志
		//bl.setFlagSettle("1");//结算标志
		bl.setPkSettle(strikeSettleInfo.getSELFINDEXNO());//结算唯一标识
		bl.setDelFlag("0");//删除标志
		param.put("deposit", bl);
		
		param.put("pkPv", pve.getPkPv());
		param.put("dateEnd", strikeSettleInfo.getOPERATTIME());//时间
		param.put("euSttype", "0");
		param.put("amountPrep", strikeSettleInfo.getPREPAIDMONEY());//预交金合计
		param.put("amountSt", strikeSettleInfo.getSELFTOTALPAYMENT());//患者费用合计
		param.put("amountPi", strikeSettleInfo.getSELFPAYMENTFEE());//自费费用
		param.put("amountInsu", strikeSettleInfo.getMEDICAREPAYMENT());//医保支付金额
		param.put("euStresult", "0");//结算结果分类
		param.put("flagSpItemCg", "0");//是否是特殊项目结算
		
		List<InvInfoVo> invos = new ArrayList<InvInfoVo>();
		InvInfoVo invInfoVo = new InvInfoVo();
		BillInfo inv = new BillInfo();
		invInfoVo.setInv(inv);
		invos.add(invInfoVo);
		param.put("invos", invos);
		
		return param;
	}
	/**
	 * 组建入院登记信息
	 * @param adtInfo
	 * @param loginUser
	 * @return
	 */
	public AdtRegParam createAdtRegParam(AdtRegisterInfo adtInfo, User loginUser) {
		AdtRegParam adt = new AdtRegParam();
		PvEncounter pv = new PvEncounter();
		PiMaster pi = new PiMaster();
		String jsonParam = null;
		//入院科室
		jsonParam = createParam(loginUser,"bd_ou_dept","pk_dept","code_dept",adtInfo.getADMITDEPT());
		String pkDept = commonService.getDictPkByCode(jsonParam, loginUser);
		adt.setPkDeptAdmit(pkDept);
		//入院病区
		jsonParam = createParam(loginUser,"bd_ou_dept","pk_dept","code_dept",adtInfo.getADMITWARDCODE());
		String pkDeptNs = commonService.getDictPkByCode(jsonParam, loginUser);
		adt.setPkDeptNsAdmit(pkDeptNs);
		//住院次数
		adt.setIpTimes(Integer.valueOf(adtInfo.getADMITTIMES()));
		//病情等级
		adt.setDtLevelDise(adtInfo.getDISELEVEL());
		//入院途径
		adt.setDtIntype(adtInfo.getADMITWAYCODE());
		//就诊号-以住院号作为就诊号
		adt.setCodePv(adtInfo.getINHOSPNO());
		//收治医生
		String pkEmpTre = null;
		if(adtInfo.getRECEPTTREATDRCODE() != null && !"".equals(adtInfo.getRECEPTTREATDRCODE())){
			jsonParam = createParam(loginUser,"bd_ou_employee","pk_emp","code_emp",adtInfo.getRECEPTTREATDRCODE());
			pkEmpTre = commonService.getDictPkByCode(jsonParam, loginUser);
			adt.setPkEmpTre(pkEmpTre);
		}
		//收治医生编码
		adt.setCodeEmpTre(adtInfo.getRECEPTTREATDRCODE());
		//登记日期--为了测试new Date()
		adt.setDateReg(DateUtils.strToDate(adtInfo.getADMITDATE(), "yyyy-MM-dd HH:mm:ss"));
		//adt.setDateReg(new Date());
		//医保计划
		loginUser.setPkOrg("~                               ");
		jsonParam = createParam(loginUser,"bd_hp","pk_hp","code",adtInfo.getMEDPLAN());
		String pkInsu = commonService.getDictPkByCode(jsonParam, loginUser);
		adt.setPkInsu(pkInsu);
		String[] pkHps = {pkInsu};
		adt.setPkHps(pkHps);
		//adt.setPkInsu("21f8f93ac28c4be88b55d1f2187a90df");

		loginUser.setPkDept(pkDept);
		loginUser.setPkEmp(pkEmpTre);
		loginUser.setNameEmp("123");
		UserContext.setUser(loginUser);
		//患者分类
		String pkPicate = null;
		if(adtInfo.getPATTYPE() != null && !"".equals(adtInfo.getPATTYPE())){
			jsonParam = createParam(loginUser,"pi_cate","pk_picate","code",adtInfo.getPATTYPE());
		    pkPicate = commonService.getDictPkByCode(jsonParam, loginUser);
			adt.setPkPicate(pkPicate);
		}else{
			pkPicate="2db38b88747848f28dd085b4842fdca7";
			adt.setPkPicate("2db38b88747848f28dd085b4842fdca7");
		}
		//入院诊断
		BdTermDiag diag = DataBaseHelper.queryForBean("select diagname,pk_diag from bd_term_diag where diagcode=? ", BdTermDiag.class, new Object[]{adtInfo.getADMITDIAGCODE()});
		if(diag != null ){
			adt.setPkDiag(diag.getPkDiag());
			adt.setDescDiag(diag.getDiagname());//诊断描述
		}else{
			adt.setPkDiag("");
			adt.setDescDiag("");//诊断描述
		}
		//诊断类型-入院诊断
		adt.setDtDiagtype("0100");
		//诊断编码
		adt.setCodeDiag(adtInfo.getRECEPTTREATDRCODE());
		
		pv.setCodePv(adtInfo.getINHOSPNO());//就诊号-以住院号作为就诊号
		pv.setDateBegin(DateUtils.strToDate(adtInfo.getADMITDATE(), "yyyy-MM-dd HH:mm:ss"));//开始时间
		pv.setNamePi(adtInfo.getPATNAME());//患者姓名
		pv.setDtSex(changeSexCode(adtInfo.getPHYSISEXCODE()));//性别
		pv.setDtMarry(adtInfo.getMARITALSTATUSCODE());//婚姻
		pv.setPkDept(pkDept);//当前科室
		pv.setPkDeptNs(pkDeptNs);//当前病区
		pv.setPkEmpTre(pkEmpTre);//收治医生主键
		pv.setNameEmpTre("");//收治医生
		pv.setPkInsu(pkInsu);//医保计划
		pv.setPkPicate(pkPicate);//患者分类
		pv.setFlagCancel("0");//退诊标志
		pv.setAddrCur(null);//现住地址描述
		pv.setAddrCurDt(adtInfo.getCURRADDR());//现住详细地址
		pv.setAddrcodeRegi(null);//户口地址编码
		pv.setAddrRegi(null);//户口地址描述
		pv.setAddrRegiDt(adtInfo.getHUKOUADDR());//户口详细地址
		pv.setUnitWork(adtInfo.getCOMPANY());//工作地址
		pv.setNameRel(adtInfo.getCONTACTNAME());//联系人姓名
		pv.setTelRel(adtInfo.getCONTACTPHONENO());//联系人电话
		pv.setDtRalation(adtInfo.getCONTACTRELATIONCODE());//联系人关系
		pv.setPkOrg("89ace0e12aba439991c0eb001aaf02f7");


		if(adtInfo.getPATINDEXNO() != null && !"".equals(adtInfo.getPATINDEXNO())){
			loginUser.setPkOrg("89ace0e12aba439991c0eb001aaf02f7");
			String pkPi= "";
			jsonParam = createParam(loginUser,"pi_master","pk_pi","code_pi",adtInfo.getPATINDEXNO());//用患者id（his传的患者id为住院患者id（有问题））查询pkpi
			pkPi = commonService.getDictPkByCode(jsonParam, loginUser);
			pi.setPkPi(pkPi);
		}
		if(!StringUtils.isEmpty(adtInfo.getCERTIFICATENO())) {
			String idNo= "";
			idNo = DataBaseHelper.queryForBean("select id_no from pi_master where id_no =?", String.class, adtInfo.getCERTIFICATENO());//用身份证号查询pkpi
			if(!StringUtils.isEmpty(idNo)){
				pi.setIdNo("");
			}
			else {
				pi.setIdNo(idNo);
			}
		}
		pi.setCodePi(adtInfo.getPATINDEXNO());//患者编码
		pi.setCodeIp(adtInfo.getINHOSPNO());//住院号
		pi.setPkPicate(pkPicate);
		pi.setNamePi(adtInfo.getPATNAME());//姓名
		pi.setDtIdtype("30");//证件类型，只有身份证号
//		pi.setIdNo(adtInfo.getCERTIFICATENO());
		pi.setDtSex(changeSexCode(adtInfo.getPHYSISEXCODE()));//性别
		if(adtInfo.getDATEBIRTH() != null && !"".equals(adtInfo.getDATEBIRTH())){
			pi.setBirthDate(DateUtils.strToDate(adtInfo.getDATEBIRTH(),"yyyy-MM-dd"));
		}
		pi.setDtMarry(adtInfo.getMARITALSTATUSCODE());
		pi.setDtCountry(adtInfo.getNATIONCODE());
		pi.setDtNation(adtInfo.getETHNICCODE());
		pi.setMobile(adtInfo.getPHONENO());
		pi.setAddrcodeCur(null);
		pi.setAddrCur(null);
		pi.setAddrCurDt(adtInfo.getCURRADDR());
		pi.setAddrcodeRegi(null);//户口地址编码
		pi.setAddrRegi(null);//户口地址描述
		pi.setAddrRegiDt(adtInfo.getHUKOUADDR());//户口详细地址
		pi.setNameRel(adtInfo.getCONTACTNAME());//联系人姓名
		pi.setTelRel(adtInfo.getCONTACTPHONENO());//联系人电话
		pi.setDtRalation(adtInfo.getCONTACTRELATIONCODE());//联系人关系
		pi.setCreateTime(DateUtils.strToDate(adtInfo.getADMITDATE(), "yyyy-MM-dd HH:mm:ss"));//创建时间
		pi.setPkOrg("89ace0e12aba439991c0eb001aaf02f7");
		pi.setCreateTime(new Date());
		adt.setPiMaster(pi);
		adt.setPvEncounter(pv);
		
		return adt;
	}
	/**
	 * 组建住院预交金
	 * @param preInfo
	 * @param user
	 */
	private BlDeposit createPrePayInfo(PrePayInfo preInfo, User user) {
		BlDeposit bl = new BlDeposit();
		String jsonParam = null;
		bl.setPkEmpinv(preInfo.getRECEIPTINDEXNO());//票据领用主键--取报文中的票据领用索引号-待确定
		bl.setEuPvtype("3");//就诊类型
		bl.setEuDptype("9");//收费款类型-9是住院预缴金
		bl.setEuDirect(preInfo.getCHARGEFLAG());//收退方向
		PiMaster pi = DataBaseHelper.queryForBean("select pk_pi from pi_master where code_pi=?", PiMaster.class, new Object[]{preInfo.getPATINDEXNO()});
		if(pi != null){//pi如果为空的话，就创建一个
			bl.setPkPi(pi.getPkPi());//患者主键
		}
		//根据报文中的住院索引号(住院索引号应该是住院号)--得到pk_pv
		PvEncounter pve = DataBaseHelper.queryForBean("select * from pv_encounter where code_pv=?", PvEncounter.class, new Object[]{preInfo.getINHOSPINDEXNO()});
		if(pve != null){//pve如果为空的话，就创建一个
			bl.setPkPv(pve.getPkPv());//就诊主键
		}else {
			bl.setPkPv(preInfo.getINHOSPINDEXNO());
		}
		if("-1".equals(preInfo.getCHARGEFLAG())){
			//处理目的：不确定报文中的金额为正为负，如果是退方向，将金额转换为负值
			if(new BigDecimal(preInfo.getPROSPAY()).compareTo(BigDecimal.ZERO) == -1){//表示小于0
				bl.setAmount(new BigDecimal(preInfo.getPROSPAY()));//交款金额--注意金额正负
			}
			if(new BigDecimal(preInfo.getPROSPAY()).compareTo(BigDecimal.ZERO) == 1){//表示大于0
				bl.setAmount(new BigDecimal(preInfo.getPROSPAY()).multiply(new BigDecimal(-1)));//交款金额--注意金额正负
			}
		}else{
			bl.setAmount(new BigDecimal(preInfo.getPROSPAY()));
		}
		
		bl.setDtPaymode(changeDtPayMode(preInfo.getPAYMETHODNAME()));//付款方式--传进来的是汉字
		bl.setDatePay(DateUtils.strToDate(preInfo.getPAYTIME(), "yyyy-MM-dd HH:mm:ss"));//付款时间--传过来
		jsonParam = createParam(user,"bd_ou_dept","pk_dept","code_dept",preInfo.getDEPTINDEXNO());
		String pkDept = commonService.getDictPkByCode(jsonParam, user);
		bl.setPkDept(pkDept);//收付款部门
		//收款人
		jsonParam = createParam(user,"bd_ou_employee","pk_emp","code_emp",preInfo.getRECORDDRCODE());
		String pkUser = commonService.getDictPkByCode(jsonParam, user);
		bl.setPkEmpPay(pkUser);//收款人
		bl.setNameEmpPay("");//收款人姓名
		bl.setFlagAcc("0");//帐户支付标志
		bl.setPkAcc("");//帐户主键
		bl.setFlagSettle("0");//结算标志
		bl.setReptNo(preInfo.getRECEIPTNO());//收据编号-取得是报文中的收据号字段
		bl.setPkDepoBack("");//退费时对应的收费主键
		if("-1".equals(preInfo.getCHARGEFLAG())){
			bl.setNote("退" + preInfo.getRECEIPTNO());//交款描述信息
		}
		bl.setCreateTime(DateUtils.strToDate(preInfo.getPAYTIME(), "yyyy-MM-dd HH:mm:ss"));//创建时间---
		//操作人员

		bl.setCreator(pkUser);
		bl.setModifier(pkUser);
		bl.setTs(DateUtils.strToDate(preInfo.getPAYTIME(), "yyyy-MM-dd HH:mm:ss"));
		bl.setPkOrg(user.getPkOrg());
		bl.setDelFlag("0");
		//bl.setCodePv(preInfo.getINHOSPINDEXNO());

		return bl;
	}
	/**
     * 组建参数
     * @param loginUser
     * @param tableName
     * @param pkColName
     * @param codeColName
     * @param code
     * @return
     */
    public String createParam(User loginUser, String tableName, String pkColName,String codeColName, String code) {
    	Map<String, Object> mapParam = new HashMap<String,Object>();
    	mapParam.put("pkOrg", loginUser.getPkOrg());
		mapParam.put("tableName", tableName);
		mapParam.put("pkColName", pkColName);
		mapParam.put("codeColName", codeColName);
		mapParam.put("code", code);
		mapParam.put("delFlag", "0");
		return JsonUtil.writeValueAsString(mapParam);
    }	
    
    private void dealPdInfo(BlIpDt vo, PatiCgParamVo pvo) {
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

    public String dateTrans(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String temp = null;
		if(date!=null){
			temp = sdf.format(date);
		}
		
		return temp;
	}

    /**
	 * 
	 * @param pkPv 患者就诊
	 * @param euSttype 结算类型  0出院结算，1中途结算
	 * @param dateEndTime 中途结算时间
	 * @return
	 */
	public Map<String, Object> settleData(Map<String, Object> param,String pkPv, String euSttype,String dateBegin,String dateEnd, IUser user, String euStresult) {
		// 1.1查询患者就诊信息
		PvEncounter pvvo = DataBaseHelper.queryForBean( "select * from pv_encounter where pk_pv = ?", PvEncounter.class, pkPv);
		Date dateEndTime = dateTrans(dateEnd);
		User userInfo = (User) user;
		BlSettle blSettle = new BlSettle();
		blSettle.setDateBegin(dateTrans(dateBegin));
		blSettle.setPkPv(pkPv);// 当前患者就诊主键
		// 预缴金额AmtPrep
		Map<String, Object> amtPrepMap = DataBaseHelper
				.queryForMap(
						"select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.pk_pv = ? and depo.date_pay >= ? and depo.date_pay <= ?",
						pkPv, dateTrans(dateBegin), dateEndTime);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		blSettle.setAmountPrep(amtPrep);
		// 1.生成结算信息

		blSettle.setPkSettle(NHISUUID.getKeyId());
		//结算编码
		//blSettle.setCodeSt(ApplicationUtils.getCode("0605"));
		blSettle.setPkOrg(userInfo.getPkOrg());// 当前机构
		blSettle.setPkPi(pvvo.getPkPi());// 当前患者主键
		blSettle.setEuPvtype("3");// 就诊类型
		blSettle.setPkInsurance(pvvo.getPkInsu());// 患者主医保

		// 结算类型
		if ("0".equals(euSttype)) {
			blSettle.setDtSttype("10"); // 结算类型--出院结算
			blSettle.setDateEnd(pvvo.getDateEnd());
		}
		blSettle.setEuStresult(euStresult);
		blSettle.setDateSt(DateUtils.strToDate(param.get("dateEnd").toString(), "yyyy-MM-dd HH:mm:ss"));// 当前日期
		blSettle.setPkOrgSt(userInfo.getPkOrg());// 当前机构
		blSettle.setPkDeptSt(userInfo.getPkDept());// 当前部门
		blSettle.setPkEmpSt(userInfo.getPkEmp());// 当前用户
		blSettle.setNameEmpSt(userInfo.getNameEmp());// 当前用户姓名

		blSettle.setAmountPrep(new BigDecimal(param.get("amountPrep").toString())); //患者预交金合计
		blSettle.setAmountSt(new BigDecimal(param.get("amountSt").toString()));//患者费用合计
		blSettle.setAmountPi(new BigDecimal(param.get("amountPi").toString()));//患者自付合计
		blSettle.setAmountInsu(new BigDecimal(param.get("amountInsu").toString()));//第三方医保接口返回的医保支付
		
		blSettle.setFlagCanc("0");
		blSettle.setReasonCanc(null);
		blSettle.setPkSettleCanc(null);
		blSettle.setFlagArclare("0");
		blSettle.setFlagCc("0");
		blSettle.setPkCc(null);
		// 2.生成结算明细 并处理相关金额赋值
		List<BlSettleDetail> detailVos = handleSettleDetailC(pkPv, blSettle, userInfo.getPkOrg(),euSttype,dateBegin,dateEnd);
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("settle", blSettle);
		resMap.put("detail", detailVos);
		return resMap;

	}
	
	private Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date temp = null;
		if(StringUtils.hasText(date)){
			try {
				temp = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return temp;
	}
	private BigDecimal amtTrans(Object amt) {
		if(amt == null){
			return BigDecimal.ZERO;
		}else{
			return (BigDecimal)amt;
		}
	}
	private List<BlSettleDetail> handleSettleDetailC(String PkPv, BlSettle blSettle,String pkOrg,String euSttype,String begin,String end){
		Map<String,BlSettleDetail> res = new HashMap<String,BlSettleDetail>();
		//1.查询记费表的数据，组织记费阶段的数据	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pkPv", PkPv);
		params.put("dateBegin", begin);
		params.put("dateEnd", end);
		params.put("euSttype", euSttype);
		List<BlIpDt> cgVos = blIpPubMapper.qryCgIps(params);	
		Double amtHp = 0.0;
		Double  patiSelf = 0.0;
		for(BlIpDt vo : cgVos){
			Double amtHpTemp = 0.0; 
			Double a = vo.getRatioSelf();
			if(1.0 == vo.getRatioSelf()){ 
				Double price = MathUtils.sub(vo.getPriceOrg(), vo.getPrice());  
				amtHpTemp= MathUtils.mul(price, vo.getQuan());			
			}
			else {
				amtHpTemp = MathUtils.mul(MathUtils.mul(vo.getQuan(), vo.getPrice()), MathUtils.sub(1.0, vo.getRatioSelf()));  
			}
			amtHp = MathUtils.add(amtHp.doubleValue(), amtHpTemp);
			patiSelf = MathUtils.add(patiSelf, vo.getAmountPi()); 
		}
	    List<BlSettleDetail> resvo = new ArrayList<BlSettleDetail>();
		//4.结算时患者自付金额。
		BlSettleDetail voSelf = new BlSettleDetail();
		voSelf.setAmount(patiSelf.doubleValue());
		voSelf.setPkSettle(blSettle.getPkSettle());
		voSelf.setPkInsurance(null);
		res.put("pkSelf",voSelf);
		//5.给主表赋值
		blSettle.setAmountPi(new BigDecimal(patiSelf).setScale(2, BigDecimal.ROUND_HALF_UP));//不保留两位小数，保存的时候会报错
		resvo.addAll(res.values());
		return resvo;
    }
	/**
     * 性别
     * * @param hiscode
     * hiscode 1男 2女
     * NEMS 02男 03女
     *
     * @return
     */
    public String changeSexCode(String hiscode) {
        String nemsCode = "";
        if (hiscode == null || "".equals(hiscode)) {
            return "0";
        }
        switch (hiscode) {
            case "1":
                nemsCode = "02";
                break;
            case "2":
                nemsCode = "03";
                break;
            case "9":
                nemsCode = "0";
                break;
            default:
                nemsCode = "0";
                break;
        }
        return nemsCode;
    }
    
    public String changeDtPayMode(String payCode){
    	String nemsCode = "";
        if (payCode == null || "".equals(payCode)) {
            return "01";
        }
        switch (payCode) {
            case "现金":
                nemsCode = "01";
                break;
            case "支票":
                nemsCode = "02";
                break;
            case "银行卡":
                nemsCode = "06";
                break;
            case "帐户":
                nemsCode = "a";
                break;
            default:
                nemsCode = "0";
                break;
        }
        return nemsCode;
    }
    
 // 住院：向ex_assist_occ表中放数据
 	private ExAssistOcc constructExAssistOcc(BlIpDt blIpDt, String pkExocc, IUser user) {
 		// 添加至医技参数集合
 		ExAssistOcc exAssietocc = new ExAssistOcc();
 		exAssietocc.setPkOrg(blIpDt.getPkOrg());
 		exAssietocc.setPkCnord(blIpDt.getPkCnord());
 		exAssietocc.setPkPv(blIpDt.getPkPv());
 		exAssietocc.setPkPi(blIpDt.getPkPi());
 		exAssietocc.setEuPvtype("3");
 		// 执行单号
 		exAssietocc.setCodeOcc(ApplicationUtils.getCode("0503"));
 		exAssietocc.setPkEmpOrd(((User) user).getPkEmp());// 开单医生主键
 		exAssietocc.setNameEmpOrd(((User) user).getNameEmp());// 开单医生姓名
 		exAssietocc.setPkDept(blIpDt.getPkDeptApp());
 		exAssietocc.setDateOrd(new Date());
 		exAssietocc.setQuanOcc(blIpDt.getQuan());
 		exAssietocc.setTimesOcc(1);
 		exAssietocc.setTimesTotal(1);
 		exAssietocc.setPkOrgOcc(blIpDt.getPkOrgEx());
 		exAssietocc.setPkDeptOcc(blIpDt.getPkDeptEx());
 		exAssietocc.setFlagOcc("0");
 		exAssietocc.setFlagCanc("0");
 		exAssietocc.setPkExocc(pkExocc);
 		exAssietocc.setInfantNo("0");
 		exAssietocc.setEuStatus("0");
 		exAssietocc.setFlagPrt("0");
 		exAssietocc.setFlagRefund("0");

 		return exAssietocc;
 	}
 	
 	private List<BlIpDt> getFromOrd(PvEncounter pvvo, PatiCgParamVo pvo, BlIpDt vo) {
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
				vos.addAll(this.dealPriceInfo(quanCg, priceVos, vo));
			}
		}
		return vos;
	}

	public List<BlIpDt> dealPriceInfo(double quanCg,
									  List<BlPubPriceVo> priceVos, BlIpDt vo) {
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
}
