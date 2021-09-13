package com.zebone.nhis.ex.nis.ns.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 药品请领
 * @author yangxue
 *
 */
@Mapper
public interface DeptPdApplyMapper {
	/**
	 * 查询需要生成请领单的医嘱列表
	 * @param map{}
	 * @return
	 */
	public List<GeneratePdApExListVo> getGenPdApOrdList(Map<String,Object> map);

	/**
	 * 查询需要生成请领单的医嘱列表(查长期口服的执行单)
	 * @param map{}
	 * @return
	 */
	public List<GeneratePdApExListVo> getGenPdApOrdOralList(Map<String,Object> map);
	/**
	 * 获取当前机构系统参数的备注
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> getPivasRule(Map<String,Object> paramMap);
	
	/**
	 * 获取待进静配规则的数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryUpdateEx(Map<String,Object> paramMap);

	/**
	 * 获取待处理静配规则的数据 - with as
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryUpdateExByWithAs(Map<String,Object> paramMap);
	
}
