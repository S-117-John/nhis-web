package com.zebone.nhis.common.arch.service;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import com.zebone.nhis.common.arch.vo.ArchUploadParam;
import com.zebone.nhis.common.arch.vo.PaperVo;
import com.zebone.nhis.common.arch.vo.PatiInfo;
import com.zebone.nhis.common.arch.vo.UploadHead;
import com.zebone.nhis.common.module.arch.BdArchDoctype;
import com.zebone.nhis.common.module.arch.PvArchDoc;
import com.zebone.nhis.common.module.arch.PvArchOpt;
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.nhis.common.support.PinyinUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * ??????????????????
 * @author Roger
 *
 */
@Service
public class ArchiveCollectService {
	
    
	@Autowired
	private ArchOuterService archOuterService;
	public Logger logger = LogManager.getLogger(ArchiveCollectService.class.getName());
	
	//??????????????????

	
	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 */
    @SuppressWarnings("unchecked")
	public void geneArchRecrd(String param, IUser user) { 
		
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String pkPv = params.get("pkPv");
		
		geneArchRecrd(pkPv);
		
	}
	
	/**
	 * ???????????????????????????????????????url
	 * @param param
	 * @param user
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<String> getImagePaths(String param, IUser user) { 
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String dir = params.get("dir");
		if(CommonUtils.isEmptyString(dir)){
			throw new BusException("??????????????????????????????????????????");
		}
		return FTPUtil.getFileListPath(dir);		
	}

	
	/**
	 * ?????????????????? Pv_Archive
	 * @param pkPv
	 * @return
	 */
	public PvArchive geneArchRecrd(String pkPv){
		
		List<PvArchive> voList = DataBaseHelper.queryForList("select * from pv_archive where pk_pv = ?", PvArchive.class, pkPv);
		PvArchive vo = null;
		if(voList!=null && voList.size()>0){
			vo = voList.get(0);
		}else{
			vo = new PvArchive();
			vo.setPkOrg(UserContext.getUser().getPkOrg());
			vo.setPkPv(pkPv);
			vo.setFlagArch("0");
			vo.setFlagRev("0");
			vo.setEuStatus("0");
			vo.setEuArchtype("0");
			vo.setCntArch("0");
			vo.setCntTotal("0");
			ApplicationUtils.setDefaultValue(vo, true);
			int cnt = DataBaseHelper.insertBean(vo);
			vo = cnt>0?DataBaseHelper.queryForBean("select * from pv_archive where pk_Archive =?", PvArchive.class, vo.getPkArchive()):null;

		}
		
		return vo;
	}
	public PvArchive geneArchRecrdByPvEn(PvEncounter pkPv){
		List<PvArchive> voList = DataBaseHelper.queryForList("select * from pv_archive where pk_pv = ?", PvArchive.class, pkPv.getPkPv());
		PvArchive vo = null;
		if(voList!=null && voList.size()>0){
			vo = voList.get(0);
		}else{
			vo = new PvArchive();
			vo.setPkOrg(pkPv.getPkOrg());
			vo.setPkPv(pkPv.getPkPv());
			vo.setFlagArch("0");
			vo.setFlagRev("0");
			vo.setEuStatus("0");
			vo.setEuArchtype("0");
			vo.setCntArch("0");
			vo.setCntTotal("0");
			vo.setFlagDel("0");
			vo.setCreateTime(new Date());
			vo.setTs(new Date());
			int cnt = DataBaseHelper.insertBean(vo);
			vo = cnt>0?DataBaseHelper.queryForBean("select * from pv_archive where pk_Archive =?", PvArchive.class, vo.getPkArchive()):null;

		}
		return vo;
	}
	/**
	 * ????????????????????????
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void geneDocOperRecrd(String param, IUser user) { 
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String pkDoc = params.get("pkDoc");//?????? pk_doc
		String euOptype = params.get("euOptype");//???????????? eu_optype ???0?????? 1 ?????????
		geneDocOperRecrd(pkDoc,euOptype);
		
	}

	public void geneDocOperRecrd(String pkDoc,String euOptype){
		PvArchOpt vo = new PvArchOpt();
		
		vo.setPkEmpOp(UserContext.getUser().getPkEmp());
		vo.setNameEmpOp(UserContext.getUser().getNameEmp());
		vo.setPkOrg(UserContext.getUser().getPkOrg());
		vo.setPkDoc(pkDoc);
		vo.setEuOptype(euOptype);
		vo.setDateOp(new Date());
		ApplicationUtils.setDefaultValue(vo, true);
		DataBaseHelper.insertBean(vo);
		
	}
	
	/**
	 * ???????????????????????????
	 * @param system
	 * @return
	 */
	public List<BdArchDoctype> getDoctype(String system){
		return CommonUtils.isEmptyString(system)?null:DataBaseHelper.queryForList("select doc.code_doctype, doc.name_doctype,doc.code_map from bd_arch_doctype doc where doc.flag_active='1' and doc.dt_systype=?", BdArchDoctype.class, system);
	}
	
