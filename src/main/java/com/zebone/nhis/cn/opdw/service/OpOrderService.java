package com.zebone.nhis.cn.opdw.service;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.HL7Exception;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.bl.opcg.vo.OpCgCnOrder;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.ipdw.service.BloodService;
import com.zebone.nhis.cn.ipdw.service.CnConsultApplyService;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.BloodApplyVO;
import com.zebone.nhis.cn.ipdw.vo.CnConsultApplyVO;
import com.zebone.nhis.cn.opdw.dao.ExLabOccVoMapper;
import com.zebone.nhis.cn.opdw.dao.ExRisOccVoMapper;
import com.zebone.nhis.cn.opdw.dao.OpOrderMapper;
import com.zebone.nhis.cn.opdw.vo.CnOrderBlOpDtVo;
import com.zebone.nhis.cn.opdw.vo.ExLabOccVo;
import com.zebone.nhis.cn.opdw.vo.ExRisOccVo;
import com.zebone.nhis.cn.opdw.vo.OpApplyRecordVo;
import com.zebone.nhis.cn.opdw.vo.OpPrescriptionVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnConsultResponse;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OpOrderService {

	@Autowired
	private OpOrderMapper OpOrder ;

	@Autowired
	private OpCgPubService opCgPubService;

	@Autowired
	private OpApplyService opApplyService;

	@Autowired
	private CnConsultApplyService cnConsultApplyService;

	@Autowired
	private BloodService bloodService;

	@Autowired
	private PdStOutPubService pdStOutPubService;

	@Autowired
	private ExLabOccVoMapper exLabOccVoMapper;

	@Autowired
	private ExRisOccVoMapper exRisOccVoMapper;
	

	/*@Autowired
	private DataSourceTransactionManager transactionManager;*/
	/**
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getEmpPresAuthority(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OpOrder.getEmpPresAuthority(map.get("pkEmp").toString());
	}
	/**
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getDrugStore(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);

		return OpOrder.getDrugStore(map.get("pkDept").toString());
	}		
	/**
	 * ????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPrescription(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		User user2 = (User) user;
		String pkEmp=null;//user2.getPkEmp(); //lxw @todo ??????????????????????????????
		if (ApplicationUtils.getPropertyValue("workspaceIsSy", "").equals("yes")) {// ?????????????????????????????????
			pkEmp=null;
			return OpOrder.getPrescriptionNems(map.get("pkPv").toString(),pkEmp);
		}
		return OpOrder.getPrescription(map.get("pkPv").toString(),pkEmp);
	}
	/**
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPrescriptionDetail(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String sPkPres = "";
		if (map.get("pkPres") == null || map.get("pkPres").equals(""))
			sPkPres = "-1";
		else
			sPkPres = map.get("pkPres").toString();
		List<Map<String, Object>> prescriptionDetails = OpOrder.getPrescriptionDetail(sPkPres,map.get("pkDeptExec")!=null?map.get("pkDeptExec").toString():"");
		
		List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> detail : prescriptionDetails) {
			detail.put("flagSettle", "1");
			List<BlOpDt> blopdts=DataBaseHelper.queryForList("select * from bl_op_dt where del_flag='0' and pk_cnord=?", BlOpDt.class,detail.get("pkCnord"));
			if(blopdts.size()>0){
				for (BlOpDt blOpDt : blopdts) {
					if(blOpDt.getFlagSettle().equals("0")){
						detail.put("flagSettle", "0");
						break;
					}
				}
			}else{
				detail.put("flagSettle", "0");
			}
			temp.add(detail);
		}
		return temp;
	}
	/**
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPresDetailChargeAdd(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String sPkPres = "";
		if (map.get("pkPres") == null || map.get("pkPres").equals(""))
			sPkPres = "-1";
		else
			sPkPres = map.get("pkPres").toString();
		return OpOrder.getPresDetailChargeAdd(sPkPres);
	}	

	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public String savePrescription(String param,IUser user){
		OpPrescriptionVo opPresAll = JsonUtil.readValue(param, new TypeReference<OpPrescriptionVo>(){} );
		String result=savePrescription(opPresAll,user);
		//???????????????????????????
		Map<String,Object> paramMap =  JsonUtil.readValue(param, Map.class);
		paramMap.put("type", opPresAll.getCnPres().get(0).getRowStatus());
		PlatFormSendUtils.sendCnPresOpMsg(paramMap);
		return result;
	}
	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public String savePrescription(OpPrescriptionVo opPresAll,IUser user){
		List<CnPrescription> PresList =  opPresAll.getCnPres();
		List<OpCgCnOrder> PresDetailList = opPresAll.getCnOrder();
		List<BlOpDt> PresChargeList = opPresAll.getCnOrderCharge();
		List<OpCgCnOrder> OrderInsertList = new ArrayList<OpCgCnOrder>();
		List<OpCgCnOrder> OrderUpdateList = new ArrayList<OpCgCnOrder>();
		List<BlPubParamVo> bodst = new ArrayList<BlPubParamVo>();
		Set<String> pkCnOrderSet = new HashSet<String>();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); 
		Date tsOld=null;
		PvEncounter pvInfo = null;
		String tsNew=null;
		if	(PresList.size() == 0)
			return "";
			
		//?????????????????????????????????
		int  diagM = DataBaseHelper.queryForScalar("select count(1) from pv_diag where del_flag='0' and flag_maj='1'  and pk_pv=? ", Integer.class, new Object[]{ PresList.get(0).getPkPv()});
		if(diagM<=0)  return "0";
		pvInfo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, PresList.get(0).getPkPv());

		for(int i=0; i<PresList.size(); i++){
			CnPrescription vPres = PresList.get(i);
			if(vPres!=null){
				if(Constants.RT_NEW.equals(vPres.getRowStatus()))
				{		
					if	(!(PresDetailList.size() == 1 && Constants.RT_LOAD.equals(PresDetailList.get(0).getRowStatus()))) //??????????????????????????????
					{
						Map<String,Object> storePriceMap = new HashMap<String,Object>();
						Map<String,Object> quanMap = new HashMap<String,Object>();
						List<String> pkPds = new ArrayList<String>();
						for (OpCgCnOrder co: PresDetailList)
						{	
							ApplicationUtils.setDefaultValue(co, false);
							pkPds.add(co.getPkOrd());
							quanMap.put(co.getPkOrd(), MathUtils.mul(co.getQuanCg(), co.getPackSize()));
//							// ???????????????????????????
//							List<PdOutDtParamVo> pdOutDtParamVos = pdStOutPubService.getPdStorePrice(co.getPkDeptExec(), null, co.getPkOrd(), co.getQuanCg(), null,false);
//							PdOutDtParamVo pdOutDtParamVo = pdOutDtParamVos.get(0);
//							co.setDateExpire(pdOutDtParamVo.getDateExpire());
//							co.setPriceCg(pdOutDtParamVo.getPriceStore());
//							co.setPriceCost(pdOutDtParamVo.getPriceCost());
//							co.setBatchNo(pdOutDtParamVo.getBatchNo());
//							co.setPackSize(pdOutDtParamVo.getPackSize().doubleValue());		
							OrderInsertList.add(co);
						}
						if(OrderInsertList.size()>0){
							storePriceMap.put("pkStore", "");
							storePriceMap.put("pkDeptExec", OrderInsertList.get(0).getPkDeptExec());
							storePriceMap.put("quanMap", quanMap);
							storePriceMap.put("pkPd", pkPds);
							List<PdOutDtParamVo> pdOutDtParamVos = pdStOutPubService.getPdStorePrice(storePriceMap,false);
							for(OpCgCnOrder co : OrderInsertList){
								for(PdOutDtParamVo pdOutDtParamVo:pdOutDtParamVos){
									if(co.getPkOrd().equals(pdOutDtParamVo.getPkPd())){
										co.setDateExpire(pdOutDtParamVo.getDateExpire());
										co.setPriceCg(pdOutDtParamVo.getPriceStore());
										co.setPriceCost(pdOutDtParamVo.getPriceCost());
										co.setBatchNo(pdOutDtParamVo.getBatchNo());
										co.setPackSize(pdOutDtParamVo.getPackSize().doubleValue());
										co.setTs(new Date());
										break;
									}
								}
							}
						}
					}
					dateZeroForPress(vPres);
					DataBaseHelper.insertBean(vPres);
					List<CnPrescription> queryForList = DataBaseHelper.queryForList("select * from CN_PRESCRIPTION where pk_pres=? and del_flag='0'", CnPrescription.class, vPres.getPkPres());
				    if(queryForList!=null&&queryForList.size()>0) tsNew = format.format(queryForList.get(0).getTs());
				}
				else if(Constants.RT_UPDATE.equals(vPres.getRowStatus()))
				{
					if	(!(PresDetailList.size() == 1 && Constants.RT_LOAD.equals(PresDetailList.get(0).getRowStatus()))) //??????????????????????????????
					{
						//??????????????????????????????????????????????????????????????????
						if	(!DeletePresDetail(vPres))
						{
							throw new BusException("???????????????????????????");
						}
						Map<String,Object> storePriceMap = new HashMap<String,Object>();
						Map<String,Object> quanMap = new HashMap<String,Object>();
						List<String> pkPds = new ArrayList<String>();
						for (OpCgCnOrder co: PresDetailList)
						{ 
							pkCnOrderSet.add(co.getPkCnord());
							pkPds.add(co.getPkOrd());
							quanMap.put(co.getPkOrd(), MathUtils.mul(co.getQuanCg(), co.getPackSize()));
							// ???????????????????????????
//							List<PdOutDtParamVo> pdOutDtParamVos = pdStOutPubService.getPdStorePrice(co.getPkDeptExec(), null, co.getPkOrd(), co.getQuanCg(), null,false);
//							PdOutDtParamVo pdOutDtParamVo = pdOutDtParamVos.get(0);
//							co.setDateExpire(pdOutDtParamVo.getDateExpire());
//							co.setPriceCg(pdOutDtParamVo.getPriceStore());
//							co.setPriceCost(pdOutDtParamVo.getPriceCost());
//							co.setBatchNo(pdOutDtParamVo.getBatchNo());
//							co.setPackSize(pdOutDtParamVo.getPackSize().doubleValue());
//							co.setTs(new Date());
							OrderUpdateList.add(co);
						}
						if(OrderUpdateList.size()>0){
							storePriceMap.put("pkStore", "");
							storePriceMap.put("pkDeptExec", OrderUpdateList.get(0).getPkDeptExec());
							storePriceMap.put("quanMap", quanMap);
							storePriceMap.put("pkPd", pkPds);
							List<PdOutDtParamVo> pdOutDtParamVos = pdStOutPubService.getPdStorePrice(storePriceMap,false);
							for(OpCgCnOrder co : OrderUpdateList){
								for(PdOutDtParamVo pdOutDtParamVo:pdOutDtParamVos){
									if(co.getPkOrd().equals(pdOutDtParamVo.getPkPd())){
										co.setDateExpire(pdOutDtParamVo.getDateExpire());
										co.setPriceCg(pdOutDtParamVo.getPriceStore());
										co.setPriceCost(pdOutDtParamVo.getPriceCost());
										co.setBatchNo(pdOutDtParamVo.getBatchNo());
										co.setPackSize(pdOutDtParamVo.getPackSize().doubleValue());
										co.setTs(new Date());
										break;
									}
								}
							}
						}
					}
					dateZeroForPress(vPres);
					if (!ApplicationUtils.getPropertyValue("workspaceIsSy", "").equals("yes")) {// ?????????????????????????????????
						//???????????????????????????Ts
						tsOld=vPres.getTs();
						if(tsOld==null){
							throw new BusException("?????????????????????Ts???");
						}
						//??????????????????Ts
						List<CnPrescription> queryForList = DataBaseHelper.queryForList("select * from CN_PRESCRIPTION where pk_pres=? and del_flag='0'", CnPrescription.class, vPres.getPkPres());
						
						if(queryForList==null||queryForList.size()==0||queryForList.get(0).getTs()==null||!(format.format(queryForList.get(0).getTs()).equals(format.format(tsOld))))
							throw new BusException("???????????????????????????,???????????????????????????");
					}
					DataBaseHelper.updateBeanByPk(vPres, false);
					List<CnPrescription> queryForList = DataBaseHelper.queryForList("select * from CN_PRESCRIPTION where pk_pres=? and del_flag='0'", CnPrescription.class, vPres.getPkPres());
				    if(queryForList!=null&&queryForList.size()>0) tsNew = format.format(queryForList.get(0).getTs());
				}
				else if(Constants.RT_REMOVE.equals(vPres.getRowStatus()))
				{	
					//??????????????????????????????????????????????????????????????????
					if	(!DeletePresDetail(vPres))
					{
						throw new BusException("???????????????????????????");
					}
					if (!ApplicationUtils.getPropertyValue("workspaceIsSy", "").equals("yes")) {// ?????????????????????????????????
						//???????????????????????????Ts
						tsOld=vPres.getTs();
						if(tsOld==null){
							throw new BusException("?????????????????????Ts???");
						}
						//??????????????????Ts
						List<CnPrescription> queryForList = DataBaseHelper.queryForList("select * from CN_PRESCRIPTION where pk_pres=? and del_flag='0'", CnPrescription.class, vPres.getPkPres());
						
						if(queryForList==null||queryForList.size()==0||queryForList.get(0).getTs()==null||!(format.format(queryForList.get(0).getTs()).equals(format.format(tsOld))))
							throw new BusException("???????????????????????????,???????????????????????????");
					}
					DataBaseHelper.deleteBeanByPk(vPres);	
				}
			}
		}

		String sEffeDate = ApplicationUtils.getSysparam("CN0004", false);
		int IntEffeDate = 3;
		if (sEffeDate != null)
			IntEffeDate = Integer.parseInt(sEffeDate);
		for(OpCgCnOrder o : OrderInsertList) o = dateZeroForOpCgCnOrder(o, IntEffeDate);
		for(OpCgCnOrder o : OrderUpdateList) o = dateZeroForOpCgCnOrder(o, IntEffeDate);
//		if(PresChargeList!=null&&PresChargeList.size()>0){
//			for(BlOpDt o : PresChargeList) dateZeroForBlOpDt(o);
//		}

		//??????order?????????????????????bd_ord_dt
		if (OrderInsertList.size() > 0) {
			// ??????????????????
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), OrderInsertList);
			drugOpCg_temp(OrderInsertList,bodst,pvInfo);
			//drugOpCg(OrderInsertList, pvInfo);
		}
		if (OrderUpdateList.size() > 0) {
			// ????????????????????????
			//			String sql = "delete from bl_op_dt op where op.pk_cnord in (" + BlcgUtil.convertSetToSqlInPart(pkCnOrderSet, "pk_cnord") + ")";
			//			DataBaseHelper.execute(sql, new Object[] {});

			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), OrderUpdateList);
			drugOpCg_temp(OrderUpdateList,bodst,pvInfo);
			//drugOpCg(OrderUpdateList, pvInfo);
		}
		////????????????????????????????????????????????????????????????????????????
//		if(PresChargeList!=null&&PresChargeList.size()>0){
//			for (BlOpDt item: PresChargeList)
//			{
//				ApplicationUtils.setDefaultValue(item, true);
//			}
//		}
		
		if	(PresChargeList!=null&&PresChargeList.size()>0) 
			drugOpCgSupplyItem(PresChargeList,bodst,pvInfo);
			//DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), PresChargeList);
		if(bodst.size()>0) opCgPubService.blOpCgBatch(bodst);
		return "1"+tsNew!=null?","+tsNew:"";
	}
	
	private void drugOpCgSupplyItem(List<BlOpDt> presChargeList,
			List<BlPubParamVo> bodst, PvEncounter pvInfo) {
		User u = UserContext.getUser();
		String pkOrg = u.getPkOrg();
		String pkDept = u.getPkDept();
		List<BlPubParamVo> bods = new ArrayList<BlPubParamVo>();
		for (BlOpDt opCgCnOrder : presChargeList) {
			BlPubParamVo bpb = new BlPubParamVo();
			bpb.setPkOrg(pkOrg);
			bpb.setEuPvType(pvInfo.getEuPvtype());
			bpb.setPkPv(pvInfo.getPkPv());
			bpb.setPkPi(pvInfo.getPkPi());
		//	bpb.setPkOrd(opCgCnOrder.getPkOrd());
			bpb.setPkCnord(opCgCnOrder.getPkCnord());
			bpb.setPkPres(opCgCnOrder.getPkPres());
			bpb.setPkItem(opCgCnOrder.getPkItem());
			//bpb.setFlagPd(opCgCnOrder.getPkPres() == null ? "0" : "1");
			bpb.setFlagPd("0");
			opCgCnOrder.setQuanCg(opCgCnOrder.getQuan());
			bpb.setQuanCg(opCgCnOrder.getQuanCg()==null ? 0.0:opCgCnOrder.getQuanCg().doubleValue());
		//	bpb.setPkOrgEx(opCgCnOrder.getPkOrgExec());
			bpb.setPkOrgEx(opCgCnOrder.getPkOrgEx());
			bpb.setPkOrgApp(pvInfo.getPkOrg());
		//	bpb.setPkDeptEx(opCgCnOrder.getPkDeptExec());
			bpb.setPkDeptEx(opCgCnOrder.getPkDeptEx());
			bpb.setPkDeptApp(pvInfo.getPkDept());
		//	bpb.setPkEmpApp(opCgCnOrder.getPkEmpOrd());
			bpb.setPkEmpApp(opCgCnOrder.getPkEmpApp());
//			bpb.setNameEmpApp(opCgCnOrder.getNameEmpOrd());
			bpb.setNameEmpApp(opCgCnOrder.getNameEmpApp());
//			bpb.setNamePd(opCgCnOrder.getNameOrd());
			bpb.setNamePd(opCgCnOrder.getNameCg());
			bpb.setFlagPv("0");
			bpb.setDateExpire(opCgCnOrder.getDateExpire());
//			bpb.setPkUnitPd(opCgCnOrder.getPkUnitCg());
			bpb.setPkUnitPd(opCgCnOrder.getPkUnitPd());
			bpb.setPackSize(opCgCnOrder.getPackSize().intValue());
//			bpb.setPrice(opCgCnOrder.getPriceCg());
			bpb.setPrice(opCgCnOrder.getPrice());
			bpb.setPriceCost(opCgCnOrder.getPriceCost());
			bpb.setDateHap(new Date());
			bpb.setPkDeptCg(pkDept);
			bpb.setPkEmpCg(u.getPkEmp());
			bpb.setNameEmpCg(u.getNameEmp());
			bpb.setEuAdditem("1");//????????????????????????
			bods.add(bpb);
		}
		if(bodst!=null){
			bodst.addAll(bods);
		}
	}
	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	public void saveExOrderOcc(OpPrescriptionVo opPresAll) throws IllegalAccessException, InvocationTargetException{
		List<CnPrescription> PresList =  opPresAll.getCnPres();
		List<OpCgCnOrder> PresDetailList = opPresAll.getCnOrder();
		List<ExOrderOcc> OrderInsertList = new ArrayList<ExOrderOcc>();
		List<ExOrderOcc> OrderUpdateList = new ArrayList<ExOrderOcc>();

		Date date=new Date();
		for(int i=0; i<PresList.size(); i++){
			CnPrescription vPres = PresList.get(i);
			if(vPres!=null){
				if(Constants.RT_NEW.equals(vPres.getRowStatus()))
				{		
					if	(!(PresDetailList.size() == 1 && Constants.RT_LOAD.equals(PresDetailList.get(0).getRowStatus()))) //??????????????????????????????
					{
						for (OpCgCnOrder opCgCnOrder : PresDetailList) {//?????????????????????????????????
							if(opCgCnOrder.getPkPres().equals(vPres.getPkPres())){
								ExOrderOcc exOrderOcc=new ExOrderOcc();
								BeanUtils.copyProperties(exOrderOcc,
										opCgCnOrder);
								ApplicationUtils.setDefaultValue(exOrderOcc, true);
								exOrderOcc.setQuanCg(opCgCnOrder.getQuanCg());
								exOrderOcc.setPriceRef(opCgCnOrder.getPriceCg());
								exOrderOcc.setDatePlan(date);
								exOrderOcc.setPkUnitCg(opCgCnOrder.getPkUnitCg());
								exOrderOcc.setPkUnit(null);
								exOrderOcc.setEuStatus("0");
								OrderInsertList.add(exOrderOcc);
							}
						}

					}
				}
				else if(Constants.RT_UPDATE.equals(vPres.getRowStatus()))
				{
					DataBaseHelper.execute("delete occ from ex_order_occ occ inner join cn_order cn on occ.pk_cnord=cn.pk_cnord where cn.pk_pres = ?", vPres.getPkPres());	
					for (OpCgCnOrder opCgCnOrder : PresDetailList) {//?????????????????????????????????
						if(opCgCnOrder.getPkPres().equals(vPres.getPkPres())){
							ExOrderOcc exOrderOcc=new ExOrderOcc();
							BeanUtils.copyProperties(exOrderOcc,
									opCgCnOrder);
							ApplicationUtils.setDefaultValue(exOrderOcc, true);
							exOrderOcc.setQuanCg(opCgCnOrder.getQuanCg());
							exOrderOcc.setPriceRef(opCgCnOrder.getPriceCg());
							exOrderOcc.setDatePlan(date);
							exOrderOcc.setPkUnitCg(opCgCnOrder.getPkUnitCg());
							exOrderOcc.setPkUnit(null);
							exOrderOcc.setEuStatus("0");
							OrderUpdateList.add(exOrderOcc);
						}
					}
				}
				else if(Constants.RT_REMOVE.equals(vPres.getRowStatus()))
				{	
					String sql_del = "delete from ex_order_occ where pk_pres = ?";
					for (OpCgCnOrder opCgCnOrder : PresDetailList) {//?????????????????????????????????
						if(opCgCnOrder.getPkPres().equals(vPres.getPkPres())){
							DataBaseHelper.execute("delete from ex_order_occ where pk_cnord = ?", opCgCnOrder.getPkCnord());
						}
					}
				}
			}
		}

		//??????ExOrderOcc???
		if (OrderInsertList.size() > 0) {
			// ????????????????????????
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExOrderOcc.class), OrderInsertList);
		}
		if (OrderUpdateList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExOrderOcc.class), OrderUpdateList);
		}
	}

	
	/**
	 * ??????sql server???????????????01?????????
	 * @param cn
	 */
	private static void dateZeroForPress(CnPrescription cn){
		boolean flag_create = false; //????????????????????????
		int year = -1;

		if(null == cn.getCreateTime()){
			cn.setCreateTime(new Date());
		}else{		
			year = cn.getCreateTime().getYear();
			if(year < 0) cn.setCreateTime(new Date());
			else flag_create = true;
		}

		if(null == cn.getModityTime()) cn.setModityTime(new Date());

		year = cn.getModityTime().getYear();
		if(year < 0){
			if(flag_create) cn.setModityTime(new Date());
			else cn.setModityTime(null);
		}

		if(null == cn.getTs()) cn.setTs(new Date());
		year = cn.getTs().getYear();
		if(year < 0) cn.setTs(new Date());
	}

	public static Date getDateAfter(Date d, int day) {  
		Calendar now = Calendar.getInstance();  
		now.setTime(d);  
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);  
		return now.getTime();  
	}

	/**
	 * ??????sql server???????????????01?????????, ???????????????????????????and ????????????ordsn_parent
	 * @param cn
	 * @param EffeDate  ????????????????????????
	 */
	private static OpCgCnOrder dateZeroForOpCgCnOrder(OpCgCnOrder cn, int EffeDate){
		Date dt = new Date();
		Date dtEffe = getDateAfter(dt, EffeDate);
		cn.setDateEffe(dtEffe);
		cn.setDateSign(dt);
		if(cn.getDateStart()==null){
			cn.setDateStart(dt); 
		}
		cn.setFlagFirst("0");
		cn.setFlagDurg("0");
		//cn.setFlagSelf("0");
		cn.setFlagNote("0");		
		cn.setFlagBase("0");
		cn.setFlagBl("0");
		cn.setFlagStop("0");
		cn.setFlagStopChk("0");				
		cn.setFlagErase("0");
		cn.setFlagEraseChk("0");
		cn.setFlagCp("0");
		cn.setFlagDoctor("1");		//????????????
		cn.setFlagPrint("0");
		cn.setFlagMedout("0");
		cn.setFlagEmer("0");
		cn.setFlagThera("0");				
		cn.setFlagPrev("0");		
		cn.setFlagFit("0");
		cn.setFlagSign("0");
		cn.setFlagDurg("1");  //????????????
		cn.setOrds((long) 1);
		if	(cn.getPkOrg() == null)  //????????????
			cn.setPkOrg(UserContext.getUser().getPkOrg());
		if (cn.getOrdsnParent() == 0)  //???????????????ordsn_parent=ordsn
			cn.setOrdsnParent(cn.getOrdsn());


		boolean flag_create = false; //????????????????????????
		int year = -1;

		if(null == cn.getCreateTime()){
			cn.setCreateTime(new Date());
		}else{		
			year = cn.getCreateTime().getYear();
			if(year < 0) cn.setCreateTime(new Date());
			else flag_create = true;
		}

		if(null == cn.getModityTime()) cn.setModityTime(new Date());

		year = cn.getModityTime().getYear();
		if(year < 0){
			if(flag_create) cn.setModityTime(new Date());
			else cn.setModityTime(null);
		}

		if(null == cn.getTs()) cn.setTs(new Date());
		year = cn.getTs().getYear();
		if(year < 0) cn.setTs(new Date());
		return cn;
	}

	/**
	 * ??????sql server???????????????01?????????
	 * @param cn
	 */
	private static void dateZeroForBlOpDt(BlOpDt cn){
		int year = -1;

		if(null == cn.getCreateTime()){
			cn.setCreateTime(new Date());
		}else{		
			year = cn.getCreateTime().getYear();
			if(year < 0) cn.setCreateTime(new Date());
		}

		if(null != cn.getDateCg()){	
			year = cn.getDateCg().getYear();
			if(year < 0) cn.setDateCg(new Date());
		}

		if(null != cn.getDateExpire()){
			year = cn.getDateExpire().getYear();
			if(year < 0) cn.setDateExpire(new Date());
		}

		if(null != cn.getDateHap()){
			year = cn.getDateHap().getYear();
			if(year < 0) cn.setDateHap(new Date());
		}

		if(null == cn.getTs()) cn.setTs(new Date());
		year = cn.getTs().getYear();
		if(year < 0) cn.setTs(new Date());
	}

	private boolean DeletePresDetail(CnPrescription cnPres)
	{
		Boolean ret = false;
		String sql_select = "select count (*) from cn_order where pk_pres = ? and pk_cnord in (select pk_cnord from bl_op_dt " +
				"where pk_pres = ? and flag_settle = '0')";
		int iCount = DataBaseHelper.queryForScalar(sql_select, Integer.class, cnPres.getPkPres(), cnPres.getPkPres());
		//??????????????????
		String sql_del = "delete from bl_op_dt where pk_cnord in (select pk_cnord from cn_order ord where ord.pk_pres = ?)";
		DataBaseHelper.execute(sql_del, cnPres.getPkPres());	
		//???????????????
		sql_del = "delete from cn_order where pk_pres = ?";
		int iDeleteCount = DataBaseHelper.execute(sql_del, cnPres.getPkPres());	
		if (iCount == iDeleteCount)
			ret = true;
		else
			ret = false;
		return ret;
	}
	private void drugOpCg_temp(List<OpCgCnOrder> occo,List<BlPubParamVo> bodst, PvEncounter pvInfo) {
		User u = UserContext.getUser();
		String pkOrg = u.getPkOrg();
		String pkDept = u.getPkDept();
		List<BlPubParamVo> bods = new ArrayList<BlPubParamVo>();
		for (OpCgCnOrder opCgCnOrder : occo) {
			BlPubParamVo bpb = new BlPubParamVo();
			bpb.setPkOrg(pkOrg);
			bpb.setEuPvType(pvInfo.getEuPvtype());
			bpb.setPkPv(pvInfo.getPkPv());
			bpb.setPkPi(pvInfo.getPkPi());
			bpb.setPkOrd(opCgCnOrder.getPkOrd());
			bpb.setPkCnord(opCgCnOrder.getPkCnord());
			bpb.setPkPres(opCgCnOrder.getPkPres());
			bpb.setPkItem(null);
			bpb.setQuanCg(opCgCnOrder.getQuanCg());
			bpb.setPkOrgEx(opCgCnOrder.getPkOrgExec());
			bpb.setPkOrgApp(pvInfo.getPkOrg());
			bpb.setPkDeptEx(opCgCnOrder.getPkDeptExec());
			bpb.setPkDeptApp(pvInfo.getPkDept());
			bpb.setPkEmpApp(opCgCnOrder.getPkEmpOrd());
			bpb.setNameEmpApp(opCgCnOrder.getNameEmpOrd());
			bpb.setFlagPd(opCgCnOrder.getPkPres() == null ? "0" : "1");
			bpb.setNamePd(opCgCnOrder.getNameOrd());
			bpb.setFlagPv("0");
			bpb.setDateExpire(opCgCnOrder.getDateExpire());
			bpb.setPkUnitPd(opCgCnOrder.getPkUnitCg());
			bpb.setPackSize(opCgCnOrder.getPackSize().intValue());
			bpb.setPrice(opCgCnOrder.getPriceCg());
			bpb.setPriceCost(opCgCnOrder.getPriceCost());
			bpb.setDateHap(new Date());
			bpb.setPkDeptCg(pkDept);
			bpb.setPkEmpCg(u.getPkEmp());
			bpb.setNameEmpCg(u.getNameEmp());			
			bods.add(bpb);
		}
		if(bodst!=null){
			bodst.addAll(bods);
		}
		// ????????????
		//opcgPubService.blOpCg(bods);	yangxue ??????
	//	opcgPubService.blOpCgBatch(bods);
	//	opcgPubService.blOpCgPds(bods);	// @todo lxw :???????????????
	}	
	private void drugOpCg(List<OpCgCnOrder> occo, PvEncounter pvInfo) {
		User u = UserContext.getUser();
		String pkOrg = u.getPkOrg();
		String pkDept = u.getPkDept();
		List<BlPubParamVo> bods = new ArrayList<BlPubParamVo>();
		for (OpCgCnOrder opCgCnOrder : occo) {
			BlPubParamVo bpb = new BlPubParamVo();
			bpb.setPkOrg(pkOrg);
			bpb.setEuPvType(pvInfo.getEuPvtype());
			bpb.setPkPv(pvInfo.getPkPv());
			bpb.setPkPi(pvInfo.getPkPi());
			bpb.setPkOrd(opCgCnOrder.getPkOrd());
			bpb.setPkCnord(opCgCnOrder.getPkCnord());
			bpb.setPkPres(opCgCnOrder.getPkPres());
			bpb.setPkItem(null);
			bpb.setQuanCg(opCgCnOrder.getQuanCg());
			bpb.setPkOrgEx(opCgCnOrder.getPkOrgExec());
			bpb.setPkOrgApp(pvInfo.getPkOrg());
			bpb.setPkDeptEx(opCgCnOrder.getPkDeptExec());
			bpb.setPkDeptApp(pvInfo.getPkDept());
			bpb.setPkEmpApp(opCgCnOrder.getPkEmpOrd());
			bpb.setNameEmpApp(opCgCnOrder.getNameEmpOrd());
			bpb.setFlagPd(opCgCnOrder.getPkPres() == null ? "0" : "1");
			bpb.setNamePd(opCgCnOrder.getNameOrd());
			bpb.setFlagPv("0");
			bpb.setDateExpire(opCgCnOrder.getDateExpire());
			bpb.setPkUnitPd(opCgCnOrder.getPkUnitCg());
			bpb.setPackSize(opCgCnOrder.getPackSize().intValue());
			bpb.setPrice(opCgCnOrder.getPriceCg());
			bpb.setPriceCost(opCgCnOrder.getPriceCost());
			bpb.setDateHap(new Date());
			bpb.setPkDeptCg(pkDept);
			bpb.setPkEmpCg(u.getPkEmp());
			bpb.setNameEmpCg(u.getNameEmp());			
			bods.add(bpb);
		}
		// ????????????
		opCgPubService.blOpCg(bods);	
		//opcgPubService.blOpCgBatch(bods); ?????????????????????????????????????????????????????????????????????
	//	opcgPubService.blOpCgPds(bods);	// @todo lxw :???????????????
	}	

	public List<Map<String,Object>> getFreqTimes(String param, IUser user){
		String sql = " select a.*, b.code,b.eu_cycle,b.cnt,b.cnt_cycle from bd_term_freq_time a inner join bd_term_freq b on a.pk_freq = b.pk_freq";
		return DataBaseHelper.queryForList(sql);
	}
	
	public List<Map<String,Object>> GetPiCurrMonthPdDays(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OpOrder.GetPiCurrMonthPdDays(map);
	}

	public List<Map<String,Object>> getSupplyItem(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OpOrder.getSupplyItem(map);
	}

	public int getPresSettled(String param, IUser user){
		CnPrescription vPres = JsonUtil.readValue(param, CnPrescription.class);
		int ret = DataBaseHelper.queryForScalar("select count(*) from bl_op_dt where pk_pres = ? and flag_settle = '1'", Integer.class,  vPres.getPkPres());
		return ret;
	}
	/**
	 * ????????????????????????????????????5???????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Page<ExLabOccVo> getInspectionReport(String param, IUser user){
		ExLabOccVo exLabOccVo = JsonUtil.readValue(param, ExLabOccVo.class);
		MyBatisPage.startPage(exLabOccVo.getPageNum(), exLabOccVo.getPageSize());
		String pkpi = exLabOccVo.getPkPi();
		List<ExLabOccVo> list = new ArrayList<ExLabOccVo>();
		if (Application.isSqlServer()) {
			list = exLabOccVoMapper.getExLabOccVo(pkpi);//??????????????????		
		} else {
			list = exLabOccVoMapper.getExLabOccVoOracle(pkpi);//??????????????????	
		}

		Page<ExLabOccVo> page = MyBatisPage.getPage();
		page.setRows(list);
		return page;

	}
	/**
	 * ????????????????????????????????????5???????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Page<ExRisOccVo> getExaminationReport(String param, IUser user){
		ExRisOccVo exRisOccVo = JsonUtil.readValue(param, ExRisOccVo.class);
		MyBatisPage.startPage(exRisOccVo.getPageNum(), exRisOccVo.getPageSize());
		String pkpi = exRisOccVo.getPkPi();
		List<ExRisOccVo> list = new ArrayList<ExRisOccVo>();
		if (Application.isSqlServer()) {
			list = exRisOccVoMapper.getExRisOccVo(pkpi);//??????????????????	
		} else {
			list = exRisOccVoMapper.getExRisOccVoOracle(pkpi);//??????????????????	
		}

		Page<ExRisOccVo> page = MyBatisPage.getPage();
		page.setRows(list);
		return page;

	}

	/**
	 * ????????????(??????)
	 * 017003001026
	 * 004003007005???????????????004003007014???????????????????????????004004005001???????????????004004004002????????????
	 * @param param
	 * @param user
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<Map> saveCnOrderAndExCnOrder(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
		List<CnOrderBlOpDtVo> cnOrderBlOpDtVos= JSON.parseArray(param,
				CnOrderBlOpDtVo.class);

		//????????????????????????????????????
		OpApplyRecordVo opApplyRecordVo=new OpApplyRecordVo();
		//????????????????????????
		//List<CnConsultApplyVO> cnConsultApplyVOs=test.getCodeApplys();
		List<CnConsultApplyVO> cnConsultApplyVOs=new ArrayList<CnConsultApplyVO>();
		//?????????????????????????????????
		List<BloodApplyVO> bloodApplyVOs=new ArrayList<BloodApplyVO>();
		String ordtype="";

		List<CnOrder> bloodOrds =new ArrayList<CnOrder>();

		//?????????????????????????????????????????????
		List<OpCgCnOrder> applyRecords=new ArrayList<OpCgCnOrder>();
		//???????????????????????????
		List<CnRisApply> cnRisApplies=new ArrayList<CnRisApply>();
		//???????????????????????????
		List<CnLabApply> cnLabApplies=new ArrayList<CnLabApply>();
		
		List<ExOrderOcc> exOrderOccs=new ArrayList<ExOrderOcc>();
		
		if(cnOrderBlOpDtVos!=null&&cnOrderBlOpDtVos.size()>0){
			
			int count=DataBaseHelper.queryForScalar("select count(1) from pv_encounter where pk_pv=? and del_flag='0' and eu_status='2'", Integer.class, cnOrderBlOpDtVos.get(0).getPkPv());
			
			String sql="";
			
			if(count!=0)
				throw new BusException("??????????????????,???????????????"); 
				
			List<CnPrescription> cnPress=new ArrayList<CnPrescription>();
			for(CnOrderBlOpDtVo cnOrderBlOpDtVo : cnOrderBlOpDtVos) {
				if(cnOrderBlOpDtVo.getCnPrescription()!=null){
					cnPress.add(cnOrderBlOpDtVo.getCnPrescription());
				}
			}

			for (CnOrderBlOpDtVo cnOrderBlOpDtVo : cnOrderBlOpDtVos) {

				ordtype=cnOrderBlOpDtVo.getCodeOrdtype();
				OpCgCnOrder opCgCnOrder=new OpCgCnOrder();
				BeanUtils.copyProperties(opCgCnOrder, 
						cnOrderBlOpDtVo);
				if(cnOrderBlOpDtVo.getEuStatusOrd().equals("1")){
					ExOrderOcc exOrderOcc=new ExOrderOcc();
					BeanUtils.copyProperties(exOrderOcc,
							cnOrderBlOpDtVo);
					ApplicationUtils.setDefaultValue(exOrderOcc, true);
					exOrderOcc.setQuanCg(cnOrderBlOpDtVo.getQuanCg());
					exOrderOcc.setPriceRef(cnOrderBlOpDtVo.getPriceCg());
					exOrderOcc.setDatePlan(new Date());
					exOrderOcc.setPkUnitCg(cnOrderBlOpDtVo.getPkUnitCg());
					exOrderOcc.setPkUnit(null);
					exOrderOcc.setEuStatus("0");
					exOrderOccs.add(exOrderOcc);
					//??????????????????
					//DataBaseHelper.insertBean(exOrderOcc);
				}
				if(ordtype.startsWith("01")&&cnOrderBlOpDtVo.getCnPrescription()!=null){//??????????????????
					//????????????????????????
					OpPrescriptionVo opPrescriptionVo=new OpPrescriptionVo();
					//?????????????????????????????????
					List<OpCgCnOrder> prescriptions=new ArrayList<OpCgCnOrder>();
					//?????????????????????????????????
					List<BlOpDt> prescriptionDts = new ArrayList<BlOpDt>();
					//?????????????????????
					List<CnPrescription> PresList =  new ArrayList<CnPrescription>();
					CnPrescription cnPrescription=cnOrderBlOpDtVo.getCnPrescription();
					PresList.add(cnPrescription);

					if(cnOrderBlOpDtVo.getBlOpDt()!=null){
						prescriptionDts=cnOrderBlOpDtVo.getBlOpDt();
					}
					prescriptions.add(opCgCnOrder);
					for (CnOrderBlOpDtVo cnorderVo : cnOrderBlOpDtVos) {
						if(cnorderVo.getCnPrescription()==null&&cnPrescription.getPkPres().equals(cnorderVo.getPkPres())){
							OpCgCnOrder cnOrder=new OpCgCnOrder();
							BeanUtils.copyProperties(cnOrder, 
									cnorderVo);

							if(cnorderVo.getBlOpDt()!=null){
								prescriptionDts.addAll(cnorderVo.getBlOpDt());
							}

							prescriptions.add(cnOrder);
						}
					}
					//????????????
					opPrescriptionVo.setCnOrder(prescriptions);
					opPrescriptionVo.setCnPres(PresList);
					opPrescriptionVo.setCnOrderCharge(prescriptionDts);
					savePrescriptionNems(opPrescriptionVo,user);

				}else if(ordtype.startsWith("02")||ordtype.startsWith("03")||ordtype.startsWith("05")||ordtype.startsWith("06")||ordtype.startsWith("07")){//??????????????????????????????
					if(cnOrderBlOpDtVo.getCnRisApply()==null){
						/*//?????????????????????????????????
						DataBaseHelper.update("update cn_order set eu_status_ord='1' where pk_cnord = '"+cnOrderBlOpDtVo.getPkCnord()+"'" );*/
						sql=sql+"'"+cnOrderBlOpDtVo.getPkCnord()+"',";
						cnOrderBlOpDtVo.setCnRisApply(new CnRisApply());
					}
					if(cnOrderBlOpDtVo.getCnLabApply()==null){
						cnOrderBlOpDtVo.setCnLabApply(new CnLabApply());
					}

					cnRisApplies.add(cnOrderBlOpDtVo.getCnRisApply());
					cnLabApplies.add(cnOrderBlOpDtVo.getCnLabApply());
					applyRecords.add(opCgCnOrder);
				}else if(ordtype.startsWith("0903")){//??????????????????
					CnConsultApplyVO cnConsultApplyVO=new CnConsultApplyVO();
					BeanUtils.copyProperties(cnConsultApplyVO,
							cnOrderBlOpDtVo.getCnConsultApply());
					CnOrder order=new CnOrder();
					BeanUtils.copyProperties(order,
							cnOrderBlOpDtVo);
					cnConsultApplyVO.setOrder(order);
					cnConsultApplyVOs.add(cnConsultApplyVO);
				}else if(ordtype.startsWith("12")){//??????????????????
					/*BloodApplyVO bloodApplyVO=new BloodApplyVO();
					BeanUtils.copyProperties(bloodApplyVO,
							cnOrderBlOpDtVo);*/
					bloodApplyVOs.add(cnOrderBlOpDtVo.getBloodApply());
				}else if(ordtype.startsWith("99")){//????????????
					CnOrder order=new CnOrder();
					BeanUtils.copyProperties(order,
							cnOrderBlOpDtVo);
					ApplicationUtils.setDefaultValue(order,true);
					DataBaseHelper.insertBean(order);
				}

			}
			if(sql.length()>0){
				sql="update cn_order set eu_status_ord='1' where pk_cnord in ("+sql;
				//?????????????????????????????????
				sql=sql.substring(0,sql.length()-1)+")";
				DataBaseHelper.update(sql);
			}
			
			
			//????????????????????????
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExOrderOcc.class), exOrderOccs);
			//??????????????????
			cnConsultApplyService.saveOrUpdateCnConsultApply(cnConsultApplyVOs,user);

			//??????????????????
			opApplyRecordVo.setOpApply(applyRecords);
			opApplyRecordVo.setOpRis(cnRisApplies);
			opApplyRecordVo.setOpLab(cnLabApplies);
			opApplyService.saveApplyRecordNems(opApplyRecordVo,user);

			//?????????????????????????????????
			bloodOrds = bloodService.saveOrUpdateBloodApply(bloodApplyVOs,user);
		}
		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // ??????????????????????????????????????????????????????????????????
		TransactionStatus status = transactionManager.getTransaction(def); // ??????????????????
		try {
			// ???????????????????????????????????????????????????
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
		}*/
		List<Map> temp=new ArrayList<Map>();
		//??????????????????????????????
		if(bloodOrds!=null&&bloodOrds.size()>0){
			String pkPv = bloodOrds.get(0).getPkPv();
			Map params=new HashMap();
			List<String> pkPvs=new ArrayList<String>();
			pkPvs.add(pkPv);
			params.put("pkPvs", pkPvs);
			List<Map> result=OpOrder.listCnOrder(params);
			for (Map map : result) {
				String type=(String) map.get("CodeOrdType");
				if(type.startsWith("12")){
					Map bloodApply = new HashMap();
					bloodApply.putAll(map);
					CnTransApply cnTransApply=DataBaseHelper.queryForBean("select * from CN_TRANS_APPLY where pk_cnord=?",CnTransApply.class,map.get("PkCnord"));
					Map bloodPatientVO=ApplicationUtils.beanToMap(cnTransApply);
					bloodApply.put("bloodPatientVO", bloodPatientVO);
					map.put("bloodApply", bloodApply);

					temp.add(map);
				}
			}



		}

		//??????????????????????????????
		if(cnConsultApplyVOs.size()>0){
			String pkPv = cnConsultApplyVOs.get(0).getOrder().getPkPv();
			Map params=new HashMap();
			List<String> pkPvs=new ArrayList<String>();
			pkPvs.add(pkPv);
			params.put("pkPvs", pkPvs);
			List<Map> result=OpOrder.listCnOrder(params);
			for (Map map : result) {
				String type=(String) map.get("CodeOrdType");
				if(type.startsWith("0903")){
					//???????????????????????????????????????
					CnConsultApplyVO cnConsultApplyVO=DataBaseHelper.queryForBean("select * from cn_consult_apply where pk_cnord=?",CnConsultApplyVO.class, map.get("PkCnord"));
					Map consultMap = ApplicationUtils.beanToMap(cnConsultApplyVO);
					List<CnConsultResponse> cnConsultResponses=DataBaseHelper.queryForList("select * from CN_CONSULT_RESPONSE where pk_cons=?",CnConsultResponse.class, cnConsultApplyVO.getPkCons());
					Map resps=new HashMap();
					consultMap.put("resps",cnConsultResponses); 
					map.put("cnConsultApply",consultMap);
					temp.add(map);
				}
			}
		}
		
		return temp;
	}
	/**
	 * ????????????
	 * @param param
	 * @param user
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void submitCnOrder(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnOrderBlOpDtVo> cnOrderBlOpDtVos= JSON.parseArray(param,
				CnOrderBlOpDtVo.class);
		if(cnOrderBlOpDtVos!=null&&cnOrderBlOpDtVos.size()>0){
			List<ExOrderOcc> exOrderOccs=new ArrayList<ExOrderOcc>();
			
			String sql="update cn_order set eu_status_ord='1' where pk_cnord in (";
			
			for (CnOrderBlOpDtVo cnOrderBlOpDtVo : cnOrderBlOpDtVos) {
				sql=sql+"'"+cnOrderBlOpDtVo.getPkCnord()+"',";
				/*//?????????????????????????????????
				DataBaseHelper.update("update cn_order set eu_status_ord='1' where pk_cnord = '"+cnOrderBlOpDtVo.getPkCnord()+"'" );*/
				ExOrderOcc exOrderOcc=new ExOrderOcc();
				BeanUtils.copyProperties(exOrderOcc,
						cnOrderBlOpDtVo);
				ApplicationUtils.setDefaultValue(exOrderOcc, true);
				exOrderOcc.setQuanCg(cnOrderBlOpDtVo.getQuanCg());
				exOrderOcc.setPriceRef(cnOrderBlOpDtVo.getPriceCg());
				exOrderOcc.setDatePlan(new Date());
				exOrderOcc.setPkUnitCg(cnOrderBlOpDtVo.getPkUnitCg());
				exOrderOcc.setPkUnit(null);
				exOrderOcc.setEuStatus("0");
				exOrderOccs.add(exOrderOcc);
				/*//??????????????????
				DataBaseHelper.insertBean(exOrderOcc);*/
			}
			sql=sql.substring(0,sql.length()-1)+")";
			DataBaseHelper.update(sql);
			//????????????????????????
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExOrderOcc.class), exOrderOccs);
		}
	}
	/**
	 * ????????????
	 * @param param
	 * @param user
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void delCnOrder(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnOrderBlOpDtVo> cnOrderBlOpDtVos= JSON.parseArray(param,
				CnOrderBlOpDtVo.class);

		//????????????????????????
		OpPrescriptionVo opPrescriptionVo=new OpPrescriptionVo();
		//????????????????????????????????????
		OpApplyRecordVo opApplyRecordVo=new OpApplyRecordVo();
		//????????????????????????
		//List<CnConsultApplyVO> cnConsultApplyVOs=test.getCodeApplys();
		List<CnConsultApplyVO> cnConsultApplyVOs=new ArrayList<CnConsultApplyVO>();
		//????????????????????????
		List<BloodApplyVO> bloodApplyVOs=new ArrayList<BloodApplyVO>();


		//?????????????????????????????????
		List<OpCgCnOrder> prescriptions=new ArrayList<OpCgCnOrder>();
		//?????????????????????????????????
		List<BlOpDt> prescriptionDts = new ArrayList<BlOpDt>();
		//?????????????????????
		List<CnPrescription> PresList =  new ArrayList<CnPrescription>();

		String ordtype="";
		if(cnOrderBlOpDtVos!=null&&cnOrderBlOpDtVos.size()>0){
			for (CnOrderBlOpDtVo cnOrderBlOpDtVo : cnOrderBlOpDtVos) {

				ordtype=cnOrderBlOpDtVo.getCodeOrdtype();
				OpCgCnOrder opCgCnOrder=new OpCgCnOrder();
				BeanUtils.copyProperties(opCgCnOrder,
						cnOrderBlOpDtVo);
				opCgCnOrder.setRowStatus(Constants.RT_REMOVE);

				Map<String, String> pkCnord=new HashMap<String, String>();
				if(ordtype.startsWith("01")){//????????????????????????
					CnPrescription cnPrescription=cnOrderBlOpDtVo.getCnPrescription();
					if(cnPrescription!=null){
						cnPrescription.setRowStatus(Constants.RT_REMOVE);
						PresList.add(cnPrescription);
					}
					if(cnOrderBlOpDtVo.getBlOpDt()!=null){
						PresList.add(cnPrescription);
						prescriptionDts=cnOrderBlOpDtVo.getBlOpDt();
					}
					prescriptions.add(opCgCnOrder);
					opPrescriptionVo.setCnPres(PresList);
				}else if(ordtype.startsWith("02")){//????????????
					pkCnord.put("pkCnord", cnOrderBlOpDtVo.getPkCnord());
					opApplyService.delRisApply(pkCnord);
				}
				//????????????????????????
				else if(ordtype.startsWith("03")){
					pkCnord.put("pkCnord", cnOrderBlOpDtVo.getPkCnord());
					opApplyService.delLabApply(pkCnord);

				}
				//????????????????????????????????????
				else if(ordtype.startsWith("05")||ordtype.startsWith("06")||ordtype.startsWith("07")){
					pkCnord.put("pkCnord", cnOrderBlOpDtVo.getPkCnord());
					opApplyService.delCnOrd(pkCnord);
				}
				//????????????
				else if(ordtype.startsWith("0903")){
					CnConsultApplyVO cnConsultApplyVO=new CnConsultApplyVO();
					BeanUtils.copyProperties(cnConsultApplyVO,
							cnOrderBlOpDtVo.getCnConsultApply());
					CnOrder order=new CnOrder();
					BeanUtils.copyProperties(order,
							cnOrderBlOpDtVo);
					cnConsultApplyVO.setOrder(order);

					cnConsultApplyService.delApply(cnConsultApplyVO);
				}
				//????????????
				else if(ordtype.startsWith("12")){
					BloodApplyVO bloodApply = cnOrderBlOpDtVo.getBloodApply();
					BeanUtils.copyProperties(bloodApply, cnOrderBlOpDtVo);
					bloodService.delApply(bloodApply);
				}
				//????????????
				else if(ordtype.startsWith("99")){
					DataBaseHelper.update("update cn_order set del_flag='1' where pk_cnord = '"+cnOrderBlOpDtVo.getPkCnord()+"'");
				}
			}

		}
		//??????????????????
		savePrescriptionNems(opPrescriptionVo,user);

	}
	
	/**
	 * ?????????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public String savePrescriptionNems(OpPrescriptionVo opPresAll,IUser user){
		List<CnPrescription> PresList =  opPresAll.getCnPres();
		List<OpCgCnOrder> PresDetailList = opPresAll.getCnOrder();
		List<BlOpDt> PresChargeList = opPresAll.getCnOrderCharge();
		List<OpCgCnOrder> OrderInsertList = new ArrayList<OpCgCnOrder>();
		List<OpCgCnOrder> OrderUpdateList = new ArrayList<OpCgCnOrder>();
		Set<String> pkCnOrderSet = new HashSet<String>();
		Map<String,Object> result=new HashMap<String, Object>();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); 
		Date tsOld=null;
		PvEncounter pvInfo = null;
		if	(PresList.size() == 0)
			return "";
			
		//?????????????????????????????????
		int  diagM = DataBaseHelper.queryForScalar("select count(1) from pv_diag where del_flag='0' and flag_maj='1'  and pk_pv=? ", Integer.class, new Object[]{ PresList.get(0).getPkPv()});
		if(diagM<=0)  return "0";
		pvInfo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, PresList.get(0).getPkPv());

		for(int i=0; i<PresList.size(); i++){
			CnPrescription vPres = PresList.get(i);
			if(vPres!=null){
				if(Constants.RT_NEW.equals(vPres.getRowStatus()))
				{		
					if	(!(PresDetailList.size() == 1 && Constants.RT_LOAD.equals(PresDetailList.get(0).getRowStatus()))) //??????????????????????????????
					{
						Map<String,Object> storePriceMap = new HashMap<String,Object>();
						Map<String,Object> quanMap = new HashMap<String,Object>();
						List<String> pkPds = new ArrayList<String>();
						for (OpCgCnOrder co: PresDetailList)
						{	
							ApplicationUtils.setDefaultValue(co, false);
							pkPds.add(co.getPkOrd());
							quanMap.put(co.getPkOrd(), MathUtils.mul(co.getQuanCg(), co.getPackSize()));
//							// ???????????????????????????
//							List<PdOutDtParamVo> pdOutDtParamVos = pdStOutPubService.getPdStorePrice(co.getPkDeptExec(), null, co.getPkOrd(), co.getQuanCg(), null,false);
//							PdOutDtParamVo pdOutDtParamVo = pdOutDtParamVos.get(0);
//							co.setDateExpire(pdOutDtParamVo.getDateExpire());
//							co.setPriceCg(pdOutDtParamVo.getPriceStore());
//							co.setPriceCost(pdOutDtParamVo.getPriceCost());
//							co.setBatchNo(pdOutDtParamVo.getBatchNo());
//							co.setPackSize(pdOutDtParamVo.getPackSize().doubleValue());		
							OrderInsertList.add(co);
						}
						if(OrderInsertList.size()>0){
							storePriceMap.put("pkStore", "");
							storePriceMap.put("pkDeptExec", OrderInsertList.get(0).getPkDeptExec());
							storePriceMap.put("quanMap", quanMap);
							storePriceMap.put("pkPd", pkPds);
							List<PdOutDtParamVo> pdOutDtParamVos = pdStOutPubService.getPdStorePrice(storePriceMap,false);
							for(OpCgCnOrder co : OrderInsertList){
								for(PdOutDtParamVo pdOutDtParamVo:pdOutDtParamVos){
									if(co.getPkOrd().equals(pdOutDtParamVo.getPkPd())){
										co.setDateExpire(pdOutDtParamVo.getDateExpire());
										co.setPriceCg(pdOutDtParamVo.getPriceStore());
										co.setPriceCost(pdOutDtParamVo.getPriceCost());
										co.setBatchNo(pdOutDtParamVo.getBatchNo());
										co.setPackSize(pdOutDtParamVo.getPackSize().doubleValue());
										co.setTs(new Date());
										break;
									}
								}
							}
						}
					}
					dateZeroForPress(vPres);
					DataBaseHelper.insertBean(vPres);
				}
				else if(Constants.RT_UPDATE.equals(vPres.getRowStatus()))
				{
					if	(!(PresDetailList.size() == 1 && Constants.RT_LOAD.equals(PresDetailList.get(0).getRowStatus()))) //??????????????????????????????
					{
						//??????????????????????????????????????????????????????????????????
						if	(!DeletePresDetail(vPres))
						{
							throw new BusException("???????????????????????????");
						}
						Map<String,Object> storePriceMap = new HashMap<String,Object>();
						Map<String,Object> quanMap = new HashMap<String,Object>();
						List<String> pkPds = new ArrayList<String>();
						for (OpCgCnOrder co: PresDetailList)
						{ 
							pkCnOrderSet.add(co.getPkCnord());
							pkPds.add(co.getPkOrd());
							quanMap.put(co.getPkOrd(), MathUtils.mul(co.getQuanCg(), co.getPackSize()));
							// ???????????????????????????
//							List<PdOutDtParamVo> pdOutDtParamVos = pdStOutPubService.getPdStorePrice(co.getPkDeptExec(), null, co.getPkOrd(), co.getQuanCg(), null,false);
//							PdOutDtParamVo pdOutDtParamVo = pdOutDtParamVos.get(0);
//							co.setDateExpire(pdOutDtParamVo.getDateExpire());
//							co.setPriceCg(pdOutDtParamVo.getPriceStore());
//							co.setPriceCost(pdOutDtParamVo.getPriceCost());
//							co.setBatchNo(pdOutDtParamVo.getBatchNo());
//							co.setPackSize(pdOutDtParamVo.getPackSize().doubleValue());
//							co.setTs(new Date());
							OrderUpdateList.add(co);
						}
						if(OrderUpdateList.size()>0){
							storePriceMap.put("pkStore", "");
							storePriceMap.put("pkDeptExec", OrderUpdateList.get(0).getPkDeptExec());
							storePriceMap.put("quanMap", quanMap);
							storePriceMap.put("pkPd", pkPds);
							List<PdOutDtParamVo> pdOutDtParamVos = pdStOutPubService.getPdStorePrice(storePriceMap,false);
							for(OpCgCnOrder co : OrderUpdateList){
								for(PdOutDtParamVo pdOutDtParamVo:pdOutDtParamVos){
									if(co.getPkOrd().equals(pdOutDtParamVo.getPkPd())){
										co.setDateExpire(pdOutDtParamVo.getDateExpire());
										co.setPriceCg(pdOutDtParamVo.getPriceStore());
										co.setPriceCost(pdOutDtParamVo.getPriceCost());
										co.setBatchNo(pdOutDtParamVo.getBatchNo());
										co.setPackSize(pdOutDtParamVo.getPackSize().doubleValue());
										co.setTs(new Date());
										break;
									}
								}
							}
						}
					}
					dateZeroForPress(vPres);
					/*if (!ApplicationUtils.getPropertyValue("workspaceIsSy", "").equals("yes")) {// ?????????????????????????????????
						//???????????????????????????Ts
						tsOld=vPres.getTs();
						if(tsOld==null){
							throw new BusException("?????????????????????Ts???");
						}
						//??????????????????Ts
						List<CnPrescription> queryForList = DataBaseHelper.queryForList("select * from CN_PRESCRIPTION where pk_pres=? and del_flag='0'", CnPrescription.class, vPres.getPkPres());
						
						if(queryForList==null||queryForList.size()==0||queryForList.get(0).getTs()==null||!(format.format(queryForList.get(0).getTs()).equals(format.format(tsOld))))
							throw new BusException("???????????????????????????,???????????????????????????");
					}*/
					DataBaseHelper.updateBeanByPk(vPres, false);
				}
				else if(Constants.RT_REMOVE.equals(vPres.getRowStatus()))
				{	
					//??????????????????????????????????????????????????????????????????
					if	(!DeletePresDetail(vPres))
					{
						throw new BusException("???????????????????????????");
					}
					/*if (!ApplicationUtils.getPropertyValue("workspaceIsSy", "").equals("yes")) {// ?????????????????????????????????
						//???????????????????????????Ts
						tsOld=vPres.getTs();
						if(tsOld==null){
							throw new BusException("?????????????????????Ts???");
						}
						//??????????????????Ts
						List<CnPrescription> queryForList = DataBaseHelper.queryForList("select * from CN_PRESCRIPTION where pk_pres=? and del_flag='0'", CnPrescription.class, vPres.getPkPres());
						System.out.println(format.format(queryForList.get(0).getTs()));
						System.out.println(format.format(tsOld));
						
						if(queryForList==null||queryForList.size()==0||queryForList.get(0).getTs()==null||!(format.format(queryForList.get(0).getTs()).equals(format.format(tsOld))))
							throw new BusException("???????????????????????????,???????????????????????????");
					}*/
					DataBaseHelper.deleteBeanByPk(vPres);	
				}
			}
		}

		String sEffeDate = ApplicationUtils.getSysparam("CN0004", false);
		int IntEffeDate = 3;
		if (sEffeDate != null)
			IntEffeDate = Integer.parseInt(sEffeDate);
		for(OpCgCnOrder o : OrderInsertList) o = dateZeroForOpCgCnOrder(o, IntEffeDate);
		for(OpCgCnOrder o : OrderUpdateList) o = dateZeroForOpCgCnOrder(o, IntEffeDate);
		if(PresChargeList!=null&&PresChargeList.size()>0){
			for(BlOpDt o : PresChargeList) dateZeroForBlOpDt(o);
		}

		//??????order?????????????????????bd_ord_dt
		if (OrderInsertList.size() > 0) {
			// ??????????????????
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), OrderInsertList);
			drugOpCg(OrderInsertList, pvInfo);
		}
		if (OrderUpdateList.size() > 0) {
			// ????????????????????????
			//			String sql = "delete from bl_op_dt op where op.pk_cnord in (" + BlcgUtil.convertSetToSqlInPart(pkCnOrderSet, "pk_cnord") + ")";
			//			DataBaseHelper.execute(sql, new Object[] {});

			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), OrderUpdateList);
			drugOpCg(OrderUpdateList, pvInfo);
		}
		if(PresChargeList!=null&&PresChargeList.size()>0){
			for (BlOpDt item: PresChargeList)
			{
				ApplicationUtils.setDefaultValue(item, true);
			}
		}
		if	(PresChargeList!=null&&PresChargeList.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), PresChargeList);
		
		return "1";
	}
	
	
}
