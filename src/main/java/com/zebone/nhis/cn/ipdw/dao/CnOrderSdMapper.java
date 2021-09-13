package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zebone.nhis.cn.ipdw.vo.CnOrderInputVO;
import com.zebone.nhis.cn.ipdw.vo.SzybVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnOrderSdMapper {
	//医嘱录入字典查询--深大版本
	List<CnOrderInputVO> findOrdBaseList(Map<String, Object> params);
	
	//查询药品的限制信息
	List<SzybVo> findOrdSzyb(Map<String, Object> params);

	List<SzybVo> findOrdSzybByCodes(String code);
}
