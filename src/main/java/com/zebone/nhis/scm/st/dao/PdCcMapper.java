package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.scm.st.PdCc;
import com.zebone.nhis.common.module.scm.st.PdCcDetail;
import com.zebone.nhis.scm.st.vo.PdBaseParam;
import com.zebone.nhis.scm.st.vo.PdLedgerVo;
import com.zebone.nhis.scm.st.vo.PdStDetailVo;
import com.zebone.platform.modules.mybatis.Mapper;

/***
 * 库存结账mapper
 *  
 * @author wangpeng
 * @date 2016年12月5日
 *
 */
@Mapper
public interface PdCcMapper {
	
	//根据库存结账主键查询
	public PdCc getPdCcById(String pkPdcc);
	
	//根据库存结账主键查询库存结账明细列表
	List<PdCcDetail> getPdCcDetailListById(String pkPdcc);
	
	//获取统计时间段内上月结账、出库、入库物品信息
	//List<PdStDetailVo> getPdStDetailVoList(Map<String, Object> mapParam);
	
	//List<PdCc> getPcMouth(Map<String, Object> mapParam);
	//获取统计时间段内物品基本信息
	List<PdBaseParam> getPdBaseParamList(Map<String, Object> mapParam);
	
	//库存台账：期初
	List<PdLedgerVo> getPdLedgerCCList(Map<String, Object> mapParam);
	//库存台账：收入
	List<PdLedgerVo> getPdLedgerSTInList(Map<String, Object> mapParam);
	//库存台账：支出
	List<PdLedgerVo> getPdLedgerSTOutList(Map<String, Object> mapParam);
	//库存台账：调价
	List<PdLedgerVo> getPdLedgerHistList(Map<String, Object> mapParam);
	//库存台账明细
	List<PdLedgerVo> getPdLedgerDt(Map<String, Object> mapParam);
}
