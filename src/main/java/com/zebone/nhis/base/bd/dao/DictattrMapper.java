package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.base.bd.vo.DictattrVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DictattrMapper {

	public List<Map<String, Object>> qirDictattrTemp(String dicttype);
	
	public List<Map<String, Object>> qirDictattr(String codeAttr);
	
	public void updateDictattr(DictattrVo dictattrVo);
	
	public void insertDictattr(DictattrVo dictattrVo);
	
	public void delDictattr(DictattrVo dictattrVo);
	
	public List<Map<String, Object>> qryLeadDictattr(DictattrVo dictattrVo);
}
