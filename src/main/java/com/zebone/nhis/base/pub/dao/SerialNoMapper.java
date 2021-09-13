package com.zebone.nhis.base.pub.dao;

import com.zebone.nhis.base.pub.vo.SerialNoVO;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SerialNoMapper {

    List<SerialNoVO> getList();

    int delete(String id);

    List<Map<String,Object>> getSerialNo(Map<String, Object> map);

    List<SerialNoVO> getSerialVal(@Param("map") Map<String, Object> map);
}
