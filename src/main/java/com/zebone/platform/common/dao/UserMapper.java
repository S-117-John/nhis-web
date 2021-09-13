package com.zebone.platform.common.dao;

import com.zebone.platform.common.support.UserMenuElement;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息
 * @author yangxue
 *
 */
@Mapper
public interface UserMapper {
	/**
	 * 查询菜单元素集合
	 * @param params
	 * @return
	 */
	public List<UserMenuElement> queryMenuElements(@Param("pkUser") String pkUser);
}
