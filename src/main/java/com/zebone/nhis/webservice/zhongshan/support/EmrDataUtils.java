package com.zebone.nhis.webservice.zhongshan.support;

import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.emr.comm.vo.EmrMedRecPatVo;
import com.zebone.nhis.emr.common.EmrUtils;
import com.zebone.nhis.webservice.zhongshan.vo.TaiKangRespEmrAdmitRec;
import com.zebone.nhis.webservice.zhongshan.vo.TaiKangRespEmrAdmitRecInfo;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 病历处理工具
 * @author chengjia
 *
 */
public class EmrDataUtils {

	//住院入院记录
	public static TaiKangRespEmrAdmitRec genPatAdmitRec(List<EmrMedRecPatVo> list) throws Exception {
		Iterator<Element> it = null;
		Element node=null;
		String nodeName="";
		String paraCodeElement="";
		Element nodeText=null;
		String text="";
		TaiKangRespEmrAdmitRec rtnRec=new TaiKangRespEmrAdmitRec();
		
		List<TaiKangRespEmrAdmitRecInfo> noteList=new ArrayList<TaiKangRespEmrAdmitRecInfo>();
		if(list==null||list.size()==0) return rtnRec;
		rtnRec.setRecList(noteList);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
		TaiKangRespEmrAdmitRecInfo note = null;
		for (EmrMedRecPatVo patRec : list) {
			if((patRec.getTypeCode() != null && patRec.getTypeCode().contains("0900")) || 
					(patRec.getTypeCode() != null && patRec.getTypeCode().contains("1200"))){
			if(note==null){
				note=new TaiKangRespEmrAdmitRecInfo();
				noteList.add(note);
			}
			//业务流水号_入院日期
			String codeIpDateBegin = patRec.getCodeIp()+"_"+format2.format(patRec.getDateAdmit());
			note.setCodeIpDateBegin(codeIpDateBegin);
			//就诊号-业务流水号
			note.setCodeIp(patRec.getCodeIp());
			//入院时间-入院时间
			note.setDateBegin(format.format(patRec.getDateBegin()));
			note.setTreatOffice(patRec.getDeptName());
			String flagCourseStr=patRec.getDocType().getFlagCourse();
			Boolean flagCourse=flagCourseStr==null?false:(flagCourseStr.equals("1")?true:false);
			EmrMedDoc doc =patRec.getMedDoc();
			org.w3c.dom.Document document=null;
			if(doc!=null){
				String docXml=doc.getDocXml();
				if (StringUtils.isNotBlank(docXml)){
					document=EmrUtils.string2doc(docXml);
				}
				if(docXml!=null&&!docXml.equals("")){
					try {
						Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
						Matcher matcher = pattern.matcher(docXml);
						if(matcher.find()){
							String str=matcher.group(1);
							docXml=docXml.replace(str,"");
						}

						SAXReader reader = new SAXReader();
						Document docReader = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8")));
						Element root = docReader.getRootElement();
						it = root.elementIterator();
						while (it.hasNext()) {
							// 获取某个子节点对象
							node = it.next();
							nodeName=node.getName();
							if(nodeName==null||!nodeName.equals("Region")) continue;

							if(flagCourse==false){
								//非病程
								if(node.attribute("para_code")==null) continue;
								paraCodeElement=node.attribute("para_code").getText();
								if(paraCodeElement!=null){
									nodeText= node.element("Content_Text");
									if(nodeText!=null){
										text=nodeText.getText();
									}else{
										text="-";
									}
									if(paraCodeElement.equals("01")){
										//主诉
										note.setMainSuit(text);
									}else if(paraCodeElement.equals("03")){
										//现病史
										note.setHistoryPresentIllness(text);
									}
									else if(paraCodeElement.equals("04")){
										//既往史
										note.setPastHistory(text);
									}
									else if(paraCodeElement.equals("04009")){
										//个人史
										note.setPersonalHistory(text);
									}
									else if(paraCodeElement.equals("04010")){
										//婚育史
										
									}
									else if(paraCodeElement.equals("04013")){
										//家族史
										note.setFamilyHistory(text);
									}
									else if(paraCodeElement.equals("04008")){
										//系统回顾
										
									}
									else if(paraCodeElement.equals("04011")){
										//月经史
										note.setMenstrualHistory(text);
									}
									else if(paraCodeElement.equals("07001")){
										//疾病名称/入院诊断
										note.setDiseaseName(text);
									}
									else if(paraCodeElement.equals("07002")){
										//出院诊断
										note.setLeaveMedicalSick(text);
									}
									else if(paraCodeElement.equals("02021")){
										//专科检查
										note.setPhysicalExamination(text);
									}
									else if(paraCodeElement.equals("02022")){
										//辅助检查
										note.setAuxiliaryExamination(text);
									}
									else if(paraCodeElement.equals("04006")){
										//过敏史
										note.setAllergies(text);
									}
									else if(paraCodeElement.equals("02")){
										//体格检查
										 
									}else if(paraCodeElement.equals("1200106")){
										//病历摘要
										
									}else if(paraCodeElement.equals("09001")){
										//预防接种史
										note.setHistoryOfImmunizations(text);
									}
									else if(paraCodeElement.equals("1200110")){
										//入院情况
										if(patRec.getTypeCode().contains("1200") || patRec.getTypeCode().contains("09000")){
											note.setCheckInDescription(text);
										}
									}
									else if(paraCodeElement.equals("1200112")){
										//诊疗经过
										if(patRec.getTypeCode().contains("1200") || patRec.getTypeCode().contains("09000")){
											note.setTreatmentRecord(text);
										}
									}
									else if(paraCodeElement.equals("1200111")){
										//出院情况描述
										if(patRec.getTypeCode().contains("1200") || patRec.getTypeCode().contains("09000")){
											note.setLeaveDescription(text);
										}
									}
									else if(paraCodeElement.equals("1200201")){
										//出院医嘱
										if(patRec.getTypeCode().contains("1200") || patRec.getTypeCode().contains("09000")){
											note.setDischargeOrder(text);
										}
									}
									else if(paraCodeElement.equals("11001")){
										//治疗结果
										note.setTreatmentResult(text);
									}
									
								}
							}
						}
						
						document.toString();


					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			
			}}
			//break;
		}
		for (TaiKangRespEmrAdmitRecInfo taiKangRespEmrAdmitRecInfo : rtnRec.getRecList()) {
			if(taiKangRespEmrAdmitRecInfo.getMainSuit()==null){//主诉
				taiKangRespEmrAdmitRecInfo.setMainSuit("");
			}
			if(taiKangRespEmrAdmitRecInfo.getHistoryPresentIllness()==null){//现病史
				taiKangRespEmrAdmitRecInfo.setHistoryPresentIllness("");
			}
			if(taiKangRespEmrAdmitRecInfo.getPastHistory()==null){//既往史
				taiKangRespEmrAdmitRecInfo.setPastHistory("");
			}
			if(taiKangRespEmrAdmitRecInfo.getPersonalHistory()==null){//个人史
				taiKangRespEmrAdmitRecInfo.setPersonalHistory("");
			}
			if(taiKangRespEmrAdmitRecInfo.getFamilyHistory()==null){//家族史
				taiKangRespEmrAdmitRecInfo.setFamilyHistory("");
			}
			if(taiKangRespEmrAdmitRecInfo.getDiseaseCode()==null){//疾病代码
				taiKangRespEmrAdmitRecInfo.setDiseaseCode("");
			}
			if(taiKangRespEmrAdmitRecInfo.getDiseaseName()==null){//疾病名称
				taiKangRespEmrAdmitRecInfo.setDiseaseName("");
			}
			if(taiKangRespEmrAdmitRecInfo.getOpsCode()==null){//手术编码
				taiKangRespEmrAdmitRecInfo.setOpsCode("");
			}
			if(taiKangRespEmrAdmitRecInfo.getOpsName()==null){//手术名称
				taiKangRespEmrAdmitRecInfo.setOpsName("");
			}
			if(taiKangRespEmrAdmitRecInfo.getLeaveMedicalSick()==null){//出院诊断
				taiKangRespEmrAdmitRecInfo.setLeaveMedicalSick("");
			}
			if(taiKangRespEmrAdmitRecInfo.getHistoryOfBloodTransfusion()==null){//输血史
				taiKangRespEmrAdmitRecInfo.setHistoryOfBloodTransfusion("");
			}
			if(taiKangRespEmrAdmitRecInfo.getAllergies()==null){//过敏史
				taiKangRespEmrAdmitRecInfo.setAllergies("");
			}
			if(taiKangRespEmrAdmitRecInfo.getHistoryOfImmunizations()==null){//预防接种史
				taiKangRespEmrAdmitRecInfo.setHistoryOfImmunizations("");
			}
			if(taiKangRespEmrAdmitRecInfo.getMenstrualHistory()==null){//月经史
				taiKangRespEmrAdmitRecInfo.setMenstrualHistory("");
			}
			if(taiKangRespEmrAdmitRecInfo.getPhysicalExamination()==null){//体格检查结果
				taiKangRespEmrAdmitRecInfo.setPhysicalExamination("");
			}
			if(taiKangRespEmrAdmitRecInfo.getAuxiliaryExamination()==null){//辅助检查结果
				taiKangRespEmrAdmitRecInfo.setAuxiliaryExamination("");
			}
			if(taiKangRespEmrAdmitRecInfo.getTreatOffice()==null){//科室
				taiKangRespEmrAdmitRecInfo.setTreatOffice("");
			}
			if(taiKangRespEmrAdmitRecInfo.getPathologicalExaminationNo()==null){//病理检查号
				taiKangRespEmrAdmitRecInfo.setPathologicalExaminationNo("");
			}
			if(taiKangRespEmrAdmitRecInfo.getCheckInDescription()==null){//入院情况
				taiKangRespEmrAdmitRecInfo.setCheckInDescription("");
			}
			if(taiKangRespEmrAdmitRecInfo.getTreatmentRecord()==null){//诊疗经过
				taiKangRespEmrAdmitRecInfo.setTreatmentRecord("");
			}
			if(taiKangRespEmrAdmitRecInfo.getTreatmentResult()==null){//治疗结果
				taiKangRespEmrAdmitRecInfo.setTreatmentResult("");
			}
			if(taiKangRespEmrAdmitRecInfo.getLeaveDescription()==null){//出院情况描述
				taiKangRespEmrAdmitRecInfo.setLeaveDescription("");
			}
			if(taiKangRespEmrAdmitRecInfo.getDischargeOrder()==null){//出院医嘱
				taiKangRespEmrAdmitRecInfo.setDischargeOrder("");
			}
			if(taiKangRespEmrAdmitRecInfo.getMasterDoctor()==null){//经治医生
				taiKangRespEmrAdmitRecInfo.setMasterDoctor("");
			}
			if(taiKangRespEmrAdmitRecInfo.getSuperiorDoctor()==null){//上级医生
				taiKangRespEmrAdmitRecInfo.setSuperiorDoctor("");
			}
			if(taiKangRespEmrAdmitRecInfo.getDischargeMedication()==null){//出院带药
				taiKangRespEmrAdmitRecInfo.setDischargeMedication("");
			}
		}
		return rtnRec;
	}


}














