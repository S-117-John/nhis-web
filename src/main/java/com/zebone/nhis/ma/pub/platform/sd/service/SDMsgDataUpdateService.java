package com.zebone.nhis.ma.pub.platform.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.message.SRR_S01;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.bl.pub.vo.BlIpCgVo;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.cn.ipdw.ExOpSch;
import com.zebone.nhis.common.module.ex.nis.ns.*;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDOpMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.entity.MidBlOp;
import com.zebone.nhis.ma.pub.platform.sd.util.Constant;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.RAR_RAR;
import com.zebone.nhis.pi.pub.service.PiPubService;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ????????????????????????????????????????????????
 * @author maijiaxing
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDMsgDataUpdateService {
	//??????????????????
	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private PiPubService piPubService;
	@Autowired
	private SDOpMsgMapper sDOpMsgMapper;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");

	/**
	 * ??????????????????
	 * @param pkCnord
	 * @param codeApply
	 * @param exLabOccList
	 */
    public void saveLisRptList(String pkCnord,String codeApply ,List<ExLabOcc> exLabOccList){
    	if(exLabOccList==null || exLabOccList.size()==0) {
    		log.info("??????????????????????????????????????????"+codeApply);
    		return;
    	}
    	//??????????????????
    	DataBaseHelper.execute("update ex_lab_occ set del_flag='1' where code_apply =? and code_rpt=?", codeApply,exLabOccList.get(0).getCodeRpt() );
    	//???????????????
    	//DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExLabOcc.class), exLabOccList);
    	for (ExLabOcc exLabOcc : exLabOccList) {
    		DataBaseHelper.insertBean(exLabOcc);
		}
		//????????????
		//DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExLabOcc.class),exLabOccList);
    	//??????????????????
    	//DataBaseHelper.update("update cn_lab_apply set eu_status ='4' where pk_cnord in (select pk_cnord from cn_order where cn_order.pk_pv = ? and code_apply in "+codeApplyStr+")",new Object[] { pkPv });
    	int execute = DataBaseHelper.execute("update cn_lab_apply set eu_status ='4' where pk_cnord =? ",pkCnord);
    	if(execute<=0){
    		log.info("???????????????"+codeApply+"??????????????????????????????");
    	}

    }



   /**
    * ?????????????????????
    *
    * @param map
    * @return
 * @throws HL7Exception 
    */
   public String disposeUnpaidInfo(Map<String, Object> map) throws HL7Exception{
	   String codePi = SDMsgUtils.getPropValueStr(map,"paitId");
	   String sql = "select pv.pk_pv,pv.pk_pi,pv.date_begin,pv.pk_org,pv.code_pv,pi.code_pi from pv_encounter pv inner join pi_master pi on pi.pk_pi = pv.pk_pi where pi.del_flag = '0' and pv.EU_STATUS<9 and pi.code_pi =?";
	   List<Map<String, Object>> pvInfoList =  DataBaseHelper.queryForList(sql,new Object[]{codePi});
	   String msg = "";
	   if(pvInfoList.size()!=0){
		   RAR_RAR rar = new RAR_RAR();
		   String msgId = SDMsgUtils.getMsgId();
		   String sendApp = SDMsgUtils.getPropValueStr(map,"sendApp");
		   MSH msh = rar.getMSH();
			MSA msa = rar.getMSA();
			SDMsgUtils.createMSHMsg(msh, msgId, "RAR", "RAR");
			//?????????????????????>???????????????
			msh.getReceivingApplication().getNamespaceID().setValue(sendApp);//@todo
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(SDMsgUtils.getPropValueStr(map,"oldmsgid"));
			msa.getTextMessage().setValue("??????");
			msa.getExpectedSequenceNumber().setValue("100");
			msa.getDelayedAcknowledgmentType().setValue("F");
			Map<String, Object> paramMap =  new HashMap<>();
			paramMap.put("X", "0");
		   for (int i = 0; i < pvInfoList.size(); i++) {
			   Map<String, Object> pvInfoMap = pvInfoList.get(i);
			   pvInfoMap.put("curDate", SDMsgUtils.getPropValueStr(map,"time"));
			   List<BlPatiCgInfoNotSettleVO> blCgNoSettleList = sDOpMsgMapper.queryNoSettleInfoForCg(pvInfoMap);
			   paramMap.put("oldmsgid", SDMsgUtils.getPropValueStr(map,"oldmsgid"));
			   paramMap.put("blCgNoSettleList", blCgNoSettleList);
			   paramMap.put("frequency", i);
			   paramMap.put("codePv", SDMsgUtils.getPropValueStr(pvInfoMap,"codePv"));
			   paramMap.put("codePi", SDMsgUtils.getPropValueStr(pvInfoMap,"codePi"));
			   paramMap.put("sendApp", sendApp);
			   Map<String, Object> creRarMap = createRARInfo(paramMap,rar);
			   paramMap.put("X", creRarMap.get("X"));
			   paramMap.put("sum", rar.getDEFINITION(0).getORDER(i).getORC().getAdvancedBeneficiaryNoticeCode().getIdentifier().getValue());
		   }
		   msg =  SDMsgUtils.getParser().encode(rar);
	   }else{
		   throw new BusException("?????????????????????("+codePi+")????????????????????????????????????");
	   }

	   return msg;
   }


   /**
    * ??????RAR_RAR????????????
    * @param map
    * @return
 * @throws DataTypeException 
    */
  public Map<String, Object> createRARInfo(Map<String, Object> map,RAR_RAR rar) throws DataTypeException{
	  Map<String, Object> creRarMap = new HashMap<String, Object>();
	  creRarMap.put("isRep","isRep");
	  List<BlPatiCgInfoNotSettleVO> blCgNoSettleList  = (List<BlPatiCgInfoNotSettleVO>)map.get("blCgNoSettleList");
		if(blCgNoSettleList.size()==0){
			creRarMap.put("X", map.get("X"));
			creRarMap.put("rar", rar);
	    	return creRarMap;
	    }
		BigDecimal count = BigDecimal.ZERO;
		Map<String, Object> hpMap = DataBaseHelper.queryForMap("select * from bd_hp where pk_hp = (select pk_insu from pv_encounter where code_pv = ? ) and  del_flag = '0'",SDMsgUtils.getPropValueStr(map,"codePv"));
	    //int i = Integer.parseInt(SDMsgUtils.getPropValueStr(map,"frequency"));
	    String sumStr = SDMsgUtils.getPropValueStr(map,"sum");
	    BigDecimal sum = BigDecimal.ZERO;
	    if(!"".equals(sumStr))
	    	sum = sum.add(new  BigDecimal(sumStr));


	    String codeSetType = SDMsgUtils.getPropValueStr(hpMap,"code");
	    String codePvType = "";
	    String codeHpType="";
		if("01".equals(codeSetType)){
			codePvType = "01";
			codeSetType = "1";
			codeHpType="01";
		}else if("A31004".equals(codeSetType) ||"A31005".equals(codeSetType)){
			codeSetType = "3";
			codeHpType="03";
			codePvType = "02";
		}else{
			codeSetType = "2";
			codePvType = "02";
			codeHpType="02";
		}
	    //PID-2    ????????????
		rar.getDEFINITION(0).getPATIENT().getPID().getPatientID().getID().setValue(SDMsgUtils.getPropValueStr(map,"codePi"));
		if("MIH".equals(SDMsgUtils.getPropValueStr(map,"sendApp"))){

			//PV1-18 ????????????
			rar.getDEFINITION(0).getPATIENT().getPV1().getPatientType().setValue(codeHpType);
		}else{
			//PV1-18 ????????????
			rar.getDEFINITION(0).getPATIENT().getPV1().getPatientType().setValue(codePvType);
		}
		//PV1-19 ???????????????
		rar.getDEFINITION(0).getPATIENT().getPV1().getVisitNumber().getID().setValue(SDMsgUtils.getPropValueStr(map,"codePv"));
		//NTE-3 ??????
		rar.getDEFINITION(0).getPATIENT().getNTE(0).getCommentType().getText().setValue("Zfzje");
		//NTE-3 ????????????
		rar.getDEFINITION(0).getPATIENT().getNTE(1).getComment(0).setValue("0");
		//NTE-3 ??????
		rar.getDEFINITION(0).getPATIENT().getNTE(1).getCommentType().getText().setValue("Sbzje");
		String ordsnOne = blCgNoSettleList.get(0).getOrdsn().toString();
		Map<String, Object>  empPhyMap =	new HashMap<>();
		String ghCodeDept = "";
		String ghNameDept = "";
		int x = Integer.valueOf(SDMsgUtils.getPropValueStr(map,"X"));//?????????????????????ORDER??????
		int z = 0;//????????????RXA
		int y = 0;//??????ORDER??????
		for (int j = 0; j < blCgNoSettleList.size(); j++) {
			sum = sum.add(blCgNoSettleList.get(j).getAmount());
			//blCgNoSettleList.get(j).getOrdsn();
			//NTE-3 ????????????
			rar.getDEFINITION(0).getPATIENT().getNTE(0).getComment(0).setValue(sum.toString());

			if(!ordsnOne.equals(blCgNoSettleList.get(j).getOrdsn().toString())){
				count = count.add(blCgNoSettleList.get(j).getAmount());
				//???????????????????????????????????????
				for (int j2 = j; j2 < blCgNoSettleList.size(); j2++) {
					if(j2+1==blCgNoSettleList.size())break;
					if(blCgNoSettleList.get(j2).getOrdsn().toString().equals(blCgNoSettleList.get(j2+1).getOrdsn().toString()))
						count = count.add(blCgNoSettleList.get(j2+1).getAmount());
					else
						break;
				}
				y ++;
				//????????????????????????
				Map<String, Object> hpType = getHpType(blCgNoSettleList.get(j).getOrdsn().toString());
				//ORC-2.1  ????????????
				if(CommonUtils.isNotNull(blCgNoSettleList.get(j).getPresNo())){
					rar.getDEFINITION(0).getORDER(x+y).getORC().getPlacerOrderNumber().getEntityIdentifier().setValue(blCgNoSettleList.get(j).getPresNo());
				}else{
					rar.getDEFINITION(0).getORDER(x+y).getORC().getPlacerOrderNumber().getEntityIdentifier().setValue(CommonUtils.getString(blCgNoSettleList.get(j).getOrdsnParent(),""));
				}
				if("MIH".equals(SDMsgUtils.getPropValueStr(map,"sendApp"))){
					rar.getDEFINITION(0).getORDER(x+y).getORC().getPlacerOrderNumber().getEntityIdentifier().setValue(CommonUtils.getString(blCgNoSettleList.get(j).getOrdsnParent(),""));
				}
				//ORC-2.2  ????????????(???????????????)
				rar.getDEFINITION(0).getORDER(x+y).getORC().getPlacerOrderNumber().getNamespaceID().setValue(blCgNoSettleList.get(j).getOrdsn().toString());
				//ORC-2.3  ???????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getPlacerOrderNumber().getUniversalID().setValue(SDMsgUtils.getPropValueStr(map,"codePv"));
				//ORC-2.4  ????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getPlacerOrderNumber().getUniversalIDType().setValue("O");
				//ORC-3.1 ???????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getFillerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map,"codePv"));
				//ORC-3.2 ???????????????  (???????????????+?????????)
				rar.getDEFINITION(0).getORDER(x+y).getORC().getFillerOrderNumber().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(map,"codePv")+blCgNoSettleList.get(j).getOrdsn().toString());
				if("MIH".equals(SDMsgUtils.getPropValueStr(map,"sendApp"))){

//					ORC-3.3 ??????????????????
					rar.getDEFINITION(0).getORDER(x+y).getORC().getFillerOrderNumber().getUniversalID().setValue(codeSetType);
				}else{
					//????????????????????????????????????
					rar.getDEFINITION(0).getORDER(x+y).getORC().getFillerOrderNumber().getUniversalID().setValue(SDMsgUtils.getPropValueStr(hpType, "dtHpprop"));
				}
				//ORC-10 ??????????????????
				Map<String, Object> emp1 = sDQueryUtils.getUserCodeByPkUser(blCgNoSettleList.get(j).getPkEmpApp().toString());
				rar.getDEFINITION(0).getORDER(x+y).getORC().getEnteredBy(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(emp1,"code"));
				//ORC-10 ??????????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getEnteredBy(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(emp1,"name"));
				//empPhyMap =	DataBaseHelper.queryForMap(" select appv.pk_emp_phy,app.pk_dept_ex,to_char(app.date_reg,'yyyyMMddHH24Miss') date_reg   from 	sch_appt_pv appv left join pv_encounter pv on pv.pk_pv = appv.pk_pv left join sch_appt app on app.pk_schappt = appv.pk_schappt   where pv.code_pv = ? and pv.del_flag = '0'  and appv.del_flag = '0'",SDMsgUtils.getPropValueStr(map,"codePv"));
				empPhyMap = DataBaseHelper.queryForMap("select pv.PK_EMP_PHY,pv.PK_DEPT,to_char(pv.date_reg,'yyyyMMddHH24Miss') date_reg from pv_encounter pv where pv.code_pv = ? and pv.del_flag = '0' ",SDMsgUtils.getPropValueStr(map,"codePv"));
				//ORC-11 ??????????????????
				Map<String, Object> emp2 = sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(empPhyMap, "pkEmpPhy"));
				rar.getDEFINITION(0).getORDER(x+y).getORC().getVerifiedBy(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(emp2,"code"));
				//ORC-11??????????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getVerifiedBy(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(emp2,"name"));
				Map<String, Object> dept1 = sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(empPhyMap, "pkDept"));
				ghCodeDept = SDMsgUtils.getPropValueStr(dept1,"codeDept");
				ghNameDept = SDMsgUtils.getPropValueStr(dept1,"nameDept");
				//ORC-11 ??????????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getVerifiedBy(0).getGivenName().setValue(ghCodeDept);
				//ORC-11??????????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getVerifiedBy(0).getSecondAndFurtherGivenNamesOrInitialsThereof().setValue(ghNameDept);
				//ORC-13??????????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getEntererSLocation().getPointOfCare().setValue(ghCodeDept);
				//ORC-13??????????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getEntererSLocation().getRoom().setValue(ghNameDept);
				//orc-15????????????
				String dateReg = SDMsgUtils.getPropValueStr(empPhyMap,"dateReg");
				rar.getDEFINITION(0).getORDER(x+y).getORC().getOrderEffectiveDateTime().getTimeOfAnEvent().setValue("".equals(dateReg)?sdf.format(new Date()):dateReg);
				//orc -17  ??????/???????????? /????????????/??????????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getOrc17_EnteringOrganization().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(hpType, "orc17"));
				//orc -18 ????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getOrc18_EnteringDevice().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(hpType, "orc18"));
				//orc -19 ????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getOrc19_ActionBy(0).getXcn1_IDNumber().setValue(SDMsgUtils.getPropValueStr(hpType, "orc19"));
				//orc-20 ??????????????????
				rar.getDEFINITION(0).getORDER(x+y).getORC().getAdvancedBeneficiaryNoticeCode().getIdentifier().setValue(count.toString());// ????????????
				z = 0 ;
				count = BigDecimal.ZERO;

				ordsnOne = blCgNoSettleList.get(j).getOrdsn().toString() ;
			}

			if(j==0){
				count = count.add(blCgNoSettleList.get(j).getAmount());
				for (int j2 = j; j2 < blCgNoSettleList.size(); j2++) {
					if(j2+1==blCgNoSettleList.size())break;
					if(blCgNoSettleList.get(j2).getOrdsn().toString().equals(blCgNoSettleList.get(j2+1).getOrdsn().toString()))
						count = count.add(blCgNoSettleList.get(j2+1).getAmount());
					else
						break;
				}
				//????????????????????????
				Map<String, Object> hpType = getHpType(blCgNoSettleList.get(j).getOrdsn().toString());
				//ORC-2.1  ????????????
				if(CommonUtils.isNotNull(blCgNoSettleList.get(j).getPresNo())){
					rar.getDEFINITION(0).getORDER(x+y).getORC().getPlacerOrderNumber().getEntityIdentifier().setValue(blCgNoSettleList.get(j).getPresNo());
				}else{
					rar.getDEFINITION(0).getORDER(x+y).getORC().getPlacerOrderNumber().getEntityIdentifier().setValue(CommonUtils.getString(blCgNoSettleList.get(j).getOrdsnParent(),""));
				}
				if("MIH".equals(SDMsgUtils.getPropValueStr(map,"sendApp"))){
					rar.getDEFINITION(0).getORDER(x+y).getORC().getPlacerOrderNumber().getEntityIdentifier().setValue(CommonUtils.getString(blCgNoSettleList.get(j).getOrdsnParent(),""));
				}
				//ORC-2.2  ????????????(???????????????)
				rar.getDEFINITION(0).getORDER(x+0).getORC().getPlacerOrderNumber().getNamespaceID().setValue(blCgNoSettleList.get(0).getOrdsn().toString());
				//ORC-2.3  ???????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getPlacerOrderNumber().getUniversalID().setValue(SDMsgUtils.getPropValueStr(map,"codePv"));
				//ORC-2.4  ????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getPlacerOrderNumber().getUniversalIDType().setValue("O");
				//ORC-3.1 ???????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getFillerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map,"codePv"));
				//ORC-3.2 ???????????????  (???????????????+?????????)
				rar.getDEFINITION(0).getORDER(x+0).getORC().getFillerOrderNumber().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(map,"codePv")+blCgNoSettleList.get(0).getOrdsn().toString());
