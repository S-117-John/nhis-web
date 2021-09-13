package com.zebone.nhis.ma.tpi.rhip.support;

import com.alibaba.druid.support.json.JSONUtils;
import com.foxinmy.weixin4j.util.StringUtil;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.emr.rec.rec.*;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;
import com.zebone.nhis.common.module.ma.tpi.rhip.LisResultInfo;
import com.zebone.nhis.common.module.ma.tpi.rhip.PacsRptInfo;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStock;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.emr.common.EmrUtils;
import com.zebone.nhis.ma.tpi.rhip.vo.*;
import com.zebone.platform.framework.support.IUser;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 接口XML创建工具
 * @author chengjia
 *
 */
public class RpDataUtils {
	public static final String nullChar = "#!#";

	//1/住院就诊记录表
	public static IptRecord createIptRecord(IUser pUser,PatListVo pat) throws Exception {
		IptRecord record=new IptRecord();
		record.setName("住院就诊记录表");
		//卡号
		String cardNo=pat.getBarcode();
		record.setKh((cardNo==null||cardNo.equals(""))?"-":cardNo);
		//卡类型
		record.setKlx(pat.getCardType());
		if(pat.getDtIdtype()==null || pat.getDtIdtype().equals("98")) {
			record.setIdCardCode("99");
			record.setIdCard("99");
		}else {
			//身份证件类别代码
			record.setIdCardCode(pat.getDtIdtype());
			//身份证件号码
			record.setIdCard(pat.getIdNo());
		}
		
		//就诊流水号
		record.setJzlsh(pat.getCodePv());
		//病案号
		record.setBzbah(pat.getCodeIp());
		//住院就诊类型
		record.setZyjzlx("400");//普通住院
		//入院科室编码RYKSBM 1010203 其他科室
		record.setRyksbm(pat.getDeptCodeOrig()== null?"1010203":pat.getDeptCodeOrig());
		//出院科室编码CYKSBM
		record.setCyksbm(pat.getDeptCodeOrig()== null?"1010203":pat.getDeptCodeOrig());
		//入院时间RYSJ
		record.setRysj(DateUtils.getDateStr(pat.getDateBegin()));
		//出院时间CYSJ
		Date disDate=pat.getDateEnd();
		if(disDate==null) disDate=new Date();
		record.setCysj(DateUtils.getDateStr(disDate));
		//出院诊断代码CYZD
		record.setCyzd("-");
		//出院诊断名称ZDMC
		String diagDisTxt=pat.getDiagDisTxt();
		if(diagDisTxt==null||diagDisTxt.equals("")) diagDisTxt="-";
		record.setZdmc(diagDisTxt);
		//本人姓名BRXM
		record.setBrxm(pat.getNamePi());
		//病区编码BQBM
		record.setBqbm(pat.getDeptNsCodeOrig());
		//病区名称BQMC
		record.setBqmc(pat.getDeptNsName());
		//性别XB
		String sex=pat.getSexSpcode();
		//sex=getDictCode("rhip.sex",sex);
		record.setXb(sex);
		//婚姻状况
		//record.setHyzk(pat.getMarrySpcode());//getDictCode("rhip.marray",pat.getDtMarry())
		//职业ZY
		record.setZy(pat.getOccuSpcode());//getOccuCode(pat.getDtOccu())
		//民族
		//record.setMz(pat.getNationSpcode());//getNationCode(pat.getDtNation())
		//国籍
		//record.setGj(pat.getCountrySpcode());//getCountryCode(pat.getDtCountry())
		//医保类型
		String yblx=getDictCodeMap("rhip.insu",pat.getInsuCode());
		record.setYblx(yblx);//
		//出生日期
		record.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//床号CH
		record.setCh(pat.getBedNo());
		//出院病区编码CYBQBM
		record.setCybqbm(pat.getDeptNsCodeOrig());
		//病人状态
		String brzt="0";
		if(pat.getFlagIn()!=null&&pat.getFlagIn().equals("0")) brzt="1";//出院
		record.setBrzt(brzt);
		//新生儿标志XSEBZ
		if(pat.getCodeIp().contains("B")) 
		{
			record.setXsebz("1");
			//身份证件类别代码
			if(pat.getDtIdtype()==null || pat.getDtIdtype().equals("")) {record.setIdCardCode("99");}
			else{record.setIdCardCode(pat.getDtIdtype());}
			//身份证件号码
			if(pat.getIdNo()==null || pat.getIdNo().equals("")) {record.setIdCard("无");}
			else{record.setIdCard(pat.getIdNo());}
		}
		else {
			record.setXsebz("0");
		}
		//医疗费用来源类别代码
		String yblb=getDictCodeMapDefault("rhip.yblb",pat.getInsuCode(),"99");
		record.setYblb(yblb);
		//入院诊断代码
		record.setRyzdDm(pat.getAdmissionDiagnosisCode()==null?"-":pat.getAdmissionDiagnosisCode());
		//入院诊断名称
		record.setRyzdMc(pat.getAdmissionDiagnosisName()==null?"-":pat.getAdmissionDiagnosisName());
		//record.setRyzdMc(pat.getDiagAdmitTxt());
		//住院传染性标识代码 1:否,2:是,9:不详
		record.setZycrbs("9");//@todo
		//住院疾病状态代码	1危急2严重3一般4不适用9其他
		record.setZyjbzt("3");//@todo
		//住院发病时间
		record.setZyfbsj(DateUtils.getDateStr(pat.getDateBegin()));//@todo
		//住院确诊时间
		record.setZyqzrq(pat.getDateEnd()==null?DateUtils.getDateStr(pat.getDateBegin()):DateUtils.getDateStr(pat.getDateEnd()));//@todo
		//住院治疗结果代码CV05_10_010 1治愈2好转3稳定4恶化5死亡9其他
		record.setZyzljg("1");//@todo
		//住院原因代码HIS18_01_010       1病伤2体检3分娩9其他
		record.setZyyyDm("1");//@todo
		//是否会诊 1：否；2：是；9：不详
		record.setSfhz("1");//@todo
		//门诊流水号
		record.setMzlsh(pat.getCodePv());
		//入院症状名称
		record.setRyzzMc("-");
		//住院根本死因代码
		record.setZygbsyDm("00");//@todo
		//住院死亡时间
		record.setZyswsj(DateUtils.getDateStrHMS(new Date()));
		//会诊原因
		record.setHzyy("-");
		//会诊意见
		record.setHzyj("-");
		//体格检查记录
		record.setTgjcjl("-");
		//专科情况
		//record.setZkqk("-");
		//最后修改人员代码
		record.setZhxgrydm("-");
		//最后修改人员名称
		record.setZhxgrymc("-");
		//记录人
		record.setJlr("-");
		//记录时间
		record.setJlsj(DateUtils.getDateStrHMS(new Date()));

		return record;
	}

