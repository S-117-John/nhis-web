package com.zebone.nhis.webservice.pskq.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybPv;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybSt;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybStDt;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybVisit;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.webservice.pskq.model.message.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybSt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCity;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCitydt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybVisit;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.pskq.repository.BdOuEmployeeRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PvEncounterRepository;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.OrderOutpat;
import com.zebone.nhis.webservice.pskq.model.SettlementDetailOutpat;
import com.zebone.nhis.webservice.pskq.model.SettlementMasterOutpat;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.service.SettlementOutpatService;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.webservice.pskq.utils.PskqConstant;
import com.zebone.nhis.webservice.pskq.utils.PskqMesUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;


/**
 * 门诊收费策略   存在问题
 */
public class SettlementOutpatServiceImpl implements SettlementOutpatService {



    public String save(RequestBody requestBody) throws Exception {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());





        AckElement ackElement = new AckElement();
        ackElement.setTargetMessageId(requestBody.getId());
        List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("SETTLEMENT_MASTER_OUTPAT");
        SettlementMasterOutpat settlementMasterOutpat = (SettlementMasterOutpat) MessageFactory.deserialization(list, new SettlementMasterOutpat());


//        settlementMasterOutpat.setAccountTypeCode("2");


        List<Map<String, Object>> orderList = (List<Map<String, Object>>) requestBody.getMessage().get("ORDER_OUTPAT_LIST");
        List<DataElement> orderOne = (List<DataElement>) orderList.get(0).get("ORDER_OUTPAT");
        OrderOutpat orderOutpat = (OrderOutpat) MessageFactory.deserialization(orderOne, new OrderOutpat());
        List<Map<String,Object>> ybcsList = (List<Map<String,Object>>) requestBody.getMessage().get("ybcs");
        Map<String,Object> ybcs = new HashMap<String, Object>();
        if(ybcsList != null && ybcsList.size() > 0) {
            ybcs = ybcsList.get(0);
        }
        //结算时间
        if ("".equals(settlementMasterOutpat.getSettlementDateTime()) || settlementMasterOutpat.getSettlementDateTime() == null) {
            throw new BusException("未传入结算时间,请检查");
        }
        //病人类别代码  自费 医保等
        if ("".equals(settlementMasterOutpat.getPatientTypeCode()) || settlementMasterOutpat.getPatientTypeCode() == null) {
            throw new BusException("病人类别代码,请检查");
        }
        //总金额
        if ("".equals(settlementMasterOutpat.getTotalFee()) || settlementMasterOutpat.getTotalFee() == null) {
            throw new BusException("未传入总金额,请检查");
        }
        //个人支付费用
        if ("".equals(settlementMasterOutpat.getSelfPaymentFee()) || settlementMasterOutpat.getSelfPaymentFee() == null) {
            settlementMasterOutpat.setSelfPaymentFee("0");
        }
        //医保支付费用
        if ("".equals(settlementMasterOutpat.getMiPaymentFee()) || settlementMasterOutpat.getMiPaymentFee() == null) {
            settlementMasterOutpat.setMiPaymentFee("0");
        }
        //结算类型
        if ("".equals(settlementMasterOutpat.getAccountTypeCode()) || settlementMasterOutpat.getAccountTypeCode() == null) {
            throw new BusException("未传入结算类型,请检查");
        }
        //结算人ID
        if ("".equals(settlementMasterOutpat.getSettlementOperaId()) || settlementMasterOutpat.getSettlementOperaId() == null) {
            throw new BusException("未传入结算人ID,请检查");
        }
        if(settlementMasterOutpat.getSettlementDateTime() == null) {
            throw new BusException("未传入结算日期,请检查");
        }
        Date date = PskqMesUtils.simDatFor.parse(settlementMasterOutpat.getSettlementDateTime());

        BdOuEmployee bdOuEmployee = BdOuEmployeeRepository.findByCodeEmp(settlementMasterOutpat.getSettlementOperaId());
        if (bdOuEmployee == null) {
            throw new BusException("通过结算人ID:" + settlementMasterOutpat.getSettlementOperaId() + "未查询到结算人信息,请检查是否同步人员信息");
        }
        //是否医保
        if(CommonUtils.isEmptyString(settlementMasterOutpat.getIsInsurance())){
            //listener.error("是否医保不能为空");
            settlementMasterOutpat.setIsInsurance("0");
        }
        String isInsurance = settlementMasterOutpat.getIsInsurance();

        //医保参数验证
        if("1".equals(isInsurance)) {
            if(MapUtils.isEmpty(ybcs)) {
                throw new BusException("医保患者挂号，医保参数体不能为空！");
            }
        }

        String settleNo = settlementMasterOutpat.getSettlementNo();
        //结算单号
        if ("".equals(settleNo) || settleNo == null) {
            throw new BusException("未传入结算单号,请检查");
        }
        String payNo = settlementMasterOutpat.getPaymentNo();
        //支付单号
        if ("".equals(payNo) || payNo == null) {
            throw new BusException("未传入支付单号,请检查");
        }
        String pvCode = orderOutpat.getVisitId();
        //就诊流水号
        if ("".equals(pvCode) || pvCode == null) {
            throw new BusException("未传入就诊流水号,请检查");
        }
        String pvSql = "select * from PV_ENCOUNTER where code_pv = ? and del_flag = '0' ";
        PvEncounter pvencounter = DataBaseHelper.queryForBean(pvSql,PvEncounter.class,new Object[]{pvCode});
        if (pvencounter == null) {
            throw new BusException("通过就诊流水号:" + pvCode + "未查询到就诊信息,请检查");
        }

        // 构建session中User信息
        User user = new User();
        user.setPkOrg(PskqConstant.PKORG); //固定值-机构
        user.setPkDept(PskqConstant.PKDEPT);//固定科室-门诊收费
        String pkEmp = bdOuEmployee.getPkEmp();
        String nameEmp = bdOuEmployee.getNameEmp();
        String codeEmp = bdOuEmployee.getCodeEmp();
        user.setPkEmp(pkEmp);// evn中查询获取  pkEmp
        user.setNameEmp(nameEmp);// evn查询获取-nameEmp
        user.setCodeEmp(codeEmp);
        UserContext.setUser(user);

        //医保患者自费结算，先转为自费结算后，结算完成后转为医保
        String pkInsu = new String(pvencounter.getPkInsu());//原来的类型

