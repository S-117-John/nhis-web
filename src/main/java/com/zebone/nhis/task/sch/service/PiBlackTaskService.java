package com.zebone.nhis.task.sch.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsrm.sch.service.PiBlackService;
import com.zebone.nhis.pro.zsrm.sch.vo.PiBlackVo;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PiBlackTaskService {


    @Autowired
    private PiBlackService piBlackService;

    public void autoLock(QrtzJobCfg cfg) {
        valid(cfg);
        int limitDay = 10;
        if(StringUtils.isNotBlank(cfg.getJobparam())){
            try{
                Map map = JsonUtil.readValue(cfg.getJobparam(), Map.class);
                limitDay = MapUtils.getIntValue(map,"limitDay");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        piBlackService.genPiLock(limitDay);
    }

    public void autoDelLock(QrtzJobCfg cfg) {
        valid(cfg);
        piBlackService.delAutoLock();
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
