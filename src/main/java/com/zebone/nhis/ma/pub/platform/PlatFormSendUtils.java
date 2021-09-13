package com.zebone.nhis.ma.pub.platform;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.send.IPlatFormSendHandler;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 发送平台消息工具类
 * @author yangxue
 *
 */
@Service
@SuppressWarnings("unchecked")
public class PlatFormSendUtils {

	/***************以下为发送BD领域********************/

	/**
	 * 发送医疗组信息
	 * @param paramMap
	 */
	public static void sendOrgDeptWgMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendOrgDeptWgMsg");
	}
	/**
	 * 保存业务线消息
	 * @param paramMap
	 */
	public static void sendSaveDeptBuAndBusesMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendSaveDeptBuAndBusesMsg");
	}
	/**
	 * 诊疗服务维护（挂号级别信息字典）（深大项目）
	 * @param paramMap
	 */
	public static void sendRegLevelMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendRegLevelMsg");
	}

	/**
	 * 发送合同单位信息（医保信息）（深大项目）
	 * @param paramMap
	 */
	public static void sendPactMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPactMsg");
	}

	/**
	 * 发送公共字典信息
	 * @param paramMap{codeEmp,bdDefdoc:字典表集合【List<BdDefdoc>】含未变更内容,delPkDefdocs:删除的字典主键集合【List<String>】}
	 */
	public static void sendBdDefDocMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdDefDocMsg");
	}
	/**
	 * 发送诊断信息
	 * @param paramMap{codeEmp,diag(BdTermDiag),STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdTermDiagMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdTermDiagMsg");
	}

	/**
	 * 发送频次信息
	 * @param paramMap{codeEmp,freq(BdTermFreq),STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdTermFreqMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdTermFreqMsg");
	}
	/**
	 * 发送用法信息
	 * @param paramMap{codeEmp,supply(BdSupply),STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdSupplyMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdSupplyMsg");
	}
	/**
	 * 发送床位信息
	 * @param paramMap{codeEmp,delBed(含BdResbed字段的Map)-删除,resbedList【List<BdResBed>】-新增及更新}
	 */
	public static void sendBdResBedMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdResBedMsg");
	}

	/**
	 * 发送医嘱信息{codeEmp,bdOrd(BdOrd),STATUS:_ADD,_UPDATE,_DELETE,pkOrd--删除时使用}
	 * @param paramMap
	 */
	public static void sendBdOrdMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdOrdMsg");
	}

	/**
	 * 发送收费项目信息
	 * @param paramMap{codeEmp,item(BdItem),STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdItemMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdItemMsg");
	}
	/**
	 * 发送收费组套信息paramMap{codeEmp,itemSets【List<BdItemSet(map)>】,STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdItemSetMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdItemSetMsg");
	}
	/**
	 * 发送科室信息
	 * @param paramMap{codeEmp,dept【BdOuDept】,STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdOuDeptMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdOuDeptMsg");
	}

	/**
	 * 发送科室和病区关系
	 *
	 * @param paramMap
	 */
	public static void sendRelationshipMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendRelationshipMsg");
	}

	/**
	 * 发送人员信息
	 * @param paramMap{codeEmp,emp【BdOuDept(map)】,empJobs【List<Map<String,Object>(BdOuEmpjob)>】STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdOuEmpMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdOuEmpMsg");
	}

	/**
	 * 发送用户信息
	 * @param paramMap{codeEmp,user【BdOuUser(map)】,STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdOuUserMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdOuUserMsg");
	}
	/**
	 * 发送药品信息paramMap{pd:BdPd对象,STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdPdMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdPdMsg");
	}

	/**
	 * 发送病历信息
	 * @param paramMap{codeIp,codePv,codePi,IpTimes}
	 */
	public static void sendEmrMsg(Map<String, Object> paramMap){
		execute(paramMap,"sendEmrMsg");
	}


	/**
	 * 发送生产厂家信息paramMap{factory:BdFactory对象,STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdFactoryMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdFactoryMsg");
	}

	/**
	 * 发送机构信息
	 * @param paramMap{codeEmp,dept【BdOuDept】,STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public static void sendBdOuOrgMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdOuOrgMsg");
	}

	/***************以下为发送ADT领域*******************/


	/**
	 * 发送换床信息
	 * @param paramMap{pkPv,pkPvDes,codeEmp}
	 */
	public static void sendBedChangeMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBedChange");
	}
	/**
	 * 发送包床信息
	 * @param paramMap{codeEmp,pk_pv,bednos【List<String>】}
	 */
	public static void sendBedPackMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBedPackMsg");
	}
	/**
	 * 发送退包床信息
	 * @param paramMap{codeEmp,pk_pv,bednos【List<String>】}
	 */
	public static void sendBedRtnPackMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBedRtnPackMsg");
	}
	/**
	 * 发送入科信息（含转入接收，新生儿接收）
	 * @param paramMap{pkPv,pkEmp,codeEmp,nameEmp,adtType:入院,新出生，转科}
	 */
	public static void sendDeptInMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendDeptInMsg");
	}
	/**
	 * 发送转科信息
	 * @param paramMap{pkPv,pkEmp,codeEmp,nameEmp}
	 */
	public static void sendDeptChangeMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendDeptChangeMsg");
	}
	/**
	 * 发送取消入科信息
	 * @param paramMap{pkPv,pkDeptNs,pkEmp,codeEmp,nameEmp,flagInf:1表示新生儿撤销登记}
	 */
	public static void sendCancelDeptInMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCancelDeptInMsg");
	}
	/**
	 * 发送主治医师变更信息
	 * @param paramMap{pkPv,pkEmp,codeEmp,nameEmp,pkNewDoc:新医生主键,nameNewDoc：新医生姓名}
	 */
	public static void sendDoctorChangeMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendDoctorChangeMsg");
	}
	/**
	 * 发送患者基本信息
	 * @param paramMap{codeEmp,pi【PiMaster(map)】}
	 */
	public static void sendPiMasterMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPiMasterMsg");
	}
	/**
	 * ADT^A08
	 * 发送患者就诊信息
	 * @param paramMap
	 */
	public static void sendPvInfoMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPvInfoMsg");
	}
	/**
	 * 发送入院信息 ADT^A01
	 * @param paramMap{AdtRegParam-所有属性，codeEmp}
	 */
	public static void sendPvInMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPvInMsg");
	}
	/**
	 * 发送取消入院信息
	 * @param paramMap{AdtOutParam-所有属性,codeEmp}
	 */
	public static void sendPvCancelInMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPvCancelInMsg");
	}

	/**
	 * 发送患者出院信息
	 * @param paramMap{AdtOutParam-所有属性,codeEmp}
	 */
	public static void sendPvOutMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPvOutMsg");
	}
	/**
	 * 发送取消出院信息
	 * @param paramMap{AdtOutParam-所有属性,codeEmp}
	 */
	public static void sendPvCancelOutMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPvCancelOutMsg");
	}
	/**
	 * 发送门诊挂号信息{codeEmp,nameEmp,pkPv}
	 * @param paramMap
	 */
	public static void sendPvOpRegMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPvOpRegMsg");
	}
	/**
	 * 发送门诊退号信息
	 * @param paramMap{PvEncounter--属性,codeEmp}
	 */
	public static void sendPvOpCancelRegMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPvOpCancelRegMsg");
	}

	/**
	 * 发送门诊预约信息
	 * @param paramMap
	 */
	public static void sendSchAppt(Map<String,Object> paramMap){
		execute(paramMap,"sendSchAppt");
	}

	/**
	 * 发送门诊预约登记信息
	 * @param paramMap
	 */
	public static void sendSchApptReg(Map<String,Object> paramMap){
		execute(paramMap,"sendSchApptReg");
	}
	/**
	 * 发送号源排班信息
	 *
	 */
	public static void sendPvOpNoMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendPvOpNoMsg");
	}




	/**
	 * 发送门诊到诊消息   ADT^A08
	 *
	 */
	public static void sendOpArriveMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendOpArriveMsg");
	}


	/**
	 * 发送门诊转住院消息   ADT^A06
	 *
	 */
	public static void sendOpToIpMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendOpToIpMsg");
	}

	/**
	 * 发送门诊诊毕   ADT^A03
	 *
	 */
	public static void sendFinishClinicMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendFinishClinicMsg");
	}


	/**
	 * 发送门诊取消到诊   ADT^A32
	 *
	 */
	public static void sendCancelClinicMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCancelClinicMsg");
	}

	/**
	 * 发送门诊取消到诊   ADT^A08
	 *
	 */
	public static void sendUpPiInfoMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendUpPiInfoMsg");
	}


	/**
	 * 发送门诊医嘱消息
	 */
	public static void sendOpO09Msg(Map<String,Object> paramMap){
		execute(paramMap,"sendOpO09Msg");
	}

	/***************以下为发送BL领域*******************/

	/**
	 * 发送给微信--住院相关的缴费信息(深大)
	 * @param paramMap{codePv}
	 */
	public static void  sendBlWeiXinSQMZQ1Msg(Map<String,Object> paramMap){
		execute(paramMap,"sendBlWeiXinSQMZQ1Msg");
	}

	/**
	 * 发送给微信--住院费每日账单详情(深大)
	 * @param paramMap{codePv}
	 */
	public static void sendBlWeiXinQBPZZLMsgDetails(Map<String,Object> paramMap){
		execute(paramMap,"sendBlWeiXinQBPZZLMsgDetails");
	}

	/**
	 * 发送给微信--住院费每日账单列表(深大)
	 * @param paramMap{codePv}
	 */
	public static void sendBlWeiXinQBPZZLMsgTheme(Map<String,Object> paramMap){
		execute(paramMap,"sendBlWeiXinQBPZZLMsgTheme");
	}



	/**
	 * 发送住院取消结算信息
	 * @param paramMap{codeEmp,nameEmp,pkSettle,pkPv}
	 */
	public static void  sendBlCancelSettleMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBlCancelSettleMsg");
	}
	/**
	 * 发送住院结算信息
	 * @param paramMap{"codeEmp":"操作员编码","nameEmp":"操作员名称","pkPi":"患者编码","pkPv":"就诊编码","totalAmount":"结算总金额","selfAmount":"自费金额"}
	 */
	public static void sendBlSettleMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBlSettleMsg");
	}
	/**
	 * 发送门诊结算信息
	 * @param paramMap(OpCgTransforVo-所有属性)
	 */
	public static void sendBlOpSettleMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBlOpSettleMsg");
	}

	/**
	 * 发送急的检验检查信息
	 * @param paramMap
	 */
	public static void sendOpEmeOrdMag(Map<String,Object> paramMap){
		execute(paramMap,"sendOpEmeOrdMag");
	}
	/**
	 * 发送门诊取消结算信息()
	 * @param paramMap
	 */
	public static void sendBlOpCancelSettleMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBlOpCancelSettleMsg");
	}
	/**
	 * 发送检查检验记费信息(含门诊、住院)
	 * @param paramMap{dtlist:List<MedExeIpParam>,type:I,O--住院、门诊}
	 */
	public static void sendBlMedApplyMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBlMedApplyMsg");
	}
	/**
	 * 发送临床诊断信息
	 * @param paramMap{pkPv,pvDiagList}
	 */
	public static void sendCnDiagMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCnDiagMsg");
	}
	/**
	 * 发送临床医技申请单信息
	 * @param paramMap{pkPv,type:Lis/Ris,lis:【List<CnLisApply>】ris:【List<CnRisApply>】}
	 */
	public static void sendCnMedApplyMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCnMedApplyMsg");
	}
	/**
	 * 发送门诊处方信息
	 * @param paramMap{cnPres:List<CnPrescription>,cnOrder:List<OpCgCnOrder>,cnOrderCharge:List<BlOpDt>,type:处方状态}
	 */
	public static void sendCnPresOpMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCnPresOpMsg");
	}
	/**
	 * 发送住院预交金信息 （深大项目）
	 * @param paramMap{}
	 */
	public static void  sendDepositMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendDepositMsg");
	}

	/**
	 * 发票（住院） （深大项目）
	 * 重打发票发送，发票消息放在收费结算里了
	 * @param paramMap{}
	 */
	public static void  sendReceiptMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendReceiptMsg");
	}


	/**
	 * 发送门诊检查检验消息
	 * @param paramMap
	 */
	public static void sendCnOpAppMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCnOpAppMsg");
	}

	/**
    * 催缴预交金(深大项目)
    * @param paramMap
    */
	public static void  sendCallPayMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCallPayMsg");
	}

	/**
	 * 发送住院床位费用
	 * @param paramMap
	 */
	public static void sendBedcgMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBedCgMsg");
	}

	/**
	 * 发送费用短信提醒信息
	 * zsrm-门诊欠费短信提醒
	 *
	 * @param paramMap
	 */
	public static void sendOpFeeReminderMsg(Map<String, Object> paramMap) {
		execute(paramMap, "sendOpFeeReminderMsg");
	}

	/**
	 * 向微信推送当天未缴费信息模板内容
	 */
	public static void sendWeiXinForNotPayCost(Map<String, Object> paramMap) {
		execute(paramMap, "sendWeiXinForNotPayCost");
	}

	/**************************以下为发送EX领域*******************************/

	/**
	 * 医嘱执行确认时发送消息
	 *
	 * @param paramMap{exlist:List<Map<String,Object>>(ExlistPubVo),typeStatus:ADD--zb集成平台需要}
	 */
	public static void sendExConfirmMsg(Map<String, Object> paramMap) {
		execute(paramMap, "sendExConfirmMsg");
	}

	/**
	 * 医嘱取消执行发送消息
	 * @param paramMap{exlist:List<Map<String,Object>>(ExlistPubVo),typeStatus:ADD--zb集成平台需要}
	 */
	public static void sendCancelExMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCancelExMsg");
	}
	/**
	 * 发送医嘱新增，更新信息服务 （发送OMP^O09消息）
	 * @param paramMap{ordlist:List<Map<String,Object>>(OrderCheckVo)}
	 */
	public static void sendExOrderCheckMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendExOrderCheckMsg");
	}
	/**
	 * 发送诊疗排班信息
	 * @param paramMap
	 */
	public static void sendSchInfo(Map<String,Object> paramMap){
		execute(paramMap,"sendSchInfo");
	}
	/**
	 * 获取临床数据（病历临床数据提取时使用）
	 * @param
	 */
	public  List<Map<String,Object>> getEMRClinicalData(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//List<Map<String,Object>>  listMap=new ArrayList<Map<String,Object>>();
		//获取自定义临床数据提取服务service，对应配置文件msg.properties配置文件
		if ("1".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0"))) {
			//获取自定义记费服务service，对应配置文件msg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("msg.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				IPlatFormSendHandler handler = (IPlatFormSendHandler) bean;
				return handler.getEMRClinicalData(paramMap);
			}
		}
		return null;
	}
	/**
	 * 发送住院发药信息
	 * @param paramMap{deDruglist：List<ExPdDe> 发药明细,exPdAppDetails:List<ExPdApplyDetail>请领明细}
	 */
	public static void sendScmIpDeDrug(Map<String,Object> paramMap){
		execute(paramMap,"sendScmIpDeDrug");
	}





	/**
	 * 门诊推送叫号信息
	 * @param paramMap
	 */
	public static String sendCnOpCall(Map<String,Object> paramMap){
		execute(paramMap,"sendCnOpCall");
		return null;
	}
	/**
	 * 会诊申请消息（深大项目）
	 * @param paramMap
	 */
	public static void sendConsultMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendConsultMsg");
	}
	/**
	 * 会诊应答消息（深大项目）
	 * @param paramMap
	 */
	public static void sendConsultResponeMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendConsultResponeMsg");
	}

	/**
	 * 发送手术申请信息（深大项目）
	 * @param paramMap
	 */
	public static void sendOpApplyMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendOpApplyMsg");
	}


	/**
	 * 发送手术确认息（深大项目）
	 * @param paramMap
	 */
	public static void sendOpConfirmMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendOpConfirmMsg");
	}

	/**
	 * 发送门诊诊毕消息（深大项目）
	 * @param paramMap
	 */
	public static void sendOpCompleteMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendOpCompleteMsg");
	}



	/**
	 * 发送查询住院信息列表（深大项目）
	 * @param paramMap
	 */
	public static void sendQueryIpMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendQueryIpMsg");
	}

	/**
	 * 发送查询患者信息(住院)（深大项目）
	 * @param paramMap
	 */
	public static void sendQueryPiMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendQueryPiMsg");
	}

	/**
	 * 发送查询费用类别统计(TotalExpenses)（深大项目）
	 * @param paramMap
	 */
		//获取是否启用发送消息服务, 对应配置文件msg.properties配置文件
	public static void sendTotalExpensesMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendTotalExpensesMsg");
	}

	/**
	 * 押金缴入(住院) 接收 DFT^P03  WeChat（深大项目）（深大项目）
	 * @param paramMap
	 */
	public static void sendFeedbackDepositMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendFeedbackDepositMsg");
	}

	/**
	 * 查询押金缴入状态 接收 QBP^ZYJ WeChat（深大项目）
	 * @param paramMap
	 */
	public static void sendDepositStatusMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendDepositStatusMsg");
	}

	/**
	 * 发送新增执行单信息
	 * @param paramMap
	 */
	public static void sendAddExOrderOccMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendAddExOrderOccMsg");
	}
	/**
	 * 发送删除执行单信息
	 * @param paramMap
	 */
	public static void sendDelExOrderOccMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendDelExOrderOccMsg");
	}
	/**
	 * 发送更新执行单信息
	 * @param paramMap
	 */
	public static void sendUpDateExOrderOccMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendUpDateExOrderOccMsg");
	}
	/**
	 * 发送全类型执行单信息{
	 * addList:List<ExOrderOcc>新增执行单集合;
	 * deleteList:List<ExOrderOcc>删除执行单集合;
	 * updateList:List<ExOrderOcc>更新执行单集合
	 * }
	 * @param paramMap
	 */
	public static void sendAllExOrderOccMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendAllExOrderOccMsg");
	}
	/**
	 * 处方签署CnIpPresService.signPres
	 * @param paramMap
	 */
	public static void sendSignPresMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendSignPresMsg");
	}

	/**
	 * 处方作废CnIpPresService.cancelSignPres
	 * @param paramMap
	 */
	public static void sendCancelSignPresMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCancelSignPresMsg");
	}
	/**
	 * 作废草药处方 CnIpPresService.cancelHerbOrder
	 * @param paramMap
	 */
	public static void sendCancelHerbOrderMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCancelHerbOrderMsg");
	}
	/**
	 * 取消签署草药处方 CnIpPresService.updateSign
	 * @param paramMap
	 */
	public static void sendUpdateSignMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendUpdateSignMsg");
	}
	/**
	 * 签署草药处方 CnIpPresService.signHerbOrder2
	 * @param paramMap
	 */
	public static void sendSignHerbOrder2Msg(Map<String,Object> paramMap){
		execute(paramMap,"sendSignHerbOrder2Msg");
	}
	/**
	 * 检查作废 CnReqService.cancleRisApplyList
	 * @param paramMap
	 */
	public static void sendCancleRisApplyListMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCancleRisApplyListMsg");
	}
	/**
	 * 医生站--检验作废 CnReqService.cancleLisApplyList
	 * @param paramMap
	 */
	public static void sendCancleLisApplyListMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendCancleLisApplyListMsg");
	}


	/**
	 * 手麻系统--手术医嘱作废-消息触发
	 * @param paramMap
	 */
	public static void sendOperaOrderCancelMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendOperaOrderCancelMsg");
	}

	/**
	 * 高值耗材计费上传平台
	 * @param paramMap
	 */
	public static void sendHighValueConSumIp(Map<String,Object> paramMap){
		execute(paramMap,"sendHighValueConSumIp");
	}
	/**
	 * 高值耗材退费上传平台
	 * @param paramMap
	 */
	public static void sendHighValueConSumIpBack(Map<String,Object> paramMap){
		execute(paramMap,"sendHighValueConSumIpBack");
	}

	/***
	 * 传入参数和目标方法名，通过配置执行具体平台消息处理器
	 * @param paramMap
	 * @param methodName
	 */
	public static void execute(Map<String,Object> paramMap, String methodName){
		ServiceLocator.getInstance().getBean(PlatFormSendProcessor.class)
				.execute(paramMap, UserContext.getUser(), methodName);
	}

	public static void sendBdMaterMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdMaterMsg");
	}
	
	/**
	 * 发送门诊诊室信息
	 * @param paramMap
	 */
	public static void sendBdDeptUnitMsg(Map<String,Object> paramMap){
		execute(paramMap,"sendBdDeptUnitMsg");
	}


	/**
	 * 发送手机验证码信息
	 * @param paramMap
	 */
	public static void sendPhoneCheck(Map<String,Object> paramMap){execute(paramMap,"sendShortMsgPhoneChk");}

	/**
	 * 推送发放卡片接口
	 * @param paramMap
	 */
	public static void sendDstributeCardMsg(Map<String,Object> paramMap){execute(paramMap,"sendDstributeCardMsg");}

	/**
	 * 发送入院评估
	 * @param paramMap
	 */
	public static void sendAdmissionAssessment(Map<String,Object> paramMap){
		execute(paramMap,"sendAdmissionAssessment");
	}


	/**
	 * 发送诊间预约
	 * @param paramMap
	 */
	public static void sendReserveOutpatient(Map<String,Object> paramMap){
		execute(paramMap,"sendReserveOutpatient");
	}

	/**
	 * 发送护理记录
	 * @param paramMap
	 */
	public static void sendNursingRecordSheet(Map<String,Object> paramMap){
		execute(paramMap,"sendNursingRecordSheet");
	}

	/**
	 * 发送生命体征
	 * @param paramMap
	 */
	public static void sendVitalSigns(Map<String,Object> paramMap){
		execute(paramMap,"sendVitalSigns");
	}
}
