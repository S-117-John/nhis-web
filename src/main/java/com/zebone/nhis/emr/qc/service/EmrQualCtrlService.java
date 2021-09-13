package com.zebone.nhis.emr.qc.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import sun.awt.AppContext;

import com.zebone.nhis.common.module.emr.qc.EmrAmendDoc;
import com.zebone.nhis.common.module.emr.qc.EmrAmendRec;
import com.zebone.nhis.common.module.emr.qc.EmrArchiveRec;
import com.zebone.nhis.common.module.emr.qc.EmrAuditDoc;
import com.zebone.nhis.common.module.emr.qc.EmrAuditRec;
import com.zebone.nhis.common.module.emr.qc.EmrEventRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeDept;
import com.zebone.nhis.common.module.emr.qc.EmrGradeItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRule;
import com.zebone.nhis.common.module.emr.qc.EmrGradeStandard;
import com.zebone.nhis.common.module.emr.qc.EmrGradeType;
import com.zebone.nhis.common.module.emr.qc.EmrMedRecTask;
import com.zebone.nhis.common.module.emr.qc.ViewEmrDeptList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrDeptList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.emr.common.EmrConstants;
import com.zebone.nhis.emr.common.EmrSaveUtils;
import com.zebone.nhis.emr.common.EmrUtils;
import com.zebone.nhis.emr.qc.dao.EmrQCMapper;
import com.zebone.nhis.emr.qc.vo.EmrDefectVo;
import com.zebone.nhis.emr.qc.vo.EmrGradeMsgItemVo;
import com.zebone.nhis.emr.qc.vo.EmrMedRecTaskVo;
import com.zebone.nhis.emr.qc.vo.EmrPatRecListVo;
import com.zebone.nhis.emr.rec.rec.dao.EmrRecMapper;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 病历质控服务
 * @author chengjia
 *
 */
@Service
public class EmrQualCtrlService {
	
	@Resource
	private	EmrQCMapper qcMapper;
	
	@Resource
	private	EmrRecMapper recMapper;
	
	/**
	 * 查询病历评分标准分类
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrGradeType> queryGradeTypeList(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrGradeType> rtnList=qcMapper.queryGradeTypeList();
		return rtnList;
	}
	
	/**
	 * 根据分类查询评分标准
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrGradeStandard> queryGradeStdByTypes(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<String> typeCodeList=null;
		String pkDept=null;
		String cateCode=null;
		if(map!=null){
			if(map.get("pkDept")!=null) pkDept=map.get("pkDept").toString();
			if(map.get("cateCode")!=null){cateCode=map.get("cateCode").toString();}
			String strs=map.get("typeCodeList")==null?"":map.get("typeCodeList").toString();
			if(strs!=null&&!strs.equals("")){
				String content = strs.replaceAll("\\b", "'");
				typeCodeList=JsonUtil.readValue(content, new TypeReference<List<String>>(){});
			}
		}
		
		List<EmrGradeStandard> rtnList=qcMapper.queryGradeStdByTypes(typeCodeList,pkDept,cateCode);
		return rtnList;
	}
	/**
	 * 根据分类查询评分标准(质控中心标准or专家教授标准)
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrGradeStandard> qryGradeStdByCenter(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<String> typeCodeList=null;
		String cateCode=null;
		if(map.get("cateCode")!=null){cateCode=map.get("cateCode").toString();}
		String strs=map.get("typeCodeList")==null?"":map.get("typeCodeList").toString();
		if(strs!=null&&!strs.equals("")){
			String content = strs.replaceAll("\\b", "'");
			typeCodeList=JsonUtil.readValue(content, new TypeReference<List<String>>(){});
		}
		List<EmrGradeStandard> rtnList=qcMapper.qryGradeStdByCenter(typeCodeList,cateCode);
		return rtnList;
	}
    /**
     * 根据条件查询评分标准
     * stdTypes/typeCodes/scoreXml/orderBy
     * @param map
     * @return
     */
    public List<EmrGradeStandard> queryGradeStdByConds(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	return qcMapper.queryGradeStdByConds(map);
    }
	
	
    /**
     * 查询病历评分记录
     * @param pkPv/euGradeType
     * @return
     */
    public List<EmrGradeRec> queryGradeRecByConds(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrGradeRec> rtnList=qcMapper.queryGradeRecByConds(map);
    	return rtnList;
    }

    /**
     * 查询病历评分记录明细
     * @param pkGraderec
     * @return
     */
    public List<EmrGradeItem> queryGradeItemByConds(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrGradeItem> rtnList=qcMapper.queryGradeItemByConds(map);
    	return rtnList;
    }

