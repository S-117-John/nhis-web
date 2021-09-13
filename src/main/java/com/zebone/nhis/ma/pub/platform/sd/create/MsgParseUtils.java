package com.zebone.nhis.ma.pub.platform.sd.create;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.segment.MFE;
import ca.uhn.hl7v2.model.v24.segment.MFI;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.QPD;
import ca.uhn.hl7v2.model.v24.segment.RXA;
import ca.uhn.hl7v2.model.v24.segment.RXO;
import ca.uhn.hl7v2.model.v24.segment.RXR;

/**
 * HL7消息解析类
 * @author maijiaxing
 *
 */
@Component
public class MsgParseUtils {
	
	/**
	 * 构造  记费对外入口参数(弃用)
	 * @param orderMap
	 * @return
	 */
	public BlPubParamVo getCgVo(Map<String, Object> paramMap){
		BlPubParamVo blPubParamVo = new BlPubParamVo();
		blPubParamVo.setPkOrg(SDMsgUtils.getPropValueStr(paramMap,"pkOrg"));//机构必填
		blPubParamVo.setPkPres(null);//处方主键(选填)
		blPubParamVo.setEuPvType(SDMsgUtils.getPropValueStr(paramMap,"euPvtype"));//就诊类型（选填）
		blPubParamVo.setSpec(null);//规格
		blPubParamVo.setPkPv(SDMsgUtils.getPropValueStr(paramMap,"pkPv"));//患者就诊主键（必填）
		blPubParamVo.setPkPi(SDMsgUtils.getPropValueStr(paramMap,"pkPi"));//患者主键（必填）
		blPubParamVo.setPkItem(SDMsgUtils.getPropValueStr(paramMap,"pkOrd"));//医嘱编码主键 （选填） 记医嘱项目与药品费时必传
		blPubParamVo.setPkCnord(SDMsgUtils.getPropValueStr(paramMap,"pkCnord"));//医嘱主键（选填）
		blPubParamVo.setPkItem(null);//收费项目主键 false 只记收费项目时必传，记医嘱项目与药品费时不允许传
		Double quan = 1.00;
        Double quanCg = 1.00;
		String quanStr  = SDMsgUtils.getPropValueStr(paramMap,"quan");
		if(StringUtils.isNotBlank(quanStr)) quan = Double.valueOf(quanStr);
		String quanCgStr  = SDMsgUtils.getPropValueStr(paramMap,"quanCg");
		if(StringUtils.isNotBlank(quanCgStr)) quanCg = Double.valueOf(quanCgStr);
		blPubParamVo.setQuanCg("3".equals(blPubParamVo.getEuPvType())?quan:quanCg);//医嘱记费数量(必填)
		blPubParamVo.setPkOrgEx(SDMsgUtils.getPropValueStr(paramMap,"pkOrgEcex"));//执行机构 true
		blPubParamVo.setPkOrgApp(blPubParamVo.getPkOrg());//开立机构 true
		blPubParamVo.setPkDeptEx(SDMsgUtils.getPropValueStr(paramMap,"pkDeptExec"));//执行科室 true
		blPubParamVo.setPkDeptApp(SDMsgUtils.getPropValueStr(paramMap,"pkDept"));//开立科室 true
		blPubParamVo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(paramMap,"pkDeptNs"));//开立科室 true(护士)
		blPubParamVo.setPkEmpApp(SDMsgUtils.getPropValueStr(paramMap,"pkEmpInput"));//开立医生true
		blPubParamVo.setNameEmpApp(SDMsgUtils.getPropValueStr(paramMap,"nameEmpInput"));//开立医生姓名true
		blPubParamVo.setFlagPd("0");//物品标志 true
		blPubParamVo.setFlagFit(SDMsgUtils.getPropValueStr(paramMap,"flagFit"));//适应症标志 0非适应症，1适应症；药品记费使用
		blPubParamVo.setDescFit(null);//适应症信息
		blPubParamVo.setBatchNo(null);//批号
		blPubParamVo.setDateExpire(null);//失效日期
		blPubParamVo.setPkUnitPd(null);//零售单位
		blPubParamVo.setPackSize(null);//包装量（如果是药品，必传）
		blPubParamVo.setPriceCost(null);//药品成本单价 double类型
		blPubParamVo.setNamePd(null);//药品名称
		blPubParamVo.setFlagPv("0");//挂号费用标志 false 如果是挂号费用必须为1 否则全为0
		blPubParamVo.setDateHap(new Date());//服务发生日期true
		blPubParamVo.setPkDeptCg(blPubParamVo.getPkDeptEx());//记费科室true
		blPubParamVo.setPkEmpCg("~");//记费人员true
		blPubParamVo.setNameEmpCg("~");//记费人员名称true
		blPubParamVo.setPkOrdexdt(null);//关联执行单主键
		blPubParamVo.setInfantNo(SDMsgUtils.getPropValueStr(paramMap,"infantNo"));// 婴儿标志
		String price = "0.00" ;
		blPubParamVo.setPrice(Double.parseDouble(price));//如果是药品，此单价从外部直接传入，零售单价(double)
		blPubParamVo.setEuAdditem(null);//附加项目标志 0 非附加，1 附加项目
		