	/**
	 * ??????Pv_Arch_Doc??????
	 * @param arch
	 * @param nameDoc
	 * @param pkDoctype
	 * @return
	 */
	public PvArchDoc geneArchiveDoc(PvArchive arch,String nameDoc,String pkDoctype){
		PvArchDoc doc = new PvArchDoc();
		doc.setPkOrg(UserContext.getUser().getPkOrg());
		doc.setPkArchive(arch.getPkArchive());
		doc.setNameDoc(nameDoc);
		doc.setPkDoctype(pkDoctype);
		doc.setFlagUpload("0");
		doc.setFlagValid("0");
		ApplicationUtils.setDefaultValue(doc, true);
		int cnt = DataBaseHelper.insertBean(doc);
		
		return cnt>0?DataBaseHelper.queryForBean("select * from pv_arch_doc where pk_doc =?", PvArchDoc.class, doc.getPkDoc()):null;
	}
	
	
	/**
	 * ????????????--????????????????????????????????????
	 * @param param ????????????pk_pv
	 * @param user
	 */
	public void recycleReg(String param, IUser user) {		
		List<PatiInfo> params = JsonUtil.readValue(param, new TypeReference<List<PatiInfo>>() {});
		
		if(params!=null&& params.size()>0){
			for(PatiInfo vo : params){
				String pkPv = vo.getPkPv();
				int cnt = DataBaseHelper.queryForScalar("select count(1) from Pv_Archive where pk_pv = ?", Integer.class, pkPv);
				if(cnt<1){
					geneArchRecrd(pkPv);
				}
				
				DataBaseHelper.execute(" update pv_archive set flag_rev='1',date_rev = ? ,pk_emp_rev=?,name_emp_rev =? where pk_pv=? and flag_rev='0'", new Date(),UserContext.getUser().getPkEmp(),UserContext.getUser().getNameEmp(),pkPv);
			}
		}
	}
	
	
	/**
	 * ?????????????????????
	 * @param pv
	 * @param code_pi
	 * @return
	 */
	public String getFolder(PvEncounter pv,String code_pi) {
		StringBuilder folder =  new StringBuilder("/archive/");
		folder.append(code_pi);
		folder.append("(");
		folder.append(PinyinUtils.getPingYin(pv.getNamePi()));
		folder.append(")");
		folder.append("/");
		folder.append(pv.getCodePv());
		folder.append("_");
		folder.append(dateTrans(pv.getDateEnd()));
		return folder.toString();
	}

	public String dateTrans(Date date) {
		return dateTrans(date,"yyyyMMdd");
	}
	
