package com.zebone.nhis.webservice.zsrm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.transcode.SysLogInterface;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvPe;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.IOrdTypeCodeConst;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybSt;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybStDt;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybVisit;
import com.zebone.nhis.common.module.compay.ins.qgyb.OutParamHuaJia;
import com.zebone.nhis.ma.pub.platform.sd.service.ElectInvService;
import com.zebone.nhis.webservice.zsrm.dao.ZsrmSelfAppMapper;
import com.zebone.nhis.webservice.zsrm.vo.self.CommonReqSelfVo;
import com.zebone.nhis.webservice.zsrm.vo.self.CommonResSelfVo;
import com.zebone.nhis.webservice.zsrm.vo.self.ReqYbPreSettleInfovo;
import com.zebone.nhis.webservice.zsrm.vo.self.ReqYbPreSettleParamVo;
import com.zebone.nhis.webservice.zsrm.vo.self.RequestAppointNucVo;
import com.zebone.nhis.webservice.zsrm.vo.self.RequestQryOutpfeeDetailVo;
import com.zebone.nhis.webservice.zsrm.vo.self.RequestQryOutpfeeMasterVo;
import com.zebone.nhis.webservice.zsrm.vo.self.RequestSelfAppSettleVo;
import com.zebone.nhis.webservice.zsrm.vo.self.RequestSelfSettleDtVo;
import com.zebone.nhis.webservice.zsrm.vo.self.RequestYbSettleDtVo;
import com.zebone.nhis.webservice.zsrm.vo.self.RequestYbSettleVo;
import com.zebone.nhis.webservice.zsrm.vo.self.RequestYbWillSettleVo;
import com.zebone.nhis.webservice.zsrm.vo.self.ResponseAppointNucVo;
import com.zebone.nhis.webservice.zsrm.vo.self.ResponseQryNoSettleVo;
import com.zebone.nhis.webservice.zsrm.vo.self.ResponseQryOutpfeeDetailVo;
import com.zebone.nhis.webservice.zsrm.vo.self.ResponseQryOutpfeeMasterVo;
import com.zebone.nhis.webservice.zsrm.vo.self.ResponseSettleVo;
import com.zebone.nhis.webservice.zsrm.vo.self.ResponseYbWillSettleVo;
import com.zebone.nhis.webservice.zsrm.vo.self.ZsrmSelfAppConstant;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class ZsrmSelfAppService {

    private static Logger log = LoggerFactory.getLogger("nhis.ZsrmWeChatZzjLog");
    @Resource
    private ZsrmSelfAppMapper zsrmSelfAppMapper;
    @Resource
    private OpcgPubHelperService opcgPubHelperService;
    @Resource(name = "transactionManager")
    private PlatformTransactionManager platformTransactionManager;
    @Resource
    private ElectInvService electInvService;


    /**
     * 查询待缴费费用
     * @param param
     * @return
     */
    public String queryPatiCgInfoNotSettle(String param){
        RequestQryOutpfeeMasterVo reqvo = JsonUtil.readValue(param, RequestQryOutpfeeMasterVo.class);
        try {
            String codePi = reqvo.getCodePi();
            String codePv = reqvo.getCodePv();
            if (CommonUtils.isEmptyString(codePi)) {
                String resJson=errorJson("未传入[codePi]患者ID！");
                return resJson;
            }

            List<Map<String, Object>> resPvList = qryPipvInfo(codePi, codePv);
            if (resPvList == null || resPvList.size() == 0) {
                String resJson=errorJson("未查询到患者有效就诊记录！");
                return resJson;
            }
            User user=new User();
            user.setPkEmp(ZsrmSelfAppConstant.PK_EMP_CG);//--结算人
            user.setNameEmp(ZsrmSelfAppConstant.NAME_EMP);//结算人
            user.setPkOrg(ZsrmSelfAppConstant.PK_ORG);//机构
            user.setPkDept(ZsrmSelfAppConstant.PK_DEPT_CG);//结算科室
            UserContext.setUser(user);

            List<ResponseQryNoSettleVo> resNosettleList = new ArrayList<>();

            if (CommonUtils.isEmptyString(codePv)) {//返回多次就诊 待缴费，费用明细
                for (Map<String, Object> map : resPvList) {
                    List<ResponseQryNoSettleVo> tempSettles = qryPvCgList(map);
                    if (tempSettles != null) {
                        resNosettleList.addAll(tempSettles);
                    }
                }
            } else {//返回 特定就诊的费用明细 ：需判断是否是有效就诊
                List<ResponseQryNoSettleVo> tempSettles = qryPvCgList(resPvList.get(0));
                if (tempSettles != null) {
                    resNosettleList.addAll(tempSettles);
                }
            }


            String dateNow = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            CommonResSelfVo<List<ResponseQryNoSettleVo>> response = new CommonResSelfVo<List<ResponseQryNoSettleVo>>(
                    "1", "成功！", reqvo.getReqId(), dateNow, resNosettleList);
            String hisjson=JsonUtil.writeValueAsString(response);
            hisjson="{\"data\":"+JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data")) + "}";
            String resJson=successJson(hisjson);
            return resJson;
        }catch (Exception e){
            e.printStackTrace();
            String resJson=errorJson("HIS处理失败！");
            return resJson;
        }
    }

    /**
     * 结算服务(自费+医保)
     * @param param
     * @return
     */
    public String saveSettleForSelf(String param) {
        //修改为手动事物 , 关闭事务自动提交
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        TransactionSynchronizationManager.setActualTransactionActive(true);
        def.setName(this.getClass().getName()+NHISUUID.getKeyId());
        TransactionSynchronizationManager.setCurrentTransactionName(def.getName());
        String ybPkSettle = "";
        try {
            //1.参数解析关键数据校验工作（codePv,tradeNo,amountExt,dataList数据集合）
            RequestSelfAppSettleVo request = JsonUtil.readValue(param, RequestSelfAppSettleVo.class);
            String msg = checkReqSettleParam(request);
            if(StringUtils.isNotBlank(msg)){
                String resJson=errorJson(msg);
                return resJson;
            }

            //重发校验
            String transNo=request.getTradeNo();
            String sqlExt="select pk_settle from bl_ext_pay where TRADE_NO='"+transNo+"' and ROWNUM=1";
            Map<String, Object> forMap=DataBaseHelper.queryForMap(sqlExt);
            if(forMap!=null){
                String resJson=createSelfSettleResult(request,param,MapUtils.getString(forMap,"pkSettle"));
                return resJson;
            }
            //2.患者就诊信息构建
            String sql="select * from pv_encounter pv where code_pv=?";
            PvEncounter pvinfo=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{request.getCodePv()});
            if(pvinfo==null){
                String resJson=errorJson("未获取到患者有效就诊记录！");
                return resJson;
            }
            //医保患者自费结算，先转为自费结算后，结算完成后转为医保
            String pkInsu = new String(pvinfo.getPkInsu());//原来的类型

            //3.User信息构建
            User user=new User();
            BdOuEmployee emp =getEmpInfo(request.getCodeEmpSt());
            BdOuDept dept=getDeptInfo(request.getCodeDeptSt());
            user.setPkEmp(emp.getPkEmp());//--结算人
            user.setCodeEmp(emp.getCodeEmp());
            user.setNameEmp(emp.getNameEmp());//结算人
            user.setPkOrg(pvinfo.getPkOrg());//机构
            //user.setPkDept(dept!=null?dept.getPkDept():ZsrmSelfAppConstant.PK_DEPT_CG);//结算科室
            user.setPkDept(dept!=null?dept.getPkDept():getPkDeptByConf(pvinfo.getPkOrg()));//结算科室
            UserContext.setUser(user);

            //4.自费结算，先将患者类型转为自费
            if(EnumerateParameter.ZERO.equals(request.getIsHp())) {
            	String hpSql = "select * from BD_HP where del_flag = '0' and eu_hptype ='0' ";
            	BdHp bdHp = DataBaseHelper.queryForBean(hpSql,BdHp.class);
            	updatePkInsu(bdHp.getPkHp(),pvinfo);
            	pvinfo = DataBaseHelper.queryForBean(sql, PvEncounter.class,new Object[]{request.getCodePv()});
            }

            //4.根据就诊信息获取指定就诊待缴费记录明细
            Map<String,Object> noSettleParamMap=new HashMap<>();
            String curtime = DateUtils.getDateTimeStr(new Date());
            noSettleParamMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
            noSettleParamMap.put("notDisplayFlagPv", "0");
            noSettleParamMap.put("isNotShowPv", "0");
            noSettleParamMap.put("pkPv",pvinfo.getPkPv());
            noSettleParamMap.put("pkPi",pvinfo.getPkPi());

            List<BlPatiCgInfoNotSettleVO> mapResult= opcgPubHelperService.queryPatiCgInfoNotSettle(noSettleParamMap);

            //5.获取参数中的计费明细与待缴费明细比对，并核实订单金额是否一致
	        if(mapResult==null || mapResult.size()==0){
	        	//反查是否已经处理成功
		    	Map<String, Object> resMap = DataBaseHelper.queryForMap(sqlExt);
		        if(resMap!=null){
		        	//自费结算更改了患者的医保类型，这里需要回滚
		            if(EnumerateParameter.ZERO.equals(request.getIsHp())) {
		            	platformTransactionManager.rollback(status);
		            }
		            String resJson=createSelfSettleResult(request,param,MapUtils.getString(forMap,"pkSettle"));
		            return resJson;
		        }else {
		        	throw new BusException("未查询到待缴费费用明细！");
		        }
            }
            Map<String,Object> tempResMap=getHisAmountSum(request,mapResult);
            BigDecimal amountHisSum= (BigDecimal)tempResMap.get("amountHisSum");
            BigDecimal amountOrder=new BigDecimal(request.getAmountSt()).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal amountPi = new BigDecimal(request.getAmountPi()).setScale(2,BigDecimal.ROUND_HALF_UP);
            if(amountOrder.compareTo(amountHisSum)!=0){
            	throw new BusException("订单总金和HIS结算总金额计算不符请重新计算！");
            }
            if(EnumerateParameter.ONE.equals(request.getIsHp())) {
            	ReqYbPreSettleInfovo ybPreSettleInfo = request.getYbPreSettleInfo();
            	if(amountHisSum.compareTo(ybPreSettleInfo.getAmount())!=0){
                    throw new BusException("医保结算单：医保结算总和HIS结算总金额不符请重新计算！");
                }
            }else {
            	if(amountPi.compareTo(amountHisSum)!=0){
            		throw new BusException("自费结算单：自费总金额和HIS结算总金额计算不符请重新计算！");
                }
            }

            //7.调用预结算接口
            List<BlPatiCgInfoNotSettleVO> tempSetvoList=(List<BlPatiCgInfoNotSettleVO> )tempResMap.get("tempSetvoList");
            String mapResultJson=JsonUtil.writeValueAsString(tempSetvoList);
            List<BlOpDt> opDts = JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});
            ApplicationUtils apputil = new ApplicationUtils();
            OpCgTransforVo opCgforVo = new OpCgTransforVo();
            opCgforVo = settleWillSelf(request, pvinfo, user, amountHisSum,amountPi, opDts, apputil, opCgforVo);

            //8.调用医保结算
            InsQgybSt insQgybSt = null;
            if(EnumerateParameter.ONE.equals(request.getIsHp())) {
            	if(EnumerateParameter.ONE.equals(request.getIsHpOnline())){//微信线上医保支付
            		insQgybSt = saveYbSettleInfo(request.getSetlinfo(),request.getSetldetail(),pvinfo,user,apputil);
            	}
            	else{//自助机医保支付
             		insQgybSt = (InsQgybSt)ybSettleMethod(request, pvinfo,apputil,user);
            	}
            	ybPkSettle = insQgybSt.getSetlId();
            	
            }
            //9.调用结算接口
            opCgforVo = getSettltForSelfOpCgTransforVo(request, pvinfo, amountPi,user,opDts, apputil, opCgforVo);

            //10.自费结算，将患者类型转为原类型
            if(EnumerateParameter.ZERO.equals(request.getIsHp())) {
            	updatePkInsu(pkInsu,pvinfo);
            }
            platformTransactionManager.commit(status);

            //11.欠费解锁
        	unlockPi(def,pvinfo.getPkPi(),apputil,user);

            //12.医保结算记录和HIS结算记录绑定
            if(EnumerateParameter.ONE.equals(request.getIsHp())) {
                hisYbRelationship(def,pvinfo.getPkPv(),ybPkSettle,opCgforVo.getPkSettle(),apputil,user);
            }

            //13.更新第三方支付表
            updateExt(def,opCgforVo.getPkSettle());

            //14.生成电子票据
            getElectronicBill(def,user,opCgforVo);

            //15.结算完成，构建返回参数
            String resJson=createSelfSettleResult(request,param,opCgforVo.getPkSettle());
            return resJson;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("自助机门诊自费结算服务失败："+ ex.getMessage());
            platformTransactionManager.rollback(status);
            //失败后进行医保结算撤销
            if(CommonUtils.isNotNull(ybPkSettle)) {
            	 ApplicationUtils apputil = new ApplicationUtils();
            	 String ybCancelStr = ybSettleCancelMethod(ybPkSettle,apputil, UserContext.getUser());
            	 if(CommonUtils.isNotNull(ybCancelStr)) {
            		 log.error("自助机门诊自费结算服务失败："+ ybCancelStr);
            	 }
            }
            String resJson=errorJson("自助机门诊自费结算服务失败："+ ex.getMessage());
            return resJson;
        }
    }

    /**
     * 修改患者类型
     *
     * @param pkInsu
     * @param pvencounter
     */
    private void updatePkInsu(String pkInsu, PvEncounter pvencounter) {
        pvencounter.setPkInsu(pkInsu);
        DataBaseHelper.updateBeanByPk(pvencounter);
    }

    /**
     * 医保预结
     * @param param
     * @return
     */
    public String getYbWillSettle(String param){
    	//修改为手动事物 , 关闭事务自动提交
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        try {
            //1.参数解析关键数据校验工作（codePv,tradeNo,amountExt,dataList数据集合）
            RequestYbWillSettleVo request = JsonUtil.readValue(param, RequestYbWillSettleVo.class);
            String msg = checkReqWillSettleParam(request);
            if(StringUtils.isNotBlank(msg)){
                String resJson=errorJson(msg);
                return resJson;
            }
            //2.患者就诊信息构建
            String sql = "select * from pv_encounter pv where code_pv=?";
            PvEncounter pvinfo = DataBaseHelper.queryForBean(sql, PvEncounter.class, new Object[]{request.getCodePv()});
            if (pvinfo == null) {
                String resJson=errorJson("未获取到患者有效就诊记录！");
                return resJson;
            }
            //3.User信息构建
            User user = new User();
            BdOuEmployee emp =getEmpInfo(CommonUtils.isNull(request.getCodeEmpSt())? ZsrmSelfAppConstant.CODE_EMP : request.getCodeEmpSt());
            BdOuDept dept=getDeptInfo(CommonUtils.isNull(request.getCodeDeptSt())? ZsrmSelfAppConstant.CODE_DEPT_CG : request.getCodeDeptSt());
            user.setPkEmp(emp.getPkEmp());//--结算人
            user.setCodeEmp(emp.getCodeEmp());
            user.setNameEmp(emp.getNameEmp());//结算人
            user.setPkOrg(ZsrmSelfAppConstant.PK_ORG);//机构
            //user.setPkDept(dept!=null?dept.getPkDept():ZsrmSelfAppConstant.PK_DEPT_CG);//结算科室
            user.setPkDept(dept!=null?dept.getPkDept():getPkDeptByConf(pvinfo.getPkOrg()));//结算科室
            UserContext.setUser(user);

            //4.根据就诊信息获取指定就诊待缴费记录明细
            Map<String,Object> noSettleParamMap=new HashMap<>();
            String curtime = DateUtils.getDateTimeStr(new Date());
            noSettleParamMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
            noSettleParamMap.put("notDisplayFlagPv", "0");
            noSettleParamMap.put("isNotShowPv", "0");
            noSettleParamMap.put("pkPv",pvinfo.getPkPv());
            noSettleParamMap.put("pkPi",pvinfo.getPkPi());

            List<BlPatiCgInfoNotSettleVO> mapResult= opcgPubHelperService.queryPatiCgInfoNotSettle(noSettleParamMap);
            //5.获取参数中的计费明细与待缴费明细比对，并核实订单金额是否一致
            if(mapResult==null || mapResult.size()==0){
                String resJson=errorJson("未查询到待缴费费用明细！");
                return resJson;
            }

            List<BlPatiCgInfoNotSettleVO> tempCgList=getHisYbWillCgList(request,mapResult);

            List<String> pkCgops=new ArrayList<>();
            tempCgList.forEach(m->{pkCgops.add(m.getPkCgop());});
            Map<String,Object> ybWillDate = ybWillSettleMethod(pkCgops,pvinfo,user);
            //医保预算成功返回实体
        	String resJson = createYbWillSettleResult(request,param,tempCgList,ybWillDate);
        	platformTransactionManager.commit(status);
        	return resJson;
        }catch (Exception e){
        	platformTransactionManager.rollback(status);
            String resJson=errorJson("调用医保预结算失败，失败原因："+e.getMessage());
            return resJson;
        }

    }

    /**
     * 预约核酸检测
     * @param param
     * @return
     */
    public String appointNuc(String param) {
    	//修改为手动事物 , 关闭事务自动提交
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        try {
            //1.参数解析关键数据校验工作（codePi）
        	RequestAppointNucVo request = JsonUtil.readValue(param, RequestAppointNucVo.class);
            if (request == null)  {
                String resJson=errorJson("获取请求参数异常NULL");
                return resJson;
            }
            if (CommonUtils.isNull(request.getPatientId())) {
                String resJson=errorJson("未传入[patientId]病人Id!");
                return resJson;
            }
            //预约日期
            Date appointMentDate  = new Date();
            String appointMentStr = request.getAppointMentDate();
            if(CommonUtils.isNotNull(appointMentStr)) {
            	appointMentDate = DateUtils.strToDate(appointMentStr, "yyyy-MM-dd");
            }
            //2.患者信息
            String sql = "select * from pi_master pi where code_op=?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql, PiMaster.class, new Object[]{request.getPatientId()});
            if (piMaster == null) {
                String resJson=errorJson("根据patientId【"+request.getPatientId()+"】未获取到患者信息！");
                return resJson;
            }
            //3.1 防止多次调用（理论上一天只能预约一次吧？后期如果无限制，可以去掉）
            /*
            String dateToStr = DateUtils.dateToStr("yyyyMMdd", new Date());
            List<Map<String, Object>> schAppList =DataBaseHelper.queryForList("select * from PV_ENCOUNTER pv inner join PV_PE pe on pe.PK_PV = pv.PK_PV  "
            		+ " where pv.EU_PVTYPE in('4') AND pv.EU_STATUS in('0','1','2') "
            		+ " and to_char(pv.DATE_BEGIN,'yyyyMMdd') = ? and pv.pk_pi = ?", new Object[]{dateToStr,piMaster.getPkPi()});
			if(schAppList.size()>0){
				PvEncounter pvEncounter  = new PvEncounter();
				pvEncounter.setCodePv(CommonUtils.getPropValueStr(schAppList.get(0), "codePv"));
				String resJson = createAppointNucResult(request,piMaster,pvEncounter);
                return resJson;
			}
			*/

            //3.User信息构建
            User user = new User();
            BdOuEmployee emp =getEmpInfo(ZsrmSelfAppConstant.CODE_EMP_NUC);
            BdOuDept dept=getDeptInfo(ZsrmSelfAppConstant.CODE_DEPT_NUC);
            user.setPkEmp(emp.getPkEmp());//--结算人
            user.setCodeEmp(emp.getCodeEmp());
            user.setNameEmp(emp.getNameEmp());//结算人
            user.setPkOrg(ZsrmSelfAppConstant.PK_ORG);//机构
            user.setPkDept(dept!=null?dept.getPkDept():ZsrmSelfAppConstant.PK_DEPT_NUC);//核酸预约科室
            UserContext.setUser(user);

       		String hpSql = "select * from BD_HP where del_flag = '0' and eu_hptype ='0' ";
        	BdHp bdHp = DataBaseHelper.queryForBean(hpSql,BdHp.class);

            //4.生成就诊记录
            String pkPv = NHISUUID.getKeyId();
            PvEncounter pv = savePvEncounter(piMaster, pkPv,bdHp,appointMentDate,user);

            //5.生成体检就诊记录，写表pv_pe
            savePvPe(piMaster,pv,appointMentDate,user);

            //6.保存医嘱信息
            String codeApply = saveOrder(piMaster, pv,bdHp,dept,appointMentDate,user);

            //7.医保预算成功返回实体
        	String resJson = createAppointNucResult(request,piMaster,pv,codeApply);
        	platformTransactionManager.commit(status);
        	return resJson;
        }catch (Exception e){
        	platformTransactionManager.rollback(status);
            String resJson=errorJson("预约核酸检测失败，失败原因："+e.getMessage());
            return resJson;
        }
    }

    /**
     * 获取门诊费用主表服务
     * @param param
     * @return
     */
	public String queryOutpfeeMasterInfo(String param) {
		RequestQryOutpfeeMasterVo reqvo = JsonUtil.readValue(param, RequestQryOutpfeeMasterVo.class);
        try {
            String patientid = reqvo.getPatientid();
            String indate = reqvo.getIndate();
            String outdate = reqvo.getOutdate();

            if (CommonUtils.isEmptyString(patientid)) {
                String resJson=errorJson("未传入[patientid]患者ID！");
                return resJson;
            }
            if (CommonUtils.isEmptyString(indate)) {
                String resJson=errorJson("未传入[indate]收费开始时间！");
                return resJson;
            }
            if (CommonUtils.isEmptyString(outdate)) {
                String resJson=errorJson("未传入[outdate]收费结束时间！");
                return resJson;
            }
            List<ResponseQryOutpfeeMasterVo> outfee = zsrmSelfAppMapper.qryOutpfeeMasterInfo(patientid,indate,outdate);

            String dateNow = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            CommonResSelfVo<List<ResponseQryOutpfeeMasterVo>> response = new CommonResSelfVo<List<ResponseQryOutpfeeMasterVo>>(
                    "1", "成功！", reqvo.getReqId(), dateNow, outfee);
            String hisjson=JsonUtil.writeValueAsString(response);
            hisjson="{\"outfee\":"+JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data")) + "}";
            String resJson=successJson(hisjson);
            return resJson;
        }catch (Exception e){
            e.printStackTrace();
            String resJson=errorJson("HIS处理失败！");
            return resJson;
        }
	}

	/**
     * 获取门诊费用明细服务
     * @param param
     * @return
     */
	public String queryOutpfeeDetailInfo(String param) {
		RequestQryOutpfeeDetailVo reqvo = JsonUtil.readValue(param, RequestQryOutpfeeDetailVo.class);
		try {
            String patientid = reqvo.getPatientid();
            String codeSt = reqvo.getReceiptNo();

            if (CommonUtils.isEmptyString(patientid)) {
                String resJson=errorJson("未传入[patientid]患者ID！");
                return resJson;
            }
            if (CommonUtils.isEmptyString(codeSt)) {
                String resJson=errorJson("未传入[receiptNo]票据号！");
                return resJson;
            }
            List<ResponseQryOutpfeeDetailVo> outfeedetail = zsrmSelfAppMapper.qryOutpfeeDetailInfo(patientid,codeSt);

            String dateNow = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            CommonResSelfVo<List<ResponseQryOutpfeeDetailVo>> response = new CommonResSelfVo<List<ResponseQryOutpfeeDetailVo>>(
                    "1", "成功！", reqvo.getReqId(), dateNow, outfeedetail);
            String hisjson=JsonUtil.writeValueAsString(response);
            hisjson="{\"outfee\":"+JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data")) + "}";
            String resJson=successJson(hisjson);
            return resJson;
        }catch (Exception e){
            e.printStackTrace();
            String resJson=errorJson("HIS处理失败！");
            return resJson;
        }
	}

    /**
     * 创建结算返回信息（自费+医保）
     * @param request
     * @param param
     * @param pkSettle
     * @return
     */
    private String createSelfSettleResult(CommonReqSelfVo request,String param,String pkSettle){
        //根据pkSettle 获取本次结算返回信息内容
        try{
            String sql = "SELECT bls.CODE_ST,inv.CODE_INV,bdu.NAME,dept.NAME_DEPT,get_zhiyin('" +
                    pkSettle + "') as note,occ.winno_conf,occ.pk_dept_ex " +
                    "from bl_settle bls " +
                    "left JOIN bl_st_inv stinv ON bls.pk_settle = stinv.pk_settle " +
                    "left JOIN bl_invoice inv ON inv.pk_invoice = stinv.pk_invoice " +
                    "left join ex_pres_occ occ on occ.PK_SETTLE=bls.PK_SETTLE " +
                    "left join bd_ou_dept dept on dept.PK_DEPT=occ.PK_DEPT_EX " +
                    "left join BD_DEPT_UNIT bdu on bdu.CODE=occ.WINNO_CONF " +
                    "WHERE  bls.PK_SETTLE = '"+pkSettle+"' " +
                    "group by bls.CODE_ST,inv.CODE_INV,bdu.NAME,dept.NAME_DEPT,dept.NOTE,occ.winno_conf,occ.pk_dept_ex";
            List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql);

            String  dateNow = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            ResponseSettleVo response =new ResponseSettleVo();
            response.setDiagnostics("成功！");
            response.setCodePi(request.getCodePi());
            response.setCodeOp(request.getCodePi());
            response.setCodePv(request.getCodePv());
            String codeStr = DataBaseHelper.queryForScalar("select wm_concat(t.CODE_APPLY) CODE_APPLY_STR  from (SELECT ord.code_apply FROM cn_order ord WHERE ord.flag_erase=0 and ord.code_ordtype like '02%' and ord.pk_pv = (select pk_pv from PV_ENCOUNTER where code_pv =?) group by  ord.code_apply) t",String.class,request.getCodePv());
            if(mapList==null||mapList.size()==0){
                response.setWinnoConf("");
                response.setWinnoConfNote("");
                response.setCodeSt("");
                response.setCodeInv("");
            }else{
                StringBuffer winnoConf = new StringBuffer("");
                List<String> list = new ArrayList<>();
                for(Map<String, Object> map :mapList){
                    if(StringUtils.isNotBlank(MapUtils.getString(map,"pkDeptEx"))) {
                        if (!list.contains(MapUtils.getString(map, "pkDeptEx"))) {
                            list.add(MapUtils.getString(map, "pkDeptEx"));
                            winnoConf.append(MapUtils.getString(map, "name", ""));
                            winnoConf.append("|");
                        }
                    }
                }
                response.setWinnoConf(winnoConf.toString());
                response.setWinnoConfNote(MapUtils.getString(mapList.get(0),"note",""));
                response.setCodeSt(MapUtils.getString(mapList.get(0),"codeSt",""));
                response.setCodeInv(MapUtils.getString(mapList.get(0),"codeInv",""));
            }
            response.setApplyNo(StringUtils.isBlank(codeStr)?null:codeStr);

            CommonResSelfVo<ResponseSettleVo> resSelfVo=new CommonResSelfVo<ResponseSettleVo>(
                    "1", "成功！", request.getReqId(), dateNow, response);
            String hisjson=JsonUtil.writeValueAsString(resSelfVo);
            hisjson= "{\"data\":"+JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data")) + "}";
            String resJson=successJson(hisjson);
            return resJson;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("返回指引单信息失败："+ ex.getMessage());
            String resJson=guideErrorJson("返回指引单信息失败："+ ex.getMessage());
            return resJson;
        }
    }
    /**
     * 创建医保预结算返回信息
     * @param request
     * @param param
     * @return
     */
    private String createYbWillSettleResult(RequestYbWillSettleVo request,String param,List<BlPatiCgInfoNotSettleVO> tempCgList,Map<String, Object> ybWillData){
        String  dateNow= DateUtils.getDate("yyyy-MM-dd HH:mm:ss");

        //his总金额
        BigDecimal hisAmount = BigDecimal.ZERO;
        for (BlPatiCgInfoNotSettleVO tempCg : tempCgList) {
        	hisAmount = hisAmount.add(tempCg.getAmount());
		}
        OutParamHuaJia ybPreSettle = (OutParamHuaJia)ybWillData.get("ybPreSettleInfo");
        BigDecimal ybAmount = BigDecimal.valueOf(ybPreSettle.getAmount() == null ? 0d :ybPreSettle.getAmount());//医保返回的总金额
        BigDecimal jjzf = BigDecimal.valueOf(ybPreSettle.getAmtJjzf() == null ? 0d : ybPreSettle.getAmtJjzf());//医保返回的基金支付
        BigDecimal grzf = BigDecimal.valueOf(ybPreSettle.getAmtGrzf() == null ? 0d : ybPreSettle.getAmtGrzf());//医保返回的个人支付

        //校验医保支付金额或者自费支付金额是否超过总金额，如果超过总金额，以总金额为准
        if (jjzf.compareTo(hisAmount) == 1) {
        	jjzf = hisAmount;
        	ybAmount = hisAmount;
        } else if (grzf.compareTo(hisAmount) == 1) {
        	grzf = hisAmount;
        	ybAmount = hisAmount;
        }
        if (hisAmount.compareTo(ybAmount) != 0) {
        	grzf = grzf.add((hisAmount.subtract(ybAmount)));
            if (grzf.compareTo(BigDecimal.ZERO) == -1) {
            	grzf = BigDecimal.ZERO;
            }
            ybAmount = hisAmount;
        }

        BigDecimal grzhzf = BigDecimal.ZERO;//个人账户支付（一代卡或三代卡）
        BigDecimal hpCateBalance  = request.getHpCateBalance() == null ? BigDecimal.ZERO : request.getHpCateBalance();//个人账户余额
        //个人账户有余额，计算个账支付
        if (hpCateBalance.compareTo(BigDecimal.ZERO) == 1) {
        	if(hpCateBalance.compareTo(grzf) == -1) {//个人账户余额不足(扣除个账所有余额，不足的部个人现金支付)
        		grzhzf = hpCateBalance;
        		grzf = grzf.subtract(grzhzf);
        	}else  {
        		//个人账户余额充足
        		grzhzf = grzf;
        		grzf = BigDecimal.ZERO;
        	}
        }

        //组织返回参数
        ResponseYbWillSettleVo response =new ResponseYbWillSettleVo();
        response.setDiagnostics("成功！");
        response.setCodePi(request.getCodePi());
        response.setCodePv(request.getCodePv());
        ReqYbPreSettleInfovo ybPreSettleInfo = new ReqYbPreSettleInfovo();
        ybPreSettleInfo.setAmount(ybAmount);
        ybPreSettleInfo.setAmtJjzf(jjzf);
        ybPreSettleInfo.setAmtGrzhzf(grzhzf);
        ybPreSettleInfo.setAmtGrzf(grzf);
        ybPreSettleInfo.setAmtGrzh(BigDecimal.ZERO);
        ybPreSettleInfo.setCashAddFee(ybPreSettleInfo.getAmount());
        ybPreSettleInfo.setMedType(ybPreSettle.getMedType());
        ybPreSettleInfo.setMdtrtId(ybPreSettle.getMdtrtId());
        ybPreSettleInfo.setMdtrtCertType(ybPreSettle.getMdtrtCertType());


        Map<String, Object> ybPreSettleParamMap = (Map<String, Object>)ybWillData.get("ybPreSettleParam");
        ReqYbPreSettleParamVo ybPreSettleParam = new ReqYbPreSettleParamVo();
        String medfeeSumamt = CommonUtils.getPropValueStr(ybPreSettleParamMap, "medfee_sumamt");
        ybPreSettleParam.setMedfeeSumamt(BigDecimal.valueOf(CommonUtils.isNull(medfeeSumamt) ? 0d : Double.valueOf(medfeeSumamt)));
        ybPreSettleParam.setPsnNo(CommonUtils.getPropValueStr(ybPreSettleParamMap, "psn_no"));
        ybPreSettleParam.setPort("2206");
        ybPreSettleParam.setMdtrtId(CommonUtils.getPropValueStr(ybPreSettleParamMap, "mdtrt_id"));
        ybPreSettleParam.setMdtrtCertType(CommonUtils.getPropValueStr(ybPreSettleParamMap, "mdtrt_cert_type"));
        ybPreSettleParam.setMedType(CommonUtils.getPropValueStr(ybPreSettleParamMap, "med_type"));
        ybPreSettleParam.setMdtrtCertNo(CommonUtils.getPropValueStr(ybPreSettleParamMap, "mdtrt_cert_no"));
        ybPreSettleParam.setChrgBchno(CommonUtils.getPropValueStr(ybPreSettleParamMap, "chrg_bchno"));
        ybPreSettleParam.setInsutype(CommonUtils.getPropValueStr(ybPreSettleParamMap, "insutype"));
        ybPreSettleParam.setAcctUsedFlag(CommonUtils.getPropValueStr(ybPreSettleParamMap, "acct_used_flag"));
        ybPreSettleParam.setPsnSetlway(CommonUtils.getPropValueStr(ybPreSettleParamMap, "psn_setlway"));
        response.setYbPreSettleParam(ybPreSettleParam);
        //计算HIS与医保结构体差额
        if(ybPreSettleInfo.getAmount().subtract(ybPreSettleParam.getMedfeeSumamt()).compareTo(BigDecimal.ZERO) == 1){
            ybPreSettleInfo.setCashAddFee(ybPreSettleInfo.getAmount().subtract(ybPreSettleParam.getMedfeeSumamt()));
        }else{
        	ybPreSettleInfo.setCashAddFee(BigDecimal.ZERO);
        }
        response.setYbPreSettleInfo(ybPreSettleInfo);
        			

        CommonResSelfVo<ResponseYbWillSettleVo> resSelfVo=new CommonResSelfVo<ResponseYbWillSettleVo>(
                "1", "成功！", request.getReqId(), dateNow, response);
        String hisjson=JsonUtil.writeValueAsString(resSelfVo);
        hisjson=JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data"));
        String resJson=successJson(hisjson);
        return resJson;
    }

    /**
     * 创建预约核酸检测返回信息
     * @param request
     * @param param
     * @param pkSettle
     * @return
     */
    private String createAppointNucResult(CommonReqSelfVo request,PiMaster master,PvEncounter pv,String codeApply){
        String  dateNow= DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        //组织返回参数
        ResponseAppointNucVo response =new ResponseAppointNucVo();
        response.setCode("infomation");
        response.setSeverity("infomation");
        response.setDiagnostics("成功！");
        response.setCodePv(pv.getCodePv());
        response.setApplyNo(codeApply);
        CommonResSelfVo<ResponseAppointNucVo> resSelfVo=new CommonResSelfVo<ResponseAppointNucVo>(
                "1", "成功！", request.getReqId(), dateNow, response);
        String hisjson=JsonUtil.writeValueAsString(resSelfVo);
        hisjson=JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data"));
        String resJson=successJson(hisjson);
        return resJson;
    }

    /**
     * 医保预结算方法
     * @param request
     * @param pvinfo
     * @return
     */
    private Map<String,Object> ybWillSettleMethod(List<String> pkCgops, PvEncounter pvinfo,User user){
    	Map<String,Object> paramHisMap=new HashMap<String,Object>();
        paramHisMap.put("pkPv",pvinfo.getPkPv());
        paramHisMap.put("pkCgops",pkCgops);
        ApplicationUtils apputil = new ApplicationUtils();
        ResponseJson response =apputil.execService("COMPAY", "ZsrmQGService", "mzHpHuaJia", paramHisMap,user);
        if(response.getStatus() != 0){
            throw new BusException("门诊HIS医保预结算失败："+response.getDesc());
        }
        return  (Map<String,Object>)response.getData();
    }

    /**
     * 医保结算方法
     * @param request
     * @param pvinfo
     * @return
     */
    private Object ybSettleMethod(RequestSelfAppSettleVo request, PvEncounter pvinfo,ApplicationUtils apputil,User user){
        Map<String,Object> hpSettleMap=new HashMap<>();
        hpSettleMap.put("pkPv",pvinfo.getPkPv());
        Map<String,Object> ybpreSetMap=new HashMap<>();
        ReqYbPreSettleParamVo preSetvo=request.getYbPreSettleParam();
        ybpreSetMap.put("medfee_sumamt",preSetvo.getMedfeeSumamt());
        ybpreSetMap.put("psn_no",preSetvo.getPsnNo());
        ybpreSetMap.put("port",preSetvo.getPort());
        ybpreSetMap.put("mdtrt_id",preSetvo.getMdtrtId());
        ybpreSetMap.put("mdtrt_cert_type",preSetvo.getMdtrtCertType());
        ybpreSetMap.put("med_type",preSetvo.getMedType());
        ybpreSetMap.put("mdtrt_cert_no",preSetvo.getMdtrtCertType());
        ybpreSetMap.put("chrg_bchno",preSetvo.getChrgBchno());
        ybpreSetMap.put("insutype",preSetvo.getInsutype());
        ybpreSetMap.put("acct_used_flag",preSetvo.getAcctUsedFlag());
        ybpreSetMap.put("psn_setlway",preSetvo.getPsnSetlway());
        hpSettleMap.put("ybPreSettlParam",ybpreSetMap);
        ResponseJson  response = apputil.execService("COMPAY","ZsrmQGService","mzHpJiaokuan",hpSettleMap,user);
        if(response.getStatus() != 0){
            throw new BusException("门诊HIS医保结算失败："+response.getDesc());
        }
        return  response.getData();
    }

    /**
     * 医保结算撤销方法
     * @param request
     * @param pvinfo
     * @return
     */
    private String ybSettleCancelMethod(String ybPkSettle, ApplicationUtils apputil,User user){
        Map<String,Object> hpSettleCancelMap=new HashMap<>();
        hpSettleCancelMap.put("ybPkSettle",ybPkSettle);
        ResponseJson  response = apputil.execService("COMPAY","ZsrmQGService","mzHpSetttleCancel",hpSettleCancelMap,user);
        if(response.getStatus() != 0){
            return "医保结算撤销失败："+response.getDesc();
        }
        return "";
    }

    /**
   	 * 保存就诊记录
   	 * @param master
   	 * @param pkPv
   	 * @return
   	 */
   	private PvEncounter savePvEncounter(PiMaster master,String pkPv,BdHp bdHp,Date appointMentDate,User user){
   		// 保存就诊记录
   		PvEncounter pvEncounter = new PvEncounter();
   		pvEncounter.setPkPv(pkPv);
   		pvEncounter.setPkPi(master.getPkPi());
   		pvEncounter.setPkDept(user.getPkDept());//就诊科室
   		pvEncounter.setCodePv(ApplicationUtils.getCode("0301"));
   		pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_4);// 体检
   		pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_2); // 结束
   		pvEncounter.setNamePi(master.getNamePi());
   		pvEncounter.setDtSex(master.getDtSex());
   		pvEncounter.setAgePv(ApplicationUtils.getAgeFormat(master.getBirthDate(),null));
   		pvEncounter.setAddress(master.getAddress());
   		pvEncounter.setFlagIn("0");
   		pvEncounter.setFlagSettle("1");
   		pvEncounter.setDtMarry(master.getDtMarry());
   		pvEncounter.setPkInsu(bdHp.getPkHp());//默认自费
   		pvEncounter.setPkPicate(master.getPkPicate());
   		pvEncounter.setPkEmpReg(user.getPkEmp());
   		pvEncounter.setNameEmpReg(user.getNameEmp());
   		pvEncounter.setPkEmpPhy(user.getPkEmp());
   		pvEncounter.setNameEmpPhy(user.getNameEmp());
   		pvEncounter.setDateBegin(appointMentDate);//挂号收费日期
   		pvEncounter.setDateReg(appointMentDate);//挂号的排班日期
   		pvEncounter.setDateClinic(appointMentDate);//看诊日期
   		pvEncounter.setFlagCancel("0");
   		pvEncounter.setDtIdtypeRel("01");
   		pvEncounter.setDtPvsource(master.getDtSource());
   		pvEncounter.setNameRel(master.getNameRel());
   		pvEncounter.setIdnoRel(master.getIdnoRel());
   		pvEncounter.setTelRel(master.getTelRel());
   		pvEncounter.setEuPvmode("0");
   		pvEncounter.setFlagSpec("0");
   		pvEncounter.setEuStatusFp("0");
   		pvEncounter.setEuLocked("0");
   		pvEncounter.setEuDisetype("0");
   		pvEncounter.setTs(new Date());
   		DataBaseHelper.insertBean(pvEncounter);
   		return pvEncounter;
   	}

   	/**
   	 * 保存体检属性
   	 * @param master
   	 * @param pkPv
   	 * @return
   	 */
   	public PvPe savePvPe(PiMaster master,PvEncounter pv,Date appointMentDate,User user){
   		PvPe pvPe = new PvPe();
   		pvPe.setPkPv(pv.getPkPv());
   		pvPe.setPkPi(master.getPkPi());
   		pvPe.setEffectiveB(appointMentDate);
		pvPe.setTicketno(0L);
		DataBaseHelper.insertBean(pvPe);
		return pvPe;
	}

	/**
   	 * 保存医嘱信息
   	 * @param master
   	 * @param pkPv
   	 * @return
   	 */
   	public String saveOrder(PiMaster master,PvEncounter pv,BdHp bdhp,BdOuDept dept,Date appointMentDate,User user){
   		ApplicationUtils apputil = new ApplicationUtils();
		// 组装患者信息
   		Map<String,Object> reqOrderMap = new HashMap<String, Object>();
   		String dateToStr = DateUtils.dateToStr("yyyyMMddHHmmss", appointMentDate);
   		Map<String,Object> patInfo = new HashMap<String, Object>();
   		patInfo.put("pkPi", master.getPkPi());
   		patInfo.put("pkPv", pv.getPkPv());
   		patInfo.put("age", pv.getAgePv());
   		patInfo.put("ageFormat", pv.getAgePv());
   		patInfo.put("pkDept", dept!=null?dept.getPkDept():ZsrmSelfAppConstant.PK_DEPT_NUC);
   		patInfo.put("deptName", dept!=null?dept.getNameDept():ZsrmSelfAppConstant.NAME_DEPT_NUC);
   		patInfo.put("codePi", master.getCodePi());
   		patInfo.put("codeOp", master.getCodeOp());
   		patInfo.put("namePi", pv.getNamePi());
   		patInfo.put("dtSex", master.getDtSex());
   		patInfo.put("sexName", "");
   		patInfo.put("birthDate", DateUtils.dateToStr("yyyyMMddHHmmss", master.getBirthDate()));
   		patInfo.put("idNo", master.getIdNo());
   		patInfo.put("dtIdtype", master.getDtIdtype());
   		patInfo.put("idTypeName", "");
   		patInfo.put("mobile", master.getMobile());
   		patInfo.put("pkInsu", pv.getPkInsu());
   		patInfo.put("nameInsu", "");
   		patInfo.put("pkDiag", "");
   		patInfo.put("euStatus", pv.getEuStatus());
   		patInfo.put("statusName", "");
   		patInfo.put("dateBegin", DateUtils.dateToStr("yyyyMMddHHmmss", pv.getDateBegin()));
   		patInfo.put("dateClinic", DateUtils.dateToStr("yyyyMMddHHmmss", pv.getDateClinic()));
   		patInfo.put("euPvtype", pv.getEuPvtype());
   		patInfo.put("codePv", pv.getCodePv());
   		patInfo.put("pkEmpPhy", user.getPkEmp());
   		patInfo.put("nameEmpPhy", user.getNameEmp());
   		reqOrderMap.put("patInfo", patInfo);

   		//获取检查检验医嘱信息
   		String sql = "select ord.*,org.price,lab.dt_contype,lab.dt_colltype,lab.dt_samptype,ris.dt_body,ris.dt_type,ris.desc_att "
   				+ " from bd_ord ord inner join bd_ord_org org on org.pk_ord = ord.pk_ord "
   				+ " left join bd_ord_lab lab on lab.pk_ord = ord.pk_ord and lab.del_flag = '0' "
   				+ " left join bd_ord_ris ris on ris.pk_ord = ord.pk_ord and ris.del_flag = '0' "
   				+ " where ord.del_flag='0' and ord.code='" + ZsrmSelfAppConstant.CODE_ORD + "'";
   		Map<String,Object> bdOrdMap = DataBaseHelper.queryForMap(sql);
   		if(bdOrdMap == null) {
   			throw new BusException("预约核酸检测失败：根据医嘱编码code_ord【"+ZsrmSelfAppConstant.CODE_ORD+"】未找到对应医嘱信息");
   		}
   		//获取医嘱号
   		Map<String, String> ordsnMap = new HashMap<String, String>();
   		ordsnMap.put("tableName", "CN_ORDER");
   		ordsnMap.put("fieldName", "ORDSN");
   		ordsnMap.put("count", "1");
   		ResponseJson resOrdsn =apputil.execService("CN", "BdSnService", "getSerialNo", ordsnMap,user);
   		if(resOrdsn.getStatus() != 0){
            throw new BusException("预约核酸检测失败："+resOrdsn.getDesc());
        }
   		int ordsn = (int)resOrdsn.getData();
   		//获取申请单号
   		String codeApply = "";
   		String codeOrdtype = CommonUtils.getPropValueStr(bdOrdMap, "codeOrdtype");
   		if(CommonUtils.isNotNull(codeOrdtype)) {
   			String subCode = codeOrdtype.substring(0, 2);
   			if(IOrdTypeCodeConst.DT_ORDTYPE_EXAMINE.equals(subCode)) {
   				//检查
   				codeApply = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_JCSQD);
   			}else if(IOrdTypeCodeConst.DT_ORDTYPE_TEST.equals(subCode)) {
   				//检验
   				codeApply = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_JYSQD);
   			}
   		}

   		//组装检查检验医嘱入参
   		List<Map<String,Object>> modifyListMap = new ArrayList<Map<String,Object>>();
   		Map<String,Object> modifyMap = new HashMap<String, Object>();
   		modifyMap.put("ordsn", ordsn);
   		modifyMap.put("ordsnParent", ordsn);
   		modifyMap.put("euDataState", "N");
   		modifyMap.put("groupno", 1);
   		modifyMap.put("pkEmpPhy", user.getPkEmp());
   		modifyMap.put("dtColltype", bdOrdMap.get("dtColltype"));
   		modifyMap.put("dtTubetype", bdOrdMap.get("dtContype"));
   		modifyMap.put("dtSamptype", bdOrdMap.get("dtSamptype"));
   		modifyMap.put("descBody", bdOrdMap.get("dtBody"));
   		modifyMap.put("dtRistype", bdOrdMap.get("dtType"));
   		modifyMap.put("risNotice", bdOrdMap.get("descAtt"));
   		modifyMap.put("ordsnParentRef", 0);
   		modifyMap.put("formApp", codeApply);
   		modifyMap.put("codeApply", codeApply);
   		modifyMap.put("dtHpprop", EnumerateParameter.ZERO.equals(bdhp.getEuHptype()) ? "01" : "11");//全部改为普通门诊
   		modifyMap.put("purpose", "");
   		modifyMap.put("pkPi", master.getPkPi());
   		modifyMap.put("pkPv", pv.getPkPv());
   		modifyMap.put("euPvtype", pv.getEuPvtype());
   		String opDateEffe = ApplicationUtils.getSysparam("CN0004", false);//医嘱有效天数
   		if(CommonUtils.isNull(opDateEffe)) {
   			opDateEffe = EnumerateParameter.ONE;
   		}
   		Calendar calendar = Calendar.getInstance();
   		calendar.setTime(appointMentDate);
   		calendar.add(Calendar.DATE,Integer.valueOf(opDateEffe)); //把日期往后增加,整数往后推,负数往前移动
   		Date dateEffe = calendar.getTime(); //日期的结果
   		modifyMap.put("dateEffe", dateEffe);//医嘱有效日期
   		modifyMap.put("codeOrdtype", bdOrdMap.get("codeOrdtype"));
   		modifyMap.put("dtOrder", bdOrdMap.get("codeOrdtype"));
   		modifyMap.put("euAlways", "1");
   		modifyMap.put("codeFreq", ApplicationUtils.getSysparam("CN0019", false));
   		modifyMap.put("pkOrd", bdOrdMap.get("pkOrd"));
   		modifyMap.put("codeOrd", bdOrdMap.get("code"));
   		modifyMap.put("nameOrd", bdOrdMap.get("name"));
   		modifyMap.put("dosage", 0);
   		modifyMap.put("quan", 1);
   		modifyMap.put("quanCg", 1);
   		modifyMap.put("quanDisp", 0);
   		modifyMap.put("packSize", 0);
   		modifyMap.put("days", 1);
   		modifyMap.put("ords", 1);
   		modifyMap.put("priceCg", bdOrdMap.get("price"));
   		modifyMap.put("amount", bdOrdMap.get("price"));
   		modifyMap.put("pkOrg", user.getPkOrg());
   		modifyMap.put("pkOrgExec", user.getPkOrg());
   		modifyMap.put("pkDeptExec", user.getPkDept());
   		modifyMap.put("dateEnter", dateToStr);
   		modifyMap.put("dateStart", dateToStr);
   		modifyMap.put("pkDept", user.getPkDept());
   		modifyMap.put("pkEmpInput", user.getPkEmp());
   		modifyMap.put("nameEmpInput", user.getNameEmp());
   		modifyMap.put("pkEmpOrd", user.getPkEmp());
   		modifyMap.put("nameEmpOrd", user.getNameEmp());
   		modifyMap.put("pricePd", 0);
   		modifyMap.put("flagDoctor", "1");
   		modifyMap.put("flagBl", "1");
   		modifyMap.put("flagCp", "0");
   		modifyMap.put("flagOcc", "0");
   		modifyMap.put("flagStop", "0");
   		modifyMap.put("flagStopChk", "0");
   		modifyMap.put("flagErase", "0");
   		modifyMap.put("flagEraseChk", "0");
   		modifyMap.put("flagDurg", "0");
   		modifyMap.put("flagSelf", "0");
   		modifyMap.put("flagSign", "0");
   		modifyMap.put("flagNote", "0");
   		modifyMap.put("flagBase", "0");
   		modifyMap.put("flagPrint", "0");
   		modifyMap.put("flagMedout", "0");
   		modifyMap.put("euExctype", "0");
   		modifyMap.put("flagEmer", "0");
   		modifyMap.put("flagPrev", "0");
   		modifyMap.put("flagThera", "0");
   		modifyMap.put("flagFit", "0");
   		modifyMap.put("flagFirst", "0");
   		modifyMap.put("delFlag", "0");
   		modifyMap.put("euStatusOrd", "0");
   		modifyMap.put("infantNo", "0");
   		modifyMap.put("quanBed", 0);
        modifyMap.put("flagRescue", "0");

   		modifyListMap.add(modifyMap);
   		reqOrderMap.put("modifyList", modifyListMap);
   		List<Map<String,Object>> deletingList = new ArrayList<Map<String,Object>>();
   		reqOrderMap.put("deletingList", deletingList);
   		reqOrderMap.put("flagQuan", "0");

        ResponseJson response =apputil.execService("CN", "SyxCnOpApplyOrdService", "saveRisLabApply", reqOrderMap,user);
        if(response.getStatus() != 0){
            throw new BusException("预约核酸检测失败："+response.getDesc());
        }
        return codeApply;
	}

   	/**
   	 * 欠费解锁
   	 * @param pkPi
   	 * @param apputil
   	 * @param user
   	 */
   	public void unlockPi(DefaultTransactionDefinition def,String pkPi,ApplicationUtils apputil,User user) {
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        try {
	   		Map<String,Object> map = new HashMap<String, Object>();
	   		map.put("pkPi", pkPi);
	   		ResponseJson response =apputil.execService("PRO", "ZsrmOpCgSettleService", "unlockPi", map,user);
	        if(response.getStatus() != 0){
	        	//记录失败，不影响流程
	            log.error("患者缴费后欠费解锁失败："+ response.getDesc());
	            platformTransactionManager.rollback(status);
	            return;
	        }
	        platformTransactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("自助机门诊结算患者缴费后欠费解锁服务失败："+ e.getMessage());
            platformTransactionManager.rollback(status);
        }
   	}

  	/**
   	 * 医保结算记录和HIS结算记录绑定
   	 * @param pkPv
   	 * @param ybPkSettle
   	 * @param pkSettle
   	 */
   	public void hisYbRelationship(DefaultTransactionDefinition def,String pkPv,String ybPkSettle,String pkSettle,ApplicationUtils apputil,User user) {
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        try {
        	Map<String,Object> hisYbRelationship = new HashMap<String, Object>();
            hisYbRelationship.put("pkPtmzjs", ybPkSettle);
            hisYbRelationship.put("euStatus", EnumerateParameter.TWO);
            hisYbRelationship.put("pkPv", pkPv);
            hisYbRelationship.put("pkSettle", pkSettle);
            hisYbRelationship.put("yWLX", EnumerateParameter.ONE);
	   		ResponseJson response = apputil.execService("COMPAY","ZsrmQGService","updatePkSettle",hisYbRelationship,user);
            if(response.getStatus() != 0){
            	//记录失败，不影响流程
                log.error("HIS结算和医保结算关联失败："+ response.getDesc());
                platformTransactionManager.rollback(status);
	            return;
            }
	        platformTransactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("自助机门诊结算医保结算记录和HIS结算记录绑定服务失败："+ e.getMessage());
            platformTransactionManager.rollback(status);
        }
   	}

   	/**
   	 * 更新第三方支付表
   	 * @param def
   	 * @param pkSettle
   	 */
   	public void updateExt(DefaultTransactionDefinition def,String pkSettle) {
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        try {
        	List<BlDeposit> blList = DataBaseHelper.queryForList("select PK_DEPO,PAY_INFO from BL_DEPOSIT where PK_SETTLE = '"+pkSettle+"'",BlDeposit.class);
            if(blList != null && blList.size() > 0){
            	for (BlDeposit blDeposit : blList) {
            		String upExtSql="update bl_ext_pay set pk_settle=?,PK_DEPO =? where SERIAL_NO=?";
                    DataBaseHelper.update(upExtSql,new Object[]{pkSettle,blDeposit.getPkDepo(),blDeposit.getPayInfo()});
				}
            }
	        platformTransactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("自助机门诊结算更新第三方支付表服务失败："+ e.getMessage());
            platformTransactionManager.rollback(status);
        }
   	}

    /**
     * 医保结算
     * @param param
     * @return
     */
    /*
    public String ybSettlemethod(String param){
        //修改为手动事物 , 关闭事务自动提交
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        try {
            //1.参数解析关键数据校验工作（codePv,tradeNo,amountExt,dataList数据集合）
            RequestSelfAppSettleVo request = JsonUtil.readValue(param, RequestSelfAppSettleVo.class);
            String msg = checkReqSettleParam(request);
            if(StringUtils.isNotBlank(msg)){
                String resJson=errorJson(msg);
                return resJson;
            }
            //2.患者就诊信息构建
            String sql="select * from pv_encounter pv where code_pv=?";
            PvEncounter pvinfo=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{request.getCodePv()});
            if(pvinfo==null){
                String resJson=errorJson("未获取到患者有效就诊记录！");
                return resJson;
            }

            //3.User信息构建
            User user=new User();
            String empSql="select PK_EMP from BD_OU_EMPLOYEE where CODE_EMP=?";
            Map<String,Object> empMap=DataBaseHelper.queryForMap(empSql,new Object[]{request.getCodeEmpSt()});
            String deptSql="select PK_DEPT from BD_OU_DEPT where CODE_DEPT=?";
            Map<String,Object> deptMap=DataBaseHelper.queryForMap(deptSql,new Object[]{request.getCodeDeptSt()});
            user.setPkEmp(MapUtils.getString(empMap,"pkEmp",ZsrmSelfAppConstant.PK_EMP_CG));//--结算人
            user.setNameEmp(request.getNameEmpSt());//结算人
            user.setPkOrg(ZsrmSelfAppConstant.PK_ORG);//机构
            user.setPkDept(MapUtils.getString(deptMap,"pkDept",ZsrmSelfAppConstant.PK_DEPT_CG));//结算科室
            UserContext.setUser(user);

            //4.根据就诊信息获取指定就诊待缴费记录明细
            Map<String,Object> noSettleParamMap=new HashMap<>();
            String curtime = DateUtils.getDateTimeStr(new Date());
            noSettleParamMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
            noSettleParamMap.put("notDisplayFlagPv", "0");
            noSettleParamMap.put("isNotShowPv", "0");
            noSettleParamMap.put("pkPv",pvinfo.getPkPv());
            noSettleParamMap.put("pkPi",pvinfo.getPkPi());

            List<BlPatiCgInfoNotSettleVO> mapResult= opcgPubHelperService.queryPatiCgInfoNotSettle(noSettleParamMap);

            //5.获取参数中的计费明细与待缴费明细比对，并核实订单金额是否一致
            if(mapResult==null || mapResult.size()==0){
                String resJson=errorJson("未查询到待缴费费用明细！");
                return resJson;
            }

            Map<String,Object> tempResMap=getHisAmountSum(request,mapResult);
            BigDecimal amountHisSum= (BigDecimal)tempResMap.get("amountHisSum");
            BigDecimal amountOrder=new BigDecimal(request.getAmountSt()).setScale(2,BigDecimal.ROUND_HALF_UP);
            if(amountOrder.compareTo(amountHisSum)!=0){
                String resJson=errorJson("订单总金和和HIS结算总金额计算不符请新计算！");
                return resJson;
            }

            //6.调用医保结算
            ApplicationUtils apputil = new ApplicationUtils();
            Map<String,Object> ybResSetDatavo=(Map<String,Object>)ybSettleMethod(request, pvinfo,apputil,user);

            //7.调用HIS结算服务
            OpCgTransforVo opCgVo = settleHisForYb(request, pvinfo, user, tempResMap, amountHisSum, apputil, ybResSetDatavo);
            platformTransactionManager.commit(status);

            //8.生成电子票据
            if("1".equals(request.getIsElecTicket())) {
                getElectronicBill(def,user,opCgVo);
            }
            //TODO 9.结算完成，构建返回参数
            String resJson = createSelfSettleResult(request,param,opCgVo.getPkSettle());
            return resJson;
        }catch (Exception e){
            String resJson=errorJson("医保结算失败,失败原因："+e.getMessage());
            return resJson;
        }

    }
    */
    /**
     * HIS结算服务-医保
     * @param request
     * @param pvinfo
     * @param user
     * @param tempResMap
     * @param amountHisSum
     * @param apputil
     * @param ybResSetDatavo
     * @return
     */
    /*
    private OpCgTransforVo settleHisForYb(RequestSelfAppSettleVo request, PvEncounter pvinfo, User user, Map<String, Object> tempResMap, BigDecimal amountHisSum, ApplicationUtils apputil, Map<String, Object> ybResSetDatavo) {
        String amtPiStr= MapUtils.getString(ybResSetDatavo,"amtGrzf");//个人支付
        BigDecimal amtPi=new BigDecimal(amtPiStr).setScale(2, RoundingMode.HALF_UP);

        List<BlPatiCgInfoNotSettleVO> tempSetvoList=(List<BlPatiCgInfoNotSettleVO> )tempResMap.get("tempSetvoList");
        String mapResultJson= JsonUtil.writeValueAsString(tempSetvoList);
        List<BlOpDt> opDts=JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});

        OpCgTransforVo opCgforVo=new OpCgTransforVo();
        opCgforVo.setPkPv(pvinfo.getPkPv());
        opCgforVo.setPkPi(pvinfo.getPkPi());
        opCgforVo.setBlOpDts(opDts);
        opCgforVo.setInvStatus("-2");//自费-不限制发票信息传入

        //付款数据构建
        List<BlDeposit> depositList = new ArrayList<BlDeposit>();
        BlDeposit deposit=new BlDeposit();
        deposit.setDtPaymode(request.getEuPayType());//支付方式
        deposit.setAmount(amtPi);
        deposit.setFlagAcc("0");
        deposit.setDelFlag("0");
        deposit.setPayInfo(request.getSerialNo());//第三方订单号
        depositList.add(deposit);
        opCgforVo.setBlDeposits(depositList);


        BlExtPay extPay=new BlExtPay();
        extPay.setPkExtpay(NHISUUID.getKeyId());
        extPay.setPkOrg(user.getPkOrg());
        extPay.setAmount(BigDecimal.valueOf(request.getAmountPi()));
        extPay.setFlagPay("1");//支付标志
        String date = request.getDateOrder();
        extPay.setDateAp(DateUtils.strToDate(date));//请求时间
        extPay.setDatePay(DateUtils.strToDate(date));//支付时间
        extPay.setSerialNo(request.getSerialNo());//订单号
        extPay.setTradeNo(request.getTradeNo());//订单号
        extPay.setSysname(request.getSysname());//系统名称
        extPay.setDescPay("");//
        extPay.setResultPay(request.getResultPay());
        extPay.setEuPaytype(request.getEuPayType());
        extPay.setCreator(user.getPkEmp());
        extPay.setCreateTime(new Date());
        extPay.setTs(new Date());
        extPay.setDelFlag("0");
        extPay.setDtBank(request.getDtBank());//TODO 银行
        List<BlExtPay> expayList=new ArrayList<BlExtPay>();
        expayList.add(extPay);
        opCgforVo.setBlExtPays(expayList);

        opCgforVo.setAggregateAmount(amountHisSum); //需支付总金额
        opCgforVo.setMedicarePayments(amountHisSum.subtract(amtPi));//医保支付
        opCgforVo.setPatientsPay(amtPi);//患者支付
        opCgforVo.setAmtInsuThird(amountHisSum.subtract(amtPi));
        ResponseJson respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
        if(respSettle.getStatus()!=0) {
            throw new BusException("门诊结算失败："+respSettle.getErrorMessage());
        }
        return opCgforVo;
    }
   */
    /**
     * 结算预结
     * @param request
     * @param pvinfo
     * @param user
     * @param amountHisSum
     * @param opDts
     * @param apputil
     * @param opCgforVo
     * @return
     */
    private OpCgTransforVo settleWillSelf(RequestSelfAppSettleVo request, PvEncounter pvinfo, User user, BigDecimal amountHisSum, BigDecimal amtPi,List<BlOpDt> opDts,
    		ApplicationUtils apputil, OpCgTransforVo opCgforVo) {
        opCgforVo.setPkPv(pvinfo.getPkPv());
        opCgforVo.setPkPi(pvinfo.getPkPi());
        opCgforVo.setBlOpDts(opDts);
        //医保支付
        ReqYbPreSettleInfovo ybPreSettleInfo = request.getYbPreSettleInfo();
        if(EnumerateParameter.ONE.equals(request.getIsHp())) {
        	opCgforVo.setAggregateAmount(ybPreSettleInfo.getAmount()); //需支付总金额
        	opCgforVo.setXjzf(ybPreSettleInfo.getAmtGrzf().add(ybPreSettleInfo.getAmtGrzhzf()));//现金支付（个人账户支付+现金支付）
        	opCgforVo.setAmtInsuThird(ybPreSettleInfo.getAmtJjzf());//医保支付
        	opCgforVo.setMedicarePayments(ybPreSettleInfo.getAmtJjzf());//医保支付
        	opCgforVo.setYbzf(ybPreSettleInfo.getAmtJjzf());
        }else {
        	//自费支付
        	opCgforVo.setAggregateAmount(amountHisSum); //需支付总金额
        	//opCgforVo.setMedicarePayments(amountHisSum.subtract(amtPi));//医保支付
        	opCgforVo.setPatientsPay(amtPi);//现金支付
        }
        //调用预结算接口 countOpcgAccountingSettlement
        ResponseJson response = apputil.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo,user);
        if(response.getStatus()!=0){
            // TODO 预结算接口调用失败 -处理异常消息：注意此接口调用要避免异常
           throw new BusException("门诊结算HIS预结算接口服务失败："+response.getDesc());
        }
        opCgforVo = (OpCgTransforVo)response.getData();
        if(EnumerateParameter.ONE.equals(request.getIsHp())) {
        	opCgforVo.setAmtInsuThird(ybPreSettleInfo.getAmtJjzf());//医保支付
        	opCgforVo.setXjzf(ybPreSettleInfo.getAmtGrzf().add(ybPreSettleInfo.getAmtGrzhzf()));//现金支付（个人账户支付+现金支付）
        }
        return opCgforVo;
    }

    /**
     * 进行自助机自费结算
     * @param request 请求参数
     * @param pvinfo 患者数据
     * @param user 当前用户
     * @param amountHisSum HIS结算总金额
     * @param opDts 收费明细
     * @param apputil 调用服务
     * @param amtPi 自费
     * @param opCgforVo 预结算响应参数
     * @return
     */
    private OpCgTransforVo getSettltForSelfOpCgTransforVo(RequestSelfAppSettleVo request, PvEncounter pvinfo,BigDecimal amtPi, User user,List<BlOpDt> opDts,ApplicationUtils apputil, OpCgTransforVo opCgforVo) {

        opCgforVo.setPkPv(pvinfo.getPkPv());
        opCgforVo.setPkPi(pvinfo.getPkPi());
        opCgforVo.setInvStatus("-2");//自费-不限制发票信息传入
        opCgforVo.setBlOpDts(opDts);

        //付款数据构建
        List<BlDeposit> depositList = new ArrayList<BlDeposit>();

        //医保结算
        if(EnumerateParameter.ONE.equals(request.getIsHp())) {
        	ReqYbPreSettleInfovo ybPreSettleInfo = request.getYbPreSettleInfo();
        	if(ybPreSettleInfo.getAmtGrzhzf().compareTo(BigDecimal.ZERO) == 1) {
        		BlDeposit deposit=new BlDeposit();
                deposit.setDtPaymode(ybPreSettleInfo.getGrzhzfPayType());//支付方式
                deposit.setAmount(ybPreSettleInfo.getAmtGrzhzf());//个人账户支付
                deposit.setFlagAcc("0");
                deposit.setDelFlag("0");
                deposit.setPayInfo(ybPreSettleInfo.getGzSerialNo());//第三方订单号
                depositList.add(deposit);

                //插入第三方支付表
                insertExtPay(ybPreSettleInfo.getGrzhzfPayType(),ybPreSettleInfo.getGzSerialNo(),ybPreSettleInfo.getGzTradeNo(),ybPreSettleInfo.getAmtGrzhzf() ,ybPreSettleInfo.getGzBankCardNo(),request.getDateOrder(),opCgforVo,user);
        	}
        	if(ybPreSettleInfo.getAmtGrzf().compareTo(BigDecimal.ZERO) == 1) {
        		BlDeposit deposit=new BlDeposit();
                deposit.setDtPaymode(request.getEuPayType());//支付方式
                deposit.setAmount(ybPreSettleInfo.getAmtGrzf());//个人现金支付
                deposit.setFlagAcc("0");
                deposit.setDelFlag("0");
                deposit.setPayInfo(request.getSerialNo());//第三方订单号
                depositList.add(deposit);
                //插入第三方支付表
                insertExtPay(request.getEuPayType(),request.getSerialNo(),request.getTradeNo(),ybPreSettleInfo.getAmtGrzf() ,ybPreSettleInfo.getGzBankCardNo(),request.getDateOrder(),opCgforVo,user);
        	}
        	//医保全部报销，没有扣个账以及个人支付的情况
        	if(depositList.size() == 0) {
        		BlDeposit deposit=new BlDeposit();
                deposit.setDtPaymode(IDictCodeConst.CASH);//支付方式
                deposit.setAmount(BigDecimal.ZERO);//个人现金支付
                deposit.setFlagAcc("0");
                deposit.setDelFlag("0");
                deposit.setPayInfo(request.getSerialNo());//第三方订单号
                depositList.add(deposit);
                //插入第三方支付表
                insertExtPay(request.getEuPayType(),request.getSerialNo(),request.getTradeNo(),ybPreSettleInfo.getAmtGrzf() ,ybPreSettleInfo.getGzBankCardNo(),request.getDateOrder(),opCgforVo,user);
        	}
        }else {
        	 BlDeposit deposit=new BlDeposit();
             deposit.setDtPaymode(request.getEuPayType());//支付方式
             deposit.setAmount(amtPi);//自费支付
             deposit.setFlagAcc("0");
             deposit.setDelFlag("0");
             deposit.setPayInfo(request.getSerialNo());//第三方订单号
             depositList.add(deposit);
             //插入第三方支付表
             insertExtPay(request.getEuPayType(),request.getSerialNo(),request.getTradeNo(),amtPi ,"",request.getDateOrder(),opCgforVo,user);
        }



        opCgforVo.setBlDeposits(depositList);

        ResponseJson  respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
        if(respSettle.getStatus()!=0) {
            throw new BusException("门诊自助机结算失败："+respSettle.getDesc());
        }
        if(respSettle.getData()!=null){
            OpCgTransforVo opCgTransforVo = (OpCgTransforVo) respSettle.getData();
            opCgforVo.setPkSettle(opCgTransforVo.getPkSettle());
            opCgforVo.setBlDeposits(opCgTransforVo.getBlDeposits());
        }
        return opCgforVo;
    }

    public void insertExtPay(String euPayType,String serialNo,String tradeNo,BigDecimal amountPi,String cardNo,
    		String date,OpCgTransforVo opCgforVo,User user) {
    	 BlExtPay extPay=new BlExtPay();
         extPay.setPkExtpay(NHISUUID.getKeyId());
         extPay.setPkOrg(user.getPkOrg());
         extPay.setAmount(amountPi);
         extPay.setFlagPay("1");//支付标志
         extPay.setDateAp(DateUtils.strToDate(date,"yyyy-MM-dd HH:mm:ss"));//请求时间
         extPay.setDatePay(DateUtils.strToDate(date,"yyyy-MM-dd HH:mm:ss"));//支付时间
         extPay.setSerialNo(serialNo);//订单号
         extPay.setTradeNo(tradeNo);//订单号
         extPay.setSysname("WX".equals(user.getCodeEmp()) ? "MZWX":"MZZZJ");//系统名称
         extPay.setDescPay("");//TODO 支付描述
         extPay.setResultPay("");//TODO 结果描述
         extPay.setEuPaytype(euPayType);
         extPay.setCreator(user.getPkEmp());
         extPay.setCreateTime(new Date());
         extPay.setTs(new Date());
         extPay.setDelFlag("0");
         extPay.setDtBank("");//TODO 银行
         extPay.setCardNo(cardNo);//个账支付对应的银行卡号
         extPay.setPkPi(opCgforVo.getPkPi());
         extPay.setPkPv(opCgforVo.getPkPv());
         DataBaseHelper.insertBean(extPay);
    }

    /**
     * 获取HIS总金额
     * @param request
     * @param mapResult
     * @return
     */
    private Map<String,Object> getHisAmountSum(RequestSelfAppSettleVo request,List<BlPatiCgInfoNotSettleVO> mapResult){
        Map<String,Object> resMap=new HashMap<String,Object>();
        boolean isSettleOnly=false;//是否进行部分结算
        if(request.getDataList()!=null && request.getDataList().size()>0){
            isSettleOnly=true;
        }
        List<BlPatiCgInfoNotSettleVO> tempSetvoList=new ArrayList<>();
        BigDecimal amountHisSum=new BigDecimal("0.00");
        if(isSettleOnly) {
            for (RequestSelfSettleDtVo inputParam : request.getDataList()) {
                List<BlPatiCgInfoNotSettleVO> notSettleVOList = mapResult.stream().filter(m -> m.getPkCgop().equals(inputParam.getPkCgop())).collect(Collectors.toList());

                if (notSettleVOList == null || notSettleVOList.size() == 0) {
                    throw new BusException("传入的计费明细不在待结算的明细中，请核实数据！");
                } else {
                    amountHisSum = amountHisSum.add(notSettleVOList.stream().map(BlPatiCgInfoNotSettleVO::getAmountPi).reduce(BigDecimal::add).get());
                    tempSetvoList.addAll(notSettleVOList);
                }
            }
        }else{
            amountHisSum = amountHisSum.add(mapResult.stream().map(BlPatiCgInfoNotSettleVO::getAmountPi).reduce(BigDecimal::add).get());
            tempSetvoList.addAll(mapResult);
        }
        resMap.put("amountHisSum",amountHisSum);
        resMap.put("tempSetvoList",tempSetvoList);
        return resMap;
    }

    /**
     * 获取医保试算
     * @param request
     * @param mapResult
     * @return
     */
    private List<BlPatiCgInfoNotSettleVO> getHisYbWillCgList(RequestYbWillSettleVo request,List<BlPatiCgInfoNotSettleVO> mapResult){
        boolean isSettleOnly=false;//是否进行部分结算
        if(request.getDataList()!=null && request.getDataList().size()>0){
            isSettleOnly=true;
        }
        List<BlPatiCgInfoNotSettleVO> tempSetvoList=new ArrayList<>();
        if(isSettleOnly) {
            for (RequestSelfSettleDtVo inputParam : request.getDataList()) {
                List<BlPatiCgInfoNotSettleVO> notSettleVOList = mapResult.stream().filter(m -> m.getPkCgop().equals(inputParam.getPkCgop())).collect(Collectors.toList());

                if (notSettleVOList == null || notSettleVOList.size() == 0) {
                    //TODO 异常信息：传入的计费明细不在待结算的明细中，请核实数据！
                    throw new BusException();
                } else {
                   // amountHisSum = amountHisSum.add(notSettleVOList.stream().map(BlPatiCgInfoNotSettleVO::getAmount).reduce(BigDecimal::add).get());
                    tempSetvoList.addAll(notSettleVOList);
                }
            }
        }else{
           // amountHisSum = amountHisSum.add(mapResult.stream().map(BlPatiCgInfoNotSettleVO::getAmount).reduce(BigDecimal::add).get());
            tempSetvoList.addAll(mapResult);
        }
        return tempSetvoList;
    }

	private InsQgybSt saveYbSettleInfo(RequestYbSettleVo setlinfo, List<RequestYbSettleDtVo> setldetail, PvEncounter pvinfo, User user,ApplicationUtils apputil) {

		try {
			if (setlinfo == null && CommonUtils.isNotNull(setlinfo.getSetlId())) {
				throw new BusException("医保订单保存失败：未传入医保结算信息 [setlinfo]");
			}

			// 查询登记信息
			InsQgybVisit visit = DataBaseHelper.queryForBean(
					"select * from ins_qgyb_visit where pk_pv = ? and  mdtrt_id = ? and del_flag='0'",
					InsQgybVisit.class, pvinfo.getPkPv(), setlinfo.getMdtrtId());
			if (visit == null) {
				throw new BusException("医保订单保存失败：传入的医保就诊id未检索到登记数据：" + visit.getMdtrtId());
			}
			// 保存医保结算信息
			InsQgybSt insQgybSt = new InsQgybSt();
			ApplicationUtils.copyProperties(insQgybSt, setlinfo);
			insQgybSt.setPkPv(pvinfo.getPkPv());
			insQgybSt.setPkPi(pvinfo.getPkPi());
			insQgybSt.setPkHp(pvinfo.getPkInsu());
			insQgybSt.setPkVisit(visit.getPkVisit());
			insQgybSt.setDateSt(new Date());
			insQgybSt.setAmount(insQgybSt.getMedfeeSumamt());// 总金额
			insQgybSt.setAmtGrzf(insQgybSt.getPsnCashPay());// 现金
			insQgybSt.setAmtJjzf(insQgybSt.getFundPaySumamt());// 基金
			insQgybSt.setYbPksettle(insQgybSt.getSetlId());
			DataBaseHelper.insertBean(insQgybSt);

			// 保存基金分项信息
			List<InsQgybStDt> insQgybStDts = new ArrayList<InsQgybStDt>();
			for (RequestYbSettleDtVo insDtVo : setldetail) {
				InsQgybStDt insDt = new InsQgybStDt();
				ApplicationUtils.copyProperties(insDt, insDtVo);
				ApplicationUtils.setDefaultValue(insDt, true);
				insDt.setPkInsst(insQgybSt.getPkInsst());
				insQgybStDts.add(insDt);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStDt.class), insQgybStDts);
			//保存5204 结算明细。发票自付金额使用
			ResponseJson  response = apputil.execService("COMPAY","ZsrmQGService","searchStChargeDtls",insQgybSt,user);
	        if(response.getStatus() != 0){
	            throw new BusException("调用医保功能号5204失败："+response.getDesc());
	        }
			
			return insQgybSt;
		} catch (Exception e) {
			log.error("医保结算订单,保存信息失败：" + e.getMessage());
			throw new BusException("线医保结算订单,保存信息失败：" + e.getMessage());
		}
		
	}
    


    /**
     * 生成电子票据
     * @param def
     * @param user
     * @param opCgforVo
     */
    private void getElectronicBill (DefaultTransactionDefinition def,User user,OpCgTransforVo opCgforVo){
        //生成电子票据
        TransactionStatus status2 = platformTransactionManager.getTransaction(def);
        try {
            Map<String, Object> invMap = electInvService.mzOutElectInv(opCgforVo.getPkPv(), user, opCgforVo.getPkSettle());

            platformTransactionManager.commit(status2);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("自助机门诊自费结算电子票据生成服务失败："+ e.getMessage());
            platformTransactionManager.rollback(status2);
        }
    }

    /**
     * 校验参数完整性以及匹配数据
     * @param request
     */
    private String checkReqSettleParam(RequestSelfAppSettleVo request) throws Exception {
        StringBuffer msg = new StringBuffer();
        if (request == null)  {
            return "获取请求参数异常NULL";
        }
        if(EnumerateParameter.ONE.equals(request.getIsHp())) {
        	ReqYbPreSettleInfovo ybPreSettleInfo = request.getYbPreSettleInfo();
        	if(CommonUtils.isNull(ybPreSettleInfo)) {
        		return "医保结算订单，未获取到医保参数[ybPreSettleInfo]";
        	}
        	ReqYbPreSettleParamVo ybPreSettleParam = request.getYbPreSettleParam();
        	if(CommonUtils.isNull(ybPreSettleParam)) {
        		return "医保结算订单，未获取到医保预结算参数[ybPreSettleParam]！";
        	}
        	//线上医保结算
        	if(EnumerateParameter.ONE.equals(request.getIsHpOnline())){
        		if (CommonUtils.isNull(request.getSetlinfo())) {
    				throw new BusException("医保结算订单：未传入医保结算信息 [setlinfo]!");
    			}
        	}
        	
        	
        }else {
        	//自费校验euPayType和serialNo （医保存在全报销不需要校验）
        	if (CommonUtils.isNull(request.getEuPayType())) {
                msg.append("&&未传入[euPayType]参数!");
            }
        	if (CommonUtils.isNull(request.getSerialNo())) {
                msg.append("&&未获取到第三方交易订单流水号！");
            }
        }
        if (CommonUtils.isNull(request.getCodePi())) {
            msg.append("&&未传入[codePi]患者Id!");
        }
        if (CommonUtils.isNull(request.getCodePv())) {
            msg.append("&&未传入[codePv]患者就诊Id!");
        }
        if (CommonUtils.isNull(request.getIsHp())) {
            msg.append("&&未传入[isHp]参数");
        }
        if (CommonUtils.isNull(request.getIsElecTicket())) {
            msg.append("&&未传入[isElecTicket]参数!");
        }
        if (CommonUtils.isNull(request.getDateOrder())) {
            msg.append("&&未传入[dateOrder]参数!");
        }
        if (CommonUtils.isNull(request.getFlagPay())) {
            msg.append("&&未传入[flagPay]参数!");
        }
        if (CommonUtils.isNull(request.getSysname())) {
            msg.append("&&未传入[sysname]参数!");
        }
        if (CommonUtils.isNull(request.getCodeEmpSt())) {
            msg.append("&&未传入[codeEmpSt]参数!");
        }
        if (CommonUtils.isNull(request.getNameEmpSt())) {
            msg.append("&&未传入[nameEmpSt]参数!");
        }
        if(CommonUtils.isNull(request.getCodeDeptSt())){
            msg.append("&&未传入[codeDeptSt]参数!");
        }
        if(CommonUtils.isNull(request.getNameDeptSt())){
            msg.append("&&未传入[nameDeptSt]参数!");
        }
        if (CommonUtils.isNull(request.getAmountExt())) {
            msg.append("&&未获取订单总金额!");
        }

        if(StringUtils.isNotBlank(msg)){
            return msg.toString();
        }
        String sql = "select count(1) from pv_encounter where code_pv=?";
        Integer countPv = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{request.getCodePv()});
        if (countPv != 1) {
            return "未查询到患者有效就诊记录，请重新核对";
        }
        return "";
    }

    /**
     * 医保预结算接口入参数据校验
     * @param request
     * @throws Exception
     */
    private String checkReqWillSettleParam(RequestYbWillSettleVo request) throws Exception{
    	StringBuffer msg = new StringBuffer();
        if (request == null)  {
            return "获取请求参数异常NULL";
        }
        if (CommonUtils.isNull(request.getCodePi())) {
            msg.append("&&未传入[codePi]患者Id!");
        }
        if (CommonUtils.isNull(request.getCodePv())) {
            msg.append("&&未传入[codePv]患者就诊Id!");
        }
        if(StringUtils.isNotBlank(msg)){
            return msg.toString();
        }
        String sql="select count(1) from pv_encounter where code_pv=?";
        Integer countPv= DataBaseHelper.queryForScalar(sql,Integer.class,new Object[]{request.getCodePv()});
        if(countPv!=1){
        	return "未查询到患者有效就诊记录，请重新核对！";
        }
        return "";
    }

    /**
     * 查询费用信息
     * @param paramMap
     * @return
     */
    private  List<ResponseQryNoSettleVo> qryPvCgList(Map<String,Object> paramMap){
        Map<String,Object> tempmap=new HashMap<>();
        String curtime = DateUtils.getDateTimeStr(new Date());
        tempmap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
        tempmap.put("notDisplayFlagPv", "0");
        tempmap.put("isNotShowPv", "0");
        tempmap.put("pkOrg",UserContext.getUser().getPkOrg());
        tempmap.put("pkPv",paramMap.get("pkPv"));
        tempmap.put("pkPi",paramMap.get("pkPi"));
        List<BlPatiCgInfoNotSettleVO> mapResult= opcgPubHelperService.queryPatiCgInfoNotSettle(tempmap);

        if(mapResult==null ||mapResult.size()==0){
            return null;
        }else{
            List<ResponseQryNoSettleVo> settleVos=new ArrayList<ResponseQryNoSettleVo>();
            for(BlPatiCgInfoNotSettleVO notSettleVO:mapResult){
                ResponseQryNoSettleVo settleVo=new ResponseQryNoSettleVo();
                settleVo.setNamePi(MapUtils.getString(paramMap,"namePi"));
                settleVo.setCodePi(MapUtils.getString(paramMap,"codePi"));
                settleVo.setCodePv(MapUtils.getString(paramMap,"codePv"));
                settleVo.setCodeOp(MapUtils.getString(paramMap,"codeOp"));
                settleVo.setOpTimes(MapUtils.getInteger(paramMap,"opTimes"));
                settleVo.setDatePv(MapUtils.getString(paramMap,"datePv"));
                //第三方医保身份信息
                settleVo.setDtExthp(CommonUtils.getPropValueStr(paramMap, "dtExthp"));
                settleVo.setNameExthp(CommonUtils.getPropValueStr(paramMap, "nameExthp"));

                settleVo.setCreatDocterNo(notSettleVO.getCreatDocterNo());
                settleVo.setCreatDocterName(notSettleVO.getNameEmpPhy());

                settleVo.setPresNo(notSettleVO.getPresNo()==null?"":notSettleVO.getPresNo());
                settleVo.setItemCode(notSettleVO.getItemCode());
                settleVo.setNameOrd(notSettleVO.getNameCg());
                settleVo.setNameBill(notSettleVO.getNameBill());
                settleVo.setAmount(notSettleVO.getAmountPi());
                settleVo.setOrdsn(CommonUtils.getString(notSettleVO.getOrdsn()));
                settleVo.setUnit(notSettleVO.getUnit());
                settleVo.setPrice(notSettleVO.getPrice());
                settleVo.setQuanCg(notSettleVO.getQuanCg());
                settleVo.setSpec(notSettleVO.getSpec());
                settleVo.setNameDeptEx(notSettleVO.getNameDeptEx());
                settleVo.setNameDept(notSettleVO.getNameDept());
                String dateEnter=DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss",notSettleVO.getDateEnter());
                settleVo.setDateEnter(dateEnter);
                settleVo.setPkCgop(notSettleVO.getPkCgop());
                settleVos.add(settleVo);
            }

            return settleVos;
        }

    }

    /**
     * 查询患者信息
     * @param codePi
     * @param codePv
     * @return
     */
    private List<Map<String,Object>> qryPipvInfo(String codePi,String codePv){
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("codePv",codePv);
        paramMap.put("codePi",codePi);

        List<Map<String,Object>>  resList=zsrmSelfAppMapper.qryPvdataByPi(paramMap);
        return resList;
    }

    /**
     * 保存第三方交互日志
     * @param nameSys
     * @param inputParam
     * @param outParam
     * @param euStatus
     */
    private void saveSysLogInterface(String nameSys,String inputParam,String outParam,String euStatus){
        SysLogInterface log =new SysLogInterface();
        log.setPkLogIntf(NHISUUID.getKeyId());
        log.setInParam(inputParam);
        log.setOutParam(outParam);
        log.setType(euStatus);
        log.setNameIntf("ZSRMZZJ_"+nameSys);
        log.setCreateTime(new Date());
        log.setTs(new Date());
        DataBaseHelper.insertBean(log);
    }

    /**
     * 成功消息封装
     * @param hisJson
     * @return
     */
    private String successJson(String hisJson){
        String result="{\"resourceType\":\"Bundle\",\"id\":\"%s\",\"type\":\"message\",\"timestamp\":\"%s\",\"entry\":[{\"response\":{\"staus\":\"AA\",\"outcome\":{\"resourceType\":\"OperationOutcome\",\"issue\":%s}}}]}\n";
        result=String.format(result,NHISUUID.getKeyId(),DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),hisJson);
        return result;

    }

    /**
     * 失败消息分装
     * @param message
     * @return
     */
    private String errorJson(String message){
        String result="{\"resourceType\":\"Bundle\",\"id\":\"%s\",\"type\":\"message\",\"timestamp\":\"%s\",\"entry\":[{\"response\":{\"staus\":\"AE\",\"outcome\":{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"informational\",\"diagnostics\":\"%s\"}]}}}]}";
        result=String.format(result,NHISUUID.getKeyId(),DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),message);
        return result;

    }

    /**
     * 失败消息分装
     * @param message
     * @return
     */
    private String guideErrorJson(String message){
        String result="{\"resourceType\":\"Bundle\",\"id\":\"%s\",\"type\":\"message\",\"timestamp\":\"%s\",\"entry\":[{\"response\":{\"staus\":\"AC\",\"outcome\":{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"informational\",\"diagnostics\":\"%s\"}]}}}]}";
        result=String.format(result,NHISUUID.getKeyId(),DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),message);
        return result;

    }

    /**
     * 获取人员信息
     * @param codeEmp
     * @return
     */
    private BdOuEmployee getEmpInfo(String codeEmp){
        String sql="select * from bd_ou_employee where code_emp =?";
        BdOuEmployee emp =DataBaseHelper.queryForBean(sql,BdOuEmployee.class,new Object[]{codeEmp});
        if(emp==null){
            throw new BusException("未传入结算人工号，请核实！");
        }
        return emp;
    }

    /**
     * 获取科室信息
     * @param codeDept
     * @return
     */
    private BdOuDept getDeptInfo(String codeDept){
        String sql="select * from bd_ou_dept where code_dept=?";
        BdOuDept dept=DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{codeDept});
        return dept;
    }
    /**
     * 根据机构配置取科室主键
     * 默认本院
     * @param codeDept
     * @return
     */
    private String getPkDeptByConf(String pkOrg){
        	if(pkOrg == ZsrmSelfAppConstant.PK_ORG){//本院
        		return ZsrmSelfAppConstant.PK_DEPT_CG;
        	}else if(pkOrg == ZsrmSelfAppConstant.pk_Org_LF){//莲峰
        		return ZsrmSelfAppConstant.pk_Dept_LF;
        	}else {
        		return ZsrmSelfAppConstant.PK_DEPT_CG;
        	}

    }
    
    

}