        //费用总额
        BigDecimal totalFee = new BigDecimal(settlementMasterOutpat.getTotalFee());
        //自费总金额
        BigDecimal amountPi = BigDecimal.ZERO.add(new BigDecimal(settlementMasterOutpat.getSelfPaymentFee()));
        //医保总金额
        BigDecimal amountInsu = BigDecimal.ZERO.add(new BigDecimal(settlementMasterOutpat.getMiPaymentFee()));


        ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
        OpcgPubHelperService opcgPubHelperService = applicationContext.getBean("opcgPubHelperService", OpcgPubHelperService.class);
        Map<String,Object> doYbRetData = new HashMap<String, Object>();
        InsQgybSt insQgybSt = new InsQgybSt();
        if("0".equals(isInsurance)) {
            updatePkInsu(PskqConstant.PKINSUSELF,pvencounter);
            pvencounter = DataBaseHelper.queryForBean(pvSql, PvEncounter.class,new Object[]{pvCode});
        }else {
            //医保结算vo；保存医保数据
//                	doYbRetData = saveYbSettleData(ybcs,pvencounter);
//            		if(!"1".equals(doYbRetData.get("status"))){
//                        throw new BusException("HIS方法【saveYbSettleData】保存医保数据失败"+doYbRetData.get("data"));
//            		}
            if("1".equals(settlementMasterOutpat.getIsInsurance())){
                String sql = "select * from PV_ENCOUNTER where code_pv = ? and del_flag = '0' ";
//                        PvEncounter pvencounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,orderOutpat.getVisitId());
//                        sql="select * from ins_qgyb_visit where del_flag='0'  and pk_pv=? order by date_reg desc ";
//                        String akc190 = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"akc190","");
//                        List<InsSzybVisit> visitList = DataBaseHelper.queryForList(sql, InsSzybVisit.class,new Object[]{pvencounter.getPkPv()});
                sql="select * from ins_qgyb_visit where del_flag='0'  and pk_pv=? order by date_reg desc ";
                List<InsQgybVisit> visitList = DataBaseHelper.queryForList(sql, InsQgybVisit.class,new Object[]{pvencounter.getPkPv()});
                if (visitList.size()==0){
                    throw new BusException("未查询到医保登记信息");
                }
                sql = "select * from BD_OU_EMPLOYEE where CODE_EMP = ?";

                BdOuEmployee employee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,settlementMasterOutpat.getSettlementOperaId());

                String fundPayType = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"fundPayType","");
                String inscpScpAmt = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"inscpScpAmt","");
                String crtPaybLmtAmt = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"crtPaybLmtAmt","");
                String fundPayamt = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"fundPayamt","");
                String fundPayTypeName = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"fundPayTypeName","");
//                        InsSzybVisit insSzybVisit = visitList.get(0);
                String setlId = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"setl_id","");
                String mdtrtId = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"mdtrtId","");
                InsQgybVisit insQgybVisit = visitList.get(0);
                //保存全国医保

                insQgybSt.setSetlId(setlId);//结算ID
                sql = "select * from INS_QGYB_PV where PK_PV = ? and rownum = 1 order by CREATE_TIME desc";
                InsQgybPv insQgybPv = DataBaseHelper.queryForBean(sql,InsQgybPv.class,pvencounter.getPkPv());
                insQgybSt.setPsnNo(insQgybVisit.getPsnNo());
                insQgybSt.setMedType(insQgybPv!=null?insQgybPv.getMedType():"");
                insQgybSt.setMdtrtId(mdtrtId);
                insQgybSt.setDateSt(new Date());
                insQgybSt.setAmtGrzhzf(org.springframework.util.StringUtils.isEmpty(fundPayamt)?null:Double.valueOf(fundPayamt));
                insQgybSt.setPkVisit(insQgybVisit.getPkVisit());//医保登记主键
                insQgybSt.setPkHp(insQgybVisit.getPkHp());//医保主键
                insQgybSt.setPkPv(pvencounter.getPkPv());//就诊主键
                insQgybSt.setPkPi(pvencounter.getPkPi());//患者主键
                insQgybSt.setPkSettle("");//结算主键--先给空，结算完成更新此字段
                insQgybSt.setDateSt(null);//结算日期
                insQgybSt.setAmount(org.springframework.util.StringUtils.isEmpty(fundPayamt)?null:Double.valueOf(fundPayamt));//结算金额
                insQgybSt.setCreator(employee.getPkEmp());//创建人
                insQgybSt.setCreateTime(new Date());//创建时间
                insQgybSt.setDelFlag("0");//删除标志
                insQgybSt.setTs(new Date());//时间戳
                DataBaseHelper.insertBean(insQgybSt);

                InsQgybStDt insQgybStDt = new InsQgybStDt();
                insQgybStDt.setPkInsst(insQgybSt.getPkInsst());
                insQgybStDt.setFundPayType(fundPayType);
                insQgybStDt.setInscpScpAmt(org.springframework.util.StringUtils.isEmpty(inscpScpAmt)?null:Double.valueOf(inscpScpAmt));
                insQgybStDt.setCrtPaybLmtAmt(org.springframework.util.StringUtils.isEmpty(crtPaybLmtAmt)?null:Double.valueOf(crtPaybLmtAmt));
                insQgybStDt.setFundPayamt(org.springframework.util.StringUtils.isEmpty(fundPayamt)?null:Double.valueOf(fundPayamt));
                insQgybStDt.setFundPayTypeName(fundPayTypeName);
                insQgybStDt.setSetlProcInfo("");
                insQgybStDt.setCreator(employee.getPkEmp());
                insQgybStDt.setCreateTime(new Date());
                insQgybStDt.setDelFlag("0");
                insQgybStDt.setTs(new Date());
                DataBaseHelper.insertBean(insQgybStDt);
            }
        }

        Map<String, Object> resMap = new HashMap<>();
        String curtime = DateUtils.getDateTimeStr(new Date());
        resMap.put("curDate", curtime.substring(0, curtime.length() - 4) + "0000"); //有效时间匹配到小时
        resMap.put("notDisplayFlagPv", "0");
        resMap.put("isNotShowPv", "0");
        resMap.put("pkPv", pvencounter.getPkPv());
        resMap.put("pkPi", pvencounter.getPkPi());

        //未计算金额
        //HIS未结算总金额 (需要结算部分)
        BigDecimal amountSt = BigDecimal.ZERO;
        List<BlPatiCgInfoNotSettleVO> mapResult = opcgPubHelperService.queryPatiCgInfoNotSettle(resMap);
        if (mapResult == null || mapResult.size() == 0) {
            throw new BusException("未查询到未结算的数据信息");
        }

        Iterator<BlPatiCgInfoNotSettleVO> iterator = mapResult.iterator();
        while (iterator.hasNext()) {
            BlPatiCgInfoNotSettleVO settleVO = iterator.next();
            amountSt = amountSt.add(settleVO.getAmount());
        }
        if (amountSt.compareTo(totalFee) != 0) {

            throw new BusException("传入总金额与HIS计算结果不符,传入金额【" + totalFee + "】,HIS计算金额【" + amountSt + "】");
        }
        //组装预结算 数据
        String mapResultJson = JsonUtil.writeValueAsString(mapResult);
        List<BlOpDt> opDts = JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {
        });
        OpCgTransforVo opCgforVo = new com.zebone.nhis.bl.pub.vo.OpCgTransforVo();
        opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
        opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
        opCgforVo.setBlOpDts(opDts);
        opCgforVo.setAggregateAmount(amountSt); //需支付总金额
        opCgforVo.setMedicarePayments(amountInsu);//医保支付
        opCgforVo.setPatientsPay(amountPi);//现金支付
        opCgforVo.setXjzf(new BigDecimal(settlementMasterOutpat.getSelfPaymentFee()));
        //调用预结算接口 countOpcgAccountingSettlement
        ResponseJson response = ApplicationUtils.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo, user);
        if (response.getStatus() != 0) {

            // 预结算接口调用失败 -处理异常消息：注意此接口调用要避免异常
            throw new BusException("HIS预结算接口【YjSettle】调用失败:" + response.getDesc());
        }
        opCgforVo = (com.zebone.nhis.bl.pub.vo.OpCgTransforVo) response.getData();
        opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
        opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
        opCgforVo.setBlOpDts(opDts);
        opCgforVo.setAmtInsuThird(amountInsu);

        //付款数据构建
        List<BlDeposit> depositList = new ArrayList<BlDeposit>();
        BlDeposit deposit = new BlDeposit();

        deposit.setDtPaymode(settlementMasterOutpat.getAccountTypeCode());
        deposit.setAmount(amountPi);
        deposit.setFlagAcc("0");
        deposit.setDelFlag("0");
        deposit.setPayInfo(payNo);//第三方订单号
        depositList.add(deposit);
        opCgforVo.setBlDeposits(depositList);
        opCgforVo.setPatientsPay(amountPi);

        //opCgforVo.setInvStatus("-2");//自费-不限制发票信息传入
        //打印电子票--传入计算机工作站名称、打印纸质标志不打印纸质
        List<InvoiceInfo> invos = new ArrayList<InvoiceInfo>();
        InvoiceInfo InvoiceInfo = new InvoiceInfo();
        InvoiceInfo.setFlagPrint("0");
        InvoiceInfo.setMachineName(codeEmp.toUpperCase());
        List<BlInvoiceDt> blInvoiceDtList = new ArrayList<>();
        BlInvoiceDt blInvoiceDt = new BlInvoiceDt();

        blInvoiceDtList.add(blInvoiceDt);
        InvoiceInfo.setBlInDts(blInvoiceDtList);
        opCgforVo.setMachineName(codeEmp.toUpperCase());
        opCgforVo.setFlagPrint("0");
        invos.add(InvoiceInfo);
        opCgforVo.setInvoiceInfo(invos);


        String sql = "select * from bl_ext_pay where SERIAL_NO = ?";
        String serialNo = settlementMasterOutpat.getPaymentNo();
        if(!StringUtils.isEmpty(serialNo)){
            BlExtPay blExtPay = DataBaseHelper.queryForBean(sql,BlExtPay.class,serialNo);
            if(blExtPay==null){
                blExtPay = new BlExtPay();
                blExtPay.setAmount(BigDecimal.ZERO);
                blExtPay.setPkExtpay(NHISUUID.getKeyId());
                blExtPay.setPkPi(opCgforVo.getPkPi());
                blExtPay.setPkPv(opCgforVo.getPkPv());
                blExtPay.setDateAp(new Date());//请求时间
                blExtPay.setDatePay(date);//支付时间
                blExtPay.setAmount(amountPi);//现金支付
                blExtPay.setEuPaytype(settlementMasterOutpat.getAccountTypeCode());//付款方式
                blExtPay.setFlagPay("1");//支付标志
                blExtPay.setSerialNo(settlementMasterOutpat.getPaymentNo());//订单号  结算单号
                blExtPay.setTradeNo(settlementMasterOutpat.getSettlementNo());//系统订单号    TradeNo  系统订单号(微信退货交易时使用此域)
                blExtPay.setSysname(settlementMasterOutpat.getSourceSystemCode());//终端号
                blExtPay.setPkSettle(opCgforVo.getPkSettle());
                String sqlDepo = "select pk_depo from bl_deposit where pk_settle=?";
                Map<String, Object> depoMap = DataBaseHelper.queryForMap(sqlDepo, new Object[]{opCgforVo.getPkSettle()});
                blExtPay.setPkDepo(SDMsgUtils.getPropValueStr(depoMap, "pkDepo"));
                blExtPay.setCreator(user.getPkEmp());
                blExtPay.setCreateTime(new Date());
                blExtPay.setPkOrg(user.getPkOrg());
                blExtPay.setTs(new Date());
                blExtPay.setModifier(user.getPkEmp());
                blExtPay.setDelFlag("0");
                DataBaseHelper.insertBean(blExtPay);
            }
        }


        //此状态控制事务提交后在发送消息
        opCgforVo.setSource("noSendMessage");
        ResponseJson respSettle = ApplicationUtils.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo, user);
        if (respSettle.getStatus() != 0) {

            throw new BusException("HIS结算接口【Settle】调用失败:" + respSettle.getDesc() + respSettle.getErrorMessage());
        }

        //保存第三方支付表
        opCgforVo = (OpCgTransforVo) respSettle.getData();

