package com.zebone.nhis.base.ou.dao;

import java.util.List;

import com.zebone.nhis.common.module.base.ou.BdOuElement;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MenuMapper {

    List<BdOuUser> listAllUsers();

    List<BdOuElement> getElement(@Param("pkMenu")String pkMenu);
    
}
