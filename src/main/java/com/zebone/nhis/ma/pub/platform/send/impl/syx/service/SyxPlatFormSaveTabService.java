package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.ma.pub.platform.send.impl.syx.dao.SyxPlatFormSaveTabMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.vo.SysEsbmsg;
import com.zebone.nhis.ma.pub.platform.syx.support.MsgUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.XmlProcessUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class SyxPlatFormSaveTabService {
	@Autowired
	private SyxPlatFormSaveTabMapper syxPlatFormSaveTabMapper;
	
	 /**
	 * 查询医嘱信息发送者IP
	 * @return
	 */
	private String qryIpSend(String action){
		switch (action) {
		case "OperationAppInfoAdd":
			action="OpAppInfoAdd";
			break;
		case "OperationAppInfoUpdate":
			action="OpAppInfoUpdate";
			break;
		case "ExamAppInfoUpdate":
			action="ExamInfoUpdate";
			break;
		case "CheckAppInfoUpdate":
			action="CheckInfoUpdate";
			break;
		case "PathologyAppInfoAdd":
			action="PathologyAdd";
			break;
		case "PathologyAppInfoUpdate":
			action="PathologyUpdate";
			break;
		case "InPatientInfoUpdate":
			action="InPatientUpdate";
			break;
		case "DischargeInfoAdd":
			action="DischargeAdd";
			break;
		case "DischargeInfoUpdate":
			action="DischargeUpdate";
			break;
		case "TransferInfoAdd":
			action="TransferAdd";
			break;
		case "TransferInfoUpdate":
			action="TransferUpdate";
			break;
		default:
			break;
		}
		return syxPlatFormSaveTabMapper.qryIpSend(action);
		
	}
	
	 private SysEsbmsg createSysEsbmsg(Request req, Map<String, Object> paramMap) {
			String reqXml=XmlProcessUtils.toRequestXml(req,req.getReqHead());
			String action = MsgUtils.getPropValueStr(paramMap, "action");
			//System.out.println(content);
			String id = req.getId().getExtension();
			SysEsbmsg sysEsbmsg = new SysEsbmsg(); 
			sysEsbmsg.setPkEsbmsg(NHISUUID.getKeyId());//消息主键
			sysEsbmsg.setPkOrg(UserContext.getUser().getPkOrg());//机构
			sysEsbmsg.setIdMsg(id);//消息id
			String pkPv = MsgUtils.getPropValueStr(paramMap, "pkPv");
			sysEsbmsg.setPkPv(pkPv );//患者主键
			String headType = MsgUtils.getPropValueStr(paramMap, "headType");
			sysEsbmsg.setDtEsbmsgtype(headType );//消息类型
			
			sysEsbmsg.setEuStatus("0");//消息状态（0未处理，1.已处理，2.处理失败）
			sysEsbmsg.setDateSend(new Date());//发送时间
			sysEsbmsg.setDescError("");//操作失败描述
			sysEsbmsg.setCntHandle(0);//操作次数
			String content = "";
			String ipSend = MsgUtils.getPropValueStr(paramMap, action);
			if(!StringUtils.isNotBlank(ipSend)){
				ipSend = qryIpSend(action);//从码表bd_defdoc获取发送者ip
				paramMap.put(action, ipSend);
			}
			content = getSoapXML(action , reqXml,ipSend);
			sysEsbmsg.setContentMsg(content);//消息内容
			sysEsbmsg.setIpSend(ipSend);//发送IP
			sysEsbmsg.setAddrSend("");//发送地址
			sysEsbmsg.setNote("");//备注
			User user = UserContext.getUser();
			sysEsbmsg.setCreator(user.getPkEmp());//创建人
			sysEsbmsg.setCreateTime(new Date());//创建时间
			sysEsbmsg.setDelFlag("0");//
			sysEsbmsg.setTs(new Date());//时间戳
			return sysEsbmsg;
		}
	 
	 public void saveData(List<Request> reqs, Map<String,Object> map){
		 if (reqs != null && reqs.size()>0) {
			 
			 List<SysEsbmsg> sysEsbmsgs = new ArrayList<>();
			for (Request req : reqs) {
				sysEsbmsgs.add(createSysEsbmsg(req,map));
			}
			
			if(sysEsbmsgs.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SysEsbmsg.class),sysEsbmsgs);
			}
		}
	 }
	 
	 public void saveData(Request req, Map<String,Object> map){
		 if (req != null ) 
			DataBaseHelper.insertBean(createSysEsbmsg(req,map)) ;
	 }
	 
	 /**
	     * 组装发送消息报文
	     * @param action
	     * @param inputXml
	 * @param ipSend 
	     * @return
	     */
	    public  String getSoapXML(String action,String inputXml, String ipSend){
	    	String serviceName = ipSend.substring(ipSend.lastIndexOf("/")+1,ipSend.lastIndexOf("?"));
	    	StringBuilder sbr = new StringBuilder("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:hl7-org:v3\"><soap:Header/><soap:Body><urn:"+serviceName+">");
	    	sbr.append("<action>");
	    	sbr.append(action);
	    	sbr.append("</action>");
	    	sbr.append("<message><![CDATA[");
	    	sbr.append(inputXml);
	    	sbr.append("]]></message>");
	    	sbr.append("</urn:"+serviceName+"></soap:Body></soap:Envelope>");
			return sbr.toString();
	    }
}
