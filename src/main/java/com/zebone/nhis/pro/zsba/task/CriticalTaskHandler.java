package com.zebone.nhis.pro.zsba.task;

import com.zebone.nhis.common.module.base.support.CvMsg;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.cn.opdw.service.CnOrderOpService;
import com.zebone.nhis.pro.zsba.ex.service.ZsbaPvIpdailyService;
import com.zebone.nhis.pro.zsba.msg.service.CriticalMsgService;
import com.zebone.nhis.pro.zsba.msg.vo.ConditionVo;
import com.zebone.nhis.pro.zsba.msg.vo.PacsVo;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CriticalTaskHandler {
    @Autowired
    private CriticalMsgService criticalMsgService;
    @Autowired
    private ZsbaPvIpdailyService zsbaPvIpdailyService;
    @Autowired
    private CnOrderOpService cnOrderOpService;

    @SuppressWarnings("rawtypes")
    public void check(QrtzJobCfg cfg) {
        String pkOrg = cfg.getJgs();
        if (CommonUtils.isEmptyString(pkOrg))
        {throw new BusException("未获取到机构信息！");}
        ConditionVo vo = new ConditionVo();
        //Date date=new Date();
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);  
        //calendar.add(Calendar.DAY_OF_MONTH, -3);  
        //date = calendar.getTime();  
        //SimpleDateFormat formatter; 
        //formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
        //vo.setDateS(formatter.format(date)); 
        vo.setState("1");
        List<CvMsg> list = criticalMsgService.queryCriticalMsg(vo);
        DataSourceRoute.putAppId("lis");
        List<PacsVo> lis = criticalMsgService.checkMsg(list);
        DataSourceRoute.putAppId("pacs");
        List<PacsVo> pacs = criticalMsgService.checkMsg(list);
        if (lis != null && lis.size() > 0) {
            DataSourceRoute.putAppId("dept");
            Map<String, Object> map = criticalMsgService.updateMsg(lis, "lis");
            DataSourceRoute.putAppId("default");
            criticalMsgService.insertMsg(map);
        }
        if (pacs != null && pacs.size() > 0) {
            DataSourceRoute.putAppId("dept");
            Map<String, Object> map = criticalMsgService.updateMsg(pacs, "pacs");
            DataSourceRoute.putAppId("default");
            criticalMsgService.insertMsg(map);
        }
        DataSourceRoute.putAppId("default");
    }


    /**
     * 生成病区日报定时任务
     *
     * @param cfg
     */
    @SuppressWarnings("rawtypes")
    public void deptDayReportTask(QrtzJobCfg cfg) {
        try {
            String pkOrg = cfg.getJgs();
            if (CommonUtils.isEmptyString(pkOrg)) {
                throw new BusException("未获取到机构信息！");
            }
            String[] orgArr = pkOrg.split(",");
            if (orgArr != null && orgArr.length > 0) {
                zsbaPvIpdailyService.statDeptNsReport(orgArr);
                zsbaPvIpdailyService.statDeptReport(orgArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description 根据模板明细内容反写模板可以使用页签
     * 针对博爱fuck需求6418
     * @auther wuqiang
     * @Date 2021-04-09
     * @Param [cfg]
     * @return void
     */
    public void  updateBDOrdSetTask(QrtzJobCfg cfg){
        cnOrderOpService.updateBDOrdSet();
    }
}
