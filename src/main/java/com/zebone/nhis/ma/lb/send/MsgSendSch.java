package com.zebone.nhis.ma.lb.send;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.lb.dao.SelfMsgMapper;
import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7MsgHander;
import com.zebone.nhis.ma.pub.platform.zb.model.QBP_Z11;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.nhis.sch.plan.service.SchService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.group.MFN_M01_MF;
import ca.uhn.hl7v2.model.v24.message.MFN_M01;
import ca.uhn.hl7v2.model.v24.message.SRR_S01;
import ca.uhn.hl7v2.model.v24.segment.MFE;
import ca.uhn.hl7v2.model.v24.segment.MFI;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.SCH;
import ca.uhn.hl7v2.model.v24.segment.Zxx;

/**
 * 自助机排班信息
 * @author 18518
 *
 */
@Service
public class MsgSendSch {
	@Resource
	private Hl7MsgHander msgHander;
	
	@Autowired
	private SchService schService;
	
	@Resource
	private SelfMsgMapper selfMsgMapper;
	
	
	/**
	 * 排班资源信息查询
	 * @param string
	 * @return
	 * @throws HL7Exception 
	 */
	public String sendSchResource(String triggerEvent,Map<String,Object> paramMap) throws HL7Exception {
		if(paramMap == null || paramMap.size()==0){
			return null;
		}
		/*//预约排班
		if(paramMap.get("schInfo").equals("Reservation")){
			paramMap.put("nowDate", paramMap.get("queryDate").toString() + "000000");
		}
		
		//根据信息得到号源
		Map<Object, Object> hashMap = new HashMap<>();
		hashMap.put("rescode", null);
		String param = ApplicationUtils.objectToJson(hashMap);
		User user = new User();
		user.setPkOrg(paramMap.get("pkOrg").toString());
		List<Map<String, Object>> schlist = schService.getSchInfoByResAndTime(param,user);*/
		
		//查询当天排班
		DateTime dt = DateTime.now();
		String nowDate="";
		if(paramMap.get("schInfo").equals("ThayDay")){
			nowDate = dt.toString("yyyyMMdd") + "000000";//当天的日期
			String nowDay = dt.toString("MM-dd");//当前日期
			String nowTime = dt.toString("HH:mm:ss");//当前时刻
			paramMap.put("nowDate", nowDate);
			paramMap.put("nowDay", nowDay);
			paramMap.put("nowTime", nowTime);
		}
		//预约排班
		if(paramMap.get("schInfo").equals("Reservation")){
			paramMap.put("nowDate", paramMap.get("queryDate").toString() + "000000");
		}
		
		//根据信息得到号源
		List<Map<String, Object>> schlist = selfMsgMapper.qrySchedulInfo(paramMap);
		if(schlist != null && schlist.size() > 0){
			/**相关的附加描述性字段--价格*/
			Map<String, Map<String, Object>> srvMap = DataBaseHelper
					.queryListToMap("select so.pk_schsrv as key_, sum(t.price) as price from sch_srv_ord so "
							+ "inner join bd_ord_item ot on so.pk_ord = ot.pk_ord "
							+ "inner join bd_item t on ot.pk_item=t.pk_item where so.del_flag = '0' and ot.del_flag = '0' " + "group by so.pk_schsrv");
			for (Map<String, Object> main : schlist) {
				if (srvMap.get(main.get("pkSchsrv")) != null) {
					main.putAll(srvMap.get(main.get("pkSchsrv")));
				}
			}
		}
		
		if (triggerEvent.equals("Z11")) {
			QBP_Z11 qbp = new QBP_Z11();
			// 字典管理
    		MFN_M01 mfn = new MFN_M01();
    		String msgId = MsgUtils.getMsgId();
    		//MSH
    		MSH msh = mfn.getMSH();
    		//创建MSH消息对象
    		MsgUtils.createMSHMsg(msh, msgId, "MFN", "M01");
    		msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
			int i, j, len;
			len = schlist.size();
			
			if(schlist == null || schlist.size()==0){
				MFI mfi = mfn.getMFI();
	    		MFN_M01_MF mf = mfn.getMF(0);
	    		ST key = new ST(mfn);
	    		MFE mfe = mf.getMFE();
	    		//创建MFI
	    		createMfiMessage(mfi);
	    		mfi.getMfi6_ResponseLevelCode().setValue("ER");//重新设定值
	    		//创建MFE
	    		mfe.getRecordLevelEventCode().setValue("MDC");
	    		mfe.getMFNControlID().setValue("当前暂无排班资源，请重新选择");
	    		key.setValue("45093");
	    		mfe.getPrimaryKeyValueMFE(0).setData(key);
	    		mfe.getPrimaryKeyValueType(0).setValue("CE");		
			}else{
				MFI mfi = mfn.getMFI();
				
	    		//循环创建Z11
				for (i = 0; i < len; i++) {
		        	Map<String, Object> map = schlist.get(i);
		        	MFN_M01_MF mf = mfn.getMF(i);
		        	MFE mfe = mf.getMFE();
		    		ST key = new ST(mfn);
		    		//组装MFI信息
		    		createMfiMessage(mfi);
		    		mfe.getRecordLevelEventCode().setValue("MUP");
		    		key.setValue(map.get("pkSch").toString());
		    		mfe.getPrimaryKeyValueMFE(0).setData(key);
		    		mfe.getPrimaryKeyValueType(0).setValue("CE");
		    		createZ11(map, mf);
		        }
			}
			String msg=MsgUtils.getParser().encode(mfn);
		    return msg;
		}
		return "";
	}

	
	
	

	
	/**
	 * 完成MFI数据封装
	 * 
	 * @param dictCode
	 *            用于主文件标识符
	 * @param mfn
	 *            当前使用的mfn
	 * @param mfi
	 *            当前使用的mfi
	 * @param i
	 *            当前传入需要发送的数据的index
	 * @return
	 * @throws HL7Exception
	 */
	private void createMfiMessage(MFI mfi)
			throws HL7Exception {
		// Master file identification
		// Master File Identifier 主文件标识符,传入表名
		mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue("OPRSCHEMA");
		mfi.getMfi1_MasterFileIdentifier().getText().setValue("排班信息");
		mfi.getMfi1_MasterFileIdentifier().getNameOfCodingSystem().setValue("NHIS");;
		// File-Level Event Code
		mfi.getMfi3_FileLevelEventCode().setValue("UPD");
		// Entered Date/Time
		mfi.getMfi4_EnteredDateTime().getTimeOfAnEvent().setValue(DateUtils.getDateTimeStr(new Date()));
		// Effective Date/Time
		mfi.getMfi5_EffectiveDateTime().getTimeOfAnEvent().setValue(DateUtils.getDateTimeStr(new Date()));
		// Response Level Code
		mfi.getMfi6_ResponseLevelCode().setValue("AL");
	}
	
