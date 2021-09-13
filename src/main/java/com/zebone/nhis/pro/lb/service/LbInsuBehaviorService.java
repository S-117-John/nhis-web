package com.zebone.nhis.pro.lb.service;


import com.zebone.nhis.ma.pub.lb.service.NHISLBGXInsurHandler;
import com.zebone.nhis.ma.pub.lb.vo.archInfoVo.PiArchInfoVo;
import com.zebone.nhis.ma.pub.lb.vo.archInfoVo.QryVo;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LbInsuBehaviorService {

    @Resource
    private NHISLBGXInsurHandler nhislbgxInsurHandler;
    /**
     * 病案信息上传审核
     * @param param
     * @param user
     */
    public Map<String, Object> uploadArchInfo(String param, IUser user){
        String pkpv = MapUtils.getString(JsonUtil.readValue(param,Map.class), "pkpv");
        Map<String,Object> resp = (Map<String,Object>)ExtSystemProcessUtils.processExtMethod("ArchInfo", "oneUpload", new Object[]{pkpv});
        return resp;
    }
    /**
     * 病案信息作废
     * @param param
     * @param user
     */
    public Map<String, Object> invalid(String param, IUser user){
        String pkpv = MapUtils.getString(JsonUtil.readValue(param,Map.class), "pkpv");
        Map<String,Object> resp = (Map<String,Object>)ExtSystemProcessUtils.processExtMethod("ArchInfo", "invalid", new Object[]{pkpv});
        return resp;
    }
    /**
     * 查询患者信息
     * @param param
     * @param user
     * @return
     */
    public List<PiArchInfoVo> qryPiArchInfo(String param, IUser user){
        QryVo qryVo = JsonUtil.readValue(param,QryVo.class);
        List<PiArchInfoVo> piArchInfoVoList = (List<PiArchInfoVo>) ExtSystemProcessUtils.processExtMethod("ArchInfo"
                , "qryPiArchInfo", new Object[]{qryVo});
        return piArchInfoVoList;
    }
}
