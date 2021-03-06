package com.zebone.nhis.webservice.syx.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageCharges;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOps;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.emr.common.EmrUtils;
import com.zebone.nhis.webservice.service.BdPubForWsService;
import com.zebone.nhis.webservice.syx.dao.EmrSyxRecMapper;
import com.zebone.nhis.webservice.syx.vo.EmrAdmitRecResult;
import com.zebone.nhis.webservice.syx.vo.EmrAdmitRecRtn;
import com.zebone.nhis.webservice.syx.vo.EmrEncInfoVo;
import com.zebone.nhis.webservice.syx.vo.EmrPatInfoResult;
import com.zebone.nhis.webservice.syx.vo.EmrPatInfoRtn;
import com.zebone.nhis.webservice.syx.vo.EmrPvDiagList;
import com.zebone.nhis.webservice.syx.vo.EmrReqDocDataBts;
import com.zebone.nhis.webservice.syx.vo.EmrReqDocDataEff;
import com.zebone.nhis.webservice.syx.vo.EmrReqDocDataEst;
import com.zebone.nhis.webservice.syx.vo.EmrRequestData;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfAdmitRec;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfCourseRec;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfHomePage;
import com.zebone.nhis.webservice.syx.vo.emr.EmrHmCourseRslt;
import com.zebone.nhis.webservice.syx.vo.emr.EmrHmFirstCourseRslt;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.LoadDataSource;

/**
 * ????????????-??????
 * 
 * @author chengjia
 *
 */
@Service
public class EmrSyxRecService {

	@Resource
	private EmrSyxRecMapper recMapper;
	@Resource
	private BdPubForWsService bdPubForWsService;
	
	private SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdfHm= new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat sdfMdHm= new SimpleDateFormat("MM-dd HH:mm");
	
