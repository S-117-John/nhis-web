package com.zebone.nhis.base.bd.dao;

import com.zebone.nhis.base.bd.vo.*;
import com.zebone.nhis.base.pub.vo.CgdivItemVo;
import com.zebone.nhis.common.module.base.bd.srv.*;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.scm.pub.BdHpCgdivTmp;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrvMapper {

	int BdOrdExcluCheckExist(Map<String, String> params);

	/***获取医嘱项目及相关所有信息*/
	List<BdOrdAndPkDeptVo> getBdOrdByRequires(@Param("searchData")String searchData,@Param("jg") String jg,@Param("codeOrdtype") String codeOrdtype,@Param("code") String code,@Param("name") String name,@Param("pkOrd") String pkOrd,@Param("spcode") String spcode,@Param("dtOrdType") String DtOrdCate,@Param("pkDept") String pkDept,@Param("codeExt") String codeExt,@Param("dtContype") String dtContype,@Param("delFlag") String delFlag,@Param("pkOrg") String pkOrg);
	List<BdOrdAndPkDeptVo> getBdOrdByJgRequires(@Param("jg") String jg,@Param("codeOrdtype") String codeOrdtype,@Param("code") String code,@Param("name") String name,@Param("pkOrd") String pkOrd,@Param("spcode") String spcode,@Param("dtOrdType") String DtOrdCate,@Param("pkDept") String pkDept,@Param("codeExt") String codeExt,@Param("dtContype") String dtContype,@Param("delFlag") String delFlag);
	BdOrd getBdOrdByPk(@Param("pkOrd") String pkOrd);
	List<BdOrd> findAllBdOrds();
	List<BdOrdAlias> getBdOrdAliasByOrd(@Param("pkOrd") String pkOrd);
	List<BdOrdOrg> getBdOrdOrgsByOrd(@Param("pkOrd") String pkOrd);
	List<BdOrdOrg> getBdOrdOrgList(@Param("pkOrd") String pkOrd);
	//查询收费项目信息
	List<BdOrdItemExt> getBdOrdItemsByOrd(@Param("pkOrd") String pkOrd,@Param("pkOrg") String pkOrg);
	List<BdOrdEmr> getBdOrdEmrsByOrd(@Param("pkOrd") String pkOrd);
	BdOrdLab getBdOrdLabByOrd(@Param("pkOrd") String pkOrd);
	BdOrdRis getBdOrdRisByOrd(@Param("pkOrd") String pkOrd);
	//List<BdOrdDept> getBdOrdDeptsByOrd(@Param("pkOrd") String pkOrd,@Param("pkOrg") String pkOrg);
	List<BdOrdDept> getBdOrdDeptsByOrd(@Param("pkOrd") String pkOrd);
	List<BdOrdLabCol> getBdOrdLabColList(@Param("pkOrd") String pkOrd,@Param("pkOrg") String pkOrg);
	/**收费项目-明细查看**/
	BdItem getBdItemByPk(@Param("pkItem") String pkItem);
	
	List<BdItem> getBdItemsByCon(List<BlIpDt> list);
	
	List<BdItemPrice> getBdItemPriceByItem(@Param("pkItem") String pkItem, @Param("orgpklist") List<String> orgPkList);
	
	List<BdItemPrice> getBdItemPriceByItemContainDel(@Param("pkItem") String pkItem, @Param("orgpklist") List<String> orgPkList, @Param("isContainAll") String isContainAll);
	
	List<BdItemHp> getBdItemHpByItem(@Param("pkItem") String pkItem);
	
	List<BdItemSp> getBdItemSpByItem(@Param("pkItem") String pkItem);
	
	List<BdItemSet> getBdItemSetByItem(@Param("pkItem") String pkItem);
	
	/**获取收费项目扩展属性*/
	List<BdItemAttrVo> getBdItemAttrByItem(@Param("pkOrg") String pkOrg,@Param("pkItem") String pkItem);
	
	/**获取收费项目扩展属性模板*/
	List<BdItemAttrVo> getBdItemAttrtemp(@Param("pkOrg") String pkOrg);
	
	/**查询收费项目关联组套信息*/
	List<Map<String,Object>> qryRatStack(@Param("pkItem") String pkItem);
	
	/**查询收费项目关联医嘱信息*/
	List<Map<String,Object>> qryRatOrd(@Param("pkItem") String pkItem);
	/**更新医疗记录信息*/
	void updateBdord(BdOrdEmr emr);

	List<BdItemSp> qrySp(@Param("pkItem")String pkItem);
 
	List<CgdivItemVo> qryCgDiv(@Param("pkItem")String pkItem);
	
	int delSp(@Param("pkItemsp")String pkItemsp);
	
	/**
	 * 查询记费策略模板
	 * @return
	 */
	public List<BdHpCgdivTmp> qryCgdivTmp();
	
	/**
	 * 查询记费策略模板明细
	 * @param codeTmp 模板编码
	 * @return
	 */
	public List<Map<String,Object>> qryCgDivTmpDt(String codeTmp);
	
	/**
	 * 查询医嘱项目拓展属性
	 * @param 
	 * @param 
	 * @return
	 */
	List<Map<String, Object>> getBdOrdAttrByOrd(@Param("pkOrgUse")String pkOrgUse,@Param("pkDict")String pkDict);
	
	public String[] qryExcluDt(String pkExclu);
	
	public void updateOrdBypk(Map<String, String> map);
	
	//收费项目导出信息查询
	public List<Map<String,Object>> queryByExcel(Map<String, String> map);
	
	//校验是否被医嘱业务引用
	public Integer countCnOrder(String codeOrdtype);
	//校验是否被医嘱字典引用
	public Integer countBdOrd(String codeOrdtype);
	
	//删除医嘱类型定义
	public void delBdOrdtype(String pkOrdtype);
	

	List<Map<String, Object>> getChargeItemList(Map<String, Object> map);

	List<BdOrdAndPkDeptVo> getBdOrdByRequiresSqlserver(@Param("searchData")String searchData,@Param("jg") String jg,@Param("codeOrdtype") String codeOrdtype,@Param("code") String code,@Param("name") String name,@Param("pkOrd") String pkOrd,@Param("spcode") String spcode,@Param("dtOrdType") String DtOrdCate,@Param("pkDept") String pkDept,@Param("codeExt") String codeExt,@Param("dtContype") String dtContype,@Param("delFlag") String delFlag,@Param("pkOrg") String pkOrg);

	List<BdOrdAndPkDeptVo> getBdOrdByJgRequiresSqlserver(@Param("jg") String jg,@Param("codeOrdtype") String codeOrdtype,@Param("code") String code,@Param("name") String name,@Param("pkOrd") String pkOrd,@Param("spcode") String spcode,@Param("dtOrdType") String DtOrdCate,@Param("pkDept") String pkDept,@Param("codeExt") String codeExt,@Param("dtContype") String dtContype,@Param("delFlag") String delFlag);

	List<BdOrdAndPkDeptVo> findOrderByItemCode(@Param("codeOrdtype") String codeOrdtype,@Param("code") String code,@Param("name") String name,@Param("dtOrdType") String dtOrdType,@Param("codeExt") String codeExt,@Param("dtContype") String dtContype,@Param("chargeItemCode") String chargeItemCode,@Param("pkOrg") String pkOrg);

	int restoreChargeItem(@Param("pkItem") String pkItem);

	/**
	 * 批量修改-关联收费项目条目查询
	 * @param pkItem
	 * @return
	 */
	String getBdItemCount(@Param("pkItem") String pkItem);

	/**
	 * 批量修改-保存修改信息
	 * @param bdOrdOrgVo
	 * @return
	 */
	void SaveItemOld(BdOrdItemOldVo bdOrdOrgVo);

	List<Map<String,Object>> getBdItemByItemCate(Map<String,Object> map);

	List<BdOrdAndPkDeptVo> getSetMealByPkOrd(List<String> list);

	List<Map<String,Object>> findOrderPrice(List<String> list);
	
	List<Map<String,Object>> findOrderSumPrice(List<String> list);

	List<Map<String,Object>> getUpdateOrderPrice(Map<String,Object> map);

	/**
	 * 编辑医嘱项目同步更新价格
	 * @param map
	 * @return
	 */
	int updateOrdOrgPrice(Map<String, Object> map);

	/**
	 * 获取收费项目对应的医嘱项目
	 *
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getOrdByPkItem(Map<String, Object> map);

	List<Map<String, Object>> findOrderPriceSqlSer(List<String> pkOrders);

	/**
	 * 获取医嘱项目下的收费项目
	 * @param pkOrd
	 * @return
	 */
	List<BdOrdItem> getBdItemByPkOrd(String pkOrd);

    BdPd getBdPdByPk(@Param("pkItem") String pkItem);

    List<PrintItemInfoVo>  getPrintItemInfo(ParamBdOrdVo paramBdOrdVo);

	List<PrintItemInfoVo> getPrintItemInfoGroupOrdName(ParamBdOrdVo paramBdOrdVo);


}
