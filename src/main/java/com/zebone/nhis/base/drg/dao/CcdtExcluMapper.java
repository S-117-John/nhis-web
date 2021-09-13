package com.zebone.nhis.base.drg.dao;

import com.zebone.nhis.base.drg.vo.BdTermCcdtSaveParam;
import com.zebone.nhis.common.module.base.bd.drg.BdTermCcdtExclu;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.List;
import java.util.Map;

@Mapper
public interface CcdtExcluMapper {
    //查询左侧树
    public List<BdTermCcdtSaveParam> qryCcdtExclu(@Param(value = "nameRule") String nameRule, @Param(value = "spcode")String spcode);
    //返回规则组号
    public Integer getGroupno();
    List<Map<String,Object>> getReleaseListByCcdtExclu(Map map);

}
