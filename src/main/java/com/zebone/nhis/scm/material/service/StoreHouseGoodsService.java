package com.zebone.nhis.scm.material.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.pub.BdPdStore;
import com.zebone.nhis.common.module.scm.pub.BdStore;
import com.zebone.nhis.scm.material.dao.MtlStoreHouseGoodsMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class StoreHouseGoodsService {
	
	@Autowired
	private MtlStoreHouseGoodsMapper stoHouseGoodsMapper;
	
	
	/**
	 * 交易号：008001001060
	 * 查询仓库物品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> quePdStores(String param, IUser user){
		
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String pkStore = ((User)user).getPkStore();
		paramMap.put("pkStore", pkStore);
		
		String pycode = "";
		if(paramMap.get("pycode")!=null) pycode = paramMap.get("pycode").toString().toUpperCase();
		
		paramMap.put("pycode",pycode);
		
		return this.stoHouseGoodsMapper.queMtlPdStores(paramMap);
	}
	
	
	/**
	 * 交易号：008001001061
	 * 保存物资仓库物品信息
	 * @param param
	 * @param user
	 */
	public void newPdStores(String param, IUser user){
		List<BdPdStore> pdlist = JsonUtil.readValue(param, new TypeReference<List<BdPdStore>>(){});
		if(pdlist!=null && pdlist.size() > 0){
			String pkStore = ((User)user).getPkStore();
			String pkDept = ((User)user).getPkDept();
			String pkOrg = ((User)user).getPkOrg();
			String pkUser = ((User)user).getPkEmp();
			Date opeDate = new Date();
			
			
			
			for(BdPdStore pdstore : pdlist){
				Map<String,Object> conMap=qryConvert(pdstore.getPkPd());
				pdstore.setPkDept(pkDept);
				pdstore.setPkStore(pkStore);
				pdstore.setFlagStop("0");
				pdstore.setPkPdstore(NHISUUID.getKeyId());
				pdstore.setDelFlag("0");
				pdstore.setPkOrg(pkOrg);
				pdstore.setCreator(pkUser);
				pdstore.setCreateTime(opeDate);
				pdstore.setTs(opeDate);
				pdstore.setPkPdconvert(conMap.get("pkPdconvert").toString());
				pdstore.setPkUnit(conMap.get("pkUnit").toString());
				pdstore.setPackSize(Long.valueOf(conMap.get("packSize").toString()));		//默认为1
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdStore.class), pdlist);
		}
	}
	
	private Map<String, Object> qryConvert(String pkPd) {
		Map<String, Object>  convertMap = DataBaseHelper.queryForMap( "select * from bd_pd_convert where del_flag='0'  and pk_pd = ?", pkPd);
		if (convertMap == null) {
			Map<String, Object> pd = DataBaseHelper.queryForMap("select name from bd_pd where pk_pd = ?", pkPd);
			throw new BusException("物品" + pd.get("name").toString()+ "没有设置包装，请重新保存物品数据！");
		}
		return convertMap;

	}
	
	/**
	 * 交易号：008001001062
	 * 查询仓库新增的物品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queNewPds(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkStore = ((User)user).getPkStore();
		paramMap.put("pkStore", pkStore);
		
		String spcode = "";
		if(paramMap.get("spcode")!=null) spcode = paramMap.get("spcode").toString().toUpperCase();
		
		paramMap.put("spcode",spcode);
		return stoHouseGoodsMapper.queNewMtlBdPds(paramMap);
	}
	

	/**
	 * 交易号：008001001063
	 * 保存仓库物品信息和处方包装信息
	 * @param param
	 * @param user
	 */
	public void saveBdPdStoreVo(String param, IUser user){
		BdPdStore pdStore = JsonUtil.readValue(param, BdPdStore.class);
		
		//pdStore.setPackSize(Long.valueOf(1));	//包装量默认为1
		
		//更新物品表
		DataBaseHelper.updateBeanByPk(pdStore,false);	
	}
	
	
}
