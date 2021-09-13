package com.zebone.nhis.base.bd.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.mk.BdCndiag;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiagAs;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiagComp;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiagComt;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiagComtDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CndiagMapper {

	List<BdCndiag> qryCndiag(Map map);
	
	List<BdCndiagAs> qryAs(String pkCndiag);
	
	List<BdCndiagComt> qryCndiagComt(String pkCndiag);
	
	List<BdCndiagComp> qryCndiagComp(String pkCndiag);
	
	List<BdCndiagComtDt> qrBdCndiagComtDt(String pkCndiagcomt);
	
	void delCndiag(Map map);
	
	void delAs(Map map);
	
	void delCndiagComt(Map map);
	
	void delCndiagComp(Map map);
	
	void delBdCndiagComtDt(Map map);

	void delAsByList(Map map);

	void delCompByList(Map map);

	void delComtByList(Map map);

	void delDtByList(Map map);

	BdCndiag qryCndiagBypk(String pkCndiag);
	
	Integer qryCountCode(Map map);
	
	Integer qryCountName(Map map);

	void delDtByComt(String pkCndiag);

	List<BdCndiagComtDt> qryDtBylist(ArrayList<String> list);
	
	Integer getCodeCd(String codeCd);

	void delUnnecessaryDt();
	
	Integer qryCodeCd();
	
	//临床诊断维护查询术后备注字典列表
	List<Map<String, Object>> qryOpAfterDictionary();
}