	public String dateTrans(Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String temp = null;
		if(date!=null){
			temp = sdf.format(date);
		}
		
		return temp;
	}
	/**
	 * ????????????????????????????????????????????????????????????
	 * @param param
	 * @param user
	 */
	public void scanUpload(String param,IUser user){
		
		
		//1.???????????????????????????????????????????????????
		

		//1.????????????
		List<PaperVo> params = JsonUtil.readValue(param, new TypeReference<List<PaperVo> >(){});
		if(params!=null && params.size()>0){
			int SN = 1;
			for(PaperVo vo : params){
				
				PvEncounter pv = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv =?", PvEncounter.class, vo.getPkPv());
				PiMaster pi = DataBaseHelper.queryForBean("select * from pi_master where pk_pi =?", PiMaster.class, pv.getPkPi());
				PvArchive arch = geneArchRecrd(vo.getPkPv());
				BdArchDoctype type = DataBaseHelper.queryForBean("select * from bd_arch_doctype where pk_doctype =?", BdArchDoctype.class, vo.getPkDoctype());
				String path = vo.getImagePath();//"/archive";
				if(CommonUtils.isEmptyString(path)){
					logger.error("????????????????????????????????????????????????????????????"+pv.getNamePi()+"/"+type.getNameDoctype());
					continue;
				}
				//2.????????????
				String fileName = path.substring(path.lastIndexOf("/")+1);
				path = path.substring(path.indexOf("//")+2);
				path = path.substring(path.indexOf("/"), path.lastIndexOf("/"));
				
				//3.????????????
				byte[] img = FTPUtil.downloadFile(path, fileName).toByteArray();
				
				//4.pdf??????
				byte[] pdf = imag2PDF(img,fileName);
				
				//5.????????????????????????
				// String folder = getFolder(pv, pi.getCodePi());
						 StringBuilder sbDir = new StringBuilder(ArchUtils.BASE_DIR);
					sbDir.append(ArchUtils.SEPARATOR).append(ArchUtils.get1stDir(pi.getCodePi())).
					append(ArchUtils.SEPARATOR).append(ArchUtils.patidHandle(pi.getCodePi())).
					append(ArchUtils.SEPARATOR).append(pv.getCodePv()+"_1");

					 String folder = sbDir.toString();
				 //6.?????????????????????
				 String NewFileName = "HIS" + "_"
						+ type.getCodeDoctype() + "_" + SN+".pdf";
				 //7.??????????????????
				 PvArchDoc doc = genArchDoc(NewFileName, vo.getPkDoctype(), UserContext.getUser().getPkOrg(), null, arch,null,null);
				 
				 
				 //8.?????????????????????????????????
				    //8.1????????????
//				    ArchUploadParam upPara= new ArchUploadParam();
//				    UploadBody body = new UploadBody(); 
//				    body.setCafilecontent(null);
//				    body.setCafilename(null);
//				    body.setFilecontent(encoder.encodeBuffer(pdf).trim());
//				    upPara.setUploadBody(body);
//				    UploadHead head = new UploadHead();
//				    
//				    head.setDate(dateTrans(new Date(),"yyyy-MM-dd HH:mm:ss"));
//				    head.setDocname(NewFileName);
//				    head.setDoctype(type.getPkDoctype());
//				    head.setMemo(type.getNameDoctype()+"_"+type.getNameDoctype());
//				    head.setPath(path);
//				    head.setPaticode(pi.getCodePi());
//				    head.setPatiname(pi.getNamePi());
//				    head.setStatus("0");
//				    head.setSysname("HIS");
//				    head.setUser(UserContext.getUser().getNameEmp());
//				    head.setVisitcode(pv.getCodePv());
//				    head.setVisitdate(dateTrans(pv.getDateBegin(),"yyyy-MM-dd HH:mm:ss"));
//				    head.setVisittype(pv.getEuPvtype());
//				    upPara.setUploadHead(head);
				    
				    ArchUploadParam upPara = ArchUtils.genUpdParam(pv, pi.getCodePi(), NewFileName, type.getNameDoctype()+"_"+type.getNameDoctype(), type.getCodeDoctype(), pdf);
				 
				 //8.????????????
				// FTPUtil.uploadFileNew(pdf, folder+"/"+NewFileName, "admin");
				 archOuterService.uploadFile(upPara);
				 //9.??????????????????
				 updateArchDoc(pdf.length, null, doc, folder, 1,null);
				 
				 //10.????????????????????????
				 geneDocOperRecrd(doc.getPkDoc(), "1");
				 
				 SN++;
				
			}
		}
		
		
		
	}
	
	
	  
