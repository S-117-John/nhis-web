package com.zebone.nhis.bl.pub.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.vo.BillItemVo;
import com.zebone.nhis.bl.pub.vo.BlDiagDivVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdHpItemdiv;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 费用信息查询
 * @author yangxue
 *
 */  
@Mapper
public interface  CgQryMaintainMapper {
  /**
	 * 查询住院费用明细
	 * @param paramMap
	 * @return
	 */
  public List<Map<String,Object>> queryBlCgIpDetails(Map<String,Object> paramMap);
  
  /**
	 * 查询住院费用明细(含高值耗材条码号)
	 * @param paramMap
	 * @return
	 */
  public List<Map<String,Object>> queryBlCgIpDetailsIncludeHVB(Map<String,Object> paramMap);
  
  /**
   * 查询限价明细
   * @return
   */
  public List<BlDiagDivVo> queryItemCateList(@Param("pkPv")String pkPv);
  /**
	 * 查询总费用
	 * @param paramMap{pkPv}
	 * @return
	 */
  public BigDecimal getTotalFee(@Param("pkPv")String pkPv);
  /**
   * 查询患者各类费用总额
   * @return
   */
  public List<Map<String,Object>> getPvTotalFeeByCate(@Param("pkPv")String pkPv);
  /**
   * 批量查询医保计划详细信息
   * @param mapParam
   * @return
   */
  public List<BdHp> qryBdHpInfoList(Map<String,Object> paramMap);
  /**
   * 批量查询医保计划详细信息
   * @param mapParam
   * @return
   */
  public BdHp qryBdHpByPiCate(Map<String,Object> paramMap);
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
   * 获取医嘱对应的收费项目集合
   * @param paramMap{pkOrds}
   * @return
   */
  public List<Map<String,Object>> queryItemByOrd(Map<String,Object> paramMap);
  /**
   * 根据收费项目主键获取收费项目
   * @param pkItems
   * @return
   */
  public List<BdItem> queryItemsByPk(List<String> pkItems);
  /**
   * 查询医保按项目分摊定义内容
   * @param pkHp
   * @return
   */
  public List<BdHpItemdiv> queryHpItemDivList(@Param("pkHp")String pkHp,@Param("dateHap")String dateHap);
  /**
   * 根据费用分类查询发票分类
   * @param {pkItemcates}
   * @return
   */
  public List<Map<String,Object>> queryInvItemByItemCate(Map<String,Object> param);
  /**
   * 根据费用分类查询核算码
   * @param {pkItemcates}
   * @return
   */
  public List<Map<String,Object>> queryAuditItemByItemCate(Map<String,Object> param);
  
  /**
   * 根据就诊主键，查询医保计划详细信息
   * @param pkPv
   * @param euPvType 
   * @return
   */
  public List<BdHp> qryBdHpInfoListByPv(Map<String,Object> paramMap);
 
  /**
   * 判断当前医保是否广州公医，1是0否
   * @param pkHp
   * @return
   */
  public String qryHpValAttr(@Param("pkPv") String pkPv);
  
  /**
   * 批量查询账单码与发票码
   * @param paramMap
   * @return
   */
  public List<BillItemVo> qryBillCodeByPkItems(Map<String,Object> paramMap);
  
}
