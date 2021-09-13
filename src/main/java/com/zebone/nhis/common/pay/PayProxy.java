package com.zebone.nhis.common.pay;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.payment.mch.MchPayRequest;
import com.foxinmy.weixin4j.payment.mch.RefundResult;
import com.foxinmy.weixin4j.type.CurrencyType;
import com.foxinmy.weixin4j.type.IdQuery;
import com.foxinmy.weixin4j.type.IdType;
import com.zebone.nhis.common.pay.support.weixinPay.WeixinConfig;
import com.zebone.nhis.common.support.ApplicationUtils;

public class PayProxy {

    public static String notifyDomain;

    static {
        notifyDomain = ApplicationUtils.getPropertyValue("pay.notifyDomain", "");
    }

    public static String getAlipayQrcode(String orderNo, String subject, String body, String totalFee,
            String notifyUrl) {

        return null;
    }

    /**
     * 
     * @param tradeNo
     *            支付宝订单号
     * @param refundAmount
     *            需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     * @param refundReason
     *            退款的原因说明
     * @param outRequestNo
     *            标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
     */
    public static String alipayRefund(String tradeNo, double refundAmount, String refundReason, String outRequestNo) {
        return "fail";
    }

    public static String getWeixinPayQrcode(String productId, String body, String outTradeNo, double totalFee,
            String notifyUrl, String createIp, String attach) {
        try {
            MchPayRequest payRequest = WeixinConfig.wxPayClient().createNativePayRequest(productId, body, outTradeNo,
                    totalFee, notifyUrl, createIp, attach);
            return payRequest.toRequestString();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static String weixinPayRefund(String transactionId, String outRefundNo, double totalFee, double refundFee) {
        IdQuery idQuery = new IdQuery(transactionId, IdType.TRANSACTIONID);
        try {
            RefundResult refundResult = WeixinConfig.wxPayClient().applyRefund(idQuery, outRefundNo, totalFee,
                    refundFee, CurrencyType.CNY, WeixinConfig.getMchId(), null, null);
            return refundResult.toString();
        } catch (WeixinException e) {
            e.printStackTrace();
        }
        return "fail";
    }

}
