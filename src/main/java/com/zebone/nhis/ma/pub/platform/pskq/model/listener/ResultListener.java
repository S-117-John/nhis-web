package com.zebone.nhis.ma.pub.platform.pskq.model.listener;

/**
 * @author lijin
 * 结果监听回调接口
 */
public interface ResultListener {

    void success(String requestJson, String responseJson);

    void error(String message,String requestJson,String responseJson);

    void exception(String message,String requestJson,String responseJson);



}
