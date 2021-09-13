package com.zebone.nhis.pro.zsba.mz.pub.dao;

import com.zebone.nhis.pro.zsba.mz.pub.vo.PiParamVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsbaOpSettleSyxMapper {
	/**
	 * 更新患者基本信息,只更新界面录入的信息
	 * @param pivo
	 */
	public void updatePiMaster(PiParamVo pivo);
	/**
	 * 更新就诊信息
	 * @param pivo
	 */
	public void updatePvEncounter(PiParamVo pivo);
}
