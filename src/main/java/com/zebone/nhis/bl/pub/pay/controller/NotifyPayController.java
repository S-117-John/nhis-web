package com.zebone.nhis.bl.pub.pay.controller;

import com.zebone.nhis.bl.pub.pay.service.NotifyPayService;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.pay.support.weixinPay.Dom4jUtils;
import com.zebone.nhis.common.pay.support.weixinPay.WeixinConfig;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/static/clinicPay")
public class NotifyPayController {

    @Autowired
    private NotifyPayService notifyPayService;

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
            String out_trade_no = request.getParameter("out_trade_no");  // "alipay" + 临时表结算主键
            String serialNo = out_trade_no.substring(6);
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
//                    final BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where pk_bus = ?",
//                            BlExtPay.class, new Object[] { serialNo });
//                    if (blExtPay != null) {
//                    	// 金额验证
//                        if (!total_fee.equals(blExtPay.getAmount() + "")) {
//                            response.getWriter().write("failure");
//                            return;
//                        }
//                        // 避免重复处理
//                        if (!"1".equals(blExtPay.getFlagPay())) {
//                            blExtPay.setFlagPay("1");
//                            blExtPay.setSerialNo(serialNo);
//                            blExtPay.setDatePay(new Date());
//                            blExtPay.setEuPaytype(EnumerateParameter.EIGHT);
//                            new Thread()   //回调线程
//                            {
//                                public void run() {
//                                	notifyPayService.clinicNotifyBusiness(blExtPay);
//                                }
//                            }.start();
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
            e.printStackTrace();
            try {
                response.getWriter().write("failure");
            } catch (IOException e1) {
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
                String serialNo = outTradeNo.substring(6);
                double totalFee = Double.parseDouble((String) map.get("total_fee")) / 100;

                String resultCode = (String) map.get("result_code");
                final BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where pk_bus = ?",
                        BlExtPay.class, new Object[] { serialNo });
                if (blExtPay != null) {
                    if (totalFee != blExtPay.getAmount().doubleValue()) {
                        returnCode = "FAIL";
                        returnMsg = "金额不符";
                    }
                    if (!"1".equals(blExtPay.getFlagPay())) {// 避免重复处理
                        if ("FAIL".equals(resultCode)) {
                            blExtPay.setFlagPay("9");
                        } else {
                            blExtPay.setFlagPay("1");
                            blExtPay.setEuPaytype(EnumerateParameter.SEVEN);
                            blExtPay.setOutTradeNo(outTradeNo);
                            new Thread()   //回调线程
                            {
                            	public void run() {
                            		notifyPayService.clinicNotifyBusiness(blExtPay);
                            	}
                            }.start();
                        }
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
            e.printStackTrace();
            returnCode = "FAIL";
            returnMsg = "系统异常";
        }

        String returnStr = String.format(
                "<xml><return_code><![CDATA[%s]]></return_code><return_msg><![CDATA[%s]]></return_msg></xml>",
                returnCode, returnMsg);
        response.getWriter().write(returnStr);
    }
    
    public static void main(String[] args) {
		String string = "alipay324234";
	    System.out.println(string.substring(6));
	}

}
