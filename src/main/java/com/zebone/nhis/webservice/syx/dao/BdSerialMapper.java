package com.zebone.nhis.webservice.syx.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.webservice.syx.vo.BdSerial;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdSerialMapper {
    Double selectSn(@Param("tableName") String tableName, @Param("fieldName") String fieldName); 

   // int updateSn(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("count") int count);

    int initSn(BdSerial initSn);

    /**
     * 获取CIS系统医嘱号
     * @param count
     * @return
     */
    //updateCisOrdSn(@Param("count") int count,@Param("orderSn") int orderSn);
   // void updateCisOrdSn(Map<String, Object> param);

}