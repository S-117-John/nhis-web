package com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao;

import java.util.Map;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaSignInQg;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 全国医保
 * @author Administrator
 *
 */
@Mapper
public interface InsQgybSignInMapper {
	/**
	 * 获取签到信息
	 * @param map
	 * @return
	 */
	public InsZsbaSignInQg getInsSignIn(Map<String,Object> map);
}
