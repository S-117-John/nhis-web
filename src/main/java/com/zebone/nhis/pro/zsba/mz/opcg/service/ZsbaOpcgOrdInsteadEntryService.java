package com.zebone.nhis.pro.zsba.mz.opcg.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.vo.BlOpDrugStorePriceInfo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.base.bd.mk.BdSupplyItem;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.support.OrderFreqCalCountHandler;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsba.mz.opcg.vo.OpCgCnOrder;
import com.zebone.nhis.pro.zsba.mz.opcg.vo.OpOrdInsteadEntryDto;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊收费，医嘱代录服务类
 * @author gongxy
 */
@Service
public class ZsbaOpcgOrdInsteadEntryService {

	@Autowired private OpCgPubService opCgPubService;
	@Autowired private OpcgPubHelperService opcgPubHelperService;
	@Autowired private PriceStrategyService priceStrategyService;
	
	/**
	 * 根据患者主键查询患者的就诊记录
	 * 007002003004->022003027067
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPatiPvRecord(String param, IUser user) {

		Map<String, Object> mapParam = JsonUtil.readValue(param, HashMap.class);
		mapParam.put("pkOrg", ((User) user).getPkOrg());

		
		//从系统中获取门诊挂号有效期天数
		String valsql ="SELECT VAL FROM bd_sysparam where del_flag = '0'  AND PK_ORG=? and code='PV0003' ";
		Map<String,Object> map = DataBaseHelper.queryForMap(valsql, new Object[] {((User) user).getPkOrg()});
		String val = CommonUtils.getString(map.get("val"),"0");
		val = val.toUpperCase().startsWith("H")?val.substring(1):String.valueOf(Integer.valueOf(val)*24);
		String dateDiffFun = Application.isSqlServer()?"DATEDIFF(hh,getdate(),nvl(pv.date_reg,pv.date_begin))":"ceil((SYSDATE - nvl(pv.date_reg,pv.date_begin)) * 24)";		
		StringBuffer sql = new StringBuffer();
		sql.append("select pv.pk_pv,pv.eu_pvtype,pv.pk_dept,pv.name_emp_tre,dept.name_dept,pv.date_reg ");
		sql.append(" from pv_encounter pv inner join bd_ou_dept dept on dept.pk_dept=pv.pk_dept ");
		sql.append(" where pv.pk_pi=? and pv.eu_status <> '9' and pv.pk_org=? and pv.eu_pvtype in ('1','2','4') and ");
		sql.append(dateDiffFun).append("<=?");
		sql.append(" order by pv.date_reg desc");
		// 当日的就诊记录
		List<Map<String, Object>> mapResult = DataBaseHelper.queryForList(sql.toString(), new Object[] { mapParam.get("pkPi"), mapParam.get("pkOrg"),Integer.valueOf(val) });
		return mapResult;
	}
	
	/**
	 * 医嘱代录保存(处方)
	 * 007002003005->022003027068
	 * @param param
	 * @param user
	 * @return
	 */
	public void savePresAndFees(String param, IUser user) {

		OpOrdInsteadEntryDto ooied = JsonUtil.readValue(param, OpOrdInsteadEntryDto.class);
		String name = null;
		// 生成医嘱和处方信息
		// 调用记费服务，生成记费数据
		List<OpCgCnOrder> ordListParam = ooied.getCnOrders();
		if (ordListParam.size() <= 0 || ordListParam == null) throw new BusException("请录入处方明细!");
		if(ordListParam.get(0).getPkEmpOrd() != null || ordListParam.get(0).getPkEmpOrd() != ""){
			String sql = "SELECT * FROM BD_OU_EMPLOYEE WHERE PK_EMP = ? AND DEL_FLAG = '0'";
			BdOuEmployee emp = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, ordListParam.get(0).getPkEmpOrd());
		    name = emp.getNameEmp();
		}
		CnPrescription cnPrescription = ooied.getOpPres();
		User u = (User) user;
		String pkOrg = u.getPkOrg();
		// 保存时只能保存相同pkpv的信息
		String pkPres = cnPrescription.getPkPres();
		String pkPv = cnPrescription.getPkPv();
		PvEncounter pvInfo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, pkPv);
		// 加工前台传过来的数据
		List<OpCgCnOrder> ordsInsertList = new ArrayList<OpCgCnOrder>();
		List<OpCgCnOrder> ordsUpdateList = new ArrayList<OpCgCnOrder>();
		
		Set<String> pkCnOrderSet = new HashSet<String>();
		List<CnOrder> ordsInsertListDao = new ArrayList<CnOrder>();
		List<CnOrder> ordsUpdateListDao = new ArrayList<CnOrder>();
		List<OpCgCnOrder> returnResult = new ArrayList<OpCgCnOrder>();

		//查询当前医生考勤科室
		String pkDeptJob = getPkDeptJob(u.getPkEmp());
		 
		// 组装数据
		for (OpCgCnOrder opCgCnOrder : ordListParam) {
			opCgCnOrder.setPkDeptJob(pkDeptJob);
			opCgCnOrder.setPkDeptAreaapp(pvInfo.getPkDeptArea());//开立诊区（患者就诊所在诊区）
			if (opCgCnOrder.getPkCnord() == null) {
				ordsInsertList.add(opCgCnOrder);
			} else {
				pkCnOrderSet.add(opCgCnOrder.getPkCnord());
				ordsUpdateList.add(opCgCnOrder);
			}
		}
		// 分开处理
		// 一、保存修改的数据
		for (OpCgCnOrder opCgCnOrder : ordsUpdateList) {
			CnOrder ord = new CnOrder();
			ApplicationUtils.copyProperties(ord, opCgCnOrder);
			ApplicationUtils.setDefaultValue(ord, false);
			returnResult.add(opCgCnOrder);
			ordsUpdateListDao.add(ord);
		}
		// 二、新增保存
		for (OpCgCnOrder opCgCnOrder : ordsInsertList) {
			CnOrder ord = new CnOrder();
			ApplicationUtils.copyProperties(ord, opCgCnOrder);
			double vol = opCgCnOrder.getVol();
			ord.setPkOrg(pkOrg);
			ord.setEuPvtype(pvInfo.getEuPvtype());
			ord.setEuAlways(EnumerateParameter.ONE);// 临时
			ord.setDescOrd(ord.getNameOrd());
			ord.setCodeApply(null);
			ord.setNoteSupply(null);
			ord.setPkPres(pkPres);
			ord.setFlagFirst(EnumerateParameter.ZERO);
			ord.setEuStatusOrd(EnumerateParameter.ONE);// 签署状态
			ord.setDateEnter(new Date());
			ord.setDateStart(new Date());
			ord.setDateEffe(getDateEffec(pvInfo.getEuPvtype()));// 有效日期
			ord.setFlagDurg(EnumerateParameter.ONE);
			ord.setFlagSelf(EnumerateParameter.ZERO);
			ord.setFlagNote(EnumerateParameter.ZERO);
			ord.setFlagBase(EnumerateParameter.ZERO);
			ord.setFlagBl(EnumerateParameter.ONE);
			ord.setPkEmpInput(u.getPkEmp());
			ord.setNameEmpInput(u.getNameEmp());
			ord.setFlagSign(EnumerateParameter.ONE);
			ord.setDateSign(new Date());
			ord.setFlagStop(EnumerateParameter.ZERO);
			ord.setFlagStopChk(EnumerateParameter.ZERO);
			ord.setFlagErase(EnumerateParameter.ZERO);
			ord.setFlagEraseChk(EnumerateParameter.ZERO);
			ord.setFlagCp(EnumerateParameter.ZERO);
			ord.setFlagDoctor(EnumerateParameter.ONE);
			ord.setFlagPrint(EnumerateParameter.ZERO);
			ord.setFlagEmer(EnumerateParameter.ZERO);
			ord.setFlagMedout(EnumerateParameter.ZERO);
			ord.setFlagEmer(EnumerateParameter.ZERO);
			ord.setFlagThera(EnumerateParameter.ZERO);
			ord.setFlagPrev(EnumerateParameter.ZERO);
			ord.setFlagFit(EnumerateParameter.ZERO);
			ord.setOrds(1L);
			ord.setNoteOrd(ord.getNameOrd());
			ord.setNameEmpOrd(name);
			ApplicationUtils.setDefaultValue(ord, true);
			ApplicationUtils.copyProperties(opCgCnOrder, ord);
			// 调用供应链询价服务
			BlOpDrugStorePriceInfo blOpDrugStorePriceInfo = new BlOpDrugStorePriceInfo();
			blOpDrugStorePriceInfo.setPkPd(ord.getPkOrd());
			blOpDrugStorePriceInfo.setPkDept(ord.getPkDeptExec());
			blOpDrugStorePriceInfo.setQuanAp(ord.getQuanCg());
			blOpDrugStorePriceInfo.setPkOrg(pkOrg);
			blOpDrugStorePriceInfo.setNamePd(ord.getNameOrd());
			blOpDrugStorePriceInfo = priceStrategyService.queryDrugStorePriceInfo(blOpDrugStorePriceInfo);
			opCgCnOrder.setDateExpire(blOpDrugStorePriceInfo.getDateExpire());
			opCgCnOrder.setPriceCg(blOpDrugStorePriceInfo.getPrice());
			opCgCnOrder.setPriceCost(blOpDrugStorePriceInfo.getPriceCost());
			opCgCnOrder.setBatchNo(blOpDrugStorePriceInfo.getBatchNo());
			opCgCnOrder.setPackSize(blOpDrugStorePriceInfo.getPackSize().doubleValue());

			opCgCnOrder.setPkCnord(ord.getPkCnord());
			returnResult.add(opCgCnOrder);
			ordsInsertListDao.add(ord);
		}
		CnPrescription cnPresDao = DataBaseHelper.queryForBean("select * from cn_prescription where pk_pres=?", CnPrescription.class, new Object[] { pkPres });
		// 组织保存处方数据
		cnPrescription.setPkOrg(pkOrg);
		cnPrescription.setPkPv(pvInfo.getPkPv());
		cnPrescription.setPkPi(pvInfo.getPkPi());
		//cnPrescription.setNameEmpOrd(cnPresDao.getNameEmpOrd());
		ApplicationUtils.setDefaultValue(cnPrescription, false);
		if (cnPresDao == null)
			DataBaseHelper.insertBean(cnPrescription);
		else {
			DataBaseHelper.update("update cn_prescription set dt_prestype =:dtPrestype,pk_diag =:pkDiag,note =:note where pk_pres =:pkPres", cnPrescription);
		}
		if (ordsInsertList.size() > 0) {
			// 调用药品计费方法
			drugOpCg(ordsInsertList, pvInfo);
			// 保存用法附加费用 2019-10-23加
			saveSupplyFee(ordsInsertList, pvInfo);
			// 保存新增的医嘱
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), ordsInsertListDao);
		}
		if (ordsUpdateList.size() > 0) {
			// 删除掉之前的计费
			String sql = "delete from bl_op_dt where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnOrderSet, "pk_cnord") + ")";
			DataBaseHelper.execute(sql, new Object[] {});
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOrder.class), ordsUpdateListDao);
			// 重新计费
			drugOpCg(ordsUpdateList, pvInfo);
			// 保存用法附加费用 2019-10-23加
			saveSupplyFee(ordsUpdateList, pvInfo);
		}
		//20210515代录保存后更新就诊表状态
		DataBaseHelper.execute("update pv_encounter set eu_locked='0',eu_status='2' where pk_pv=? and del_flag='0'", pvInfo.getPkPv());
		
        //发送处方信息至平台
		if(ordsInsertListDao.size()>0){
			Map<String,Object> paramMap =  new HashMap<>();
			paramMap.put("type","NW");
			paramMap.put("cnOrder",ordsInsertListDao);
			PlatFormSendUtils.sendCnPresOpMsg(paramMap);
		}
		if(ordsUpdateListDao.size()>0){
			Map<String,Object> paramMap =  new HashMap<>();
			paramMap.put("type","RU");
			paramMap.put("cnOrder",ordsUpdateListDao);
			PlatFormSendUtils.sendCnPresOpMsg(paramMap);
		}
	}
	
	/**
	 * 保存用法附加费用
	 * @param occo
	 * @param pvInfo
	 */
	public void saveSupplyFee(List<OpCgCnOrder> occo, PvEncounter pvInfo){
		if(occo!=null && occo.size()>0){
			User u = UserContext.getUser();
			String pkOrg = u.getPkOrg();
			String pkDept = u.getPkDept();
			List<BlPubParamVo> bods = new ArrayList<BlPubParamVo>();
			
			for(int i=0; i<occo.size(); i++){
				//同组医嘱只有主医嘱生成附加费用，子医嘱不需要
				if(!occo.get(i).getOrdsn().equals(occo.get(i).getOrdsnParent()))
					continue;
				
				String addSql = "";
				if(!CommonUtils.isEmptyString(occo.get(i).getCodeSupply()) && 
						!CommonUtils.isEmptyString(occo.get(i).getCodeSupplyAdd())){
					addSql = " in ('"+occo.get(i).getCodeSupply().trim()+"','"+occo.get(i).getCodeSupplyAdd().trim()+"') ";
				}else if(!CommonUtils.isEmptyString(occo.get(i).getCodeSupply())){
					addSql = " ='"+occo.get(i).getCodeSupply().trim()+"' ";
				}else if(!CommonUtils.isEmptyString(occo.get(i).getCodeSupplyAdd())){
					addSql = " ='"+occo.get(i).getCodeSupplyAdd().trim()+"' ";
				}
				
				//查询用法附加费用
				StringBuffer sbfSql = new StringBuffer();
				sbfSql.append(" select * from bd_supply_item supItem ");
				sbfSql.append(" inner join bd_supply sup on sup.pk_supply = supItem.pk_supply and sup.del_flag = '0' ");
				sbfSql.append(" where supItem.del_flag='0' and supItem.eu_pvtype = ? and sup.code "+addSql);
				
				List<BdSupplyItem> addItemList = DataBaseHelper.queryForList(
						sbfSql.toString(),
						BdSupplyItem.class,new Object[]{pvInfo.getEuPvtype()});
				
				//组装记费Vo
				if(addItemList!=null && addItemList.size()>0){
					for(BdSupplyItem addItem : addItemList){
						BlPubParamVo bpb = new BlPubParamVo();
						bpb.setPkOrg(pkOrg);
						bpb.setEuPvType(pvInfo.getEuPvtype());
						bpb.setPkPv(pvInfo.getPkPv());
						bpb.setPkPi(pvInfo.getPkPi());
						bpb.setPkCnord(occo.get(i).getPkCnord());
						bpb.setPkPres(occo.get(i).getPkPres());
						bpb.setPkItem(addItem.getPkItem());
						bpb.setQuanCg(addItem.getQuan());
						bpb.setPkOrgEx(occo.get(i).getPkOrgExec());
						bpb.setPkOrgApp(pvInfo.getPkOrg());
						bpb.setPkDeptEx(occo.get(i).getPkDeptExec());
						bpb.setPkDeptApp(pvInfo.getPkDept());
						bpb.setPkEmpApp(occo.get(i).getPkEmpOrd());
						bpb.setNameEmpApp(occo.get(i).getNameEmpOrd());
						bpb.setFlagPd("0");
						bpb.setNamePd(occo.get(i).getNameOrd());
						bpb.setFlagPv(EnumerateParameter.ZERO);
						bpb.setDateExpire(occo.get(i).getDateExpire());
						bpb.setPkUnitPd(null);
						bpb.setPackSize(null);
						bpb.setPrice(null);
						bpb.setPriceCost(null);
						bpb.setDateHap(new Date());
						bpb.setPkDeptCg(pkDept);
						bpb.setPkEmpCg(u.getPkEmp());
						bpb.setNameEmpCg(u.getNameEmp());
						bpb.setEuAdditem("1");
						bpb.setPkDeptAreaapp(occo.get(i).getPkDeptArea());
						bods.add(bpb);
					}
				}
			}
			
			//调用记费接口
			//opcgPubService.blOpCg(bods);
			//新增记费记录
			if(bods.size()>0){
				opCgPubService.blOpCgBatch(bods);
			}
		}
	}

	/**
	 * 草药代录保存草药
	 * 007002003011->022003027069
	 * @param param
	 * @param user
	 * @return
	 */
	public String saveHerbAndFees(String param, IUser user) {
		// 一个处方信息对应一个医嘱信息对应多个草药细目信息
		OpOrdInsteadEntryDto ooied = JsonUtil.readValue(param, OpOrdInsteadEntryDto.class);
		User u = (User) user;
		// 草药明细信息（新加的）
		List<CnOrdHerb> cohs = ooied.getCnOrdHerbs();
		// 删除的草药明细信
		List<CnOrdHerb> cnOrdHerbDeletes = ooied.getCnOrdHerbDeletes();
		// 处方信息
		CnPrescription presInfo = ooied.getOpPres();
		// 医嘱信息
		CnOrder cnOrder = ooied.getCnOrder();
		
		//查询当前医生考勤科室
		String pkDeptJob = getPkDeptJob(u.getPkEmp());
		cnOrder.setPkDeptJob(pkDeptJob);
		
		//查询患者就诊所在诊区
		String pkDeptAreas = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept_area From pv_encounter where pk_pv=?",cnOrder.getPkPv()), "pkDeptArea");
		cnOrder.setPkDeptAreaapp(pkDeptAreas);//开立诊区（患者就诊所在诊区）
		
		List<OpCgCnOrder> cgCnOrders = new ArrayList<OpCgCnOrder>();
		StringBuffer nameOrd = new StringBuffer();
		String pkCnord = cnOrder.getPkCnord();
		for (CnOrdHerb cnOrdHerb : cohs) {
			OpCgCnOrder cgCnOrder = new OpCgCnOrder();
			cgCnOrder.setPkOrd(cnOrdHerb.getPkPd());
			cgCnOrder.setQuanCg(MathUtils.mul(cnOrdHerb.getQuan(), cnOrder.getOrds().doubleValue()));
			cgCnOrder.setNameOrd(cnOrdHerb.getNamePd());
			cgCnOrder.setPkUnitCg(cnOrdHerb.getPkUnit());
			cgCnOrder.setPkEmpOrd(presInfo.getPkEmpOrd());
			cgCnOrder.setNameEmpOrd(presInfo.getNameEmpOrd());
			cgCnOrder.setPkDeptJob(cnOrder.getPkDeptJob());
			cgCnOrder.setPkDeptAreaapp(cnOrder.getPkDeptAreaapp());
			cgCnOrder.setPackSize(1D);
			pkCnord = pkCnord == null ? NHISUUID.getKeyId() : pkCnord;
			cnOrdHerb.setPkCnord(pkCnord);
			cgCnOrders.add(cgCnOrder);
			nameOrd.append("【" + cnOrdHerb.getNamePd() + "】");
			ApplicationUtils.setDefaultValue(cnOrdHerb, true);
		}
		// 草药明细
		if (cohs != null && cohs.size() > 0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrdHerb.class), cohs);
		PvEncounter pvInfo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, presInfo.getPkPv());
		if (presInfo.getPkPres() == null) {
			// 加工处方信息
			presInfo.setDtPrestype("02");
			presInfo.setFlagPrt(EnumerateParameter.ZERO);
			ApplicationUtils.setDefaultValue(presInfo, true);
			// 插入处方信息
			DataBaseHelper.insertBean(presInfo);
			// 加工医嘱信息
			ApplicationUtils.setDefaultValue(cnOrder, true);
			cnOrder.setPkCnord(pkCnord);
			cnOrder.setEuPvtype(pvInfo.getEuPvtype());
			cnOrder.setEuAlways(EnumerateParameter.ONE);
			cnOrder.setPkPres(presInfo.getPkPres());
			cnOrder.setDateEffe(getDateEffec(pvInfo.getEuPvtype()));
			cnOrder.setCodeOrdtype("0103");
			cnOrder.setNameOrd(nameOrd.toString());
			cnOrder.setDescOrd(nameOrd.toString());
			cnOrder.setCodeFreq("once");
			cnOrder.setFlagDurg(EnumerateParameter.ONE);
			cnOrder.setFlagSelf(EnumerateParameter.ZERO);
			cnOrder.setFlagNote(EnumerateParameter.ZERO);
			cnOrder.setFlagBase(EnumerateParameter.ZERO);
			cnOrder.setFlagBl(EnumerateParameter.ONE);
			cnOrder.setPkEmpInput(u.getPkEmp());
			cnOrder.setNameEmpInput(u.getNameEmp());
			cnOrder.setFlagSign(EnumerateParameter.ONE);
			cnOrder.setDateSign(new Date());
			cnOrder.setFlagStop(EnumerateParameter.ZERO);
			cnOrder.setFlagStopChk(EnumerateParameter.ZERO);
			cnOrder.setFlagErase(EnumerateParameter.ZERO);
			cnOrder.setFlagEraseChk(EnumerateParameter.ZERO);
			cnOrder.setFlagCp(EnumerateParameter.ZERO);
			cnOrder.setFlagDoctor(EnumerateParameter.ONE);
			cnOrder.setFlagPrint(EnumerateParameter.ZERO);
			cnOrder.setFlagEmer(EnumerateParameter.ZERO);
			cnOrder.setFlagMedout(EnumerateParameter.ZERO);
			cnOrder.setFlagEmer(EnumerateParameter.ZERO);
			cnOrder.setFlagThera(EnumerateParameter.ZERO);
			cnOrder.setFlagPrev(EnumerateParameter.ZERO);
			cnOrder.setFlagFit(EnumerateParameter.ZERO);
			cnOrder.setDays(1L);
			cnOrder.setPkEmpInput(u.getPkEmp());
			cnOrder.setNameEmpInput(u.getNameEmp());
			cnOrder.setFlagSign(EnumerateParameter.ONE);
			cnOrder.setNoteOrd(presInfo.getNote());
			// 插入医嘱信息
			DataBaseHelper.insertBean(cnOrder);
		} else {
			ApplicationUtils.setDefaultValue(presInfo, false);
			DataBaseHelper.update("update cn_prescription set note=:note,ts=:ts where pk_pres=:pkPres", presInfo);
			nameOrd.setLength(0);
			if (cnOrdHerbDeletes != null && cnOrdHerbDeletes.size() > 0) {
				String sqlHerbName = "select pd.name from cn_ord_herb herb inner join bd_pd pd on herb.pk_pd=pd.pk_pd where herb.pk_cnord=?";
				List<Map<String, Object>> mapHerbName = DataBaseHelper.queryForList(sqlHerbName, cnOrder.getPkCnord());

				for (Map<String, Object> mapTemp : mapHerbName) {
					nameOrd.append("【" + mapTemp.get("name") + "】");
				}
			}
			ApplicationUtils.setDefaultValue(cnOrder, false);
			String sqlQueryCnOrd = "select ords from cn_order where pk_cnord=?";
			CnOrder co = DataBaseHelper.queryForBean(sqlQueryCnOrd, CnOrder.class, cnOrder.getPkCnord());
			if (co.getOrds() != cnOrder.getOrds()) {
				String sqlUpdateOrd = "update cn_order set ords=:ords,ts=:ts where pk_cnord=:pkCnord";
				if (nameOrd.length() > 0) {
					sqlUpdateOrd = "update cn_order set ords=:ords,ts=:ts,name_ord='" + nameOrd + "' where pk_cnord=:pkCnord";
				}
				DataBaseHelper.update(sqlUpdateOrd, cnOrder);
				List<BlOpDt> opDts = DataBaseHelper.queryForList("select * from bl_op_dt where pk_cnord=?", BlOpDt.class, cnOrder.getPkCnord());
				// 此处为了计算相对准确，特地重新查询处方明细表的数量，本来可以直接修改费用的明细的数量的（这个会出现除法）
				String sqlQueryHerb = "select pk_pd key_,quan,pk_ordherb from cn_ord_herb where pk_cnord=?";
				Map<String, Map<String, Object>> mapHerbs = DataBaseHelper.queryListToMap(sqlQueryHerb, cnOrder.getPkCnord());
				for (BlOpDt blOpDt : opDts) {
					// 明细的数量重新计算
					double quanNew = Double.parseDouble(mapHerbs.get(blOpDt.getPkPd()).get("quan").toString()) * cnOrder.getOrds();
					blOpDt.setQuan(quanNew);
				}
				// 调用更新明细服务
				opcgPubHelperService.updateDtQuan(opDts);
			}
		}

		for (OpCgCnOrder opCgCnOrder : cgCnOrders) {
			opCgCnOrder.setPkCnord(pkCnord);
			opCgCnOrder.setPkPres(presInfo.getPkPres());
			opCgCnOrder.setPkDeptExec(cnOrder.getPkDeptExec());
			opCgCnOrder.setPkOrgExec(cnOrder.getPkOrgExec());
			// 调用供应链询价服务
			BlOpDrugStorePriceInfo blOpDrugStorePriceInfo = new BlOpDrugStorePriceInfo();
			blOpDrugStorePriceInfo.setPkPd(opCgCnOrder.getPkOrd());
			blOpDrugStorePriceInfo.setPkDept(opCgCnOrder.getPkDeptExec());
			blOpDrugStorePriceInfo.setQuanAp(opCgCnOrder.getQuanCg());
			blOpDrugStorePriceInfo.setPkOrg(u.getPkOrg());
			blOpDrugStorePriceInfo.setNamePd(opCgCnOrder.getNameOrd());
			blOpDrugStorePriceInfo = priceStrategyService.queryDrugStorePriceInfo(blOpDrugStorePriceInfo);
			opCgCnOrder.setDateExpire(blOpDrugStorePriceInfo.getDateExpire());
			opCgCnOrder.setPriceCg(blOpDrugStorePriceInfo.getPrice());
			opCgCnOrder.setPriceCost(blOpDrugStorePriceInfo.getPriceCost());
			opCgCnOrder.setBatchNo(blOpDrugStorePriceInfo.getBatchNo());
			opCgCnOrder.setPackSize(blOpDrugStorePriceInfo.getPackSize().doubleValue());
		}
		// 调用药品计费
		if (cgCnOrders.size() > 0)
			drugOpCg(cgCnOrders, pvInfo);
		
		//20210515代录保存后更新就诊表状态
		DataBaseHelper.execute("update pv_encounter set eu_locked='0',eu_status='2' where pk_pv=? and del_flag='0'", pvInfo.getPkPv());
		return presInfo.getPkPres();
	}

	/**
	 * 删除医嘱代录的录入信息(西药成药细目处方，检查检验)
	 * 007002003012->022003027070
	 * @param param
	 * @param user
	 * @return
	 */
	public int deleteCurEnterInfo(String param, IUser user) {

		List<OpCgCnOrder> cgCnOrders = JsonUtil.readValue(param, new TypeReference<List<OpCgCnOrder>>() {
		});
		OpCgCnOrder cgCnOrder = cgCnOrders.get(0);
		String codeOrdtype = cgCnOrder.getCodeOrdtype().substring(0, 2);
		Set<String> pkCnords = new HashSet<String>();
		int num = 0;
		for (OpCgCnOrder opCgCnOrder : cgCnOrders) {
			pkCnords.add(opCgCnOrder.getPkCnord());
		}
		// 西药成药
		if ("01".equals(codeOrdtype)) {// 药品
			String newSql = "delete from bl_op_dt where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")";
			DataBaseHelper.execute(newSql, new Object[] {});
			String sql = "delete from cn_order where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")";
			num += DataBaseHelper.execute(sql, new Object[] {});

		}else if ("02".equals(codeOrdtype)) {// 检查
			String newSqlBl = "delete from bl_op_dt where pk_cnord =? ";
			DataBaseHelper.execute(newSqlBl, new Object[] { cgCnOrder.getPkCnord() });
			String sql = "delete from cn_order where pk_cnord =? ";
			num += DataBaseHelper.execute(sql, new Object[] { cgCnOrder.getPkCnord() });
			String newSql = "delete from cn_ris_apply where pk_cnord =? ";
			DataBaseHelper.execute(newSql, new Object[] { cgCnOrder.getPkCnord() });

		}else if ("03".equals(codeOrdtype)) {// 检验
			String newSqlBl = "delete from bl_op_dt where pk_cnord =? ";
			DataBaseHelper.execute(newSqlBl, new Object[] { cgCnOrder.getPkCnord() });
			String sql = "delete from cn_order where pk_cnord =? ";
			num += DataBaseHelper.execute(sql, new Object[] { cgCnOrder.getPkCnord() });
			String newSql = "delete from cn_lab_apply where pk_cnord =? ";
			DataBaseHelper.execute(newSql, new Object[] { cgCnOrder.getPkCnord() });

		}else if ("05".equals(codeOrdtype)) {// 治疗
			String newSqlBl = "delete from bl_op_dt where pk_cnord =? ";
			DataBaseHelper.execute(newSqlBl, new Object[] { cgCnOrder.getPkCnord() });
			String sql = "delete from cn_order where pk_cnord =? ";
			num += DataBaseHelper.execute(sql, new Object[] { cgCnOrder.getPkCnord() });


		}else {// 卫材、其他等
			String newSqlBl = "delete from bl_op_dt where pk_cnord =? ";
			DataBaseHelper.execute(newSqlBl, new Object[] { cgCnOrder.getPkCnord() });
			String sql = "delete from cn_order where pk_cnord =? ";
			num += DataBaseHelper.execute(sql, new Object[] { cgCnOrder.getPkCnord() });

		}
		return num;
	}

	/**
	 * 删除处方信息(西药成药处方，草药处方)
	 * 007002003016->022003027071
	 * @param param
	 * @param user
	 * @return
	 */
	public int deleteCnPresInfoAndFees(String param, IUser user) {

		String pkPres = JsonUtil.readValue(param, HashMap.class).get("pkPres").toString();

		CnPrescription pres = DataBaseHelper.queryForBean("select * from cn_prescription where pk_pres=?", CnPrescription.class, pkPres);
		if ("02".equals(pres.getDtPrestype())) {// 草药
			CnOrder co = DataBaseHelper.queryForBean("select * from cn_order where pk_pres = ?", CnOrder.class, pkPres);
			String sqlHerb = "delete from cn_ord_herb where pk_cnord=?";
			DataBaseHelper.execute(sqlHerb, co.getPkCnord());
		}
		String sqlBod = "delete from bl_op_dt where pk_pres=?";
		DataBaseHelper.execute(sqlBod, pkPres);
		String sqlOrd = "delete from cn_order where pk_pres=?";
		DataBaseHelper.execute(sqlOrd, pkPres);


		String sqlPres = "delete from cn_prescription where pk_pres=?";
		return DataBaseHelper.execute(sqlPres, pkPres);
	}

	/**
	 * 删除草药明细信息
	 * 007002003017->022003027072
	 * @param param
	 * @param user
	 * @return
	 */
	public int deleteCnHerbDetailAndFees(String param, IUser user) {

		List<CnOrdHerb> cnOrdHerbs = JsonUtil.readValue(param, new TypeReference<List<CnOrdHerb>>() {
		});
		Set<String> pkOrdherbs = new HashSet<String>();
		for (CnOrdHerb cnOrdHerb : cnOrdHerbs) {
			pkOrdherbs.add(cnOrdHerb.getPkOrdherb());
		}
		String sqlBod = "delete from bl_op_dt where exists (select 1 from cn_ord_herb where  bl_op_dt.pk_cnord=cn_ord_herb.pk_cnord and bl_op_dt.pk_pd=cn_ord_herb.pk_pd and cn_ord_herb.pk_ordherb in ("
				+ CommonUtils.convertSetToSqlInPart(pkOrdherbs, "cn_ord_herb.pk_ordherb") + "))";
		DataBaseHelper.execute(sqlBod, new Object[] {});
		String sqlOrd = "delete from cn_ord_herb where pk_ordherb in (" + CommonUtils.convertSetToSqlInPart(pkOrdherbs, "pk_ordherb") + ")";

		return DataBaseHelper.execute(sqlOrd, new Object[] {});
	}

	/**
	 * 查询录入人是当前登录人的处方明细
	 * 007002003008->022003027073
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryCurEnterPresInfo(String param, IUser user) {

		OpOrdInsteadEntryDto ooied = JsonUtil.readValue(param, OpOrdInsteadEntryDto.class);
		User u = (User) user;
		StringBuffer sql = new StringBuffer();
		sql.append("select pd.*,ord.*,opdt.date_expire,opdt.price_cost,opdt.price_org price_cg,unitdos.name name_dosage   from cn_order ord inner join bd_pd pd on pd.pk_pd=ord.pk_ord");
		sql.append(" inner join bd_unit unitdos on unitdos.pk_unit=ord.pk_unit_dos ");
		sql.append(" inner join cn_prescription pres   on ord.pk_pres = pres.pk_pres ");
		sql.append(" inner join bl_op_dt opdt   on opdt.pk_cnord = ord.pk_cnord and opdt.flag_settle='0' and opdt.flag_pd='1'");
		sql.append(" where ord.eu_pvtype < 3  and ord.pk_pres = ? and ord.pk_emp_input = ?  order by ord.ordsn");
		List<Map<String, Object>> result = DataBaseHelper.queryForList(sql.toString(), new Object[] { ooied.getPkPres(), u.getPkEmp() });
		if (result == null || result.size() == 0)
			throw new BusException("此处方不是当前登录用户录入的，不能修改");
		return result;
	}

	/**
	 * 查询录入人是当前登录人的所有检查检验医嘱
	 * 007002003009->022003027074
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> queryCurEnterLisRisInfo(String param, IUser user) {

		OpOrdInsteadEntryDto ooied = JsonUtil.readValue(param, OpOrdInsteadEntryDto.class);
		
		if(CommonUtils.isEmptyString(ooied.getCodeOrdtype())){
			throw new BusException("您选择的是非医嘱项目，不能修改！");
		}
		
		User u = (User) user;
		StringBuffer sql = new StringBuffer();
		String codeOrdtypeParent = ooied.getCodeOrdtype().substring(0, 2);
		if ("02".equals(codeOrdtypeParent)) {// 检查
			sql.append("select ord.*,opdt.price_org priceCg,opdt.amount,opdt.quan quanCg,cra.* from cn_order ord inner join bl_op_dt opdt   on opdt.pk_cnord = ord.pk_cnord  and opdt.flag_settle='0' inner join cn_ris_apply cra on cra.pk_cnord = ord.pk_cnord ");
		}
		if ("03".equals(codeOrdtypeParent)) {// 检验
			sql.append("select ord.*,opdt.price_org priceCg,opdt.amount,opdt.quan quanCg,cla.* from cn_order ord inner join bl_op_dt opdt   on opdt.pk_cnord = ord.pk_cnord  and opdt.flag_settle='0' inner join cn_lab_apply cla on cla.pk_cnord = ord.pk_cnord ");
		}
		if ("05".equals(codeOrdtypeParent)) {// 治疗
			sql.append("select ord.*,opdt.price_org priceCg,opdt.amount,opdt.quan quanCg from cn_order ord inner join bl_op_dt opdt   on opdt.pk_cnord = ord.pk_cnord  and opdt.flag_settle='0'  ");
		}
		if(CommonUtils.isEmptyString(CommonUtils.getString(sql)))
			sql.append("select ord.*,opdt.price_org priceCg,opdt.amount,opdt.quan quanCg from cn_order ord inner join bl_op_dt opdt   on opdt.pk_cnord = ord.pk_cnord  and opdt.flag_settle='0'  ");
			
		sql.append("  where ord.eu_pvtype < 3 and ord.pk_cnord=? and ord.pk_emp_input = ?");

		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(), new Object[] { ooied.getPkCnord(), u.getPkEmp() });
		if (mapResult == null)
			throw new BusException("此医嘱不是当前登录用户录入的，不能修改！");
		return mapResult;

	}

	/**
	 * 查询录入人是当前登录人的草药处方信息
	 * 007002003010->022003027075
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryCurEnterHerbInfo(String param, IUser user) {

		OpOrdInsteadEntryDto ooied = JsonUtil.readValue(param, OpOrdInsteadEntryDto.class);
		User u = (User) user;
		StringBuffer sql = new StringBuffer();
		sql.append(" select herb.*,pd.name name_pd,unit.name unit_name   from cn_order ord inner join cn_prescription pres on ord.pk_pres=pres.pk_pres  ");
		sql.append(" inner join cn_ord_herb herb on herb.pk_cnord = ord.pk_cnord inner join bd_pd pd on pd.pk_pd=herb.pk_pd inner join bd_unit unit on herb.pk_unit=unit.pk_unit  where pres.pk_pres=? and ord.pk_emp_input = ?");
		List<Map<String, Object>> result = DataBaseHelper.queryForList(sql.toString(), new Object[] { ooied.getPkPres(), u.getPkEmp() });
		if (result == null || result.size() == 0)
			throw new BusException("此处方不是当前登录用户录入的，不能修改");
		return result;
	}

	/**
	 * 通用记费方法
	 * @param occo
	 * @param pvInfo
	 */
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
			bpb.setFlagPd(opCgCnOrder.getPkPres() == null ? EnumerateParameter.ZERO : EnumerateParameter.ONE);
			bpb.setNamePd(opCgCnOrder.getNameOrd());
			bpb.setFlagPv(EnumerateParameter.ZERO);
			bpb.setDateExpire(opCgCnOrder.getDateExpire());
			bpb.setPkUnitPd(opCgCnOrder.getPkUnitCg());
			bpb.setPackSize(opCgCnOrder.getPackSize().intValue());
			bpb.setPrice(opCgCnOrder.getPriceCg());
			bpb.setPriceCost(opCgCnOrder.getPriceCost());
			bpb.setDateHap(new Date());
			bpb.setPkDeptCg(pkDept);
			bpb.setPkEmpCg(u.getPkEmp());
			bpb.setNameEmpCg(u.getNameEmp());
			bpb.setPkDeptJob(opCgCnOrder.getPkDeptJob());
			bpb.setPkDeptAreaapp(opCgCnOrder.getPkDeptAreaapp());
			bods.add(bpb);
		}
		// 批量记费
		//opcgPubService.blOpCg(bods);
		
		//新增记费记录
		if(bods.size()>0){
			opCgPubService.blOpCgBatch(bods);
		}
	}

	/**
	 * 医嘱代录保存(Lis,Ris)
	 * 007002003006->022003027076
	 * @param param
	 * @param user
	 * @return
	 */
	public List<OpCgCnOrder> saveLisRisAndFees(String param, IUser user) {
		List<OpCgCnOrder> ordListParam = JsonUtil.readValue(param, new TypeReference<List<OpCgCnOrder>>() {
		});
		// 生成医嘱和检查检验信息
		// 调用记费服务，生成记费数据
		User u = (User) user;
		String pkOrg = u.getPkOrg();
		// 保存时只能保存相同pkpv的信息
		String pkPv = ordListParam.get(0).getPkPv();
		PvEncounter pvInfo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, pkPv);
		List<CnRisApply> cras = new ArrayList<CnRisApply>();
		List<CnLabApply> clas = new ArrayList<CnLabApply>();
		// 加工前台传过来的数据
		List<OpCgCnOrder> ordsInsertList = new ArrayList<OpCgCnOrder>();
		List<OpCgCnOrder> ordsUpdateList = new ArrayList<OpCgCnOrder>();
		Set<String> pkCnOrderSet = new HashSet<String>();
		List<OpCgCnOrder> returnResult = new ArrayList<OpCgCnOrder>();
		// 持久化医嘱
		List<CnOrder> ordsInsertListDao = new ArrayList<CnOrder>();
		List<CnOrder> ordsUpdateListDao = new ArrayList<CnOrder>();
		
		//查询当前医生考勤科室
		String pkDeptJob = getPkDeptJob(u.getPkEmp());
		
		for (OpCgCnOrder opCgCnOrder : ordListParam) {
			opCgCnOrder.setPkDeptJob(pkDeptJob);
			opCgCnOrder.setPkDeptAreaapp(pvInfo.getPkDeptArea());//开立诊区（患者就诊所在诊区）
			if (opCgCnOrder.getPkCnord() == null) {
				ordsInsertList.add(opCgCnOrder);
			} else {
				pkCnOrderSet.add(opCgCnOrder.getPkCnord());
				ordsUpdateList.add(opCgCnOrder);
			}
		}
		for (OpCgCnOrder opCgCnOrder : ordsUpdateList) {
			CnOrder ord = new CnOrder();
			ApplicationUtils.copyProperties(ord, opCgCnOrder);
			ApplicationUtils.setDefaultValue(ord, false);
			returnResult.add(opCgCnOrder);
			ordsUpdateListDao.add(ord);
		}
		for (int i = 0; i < ordsInsertList.size(); i++) {
			CnOrder ord = new CnOrder();
			ApplicationUtils.copyProperties(ord, ordsInsertList.get(i));
			String codeOrdtypeParent = ord.getCodeOrdtype().substring(0, 2);
			ord.setPkOrg(pkOrg);
			ord.setEuPvtype(pvInfo.getEuPvtype());
			ord.setDateEffe(getDateEffec(pvInfo.getEuPvtype()));// 有效日期
			ord.setDescOrd(ord.getNameOrd());
			ord.setOrdsn(ord.getOrdsn());
			ord.setFlagFirst(EnumerateParameter.ZERO);
			ord.setEuStatusOrd(EnumerateParameter.ONE);// 签署状态
			ord.setDateEnter(new Date());
			ord.setFlagDurg(EnumerateParameter.ZERO);
			ord.setFlagSelf(EnumerateParameter.ZERO);
			ord.setFlagNote(EnumerateParameter.ZERO);
			ord.setFlagBase(EnumerateParameter.ZERO);
			ord.setFlagBl(EnumerateParameter.ONE);
			ord.setPkEmpInput(u.getPkEmp());
			ord.setNameEmpInput(u.getNameEmp());
			ord.setFlagSign(EnumerateParameter.ONE);
			ord.setDateSign(new Date());
			ord.setFlagStop(EnumerateParameter.ZERO);
			ord.setFlagStopChk(EnumerateParameter.ZERO);
			ord.setFlagErase(EnumerateParameter.ZERO);
			ord.setFlagEraseChk(EnumerateParameter.ZERO);
			ord.setFlagCp(EnumerateParameter.ZERO);
			ord.setFlagDoctor(EnumerateParameter.ONE);
			ord.setFlagPrint(EnumerateParameter.ZERO);
			ord.setFlagEmer(EnumerateParameter.ZERO);
			ord.setFlagMedout(EnumerateParameter.ZERO);
			ord.setFlagEmer(EnumerateParameter.ZERO);
			ord.setFlagThera(EnumerateParameter.ZERO);
			ord.setFlagPrev(EnumerateParameter.ZERO);
			ord.setFlagFit(EnumerateParameter.ZERO);
			ord.setNoteOrd(ord.getNameOrd());
			Date date = new Date();
			ord.setDateStart(date);
			ApplicationUtils.setDefaultValue(ord, true);
			ApplicationUtils.copyProperties(ordsInsertList.get(i), ord);
			ordsInsertListDao.add(ord);
			returnResult.add(ordsInsertList.get(i));
			if ("02".equals(codeOrdtypeParent)) {// 检查
				CnRisApply cra = new CnRisApply();
				cra.setPkOrg(pkOrg);
				cra.setPkCnord(ord.getPkCnord());
				cra.setFlagBed(EnumerateParameter.ZERO);
				cra.setDescBody(ordListParam.get(i).getDescBody());
				//检查过程状态
				cra.setEuStatus(EnumerateParameter.ZERO);
				cra.setFlagPrint(EnumerateParameter.ZERO);
				ApplicationUtils.setDefaultValue(cra, true);
				cras.add(cra);
			}
			if ("03".equals(codeOrdtypeParent)) {// 检验
				CnLabApply cla = new CnLabApply();
				cla.setPkOrg(pkOrg);
				cla.setPkCnord(ord.getPkCnord());
				cla.setDtSamptype(ordListParam.get(i).getDtSamptype());
				//检验过程状态
				cla.setEuStatus(EnumerateParameter.ZERO);
				cla.setFlagPrt(EnumerateParameter.ZERO);
				ApplicationUtils.setDefaultValue(cla, true);
				clas.add(cla);
			}
			if ("05".equals(codeOrdtypeParent)) {// 治疗

			}
		}
		if (ordsInsertList.size() > 0) {
			// 批量保存医嘱
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), ordsInsertListDao);
			drugOpCg(ordsInsertList, pvInfo);
		}
		if (ordsUpdateList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOrder.class), ordsUpdateListDao);
			for (CnOrder cnOrder : ordsUpdateListDao) {
				String codeOrdtypeParent = cnOrder.getCodeOrdtype().substring(0, 2);
				if ("02".equals(codeOrdtypeParent)) {// 检查
					for (OpCgCnOrder opCgCnOrder : ordListParam) {
						if (cnOrder.getPkCnord().equals(opCgCnOrder.getPkCnord())) {
							String sql = "update cn_ris_apply set desc_body = ? where  pk_cnord = ? ";
							DataBaseHelper.execute(sql, opCgCnOrder.getDescBody(), cnOrder.getPkCnord());
						}
					}
				}
				if ("03".equals(codeOrdtypeParent)) {// 检验
					for (OpCgCnOrder opCgCnOrder : ordListParam) {
						if (cnOrder.getPkCnord().equals(opCgCnOrder.getPkCnord())) {
							String sql = "update cn_lab_apply set dt_samptype = ? where  pk_cnord = ? ";
							DataBaseHelper.execute(sql, opCgCnOrder.getDtSamptype(), cnOrder.getPkCnord());
						}
					}
				}
				if ("05".equals(codeOrdtypeParent)) {// 治疗

				}
			}
			// 删除掉之前的计费
			String sql = "delete from bl_op_dt  where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnOrderSet, "pk_cnord") + ")";
			DataBaseHelper.execute(sql, new Object[] {});
			drugOpCg(ordsUpdateList, pvInfo);
		}
		// 批量保存检查
		if (cras.size() > 0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnRisApply.class), cras);
		// 批量保存检验
		if (clas.size() > 0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnLabApply.class), clas);
		
		//灵璧发送检查申请单至平台
	    List<CnOrder> risLis = new ArrayList<>();
	    List<CnOrder> lisLis = new ArrayList<>();
		if(ordsInsertListDao !=null && ordsInsertListDao.size()>0){
			for(CnOrder ord : ordsInsertListDao){
				//判断检验
				if(("03").equals(ord.getCodeOrdtype().substring(0, 2))){
					lisLis.add(ord);
				}else if(("02").equals(ord.getCodeOrdtype().substring(0, 2))){
					risLis.add(ord);
				}
			}
		}
		if(risLis!=null && risLis.size()>0){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkPv", pkPv);
			//ORC-1申请控制NW:新增申请
			paramMap.put("Control", "NW");
			paramMap.put("risList", risLis);
			paramMap.put("type", "ris");
			//门诊状态参数
			paramMap.put("state", "out");
			PlatFormSendUtils.sendCnMedApplyMsg(paramMap);
		}
		if(lisLis!=null && lisLis.size()>0){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkPv", pkPv);
			//ORC-1申请控制NW:新增申请
			paramMap.put("Control", "NW");
			paramMap.put("lisList", lisLis);
			paramMap.put("type", "lis");
			//门诊状态参数
			paramMap.put("state", "out");
			PlatFormSendUtils.sendCnMedApplyMsg(paramMap);
		}
		//20210515代录保存后更新就诊表状态
		DataBaseHelper.execute("update pv_encounter set eu_locked='0',eu_status='2' where pk_pv=? and del_flag='0'", pvInfo.getPkPv());
		return returnResult;
	}

	/**
	 * 计算医嘱执行次数
	 * 007002003007->022003027077
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public int countOrderTimesOP(String param, IUser user) {

		CnOrder co = JsonUtil.readValue(param, CnOrder.class);
		if (null == co)
			throw new BusException("没有获取到医嘱信息！");
		Date start = null;
		if (null == co.getDateStart())
			start = new Date();
		else
			start = co.getDateStart();

		if (null == co.getDays())
			throw new BusException("没有获取到医嘱天数！");
		Date endd = new Date(start.getTime() + co.getDays() * 1000 * 60 * 60 * 24);

		OrderAppExecVo exceVO = new OrderFreqCalCountHandler().calCount(co.getCodeOrdtype(), co.getCodeFreq(), start, endd, co.getQuan(), false);

		return new Double(exceVO.getCount()).intValue();
	}

	private Date getDateEffec(String euPvType) {

		String val = ApplicationUtils.getSysparam("CN0004", false);
		if (StringUtils.isEmpty(val)) {
			if (EnumerateParameter.TWO.equals(euPvType))// 急诊
			{
				val = EnumerateParameter.TWO;
			} else {// 门诊
				val = EnumerateParameter.THREE;
			}

		}
		Date dateEffec = DateUtils.getSpecifiedDay(new Date(), Integer.parseInt(val));
		return dateEffec;
	}
	
	/**
	 * 查询当前医生考勤科室
	 * @param pkEmp
	 * @return
	 */
	private String getPkDeptJob(String pkEmp) {
		//查询当前医生考勤科室
		BdOuEmpjob emp = DataBaseHelper.queryForBean(
				"SELECT * FROM bd_ou_empjob WHERE pk_emp =? AND is_main = '1' ", BdOuEmpjob.class, new Object[]{pkEmp});
		return emp != null ? emp.getPkDept() : null;
	}


}