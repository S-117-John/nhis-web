package com.zebone.nhis.scm.material.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zebone.nhis.common.support.EnumerateParameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.srv.BdOrdDept;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.module.scm.pub.BdPdAs;
import com.zebone.nhis.common.module.scm.pub.BdPdAtt;
import com.zebone.nhis.common.module.scm.pub.BdPdConvert;
import com.zebone.nhis.common.module.scm.pub.BdPdStore;
import com.zebone.nhis.common.module.scm.pub.BdPdcate;
import com.zebone.nhis.common.module.scm.pub.BdStore;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.zb.utils.MapUtils;
import com.zebone.nhis.scm.material.dao.MtlBdPdMapper;
import com.zebone.nhis.scm.material.vo.BdPdAttVo;
import com.zebone.nhis.scm.material.vo.BdPdBaseVo;
import com.zebone.nhis.scm.material.vo.BdPdStoreInfo;
import com.zebone.nhis.scm.material.vo.MtlQryPdInfo;
import com.zebone.nhis.scm.material.vo.MtlSavePdInfo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 物资耗材-物品字典维护
 * @author wj
 *
 */
@Service
public class MtlBdPdService {
	
	@Autowired
	private MtlBdPdMapper mtlBdPdMapper;
	//增删改标志
    private String rleCode = "";
    /**
     * 添加状态
     **/
    public static final String AddState = "_ADD";

    /**
     * 更新状态
     */
    public static final String UpdateState = "_UPDATE";
	