		//blPubParamVo.setDateStart(null);//医嘱开始日期，检验项目合并记费使用--中二
		//blPubParamVo.setCodeOrdtype(null);// 医嘱类型编码，检验项目合并记费使用--中二
		
		User u = new User();
		u.setPkOrg(blPubParamVo.getPkOrg()); //所属机构
		u.setPkEmp("~"); //@todo 记费人
		UserContext.setUser(u);
		return blPubParamVo;
	}
	
	/**
	 * 解析MSH消息
	 * @param msg
	 * @return
	 * @throws HL7Exception
	 */
	public static Map<String,Object> getMSH(Message msg) throws HL7Exception{
		Map<String,Object> mshMap = new HashMap<String,Object>();
		Segment segment = (Segment) msg.get("MSH");
		MSH msh=(MSH)segment;
		mshMap.put("MSH", msh);
		mshMap.put("msgType", msh.getMessageType().encode());
		mshMap.put("msgOldId", msh.getMessageControlID().getValue());
		mshMap.put("receive", msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
		return mshMap;
	}
	
	/**
	 * 解析PID消息
	 * @param msg
	 * @return
	 */
	public static Map<String,Object> getPID(PID pid){
		Map<String,Object> pidMap = new HashMap<String,Object>();
		//code_pi
		pidMap.put("codePi", pid.getPatientID().getID().getValue());
		return pidMap;
	}
	
	/**
	 * 解析PV1消息
	 * @param pv1
	 * @return
	 */
	public static Map<String,Object> getPV1(PV1 pv1){
		Map<String,Object> pv1Map = new HashMap<String,Object>();
		//mshMap.put("patientId", pv1.getPatientID().getID().getValue());
		return pv1Map;
	}
	
	/**
	 * 解析OBR消息
	 * @param obr
	 * @return
	 * @throws HL7Exception 
	 */
	public static Map<String,Object> getOBR(OBR obr) throws HL7Exception{
		Map<String,Object> obrMap = new HashMap<String,Object>();
		//OBR-2 医嘱号  ordsn
		obrMap.put("ordsn", obr.getPlacerOrderNumber().getEntityIdentifier().getValue());
		//OBR-3 申请号 code_apply（标本号）
		obrMap.put("codeApply", obr.getFillerOrderNumber().getEntityIdentifier().getValue());
		//检验类型Ordinary：普通报告 Describe：描述报告 MIC：药敏报告
		obrMap.put("rptType", obr.getUniversalServiceIdentifier().getCe3_NameOfCodingSystem().getValue());
		//OBR-6 送检日期
		obrMap.put("dateChk", obr.getField(6, 0).encode());
		//OBR-15 送检样品
		String[] specimen = obr.getField(15, 0).encode().split("&");
		if(specimen!=null && specimen.length>1){
			//样本号
			obrMap.put("codeSamp", specimen[0]);
			//样本名称
			obrMap.put("nameSamp", specimen[1]);
		}else{
			//样本名称
			obrMap.put("nameSamp", specimen[0]);
		}
		//OBR-18 送检科室名称
		obrMap.put("nameDept", obr.getPlacerField1().getValue());
		//OBR-19 送检科室编码
		obrMap.put("codeDept", obr.getPlacerField2().getValue());
		//OBR-20 检验报告唯一号
		obrMap.put("codeRpt", obr.getField(20, 0).encode());
		//OBR-22 报告日期
		obrMap.put("dateRpt", obr.getField(22, 0).encode());
		//OBR-32  主要解析者
		String[] obr32 = obr.getField(32, 0).encode().split("&");
		if(obr32 != null && obr32.length>0 && !"".equals(obr32[0])){
			String sql = "select e.pk_org,e.pk_emp,e.name_emp from bd_ou_employee e where e.code_emp=?";
			Map<String, Object> queryEmpByCode = DataBaseHelper.queryForMap(sql,obr32[0]);
			obrMap.put("codeChk", obr32[0]);
			if(queryEmpByCode!=null && queryEmpByCode.size()>0){
				obrMap.put("nameChk", SDMsgUtils.getPropValueStr(queryEmpByCode, "nameEmp"));
				obrMap.put("pkChk", SDMsgUtils.getPropValueStr(queryEmpByCode, "pkEmp"));
			}else{
				obrMap.put("nameChk", obr32[0]);
			}
		}
		//OBR-33  结果辅助解释者
		String[] obr33 = obr.getField(33, 0).encode().split("&");
		if(obr33 != null && obr33.length>0 && !"".equals(obr33[0])){
			String sql = "select e.pk_org,e.pk_emp,e.name_emp from bd_ou_employee e where e.code_emp=?";
			Map<String, Object> queryEmpByCode = DataBaseHelper.queryForMap(sql,obr33[0]);
			obrMap.put("codeEmpOcc", obr33[0]);
			if(queryEmpByCode!=null && queryEmpByCode.size()>0){
				obrMap.put("nameEmpOcc", SDMsgUtils.getPropValueStr(queryEmpByCode, "nameEmp"));
				obrMap.put("pkEmpOcc", SDMsgUtils.getPropValueStr(queryEmpByCode, "pkEmp"));
			}else{
				obrMap.put("nameEmpOcc", obr33[0]);
			}
		}
		return obrMap;
	}
	
	/**
	 * 解析QPD消息
	 * @param qpd
	 * @return
	 * @throws HL7Exception 
	 */
	public static Map<String,Object> getQPD(QPD qpd) throws HL7Exception{
		Map<String,Object> qpdMap = new HashMap<String,Object>();
		//QPD-1：Theme：列表查询，查询该患者所有住院就诊记录  Details
		qpdMap.put("sign", qpd.getMessageQueryName().getIdentifier().getValue());
		//QPD-3
		String varies = qpd.getQpd3_UserParametersInsuccessivefields().toString();
		//System.out.println(varies);
		//String string = varies.substring(varies.indexOf("[")+1, varies.indexOf("]"));
		Map<String, Object> map = SDMsgUtils.getStrBySplit("[","]","\\^",varies);
		if(null!=map){
			qpdMap.putAll(map);
		}
		//QPD-3.1：住院号  code_ip
		qpdMap.put("codeIp",SDMsgUtils.getPropValueStr(qpdMap, "1"));
		//QPD-3.5：PatientNO：住院号标识 
		qpdMap.put("signIp", SDMsgUtils.getPropValueStr(qpdMap, "5"));
		return qpdMap;
	}
	/**
	 * 解析ORC消息
	 * @param orc
	 * @return
	 */
	public static Map<String,Object> getORC(ORC orc){
		Map<String,Object> orcMap = new HashMap<String,Object>();
		//ORC-2  处方明细编码(执行主键)
		orcMap.put("pkExocc", orc.getPlacerOrderNumber().getEntityIdentifier().getValue());
		//ORC-4 医嘱号
		orcMap.put("ordsn", orc.getPlacerGroupNumber().getEntityIdentifier().getValue());
		////ORC-15  执行时间
		//20200104090300
		return orcMap;
	}
	
	/**
	 * 解析RXO消息
	 * @param rxo
	 * @return
	 */
	public static Map<String,Object> getRXO(RXO rxo){
		Map<String,Object> rxoMap = new HashMap<String,Object>();
		return rxoMap;
	}
	
	/**
	 *  解析RXR消息
	 * @param rxr
	 * @return
	 */
	public static Map<String,Object> getRXR(RXR rxr){
		Map<String,Object> rxrMap = new HashMap<String,Object>();
		return rxrMap;
	}
	
	/**
	 *  解析RXA消息
	 * @param rxr
	 * @return
	 */
	public static Map<String,Object> getRXA(RXA rxa){
		Map<String,Object> rxaMap = new HashMap<String,Object>();
		//10 -Completion Status完成情况	执行护士
		rxaMap.put("nsCode", rxa.getCompletionStatus().getValue());
		//11-Administered-at Location执行定位	执行科室
		rxaMap.put("nsDept", rxa.getAdministeredAtLocation().getPointOfCare().getValue());
		//21-Action Code-RXA　RXA行动代码	"0：取消  1：执行"
		rxaMap.put("status", rxa.getActionCodeRXA().getValue());
		//22-System Entry Date/Time系统录入的日期/时间	执行时间
		rxaMap.put("dateOcc", rxa.getSystemEntryDateTime().getTimeOfAnEvent().getValue());
		return rxaMap;
	}

	/**
	 * 获取MFI段消息数据
	 * @param mfi
	 * @return
	 */
	public static Map<String, Object> getMFI(MFI mfi) {
		Map<String,Object> mfiMap = new HashMap<String,Object>();
		//消息类型
		mfiMap.put("msgTypeCode", mfi.getMasterFileIdentifier().getIdentifier().getValue());
		//mfi.getMasterFileIdentifier().getText().getValue();
		//消息类型名字
		mfiMap.put("msgTypeName", mfi.getMasterFileIdentifier().getText().getValue());
		
		return mfiMap;
	}

	/**
	 * 获取MFE段消息数据
	 * @param mfe
	 * @return
	 */
	public static Map<String, Object> getMFE(MFE mfe) {
		Map<String,Object> mfeMap = new HashMap<String,Object>();
		//操作类型（增删改查）
		mfeMap.put("opType", mfe.getRecordLevelEventCode().getValue());
		return mfeMap;
	}

	/**
	 * 获取Z段消息数据（根据| 和^ 分割）
	 * @param zxx
	 * @return
	 * @throws HL7Exception 
	 */
	public static Map<String, Object> getZXX(Message msg,String msgType) throws HL7Exception {
		Map<String,Object> zxxMap = new HashMap<String,Object>();
		String msgStr = msg.encode();
	    String zStr = msgStr.substring(msgStr.substring(0, msgStr.indexOf(msgType)).length(), msgStr.length());
	    String[] zArr = zStr.split("\\|");
		for(int i=0;i<zArr.length;i++){
			//判断是否为空
			if(zArr[i]!=null){
				//判断是否有^
				if(zArr[i].contains("^")){
					String[] zData = zArr[i].split("\\^");
					for(int z=0;z<zData.length;z++){
						if(zData[z]!=null){
							zxxMap.put("z"+i+"."+(z+1), zData[z].toString());
						}else {
							zxxMap.put("z"+i+"."+(z+1), "");
						}
					}
				}else {
					zxxMap.put("z"+i, zArr[i]);
				}
			}else{
				zxxMap.put("z"+i, "");
			}
		}
		return zxxMap;
	}
	
	/**
	 * 获取某个消息域的值（根据^ 分割）
	 * @param value
	 * @return
	 */
	public static Map<String,Object> getMsgValue(String value){
		Map<String,Object> valueMap = new HashMap<String,Object>();
		if(value==null || "".equals(value)){
			return null;
		}else{
			String[] split = value.split("\\^");
			for(int i=0;i<split.length;i++){
				valueMap.put("value"+(i+1), split[i]);
			}
		}
		return valueMap;
	}

	/**
	 * 获取检验结果信息
	 * @param obx
	 * @return
	 * @throws HL7Exception 
	 */
	public static Map<String,Object> getOBX(OBX obx) throws HL7Exception {
		Map<String,Object> obxMap = new HashMap<String,Object>();
		//obx-1 设置ID
		obxMap.put("id", obx.getSetIDOBX().getValue());
		//obx-2 类型
		obxMap.put("type", obx.getValueType().getValue());
		//obx-3.1 项目id(细菌英文)
		obxMap.put("codeIndex", obx.getObservationIdentifier().getIdentifier().getValue());
		//obx-3.2 项目名称（细菌名称）
		obxMap.put("nameIndex", obx.getObservationIdentifier().getText().getValue());
		//obx-3.4 细菌编码
		obxMap.put("codeBact", obx.getObservationIdentifier().getAlternateIdentifier().getValue());
		//obx-3.5 细菌名称
		obxMap.put("nameBact", obx.getObservationIdentifier().getAlternateText().getValue());
		//obx-5 结果值
		obxMap.put("val", obx.getField(5,0).encode());
		//obx-6 单位
		obxMap.put("unit", obx.getUnits().getIdentifier().getValue());
		//obx-7 参考范围
		obxMap.put("references", obx.getReferencesRange().getValue());
		//obx-8 异常标志(结论)
		obxMap.put("euResult", obx.getAbnormalFlags().getValue());
		//obx-11 结果状态
		obxMap.put("sign", obx.getObservationResultStatus().getValue());
		//obx-13 菌落计数
		obxMap.put("bactcol", obx.getUserDefinedAccessChecks().getValue());
		//obx-14  检验时间
		obxMap.put("dateOcc", obx.getField(14, 0).encode());
		return obxMap;
	}
	
}
