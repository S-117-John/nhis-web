package com.zebone.nhis.cn.opdw.service;

import ca.uhn.hl7v2.util.StringUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.opdw.dao.SyxCnOpHerbMapper;
import com.zebone.nhis.cn.opdw.vo.*;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.pi.PatInfo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SyxCnOpHerbService {
	private Logger logger = LoggerFactory.getLogger("nhis.cnOrder");
	private static final String HERB_NAME = "????????????";
	private static final String HERB_CODE = "DEF99999";
	private static SimpleDateFormat dateformat =  new SimpleDateFormat("yyyyMMddHHmmss");

	@Autowired
	public SyxCnOpHerbMapper herbDao;

	@Autowired
	private BdSnService bdSnService;

	@Autowired
	private OpCgPubService opCgPubService;
	@Autowired
	private CnPubService cnPubService;
	@Autowired
	public SyxCnOpPatiPvService syxCnOpPatiPvService;

	public List<SyxCnOpPresVo> qryHerbOrders(String param,IUser user){
		//pkPv
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkPv = paramMap.get("pkPv")!=null?paramMap.get("pkPv").toString():"";
		if(StringUtils.isBlank(pkPv)) throw new BusException("??????pkPv?????????");
		List<SyxCnOpPresVo> herbOrderlist = null;
		if (Application.isSqlServer()) {
			herbDao.qryHerbOrdersSqlServer(paramMap);
		}else{
			herbDao.qryHerbOrders(paramMap);
		}

		return herbOrderlist;
	}

	public void saveHerbOrders(String param,IUser user) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException{
		SyxOpHerbPresVo herbPres = JsonUtil.readValue(param, SyxOpHerbPresVo.class);
		List<SyxCnOpPresVo> addingList = herbPres.getAddingList();
		List<SyxCnOpPresVo> delingList = herbPres.getDeletingList();
		List<SyxCnOpPresVo> editingList = herbPres.getEditingList();
		List<CnPrescription> addHerbPres = new ArrayList<CnPrescription>();
		List<CnPrescription> editHerbPres = new ArrayList<CnPrescription>();
		List<CnOrder> addCnOrd = new ArrayList<CnOrder>();
		List<CnOrder> editCnOrd = new ArrayList<CnOrder>();
		List<CnOrdHerb> addCnOrdHerb = new ArrayList<CnOrdHerb>();
		List<String> delPresByPkPres = new ArrayList<String>();
		List<String> delHerbByPkCn = new ArrayList<String>();
		List<BlPubParamVo> blOpItem = new ArrayList<BlPubParamVo>();
		List<String> delBlItemsByCnord = new ArrayList<String>();
		List<CnOrder> euBoilList = new ArrayList<CnOrder>();
		Date d = new Date();
		User u = (User)user;
		if(addingList!=null&&addingList.size()>0){
			for(SyxCnOpPresVo pres :addingList){
				if(StringUtils.isNotBlank(pres.getPkPres())) continue;
				String pkPres = NHISUUID.getKeyId();
				pres.setPresNo(ApplicationUtils.getCode("0407"));
				pres.setDatePres(d);
				pres.setPkPres(pkPres);
				pres.setDelFlag("0");
				pres.setTs(d);
				pres.setPkOrg(u.getPkOrg());
				CnOrder cnOrder = new CnOrder();
				BeanUtils.copyProperties(cnOrder, pres);
				cnOrder.setPkCnord(NHISUUID.getKeyId());
				cnOrder.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, u));
				cnOrder.setOrdsnParent(cnOrder.getOrdsn());
				cnOrder.setEuAlways("1");
				cnOrder.setDateStart(d);
				cnOrder.setDateEnter(d);
				cnOrder.setPkEmpInput(cnOrder.getPkEmpOrd());
				cnOrder.setNameEmpInput(cnOrder.getNameEmpOrd());
				cnOrder.setPkPres(pkPres);
				cnOrder.setDelFlag("0");
				cnOrder.setTs(d);
				cnOrder.setPkOrg(u.getPkOrg());

				List<SyxOpHerbVo>  herbOrders = pres.getHerbOrders();
				if(herbOrders!=null && herbOrders.size()>0){
					for(SyxOpHerbVo herb :herbOrders){
						if(StringUtils.isNotBlank(herb.getPkOrdherb())) continue;
						herb.setPkOrdherb(NHISUUID.getKeyId());
						herb.setPkCnord(cnOrder.getPkCnord());
						herb.setDelFlag("0");
						herb.setTs(d);
						opOrdToOpCg(cnOrder,herb,blOpItem);
						addCnOrdHerb.add(herb);
					}
				}
				addHerbPres.add(pres);
				addCnOrd.add(cnOrder);
				//??????????????????
				if("1".equals(herbPres.getDjf())){
					if("4".equals(pres.getEuBoil()) ||"6".equals(pres.getEuBoil())){
						euBoilList.add(cnOrder);
					}
				}
				else{
					if("1".equals(pres.getEuBoil()) ||"2".equals(pres.getEuBoil())||"3".equals(pres.getEuBoil())){
						euBoilList.add(cnOrder);
					}
				}
			}
		}

		if(delingList!=null && delingList.size()>0){
			for(SyxCnOpPresVo delPres : delingList){
				delPresByPkPres.add(delPres.getPkPres());
			}
		}
		if(editingList!=null && editingList.size()>0){
			for(SyxCnOpPresVo cnPres : editingList){
				//delPresByPkPres.add(cnPres.getPkPres());
				//CnPrescription cnPres = new CnPrescription();
				//BeanUtils.copyProperties(cnPres, editPres);
				//cnPres.setDatePres(d);
				//cnPres.setPkPres(NHISUUID.getKeyId());
				//cnPres.setDelFlag("0");
				cnPres.setTs(d);
				//cnPres.setPkOrg(u.getPkOrg());
				//cnPres.setPresNo(StringUtils.isNotBlank(cnPres.getPresNo())?cnPres.getPresNo():ApplicationUtils.getCode("0407"));
				CnOrder cnOrder = new CnOrder();
				BeanUtils.copyProperties(cnOrder, cnPres);
				cnOrder.setPkCnord(cnPres.getPkCnord());
				//cnOrder.setPkPv(cnPres.getPkPv());
				//cnOrder.setPkPi(cnPres.getPkPi());
				//cnOrder.setPkPres(cnPres.getPkPres());
				cnOrder.setCodeSupply(cnPres.getCodeSupply());
				cnOrder.setNameOrd(cnPres.getNameOrd());
				cnOrder.setNoteOrd(cnPres.getNote());
				cnOrder.setOrds(cnPres.getOrds());
				//cnOrder.setPkPres(cnPres.getPkPres());
				//cnOrder.setOrdsn(cnOrder.getOrdsn()!=0?cnOrder.getOrdsn():bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, u));
				//cnOrder.setOrdsnParent(cnOrder.getOrdsn());
				//cnOrder.setEuAlways("1");
				//cnOrder.setDateStart(d);
				//cnOrder.setDateEnter(d);
				//cnOrder.setPkEmpInput(cnOrder.getPkEmpOrd());
				//cnOrder.setNameEmpInput(cnOrder.getNameEmpEntry());
				//cnOrder.setDelFlag("0");
				cnOrder.setTs(d);
				//cnOrder.setPkOrg(u.getPkOrg());
				delHerbByPkCn.add(cnOrder.getPkCnord());
				delBlItemsByCnord.add(cnOrder.getPkCnord());
				List<SyxOpHerbVo>  herbOrders = cnPres.getHerbOrders();
				if(herbOrders!=null && herbOrders.size()>0){
					for(SyxOpHerbVo herb :herbOrders){
					//	BeanUtils.copyProperties(herb, herbVo);
						herb.setPkOrdherb(NHISUUID.getKeyId());
						herb.setPkCnord(cnOrder.getPkCnord());
						herb.setDelFlag("0");
						herb.setTs(d);
						//DataBaseHelper.insertBean(herb);
						//????????????
						opOrdToOpCg(cnOrder,herb,blOpItem);
						addCnOrdHerb.add(herb);
					}
				}
				//DataBaseHelper.insertBean(cnOrder);
				//DataBaseHelper.insertBean(cnPres);

				editHerbPres.add(cnPres);
				editCnOrd.add(cnOrder);
				//??????????????????
				if("1".equals(herbPres.getDjf())){
					if("4".equals(cnPres.getEuBoil()) ||"5".equals(cnPres.getEuBoil())){
						euBoilList.add(cnOrder);
					}
				}else{
					if("1".equals(cnPres.getEuBoil()) ||"2".equals(cnPres.getEuBoil())||"3".equals(cnPres.getEuBoil())){
						euBoilList.add(cnOrder);
					}
				}
			}
		}
		//?????????????????????????????????????????????
		euBoilItemToOpCg(euBoilList,blOpItem,herbPres.getDjf());

		if(delPresByPkPres.size()>0){
			Map<String,Object> presMap = new HashMap<String,Object>();
			presMap.put("pkPres", delPresByPkPres);
			DataBaseHelper.update("delete from bl_op_dt where pk_pres in(:pkPres) and flag_settle='0' ", presMap);
			DataBaseHelper.update("delete from cn_ord_herb herb where exists (select * from cn_order  ord where herb.pk_cnord = ord.pk_cnord and ord.pk_pres in (:pkPres) )", presMap);
			logger.error("SyxCnOpHerbService.saveHerbOrders???????????????????????????{}",presMap);
			DataBaseHelper.update("delete from cn_order where pk_pres in(:pkPres)", presMap);
			DataBaseHelper.update("delete from cn_prescription where pk_pres in(:pkPres)", presMap);

		}
		if(delHerbByPkCn.size()>0){
			Map<String,Object> cnMap = new HashMap<String,Object>();
			cnMap.put("pkCnord", delHerbByPkCn);
			DataBaseHelper.update("delete from cn_ord_herb where pk_cnord in(:pkCnord)", cnMap);
		}
		if(addHerbPres.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnPrescription.class), addHerbPres);
		}
		if(addCnOrd.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), addCnOrd);
		}
		if(addCnOrdHerb.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrdHerb.class), addCnOrdHerb);
		}
		if(editHerbPres.size()>0) {
			DataBaseHelper.batchUpdate("update cn_prescription set ts=:ts, eu_boil=:euBoil,dt_boiltype=:dtBoiltype,usage_count=:usageCount,note=:note where pk_pres=:pkPres", editHerbPres);
		}
		if(editCnOrd.size()>0){
			DataBaseHelper.batchUpdate("update cn_order set ts=:ts,name_ord=:nameOrd, code_supply=:codeSupply,note_ord=:noteOrd,ords=:ords,eu_injury=:euInjury  where pk_cnord=:pkCnord", editCnOrd);
		}
		 if(delBlItemsByCnord.size()>0){
				Map<String,Object> delM = new HashMap<String,Object>();
				delM.put("pkCnord", delBlItemsByCnord);
				DataBaseHelper.update("delete from bl_op_dt where pk_cnord in (:pkCnord) and flag_settle='0'", delM);
		 }
		 if(blOpItem.size()>0){
			 opCgPubService.blOpCgBatch(blOpItem);
		 }
	}

	/**
	 * ??????????????????
	 * @param opOrd
	 * @param herb
	 * @param blOpItem
	 */
	private void opOrdToOpCg(CnOrder opOrd,CnOrdHerb herb,List<BlPubParamVo> blOpItem) {
		User u = UserContext.getUser();
		String pkOrg = u.getPkOrg();
		String pkDept = u.getPkDept();
		BlPubParamVo bpb = new BlPubParamVo();
		bpb.setPkOrg(pkOrg);
		bpb.setEuPvType(opOrd.getEuPvtype());
		bpb.setPkPv(opOrd.getPkPv());
		bpb.setPkPi(opOrd.getPkPi());
		bpb.setPkCnord(opOrd.getPkCnord());
		bpb.setPkPres(opOrd.getPkPres());
		bpb.setPkOrgEx(opOrd.getPkOrgExec());
		bpb.setPkOrgApp(opOrd.getPkOrg());
		bpb.setPkDeptEx(opOrd.getPkDeptExec());
		bpb.setPkDeptApp(opOrd.getPkDept());
		bpb.setPkEmpApp(opOrd.getPkEmpOrd());
		bpb.setNameEmpApp(opOrd.getNameEmpOrd());
		bpb.setFlagPd("1");
		bpb.setNamePd(opOrd.getNameOrd());
		bpb.setFlagPv("0");
		//bpb.setDateExpire(opOrd.getDateExpire());
		bpb.setPkOrd(herb.getPkPd());
		bpb.setPkItem(herb.getPkPd());
		bpb.setQuanCg(herb.getQuan()*opOrd.getOrds());
		bpb.setPkUnitPd(herb.getPkUnit());
		bpb.setPackSize(1);
		bpb.setPrice(herb.getPrice());
		//bpb.setPriceCost(opOrd.getPriceCost());
		bpb.setDateHap(new Date());
		bpb.setPkDeptCg(pkDept);
		bpb.setPkEmpCg(u.getPkEmp());
		bpb.setNameEmpCg(u.getNameEmp());
		bpb.setEuAdditem("0"); //????????????
		bpb.setPkDeptJob(opOrd.getPkDeptJob());
		bpb.setPkDeptAreaapp(opOrd.getPkDeptAreaapp());
		blOpItem.add(bpb);
	}

	/**
	 * ??????????????????
	 * @param opOrds
	 * @param blOpItem
	 */
	private void euBoilItemToOpCg(List<CnOrder> opOrds,List<BlPubParamVo> blOpItem,String costRyDjf) {
		if	(opOrds==null || opOrds.size()<=0)  return;
		User u = UserContext.getUser();
		String pkOrg = u.getPkOrg();
		String pkDept = u.getPkDept();
		String pkItemByEuBoil = ApplicationUtils.getSysparam("BL0032", false);//?????????????????????????????????
		if (StringUtil.isBlank(pkItemByEuBoil)) {
			throw new BusException("????????????????????????????????????????????????BL0032???");
		}
		BdItem bdItem= DataBaseHelper.queryForBean("select pk_item from bd_item where code=? ", BdItem.class, new Object[]{pkItemByEuBoil});
		if (bdItem == null) {
			throw new BusException("???????????????BL0032??????????????????????????????????????????????????????????????????!");
		}
		for(CnOrder opOrd : opOrds){
			BlPubParamVo bpb = new BlPubParamVo();
			bpb.setPkOrg(pkOrg);
			bpb.setEuPvType(opOrd.getEuPvtype());
			bpb.setPkPv(opOrd.getPkPv());
			bpb.setPkPi(opOrd.getPkPi());
			bpb.setPkCnord(opOrd.getPkCnord());
			bpb.setPkPres(opOrd.getPkPres());
			bpb.setPkOrgEx(opOrd.getPkOrgExec());
			bpb.setPkOrgApp(opOrd.getPkOrg());
			bpb.setPkDeptEx(opOrd.getPkDeptExec());
			bpb.setPkDeptApp(opOrd.getPkDept());
			bpb.setPkEmpApp(opOrd.getPkEmpOrd());
			bpb.setNameEmpApp(opOrd.getNameEmpOrd());
			bpb.setFlagPd("0");
			bpb.setNamePd(opOrd.getNameOrd());
			bpb.setFlagPv("0");
			//bpb.setDateExpire(opOrd.getDateExpire());
			//bpb.setPkOrd(herb.getPkPd());
			//bpb.setPkUnitPd(herb.getPkUnit());
			//bpb.setPackSize(1);
			//bpb.setPrice(herb.getPrice());
			bpb.setPkItem(bdItem.getPkItem());
			if("1".equals(costRyDjf)){
				bpb.setQuanCg(Double.parseDouble(opOrd.getFriedNum().toString()));
			}else {
				bpb.setQuanCg(1.0);
			}
			//bpb.setPriceCost(opOrd.getPriceCost());
			bpb.setDateHap(new Date());
			bpb.setPkDeptCg(pkDept);
			bpb.setPkEmpCg(u.getPkEmp());
			bpb.setNameEmpCg(u.getNameEmp());
			bpb.setEuAdditem("0"); //????????????
			bpb.setPkDeptJob(opOrd.getPkDeptJob());
			bpb.setPkDeptAreaapp(opOrd.getPkDeptAreaapp());
			blOpItem.add(bpb);
		}
	}
	/**
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PiPresInfo> getCopyPres(String param,IUser user ){
		PiPresInfo para = JsonUtil.readValue(param, new TypeReference<PiPresInfo>(){});
		if(Application.isSqlServer()){
			return para!=null?herbDao.getCopyPres(para):null;
		}else {
			return para!=null?herbDao.getCopyPresOracle(para):null;
		}

	}

	//????????????????????????????????????
	public List<Map<String,Object>> queryPiHistoryHerbPres(String param,IUser user ){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = herbDao.queryPiHistoryHerbPres(paramMap);
		return list;
	}

	/**
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PiPresInfoDt> getCopyPresDt(String param,IUser user ){
		Map<String,Object> para = JsonUtil.readValue(param, Map.class);
		String pkPres = para.get("pkPres") == null?null:(String) para.get("pkPres");
		String pkDept = para.get("pkDept") == null?null:(String) para.get("pkDept");
		String pkHp = para.get("pkHp") == null?null:(String) para.get("pkHp");
		List<PiPresInfoDt> res = null;
		if(!CommonUtils.isEmptyString(pkDept) && !CommonUtils.isEmptyString(pkPres)){
			if (Application.isSqlServer()) {
				res = herbDao.getCopyPresDtSqlServer(pkPres,pkDept);
			}else {
				res = herbDao.getCopyPresDt(pkPres,pkDept,pkHp);
			}

		}
		return res;
	}


	//??????????????????
	public void saveHerbOrderInfo(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		SyxOpHerbInfo herbOrder = JsonUtil.readValue(param, new TypeReference<SyxOpHerbInfo>(){});
		User u = (User)user;
		PatInfo patInfo=herbOrder.getPatInfo();
		CnOrder order = herbOrder.getOrder();
		CnPrescription presVo = null;
		Date dt = new Date();
		List<BlPubParamVo> blOpCgList = new ArrayList<BlPubParamVo>();

		//??????????????????????????????????????????????????????
		String euLocked = syxCnOpPatiPvService.getPvEuLocked(patInfo.getPkPv());
		String pv0057 = ApplicationUtils.getSysparam("PV0057", false);
		if(!"0".equals(pv0057)){
			pv0057 = "1";
		}
		if("2".equals(euLocked) && "1".equals(pv0057)) throw new BusException("???????????????????????????????????????????????????????????????????????????");

		//??????????????????????????????
		BdOuEmpjob emp = DataBaseHelper.queryForBean(
				"SELECT * FROM bd_ou_empjob WHERE pk_emp =? AND is_main = '1' ", BdOuEmpjob.class, new Object[]{u.getPkEmp()});
		String pkDeptJob= emp!=null && StringUtils.isNotBlank(emp.getPkDept())?emp.getPkDept():null;
		
		//??????????????????????????????
		String pkDeptArea = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept_area From pv_encounter where pk_pv=?",patInfo.getPkPv()), "pkDeptArea");
		
		//?????????????????????
		if(herbOrder.getOrder().getTs()!=null){
			List<String> pkCnordTsList = new ArrayList<String>();
			pkCnordTsList.add(herbOrder.getOrder().getPkCnord()+","+dateformat.format(herbOrder.getOrder().getTs()));
			if(pkCnordTsList==null||pkCnordTsList.size()<=0) return;
			cnPubService.splitPkTsValidOrd(pkCnordTsList);
		}

		order.setPkDeptJob(pkDeptJob);//????????????
		order.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
		//??????????????????cn_order???CN_PRESCRIPTION
		if( StringUtils.isBlank(order.getPkCnord())){
			presVo = new CnPrescription();
			presVo.setPkPres(NHISUUID.getKeyId());
			presVo.setPkOrg(u.getPkOrg());
			presVo.setPkPv(order.getPkPv());
			presVo.setPkPi(order.getPkPi());
			if(StringUtils.isEmpty(order.getDtPrestype())){
				presVo.setDtPrestype("02");//02 ????????????
			}else{
				presVo.setDtPrestype(order.getDtPrestype());//02 ????????????
			}

			presVo.setPresNo(order.getPresNo());
			presVo.setDatePres(dt);
			presVo.setPkDept(u.getPkDept());
			presVo.setPkDeptNs(order.getPkDeptNs());
			presVo.setPkEmpOrd(u.getPkEmp());
			presVo.setNameEmpOrd(u.getNameEmp());
			presVo.setFlagPrt(Constants.FALSE);
			presVo.setEuBoil(order.getEuBoil());
			presVo.setFriedNum(order.getFriedNum());
			presVo.setUsageCount(order.getUsageCount());
			presVo.setDtProperties(order.getDtProperties());
			presVo.setDosagePack(order.getDosagePack());
			presVo.setDtBoiltype(order.getDtBoiltype());
			presVo.setDtHpprop(order.getDtHpprop());
			presVo.setTs(dt);
			presVo.setPkDiag(herbOrder.getPkDiag());
			presVo.setPkSymp(herbOrder.getPkSymp());
			presVo.setCodeDiag(herbOrder.getCodeDiag());
			presVo.setCodeSymp(herbOrder.getCodeSymp());
			presVo.setNameDiag(!CommonUtils.isEmptyString(herbOrder.getNameDiag())?herbOrder.getNameDiag():order.getNameDiag());
			presVo.setNameSymp(herbOrder.getNameSymp());
			presVo.setNote(herbOrder.getPresNote());


			DataBaseHelper.insertBean(presVo);

			order.setPkCnord(NHISUUID.getKeyId());
			order.setCodeApply(null);
			order.setPkPres(presVo.getPkPres());
			order.setCodeOrdtype("0103");	//???????????????bd_ordtype
			order.setEuAlways("1");	        //??????
			order.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));
			order.setDescOrd(order.getNameOrd());
			order.setTs(dt);
			order.setModityTime(null);
			if (StringUtils.isBlank(order.getCodeFreq())) {
				order.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
			}
			order.setQuan(1.0);
			order.setQuanCg(1.0);
			order.setEuStatusOrd("0");		//??????
			order.setDateEnter(dt);			//????????????????????????db??????????????????
			order.setDateStart(dt);
			order.setPkDept(u.getPkDept());
			order.setPkDeptNs(order.getPkDeptNs());
			order.setPkEmpInput(u.getPkEmp());
			order.setNameEmpInput(u.getUserName());
			order.setDateSign(dt);
			order.setPkEmpOrd(u.getPkEmp());
			order.setNameEmpOrd(u.getUserName());
			order.setEuIntern(order.getEuIntern());
			order.setEuPvtype(patInfo.getEuPvtype());
			order.setOrdsnParent(order.getOrdsn());
			order.setNameOrd(order.getNameOrd()==null?HERB_NAME:order.getNameOrd());
			order.setDescOrd(order.getNameOrd()==null?HERB_NAME:order.getNameOrd());
			order.setCodeOrd(HERB_CODE);
			order.setFlagDoctor("1");
			order.setFlagDurg("1");
			order.setFlagSign("1");
			order.setPkEmpChk(null);
			order.setNameEmpChk(null);
			order.setDateChk(null);
			order.setDateLastEx(null);
			order.setDateErase(null);
			order.setPkEmpErase(null);
			order.setNameEmpErase(null);
			order.setFlagErase("0");
			order.setFlagEraseChk("0");
			order.setDosage(1.00);
			order.setDateEffe(getDateEffec(order.getEuPvtype()));// ????????????
			DataBaseHelper.insertBean(order);

			if(order.getCnSignCa()!=null){
				List<CnOrder> addCnOrdList = new ArrayList<>();
				addCnOrdList.add(order);
				//??????CA????????????
				cnPubService.caRecordByOrd(false, addCnOrdList);
			}
		}else {
			order.setTs(dt);
			order.setModityTime(dt);
			order.setDateEnter(dt);
			order.setPkEmpOrd(u.getPkEmp());
			order.setNameEmpOrd(u.getUserName());
			DataBaseHelper.updateBeanByPk(order,false);

			presVo = DataBaseHelper.queryForBean("select * from cn_prescription where pk_pres=?", CnPrescription.class, new Object[]{order.getPkPres()});
			presVo.setEuBoil(order.getEuBoil());
			presVo.setDtBoiltype(order.getDtBoiltype());
			presVo.setFriedNum(order.getFriedNum());
			presVo.setUsageCount(order.getUsageCount());
			presVo.setDtProperties(order.getDtProperties());
			presVo.setDosagePack(order.getDosagePack());
			presVo.setDtHpprop(order.getDtHpprop());
			presVo.setTs(dt);
			presVo.setPkDiag(herbOrder.getPkDiag());
			presVo.setPkSymp(herbOrder.getPkSymp());
			presVo.setCodeDiag(herbOrder.getCodeDiag());
			presVo.setCodeSymp(herbOrder.getCodeSymp());
			presVo.setNameDiag(!CommonUtils.isEmptyString(herbOrder.getNameDiag())?herbOrder.getNameDiag():order.getNameDiag());
			presVo.setNameSymp(herbOrder.getNameSymp());
			presVo.setNote(herbOrder.getPresNote());
			if(StringUtils.isEmpty(order.getDtPrestype())){
				presVo.setDtPrestype("02");//02 ????????????
			}else{
				presVo.setDtPrestype(order.getDtPrestype());//02 ????????????
			}
			DataBaseHelper.updateBeanByPk(presVo, false);
		}

		//?????????????????????cn_ord_herb
		//???????????????
		List<CnOrdHerb> addHerbItemList = new ArrayList<CnOrdHerb>();
		List<CnOrdHerb> updateHerbItemList  = new ArrayList<CnOrdHerb>();
		if(herbOrder.getHerbs() != null)
		{
			for(CnOrdHerb herbVo: herbOrder.getHerbs() ){
				if(StringUtils.isBlank(herbVo.getPkOrdherb())){
					String pkOrdherb = NHISUUID.getKeyId();
					herbVo.setPkOrdherb(pkOrdherb);
					herbVo.setPkCnord(order.getPkCnord());
					herbVo.setDelFlag("0");
					herbVo.setTs(dt);
					herbVo.setCreateTime(dt);
					herbVo.setCreator(u.getPkEmp());
					addHerbItemList.add(herbVo);
				}else{
					herbVo.setTs(dt);
					updateHerbItemList.add(herbVo);
				}

				//????????????????????????
				opOrdToOpCg(order,herbVo,blOpCgList);
			}
		}

		//?????????????????????
		//??????????????????
		if("1".equals(herbOrder.getDjf())){
			if("4".equals(order.getEuBoil()) ||"5".equals(order.getEuBoil())){
				List<CnOrder> ordList=new ArrayList<CnOrder>();
				ordList.add(order);
				euBoilItemToOpCg(ordList,blOpCgList,herbOrder.getDjf());
			}
		}else{
			if("1".equals(order.getEuBoil()) ||"2".equals(order.getEuBoil())||"3".equals(order.getEuBoil())){
				List<CnOrder> ordList=new ArrayList<CnOrder>();
				ordList.add(order);
				euBoilItemToOpCg(ordList,blOpCgList,herbOrder.getDjf());
			}
		}


		if (addHerbItemList != null && addHerbItemList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrdHerb.class), addHerbItemList);
		}
		if (updateHerbItemList != null && updateHerbItemList.size() > 0) {
			for(CnOrdHerb herbVo: updateHerbItemList ){
				DataBaseHelper.updateBeanByPk(herbVo, false);
			}
		}

		//????????????
		List<CnOrdHerb> delHerbItemList= herbOrder.getDelHerbs();
		if (delHerbItemList != null && delHerbItemList.size() > 0) {
			DataBaseHelper.batchUpdate("delete from   cn_ord_herb where pk_ordherb = :pkOrdherb ", delHerbItemList);
		}

		//??????????????????--????????????
		int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_pv='"+patInfo.getPkPv()+"' and pk_cnord ='"+order.getPkCnord()+"'  and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{});
		if(hadSettleCgCount>0){
			throw new BusException("?????????????????????????????????????????????????????????????????????????????? ");
		}
		DataBaseHelper.update("delete from bl_op_dt where pk_pv='"+patInfo.getPkPv()+"' and pk_cnord = :pkCnord ", order);
		if(blOpCgList.size()>0){
			 opCgPubService.blOpCgBatch(blOpCgList);
		 }

		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put("OMPtype", "prescription");
		mapParam.put("addHerbItemList", addHerbItemList);
		mapParam.put("updateHerbItemList", updateHerbItemList);
		PlatFormSendUtils.sendOpO09Msg(mapParam);

	}

	//??????????????????
	public void deleteHerbOrder(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
	    String pkCnordTs=paramMap.get("pkCnordTs");
	    String pkPv = paramMap.get("pkPv");

	    //??????????????????????????????????????????????????????
	    String euLocked = syxCnOpPatiPvService.getPvEuLocked(pkPv);
	    if("2".equals(euLocked)) throw new BusException("???????????????????????????????????????????????????????????????????????????");
	    String pkCnord = cnPubService.splitPkTsValidOrd(pkCnordTs);

	    //??????????????????????????????
		String processClass = ApplicationUtils.getPropertyValue("msg.processClass", "");
		List<Map<String,Object>> delMapList = null ;
		if("SDPlatFormSendService".equals(processClass)){
		 //????????????
			   String sql="SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always, "
					    +" dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer, "
					    +" co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input FROM cn_order co "
					    +" LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where 1=1 "
					    + "and co.pk_cnord  = '" +pkCnord +"'" ;
			    delMapList=DataBaseHelper.queryForList(sql);
		}

		//????????????
		int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord ='"+pkCnord+"'  and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{});
		if(hadSettleCgCount>0){
			throw new BusException("??????????????????????????????????????????????????????????????????????????????");
		}
		DataBaseHelper.execute("delete from bl_op_dt where pk_cnord =?", new Object[]{pkCnord});


		DataBaseHelper.execute("delete from cn_prescription where pk_pres = (select pk_pres from cn_order where PK_CNORD = ? ) ", new Object[]{pkCnord});
	    DataBaseHelper.execute("delete from cn_ord_herb where PK_CNORD = (select PK_CNORD from cn_order where PK_CNORD = ? )", new Object[]{pkCnord});
		logger.error("SyxCnOpHerbService.deleteHerbOrder???????????????????????????{}",pkCnord);
	    DataBaseHelper.execute("delete from cn_order where eu_status_ord = '0' and ORDSN_PARENT IN (SELECT ORDSN_PARENT FROM CN_ORDER WHERE PK_CNORD = ? )", new Object[]{pkCnord}); //????????????

  		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put("OMPtype", "prescription");
		mapParam.put("delMapList", delMapList);
		PlatFormSendUtils.sendOpO09Msg(mapParam);
	}

	//????????????????????????
	public List<Map<String,Object>> queryOpHerbPresList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkPv = paramMap.get("pkPv")!=null?paramMap.get("pkPv").toString():"";
		if(StringUtils.isBlank(pkPv)) throw new BusException("??????pkPv?????????");
		List<Map<String,Object>> herbOrderlist = herbDao.queryOpHerbPresList(paramMap);
		return herbOrderlist;
	}

	private Date getDateEffec(String euPvType) {

		String val = ApplicationUtils.getSysparam("CN0004", false);
		if (org.apache.commons.lang3.StringUtils.isEmpty(val)) {
			if (EnumerateParameter.TWO.equals(euPvType))// ??????
			{
				val = EnumerateParameter.TWO;
			} else {// ??????
				val = EnumerateParameter.THREE;
			}

		}
		Date dateEffec = DateUtils.getSpecifiedDay(new Date(), Integer.parseInt(val));
		return dateEffec;
	}

	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 */
	public void copyHerbOrders(String param, IUser user) throws Exception {
		Map<String,Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>() {});
		Object objPat = MapUtils.getObject(paramMap,"patInfo");
		List<String> pkCnorderList = (List<String>) MapUtils.getObject(paramMap,"pkCnorderList");
		String euInjury = MapUtils.getString(paramMap,"euInjury");
		if(objPat == null){
			throw new BusException("?????????????????????");
		}
		if(pkCnorderList == null){
			throw new BusException("?????????????????????");
		}
		PatInfo patInfo = new PatInfo();
		BeanUtils.copyProperties(patInfo, objPat);
		if(CollectionUtils.isNotEmpty(pkCnorderList)){
			Set<String> set = new HashSet<String>(pkCnorderList);
			String str = CommonUtils.convertSetToSqlInPart(set, "pk_cnord");
			List<CnOrder> orderList = DataBaseHelper.queryForList("select od.*,pres.name_diag,pres.USAGE_COUNT,pres.DT_BOILTYPE,pres.FRIED_NUM,pres.DT_PROPERTIES,pres.DOSAGE_PACK,pres.EU_BOIL,pres.name_symp,pres.code_diag,pres.pk_diag,pres.code_symp,pres.pk_symp from CN_ORDER od inner join CN_PRESCRIPTION pres on od.pk_pres = pres.pk_pres  where od.PK_CNORD in(" + str + ")", CnOrder.class);
			//List<CnPrescription> ordPresList=DataBaseHelper.queryForList("select pres.* from CN_ORDER od inner join CN_PRESCRIPTION pres on od.pk_pres = pres.pk_pres  where od.PK_CNORD in(" + str + ")", CnPrescription.class);
			List<CnOrdHerb> ordHerbList = DataBaseHelper.queryForList("select * from CN_ORD_HERB where PK_CNORD in(" + str + ")", CnOrdHerb.class);
			if(CollectionUtils.isEmpty(orderList) || CollectionUtils.isEmpty(ordHerbList)){
				throw new BusException("????????????????????????????????????????????????");
			}
			Map<String, Collection<CnOrdHerb>> herbMap = Multimaps.index(ordHerbList, new Function<CnOrdHerb, String>() {
				@Override
				public String apply(CnOrdHerb cnOrdHerb) {
					cnOrdHerb.setPkOrdherb(null);
					return cnOrdHerb.getPkCnord();
				}
			}).asMap();

			for(CnOrder cnOrder:orderList){
				SyxOpHerbInfo herbOrder = new SyxOpHerbInfo();
				herbOrder.setPatInfo(patInfo);
				herbOrder.setHerbs(Lists.newArrayList(herbMap.get(cnOrder.getPkCnord())));
				if(herbOrder.getHerbs().size() == 0){
					continue;
				}
				cnOrder.setPkPv(patInfo.getPkPv());
				cnOrder.setPkCnord(null);
				cnOrder.setTs(null);
				cnOrder.setPkDeptNs(patInfo.getPkDeptNs());
				cnOrder.setPresNo(ApplicationUtils.getCode("0407"));
				//cnOrder.setPkDiag(null);
				//cnOrder.setNameDiag(null);
				if(euInjury !=null && euInjury.length() > 0){
					cnOrder.setEuInjury(euInjury);
				}
				herbOrder.setOrder(cnOrder);
				herbOrder.setPkDiag(cnOrder.getPkDiag());
				herbOrder.setCodeDiag(cnOrder.getCodeDiag());
				herbOrder.setNameDiag(cnOrder.getNameDiag());
				herbOrder.setPkSymp(cnOrder.getPkSymp());
				herbOrder.setCodeSymp(cnOrder.getCodeSymp());
				herbOrder.setNameSymp(cnOrder.getNameSymp());

//				if(ordPresList!=null && ordPresList.size()>0){
//					for(CnPrescription preOrd:ordPresList){
//						if(preOrd.getPkPres().equals(cnOrder.getPkPres())){
//							herbOrder.setPkDiag(preOrd.getPkDiag());
//							herbOrder.setCodeDiag(preOrd.getCodeDiag());
//							herbOrder.setNameDiag(preOrd.getNameDiag());
//							herbOrder.setPkSymp(preOrd.getPkSymp());
//							herbOrder.setCodeSymp(preOrd.getCodeSymp());
//							herbOrder.setNameSymp(preOrd.getNameSymp());
//							break;
//						}
//					}
//				}
				saveHerbOrderInfo(JsonUtil.writeValueAsString(herbOrder),user);
			}
		}
	}
}
