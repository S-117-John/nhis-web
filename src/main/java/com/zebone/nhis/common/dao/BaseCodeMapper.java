package com.zebone.nhis.common.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.base.bd.code.BdWorkcalendardate;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BaseCodeMapper {
	
	BdCodeDateslot selectBdCodeDateslotByPk(String pk);
	
	List<BdCodeDateslot> getBdCodeDateslotByPkForType(@Param("pks")Set<String> pks);
	
	List<BdWorkcalendardate> getWordcalendardate(Map<String, String> params);

	/*List<BdWorkcalendardate> getWordcalendardateOracle(Map<String, String> params);*/
	
    List<BdCodeDateslot> listByDtDateslottype(String dtDateslottype);
	
    Integer findCntStd(Map<String, String> params);

}
