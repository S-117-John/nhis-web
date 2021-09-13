package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.opdw.vo.BdOrdSetShare;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdDept;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdLab;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdRis;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Lis;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OpTemplatesMapper {
	//获得医嘱模板列表
	public List<Map<String,Object>> getTemplates(Map<String,Object> map);
	//获得医嘱模板明细列表
	public List<Map<String,Object>> getTemplatesDetail(Map<String,Object> map);
	//获得患者常用药品列表
	public List<Map<String,Object>> getFavorites(Map<String,Object> map);
	//获得复制处方列表
	public List<Map<String,Object>> getOpOrderCopy(Map<String,Object> map);
	//获得复制处方明细列表  没有用到  明细从处方CpOrderMapper中的getPrescriptionDetail获得
	public List<Map<String,Object>> getOpOrderCopyDetail(Map<String,Object> map);
	//获得医嘱模板列表
	public List<Map<String,Object>> getTemplatesCure(Map<String,Object> map);
	//获得医嘱模板明细列表
	public List<Map<String,Object>> getTemplatesCureDetail(Map<String,Object> map);

	//获得草药模板列表
	public List<Map<String,Object>> getHerbTemplates(Map<String,Object> map);

	//获得草药模板列表明细
	public List<Map<String,Object>> getHerbTemplatesDetail(Map<String,Object> map);

	//获得模板列表明细
	public List<Map<String,Object>> getAllDetails(Map<String,Object> map);

	//检验模板编码和名称是否唯一
	public int ischeckoutSoleCodeOrName(Map<String, String> params);
	//查询医嘱项目的默认执行科室和机构
	public List<BdOrdDept> getBdOrdDepts(Map<String, String> params);
	//根据主键查询检查项目定义
	public List<BdOrdRis> getRisDescAtt(Map<String, String> params);
	//根据主键查询检验项目定义
	public List<BdOrdLab> getLabNote(Map<String, String> params);

	public List<Map<String,Object>> getOrdSetShare(Map<String, Object> params);

	public List<Map<String,Object>> getOrdSetUnShare(Map<String, Object> params);
}
