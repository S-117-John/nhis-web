package com.zebone.nhis.common.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;


import com.zebone.nhis.common.arch.vo.ArchUploadParam;
import com.zebone.nhis.common.arch.vo.HISFileInfo4Upd;
import com.zebone.nhis.common.arch.vo.UploadBody;
import com.zebone.nhis.common.arch.vo.UploadHead;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.ma.pub.arch.vo.ArchUploadContent;
import com.zebone.nhis.ma.pub.arch.vo.ArchUploadHead;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class ArchUtils {
	
    public static final String  BASE_DIR_OLD = "/archiveNew";
    public static final String  BASE_DIR = "/NHISArchive";
    public static final String  SEPARATOR = "/";
    public static final String  XMLSUFFIX = ".xml";
    public static String  FILESUFFIX = ".pdf";
    public static String ZEROSTR ="000000000000";
    public static List<String> PVTYPE = new ArrayList<String>();
    
	
	
    static{
    	PVTYPE.add("1");
    	PVTYPE.add("2");
    	PVTYPE.add("3");
    }
	
	
	public static ArchUploadParam genUpdParam(PvEncounter pv ,String codePati,String fileName,String chnName ,String fileType,byte[] fileContent){
		ArchUploadParam upPara= new ArchUploadParam();
	    UploadBody body = new UploadBody(); 
	    body.setCafilecontent(null);
	    body.setCafilename(null);
	    body.setFilecontent(Base64.getEncoder().encodeToString(fileContent).trim());
	    upPara.setUploadBody(body);
	    UploadHead head = new UploadHead();
	    
	    head.setDate(dateTrans(new Date(),"yyyy-MM-dd HH:mm:ss"));
	    head.setDocname(fileName);
	    head.setDoctype(fileType);
	    head.setMemo(chnName);
	    head.setPath(null);
	    head.setPaticode(codePati);
	    head.setPatiname(pv.getNamePi());
	    head.setStatus("0");
	    head.setSysname("HIS");
	    head.setUser(UserContext.getUser().getNameEmp());
	    head.setVisitcode(pv.getCodePv());
	    head.setVisitdate(dateTrans(pv.getDateBegin(),"yyyy-MM-dd HH:mm:ss"));
	    head.setVisittype(pv.getEuPvtype());
	    
	    head.setAge(pv.getAgePv());
	    head.setIptimes("1");
	    
	    upPara.setUploadHead(head);
		return upPara;
		
	}
	
	public static ArchUploadParam genUpdParam(HISFileInfo4Upd param){
		if(param!=null){
			PvEncounter pv = DataBaseHelper.queryForBean(" select pv.*,pi.code_pi from pv_encounter pv inner join pi_master pi on pi.pk_pi = pv.pk_pi where pv.pk_pv = ? ", PvEncounter.class, param.getPkPv());
			return genUpdParam(pv ,pv.getCodePi(),param.getFileName(),param.getChnName() ,param.getFileType(),param.getFileConetent());
		}else{
			return null;
		}
		
	}
	
	
	
	public static String dateTrans(Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String temp = null;
		if(date!=null){
			temp = sdf.format(date);
		}
		
		return temp;
	}
	
	public static String get1stDir(String paticode){
		paticode = paticode.replaceAll("[a-zA-Z]", "");
			Long sno = Long.parseLong(paticode);
			Long s = sno/600000;
			Long y = sno/600000;
			return y>=0?(s*600000)+"~"+((s+1)*600000):((s-1)<0?0:(s-1)*600000)+"~"+(s*600000);

		}
	
	public static String get1stDirOld(String paticode){
		paticode = paticode.replaceAll("[a-zA-Z]", "");
			Long sno = Long.parseLong(paticode);
			Long s = sno/6000;
			Long y = sno/6000;
			return y>=0?(s*6000)+"~"+((s+1)*6000):((s-1)<0?0:(s-1)*6000)+"~"+(s*6000);

		}
	
	public static String patidHandle(String paticode){
		String res = "";
		if(CommonUtils.isEmptyString(paticode)){
			res =paticode;
		}else{
			if(paticode.length()<12){
				res = ZEROSTR+paticode+"00";
				res = res.substring(res.length()-12, res.length());
			}else{
				res = paticode;
			}
		}
		return res;
	}
	
	
	
	
	public static void main(String args[]){
		//System.out.println(get1stDir("700061300"));
		System.out.println(patidHandle("3"));
	}

	public static ArchUploadParam handlePaticode(ArchUploadParam fileInfo) {
        UploadHead head = fileInfo.getUploadHead();
        String pvtype = head.getVisittype();
        String paticode = head.getPaticode();
        String visitcode = head.getVisitcode();
        String iptimes = head.getIptimes();
        
        //1.处理患者ID
        if("1".equals(pvtype) || "2".equals(pvtype) || "3".equals(pvtype)){
        	//1.2门诊，急诊，住院都转换成12位
        	head.setPaticode(patidHandle(paticode));
        	
        	//1.2处理就诊记录号
        	if("3".equals(pvtype)){
        		//1.2.1住院流程
        		head.setVisitcode(visitcode+"_"+iptimes);
        		
        	}else{
        		//1.2.2门诊，急诊流程
        		head.setVisitcode(patidHandle(visitcode));
        	}
        }        
        fileInfo.setUploadHead(head);
		return fileInfo;
	}
	
	public static ArchUploadParam handlePaticodeZsrm(ArchUploadParam fileInfo) {
        UploadHead head = fileInfo.getUploadHead();
//        String visitcode = head.getVisitcode();
//        if(visitcode!=null&&visitcode.indexOf("|")>=0) {
//        	visitcode=visitcode.replace("|", "_");
//        	head.setVisitcode(visitcode);
//        	fileInfo.setUploadHead(head);
//        }
        String visitcode=head.getPaticode()+"_"+head.getTimes();
        head.setVisitcode(visitcode);
    	fileInfo.setUploadHead(head);
        
		return fileInfo;
	}
	
	public static ArchUploadContent handlePaticodeNew(ArchUploadContent fileInfo) {
		ArchUploadHead head = fileInfo.getHead();

        String visitcode=head.getPaticode()+"_"+head.getTimes();
        head.setVisitcode(visitcode);
    	fileInfo.setHead(head);
        
		return fileInfo;
	}
	
	public static String getErrorXml(ArchUploadParam fileInfo){
		String res = "";
		if(fileInfo!=null ){
//			UploadBody body = fileInfo.getUploadBody();
//			body.setCafilecontent(null);
//			body.setFilecontent(null);
			fileInfo.setUploadBody(null);
			res = XmlUtil.beanToXml(fileInfo, fileInfo.getClass());
		}
		
		
		
		return res;
	}

}
