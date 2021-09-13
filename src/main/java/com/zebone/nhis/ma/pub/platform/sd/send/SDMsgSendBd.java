package com.zebone.nhis.ma.pub.platform.sd.send;

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
import ca.uhn.hl7v2.util.StringUtil;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Adt消息处理(灵璧复制版本)
 *
 * @author chengjia
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDMsgSendBd {

	@Resource
	private SDHl7MsgHander sDHl7MsgHander;
	@Autowired
	private SDMsgMapper sDMsgMapper;
	@Resource
	private SDQueryUtils sDQueryUtils;
	//拼接自定义消息段（Z段）使用
	private String zstr;
	//添加状态
	public static final String AddState = "_ADD";
	//更新状态
	public static final String UpdateState = "_UPDATE";
	//删除状态
	public static final String DelState = "_DELETE";

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	private static Logger loger = LoggerFactory.getLogger("nhis.Hl7Log");

	/**
	 * 发送基础字典 Hl7消息
	 * @param dictCode
	 * @param listMap
	 * @throws HL7Exception
	 */
	public void sendMfnMsg(String dictCode, List<Map<String, Object>> listMap) throws HL7Exception {
		try {
			String msgId = SDMsgUtils.getMsgId();
			Message message = createMfnMsg(msgId, dictCode, listMap);

			String msg = SDMsgUtils.getParser().encode(message);
			// 发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			loger.info(dictCode+"字典"+e);
			e.printStackTrace();
			throw new HL7Exception("发送消息失败！");
		}
	}

	/**
	 * 创建基础字典 Mfn消息
	 *
	 * @param dictCode
	 * @param listMap
	 * @throws HL7Exception
	 */
	private Message createMfnMsg(String msgId, String dictCode,List<Map<String, Object>> listMap){
		if (listMap==null || listMap.size()<=0) {
			return null;
		}
		// 字典管理
		MFN_M01 mfn = new MFN_M01();
		// MSH
		MSH msh = mfn.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "MFN", "M01");
		MFI mfi = mfn.getMFI();
		//mfi.getMasterFileIdentifier().getIdentifier().setValue(dictCode);
		int i, len;
		len = listMap.size();
		for (i = 0; i < len; i++) {
			Map<String, Object> map = listMap.get(i);
			MFN_M01_MF mf = mfn.getMF(i);
			ST key = new ST(mfn);
			MFE mfe = mf.getMFE();
			if ("Department".equals(dictCode)) {
				//科室字典信息
				createMsgForDepartment_Z01(dictCode, map, mfi, mf, mfe, key);
			} else if ("Undrug".equals(dictCode)) {
				// 非药品收费项目分支 Z04
				createMsgForUnDrug(dictCode, map, mfn, mfi, i);
			} else if ("UndrugGroup".equals(dictCode)) {
				// 非药品收费组套分支
				createMsgForUndrugGroup(dictCode, map, mfn, mfi, i);
			}else if ("Drug".equals(dictCode)) {
				//药品字典
				createMsgForDrug_Z06(dictCode, map, mfi, mf, mfe,key);
			} else if("UserInfo".equals(dictCode)){
				//用户字典信息
				createMsgForUserInfo_ZU0(dictCode, map, mfi, mf, mfe, key);
			} else if("Employee".equals(dictCode)){
				//人员字典信息
				createMsgForEmployee_Z02(dictCode, map, mfi, mf, mfe, key);
			} else if("DeptStat".equals(dictCode)){
				//科室与病区关系字典
				createMsgForDeptStat_Z03(dictCode, map, mfi, mf, mfe, key);
			} else if("yz_order_item".equals(dictCode)){
				//医嘱字典
				createMsgForOrderItem_ZA1(dictCode, map, mfi, mf, mfe, key);
			} else if("yz_supply".equals(dictCode)){
				//医嘱用法
				createMsgForYzSupply_ZA2(dictCode, map, mfi, mf, mfe, key);
			} else if("yz_frequency".equals(dictCode)){
				//医嘱频率
				createMsgForYzFrequency_ZA3(dictCode, map, mfi, mf, mfe, key);
			} else if("ICD10".equals(dictCode)){
				//ICD10信息字典
				createMsgForICD10_Z10(dictCode, map, mfi, mf, mfe, key);
			} else if("Operation".equals(dictCode)){
				//手术编码信息
				createMsgForOperation_ZD1(dictCode, map, mfi, mf, mfe, key);
			} else if("BdDefdocType".equals(dictCode)){
				//公共字典信息
				createMsgForBdDefdocType_Z99(dictCode, map, mfi, mf, mfe, key);
			} else if("Bed".equals(dictCode)){
				//床位字典
				createMsgForBed_Z07(dictCode, map, mfi, mf, mfe, key);
			} else if("Pact".equals(dictCode)){
				//合同信息（医保计划）
				createMsgForPact_Z08(dictCode, map, mfi, mf, mfe, key);
			} else if("RegLevel".equals(dictCode)){
				//挂号级别信息
				createMsgForRegLevel_Z12(dictCode, map, mfi, mf, mfe, key);
			}
		}
		return mfn;
	}

	/**
	 * 合同单位（医保计划）Z08
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 */
	private void createMsgForPact_Z08(String dictCode,Map<String, Object> map, MFI mfi, MFN_M01_MF mf, MFE mfe, ST key) {
		try {
			//组装mfi信息
			createMfiMessage(dictCode, null, mfi);
			key.setValue(SDMsgUtils.getPropValueStr(map, "code"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("CE");
			mf.addNonstandardSegment("Z08");
			switch (map.get("state").toString()) {
				case AddState:
					mfe.getRecordLevelEventCode().setValue("MAD");
					setMfSegmentFromPact(map, mf);

					break;
				case UpdateState:
					mfe.getRecordLevelEventCode().setValue("MUP");
					setMfSegmentFromPact(map, mf);

					break;
				case DelState:
					mfe.getRecordLevelEventCode().setValue("MDL");
					setMfSegmentFromPact(map, mf);

					break;
				default:
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 合同单位（医保计划）Z08
	 * @param map
	 * @param mf
	 */
	private void setMfSegmentFromPact(Map<String, Object> map, MFN_M01_MF mf) {
		try {
			Segment Z08 = (Segment) mf.get("Z08");
			String[] strs = new String[6];
			//合同单位编码
			strs[0] = SDMsgUtils.getPropValueStr(map,"code");
			//合同单位名称 name
			strs[1] = SDMsgUtils.getPropValueStr(map,"name");
			//类别
			strs[2] = "";
			//顺序号
			strs[3] = "";
			//操作员
			strs[4] = SDMsgUtils.getPropValueStr(map,"empCode");
			//操作时间
			strs[5] = sdf.format(new Date());
			Z08.parse(makeZxxString(strs, zstr));
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 挂号级别
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key
	 */
	private void createMsgForRegLevel_Z12(String dictCode,Map<String, Object> map, MFI mfi, MFN_M01_MF mf, MFE mfe, ST key) {
		try {
			//组装mfi信息
			createMfiMessage(dictCode, null, mfi);
			key.setValue(SDMsgUtils.getPropValueStr(map, "code"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("CE");
			mf.addNonstandardSegment("Z12");
			switch (map.get("state").toString()) {
				case AddState:
					mfe.getRecordLevelEventCode().setValue("MAD");
					setMfSegmentFromRegLevel(map, mf);

					break;
				case UpdateState:
					mfe.getRecordLevelEventCode().setValue("MUP");
					setMfSegmentFromRegLevel(map, mf);

					break;
				case DelState:
					mfe.getRecordLevelEventCode().setValue("MDL");
					setMfSegmentFromRegLevel(map, mf);

					break;
				default:
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 挂号级别Z段信息（z12）
	 * @param map
	 * @param mf
	 */
	private void setMfSegmentFromRegLevel(Map<String, Object> map, MFN_M01_MF mf) {
		try {
			Segment Z12 = (Segment) mf.get("Z12");
			System.out.println(JsonUtil.writeValueAsString(map));
			String[] strs = new String[15];
			//挂号级别代码
			strs[0] = SDMsgUtils.getPropValueStr(map,"code");
			//挂号级别名称
			strs[1] = SDMsgUtils.getPropValueStr(map,"name");
			//显示顺序
			strs[2] = "";
			//是否有效 DEL_FLAG
			if(SDMsgUtils.getPropValueStr(map,"DEL_FLAG").equals("1")){
				strs[3] = "0";
			}else {
				strs[3] = "1";
			}
			//默认0 （0为不是，1为是）
			//是否专家号
			strs[4] = "0";
			//是否专科号
			strs[5] = "0";
			//是否特诊号
			strs[6] = "0";
			//是否默认项
			strs[7] = "0";
			//是否急诊
			strs[8] = "0";
			//0 普通；1 专家；2 特诊；9 急诊
			String euSrvtype = SDMsgUtils.getPropValueStr(map,"euSrvtype");
			if(euSrvtype.equals("0")){
				strs[7] = "1";
			}else if("1".equals(euSrvtype)){
				//是否专家号
				strs[4] = "1";
			}else if("2".equals(euSrvtype)){
				//是否特诊号
				strs[6] = "1";
			}else if("9".equals(euSrvtype)){
				//是否急诊
				strs[8] = "1";
			}
			//操作员
			strs[9] = SDMsgUtils.getPropValueStr(map,"empCode");
			//操作时间
			strs[10] = sdf.format(new Date());
			//挂号费
			strs[11] = "";
			//诊查费 (传值)
			strs[12] = "";
			//医生列表
			strs[13] = "";
			//医保费用（传值）
			strs[14] = "";
			String sqlPrice = "select item.code,item.name,item.price,case when cgdiv.amount is null then item.price else cgdiv.amount end price_hp " +
					" from sch_srv_ord srvord inner join bd_ord ord on ord.pk_ord =srvord.pk_ord inner join bd_item item on item.code=ord.code " +
					" inner join bd_itemcate cate on cate.pk_itemcate=item.pk_itemcate left join bd_hp_cgdiv_item cgdiv on cgdiv.pk_item=item.pk_item " +
					" where cate.code='0904' and srvord.pk_schsrv=? and srvord.eu_type='0'";
			List<Map<String,Object>> resList=DataBaseHelper.queryForList(sqlPrice, SDMsgUtils.getPropValueStr(map, "pkSchsrv"));
			if(resList!=null && resList.get(0)!=null){
				strs[12] = SDMsgUtils.getPropValueStr(resList.get(0), "price");
				strs[14] = SDMsgUtils.getPropValueStr(resList.get(0), "priceHp");
			}
			Z12.parse(makeZxxString(strs, zstr));
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			loger.info("MSH失败"+e);
		}
	}

	/**
	 * @param dictCode  用于主文件标识符
	 * @param mfn 当前使用的mfn
	 * @param mfi 当前使用的mfi
	 */
	private void createMfiMessage(String dictCode, MFN_M01 mfn, MFI mfi){
		// Master file identification
		// Master File Identifier 主文件标识符,传入表名
		try {

			if (dictCode.equals("Undrug")) {
				mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
				mfi.getMfi1_MasterFileIdentifier().getText().setValue("非药品物价信息");
				mfi.getMfi1_MasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");

			} else if(dictCode.equals("UndrugGroup")) {
				mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
				mfi.getMfi1_MasterFileIdentifier().getText().setValue("非药品套餐信息");
				mfi.getMfi1_MasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");

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
				//mfi.getMasterFileIdentifier().getText().setValue("人员信息");
				mfi.getMasterFileIdentifier().getText().setValue("人员");
				mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");

			} else if(dictCode.equals("DeptStat")){

				mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
				mfi.getMasterFileIdentifier().getText().setValue("科室病区关系");
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

			} else if(dictCode.equals("Pact")){
				mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
				mfi.getMasterFileIdentifier().getText().setValue("合同单位信息");
				mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");

			} else if(dictCode.equals("RegLevel")){
				mfi.getMfi1_MasterFileIdentifier().getIdentifier().setValue(dictCode);
				mfi.getMasterFileIdentifier().getText().setValue("挂号级别信息");
				mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");
			}

			// File-Level Event Code
			mfi.getMfi3_FileLevelEventCode().setValue("UPD");
			// Entered Date/Time
			mfi.getMfi4_EnteredDateTime().getTimeOfAnEvent().setValue(DateUtils.getDateTimeStr(new Date()));
			// Effective Date/Time
			mfi.getMfi5_EffectiveDateTime().getTimeOfAnEvent().setValue(DateUtils.getDateTimeStr(new Date()));
			// Response Level Code
			mfi.getMfi6_ResponseLevelCode().setValue("AL");

		} catch (Exception e) {
			// TODO: handle exception
			loger.info(dictCode+"字典MFI"+e);
		}
	}

	/**
	 * 发送非药品组套收费信息,注意:组套信息可能包含多个
	 * @param dictCode
	 * @param map
	 * @param mfn
	 * @param mfi
	 * @param i
	 */
	private void createMsgForUndrugGroup(String dictCode,Map<String, Object> map, MFN_M01 mfn, MFI mfi, int i){

		//完善MSH
		completeMshMessage(mfn);
		// 完成Mfi
		createMfiMessage(dictCode, mfn, mfi);

		// MF
		MFN_M01_MF mf = null;

		switch (SDMsgUtils.getPropValueStr(map, "state")) {
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
	private MFN_M01_MF createMfMessage(Map<String, Object> map, MFN_M01 mfn, int i, String state, String segment){

		// MF = MFE + Zxx
		MFN_M01_MF mf = mfn.getMF(i);
		try{
			// MFE
			MFE mfe = mf.getMFE();

			// Record-Level Event Code
			mfe.getMfe1_RecordLevelEventCode().setValue(state);
			// Primary Key Value
			ST key = new ST(mfn);

			key.setValue(SDMsgUtils.getPropValueStr(map, "code"));
			//因为bd_item_set没有code字段,所以使用此方法,后期优化
			if(segment.equals("Z05")) {
				key.setValue(transItemCodeFromPk(SDMsgUtils.getPropValueStr(map, "pkItemChild")));
			}

			mfe.getMfe4_PrimaryKeyValueMFE(0).setData(key);
			// Primary Key Value Type
			mfe.getMfe5_PrimaryKeyValueType(0).setValue("CE");

			// Zxx
			mf.addNonstandardSegment(segment);

		} catch (Exception e) {
			// TODO: handle exception
			loger.info(segment+"组装Mf失败"+e);
		}
		return mf;
	}

	/**
	 * 发送非药品收费信息,注意:组套信息可能包含多个
	 * @param dictCode
	 * @param map
	 * @param mfn
	 * @param mfi
	 * @param i
	 */
	private void createMsgForUnDrug(String dictCode, Map<String, Object> map,MFN_M01 mfn, MFI mfi, int i){
		//完善MSH
		completeMshMessage(mfn);
		// 完成MFI
		createMfiMessage(dictCode, mfn, mfi);

		MFN_M01_MF mf;

		switch (SDMsgUtils.getPropValueStr(map, "state")) {
			case AddState:
				mf = createMfMessage(map, mfn, i, "MAD", "Z04");
				if(map.containsKey("group")){
					createMsgForUndrug_Z04Group(map, mf);
				}else{
					createMsgForUndrug_Z04(map, mf);
				}


				break;
			case UpdateState:
				mf = createMfMessage(map, mfn, i, "MUP", "Z04");
				if(map.containsKey("group")){
					createMsgForUndrug_Z04Group(map, mf);
				}else{
					createMsgForUndrug_Z04(map, mf);
				}

				break;
			case DelState:
				mf = createMfMessage(map, mfn, i, "MDL", "Z04");
				if(map.containsKey("group")){
					createMsgForUndrug_Z04Group(map, mf);
				}else{
					createMsgForUndrug_Z04(map, mf);
				}

				break;
			default:
				break;
		}
	}
	/**
	 * 收费项目信息
	 * @param map
	 * @param mf
	 */
	private void createMsgForUndrug_Z04(Map<String, Object> map, MFN_M01_MF mf){
		try{
			String codeOrdtype = SDMsgUtils.getPropValueStr(map, "codeOrdtype");
			Segment Z04 = (Segment) mf.get("Z04");
			String[] strs = new String[28];
			// 从map中通过key取对应的数据
			// 非药品编码
			strs[0] = SDMsgUtils.getPropValueStr(map, "code");
			// 用户自定义（助记）码
			strs[1] = SDMsgUtils.getPropValueStr(map, "spcode");
			// 非药品名称
			strs[2] = SDMsgUtils.getPropValueStr(map, "name");
			// 系统类别
			if(codeOrdtype.startsWith("02")){
				strs[3] = "UC";
			}else if(codeOrdtype.startsWith("03")){
				strs[3] = "UL";
			}
			// 最小费用代码
			strs[4] ="";
			// 国家编码
			strs[5] = SDMsgUtils.getPropValueStr(map, "");
			// 国际标准代码
			strs[6] = SDMsgUtils.getPropValueStr(map, "");
			// 三甲价
			strs[7] = SDMsgUtils.getPropValueStr(map, "price");
			// 单位
			strs[8] = transUnitNameFromPk(SDMsgUtils.getPropValueStr(map, "pkUnit"));
			// 特定治疗项目 0假 1真
			strs[9] = SDMsgUtils.getPropValueStr(map, "");
			// 计划生育标记
			strs[10] = SDMsgUtils.getPropValueStr(map, "");
			// 确认标志
			strs[11] = SDMsgUtils.getPropValueStr(map, "flagActive");
			// 有效性标识,因为目前标准不一致,所以进行转换
			strs[12] = transFlagActive(SDMsgUtils.getPropValueStr(map, "delFlag"));
			// 规格
			strs[13] = SDMsgUtils.getPropValueStr(map, "spec");
			// 执行科室
			strs[14] = SDMsgUtils.getPropValueStr(map, "codeDept");
			// 单位标识 （是否医嘱项目1 是，0不是）
			//strs[15] = SDMsgUtils.getPropValueStr(map, "flagSet");
			//strs[15] = map.containsKey("group")?"1":"0";
			strs[15] = "0";
			// 备注
			strs[16] = SDMsgUtils.getPropValueStr(map, "note");
			// 适用范围   默认 全部0  门诊 1 住院2
			strs[17] = "2";
			if("1".equals(SDMsgUtils.getPropValueStr(map, "flagOp"))){
				strs[17] = "1";
				if("1".equals(SDMsgUtils.getPropValueStr(map, "flagIp"))){
					strs[17] = "0";
				}
			}
			// 拼音码
			strs[18] = SDMsgUtils.getPropValueStr(map, "spcode");
			// 五笔码
			strs[19] = SDMsgUtils.getPropValueStr(map, "dCode");
			// 操作员
			//strs[20] = SDMsgUtils.getPropValueStr(map, "deptCode");
			strs[20] = UserContext.getUser().getCodeEmp();
			// 操作日期
			//strs[21] = SDMsgUtils.getPropValueStr(map, "modityTime");
			strs[21] = sdf.format(new Date());
			// 申请单类型
			strs[22] = codeOrdtype;
			// 样本类型 dt_ordcate (医嘱类型)
			strs[23] = SDMsgUtils.getPropValueStr(map, "dtOrdcate");
			// 特殊标记4(打包收费标志)
			strs[24] = SDMsgUtils.getPropValueStr(map, "");
			// 特殊标记2（限制标志）
			strs[25] = SDMsgUtils.getPropValueStr(map, "");
			// 特殊标记1（住院打包收费标志）
			strs[26] = SDMsgUtils.getPropValueStr(map, "");
			// 特殊标记（开立不显示标志）
			strs[27] = SDMsgUtils.getPropValueStr(map, "");
			Z04.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z04失败"+e);
		}
	}

	/**
	 * Undrug Z04(Group) 医嘱项目
	 * @param map
	 * @param mf
	 * @throws HL7Exception
	 */
	private void createMsgForUndrug_Z04Group(Map<String, Object> map, MFN_M01_MF mf){
		try{
			String codeOrdtype = SDMsgUtils.getPropValueStr(map, "codeOrdtype");
			Segment Z04 = (Segment) mf.get("Z04");
			String[] strs = new String[28];
			// 从map中通过key取对应的数据
			// 非药品编码
			strs[0] = SDMsgUtils.getPropValueStr(map, "code");
			// 用户自定义（助记）码
			strs[1] = SDMsgUtils.getPropValueStr(map, "spcode");
			// 非药品名称
			strs[2] = SDMsgUtils.getPropValueStr(map, "name");
			// 系统类别
			if(codeOrdtype.startsWith("02")){
				strs[3] = "UC";
			}else if(codeOrdtype.startsWith("03")){
				strs[3] = "UL";
			}
			// 最小费用代码
			strs[4] = SDMsgUtils.getPropValueStr(map, "euPriceType");
			// 国家编码
			strs[5] = SDMsgUtils.getPropValueStr(map, "");
			// 国际标准代码
			strs[6] = SDMsgUtils.getPropValueStr(map, "");
			// 三甲价
			strs[7] = SDMsgUtils.getPropValueStr(map, "price");
			// 单位
			strs[8] = transUnitNameFromPk(SDMsgUtils.getPropValueStr(map, "pkUnit"));
			// 特定治疗项目 0假 1真
			strs[9] = SDMsgUtils.getPropValueStr(map, "");
			// 计划生育标记
			strs[10] = SDMsgUtils.getPropValueStr(map, "");
			// 确认标志
			strs[11] = SDMsgUtils.getPropValueStr(map, "flagActive");
			// 有效性标识,因为目前标准不一致,所以进行转换
			strs[12] = transFlagActive(SDMsgUtils.getPropValueStr(map, "delFlag"));
			// 规格
			strs[13] = SDMsgUtils.getPropValueStr(map, "spec");
			// 执行科室
			strs[14] = SDMsgUtils.getPropValueStr(map, "codeDept");
			// 单位标识 （是否医嘱项目1 是，0不是）
			//strs[15] = SDMsgUtils.getPropValueStr(map, "flagSet");
			//strs[15] = map.containsKey("group")?"1":"0";
			strs[15] = "1";
			// 备注
			strs[16] = SDMsgUtils.getPropValueStr(map, "note");
			// 适用范围   默认 全部0  门诊 1 住院2
			strs[17] = "0";
			// 拼音码
			strs[18] = SDMsgUtils.getPropValueStr(map, "spcode");
			// 五笔码
			strs[19] = SDMsgUtils.getPropValueStr(map, "dCode");
			// 操作员
			strs[20] = UserContext.getUser().getCodeEmp();
			// 操作日期
			//strs[21] = SDMsgUtils.getPropValueStr(map, "modityTime");
			strs[21] = sdf.format(new Date());
			// 申请单类型
			strs[22] = codeOrdtype;
			// 样本类型 dt_ordcate (医嘱类型)
			strs[23] = SDMsgUtils.getPropValueStr(map, "dtOrdcate");
			// 特殊标记4(打包收费标志)
			strs[24] = SDMsgUtils.getPropValueStr(map, "");
			// 特殊标记2（限制标志）
			strs[25] = SDMsgUtils.getPropValueStr(map, "");
			// 特殊标记1（住院打包收费标志）
			strs[26] = SDMsgUtils.getPropValueStr(map, "");
			// 特殊标记（开立不显示标志）
			strs[27] = SDMsgUtils.getPropValueStr(map, "");
			Z04.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z04失败"+e);
		}
	}

	/**
	 * UndruGroup
	 * @param map
	 * @param mf
	 * @throws HL7Exception
	 */
	private void createMsgForUndrug_Z05(Map<String, Object> map, MFN_M01_MF mf){
		try{
			mf.addNonstandardSegment("Z05");
			Segment Z05 = (Segment) mf.get("Z05");
			String[] strs = new String[11];

			// 从map中通过key取对应的数据
			// 组套编码
			strs[0] = transItemCodeFromPk(SDMsgUtils.getPropValueStr(map, "pkItem"));
			// 组套名称
			strs[1] = transItemNameFromPk(SDMsgUtils.getPropValueStr(map, "pkItem"));
			// 非药品编码
			strs[2] = transItemCodeFromPk(SDMsgUtils.getPropValueStr(map, "pkItemChild"));
			// 非药品名称
			strs[3] = transItemNameFromPk(SDMsgUtils.getPropValueStr(map, "pkItemChild"));
			// 输入码
			strs[4] = "";
			// 有效性,用del还是active?
			strs[5] = transFlagActive(SDMsgUtils.getPropValueStr(map, "delFlag"));
			// 数量
			// 将int转换成string
			strs[6] = SDMsgUtils.getPropValueStr(map, "quan");
			// 顺序号
			strs[7] = "0";
			// 操作员
			strs[8] = UserContext.getUser().getCodeEmp();
			// 操作日期
			strs[9] = DateUtils.getDateTimeStr(new Date());
			// 系统类别
			strs[10] = "";

			Z05.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z05失败"+e);
		}
	}

	/**
	 * 拼接Z段数据
	 * @param strs  包含数据的String数组
	 * @param zstr 拼接后存放的String
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
	 * @param value  需要转换的值
	 * @return
	 */
	private String transFlagActive(String value) {
		if ("1".equals(value)) {
			return "0";
		} else {
			return "1";
		}
	}

	/**
	 * 通过主键对单位进行转换
	 * @param pk  主键
	 * @return 单位名称
	 */
	private String transUnitNameFromPk(String pk) {
		if(StringUtil.isBlank(pk)){
			return "";
		}
		Map<String, Object> resultMap = DataBaseHelper.queryForMap("SELECT NAME from BD_UNIT where PK_UNIT = ?", pk);
		String name = (String) resultMap.get("name");
		if (null == name) {
			name = "";
		}
		return name;
	}

	/**
	 * 通过主键对名称进行转换
	 */
	private String transItemNameFromPk(String pk) {
		Map<String, Object> resultMap = DataBaseHelper.queryForMap(
				"SELECT NAME from BD_ITEM WHERE PK_ITEM = ?", pk);
		String name = (String) resultMap.get("name");
		if (null == name) {
			name = "";
		}
		return name;
	}

	/**
	 * 通过主键对编码进行转换
	 */
	private String transItemCodeFromPk(String pk) {
		Map<String, Object> resultMap = DataBaseHelper.queryForMap(
				"SELECT CODE from BD_ITEM WHERE PK_ITEM = ?", pk);
		String code = (String) resultMap.get("code");
		if (null == code) {
			code = "";
		}
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
			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "opeType"));
			key.setValue(SDMsgUtils.getPropValueStr(map, "code"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("CE");

			mf.addNonstandardSegment("Z06");
			Segment z06 = (Segment) mf.get("Z06");

			/** 查询药品基本信息,编码,名称等 */
			List<Map<String,Object>> list = sDMsgMapper.queryPdInfoList(map);
			if(list != null && list.size() > 0){
				Map<String, Object> nameMap = list.get(0);
				if(nameMap != null) map.putAll(nameMap);
			}

			String[] strs = new String[62];
			// √ 药品编码
			strs[0] = SDMsgUtils.getPropValueStr(map, "code");
			// √ 商品名称
			strs[1] = SDMsgUtils.getPropValueStr(map, "name");
			// √ 商品名自定义码
			strs[2] = SDMsgUtils.getPropValueStr(map, "spcode");
			// √ 物品简称
			strs[3] = SDMsgUtils.getPropValueStr(map, "shortName");
			// √ 通用名自定义码
			strs[4] = "";
			//学名（取了全名）
			strs[5] = SDMsgUtils.getPropValueStr(map, "nameGen");
			//学名自定义码
			strs[6] = SDMsgUtils.getPropValueStr(map, "spcode");
			//别名(取了化学名称)
			strs[7] = SDMsgUtils.getPropValueStr(map, "nameChem");
			//别名自定义码
			strs[8] = "";
			//英文通用名
			strs[9] = "";
			//英文别名,剂型编码（常数DOSAGEFORM）
			strs[10] = "";//SDMsgUtils.getPropValueStr(map, "dtDosageName");
			//英文名
			strs[11] = "";
			//国际编码
			strs[12] = "";
			// √ 国家编码
			strs[13] = SDMsgUtils.getPropValueStr(map, "codeStd");
			strs[14] = getDrugSysClass(map);// √ 系统类别(约定值) = 药品类别
			strs[15] = getDrugFeeCode(map);// √ 最小费用代码（常数MINFEE）
			strs[16] = strs[14];// √ 药品类别（常数ITEMTYPE）
			strs[17] = getDrugQuality(map);// √ 药品性质（常数DRUGQUALITY）
			strs[18] = SDMsgUtils.getPropValueStr(map, "spec");// √ 规格
			strs[19] = SDMsgUtils.getPropValueStr(map, "price");// √ 零售价
			strs[20] = SDMsgUtils.getPropValueStr(map, "price");// √ 最新购入价
			strs[21] = SDMsgUtils.getPropValueStr(map, "unitPackName");// √ 包装单位-名称
			strs[22] = SDMsgUtils.getPropValueStr(map, "packSize");// √ 包装数
			strs[23] = SDMsgUtils.getPropValueStr(map, "unitMinName");// √ 最小单位-名称
			strs[24] = SDMsgUtils.getPropValueStr(map, "dtDosage");// √ 剂型编码（常数DOSAGEFORM）
			strs[25] = SDMsgUtils.getPropValueStr(map, "vol");// √ 基本剂量
			strs[26] = SDMsgUtils.getPropValueStr(map, "unitVolName");// √ 剂量单位-名称
			strs[27] = SDMsgUtils.getPropValueStr(map, "codeSupply");//用法编码（CIS提供）
			strs[28] = SDMsgUtils.getPropValueStr(map, "codeFreq");//频次编码（CIS提供）
			strs[29] = "";// √ 一次用量
			strs[30] = "";//注意事项
			strs[31] = "";//一级药理作用 -字典PhaFunction
			strs[32] = "";//二级药理作用 -字典PhaFunction
			strs[33] = "";//三级药理作用 -字典PhaFunction
			strs[34] = getDrugValidSate(map);// √ 有效性标志（1 在用 0 停用 2 废弃）
			strs[35] = getDrugSelfFlag(map);// √ 自制标志（0-非自产，1-自产）
			strs[36] = "";// √ OCT标志（0非处方药 1处方药）
			strs[37] = SDMsgUtils.getPropValueStr(map, "flagGmp");// √ GMP标志 （0非GMP,1GMP）
			strs[38] = SDMsgUtils.getPropValueStr(map, "flagSt");// √ 是否需要试敏（0不需要1需要）
			strs[39] = SDMsgUtils.getPropValueStr(map, "companyCode");// √ 最新供药公司-字典PhaCompany
			strs[40] = SDMsgUtils.getPropValueStr(map, "produceName");//产地 ,生产厂家名称
			strs[41] = SDMsgUtils.getPropValueStr(map, "produceCode");// √ 生产厂家 - 字典PhaProduct
			strs[42] = SDMsgUtils.getPropValueStr(map, "apprNo");//批文信息
			strs[43] = "";//有效成分
			strs[44] = "";// √ 临嘱拆分类型（0 可拆包装单位 1 不能拆包装单位）
			strs[45] = "0";// √ 参考零售价2
			strs[46] = "";// √ 扩展数据3（长嘱拆分）
			strs[47] = SDMsgUtils.getPropValueStr(map, "note");//备注
			strs[48] = SDMsgUtils.getPropValueStr(map, "spcode");// √ 商品名拼音码
			strs[49] = "";// √ 商品名五笔码
			strs[50] = SDMsgUtils.getPropValueStr(map, "spcodePdGn");// √ 通用名拼音码
			strs[51] = SDMsgUtils.getPropValueStr(map, "dCodePdGn");// √ 通用名五笔码
			strs[52] = "";//学名拼音码
			strs[53] = "";//学名五笔码
			strs[54] = "";//别名拼音码
			strs[55] = "";//别名五笔码
			strs[56] = UserContext.getUser().getCodeEmp();// √ 操作员(人员编码)
			strs[57] = sdf.format(new Date());// √ 操作时间--当前时间
			strs[58] = "";// √ 特殊标记4（门诊（打包收费标记）0否 1是 ）
			strs[59] = "";// √ 特殊标记2（限制标志   0否 1是 ）
			strs[60] = "";// √ 特殊标记1（住院打包收费标志   0否 1是 ）
			strs[61] = "";// √ 项目等级（药品使用等级）

			z06.parse(makeZxxString(strs, zstr));

		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z06失败"+e);
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

			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "rleCode"));
			key.setValue(SDMsgUtils.getPropValueStr(map, "codeDept"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("CE");

			mf.addNonstandardSegment("Z01");
			Segment Z01 = (Segment) mf.get("Z01");

			String[] strs = new String[14];
			// 科室编码
			strs[0] = SDMsgUtils.getPropValueStr(map, "codeDept");
			// 科室名称
			strs[1] = SDMsgUtils.getPropValueStr(map, "nameDept");
			// 科室英文
			strs[2] = "";
			// 科室简称
			strs[3] = SDMsgUtils.getPropValueStr(map, "shortname");
			// 科室类型
			//strs[4] = SDMsgUtils.getPropValueStr(map, "dtDepttype");
			// 科室类型dtDepttype
			String dtEmptype = map.get("dtDepttype").toString();
			if(dtEmptype != null && dtEmptype.equals("01")){
				if("1".equals(map.get("flagIp"))){
					// I  住院
					strs[4]="I";
				}else if("1".equals(map.get("flagOp"))){
					// C 门诊
					strs[4]="C";
				}
				strs[4]="I";//默认住院
			}else if("02".equals(dtEmptype)){
				//护理
				strs[4]="N";
			}else if("03".equals(dtEmptype.substring(0, 2))){
				//医技
				strs[4]="T";
			}else if("04".equals(dtEmptype.substring(0,2))){
				//药剂或 P药房（0402）
				strs[4]="P";
				if("0401".equals(dtEmptype)){
					strs[4]="PI";//药库
				}
			}else if("08".equals(dtEmptype)){
				strs[4]="F";
			}else if("0802".equals(dtEmptype)){
				//D  部门机关(department)
				strs[4]="D";
			}
			else {//其他
				strs[4]="O";
			}
			// 是否挂号科室
			strs[5] = "";
			// 是否核算科室
			strs[6] = "";
			// 特殊科室属性
			strs[7] = "";
			// 有效性标志
			strs[8] = SDMsgUtils.getPropValueStr(map, "flagActive");
			// 顺序号
			strs[9] = SDMsgUtils.getPropValueStr(map, "sortno");
			// 拼音码
			strs[10] = SDMsgUtils.getPropValueStr(map, "pyCode");
			// 自定义码
			strs[11] = SDMsgUtils.getPropValueStr(map, "dCode");
			// 操作员
			strs[12] = SDMsgUtils.getPropValueStr(map, "empCode");
			// 操作时间
			strs[13] = sdf.format(new Date());

			Z01.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z01失败"+e);
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
					mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "rleCode"));
					key.setValue(SDMsgUtils.getPropValueStr(userMap, "codeUser"));
					mfe.getPrimaryKeyValueMFE(0).setData(key);
					mfe.getPrimaryKeyValueType(0).setValue("CE");

					mf.addNonstandardSegment("ZU0");
					Segment ZU0 = (Segment) mf.get("ZU0");

					//获取用户所在用户组名称
					//String codeusrgrp = DataBaseHelper.queryForScalar("select code_usrgrp from BD_OU_USRGRP where pk_usrgrp=?", String.class, SDMsgUtils.getPropValueStr(userMap,"pkUsrgrp"));
					//String codeusrgrp = DataBaseHelper.queryForScalar("select * from (SELECT CODE_DEPT FROM bd_ou_dept bd LEFT JOIN bd_ou_empjob empjob ON empjob.pk_dept = bd.pk_dept WHERE empjob.Pk_emp = ?) where rownum=1", String.class, SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
					//获取用户关联人员的医疗项目权限(职称)
					String educationTitle = null;
					try{
						educationTitle = DataBaseHelper.queryForScalar("SELECT  name from  BD_DEFDOC where CODE_DEFDOCLIST='010305' and code =(select dt_empsrvtype from bd_ou_employee where pk_emp=?)",String.class,SDMsgUtils.getPropValueStr(userMap,"pkEmp"));
					}catch(EmptyResultDataAccessException e){
						educationTitle = "";
					}
					//判断用户主键是否是空(添加的时候主键为空),如果为空则查询数据库

					String pkUser = SDMsgUtils.getPropValueStr(userMap,"pkUser");
					if(pkUser==null || pkUser.equals("")){
						//查询用户主键和密码
						Map<String,Object> userInfo = DataBaseHelper.queryForMap("select pk_user,pwd from BD_OU_USER where pk_org=? and code_user=?",SDMsgUtils.getPropValueStr(userMap,"pkOrg"),SDMsgUtils.getPropValueStr(userMap,"codeUser"));
						userMap.put("pkUser", userInfo.get("pkUser"));
						userMap.put("pwd", userInfo.get("pwd"));
					}

					String[] strs = new String[17];
					//数据库用户名
					strs[0]=SDMsgUtils.getPropValueStr(userMap,"pkUser");
					//用户标识
					strs[1]=SDMsgUtils.getPropValueStr(userMap,"pkUser");
					//用户姓名
					strs[2]=SDMsgUtils.getPropValueStr(userMap,"nameUser");
					//用户科室(用户组名称)
					strs[3]=SDMsgUtils.getPropValueStr(userMap,"codeDept");
					//建立日期
					Date date=SDMsgUtils.getPropValueDate(userMap,"createTime");
					strs[4]=date==null?"":sdf.format(date);
					//用户密码(加密)
					//strs[5]=SDMsgUtils.getPropValueStr(userMap,"pwd");
					strs[5]="";
					//输入拼音(表里没有该字段)
					strs[6]=SDMsgUtils.getPropValueStr(userMap,"spcode");
					//拼音码(表里没有该字段)
					strs[7]=SDMsgUtils.getPropValueStr(userMap,"spcode");
					//五笔码(表里没有该字段)
					strs[8]="";
					//域用户名(表里没有该字段)
					strs[9]="";
					//密保等级(表里没有该字段)
					strs[10]="";
					//登录名
					strs[11]=SDMsgUtils.getPropValueStr(userMap,"codeUser");
					//组编号(未确定)
					strs[12]="";
					//职称(表里没有该字段)
					strs[13]=educationTitle;
					//有无资质
					strs[14]="1";
					//数字证书(表里没有该字段)
					strs[15]="";
					//用户类型(未确定)
					strs[16]=SDMsgUtils.getPropValueStr(userMap,"euUsertype");

					ZU0.parse(makeZxxString(strs, zstr));
				}}
		} catch (Exception e) {
			loger.info("组装ZU0失败"+e);
			e.printStackTrace();
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

			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "rleCode"));
			key.setValue(SDMsgUtils.getPropValueStr(map, "codeEmp"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("CE");

			mf.addNonstandardSegment("Z02");
			Segment Z02 = (Segment) mf.get("Z02");

			String[] strs=new String[18];
			//员工代码
			strs[0]=SDMsgUtils.getPropValueStr(map,"codeEmp");
			//员工姓名
			strs[1]=SDMsgUtils.getPropValueStr(map,"nameEmp");
			//员工性别
			strs[2]=SDMsgUtils.getSex(SDMsgUtils.getPropValueStr(map,"dtSex"));
			//出生日期
			Date date=SDMsgUtils.getPropValueDate(map,"birthday");
			strs[3]=date==null?"":sdf.format(date);
			//Map<String,Object> empMap=(Map<String,Object>)map.get("emp");

			String dtEmptype =SDMsgUtils.getSex(SDMsgUtils.getPropValueStr(map,"dtEmptype"));
			strs[4] = dtEmptype;
			//职级代号(待确定)
			strs[5]="";
			//学历(表里没有该字段)
			strs[6]="";
			//身份证号
			strs[7]=SDMsgUtils.getPropValueStr(map,"idno");
			//所属科室(科室编码^科室名称)
			strs[8]=SDMsgUtils.getPropValueStr(map,"deptInfo");
			//所属护理站(待确定)
			strs[9]=SDMsgUtils.getPropValueStr(map,"CODEDEPTS");
			//人员类型
			strs[10]=SDMsgUtils.getPropValueStr(map,"dtEmptype");
			//是否专家
			strs[11]=SDMsgUtils.getPropValueStr(map,"flagSpec");
			//有效性标志
			strs[12]=SDMsgUtils.getPropValueStr(map,"flagActive");
			//顺序号(表里没有该字段)
			strs[13]="";
			//拼音码
			strs[14]=SDMsgUtils.getPropValueStr(map,"pyCode");
			//五笔码
			strs[15]=SDMsgUtils.getPropValueStr(map,"dCode");
			//操作员
			strs[16]=SDMsgUtils.getPropValueStr(map,"empCode");//当前登录人的编码
			//操作时间
			strs[17]=sdf.format(new Date());

			Z02.parse(makeZxxString(strs, zstr));

		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z02失败"+e);
		}
	}

	/**
	 * 发送科室与病区关系字典信息
	 * @param dictCode
	 * @param map
	 * @param mfi
	 * @param mf
	 * @param mfe
	 * @param key :用户密码取消发送 。发送科室与病区关系字典信息(科室与病区关系):主键列传科室编码
	 * @throws HL7Exception
	 */
	private void createMsgForDeptStat_Z03(String dictCode, Map<String, Object> map,MFI mfi, MFN_M01_MF mf, MFE mfe,ST key){
		try{
			//组装mfi信息
			createMfiMessage(dictCode, null, mfi);

			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "control"));
			key.setValue(SDMsgUtils.getPropValueStr(map, "codeDept"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("CE");

			mf.addNonstandardSegment("Z03");
			Segment Z03 = (Segment) mf.get("Z03");

			String[] strs=new String[7];
			//判断是科室或是病区
			String deptType = "".equals(SDMsgUtils.getPropValueStr(map, "dtDepttype"))?"":SDMsgUtils.getPropValueStr(map, "dtDepttype").substring(0,2);
			switch (deptType){
				case "01":{
					strs[0]=SDMsgUtils.getPropValueStr(map, "codeDept");
				}
				case "02":{
					strs[1]=SDMsgUtils.getPropValueStr(map, "codeDept");
					strs[2]=SDMsgUtils.getPropValueStr(map, "nameDept");
				}
			}
			//当前科室编码
			strs[3]=SDMsgUtils.getPropValueStr(map,"codeDept");
			//有效性标志
			strs[4]="1";
			//操作员
			strs[5]=UserContext.getUser().getCodeEmp();
			//操作时间
			strs[6]=sdf.format(new Date());
			Z03.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z03失败"+e);
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

			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "rleCode"));
			key.setValue(SDMsgUtils.getPropValueStr(map, "code"));
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
			String pkOrdtype = SDMsgUtils.getPropValueStr(map,"pkOrdtype");
			if(!"".equals(pkOrdtype)){
				String name = DataBaseHelper.queryForScalar("select name from bd_ordtype where PK_ORDTYPE = ?",String.class,pkOrdtype);
				if("检查".equals(name)) {
					occType = "1";
				} else if("检验".equals(name)) {
					occType = "2";
				} else if("护理".equals(name)) {
					occType = "3";
				} else if("材料".equals(name)) {
					occType = "4";
				} else {
					occType = "0";
				}
			}
			//获取执行分类
			String occFlag = "";
			String flagNs = SDMsgUtils.getPropValueStr(map,"flagNs");//护士使用标志
			String flagDr = SDMsgUtils.getPropValueStr(map,"flagDr");//医生使用标志
			if("1".equals(flagNs)&& "1".equals(flagDr)) {
				occFlag = "2";
			} else if("1".equals(flagNs)) {
				occFlag = "0";
			} else if("1".equals(flagDr)) {
				occFlag = "1";
			}

			String[] strs = new String[13];
			//医嘱编码
			strs[0]=SDMsgUtils.getPropValueStr(map,"code");
			//医嘱名称
			strs[1]=SDMsgUtils.getPropValueStr(map,"name");
			//拼音码
			strs[2]=SDMsgUtils.getPropValueStr(map,"spcode");
			//自定义码
			strs[3]=SDMsgUtils.getPropValueStr(map,"dCode");
			//排斥分组码
			strs[4]=SDMsgUtils.getPropValueStr(map,"euExclude");
			//医嘱分类(分类编码)
			strs[5]=map.get("codeOrdtype").toString().substring(0,2);
			//删除标记
			strs[6]=SDMsgUtils.getPropValueStr(map,"flagDel");
			//描述
			strs[7]=SDMsgUtils.getPropValueStr(map,"note");
			//执行科室(科室编码)
			strs[8]=bdOrdDeptCode;
			String flagCg = SDMsgUtils.getPropValueStr(map,"flagCg");
			//执行标记(执行计费) 0 直接执行 1 计费执行 2 执行计费 flagCg
			if("1".equals(flagCg)){
				strs[9]="1";
			}else{
				strs[9]="0";
			}
			//执行单分类
			strs[10]=occType;
			//执行分类
			strs[11]=occFlag;
			//打印名称
			strs[12]=SDMsgUtils.getPropValueStr(map,"namePrt");

			ZA1.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装ZA1失败"+e);
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
				map.putAll(SDMsgUtils.objectToMap(map.get("freq")));
			}
			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "rleCode"));
			key.setValue(SDMsgUtils.getPropValueStr(map, "code"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("ST");

			mf.addNonstandardSegment("ZA2");
			Segment ZA2 = (Segment) mf.get("ZA2");

			//获取分类
			String pkSupplycate = SDMsgUtils.getPropValueStr(map,"pkSupplycate");//获取医嘱分类主键
			String classCode="";
			if(!"".equals(pkSupplycate)){
				String name = DataBaseHelper.queryForScalar("select name from bd_supply_class where PK_SUPPLYCATE = ?",String.class,pkSupplycate);
				if("口服".equals(name)|| "口服类".equals(name)) {
					classCode="1";
				} else if("输液".equals(name)|| "输液类".equals(name)) {
					classCode="2";
				} else if("注射".equals(name)|| "注射类".equals(name)) {
					classCode="3";
				} else if("出院带药".equals(name)|| "出院带药类".equals(name)) {
					classCode="4";
				} else if("领药(临时出现)".equals(name)|| "领药类(临时出现)".equals(name)) {
					classCode="5";
				} else if("退药".equals(name)|| "退药类".equals(name)) {
					classCode="6";
				} else if("领药(不出现)".equals(name)|| "领药类(不出现)".equals(name)) {
					classCode="7";
				} else if("其他".equals(name)|| "其他类".equals(name)) {
					classCode="9";
				} else if("草药".equals(name)|| "草药类".equals(name)) {
					classCode="a";
				}
			}

			String[] strs = new String[8];
			//编码
			strs[0]=SDMsgUtils.getPropValueStr(map,"code");
			//名称
			strs[1]=SDMsgUtils.getPropValueStr(map,"name");
			//拼音码
			strs[2]=SDMsgUtils.getPropValueStr(map,"spcode");
			//自定义码
			strs[3]=SDMsgUtils.getPropValueStr(map,"dCode");
			//分类
			strs[4]=classCode;
			//排序码(表内没有该字段)
			strs[5]="";
			//打印名称
			strs[6]=SDMsgUtils.getPropValueStr(map,"namePrint");
			//删除标记
			strs[7]=SDMsgUtils.getPropValueStr(map,"delFlag");

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

			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "rleCode"));
			key.setValue(SDMsgUtils.getPropValueStr(map, "code"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("ST");

			mf.addNonstandardSegment("ZA3");
			Segment ZA3 = (Segment) mf.get("ZA3");

			//获取频次时间点
			StringBuilder freqTimes = new StringBuilder();
			List<Map<String,Object>>  timeList = (List<Map<String,Object>>)map.get("timeList");
			if(timeList!=null && timeList.size()>0){
				for(int p=0;p<timeList.size();p++){
					String tempTime = timeList.get(p).get("timeOcc").toString();
					String timeOcc=tempTime==null?"":tempTime;
					if(p==0) {
						freqTimes = new StringBuilder(timeOcc);
					} else {
						freqTimes.append("-").append(timeOcc);
					}
				}
			}

			String[] strs = new String[10];
			//编码
			strs[0]=SDMsgUtils.getPropValueStr(map,"code");
			//名称
			strs[1]=SDMsgUtils.getPropValueStr(map, "name");
			//拼音码
			strs[2]=SDMsgUtils.getPropValueStr(map,"spcode");
			//自定义码
			strs[3]=SDMsgUtils.getPropValueStr(map,"dCode");
			//打印名称
			strs[4]=SDMsgUtils.getPropValueStr(map,"namePrint");
			//周标志
			strs[5]=SDMsgUtils.getPropValueStr(map,"euCycle");
			//删除标记
			strs[6]=SDMsgUtils.getPropValueStr(map,"delFlag");
			//排序码(表内没有该字段)
			strs[7]="";
			//医生使用标志(表内没有该字段)
			strs[8]="";
			//频次时间点
			strs[9]= freqTimes.toString();

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
				map.putAll(SDMsgUtils.objectToMap(map.get("diag")));
			}
			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "rleCode"));
			key.setValue(SDMsgUtils.getPropValueStr(map, "diagcode"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("CE");

			mf.addNonstandardSegment("Z10");
			Segment Z10 = (Segment) mf.get("Z10");
			String[] strs = new String[12];
			//主键
			strs[0]=SDMsgUtils.getPropValueStr(map,"pkDiag");
			//icd10主诊断编码
			strs[1]=SDMsgUtils.getPropValueStr(map,"diagcode");
			//中文疾病名称
			strs[2]=SDMsgUtils.getPropValueStr(map,"diagname");
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
			strs[8]=SDMsgUtils.getPropValueStr(map,"empCode");
			//操作时间
			strs[9]=sdf.format(new Date());
			//拼音码
			strs[10]=SDMsgUtils.getPropValueStr(map,"pyCode");
			//五笔码
			strs[11]=SDMsgUtils.getPropValueStr(map,"dCode");
			Z10.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z10失败"+e);
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

			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(map, "rleCode"));
			key.setValue(SDMsgUtils.getPropValueStr(map, "diagcode"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("CE");

			mf.addNonstandardSegment("ZD1");
			Segment ZD1 = (Segment) mf.get("ZD1");

			//是否自定义
			String dtDiagcate = SDMsgUtils.getPropValueStr(map,"dtDiagcate");//获取自定义分类
			String IsZdy = dtDiagcate!=null&&!dtDiagcate.equals("")?"1":"0";

			String[] strs = new String[10];
			//手术操作代码
			strs[0]=SDMsgUtils.getPropValueStr(map,"diagcode");
			//手术操作名词
			strs[1]=SDMsgUtils.getPropValueStr(map,"diagname");
			//手术等级
			strs[2]=SDMsgUtils.getPropValueStr(map,"levelOp");
			//正名标志(未确定)
			strs[3]="";
			//标准化标志(未确定)
			strs[4]="";
			//创建日期(表内没有该字段)
			strs[5]="";
			//拼音码
			strs[6]=SDMsgUtils.getPropValueStr(map,"pyCode");
			//五笔码
			strs[7]=SDMsgUtils.getPropValueStr(map,"dCode");
			//是否停止(未确定)
			strs[8]="";
			//是否自定义
			strs[9]=IsZdy;

			ZD1.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装ZD1失败"+e);
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

			key.setValue(SDMsgUtils.getPropValueStr(map, "code"));
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
			loger.info("Z99组装MFE失败"+e);
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

			key.setValue(SDMsgUtils.getPropValueStr(map, "code"));
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
			loger.info("Z07组装MFE失败"+e);
		}
	}

	public void setMfSegmentFromBdDefdoc(Map<String,Object> map, MFN_M01_MF mf){
		try{
			Segment Z99 = (Segment) mf.get("Z99");
			String[] strs = new String[11];
			//类型
			strs[0]=SDMsgUtils.getPropValueStr(map,"codeDefdoclist");
			//编码
			strs[1]=SDMsgUtils.getPropValueStr(map,"code");
			//名称
			strs[2]=SDMsgUtils.getPropValueStr(map,"name");
			//用户自定义助记码(未确定)
			strs[3]="";
			//顺序号(表内没有该字段)
			strs[4]="";
			//有效应标志(未确定)
			strs[5]=SDMsgUtils.getPropValueStr(map,"delFlag");
			//备注
			strs[6]=SDMsgUtils.getPropValueStr(map,"memo");
			//拼音码
			strs[7]=SDMsgUtils.getPropValueStr(map,"pyCode");
			//五笔码
			strs[8]=SDMsgUtils.getPropValueStr(map,"dCode");
			//操作员
			strs[9]=SDMsgUtils.getPropValueStr(map,"empCode");
			//操作时间
			strs[10]=sdf.format(new Date());

			Z99.parse(makeZxxString(strs, zstr));
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z99失败"+e);
		}
	}

	public void setMfSegmentFromBed(Map<String,Object> map, MFN_M01_MF mf){
		try{
			Segment Z07 = (Segment) mf.get("Z07");
			String[] strs = new String[17];
			//床位编码
			Map<String, Object> deptInfoByPkDept = sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(map,"pkWard"));
			if(deptInfoByPkDept!=null && deptInfoByPkDept.size()>0){
				String code = SDMsgUtils.getPropValueStr(deptInfoByPkDept, "codeDept")+SDMsgUtils.getPropValueStr(map,"code");
				strs[0] = code;
			}
			//护士站代码（所属病区编码）
			strs[1] = getDeptCode(SDMsgUtils.getPropValueStr(map,"pkWard"));
			//床位等级编码(未确定)
			strs[2] = "";
			//床位编制(未确定)
			strs[3] = SDMsgUtils.getPropValueStr(map, "dtBedtype");
			//床位状态
			strs[4] = SDMsgUtils.getPropValueStr(map,"euStatus");
			//病室号
			strs[5] = SDMsgUtils.getPropValueStr(map,"houseno");
			//医师代码(主管医生编码)
			strs[6] = SDMsgUtils.getPropValueStr(map,"pkPhyEmp");
			//床位电话(未确定)于病历协商穿床位名称
			strs[7] = SDMsgUtils.getPropValueStr(map,"name");
			//归属
			strs[8] = "";
			//医疗流水号(住院号) (深大改为不赋值)
			//if(qryOpOrIpCodeBypkPi(SDMsgUtils.getPropValueStr(map,"pkPi"))!=null)
			//strs[9] =qryOpOrIpCodeBypkPi(SDMsgUtils.getPropValueStr(map,"pkPi")).get("codeIp").toString();
			//出院日期
			strs[10] = "";
			//有效性标志
			strs[11] = SDMsgUtils.getPropValueStr(map,"flagActive");
			String yuYue="";
			if("04".equals(SDMsgUtils.getPropValueStr(map, "euStatus"))) {
				yuYue="1";
			} else{
				yuYue="0";
			}
			//预约标志(未确定)
			strs[12] = yuYue;
			//顺序号
			strs[13] = SDMsgUtils.getPropValueStr(map,"code");
			//护理组(未确定)
			strs[14] =SDMsgUtils.getPropValueStr(map, "namePlace");
			//操作员
			strs[15] = SDMsgUtils.getPropValueStr(map,"empCode");
			//操作日期
			strs[16] = sdf.format(new Date());

			Z07.parse(makeZxxString(strs, zstr));

		} catch (Exception e) {
			// TODO: handle exception
			loger.info("组装Z07失败"+e);
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
	 * Z06-15 sys_class | Z06-17 drug_type 获取系统类别 | 药品类别
	 * @param map
	 * @return
	 */
	private String getDrugSysClass(Map<String, Object> map){
		if(map==null) {
			return "";
		}
		String durgType = SDMsgUtils.getPropValueStr(map, "euDrugtype");
		if("0".equals(durgType)) {
			return "P";
		} else if("1".equals(durgType)) {
			return "PCC";
		} else if("2".equals(durgType)) {
			return "PCZ";
		} else {
			return "";
		}
	}

	/**
	 * Z06-16 fee_code 最小费用 => 账单码
	 * @param map
	 * @return
	 */
	private String getDrugFeeCode(Map<String, Object> map){
		if(map==null) {
			return null;
		}
		String feeCode="";
		List<String> codeList=DataBaseHelper.queryForList(" select  inv_item.code from bd_invcate_item inv_item "
				+ " inner join bd_invcate_itemcate inv_itemcate on inv_item.pk_invcateitem = inv_itemcate.pk_invcateitem  "
				+ " inner join bd_pd pd on inv_itemcate.pk_itemcate = pd.pk_itemcate and inv_itemcate.del_flag = '0' "
				+ " where inv_item.del_flag = '0' and pd.pk_pd = ? order by inv_item.code",String.class, map.get("pkPd").toString());
		if(codeList!=null&& codeList.size()>0) {
			feeCode=codeList.get(0);
		}
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
		if(map==null) {
			return "";
		}
		String dtPois = SDMsgUtils.getPropValueStr(map, "dtPois");

		if("1".equals(SDMsgUtils.getPropValueStr(map, "flagRm"))) {
			return "E1";//原料
		} else if("1".equals(SDMsgUtils.getPropValueStr(map, "flagReag"))) {
			return "E2";//试剂
		} else if("1".equals(SDMsgUtils.getPropValueStr(map, "flagPrecious"))) {
			return "V";//贵重药
		} else if("00".equals(dtPois)) {
			return "O";//普药
		} else if("02".equals(dtPois)) {
			return "P1";//精一
		} else if("03".equals(dtPois)) {
			return "P2";//精二
		} else if("01".equals(dtPois)) {
			return "S1";//麻
		} else if("04".equals(dtPois)) {
			return "S2";//毒
		} else {
			return "";
		}
	}

	/**
	 * Z06-35 valid_state	有效性标志
	 * @param map
	 * @return
	 */
	private String getDrugValidSate(Map<String, Object> map){
		if(map==null) {
			return "";
		}
		String flagStop =  SDMsgUtils.getPropValueStr(map, "flagStop");
		if("0".equals(flagStop)) {
			return "1";
		} else if("1".equals(flagStop)) {
			return "0";
		} else {
			return "";
		}
	}

	/**
	 * Z06-36 self_flag	自制标志 :0-非自产，1-自产
	 * dtMade:0 购入, 1 自制, 2 原研
	 * @param map
	 * @return
	 */
	private String getDrugSelfFlag(Map<String, Object> map){
		if(map==null) {
			return "";
		}
		String flagStop =  SDMsgUtils.getPropValueStr(map, "dtMade");
		if("1".equals(flagStop) || "2".equals(flagStop)) {
			return "1";
		} else if("0".equals(flagStop)) {
			return "0";
		} else {
			return "";
		}
	}
}
