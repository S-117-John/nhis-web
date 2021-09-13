package com.zebone.nhis.scm.ipdedrug.service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdDe;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ex.nis.ns.support.ExlistPrintSortByOrdUtil;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.scm.ipdedrug.dao.IpDeDrugMapper;
import com.zebone.nhis.scm.ipdedrug.dao.IpPdDrugPackMapper;
import com.zebone.nhis.scm.ipdedrug.vo.IpDeDrugDto;
import com.zebone.nhis.scm.ipdedrug.vo.IpPivasBackDrugVo;
import com.zebone.nhis.scm.pub.service.PdStInPubService;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.vo.PdInParamVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 住院药房退药服务
 * @author jiangdong
 *
 */
@Service
public class IpPdReBackDrugService {

	@Autowired
	private IpDeDrugMapper ipDeDrugMapper;

	@Autowired
	private PdStOutPubService pdStOutPubService;

	@Autowired
	private PdStInPubService pdStInPubService;

	@Autowired
	private IpCgPubService ipCgPubService;
	
	@Autowired
	private IpPdDrugPackMapper drugPackMapper;
	
	private Logger logger = LoggerFactory.getLogger("nhis.scm");

	/**
	 * 住院医嘱退药请领单查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryIpReBackAppListByCDT(String param, IUser user) {
		IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
		Map<String,Object> paramMap=new HashMap<String, Object>();
		User userCur = (User) user;
		paramMap.put("pkDeptDe",userCur.getPkDept());
		if(ipDeDrugDto.getDateStart()!=null){
			paramMap.put("dateStart", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.getDateMorning(ipDeDrugDto.getDateStart(), 0)));
		}
		if(ipDeDrugDto.getDateEnd()!=null){
			paramMap.put("dateEnd", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss",DateUtils.getDateMorning(ipDeDrugDto.getDateEnd(), 1)));
		}
		paramMap.put("pkDeptAp", ipDeDrugDto.getPkAppDeptNs());
		paramMap.put("codeAp", ipDeDrugDto.getCodeAp());
		List<Map<String, Object>> mapResult=ipDeDrugMapper.queryIpReBackAppListByCDT(paramMap);
		return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
	}

	/**
	 * 根据条件查询药品退药请领明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryIpReBackDrugDetailByCDT(String param, IUser user) {

		IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
		User userCur = (User) user;
		ipDeDrugDto.setPkPdStock(userCur.getPkStore());
		ipDeDrugDto.setPkOrg(userCur.getPkOrg());
		List<Map<String, Object>> mapResult = ipDeDrugMapper.qryIpReBackDrugDetailByCDT(ipDeDrugDto);
		return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
	}

	/**
	 * 退药处理
	 * @param param
	 * @param user
	 * @return
	 */
	public String mergeIpReBackDrug(String param, IUser user) {
		// 前台传过来是勾选的请领明细
		List<ExPdApplyDetail> exPdAppDetails = JsonUtil.readValue(param, new TypeReference<List<ExPdApplyDetail>>() {});
		// 获取本次发药的退药单号
		String codeRefund = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_IPDS);
		
