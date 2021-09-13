package com.zebone.nhis.common.pay.controller;

import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.pay.service.PayService;
import com.zebone.nhis.common.pay.support.weixinPay.Dom4jUtils;
import com.zebone.nhis.common.pay.support.weixinPay.WeixinConfig;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping("/alipay/notify")
    public void alipayNotify(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        // 获取参数
        try {
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                // 乱码解决，这段代码在出现乱码时使用，如果mysign和sign不相等也可以使用这段代码转化
                // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                params.put(name, valueStr);
            }

            String trade_status = request.getParameter("trade_status");
            String out_trade_no = request.getParameter("out_trade_no");
            String trade_no = request.getParameter("trade_no");
            String total_fee = request.getParameter("total_amount");
//            if (AlipaySignature.rsaCheckV1(params, AlipayConfig.getApipayPublicKey(), AlipayConfig.getCharset(),
//                    AlipayConfig.getSignType())) {// 验证成功
//                if (trade_status.equals("WAIT_BUYER_PAY")) {// 交易创建
//                    response.getWriter().write("success");
//                    return;
//                } else if (trade_status.equals("TRADE_FINISHED")) {// 交易付款完成
//                    response.getWriter().write("success");
//                    return;
//                } else if (trade_status.equals("TRADE_SUCCESS")) {// 交易结束
//                    // to do
//                    BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where serial_num = ?",
//                            BlExtPay.class, new Object[] { out_trade_no });
//                    if (blExtPay != null) {
//                        if (!total_fee.equals(blExtPay.getAmount() + "")) {
//                            response.getWriter().write("failure");
//                            return;
//                        }
//                        if (!"1".equals(blExtPay.getFlagPay())) {// 避免重复处理
//                            blExtPay.setFlagPay("1");
//                            blExtPay.setDatePay(new Date());
//                            //blExtPay.setPayResult(JSON.toJSONString(params));
//                            //blExtPay.setOutTradeNo(trade_no);
//                            payService.updatePay(blExtPay);
//                        }
//                        response.getWriter().write("success");
//                        return;
//                    } else {
//                        response.getWriter().write("failure");
//                        return;
//                    }
//                } else if (trade_status.equals("TRADE_CLOSED")) {
//                    response.getWriter().write("success");
//                    return;
//                }
//            } else {// 验证失败
//                response.getWriter().write("failure");
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                response.getWriter().write("failure");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    @RequestMapping("/weixinPay/notify")
    public void weixinPayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/xml; charset=utf-8");
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String s = "";
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        br.close();
        String xml = sb.toString();
        String returnCode = "SUCCESS";
        String returnMsg = "OK";
        try {
            Document document = DocumentHelper.parseText(xml);
            Map<String, Object> map = Dom4jUtils.Dom2Map(document);
            String sign = (String) map.get("sign");
            map.remove("sign");
            String reSign = WeixinConfig.getWeixinSignature().sign(map);
            if (sign.equals(reSign)) {// 签名校验通过
                String outTradeNo = (String) map.get("out_trade_no");
                String transactionId = (String) map.get("transaction_id");
                BigDecimal totalFee = new BigDecimal((String) map.get("total_fee")).divide(new BigDecimal(100));

                String resultCode = (String) map.get("result_code");
                BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where serial_num = ?",
                        BlExtPay.class, new Object[] { outTradeNo });
                if (blExtPay != null) {
                    if (totalFee != blExtPay.getAmount()) {
                        returnCode = "FAIL";
                        returnMsg = "金额不符";
                    }
                    if (!"1".equals(blExtPay.getFlagPay())) {// 避免重复处理
                        if ("FAIL".equals(resultCode)) {
                            blExtPay.setFlagPay("9");
                        } else {
                            blExtPay.setFlagPay("1");
                        }
                        blExtPay.setDatePay(new Date());
                        //blExtPay.setPayResult(xml);
                        //blExtPay.setOutTradeNo(transactionId);
                        payService.updatePay(blExtPay);
                    }
                } else {
                    returnCode = "FAIL";
                    returnMsg = "订单不存在";
                }
            } else {
                returnCode = "FAIL";
                returnMsg = "签名错误";
            }

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            returnCode = "FAIL";
            returnMsg = "系统异常";
        }

        String returnStr = String.format(
                "<xml><return_code><![CDATA[%s]]></return_code><return_msg><![CDATA[%s]]></return_msg></xml>",
                returnCode, returnMsg);
        response.getWriter().write(returnStr);
    }

}
