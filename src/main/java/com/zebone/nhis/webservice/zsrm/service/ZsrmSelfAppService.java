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
     * ?????????????????????
     * @param param
     * @return
     */
    public String queryPatiCgInfoNotSettle(String param){
        RequestQryOutpfeeMasterVo reqvo = JsonUtil.readValue(param, RequestQryOutpfeeMasterVo.class);
        try {
            String codePi = reqvo.getCodePi();
            String codePv = reqvo.getCodePv();
            if (CommonUtils.isEmptyString(codePi)) {
                String resJson=errorJson("?????????[codePi]??????ID???");
                return resJson;
            }

            List<Map<String, Object>> resPvList = qryPipvInfo(codePi, codePv);
            if (resPvList == null || resPvList.size() == 0) {
                String resJson=errorJson("???????????????????????????????????????");
                return resJson;
            }
            User user=new User();
            user.setPkEmp(ZsrmSelfAppConstant.PK_EMP_CG);//--?????????
            user.setNameEmp(ZsrmSelfAppConstant.NAME_EMP);//?????????
            user.setPkOrg(ZsrmSelfAppConstant.PK_ORG);//??????
            user.setPkDept(ZsrmSelfAppConstant.PK_DEPT_CG);//????????????
            UserContext.setUser(user);

            List<ResponseQryNoSettleVo> resNosettleList = new ArrayList<>();

            if (CommonUtils.isEmptyString(codePv)) {//?????????????????? ????????????????????????
                for (Map<String, Object> map : resPvList) {
                    List<ResponseQryNoSettleVo> tempSettles = qryPvCgList(map);
                    if (tempSettles != null) {
                        resNosettleList.addAll(tempSettles);
                    }
                }
            } else {//?????? ??????????????????????????? ?????????????????????????????????
                List<ResponseQryNoSettleVo> tempSettles = qryPvCgList(resPvList.get(0));
                if (tempSettles != null) {
                    resNosettleList.addAll(tempSettles);
                }
            }


            String dateNow = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            CommonResSelfVo<List<ResponseQryNoSettleVo>> response = new CommonResSelfVo<List<ResponseQryNoSettleVo>>(
                    "1", "?????????", reqvo.getReqId(), dateNow, resNosettleList);
            String hisjson=JsonUtil.writeValueAsString(response);
            hisjson="{\"data\":"+JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data")) + "}";
            String resJson=successJson(hisjson);
            return resJson;
        }catch (Exception e){
            e.printStackTrace();
            String resJson=errorJson("HIS???????????????");
            return resJson;
        }
    }

    /**
     * ????????????(??????+??????)
     * @param param
     * @return
     */
    public String saveSettleForSelf(String param) {
        //????????????????????? , ????????????????????????
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        TransactionSynchronizationManager.setActualTransactionActive(true);
        def.setName(this.getClass().getName()+NHISUUID.getKeyId());
        TransactionSynchronizationManager.setCurrentTransactionName(def.getName());
        String ybPkSettle = "";
        try {
            //1.???????????????????????????????????????codePv,tradeNo,amountExt,dataList???????????????
            RequestSelfAppSettleVo request = JsonUtil.readValue(param, RequestSelfAppSettleVo.class);
            String msg = checkReqSettleParam(request);
            if(StringUtils.isNotBlank(msg)){
                String resJson=errorJson(msg);
                return resJson;
            }

            //????????????
            String transNo=request.getTradeNo();
            String sqlExt="select pk_settle from bl_ext_pay where TRADE_NO='"+transNo+"' and ROWNUM=1";
            Map<String, Object> forMap=DataBaseHelper.queryForMap(sqlExt);
            if(forMap!=null){
                String resJson=createSelfSettleResult(request,param,MapUtils.getString(forMap,"pkSettle"));
                return resJson;
            }
            //2.????????????????????????
            String sql="select * from pv_encounter pv where code_pv=?";
            PvEncounter pvinfo=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{request.getCodePv()});
            if(pvinfo==null){
                String resJson=errorJson("???????????????????????????????????????");
                return resJson;
            }
            //?????????????????????????????????????????????????????????????????????????????????
            String pkInsu = new String(pvinfo.getPkInsu());//???????????????

            //3.User????????????
            User user=new User();
            BdOuEmployee emp =getEmpInfo(request.getCodeEmpSt());
            BdOuDept dept=getDeptInfo(request.getCodeDeptSt());
            user.setPkEmp(emp.getPkEmp());//--?????????
            user.setCodeEmp(emp.getCodeEmp());
            user.setNameEmp(emp.getNameEmp());//?????????
            user.setPkOrg(pvinfo.getPkOrg());//??????
            //user.setPkDept(dept!=null?dept.getPkDept():ZsrmSelfAppConstant.PK_DEPT_CG);//????????????
            user.setPkDept(dept!=null?dept.getPkDept():getPkDeptByConf(pvinfo.getPkOrg()));//????????????
            UserContext.setUser(user);

            //4.?????????????????????????????????????????????
            if(EnumerateParameter.ZERO.equals(request.getIsHp())) {
            	String hpSql = "select * from BD_HP where del_flag = '0' and eu_hptype ='0' ";
            	BdHp bdHp = DataBaseHelper.queryForBean(hpSql,BdHp.class);
            	updatePkInsu(bdHp.getPkHp(),pvinfo);
            	pvinfo = DataBaseHelper.queryForBean(sql, PvEncounter.class,new Object[]{request.getCodePv()});
            }

            //4.?????????????????????????????????????????????????????????
            Map<String,Object> noSettleParamMap=new HashMap<>();
            String curtime = DateUtils.getDateTimeStr(new Date());
            noSettleParamMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //???????????????????????????
            noSettleParamMap.put("notDisplayFlagPv", "0");
            noSettleParamMap.put("isNotShowPv", "0");
            noSettleParamMap.put("pkPv",pvinfo.getPkPv());
            noSettleParamMap.put("pkPi",pvinfo.getPkPi());

            List<BlPatiCgInfoNotSettleVO> mapResult= opcgPubHelperService.queryPatiCgInfoNotSettle(noSettleParamMap);

            //5.??????????????????????????????????????????????????????????????????????????????????????????
	        if(mapResult==null || mapResult.size()==0){
	        	//??????????????????????????????
		    	Map<String, Object> resMap = DataBaseHelper.queryForMap(sqlExt);
		        if(resMap!=null){
		        	//???????????????????????????????????????????????????????????????
		            if(EnumerateParameter.ZERO.equals(request.getIsHp())) {
		            	platformTransactionManager.rollback(status);
		            }
		            String resJson=createSelfSettleResult(request,param,MapUtils.getString(forMap,"pkSettle"));
		            return resJson;
		        }else {
		        	throw new BusException("????????????????????????????????????");
		        }
            }
            Map<String,Object> tempResMap=getHisAmountSum(request,mapResult);
            BigDecimal amountHisSum= (BigDecimal)tempResMap.get("amountHisSum");
            BigDecimal amountOrder=new BigDecimal(request.getAmountSt()).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal amountPi = new BigDecimal(request.getAmountPi()).setScale(2,BigDecimal.ROUND_HALF_UP);
            if(amountOrder.compareTo(amountHisSum)!=0){
            	throw new BusException("???????????????HIS?????????????????????????????????????????????");
            }
            if(EnumerateParameter.ONE.equals(request.getIsHp())) {
            	ReqYbPreSettleInfovo ybPreSettleInfo = request.getYbPreSettleInfo();
            	if(amountHisSum.compareTo(ybPreSettleInfo.getAmount())!=0){
                    throw new BusException("????????????????????????????????????HIS???????????????????????????????????????");
                }
            }else {
            	if(amountPi.compareTo(amountHisSum)!=0){
            		throw new BusException("????????????????????????????????????HIS?????????????????????????????????????????????");
                }
            }

            //7.?????????????????????
            List<BlPatiCgInfoNotSettleVO> tempSetvoList=(List<BlPatiCgInfoNotSettleVO> )tempResMap.get("tempSetvoList");
            String mapResultJson=JsonUtil.writeValueAsString(tempSetvoList);
            List<BlOpDt> opDts = JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});
            ApplicationUtils apputil = new ApplicationUtils();
            OpCgTransforVo opCgforVo = new OpCgTransforVo();
            opCgforVo = settleWillSelf(request, pvinfo, user, amountHisSum,amountPi, opDts, apputil, opCgforVo);

            //8.??????????????????
            InsQgybSt insQgybSt = null;
            if(EnumerateParameter.ONE.equals(request.getIsHp())) {
            	if(EnumerateParameter.ONE.equals(request.getIsHpOnline())){//????????????????????????
            		insQgybSt = saveYbSettleInfo(request.getSetlinfo(),request.getSetldetail(),pvinfo,user,apputil);
            	}
            	else{//?????????????????????
             		insQgybSt = (InsQgybSt)ybSettleMethod(request, pvinfo,apputil,user);
            	}
            	ybPkSettle = insQgybSt.getSetlId();
            	
            }
            //9.??????????????????
            opCgforVo = getSettltForSelfOpCgTransforVo(request, pvinfo, amountPi,user,opDts, apputil, opCgforVo);

            //10.?????????????????????????????????????????????
            if(EnumerateParameter.ZERO.equals(request.getIsHp())) {
            	updatePkInsu(pkInsu,pvinfo);
            }
            platformTransactionManager.commit(status);

            //11.????????????
        	unlockPi(def,pvinfo.getPkPi(),apputil,user);

            //12.?????????????????????HIS??????????????????
            if(EnumerateParameter.ONE.equals(request.getIsHp())) {
                hisYbRelationship(def,pvinfo.getPkPv(),ybPkSettle,opCgforVo.getPkSettle(),apputil,user);
            }

            //13.????????????????????????
            updateExt(def,opCgforVo.getPkSettle());

            //14.??????????????????
            getElectronicBill(def,user,opCgforVo);

            //15.?????????????????????????????????
            String resJson=createSelfSettleResult(request,param,opCgforVo.getPkSettle());
            return resJson;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("??????????????????????????????????????????"+ ex.getMessage());
            platformTransactionManager.rollback(status);
            //?????????????????????????????????
            if(CommonUtils.isNotNull(ybPkSettle)) {
            	 ApplicationUtils apputil = new ApplicationUtils();
            	 String ybCancelStr = ybSettleCancelMethod(ybPkSettle,apputil, UserContext.getUser());
            	 if(CommonUtils.isNotNull(ybCancelStr)) {
            		 log.error("??????????????????????????????????????????"+ ybCancelStr);
            	 }
            }
            String resJson=errorJson("??????????????????????????????????????????"+ ex.getMessage());
            return resJson;
        }
    }

    /**
     * ??????????????????
     *
     * @param pkInsu
     * @param pvencounter
     */
    private void updatePkInsu(String pkInsu, PvEncounter pvencounter) {
        pvencounter.setPkInsu(pkInsu);
        DataBaseHelper.updateBeanByPk(pvencounter);
    }

    /**
     * ????????????
     * @param param
     * @return
     */
    public String getYbWillSettle(String param){
    	//????????????????????? , ????????????????????????
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        try {
            //1.???????????????????????????????????????codePv,tradeNo,amountExt,dataList???????????????
            RequestYbWillSettleVo request = JsonUtil.readValue(param, RequestYbWillSettleVo.class);
            String msg = checkReqWillSettleParam(request);
            if(StringUtils.isNotBlank(msg)){
                String resJson=errorJson(msg);
                return resJson;
            }
            //2.????????????????????????
            String sql = "select * from pv_encounter pv where code_pv=?";
            PvEncounter pvinfo = DataBaseHelper.queryForBean(sql, PvEncounter.class, new Object[]{request.getCodePv()});
            if (pvinfo == null) {
                String resJson=errorJson("???????????????????????????????????????");
                return resJson;
            }
            //3.User????????????
            User user = new User();
            BdOuEmployee emp =getEmpInfo(CommonUtils.isNull(request.getCodeEmpSt())? ZsrmSelfAppConstant.CODE_EMP : request.getCodeEmpSt());
            BdOuDept dept=getDeptInfo(CommonUtils.isNull(request.getCodeDeptSt())? ZsrmSelfAppConstant.CODE_DEPT_CG : request.getCodeDeptSt());
            user.setPkEmp(emp.getPkEmp());//--?????????
            user.setCodeEmp(emp.getCodeEmp());
            user.setNameEmp(emp.getNameEmp());//?????????
            user.setPkOrg(ZsrmSelfAppConstant.PK_ORG);//??????
            //user.setPkDept(dept!=null?dept.getPkDept():ZsrmSelfAppConstant.PK_DEPT_CG);//????????????
            user.setPkDept(dept!=null?dept.getPkDept():getPkDeptByConf(pvinfo.getPkOrg()));//????????????
            UserContext.setUser(user);

            //4.?????????????????????????????????????????????????????????
            Map<String,Object> noSettleParamMap=new HashMap<>();
            String curtime = DateUtils.getDateTimeStr(new Date());
            noSettleParamMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //???????????????????????????
            noSettleParamMap.put("notDisplayFlagPv", "0");
            noSettleParamMap.put("isNotShowPv", "0");
            noSettleParamMap.put("pkPv",pvinfo.getPkPv());
            noSettleParamMap.put("pkPi",pvinfo.getPkPi());

            List<BlPatiCgInfoNotSettleVO> mapResult= opcgPubHelperService.queryPatiCgInfoNotSettle(noSettleParamMap);
            //5.??????????????????????????????????????????????????????????????????????????????????????????
            if(mapResult==null || mapResult.size()==0){
                String resJson=errorJson("????????????????????????????????????");
                return resJson;
            }

            List<BlPatiCgInfoNotSettleVO> tempCgList=getHisYbWillCgList(request,mapResult);

            List<String> pkCgops=new ArrayList<>();
            tempCgList.forEach(m->{pkCgops.add(m.getPkCgop());});
            Map<String,Object> ybWillDate = ybWillSettleMethod(pkCgops,pvinfo,user);
            //??????????????????????????????
        	String resJson = createYbWillSettleResult(request,param,tempCgList,ybWillDate);
        	platformTransactionManager.commit(status);
        	return resJson;
        }catch (Exception e){
        	platformTransactionManager.rollback(status);
            String resJson=errorJson("?????????????????????????????????????????????"+e.getMessage());
            return resJson;
        }

    }

    /**
     * ??????????????????
     * @param param
     * @return
     */
    public String appointNuc(String param) {
    	//????????????????????? , ????????????????????????
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        try {
            //1.???????????????????????????????????????codePi???
        	RequestAppointNucVo request = JsonUtil.readValue(param, RequestAppointNucVo.class);
            if (request == null)  {
                String resJson=errorJson("????????????????????????NULL");
                return resJson;
            }
            if (CommonUtils.isNull(request.getPatientId())) {
                String resJson=errorJson("?????????[patientId]??????Id!");
                return resJson;
            }
            //????????????
            Date appointMentDate  = new Date();
            String appointMentStr = request.getAppointMentDate();
            if(CommonUtils.isNotNull(appointMentStr)) {
            	appointMentDate = DateUtils.strToDate(appointMentStr, "yyyy-MM-dd");
            }
            //2.????????????
            String sql = "select * from pi_master pi where code_op=?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql, PiMaster.class, new Object[]{request.getPatientId()});
            if (piMaster == null) {
                String resJson=errorJson("??????patientId???"+request.getPatientId()+"??????????????????????????????");
                return resJson;
            }
            //3.1 ???????????????????????????????????????????????????????????????????????????????????????????????????
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

            //3.User????????????
            User user = new User();
            BdOuEmployee emp =getEmpInfo(ZsrmSelfAppConstant.CODE_EMP_NUC);
            BdOuDept dept=getDeptInfo(ZsrmSelfAppConstant.CODE_DEPT_NUC);
            user.setPkEmp(emp.getPkEmp());//--?????????
            user.setCodeEmp(emp.getCodeEmp());
            user.setNameEmp(emp.getNameEmp());//?????????
            user.setPkOrg(ZsrmSelfAppConstant.PK_ORG);//??????
            user.setPkDept(dept!=null?dept.getPkDept():ZsrmSelfAppConstant.PK_DEPT_NUC);//??????????????????
            UserContext.setUser(user);

       		String hpSql = "select * from BD_HP where del_flag = '0' and eu_hptype ='0' ";
        	BdHp bdHp = DataBaseHelper.queryForBean(hpSql,BdHp.class);

            //4.??????????????????
            String pkPv = NHISUUID.getKeyId();
            PvEncounter pv = savePvEncounter(piMaster, pkPv,bdHp,appointMentDate,user);

            //5.?????????????????????????????????pv_pe
            savePvPe(piMaster,pv,appointMentDate,user);

            //6.??????????????????
            String codeApply = saveOrder(piMaster, pv,bdHp,dept,appointMentDate,user);

            //7.??????????????????????????????
        	String resJson = createAppointNucResult(request,piMaster,pv,codeApply);
        	platformTransactionManager.commit(status);
        	return resJson;
        }catch (Exception e){
        	platformTransactionManager.rollback(status);
            String resJson=errorJson("??????????????????????????????????????????"+e.getMessage());
            return resJson;
        }
    }

    /**
     * ??????????????????????????????
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
                String resJson=errorJson("?????????[patientid]??????ID???");
                return resJson;
            }
            if (CommonUtils.isEmptyString(indate)) {
                String resJson=errorJson("?????????[indate]?????????????????????");
                return resJson;
            }
            if (CommonUtils.isEmptyString(outdate)) {
                String resJson=errorJson("?????????[outdate]?????????????????????");
                return resJson;
            }
            List<ResponseQryOutpfeeMasterVo> outfee = zsrmSelfAppMapper.qryOutpfeeMasterInfo(patientid,indate,outdate);

            String dateNow = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            CommonResSelfVo<List<ResponseQryOutpfeeMasterVo>> response = new CommonResSelfVo<List<ResponseQryOutpfeeMasterVo>>(
                    "1", "?????????", reqvo.getReqId(), dateNow, outfee);
            String hisjson=JsonUtil.writeValueAsString(response);
            hisjson="{\"outfee\":"+JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data")) + "}";
            String resJson=successJson(hisjson);
            return resJson;
        }catch (Exception e){
            e.printStackTrace();
            String resJson=errorJson("HIS???????????????");
            return resJson;
        }
	}

	/**
     * ??????????????????????????????
     * @param param
     * @return
     */
	public String queryOutpfeeDetailInfo(String param) {
		RequestQryOutpfeeDetailVo reqvo = JsonUtil.readValue(param, RequestQryOutpfeeDetailVo.class);
		try {
            String patientid = reqvo.getPatientid();
            String codeSt = reqvo.getReceiptNo();

            if (CommonUtils.isEmptyString(patientid)) {
                String resJson=errorJson("?????????[patientid]??????ID???");
                return resJson;
            }
            if (CommonUtils.isEmptyString(codeSt)) {
                String resJson=errorJson("?????????[receiptNo]????????????");
                return resJson;
            }
            List<ResponseQryOutpfeeDetailVo> outfeedetail = zsrmSelfAppMapper.qryOutpfeeDetailInfo(patientid,codeSt);

            String dateNow = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            CommonResSelfVo<List<ResponseQryOutpfeeDetailVo>> response = new CommonResSelfVo<List<ResponseQryOutpfeeDetailVo>>(
                    "1", "?????????", reqvo.getReqId(), dateNow, outfeedetail);
            String hisjson=JsonUtil.writeValueAsString(response);
            hisjson="{\"outfee\":"+JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data")) + "}";
            String resJson=successJson(hisjson);
            return resJson;
        }catch (Exception e){
            e.printStackTrace();
            String resJson=errorJson("HIS???????????????");
            return resJson;
        }
	}

    /**
     * ?????????????????????????????????+?????????
     * @param request
     * @param param
     * @param pkSettle
     * @return
     */
    private String createSelfSettleResult(CommonReqSelfVo request,String param,String pkSettle){
        //??????pkSettle ????????????????????????????????????
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
            response.setDiagnostics("?????????");
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
                    "1", "?????????", request.getReqId(), dateNow, response);
            String hisjson=JsonUtil.writeValueAsString(resSelfVo);
            hisjson= "{\"data\":"+JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data")) + "}";
            String resJson=successJson(hisjson);
            return resJson;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("??????????????????????????????"+ ex.getMessage());
            String resJson=guideErrorJson("??????????????????????????????"+ ex.getMessage());
            return resJson;
        }
    }
    /**
     * ?????????????????????????????????
     * @param request
     * @param param
     * @return
     */
    private String createYbWillSettleResult(RequestYbWillSettleVo request,String param,List<BlPatiCgInfoNotSettleVO> tempCgList,Map<String, Object> ybWillData){
        String  dateNow= DateUtils.getDate("yyyy-MM-dd HH:mm:ss");

        //his?????????
        BigDecimal hisAmount = BigDecimal.ZERO;
        for (BlPatiCgInfoNotSettleVO tempCg : tempCgList) {
        	hisAmount = hisAmount.add(tempCg.getAmount());
		}
        OutParamHuaJia ybPreSettle = (OutParamHuaJia)ybWillData.get("ybPreSettleInfo");
        BigDecimal ybAmount = BigDecimal.valueOf(ybPreSettle.getAmount() == null ? 0d :ybPreSettle.getAmount());//????????????????????????
        BigDecimal jjzf = BigDecimal.valueOf(ybPreSettle.getAmtJjzf() == null ? 0d : ybPreSettle.getAmtJjzf());//???????????????????????????
        BigDecimal grzf = BigDecimal.valueOf(ybPreSettle.getAmtGrzf() == null ? 0d : ybPreSettle.getAmtGrzf());//???????????????????????????

        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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

        BigDecimal grzhzf = BigDecimal.ZERO;//?????????????????????????????????????????????
        BigDecimal hpCateBalance  = request.getHpCateBalance() == null ? BigDecimal.ZERO : request.getHpCateBalance();//??????????????????
        //??????????????????????????????????????????
        if (hpCateBalance.compareTo(BigDecimal.ZERO) == 1) {
        	if(hpCateBalance.compareTo(grzf) == -1) {//????????????????????????(?????????????????????????????????????????????????????????)
        		grzhzf = hpCateBalance;
        		grzf = grzf.subtract(grzhzf);
        	}else  {
        		//????????????????????????
        		grzhzf = grzf;
        		grzf = BigDecimal.ZERO;
        	}
        }

        //??????????????????
        ResponseYbWillSettleVo response =new ResponseYbWillSettleVo();
        response.setDiagnostics("?????????");
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
        //??????HIS????????????????????????
        if(ybPreSettleInfo.getAmount().subtract(ybPreSettleParam.getMedfeeSumamt()).compareTo(BigDecimal.ZERO) == 1){
            ybPreSettleInfo.setCashAddFee(ybPreSettleInfo.getAmount().subtract(ybPreSettleParam.getMedfeeSumamt()));
        }else{
        	ybPreSettleInfo.setCashAddFee(BigDecimal.ZERO);
        }
        response.setYbPreSettleInfo(ybPreSettleInfo);
        			

        CommonResSelfVo<ResponseYbWillSettleVo> resSelfVo=new CommonResSelfVo<ResponseYbWillSettleVo>(
                "1", "?????????", request.getReqId(), dateNow, response);
        String hisjson=JsonUtil.writeValueAsString(resSelfVo);
        hisjson=JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data"));
        String resJson=successJson(hisjson);
        return resJson;
    }

    /**
     * ????????????????????????????????????
     * @param request
     * @param param
     * @param pkSettle
     * @return
     */
    private String createAppointNucResult(CommonReqSelfVo request,PiMaster master,PvEncounter pv,String codeApply){
        String  dateNow= DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        //??????????????????
        ResponseAppointNucVo response =new ResponseAppointNucVo();
        response.setCode("infomation");
        response.setSeverity("infomation");
        response.setDiagnostics("?????????");
        response.setCodePv(pv.getCodePv());
        response.setApplyNo(codeApply);
        CommonResSelfVo<ResponseAppointNucVo> resSelfVo=new CommonResSelfVo<ResponseAppointNucVo>(
                "1", "?????????", request.getReqId(), dateNow, response);
        String hisjson=JsonUtil.writeValueAsString(resSelfVo);
        hisjson=JsonUtil.writeValueAsString(JsonUtil.getJsonNode(hisjson,"data"));
        String resJson=successJson(hisjson);
        return resJson;
    }

    /**
     * ?????????????????????
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
            throw new BusException("??????HIS????????????????????????"+response.getDesc());
        }
        return  (Map<String,Object>)response.getData();
    }

    /**
     * ??????????????????
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
            throw new BusException("??????HIS?????????????????????"+response.getDesc());
        }
        return  response.getData();
    }

    /**
     * ????????????????????????
     * @param request
     * @param pvinfo
     * @return
     */
    private String ybSettleCancelMethod(String ybPkSettle, ApplicationUtils apputil,User user){
        Map<String,Object> hpSettleCancelMap=new HashMap<>();
        hpSettleCancelMap.put("ybPkSettle",ybPkSettle);
        ResponseJson  response = apputil.execService("COMPAY","ZsrmQGService","mzHpSetttleCancel",hpSettleCancelMap,user);
        if(response.getStatus() != 0){
            return "???????????????????????????"+response.getDesc();
        }
        return "";
    }

    /**
   	 * ??????????????????
   	 * @param master
   	 * @param pkPv
   	 * @return
   	 */
   	private PvEncounter savePvEncounter(PiMaster master,String pkPv,BdHp bdHp,Date appointMentDate,User user){
   		// ??????????????????
   		PvEncounter pvEncounter = new PvEncounter();
   		pvEncounter.setPkPv(pkPv);
   		pvEncounter.setPkPi(master.getPkPi());
   		pvEncounter.setPkDept(user.getPkDept());//????????????
   		pvEncounter.setCodePv(ApplicationUtils.getCode("0301"));
   		pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_4);// ??????
   		pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_2); // ??????
   		pvEncounter.setNamePi(master.getNamePi());
   		pvEncounter.setDtSex(master.getDtSex());
   		pvEncounter.setAgePv(ApplicationUtils.getAgeFormat(master.getBirthDate(),null));
   		pvEncounter.setAddress(master.getAddress());
   		pvEncounter.setFlagIn("0");
   		pvEncounter.setFlagSettle("1");
   		pvEncounter.setDtMarry(master.getDtMarry());
   		pvEncounter.setPkInsu(bdHp.getPkHp());//????????????
   		pvEncounter.setPkPicate(master.getPkPicate());
   		pvEncounter.setPkEmpReg(user.getPkEmp());
   		pvEncounter.setNameEmpReg(user.getNameEmp());
   		pvEncounter.setPkEmpPhy(user.getPkEmp());
   		pvEncounter.setNameEmpPhy(user.getNameEmp());
   		pvEncounter.setDateBegin(appointMentDate);//??????????????????
   		pvEncounter.setDateReg(appointMentDate);//?????????????????????
   		pvEncounter.setDateClinic(appointMentDate);//????????????
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
   	 * ??????????????????
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
   	 * ??????????????????
   	 * @param master
   	 * @param pkPv
   	 * @return
   	 */
   	public String saveOrder(PiMaster master,PvEncounter pv,BdHp bdhp,BdOuDept dept,Date appointMentDate,User user){
   		ApplicationUtils apputil = new ApplicationUtils();
		// ??????????????????
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

   		//??????????????????????????????
   		String sql = "select ord.*,org.price,lab.dt_contype,lab.dt_colltype,lab.dt_samptype,ris.dt_body,ris.dt_type,ris.desc_att "
   				+ " from bd_ord ord inner join bd_ord_org org on org.pk_ord = ord.pk_ord "
   				+ " left join bd_ord_lab lab on lab.pk_ord = ord.pk_ord and lab.del_flag = '0' "
   				+ " left join bd_ord_ris ris on ris.pk_ord = ord.pk_ord and ris.del_flag = '0' "
   				+ " where ord.del_flag='0' and ord.code='" + ZsrmSelfAppConstant.CODE_ORD + "'";
   		Map<String,Object> bdOrdMap = DataBaseHelper.queryForMap(sql);
   		if(bdOrdMap == null) {
   			throw new BusException("?????????????????????????????????????????????code_ord???"+ZsrmSelfAppConstant.CODE_ORD+"??????????????????????????????");
   		}
   		//???????????????
   		Map<String, String> ordsnMap = new HashMap<String, String>();
   		ordsnMap.put("tableName", "CN_ORDER");
   		ordsnMap.put("fieldName", "ORDSN");
   		ordsnMap.put("count", "1");
   		ResponseJson resOrdsn =apputil.execService("CN", "BdSnService", "getSerialNo", ordsnMap,user);
   		if(resOrdsn.getStatus() != 0){
            throw new BusException("???????????????????????????"+resOrdsn.getDesc());
        }
   		int ordsn = (int)resOrdsn.getData();
   		//??????????????????
   		String codeApply = "";
   		String codeOrdtype = CommonUtils.getPropValueStr(bdOrdMap, "codeOrdtype");
   		if(CommonUtils.isNotNull(codeOrdtype)) {
   			String subCode = codeOrdtype.substring(0, 2);
   			if(IOrdTypeCodeConst.DT_ORDTYPE_EXAMINE.equals(subCode)) {
   				//??????
   				codeApply = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_JCSQD);
   			}else if(IOrdTypeCodeConst.DT_ORDTYPE_TEST.equals(subCode)) {
   				//??????
   				codeApply = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_JYSQD);
   			}
   		}

   		//??????????????????????????????
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
   		modifyMap.put("dtHpprop", EnumerateParameter.ZERO.equals(bdhp.getEuHptype()) ? "01" : "11");//????????????????????????
   		modifyMap.put("purpose", "");
   		modifyMap.put("pkPi", master.getPkPi());
   		modifyMap.put("pkPv", pv.getPkPv());
   		modifyMap.put("euPvtype", pv.getEuPvtype());
   		String opDateEffe = ApplicationUtils.getSysparam("CN0004", false);//??????????????????
   		if(CommonUtils.isNull(opDateEffe)) {
   			opDateEffe = EnumerateParameter.ONE;
   		}
   		Calendar calendar = Calendar.getInstance();
   		calendar.setTime(appointMentDate);
   		calendar.add(Calendar.DATE,Integer.valueOf(opDateEffe)); //?????????????????????,???????????????,??????????????????
   		Date dateEffe = calendar.getTime(); //???????????????
   		modifyMap.put("dateEffe", dateEffe);//??????????????????
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
            throw new BusException("???????????????????????????"+response.getDesc());
        }
        return codeApply;
	}

   	/**
   	 * ????????????
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
	        	//??????????????????????????????
	            log.error("????????????????????????????????????"+ response.getDesc());
	            platformTransactionManager.rollback(status);
	            return;
	        }
	        platformTransactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("???????????????????????????????????????????????????????????????"+ e.getMessage());
            platformTransactionManager.rollback(status);
        }
   	}

  	/**
   	 * ?????????????????????HIS??????????????????
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
            	//??????????????????????????????
                log.error("HIS????????????????????????????????????"+ response.getDesc());
                platformTransactionManager.rollback(status);
	            return;
            }
	        platformTransactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("??????????????????????????????????????????HIS?????????????????????????????????"+ e.getMessage());
            platformTransactionManager.rollback(status);
        }
   	}

   	/**
   	 * ????????????????????????
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
            log.error("????????????????????????????????????????????????????????????"+ e.getMessage());
            platformTransactionManager.rollback(status);
        }
   	}

    /**
     * ????????????
     * @param param
     * @return
     */
    /*
    public String ybSettlemethod(String param){
        //????????????????????? , ????????????????????????
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        try {
            //1.???????????????????????????????????????codePv,tradeNo,amountExt,dataList???????????????
            RequestSelfAppSettleVo request = JsonUtil.readValue(param, RequestSelfAppSettleVo.class);
            String msg = checkReqSettleParam(request);
            if(StringUtils.isNotBlank(msg)){
                String resJson=errorJson(msg);
                return resJson;
            }
            //2.????????????????????????
            String sql="select * from pv_encounter pv where code_pv=?";
            PvEncounter pvinfo=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{request.getCodePv()});
            if(pvinfo==null){
                String resJson=errorJson("???????????????????????????????????????");
                return resJson;
            }

            //3.User????????????
            User user=new User();
            String empSql="select PK_EMP from BD_OU_EMPLOYEE where CODE_EMP=?";
            Map<String,Object> empMap=DataBaseHelper.queryForMap(empSql,new Object[]{request.getCodeEmpSt()});
            String deptSql="select PK_DEPT from BD_OU_DEPT where CODE_DEPT=?";
            Map<String,Object> deptMap=DataBaseHelper.queryForMap(deptSql,new Object[]{request.getCodeDeptSt()});
            user.setPkEmp(MapUtils.getString(empMap,"pkEmp",ZsrmSelfAppConstant.PK_EMP_CG));//--?????????
            user.setNameEmp(request.getNameEmpSt());//?????????
            user.setPkOrg(ZsrmSelfAppConstant.PK_ORG);//??????
            user.setPkDept(MapUtils.getString(deptMap,"pkDept",ZsrmSelfAppConstant.PK_DEPT_CG));//????????????
            UserContext.setUser(user);

            //4.?????????????????????????????????????????????????????????
            Map<String,Object> noSettleParamMap=new HashMap<>();
            String curtime = DateUtils.getDateTimeStr(new Date());
            noSettleParamMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //???????????????????????????
            noSettleParamMap.put("notDisplayFlagPv", "0");
            noSettleParamMap.put("isNotShowPv", "0");
            noSettleParamMap.put("pkPv",pvinfo.getPkPv());
            noSettleParamMap.put("pkPi",pvinfo.getPkPi());

            List<BlPatiCgInfoNotSettleVO> mapResult= opcgPubHelperService.queryPatiCgInfoNotSettle(noSettleParamMap);

            //5.??????????????????????????????????????????????????????????????????????????????????????????
            if(mapResult==null || mapResult.size()==0){
                String resJson=errorJson("????????????????????????????????????");
                return resJson;
            }

            Map<String,Object> tempResMap=getHisAmountSum(request,mapResult);
            BigDecimal amountHisSum= (BigDecimal)tempResMap.get("amountHisSum");
            BigDecimal amountOrder=new BigDecimal(request.getAmountSt()).setScale(2,BigDecimal.ROUND_HALF_UP);
            if(amountOrder.compareTo(amountHisSum)!=0){
                String resJson=errorJson("??????????????????HIS??????????????????????????????????????????");
                return resJson;
            }

            //6.??????????????????
            ApplicationUtils apputil = new ApplicationUtils();
            Map<String,Object> ybResSetDatavo=(Map<String,Object>)ybSettleMethod(request, pvinfo,apputil,user);

            //7.??????HIS????????????
            OpCgTransforVo opCgVo = settleHisForYb(request, pvinfo, user, tempResMap, amountHisSum, apputil, ybResSetDatavo);
            platformTransactionManager.commit(status);

            //8.??????????????????
            if("1".equals(request.getIsElecTicket())) {
                getElectronicBill(def,user,opCgVo);
            }
            //TODO 9.?????????????????????????????????
            String resJson = createSelfSettleResult(request,param,opCgVo.getPkSettle());
            return resJson;
        }catch (Exception e){
            String resJson=errorJson("??????????????????,???????????????"+e.getMessage());
            return resJson;
        }

    }
    */
    /**
     * HIS????????????-??????
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
        String amtPiStr= MapUtils.getString(ybResSetDatavo,"amtGrzf");//????????????
        BigDecimal amtPi=new BigDecimal(amtPiStr).setScale(2, RoundingMode.HALF_UP);

        List<BlPatiCgInfoNotSettleVO> tempSetvoList=(List<BlPatiCgInfoNotSettleVO> )tempResMap.get("tempSetvoList");
        String mapResultJson= JsonUtil.writeValueAsString(tempSetvoList);
        List<BlOpDt> opDts=JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});

        OpCgTransforVo opCgforVo=new OpCgTransforVo();
        opCgforVo.setPkPv(pvinfo.getPkPv());
        opCgforVo.setPkPi(pvinfo.getPkPi());
        opCgforVo.setBlOpDts(opDts);
        opCgforVo.setInvStatus("-2");//??????-???????????????????????????

        //??????????????????
        List<BlDeposit> depositList = new ArrayList<BlDeposit>();
        BlDeposit deposit=new BlDeposit();
        deposit.setDtPaymode(request.getEuPayType());//????????????
        deposit.setAmount(amtPi);
        deposit.setFlagAcc("0");
        deposit.setDelFlag("0");
        deposit.setPayInfo(request.getSerialNo());//??????????????????
        depositList.add(deposit);
        opCgforVo.setBlDeposits(depositList);


        BlExtPay extPay=new BlExtPay();
        extPay.setPkExtpay(NHISUUID.getKeyId());
        extPay.setPkOrg(user.getPkOrg());
        extPay.setAmount(BigDecimal.valueOf(request.getAmountPi()));
        extPay.setFlagPay("1");//????????????
        String date = request.getDateOrder();
        extPay.setDateAp(DateUtils.strToDate(date));//????????????
        extPay.setDatePay(DateUtils.strToDate(date));//????????????
        extPay.setSerialNo(request.getSerialNo());//?????????
        extPay.setTradeNo(request.getTradeNo());//?????????
        extPay.setSysname(request.getSysname());//????????????
        extPay.setDescPay("");//
        extPay.setResultPay(request.getResultPay());
        extPay.setEuPaytype(request.getEuPayType());
        extPay.setCreator(user.getPkEmp());
        extPay.setCreateTime(new Date());
        extPay.setTs(new Date());
        extPay.setDelFlag("0");
        extPay.setDtBank(request.getDtBank());//TODO ??????
        List<BlExtPay> expayList=new ArrayList<BlExtPay>();
        expayList.add(extPay);
        opCgforVo.setBlExtPays(expayList);

        opCgforVo.setAggregateAmount(amountHisSum); //??????????????????
        opCgforVo.setMedicarePayments(amountHisSum.subtract(amtPi));//????????????
        opCgforVo.setPatientsPay(amtPi);//????????????
        opCgforVo.setAmtInsuThird(amountHisSum.subtract(amtPi));
        ResponseJson respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
        if(respSettle.getStatus()!=0) {
            throw new BusException("?????????????????????"+respSettle.getErrorMessage());
        }
        return opCgforVo;
    }
   */
    /**
     * ????????????
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
        //????????????
        ReqYbPreSettleInfovo ybPreSettleInfo = request.getYbPreSettleInfo();
        if(EnumerateParameter.ONE.equals(request.getIsHp())) {
        	opCgforVo.setAggregateAmount(ybPreSettleInfo.getAmount()); //??????????????????
        	opCgforVo.setXjzf(ybPreSettleInfo.getAmtGrzf().add(ybPreSettleInfo.getAmtGrzhzf()));//?????????????????????????????????+???????????????
        	opCgforVo.setAmtInsuThird(ybPreSettleInfo.getAmtJjzf());//????????????
        	opCgforVo.setMedicarePayments(ybPreSettleInfo.getAmtJjzf());//????????????
        	opCgforVo.setYbzf(ybPreSettleInfo.getAmtJjzf());
        }else {
        	//????????????
        	opCgforVo.setAggregateAmount(amountHisSum); //??????????????????
        	//opCgforVo.setMedicarePayments(amountHisSum.subtract(amtPi));//????????????
        	opCgforVo.setPatientsPay(amtPi);//????????????
        }
        //????????????????????? countOpcgAccountingSettlement
        ResponseJson response = apputil.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo,user);
        if(response.getStatus()!=0){
            // TODO ??????????????????????????? -?????????????????????????????????????????????????????????
           throw new BusException("????????????HIS??????????????????????????????"+response.getDesc());
        }
        opCgforVo = (OpCgTransforVo)response.getData();
        if(EnumerateParameter.ONE.equals(request.getIsHp())) {
        	opCgforVo.setAmtInsuThird(ybPreSettleInfo.getAmtJjzf());//????????????
        	opCgforVo.setXjzf(ybPreSettleInfo.getAmtGrzf().add(ybPreSettleInfo.getAmtGrzhzf()));//?????????????????????????????????+???????????????
        }
        return opCgforVo;
    }

    /**
     * ???????????????????????????
     * @param request ????????????
     * @param pvinfo ????????????
     * @param user ????????????
     * @param amountHisSum HIS???????????????
     * @param opDts ????????????
     * @param apputil ????????????
     * @param amtPi ??????
     * @param opCgforVo ?????????????????????
     * @return
     */
    private OpCgTransforVo getSettltForSelfOpCgTransforVo(RequestSelfAppSettleVo request, PvEncounter pvinfo,BigDecimal amtPi, User user,List<BlOpDt> opDts,ApplicationUtils apputil, OpCgTransforVo opCgforVo) {

        opCgforVo.setPkPv(pvinfo.getPkPv());
        opCgforVo.setPkPi(pvinfo.getPkPi());
        opCgforVo.setInvStatus("-2");//??????-???????????????????????????
        opCgforVo.setBlOpDts(opDts);

        //??????????????????
        List<BlDeposit> depositList = new ArrayList<BlDeposit>();

        //????????????
        if(EnumerateParameter.ONE.equals(request.getIsHp())) {
        	ReqYbPreSettleInfovo ybPreSettleInfo = request.getYbPreSettleInfo();
        	if(ybPreSettleInfo.getAmtGrzhzf().compareTo(BigDecimal.ZERO) == 1) {
        		BlDeposit deposit=new BlDeposit();
                deposit.setDtPaymode(ybPreSettleInfo.getGrzhzfPayType());//????????????
                deposit.setAmount(ybPreSettleInfo.getAmtGrzhzf());//??????????????????
                deposit.setFlagAcc("0");
                deposit.setDelFlag("0");
                deposit.setPayInfo(ybPreSettleInfo.getGzSerialNo());//??????????????????
                depositList.add(deposit);

                //????????????????????????
                insertExtPay(ybPreSettleInfo.getGrzhzfPayType(),ybPreSettleInfo.getGzSerialNo(),ybPreSettleInfo.getGzTradeNo(),ybPreSettleInfo.getAmtGrzhzf() ,ybPreSettleInfo.getGzBankCardNo(),request.getDateOrder(),opCgforVo,user);
        	}
        	if(ybPreSettleInfo.getAmtGrzf().compareTo(BigDecimal.ZERO) == 1) {
        		BlDeposit deposit=new BlDeposit();
                deposit.setDtPaymode(request.getEuPayType());//????????????
                deposit.setAmount(ybPreSettleInfo.getAmtGrzf());//??????????????????
                deposit.setFlagAcc("0");
                deposit.setDelFlag("0");
                deposit.setPayInfo(request.getSerialNo());//??????????????????
                depositList.add(deposit);
                //????????????????????????
                insertExtPay(request.getEuPayType(),request.getSerialNo(),request.getTradeNo(),ybPreSettleInfo.getAmtGrzf() ,ybPreSettleInfo.getGzBankCardNo(),request.getDateOrder(),opCgforVo,user);
        	}
        	//???????????????????????????????????????????????????????????????
        	if(depositList.size() == 0) {
        		BlDeposit deposit=new BlDeposit();
                deposit.setDtPaymode(IDictCodeConst.CASH);//????????????
                deposit.setAmount(BigDecimal.ZERO);//??????????????????
                deposit.setFlagAcc("0");
                deposit.setDelFlag("0");
                deposit.setPayInfo(request.getSerialNo());//??????????????????
                depositList.add(deposit);
                //????????????????????????
                insertExtPay(request.getEuPayType(),request.getSerialNo(),request.getTradeNo(),ybPreSettleInfo.getAmtGrzf() ,ybPreSettleInfo.getGzBankCardNo(),request.getDateOrder(),opCgforVo,user);
        	}
        }else {
        	 BlDeposit deposit=new BlDeposit();
             deposit.setDtPaymode(request.getEuPayType());//????????????
             deposit.setAmount(amtPi);//????????????
             deposit.setFlagAcc("0");
             deposit.setDelFlag("0");
             deposit.setPayInfo(request.getSerialNo());//??????????????????
             depositList.add(deposit);
             //????????????????????????
             insertExtPay(request.getEuPayType(),request.getSerialNo(),request.getTradeNo(),amtPi ,"",request.getDateOrder(),opCgforVo,user);
        }



        opCgforVo.setBlDeposits(depositList);

        ResponseJson  respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
        if(respSettle.getStatus()!=0) {
            throw new BusException("??????????????????????????????"+respSettle.getDesc());
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
         extPay.setFlagPay("1");//????????????
         extPay.setDateAp(DateUtils.strToDate(date,"yyyy-MM-dd HH:mm:ss"));//????????????
         extPay.setDatePay(DateUtils.strToDate(date,"yyyy-MM-dd HH:mm:ss"));//????????????
         extPay.setSerialNo(serialNo);//?????????
         extPay.setTradeNo(tradeNo);//?????????
         extPay.setSysname("WX".equals(user.getCodeEmp()) ? "MZWX":"MZZZJ");//????????????
         extPay.setDescPay("");//TODO ????????????
         extPay.setResultPay("");//TODO ????????????
         extPay.setEuPaytype(euPayType);
         extPay.setCreator(user.getPkEmp());
         extPay.setCreateTime(new Date());
         extPay.setTs(new Date());
         extPay.setDelFlag("0");
         extPay.setDtBank("");//TODO ??????
         extPay.setCardNo(cardNo);//?????????????????????????????????
         extPay.setPkPi(opCgforVo.getPkPi());
         extPay.setPkPv(opCgforVo.getPkPv());
         DataBaseHelper.insertBean(extPay);
    }

    /**
     * ??????HIS?????????
     * @param request
     * @param mapResult
     * @return
     */
    private Map<String,Object> getHisAmountSum(RequestSelfAppSettleVo request,List<BlPatiCgInfoNotSettleVO> mapResult){
        Map<String,Object> resMap=new HashMap<String,Object>();
        boolean isSettleOnly=false;//????????????????????????
        if(request.getDataList()!=null && request.getDataList().size()>0){
            isSettleOnly=true;
        }
        List<BlPatiCgInfoNotSettleVO> tempSetvoList=new ArrayList<>();
        BigDecimal amountHisSum=new BigDecimal("0.00");
        if(isSettleOnly) {
            for (RequestSelfSettleDtVo inputParam : request.getDataList()) {
                List<BlPatiCgInfoNotSettleVO> notSettleVOList = mapResult.stream().filter(m -> m.getPkCgop().equals(inputParam.getPkCgop())).collect(Collectors.toList());

                if (notSettleVOList == null || notSettleVOList.size() == 0) {
                    throw new BusException("?????????????????????????????????????????????????????????????????????");
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
     * ??????????????????
     * @param request
     * @param mapResult
     * @return
     */
    private List<BlPatiCgInfoNotSettleVO> getHisYbWillCgList(RequestYbWillSettleVo request,List<BlPatiCgInfoNotSettleVO> mapResult){
        boolean isSettleOnly=false;//????????????????????????
        if(request.getDataList()!=null && request.getDataList().size()>0){
            isSettleOnly=true;
        }
        List<BlPatiCgInfoNotSettleVO> tempSetvoList=new ArrayList<>();
        if(isSettleOnly) {
            for (RequestSelfSettleDtVo inputParam : request.getDataList()) {
                List<BlPatiCgInfoNotSettleVO> notSettleVOList = mapResult.stream().filter(m -> m.getPkCgop().equals(inputParam.getPkCgop())).collect(Collectors.toList());

                if (notSettleVOList == null || notSettleVOList.size() == 0) {
                    //TODO ????????????????????????????????????????????????????????????????????????????????????
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
				throw new BusException("?????????????????????????????????????????????????????? [setlinfo]");
			}

			// ??????????????????
			InsQgybVisit visit = DataBaseHelper.queryForBean(
					"select * from ins_qgyb_visit where pk_pv = ? and  mdtrt_id = ? and del_flag='0'",
					InsQgybVisit.class, pvinfo.getPkPv(), setlinfo.getMdtrtId());
			if (visit == null) {
				throw new BusException("????????????????????????????????????????????????id???????????????????????????" + visit.getMdtrtId());
			}
			// ????????????????????????
			InsQgybSt insQgybSt = new InsQgybSt();
			ApplicationUtils.copyProperties(insQgybSt, setlinfo);
			insQgybSt.setPkPv(pvinfo.getPkPv());
			insQgybSt.setPkPi(pvinfo.getPkPi());
			insQgybSt.setPkHp(pvinfo.getPkInsu());
			insQgybSt.setPkVisit(visit.getPkVisit());
			insQgybSt.setDateSt(new Date());
			insQgybSt.setAmount(insQgybSt.getMedfeeSumamt());// ?????????
			insQgybSt.setAmtGrzf(insQgybSt.getPsnCashPay());// ??????
			insQgybSt.setAmtJjzf(insQgybSt.getFundPaySumamt());// ??????
			insQgybSt.setYbPksettle(insQgybSt.getSetlId());
			DataBaseHelper.insertBean(insQgybSt);

			// ????????????????????????
			List<InsQgybStDt> insQgybStDts = new ArrayList<InsQgybStDt>();
			for (RequestYbSettleDtVo insDtVo : setldetail) {
				InsQgybStDt insDt = new InsQgybStDt();
				ApplicationUtils.copyProperties(insDt, insDtVo);
				ApplicationUtils.setDefaultValue(insDt, true);
				insDt.setPkInsst(insQgybSt.getPkInsst());
				insQgybStDts.add(insDt);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStDt.class), insQgybStDts);
			//??????5204 ???????????????????????????????????????
			ResponseJson  response = apputil.execService("COMPAY","ZsrmQGService","searchStChargeDtls",insQgybSt,user);
	        if(response.getStatus() != 0){
	            throw new BusException("?????????????????????5204?????????"+response.getDesc());
	        }
			
			return insQgybSt;
		} catch (Exception e) {
			log.error("??????????????????,?????????????????????" + e.getMessage());
			throw new BusException("?????????????????????,?????????????????????" + e.getMessage());
		}
		
	}
    


    /**
     * ??????????????????
     * @param def
     * @param user
     * @param opCgforVo
     */
    private void getElectronicBill (DefaultTransactionDefinition def,User user,OpCgTransforVo opCgforVo){
        //??????????????????
        TransactionStatus status2 = platformTransactionManager.getTransaction(def);
        try {
            Map<String, Object> invMap = electInvService.mzOutElectInv(opCgforVo.getPkPv(), user, opCgforVo.getPkSettle());

            platformTransactionManager.commit(status2);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("????????????????????????????????????????????????????????????"+ e.getMessage());
            platformTransactionManager.rollback(status2);
        }
    }

    /**
     * ???????????????????????????????????????
     * @param request
     */
    private String checkReqSettleParam(RequestSelfAppSettleVo request) throws Exception {
        StringBuffer msg = new StringBuffer();
        if (request == null)  {
            return "????????????????????????NULL";
        }
        if(EnumerateParameter.ONE.equals(request.getIsHp())) {
        	ReqYbPreSettleInfovo ybPreSettleInfo = request.getYbPreSettleInfo();
        	if(CommonUtils.isNull(ybPreSettleInfo)) {
        		return "?????????????????????????????????????????????[ybPreSettleInfo]";
        	}
        	ReqYbPreSettleParamVo ybPreSettleParam = request.getYbPreSettleParam();
        	if(CommonUtils.isNull(ybPreSettleParam)) {
        		return "??????????????????????????????????????????????????????[ybPreSettleParam]???";
        	}
        	//??????????????????
        	if(EnumerateParameter.ONE.equals(request.getIsHpOnline())){
        		if (CommonUtils.isNull(request.getSetlinfo())) {
    				throw new BusException("???????????????????????????????????????????????? [setlinfo]!");
    			}
        	}
        	
        	
        }else {
        	//????????????euPayType???serialNo ??????????????????????????????????????????
        	if (CommonUtils.isNull(request.getEuPayType())) {
                msg.append("&&?????????[euPayType]??????!");
            }
        	if (CommonUtils.isNull(request.getSerialNo())) {
                msg.append("&&?????????????????????????????????????????????");
            }
        }
        if (CommonUtils.isNull(request.getCodePi())) {
            msg.append("&&?????????[codePi]??????Id!");
        }
        if (CommonUtils.isNull(request.getCodePv())) {
            msg.append("&&?????????[codePv]????????????Id!");
        }
        if (CommonUtils.isNull(request.getIsHp())) {
            msg.append("&&?????????[isHp]??????");
        }
        if (CommonUtils.isNull(request.getIsElecTicket())) {
            msg.append("&&?????????[isElecTicket]??????!");
        }
        if (CommonUtils.isNull(request.getDateOrder())) {
            msg.append("&&?????????[dateOrder]??????!");
        }
        if (CommonUtils.isNull(request.getFlagPay())) {
            msg.append("&&?????????[flagPay]??????!");
        }
        if (CommonUtils.isNull(request.getSysname())) {
            msg.append("&&?????????[sysname]??????!");
        }
        if (CommonUtils.isNull(request.getCodeEmpSt())) {
            msg.append("&&?????????[codeEmpSt]??????!");
        }
        if (CommonUtils.isNull(request.getNameEmpSt())) {
            msg.append("&&?????????[nameEmpSt]??????!");
        }
        if(CommonUtils.isNull(request.getCodeDeptSt())){
            msg.append("&&?????????[codeDeptSt]??????!");
        }
        if(CommonUtils.isNull(request.getNameDeptSt())){
            msg.append("&&?????????[nameDeptSt]??????!");
        }
        if (CommonUtils.isNull(request.getAmountExt())) {
            msg.append("&&????????????????????????!");
        }

        if(StringUtils.isNotBlank(msg)){
            return msg.toString();
        }
        String sql = "select count(1) from pv_encounter where code_pv=?";
        Integer countPv = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{request.getCodePv()});
        if (countPv != 1) {
            return "??????????????????????????????????????????????????????";
        }
        return "";
    }

    /**
     * ???????????????????????????????????????
     * @param request
     * @throws Exception
     */
    private String checkReqWillSettleParam(RequestYbWillSettleVo request) throws Exception{
    	StringBuffer msg = new StringBuffer();
        if (request == null)  {
            return "????????????????????????NULL";
        }
        if (CommonUtils.isNull(request.getCodePi())) {
            msg.append("&&?????????[codePi]??????Id!");
        }
        if (CommonUtils.isNull(request.getCodePv())) {
            msg.append("&&?????????[codePv]????????????Id!");
        }
        if(StringUtils.isNotBlank(msg)){
            return msg.toString();
        }
        String sql="select count(1) from pv_encounter where code_pv=?";
        Integer countPv= DataBaseHelper.queryForScalar(sql,Integer.class,new Object[]{request.getCodePv()});
        if(countPv!=1){
        	return "?????????????????????????????????????????????????????????";
        }
        return "";
    }

    /**
     * ??????????????????
     * @param paramMap
     * @return
     */
    private  List<ResponseQryNoSettleVo> qryPvCgList(Map<String,Object> paramMap){
        Map<String,Object> tempmap=new HashMap<>();
        String curtime = DateUtils.getDateTimeStr(new Date());
        tempmap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //???????????????????????????
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
                //???????????????????????????
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
     * ??????????????????
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
     * ???????????????????????????
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
     * ??????????????????
     * @param hisJson
     * @return
     */
    private String successJson(String hisJson){
        String result="{\"resourceType\":\"Bundle\",\"id\":\"%s\",\"type\":\"message\",\"timestamp\":\"%s\",\"entry\":[{\"response\":{\"staus\":\"AA\",\"outcome\":{\"resourceType\":\"OperationOutcome\",\"issue\":%s}}}]}\n";
        result=String.format(result,NHISUUID.getKeyId(),DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),hisJson);
        return result;

    }

    /**
     * ??????????????????
     * @param message
     * @return
     */
    private String errorJson(String message){
        String result="{\"resourceType\":\"Bundle\",\"id\":\"%s\",\"type\":\"message\",\"timestamp\":\"%s\",\"entry\":[{\"response\":{\"staus\":\"AE\",\"outcome\":{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"informational\",\"diagnostics\":\"%s\"}]}}}]}";
        result=String.format(result,NHISUUID.getKeyId(),DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),message);
        return result;

    }

    /**
     * ??????????????????
     * @param message
     * @return
     */
    private String guideErrorJson(String message){
        String result="{\"resourceType\":\"Bundle\",\"id\":\"%s\",\"type\":\"message\",\"timestamp\":\"%s\",\"entry\":[{\"response\":{\"staus\":\"AC\",\"outcome\":{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"informational\",\"diagnostics\":\"%s\"}]}}}]}";
        result=String.format(result,NHISUUID.getKeyId(),DateUtils.getDate("yyyy-MM-dd HH:mm:ss"),message);
        return result;

    }

    /**
     * ??????????????????
     * @param codeEmp
     * @return
     */
    private BdOuEmployee getEmpInfo(String codeEmp){
        String sql="select * from bd_ou_employee where code_emp =?";
        BdOuEmployee emp =DataBaseHelper.queryForBean(sql,BdOuEmployee.class,new Object[]{codeEmp});
        if(emp==null){
            throw new BusException("???????????????????????????????????????");
        }
        return emp;
    }

    /**
     * ??????????????????
     * @param codeDept
     * @return
     */
    private BdOuDept getDeptInfo(String codeDept){
        String sql="select * from bd_ou_dept where code_dept=?";
        BdOuDept dept=DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{codeDept});
        return dept;
    }
    /**
     * ?????????????????????????????????
     * ????????????
     * @param codeDept
     * @return
     */
    private String getPkDeptByConf(String pkOrg){
        	if(pkOrg == ZsrmSelfAppConstant.PK_ORG){//??????
        		return ZsrmSelfAppConstant.PK_DEPT_CG;
        	}else if(pkOrg == ZsrmSelfAppConstant.pk_Org_LF){//??????
        		return ZsrmSelfAppConstant.pk_Dept_LF;
        	}else {
        		return ZsrmSelfAppConstant.PK_DEPT_CG;
        	}

    }
    
    

}
