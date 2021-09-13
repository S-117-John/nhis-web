package com.zebone.nhis.pro.sd.bl.service;

import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 商保后台服务
 */
@Service
public class SdTkInsuService {

    /**
     * 交易号：022004005001
     * 商保客户身份校验
     *
     * @param param 入参
     * @param user  用户
     * @return 结果
     */
    public Map<String, Object> patiIdCheck(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> rtnMap = new HashMap<>(16);

        //调用开商保客户身份校验
        rtnMap = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS200", new Object[]{paramMap, user});

        return rtnMap;
    }

    /**
     * 交易号：022004005002
     * 调用保存商保登记信息
     */
    public void saveCiInsuRegInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

        //调用保存商保登记信息
        ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS210", new Object[]{paramMap, user});
    }

    /**
     * 交易号：022004005003
     * 修改商保登记信息
     */
    public void updateCiInsuRegInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

        //调用修改商保登记信息
        ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS230", new Object[]{paramMap, user});
    }

    /**
     * 交易号：022004005004
     * 撤销商保登记信息
     */
    public void canlCiInsuRegInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

        //调用撤销商保登记信息
        ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS240", new Object[]{paramMap, user});
    }

    /**
     * 交易号：022004005005
     * 商保费用明细上传
     */
    public void zyMirHpBillUpload(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

        //调用商保费用明细上传
        ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS250", new Object[]{paramMap, user});
    }

    /**
     * 交易号：022004005006
     * 撤销商保费用明细上传
     */
    public void canlZyMirHpBillUpload(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

        //调用商保费用明细上传
        ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS260", new Object[]{paramMap, user});
    }

    /**
     * 病历信息上传
     * 022004005013
     *
     * @param param
     * @param user
     */
    public void medicalRecordUpload(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

        //调用商保费用明细上传
        ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS340", new Object[]{paramMap, user});
    }

    /**
     * 交易号：022004005008
     * 商保预结算
     *
     * @param param 入参
     * @param user  用户
     * @return 结果
     */
    public Map<String, Object> zyMirHpPreSettle(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> rtnMap = new HashMap<>(16);

        //调用开商保客户身份校验
        rtnMap = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS290", new Object[]{paramMap, user});

        return rtnMap;
    }

    /**
     * 商保结算
     * 022004005009
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> zyMirHpSettle(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> rtnMap = new HashMap<>(16);

        //调用开商保客户身份校验
        rtnMap = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS290", new Object[]{paramMap, user});

        return rtnMap;
    }

    /**
     * 取消结算
     * 022004005011
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> zyMirHpSettleCancel(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> rtnMap = new HashMap<>(16);

        //调用开商保客户身份校验
        rtnMap = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS270", new Object[]{paramMap, user});

        return rtnMap;
    }

    /**
     * 理赔申请
     * 022004005010
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> zyMirStApf(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> rtnMap = new HashMap<>(16);

        //调用开商保客户身份校验
        rtnMap = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS300", new Object[]{paramMap, user});

        return rtnMap;
    }

    /**
     * 商保理赔确认
     * 022004005012
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> zyMirStConfrim(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> rtnMap = new HashMap<>(16);

        //调用开商保客户身份校验
        rtnMap = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("CiInsu", "sendS310", new Object[]{paramMap, user});

        return rtnMap;
    }

}
