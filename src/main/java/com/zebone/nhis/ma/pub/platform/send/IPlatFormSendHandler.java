package com.zebone.nhis.ma.pub.platform.send;

import ca.uhn.hl7v2.HL7Exception;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 发送平台消息接口（所有业务）,接口实现类需要为每个业务独立添加事物 * @author yangxue
 *
 */
public interface IPlatFormSendHandler {
    /********************以下为BD领域*******************/
	/**
	 * 保存业务线
	 */
	void sendSaveDeptBuAndBusesMsg(Map<String,Object> paramMap);

	/**
	 * 诊疗服务维护（挂号级别信息字典）（深大项目）
	 * @param paramMap
	 */
	public void sendRegLevelMsg(Map<String,Object> paramMap);

	/**
	 * 发送合同单位信息（医保信息）（深大项目）
	 * @param paramMap
	 */
	public void sendPactMsg(Map<String,Object> paramMap);

	/**
	 * 发送换床信息
	 * @param paramMap
	 * @
	 */
	public void sendBedChange(Map<String,Object> paramMap);
	/**
	 * 发送公共字典信息
	 * @param paramMap{codeEmp,bdDefdoc:字典表集合【List<BdDefdoc>】含未变更内容,delPkDefdocs:删除的字典主键集合【List<String>】}
	 */
	public void sendBdDefDocMsg(Map<String,Object> paramMap);
	/**
	 * 发送诊断信息
	 * @param paramMap{codeEmp,diag(BdTermDiag),STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public void sendBdTermDiagMsg(Map<String,Object> paramMap);

	/**
	 * 发送频次信息
	 * @param paramMap{codeEmp,freq(BdTermFreq),STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public void sendBdTermFreqMsg(Map<String,Object> paramMap);
	/**
	 * 发送用法信息
	 * @param paramMap{codeEmp,supply(BdSupply),STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public void sendBdSupplyMsg(Map<String,Object> paramMap);
	/**
	 * 发送床位信息
	 * @param paramMap{codeEmp,delBed(含BdResbed字段的Map)-删除,resbedList【List<BdResBed>】-新增及更新}
	 */
	public void sendBdResBedMsg(Map<String,Object> paramMap);

	/**
	 * 发送医嘱信息{codeEmp,bdOrd(BdOrd),STATUS:_ADD,_UPDATE,_DELETE,pkOrd--删除时使用}
	 * @param paramMap
	 */
	public void sendBdOrdMsg(Map<String,Object> paramMap) throws IllegalAccessException, IOException;

	/**
	 * 发送收费项目信息
	 * @param paramMap{codeEmp,item(BdItem),STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public void sendBdItemMsg(Map<String,Object> paramMap);
	/**
	 * 发送收费组套信息paramMap{codeEmp,itemSets【List<BdItemSet(map)>】,STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public void sendBdItemSetMsg(Map<String,Object> paramMap);
	/**
	 * 发送科室信息
	 * @param paramMap{codeEmp,dept【BdOuDept】,STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public void sendBdOuDeptMsg(Map<String,Object> paramMap);
	/**
	 * 发送人员信息
	 * @param paramMap{codeEmp,emp【BdOuDept(map)】,empJobs【List<Map<String,Object>(BdOuEmpjob)>】STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public void sendBdOuEmpMsg(Map<String,Object> paramMap);

	/**
	 * 发送用户信息
	 * @param paramMap{codeEmp,user【BdOuUser(map)】,STATUS:_ADD,_UPDATE,_DELETE}
	 */
	public void sendBdOuUserMsg(Map<String,Object> paramMap);
	/**
	 * 发送药品信息
	 * @param paramMap{pd:BdPd对象,STATUS:_ADD,_UPDATE,_DELETE}
	 * @
	 */
	public void sendBdPdMsg(Map<String,Object> paramMap);



	/**
	 * 发送生产厂家信息
	 * @param paramMap{factory:BdFactory对象,STATUS:_ADD,_UPDATE,_DELETE}
	 * @
	 */
	public void sendBdFactoryMsg(Map<String,Object> paramMap);

	/**
	 * 发送机构信息
	 * @param paramMap
	 */
	public void sendBdOuOrgMsg(Map<String,Object> paramMap);
	/***************以下为发送ADT领域*******************/