	/**
	 * 交易码:008007001001
	 * 1-1 查询物品分类
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPdcateList(String param, IUser user) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = mtlBdPdMapper.queryBdPdcateList(map);
		return list;
	}
	
	/**
	 * 交易码:008007001002
	 * 1-2 保存 物品分类
	 * @param param
	 * @param user
	 * @return
	 */
	public BdPdcate saveBdPdcate(String param, IUser user) {
		BdPdcate pdcate = JsonUtil.readValue(param, BdPdcate.class);
		if(pdcate == null)
			throw new BusException("未获取到待保存的物品分类信息！");
		
		//1、校验编码、名称 全局唯一
		String codeSql = "select count(1) from bd_pdcate where code_pdcate=? and del_flag='0' ";
		String nameSql = "select count(1) from bd_pdcate where name_pdcate=? and del_flag='0' ";
		if(!CommonUtils.isEmptyString(pdcate.getPkPdcate())){
			codeSql += " and pk_pdcate != '"+pdcate.getPkPdcate()+"'";
			nameSql += " and pk_pdcate != '"+pdcate.getPkPdcate()+"'";
		}
		int count_code = DataBaseHelper.queryForScalar(codeSql, Integer.class, pdcate.getCodePdcate());
		int count_name = DataBaseHelper.queryForScalar(nameSql, Integer.class, pdcate.getNamePdcate());
		if(count_code > 0)
			throw new BusException("编码【"+pdcate.getCodePdcate()+"】已经存在，请重新输入！");
		if(count_name > 0)
			throw new BusException("名称【"+pdcate.getNamePdcate()+"】已经存在，请重新输入！");
		
		//2、保存相关记录
		if(CommonUtils.isEmptyString(pdcate.getPkPdcate())){
			pdcate.setPkOrg("~");
			DataBaseHelper.insertBean(pdcate);
		}else{
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(BdPdcate.class), pdcate);
		}
		return pdcate;
	}

	/**
	 * 交易码:008007001003
	 * 1-3  删除 物品分类
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void delBdPdcate(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到删除物品分类的相关入参！");
		if(null == map.get("pkPdcate") || CommonUtils.isEmptyString(map.get("pkPdcate").toString()))
			throw new BusException("未获取待删除的物品分类主键！");
		String pkPdcate = map.get("pkPdcate").toString();
		
		int count = DataBaseHelper.queryForScalar(
				"select count(1) from bd_pd where pk_pdcate=?", Integer.class, pkPdcate);
		if(count > 0)
			throw new BusException("该物品分类已经被使用，不可删除！");
		DataBaseHelper.execute("delete from bd_pdcate where pk_pdcate=?", pkPdcate);
	}

	/**
	 * 交易码:008007001004
	 * 2-1查询物品列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BdPdBaseVo> queryPdList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到相关入参！");
		List<BdPdBaseVo> list = mtlBdPdMapper.queryPdBaseList(map);
		return list;
	}
	
	/**
	 * 交易码:008007001005
	 * 2-2  查询 物品详细信息
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public MtlQryPdInfo queryPdDtInfo(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到相关入参！");
		MtlQryPdInfo pdInfo = queryPdAllInfo(map);
		System.out.println(JsonUtil.writeValueAsString(pdInfo));
		return pdInfo;
	}

	/**
	 * 根据参数查询物品详细信息
	 * @param map
	 * @return
	 */
	private MtlQryPdInfo queryPdAllInfo(Map<String, Object> map) {
		String flagNew = null ==  map.get("flagNew") 
				|| CommonUtils.isEmptyString(map.get("flagNew").toString()) ? "" : map.get("flagNew").toString();
		MtlQryPdInfo pdInfo = new MtlQryPdInfo();
		if("1".equals(flagNew))
		{
			//新增时，获取附加属性、可分配仓库
			List<BdPdAttVo> pdAttList = mtlBdPdMapper.queryBdPdDefNew(map);
			pdInfo.setPdAttList(pdAttList);
			
			List<BdPdStoreInfo> pdStoreList = mtlBdPdMapper.queryBdPdStoreNew(map);
			pdInfo.setPdStoreList(pdStoreList);
		}
		else
		{
			//修改时，获取物品信息、别名列表、附加属性、可分配仓库
			if(null == map.get("pkPd") || CommonUtils.isEmptyString(map.get("pkPd").toString()))
				throw new BusException("未获取待查询的物品主键！");
			List<BdPdBaseVo> list = mtlBdPdMapper.queryPdBaseList(map);
			if(null == list || list.size() < 1)
				throw new BusException("未获取到主键为【"+map.get("pkPd")+"】的物品信息！");
			pdInfo.setBdPd(list.get(0));
			
			List<BdPdAs> pdAsList = mtlBdPdMapper.queryBdPdAsList(map);
			if(null == pdAsList || pdAsList.size() < 1)
				throw new BusException("未获取到主键为【"+map.get("pkPd")+"】的物品别名信息！");
			pdInfo.setPdAsList(pdAsList);
			
			List<BdPdAttVo> pdAttList = mtlBdPdMapper.queryBdPdDef(map);
			pdInfo.setPdAttList(pdAttList);
			
			List<BdPdStoreInfo> pdStoreList = mtlBdPdMapper.queryBdPdStore(map);
			pdInfo.setPdStoreList(pdStoreList);
		}
		return pdInfo;
	}
	
	/**
	 * 交易码:008007001006
	 * 2-3 保存 物品详细信息
	 * @param param
	 * @param user
	 * @return
	 */
	public MtlQryPdInfo savePdDtInfo(String param, IUser user) {
		MtlSavePdInfo savePdInfo = JsonUtil.readValue(param, MtlSavePdInfo.class);
		MtlQryPdInfo allInfo = JsonUtil.readValue(param, MtlQryPdInfo.class);
		if(null == savePdInfo || null == allInfo)
			throw new BusException("未获取到待保存的物品信息！");
		if(null == savePdInfo.getBdPd() || null == allInfo.getBdPd())
			throw new BusException("未获取到待保存的物品基本信息！");
		
		boolean flagNew = CommonUtils.isEmptyString(savePdInfo.getBdPd().getPkPd()); 
		 //'修改'/'新增'标志(通过判断主键是否为空决定标志)
        rleCode = flagNew==true ? AddState : this.UpdateState;
		//1、保存物品
		BdPd pd = saveBdPd(savePdInfo.getBdPd(),allInfo.getBdPd(), flagNew);
		
		//2、保存物品别名
		savePdAs(savePdInfo.getPdAsList(), pd.getPkPd(), flagNew);
		
		//3.保存物资包装信息
		BdPdConvert pdcon= savePdConvert(savePdInfo.getPdConvertList(),savePdInfo.getBdPd());
		
		//3、保存物品附加属性
		savePdAtt(savePdInfo.getPdAttList(), pd.getPkPd(), flagNew);
		
		//4、保存仓库物品信息
		SavePdstore(savePdInfo.getPdStoreList(), pd.getPkPd(), flagNew,pdcon);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkPd", pd.getPkPd());
		map.put("dtPdtype",pd.getDtPdtype());
		MtlQryPdInfo pdInfo = queryPdAllInfo(map);
		//发送消息至平台
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPd",  pd.getPkPd());
        paramMap.put("pkOrg", UserContext.getUser().getPkOrg());

        paramMap.put("STATUS", rleCode);
        PlatFormSendUtils.sendBdMaterMsg(paramMap);
		return pdInfo;
	}
 
	/**
	 * 保存包装信息
	 * @param pdConvertList
	 * @param bdpd
	 * @return
	 */
	private BdPdConvert savePdConvert(List<BdPdConvert> pdConvertList, BdPd bdpd){
		List<BdPdConvert> insertList=new ArrayList<BdPdConvert>();
		BdPdConvert retCon=new BdPdConvert();
		if(pdConvertList==null || pdConvertList.size()<=0)
			throw new BusException("未获取物品包装数据，请核对！");
		DataBaseHelper.execute("delete from bd_pd_convert where pk_pd='"+bdpd.getPkPd()+"'");
		for(BdPdConvert bdPdConvert : pdConvertList){
			bdPdConvert.setPkPd(bdpd.getPkPd());
			ApplicationUtils.setDefaultValue(bdPdConvert, true);
			insertList.add(bdPdConvert);
			if("1".equals(bdPdConvert.getFlagIp())){
				retCon=bdPdConvert;
			}
		}
		if(insertList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdConvert.class), insertList);
		}
		return retCon;
	}
	
	/**
	 * 2-3.1 保存物品基本信息
	 * @param pd
	 * @param flagNew
	 * @return
	 */
	private BdPd saveBdPd(BdPd pd,BdPdBaseVo basePd, boolean flagNew){
		//1.1 校验 【编码】全局唯一
		String chkCode = "select count(1) from bd_pd where code=? and del_flag='0' ";
		if(!flagNew)
			chkCode += " and pk_pd != '"+pd.getPkPd()+"'";
		int cut_code = DataBaseHelper.queryForScalar(chkCode, Integer.class, pd.getCode());
		if(cut_code > 0)
			throw new BusException("编码【"+pd.getCode()+"】全局不唯一，请重新输入！");
		
		//1.2 校验 【物品名称+规格+生产厂家】全局唯一
		String chkName = "select count(1) from bd_pd where name=? and spec=? and pk_factory=? and del_flag='0' ";
		if(!flagNew)
			chkName += " and pk_pd != '"+pd.getPkPd()+"'";
		int cut_name = DataBaseHelper.queryForScalar(chkName, Integer.class, pd.getName(),pd.getSpec(),pd.getPkFactory());
		if(cut_name > 0)
			throw new BusException("名称【"+pd.getName()+"】+规格【"+pd.getSpec()+"】"
					+ "+生产厂家【"+basePd.getNameFactory().trim()+"】全局不唯一，请重新输入！");
		
		//1.3 校验 【条形码】全局唯一
		String chkBarCode = "select count(1) from bd_pd where barcode=? and del_flag='0'";
		if(!flagNew)
			chkBarCode += " and pk_pd != '"+pd.getPkPd()+"'";
		int cut_barcode = DataBaseHelper.queryForScalar(chkBarCode, Integer.class, pd.getBarcode());
		if(cut_barcode > 0)
			throw new BusException("条形码【"+pd.getBarcode()+"】全局不唯一，请重新输入！");
		//1.4 校验“在用”,已经存在“在用”记录的，不能去掉在用标记
		if(!flagNew && !EnumerateParameter.ONE.equals(pd.getFlagUse())
				&& DataBaseHelper.queryForScalar("select count(*) from pd_deptusing where PK_PD=?",Integer.class,pd.getPkPd())>0){
			throw new BusException("物品已经存在“在用记录”,不能去掉“在用”标志！");
		}
		//2 保存物品信息
		if(flagNew){
			pd.setPkPd(NHISUUID.getKeyId());
			DataBaseHelper.insertBean(pd);
		}
		else{
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(BdPd.class), pd);
		}
		return pd;
	}
	
	/**
	 * 2-3.2 保存物品别名信息
	 * a、将传入后台的数据，全部保存|插入
	 * b、删除掉不在前台传入的列表中的数据
	 * @param pdAsList
	 * @param pkPd
	 * @param flagNew
	 */
	private void savePdAs(List<BdPdAs> pdAsList, String pkPd ,boolean flagNew) {
		if(null == pdAsList || pdAsList.size() < 1)
			throw new BusException("至少维护一条物品别名信息！");
		String delStrBuf = "";
		//保存|插入相关数据
		for (BdPdAs pdAs: pdAsList) {
			if(CommonUtils.isEmptyString(pdAs.getPkPdas())){
				pdAs.setPkOrg("~");
				pdAs.setPkPd(pkPd);
				DataBaseHelper.insertBean(pdAs);
			}else
				DataBaseHelper.update(DataBaseHelper.getUpdateSql(BdPdAs.class), pdAs);	
			if(!flagNew)
				delStrBuf += "'" + pdAs.getPkPdas() + "',";
		}
		//删除前台删除的数据
		if(delStrBuf.length() > 0)
			DataBaseHelper.execute("delete from bd_pd_as where pk_pd= ? "
					+ "and pk_pdas not in ("+delStrBuf.substring(0, delStrBuf.length() -1) +")", pkPd);
	}

	/**
	 * 2-3.3保存物品附加属性
	 * @param attList
	 * @param pkPd
	 * @param flagNew
	 */
	private void savePdAtt(List<BdPdAtt> attList, String pkPd, boolean flagNew) {
		if(null == attList || attList.size() < 1) return;
		String delStrBuf = "";
		for (BdPdAtt pdAttVo : attList) {
			if(CommonUtils.isEmptyString(pdAttVo.getPkPdatt())){
				pdAttVo.setPkOrg("~");
				pdAttVo.setPkPd(pkPd);
				DataBaseHelper.insertBean(pdAttVo);
			}
			else
				DataBaseHelper.updateBeanByPk(pdAttVo,false);
			if(!flagNew)
				delStrBuf += "'"+pdAttVo.getPkPdatt()+"',";
		}
		if(delStrBuf.length() > 0)
			DataBaseHelper.execute("delete from bd_pd_att where pk_pd= ? "
					+ "and pk_pdatt not in ("+delStrBuf.substring(0, delStrBuf.length() -1) +")", pkPd);
	}

	/**
	 * 2-3.4.1根据物品主键、仓库主键，删除仓库物品信息
	 * @param pkPd
	 * @param pkPdStore
	 * @return
	 */
	private void delPdStore(String pkPd, String pkPdStore){
		//校验能否删除
		if(CommonUtils.isEmptyString(pkPd))
			throw new BusException("未获取待取消仓库分配的物品主键！");
		if(CommonUtils.isEmptyString(pkPdStore))
			throw new BusException("未获取待取消仓库分配的仓库主键！");
		BdStore store = DataBaseHelper.queryForBean("select * from bd_store where pk_store=? ", BdStore.class, pkPdStore);
		if(null == store)
			throw new BusException("待取消分配的仓库不存在！");
		
		//1 数据校验
		//1.1 盘点
		int count_store = DataBaseHelper.queryForScalar("select count(1) from pd_inventory inv "
				+ " inner join pd_inv_detail invdt on inv.pk_pdinv=invdt.pk_pdinv  and invdt.del_flag='0' "
				+ " where inv.pk_store=? and invdt.pk_pd=? and inv.del_flag='0' ", Integer.class, pkPdStore, pkPd);
		if(count_store > 0)
			throw new BusException("该物品在仓库【"+store.getName()+"】中已存在【盘点记录】，不可删除！");
		
		//1.2 结账
		int count_sup = DataBaseHelper.queryForScalar("select count(1) from pd_cc cc "
				+ " inner join pd_cc_detail ccdt on cc.pk_pdcc=ccdt.pk_pdcc and ccdt.del_flag='0' "
				+ " where cc.pk_store=? and ccdt.pk_pd=? and cc.del_flag='0' ", Integer.class, pkPdStore, pkPd);
		if(count_sup > 0)
			throw new BusException("该物品在仓库【"+store.getName()+"】中已存在【结账记录】，不可删除！");
		
		//1.3 计划
		int count_plan = DataBaseHelper.queryForScalar("select count(1) from pd_plan pla "
				+ "inner join pd_plan_detail pladt on pla.pk_pdplan=pladt.pk_pdplan and pladt.del_flag='0' "
				+ "where pla.pk_store=? and pladt.pk_pd=? and pla.del_flag='0' ", Integer.class, pkPdStore, pkPd);
		if(count_plan > 0)
			throw new BusException("该物品在仓库【"+store.getName()+"】中已存在【采购计划】，不可删除！");
		
		//1.5 出入库记录
		int count_st = DataBaseHelper.queryForScalar("select count(1) from pd_st st "
				+ "inner join pd_st_detail stdt on st.pk_pdst=stdt.pk_pdst and stdt.del_flag='0' "
				+ "where st.pk_store_st=? and stdt.pk_pd=? and st.del_flag='0' ", Integer.class, pkPdStore, pkPd);
		if(count_st > 0)
			throw new BusException("该物品在仓库【"+store.getName()+"】中已存在【出入库记录】，不可删除！");
		
		//1.6 库存
		int count_stock = DataBaseHelper.queryForScalar("select count(1) from pd_stock sto "
				+ "where sto.pk_store=? and sto.pk_pd=? and sto.del_flag='0' ", Integer.class, pkPdStore, pkPd);
		if(count_stock > 0)
			throw new BusException("该物品在仓库【"+store.getName()+"】中已存在【库存记录】，不可删除！");
		
		//2 删除仓库物品信息
		DataBaseHelper.execute("delete from bd_pd_store where pk_store=? and pk_pd=?",pkPdStore, pkPd);
	}

	/**
	 * 2-3.4 保存仓库物品信息
	 * 	 * a、修改保存前，先删除前台取消选中的相关记录
	 * 	 * b、保存剩余存在的记录
	 * @param StoreList
	 * @param pkPd
	 * @param flagNew
	 * @param pdCon
	 */
	private void SavePdstore(List<BdPdStore> StoreList, String pkPd, boolean flagNew,BdPdConvert pdCon) {
		
		//1、修改保存时，先删除仓库物品记录
		if(!flagNew){
			String qryPkSql = "select pk_store from bd_pd_store where pk_pd = ? and del_flag = '0'";
			if(null != StoreList && StoreList.size() > 0){
				String pkPdStoreStr = ""; 
				for (BdPdStore bdPdStoreInfo : StoreList) {
					if(!CommonUtils.isEmptyString(bdPdStoreInfo.getPkPdstore()))
						pkPdStoreStr += "'"+bdPdStoreInfo.getPkPdstore()+"',";
				}
				if(!CommonUtils.isEmptyString(pkPdStoreStr)){
					qryPkSql += "and pk_pdstore not in ("+pkPdStoreStr.substring(0,pkPdStoreStr.length()-1)+") ";
				}
			}
			List<String> pkPdStores = DataBaseHelper.getJdbcTemplate().queryForList(qryPkSql,String.class, pkPd);
			if(null != pkPdStores && pkPdStores.size() > 0){
				for (String str : pkPdStores) {
					delPdStore(pkPd, str);
				}
			}
		}
		
		if(null == StoreList || StoreList.size() < 1)	return;
			
		//2、保存相关记录
		for (BdPdStore pdStore : StoreList) {
			pdStore.setPkPd(pkPd);
			pdStore.setPkOrg("~                               ");
			if(CommonUtils.isEmptyString(pdStore.getPkPdstore())){
				pdStore.setPkUnit(pdCon.getPkUnit());
				pdStore.setPackSize((long)pdCon.getPackSize());
				pdStore.setPkPdconvert(pdCon.getPkPdconvert());
				ApplicationUtils.setDefaultValue(pdStore, true);
				DataBaseHelper.insertBean(pdStore);
			}
			else{
				ApplicationUtils.setDefaultValue(pdStore, false);
				DataBaseHelper.updateBeanByPk(pdStore,false);
			}
		}
	}
	
	/**
	 * 交易码:008007001007
	 * 2-4 删除 物品详细信息
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void delPdDtInfo(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到删除物品信息的相关入参！");
		if(null == map.get("pkPd") || CommonUtils.isEmptyString(map.get("pkPd").toString()))
			throw new BusException("未获取待删除的物品主键！");
		String pkPd = map.get("pkPd").toString();
		
		//1 数据校验
		//1.1 仓库物品
		int count_store = DataBaseHelper.queryForScalar("select count(1) "
				+ "from bd_pd_store where pk_pd=? and del_flag='0' ", Integer.class, pkPd);
		if(count_store > 0)
			throw new BusException("该物品已被【仓库物品】引用，不可删除！");
		
		//1.2 供应商协议
		int count_sup = DataBaseHelper.queryForScalar("select count(1) "
				+ "from bd_pd_supplyer where pk_pd=? and del_flag='0' ", Integer.class, pkPd);
		if(count_sup > 0)
			throw new BusException("该物品已被【供应商协议】引用，不可删除！");
		
		//1.3 计划
		int count_plan = DataBaseHelper.queryForScalar("select count(1) "
				+ "from pd_plan_detail where pk_pd=? and del_flag='0' ", Integer.class, pkPd);
		if(count_plan > 0)
			throw new BusException("该物品已存在【采购计划】，不可删除！");
		
		//1.4 采购
		int count_purchase = DataBaseHelper.queryForScalar("select count(1) "
				+ "from pd_purchase_dt where pk_pd=? and del_flag='0' ", Integer.class, pkPd);
		if(count_purchase > 0)
			throw new BusException("该物品已存在【 采购记录】，不可删除！");
		
		//1.5 出入库记录
		int count_st = DataBaseHelper.queryForScalar("select count(1) "
				+ "from pd_st_detail where pk_pd=? and del_flag='0' ", Integer.class, pkPd);
		if(count_st > 0)
			throw new BusException("该物品已存在【出入库记录】，不可删除！");
		
		//1.6 库存
		int count_stock = DataBaseHelper.queryForScalar("select count(1) "
				+ "from pd_stock where pk_pd=? and del_flag='0' ", Integer.class, pkPd);
		if(count_stock > 0)
			throw new BusException("该物品已存在【库存记录】，不可删除！");
		
		//2 删除物品相关信息
		//2.1 删除别名
		DataBaseHelper.execute("delete from bd_pd_as where pk_pd = ?", pkPd);
		//2.2 删除附加属性
		DataBaseHelper.execute("delete from bd_pd_att where pk_pd = ?", pkPd);
		//2.3 删除药品信息
		DataBaseHelper.execute("delete from bd_pd where pk_pd = ?", pkPd);
	}

	/**
	 * 交易码:008007001008
	 * 2-5 停用|恢复 物品状态
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void updatePdStatus(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到 停用|恢复物品状态的相关入参！");
		if(null == map.get("pkPd") || CommonUtils.isEmptyString(map.get("pkPd").toString()))
			throw new BusException("未获取待 停用|恢复 的物品主键！");
		String pkPd = map.get("pkPd").toString();
		
		if(null == map.get("status") || CommonUtils.isEmptyString(map.get("status").toString()))
			throw new BusException("未获取待操作的状态！");
		String status = map.get("status").toString();

		int cout = 0;
		if("0".equals(status))//停用
			cout = DataBaseHelper.execute("update bd_pd set flag_stop=1 where pk_pd = ? and flag_stop=0 ", pkPd);
		else if("1".equals(status))//恢复
			cout = DataBaseHelper.execute("update bd_pd set flag_stop=0 where pk_pd = ? and flag_stop=1 ", pkPd);
		
		if(cout != 1)
			throw new BusException("更新失败！");
	}

	/**
	 * 交易码:008007001009
	 * 2-6 校验是否可以取消仓库分配
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public boolean chkDelPdPdStore(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到取消仓库分配的相关入参！");
		if(null == map.get("pkPd") || CommonUtils.isEmptyString(map.get("pkPd").toString()))
			throw new BusException("未获取待取消仓库分配的物品主键！");
		String pkPd = map.get("pkPd").toString();
		if(null == map.get("pkPdStore") || CommonUtils.isEmptyString(map.get("pkPdStore").toString()))
		throw new BusException("未获取待取消仓库分配的仓库主键！");
		String pkPdStore = map.get("pkPdStore").toString();
		delPdStore(pkPd,pkPdStore);//校验是否可以取消仓库，如果可以则删除
		return true;
	}
	
	/**
	 * 交易码:00800700101
	 * 2-7 校验物品的库存
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void chkPdStock(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到 校验物品库存 的相关入参！");
		if(null == map.get("pkPd") || CommonUtils.isEmptyString(map.get("pkPd").toString()))
			throw new BusException("未获取待 校验物品库存  的物品主键！");
		String pkPd = map.get("pkPd").toString();
		BigDecimal count_stock = DataBaseHelper.queryForScalar("select count(quan_min) from pd_stock where pk_pd=? and del_flag = '0'", BigDecimal.class, pkPd);
		if( null != count_stock && count_stock.compareTo(BigDecimal.ZERO) > 0){
			throw new BusException("物品仓库库存 = "+ count_stock +" > 0！");
		}
		StringBuffer sqlStore=new StringBuffer();
		sqlStore.append("select store.name from bd_pd_store bps inner join bd_store store on store.pk_store=bps.pk_store ");
		sqlStore.append(" where bps.pk_pd=?");
		List<Map<String,Object>> resList=DataBaseHelper.queryForList(sqlStore.toString(),new Object[]{pkPd} );
		String resMsg="";
		if(resList.size()>0){
			for (Map<String, Object> resMap : resList) {
				if(CommonUtils.isEmptyString(resMsg)){
					resMsg=resMap.get("name").toString();
				}else{
					resMsg+=","+resMap.get("name").toString();
				}
			}
			throw new BusException("当前物品已被仓库引用，如要修改【单品属性】，请移除以下仓库所属物品:\n"+resMsg);	
		}
	}
	
	/**
	 * 008007001021
	 * 获取物品包装信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPdConvert(String param,IUser user){
		String pkPd=JsonUtil.getFieldValue(param, "pkPd");
		
		return mtlBdPdMapper.getPdConvert(pkPd);
	}
	
	/**
	 * 008007001022
	 * 获取物品分配信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPdStore(String param,IUser user){
		String pkPd=JsonUtil.getFieldValue(param, "pkPd");
		return mtlBdPdMapper.getPdStore(pkPd);
	}
}