	/**
	 * ????????????????????????????????????
	 * @param 
	 * @param 
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public EmrAdmitRecRtn queryPatEncList(Map<String,Object> map) throws IllegalAccessException, InvocationTargetException {

		List<EmrEncInfoVo> list = recMapper.queryPatEncList(map);

		List<EmrPvDiagList> diagList = recMapper.queryPvDiagList(map);
		int i,j;
		List<EmrAdmitRecResult> rstList = new ArrayList<EmrAdmitRecResult>();
		EmrAdmitRecRtn rtn = new EmrAdmitRecRtn();
		EmrAdmitRecResult result = new EmrAdmitRecResult();
		List<EmrPvDiagList> diagListNew=new ArrayList<EmrPvDiagList>();
		if(list!=null&&list.size()>0){
			String url=ApplicationUtils.getPropertyValue("emr.doc.url", "");
			if(url==null) url="";
			for (i = 0; i < list.size(); i++) {
				EmrEncInfoVo vo=list.get(i);

				vo.setDocUrl(url+vo.getFilePath()+vo.getFileName()+"."+vo.getFileType());
				if(vo.getRelator()==null||vo.getRelator().equals("")) vo.setRelator("??????");
				result = new EmrAdmitRecResult();
				BeanUtils.copyProperties(result, vo);
				rstList.add(result);
				diagListNew=new ArrayList<EmrPvDiagList>();
				if(diagList!=null&&diagList.size()>0){
					for (j = 0; j < diagList.size(); j++) {
						EmrPvDiagList diag=diagList.get(j);
						if(diag==null||diag.getPkPv()==null||diag.getDtDiagType()==null) continue;
						
						if(result.getPkPv()!=null&&result.getPkPv().equals(diag.getPkPv())){
							String diagType=diag.getDtDiagType();
							if(diagType.equals("0100")){
								//????????????
								diag.setMode("1");
								diag.setType("1");
							}else if(diagType.equals("0109")){
								//????????????
								diag.setMode("2");
								diag.setType("2");
							}
							diagListNew.add(diag);
						}
					}
				}
				result.setDiagList(diagListNew);
			}
		}
		
		rtn.setRstList(rstList);
		//String rtnStr = XmlUtil.beanToXml(rtn, rtn.getClass());
		
		return rtn;
	}
	public EmrPatInfoRtn queryPatInfoList(Map<String,Object> map){
		List<EmrPatInfoResult> list = recMapper.queryPatInfoList(map);
		EmrPatInfoRtn rtn=new EmrPatInfoRtn();
		rtn.setRstList(list);
		return rtn;
	}
	
	/**
	 * ??????????????????
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String genEmrBloolTransRec(Map<String,Object> map) throws UnsupportedEncodingException{
		List<EmrEncInfoVo> list = recMapper.queryPatEmrEncList(map);
		
		Date now = new Date();
		if(list!=null&&list.size()>0){
			EmrEncInfoVo pat=list.get(0);
			EmrRequestData reqData = (EmrRequestData)map.get("reqData");
			if(reqData==null || reqData.getDocData()==null) return "?????????DOCDATA??????";
			EmrReqDocDataBts bts = reqData.getDocData().getBts();
			if(bts==null) return "?????????DOCDATA???bts??????";
			BdOuEmployee emp=null;
			if(!StringUtils.isEmpty(reqData.getEmpCode())){
				String sql="";
				//List<BdOuEmployee> listEmp = DataBaseHelper.queryForList("select * from BD_OU_EMPLOYEE where CODE_EMP = ?", BdOuEmployee.class, reqData.getEmpCode());
				List<BdOuEmployee> listEmp = DataBaseHelper.queryForList("select * from BD_OU_EMPLOYEE where CODE_EMP = ?", BdOuEmployee.class,reqData.getEmpCode());
				if(listEmp!=null&&listEmp.size()>0){
					emp = listEmp.get(0);
				}
			}
			
			String xml=parseXML("emrBldTrnRec");
			//System.out.println("xml1:"+xml);
			if(StringUtils.isEmpty(xml)) return "xml????????????";
			if(emp==null) return "?????????????????????";
			//??????
			xml=xml.replaceAll("test0323", pat.getName());
			//??????
			xml=xml.replaceAll("???", pat.getNameSex());
			//??????
			xml=xml.replaceAll("???1", pat.getBedNo());
			//?????????
			xml=xml.replaceAll("806396", pat.getCodeIp());
			//????????????
			xml=xml.replaceAll("????????????????????????", pat.getNameDept());
			//????????????
			String recTime=bts.getRecTime();
			if(recTime!=null){
				if(recTime.length()>16){
					xml=xml.replaceAll("2019-05-10 12:00", recTime.substring(0, 15));
				}else{
					xml=xml.replaceAll("2019-05-10 12:00", recTime);
				}
				
			}
			//????????????
			String operTime=bts.getOperTime();
			if(operTime!=null){
				if(operTime.length()>16){
					xml=xml.replaceAll("2019-05-10 00:00", operTime.substring(0, 15));
				}else{
					xml=xml.replaceAll("2019-05-10 00:00", operTime);
				}
				
			}
			//??????
			xml=xml.replaceAll("AB???", getAboName(bts.getAboName()));
			//????????????
			xml=xml.replaceAll("???????????????", bts.getTypeName());
			//?????????
			xml=xml.replaceAll("888", bts.getQty());
			//????????????
			xml=xml.replaceAll("mll", bts.getUnit());
			//?????????
			xml=xml.replaceAll("###", "");
			//????????????
			xml=xml.replaceAll("#???????????????#", bts.getReact());
			//??????
			String empName=reqData.getEmpName();
			if(StringUtils.isEmpty(empName)) empName=emp.getNameEmp();
			xml=xml.replaceAll("??????",empName);
			//????????????
			String pkRec=NHISUUID.getKeyId();
			String pkDoc=NHISUUID.getKeyId();
            xml=xml.replaceAll("d6cde750bd8444aa94e4a73ab8f08a9b",pkRec);
            xml=xml.replaceAll("a8dac79a2d85479086ab10941d689210",pkDoc);
            String pkTmp=ApplicationUtils.getPropertyValue("emr.bts.rec.tmp", "7f693564015443e696d897f6a36e3d76");
            xml=xml.replaceAll("7f693564015443e696d897f6a36e3d76",pkTmp);
            xml=xml.replaceAll("2019-05-12 10:41:56",sdf.format(now));//create_date
            xml=xml.replaceAll("2019-05-12 10:41",recTime.substring(0, 15));//rec_date
            xml=xml.replaceAll("b460dce3c0b2481080b116781ea4bf5b",emp.getPkEmp());//creator
            xml = xmlRepTxt(xml);
            
			//createXml(xml);
			//String str = new String(xml.getBytes("utf-8"),"utf-8");
			genMedRec("????????????",now, pat, reqData, bts, emp, xml, pkRec,pkDoc,pkTmp);
			
		}else{
			return "?????????????????????";
		}
		//rtn.setRstList(list);
		return null;
	}
	//?????????????????????
	private String xmlRepTxt(String xml) {
		xml=xml.replaceAll("<PrintVisibility>Hidden</PrintVisibility>","<PrintVisibility>Visible</PrintVisibility>");
		xml=xml.replaceAll("<StartBorderText>\\{</StartBorderText>","<StartBorderText></StartBorderText>");
		xml=xml.replaceAll("<EndBorderText>\\}</EndBorderText>","<EndBorderText></EndBorderText>");
		return xml;
	}
	//??????medrec/doc
	private void genMedRec(String titleName,Date now, EmrEncInfoVo pat, EmrRequestData reqData,
			Object itemObj, BdOuEmployee emp, String xml, String pkRec,String pkDoc,String pkTmp) throws UnsupportedEncodingException {
		//EmrReqDocDataBts bts,
		String recTimeStr="";
		EmrReqDocDataBts bts=null;
		EmrReqDocDataEst est=null;
		EmrReqDocDataEff eff=null;
		if(itemObj instanceof  EmrReqDocDataBts){
			bts = (EmrReqDocDataBts)itemObj;
			recTimeStr =bts.getRecTime();
		}else if(itemObj instanceof  EmrReqDocDataEst){
			est = (EmrReqDocDataEst)itemObj;
			recTimeStr =est.getRecTime();
		}else if(itemObj instanceof  EmrReqDocDataEff){
			eff = (EmrReqDocDataEff)itemObj;
			recTimeStr =eff.getRecTime();
		}
		EmrMedRec rec =new EmrMedRec();
		//System.out.println("pkRec:"+pkRec);
		rec.setPkRec(pkRec);
		rec.setPkPatrec(pat.getPkPatrec());
		
		Date recDate = new Date();
		if(recTimeStr!=null&&recTimeStr.length()>=16){
			if(recTimeStr.length()==16){
				try {
					recDate=sdfHm.parse(recTimeStr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					recDate=sdf.parse(recTimeStr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//rec.setName("05-09 22:04-????????????-??????");
		rec.setName(sdfMdHm.format(recDate)+"-" + titleName + "-"+reqData.getEmpName());
		rec.setSeqNo(Long.parseLong(Integer.toString(bdPubForWsService.getSerialNo("emr_med_rec", "rec_id", 1, null))));
		rec.setRecDate(recDate);
		rec.setDescribe("??????????????????");
		rec.setPkPi(pat.getPkPi());
		rec.setTimes(pat.getIpTimes());
		rec.setPkPv(pat.getPkPv());
		rec.setTypeCode("100002");
		rec.setPkTmp(pkTmp);
		rec.setPkDoc(pkDoc);
		rec.setFlagAudit("1");
		rec.setEuAuditLevel(new Short("0"));
		rec.setEuDocStatus("0");
		rec.setEuAuditStatus("0");
		rec.setPkEmpRefer(emp.getPkEmp());
		rec.setCreator(emp.getPkEmp());
		rec.setCreateTime(now);
		rec.setTs(now);
		rec.setFlagAuditFinish("0");
		rec.setPkEmpReferAct(emp.getPkEmp());
		rec.setPkDept(pat.getPkDept());
		rec.setPkWard(pat.getPkDeptNs());
		rec.setDelFlag("0");
		recMapper.saveEmrMedRec(rec);

//			byte[] b = xml.getBytes();
//			System.out.println("?????????"+xml+"gbk???????????????"+Arrays.toString(b));
//			String s = new String(b,"iso8859-1");
//			System.out.println("???????????????iso8859-1???????????????"+s);
//			byte[] b1 = s.getBytes("iso8859-1");
//			System.out.println(s+"???????????????iso8859-1???????????????"+Arrays.toString(b1));
//			String s1 = new String(b1,"gbk");
//			System.out.println(s1);

		byte[] data = xml.getBytes("utf-8");//"utf-8"
		EmrMedDoc doc =new EmrMedDoc();
		doc.setPkDoc(pkDoc);
		doc.setDocData(data);
		doc.setDocXml(null);
		doc.setDelFlag("0");
		doc.setFileName(null);
		doc.setFilePath(null);
		doc.setFileType(null);
		doc.setCreator(emp.getPkEmp());
		doc.setCreateTime(now);
		doc.setTs(now);
		recMapper.saveEmrMedDoc(doc);
	}
	

	public void createXml(String strXML){
        SAXReader reader = new SAXReader();
        StringReader sr = new StringReader(strXML);
        InputSource is = new InputSource(sr);
        reader.setEncoding("utf-8");
        try {
            Document document = reader.read(is);

            File file = new File("d:\\bbb.xml");  
            if (file.exists()) {  
                file.delete();  
            }  
            try {
                file.createNewFile();
                OutputFormat format = OutputFormat.createPrettyPrint();
                Writer xmlwriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
                format.setEncoding("utf-8");
 
                //XMLWriter out = new XMLWriter(new FileWriter(file),format);
                XMLWriter out = new XMLWriter(xmlwriter,format);
               
                out.write(document);  
                out.flush();  
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	 
	public static void OutputXml(Document doc, String filename) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		/** ??????XML?????? */
		format.setEncoding("UTF-8");
		/** ???document??????????????????????????? */
		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(new File(filename)), format);
			writer.write(doc);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * ?????????????????????
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String genEmrBloolTransEst(Map<String,Object> map) throws UnsupportedEncodingException{
		List<EmrEncInfoVo> list = recMapper.queryPatEmrEncList(map);
		
		Date now = new Date();
		if(list!=null&&list.size()>0){
			EmrEncInfoVo pat=list.get(0);
			EmrRequestData reqData = (EmrRequestData)map.get("reqData");
			if(reqData==null || reqData.getDocData()==null) return "?????????DOCDATA??????";
			EmrReqDocDataEst est = reqData.getDocData().getEst();
			if(est==null) return "?????????DOCDATA???est??????";
			BdOuEmployee emp=null;
			if(!StringUtils.isEmpty(reqData.getEmpCode())){
				String sql="";
				List<BdOuEmployee> listEmp = DataBaseHelper.queryForList("select * from BD_OU_EMPLOYEE where CODE_EMP = ?", BdOuEmployee.class,reqData.getEmpCode());
				if(listEmp!=null&&listEmp.size()>0){
					emp = listEmp.get(0);
				}
			}
			
			String xml=parseXML("emrBldTrnEst");
			if(StringUtils.isEmpty(xml)) return "xml????????????";
			if(emp==null) return "?????????????????????";
			//??????
			xml=xml.replaceAll("test0323", pat.getName());
			//??????
			xml=xml.replaceAll("???", pat.getNameSex());
			//??????
			xml=xml.replaceAll("???1", pat.getBedNo());
			//?????????
			xml=xml.replaceAll("806396", pat.getCodeIp());
			//????????????
			xml=xml.replaceAll("????????????????????????", pat.getNameDept());
			//????????????
			String recTime=est.getRecTime();
			if(recTime!=null){
				if(recTime.length()>16){
					xml=xml.replaceAll("2019-05-10 12:00", recTime.substring(0, 15));
				}else{
					xml=xml.replaceAll("2019-05-10 12:00", recTime);
				}
				
			}
			//??????
			xml=xml.replaceAll("tempe_v", est.getTempe());
			//??????
			xml=xml.replaceAll("pulse_v", est.getPulse());
			//??????
			xml=xml.replaceAll("breath_v", est.getBreath());
			//?????????
			xml=xml.replaceAll("systolic_v", est.getSystolic());
			//?????????
			xml=xml.replaceAll("diastolic_v", est.getDiastolic());
			//HB
			xml=xml.replaceAll("hb_v", est.getHb());
			//PLT
			xml=xml.replaceAll("plt_v", est.getPlt());
			//PT
			xml=xml.replaceAll("pt_v", est.getPt());
			//APTT
			xml=xml.replaceAll("aptt_v", est.getAptt());
			//FIG
			xml=xml.replaceAll("fig_v", est.getFig());
			//TRIGGER
			xml=xml.replaceAll("??????????????????????????????????????????", est.getTrigger());
			//??????
			xml=xml.replaceAll("AB???", getAboName(est.getAboName()));
			//????????????
			xml=xml.replaceAll("type_v", est.getTypeName());
			//?????????
			xml=xml.replaceAll("num_v", est.getQty());
			//????????????
			xml=xml.replaceAll("unit_v", est.getUnit());
			
			//??????
			String empName=reqData.getEmpName();
			if(StringUtils.isEmpty(empName)) empName=emp.getNameEmp();
			xml=xml.replaceAll("??????",empName);
			//????????????
			String pkRec=NHISUUID.getKeyId();
			String pkDoc=NHISUUID.getKeyId();
            xml=xml.replaceAll("12bc2ca3683e459b96dafd20b5ddda28",pkRec);
            xml=xml.replaceAll("fc11bf3a374c41728ca68f59181bfbf0",pkDoc);
            String pkTmp=ApplicationUtils.getPropertyValue("emr.est.rec.tmp", "61eeace2bb6d41159b44cc7d32f3fdeb");
            xml=xml.replaceAll("61eeace2bb6d41159b44cc7d32f3fdeb",pkTmp);
            xml=xml.replaceAll("2019-05-12 13:41:14",sdf.format(now));//create_date
            xml=xml.replaceAll("2019-05-12 13:41",recTime.substring(0, 15));//rec_date
            xml=xml.replaceAll("b460dce3c0b2481080b116781ea4bf5b",emp.getPkEmp());//creator
            xml = xmlRepTxt(xml);
            
			genMedRec("???????????????",now, pat, reqData, est, emp, xml, pkRec,pkDoc,pkTmp);
		}else{
			return "?????????????????????";
		}
		//rtn.setRstList(list);
		return null;
		
	}
	private String getAboName(String name) {
		String aboName=name;
		if(!StringUtils.isEmpty(aboName)){
			if(aboName.equals("A")||aboName.equals("B")||aboName.equals("O")||aboName.equals("AB")){
				aboName=aboName+"???";
			}
		}
		return aboName;
	}
	
	/**
	 * ???????????????????????????
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String genEmrBloolTransEff(Map<String,Object> map) throws UnsupportedEncodingException{
		List<EmrEncInfoVo> list = recMapper.queryPatEmrEncList(map);
		
		Date now = new Date();
		if(list!=null&&list.size()>0){
			EmrEncInfoVo pat=list.get(0);
			EmrRequestData reqData = (EmrRequestData)map.get("reqData");
			if(reqData==null || reqData.getDocData()==null) return "?????????DOCDATA??????";
			EmrReqDocDataEff eff = reqData.getDocData().getEff();
			if(eff==null) return "?????????DOCDATA???eff??????";
			BdOuEmployee emp=null;
			if(!StringUtils.isEmpty(reqData.getEmpCode())){
				String sql="";
				List<BdOuEmployee> listEmp = DataBaseHelper.queryForList("select * from BD_OU_EMPLOYEE where CODE_EMP = ?", BdOuEmployee.class,reqData.getEmpCode());
				if(listEmp!=null&&listEmp.size()>0){
					emp = listEmp.get(0);
				}
			}
			
			String xml=parseXML("emrBldTrnEff");
			if(StringUtils.isEmpty(xml)) return "xml????????????";
			if(emp==null) return "?????????????????????";
			//??????
			xml=xml.replaceAll("test0323", pat.getName());
			//??????
			xml=xml.replaceAll("???", pat.getNameSex());
			//??????
			xml=xml.replaceAll("???1", pat.getBedNo());
			//?????????
			xml=xml.replaceAll("806396", pat.getCodeIp());
			//????????????
			xml=xml.replaceAll("????????????????????????", pat.getNameDept());
			//????????????
			String recTime=eff.getRecTime();
			if(recTime!=null){
				if(recTime.length()>16){
					xml=xml.replaceAll("2019-05-10 12:00", recTime.substring(0, 15));
				}else{
					xml=xml.replaceAll("2019-05-10 12:00", recTime);
				}
				
			}
			//????????????
			String operTime=eff.getOperTime();
			if(operTime!=null){
				if(operTime.length()>16){
					xml=xml.replaceAll("2019-05-10 00:00", operTime.substring(0, 15));
				}else{
					xml=xml.replaceAll("2019-05-10 00:00", operTime);
				}
				
			}
			//??????
			xml=xml.replaceAll("AB???", getAboName(eff.getAboName()));
			//????????????
			xml=xml.replaceAll("???????????????", eff.getTypeName());
			//??????
			xml=xml.replaceAll("??????????????????????????????????????????????????????/?????????", eff.getEffect());
			//????????????
			xml=xml.replaceAll("?????????/????????????", eff.getReItem());
			//????????????
			xml=xml.replaceAll("??????", eff.getReEffect());
			//??????
			String empName=reqData.getEmpName();
			if(StringUtils.isEmpty(empName)) empName=emp.getNameEmp();
			xml=xml.replaceAll("??????",empName);
			//????????????
			String pkRec=NHISUUID.getKeyId();
			String pkDoc=NHISUUID.getKeyId();
            xml=xml.replaceAll("a23308c0817841d8bbe18cc1b966be76",pkRec);
            xml=xml.replaceAll("d3a921eeeede4bbe8c4f7f8413fce877",pkDoc);
            String pkTmp=ApplicationUtils.getPropertyValue("emr.eff.rec.tmp", "5534e3ab231e45e282172b7fa3c5b6df");
            xml=xml.replaceAll("5534e3ab231e45e282172b7fa3c5b6df",pkTmp);
            xml=xml.replaceAll("2019-05-12 18:09:40",sdf.format(now));//create_date
            xml=xml.replaceAll("2019-05-12 18:09",recTime.substring(0, 15));//rec_date
            xml=xml.replaceAll("b460dce3c0b2481080b116781ea4bf5b",emp.getPkEmp());//creator
            
            xml = xmlRepTxt(xml);
            
			genMedRec("?????????????????????",now, pat, reqData, eff, emp, xml, pkRec,pkDoc,pkTmp);
			
		}else{
			return "?????????????????????";
		}
		//rtn.setRstList(list);
		return null;
	}
	
	public String parseXML(String xmlName){
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("utf-8");
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String path = LoadDataSource.class.getClassLoader().getResource("config/emr/"+xmlName+".xml").getFile().toString();
			Document document = saxReader.read(new File(path));

			String xml=document.asXML();
			
			return xml;
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ????????????????????????
	 * @param map
	 * @return
	 */
	public List<EmrHmFirstCourseRslt> queryHmFirstCourse(Map<String,Object> map){
		List<EmrHmFirstCourseRslt> list = recMapper.queryHmFirstCourse(map);
		List<EmrHmFirstCourseRslt> rtnList = new ArrayList<EmrHmFirstCourseRslt>();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				EmrHmFirstCourseRslt item = list.get(i);
				String docXml=item.getDocXml();
				if(StringUtils.isEmpty(docXml)) continue;
				//??????
				item.setZs(EmrUtils.getEmrNoteText(docXml,"0002796"));
				//?????????
				item.setXbs(EmrUtils.getEmrNoteText(docXml,"0002634"));
				//?????????
				item.setJws(EmrUtils.getEmrNoteText(docXml,"0002283"));
				//????????????
				item.setTgjc(EmrUtils.getEmrNoteText(docXml,"0002814"));
				//????????????
				item.setFzjc(EmrUtils.getEmrNoteText(docXml,"0002170"));
				//????????????
				item.setZdyj(EmrUtils.getEmrNoteText(docXml,"0002739"));
				//????????????
				item.setCbzd(EmrUtils.getEmrNoteText(docXml,"0002464"));
				//????????????
				item.setJbzd(EmrUtils.getEmrNoteText(docXml,"0002339"));
				//????????????
				item.setTreatment(EmrUtils.getEmrNoteText(docXml,"0002743"));
				//????????????
				//item.setDocTxt(EmrUtils.getDocTxt(docXml));
				
				item.setDocXml("");

				rtnList.add(item);
			}
		}
		return rtnList;
	}
	
	/**
	 * ??????????????????
	 * @param map
	 * @return
	 */
	public List<EmrHmCourseRslt> queryHmCourse(Map<String,Object> map){
		List<EmrHmCourseRslt> list = recMapper.queryHmCourse(map);
		List<EmrHmCourseRslt> rtnList = new ArrayList<EmrHmCourseRslt>();
		List<EmrHmCourseRslt> admitList = new ArrayList<EmrHmCourseRslt>();
		EmrHmCourseRslt item=null;
		Map<String,Object> admitMap=new HashMap<String, Object>();
		String pkPv="";
		String docXmlAdmit="";
		if(list!=null&&list.size()>0){
			item=list.get(0);
			for(int i=0;i<list.size();i++){
				item = list.get(i);
				String docXml=item.getDocXml();
				if(StringUtils.isEmpty(docXml)) continue;
				if(!pkPv.equals(item.getPkPv())){
					pkPv=item.getPkPv();
					admitMap.put("pkPv", admitMap);
					admitList = recMapper.queryHmCourse(admitMap);
					if(admitList!=null&&admitList.size()>0){
						docXmlAdmit=admitList.get(0).getDocXml();
					}else{
						docXmlAdmit="";
					}
				}
				if(docXmlAdmit==null) docXmlAdmit="";
				if(!docXmlAdmit.equals("")){
					//??????
					item.setZs(EmrUtils.getEmrNoteText(docXmlAdmit,"0002796"));
					//????????????
					item.setRyzd(EmrUtils.getEmrNoteText(docXmlAdmit,"0002464"));
					//????????????
					item.setMqzd(EmrUtils.getEmrNoteText(docXmlAdmit,"0002464"));
				}
				//????????????
				item.setRyqk(EmrUtils.getEmrNoteText(docXml,"0002460"));
				//????????????
				item.setZljg(EmrUtils.getEmrNoteText(docXml,"0002741"));
				//????????????
				item.setMqqk(EmrUtils.getEmrNoteText(docXml,"0002412"));
				//?????????????????????NextZljh
				item.setZljh(EmrUtils.getEmrNoteText(docXml,"0002743"));
				//????????????
				item.setDocTxt(EmrUtils.getDocTxt(docXml));
				
				item.setDocXml("");

				rtnList.add(item);
			}
		}
		return rtnList;  
	}
	
	/**
	 * ????????????????????????-??????
	 * @param map
	 * @return
	 */
	public List<EmrAfHomePage> queryAfHomePage(Map<String,Object> map){
		List<EmrAfHomePage> rtnList = recMapper.queryAfHomePage(map);
		List<String> pks=new ArrayList<String>();
		int i=0;
		int j=0;
		int size=0;
		if(rtnList!=null&&rtnList.size()>0){
			size=rtnList.size();
			for(i=0;i<size;i++){
				EmrAfHomePage item = rtnList.get(i);
				
				String ageTxt=item.getAgeTxt();
				
				if(ageTxt==null||ageTxt.equals("")) continue;
				if(ageTxt.indexOf("Y")>=0){
					ageTxt=ageTxt.substring(0,1);
					try {
						int age=Integer.parseInt(ageTxt);
						if(age<1){
							ageTxt="";
						}
					} catch (Exception e) {
						ageTxt="";
					}
					
				}else if(ageTxt.indexOf("D")>=0){
					ageTxt="";
				}
				item.setAgeTxt(ageTxt);
				
				item.setAdmitDeptCode("0304");//?????????????????????
				item.setDisDeptCode("0304");
				
				pks.add(item.getPkPage());
			}
			List<EmrHomePageDiags> diagList = recMapper.queryHomePageDiagsByPks(pks);
			List<EmrHomePageOps> opsList = recMapper.queryHomePageOpsByPks(pks);
			List<EmrHomePageCharges> chargeList = recMapper.queryHomePageChargesByPks(pks);
			String fieldName="";
			for(i=0;i<size;i++){
				EmrAfHomePage page = rtnList.get(i);
				if(diagList!=null&&diagList.size()>0){
					for(j=0;j<diagList.size();j++){
						EmrHomePageDiags diag=diagList.get(j);
						if(diag.getPkPage().equals(page.getPkPage())){
							int seqNo = diag.getSeqNo();
							setFieldValue(page, ("diagCodeDis"+Integer.toString(seqNo-1)).replace("0", ""), diag.getDiagCode());
							setFieldValue(page, ("admitCondCode"+Integer.toString(seqNo-1)), diag.getAdmitCondCode());
							setFieldValue(page, ("diagNameDis"+Integer.toString(seqNo-1)).replace("0", ""), diag.getDiagName());
						}
					}
				}
				if(opsList!=null&&opsList.size()>0){
					for(j=0;j<opsList.size();j++){
						EmrHomePageOps ops=opsList.get(j);
						if(ops.getPkPage().equals(page.getPkPage())){
							int seqNo = ops.getSeqNo();
							setFieldValue(page, "opCode"+Integer.toString(seqNo-1), ops.getOpCode());
							setFieldValue(page, "opDate"+Integer.toString(seqNo-1), ops.getOpDate());
							setFieldValue(page, "gradeCode"+Integer.toString(seqNo-1), ops.getGradeCode());
							setFieldValue(page, "opName"+Integer.toString(seqNo-1), ops.getOpName());
							setFieldValue(page, "opDocName"+Integer.toString(seqNo-1), ops.getOpDocName());
							setFieldValue(page, "opiName"+Integer.toString(seqNo-1), ops.getOpiName());
							setFieldValue(page, "opiiName"+Integer.toString(seqNo-1), ops.getOpiiName());
							setFieldValue(page, "anesTypeCode"+Integer.toString(seqNo-1), ops.getAnesTypeCode());
							setFieldValue(page, "anesLevelCode"+Integer.toString(seqNo-1), ops.getIncisionTypeCode());
							setFieldValue(page, "anesDocName"+Integer.toString(seqNo-1), ops.getAnesDocName());
						}
					}
				}
				if(chargeList!=null&&chargeList.size()>0){
					for(j=0;j<chargeList.size();j++){
						EmrHomePageCharges charge=chargeList.get(j);
						if(charge.getPkPage().equals(page.getPkPage())){
							int seqNo = charge.getSeqNo();
							if(seqNo==0){
								page.setTotalCost(charge.getItemAmount());
							}else if(seqNo==99){
								page.setSelfCost(charge.getItemAmount());
							}else{
								setFieldValue(page, "cost"+Integer.toString(seqNo), charge.getItemAmount());
							}
						}
					}
				}
			}
		}
		return rtnList;
	}
	
	
	/**
	 * ?????????????????????-??????
	 * @param map
	 * @return
	 */
	public List<EmrAfAdmitRec> queryAfAdmitRec(Map<String,Object> map){
		List<EmrAfAdmitRec> rtnList = recMapper.queryAfAdmitRec(map);
		int i=0;
		int size=0;
		if(rtnList!=null&&rtnList.size()>0){
			size=rtnList.size();
			for(i=0;i<size;i++){
				EmrAfAdmitRec item = rtnList.get(i);
				
				String ageTxt=item.getAgeTxt();
				
				if(ageTxt==null||ageTxt.equals("")) continue;
				if(ageTxt.indexOf("Y")>=0){
					ageTxt=ageTxt.substring(0,1);
					try {
						int age=Integer.parseInt(ageTxt);
						if(age<1){
							ageTxt="";
						}
					} catch (Exception e) {
						ageTxt="";
					}
					
				}else if(ageTxt.indexOf("D")>=0){
					ageTxt="";
				}
				item.setAgeTxt(ageTxt);
				//??????xml
				String docXml=item.getDocXml();
				//???????????????
				item.setMedHistRep(EmrUtils.getEmrNoteText(docXml, "0002040"));
				//??????????????????????????????
				item.setMedHistRepRelate(EmrUtils.getEmrNoteText(docXml, "0002090"));
				//??????
				item.setChiefComplaint(EmrUtils.getEmrNoteText(docXml, "0002796"));
				//?????????
				item.setPresentIllnessHistory(EmrUtils.getEmrNoteText(docXml, "0002634"));
				//?????????
				item.setPastHistory(EmrUtils.getEmrNoteText(docXml, "0002283"));
				//?????????
				item.setPersonalHistory(EmrUtils.getEmrNoteText(docXml, "0002182"));
				//?????????
				item.setMarriageHistory(EmrUtils.getEmrNoteText(docXml, "0002265"));
				if(item.getDtSex()!=null&&item.getDtSex().equals("2")){
					//?????????
					item.setMenstrualHistory(EmrUtils.getEmrNoteText(docXml, "0002728"));
				}
				//?????????
				item.setFamilyHistory(EmrUtils.getEmrNoteText(docXml, "0002287"));
				//????????????/??????
				item.setPhysicalExamination(EmrUtils.getEmrNoteText(docXml, "02"));
				//????????????
				item.setFamilyHistory(EmrUtils.getEmrNoteText(docXml, "0002170"));
				//????????????
				//item.setMedicalHistorySummary(EmrUtils.getEmrNoteText(docXml, ""));
				//????????????/??????
				item.setFamilyHistory(EmrUtils.getEmrNoteText(docXml, "07001"));
			}
			

		}
		return rtnList;
	}
	
	/**
	 * ??????????????????-??????
	 * @param map
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<EmrAfCourseRec> queryAfCourseRec(Map<String,Object> map) throws IllegalAccessException, InvocationTargetException{
		List<EmrAfCourseRec> list = recMapper.queryAfCourseRec(map);
		List<EmrAfCourseRec> rtnList = new ArrayList<>();
		List<String> pks=new ArrayList<String>();
		int i=0;
		int size=0;
		String pkPv="";
		EmrAfCourseRec rtnItem=null;
		if(list!=null&&list.size()>0){
			size=list.size();
			for(i=0;i<size;i++){
				EmrAfCourseRec item = list.get(i);
				if(!pkPv.equals(item.getPkPv())){
					rtnItem=new EmrAfCourseRec();
					BeanUtils.copyProperties(rtnItem, item);
					rtnList.add(rtnItem);
					pkPv=item.getPkPv();
				}
				
				String ageTxt=item.getAgeTxt();
				
				if(ageTxt==null||ageTxt.equals("")) continue;
				if(ageTxt.indexOf("Y")>=0){
					ageTxt=ageTxt.substring(0,1);
					try {
						int age=Integer.parseInt(ageTxt);
						if(age<1){
							ageTxt="";
						}
					} catch (Exception e) {
						ageTxt="";
					}
					
				}else if(ageTxt.indexOf("D")>=0){
					ageTxt="";
				}
				rtnItem.setAgeTxt(ageTxt);
				//??????xml
				String docXml=item.getDocXml();
				String flagFirst=item.getFlagFirst();
				String flagCourse=item.getFlagCourse();
				if(docXml==null||docXml.equals("")) continue;
				String docTxt=EmrUtils.getDocTxt(docXml);
				String typeCode=item.getTypeCode();
				if(typeCode==null||typeCode.equals("")) continue;
				if(rtnItem.getDailyDiseaseCourse()==null){
					rtnItem.setDailyDiseaseCourse(new ArrayList<String>());
				}
				//????????????
				if(rtnItem.getSalvageRecord()==null){
					rtnItem.setSalvageRecord(new ArrayList<String>());
				}
				//????????????
				if(rtnItem.getOperationRecord()==null){
					rtnItem.setOperationRecord(new ArrayList<String>());
				}
				
				if(flagCourse!=null&&flagCourse.equals("1")){
					if(flagFirst!=null&&flagFirst.equals("1")){
						//????????????
						rtnItem.setFirstDiseaseCourse(docTxt);
					}else{
						//????????????
						rtnItem.getDailyDiseaseCourse().add(docTxt);
						//????????????
						if(typeCode.equals("100008")){
							rtnItem.getSalvageRecord().add(docTxt);
						}
					}
				}else{
					if(typeCode.equals("050102")){
						//????????????
						rtnItem.getOperationRecord().add(docTxt);
					}else if(typeCode.equals("120001")){
						//????????????
						rtnItem.setDischargeRecord(docTxt);
					}else if(typeCode.equals("120002")){
						//????????????
						rtnItem.setDeathRecord(docTxt);
					}
				}
			}
			

		}
		return rtnList;
	}
	
	public static void setFieldValue(Object obj, String fieldName,Object value) {
		try {
			if(value==null||value.toString().equals("null")) return;
			
			// ??????obj????????????????????????
			Class c = obj.getClass();
			// ???????????????????????????
			Field f = c.getDeclaredField(fieldName);
			// ????????????????????????
			f.setAccessible(true);
			// ???????????????
			f.set(obj, value);
		} catch (Exception e) {
			//System.out.println("setFieldValue error:"+fieldName+":"+value+":"+e.getMessage());
		}

	}

}
