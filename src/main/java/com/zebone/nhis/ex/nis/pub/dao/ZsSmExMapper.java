package com.zebone.nhis.ex.nis.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.nis.ns.vo.ZsLisCxInfo;
import com.zebone.nhis.ex.pub.vo.CnOrdAndAppVo;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsSmExMapper {
	
	/**
	 * 根据条码获取医嘱号
	 * @param paramMap
	 * @return
	 */
	public List<ZsLisCxInfo> nisGetZyInfoFromHis(Map<String, Object> paramMap);
	
	/**
	 * 根据医嘱号获取执行单 -- 作废
	 * @param paramMap
	 * @return
	 */
	public List<ExlistPubVo> getExlistByOrdsn(Map<String, Object> paramMap);
	
	/**
	 * 根据条形码更新采血项目 
	 * @param paramMap
	 * @return
	 */
	public int nisSetGatherInfoToLis(Map<String, Object> paramMap);
	
	/**
	 * 根据执行单主键更新执行单记录 -- 作废
	 * @param paramMap
	 * @return
	 */
	public int updateCxExlist(Map<String, Object> paramMap);

	/**
	 * 根据医嘱号获取执行单 + 申请单 【2018-03-23 扫码执行调试后修改】
	 * @param paramMap
	 * @return
	 */
	public List<CnOrdAndAppVo> getExAndAppByOrdsn(Map<String, Object> paramMap);

	/**
	 * 根据医嘱号更新校验申请单记录
	 * @param paramMap
	 * @return
	 */
	public int updateCxApp(Map<String, Object> paramMap);
	
}
