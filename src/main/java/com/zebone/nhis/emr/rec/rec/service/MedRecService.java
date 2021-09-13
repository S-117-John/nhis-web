package com.zebone.nhis.emr.rec.rec.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.emr.rec.rec.*;

import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.emr.rec.rec.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.arch.BdArchSrvconf;import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.module.base.transcode.SysApplog;
import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.module.emr.oth.CnDiag;
import com.zebone.nhis.common.module.emr.oth.CnDiagDt;
import com.zebone.nhis.common.module.emr.oth.PvDiag;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDoctor;
import com.zebone.nhis.common.module.labor.nis.PvLaborRecDt;import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.MessageService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.emr.comm.service.EmrCommService;
import com.zebone.nhis.emr.common.EmrConstants;
import com.zebone.nhis.emr.common.EmrFtpUtils;
import com.zebone.nhis.emr.common.EmrSaveUtils;
import com.zebone.nhis.emr.common.EmrUtils;
import com.zebone.nhis.emr.qc.service.EmrQualCtrlService;
import com.zebone.nhis.emr.rec.rec.dao.EmrRecMapper;
import com.zebone.nhis.emr.rec.rec.handler.ReqRsltQryHandler;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

/**
 * 病历书写-病历书写
 * 
 * @author chengjia
 *
 */
@Service
public class MedRecService {

	@Resource
	private EmrRecMapper recMapper;
	@Resource
	private MessageService msgService;
	@Resource
	private EmrQualCtrlService qcService;
	@Resource
	private ReqRsltQryHandler reqRsltQryHandler;
	@Resource
	private EmrCommService commService;
	
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(MedRecService.class);
	
	/**
	 * 查询病历文档记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public List<EmrMedRec> queryMedRecList(String param, IUser user) throws UnsupportedEncodingException {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.get("pkPv").toString();
		String euType=null;
		List<EmrMedRec> rtnList =new ArrayList<EmrMedRec>();
		if(map.containsKey("euType")){
			euType= map.get("euType").toString();
			rtnList=recMapper.queryMedRecListReview(pkPv,euType);
		}else if(map.containsKey("flagRecoverDoc")){//病历恢复功能查询所有被删除的病历
			rtnList=recMapper.queryMedRecListByFlagRecover(pkPv);
		}else{
			rtnList=recMapper.queryMedRecList(pkPv);
		}
		return rtnList;
	}
	/**
	 * 查询当前病人所有病历数据
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrMedRec> queryPatMedRecList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.get("pkPv").toString();
		List<EmrMedRec> rtnList =new ArrayList<EmrMedRec>();
		rtnList = recMapper.queryPatMedRecList(pkPv);
		return rtnList;
	}

	/**
	 * 查询病历文档记录(模板/分类)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrMedRec> queryMedRecListAll(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.get("pkPv").toString();
		List<EmrMedRec> rtnList = recMapper.queryMedRecListAll(pkPv);
		return rtnList;
	}

	/**
	 * 查询病历文档内容
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrMedDoc queryEmrMedDocById(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkDoc = map.get("pkDoc").toString();
		String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();
		
		EmrMedDoc doc=null;
//		if(dbName!=null&&!dbName.equals("")){
//			doc = recMapper.queryEmrMedDocByIdEmr(pkDoc,dbName);
//			if(doc==null) doc = recMapper.queryEmrMedDocById(pkDoc);
//		}else{
//			doc = recMapper.queryEmrMedDocById(pkDoc);
//		}
		if(saveDataMode.equals("0")){//本地存储
			doc = recMapper.queryEmrMedDocById(pkDoc);
		}else if(saveDataMode.equals("1")){//独立存储/共同存储
			doc = recMapper.queryEmrMedDocByIdEmr(pkDoc,dbName);
			if(doc==null) doc = recMapper.queryEmrMedDocById(pkDoc);
		}
		return doc;
	}

	/**
	 * 根据主键查询病历文档记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrMedRec queryEmrMedRecById(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkRec = map.get("pkRec").toString();
		EmrMedRec rec = recMapper.queryEmrMedRecById(pkRec);
		return rec;
	}

	/**
	 * 保存病历文档记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrMedRec saveEmrMedRec(String param, IUser user) {
		EmrMedRec medRec = JsonUtil.readValue(param, EmrMedRec.class);
		String status = "";
		int rtn = 0;
		int row = 0;
		if (StringUtils.isNoneEmpty(medRec.getStatus()))
			status = medRec.getStatus();		
		String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();

		boolean genAuditRec = false;
		if (status.equals(EmrConstants.STATUS_NEW)) {
			rtn = recMapper.saveEmrMedRec(medRec);
		} else if (status.equals(EmrConstants.STATUS_UPD)) {
			rtn = recMapper.updateEmrMedRec(medRec);
			String operateType = medRec.getOperateType();
			if (operateType == null)
				operateType = "*";
			String strs = "submit,audit,recall,return";
			if (strs.indexOf(operateType) >= 0) {
				genAuditRec = true;
			}
		} else if (status.equals(EmrConstants.STATUS_DEL)) {
			rtn = recMapper.deleteEmrMedRec(medRec.getPkRec());
		}
		EmrMedDoc medDoc = medRec.getMedDoc();
		status = "";
			if (StringUtils.isNoneEmpty(medDoc.getStatus()));
		
			status = medDoc.getStatus();
		
		byte[] expData=null;
		byte[] saveData=null;
		if(status.equals(EmrConstants.STATUS_NEW)||status.equals(EmrConstants.STATUS_UPD)){
			expData = medDoc.getDocExpData();
		}
		medDoc.setDbName(dbName);
		saveData=medDoc.getDocData();
		if (status.equals(EmrConstants.STATUS_NEW)) {
			//rtn = recMapper.saveEmrMedDoc(medDoc);
			if(saveDataMode.equals("0")){//本地存储
				recMapper.saveEmrMedDoc(medDoc);
			}else if(saveDataMode.equals("1")){//独立存储
				recMapper.saveEmrMedDocEmr(medDoc);
				medDoc.setDocData(null);
				medDoc.setDocDataBak(null);
				medDoc.setDocExpData(null);
				recMapper.saveEmrMedDoc(medDoc);
			}
		} else if (status.equals(EmrConstants.STATUS_UPD)) {
			//rtn = recMapper.updateEmrMedDoc(medDoc);
			if(saveDataMode.equals("0")){//本地存储
				recMapper.updateEmrMedDoc(medDoc);
			}else if(saveDataMode.equals("1")){//独立存储
				row = recMapper.updateEmrMedDocEmr(medDoc);
				if(row<=0){
					//兼容并行期/存储库无记录则插入
					recMapper.saveEmrMedDocEmr(medDoc);
				}
				medDoc.setDocData(null);
				medDoc.setDocDataBak(null);
				medDoc.setDocExpData(null);
				recMapper.updateEmrMedDoc(medDoc);
			}
		} else if (status.equals(EmrConstants.STATUS_DEL)) {
			//rtn = recMapper.deleteEmrMedDoc(medDoc.getPkDoc());
			if(saveDataMode.equals("0")){//本地存储
				recMapper.deleteEmrMedDoc(medDoc.getPkDoc());
			}else if(saveDataMode.equals("1")){//独立存储
				recMapper.deleteEmrMedDocEmr(medDoc.getPkDoc(),dbName);
				recMapper.deleteEmrMedDoc(medDoc.getPkDoc());
			}
		}

		if(medRec.getDelFlag()!=null&&medRec.getDelFlag().equals("1")&&medRec.getDocType()!=null&&medRec.getDocType().getFlagHomePage()!=null&&medRec.getDocType().getFlagHomePage().equals("1")){
			//应付出现的文档删除、但没有删除首页的临时特殊处理
			DataBaseHelper.execute("update emr_home_page set del_flag='1' where pk_pv = ? and del_flag='0'",new Object[] {medRec.getPkPv()});
		}
		/*
		 * String[] scopes={medRec.getPkWard()};
		 * 
		 * //SysMessage msg=MessageUtils.createSysMessage("保存病历",
		 * medRec.getName(), "1", "1", 3, MessageUtils.CODE_OPER_NS_ORDER_CHECK,
		 * "参数", null,scopes); SysMessage
		 * msg=MessageUtils.createSysMessageOrd("新医嘱", medRec.getName(),
		 * medRec.getPkPv(),medRec.getPkWard()); msgService.saveMessage(msg);
		 */
		medDoc.setDocData(saveData);
		medDoc.setDocExpData(expData);
		
		//自动创建的病案首页第一次不产生图片
		String autoGenHomePage = ApplicationUtils.getSysparam("AllowCreateMedicalRecordIndex", true);
		if(autoGenHomePage!=null&&autoGenHomePage.equals("1")){
			if(status.equals(EmrConstants.STATUS_NEW)&&medRec.getTypeCode()!=null&&medRec.getTypeCode().indexOf("0800")==0) return medRec;
		}
		
		//保存病历图片
		String path=saveEmrImageData(status, medDoc,medRec.getPkPv());
		if(path!=null&&!path.equals("")){
			if(path.indexOf(medRec.getPkPv())<0){
				path=path + medRec.getPkPv()+"/";
			}
			medDoc.setFilePath(path);
			DataBaseHelper.update("update emr_med_doc set file_path=? where pk_doc=?", new Object[]{path,medDoc.getPkDoc()});
		}
		String filePath=medDoc.getFilePath();
		if(filePath!=null&&!filePath.equals("")){
			//2019-08-19 存在无/emr/doc/数据
			String ftpPath=ApplicationUtils.getPropertyValue("emr.ftp.path", "emr");
			String fileSaveMode=ApplicationUtils.getPropertyValue("emr.fileSaveMode", "loc");
			if(fileSaveMode!=null&&fileSaveMode.equals("ftp")&&filePath!=null&&ftpPath!=null&&filePath.indexOf(ftpPath)<0){
				if(path==null||path.equals("")) path=filePath;
				filePath=ftpPath+path+"/";
				medDoc.setFilePath(filePath);
				DataBaseHelper.update("update emr_med_doc set file_path=? where pk_doc=?", new Object[]{filePath,medDoc.getPkDoc()});
			}else if(fileSaveMode!=null&&fileSaveMode.equals("ftp")&&filePath!=null&&ftpPath!=null&&filePath.indexOf(medRec.getPkPv())<0){
				//2020-07-28 存在无pk_pv数据:5d037ba9e50a4585af220db9cfaa9e1c	/emr/doc//
				filePath=filePath.replace("//", "/"+medRec.getPkPv());
				filePath=filePath+"/";
				medDoc.setFilePath(filePath);
				DataBaseHelper.update("update emr_med_doc set file_path=? where pk_doc=?", new Object[]{filePath,medDoc.getPkDoc()});
			}
			
		}
		User u = (User) user;
		// 提交/产生审签任务
		if (genAuditRec)
			qcService.saveEmrAuditRec(medRec, medDoc, u.getPkEmp());
		
		//保存病历文本数据
		saveEmrExpData(medRec, medDoc,status);
		
		//保存扩展存储信息
		List<Map<String,Object>> extSaveList=medRec.getExtSaveList();
		if(extSaveList!=null&&extSaveList.size()>0){
			for(int j=0;j<extSaveList.size();j++){
				Map<String,Object> map = extSaveList.get(j);
				map.put("status", medRec.getStatus());
				map.put("delFlag", medRec.getDelFlag());
			}
			commService.saveEmrExtData(extSaveList,user);
		}


