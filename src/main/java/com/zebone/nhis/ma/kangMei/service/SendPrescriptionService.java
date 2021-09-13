package com.zebone.nhis.ma.kangMei.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.pi.PiAddress;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.kangMei.dao.SendPrescriptionMapper;
import com.zebone.nhis.ma.kangMei.send.SendPrescription;
import com.zebone.nhis.ma.kangMei.vo.*;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.util.CollectionUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SendPrescriptionService {
    @Autowired
    SendPrescription sendPrescription;
    @Autowired
    SendPrescriptionMapper sendPrescriptionMapper;
    @Autowired
    private IpCgPubService ipCgPubService;

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private Logger logger = LoggerFactory.getLogger("ma.syxInterface");

    /**
     * 保存订单  发药订单
     *
     * @param msgParam
     * @return
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public String sendPresData(Map<String, Object> msgParam) throws Exception {
        logger.info("调用了康美发药接口！");
        String key = String.format("%d", System.currentTimeMillis());
        String psw = null;
        String companyNum = null;
        if ("001".equals(msgParam.get("codeArea"))) {//北院区
            psw = ApplicationUtils.getPropertyValue("KangMei.psw.NorthDistrict", "0");
            //康美分配给医院或机构的编号
            companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.NorthDistrict", "0");
        } else if ("002".equals(msgParam.get("codeArea"))) {//南院区
            psw = ApplicationUtils.getPropertyValue("KangMei.psw.SouthDistrict", "0");
            //康美分配给医院或机构的编号
            companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.SouthDistrict", "0");
        } else if ("202".equals(msgParam.get("codeArea"))) {//博爱医院
            psw = ApplicationUtils.getPropertyValue("KangMei.psw.OurHospital", "0");
            //康美分配给医院或机构的编号
            companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.OurHospital", "0");
        }
        //认证签名  Md5(接口名+时间+psw)
        String sign = "saveOrderInfo" + key + psw;
        //一次性查出所有的数据
        List<Map<String, Object>> qrySendDispMap = new ArrayList<>(16);
        qrySendDispMap = sendPrescriptionMapper.qrySendDispMapSql(msgParam.get("presNo").toString());
        if (CollectionUtils.isEmpty(qrySendDispMap)) {
            logger.info("未查出需要发送的数据！处方号：" + msgParam.get("presNo").toString());
            throw new BusException("未查出需要发送的数据！处方号：" + msgParam.get("presNo").toString());
        }
        DataReq dataReq = ApplicationUtils.mapToBean(qrySendDispMap.get(0), DataReq.class);
        dataReq.setOrderTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
        PdetailReq pdetailReq = ApplicationUtils.mapToBean(qrySendDispMap.get(0), PdetailReq.class); //= sendPrescriptionMapper.qrySendDispPdetail(msgParam.get("presNo").toString()); // <pdetail>
        pdetailReq.setCheckArea("1");
        /*
         * 1.处方性质是出院带药，默认送个人，其他送医院，不做选择
         * 2.煎药方式：2代煎且送，3.只配置
         * 3.煎法：6为膏方
         * 通过以上，2.3条满足以下要求
         * 三方说明：
         * 代送不代煎(中药饮片)：amount为实际数量；is_suffering=0；suffering_num=0；ji_fried=0；dose传单剂的数量；
         *   代送且代煎(中药代煎)：amount为实际数量；is_suffering=1；suffering_num=amount；ji_fried=1；dose传单剂的数量；
         *   膏方：               amount=1；is_suffering=0；suffering_num=0；ji_fried=0；同时药材节点里面的dose为总数(如3剂10g甘草，那么dose应该传30g)；
         *   丸剂：                amount=1；is_suffering=0；suffering_num=0；ji_fried=0；同时药材节点里面的dose为总数(如3剂10g甘草，那么dose应该传30g)；
         *   amount：       剂数   处方实际剂数，当处方为西药、膏方或丸剂时传1；散剂、免煎颗粒按照实际剂数传输即可；
         *  is_suffering  是否煎煮  取值范围：0 否，1 是；膏方、丸剂、散剂、免煎颗粒统一非煎煮，传0
         *  suffering_num   此处方代煎数量    当is_suffering=1时，suffering_num=amount*ji_fried；其他都传0。
         *     ji_fried        几煎              不代煎传0，一煎传1，二煎传2
         */
        if ("09".equals(dataReq.getDtProperties())) {
            String sql = "select  NAME_PROV,NAME_CITY,NAME_DIST,NAME_STREET,ADDR,NAME_REL ,TEL " +
                    "from PI_ADDRESS PA where PK_PI=? and FLAG_USE='1'";
            PiAddress piAddress = DataBaseHelper.queryForBean(sql, PiAddress.class, dataReq.getPkPi());
            dataReq.setAddrStr(piAddress.getNameProv() + "," + piAddress.getNameCity() + "," + piAddress.getNameDist() + "," + piAddress.getAddr());
            dataReq.setIsHosAddr("2");
            dataReq.setConsignee(piAddress.getNameRel());
            dataReq.setConTel(piAddress.getTel());
        }
        //处理年龄
        //1岁以上
        boolean conS = StringUtils.contains(pdetailReq.getAge(), "岁");
        //岁以下
        boolean conY = !conS && StringUtils.endsWith(pdetailReq.getAge(), "月");
        if (conS) {
            pdetailReq.setAge(StringUtils.substringBefore(pdetailReq.getAge(), "岁"));
        }
        if (conY) {
            pdetailReq.setAge("0");
            pdetailReq.setPrescriptRemark(pdetailReq.getPrescriptRemark() + "患者年龄" + pdetailReq.getAge());
        }
        List<XqReq> xqreq = Lists.newArrayList();
        XqReq xqR = null;
        //代煎代送
        boolean friedDelivered = "2".equals(pdetailReq.getIsSuffering());
        // 膏方
        boolean gaoFang = "03".equals(pdetailReq.getType());
        //代送不待煎（不含膏方）
        boolean friedNoDelivered = "3".equals(pdetailReq.getIsSuffering()) && !gaoFang;
        if (friedDelivered) {
            pdetailReq.setIsSuffering("1");
            pdetailReq.setType("0");
            for (Map<String, Object> temp : qrySendDispMap) {
                xqR = ApplicationUtils.mapToBean(temp, XqReq.class);
                xqR.setDose(subZeroAndDot(xqR.getDose()));
                xqR.setHerbUnitPrice(subZeroAndDot(xqR.getHerbUnitPrice()));
                xqreq.add(xqR);
            }
        } else if (gaoFang) {
            pdetailReq.setIsSuffering("0");
            pdetailReq.setType("2");
            pdetailReq.setSufferingNum("0");
            pdetailReq.setJiFried("0");
            for (Map<String, Object> temp : qrySendDispMap) {
                xqR = ApplicationUtils.mapToBean(temp, XqReq.class);
                String does = String.valueOf(Double.valueOf(pdetailReq.getAmount()) * Double.valueOf(xqR.getDose()));
                xqR.setDose(subZeroAndDot(does));
                xqR.setHerbUnitPrice(subZeroAndDot(xqR.getHerbUnitPrice()));
                xqreq.add(xqR);
            }
            pdetailReq.setAmount("1");
        } else if (friedNoDelivered) {
            pdetailReq.setIsSuffering("0");
            pdetailReq.setType("0");
            pdetailReq.setSufferingNum("0");
            pdetailReq.setJiFried("0");
            for (Map<String, Object> temp : qrySendDispMap) {
                xqR = ApplicationUtils.mapToBean(temp, XqReq.class);
                xqR.setDose(subZeroAndDot(xqR.getDose()));
                xqR.setHerbUnitPrice(subZeroAndDot(xqR.getHerbUnitPrice()));
                xqreq.add(xqR);
            }
        }
        pdetailReq.setXqReq(xqreq);
        List<PdetailReq> pde = new ArrayList<PdetailReq>();
        pde.add(pdetailReq);
        dataReq.setPdetailReq(pde);
        //<head>
        HeadReq headReq = new HeadReq();
        headReq.setKey(key);   //set KEY
        headReq.setSign(encodeByMD5(sign)); //set 签名
        headReq.setCompanyNum(companyNum);
        //<orderInfo>
        OrderInfoReq orderInfoReq = new OrderInfoReq();
        orderInfoReq.setDataReq(dataReq);
        orderInfoReq.setHeadReq(headReq);
        String xml = XmlUtil.beanToXml(orderInfoReq, OrderInfoReq.class);//对象转XML
        logger.info("发送至康美的XML：\n" + xml);
        String responseBase64 = null; //响应的加密字符串
        try {
            //加密
            String strBase64 = Base64.encodeToString(xml.getBytes("utf-8"));
            //发送数据
            logger.info("发送数据至康美！");
            String sendHospDispResponse = sendPrescription.sendHospDisp(getXML(strBase64, "saveOrderInfoToCharacterSet"));
            responseBase64 = StringUtils.substringBetween(sendHospDispResponse, "<return>", "</return>");
            logger.info("康美返回的未解密的数据：" + responseBase64);
        } catch (IOException e) {
            logger.info("康美发药接口错误信息：" + e.getMessage());
            e.printStackTrace();
        }
        // 解密
        String strResponse = Base64.decodeToString(responseBase64);
        logger.info("康美发药接口保存订单返回的结果：" + strResponse);
        //获取返回状态码
        String resultCode = StringUtils.substringBetween(strResponse, "<resultCode>", "</resultCode>");
        //保存返回数据
        if ("0".equals(resultCode)) {
            ExPdExtRet exPdExtRet = new ExPdExtRet();
            exPdExtRet.setPkPv(dataReq.getPkPv());
            exPdExtRet.setRegNum(StringUtils.substringBetween(strResponse, "<reg_num>", "</reg_num>"));
            exPdExtRet.setOrderid(StringUtils.substringBetween(strResponse, "<orderid>", "</orderid>"));
            exPdExtRet.setPresNo(msgParam.get("presNo").toString());
            exPdExtRet.setOrdsn(dataReq.getOrdsn());
            exPdExtRet.setDescInfo(StringUtils.substringBetween(strResponse, "<description>", "</description>"));
            exPdExtRet.setEuStatus(resultCode);
            DataBaseHelper.insertBean(exPdExtRet);
            //更新 ex_pd_apply 医疗执行-物品请领（退）单      ex_pd_apply_detail 医疗执行-物品请领(退)明细
            //写ex_pd_de 医疗执行_部门_物品发退
        }
        return strResponse;
    }

    /**
     * 门诊草药处方发送康美接口
     *
     * @param msgParam
     * @return
     * @throws Exception
     */
    public String sendOpPresData(Map<String, Object> msgParam) throws Exception {
        logger.info("调用了康美发药接口！");
        String key = String.format("%d", System.currentTimeMillis());
        String psw = null;
        String companyNum = null;
        if ("001".equals(msgParam.get("codeArea"))) {//北院区
            psw = ApplicationUtils.getPropertyValue("KangMei.psw.NorthDistrict", "0");
            //康美分配给医院或机构的编号
            companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.NorthDistrict", "0");
        } else if ("002".equals(msgParam.get("codeArea"))) {//南院区
            psw = ApplicationUtils.getPropertyValue("KangMei.psw.SouthDistrict", "0");
            //康美分配给医院或机构的编号
            companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.SouthDistrict", "0");
        }else if ("202".equals(msgParam.get("codeArea"))) {//博爱医院本院区
            psw = ApplicationUtils.getPropertyValue("KangMei.psw.OurHospital", "0");
            //康美分配给医院或机构的编号
            companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.OurHospital", "0");
        }
        //认证签名  Md5(接口名+时间+psw)
        String sign = "saveOrderInfo" + key + psw;
        // 得到XML对象 <data>
        List<Map<String, Object>> qrySendDispMap = sendPrescriptionMapper.qryOpPresInfo(msgParam);
        DataReq dataReq = ApplicationUtils.mapToBean(qrySendDispMap.get(0), DataReq.class);
        PdetailReq pdetailReq = ApplicationUtils.mapToBean(qrySendDispMap.get(0), PdetailReq.class); //= sendPrescriptionMapper.qrySendDispPdetail(msgParam.get("presNo").toString()); // <pdetail>
        List<XqReq> xqreq = Lists.newArrayList(); //sendPrescriptionMapper.qrySendDispXq(msgParam.get("presNo").toString()); // <xq>
        for (Map<String, Object> temp : qrySendDispMap) {
            xqreq.add((XqReq) ApplicationUtils.mapToBean(temp, XqReq.class));
        }
        pdetailReq.setXqReq(xqreq);
        List<PdetailReq> pde = new ArrayList<PdetailReq>();
        pde.add(pdetailReq);
        dataReq.setPdetailReq(pde);
        //设置婴儿年龄
        if (StringUtils.endsWith(dataReq.getAgePv(), "月")) {
            String ageByBirthday = DateUtils.getAgeByBirthday(dataReq.getBirthDate(), new Date());
            pdetailReq.setAge("0");
            pdetailReq.setPrescriptRemark(ageByBirthday);
        }
        Map<String, Object> addrMap = qrySendDispMap.get(0);
        dataReq.setAddrStr(addrMap.get("nameProv") + "," + addrMap.get("nameCity") + "," + addrMap.get("nameDist") + "," + addrMap.get("addr"));
        if (msgParam.get("sendDateTime") != null) {//设置发送时间
            dataReq.setSendGoodsTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.strToDate(msgParam.get("sendDateTime").toString())));
        }
        //<head>
        HeadReq headReq = new HeadReq();
        headReq.setKey(key);   //set KEY
        headReq.setSign(encodeByMD5(sign)); //set 签名
        headReq.setCompanyNum(companyNum);
        //<orderInfo>
        OrderInfoReq orderInfoReq = new OrderInfoReq();
        orderInfoReq.setDataReq(dataReq);
        orderInfoReq.setHeadReq(headReq);
        String xml = XmlUtil.beanToXml(orderInfoReq, OrderInfoReq.class);//对象转XML
        logger.info("发送至康美的XML：\n" + xml);
        String responseBase64 = null; //响应的加密字符串
        try {
            //加密
            String strBase64 = Base64.encodeToString(xml.getBytes("utf-8"));
            //发送数据
            logger.info("发送数据至康美！");
            String sendHospDispResponse = sendPrescription.sendHospDisp(getXML(strBase64, "saveOrderInfoToCharacterSet"));
            responseBase64 = StringUtils.substringBetween(sendHospDispResponse, "<return>", "</return>");
            logger.info("康美返回的未解密的数据：" + responseBase64);
        } catch (IOException e) {
            logger.info("康美发药接口错误信息：" + e.getMessage());
            e.printStackTrace();
        }
        // 解密
        String strResponse = Base64.decodeToString(responseBase64);
        logger.info("康美发药接口保存订单返回的结果：" + strResponse);
        //获取返回状态码
        String resultCode = StringUtils.substringBetween(strResponse, "<resultCode>", "</resultCode>");
        //保存返回数据
        if ("0".equals(resultCode)) {
            ExPdExtRet exPdExtRet = new ExPdExtRet();
            exPdExtRet.setPkPv(msgParam.get("pkPv").toString());
            exPdExtRet.setRegNum(StringUtils.substringBetween(strResponse, "<reg_num>", "</reg_num>"));
            exPdExtRet.setOrderid(StringUtils.substringBetween(strResponse, "<orderid>", "</orderid>"));
            exPdExtRet.setPresNo(msgParam.get("presNo").toString());
            exPdExtRet.setOrdsn(dataReq.getOrdsn());
            exPdExtRet.setDescInfo(StringUtils.substringBetween(strResponse, "<description>", "</description>"));
            exPdExtRet.setEuStatus(resultCode);
            DataBaseHelper.insertBean(exPdExtRet);
        }
        return strResponse;
    }

    /**
     * 取消发药订单
     * 008004002004
     *
     * @param param
     * @return
     */
    @SuppressWarnings("static-access")
    public String sendCancelPresData(String param, IUser user) {
        List<Map<String, Object>> msgParam = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        logger.info("调用了康美退药接口！");
        String key = String.format("%d", System.currentTimeMillis());
        String psw = null;
        String companyNum = null;
        //查询该科室所属院区
        Map<String, Object> codeArea = DataBaseHelper.queryForMap("select code_area from bd_ou_org_area where pk_orgarea in (select pk_orgarea from bd_ou_dept where pk_dept=?)", new Object[]{UserContext.getUser().getPkDept()});
        if (codeArea == null) return null;
        if ("001".equals(codeArea.get("codeArea").toString())) {//北院区
            psw = ApplicationUtils.getPropertyValue("KangMei.psw.NorthDistrict", "0");
            //康美分配给医院或机构的编号
            companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.NorthDistrict", "0");
        } else if ("002".equals(codeArea.get("codeArea").toString())) {//南院区
            psw = ApplicationUtils.getPropertyValue("KangMei.psw.SouthDistrict", "0");
            //康美分配给医院或机构的编号
            companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.SouthDistrict", "0");
        } else if ("202".equals(codeArea.get("codeArea").toString())) {//博爱医院本院区
            psw = ApplicationUtils.getPropertyValue("KangMei.psw.OurHospital", "0");
            //康美分配给医院或机构的编号
            companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.OurHospital", "0");
        }
        //认证签名  Md5(接口名+时间+psw)
        String sign = "saveCancelOrder" + key + psw;
        String pkItem = null;
        String itemCode = ApplicationUtils.getSysparam("BL0032", false);
        if (!StringUtils.isBlank(itemCode)) {
            String sql = "select pk_item,price from bd_item where code=?";
            Map<String, Object> boilData = DataBaseHelper.queryForMap(sql, itemCode);
            if (boilData == null) {
                pkItem = boilData.get("pkItem").toString();
            }
        }
        for (Map<String, Object> map : msgParam) {
            sendCancel(map, companyNum, key, sign);
            refundDecoctingFee(map, (User) user, pkItem);
        }
        return null;
    }

    /**
     * @return void
     * @Description 调用三方接口退订单
     * @auther wuqiang
     * @Date 2020-11-06
     * @Param [msgParam, companyNum, key, sign]
     */
    private void sendCancel(Map<String, Object> msgParam, String companyNum, String key, String sign) {
        //组装xml对象
        List<DataReq> cancelOrder = sendPrescriptionMapper.cancelOrder(msgParam.get("presNo").toString(), msgParam.get("pkPv").toString());
        if (cancelOrder == null || cancelOrder.size() <= 0) {
            throw new BusException("未查到该发药单发送成功的记录！");
        }
        DataReq dataReq = sendPrescriptionMapper.cancelOrder(msgParam.get("presNo").toString(), msgParam.get("pkPv").toString()).get(0);
        dataReq.setOperName(UserContext.getUser().getNameEmp());
        dataReq.setReason(msgParam.get("reason").toString());
        HeadReq headReq = new HeadReq();
        headReq.setCompanyNum(companyNum);
        headReq.setKey(key);
        headReq.setSign(encodeByMD5(sign));
        OrderInfoReq infoReq = new OrderInfoReq();
        infoReq.setDataReq(dataReq);
        infoReq.setHeadReq(headReq);
        String xml = XmlUtil.beanToXml(infoReq, OrderInfoReq.class);
//		System.out.println(xml);
        String responseBase64 = null; //响应字符串加密
        try {
            //加密
            String strBase64 = Base64.encodeToString(xml.getBytes("utf-8"));
            //发送数据
            String cancelOrderResponse = sendPrescription.sendHospDisp(getXML(strBase64, "saveCancelOrder"));
            responseBase64 = StringUtils.substringBetween(cancelOrderResponse, "<return>", "</return>");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("康美发药接口错误信息：" + e.getMessage());
        }
        //解密 并返回
        String strResponse = Base64.decodeToString(responseBase64);
        logger.info("康美发药接口取消订单返回的结果：" + strResponse);
        if ("23".equals(StringUtils.substringBetween(strResponse, "<resultCode>", "</resultCode>"))) {
            throw new BusException("取消订单失败，该处方已经调配！！！");
        }
        StringBuffer upSql = new StringBuffer("update ex_pd_ext_ret set eu_status='9'");
        upSql.append(" where pk_pv=:pkPv and pres_no=:presNo");
        DataBaseHelper.execute(upSql.toString(), msgParam);
    }

    /**
     * @return void
     * @Description 三方煎药费退费
     * @auther wuqiang
     * @Date 2020-11-06
     * @Param []
     */
    private void refundDecoctingFee(Map<String, Object> msgParam, User user, String pkitem) {
        if (StringUtils.isBlank(pkitem)) {
            return;
        }
        List<BlIpDt> vos = sendPrescriptionMapper.qryBlipDtByPkPres(msgParam.get("pkPres").toString(), msgParam.get("pkPv").toString(), pkitem);
        if (vos == null || vos.size() <= 0) {
            return;
        }
        RefundVo voOrg = new RefundVo();
        voOrg.setNameEmp(user.getNameEmp());
        voOrg.setPkDept(user.getPkDept());
        voOrg.setPkEmp(user.getPkEmp());
        voOrg.setPkOrg(user.getPkOrg());
        List<RefundVo> refunds = new ArrayList<RefundVo>();
        for (BlIpDt vo : vos) {
            RefundVo reVo = (RefundVo) voOrg.clone();
            reVo.setPkCgip(vo.getPkCgip());
            reVo.setQuanRe(vo.getQuan());
            refunds.add(reVo);
        }
        BlPubReturnVo refundRes = ipCgPubService.refundInBatch(refunds);
    }

    /**
     * 对字符串进行MD5编码
     */
    private String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                // 创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                // 将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 轮换字节数组为十六进制字符串
     *
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 拼接请求的xml串
     *
     * @param strBase64 加密的请求数据
     * @param method    请求方法
     * @return
     */
    public static String getXML(String strBase64, String method) {
        String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> "
                + "<soap:Body>"
                + "<ns1:" + method + " xmlns:ns1=\"http://factory.service.cxf.kangmei.com/\">"
                + "<request>"
                + strBase64
                + "</request>";
        if ("saveOrderInfoToCharacterSet".equals(method)) {
            soapXML = soapXML + "<characterSet>utf-8</characterSet>";
        }
        soapXML = soapXML + "</ns1:" + method + ">"
                + "</soap:Body>"
                + "</soap:Envelope>";
        return soapXML;
    }

    /**
     * @return java.lang.String
     * @Description 草药代煎重发
     * @auther wuqiang
     * @Date 2020-10-21
     * @Param [param, user]
     */
    public String sendPlatMsgRepeat(String param, IUser user) throws Exception {
        List<Map<String, Object>> msgParam = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        StringBuilder stringBuilder = new StringBuilder();
        for (Map<String, Object> map : msgParam) {
            stringBuilder.append(sendPresData(map));
        }
        return stringBuilder.toString();
    }

    /**
     * 住院处方发药：查询发送三方数据
     *
     * @param param{"dateBegin":"开始时间","dateEnd":"结束时间","pkDeptDe":"发药药房科室","pkDeptAp":"申请科室","codeIp":"住院号"}
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPresListData(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) {
            return null;
        }
        paramMap.put("pkDeptDe", ((User) user).getPkDept());
        List<Map<String, Object>> resData = sendPrescriptionMapper.qryPresListData(paramMap);
        return resData;
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    private String subZeroAndDot(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