	/**
	 * 拼接数据
	 * 
	 * @param strs
	 *            包含数据的String数组
	 * @param zstr
	 *            拼接后存放的String
	 * @return 返回Stirng
	 */
	private String makeZxxString(String[] strs) {
		String zstr = "";
		for (int k = 0; k < strs.length; k++) {
			String tmp = strs[k] == null ? "" : strs[k];
			if (k == 0) {
				zstr = "|" + tmp;
			} else {
				zstr = zstr + "|" + tmp;
			}
		}
		return zstr;
	}
	
	
	private String timeTodate(String time) {
		String date = DateUtils.getDate("yyyyMMdd");
		String[] split = time.split(":");
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < split.length; i++){
		 sb.append(split[i]);
		}
		String s = sb.toString();
		return date+s;
	}

	/**
	 * 组装Z11消息体
	 * @param map
	 * @param mf
	 * @throws HL7Exception
	 */
	private void createZ11(Map<String,Object> map,MFN_M01_MF mf) throws HL7Exception {
		mf.addNonstandardSegment("Z11");
		Segment Z11 = (Segment) mf.get("Z11");
		String[] strs = new String[30];
		//序号
		strs[0]=map.get("pkSch").toString();
		//排班类型
		strs[1]=map.get("euRestype").toString();
		//看诊日期
		strs[2] = DateUtils.getDateTimeStr(new Date());
		//星期
		strs[3] = DateUtils.getDayofWeek(new Date());
		//开始时间
		strs[4] = timeTodate(map.get("timeBegin").toString()); 
		//结束时间
		strs[5] = timeTodate(map.get("timeEnd").toString());
		//科室代号
		strs[6] = MsgUtils.getPropValueStr(map, "codeDept");
		//科室名称
		strs[7] = MsgUtils.getPropValueStr(map, "nameDept");
		//医师代号
		strs[8] = MsgUtils.getPropValueStr(map, "pkEmp");
		//医生姓名
		strs[9] = MsgUtils.getPropValueStr(map, "nameEmp");
		//医生类型
		strs[10] = "1";
		//来人挂号限额
		strs[11] = map.get("cntTotal").toString();
		//挂号已挂
		strs[12] = map.get("cntUsed").toString();
		//来电挂号限额
		strs[13] = map.get("cntAppt").toString();
		//来电已挂
		strs[14] = "";
		//来点预约
		strs[15] = "";
		//特诊挂号限额
		strs[16] = "";
		//特诊已挂
		strs[17] = "";
		//正常/停诊
		strs[18] = "";
		//加号
		strs[19] = "";
		//停诊原因
		strs[20] = "";
		//停诊原因名称
		strs[21] = "";
		//停止人
		strs[22] = "";
		//停止时间
		strs[23] = "";
		//顺序号
		strs[24] = "";
		//挂号级别代码
		strs[25] = map.get("code").toString();
		//挂号级别名称
		strs[26] = map.get("name").toString();
		//备注
		strs[27] = map.get("price").toString();
		//操作员
		strs[28] = "";
		//最近改动日期
		strs[29] = "";
		String makeZxxString = makeZxxString(strs);
		Z11.parse(makeZxxString);
	}
	
}