	/**
	 * 保存病历评分记录
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveEmrGradeRec(String param , IUser user){
		EmrGradeRec gradeRec=JsonUtil.readValue(param, EmrGradeRec.class);
		String status="";
		int rtn=0;
		if(StringUtils.isNoneEmpty(gradeRec.getStatus())) status= gradeRec.getStatus();
		
		if(status.equals(EmrConstants.STATUS_NEW)){
			rtn= qcMapper.saveEmrGradeRec(gradeRec);
		}else if(status.equals(EmrConstants.STATUS_UPD)){
			rtn= qcMapper.updateEmrGradeRec(gradeRec);
		}else if(status.equals(EmrConstants.STATUS_DEL)){
			rtn= qcMapper.deleteEmrGradeRec(gradeRec.getPkGraderec());
		}
		EmrGradeItem item=null;
		for (int i = 0; gradeRec.getItems()!=null&&i < gradeRec.getItems().size(); i++) {
			item=gradeRec.getItems().get(i);
			status="";
			if(StringUtils.isNoneEmpty(item.getStatus())) status= item.getStatus();
			if(item.getEuStatus()==null){item.setEuStatus("0");}
			if(status.equals(EmrConstants.STATUS_NEW)){
				rtn =qcMapper.saveEmrGradeItem(item);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				rtn= qcMapper.updateEmrGradeItem(item);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				rtn= qcMapper.deleteEmrGradeItem(item.getPkGradeitem());
			}
		}
	
	}  
	
    /**
     * 更新病历事件记录
     * @param map
     */
    public void updteEventRec(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	String sysDateStr=map.get("sysDate").toString();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  

    	try {
			Date sysDate=sdf.parse(sysDateStr);
			map.put("sysDate", sysDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		qcMapper.updteEventRec(map);
    }
    
    /**
     * 查询病历评分规则
     * @param dtRuleType
     * @return
     */
    public List<EmrGradeRule> queryGradeRuleByConds(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrGradeRule> rtnList=qcMapper.queryGradeRuleByConds(map);
    	return rtnList;
    }
    
    /**
     * 查询病历事件记录
     * @param pkPv/euEventStatus
     * @return
     */
    public List<EmrEventRec> queryEventRecByConds(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrEventRec> rtnList=qcMapper.queryEventRecByConds(map);
    	return rtnList;
    }
    
    
    
    /**
     * 查询病历事件记录DC
     * @param pkPv/euEventStatus
     * @return
     */
    public List<EmrEventRec> queryEventRecByCondsDC(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrEventRec> rtnList=qcMapper.queryEventRecByCondsDC(map);
    	return rtnList;
    }
    
    
    
    /**
     * 根据文档分类查询患者病历记录
     * @param pkPv
     * @param typeCode
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<EmrMedRec> queryMedRecListByType(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	
    	Date beginDate=null;
    	Date endDate=null;
    	
    	try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
    		if(map.get("beginDate")!=null){
    			String beginDateStr=map.get("beginDate").toString();
        		beginDate=sdf.parse(beginDateStr);
    		}
    		if(map.get("endDate")!=null){
    			String endDateStr=map.get("endDate").toString();
    			endDate=sdf.parse(endDateStr);
    		}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
    	
    	//List<EmrMedRec> rtnList=qcMapper.queryMedRecListByType(map);
    	List<EmrMedRec> rtnList =new ArrayList<EmrMedRec>();
		List<EmrMedRec> listData =new ArrayList<EmrMedRec>();
    	String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();
    	if(saveDataMode.equals("0")){//本地存储
			rtnList = qcMapper.queryMedRecListByType(map);
		}else if(saveDataMode.equals("1")){//分离存储
			rtnList = qcMapper.queryMedRecListByType(map);
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
     * 根据文档分类查询患者病历记录(DC)
     * @param pkPv
     * @param typeCode
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<EmrMedRec> queryMedRecListByTypeDC(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	
    	Date beginDate=null;
    	Date endDate=null;
    	
    	try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
    		if(map.get("beginDate")!=null){
    			String beginDateStr=map.get("beginDate").toString();
        		beginDate=sdf.parse(beginDateStr);
    		}
    		if(map.get("endDate")!=null){
    			String endDateStr=map.get("endDate").toString();
    			endDate=sdf.parse(endDateStr);
    		}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
    	
		String Status=(String) map.get("orderTypeStatus");
		if (Status != null && Status.length() != 0) {
		if(Status.equals("seqNo")){
			map.put("orderTypeStatus", " order by rec.seq_no desc");
		}else if(Status.equals("createTime")){
			map.put("orderTypeStatus", " order by rec.create_time");
		}else{
			Status=null;
		}
		}
		
		
		
    	List<EmrMedRec> rtnList=qcMapper.queryMedRecListByTypeDC(map);
    	return rtnList;
    }
    
    
    
    
    
    
    /**
     * 根据条件查询病历书写任务
     * @param pkPv
     * @param ruleCode
     * @param kEvtrec
     * @return
     */
    public List<EmrMedRecTask> queryMedRecTaskByConds(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrMedRecTask> rtnList=qcMapper.queryMedRecTaskByConds(map);
    	return rtnList;
    }
    
	/**
	 * 保存病历书写任务
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveEmrMedRecTasks(String param , IUser user){
		List<EmrMedRecTask> list=new ArrayList<EmrMedRecTask>();
		
		if(param!=null&&!param.equals("")){
			list=JsonUtil.readValue(param, new TypeReference<List<EmrMedRecTask>>(){});
		}
		for (int i = 0; i < list.size(); i++) {
			EmrMedRecTask item=list.get(i);
			String status=item.getStatus();
			if(status==null) status="";
			if(status.equals(EmrConstants.STATUS_NEW)){
				qcMapper.saveEmrMedRecTask(item);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				qcMapper.updateEmrMedRecTask(item);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				qcMapper.deleteEmrMedRecTask(item.getPkTask());
			}
		}
	
	} 
	
	/**
	 * 查询病历质控评分
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryPatGradeScore(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<Map> rtnList=new ArrayList<Map>();
		if(Application.isSqlServer()){
			rtnList=qcMapper.queryPatGradeScoreSql(map);
		}else{
	    	qcMapper.queryPatGradeScore(map);
	    	rtnList=(List<Map>)map.get("result");
		}
    	return rtnList;
	}
	
    /**
     * 查询病历评分消息记录
     * @param pkPv/euMsgType
     * @return
     */
    public List<EmrGradeMsgRec> queryGradeMsgRecByConds(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrGradeMsgRec> rtnList=qcMapper.queryGradeMsgRecByConds(map);
    	return rtnList;
    }

    /**
     * 查询病历评分消息明细
     * @param pkGradeMsgrec
     * @return
     */
    public List<EmrGradeMsgItem> queryGradeMsgItemByConds(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrGradeMsgItem> rtnList=qcMapper.queryGradeMsgItemByConds(map);
    	return rtnList;
    }
    
	/**
	 *  保存病历评分消息记录
	 * @param param
	 * @param user
	 */
	public void saveEmrGradeMsgRec(String param , IUser user){
		EmrGradeMsgRec msgRec=JsonUtil.readValue(param, EmrGradeMsgRec.class);
		String statusRec="";
		if(StringUtils.isNoneEmpty(msgRec.getStatus())) statusRec= msgRec.getStatus();
		
		if(statusRec.equals(EmrConstants.STATUS_NEW)){
			 qcMapper.saveEmrGradeMsgRec(msgRec);
		}else if(statusRec.equals(EmrConstants.STATUS_UPD)){
			 qcMapper.updateEmrGradeMsgRec(msgRec);
		}else if(statusRec.equals(EmrConstants.STATUS_DEL)){
			 qcMapper.deleteEmrGradeMsgRec(msgRec.getPkGradeMsgrec());
		}
		String status="";
		EmrGradeMsgItem item=null;
		for (int i = 0; msgRec.getItems()!=null&&i < msgRec.getItems().size(); i++) {
			item=msgRec.getItems().get(i);
			status="";
			if(StringUtils.isNoneEmpty(item.getStatus())) status= item.getStatus();
			
			if(status.equals(EmrConstants.STATUS_NEW)){
				qcMapper.saveEmrGradeMsgItem(item);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				qcMapper.updateEmrGradeMsgItem(item);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				qcMapper.deleteEmrGradeMsgItem(item.getPkGradeMsgitem());
			}
		}
		//新增、修改时同步评分记录发送标志
		if(statusRec.equals(EmrConstants.STATUS_NEW)||statusRec.equals(EmrConstants.STATUS_UPD)){
			//发送标志
			if(msgRec.getFlagSend()!=null&&msgRec.getFlagSend().equals("1")){
				DataBaseHelper.update("update emr_grade_rec set flag_send='1' where pk_graderec=?", new Object[]{msgRec.getPkGraderec()});
			}
			//环节质控
			if(msgRec.getEuMsgType()!=null&&msgRec.getEuMsgType().equals("link")){
				EmrGradeRec gradeRec = qcMapper.queryGradeRecByPk(msgRec.getPkGraderec());
				if(gradeRec!=null){
					DataBaseHelper.update("update emr_pat_rec set eu_link_qc_grade=?,link_qc_score=?,pk_emp_link_qc=?,link_qc_date=?  "
							+ " where pk_pv=?", new Object[]{gradeRec.getEuGrade(),gradeRec.getScore(),gradeRec.getPkEmpQc(),gradeRec.getQcDate(),msgRec.getPkPv()});
				}
			}
		}
	}  
	
    /**
     * 查询病历评分结果
     * @param map
     * @return
     */
    public List<EmrGradeItem> queryGradeScoreByConds(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrGradeItem> rtnList=qcMapper.queryGradeScoreByConds(map);
    	return rtnList;
    }
    
    /**
     * 查询医师病历书写任务
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<EmrMedRecTaskVo> queryEmpTaskList(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	
    	qcMapper.queryEmpTaskList(map);
    	List<EmrMedRecTaskVo> rtnList=(List<EmrMedRecTaskVo>)map.get("result");
    	
    	//System.out.println("queryEmpTaskList:"+rtnList.size());
    	return rtnList;
    }
    
	/**
	 * 保存病历审签记录
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveEmrAuditRec(String param , IUser user){
		EmrAuditRec rec=JsonUtil.readValue(param, EmrAuditRec.class);
		String status="";
		int rtn=0;
		int row=0;
		if(StringUtils.isNoneEmpty(rec.getStatus())) status= rec.getStatus();
		//病历存储数据模式：0、本地存储（空或未设置）1、独立存储（只是数据文件分开存储）
		String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();
		
		if(status.equals(EmrConstants.STATUS_NEW)){
			rtn= qcMapper.saveEmrAuditRec(rec);
		}else if(status.equals(EmrConstants.STATUS_UPD)){
			rtn= qcMapper.updateEmrAuditRec(rec);
		}else if(status.equals(EmrConstants.STATUS_DEL)){
			rtn= qcMapper.deleteEmrAuditRec(rec.getPkAudit());
		}
		EmrAuditDoc doc=rec.getDoc();
		status="";
		if(StringUtils.isNoneEmpty(doc.getStatus())) status= doc.getStatus();
		doc.setDbName(dbName);
		byte[] expData=null;

		if(status.equals(EmrConstants.STATUS_NEW)){
			rtn =qcMapper.saveEmrAuditDoc(doc);
			if(saveDataMode.equals("0")){//本地存储
				qcMapper.saveEmrAuditDoc(doc);
			}else if(saveDataMode.equals("1")){//独立存储
				qcMapper.saveEmrAuditDocEmr(doc);
				doc.setDocData(null);
				doc.setDocDataBak(null);
				qcMapper.saveEmrAuditDoc(doc);
			}
		}else if(status.equals(EmrConstants.STATUS_UPD)){
			//rtn= qcMapper.updateEmrAuditDoc(doc);
			if(saveDataMode.equals("0")){//本地存储
				qcMapper.updateEmrAuditDoc(doc);
			}else if(saveDataMode.equals("1")){//独立存储
				row=qcMapper.updateEmrAuditDocEmr(doc);
				if(row<=0){
					qcMapper.saveEmrAuditDocEmr(doc);
				}
				doc.setDocData(null);
				doc.setDocDataBak(null);
				qcMapper.updateEmrAuditDoc(doc);
			}
		}else if(status.equals(EmrConstants.STATUS_DEL)){
			//rtn= qcMapper.deleteEmrAuditDoc(doc.getPkAuditDoc());
			if(saveDataMode.equals("0")){//本地存储
				qcMapper.deleteEmrAuditDoc(doc.getPkAuditDoc());
			}else if(saveDataMode.equals("1")){//独立存储
				qcMapper.deleteEmrAuditDocEmr(doc.getPkAuditDoc(),dbName);
				qcMapper.deleteEmrAuditDoc(doc.getPkAuditDoc());
			}
		}
	
	}  

	/**
	 * 保存病历审签记录
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveEmrAuditRec(EmrMedRec medRec,EmrMedDoc medDoc,String pkEmp){
		if(medRec==null||medDoc==null) return;
		String operateType=medRec.getOperateType();
		if(operateType==null) operateType="";
		String strs="submit,audit,recall,return";
		if(strs.indexOf(operateType)<0) return;

		String euDocStatus=medRec.getEuDocStatus();//0:书写1：住院医师2：主治医师	3：主任医师
		if(euDocStatus==null) euDocStatus="";
		if(euDocStatus.equals("0")&&!operateType.equals("recall")&&!operateType.equals("submit")&&!operateType.equals("audit")&&!operateType.equals("return")) return;
		
		Map map=new HashMap<String, String>();
		map.put("pkPv", medRec.getPkPv());
		map.put("pkRec", medRec.getPkRec());
		String euLevel=euDocStatus;
		int euLevelNew=Integer.parseInt(euLevel);
		//int euLevelQry=euLevelNew;
		if(operateType.equals("recall")){
			euLevelNew=Integer.parseInt(euLevel);
			map.put("euLevelGreaterDesc", euLevelNew);
			
		}else if(operateType.equals("audit")){
			euLevelNew=Integer.parseInt(euLevel);
			if(euLevelNew==0) euLevelNew=1;
			map.put("euLevel", euLevelNew);
		}else if(operateType.equals("submit")){
			//提交
			euLevelNew=euLevelNew+1;
			map.put("euLevelLess", euLevelNew);
		}else if(operateType.equals("return")){
			euLevelNew=Integer.parseInt(euLevel);
			map.put("euLevelGreater", euLevelNew);
		}
		Date now=new Date();
		List<EmrAuditRec> list=qcMapper.queryEmrAuditRec(map);
		EmrAuditRec rec=null;
		if(list!=null&&list.size()>0) rec=list.get(0);
		EmrAuditDoc doc=null; 
		EmrAuditRec recNew=null;
		EmrAuditDoc docNew=null; 
		if(rec==null){
			//提交/无记录
			rec=createEmrAuditRec(medRec,medDoc,euLevel,pkEmp);
			rec.setEuLevel(Integer.toString(euLevelNew));
			doc=createEmrAuditDoc(rec.getPkAudit(),medDoc);
			
			rec.setDoc(doc);
		}else{
			rec.setStatus(EmrConstants.STATUS_UPD);
			//原有记录处理完成、新增审签记录
			if(operateType.equals("submit")){
				rec.setDateFinish(now);
				rec.setEuStatus("2");//已审
				rec.setPkEmpAudit(pkEmp);
				rec.setContent(rec.getContent()+"-完成");
				if(euLevelNew<=3){
					recNew=createEmrAuditRec(medRec,medDoc,Integer.toString(euLevelNew),pkEmp);
					
					docNew=createEmrAuditDoc(rec.getPkAudit(),medDoc);
					
					recNew.setDoc(docNew);
				}
				
			}else if(operateType.equals("audit")){
				rec.setDateReceive(now);
				rec.setEuStatus("1");//在审
				rec.setPkEmpAudit(pkEmp);
				rec.setContent(rec.getContent()+"-接收");
			}else if(operateType.equals("recall")){
//				rec.setDateReceive(now);
//				rec.setEuStatus("1");//在审
//				rec.setPkEmpAudit(pkEmp);
				rec.setContent(rec.getContent()+"-撤回");
				if(rec.getEuLevel().equals("3")&&rec.getEuStatus().equals("2")){
					rec.setEuStatus("1");
					rec.setDateFinish(null);
				}else{
					rec.setDelFlag("1");
				}
			}else if(operateType.equals("return")){
//				rec.setDateReceive(null);
//				rec.setEuStatus("0");
//				rec.setContent(rec.getContent()+"-退回");
//				rec.setDelFlag("1");
			}
		}
		
		String status="";
		int rtn=0;
		if(StringUtils.isNoneEmpty(rec.getStatus())) status= rec.getStatus();
		
		if(status.equals(EmrConstants.STATUS_NEW)){
			rtn= qcMapper.saveEmrAuditRec(rec);
		}else if(status.equals(EmrConstants.STATUS_UPD)){
			rtn= qcMapper.updateEmrAuditRec(rec);
		}else if(status.equals(EmrConstants.STATUS_DEL)){
			rtn= qcMapper.deleteEmrAuditRec(rec.getPkAudit());
		}
		
		String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();
		int row=0;
		doc=rec.getDoc();
		if(doc!=null){
			status="";
			if(StringUtils.isNoneEmpty(doc.getStatus())) status= doc.getStatus();
			doc.setDbName(dbName);
			if(status.equals(EmrConstants.STATUS_NEW)){
				if(saveDataMode.equals("0")){//本地存储
					qcMapper.saveEmrAuditDoc(doc);
				}else if(saveDataMode.equals("1")){//
					qcMapper.saveEmrAuditDocEmr(doc);
					doc.setDocData(null);
					doc.setDocDataBak(null);
					qcMapper.saveEmrAuditDoc(doc);
				}
			}else if(status.equals(EmrConstants.STATUS_UPD)){
//				rtn= qcMapper.updateEmrAuditDoc(doc);
//				if(dbName!=null&&!dbName.equals("")){
//					rtn= qcMapper.updateEmrAuditDocEmr(doc);
//				}
				if(saveDataMode.equals("0")){//本地存储
					qcMapper.updateEmrAuditDoc(doc);
				}else if(saveDataMode.equals("1")){//
					row = qcMapper.updateEmrAuditDocEmr(doc);
					if(row<=0){
						qcMapper.saveEmrAuditDocEmr(doc);
					}
					doc.setDocData(null);
					doc.setDocDataBak(null);
					qcMapper.updateEmrAuditDoc(doc);
				}
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				//rtn= qcMapper.deleteEmrAuditDoc(doc.getPkAuditDoc());
				if(saveDataMode.equals("0")){//本地存储
					qcMapper.deleteEmrAuditDoc(doc.getPkAuditDoc());
				}else if(saveDataMode.equals("1")){//
					qcMapper.deleteEmrAuditDocEmr(doc.getPkAuditDoc(),dbName);
					qcMapper.deleteEmrAuditDoc(doc.getPkAuditDoc());
				}
			}
		}
	
		//新记录
		if(recNew!=null){
			if(StringUtils.isNoneEmpty(recNew.getStatus())) status= recNew.getStatus();
			
			if(status.equals(EmrConstants.STATUS_NEW)){
				rtn= qcMapper.saveEmrAuditRec(recNew);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				rtn= qcMapper.updateEmrAuditRec(recNew);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				rtn= qcMapper.deleteEmrAuditRec(recNew.getPkAudit());
			}
			
		}
		if(docNew!=null){
			status="";
			if(StringUtils.isNoneEmpty(docNew.getStatus())) status= docNew.getStatus();
			
			docNew.setDbName(dbName);
			if(status.equals(EmrConstants.STATUS_NEW)){
				//rtn =qcMapper.saveEmrAuditDoc(docNew);
				if(saveDataMode.equals("0")){//本地存储
					qcMapper.saveEmrAuditDoc(docNew);
				}else if(saveDataMode.equals("1")){//
					qcMapper.saveEmrAuditDocEmr(docNew);
					docNew.setDocData(null);
					docNew.setDocDataBak(null);
					qcMapper.saveEmrAuditDoc(docNew);
				}
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				//rtn= qcMapper.updateEmrAuditDoc(docNew);
				row = qcMapper.updateEmrAuditDoc(docNew);
				if(row<=0){
					qcMapper.saveEmrAuditDocEmr(docNew);
				}
				docNew.setDocData(null);
				docNew.setDocDataBak(null);
				qcMapper.updateEmrAuditDoc(docNew);
				
				if(saveDataMode.equals("0")){//本地存储
					qcMapper.updateEmrAuditDoc(docNew);
				}else if(saveDataMode.equals("1")){//独立存储
					row = qcMapper.updateEmrAuditDoc(docNew);
					if(row<=0){
						//兼容并行期/存储库无记录则插入
						qcMapper.saveEmrAuditDocEmr(docNew);
					}
					docNew.setDocData(null);
					docNew.setDocDataBak(null);
					qcMapper.updateEmrAuditDoc(docNew);;
				}
				
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				//rtn= qcMapper.deleteEmrAuditDoc(docNew.getPkAuditDoc());
				if(saveDataMode.equals("0")){//本地存储
					qcMapper.deleteEmrAuditDoc(docNew.getPkAuditDoc());
				}else if(saveDataMode.equals("1")){//
					qcMapper.deleteEmrAuditDocEmr(docNew.getPkAuditDoc(),dbName);
					qcMapper.deleteEmrAuditDoc(docNew.getPkAuditDoc());
				}
			}
		}
	}  
	
	public EmrAuditRec createEmrAuditRec(EmrMedRec medRec,EmrMedDoc medDoc,String euLevel,String pkEmp){
		EmrAuditRec rec=new EmrAuditRec();
		String pkAudit=NHISUUID.getKeyId();
		rec.setPkAudit(pkAudit);
		rec.setPkRec(medRec.getPkRec());
		rec.setPkOrg(medDoc.getPkOrg());
		rec.setPkPv(medRec.getPkPv());
		rec.setDateApp(new Date());
		rec.setContent("审签");
		rec.setPkEmpApp(pkEmp);
		rec.setEuStatus("0");
		rec.setDelFlag("0");
		rec.setEuLevel(euLevel);
		rec.setStatus(EmrConstants.STATUS_NEW);	
		
		return rec;
	}
	
	public EmrAuditDoc createEmrAuditDoc(String pkAudit,EmrMedDoc medDoc){
		EmrAuditDoc doc=new EmrAuditDoc();
		String pkAuditDoc=NHISUUID.getKeyId();
		doc.setPkAuditDoc(pkAuditDoc);
		doc.setPkAudit(pkAudit);
		doc.setDocXml(medDoc.getDocXml());
		//2019-04-17屏蔽存储bak数据量过大
		//doc.setDocXmlBak(medDoc.getDocXml());
		doc.setDocData(medDoc.getDocData());
		//2019-04-17
		//doc.setDocDataBak(medDoc.getDocData());
		doc.setRemark(null);
		doc.setDelFlag("0");
		doc.setStatus(EmrConstants.STATUS_NEW);
		
		return doc;
	}
	/**
	 * 保存病历整改记录
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveEmrAmendRec(String param , IUser user){
		List<EmrAmendRec> list=JsonUtil.readValue(param, new TypeReference<List<EmrAmendRec>>(){});
		String status="";
		int rtn=0;
		int row=0;
		//病历存储数据模式：0、本地存储（空或未设置）1、独立存储（只是数据文件分开存储）
		String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();
		for (EmrAmendRec rec : list) {
			status="";
			rtn=0;
			if(StringUtils.isNoneEmpty(rec.getStatus())) status= rec.getStatus();
			
			if(status.equals(EmrConstants.STATUS_NEW)){
				rtn= qcMapper.saveEmrAmendRec(rec);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				rtn= qcMapper.updateEmrAmendRec(rec);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				rtn= qcMapper.deleteEmrAmendRec(rec.getPkAmend());
			}
			EmrAmendDoc doc=rec.getDoc();
			if(doc!=null){
				status="";
				doc.setDbName(dbName);
				if(StringUtils.isNoneEmpty(doc.getStatus())) status= doc.getStatus();
				
				if(status.equals(EmrConstants.STATUS_NEW)){
					if(saveDataMode.equals("0")){//本地存储
						qcMapper.saveEmrAmendDoc(doc);
					}else if(saveDataMode.equals("1")){//独立存储
						qcMapper.saveEmrAmendDocEmr(doc);
						doc.setDocData(null);
						doc.setDocDataBak(null);
						qcMapper.saveEmrAmendDoc(doc);
					}
					//rtn =qcMapper.saveEmrAmendDoc(doc);

				}else if(status.equals(EmrConstants.STATUS_UPD)){
					//rtn= qcMapper.updateEmrAmendDoc(doc);
					if(saveDataMode.equals("0")){//本地存储
						qcMapper.updateEmrAmendDoc(doc);
					}else if(saveDataMode.equals("1")){//独立存储
						row = qcMapper.updateEmrAmendDocEmr(doc);
						if(row<=0){
							qcMapper.saveEmrAmendDocEmr(doc);
						}
						doc.setDocData(null);
						doc.setDocDataBak(null);
						qcMapper.updateEmrAmendDoc(doc);
					}
					
				}else if(status.equals(EmrConstants.STATUS_DEL)){
					//rtn= qcMapper.deleteEmrAmendDoc(doc.getPkAmendDoc());
					if(saveDataMode.equals("0")){//本地存储
						qcMapper.deleteEmrAmendDoc(doc.getPkAmendDoc());
					}else if(saveDataMode.equals("1")){//独立存储
						qcMapper.deleteEmrAmendDocEmr(doc.getPkAmendDoc(),dbName);
						qcMapper.deleteEmrAmendDoc(doc.getPkAmendDoc());
					}
				}
			}
			
			EmrMedRec medRec=rec.getRec();
			if(medRec!=null){
				status="";
				if(StringUtils.isNoneEmpty(medRec.getStatus())) status= medRec.getStatus();

				if(status.equals(EmrConstants.STATUS_NEW)){
					rtn= recMapper.saveEmrMedRec(medRec);
				}else if(status.equals(EmrConstants.STATUS_UPD)){
					rtn= recMapper.updateEmrMedRec(medRec);
				}else if(status.equals(EmrConstants.STATUS_DEL)){
					rtn= recMapper.deleteEmrMedRec(medRec.getPkRec());
				}
				EmrMedDoc medDoc=medRec.getMedDoc();
				byte[] expData=null;
				if(medDoc!=null){
					status="";
					if(StringUtils.isNoneEmpty(medDoc.getStatus())) status= medDoc.getStatus();
					if(status.equals(EmrConstants.STATUS_NEW)||status.equals(EmrConstants.STATUS_UPD)){
						expData = medDoc.getDocExpData();
					}
					medDoc.setDbName(dbName);
					if(status.equals(EmrConstants.STATUS_NEW)){
						//rtn =recMapper.saveEmrMedDoc(medDoc);
						if(saveDataMode.equals("0")){//本地存储
							recMapper.saveEmrMedDoc(medDoc);
						}else if(saveDataMode.equals("1")){//独立存储
							recMapper.saveEmrMedDocEmr(medDoc);
							medDoc.setDocData(null);
							medDoc.setDocDataBak(null);
							medDoc.setDocExpData(null);
							recMapper.saveEmrMedDoc(medDoc);
						}
					}else if(status.equals(EmrConstants.STATUS_UPD)){
						//rtn= recMapper.updateEmrMedDoc(medDoc);
						if(saveDataMode.equals("0")){//本地存储
							recMapper.updateEmrMedDoc(medDoc);
						}else if(saveDataMode.equals("1")){//独立存储
							row = recMapper.updateEmrMedDocEmr(medDoc);
							if(row<=0){
								recMapper.saveEmrMedDocEmr(medDoc);
							}
							medDoc.setDocData(null);
							medDoc.setDocDataBak(null);
							medDoc.setDocExpData(null);
							recMapper.updateEmrMedDoc(medDoc);
						}
					}else if(status.equals(EmrConstants.STATUS_DEL)){
						//rtn= recMapper.deleteEmrMedDoc(medDoc.getPkDoc());
						if(saveDataMode.equals("0")){//本地存储
							recMapper.deleteEmrMedDoc(medDoc.getPkDoc());
						}else if(saveDataMode.equals("1")){//独立存储
							recMapper.deleteEmrMedDocEmr(medDoc.getPkDoc(),dbName);
							recMapper.deleteEmrMedDoc(medDoc.getPkDoc());
						}
					}
					
					//新增和修改文档保存pdf、pic或html
					if(status.equals(EmrConstants.STATUS_NEW)||status.equals(EmrConstants.STATUS_UPD)){
						String path=EmrSaveUtils.saveEmrImageData(status, medDoc);
						if(path!=null&&!path.equals("")){
							DataBaseHelper.update("update emr_med_doc set file_path=? where pk_doc=?", new Object[]{path,medDoc.getPkDoc()});
						}
					}
					//存储病历文本数据
					medDoc.setDocExpData(expData);
					EmrSaveUtils.saveEmrExpData(medRec, medDoc, status);
				}
				
			}
			
		}
	}
	
	/**
	 * 查询病历整改记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrAmendRec> queryAmendRecList(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrAmendRec> rtnList=qcMapper.queryAmendRecList(map);
		return rtnList;
	}
	
	/**
	 * 查询病历整改记录文档
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrAmendRec getEmrAmendRec(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String pkAmend=map.get("pkAmend").toString();
		
		String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();
		EmrAmendRec rec = null;
		
		
		if(saveDataMode.equals("0")){//本地存储
			rec=qcMapper.getEmrAmendRec(pkAmend);
		}else if(saveDataMode.equals("1")){//独立存储/共同存储
			rec=qcMapper.getEmrAmendRecEmr(pkAmend,dbName);
			if(rec==null||rec.getDoc()==null||rec.getDoc().getDocData()==null) rec = qcMapper.getEmrAmendRec(pkAmend);
		}
		
		return rec;
	}

	/**
     * 查询病历评分消息明细记录
     * @param map
     * @return
     */
    public List<EmrGradeMsgItemVo> queryGradeMsgItem(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	return qcMapper.queryGradeMsgItem(map);
    }
    
    
    /**
	 * 保存病历评分消息明细记录
	 * @param param
	 * @param user
	 */
	public void saveEmrGradeMsgItem(String param , IUser user){
		List<EmrGradeMsgItem> itemList = JsonUtil.readValue(param, new TypeReference<List<EmrGradeMsgItem>>() {});
		for (int i = 0; itemList!=null&&i < itemList.size(); i++) {
			EmrGradeMsgItem item=itemList.get(i);
			String status="";
			if(StringUtils.isNoneEmpty(item.getStatus())) status= item.getStatus();
			
			if(status.equals(EmrConstants.STATUS_NEW)){
				qcMapper.saveEmrGradeMsgItem(item);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				qcMapper.updateEmrGradeMsgItem(item);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				qcMapper.deleteEmrGradeMsgItem(item.getPkGradeMsgitem());
			}
		}
	
	}  
	/**
	 * 保存病历评分标准科室引用
	 * @param param
	 * @param user
	 */
	public void saveEmrGradeDept(String param , IUser user){
		List<EmrGradeDept> itemList = JsonUtil.readValue(param, new TypeReference<List<EmrGradeDept>>() {});

		for(int i=0;itemList!=null && i<itemList.size();i++){
			EmrGradeDept emrGradeDept=itemList.get(i);
			String status=emrGradeDept.getStatus();
			if(status==null) status="";
			if(status.equals("new")){
				DataBaseHelper.insertBean(emrGradeDept);
			}else if(status.equals("upd")){
				DataBaseHelper.updateBeanByPk(emrGradeDept, false);
			}else if(status.equals("del")){
				DataBaseHelper.deleteBeanByPk(emrGradeDept);
			}
		}

	} 

	/**
	 * 查询系统分类下已维护科室
	 * @param param 
	 * @param user
	 * @return
	 */
	public List<EmrGradeDept> findHaveDept(String param , IUser user){
    	String cateCode = JsonUtil.getFieldValue(param, "cateCode")  ;
    	return qcMapper.findHaveDept(cateCode);
    }
	/**
	 * 查询除系统分类已维护科室的其他科室
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ViewEmrDeptList> findElseDept(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
    	return qcMapper.findElseDept(map);
    }
	/**
	 * 根据条件查询质控缺陷
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public List<EmrDefectVo> findDefectByParam(String param , IUser user) throws ParseException{
		Map map = JsonUtil.readValue(param,Map.class);
		//转换日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date qcb = sdf.parse(map.get("qcBegin").toString());
		Date qce = sdf.parse(map.get("qcEnd").toString());
		Calendar c = Calendar.getInstance(); 
		c.setTime(qce);
		c.add(Calendar.DAY_OF_MONTH,1); 
		Date endDate=c.getTime();
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("qcBegin", sdf.format(qcb));
		map.put("qcEnd", sdf.format(endDate));
		//声明一个新的集合
		List<EmrDefectVo> sumDefect=new ArrayList<EmrDefectVo>();
		List<EmrDefectVo> allDefect=qcMapper.findDefectByParam(map);
		String pkPv="";
		EmrDefectVo emrDefect=null;
		int defnum=1;
		int vetonum=1;
		String zong="100";
		String rex="\\d+(\\.\\d+)?";
		for (int i = 0; i < allDefect.size(); i++) {
			if(!allDefect.get(i).getPkPv().equals(pkPv)){
				emrDefect=new EmrDefectVo();
				defnum=1;
				vetonum=1;
				ApplicationUtils.copyProperties(emrDefect,allDefect.get(i));
				sumDefect.add(emrDefect);
				pkPv=allDefect.get(i).getPkPv();
				if(allDefect.get(i).getScore()!=null && allDefect.get(i).getScore().toString().matches(rex)){
					emrDefect.setDefectObj(defnum+"."+allDefect.get(i).getStandardName());
					defnum=defnum+1;
				}else{
					emrDefect.setVetoObj(vetonum+"."+allDefect.get(i).getStandardName());
					vetonum=vetonum+1;
				}
			}else{
				if(allDefect.get(i).getScore()!=null && allDefect.get(i).getScore().toString().matches(rex)){
					if(emrDefect.getScore().toString().matches(rex)){
						Double sum=Double.parseDouble(emrDefect.getScore())+Double.parseDouble(allDefect.get(i).getScore());
						emrDefect.setScore(sum.toString());
						Double de=Double.parseDouble(zong)-Double.parseDouble(emrDefect.getScore());
						emrDefect.setDeScore(de.toString());
					}
					if(emrDefect.getDefectObj()!=null){
						emrDefect.setDefectObj(emrDefect.getDefectObj().toString()+"\n"+defnum+"."+allDefect.get(i).getStandardName());
						defnum=defnum+1;
					}else{
						emrDefect.setDefectObj(defnum+"."+allDefect.get(i).getStandardName());
						defnum=defnum+1;
					}
				}else{
					if(emrDefect.getVetoObj()!=null){
						emrDefect.setVetoObj(emrDefect.getVetoObj().toString()+"\n"+vetonum+"."+allDefect.get(i).getStandardName());
						vetonum=vetonum+1;
					}else{
						emrDefect.setVetoObj(vetonum+"."+allDefect.get(i).getStandardName());
						vetonum=vetonum+1;
					}
				}
			}
		}
		for (EmrDefectVo emrDefectVo : sumDefect) {
			int opCount=qcMapper.findIsOpByPkPv(emrDefectVo.getPkPv(), null);
			if(opCount>0){
				emrDefectVo.setIsOp("1");
			}
			int opFourCount=qcMapper.findIsOpByPkPv(emrDefectVo.getPkPv(), "04");
			if(opFourCount>0){
				emrDefectVo.setIsFourOp("1");
			}
		}
    	return sumDefect;
    }
	public List<EmrDefectVo> findDefectByPkPv(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrDefectVo> allDefect=qcMapper.findDefectByPkPv(map);
		for (EmrDefectVo emrDefectVo : allDefect) {
			emrDefectVo.setStandardName("("+emrDefectVo.getTypeName()+")" +emrDefectVo.getStandardName());
		}
		return allDefect;
	}
	
	/**
	 * 根据主键更新质控明细的状态
	 * @param param
	 * @param user
	 */
	public void updDefectEuStatus(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		User u = (User) user;
		String pkEmpReceive=u.getId();
		Date date=new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String pkItem=map.get("pkGradeitem").toString();
		String sql="update EMR_GRADE_ITEM set EU_STATUS='1',REVISE_DATE=to_date('"+sf.format(date)+"','yyyymmddhh24miss'),pk_emp_revise='"+pkEmpReceive+"' where PK_GRADEITEM='"+pkItem+"'";
		DataBaseHelper.update(sql);
	}  
	
	public List<EmrDefectVo> findChangedDefect(String param , IUser user) throws ParseException{
		Map map = JsonUtil.readValue(param,Map.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date changeB = sdf.parse(map.get("chaDateBegin").toString());
		Date changeE = sdf.parse(map.get("chaDateEnd").toString());
		Calendar c = Calendar.getInstance(); 
		c.setTime(changeE);
		c.add(Calendar.DAY_OF_MONTH,1); 
		Date endDate=c.getTime();
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("chaBegin", sdf.format(changeB));
		map.put("chaEnd", sdf.format(endDate));
		List<EmrDefectVo> chagedDefect=qcMapper.findChangedDefect(map);
		return chagedDefect;
	}
	
	
	public List<EmrDeptList> findDept(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrDeptList> deptList=qcMapper.findDept(map);
		return deptList;
	}
	/**
	 * 保存病历归档记录
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveEmrArchiveRec(String param , IUser user){
		List<EmrArchiveRec> list=JsonUtil.readValue(param, new TypeReference<List<EmrArchiveRec>>(){});
		String status="";
		for (EmrArchiveRec rec : list) {
			status="";
			if(StringUtils.isNoneEmpty(rec.getStatus())) status= rec.getStatus();
			
			if(status.equals(EmrConstants.STATUS_NEW)){
				DataBaseHelper.insertBean(rec);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				DataBaseHelper.updateBeanByPk(rec);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				DataBaseHelper.deleteBeanByPk(rec);
			}
		}

	}	
	
	/**
	 * 批量更新病历评分记录
	 * @param param
	 * @param user
	 */
	public void saveEmrGradeRecList(String param , IUser user){
		List<EmrGradeRec> list=JsonUtil.readValue(param, new TypeReference<List<EmrGradeRec>>(){});
		String status="";
		String euUpdate="";
		String pkEmp="";
		String pkOrg = ((User)user).getPkOrg();
		List<String> pkPvs=new ArrayList<String>();
		Date qcDate=new Date();
		List<EmrArchiveRec> listArchive=new ArrayList<EmrArchiveRec>();
		EmrArchiveRec archiveRec;
		for (EmrGradeRec rec : list) {
			status="";
			euUpdate=rec.getEuUpdate();
			if(StringUtils.isNoneEmpty(rec.getStatus())) status= rec.getStatus();
			if(status.equals(EmrConstants.STATUS_NEW)){
				qcMapper.saveEmrGradeRec(rec);
				if(euUpdate!=null&&euUpdate.equals("1")){
					//批量质控
					pkPvs.add(rec.getPkPv());
					if(pkEmp==null||pkEmp.equals("")) pkEmp=rec.getPkEmpQc();
					archiveRec=new EmrArchiveRec();
					archiveRec.setPkArchive(NHISUUID.getKeyId());
					archiveRec.setPkOrg(pkOrg);
					archiveRec.setPkPv(rec.getPkPv());
					archiveRec.setPkPatrec(null);
					archiveRec.setPkEmpApp(rec.getPkEmpQc());
					archiveRec.setDateApply(qcDate);
					archiveRec.setEuStatus("0");
					archiveRec.setRemark("批量质控");
					archiveRec.setDelFlag("0");
					archiveRec.setStatus(EmrConstants.STATUS_NEW);
					
					listArchive.add(archiveRec);
				}
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				qcMapper.updateEmrGradeRec(rec);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				qcMapper.deleteEmrGradeRec(rec.getPkGraderec());
			}
		}
		if(pkPvs.size()>0){
			//批量更新患者病历记录状态（终末提交）
			qcMapper.updateEmrPatRecList(pkPvs, pkEmp, qcDate);
		}
		if(listArchive.size()>0){
			for (EmrArchiveRec emrArchiveRec : listArchive) {
				DataBaseHelper.insertBean(emrArchiveRec);
			}
		}
	}	
	
	/**
	 * 查询病历评分标准分类
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrPatRecListVo> queryPatArchList(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrPatRecListVo> rtnList=qcMapper.queryPatArchList(map);
		return rtnList;
	}
	/*
	 * 增加时限质控
	 * */
	public void updteEventRecForTimeLimit(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sysDateStr = map.get("sysDate").toString();
		String dateBeginStr = map.get("dateBegin").toString();
		String dateEndStr = map.get("dateEnd").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date sysDate = sdf.parse(sysDateStr);
			map.put("sysDate", sysDate);

			Date dateBegin = sdf.parse(dateBeginStr);
			map.put("dateBegin", dateBegin);

			Date dateEnd = sdf.parse(dateEndStr);
			map.put("dateEnd", dateEnd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		qcMapper.updteEventRecTimeLimit(map);
	}
	/**
	 * 查询时限类所哟患者的时间事件
	 * */
	public List<EmrEventRec> queryEventRecByCondsForTimeLimit(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if(map.get("pkPvs")!=null){
			String strPkPv = map.get("pkPvs").toString();
			String sql = "SELECT * FROM emr_event_rec where del_flag !='1' and pk_pv in("+strPkPv+")";
			List<EmrEventRec> rtnList = DataBaseHelper.queryForList(sql, EmrEventRec.class, new Object[]{}); //qcMapper.queryEventRecByConds(map);
			return rtnList;
		}
		return null;
	}

	/**
	 * 根据PK主键更新终末质控接收标志,接收人,时间
	 * @param param
	 * @param user
	 * @throws ParseException 
	 */
	public void updFinalRecv(String param, IUser user) throws ParseException {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkEmpReceive=map.get("pkEmpFinalQcRecv").toString();
		List<String> list=(List)map.get("pkPv");
		List<String> upSqls=new ArrayList<String>();
		if(map.containsKey("unReceive")){
			for (String li : list) {
				StringBuffer upSql=new StringBuffer("update emr_pat_rec set flag_final_recv=null,pk_emp_final_qc_recv =null,final_qc_recv_date=null  where pk_pv= '");
				upSql.append(li+"'");
				upSqls.add(upSql.toString());
			}
		}else{
			for (String li : list) {
				StringBuffer upSql=new StringBuffer("update emr_pat_rec set flag_final_recv='1',pk_emp_final_qc_recv ='"+pkEmpReceive+"',final_qc_recv_date=to_date('"+map.get("finalQcRecvDate")+"','yyyymmddhh24miss')  where pk_pv= '");
				upSql.append(li+"'");
				upSqls.add(upSql.toString());
			}
		}
		DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
		
	}
}

