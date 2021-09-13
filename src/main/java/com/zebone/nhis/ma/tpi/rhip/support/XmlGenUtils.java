package com.zebone.nhis.ma.tpi.rhip.support;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.ibatis.reflection.wrapper.CollectionWrapper;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.ma.tpi.rhip.vo.Authororganization;
import com.zebone.nhis.ma.tpi.rhip.vo.BsPatient;
import com.zebone.nhis.ma.tpi.rhip.vo.DocInfo;
import com.zebone.nhis.ma.tpi.rhip.vo.IptAdmissionNote;
import com.zebone.nhis.ma.tpi.rhip.vo.IptAdviceDetail;
import com.zebone.nhis.ma.tpi.rhip.vo.IptFee;
import com.zebone.nhis.ma.tpi.rhip.vo.IptFeeDetail;
import com.zebone.nhis.ma.tpi.rhip.vo.IptLeaveRecord;
import com.zebone.nhis.ma.tpi.rhip.vo.IptMedicalRecordPage;
import com.zebone.nhis.ma.tpi.rhip.vo.IptRecord;
import com.zebone.nhis.ma.tpi.rhip.vo.IptSignsRecord;
import com.zebone.nhis.ma.tpi.rhip.vo.OptBL;
import com.zebone.nhis.ma.tpi.rhip.vo.OptFee;
import com.zebone.nhis.ma.tpi.rhip.vo.OptFeeDetail;
import com.zebone.nhis.ma.tpi.rhip.vo.OptRecipe;
import com.zebone.nhis.ma.tpi.rhip.vo.OptRecord;
import com.zebone.nhis.ma.tpi.rhip.vo.OptRegister;
import com.zebone.nhis.ma.tpi.rhip.vo.PMCDrugEntry;
import com.zebone.nhis.ma.tpi.rhip.vo.PMCDrugStock;
import com.zebone.nhis.ma.tpi.rhip.vo.PMCDrugStore;
import com.zebone.nhis.ma.tpi.rhip.vo.PMCPharmacyEntry;
import com.zebone.nhis.ma.tpi.rhip.vo.PMCPharmacyStock;
import com.zebone.nhis.ma.tpi.rhip.vo.PatListVo;
import com.zebone.nhis.ma.tpi.rhip.vo.Patient;
import com.zebone.nhis.ma.tpi.rhip.vo.PtDiagnosis;
import com.zebone.nhis.ma.tpi.rhip.vo.PtExamReport;
import com.zebone.nhis.ma.tpi.rhip.vo.PtLabReport;
import com.zebone.nhis.ma.tpi.rhip.vo.PtOperation;
import com.zebone.nhis.ma.tpi.rhip.vo.Request;
import com.zebone.nhis.ma.tpi.rhip.vo.Response;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;

/**
 * 接口XML创建工具
 * @author chengjia
 *
 */
public class XmlGenUtils {
	
	public static void main(String[] args) throws Exception {
		User user=new User();
		IptAdmissionNote note=new IptAdmissionNote();
		PvEncounter pv=new PvEncounter();
		PiMaster pi=new PiMaster();
//		String xml=create(user,pi,pv,note);
//		System.out.println(xml);
	}
	
