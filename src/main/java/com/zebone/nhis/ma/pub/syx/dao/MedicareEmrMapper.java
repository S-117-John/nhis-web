package com.zebone.nhis.ma.pub.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.syx.vo.HomeLhVo;
import com.zebone.nhis.ma.pub.syx.vo.HomePageBrVo;
import com.zebone.nhis.ma.pub.syx.vo.HomePageDiagVo;
import com.zebone.nhis.ma.pub.syx.vo.HomePageOpVo;
import com.zebone.nhis.ma.pub.syx.vo.HomePageOrDtVo;
import com.zebone.nhis.ma.pub.syx.vo.HomePageOrVo;
import com.zebone.nhis.ma.pub.syx.vo.HomePageVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MedicareEmrMapper {

	/**
	 * 查询患者的首页数据
	 * @param map
	 * @return
	 */
	public List<HomePageVo> getHomePageList(Map map);
	
	/**
	 * 查询患者的诊断首页数据
	 * @param map
	 * @return
	 */
	public List<HomePageDiagVo> getHomePageDiagList(String pkPage);
	
	/**
	 * 查询患者的手术首页数据
	 * @param map
	 * @return
	 */
	public List<HomePageOpVo> getHomePageOpList(String pkPage);
	
	/**
	 * 查询患者的妇婴首页数据
	 * @param map
	 * @return
	 */
	public List<HomePageBrVo> getHomePageBrList(String pkPage);
	
	/**
	 * 查询患者的诊断首页数据
	 * @param map
	 * @return
	 */
	public List<HomePageOrVo> getHomePageOrList(String pkPage);
	
	/**
	 * 查询患者的诊断首页数据
	 * @param map
	 * @return
	 */
	public List<HomePageOrDtVo> getHomePageOrDtList(String pkPv);
	
	/**
	 * 查询患者的诊断首页数据
	 * @param map
	 * @return
	 */
	public List<HomeLhVo> getHomePageLhList(String pkPv);
}
