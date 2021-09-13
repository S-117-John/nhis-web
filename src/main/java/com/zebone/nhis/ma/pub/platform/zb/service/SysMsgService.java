package com.zebone.nhis.ma.pub.platform.zb.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.segment.MSH;

import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.zb.dao.MsgRecBlMapper;
import com.zebone.nhis.ma.pub.platform.zb.dao.MsgRecMapper;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendAdt;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMsgService {


	@Resource
	private	MsgRecMapper msgRecMapper;
	@Resource
	private	MsgRecBlMapper msgRecBlMapper;
	@Resource
	private	MsgUtils msgUtils;
	
	@Resource
	private	MsgSendAdt msgSendAdt;
	
	/**
	 * 保存Hl7消息
	 * @param message
	 * @param expandStr
	 * @throws HL7Exception
	 */
	public SysMsgRec saveHl7MsgReceive(Message message,String msg) throws HL7Exception{
		Segment segment = (Segment) message.get("MSH");
		MSH msh=(MSH)segment;
		SysMsgRec rec = new SysMsgRec();
		rec.setTransType("receive");
		rec.setMsgType(msh.getMessageType().getMessageType().getValue()+"^"+msh.getMessageType().getTriggerEvent().getValue());
		rec.setMsgId(msh.getMessageControlID().getValue());
		rec.setTransDate(new Date());
		rec.setMsgContent(msg);
		String sysCode=msh.getSendingApplication().getNamespaceID().getValue();
		if(sysCode==null||sysCode.equals("")) sysCode="EAI";
		rec.setSysCode(sysCode);
		rec.setMsgStatus("SAVE");
		
		DataBaseHelper.insertBean(rec);
		
		return rec;
	}
	
	/**
	 * 保存系统消息日志
	 * @param message
	 * @param expandStr
	 * @throws HL7Exception
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveHl7Msg(Message message,String msg) throws HL7Exception{
		Segment segment = (Segment) message.get("MSH");
		MSH msh=(MSH)segment;
		
		SysMsgRec rec = new SysMsgRec();
		rec.setTransType("send");
		rec.setMsgType(msh.getMessageType().getMessageType().getValue()+"^"+msh.getMessageType().getTriggerEvent().getValue());
		rec.setMsgId(msh.getMessageControlID().getValue());
		rec.setTransDate(new Date());
		rec.setMsgContent(msg);
		rec.setSysCode("NHIS");
		rec.setMsgStatus("SAVE");
		
		DataBaseHelper.insertBean(rec);
	}
		
	/**
	 * 保存系统消息日志
	 * @param list
	 * @param pkPi
	 */
	public void saveSysMsgRec(SysMsgRec rec) {
		DataBaseHelper.insertBean(rec);
	}
	
	/**更新系统消息日志
	 * @param msgId
	 * @param msgStatus
	 * @param msgTxt
	 */
	public void updateSysMsgRec(String msgId,String msgStatus,String errTxt) {
		DataBaseHelper.update("update sys_msg_rec set msg_status = '"+msgStatus+"',err_txt=? where msg_id = ?",
				new Object[] { errTxt,msgId});
	}
	
	/**更新系统消息日志
	 * @param msgId
	 * @param msgStatus
	 * @param msgTxt
	 */
	public void updateSysMsgRec(String msgId,String msgStatus) {
		DataBaseHelper.update("update sys_msg_rec set msg_status = '"+msgStatus+"' where msg_id = ?",new Object[] {msgId});
	}

	public void updateReceiveSysMsgRec(String msgId,String msgType,String msgStatus){
		DataBaseHelper.update(" update sys_msg_rec  set msg_type= '"+msgType+"',msg_status= '"+msgStatus+"' where msg_id=?",new Object[] {msgId});
	}	
	
    /**
     * 查询HL7消息记录
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<SysMsgRec> querMsgList(String param , IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map!=null && map.get("transType")!=null){
    		String transType=map.get("transType").toString().toUpperCase();
    		map.put("transType", transType);
    	}
    	return msgRecMapper.queryMsgList(map);
    }

	public Page<SysMsgRec> querMsgListOfPage(String param , IUser user){
		Map<String,Object> map = JsonUtil.readValue(param,Map.class);
		if(map!=null && map.get("transType")!=null){
			map.put("transType", map.get("transType").toString().toUpperCase());
		}
		MyBatisPage.startPage(MapUtils.getIntValue(map,"pageIndex",1), MapUtils.getIntValue(map,"pageSize",20));
		List<SysMsgRec> sysMsgRecs = msgRecMapper.queryMsgList(map);
		Page<SysMsgRec> page = MyBatisPage.getPage();
		page.setRows(sysMsgRecs);
		return page;
	}
    /**
     * 解析hl7消息根据解析数据查询患者信息
     * 查询患者信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryPatList(Map<String,Object> map){
    	return msgRecMapper.queryPatList(map);
    }
    
    
    /**
     * 查询申请医嘱信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryReqOrdList(Map<String,Object> map){
    	return msgRecMapper.queryReqOrdList(map);
    }  
    
    /**
     * 查询申请医嘱信息
     * @param map
     * @return
     */
    public void saveRisRptList(String pkPv,String codeApplyStr,List<ExRisOcc> list){
    	if(list==null||list.size()==0) return;
    	DataBaseHelper.execute("delete from ex_ris_occ where pk_pv = ? and code_apply in "+codeApplyStr, new Object[] { pkPv });
    	
    	for (ExRisOcc exRisOcc : list) {
    		DataBaseHelper.insertBean(exRisOcc);
		}
    	//更新报告状态
    	DataBaseHelper.update("update cn_ris_apply set eu_status ='4' where pk_cnord in (select pk_cnord from cn_order where code_apply in "+codeApplyStr+")",new Object[] {});
    } 
    /**
     * 保存检验结果
     * @param map
     * @return
     */
    public void saveLisRptList(String pkPv,String codeApplyStr,List<ExLabOcc> list){
    	if(list==null||list.size()==0) return;
    	DataBaseHelper.execute("delete from ex_lab_occ where pk_pv = ? and code_apply in "+codeApplyStr, new Object[] { pkPv });
    	
    	for (ExLabOcc exLisOcc : list) {
    		DataBaseHelper.insertBean(exLisOcc);
		}
    	//更新报告状态
    	DataBaseHelper.update("update cn_lab_apply set eu_status ='4' where pk_cnord in (select pk_cnord from cn_order where cn_order.pk_pv = ? and code_apply in "+codeApplyStr+")",new Object[] { pkPv });
    } 
    
    
    /**
     * 检验作废
     * @param map
     * @return
     */
    public void updateLisRptList(Map<String, Object> map){
    	if(map!=null){
    		if(("3").equals(map.get("euPvtype"))){//住院
    			if(("02").equals(map.get("liseustu"))){//检验接收条码作废消息后更新数据
    				//更新检验状态，做退费处理
        	    	DataBaseHelper.execute("update cn_lab_apply set eu_status ='3',modifier=?,modity_time=? where pk_cnord = ?",map.get("pkEmp"),new Date(),map.get("pkCnord"));
    			}
    		}else{//门诊
    			if(("02").equals(map.get("liseustu"))){//检验接收条码作废消息后更新数据
    				DataBaseHelper.execute("update ex_assist_occ set flag_canc='1',date_canc=?,pk_emp_canc=?,eu_status='9' where pk_cnord=? ",new Date(),map.get("pkEmp"), map.get("pkCnord"));
        			DataBaseHelper.execute("update cn_lab_apply set eu_status ='0',modifier=?,modity_time=? where pk_cnord=?",map.get("pkEmp"),new Date(),map.get("pkCnord"));
    			}else{//检验接收样本采集消息后更新数据
    				DataBaseHelper.execute("update ex_assist_occ set flag_occ='1',date_occ=?,pk_emp_occ=?,eu_status='1' where pk_cnord=? ", new Date(),map.get("pkEmp"),map.get("pkCnord"));
        			DataBaseHelper.execute("update cn_lab_apply set eu_status ='2',modifier=?,modity_time=? where pk_cnord=?",map.get("pkEmp"),new Date(),map.get("pkCnord"));
    			}
    			
    		}
    	}
    	
    } 
    
    
    /**
     * 检查作废
     * @param map
     * @return
     */
    public void updateRisRptList(Map<String, Object> map){
    	if(map!=null){
    		if(("02").equals(map.get("riseustu"))){//检验接收条码作废消息后更新数据
				DataBaseHelper.execute("update ex_assist_occ set flag_canc='1',date_canc=?,pk_emp_canc=?,eu_status='9' where pk_cnord=? ",new Date(),map.get("pkEmp"), map.get("pkCnord"));
    			DataBaseHelper.execute("update cn_ris_apply set eu_status ='1',modifier=?,modity_time=? where pk_cnord=?",map.get("pkEmp"),new Date(),map.get("pkCnord"));
			}else{//检验接收样本采集消息后更新数据
				DataBaseHelper.execute("update ex_assist_occ set flag_occ='1',date_occ=?,pk_emp_occ=?,eu_status='1' where pk_cnord=? ", new Date(),map.get("pkEmp"),map.get("pkCnord"));
    			DataBaseHelper.execute("update cn_ris_apply set eu_status ='3',modifier=?,modity_time=? where pk_cnord=?",map.get("pkEmp"),new Date(),map.get("pkCnord"));
			}
    	}
    }
    
    
    /**
     * 获取病人基本信息MAP
     * @param map
     * @return
     * @throws Exception 
     */
    public Map<String, Object> getPatMap(String pkPv,Map<String,Object> patMap) throws Exception{
    	if(pkPv==null||pkPv.equals("")){
    		throw new Exception("病人信息和患者就诊pkPv不能同时为空！");
    	}
    	Map<String, Object> qryParam = new HashMap<String, Object>();
    	qryParam.put("pkPv", pkPv);
    	List<Map<String, Object>> list= msgRecMapper.queryPatListIp(qryParam);
    	if(list==null||list.size()==0){
    		throw new Exception("病人信息为空！");
    	}
    	patMap.putAll(list.get(0));
		return patMap;
    }
    
    /**
     * 组装hl7消息时使用
     * 获取病人基本信息MAP门诊
     * @param map
     * @return
     * @throws Exception 
     */
    public Map<String, Object> getPatMapOut(String pkPv,Map<String,Object> patMap) throws Exception{
    	if(pkPv==null||pkPv.equals("")){
    		throw new Exception("病人信息和患者就诊pkPv不能同时为空！");
    	}
    	Map<String, Object> qryParam = new HashMap<String, Object>();
    	qryParam.put("pkPv", pkPv);
    	List<Map<String, Object>> list= msgRecMapper.queryPatListOp(qryParam);
    	if(list==null||list.size()==0){
    		throw new Exception("病人信息为空！");
    	}
    	patMap.putAll(list.get(0));
		return patMap;
    }
    
    /**
     * 根据用户主外键查询编码
     * @param pkUser
     * @return CODE:编码,NAME
     * 查询BD_OU_USER
     */
    public Map<String,Object> getUserCodeByPkUser(String pkUser){
    	if(pkUser==null || "".equals(pkUser)) return null;
    	String sql="select code_emp CODE,name_emp NAME from bd_ou_employee where pk_emp=?";	
    	Map<String,Object> userCode=DataBaseHelper.queryForMap(sql,pkUser);
    	return userCode;
    }
    /**
     * 根据科室主键查询编码和名称
     * @param pkDept
     * @return map{codeDept,nameDept}
     */
    public Map<String,Object> getDeptInfoByPkDept(String pkDept){
    	String sql="select code_dept,name_dept from bd_ou_dept where pk_dept=?";
    	Map<String,Object> map=DataBaseHelper.queryForMap(sql, pkDept);
    	return map;
    }
    
    /**
     * 根据科室主键查询编码和名称
     * @param pkDept
     * @return map{codeDept,nameDept}
     */
    public Map<String,Object> getDeptInfoByPkDepts(String codeDept){
    	String sql="select pk_dept,code_dept,name_dept from bd_ou_dept where code_dept=?";
    	Map<String,Object> map=DataBaseHelper.queryForMap(sql, codeDept);
    	return map;
    }

    /**
	 * 根据的cn_op_apply.dt_oplevel查询手术等级
	 * @param dtOplevel
	 * @return
	 */
	public Map<String,Object> queryOplevel(String dtOplevel){
		if(dtOplevel==null || dtOplevel.equals("")){
			return null;
		}
		String sql = "select t.ba_code from bd_defdoc t where t.code_defdoclist='030305' and t.code=?";
		Map<String, Object> oplevelMap = DataBaseHelper.queryForMap(sql, dtOplevel);
		return oplevelMap;
	}
	
    /**
	 * 查询麻醉方式
	 * @param dtOplevel
	 * @return
	 */
	public Map<String,Object> querydtAnae(String dtOplevel){
		if(dtOplevel==null || dtOplevel.equals("")){
			return null;
		}
		String sql = "select t.code,t.name from bd_defdoc t where t.code_defdoclist='030300' and t.code=?";
		Map<String, Object> oplevelMap = DataBaseHelper.queryForMap(sql, dtOplevel);
		return oplevelMap;
	}
	
	/**
	 * 查询手术编码和名字
	 * @param pkOp
	 * @return
	 */
	public Map<String,Object> queryBdTermDiag(String pkOp){
		if(pkOp==null||"".equals(pkOp)){
			return null;
		}
		String sql = "select diagcode，diagname from BD_TERM_DIAG where pk_diag=? ";
		return DataBaseHelper.queryForMap(sql, pkOp);
	}
    /*
	 *  发送Hl7消息重推
	 * @param triggerEvent
	 * @param paramMap 
	 * @throws HL7Exception
	 */
	public void againNewsPush(String param,IUser u){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		try {
			msgSendAdt.sendAdtMsg(MsgUtils.getPropValueStr(paramMap,"triggerEvent"),paramMap);
		} catch (HL7Exception e) {
		}
	}
	
	/*
	 *  发送Hl7消息重推
	 * @param triggerEvent
	 * @param paramMap 
	 * @throws HL7Exception
	 */
	public void sendExOrderCheckMsg(String param,IUser u){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ordlist", JsonUtil.readValue(param, Map.class));
	    PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
	}
	
}
