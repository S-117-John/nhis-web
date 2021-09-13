package com.zebone.nhis.task.ma.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.sd.bl.service.RepEBillDataService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 电子票据自动任务
 */
@Service
public class EnoteTaskService {

    @Autowired
    private RepEBillDataService repEBillDataService;

    public Map autoInvoice(QrtzJobCfg cfg) {
        StringBuilder sblError = new StringBuilder();
        StringBuilder sblSuccess = new StringBuilder();
        String pkOrg = valid(cfg);
        User user = new User();
        user.setPkOrg(pkOrg);

        int days = 0;
        String elDtSttypes = null;//补开电子票据时的 结算类型
        if(StringUtils.isNotBlank(cfg.getJobparam())) {
            Map configMap = JsonUtil.readValue(cfg.getJobparam(), Map.class);
            days = MapUtils.getIntValue(configMap,"days");
            elDtSttypes = MapUtils.getString(configMap,"elDtSttypes");
        }
        Map<String,Object> params = new HashMap<>();
        //补开 作废发票
        params.put("dateBegin", DateUtils.addDate(new Date(),-days,3,"yyyyMMdd")+"000000");
        try {
            params.put("dtSttype","21");
            params.put("euEbillRep","2");//2:补传作废票
            String rs = repEBillDataService.drawCancelBill(JsonUtil.writeValueAsString(params),user);
            sblSuccess.append("补开作废票成功：").append("入参：").append(JsonUtil.writeValueAsString(params)).append("结果：").append(rs);
        } catch (Exception e){
            sblError.append("补开作废发票执行异常：").append(e.getMessage());
        }
        sblSuccess.append("补开电子票入参：");
        if(StringUtils.isNotBlank(elDtSttypes)) {
            //一般不会有多个，如果有，就循环调用就行了
            String[] dtSttypes = elDtSttypes.split(",");
            for (String dtSttype : dtSttypes) {
                if(StringUtils.isNotBlank(dtSttype)){
                   try {
                       params.put("dtSttype",dtSttype);
                       params.put("euEbillRep","0");//0:补传电子票
                       String rs = repEBillDataService.drawEbill(JsonUtil.writeValueAsString(params),user);
                       sblSuccess.append("[").append(JsonUtil.writeValueAsString(params)).append("]").append("结果：").append(rs);
                   } catch (Exception e){
                       sblError.append("补开电子票异常：").append("结算类型：").append(dtSttype).append(" 异常：").append(e.getMessage());
                   }
                }
            }
        } else {
            params.put("dtSttype","01");
            params.put("euEbillRep","0");//0:补传电子票
            String rs = repEBillDataService.drawEbill(JsonUtil.writeValueAsString(params),user);
            sblSuccess.append(JsonUtil.writeValueAsString(params)).append("结果：").append(rs);
        }
        params.clear();
        if(sblError.length()>0){
            params.put("customStatus", sblError.toString());
        }
        params.put("msg",sblSuccess.toString());
        return params;
    }

    private String valid(QrtzJobCfg cfg){
        String pkOrg = cfg.getJgs();
        if (StringUtils.isBlank(pkOrg)) {
            throw new BusException("请先对任务授权");
        }
        if (pkOrg != null && pkOrg.contains(",")) {
            pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(), "").replace(",", "");
        } else if (CommonUtils.getGlobalOrg().equals(pkOrg)) {
            throw new BusException("请将任务授权给具体机构");
        }
        return pkOrg;
    }

}
