package com.zebone.nhis.bl.pub.syx.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.syx.vo.BlBedItemStyVo;
import com.zebone.nhis.bl.pub.syx.vo.BlHvItemStyVo;
import com.zebone.nhis.bl.pub.syx.vo.BlSpItemStyVo;
import com.zebone.nhis.bl.pub.syx.vo.ExOrdItemVo;
import com.zebone.nhis.bl.pub.syx.vo.ExamFeeStyVo;
import com.zebone.nhis.bl.pub.syx.vo.HpVo;
import com.zebone.nhis.bl.pub.syx.vo.ItemCgNumVo;
import com.zebone.nhis.bl.pub.syx.vo.OrdNumVo;
import com.zebone.nhis.bl.pub.syx.vo.PvIpDtVo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdChap;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivSpitem;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyBl;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 各类价格策略信息查询服务
 * @author yangxue
 *
 */  
@Mapper
public interface  CgStrategyPubMapper {
	/**
	 * 查询项目收费策略定义信息
	 * @param paramMap
	 * @return
	 */
	 public List<BdChap> queryBdChap(Map<String,Object> paramMap);
	 /**
	  * 查询医保记费策略--按项目记费策略信息
	  * @param paramMap
	  * @return
	  */
	public List<Map<String,Object>> queryHpCgDivItem(Map<String,Object> paramMap);
	 /**
	  * 查询医保记费策略--按项目分类记费策略信息
	  * @param paramMap
	  * @return
	  */
	public List<Map<String,Object>> queryHpCgDivItemcate(Map<String,Object> paramMap);
	 /**
	   * 获取高值耗材策略
	   * @param paramMap
	   * @return
	   */
	  public List<BlHvItemStyVo> qryHvitemPriceLimit(Map<String,Object> paramMap);
	  /**
	   * 查询床位费策略
	   * @param paramMap
	   * @return
	   */
	  public List<BlBedItemStyVo> qryBedItemPriceSty(Map<String,Object> paramMap);
	  /**
	   * 查询特殊项目费用策略
	   * @param paramMap
	   * @return
	   */
	  public List<BlSpItemStyVo> qrySpItemPriceSty(Map<String,Object> paramMap);
	  /**
	   * 查询高值耗材费用策略
	   * @param paramMap
	   * @return
	   */
	  public List<BlHvItemStyVo> qryHvItemPriceSty(Map<String,Object> paramMap);
	  /**
	   * 查询医保相关信息
	   * @param paramMap{pkHp,pkOrg}
	   * @return
	   */
	  public List<HpVo> queryHpList(Map<String,Object> paramMap);
	  /**
	   * 查询就诊信息列表
	   * @param paramMap
	   * @return
	   */
	  public List<PvEncounter> queryPvList(List<String> pvlist);
	  
	  /**
	   * 判断当前医保是否广州公医，1是0否
	   * @param pkHp
	   * @return
	   */
	  public String qryHpValAttr(@Param("pkHp") String pkHp);
	  
	  /**
	   * 获取特殊项目的分担比例
	   * @param pkHp
	   * @return
	   */
	  public List<InsGzgyDivSpitem> qrySpitemRatioUnit(@Param("pkHp") String pkHp);
	  
	  /**
	   * 根据患者就诊pk_pv查询费用明细
	   * @param pkPv
	   * @return
	   */
	  public PvIpDtVo qryPvIpDt(@Param("pkPv") String pkPv);
	  
	  
	  /**
	   * 获取指定医保下的收费项目价格
	   * @param paramMap{pkHp,dateHap,pkOrg,itemList}
	   * @return
	   */
	  public List<ItemPriceVo> queryItemAndChildPrice(Map<String,Object> paramMap);
	  /**
	   * 获取指定医保下的收费项目价格
	   * @param paramMap{pkHp,dateHap,pkOrg,ordList}
	   * @return
	   */
	  public List<ItemPriceVo> queryItemAndChildPriceByOrd(Map<String,Object> paramMap);
	  
