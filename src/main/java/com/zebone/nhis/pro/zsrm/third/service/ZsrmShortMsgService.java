package com.zebone.nhis.pro.zsrm.third.service;

import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/***
 * 手机短信认证服务
 * 中山人民医院单独使用
 */
@Service
public class ZsrmShortMsgService {

    /**
     * 发送短信认证接口
     * @param param
     * @param user
     */
    public void sendCheckPhone(String param, IUser user){
        Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
        PlatFormSendUtils.sendPhoneCheck(paramMap);

    }
}