	/**
	 * 创建xml
	 * @param pUser
	 * @param pat
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String create(IUser pUser,PatListVo pat,Object obj) throws Exception {
		User user = (User) pUser;
		String recordClassifying="";
		String recordTitle="";
		GregorianCalendar gcal =new GregorianCalendar();
		XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);

		Calendar cal=Calendar.getInstance();
		String sourceId="3999238";//根据数据集设置
		String versionNumber=RpDataUtils.getPropValue("rhip.verNum");//"v2012";
		String dateStr=new Date().toString();
		if(obj instanceof IptRecord){
			//1
			recordTitle="住院就诊记录表";
			recordClassifying="Ipt_Record";
		}else if(obj instanceof IptAdmissionNote){
			//2
			recordTitle="住院入院记录";
			recordClassifying="Ipt_AdmissionNote";
		}else if(obj instanceof IptAdviceDetail){
			//3
			recordTitle="住院医嘱明细表";
			recordClassifying="Ipt_AdviceDetail";
		}else if(obj instanceof IptSignsRecord){
			//4
			recordTitle="住院体温单";
			recordClassifying="Ipt_SignsRecord";
		}else if(obj instanceof IptLeaveRecord){
			//5
			recordTitle="住院出院记录";
			recordClassifying="Ipt_LeaveRecord";
		}else if(obj instanceof IptMedicalRecordPage){
			//6
			recordTitle="住院病案首页";
			recordClassifying="Ipt_MedicalRecordPage";
		}else if(obj instanceof PtOperation){
			//7
			recordTitle="手术记录";
			recordClassifying="Pt_Operation";
		}else if(obj instanceof IptFee){
			//8
			recordTitle="在/出院结算表";
			recordClassifying="Ipt_Fee";
		}else if(obj instanceof IptFeeDetail){
			//9
			recordTitle="住院费用明细表";
			recordClassifying="Ipt_FeeDetail";
		}else if(obj instanceof OptRegister){
			//门诊
			//1
			recordTitle="挂号表";
			recordClassifying="Opt_Register";
		}else if(obj instanceof OptRecord){
			//2
			recordTitle="门诊就诊记录表";
			recordClassifying="Opt_Record";
		}else if(obj instanceof OptRecipe){
			//3
			recordTitle="门诊处方记录表";
			recordClassifying="Opt_Recipe";
		}else if(obj instanceof OptFee){
			//4
			recordTitle="门诊收费表";
			recordClassifying="Opt_Fee";
		}else if(obj instanceof OptFeeDetail){
			//4
			recordTitle="门诊收费明细表";
			recordClassifying="Opt_FeeDetail";
		}else if(obj instanceof PtExamReport){
			//3-1
			recordTitle="检查报告";
			recordClassifying="Pt_ExamReport";
		}else if(obj instanceof PtLabReport){
			//3-2
			recordTitle="检验报告";
			recordClassifying="Pt_LabReport";
		}else if(obj instanceof PtDiagnosis){
			//3-5
			recordTitle="诊断明细报告";
			recordClassifying="Pt_Diagnosis";
		}else if(obj instanceof BsPatient){
			//
			recordTitle="病人基本信息";
			recordClassifying="Bs_Patient";
		}else if(obj instanceof OptBL){
			//
			recordTitle="门诊病历";
			recordClassifying="Opt_BL";
		}else if(obj instanceof PMCDrugStore){
			//
			recordTitle="药房出库";
			recordClassifying="PMC_DrugStore";
		}else if(obj instanceof PMCDrugEntry){
			//
			recordTitle="药房入库";
			recordClassifying="PMC_DrugEntry";
		}else if(obj instanceof PMCDrugStock){
			//
			recordTitle="药房库存";
			recordClassifying="PMC_DrugStock";
		}else if(obj instanceof PMCPharmacyEntry){
			//
			recordTitle="药库入库";
			recordClassifying="PMC_PharmacyEntry";
		}else if(obj instanceof PMCPharmacyStock){
			//
			recordTitle="药库库存";
			recordClassifying="PMC_PharmacyStock";
		}
		
		Request request=new Request();
		request.body=new Request.Body();
		request.body.setDocFormat("02");
		request.header=new Request.Header();
		
		//DocInfo
		DocInfo docInfo=new DocInfo();
		docInfo.setRecordClassifying(recordClassifying);
		docInfo.setRecordTitle(recordTitle);
		docInfo.setEffectiveTime(now);
		docInfo.setAuthor(user.getNameEmp());
		docInfo.setAuthorID(user.getId());
		Authororganization author=new Authororganization();
		//author.setLocalText("中山市博爱医院");
		String orgName=RpDataUtils.getPropValue("rhip.orgName");
		author.setLocalText(orgName);
		//author.setValue("45726620444200011A1001");
		author.setValue(RpDataUtils.getPropValue("rhip.orgCode"));
		docInfo.setAuthororganization(author);
		//docInfo.setAuthororganization("45726620444200011A1001");
		
		docInfo.setSourceID(sourceId);
		docInfo.setVersionNumber(versionNumber);
		docInfo.setSystemTime(now);
		
		//Patient
		Patient patient=new Patient();
		patient.setPersonName(pat.getNamePi());
		patient.setSexCode(pat.getSexSpcode());
		patient.setBirthday(DateUtils.getDateStr(pat.getBirthDate()));
		patient.setIdCard(pat.getIdNo());
		if(pat.getDtIdtype()==null || pat.getDtIdtype().equals("98")) {
			patient.setIdType("99");
			patient.setCardNo("99");
		}else {
			patient.setIdType(pat.getDtIdtype());
			patient.setCardNo(pat.getBarcode());
		}
		
		patient.setCardType(RpDictUtils.getKlx(pat));
		patient.setAddress(pat.getAddress());
		patient.setAddressType("09");//@todo现住址
		patient.setContactNo(pat.getTelRel());
		patient.setNationCode(RpDataUtils.getNationCode(pat.getDtNation()));
		patient.setBloodTypeCode(pat.getAboSpcode());
		patient.setRhBloodCode(pat.getRhSpcode());
		patient.setMaritalStatusCode(pat.getMarrySpcode());
		patient.setWorkPlace(pat.getUnitWork());
		
		request.header.setDocInfo(docInfo);
		request.header.setPatient(patient);
		if(recordClassifying.equals("Ipt_Record")){
			//1住院就诊记录表
			IptRecord record=(IptRecord)obj;
			docInfo.setSourceID(record.getSourceId());
			request.body.setIptRecord(record);
		}else if(recordClassifying.equals("Ipt_AdviceDetail")){
			//2住院医嘱明细表
			IptAdviceDetail detail=(IptAdviceDetail)obj;
			docInfo.setSourceID(detail.getSourceId());
			request.body.setIptAdviceDetail(detail);
		}else if(recordClassifying.equals("Ipt_AdmissionNote")){
			//3住院入院记录
			IptAdmissionNote note=(IptAdmissionNote)obj;
			docInfo.setSourceID(note.getSourceId());
			request.body.setIptAdmissionNote(note);
		}else if(recordClassifying.equals("Ipt_SignsRecord")){
			//4住院体温单
			IptSignsRecord sRec=(IptSignsRecord)obj;
			docInfo.setSourceID(sRec.getSourceId());
			request.body.setIptSignsRecord(sRec);
		}else if(recordClassifying.equals("Ipt_LeaveRecord")){
			//5住院出院记录
			IptLeaveRecord lRec=(IptLeaveRecord)obj;
			docInfo.setSourceID(lRec.getSourceId());
			request.body.setIptLeaveRecord(lRec);
		}else if(recordClassifying.equals("Ipt_MedicalRecordPage")){
			//6住院病案首页
			IptMedicalRecordPage page=(IptMedicalRecordPage)obj;
			docInfo.setSourceID(page.getSourceId());
			request.body.setIptMedicalRecordPage(page);
		}else if(recordClassifying.equals("Pt_Operation")){
			//7手术记录
			PtOperation ptOperation=(PtOperation)obj;
			docInfo.setSourceID(ptOperation.getSourceId());
			request.body.setPtOperation(ptOperation);
		}else if(recordClassifying.equals("Ipt_Fee")){
			//8在/出院结算表
			IptFee iptFee=(IptFee)obj;
			docInfo.setSourceID(iptFee.getSourceId());
			request.body.setIptFee(iptFee);
		}else if(recordClassifying.equals("Ipt_FeeDetail")){
			//9住院费用明细表
			IptFeeDetail iptFeeDetail=(IptFeeDetail)obj;
			docInfo.setSourceID(iptFeeDetail.getSourceId());
			request.body.setIptFeeDetail(iptFeeDetail);
		}else if(recordClassifying.equals("Pt_Diagnosis")){
			//3-5诊断明细报告
			PtDiagnosis ptDiagnosis=(PtDiagnosis)obj;
			docInfo.setSourceID(ptDiagnosis.getSourceId());
			request.body.setPtDiagnosis(ptDiagnosis);
		}else if(recordClassifying.equals("Opt_Register")){
			//门诊
			//1挂号表
			OptRegister optRegister=(OptRegister)obj;
			docInfo.setSourceID(optRegister.getSourceId());
			request.body.setOptRegister(optRegister);
		}else if(recordClassifying.equals("Opt_Record")){
			//2门诊就诊记录表
			OptRecord optRecord=(OptRecord)obj;
			docInfo.setSourceID(optRecord.getSourceId());
			request.body.setOptRecord(optRecord);
		}else if(recordClassifying.equals("Opt_Recipe")){
			//3门诊处方记录表
			OptRecipe optRecipe=(OptRecipe)obj;
			docInfo.setSourceID(optRecipe.getSourceId());
			request.body.setOptRecipe(optRecipe);
		}else if(recordClassifying.equals("Opt_Fee")){
			//4门诊收费表
			OptFee optFee=(OptFee)obj;
			docInfo.setSourceID(optFee.getSourceId());
			request.body.setOptFee(optFee);
		}else if(recordClassifying.equals("Opt_FeeDetail")){
			//5门诊收费明细表
			OptFeeDetail optFeeDetail=(OptFeeDetail)obj;
			docInfo.setSourceID(optFeeDetail.getSourceId());
			request.body.setOptFeeDetail(optFeeDetail);
		}else if(recordClassifying.equals("Pt_ExamReport")){
			//3-1检查报告
			PtExamReport ptExamReport=(PtExamReport)obj;
			docInfo.setSourceID(ptExamReport.getSourceId());
			request.body.setPtExamReport(ptExamReport);
		}else if(recordClassifying.equals("Pt_LabReport")){
			//3-2检验报告
			PtLabReport ptLabReport=(PtLabReport)obj;
			docInfo.setSourceID(ptLabReport.getSourceId());
			request.body.setPtLabReport(ptLabReport);
		}else if(recordClassifying.equals("Pt_Diagnosis")){
			//3-5诊断明细报告
			PtDiagnosis ptDiagnosis=(PtDiagnosis)obj;
			docInfo.setSourceID(ptDiagnosis.getSourceId());
			request.body.setPtDiagnosis(ptDiagnosis);
		}else if(recordClassifying.equals("Bs_Patient")){
			//1.病人基本信息
			BsPatient bsPatient=(BsPatient)obj;
			docInfo.setSourceID(bsPatient.getSourceid());
			request.body.setBsPatient(bsPatient);
		}else if(recordClassifying.equals("Opt_BL")){
			//2.门诊病历
			OptBL optBL=(OptBL)obj;
			docInfo.setSourceID(sourceId);
			request.body.setOptBL(optBL); 
		}else if(recordClassifying.equals("PMC_DrugStore")){
			//1.药房出库
			PMCDrugStore pmcDrugStore=(PMCDrugStore)obj;
			docInfo.setSourceID(sourceId);
			request.body.setPmcDrugStore(pmcDrugStore); 
		}else if(recordClassifying.equals("PMC_DrugEntry")){
			//2.药房入库
			PMCDrugEntry pmcDrugEntry=(PMCDrugEntry)obj;
			docInfo.setSourceID(sourceId);
			request.body.setPmcDrugEntry(pmcDrugEntry); 
		}else if(recordClassifying.equals("PMC_DrugStock")){
			//3.药房库存
			PMCDrugStock pmcDrugStock=(PMCDrugStock)obj;
			docInfo.setSourceID(sourceId);
			request.body.setPmcDrugStock(pmcDrugStock); 
		}else if(recordClassifying.equals("PMC_PharmacyEntry")){
			//4.药库入库
			PMCPharmacyEntry pmcPharmacyEntry=(PMCPharmacyEntry)obj;
			docInfo.setSourceID(sourceId);
			request.body.setPmcPharmacyEntry(pmcPharmacyEntry); 
		}else if(recordClassifying.equals("PMC_PharmacyStock")){
			//2.药库库存
			PMCPharmacyStock pmcPharmacyStock=(PMCPharmacyStock)obj;
			docInfo.setSourceID(sourceId);
			request.body.setPmcPharmacyStock(pmcPharmacyStock); 
		}
		String retXml=null;
		try {
			JaxbUtil requestBinder = new JaxbUtil();
			retXml = requestBinder.toXml(request, "utf-8");
			//System.out.println("retXml:"+retXml);
			if(retXml!=null) retXml=retXml.replaceAll(RpDataUtils.nullChar, "");
			if(retXml!=null) retXml=retXml.replace("{", "");
			if(retXml!=null) retXml=retXml.replace("}", "");
//			System.out.println("xml:"+retXml);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error");
		}
		
		return retXml;
	}
	
	/**
	 * 根据xml创建Response
	 * @param xml
	 * @param response
	 * @throws Exception
	 */
	public static Response resolveResp(String xml) throws Exception {
		if(xml.isEmpty()) return null;
	    JaxbUtil requestBinder = new JaxbUtil(Response.class,CollectionWrapper.class);
	    Response response = requestBinder.fromXml(xml);
	    
	    return response;
	}
}
