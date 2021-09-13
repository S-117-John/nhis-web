package com.zebone.nhis.pro.zsrm.cn.service;

import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class OrdHandler {
    /**
     * 查询患者历史就诊信息
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> serschPv(String param, IUser user) {
        Map<String,Object> serParam= JsonUtil.readValue(param, Map.class);
        // 校验条件
        String codePi= MapUtils.getString(serParam,"codePi");
        if(StringUtils.isEmpty(codePi)){
            throw new BusException("未获取到查患者ID！");
        }
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        Callable<List<Map<String,Object>>> task = new Callable<List<Map<String,Object>>>() {
            @Override
            public List<Map<String,Object>> call() throws Exception {
                //DataSourceRoute.putAppId("ZSRM_OLD");
                String sql="select * from view_nhis_query_visit where p_id= ? ";
                List<Map<String, Object>> list = DataBaseHelper.queryForList(sql,new Object[]{codePi});
                return list;
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Future<List<Map<String,Object>>> future = executorService.submit(task);
            result = future.get(7, TimeUnit.SECONDS);
        }catch (Exception e) {
            throw new BusException("查询患者就诊历史信息出错/超时！！！");
        }finally {
            //DataSourceRoute.putAppId("default");
            executorService.shutdownNow();
        }
        return result;
    }

    /**
     * 查询患者就诊医嘱信息
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> serschOrdByPv(String param, IUser user) {
        Map<String,Object> serParam= JsonUtil.readValue(param, Map.class);
        // 校验条件
        String pId= org.apache.commons.collections.MapUtils.getString(serParam,"codePi");
        if(StringUtils.isEmpty(pId)){
            throw new BusException("未获取到查患者ID！");
        }
        String times= MapUtils.getString(serParam,"times");
        if(StringUtils.isEmpty(times)){
            throw new BusException("未获取到查患者就诊次数！");
        }

        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        Callable<List<Map<String,Object>>> task = new Callable<List<Map<String,Object>>>() {
            @Override
            public List<Map<String,Object>> call() throws Exception {
                //DataSourceRoute.putAppId("ZSRM_OLD");
                List<Map<String, Object>> list = new ArrayList<>();
                list = DataBaseHelper.queryForList(
                        "select * from view_nhis_query_order where p_id= ? and times = ? ",
                        new Object[] { pId,times });
                return list;
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Future<List<Map<String,Object>>> future = executorService.submit(task);
            result = future.get(7, TimeUnit.SECONDS);
        }catch (Exception e) {
            throw new BusException("查询患者就诊医嘱信息出/超时！！！");
        }finally {
            //DataSourceRoute.putAppId("default");
            executorService.shutdownNow();
        }
        return result;
    }
}
