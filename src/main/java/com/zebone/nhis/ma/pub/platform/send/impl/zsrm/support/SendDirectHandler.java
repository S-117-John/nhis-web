package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 给人民医院的，不经过平台时，直连转发处理<br>
 *     ①不修改任务接口代码，默认平台透传，三方不需要重新对接。<br>
 *     ②对应关系直接入库、或者入缓存，不用重启服务做到及时切换
 */
@Service
public class SendDirectHandler {

    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    private static final String CATCH_ENABLE_KEY = "enable_config";
    private static final String CATCH_NAME = "direct:config";

    public List<String> getReqUrls(String remoteMethod,String content){
        try {
            String implicitRules = null;
            if(StringUtils.isNotBlank(content)) {
                JSONObject jsonObject = JSON.parseObject(content);
                implicitRules = jsonObject.getString("implicitRules");
                if(StringUtils.isBlank(implicitRules)) {
                    JSONArray jsonArray = jsonObject.getJSONArray("entry");
                    for (Object entry : jsonArray) {
                        if(entry == null){
                            continue;
                        }
                        JSONObject resource = ((JSONObject) entry).getJSONObject("resource");
                        if(resource!=null && (implicitRules = resource.getString("implicitRules"))!=null){
                            break;
                        }
                    }
                }
            }
            return getForwardCache(remoteMethod,implicitRules);
        } catch (Exception e){
            log.error("获取直连配置异常：",e);
        }
        return null;
    }


    /**
     * 004003011070
     * @param param
     * {"enable_config":"n","Condition":"http://xx.oo.aa,http://11.xx.aa","ServiceRequest_JYSQXZ":"xxx.xxx","ServiceRequest_JYSQXG":"oo.xx"}
     * @param user
     */
    public void refreshForwardMap(String param, IUser user){
        Map<String,String> map = JsonUtil.readValue(param, Map.class);
        if(MapUtils.isNotEmpty(map)){
            log.info("设置direct:config,{}",user.getId());
            String pre = RedisUtils.getRedisPreName();
            map.keySet().forEach(k -> RedisUtils.getRedisTemplate().opsForHash().put(pre+CATCH_NAME,k.toLowerCase(),map.get(k)));
        }
    }

    /**
     * 004003011071
     * @param param
     * @param user
     */
    public Map<String,Object> getForwardMap(String param, IUser user){
        Map<String,String> map = JsonUtil.readValue(param, Map.class);
        String qryStat = MapUtils.getString(map,"qryStat","0");

        Map<String,Object> rs = Maps.newHashMap();
        if(EnumerateParameter.ONE.equals(qryStat)) {
            String date = MapUtils.getString(map, "date", DateUtils.addDate(new Date(),-5,5,"yyyy-MM-dd HH:mm:ss"));
            List<SysMsgRec> list = DataBaseHelper.queryForList("select MSG_TYPE,MSG_STATUS,ERR_TXT,REMARK,CREATE_TIME from SYS_MSG_REC where CREATE_TIME>=to_date(?,'yyyy-MM-dd HH24:mi:ss')" +
                    " and TRANS_TYPE='send' and SYS_CODE='NHIS'and REMARK is not null",SysMsgRec.class,new Object[]{date});
            rs.put("stat",list);
        }
        rs.put("config", JsonUtil.writeValueAsString(RedisUtils.getRedisTemplate().opsForHash().entries(RedisUtils.getRedisPreName()+CATCH_NAME)));
        return rs;
    }


    private static List<String> getForwardCache(String key,String implicitRules){
        Object enableVal = RedisUtils.getCacheHashObj(CATCH_NAME, CATCH_ENABLE_KEY);
        if(enableVal == null || !StringUtils.equalsIgnoreCase(String.valueOf(enableVal),"y")){
            return null;
        }

        Map<Object, Object> config = RedisUtils.getRedisTemplate().opsForHash().entries(RedisUtils.getRedisPreName()+CATCH_NAME);
        String val = null;
        if(StringUtils.isNotBlank(implicitRules)) {
            val = MapUtils.getString(config, (key+"_"+implicitRules).toLowerCase());
        }
        val = val ==null?MapUtils.getString(config, key.toLowerCase()):val;
        if(StringUtils.isNotBlank(val)){
            return Lists.newArrayList(StringUtils.split(val,","));
        }
        return null;
    }


}