	  /**
	   * 查询患者未结算收费明细
	   * @param pkPv
	   * @return
	   */
	  public List<BlIpDt> qryIpDtByPv(@Param("pkPv") String pkPv);
	  /**
	   * 查询患者未结算收费明细
	   * @param pkPv
	   * @return
	   */
	  public List<BlOpDt> qryOpDtByPv(@Param("pkPv") String pkPv);
	  
	  /**
	   * 查询收费项目特诊加收比例
	   * @param pkItems
	   * @return
	   */
	  public List<Map<String,Object>> qryRatioSpecList(@Param("euPvtype") String euPvtype,@Param("pkItems") Set<String> pkItems);
	  
	  /**
	   * 根据就诊主键和计费明细主键获取公医计费明细
	   * @param pkPv
	   * @param pkCgips
	   * @return
	   */
	  public List<InsGzgyBl> qryGzgyBlList(@Param("pkPv")String pkPv,@Param("pkCgips")List<String> pkCgips);
	  
	  /**
	   * 获取患者计费信息
	   * @param paramMap
	   * @return
	   */
	  public List<BlIpDt> qryIpDtList(Map<String,Object> paramMap);
	  
	  /**
	   * 查询医嘱项目下的收费项目总数量
	   * @param pkItem
	   * @return
	   */
	  public List<OrdNumVo> qryOrdItemNum(@Param("pkOrdList")List<String> pkOrdList);
	  
	  /**
	   * 查询检验医嘱对应的分类编码
	   * @return
	   */
	  public List<String> qryOrdType();
	  
	  /**
	   * 判断当前医保是否广州公医，1是0否
	   * @param pkHp
	   * @return
	   */
	  public String qryHpValAttrByPv(@Param("pkPv") String pkPv);
	  
	  /**
	   * 查询需要自动执行的医嘱数据
	   * @return
	   */
	  public List<BlPubParamVo> qryOrdExecBl();
	  
	  /**
	   * 更新医嘱执行单
	   * @param paramMap
	   * @return
	   */
	  public int updateOrdExocc(Map<String,Object> paramMap);
	  
	  /**
	   * 查询同组医嘱关联的收费项目
	   * @return
	   */
	  public List<ExOrdItemVo> qryExItem(@Param("ordsnParent") Integer ordsnParent);
	  
	  /**
	   * 查询患者就诊状态
	   * @param pkPv
	   * @return
	   */
	  public Map<String,Object> qryPvStatus(@Param("pkPv")String pkPv);
	  
	  /**查询收费项目及其数量限制属性*/
	  public List<ItemCgNumVo> qryItemCgNum(@Param("pkList") List<String> pkList);
	
	  /**获取患者指定日期下的已记费数量*/
	  public String qryCgNumByPv(@Param("pkPv")String pkPv,@Param("dateHap")String dateHap,@Param("pkItem")String pkItem);
	  
	  /**查询诊查费记费策略*/
	  public List<ExamFeeStyVo> qryExamFeeList(Map<String,Object> paramMap);
	  
	  /**查询单味草药医嘱信息*/
	  public List<String> qryHerbDispInfo(@Param("pkCnord")String pkCnord);

	  /**根据医嘱主键查询对应的医嘱合并代码信息*/
	  public List<Map<String,Object>> qryOrdCombByPkCnord(@Param("pkList") List<String> pkList);

	/**
	 * 根据就诊主键查询对应的医嘱信息
	 * @return
	 */
	public List<CnOrder> qryCnorderByPkPv(@Param("pkPv") String pkPv);

	/**根据pk_cnord查询医嘱记费次数*/
	List<Map<String,Object>> qryOrdChargeCnt(@Param("pkList") List<String> pkList);

	/**批量更新费用明细表结算主键*/
	public int updateBlOpDtListByPk(@Param("pkSettle") String pkSettle,@Param("blOpDtList") List<BlOpDt> blOpDtList);
	
	/**根据处方主键查询检验属性。合并记费用使用*/
	List<Map<String,Object>> qryOrdLabApplyListInfo(@Param("pkCnords") List<String> pkCnords);
}