//				ORC-3.3 ??????????????????
				if("MIH".equals(SDMsgUtils.getPropValueStr(map,"sendApp"))){
					rar.getDEFINITION(0).getORDER(x+0).getORC().getFillerOrderNumber().getUniversalID().setValue(codeSetType);
				}else{
					//??????????????????
					rar.getDEFINITION(0).getORDER(x+0).getORC().getFillerOrderNumber().getUniversalID().setValue(SDMsgUtils.getPropValueStr(hpType, "dtHpprop"));
				}
				//ORC-10 ??????????????????
				Map<String, Object> emp1 = sDQueryUtils.getUserCodeByPkUser(blCgNoSettleList.get(0).getPkEmpApp().toString());
				rar.getDEFINITION(0).getORDER(x+0).getORC().getEnteredBy(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(emp1,"code"));
				//ORC-10 ??????????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getEnteredBy(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(emp1,"name"));
				//empPhyMap =	DataBaseHelper.queryForMap(" select appv.pk_emp_phy,app.pk_dept_ex,to_char(app.date_reg,'yyyyMMddHH24Miss')   from 	sch_appt_pv appv left join pv_encounter pv on pv.pk_pv = appv.pk_pv left join sch_appt app on app.pk_schappt = appv.pk_schappt   where pv.code_pv = ? and pv.del_flag = '0'  and appv.del_flag = '0'",SDMsgUtils.getPropValueStr(map,"codePv"));
				empPhyMap = DataBaseHelper.queryForMap("select pv.PK_EMP_PHY,pv.PK_DEPT,to_char(pv.date_reg,'yyyyMMddHH24Miss') date_reg from pv_encounter pv where pv.code_pv = ? and pv.del_flag = '0' ",SDMsgUtils.getPropValueStr(map,"codePv"));
				//ORC-11 ??????????????????
				Map<String, Object> emp2 = sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(empPhyMap,"pkEmpPhy"));
				rar.getDEFINITION(0).getORDER(x+0).getORC().getVerifiedBy(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(emp2,"codeEmp"));
				//ORC-11??????????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getVerifiedBy(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(emp2,"nameEmp"));
				Map<String, Object> dept2 = sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(empPhyMap, "pkDept"));
				ghCodeDept = SDMsgUtils.getPropValueStr(dept2,"codeDept");
				ghNameDept = SDMsgUtils.getPropValueStr(dept2,"nameDept");
				//ORC-11 ??????????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getVerifiedBy(0).getGivenName().setValue(ghCodeDept);
				//ORC-11??????????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getVerifiedBy(0).getSecondAndFurtherGivenNamesOrInitialsThereof().setValue(ghNameDept);
				//ORC-13??????????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getEntererSLocation().getPointOfCare().setValue(ghCodeDept);
				//ORC-13??????????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getEntererSLocation().getRoom().setValue(ghNameDept);
				//orc-15????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(empPhyMap,"dateReg"));
				//orc -17  ??????/???????????? /????????????/??????????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getOrc17_EnteringOrganization().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(hpType, "orc17"));
				//orc -18 ????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getOrc18_EnteringDevice().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(hpType, "orc18"));
				//orc -19 ????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getOrc19_ActionBy(0).getXcn1_IDNumber().setValue(SDMsgUtils.getPropValueStr(hpType, "orc19"));
				//orc-20 ??????????????????
				rar.getDEFINITION(0).getORDER(x+0).getORC().getAdvancedBeneficiaryNoticeCode().getIdentifier().setValue(count.toString());
				ordsnOne = blCgNoSettleList.get(j).getOrdsn().toString();
				count = BigDecimal.ZERO;
			}
			//RXA-1  ??????
			rar.getDEFINITION(0).getORDER(x+y).getRXA(z).getGiveSubIDCounter().setValue(String.valueOf(z+1));
			//RXA-2  ??????
			rar.getDEFINITION(0).getORDER(x+y).getRXA(z).getAdministrationSubIDCounter().setValue(blCgNoSettleList.get(j).getPrice().toString());
			//RXA-5  ????????????
			rar.getDEFINITION(0).getORDER(x+y).getRXA(z).getAdministeredCode().getIdentifier().setValue(blCgNoSettleList.get(j).getItemCode().toString());
			//RXA-5  ????????????
			rar.getDEFINITION(0).getORDER(x+y).getRXA(z).getAdministeredCode().getText().setValue(blCgNoSettleList.get(j).getNameCg().toString());
			//RXA-6  ????????????
			rar.getDEFINITION(0).getORDER(x+y).getRXA(z).getAdministeredAmount().setValue(blCgNoSettleList.get(j).getQuanCg().toString());
			//RXA-7  ????????????
			rar.getDEFINITION(0).getORDER(x+y).getRXA(z).getAdministeredUnits().getIdentifier().setValue(blCgNoSettleList.get(j).getUnit().toString());
			//RXA-13  ????????????
			rar.getDEFINITION(0).getORDER(x+y).getRXA(z).getAdministeredStrength().setValue(blCgNoSettleList.get(j).getAmount().toString());
			z++;

		}
		creRarMap.put("X", x+y+1);
		creRarMap.put("rar", rar);
		return creRarMap;
  }

	/**
	 * ????????????????????????????????????????????????
	 * @param ordsn
	 * @return
	 */
	  private Map<String,Object> getHpType(String ordsn){
		  //????????????????????????(??????)
		  //Map<String, Object> hpMap = DataBaseHelper.queryForMap("select code,name from bd_defdoc where CODE_DEFDOCLIST='060107' ");
		  StringBuffer sql = new StringBuffer("select ord.PK_PV,ord.DT_HPPROP,yb.cka303,yb.cka305,cka304,yb.cme320,yb.amc021,yb.cme331 from CN_ORDER ord ")
				  .append(" left join ins_szyb_visit_city yb on ord.PK_PV=yb.PK_PV where ORDSN='").append(ordsn).append("'");
		  Map<String, Object> hpMap = DataBaseHelper.queryForMap(sql.toString());
		  String dtHpprop = SDMsgUtils.getPropValueStr(hpMap, "dtHpprop");
		  switch (dtHpprop){
		  	//01	??????
		  	case "01":break;
		  	//11	????????????
		  	case "11":break;
		    //12	????????????
		  	case "12":break;
		  	//13  ????????????  ?????????????????????(cka303)
		  	case "13":{
		  		hpMap.put("orc17", SDMsgUtils.getPropValueStr(hpMap, "cka303"));
		  		break;
		  	}
		   //14	????????????
		  	case "14":break;
		    //15 ???????????? ???????????????????????????(cka305)  01=????????? 02=?????????
		  	case "15":{
		  		hpMap.put("orc17", SDMsgUtils.getPropValueStr(hpMap, "cka305"));
		  		break;
		  	}
		  	//16  ???????????? ?????????????????????(cka304)
		  	case "16":{
		  		String sqlTj = "select DISTINCT attr.VAL_ATTR from BD_ORD ord inner join CN_ORDER cnord on cnord.pk_ord = ord.pk_ord INNER JOIN bd_dictattr attr ON attr.pk_dict = ord.pk_ord AND attr.del_flag = '0' INNER JOIN bd_dictattr_temp rtemp ON rtemp.pk_dictattrtemp = attr.pk_dictattrtemp AND rtemp.del_flag = '0' WHERE rtemp.code_attr = '0205' and attr.VAL_ATTR is not null AND ORDSN=?";
				List<Map<String, Object>> list = DataBaseHelper.queryForList(sqlTj, new Object[]{ordsn});
				//hpMap.put("orc17", SDMsgUtils.getPropValueStr(hpMap, "cka304"));
				hpMap.put("orc17", list!=null&&!list.isEmpty()?SDMsgUtils.getPropValueStr(list.get(0), "valAttr"):SDMsgUtils.getPropValueStr(hpMap, "cka304"));
		  		break;
		  	}
		    //17	????????????
		  	case "17":break;
		    //18	????????????
		  	case "18":break;
		    //19	????????????
		  	case "19":break;
		    //51	??????????????????????????????  ??????????????? (cme320) ,????????????(amc021)
		  	case "51":{
		  		hpMap.put("orc17", SDMsgUtils.getPropValueStr(hpMap, "cme320"));
		  		hpMap.put("orc18", SDMsgUtils.getPropValueStr(hpMap, "amc021"));
		  		break;
		  	}
		  	//53	????????????(????????????)  ??????????????? (cme320)
		  	case "53":{
		  		hpMap.put("orc17", SDMsgUtils.getPropValueStr(hpMap, "cme320"));
		  		break;
		  	}
		  	//55	????????????(????????????)	??????????????? (cme320) ,????????????(amc021)
		  	case "55":{
		  		hpMap.put("orc17", SDMsgUtils.getPropValueStr(hpMap, "cme320"));
		  		hpMap.put("orc18", SDMsgUtils.getPropValueStr(hpMap, "amc021"));
		  		break;
		  	}
		  	//57	???????????????????????? ??????????????? (cme320) ,????????????(amc021),????????????(cme331)
		  	case "57":{
		  		hpMap.put("orc17", SDMsgUtils.getPropValueStr(hpMap, "cme320"));
		  		hpMap.put("orc18", SDMsgUtils.getPropValueStr(hpMap, "amc021"));
		  		hpMap.put("orc19", SDMsgUtils.getPropValueStr(hpMap, "cme331"));
		  		break;
		  	}
		  	//81	?????????????????? ????????? ???????????????
		  	case "81":break;
		  	default:hpMap.put("dtHpprop", dtHpprop);break;
		  }
		  return hpMap;
	  }


    /**
	 *
	 * ????????????????????????
	 * 0.????????????????????????
	   1.????????????????????????sch_sch???
	   2.??????????????????????????????????????????sch_appt???
	   3.????????????????????? 	?????????sch_ticket
	 * @param regvo
	 * @param appRegParam
	 * @return
	 * @throws
	 * @author  fuhao
	 */
    public void  cancelAppRegistPi(Map<String, Object> appRegParam,PiMasterRegVo regvo){

   		if(CommonUtils.isNull(regvo.getPkSch()))
   			throw new BusException("?????????????????????????????????pkSch???");
   		//??????????????????
   		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?", new Object[] { regvo.getPkSch() });
   		//????????????
   		DataBaseHelper.update("update sch_ticket set FLAG_USED ='0' where ticketno = ? and pk_sch = ?", new Object[] {SDMsgUtils.getPropValueStr(appRegParam,"ticketno") ,regvo.getPkSch()});
   		Map<String,Object> updateMap = new HashMap<String,Object>();
   		updateMap.put("flagCancel", "1");
   		updateMap.put("euStatus", "9");
   		updateMap.put("pkEmpCancel", SDMsgUtils.getPropValueStr(appRegParam, "pkEmp"));
   		updateMap.put("nameEmpCancel", SDMsgUtils.getPropValueStr(appRegParam, "nameEmp"));
   		updateMap.put("dateCancel", new Date());
   		updateMap.put("ts", new Date());
   		updateMap.put("pkPv", regvo.getPkPv());
   		//????????????????????????????????????????????????
   		if(CommonUtils.isNotNull(SDMsgUtils.getPropValueStr(appRegParam,"pkschappt"))){
   			updateMap.put("pkSchappt", SDMsgUtils.getPropValueStr(appRegParam,"pkschappt"));
   			sDOpMsgMapper.updateSchAppt(updateMap);
   		}
   		//??????????????????
   		//regSyxMapper.updatePvEncounter(updateMap);
    }





    /**
     * ????????????????????????
     * 0.?????????????????????(????????????????????????????????????????????????????????????)
     * 1.??????????????????
     * 2.???????????????????????????
     * @param appRegParam
     * @return
     * @throws HL7Exception 
     */
   public String  cancelPiReg(Map<String, Object> appRegParam) throws HL7Exception{

	    String code =SDMsgUtils.getPropValueStr(appRegParam,"code");
	    String sql = " select appt.eu_status,appt.pk_sch,appt.ticket_no,appt.pk_schappt,app.pk_pv,pi.pk_pi,pi.name_pi,pi.pk_org,pv.flag_cancel   from sch_appt_pv app left join sch_appt appt on   appt.pk_schappt = app.pk_schappt left join pi_master pi on pi.pk_pi = appt.pk_pi left join pv_encounter  pv on pv.pk_pv = app.pk_pv"+
	    			 "  where appt.code = ?  and app.del_flag = '0' ";
	    Map<String, Object> pkSchMap =  DataBaseHelper.queryForMap(sql, code);
	    if(pkSchMap==null){
	    	throw new BusException("???????????????????????????"+code+"????????????????????????");
	    }
	    if("9".equals(SDMsgUtils.getPropValueStr(pkSchMap,"euStatus"))){
		    throw new BusException("???????????????????????????!!!");
		}
	    if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(pkSchMap,"pkPv"))&&"0".equals(SDMsgUtils.getPropValueStr(pkSchMap,"flagCancel"))){
		    throw new BusException("????????????????????????????????????");
		}


	    String pkSch = SDMsgUtils.getPropValueStr(pkSchMap,"pkSch");
	    appRegParam.put("pksch", pkSch);
	    appRegParam.put("ticketno", SDMsgUtils.getPropValueStr(pkSchMap,"ticketNo"));
	    appRegParam.put("pkschappt", SDMsgUtils.getPropValueStr(pkSchMap,"pkSchappt"));
		PiMasterRegVo regvo = getPiMasterRegVo(appRegParam);

	    cancelAppRegistPi(appRegParam,regvo);
		//??????????????????
		SRR_S01 srr = new SRR_S01();
		String msgId = SDMsgUtils.getMsgId();
		String msg = "";
		MSH msh = srr.getMSH();
		MSA msa = srr.getMSA();
		SDMsgUtils.createMSHMsg(msh, msgId, "SRR", "S04");
		//Map<String, Object>  priceMap =   DataBaseHelper.queryForMap("select price from sch_resource where pk_schres = ?  and del_flag = '0'",SDMsgUtils.getPropValueStr(appRegParam,"pkSchres"));

		msa.getAcknowledgementCode().setValue("AA");
		msa.getMessageControlID().setValue(SDMsgUtils.getPropValueStr(appRegParam,"oldmsgid"));
		msa.getTextMessage().setValue("??????");
		msa.getExpectedSequenceNumber().setValue("100");//???????
		msa.getDelayedAcknowledgmentType().setValue("F");
		msg =  SDMsgUtils.getParser().encode(srr);
		return msg;

   }




	    /**
	     * ???????????????????????????????????????????????????????????????????????????
	     * 
	     * ??????PiMasterRegVo?????????
	     * @param appRegParam   ????????????????????????pksch, ????????????identifyno??????????????????  patientno
	     * @return
	     */
	   public PiMasterRegVo getPiMasterRegVo(Map<String, Object> appRegParam){
		   PiMasterRegVo piMaster = new PiMasterRegVo();
		   String pkSch =  SDMsgUtils.getPropValueStr(appRegParam,"pksch");

		   SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?", SchSch.class, pkSch);

		   if(schSch==null){
			   throw new BusException("????????????????????????????????????");
		   }
		   Map<String, Object> schTimeMap =  DataBaseHelper.queryForMap("select to_char(to_date(time_begin,'hh24:mi:ss'),'hh24miss') time_begin from bd_code_dateslot  where pk_dateslot = ? ",schSch.getPkDateslot());
		   String dateReg = sdfDay.format(schSch.getDateWork())+SDMsgUtils.getPropValueStr(schTimeMap,"timeBegin");
		   try {
			   piMaster.setDateReg(sdf.parse(dateReg));
		   } catch (ParseException e) {
				e.printStackTrace();
				throw new BusException("???????????????????????????????????????????????????");
		   }


		   piMaster.setPkDept(schSch.getPkDept());
		   Map<String , Object> srvMap =  DataBaseHelper.queryForMap("select pk_srv from  pv_pe where pk_sch = ? and del_flag = '0' ", pkSch);
		   piMaster.setPkSrv(SDMsgUtils.getPropValueStr(srvMap,"pkSrv"));//???????????????????????????
		   piMaster.setPkSchres(schSch.getPkSchres());
		   piMaster.setPkSchsrv(schSch.getPkSchsrv());
		   piMaster.setPkSch(pkSch);
		   piMaster.setPkSchplan(schSch.getPkSchplan());
		   piMaster.setPkDateslot(schSch.getPkDateslot());
		   piMaster.setPkAppt(SDMsgUtils.getPropValueStr(appRegParam, "pkSchappt"));
		   /**
		   String sendApp = SDMsgUtils.getPropValueStr(appRegParam,"sendApp");
		   if("MIH".equals(sendApp)||"YYT".equals(sendApp)){//?????? ?????????
			   piMaster.setEuPvtype("1");//	1 ?????????2 ?????????3 ?????????4 ?????????5 ????????????
		   }else if ("PEIS".equals(sendApp)) {//??????
			   piMaster.setEuPvtype("4");
		   }*/
		   piMaster.setEuPvtype(SDMsgUtils.getPropValueStr(appRegParam, "euPvType"));
		   String cashInsur = SDMsgUtils.getPropValueStr(appRegParam, "cashInsur");
		   piMaster.setAmtInsuThird(BigDecimal.ZERO.add(new BigDecimal("".equals(cashInsur)?"0":cashInsur)));//??????????????????
		   //sch_srv.eu_srvtype
		   Map<String , Object> srvtypeMap =  DataBaseHelper.queryForMap("select eu_srvtype from  sch_srv where pk_schsrv = ? and del_flag = '0'", schSch.getPkSchsrv());
		   piMaster.setEuSrvtype(SDMsgUtils.getPropValueStr(srvtypeMap,"euSrvtype"));
		   piMaster.setEuSchclass(schSch.getEuSchclass());
		   // outsideOrderId;???????????????????????????
		   piMaster.setOutsideOrderId(SDMsgUtils.getPropValueStr(appRegParam,"outsideOrderId"));
		   // orderSource;??????????????????
		   piMaster.setOrderSource(SDMsgUtils.getPropValueStr(appRegParam,"orderSource"));
		   //??????????????????   ????????????????????????????????????

		   String patientno = SDMsgUtils.getPropValueStr(appRegParam,"patientno");
		   String identifyno = SDMsgUtils.getPropValueStr(appRegParam,"identifyno");
		   PiMaster pi = null;
		   if(!CommonUtils.isEmptyString(identifyno)){//?????????????????????????????????
			   pi = DataBaseHelper.queryForBean("Select * from pi_master where  id_no = ?  and del_flag = '0'",PiMaster.class, identifyno);
		   }else if (!CommonUtils.isEmptyString(patientno)) {//????????????????????????????????????
			   pi = DataBaseHelper.queryForBean("Select * from pi_master where  code_pi = ? and del_flag = '0'",PiMaster.class,patientno);
		   }
		   if(pi==null){
			   throw new BusException("???????????????????????????????????????????????????");
		   }
		   // 01 ???????????????(???)  02  ????????????????????????  ...
		   //piMaster.setDtSource("");//????????????
//		   User user = new User();
//		   user.setPkEmp(Constant.PKZZJ);//??????????????????????????????????????????
//		   user.setNameEmp(Constant.NAMEZZJ);
//		   user.setPkOrg(pi.getPkOrg());
//		   user.setPkDept(Constant.PKDEPT);//???????????????
//		   UserContext.setUser(user);
		   piMaster.setNamePi(pi.getNamePi());
		   piMaster.setPkPi(pi.getPkPi());
		   piMaster.setPkOrg(pi.getPkOrg());
		   piMaster.setCodePi(pi.getCodePi());
		   piMaster.setCodeOp(pi.getCodeOp());
		   piMaster.setCodeIp(pi.getCodeIp());
		   piMaster.setMobile(pi.getMobile());
		   piMaster.setAddress(pi.getAddress());
		   piMaster.setDtSex(pi.getDtSex());
		   piMaster.setBirthDate(pi.getBirthDate());
		   piMaster.setTelNo(pi.getTelNo());
		   piMaster.setTelWork(pi.getTelWork());
		   piMaster.setTelRel(pi.getTelRel());
		   piMaster.setPkPicate(pi.getPkPicate());
		   piMaster.setIdNo(pi.getIdNo());
		   piMaster.setDtIdtype(pi.getDtIdtype());
		   piMaster.setInsurNo(pi.getInsurNo());
		   piMaster.setAgePv(ApplicationUtils.getAgeFormat(pi.getBirthDate(),null));
		   return piMaster;
	   }





    /**
     * ????????????????????????
     * @throws HL7Exception 
     */
    public String addPiInfo(PiMaster pi,String oldMsgId,String piInsurTypeId,String piInsurTypeName,String sendApp) throws HL7Exception{

    	PiMasterParam pati = new PiMasterParam();
    	List<PiInsurance> insuranceList = new ArrayList<>();
    	PiInsurance piInsurance = new PiInsurance();
    	String sql = " select pk_hp from bd_hp where  name = ? and del_flag = '0'";
    	if(piInsurTypeName==null||piInsurTypeName =="")
    		piInsurTypeName = "??????";
    	Map<String, Object> hpMap = DataBaseHelper.queryForMap(sql, piInsurTypeName);
    	if("".equals(SDMsgUtils.getPropValueStr(hpMap,"pkHp")))
    		hpMap = DataBaseHelper.queryForMap(sql, "??????");
    	piInsurance.setPkHp(SDMsgUtils.getPropValueStr(hpMap,"pkHp"));
    	insuranceList.add(piInsurance);
    	pati.setInsuranceList(insuranceList);
    	pati.setMaster(pi);
    	PiMaster master = new PiMaster();
    	//20201106?????? ??????????????????????????????????????????
    	String sqlPi = "select * from PI_MASTER pi where (pi.ID_NO = upper(?) or pi.ID_NO = lower(?)) and (pi.name_pi = upper(?) or pi.name_pi = lower(?) or pi.name_pi = ?) order by pi.CODE_PI desc";
		List<PiMaster> piMasterList = DataBaseHelper.queryForList(sqlPi, PiMaster.class, new Object[]{pi.getIdNo(), pi.getIdNo(), pi.getNamePi(), pi.getNamePi(), pi.getNamePi()});
		if(piMasterList!=null && !piMasterList.isEmpty()){
			master.setCodePi(piMasterList.get(0).getCodePi());
    		master.setNamePi(piMasterList.get(0).getNamePi());
    	}else {
			master = piPubService.savePiMasterParamAutoCommint(pati,false);
		}


		SRR_S01 srr = new SRR_S01();
		String msgId = SDMsgUtils.getMsgId();
		String msg = "";
		MSH msh = srr.getMSH();
		MSA msa = srr.getMSA();
		SDMsgUtils.createMSHMsg(msh, msgId, "SRR", "S01");
		msh.getReceivingApplication().getNamespaceID().setValue(sendApp);//@todo
		if(master== null){
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(oldMsgId);
			msa.getTextMessage().setValue("?????????????????????");
			msg =  SDMsgUtils.getParser().encode(srr);
			return msg;
		}

		msa.getAcknowledgementCode().setValue("AA");
		msa.getMessageControlID().setValue(oldMsgId);
		msa.getTextMessage().setValue("??????");
		msa.getExpectedSequenceNumber().setValue("100");//???????
		msa.getDelayedAcknowledgmentType().setValue("F");
		SCH sch = srr.getSCHEDULE().getSCH();
		sch.getFillerStatusCode().getIdentifier().setValue("0");//??????
		NTE nte0 = srr.getSCHEDULE().getNTE(0);
		nte0.getComment(0).setValue("0");//??????????????????
		nte0.getCommentType().getText().setValue("Zfzje");
		NTE nte1 = srr.getSCHEDULE().getNTE(1);
		nte1.getComment(0).setValue("0");//??????????????????
		nte1.getCommentType().getText().setValue("Sbzje");
		PID pid = srr.getSCHEDULE().getPATIENT().getPID();
		pid.getPatientID().getID().setValue(master.getCodePi());//????????????
		pid.getPatientName(0).getFamilyName().getSurname().setValue(master.getNamePi());//????????????
		PV1 pv1 = srr.getSCHEDULE().getPATIENT().getPV1();
		pv1.getPatientClass().setValue("O");//O??????  I??????
		msg =  SDMsgUtils.getParser().encode(srr);

    	return msg;
    }








    /**
     * ????????????????????????
     * @param map
     * @return
     */
    public void updateOpApplyList(Map<String, Object> map){
    	if(map!=null){
    		if(("OC").equals(map.get("control"))){//????????????    ??????????????????     ???????????????????????????
    			DataBaseHelper.update("update cn_op_apply set eu_status ='1',modifier=?,modity_time=sysdate where pk_cnord=? and eu_status !='5' and del_flag = '0' ",map.get("pkemp"),map.get("pkCnord"));
    			DataBaseHelper.update("update ex_op_sch  set eu_status = '9',pk_emp_canc=?,name_emp_canc = ?,date_canc=sysdate where pk_cnord=?  and del_flag = '0' ",map.get("pkemp"),map.get("nameEmp"),map.get("pkCnord"));
			}else if(("OR").equals(map.get("control"))){//??????????????????????????????
				//DataBaseHelper.update("update cn_order set eu_status_ord ='3',pk_emp_ex=?,date_plan_ex=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord=? and code_ordtype =?",map.get("pkemp"),map.get("pkCnord"),map.get("ordtype"));
    			DataBaseHelper.update("update cn_op_apply set eu_status ='3',modifier=?,modity_time=sysdate where pk_cnord=? and eu_status !='5' and del_flag = '0' ",map.get("pkemp"),map.get("pkCnord"));
    		}
    	}
    }


    /**
     * ????????????
     * @param map
     * @return
     * @throws ParseException 
     */
    public void updateLisRptList(Map<String, Object> map) throws ParseException{
    	List<BlPubParamVo> blCgVos = new ArrayList<>();
		if(("CM").equals(map.get("ordcontrol"))){//????????????


		}else if(("F").equals(map.get("ordcontrol"))){//????????????  ??????
			 Map<String, Object> patiInfo = new HashMap<>();
			 String codeApply = (String)map.get("codeApply");
			 if(StringUtils.isNotBlank(codeApply)){
				 String sql ="select * from cn_order where code_apply = ? and code_ordtype like '03%' and del_flag = '0'";
				 patiInfo = DataBaseHelper.queryForMap(sql,codeApply);
			 }
			 String pkCnord = SDMsgUtils.getPropValueStr(patiInfo,"pkCnord");

			//?????????????????????????????????????????????
			String sql = "select * from ex_order_occ ex  where ex.eu_status='0' and ex.pk_cnord = '"+pkCnord+"'";
			Map<String, Object> mapOcc = DataBaseHelper.queryForMap(sql);
			if(mapOcc!=null){
				int update = DataBaseHelper.execute("update ex_order_occ set flag_canc='0',pk_emp_canc= null ,date_canc= null ,eu_status='1',pk_emp_occ=?,name_emp_occ=?,date_occ=to_date(?,'yyyyMMddHH24miss') where pk_cnord=? ",map.get("pkempqry"),map.get("nameemp"),map.get("date"),map.get("pkCnord"));
				int update2 = DataBaseHelper.execute("update cn_lab_apply set eu_status ='2',pk_emp_col=?,name_emp_col = ?,modifier=?,date_col=to_date(?,'yyyyMMddHH24miss') ,modity_time=sysdate where pk_cnord=?",map.get("pkempqry"),map.get("nameemp"),map.get("pkempqry"),map.get("date"),map.get("pkCnord"));
				int execute = DataBaseHelper.execute("update CN_ORDER set DATE_PLAN_EX=to_date(?,'yyyyMMddHH24miss'),PK_EMP_EX=?,NAME_EMP_EX=?,ts=sysdate where pk_cnord=?",map.get("date"),map.get("pkempqry"),map.get("nameemp"),map.get("pkCnord"));
				map.put("pkexocc",SDMsgUtils.getPropValueStr(mapOcc,"pkExocc"));
				map.put("quanocc",SDMsgUtils.getPropValueStr(mapOcc,"quanOcc"));
				map.put("pkempocc",SDMsgUtils.getPropValueStr(mapOcc,"pkEmpOcc"));
				map.put("pkdeptocc",SDMsgUtils.getPropValueStr(mapOcc,"pkDeptOcc"));
				map.put("nameempocc",SDMsgUtils.getPropValueStr(mapOcc,"nameEmpOcc"));
				map.put("creator",SDMsgUtils.getPropValueStr(mapOcc,"creator"));
				saveLisItem(map);
				if(update==0 || update2==0 || execute==0){
					log.info("??????????????????????????????");
					throw new BusException("??????????????????????????????");
				}
			}else {
				log.info("?????????????????????????????????????????????????????????");
				throw new BusException("?????????????????????????????????????????????????????????");
			}
			BlPubParamVo vo = new BlPubParamVo();
			vo.setPkOrg(SDMsgUtils.getPropValueStr(patiInfo,"pkOrg"));
			vo.setPkPi(SDMsgUtils.getPropValueStr(patiInfo,"pkPi"));
			vo.setPkPv(SDMsgUtils.getPropValueStr(patiInfo,"pkPv"));
			vo.setEuPvType(SDMsgUtils.getPropValueStr(patiInfo,"euPvtype"));
			vo.setFlagPd("0");//0????????????
			vo.setPkOrd(SDMsgUtils.getPropValueStr(patiInfo,"pkOrd"));
			vo.setPkCnord(SDMsgUtils.getPropValueStr(patiInfo,"pkCnord"));
			Double num = 0.0;
			if(!"".equals(SDMsgUtils.getPropValueStr(patiInfo,"quan"))){
				num = Double.valueOf(SDMsgUtils.getPropValueStr(patiInfo,"quan"));
			}
			vo.setQuanCg(num);
			vo.setPkOrgApp(SDMsgUtils.getPropValueStr(patiInfo,"pkOrg"));
			vo.setPkDeptApp(SDMsgUtils.getPropValueStr(patiInfo,"pkDept"));
			vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(patiInfo,"pkDeptNs"));
			vo.setPkEmpApp(SDMsgUtils.getPropValueStr(patiInfo,"pkEmpOrd"));
			vo.setNameEmpApp(SDMsgUtils.getPropValueStr(patiInfo,"nameEmpOrd"));
			vo.setPkOrdexdt(mapOcc.get("pkExocc").toString());
			vo.setPkOrgEx(SDMsgUtils.getPropValueStr(patiInfo,"pkOrg"));
			vo.setPkDeptEx(SDMsgUtils.getPropValueStr(patiInfo,"pkDeptExec"));
			vo.setDateHap(new Date());
			String codedeptcg = SDMsgUtils.getPropValueStr(map,"codedeptcg");
			if(!"".equals(codedeptcg)){
				Map<String, Object> pkJobMap = DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ? and del_flag = '0' ",SDMsgUtils.getPropValueStr(map,"codedeptcg"));
				if(pkJobMap!=null)
					vo.setPkDeptCg(SDMsgUtils.getPropValueStr(pkJobMap,"pkDept"));
				else
					vo.setPkDeptCg("");
			}else{
				vo.setPkDeptCg("");
			}
			String pkempqry = SDMsgUtils.getPropValueStr(map,"pkempqry");
			if(!"".equals(pkempqry)){
				vo.setPkEmpCg(SDMsgUtils.getPropValueStr(map,"pkempqry"));
			}else{//?????????????????????????????????MSH-3
				vo.setPkEmpCg(SDMsgUtils.getPropValueStr(map,"send"));
			}
			vo.setNameEmpCg(SDMsgUtils.getPropValueStr(map,"nameemp"));
			//vo.setNameEmpCg(Constant.NOTE);
			vo.setEuBltype("99");//????????????
			blCgVos.add(vo);
			//???????????????
			Map<String,Object> userMap = new HashMap<String,Object>();
			userMap.put("pkOrg", blCgVos.get(0).getPkOrg());
			userMap.put("pkDept", blCgVos.get(0).getPkDeptCg());
			//ORC??????????????????pk
			userMap.put("pkEmp", SDMsgUtils.getPropValueStr(map, "pkempqry"));
			userMap.put("nameEmp", SDMsgUtils.getPropValueStr(map,"nameemp"));
			List<BlPubParamVo> exSonOrder = exSonOrder(SDMsgUtils.getPropValueStr(patiInfo,"ordsn"),userMap);
			blCgVos.addAll(exSonOrder);
			//??????????????????
			List<BlPubParamVo> blSamp = blSamp(SDMsgUtils.getPropValueStr(patiInfo,"ordsn"),userMap);
			blCgVos.addAll(blSamp);
			//??????????????????
			User user = new User();
			user.setPkOrg(blCgVos.get(0).getPkOrg());
			user.setPkEmp(blCgVos.get(0).getPkEmpEx());
			user.setNameEmp(blCgVos.get(0).getNameEmpEx());
			//user.setNameEmp(Constant.NOTE);
			user.setPkDept(blCgVos.get(0).getPkDeptEx());
			UserContext.setUser(user);
			//??????????????????
			BlIpCgVo cgVo = new BlIpCgVo();
			cgVo.setAllowQF(true);
			cgVo.setBlCgPubParamVos(blCgVos);
			ApplicationUtils apputil = new ApplicationUtils();
			ResponseJson  rs =  apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,user);
			if(!CommonUtils.isEmptyString(rs.getErrorMessage()))
			CommonUtils.getString(new RespJson("99|"+rs.getErrorMessage(), false));
		}else if(("A").equals(map.get("ordcontrol"))){//??????
			//DataBaseHelper.execute("update ex_order_occ set flag_canc='0',pk_emp_occ=?,date_occ=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss'),eu_status='1' where pk_cnord=? ",map.get("pkempqry"),map.get("pkCnord"));
			
			DataBaseHelper.execute("update cn_lab_apply set eu_status ='3',modifier=?,modity_time=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord=?",map.get("pkempqry"),map.get("pkCnord"));
			
			String pkDeptCg="";
			if(CommonUtils.isNotNull(SDMsgUtils.getPropValueStr(map, "codedeptcg"))){
				Map<String,Object> deptMap=DataBaseHelper.queryForMap("select pk_dept,name_dept from bd_ou_dept where code_dept=?", new Object[]{SDMsgUtils.getPropValueStr(map, "codedeptcg")});
				pkDeptCg=CommonUtils.getString(deptMap.get("pkDept"),"");
			}
			StringBuffer sql=new StringBuffer();
			sql.append("UPDATE ex_assist_occ SET TS= sysdate,");
			sql.append("FLAG_OCC = '1', DATE_OCC = to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss'), PK_EMP_OCC = ?, NAME_EMP_OCC = ?,");
			sql.append(" EU_STATUS = '1', PK_ORG_OCC = ?, PK_DEPT_OCC = ?");
			sql.append(" WHERE flag_occ = '0' AND flag_canc = '0' AND flag_refund = '0' AND PK_CNORD = ? ");
			DataBaseHelper.execute(sql.toString(), new Object[]{map.get("pkempqry"),map.get("nameemp"),Constant.PKORG,pkDeptCg,map.get("pkCnord")});
		
		}else if(("CA").equals(map.get("ordcontrol"))){//??????        ??????
			//?????????????????????????????????
			if(!"3".equals(SDMsgUtils.getPropValueStr(map, "euPvtype"))){
				StringBuilder logStr = new StringBuilder("???????????????SDMsgDataUpdateService.updateLisRptList ?????????");
				//???????????????????????????
				ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean("select * from EX_ASSIST_OCC where PK_CNORD=?",ExAssistOcc.class, SDMsgUtils.getPropValueStr(map, "pkCnord"));
				if(exAssistOcc!=null){
					exAssistOcc.setDateCanc(sdf.parse(SDMsgUtils.getPropValueStr(map, "date")));
					exAssistOcc.setPkEmpCanc(SDMsgUtils.getPropValueStr(map, "pkempqry"));
					exAssistOcc.setNameEmpCanc(SDMsgUtils.getPropValueStr(map, "nameemp"));
					//exAssistOcc.setPkDeptOcc(SDMsgUtils.getPropValueStr(paramMap, "pkDept"));
					//exAssistOcc.setPkOrgOcc(cnRisApply.getPkOrg());
					exAssistOcc.setEuStatus("0");
					int updateBeanByPk = DataBaseHelper.updateBeanByPk(exAssistOcc);
					logStr.append("?????? ExAssistOcc ???  ").append(updateBeanByPk).append("????????????");
				}
				//?????????????????????
				CnLabApply cnLabApply = DataBaseHelper.queryForBean("select * from cn_lab_apply where PK_CNORD=?",CnLabApply.class, SDMsgUtils.getPropValueStr(map, "pkCnord"));
				if(cnLabApply!=null){
					cnLabApply.setEuStatus("1");
					cnLabApply.setModifier(SDMsgUtils.getPropValueStr(map, "pkempqry"));
					cnLabApply.setModityTime(new Date());
					int updateBeanByPk = DataBaseHelper.updateBeanByPk(cnLabApply);
					logStr.append("?????? cnLabApply ???  ").append(updateBeanByPk).append("?????????!");
				}
				log.info(logStr.toString());
				//???????????????
				return;
			}
			//?????????????????? ??????????????????????????????
			List<RefundVo> reFund = new ArrayList<RefundVo>();//????????????
			Map<String, Object> exDoc = new HashMap<>();
			String pkemp = (String)map.get("pkempqry");
			if(StringUtils.isNotBlank(pkemp)){
				 String sql = "SELECT emp.pk_org,emp.PK_EMP,emp.name_emp,job.pk_dept FROM bd_ou_employee emp left join bd_ou_empjob job on emp.pk_emp = job.pk_emp where emp.PK_EMP =?";
				 exDoc = DataBaseHelper.queryForMap(sql,pkemp);
				 if(exDoc==null){
					 throw new BusException("????????????????????????");
				 }
			}
			//????????????????????????????????????
			//??????????????????
			String sqlBl = "select * from (select blip.pk_cgip,blip.quan + (CASE WHEN back.quan is null THEN 0 ELSE back.quan END) as quan  from BL_IP_DT blip  left outer join  (select sum(quan) quan,sum(amount) amt,pk_cgip_back from bl_ip_dt  where flag_settle = 0 and flag_pd = 0 and  quan < 0  and PK_CNORD = ?  group by pk_cgip_back ) back on blip.pk_cgip=back.pk_cgip_back where blip.del_flag = '0' and blip.pk_cgip_back is null and blip.flag_settle='0' and blip.quan >0 and blip.pk_cnord = ? ) dt where dt.quan>0 ";
			List<Map<String, Object>> BlList = DataBaseHelper.queryForList(sqlBl,new Object[]{map.get("pkCnord"),map.get("pkCnord")});
			Double num = 0.0;
			for (int i = 0; i < BlList.size(); i++) {
				Map<String, Object> blMap = BlList.get(i);
				if(!"".equals(SDMsgUtils.getPropValueStr(blMap,"quan"))){
					num = Double.valueOf(SDMsgUtils.getPropValueStr(blMap,"quan"));
				}
				RefundVo refundVo = new RefundVo();
				refundVo.setPkOrg(SDMsgUtils.getPropValueStr(map,"pkOrg"));
				refundVo.setPkCgip(SDMsgUtils.getPropValueStr(blMap,"pkCgip"));//????????????
				refundVo.setNameEmp(SDMsgUtils.getPropValueStr(exDoc,"nameEmp"));
				//refundVo.setNameEmp(Constant.NOTE);
				refundVo.setPkEmp(SDMsgUtils.getPropValueStr(exDoc,"pkEmp"));
				refundVo.setPkDept(SDMsgUtils.getPropValueStr(exDoc,"pkDept"));
				refundVo.setQuanRe(num);
				reFund.add(refundVo);
			}
			User user = new User();
			user.setPkOrg(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			user.setPkEmp(SDMsgUtils.getPropValueStr(exDoc,"pkEmp"));
			user.setNameEmp(SDMsgUtils.getPropValueStr(exDoc,"nameEmp"));
			user.setPkDept(SDMsgUtils.getPropValueStr(exDoc,"pkDept"));
			UserContext.setUser(user);
			//???????????????
			String ordsns = "'"+SDMsgUtils.getPropValueStr(map, "ordsn")+"'";
			List<RefundVo> cancelSonOrder = cancelSonOrder(ordsns,user);
			reFund.addAll(cancelSonOrder);
			//??????
			if(reFund!=null && reFund.size()>0){
				ApplicationUtils apputil = new ApplicationUtils();
				ResponseJson prePayInfo = apputil.execService("PV", "ipCgPubService", "savePatiRefundInfo",reFund,user);
				if(prePayInfo.getStatus()<0){
					log.info("????????????:"+prePayInfo.getDesc());
					throw new BusException("????????????:"+prePayInfo.getDesc());
				}else{
					//  ????????????  flag_canc='1' ???????????????????????????????????????????????????
					int execute1 = DataBaseHelper.execute("update ex_order_occ set date_occ= null, pk_emp_occ= null, name_emp_occ= null, pk_cg= null,eu_status ='0',pk_emp_canc=?,name_emp_canc=?,date_canc=sysdate where pk_cnord=? ",map.get("pkempqry"),map.get("nameemp"),map.get("pkCnord"));
					int execute2 = DataBaseHelper.execute("update cn_lab_apply set eu_status ='1',modifier=?,modity_time=sysdate where pk_cnord=?",map.get("pkempqry"),map.get("pkCnord"));
					if(execute1==0 || execute2==0){
						log.info("?????????????????????????????????????????????");
						throw new BusException("?????????????????????????????????????????????");
					}
					DataBaseHelper.execute("delete from ex_assist_occ where pk_cnord = '"+ map.get("pkCnord")+"'");
					DataBaseHelper.execute("delete from ex_assist_occ_dt where pk_cnord = '"+ map.get("pkCnord")+"'");
				}
			}else{
				log.info("????????????:??????????????????");
				throw new BusException("????????????:??????????????????");
			}
		}
    }

    /**
     * ??????????????????
     * @param ordsn
     * @param userMap
     * @return
     */
    private List<BlPubParamVo> blSamp(String ordsn, Map<String, Object> userMap) {
    	List<BlPubParamVo> voList = new ArrayList<BlPubParamVo>();
    	//??????????????????????????????????????????????????????
    	 String sql = "select item.*,ap.*,o.* from bd_item_defdoc item "
    	 		+ "inner join bd_defdoc samp on item.code_defdoclist=samp.code_defdoclist and item.code_defdoc=samp.code "
    	 		+ "left join CN_LAB_APPLY ap on ap.DT_SAMPTYPE=samp.CODE "
    	 		+ "left join CN_ORDER o on o.PK_CNORD=ap.PK_CNORD "
    	 		+ "where item.code_defdoclist='030200' and o.DEL_FLAG='0' "
    	 		+ "and item.eu_pvtype='3' and item.del_flag='0' "
    	 		+ "and samp.del_flag='0' and ap.DEL_FLAG='0' and o.ORDSN=?";
		 List<Map<String,Object>> dtlist = DataBaseHelper.queryForList(sql, ordsn);
		 for(Map<String, Object> map : dtlist){
			 String pkPv = SDMsgUtils.getPropValueStr(map, "pkPv");
			 String pkItem = SDMsgUtils.getPropValueStr(map, "pkItem");
			 String dateNow = sdfDay.format(new Date());
			 //???????????????????????????
			 String checkSql = "select PK_CGIP,pk_Item from bl_ip_dt where pk_pv='"+pkPv
					 +"' and pk_item='"+pkItem
					 +"' and to_char(DATE_CG,'yyyyMMdd')='"+dateNow+"'";
			 List<Map<String, Object>> check = DataBaseHelper.queryForList(checkSql);
			 //??????????????????????????????????????????
			 if(check!=null && !check.isEmpty() ){
				 continue;
			 }
			 BlPubParamVo vo = new BlPubParamVo();
			 vo.setPkOrg(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			 vo.setPkPi(SDMsgUtils.getPropValueStr(map,"pkPi"));
			 vo.setPkPv(SDMsgUtils.getPropValueStr(map,"pkPv"));
			 vo.setEuPvType(SDMsgUtils.getPropValueStr(map,"euPvtype"));
			 vo.setPkItem(SDMsgUtils.getPropValueStr(map,"pkItem"));//????????????
			 vo.setFlagPd("0");//0????????????
			 //vo.setPkOrd(SDMsgUtils.getPropValueStr(map,"pkOrd"));
			 vo.setPkCnord(SDMsgUtils.getPropValueStr(map,"pkCnord"));
			 Double num = 0.0;
			 if(!"".equals(SDMsgUtils.getPropValueStr(map,"quan"))){
				 num = Double.valueOf(SDMsgUtils.getPropValueStr(map,"quan"));
			 }
			 vo.setQuanCg(num);
			 vo.setPkOrgApp(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			 vo.setPkDeptApp(SDMsgUtils.getPropValueStr(map,"pkDept"));
			 vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(map,"pkDeptNs"));
			 vo.setPkEmpApp(SDMsgUtils.getPropValueStr(map,"pkEmpOrd"));
			 vo.setNameEmpApp(SDMsgUtils.getPropValueStr(map,"nameEmpOrd"));
			 vo.setPkOrdexdt(SDMsgUtils.getPropValueStr(map,"pkExocc"));
			 vo.setEuBltype("99");//????????????
			 vo.setDateHap(new Date());
			 //???????????????????????? (????????????)
			 vo.setPkOrgEx(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			 vo.setPkDeptEx(SDMsgUtils.getPropValueStr(map,"pkDeptExec"));
			 //vo.setPkDeptEx(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			 vo.setPkEmpEx(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			 vo.setNameEmpEx(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			 //????????????????????????
			 //vo.setPkDeptCg(SDMsgUtils.getPropValueStr(map,"pkDeptExec"));
			 vo.setPkDeptCg(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			 vo.setPkEmpCg(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			 vo.setNameEmpCg(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			 vo.setDateCg(new Date());
			 voList.add(vo);
		 }
		 return voList;
	}


	/**
     * ???????????????
     * @param map
     */

    private void saveLisItem(Map<String,Object> map){
		//??????????????????EX_ASSIST_OCC   EX_ASSIST_OCC_DT    ????????????????????????????????????
		ExAssistOcc exAssistOcc = new ExAssistOcc();
		String pkAssocc = SDMsgUtils.getPk();
		exAssistOcc.setPkAssocc(pkAssocc);
		exAssistOcc.setPkOrg(SDMsgUtils.getPropValueStr(map, "pkOrg"));
		exAssistOcc.setPkCnord(SDMsgUtils.getPropValueStr(map, "pkCnord"));
		exAssistOcc.setPkPv(SDMsgUtils.getPropValueStr(map, "pkPv"));
		exAssistOcc.setPkPi(SDMsgUtils.getPropValueStr(map, "pkPi"));
		exAssistOcc.setEuPvtype(SDMsgUtils.getPropValueStr(map, "euPvtype"));
		exAssistOcc.setCodeOcc(ApplicationUtils.getCode("0503"));
		exAssistOcc.setPkDept(SDMsgUtils.getPropValueStr(map, "pkDept"));
		exAssistOcc.setPkEmpOrd(SDMsgUtils.getPropValueStr(map, "pkEmpOrd"));
		exAssistOcc.setNameEmpOrd(SDMsgUtils.getPropValueStr(map, "nameEmpOrd"));
		exAssistOcc.setDateOrd(SDMsgUtils.getPropValueDate(map, "dateSign"));//????????????
		//exAssistOcc.setPkEmpOcc(SDMsgUtils.getPropValueStr(map, "pkEmpOcc"));//?????????
		exAssistOcc.setQuanOcc(Double.valueOf(SDMsgUtils.getPropValueStr(map, "quanocc")));
		exAssistOcc.setTimesOcc(1);//??????????????????
		//exAssistOcc.setTimesTotal(timesTotal);//???????????????
		exAssistOcc.setPkOrgOcc(SDMsgUtils.getPropValueStr(map, "pkOrg"));
		exAssistOcc.setPkDeptOcc(SDMsgUtils.getPropValueStr(map, "pkdeptocc"));
		exAssistOcc.setFlagOcc("0");
		//exAssistOcc.setDateOcc(new Date());
		exAssistOcc.setPkEmpOcc(SDMsgUtils.getPropValueStr(map, "pkempocc"));
		exAssistOcc.setNameEmpOcc(SDMsgUtils.getPropValueStr(map, "nameempocc"));
		exAssistOcc.setFlagCanc("0");
		exAssistOcc.setPkExocc(SDMsgUtils.getPropValueStr(map, "pkexocc"));
		exAssistOcc.setInfantNo(SDMsgUtils.getPropValueStr(map, "infantNo"));//????????????
		exAssistOcc.setEuStatus("0");
		exAssistOcc.setFlagPrt("0");
		exAssistOcc.setCreator(SDMsgUtils.getPropValueStr(map, "creator"));
		exAssistOcc.setCreateTime(new Date());
		exAssistOcc.setTs(new Date());
		exAssistOcc.setDelFlag("0");
		exAssistOcc.setFlagRefund("0");
		int insertBean = DataBaseHelper.insertBean(exAssistOcc);
		ExAssistOccDt exAssistOccDt = new ExAssistOccDt();
		exAssistOccDt.setPkAssocc(pkAssocc);
		exAssistOccDt.setPkAssoccdt(SDMsgUtils.getPk());
		exAssistOccDt.setPkOrg(SDMsgUtils.getPropValueStr(map, "pkOrg"));
		exAssistOccDt.setFlagMaj("1");
		exAssistOccDt.setPkCnord(SDMsgUtils.getPropValueStr(map, "pkCnord"));
		exAssistOccDt.setPkExocc(SDMsgUtils.getPropValueStr(map, "pkexocc"));
		exAssistOccDt.setDelFlag("0");
		exAssistOccDt.setCreateTime(new Date());
		exAssistOccDt.setTs(new Date());
		exAssistOccDt.setPkOrd(SDMsgUtils.getPropValueStr(map, "pkOrd"));
		int insertBeanDt = DataBaseHelper.insertBean(exAssistOccDt);
		if(insertBeanDt<1||insertBean<1){
			throw new BusException("???????????????????????????");
		}

    }

    /**
     * ????????????
     * @param map
     * @return
     */
    public void updateRisRptList(Map<String, Object> map){
    	List<Map<String,Object>> list = (List<Map<String, Object>>) map.get("dtlist");
    	if(map!=null){
    		if(("02").equals(map.get("riseustu"))){//?????????????????????????????????????????????
				DataBaseHelper.update("update ex_assist_occ set flag_canc='1',flag_occ='0',pk_emp_canc=:pkemp,date_canc=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss'),eu_status='9' where pk_cnord=:pkCnord ",map);
    			DataBaseHelper.update("update cn_ris_apply set eu_status ='1',modifier=:pkemp,modity_time=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord=:pkCnord",map);//
			}else{//?????????????????????????????????????????????
				//DataBaseHelper.update("update ex_assist_occ set flag_canc='0',flag_occ='1',pk_emp_occ=:pkemp,date_occ=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss'),eu_status='1' where pk_cnord=:pkCnord ",map);
    			//DataBaseHelper.update("update cn_ris_apply set eu_status ='3',modifier=:pkemp,modity_time=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord=:pkCnord",map);
    			//????????????
    			//DataBaseHelper.update("update cn_order set eu_status_ord='3',date_last_ex=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord=:pkCnord",map);
				for(Map<String,Object> m : list){
					DataBaseHelper.update("update ex_order_occ o set o.eu_status ='1' where pk_cnord=:pkCnord",m);
				}
			}
    	}
    }

	/**
	 * ?????????????????????????????????????????????
	 * @param codeApply
	 * @param list
	 */
    public void saveRisRptList(String codeApply,List<ExRisOcc> list){
    	if(list==null||list.size()==0) return;
    	String [] str = codeApply.split("#");
		//???????????????????????? ????????????????????????
		for (int j = 0; j < str.length; j++) {
			String[] qry = str[j].split("H");
			DataBaseHelper.execute("update ex_ris_occ set del_flag = '1' where pk_cnord = (select ord.pk_cnord from cn_order ord  where ord.del_flag = '0' and  ord.code_apply = ?  and  ord.ordsn = ?)", new Object[]{qry[0],qry[1]});
			DataBaseHelper.execute("update cn_ris_apply set eu_status ='4' where pk_cnord = (select ord.pk_cnord from cn_order ord  where ord.del_flag = '0' and  ord.code_apply = ?  and  ord.ordsn = ?)", new Object[]{qry[0],qry[1]});
		}
		//???????????????
		//DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExRisOcc.class), list);
		for (ExRisOcc exRisOcc : list) {
			DataBaseHelper.insertBean(exRisOcc);
		}
		
		//???????????????????????????
		ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean("select * from EX_ASSIST_OCC where PK_CNORD=?",ExAssistOcc.class, list.get(0).getPkCnord());
		//???????????????????????????
		if(exAssistOcc!=null){
			exAssistOcc.setDateOcc(list.get(0).getDateOcc());
			exAssistOcc.setPkOrgOcc(list.get(0).getPkOrg());
			exAssistOcc.setPkEmpOcc(list.get(0).getPkEmpOcc());
			exAssistOcc.setNameEmpOcc(list.get(0).getNameEmpOcc());
			exAssistOcc.setPkDeptOcc(list.get(0).getPkDeptOcc());
			exAssistOcc.setEuStatus("1");
			DataBaseHelper.updateBeanByPk(exAssistOcc);
		}
    }

	/**
	 * ???????????????????????????????????????????????????
	 * @param exOpSch
	 * @param orderMap
	 */
    public void updateCnOpApplyList(ExOpSch exOpSch,Map<String, Object> orderMap){
    	//????????????????????????
    	DataBaseHelper.update("update cn_op_apply set eu_status ='2' where pk_cnord in (select pk_cnord from cn_order ord where ord.code_ordtype like '04%' and ord.code_apply =:appId  ) and eu_status !='5' ",orderMap);
    	//????????????????????????
    	DataBaseHelper.execute("delete from ex_op_sch where pk_cnord in (select pk_cnord from cn_order ord where ord.code_ordtype like '04%' and ord.code_apply = '"+orderMap.get("appId")+"') ");
    	DataBaseHelper.insertBean(exOpSch);
    }

	/**
	 * ?????????????????????
	 * @param cnRisApply
	 */
	public void updateRisApply(CnRisApply cnRisApply,ExAssistOcc exAssistOcc){
		//?????? CnRisApply ???????????????
		int updateBeanByPk = DataBaseHelper.updateBeanByPk(cnRisApply);
		//??????????????????
		StringBuilder logStr = new StringBuilder("???????????????").append(cnRisApply.getCodeApply()).append("??? ????????????/?????????????????????SDMsgDataUpdateService.updateRisApply ???????????????CnRisApply???").append(updateBeanByPk).append("????????????");
		if(exAssistOcc!=null){
			int updateBeanByPk2 = DataBaseHelper.updateBeanByPk(exAssistOcc);
			logStr.append("??????ExAssistOcc???").append(updateBeanByPk2).append("?????????");
		}
		if(updateBeanByPk<=0 ){
			throw new BusException("????????????????????????????????????");
		}
		log.info(logStr.toString());
	}

	/**
	 * ??????????????????????????????
	 * @param userMap  (pkEmp codeEmp nameEmp pkDept nameDept codeDept pkOrg)
	 * @param paramMap
	 */
	public void updateRisEx(Map<String,Object> paramMap, Map<String, Object> userMap) {
		List<BlPubParamVo> blCgVos = new ArrayList<>();
		//???????????????
		List<Map<String, Object>> dtlist = (List<Map<String, Object>>) paramMap.get("dtlist");
		//??????????????????
		String pkOrg = "".equals(SDMsgUtils.getPropValueStr(dtlist.get(0),"pkOrg"))?SDMsgUtils.getPropValueStr(userMap,"sysName"):SDMsgUtils.getPropValueStr(dtlist.get(0),"pkOrg");
		//?????????????????????????????????
		String ordsn = "";
		//String pkDept = "".equals(SDMsgUtils.getPropValueStr(userMap,"pkDept"))?SDMsgUtils.getPropValueStr(userMap,"sysName"):SDMsgUtils.getPropValueStr(userMap,"pkDept");
		//String nameEmp = "".equals(SDMsgUtils.getPropValueStr(userMap,"nameEmp"))?SDMsgUtils.getPropValueStr(userMap,"sysName"):SDMsgUtils.getPropValueStr(userMap,"nameEmp");
		//String pkEmp = "".equals(SDMsgUtils.getPropValueStr(userMap,"pkEmp"))?SDMsgUtils.getPropValueStr(userMap,"sysName"):SDMsgUtils.getPropValueStr(userMap,"pkEmp");
		User user =  new User();
		for(Map<String, Object> map : dtlist){
			ordsn = SDMsgUtils.getPropValueStr(map,"ordsn");
			//???????????????????????????????????????
			//String sql = "select sum(d.amount) momey,sum(quan) quan from bl_ip_dt d where d.pk_cnord=?";
			//Map<String, Object> count = DataBaseHelper.queryForMap(sql, SDMsgUtils.getPropValueStr(map,"pkCnord"));
			BlPubParamVo vo = new BlPubParamVo();
			vo.setPkOrg(pkOrg);
			vo.setPkPi(SDMsgUtils.getPropValueStr(map,"pkPi"));
			vo.setPkPv(SDMsgUtils.getPropValueStr(map,"pkPv"));
			vo.setEuPvType(SDMsgUtils.getPropValueStr(map,"euPvtype"));
			//vo.setPkItem(SDMsgUtils.getPropValueStr(map,"pkItem"));//????????????
			vo.setFlagPd("0");//0????????????
			vo.setPkOrd(SDMsgUtils.getPropValueStr(map,"pkOrd"));
			vo.setPkCnord(SDMsgUtils.getPropValueStr(map,"pkCnord"));
			Double num = 0.0;
			if(!"".equals(SDMsgUtils.getPropValueStr(map,"quan"))){
				num = Double.valueOf(SDMsgUtils.getPropValueStr(map,"quan"));
			}
			vo.setQuanCg(num);
			vo.setPkOrgApp(pkOrg);
			vo.setPkDeptApp(SDMsgUtils.getPropValueStr(map,"pkDept"));
			vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(map,"pkDeptNs"));
			vo.setPkEmpApp(SDMsgUtils.getPropValueStr(map,"pkEmpOrd"));
			vo.setNameEmpApp(SDMsgUtils.getPropValueStr(map,"nameEmpOrd"));
			vo.setPkOrdexdt(SDMsgUtils.getPropValueStr(map,"pkExocc"));
			vo.setEuBltype("99");//????????????
			vo.setDateHap(new Date());
			//???????????????????????? (????????????)
			vo.setPkOrgEx(pkOrg);
			vo.setPkDeptEx(SDMsgUtils.getPropValueStr(map,"pkDeptExec"));
			//vo.setPkDeptEx(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			vo.setPkEmpEx(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			vo.setNameEmpEx(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			//????????????????????????
			//vo.setPkDeptCg(SDMsgUtils.getPropValueStr(map,"pkDeptExec"));
			vo.setPkDeptCg(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			vo.setPkEmpCg(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			vo.setNameEmpCg("??????????????????");
			vo.setDateCg(new Date());
			blCgVos.add(vo);
			//??????????????????
			user.setPkOrg(pkOrg);
			//u.setPkDept(SDMsgUtils.getPropValueStr(map,"pkDeptExec"));
			user.setPkDept(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			user.setPkEmp(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			user.setNameEmp(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			user.setCodeEmp(SDMsgUtils.getPropValueStr(userMap,"codeEmp"));
			UserContext.setUser(user);
		}
		//?????????????????????
		List<BlPubParamVo> exSonOrder = exSonOrder(ordsn,userMap);
		blCgVos.addAll(exSonOrder);
		//??????????????????
		BlIpCgVo cgVo = new BlIpCgVo();
		cgVo.setAllowQF(true);
		cgVo.setBlCgPubParamVos(blCgVos);
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson  rs =  apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,user);
		if(!CommonUtils.isEmptyString(rs.getErrorMessage())) {
			String string = CommonUtils.getString(new RespJson("99|"+rs.getErrorMessage(), false));
			log.info("?????????????????????????????????????????????/r/n"+string);
		}
		//??????????????????
		updateRisCharge(paramMap,userMap);
	}

	/**
	 * ????????????????????????
	 * @param paramMap
	 * @param userMap  (pkEmp codeEmp nameEmp pkDept nameDept codeDept pkOrg)
	 */
	private void updateRisCharge(Map<String, Object> paramMap, Map<String, Object> userMap) {
		List<Map<String,Object>> list = (List<Map<String, Object>>) paramMap.get("dtlist");
		for(Map<String,Object> map : list){
			//?????????????????????
			int update = DataBaseHelper.update("update ex_order_occ  set eu_status ='1', pk_emp_occ=?,name_emp_occ=?,date_occ=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord=?",userMap.get("pkEmp"),userMap.get("nameEmp"),map.get("pkCnord"));
			int update2 = DataBaseHelper.update("update cn_ris_apply set eu_status ='3',modity_time=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord=:pkCnord",map);
			//??????????????????EX_ASSIST_OCC   EX_ASSIST_OCC_DT
			ExAssistOcc exAssistOcc = new ExAssistOcc();
			String pkAssocc = SDMsgUtils.getPk();
			exAssistOcc.setPkAssocc(pkAssocc);
			exAssistOcc.setPkOrg(SDMsgUtils.getPropValueStr(map, "pkOrg"));
			exAssistOcc.setPkCnord(SDMsgUtils.getPropValueStr(map, "pkCnord"));
			exAssistOcc.setPkPv(SDMsgUtils.getPropValueStr(map, "pkPv"));
			exAssistOcc.setPkPi(SDMsgUtils.getPropValueStr(map, "pkPi"));
			exAssistOcc.setEuPvtype(SDMsgUtils.getPropValueStr(map, "euPvtype"));
			exAssistOcc.setPkDept(SDMsgUtils.getPropValueStr(map, "pkDept"));
			exAssistOcc.setPkEmpOrd(SDMsgUtils.getPropValueStr(map, "pkEmpOrd"));
			exAssistOcc.setNameEmpOrd(SDMsgUtils.getPropValueStr(map, "nameEmpOrd"));
			exAssistOcc.setDateOrd(SDMsgUtils.getPropValueDate(map, "dateSign"));//????????????
			exAssistOcc.setQuanOcc(Double.valueOf(SDMsgUtils.getPropValueStr(map, "quanOcc")));
			exAssistOcc.setTimesOcc(1);//??????????????????
			//exAssistOcc.setTimesTotal(timesTotal);//???????????????
			exAssistOcc.setPkOrgOcc(SDMsgUtils.getPropValueStr(userMap, "pkOrg"));
			exAssistOcc.setPkDeptOcc(SDMsgUtils.getPropValueStr(userMap, "pkDept"));
			exAssistOcc.setPkEmpOcc(SDMsgUtils.getPropValueStr(userMap, "pkEmp"));//?????????
			exAssistOcc.setNameEmpOcc(SDMsgUtils.getPropValueStr(userMap, "nameEmp"));
			exAssistOcc.setCodeOcc(SDMsgUtils.getPropValueStr(userMap, "codeEmp"));
			exAssistOcc.setDateOcc(new Date());
			exAssistOcc.setFlagOcc("1");
			exAssistOcc.setFlagCanc("0");
			exAssistOcc.setEuStatus("1");
			exAssistOcc.setFlagPrt("0");
			exAssistOcc.setDelFlag("0");
			exAssistOcc.setCreateTime(new Date());
			exAssistOcc.setTs(new Date());
			exAssistOcc.setPkExocc(SDMsgUtils.getPropValueStr(map, "pkExocc"));
			exAssistOcc.setInfantNo(SDMsgUtils.getPropValueStr(map, "infantNo"));//????????????
			exAssistOcc.setCreator(SDMsgUtils.getPropValueStr(map, "creator"));
			exAssistOcc.setFlagRefund(SDMsgUtils.getPropValueStr(map, "flagRefund"));
			int insertBean = DataBaseHelper.insertBean(exAssistOcc);
			ExAssistOccDt exAssistOccDt = new ExAssistOccDt();
			exAssistOccDt.setPkAssocc(pkAssocc);
			exAssistOccDt.setPkAssoccdt(SDMsgUtils.getPk());
			exAssistOccDt.setPkOrg(SDMsgUtils.getPropValueStr(userMap, "pkOrg"));
			exAssistOccDt.setPkCnord(SDMsgUtils.getPropValueStr(map, "pkCnord"));
			exAssistOccDt.setPkExocc(SDMsgUtils.getPropValueStr(map, "pkExocc"));
			exAssistOccDt.setPkOrd(SDMsgUtils.getPropValueStr(map, "pkOrd"));
			exAssistOccDt.setFlagMaj("1");
			exAssistOccDt.setDelFlag("0");
			exAssistOccDt.setCreateTime(new Date());
			exAssistOccDt.setTs(new Date());
			int insertBean2 = DataBaseHelper.insertBean(exAssistOccDt);
			if(update>0 && update2>0 && insertBean>0 && insertBean2>0){
				//????????????
			}else{
				//???????????????
				log.info("?????????????????????????????????????????????/r/n pkCnord:"+SDMsgUtils.getPropValueStr(map, "pkCnord"));
				throw new BusException("??????????????????????????????");
			}
		}
	}

	/**
	 * ????????????????????????????????????
	 * @param orderMap
	 * @param userMap
	 */
	public void updateRisRefund(Map<String, Object> orderMap, Map<String, Object> userMap) {
		//?????????????????????
		//String sqlEmp = "SELECT emp.pk_org, emp.PK_EMP,emp.name_emp,job.pk_dept FROM bd_ou_employee emp left join bd_ou_empjob job on emp.pk_emp = job.pk_emp where emp.PK_EMP =?";
		//Map<String, Object> ExEmpMap = DataBaseHelper.queryForMap(sqlEmp,SDMsgUtils.getPropValueStr(orderMap, "pkEmpEx"));
		//String pkEmp = SDMsgUtils.getPropValueStr(ExEmpMap,"pkEmp");
		String pkCnords = SDMsgUtils.getPropValueStr(orderMap, "pkCnords");
		List<Map<String, Object>> BlList = (List<Map<String, Object>>) orderMap.get("BlList");
		if(BlList == null || BlList.size()<=0){
			log.info("?????????????????????????????????");
			throw new BusException("?????????????????????????????????");
		}
		//??????????????????
		String pkOrg = "".equals(SDMsgUtils.getPropValueStr(BlList.get(0),"pkOrg"))?SDMsgUtils.getPropValueStr(userMap,"sysName"):SDMsgUtils.getPropValueStr(BlList.get(0),"pkOrg");
		//String pkDept = "".equals(SDMsgUtils.getPropValueStr(userMap,"pkDept"))?SDMsgUtils.getPropValueStr(userMap,"sysName"):SDMsgUtils.getPropValueStr(userMap,"pkDept");
		//String nameEmp = "".equals(SDMsgUtils.getPropValueStr(userMap,"pkDept"))?SDMsgUtils.getPropValueStr(userMap,"sysName"):SDMsgUtils.getPropValueStr(userMap,"nameEmp");
		//String pkEmp = "".equals(SDMsgUtils.getPropValueStr(userMap,"pkEmp"))?SDMsgUtils.getPropValueStr(userMap,"sysName"):SDMsgUtils.getPropValueStr(userMap,"pkEmp");
		//????????????
		List<RefundVo> refundList = new ArrayList<RefundVo>();
		int size = BlList.size();
		for(int i=0;i<size;i++){
			String pkCgip = SDMsgUtils.getPropValueStr(BlList.get(i),"pkCgip");//????????????
			//????????????
			Double quan = 0.0;
			if(!"".equals(SDMsgUtils.getPropValueStr(BlList.get(i),"quan"))){
				quan = Double.valueOf(SDMsgUtils.getPropValueStr(BlList.get(i),"quan"));
			}else{
				continue;
			}
			//??????????????????
			RefundVo refundVo = new RefundVo();
			refundVo.setPkCgip(pkCgip);//????????????
			//?????????
			refundVo.setPkOrg(pkOrg);
			refundVo.setPkDept(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			refundVo.setNameEmp(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			refundVo.setPkEmp(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			refundVo.setQuanRe(quan);
			refundList.add(refundVo);
		}
		//??????????????????
		User user =  new User();
		user.setPkOrg(pkOrg);
		user.setPkEmp(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
		user.setNameEmp(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
		user.setPkDept(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
		UserContext.setUser(user);
		//???????????????
		List<RefundVo> cancelSonOrder = cancelSonOrder(SDMsgUtils.getPropValueStr(orderMap, "ordsns"),user);
		refundList.addAll(cancelSonOrder);
		//??????
		if(refundList!=null && refundList.size()>0){
			ApplicationUtils apputil = new ApplicationUtils();
			ResponseJson execService = apputil.execService("PV", "ipCgPubService", "savePatiRefundInfo",refundList,user);
			if(execService.getStatus()<0){
				log.info("??????????????????????????????"+execService.getErrorMessage()+"\r\n"+SDMsgUtils.getPropValueStr(orderMap, "msg"));
				throw new BusException("????????????:"+execService.getDesc());
			}else {
				String sql = "update ex_order_occ set eu_status ='0',name_emp_canc='"+SDMsgUtils.getPropValueStr(userMap,"nameEmp")+"',pk_emp_canc='"+SDMsgUtils.getPropValueStr(userMap,"pkEmp")+"',date_canc=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord in ("+pkCnords+")";
				int execute = DataBaseHelper.execute(sql);
				int execute2 = DataBaseHelper.execute("update cn_ris_apply set eu_status ='1',modifier='"+SDMsgUtils.getPropValueStr(userMap,"pkEmp")+"',modity_time=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord in ("+pkCnords+")");
				int execute3 = DataBaseHelper.execute("update bl_ip_dt set del_flag ='1' where pk_cnord in ("+pkCnords+")");
				//????????????????????????
				int execute4 = DataBaseHelper.execute("delete from EX_ASSIST_OCC where pk_cnord in ("+pkCnords+")");
				int execute5 = DataBaseHelper.execute("delete from EX_ASSIST_OCC_DT where pk_cnord in ("+pkCnords+")");
				if(execute>0 && execute2>0 && execute3>0 && execute4>0 && execute5>0){
					//????????????
				}else{
					log.info("???????????????"+execService.getErrorMessage()+"\r\n"+SDMsgUtils.getPropValueStr(orderMap, "msg"));
					throw new BusException("?????????????????????????????????????????????");
				}
			}
		}
	}

	/**
	 * ??????????????????
	 * @param exLabOccBactList
	 * @param exLabOccBactAlList
	 */
	public void saveBactRptList(String codeApply,List<ExLabOccBact> exLabOccBactList, List<ExLabOccBactAl> exLabOccBactAlList) {
		//??????????????????
		String sql = "update EX_LAB_OCC_BACT set DEL_FLAG='1' where PK_LABOCC in (select EX_LAB_OCC.PK_LABOCC from EX_LAB_OCC where CODE_APPLY=?)";
		DataBaseHelper.execute(sql,codeApply);
		
		//???????????????
		//DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExLabOccBact.class), exLabOccBactList);
		for (ExLabOccBact exLabOccBact : exLabOccBactList) {
    		DataBaseHelper.insertBean(exLabOccBact);
		}

	}

	/**
	 * ????????????????????????????????????
	 * @param blMap
	 * @param userMap
	 * @throws ParseException
	 */
	public void saveReportBl(Map<String,Object> blMap,Map<String,Object> userMap) throws ParseException {
		List<BlPubParamVo> blCgVos = new ArrayList<>();
		BlPubParamVo vo = new BlPubParamVo();
		vo.setPkOrg(SDMsgUtils.getPropValueStr(blMap,"pkOrg"));
		vo.setPkPi(SDMsgUtils.getPropValueStr(blMap,"pkPi"));
		vo.setPkPv(SDMsgUtils.getPropValueStr(blMap,"pkPv"));
		vo.setEuPvType(SDMsgUtils.getPropValueStr(blMap,"euPvtype"));
		vo.setFlagPd("0");//0????????????
		vo.setPkOrd(SDMsgUtils.getPropValueStr(blMap,"pkOrd"));
		vo.setPkCnord(SDMsgUtils.getPropValueStr(blMap,"pkCnord"));
		Double num = 0.0;
		if(!"".equals(SDMsgUtils.getPropValueStr(blMap,"quan"))){
			num = Double.valueOf(SDMsgUtils.getPropValueStr(blMap,"quan"));
		}
		vo.setQuanCg(num);
		vo.setPkOrgApp(SDMsgUtils.getPropValueStr(blMap,"pkOrg"));
		vo.setPkDeptApp(SDMsgUtils.getPropValueStr(blMap,"pkDept"));
		vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(blMap,"pkDeptNs"));
		vo.setPkEmpApp(SDMsgUtils.getPropValueStr(blMap,"pkEmpOrd"));
		vo.setNameEmpApp(SDMsgUtils.getPropValueStr(blMap,"nameEmpOrd"));
		vo.setPkOrdexdt(SDMsgUtils.getPropValueStr(blMap,"pkExocc"));
		vo.setPkOrgEx(SDMsgUtils.getPropValueStr(blMap,"pkOrg"));
		vo.setPkDeptEx(SDMsgUtils.getPropValueStr(blMap,"pkDeptExec"));
		vo.setDateHap(new Date());
		vo.setPkDeptCg(SDMsgUtils.getPropValueStr(blMap,"pkDeptNs"));//????????????
		vo.setPkEmpCg(SDMsgUtils.getPropValueStr(blMap,"pkEmpEx"));//?????????
		vo.setNameEmpCg(SDMsgUtils.getPropValueStr(blMap,"nameEmpEx"));
		vo.setDateCg(sdf.parse(SDMsgUtils.getPropValueStr(userMap,"date")));
		vo.setDateStart(sdf.parse(SDMsgUtils.getPropValueStr(userMap,"date")));
		vo.setDateHap(sdf.parse(SDMsgUtils.getPropValueStr(userMap,"date")));
		vo.setDateStart(sdf.parse(SDMsgUtils.getPropValueStr(userMap,"date")));
		vo.setNameEmpCg(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
		vo.setEuBltype("99");//????????????
		blCgVos.add(vo);
		//???????????????
		String ordsn = SDMsgUtils.getPropValueStr(blMap,"ordsn");
		List<BlPubParamVo> exSonOrder = exSonOrder(ordsn,userMap);
		blCgVos.addAll(exSonOrder);
		//??????????????????
		List<BlPubParamVo> blSamp = blSamp(ordsn,userMap);
		blCgVos.addAll(blSamp);
		//??????????????????
		User user = new User();
		user.setPkOrg(SDMsgUtils.getPropValueStr(userMap,"pkOrg"));
		user.setPkDept(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
		user.setPkEmp(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
		user.setNameEmp(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
		UserContext.setUser(user);
		//??????????????????
		BlIpCgVo cgVo = new BlIpCgVo();
		cgVo.setAllowQF(true);
		cgVo.setBlCgPubParamVos(blCgVos);
		ApplicationUtils apputil = new ApplicationUtils();
		apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,user);
		DataBaseHelper.execute("update ex_order_occ set flag_canc='0',pk_emp_canc= null ,date_canc= null ,eu_status='1',pk_emp_occ=?,name_emp_occ=?,date_occ=to_date(?,'yyyyMMddHH24miss') where pk_cnord=? ",blMap.get("pkEmpEx"),blMap.get("nameEmpEx"),userMap.get("date"),blMap.get("pkCnord"));
		DataBaseHelper.execute("update cn_lab_apply set eu_status ='2',pk_emp_col=?,name_emp_col = ?,modifier=?,date_col=to_date(?,'yyyyMMddHH24miss') ,modity_time=sysdate where pk_cnord=?",blMap.get("pkEmpEx"),blMap.get("nameEmpEx"),blMap.get("pkEmpEx"),userMap.get("date"),blMap.get("pkCnord"));
		DataBaseHelper.execute("update CN_ORDER set DATE_PLAN_EX=to_date(?,'yyyyMMddHH24miss'),PK_EMP_EX=?,NAME_EMP_EX=?,ts=sysdate where pk_cnord=?",userMap.get("date"),blMap.get("pkEmpEx"),blMap.get("nameEmpEx"),blMap.get("pkCnord"));
	}


	/**
	 * ???????????????????????????
	 * @param paramMap
	 */
	public boolean WechatDepositUpdete(Map<String, Object> paramMap) {
		boolean rs = false;
		BlDeposit blDeposit = new BlDeposit();
		blDeposit.setPkDepo(SDMsgUtils.getPk());
		blDeposit.setPkOrg(SDMsgUtils.getPropValueStr(paramMap, "pkOrg"));
		blDeposit.setEuDptype(SDMsgUtils.getPropValueStr(paramMap, "euDptype"));//??????????????? 0 ???????????????1 ???????????????2 ???????????????3 ???????????????4 ???????????????9 ???????????????
		blDeposit.setEuDirect(SDMsgUtils.getPropValueStr(paramMap, "euDirect"));//euDirect ???????????? 1??? -1???
		blDeposit.setPkPi(SDMsgUtils.getPropValueStr(paramMap,"pkPi"));//????????????
		blDeposit.setPkPv(SDMsgUtils.getPropValueStr(paramMap,"pkPv"));//????????????
		blDeposit.setDtPaymode(SDMsgUtils.getPropValueStr(paramMap, "dtPaymode"));//??????????????? ?????????????????????????????????????????????
		blDeposit.setAmount(new BigDecimal(SDMsgUtils.getPropValueStr(paramMap, "acoumt")));//??????
		//blDeposit.setDtBank(SDMsgUtils.getPropValueStr(paramMap, "dtBank"));//?????? ????????????????????????????????????????????????????????????
		blDeposit.setBankNo(SDMsgUtils.getPropValueStr(paramMap, "bankNo"));//???????????? ??????????????????????????????????????????
		blDeposit.setPayInfo(SDMsgUtils.getPropValueStr(paramMap, "payInfo"));//????????????????????? ???????????????????????????????????????
		try {
			Date datePay = sdf.parse(SDMsgUtils.getPropValueStr(paramMap, "datePay"));
			blDeposit.setDatePay(datePay);//datePay ???????????????
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		blDeposit.setPkDept(SDMsgUtils.getPropValueStr(paramMap, "pkDept"));//pkDept ???????????????
		//blDeposit.setPkEmpPay(SDMsgUtils.getPropValueStr(paramMap, "pkEmpPay"));//pkEmpPay ?????????
		blDeposit.setNameEmpPay(SDMsgUtils.getPropValueStr(paramMap, "nameEmpPay"));//nameEmpPay ???????????????
		blDeposit.setFlagAcc("0");//flagAcc ??????????????????
		blDeposit.setPkAcc(SDMsgUtils.getPropValueStr(paramMap, "pkAcc"));//pkAcc ????????????
		blDeposit.setFlagSettle("0");//flagSettle ????????????
		blDeposit.setPkSettle(SDMsgUtils.getPropValueStr(paramMap, "pkSettle"));//pkSettle ????????????
		blDeposit.setFlagCc(SDMsgUtils.getPropValueStr(paramMap, "flagCc"));//flagCc ?????????????????????
		blDeposit.setPkCc(SDMsgUtils.getPropValueStr(paramMap, "pkCc"));//pkCc ?????????????????????
		blDeposit.setReptNo(SDMsgUtils.getPropValueStr(paramMap, "reptNo"));//reptNo????????????
		blDeposit.setFlagReptBack(SDMsgUtils.getPropValueStr(paramMap, "flagReptBack"));//flagReptBack ????????????????????????????????????
		//blDeposit.setDateReptBack(new Date());//dateReptBack ??????????????????
		blDeposit.setPkEmpBack(SDMsgUtils.getPropValueStr(paramMap, "pkEmpBack"));//pkEmpBack ??????????????????
		blDeposit.setNameEmpBack(SDMsgUtils.getPropValueStr(paramMap, "nameEmpBack"));//nameEmpBack ????????????????????????
		blDeposit.setPkDepoBack(SDMsgUtils.getPropValueStr(paramMap, "pkDepoBack"));//pkDepoBack ????????????????????????????????????????????????
		blDeposit.setPkStMid(SDMsgUtils.getPropValueStr(paramMap, "pkStMid"));//pkStMid ?????????????????????????????????????????????????????????
		blDeposit.setNote(Constant.NOTE);//note ??????????????????
		blDeposit.setEuPvtype(SDMsgUtils.getPropValueStr(paramMap, "euPvtype"));//euPvtype ????????????
		blDeposit.setPkEmpinvoice(SDMsgUtils.getPropValueStr(paramMap, "pkEmpinvoice"));//pkEmpinvoice ??????????????????
		blDeposit.setFlagCcRept(SDMsgUtils.getPropValueStr(paramMap, "flagCcRept"));//flagCcRept  ??????????????????
		blDeposit.setPkCcRept(SDMsgUtils.getPropValueStr(paramMap, "pkCcRept"));//pkCcRept ??????????????????
		//blDeposit.setDateRept(new Date());//dateRept ??????????????????
		blDeposit.setPkEmpRept(SDMsgUtils.getPropValueStr(paramMap, "pkEmpRept"));//pkEmpRept  ??????????????????
		blDeposit.setNameEmpRept(SDMsgUtils.getPropValueStr(paramMap, "nameEmpRept"));//nameEmpRept  ????????????????????????
		blDeposit.setPaymodeName(SDMsgUtils.getPropValueStr(paramMap, "paymodeName"));//paymodeName ?????????????????????
		//blDeposit.setCnt(Integer.parseInt(SDMsgUtils.getPropValueStr(paramMap, "cnt")));// cnt ???????????????
		//blDeposit.setBankTime(new Date());//bankTime  ????????????
		blDeposit.setOutTradeNo(SDMsgUtils.getPropValueStr(paramMap, "outTradeNo"));//outTradeNo pos?????????
		blDeposit.setPayResult(SDMsgUtils.getPropValueStr(paramMap, "payResult"));//payResult ????????????
		blDeposit.setSerialNum(SDMsgUtils.getPropValueStr(paramMap, "serialNum"));//serialNum ???????????????
		//blDeposit.setExtAmount(new BigDecimal(SDMsgUtils.getPropValueStr(paramMap, "extAmount")));// extAmount ?????????????????????
		blDeposit.setEuStatus(SDMsgUtils.getPropValueStr(paramMap, "euStatus"));//euStatus ???????????????pv_encounter.eu_status **/
		blDeposit.setTradeNo(SDMsgUtils.getPropValueStr(paramMap, "tradeNo"));//tradeNo ??????????????????
		blDeposit.setCreateTime(new Date());
		blDeposit.setDelFlag("0");
		blDeposit.setTs(new Date());
		if(CommonUtils.isEmptyString(blDeposit.getCodeDepo())){
			//codeDepo ????????????
			blDeposit.setCodeDepo(ApplicationUtils.getCode("0606"));
		}
		//???????????????

		 int insertBean = DataBaseHelper.insertBean(blDeposit);
		 if(insertBean>0){
			 rs = true;
		 }
		//ApplicationUtils apputil = new ApplicationUtils();
		//ResponseJson execService = apputil.execService("PV", "BlPrePayService", "savePatiRefundInfo",refundList,u);
		return rs;
	}

	/**
	 * ??????????????????
	 * @param paramMap
	 */
	public void updateWeightInfo(Map<String, Object> paramMap) {
		String sql = "update pv_encounter pv set pv.weight=? where pv.code_pv=?";
		int execute = DataBaseHelper.execute(sql, paramMap.get("weight"),paramMap.get("codePv"));
		if(execute!=1){
			throw new BusException("????????????????????????????????????????????????????????????????????????");
		}
	}


	/**
	 * ????????????????????????PDA???
	 * @param userMap
	 * @param exList
	 * @param stMap ????????????
	 * @return
	 * @throws ParseException
	 */
	public boolean saveExBlOrd(List<Map<String, Object>> exList, Map<String, Object> userMap,Map<String,Object> stMap) throws ParseException {
		boolean result = false;
		//??????????????????
		List<String> pkExoccList = new ArrayList<String>();
		//????????????
		List<String> pkCnordList = new ArrayList<String>();
		List<BlPubParamVo> blCgVos = new ArrayList<>();
		for(Map<String, Object> map : exList){
			//??????????????????
			pkExoccList.add(SDMsgUtils.getPropValueStr(map,"pkExocc"));
			//????????????
			pkCnordList.add(SDMsgUtils.getPropValueStr(map,"pkCnord"));
			//??????????????????????????????
			if("1".equals(SDMsgUtils.getPropValueStr(map,"flagDurg"))){
				continue;
			}
			BlPubParamVo vo = new BlPubParamVo();
			vo.setPkOrg(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			vo.setPkPi(SDMsgUtils.getPropValueStr(map,"pkPi"));
			vo.setPkPv(SDMsgUtils.getPropValueStr(map,"pkPv"));
			vo.setEuPvType(SDMsgUtils.getPropValueStr(map,"euPvtype"));
			//vo.setPkItem(SDMsgUtils.getPropValueStr(map,"pkItem"));//????????????
			vo.setFlagPd(SDMsgUtils.getPropValueStr(map,"flagDurg"));//0????????????
			vo.setPkOrd(SDMsgUtils.getPropValueStr(map,"pkOrd"));
			vo.setPkCnord(SDMsgUtils.getPropValueStr(map,"pkCnord"));
			if(CommonUtils.isEmptyString(vo.getPkOrd()) &&CommonUtils.isEmptyString(vo.getPkItem())){
				log.info("????????????"+Thread.currentThread().getName()+"???????????????ZAS_O17,pkItem???pkOrd???????????????????????????"+SDMsgUtils.getPropValueStr(map,"pkExocc"));
				continue;
			}
			Double num = 0.0;
			if(!"".equals(SDMsgUtils.getPropValueStr(map,"quan"))){
				num = Double.valueOf(SDMsgUtils.getPropValueStr(map,"quan"));
			}
			vo.setQuanCg(num);
			Double price = 0.0;
			if(!"".equals(SDMsgUtils.getPropValueStr(map,"priceCg"))){
				price = Double.valueOf(SDMsgUtils.getPropValueStr(map,"priceCg"));
			}
			vo.setPrice(price);
			vo.setPriceCost(price);//????????????
			int packSize = 0;
			if(!"".equals(SDMsgUtils.getPropValueStr(map,"packSize"))){
				packSize = Integer.valueOf(SDMsgUtils.getPropValueStr(map,"packSize"));
			}
			vo.setPackSize(packSize);//?????????
			vo.setBatchNo("~");//????????????
			vo.setPkUnitPd(SDMsgUtils.getPropValueStr(map,"pkUnit"));//??????
			vo.setPkOrgApp(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			vo.setPkDeptApp(SDMsgUtils.getPropValueStr(map,"pkDept"));
			vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(map,"pkDeptNs"));
			vo.setPkEmpApp(SDMsgUtils.getPropValueStr(map,"pkEmpOrd"));
			vo.setNameEmpApp(SDMsgUtils.getPropValueStr(map,"nameEmpOrd"));
			vo.setPkOrdexdt(SDMsgUtils.getPropValueStr(map,"pkExocc"));
			vo.setPkOrgEx(SDMsgUtils.getPropValueStr(userMap,"pkOrg"));
			vo.setPkDeptEx(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			vo.setPkEmpEx(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			vo.setNameEmpEx(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			vo.setPkDeptCg(SDMsgUtils.getPropValueStr(userMap,"pkDeptOcc"));
			vo.setPkEmpCg(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			vo.setEuBltype("99");
			vo.setNameEmpCg(SDMsgUtils.getPropValueStr(userMap, "nameEmp"));
			vo.setDateHap(sdf.parse(SDMsgUtils.getPropValueStr(userMap, "date")));
			vo.setDateCg(sdf.parse(SDMsgUtils.getPropValueStr(userMap, "date")));
			blCgVos.add(vo);
		}
		//??????????????????
		List<BlPubParamVo> stResList=createSupplyCgInfo(exList.get(0),SDMsgUtils.getPropValueStr(stMap, "codeSupply"),SDMsgUtils.getPropValueStr(stMap, "batchNo"),userMap);
		if(stResList!=null && stResList.size()>0){
			blCgVos.addAll(stResList);
		}
		//??????????????????
		User user = new User();
		user.setPkOrg(SDMsgUtils.getPropValueStr(userMap, "pkOrg"));
		user.setPkEmp(SDMsgUtils.getPropValueStr(userMap, "pkEmp"));
		//user.setNameEmp(Constant.NOTE);
		user.setNameEmp(SDMsgUtils.getPropValueStr(userMap, "nameEmp"));
		user.setPkDept(SDMsgUtils.getPropValueStr(userMap, "pkDept"));
		UserContext.setUser(user);
		//????????????????????????????????????????????????????????????????????????
		if(blCgVos!=null && blCgVos.size()>0){
			//??????????????????
			BlIpCgVo cgVo = new BlIpCgVo();
			cgVo.setAllowQF(true);
			cgVo.setBlCgPubParamVos(blCgVos);
			ApplicationUtils apputil = new ApplicationUtils();
			apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,user);
			result = true;
		}
		if(pkExoccList.size()>0 && pkCnordList.size()>0){
			//??????????????????
			updateExBlOrd(pkExoccList,pkCnordList,userMap);
		}

		//??????????????????????????????
		if(stMap!=null && !"".equals(SDMsgUtils.getPropValueStr(stMap, "result")) && !"".equals(SDMsgUtils.getPropValueStr(stMap, "batchNo"))){
			//??????????????????
			updateStResult(exList.get(0),userMap,stMap);
		}

		return result;
	}

	/**
	 * ??????????????????
	 * @param map ??????????????????
	 * @param codeSupply ????????????
	 * @param batchNo ?????????????????? ?????????
	 * @param userMap ??????????????????
	 * @return
	 */
	public List<BlPubParamVo> createSupplyCgInfo(Map<String,Object> map,String codeSupply,String batchNo,Map<String,Object> userMap){
		//???????????????????????????????????????
		String pkCnord=SDMsgUtils.getPropValueStr(map,"pkCnord");
		String chk="select count(1) from cn_order where ordsn = ordsn_parent and pk_cnord=?";
		Integer count=DataBaseHelper.queryForScalar(chk, Integer.class, new Object[]{pkCnord});
		if(count==0 ||count==null){
			return null;
		}
		//????????????????????????????????????
		StringBuffer sql=new StringBuffer();
		sql.append(" select ui.*,item.price,item.PK_UNIT from bd_supply_item ui");
		sql.append(" inner join bd_supply sup on sup.pk_supply=ui.pk_supply");
		sql.append(" inner join bd_item item on item.pk_item=ui.pk_item");
		sql.append(" where ui.eu_pvtype = ? and sup.code = ?");
		List<Map<String,Object>> itemList=DataBaseHelper.queryForList(sql.toString(),new Object[]{SDMsgUtils.getPropValueStr(map,"euPvtype"),codeSupply});
		if(itemList==null || itemList.size()==0) return null;
		List<BlPubParamVo> resList=new ArrayList<>();
		for (Map<String, Object> item : itemList) {
			BlPubParamVo vo = new BlPubParamVo();
			vo.setPkOrg(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			vo.setPkPi(SDMsgUtils.getPropValueStr(map,"pkPi"));
			vo.setPkPv(SDMsgUtils.getPropValueStr(map,"pkPv"));
			vo.setEuPvType(SDMsgUtils.getPropValueStr(map,"euPvtype"));
			vo.setPkItem(SDMsgUtils.getPropValueStr(item,"pkItem"));//????????????
			vo.setFlagPd("0");//0????????????
			//vo.setPkOrd(SDMsgUtils.getPropValueStr(map,"pkOrd"));
			vo.setPkCnord(SDMsgUtils.getPropValueStr(map,"pkCnord"));
			Double num = 0.0;
			if(!"".equals(SDMsgUtils.getPropValueStr(item,"quan"))){
				num = Double.valueOf(SDMsgUtils.getPropValueStr(item,"quan"));
			}
			vo.setQuanCg(num);
			Double price = 0.0;
			if(!"".equals(SDMsgUtils.getPropValueStr(item,"price"))){
				price = Double.valueOf(SDMsgUtils.getPropValueStr(item,"price"));
			}
			vo.setPriceCost(price);//????????????
			vo.setPackSize(1);//?????????
			vo.setBatchNo(batchNo!=null?batchNo:"~");//????????????
			vo.setPkUnitPd(SDMsgUtils.getPropValueStr(map,"pkUnit"));//??????
			vo.setPkOrgApp(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			vo.setPkDeptApp(SDMsgUtils.getPropValueStr(map,"pkDept"));
			vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(map,"pkDeptNs"));
			vo.setPkEmpApp(SDMsgUtils.getPropValueStr(map,"pkEmpOrd"));
			vo.setNameEmpApp(SDMsgUtils.getPropValueStr(map,"nameEmpOrd"));
			vo.setPkOrdexdt(SDMsgUtils.getPropValueStr(map,"pkExocc"));
			vo.setPkOrgEx(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			vo.setPkDeptEx(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			vo.setPkEmpEx(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			vo.setNameEmpEx(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			vo.setPkDeptCg(SDMsgUtils.getPropValueStr(map,"pkDeptOcc"));
			vo.setPkEmpCg(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			vo.setEuBltype("99");
			vo.setNameEmpCg(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			vo.setDateHap(new Date());
			vo.setDateCg(new Date());
			resList.add(vo);
		}

		return resList;
	}

	/**
	 * ??????????????????
	 * @param map ??????????????????????????????
	 * @param userMap ????????????
	 * @param stMap ??????????????????
	 */
	private void updateStResult(Map<String, Object> map,Map<String, Object> userMap,Map<String,Object> stMap){
		ExStOcc stocc=new ExStOcc();
		ApplicationUtils.setDefaultValue(stocc, true);
		stocc.setPkStocc(NHISUUID.getKeyId());
		stocc.setPkPi(SDMsgUtils.getPropValueStr(map,"pkPi"));
		stocc.setPkPv(SDMsgUtils.getPropValueStr(map,"pkPv"));
		stocc.setPkCnord(SDMsgUtils.getPropValueStr(map,"pkCnord"));
		stocc.setPkDeptOcc(SDMsgUtils.getPropValueStr(map,"pkDeptOcc"));
		stocc.setPkOrgOcc(SDMsgUtils.getPropValueStr(map,"pkOrg"));
		stocc.setPkEmpOcc(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
		stocc.setNameEmpOcc(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
		stocc.setDateOcc(SDMsgUtils.getPropValueDate(userMap,"date"));
		//stocc.setDateBeginSt(SDMsgUtils.getPropValueDate(stMap,"dateStart"));
		stocc.setDateBeginSt(SDMsgUtils.getPropValueDate(userMap,"date"));
		String result=SDMsgUtils.getPropValueStr(stMap,"result");
		stocc.setResult(result);//????????????
		stocc.setPkDeptAp(SDMsgUtils.getPropValueStr(map,"pkDept"));
		stocc.setPkDeptNsAp(SDMsgUtils.getPropValueStr(map,"pkDeptNs"));
		stocc.setFlagChk("1");
		stocc.setPkEmpChk(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
		stocc.setNameEmpChk(SDMsgUtils.getPropValueStr(userMap,"nameEmp")+"*");
		stocc.setDateChk(SDMsgUtils.getPropValueDate(stMap,"date"));
		stocc.setBatchNo(SDMsgUtils.getPropValueStr(stMap,"batchNo"));
		//???????????? ??????20min
		stocc.setDuartion(20);
		stocc.setModifier(Constant.NOTE);
		DataBaseHelper.insertBean(stocc);

		String euSt="";
		if(result.contains("???")||result.contains("+")){
			euSt="3";
		}
		if(result.contains("???")||result.contains("-")){
			euSt="2";
		}
		if(CommonUtils.isNotNull(euSt)){
			String pkCnord=SDMsgUtils.getPropValueStr(map,"pkCnord");
			String sql="update cn_order set eu_st=? where ordsn_parent in(select ordsn_parent from cn_order where  pk_cnord=?) or pk_cnord_rl=?";
			DataBaseHelper.update(sql, new Object[]{euSt,pkCnord,pkCnord});

		}
	}

	/**
	 * ????????????????????????????????????????????????
	 * @param pkExoccList
	 * @param pkCnordList
	 * @param userMap
	 */
	private void updateExBlOrd(List<String> pkExoccList,List<String> pkCnordList,Map<String, Object> userMap) {
		int rs = 0;
		//?????????????????????
		for(String pkExocc : pkExoccList){
			if(pkExocc != null && !"".equals(pkExocc)){
				userMap.put("pkExocc", pkExocc);
				String sql ="update ex_order_occ ex set ex.eu_status ='1',ex.modifier='PDA??????',ex.ts = sysdate,ex.date_occ = to_date(?,'YYYYMMDDHH24MiSS'),ex.name_emp_occ=?,ex.pk_emp_occ=? where ex.pk_exocc = ?";
				rs += DataBaseHelper.execute(sql,SDMsgUtils.getPropValueStr(userMap, "date"),SDMsgUtils.getPropValueStr(userMap, "nameEmp"),SDMsgUtils.getPropValueStr(userMap, "pkEmp"),pkExocc);
			}
		}
		//??????????????????
		for(String pkCnord : pkCnordList){
			String sql = "update CN_ORDER set DATE_PLAN_EX=to_date(?,'YYYYMMDDHH24MiSS'),NAME_EMP_EX=?,PK_EMP_EX=? ,ts = sysdate where PK_CNORD=?";
			rs += DataBaseHelper.execute(sql,SDMsgUtils.getPropValueStr(userMap, "date"),SDMsgUtils.getPropValueStr(userMap, "nameEmp"),SDMsgUtils.getPropValueStr(userMap, "pkEmp"),pkCnord);
		}
		if(rs <= 0){
			log.info("?????????????????????????????????????????????/r/n");
			throw new BusException("????????????????????????????????????????????????????????????????????????????????????");
		}
	}

	/**
	 * ????????????????????????????????????
	 * @param ordsn
	 * @param userMap
	 * @return
	 */
	private List<BlPubParamVo> exSonOrder(String ordsn,Map<String,Object> userMap){
		List<BlPubParamVo> voList = new ArrayList<BlPubParamVo>();
		String sql = "select * from CN_ORDER o left join EX_ORDER_OCC ex on o.PK_CNORD=ex.PK_CNORD "
				+ "where ex.eu_status='0' and o.ORDSN_PARENT^=o.ORDSN and o.ORDSN_PARENT=?";
		List<Map<String,Object>> dtlist = DataBaseHelper.queryForList(sql, ordsn);
		for(Map<String, Object> map : dtlist){
			//???????????????????????????????????????
			//String sql = "select sum(d.amount) momey,sum(quan) quan from bl_ip_dt d where d.pk_cnord=?";
			//Map<String, Object> count = DataBaseHelper.queryForMap(sql, SDMsgUtils.getPropValueStr(map,"pkCnord"));
			BlPubParamVo vo = new BlPubParamVo();
			vo.setPkOrg(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			vo.setPkPi(SDMsgUtils.getPropValueStr(map,"pkPi"));
			vo.setPkPv(SDMsgUtils.getPropValueStr(map,"pkPv"));
			vo.setEuPvType(SDMsgUtils.getPropValueStr(map,"euPvtype"));
			//vo.setPkItem(SDMsgUtils.getPropValueStr(map,"pkItem"));//????????????
			vo.setFlagPd("0");//0????????????
			vo.setPkOrd(SDMsgUtils.getPropValueStr(map,"pkOrd"));
			vo.setPkCnord(SDMsgUtils.getPropValueStr(map,"pkCnord"));
			Double num = 0.0;
			if(!"".equals(SDMsgUtils.getPropValueStr(map,"quan"))){
				num = Double.valueOf(SDMsgUtils.getPropValueStr(map,"quan"));
			}
			vo.setQuanCg(num);
			vo.setPkOrgApp(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			vo.setPkDeptApp(SDMsgUtils.getPropValueStr(map,"pkDept"));
			vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(map,"pkDeptNs"));
			vo.setPkEmpApp(SDMsgUtils.getPropValueStr(map,"pkEmpOrd"));
			vo.setNameEmpApp(SDMsgUtils.getPropValueStr(map,"nameEmpOrd"));
			vo.setPkOrdexdt(SDMsgUtils.getPropValueStr(map,"pkExocc"));
			vo.setEuBltype("99");//????????????
			vo.setDateHap(new Date());
			//???????????????????????? (????????????)
			vo.setPkOrgEx(SDMsgUtils.getPropValueStr(map,"pkOrg"));
			vo.setPkDeptEx(SDMsgUtils.getPropValueStr(map,"pkDeptExec"));
			//vo.setPkDeptEx(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			vo.setPkEmpEx(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			vo.setNameEmpEx(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			//????????????????????????
			//vo.setPkDeptCg(SDMsgUtils.getPropValueStr(map,"pkDeptExec"));
			vo.setPkDeptCg(SDMsgUtils.getPropValueStr(userMap,"pkDept"));
			vo.setPkEmpCg(SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
			vo.setNameEmpCg(SDMsgUtils.getPropValueStr(userMap,"nameEmp"));
			//vo.setNameEmpCg(SDMsgUtils.getPropValueStr(map,"nameemp"));
			vo.setDateCg(new Date());
			voList.add(vo);
			String sqlOrder = "update CN_ORDER o set o.EU_STATUS_ORD='3',o.date_last_ex=sysdate,o.pk_emp_ex='"
					+SDMsgUtils.getPropValueStr(userMap,"pkEmp")
					+"',name_emp_ex='"+SDMsgUtils.getPropValueStr(userMap,"nameEmp")
					+"' where o.ordsn^=o.ordsn_parent and o.ORDSN_PARENT=?";
			DataBaseHelper.execute(sqlOrder,ordsn);
			String sqlEx = "update ex_order_occ ex set ex.eu_status ='1',ex.date_occ = sysdate,ex.name_emp_occ=:nameEmp,ex.pk_emp_occ=:pkEmp where ex.eu_status='0' and ex.pk_exocc ='"+SDMsgUtils.getPropValueStr(map,"pkExocc")+"'";
			DataBaseHelper.update(sqlEx,userMap);

		}
		return voList;
	}

	/**
	 * ???????????????
	 * @param ordsns
	 * @param user
	 * @return
	 */
	private List<RefundVo> cancelSonOrder(String ordsns,User user){
		List<RefundVo> voList = new ArrayList<RefundVo>();
		String sql = "select o.pk_cnord from CN_ORDER o left join EX_ORDER_OCC ex on o.PK_CNORD=ex.PK_CNORD "
				+ "where ex.eu_status='1' and o.ORDSN_PARENT^=o.ORDSN and o.ORDSN_PARENT in ("+ordsns+")";
		List<Map<String, Object>> dtlist = DataBaseHelper.queryForList(sql);
		for(Map<String,Object> map : dtlist){
			String pkCnord = SDMsgUtils.getPropValueStr(map, "pkCnord");
			String sqlBl = "select * from (select blip.pk_cgip,blip.quan + (CASE WHEN back.quan is null THEN 0 ELSE back.quan END) as quan  from BL_IP_DT blip  left outer join  (select sum(quan) quan,sum(amount) amt,pk_cgip_back from bl_ip_dt  where flag_settle = 0 and flag_pd = 0 and  quan < 0  and PK_CNORD = ?  group by pk_cgip_back ) back on blip.pk_cgip=back.pk_cgip_back where blip.del_flag = '0' and blip.pk_cgip_back is null and blip.flag_settle='0' and blip.quan >0 and blip.pk_cnord = ? ) dt where dt.quan>0 ";
			List<Map<String, Object>> BlList = DataBaseHelper.queryForList(sqlBl,pkCnord,pkCnord);
			for (Map<String,Object> m:BlList) {
				double num = 0.0;
				if(!"".equals(SDMsgUtils.getPropValueStr(m,"quan"))){
					num = Double.valueOf(SDMsgUtils.getPropValueStr(m,"quan"));
				}
				RefundVo refundVo = new RefundVo();
				refundVo.setPkOrg(user.getPkOrg());
				refundVo.setPkCgip(SDMsgUtils.getPropValueStr(m,"pkCgip"));//????????????
				//refundVo.setNameEmp(SDMsgUtils.getPropValueStr(exDoc,"nameEmp"));
				refundVo.setNameEmp(user.getNameEmp());
				refundVo.setPkEmp(user.getPkEmp());
				refundVo.setPkDept(user.getPkDept());
				refundVo.setQuanRe(num);
				voList.add(refundVo);
			}
			//????????????
			// ????????????  flag_canc='1' ???????????????????????????????????????????????????
			DataBaseHelper.execute("update ex_order_occ set date_occ= null, pk_emp_occ= null, name_emp_occ= null, pk_cg= null,eu_status ='0',pk_emp_canc=?,date_canc=sysdate where pk_cnord=? ",user.getPkEmp(),pkCnord);
		}
		return voList;
	}

	/**
	 * ??????????????????
	 * @param codeApply
	 */
	public void delLisRptList(String codeApply,String codeRpt) {
		//????????????
		if(codeApply != null && !"".equals(codeApply)){
			DataBaseHelper.execute("delete from EX_LAB_OCC where CODE_APPLY=? and CODE_RPT=? ", codeApply,codeRpt);
			DataBaseHelper.execute("delete from EX_LAB_OCC_BACT where pk_labocc in (select pk_labocc from EX_LAB_OCC where CODE_APPLY=? and CODE_RPT=?)", codeApply,codeRpt);
		}else{
			log.info("??????????????????????????????????????????????????????"+ codeApply);
		}

	}


	/**
	 * ?????????????????????
	 * @param pvIpNotice
	 * @param insert
	 */
	public void updateADTA06(PvIpNotice pvIpNotice,boolean insert) {
		int result = 0;
		//?????????????????????
		if(insert){
			result = DataBaseHelper.insertBean(pvIpNotice);
		}
		else {
			result = DataBaseHelper.updateBeanByPk(pvIpNotice);
		}
		if(result!=1){
			throw new BusException("??????????????????????????????????????????????????????????????????");
		}
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param addList
	 */
	public void saveQRYP04(List<MidBlOp> addList) {
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(MidBlOp.class), addList);
	}

}
