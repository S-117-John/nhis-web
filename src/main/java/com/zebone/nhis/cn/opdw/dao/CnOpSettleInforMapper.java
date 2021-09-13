package com.zebone.nhis.cn.opdw.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Classname CnOpSettleInforMapper
 * @Description TODO
 * @Date 2019-12-23 16:13
 * @Created by wuqiang
 */
@Mapper
public interface CnOpSettleInforMapper {


    public   List<Map<String, Object>>  qryChargeDetail(Map<String, Object> map);

   public  List<Map<String, Object>> qryChargeSum(Map<String, Object> map);

   public  List<Map<String, Object>> qryItemSum(Map<String, Object> map);
}
