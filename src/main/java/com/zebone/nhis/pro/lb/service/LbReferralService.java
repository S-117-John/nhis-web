package com.zebone.nhis.pro.lb.service;

import com.zebone.nhis.ma.pub.lb.service.LbXFReferralHandler;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class LbReferralService {

    @Resource
    private LbXFReferralHandler lbXFReferralHandler;
    /**
     *门诊转诊
     * @param param
     * @param user
     */
    public Map<String, Object> visitReferral(String param, IUser user){
        Map<String,Object> resp = (Map<String,Object>) ExtSystemProcessUtils.processExtMethod("XFReferral"
                , "visitReferral", new Object[]{param});
        return resp;
    }

    /**
     *出院转诊
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> leaveReferral(String param, IUser user){
        String pkPv = MapUtils.getString(JsonUtil.readValue(param,Map.class), "pkPv");
        User u =(User)user;
        Map<String,Object> resp = (Map<String,Object>) ExtSystemProcessUtils.processExtMethod("XFReferral"
                , "leaveReferral", new Object[]{pkPv,u});
        return resp;
    }
}
