package com.zebone.nhis.ex.nis.ns.dao;

import com.zebone.nhis.ex.nis.ns.vo.OrdVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wuqiang
 * @Date: 2018/10/31 17:45
 * @Description:
 */
@Mapper
public interface OrdPharmacyStopMapper {
	/**
	 * 查询待处理的停发
	 * @param pkDept
	 * @return
	 */
	public List<Map<String, Object>> ordQuery(String pkDept);
	/**
	 * 更新请领单相关信息
	 * @param ordVo
	 */
	public void ordStopUpdate(OrdVo ordVo);
	/**
	 * 更新执行单相关信息
	 * @param ordVo
	 */
	public void updateExlist(OrdVo ordVo);
}
