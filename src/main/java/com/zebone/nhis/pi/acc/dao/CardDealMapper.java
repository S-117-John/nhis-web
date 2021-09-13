package com.zebone.nhis.pi.acc.dao;

import com.zebone.nhis.common.module.pi.acc.PiCardIss;
import com.zebone.nhis.pi.acc.vo.PageQueryPiccParam;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CardDealMapper {
	
	List<PiCardIss> getPiCardIss(PiCardIss piCardIss);
	
	/**查询卡操作记录*/
	public List<Map<String,Object>> qryCardActionRec(Map<String,Object> paramMap);

  public    List<Map<String, Object>> queryPiAccs(PageQueryPiccParam pageQueryPiccParam);
  //查询领卡记录中的当前卡号
	Map<String,Object> getCardNo(Map<String,Object> paramMap);
}