	//2/住院入院记录
	public static IptAdmissionNote createIptAdmissionNote(IUser pUser,PatListVo pat,MedRecVo rec) throws Exception {
		Iterator<Element> it = null;
		Element node=null;
		String nodeName="";
		String paraCodeElement="";
		String xpath="";
		Element nodeText=null;
		String text="";
		IptAdmissionNote note=new IptAdmissionNote();
		note.setName("住院入院记录");
		//就诊流水号
		note.setJzlsh(pat.getCodePv());
		//病案号
		note.setBzbah(pat.getCodeIp());
		//病史陈述者
		note.setBzbscsz("本人");
		//出院31天内再住院标志
		note.setDay31InpatientMk("1");//1无2有@todo
		note.setDay31InpatientAim("无");
		String flagCourseStr=rec.getDocType().getFlagCourse();
		Boolean flagCourse=flagCourseStr==null?false:(flagCourseStr.equals("1")?true:false);
		EmrMedDoc doc =rec.getMedDoc();
		org.w3c.dom.Document document=null;
		if(doc!=null){
			String docXml=doc.getDocXml();
			document=EmrUtils.string2doc(docXml);
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
								if(paraCodeElement.equals("03")){
									//现病史
									note.setBzxbs(text);
								}
								else if(paraCodeElement.equals("04")){
									//既往史
									note.setBzjws(text);
								}
								else if(paraCodeElement.equals("04009")){
									//个人史
									note.setBzgrs(text);
								}
								else if(paraCodeElement.equals("04010")){
									//婚育史
									note.setBzhys(text);
								}
								else if(paraCodeElement.equals("04013")){
									//家族史
									note.setBzjzs(text);
								}
								else if(paraCodeElement.equals("04008")){
									//系统回顾
									note.setBzxthg(text);
								}
								else if(paraCodeElement.equals("04011")){
									//月经史
									note.setBzyjs(text);
								}
								else if(paraCodeElement.equals("04013")){
									//家族史
									note.setBzjzs(text);
								}
								else if(paraCodeElement.equals("07001")){
									//入院初步诊断
									note.setBzcbzd(text);
								}
								else if(paraCodeElement.equals("07004")){
									//修正诊断
									note.setBzxzzd(text);
								}
								else if(paraCodeElement.equals("02022")){
									//辅助检查
									note.setBzfzjcqt(text);
								}
								else if(paraCodeElement.equals("04006")){
									//过敏史
									note.setGms(text);
								}
								else if(paraCodeElement.equals("02")){
									//体格检查
									if(text!=null&&text.length()>1000){
										note.setTgjc(text.substring(0,1000));
									}else{
										note.setTgjc(text);
									}
								}else if(paraCodeElement.equals("1200106")){
									//病历摘要
									note.setBzbszy(text);
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
		}
		//现病史
		if(note.getBzxbs()==null){
			note.setBzxbs("-");
		}
		//既往史
		if(note.getBzjws()==null){
			note.setBzjws("-");
		}
		//个人史
		if(note.getBzgrs()==null){
			note.setBzgrs("-");
		}
		//婚育史
		if(note.getBzhys()==null){
			note.setBzhys("-");
		}
		//家族史
		if(note.getBzjzs()==null){
			note.setBzjzs("-");
		}
		//月经史
		if(note.getBzyjs()==null){
			note.setBzyjs("-");
		}

		/*
		//现病史
		note.setBZXBS("现病史");
		//既往史
		note.setBZJWS("BZJWS");
		//个人史
		note.setBZGRS("个人史");
		//婚育史
		note.setBZHYS("婚育史");
		//家族史
		note.setBZJZS("家族史");
		//系统回顾
		note.setBZXTHG("系统回顾");
		//喂养史
		note.setBZWYS("喂养史");
		//出生史
		note.setBZCSS("出生史");
		//月经史
		note.setBZYJS("月经史");
		//病史摘要
		note.setBZBSZY("病史摘要");
		*/
		//系统回顾
		note.setBzxthg("-");
		//喂养史
		note.setBzwys("-");
		//出生史
		note.setBzcss("-");
		//病史摘要
		if(note.getBzbszy()==null||note.getBzbszy().equals("")) note.setBzbszy("-");

		resolveEmrParaText(rec,note);
		//入院初步诊断
		//note.setBzcbzd("入院初步诊断");
		if(note.getBzcbzd()==null){
			note.setBzcbzd("-");
		}
		//主诉
		if(note.getBzzs()==null){
			note.setBzzs("-");
		}
		//修正诊断
		if(note.getBzxzzd()==null) note.setBzxzzd("-");
		//辅助检查-实验室检查
		note.setBzfzjcsysjc("辅助检查-实验室检查");
		//辅助检查-特殊检查
		note.setBzfzjctsjc("辅助检查-特殊检查");
		//辅助检查-病理检查
		note.setBzfzjcbljc("辅助检查-病理检查");
		//辅助检查-其他
		//note.setBzfzjcqt("辅助检查-其他");
		//与患者关系
		/*if(document!=null){
			String nodeCode="0002090";
			xpath="//NewCtrl[@de_code='"+nodeCode+"']/Content_Text/text()";
			Object tmp = EmrUtils.getXPathElement(document, xpath);
			if(tmp!=null){
				String tmpStr=tmp.toString();
				note.setBzyhzgx(tmpStr);
			}
		}else{
			note.setBzyhzgx("01");//@todo 01本人
		}*/
		note.setBzyhzgx("1");//@todo 01本人
		//住院医生工号
		note.setZyysgh(rec.getReferEmpCode()==null?"-":rec.getReferEmpCode());//
		//住院医生姓名
		note.setZyysxm(rec.getReferEmpName()==null?"-":rec.getReferEmpName());
		//主任医师姓名
		note.setZrysxm(rec.getDirectorEmpName()==null?"-":rec.getDirectorEmpName());
		//主任医生工号
		note.setZrysgh(rec.getDirectorEmpCode()==null?"-":rec.getDirectorEmpCode());
		//主治医师姓名
		note.setZzysxm(rec.getConsultEmpName()==null?"-":rec.getConsultEmpName());
		//就诊医生工号
		note.setJzysgh(rec.getReferEmpCode()==null?"-":rec.getReferEmpCode());
		//主治医师工号
		note.setZzysgh(rec.getConsultEmpCode()==null?"-":rec.getConsultEmpCode());
		//就诊医生姓名
		note.setJzysxm(rec.getReferEmpName()==null?"-":rec.getReferEmpName());
		//入院日期时间
		note.setRyrqsj(DateUtils.getDateStr(pat.getDateBegin()));
		//患者姓名
		note.setHzxm(pat.getNamePi());
		//医保类型
		String yblx=getDictCodeMap("rhip.insu",pat.getInsuCode());
		note.setYblx(yblx);
		//性别代码
		note.setXbdm(pat.getSexSpcode());
		//出生日期
		note.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//婚姻状况
		//note.setHyzk(pat.getMarrySpcode());
		//职业ZY
		note.setZy(pat.getOccuSpcode());
		//note.setMz(pat.getNationSpcode());
		//过敏史
		note.setGms("-");//@todo

		List<IptPhysiqueExam> list=new ArrayList<>();
		IptPhysiqueExam exam=new IptPhysiqueExam();
		String tmpStr="";
		//@todo
		//0002620
		list.add(exam);

		if(document!=null){
			//体温//体温（℃）TW
			String nodeCode="0002620";
			xpath="//NewCtrl[@de_code='"+nodeCode+"']/Content_Text/text()";
			Object tmp = EmrUtils.getXPathElement(document, xpath);
			if(tmp!=null){
				tmpStr=tmp.toString();
				exam.setTw(tmpStr);
				//note.setTw(tmpStr);2.2
			}
			//心率
			nodeCode="0002644";
			xpath="//NewCtrl[@de_code='"+nodeCode+"']/Content_Text/text()";
			tmp = EmrUtils.getXPathElement(document, xpath);
			if(tmp!=null){
				tmpStr=tmp.toString();
				exam.setXl(tmpStr);
				//note.setXl(tmpStr);2.2
			}
			//呼吸频率（次/min）HXPL
			nodeCode="0002218";
			xpath="//NewCtrl[@de_code='"+nodeCode+"']/Content_Text/text()";
			tmp = EmrUtils.getXPathElement(document, xpath);
			if(tmp!=null){
				tmpStr=tmp.toString();
				exam.setHxpl(tmpStr);
				//note.setHxpl(tmpStr);2.2
			}
			//收缩压（mmHg）SSY
			nodeCode="0002489";
			xpath="//NewCtrl[@de_code='"+nodeCode+"']/Content_Text/text()";
			tmp = EmrUtils.getXPathElement(document, xpath);
			if(tmp!=null){
				tmpStr=tmp.toString();
				try {
					int ssy = Integer.parseInt(tmpStr);
				} catch (Exception e) {
					tmpStr = "";
				}
				exam.setSsy(tmpStr);
				//note.setSsy(tmpStr);2.2
			}
			//舒张压（mmHg）SZY
			nodeCode="0002528";
			xpath="//NewCtrl[@de_code='"+nodeCode+"']/Content_Text/text()";
			tmp = EmrUtils.getXPathElement(document, xpath);
			if(tmp!=null){
				tmpStr=tmp.toString();
				try {
					int szy = Integer.parseInt(tmpStr);
				} catch (Exception e) {
					tmpStr = "";
				}
				exam.setSzy(tmpStr);
				//note.setSzy(tmpStr);2.2
			}

		}
//		if(CommonUtils.isEmptyString(exam.getTw())) exam.setTw("0");
//		if(CommonUtils.isEmptyString(exam.getXl())) exam.setXl("0");
//		if(CommonUtils.isEmptyString(exam.getHxpl())) exam.setHxpl("0");
//		if(CommonUtils.isEmptyString(exam.getSsy())) exam.setSsy("0");
//		if(CommonUtils.isEmptyString(exam.getSzy())) exam.setSzy("0");
//		exam.setDay31InpatientMk("1");//1无2有@todo
//		exam.setDay31InpatientAim("-");
//		exam.setTgjc("-");
		//2.2
		if(CommonUtils.isEmptyString(note.getTw())) note.setTw("0");
		if(CommonUtils.isEmptyString(note.getXl())) note.setXl("0");
		if(CommonUtils.isEmptyString(note.getHxpl())) note.setHxpl("0");
		if(CommonUtils.isEmptyString(note.getSsy())) note.setSsy("0");
		if(CommonUtils.isEmptyString(note.getSzy())) note.setSzy("0");
		if(CommonUtils.isEmptyString(note.getTgjc())) note.setTgjc("-");

		/*
		//体温（℃）TW
		exam.setTw("36.5");
		//心率（次/min）XL
		exam.setXl("75");
		//呼吸频率（次/min）HXPL
		exam.setHxpl("20");
		//收缩压（mmHg）SSY
		exam.setSsy("120");
		//舒张压（mmHg）SZY
		exam.setSzy("80");
		list.add(exam);
		*/
		/*
		exam=new IptPhysiqueExam();
		//体温（℃）TW
		exam.setTw("37");
		//心率（次/min）XL
		exam.setXl("76");
		//呼吸频率（次/min）HXPL
		exam.setHxpl("21");
		//收缩压（mmHg）SSY
		exam.setSsy("121");
		//舒张压（mmHg）SZY
		exam.setSzy("81");
		list.add(exam);
		*/

		//note.setIpt_PhysiqueExams(list); /2.0

		return note;
	}

	//3/住院医嘱明细表
	public static IptAdviceDetail createIptAdviceDetail(IUser pUser,PatListVo pat,TOrderVo ord) throws Exception {

		IptAdviceDetail item=new IptAdviceDetail();
		item.setName("住院医嘱明细表");
		//执行时间
		item.setZxsj(DateUtils.getDateStr(ord.getDateStart()));
		//医嘱明细名称
		item.setYzmxmc(ord.getNameOrd());
		//发药数量单位
		//@todo
		String quanStr="1";
		Double quan=ord.getQuan();
		if(quan!=null){
			quan=(double)Math.round(quan*10)/10;
			quanStr=quan.toString();
		}
		if(quanStr.equals("100.0")) quanStr="100";//@todo
		item.setYpsl(quanStr);
		item.setYpdw(ord.getUnitName()==null?"-":ord.getUnitName());
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//销售药品单位
		item.setXsypdw(ord.getUnitName()==null?"-":ord.getUnitName());
		//医嘱ID
		item.setYzid(ord.getOrdsn().toString());
		//医嘱组号
		item.setYzzh(ord.getOrdsnParent().toString());
		//医嘱标识(1长期 2临时 3带药)
		String yzbs="2";
		if(ord.getEuAlways().equals("0")) yzbs="1";
		item.setYzbs(yzbs);
		//医嘱明细编码
		item.setYzmxbm(ord.getCodeOrd());
		if(ord.getFlagDurg().equals("1")){
			//药品通用名
			item.setYptym(ord.getNameOrd());
			//药品商品名
			item.setYpspm(ord.getNameOrd());
			//产地ID	CDID
			item.setCdid("-");
			//产地名称 CDMC
			item.setCdmc("-");
			//药品包装规格系数
			if(ord.getPackSize()!=null){
				item.setYpbzggxs(ord.getPackSize().toString());
			}else{
				item.setYpbzggxs("1");
			}

			//药品包装规格单位
			item.setYpbzggdw(ord.getUnitName());
		}else{
			item.setYpbzggxs("-");
			item.setYpbzggdw("-");
			//药品通用名
			item.setYptym("-");
			//药品商品名
			item.setYpspm("-");
			//产地ID	CDID
			item.setCdid("-");
			//产地名称 CDMC
			item.setCdmc("-");
		}

		if(item.getYpbzggdw()==null) item.setYpbzggdw("-");
		//是否药品
		item.setSfyp(ord.getFlagDurg());
		//药品规格
		item.setYpgg(ord.getSpec()==null?"-":ord.getSpec());
		//发药数量
		item.setYpsl(ord.getQuan().toString());
		//药品用法
		//ord.getCodeSupply()
		item.setYpyf(ord.getSupplySpcode()==null?"176":ord.getSupplySpcode());
		//用药频度
		String freq=ord.getCodeFreq().toUpperCase();
		if(freq.toUpperCase().equals("ONCE")){
			freq="ST";
		}
		item.setYypd(freq);//@todo待验证
		//药物副作用
		item.setYwfzy("无");
		if(ord.getFlagDurg().equals("1")){
			if(ord.getOrdsn().intValue()==ord.getOrdsnParent().intValue()){
				item.setZybzdm("2");
			}else{
				item.setZybzdm("1");
			}
			//每次使用剂量
			item.setMcsyjl(ord.getDosage().toString());
			//每次使用剂量单位
			item.setMcsyjldw(ord.getUnitNameDos());
			//每次使用数量
			item.setMcsysl(ord.getQuan().toString());
			//每次使用数量单位
			item.setMcsysldw(ord.getUnitName());
			//主副药标识
			item.setZfybs("1");//@todo待验证
			//给药途径代码
			String supplyCate=ord.getCodeSupply();
			if(CommonUtils.isEmptyString(supplyCate)) supplyCate="9";
			supplyCate="9";
			item.setTjdm(ord.getSupplyCate());//ord.getSupplySpcode()@todo
			//中药煎煮法代码
			item.setZyjzfdm("-");
			//用药天数
			if(ord.getDays()!=null){
				item.setYyts(ord.getDays().toString());
			}else{
				item.setYyts("1");
			}
			//药品有效成分数量
			item.setYpyxcfsl("-");
			//药品有效成分单位
			item.setYpyxcfdw("-");
			//用药目的
			String yymd="9";
			if(ord.getFlagPrev()!=null&&ord.getFlagPrev().equals("1")){
				yymd="1";
			}else if(ord.getFlagThera()!=null&&ord.getFlagThera().equals("1")){
				yymd="3";
			}else if(ord.getFlagEmer()!=null&&ord.getFlagEmer().equals("1")){
				yymd="2";
			}
			item.setYymd(yymd); // //1、预防  2、应急   3. 治疗     9.其他
//			item.setYpdm(ord.getCodeOrd());
//			item.setYpmc(ord.getNameOrd());

		}else{
			item.setMcsyjl("0");
			item.setMcsyjldw("-");
			item.setMcsysl("1");
			item.setMcsysldw("-");
			item.setZfybs("1");
			item.setTjdm("9");//CV06_00_102
			item.setZyjzfdm("-");
			item.setYyts("1");
			item.setYpyxcfsl("-");
			item.setYpyxcfdw("-");
			item.setYymd("9");
			item.setZybzdm("9");
		}
		item.setYpdm(ord.getCodeOrd());
		item.setYpmc(ord.getNameOrd());

		//开单医生工号
		item.setKdysgh(ord.getEmpCode()==null?"-":ord.getEmpCode());
		//开单医生姓名
		item.setKdysxm(ord.getNameEmpOrd()==null?"-":ord.getNameEmpOrd());
		//开单科室代码
		item.setKdksdm(ord.getDeptCodeOrig()==null?"020204":ord.getDeptCodeOrig());
		//开单医生职称
		item.setKdyszc("-");
		//开单时间
		item.setKdsj(DateUtils.getDateStr(ord.getDateEnter()));
		//执行人员工号
		item.setZxrygh(ord.getEmpCodeChk()==null?"-":ord.getEmpCodeChk());
		//执行人员姓名
		String zxryxm=ord.getNameEmpChk();
		if(zxryxm==null||zxryxm.equals("")) zxryxm="-";
		item.setZxryxm(zxryxm);
		//执行科室代码（其他）
		item.setZxksdm(ord.getDeptCodeOrig()==null?"3060700":ord.getDeptCodeOrig());
//		item.setZxksdm(ord.getDeptCodeExecOrig()==null?ord.getDeptCodeOrig():ord.getDeptCodeExecOrig());
		//检查部位ACR编码
		item.setJcbwacrbm("-");
		//单价
		if(ord.getPriceCg()!=null){
			item.setDj(ord.getPriceCg().toString());
		}else{
			item.setDj("0");
		}
		//总额
		item.setZe("0");
		//终止时间
		item.setZzsj(DateUtils.getDateStrHMS(ord.getDateStop()==null?ord.getDateStart():ord.getDateStop()));//yyyy-mm-dd hh:mm:ss
		//给药途径名称
		item.setTjmc(ord.getSupplySpcode() == null ? "":ord.getSupplySpcode());
//		item.setTjmc(ord.getSupplySpcode()); //old
		//药物剂型名称
		item.setYwjxmc("-");//@todo
		//药物剂型代码
		item.setYwjxdm("CL"); //@todo对照
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//医嘱项目类型编码
		//1 西药/中成药 2中草药 3注射类药 4检验类 5检查类 6 手术类 7输血类医嘱 8处置类医嘱 9 料类医嘱  10 嘱托医嘱   99其他医嘱
		String orderType=getDictCodeMap("rhip.ordtype",ord.getCodeOrdtype());
		if(orderType==null||orderType.equals("")) orderType="99";
		item.setYzxmlxbm(orderType);
		//医嘱项目类型名称
		//item.setYzxmlxmc("-");
		//药品批准文号
		item.setYppzwh("-");
		//药品统一编码
		item.setYptybm("-");
		//药品有效期
		item.setYpyxq("-");

		//住院诊断流水号
		item.setZyzdlsh("-");
		//住院病案号
		item.setZybah(pat.getCodeIp());
		//病区名称
		item.setBqmc(pat.getDeptNsName());
		//病房号
		item.setBfh(pat.getDeptNsName());
		//病床号
		item.setBch(pat.getBedNo());
		//科室编码
		item.setKsbm(pat.getDeptNsCodeOrig());
		//科室名称
		item.setKsmc(pat.getDeptName());
		//医嘱备注信息
		item.setYzbzxx("-");
		//药物类型
		item.setYwlxDm("9999");//@todo
		//医嘱审核者
		item.setYzshz(ord.getNameEmpOrd()==null?"-":ord.getNameEmpOrd());
		//医嘱审核时间
		item.setYzshsj(DateUtils.getDateStrHMS(ord.getDateChk()==null?ord.getDateEnter():ord.getDateChk()));
		//核对医嘱护士
		item.setHdyzhs(ord.getNameEmpChk()==null?"-":ord.getNameEmpChk());
		//医嘱核对时间
		item.setYzhdrq(DateUtils.getDateStrHMS(ord.getDateChk()==null?ord.getDateEnter():ord.getDateChk()));
		//医嘱执行状态
		item.setYzzxzt("-");
		//取消医嘱者
		item.setQxyzz(ord.getNameEmpErase()==null?"-":ord.getNameEmpErase());
		//医嘱取消时间
		item.setYzqxrq(DateUtils.getDateStrHMS(ord.getDateErase()==null?new Date():ord.getDateErase()));
		//停止医嘱者
		item.setTzyzz(ord.getNameEmpStop() == null ? "":ord.getNameEmpStop());
		//服务项目代码
		item.setFwxmdm(ord.getCodeOrd());
		//服务项目名称
		item.setFwxmmc(ord.getNameOrd());
		//中药使用类别代码//CV06_00_101中药类别代码字典/1未使用2中成药3中草药9其他中药
		String zylb="1";
		if(ord.getCodeOrdtype()!=null){
			if(ord.getCodeOrdtype().equals("0101")){
				zylb="1";
			}else if(ord.getCodeOrdtype().equals("0102")){
				zylb="2";
			}else if(ord.getCodeOrdtype().equals("0103")){
				zylb="3";
			}
		}
		item.setZysylbDm(zylb);
		item.setZylbdm(zylb);
		//组号
		item.setZh(ord.getGroupno()==null?"0":ord.getGroupno());
		//皮试0否1是
		String euSt=ord.getEuSt();
		String flag="0";
		if(euSt==null||euSt.equals("")||euSt.equals("0")){
			flag="0";
		}else{
			flag="1";
		}
		item.setSfpsbs(flag);
		//总剂量单位
		item.setZjldw("-");//@todo
		//总剂量单位
		item.setZjl("0");
		//药物规格
		if (ord.getSpec() == null) {
			item.setYwgg("");
		}else {
			item.setYwgg(ord.getSpec());

		}
		//给药途径
		String supplyCate=ord.getCodeSupply();
		//String tjdm=getDictCodeMapDefault("rhip.supplycate",supplyCate,"9");
		if(CommonUtils.isEmptyString(supplyCate)) supplyCate="9";
		supplyCate="9";
		item.setTjdm(supplyCate);
		//医嘱项目明细ID
		item.setYzxmmxid(ord.getCodeOrd());
		return item;
	}

	//4/住院体温单
	public static IptSignsRecord createIptSignsRecord(IUser pUser,PatListVo pat,RhipExVtsOccDtVo dt) throws Exception {

		ExVtsOcc occ=dt.getOcc();
		if(occ==null) return null;

		IptSignsRecord item=new IptSignsRecord();
		item.setName("住院体温单");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//病案号
		item.setBzbah(pat.getCodeIp());
		item.setTwdlsh(dt.getPkVtsoccdt());
		//记录时间
		item.setJlsj(DateUtils.getDateStr(occ.getDateVts()));	//@todo日期+时刻
		//体温
		if(dt.getValTemp()!=null) {//修改
			item.setTw(dt.getValTemp().toString());
		}else {
			item.setTw("");
		}
//		if(dt.getValTemp()!=null) item.setTw(dt.getValTemp().toString());
		//收缩压（mmHg）
//		if(occ.getValSbp()!=null) item.setSsy(occ.getValSbp().toString());
		if(occ.getValSbp()!=null) {//修改
			item.setSsy(occ.getValSbp().toString());
		}else {
			item.setSsy("");
		}

		//舒张压（mmHg）
//		if(occ.getValDbp()!=null) item.setSzy(occ.getValDbp().toString());
		if(occ.getValDbp()!=null) {
			item.setSzy(occ.getValDbp().toString());
		}else {
			item.setSzy("");
		}
		//空腹血糖值（mmol/L）
		//餐后血糖
		//身高（cm）
		item.setSg("0");//@todo
		//体重（kg）
		if(occ.getValWeight()!=null) item.setTz(occ.getValWeight().toString());
		if(item.getTz()==null) item.setTz("0");
		//呼吸频率（次/min）
		if(dt.getValBre()!=null) item.setHxpl(dt.getValBre().toString());
		if(dt.getValBre()==null) item.setHxpl("0");
		//心率（次/min）
		if(dt.getValHr()!=null) item.setXl(dt.getValHr().toString());
		if(item.getXl()==null) item.setXl("0");
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//入科时间
		item.setRksj(DateUtils.getDateStr(pat.getDateBegin()));
		//记录者工号
		item.setJlzgh(dt.getCreator());//@todo
		////记录者姓名
		item.setJlrxm(dt.getCreator());//@todo
		// 出院日期
		item.setCyrq(DateUtils.getDateStr(pat.getDateEnd()));
		return item;
	}

	//5/住院出院记录
	public static IptLeaveRecord createIptLeaveRecord(IUser pUser,PatListVo pat,MedRecVo rec) throws Exception {

		IptLeaveRecord item=new IptLeaveRecord();
		item.setName("住院出院记录");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//病案号
		item.setBzbah(pat.getCodeIp());
		//门诊诊断
		item.setMzzd("-");//@todo待增加
		//出院记录
		resolveEmrParaTextDis(rec,item);
		//科室编码
		item.setKsbm(pat.getDeptCodeOrig());
		//住院天数
		Integer days=pat.getInHosDays();
		item.setZyts(days.toString());
		//住院医生工号
		item.setZyysgh(rec.getReferEmpCode()==null?"-":rec.getReferEmpCode());
		//住院医师姓名
		item.setZyysxm(pat.getReferName()==null?"-":pat.getReferName());
		//主治医师姓名
		item.setZzysxm(pat.getConsultName()==null?"-":pat.getConsultName());
		//主治医师工号
		item.setZzysxm(rec.getConsultEmpCode()==null?"-":rec.getConsultEmpCode());
		//主任医师姓名
		item.setZzysxm(pat.getDirectorName()==null?"-":pat.getDirectorName());
		//主任医师工号
		//item.setZrysgh(rec.getDirectorEmpCode()==null?"-":rec.getDirectorEmpCode());2.0
		//就诊医师姓名
		//item.setJzysxm(rec.getReferEmpName()==null?"-":rec.getReferEmpName());2.0
		//就诊医师工号
		//item.setJzysgh(rec.getReferEmpCode()==null?"-":rec.getReferEmpCode());2.0
		//出院时间
		if(pat.getDateEnd()!=null) item.setCysj(DateUtils.getDateStr(pat.getDateEnd()));
		//记录时间
		Date recDate=rec.getRecDate();
		if(recDate==null) recDate=rec.getCreateTime();
		String recDateStr=DateUtils.getDateStrHMS(recDate);
		item.setJlsj(recDateStr);
		//入院时间
		if(pat.getDateBegin()!=null) item.setRysj(DateUtils.getDateStr(pat.getDateBegin()));
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//婚姻状况
		//item.setHyzk(pat.getMarrySpcode());
//		//职业
//		item.setZy(getOccuCode(pat.getDtOccu()));
		//出院诊断-主要诊断-疾病编码(主要诊断编码)
		item.setMasterDiseaseCode(pat.getDiagCode());
		//出院诊断-主要诊断-出院情况(主要诊断名称)
		item.setMasterDiseaseName(pat.getDiagName());
//		item.setMasterDiseaseName(item.getCyzd());
		//出院诊断-主要诊断-入院病情/CC09_00_104_00/1有明确诊断2临床未确定3情况不明4无法确定
		item.setMasterAdmissionCondition("1");
		//离院方式/CVX_LeaveHospitalType/1医嘱离院2医嘱转院3医嘱转社区卫生服务机构/乡镇卫生院4非医嘱离院5死亡9其他
		item.setDischargeMethods(pat.getLeaveHosCode()==null?"1":pat.getLeaveHosCode());
		item.setKsbm(pat.getDeptCodeOrig()==null?"-":pat.getDeptCodeOrig());
		item.setKsmc(pat.getDeptName());
		item.setDischargeSickRoom(pat.getDeptNsName());
		//出院情况
		item.setCyqk(item.getCyqk()==null?"-":item.getCyqk());
		//入院情况
		item.setRyqk(item.getRyqk()==null?"-":item.getRyqk());
		//入院诊断
		item.setRyzd(item.getRyzd()==null?"-":item.getRyzd());
		//出院诊断
		item.setCyzd(item.getCyzd()==null?"-":item.getCyzd());
		
		return item;
	}

	//6住院病案首页
	public static IptMedicalRecordPage createIptMedicalRecordPage(IUser pUser,PatListVo pat,EmrHomePage page) throws Exception {
		IptMedicalRecordPage item=new IptMedicalRecordPage();
		item.setName("住院病案首页");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//居民健康卡号
		String healthCardNo=page.getHealthCardNo();
		if(healthCardNo==null||healthCardNo.equals("")) healthCardNo="-";
		item.setHealthCardID(healthCardNo);
		//住院号
		item.setHospizationID(pat.getCodeIp());
		//病案号
		item.setMedicalRecordID(pat.getCodeIp());
		//病理号
		item.setPathologyID(page.getPathoNo()==null?"-":page.getPathoNo());
		//卡号
		item.setKh(pat.getBarcode()==null?"-":pat.getBarcode());//@todo
		//卡类型
		item.setKlx(pat.getCardType());//@todo 01	医保 	02	就诊卡		99	其他		03	健康卡
		//患者姓名
		item.setXm(page.getName());
		//性别
		item.setSex(pat.getSexSpcode());
		/*//年龄
		item.setNl(page.getAgeTxt());*/
		//婚姻状况代码
		item.setHyzkdm(pat.getMarrySpcode());
		//职业类别代码
		item.setZylbdm(pat.getOccuSpcode());
		//国籍
		//item.setGj(pat.getCountrySpcode());
		//出生日期时间
		item.setCsrqsj(DateUtils.getDateStr(page.getBirthDate()));
		//ABO血型代码
		String abo=page.getBloodCodeAbo();
		if(abo==null||abo.equals("")) abo="%";
		//item.setAboxx(getAboCode(abo));
		item.setAboxx(pat.getAboSpcode()==null?"%":pat.getAboSpcode());
		//Rh血型代码
		item.setRhxx(pat.getRhSpcode()==null?"%":pat.getRhSpcode());//getRhCode(page.getBloodCodeRh())
		//籍贯-省（区、市）
		item.setOriginProvince(page.getOriginAddrProv()==null?"-":page.getOriginAddrProv());
		//籍贯-市
		item.setOriginCity(page.getOriginAddrCity()==null?"-":page.getOriginAddrCity());
		//现住址-省（区、市
		item.setPresentAddrProvince(page.getCurrAddrProv()==null?"-":page.getCurrAddrProv());
		//现住址-市
		item.setPresentAddrCity(page.getCurrAddrCity()==null?"-":page.getCurrAddrCity());
		//现住址-县
		item.setPresentAddrCounty(page.getCurrAddrCounty()==null?"-":page.getCurrAddrCounty());
		//现住址-其它
		item.setPresentAddrOther(page.getCurrAddr()==null?"-":page.getCurrAddr());
		//现住址-邮编
		item.setPresentAddrPostalcode(page.getCurrZipCode()==null?"000000":page.getCurrZipCode());
		//医疗付费方式
		item.setMedicalPayment(getMedPayMode(page.getMedPayMode()));
		//损伤中毒的外部原因-疾病编码
		item.setOutsideReasonOfInjuryAndPoCode(page.getDiagCodeExtcIp()==null?"-":page.getDiagCodeExtcIp());
		//出院诊断-主要诊断-疾病编码
		item.setMasterDiseaseCode(page.getDiagCodeDis()==null?"-":page.getDiagCodeDis()) ;
		//出院诊断-主要诊断-疾病名称
		item.setMasterDiseaseName(page.getDiagNameDis());
		//出院诊断-主要诊断-入院病情//@todo
		String admitCondCode=page.getAdmitCondCode();
		if(admitCondCode==null||admitCondCode.equals("")) admitCondCode="1";
		item.setMasterAdmissionCondition(admitCondCode);
		//出院中医诊断-主病名称
		item.setCyzyzdzbmc("-");
		//出院中医诊断-主病编码
		item.setCyzyzdzbbm("-");
		//出院中医诊断－主病－入院病情代码
		item.setCyzyzdrybq("1");//@todo
		//出院中医诊断-主证名称
		item.setCyzyzdzzmc("-");
		//出院中医诊断-主证编码
		//CYZYZD_ZZBM
		item.setCyzyzdzzbm("-");
		//出院中医诊断－主证－入院病情代码
		item.setCyzyzdzzrybq("1");//@todo
		//病理诊断-疾病编码
		item.setPathologyDiseaseCode(page.getDiagCodePatho()==null?"-":page.getDiagCodePatho());
		//病理诊断-疾病名称
		item.setPathologyDiseaseName(page.getDiagNamePatho()==null?"-":page.getDiagNamePatho());
		//门(急)诊诊断(西医诊断)-疾病编码
		item.setClinicWesternDiagnosisCode(page.getDiagCodeClinic()==null?"-":page.getDiagCodeClinic());
		//门（急）诊诊断名称
		item.setClinicDiagnosis(page.getDiagNameClinic()==null?"-":page.getDiagNameClinic());
		//门（急）诊诊断（中医诊断）	//@todo
		item.setMjzzdzyzdmc("-");	//@todo
		//门（急）诊诊断（中医诊断）病名编码	//@todo
		item.setMjzzdzyzdbm("-");	//@todo
		//门（急）诊诊断（中医证候）
		item.setMjzzdzyzhmc("-");//@todo
		//门（急）诊诊断（中医证候）证候编码
		item.setMjzzdzyzhbm("-");//@todo
		//使用医疗机构中药制剂标志SYZYZJBZ
		item.setSyzyzjbz("0");
		//使用中医诊疗设备标志SYZYSBBZ
		item.setSyzysbbz("0");
		//使用中医诊疗技术标志SYZYJSBZ
		item.setSyzyjsbz("0");
		//辨证施护标志BZSHBZ
		item.setBzshbz("0");
		//月龄MonthsAge
		item.setMonthsAge("-");
		//新生儿出生体重NeonatalBirthWeight
		item.setNeonatalBirthWeight("-");
		//新生儿入院体重NeonatalAdmissionWeight
		item.setNeonatalAdmissionWeight("-");
		//出生地-省（区、市）
		item.setProvince(page.getBirthAddrProv()==null?"-":page.getBirthAddrProv());
		//出生地-市
		item.setCity(page.getBirthAddrCity()==null?"-":page.getBirthAddrCity());
		//出生地-县
		item.setCounty(page.getBirthAddrCounty()==null?"-":page.getBirthAddrCounty());
		//*患者电话号码
		item.setPatientPhone(page.getContactPhone());
		//户口地址-省（区、市）
		item.setRegisteredPermanentResidencePr(page.getResideAddr()==null?"-":page.getResideAddr());
		//户口地址-市
		item.setRegisteredPermanentResidenceCi(page.getResideAddrCity()==null?"-":page.getResideAddrCity());
		//户口地址-县
		item.setRegisteredPermanentResidenceTo(page.getResideAddrCounty()==null?"-":page.getResideAddrCounty());
		//户口地址-其它
		item.setRegisteredPermanentResidenceOt(page.getResideAddr()==null?"-":page.getResideAddr());
		//户口地址-邮编
		item.setRegisteredPermanentResidencePo(page.getResideZipCode()==null?"-":page.getResideZipCode());
		//工作单位电话号码
		item.setWorkAddrPhone(page.getWorkUnitPhone()==null?"-":page.getWorkUnitPhone());
		//入院途径
		item.setAdmissionWay(page.getAdmitPathCode()==null?"-":page.getAdmitPathCode());
		//出院日期时间//@todo
		Date disTime=page.getDisTime();
		if(disTime==null) disTime=new Date();
		item.setDischargeDateTime(DateUtils.getDateStr(disTime));
		//出院病房
		item.setDischargeSickRoom(pat.getDeptNsCodeOrig()==null?"-":pat.getDeptNsCodeOrig());
		//实际住院天数
		item.setActualHospitalizationDays(page.getInHosDays()==null?"0":page.getInHosDays().toString());
		//出院诊断-主要诊断-出院情况MasterDischargePrognosisCondit
		item.setMasterDischargePrognosisCondit("1");
		//损伤中毒的外部原因
		item.setOutsideReasonOfInjuryAndPoison(page.getDiagCodeExtcIp()==null?"-":page.getDiagCodeExtcIp());
		//药物过敏(1:无2有)
		item.setDrugAllergy(page.getFlagDrugAllergy()==null?"1":page.getFlagDrugAllergy());
		//过敏药物
		item.setAllergicDrug(page.getAllergicDrug()==null?"-":page.getAllergicDrug());
		//入院日期时间
		item.setAdmissionDateTime(DateUtils.getDateStr(page.getAdmitTime()));
		//入院病房
		item.setAdmissionSickroom(pat.getDeptNsCodeAdmitOrig()==null?"-":pat.getDeptNsCodeAdmitOrig());
		//死亡患者尸检标志
		String flagAutopsy=getAutopsyCode(page.getFlagAutopsy());
		item.setAutopsySign(flagAutopsy);
		//病案质量
		item.setMedicalRecordQuality(page.getQualityCode()==null?"1":page.getQualityCode());
		//质控日期
		Date qcDate=page.getQcDate()==null?page.getDisTime():page.getQcDate();
		if(qcDate==null) qcDate=new Date();
		item.setQualityControlDate(DateUtils.getDateStrHMS(qcDate));
		//离院方式//@tdo
		String leaveHosCode=page.getLeaveHosCode();
		if(leaveHosCode==null||leaveHosCode.equals("")) leaveHosCode="1";//医嘱
		item.setDischargeMethods(leaveHosCode);
		//拟接受医疗机构名称
		item.setPrepareAcceptHospitalName(page.getReceiveOrgName()==null?"-":page.getReceiveOrgName());
		//出院31 天内再住院标志
		item.setDay31InpatientMk(page.getFlagReadmit()==null?"1":page.getFlagReadmit());
		//出院 31 天内再住院目的
		item.setDay31InpatientAim(page.getReadmitPurp()==null?"-":page.getReadmitPurp());
		//颅脑损伤患者入院前昏迷时间//@todo
		if(page.getComaMinBef()!=null) item.setStuporTimeBeforeAdmission(page.getComaMinBef().toString());
		if(item.getStuporTimeBeforeAdmission()==null) item.setStuporTimeBeforeAdmission("0");
		//颅脑损伤患者入院后昏迷时间//@todo
		if(page.getComaMinAfter()!=null) item.setStuporTimeAfterAdmission(page.getComaMinAfter().toString());
		if(item.getStuporTimeAfterAdmission()==null) item.setStuporTimeAfterAdmission("0");
		//诊断符合情况-门诊和出院//0未做1符合2不符合3不确定@todo
		//诊断符合情况-入院和出院
		//诊断符合情况-术前和术后
		//诊断符合情况-临床和病理
		item.setDiagCoinClinicalVsPathologica("");
		//诊断符合情况-放射和病理
		item.setDiagCoinRadiologyVspathology("");
		//抢救情况-抢救次数
		if(page.getNumRes()!=null) item.setSalvageConditionSalvageTimes(page.getNumRes().toString());
		//抢救情况-成功次数
		if(page.getNumSucc()!=null) item.setSalvageConditionSuccessTimes(page.getNumSucc().toString());
		//临床路径管理
		item.setClinicalPathManagement(page.getFlagCp()==null?"0":page.getFlagCp());
		//主治医师签名
		item.setAttendingDoctor(page.getConsultName()==null?"-":page.getConsultName());
		//住院医师签名
		item.setHospizationDoctor(page.getReferName()==null?"-":page.getReferName());
		//责任护士签名
		item.setResponsibilityNurse(page.getNurseName()==null?"-":page.getNurseName());
		//进修医师签名
		item.setRefresherDoctors(page.getLearnName()==null?"-":page.getLearnName());
		//实习医师签名
		item.setInternDoctor(page.getInternName()==null?"-":page.getInternName());
		//病案编码员签名
		item.setMedicalRecordCoderSign(page.getCoderName()==null?"-":page.getCoderName());
		//质控医师签名
		item.setQualityControlDoctor(page.getQcDocName()==null?"-":page.getQcDocName());
		//质控护士签名
		item.setQualityControlSign(page.getQcNurseName()==null?"-":page.getQcNurseName());
		//转科科室名称
		item.setTransferredDeptName(page.getTransDeptNames());
		//出院科室名称
		item.setDischargeDepartment(page.getDisDeptName()==null?"-":page.getDisDeptName());
		//入院科室名称
		item.setAdmissionDeptName(page.getAdmitDeptName()==null?"-":page.getAdmitDeptName());
		//费用
		setPageCharges(item,page.getCharges());
		//科主任签名
		item.setDeptManager(page.getChiefName()==null?"-":page.getChiefName());
		//主任（副主任）医师签名
		item.setChiefDoctorSign(page.getDirectorName()==null?"-":page.getDirectorName());
		//住院总费用-自付金额（元）
		if(page.getSelfCost()!=null) {
			item.setHospizationTotalPersonalCost(page.getSelfCost().toString());
		}else{
			item.setHospizationTotalPersonalCost("0");
		}
		//住院总费用（元）
		if(page.getTotalCost()!=null){
			item.setHospizationTotalCost(page.getTotalCost().toString());
		}else{
			item.setHospizationTotalCost("0");
		}
		//民族
		item.setMz(pat.getNationSpcode());
		//住院次数（次）
		item.setZycs(page.getTimes().toString());
		//工作单位名称
		item.setGzdwmc(page.getWorkUnit());
		//工作单位地址
		item.setGzdwdz(page.getWorkUnit());
		//工作地点邮编
		item.setGzddyb(page.getWorkUnitZipCode());
		//联系人姓名
		item.setLxrxm(page.getContactName());
		//联系人与患者关系代码
		item.setLxryhzgx(page.getContactRelatCode());
		//联系人地址
		item.setLxrdz(page.getContactAddr());
		//联系人电话
		item.setLxrdh(page.getContactPhone());
		//工作单位电话号码
		item.setGzdwdhhm(page.getWorkUnitPhone());
		//现住址-代码（居委会、村委会）
		item.setXzzDm(page.getCurrAddrCode()==null?"-":page.getCurrAddrCode());
		//检查/检验结果代码
		//检查/检验定量结果
		//住院者疾病状态代码
		//其他医学处置
		//检查/检验类别
		//药物类型
		//症状名称
		//医疗机构名称
		item.setYljgmc("中山市博爱医院");
		//身份标识类别代码
		item.setSfbsDm("01");//@todo
		item.setSfbshm(pat.getIdNo());
		//HBsAg检查结果代码/1阴性2阳性9不详
		item.setHbsagjcjgDm("9");
		//HCV_Ab检查结果代码
		item.setHcvabjcjgDm("9");
		//HIV_Ab检查结果代码
		item.setHivabjcjgDm("9");
		//病案质量评定代码 甲乙丙
		item.setBazlpdDm("1");
		//转诊医生工号
		item.setZzysgh("-");
		//住院医生工号
		item.setZyysgh("-");
		//进修医师工号
		item.setJxysgh("-");
		//责任护士工号
		item.setZrhsgh("0");//2.0
		//实习医师工号
		item.setSxysgh("-");
		//编码员工号
		item.setBmygh("-");
		//质控医师工号
		item.setZkysgh("-");
		//质控护士工号
		item.setZzkhsbhkysgh("-");
		//出院科别编码
		item.setCykbbm("=");
		//入院科别编码
		item.setRykbbm("-");
		//入院时情况代码HIS18_01_014/1危重2急诊3一般4不适用5其他
		item.setRysqkdm("3");//@todo
		//住院患者入院病情
		item.setZyhzrybq("-");
		//科主任工号
		item.setKzrgh("-");
		//主任医师工号
		item.setZrysgh("-");//@todo
		//：A一般，B急，C疑难,D危重
		item.setBlfx("A");//@TODO
		//机构组织机构代码/检查/检验类别/01：检验，02：检查
		item.setJcjylb("0");
		//医疗费用结算方式代码/HIS18_01_002/
		//01现金02支票03汇款存款04内部转账05单位记账06账户金07	统筹金08	银行卡09	微信支付10支付宝支付11健康中山APP支付99其他
		item.setYlfyjsfsDm("99");//@todo
		//病历分析
		if(page.getOr()!=null){
			EmrHomePageOr or=page.getOr();
			//肿瘤分期类型
			//item.setZlfqlx(or.getStageTypeCode());
			//肿瘤分期
			item.setZlfq(or.getStageName());
			//肿瘤T分期
			item.setZltfq(or.getStageT());
			//肿瘤N分期
			item.setZlnfq(or.getStageN());
			//肿瘤M分期
			item.setZlmfq(or.getStageM());
			//肿瘤放疗方式编码
			item.setZlflfsbm(or.getRadiothModeCode());
			//肿瘤放疗方式名称
			item.setZlflfsmc(or.getRadiothModeName());
			//肿瘤放疗程式编码
			item.setZlflcsbm(or.getRadiothProgrCode());
			//肿瘤放疗程式名称
			item.setZlflcsmc(or.getRadiothProgrName());
			//肿瘤放疗装置编码
			item.setZlflzzbm(or.getRadiothDeviceCode());
			//肿瘤放疗装置名称
			item.setZlflzzmc(or.getRadiothDeviceName());
			//原发灶剂量
			if(or.getPriDose()!=null) item.setYfzjl(or.getPriDose().toString());
			//原发灶次数
			if(or.getPriTimes()!=null) item.setYfzcs(or.getPriTimes().toString());
			//原发灶天数
			if(or.getPriDays()!=null) item.setYfzts(or.getPriDays().toString());
			//原发灶开始日期
			item.setYfzkssj(DateUtils.getDateStr(or.getPriDateBegin()));
			//原发灶结束时间
			item.setYfzjssj(DateUtils.getDateStr(or.getPriDateEnd()));
			//区域淋巴结剂量
			if(or.getLymDose()!=null) item.setQylbjjl(or.getLymDose().toString());
			//区域淋巴结次数
			if(or.getLymTimes()!=null) item.setQylbjcs(or.getLymTimes().toString());
			//区域淋巴结天数
			if(or.getLymDays()!=null) item.setQylbjts(or.getLymDays().toString());
			//区域淋巴结开始时间
			item.setQylbjkssj(DateUtils.getDateStr(or.getLymDateBegin()));
			//区域淋巴结结束时间
			item.setQylbjjssj(DateUtils.getDateStr(or.getLymDateEnd()));
			//转移灶名称
			item.setZyzmc(or.getMetName());
			//转移灶剂量
			if(or.getMetDose()!=null) item.setZyzjl(or.getMetDose().toString());
			//转移灶次数
			if(or.getMetTimes()!=null) item.setZyzcs(or.getMetTimes().toString());
			//转移灶天数
			if(or.getMetDays()!=null) item.setZyzts(or.getMetDays().toString());
			//转移灶开始时间
			item.setZyzkssj(DateUtils.getDateStr(or.getMetDateBegin()));
			//转移灶结束时间
			item.setZyzjssj(DateUtils.getDateStr(or.getMetDateBegin()));
			//化疗方式编码
			item.setHlfsbm(or.getChemModeCode());
			//化疗方式名称
			item.setHlfsmc(or.getChemModeName());
			//化疗方法编码
			item.setHlffbm(or.getChemMethodCode());
			//化疗方法名称
			item.setHlffmc(or.getChemMethodName());
			//化疗药物
			if(page.getOrdts()!=null&&page.getOrdts().size()>0){
				List<IPTDrugRecord> drugs=new ArrayList<IPTDrugRecord>();
				for (EmrHomePageOrDt ordt : page.getOrdts()) {
					IPTDrugRecord drug=new IPTDrugRecord();
					//化疗日期
					drug.setHlrq(DateUtils.getDateStr(ordt.getBeginDate()));
					//化疗药物名称
					drug.setHlywmc(ordt.getDrugName());
					//化疗药物剂量
					//化疗疗程
					drug.setHllc(ordt.getCourseName());
					//化疗疗效
					drug.setHllx(ordt.getEffectName());

					drugs.add(drug);
				}
				item.setIPT_DrugRecords(drugs);
			}
		}

		//婴儿记录
		if(page.getBrs()!=null&&page.getBrs().size()>0){
			List<IPTBabyRecord> babys=new ArrayList<IPTBabyRecord>();
			for (EmrHomePageBr br : page.getBrs()) {
				IPTBabyRecord baby=new IPTBabyRecord();
				//分娩结果
				baby.setFmjg(br.getNameCb());
				//抢救次数
				if(br.getNumRes()!=null) baby.setYeqjcgcs(br.getNumRes().toString());
				//抢救成功次数
				if(br.getNumSucc()!=null) baby.setYeqjcs(br.getNumSucc().toString());
				//婴儿体重
				if(br.getWeight()!=null) baby.setYetz(br.getWeight().toString());
				//婴儿转归
				baby.setYezg(br.getCodeOc());
				//呼吸
				baby.setYehx(br.getCodeBreath());
				//婴儿性别
				baby.setYexb(getDictCodeMap("rhip.sex",br.getDtSex()));

				babys.add(baby);
			}
			item.setIPT_BabyRecords(babys);
		}

		//出院诊断-其他诊断
		if(page.getDiags()!=null&&page.getDiags().size()>0){
			List<IptLeaveDiagnosisNew> diags=new ArrayList<IptLeaveDiagnosisNew>();
			for (EmrHomePageDiags diag : page.getDiags()) {
				IptLeaveDiagnosisNew iptLeave=new IptLeaveDiagnosisNew();
				//出院诊断-其他诊断-疾病名称
				iptLeave.setCyzdqtzdjbmc(diag.getDiagName());
				//出院诊断-其他诊断-疾病编码
				iptLeave.setCyzdqtzdjbbm(diag.getDiagCode());
				//出院诊断-其他诊断-出院情况
				diags.add(iptLeave);
			}
			item.setIptLeaveDiagnosisNews(diags);
		}

		//手术记录
		if(page.getOps()!=null&&page.getOps().size()>0){
			List<IptOperationNew> iptList=new ArrayList<IptOperationNew>();
			for (EmrHomePageOps ops : page.getOps()) {
				IptOperationNew ipto=new IptOperationNew();
				//麻醉医生工号
				ipto.setMzysgh(ops.getEmpCodeAnes()==null?"0":ops.getEmpCodeAnes());
				//手术医生工号
				ipto.setSsysgh(ops.getEmpCodeOp()==null?"0":ops.getEmpCodeOp());
				//手术助手I工号
				ipto.setSszsigh(ops.getEmpCodeOpi()== null?"0":ops.getEmpCodeOpi());
				//手术助手II工号
				ipto.setSszsiigh(ops.getEmpCodeOpii()== null?"-":ops.getEmpCodeOpii());
				//手术切口愈合等级
				ipto.setSsqkyhdj(ops.getHealGradeCode());
				//麻醉医生姓名
				ipto.setMzysxm(ops.getAnesDocName());
				//手术/操作名称
				ipto.setSsczmc(ops.getOpName());
				//手术/操作日期时间
				ipto.setSsczrqsj(DateUtils.getDateStr(ops.getOpDate()));
				//麻醉方法代码
				ipto.setMzffdm(ops.getAnesTypeCode());
				//手术医生姓名
				ipto.setSsysxm(ops.getOpDocName()==null?"-":ops.getOpDocName());
				//手术/操作代码
				ipto.setSsczdm(ops.getOpCode()==null?"-":ops.getOpCode());
				ipto.setBassid(ops.getOpCode()==null?"-":ops.getOpCode());//OP_CODE手术编码  /病案手术ID
				ipto.setSSXHmedicalrecordid(Integer.toString(ops.getSeqNo())==null?"-":Integer.toString(ops.getSeqNo()));//手术序号
				ipto.setSsczmbbw_dm(ops.getIncisionTypeCode() == null?"9999":ops.getIncisionTypeCode());//手术/操作-目标部位代码
				ipto.setSsqssj(DateUtils.getDateStr(ops.getOpDate()));//开始时间
				ipto.setSshzd(page.getDiagCodeDis()==null?"9":page.getDiagCodeDis());//手术后诊断，传病案首页的出院诊断编码
				String diagCodeClinic = page.getDiagCodeClinic();
				if (StringUtil.isBlank(diagCodeClinic)) {
					diagCodeClinic = "-";
				}
				ipto.setSsqzd(diagCodeClinic);//手术前诊断编码
				ipto.setMzffdm(ops.getAnesTypeCode() ==null?"9":ops.getAnesTypeCode());//麻醉方法代码 9是其他麻醉方式
				ipto.setMzysgh(ops.getEmpCodeAnes()==null?"0":ops.getEmpCodeAnes());//麻醉医生工号
				ipto.setSsysgh(ops.getEmpCodeOp()==null?"0":ops.getEmpCodeOp());//手术医生工号
				ipto.setSsyzmc(ops.getEmpCodeOpi()==null?"0":ops.getEmpCodeOpi());//手术一助名称
				ipto.setSsezmc(ops.getEmpCodeOpii()==null?"0":ops.getEmpCodeOpii());//手术二助手名称
				iptList.add(ipto);
			}
//			//@todo for test
//			IptOperationNew ipto=new IptOperationNew();
//			//麻醉医生工号
//			ipto.setMzysgh("-");
//			//手术医生工号
//			ipto.setSsysgh("-");
//			//手术助手I工号
//			ipto.setSszsigh("-");
//			//手术助手II工号
//			ipto.setSszsiigh("-");
//			//手术切口愈合等级
//			ipto.setSsqkyhdj("9");
//			//麻醉医生姓名
//			ipto.setMzysxm("-");
//			//手术/操作名称
//			ipto.setSsczmc("-");
//			//手术/操作日期时间
//			ipto.setSsczrqsj(DateUtils.getDateStr(new Date()));
//			//麻醉方法代码
//			ipto.setMzffdm("9");
//			//麻醉方法名称
//			ipto.setMzffmc("-");
//			//手术医生姓名
//			ipto.setSsysxm("-");
//			//手术/操作代码
//			ipto.setSsczdm("-");
//
//			iptList.add(ipto);
//
			item.setIptOperationNews(iptList);
		}

		if(CommonUtils.isEmptyString(item.getHxb())) item.setHxb("-");
		if(CommonUtils.isEmptyString(item.getXxb())) item.setXxb("-");
		if(CommonUtils.isEmptyString(item.getXj())) item.setXj("-");
		if(CommonUtils.isEmptyString(item.getQx())) item.setQx("-");
		if(CommonUtils.isEmptyString(item.getQt())) item.setQt("-");

		if(CommonUtils.isEmptyString(item.getHjdzDm())) item.setHjdzDm("-");
		if(CommonUtils.isEmptyString(item.getBlqph())) item.setBlqph("-");
		if(CommonUtils.isEmptyString(item.getXxph())) item.setXxph("-");
		if(CommonUtils.isEmptyString(item.getCyyy())) item.setCyyy("-");
		if(CommonUtils.isEmptyString(item.getCyjy())) item.setCyjy("-");
		if(CommonUtils.isEmptyString(item.getYygrjgDm())) item.setYygrjgDm("1");
		if(CommonUtils.isEmptyString(item.getGmyMc())) item.setGmyMc("-");
		if(CommonUtils.isEmptyString(item.getZyhzgmz())) item.setZyhzgmz(DateUtils.getDateStrHMS(new Date()));
		if(CommonUtils.isEmptyString(item.getHzjbzddzDm())) item.setHzjbzddzDm("1");//@todo 1稳定期2活动期3复发期4缓解期5根除
		if(CommonUtils.isEmptyString(item.getZyhzzdfhqkxxms())) item.setZyhzzdfhqkxxms("-");//ZYHZZDFHQKXXMS
		if(CommonUtils.isEmptyString(item.getZyhzjbzdlxxxms())) item.setZyhzjbzdlxxxms("-");//ZYHZJBZDLXXXMS
		if(CommonUtils.isEmptyString(item.getSfszDm())) item.setSfszDm("9");//SFSZ_DM1否2是9不详
		if(CommonUtils.isEmptyString(item.getSzqx())) item.setSzqx("-");//SZQX
		if(CommonUtils.isEmptyString(item.getSzqxsjDw())) item.setSzqxsjDw("-");//SZQXSJ_DW

		if(CommonUtils.isEmptyString(item.getSjblDm())) item.setSjblDm("9");//SJBL_DM 1否2是9不详
		if(CommonUtils.isEmptyString(item.getSszljczdwbydylDm())) item.setSszljczdwbydylDm("9");//1否2是9不详
		if(CommonUtils.isEmptyString(item.getRyhqzrq())) item.setRyhqzrq(DateUtils.getDateStrHMS(pat.getDateBegin()));//RYHQZRQ
		if(CommonUtils.isEmptyString(item.getDiagCoinOutpatientVsDischarge())) item.setDiagCoinOutpatientVsDischarge("1");//DiagCoinOutpatientVsDischarge
		//诊断符合情况-入院和出院0未做1符合2不符合3不确定//@todo
		if(CommonUtils.isEmptyString(item.getDiagCoinAdmssionVsDischarge())) item.setDiagCoinAdmssionVsDischarge("1");//DiagCoinAdmssionVsDischarge
		if(CommonUtils.isEmptyString(item.getDiagCoinPreoperativeVsPost())) item.setDiagCoinPreoperativeVsPost("1");//DiagCoinPreoperativeVsPost
		if(CommonUtils.isEmptyString(item.getYjssxysgh())) item.setYjssxysgh("-");//YJSSXYSGH
		if(CommonUtils.isEmptyString(item.getYjssxysmc())) item.setYjssxysmc("-");//YJSSXYSMC
		if(CommonUtils.isEmptyString(item.getZkkbdm())) item.setZkkbdm("-");//ZKKBDM
		if(CommonUtils.isEmptyString(item.getTransferredDeptName())) item.setTransferredDeptName("-");//TransferredDeptName
		if(CommonUtils.isEmptyString(item.getGenerMediServChargeZYBZLZF())) item.setGenerMediServChargeZYBZLZF("-");//GenerMediServCharge_ZYBZLZF
		if(CommonUtils.isEmptyString(item.getZyzlfzywzf())) item.setZyzlfzywzf("-");//ZYZLF_ZYWZF
		if(CommonUtils.isEmptyString(item.getZyzlfzygsf())) item.setZyzlfzygsf("-");//ZYZLF_ZYGSF
		if(CommonUtils.isEmptyString(item.getZyzlfzyzjf())) item.setZyzlfzyzjf("-");//ZYZLF_ZYZJF
		if(CommonUtils.isEmptyString(item.getZyzlfzytnf())) item.setZyzlfzytnf("-");//ZYZLF_ZYTNF
		if(CommonUtils.isEmptyString(item.getZyzlfzygczlf())) item.setZyzlfzygczlf("-");//ZYZLF_ZYGCZLF
		if(CommonUtils.isEmptyString(item.getZyzlfzytszlf())) item.setZyzlfzywzf("-");//ZYZLF_ZYTSZLF
		if(CommonUtils.isEmptyString(item.getZyqtf())) item.setZyqtf("-");//ZYQTF
		if(CommonUtils.isEmptyString(item.getZyqtftstpjgf())) item.setZyqtftstpjgf("-");//ZYQTF_TSTPJGF
		if(CommonUtils.isEmptyString(item.getZyqtfbzssf())) item.setZyqtfbzssf("-");	//ZYQTF_BZSSF
		if(CommonUtils.isEmptyString(item.getMedicineChinaZYZJF())) item.setMedicineChinaZYZJF("-");	//MedicineChina_ZYZJF
		if(CommonUtils.isEmptyString(item.getGzdwmc())) item.setGzdwmc("-");	//GZDWMC
		if(CommonUtils.isEmptyString(item.getGzdwdz())) item.setGzdwdz("-");	//GZDWDZ
		if(CommonUtils.isEmptyString(item.getGzddyb())) item.setGzddyb("-");	//GZDDYB
		if(CommonUtils.isEmptyString(item.getGzdwdhhm())) item.setGzdwdhhm("-");	//GZDWDHHM
		//JCJYJGDM/检查/检验结果代码/CC04_30_017_00/受检者检查/检验结果在特定分类中的代码1正常2异常9不确定
		if(CommonUtils.isEmptyString(item.getJcjyjgdm())) item.setJcjyjgdm("1");

		if(CommonUtils.isEmptyString(item.getJcjydljg())) item.setJcjydljg("-");	//JCJYDLJG
		//住院者疾病状态代码:1危急2严重3一般4不适用9其他
		if(CommonUtils.isEmptyString(item.getZyzjbztdm())||item.getZyzjbztdm().equals("-")) item.setZyzjbztdm("3");	//ZYZJBZTDM
		if(CommonUtils.isEmptyString(item.getQtyxcz())) item.setQtyxcz("-");	//QTYXCZ

		return item;
	}

	//7/手术记录@todo（病案首页手术还是手术登记）
	public static PtOperation createPtOperation(IUser pUser,PatListVo pat,RhipCnOpApply op) throws Exception {
		if(op==null) return null;
		PtOperation item=new PtOperation();
		item.setName("手术记录");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//手术记录流水号
		item.setSsjllsh(op.getCodeApply());
		//门诊住院标识
		//1	门诊 2	住院		3	体检		9	其它
		item.setMzzybz("2"); //@todo
		//手术起始时间
		item.setSsqssj(DateUtils.getDateStr(op.getDatePlan()));
		//手术结束时间
		item.setSsjssj(DateUtils.getDateStr(op.getDateConfirm()==null?op.getDatePlan():op.getDateConfirm()));
		//手术前诊断
		item.setSsqzd(op.getCodeDiag());
		//手术后诊断
		item.setSshzd("-");//@todo
		//手术代码
		item.setSsdm(op.getCodeOp());
		//手术名称
		item.setSsmc(op.getNameOp());
		item.setSsczmc(op.getOpName());
		//手术部位代码
		item.setSsbwdm("9999");//@todo待核实op.getDtOpbody()//待对照
		//手术类型(1 一般、2抢救、3术中及抢救 9其他   默认一般)
		item.setSslx("1");	//@todo
		//手术级别
		String level=op.getDtOplevel();
		if(CommonUtils.isEmptyString(level)) level="1";
		level=Integer.toString(Integer.parseInt(level));
		item.setOperationLevel(level);//@todo
		//手术经过描述
		item.setSsjgms("-");//@todo chengjia
		//麻醉方法
		String anaeType=getDictCodeMapDefault("rhip.anaeType",op.getDtAnae(),"14");
		item.setMzff(anaeType);
		item.setMzffdm(anaeType);
		//麻醉反应(1.无麻醉  2.有反应  3无反应 )
		item.setMzfy("2");  //@todo
		//切口愈合等级(1	甲		2	乙		3	丙	9	其他
		item.setTcyhdj("1");;
		//手术来源(是否源于本院，1 是   2 否)
		item.setSsly("1");
		//手术医生工号
		item.setSsysgh(op.getCodeEmpPhyOp());
		//手术医师姓名
		item.setSsysxm(op.getNameEmpPhyOp());
		//主刀医生科室代码
		item.setZdysksdm(pat.getDeptCodeOrig());
		//麻醉医师工号
		item.setMzysgh("-");//op.getPkEmpAnae()
		//麻醉医师姓名
		item.setMzysxm(op.getNameEmpPhyOp());
		//指导医生工号
		//手术医生助手I工号
		item.setSsyszsigh(op.getEmpCodeAsis()==null?"-":op.getEmpCodeAsis());//@todo op.getPkEmpAsis()
		//手术医生助手I姓名
		item.setSsyszsixm(op.getNameEmpAsis()==null?"-":op.getNameEmpAsis());
		//@todo手术一助名称
		item.setSsyzmc(op.getNameEmpAsis()==null?"-":op.getNameEmpAsis());
		item.setSszsigh(op.getNameEmpAsis()==null?"-":op.getNameEmpAsis());
		//手术医生助手II工号
		item.setSsyszsiigh(op.getEmpCodeAsis2()==null?"-":op.getEmpCodeAsis2());//@todo

		//手术医生助手II姓名
		item.setSsyszsiixm(op.getNameEmpAsis2()==null?"-":op.getNameEmpAsis2());
		item.setSsezmc(op.getNameEmpAsis2()==null?"-":op.getNameEmpAsis2());
		item.setSszsiigh(op.getNameEmpAsis2()==null?"-":op.getNameEmpAsis2());
		//手术切口类别
		//手术前诊断名称
		item.setSsqzdmc(op.getNameDiag());
		//手术后诊断名称
		item.setSshzdmc("-");//@todo
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//婚姻状况
		item.setHyzk(pat.getMarrySpcode());
		//职业
		item.setZy("90");//@todo
		//手术执行科室编码dept_exec_name
		item.setSszxksbm(op.getDeptCodeExec());//@todo转编码op.getPkDeptExec()
		//手术执行科室名称//@todo
		item.setSszxksmc(op.getDeptExecName());
		//植入手术标志
		item.setZrssbz("0");
		//输血量
		item.setSxl("0");
		//失血量
		item.setShxl("0");
		//手术切口等级编码
		//手术切口等级名称
		//SSCZRQSJ手术/操作日期时间
		item.setSsczrqsj(DateUtils.getDateStrHMS(op.getDateBegin()==null?op.getDateApply():op.getDateBegin()));
		//身份标识类别代码
		item.setSfbshm(pat.getIdNo()==null?"-":pat.getIdNo());
		//卡类型
		item.setKlx(pat.getCardType());//@todo
		//卡号
		item.setKh(pat.getBarcode());//
		//JSWLBZ/计生五类手术标识/HIS19_01_026:1上环2	取环3结扎4流产5引产9非计生类
		item.setJswlbz("9");//@todo
		item.setSfbshm("01");
		item.setSfbsdm("01");//@todo

		return item;
	}


	//8/在/出院结算表@todo
	//说明：NHIS发票与结算并不是一对一的关系，而接口要求必须有发票号，因此目前是以发票
	public static IptFee createIptFee(IUser pUser,PatListVo pat,RhipBlSettleVo vo,List<IptFeeCostVo> iptFeeList) throws Exception {
		if(vo==null||vo.getItems()==null||vo.getItems().size()==0) return null;
		List<RhipBlSettleItemVo> dts=vo.getItems();
		IptFee item=new IptFee();
		item.setName("在/出院结算表");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//退费标志//@todo待验证
		String flagBack="0";
		String pkSettleRev=vo.getPkSettleRev();
		if(pkSettleRev!=null&&!pkSettleRev.equals("")){
			flagBack="1";
		}
		item.setTfbz(flagBack);
		//医保结算流水号
		item.setYbjslsh(vo.getPkSettle());
		//费用流水号//@todo待验证
		item.setFyid(vo.getPkSettle());
		//结算来源
		item.setJsly("2");//1.诊间  2.窗口  3.自助机  9.其他
		//发票号码
		item.setFphm(vo.getInvCodes()==null?"-":vo.getInvCodes());
		//收费/退费时间
		item.setStfsj(DateUtils.getDateStrHMS(vo.getDateSt()));
		//费用金额
		if(vo.getAmountSt()!=null) item.setFyje(vo.getAmountSt().toString());



		//@todo是否根据编码来确认
//		i/*tem.setJcf(getInvFeeAmount(dts,"检查费"));
//		item.setZlf(getInvFeeAmount(dts,"治疗费"));
//		item.setQtf(getInvFeeAmount(dts,"其他费"));
//		item.setSsf(getInvFeeAmount(dts,"手术费"));
//		item.setFsf(getInvFeeAmount(dts,"放射费"));
//		item.setHyf(getInvFeeAmount(dts,"化验费"));
//		item.setZcf(getInvFeeAmount(dts,"诊察费"));
//		item.setZcyf(getInvFeeAmount(dts,"中草药费"));
//		item.setZcyf(getInvFeeAmount(dts,"中成药费"));
//		item.setGhf(getInvFeeAmount(dts,"挂号费"));
//		item.setXyf(getInvFeeAmount(dts,"西药费"));
//		item.setHlf(getInvFeeAmount(dts,"护理费"));
//		item.setSxf(getInvFeeAmount(dts,"输血费"));
//		item.setMzf(getInvFeeAmount(dts,"麻醉费"));
//		item.setTsf(getInvFeeAmount(dts,"透视费"));
//		item.setSxf(getInvFeeAmount(dts,"输氧费"));
//		item.setTxyf(getInvFeeAmount(dts,"特需药费"));
//		item.setSsclf(getInvFeeAmount(dts,"手术材料费"));
//		item.setCwf(getInvFeeAmount(dts,"床位费"));
//		item.setJsf(getInvFeeAmount(dts,"接生费"));
//		item.setPcf(getInvFeeAmount(dts,"陪床费"));
//		item.setTxf(getInvFeeAmount(dts,"特需费"));
//		item.setKtf(getInvFeeAmount(dts,"空调费"));
//		item.setZjf(getInvFeeAmount(dts,"正畸费"));
//		item.setXyf(getInvFeeAmount(dts,"镶牙费"));
//		item.setClf(getInvFeeAmount(dts,"材料费"));
//		item.setQjf(getInvFeeAmount(dts,"抢救费"));
//		item.setJcf(getInvFeeAmount(dts,"监测费"));*/
		//医保类型编码
		item.setYblxbm(vo.getHpCode());
		//医保类型名称
		item.setYblxmc(vo.getHpName());
		//自理金额
		//指乙类药品、诊疗项目、服务设施中个人按比例先行支付部分
		if(vo.getAmountPi()!=null) item.setZlje(vo.getAmountPi().toString());
		if(CommonUtils.isEmptyString(item.getZlje())) item.setZlje("0");
		//自费金额
		item.setZfje(item.getZlje());
		//指丙类药品、丙类诊疗项目、丙类服务设施和超限价部分
		//符合医保费用
		if(vo.getAmountInsu()!=null) item.setFhybfy(vo.getAmountInsu().toString());
		if(CommonUtils.isEmptyString(item.getFhybfy())) item.setFhybfy("0");
		//医保基金
		if(vo.getAmountInsu()!=null) item.setYbjj(vo.getAmountInsu().toString());
		if(CommonUtils.isEmptyString(item.getYbjj())) item.setYbjj("0");
		//医保起付线
		item.setYbqfx("0");//@todo
		//个人自付
		if(vo.getAmountPi()!=null) item.setGrzf(vo.getAmountPi().toString());
		//医院负担
		item.setYyfd("0");//@todo
		//转诊先自付
		item.setZzxzf("0");//@todo
		//统筹分段自付
		item.setTcfdzf("0");//@todo
		//超封顶线自付
		item.setCfdxzf("0");//@todo
		//统筹基金支付
		item.setTcjjzf("0");//@todo
		//公务员基金支付
		item.setGwyjjzf("0");//@todo
		//大病基金支付
		item.setDbjjzf("0");//@todo
		//账户支付
		if(vo.getAmountPi()!=null) item.setZhzf(vo.getAmountPi().toString());
		if(CommonUtils.isEmptyString(item.getZhzf())) item.setZhzf("0");
		//民政救助支付
		item.setMzjzzf("0");//@todo
		//其他基金支付
		item.setQtjjzf("0");//@todo
		//本次现金支付
		item.setBcxjzf("0");//@todo
		//结算日期
		item.setJsrq(DateUtils.getDateStrHMS(vo.getDateSt()));
		item.setSfygh("-");//@todo
		item.setSfyxm(vo.getNameEmpSt());
		if(CommonUtils.isEmptyString(item.getSfyxm())) item.setSfyxm("-");
		String brlb=getDictCodeMap("rhip.insu_op",vo.getDtSttype());
		//医疗费用来源类别代码/CV07_10_003/01社会基本医疗保险...
		item.setYlfylylbDm("01");//@todo
		//医疗费用结算方式代码/HIS18_01_002/01现金...
		item.setYlfyjsfsDm("99");//@todo
		item.setSfbsDm("01");//@todo
		item.setSfbshm(pat.getIdNo());
		item.setDjsj(DateUtils.getDateStrHMS(vo.getDateSt()));
		item.setDjrygh("-");
		item.setDjrymc("-");
		item.setZhxgsj(DateUtils.getDateStrHMS(new Date()));
		item.setZhxgrygh("-");
		item.setZhxgrymc("-");
		item.setBcyltc("0");
		List<IptFeeCost> iptList = new ArrayList<IptFeeCost>();
		for (IptFeeCostVo iptFeeCost: iptFeeList) {
			IptFeeCost iptVo =new IptFeeCost();
			iptVo.setZyfyfl_dm(iptFeeCost.getCode());
			iptVo.setZyfyfl_je(iptFeeCost.getAmount());
			iptVo.setFpfyflmc(iptFeeCost.getName());
			iptList.add(iptVo);
		}
		item.setIPT_DrugRecords(iptList);

		return item;
	}


	//8/在/出院结算表bak@todo
	//说明：NHIS发票与结算并不是一对一的关系，而接口要求必须有发票号，因此目前是以发票
	public static IptFee createIptFeeBak(IUser pUser,PatListVo pat,RhipBlInvoice bi) throws Exception {
		if(bi==null||bi.getInv()==null||bi.getBs()==null||bi.getItems()==null||bi.getItems().size()==0) return null;
		BlSettle bs=bi.getBs();
		BlInvoice inv=bi.getInv();
		List<BlInvoiceDt> dts=bi.getItems();
		IptFee item=new IptFee();
		item.setName("在/出院结算表");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//退费标志//@todo待验证
		String flagBack="0";
		String pkSettleRev=bs.getPkSettleRev();
		if(pkSettleRev!=null&&!pkSettleRev.equals("")){
			flagBack="1";
		}
		item.setTfbz(flagBack);
		//费用流水号//@todo待验证
		item.setFyid(inv.getPkInvoice());
		//结算来源
		item.setJsly("2");//1.诊间  2.窗口  3.自助机  9.其他
		//发票号码
		item.setFphm(inv.getCodeInv());
		//收费/退费时间
		item.setStfsj(DateUtils.getDateStrHMS(bs.getDateSt()));
		//费用金额
		if(inv.getAmountInv()!=null) item.setFyje(inv.getAmountInv().toString());
		//@todo是否根据编码来确认
//		item.setJcf(getInvFeeAmount(dts,"检查费"));
//		item.setZlf(getInvFeeAmount(dts,"治疗费"));
//		item.setQtf(getInvFeeAmount(dts,"其他费"));
//		item.setSsf(getInvFeeAmount(dts,"手术费"));
//		item.setFsf(getInvFeeAmount(dts,"放射费"));
//		item.setHyf(getInvFeeAmount(dts,"化验费"));
//		item.setZcf(getInvFeeAmount(dts,"诊察费"));
//		item.setZcyf(getInvFeeAmount(dts,"中草药费"));
//		item.setZcyf(getInvFeeAmount(dts,"中成药费"));
//		item.setGhf(getInvFeeAmount(dts,"挂号费"));
//		item.setXyf(getInvFeeAmount(dts,"西药费"));
//		item.setHlf(getInvFeeAmount(dts,"护理费"));
//		item.setSxf(getInvFeeAmount(dts,"输血费"));
//		item.setMzf(getInvFeeAmount(dts,"麻醉费"));
//		item.setTsf(getInvFeeAmount(dts,"透视费"));
//		item.setSxf(getInvFeeAmount(dts,"输氧费"));
//		item.setTxyf(getInvFeeAmount(dts,"特需药费"));
//		item.setSsclf(getInvFeeAmount(dts,"手术材料费"));
//		item.setCwf(getInvFeeAmount(dts,"床位费"));
//		item.setJsf(getInvFeeAmount(dts,"接生费"));
//		item.setPcf(getInvFeeAmount(dts,"陪床费"));
//		item.setTxf(getInvFeeAmount(dts,"特需费"));
//		item.setKtf(getInvFeeAmount(dts,"空调费"));
//		item.setZjf(getInvFeeAmount(dts,"正畸费"));
//		item.setXyf(getInvFeeAmount(dts,"镶牙费"));
//		item.setClf(getInvFeeAmount(dts,"材料费"));
//		item.setQjf(getInvFeeAmount(dts,"抢救费"));
//		item.setJcf(getInvFeeAmount(dts,"监测费"));
		//医保类型编码
		item.setYblxbm(bi.getHpCode());
		//医保类型名称
		item.setYblxmc(bi.getHpName());
		//自理金额
		//指乙类药品、诊疗项目、服务设施中个人按比例先行支付部分
		//自费金额
		//指丙类药品、丙类诊疗项目、丙类服务设施和超限价部分
		//符合医保费用
		//医保基金
		//医保起付线
		//个人自付
		if(inv.getAmountPi()!=null) item.setGrzf(inv.getAmountPi().toString());
		//医院负担
		//转诊先自付
		//统筹分段自付
		//超封顶线自付
		//统筹基金支付
		//公务员基金支付
		//大病基金支付
		//账户支付
		//民政救助支付
		//其他基金支付
		//本次现金支付
		//结算日期
		item.setJsrq(DateUtils.getDateStrHMS(bs.getDateSt()));

		return item;
	}


	//9/住院费用明细表
	public static IptFeeDetail createIptFeeDetail(IUser pUser,PatListVo pat,RhipBlIpDtVo vo) throws Exception {
		if(vo==null) return null;
		IptFeeDetail item=new IptFeeDetail();
		item.setName("住院费用明细表");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//规格
		item.setGg(vo.getSpec());
		//费用流水号
		item.setFyid(vo.getPkSettle());
		item.setFymxlsh(vo.getPkSettle());
		//收费目录类别
		//医保收费项目编码
		item.setYbsfxmbm(vo.getCodeCg());
		//医保收费项目名称
		item.setYbsfxmmc(vo.getNameCg());
		//医疗项目类别编码
		item.setYlxmlb(vo.getCateCode());
		//医院项目类别名称
		item.setYlxmlbmc(vo.getCateName());
		//单价
		if(vo.getPrice()!=null) item.setDj(vo.getPrice().toString());
		//数量
		if(vo.getQuan()!=null) item.setSl(vo.getQuan().toString());
		//总金额
		if(vo.getAmount()!=null) item.setSl(vo.getAmount().toString());
		//剂型
		//使用频次
		//用药途径
		//用药天数
		//使用方法
		//计量单位
		item.setJldw(vo.getUnitName());
		//医保结算范围费用总额
		//个人自费
		//自付比例
		//收费项目编码
		//医嘱ID
		//流水号
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//费用明细流水号
		//医嘱号
		//医嘱内容
		//执行次数
		//住院收费发生时间
		item.setZysffsrq(DateUtils.getDateStrHMS(vo.getDateHap()));
		//收费/退费标志代码/1收费2退费9不详
		String bz="1";
		if(vo.getFlagCanc()!=null&&vo.getFlagCanc().equals("1")){
			bz="2";
		}
		item.setStbzDm(bz);
		//SFMLLB收费目录类别CV20_17_001_01/1药品/2检治项目/3材料与服务设施/99未知
		String lb="99";
		String cateCode=vo.getCateCode();
		if(cateCode==null) cateCode="";
		if(cateCode.equals("0001")||cateCode.equals("0002")||cateCode.equals("0003")||cateCode.equals("0004")||cateCode.equals("0005")){
			lb="1";
		}else if(cateCode.equals("0045")||cateCode.equals("0046")||cateCode.equals("0047")||cateCode.equals("0048")
				||cateCode.equals("0049")||cateCode.equals("0050")||cateCode.equals("0051")||cateCode.equals("0052")||cateCode.equals("0053")){
			lb="3";
		}else{
			lb="2";
		}
		item.setSfmllb(lb);
		//ZYFYFL_DM住院费用分类代码/HIS18_01_008
		String zyfyflDm = getDictCodeMapDefault("rhip.cateCode",vo.getCateCode(),"I");
		item.setZyfyflDm(zyfyflDm);
		//SFXMBM收费项目编码/CV07_10_001（费用分类代码字典）
		String sfxmbm = getDictCodeMapDefault("rhip.costClass",vo.getCateCode(),"14");//@todo99
		item.setSfxmbm(sfxmbm);
		//ZJE总金额
		item.setZje(vo.getAmount()==null?"0":vo.getAmount().toString());
		//YBJSFWFYZJE医保结算范围费用总额
		Double num=1-vo.getRatioSelf();
		Double amount=vo.getAmount()*num;
		item.setYbjsfwfyzje(amount==null?"0":amount.toString());
		//GRZF个人自费
		item.setGrzf(vo.getAmountPi()==null?"0":vo.getAmountPi().toString());
		//MXXMYBFNFLZFJE明细项目医保范围内分类自负金额
		item.setMxxmybfnflzfje("0");//@todo
		//YZXMMXID医嘱项目明细ID
		item.setYzxmmxid(vo.getCodeCg());
		item.setDw(vo.getUnitName());
		//XMDM项目代码
		item.setXmdm(vo.getCodeCg());
		//XMMC项目名称
		item.setXmmc(vo.getNameCg());
		//MXXMYBFWWZFZE医保范围外自费金额
		item.setMxxmybfwwzfze(vo.getAmountPi()==null?"0":vo.getAmountPi().toString());
		//YZID医嘱ID
		item.setYzid(vo.getOrdsn()==null?"-":vo.getOrdsn());
		//ZFBL自付比例
		item.setZfbl(vo.getRatioSelf()==null?"1":vo.getRatioSelf().toString());
		//YBXE医保限额
		item.setYbxe(vo.getAmount()==null?"0":vo.getAmount().toString());
		item.setYzh(vo.getOrdsn()==null?"-":vo.getOrdsn());// 医嘱号
		item.setYznr("-");//医嘱内容
		item.setZxcs("-");//执行次数
		item.setSfyp_bs("9");//是否药品标识
		item.setYpdm("-");//药品代码
		item.setYpmc("-");//
		item.setFwxmdm("-");//
		item.setFwxmmc("-");//
		item.setJx("-");//
		item.setSypc("-");//使用频次
		item.setJx("-");//使用途径
		item.setYyts("-");
		item.setSyff("-");
		item.setJldw("-");
		item.setDjsj(DateUtils.getDateStrHMS(new Date()));
		item.setDjrygh("-");
		item.setDjrymc("-");
		item.setZhxgsj(DateUtils.getDateStrHMS(new Date()));
		item.setZhxgrygh("-");
		item.setZhxgrymc("-");
		item.setTjmc("-");
		item.setDw("-");
		return item;
	}

	//其他数据集
	//5/诊断明细报告
	public static PtDiagnosis createPtDiagnosis(IUser pUser,PatListVo pat,PvDiagVo diag) throws Exception {
		if(diag==null) return null;
		PtDiagnosis item=new PtDiagnosis();
		item.setName("诊断明细报告");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//诊断流水号
		item.setZdlsh(diag.getPkPvdiag());
		//身份标识类别代码
		item.setSfbsDm("01");//@todo
		item.setSfbshm(pat.getIdNo());
		//卡类型
		item.setKlx(pat.getCardType());//@todo
		//卡号
		item.setKh(pat.getBarcode());//@todo
		//病人标识类别
		//1	门诊 2	住院		3	体检		9	其它
		item.setBrbslb("2");
		//中医/西医诊断标志代码//3西医
		item.setZyxyzdbzDm("3");
		//诊断依据代码
		item.setZdyjdm("1");//1/临床诊断
		//疾病代码
		String diagCode=diag.getCodeDiag();
		if(diagCode==null||diagCode.equals("")) diagCode="-";
		item.setJbdm(diagCode);
		//疾病名称
		String diagName=diag.getNameDiag();
		if(diagName==null||diagName.equals("")) diagName=diag.getDescDiag();
		if(diagName==null||diagName.equals("")) return null;

		item.setJbmc(diagName);
		//中医诊断类别代码
		item.setZyzdlbdm("3");//出院中医诊断病名
		//西医诊断类别代码
		String xyzdlbdm="";
		if(diag.getDtDiagtype()!=null){
			if(diag.getDtDiagtype().equals("0100")){
				//入院
				xyzdlbdm = "03";
			}else if(diag.getDtDiagtype().equals("0109")){
				//出院
				xyzdlbdm = "01";
			}else{
				//其他
				xyzdlbdm = "99";
			}
		}
		item.setXyzdlbdm(xyzdlbdm);
		//主要诊断标志
		item.setZyzdbz(getDiagMainFlag(diag.getFlagMaj()));
		//疑似诊断标志
		item.setYszdbz(diag.getFlagSusp());
		//诊断依据代码
		//诊断日期
		item.setZdrq(DateUtils.getDateStr(diag.getDateDiag()));
		//转归情况
		//诊断类型编码1主要诊断；2初步诊断；3 入院诊断 ；4出院诊断；5门诊诊断
		item.setZdlxbm(getDiagtypeCode(diag));
		//诊断过程描述
		item.setZdgcms(diag.getDescDiag());
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//诊断医师工号
		item.setZdysgh(diag.getEmpCode()==null?"-":diag.getEmpCode());
		//诊断医师姓名
		item.setZdysxm(diag.getNameEmpDiag());
		//治疗结果代码
		item.setZljgDm("1");//1治愈
		//其它治疗结果情况详述
		item.setQtzljgqkxs("-");
		//转归
		item.setZgqk("1");
		//疾病诊断编码版本号
		item.setJbzdbmbbh("ICD-10");

		return item;
	}


	///门诊数据集和检验检查报告、体检记录
	//门诊数据集
	//1/挂号表(Opt_Register)
	public static OptRegister createOptRegister(IUser pUser,EncVo enc,PatListVo pat) throws Exception {
		if(enc==null||enc.getPi()==null||enc.getPv()==null) return null;
		PvEncounter pv=enc.getPv();
		PiMaster pi=enc.getPi();
		PvIp pvIp=enc.getPvIp();
		PvOp pvOp=enc.getPvOp();

		OptRegister item=new OptRegister();
		item.setName("挂号表");
		//就诊流水号
		item.setJzlsh(pv.getCodePv());
		//预约流水号
		item.setYylsh(enc.getPvOp().getPkSchappt()==null?enc.getPvOp().getPkPvop():enc.getPvOp().getPkSchappt());
		//挂号流水号
		item.setGhlsh(pv.getCodePv());
		//卡号
		String cardNo=pi.getBarcode();
		if(cardNo==null||cardNo.equals("")) cardNo="-";
		item.setKh(cardNo);
		//卡类型01医保02就诊卡99其他03健康卡
		item.setKlx(pat.getCardType());
		//挂号日期
		item.setGhrq(DateUtils.getDateStr(pv.getDateReg()));
		//身份证件类别代码
		/*
		 * 01	居民身份证
		   02	居民户口簿
           03	护照
           04	军官证
           05	驾驶证
           06	港澳居民往来内地通行证
           07	台湾居民往来内地通行证
           99	其他法定有效证件
		 *
		 */
		item.setIdCardCode(pi.getDtIdtype());
		//身份证件号码
		item.setIdCard(pi.getIdNo());
		//挂号方式1.自助机   2.窗口   3.手机端 31医院公众号 32卫计局公众号 33健康中山app 34健康中山公众号 4预约挂号网  9.其他
		item.setGhfs("2");//@todo
		//病人类别/1基本医疗保险2全公费3新型农村合作医疗4商业医疗保险5全自费7贫困救助99其他
		//CVX_PatientCategory
		String brlb=getDictCodeMap("rhip.insu_op",enc.getHpCode());
		item.setBrlb(brlb);
		//退号标志0.正常   1.退号
		String flagCancel=pv.getFlagCancel();
		if(flagCancel==null||flagCancel.equals("")) flagCancel="0";
		item.setThbz(flagCancel);
		//挂号类别/参照挂号类别：01.普通门诊   02.急诊   03.专家门诊  04.专科门诊   05.特需门诊  06.专病门诊   10.体检   99.其他
		item.setGhlb("01");//@todo
		//挂号费
		item.setGhf("111");//@todo
		//诊疗费
		item.setZlf("222");//@todo
		//其他费
		item.setQtf("333");//@todo
		//挂号科室代码
		item.setGhksdm(enc.getDeptCodeOrig());
		//单据号
		item.setGhdjh("-");//@todo
		//发票号
		item.setFphm("-");//@todo
		//患者姓名
		item.setHzxm(pi.getNamePi());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pi.getBirthDate()));
		//是否预约标识SFYYBS/1否2是9不详
		String sfyybs="1";
		if(enc.getPvOp().getEuRegtype()!=null&&enc.getPvOp().getEuRegtype().equals("1")){
			sfyybs="2";
		}
		item.setSfyybs(sfyybs);
		//医生姓名
		item.setYsxm(enc.getPvOp().getNameEmpPv()==null?"-":enc.getPvOp().getNameEmpPv());
		//医生工号
		item.setYsgh(enc.getCodeEmpPv()==null?"-":enc.getCodeEmpPv());
		//科室
		item.setGhksmc(enc.getDeptNameGh());