		return this.mergeIpReBackDrug(exPdAppDetails,codeRefund,new Date(),(User)user);
	}
	/**
	 * 退药处理公共服务
	 * @param exPdAppDetails
	 * @param codeRefund
	 * @param userCur
	 * @return
	 */
	public String mergeIpReBackDrug(List<ExPdApplyDetail> exPdAppDetails, String codeRefund,Date dateDe,User userCur) {
		/*
		 * 1、调用供应链出库服务 2、调用住院退费服务 3、写退药表 4、更新请领明细
		 */
		if (exPdAppDetails == null || exPdAppDetails.size() <= 0)
			throw new BusException("传入的请领明细数据错误");
		String pkOrg = userCur.getPkOrg();
		String pkDept = userCur.getPkDept();
		String pkEmp = userCur.getPkEmp();
		String nameEmp = userCur.getNameEmp();
		String pkStock = userCur.getPkStore();
		// 利用set集合的不可重复性，存储本次发药请领单的主键
		Set<String> pkPdApSet = new HashSet<>();
		String pkOrgAp = "";
		String pkDeptAp = "";
		List<String> pkcgipList=new ArrayList<>();//退药申请明细主键
		
		for (ExPdApplyDetail exap : exPdAppDetails) {
			pkcgipList.add(exap.getPkCgip());
		}
		List<ExPdApplyDetail> retapdtPriceList=drugPackMapper.qryRetDrugPrice(pkcgipList);
		// 调用出库服务
		// 将前台勾选的出库明细转化成出库服务需要的vo
		List<PdInParamVo> popvList = new ArrayList<PdInParamVo>();
		for (ExPdApplyDetail exPdApplyDetail : exPdAppDetails) {
			pkOrgAp = exPdApplyDetail.getPkOrgAp();
			pkDeptAp = exPdApplyDetail.getPkDeptAp();
			PdInParamVo popv = new PdInParamVo();
			popv.setPkPd(exPdApplyDetail.getPkPd());
			popv.setPkPdapdt(exPdApplyDetail.getPkPdapdt());
			for (ExPdApplyDetail retpd : retapdtPriceList) {
				if(exPdApplyDetail.getPkCgip().equals(retpd.getPkCgip())){
					popv.setPriceCost(retpd.getPriceCost());
					popv.setPrice(retpd.getPrice());
					popv.setBatchNo(retpd.getBatchNo());
					popv.setDateExpire(retpd.getDateExpire());
				}
			}
			//以下逻辑是为兼容原始数据未写入问题
			if(CommonUtils.isNull(popv.getPrice())){
				popv.setPrice(exPdApplyDetail.getPrice());
			}
			if(CommonUtils.isNull(popv.getPriceCost())){
				popv.setPriceCost(exPdApplyDetail.getPriceCost());		
			}
			if(CommonUtils.isNull(popv.getBatchNo())){
				popv.setBatchNo(exPdApplyDetail.getBatchNo());
			}
			if(CommonUtils.isNull(popv.getDateExpire())){
				popv.setDateExpire(exPdApplyDetail.getDateExpire());
			}
			popv.setQuanMin(exPdApplyDetail.getQuanMin());
			popv.setQuanPack(Math.abs(exPdApplyDetail.getQuanPack()));
			popv.setAmount(Math.abs(exPdApplyDetail.getAmount()));
			popv.setPackSize(exPdApplyDetail.getPackSize());
			popv.setPkPv(exPdApplyDetail.getPkPv());
			popv.setPkCnOrd(exPdApplyDetail.getPkCnord());
			popv.setPkUnitPack(exPdApplyDetail.getPkUnit());
			popvList.add(popv);
			// 存放请领单主键
			pkPdApSet.add(exPdApplyDetail.getPkPdap());
		}
		// 返回请领明细主键和入库明细主键
		Map<String, Object> mapInResult = pdStInPubService.execStIn(pkOrgAp,pkDeptAp,popvList, pkStock);
		List<RefundVo> refundVos = new ArrayList<RefundVo>();
		List<ExPdDe> exPdDes = new ArrayList<ExPdDe>();
		List<ExPdApplyDetail> exPdAppDetailsNew = new ArrayList<ExPdApplyDetail>();
		for (ExPdApplyDetail exPdApplyDetail : exPdAppDetails) {
			// 调用住院退费服务
			RefundVo refundVo = new RefundVo();
			refundVo.setPkOrg(pkOrg);
			refundVo.setPkDept(pkDept);
			refundVo.setNameEmp(nameEmp);
			refundVo.setPkEmp(pkEmp);
			refundVo.setPkCgip(exPdApplyDetail.getPkCgip());
			refundVo.setQuanRe(Math.abs(exPdApplyDetail.getQuanPack()));

			// 写退药表ex_pd_de
			ExPdDe exPdDe = new ExPdDe();
			// 设置默认值
			ApplicationUtils.setDefaultValue(exPdDe, true);
			refundVo.setPkOrdexdt(exPdDe.getPkPdde());
			refundVos.add(refundVo);
			exPdDe.setPkPdapdt(exPdApplyDetail.getPkPdapdt());
			exPdDe.setPkOrg(pkOrg);
			exPdDe.setCodeDe(codeRefund);// 调用编码生成规则获取退药单号
			exPdDe.setEuDirect("-1");
			exPdDe.setPkCnord(exPdApplyDetail.getPkCnord());
			exPdDe.setPkDeptAp(exPdApplyDetail.getPkDeptAp());
			exPdDe.setPkOrgAp(exPdApplyDetail.getPkOrgAp());
			exPdDe.setPkPv(exPdApplyDetail.getPkPv());
			exPdDe.setPkPd(exPdApplyDetail.getPkPd());
			exPdDe.setPkUnit(exPdApplyDetail.getPkUnit());
			exPdDe.setDateExpire(exPdApplyDetail.getDateExpire());
			exPdDe.setPackSize(exPdApplyDetail.getPackSize());
			exPdDe.setBatchNo(exPdApplyDetail.getBatchNo());
			exPdDe.setQuanPack(Math.abs(exPdApplyDetail.getQuanPack()));
			exPdDe.setQuanMin(exPdApplyDetail.getQuanMin());
			exPdDe.setPriceCost(exPdApplyDetail.getPriceCost());
			exPdDe.setPrice(exPdApplyDetail.getPrice());
			exPdDe.setAmount(Math.abs(exPdApplyDetail.getAmount()));// 包装数量乘以零售单价
			exPdDe.setPkPdstdt(mapInResult.get(exPdApplyDetail.getPkPdapdt()).toString());
			exPdDe.setPkPddecate(exPdApplyDetail.getPkPddecate());
			exPdDe.setNameDecate(exPdApplyDetail.getNameDecate());
			exPdDe.setPkDeptDe(pkDept);
			exPdDe.setPkStoreDe(pkStock);
			exPdDe.setDateDe(dateDe);
			exPdDe.setPkEmpDe(pkEmp);
			exPdDe.setNameEmpDe(nameEmp);
			exPdDe.setFlagPrt("0");
			exPdDe.setFlagPivas("0");
			exPdDe.setFlagBarcode("0");
			exPdDes.add(exPdDe);
			exPdApplyDetail.setFlagDe("1");
			exPdApplyDetail.setFlagFinish("1");
			ApplicationUtils.setDefaultValue(exPdApplyDetail, false);
			exPdAppDetailsNew.add(exPdApplyDetail);
		}
		// 所有数据库操作全部放在循环外边做
		// 批量更新请领明细
		Set<String> pkPdapdts=new HashSet<String>();
		for (ExPdApplyDetail exPdapDt : exPdAppDetailsNew) {
			pkPdapdts.add(exPdapDt.getPkPdapdt());
		}
		if(pkPdapdts.size()>0){
			String updateApDetail ="update ex_pd_apply_detail set flag_de='1',flag_finish='1',ts=? where pk_pdapdt in (" + CommonUtils.convertSetToSqlInPart(pkPdapdts, "pk_pdapdt") + ") and flag_de='0' and flag_finish='0' and nvl(flag_canc,'0') <> '1' and flag_stop='0'";
			int count=DataBaseHelper.execute(updateApDetail, new Date());
			if(count!=exPdAppDetailsNew.size()){
				throw new BusException("您本次提交的记录已变更，请刷新后重新操作！");
			}
		}
		
		// 批量插入发药记录表
		 DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdDe.class), exPdDes);
		
		// 批量退费
		BlPubReturnVo rtnvo = ipCgPubService.refundInBatch(refundVos);
		int cgcnt = 0;
		if(rtnvo==null||rtnvo.getBids()==null||rtnvo.getBids().size()<=0){
			logger.info("================退药单【"+codeRefund+"】在退费时未产生退费明细================");
		}else{
			cgcnt = rtnvo.getBids().size();
		}
		logger.info("================退药单【"+codeRefund+"】对应"+exPdAppDetails.size()+"条退请领明细，共生成"+exPdDes.size()+"条退药记录,"+cgcnt+"条退费明细记录================");
		// 批量更新表ex_pd_apply
		List<ExPdApply> exPdApplys = new ArrayList<ExPdApply>();
		for (String pkPdAp : pkPdApSet) {
			ExPdApply exPdApply = new ExPdApply();
			exPdApply.setPkPdap(pkPdAp);
			exPdApplys.add(exPdApply);
		}
		String sqlTemp = "update ex_pd_apply set flag_finish='1' where pk_pdap=:pkPdap and not exists (select 1  from ex_pd_apply_detail detail  where ex_pd_apply.pk_pdap = detail.pk_pdap and detail.flag_finish = '0' and nvl(detail.flag_canc,'0')='0' and detail.flag_stop='0')";
		DataBaseHelper.batchUpdate(sqlTemp, exPdApplys);
		// 只要请领单有一条明细发放，那么请领单的状态就是发放状态
		String sqlTemp2 = "update ex_pd_apply set eu_status='1' where pk_pdap=:pkPdap and  exists (select 1  from ex_pd_apply_detail detail  where ex_pd_apply.pk_pdap = detail.pk_pdap and detail.flag_de = '1')";
		DataBaseHelper.batchUpdate(sqlTemp2, exPdApplys);
		/**
		 * 发送住院退药信息
		 */
		Map<String, Object> map = new HashMap<>();
		map.put("exPdAppDetails",exPdAppDetails);//请领明细
		map.put("orc", "CR");//撤销
		map.put("exPdDes", exPdDes);//
		PlatFormSendUtils.sendScmIpDeDrug(map);
		return codeRefund;

	}
	/**
	 * 008004002005
	 * 住院静配退药-查询退药请领明细的执行数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryBackDrugPivasInfo(String param,IUser user){
		IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
		User userCur = (User) user;
		ipDeDrugDto.setPkPdStock(userCur.getPkStore());
		ipDeDrugDto.setPkOrg(userCur.getPkOrg());
		List<Map<String,Object>> resList=ipDeDrugMapper.qryBackDrugPivasInfo(ipDeDrugDto);
		for (Map<String, Object> map : resList) {
			double quanPack=Math.ceil(Double.parseDouble(map.get("quanPackIn").toString()))*-1;
			double price=Double.parseDouble(map.get("price").toString());
			double amount=MathUtils.mul(quanPack, price);
			map.put("quanPack", quanPack);
			map.put("amount", amount);
		}
		new ExlistPrintSortByOrdUtil().ordGroup(resList);
		return resList;
	}
	
	/**
	 * 008004002006
	 * 住院静配退药-取消退药
	 * @param param
	 * @param user
	 */
	public void cancelBackPivasDrug(String param,IUser user){
		List<IpPivasBackDrugVo> pivasDrugs=JsonUtil.readValue(param, new TypeReference<List<IpPivasBackDrugVo>>() {});
		if(pivasDrugs==null|| pivasDrugs.size()<=0)return ;
		User us=(User)user;
		Set<String> pkPdaps=new HashSet<String>();
		Set<String> pkPdapdts=new HashSet<String>();
		Set<String> pkPdapHerbs=new HashSet<String>();
		for (IpPivasBackDrugVo pivasVo : pivasDrugs) {
			if("0103".equals(pivasVo.getCodeOrdtype())){
				pkPdapHerbs.add(pivasVo.getPkPdap());
			}else{
				pkPdapdts.add(pivasVo.getPkPdapdt());
			}
			pkPdaps.add(pivasVo.getPkPdap());
		}
		String noteBack = pivasDrugs.get(0).getNoteBack();
		//更新草药请领明细单：因一条草药处方单会有一条执行单，所以退回其中的草药处方中的某一条数据时整条处方都需退药处理
		if(pkPdapHerbs.size()>0){
			updateHerbExApdt(pkPdapHerbs,us,noteBack);
		}
		//非草药数据处理
		if(pkPdapdts.size()>0){
			updateExApDt(pkPdapdts,us,noteBack);
		}
		//更新请领单
		StringBuffer exApSql=new StringBuffer("update ex_pd_apply  set flag_cancel='1' ,eu_status='9', note='药房取消退药' ,ts=?,");
		exApSql.append(" pk_emp_cancel=? ,name_emp_cancel=?, date_cancel=? ,pk_dept_cancel=?");
		exApSql.append(" where pk_pdap not in ( select dt.pk_pdap from ex_pd_apply_detail dt ");
		exApSql.append(" inner join ex_order_occ occ on occ.pk_pdback=dt.pk_pdapdt");
		exApSql.append(" where dt.pk_pdap in (");
		exApSql.append(CommonUtils.convertSetToSqlInPart(pkPdaps, "pk_pdap"));
		exApSql.append(") )and pk_pdap in (");
		exApSql.append(CommonUtils.convertSetToSqlInPart(pkPdaps, "pk_pdap"));
		exApSql.append(")");
		DataBaseHelper.execute(exApSql.toString(),new Object[]{new Date(),us.getPkEmp(),us.getNameEmp(),new Date(),us.getPkDept()});
		
		//4.更新请领单-如果请领单对应的明细即包含已经完成发药，或者退回处理的将请领单置为完成状态
		StringBuffer upPdaps=new StringBuffer("update ex_pd_apply   set ex_pd_apply.flag_finish='1' where ex_pd_apply.pk_pdap in (");
		upPdaps.append(CommonUtils.convertSetToSqlInPart(pkPdaps, "pk_pdap"));
		upPdaps.append(") and exists (select 1 from ex_pd_apply_detail dt where ex_pd_apply.pk_pdap=dt.pk_pdap and dt.flag_finish='1') ");
		upPdaps.append(" and not exists (select 1 from ex_pd_apply_detail dt1  where ex_pd_apply.pk_pdap=dt1.pk_pdap and dt1.flag_finish='0' and (dt1.flag_canc='0' or dt1.flag_canc is null) and dt1.flag_stop='0') ");
		DataBaseHelper.update(upPdaps.toString());
	}
	
	/**
	 * 更新除草药的请领单明细数据以及对应的执行单数据
	 * @param pkPdapdts
	 * @param us
	 */
	private void updateExApDt(Set<String> pkPdapdts,User us,String noteBack){
		StringBuffer pdapSql=new  StringBuffer("update ex_pd_apply_detail set flag_canc='1',eu_result='3'  ");
		pdapSql.append(" ,pk_emp_back=?,name_emp_back=? ,date_back=? ,note='药房退回',note_back =? ");
		pdapSql.append(" where pk_pdapdt in (");
		pdapSql.append(CommonUtils.convertSetToSqlInPart(pkPdapdts, "pk_pdapdt"));
		pdapSql.append(") and flag_de='0' and (flag_canc='0' or flag_canc is null)");
		//更新请领明细
		int counts=DataBaseHelper.update(pdapSql.toString(),new Object[]{us.getPkEmp(),us.getNameEmp(),new Date(),noteBack});
		if(counts!=pkPdapdts.size()){
			throw new BusException("您本次提交的药品明细中已有被其他人完成的记录，请刷新请领单后重新处理！");
		}
		//更新执行单
		StringBuffer upExSql=new StringBuffer("update ex_order_occ ");
		upExSql.append(" set ex_order_occ.pk_pdback=null");
		upExSql.append(" where exists (select 1 from ex_pd_apply_detail dt");
		upExSql.append(" where dt.flag_de='0') and ex_order_occ.pk_pdback in (");
		upExSql.append(CommonUtils.convertSetToSqlInPart(pkPdapdts, "pk_pdapdt"));
		upExSql.append(")");
		DataBaseHelper.update(upExSql.toString());
	}
	
	/**
	 * 更新草药的请领单明细以及对应的执行单数据
	 * @param pkPdapHerbs
	 * @param us
	 */
	private void updateHerbExApdt(Set<String> pkPdapHerbs,User us,String noteBack){
		StringBuffer pdapHerbSql=new StringBuffer("update ex_pd_apply_detail set flag_canc='1',eu_result='3' ");
		pdapHerbSql.append(" ,pk_emp_back=?,name_emp_back=? ,date_back=? ,note='药房退回',note_back=? ");
		pdapHerbSql.append(" where pk_pdap in (");
		pdapHerbSql.append(CommonUtils.convertSetToSqlInPart(pkPdapHerbs, "pk_pdap"));
		pdapHerbSql.append(") and flag_de='0' and (flag_canc='0' or flag_canc is null)");
		DataBaseHelper.update(pdapHerbSql.toString(),new Object[]{us.getPkEmp(),us.getNameEmp(),new Date(),noteBack});
		
		StringBuffer ordHerbSql=new StringBuffer("update ex_order_occ set pk_pdback=null ");
		ordHerbSql.append(" where pk_pdback in (select pk_pdapdt  from ex_pd_apply_detail dt where dt.pk_pdap in (");
		ordHerbSql.append(CommonUtils.convertSetToSqlInPart(pkPdapHerbs, "pk_pdap"));
		ordHerbSql.append("))");
		DataBaseHelper.update(ordHerbSql.toString());
	}
}
