package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.st.vo.PdInvInitVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 初始建账服务接口
 * @author yangxue
 *
 */
@Mapper
public interface StoreInitMapper {
	/**
	 * 查询初始化信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryInvInit(Map<String,String> map);
	
	
	/**
	 * 查询未结账的初始库存信息--合并库存使用
	 * @param param
	 * @return
	 */
	public List<PdInvInitVo> getAllInvInitByCC(Map<String,Object> param);
}