		return item;
	}

	//2/门诊就诊记录表(Opt_Record)
	public static OptRecord createOptRecord(IUser pUser,EncVo enc,CnOpEmrRecord opRec,PatListVo pat) throws Exception {
		if(enc==null||enc.getPi()==null||enc.getPv()==null) return null;
		PvEncounter pv=enc.getPv();
		PiMaster pi=enc.getPi();
		PvIp pvIp=enc.getPvIp();
		PvOp pvOp=enc.getPvOp();

		OptRecord item=new OptRecord();
		item.setName("门诊就诊记录表");
		//*就诊流水号
		item.setJzlsh(pv.getCodePv());
		//挂号流水号
		item.setGhlsh(pv.getCodePv());
		//*是否预约
		String isAppt="0";
		if(pvOp.getPkAppo()==null||pvOp.getPkAppo().equals("")) isAppt="1";
		item.setSfyy(isAppt);
		//卡号
		String cardNo=pi.getBarcode();
		if(cardNo==null||cardNo.equals("")) cardNo="-";
		item.setKh(cardNo);
		//卡类型01医保02就诊卡99其他03健康卡
		//@todo
		item.setKlx("02");
		//身份证件类别代码
		/*
		 * 01	居民身份证
		   02	居民户口簿
           03	护照
           04	军官证
           05	驾驶证
           06	港澳居民往来内地通行证
           07	台湾居民往来内地通行证
           99	其他法定有效证件
		 *
		 */
		item.setIdCardCode(pi.getDtIdtype());
		//身份证件号码
		item.setIdCard(pi.getIdNo());
		//*门诊类别0	门诊		1	急诊		9	其他
		item.setMzlb("0");//@todo
		//*科室代码
		item.setJzksbm(enc.getDeptCodeOrig());
		//诊断编码(主要)
		item.setZdbm("-");
		//*诊断名称(主要)
		item.setZdmc("--");
		//主诉
		String zs="空";
		if(opRec!=null) zs=opRec.getProblem();
		item.setZs(zs);//@todo
		//症状描述
		item.setZzms("-");//
		//现病史
		String xbs="-";
		if(opRec!=null) xbs=opRec.getPresent();
		item.setXbs(xbs);
		//*就诊日期
		Date date=pv.getDateClinic()==null?pv.getDateBegin():pv.getDateClinic();
		item.setJzrq(DateUtils.getDateStr(date));
		//咨询问题
		item.setZxwt("");
		//卫生服务要求
		item.setWsfwyq("");
		//发病日期时间
		item.setFbrqsj("");
		//症状持续时间（min）
		item.setZzcxsj("");
		//健康问题评估
		item.setJkwtpg("");
		//处置计划
		item.setCzjh("");
		//中药类别代码
		item.setZylbdm("");
		//用药停止日期时间
		item.setYytzrqsj("");
		//责任医师姓名
		item.setZrysxm(pv.getNameEmpPhy());

		//患者姓名
		item.setHzxm(pi.getNamePi());
		//医保类型
		item.setYblx("");
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pi.getBirthDate()));
		//新生儿标志/0-非新生儿,1-新生儿
		item.setXsebz("0");//@todo
		//诊断类型区分代码/HIS18_01_003/1中医12民族医2中西医3西医
		item.setZdlxqfDm("3");//@todo
		//诊断类别代码/HIS18_01_004/01:ICD -10 02:国标-95 03:国标-97
		item.setZdlbDm("01");//@todo
		//处理措施/处理措施(对患者所采取处理措施记录DE05.10.014.00)
		item.setClcs("-");
		//治疗方案
		item.setZlfa("-");
		//就诊时间
		item.setJzsj(DateUtils.getDateStr(pat.getDateBegin()));
		//责任医师工号
		item.setZrysgh(enc.getCodeEmpPv()==null?"-":enc.getCodeEmpPv());

		return item;
	}

	//3/门诊处方记录表(Opt_Recipe)
	public static OptRecipe createOptRecipe(IUser pUser,EncVo enc, CnPresVo pres,PatListVo pat) throws Exception {
		if(enc==null||enc.getPi()==null||enc.getPv()==null||pres==null||pres.getOrders()==null) return null;
		PvEncounter pv=enc.getPv();
		PiMaster pi=enc.getPi();
		DecimalFormat df = new DecimalFormat("#0.0000");

		OptRecipe item=new OptRecipe();
		item.setName("门诊处方记录表");
		//*就诊流水号
		item.setJzlsh(pv.getCodePv());
		//*处方ID
		item.setCfid(pres.getPkPres());

		item.setGhlsh(pv.getCodePv());
		//*处方来源
		//1门诊、2急诊@todo
		item.setCfly("1");
		//*科室代码
		item.setKfksdm(enc.getDeptCodeOrig());
		//科室名称
		item.setKfksmc(enc.getDeptNameGh());
		//*处方状态0正常1作废
		String cfzt="0";//@todo
		item.setCfzt(cfzt);
		//*处方类别代码1	西药	2	成药		3	草药		4	非药物处方		9	其他
		//医院数据：01诊疗02西药03中药04急诊西药07针剂处方08儿科西药09海湾城西药
		item.setCflb("02");//@todo
		//*开方时间
		item.setKfsj(DateUtils.getDateStr(pres.getDatePres()));
		//发药时间
		item.setFysj("");
		//处方下达医生工号
		item.setYzxdysgh(pres.getCodeEmp()==null?"-":pres.getCodeEmp());
		//处方下达医生姓名
		item.setYzxdysxm(pres.getNameEmpOrd()==null?"-":pres.getNameEmpOrd());
		//备注
		item.setBz(pres.getNote());
		//患者姓名
		item.setHzxm(pi.getNamePi());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pi.getBirthDate()));
		List<OptRecipeDetail> details=new ArrayList<>();
		for (TOrderVo ord : pres.getOrders()) {
			OptRecipeDetail detail=new OptRecipeDetail();
			//处方明细号码
			detail.setCfmxhm(ord.getOrdsn().toString());
			//项目编号
			detail.setXmbh(ord.getCodeOrd());
			detail.setFwxmdm(ord.getCodeOrd()==null?"-":ord.getCodeOrd());
			//*项目名称
			detail.setXmmc(ord.getNameOrd());
			detail.setFwxmmc(ord.getNameOrd()==null?"":ord.getNameOrd());
			//*是否药品
			detail.setSfyp(ord.getFlagDurg());
			detail.setYpdm(ord.getCodeOrd());
			detail.setYpmc(ord.getNameOrd());
			//产地ID
			detail.setCdid(ord.getFactoryCode()==null?"-":ord.getFactoryCode());
			//产地名称
			detail.setCdmc(ord.getFactoryName()==null?"-":ord.getFactoryName());
			//组号
			detail.setZh(ord.getGroupno()==null?"0":ord.getGroupno());
			//主药标识
			detail.setZybz("0");//@todo
			//药品剂型名称
			//detail.setJxmc("");
			//药品剂型代码
			detail.setJxdm("");
			//药品通用名
			detail.setYptym("");
			//药品商品名
			detail.setYpspm("");
			//药品规格
			detail.setYpgg(ord.getSpec()==null?"-":ord.getSpec());
			//药品包装规格单位
			detail.setYpggdw(ord.getUnitName()==null?"-":ord.getUnitName());
			//药品包装规格系数
			Double packSize=new Double("1");
			if(ord.getPackSize()!=null){
				packSize=ord.getPackSize();
			}
			detail.setYpggxs(packSize.toString());
			//发药单位
			//detail.setFydw(ord.getUnitName());
			//*发药数量
			Double quan=new Double("1");
			if(ord.getQuan()!=null){
				quan=ord.getQuan();
			}
			detail.setFysl(quan.toString());//@todo/ord.getQuan().toString()
			//用药频度
			if(ord.getCodeFreq()!=null) detail.setYypd(ord.getCodeFreq().toUpperCase());
			//给药途径名称
			detail.setTjmc(ord.getSupply());
			//给药途径代码
			String supplyCate=ord.getCodeSupply();
			if(CommonUtils.isEmptyString(supplyCate)) supplyCate="9";
			supplyCate="9";
			detail.setTjdm(supplyCate);//@todo ord.getCodeSupply()
			//用药天数
			if(ord.getDays()!=null) detail.setYyts(ord.getDays().toString());
			//每次使用剂量
			if(ord.getDosage()!=null) detail.setJl(ord.getDosage().toString());
			//每次使用剂量单位
			detail.setDw(ord.getUnitNameDos());
			//每次使用数量
			detail.setMcsl(quan.toString());
			//每次使用数量单位
			detail.setMcdw(ord.getUnitName());
			//帖数
			if(ord.getOrds()!=null) detail.setTs(ord.getOrds().toString());
			//总额
			detail.setZe("");
			//单价
			detail.setDj(ord.getPriceCg()==null?"0.0":df.format(ord.getPriceCg()));
			//药品统一编码
			detail.setYptybm("-"); //@todo
			//药品批准文号
			detail.setYppzwh("");
			//药品有效期
			detail.setYpyxq("");
			//YWLX_DM药物类型代码/HIS18_01_006
			String ywlxdm = ord.getPharmBacode();
			if(CommonUtils.isEmptyString(ywlxdm)) ywlxdm="9999";//@todo
			detail.setYwlxdm(ywlxdm);
			//HDYSGH核对药师工号//@todo
			detail.setHdysgh("-");
			detail.setHdysxm("-");
			//发药药师//@todo
			detail.setFyysgh("-");
			detail.setFyysxm("-");
			//处方执行科室
			detail.setYzzxksbm(ord.getDeptCodeExec()==null?"-":ord.getDeptCodeExec());
			detail.setYzzxksmc(ord.getDeptNameExec()==null?"-":ord.getDeptNameExec());
			//处方执行人/@todo
			detail.setYzzxrgh("-");
			detail.setYzzxrxm("-");
			//处方执行时间/@todo
			detail.setYzzzsj(DateUtils.getDateStr(ord.getDateStart()));
			//药物类型/@todo
			detail.setYwlxdm(ywlxdm);
			detail.setYwlx("-");
			//药品剂型
			detail.setJxdm(ord.getDtDosage()==null?"-":ord.getDtDosage());//@todo 剂型对照
			detail.setJxdm("-");
			//批文
			detail.setYppzwh(ord.getApprNo()==null?"-":ord.getApprNo());
			//中药类别代码ZYLBDM0未使用1中成药2中草药9其他中药
			String zylbdm="0";
			String codeOrdType=ord.getCodeOrdtype();
			if(codeOrdType==null) codeOrdType="";
			if(codeOrdType.equals("0102")){
				zylbdm="1";
			}else if(codeOrdType.equals("0103")){
				zylbdm="2";
			}
			detail.setZylbdm(zylbdm);
			//YZXMLXBM/处方项目类型编码/CV06_00_229
			String yzxmlxbm = getDictCodeMapDefault("rhip.codeordtype",ord.getCodeOrdtype(),"31");
			detail.setYzxmlxbm(yzxmlxbm);
			detail.setYypd((ord.getCodeFreq()==null?"":ord.getCodeFreq()).toUpperCase());
			detail.setZyjs("0");//@todo ZYJS 中药剂数
			//FYSLDW_DM	发药数量单位代码
			detail.setFysldwDm(ord.getUnitCode());
			detail.setFysldwDm("7");//片//@todo
			detail.setTjdm(supplyCate);
			detail.setTjmc(ord.getSupply()==null?"0":ord.getSupply());
			detail.setYyts((ord.getDays()==null?"0":ord.getDays()).toString());
			detail.setJl((ord.getDosage()==null?"0":ord.getDosage()).toString());
			detail.setDw("-");
			details.add(detail);
		}
		item.setOptRecipeDetails(details);

		return item;
	}

	////4/门诊收费表(Opt_Fee)
	//说明：NHIS发票与结算并不是一对一的关系，而接口要求必须有发票号，因此目前是以发票
	public static OptFee createOptFee(IUser pUser,PatListVo pat,RhipBlSettleVo vo) throws Exception {
		if(vo==null||vo.getItems()==null||vo.getItems().size()==0) return null;
		List<RhipBlSettleItemVo> dts=vo.getItems();
		OptFee item=new OptFee();
		item.setName("门诊收费表");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//处方流水号
		item.setCflsh(vo.getCodeSt()==null?vo.getPkSettle():vo.getCodeSt());//@todo 待确定
		item.setYbjslsh(vo.getCodeSt()==null?vo.getPkSettle():vo.getCodeSt());//@todo
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//退费标志//@todo待验证
		String flagBack="0";
		String pkSettleRev=vo.getPkSettleRev();
		if(pkSettleRev!=null&&!pkSettleRev.equals("")){
			flagBack="1";
		}
		item.setTfbz(flagBack);
		//费用流水号//@todo待验证
		item.setFyid(vo.getPkSettle());
		//结算来源
		item.setJsly("2");//1.诊间  2.窗口  3.自助机  9.其他
		//发票号码
		item.setFphm(vo.getInvCodes()==null?"-":vo.getInvCodes());
		//收费/退费时间
		item.setStfsj(DateUtils.getDateStrHMS(vo.getDateSt()));
		//费用金额
		if(vo.getAmountSt()!=null) item.setFyje(vo.getAmountSt().toString());
		//@todo是否根据编码来确认
		//item.setJcf(getInvFeeAmount(dts,"检查费"));
		//item.setZlf(getInvFeeAmount(dts,"治疗费"));
//		item.setQtf(getInvFeeAmount(dts,"其他费"));
//		item.setSsf(getInvFeeAmount(dts,"手术费"));
//		item.setFsf(getInvFeeAmount(dts,"放射费"));
//		item.setHyf(getInvFeeAmount(dts,"化验费"));
//		item.setZcf(getInvFeeAmount(dts,"诊察费"));
//		item.setZcyf(getInvFeeAmount(dts,"中草药费"));
//		item.setZcyf(getInvFeeAmount(dts,"中成药费"));
//		item.setGhf(getInvFeeAmount(dts,"挂号费"));
//		item.setXyf(getInvFeeAmount(dts,"西药费"));
//		item.setHlf(getInvFeeAmount(dts,"护理费"));
//		item.setSxf(getInvFeeAmount(dts,"输血费"));
//		item.setMzf(getInvFeeAmount(dts,"麻醉费"));
//		item.setTsf(getInvFeeAmount(dts,"透视费"));
//		item.setSxf(getInvFeeAmount(dts,"输氧费"));
//		item.setTxyf(getInvFeeAmount(dts,"特需药费"));
//		item.setSsclf(getInvFeeAmount(dts,"手术材料费"));
//		item.setCwf(getInvFeeAmount(dts,"床位费"));
//		item.setJsf(getInvFeeAmount(dts,"接生费"));
//		item.setPcf(getInvFeeAmount(dts,"陪床费"));
//		item.setTxf(getInvFeeAmount(dts,"特需费"));
//		item.setKtf(getInvFeeAmount(dts,"空调费"));
//		item.setZjf(getInvFeeAmount(dts,"正畸费"));
//		item.setXyf(getInvFeeAmount(dts,"镶牙费"));
//		item.setClf(getInvFeeAmount(dts,"材料费"));
//		item.setQjf(getInvFeeAmount(dts,"抢救费"));
		//item.setJcf(getInvFeeAmount(dts,"监测费"));
		//医保类型编码
		int aa=vo.getHpCode().length();
		item.setYblxbm(vo.getHpCode().length()>4?vo.getHpCode().substring(0,4):vo.getHpCode());
		//医保类型名称
		item.setYblxmc(vo.getHpName());
		//自理金额
		item.setZlje("0");//@todo
		//指乙类药品、诊疗项目、服务设施中个人按比例先行支付部分
		//自费金额
		item.setZfje("0");//@todo
		//指丙类药品、丙类诊疗项目、丙类服务设施和超限价部分
		//符合医保费用
		item.setFhybfy("0");//@todo
		//医保基金
		item.setYbjj("0");//@todo
		//医保起付线
		item.setYbqfx("0");//@todo
		//个人自付
		if(vo.getAmountPi()!=null) item.setGrzf(vo.getAmountPi().toString());
		//医院负担
		item.setYyfd("0"); //@todo
		//转诊先自付
		item.setZzxzf("0");//@todo
		//统筹分段自付
		item.setTcfdzf("0");//@todo
		//超封顶线自付
		item.setCfdxzf("0");
		//统筹基金支付
		item.setTcjjzf("0");
		//公务员基金支付
		item.setGwyjjzf("0");
		//大病基金支付
		item.setDbjjzf("0");
		//账户支付
		item.setZhzf("0");
		//民政救助支付
		item.setMzjzzf("0");
		//其他基金支付
		item.setQtjjzf("0");
		//本次现金支付
		item.setBcxjzf("0");
		//结算日期
		item.setJsrq(DateUtils.getDateStrHMS(vo.getDateSt()));
		//收费员
		item.setSfysgh(vo.getCodeEmpSt()==null?"-":vo.getCodeEmpSt());
		item.setSfysxm(vo.getNameEmpSt()==null?"-":vo.getNameEmpSt());
		//医疗费用结算方式代码/HIS18_01_002
		item.setYlfyjsfsDm("01");//@todo
		//BCYLTC/补充医疗统筹
		item.setBcyltc("0");//@todo
		return item;
	}

	//5/门诊收费明细表(Opt_FeeDetail)
	public static OptFeeDetail createOptFeeDetail(IUser pUser,PatListVo pat,RhipBlOpDtVo vo) throws Exception {
		if(vo==null) return null;
		OptFeeDetail item=new OptFeeDetail();
		item.setName("门诊收费明细表");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//处方流水号
		item.setCflsh(vo.getPresNo()==null?"-":vo.getPresNo());
		//费用流水号
		item.setFyid(vo.getPkSettle());
		//费用明细流水号
		item.setFymxlsh(vo.getPkCgop()==null?"-":vo.getPkCgop());//@todo
		//CFMXHM
		item.setCfmxhm(vo.getPkCgop()==null?"-":vo.getPkCgop()); //@todo
		//SFXMBM收费项目编码/CV07_10_001（费用分类代码字典）
		String sfxmbm = getDictCodeMapDefault("rhip.costClass",vo.getCateCode(),"14");//@todo99
		item.setSfxmbm(sfxmbm);

		//收费目录类别/1药品2诊疗项目3材料和服务设施
		String sfmllb="";
		String codeOrdtype=vo.getCodeOrdtype();
		if(codeOrdtype==null||codeOrdtype.length()<2) codeOrdtype="**";
		if(codeOrdtype.substring(0, 2).equals("01")){
			sfmllb="1";
		}else if(codeOrdtype.substring(0, 2).equals("07")){
			sfmllb="3";
		}else{
			sfmllb="2";
		}
		item.setSfmllb(sfmllb);
		//医保收费项目编码
		item.setYbsfxmbm(vo.getCodeCg());
		//医保收费项目名称
		item.setYbsfxmmc(vo.getNameCg());
		//医疗项目类别编码
		String ylxmlb = getDictCodeMapDefault("rhip.costClass",vo.getCateCode(),"016");
		item.setYlxmlb(ylxmlb);
		//医院项目类别名称
		item.setYlxmlbmc(vo.getCateName());
		//单价
		if(vo.getPrice()!=null) item.setDj(vo.getPrice().toString());
		//数量
		if(vo.getQuan()!=null) item.setSl(vo.getQuan().toString());
		//总金额
		if(vo.getAmount()!=null) item.setSl(vo.getAmount().toString());
		//剂型
		//使用频次
		//用药途径
		//用药天数
		//使用方法
		//计量单位
		item.setJldw(vo.getUnitName());
		//规格
		item.setGg(vo.getSpec());
		//医保结算范围费用总额
		//MXXMYBFNFLZFJE明细项目医保范围内分类自负金额
		item.setMxxmybfnflzfje("0");//@todo

		item.setYbjsfwfyzje(vo.getAmount().toString());//@todo
		//个人自费
		item.setGrzf(vo.getAmountPi()==null?"0":vo.getAmountPi().toString());
		//自付比例
		item.setZfbl(vo.getRatioSelf()==null?"0":vo.getRatioSelf().toString());
		//收费/退费标志代码/1收费2退费9不详
		String stbzDm="1";
		if(vo.getAmount().doubleValue()<0){
			stbzDm="2";
		}
		item.setStbzDm(stbzDm);
		//收费项目编码
		//医嘱ID
		//流水号
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//费用明细流水号
		//医嘱号
		//医嘱内容
		//执行次数
		//医疗保险类型
		//婴儿标志
		//MZFYFL_DM/门诊费用分类代码/HIS18_01_008
		String mzfyflDm = getDictCodeMapDefault("rhip.cateCode",vo.getCateCode(),"I");
		item.setMzfyflDm(mzfyflDm);
		//项目名称
		item.setXmmc(vo.getNameCg());
		//项目代码
		item.setXmdm(vo.getCodeCg());
		//明细项目单位
		item.setMxxmdw(vo.getUnitName());
		//明细项目单价
		item.setMxxmdj(vo.getPrice()==null?"0":vo.getPrice().toString());
		//明细项目数量
		String num=vo.getQuan()==null?"0":vo.getQuan().toString();
		Double numd=Double.parseDouble(num);
		Integer numI=numd.intValue();
		item.setMxxmsl(numI.toString());
		//明细项目金额
		item.setMxxmje(vo.getAmount()==null?"0":vo.getAmount().toString());
		//明细项目医保范围外自费金额
		item.setMxxmybfwwzfze("0");//@todo
		//明细项目医保范围内分类自负金额
		item.setMxxmybfwwzfze("0");//@todo
		//医保限额
		item.setYbxe("0");//@todo

		return item;
	}

	//其他数据集
	//1/检查报告
	public static PtExamReport createPtExamReport(IUser pUser,PatListVo pat,PacsRptInfo rpt) throws Exception {
		if(rpt==null) return null;
		PtExamReport item=new PtExamReport();
		Integer deptType = rpt.getDeptType();//
		String bgksbm = "-";
		String bgksmc = "-";
		String[] bgksInfo = {};
		if (deptType != null) {
			bgksInfo = RpDictUtils.getbgksInfo(deptType);
			if (bgksInfo != null) {
				bgksbm = bgksInfo[0];
				bgksmc = bgksInfo[1];
			}
		}

		item.setName("检查报告");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//检查部位ACR编码
		//检查流水号
		item.setJclsh(rpt.getRecordNo());//
		String bcksj = rpt.getReportDescTxt();
		if (bcksj==null) {
			bcksj = rpt.getReportDiagTxt();
			if (bcksj == null) {
				bcksj = "-";
			}
		}
		//检查报告结果-客观所见
		item.setBckgsj(bcksj);
		//病人标识类别
		//1	门诊 2	住院		3	体检		9	其它
		item.setBrbslb("2");
		//检查报告结果-主观提示
		item.setBczgts(rpt.getReportDiagTxt()==null?"-":rpt.getReportDiagTxt());
		//身份证件类别代码
		item.setIdCardCode(pat.getDtIdtype());
		//身份证件号码
		item.setIdCard(pat.getIdNo());
		//卡号
		item.setKh(pat.getBarcode());
		//卡号
		item.setKlx(pat.getCardType());

		//申请单号
		item.setSqdh(rpt.getHisUid()==null?"-":rpt.getHisUid());
		//申请科室编码
		item.setSqksbm(pat.getDeptCodeOrig()== null?"1010203":pat.getDeptCodeOrig());//@todo取对照
		//申请科室名称
		item.setSqksmc(pat.getDeptName());
		//检查项目代码
		item.setJcxmdm("-");//@todo关联申请项目
		//检查项目名称
		item.setJcxmmc(rpt.getBodyOfCase());
		//检查类型/其他//@todo/CVX_TypeOfExam/检查类型字典
		item.setJclx("99");
		//检查方法
		//检查设备仪器名称*
		item.setJcsbyqmc("-");//@todo
		//检查设备仪器号*
		item.setJcsbyqh("-");//@todo
		//检查时间
		item.setJcrq(DateUtils.getDateStr(rpt.getReportDate()));//@todo
		//阴阳性
		//是否有影像
		item.setSfyyx("1");//@todo
		//是否放射性
		item.setSffsx("0");//@todo
		//报告时间
		item.setBgsj(DateUtils.getDateStr(rpt.getReportDate()));
		//审核日期
		item.setShrq(DateUtils.getDateStr(rpt.getReportDate()));//@todo
		//报告备注
		//预约开始时间
		//预约结束时间
		//申请医生工号
		item.setSqysgh("-");//@todo
		//申请医生姓名
		item.setSqysxm("-");//@todo
		//开单/预约时间
		//作废判别
		//影像数量
		//报告医生姓名
		item.setBgysxm(rpt.getReportDocname()==null?"-":rpt.getReportDocname());
		//审核医生身份证号
		//审核医生姓名
		//明细号码
		item.setMxhm(rpt.getHisUid());//@todo
		//大型仪器编码
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		//影像类型
		//影像序列描述
		//影像序列唯一ID
		//影像检查描述
		//影像检查唯一ID
		//影像唯一ID
		//申请日期
		item.setSqrq(DateUtils.getDateStr(rpt.getReqDate()==null?rpt.getReportDate():rpt.getReqDate()));
		item.setYzid(rpt.getHisUid()==null?"-":rpt.getHisUid());
		//检查报告单机构(科室)
		item.setBcksmc("-");
		//检查科室名称
		item.setJcksmc("-");
		//检查结果代码
		item.setJcjgDm("1");
		item.setJcksmc("-");
		item.setJcysgh("-");
		item.setBgksmc(bgksbm);//报告科室名称
		item.setBgysgh("-");
		item.setShysgh("-");
		//检查医生姓名
		item.setJcyscxm("-");
		item.setBgks_bm(bgksmc);//报告科室编码
		item.setShysgh("-");//
		item.setSqyy_bm("");//申请医院编码
		item.setKdsj(DateUtils.getDateStr(rpt.getReqDate()==null?rpt.getReportDate():rpt.getReqDate()));
		item.setJcff("-");
		item.setJcbwacrbm("-");//
		item.setJcjgDm("");//
		item.setSblb_bm("");//
		item.setSblb_mc("");//
		item.setDxyqbm("");//
		item.setJczdhts("");//
		item.setYyx("");//
		item.setZfpb("");//
		item.setYxsl("");//
		item.setShysxm("");//
		item.setBgbz("");//
		item.setYykssj("");//
		item.setYyjssj("");//
		item.setDjsj("");//
		item.setDjrygh("");//
		item.setDjrymc("");//
		item.setDjrygh("");//
		item.setZhxgsj("");//
		item.setZhxgrygh("");//
		item.setZhxgrymc("");//
		item.setJcjldw("-");//检查计量单位


		List<PTExamPACS> ptExamPACS = new ArrayList<PTExamPACS>();
		PTExamPACS child=new PTExamPACS();
		child.setStudyUID("-");//影像检查唯一ID
		child.setSeriesUID("-");
		child.setStudyDescription("-");
		child.setYxh("");
		child.setJch("-");
		child.setJcbw("_");
		child.setJcbwyff("-");
		child.setZyjcxx("--");//专有检查信息
		child.setYyxdm("1");// 阴阳性代码
		child.setLczd("-");//临床诊断
		child.setYxbxhjcsj("-");//影像表现或检查所见
		child.setJczdhts1("");//
		child.setBzhjy("_");//
		child.setYxfwqdz("-");//
		child.setSeriesDescription("-");//
		child.setSopClassUID("-");//
		child.setInstanceUID("-");
		ptExamPACS.add(child);
		item.setPtExamPACSs(ptExamPACS);
		return item;
	}

	//2/检验报告
	public static PtLabReport createPtLabReport(IUser pUser,PatListVo pat,LisResultInfo rpt) throws Exception {
		if(rpt==null) return null;
		PtLabReport item=new PtLabReport();
		item.setName("检验报告");
		//就诊流水号
		item.setJzlsh(pat.getCodePv());
		//检验报告单号
		item.setJybgdh(rpt.getRptNo());
		//医嘱ID
		item.setYzid(rpt.getItmOrder().toString());
		//检验ID
		item.setJyid(rpt.getItmId().toString());
		item.setKlx(pat.getCardType());//卡类型
		//病人标识类别
		//1	门诊 2	住院		3	体检		9	其它
		item.setBrbslb("2");
		//检验报告单代码
		//检验报告单—项目名称（检查目的）
		item.setJybgdmc(rpt.getJyName());
		//审核医生姓名
		item.setShysxm(rpt.getShName());
		//*审核医生工号
		item.setShysgh("-");
		//*报告医生姓名
		item.setBgysxm(rpt.getDoctor());
		//*报告医生工号
		item.setBgysgh("-");
		item.setSfbslb_dm("01");//身份标识代码
		item.setSfbshm("-");//身份标识号码
		item.setKh(pat.getBarcode());//卡号
		item.setBgdlbd_bm("1");//报告单类别代码
		item.setJyxm_dm("-");//检验项目代码
		item.setJyxm_mc("-");//检验项目名称
		item.setShrq(DateUtils.getDateStr(rpt.getJsDate()));//审核日期
		item.setShysgh("-");//审核医生工号
		item.setShysxm("-");//审核医生姓名
		item.setBgrq(rpt.getShDate());//报告日期
		item.setBgysgh("-");//报告医生工号
		item.setBgysxm("-");//报告医生姓名
		item.setSqysgh("-");//申请医生姓名
		item.setSqysxm("-");//申请医生姓名
		item.setSqksdm("1050105");//@todo取对照//申请科室代码
		item.setSqrq(DateUtils.getDateStr(rpt.getJsDate()));//申请日期
		item.setSqyy_bm("-");//申请医院编码 （非必填）
		item.setSqyy_mc("-");//申请医院编码 （非必填）
		item.setSqksbm("");// 申请科室编码
		item.setSqksmc("");// 申请科室名称
		item.setJybgdjgdm("");// 检验报告单机构代码
		item.setJcysgh("123");// 检测医生工号
		item.setJcysxm("");// 检测医生姓名
		item.setBblbdm("");// 标本代码（可选）
		item.setBb_mc("-");// 标本名称
		item.setCjsj("");// 采集时间（可选）
		item.setCjbw("");// 采集部位（可选）
		item.setTxdm("");// 条形代码
		item.setYbzt("");// 样本状态
		item.setJcsbyqmc("");// 仪器名称
		item.setYebz("0");// 婴儿标记
		item.setDyrq("");// 打印日期（可选）




		//申请医生姓名
		//申请医生工号
		//申请日期
		//检验日期
		item.setJyrq(DateUtils.getDateStr(rpt.getJsDate()));
		//审核日期

		//标本代码
		//审核医生工号2
		//仪器名称
		//样本状态
		//婴儿标志
		//急诊标志
		item.setJzbz("1");	//1是 0 否@todo
		//采集时间
		//样本接收时间
		item.setYbjssj(DateUtils.getDateStr(rpt.getJsDate()));//@todo
		//采集部位
		//条形代码
		//申请科室编码
		//item.setSqksdm("1050105");//@todo取对照
		//检验报告单机构（科室）
		item.setJybgdjg(rpt.getUnitName());	//@todo
		//备注
		item.setBz("");
		//患者姓名
		item.setHzxm(pat.getName());
		//性别代码
		item.setXbdm(pat.getSexSpcode());
		//出生日期
		item.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
		item.setBq("-");//病区（可选
		item.setCh("-");//床号（可选
		item.setJybgddm("-");//检验报告单编码（可选
		item.setCfid("");//处方id（可选
		item.setJybgdmc("");//检验报告单一项目名称（可选
		item.setShysgh2("");//审核医生公告2（可选
		item.setJbzd_bm("");//疾病诊断编码（可选
		item.setBgbz("-");//报告备注（可选
		item.setDjsj("");//登记时间（可选
		item.setDjrygh("");//登记人员工号（可选
		item.setDjrymc("");//登记人员名称（可选
		item.setZhxgsj("");//最后修改时间（可选
		item.setZhxgrygh("");//最后修改人员工号（可选
		item.setZhxgrymc("");//最后修改人员名称（可选*/
		/*
		 *//*
		 * 培养结果
		 *//*
		List<PtBacteriaResult> resultList = new ArrayList<PtBacteriaResult>();
		PtBacteriaResult ptBacteriaResult = new PtBacteriaResult();
		ptBacteriaResult.setXjjglsh("");//细菌结果流水号
		ptBacteriaResult.setXjid("");//细菌代码
		ptBacteriaResult.setXjbgrq("");//细菌报告日期
		ptBacteriaResult.setZjts("");//专家提示 （可选）
		ptBacteriaResult.setXjmc("");//细菌名称
		ptBacteriaResult.setJljs("");//菌落计数
		ptBacteriaResult.setXjxh("");//细菌序号
		ptBacteriaResult.setPysj("");//培养时间
		ptBacteriaResult.setPytj("");//培养条件
		ptBacteriaResult.setFxsf("");//发现方式
		ptBacteriaResult.setYqbh("");//仪器编号（可选
		ptBacteriaResult.setJcjg("");//检测结果
		ptBacteriaResult.setYqmc("");//仪器名称
		ptBacteriaResult.setJcjgwzxs("");//检测结果文字描述
		ptBacteriaResult.setXjlb("");//细菌类别
		ptBacteriaResult.setSbbm("");//设备编码（可选
		ptBacteriaResult.setPyj("");//培养基
		ptBacteriaResult.setXjsybxh("");//细菌试验板序号（可选
		ptBacteriaResult.setSybmc("");//试验板名称（可选
		ptBacteriaResult.setPyjyxmmc("");//培养检验项目名称
		ptBacteriaResult.setPyjyxmdm("");//培养检验项目代码
		ptBacteriaResult.setPyjglsh("");//培养检结果流水号
		ptBacteriaResult.setXjpylx("");//细菌培养类型（可选）
		ptBacteriaResult.setXjpyjg("");//细菌培养结果（可选
		ptBacteriaResult.setJglx("");//结果类型
		resultList.add(ptBacteriaResult);
		item.setPtBacteriaResults(resultList);
		*//*
            药敏结果 PtAllergyResult
		 *//*
		List<PtAllergyResult> ptAllergyList = new ArrayList<PtAllergyResult>();
		PtAllergyResult ptAllergy = new PtAllergyResult();
		ptAllergy.setYmbgrq(DateUtils.getDateStr(pat.getDateReg()));//药敏报告日期
		ptAllergy.setXjdh("11");//细菌代号
		ptAllergy.setXjjglsh("111");//细菌结果流水号
		ptAllergy.setJcff("纸片法");//检测方法
		ptAllergy.setDyxh("");//打印序号（可选
		ptAllergy.setYmjglsh(rpt.getRptNo());//药敏结果流水号
		ptAllergy.setYhhzj("");//抑菌环直径（可选
		ptAllergy.setJynd("");//抑菌浓度（可选
		ptAllergy.setZphyl("");//纸片含药量（可选
		ptAllergy.setZphyldw("");//纸片含药量单位（可选
		ptAllergy.setSybxh("");//试验板序号（可选
		ptAllergy.setJcjgms("耐药");//检验结果描述
		ptAllergy.setYmdm("amx");//药敏代码
		ptAllergy.setYmmc("测试");//药敏名称
		ptAllergy.setCkfw("");//参考范围
		ptAllergyList.add(ptAllergy);
		item.setPtAllergyResults(ptAllergyList);*/
		/*	*//*
			检验明细PtLabDetail
		 *//*
		List<PtLabDetail> ptLabDetailArrayList = new ArrayList<PtLabDetail>();
		PtLabDetail ptLabDetail = new PtLabDetail();
		ptLabDetail.setCkdx("");//参考值范围 （可选
		ptLabDetail.setJyff_dm("");//检验方法代码
		ptLabDetail.setLoindbm("");//LOINC编码
		ptLabDetail.setYcts_dm("");//异常提示代码
		//ptLabDetail.setWjzbz("");//危急值标志
		ptLabDetail.setJyxmywmc("");//检验项目英文名称
		ptLabDetail.setJymxlsh("");//检验明细流水号
		ptLabDetail.setJymxrq("");//检验明细日期
		ptLabDetail.setJybgrq("");//检验报告日期
		ptLabDetail.setMxyqbh("");//明细仪器编号
		ptLabDetail.setMxyqmc("");//明细仪器名称
		//ptLabDetail.setJyxmmc("");//检验明细项目名称
		ptLabDetail.setJyxmdm("");//检验明细项目代码
		ptLabDetail.setJldw("");//计量单位
		ptLabDetail.setLcyy("");//临床意义
		ptLabDetail.setTs("");//提示
		ptLabDetail.setXssx("");//显示顺序
		ptLabDetail.setJyzb_dm("");//检验指示代码
		ptLabDetail.setJyjg("");//检验结果
		ptLabDetail.setJyjgdx("");//检验结果（定性）
		ptLabDetail.setJyjgdl("");//检验结果（定量）
		ptLabDetail.setJydyxh("");// 打印序号
		ptLabDetail.setMxbz("");// 明细备注
		ptLabDetail.setSfdm("");// 收费代码
		ptLabDetail.setJylb_dm("");// 检验类别代码
		ptLabDetail.setJcrbm("");// 检测人编码
		ptLabDetail.setSbm("");// 设备码
		ptLabDetail.setJyrq1("");// 检验日期1
		ptLabDetail.setShr("");// 审核医生
		ptLabDetail.setShrbm("");// 审核医生工号
		ptLabDetail.setXgyzidhcfxmmxbh("");// 相关医嘱ID或处方名称编码（可选
		ptLabDetailArrayList.add(ptLabDetail);
		item.setPtLabDetails(ptLabDetailArrayList);*/
		return item;
	}

	//明细
	public static PtLabDetail createPtLabDetail(IUser pUser,PatListVo pat,LisResultInfo rpt) throws Exception {
		if(rpt==null) return null;
		PtLabDetail item=new PtLabDetail();
		//危急值标志
		item.setWjzbz("0");//1 是  0 否 @todo
		//相关医嘱ID或处方项目明细编号
		//收费代码
		//审核人编码
		//检测人编码
		//审核人
		//检测人
		//检验项目名称
		item.setJyxmmc(rpt.getItemName());
		//检验项目代码
		item.setJyxmdm(rpt.getItemCode());
		//检查/检验计量单位
		item.setJldw(rpt.getUnit());
		//检测方法
		item.setJyff_dm("114");//其他
		//显示顺序
		if(rpt.getItmOrder()!=null) item.setXssx(rpt.getItmOrder().toString());
		//提示
		item.setTs("");//上下箭头
		//临床意义
		//参考值范围
		item.setCkdx(rpt.getRefer1Min()+"-"+rpt.getRefer1Max());
		//检验结果
		item.setJyjg(rpt.getResult());
		//检验项目英文简称
		item.setJyxmywmc(rpt.getItemCode());
		//设备码
		//明细号码
		if(rpt.getItmId()!=null) item.setJymxlsh(rpt.getItmId().toString());//@todo
		//检验明细日期
		item.setJymxrq(DateUtils.getDateStr(rpt.getJsDate()));
		//检验报告日期
		item.setJybgrq(DateUtils.getDateStr(rpt.getJsDate()));
		//检验结果
		String result="3";
		try {
			if(Double.parseDouble(rpt.getResult())>=Double.parseDouble(rpt.getRefer1Min())&&Double.parseDouble(rpt.getResult())<=Double.parseDouble(rpt.getRefer1Max())){
				result="1";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		item.setJyjgdx(result);
		item.setJyjgdl(result);
		item.setLoindbm("");//LOINC编码
		item.setYcts_dm("");//异常提示代码
		item.setMxyqbh("");//明细仪器编号
		item.setMxyqmc("");//明细仪器名称
		item.setLcyy("");//临床意义
		item.setJyzb_dm("");//检验指示代码
		item.setJydyxh("");// 打印序号
		item.setMxbz("");// 明细备注
		item.setSfdm("");// 收费代码
		item.setJylb_dm("");// 检验类别代码
		item.setJcrbm("");// 检测人编码
		item.setSbm("");// 设备码
		item.setJyrq1("");// 检验日期1
		item.setShr("");// 审核医生
		item.setShrbm("");// 审核医生工号
		item.setXgyzidhcfxmmxbh("");// 相关医嘱ID或处方名称编码（可选
		return item;
	}

	public static String getInvFeeAmount(List<RhipBlSettleItemVo> dts,String text){
		String rtn="";
		if(dts==null||dts.size()==0||text==null) return rtn;
		for (RhipBlSettleItemVo item : dts) {
			if(item.getBillName()!=null&&item.getBillName().equals(text)){
				return item.getAmount();
			}
		}
		return rtn;
	}

	public static String getInvFeeAmountBak(List<BlInvoiceDt> dts,String text){
		String rtn="";
		if(dts==null||dts.size()==0||text==null) return rtn;
		for (BlInvoiceDt blInvoiceDt : dts) {
			if(blInvoiceDt.getNameBill()!=null&&blInvoiceDt.getNameBill().equals(text)){
				Double  amount= blInvoiceDt.getAmount();
				if(amount!=null){
					rtn=amount.toString();
					return rtn;
				}

			}
		}
		return rtn;
	}
	/**
	 * 查询病历文档段落内容
	 * 说明：1、病程记录：typeCode[1000]paraCode[null] 2、主诉：typeCode[090001]paraCode[01]...
	 * @param pkPv就诊主键
	 * @param typeCode文档分类编码
	 * @param paraCode文档段落编码
	 * @return
	 */
	public String getEmrParaText(String pkPv , String typeCode,String paraCode,EmrMedRec rec){
		String text="";
		Iterator<Element> it = null;
		Element node=null;
		String nodeName="";
		String paraCodeElement="";
		Element nodeText=null;
		String recDateStr="";
		Date recDate=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


		String flagCourseStr=rec.getDocType().getFlagCourse();

		Boolean flagCourse=flagCourseStr==null?false:(flagCourseStr.equals("1")?true:false);
		if(rec.getPkDoc()!=null){
			if(rec==null||rec.getDocXml()==null) return "";
			String docXml=rec.getDocXml();
			if(docXml!=null&&!docXml.equals("")){
				try {
					Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
					Matcher matcher = pattern.matcher(docXml);
					if(matcher.find()){
						String str=matcher.group(1);
						docXml=docXml.replace(str,"");
					}

					SAXReader reader = new SAXReader();
					Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8")));
					Element root = document.getRootElement();
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
								if(paraCodeElement.equals(paraCode)||paraCode==null||paraCode.equals("")){
									nodeText= node.element("Content_Text");
									if(nodeText!=null){
										text=nodeText.getText();
									}
									break;
								}
							}

						}else{
							//病程
							if(node.attribute("pk_rec")==null) continue;
							if(node.attribute("rec_date")==null) continue;
							recDateStr=node.attribute("rec_date").getText();
							String pkRec=node.attribute("pk_rec").getText();
							if(recDate==null||recDate.compareTo(format.parse(recDateStr))<0){
								recDate=format.parse(recDateStr);
								nodeText= node.element("Content_Text");
								if(nodeText!=null){
									text = nodeText.getText();
								}


							}

						}
					}
					document.toString();


				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return "";
				}

			}
			if(text!=null){
				text=text.trim();
				//text=text.replaceAll("[\\t]", "");//text.replaceAll("[\\t\\n\\r]", ""); \t为制表符 \n为换行 \r为回车
				text= text.replaceAll("(\r?\n()+)", "\r\n");
				text=text.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");
			}
		}
		//System.out.println("text:"+text);
		return text;
	}

	/**
	 * 遍历病历文档段落内容
	 * 说明：1、病程记录：typeCode[1000]paraCode[null] 2、主诉：typeCode[090001]paraCode[01]...
	 * @param pkPv就诊主键
	 * @param typeCode文档分类编码
	 * @param paraCode文档段落编码
	 * @return
	 */
	public static void resolveEmrParaText(MedRecVo rec,IptAdmissionNote note){
		String text="";
		Iterator<Element> it = null;
		Element node=null;
		String nodeName="";
		String paraCode="";
		Element nodeText=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


		String flagCourseStr=rec.getDocType().getFlagCourse();

		Boolean flagCourse=flagCourseStr==null?false:(flagCourseStr.equals("1")?true:false);
		if(rec.getPkDoc()!=null){
			if(rec==null||rec.getDocXml()==null) return;
			String docXml=rec.getDocXml();
			if(docXml!=null&&!docXml.equals("")){
				try {
					Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
					Matcher matcher = pattern.matcher(docXml);
					if(matcher.find()){
						String str=matcher.group(1);
						docXml=docXml.replace(str,"");
					}

					SAXReader reader = new SAXReader();
					Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8")));
					Element root = document.getRootElement();
					it = root.elementIterator();
					while (it.hasNext()) {
						// 获取某个子节点对象
						node = it.next();
						nodeName=node.getName();
						if(nodeName==null||!nodeName.equals("Region")) continue;
						if(flagCourse==false){
							//非病程
							if(node.attribute("para_code")==null) continue;
							paraCode=node.attribute("para_code").getText();
							if(paraCode!=null){
								nodeText= node.element("Content_Text");
								if(nodeText!=null){
									text=nodeText.getText();
									text=text.trim();
									text= text.replaceAll("(\r?\n()+)", "\r\n");
									text=text.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");

									if(paraCode.equals("01")){
										//主诉
										note.setBzzs(text);
									}else if(paraCode.equals("03")){
										//现病史
										note.setBzxbs(text);
									}else if(paraCode.equals("04")){
										//既往史
										note.setBzjws(text);
										//系统回顾
										note.setBzxbs(text);//@todo
									}else if(paraCode.equals("04009")){
										//个人史
										note.setBzgrs(text);
									}else if(paraCode.equals("04010")){
										//婚育史
										note.setBzhys(text);
									}else if(paraCode.equals("04013")){
										//家族史
										note.setBzjzs(text);
									}else if(paraCode.equals("04011")){
										//月经史
										note.setBzyjs(text);
									}else if(paraCode.equals("xx")){
										//@todo
										//喂养史
										//note.setBZWYS("喂养史");
										//出生史
										//note.setBZCSS("出生史");
										//病史摘要
										//note.setBZBSZY("病史摘要");
									}

								}

							}
						}
					}
					document.toString();


				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return;
				}

			}
			if(text!=null){
				text=text.trim();
				text= text.replaceAll("(\r?\n()+)", "\r\n");
				text=text.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");
			}
		}
		//System.out.println("text:"+text);
		return;
	}


	/**
	 * 遍历病历文档段落内容(出院记录)
	 * 说明：1、病程记录：typeCode[1000]paraCode[null] 2、主诉：typeCode[090001]paraCode[01]...
	 * @return
	 */
	public static void resolveEmrParaTextDis(MedRecVo rec,IptLeaveRecord note){
		String text="";
		Iterator<Element> it = null;
		Element node=null;
		String nodeName="";
		String paraCode="";
		Element nodeText=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


		String flagCourseStr=rec.getDocType().getFlagCourse();

		Boolean flagCourse=flagCourseStr==null?false:(flagCourseStr.equals("1")?true:false);
		if(rec.getPkDoc()!=null){
			if(rec==null||rec.getDocXml()==null) return;
			String docXml=rec.getDocXml();
			if(docXml!=null&&!docXml.equals("")){
				try {
					Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
					Matcher matcher = pattern.matcher(docXml);
					if(matcher.find()){
						String str=matcher.group(1);
						docXml=docXml.replace(str,"");
					}

					SAXReader reader = new SAXReader();
					Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8")));
					Element root = document.getRootElement();
					it = root.elementIterator();
					while (it.hasNext()) {
						// 获取某个子节点对象
						node = it.next();
						nodeName=node.getName();
						if(nodeName==null||!nodeName.equals("Region")) continue;
						if(flagCourse==false){
							//非病程
							if(node.attribute("para_code")==null) continue;
							paraCode=node.attribute("para_code").getText();
							if(paraCode!=null){
								nodeText= node.element("Content_Text");
								if(nodeText!=null){
									text=nodeText.getText();
									text=text.trim();
									text= text.replaceAll("(\r?\n()+)", "\r\n");
									text=text.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");

									if(paraCode.equals("1200110")){
										//入院情况
										note.setRyqk(text);
									}else if(paraCode.equals("1200118")||paraCode.equals("1200115")){
										//治疗情况
										note.setZzjg(text);
									}else if(paraCode.equals("1200111")){
										//出院情况
										note.setCyqk(text);
									}else if(paraCode.equals("07001")){
										//入院诊断
										note.setRyzd(text);
									}else if(paraCode.equals("07002")){
										//出院诊断
										note.setCyzd(text);
									}else if(paraCode.equals("1200201")){
										//出院医嘱
										note.setCyyz(text);
									}

								}

							}
						}
					}
					document.toString();


				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return;
				}

			}
			if(text!=null){
				text=text.trim();
				text= text.replaceAll("(\r?\n()+)", "\r\n");
				text=text.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");
			}
		}
		//System.out.println("text:"+text);
		return;
	}

	public static String getDiagMainFlag(String flag){
		String rtn="2";
		if(flag!=null&&flag.equals("1")){
			rtn="1";
		}

		return rtn;
	}

