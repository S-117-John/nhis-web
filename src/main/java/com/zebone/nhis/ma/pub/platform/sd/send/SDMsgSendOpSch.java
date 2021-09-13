package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.segment.MFE;
import ca.uhn.hl7v2.model.v24.segment.MFI;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.create.CreateOpMsg;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDOpMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.MFN_M01_Z11;
import com.zebone.nhis.ma.pub.platform.sd.vo.Z11;
import com.zebone.platform.common.support.UserContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 深大门诊排班，号源消息构建
 * 此类消息构建包含：资源排班，预约，号源等业务消息的构建
 * @author jd_em
 *
 */
@Service
public class SDMsgSendOpSch {


	@Resource
	private CreateOpMsg createOpMsg;
	@Resource
	private SDOpMsgMapper sDOpMsgMapper;
	@Resource
	private SDHl7MsgHander sdHl7MsgHander;


	/**
	 * 发送排班字典消息
	 * @param paramMap{status:操作状态（），pksch ：list集合}
	 * @throws HL7Exception
	 */
	@SuppressWarnings("unchecked")
	public void sendSchDict(Map<String,Object> paramMap) throws HL7Exception{
		List<String> pkSchs=(List<String>) paramMap.get("pkSchs");
		if(pkSchs==null ||pkSchs.size()==0) {
			return;
		}
		List<Map<String,Object>> resMapList=sDOpMsgMapper.qrySchDict(pkSchs);
		if(resMapList==null || resMapList.size()==0) {
			return;
		}
		for (Map<String, Object> map : resMapList) {
			String status=SDMsgUtils.getPropValueStr(map, "euStatus");
			String flagStop=SDMsgUtils.getPropValueStr(map, "flagStop");
			//String flagModi=SDMsgUtils.getPropValueStr(map, "flagModi");
			//  发消息控制： 只有发布状态的排班才发送消息（ status='8' || saveAudit 取消发布）
			if(!("8".equals(status) || paramMap.containsKey("saveAudit"))){
				continue;
			}
			//拼接消息
			String msgId = SDMsgUtils.getMsgId();
			MFN_M01_Z11 mfn = new MFN_M01_Z11();
			MSH msh=mfn.getMSH();
			MFI mfi=mfn.getMFI();
			MFE mfe=mfn.getMFE();
			map.put("msgid",msgId);
			map.put("msgtype", "MFN");
			map.put("triggerevent", "M01");
			
			createOpMsg.createMSHMsg(msh, map);

			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue("OPRSCHEMA");
			mfi.getMfi1_MasterFileIdentifier().getText().setValue("排班信息");
			mfi.getMfi1_MasterFileIdentifier().getNameOfCodingSystem().setValue("NHIS");
			mfi.getMfi3_FileLevelEventCode().setValue("UPD");
			mfi.getMfi4_EnteredDateTime().getTimeOfAnEvent().setValue(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
			mfi.getMfi5_EffectiveDateTime().getTimeOfAnEvent().setValue(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
			mfi.getMfi6_ResponseLevelCode().setValue("AL");

			ST key = new ST(mfn);
    		//创建MFE   status：发布：8  取消发布：0            flagStop：停止：1    恢复：0
			String eventCode="";
			if("8".equals(status) ){
				//发布
				if("0".equals(flagStop)){
					if("update".equals(SDMsgUtils.getPropValueStr(paramMap, "operation"))){
						//修改
						eventCode="MUP";
					}else{
						//新增
						eventCode="MAD";
					}
				}else{
					//取消-失效
					eventCode="MDL";
				}
			}else if("0".equals(status) || "1".equals(flagStop)){
				//恢复操作  ，取消发布 //取消-失效
				eventCode="MDL";
			}
    		mfe.getRecordLevelEventCode().setValue(eventCode);
    		key.setValue(SDMsgUtils.getPropValueStr(map, "pkSch"));
    		mfe.getPrimaryKeyValueMFE(0).setData(key);
    		mfe.getPrimaryKeyValueType(0).setValue("CE");
    		createZ11(map,mfn);
    		String msg =  SDMsgUtils.getParser().encode(mfn);
    		//如果消息没有Z11 段直接返回 并记录日志
    		if(!msg.contains("Z11")){
    			continue;
    		}
    		sdHl7MsgHander.sendMsg(msgId, msg);
		}
	}

	/**
	 * 组装Z11消息体
	 * @param map
	 * @param mfn
	 * @throws HL7Exception
	 */
	private void createZ11(Map<String,Object> map,MFN_M01_Z11 mfn) throws HL7Exception {
		boolean check = "30".equals(SDMsgUtils.getPropValueStr(map, "dtMedicaltype"));
		//体检判断标志
		List<Map<String, Object>> schInfoList  = sDOpMsgMapper.qryArrGroup(map);
		int size = schInfoList.size();
		if(size==0) {
			return;
		}
		for (int j = 0; j < size; j++) {
			Map<String, Object>  schInfoMap = schInfoList.get(j);
			Z11 z11 = mfn.getACK_ZRN_Z11Loop(j).getZ11();
			if( j ==0) {
				map.putAll(sDOpMsgMapper.qrySchDictByOneKey(schInfoMap).get(0));
			} else{
				String pk0= SDMsgUtils.getPropValueStr(schInfoList.get(j-1),"pkSch");
				String pk1 = SDMsgUtils.getPropValueStr(schInfoList.get(j),"pkSch");
				if (!pk0.equals(pk1)){
					map.putAll(sDOpMsgMapper.qrySchDictByOneKey(schInfoMap).get(0));
				}
			}
			//z11-1 序号 //唯一号
			z11.getID().setValue(SDMsgUtils.getPropValueStr(map, "pkSch")+"#"+SDMsgUtils.getPropValueStr(schInfoMap,"timegroup").split("-")[0].substring(8,14));
			//z11-2 排班类型 0科室/1医生
			z11.getSCHEMA_TYPE().setValue(SDMsgUtils.getPropValueStr(map, "euRestype"));
			//z11-3 看诊日期
			z11.getSEE_DATE().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"timegroup").split("-")[0].substring(0, 8));
			//z11-4 星期
			z11.getWEEK().setValue(SDMsgUtils.getPropValueStr(map, "weekNo"));
			//z11-5 开始日期
			z11.getBEGIN_TIME().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"timegroup").split("-")[0]);
			//z11-6 结束日期
			z11.getEND_TIME().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"timegroup").split("-")[1]);
			//z11-7 科室代号
			z11.getDEPT_CODE().setValue(SDMsgUtils.getPropValueStr(map,"codeDept"));
			//z11-8 科室名称
			z11.getDEPT_NAME().setValue(SDMsgUtils.getPropValueStr(map,"nameDept"));
			//z11-9 医师代号
			z11.getDOCT_CODE().setValue("".equals(SDMsgUtils.getPropValueStr(map,"codeEmp"))?SDMsgUtils.getPropValueStr(map,"codeDept"):SDMsgUtils.getPropValueStr(map,"codeEmp"));
			//z11-10 医生姓名
			z11.getDOCT_NAME().setValue("".equals(SDMsgUtils.getPropValueStr(map,"nameEmp"))?SDMsgUtils.getPropValueStr(map,"nameDept"):SDMsgUtils.getPropValueStr(map,"nameEmp"));

			//z11-11 医生类型
			String dtEmptype=SDMsgUtils.getPropValueStr(map, "dtEmptype");
			if("1".equals(dtEmptype)){
				z11.getDOCT_TYPE().setValue("1");
			}else if("5".equals(dtEmptype) || "6".equals(dtEmptype)){
				z11.getDOCT_TYPE().setValue("2");
			}
			//z11-12  来人挂号限额
			z11.getREG_LMT().setValue("");
			//z11-13  挂号已挂
			z11.getREGED().setValue("");
			//z11-14  当前时间分组限额 （实际：总号数 countnum）
			z11.getTEL_LMT().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"countnum"));
			//z11-15  当前时间已挂号 （实际：可预约数 rmngnum）
			z11.getTEL_REGED().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"rmngnum"));
			//z11-16  当前时间已预约 （实际：已使用 usenum）
			z11.getTEL_REGING().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"usenum"));
			//z11-17 特诊挂号限额
			z11.getSPE_LMT().setValue("");
			//z11-18 特诊已挂
			z11.getSPE_REGED().setValue("");
			//z11-19 1正常/0停诊
			String status=SDMsgUtils.getPropValueStr(map, "euStatus");
			String flagStop=SDMsgUtils.getPropValueStr(map, "flagStop");
			String delFlag=SDMsgUtils.getPropValueStr(map, "delFlag");
			if("8".equals(status)&&"0".equals(flagStop)&&"0".equals(delFlag) ){
				//未停止，未删除，已发布的为正常 其他的为停诊
				z11.getVALID_FLAG().setValue("1");
			}else {
				//恢复操作  ，取消发布  1正常/0停诊
				z11.getVALID_FLAG().setValue("0");
			}
			//z11-20 1加号/0否
			z11.getAPPEND_FLAG().setValue( Integer.parseInt(SDMsgUtils.getPropValueStr(map, "cntAdd"))>0?"1":"0");
			//z11-21	REASON_NO	停诊原因
			//z11-22	REASON_NAME	停诊原因名称
			//z11-23  停止人 23
			z11.getSTOP_OPCD().setValue(UserContext.getUser().getNameEmp());
			//z11-24 STOP_DATE	停止时间
			//z11-25 ORDER_NO	顺序号
			//z11-26 挂号级别代码
			z11.getREGLEVL_CODE().setValue(SDMsgUtils.getPropValueStr(map, "codeSrv"));
			//z11-27 REGLEVL_NAME	挂号级别名称
			z11.getREGLEVL_NAME().setValue(SDMsgUtils.getPropValueStr(map, "nameSrv"));
			//z11-28 REMARK	备注 是否专病
			z11.getREMARK().setValue(SDMsgUtils.getPropValueStr(map, "flagspecdise"));
			//z11-29 OPER_CODE	操作员
			z11.getOPER_CODE().setValue(UserContext.getUser().getNameEmp());
			//z11-30 OPER_DATE	最近改动日期
			z11.getOPER_DATE().setValue(SDMsgUtils.getPropValueStr(map, "ts"));
			//z11-31 	NOON_ID 午别
			String codeDateslot=SDMsgUtils.getPropValueStr(map, "codeDateslot");
			String namDateSlot="";
			if("0101".equals(codeDateslot)||"005".equals(codeDateslot)||"0601".equals(codeDateslot)||"0402".equals(codeDateslot)){
				namDateSlot="1";
			}else if("0102".equals(codeDateslot)||"006".equals(codeDateslot)||"0602".equals(codeDateslot)||"0401".equals(codeDateslot)){
				namDateSlot="2";
			}else if("0201".equals(codeDateslot)){
				namDateSlot="早班";
			}else if("0202".equals(codeDateslot)){
				namDateSlot="中班";
			}else if("0203".equals(codeDateslot)){
				namDateSlot="晚班";
			}else if("0301".equals(codeDateslot)||"0603".equals(codeDateslot)){
				namDateSlot="3";
				if(check && "0301".equals(codeDateslot)) {
					namDateSlot="连续";
				}
			}else{
				namDateSlot = codeDateslot;
			}
			//z11-31	NOON_ID	午别
			z11.getNOON_ID().setValue(namDateSlot);
			//z11-32	INTERVAL	排班时间间隔
			z11.getINTERVAL().setValue(SDMsgUtils.getPropValueStr(map, "minutePer"));
			//z11-33	WEB_LMT	网站挂号限额
			z11.getWEB_LMT().setValue("");
			//z11-34	WEIXIN_LMT	微信挂号限额
			z11.getWEIXIN_LMT().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"countnum"));
			//z11-35	OTHER_LMT	预留挂号限额
			z11.getOTHER_LMT().setValue("");

			//新字段文档未定定义
			//36 可预约号
			z11.getRmngNum().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"rmngnum"));
			//37 科室编码
			z11.getCODE_RES().setValue(SDMsgUtils.getPropValueStr(map, "codeRes"));
			//38 科室名称
			z11.getNAME_RES().setValue(SDMsgUtils.getPropValueStr(map, "nameRes"));
			
		}
	}



}
