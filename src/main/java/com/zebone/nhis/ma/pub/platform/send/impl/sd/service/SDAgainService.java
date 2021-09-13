package com.zebone.nhis.ma.pub.platform.send.impl.sd.service;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgResend;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendBl;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendCn;
import com.zebone.nhis.ma.pub.platform.sd.service.SDSysMsgWeChatService;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.WeChatCancelResultVo;
import com.zebone.nhis.ma.pub.platform.send.impl.sd.dao.SDCnSendMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Hl7重新推送
 *
 */

@SuppressWarnings("unchecked")
@Service("sDAgainService")
public class SDAgainService {
	 @Resource
	 private SDMsgSendBl sDMsgSendBl;
	 @Resource
	 private SDMsgSendCn sDMsgSendCn;
	 @Resource
	 private SDCnSendMapper sDCnSendMapper;
	 @Resource
	 private SDMsgResend sDMsgResend;
	 
	 @Resource
	 private SDHl7MsgHander sDHl7MsgHander;
	 @Resource
	 private SDSysMsgWeChatService msgWeChatService;
	 

	 /**
	  * 022004003001
	  * 住院消息重新生成
	  * @param param 消息类型 type  医嘱号ordsn ，申请状态 control , 医嘱状态 ordStatus(O09必填)
	  * @param user
	  */
	 public String rebuildMsg(String param,IUser user){
		 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		 String result = sDMsgResend.msgResend(paramMap);
		 return result;
	 }

