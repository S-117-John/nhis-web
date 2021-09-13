package com.zebone.nhis.webservice.syx.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdPayer;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.webservice.syx.dao.PlatFormPayOrderMapper;
import com.zebone.nhis.webservice.syx.support.SetUserUtils;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.web.support.ResponseJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PlatFormPayOrderService {

    @Autowired
    private PlatFormPayOrderMapper payOrderMapper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formattter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private User u = new User();

    /**
     * 3.13	挂号支付接口
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String payOrderInfo(String content) throws Exception {
        PayOrderReqSubject reqData = (PayOrderReqSubject) XmlUtil.XmlToBean(content, PayOrderReqSubject.class);
        u = SetUserUtils.setUserByEmpCode(reqData.getSender().getSenderId());
        List<PayOrder> payOrderList = reqData.getSubject();
        PayOrder payOrder = payOrderList.get(0);

        /**查询预约信息*/
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dtApptype", CommonUtils.isEmptyString(payOrder.getOrderType()) ? "" : payOrder.getOrderType());//预约来源
        paramMap.put("orderidExt", CommonUtils.isEmptyString(payOrder.getOrderId()) ? "" : payOrder.getOrderId());//订单号
        SchApptVo schAppt = payOrderMapper.qrySchApptInfo(paramMap);

        //res
        PayOrderRes res = new PayOrderRes();
        //subject
        PayOrderResSubject subject = new PayOrderResSubject();
        //result
        PayOrderResResult payOrderResResult = new PayOrderResResult();
        //response
        PayOrderResExd payOrderResExd = new PayOrderResExd();

        if (schAppt != null) {
            boolean isSuc = true;//标志是否可以进行处理his业务
            if (schAppt.getFlagCancel().equals("1")) {//已取消
                res.setResultCode("-1");
                res.setResultDesc("预约来源(" + schAppt.getApptypeName() + ")的订单号(" + payOrder.getOrderId() + ")对应的挂号记录已取消。");
                isSuc = false;
            }
            if (isSuc && schAppt.getFlagPay().equals("1")) {//已支付
                res.setResultCode("-1");
                res.setResultDesc("预约来源(" + schAppt.getApptypeName() + ")的订单号(" + payOrder.getOrderId() + ")对应的挂号记录已支付。");
                isSuc = false;
            }

            if (isSuc) {
                //查询患者基本信息
                PiMaster master = DataBaseHelper.queryForBean("select * from pi_master where pk_pi = ?",
                        PiMaster.class, new Object[]{schAppt.getPkPi()});
                if (master != null) {
                    /**1.1保存PV_ENCOUNTER、pv_op、PvInsurance信息*/
                    PvEncounter pvEncounter = savePvEncounter(master, schAppt);
                    /**1.2据就诊服务pk_schsrv生成挂号费用信息*/
                    ApplicationUtils apputil = new ApplicationUtils();
                    //组装用户信息
                    u.setPkOrg(schAppt.getPkOrg());
                    UserContext.setUser(u);
                    //组装入 参信息
                    Map<String, Object> cgParam = new HashMap<>();
                    cgParam.put("pkSchsrv", schAppt.getPkSchsrv());
                    cgParam.put("pkInsu", pvEncounter.getPkInsu());
                    BdHp hp = payOrderMapper.qryHpInfo(pvEncounter.getPkInsu());
                    cgParam.put("nameInsu", hp.getName());
                    cgParam.put("euPvType", pvEncounter.getEuPvtype());
                    cgParam.put("pkPicate", pvEncounter.getPkPicate());
                    cgParam.put("flagSpec", pvEncounter.getFlagSpec());
                    cgParam.put("dateBirth", sdf.format(master.getBirthDate()));

                    //获取挂号费用
                    ResponseJson rs = apputil.execService("PV", "RegSyxService", "getItemBySrv", cgParam, u);
                    List<Object> itemObj = (List<Object>) rs.getData();
                    List<ItemPriceVo> itemList = Lists.newArrayList();
                    for (Object temp : itemObj) {
                        ItemPriceVo itemPriceVo = new ItemPriceVo();
                        ApplicationUtils.copyProperties(itemPriceVo, temp);
                        itemList.add(itemPriceVo);
                    }
                    if (itemList.size() > 0) {
                        //判断外部系统的支付金额与系统待支付金额是否一致
                        Double amtAll = 0D;
                        for (ItemPriceVo priceVo : itemList) {
                            amtAll = MathUtils.add(amtAll, priceVo.getPriceOrg());
                        }
                        if (MathUtils.compareTo(amtAll, payOrder.getPayAmout()) == 0) {
                            /**1.3组织记费明细信息写表bl_op_dt*/
                            BlIpCgVo cgVo = new BlIpCgVo();
                            cgVo.setItems(itemList);
                            cgVo.setCodeCg(ApplicationUtils.getCode("0601"));
                            cgVo.setPv(pvEncounter);
                            ResponseJson dtsRs = apputil.execService("BL", "CgProcessHandler", "constructBlOpDtWs", cgVo, u);
                            List<BlOpDt> opDts = (List<BlOpDt>) dtsRs.getData();
                            if (opDts != null && opDts.size() > 0)
                                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), opDts);
                            try {
                                Map<String, Object> orderMap = new HashMap<>();
                                orderMap.put("payNum", payOrder.getPayNum());
                                orderMap.put("orderId", payOrder.getOrderId());
                                orderMap.put("payAmount", payOrder.getPayAmout());
                                orderMap.put("payDesc", payOrder.getPayDesc());
                                Date date =  formattter.parse(payOrder.getPayTime());
                                orderMap.put("payTime", dateTrans(date));

                                /**1.6基于bl_op_dt记录写结算表bl_settle、bl_settle_detail、bl_deposit*/
                                saveSettle(pvEncounter, opDts, null, orderMap);
                                //更新sch_appt支付标志flag_pay=1
                                DataBaseHelper.execute("update sch_appt set FLAG_PAY = '1' where PK_SCHAPPT = ?", new Object[]{schAppt.getPkSchappt()});
                                DataBaseHelper.execute("update sch_appt_pv set pk_pv = ? where pk_apptpv = ?", new Object[]{pvEncounter.getPkPv(),schAppt.getPkApptpv()});
                                
                                String sql = "select appt.code from sch_appt appt inner join pv_op op on op.pk_schappt=appt.pk_schappt where op.orderid_ext=?";
                        		List<Map<String, Object>> orderHisId = DataBaseHelper.queryForList(sql, new Object[]{reqData.getSubject().get(0).getOrderId()});
                                res.setOrderIdHis((String) orderHisId.get(0).get("code"));
                                res.setResultCode("0");
                                res.setResultDesc("支付成功！");
                            } catch (Exception e) {
                                throw e;
                            }

                        } else {
                            throw new BusException("预约来源(" + schAppt.getApptypeName() + ")的订单号(" + payOrder.getOrderId() + ")支付金额(" + CommonUtils.getString(payOrder.getPayAmout()) + ")不正确。");
                        }
                    } else {
                        throw new BusException("未获取到挂号费用，请检查！");
                    }
                } else {
                    throw new BusException("未查询到患者基本信息，请检查！");
                }
            }
        } else {
            //查询预约来源码表信息
            List<BdDefdoc> docList = payOrderMapper.qryApptypeDocList(null);
            String apptypeName = "";
            if (docList != null && docList.size() > 0) {
                for (BdDefdoc doc : docList) {
                    if (doc.getCode().equals(payOrder.getOrderType())) {
                        apptypeName = doc.getName();
                        break;
                    }
                }
            }

            res.setResultCode("-1");
            res.setResultDesc("预约来源(" + apptypeName + ")的订单号(" + payOrder.getOrderId() + ")没有对应的挂号记录。");
        }

        subject.setRes(res);

        payOrderResResult.setId(res.getResultCode().equals("0") ? "AA" : "AE");
        payOrderResResult.setText(res.getResultCode().equals("0") ? "处理成功!" : "处理失败!");
        payOrderResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        payOrderResResult.setRequestId(reqData.getId());
        payOrderResResult.setSubject(subject);

        payOrderResExd.setResult(payOrderResResult);

        String payOrderXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, payOrderResExd), PayOrderResExd.class);
        return payOrderXml;
    }

    /**
     * 3.15	挂号退费接口
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String rtnPayInfo(String content) throws Exception {
        PayOrderReqSubject reqData = (PayOrderReqSubject) XmlUtil.XmlToBean(content, PayOrderReqSubject.class);
        List<PayOrder> payOrderList = reqData.getSubject();
        PayOrder payOrder = payOrderList.get(0);

        /*查询预约就诊信息*/
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dtApptype", CommonUtils.isEmptyString(payOrder.getOrderType()) ? "" : payOrder.getOrderType());//预约来源
        paramMap.put("orderidExt", CommonUtils.isEmptyString(payOrder.getOrderId()) ? "" : payOrder.getOrderId());//订单号
        SchApptVo schAppt = payOrderMapper.qrySchApptPvInfo(paramMap);

        //res
        PayOrderRes res = new PayOrderRes();
        //subject
        PayOrderResSubject subject = new PayOrderResSubject();
        //result
        PayOrderResResult payOrderResResult = new PayOrderResResult();
        //response
        PayOrderResExd payOrderResExd = new PayOrderResExd();

        if (schAppt != null) {
            boolean isSuc = true;//标志是否可以进行处理his业务
            if (schAppt.getFlagCancel().equals("1")) {//已取消
                res.setResultCode("-1");
                res.setResultDesc("预约来源(" + schAppt.getApptypeName() + ")的订单号(" + payOrder.getOrderId() + ")对应的挂号记录已取消。");
                isSuc = false;
            }
            if ("1".equals(schAppt.getFlagSpec())) {
                res.setResultCode("-1");
                res.setResultDesc("专家号不可取消挂号！");
                isSuc = false;
            }
            if (isSuc && (CommonUtils.isEmptyString(schAppt.getFlagPay()) || "0".equals(schAppt.getFlagPay()))) {
                res.setResultCode("-1");
                res.setResultDesc("预约来源(" + schAppt.getApptypeName() + ")的订单号(" + payOrder.getOrderId() + ")对应的挂号记录未支付。");
                isSuc = false;
            }
            if (isSuc && schAppt.getDateAppt().compareTo(new Date()) <= 0) {
                res.setResultCode("-1");
                res.setResultDesc("预约来源(" + schAppt.getApptypeName() + ")的订单号(" + payOrder.getOrderId() + ")就诊当日的预约号不可退!");
                isSuc = false;
            }
//            if (isSuc && !CommonUtils.isEmptyString(schAppt.getPkInvoice())) {
//                res.setResultCode("-1");
//                res.setResultDesc("预约来源(" + schAppt.getApptypeName() + ")的订单号(" + payOrder.getOrderId() + ")已到挂号处打印发票。");
//                isSuc = false;
//            }
            if (isSuc && !CommonUtils.isEmptyString(schAppt.getFlagCancelPv()) && "1".equals(schAppt.getFlagCancelPv())) {
                res.setResultCode("-1");
                res.setResultDesc("预约来源(" + schAppt.getApptypeName() + ")的订单号(" + payOrder.getOrderId() + ")已被退号。");
                isSuc = false;
            }
            if (isSuc && "1".equals(schAppt.getPvEuStatus())) {
                res.setResultCode("-1");
                res.setResultDesc("预约来源(" + schAppt.getApptypeName() + ")的订单号(" + payOrder.getOrderId() + ")已被接诊。");
                isSuc = false;
            }
            //增加不允许外部预约系统退号的门诊医生分类，校验是否允许退号(逻辑待写)

            /*取消结算*/
            if (isSuc) {
                //组装用户信息
                u.setPkOrg(schAppt.getPkOrg());
                UserContext.setUser(u);

                /*1.1取消结算 写表bl_settle，bl_settle_detail、bl_deposit、bl_op_dt*/
                canlSettle(schAppt.getPkPv());
                /*1.2退诊处理 更新pv_encounter*/
                DataBaseHelper.execute("update PV_ENCOUNTER set EU_STATUS = '9' where PK_PV = ?", new Object[]{schAppt.getPkPv()});
                /*1.3释放号源*/
                //查询释放号源方式
                Map<String, Object> reasMap = DataBaseHelper.queryForMap("select eu_releasetype from sch_sch where pk_sch = ?",
                        new Object[]{schAppt.getPkSch()});
                if (reasMap != null && reasMap.size() > 0) {
                    if ("1".equals(CommonUtils.getString(reasMap.get("euReleasetype")))) {
                        /*1.3.1值为1时，更新pv_op表flag_norelease字段为1,取消预约，更新sch_appt表*/
                        DataBaseHelper.execute("update pv_op set flag_norelease = '1' where pk_pv = ?", new Object[]{schAppt.getPkPv()});
                        DataBaseHelper.execute("update sch_appt set EU_STATUS='9',FLAG_CANCEL='1',PK_EMP_CANCEL='-1',NAME_EMP_CANCEL='-1' where PK_SCHAPPT = ?", new Object[]{schAppt.getPkSchappt()});
                    } else if ("0".equals(CommonUtils.getString(reasMap.get("euReleasetype")))) {
                        /*1.3.2值为0时，取消预约更新sch_appt表，更新sch_sch表，更新sch_ticket表*/
                        DataBaseHelper.execute("update sch_appt set EU_STATUS='9',FLAG_CANCEL='1',PK_EMP_CANCEL='-1', NAME_EMP_CANCEL='-1' where PK_SCHAPPT = ?", new Object[]{schAppt.getPkSchappt()});
                        DataBaseHelper.update("update sch_ticket set FLAG_USED ='0' where ticketno = ? and pk_sch = ?", new Object[]{schAppt.getTicketNo(), schAppt.getPkSch()});
                        DataBaseHelper.execute("update sch_sch set cnt_used=cnt_used-1 where pk_sch=?", schAppt.getTicketNo(), schAppt.getPkSch());
                    }
                }
                res.setResultCode("0");
                res.setResultDesc("成功！");
            }
        } else {
            //查询预约来源码表信息
            List<BdDefdoc> docList = payOrderMapper.qryApptypeDocList(null);
            String apptypeName = "";
            if (docList != null && docList.size() > 0) {
                for (BdDefdoc doc : docList) {
                    if (doc.getCode().equals(payOrder.getOrderType())) {
                        apptypeName = doc.getName();
                        break;
                    }
                }
            }

            res.setResultCode("-1");
            res.setResultDesc("预约来源(" + apptypeName + ")的订单号(" + payOrder.getOrderId() + ")没有对应的挂号记录。");
        }

        subject.setRes(res);

        payOrderResResult.setId(res.getResultCode().equals("0") ? "AA" : "AE");
        payOrderResResult.setText(res.getResultCode().equals("0") ? "处理成功!" : "处理失败!");
        payOrderResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        payOrderResResult.setRequestId(reqData.getId());
        payOrderResResult.setSubject(subject);

        payOrderResExd.setResult(payOrderResResult);

        String payOrderXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, payOrderResExd), PayOrderResExd.class);
        return payOrderXml;
    }

    /**
     * 3.22	用户待缴费记录支付接口
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String payBigOrder(String content) throws Exception {
        PayBigOrderInfoReqSubject reqData = (PayBigOrderInfoReqSubject) XmlUtil.XmlToBean(content, PayBigOrderInfoReqSubject.class);
        SetUserUtils.setUserByOldId(reqData.getSender().getSenderId());
        List<PayBigOrderInfo> list = reqData.getSubject();
        String[] splitAddress=reqData.getSubject().get(0).getOrderDetailInfo().get(0).getDetailId().split("\\,");
        List<OrderDetailInfo> lists  = new ArrayList<OrderDetailInfo>();
        for (String detailID : splitAddress) {
        	OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        	orderDetailInfo.setDetailId(detailID.trim());
        	lists.add(orderDetailInfo);
		}                
        PayBigOrderInfo payBigOrder = list.get(0);
        payBigOrder.setOrderDetailInfo(lists);
        //res
        PayBigOrderResInfo res = new PayBigOrderResInfo();
        //subject
        PayBigOrderInfoResSubject subject = new PayBigOrderInfoResSubject();
        //result
        PayBigOrderInfoResResult payOrderResResult = new PayBigOrderInfoResResult();
        //response
        PayBigOrderInfoResExd payOrderResExd = new PayBigOrderInfoResExd();

        //查询患者就诊信息
        PvEncounter pv = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where CODE_PV = ? and EU_PVTYPE='1'",
                PvEncounter.class, new Object[]{payBigOrder.getInfoSeq()});

        if (pv != null) {
            boolean isSuc = true;//标志是否可以进行处理his业务
            PiMaster master = DataBaseHelper.queryForBean("select * from pi_master where pk_pi = ?",
                    PiMaster.class, new Object[]{pv.getPkPi()});
            //校验身份证号是否相同
            if (payBigOrder.getUserCardType().equals("0") && !payBigOrder.getUserCardId().equals(master.getIdNo())) {
                res.setResultCode("-1");
                res.setResultDesc("相应的证件号(患者身份证件号码)和就诊登记号信息不匹配。");
                isSuc = false;
            }
            //校验医保卡号是否相同
            if (isSuc && payBigOrder.getUserCardType().equals("3") && !payBigOrder.getUserCardId().equals(master.getInsurNo())) {
                res.setResultCode("-1");
                res.setResultDesc("相应的证件号(患者医保卡号码)和就诊登记号信息不匹配。");
                isSuc = false;
            }
            //校验监护人身份证是否相同
            if (isSuc && payBigOrder.getUserCardType().equals("4") && !payBigOrder.getUserCardId().equals(pv.getIdnoRel())) {
                res.setResultCode("-1");
                res.setResultDesc("相应的证件号(患者监护人身份证件号码)和就诊登记号信息不匹配。");
                isSuc = false;
            }
            //校验患者电话是否相同
            if (isSuc && payBigOrder.getUserCardType().equals("5") && !payBigOrder.getUserCardId().equals(master.getTelNo())) {
                res.setResultCode("-1");
                res.setResultDesc("相应的证件号(患者电话)和就诊登记号信息不匹配。");
                isSuc = false;
            }
            //校验待结算费用明细条目数是否相同
            Set<String> pkList = new HashSet<>();
            for (OrderDetailInfo vo : payBigOrder.getOrderDetailInfo()) {
                pkList.add(vo.getDetailId());
            }
            String qrySql = "select * from bl_op_dt where pk_cgop in (" + CommonUtils.convertSetToSqlInPart(pkList, "pk_cgop") + ")";
            List<BlOpDt> opDts = DataBaseHelper.queryForList(qrySql, BlOpDt.class, new Object[]{});
            if (isSuc && opDts.size() != payBigOrder.getOrderDetailInfo().size()) {
                res.setResultCode("-1");
                res.setResultDesc("未缴费的费用明细信息不匹配。");
                isSuc = false;
            }
            if (isSuc) {
                /*需添加HISKEY的校验逻辑待写*/

                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("pkPv", pv.getPkPv());
                //查询患者医保信息
                BdHp hpInfo = payOrderMapper.qryHpInfo(pv.getPkInsu());
                SchApptVo schAppt = payOrderMapper.qrySchApptPvInfo(paramMap);//查询预约信息
                if (!hpInfo.getEuHptype().equals("0")) {//患者医保不是自费
                    //且参数operatetype(预约来源)包含在BL0044参数值，且患者医保扩展属性0319=1，且未收费明细不包含草药，或未收费的草药只有1个,草药的扩展属性0506值0，或未收费草药大于1个时
                    String sysParam = ApplicationUtils.getSysparam("BL0044", false);
                    if (!CommonUtils.isEmptyString(sysParam)) {
                        String[] apptypeAry = sysParam.split(",");
                        boolean flag = false;
                        //查询operatetype(预约来源)包含在BL0044参数值
                        for (String apptype : apptypeAry) {
                            if (schAppt.getDtApptype().equals(apptype)) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            Integer cnt = payOrderMapper.qryCyCntByPv(paramMap);
                            if (cnt == null || cnt > 1) {

                                //且未收费明细不包含草药，或未收费草药大于1个(或未收费的草药只有1个，草药的扩展属性0506值0[括号里的逻辑待写])


                                //校验医保金额正确性
                                OpGyAmtVo amtVo = qryYbAmtInfo(pv, opDts);
                                if (amtVo.getAmountSt().compareTo(BigDecimal.valueOf(payBigOrder.getPayAmout())) == 0) {
                                    /*his结算业务处理*/
                                    Map<String, Object> orderMap = new HashMap<>();
                                    orderMap.put("payNum", payBigOrder.getPayCardNum());
                                    orderMap.put("orderId", payBigOrder.getOrderId());
                                    orderMap.put("payAmount", amtVo.getAmountPi());
                                    orderMap.put("payDesc", "");
                                    Date date =  formattter.parse(payBigOrder.getPayTime());
                                    orderMap.put("payTime", dateTrans(date));
                                    orderMap.put("payMode", payBigOrder.getPayMode());

                                    /*1.6基于bl_op_dt记录写结算表bl_settle、bl_settle_detail、bl_deposit*/
                                    BlSettle st = saveSettle(pv, opDts, amtVo, orderMap);
                                    /*组装res信息*/
                                    res.setResultCode("0");
                                    res.setResultDesc("结算成功！");
                                    res.setOrderIdHis(st.getCodeSt());
                                    res.setOrderDesc("");//待写
                                } else {
                                    res.setResultCode("-1");
                                    res.setResultDesc("本次就诊记录请在门诊收费处人工窗口处理!");
                                }
                            } else {
                                res.setResultCode("-1");
                                res.setResultDesc("本次就诊记录请在门诊收费处人工窗口处理!");
                            }

                        } else {
                            res.setResultCode("-1");
                            res.setResultDesc("本次就诊记录请在门诊收费处人工窗口处理!");
                        }
                    } else {
                        res.setResultCode("-1");
                        res.setResultDesc("本次就诊记录请在门诊收费处人工窗口处理!");
                    }
                } else {
                    /*his结算业务处理*/
                    Map<String, Object> orderMap = new HashMap<>();
                    orderMap.put("payNum", payBigOrder.getPayCardNum());
                    orderMap.put("orderId", payBigOrder.getOrderId());
                    orderMap.put("payAmount", payBigOrder.getPayAmout());
                    orderMap.put("payDesc", "");
                    Date date =  formattter.parse(payBigOrder.getPayTime());
                    orderMap.put("payTime", dateTrans(date));
                    orderMap.put("payMode", payBigOrder.getPayMode());

                    /*1.6基于bl_op_dt记录写结算表bl_settle、bl_settle_detail、bl_deposit*/
                    BlSettle st = saveSettle(pv, opDts, null, orderMap);

                    //	返回结算编码（orderidhis）和描述信息（orderdesc）描述信息包括取药药房，执行信息信息
                    Map<String,Object> codeAndDesc = payOrderMapper.qryCodeAndDesc(st.getCodeSt());

                    /*组装res信息*/
                    res.setResultCode("0");
                    res.setResultDesc("结算成功！");
                    res.setOrderIdHis(st.getCodeSt());
                    res.setOrderDesc(codeAndDesc.get("orderDesc") == null ? "" : codeAndDesc.get("orderDesc").toString());
                }
            }
        } else {
            res.setResultCode("-1");
            res.setResultDesc("就诊登记号(" + payBigOrder.getInfoSeq() + ")没有HIS系统找到对应的就诊记录。");
        }

        subject.setRes(res);

        payOrderResResult.setId(res.getResultCode().equals("0") ? "AA" : "AE");
        payOrderResResult.setText(res.getResultCode().equals("0") ? "处理成功!" : "处理失败!");
        payOrderResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        payOrderResResult.setRequestId(reqData.getId());
        payOrderResResult.setSubject(subject);

        payOrderResExd.setResult(payOrderResResult);

        String payOrderXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, payOrderResExd), PayBigOrderInfoResExd.class);
        return payOrderXml;

    }

    /**
     * 获取医保金额信息
     *
     * @param pv
     * @param opDtList
     * @return
     */
    private OpGyAmtVo qryYbAmtInfo(PvEncounter pv, List<BlOpDt> opDtList) throws Exception {
        QryYbInfoVo qryVo = new QryYbInfoVo();
        qryVo.setPv(pv);
        qryVo.setBlOpDts(opDtList);

        //查询患者医保是否是公医
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pkHp", pv.getPkInsu());
        paramMap.put("codeAttr", "0301");
        String hpAttr = payOrderMapper.qryHpValAttr(paramMap);
        //公医
        OpGyAmtVo amtVo = new OpGyAmtVo();
        if (CommonUtils.isEmptyString(hpAttr) && hpAttr.equals("1")) {
            ApplicationUtils apputil = new ApplicationUtils();
            //组装用户信息
            u.setPkOrg(pv.getPkOrg());
            UserContext.setUser(u);
            ResponseJson dtsRs = apputil.execService("BL", "OgCgStrategyPubService", "opSyxSettle", qryVo, u);
            amtVo = (OpGyAmtVo) dtsRs.getData();
        } else {//外部医保
            //调用外部医保接口(需要黄瑞炎添加)

        }

        return amtVo;
    }

    /**
     * 生成就诊信息
     *
     * @param master
     * @return
     */
    private PvEncounter savePvEncounter(PiMaster master, SchApptVo schAppt) throws Exception {
        /**1.1保存PvEncounter表*/
        PvEncounter pvEncounter = new PvEncounter();
        pvEncounter.setPkPv(NHISUUID.getKeyId());
        pvEncounter.setPkPi(master.getPkPi());
        pvEncounter.setPkDept(schAppt.getPkDept());//就诊科室
        pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
        pvEncounter.setEuPvtype("1"); //门诊
        pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // 登记
        pvEncounter.setNamePi(master.getNamePi());
        pvEncounter.setDtSex(master.getDtSex());
        pvEncounter.setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(),pvEncounter.getDateBegin()));
        pvEncounter.setAddress(master.getAddress());
        pvEncounter.setFlagIn("0");
        pvEncounter.setFlagSettle("1");
        pvEncounter.setDtMarry(master.getDtMarry());
        //查询患者主医保
        List<PiInsurance> piInsus = payOrderMapper.qryPiInsu(master.getPkPi());
        if (piInsus != null && piInsus.size() > 0) {
            pvEncounter.setPkInsu(piInsus.get(0).getPkHp());
        } else {
            List<Map<String, Object>> pkHp = DataBaseHelper.queryForList("select pk_hp from bd_hp where EU_HPTYPE = '0' ", new Object[]{});
            if (pkHp == null) {
                throw new BusException("请维护一个全自费的医保计划！");
            }
            pvEncounter.setPkInsu(pkHp.get(0).get("pkHp").toString());
        }
        pvEncounter.setPkPicate(master.getPkPicate());
        pvEncounter.setPkEmpReg(UserContext.getUser().getPkEmp());
        pvEncounter.setNameEmpReg(UserContext.getUser().getNameEmp());
        pvEncounter.setDateReg(new Date());
        if (schAppt.getDateAppt() != null) {//如果是预约挂号，开始日期为预约日期
            pvEncounter.setDateBegin(schAppt.getDateAppt());
        } else {
            pvEncounter.setDateBegin(schAppt.getDateReg());//挂号的排班日期
        }
        pvEncounter.setPkEmpPhy(schAppt.getPkEmp());
        pvEncounter.setFlagCancel("0");
        pvEncounter.setDtIdtypeRel("01");
        pvEncounter.setDtPvsource(master.getDtSource());
        pvEncounter.setNameRel(master.getNameRel());
        pvEncounter.setIdnoRel(master.getIdnoRel());
        pvEncounter.setTelRel(master.getTelRel());
        pvEncounter.setEuPvmode("0");
        pvEncounter.setFlagSpec("2".equals(schAppt.getEuSrvtype()) ? "1" : "0");
        pvEncounter.setEuStatusFp("0");
        pvEncounter.setEuLocked("0");
        pvEncounter.setEuDisetype("0");
        DataBaseHelper.insertBean(pvEncounter);
        /**1.2保存pv_op*/
        PvOp pvOp = new PvOp();
        pvOp.setPkPv(pvEncounter.getPkPv());
        Integer opTimes = payOrderMapper.getMaxOpTimes(master.getPkPi());
        pvOp.setOpTimes(new Long(opTimes + 1));
        pvOp.setPkSchsrv(schAppt.getPkSchsrv());
        pvOp.setPkRes(schAppt.getPkSchres());
        pvOp.setPkDateslot(schAppt.getPkDateslot());
        pvOp.setPkDeptPv(pvEncounter.getPkDept());
        pvOp.setPkEmpPv(schAppt.getPkEmp());
        pvOp.setTicketno(schAppt.getTicketNo());
        pvOp.setPkSch(schAppt.getPkSch());
        pvOp.setFlagFirst("1"); // 初诊
        pvOp.setPkSchappt(schAppt.getPkSchappt());// 对应预约
        pvOp.setDtApptype(schAppt.getDtApptype());
        pvOp.setOrderidExt(schAppt.getOrderidExt());
        if (CommonUtils.isNull(schAppt.getPkSchappt())) {//挂号方式
            pvOp.setEuRegtype("0");
        } else {
            pvOp.setEuRegtype("1");
        }
        // 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
        // ( 参数-1) || '23:59:59'
        pvOp.setDateBegin(pvEncounter.getDateBegin());
        if (!"9".equals(schAppt.getEuSrvtype())) {
            pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pvEncounter.getDateBegin()));
        } else {//急诊号24小时有效
            pvOp.setDateEnd(DateUtils.strToDate(DateUtils.addDate(pvEncounter.getDateBegin(), 24, 4, "yyyyMMddHHmmss")));
        }
        DataBaseHelper.insertBean(pvOp);
        /**1.3保存PvInsurance信息*/
        PvInsurance insu = new PvInsurance();
        insu.setPkPvhp(NHISUUID.getKeyId());
        insu.setPkOrg(UserContext.getUser().getPkOrg());
        insu.setCreator(UserContext.getUser().getPkEmp());
        insu.setCreateTime(new Date());
        insu.setModifier(UserContext.getUser().getPkEmp());
        insu.setTs(new Date());
        insu.setPkPv(pvEncounter.getPkPv());
        insu.setPkHp(pvEncounter.getPkInsu());
        DataBaseHelper.insertBean(insu);

        return pvEncounter;
    }

    /**
     * 保存结算信息
     *
     * @param pvEncounter
     * @param opDts
     */
    private BlSettle saveSettle(PvEncounter pvEncounter, List<BlOpDt> opDts, OpGyAmtVo amtVo, Map<String, Object> orderMap) throws Exception {
        BigDecimal amountSt = BigDecimal.ZERO;// 结算金额
        BigDecimal amountPi = BigDecimal.ZERO;// 患者自付金额
        BigDecimal amountInsu = BigDecimal.ZERO;// 医保支付金额
        BigDecimal discAmount = BigDecimal.ZERO;// 患者优惠金额
        BigDecimal accountPrepaid = BigDecimal.ZERO;// 账户已付
        String pkDisc = null;
        if (amtVo != null && amtVo.getAmountPi().compareTo(new BigDecimal(0D)) > 0) {
            amountSt = amtVo.getAmountSt();
            amountPi = amtVo.getAmountPi();
            amountInsu = amtVo.getAmountInsu();
            discAmount = amtVo.getDiscAmount();
            accountPrepaid = amtVo.getAccountPrepaid();
            pkDisc = amtVo.getPkDisc();
        } else {
            for (BlOpDt bpt : opDts) {
                amountSt = amountSt.add(new BigDecimal(bpt.getAmount()));
                amountPi = amountPi.add(new BigDecimal(bpt.getAmountPi()));
                //医保优惠计费部分
                amountInsu = amountInsu.add(new BigDecimal(((bpt.getPriceOrg() - bpt.getPrice()) + (bpt.getPriceOrg() * (1 - bpt.getRatioSelf()))) * bpt.getQuan()));
                pkDisc = bpt.getPkDisc();// 优惠类型
                if (pkDisc != null) {
                    //患者优惠
                    discAmount = discAmount.add(new BigDecimal(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan())));
                }
                if ("1".equals(bpt.getFlagAcc())) {
                    accountPrepaid = accountPrepaid.add(new BigDecimal(bpt.getAmountPi()));
                }
            }
        }

        //第三方医保支付金额
        BlSettle bs = new BlSettle();
        bs.setPkOrg(pvEncounter.getPkOrg());
        bs.setPkPi(pvEncounter.getPkPi());
        bs.setPkPv(pvEncounter.getPkPv());
        bs.setEuPvtype(pvEncounter.getEuPvtype());
        bs.setPkInsurance(pvEncounter.getPkInsu());
        bs.setDtSttype("00");// 结算类型
        bs.setEuStresult(EnumerateParameter.ZERO);// 结算结果分类
        bs.setAmountSt(amountSt.setScale(2, BigDecimal.ROUND_HALF_UP));
        //此处患者支付还没有排除结算医保分摊的金额，同样医保支付还没有包含结算时医保分摊的金额
        bs.setAmountPi(amountPi.setScale(2, BigDecimal.ROUND_HALF_UP));
        bs.setAmountInsu(discAmount);
        bs.setDateSt(new Date());
        bs.setPkOrgSt(pvEncounter.getPkOrg());
        bs.setPkDeptSt("-1");
        bs.setPkEmpSt("-1");
        bs.setNameEmpSt("-1");
        bs.setFlagCc(EnumerateParameter.ZERO);
        bs.setFlagCanc(EnumerateParameter.ZERO);
        bs.setFlagArclare(EnumerateParameter.ZERO);
        bs.setAmountPrep(BigDecimal.ZERO);
        bs.setAmountAdd(0d);
        bs.setAmountDisc(0d);
        bs.setCodeSt(ApplicationUtils.getCode("0604"));
        ApplicationUtils.setDefaultValue(bs, true);
        DataBaseHelper.insertBean(bs);
        String pkSettle = bs.getPkSettle();
        // 将结算主键反写到记费细目表
        List<BlOpDt> blOpDtNews = new ArrayList<BlOpDt>();
        StringBuilder pkBlOpDts = new StringBuilder("");
        for (BlOpDt bpt : opDts) {
            BlOpDt bodNew = new BlOpDt();
            bodNew.setPkSettle(pkSettle);
            bodNew.setFlagSettle(EnumerateParameter.ONE);
            bodNew.setTs(new Date());
            bodNew.setPkCgop(bpt.getPkCgop());
            blOpDtNews.add(bodNew);
            pkBlOpDts = pkBlOpDts.append("'").append(bpt.getPkCgop()).append("',");
        }
        DataBaseHelper.batchUpdate("update bl_op_dt set pk_settle=:pkSettle,flag_settle=:flagSettle,ts=:ts where pk_cgop=:pkCgop ", blOpDtNews);

        //3、生成结算明细
        /*
         * 结算明细组成说明：
         * （1）记费表bl_op_dt内部医保支付金额;
         * （2）记费表bl_op_dt优惠比例金额；
         * （3）结算时第三方医保支付金额；
         * （4）结算时每次调动医保返回的报销金额；
         * （5）患者自付金额。
         */
        Map<String, Object> mapParamTemp = new HashMap<String, Object>();
        List<BlSettleDetail> blSettleDetails = new ArrayList<BlSettleDetail>();
        BdHp bdHp = null;
        // 3.1、内部主医保项目和种类支付金额
        if (amountInsu.compareTo(BigDecimal.ZERO) == 1) {
            BlSettleDetail blSettleDetail = new BlSettleDetail();
            blSettleDetail.setPkSettle(bs.getPkSettle());
            bdHp = payOrderMapper.qryHpInfo(pvEncounter.getPkInsu());
            blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
            blSettleDetail.setPkInsurance(pvEncounter.getPkInsu());// 主医保计划
            blSettleDetail.setAmount(amountInsu.doubleValue());
            ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
            blSettleDetails.add(blSettleDetail);
        }

        // 3.3、患者的优惠比例金额
        if (discAmount.compareTo(BigDecimal.ZERO) == 1) {
            BlSettleDetail blSettleDetail = new BlSettleDetail();
            blSettleDetail.setPkSettle(bs.getPkSettle());
            bdHp = payOrderMapper.qryHpInfo(pkDisc);
            blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
            blSettleDetail.setPkInsurance(pkDisc);// 优惠类型主键
            blSettleDetail.setAmount(discAmount.doubleValue());
            ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
            blSettleDetails.add(blSettleDetail);
        }
        //不计算总额分摊的情况
        //3.4、患者支付金额
        if (amountPi.compareTo(BigDecimal.ZERO) == 1) {
            BlSettleDetail blSettleDetail = new BlSettleDetail();
            blSettleDetail.setPkSettle(bs.getPkSettle());
            mapParamTemp.put("pkOrg", "~                               ");
            String sql = "select * from bd_payer where eu_type='0' and pk_org=?";
            BdPayer bdPayer = DataBaseHelper.queryForBean(sql, BdPayer.class, new Object[]{mapParamTemp.get("pkOrg")});
            if (bdPayer == null) {
                throw new BusException("未维护支付方为本人的医保计划");
            }
            blSettleDetail.setPkPayer(bdPayer.getPkPayer());// 支付方(本人)
            blSettleDetail.setPkInsurance(pvEncounter.getPkInsu());//自费部分，默认全按主医保计划显示
            blSettleDetail.setAmount(amountPi.doubleValue());// 患者自付金额
            ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
            blSettleDetails.add(blSettleDetail);
        }
        // 批量统一插入结算明细表
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), blSettleDetails);
        // 4、写结算交款记录表
        List<BlDeposit> depositListDao = new ArrayList<BlDeposit>();

        BlDeposit blDepositDao = new BlDeposit();
        ApplicationUtils.setDefaultValue(blDepositDao, true);// 设置默认字段
        blDepositDao.setPkOrg(pvEncounter.getPkOrg());
        blDepositDao.setEuDptype(EnumerateParameter.ZERO);
        blDepositDao.setEuDirect(EnumerateParameter.ONE);
        blDepositDao.setPkPi(pvEncounter.getPkPi());
        blDepositDao.setPkPv(pvEncounter.getPkPv());
        blDepositDao.setEuPvtype(pvEncounter.getEuPvtype());
        blDepositDao.setAmount(bs.getAmountPi());// 交易金额
        blDepositDao.setDtPaymode(CommonUtils.isEmptyString(CommonUtils.getString(orderMap.get("payMode"))) ? "1" : CommonUtils.getString(orderMap.get("payMode")));
        blDepositDao.setPayInfo(""); //收付款方式信息 对应支票号，银行交易号码
        blDepositDao.setDatePay(new Date());
        blDepositDao.setPkDept("-1");
        blDepositDao.setPkEmpPay("-1");
        blDepositDao.setNameEmpPay("-1");
        blDepositDao.setFlagAcc(blDepositDao.getFlagAcc() == null ? EnumerateParameter.ZERO : EnumerateParameter.ONE);
        blDepositDao.setFlagSettle(EnumerateParameter.ONE);
        blDepositDao.setPkSettle(bs.getPkSettle());
        blDepositDao.setFlagCc(EnumerateParameter.ZERO);// 操作员结账标志
        depositListDao.add(blDepositDao);

        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depositListDao);
        //写外部交易接口支付bl_ext_pay
        BlExtPay extPay = new BlExtPay();
        extPay.setPkExtpay(NHISUUID.getKeyId());
        extPay.setPkOrg(pvEncounter.getPkOrg());
        extPay.setAmount(BigDecimal.valueOf(CommonUtils.getDouble(orderMap.get("payAmount"))));
        extPay.setEuPaytype("9");
        extPay.setDtBank("");
        extPay.setNameBank("");
        extPay.setDateAp(DateUtils.strToDate(CommonUtils.getString(orderMap.get("payTime"))));
        extPay.setFlagPay("1");
        extPay.setDatePay(depositListDao.get(0).getDatePay());
        extPay.setTradeNo(CommonUtils.getString(orderMap.get("payNum")));
        extPay.setSerialNo(CommonUtils.getString(orderMap.get("orderId")));
        extPay.setDescPay(CommonUtils.getString(orderMap.get("payDesc")));
        extPay.setSysname("");
        extPay.setPkPi(pvEncounter.getPkPi());
        extPay.setPkPv(pvEncounter.getPkPv());
        extPay.setPkDepo(depositListDao.get(0).getPkDepo());
        extPay.setResultPay("");
        extPay.setRefundNo("");
        extPay.setDateRefund(null);
        extPay.setEuBill("0");
        extPay.setPkBill("");
        extPay.setDateBill(null);
        ApplicationUtils.setDefaultValue(extPay, true);
        DataBaseHelper.insertBean(extPay);

        return bs;
    }


    /**
     * 取消挂号结算
     *
     * @param pkPv
     */
    private void canlSettle(String pkPv) throws Exception {
        //查询挂号结算信息
        BlSettle stVoTemp = DataBaseHelper.queryForBean("select * from bl_settle where dt_sttype='00' and flag_canc='0' and pk_pv = ?",
                BlSettle.class, new Object[]{pkPv});
        if (stVoTemp != null) {
            /**写表bl_settle，bl_settle_detail、bl_deposit、bl_op_dt*/
            //写bl_settle反结算记录表
            BlSettle stVo = (BlSettle) stVoTemp.clone();
            stVo.setPkOrg(u.getPkOrg());
            stVo.setEuPvtype(stVoTemp.getEuPvtype());
            stVo.setDtSttype("20");
            PvEncounter pvvo = DataBaseHelper.queryForBean(
                    "select * from pv_encounter where pk_pv = ?",
                    PvEncounter.class, stVo.getPkPv());
            stVo.setPkInsurance(pvvo.getPkInsu());//主医保；
            stVo.setAmountPrep(new BigDecimal(-1).multiply(stVo.getAmountPrep()));
            stVo.setAmountSt(new BigDecimal(-1).multiply(stVo.getAmountSt()));
            stVo.setAmountPi(new BigDecimal(-1).multiply(stVo.getAmountPi()));
            stVo.setAmountInsu(new BigDecimal(-1).multiply(stVo.getAmountInsu()));
            stVo.setDateSt(new Date());
            stVo.setPkOrgSt(u.getPkOrg());
            stVo.setPkDeptSt("-1");
            stVo.setPkEmpSt("-1");
            stVo.setNameEmpSt("-1");
            stVo.setFlagCc("0");
            stVo.setFlagCanc("1");
            stVo.setPkSettleCanc(stVo.getPkSettle());
            stVo.setReasonCanc("取消结算" + stVo.getCodeSt());
            //结算编码
            stVo.setCodeSt(ApplicationUtils.getCode("0605"));
            stVo.setFlagArclare("0");
            stVo.setPkSettle(NHISUUID.getKeyId());
            DataBaseHelper.insertBean(stVo);

            //写bl_settle_detail反结算记录
            List<BlSettleDetail> stDts = DataBaseHelper.queryForList(
                    "select * from BL_SETTLE_DETAIL where PK_SETTLE = ?",
                    BlSettleDetail.class, stVoTemp.getPkSettle());
            if (stDts != null && stDts.size() > 0) {
                for (BlSettleDetail dt : stDts) {
                    dt.setPkStdt(NHISUUID.getKeyId());
                    dt.setPkSettle(stVo.getPkSettle());
                    dt.setAmount(MathUtils.mul(new Double(-1), dt.getAmount()));
                }
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), stDts);
            }
            //写bl_deposit反结算记录
            List<BlDeposit> depoList = DataBaseHelper.queryForList("select * from bl_deposit where eu_dptype='0' and pk_settle = ?",
                    BlDeposit.class, new Object[]{stVo.getPkSettle()});
            if (depoList != null && depoList.size() > 0) {
                for (BlDeposit depo : depoList) {
                    depo.setEuDirect("-1");
                    depo.setEuDptype("4");
                    depo.setAmount(depo.getAmount().multiply(new BigDecimal(-1)));
                    depo.setFlagCc("0");
                    depo.setPkCc(null);
                    depo.setPkStMid(null);
                    depo.setPkSettle(stVo.getPkSettle());
                }
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depoList);
            }

            //写bl_op_dt反结算记录
            List<BlOpDt> opDtList = DataBaseHelper.queryForList("select * from bl_op_dt where pk_settle = ?",
                    BlOpDt.class, new Object[]{stVoTemp.getPkSettle()});
            List<BlOpDt> blOpDts = Lists.newArrayList();
            if (opDtList != null && opDtList.size() > 0) {
                for (BlOpDt vo : opDtList) {
                    BlOpDt voc = new BlOpDt();
                    try {
                        voc = (BlOpDt) vo.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    voc.setQuan(vo.getQuan() * -1);
                    voc.setAmount(vo.getAmount() * -1);
                    voc.setAmountPi(vo.getAmountPi() * -1);
                    voc.setAmountHppi(vo.getAmountHppi() * -1);
                    if (vo.getAmountAdd() != null)
                        voc.setAmountAdd(vo.getAmountAdd() * -1);
                    voc.setPkSettle(stVo.getPkSettle());
                    voc.setPkCgop(NHISUUID.getKeyId());
                    voc.setDateCg(new Date());
                    voc.setCreateTime(new Date());
                    blOpDts.add(voc);
                }
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), blOpDts);
            }

        } else {
            throw new Exception("未查询到HIS挂号结算信息，请检查!");
        }

    }
    /**
     * 3.26 申请挂号退费接口
     * @param content
     * @return
     * @throws Exception
     */
    public String requestreturnPay(String content) throws Exception {

    	ReturnPayReqSubject reqData = (ReturnPayReqSubject) XmlUtil.XmlToBean(content, ReturnPayReqSubject.class);
        //SetUserUtils.setUserByEmpCode(reqData.getSender().getSenderId());

        //res
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
        if (reqData.getSubject() != null && reqData.getSubject().size() > 0) {
            paramMap.put("orderId", reqData.getSubject().get(0).getOrderId());
            paramMap.put("orderType", reqData.getSubject().get(0).getOrderType());
        }
        if(reqData.getSubject().get(0).getOrderId().length() == 0 || reqData.getSubject().get(0).getOrderType().length() == 0){

        	ReturnPayInfoResResult returnPayInfoResResult = new ReturnPayInfoResResult();
	    	returnPayInfoResResult.setId("AE");
	    	returnPayInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
	    	returnPayInfoResResult.setRequestId(reqData.getId());
	    	returnPayInfoResResult.setText("入参不能为空!");

	    	ReturnPayInfoResExd returnPayInfoResExd = new ReturnPayInfoResExd();
	    	returnPayInfoResExd.setResult(returnPayInfoResResult);
        	return XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, returnPayInfoResExd), ReturnPayInfoResExd.class);
        }
        List<Map<String, Object>> returnPayInfoList = payOrderMapper.getReturnPayInfo(paramMap);

        //subject   	
    	ReturnPayInfoResSubject returnPayInfoResSubject = new ReturnPayInfoResSubject();
    	//List<ReturnPayInfo> returnPayInfos = new ArrayList<>();

    	ReturnPayInfo returnPayInfo = new ReturnPayInfo();
    	ReturnPayInfoResResult returnPayInfoResResult = null;
		if(returnPayInfoList.size() >0){
			 Date timeDate = (Date) returnPayInfoList.get(0).get("dateAppt");
			if(returnPayInfoList.get(0).get("dateSign") != null){
				returnPayInfo.setResultDesc("该记录已签到!");
				returnPayInfo.setResultCode("1");
			}else if(returnPayInfoList.get(0).get("euStatus") != null && Integer.parseInt(returnPayInfoList.get(0).get("euStatus").toString())<9 && Integer.parseInt(returnPayInfoList.get(0).get("euStatus").toString())>0){
				returnPayInfo.setResultDesc("该记录已就诊!");
				returnPayInfo.setResultCode("1");
			}else if(returnPayInfoList.get(0).get("euStatus") !=null && returnPayInfoList.get(0).get("euStatus").equals("9")){
				returnPayInfo.setResultDesc("该记录已退诊!");
				returnPayInfo.setResultCode("1");
			}else if(returnPayInfoList.get(0).get("backAmountSt") != null){
				returnPayInfo.setResultDesc("该记录已退费!");
				returnPayInfo.setResultCode("1");
			}else if(reqData.getSubject().get(0).getReturnAmout() != null && !returnPayInfoList.get(0).get("amountSt").equals(new BigDecimal(reqData.getSubject().get(0).getReturnAmout()))){
				returnPayInfo.setResultDesc("退费金额和收费金额不符!");
				returnPayInfo.setResultCode("1");
			}else if(returnPayInfoList.get(0).get("flagCancel").equals("1")){
				returnPayInfo.setResultDesc("预约号已取消!");
				returnPayInfo.setResultCode("1");
			}else if(timeDate.compareTo(new Date()) <= 0){
				returnPayInfo.setResultDesc("就诊当日的预约号不可退!");
				returnPayInfo.setResultCode("1");
			}else if(returnPayInfoList.get(0).get("receiptNo") != null && returnPayInfoList.get(0).get("receiptNo") !=""){
				returnPayInfo.setResultDesc("挂号发票已打印，不可退费!");
				returnPayInfo.setResultCode("1");
			}else if(returnPayInfoList.get(0).get("flagSpec") != null && returnPayInfoList.get(0).get("flagSpec").equals("1")){
				returnPayInfo.setResultDesc("当前挂号医生不支持线上退号!");
				returnPayInfo.setResultCode("1");
			}else if(!returnPayInfo.getResultCode().equals("1")){
				returnPayInfo.setResultDesc("返回成功");
				returnPayInfo.setResultCode("0");
			}

			returnPayInfoResSubject.setRes(returnPayInfo);
			//result
	    	returnPayInfoResResult = new ReturnPayInfoResResult();
	    	returnPayInfoResResult.setId("AA");
	    	returnPayInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
	    	returnPayInfoResResult.setRequestId(reqData.getId());
	    	returnPayInfoResResult.setText("处理成功!");
	    	returnPayInfoResResult.setSubject(returnPayInfoResSubject);
		}else{
			returnPayInfo.setResultDesc("未查询到数据！");
			returnPayInfo.setResultCode("0");
			returnPayInfoResSubject.setRes(returnPayInfo);
			returnPayInfoResResult = new ReturnPayInfoResResult();
	    	returnPayInfoResResult.setId("AD");
	    	returnPayInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
	    	returnPayInfoResResult.setRequestId(reqData.getId());
	    	returnPayInfoResResult.setText("未查询到数据!");
	    	returnPayInfoResResult.setSubject(returnPayInfoResSubject);
		}

    	//response
    	ReturnPayInfoResExd returnPayInfoResExd = new ReturnPayInfoResExd();
    	returnPayInfoResExd.setResult(returnPayInfoResResult);

    	return  XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, returnPayInfoResExd), ReturnPayInfoResExd.class);  
    	
    }



    public String dateTrans(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String temp = null;
        if (date != null) {
            temp = sdf.format(date);
        }

        return temp;
    }
}