	/**
	 * 发送换床信息
	 * @param paramMap{pkPv,pkPvDes,codeEmp}
	 */
	public void sendBedChangeMsg(Map<String,Object> paramMap);
	/**
	 * 发送包床信息
	 * @param paramMap{codeEmp,pk_pv,bednos【List<String>】}
	 */
	public void sendBedPackMsg(Map<String,Object> paramMap);
	/**
	 * 发送退包床信息
	 * @param paramMap{codeEmp,pk_pv,bednos【List<String>】}
	 */
	public void sendBedRtnPackMsg(Map<String,Object> paramMap);
	/**
	 * 发送入科信息（含转入接收，新生儿接收）
	 * @param paramMap{pkPv,pkEmp,codeEmp,nameEmp,adtType:入院,新出生，转科}
	 */
	public void sendDeptInMsg(Map<String,Object> paramMap);
	/**
	 * 发送转科信息
	 * @param paramMap{pkPv,pkEmp,codeEmp,nameEmp}
	 */
	public void sendDeptChangeMsg(Map<String,Object> paramMap);

	/**
	 * 取消转科
	 * @param paramMap
	 */
	default void sendCancelDeptChangeMsg(Map<String,Object> paramMap){};
	/**
	 * 发送取消入科信息
	 * @param paramMap{pkPv,pkDeptNs,pkEmp,codeEmp,nameEmp,flagInf:1表示新生儿撤销登记}
	 */
	public void sendCancelDeptInMsg(Map<String,Object> paramMap);
	/**
	 * 发送主治医师变更信息
	 * @param paramMap{pkPv,pkEmp,codeEmp,nameEmp,pkNewDoc:新医生主键,nameNewDoc：新医生姓名}
	 */
	public void sendDoctorChangeMsg(Map<String,Object> paramMap);
	/**
	 * 发送患者基本信息
	 * @param paramMap{codeEmp,pi【PiMaster(map)】}
	 */
	public void sendPiMasterMsg(Map<String,Object> paramMap);
	/**
	 * 发送患者就诊信息
	 * @param paramMap
	 * @throws Exception
	 */
	public void sendPvInfoMsg(Map<String,Object> paramMap);
	/**
	 * 发送入院信息
	 * @param paramMap{AdtRegParam-所有属性，codeEmp}
	 */
	public void sendPvInMsg(Map<String,Object> paramMap);
	/**
	 * 发送取消入院信息
	 * @param paramMap{AdtOutParam-所有属性,codeEmp}
	 */
	public void sendPvCancelInMsg(Map<String,Object> paramMap);

	/**
	 * 发送患者出院信息
	 * @param paramMap{AdtOutParam-所有属性,codeEmp}
	 */
	public void sendPvOutMsg(Map<String,Object> paramMap);
	/**
	 * 发送取消出院信息
	 * @param paramMap{AdtOutParam-所有属性,codeEmp}
	 */
	public void sendPvCancelOutMsg(Map<String,Object> paramMap);
	/**
	 * 发送门诊挂号信息{codeEmp,nameEmp,pkPv}
	 * @param paramMap
	 */
	public void sendPvOpRegMsg(Map<String,Object> paramMap);
	/**
	 * 发送门诊退号信息
	 * @param paramMap{PvEncounter--属性,codeEmp}
	 */
	public void sendPvOpCancelRegMsg(Map<String,Object> paramMap);

	/**
	 * 发送门诊到诊消息   ADT^A08
	 * @param paramMap
	 */
	public void sendOpArriveMsg(Map<String,Object> paramMap);

	/**
	 * 发送门诊转住院消息   ADT^A06
	 * @param paramMap
	 */
	public void sendOpToIpMsg(Map<String,Object> paramMap);


	/**
	 * 发送门诊诊毕消息   ADT^A03
	 * @param paramMap
	 */
	public void sendFinishClinicMsg(Map<String,Object> paramMap);

	/**
	 * 发送门诊取消到诊消息   ADT^A32
	 * @param paramMap
	 */
	public void sendCancelClinicMsg(Map<String,Object> paramMap);


