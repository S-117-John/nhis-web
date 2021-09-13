package com.zebone.nhis.ma.pub.platform.zb.send;

import java.util.*;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.group.MFN_M01_MF;
import ca.uhn.hl7v2.model.v24.message.MFN_M01;
import ca.uhn.hl7v2.model.v24.segment.MFE;
import ca.uhn.hl7v2.model.v24.segment.MFI;
import ca.uhn.hl7v2.model.v24.segment.MSH;

import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7MsgHander;
import com.zebone.nhis.ma.pub.platform.zb.dao.MsgRecBdMapper;
import com.zebone.nhis.ma.pub.platform.zb.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * Adt消息处理
 * 
 * @author chengjia
 * 
 */
@Service
public class MsgSendBd {

	/**
	 * 添加状态
	 **/
	public static final String AddState = "_ADD";

	/**
	 * 更新状态
	 */
	public static final String UpdateState = "_UPDATE";

	/**
	 * 删除状态
	 */
	public static final String DelState = "_DELETE";

	//public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	//public SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Resource
	private Hl7MsgHander msgHander;
	
	@Autowired
	private MsgRecBdMapper msgRecBdMapper;

	private String zstr;

	/**
	 * 发送Hl7消息
	 * 
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendMfnMsg(String dictCode, List<Map<String, Object>> listMap) throws HL7Exception {
		try {
			String msgId = MsgUtils.getMsgId();
			Message message = createMfnMsg(msgId, dictCode, listMap);

			String msg = MsgUtils.getParser().encode(message);
			// 发送消息
			msgHander.sendMsg(msgId, msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			loger.error("字典{},{}",dictCode,e.getMessage());
			throw new HL7Exception("发送消息失败！");
		}
	}

	/**
	 * 发送Mfn消息
	 * 
	 * @param dictCode
	 * @param listMap
	 * @throws HL7Exception
	 */
	private Message createMfnMsg(String msgId, String dictCode,
			List<Map<String, Object>> listMap){
		if (listMap == null || listMap.size() == 0)
			return null;
		// 字典管理
		MFN_M01 mfn = new MFN_M01();
		// MSH
		MSH msh = mfn.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "MFN", "M01");
		MFI mfi = mfn.getMFI();
		//mfi.getMasterFileIdentifier().getIdentifier().setValue(dictCode);
		int i, len;
		len = listMap.size();
		for (i = 0; i < len; i++) {
			Map<String, Object> map = listMap.get(i);
			MFN_M01_MF mf = mfn.getMF(i);
			ST key = new ST(mfn);
			MFE mfe = mf.getMFE();
			if (dictCode.equals("Department")) {
				//科室字典信息
				createMsgForDepartment_Z01(dictCode, map, mfi, mf, mfe, key);
			} else if (dictCode.equals("Undrug")) {
				// 非药品收费项目分支
				createMsgForUnDrug(dictCode, map, mfn, mfi, i);
			} else if (dictCode.equals("UndrugGroup")) {
				// 非药品收费组套分支
				createMsgForUndrugGroup(dictCode, map, mfn, mfi, i);
			}
			else if (dictCode.equals("Drug")) {
				createMsgForDrug_Z06(dictCode, map, mfi, mf, mfe,key);//药品字典
			} else if(dictCode.equals("UserInfo")){
				//用户字典信息
				createMsgForUserInfo_ZU0(dictCode, map, mfi, mf, mfe, key);
			} else if(dictCode.equals("Employee")){
				//人员字典信息
				createMsgForEmployee_Z02(dictCode, map, mfi, mf, mfe, key);
			} else if(dictCode.equals("DeptStat")){
				//科室与病区关系字典
				createMsgForDeptStat_Z03(dictCode, map, mfi, mf, mfe, key);
			} else if(dictCode.equals("yz_order_item")){
				//医嘱字典
				createMsgForOrderItem_ZA1(dictCode, map, mfi, mf, mfe, key);
			} else if(dictCode.equals("yz_supply")){
				//医嘱用法
				createMsgForYzSupply_ZA2(dictCode, map, mfi, mf, mfe, key);
			} else if(dictCode.equals("yz_frequency")){
				//医嘱频率
				createMsgForYzFrequency_ZA3(dictCode, map, mfi, mf, mfe, key);
			} else if(dictCode.equals("ICD10")){
				//ICD10信息字典
				createMsgForICD10_Z10(dictCode, map, mfi, mf, mfe, key);
			} else if(dictCode.equals("Operation")){
				//手术编码信息
				createMsgForOperation_ZD1(dictCode, map, mfi, mf, mfe, key);
			} else if(dictCode.equals("BdDefdocType")){
				//公共字典信息
				createMsgForBdDefdocType_Z99(dictCode, map, mfi, mf, mfe, key);
			} else if(dictCode.equals("Bed")){
				//床位字典
				createMsgForBed_Z07(dictCode, map, mfi, mf, mfe, key);
			}
		}

