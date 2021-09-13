package com.zebone.nhis.compay.ins.syx.dao.pub;

import java.util.List;

import com.zebone.nhis.common.module.compay.ins.syx.GzybDataSource;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface GzybDataSourceMapper {

	/** 015001007001 查询数据源配置列表 */
	public List<GzybDataSource> getList();

	/** 015001007002 保存医保数据源配置信息 */
	public int save(GzybDataSource info);

}
