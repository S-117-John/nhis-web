package com.zebone.nhis.common.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.cn.ipdw.CnNotice;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 临床消息提醒服务
 * @author yangxue
 *
 */
@Mapper
public interface CnNoticeMapper{   
    /**
     * 查询临床服务提醒消息数量
     * @param paramMap
     * @return
     */
	public List<Map<String,Object>> queryCnNoticeCnt(Map<String,Object> paramMap);
	/**
	 * 更新消息数量及状态
	 * @param paramMap
	 */
	public void updateCnNotice(Map<String,Object> paramMap);
	/**
     * 查询临床服务提醒消息详细信息
     * @param paramMap
     * @return
     */
	public List<CnNotice> queryCnNotice(Map<String,Object> paramMap);

	List<CnNotice> queryHintCnNotice(Map<String,Object> paramMap);

	/**
	 * 获取通知详情
	 * @param pkDept
	 */
	List<Map<String,Object>> noticeDetail(String pkDept);
}
