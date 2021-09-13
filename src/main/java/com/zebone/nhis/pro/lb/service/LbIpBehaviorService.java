package com.zebone.nhis.pro.lb.service;

import com.zebone.nhis.ma.pub.lb.service.MedicalInsuranceCostsHandler;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class LbIpBehaviorService {

    @Resource
    private MedicalInsuranceCostsHandler medicalInsuranceCostsHandler;
    /**
     * 门诊处方审核
     * @param param
     * @param user
     */
    public Map<String,Object> opReview(String param, IUser user){
        User u = (User)user;
        Map<String,Object> resp = (Map<String, Object>)ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "MXBAudit"
                , new Object[]{param,u});
        return resp;
    }
    /**
     * 强制结算申请
     * @param param
     * @param user
     */
    public void mandatory(String param, IUser user){
        ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "mandatory", new Object[]{param});
    }
    /**
     * 撤销处方
     * @param param
     * @param user
     */
    public void undoaudit(String param, IUser user){
        ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "undoaudit", new Object[]{param});
    }
    /**
     * 结算结果上报
     * @param param
     * @param user
     */
    public void clibalance(String param, IUser user){
        ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "clibalance", new Object[]{param});
    }
    /**
     * 住院医嘱上传审核
     * @param param
     * @param user
     */
    public Map<String,Object> getAuditContent(String param, IUser user){
        User u = (User)user;
        Map<String,Object> resp = (Map<String, Object>)ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "adviceAudit"
                , new Object[]{param,u});
        return resp;
    }

    /**
     * 住院医嘱保存
     * @param param
     * @param user
     */
    public void getAuditSaveContent(String param, IUser user){
        ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "adviceSave", new Object[]{param});

    }

    /**
     * 住院医嘱撤销
     * @param param
     * @param user
     */
    public void getAuditCancelContent(String param, IUser user){
        String pkPv = MapUtils.getString(JsonUtil.readValue(param, Map.class),"pkPv");
        String inpatientRxno = MapUtils.getString(JsonUtil.readValue(param, Map.class),"inpatientRxno");
        ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "adviceCancel", new Object[]{pkPv,inpatientRxno});

    }

    /**
     * 住院费用清单明细
     * @param param
     * @param user
     */
    public void getInpDetailInputContent(String param, IUser user){
        String pkPv = MapUtils.getString(JsonUtil.readValue(param, Map.class),"pkPv");
        ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "inpDetailInput", new Object[]{pkPv});

    }

    /**
     *撤销住院费用明细
     * @param param
     * @param user
     */
    public void getInpCancelFeeContent(String param, IUser user){
        String pkPv = MapUtils.getString(JsonUtil.readValue(param, Map.class),"pkPv");
        ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "inpCancelFee", new Object[]{pkPv});

    }

    /**
     * 住院费用审核
     * @param param
     * @param user
     */
    public Map<String,Object> getFreeAuditContent(String param, IUser user){
        String pkPv = MapUtils.getString(JsonUtil.readValue(param, Map.class),"pkPv");
        Map<String,Object> resp = (Map<String, Object>)ExtSystemProcessUtils.processExtMethod("MedicalCostsAudit", "freeAudit", new Object[]{pkPv});
        return resp;
    }
}
