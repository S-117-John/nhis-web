package com.zebone.nhis.pi.pub.support;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiMaster;

@Service
public interface EmpiClientInterFace {
	
	//患者主索引注册接口
	public Object addEmpi(PiMaster piMaster);
	//患者主索引更新接口
	public Object updateEmpi(PiMaster piMaster);
	//患者主索引查询接口
	public Object queryEmpi(PiMaster piMaster);
	//患者主索引合并接口
	public Object combineEmpi(Object object);
	//交叉主索引查询接口
	public Object queryRelateEmpi(String code, String idtype,String cardNo);
}
