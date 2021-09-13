package com.zebone.nhis.scm.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.base.pub.vo.CgdivItemVo;
import com.zebone.nhis.common.module.scm.pub.BdFactory;
import com.zebone.nhis.common.module.scm.pub.BdHpCgdivTmp;
import com.zebone.nhis.common.module.scm.pub.BdPdAttDefine;
import com.zebone.nhis.common.module.scm.pub.BdPdStore;
import com.zebone.nhis.common.module.scm.pub.BdStore;
import com.zebone.nhis.common.module.scm.pub.BdStorePosi;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ScmPubMapper {

	public List<BdStore> findAllStores(@Param("pkOrg") String pkOrg);

	public BdStore getStoreByDept(@Param("pkDept") String pkDept);

	public List<Map<String,Object>> quePdStores(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> quePdStoresSpecial(Map<String,Object> paramMap);

	public int updatePdStore(BdPdStore pdStore);
    /**
     * 查询可选物品
     * @param paramMap
     * @return
     */
	public List<Map<String,Object>> queNewPds(Map<String,Object> paramMap);
	
	public List<BdFactory> findAllFactories();
	
	public List<BdPdAttDefine> findAllAttDefines(@Param("euPdtype") String euPdtype);
	
	public List<Map<String,Object>> quePdSupplyers(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queEditPdSupplyers(Map<String,Object> paramMap);
	
	/**查询供应商协议信息(物资-供应商协议)*/
	public List<Map<String,Object>> queMtlPdSupplyers(Map<String,Object> paramMap);
	
	/**
	 * 根据药品主键查询药品的分配信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPdStoreByPd(Map<String,Object> paramMap);

	public List<CgdivItemVo> qryCgDiv(@Param("pkItem")String pkItem);
	
	/**
	 * 药品字典维护-查询数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryBdPdDicList(Map<String,Object> paramMap);
	
	/**
	 * 查询本仓库对应的货位字典
	 * @param pkStore
	 * @return
	 */
	public List<BdStorePosi> qryStorePosiList(String pkStore);
	
	/**
	 * 校验当前包装单位存在未完成出库的记录!
	 * @param paramMap
	 * @return
	 */
	public int checkUnitOut(Map<String,Object> paramMap);
	
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
	 * 查询最大的医保上传编码
	 * @return
	 */
	public String qryCodeHpFromPd();
	
	public List<String> qryPdSearch(Map<String,Object> searchTxt);
	
	public List<Map<String,Object>> selectUniversalName(Map<String,Object> paramMap);

    List<BdPdAttDefine> quePdattdef(Map<String,Object> paramMap);

	/**
	 * 查询各药房库存信息
	 * @param pkPdList
	 * @return
	 */
	public List<Map<String,Object>> queryForPdStockInfo(@Param("pkPds") String pkPds);
}