//	public static String getSexcode(String sexCode){
//		String sex=sexCode;
////		if(sexCode==null||sexCode.equals("")){
////			sex="0";
////			return sex;
////		}
////		if(sex.equals("02")){
////			sex="1";
////		}else if(sex.equals("03")){
////			sex="2";
////		}else{
////			sex="9";
////		}
//		String str=ApplicationUtils.getPropertyValue("rhip.sex", "");
//		if(str==null||str.equals("")) return sexCode;
//		Map<String,String> map=(Map<String, String>) JSONUtils.parse(str);
//		if(map==null) return sexCode;
//		if(!map.containsKey(sexCode)) return sexCode;
//		sex=map.get(sexCode);
//		return sex;
//	}

	//诊断类别@todo
	public static String getDiagtypeCode(PvDiagVo diag){
		//1主要诊断；2初步诊断；3 入院诊断 ；4出院诊断；5门诊诊断
		String rtn="4";
		if(diag==null) return rtn;
		if(diag.getFlagMaj()!=null&&diag.getFlagMaj().equals("1")) return "1";
		String diagType=diag.getDtDiagtype();
		if(diagType==null||diagType.equals("")) return rtn;

		if(diagType.equals("0100")){
			//入院诊断
			rtn="3";
		}else if(diagType.equals("0103")){
			//出院诊断
			rtn="4";
		}else if(diagType.equals("0000")){
			//门诊诊断
			rtn="5";
		}
		return rtn;
	}

	public static String getPropValue(String code){
		String str=ApplicationUtils.getPropertyValue(code, "");
		return str;
	}

	public static String getDictCodeMap(String field,String code){
		String str=ApplicationUtils.getPropertyValue(field, "");
		if(str==null||str.equals("")) return "";
		Map<String,String> map=(Map<String, String>) JSONUtils.parse(str);
		if(map==null) return "";
		if(!map.containsKey(code)) return "";
		String rtnCode=map.get(code);
		return rtnCode;
	}
	public static String getDictCodeMapDefault(String field,String code,String dCode){
		String str=ApplicationUtils.getPropertyValue(field, "");
		if(str==null||str.equals("")) return dCode;
		Map<String,String> map=(Map<String, String>) JSONUtils.parse(str);
		if(map==null) return dCode;
		if(!map.containsKey(code)) return dCode;
		String rtnCode=map.get(code);
		return rtnCode;
	}