	/**
	 * 发送门诊患者信息修改    ADT^A08
	 * @param paramMap
	 */
	public void sendUpPiInfoMsg(Map<String,Object> paramMap);

	/**
	 * 发送门诊医嘱消息    OMP^O09
	 * @param paramMap
	 */
	public void sendOpO09Msg(Map<String,Object> paramMap);



	/***************以下为发送BL领域*******************/

	/**
    * 发送给微信--住院相关的缴费信息
    * @param paramMap
    */

   public void sendBlWeiXinSQMZQ1Msg(Map<String,Object> paramMap);

   /**
    * 发送给微信--住院费每日账单详情
    * @param paramMap
    */
   public void sendBlWeiXinQBPZZLMsgDetails(Map<String,Object> paramMap);

   /**
    * 发送给微信--住院费每日账单列表
    * @param paramMap
    * @throws HL7Exception
    */
   public void sendBlWeiXinQBPZZLMsgTheme(Map<String,Object> paramMap);
	/**
	 * 发送住院取消结算信息
	 * @param paramMap{codeEmp,nameEmp,pkSettle,pkPv}
	 */
	public void  sendBlCancelSettleMsg(Map<String,Object> paramMap);
	/**
	 * 发送住院结算信息
	 * @param paramMap{"codeEmp":"操作员编码","nameEmp":"操作员名称","pkPi":"患者编码","pkPv":"就诊编码","totalAmount":"结算总金额","selfAmount":"自费金额"}
	 */
	public void sendBlSettleMsg(Map<String,Object> paramMap);
	/**
	 * 发送门诊结算信息
	 * @param paramMap(OpCgTransforVo-所有属性)
	 */
	public void sendBlOpSettleMsg(Map<String,Object> paramMap);

	/**
	 * 发送急的检验检查信息
	 * @param paramMap
	 */
	public void sendOpEmeOrdMag(Map<String,Object> paramMap);
	/**
	 * 发送门诊取消结算信息()
	 * @param paramMap
	 */
	public void sendBlOpCancelSettleMsg(Map<String,Object> paramMap);
	/**
	 * 发送检查检验记费信息(含门诊、住院)
	 * @param paramMap{dtlist:List<MedExeIpParam>,type:I,O--住院、门诊}
	 */
	public void sendBlMedApplyMsg(Map<String,Object> paramMap);
	/**
	 * 发送临床诊断信息
	 * @param paramMap{pkPv,pvDiagList}
	 */
	public void sendCnDiagMsg(Map<String,Object> paramMap);
	/**
	 * 发送临床医技申请单信息
	 * @param paramMap{pkPv,type:Lis/Ris,lis:【List<CnLisApply>】ris:【List<CnRisApply>】}
	 */
	public void sendCnMedApplyMsg(Map<String,Object> paramMap);


	/**
	 * 发送门诊检查检验消息
	 * @param paramMap
	 */
	public void sendCnOpAppMsg(Map<String,Object> paramMap);

	/**
	 * 发送门诊处方信息
	 * @param paramMap{cnPres:List<CnPrescription>,cnOrder:List<OpCgCnOrder>,cnOrderCharge:List<BlOpDt>,type:处方状态}
	 */
	public void sendCnPresOpMsg(Map<String,Object> paramMap);
	/**
	 * 发送住院预交金信息（深大项目）
	 * @param paramMap
	 */
	public void sendDepositMsg(Map<String,Object> paramMap);
	/**
	 * 发票（住院） （深大项目）
	 * @param paramMap
	 */
	public void sendReceiptMsg(Map<String,Object> paramMap);

	/**
	 * 发送住院催缴预交金信息（深大项目）
	 * @param paramMap
	 */
	public void sendCallPayMsg(Map<String,Object> paramMap);

	/**
	 * 发送住院床位费用信息
	 * @param paramMap
	 */
	public void sendBedCgMsg(Map<String,Object> paramMap);

