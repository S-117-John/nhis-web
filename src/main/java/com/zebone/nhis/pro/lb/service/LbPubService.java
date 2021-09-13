package com.zebone.nhis.pro.lb.service;

import com.zebone.nhis.pro.lb.dao.LbPiPubMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Classname LbPubService
 * 灵璧项目特殊化需求公共服务类
 * @Description TODO
 * @Date 2019-12-10 16:41
 * @Created by wuqiang
 */

@Service
public class LbPubService {
  @Resource
   private LbPiPubMapper lbPiPubMapper;

  /***
   * @Description
   * 根据身份证号码查询患者家庭成员信息
   * 023002001007
   * @auther wuqiang
   * @Date 2019-12-11
   * @Param [param, user]
   * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
   */
    public List<Map<String,Object >> queryPiFromOther(String param, IUser user){
       Map<String,Object> objectMap= JsonUtil.readValue(param,Map.class);
       String sql=" select * from  LB_INS_SUZHOUNH_WEB_PI  where ID_NO=?" ;
       List<Map<String,Object >> mapList= DataBaseHelper.queryForList(sql,objectMap.get("id_no"));
       if (mapList.size() > 0){
           return  lbPiPubMapper.queryPiFromOther(mapList.get(0));
       }
        return  new ArrayList<>();
    }



}