	 /**
	  * 022004003002
	  * 住院消息重新发送
	  * @param param hl7 消息
	  * @param user
	  */
	 public String resendMsg(String param,IUser user){
		String result = "发送失败！";
		//发送消息
		try{
			//获取数据
			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
			String message = SDMsgUtils.getPropValueStr(paramMap, "message");
			//验证HL7格式
			Parser parser = new DefaultHapiContext().getGenericParser();
			//获取MSH消息段，获取消息ID
			Message parse = parser.parse(message);
			MSH msh = (MSH) parse.get("MSH");
			String msgId = msh.getMessageControlID().getValue();
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, message);
			result = "发送成功!";
		}catch (Exception e){
			e.printStackTrace();
			result += e.getMessage();
		}
		return result;
	 }
	 
 	/**
	 * 022004003004
	 * 消息查询
	 * @param param
	 * 日期，消息类型，消息状态
	 */
	public String queryMessage(String param,IUser user){
		StringBuilder result = new StringBuilder();
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<SysMsgRec> sendSaveMsg = sDMsgResend.sendSaveMsg(paramMap);
		//发送消息
		for(SysMsgRec msg : sendSaveMsg){
			sDHl7MsgHander.sendMsg(msg.getMsgId(), msg.getMsgContent(),false);
		}
		result.append("发送成功！共发送").append(sendSaveMsg.size()).append("条消息！");
		return result.toString();
	}
	
	/**
	 * 022004003005
	 * @param param
	 * @param user
	 * @return
	 */
	public WeChatCancelResultVo sendWechatCancelOp(String param,IUser user){
		Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
		String pkSettle=paramMap.get("pkSettle").toString();
		String type =paramMap.get("type").toString();
		
		return msgWeChatService.sendCancelApplyOp(pkSettle, type);
		
		
	}

	/**
	 * 022004003003
	 * 消息批量重发 （修改代码重发方法）
	 * @param param
	 * @param user
	 * @return
	 */
	public String sendAgain(String param,IUser user){
		StringBuilder result = new StringBuilder();
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	//msgRecMapper.queryMsgList(paramMap);
		//List<SysMsgRec> sendSaveMsg = sDMsgResend.sendSaveMsg(paramMap);
		//SYS_MSG_REC_BACK    SYS_MSG_REC
		StringBuffer sql = new StringBuffer("select * from SYS_MSG_REC  where TRANS_TYPE='send' ");
		sql.append("and MSG_CONTENT like '%9928^%' ");
		//sql.append("and MSG_TYPE='OMP^O09' ");
		sql.append("and to_char(TRANS_DATE,'YYYYMMDD') between '20201021' and '20201029' ");
		sql.append("order by TRANS_DATE asc ");
		List<SysMsgRec> sendSaveMsg = DataBaseHelper.queryForList(sql.toString(), SysMsgRec.class,new Object[]{});
		//发送消息
		int i = 0;
		for(SysMsgRec msg : sendSaveMsg){
 			//sDHl7MsgHander.sendMsg(msg.getMsgId(), msg.getMsgContent(),false);
			i++;
 			System.out.println("发送成功！"+i);
		}
		result.append("发送成功！共发送").append(sendSaveMsg.size()).append("条消息！");
	
		return result.toString();
	}


	 /*
	 * 门诊 医技申请Hl7消息重推
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendCnMed(String param,IUser u){
		List<Map<String,Object>> paramList = JsonUtil.readValue(param, List.class);
		//String pkPv = SDMsgUtils.getPropValueStr(paramList.get(0), "pkPv");

		 String pkCnords="";
		   if(paramList.size()>0){
			   for (int i = 0; i < paramList.size(); i++) {
				   if(i==paramList.size()-1){
					   pkCnords+="'"+SDMsgUtils.getPropValueStr(paramList.get(i), "pkCnord")+"'";
				   }else{
					   pkCnords+="'"+SDMsgUtils.getPropValueStr(paramList.get(i), "pkCnord")+"',";
				   }
			   }
		   }

		   StringBuilder sql=new StringBuilder("SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,");
		    sql.append("co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.code_freq,co.flag_emer,");
		    sql.append("co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd.code_emp,co.name_emp_ord FROM cn_order co ");
		    sql.append("LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi LEFT JOIN BD_OU_EMPLOYEE bd on BD.pk_emp=co.pk_emp_ord where 1=1 ");
		    //sql.append(" and co.code_ordtype='03' and co.date_sign < to_date(20191117160000,'YYYYMMDDHH24MISS') and co.date_sign > to_date(20191117120000,'YYYYMMDDHH24MISS')");
            sql.append("and co.pk_cnord  in ("+pkCnords+")");
		   List<Map<String,Object>> mapList=DataBaseHelper.queryForList(sql.toString());
		   try {
		   Map<String, Object> mapRis = new HashMap<>();
		   mapRis.put("pkPv", mapList.get(0).get("pkPv"));
		   mapRis.put("type","ris");
		   List<Map<String, Object>> listMap =sDCnSendMapper.qryRisInfo(mapRis);
		   mapRis.clear();
		   for (Map<String, Object> map : mapList) {
			   if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
				   //遍历判断所需检查进行推送申请单
				   for(Map<String, Object> risMap:listMap){
					   if((map.get("ordsn")).equals(risMap.get("ordsn"))){
						   risMap.put("control", "NW");
						   risMap.put("fenlei", "ris");
							sDMsgSendCn.sendOmlMsg("O19", map.get("pkPv").toString(), null, risMap,"out");
						}
				   }
			}
			if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
				//ORC-1申请控制NW:新增申请
				map.put("control", "NW");
				sDMsgSendCn.sendOmlMsg("O21",  map.get("pkPv").toString(), null, map, "out");//门诊检验申请
			}
		   }


		   Thread.sleep(10000);

		   for (Map<String, Object> map : mapList) {
			   map.put("Control","OK");
				if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
						map.put("fenlei", "ris");
						//orgList.add(map);
						//msgSendBl.sendOGRGMsg("O20", orgList,"O");
						sDMsgSendBl.sendOGRGMsg("O20", map,"O");
				}
				if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
					//orlList.add(map);
					//msgSendBl.sendORLMsg("O22", orlList,"O");
					sDMsgSendBl.LbsendORLMsg("O22", map,"O");

				}
			}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		/*
		 * 门诊医技记费Hl7消息重推
		 * @param triggerEvent
		 * @param paramMap
		 * @throws HL7Exception
		 */
		public void sendBlMedApply(String param,IUser u)throws HL7Exception{
			List<Map<String,Object>> paramList = JsonUtil.readValue(param, List.class);
			//String pkPv = SDMsgUtils.getPropValueStr(paramList.get(0), "pkPv");

			 String pkCnords="";
			   if(paramList.size()>0){
				   for (int i = 0; i < paramList.size(); i++) {
					   if(i==paramList.size()-1){
						   pkCnords+="'"+SDMsgUtils.getPropValueStr(paramList.get(i), "pkCnord")+"'";
					   }else{
						   pkCnords+="'"+SDMsgUtils.getPropValueStr(paramList.get(i), "pkCnord")+"',";
					   }
				   }
			   }

			 StringBuilder sql=new StringBuilder("SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,");
			 sql.append("co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.code_freq,co.flag_emer,");
			 sql.append("co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi FROM cn_order co ");
			 sql.append("LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi where 1=1 ");
             sql.append("and co.pk_cnord  in ("+pkCnords+")");

			 List<Map<String,Object>> mapList=DataBaseHelper.queryForList(sql.toString());
			   for (Map<String, Object> map : mapList) {
				   map.put("Control","OK");
					if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
							map.put("fenlei", "ris");
							//orgList.add(map);
							//msgSendBl.sendOGRGMsg("O20", orgList,"O");
							sDMsgSendBl.sendOGRGMsg("O20", map,"O");
					}
					if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
						//orlList.add(map);
						//msgSendBl.sendORLMsg("O22", orlList,"O");
						sDMsgSendBl.LbsendORLMsg("O22", map,"O");

					}
				}
		}

		/*
		 *  发送Hl7消息重推
		 * @param triggerEvent
		 * @param paramMap
		 * @throws HL7Exception

		public void sendExOrderCheckMsg(String param,IUser u){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			List<Map<String,Object>> paramList = JsonUtil.readValue(param, List.class);
			paramMap.put("addingList", paramList);
			//sDMsgSendCn.sendOMPMsg("O09",paramMap);
		} */
		
		/**
		 * 022004003006
		 * @param param
		 * @param user
		 * @return
		 */
		public Integer  checkCancelAllSettle(String param,IUser user){
			Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
			if(paramMap==null){
				throw new BusException("未传入有效参数信息！");
			}
			
			String pkSettle=paramMap.get("pkSettle").toString();
			String sql="select count(1) from bl_ext_pay where pk_settle=? and sysname='Y160' and flag_pay = '1'";
			Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkSettle});
			return count;
		}
}