//public static String getMarrayCode(String code){
//
//		String rtnCode="9";
//		if(code==null||code.equals("")) return rtnCode;
//		if(code.equals("00")){
//			//未婚
//			rtnCode="1";
//		}else if(code.equals("01")){
//			//已婚
//			rtnCode="2";
//		}else if(code.equals("02")){
//			//初婚
//			rtnCode="2";
//		}else if(code.equals("03")){
//			//再婚
//			rtnCode="2";
//		}else if(code.equals("04")){
//			//复婚
//			rtnCode="2";
//		}else if(code.equals("05")){
//			//丧偶
//			rtnCode="3";
//		}else if(code.equals("06")){
//			//离婚
//			rtnCode="4";
//		}else{
//			//其他
//			rtnCode="9";
//		}
//		return rtnCode;
//	}

	//职业
	public static String getOccuCode(String code){
//		String rtnCode="90";
//		if(code==null||code.equals("")) return rtnCode;
//		if(code.equals("00")||code.equals("08")){
//			//国家公务员
//			rtnCode="11";
//		}else if(code.equals("01")||code.equals("04")){
//			//专业技术人员
//			rtnCode="12";
//		}else if(code.equals("02")||code.equals("03")||code.equals("05")||code.equals("09")){
//			//职员
//			rtnCode="17";
//		}else if(code.equals("10")){
//			//企业管理人员
//			rtnCode="21";
//		}else if(code.equals("11")){
//			//工人
//			rtnCode="24";
//		}else if(code.equals("12")){
//			//农民
//			rtnCode="27";
//		}else if(code.equals("13")){
//			//学生
//			rtnCode="31";
//		}else if(code.equals("06")){
//			//现役军人
//			rtnCode="37";
//		}else if(code.equals("14")){
//			//自由职业者
//			rtnCode="51";
//		}else if(code.equals("15")){
//			//个体经营者
//			rtnCode="54";
//		}else if(code.equals("16")){
//			//无业人员
//			rtnCode="70";
//		}else if(code.equals("06")){
//			//退（离）休人员
//			rtnCode="80";
//		}else{
//			//其他
//			rtnCode="90";
//		}
//		return rtnCode;
		String str=(code==null||code.equals(""))?"07":code;
		return getDictCodeMap("rhip.occu", str);
	}

	//民族@todo
	public static String getNationCode(String code) {
//		String rtnCode=code;
//		if(code==null||code.equals("")) rtnCode="HA"; //汉族
//		return rtnCode;
		if(code==null||code.equals("")) code="01";
		String str=ApplicationUtils.getPropertyValue("rhip.nation.rmvzr", "");
		if(str!=null&&str.equals("1")){
			String first=code.substring(0, 1);
			if(first!=null&&first.equals("0")){
				return code.replaceFirst("0", "");
			}else{
				return code;
			}

		}else{
			return code;
		}
	}

	//国籍
	public static String getCountryCode(String code) {
		//@todo对照
		String countryCode=code;
		if(code==null||code.equals("")) countryCode="cn";	//中国
		return countryCode;
	}

	public static String getAboCode(String code) {
		/*//1	A型  2	B型		3	O型		4	AB型		5	不详
		String rtnCode=code;
		if(code==null||code.equals("")) rtnCode="5"; //不详
		return rtnCode;*/
		return getDictCodeMap("rhip.abo", code);

	}

	public static String getRhCode(String code){
		//1	阴性	2	阳性		3	不详		4	未查
		/*String rtnCode="3";
		if(code==null||code.equals("01")){
			rtnCode="4";
		}else if(code.equals("02")){
			rtnCode="2";
		}
		return rtnCode;*/
		return getDictCodeMap("rhip.rh", code);

	}

	public static String getAutopsyCode(String code){
		//1是2否3非死亡患者
		String rtnCode="3";
		if(code==null||code.equals("")){
			rtnCode="3";
		}else if(code.equals("0")){
			rtnCode="2";
		}else if(code.equals("1")){
			rtnCode="1";
		}
		return rtnCode;
	}

	//付款方式代码@todo
	public static String getMedPayMode(String code){
		//01	城镇职工基本医疗保险
		//02	城镇居民基本医疗保险
		//03	新型农村合作医疗
		//04	贫因救助
		//05	商业医疗保险
		//06	全公费
		//07	全自费
		//99	其他
		if(code==null||code.equals("")||code.equals("9")) return "99";
		String rtnCode="0"+code;
		if(rtnCode.length()>2) rtnCode="99";
		return rtnCode;
	}

	public static EmrHomePageDiags getPriDiag(List<EmrHomePageDiags> diags){
		if(diags==null) return null;
		for (EmrHomePageDiags diag : diags) {
			if(diag.getFlagPrimary()!=null&&diag.getFlagPrimary().equals("1")) return diag;
		}

		return null;
	}

	public static void setPageCharges(IptMedicalRecordPage item,List<EmrHomePageCharges> charges){
		String amount="0";
		//初始化为0
		item.setGenerMediServCharge(amount);
		item.setGenerTreatHandlingFee(amount);
		item.setGenerMediServCharge(amount);
		item.setGenerTreatHandlingFee(amount);
		item.setNurse(amount);
		item.setGenerMediServChargeOther(amount);
		item.setPathologicalFee(amount);
		item.setLaboratoryFee(amount);
		item.setImagingFee(amount);
		item.setClinicalDiagnosisFee(amount);
		item.setNonoperativeTreatFee(amount);
		item.setClinicalPhysicalTreatment(amount);
		item.setSurgicalTreatment(amount);
		item.setEstheticFee(amount);
		item.setOperationFee(amount);
		item.setRehabilitationFee(amount);
		item.setZylzyzdf(amount);
		item.setXyFee(amount);
		item.setAntibacterialDrugExp(amount);
		item.setMedicineChina(amount);
		item.setHerbalMedicineFee(amount);
		item.setBloodFee(amount);
		item.setAcpFee(amount);
		item.setGcpFee(amount);
		item.setNxyzFee(amount);
		item.setXbyzFee(amount);
		item.setYcyycxFee(amount);
		item.setZlyycxFee(amount);
		item.setSsyycxzlFee(amount);
		item.setOtherFee(amount);
		item.setZyzlFee(amount);
		for (EmrHomePageCharges charge : charges) {
			String itemCode=charge.getItemCode();
			if(itemCode==null) continue;
			amount="0";
			if(charge.getItemAmount()!=null) amount=charge.getItemAmount().toString();
			//综合医疗服务类-一般医疗服务费-中医辨证论治费
			//综合医疗服务类-一般医疗服务费-中医辨证论治会诊费
			//一般治疗操作费

			//综合医疗服务类其他费用
			//病理诊断费
			//实验室诊断费
			//影像学诊断费
			//临床诊断项目费
			//非手术治疗项目费
			//临床物理治疗费
			//手术治疗费
			//麻醉费
			//手术费
			//康复费
			//中医类-中医诊断费
			//中医治疗费
			//中医类-中医治疗费-中医外治费
			//中医类-中医治疗费-中医骨伤费
			//中医类-中医治疗费-针刺与灸法费
			//中医类-中医治疗费-中医推拿治疗费
			//中医类-中医治疗费-中医肛肠治疗费
			//中医类-中医治疗费-中医特殊治疗费
			//中医类-中医其他费
			//中医类-中医其他费-中医特殊调配加工费
			//中医类-中医其他费-辨证施膳费
			//中成药费
			//中药类-中成药费-医疗机构中药制剂费
			//西药费
			//中草药费
			//抗菌药物费用
			//血费
			//白蛋白类制品费
			//球蛋白类制品费
			//凝血因子类制品费
			//细胞因子类制品费


			if(itemCode.equals("1")){
				//一般医疗服务费
				item.setGenerMediServCharge(amount);
			}else if(itemCode.equals("2")){
				//一般治疗操作费
				item.setGenerTreatHandlingFee(amount);
			}else if(itemCode.equals("3")){
				//护理费
				item.setNurse(amount);
			}else if(itemCode.equals("4")){
				//其他费用
				item.setGenerMediServChargeOther(amount);
			}else if(itemCode.equals("5")){
				//病理诊断费
				item.setPathologicalFee(amount);
			}else if(itemCode.equals("6")){
				//实验室诊断费
				item.setLaboratoryFee(amount);
			}else if(itemCode.equals("7")){
				//影像学诊断费
				item.setImagingFee(amount);
			}else if(itemCode.equals("8")){
				//临床诊断项目费
				item.setClinicalDiagnosisFee(amount);
			}else if(itemCode.equals("9")){
				//非手术治疗项目费
				item.setNonoperativeTreatFee(amount);
			}else if(itemCode.equals("9-1")){
				//临床物理治疗费
				item.setClinicalPhysicalTreatment(amount);
			}else if(itemCode.equals("10")){
				//手术治疗费
				item.setSurgicalTreatment(amount);
			}else if(itemCode.equals("10-1")){
				//麻醉费
				item.setEstheticFee(amount);
			}else if(itemCode.equals("10-2")){
				//手术费
				item.setOperationFee(amount);
			}else if(itemCode.equals("11")){
				//康复费
				item.setRehabilitationFee(amount);
			}else if(itemCode.equals("12")){
				//中医诊断费
				item.setZylzyzdf(amount);
			}else if(itemCode.equals("13")){
				//西药费
				item.setXyFee(amount);
			}else if(itemCode.equals("13-1")){
				//抗菌药物费用
				item.setAntibacterialDrugExp(amount);
			}else if(itemCode.equals("14")){
				//中成药费
				item.setMedicineChina(amount);
			}else if(itemCode.equals("15")){
				//中草药费
				item.setHerbalMedicineFee(amount);
			}else if(itemCode.equals("16")){
				//血费
				item.setBloodFee(amount);
			}else if(itemCode.equals("17")){
				//白蛋白类制品费
				item.setAcpFee(amount);
			}else if(itemCode.equals("18")){
				//球蛋白类制品费
				item.setGcpFee(amount);
			}else if(itemCode.equals("19")){
				//凝血因子类制品费
				item.setNxyzFee(amount);
			}else if(itemCode.equals("20")){
				//细胞因子类制品费
				item.setXbyzFee(amount);
			}else if(itemCode.equals("21")){
				//检查用一次性医用材料费
				item.setYcyycxFee(amount);
			}else if(itemCode.equals("22")){
				//治疗用一次性医用材料费
				item.setZlyycxFee(amount);
			}else if(itemCode.equals("23")){
				//手术用一次性医用材料费
				item.setSsyycxzlFee(amount);
			}else if(itemCode.equals("24")){
				//其他费
				item.setOtherFee(amount);
			}
		}

	}

	/**
	 * 创建手术记录报告
	 * @return
	 */
	public static PtOperation createPtOperationReport (IUser pUser, PatListVo pat, OperationRecordInfo operation){
		PtOperation ptOperation = new PtOperation();
		ptOperation.setName("手术记录");
		ptOperation.setJzlsh(pat.getCodePv());//就诊流水号
		ptOperation.setSsjllsh(operation.getSsjllsh());//手术记录流水号
		ptOperation.setMzzybz(operation.getMzzybz()==null?"_":operation.getMzzybz());//门诊住院标识
		ptOperation.setJswlbz("9");//计生五类手术标识
		ptOperation.setSfbsdm(pat.getDtIdtype());//身份标识代码
		ptOperation.setSfbshm(pat.getIdNo());//身份标识号码
		ptOperation.setKh(pat.getBarcode() == null ? "-":pat.getBarcode());//卡号
		ptOperation.setKlx(RpDictUtils.getKlx(pat));// 卡类型
		ptOperation.setSsdm(operation.getSsdm() == null ? "-":operation.getSsdm());//手术代码
		ptOperation.setSsmc(operation.getSsmc());//手术名称
		ptOperation.setSsbwdm(operation.getSsbwdm() == null? "-":operation.getSsbwdm());//手术部位代码
		ptOperation.setMzff(operation.getMzff() == null ?"13":operation.getMzff());//麻醉方法 CV06_00_103 13基础麻醉 todo
		ptOperation.setMzfy(operation.getMzfy() == null?"2":operation.getMzfy());//麻醉反应
		ptOperation.setSszxksbm(operation.getSszxksbm() == null?"-":operation.getSszxksbm());//手术执行科室编码
		ptOperation.setSszxksmc(operation.getSszxksmc() == null?"_":operation.getSszxksmc());//手术执行科室名称
		ptOperation.setSszljczdwbydyldm("9");//手术治疗检查诊断为本院第一例代码 todo
		ptOperation.setSslx(operation.getSslx() == null ?"-":operation.getSslx());//手术类型
		ptOperation.setOperationLevel(operation.getOperationlevel()== null?"1":operation.getOperationlevel());//手术级别
		ptOperation.setZrssbz(operation.getZrssbz() == null?"0":operation.getZrssbz());//植入手术标志
		ptOperation.setSsjgms(operation.getSsjgms() == null?"-":operation.getSsjgms());//手术经过描述
		ptOperation.setSsly(operation.getSsly()== null?"-":operation.getSsly());//手术来源
		ptOperation.setSxl(operation.getSxl()== null?"-":operation.getSxl());//输血量
		ptOperation.setShxl(operation.getShxl()== null?"-":operation.getShxl());//失血量
		ptOperation.setSsqklb(operation.getSsqklb()== null?"-":operation.getSsqklb());//手术切口类别
		ptOperation.setSsqkdjbm(operation.getSsqkdjbm()== null?"-":operation.getSsqkdjbm());//手术切口等级编码
		ptOperation.setSsqkdjmc(operation.getSsqkdjmc()== null?"-":operation.getSsqkdjmc());//手术切口等级名称
		ptOperation.setTcyhdj(operation.getTcyhdj()== null?"-":operation.getTcyhdj());//切口愈合等级
		ptOperation.setSsczrqsj(DateUtils.getDateStr(operation.getSsqssj()));//手术/操作日期时间
		ptOperation.setSsqssj(DateUtils.getDateStr(operation.getSsqssj()));//手术起始时间
		ptOperation.setSsjssj(DateUtils.getDateStr(operation.getSsjssj()));//手术结束时间
		ptOperation.setSsczmc("");//手术操作名称
		ptOperation.setSshzdmc(operation.getSshzdmc()== null?"-":operation.getSshzdmc());//手术后诊断名称
		ptOperation.setSshzd(operation.getSshzd()== null?"-":operation.getSshzd());//手术后诊断
		ptOperation.setSsqzdmc(operation.getSsqzdmc()== null?"-":operation.getSsqzdmc());//手术前诊断名称
		ptOperation.setSsqzd(operation.getSsqzd()== null?"-":operation.getSsqzd());//手术前诊断
		ptOperation.setMzffdm("0");//麻醉方法代码 todo
		ptOperation.setMzysxm(operation.getMzysxm()== null?"-":operation.getMzysxm());//麻醉医生姓名
		ptOperation.setSsysxm(operation.getSsysxm());//手术医生姓名
		ptOperation.setMzysgh(operation.getMzysgh() == null?"-":operation.getMzysgh());//麻醉医生工号
		ptOperation.setSsysgh(operation.getSsysgh() == null?"-":operation.getSsysgh());//手术医生工号
		ptOperation.setSszsiigh("-");//手术助手II工号
		ptOperation.setSsezmc("-");//手术二助名称
		ptOperation.setSszsigh("-");//手术助手I工号
		ptOperation.setSsyzmc("-");//手术一助名称
		ptOperation.setZdysgh("");//手术一助名称
		ptOperation.setSsyszsigh(operation.getSsyszsigh() == null?"-":operation.getSsyszsigh());//手术医生助手I工号
		ptOperation.setSsyszsixm(operation.getSsyszsixm()== null?"-":operation.getSsyszsixm());//手术医生助手I姓名
		ptOperation.setSsyszsiigh(operation.getSsyszsiigh()== null?"-":operation.getSsyszsiigh());//手术医生助手II工号
		ptOperation.setSsyszsiixm(operation.getSsyszsiixm()== null?"-":operation.getSsyszsiixm());//手术医生助手II工号
		ptOperation.setHzxm(pat.getNamePi());//患者姓名
		ptOperation.setXbdm(pat.getSexSpcode());//性别代码
		ptOperation.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));//出生日期
		ptOperation.setHyzk(pat.getMarrySpcode());//婚姻状况
		ptOperation.setZy(pat.getOccuSpcode());//职业
		ptOperation.setZdysksdm(operation.getZdysksdm()== null?"-":operation.getZdysksdm());//主刀医生科室代码
		ptOperation.setSsqkyhdj("9");//手术切口愈合等级 1甲，2乙，3丙，9其他

		return ptOperation;
	}
	
	//1.病人基本信息
		public static BsPatient createBsPatient(IUser pUser,PatListVo pat,List<CardInfo> card,List<AddressInfo> addr) throws Exception {
			BsPatient patient=new BsPatient();
			patient.setName("病人基本信息");
			
			patient.setCardInfo(card);
			patient.setAddressInfo(addr);
			//卡类型
			//patient.setCardtype(pat.getDtIdtype());
			//卡号
			//patient.setCardno(pat.getIdNo());
			//身份证件类别代码
			patient.setIdtype(pat.getDtIdtype());
			//身份证件号码
			patient.setIdcard(pat.getIdNo());
			//本人姓名
			patient.setPersonname(pat.getNamePi());
			//出生日期
			patient.setBirthday(DateUtils.getDateStr(pat.getBirthDate()));
			//性别XB
			String sex=pat.getSexSpcode();
			patient.setSexcode(sex);
			//ABO血型
			patient.setBloodtypecode(pat.getBloodAboName()==null?"":pat.getBloodAboName());
			//RH血型
			patient.setRhbloodcode(pat.getBloodRhName()==null?"":pat.getBloodRhName());
			//姓名拼音
			patient.setPersonnamepycode("-");
			//民族
			patient.setNationCode(pat.getNationSpcode()==null?"":pat.getNationSpcode());//"01"
			//国籍
			patient.setNationalitycode(pat.getCountrySpcode()==null?"":pat.getCountrySpcode());//"cn"
			//婚姻
			patient.setMaritalStatuscode(pat.getMarrySpcode()==null?"":pat.getMarrySpcode());
			//工作类别代码
			patient.setWorkcode(pat.getOccuSpcode()==null?"":pat.getOccuSpcode());
			//文化程度代码
			patient.setEducationcode(pat.getDtEdu()==null?"":pat.getDtEdu());
			//工作单位名称
			patient.setWorkplace(pat.getUnitWork()==null?"":pat.getUnitWork());
			//地址类型
			//patient.setAddresstype(pat.getAddress()==null?"":pat.getAddress());
			//详细地址
			//patient.setAddress(pat.getAddress()==null?"":pat.getAddress());
			//联系电话类别
			patient.setContacttypecode("01");
			//联系电话
			patient.setContactno(pat.getMobile()==null?"":pat.getMobile());
			//参加工作日期
			//patient.setStartworkdate("-");
			//紧急联系人证件类别
			patient.setEmergencyidtype("");
			//紧急联系人证件号码
			patient.setEmergencyidcard("-");
			//紧急联系人姓名
			patient.setEmergencyname("-");
			//紧急联系人与患者关系
			patient.setEmergencyrelations(pat.getDtRalation()==null?"":pat.getDtRalation());
			//紧急联系人电话
			patient.setEmergencycontactno("-");
			//个人编号来源
			patient.setSourceid("-");
			//个人内部编号
			patient.setLocalid("-");
			//来源机构
			patient.setManageunit("中山市博爱医院");
			
			return patient;
		}
		
		//1.门诊病历
		public static OptBL createOptBL(IUser pUser,PatListVo pat,CnOpEmrRecord record,List<SSLB> sslb) throws Exception {
			OptBL optbl=new OptBL();
			optbl.setName("门诊病历");
			//就诊流水号
			optbl.setJzlsh(pat.getCodePv());
			//手术列表
			optbl.setSslb(sslb);
			//病历ID
			optbl.setBlid(record.getPkEmrop());
			//就诊日期时间
			optbl.setJzrqsj(DateUtils.getDateStr(pat.getDateBegin()));
			//过敏史标志
			optbl.setGmsbz(record.getAllergy()!=null?"1":"0");
			//过敏史
			if(record.getAllergy()==null || record.getAllergy().equals("")) {optbl.setGms("-");}
			else{optbl.setGms(record.getAllergy());}
			//初诊标志代码
			optbl.setCzbzdm("1");
			//主诉
			optbl.setZs(record.getProblem());
			//现病史
			optbl.setXbs(record.getPresent());
			//既往史
			if(record.getHistory()==null || record.getHistory().equals("")) {optbl.setJws("-");}
			else{optbl.setJws(record.getHistory());}
			//个人史
			if(record.getPsnhist()==null || record.getPsnhist().equals("")) {optbl.setGrs("-");}
			else{optbl.setGrs(record.getPsnhist());}
			//家族史
			if(record.getFmyhist()==null || record.getFmyhist().equals("")) {optbl.setJzs("-");}
			else{optbl.setJzs(record.getFmyhist());}
			//婚育史
			optbl.setHys("-");
			//体温(℃)
			if(record.getTemperature()!=null) {optbl.setTw(record.getTemperature().toString());}
			else {optbl.setTw("-");}
			//心率(次/min)
			if(record.getPulse()==null || record.getPulse().equals("")) {optbl.setXl("-");}
			else{optbl.setXl(record.getPulse());}
			//呼吸频率(次/min)
			if(record.getBreathe()==null || record.getBreathe().equals("")) {optbl.setHxpl("-");}
			else{optbl.setHxpl(record.getBreathe());}
			//收缩压(mmHg)
			if(record.getSbp()!=null) {optbl.setSsy(record.getSbp().toString());}
			else {optbl.setSsy("-");}
			//舒张压(mmHg)
			if(record.getDbp()!=null) {optbl.setSzy(record.getDbp().toString());}
			else {optbl.setSzy("-");}
			//身高(cm)
			if(record.getHeight()!=null) {optbl.setSg(record.getHeight().toString());}
			else {optbl.setSg("-");}
			//体重(kg)
			if(record.getWeight()!=null) {optbl.setTz(record.getWeight().toString());}
			else {optbl.setTz("-");}
			//体格检查
			optbl.setTgjc(record.getExamPhy());
			//辅助检查
			optbl.setFzjc(record.getExamAux());
			//专科情况
			optbl.setZkqk(record.getExamSpec());
			//健康问题评估
			optbl.setJkwtpg("-");
			//主要诊断编码
			optbl.setZyzdbm("-");
			//主要诊断名称
			optbl.setZyzdmc("-");
			//次要诊断编码
			optbl.setCyzdbm("-");
			//次要诊断名称
			optbl.setCyzdmc("-");
			//中医病名代码
			optbl.setCbzdzybmdm("-");
			//中医病名名称
			optbl.setCbzdzybmmc("-");
			//中医证候代码
			optbl.setCbzdzyzhdm("-");
			//中医证候名称
			optbl.setCbzdzyzhmc("-");
			//科室编码
			optbl.setKsbm(pat.getDeptCodeOrig());
			//科室名称
			optbl.setKsmc(pat.getDeptName());
			//医生工号
			optbl.setYsgh("-");
			//医生姓名
			optbl.setYsxm(record.getNameEmpEmr());
			//填写时间
			optbl.setTxsj(DateUtils.getDateStr(record.getCreateTime()));
			//用户编码
			optbl.setYhbm("-");
			//患者姓名
			optbl.setHzxm(pat.getName());
			//性别代码
			optbl.setXbdm(pat.getSexSpcode());
			//出生日期
			optbl.setCsrq(DateUtils.getDateStr(pat.getBirthDate()));
			//年龄
			if(pat.getAgePv().contains("岁")) {//1岁10月
				optbl.setNls(pat.getAgePv().split("岁")[0]);
			}else if(pat.getAgePv().contains("月")) {//10月19天
				optbl.setNls(pat.getAgePv().split("月")[0]);
			} 
			//中医“四诊”观察结果
			optbl.setZyszgcjg("-");
			//辨证依据
			optbl.setBzyj("-");
			//治则治法
			optbl.setZzzf("-");
			//专科情况
			optbl.setZkqk("-");
			//
			return optbl;
		}
		/**
		 * 药房出库(PMC_DrugStore)
		 * @param pUser
		 * @param pdst
		 * @return
		 * @throws Exception
		 */
		public static PMCDrugStore createPMCDrugStore(IUser pUser,PdstDetail store) throws Exception {
			PMCDrugStore drugStore=new PMCDrugStore();
			//流水号
			drugStore.setLsh(store.getCodeSt());
			//出库日期
			drugStore.setCkrq(DateUtils.getDateStr(store.getCreateTime()));
			//科室编码
			drugStore.setKsbm(null);
			//科室名称
			drugStore.setKsmc(null);
			//项目类型代码
			drugStore.setXmlx_dm(null);
			//项目类型名称
			drugStore.setXmlx_mc(null);
			//项目编码
			drugStore.setXm_bm(null);
			//项目名称
			drugStore.setXm_mc(null);
			//最小单位代码
			drugStore.setZxdw_dm("-");
			//最小单位名称
			drugStore.setZxdw_mc("-");
			//出库数量
			drugStore.setCksl(store.getQuanOutstore()==null?"-":store.getQuanOutstore().toString());
			//进货价格
			drugStore.setJkjg(store.getAmountCost()==null?"-":store.getAmountCost().toString());
			//零售价
			drugStore.setLsj(store.getPriceCost()==null?"-":store.getPriceCost().toString());
			//出库金额
			drugStore.setCkje(store.getAmountPay()==null?"-":store.getAmountPay().toString());
			//最新进价
			drugStore.setZxjj("-");
			//出库单号
			drugStore.setCkdh(store.getCodeSt());
			//登记时间(系统)
			drugStore.setDjsj(DateUtils.getDateStr(store.getCreateTime()));
			//登记人员工号
			drugStore.setDjrygh(store.getPkEmpOp());
			//登记人员名称
			drugStore.setDjrymc(store.getNameEmpOp());
			//最后修改时间
			drugStore.setZhxgsj(DateUtils.getDateStr(store.getCreateTime()));
			//最后修改人员工号
			drugStore.setZhxgrygh(store.getPkEmpChk());
			//最后修改人员名称
			drugStore.setZhxgrymc(store.getNameEmpChk());
			return drugStore;
		}
		
		/**
		 * 药房入库(PMC_DrugEntry)
		 * @param pUser
		 * @param pdst
		 * @return
		 * @throws Exception
		 */
		public static PMCDrugEntry createPMCDrugEntry(IUser pUser,PdstDetail pdst) throws Exception {
			PMCDrugEntry drugEntry=new PMCDrugEntry();
			//流水号
			drugEntry.setLsh(pdst.getCodeSt());
			//科室编码
			drugEntry.setKsbm("-");
			//科室名称
			drugEntry.setKsmc("-");
			//项目类型代码
			drugEntry.setXmlx_dm("-");
			//项目类型名称
			drugEntry.setXmlx_mc("-");
			//项目编码
			drugEntry.setXm_bm("-");
			//项目名称
			drugEntry.setXm_mc("-");
			//入库日期
			drugEntry.setRkrq(DateUtils.getDateStr(pdst.getDateSt()));
			//入库来源
			drugEntry.setRkly("-");
			//入库数量
			drugEntry.setRksl(pdst.getQuanPack()==null?"":pdst.getQuanPack().toString());
			//最小单位代码
			drugEntry.setZxdw_dm("-");
			//最小单位名称
			drugEntry.setZxdw_mc("-");
			//进货价格
			drugEntry.setJhjg(pdst.getAmountPay()==null?"":pdst.getAmountPay().toString());
			//批发价格
			drugEntry.setPfjg(pdst.getPriceCost()==null?"":pdst.getPriceCost().toString());
			//批发金额
			drugEntry.setPfje(pdst.getAmountCost()==null?"":pdst.getAmountCost().toString());
			//零售价
			drugEntry.setLsj(pdst.getPrice()==null?"":pdst.getPrice().toString());
			//销售金额
			drugEntry.setXsje(pdst.getAmount()==null?"":pdst.getAmount().toString());
			//库房代码
			drugEntry.setKfdm(null);
			//库房名称
			drugEntry.setKfmc(null);
			//入库单号
			drugEntry.setRkdh(pdst.getCodeSt());
			//生产日期
			drugEntry.setScrq(DateUtils.getDateStr(pdst.getDateFac()));
			//失效日期
			drugEntry.setSxrq(DateUtils.getDateStr(pdst.getDateExpire()));
			//生产批号
			drugEntry.setScph(pdst.getBatchNo());
			//药品类型代码
			drugEntry.setYplx_dm(null);
			//药品类型名称
			drugEntry.setYplx_mc(null);
			//最大单位代码
			drugEntry.setZddw_dm(null);
			//最大单位名称
			drugEntry.setZddw_mc(null);
			//最大单位
			drugEntry.setZddw(null);
			//拆分系数
			drugEntry.setCfxs(null);
			//是否基药代码
			drugEntry.setSfjy_dm(null);
			//是否基药名称
			drugEntry.setSfjy_mc(null);
			//生产厂商
			drugEntry.setSccs(null);
			//登记时间(系统)
			drugEntry.setDjsj(DateUtils.getDateStr(pdst.getCreateTime()));
			//登记人员工号
			drugEntry.setDjrygh(pdst.getPkEmpOp());
			//登记人员名称
			drugEntry.setDjrymc(pdst.getNameEmpOp());
			//最后修改时间
			drugEntry.setZhxgsj(DateUtils.getDateStr(pdst.getCreateTime()));
			//最后修改人员工号
			drugEntry.setZhxgrygh(pdst.getPkEmpChk());
			//最后修改人员名称
			drugEntry.setZhxgrymc(pdst.getNameEmpChk());
			return drugEntry;
		}
		/**
		 * 药房库存(PMC_DrugStock)
		 * @param pUser
		 * @param pdStock
		 * @return
		 * @throws Exception
		 */
		public static PMCDrugStock createPMCDrugStock(IUser pUser,PdStock pdStock) throws Exception {
			PMCDrugStock drugStock=new PMCDrugStock();
			//流水号
			drugStock.setLsh(null);
			//项目编码
			drugStock.setXm_bm(null);
			//项目名称
			drugStock.setXm_mc(null);
			//入库日期
			drugStock.setRkrq(null);
			//出库日期
			drugStock.setCkrq(null);
			//规格
			drugStock.setGg(null);
			//最小单位代码
			drugStock.setZxdw_dm(null);
			//最小单位名称
			drugStock.setZxdw_mc(null);
			//药房拆分系数
			drugStock.setYfcfxs(null);
			//药库单位
			drugStock.setYkdw(null);
			//生产批号
			drugStock.setScph(null);
			//药品类型代码
			drugStock.setYplx_dm(null);
			//药品类型名称
			drugStock.setYplx_mc(null);
			//库存数量
			drugStock.setKcsl(null);
			//进货价格
			drugStock.setJhjg(null);
			//零售价
			drugStock.setLsj(null);
			//库存金额
			drugStock.setKcje(null);
			//基本药品标志
			drugStock.setJbypbz(null);
			//药品失效的日期
			drugStock.setYpsxrq(null);
			//批发药品价格
			drugStock.setPfypjg(null);
			//药品包装倍数
			drugStock.setYpbzbs(null);
			//药品包装单位
			drugStock.setYpbzdw(null);
			//药品产地
			drugStock.setYpcd(null);
			//药品出库数量
			drugStock.setYpcksl(null);
			//药品库存编号
			drugStock.setYpkcbh(null);
			//药品库存单位
			drugStock.setYpkcdw(null);
			//药品入库数量
			drugStock.setYprksl(null);
			//药品所在库房编号
			drugStock.setYpszkf_bh(null);
			//药品所在库房名称
			drugStock.setYpszkf_mc(null);
			//药品毒理分类编码
			drugStock.setYpdlfl_bm(null);
			//药品毒理分类名称
			drugStock.setYpdlfl_mc(null);
			//药品类别代码
			drugStock.setYplb_dm(null);
			//药品类别名称
			drugStock.setYplb_mc(null);
			//药品规格的描述
			drugStock.setYpggdms(null);
			//药品的剂量
			drugStock.setYpdjl(null);
			//药品的名称
			drugStock.setYpdmc(null);
			//药品的批号
			drugStock.setYpdph(null);
			//登记时间(系统)
			drugStock.setDjsj(null);
			//登记人员工号
			drugStock.setDjrygh(null);
			//登记人员名称
			drugStock.setDjrymc(null);
			//最后修改时间
			drugStock.setZhxgsj(null);
			//最后修改人员工号
			drugStock.setZhxgrygh(null);
			//最后修改人员名称
			drugStock.setZhxgrymc(null);
			return drugStock;
		}
		
		/**
		 *  药库入库(PMC_PharmacyEntry)
		 * @param pUser
		 * @param pdst
		 * @return
		 * @throws Exception
		 */
		public static PMCPharmacyEntry createPMCPharmacyEntry(IUser pUser,PdstDetail pdst) throws Exception {
			PMCPharmacyEntry pharmacyEntry=new PMCPharmacyEntry();
			//流水号
			pharmacyEntry.setLsh(pdst.getCodeSt());
			//科室编码
			pharmacyEntry.setKsbm(null);
			//科室名称
			pharmacyEntry.setKsmc(null);
			//项目类型代码
			pharmacyEntry.setXmlx_dm(null);
			//项目类型名称
			pharmacyEntry.setXmlx_mc(null);
			//项目编码
			pharmacyEntry.setXm_bm(null);
			//项目名称
			pharmacyEntry.setXm_mc(null);
			//入库日期
			pharmacyEntry.setRkrq(DateUtils.getDateStr(pdst.getDateSt()));
			//入库来源
			pharmacyEntry.setRkly("-");
			//入库数量
			pharmacyEntry.setRksl(pdst.getQuanPack()==null?"":pdst.getQuanPack().toString());
			//最小单位代码
			pharmacyEntry.setZxdw_dm("-");
			//最小单位名称
			pharmacyEntry.setZxdw_mc("-");
			//进货价格
			pharmacyEntry.setJhjg(pdst.getAmountPay()==null?"":pdst.getAmountPay().toString());
			//批发价格
			pharmacyEntry.setPfjg(pdst.getPriceCost()==null?"":pdst.getPriceCost().toString());
			//批发金额
			pharmacyEntry.setPfje(pdst.getAmountCost()==null?"":pdst.getAmountCost().toString());
			//零售价
			pharmacyEntry.setLsj(pdst.getPrice()==null?"":pdst.getPrice().toString());
			//销售金额
			pharmacyEntry.setXsje(pdst.getAmount()==null?"":pdst.getAmount().toString());
			//库房代码
			pharmacyEntry.setKfdm(null);
			//库房名称
			pharmacyEntry.setKfmc(null);
			//入库单号
			pharmacyEntry.setRkdh(pdst.getCodeSt());
			//生产日期
			pharmacyEntry.setScrq(DateUtils.getDateStr(pdst.getDateFac()));
			//失效日期
			pharmacyEntry.setSxrq(DateUtils.getDateStr(pdst.getDateExpire()));
			//生产批号
			pharmacyEntry.setScph(pdst.getBatchNo());
			//药品类型代码
			pharmacyEntry.setYplx_dm(null);
			//药品类型名称
			pharmacyEntry.setYplx_mc(null);
			//登记时间(系统)
			pharmacyEntry.setDjsj(DateUtils.getDateStr(pdst.getCreateTime()));
			//登记人员工号
			pharmacyEntry.setDjrygh(pdst.getPkEmpOp());
			//登记人员名称
			pharmacyEntry.setDjrymc(pdst.getNameEmpOp());
			//最后修改时间
			pharmacyEntry.setZhxgsj(DateUtils.getDateStr(pdst.getCreateTime()));
			//最后修改人员工号
			pharmacyEntry.setZhxgrygh(pdst.getPkEmpChk());
			//最后修改人员名称
			pharmacyEntry.setZhxgrymc(pdst.getNameEmpChk());
			return pharmacyEntry;
		}
		
		/**
		 * 药库库存(PMC_PharmacyStock)
		 * @param pUser
		 * @param pdst
		 * @return
		 * @throws Exception
		 */
		public static PMCPharmacyStock createPMCPharmacyStock(IUser pUser,PdstDetail pdst) throws Exception {
			PMCPharmacyStock pharmacyStock=new PMCPharmacyStock();
			//流水号
			pharmacyStock.setLsh(pdst.getCodeSt());
			//类型代码
			pharmacyStock.setLx_dm(null);
			//类型名称
			pharmacyStock.setLx_mc(null);
			//项目编码
			pharmacyStock.setXm_bm(null);
			//项目名称
			pharmacyStock.setXm_mc(null);
			//规格
			pharmacyStock.setGg(null);
			//入库日期
			pharmacyStock.setRkrq(DateUtils.getDateStr(pdst.getDateSt()));
			//出库日期
			pharmacyStock.setCkrq(null);
			//最小单位代码
			pharmacyStock.setZxdw_dm(null);
			//最小单位名称
			pharmacyStock.setZxdw_mc(null);
			//药房拆分系数
			pharmacyStock.setYfcfxs(null);
			//药库单位
			pharmacyStock.setYkdw(null);
			//药品类型代码
			pharmacyStock.setYplx_dm(null);
			//药品类型名称
			pharmacyStock.setYplx_mc(null);
			//库存数量
			pharmacyStock.setKcsl(pdst.getQuanPack()==null?"":pdst.getQuanPack().toString());
			//进货价格
			pharmacyStock.setJhjg(pdst.getAmountPay()==null?"":pdst.getAmountPay().toString());
			//零售价
			pharmacyStock.setLsj(pdst.getPrice()==null?"":pdst.getPrice().toString());
			//库存金额
			pharmacyStock.setKcje(null);
			//生产厂商
			pharmacyStock.setSccs(null);
			//登记时间(系统)
			pharmacyStock.setDjsj(DateUtils.getDateStr(pdst.getCreateTime()));
			//登记人员工号
			pharmacyStock.setDjrygh(pdst.getPkEmpOp());
			//登记人员名称
			pharmacyStock.setDjrymc(pdst.getNameEmpOp());
			//最后修改时间
			pharmacyStock.setZhxgsj(DateUtils.getDateStr(pdst.getCreateTime()));
			//最后修改人员工号
			pharmacyStock.setZhxgrygh(pdst.getPkEmpChk());
			//最后修改人员名称
			pharmacyStock.setZhxgrymc(pdst.getNameEmpChk());
			return pharmacyStock;
		}
}














