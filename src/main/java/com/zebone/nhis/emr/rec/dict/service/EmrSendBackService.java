package com.zebone.nhis.emr.rec.dict.service;

import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatRec;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.emr.rec.dict.dao.EmrSendBackMapper;
import com.zebone.nhis.emr.rec.dict.vo.*;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.BeanExpressionException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * create by: gao shiheng
 * create time: 19:34 2019/5/4
 *病历退回
 * @Param: null
 * @return
 */
@Service
public class EmrSendBackService {
    @Resource
    EmrSendBackMapper emrSendBackMapper;
    /**
     * create by: gao shiheng
     *根据患者就诊主键查询已经科室质控提交未归档的病历列表
     * @return a
     * @Param: null
    */
    public List<EmrDocListPrarm> queryEmrDocList(String param,IUser user){
        String 	pkPv = JsonUtil.getFieldValue(param, "pkPv");
        if (pkPv == null || pkPv == "")
            throw new BeanExpressionException("参数异常");
        return emrSendBackMapper.queryEmrDocList(pkPv);
    }

    /**
     * 保存病历召回审批新增的病历
     * @param param
     * @param user  
     */
    public void saveAddTypeCode(String param, IUser user){
    	List<EmrOpenDocPrarm> emrOpenDocPrarmList = JsonUtil.readValue(param,
                new TypeReference<List<EmrOpenDocPrarm>>(){});
    	if(emrOpenDocPrarmList.size()>0){
    		for (EmrOpenDocPrarm doc:emrOpenDocPrarmList) {
                doc.setPkEditRec(doc.getPkEditRec());
                doc.setPkOrg(UserContext.getUser().getPkOrg());
                doc.setEuType("1");
                doc.setEuStatus("0");
                doc.setEuEditType("1");
                doc.setDelFlag("0");
                doc.setTypeCode(doc.getTypeCode());
                if(doc.getPkEditRec()!=null){
                	DataBaseHelper.insertBean(doc);
                }
            }
    	}
    	
    }
    
    /**
     * create by: gao shiheng
     *病历退回
     * @return a
     * @Param: null
    */
    public int emrSendBack(String param, IUser user){
        List<EmrOpenDocPrarm> emrOpenDocPrarmList = JsonUtil.readValue(param,
                new TypeReference<List<EmrOpenDocPrarm>>(){});
       List<EmrOpenRecPrarm> emrOpenRecPrarmList = JsonUtil.readValue(param,
              new TypeReference<List<EmrOpenRecPrarm>>(){});
       int flag = 0;
        //获取当前用户、科室、机构
        String pkEmpApprove = UserContext.getUser().getId();
        String pkDeptApprove = UserContext.getUser().getPkDept();
        String pkOrg = UserContext.getUser().getPkOrg();
        Map map = new HashMap();
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
        List pkRecList = new ArrayList();

        String pkEditRec = NHISUUID.getKeyId();
        emrOpenRecPrarmList.get(0).setPkEditRec(pkEditRec);
        emrOpenRecPrarmList.get(0).setPkOrg(pkOrg);
        emrOpenRecPrarmList.get(0).setEuType("2");
        emrOpenRecPrarmList.get(0).setApplyTxt("病历退回");
        emrOpenRecPrarmList.get(0).setEuStatus("0");
        emrOpenRecPrarmList.get(0).setPkEmpApprove(pkEmpApprove);
        emrOpenRecPrarmList.get(0).setPkDeptApprove(pkDeptApprove);
        emrOpenRecPrarmList.get(0).setApproveDate(new Date());
        emrOpenRecPrarmList.get(0).setDelFlag("1");
        emrOpenRecPrarmList.get(0).setCreator(pkEmpApprove);
        emrOpenRecPrarmList.get(0).setCreateTime(new Date());
        //插入病历开放编辑记录
        DataBaseHelper.insertBean(emrOpenRecPrarmList.get(0));
        for (EmrOpenDocPrarm doc:emrOpenDocPrarmList) {
            doc.setPkEditDoc(NHISUUID.getKeyId());
            doc.setPkEditRec(pkEditRec);
            doc.setPkOrg(pkOrg);
            doc.setEuType("2");
            doc.setApplyTxt("病历退回");
            doc.setEuStatus("0");
            doc.setPkEmpApprove(pkEmpApprove);
            doc.setPkDeptApprove(pkDeptApprove);
            doc.setApplyDate(new Date());
            doc.setDelFlag("0");
            doc.setCreator(pkEmpApprove);
            doc.setCreateTime(new Date());
            doc.setEuEditType(doc.getEuEditType());
            doc.setTypeCode(doc.getTypeCode());
            if(doc.getPkRec()!=null){
            	pkRecList.add(doc.getPkRec());
            }
        }
        //批量添加病历开放编辑文档记录
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(EmrOpenDocPrarm.class),emrOpenDocPrarmList);
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        //更新患者病历状态为emr_pat_rec.eu_status=1
        String sql = "update emr_pat_rec set eu_status='1',flag_open_edit='1',edit_begin_date=to_date('"+sdf.format(emrOpenRecPrarmList.get(0).getBeginDate())+"','yyyy-MM-dd hh24:mi:ss'),edit_end_date=to_date('"+sdf.format(emrOpenRecPrarmList.get(0).getEndDate())+"','yyyy-MM-dd hh24:mi:ss') where pk_pv='"+emrOpenRecPrarmList.get(0).getPkPv()+"'"+"and del_flag ='0'";
        flag = flag + DataBaseHelper.update(sql,map);
        /*EmrPatRec emrPatRec = emrSendBackMapper.selectEmrPatRecByPkPv(emrOpenRecPrarmList.get(0).getPkPv());
        if (emrPatRec != null){
            map.put("euStatus","1");
            map.put("pkPatrec",emrPatRec.getPkPatrec());
            
        }*/

