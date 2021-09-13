package com.zebone.nhis.pro.lb.dao;

import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Classname PiPubMapper
 * @Description TODO
 * @Date 2019-12-11 9:47
 * @Created by wuqiang
 */
@Mapper
public interface LbPiPubMapper {


   public List<Map<String, Object>> queryPiFromOther(Map<String, Object> objectMap);

   public  Map<String, Object> querySumIpMessage(@Param("listS")List<String> list,@Param("date") String date);

  public  Map<String, Object> querySumOpMessage(@Param("listS") List<String> list, @Param("date") String date);

}
