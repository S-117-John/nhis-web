package com.zebone.nhis.cn.pub.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.cn.pub.vo.BdSerialno;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdSerialnoMapper {
    Double selectSn(@Param("tableName") String tableName, @Param("fieldName") String fieldName); 

    //int updateSn(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("count") int count);

    int initSn(BdSerialno initSn);

    /**
     * 获取CIS系统医嘱号
     * @param count
     * @return
     */
    //updateCisOrdSn(@Param("count") int count,@Param("orderSn") int orderSn);
    //void updateCisOrdSn(Map<String, Object> param);
    /**
     * 获取最大长度
     * @param tableName
     * @param fieldName
     * @return
     */
    Integer selectLength(@Param("tableName") String tableName, @Param("fieldName") String fieldName);

}