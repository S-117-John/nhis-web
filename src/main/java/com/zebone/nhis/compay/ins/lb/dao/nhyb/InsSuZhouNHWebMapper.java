package com.zebone.nhis.compay.ins.lb.dao.nhyb;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.mk.BdCndiag;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhouNHWebDisMap;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhWebJs;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhWebReginfo;
import com.zebone.nhis.compay.ins.lb.vo.nhyb.BdHpExPro;
import com.zebone.nhis.compay.ins.lb.vo.nhyb.InsNhybItemMap;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * TODO
 * 
 * @author
 */
@Mapper
public interface InsSuZhouNHWebMapper {

	/**
	 * 模糊查询所有单病种治疗方式记录
	 */
//	/public List<InsSuzhounhTreat> getInsSuzhounhTreatList(@Param("key")String key);
	
	/**
	 * 按照PK_PV删除宿州农合的登记信息
	 * @param pkpv
	 */
	void deleteRegInfo(String pkpv);
	
	/**
	 * 按照PK_PV删除宿州农合的登记信息
	 * @param pkpv
	 */
	void deleteRegInfoById(String id);
	
	/**
	 * 跟据PKPV查询宿州医保登记信息
	 * @param pkpv
	 * @return
	 */
	List<InsSuzhounhWebReginfo> getRegInfoByPkPv(String pkpv);
	
	/**
	 * 获取宿州农保结算数据
	 * @param pkSettle
	 * @return
	 */
	InsSuzhounhWebJs getJsInfoByPkSettle(String pkSettle);
	
	/**
	 * 删除宿州农保结算记录
	 * @param pkSettle
	 */
	//void deleteJsInfo(String pkSettle);
	/**
	 * 根据就诊主键获取bl_ip_dt中未上传至医保的收费项目集合和医嘱信息
	 * @param map
	 * @return
	 */
	//public List<CnOrderAndBdItem> qryBdItemAndOrderByPkPv(Map<String,Object> map);
	
	/**
	 * 查询农合医保项目与nhis的对照信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryNhItemWithInfo(Map<String,Object> map);
	
	/**
	 * 查询农合医保疾病与nhis的对照信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryNhDisWithInfo(Map<String,Object> map);
	
	/**
	 * 查询农合药品和nhis对照项目
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryNhPdWithInfo(Map<String,Object> map);

	/**
	 * 宿州农合医保发票打印，需要的结算信息
	 * @param map
	 * @return
	 */
	//public Map<String, Object> qrySettleInfoForFpPrint(Map<String,Object> map);

	/**
	 * 查询发票主键
	 * @param paramMap
	 * @return
	 */
	//public List<String> qrySettleInfoForFpid(Map<String, Object> paramMap);
	
	/** 获取结算信息列表（分页） */
	List<InsSuzhounhWebJs> getJSList(InsSuzhounhWebJs insSuzhounhWebJs);
	
	/**
	 * 查询宿州各医保收费项目与医院信息未匹配信息
	 * @param paramMap {"euMatch":"匹配状态（1：匹配，2：未匹配）", "pkHp":"医保主键", "xmlb":"项目类别", "info":"文本框搜索信息（暂未使用）"}
	 * @return
	 */
	public List<Map<String,Object>> qryYbItemDicNoWithInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询宿州各医保疾病与医院信息未匹配信息
	 * @param paramMap {"euMatch":"匹配状态（1：匹配，2：未匹配）", "pkHp":"医保主键", "xmlb":"项目类别", "info":"文本框搜索信息（暂未使用）"}
	 * @return
	 */
	public List<Map<String,Object>> qryYbDisDicNoWithInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询宿州各医保药品与医院药品未匹配信息
	 * @param paramMap {"euMatch":"匹配状态（1：匹配，2：未匹配）", "pkHp":"医保主键", "xmlb":"项目类别", "info":"文本框搜索信息（暂未使用）"}
	 * @return
	 */
	public List<Map<String,Object>> qryYbPdDicNoWithInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询宿州医保对照信息
	 * @param param
	 * @return
	 */
	public List<InsSuzhouNHWebDisMap> qrySzybItemMapInfo(Map<String,Object> param);
	
	/**
	 * 查询诊断信息
	 * @param param
	 * @return
	 */
	public List<BdCndiag> qryYbDicNoWithInfo(Map<String,Object> param);
	
	/*
	 * 根据pkHp，List<pkitem>查询已匹配的项目对照项目
	 */
	public List<InsNhybItemMap> qrySzybItemMapInfoForMZ(Map<String,Object> param);
	
	
	/*
	 * 根据pkHp，List<pkitem>查询已匹配的项目对照项目
	 */
	public List<BdHpExPro> qryIpVisitCount(Map<String,String> param);
	
	
}
