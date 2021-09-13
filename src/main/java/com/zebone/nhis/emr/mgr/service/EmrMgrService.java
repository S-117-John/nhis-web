package com.zebone.nhis.emr.mgr.service;


import java.io.UnsupportedEncodingException;
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
import org.apache.ibatis.annotations.Param;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.emr.mgr.EmrBorrowDoc;
import com.zebone.nhis.common.module.emr.mgr.EmrBorrowRec;
import com.zebone.nhis.common.module.emr.mgr.EmrOpenEditDoc;
import com.zebone.nhis.common.module.emr.mgr.EmrOpenEditRec;
import com.zebone.nhis.common.module.emr.qc.EmrAmendRec;
import com.zebone.nhis.common.module.emr.qc.EmrEventRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRule;
import com.zebone.nhis.common.module.emr.qc.EmrGradeStandard;
import com.zebone.nhis.common.module.emr.qc.EmrGradeType;
import com.zebone.nhis.common.module.emr.qc.EmrMedRecTask;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDoctor;
import com.zebone.nhis.common.module.emr.rec.dict.EmrPatRec;
import com.zebone.nhis.common.module.emr.rec.dict.ViewEmrPatList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiagsIcd;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOpsIcd;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.emr.common.EmrConstants;
import com.zebone.nhis.emr.mgr.dao.EmrMgrMapper;
import com.zebone.nhis.emr.mgr.vo.EmrHomePageFeedBackIcd;
import com.zebone.nhis.emr.mgr.vo.EmrOpenEditRecParam;
import com.zebone.nhis.emr.mgr.vo.ParamVo;
import com.zebone.nhis.emr.mgr.vo.SaveEmrHomePageParam;
import com.zebone.nhis.emr.qc.dao.EmrQCMapper;
import com.zebone.nhis.emr.qc.vo.EmrMedRecTaskVo;
import com.zebone.nhis.emr.rec.dict.vo.EmrOpenDocPrarm;
import com.zebone.nhis.emr.rec.dict.vo.EmrPatListPrarm;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 病历管理服务
 * @author chengjia
 *
 */
@Service
public class EmrMgrService {
	
	@Resource
	private	EmrMgrMapper mgrMapper;
	
	
	/**
	 * 查询借阅记录
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EmrBorrowRec> queryBorrowRecList(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");

		List<EmrBorrowRec> rtnList=mgrMapper.queryBorrowRecList(map);
		return rtnList;
	}
	
	
	/**
	 * 查询借阅文档记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrBorrowDoc> queryBorrowDocList(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrBorrowDoc> rtnList=mgrMapper.queryBorrowDocList(map);
		return rtnList;
	}
	

	/**
	 * 保存借阅记录
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveEmrBorrowRec(String param , IUser user){
		EmrBorrowRec rec=JsonUtil.readValue(param, EmrBorrowRec.class);
		String status="";
		int rtn=0;
		if(StringUtils.isNoneEmpty(rec.getStatus())) status= rec.getStatus();
		
		if(status.equals(EmrConstants.STATUS_NEW)){
			rtn= mgrMapper.saveEmrBorrowRec(rec);
		}else if(status.equals(EmrConstants.STATUS_UPD)){
			rtn= mgrMapper.updateEmrBorrowRec(rec);
		}else if(status.equals(EmrConstants.STATUS_DEL)){
			rtn= mgrMapper.deleteEmrBorrowRec(rec.getPkBorrow());
		}
		if(rtn<=0) return;
		EmrBorrowDoc doc=null;
		for (int i = 0; rec.getDocs()!=null&&i < rec.getDocs().size(); i++) {
			doc=rec.getDocs().get(i);
			status="";
			if(StringUtils.isNoneEmpty(doc.getStatus())) status= doc.getStatus();
			
			if(status.equals(EmrConstants.STATUS_NEW)){
				rtn =mgrMapper.saveEmrBorrowDoc(doc);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				rtn= mgrMapper.updateEmrBorrowDoc(doc);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				rtn= mgrMapper.deleteEmrBorrowDoc(doc.getPkBorrowDoc());
			}
		}
	
	}  
	/**
	 * 查询首页上传失败的患者信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrPatList> queryErrorPagePiInfo(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String type=map.get("type").toString();
		List<EmrPatList> emrpatList=new ArrayList<EmrPatList>();
		String sql="";
		if(type.equals("one")){
			sql="select pat.* from view_emr_pat_list pat left outer join emr_operate_logs logs on logs.pk_pv=pat.pk_pv and logs.del_flag='0' where logs.code='page_upload' and pat.del_flag='0'";
			emrpatList=DataBaseHelper.queryForList(sql, EmrPatList.class);
		}else{
			sql="select * from view_emr_pat_list where EU_STATUS='4' and FLAG_PAGE_UPLOAD is null and DATE_END>=to_date('2019-09-01','yyyy-mm-dd') and DEL_FLAG='0'";
			emrpatList=DataBaseHelper.queryForList(sql, EmrPatList.class);
		}
		
		return emrpatList;
	}
	/**
	 * 根据患者查询病案上传的错误日志
	 * @param param
	 * @param user
	 * @return
	 */
	public String queryErrorMessageByPv(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String sql="select operate_txt from emr_operate_logs where pk_pv=? and del_flag='0'";
		String errorMessage=DataBaseHelper.queryForScalar(sql, String.class, map.get("pkPv"));
		return errorMessage;
	}
	/**
	 * 根据参数查询病历签收患者信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrPatListPrarm> querySignForInfo(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrPatListPrarm> signForList= mgrMapper.querySignForInfo(map);
		return signForList;
	}
	/**
	 * 根据PK主键更新签收标志,签收人,时间
	 * @param param
	 * @param user
	 * @throws ParseException 
	 */
	public void updSignForFlag(String param, IUser user) throws ParseException {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkEmpReceive=map.get("pkEmpReceive").toString();
		List<String> list=(List)map.get("pkPatRec");
		List<String> upSqls=new ArrayList<String>();
		for (String li : list) {
			StringBuffer upSql=new StringBuffer("update emr_pat_rec set flag_receive='1',pk_emp_receive ='"+pkEmpReceive+"',receive_date=to_date('"+map.get("receiveDate")+"','yyyymmddhh24miss')  where pk_patrec= '");
			upSql.append(li+"'");
			upSqls.add(upSql.toString());
		}
		DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
		
	}
	/**
	 * 根据住院号直接接收
	 * @param param
	 * @param user
	 * @throws ParseException
	 */
	public String updSignForFlagByPatNo(String param, IUser user) throws ParseException {
		Map map = JsonUtil.readValue(param, Map.class);
		String message="";
		List<EmrPatListPrarm> signForList= mgrMapper.querySignForInfo(map);
		if(signForList.size()>0){
			User u = (User) user;
			String pkEmpReceive=u.getPkEmp();
			List<String> upSqls=new ArrayList<String>();
			Date date=new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (EmrPatListPrarm li : signForList) {
				StringBuffer upSql=new StringBuffer("update emr_pat_rec set flag_receive='1',pk_emp_receive ='"+pkEmpReceive+"',receive_date=to_date('"+sf.format(date)+"','yyyymmddhh24miss')  where pk_patrec= '");
				upSql.append(li.getPkPatrec()+"'");
				upSqls.add(upSql.toString());
			}
			DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
			message="签收成功: "+signForList.get(0).getCodeIp()+" "+signForList.get(0).getName()+" "+signForList.get(0).getSexName();
		}
		else{
			message="此住院号没有需要签收的记录"; 
		}
		return message;
	}
	