		return medRec;
	}
	
	/**
	 * 保存病历文本数据
	 * @param medRec
	 * @param medDoc
	 */
	private void saveEmrExpData(EmrMedRec medRec, EmrMedDoc medDoc,String status) {
		//保存病历文档的同时，将特定文档特定段落的内容存储到表中
		String strs=ApplicationUtils.getPropertyValue("emr.save.data.paras", "");
		if(strs!=null&&!strs.equals("")){
			Date now = new Date();
			//先删除、再插入
			DataBaseHelper.execute("delete from emr_txt_data where pk_pv = ? and  pk_rec = ? ",new Object[] {medRec.getPkPv(),medRec.getPkRec()});
			
			if ((medRec.getDelFlag()==null||medRec.getDelFlag().equals("0"))&&(status.equals(EmrConstants.STATUS_NEW) || status.equals(EmrConstants.STATUS_UPD))) {
				
				//存储整份病历文本
				saveEmrTxtData(medRec, now, "00","00", EmrUtils.getDocTxt(medDoc.getDocXml()),null);
				
				List<String> listType = java.util.Arrays.asList(strs.split(";"));
				if(listType!=null&&listType.size()>0){
					for(int i=0;i<listType.size();i++){
						String str=listType.get(i);
						if(str==null||str.equals("")) continue;
						List<String> listDoc = java.util.Arrays.asList(str.split(":"));
						if(listDoc!=null&&listDoc.size()==3){
							String docType=listDoc.get(0);
							if(docType==null||docType.equals("")) continue;
							if(!docType.equals(medRec.getTypeCode())) continue;
							//病程记录
							String qryType=listDoc.get(1);
							if(qryType==null||qryType.equals("")) continue;
							if("012".indexOf(qryType)<=0) continue;
							
							String strDoc=listDoc.get(2);
							if(strDoc==null||strDoc.equals("")) continue;
							List<String> listPara = java.util.Arrays.asList(strDoc.split(","));
							if(listPara!=null&&listPara.size()>0){
								for(int j=0;j<listPara.size();j++){
									String strCode=listPara.get(j);
									if(strCode==null||strCode.equals("")) continue;
									List<String> listDe = java.util.Arrays.asList(strCode.split("-"));
									String paraCode="";
									if(listDe!=null&&listDe.size()==2){
										paraCode=listDe.get(0);
										strCode=listDe.get(1);
									}
									String[] docTxts=EmrUtils.getPatEmrParaText(medRec, medDoc,medRec.getTypeCode(), strCode,qryType);
									String textCode=null;
									if(docTxts!=null&&docTxts.length>0&&docTxts[0]!=null&&!docTxts[0].equals("")){
										if(docTxts.length>3) textCode=docTxts[3];
										saveEmrTxtData(medRec, now, strCode.replace("de", ""),paraCode, docTxts[0],textCode);	
									}
									//名称对应的编码字段
									if(docTxts!=null&&docTxts.length>2&&docTxts[1]!=null&&!docTxts[1].equals("")){
										if(docTxts[2]!=null&&!docTxts[2].equals("")){
											saveEmrTxtData(medRec, now, strCode.replace("de", ""),docTxts[1], docTxts[2],null);	
										}
									}
									
								}
							}

						}
						
					}
				}
				
				//保存专科护理记录单
				if(medRec!=null&&medDoc!=null&&medDoc.getDocXml()!=null&&medRec.getTypeCode()!=null&&medRec.getTypeCode().equals("060501")) {
					saveNrDocTxt(medRec,medDoc.getDocXml());
				}
			}
			
		}
	}
	
	/**
	 * 保存病历图片
	 * @param status
	 * @param medDoc
	 * @return
	 */
	private String saveEmrImageData(String status, EmrMedDoc medDoc,String pkPv) {
		String pathSave="";
		String fileSaveMode = ApplicationUtils.getPropertyValue("emr.fileSaveMode", "loc");
		//loc/ftp
		if(fileSaveMode==null||fileSaveMode.equals("")) fileSaveMode="loc";
		logger.info("------------saveEmrImageData:"+fileSaveMode);
		// 新增和修改文档保存pdf、pic或html
		if (status.equals(EmrConstants.STATUS_NEW) || status.equals(EmrConstants.STATUS_UPD)) {
			try {
				byte[] expData = medDoc.getDocExpData();
				if (expData != null && expData.length > 0) {
					String fileName = medDoc.getFileName();
					String filePath = medDoc.getFilePath();
					String fileType = medDoc.getFileType();
					String path = "";
					String lastStr = "";
					//String pathSave="";
					if(fileSaveMode.equals("loc")){
						String rootStr = ApplicationUtils.getPropertyValue("emr.filePath", "");
						if (rootStr != null && !rootStr.equals("")) {
							// 判断文件夹是否存在/不存在添加
							EmrUtils.checkDirExists(rootStr);

							lastStr = rootStr.substring(rootStr.length() - 1, rootStr.length());
							if (lastStr != null && !lastStr.equals("\\")) {
								rootStr = rootStr + "\\";
							}
							if (filePath != null && !filePath.equals("")) {
								lastStr = filePath.substring(filePath.length() - 1, filePath.length());
								if (lastStr != null && !lastStr.equals("\\")) {
									filePath = filePath + "\\";
								}
							}
							if (fileType == null || fileType.equals(""))
								fileType = "pdf";
							// 判断文件夹是否存在/不存在创建
							EmrUtils.checkDirExists(rootStr + filePath);

							path = rootStr + filePath + fileName + "." + fileType;
							// path=rootStr+NHISUUID.getKeyId()+".pdf";
							// 判断文件是否存在/存在删除
							// EmrUtils.removeExistFiles(path);
							// 生成图片（覆盖）
							EmrUtils.byte2image(expData, path);
							pathSave=path;
						}
					}else if (fileSaveMode.equals("ftp")){
						path=filePath;
						if (fileType == null || fileType.equals("")) fileType = "pdf";
						fileName=fileName+"."+fileType;
						pathSave = EmrFtpUtils.fileUpload(path, fileName, expData);
						//2019-05-17 发现一些不能重现的数据//暂时先处理一下问题数据
						///emr/doc//emr/doc/80be86ad244040b6973236a23467c32b//
						//613656d8006248b08a05509151849bdd
						if(pathSave!=null){
							String ftpPath=ApplicationUtils.getPropertyValue("emr.ftp.path", "emr");///emr/doc/
							if(pathSave.indexOf(ftpPath)>=0){
								if(pathSave.indexOf("/emr/doc//emr/doc/")>=0){
									pathSave=pathSave.replace("/emr/doc//emr/doc/", "/emr/doc/");
								}
							}else{
								pathSave=ftpPath+path+"/";
							}
							
						}
					}
					
					//DataBaseHelper.update("update emr_med_doc set file_path=? where pk_doc=?", new Object[]{pathSave,medDoc.getPkDoc()});
				}
			} catch (Exception e) {
				// TODO: handle exception
				saveErrLogs(pkPv,medDoc.getPkDoc(),e);
				e.printStackTrace();
			}

		}else if(status.equals(EmrConstants.STATUS_DEL)){
			if(fileSaveMode.equals("app")){
				
			}else if (fileSaveMode.equals("ftp")){
				
			}
		}
		return pathSave;
	}
	
	private void saveErrLogs(String pkPv,String pkRec,Exception e){
		EmrOperateLogs emrOpeLogs = new EmrOperateLogs();
		emrOpeLogs.setCode("save_image");
		emrOpeLogs.setName("保存图片");
		emrOpeLogs.setDelFlag("0");
		emrOpeLogs.setEuStatus("0");
		emrOpeLogs.setPkOrg(UserContext.getUser().getPkOrg());
		emrOpeLogs.setPkPv(pkPv);
		emrOpeLogs.setPkRec(pkRec);
		emrOpeLogs.setPkPi(null);
		emrOpeLogs.setTimes(null);
		if(e.getMessage().length()>1000){
			emrOpeLogs.setOperateTxt(e.getMessage().substring(0, 1000));
		}else{
			emrOpeLogs.setOperateTxt(e.getMessage());
		}
		
		DataBaseHelper.insertBean(emrOpeLogs);
	}
	private void saveEmrTxtData(EmrMedRec medRec, Date now, String strCode,
			String paraCode, String docTxt,String remark) {
		EmrTxtData txt = new EmrTxtData();
		
		txt.setPkPv(medRec.getPkPv());
		txt.setPkDoc(medRec.getPkDoc());
		txt.setPkRec(medRec.getPkRec());
		txt.setTypeCode(medRec.getTypeCode());
		txt.setParaCode(paraCode.equals("")?strCode:paraCode);
		txt.setDocTxt(docTxt);
		if(remark!=null && !remark.equals("")){
			if(remark.length()>450){remark=remark.substring(0, 450);}
		}
		txt.setRemark(remark);
		txt.setDelFlag("0");
		txt.setCreator(medRec.getCreator());
		txt.setCreateTime(now);
		txt.setTs(now);
		
		DataBaseHelper.insertBean(txt);
	}

	/**
	 * 病历通用查询
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<HashMap<String, String>> queryDictList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sqlText = map.get("sqlText").toString();
		List<HashMap<String, String>> listMap = recMapper.queryDictList(sqlText);
		return listMap;
	}

	/**
	 * 查询病历患者列表
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrPatList> queryPatListByDept(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrPatList> rtnList = recMapper.queryPatListByDept(map);
		return rtnList;
	}
	
	/**
	 * 根据Pkpv查询病历患者列表
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrPatList queryPatListByPkpv(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		EmrPatList rtnList = recMapper.queryPatListByPkpv(map);
		return rtnList;
	}

	/**
	 * 查询病历患者列表(DC)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrPatList> queryPatListByDeptDC(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		String Status = (String) map.get("orderByStatus");
		if (Status != null && Status.length() != 0) {
			if (Status.equals("bedNo")) {
				map.put("orderByStatus", " order by bed_no,pat_no,name,pk_pi");
			} else if (Status.equals("dateEnd")) {
				map.put("orderByStatus", " order by date_end,pat_no,name,pk_pi");
			} else {
				Status = null;
			}
		}
		List<EmrPatList> rtnList = recMapper.queryPatListByDeptDc(map);
		return rtnList;
	}

	public List<EmrPatListReview> queryPatListByDeptDCReview(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		String Status = (String) map.get("orderByStatus");
		if (Status != null && Status.length() != 0) {
			if (Status.equals("bedNo")) {
				map.put("orderByStatus", " order by bed_no,pat_no,name,pk_pi");
			} else if (Status.equals("dateEnd")) {
				map.put("orderByStatus", " order by date_end,pat_no,name,pk_pi");
			} else {
				Status = null;
			}
		}
		List<EmrPatListReview> rtnList = recMapper.queryPatListByDeptDcReview(map);
		return rtnList;
	}

	/**
	 * 查询病历患者列表-审签模式
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrPatList> queryPatListAudit(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrPatList> rtnList = new ArrayList<EmrPatList>();
		if (Application.isSqlServer()) {
			rtnList = recMapper.queryPatListAudit(map);
		} else {
			rtnList = recMapper.queryPatListAuditOracle(map);
		}

		return rtnList;
	}

	/**
	 * 根据条件查询患者病历记录
	 * 
	 * @param pkPatrec/pkPv
	 * @param user
	 * @return
	 */
	public EmrPatRec queryEmrPatRecByConds(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		EmrPatRec rec = null;
		List<EmrPatRec> list = recMapper.queryEmrPatRecByConds(map);
		if (list != null && list.size() > 0) {
			rec = list.get(0);
		}
		return rec;
	}

	/**
	 * 根据条件查询患者就诊记录
	 * 
	 * @param pkPv/orderBy
	 * @param user
	 * @return
	 */
	public List<EmrPatList> queryPatListByConds(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrPatList> list = recMapper.queryPatListByConds(map);
		return list;
	}

	/**
	 * 保存患者病历记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrPatRec saveEmrPatRec(String param, IUser user) {
		EmrPatRec rec = JsonUtil.readValue(param, EmrPatRec.class);
		String status = "";
		int rtn = 0;
		if (StringUtils.isNoneEmpty(rec.getStatus()))
			status = rec.getStatus();

		if (status.equals(EmrConstants.STATUS_NEW)) {
			EmrPatRec recIsNull = DataBaseHelper.queryForBean("select * from EMR_PAT_REC where pk_pv = ?", EmrPatRec.class, rec.getPkPv());
			if(recIsNull==null)
			{
				rtn = recMapper.saveEmrPatRec(rec);
			}else{
				rec.setPkPatrec(recIsNull.getPkPatrec());
				rtn = recMapper.updateEmrPatRec(rec);
			}
		} else if (status.equals(EmrConstants.STATUS_UPD)) {
			rtn = recMapper.updateEmrPatRec(rec);
		} else if (status.equals(EmrConstants.STATUS_DEL)) {
			rtn = recMapper.deleteEmrPatRec(rec.getPkPatrec());
		}
		return rec;
	}

	/**
	 * 根据编码查询病历人员信息
	 * 
	 * @param codes
	 * @return
	 */
	public List<EmrEmpList> queryEmpListByPks(String param, IUser user) {
		List<String> pks = JsonUtil.readValue(param, List.class);

		List<EmrEmpList> list = recMapper.queryEmpListByPks(pks);
		return list;
	}

	/**
	 * 病历通用更新
	 * 
	 * @param sqlText
	 * @return
	 */
	public int updateDictData(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sqlText = map.get("sqlText").toString();
		int ret = recMapper.updateDictData(sqlText);
		return ret;

	}

	/**
	 * 病历通用更新DC
	 * 
	 * @param sqlText
	 * @return
	 */
	public int updateDictDataDC(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sqlText = null;
		String Status = (String) map.get("status");
		String PkPv = (String) map.get("pkPv");
		String euGradeType = map.get("euGradeType")==null?"dept":map.get("euGradeType").toString();
		String val = ApplicationUtils.getSysparam("IsDirectWriteByDeptCon", true);
		if(euGradeType==null||euGradeType.equals("")) euGradeType="dept";
		if (Status != null && Status.length() != 0 && PkPv != null && PkPv.length() != 0) {
			if(!Status.equals("case3")){
				EmrPatRec patRec = DataBaseHelper.queryForBean("select * from EMR_PAT_REC where pk_pv = ?", EmrPatRec.class, PkPv);
				if(patRec!=null&&patRec.getFlagReceive()!=null&&patRec.getFlagReceive().equals("1")&&!patRec.getEuStatus().equals("3")){
					throw new BusException("病案已签收，不能取消质控，要通过病历召回功能才能修改病历！");
				}
			}
			//取消质控(科室)/直接提交
			if (Status.equals("case1")) {
				if(val!=null && val.equals("1"))
				{
					sqlText = "update emr_pat_rec set eu_status='1',pk_emp_qc=null,qc_date=null,pk_emp_submit=null,submit_date=null,eu_dept_qc_grade=null,dept_qc_score=null,pk_emp_dept_qc=null,dept_qc_date=null,pk_emp_finish=null,finish_date=null where pk_pv='"
						+ PkPv + "'";
				}else{
					sqlText = "update emr_pat_rec set eu_status='2',pk_emp_qc=null,qc_date=null,pk_emp_submit=null,submit_date=null,eu_dept_qc_grade=null,dept_qc_score=null,pk_emp_dept_qc=null,dept_qc_date=null where pk_pv='"
						+ PkPv + "'";
				}
			} else if (Status.equals("case2")) {
				//完成、提交分开
				//判断系统参数是否取消质控后直接变为书写状态
				if(val!=null && val.equals("1"))
				{
					sqlText = "update emr_pat_rec set eu_status='1',pk_emp_qc=null,qc_date=null,eu_dept_qc_grade=null,dept_qc_score=null,pk_emp_dept_qc=null,dept_qc_date=null,pk_emp_finish=null,finish_date=null where pk_pv='" + PkPv + "'";
				}else{
					sqlText = "update emr_pat_rec set eu_status='2',pk_emp_qc=null,qc_date=null,eu_dept_qc_grade=null,dept_qc_score=null,pk_emp_dept_qc=null,dept_qc_date=null where pk_pv='" + PkPv + "'";
				}
			} else if (Status.equals("case3")) {
				//终末质控/取消质控
				sqlText = "update emr_pat_rec set eu_status='4',pk_emp_final_qc=null,final_qc_date=null,pk_emp_final_qc_submit=null,final_qc_submit_date=null,flag_final_qc=null where pk_pv='" + PkPv + "'";
			} else {
				Status = null;
			}
		}
		int ret = recMapper.updateDictDataDC(sqlText);
		
		String cancelQuaDelScore = ApplicationUtils.getSysparam("CancelQuaDelScore", true);
		String quaCtrlSubmitDirect = ApplicationUtils.getSysparam("QuaCtrlSubmitDirect", true);
		if(cancelQuaDelScore!=null&&cancelQuaDelScore.equals("1")){
			sqlText = "update emr_grade_item set del_flag='1' where pk_graderec in (select pk_graderec from emr_grade_rec where pk_pv=? and eu_grade_type='"+euGradeType+"')";
			DataBaseHelper.update(sqlText, new Object[]{PkPv});
			sqlText = "update emr_grade_rec set del_flag='1' where pk_pv=? and eu_grade_type='"+euGradeType+"'";
			DataBaseHelper.update(sqlText, new Object[]{PkPv});
		}
		return ret;

	}

	/**
	 * 查询病历诊断记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrPatDiags> queryPatDiagsList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		// String pkPv=map.get("pkPv").toString();
		List<EmrPatDiags> rtnList = recMapper.queryPatDiagsList(map);
		return rtnList;
	}

	/**
	 * 保存病历诊断记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveEmrPatDiags(String param, IUser user) {
		List<EmrPatDiags> diagList = JsonUtil.readValue(param, new TypeReference<List<EmrPatDiags>>() {
		});
		if (diagList == null || diagList.size() == 0)
			return;

		String status = "";
		int rtn = 0;
		EmrPatDiags patDiags = null;
		for (int i = 0; i < diagList.size(); i++) {
			patDiags = (EmrPatDiags) diagList.get(i);
			status = "";
			if (StringUtils.isNoneEmpty(patDiags.getStatus()))
				status = patDiags.getStatus();

			if (status.equals(EmrConstants.STATUS_NEW)) {
				rtn = recMapper.saveEmrPatDiags(patDiags);
			} else if (status.equals(EmrConstants.STATUS_UPD)) {
				rtn = recMapper.updateEmrPatDiags(patDiags);
			} else if (status.equals(EmrConstants.STATUS_DEL)) {
				rtn = recMapper.deleteEmrPatDiags(patDiags.getPkDiag());
			}
		}

	}

	/**
	 * 查询医师常用病历诊断
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrPatDiags> queryEmpDiagsList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkEmp = map.get("pkEmp").toString();
		List<EmrPatDiags> rtnList = recMapper.queryEmpDiagsList(pkEmp);
		return rtnList;
	}

	/**
	 * 查询患者医嘱记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrOrdList> queryPatOrdList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.get("pkPv").toString();
		List<EmrOrdList> rtnList = recMapper.queryPatOrdList(pkPv);
		
		return rtnList;
	}

	/**
	 * 查询患者检验记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrLisResult> queryPatLisResult(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.containsKey("pkPv")&&map.get("pkPv")!=null?map.get("pkPv").toString():null;
		String pkPi = map.containsKey("pkPi")&&map.get("pkPi")!=null?map.get("pkPi").toString():null;
		String beginDateStr=null;
		String endDateStr=null;
		if(map.get("beginDate")!=null){
			try {
				String strb=map.get("beginDate").toString();
				String stre=map.get("endDate").toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date tmp=sdf.parse(strb);
				Date tmp2=sdf.parse(stre);
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				beginDateStr=sdf.format(tmp);
				endDateStr=sdf.format(tmp2);
				map.put("beginDateStr", beginDateStr);
				map.put("endDateStr", endDateStr);
				//博爱查询的时间转换一下
				Calendar c = Calendar.getInstance(); 
				c.setTime(tmp2);
				c.add(Calendar.DAY_OF_MONTH,1); 
				Date endDate=c.getTime();
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				map.put("dateBeginBa", sdf.format(tmp));
				map.put("dateEndBa", sdf.format(endDate));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		List<EmrLisResult> rtnList = new ArrayList<EmrLisResult>();
		String hosCode=ApplicationUtils.getPropertyValue("emr.hos.code", "");
		String qryType=ApplicationUtils.getPropertyValue("emr.rpt.lis", "");
		if(StringUtils.isEmpty(qryType)) qryType="db";
		if(qryType.equalsIgnoreCase("eai")){
			//平台
////			map.put("qryType", "lis");
////			List<Map<String,Object>> listMap = PlatFormSendUtils.getEMRClinicalData(map);
////			if(listMap!=null&&listMap.size()>0){
////				EmrLisResult result=new EmrLisResult();
////				for(int i=0;i<listMap.size();i++){
////					Map<String,Object> rtnMap=listMap.get(i);
////					result=new EmrLisResult();
////					try {
////						result = ApplicationUtils.mapToBean(rtnMap, EmrLisResult.class);
////						rtnList.add(result);
////					} catch (Exception e) {
////						// TODO Auto-generated catch block
////						//e.printStackTrace();
////					}
////				}
//			}
			ApplicationUtils apputil = new ApplicationUtils();
			map.put("qryType", "lis");
			ResponseJson  rs =  apputil.execService("MA", "PlatFormSendUtils", "getEMRClinicalData",map,user);
			List<Map<String,Object>> listMap = (List<Map<String,Object>>)rs.getData();
			if(listMap!=null&&listMap.size()>0){
				EmrLisResult result=new EmrLisResult();
				for(int i=0;i<listMap.size();i++){
					Map<String,Object> rtnMap=listMap.get(i);
					result=new EmrLisResult();
					try {
						result = ApplicationUtils.mapToBean(rtnMap, EmrLisResult.class);
						rtnList.add(result);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			}
		}else{
			if(hosCode!=null&&hosCode.equals("syx")){
				rtnList = reqRsltQryHandler.queryPatLisResultSyx(map);
			}else if (hosCode!=null&&hosCode.equals("zsba")){
				String intfSwtch = ApplicationUtils.getPropertyValue("emr.shield.tpi.intf", "");
	            if(StringUtils.isEmpty(intfSwtch)) intfSwtch="0";
	            if(!intfSwtch.equals("1")) {
	            	rtnList = reqRsltQryHandler.queryPatLisResultboai(map);
				}
			}else if(hosCode!=null&&hosCode.equals("sdtj")){
				rtnList = recMapper.queryPatLisResultSdtj(pkPv,beginDateStr,endDateStr,pkPi);
			}
			else{
				rtnList = recMapper.queryPatLisResult(pkPv,beginDateStr,endDateStr,pkPi);
			}
		}
		
		return rtnList;
	}

	/**
	 * 查询患者检查记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrRisResult> queryPatRisResult(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String beginDateStr=null;
		String endDateStr=null;
		if(map.get("beginDate")!=null){
			try {
				String bstr=map.get("beginDate").toString();
				String estr=map.get("endDate").toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date tmp=sdf.parse(bstr);
				Date tmp2=sdf.parse(estr);
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				beginDateStr=sdf.format(tmp);
				endDateStr=sdf.format(tmp2);
				map.put("beginDateStr", beginDateStr);
				map.put("endDateStr", endDateStr);
				//博爱查询的时间转换一下
				Calendar c = Calendar.getInstance(); 
				c.setTime(tmp2);
				c.add(Calendar.DAY_OF_MONTH,1); 
				Date endDate=c.getTime();
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				map.put("dateBeginBa", sdf.format(tmp));
				map.put("dateEndBa", sdf.format(endDate));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		List<EmrRisResult> rtnList = new ArrayList<EmrRisResult>();
		
		String hosCode=ApplicationUtils.getPropertyValue("emr.hos.code", "");
		String qryType=ApplicationUtils.getPropertyValue("emr.rpt.ris", "");
		if(StringUtils.isEmpty(qryType)) qryType="db";
		if(qryType.equalsIgnoreCase("eai")){
//			//平台
//			map.put("qryType", "ris");
//			List<Map<String,Object>> listMap = PlatFormSendUtils.getEMRClinicalData(map);
//			if(listMap!=null&&listMap.size()>0){
//				EmrRisResult result=new EmrRisResult();
//				for(int i=0;i<listMap.size();i++){
//					Map<String,Object> rtnMap=listMap.get(i);
//					result=new EmrRisResult();
//					try {
//						result = ApplicationUtils.mapToBean(rtnMap, EmrRisResult.class);
//						rtnList.add(result);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						//e.printStackTrace();
//					}
//				}
//			}
			ApplicationUtils apputil = new ApplicationUtils();
			map.put("qryType", "ris");
			ResponseJson  rs =  apputil.execService("MA", "PlatFormSendUtils", "getEMRClinicalData",map,user);
			List<Map<String,Object>> listMap = (List<Map<String,Object>>)rs.getData();
			if(listMap!=null&&listMap.size()>0){
				EmrRisResult result=new EmrRisResult();
				for(int i=0;i<listMap.size();i++){
					Map<String,Object> rtnMap=listMap.get(i);
					result=new EmrRisResult();
					try {
						result = ApplicationUtils.mapToBean(rtnMap, EmrRisResult.class);
						rtnList.add(result);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			}
		}else{
			if(hosCode!=null&&hosCode.equals("syx")){
				rtnList = reqRsltQryHandler.queryPatRisResultSyx(map);
			}else if(hosCode!=null&&hosCode.equals("zsba")){
				String intfSwtch = ApplicationUtils.getPropertyValue("emr.shield.tpi.intf", "");
	            if(StringUtils.isEmpty(intfSwtch)) intfSwtch="0";
	            if(!intfSwtch.equals("1")) {
	            	rtnList = reqRsltQryHandler.queryPatRisResultBoai(map);
	            }
				
			}else{
				String pkPv = map.containsKey("pkPv")&&map.get("pkPv")!=null?map.get("pkPv").toString():null;
				String pkPi = map.containsKey("pkPi")&&map.get("pkPi")!=null?map.get("pkPi").toString():null;
				rtnList = recMapper.queryPatRisResult(pkPv,beginDateStr,endDateStr,pkPi);
			}
		}

		return rtnList;
	}

	/**
	 * 根据文档分类查询患者病历记录(包含文档内容)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrMedRec> queryMedRecListByType(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.get("pkPv").toString();
		String typeCode = map.get("typeCode").toString();
		List<EmrMedRec> rtnList =new ArrayList<EmrMedRec>();
		List<EmrMedRec> listData =new ArrayList<EmrMedRec>();
		String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();
		if(saveDataMode.equals("0")){//本地存储
			rtnList = recMapper.queryMedRecListByType(pkPv, typeCode);
		}else if(saveDataMode.equals("1")){//分离存储
			rtnList = recMapper.queryMedRecListByType(pkPv, typeCode);
			int i=0;
			int j=0;
			EmrMedRec rec=null;
			EmrMedDoc doc=null;
			List<String> pkDocs=new ArrayList<>();
			if(rtnList!=null&&rtnList.size()>0){
				for(i=0;i<rtnList.size();i++){
					rec=rtnList.get(i);
					pkDocs.add(rec.getPkDoc());
				}
				if(pkDocs!=null&&pkDocs.size()>0){
					List<EmrMedDoc> docs = recMapper.queryDocListEmrByPks(dbName, pkDocs);
					if(docs!=null&&docs.size()>0){
						for(i=0;i<docs.size();i++){
							doc=docs.get(i);
							if(doc!=null&&doc.getDocData()!=null){
								for(j=0;j<rtnList.size();j++){
									EmrMedRec recRtn=rtnList.get(j);
									if(recRtn.getPkDoc()==null) continue;
									if(doc.getPkDoc()!=null&&doc.getPkDoc().equals(recRtn.getPkDoc())){
										recRtn.setDocData(doc.getDocData());
										break;
									}
								}
								
							}
						}
					}
				}
			}
					
		}
			
		return rtnList;
	}

	/**
	 * 查询病历科室列表
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<EmrDeptList> queryEmrDeptList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim() + "%");
		List<EmrDeptList> rtnList = recMapper.queryEmrDeptList(map);
		return rtnList;
	}

	/**
	 * 根据条件查询患者病案首页记录
	 * 
	 * @param pkPv
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public EmrHomePage queryHomePageByPv(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		EmrHomePage page = recMapper.queryHomePageByPv(map);
		if (page == null)
			return null;
		String pkPage = page.getPkPage();
		map.put("pkPage", pkPage);
		List<EmrHomePageDiags> diags = recMapper.queryHomePageDiags(map);
		page.setDiags(diags);
		List<EmrHomePageOps> ops = recMapper.queryHomePageOps(map);
		page.setOps(ops);
		List<EmrHomePageCharges> charges = recMapper.queryHomePageCharges(map);
		page.setCharges(charges);
		
		List<EmrHomePageBr> brs = recMapper.queryEmrHomePageBrsByPage(pkPage);
		page.setBrs(brs);
		if(Application.isSqlServer()){
			EmrHomePageOr or = recMapper.queryEmrHomePageOrByPageSql(pkPage);
			page.setOr(or);
		}else{
			EmrHomePageOr or = recMapper.queryEmrHomePageOrByPage(pkPage);
			page.setOr(or);
		}
		List<EmrHomePageOrDt> ordts = recMapper.queryEmrHomePageOrDtsByPage(pkPage);
		page.setOrdts(ordts);
		
		EmrHomePageZL zls =  recMapper.queryHomePageZls(map);
		page.setHomePageZL(zls);

		return page;
	}

	/**
	 * 根据条件查询患者病案首页记录(附卡)
	 * 
	 * @param pkPv
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public EmrHomePage queryHomePageSupp(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		EmrHomePage page = recMapper.queryHomePageByPv(map);
		if (page == null)
			return null;
		String pkPage = page.getPkPage();
		List<EmrHomePageBr> brs = recMapper.queryEmrHomePageBrsByPage(pkPage);
		page.setBrs(brs);
		EmrHomePageOr or = recMapper.queryEmrHomePageOrByPage(pkPage);
		page.setOr(or);
		List<EmrHomePageOrDt> ordts = recMapper.queryEmrHomePageOrDtsByPage(pkPage);
		page.setOrdts(ordts);

		return page;
	}

	/**
	 * 保存患者病案首页记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public int saveEmrHomePage(String param, IUser user) {
		EmrHomePage page = JsonUtil.readValue(param, EmrHomePage.class);
		String suppMode = ApplicationUtils.getSysparam("HomePageAddSupCard", true);
		String IsSyn = ApplicationUtils.getSysparam("IsSynchronizationPi", true);
		User u = (User)user;
		if(suppMode==null||suppMode.equals("")) suppMode="1";
		String status = "";
		int rtn = 0;
		if (StringUtils.isNoneEmpty(page.getStatus()))
			status = page.getStatus();
		String pageType = page.getPageType() == null ? "" : page.getPageType();
		if (status.equals(EmrConstants.STATUS_NEW)) {
			rtn = recMapper.saveEmrHomePage(page);
		} else if (status.equals(EmrConstants.STATUS_UPD)) {
			rtn = recMapper.updateEmrHomePage(page);
			if(IsSyn!=null && IsSyn.equals("1")){updHisPiMaster(page,u);}
		} else if (status.equals(EmrConstants.STATUS_DEL)) {
			String pkPage = page.getPkPage();

			// 删除全部子表
			if (!pageType.equals("supp")||suppMode.equals("1")) {
				rtn = recMapper.deleteEmrHomePage(page.getPkPage());
				recMapper.deleteEmrHomePageOpsByPage(pkPage);
				recMapper.deleteEmrHomePageDiagsByPage(pkPage);
				recMapper.deleteEmrHomePageChargesByPage(pkPage);
			}
			recMapper.deleteEmrHomePageBrByPage(pkPage);
			recMapper.deleteEmrHomePageOrDtByPage(pkPage);
			recMapper.deleteEmrHomePageOrByPage(pkPage);

			return 1;
		}

		int i;
		
		if(suppMode.equals("1")){
			// 主卡
			List<EmrHomePageDiags> diagList = page.getDiags();
			for (i = 0; diagList != null && i < diagList.size(); i++) {
				EmrHomePageDiags diag = (EmrHomePageDiags) diagList.get(i);
				status = "";
				if (StringUtils.isNoneEmpty(diag.getStatus()))
					status = diag.getStatus();

				if (status.equals(EmrConstants.STATUS_NEW)) {
					rtn = recMapper.saveEmrHomePageDiags(diag);
				} else if (status.equals(EmrConstants.STATUS_UPD)) {
					rtn = recMapper.updateEmrHomePageDiags(diag);
				} else if (status.equals(EmrConstants.STATUS_DEL)) {
					rtn = recMapper.deleteEmrHomePageDiags(diag.getPkPagediag());
				}
			}

			List<EmrHomePageOps> opsList = page.getOps();
			for (i = 0; opsList != null && i < opsList.size(); i++) {
				EmrHomePageOps ops = (EmrHomePageOps) opsList.get(i);
				status = "";
				if (StringUtils.isNoneEmpty(ops.getStatus()))
					status = ops.getStatus();

				if (status.equals(EmrConstants.STATUS_NEW)) {
					rtn = recMapper.saveEmrHomePageOps(ops);
				} else if (status.equals(EmrConstants.STATUS_UPD)) {
					rtn = recMapper.updateEmrHomePageOps(ops);
				} else if (status.equals(EmrConstants.STATUS_DEL)) {
					rtn = recMapper.deleteEmrHomePageOps(ops.getPkOps());
				}
			}

			List<EmrHomePageCharges> chargeList = page.getCharges();
			for (i = 0; chargeList != null && i < chargeList.size(); i++) {
				EmrHomePageCharges charge = (EmrHomePageCharges) chargeList.get(i);
				//System.out.println("费用为:"+charge.getItemAmount());
				status = "";
				if (StringUtils.isNoneEmpty(charge.getStatus()))
					status = charge.getStatus();

				if (status.equals(EmrConstants.STATUS_NEW)) {
					rtn = recMapper.saveEmrHomePageCharges(charge);
				} else if (status.equals(EmrConstants.STATUS_UPD)) {
					rtn = recMapper.updateEmrHomePageCharges(charge);
				} else if (status.equals(EmrConstants.STATUS_DEL)) {
					rtn = recMapper.deleteEmrHomePageCharges(charge.getPkCharge());
				}
			}

			List<EmrHomePageTrans> transList = page.getTrans();
			for (i = 0; transList != null && i < transList.size(); i++) {
				EmrHomePageTrans trans = (EmrHomePageTrans) transList.get(i);
				status = "";
				if (StringUtils.isNoneEmpty(trans.getStatus()))
					status = trans.getStatus();

				if (status.equals(EmrConstants.STATUS_NEW)) {
					rtn = recMapper.saveEmrHomePageTrans(trans);
				} else if (status.equals(EmrConstants.STATUS_UPD)) {
					rtn = recMapper.updateEmrHomePageTrans(trans);
				} else if (status.equals(EmrConstants.STATUS_DEL)) {
					rtn = recMapper.deleteEmrHomePageTrans(trans.getPkTrans());
				}
			}
			
			// 附属卡
			List<EmrHomePageBr> brList = page.getBrs();
			for (i = 0; brList != null && i < brList.size(); i++) {
				EmrHomePageBr br = (EmrHomePageBr) brList.get(i);
				status = "";
				if (StringUtils.isNoneEmpty(br.getStatus()))
					status = br.getStatus();

				if (status.equals(EmrConstants.STATUS_NEW)) {
					rtn = recMapper.saveEmrHomePageBr(br);
				} else if (status.equals(EmrConstants.STATUS_UPD)) {
					rtn = recMapper.updateEmrHomePageBr(br);
				} else if (status.equals(EmrConstants.STATUS_DEL)) {
					rtn = recMapper.deleteEmrHomePageOps(br.getPkBr());
				}
			}

			if (page.getOr() != null) {
				EmrHomePageOr or = (EmrHomePageOr) page.getOr();
				status = "";
				if (StringUtils.isNoneEmpty(or.getStatus()))
					status = or.getStatus();

				if (status.equals(EmrConstants.STATUS_NEW)) {
					rtn = recMapper.saveEmrHomePageOr(or);
				} else if (status.equals(EmrConstants.STATUS_UPD)) {
					rtn = recMapper.updateEmrHomePageOr(or);
				} else if (status.equals(EmrConstants.STATUS_DEL)) {
					rtn = recMapper.deleteEmrHomePageOr(or.getPkPageor());
				}
			}

			List<EmrHomePageOrDt> ordtList = page.getOrdts();
			for (i = 0; ordtList != null && i < ordtList.size(); i++) {
				EmrHomePageOrDt ordt = (EmrHomePageOrDt) ordtList.get(i);
				status = "";
				if (StringUtils.isNoneEmpty(ordt.getStatus()))
					status = ordt.getStatus();
				if(page.getDelFlag()!=null&&page.getDelFlag().equals("1")){
					if(status.equals(EmrConstants.STATUS_NEW)) status=EmrConstants.STATUS_UPD;
				}
				if (status.equals(EmrConstants.STATUS_NEW)) {
					rtn = recMapper.saveEmrHomePageOrDt(ordt);
				} else if (status.equals(EmrConstants.STATUS_UPD)) {
					rtn = recMapper.updateEmrHomePageOrDt(ordt);
				} else if (status.equals(EmrConstants.STATUS_DEL)) {
					rtn = recMapper.deleteEmrHomePageOrDt(ordt.getPkOrdt());
				}
			}
			
			EmrHomePageZL zl = (EmrHomePageZL)page.getHomePageZL();
			if(zl != null)
			{
				status = "";
				if (StringUtils.isNoneEmpty(zl.getStatus()))
					status = zl.getStatus();
	
				if (status.equals(EmrConstants.STATUS_NEW)) {
					rtn = recMapper.saveEmrHomePageZL(zl);
				} else if (status.equals(EmrConstants.STATUS_UPD)) {
					rtn = recMapper.updateEmrHomePageZL(zl);
				} else if (status.equals(EmrConstants.STATUS_DEL)) {
					rtn = recMapper.deleteEmrHomePageZL(zl.getPkZl());
				}
			}
		}else{
			if (!pageType.equals("supp")) {
				// 主卡
				List<EmrHomePageDiags> diagList = page.getDiags();
				for (i = 0; diagList != null && i < diagList.size(); i++) {
					EmrHomePageDiags diag = (EmrHomePageDiags) diagList.get(i);
					status = "";
					if (StringUtils.isNoneEmpty(diag.getStatus()))
						status = diag.getStatus();

					if (status.equals(EmrConstants.STATUS_NEW)) {
						rtn = recMapper.saveEmrHomePageDiags(diag);
					} else if (status.equals(EmrConstants.STATUS_UPD)) {
						rtn = recMapper.updateEmrHomePageDiags(diag);
					} else if (status.equals(EmrConstants.STATUS_DEL)) {
						rtn = recMapper.deleteEmrHomePageDiags(diag.getPkPagediag());
					}
				}

				List<EmrHomePageOps> opsList = page.getOps();
				for (i = 0; opsList != null && i < opsList.size(); i++) {
					EmrHomePageOps ops = (EmrHomePageOps) opsList.get(i);
					status = "";
					if (StringUtils.isNoneEmpty(ops.getStatus()))
						status = ops.getStatus();

					if (status.equals(EmrConstants.STATUS_NEW)) {
						rtn = recMapper.saveEmrHomePageOps(ops);
					} else if (status.equals(EmrConstants.STATUS_UPD)) {
						rtn = recMapper.updateEmrHomePageOps(ops);
					} else if (status.equals(EmrConstants.STATUS_DEL)) {
						rtn = recMapper.deleteEmrHomePageOps(ops.getPkOps());
					}
				}

				List<EmrHomePageCharges> chargeList = page.getCharges();
				for (i = 0; chargeList != null && i < chargeList.size(); i++) {
					EmrHomePageCharges charge = (EmrHomePageCharges) chargeList.get(i);
					status = "";
					if (StringUtils.isNoneEmpty(charge.getStatus()))
						status = charge.getStatus();

					if (status.equals(EmrConstants.STATUS_NEW)) {
						rtn = recMapper.saveEmrHomePageCharges(charge);
					} else if (status.equals(EmrConstants.STATUS_UPD)) {
						rtn = recMapper.updateEmrHomePageCharges(charge);
					} else if (status.equals(EmrConstants.STATUS_DEL)) {
						rtn = recMapper.deleteEmrHomePageCharges(charge.getPkCharge());
					}
				}

				List<EmrHomePageTrans> transList = page.getTrans();
				for (i = 0; transList != null && i < transList.size(); i++) {
					EmrHomePageTrans trans = (EmrHomePageTrans) transList.get(i);
					status = "";
					if (StringUtils.isNoneEmpty(trans.getStatus()))
						status = trans.getStatus();

					if (status.equals(EmrConstants.STATUS_NEW)) {
						rtn = recMapper.saveEmrHomePageTrans(trans);
					} else if (status.equals(EmrConstants.STATUS_UPD)) {
						rtn = recMapper.updateEmrHomePageTrans(trans);
					} else if (status.equals(EmrConstants.STATUS_DEL)) {
						rtn = recMapper.deleteEmrHomePageTrans(trans.getPkTrans());
					}
				}
			} else {
				// 附属卡
				List<EmrHomePageBr> brList = page.getBrs();
				for (i = 0; brList != null && i < brList.size(); i++) {
					EmrHomePageBr br = (EmrHomePageBr) brList.get(i);
					status = "";
					if (StringUtils.isNoneEmpty(br.getStatus()))
						status = br.getStatus();

					if (status.equals(EmrConstants.STATUS_NEW)) {
						rtn = recMapper.saveEmrHomePageBr(br);
					} else if (status.equals(EmrConstants.STATUS_UPD)) {
						rtn = recMapper.updateEmrHomePageBr(br);
					} else if (status.equals(EmrConstants.STATUS_DEL)) {
						rtn = recMapper.deleteEmrHomePageBr(br.getPkBr());
					}
				}

				if (page.getOr() != null) {
					EmrHomePageOr or = (EmrHomePageOr) page.getOr();
					status = "";
					if (StringUtils.isNoneEmpty(or.getStatus()))
						status = or.getStatus();

					if (status.equals(EmrConstants.STATUS_NEW)) {
						rtn = recMapper.saveEmrHomePageOr(or);
					} else if (status.equals(EmrConstants.STATUS_UPD)) {
						rtn = recMapper.updateEmrHomePageOr(or);
					} else if (status.equals(EmrConstants.STATUS_DEL)) {
						rtn = recMapper.deleteEmrHomePageOr(or.getPkPageor());
					}
				}

				List<EmrHomePageOrDt> ordtList = page.getOrdts();
				for (i = 0; ordtList != null && i < ordtList.size(); i++) {
					EmrHomePageOrDt ordt = (EmrHomePageOrDt) ordtList.get(i);
					status = "";
					if (StringUtils.isNoneEmpty(ordt.getStatus()))
						status = ordt.getStatus();

					if (status.equals(EmrConstants.STATUS_NEW)) {
						rtn = recMapper.saveEmrHomePageOrDt(ordt);
					} else if (status.equals(EmrConstants.STATUS_UPD)) {
						rtn = recMapper.updateEmrHomePageOrDt(ordt);
					} else if (status.equals(EmrConstants.STATUS_DEL)) {
						rtn = recMapper.deleteEmrHomePageOrDt(ordt.getPkOrdt());
					}
				}
			}
		}

		return rtn;
	}

	/**
	 * 根据条件查询患者HIS病案首页记录
	 * 
	 * @param pkPv
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("rawtypes")
	public EmrHomePage queryHisHomePageData(String param, IUser user) throws ParseException {
		Map map = JsonUtil.readValue(param, Map.class);
		//System.out.println("queryHisHomePageData0:"+new Date());
		EmrHomePage page = recMapper.queryHisHomePageData(map);
		//System.out.println("queryHisHomePageData1:"+new Date());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (page.getBirthDate() != null) {
			String strBirth = formatter.format(page.getBirthDate());
			Date dateBirth;
			try {
				dateBirth = formatter.parse(strBirth);
				page.setBirthDate(dateBirth);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (page.getAdmitTime() != null) {
			String strAdmit = formatter.format(page.getAdmitTime());
			Date dateAdmit;
			try {
				dateAdmit = formatter.parse(strAdmit);
				page.setAdmitTime(dateAdmit);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (page.getDisTime() != null) {
			String strDist = formatter.format(page.getDisTime());
			Date dateDist;
			try {
				dateDist = formatter.parse(strDist);
				page.setDisTime(dateDist);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (page.getQcDate() != null) {
			String strQc = formatter.format(page.getQcDate());
			Date dateQc;
			try {
				dateQc = formatter.parse(strQc);
				page.setQcDate(dateQc);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String pkPv = (String) map.get("pkPv");
		// 婚姻
		String marryCode = page.getMarryCode();
		Integer tmp;
		if (marryCode != null && !marryCode.equals("")) {
			try {
				tmp = Integer.parseInt(marryCode);
				page.setMarryCode(tmp.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 入院途径
		String admitPathCode = page.getAdmitPathCode();
		if (admitPathCode != null && !admitPathCode.equals("")) {
			try {
				tmp = Integer.parseInt(admitPathCode);
				page.setAdmitPathCode(tmp.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 血型
		String aboCode = page.getBloodCodeAbo();
		if (aboCode != null && !aboCode.equals("")) {
			try {
				tmp = Integer.parseInt(aboCode);
				page.setBloodCodeAbo(tmp.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		String rhCode = page.getBloodNameRh();
		if (rhCode != null && !rhCode.equals("")) {
			try {
				tmp = Integer.parseInt(rhCode);
				page.setBloodCodeRh(tmp.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//@todo
		String hosCode=ApplicationUtils.getPropertyValue("emr.hos.code", "");
		//创建病案首页是否调用第三方数据
		String loadIntfData = ApplicationUtils.getSysparam("HomePageGenLoadIntfData", true);
		boolean bLoadIntfData=false;
		if(loadIntfData!=null&&loadIntfData.equals("1")) bLoadIntfData=true;
		//中二分支
		if(hosCode!=null&&hosCode.equals("syx")&&page!=null&&bLoadIntfData){
			Map<String,String> qryMap=new HashMap<>();
			qryMap.put("pkPv", page.getPkPv());
			qryMap.put("codePi", page.getCodePi());
			qryMap.put("codeIp", page.getPatNo());
			qryMap.put("pkPv", page.getPkPv());
			SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");//new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(page.getAdmitTime()!=null){
				Date beginDate = page.getAdmitTime();
				qryMap.put("beginDate", sdf.format(beginDate));
			}
			//查询血型、病理号数据
			Map<String,String> rtnMap=reqRsltQryHandler.getPatObsInfo(qryMap);
			if(rtnMap!=null){
				if(rtnMap.containsKey("bloodCodeAbo"))	//&&(pageAbo.equals("")||pageAbo.equals("5")||pageAbo.equals(""))
				page.setBloodCodeAbo(rtnMap.get("bloodCodeAbo"));
				if(rtnMap.containsKey("bloodCodeRh"))	//&&(page.getBloodCodeRh()==null||page.getBloodCodeRh().equals(""))
					page.setBloodCodeRh(rtnMap.get("bloodCodeRh"));
				if(rtnMap.containsKey("pathoNo"))	//&&(page.getPathoNo()==null||page.getPathoNo().equals(""))
					page.setPathoNo(rtnMap.get("pathoNo"));
				if(rtnMap.containsKey("diagNamePatho"))	
				{
					String lenstr=rtnMap.get("diagNamePatho").toString();
					lenstr=lenstr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");//去除空行回车
					int len=lenstr.length();
					if(len>100)
					{
						page.setDiagNamePatho(lenstr.substring(0,90));
					}else{
						page.setDiagNamePatho(lenstr);
					}
					
				}
					
			}
		}else if(hosCode!=null&&hosCode.equals("zsba")&&page!=null&&bLoadIntfData){//博爱分支
			Map<String,String> qryMap=new HashMap<>();
			qryMap.put("pkPv", page.getPkPv());
			qryMap.put("codePi", page.getCodePi());
			qryMap.put("codeIp", page.getPatNo());
			qryMap.put("pkPv", page.getPkPv());
			SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");//new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(page.getAdmitTime()!=null){
				Date beginDate = page.getAdmitTime();
				qryMap.put("beginDate", sdf.format(beginDate));
			}
			//查询血型、病理号数据
			Map<String,String> rtnMap=reqRsltQryHandler.getPatObsInfoBoai(qryMap);
			if(rtnMap!=null){
				if(rtnMap.containsKey("bloodCodeAbo"))	//&&(pageAbo.equals("")||pageAbo.equals("5")||pageAbo.equals(""))
				page.setBloodCodeAbo(rtnMap.get("bloodCodeAbo"));
				if(rtnMap.containsKey("bloodCodeRh"))	//&&(page.getBloodCodeRh()==null||page.getBloodCodeRh().equals(""))
					page.setBloodCodeRh(rtnMap.get("bloodCodeRh"));
				if(rtnMap.containsKey("pathoNo"))	//&&(page.getPathoNo()==null||page.getPathoNo().equals(""))
					page.setPathoNo(rtnMap.get("pathoNo"));
				if(rtnMap.containsKey("diagNamePatho"))	
				{
					String lenstr=rtnMap.get("diagNamePatho").toString();
					lenstr=lenstr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");//去除空行回车
					int len=lenstr.length();
					if(len>100)
					{
						page.setDiagNamePatho(lenstr.substring(0,90));
					}else{
						page.setDiagNamePatho(lenstr);
					}
					
				}
					
			}
			//取新生儿出生体重
			List<EmrInfantRecVo> dts = recMapper.queryPvLaborRecDt(page.getPkPv());
			if(dts!=null && dts.size()>0)
			{
				for (EmrInfantRecVo emrInfantRecVo : dts) {
					if(page.getNewbornWeight()==null){
						page.setNewbornWeight(new BigDecimal(emrInfantRecVo.getWeight().toString()));
					}
				}
			}
		}
		
		// 查询诊断列表
		// HashMap<String,String> diagMap=new HashMap<String,String>();
		// diagMap.put("pkPv", map.get("pkPv").toString());
		// diagMap.put("dtDiagType", "02"); //出院诊断
		// List<EmrPatDiags> diags=recMapper.queryPatDiagsList(diagMap);
		// if(diags!=null&&diags.size()>0){
		// List<EmrHomePageDiags> diagList=new ArrayList<EmrHomePageDiags>();
		// int seqNo=1;
		// for (EmrPatDiags item : diags) {
		// EmrHomePageDiags diag=new EmrHomePageDiags();
		// diag.setDiagCode(item.getCode());
		// diag.setDiagName(item.getName());
		// diag.setDiagDesc(item.getName());
		// diag.setSeqNo(seqNo);
		// seqNo++;
		// diagList.add(diag);
		// }
		// page.setDiags(diagList);
		// }
		//System.out.println("queryPvDiagVosPage0:"+new Date());
		List<PvDiagVo> pvDiagVo = recMapper.queryPvDiagVosPage(pkPv);
		//System.out.println("queryPvDiagVosPage1:"+new Date());
		page.setDiagVos(pvDiagVo);
		// 查询费用列表
		int i = 0, j = 0;
		int seqNo = 0;
		List<String> itemCodes;
		List<List<String>> itemList;
		String itemCode;
		EmrHomePageCharges charge;
		List<String> itemCodes2;
		String itemCodeStr = "";
		//List<EmrChargeList> charges = recMapper.queryChargeList(map);
		List<EmrChargeList> charges = recMapper.queryChargeListPg(map);
		//System.out.println("queryChargeListPg:"+new Date());
		List<EmrHomePageCharges> chargeList = new ArrayList<EmrHomePageCharges>();
		BigDecimal totalAmount = new BigDecimal("0");
		BigDecimal totalAmountPi = new BigDecimal("0");
		//BigDecimal amountSum = new BigDecimal("0");
		/*
		seqNo = 1;
		for (i = 0; i < 23; i++) {
			itemCodes = new ArrayList<String>();
			itemList = new ArrayList<List<String>>();
			itemList.add(itemCodes);
			if (seqNo == 1) {
				// 一般医疗服务费(诊查费、床位费、会诊费、营养咨询)
				itemCodes.add("01");
				itemCodes.add("02");
				itemCodes.add("03");
			} else if (seqNo == 2) {
				// 一般治疗操作费：包括注射、清创、换药、导尿、吸氧、抢救、重症监护等费用。
				itemCodes.add("06");
				itemCodes.add("0601");
				itemCodes.add("0602");
				itemCodes.add("0603");
				itemCodes.add("0604");
			} else if (seqNo == 3) {
				// 护理费：患者住院期间等级护理费用及专项护理费用。
				itemCodes.add("11");
			} else if (seqNo == 4) {
				// 其他费用：病房取暖费、病房空调费、救护车使用费、尸体料理费等。
				itemCodes.add("0699");
				itemCodes.add("9902");
			} else if (seqNo == 5) {
				// 病理诊断费：患者住院期间进行病理学有关检查项目费用。
				itemCodes.add("0406");
			} else if (seqNo == 6) {
				// 实验室诊断费：患者住院期间进行各项实验室检验费用。
				itemCodes.add("05");
				itemCodes.add("0501");
				itemCodes.add("0502");
				itemCodes.add("0503");
				itemCodes.add("0504");
				itemCodes.add("0505");
				itemCodes.add("0506");
				itemCodes.add("0507");
				itemCodes.add("0599");
			} else if (seqNo == 7) {
				// 影像学诊断费：患者住院期间进行透视、造影、CT、磁共振检查、B超检查、核素扫描、PET等影像学检查费用。
				itemCodes.add("04");
				itemCodes.add("0401");
				itemCodes.add("0402");
				itemCodes.add("0403");
				itemCodes.add("0404");
			} else if (seqNo == 8) {
				// 临床诊断项目费：临床科室开展的其他用于诊断的各种检查项目费用。包括有关内镜检查、肛门指诊、视力检测等项目费用。
				itemCodes.add("0405");
				itemCodes.add("0499");
			} else if (seqNo == 9) {
				// 非手术治疗项目费：临床利用无创手段进行治疗的项目产生的费用。包括高压氧舱、血液净化、精神治疗、临床物理治疗等。
				itemList.add(itemCodes);
				// 临床物理治疗指临床利用光、电、热等外界物理因素进行治疗的项目产生的费用，如放射治疗、放射性核素治疗、聚焦超声治疗等项目产生的费用。@todo
				itemCodes2 = new ArrayList<String>();
				itemList.add(itemCodes2);
			} else if (seqNo == 10) {
				// 手术治疗费：临床利用有创手段进行治疗的项目产生的费用。包括麻醉费及各种介入、孕产、手术治疗等费用。
				itemCodes.add("07");
				itemCodes.add("0701");
				itemCodes.add("0702");
				itemCodes.add("0703");
				itemList.add(itemCodes);
				itemCodes2 = new ArrayList<String>();
				itemCodes2.add("0703");
				itemList.add(itemCodes2);
				itemCodes2 = new ArrayList<String>();
				itemCodes2.add("0701");
				itemCodes2.add("0702");
				itemList.add(itemCodes2);
			} else if (seqNo == 11) {
				// 康复类：对患者进行康复治疗产生的费用。包括康复评定和治疗。
			} else if (seqNo == 12) {
				// 中医类：利用中医手段进行治疗产生的费用。
			} else if (seqNo == 13) {
				// 西药费：患者住院期间使用西药所产生的费用。
				// 抗菌药物费用：患者住院期间使用抗菌药物所产生的费用，包含于“西药费”中。@todo
				itemCodes.add("0901");
			} else if (seqNo == 14) {
				// 中成药费：患者住院期间使用中成药所产生的费用。中成药是以中草药为原料，经制剂加工制成各种不同剂型的中药制品。
				itemCodes.add("0902");
			} else if (seqNo == 15) {
				// 中草药费：患者住院期间使用中草药所产生的费用。中草药主要由植物药（根、茎、叶、果）、动物药（内脏、皮、骨、器官等）和矿物药组成。
				itemCodes.add("0903");
			} else if (seqNo == 16) {
				// 血费：患者住院期间使用临床用血所产生的费用，包括输注全血、红细胞、血小板、白细胞、血浆的费用。医疗机构对患者临床用血的收费包括血站供应价格、配血费和储血费。
				itemCodes.add("0605");
			} else if (seqNo == 17) {
				// 白蛋白类制品费：患者住院期间使用白蛋白的费用。

			} else if (seqNo == 18) {
				// 球蛋白类制品费：患者住院期间使用球蛋白的费用。

			} else if (seqNo == 19) {
				// 凝血因子类制品费：患者住院期间使用凝血因子的费用。

			} else if (seqNo == 20) {
				// 细胞因子类制品费：患者住院期间使用细胞因子的费用。

			} else if (seqNo == 21) {
				// 检查用一次性医用材料费：患者住院期间检查检验所使用的一次性医用材料费用。
				itemCodes.add("10");
			} else if (seqNo == 22) {
				// 治疗用一次性医用材料费：患者住院期间治疗所使用的一次性医用材料费用。
				itemCodes.add("0406");
			} else if (seqNo == 23) {
				// 手术用一次性医用材料费：患者住院期间进行手术、介入操作时所使用的一次性医用材料费用。
				itemCodes.add("0406");
			}
			for (j = 0; j < itemList.size(); j++) {

				charge = new EmrHomePageCharges();
				charge.setItemCodes(itemList.get(j));
				charge.setSeqNo(seqNo);
				if (j == 0) {
					itemCodeStr = "cost" + seqNo;
				} else {
					itemCodeStr = "cost" + seqNo + "-" + j;
				}
				charge.setItemCode(seqNo + "-" + j);
				charge.setCtrlName(itemCodeStr);
				BigDecimal amount = new BigDecimal("0");
				if (charge.getItemCodes().size() > 0) {
					for (EmrChargeList hisCharge : charges) {
						if (charge.getItemCodes().indexOf(hisCharge.getCode()) >= 0) {
							amount = amount.add(hisCharge.getAmount());
							amountSum = amountSum.add(amount);
						}
					}
				}

				charge.setItemAmount(amount);
				chargeList.add(charge);
			}

			seqNo++;
		}

		
		for (EmrChargeList hisCharge : charges) {
			totalAmount = totalAmount.add(hisCharge.getAmount());
		}
		
		// 其他费：患者住院期间未能归入以上各类的费用总和。
		charge = new EmrHomePageCharges();
		charge.setSeqNo(24);
		charge.setItemCode("24");
		charge.setCtrlName("cost24");
		charge.setItemAmount(totalAmount.subtract(amountSum));
		*/
		
		/*
		 * 1.综合医疗服务类：各科室共同使用的医疗服务项目发生的费用。 （1）一般医疗服务费：包括诊查费、床位费、会诊费、营养咨询等费用。
		 * （2）一般治疗操作费：包括注射、清创、换药、导尿、吸氧、抢救、重症监护等费用。 （3）护理费：患者住院期间等级护理费用及专项护理费用。
		 * （4）其他费用：病房取暖费、病房空调费、救护车使用费、尸体料理费等。 2.诊断类：用于诊断的医疗服务项目发生的费用
		 * （1）病理诊断费：患者住院期间进行病理学有关检查项目费用。 （2）实验室诊断费：患者住院期间进行各项实验室检验费用。
		 * （3）影像学诊断费：患者住院期间进行透视、造影、CT、磁共振检查、B超检查、核素扫描、PET等影像学检查费用。
		 * （4）临床诊断项目费：临床科室开展的其他用于诊断的各种检查项目费用。包括有关内镜检查、肛门指诊、视力检测等项目费用。 3.治疗类：
		 * （1）非手术治疗项目费：临床利用无创手段进行治疗的项目产生的费用。包括高压氧舱、血液净化、精神治疗、临床物理治疗等。
		 * 临床物理治疗指临床利用光、电、热等外界物理因素进行治疗的项目产生的费用，如放射治疗、放射性核素治疗、聚焦超声治疗等项目产生的费用。
		 * （2）手术治疗费：临床利用有创手段进行治疗的项目产生的费用。包括麻醉费及各种介入、孕产、手术治疗等费用。
		 * 4.康复类：对患者进行康复治疗产生的费用。包括康复评定和治疗。 5.中医类：利用中医手段进行治疗产生的费用。
		 * 6.西药类：包括有机化学药品、无机化学药品和生物制品费用。 （1）西药费：患者住院期间使用西药所产生的费用。
		 * （2）抗菌药物费用：患者住院期间使用抗菌药物所产生的费用，包含于“西药费”中。 7.中药类：包括中成药和中草药费用。
		 * （1）中成药费：患者住院期间使用中成药所产生的费用。中成药是以中草药为原料，经制剂加工制成各种不同剂型的中药制品。
		 * （2）中草药费：患者住院期间使用中草药所产生的费用。中草药主要由植物药（根、茎、叶、果）、动物药（内脏、皮、骨、器官等）和矿物药组成。
		 * 8.血液和血液制品类： （1）血费：患者住院期间使用临床用血所产生的费用，包括输注全血、红细胞、血小板、白细胞、血浆的费用。
		 * 医疗机构对患者临床用血的收费包括血站供应价格、配血费和储血费。 （2）白蛋白类制品费：患者住院期间使用白蛋白的费用。
		 * （3）球蛋白类制品费：患者住院期间使用球蛋白的费用。 （4）凝血因子类制品费：患者住院期间使用凝血因子的费用。
		 * （5）细胞因子类制品费：患者住院期间使用细胞因子的费用。
		 * 9.耗材类：当地卫生、物价管理部门允许单独收费的耗材。按照医疗服务项目所属类别对一次性医用耗材进行分类。“诊断类”
		 * 操作项目中使用的耗材均归入“检查用一次性医用材料费”；除“手术治疗”外的其他治疗和康复项目（包括“非手术治疗”、“临床物理治疗”、“康复”
		 * 、“中医治疗”）中使用的耗材均列入“治疗用一次性医用材料费”；“手术治疗”操作项目中使用的耗材均归入“手术用一次性医用材料费”。
		 * （1）检查用一次性医用材料费：患者住院期间检查检验所使用的一次性医用材料费用。
		 * （2）治疗用一次性医用材料费：患者住院期间治疗所使用的一次性医用材料费用。
		 * （3）手术用一次性医用材料费：患者住院期间进行手术、介入操作时所使用的一次性医用材料费用。 10.其他类：
		 * 其他费：患者住院期间未能归入以上各类的费用总和。
		 */
		
		for (EmrChargeList hisCharge : charges) {
			charge = new EmrHomePageCharges();
			charge.setSeqNo(seqNo);
			charge.setItemCode(hisCharge.getCode());
			
			charge.setItemAmount(hisCharge.getAmount());
			
			chargeList.add(charge);
			
			if(hisCharge.getItemType()!=null&&hisCharge.getItemType().equals("1")){
				totalAmount = totalAmount.add(hisCharge.getAmount());
				totalAmountPi=totalAmountPi.add(null == hisCharge.getAmountPi() ? BigDecimal.ZERO : hisCharge.getAmountPi());
			}
			
		}
		charge = new EmrHomePageCharges();
		charge.setItemCode("00");
		charge.setItemAmount(totalAmount);
		chargeList.add(charge);
		
		charge = new EmrHomePageCharges();
		charge.setItemCode("99");
		charge.setItemAmount(totalAmountPi);
		chargeList.add(charge);
		
		page.setCharges(chargeList);

		EmrHomePageOps pageOps;
		
		// 查询手术列表
		try {
			List<EmrOpsList> ops = recMapper.queryOpsList(map);
			//System.out.println("queryOpsList:"+new Date());
			if (ops != null && ops.size() > 0) {
				seqNo = 1;
				List<EmrHomePageOps> opsList = new ArrayList<EmrHomePageOps>();
				for (EmrOpsList hisOps : ops) {
					pageOps = new EmrHomePageOps();
					pageOps.setOpCode(hisOps.getCode());
					pageOps.setOpName(hisOps.getName());
					if (hisOps.getDateConfirm() != null) {
						pageOps.setOpDate(hisOps.getDateConfirm());
					} else {
						pageOps.setOpDate(hisOps.getDateApply());
					}
					if (hisOps.getDtOplevel() != null) {
						String opLevel = hisOps.getDtOplevel();
						Integer num = Integer.parseInt(opLevel);
						pageOps.setGradeCode(num.toString());
					}

					pageOps.setPkEmpOp(hisOps.getPkEmpPhyOp());
					pageOps.setOpDocName(hisOps.getNameEmpPhyOp());
					pageOps.setPkEmpOpi(hisOps.getPkEmpAsis());
					pageOps.setPkEmpOpii(hisOps.getPkEmpAsis2());
					pageOps.setOpiName(hisOps.getNameEmpAsis());
					pageOps.setOpiiName(hisOps.getNameEmpAsis2());
					pageOps.setPkEmpAnes(hisOps.getPkEmpAnae());
					pageOps.setAnesDocName(hisOps.getNameEmpAnae());
					if (hisOps.getDtAnae() != null) {
						String AnesTypeCode = hisOps.getDtAnae();
						try {
							Integer tmpInt = Integer.parseInt(AnesTypeCode);
							pageOps.setAnesTypeCode(tmpInt.toString());

						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					pageOps.setSeqNo(seqNo);
					opsList.add(pageOps);
					seqNo++;
				}
				page.setOps(opsList);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			// 查询手术-操作列表
			List<EmrOpHandleList> ophandle = recMapper.queryOphandleList(map);
			SimpleDateFormat opHanDate=new SimpleDateFormat("yyyy-MM-dd");
			if (ophandle != null && ophandle.size() > 0) {
				List<EmrHomePageOps> opsHanList = new ArrayList<EmrHomePageOps>();
				for (EmrOpHandleList hisOpsHan : ophandle) {
					if(CommonUtils.isEmptyString(hisOpsHan.getOperateName()) || CommonUtils.isEmptyString(hisOpsHan.getOperateTime()))
					{
						continue;
					}
					pageOps = new EmrHomePageOps();
					pageOps.setOpName(hisOpsHan.getOperateName());
					pageOps.setOpDate(opHanDate.parse(hisOpsHan.getOperateTime()));
					opsHanList.add(pageOps);
				}
				page.setOpsHandle(opsHanList);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		// 转科
		// 查询转科列表
		EmrHomePageTrans pageTrans;
		List<EmrTransList> trans = recMapper.queryTransList(map);
		//System.out.println("queryTransList:"+new Date());
		if (trans != null && trans.size() > 0) {
			seqNo = 1;
			List<EmrHomePageTrans> transList = new ArrayList<EmrHomePageTrans>();
			for (EmrTransList hisTrans : trans) {
				pageTrans = new EmrHomePageTrans();
				pageTrans.setSeqNo(seqNo);
				pageTrans.setPkDept(hisTrans.getPkDept());
				pageTrans.setDeptCode(hisTrans.getDeptCode());
				pageTrans.setDeptName(hisTrans.getDeptName());
				pageTrans.setTransDate(hisTrans.getDateBegin());

				transList.add(pageTrans);
				seqNo++;
			}

			page.setTrans(transList);
		}
		// page.setMedOrgName(UserContext.getUser().getProperties().get("org_name").toString());

		
		return page;
	}

	/**
	 * 根据条件查询患者HIS病案首页记录(附属卡)
	 * 
	 * @param pkPv
	 * @param user
	 * @return
	 */
	public EmrHomePage queryHisHomePageDataSupp(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		EmrHomePage page = new EmrHomePage();
		// 分娩记录
		List<EmrInfantRecVo> dts = recMapper.queryPvLaborRecDt(map.get("pkPv").toString());
		List<EmrHomePageOrDt> ordts=new ArrayList<EmrHomePageOrDt>();
		//查询化疗药品
		if (Application.isSqlServer()) {
			ordts= recMapper.queryHisHomePageOrDtSql(map.get("pkPv").toString());
		}else{
			ordts= recMapper.queryHisHomePageOrDt(map.get("pkPv").toString());
		}
		
		
		page.setLaborRecDt(dts);
		page.setOrdts(ordts);
		
		return page;
	}

	/**
	 * 查询患者费用信息(HIS)
	 * 
	 * @param pkPv
	 * @return
	 */
	public List<EmrChargeList> queryChargeList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrChargeList> list=recMapper.queryChargeList(map);
		return list;
	}

	/**
	 * 查询患者手术记录
	 * 
	 * @param pkPv
	 * @return
	 */
	public List<EmrOpsList> queryOpsList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		return recMapper.queryOpsList(map);
	}

	/**
	 * 查询患者HIS诊断
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> queryPatHisDiags(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		List<Map> rtnList = new ArrayList<Map>();
		if (Application.isSqlServer()) {
			rtnList = recMapper.queryPatHisDiagsSql(map);
		} else {
			recMapper.queryPatHisDiags(map);
			rtnList = (List<Map>) map.get("result");
		}

		// System.out.println("queryPatHisDiags:"+rtnList.size());
		return rtnList;
	}

	/**
	 * 查询患者HIS医护人员
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> queryPatHisStaff(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		List<Map> rtnList = new ArrayList<Map>();
		if (Application.isSqlServer()) {
			rtnList = recMapper.queryPatHisStaffSql(map);
		} else {
			recMapper.queryPatHisStaff(map);
			rtnList = (List<Map>) map.get("result");
		}

		// System.out.println("queryPatHisStaff:"+rtnList.size());
		return rtnList;
	}

	/**
	 * 查询患者临床诊断
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public DiagParam queryPvDiags(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		DiagParam diag = new DiagParam();
		String pkPv = map.get("pkPv").toString();

		List<Map<String, Object>> pvDiagList = recMapper.queryPvDiags(pkPv);
		// List<Map<String, Object>> cnDiagList=recMapper.queryCnDiag(pkPv);
		diag.setPvDiagList(pvDiagList);
		// diag.setCnDiagList(cnDiagList);
		return diag;
	}

	/**
	 * 保存患者临床诊断
	 * 
	 * @param param
	 * @param user
	 */
	public void savePvDiags(String param, IUser user) {
		SaveDiagParam diagParam = JsonUtil.readValue(param, SaveDiagParam.class);
		String pkPv = diagParam.getPkPv();
		if (StringUtils.isEmpty(pkPv))
			return;
		List<PvDiag> list = diagParam.getPvDiagList();
		User u = (User) user;
		String status = "";
		// DataBaseHelper.update("update pv_diag set del_flag='1' where
		// pk_pv=?", new Object[]{pkPv});
		for (PvDiag pvDiag : list) {
			status = pvDiag.getStatus();
			if (status == null)
				status = "";
			if (status.equals(EmrConstants.STATUS_NEW)) {
				DataBaseHelper.insertBean(pvDiag);
			} else if (status.equals(EmrConstants.STATUS_UPD)) {
				DataBaseHelper.updateBeanByPk(pvDiag, false);
			} else if (status.equals(EmrConstants.STATUS_DEL)) {
				DataBaseHelper.deleteBeanByPk(pvDiag);
			}
		}
		List<PvDiag> qryList = DataBaseHelper.queryForList(
				"select * from pv_diag where del_flag='0' and pk_pv=? order by ts desc", PvDiag.class,
				new Object[] { pkPv });
		if (qryList.size() == 0)
			return;
		CnDiag cnDiag = new CnDiag();
		cnDiag.setPkCndiag(NHISUUID.getKeyId());
		cnDiag.setEuPvtype("3"); // 住院
		cnDiag.setPkPv(pkPv);
		cnDiag.setDateDiag(new Date());
		cnDiag.setPkEmpDiag(u.getPkEmp());
		cnDiag.setNameEmpDiag(u.getNameEmp());
		String desc_diags = "";
		List<CnDiagDt> cnDiagDtList = new ArrayList<CnDiagDt>();
		for (PvDiag pvDiag : qryList) {
			if (StringUtils.isEmpty(desc_diags)) {
				desc_diags += pvDiag.getDescDiag();
			} else {
				desc_diags += "," + pvDiag.getDescDiag();
			}
			CnDiagDt cnDiagDt = new CnDiagDt();
			cnDiagDt.setPkCndiagdt(NHISUUID.getKeyId());
			cnDiagDt.setPkCndiag(cnDiag.getPkCndiag());
			cnDiagDt.setSortNo(pvDiag.getSortNo());
			cnDiagDt.setDtDiagtype(pvDiag.getDtDiagtype());
			cnDiagDt.setPkDiag(pvDiag.getPkDiag());
			cnDiagDt.setDescDiag(pvDiag.getDescDiag());
			cnDiagDt.setFlagMaj(pvDiag.getFlagMaj());
			cnDiagDt.setFlagSusp(pvDiag.getFlagSusp());
			cnDiagDt.setFlagInfect(pvDiag.getFlagContagion());
			cnDiagDt.setFlagFinally(pvDiag.getFlagFinally());
			cnDiagDt.setFlagCure(pvDiag.getFlagCure());
			cnDiagDt.setTs(new Date());
			cnDiagDtList.add(cnDiagDt);
		}
		cnDiag.setDescDiags(desc_diags);
		DataBaseHelper.insertBean(cnDiag);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnDiagDt.class), cnDiagDtList);
	}

	/**
	 * 根据主键查询病历医师
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrDoctor queryEmrDoctorByPk(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if(map.get("pkEmp")==null) return null;
		if(map.get("pwd")==null) return null;
		String pwdTxt=map.get("pwd").toString();
		
		String pkEmp=map.get("pkEmp").toString();
		List<EmrDoctor> dList=recMapper.queryEmrDoctorByPk(map);
		EmrDoctor doc=null;
		if(dList!=null&&dList.size()>0){
			doc = dList.get(0);
		}
		String sql="select u.*,e.caid from bd_ou_user u inner join bd_ou_employee e on u.pk_emp=e.pk_emp "
				+ " where u.pk_emp = ? and u.flag_active='1' and u.is_lock='0'";
		List<BdOuUser> userList = DataBaseHelper.queryForList(sql, BdOuUser.class, new Object[]{pkEmp});
		BdOuUser usr=null;
		String pwd="";
		if(userList!=null&&userList.size()>0){
			usr=userList.get(0);
			pwd=usr.getPwd();
		}
		if(doc==null||StringUtils.isEmpty(doc.getSignPwd())){
			//BdOuUser usr = DataBaseHelper.queryForBean("select * from bd_ou_user where pk_emp = ?", BdOuUser.class, new Object[]{pkEmp});
			
			String pwdEncry = new SimpleHash("md5",pwdTxt).toHex();
			if(doc==null) {
				doc = new EmrDoctor();
				doc.setPkEmp(pkEmp);
			}
			if(pwdEncry.equals(pwd)){
				doc.setSignPwd(pwdTxt);
			}else{
				doc.setSignPwd(pwdTxt+"*");
			}
		}
		if(usr!=null){
			doc.setEuCerttype(usr.getEuCerttype());
			doc.setCaid(usr.getCaid());
		}
		
		return doc;
	}

	/**
	 * 查询患者生命体征数据
	 * 
	 * @param pkPv
	 * @return
	 */
	public List<EmrVitalSigns> queryVitalSigns(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		
		List<EmrVitalSigns> list=new ArrayList<EmrVitalSigns>();
		String hosCode=ApplicationUtils.getPropertyValue("emr.hos.code", "");
		if(hosCode!=null&&hosCode.equals("syx")){
			String loadIntfData = ApplicationUtils.getSysparam("HomePageGenLoadIntfData", true);
			boolean bLoadIntfData=false;
			if(loadIntfData!=null&&loadIntfData.equals("1")) bLoadIntfData=true;
			if(bLoadIntfData){
				list = reqRsltQryHandler.queryVitalSignsSyx(map);
			}
		}else if(hosCode!=null&&hosCode.equals("zsba")){
			list = recMapper.queryVitalSigns(map);
			List<Map<String,Object>> bloodList  = recMapper.queryVitalSignsBlood(map);
			
			Float maxbl=null;
			for (EmrVitalSigns item : list) {
				if(item.getVsDate()!=null && item.getVsTime()!=null){
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						String nowd=item.getVsDate().substring(0, 10)+" "+item.getVsTime()+":00:00";
						Date now =dateFormat.parse(nowd);
						List<Map<String,Object>> glList=new ArrayList<>();
						for(Map<String,Object> item1 :bloodList){
							Date bldate=dateFormat.parse(item1.get("dateEntry").toString());
							long diff = bldate.getTime() - now.getTime();//这样得到的差值是毫秒级别  
						    long hours = diff/1000/60/60;
						    if(Math.abs(hours)<=2){
						    	glList.add(item1);
						    }
						}
						for(Map<String,Object> item2 :glList){
							boolean isnum=isNumeric(item2.get("val").toString());
							if(isnum){
								maxbl=Float.valueOf(item2.get("val").toString()); 
								if(item.getBlood()==null){
									item.setBlood(maxbl.toString());
							    }else if(Float.valueOf(item2.get("val").toString())>Float.valueOf(item.getBlood())){
							    	item.setBlood(maxbl.toString());
							    }
							}
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
					}
				}
			}
		}else{
			list = recMapper.queryVitalSigns(map);
		}
		//List<EmrVitalSigns> list = recMapper.queryVitalSigns(map);
		if (list != null && list.size() > 0) {
			for (EmrVitalSigns item : list) {
				//item.setBlood(item.getDiastolic() + "/" + item.getSystolic());
				if (item.getEuDateslot() != null && item.getEuDateslot().equals("1")) {
					String vsTime = item.getVsTime();
					if (vsTime != null && !vsTime.equals("")) {
						Integer time = Integer.parseInt(vsTime) + 12;
						item.setVsTime(time.toString());
					}
				}
			}
		}
		return list;
	}
	public boolean isNumeric(String str)
	{
		try {
			Float.parseFloat(str);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	/**
	 * 校验诊断编码
	 * 
	 * @param
	 * @return
	 */
	public void checkCode(String param, IUser user) {
		List<BdTermDiag> bdTermDiags = JSON.parseArray(param, BdTermDiag.class);
		for (BdTermDiag bdTermDiag : bdTermDiags) {
			int count = DataBaseHelper.queryForScalar("select count(1) bd_term_diag where DIAGCODE=?", Integer.class,
					bdTermDiag.getDiagcode());
			if (count == 0) {
				throw new BusException("疾病编码不在标准字典库范围内,请修改!");
			}
		}
	}

	/**
	 * 设置医师
	 * 
	 * @param
	 * @return
	 */
	public void setPhysician(String param, IUser user) {
		//1、设置医师
		Map map = JsonUtil.readValue(param, Map.class);
		recMapper.setPhysician(map);
		//2、设置文档记录中各级医师
		recMapper.updateMedRecEmpAct(map);
	}

	/**
	 * 更改病历创建医师
	 * 
	 * @param
	 * @return
	 */
	public void updEmrCreator(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String creatorbf="select * from EMR_MED_REC where PK_REC=?";
		EmrMedRec emrMedRec=DataBaseHelper.queryForBean(creatorbf, EmrMedRec.class, map.get("pkRec"));
		boolean isIntern=false;
		if(map.get("isIntern")!=null){
			String isInternStr=map.get("isIntern").toString();
			isIntern=isInternStr==null?false:(isInternStr.equals("1")?true:false);
		}
		
		//先判断原文档是否是实习生创建
		String pkEmpIntern = emrMedRec.getPkEmpIntern();
		Date internSignDate = emrMedRec.getInternSignDate();
		String sql="";
		if(StringUtils.isEmpty(pkEmpIntern)){
			//非实习生创建
			if(isIntern){
				//实习生修改（正式医师创建、实习生修改）
				sql="update emr_med_rec set name=?,creator=?,pk_emp_intern=?,pk_emp_refer_act=?,pk_emp_refer=null,intern_sign_date=null where pk_rec=?";
				DataBaseHelper.execute(sql, map.get("name"),map.get("creator"),map.get("creator"),map.get("pkEmpRefer"),map.get("pkRec"));
			}else{
				//非实习生修改（正式医师创建、正式医师修改）
				sql="update emr_med_rec set name=?,creator=?,pk_emp_refer=?,pk_emp_refer_act=? where pk_rec=?";
				DataBaseHelper.execute(sql, map.get("name"),map.get("creator"),map.get("creator"),map.get("creator"),map.get("pkRec"));
			}
		}else{
			//实习生创建
			if(isIntern){
				//实习生修改（实习生创建、实习生修改）
				sql="update emr_med_rec set name=?,creator=?,pk_emp_intern=?,pk_emp_refer_act=?,pk_emp_refer=null,intern_sign_date=null where pk_rec=?";
				DataBaseHelper.execute(sql, map.get("name"),map.get("creator"),map.get("creator"),map.get("pkEmpRefer"),map.get("pkRec"));
			}else{
				//非实习生修改（实习生创建、正式医师修改）
				sql="update emr_med_rec set name=?,creator=?,pk_emp_refer=?,pk_emp_refer_act=?,pk_emp_intern=null,intern_sign_date=null where pk_rec=?";
				DataBaseHelper.execute(sql, map.get("name"),map.get("creator"),map.get("creator"),map.get("creator"),map.get("pkRec"));
			}
			
		}
//		if(emrMedRec.getCreator()!=null&&emrMedRec.getPkEmpRefer()!=null&&emrMedRec.getCreator().equals(emrMedRec.getPkEmpRefer())){
//			sql="update emr_med_rec set name=?,creator=?,pk_emp_refer=? where pk_rec=?";
//			DataBaseHelper.execute(sql, map.get("name"),map.get("creator"),map.get("creator"),map.get("pkRec"));
//		}else{
//			sql="update emr_med_rec set name=?,creator=? where pk_rec=?";
//			DataBaseHelper.execute(sql, map.get("name"),map.get("creator"),map.get("pkRec"));
//		}
		SysApplog sysApplog = new SysApplog();
		User u = (User)user;
		sysApplog.setPkOrg(u.getPkOrg());
		sysApplog.setDateOp(new Date());
		sysApplog.setPkEmpOp(u.getPkEmp());
		sysApplog.setNameEmpOp(u.getNameEmp());
		sysApplog.setObjname("emr_med_rec 主键pkRec:"+map.get("pkRec"));
		sysApplog.setEuButype("99");
		sysApplog.setEuOptype("0");
		sysApplog.setContentBf("更改病历创建医师前:文档名称为[name]"+emrMedRec.getName()+",病历创建者为[creator] "+emrMedRec.getCreator());
		sysApplog.setContent("更改病历创建医师后:文档名称为[name]"+map.get("name")+",病历创建者为[creator] "+map.get("creator"));
		sysApplog.setDelFlag("0");
		DataBaseHelper.insertBean(sysApplog);
	}
	/**
	 * 更新病历记录
	 */
	public void renewEmrRec(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if(map.containsKey("printBatch")){
			List<String> list=(List)map.get("pkRec");
			List<EmrMedRec> reclist=new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				EmrMedRec emrRec=new EmrMedRec();
				emrRec.setPkRec(list.get(i));
				reclist.add(emrRec);
			}
			DataBaseHelper.batchUpdate("UPDATE EMR_MED_REC set flag_print='1' where PK_REC=:pkRec",reclist);
		}else{
			if(map.containsKey("coursePrint")){
				String sql="select rec.flag_print,rec.PK_REC from emr_med_rec rec left join emr_doc_type doc on rec.type_code = doc.code "+
						"where doc.flag_course = '1' and rec.pk_pv = ? and rec.del_flag = '0'";
				List<Map<String, Object>> emrMedRecList=DataBaseHelper.queryForList(sql, map.get("pkPv"));
				for (Map emr : emrMedRecList) {
					emr.put("flagPrint", "1");
					recMapper.renewRec(emr);
				}
			}else{
				recMapper.renewRec(map);
			}
		}
		
		
	}
	/**
	 * 查询患者未打印的病历
	 */
	public List<Map<String,Object>> queryRecPrint(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sql="select rec.name from emr_med_rec rec where rec.pk_pv = ? and nvl(flag_print,0) <> '1' and rec.del_flag = '0'";
		List<Map<String, Object>> recPrintList=DataBaseHelper.queryForList(sql, map.get("pkPv"));
		return recPrintList ;
	}
	/**
	 * 完成病历 006002001034
	 * 
	 * @param param
	 * @param user
	 */
	public void finishEmr(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		map.put("euStatus", map.get("euStatus").toString());
		map.put("finishDate", new Date());
		recMapper.finishEmr(map);
	}

	/**
	 * 查询病历文档记录(文档与目录对应)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrMedDocVo> queryMedDocList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.get("pkPv").toString();
		List<EmrMedDocVo> rtnList = recMapper.queryMedDocList(pkPv);
		return rtnList;
	}

	/**
	 * 通过pkpv查询视图病历数据源
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map queryEmrPatByPkpv(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.get("pkPv").toString();
		Map<String, Object> result = new HashMap();
		if (Application.isSqlServer()) {
			result = DataBaseHelper
					.queryForMap("select top 1 * from view_emr_pat_list where pk_pv=? order by date_admit desc", pkPv);
		} else {
			result = DataBaseHelper.queryForMap(
					"select t.* from (select * from view_emr_pat_list where pk_pv=? order by date_admit desc) t where rownum = 1",
					pkPv);
		}
		Map<String, Object> new_result = new HashMap();
		try {
			if (result != null) {
				for (String key : result.keySet()) {
					String keyStr=key.substring(0, 1).toUpperCase() + key.substring(1);
					Object value = result.get(key);
					new_result.put(keyStr,value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new_result;
	}
	/**
	 * 通过pkPv查询患者就诊信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map queryPatiInfoByPkpv(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		if(null == map) return null;
		String pkPv = map.get("pkPv").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		if (Application.isSqlServer()) {
			result = DataBaseHelper
					.queryForMap("select top 1 * from view_emr_pat_list_lite where pk_pv=? ", pkPv);
		} else {
			result = DataBaseHelper.queryForMap(
					"select t.* from (select * from view_emr_pat_list_lite where pk_pv=? ) t where rownum = 1",
					pkPv);
		}
		return result;
	}

	/**
	 * 查询患者扩展信息
	 * @param pkPv
	 * @param user
	 * @return
	 */
	public Map<String, Object> queryPatExtInfo(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);

		String pkPv = map.get("pkPv").toString();
		
		Map<String, Object> extInfo = new HashMap<String, Object>();
		if (Application.isSqlServer()) {
			extInfo = DataBaseHelper.queryForMap("select top 1 * from view_emr_pat_ext_info where pk_pv=? ", pkPv);
		} else {
			extInfo = DataBaseHelper.queryForMap("select t.* from (select * from view_emr_pat_ext_info where pk_pv=? ) t where rownum = 1",pkPv);
		}
		return extInfo;

	}

	/**
	 * 查询当前病人所有病历记录（列表+数据）@todo
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrMedRec> queryMedRecDocList(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrMedRec> rtnList =new ArrayList<EmrMedRec>();
		List<EmrMedRec> listData =new ArrayList<EmrMedRec>();
		String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();
		if(saveDataMode.equals("0")){//本地存储
			rtnList = recMapper.queryMedRecDocList(map);
		}else if(saveDataMode.equals("1")){//分离存储
			rtnList = recMapper.queryMedRecDocList(map);
			map.put("dbName", dbName);

			int i=0;
			int j=0;

			EmrMedRec rec=null;
			EmrMedDoc doc=null;
			List<String> pkDocs=new ArrayList<>();
			if(rtnList!=null&&rtnList.size()>0){
				for(i=0;i<rtnList.size();i++){
					rec=rtnList.get(i);
					pkDocs.add(rec.getPkDoc());
				}
				if(pkDocs!=null&&pkDocs.size()>0){
					List<EmrMedDoc> docs = recMapper.queryDocListEmrByPks(dbName, pkDocs);
					if(docs!=null&&docs.size()>0){
						for(i=0;i<docs.size();i++){
							doc=docs.get(i);
							if(doc!=null&&doc.getDocData()!=null){
								for(j=0;j<rtnList.size();j++){
									EmrMedRec recRtn=rtnList.get(j);
									if(recRtn.getMedDoc()==null) continue;
									if(doc.getPkDoc()!=null&&doc.getPkDoc().equals(recRtn.getMedDoc().getPkDoc())){
										recRtn.getMedDoc().setDocData(doc.getDocData());
										recRtn.getMedDoc().setDocDataBak(doc.getDocDataBak());
										recRtn.getMedDoc().setDocExpData(doc.getDocExpData());
										break;
									}
								}
								
							}
						}
					}
				}
				
			}
			
		}
		
		return rtnList;
	}
	/**
	 * 根据pkpv查询患者出院小结的出院时间
	 * @param pkPv
	 * @param user
	 * @return
	 */
	public String queryDisTimeBySummary(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.get("pkPv").toString();
		//String sql="select nvl(doc_txt,0) from emr_txt_data where pk_pv=? and para_code='0002103' and del_flag='0'";
		String txt=recMapper.queryDisTimeBySummaryByPk(pkPv);
		if(txt != null){
			if(txt.length()>11){
				txt=txt.substring(0, 11);
				return txt;
			}else{
				return txt;
			}
		}
		return txt;
	}

	public void updHisPiMaster(EmrHomePage page,User u){
		if(page.getPkPi()!=null){
			//查询患者原信息
			PiMaster piMaster=DataBaseHelper.queryForBean("select * from pi_master where pk_pi=? and del_flag='0'", PiMaster.class, page.getPkPi());
			//查询性别
			String sex=DataBaseHelper.queryForScalar("select CODE from BD_DEFDOC where CODE_DEFDOCLIST='000000' and BA_CODE=? and ROWNUM=1", String.class, page.getDtSex());
			//查询国籍
			String country=DataBaseHelper.queryForScalar("select CODE from BD_DEFDOC where CODE_DEFDOCLIST='000009' and BA_CODE=? and ROWNUM=1", String.class, page.getCountryCode());
			//查询民族
			String nation=DataBaseHelper.queryForScalar("select CODE from BD_DEFDOC where CODE_DEFDOCLIST='000003' and BA_CODE=? and ROWNUM=1", String.class, page.getNationCode());
			//查询职业
			String aa=page.getOccupCode();
			String occup=DataBaseHelper.queryForScalar("select CODE from BD_DEFDOC where CODE_DEFDOCLIST='000010' and BA_CODE=? and ROWNUM=1", String.class, page.getOccupCode());
			//查询婚姻
			String marry=DataBaseHelper.queryForScalar("select CODE from BD_DEFDOC where CODE_DEFDOCLIST='000006' and BA_CODE=? and ROWNUM=1", String.class, page.getMarryCode());
			//查询联系人关系
			String bb=page.getContactRelatCode();
			String contactRelat=DataBaseHelper.queryForScalar("select CODE from BD_DEFDOC where CODE_DEFDOCLIST='000013' and BA_CODE=? and ROWNUM=1", String.class, page.getContactRelatCode());
			
			if(sex!=null){page.setDtSex(sex);}
			if(country!=null){page.setCountryCode(country);}
			if(nation!=null){page.setNationCode(nation);}
			if(occup!=null){page.setOccupCode(occup);}
			if(marry!=null){page.setMarryCode(marry);}
			if(contactRelat!=null){page.setContactRelatCode(contactRelat);}
			if(page.getCurrAddrProv()==null){page.setCurrAddrProv("");}
			if(page.getCurrAddrCity()==null){page.setCurrAddrCity("");}
			if(page.getCurrAddrCounty()==null){page.setCurrAddrCounty("");}
			
			if(page.getResideAddrProv()==null){page.setResideAddrProv("");}
			if(page.getResideAddrCity()==null){page.setResideAddrCity("");}
			if(page.getResideAddrCounty()==null){page.setResideAddrCounty("");}
			if(page.getCurrAddrCode()!=null){
				String currAddr=recMapper.queryCurrAddrByCode(page.getCurrAddrCode());
				if(currAddr==null){
					page.setCurrAddr(page.getCurrAddrProv()+page.getCurrAddrCity()+page.getCurrAddrCounty());
				}else{
					page.setCurrAddr(currAddr);
				}
			}
			page.setResideAddr(page.getResideAddrProv()+page.getResideAddrCity()+page.getResideAddrCounty());
			
			int result=recMapper.updHisPiMaster(page);
			if(result>0){
				SysApplog sysApplog = new SysApplog();
				sysApplog.setPkOrg(u.getPkOrg());
				sysApplog.setDateOp(new Date());
				sysApplog.setPkEmpOp(u.getPkEmp());
				sysApplog.setNameEmpOp(u.getNameEmp());
				sysApplog.setCustip(ApplicationUtils.getCurrIp());
				sysApplog.setObjname("pi_master");
				sysApplog.setPkObj(page.getPkPi());
				sysApplog.setEuButype("1");
				sysApplog.setEuOptype("0");
				sysApplog.setContentBf("[dt_sex]"+piMaster.getDtSex()+", [dt_country]"+piMaster.getDtCountry()+", [dt_nation]"+piMaster.getDtNation()
						+", [dt_occu]"+piMaster.getDtOccu()+", [dt_marry]"+piMaster.getDtMarry()+", [addrcode_birth]"+piMaster.getAddrcodeBirth()
						+", [addr_birth]"+piMaster.getAddrBirth()+", [addrcode_origin]"+piMaster.getAddrcodeOrigin()+", [addr_origin]"+piMaster.getAddrOrigin()
						+", [addrcode_cur]"+piMaster.getAddrcodeCur()+", [addr_cur]"+piMaster.getAddrCur()+", [addr_cur_dt]"+piMaster.getAddrCurDt()+", [postcode_cur]"+piMaster.getPostcodeCur()
						+", [addrcode_regi]"+piMaster.getAddrcodeRegi()+", [addr_regi]"+piMaster.getAddrRegi()+", [addr_regi_dt]"+piMaster.getAddrRegiDt()+", [postcode_regi]"+piMaster.getPostcodeRegi()
						+", [unit_work]"+piMaster.getUnitWork()+", [tel_work]"+piMaster.getTelWork()+", [postcode_work]"+piMaster.getPostcodeWork()
						+", [name_rel]"+piMaster.getNameRel()+", [dt_ralation]"+piMaster.getDtRalation()+", [tel_rel]"+piMaster.getTelRel()+", [addr_rel]"+piMaster.getAddrRel()+", [id_no]"+piMaster.getIdNo());
				sysApplog.setContent("[dt_sex]"+sex+", [dt_country]"+country+", [dt_nation]"+nation
						+", [dt_occu]"+occup+", [dt_marry]"+marry+", [addrcode_birth]"+page.getBirthAddrCode()
						+", [addr_birth]"+page.getBirthAddr()+", [addrcode_origin]"+page.getOriginAddrCode()+", [addr_origin]"+page.getOriginAddr()
						+", [addrcode_cur]"+page.getCurrAddrCode()+", [addr_cur]"+page.getCurrAddr()+", [addr_cur_dt]"+page.getCurrAddrDt()+", [postcode_cur]"+page.getCurrZipCode()
						+", [addrcode_regi]"+page.getResideAddrCode()+", [addr_regi]"+page.getResideAddr()+", [addr_regi_dt]"+page.getResideAddrDt()+", [postcode_regi]"+page.getResideZipCode()
						+", [unit_work]"+page.getWorkUnit()+", [tel_work]"+page.getWorkUnitPhone()+", [postcode_work]"+page.getWorkUnitZipCode()
						+", [name_rel]"+page.getContactName()+", [dt_ralation]"+contactRelat+", [tel_rel]"+page.getContactPhone()+", [addr_rel]"+page.getContactAddr()+", [id_no]"+page.getIdNo());
				sysApplog.setDelFlag("0");
				DataBaseHelper.insertBean(sysApplog);
			}
		}
	}
	
	public EmrStdAddrVo queryStdAddr(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String code = map.get("code").toString();
		String sql="select * from EMR_STD_ADDR where CODE = ? and del_flag='0'";
		EmrStdAddrVo emrstdAddrVo=DataBaseHelper.queryForBean(sql, EmrStdAddrVo.class, code);
		return emrstdAddrVo;
	}
	/**
	 * 查询患者31天内是否再次住院
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public String qryIsToBeHosAgain(String param, IUser user) throws ParseException{
		Map map=JsonUtil.readValue(param, Map.class);
		String pkPi=map.get("pkPi").toString();//获取pkpi
		String pvPv=map.get("pkPv").toString();//获取pkpv
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//声明接受日期格式
		Calendar cal = Calendar.getInstance();
		Date beginTime=null;//声明这一次就诊的开始时间
		Date upEndTime=null;//声明上一次就诊的结束时间
		//根据pkpi查询该患者的就诊记录并排序
		String sql="select * from pv_encounter where pk_pi=? and eu_status!='9' and EU_PVTYPE='3' and del_flag='0' order by date_begin";
		List<PvEncounter> pvlist=DataBaseHelper.queryForList(sql, PvEncounter.class,pkPi);
		if(pvlist.size()>1){
			for (int i = 0; i < pvlist.size(); i++) {
				if(pvlist.get(i).getPkPv().equals(pvPv) && i==0){//第一次就是此次就诊就直接返回不用判断
					return null;
				}else if(pvlist.get(i).getPkPv().equals(pvPv)){
					if(pvlist.get(i-1).getDateEnd()==null){//只要上次的出院日期为空就直接返回空
						return null;
					}
					upEndTime=sf.parse(pvlist.get(i-1).getDateEnd().toString());
					beginTime=sf.parse(pvlist.get(i).getDateBegin().toString());
					cal.setTime(beginTime);
					long time1 = cal.getTimeInMillis();
					cal.setTime(upEndTime);
					long time2 = cal.getTimeInMillis();
					//算两次时间相差的天数
					long betweenDays=(time1-time2)/(1000*3600*24);
					Integer daysBetween=Integer.parseInt(String.valueOf(betweenDays));
					if(daysBetween<=31){
						return String.valueOf(betweenDays);
					}else{
						return null;
					}
					
				}
			}
		}
		return null;
	}
	/**
	 * 根据患者就诊主键查询会诊记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrConsult> queryConsultByPk(String param, IUser user){
		Map map=JsonUtil.readValue(param, Map.class);
		String pkPv=map.get("pkPv").toString();//获取pkpv
		List<EmrConsult> consultList=recMapper.queryConsultByPk(pkPv);
		return consultList;
	}
	
	/**
	 * 根据患者住院号查询历史病历(博爱)
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrHistory> queryHistorytByCodeIp(String param, IUser user){
		Map map=JsonUtil.readValue(param, Map.class);
		String codeIp=map.get("codeIp").toString();
		List<EmrHistory> historyList=recMapper.queryHistorytByCodeIp(codeIp);
		return historyList;
	}
	
	/**
	 * 
	 * 病历随访系统的保存方法
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public int saveEmrFollowUp(String param, IUser user) throws ParseException{
		int rowCount = 0;
		List<EmrFollow> emrfollowList = JsonUtil.readValue(param, new TypeReference<List<EmrFollow>>() {
        });
        if (emrfollowList == null || emrfollowList.size() == 0)
			throw new BusException("保存数据为空！");
        DataBaseHelper.execute("delete from EMR_Follow where Pk_Rec=?", new Object[]{emrfollowList.get(0).getPkRec()});
        List<EmrFollow> emrfollowListInsert = new ArrayList<EmrFollow>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(EmrFollow emrfollow : emrfollowList){
        	ApplicationUtils.setDefaultValue(emrfollow, true);
			emrfollow.setTs(new Date());
			emrfollow.setCreateTime(new Date());
			emrfollow.setCreator(user.getUserName());
        	emrfollowListInsert.add(emrfollow);
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(EmrFollow.class), emrfollowListInsert);
		return emrfollowListInsert.size();
	}

	public void saveEmrJsonData(String param, IUser user){
		Map readValue = JsonUtil.readValue(param, Map.class);
		JsonNode node = JsonUtil.getJsonNode(param, "pkPv");
		if (node==null) {
			return;
		}
		List<EmrMedValue> emrmedvalueList = JsonUtil.readValue(node, new TypeReference<List<EmrMedValue>>() {
		});
		String status = readValue.get("status").toString();
		if(emrmedvalueList==null||emrmedvalueList.size()==0) {
			throw new BusException("保存数据为空！");
		}
		if ("1".equals(status)){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(EmrMedValue.class), emrmedvalueList);
		}else {
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(EmrMedValue.class), emrmedvalueList);
		}

		EmrMedValue emrMedValue = emrmedvalueList.stream().findFirst().get();
		emrMedValue = Optional.ofNullable(emrMedValue).orElse(new EmrMedValue());
		String pkPv = emrMedValue.getFather();
		Map<String,Object> map = new HashMap<>(16);
		map.put("pkPv",pkPv);
		PlatFormSendUtils.sendAdmissionAssessment(map);

	}
	/**
	 * 取消完成病历 
	 * 
	 * @param param
	 * @param user
	 */
	public void offFinishEmr(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sql="update emr_pat_rec set eu_status='1',pk_emp_finish=null,finish_date=null where pk_pv=?";
		DataBaseHelper.update(sql, new Object[]{map.get("pkPv")});
	}
	
	/**
	 * 保存专科护理记录单内容
	 * @param docXml
	 */
	private void saveNrDocTxt(EmrMedRec medRec,String docXml){
		try {
			Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
			Matcher matcher = pattern.matcher(docXml);
			if(matcher.find()){
				String str=matcher.group(1);
				docXml=docXml.replace(str,"");
			}
			
			SAXReader reader = new SAXReader();       
			org.dom4j.Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8")));
			Element root = document.getRootElement();
			Date now = new Date();
			List<Element> listElement=root.elements();//所有一级子节点的list
			for(Element e:listElement){//遍历所有一级子节点
				String nodeName=e.getName();
				if(nodeName!=null&&nodeName.equals("NewCtrl")){
					Element textNode=e.element("Content_Text");
					String deName=e.attributeValue("de_name");
					String date=e.attributeValue("date");
					String text = textNode.getTextTrim();
					
					saveEmrTxtData(medRec, now, null,deName, text,date);
				}
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 查询患者基本信息PID，PV1
	 * @param patMap
	 * @return
	 */
	public Map<String,Object> qryEmrpPatOp(String param, IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		Map<String,Object> map = new HashMap<>();
		//1、查询患者基本信息
		List<Map<String, Object>> pati = DataBaseHelper.queryForList("SELECT * FROM View_emr_pat_list_op WHERE pk_pv is not null and PK_PV =?", new Object[]{pkPv});
		if(null != pati && pati.size()>0){
			map.putAll(pati.get(0));
		}
		return map;
	}

	/**
	 * 同江医院查询HIS中病人病历记录
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryHisPatRecBac(String param, IUser user) {
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		return recMapper.queryHisPatRecBac(paramMap);
	}

	/**
	 * 保存同江拆分数据
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException
	 */
	public void saveDataTransHis(String param, IUser user) throws InvocationTargetException, IllegalAccessException {
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String sql = "";
		String pkrec = paramMap.get("pkRec").toString();
		Integer count=-1;
		if(paramMap!=null&&(paramMap.get("docName").toString().contains("24小时")||paramMap.get("docName").toString().contains("出院")))
		{
			sql = "select count(*) from EMR_OUT_HOSTOTAL_TJ where pk_rec = ?";
			count=DataBaseHelper.queryForScalar(sql, Integer.class, pkrec);
			EmrOutHosTolTj emrOutHosTolTj = new EmrOutHosTolTj();
			BeanUtils.copyProperties(emrOutHosTolTj, paramMap);
			if (count>0){
				DataBaseHelper.updateBeanByPk(emrOutHosTolTj, false);
			}else{
				DataBaseHelper.insertBean(emrOutHosTolTj);
			}
		}else{
			sql = "select count(*) from EMR_ADMIT_REC_TJ where pk_rec = ?";
 			count=DataBaseHelper.queryForScalar(sql, Integer.class, pkrec);
			EmrAdmitRecTj emrAdmitRecTj = new EmrAdmitRecTj();
			BeanUtils.copyProperties(emrAdmitRecTj, paramMap);
			if (count>0){
				DataBaseHelper.updateBeanByPk(emrAdmitRecTj, false);
			}else{
				DataBaseHelper.insertBean(emrAdmitRecTj);
			}
		}
	}
	
	public void SovleEmrData(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		if(map.containsKey("type")){
			String pkPv=null;
			if(map.containsKey("pkPv")){ pkPv=map.get("pkPv").toString(); }
			if(map.get("type").equals("sovleFirstCourse")){
				String pkRec=null;//记录正确首次病程的pk_rec
				if(pkPv!=null){
					List<EmrMedRec> allList=recMapper.queryMedRecList(pkPv);
					if(allList!=null && allList.size()>0){
						for (EmrMedRec emrMedRec : allList) {
							if(emrMedRec.getTypeCode().equals("100001")){
								pkRec=emrMedRec.getPkRec();
								break;
							}
						}
						if(pkRec!=null){
							String sql="update emr_med_rec set del_flag='1' where pk_pv=? and del_flag='0' and type_code='100001' and pk_rec!=?";
							DataBaseHelper.update(sql, new Object[]{pkPv,pkRec});
							/*sql="update emr_med_rec set del_flag='0' where pk_rec=?";
							DataBaseHelper.update(sql, new Object[]{pkRec});*/
						}
					}
				}
			}else if(map.get("type").equals("sovleQcXml")){
				if(pkPv!=null){
					String sql="select * from EMR_MED_REC where PK_PV=? and DEL_FLAG='0' and TYPE_CODE= '080001'";
					EmrMedRec emrMedRec=DataBaseHelper.queryForBean(sql, EmrMedRec.class, new Object[]{pkPv});
					if(emrMedRec!=null && emrMedRec.getPkDoc()!=null){
						sql="update emr.dbo.emr_med_doc set doc_data=doc_data_bak where pk_doc=?";
						DataBaseHelper.update(sql, new Object[]{emrMedRec.getPkDoc()});
					}
				}
			}else if(map.get("type").equals("sovleHomePage")){
				if(pkPv!=null){
					String pkPage=null;
					String sql="select * from emr_home_page where pk_pv=? and del_flag='0' order by create_time desc";
					List<EmrHomePage> homePageList=DataBaseHelper.queryForList(sql, EmrHomePage.class, new Object[]{pkPv});
					if(homePageList!=null && homePageList.size()>1){
						for (EmrHomePage emrHomePage : homePageList) {
							pkPage=emrHomePage.getPkPage();
							break;
						}
						if(pkPage!=null){
							sql="update emr_home_page set del_flag='1' where pk_pv=? and pk_page!=?";
							DataBaseHelper.update(sql, new Object[]{pkPv,pkPage});
						}
					}else if(homePageList==null || homePageList.size()<1){//没有正常首页进入
						String delsql="select top 1 * from emr_home_page where pk_pv=? and del_flag='1' order by create_time desc";
						List<EmrHomePage> delList=DataBaseHelper.queryForList(delsql, EmrHomePage.class, new Object[]{pkPv});
						if(delList!=null && delList.size()>0) {
							delsql="update emr_home_page set del_flag='0' where pk_page=?";
							DataBaseHelper.update(sql, new Object[]{delList.get(0).getPkPage()});
						}
					}
				}
			}
		}
	}
	
	public List<Map<String, Object>> qryPvInfo(String param , IUser user){
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> searchPv = recMapper.queryOpPatInfo(paramap);
        return searchPv;
    }

	/**
	 * 查询牙位图数据
	 */
	public List<Map<String,Object>> queryRecYwData(String param, IUser user) {
		String pkPi = JsonUtil.getFieldValue(param, "pkPi");
		String sql="select med.pk_id,med.code,med.text,med.name,BIAG_DATA from EMR_MED_VALUE med inner join pv_encounter pv on med.code=pv.pk_pv where CATEGORY='PeriodontalExamination' and med.del_flag='0'   and  pv.pk_pi=? ";
		List<Map<String, Object>> emrMedValue=DataBaseHelper.queryForList(sql, new Object[]{pkPi});
		return emrMedValue ;
	}
}
