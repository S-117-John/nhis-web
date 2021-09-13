package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;





import com.zebone.nhis.common.module.cn.ipdw.QcEhpDetail;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 
 * @author dell
 *
 */
@Mapper
public interface QcEhpMapper {
	/**
	 * 查询质控列表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryQcEhpList(Map<String, Object> paramMap);
	
	/**
	 * 查询质控记录
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryQcEhpRecList(Map<String, Object> paramMap);
	/**
	 * 查询质控缺陷
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryQcEhpdetailList(Map<String, Object> paramMap);
	/**
	 * 评分-查询质控项目评分项
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryBdScoreItem(Map<String, Object> paramMap);
	/**
	 * 评分-查询评分历史记录
	 * @param paramMap
	 * @return
	 */
	public List<QcEhpDetail> queryScoreItem(Map<String, Object> paramMap);

}