//        List<BlDeposit> temp = opCgforVo.getBlDeposits();
//        for (BlDeposit blDeposit : temp) {
//            if(blDeposit != null){
//                if("微信支付".equals( settlementMasterOutpat.getAccountTypeCode())){
//                    settlementMasterOutpat.setAccountTypeCode("15");
//                }
//                if("支付宝支付".equals(settlementMasterOutpat.getAccountTypeCode())){
//                    settlementMasterOutpat.setAccountTypeCode("16");
//                }
//                DataBaseHelper.update(DataBaseHelper.getUpdateSql(BlDeposit.class),blDeposit);
//            }
//        }

        //医保患者自费结算，先转为自费结算后，转为医保
        if("0".equals(isInsurance)) {
            updatePkInsu(pkInsu, pvencounter);
        }else {
            //HIS结算成功后回写医保结算记录中得pk_settle
            if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(doYbRetData, "data"))) {
//                String upYbSql="update ins_qgyb_st set pk_settle=? where pk_insst=?";
//                DataBaseHelper.execute(upYbSql, new Object[]{opCgforVo.getPkSettle(),insQgybSt.getPkInsst()});
            }
        }




        BlExtPay blExtPay = new BlExtPay();
        blExtPay.setAmount(BigDecimal.ZERO);
        blExtPay.setPkExtpay(NHISUUID.getKeyId());
        blExtPay.setPkPi(opCgforVo.getPkPi());
        blExtPay.setPkPv(opCgforVo.getPkPv());
        blExtPay.setDateAp(new Date());//请求时间
        blExtPay.setDatePay(date);//支付时间
        blExtPay.setAmount(amountPi);//现金支付
        blExtPay.setEuPaytype(settlementMasterOutpat.getAccountTypeCode());//付款方式
        blExtPay.setFlagPay("1");//支付标志
        blExtPay.setSerialNo(settlementMasterOutpat.getPaymentNo());//订单号  结算单号
        blExtPay.setTradeNo(settlementMasterOutpat.getSettlementNo());//系统订单号    TradeNo  系统订单号(微信退货交易时使用此域)
        blExtPay.setSysname(settlementMasterOutpat.getSourceSystemCode());//终端号
        blExtPay.setPkSettle(opCgforVo.getPkSettle());
        String sqlDepo = "select pk_depo from bl_deposit where pk_settle=?";
        Map<String, Object> depoMap = DataBaseHelper.queryForMap(sqlDepo, new Object[]{opCgforVo.getPkSettle()});
        blExtPay.setPkDepo(SDMsgUtils.getPropValueStr(depoMap, "pkDepo"));
        blExtPay.setCreator(user.getPkEmp());
        blExtPay.setCreateTime(new Date());
        blExtPay.setPkOrg(user.getPkOrg());
        blExtPay.setTs(new Date());
        blExtPay.setModifier(user.getPkEmp());
        blExtPay.setDelFlag("0");
        DataBaseHelper.insertBean(blExtPay);





        String upYbSql="update ins_qgyb_st set pk_settle=? where pk_insst=?";
        DataBaseHelper.execute(upYbSql,opCgforVo.getPkSettle(),insQgybSt.getPkInsst());
        //生成电子票据
        List<BlInvoice> resInvList=new ArrayList<>();
        try {
            resInvList = mzOutElectInv(opCgforVo.getPkPv(), user, opCgforVo.getPkSettle());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回发票信息
        SettlementDetailOutpat settlementDetailOutpat = new SettlementDetailOutpat();
        settlementDetailOutpat.setEmpiId(settlementMasterOutpat.getEmpiId());
        settlementDetailOutpat.setPkPatient(settlementMasterOutpat.getPkPatient());
        List<BlInvoice> blInvoices = opCgforVo.getBlInvoices();
        if(blInvoices != null && blInvoices.size() > 0) {
            settlementDetailOutpat.setSettlementDetailId(blInvoices.get(0).getPkInvoice());
        }
        if(resInvList!=null && resInvList.size()>0){
            settlementDetailOutpat.setSettlementDetailId(resInvList.get(0).getPkInvoice());
        }
        List<DataElement> dataElementList = MessageBodyUtil.dataElementsReturnFactory(settlementDetailOutpat);

        //发送门诊收费信息至平台
        Map<String,Object> paramVo = new HashMap<String, Object>(16);
        paramVo.put("Control", "OK");
        paramVo.put("pkSettle", opCgforVo.getPkSettle());
        PlatFormSendUtils.sendBlOpSettleMsg(paramVo);
        ack.put("ackCode", "AA");
        ack.put("ackDetail", "消息处理成功");
        ack.put("SETTLEMENT_DETAIL_OUTPAT", dataElementList);
        responseBody.setAck(ack);
        return JSON.toJSONString(responseBody);

    }









    @Override
    public void save(RequestBody requestBody, ResultListener listener) {
        //修改为手动事物 , 关闭事务自动提交
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);

        try {
        	AckElement ackElement = new AckElement();
            ackElement.setTargetMessageId(requestBody.getId());
            List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("SETTLEMENT_MASTER_OUTPAT");
            SettlementMasterOutpat settlementMasterOutpat = (SettlementMasterOutpat) MessageFactory.deserialization(list, new SettlementMasterOutpat());
            List<Map<String, Object>> orderList = (List<Map<String, Object>>) requestBody.getMessage().get("ORDER_OUTPAT_LIST");
            List<DataElement> orderOne = (List<DataElement>) orderList.get(0).get("ORDER_OUTPAT");
            OrderOutpat orderOutpat = (OrderOutpat) MessageFactory.deserialization(orderOne, new OrderOutpat());
            List<Map<String,Object>> ybcsList = (List<Map<String,Object>>) requestBody.getMessage().get("ybcs");
            Map<String,Object> ybcs = new HashMap<String, Object>();
            if(ybcsList != null && ybcsList.size() > 0) {
            	ybcs = ybcsList.get(0);
            }
            //结算时间
            if ("".equals(settlementMasterOutpat.getSettlementDateTime()) || settlementMasterOutpat.getSettlementDateTime() == null) {
                listener.error("未传入结算时间,请检查");
                return;
            }
            //病人类别代码  自费 医保等
            if ("".equals(settlementMasterOutpat.getPatientTypeCode()) || settlementMasterOutpat.getPatientTypeCode() == null) {
                listener.error("病人类别代码,请检查");
                return;
            }
            //总金额
            if ("".equals(settlementMasterOutpat.getTotalFee()) || settlementMasterOutpat.getTotalFee() == null) {
                listener.error("未传入总金额,请检查");
                return;
            }
            //个人支付费用
            if ("".equals(settlementMasterOutpat.getSelfPaymentFee()) || settlementMasterOutpat.getSelfPaymentFee() == null) {
            	settlementMasterOutpat.setSelfPaymentFee("0");
            }
            //医保支付费用
            if ("".equals(settlementMasterOutpat.getMiPaymentFee()) || settlementMasterOutpat.getMiPaymentFee() == null) {
            	settlementMasterOutpat.setMiPaymentFee("0");
            }
            //结算类型
            if ("".equals(settlementMasterOutpat.getAccountTypeCode()) || settlementMasterOutpat.getAccountTypeCode() == null) {
                listener.error("未传入结算类型,请检查");
                return;
            }
            //结算人ID
            if ("".equals(settlementMasterOutpat.getSettlementOperaId()) || settlementMasterOutpat.getSettlementOperaId() == null) {
                listener.error("未传入结算人ID,请检查");
                return;
            }
            if(settlementMasterOutpat.getSettlementDateTime() == null) {
            	listener.error("未传入结算日期,请检查");
                return;
            }
            Date date = PskqMesUtils.simDatFor.parse(settlementMasterOutpat.getSettlementDateTime());

            BdOuEmployee bdOuEmployee = BdOuEmployeeRepository.findByCodeEmp(settlementMasterOutpat.getSettlementOperaId());
            if (bdOuEmployee == null) {
                listener.error("通过结算人ID:" + settlementMasterOutpat.getSettlementOperaId() + "未查询到结算人信息,请检查是否同步人员信息");
                return;
            }
            //是否医保
            if(CommonUtils.isEmptyString(settlementMasterOutpat.getIsInsurance())){
                //listener.error("是否医保不能为空");
                settlementMasterOutpat.setIsInsurance("0");
            }
            String isInsurance = settlementMasterOutpat.getIsInsurance();
            
            //医保参数验证
            if("1".equals(isInsurance)) {
            	if(MapUtils.isEmpty(ybcs)) {
            		listener.error("医保患者挂号，医保参数体不能为空！");
            		return;
            	}
            }
            
            String settleNo = settlementMasterOutpat.getSettlementNo();
            //结算单号
            if ("".equals(settleNo) || settleNo == null) {
                listener.error("未传入结算单号,请检查");
                return;
            }
            String payNo = settlementMasterOutpat.getPaymentNo();
            //支付单号
            if ("".equals(payNo) || payNo == null) {
                listener.error("未传入支付单号,请检查");
                return;
            }
            String pvCode = orderOutpat.getVisitId();
            //就诊流水号
            if ("".equals(pvCode) || pvCode == null) {
                listener.error("未传入就诊流水号,请检查");
                return;
            }        
            String pvSql = "select * from PV_ENCOUNTER where code_pv = ? and del_flag = '0' ";
            PvEncounter pvencounter = DataBaseHelper.queryForBean(pvSql,PvEncounter.class,new Object[]{pvCode});
            if (pvencounter == null) {
                listener.error("通过就诊流水号:" + pvCode + "未查询到就诊信息,请检查");
                    return;
                }
      
                // 构建session中User信息
            User user = new User();
            user.setPkOrg(PskqConstant.PKORG); //固定值-机构
            user.setPkDept(PskqConstant.PKDEPT);//固定科室-门诊收费
            String pkEmp = bdOuEmployee.getPkEmp();
            String nameEmp = bdOuEmployee.getNameEmp();
            String codeEmp = bdOuEmployee.getCodeEmp();
            user.setPkEmp(pkEmp);// evn中查询获取  pkEmp
            user.setNameEmp(nameEmp);// evn查询获取-nameEmp
            user.setCodeEmp(codeEmp);
            UserContext.setUser(user);
            
             //医保患者自费结算，先转为自费结算后，结算完成后转为医保
            String pkInsu = new String(pvencounter.getPkInsu());//原来的类型
            
            //费用总额
            BigDecimal totalFee = new BigDecimal(settlementMasterOutpat.getTotalFee());
            //自费总金额
            BigDecimal amountPi = BigDecimal.ZERO.add(new BigDecimal(settlementMasterOutpat.getSelfPaymentFee()));
            //医保总金额
            BigDecimal amountInsu = BigDecimal.ZERO.add(new BigDecimal(settlementMasterOutpat.getMiPaymentFee()));
            

         	ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            OpcgPubHelperService opcgPubHelperService = applicationContext.getBean("opcgPubHelperService", OpcgPubHelperService.class);
            Map<String,Object> doYbRetData = new HashMap<String, Object>();
            if("0".equals(isInsurance)) {
                updatePkInsu(PskqConstant.PKINSUSELF,pvencounter);
                pvencounter = DataBaseHelper.queryForBean(pvSql, PvEncounter.class,new Object[]{pvCode});
            }else {
                //医保结算vo；保存医保数据
//                	doYbRetData = saveYbSettleData(ybcs,pvencounter);
//            		if(!"1".equals(doYbRetData.get("status"))){
//            			dataSourceTransactionManager.rollback(transStatus); // 添加失败 回滚事务；
//            			listener.error("HIS方法【saveYbSettleData】保存医保数据失败"+doYbRetData.get("data"));
//            			return;
//            		}
                if("1".equals(settlementMasterOutpat.getIsInsurance())){
                    String sql = "select * from PV_ENCOUNTER where code_pv = ? and del_flag = '0' ";
//                        PvEncounter pvencounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,orderOutpat.getVisitId());
//                        sql="select * from ins_qgyb_visit where del_flag='0'  and pk_pv=? order by date_reg desc ";
//                        String akc190 = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"akc190","");
//                        List<InsSzybVisit> visitList = DataBaseHelper.queryForList(sql, InsSzybVisit.class,new Object[]{pvencounter.getPkPv()});
                    sql="select * from ins_qgyb_visit where del_flag='0'  and pk_pv=? order by date_reg desc ";
                    List<InsQgybVisit> visitList = DataBaseHelper.queryForList(sql, InsQgybVisit.class,new Object[]{pvencounter.getPkPv()});
                    if (visitList.size()==0){
                        throw new BusException("未查询到医保登记信息");
                    }
                    sql = "select * from BD_OU_EMPLOYEE where CODE_EMP = ?";

                    BdOuEmployee employee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,settlementMasterOutpat.getSettlementOperaId());

                    String fundPayType = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"fundPayType","");
                    String inscpScpAmt = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"inscpScpAmt","");
                    String crtPaybLmtAmt = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"crtPaybLmtAmt","");
                    String fundPayamt = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"fundPayamt","");
                    String fundPayTypeName = org.apache.commons.collections.MapUtils.getString(ybcsList.get(0),"fundPayTypeName","");
