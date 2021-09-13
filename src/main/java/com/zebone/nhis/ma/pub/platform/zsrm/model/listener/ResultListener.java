package com.zebone.nhis.ma.pub.platform.zsrm.model.listener;

/**
 * @author lijin
 * 结果监听回调接口
 */
public interface ResultListener {

    void success(Object object);

    void error(Object object);

    void exception(Object object);
}
