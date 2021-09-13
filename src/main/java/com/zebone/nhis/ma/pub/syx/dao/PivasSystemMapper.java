package com.zebone.nhis.ma.pub.syx.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.ma.pub.syx.vo.OrderOccVo;
import com.zebone.nhis.ma.pub.syx.vo.Torders;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PivasSystemMapper {
	/**
	 * 根据申请单创建中间库插入
	 * @param pkpdapdts
	 * @return
	 */
	public List<Torders> qryPivasList(List<String> pkpdapdts);
	
	/**
	 * 查询待记费的项目
	 * @return
	 */
	public List<Map<String,Object>> qryPivasCgItem();
	
	/**
	 * 更新tAccountList_Pivas读入标志和读入时间
	 * @param pkList
	 * @return
	 */
	public int updatePivas(@Param("pkList")List<String> pkList,@Param("readDateTime")Date readDateTime);

	/**
	 * 静配退药时查询已入仓的数据
	 * @param ordsnKeys
	 * @return
	 */
	public List<Map<String,Object>> checkPivasOut(List<String> ordsnKeys);
	
	
	public List<OrderOccVo> queryPivasData(List<String> pkCnord);
}