	/**
	 * 根据主键取消签收标志
	 * @param param
	 * @param user
	 */
	public void updUnSignForFlag(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPatRec=map.get("pkPatRec").toString();
		System.out.println();
		if(!pkPatRec.equals("")){
			String sql="update emr_pat_rec set flag_receive='0',pk_emp_receive =null,receive_date=null where pk_patrec= ?";
			DataBaseHelper.update(sql, new Object[]{pkPatRec});
		}
		
	}
	
	/**
	 * 根据参数查询病历召回申请的患者信息
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public List<EmrOpenEditRecParam> queryApplyInfo(String param , IUser user) throws ParseException{
		Map map = JsonUtil.readValue(param,Map.class);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dateBegin=sf.parse(map.get("dateBegin").toString());
		Date dateEnd=sf.parse(map.get("dateEnd").toString());
		Calendar c = Calendar.getInstance(); 
		c.setTime(dateEnd);
		c.add(Calendar.DAY_OF_MONTH,1); 
		Date endDate=c.getTime();
		sf = new SimpleDateFormat("yyyy-MM-dd");
		sf.format(dateBegin);
		map.put("dateBegin", sf.format(dateBegin));
		map.put("dateEnd", sf.format(endDate));
		List<EmrOpenEditRecParam> ApplyList= mgrMapper.queryApplyInfo(map);
		return ApplyList;
	}
	/**
	 * 保存病历召回申请
	 * @param param
	 * @param user
	 * @throws ParseException 
	 */
	public void saveOrUpdRecall(String param, IUser user) throws ParseException{
		Map map = JsonUtil.readValue(param, Map.class);
		List<ParamVo> emrOpenDocList = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "rec"),new TypeReference<List<ParamVo>>() {});
		//根据pkpv查询患者的住院次数
		String sql="select ip_times from pv_ip where PK_PV=? and del_flag='0'";
		String times=DataBaseHelper.queryForScalar(sql, String.class, map.get("pkPv"));
		if(map.containsKey("pkEditRec")){
			String delRecSql="delete emr_open_edit_rec where pk_edit_rec=?";
			String delDocSql="delete emr_open_edit_doc where pk_edit_rec=?";
			DataBaseHelper.update(delRecSql, new Object[]{map.get("pkEditRec")});
			DataBaseHelper.update(delDocSql, new Object[]{map.get("pkEditRec")});
		}
		EmrOpenEditRec emrOpenEditRec=new EmrOpenEditRec();
		emrOpenEditRec.setPkPv(map.get("pkPv").toString());
		emrOpenEditRec.setTimes(times);
		emrOpenEditRec.setEuType("1");
		emrOpenEditRec.setPkEmpApply(map.get("pkEmpApply").toString());
		emrOpenEditRec.setPkDeptApply(map.get("pkDeptApply").toString());
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date applyDate=sf.parse(map.get("applyDate").toString());
		Date beginDate=sf.parse(map.get("beginDate").toString());
		Date endDate=sf.parse(map.get("endDate").toString());
		int limit=(int)((endDate.getTime() - beginDate.getTime()) / (1000*3600*24));
		emrOpenEditRec.setApplyDate(applyDate);
		emrOpenEditRec.setTimeLimit(limit);
		emrOpenEditRec.setBeginDate(beginDate);
		emrOpenEditRec.setEndDate(endDate);
		emrOpenEditRec.setApplyTxt(map.get("applyTxt").toString());
		emrOpenEditRec.setEuStatus("0");
		emrOpenEditRec.setDelFlag("0");
		DataBaseHelper.insertBean(emrOpenEditRec);
		//List<ParamVo> emrOpenDocList=(List<ParamVo>)map.get("rec");
		for (ParamVo vo : emrOpenDocList) {
			EmrOpenEditDoc emrOpenEditDoc=new EmrOpenEditDoc();
			emrOpenEditDoc.setPkEditRec(emrOpenEditRec.getPkEditRec());
			emrOpenEditDoc.setPkRec(vo.getPkRec());
			emrOpenEditDoc.setTypeCode(vo.getTypeCode());
			//emrOpenEditDoc.setEuEditType("2");
			emrOpenEditDoc.setEuEditType(vo.getEuEditType());
			emrOpenEditDoc.setEuType("1");
			emrOpenEditDoc.setPkEmpApply(map.get("pkEmpApply").toString());
			emrOpenEditDoc.setPkDeptApply(map.get("pkDeptApply").toString());
			emrOpenEditDoc.setApplyDate(applyDate);
			emrOpenEditDoc.setBeginDate(beginDate);
			emrOpenEditDoc.setEndDate(endDate);
			emrOpenEditDoc.setApplyTxt(map.get("applyTxt").toString());
			emrOpenEditDoc.setEuStatus("0");
			emrOpenEditDoc.setDelFlag("0");
			DataBaseHelper.insertBean(emrOpenEditDoc);
		}
	}
	
	/**
	 * 查询已经申请召回的患者病历
	 * @param param
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException 
	 */
	public Map<String,Object> queryOpenRecList(String param, IUser user) throws UnsupportedEncodingException, ParseException {
		Map map = JsonUtil.readValue(param, Map.class);
		Map<String,Object> paramMap = new HashMap<String,Object>();
		String pkPv = map.get("pkPv").toString();
		String pkEditRec=map.get("pkEditRec").toString();
		//String openSql="select * from emr_open_edit_rec where pk_pv=? and del_flag='0' and begin_date <= ? and end_date >= ?";
		String openSql="select * from emr_open_edit_rec where pk_pv=? and del_flag='0' and pk_edit_rec=?";
		EmrOpenEditRec emrOpenEditRec=null;
		/*Calendar calendar = Calendar.getInstance();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(new Date());
		Date begin =  sdf.parse(dateStr);
		Date end = sdf.parse(dateStr);*/

		List<EmrOpenEditRec> list = DataBaseHelper.queryForList(openSql, EmrOpenEditRec.class, new Object[] { pkPv,pkEditRec});
		if(list!=null&&list.size()>0){
			emrOpenEditRec=list.get(0);
		}
		paramMap.put("EmrOpenEditRec", emrOpenEditRec);
		if(emrOpenEditRec!=null){
			paramMap.put("beginDate", emrOpenEditRec.getBeginDate());
			paramMap.put("endDate", emrOpenEditRec.getEndDate());
			paramMap.put("applyTxt", emrOpenEditRec.getApplyTxt());
			String pkSql="select * from emr_open_edit_doc where pk_edit_rec=?";
			String typeNameSql="select name as type_name from emr_doc_type where CODE=?";
			List<EmrOpenEditDoc> openPkRec=DataBaseHelper.queryForList(pkSql, EmrOpenEditDoc.class, emrOpenEditRec.getPkEditRec());
			List<EmrMedRec> medRec=new ArrayList<EmrMedRec>();
			for (EmrOpenEditDoc pk : openPkRec) {
				if(pk.getPkRec()!=null){
					EmrMedRec emrMedRec=mgrMapper.queryOpenRecList(pk.getPkRec());
					emrMedRec.setEuEditType(pk.getEuEditType());
					medRec.add(emrMedRec);
				}else if(pk.getPkRec()==null && pk.getTypeCode()!=null){
					EmrMedRec emrMedRec=DataBaseHelper.queryForBean(typeNameSql, EmrMedRec.class, new Object[] { pk.getTypeCode()});
					emrMedRec.setEuEditType(pk.getEuEditType());
					medRec.add(emrMedRec);
				}
				
			}
			if(medRec!=null){paramMap.put("EmrOpenRec", medRec);}
		}
		return paramMap;
	}
	/**
	 * 删除未审批的召回申请
	 * @param param
	 * @param user
	 */
	public void updOffRecall(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		String pkEditRec=map.get("pkEditRec").toString();
		if(!pkEditRec.equals("")){
			String recSql="update emr_open_edit_rec set del_flag='1' where pk_edit_rec=?";
			DataBaseHelper.update(recSql, new Object[]{pkEditRec});
			String docSql="update emr_open_edit_doc set del_flag='1' where pk_edit_rec=?";
			DataBaseHelper.update(docSql, new Object[]{pkEditRec});
		}
		
	}
	/**
	 * 查询开放的文档分类
	 * @return
	 */
	public List<String> queryOpenTypeCode(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		List<String> typeList=mgrMapper.queryOpenTypeCode(map.get("pkPv").toString());
		return typeList;
	}
	/**
	 * 查询开放的文档病历
	 * @return
	 */
	public List<EmrOpenEditDoc> queryOpenEditDoc(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrOpenEditDoc> typeList=mgrMapper.queryOpenEditDoc(map.get("pkPv").toString());
		return typeList;
	}
	/**
	 * 根据查询条件查询患者病案列表
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException
	 */
	public List<EmrPatListPrarm> queryIcdByPrame(String param, IUser user) throws ParseException{
		Map map = JsonUtil.readValue(param, Map.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date tmpb = sdf.parse(map.get("beginDate").toString());
		Date tmpe = sdf.parse(map.get("endDate").toString());
		Calendar c = Calendar.getInstance(); 
		c.setTime(tmpe);
		c.add(Calendar.DAY_OF_MONTH,1); 
		Date endDate=c.getTime();
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("disBeginDate", sdf.format(tmpb));
		map.put("disEndDate", sdf.format(endDate));
		List<EmrPatListPrarm> icdList=mgrMapper.queryIcdByPrame(map);
		return icdList;
	}
	
	public Map<String,Object> querySumAmountByPkPv(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		String sql="select sum(amount_st) amount_st,sum(amount_insu) amount_insu from BL_SETTLE where PK_PV=? and flag_canc='0'";
		Map<String,Object> result=DataBaseHelper.queryForMap(sql, map.get("pkPv"));
		return result;
	}
	/**
	 * 查询诊断编码列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrHomePageDiagsIcd> queryDiagsIcd(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		if(map.containsKey("pkPv")){
			String selSql="select * from emr_home_page_diags_icd where pk_pv=?";
			List<EmrHomePageDiagsIcd> selList=DataBaseHelper.queryForList(selSql,EmrHomePageDiagsIcd.class, map.get("pkPv"));
			if(selList.size()>0){
				List<EmrHomePageDiagsIcd> zuHeDiagsIcdList=queryZuHeDiagsIcd(map.get("pkPv").toString());
				return zuHeDiagsIcdList;
			}else{
				List<EmrHomePageDiagsIcd> diagsList=mgrMapper.queryPageDiagsByPk(map.get("pkPv").toString());
				String pkIcd=NHISUUID.getKeyId();
				for (int i = 0; i < diagsList.size(); i++) {
					String [] str=diagsList.get(i).getDiagCode().split(" ");
					if(str.length>1){
						for (int j = 0; j < str.length; j++) {
							EmrHomePageDiagsIcd newHomePage=new EmrHomePageDiagsIcd();
							ApplicationUtils.copyProperties(newHomePage,diagsList.get(i));
							newHomePage.setPkPagediagIcd(pkIcd);
							newHomePage.setDiagCode(str[j]);
							newHomePage.setDiagCodeIcd(str[j]);
							if(j>0){
								String selNameSql="select DIAGNAME from BD_TERM_DIAG where DIAGCODE=? and DEL_FLAG='0'";
								String mDiagName=DataBaseHelper.queryForScalar(selNameSql, String.class, str[j]);
								if(mDiagName!=null){newHomePage.setDiagName(mDiagName);newHomePage.setDiagNameIcd(mDiagName);}
								newHomePage.setPkPagediagIcd(null);
								newHomePage.setPkParent(pkIcd);
								newHomePage.setFlagSub("1");
								newHomePage.setFlagPrimary("0");
								String subno=j-1+"";
								newHomePage.setSeqNoSub(subno);
								diagsList.add(i+j+1,newHomePage);
							}else{
								diagsList.add(i+j+1,newHomePage);
							}
							
						}
						diagsList.remove(i);
					}
					
				}
				saveHomePageDiagIcd(diagsList,map.get("pkPv").toString(),"first");
				List<EmrHomePageDiagsIcd> zuHeDiagsIcdList=queryZuHeDiagsIcd(map.get("pkPv").toString());
				return zuHeDiagsIcdList;
			}
		}
		return null;
	}
	/**
	 * 查询组合好的诊断
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrHomePageDiagsIcd> queryZuHeDiagsIcd(String pkPv){
		String sql="select * from emr_home_page_diags_icd where pk_pv=? and del_flag='0' and FLAG_SUB!='1' order by (case when flag_primary='2' then '0' else flag_primary end)desc, SEQ_NO,SEQ_NO_SUB";
		String sqlSub="select * from emr_home_page_diags_icd diag where diag.pk_pv=? and diag.del_flag='0' and FLAG_SUB='1' and SEQ_NO='1' and SEQ_NO_SUB='0'";
		List<EmrHomePageDiagsIcd> diagsIcdList=DataBaseHelper.queryForList(sql,EmrHomePageDiagsIcd.class, pkPv);
		EmrHomePageDiagsIcd subDiag=DataBaseHelper.queryForBean(sqlSub, EmrHomePageDiagsIcd.class, pkPv);
		if(subDiag!=null){diagsIcdList.add(1, subDiag);}
		return diagsIcdList;
	}
	/**
	 * 查询病案编码中首页的诊断
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrHomePage queryHomePage(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		/*String sql="select * from emr_home_page where pk_pv=? and del_flag='0'";
		EmrHomePage homePage=DataBaseHelper.queryForBean(sql, EmrHomePage.class, map.get("pkPv"));*/
		EmrHomePage homePage=mgrMapper.queryHomePage(map.get("pkPv").toString());
		return homePage;
	}
	
	/**
	 * 查询手术编码列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrHomePageOpsIcd> queryOpsIcd(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		if(map.containsKey("pkPv")){
			String selSql="select * from EMR_HOME_PAGE_OPS_ICD where PK_PV=?";
			List<EmrHomePageOpsIcd> selList=DataBaseHelper.queryForList(selSql,EmrHomePageOpsIcd.class, map.get("pkPv"));
			String sql="select * from EMR_HOME_PAGE_OPS_ICD where PK_PV=? and DEL_FLAG='0' order by SEQ_NO,SEQ_NO_SUB";
			if(selList.size()>0){
				List<EmrHomePageOpsIcd> opsIcdList=DataBaseHelper.queryForList(sql,EmrHomePageOpsIcd.class, map.get("pkPv"));
				return opsIcdList;
			}else{
				List<EmrHomePageOpsIcd> opsList=mgrMapper.queryPageOpsByPk(map.get("pkPv").toString());
				for (EmrHomePageOpsIcd emrHomePageOpsIcd : opsList) {
					if(CommonUtils.isEmptyString(emrHomePageOpsIcd.getAnesTypeCode()) || emrHomePageOpsIcd.getAnesTypeCode().equals("无")){
						emrHomePageOpsIcd.setAnesTypeCode(null);
					}
					if(CommonUtils.isEmptyString(emrHomePageOpsIcd.getIncisionTypeCode())){
						emrHomePageOpsIcd.setIncisionTypeCode(null);
					}
					if(CommonUtils.isEmptyString(emrHomePageOpsIcd.getAsaCode())){
						emrHomePageOpsIcd.setAsaCode(null);
					}
					
					if(CommonUtils.isEmptyString(emrHomePageOpsIcd.getHealGradeCode())){
						emrHomePageOpsIcd.setHealGradeCode(null);
					}
					if(CommonUtils.isEmptyString(emrHomePageOpsIcd.getGradeCode())){
						emrHomePageOpsIcd.setGradeCode(null);
					}
					if(CommonUtils.isEmptyString(emrHomePageOpsIcd.getNnisCode())){
						emrHomePageOpsIcd.setNnisCode(null);
					}
				}
				saveHomePageOpsIcd(opsList,map.get("pkPv").toString());
				List<EmrHomePageOpsIcd> opsIcdList=DataBaseHelper.queryForList(sql,EmrHomePageOpsIcd.class, map.get("pkPv"));
				return opsIcdList;
			}
		}
		return null;
	}
	public List<EmrHomePageFeedBackIcd> queryFeedIcd(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		if(map.containsKey("del") && map.get("del").toString().equals("1")){
			String delSql="delete EMR_HOME_PAGE_FEEDBACK_ICD where PK_FEEDBACK_ICD=?";
			DataBaseHelper.update(delSql, new Object[]{map.get("pkFeedIcd")});
		}
		if(map.containsKey("pkPv")){
			String selSql="select * from EMR_HOME_PAGE_FEEDBACK_ICD where PK_PV=? and del_flag='0'";
			List<EmrHomePageFeedBackIcd> selList=DataBaseHelper.queryForList(selSql,EmrHomePageFeedBackIcd.class, map.get("pkPv"));
			return selList;
		}
		return null;
	}
	/**
	 * 保存病案编码数据
	 * @param param
	 * @param user
	 */
	public void saveDiagAndOpsIcd(String param, IUser user){
		SaveEmrHomePageParam saveEmrHomePageParam = JsonUtil.readValue(param, SaveEmrHomePageParam.class);
		//保存诊断列表
		saveHomePageDiagIcd(saveEmrHomePageParam.getDiagsList(),saveEmrHomePageParam.getPkPv(),null);
		//保存手术列表
		saveHomePageOpsIcd(saveEmrHomePageParam.getOpsList(),saveEmrHomePageParam.getPkPv());
		//保存反馈列表
		saveHomePageFeedIcd(saveEmrHomePageParam.getFeedList());
		//保存病案编码的其他内容
		if(saveEmrHomePageParam.getPkPv()!=null){
			String sql="update EMR_HOME_PAGE set DIAG_FIT_CODE_OI=?,DIAG_FIT_CODE_CP=?,"+
					"DIAG_CODE_CLINIC_ICD=?,DIAG_NAME_CLINIC_ICD=?,DIAG_CODE_EXTC_IP_ICD=?,"+
					"DIAG_NAME_EXTC_IP_ICD=?,DIAG_CODE_PATHO_ICD=?,DIAG_NAME_PATHO_ICD=?,"+
					"PATHO_NO_ICD=?,FLAG_DRUG_ALLERGY_ICD=?,ALLERGIC_DRUG_ICD=?,PART_DISEASE=? "+
					" where PK_PV=? and del_flag='0'";
			DataBaseHelper.execute(sql, new Object[]{saveEmrHomePageParam.getDiagFitCodeOi(),saveEmrHomePageParam.getDiagFitCodeCp(),
					saveEmrHomePageParam.getDiagCodeClinicIcd(),saveEmrHomePageParam.getDiagNameClinicIcd(),saveEmrHomePageParam.getDiagCodeExtcIpIcd(),
					saveEmrHomePageParam.getDiagNameExtcIpIcd(),saveEmrHomePageParam.getDiagCodePathoIcd(),saveEmrHomePageParam.getDiagNamePathoIcd(),
					saveEmrHomePageParam.getPathoNoIcd(),saveEmrHomePageParam.getFlagDrugAllergyIcd(),saveEmrHomePageParam.getAllergicDrugIcd(),
					saveEmrHomePageParam.getPartDisease(),saveEmrHomePageParam.getPkPv()});//saveEmrHomePageParam.getPatSource(),saveEmrHomePageParam.getCurrAddrIcd(),
			//更新超时标志
			if(saveEmrHomePageParam.getFlagTimeoutCode()!=null && saveEmrHomePageParam.getFlagTimeoutCode().equals("1")){
				String TimeOutSql="update emr_pat_rec set flag_timeout_code='1' where pk_pv='"+saveEmrHomePageParam.getPkPv()+"'";
				DataBaseHelper.update(TimeOutSql, new Object[] {});
			}
		}
		
	}
	
	public void saveHomePageDiagIcdfir(String param, IUser user){
		List<EmrHomePageDiagsIcd> diagIcd = JsonUtil.readValue(param, new TypeReference<List<EmrHomePageDiagsIcd>>(){});
		saveHomePageDiagIcd(diagIcd,null,null);
	}
	public void saveHomePageOpsIcdfir(String param, IUser user){
		List<EmrHomePageOpsIcd> diagIcd = JsonUtil.readValue(param, new TypeReference<List<EmrHomePageOpsIcd>>(){});
		saveHomePageOpsIcd(diagIcd,null);
	}
	/**
	 * 保存住院病案首页-诊断-病案编码
	 * @param param
	 * @param user
	 */
	public void saveHomePageDiagIcd(List<EmrHomePageDiagsIcd> diagIcd,String pkPv,String first){
		if(diagIcd.size()>0){
			String sql="select count(1) from EMR_HOME_PAGE_DIAGS_ICD where PK_PAGEDIAG_ICD=?";
			for (EmrHomePageDiagsIcd DiagsIcd : diagIcd) {
				Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, DiagsIcd.getPkPagediagIcd());
				if(count>0){
					if(DiagsIcd.getPkPagediagIcd()!=null&& first==null){
						DataBaseHelper.updateBeanByPk(DiagsIcd);
					}
				}else{
					EmrHomePageDiagsIcd emrHomePageDiagsIcd=new EmrHomePageDiagsIcd();
					emrHomePageDiagsIcd.setPkPage(DiagsIcd.getPkPage());
					if(DiagsIcd.getPkPv()==null){
						emrHomePageDiagsIcd.setPkPv(pkPv);
					}else{
						emrHomePageDiagsIcd.setPkPv(DiagsIcd.getPkPv());
					}
					if(DiagsIcd.getPkPagediagIcd()!=null){emrHomePageDiagsIcd.setPkPagediagIcd(DiagsIcd.getPkPagediagIcd());}
					emrHomePageDiagsIcd.setSeqNo(DiagsIcd.getSeqNo());
					emrHomePageDiagsIcd.setSeqNoSub(DiagsIcd.getSeqNoSub());
					emrHomePageDiagsIcd.setDtDiagType(DiagsIcd.getDtDiagType());
					emrHomePageDiagsIcd.setPkDiag(DiagsIcd.getPkDiag());
					emrHomePageDiagsIcd.setDiagCode(DiagsIcd.getDiagCode());
					emrHomePageDiagsIcd.setDiagName(DiagsIcd.getDiagName());
					emrHomePageDiagsIcd.setDiagDesc(DiagsIcd.getDiagDesc());
					emrHomePageDiagsIcd.setAdmitCondCode(DiagsIcd.getAdmitCondCode());
					emrHomePageDiagsIcd.setAdmitCondName(DiagsIcd.getAdmitCondName());
					emrHomePageDiagsIcd.setFlagPrimary(DiagsIcd.getFlagPrimary());
					emrHomePageDiagsIcd.setDiagCodeIcd(DiagsIcd.getDiagCodeIcd());
					emrHomePageDiagsIcd.setDiagNameIcd(DiagsIcd.getDiagNameIcd());
					emrHomePageDiagsIcd.setRemarkIcd(DiagsIcd.getRemarkIcd());
					emrHomePageDiagsIcd.setPkParent(DiagsIcd.getPkParent());
					emrHomePageDiagsIcd.setDiagCodeSi(DiagsIcd.getDiagCodeSi());
					emrHomePageDiagsIcd.setDiagNameSi(DiagsIcd.getDiagNameSi());
					if(DiagsIcd.getFlagSub()==null){emrHomePageDiagsIcd.setFlagSub("0");}else{emrHomePageDiagsIcd.setFlagSub(DiagsIcd.getFlagSub());}
					DataBaseHelper.insertBean(emrHomePageDiagsIcd);
				}
			}
		}
	}
	
	public void saveHomePageFeedIcd(List<EmrHomePageFeedBackIcd> feedIcd){
		if(feedIcd.size()>0){
			String sql="select count(1) from EMR_HOME_PAGE_FEEDBACK_ICD where PK_FEEDBACK_ICD=?";
			for(EmrHomePageFeedBackIcd feed: feedIcd){
				Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, feed.getPkFeedbackIcd());
				if(count>0){
					if(feed.getPkFeedbackIcd()!=null){
						DataBaseHelper.updateBeanByPk(feed);
					}
				}else{
					EmrHomePageFeedBackIcd emrHomePageFeedBackIcd=new EmrHomePageFeedBackIcd();
					emrHomePageFeedBackIcd.setPkPage(feed.getPkPage());
					emrHomePageFeedBackIcd.setPkDept(feed.getPkDept());
					emrHomePageFeedBackIcd.setPkEmp(feed.getPkEmp());
					emrHomePageFeedBackIcd.setPkPv(feed.getPkPv());
					emrHomePageFeedBackIcd.setSeqNo(feed.getSeqNo());
					emrHomePageFeedBackIcd.setCode(feed.getCode());
					emrHomePageFeedBackIcd.setBackContent(feed.getBackContent());
					emrHomePageFeedBackIcd.setType(feed.getType());
					DataBaseHelper.insertBean(emrHomePageFeedBackIcd);
				}
			}
		}
	}
	/**
	 * 保存住院病案首页-手术-病案编码
	 * @param param
	 * @param user
	 */
	public void saveHomePageOpsIcd(List<EmrHomePageOpsIcd> opsIcd,String pkPv){
		if(opsIcd.size()>0){
			String sql="select count(1) from EMR_HOME_PAGE_OPS_ICD where PK_OPS_ICD=?";
			for (EmrHomePageOpsIcd OpsIcd : opsIcd) {
				Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, OpsIcd.getPkOpsIcd());
				if(count>0){
					if(OpsIcd.getPkOpsIcd()!=null){
						DataBaseHelper.updateBeanByPk(OpsIcd);
					}
				}else{
					EmrHomePageOpsIcd emrHomePageOpsIcd=new EmrHomePageOpsIcd();
					emrHomePageOpsIcd.setPkPage(OpsIcd.getPkPage());
					if(OpsIcd.getPkPv()==null){
						emrHomePageOpsIcd.setPkPv(pkPv);
					}else{
						emrHomePageOpsIcd.setPkPv(OpsIcd.getPkPv());
					}
					if(OpsIcd.getPkOpsIcd()!=null){emrHomePageOpsIcd.setPkOpsIcd(OpsIcd.getPkOpsIcd());}
					emrHomePageOpsIcd.setSeqNo(OpsIcd.getSeqNo());
					emrHomePageOpsIcd.setSeqNoSub(OpsIcd.getSeqNoSub());
					emrHomePageOpsIcd.setEuType(OpsIcd.getEuType());
					emrHomePageOpsIcd.setOpCode(OpsIcd.getOpCode());
					emrHomePageOpsIcd.setOpName(OpsIcd.getOpName());
					emrHomePageOpsIcd.setOpDate(OpsIcd.getOpDate());
					emrHomePageOpsIcd.setGradeCode(OpsIcd.getGradeCode());
					emrHomePageOpsIcd.setGradeName(OpsIcd.getGradeName());
					emrHomePageOpsIcd.setPkEmpOp(OpsIcd.getPkEmpOp());
					emrHomePageOpsIcd.setOpDocName(OpsIcd.getOpDocName());
					emrHomePageOpsIcd.setPkEmpOpi(OpsIcd.getPkEmpOpi());
					emrHomePageOpsIcd.setOpiName(OpsIcd.getOpiName());
					emrHomePageOpsIcd.setPkEmpOpii(OpsIcd.getPkEmpOpii());
					emrHomePageOpsIcd.setOpiiName(OpsIcd.getOpiiName());
					emrHomePageOpsIcd.setIncisionTypeCode(OpsIcd.getIncisionTypeCode());
					emrHomePageOpsIcd.setIncisionTypeName(OpsIcd.getIncisionTypeName());
					emrHomePageOpsIcd.setHealGradeCode(OpsIcd.getHealGradeCode());
					emrHomePageOpsIcd.setHealGradeName(OpsIcd.getHealGradeName());
					emrHomePageOpsIcd.setAnesTypeCode(OpsIcd.getAnesTypeCode());
					emrHomePageOpsIcd.setAnesTypeName(OpsIcd.getAnesTypeName());
					emrHomePageOpsIcd.setPkEmpAnes(OpsIcd.getPkEmpAnes());
					emrHomePageOpsIcd.setAnesDocName(OpsIcd.getAnesDocName());
					emrHomePageOpsIcd.setAsaCode(OpsIcd.getAsaCode());
					emrHomePageOpsIcd.setAsaName(OpsIcd.getAsaName());
					emrHomePageOpsIcd.setNnisCode(OpsIcd.getNnisCode());
					emrHomePageOpsIcd.setNnisName(OpsIcd.getNnisName());
					emrHomePageOpsIcd.setOpCodeIcd(OpsIcd.getOpCodeIcd());
					emrHomePageOpsIcd.setOpNameIcd(OpsIcd.getOpNameIcd());
					emrHomePageOpsIcd.setFlagElective(OpsIcd.getFlagElective());
					emrHomePageOpsIcd.setPkParent(OpsIcd.getPkParent());
					emrHomePageOpsIcd.setFlagExtra(OpsIcd.getFlagExtra());
					emrHomePageOpsIcd.setOpCodeSi(OpsIcd.getOpCodeSi());
					emrHomePageOpsIcd.setOpNameSi(OpsIcd.getOpNameSi());
					DataBaseHelper.insertBean(emrHomePageOpsIcd);
				}
			}
		}
	}
	/**
	 * 删除编码icd诊断信息
	 * @param param
	 * @param user
	 */
	public void delDiagsIcd(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		if(map.containsKey("pkPagediagIcd")){
			String delSql="update emr_home_page_diags_icd set del_flag='1' where pk_pagediag_icd=? or pk_parent=?";
			DataBaseHelper.update(delSql, new Object[]{map.get("pkPagediagIcd"),map.get("pkPagediagIcd")});
		}
	}
	/**
	 * 删除编码icd手术信息
	 * @param param
	 * @param user
	 */
	public void delOpsIcd(String param, IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		if(map.containsKey("pkOpsIcd")){
			String delSql="update emr_home_page_ops_icd set del_flag='1' where pk_ops_icd=?";
			DataBaseHelper.update(delSql, new Object[]{map.get("pkOpsIcd")});
		}
	}
	
    /**
     * 查询患者病历记录列表
     * @param map
     * @return
     */
    public List<EmrPatList> queryPatExtList(String param, IUser user){
    	Map map = JsonUtil.readValue(param, Map.class);
    	
    	return mgrMapper.queryPatExtList(map);
    }
    
    public List<EmrHomePageDiagsIcd> querySubDiagsByFather(String param, IUser user){
    	Map map = JsonUtil.readValue(param, Map.class);
    	String sql="select * from EMR_HOME_PAGE_DIAGS_ICD where PK_PV=? and PK_PARENT=? and FLAG_SUB='1' and DEL_FLAG='0'order by SEQ_NO_SUB";
    	List<EmrHomePageDiagsIcd> subDiagsList=DataBaseHelper.queryForList(sql,EmrHomePageDiagsIcd.class, map.get("pkPv"),map.get("pkParent"));
    	saveHomePageDiagIcd(subDiagsList,null,null);
    	if(map.get("seqNo")!=null)
    	{
    		String updSql="update EMR_HOME_PAGE_DIAGS_ICD set SEQ_NO=? where PK_PV=? and PK_PARENT=? and DEL_FLAG='0'";
    		DataBaseHelper.update(updSql,map.get("seqNo") , map.get("pkPv"),map.get("pkParent"));
    	}
    	return subDiagsList;
    }
    
    public List<EmrDoctor> queryDoctor(String param, IUser user)
    {
    	Map map = JsonUtil.readValue(param, Map.class);
    	List<EmrDoctor> doctorList=mgrMapper.queryDoctor(map);
    	return doctorList;
    }
    /**
     * 根据科室编码查询科室pk
     * @param param
     * @param user
     * @return
     */
    public String queryDeptInfoByCode(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String sql="select pk_dept from BD_OU_DEPT where CODE_DEPT=?";
		String pkDept=DataBaseHelper.queryForScalar(sql, String.class, map.get("codeDept"));
		return pkDept;
	}
    
    public void updFlagDiagFitOp(String param , IUser user){
    	Map map = JsonUtil.readValue(param, Map.class);
    	String sql="update emr_home_page set diag_fit_ops=?,coded_not_cnfrm=?,labor_op_upload=? where pk_pv=? and del_flag='0'";
    	DataBaseHelper.update(sql, new Object[]{map.get("flagFit"),map.get("flagNotCnfrm"),map.get("flagLaborOpUp"),map.get("pkPv")});
    }
    
    public void updSeqNoByPkParent(String param , IUser user){
    	Map map = JsonUtil.readValue(param, Map.class);
    	String sql="update EMR_HOME_PAGE_DIAGS_ICD set SEQ_NO=? where PK_PV=? and PK_PARENT=? and DEL_FLAG='0'";
    	DataBaseHelper.update(sql, map.get("seqNo"),map.get("pkPv"),map.get("pkParent"));
    }
    
    public void updPatRecStatus(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String status = map.get("status").toString();
		Date now = new Date();
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(now);
		String sql="update emr_pat_rec set eu_status=? where pk_pv=? and del_flag='0'";
		if (status != null && status.equals("7")) {
			// 归档
			if(Application.isSqlServer()){
				sql = "update emr_pat_rec set eu_status='" + status
						+ "',flag_emr_archive='1',pk_emp_emr_archive='"
						+ UserContext.getUser().getPkEmp()
						+ "',emr_archive_date='" + dateStr
						+ "' where PK_PV='"
						+ map.get("pkPv").toString() + "'";
			}else{
				sql = "update emr_pat_rec set eu_status='" + status + "',"
						+ " pk_emp_archive='"
						+ UserContext.getUser().getPkEmp() + "',"
						+ "archive_date=to_date('" + dateStr
						+ "','yyyy-MM-dd hh24:mi:ss')" + " where pk_pv='"
						+ map.get("pkPv").toString() + "'";
			}
		}
		DataBaseHelper.update(sql, new Object[] {});
    }
    
    public void updRecName(String param , IUser user) throws ParseException{
    	Map map = JsonUtil.readValue(param,Map.class);
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
    	Date tmp = sf.parse(map.get("date").toString());
    	String sql="update emr_med_rec set name='"+map.get("name")+"',rec_date=to_date('"+sf.format(tmp)+"','yyyymmddhh24miss') where pk_rec='"+map.get("pkRec")+"'";
    	DataBaseHelper.update(sql, new Object[] {});
    }
    
    /**
	 * 打印医嘱时查询医嘱
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> printQryCnOrderBoai(String param , IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		//0表示住院医生站医嘱界面不打印手术医嘱
		String sysparam = ApplicationUtils.getSysparam("CN0063", false, "请维护好系统参数CN0063！");
		map.put("isOperation", sysparam);
		//数据库类型
		String dbType = MultiDataSource.getCurDbType();
		map.put("dbType",dbType);
		List<Map<String,Object>> orderList = mgrMapper.printQryCnOrderBoai(map);
		return orderList;
	}
	/**
	 * 电子病历 批量更新rec表里的主治和主任pk
	 * @param param
	 * @param user
	 */
	public void updRecNursingSign(String param , IUser user){
		List<EmrMedRec> reclist=JsonUtil.readValue(param, new TypeReference<List<EmrMedRec>>(){});
		List<String> upSqls=new ArrayList<String>();
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (EmrMedRec emrMedRec : reclist) {
			StringBuffer upSql=new StringBuffer("update emr_med_rec set ");
			//主治pk
			if(emrMedRec.getPkEmpConsult()==null){
				upSql.append("pk_emp_consult=null,");
			}else{
				upSql.append("pk_emp_consult='"+emrMedRec.getPkEmpConsult()+"',");
			}
			//主治签名日期
			if(emrMedRec.getConsultSignDate()==null){
				upSql.append("consult_sign_date=null,");
			}else{
				upSql.append("consult_sign_date=to_date('"+fm.format(emrMedRec.getConsultSignDate())+"','yyyy-MM-dd hh24:mi:ss'),");
			}
			//主任pk
			if(emrMedRec.getPkEmpDirector()==null){
				upSql.append("pk_emp_director=null,");
			}else{
				upSql.append("pk_emp_director='"+emrMedRec.getPkEmpDirector()+"',");
			}
			//主任签名日期
			if(emrMedRec.getDirectorSignDate()==null){
				upSql.append("director_sign_date=null ");
			}else{
				upSql.append("director_sign_date=to_date('"+fm.format(emrMedRec.getDirectorSignDate())+"','yyyy-MM-dd hh24:mi:ss') ");
			}
			
			upSql.append("where pk_rec='"+emrMedRec.getPkRec()+"'");
			upSqls.add(upSql.toString());
		}
		DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
	}
	
	/**
	 * 病案编码完成时修改patRec状态
	 * @param pkPv
	 * @param codeEmp
	 */
	public void updPatRecFlagCode(String param , IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String sql="update emr_pat_rec set flag_code='1' where pk_pv=? and del_flag='0'";
		DataBaseHelper.update(sql, new Object[]{map.get("pkPv")});
		User u = (User) user;
		String empCode=u.getCodeEmp();
		String empName=u.getNameEmp();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sqlhomepage="update emr_home_page set pk_emp_coder='"+empCode+"',CODER_NAME='"+empName+"' where pk_pv=? and del_flag='0'";
		DataBaseHelper.update(sqlhomepage, new Object[]{map.get("pkPv")});
		
	}
	public void saveFeedBack(String param , IUser user){
		List<EmrHomePageFeedBackIcd> feedIcd=JsonUtil.readValue(param, new TypeReference<List<EmrHomePageFeedBackIcd>>(){});
		if(feedIcd.size()>0){
			String sql="select count(1) from EMR_HOME_PAGE_FEEDBACK_ICD where PK_FEEDBACK_ICD=?";
			for(EmrHomePageFeedBackIcd feed: feedIcd){
				if(feed.getPkFeedbackIcd()==null) {
					EmrHomePageFeedBackIcd emrHomePageFeedBackIcd=new EmrHomePageFeedBackIcd();
					emrHomePageFeedBackIcd.setPkPage(feed.getPkPage());
					emrHomePageFeedBackIcd.setPkDept(feed.getPkDept());
					emrHomePageFeedBackIcd.setPkEmp(feed.getPkEmp());
					emrHomePageFeedBackIcd.setPkPv(feed.getPkPv());
					emrHomePageFeedBackIcd.setSeqNo(feed.getSeqNo());
					emrHomePageFeedBackIcd.setCode(feed.getCode());
					emrHomePageFeedBackIcd.setBackContent(feed.getBackContent());
					emrHomePageFeedBackIcd.setType(feed.getType());
					DataBaseHelper.insertBean(emrHomePageFeedBackIcd);
				}else {
					Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, feed.getPkFeedbackIcd());
					if(count>0){
						DataBaseHelper.updateBeanByPk(feed);
					}
				}
			}
		}
	}
	/**
	 * 查询反馈内容--通用
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrAmendRec> queryEmrAmendRecList(String param , IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		List<EmrAmendRec> backList = mgrMapper.queryEmrAmendRecList(map);
		return backList;
	}
}

