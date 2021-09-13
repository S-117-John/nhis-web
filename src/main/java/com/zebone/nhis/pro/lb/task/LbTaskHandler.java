package com.zebone.nhis.pro.lb.task;

import com.zebone.nhis.pro.lb.service.LbTaskServcie;
import com.zebone.nhis.pro.lb.vo.BaseSendtaskCategory;
import com.zebone.nhis.pro.lb.vo.BaseSendtaskList;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LbTaskHandler {


    @Autowired
    private  LbTaskServcie lbTaskServcie;
    @SuppressWarnings("rawtypes")
    public void sendMessage(QrtzJobCfg cfg) {
        String sql="select * from BASE_SENDTASK_CATEGORY  where  id >'59'";
        try {
            DataSourceRoute.putAppId("OA");// 切换数据源
            List<BaseSendtaskCategory> baseSendtaskCategory = DataBaseHelper.queryForList(sql, BaseSendtaskCategory.class);
            DataSourceRoute.putAppId("default");// 切换数据源
            List<BaseSendtaskList> thirdMessageVo = lbTaskServcie.getTthirdMessageVo(baseSendtaskCategory);
            DataSourceRoute.putAppId("OA");
            lbTaskServcie.updataBae(thirdMessageVo);

        } catch(Exception e)
        {
            DataSourceRoute.putAppId("default");// 切换数据源
        } finally {
            DataSourceRoute.putAppId("default");// 切换数据源
        }
    }
}











