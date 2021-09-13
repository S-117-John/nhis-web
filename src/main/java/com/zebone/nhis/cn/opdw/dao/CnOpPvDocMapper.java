package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.cn.opdw.BdPvDocTemp;
import com.zebone.nhis.common.module.cn.opdw.PvDoc;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnOpPvDocMapper {
	/**
	 * 查询就诊文书模板的二进制数据
	 * @param pkPvdoctemp
	 * @return
	 */
	public List<BdPvDocTemp> getPvDocTempData(String pkPvdoctemp);
	
	/**
	 * 获取就诊文书的二进制数据
	 * @param pkTemp
	 * @return
	 */
	public List<PvDoc> getPvDocData(String pkTemp);
	
	/**
	 * 保存就诊文书
	 * @param pv
	 */
	public void insertPvDoc(PvDoc pv);
	
	
	public int getOnleCodeOrName(Map<String, String> params);
}