	/**
	 * 发送费用短信提醒信息
	 * zsrm-门诊欠费短信提醒
	 * @param paramMap
	 */
	public void sendOpFeeReminderMsg(Map<String,Object> paramMap);
	/**************************以下为发送EX领域*******************************/
	/**
	 * 医嘱执行确认时发送消息
	 * @param paramMap{exlist:List<Map<String,Object>>(ExlistPubVo),typeStatus:ADD--zb集成平台需要}
	 */
	public void sendExConfirmMsg(Map<String,Object> paramMap);
	/**
	 * 发送医嘱核对信息
	 * @param paramMap{ordlist:List<Map<String,Object>>(OrderCheckVo)}
	 */
	public void sendExOrderCheckMsg(Map<String,Object> paramMap);

	/**
	 * 发送住院检查检验信息
	 * @param paramMap{ordlist:List<Map<String,Object>>(OrderCheckVo)}
	 */
	public void sendMsgLisRis(Map<String,Object> paramMap);
	/**
	 * 手术申请信息（深大项目）
	 * @param paramMap
	 */
	public void sendOpApplyMsg(Map<String,Object> paramMap);
	/**
	 * 会诊申请消息（深大项目）
	 * @param paramMap
	 */
	public void sendConsultMsg(Map<String,Object> paramMap);
	/**
	 * 会诊应答消息（深大项目）
	 * @param paramMap
	 */
	public void sendConsultResponeMsg(Map<String,Object> paramMap);

	/*******************************************发送SCH领域的消息**********************/

	/**
	 * 发送诊疗排班信息
	 * @param paramMap
	 */
	public void sendSchInfo(Map<String,Object> paramMap);

	/**
	 * 门诊预约状态信息发送服务
	 * @param
	 */
	public void sendSchAppt(Map<String,Object> paramMap);

	/**
	 * 门诊预约 登记
	 * @param
	 */
	void sendSchApptReg(Map<String, Object> paramMap);
	/**
	 * 发送住院发药信息
	 * @param paramMap{deDruglist：List<ExPdDe> 发药明细,exPdAppDetails:List<ExPdApplyDetail>请领明细}
	 */
	public void sendScmIpDeDrug(Map<String,Object> paramMap);

	/**
	 * 发送号源排班信息
	 * @param paramMap
	 */
	public void sendPvOpNoMsg(Map<String,Object> paramMap);

	/*****************************************发送EMR领域的消息**************************/
	/**
	 * 获取临床数据信息（病历临床数据提取使用）
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getEMRClinicalData(Map<String,Object> paramMap);

	/**
	 * 门诊推送叫号信息
	 * @param paramMap
	 */
	public String sendCnOpCall(Map<String,Object> paramMap);

	/**
	 * 发送病历信息
	 * @param paramMap{codeIp,codePv,codePi,IpTimes}
	 */
	public void sendEmrMsg(Map<String,Object> paramMap);

	/**
	 * 手术医嘱确认
	 * @param paramMap
	 */
	public void sendOpConfirmMsg(Map<String, Object> paramMap);


	/*****************************************发送门诊类消息****************************/
	/**
	 * 发送门诊诊毕消息（深大项目）
	 * @param paramMap
	 */
	public void  sendOpCompleteMsg(Map<String, Object> paramMap);

	/**************************微信模块*****************************************/
	/**
	 * 查询住院信息列表(Theme)
	 * @param paramMap
	 */
	public void sendQueryIpMsg(Map<String, Object> paramMap);

	/**
	 * 查询患者信息(住院)
	 * @param paramMap
	 */
	public void sendQueryPiMsg(Map<String, Object> paramMap);

	/**
	 * 查询费用类别统计(TotalExpenses)
	 * @param paramMap
	 */
	public void sendTotalExpensesMsg(Map<String, Object> paramMap);

	/**
	 * 押金缴入(住院) 接收 DFT^P03  WeChat（深大项目）（深大项目）
	 * @param paramMap
	 */
	public void sendFeedbackDepositMsg(Map<String, Object> paramMap);

	/**
	 * 查询押金缴入状态 接收 QBP^ZYJ WeChat（深大项目）
	 * @param paramMap
	 */
	public void sendDepositStatusMsg(Map<String,Object> paramMap);


