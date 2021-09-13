package com.zebone.nhis.task.compay.service;

import com.zebone.nhis.common.module.scm.pub.BdPdIndpd;
import com.zebone.nhis.task.compay.dao.InsZsrmTaskMapper;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 中山人民医院定时任务
 */
@Service
public class InsZsrmTaskService {
    private static Logger log = LoggerFactory.getLogger("nhis.quartz");
    @Autowired
    private InsZsrmTaskMapper insZsrmTaskMapper;
    /**
     * 同步限制用药信息
     */
    public void synchronizeMedicareInfo(QrtzJobCfg cfg){
        log.info("****************删除临床医嘱提示信息定时任务*****************");
        List<BdPdIndpd> list = insZsrmTaskMapper.qryRestrictInfo();
        if(!list.isEmpty()) {
//        向bd_pd_indpd表插入数据
            insZsrmTaskMapper.batchInsertBdPdIndpd(list);
//            insZsrmTaskMapper.addRestrictInfo();
        log.info("新增"+list.size()+"条限制用药信息");
        }
        log.info("****************结束任务删除临床医嘱提示信息*****************");
    }
}
