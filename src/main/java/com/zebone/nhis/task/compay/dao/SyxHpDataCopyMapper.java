package com.zebone.nhis.task.compay.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.compay.pub.syx.vo.ViewItem;
import com.zebone.nhis.compay.pub.syx.vo.ViewItemOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatch;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatchOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMedi;
import com.zebone.nhis.compay.pub.syx.vo.ViewMediOut;
import com.zebone.nhis.task.compay.vo.HpViewItemOutVo;
import com.zebone.nhis.task.compay.vo.HpViewItemVo;
import com.zebone.nhis.task.compay.vo.HpViewMatchOutVo;
import com.zebone.nhis.task.compay.vo.HpViewMatchVo;
import com.zebone.nhis.task.compay.vo.HpViewMediOutVo;
import com.zebone.nhis.task.compay.vo.HpViewMediVo;
import com.zebone.nhis.task.compay.vo.SyxHpDataCopyVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxHpDataCopyMapper {
	
	public List<SyxHpDataCopyVo> qryNeedCopyHpData(Map<String,Object> paramMap);
	
	public List<SyxHpDataCopyVo> qryNeedHpDataForOther();
	

	/**
	 * 获取中间库诊疗项目及服务设施目录-市医保/花都公医
	 * @return
	 */
	public List<HpViewItemVo> queryViewItemList();

	/**
	 * 获取中间库药品目录 -市医保/花都公医
	 * @return
	 */
	public List<HpViewMediVo> queryViewMediList();

	/**
	 * 获取中间库目录对应表-市医保/花都公医
	 * @return
	 */
	public List<HpViewMatchVo> queryViewMatchList();
	
	/**
	 * 获取中间库诊疗项目及服务设施目录-省内异地
	 * @return
	 */
	public List<HpViewItemOutVo> queryViewItemListOut();

	/**
	 * 获取中间库药品目录 -省内异地
	 * @return
	 */
	public List<HpViewMediOutVo> queryViewMediListOut();

	/**
	 * 获取中间库目录对应表-省内异地
	 * @return
	 */
	public List<HpViewMatchOutVo> queryViewMatchListOut();

}