//                        InsSzybVisit insSzybVisit = visitList.get(0);
                    InsQgybVisit insQgybVisit = visitList.get(0);
                    //保存全国医保
                    InsQgybSt insQgybSt = new InsQgybSt();
                    insQgybSt.setSetlId("");//结算ID
                    insQgybSt.setPkVisit(insQgybVisit.getPkVisit());//医保登记主键
                    insQgybSt.setPkHp(insQgybVisit.getPkHp());//医保主键
                    insQgybSt.setPkPv(pvencounter.getPkPv());//就诊主键
                    insQgybSt.setPkPi(pvencounter.getPkPi());//患者主键
                    insQgybSt.setPkSettle("");//结算主键--先给空，结算完成更新此字段
                    insQgybSt.setDateSt(null);//结算日期
                    insQgybSt.setAmount(org.springframework.util.StringUtils.isEmpty(fundPayamt)?null:Double.valueOf(fundPayamt));//结算金额
                    insQgybSt.setCreator(employee.getPkEmp());//创建人
                    insQgybSt.setCreateTime(new Date());//创建时间
                    insQgybSt.setDelFlag("0");//删除标志
                    insQgybSt.setTs(new Date());//时间戳
                    DataBaseHelper.insertBean(insQgybSt);

                    InsQgybStDt insQgybStDt = new InsQgybStDt();
                    insQgybStDt.setPkInsst(insQgybSt.getPkInsst());
                    insQgybStDt.setFundPayType(fundPayType);
                    insQgybStDt.setInscpScpAmt(org.springframework.util.StringUtils.isEmpty(inscpScpAmt)?null:Double.valueOf(inscpScpAmt));
                    insQgybStDt.setCrtPaybLmtAmt(org.springframework.util.StringUtils.isEmpty(crtPaybLmtAmt)?null:Double.valueOf(crtPaybLmtAmt));
                    insQgybStDt.setFundPayamt(org.springframework.util.StringUtils.isEmpty(fundPayamt)?null:Double.valueOf(fundPayamt));
                    insQgybStDt.setFundPayTypeName(fundPayTypeName);
                    insQgybStDt.setSetlProcInfo("");
                    insQgybStDt.setCreator(employee.getPkEmp());
                    insQgybStDt.setCreateTime(new Date());
                    insQgybStDt.setDelFlag("0");
                    insQgybStDt.setTs(new Date());
                    DataBaseHelper.insertBean(insQgybStDt);
                }
            }

            Map<String, Object> resMap = new HashMap<>();
            String curtime = DateUtils.getDateTimeStr(new Date());
            resMap.put("curDate", curtime.substring(0, curtime.length() - 4) + "0000"); //有效时间匹配到小时
            resMap.put("notDisplayFlagPv", "0");
            resMap.put("isNotShowPv", "0");
            resMap.put("pkPv", pvencounter.getPkPv());
            resMap.put("pkPi", pvencounter.getPkPi());

            //未计算金额
            //HIS未结算总金额 (需要结算部分)
            BigDecimal amountSt = BigDecimal.ZERO;
            List<BlPatiCgInfoNotSettleVO> mapResult = opcgPubHelperService.queryPatiCgInfoNotSettle(resMap);
            if (mapResult == null || mapResult.size() == 0) {
                dataSourceTransactionManager.rollback(transStatus); // 添加失败 回滚事务；
                listener.error("未查询到未结算的数据信息");
                return;
            }

            Iterator<BlPatiCgInfoNotSettleVO> iterator = mapResult.iterator();
            while (iterator.hasNext()) {
                BlPatiCgInfoNotSettleVO settleVO = iterator.next();
                amountSt = amountSt.add(settleVO.getAmount());
            }
            if (amountSt.compareTo(totalFee) != 0) {
                dataSourceTransactionManager.rollback(transStatus); // 添加失败 回滚事务；
                listener.error("传入总金额与HIS计算结果不符,传入金额【" + totalFee + "】,HIS计算金额【" + amountSt + "】");
                return;
            }
            //组装预结算 数据
            String mapResultJson = JsonUtil.writeValueAsString(mapResult);
            List<BlOpDt> opDts = JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {
            });
            OpCgTransforVo opCgforVo = new com.zebone.nhis.bl.pub.vo.OpCgTransforVo();
            opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
            opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
            opCgforVo.setBlOpDts(opDts);
            opCgforVo.setAggregateAmount(amountSt); //需支付总金额
            opCgforVo.setMedicarePayments(amountInsu);//医保支付
            opCgforVo.setPatientsPay(amountPi);//现金支付

            //调用预结算接口 countOpcgAccountingSettlement
            ResponseJson response = ApplicationUtils.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo, user);
            if (response.getStatus() != 0) {
                dataSourceTransactionManager.rollback(transStatus); // 添加失败 回滚事务；
                // 预结算接口调用失败 -处理异常消息：注意此接口调用要避免异常
                listener.error("HIS预结算接口【YjSettle】调用失败:" + response.getDesc());
                return;
            }
            opCgforVo = (com.zebone.nhis.bl.pub.vo.OpCgTransforVo) response.getData();
            opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
            opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
            opCgforVo.setBlOpDts(opDts);
            opCgforVo.setAmtInsuThird(amountInsu);

            //付款数据构建
            List<BlDeposit> depositList = new ArrayList<BlDeposit>();
            BlDeposit deposit = new BlDeposit();

            deposit.setDtPaymode(settlementMasterOutpat.getAccountTypeCode());
            deposit.setAmount(amountPi);
            deposit.setFlagAcc("0");
            deposit.setDelFlag("0");
            deposit.setPayInfo(payNo);//第三方订单号
            depositList.add(deposit);
            opCgforVo.setBlDeposits(depositList);
            opCgforVo.setPatientsPay(amountPi);

            //opCgforVo.setInvStatus("-2");//自费-不限制发票信息传入
            //打印电子票--传入计算机工作站名称、打印纸质标志不打印纸质
            List<InvoiceInfo> invos = new ArrayList<InvoiceInfo>();
            InvoiceInfo InvoiceInfo = new InvoiceInfo();
            InvoiceInfo.setFlagPrint("0");
            InvoiceInfo.setMachineName(codeEmp.toUpperCase());
            opCgforVo.setMachineName(codeEmp.toUpperCase());
            opCgforVo.setFlagPrint("0");
            invos.add(InvoiceInfo);
            opCgforVo.setInvoiceInfo(invos);



            //此状态控制事务提交后在发送消息
            opCgforVo.setSource("noSendMessage");
            ResponseJson respSettle = ApplicationUtils.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo, user);
            if (respSettle.getStatus() != 0) {
                dataSourceTransactionManager.rollback(transStatus); // 添加失败 回滚事务；
                listener.error("HIS结算接口【Settle】调用失败:" + respSettle.getDesc() + respSettle.getErrorMessage());
                return;
            }

            //保存第三方支付表
            opCgforVo = (OpCgTransforVo) respSettle.getData();

            //医保患者自费结算，先转为自费结算后，转为医保
            if("0".equals(isInsurance)) {
                updatePkInsu(pkInsu, pvencounter);
            }else {
                //HIS结算成功后回写医保结算记录中得pk_settle
                if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(doYbRetData, "data"))) {
                    String upYbSql="update ins_szyb_st set pk_settle=? where pk_insst=?";
                    DataBaseHelper.execute(upYbSql, new Object[]{opCgforVo.getPkSettle(),doYbRetData.get("data")});
                }
            }


            BlExtPay blExtPay = new BlExtPay();
            blExtPay.setAmount(BigDecimal.ZERO);
            blExtPay.setPkExtpay(NHISUUID.getKeyId());
            blExtPay.setPkPi(opCgforVo.getPkPi());
            blExtPay.setPkPv(opCgforVo.getPkPv());
            blExtPay.setDateAp(new Date());//请求时间
            blExtPay.setDatePay(date);//支付时间
            blExtPay.setAmount(amountPi);//现金支付
            blExtPay.setEuPaytype(settlementMasterOutpat.getAccountTypeCode());//付款方式
            blExtPay.setFlagPay("1");//支付标志
            blExtPay.setSerialNo(settlementMasterOutpat.getPaymentNo());//订单号  结算单号
            blExtPay.setTradeNo(settlementMasterOutpat.getSettlementNo());//系统订单号    TradeNo  系统订单号(微信退货交易时使用此域)
            blExtPay.setSysname(settlementMasterOutpat.getSourceSystemCode());//终端号
            blExtPay.setPkSettle(opCgforVo.getPkSettle());
            String sqlDepo = "select pk_depo from bl_deposit where pk_settle=?";
            Map<String, Object> depoMap = DataBaseHelper.queryForMap(sqlDepo, new Object[]{opCgforVo.getPkSettle()});
            blExtPay.setPkDepo(SDMsgUtils.getPropValueStr(depoMap, "pkDepo"));
            blExtPay.setCreator(user.getPkEmp());
            blExtPay.setCreateTime(new Date());
            blExtPay.setPkOrg(user.getPkOrg());
            blExtPay.setTs(new Date());
            blExtPay.setModifier(user.getPkEmp());
            blExtPay.setDelFlag("0");
            DataBaseHelper.insertBean(blExtPay);

            //生成电子票据
            List<BlInvoice> resInvList=new ArrayList<>();
            try {
                resInvList = mzOutElectInv(opCgforVo.getPkPv(), user, opCgforVo.getPkSettle());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //返回发票信息
            SettlementDetailOutpat settlementDetailOutpat = new SettlementDetailOutpat();
            settlementDetailOutpat.setEmpiId(settlementMasterOutpat.getEmpiId());
            settlementDetailOutpat.setPkPatient(settlementMasterOutpat.getPkPatient());
            List<BlInvoice> blInvoices = opCgforVo.getBlInvoices();
            if(blInvoices != null && blInvoices.size() > 0) {
                settlementDetailOutpat.setSettlementDetailId(blInvoices.get(0).getPkInvoice());
            }
            if(resInvList!=null && resInvList.size()>0){
                settlementDetailOutpat.setSettlementDetailId(resInvList.get(0).getPkInvoice());
            }
            List<DataElement> dataElementList = MessageBodyUtil.dataElementsReturnFactory(settlementDetailOutpat);

            dataSourceTransactionManager.commit(transStatus);




            //发送门诊收费信息至平台
            Map<String,Object> paramVo = new HashMap<String, Object>(16);
            paramVo.put("Control", "OK");
            paramVo.put("pkSettle", opCgforVo.getPkSettle());
            PlatFormSendUtils.sendBlOpSettleMsg(paramVo);

            listener.success(dataElementList);

		} catch (Exception e) {
			 listener.error(e.getMessage());
             dataSourceTransactionManager.rollback(transStatus);
             return;
		}
        
    }

    /**
     * 结算电子发票生成
     * @param pkPv
     * @param user
     * @param pkSettle
     */
    public List<BlInvoice> mzOutElectInv(String pkPv, User user, String pkSettle) throws Exception {
        //返回结果集
        Map<String, Object> result = null;
        List<BlInvoice> resInv = new ArrayList<>();
        Class<?> c = Class.forName("com.zebone.nhis.bl.pub.support.InvSettltService");
        //Method method= c.getMethod("repEBillMzOutpatient",String.class,IUser.class,String.class);
        //Map<String, Object> resInv = (Map<String, Object>) method.invoke(c.newInstance(),pkPv,user,pkSettle);
        //获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
        //String eBillFlag = invSettltService.getBL0031ByNameMachine(invoInfos.get(0).getNameMachine());
        Method method= c.getMethod("getBL0031ByNameMachine",String.class);
        String eBillFlag = (String) method.invoke(c.newInstance(),user.getCodeEmp().toUpperCase());
        if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
            // 调用开立票据接口生成票据信息
            Map<String,Object> paramMap = new HashMap<>(16);
            paramMap.put("pkPv", pkPv);
            paramMap.put("pkSettle", pkSettle);
            //是否打印纸质票据
            paramMap.put("flagPrint", "0");
            paramMap.put("invoInfos", null);
            paramMap.put("dateBegin", null);
            paramMap.put("dateEnd", null);
            paramMap.put("billList", null);
            result = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillMzOutpatient", new Object[]{paramMap,user});
            //如果发票数据返回为空
            if(result != null ){
                //生成发票记录
                resInv = saveBlInvoice(result,pkSettle);
            }
        }

        return resInv;
    }

    /**
     *  结算写入发票记录
     * @param resInv
     * @return
     */
    private List<BlInvoice> saveBlInvoice(Map<String, Object> resInv,String pkSettle){
        List<BlInvoice> result = new ArrayList<>();
        if(resInv!=null && resInv.size()>0 && resInv.containsKey("inv")){
            //主表数据  "inv", invs
            List<BlInvoice> invList = (List<BlInvoice>) resInv.get("inv");
            //从表数据 "invDt", invDtList
            List<BlInvoiceDt> invDtList = (List<BlInvoiceDt>) resInv.get("invDt");
            //发票与结算关联
            List<BlStInv> stinvList = new ArrayList<>();
            //组织电子票据信息
            if(invList!=null && invList.size()>0){
                //发票主表
                int[] ints = DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), invList);
                //发票细表
                int[] intsdt = DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), invDtList);
                //发票与明细关联
                for(BlInvoice invo : invList){
                    BlStInv vo = new BlStInv();
                    vo.setPkInvoice(invo.getPkInvoice());
                    vo.setPkOrg(invo.getPkOrg());
                    vo.setPkSettle(pkSettle);
                    vo.setPkStinv(NHISUUID.getKeyId());
                    setDefaultValue(vo, true);
                    stinvList.add(vo);
                }
                int[] intsStinv = DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), stinvList);

                result = invList;
            }
        }
        return result;
    }

    /**
     * 默认参数赋值
     * @param obj
     * @param flag
     */
    private void setDefaultValue(Object obj, boolean flag) {

        User user = UserContext.getUser();

        Map<String,Object> default_v = new HashMap<String,Object>(16);
        if(flag){
            // 如果新增
            default_v.put("pkOrg", user.getPkOrg());
            default_v.put("creator", user.getPkEmp());
            default_v.put("createTime",new Date());
            default_v.put("delFlag", "0");
        }

        default_v.put("ts", new Date());
        default_v.put("modifier",  user.getPkEmp());

        Set<String> keys = default_v.keySet();

        for(String key : keys){
            Field field = ReflectHelper.getTargetField(obj.getClass(), key);
            if (field != null) {
                ReflectHelper.setFieldValue(obj, key, default_v.get(key));
            }
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
	 * 处理医保结算数据
	 * @param
	 * @param
	 * @param pvencounter 就诊信息
	 * @return resMap:{"status":"1:成功；0:失败" ,"data":"错误返回信息/成功需要返回数据"}
	 */
    private Map<String,Object> saveYbSettleData(Map<String,Object> map,PvEncounter pvencounter){
    	User user = UserContext.getUser();
    	
    	Map<String,Object> resMap=new HashMap<String,Object>();
    	//ZPR-1 医疗机构编码
		//String yljgbm = CommonUtils.getPropValueStr(map, "2");
		//ZPR-2 医疗机构名称
		//String yljgmc = CommonUtils.getPropValueStr(map, "3");
		//ZPR-3 门诊流水号
		//String mzlsh = CommonUtils.getPropValueStr(map, "4");
		//ZPR-4单据号
		String Djh = CommonUtils.getPropValueStr(map, "akc190");//就医登记号
		//ZPR-5医疗证号
		//String Ylzh = CommonUtils.getPropValueStr(map, "6");
		//ZPR-6电脑号
		//String Dnh =  CommonUtils.getPropValueStr(map, "7");
		//ZPR-7姓名
		//String xm = CommonUtils.getPropValueStr(map, "8");
		//ZPR-8金额
		String je = CommonUtils.getPropValueStr(map, "akc264");//结算金额
		//现金合计 9
		String Xjhj = CommonUtils.getPropValueStr(map, "akb067");//个人支付
		//记账合计 10
		String Jzhj = CommonUtils.getPropValueStr(map, "akb066");//个人账户支付
		//结算序列号 11
		String Jsywh = CommonUtils.getPropValueStr(map, "bke384");//机构结算业务号
		//统筹合计 12
		String Tchj = CommonUtils.getPropValueStr(map, "akb068");//基金支付
		//业务号 ywh 13
		String ywh = CommonUtils.getPropValueStr(map, "ckc618");//医保结算号
		if(ywh!=null && ywh.length()>20){
			ywh = ywh.substring(0, 20);
		}
		resMap.put("amountPi", StringUtils.isBlank(Xjhj)?"0":Xjhj);//自费金额
		resMap.put("amountSt", StringUtils.isBlank(je)?"0":je);//合计金额
		resMap.put("amountInsu", StringUtils.isBlank(Jzhj)?"0":Jzhj);//医保金额
		resMap.put("amountFund", StringUtils.isBlank(Tchj)?"0":Tchj);//基金支付金额
		InsSzybSt insSzybSt=new InsSzybSt();
		insSzybSt.setPkInsst(NHISUUID.getKeyId());
		insSzybSt.setPkOrg(user.getPkOrg());//机构
		String visitSql="select * from ins_szyb_visit where del_flag='0'  and pk_pv=? and pvcode_ins=? order by date_reg desc ";
		List<InsSzybVisit> visitList = DataBaseHelper.queryForList(visitSql, InsSzybVisit.class,new Object[]{pvencounter.getPkPv(),Djh});
		if(visitList==null ||visitList.size()==0){
			resMap.put("status", "false");//1:成功；0：失败
			String message="HIS接口【诊中支付（医保）】调用失败:未查询到关联医保登记数据";
			resMap.put("data", message);
			return resMap;
		}
		InsSzybVisit insSzybVisit = visitList.get(0);
		insSzybSt.setPkVisit(insSzybVisit.getPkVisit());//医保登记主键
		insSzybSt.setPkHp(insSzybVisit.getPkHp());//医保主键
		insSzybSt.setPkPv(pvencounter.getPkPv());//就诊主键
		insSzybSt.setPkPi(pvencounter.getPkPi());//患者主键
		insSzybSt.setDateInp(pvencounter.getDateBegin());//入院日期
		insSzybSt.setDateOutp(null);//出院日期
		insSzybSt.setDays(1);//实际住院日期
		insSzybSt.setPkSettle("");//结算主键--先给空，结算完成更新此字段
		insSzybSt.setPvcodeIns(Djh);//就医登记号
		insSzybSt.setCodeSerialno(Jsywh);//医药机构结算业务
		insSzybSt.setDateSt(null);//结算日期
		insSzybSt.setAmount(Double.valueOf(je));//结算金额
		insSzybSt.setCodeCenter("");//中心编码
		insSzybSt.setCodeOrg(ApplicationUtils.getPropertyValue("yb.codeOrg", ""));//医院编码
		insSzybSt.setNameOrg(ApplicationUtils.getPropertyValue("yb.nameOrg", ""));//医院名称
		insSzybSt.setTransid(Jsywh);//出院登记流水号
		insSzybSt.setBillno("");//发票号
		insSzybSt.setCodeHpst(ywh);////医保结算号   --取消医保结算号
		insSzybSt.setCreator(user.getPkEmp());//创建人
		insSzybSt.setCreateTime(new Date());//创建时间
		insSzybSt.setDelFlag("0");//删除标志
		insSzybSt.setTs(new Date());//时间戳
		DataBaseHelper.insertBean(insSzybSt);









		InsSzybStCity cityst=new InsSzybStCity();
		cityst.setPkInsstcity(NHISUUID.getKeyId());
		cityst.setPkOrg(user.getPkOrg());
		cityst.setPkInsst(insSzybSt.getPkInsst());
		cityst.setEuTreattype("3");
		cityst.setDtMedicate("11");//医疗类别
		cityst.setDiagcodeInp("");//入院诊断编码
		cityst.setDiagnameInp("");//入院诊断名称
		cityst.setReasonOutp("");//出院
		cityst.setDiagcodeOutp("");//出院诊断编码
		cityst.setDiagnameOutp("");//出院诊断名称
		cityst.setDiagcode2Outp("");//出院诊断编码
		cityst.setDiagname2Outp("");//出院诊断名称
		cityst.setStatusOutp("");//出院情况
		cityst.setAmtJjzf(Double.valueOf(Tchj));//基金支付
		cityst.setAmtGrzhzf(Double.valueOf(Jzhj));//个人账户支付
		cityst.setAmtGrzf(Double.valueOf(Xjhj));//个人支付
		//cityst.setAmtGrzh(0.00);//个人账户
		cityst.setCreateTime(new Date());
		cityst.setCreator(user.getPkEmp());
		cityst.setDelFlag("0");
		cityst.setTs(new Date());
		DataBaseHelper.insertBean(cityst);
		
		List<Map<String, Object>> outputlist1 = (List<Map<String, Object>>)map.get("outputlist1");
		List<Map<String, Object>> outputlist2 = (List<Map<String, Object>>)map.get("outputlist2");
		List<Map<String, Object>> outputlist3 = (List<Map<String, Object>>)map.get("outputlist3");	
		List<InsSzybStCitydt> citydtList=new ArrayList<InsSzybStCitydt>();	
		for (Map<String, Object> output1 : outputlist1) {
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(CommonUtils.getPropValueStr(output1, "aka111"));//大类代码
			citydt.setCategory(CommonUtils.getPropValueStr(output1, "aka111"));//大类代码
			String amt=CommonUtils.getPropValueStr(output1, "bka058");//费用金额
			amt = "".equals(amt)?"0":amt;
			citydt.setAmtFee(Double.valueOf(amt));//费用金额
			citydt.setPkInsstcitydt(NHISUUID.getKeyId());
			citydt.setPkInsstcity(cityst.getPkInsstcity());
			citydt.setCreator(user.getPkEmp());
			citydt.setCreateTime(new Date());
			citydt.setDelFlag("0");
			citydt.setTs(new Date());
			citydtList.add(citydt);
		}
		
		for (Map<String, Object> output2 : outputlist2) {
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(CommonUtils.getPropValueStr(output2, "aaa036"));//支付项目代码
			citydt.setCategory(CommonUtils.getPropValueStr(output2, "aaa036"));//支付项目代码
			String amt=CommonUtils.getPropValueStr(output2, "aae019");//金额
			amt = "".equals(amt)?"0":amt;
			citydt.setAmtFee(Double.valueOf(amt));//费用金额
			citydt.setPkInsstcitydt(NHISUUID.getKeyId());
			citydt.setPkInsstcity(cityst.getPkInsstcity());
			citydt.setCreator(user.getPkEmp());
			citydt.setCreateTime(new Date());
			citydt.setDelFlag("0");
			citydt.setTs(new Date());
			citydtList.add(citydt);
		}
		for (Map<String, Object> output3 : outputlist3) {
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(CommonUtils.getPropValueStr(output3, "aka037"));//个人累计信息类别
			citydt.setCategory(CommonUtils.getPropValueStr(output3, "aka037"));//个人累计信息类别
			String amt=CommonUtils.getPropValueStr(output3, "bke264");//可用值
			amt = "".equals(amt)?"0":amt;
			citydt.setAmtFee(Double.valueOf(amt));//费用金额
			citydt.setPkInsstcitydt(NHISUUID.getKeyId());
			citydt.setPkInsstcity(cityst.getPkInsstcity());
			citydt.setCreator(user.getPkEmp());
			citydt.setCreateTime(new Date());
			citydt.setDelFlag("0");
			citydt.setTs(new Date());
			citydtList.add(citydt);
		}

		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybStCitydt.class),citydtList);
		resMap.put("status", "1");//1:成功；0：失败
		resMap.put("data", insSzybSt.getPkInsst());
		return resMap;
    }
    
}