	/**
	 * 发送新增执行单信息
	 * @param paramMap
	 */
	public void sendAddExOrderOccMsg(Map<String,Object> paramMap);
	/**
	 * 发送删除执行单信息
	 * @param paramMap
	 */
	public void sendDelExOrderOccMsg(Map<String,Object> paramMap);
	/**
	 * 发送修改执行单信息
	 * @param paramMap
	 */
	public void sendUpDateExOrderOccMsg(Map<String,Object> paramMap);
	/**
	 * 发送全类型执行单信息{
	 * addList:List<ExOrderOcc>新增执行单集合;
	 * deleteList:List<ExOrderOcc>删除执行单集合;
	 * updateList:List<ExOrderOcc>更新执行单集合
	 * }
	 * @param paramMap
	 */
	public void sendAllExOrderOccMsg(Map<String,Object> paramMap);

	/**
	 * 科室和病区关系
	 * @param paramMap
	 */
	public void sendRelationshipMsg(Map<String,Object> paramMap);

	/**
	 * 处方作废CnIpPresService.cancelSignPres
	 * @param paramMap
	 */
	public void sendCancelSignPresMsg(Map<String,Object> paramMap);
	/**
	 * 处方签署CnIpPresService.signPres
	 * @param paramMap
	 */
	public void sendSignPresMsg(Map<String,Object> paramMap);

	/**
	 * 作废草药处方 CnIpPresService.cancelHerbOrder
	 * @param paramMap
	 */
	public void sendCancelHerbOrderMsg(Map<String,Object> paramMap);
	/**
	 * 取消签署草药处方 CnIpPresService.updateSign
	 * @param paramMap
	 */
	public void sendUpdateSignMsg(Map<String,Object> paramMap);

	/**
	 * 签署草药处方(重写) :signHerbOrder2
	 * @param paramMap
	 */
	public void sendSignHerbOrder2Msg(Map<String,Object> paramMap);
	/**
	 * 检验作废
	 * @param paramMap
	 */
	public void sendCancleLisApplyListMsg(Map<String,Object> paramMap);
	/**
	 * 检查作废
	 * @param paramMap
	 */
	public void sendCancleRisApplyListMsg(Map<String,Object> paramMap);

	/**
	 * 手术医嘱作废
	 * @param paramMap
	 */
	public void sendOperaOrderCancelMsg(Map<String,Object> paramMap);

	/**
	 * 高值耗材计费
	 * @param paramMap
	 */
	public void sendHighValueConSumIp(Map<String,Object> paramMap);

	/**
	 * 高值耗材退费
	 * @param paramMap
	 */
	public void sendHighValueConSumIpBack(Map<String,Object> paramMap);


	/**
	 * 发送物资信息
	 * @param paramMap{pd:BdPd对象,STATUS:_ADD,_UPDATE,_DELETE}
	 * @
	 */
	void sendBdMaterMsg(Map<String,Object> paramMap);
	
	/**
	 * 发送门诊诊室信息
	 * @param 
	 * @
	 */
	void sendBdDeptUnitMsg(Map<String,Object> paramMap);
	/** 确认配药时，发送消息*/
	void sendConfirmDosage(Map<String,Object> paramMap);

	/**
	 * 发送手机验证码
	 * @param paramMap
	 */
	void sendShortMsgPhoneChk(Map<String,Object> paramMap);

	/**
	 * 推送发放卡片接口
	 * @param paramMap
	 */
	void sendDstributeCardMsg(Map<String,Object> paramMap);

	/**
	 * 发送入院评估
	 * @param paramMap
	 */
	void sendAdmissionAssessment(Map<String,Object> paramMap);

	void sendReserveOutpatient(Map<String,Object> paramMap) throws IllegalAccessException;

	/**
	 * 护理记录单
	 * @param paramMap
	 */
	void sendNursingRecordSheet(Map<String, Object> paramMap);

	/**
	 * 生命体征
	 *
	 * @param paramMap
	 */
	void sendVitalSigns(Map<String, Object> paramMap);

	/**
	 * 向微信推送当天未缴费信息模板内容
	 */
	void sendWeiXinForNotPayCost(Map<String, Object> paramMap);

	/**
	 * 发送医疗组信息
	 */
	void sendOrgDeptWgMsg(Map<String, Object> paramMap);

	/**
	 * 发送费用分类变更
	 * @param paramMap
	 */
	default void sendBdItemcateMsg(Map<String, Object> paramMap){};
}