		return mfn;
	}			
	
	/**
	 * 完善MSH的数据
	 * @param mfn
	 * @throws DataTypeException 
	 */
	private void completeMshMessage(MFN_M01 mfn){
		try {
			
		MSH msh = mfn.getMSH();
		//Accept Acknowledgment Type
		msh.getAcceptAcknowledgmentType().setValue("AL");
		//Application Acknowledgment Type
		msh.getApplicationAcknowledgmentType().setValue("AL");
		//Country Code
		msh.getCountryCode().setValue("CHN");
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("MSH失败{}",e.getMessage());
		}
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
	private void createMfiMessage(String dictCode, MFN_M01 mfn, MFI mfi){
		// Master file identification
		// Master File Identifier 主文件标识符,传入表名
		try {
			
		if (dictCode.equals("Undrug")) {
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			mfi.getMfi1_MasterFileIdentifier().getText().setValue("非药品物价信息");
			mfi.getMfi1_MasterFileIdentifier().getNameOfCodingSystem()
					.setValue("HIS");
			
		} else if(dictCode.equals("UndrugGroup")) {
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			mfi.getMfi1_MasterFileIdentifier().getText().setValue("非药品套餐信息");
			mfi.getMfi1_MasterFileIdentifier().getNameOfCodingSystem()
					.setValue("HIS");
			
		} else if(dictCode.equals("Bed")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			mfi.getMasterFileIdentifier().getText().setValue("床位信息");
			mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");
			
		} else if(dictCode.equals("UserInfo")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			
		} else if(dictCode.equals("Department")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			mfi.getMasterFileIdentifier().getText().setValue("科室");
			mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");
			
		} else if(dictCode.equals("Employee")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			mfi.getMasterFileIdentifier().getText().setValue("人员信息");
			mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");
			
		} else if(dictCode.equals("DeptStat")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			mfi.getMasterFileIdentifier().getText().setValue("科室");
			mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");
			
		} else if(dictCode.equals("Drug")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			mfi.getMasterFileIdentifier().getText().setValue("药品信息");
			mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");
			
		} else if(dictCode.equals("yz_order_item")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			
		} else if(dictCode.equals("yz_supply")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			
		} else if(dictCode.equals("yz_frequency")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			
		} else if(dictCode.equals("BdDefdocType")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			mfi.getMasterFileIdentifier().getText().setValue("药品信息");
			mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");
			
		} else if(dictCode.equals("ICD10")){
			
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			mfi.getMasterFileIdentifier().getText().setValue("诊断类型信息");
			mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");
			
		} else if(dictCode.equals("Operation")){
			
			mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
			
		}

		// File-Level Event Code
		mfi.getMfi3_FileLevelEventCode().setValue("UPD");
		// Entered Date/Time
		mfi.getMfi4_EnteredDateTime().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
		// Effective Date/Time
		mfi.getMfi5_EffectiveDateTime().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
		// Response Level Code
		mfi.getMfi6_ResponseLevelCode().setValue("AL");

		} catch (Exception e) {
			loger.error("字典MFI{},{}",dictCode,e.getMessage());
		}
	}

	/**
	 * 发送非药品组套收费信息,注意:组套信息可能包含多个
	 * 
	 * @param dictCode
	 * @param listMap
	 * @param mfn
	 * @throws HL7Exception
	 */
	private void createMsgForUndrugGroup(String dictCode,
			Map<String, Object> map, MFN_M01 mfn, MFI mfi, int i)
			{
		
		//完善MSH
		completeMshMessage(mfn);				
		// 完成Mfi
		createMfiMessage(dictCode, mfn, mfi);

		// MF
		MFN_M01_MF mf = null;

		switch (MsgUtils.getPropValueStr(map, "state")) {
		case AddState:
			mf = createMfMessage(map, mfn, i, "MAD", "Z05");
			createMsgForUndrug_Z05(map, mf);

			break;
		case UpdateState:
			mf = createMfMessage(map, mfn, i, "MUP", "Z05");
			createMsgForUndrug_Z05(map, mf);

			break;
		case DelState:
			mf = createMfMessage(map, mfn, i, "MDL", "Z05");
			createMsgForUndrug_Z05(map, mf);

			break;

		default:
			break;
		}
	}

	/**
	 * 拼接Mf信息
	 * @param map
	 * @param mfn
	 * @param i
	 * @param state
	 * @param segment
	 * @return
	 * @throws HL7Exception
	 */
	private MFN_M01_MF createMfMessage(Map<String, Object> map, MFN_M01 mfn,
			int i, String state, String segment){
		
		// MF = MFE + Zxx
		MFN_M01_MF mf = mfn.getMF(i);
		try{
		// MFE
		MFE mfe = mf.getMFE();

		// Record-Level Event Code
		mfe.getMfe1_RecordLevelEventCode().setValue(state);
		// Primary Key Value
		ST key = new ST(mfn);
		
		key.setValue(MsgUtils.getPropValueStr(map, "code"));
		//因为bd_item_set没有code字段,所以使用此方法,后期优化
		if(segment.equals("Z05")) key.setValue(transItemCodeFromPk(MsgUtils.getPropValueStr(map, "pkItemChild")));
			
		mfe.getMfe4_PrimaryKeyValueMFE(0).setData(key);
		// Primary Key Value Type
		mfe.getMfe5_PrimaryKeyValueType(0).setValue("CE");

		// Zxx
		mf.addNonstandardSegment(segment);

		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Mf失败{},{}",segment,e.getMessage());
		}
		return mf;
	}

	/**
	 * 发送非药品收费信息,注意:组套信息可能包含多个
	 * 
	 * @param dictCode
	 * @param listMap
	 * @param mfn
	 * @throws HL7Exception
	 */
	private void createMsgForUnDrug(String dictCode, Map<String, Object> map,
			MFN_M01 mfn, MFI mfi, int i){
		//完善MSH
		completeMshMessage(mfn);
		// 完成MFI
		createMfiMessage(dictCode, mfn, mfi);

		MFN_M01_MF mf = null;

		switch (MsgUtils.getPropValueStr(map, "state")) {
		case AddState:
			mf = createMfMessage(map, mfn, i, "MAD", "Z04");
			createMsgForUndrug_Z04(map, mf);

			break;
		case UpdateState:
			mf = createMfMessage(map, mfn, i, "MUP", "Z04");
			createMsgForUndrug_Z04(map, mf);

			break;
		case DelState:
			mf = createMfMessage(map, mfn, i, "MDL", "Z04");
			createMsgForUndrug_Z04(map, mf);

			break;
		default:
			break;
		}
	}

	

	/**
	 * Undrug
	 * @param map
	 * @param mf
	 * @throws HL7Exception
	 */
	private void createMsgForUndrug_Z04(Map<String, Object> map, MFN_M01_MF mf)
			{
		try{
		//mf.addNonstandardSegment("Z04");
		Segment Z04 = (Segment) mf.get("Z04");
		String[] strs = new String[28];
		
		// 从map中通过key取对应的数据
		// 非药品编码
		strs[0] = MsgUtils.getPropValueStr(map, "code");
		// 用户自定义（助记）码
		strs[1] = MsgUtils.getPropValueStr(map, "spcode");
		// 非药品名称
		strs[2] = MsgUtils.getPropValueStr(map, "name");
		// 系统类别
		if(("04").equals(MsgUtils.getPropValueStr(map, "dtItemtype"))){
			strs[3] = "JC";
		}else {
			strs[3] = MsgUtils.getPropValueStr(map, "dtItemtype");
		}
		// 最小费用代码
		strs[4] ="";
		// 国家编码
		strs[5] = MsgUtils.getPropValueStr(map, "");
		// 国际标准代码
		strs[6] = MsgUtils.getPropValueStr(map, "");
		// 三甲价
		strs[7] = MsgUtils.getPropValueStr(map, "price");
		// 单位
		if(!("").equals(MsgUtils.getPropValueStr(map, "pkUnit"))){
			strs[8] = transUnitNameFromPk(MsgUtils.getPropValueStr(map, "pkUnit"));
		}else{
			strs[8] = "";
		}
		// 特定治疗项目 0假 1真
		strs[9] = MsgUtils.getPropValueStr(map, "");
		// 计划生育标记
		strs[10] = MsgUtils.getPropValueStr(map, "");
		// 确认标志
		strs[11] = MsgUtils.getPropValueStr(map, "flagActive");
		// 有效性标识,因为目前标准不一致,所以进行转换
		strs[12] = transFlagActive(MsgUtils.getPropValueStr(map, "flagActive"));
		// 规格
		strs[13] = MsgUtils.getPropValueStr(map, "spec");
		// 执行科室
		strs[14] = UserContext.getUser().getPkDept();
		// 单位标识
		strs[15] = MsgUtils.getPropValueStr(map, "flagSet");
		// 备注
		strs[16] = MsgUtils.getPropValueStr(map, "note");
		// 适用范围
		strs[17] = MsgUtils.getPropValueStr(map, "");
		// 拼音码
		strs[18] = MsgUtils.getPropValueStr(map, "spcode");
		// 五笔码
		strs[19] = MsgUtils.getPropValueStr(map, "dCode");
		// 操作员
		strs[20] = UserContext.getUser().getCodeEmp();
		// 操作日期
		strs[21] = MsgUtils.getPropValueStr(map, "modityTime");
		// 申请单类型
		strs[22] = MsgUtils.getPropValueStr(map, "");
		// 样本类型
		strs[23] = MsgUtils.getPropValueStr(map, "");
		// 特殊标记4
		strs[24] = MsgUtils.getPropValueStr(map, "");
		// 特殊标记2
		strs[25] = MsgUtils.getPropValueStr(map, "");
		// 特殊标记1
		strs[26] = MsgUtils.getPropValueStr(map, "");
		// 特殊标记
		strs[27] = MsgUtils.getPropValueStr(map, "");

		Z04.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Z04失败{}",e.getMessage());
		}
	}

	/**
	 * UndruGroup
	 * @param map
	 * @param mf
	 * @throws HL7Exception
	 */
	private void createMsgForUndrug_Z05(Map<String, Object> map,
			MFN_M01_MF mf){
		try{
		mf.addNonstandardSegment("Z05");
		Segment Z05 = (Segment) mf.get("Z05");
		String[] strs = new String[11];

		// 从map中通过key取对应的数据
		// 组套编码
		strs[0] = transItemCodeFromPk(MsgUtils.getPropValueStr(map, "pkItem"));
		// 组套名称
		strs[1] = transItemNameFromPk(MsgUtils.getPropValueStr(map, "pkItem"));
		// 非药品编码
		strs[2] = transItemCodeFromPk(MsgUtils.getPropValueStr(map, "pkItemChild"));
		// 非药品名称
		strs[3] = transItemNameFromPk(MsgUtils.getPropValueStr(map, "pkItemChild"));
		// 输入码
		strs[4] = "";
		// 有效性,用del还是active?
		strs[5] = transFlagActive(MsgUtils.getPropValueStr(map, "delFlag"));
		// 数量
		// 将int转换成string
		strs[6] = MsgUtils.getPropValueStr(map, "quan");
		// 顺序号
		strs[7] = "0";
		// 操作员
		strs[8] = UserContext.getUser().getCodeEmp();
		// 操作日期
		strs[9] = MsgUtils.PropDateSting(new Date());
		// 系统类别
		strs[10] = "";

		Z05.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Z05失败{}",e.getMessage());
		}
	}

	/**
	 * 拼接Z段数据
	 * 
	 * @param strs
	 *            包含数据的String数组
	 * @param zstr
	 *            拼接后存放的String
	 * @return 返回Stirng
	 */
	private String makeZxxString(String[] strs, String zstr) {

		zstr = "";
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

	/**
	 * 转换标志,将1转成0,将0,null转成1
	 * 
	 * @param value
	 *            需要转换的值
	 * @return
	 */
	private String transFlagActive(String value) {
		if (value.equals("1")) {
			return "0";
		} else {
			return "1";
		}
	}

	/**
	 * 通过主键对单位进行转换
	 * 
	 * @param pk
	 *            主键
	 * @return 单位名称
	 */
	private String transUnitNameFromPk(String pk) {

		Map<String, Object> resultMap = DataBaseHelper.queryForMap(
				"SELECT NAME from BD_UNIT where PK_UNIT = ?", pk);
		String name = (String) resultMap.get("name");
		if (null == name)
			name = "";
		return name;
	}

	/**
	 * 通过主键对名称进行转换
	 */
	private String transItemNameFromPk(String pk) {
		Map<String, Object> resultMap = DataBaseHelper.queryForMap(
				"SELECT NAME from BD_ITEM WHERE PK_ITEM = ?", pk);
		String name = (String) resultMap.get("name");
		if (null == name)
			name = "";
		return name;
	}

	/**
	 * 通过主键对编码进行转换
	 */
	private String transItemCodeFromPk(String pk) {
		Map<String, Object> resultMap = DataBaseHelper.queryForMap(
				"SELECT CODE from BD_ITEM WHERE PK_ITEM = ?", pk);
		String code = (String) resultMap.get("code");
		if (null == code)
			code = "";
		return code;
	}

	/**
	 * 发送药品消息
	 * @param dictCode
	 * @param map
	 * @param mf
	 * @param mfe
	 * @throws HL7Exception
	 */
	private void createMsgForDrug_Z06(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try {
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		Map<String, Object> bdPdMap = null;
		if(null != map.get("pd")){
			bdPdMap=(Map<String, Object>)MapUtils.beanToMap(map);
		}
		
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "opeType"));
		key.setValue(MsgUtils.getPropValueStr(map, "code"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");

		mf.addNonstandardSegment("Z06");
		Segment z06 = (Segment) mf.get("Z06");

		/** 查询药品基本信息,编码,名称等 */
		List<Map<String,Object>> list = msgRecBdMapper.queryPdInfoList(map);
		if(list != null && list.size() > 0){
			Map<String, Object> nameMap = list.get(0);
			if(nameMap != null) map.putAll(nameMap);
		}
		
		String[] strs = new String[62];
		strs[0] = MsgUtils.getPropValueStr(map, "code");// √ 药品编码
		strs[1] = MsgUtils.getPropValueStr(map, "name");// √ 商品名称
		strs[2] = "";// √ 商品名自定义码
		strs[3] = "";// √ 通用名--名称
		strs[4] = "";// √ 通用名自定义码
		strs[5] = "";//学名
		strs[6] = "";//学名自定义码
		strs[7] = "";//别名
		strs[8] = "";//别名自定义码
		strs[9] = "";//英文通用名
		strs[10] = MsgUtils.getPropValueStr(map, "dtDosageName");//英文别名,剂型编码（常数DOSAGEFORM）
		strs[11] = "";//英文名
		strs[12] = "";//国际编码
		strs[13] = "";// √ 国家编码
		strs[14] = getDrugSysClass(map);// √ 系统类别(约定值) = 药品类别
		strs[15] = getDrugFeeCode(map);// √ 最小费用代码（常数MINFEE）
		strs[16] = strs[14];// √ 药品类别（常数ITEMTYPE）
		strs[17] = getDrugQuality(map);// √ 药品性质（常数DRUGQUALITY）
		strs[18] = MsgUtils.getPropValueStr(map, "spec");// √ 规格
		strs[19] = MsgUtils.getPropValueStr(map, "price");// √ 零售价
		strs[20] = MsgUtils.getPropValueStr(map, "price");// √ 最新购入价
		strs[21] = MsgUtils.getPropValueStr(map, "unitPackName");// √ 包装单位-名称
		strs[22] = MsgUtils.getPropValueStr(map, "packSize");// √ 包装数
		strs[23] = MsgUtils.getPropValueStr(map, "unitMinName");// √ 最小单位-名称
		strs[24] = MsgUtils.getPropValueStr(map, "dtDosage");// √ 剂型编码（常数DOSAGEFORM）
		strs[25] = MsgUtils.getPropValueStr(map, "vol");// √ 基本剂量
		strs[26] = MsgUtils.getPropValueStr(map, "unitVolName");// √ 剂量单位-名称
		strs[27] = MsgUtils.getPropValueStr(map, "codeSupply");//用法编码（CIS提供）
		strs[28] = MsgUtils.getPropValueStr(map, "codeFreq");//频次编码（CIS提供）
		strs[29] = "";// √ 一次用量
		strs[30] = "";//注意事项
		strs[31] = "";//一级药理作用 -字典PhaFunction
		strs[32] = "";//二级药理作用 -字典PhaFunction
		strs[33] = "";//三级药理作用 -字典PhaFunction
		strs[34] = getDrugValidSate(map);// √ 有效性标志（1 在用 0 停用 2 废弃）
		strs[35] = getDrugSelfFlag(map);// √ 自制标志（0-非自产，1-自产）
		strs[36] = "";// √ OCT标志（0非处方药 1处方药）
		strs[37] = MsgUtils.getPropValueStr(map, "flagGmp");// √ GMP标志 （0非GMP,1GMP）
		strs[38] = MsgUtils.getPropValueStr(map, "flagSt");// √ 是否需要试敏（0不需要1需要）
		strs[39] = MsgUtils.getPropValueStr(map, "companyCode");// √ 最新供药公司-字典PhaCompany
		strs[40] = MsgUtils.getPropValueStr(map, "produceName");//产地 ,生产厂家名称
		strs[41] = MsgUtils.getPropValueStr(map, "produceCode");// √ 生产厂家 - 字典PhaProduct
		strs[42] = MsgUtils.getPropValueStr(map, "apprNo");//批文信息
		strs[43] = "";//有效成分
		strs[44] = "";// √ 临嘱拆分类型（0 可拆包装单位 1 不能拆包装单位）
		strs[45] = "0";// √ 参考零售价2
		strs[46] = "";// √ 扩展数据3（长嘱拆分）
		strs[47] = MsgUtils.getPropValueStr(map, "note");//备注
		strs[48] = MsgUtils.getPropValueStr(map, "spcode");// √ 商品名拼音码
		strs[49] = "";// √ 商品名五笔码
		strs[50] = MsgUtils.getPropValueStr(map, "spcodePdGn");// √ 通用名拼音码
		strs[51] = MsgUtils.getPropValueStr(map, "dCodePdGn");// √ 通用名五笔码
		strs[52] = "";//学名拼音码
		strs[53] = "";//学名五笔码
		strs[54] = "";//别名拼音码
		strs[55] = "";//别名五笔码
		strs[56] = "admin";// √ 操作员
		strs[57] = MsgUtils.PropDateSting(new Date());// √ 操作时间--当前时间
		strs[58] = "";// √ 特殊标记4（门诊（打包收费标记）0否 1是 ）
		strs[59] = "";// √ 特殊标记2（限制标志   0否 1是 ）
		strs[60] = "";// √ 特殊标记1（住院打包收费标志   0否 1是 ）
		strs[61] = "";// √ 项目等级（药品使用等级）

		z06.parse(makeZxxString(strs, zstr));

		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Z06失败{}",e.getMessage());
		}
	}
		
	
	/**
	 * 发送科室信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForDepartment_Z01(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try {
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "rleCode"));
		key.setValue(MsgUtils.getPropValueStr(map, "codeDept"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");

		mf.addNonstandardSegment("Z01");
		Segment Z01 = (Segment) mf.get("Z01");
		
		String[] strs = new String[14];
		// 科室编码
		strs[0] = MsgUtils.getPropValueStr(map, "codeDept");
		// 科室名称
		strs[1] = MsgUtils.getPropValueStr(map, "nameDept");
		// 科室英文
		strs[2] = "";
		// 科室简称
		strs[3] = MsgUtils.getPropValueStr(map, "shortname");
		// 科室类型
		//strs[4] = MsgUtils.getPropValueStr(map, "dtDepttype");
		// 科室类型dtDepttype
		String dtEmptype = map.get("dtDepttype").toString();
		if(dtEmptype != null && dtEmptype.equals("01")){//医疗
			//职务代号(待确定)
			strs[4]="D";
		}else if(dtEmptype.equals("02")){//护理
			strs[4]="N";
		}else if(dtEmptype.equals("03")){//医技
			strs[4]="T";
		}else if(dtEmptype.equals("04")){//药剂
			strs[4]="P";
		}else if(dtEmptype.equals("05")){//财务
			strs[4]="F";
		}else if(dtEmptype.equals("06")){//行政
			strs[4]="X";
		}else if(dtEmptype.equals("07")){//管理
			strs[4]="G";
		}else if(dtEmptype.equals("08")){//其他
			strs[4]="O";
		}
		// 是否挂号科室
		strs[5] = "";
		// 是否核算科室
		strs[6] = "";
		// 特殊科室属性
		strs[7] = "";
		// 有效性标志
		strs[8] = MsgUtils.getPropValueStr(map, "flagActive");
		// 顺序号
		strs[9] = MsgUtils.getPropValueStr(map, "sortno");
		// 拼音码
		strs[10] = MsgUtils.getPropValueStr(map, "pyCode");
		// 自定义码
		strs[11] = MsgUtils.getPropValueStr(map, "dCode");
		// 操作员
		strs[12] = MsgUtils.getPropValueStr(map, "empCode");// @todo
		// 操作时间
		strs[13] = MsgUtils.PropDateSting(new Date());

		Z01.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Z01失败{}",e.getMessage());
		}
	}

	
	/**
	 * 发送用户字典信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForUserInfo_ZU0(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
         try {
        	 if(map!=null){

		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		Map<String,Object> userMap=null;
		if(map.get("user")!=null){
			userMap=(Map<String,Object>)map.get("user");
		}else{
			userMap=map;
		}
		if(userMap!=null){
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "rleCode"));
		key.setValue(MsgUtils.getPropValueStr(userMap, "codeUser"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");
		
		mf.addNonstandardSegment("ZU0");
		Segment ZU0 = (Segment) mf.get("ZU0");
		
		//获取用户所在用户组名称
		//String codeusrgrp = DataBaseHelper.queryForScalar("select code_usrgrp from BD_OU_USRGRP where pk_usrgrp=?", String.class, MsgUtils.getPropValueStr(userMap,"pkUsrgrp"));
		//String codeusrgrp = DataBaseHelper.queryForScalar("select * from (SELECT CODE_DEPT FROM bd_ou_dept bd LEFT JOIN bd_ou_empjob empjob ON empjob.pk_dept = bd.pk_dept WHERE empjob.Pk_emp = ?) where rownum=1", String.class, MsgUtils.getPropValueStr(userMap,"pkEmp"));
		//获取用户关联人员的医疗项目权限(职称)
		String educationTitle = null;
		try{
			educationTitle = DataBaseHelper.queryForScalar("SELECT  name from  BD_DEFDOC where CODE_DEFDOCLIST='010305' and code =(select dt_empsrvtype from bd_ou_employee where pk_emp=?)",String.class,MsgUtils.getPropValueStr(userMap,"pkEmp"));
		}catch(EmptyResultDataAccessException e){
			educationTitle = "";
		}	
		//判断用户主键是否是空(添加的时候主键为空),如果为空则查询数据库
		
		String pkUser = MsgUtils.getPropValueStr(userMap,"pkUser");
		if(pkUser==null || pkUser.equals("")){
			//查询用户主键和密码
			Map<String,Object> userInfo = DataBaseHelper.queryForMap("select pk_user,pwd from BD_OU_USER where pk_org=? and code_user=?",MsgUtils.getPropValueStr(userMap,"pkOrg"),MsgUtils.getPropValueStr(userMap,"codeUser"));
			userMap.put("pkUser", userInfo.get("pkUser"));
			userMap.put("pwd", userInfo.get("pwd"));
		}
		
		String[] strs = new String[17];
		//数据库用户名
		strs[0]=MsgUtils.getPropValueStr(userMap,"pkUser");
		//用户标识
		strs[1]=MsgUtils.getPropValueStr(userMap,"pkUser");
		//用户姓名
		strs[2]=MsgUtils.getPropValueStr(userMap,"nameUser");
		//用户科室(用户组名称)
		strs[3]=MsgUtils.getPropValueStr(userMap,"codeDept");
		//建立日期
		strs[4]=MsgUtils.getPropValueDateSting(userMap,"createTime");
		//用户密码(加密)
		//strs[5]=MsgUtils.getPropValueStr(userMap,"pwd");
		strs[5]="";
		//输入拼音(表里没有该字段)
		strs[6]=MsgUtils.getPropValueStr(userMap,"spcode");
		//拼音码(表里没有该字段)
		strs[7]=MsgUtils.getPropValueStr(userMap,"spcode");
		//五笔码(表里没有该字段)
		strs[8]="";
		//域用户名(表里没有该字段)
		strs[9]="";
		//密保等级(表里没有该字段)
		strs[10]="";
		//登录名
		strs[11]=MsgUtils.getPropValueStr(userMap,"codeUser");
		//组编号(未确定)
		strs[12]="";
		//职称(表里没有该字段)
		strs[13]=educationTitle;
		//有无资质
		strs[14]="1";
		//数字证书(表里没有该字段)
		strs[15]="";
		//用户类型(未确定)
		strs[16]=MsgUtils.getPropValueStr(userMap,"euUsertype");
		
		ZU0.parse(makeZxxString(strs, zstr));
		}}
 		} catch (Exception e) {
 			// TODO: handle exception
 			loger.error("组装ZU0失败{}",e.getMessage());
 		}
	}
	
	/**
	 * 发送人员字典信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForEmployee_Z02(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "rleCode"));
		key.setValue(MsgUtils.getPropValueStr(map, "codeEmp"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");
		
		mf.addNonstandardSegment("Z02");
		Segment Z02 = (Segment) mf.get("Z02");
		
		String[] strs=new String[18];
		//员工代码
		strs[0]=MsgUtils.getPropValueStr(map,"codeEmp");
		//员工姓名
		strs[1]=MsgUtils.getPropValueStr(map,"nameEmp");
		//员工性别
		strs[2]=MsgUtils.getSex(MsgUtils.getPropValueStr(map,"dtSex"));
		//出生日期
		strs[3]=MsgUtils.getPropValueDateSting(map,"birthday");
		//Map<String,Object> empMap=(Map<String,Object>)map.get("emp");
		
		String dtEmptype =MsgUtils.getSex(MsgUtils.getPropValueStr(map,"dtEmptype"));
		if(dtEmptype != null && dtEmptype.equals("01")){//医疗
			//职务代号(待确定)
			strs[4]="D";
		}else if(dtEmptype.equals("02")){//护理
			strs[4]="N";
		}else if(dtEmptype.equals("03")){//医技
			strs[4]="T";
		}else if(dtEmptype.equals("04")){//药剂
			strs[4]="P";
		}else if(dtEmptype.equals("05")){//财务
			strs[4]="F";
		}else if(dtEmptype.equals("06")){//行政
			strs[4]="X";
		}else if(dtEmptype.equals("07")){//管理
			strs[4]="G";
		}else if(dtEmptype.equals("08")){//其他
			strs[4]="O";
		}else{
			strs[4]=dtEmptype;
		}
		//职级代号(待确定)
		strs[5]="";
		//学历(表里没有该字段)
		strs[6]="";
		//身份证号
		strs[7]=MsgUtils.getPropValueStr(map,"idno");
		//所属科室(科室编码^科室名称)
		strs[8]=MsgUtils.getPropValueStr(map,"deptInfo");
		//所属护理站(待确定)
		strs[9]=MsgUtils.getPropValueStr(map,"CODEDEPTS");
		//人员类型
		strs[10]=MsgUtils.getPropValueStr(map,"dtEmptype");
		//是否专家
		strs[11]=MsgUtils.getPropValueStr(map,"flagSpec");
		//有效性标志
		strs[12]=MsgUtils.getPropValueStr(map,"flagActive");
		//顺序号(表里没有该字段)
		strs[13]="";
		//拼音码
		strs[14]=MsgUtils.getPropValueStr(map,"pyCode");
		//五笔码
		strs[15]=MsgUtils.getPropValueStr(map,"dCode");
		//操作员
		strs[16]=MsgUtils.getPropValueStr(map,"empCode");//当前登录人的编码
		//操作时间
		strs[17]=MsgUtils.PropDateSting(new Date());
		
		Z02.parse(makeZxxString(strs, zstr));

		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Z02失败{}",e.getMessage());
		}
	}
	
	/**
	 * 发送科室与病区关系字典信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key发送用户字典信息:用户密码取消发送 。发送科室与病区关系字典信息(科室与病区关系):主键列传科室编码
	 * @throws HL7Exception
	 */
	private void createMsgForDeptStat_Z03(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "rleCode"));
		key.setValue(MsgUtils.getPropValueStr(map, "codeDept"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");
		
		mf.addNonstandardSegment("Z03");
		Segment Z03 = (Segment) mf.get("Z03");
		
		//获取当前科室的父级编码和父级名称
		//Map<String,Object> fatherInfo = DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = ?", MsgUtils.getPropValueStr(map,"pkFather"));
		String sqlString = "SELECT D.code_dept AS code,D.NAME_DEPT AS name FROM (((bd_dept_bus A INNER JOIN bd_dept_bus b ON A.PK_DEPTBU = b.PK_DEPTBU)" +
		        "INNER JOIN BD_OU_DEPT c ON c.PK_DEPT = A.PK_DEPT) INNER JOIN BD_OU_DEPT D ON D.PK_DEPT = b.PK_DEPT)" +
		        "WHERE A.DT_DEPTTYPE = '02' AND A.DEL_FLAG = '0' AND b.DT_DEPTTYPE = '01' AND b.DEL_FLAG = '0'" +
		        "AND A.PK_ORG = ? AND b.PK_ORG = ? AND A.pk_dept = ?";
				
				String org = ((User)UserContext.getUser()).getPkOrg();
				String PkDept = MsgUtils.getPropValueStr(map,"pkDept");
				Map<String,Object> fatherInfo = DataBaseHelper.queryForMap(sqlString,org ,org,PkDept);
				
		String[] strs=new String[7];
		//主键列
		strs[0]=MsgUtils.getPropValueStr(map, "codeDept");
		//父级编码
		if(fatherInfo != null){
			if(null != fatherInfo.get("code")){
				strs[1]=fatherInfo.get("code").toString();
			}else{
				strs[1]="";
			}
			//父级名称
	        if(null != fatherInfo.get("name")){
	        	strs[2]=fatherInfo.get("name").toString();
			}else{
				strs[2]="";
			}
		}else{
			strs[1]="";
			strs[2]="";
		}
		
		
		//当前科室编码
		strs[3]=MsgUtils.getPropValueStr(map,"codeDept");
		//有效性标志
		strs[4]=MsgUtils.getPropValueStr(map,"flagActive");
		//操作员
		strs[5]=MsgUtils.getPropValueStr(map,"empCode");//@todo
		//操作时间
		strs[6]=MsgUtils.PropDateSting(new Date());
		
		Z03.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Z03失败{}",e.getMessage());
		}
	}
	
	/**
	 * 发送医嘱字典信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForOrderItem_ZA1(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "rleCode"));
		key.setValue(MsgUtils.getPropValueStr(map, "code"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("ST");
		
		mf.addNonstandardSegment("ZA1");
		Segment ZA1 = (Segment) mf.get("ZA1");
		
		//获取执行科室编码
		String bdOrdDeptCode="";
		if(map.containsKey("ordDept")){
			Map<String,Object> dept = (Map<String,Object>)map.get("ordDept");
			bdOrdDeptCode = DataBaseHelper.queryForScalar("select CODE_DEPT from BD_OU_DEPT where PK_DEPT=?",String.class,dept.get("pkDept"));
		}
		//获取执行单分类
		String occType="";
		String pkOrdtype = MsgUtils.getPropValueStr(map,"pkOrdtype");
		if(pkOrdtype!=null && !pkOrdtype.equals("")){
			String name = DataBaseHelper.queryForScalar("select name from bd_ordtype where PK_ORDTYPE = ?",String.class,pkOrdtype);
			if(name.equals("检查"))
				occType = "1";
			else if(name.equals("检验"))
				occType = "2";
			else if(name.equals("护理"))
				occType = "3";
			else if(name.equals("材料"))
				occType = "4";
			else
				occType = "0";
		}
		//获取执行分类
		String occFlag = "";
		String flagNs = MsgUtils.getPropValueStr(map,"flagNs");//护士使用标志
		String flagDr = MsgUtils.getPropValueStr(map,"flagDr");//医生使用标志
		if(flagNs.equals("1")&&flagDr.equals("1"))
			occFlag = "2";
		else if(flagNs.equals("1"))
			occFlag = "0";
		else if(flagDr.equals("1"))
			occFlag = "1";
		
		String[] strs = new String[13];
		//医嘱编码
		strs[0]=MsgUtils.getPropValueStr(map,"code");
		//医嘱名称
		strs[1]=MsgUtils.getPropValueStr(map,"name");
		//拼音码
		strs[2]=MsgUtils.getPropValueStr(map,"spcode");
		//自定义码
		strs[3]=MsgUtils.getPropValueStr(map,"dCode");
		//排斥分组码
		strs[4]=MsgUtils.getPropValueStr(map,"euExclude");
		//医嘱分类(分类编码)
		strs[5]=map.get("codeOrdtype").toString().substring(0,2);
		//删除标记
		strs[6]=MsgUtils.getPropValueStr(map,"flagDel");
		//描述
		strs[7]=MsgUtils.getPropValueStr(map,"note");
		//执行科室(科室编码)
		strs[8]=bdOrdDeptCode;
		//执行标记(执行计费)
		strs[9]="2";
		//执行单分类
		strs[10]=occType;
		//执行分类
		strs[11]=occFlag;
		//打印名称
		strs[12]=MsgUtils.getPropValueStr(map,"namePrt");
		
		ZA1.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装ZA1失败{}",e.getMessage());
		}
	}
	
	/**
	 * 发送医嘱用法信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForYzSupply_ZA2(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		if(null != map.get("freq")){
		   map.putAll(MapUtils.objectToMap(map.get("freq")));
		}
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "rleCode"));
		key.setValue(MsgUtils.getPropValueStr(map, "code"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("ST");
		
		mf.addNonstandardSegment("ZA2");
		Segment ZA2 = (Segment) mf.get("ZA2");
		
		//获取分类
		String pkSupplycate = MsgUtils.getPropValueStr(map,"pkSupplycate");//获取医嘱分类主键
		String classCode="";
		if(pkSupplycate!=null && !pkSupplycate.equals("")){
			String name = DataBaseHelper.queryForScalar("select name from bd_supply_class where PK_SUPPLYCATE = ?",String.class,pkSupplycate);
			if(name.equals("口服")||name.equals("口服类"))
				classCode="1";
			else if(name.equals("输液")||name.equals("输液类"))
				classCode="2";
			else if(name.equals("注射")||name.equals("注射类"))
				classCode="3";
			else if(name.equals("出院带药")||name.equals("出院带药类"))
				classCode="4";
			else if(name.equals("领药(临时出现)")||name.equals("领药类(临时出现)"))
				classCode="5";
			else if(name.equals("退药")||name.equals("退药类"))
				classCode="6";
			else if(name.equals("领药(不出现)")||name.equals("领药类(不出现)"))
				classCode="7";
			else if(name.equals("其他")||name.equals("其他类"))
				classCode="9";
			else if(name.equals("草药")||name.equals("草药类"))
				classCode="a";
		}
		
		String[] strs = new String[8];
		//编码
		strs[0]=MsgUtils.getPropValueStr(map,"code");
		//名称
		strs[1]=MsgUtils.getPropValueStr(map,"name");
		//拼音码
		strs[2]=MsgUtils.getPropValueStr(map,"spcode");
		//自定义码
		strs[3]=MsgUtils.getPropValueStr(map,"dCode");
		//分类
		strs[4]=classCode;
		//排序码(表内没有该字段)
		strs[5]="";
		//打印名称
		strs[6]=MsgUtils.getPropValueStr(map,"namePrint");
		//删除标记
		strs[7]=MsgUtils.getPropValueStr(map,"delFlag");
		
		ZA2.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装ZA2失败"+e);
		}
	}
	
	/**
	 * 发送医嘱频率信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForYzFrequency_ZA3(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "rleCode"));
		key.setValue(MsgUtils.getPropValueStr(map, "code"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("ST");
		
		mf.addNonstandardSegment("ZA3");
		Segment ZA3 = (Segment) mf.get("ZA3");
		
		//获取频次时间点
		String freqTimes = "";
		List<Map<String,Object>>  timeList = (List<Map<String,Object>>)map.get("timeList");
		if(timeList!=null && timeList.size()>0){
			for(int p=0;p<timeList.size();p++){
				String tempTime = timeList.get(p).get("timeOcc").toString();
				String timeOcc=tempTime==null?"":tempTime;
				if(p==0)
					freqTimes = timeOcc;
				else
					freqTimes+="-"+timeOcc;
			}
		}
		
		String[] strs = new String[10];
		//编码
		strs[0]=MsgUtils.getPropValueStr(map,"code");
		//名称
		strs[1]=MsgUtils.getPropValueStr(map, "name");
		//拼音码
		strs[2]=MsgUtils.getPropValueStr(map,"spcode");
		//自定义码
		strs[3]=MsgUtils.getPropValueStr(map,"dCode");
		//打印名称
		strs[4]=MsgUtils.getPropValueStr(map,"namePrint");
		//周标志
		strs[5]=MsgUtils.getPropValueStr(map,"euCycle");
		//删除标记
		strs[6]=MsgUtils.getPropValueStr(map,"delFlag");
		//排序码(表内没有该字段)
		strs[7]="";
		//医生使用标志(表内没有该字段)
		strs[8]="";
		//频次时间点
		strs[9]=freqTimes;
		
		ZA3.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装ZA3失败"+e);
		}
	}
	/**
	 * 发送ICD10信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForICD10_Z10(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		
		if(null != map.get("diag")){
		     map.putAll(MapUtils.objectToMap(map.get("diag")));
		}
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "rleCode"));
		key.setValue(MsgUtils.getPropValueStr(map, "diagcode"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");
		
		mf.addNonstandardSegment("Z10");
		Segment Z10 = (Segment) mf.get("Z10");
		String[] strs = new String[12]; 
		//主键
		strs[0]=MsgUtils.getPropValueStr(map,"pkDiag");
		//icd10主诊断编码
		strs[1]=MsgUtils.getPropValueStr(map,"diagcode");
		//中文疾病名称
		strs[2]=MsgUtils.getPropValueStr(map,"diagname");
		//30种疾病标志(未确定)
		strs[3]="";
		//传染病标志(未确定)
		strs[4]="";
		//肿瘤标志(未确定)
		strs[5]="";
		//有效性标志(未确定)
		strs[6]="";
		//序号(未确定)
		strs[7]="";
		//操作员
		strs[8]=MsgUtils.getPropValueStr(map,"empCode");
		//操作时间
		strs[9]=MsgUtils.PropDateSting(new Date());
		//拼音码
		strs[10]=MsgUtils.getPropValueStr(map,"pyCode");
		//五笔码
		strs[11]=MsgUtils.getPropValueStr(map,"dCode");
		Z10.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Z10失败{}",e.getMessage());
		}
	}
	
	/**
	 * 发送手术编码信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForOperation_ZD1(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		
		mfe.getRecordLevelEventCode().setValue(MsgUtils.getPropValueStr(map, "rleCode"));
		key.setValue(MsgUtils.getPropValueStr(map, "diagcode"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");
		
		mf.addNonstandardSegment("ZD1");
		Segment ZD1 = (Segment) mf.get("ZD1");
		
		//是否自定义
		String dtDiagcate = MsgUtils.getPropValueStr(map,"dtDiagcate");//获取自定义分类
		String IsZdy = dtDiagcate!=null&&!dtDiagcate.equals("")?"1":"0";
		
		String[] strs = new String[10];
		//手术操作代码
		strs[0]=MsgUtils.getPropValueStr(map,"diagcode");
		//手术操作名词
		strs[1]=MsgUtils.getPropValueStr(map,"diagname");
		//手术等级
		strs[2]=MsgUtils.getPropValueStr(map,"levelOp");
		//正名标志(未确定)
		strs[3]="";
		//标准化标志(未确定)
		strs[4]="";
		//创建日期(表内没有该字段)
		strs[5]="";
		//拼音码
		strs[6]=MsgUtils.getPropValueStr(map,"pyCode");
		//五笔码
		strs[7]=MsgUtils.getPropValueStr(map,"dCode");
		//是否停止(未确定)
		strs[8]="";
		//是否自定义
		strs[9]=IsZdy;
		
		ZD1.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装ZD1失败{}",e.getMessage());
		}
	}
	
	/**
	 * 发送公共字典信息  (批量保存)
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForBdDefdocType_Z99(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		
		key.setValue(MsgUtils.getPropValueStr(map, "code"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");
		
		mf.addNonstandardSegment("Z99");
		switch (map.get("state").toString()) {
		case AddState:
			mfe.getRecordLevelEventCode().setValue("MAD");
			setMfSegmentFromBdDefdoc(map, mf);

			break;
		case UpdateState:
			mfe.getRecordLevelEventCode().setValue("MUP");
			setMfSegmentFromBdDefdoc(map, mf);

			break;
		case DelState:
			mfe.getRecordLevelEventCode().setValue("MDL");
			setMfSegmentFromBdDefdoc(map, mf);

			break;
		default:
			break;
		}
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("Z99组装MFE失败{}",e.getMessage());
		}
	}
	
	/**
	 * 发送床位字典信息  (批量保存)
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 * @throws HL7Exception
	 */
	private void createMsgForBed_Z07(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
		//组装mfi信息
		createMfiMessage(dictCode, null, mfi);
		
		key.setValue(MsgUtils.getPropValueStr(map, "code"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");
		
		mf.addNonstandardSegment("Z07");
		switch (map.get("state").toString()) {
		case AddState:
			mfe.getRecordLevelEventCode().setValue("MAD");
			setMfSegmentFromBed(map, mf);

			break;
		case UpdateState:
			mfe.getRecordLevelEventCode().setValue("MUP");
			setMfSegmentFromBed(map, mf);

			break;
		case DelState:
			mfe.getRecordLevelEventCode().setValue("MDL");
			setMfSegmentFromBed(map, mf);

			break;
		default:
			break;
		}
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("Z07组装MFE失败{}",e.getMessage());
		}
	}
	
	public void setMfSegmentFromBdDefdoc(Map<String,Object> map, MFN_M01_MF mf){
		try{
		Segment Z99 = (Segment) mf.get("Z99");
		String[] strs = new String[11];
		//类型
		strs[0]=MsgUtils.getPropValueStr(map,"codeDefdoclist");
		//编码
		strs[1]=MsgUtils.getPropValueStr(map,"code");
		//名称
		strs[2]=MsgUtils.getPropValueStr(map,"name");
		//用户自定义助记码(未确定)
		strs[3]="";
		//顺序号(表内没有该字段)
		strs[4]="";
		//有效应标志(未确定)
		strs[5]=MsgUtils.getPropValueStr(map,"delFlag");
		//备注
		strs[6]=MsgUtils.getPropValueStr(map,"memo");
		//拼音码
		strs[7]=MsgUtils.getPropValueStr(map,"pyCode");
		//五笔码
		strs[8]=MsgUtils.getPropValueStr(map,"dCode");
		//操作员
		strs[9]=MsgUtils.getPropValueStr(map,"empCode");
		//操作时间
		strs[10]=MsgUtils.PropDateSting(new Date());
		
		Z99.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Z99失败{}",e.getMessage());
		}
	}
	
	public void setMfSegmentFromBed(Map<String,Object> map, MFN_M01_MF mf){
		try{
		Segment Z07 = (Segment) mf.get("Z07");
		String[] strs = new String[17];
		//床位编码
		strs[0] = MsgUtils.getPropValueStr(map,"code");
		//护士站代码（所属病区编码）
		strs[1] = getDeptCode(MsgUtils.getPropValueStr(map,"pkWard"));
		//床位等级编码(未确定)
		strs[2] = "";
		//床位编制(未确定)
		strs[3] = MsgUtils.getPropValueStr(map, "dtBedtype");
		//床位状态
		strs[4] = MsgUtils.getPropValueStr(map,"euStatus");
		//病室号
		strs[5] = MsgUtils.getPropValueStr(map,"houseno");
		//医师代码(主管医生编码)
		strs[6] = MsgUtils.getPropValueStr(map,"pkPhyEmp");
		//床位电话(未确定)于病历协商穿床位名称
		strs[7] = MsgUtils.getPropValueStr(map,"name");
		//归属
		strs[8] = "";
		//医疗流水号(住院号)
		if(qryOpOrIpCodeBypkPi(MsgUtils.getPropValueStr(map,"pkPi"))!=null)
		strs[9] =qryOpOrIpCodeBypkPi(MsgUtils.getPropValueStr(map,"pkPi")).get("codeIp").toString();
		//出院日期
		strs[10] = "";
		//有效性标志
		strs[11] = MsgUtils.getPropValueStr(map,"flagActive");
		String yuYue="";
		if("04".equals(MsgUtils.getPropValueStr(map, "euStatus")))
			yuYue="1";
		else{
			yuYue="0";
		}
		//预约标志(未确定)
		strs[12] = yuYue;
		//顺序号(未确定)
		strs[13] = MsgUtils.getPropValueStr(map,"sortno");
		//护理组(未确定)
		strs[14] =MsgUtils.getPropValueStr(map, "namePlace");
		//操作员
		strs[15] = MsgUtils.getPropValueStr(map,"empCode");
		//操作日期
		strs[16] = MsgUtils.PropDateSting(new Date());
		
		Z07.parse(makeZxxString(strs, zstr));

		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装Z07失败{}",e.getMessage());
		}
	}
	
	/**
	 * 获取所属病区编码
	 * @param pkWard
	 * @return
	 */
	private String getDeptCode(String pkWard){
		String code = "";
		if(pkWard!=null && !pkWard.equals("")){
			code = DataBaseHelper.queryForScalar("select code_dept from bd_ou_dept where PK_DEPT = ?", String.class, pkWard);
		}
		return code;
	}
	
	/**
	 * 根据患者编码获取患者所对应的住院责任医生编码
	 * @param pkPi
	 * @return
	 */
	private String getEmpPhyCode(String pkPi){
		String empCode = "";
		if(pkPi!=null && !pkPi.equals("")){
			empCode  = DataBaseHelper.queryForScalar("select code_emp from BD_OU_EMPLOYEE where pk_emp = (select pk_emp_phy from pv_encounter where PK_PI = ?)", String.class, pkPi);
		}
		return empCode;
	}
	
	/**
	 * 根据患者主键获取住院号
	 * @param pkPi
	 * @return
	 */
	private String getClinicNo(String pkPi){
		String codeIp = "";
		if(pkPi!=null && !pkPi.equals("")){
			codeIp = DataBaseHelper.queryForScalar("", String.class, pkPi);
		}
		return codeIp;
	}
	
	/**
	 * 根据患者主键获取出院时间
	 * @param pkPi
	 * @return
	 */
	private String getPrepayOutDat(String pkPi){
		String dateEnd = "";
		if(pkPi!=null && !pkPi.equals("")){
			Date date = DataBaseHelper.queryForScalar("select date_end from pv_encounter where pk_pi = ?", Date.class, pkPi);
			if(date!=null)
				dateEnd = MsgUtils.PropDateSting(date);
		}
		return dateEnd;
	}
	
	
	
	/**
	 * Z06-15 sys_class | Z06-17 drug_type 获取系统类别 | 药品类别
	 * @param map
	 * @return
	 */
	private String getDrugSysClass(Map<String, Object> map){
		if(map==null)return null;
		String durgType = MsgUtils.getPropValueStr(map, "euDrugtype");
		if("0".equals(durgType))
			return "P";
		else if("1".equals(durgType))
			return "PCC";
		else if("2".equals(durgType))
			return "PCZ";
		else return "";
	}
	
	/**
	 * Z06-16 fee_code 最小费用 => 账单码
	 * @param map
	 * @return
	 */
	private String getDrugFeeCode(Map<String, Object> map){
		if(map==null)return null;
		String feeCode="";
		StringBuilder sql=new StringBuilder("select  inv_item.code from bd_invcate_item inv_item ");
	    sql.append("inner join bd_invcate_itemcate inv_itemcate on inv_item.pk_invcateitem = inv_itemcate.pk_invcateitem ");
	    sql.append("inner join bd_pd pd on inv_itemcate.pk_itemcate = pd.pk_itemcate and inv_itemcate.del_flag = '0' ");
	    sql.append("where inv_item.del_flag = '0' and pd.pk_pd = ? order by inv_item.code ");
	    
		List<String> codeList=DataBaseHelper.queryForList(sql.toString(),String.class, map.get("pkPd").toString());
		if(codeList!=null&& codeList.size()>0)
			feeCode=codeList.get(0);
		return feeCode;
	}
	
	/**
	 * Z06-18 drug_quality 获取药品性质
	 * E1-原料,E2-试剂,E3-其它,E4-医院制剂,EF-放射性药品
	 * O-普药,P1-精一,P2-精二,S1-麻,S2-毒,T-大输液,V-贵重药
	 * @param map
	 * @return
	 */
	private String getDrugQuality(Map<String, Object> map){
		if(map==null)return null;
		String dtPois = MsgUtils.getPropValueStr(map, "dtPois");
		
		if("1".equals(MsgUtils.getPropValueStr(map, "flagRm")))
			return "E1";//原料
		else if("1".equals(MsgUtils.getPropValueStr(map, "flagReag")))
			return "E2";//试剂
		else if("1".equals(MsgUtils.getPropValueStr(map, "flagPrecious")))
			return "V";//贵重药
		else if("00".equals(dtPois))
			return "O";//普药
		else if("02".equals(dtPois))
			return "P1";//精一
		else if("03".equals(dtPois))
			return "P2";//精二
		else if("01".equals(dtPois))
			return "S1";//麻
		else if("04".equals(dtPois))
			return "S2";//毒
		else return "";
	}

	/**
	 * Z06-35 valid_state	有效性标志 
	 * @param map
	 * @return
	 */
	private String getDrugValidSate(Map<String, Object> map){
		if(map==null)return null;
		String flagStop =  MsgUtils.getPropValueStr(map, "flagStop");
		if("0".equals(flagStop))
			return "1";
		else if("1".equals(flagStop))
			return "0";
		else 
			return "";
	}
	
	/**
	 * Z06-36 self_flag	自制标志 :0-非自产，1-自产
	 * dtMade:0 购入, 1 自制, 2 原研
	 * @param map
	 * @return
	 */
	private String getDrugSelfFlag(Map<String, Object> map){
		if(map==null)return null;
		String flagStop =  MsgUtils.getPropValueStr(map, "dtMade");
		if("1".equals(flagStop) || "2".equals(flagStop))
			return "1";
		else if("0".equals(flagStop))
			return "0";
		else 
			return "";
	}
	/**
	 * 根据pkPi获取住院号或者流水号
	 * @param pkPi
	 * @return
	 */
	private Map<String,Object> qryOpOrIpCodeBypkPi(String pkPi){
		if(pkPi.length()<=0)return null;
		String sql="select code_ip,code_op from pi_master where pk_pi=?";
		Map<String,Object> resMap=DataBaseHelper.queryForMap(sql, pkPi);
		return resMap;
	}
	
	private String qryAndSetClassCode(Map<String,Object> map){
		if(map==null)return null;
		String sql="select code from bd_itemcate where del_flag='0' and pk_itemcate=?";
		Map<String,Object> resMap=DataBaseHelper.queryForMap(sql, new Object[]{map.get("pkItemcate")});
		String result="";
		if(resMap!=null && resMap.get("code")!=null){
			if("19".equals(resMap.get("code"))){
				result="JC";
			}else if("06".equals(resMap.get("code"))){
				result="JY";
			}else if("15".equals(resMap.get("code"))){
				result="CL";
			}
		}
		return result;
	} 
}
