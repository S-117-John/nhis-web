package com.zebone.nhis.compay.ins.shenzhen.dao.base;


import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybPhysirecord;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InsSzybPhysirecordDao {

    List<Map<String,Object>> selectByPrimaryKey(String pkPhysirecord);

    int updateBke155(Map<String,Object> map);

    List<Map<String,Object>> findInsSzybDictMap(Map<String,Object> map);

    int delete(Map<String,Object> map);

    int deleteDictMap(Map<String,Object> map);
}