        //更改病历文档记录逻emr_med_rec edit_begin_date、edit_end_date）。
        /*if(pkRecList.size()>0){
        	List<EmrMedRec> medRec = emrSendBackMapper.selectEmrMedRecByPkREc(pkRecList);
            List<EmrMedRec> list = new ArrayList<>();
            if (medRec != null){
                for (EmrMedRec emrMedRec :medRec){
                    EmrMedRec emrMedRec1 = new EmrMedRec();
                    emrMedRec1.setFlagOpenEdit("1");
                    emrMedRec1.setEditBeginDate(emrOpenRecPrarmList.get(0).getBeginDate());
                    emrMedRec1.setEditEndDate(emrOpenRecPrarmList.get(0).getEndDate());
                    emrMedRec1.setPkRec(emrMedRec.getPkRec());
                    list.add(emrMedRec1);
                }
                String sqlTemp ="UPDATE EMR_MED_REC SET FLAG_OPEN_EDIT=:flagOpenEdit,EDIT_BEGIN_DATE=:editBeginDate,EDIT_END_DATE=:editEndDate WHERE PK_REC =:pkRec";
                DataBaseHelper.batchUpdate(sqlTemp, list);
            }
        }*/
        
        return flag;
    }

    /**
     * create by: gao shiheng
     * create time: 17:57 2019/5/10
     *查询病历召回申请列表
     * @Param: null
     * @return
     * @throws ParseException 
     */
    public List<EmrOpenEditRecParam> queryEmrRecallList(String param, IUser user) throws ParseException{
        Map<String,Object> map = JsonUtil.readValue(param,Map.class);
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
        List<EmrOpenEditRecParam> list = emrSendBackMapper.queryEmrRecallList(map);
        return list;
    }

    /**
     * create by: gao shiheng
     * create time: 18:39 2019/5/13
     *病历召回审批查询病历文档
     * @Param: null
     * @return
     */
    public List<EmrMedRec> queryEmrDoclList(String param, IUser user){
        /*String 	pkPv = JsonUtil.getFieldValue(param, "pkPv");
        if (pkPv == null || pkPv == "")
            throw new BeanExpressionException("参数异常");*/
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	List<EmrMedRec> list= emrSendBackMapper.queryEmrDoclList(map);
        return list;
    }

    /**
     * create by: gao shiheng
     * create time: 15:03 2019/5/16
     *批量审批病历召回申请
     * @Param: null
     * @return
     */
    public void approveRecall(String param, IUser user){
        List<Map<String, Object>> list = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {});
        int flag=0;
        String openRecSql = null;
        String medSql = null;
        String patSql = null;
        /*String openPatSql = null;*/
        //获取审批人，审批科室，当前时间
        String pkEmpApprove = UserContext.getUser().getPkOrg();
        Date approveDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(approveDate);
        String pkDaptApprove = UserContext.getUser().getPkDept();
        /*
        * 当传入的euStates不为空，表示是批量通过申请
        * 如果euStates为1，表示批准通过部分病历
        * 如果euStates为2，一份病历也不通过*/
        String operType="";//0批量通过1单份审批2拒绝
        String pkPv="";
        String approveTxt="";
        String pkEditRec="";
        if(list!=null&&list.size()>0){
        	operType=(list.get(0).get("euStates")== null||list.get(0).get("euStates").toString().equals(""))?"1":list.get(0).get("euStates").toString();
        	pkPv=list.get(0).get("pkPv").toString();
        	approveTxt=list.get(0).get("approveTxt").toString();
        	pkEditRec=list.get(0).get("pkEditRec").toString();
        	if (operType.equals("1")){
        		//单份通过
                openRecSql = "update emr_open_edit_rec set eu_status='"+operType+"'"+",pk_emp_approve='"+pkEmpApprove+"'"+",pk_dept_approve='"+pkDaptApprove+"'"+",approve_txt='"+approveTxt+"'"
                         +",approve_date=to_date('"+dateStr+"','yyyy-MM-dd hh24:mi:ss')"
                         +"where pk_pv='"+pkPv+"' and del_flag != '1' and pk_edit_rec='"+pkEditRec+"'";
                DataBaseHelper.update(openRecSql);
                patSql = "update emr_pat_rec set eu_status='1',flag_open_edit='1',edit_begin_date=to_date('"+list.get(0).get("openBeginDate").toString()+"','yyyy-MM-dd hh24:mi:ss'),edit_end_date=to_date('"+list.get(0).get("openEndDate").toString()+"','yyyy-MM-dd hh24:mi:ss') where pk_pv='"+pkPv+"'"+"and del_flag ='0'";
                DataBaseHelper.update(patSql);
                 
             }else if (operType.equals("2")){
             	//单份拒绝
             	openRecSql = "update emr_open_edit_rec set eu_status='"+operType+"'"+",pk_emp_approve='"+pkEmpApprove+"'"+",pk_dept_approve='"+pkDaptApprove+"'"+",approve_txt='"+approveTxt+"'"
                         +",approve_date=to_date('"+dateStr+"','yyyy-MM-dd hh24:mi:ss')"
                         +"where pk_pv='"+pkPv+"' and del_flag != '1' and pk_edit_rec='"+pkEditRec+"'";
             	DataBaseHelper.update(openRecSql);
             	//将明细记录设置成拒绝状态
             	openRecSql = "update emr_open_edit_doc set eu_status='2',pk_emp_approve='"+pkEmpApprove+"'"+",pk_dept_approve='"+pkDaptApprove+"'"+",approve_txt='"+approveTxt+"'"
                        +",approve_date=to_date('"+dateStr+"','yyyy-MM-dd hh24:mi:ss')"
                        +"where del_flag != '1' and pk_edit_rec='"+pkEditRec+"'";
            	DataBaseHelper.update(openRecSql);
             }
        }
        for(Map map : list){
            if (map.get("pkPv").toString() == null || map.get("pkPv").toString().equals("")){
                throw new BeanExpressionException("参数异常,pkPv为空");
            }
            
            if(operType.equals("0")){
                //批量通过
                //更新emr_open_edit_rec表的sql
                openRecSql = "update emr_open_edit_rec set eu_status='1',pk_emp_approve='"+pkEmpApprove+"'"+",pk_dept_approve='"+pkDaptApprove+"'"+",approve_txt='"+map.get("approveTxt").toString()+"'"
                        +",approve_date=to_date('"+dateStr+"','yyyy-MM-dd hh24:mi:ss')"
                        +"where pk_pv='"+map.get("pkPv").toString()+"' and del_flag != '1' and pk_edit_rec='"+map.get("pkEditRec").toString()+"'";
                DataBaseHelper.update(openRecSql, map);

                //更新emr_med_rec表的sql
                medSql = "update emr_med_rec set edit_begin_date=to_date('"+map.get("openBeginDate").toString()+"','yyyy-MM-dd hh24:mi:ss')"
                        +",edit_end_date=to_date('"+map.get("openEndDate").toString()+"','yyyy-MM-dd hh24:mi:ss') "
                        + "where pk_pv='"+map.get("pkPv").toString()+"' and del_flag != '1' "
                         + "and exists (select pk_rec from emr_open_edit_rec rec,emr_open_edit_doc doc where rec.pk_pv='"+ map.get("pkPv").toString() +"' and rec.pk_edit_rec='"+ map.get("pkEditRec").toString() +"' and rec.pk_edit_rec=doc.pk_edit_rec and doc.pk_rec=emr_med_rec.pk_rec)";
                DataBaseHelper.update(medSql, map);
                //更新emr_pat_rec表
                patSql = "update emr_pat_rec set eu_status='3' where pk_pv='"+map.get("pkPv").toString()+"'"+"and del_flag ='0'";
                DataBaseHelper.update(patSql, map);
            }else if (operType.equals("1")){
                //单份审批通过
                medSql = "update emr_med_rec set edit_begin_date=to_date('"+map.get("openBeginDate").toString()+"','yyyy-MM-dd hh24:mi:ss')"
                            +",edit_end_date=to_date('"+map.get("openEndDate").toString()+"','yyyy-MM-dd hh24:mi:ss'),flag_open_edit='1' "
                            + "where pk_pv='"+map.get("pkPv").toString()+"' and pk_rec='"+map.get("pkRec")+"'"+" and del_flag != '1'";
                DataBaseHelper.update(medSql, map);
                //更新文档内容
                if(map.get("pkRec")!=null&&!map.get("pkRec").toString().equals("")){
                    openRecSql = "update emr_open_edit_doc set eu_status='1',pk_emp_approve='"+pkEmpApprove+"'"+",pk_dept_approve='"+pkDaptApprove+"'"+",approve_txt='"+map.get("approveTxt").toString()+"'"
                            +",approve_date=to_date('"+dateStr+"','yyyy-MM-dd hh24:mi:ss')"
                            +" where pk_edit_rec='"+map.get("pkEditRec").toString()+"' and pk_rec='"+map.get("pkRec").toString()+"'";
                    DataBaseHelper.update(openRecSql, map);
                }
            }

        }
    }
}