	private byte[] imag2PDF(byte[] input,String fileName) {
		com.lowagie.text.Document document = new com.lowagie.text.Document();
		ByteArrayOutputStream target = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, target);
			document.open();
			Image img = Image.getInstance(input);
			document.addAuthor("EmrArchiveApplication");
			document.addCreationDate();
			document.addCreator("iText library");
			document.addTitle("ScannerImg");
			if(fileName.startsWith("ARC_")){
				img.setWidthPercentage(100);
			}else{
				img.scalePercent(75);
			}
			document.add(img);
		} catch (Exception e) {
			logger.info("##Not Picture Formate," + e.getMessage());
			return input;
		}
		document.close();
		return target.toByteArray();
	}
	

	

	

	
	
	
	
	
	protected String getCurrentTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date(System.currentTimeMillis()));
	}
	
		public PvArchive genPvArchive(PvEncounter pv, BdOuUser usr) {
			PvArchive PA;
			PA = new PvArchive();
			PA.setPkOrg(pv.getPkOrg());
			PA.setPkPv(pv.getPkPv());
			PA.setFlagArch("0");
			PA.setFlagRev("0");
			PA.setEuStatus("0");
			PA.setEuArchtype("0");
			PA.setCntArch("0");
			PA.setCntTotal("0");
			setDefaultValue(PA, true,usr);
			DataBaseHelper.insertBean(PA);
			return PA;
		}
	
		public synchronized int updateArchDoc(double size, BdOuUser usr, PvArchDoc doc,
				String position, int pages,byte[] docData) {
			DataSourceRoute.putAppId(null);
			doc.setPosition(position);//???????????????
			 doc.setPages(pages);//?????????????????????
			 doc.setSizeDoc(size);//????????????
			 doc.setFlagUpload("1");
			 doc.setDateUpload(new Date());//????????????			 
			 doc.setFlagValid("1");
			 doc.setDocData(docData);
			 if(usr!=null){
				 setDefaultValue(doc, false,usr);
				 doc.setPkEmpUpload(usr.getPkEmp());//???????????????
				 doc.setNameEmpUpload(usr.getNameUser());//?????????????????????
				
			 }else{
				 ApplicationUtils.setDefaultValue(doc, false);
				 doc.setPkEmpUpload(UserContext.getUser().getPkEmp());
				 doc.setNameEmpUpload(UserContext.getUser().getNameEmp());
			 }
			return  DataBaseHelper.updateBeanByPk(doc,false);
		}
		
		public PvEncounter getPvEncounter(String patient_id, String times, String visit_flag) {
	    	PvEncounter pvvo = null;
	    	String sql = " select pv.* from pv_ip pvip inner join pv_encounter pv on pv.pk_pv = pvip.pk_pv inner join  pi_master pi on pi.pk_pi = pv.pk_pi  where pi.code_pi = ? and pvip.ip_times = ?";
	    	
	    	List<PvEncounter> pvlist = DataBaseHelper.queryForList(sql, PvEncounter.class, patient_id,times);
	    	if(pvlist!=null && pvlist.size()>0){
	    		pvvo = pvlist.get(0);
	    	}
			return pvvo;
		}
	
		public PvArchDoc genArchDoc(String fileName, String docType,String pkOrg, BdOuUser usr, PvArchive pavo,String codeRpt,String nameRpt) {
			
			   DataSourceRoute.putAppId(null);
			   PvArchDoc doc = new PvArchDoc();
				    doc = new PvArchDoc();
					doc.setPkOrg(pkOrg);
					doc.setPkArchive(pavo.getPkArchive());
					doc.setNameDoc(fileName);
					doc.setPkDoctype(docType);
					doc.setFlagUpload("0");
					doc.setFlagValid("0");
					doc.setCodeRpt(codeRpt);
					doc.setNameRpt(nameRpt);
					if(usr==null){
						ApplicationUtils.setDefaultValue(doc, true);
					}else{
						setDefaultValue(doc, true,usr);
					}				
					String existSql="select * from pv_arch_doc where name_doc=?";
					PvArchDoc archDoc=DataBaseHelper.queryForBean(existSql, PvArchDoc.class, fileName);
					if(archDoc!=null){
						DataBaseHelper.deleteBeanByPk(archDoc);
					}
					DataBaseHelper.insertBean(doc);			
					return  DataBaseHelper.queryForBean("select * from pv_arch_doc where pk_doc =?", PvArchDoc.class, doc.getPkDoc());
		}
		private void setDefaultValue(Object obj, boolean flag,BdOuUser user) {
				
		
				Map<String,Object> default_v = new HashMap<String,Object>();
				if(flag){  // ????????????
					default_v.put("pkOrg", user.getPkOrg());
					default_v.put("creator", user.getPkUser());
					default_v.put("createTime",new Date());
					default_v.put("delFlag", "0");
				}
				
				default_v.put("ts", new Date());
				default_v.put("modifier",  user.getPkUser());
				
				Set<String> keys = default_v.keySet();
				
				for(String key : keys){
					Field field = ReflectHelper.getTargetField(obj.getClass(), key);
					if (field != null) {
						ReflectHelper.setFieldValue(obj, key, default_v.get(key));
					}
				}
			
				if (flag) { 
					Field[] fields = obj.getClass().getDeclaredFields();
					for (Field field : fields) {
						PK pKAnnotation = field.getAnnotation(PK.class);
						if (pKAnnotation != null) {
							ReflectHelper.setFieldValue(obj, field.getName(), NHISUUID.getKeyId());
							break;
						}
					}
				}
			}
		/**
		 * ?????????????????????????????????????????????
		 * @param emrOpeLogs
		 */
		public void saveEmrOperLogs(EmrOperateLogs emrOpeLogs) {
			String sql="select * from emr_operate_logs where pk_pv=? and code='archive' and del_flag='0'";
			EmrOperateLogs exist=DataBaseHelper.queryForBean(sql, EmrOperateLogs.class, emrOpeLogs.getPkPv());
			if(exist==null){
				DataBaseHelper.insertBean(emrOpeLogs);
			}else{
				emrOpeLogs.setPkLog(exist.getPkLog());
				DataBaseHelper.updateBeanByPk(emrOpeLogs);
			}
		}
		/**
		 * ????????????pv_archive??????--??????
		 * @param pkPv
		 * @return
		 */
		public PvArchive getPvArchiveByPvEn(UploadHead headInfo){
			int times = 0;
			List<PvArchive> voList=null;
			if(headInfo.getTimes()!=null&&!headInfo.getTimes().equals("")) {
				times=Integer.parseInt(headInfo.getTimes());
			}
			PvArchive vo=null;
			PiMaster pi=null;
			String pkOrg=null;
			PvEncounter pv = getPvEncByConds(headInfo.getP_id(), times, headInfo.getPaticode(), headInfo.getVisitcode(), headInfo.getVisittype());
			if(pv!=null) {
				pkOrg=pv.getPkPv();
				voList = DataBaseHelper.queryForList("select * from pv_archive where pk_pv = ?",PvArchive.class,new Object[] {pv.getPkPv()});
			}else {
				pi = getPiByConds(headInfo.getPaticode(),headInfo.getVisittype());
			}
			if(voList==null||voList.size()==0) {
				vo = getPvArchiveByConds(headInfo.getP_id(), times, headInfo.getPaticode(), headInfo.getVisitcode(), headInfo.getVisittype(),headInfo.getPatiname());	
			}else {
				if(voList!=null&&voList.size()>0) {
					vo=voList.get(0);
				}
			}
			if(vo==null){
				vo = new PvArchive();
				//vo.setPkOrg(null);
				vo.setPkPv(null);
				vo.setFlagArch("0");
				vo.setFlagRev("0");
				vo.setEuStatus("0");
				vo.setEuArchtype("0");
				vo.setCntArch("0");
				vo.setCntTotal("0");
				vo.setFlagDel("0");
				vo.setCreateTime(new Date());
				vo.setTs(new Date());
				vo.setPatiCode(headInfo.getPaticode());
				vo.setPatiName(headInfo.getPatiname());
				Integer pvTimes=0;
				try {
					pvTimes = Integer.parseInt(headInfo.getTimes());
				} catch (Exception e) {
					// TODO: handle exception
				}
				vo.setTimes(pvTimes);
				vo.setPid(headInfo.getP_id());
				vo.setVisitCode(headInfo.getVisitcode());
				vo.setVisittype(headInfo.getVisittype());
				vo.setDateArch(new Date());
				if(StringUtils.isEmpty(pkOrg)) {
					if(UserContext.getUser()==null) {
						pkOrg=ApplicationUtils.getPropertyValue("arch.pkorg", "");;
					}else {
						pkOrg=UserContext.getUser().getPkOrg();
					}
				}
				
				vo.setPkOrg(pkOrg);
				if(pv!=null) {
					vo.setPkPv(pv.getPkPv());
					vo.setPkPi(pv.getPkPi());
				}else if(pi!=null) {
					vo.setPkPi(pi.getPkPi());
				}
				int cnt = DataBaseHelper.insertBean(vo);
				vo = cnt>0?DataBaseHelper.queryForBean("select * from pv_archive where pk_Archive =?", PvArchive.class, vo.getPkArchive()):null;

			}
			return vo;
		}
		
		public PvEncounter getPvEncByConds(String patientId, int times,String codeOpIp,String codePv, String visit_flag) {
	    	if(visit_flag==null) return null;
	    	PvEncounter pvvo = null;
	    	String timeStr=" and pvip.ip_times = ?";
	    	String codeOpIpStr=" and pi.code_ip = ?";
	    	String sql = " select pv.* from pv_ip pvip inner join "
	    			+ "       pv_encounter pv on pv.pk_pv = pvip.pk_pv inner join  "
	    			+ "       pi_master pi on pi.pk_pi = pv.pk_pi  "
	    			+ " where pv.eu_status<>'9'";
	    	//1??????2??????3??????4??????
	    	if(visit_flag.equals("1")) {
	    		//??????
	        	sql = " select pv.* from pv_op pvop inner join "
		    			+ "       pv_encounter pv on pv.pk_pv = pvop.pk_pv inner join  "
		    			+ "       pi_master pi on pi.pk_pi = pv.pk_pi  "
		    			+ " where pv.eu_status<>'9'";
//	        	sqler = " select pv.* from pv_er pver inner join "
//		    			+ "       pv_encounter pv on pv.pk_pv = pvop.pk_pv inner join  "
//		    			+ "       pi_master pi on pi.pk_pi = pv.pk_pi  "
//		    			+ " where pv.eu_status<>'9'";
	        	timeStr=" and pvop.op_times = ?";
	        	codeOpIpStr=" and pi.code_op = ?";
	    	}else if(visit_flag.equals("2")){
	    		//??????
	    		sql = " select pv.* from pv_er pver inner join "
		    			+ "       pv_encounter pv on pv.pk_pv = pvop.pk_pv inner join  "
		    			+ "       pi_master pi on pi.pk_pi = pv.pk_pi  "
		    			+ " where pv.eu_status<>'9'";
	    		
	    	}else if(visit_flag.equals("4")) {
	    		//??????
	    		sql = " select pv.* from pv_pe pvpe inner join "
		    			+ "       pv_encounter pv on pv.pk_pv = pvpe.pk_pv inner join  "
		    			+ "       pi_master pi on pi.pk_pi = pv.pk_pi  "
		    			+ " where pv.eu_status<>'9'";
	    	}
			List<Object> args = new ArrayList<Object>();
			if(!CommonUtils.isEmptyString(patientId)){
				sql+=" and pi.code_pi = ?";
				args.add(patientId);
			}
			if(times!=0){
				sql+=timeStr;
				args.add(times);
			}

			if(!CommonUtils.isEmptyString(codeOpIp)){
				sql+=codeOpIpStr;
				args.add(codeOpIp);
			}
			
			if(!CommonUtils.isEmptyString(codePv)&&codePv.indexOf("_")<0&&codePv.indexOf("-")<0&&codePv.indexOf("|")<0){
				sql+=" and pv.code_pv = ?";
				args.add(codePv);
			}
			if(args.size()==0) return null;
			
	    	List<PvEncounter> pvlist = DataBaseHelper.queryForList(sql, PvEncounter.class, args.toArray());
	    	if(pvlist!=null && pvlist.size()>0){
	    		pvvo = pvlist.get(0);
//	    	}else if(pvlist==null || pvlist.size()==0&&!CommonUtils.isEmptyString(codePv)) {
//	    		sql = "selct * from pv_encounter where code_pv=?";
//	    		pvlist = DataBaseHelper.queryForList(sql, PvEncounter.class, codePv);
//	    		if(pvlist!=null && pvlist.size()>0){
//		    		pvvo = pvlist.get(0);
//		    	}
	    	}
			return pvvo;
		}
		
		public PiMaster getPiByConds(String codeOpIp,String visit_flag) {
	    	if(visit_flag==null) return null;
	    	PiMaster pi = null;
	    	String codeOpIpStr=" and pi.code_ip = ?";
        	String sql = " select pi.*"
        			+  " from pi_master pi"
	    			+ " where pi.del_flag='0' ";
	    	//1??????2??????3??????4??????
	    	if(visit_flag.equals("1")||visit_flag.equals("2")||visit_flag.equals("4")) {
	    		//??????/??????/??????
	        	codeOpIpStr=" and pi.code_op = ?";
	    	}
			List<Object> args = new ArrayList<Object>();
			if(!CommonUtils.isEmptyString(codeOpIp)){
				sql+=codeOpIpStr;
				args.add(codeOpIp);
			}
			
			if(args.size()==0) return null;
			
	    	List<PiMaster> pilist = DataBaseHelper.queryForList(sql, PiMaster.class, args.toArray());
	    	if(pilist!=null && pilist.size()>0){
	    		pi = pilist.get(0);
	    	}
			return pi;
		}
		
		/**
		 * ????????????????????????????????????
		 * @param patientId
		 * @param times
		 * @param codeOpIp
		 * @param codePv
		 * @param visittype
		 * @param name
		 * @return
		 */
		public PvArchive getPvArchiveByConds(String patientId, int times,String codeOpIp,String codePv, String visittype,String name) {
	    	if(visittype==null) return null;
	    	PvArchive pvArchive = null;
	    	String sql = " select pv.* from pv_archive pv where pv.flag_del='0'";
	    	
			List<Object> args = new ArrayList<Object>();
			if(!CommonUtils.isEmptyString(patientId)){
				sql+=" and pv.pid = ?";
				args.add(patientId);
			}
			if(times!=0){
				sql+=" and pv.times = ?";
				args.add(times);
			}

			if(!CommonUtils.isEmptyString(codeOpIp)){
				sql+=" and pv.paticode = ?";
				args.add(codeOpIp);
			}
			
			if(!CommonUtils.isEmptyString(visittype)){
				sql+=" and pv.visittype = ?";
				args.add(visittype);
			}
			
			if(!CommonUtils.isEmptyString(name)){
				sql+=" and pv.patiname = ?";
				args.add(name);
			}
			
			if(!CommonUtils.isEmptyString(codePv)){
				sql+=" and pv.visitcode = ?";
				args.add(codePv);
			}
			
			if(args.size()==0) return null;
			
	    	List<PvArchive> pvlist = DataBaseHelper.queryForList(sql, PvArchive.class, args.toArray());
	    	if(pvlist!=null && pvlist.size()>0){
	    		pvArchive = pvlist.get(0);
	    	}
			return pvArchive;
		}
		
		/**
		 * ??????pv_arch_doc??????--??????
		 * @param headInfo
		 * @param pavo
		 * @return
		 */
		public PvArchDoc getArchDoc(UploadHead headInfo,PvArchive pavo) {
			   PvArchDoc doc = new PvArchDoc();
				    doc = new PvArchDoc();
					doc.setPkOrg(pavo.getPkOrg());
					doc.setPkArchive(pavo.getPkArchive());
					doc.setNameDoc(headInfo.getDocname());
					doc.setPkDoctype(headInfo.getDoctype());
					doc.setFlagUpload("0");
					doc.setFlagValid("0");
					doc.setCodeRpt(headInfo.getRid());
					doc.setNameRpt(headInfo.getMemo());
					doc.setCreateTime(new Date());
					doc.setModifyTime(new Date());
					doc.setIsArch(headInfo.getIsarch());
					doc.setFlagDel("0");
					if(!StringUtils.isEmpty(headInfo.getDate())) {
						String dateStr=headInfo.getDate();
						dateStr=dateStr.substring(0, 19);
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							doc.setDateDoc(sdf.parse(dateStr));
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
					String existSql="select * from pv_arch_doc where name_doc=?";
					PvArchDoc archDoc=DataBaseHelper.queryForBean(existSql, PvArchDoc.class, headInfo.getDocname());
					if(archDoc!=null){
						DataBaseHelper.deleteBeanByPk(archDoc);
					}
					DataBaseHelper.insertBean(doc);			
					return  DataBaseHelper.queryForBean("select * from pv_arch_doc where pk_doc =?", PvArchDoc.class, doc.getPkDoc());
		}
		
}
