package com.zebone.nhis.scm.pub.service;

import com.zebone.nhis.base.pub.vo.CgdivItemVo;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyDisease;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyDiseaseOrd;
import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.scm.pub.*;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.scm.pub.dao.ScmPubMapper;
import com.zebone.nhis.scm.pub.support.Constants;
import com.zebone.nhis.scm.pub.vo.*;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 供应链管理--公共服务
 * @author Xulj
 *
 */
@Service
public class ScmPubService {
	
	private static Logger log = org.slf4j.LoggerFactory.getLogger(ScmPubService.class);

	@Autowired
	private ScmPubMapper scmPubMapper;
	
	/**
	 * 添加状态
	 **/
	public static final String AddState = "_ADD";

	/**
	 * 更新状态
	 */
	public static final String UpdateState = "_UPDATE";

	/**
	 * 删除状态
	 */
	public static final String DelState = "_DELETE";

	/**
	 * 保存仓库信息
	 * @param param
	 * @param user
	 */
	public void saveBdStoreList(String param, IUser user){
		List<BdStore> storelist = JsonUtil.readValue(param, new TypeReference<List<BdStore>>() {});
		if(storelist != null && storelist.size() != 0){
			/**校验---1.校验前台所传的list的每一条编码，名称的唯一性*/
			Map<String, String> codemap = new HashMap<String, String>();
			Map<String, String> namemap = new HashMap<String, String>();
			int len = storelist.size();
			for(int i=0; i<len; i++){
				String code = storelist.get(i).getCode();
				String name = storelist.get(i).getName();
				if(codemap.containsKey(code)){
					throw new BusException("仓库信息编码重复！");
				}
				if(namemap.containsKey(name)){
					throw new BusException("仓库信息名称重复！");
				}
				codemap.put(code, storelist.get(i).getPkStore());
				namemap.put(name, storelist.get(i).getPkStore());
			}
			/**查询数据库中所有*/
			String pkOrg = ((User) user).getPkOrg();
			List<BdStore> allist = this.scmPubMapper.findAllStores(pkOrg);
			
			/**校验---2.storelist与数据库比较校验编码，名称的重复性*/
			for(BdStore dataStore : allist){
				if(codemap.containsKey(dataStore.getCode())){
					String pkStore = codemap.get(dataStore.getCode());
					if(pkStore == null){
						throw new BusException("仓库信息编码在数据库中已存在！");
					}else{
						if(!dataStore.getPkStore().equals(pkStore)){
							throw new BusException("仓库信息编码在数据库中已存在！");
						}
					}
				}
				if(namemap.containsKey(dataStore.getName())){
					String pkStore = namemap.get(dataStore.getName());
					if(pkStore == null){
						throw new BusException("仓库信息名称在数据库中已存在！");
					}else{
						if(!dataStore.getPkStore().equals(pkStore)){
							throw new BusException("仓库信息名称在数据库中已存在！");
						}
					}
				}
			}
			
			/**新增或更新到数据库*/
			for(BdStore store : storelist){
				if(store.getPkStore() == null){
					DataBaseHelper.insertBean(store);
				}else{
					DataBaseHelper.updateBeanByPk(store,false);
				}
			}
		}
	}
	
	/**
	 * 交易号：008001001048
	 * 保存仓库信息(单条信息保存)
	 * @param param
	 * @param user
	 */
	public void saveBdStore(String param, IUser user){
		BdStore saveInfo = JsonUtil.readValue(param,BdStore.class);
		String pkOrg = UserContext.getUser().getPkOrg();
		Map<String,Object> vdMap = new HashMap<>();
		
		//判断主键是否为空来确定是新增还是修改
		if(CommonUtils.isEmptyString(saveInfo.getPkStore())){
			vdMap = DataBaseHelper.queryForMap("select code,name from bd_store where del_flag='0' and pk_org = ? and (code=? or name=?)"
					,new Object[]{pkOrg,saveInfo.getCode(),saveInfo.getName()});
		}else{
			vdMap = DataBaseHelper.queryForMap("select code,name from bd_store where del_flag='0' and pk_org = ? and pk_store!=? and (code=? or name=?)"
					,new Object[]{pkOrg,saveInfo.getPkStore(),saveInfo.getCode(),saveInfo.getName()});
		}
		
		if(vdMap!=null && vdMap.size()>0){
			if(vdMap.get("code")!=null && vdMap.get("code").toString().equals(saveInfo.getCode())){
				throw new BusException("仓库信息编码在数据库中已存在！");
			} else if(vdMap.get("name")!=null && vdMap.get("name").toString().equals(saveInfo.getName())){
				throw new BusException("仓库信息名称在数据库中已存在！");
			}
		}
		
		/**新增或更新到数据库*/
		if(CommonUtils.isEmptyString(saveInfo.getPkStore())){
			saveInfo.setPkOrg(pkOrg); 	//当前机构
			DataBaseHelper.insertBean(saveInfo);
		}else{
			//当部门变更时判断仓库、库存引用
			String pkDept = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from bd_store where pk_store=?", saveInfo.getPkStore()), "pkDept");
			if(!StringUtils.equals(pkDept, saveInfo.getPkDept())) {
				Integer count = DataBaseHelper.queryForScalar("select count(1) from bd_pd_store pdst where pdst.pk_store=? and pdst.pk_dept=?", Integer.class,new Object[]{saveInfo.getPkStore(), pkDept});
				if(count !=null && count >0){
					throw new BusException("仓库中已经存在该部门的物品不能变更所属部门！");
				}
				count = DataBaseHelper.queryForScalar("select count(1) from pd_stock sto where sto.pk_store=? and sto.pk_dept=?", Integer.class,new Object[]{saveInfo.getPkStore(), pkDept});
				if(count !=null && count >0){
					throw new BusException("库存记录存在该部门的物品不能变更所属部门！");
				}
			}
			DataBaseHelper.updateBeanByPk(saveInfo,false);
		}
	}
	
	/**
	 * 删除仓库信息
	 * @param param
	 * @param user
	 */
	public void delBdStore(String param, IUser user){
		String pkStore = JsonUtil.getFieldValue(param, "pkStore");
		
		String storeSql="select count(1) from bd_pd_store where del_flag = '0' and pk_store = ?";
		String planSql="select count(1) from pd_plan where del_flag = '0' and pk_store = ?";
		String stSql="select count(1) from pd_st where del_flag = '0' and pk_store_st = ?";
		String stockSql="select count(1) from pd_stock where del_flag = '0' and pk_store = ?";
		
		if(Application.isSqlServer()){
			storeSql=storeSql.replace("count(1)", "top 1 count(1)");
			planSql=planSql.replace("count(1)", "top 1 count(1)");
			stSql=stSql.replace("count(1)", "top 1 count(1)");
			stockSql=stockSql.replace("count(1)", "top 1 count(1)");
		}else{
			storeSql+=" and rownum=1";
			planSql+=" and rownum=1";
			stSql+=" and rownum=1";
			stockSql+=" and rownum=1";
		}
		//校验仓库不能被业务表引用
		//--仓库物品
		int count_bdpdstore = DataBaseHelper.queryForScalar(storeSql, Integer.class, pkStore);
		if(count_bdpdstore != 0){
			throw new BusException("该仓库信息被仓库物品表引用！");
		}
		//--计划
		int count_pdplan = DataBaseHelper.queryForScalar(planSql, Integer.class, pkStore);
		if(count_pdplan != 0){
			throw new BusException("该仓库信息被计划表引用！");
		}
		//--出入库
		int count_pdst = DataBaseHelper.queryForScalar(stSql, Integer.class, pkStore);
		if(count_pdst != 0){
			throw new BusException("该仓库信息被出入库表引用！");
		}
		//--库存
		int count_pdstock = DataBaseHelper.queryForScalar(stockSql, Integer.class, pkStore);
		if(count_pdstock != 0){
			throw new BusException("该仓库信息被物品库存表引用！");
		}
		DataBaseHelper.execute("delete from bd_store where pk_store=?", new Object[]{pkStore});
	}
	
	/**
	 * 交易号：008001001037<br>
	 * 修改仓库所属部门数据校验
	 * @param param
	 * {
     *     "pkStore":"仓库主键",
     *     "pkDept":"原部门主键"
     * }
	 * @param user
	 * @return
	 */
	public boolean ifCanBeModified(String param, IUser user){
		BdStore store = JsonUtil.readValue(param, BdStore.class);
		//--仓库物品
		int count_pdstore = DataBaseHelper.queryForScalar("select count(1) from bd_pd_store pdst where pdst.del_flag='0' and pdst.pk_store=? and pdst.pk_dept=?", Integer.class, store.getPkStore(), store.getPkDept());
		//--库存
		int count_stock = DataBaseHelper.queryForScalar("select count(1) from pd_stock sto where sto.del_flag='0' and sto.pk_store=? and sto.pk_dept=?", Integer.class, store.getPkStore(), store.getPkDept());
		if(count_pdstore==0 && count_stock==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 保存供应商信息
	 * @param param
	 * @param user
	 * @return
	 */
	public BdSupplyer saveBdSupplyer(String param, IUser user){
		BdSupplyer supplyer = JsonUtil.readValue(param, BdSupplyer.class);
		
		String pk=""; //存储新增后的主键传入调用平台接口
		
		//校验：编码和名称全局唯一
		if(supplyer.getPkSupplyer() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_supplyer "
					+ "where del_flag = '0' and code = ?", Integer.class, supplyer.getCode());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_supplyer "
					+ "where del_flag = '0' and name = ?", Integer.class, supplyer.getName());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(supplyer);
				pk = supplyer.getPkSupplyer();
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("【供应商信息】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("【供应商信息】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("【供应商信息】编码和名称都重复！");
				}
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_supplyer "
					+ "where del_flag = '0' and code = ? and pk_supplyer != ?", Integer.class, supplyer.getCode(),supplyer.getPkSupplyer());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_supplyer "
					+ "where del_flag = '0' and name = ? and pk_supplyer != ?", Integer.class, supplyer.getName(),supplyer.getPkSupplyer());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.update(DataBaseHelper.getUpdateSql(BdSupplyer.class), supplyer);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("【供应商信息】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("【供应商信息】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("【供应商信息】编码和名称都重复！");
				}
			}
		}
		
		return supplyer;
	}
	
	/**
	 * 删除供应商信息
	 * @param param
	 * @param user
	 */
	public void delBdSupplyer(String param, IUser user){
		
		String pkSupplyer = JsonUtil.getFieldValue(param, "pkSupplyer");
		
		String supSql="select count(1) from bd_pd_supplyer where del_flag = '0' and pk_supplyer = ?";
		String planSql="select count(1) from pd_plan_detail where del_flag = '0' and pk_supplyer = ?";
		String purSql="select count(1) from pd_purchase where del_flag = '0' and pk_supplyer = ?";
		String pdstSql="select count(1) from pd_st where del_flag = '0' and pk_supplyer = ?";
		if(Application.isSqlServer()){
			supSql=supSql.replace("count(1)", "top 1 count(1)");
			planSql=planSql.replace("count(1)", "top 1 count(1)");
			purSql=purSql.replace("count(1)", "top 1 count(1)");
			pdstSql=pdstSql.replace("count(1)", "top 1 count(1)");
		}else{
			supSql+=" and rownum=1";
			planSql+=" and rownum=1";
			purSql+=" and rownum=1";
			pdstSql+=" and rownum=1";
		}
		//校验供应商不能被业务表引用
		//--供应商协议
		int count_bdpdsupplyer = DataBaseHelper.queryForScalar(supSql, Integer.class, pkSupplyer);
		if(count_bdpdsupplyer != 0){
			throw new BusException("该供应商信息被供应商协议表引用！");
		}
		//--采购计划
		int count_plandetail = DataBaseHelper.queryForScalar(planSql, Integer.class, pkSupplyer);
		if(count_plandetail != 0){
			throw new BusException("该供应商信息被采购计划表引用！");
		}
		//--采购订单
		int count_pdpurchase = DataBaseHelper.queryForScalar(purSql, Integer.class, pkSupplyer);
		if(count_pdpurchase != 0){
			throw new BusException("该供应商信息被采购订单表引用！");
		}
		//--采购入库
		int count_pdst = DataBaseHelper.queryForScalar(pdstSql, Integer.class, pkSupplyer);
		if(count_pdst != 0){
			throw new BusException("该供应商信息被出入库表引用！");
		}
		DataBaseHelper.execute("delete from bd_supplyer where pk_supplyer = ?", new Object[]{pkSupplyer});
		
		
	}
	
	/**
	 * 保存生产厂家信息
	 * @param param
	 * @param user
	 */
	public void saveBdFactoryList(String param, IUser user){
		List<BdFactory> factorylist = JsonUtil.readValue(param, new TypeReference<List<BdFactory>>() {});
		String jg = "~                               ";//生产厂家的机构都是全局的
		if(factorylist != null && factorylist.size() != 0){
			/**校验---1.校验前台所传的list的每一条编码和名称的唯一性*/
			Map<String, String> codemap = new HashMap<String, String>();
			Map<String, String> namemap = new HashMap<String, String>();
			int len = factorylist.size();
			for(int i=0;i<len;i++){
				String code = factorylist.get(i).getCode();
				String name = factorylist.get(i).getName();
				if(codemap.containsKey(code)){
					throw new BusException("生产厂家信息编码重复！");
				}
				if(namemap.containsKey(name)){
					throw new BusException("生产厂家信息名称重复！");
				}
				codemap.put(code, factorylist.get(i).getPkFactory());
				namemap.put(name, factorylist.get(i).getPkFactory());
			}
			
			/**查询数据库中所有*/
			//校验全局编码、名称唯一
			List<BdFactory> allist = this.scmPubMapper.findAllFactories();
			
			/**校验---2.factorylist与数据库比较校验编码和名称的重复性*/
			for(BdFactory datafactory : allist){
				if(codemap.containsKey(datafactory.getCode())){
					String pkFactory = codemap.get(datafactory.getCode());
					if(pkFactory == null){
						throw new BusException("生产厂家信息编码在数据库中已存在！");
					}else{
						if(!datafactory.getPkFactory().equals(pkFactory)){
							throw new BusException("生产厂家信息编码在数据库中已存在！");
						}
					}
				}
				if(namemap.containsKey(datafactory.getName())){
					String pkFactory = namemap.get(datafactory.getName());
					if(pkFactory == null){
						throw new BusException("生产厂家信息名称在数据库中已存在！");
					}else{
						if(!datafactory.getPkFactory().equals(pkFactory)){
							throw new BusException("生产厂家信息名称在数据库中已存在！");
						}
					}
				}
			}
			
			List<String> insertPk=new ArrayList<String>();//用于存储新增后的主键，调用平台时传入
			
			/**新增或更新到数据库*/
			for(BdFactory factory : factorylist){
				if(factory.getPkFactory() == null){
					factory.setPkOrg(jg);
					DataBaseHelper.insertBean(factory);
					
					insertPk.add(factory.getPkFactory());
				}else{
					//DataBaseHelper.updateBeanByPk(factory,false);
					DataBaseHelper.update(DataBaseHelper.getUpdateSql(BdFactory.class), factory);
				}
			}
		}
	}

	/**
	 * 保存生产厂家信息(单条信息保存)
	 * @param param
	 * @param user
	 */
	public BdFactory saveBdFactory(String param,IUser user){
		BdFactory saveInfo = JsonUtil.readValue(param, BdFactory.class);
		Map<String,Object> validateMap = new HashMap<>();
		//判断主键是否为空来确定是新增还是修改
		if(CommonUtils.isEmptyString(saveInfo.getPkFactory())){
			/**效验编码、名称是否重复*/
			validateMap = DataBaseHelper.queryForMap("select code,name from bd_factory where del_flag = '0'  and (code = ? or name = ?)", 
					new Object[]{saveInfo.getCode(),saveInfo.getName()});
		} else {
			/**效验编码、名称是否重复*/
			validateMap = DataBaseHelper.queryForMap("select code,name from bd_factory where del_flag = '0' and pk_factory != ? and (code = ? or name = ?)", 
					new Object[]{saveInfo.getPkFactory(),saveInfo.getCode(),saveInfo.getName()});
		}
		
		if(validateMap!=null && validateMap.size()>0){
			if(validateMap.get("code")!=null && validateMap.get("code").toString().equals(saveInfo.getCode())){
				throw new BusException("生产厂家信息编码在数据库中已存在！");
			} else if(validateMap.get("name")!=null && validateMap.get("name").toString().equals(saveInfo.getName())){
				throw new BusException("生产厂家信息名称在数据库中已存在！");
			}
		}
		
		List<String> insertPk=new ArrayList<String>();//用于存储新增后的主键，调用平台时传入
		
		/**新增或更新到数据库*/
		if(CommonUtils.isEmptyString(saveInfo.getPkFactory())){
			saveInfo.setPkOrg("~                               ");//生产厂家的机构都是全局的"
			DataBaseHelper.insertBean(saveInfo);
			insertPk.add(saveInfo.getPkFactory());
		}else{
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(BdFactory.class), saveInfo);
		}
		
		return saveInfo;
	}
	
	/**
	 * 停用|恢复生成厂家
	 * @param param
	 * @param user
	 */
	public void stopOrRtnFactory(String param,IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null)
			throw new BusException("未获取到停用或恢复生产厂家的入参!");
		if(null == paramMap.get("pkFactory") 
				|| CommonUtils.isEmptyString(paramMap.get("pkFactory").toString()))
			throw new BusException("未获取到待停用或恢复生产厂家的主键!");
		String pkFactory = paramMap.get("pkFactory").toString();
		if(null == paramMap.get("flagStop") 
				|| CommonUtils.isEmptyString(paramMap.get("flagStop").toString()))
			throw new BusException("未获取到待操作的状态!");
		String flagStop = paramMap.get("flagStop").toString();//处理方式 1停用，0恢复
		if("1".equals(flagStop)){//停用
			DataBaseHelper.update("update bd_factory set flag_stop='1' where del_flag = '0' and flag_stop='0' and pk_factory = ?", new Object[]{pkFactory});
		}else if("0".equals(flagStop)){//恢复
			DataBaseHelper.update("update bd_factory set flag_stop='0' where del_flag = '0' and flag_stop='1' and pk_factory = ?", new Object[]{pkFactory});
		}
	}
	
	/**
	 * 删除生产厂家信息
	 * @param param
	 * @param user
	 */
	public void delBdFactory(String param, IUser user){
		String pkFactory = JsonUtil.getFieldValue(param, "pkFactory");
		//校验是否被药品字典引用
		int count = DataBaseHelper.queryForScalar("select count(1) from bd_pd where del_flag = '0' and pk_factory = ?", Integer.class, pkFactory);
		if(count == 0){
			//DataBaseHelper.update("update bd_factory set del_flag = '1' where pk_factory = ?", new Object[]{pkFactory});
			DataBaseHelper.execute("delete from bd_factory where pk_factory = ?", new Object[]{pkFactory});
		}else{
			throw new BusException("该生产厂家信息被药品字典引用!");
		}
	}

	/**
	 * 保存物品附加属性字典信息
	 * @param param
	 * @param user
	 */
	public void saveAttDefineList(String param, IUser user){
		List<BdPdAttDefine> attDefinelist = JsonUtil.readValue(param, new TypeReference<List<BdPdAttDefine>>() {});
		if(attDefinelist != null && attDefinelist.size() > 0){
			/**校验---1.校验前台所传的list的每一条编码和名称的唯一性*/
			Map<String, String> codemap = new HashMap<String, String>();
			Map<String, String> namemap = new HashMap<String, String>();
			int len = attDefinelist.size();
			for(int i = 0; i<len; i++){
				String code = attDefinelist.get(i).getCodeAtt();
				String name = attDefinelist.get(i).getNameAtt();
				if(codemap.containsKey(code)){
					throw new BusException("物品附加属性字典信息编码重复！");
				}
				if(namemap.containsKey(name)){
					throw new BusException("物品附加属性字典信息名称重复！");
				}
				codemap.put(code, attDefinelist.get(i).getPkPdattdef());
				namemap.put(name, attDefinelist.get(i).getPkPdattdef());
				
			}
			/**查询数据库中所有*/
			//校验：编码和名称在当前物品类型下唯一
			List<BdPdAttDefine> allist = this.scmPubMapper.findAllAttDefines(attDefinelist.get(0).getEuPdtype());
			
			/**校验---2.attDefinelist与数据库比较校验编码和名称的重复性*/
			for(BdPdAttDefine dataattDefine : allist){
				if(codemap.containsKey(dataattDefine.getCodeAtt())){
					String pkPdattdef = codemap.get(dataattDefine.getCodeAtt());
					if(pkPdattdef == null){
						throw new BusException("物品附加属性字典信息编码在数据库中已存在！");
					}else{
						if(!dataattDefine.getPkPdattdef().equals(pkPdattdef)){
							throw new BusException("物品附加属性字典信息编码在数据库中已存在！");
						}
					}
				}
				if(namemap.containsKey(dataattDefine.getNameAtt())){
					String pkPdattdef = namemap.get(dataattDefine.getNameAtt());
					if(pkPdattdef == null){
						throw new BusException("物品附加属性字典信息名称在数据库中已存在！");
					}else{
						if(!dataattDefine.getPkPdattdef().equals(pkPdattdef)){
							throw new BusException("物品附加属性字典信息名称在数据库中已存在！");
						}
					}
				}
			}
			
			
			/**新增或更新到数据库*/
			for(BdPdAttDefine attDefine : attDefinelist){
				if(attDefine.getPkPdattdef() == null){
					DataBaseHelper.insertBean(attDefine);
				}else{
					DataBaseHelper.updateBeanByPk(attDefine,false);
				}
			}
		}
	}
	
	/**
	 * 删除物品附加属性字典信息
	 * @param param
	 * @param user
	 */
	public void delAttDefine(String param, IUser user){
		String pkPdattdef = JsonUtil.getFieldValue(param, "pkPdattdef");
		
		DataBaseHelper.execute("delete from bd_pd_att where pk_pdattdef=?", new Object[]{pkPdattdef});
		DataBaseHelper.execute("delete from bd_pd_att_define where pk_pdattdef=?", new Object[]{pkPdattdef});
	}
	
	/**
	 * 编辑页面初始化--附加属性
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> initAttDefines(String param, IUser user){
		/**接收参数*/
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String editType = paramMap.get("editType").toString();
		String pkPd = null;
		if(paramMap.get("pkPd") != null){
			pkPd = paramMap.get("pkPd").toString();
		}
		/**设置返回参数*/
		List<Map<String, Object>> initedAttDefines = null;
		/**逻辑处理*/
		if("1".equals(editType)){//新增
			initedAttDefines = DataBaseHelper.queryForList("select null as pk_pdatt,"
					+ "def.pk_pdattdef,"
					+ "def.code_att,"
					+ "def.name_att,"
					+ "def.val_def as valAtt,"
					+ "def.desc_val "
					+ "from bd_pd_att_define def where def.del_flag = '0' and def.eu_pdtype = '0'");
		}else if("2".equals(editType)){//修改
			initedAttDefines = DataBaseHelper.queryForList("select null as pk_pdatt,"
					+ "def.pk_pdattdef,"
					+ "def.code_att,"
					+ "def.name_att,"
					+ "def.val_def as valAtt,"
					+ "def.desc_val "
					+ "from bd_pd_att_define def where def.del_flag = '0' and def.eu_pdtype = '0' and not exists (select 1 from bd_pd_att att where att.pk_pdattdef=def.pk_pdattdef and att.del_flag='0' and att.pk_pd=?) "
					+ "union all "
					+ "select att.pk_pdatt,"
					+ "att.pk_pdattdef,"
					+ "def.code_att,"
					+ "def.name_att,"
					+ "att.val_att as val,"
					+ "def.desc_val "
					+ "from bd_pd_att att inner join bd_pd_att_define def on att.pk_pdattdef=def.pk_pdattdef where att.del_flag = '0' and att.pk_pd = ?",pkPd, pkPd);
		}
		
		return initedAttDefines;
	}
	
	/**
	 * 查询药品适应症信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getPdIndInfo(String param, IUser user){
		/**接收参数*/
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String editType = paramMap.get("editType").toString();
		String pkPd = null;
		if(paramMap.get("pkPd") != null){
			pkPd = paramMap.get("pkPd").toString();
		}
		/**设置返回参数*/
		List<Map<String, Object>> pdIndInfo = null;
		/**逻辑处理*/
		if("1".equals(editType)){
			//新增
		}else if("2".equals(editType)){//修改
			pdIndInfo = DataBaseHelper.queryForList("select ind.*,indtype.NAME_TYPE"
					+ " from bd_pd_ind ind"
                    + " inner join bd_indtype indtype on ind.CODE_INDTYPE = indtype.CODE_TYPE"
                    +" inner join bd_pd_indpd indpd  on ind.PK_PDIND = indpd.PK_PDIND "
                    +" where ind.DEL_FLAG='0' and indpd.DEL_FLAG='0' "
                    + " and indpd.PK_PD = ?",pkPd);
		}
		
		return pdIndInfo;
	}
	
	/**
	 * 编辑页面初始化--分配
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> initPdStores(String param, IUser user){
		/**接收参数*/
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//String editType = paramMap.get("editType").toString();
		String pkPd = null;
		if(paramMap.get("pkPd") != null){
			pkPd = paramMap.get("pkPd").toString();
		}
		/**设置返回参数*/
		List<Map<String, Object>> initPdStores = null;
		
		initPdStores=scmPubMapper.qryPdStoreByPd(paramMap);
		/**
		 * 分配页签中，已被勾选的仓库代表该药品被分配到了该仓库，当取消勾选时，实际做的是删除该仓库物品
		 * 在删除仓库物品前需要校验是否被业务引用，参考：	删除仓库物品信息接口方法delPdStore
		 * 解决方法：
		 * 在初始化时，检查每一条被勾选的分配，如果被业务引用了的是不允许取消勾选的
		 * 增加返回字段：不允许取消勾选
		 */
		if(initPdStores != null && initPdStores.size() != 0){
			for(Map<String, Object> ipdStore : initPdStores){
				//被勾选标志,只考虑被勾选的
				if(ipdStore.get("flag")!=null && "1".equals(ipdStore.get("flag").toString())){
					String pkStore = ipdStore.get("pkStore").toString();
					int citedType = ifBeCited(pkPd, pkStore);
					if(0 != citedType){                              //说明仓库物品被业务引用
						ipdStore.put("checked", "1");                //新增字段：不允许取消勾选
					}
				}
			}
		}
		return initPdStores;
	}
	
	/**
	 * 停用和恢复药品信息
	 * @param param
	 * @param user
	 */
	public void stopOrRecoverBdpd(String param, IUser user){
		/**接收参数*/
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String pkPd = paramMap.get("pkPd").toString();
		String euTreat = paramMap.get("euTreat").toString();//处理方式 1停用，0恢复
		Object msg=paramMap.get("message");
		String message=msg!=null?msg.toString():"";//停用原因
		BdPd bdPd = DataBaseHelper.queryForBean("select * from bd_pd where pk_pd = ?",BdPd.class,pkPd);
		if("1".equals(euTreat)){//停用
			DataBaseHelper.update("update bd_pd  set flag_stop='1',remark=? where del_flag = '0' and flag_stop='0' and pk_pd = ?", new Object[]{message,pkPd});
		}else if("0".equals(euTreat)){//恢复
			DataBaseHelper.update("update bd_pd  set flag_stop='0',remark=? where del_flag = '0' and flag_stop='1' and pk_pd = ?", new Object[]{message,pkPd});
		}
		
		//发送消息至平台
		Map<String,Object> msgParam = new HashMap<String,Object>();
		msgParam.put("pd", JsonUtil.readValue(param, BdPd.class));
		msgParam.put("STATUS", this.UpdateState);
		msgParam.put("pdOld",bdPd);
		msgParam.put("pkPd", pkPd);
		PlatFormSendUtils.sendBdPdMsg(msgParam);
		msgParam = null;
	}
	
	/**
	 * 查询医保上传编码的最大值
	 * @param param
	 * @param user
	 */
	public String qryCodeHpFromPd(String param, IUser user) {
		String codeHp;
		codeHp = scmPubMapper.qryCodeHpFromPd();
		if(!CommonUtils.isEmptyString(codeHp)){
			int number = Integer.parseInt(codeHp.substring(codeHp.length()-1));
			number = number+1;
			codeHp = codeHp.substring(0, codeHp.length() -1)+number;
		}
		return codeHp;
	}
	
	/**
	 * 保存药品信息
	 * 1、药品信息保存；2、包装信息保存；3、药品别名保存
	 * 4、属性保存；5、分配保存
	 * @param param
	 * @param user
	 */
	public BdPd saveBdPdAndAll(String param, IUser user){
		
		PdAndAllParam pdAndAll = JsonUtil.readValue(param, PdAndAllParam.class);
		BdPd pd = pdAndAll.getPd();
		List<BdPdConvert> pdConvertList = pdAndAll.getPdConvertList();
		List<BdPdAs> pdAsList = pdAndAll.getPdAsList();
		List<BdPdAtt> pdAttList = pdAndAll.getPdAttList();
		List<BdPdStore> pdStoreList = pdAndAll.getPdStoreList();
		List<CgdivItemVo> pdCgdivList = pdAndAll.getItemCgDivs(); //记费策略
		List<BdItemHp> pdItemHpList=pdAndAll.getPdItemHpList();//医保类型
		List<InsGzgyDiseaseOrd> insGzgyDiseaseOrdList = pdAndAll.getInsGzgyDiseaseOrdList();//门慢和门特
		List<BdPdOutcode> outcodeList=pdAndAll.getOutCodeList();//平台编码

		boolean ifNewPd = true;//是否新增药品标志

		String pk=null;//用于存储新增后的主键，调平台时传入
		BdPd bdPd = null;
		/**
		 * 药品信息的保存
		 */
		if(pd.getPkPd() == null){
			String codeSql="select count(1) from bd_pd where del_flag='0' and code = ?";
			String barCodeSql="select count(1) from bd_pd where del_flag='0' and barcode = ?";
			String nameSpec="select count(1) from bd_pd where del_flag='0' and name = ? and spec = ? and pk_factory = ? and Price = ?";
			//String codeHpSql="select count(1) from bd_pd where del_flag='0' and  code_hp=?";
			if(Application.isSqlServer()){
				codeSql=codeSql.replace("count(1)", "top 1 count(1)");
				barCodeSql=barCodeSql.replace("count(1)", "top 1 count(1)");
				nameSpec=nameSpec.replace("count(1)", "top 1 count(1)");
				//codeHpSql=codeHpSql.replace("count(1)", "top 1 count(1)");
			}else{
				codeSql+=" and rownum=1";
				barCodeSql+=" and rownum=1";
				//codeHpSql+=" and rownum=1";
				nameSpec+=" and rownum=1";
			}
			
			int code_count = DataBaseHelper.queryForScalar(codeSql, Integer.class, pd.getCode());
			if(code_count!=0){
				throw new BusException("药品编码全局不唯一！");
			}
			int barCode_count = DataBaseHelper.queryForScalar(barCodeSql, Integer.class, pd.getBarcode());
			if(barCode_count!=0){
				throw new BusException("药品条形码全局不唯一！");
			}
			int nameSpec_count = DataBaseHelper.queryForScalar(nameSpec, Integer.class, pd.getName(), pd.getSpec(),pd.getPkFactory(),pd.getPrice());
			if(nameSpec_count!=0){
				throw new BusException("药品名称+规格+生产厂家全局不唯一！");
			}
//			int codeHp_count=DataBaseHelper.queryForScalar( codeHpSql, Integer.class, pd.getCodeHp());
//			if(codeHp_count!=0){
//				throw new BusException("医保上传编码全局不唯一！");
//			}
			
			if(code_count==0 && nameSpec_count==0 && barCode_count==0){
				DataBaseHelper.insertBean(pd);
				pk=pd.getPkPd();
			}
		}else{
			 bdPd = DataBaseHelper.queryForBean("select * from bd_pd where pk_pd = ?",BdPd.class,pd.getPkPd());
			ifNewPd = false;//修改药品信息
			
			String codeSql="select count(1) from bd_pd where del_flag='0' and code = ? and pk_pd != ?";
			String barCodeSql="select count(1) from bd_pd where del_flag='0' and barcode = ? and pk_pd != ?";
			String nameSpec="select count(1) from bd_pd where del_flag='0' and name = ? and spec = ? and pk_pd != ? and pk_factory = ?";
			//String codeHpSql="select count(1) from bd_pd where del_flag='0' and  code_hp=? and pk_pd!=?";
			if(Application.isSqlServer()){
				codeSql=codeSql.replace("count(1)", "top 1 count(1)");
				barCodeSql=barCodeSql.replace("count(1)", "top 1 count(1)");
				nameSpec=nameSpec.replace("count(1)", "top 1 count(1)");
				//codeHpSql=codeHpSql.replace("count(1)", "top 1 count(1)");
			}else{
				codeSql+=" and rownum=1";
				barCodeSql+=" and rownum=1";
				//codeHpSql+=" and rownum=1";
				nameSpec+=" and rownum=1";
			}
			int code_count = DataBaseHelper.queryForScalar(codeSql, Integer.class, pd.getCode(), pd.getPkPd());
			if(code_count!=0){
				throw new BusException("药品编码全局不唯一！");
			}
			int barCode_count = DataBaseHelper.queryForScalar(barCodeSql, Integer.class, pd.getBarcode(), pd.getPkPd());
			if(barCode_count!=0){
				throw new BusException("药品条形码全局不唯一！");
			}
			int nameSpec_count = DataBaseHelper.queryForScalar(nameSpec, Integer.class, pd.getName(), pd.getSpec(), pd.getPkPd(),pd.getPkFactory());
			if(nameSpec_count!=0){
				throw new BusException("药品名称+规格+生产厂家全局不唯一！");
			}
//			int codeHp_count=DataBaseHelper.queryForScalar(codeHpSql, Integer.class, pd.getCodeHp(), pd.getPkPd());
//			if(codeHp_count!=0){
//				throw new BusException("医保上传编码全局不唯一！");
//			}
			if(code_count==0 && nameSpec_count==0 && barCode_count==0){
				DataBaseHelper.updateBeanByPk(pd,false);
			}
		}
		
		String pkPd = pd.getPkPd();
		/**
		 * 判断保存还是修改记费策略
		 */
		if(ifNewPd){
			if(pdCgdivList!=null && pdCgdivList.size()!=0)
			{
				String pkitem = pd.getPkPd();
				for (CgdivItemVo cgdivItemVo : pdCgdivList) {
					BdHpCgdivItem cgdiv = new BdHpCgdivItem();
					ApplicationUtils.copyProperties(cgdiv, cgdivItemVo);
					cgdiv.setPkItem(pkitem);
					cgdiv.setFlagPd("1");
					if(cgdiv.getPkHpcgdivitem()!=null)
					{
						DataBaseHelper.updateBeanByPk(cgdiv, false);
					}
					else{
						
						DataBaseHelper.insertBean(cgdiv);
					}
				}
			}
		}else{
			String pkitem = pd.getPkPd();
			for (CgdivItemVo cgdivItemVo : pdCgdivList) {
				BdHpCgdivItem cgdiv = new BdHpCgdivItem();
				ApplicationUtils.copyProperties(cgdiv, cgdivItemVo);
				cgdiv.setPkItem(pkitem);
				cgdiv.setFlagPd("1");
				if(cgdiv.getPkHpcgdivitem()!=null)
				{
					DataBaseHelper.updateBeanByPk(cgdiv, false);
				}
				else{
					
					DataBaseHelper.insertBean(cgdiv);
				}
			}
		}
		/**
		 * 包装信息的保存
		 */
		if(ifNewPd){    //新增药品信息的状态下
			if(pdConvertList!=null && pdConvertList.size()!=0){
				if(checkOpIpSpecNum(pdConvertList)){
					//校验：pk_unit是否互相重复
					Map<String,String> unitMap = new HashMap<String,String>();
					int len = pdConvertList.size();
					for(int i=0; i<len; i++){
						String pkUnit = pdConvertList.get(i).getPkUnit();
						if(unitMap.containsKey(pkUnit)){
							throw new BusException("包装单位重复！");
						}
						unitMap.put(pkUnit, pdConvertList.get(i).getPkPdconvert());
					}
					for(BdPdConvert convert : pdConvertList){
						convert.setPkPd(pkPd);
						DataBaseHelper.insertBean(convert);
					}
				}else{
					throw new BusException("包装页签下请至少包含一条门诊默认=true和住院默认=true的记录！");
				}
			}else{
				throw new BusException("请插入至少一条包装规格记录！");
			}
		}else{          //修改药品信息的状态下(如果前台未点击包装Tab标签查询包装信息并对包装信息做修改，前台所传pdConvertList==null)
			if(pdConvertList!=null){
				if(pdConvertList.size()!=0){
					if(checkOpIpSpecNum(pdConvertList)){
						//校验：pk_unit是否互相重复
						Map<String,String> unitMap = new HashMap<String,String>();
						int len = pdConvertList.size();
						for(int i=0; i<len; i++){
							String pkUnit = pdConvertList.get(i).getPkUnit();
							if(unitMap.containsKey(pkUnit)){
								throw new BusException("包装单位重复！");
							}
							unitMap.put(pkUnit, pdConvertList.get(i).getPkPdconvert());
						}
						//软删除，先删除后恢复
						DataBaseHelper.update("update bd_pd_convert set del_flag = '1' where pk_pd = ?", new Object[]{pkPd});
						for(BdPdConvert convert : pdConvertList){
							if(convert.getPkPdconvert() == null){
								convert.setPkPd(pkPd);
								DataBaseHelper.insertBean(convert);
							}else{
								convert.setPkPd(pkPd);
								DataBaseHelper.updateBeanByPk(convert,false);
							}
						}
					}else{
						throw new BusException("包装页签下请至少包含一条门诊默认=true和住院默认=true的记录！");
					}
				}else{
					throw new BusException("包装页签下请至少包含一条包装记录！");
				}
			}
		}
		
		/**
		 * 药品别名保存
		 */
		if(ifNewPd){
			if(pdAsList!=null && pdAsList.size()!=0){
				for(BdPdAs alia : pdAsList){
					alia.setPkPd(pkPd);
					DataBaseHelper.insertBean(alia);
				}
			}else{
				throw new BusException("请插入至少一条别名记录！");
			}
		}else{
			if(pdAsList!=null){
				if(pdAsList.size()!=0){
					//软删除，先删除后恢复
					DataBaseHelper.update("update bd_pd_as set del_flag = '1' where pk_pd = ?", new Object[]{pkPd});
					for(BdPdAs alia : pdAsList){
						if(alia.getPkPdas() == null){
							alia.setPkPd(pkPd);
							DataBaseHelper.insertBean(alia);
						}else{
							alia.setPkPd(pkPd);
							DataBaseHelper.updateBeanByPk(alia,false);
						}
					}
				}else{
					throw new BusException("别名页签下请至少包含一条别名记录！");
				}
			}
		}
		
		/** 4、属性保存 */
		savePdAttList(pdAttList, pkPd);
		
		/**
		 * 分配保存
		 */
		if(ifNewPd){
			if(pdStoreList!=null && pdStoreList.size()!=0){
				for(BdPdStore pdstore : pdStoreList){
					pdstore.setPkPd(pkPd);
					Map<String,Object> convMap=choosePkPdconvertByStore(pdstore.getPkStore(), pkPd);
					pdstore.setPkPdconvert(convMap.get("pkPdconvert").toString());
					pdstore.setFlagStop("0");//默认启用
					pdstore.setPackSize(Long.parseLong(convMap.get("packSize").toString()));
					pdstore.setPkUnit(convMap.get("pkUnit").toString());
					DataBaseHelper.insertBean(pdstore);
					
					//保存处方包装记录
					BdPdStorePack pdStorePack = new BdPdStorePack();
					pdStorePack.setPkPdstorepack(NHISUUID.getKeyId());
					pdStorePack.setPkPdstore(pdstore.getPkPdstore());
					pdStorePack.setPkPdconvert(convMap.get("pkPdconvert").toString());
					pdStorePack.setFlagDef("1");
					pdStorePack.setDelFlag("0");
					User userBean=(User)user;
					pdStorePack.setPkOrg(userBean.getPkOrg());
					pdStorePack.setCreator(userBean.getPkEmp());
					pdStorePack.setCreateTime(new Date());
					pdStorePack.setTs(new Date());
					pdStorePack.setPackSize(Long.parseLong(convMap.get("packSize").toString()));
					pdStorePack.setPkUnit(convMap.get("pkUnit").toString());
					DataBaseHelper.insertBean(pdStorePack);
				}
			}
		}else{
			if(pdStoreList!=null){
				Set<String> pkStores=new HashSet<String>();
				if(pdStoreList.size()!=0){
					for(BdPdStore pdstore :pdStoreList){
						pdstore.setPkPd(pkPd);
						Map<String,Object> convMap=choosePkPdconvertByStore(pdstore.getPkStore(), pkPd);
						pdstore.setPkPdconvert(convMap.get("pkPdconvert").toString());
						pdstore.setFlagStop("0");//默认启用
						pdstore.setPackSize(Long.parseLong(convMap.get("packSize").toString()));
						String pkUnit=convMap.get("pkUnit").toString();
						pdstore.setPkUnit(pkUnit);
						
						BdPdStorePack pdStorePack = new BdPdStorePack();
						pdStorePack.setPkPdstorepack(NHISUUID.getKeyId());
						pdStorePack.setPkPdstore(pdstore.getPkPdstore());
						pdStorePack.setPkPdconvert(convMap.get("pkPdconvert").toString());
						pdStorePack.setFlagDef("1");
						pdStorePack.setDelFlag("0");
						User userBean=(User)user;
						pdStorePack.setPkOrg(userBean.getPkOrg());
						pdStorePack.setCreator(userBean.getPkEmp());
						pdStorePack.setCreateTime(new Date());
						pdStorePack.setTs(new Date());
						pdStorePack.setPackSize(Long.parseLong(convMap.get("packSize").toString()));
						pdStorePack.setPkUnit(pkUnit);
						
						String chkPdStoreSql="select count(1) from bd_pd_store where pk_store=? and pk_pd=?";
						Integer count=DataBaseHelper.queryForScalar(chkPdStoreSql, Integer.class, new Object[]{pdstore.getPkStore(),pkPd});
						
						if(!CommonUtils.isEmptyString(pdstore.getPkPdstore())){//修改分配页签已经分配的仓库信息时；
							pdstore.setFlagStop(null);//修改时不改变原来的停用状态
							//单位相同，将数据更新，不同优先保留仓库维护好的单位数据
							DataBaseHelper.updateBeanByWhere(pdstore, " pk_unit='"+pkUnit+"' and pk_pdstore='"+pdstore.getPkPdstore()+"'",false);
							DataBaseHelper.updateBeanByWhere(pdStorePack, " pk_unit='"+pkUnit+"' and pk_pdstore='"+pdstore.getPkPdstore()+"'",false);
						}else {
							if(count==null || count==0){
								DataBaseHelper.insertBean(pdstore);
								pdStorePack.setPkPdstore(pdstore.getPkPdstore());
								DataBaseHelper.insertBean(pdStorePack);
							}
						}
						pkStores.add(pdstore.getPkStore());
						String chkPack="update bd_pd_store set pack_size=(select pack_size from bd_pd_convert where bd_pd_convert.pk_pdconvert=bd_pd_store.pk_pdconvert) where pk_pdstore=?";
						DataBaseHelper.execute(chkPack, new Object[]{pdstore.getPkPdstore()});
					}
				}else{
					DataBaseHelper.execute("delete from bd_pd_store where pk_pd = ?", new Object[]{pkPd});
				}
				String delpdStore="delete from bd_pd_store where pk_pd='"+pkPd+"' and pk_store not in ("+ CommonUtils.convertSetToSqlInPart(pkStores, "pk_store")+") ";
				DataBaseHelper.execute(delpdStore, new Object[]{});
			}
		}
		/** 新增或跟新医保类型 **///2018-10-09孙逸仙纪念医院需求
		if (pdItemHpList != null && pdItemHpList.size() != 0) {
			
			DataBaseHelper.update("update bd_item_hp set del_flag = '1' where pk_item = ?",new Object[] { pkPd });
			for (BdItemHp hp : pdItemHpList) {
				if (hp.getPkItemhp() != null) {
					hp.setDelFlag("0");// 恢复
					hp.setPkItem(pkPd);
					hp.setEuItemType("1");
					DataBaseHelper.updateBeanByPk(hp, false);
				} else {
					hp.setPkItem(pkPd);
					hp.setEuItemType("1");
					DataBaseHelper.insertBean(hp);
				}
			}
		} else {
			DataBaseHelper.update("update bd_item_hp set del_flag = '1' where pk_item = ?",new Object[] { pkPd });
		}
		//门慢和门特
		DataBaseHelper.update("update ins_gzgy_disease_ord set del_flag = '1' where pk_ord = ?",
				new Object[] { pkPd });
		if(insGzgyDiseaseOrdList != null && insGzgyDiseaseOrdList.size() > 0){
				
			for (InsGzgyDiseaseOrd ord : insGzgyDiseaseOrdList) {
				ord.setPkOrd(pkPd);
				ord.setCodeOrd(pd.getCode());
				ord.setNameOrd(pd.getName());
				if(StringUtils.isBlank(ord.getPkDiseaseord())){
					DataBaseHelper.insertBean(ord);
				}else{
					ord.setDelFlag("0");
					DataBaseHelper.updateBeanByPk(ord, false);
				}
			}
		}
		//保存平台编码
		savePdOutCode(outcodeList,pkPd);

		//发送消息至平台
		Map<String,Object> msgParam = new HashMap<String,Object>();
		msgParam.put("pd", JsonUtil.readValue(param, BdPd.class));
		msgParam.put("pkPd", pd.getPkPd());
		msgParam.put("pdOld",bdPd );
		msgParam.put("STATUS",  ifNewPd ? this.AddState : this.UpdateState);
		PlatFormSendUtils.sendBdPdMsg(msgParam);
		
		return pd;
	}

	/**
	 * 药品字典保存时插入平台编码
	 * @param outcodeList
	 * @param pkPd
	 */
	private void savePdOutCode(List<BdPdOutcode> outcodeList,String pkPd){
		if(outcodeList!=null && outcodeList.size()>0){
			String delSql="delete from bd_pd_outcode where pk_pd=?";
			DataBaseHelper.execute(delSql,new Object[]{pkPd});

			for (BdPdOutcode outcode : outcodeList) {
				outcode.setPkOUtcode(NHISUUID.getKeyId());
				outcode.setPkPd(pkPd);
				//ApplicationUtils.setDefaultValue(outcode,true);
				outcode.setCreator(UserContext.getUser().getPkEmp());
				outcode.setCreateTime(new Date());
				outcode.setTs(new Date());
			}

			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdOutcode.class),outcodeList);
		}
	}
	/**
	 * 根据仓库默认包装分类 返回 仓库物品分配中的包装主键
	 * @param pkStore 仓库主键
	 * @param pkPd 当前药品主键
	 * @return pkPdconvert 包装主键
	 */
	private Map<String,Object> choosePkPdconvertByStore(String pkStore, String pkPd){
		Map<String, Object> map = DataBaseHelper.queryForMap("select eu_packtype from bd_store where del_flag='0' and pk_store = ?", pkStore);
		String packType = "";
		if(map == null){
			throw new BusException("仓库不存在！");
		}else{
			packType = map.get("euPacktype").toString();
		}
		Map<String, Object> convertMap = null;
		if("0".equals(packType)){//门诊
			convertMap = DataBaseHelper.queryForMap("select * from bd_pd_convert where del_flag='0' and flag_op = '1' and pk_pd = ?", pkPd);
			if(convertMap == null){
				Map<String,Object> pd = DataBaseHelper.queryForMap("select name from bd_pd where pk_pd = ?", pkPd);
				throw new BusException("药品"+pd.get("name").toString()+"没有指定默认门诊包装！");
			}
		}else if("1".equals(packType)){//住院
			convertMap = DataBaseHelper.queryForMap("select * from bd_pd_convert where del_flag='0' and flag_ip = '1' and pk_pd = ?", pkPd);
			if(convertMap == null){
				Map<String,Object> pd = DataBaseHelper.queryForMap("select name from bd_pd where pk_pd = ?", pkPd);
				throw new BusException("药品"+pd.get("name").toString()+"没有指定默认住院包装！");
			}
		}
		return convertMap;
	}
	
	/**
	 * 检测“包装”页签下有且仅包含一条门诊默认=true和一条住院默认=true的记录
	 * @param pdConvertList
	 * @return
	 */
	private boolean checkOpIpSpecNum(List<BdPdConvert> pdConvertList){
		int mz = 0;
		int zy = 0;
		for(BdPdConvert convert : pdConvertList){
			if("1".equals(convert.getFlagOp())){
				mz++;
			}
			if("1".equals(convert.getFlagIp())){
				zy++;
			}
		}
		if(mz==1 && zy==1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据拼音码和名称查询药品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPdsInfos(String param, IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String name = "";
		if(paramMap.get("name")!=null){
			name = paramMap.get("name").toString();
		}
		String spcode = "";
		if(paramMap.get("spcode")!=null){			
			spcode = paramMap.get("spcode").toString().toUpperCase();
		}
		StringBuffer sql = new StringBuffer("select pd.pk_pd, pd.code, pd.name, pd.spec, pd.pk_unit_pack, unit.name name_unit, pd.eu_drugtype, ");
		sql.append("pd.pk_factory, fa.name factory, pd.price, pd.spcode, pd.flag_stop from bd_pd pd ");
		sql.append("left join bd_unit  unit  on pd.pk_unit_pack=unit.pk_unit and unit.del_flag='0' ");
		sql.append("left join bd_factory  fa on pd.pk_factory=fa.pk_factory and fa.del_flag='0' ");
		if (Application.isSqlServer()) {
		sql.append("where pd.del_flag = '0' and pd.name like '%'+?+'%' and UPPER(pd.spcode) like '%'+?+'%' ");//修改字符串拼接	
		}else {	
		sql.append("where pd.del_flag = '0' and pd.name like '%'||?||'%' and UPPER(pd.spcode) like '%'||?||'%' ");//修改字符串拼接
		}
		List<Map<String,Object>> pdsinfos = DataBaseHelper.queryForList(sql.toString(), new Object[]{name, spcode});
		return pdsinfos;
	}
	
	/**
	 * 获取包装信息（药品计量单位）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getConvertsByPkPd(String param, IUser user){
		String pkPd = JsonUtil.getFieldValue(param, "pkPd");
		List<Map<String,Object>> convertMapList = DataBaseHelper.queryForList("select * from bd_pd_convert where del_flag='0' and pk_pd=?", pkPd);//全局的
		if(convertMapList != null && convertMapList.size() != 0){
			for(Map<String,Object> convertMap : convertMapList){
				String pkPdconvert = convertMap.get("pkPdconvert").toString();
				/**
				 * 校验，判断如下三种情况：
				 * 1.在所有的仓库(全局)中，如果该药品的包装主键存在被引用，
				 * 即该药品被分配到仓库,则该包装主键在药品字典维护处，不允许删除，设置一个标志返回字段optFlag = 1
				 * 2.在该药品被分配到仓库的前提下，该药品在此仓库中还有库存，则该包装主键不允许删除同时不允许修改，设optFlag = 2
				 * 3.在所有的仓库(全局)中，如果该药品的包装主键不存在被引用，optFlag = 0
				 */
				String optFlag = Constants.OPTFLAG_ZERO;
				int count_citeConvert = DataBaseHelper.queryForScalar("select count(1) from bd_pd_store where del_flag='0' and pk_pd=? and pk_pdconvert=?", Integer.class, pkPd, pkPdconvert);
				if(count_citeConvert > 0){//该药品被分配到仓库，并指定了pk_pdconvert
					optFlag = Constants.OPTFLAG_ONE;
					String sql = "select sum(nvl(stk.quan_min,0)) kc "
							+ "from bd_pd_store ps "
							+ "inner join  pd_stock  stk on ps.pk_pd=stk.pk_pd and stk.del_flag = '0' "
							+ "where ps.del_flag='0' and ps.pk_pdconvert=? and ps.pk_pd=?";
					double kc = DataBaseHelper.queryForScalar(sql, Double.class, pkPdconvert, pkPd);
					if(kc > 0){//存在库存
						optFlag = Constants.OPTFLAG_TWO;
					}
				}
				convertMap.put("optFlag", optFlag);
			}
			return convertMapList;
		}else{
			return null;
		}
	}
	
	/**
	 * 删除药品信息
	 * @param param
	 * @param user
	 */
	public void delPd(String param, IUser user){
		String pkPd = JsonUtil.getFieldValue(param, "pkPd");
		//仓库物品
		String sql1 = "select count(1) from bd_pd_store where del_flag = '0' and pk_pd=?";
		//供应商协议
		String sql2 = "select count(1) from bd_pd_supplyer where del_flag = '0' and pk_pd=?";
		//计划
		String sql3 = "select count(1) from pd_plan_detail where del_flag = '0' and pk_pd=?";
		//采购
		String sql4 = "select count(1) from pd_purchase_dt where del_flag = '0' and pk_pd=?";
		//出入库记录
		String sql5 = "select count(1) from pd_st_detail where del_flag = '0' and pk_pd=?";
		//库存
		String sql6 = "select count(1) from pd_stock where del_flag = '0' and pk_pd=?";
		//医嘱
		String sql7 = "select count(1) from cn_order ord where ord.del_flag = '0' and ord.pk_ord=? and ord.flag_durg=1";
		//门诊费用
		String sql8 = "select count(1) from bl_op_dt cg where cg.del_flag = '0' and cg.pk_pd=?";
		//住院费用
		String sql9 = "select count(1) from bl_ip_dt cg where cg.del_flag = '0' and cg.pk_pd=?";
		
		if(Application.isSqlServer()){
			sql1=sql1.replace("count(1)", "top 1 count(1)");
			sql2=sql2.replace("count(1)", "top 1 count(1)");
			sql3=sql3.replace("count(1)", "top 1 count(1)");
			sql4=sql4.replace("count(1)", "top 1 count(1)");
			sql5=sql5.replace("count(1)", "top 1 count(1)");
			sql6=sql6.replace("count(1)", "top 1 count(1)");
			sql7=sql7.replace("count(1)", "top 1 count(1)");
			sql8=sql8.replace("count(1)", "top 1 count(1)");
			sql9=sql9.replace("count(1)", "top 1 count(1)");
		}else{
			sql1+=" and rownum=1";
			sql2+=" and rownum=1";
			sql3+=" and rownum=1";
			sql4+=" and rownum=1";
			sql5+=" and rownum=1";
			sql6+=" and rownum=1";
			sql7+=" and rownum=1";
			sql8+=" and rownum=1";
		}
		
		int count1 = DataBaseHelper.queryForScalar(sql1, Integer.class, pkPd);
		if (count1 > 0) {
			throw new BusException("药品被仓库物品表引用！");
		}
		int count2 = DataBaseHelper.queryForScalar(sql2, Integer.class, pkPd);
		if (count2 > 0) {
			throw new BusException("药品被供应商协议表引用！");
		}
		int count3 = DataBaseHelper.queryForScalar(sql3, Integer.class, pkPd);
		if (count3 > 0) {
			throw new BusException("药品被请领明细表引用！");
		}
		int count4 = DataBaseHelper.queryForScalar(sql4, Integer.class, pkPd);
		if (count4 > 0) {
			throw new BusException("药品被采购明细表引用！");
		}
		int count5 = DataBaseHelper.queryForScalar(sql5, Integer.class, pkPd);
		if (count5 > 0) {
			throw new BusException("药品被出入库明细表引用！");
		}
		int count6 = DataBaseHelper.queryForScalar(sql6, Integer.class, pkPd);
		if (count6 > 0) {
			throw new BusException("药品被库存表引用！");
		}
		int count7 = DataBaseHelper.queryForScalar(sql7, Integer.class, pkPd);
		if (count7 > 0) {
			throw new BusException("药品被临床医嘱表引用！");
		}
		int count8 = DataBaseHelper.queryForScalar(sql8, Integer.class, pkPd);
		if (count8 > 0) {
			throw new BusException("药品被门诊收费表引用！");
		}
		int count9 = DataBaseHelper.queryForScalar(sql9, Integer.class, pkPd);
		if (count9 > 0) {
			throw new BusException("药品被住院收费表引用！");
		}
		
		//删除别名
		DataBaseHelper.update("update bd_pd_as set del_flag='1' where pk_pd = ?",new Object[]{pkPd});
		//删除附加属性
		DataBaseHelper.update("update bd_pd_att set del_flag='1' where pk_pd = ?",new Object[]{pkPd});
		//删除包装
		DataBaseHelper.update("update bd_pd_convert set del_flag='1' where pk_pd = ?",new Object[]{pkPd});
		//删除药品信息
		DataBaseHelper.update("update bd_pd set del_flag='1' where pk_pd = ?",new Object[]{pkPd});
		//删除医保信息
		DataBaseHelper.update("update bd_item_hp set del_flag = '1' where pk_item = ?",new Object[] { pkPd });
		
		
		//发送消息至平台
		Map<String,Object> msgParam = new HashMap<String,Object>();
		msgParam.put("pd", JsonUtil.readValue(param, BdPd.class));
		msgParam.put("STATUS",  this.DelState);
		msgParam.put("pkPd", pkPd);
		PlatFormSendUtils.sendBdPdMsg(msgParam);
		msgParam = null;
	}

	/**
	 * 删除仓库物品信息
	 * @param param
	 * @param user
	 */
	public void delPdStore(String param, IUser user){
		List<BdPdStore> pdstoreList = JsonUtil.readValue(param, new TypeReference<List<BdPdStore>>() {
		});
		if(pdstoreList!=null && pdstoreList.size()!=0){
			for(BdPdStore pdstore : pdstoreList){
				String pkPd = pdstore.getPkPd();
				String pkStore = pdstore.getPkStore();
				/**检查仓库物品是否被引用*/
				int citedType = ifBeCited(pkPd, pkStore);
				
				if(0 == citedType){//未被引用
                    //删除处方用药包装类型
					DataBaseHelper.update("delete from  bd_pd_store_pack where pk_pdstore in  "
							+ "(select pk_pdstore from bd_pd_store where pk_store=? and pk_pd=?)", pkStore,pkPd );	
					//删除物品
					DataBaseHelper.execute("delete from bd_pd_store where pk_store = ? and pk_pd = ? ",pkStore,pkPd);
				}else{
					if(Constants.TYPE_PD_PLAN_DETAIL == citedType){//被计划引用
						throw new BusException("仓库物品被请领表引用！");
					}
					if(Constants.TYPE_PD_ST_DETAIL == citedType){//被出入库引用
						throw new BusException("仓库物品被出入库表引用！");
					}
					if(Constants.TYPE_PD_STOCK == citedType){//被库存引用
						throw new BusException("仓库物品被库存表引用！");
					}
					if(Constants.TYPE_PD_INV_DETAIL == citedType){//被盘点引用
						throw new BusException("仓库物品被盘点单表引用！");
					}
					if(Constants.TYPE_PD_CC_DETAIL == citedType){//被结账引用
						throw new BusException("仓库物品被结账表引用！");
					}
				}
			}
		}
	}

	/**
	 * 仓库物品是否被业务引用
	 * @param pkPd
	 * @param pkStore
	 * @return
	 * 0--未被引用
	 * 1--被计划引用
	 * 2--被出入库引用
	 * 3--被库存引用
	 * 4--被盘点引用
	 * 5--被结账引用
	 * 
	 */
	private int ifBeCited(String pkPd, String pkStore) {
		//计划
		String sql1 = "select pla.pk_pdplan from pd_plan pla "
				+ "inner join pd_plan_detail pladt on pla.pk_pdplan=pladt.pk_pdplan "
				+ "where pla.del_flag='0' and pla.pk_store=? and pladt.pk_pd=?";
		//出入库
		String sql2 = "select st.pk_pdst from pd_st st "
				+ "inner join pd_st_detail stdt on st.pk_pdst=stdt.pk_pdst "
				+ "where st.del_flag='0' and st.pk_store_st=? and stdt.pk_pd=?";
		//库存
		String sql3 = "select sto.pk_pdstock from pd_stock sto "
				+ "where sto.del_flag='0' and sto.pk_store=? and sto.pk_pd=?";
		//盘点
		String sql4 = "select inv.pk_pdinv from pd_inventory inv "
				+ "inner join pd_inv_detail invdt on inv.pk_pdinv=invdt.pk_pdinv "
				+ "where inv.del_flag='0' and inv.pk_store=? and invdt.pk_pd=?";
		//结账
		String sql5 = "select cc.pk_pdcc from pd_cc cc "
				+ "inner join pd_cc_detail ccdt on cc.pk_pdcc=ccdt.pk_pdcc "
				+ "where cc.del_flag='0' and cc.pk_store=? and ccdt.pk_pd=?";
		if(Application.isSqlServer()){
			sql1=sql1.replaceFirst("pla.pk_pdplan", "top 1 pla.pk_pdplan");
			sql2=sql2.replaceFirst("st.pk_pdst", "top 1 st.pk_pdst");
			sql3=sql3.replaceFirst("sto.pk_pdstock", "top 1 sto.pk_pdstock");
			sql4=sql4.replaceFirst("inv.pk_pdinv", "top 1 inv.pk_pdinv");
			sql5=sql5.replaceFirst("cc.pk_pdcc", "top 1 cc.pk_pdcc");
		}else{
			sql1+=" and rownum=1";
			sql2+=" and rownum=1";
			sql3+=" and rownum=1";
			sql4+=" and rownum=1";
			sql5+=" and rownum=1";
		}
		
		Map<String,Object> res1 = DataBaseHelper.queryForMap(sql1,pkStore, pkPd);
		if(res1 != null && res1.size()>0){
			return Constants.TYPE_PD_PLAN_DETAIL;
		}
		Map<String,Object> res2 = DataBaseHelper.queryForMap(sql2, pkStore, pkPd);
		if(res2 != null && res2.size()>0){
			return Constants.TYPE_PD_ST_DETAIL;
		}
		Map<String,Object> res3 = DataBaseHelper.queryForMap(sql3,pkStore, pkPd);
		if(res3 != null && res3.size()>0){
			return Constants.TYPE_PD_STOCK;
		}
		Map<String,Object> res4 = DataBaseHelper.queryForMap(sql4,pkStore, pkPd);
		if(res4 != null && res4.size()>0){
			return Constants.TYPE_PD_INV_DETAIL;
		}
		Map<String,Object> res5 = DataBaseHelper.queryForMap(sql5,pkStore, pkPd);
		if(res5 != null && res5.size()>0){
			return Constants.TYPE_PD_CC_DETAIL;
		}
		
		if((res1==null||res1.size()==0) && (res2==null||res2.size()==0)  && (res3==null||res3.size()==0)  && (res4==null||res4.size()==0)  && (res5==null||res5.size()==0) ){
			return 0;
		}else{
			return 9;
		}
	}
	
	/**
	 * 保存仓库物品信息
	 * @param param
	 * @param user
	 */
	public void newPdStores(String param, IUser user){		
		List<BdPdStore> pdlist = JsonUtil.readValue(param, new TypeReference<List<BdPdStore>>(){});
		if(pdlist!=null && pdlist.size() > 0){
			List<BdPdStore> pdStoreList=new ArrayList<BdPdStore>();
			List<BdPdStorePack> storePackList = new  ArrayList<BdPdStorePack>();
			BdPdStorePack pdStorePack = null;
			String pkStore = ((User)user).getPkStore();
			String pkDept = ((User)user).getPkDept();
			String pkOrg = ((User)user).getPkOrg();
			String pkUser = ((User)user).getPkEmp();
			Date opeDate = new Date();
			String pkPdConvert = "";
			for(BdPdStore pdstore : pdlist){
				String sql="select count(1) from BD_PD_STORE where PK_STORE=? and PK_PD=?";
				int count=DataBaseHelper.queryForScalar(sql, Integer.class,new Object[]{pkStore,pdstore.getPkPd()});
				if(count>0){
					continue;
				}
				Map<String,Object> convertMap=qryConvert(pkStore,pdstore);
				pkPdConvert = convertMap.get("pkPdconvert").toString();
				pdstore.setPkDept(pkDept);
				pdstore.setPkStore(pkStore);
				pdstore.setFlagStop("0");
				pdstore.setPkPdstore(NHISUUID.getKeyId());
				if(!CommonUtils.isEmptyString(pkPdConvert))
					pdstore.setPkPdconvert(pkPdConvert);
				pdstore.setDelFlag("0");
				pdstore.setPkOrg(pkOrg);
				pdstore.setCreator(pkUser);
				pdstore.setCreateTime(opeDate);
				pdstore.setTs(opeDate);
				pdstore.setPkUnit(convertMap.get("pkUnit").toString());
				pdstore.setPackSize(Long.parseLong(convertMap.get("packSize").toString()));
				pdStoreList.add(pdstore);
				
				pdStorePack = new BdPdStorePack();
				pdStorePack.setPkPdstorepack(NHISUUID.getKeyId());
				pdStorePack.setPkPdstore(pdstore.getPkPdstore());
				if(!CommonUtils.isEmptyString(pkPdConvert))
					pdStorePack.setPkPdconvert(pkPdConvert);
				pdStorePack.setPkUnit(convertMap.get("pkUnit").toString());
				pdStorePack.setPackSize(Long.parseLong(convertMap.get("packSize").toString()));
				pdStorePack.setFlagDef("1");
				pdStorePack.setDelFlag("0");
				pdStorePack.setPkOrg(pkOrg);
				pdStorePack.setCreator(pkUser);
				pdStorePack.setCreateTime(opeDate);
				pdStorePack.setFlagIp(EnumerateParameter.ONE);
				pdStorePack.setTs(opeDate);
				storePackList.add(pdStorePack);
				
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdStore.class), pdStoreList);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdStorePack.class), storePackList);
			ExtSystemProcessUtils.processExtMethod("PackMachineOp", "getMedInfo",pdStoreList);
		}
		
	}
	
	/**
	 * 根据pk_pd 查询包装单位和包装数量
	 * @param pkPd
	 * @return
	 */
	private Map<String,Object> qryConvert(String pkStore,BdPdStore pdstore){
		//task7028 【深大】药库仓库物品维护，仓库单位和包装类型自动取药品字典包装单位，配合修改后台
		//兼容性处理: pkPdconvert字段为空走原来的旧有逻辑，通过物资主键pkpd查询包装单位和数量; 不为空则通过计量单位表的主键pkPdconvert直接查询 不区分门诊住院
		String pkPd = pdstore.getPkPd();
		String pkPdconvert = pdstore.getPkPdconvert();
		
		Map<String, Object> map = DataBaseHelper.queryForMap("select eu_packtype from bd_store where del_flag='0' and pk_store = ?", pkStore);
		String packType = "";
		if(map == null){
			throw new BusException("仓库不存在！");
		}else{
			packType = map.get("euPacktype").toString();
		}
		Map<String, Object> convertMap = null;
		
		if(StringUtils.isNotBlank(pkPdconvert)) {
			convertMap = DataBaseHelper.queryForMap("select pk_pdconvert,pk_unit,pack_size from bd_pd_convert where del_flag='0' and pk_pdconvert = ?", pkPdconvert);
		} else {
			if("0".equals(packType)){//门诊
				convertMap = DataBaseHelper.queryForMap("select * from bd_pd_convert where del_flag='0' and flag_op = '1' and pk_pd = ?", pkPd);
				if(convertMap == null){
					Map<String,Object> pd = DataBaseHelper.queryForMap("select name from bd_pd where pk_pd = ?", pkPd);
					throw new BusException("药品"+pd.get("name").toString()+"没有指定默认门诊包装！");
				}
			}else if("1".equals(packType)){//住院
				convertMap = DataBaseHelper.queryForMap("select * from bd_pd_convert where del_flag='0' and flag_ip = '1' and pk_pd = ?", pkPd);
				if(convertMap == null){
					Map<String,Object> pd = DataBaseHelper.queryForMap("select name from bd_pd where pk_pd = ?", pkPd);
					throw new BusException("药品"+pd.get("name").toString()+"没有指定默认住院包装！");
				}
			}
		}
		return convertMap;
		
	}
	
	/**
	 * 修改仓库物品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public BdPdStore updatePdStore(String param, IUser user){
		
		BdPdStore pdStore = JsonUtil.readValue(param, BdPdStore.class);
		
		this.scmPubMapper.updatePdStore(pdStore);
		
		return pdStore;
	}
	
	/**
	 * 查询仓库物品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> quePdStores(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkStore = ((User)user).getPkStore();
		paramMap.put("pkStore", pkStore);
		if(paramMap.get("pycode")!=null){
			paramMap.put("pycode", paramMap.get("pycode").toString().toUpperCase());
		}
		if(paramMap.get("posiNo")!=null){
			String posiStr=paramMap.get("posiNo").toString();
			String [] str=posiStr.split(",");
			String posiSql="1=0";
			for (int i = 0; i < str.length; i++) {
				posiSql+=" or pdsto.posi_no like '%"+str[i]+"%'";
			}
			paramMap.put("posiSql", posiSql);
		}
		int pageIndex = CommonUtils.getInteger(paramMap.get("pageIndex"));
		int pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
		MyBatisPage.startPage(pageIndex, pageSize);
		
		String useLeftjoin = null;
		if(paramMap.containsKey("useLeftJoin"))
		{
			useLeftjoin = (String)paramMap.get("useLeftJoin");
		}
		List<Map<String,Object>> mapResult = null;
		if("1".equals(useLeftjoin))
		{
			mapResult = this.scmPubMapper.quePdStoresSpecial(paramMap);
		}
		else
		{
			mapResult = this.scmPubMapper.quePdStores(paramMap);
		}
		
		Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("deList", mapResult);
		resMap.put("TotalCount", page.getTotalCount());
		return resMap;
	}
	
	/**
	 * 查询仓库新增的物品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queNewPds(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkStore = ((User)user).getPkStore();
		Map<String, Object> map = DataBaseHelper.queryForMap("select eu_packtype from bd_store where del_flag='0' and pk_store = ?", pkStore);
		if(map == null||map.get("euPacktype")==null||"".equals(map.get("euPacktype").toString()))
			throw new BusException("仓库不存在或未设置包装类型，无法查询对应可用物品！");
		paramMap.put("pkStore", pkStore);
		paramMap.put("packType", map.get("euPacktype"));
		String spcode = "";
		if(paramMap.get("spcode")!=null){
			spcode = paramMap.get("spcode").toString().toUpperCase();
		}
		paramMap.put("spcode",spcode);
		return scmPubMapper.queNewPds(paramMap);
	}
	
	/**
	 * 查询供应商协议-浏览
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public List<Map<String,Object>> quePdSupplyers(String param, IUser user) throws ParseException{
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String currjg = ((User)user).getPkOrg();
		String globaljg = "~                               ";
		List<String> jglist = new ArrayList<String>();
		jglist.add(currjg);
		jglist.add(globaljg);
		paramMap.put("jglist", jglist);
		List<Map<String, Object>> quePdSupplyers = scmPubMapper.quePdSupplyers(paramMap);
		return quePdSupplyers;
	}
	
	/**
	 * 交易号：008001001071
	 * 查询供应商协议-浏览（物资）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queMtlPdSupplyers(String param, IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		
		String currjg = ((User)user).getPkOrg();
		String globaljg = "~                               ";
		List<String> jglist = new ArrayList<String>();
		jglist.add(currjg);
		jglist.add(globaljg);
		paramMap.put("jglist", jglist);
		List<Map<String, Object>> quePdSupplyers = scmPubMapper.queMtlPdSupplyers(paramMap);
		return quePdSupplyers;
	}
	
	/**
	 * 查询供应商协议-编辑
	 * @param param
	 * {
	 * "pkSupplyer":"供应商",
	 * "codeAgree":"协议号",
	 * "spcode":"拼音码"
	 * }
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queEditPdSupplyers(String param, IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String curOrg = ((User)user).getPkOrg();
		String globalOrg = "~                               ";
		List<String> pkOrgList = new ArrayList<String>();
		pkOrgList.add(curOrg);
		pkOrgList.add(globalOrg);
		paramMap.put("jglist", pkOrgList);
		List<Map<String, Object>> quePdSupplyers = scmPubMapper.queEditPdSupplyers(paramMap);
		return quePdSupplyers;
	}
	
	/**
	 * 保存供应商协议 - 新版本：那个机构下的用户操作，保存至那个机构
	 * @param param
	 * @param user
	 */
	public void savePdSupplyers(String param, IUser user){
		
		PdSuppyersParam pdSuppsParam = JsonUtil.readValue(param, PdSuppyersParam.class);
		String oldCodeAgree = pdSuppsParam.getOldCodeAgree();//旧协议号
		String oldPkSupplyer = pdSuppsParam.getOldPkSupplyer();//旧供应商
		
		log.debug(" oldCodeAgree: "+oldCodeAgree+" , oldPkSupplyer : "+oldPkSupplyer);
		
		List<BdPdSupplyer> pdSuppList = pdSuppsParam.getPdSuppList();//待保存的供应商-物品记录
		if(pdSuppList == null || pdSuppList.size() < 1)
			throw new BusException("请插入至少一条供应商协议记录（物品和供应商的对应关系）！");

		String pkOrgCur = ((User)user).getPkOrg();		
		String codeAgree = pdSuppList.get(0).getCodeAgree();
		String pkSupplyer = pdSuppList.get(0).getPkSupplyer();
		
		if(oldCodeAgree == null && oldPkSupplyer == null)//新增
		{
			String flagGlobal = ApplicationUtils.getSysparam("SCM0001", true);//协议号是否全局唯一
			String sql = "select count(*) from bd_pd_supplyer where del_flag='0' and code_agree=? ";
			if("0".equals(flagGlobal))
				sql = sql + " and pk_org = '"+((User) user).getPkOrg()+"'";
			//校验协议号 - 全局唯一
			int count = DataBaseHelper.queryForScalar(sql, Integer.class, codeAgree);
			if(count != 0)
				throw new BusException("协议号【"+codeAgree+"】已存在,请重新输入！");
			
			//插入新增供应商-物品信息
			for(BdPdSupplyer pdsupp : pdSuppList){
				//插入前先校验当前物品是否已存在
				String msg = chkPdSupOnlyOne(pkOrgCur, codeAgree, pkSupplyer, pdsupp.getPkPd());
				if(!CommonUtils.isEmptyString(msg))
					throw new BusException(msg);
				pdsupp.setPkOrg(pkOrgCur);
				DataBaseHelper.insertBean(pdsupp);
			}
		}
		else//修改
		{
			//校验协议号:1、删除旧参数；2、校验全局唯一
			DataBaseHelper.update("update bd_pd_supplyer set del_flag = '1' "
					+ "where pk_supplyer = ? and code_agree = ? and pk_org = ?", oldPkSupplyer, oldCodeAgree, pkOrgCur);
			
			//全局唯一
			int count = DataBaseHelper.queryForScalar("select count(*) from bd_pd_supplyer where del_flag='0' and code_agree=?", Integer.class,  codeAgree);
			if(count != 0)
				throw new BusException("协议号【"+codeAgree+"】已存在,请重新输入！");
			
			//更新供应商信息
			for(BdPdSupplyer pdsupp : pdSuppList){
				if(pdsupp.getPkPdsupplyer()==null){
					//插入新增记录时，先校验当前操作机构下是否已存在
					String msg = chkPdSupOnlyOne(pkOrgCur, codeAgree, pkSupplyer, pdsupp.getPkPd());
					if(!CommonUtils.isEmptyString(msg))
						throw new BusException(msg);
					pdsupp.setPkOrg(pkOrgCur);
					DataBaseHelper.insertBean(pdsupp);
				}else{
					pdsupp.setDelFlag("0");//恢复
					DataBaseHelper.updateBeanByPk(pdsupp, false);
				}
			}
		}
	}

	/**
	 * 校验 供应商协议中物品是否唯一
	 * @param jg 机构主键
	 * @param codeAgree 协议号
	 * @param pkSupplyer 供应商主键
	 * @param pkPd 物品主键
	 * @return
	 */
	private String chkPdSupOnlyOne(String jg, String codeAgree, String pkSupplyer, String pkPd) {
		String msg = "";
		Map<String,Object> chkParam = new HashMap<String,Object>();
		chkParam.put("codeAgree", codeAgree);
		chkParam.put("pkSupplyer", pkSupplyer);
		List<String> pkOrgList = new ArrayList<String>();
		pkOrgList.add(jg);
		chkParam.put("jglist", pkOrgList);
		chkParam.put("pkPd", pkPd);
		List<Map<String, Object>> quePdSupplyers = scmPubMapper.queEditPdSupplyers(chkParam);
		if(quePdSupplyers.size() > 0)
			msg = "药品【"+quePdSupplyers.get(0).get("pdname") +"】"
					+ "\n在 供应商【"+quePdSupplyers.get(0).get("supplyer") +"】"
					+ "\n-协议号【"+codeAgree+"】中"
					+ "\n已存在【"+ quePdSupplyers.size() +"】条，请删除该物品！";
		return msg;
	}
	
	/**
	 * 删除供应商协议
	 * @param param
	 * @param user
	 */
	public void delPdSupplyer(String param, IUser user){
		String pkPdsupplyer = JsonUtil.getFieldValue(param, "pkPdsupplyer");
		
		DataBaseHelper.update("update bd_pd_supplyer set del_flag = '1' where pk_pdsupplyer = ?", new Object[]{pkPdsupplyer});
	}
	/**
	 * 根据仓库物料主键获取供应链处方包装类型
	 */
	public List<BdPdStorePackVo> bdPdStorePack(String param, IUser user){
		String pkPdstore = JsonUtil.getFieldValue(param, "pkPdstore");
		String sql = "SELECT sp.*, pc.spec,bp.PK_UNIT_PACK FROM "+
	" bd_pd_store_pack sp LEFT JOIN bd_pd_convert pc ON pc.pk_pdconvert = sp.pk_pdconvert LEFT JOIN BD_PD bp on bp.pk_pd = pc.pk_pd "+
	" WHERE sp.pk_pdstore = ? AND sp.del_flag = '0'";
		
		List<BdPdStorePackVo> bdPdStorePack = DataBaseHelper.queryForList(sql,BdPdStorePackVo.class, pkPdstore);
		return bdPdStorePack;
	}
	/**
	 * 根据仓库物料主键获取供应链仓库物品医生信息
	 */
	public List<BdPdStoreEmp> bdPdStoreEmp(String param, IUser user){
		String pkPdstore = JsonUtil.getFieldValue(param, "pkPdstore");
		String sql = "SELECT * from bd_pd_store_emp  WHERE pk_pdstore= ? and del_flag='0'";		
		List<BdPdStoreEmp> bdPdStoreEmp = DataBaseHelper.queryForList(sql,BdPdStoreEmp.class, pkPdstore);
		System.out.println(bdPdStoreEmp);
		return bdPdStoreEmp;
	}
	/**
	 * 保存专用医生信息
	 */
	public void saveBdPdStoreEmp(String param, IUser user){
		BdPdStoreEmpVo bEmpVo = JsonUtil.readValue(param, BdPdStoreEmpVo.class);
		BdPdStore bStore = bEmpVo.getPdStore();
		List<BdPdStoreEmp> list = bEmpVo.getPdStoreEmpList();
		String pkPdStore= bStore.getPkPdstore();
		//DataBaseHelper.update("update bd_pd_store_emp set del_flag = '1' where pk_pdstore = ?",new Object[]{pkPdStore} );
		//删除原医师信息
		DataBaseHelper.execute("delete from bd_pd_store_emp where pk_pdstore = ? ", pkPdStore);
		if(list != null && list.size()>0){
			for(BdPdStoreEmp emp : list){
				emp.setPkPdstoreemp(NHISUUID.getKeyId());
				emp.setCreateTime(new Date());
				emp.setCreator(((User)user).getPkEmp());
				emp.setTs(new Date());
				emp.setDelFlag("0");
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdStoreEmp.class), list);
		 }	
		List<BdPdStoreEmp> dellist = bEmpVo.getPdStoreEmpDel();
		if(dellist!=null && dellist.size()>0){
			for(BdPdStoreEmp emp:dellist){
				DataBaseHelper.execute("delete from bd_pd_store_emp where pk_pdstoreemp = ? ",emp.getPkPdstoreemp());
			}
		}
	}
	/**
	 * 保存仓库物品信息和处方包装信息
	 */
	public void saveBdPdStoreVo(String param, IUser user){
		BdPdStoreVo bVo = JsonUtil.readValue(param, BdPdStoreVo.class);
		BdPdStore bStore = bVo.getPdStoreInfo();
		List<BdPdStorePack> list = bVo.getPdStorePackList();
		
		//如果更改了医嘱单位，bd_pd_store和bd_pdconvert建立关联关系并将bd_pd_store表中的pack_size重新赋值
		String pdStore="select pk_unit,pack_size from bd_pd_convert where pk_pdconvert=?";
		Map<String,Object> convertMap=DataBaseHelper.queryForMap(pdStore, new Object[]{bStore.getPkPdconvert()});
		String pkUser = ((User)user).getPkEmp();
		if(convertMap!=null){
			bStore.setPkUnit(convertMap.get("pkUnit").toString());
			bStore.setPackSize(Long.parseLong(convertMap.get("packSize").toString()));
		}
		bStore.setModifier(pkUser);


		//保存仓库物品信息
		DataBaseHelper.updateBeanByPk(bStore,false);	
		
		String pkPdStore= bStore.getPkPdstore();
		DataBaseHelper.update("update BD_PD_STORE set modifier = ?  where pk_pdstore = ?",new Object[]{pkUser,pkPdStore} );
		
		//DataBaseHelper.update("update bd_pd_store_pack set del_flag = '1' where pk_pdstore = ?",new Object[]{pkPdStore} );	
		//保存处方包装信息
		DataBaseHelper.execute("delete from bd_pd_store_pack where pk_pdstore = ? ", pkPdStore);
		if(list != null && list.size()>0){
			for(BdPdStorePack pack : list){
				pack.setPkPdstorepack(NHISUUID.getKeyId());
				String sql="select pk_unit ,pack_size from bd_pd_convert where pk_pdconvert=?";
				Map<String,Object> convMap=DataBaseHelper.queryForMap(sql, pack.getPkPdconvert());
				pack.setPkUnit(convMap.get("pkUnit").toString());
				pack.setPackSize(Long.parseLong(convMap.get("packSize").toString()));
				pack.setCreateTime(new Date());
				pack.setCreator(((User)user).getPkEmp());
				pack.setTs(new Date());
				pack.setDelFlag("0");
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdStorePack.class), list);
		 }	
		List<BdPdStore> storeList=new ArrayList<BdPdStore>();
		storeList.add(bStore);
		ExtSystemProcessUtils.processExtMethod("PackMachineOp", "getMedInfo",storeList);
	}
	/**
	 * 获取药品全部信息
	 * @param param
	 * @param user
	 * @return
	 */
	public PdAndAllParam getPdAndAllParam(String param, IUser user){
		String PkPd = JsonUtil.getFieldValue(param, "pkPd");
		PdAndAllParam pAllParam = new PdAndAllParam();
		BdPd bdPd = DataBaseHelper.queryForBean("select * from bd_pd where del_flag='0' and pk_pd = ? ", BdPd.class, PkPd);
		List<BdPdConvert> bList = DataBaseHelper.queryForList("select * from bd_pd_convert where del_flag='0' and pk_pd = ? ", BdPdConvert.class, PkPd);
		List<BdPdAs> bAs = DataBaseHelper.queryForList("select * from bd_pd_as where del_flag='0' and pk_pd = ? ", BdPdAs.class, PkPd);						
		List<BdItemHp> pdItemHpList=DataBaseHelper.queryForList("select * from bd_item_hp where del_flag = '0' and  pk_item =? ", BdItemHp.class, PkPd);	
		
		InsGzgyDisease insGzgyDiseaseSlow = DataBaseHelper.queryForBean("select dise.* from ins_gzgy_disease_ord gdo "
				+ "inner join ins_gzgy_disease dise on gdo.pk_gzgydisease=dise.pk_gzgydisease "
				+ "where gdo.pk_ord = ? and gdo.eu_ordtype='1' and dise.dt_diseasetype='01' and gdo.del_flag='0' ", InsGzgyDisease.class, PkPd);
		
		InsGzgyDisease insGzgyDiseaseMentor = DataBaseHelper.queryForBean("select dise.* from ins_gzgy_disease_ord gdo "
				+ "inner join ins_gzgy_disease dise on gdo.pk_gzgydisease=dise.pk_gzgydisease "
				+ "where gdo.pk_ord = ? and gdo.eu_ordtype='1' and dise.dt_diseasetype='02' and gdo.del_flag='0' ", InsGzgyDisease.class, PkPd);
		List<InsGzgyDisease> insGzgyDiseaseList = new ArrayList<>();
		insGzgyDiseaseList.add(insGzgyDiseaseSlow);
		insGzgyDiseaseList.add(insGzgyDiseaseMentor);
		
		pAllParam.setPd(bdPd);
		pAllParam.setPdConvertList(bList);
		pAllParam.setPdAsList(bAs);	
		pAllParam.setPdItemHpList(pdItemHpList);
		pAllParam.setInsGzgyDiseaseList(insGzgyDiseaseList);
		return pAllParam;
	}
	/**
	 * 保存药品信息 -- 返回主键 此功能有，单未启用
	 * @param param
	 * @param user
	 * @return
	 */
	public BdPd saveBdPdAllInfoRtnPd(String param, IUser user) {
		PdAndAllParam pdAndAll = JsonUtil.readValue(param, PdAndAllParam.class);
		BdPd pd = pdAndAll.getPd();
		List<BdPdConvert> pdConvertList = pdAndAll.getPdConvertList();
		List<BdPdAs> pdAsList = pdAndAll.getPdAsList();
		List<BdPdAtt> pdAttList = pdAndAll.getPdAttList();
		List<BdPdStore> pdStoreList = pdAndAll.getPdStoreList();
 
		// 是否新增药品标志
		boolean ifNewPd = null == pd.getPkPd() || CommonUtils.isEmptyString(pd.getPkPd());
		
		/** 1 药品信息的保存 */
		pd = savePdInfo(pd, ifNewPd);
		String pkPd = pd.getPkPd();// 获取修改后的主键

		/** 2、包装信息的保存 */
		savePdConverList(pdConvertList, ifNewPd, pkPd);

		/** 3、药品别名保存 */
		savePdAsList(pdAsList, ifNewPd, pkPd);

		/** 4、属性保存 */
		savePdAttList(pdAttList, pkPd);

		/** 5、分配保存  */
		savePdStoreList(pdStoreList, ifNewPd, pkPd);
		

		//发送消息至平台
		Map<String,Object> msgParam = new HashMap<String,Object>();
		msgParam.put("pd", pd);
		msgParam.put("STATUS", ifNewPd ? this.AddState : this.UpdateState);
		PlatFormSendUtils.sendBdPdMsg(msgParam);
		msgParam = null;
		return pd;
	}

	public static Map<String, Object> objectToMap(Object obj) throws Exception {    
        if(obj == null){    
            return null;    
        }   
  
        Map<String, Object> map = new HashMap<String, Object>();    
  
        Field[] declaredFields = obj.getClass().getDeclaredFields();    
        for (Field field : declaredFields) {    
            field.setAccessible(true);  
            map.put(field.getName(), field.get(obj));  
        }    
  
        return map;  
    }   

	
	/**
	 * 1、保存药品基本信息
	 * @param pd
	 * @param ifNewPd
	 * @return
	 */
	private BdPd savePdInfo(BdPd pd, boolean ifNewPd) {
		if(CommonUtils.isEmptyString(pd.getPkPdgn()))
			throw new BusException("维护药品时，请先选择通用名称！");
		else if(CommonUtils.isEmptyString(pd.getDtDosage()))
			throw new BusException("维护药品时，请选择剂型！");
		
		//1.1、校验 药品名称+规格+生产厂家全局不唯一
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("select count(*) from bd_pd where del_flag='0' and name =? and spec =? and pk_factory =? ");
		
		Object[] obj = new Object[]{pd.getName(),pd.getSpec(),pd.getPkFactory()};
		if(!ifNewPd){
			strBuf.append(" and pk_pd !=? ");
			obj = new Object[]{pd.getName(),pd.getSpec(),pd.getPkFactory(),pd.getPkPd()};
		}
		int nameSpec_count = DataBaseHelper.queryForScalar( strBuf.toString(), Integer.class,obj );
		if (nameSpec_count != 0) 
			throw new BusException("药品名称+规格+生产厂家全局不唯一！");
		
		//1.2、【2018-04-28】药品编码自动获取
		String newCode = "";
		if(ifNewPd){
			newCode = getNewPdCode(pd.getPkPdgn(), pd.getDtDosage());//新增时直接获取新编码
		}else{
			int count = DataBaseHelper.queryForScalar("select count(1) from bd_pd "
					+ "where pk_pdgn = ? and dt_dosage = ? and del_flag = '0' and pk_pd = ? ",Integer.class, 
					pd.getPkPdgn(),pd.getDtDosage(),pd.getPkPd());
			if(count == 1) //通用名称和剂型未发生改变不予以修改；否则获取新编码
				newCode = pd.getCode();
			else
				newCode = getNewPdCode(pd.getPkPdgn(), pd.getDtDosage());
		}
		if(CommonUtils.isEmptyString(newCode))
			throw new BusException("获取编码为空，保存药品失败！");
		pd.setCode(newCode);
		
		//1.3、更新|插入药品信息
		if (pd.getPkPd() == null) 
			DataBaseHelper.insertBean(pd);
		else 
			DataBaseHelper.updateBeanByPk(pd, false);
		return pd;
	}
	/**
	 * 2、保存 药品包装信息
	 * @param pdConvertList
	 * @param ifNewPd
	 * @param pkPd
	 */
	private void savePdConverList(List<BdPdConvert> pdConvertList, boolean ifNewPd, String pkPd) {
		if (ifNewPd) { // 新增药品信息的状态下
			if (pdConvertList == null || pdConvertList.size() == 0)
				throw new BusException("请插入至少一条包装规格记录！");
			if (!checkOpIpSpecNum(pdConvertList))
				throw new BusException("包装页签下请至少包含一条门诊默认=true和住院默认=true的记录！");
			if (!chkUnitByOnly(pdConvertList))
				throw new BusException("包装单位重复！");
			for (BdPdConvert convert : pdConvertList) {
				convert.setPkPd(pkPd);
				DataBaseHelper.insertBean(convert);
			}
		} else {
			// 修改药品信息的状态下(如果前台未点击包装Tab标签查询包装信息并对包装信息做修改，前台所传pdConvertList==null)
			if (pdConvertList != null) {
				if (pdConvertList.size() == 0)
					throw new BusException("包装页签下请至少包含一条包装记录！");
				if (!checkOpIpSpecNum(pdConvertList))
					throw new BusException( "包装页签下请至少包含一条门诊默认=true和住院默认=true的记录！");
				if (!chkUnitByOnly(pdConvertList))
					throw new BusException("包装单位重复！");

				// 1)插入新增，2)更新修改，3)删除不在当前List中的
				DataBaseHelper .update("update bd_pd_convert set del_flag = '1' where pk_pd = ?", new Object[] { pkPd });
				String pkCoverts = "";
				for (BdPdConvert convert : pdConvertList) {
					if (convert.getPkPdconvert() == null) {
						convert.setPkPd(pkPd);
						DataBaseHelper.insertBean(convert);
					} else {
						DataBaseHelper.updateBeanByPk(convert, false);
					}
					pkCoverts = pkCoverts + "'" + convert.getPkPdconvert() + "',";// 用于删除已删除的
				}
				if (!CommonUtils.isEmptyString(pkCoverts)) {
					DataBaseHelper.update( "delete from bd_pd_convert where pk_pd = ? "
							+ "and pk_pdconvert not in (" + pkCoverts.substring(0, pkCoverts.length() - 1) + ")", new Object[] { pkPd });
				}
			}
		}
	}
	/**
	 * 3、药品别名保存
	 * @param pdAsList
	 * @param ifNewPd
	 * @param pkPd
	 */
	private void savePdAsList(List<BdPdAs> pdAsList, boolean ifNewPd, String pkPd) {
		if (ifNewPd) {
			if (pdAsList == null || pdAsList.size() == 0)
				throw new BusException("请插入至少一条别名记录！");
			for (BdPdAs alia : pdAsList) {
				alia.setPkPd(pkPd);
				DataBaseHelper.insertBean(alia);
			}
		} else{
			if (pdAsList != null) {
				if (pdAsList.size() == 0)
					throw new BusException("别名页签下请至少包含一条别名记录！");
				
				// 1)插入新增，2)更新修改，3)删除不在当前List中的
				String pkAsList = "";
				for (BdPdAs alia : pdAsList) {
					if (alia.getPkPdas() == null) {
						alia.setPkPd(pkPd);
						DataBaseHelper.insertBean(alia);
					} else {
						DataBaseHelper.updateBeanByPk(alia, false);
					}
					pkAsList = pkAsList + "'" + alia.getPkPdas() + "',";
				}
				if (!CommonUtils.isEmptyString(pkAsList)) {
					DataBaseHelper.update( "delete from bd_pd_as where pk_pd = ? "
							+ "and pk_pdas not in (" + pkAsList.substring(0, pkAsList.length() - 1) + ")", new Object[] { pkPd });
				}
			}
		}
	}
	/**
	 * 4、保存 药品属性
	 * @param pdAttList
	 * @param pkPd
	 */
	private void savePdAttList(List<BdPdAtt> pdAttList, String pkPd) {
		if (pdAttList != null && pdAttList.size() != 0) {
			pdAttList = getPkPdAttByPkPdattdef(pdAttList, pkPd);
			for (BdPdAtt att : pdAttList) {
				System.out.println("----------"+att.getPkPdatt()+"----------");
				if (att.getPkPdatt() != null) {
					att.setPkPd(pkPd);
					DataBaseHelper.updateBeanByPk(att, false);
				} else {
					att.setPkPd(pkPd);
					DataBaseHelper.insertBean(att);
				}
			}
		}
	}
	/**
	 * 5、保存 药品 分配信息 ： 新增(插入全部),更新(删除全部，若传入不为空则插入)
	 * @param pdStoreList
	 * @param ifNewPd
	 * @param pkPd
	 */
	private void savePdStoreList(List<BdPdStore> pdStoreList, boolean ifNewPd, String pkPd) {
		if (ifNewPd) {
			if (pdStoreList != null && pdStoreList.size() != 0) {
				for (BdPdStore pdstore : pdStoreList) {
					pdstore.setPkPd(pkPd);
					Map<String,Object> convMap=choosePkPdconvertByStore( pdstore.getPkStore(), pkPd);
					pdstore.setPkPdconvert(convMap.get("pkPdconvert").toString());
					pdstore.setFlagStop("0");// 默认启用
					DataBaseHelper.insertBean(pdstore);
				}
			}
		} else {
			if (pdStoreList != null) {
				DataBaseHelper.execute( "delete from bd_pd_store where pk_pd = ?", new Object[] { pkPd });
				if (pdStoreList.size() != 0) {
					for (BdPdStore pdstore : pdStoreList) {
						pdstore.setPkPd(pkPd);
						Map<String,Object> convMap=choosePkPdconvertByStore( pdstore.getPkStore(), pkPd);
						pdstore.setPkPdconvert(convMap.get("pkPdconvert").toString());
						pdstore.setFlagStop("0");// 默认启用
						DataBaseHelper.insertBean(pdstore);
					}
				} 
			}
		}
	}
	/**
	 * 校验药品 - 包装单位是否唯一
	 * @param pdConvertList
	 * @return
	 */
	private boolean chkUnitByOnly(List<BdPdConvert> pdConvertList) {
		Map<String, String> unitMap = new HashMap<String, String>();
		int len = pdConvertList.size();
		for (int i = 0; i < len; i++) {
			String pkUnit = pdConvertList.get(i).getPkUnit();
			if (unitMap.containsKey(pkUnit)) { 
				return false;
			}
			unitMap.put(pkUnit, pdConvertList.get(i).getPkPdconvert());
		}
		return true;
	}
	
	/**
	 * 2018-04-28 根据通用名称 + 剂型 获取最新的药品编码 
	 * @param pkPdgn
	 * @param dtDosage
	 * @return
	 */
	private String getNewPdCode(String pkPdgn, String dtDosage) {
		
		Map<String,Object> codeMap=DataBaseHelper.queryForMap("select code from bd_pd_gn where pk_pdgn = ? and del_flag = '0' ", new Object[]{pkPdgn});
		String code_pdgn ="";
		if(codeMap!=null && codeMap.get("code")!=null){
			code_pdgn=codeMap.get("codePdgn").toString();
		}
		if(CommonUtils.isEmptyString(code_pdgn))
			throw new BusException("未获取到主键为【"+pkPdgn+"】的通用名称编码！");
		
		String newCode = code_pdgn + "-" + dtDosage.substring(0,2);
		Map<String,Object> codeProMap=DataBaseHelper.queryForMap("select max(code) code as code from bd_pd where pk_pdgn = ? and dt_dosage = ? and del_flag = '0' ", new Object[]{pkPdgn,dtDosage});
		String codePro = "";
		if(codeProMap!=null && codeProMap.get("code")!=null){
			codePro=codeProMap.get("code").toString();
		}
		if(null == codePro || CommonUtils.isEmptyString(codePro))
			newCode = newCode + "-001";
		else{
			int num = Integer.parseInt(codePro.substring(18, 21)) + 1;
			String num_txt = GetFormatStrFromInt(num,3);
			newCode = newCode + "-"+ num_txt;
		}
		return newCode;
	}
	/**
	 * 2018-04-28 将数字转换成固定位数的字符串，不够往前补零
	 * @param num
	 * @param count
	 * @return
	 */
	private String GetFormatStrFromInt(int num , int count) {
		NumberFormat formatter = NumberFormat.getNumberInstance(); 
		formatter.setMinimumIntegerDigits(count);//几位数，前面补0 
		formatter.setGroupingUsed(false); 
		String num_txt = formatter.format(num);
		return num_txt;
	}
	/**
	 * 通过pkpd查询计费策略
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CgdivItemVo> qryCgdiv(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String pkItem = (String)map.get("pkPd");		
		List<CgdivItemVo> qryCgDiv = scmPubMapper.qryCgDiv(pkItem);
		return qryCgDiv;
	}
	
	/**
	 * 药品字典维护-查询数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryBdPdDicList(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		if(paramMap.get("name")!=null){
			String name = (String)paramMap.get("name");
			paramMap.put("name", name.toUpperCase());
		}
		if(CommonUtils.isNotNull(paramMap.get("dtPharm"))){
			Set<String> codePharms=new HashSet<>();
			Set<String> resCodePharm=new HashSet<>();
			String code=paramMap.get("dtPharm").toString();
			codePharms.add(code);
			resCodePharm.add(code);
			boolean isChk=true;
			do {
				StringBuffer sql=new StringBuffer();
				sql.append("select doc.code from bd_defdoc doc where doc.code_defdoclist='030402' and  doc.code_parent in ("+CommonUtils.convertSetToSqlInPart(codePharms, "code")+")");
				List<Map<String,Object>> respharmList=DataBaseHelper.queryForList(sql.toString(), new Object[]{});
				
				if(respharmList==null ||respharmList.size()==0){
					isChk=false;
				}else{
					codePharms=new HashSet<String>();
					for (Map<String, Object> map : respharmList) {
						resCodePharm.add(map.get("code").toString());
						codePharms.add(map.get("code").toString());
					}
				}
			} while (isChk);
			paramMap.put("codePharms", resCodePharm);
		}
		
		return scmPubMapper.qryBdPdDicList(paramMap);
	}
	
	/**
	 *交易号：008001001084
	 * 查询本仓库对应的货位字典
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdStorePosi> qryStorePosiList(String param,IUser user){
		User newUser=(User)user;
		String pkStore=newUser.getPkStore();
		return scmPubMapper.qryStorePosiList(pkStore);
	}
	
	/**
	 * 交易号：008001001085
	 * 保存仓库货位信息
	 * @param param
	 * @param user
	 */
	public void saveStorePosiInfo(String param,IUser user){
		List<BdStorePosi> posiList=JsonUtil.readValue(param, new TypeReference<List<BdStorePosi>>() {});
		if(posiList==null){
			posiList=new ArrayList<BdStorePosi>();
		}
		User newUser=(User)user;
		String delSql="delete from bd_store_posi where pk_store=?";
		DataBaseHelper.execute(delSql, new Object[]{newUser.getPkStore()});
		
		for (int i = 0; i < posiList.size(); i++) {
			posiList.get(i).setPkOrg(((User)user).getPkOrg());
			posiList.get(i).setPkPosi(NHISUUID.getKeyId());
		}
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdStorePosi.class), posiList);
	}
	
	/**
	 * 008001001086
	 * 校验当前包装单位存在未完成出库的记录
	 * @param param
	 * @param user
	 */
	public void checkUnitOut(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return;
		int count=scmPubMapper.checkUnitOut(paramMap);
		if(count>0){
			//throw new BusException("当前包装单位存在未完成出库的记录!");
		}
	}
	
	/**
	 * 交易号：008001001087
	 * 保存记费策略模板
	 * @param param
	 * @param user
	 */
	public void saveCgDivTmp(String param,IUser user){
		String cgdivTmps=JsonUtil.getJsonNode(param, "cgdivTmps").toString();
		List<BdHpCgdivTmp> cgdivTmpList=JsonUtil.readValue(cgdivTmps, new TypeReference<List<BdHpCgdivTmp>>() {});
		if(cgdivTmpList==null || cgdivTmpList.size()<=0)return;
		
		String status=JsonUtil.getFieldValue(param, "status");
		if("INSERT".equals(status)){//校验模板编码和模板名称
			String sql="select count(1) from bd_hp_cgdiv_tmp where del_flag='0' and code_tmp=?";
			int count=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{cgdivTmpList.get(0).getCodeTmp()});
			if(count>0){
				throw new BusException("模板编码重复！");
			}
			sql="select count(1) from bd_hp_cgdiv_tmp where del_flag='0' and name_tmp=?";
			count=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{cgdivTmpList.get(0).getNameTmp()});
			if(count>0){
				throw new BusException("模板名称重复！");
			}
		}
		//删除同一模板编码的所有记费策略模板
		String delSql="delete from bd_hp_cgdiv_tmp where code_tmp=?";
		DataBaseHelper.execute(delSql, new Object[]{cgdivTmpList.get(0).getCodeTmp()});
		
		User doUser=(User)user;
		//重新保存当前模板编码的记费策略
		for(int i=0;i<cgdivTmpList.size();i++){
			cgdivTmpList.get(i).setPkHpcgdivtmp(NHISUUID.getKeyId());
			cgdivTmpList.get(i).setPkOrg("~                               ");
			cgdivTmpList.get(i).setCreator(doUser.getId());
			cgdivTmpList.get(i).setTs(new Date());
			cgdivTmpList.get(i).setCreateTime(new Date());
			cgdivTmpList.get(i).setDelFlag("0");
		}
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdHpCgdivTmp.class), cgdivTmpList);
	}


	/**
	 * 交易号：008001001088
	 * 查询记费策略模板
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdHpCgdivTmp> qryCgDivTmp(String param,IUser user){
		return scmPubMapper.qryCgdivTmp();
	}

	/**
	 * 交易号:008001001089
	 * 查询记费策略模板明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryCgDivTmpDt(String param,IUser user){
		String codeTmp=JsonUtil.getFieldValue(param, "codeTmp");
		if(codeTmp==null||"".equals(codeTmp))return null;
		
		return scmPubMapper.qryCgDivTmpDt(codeTmp);
	}
	
	/**
	 * 交易号：008001001090
	 * 删除记费模板
	 * @param param
	 * @param user
	 */
	public void delCgDivTmp(String param,IUser user){
		String codeTmp=JsonUtil.getFieldValue(param, "codeTmp");
		if(codeTmp==null||"".equals(codeTmp))return ;
		String delSql="delete from bd_hp_cgdiv_tmp where code_tmp=?";
		DataBaseHelper.execute(delSql, new Object[]{codeTmp});
	}
	
	/**
	 * 008001001091
	 * 保存记费策略数据
	 * @param param
	 * @param user
	 */
	public void saveHpCgDiv(String param,IUser user){
		List<BdHpCgdivItem> itemList=JsonUtil.readValue(param, new TypeReference<List<BdHpCgdivItem>>() {});
		if(itemList==null||itemList.size()<=0)return;
		
		String pkPd=itemList.get(0).getPkItem();
		String delSql="delete from bd_hp_cgdiv_item where del_flag='0' and pk_item=?";
		DataBaseHelper.execute(delSql, new Object[]{pkPd});
		
		for(int i=0;i<itemList.size();i++){
			itemList.get(i).setPkHpcgdivitem(NHISUUID.getKeyId());
			itemList.get(i).setFlagPd("1");
			itemList.get(i).setDelFlag("0");
			itemList.get(i).setPkOrg("~                               ");//机构
			itemList.get(i).setTs(new Date());
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdHpCgdivItem.class),itemList);
	}
	
	/**
	 * 008001001093
	 * 校验当前药品所在的仓库中是否发生业务数据
	 * @param param{"pkPd":"药品主键","pkStore":"药品仓库主键"}
	 * @param user
	 */
	public void checkStorePdRef(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return;
		Object[] obj=new Object[]{paramMap.get("pkPd"),paramMap.get("pkStore")};
		//采购计划数据业务校验
	    String planSql="select count(1) from pd_plan pla inner join pd_plan_detail pladt on pla.pk_pdplan=pladt.pk_pdplan where pla.pk_store=? and pladt.pk_pd=?";
	    int planCount=DataBaseHelper.queryForScalar(planSql, Integer.class, obj);
	    if(planCount>0){
	    	throw new BusException("当前药品在所选仓库已经发生采购业务，不能取消!");
	    }
	    //出入库数据业务校验
	    String stSql="select count(1) from pd_st st inner join pd_st_detail stdt on st.pk_pdst=stdt.pk_pdst where st.pk_store_st=? and stdt.pk_pd=? ";
	    int stCount=DataBaseHelper.queryForScalar(stSql, Integer.class, obj);
	    if(stCount>0){
	    	throw new BusException("当前药品在所选仓库已经发生出入库业务，不能取消！");
	    }
	    
	    //对应药房存在库存数据
	    String stockSql="select count(1) from pd_stock sto where sto.pk_store=? and sto.pk_pd=?";
	    int stockCount=DataBaseHelper.queryForScalar(stockSql, Integer.class, obj);
	    if(stockCount>0){
	    	throw new BusException("当前药品在所选仓库已经发生库存业务，不能取消！");
	    }
	    //盘点业务
	    String invSql="select count(1) from pd_inventory inv  inner join pd_inv_detail invdt on inv.pk_pdinv=invdt.pk_pdinv where inv.pk_store=? and invdt.pk_pd=?";
	    int invCount=DataBaseHelper.queryForScalar(invSql, Integer.class, obj);
	    if(invCount>0){
	    	throw new BusException("当前药品在所选仓库发生盘点业务，不能取消！");
	    }
	    //结账业务
	    String occSql="select count(1) from pd_cc cc inner join pd_cc_detail ccdt on cc.pk_pdcc=ccdt.pk_pdcc where cc.pk_store=? and ccdt.pk_pd=?";
	    int occCount=DataBaseHelper.queryForScalar(occSql, Integer.class, obj);
	    if(occCount>0){
	    	throw new BusException("当前药品在所选仓库发生结账业务，不能取消！");
	    }
	}
	/**
	 * 008001001094
	 * 供应链药品检索功能实现
	 * @param param
	 * @param user
	 * @return
	 */
	public List<String> qryPdSearch(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null){
			return null;
		}
		List<String> resList=scmPubMapper.qryPdSearch(paramMap);
		return resList;
	}
	
	/**
	 * 008001001096
	 * 通过序号发生器自动获取编码规则
	 * @param param{"table":"表名","file":"表字段","valInit":"初始值","count":"变化数"}  
	 * @param user
	 * @return
	 * 注：表名，字段名大写
	 */
	public String makeSeralno(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		String sql="select * from bd_serialno where name_tb=? and name_fd=?";
		BdSerialnoBase existInt=DataBaseHelper.queryForBean(sql, BdSerialnoBase.class, new Object[]{paramMap.get("table"),paramMap.get("file")});
		BdSerialnoBase serialno=new BdSerialnoBase();
		String resCode="";
		if(existInt!=null){//更新val

			Integer valMax=ApplicationUtils.getSerialNo(CommonUtils.getString(paramMap.get("table")),CommonUtils.getString(paramMap.get("file")),1);
			resCode=CommonUtils.getString(existInt.getPrefix(), "");
			if(existInt.getLength()!=null&&CommonUtils.getDouble(existInt.getLength())>0 && valMax!=null){
				String val=CommonUtils.getString(valMax,"");
				while (val.length()<CommonUtils.getDouble(existInt.getLength())){
					val="0"+val;
				}
				resCode=resCode+val;
			}else {
				resCode = resCode + CommonUtils.getString(valMax, "");
			}
			return resCode;
		}else{//新增
			serialno.setPkSerialno(NHISUUID.getKeyId());
			serialno.setPkOrg("~                               ");
			serialno.setNameTb(paramMap.get("table").toString());
			serialno.setNameFd(paramMap.get("file").toString());
			serialno.setValInit(new BigDecimal(paramMap.get("valInit").toString()));
			serialno.setVal(serialno.getValInit());
			ApplicationUtils.setDefaultValue(serialno, true);
			DataBaseHelper.insertBean(serialno);
			return serialno.getVal().toString();
		}
	}
	
	public List<Map<String,Object>> selectUniversalName(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)paramMap=new HashMap<String,Object>();
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		return scmPubMapper.selectUniversalName(paramMap);
	}
	
	/**
	 * 008001001098
	 * 控制药品是否被库存引用
	 * @param param
	 * @param user
	 * @return
	 */
	public int getStockImportbyPkPd(String param,IUser user){
		String pkPd=JsonUtil.getFieldValue(param, "pkPd");
		if(CommonUtils.isEmptyString(pkPd)){
			return 0;
		}else{
			String sql="select count(1) from pd_stock where pk_pd=?";
			Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkPd});
			if(count==null){
				return 0;
			}else{
				return count;
			}
			
		}
	}

/***
 * @Description  批量设置属性
 * @auther wuqiang
 * @Date 2020-04-21
 * @Param [param, user]
 * @return void
 */
	public void bachInsertBdPdAtt(String param,IUser user){
		PdAndAllParam pdAndAll = JsonUtil.readValue(param, PdAndAllParam.class);
		List<BdPd> pkPds= pdAndAll.getBdPdList();
		List<BdPdAtt> bdPdAtts= pdAndAll.getPdAttList();
		List<String> del=new ArrayList<>();
		List<BdPdAtt> insBdPdAtt=new ArrayList<>(16);
		Map<String,Object> mapDef = new HashMap<>();
		List<String> pdattdefList = new ArrayList<>();
		String pkPdattdef = "";
		for (BdPd pkPd:pkPds){
			for ( int i=0;i<bdPdAtts.size();i++){
				String sql="delete from BD_PD_ATT where PK_PD='"+pkPd.getPkPd()+"' and PK_PDATTDEF='"+bdPdAtts.get(i).getPkPdattdef()+"'";
				del.add(sql);
				if (StringUtils.isBlank(bdPdAtts.get(i).getValAtt())){
					continue;
				}
				BdPdAtt bdPdAtt1=new BdPdAtt();
				bdPdAtt1.setPkPd(pkPd.getPkPd());
				bdPdAtt1.setPkPdattdef(bdPdAtts.get(i).getPkPdattdef());
				bdPdAtt1.setValAtt(bdPdAtts.get(i).getValAtt());
				ApplicationUtils.setDefaultValue(bdPdAtt1,true);
				insBdPdAtt.add(bdPdAtt1);
				pdattdefList.add(bdPdAtts.get(i).getPkPdattdef());
			}
		}
		List<Map<String,Object>> mapList = new ArrayList<>();
		mapDef.put("pkPdattdefs",pdattdefList);
		if(pdattdefList.size() > 0){
			List<BdPdAttDefine>  bdPdAttDefineList = scmPubMapper.quePdattdef(mapDef);
			if(bdPdAttDefineList.size() > 0){
				for (BdPdAtt bdPdAtt:insBdPdAtt){
					for(BdPdAttDefine bdPdAttDefine:bdPdAttDefineList){
						if(bdPdAtt.getPkPdattdef().equals(bdPdAttDefine.getPkPdattdef()) && "BA11".equals(bdPdAttDefine.getCodeAtt())){
							Map<String,Object> map = new HashMap<>();
							map.put("pkPd",bdPdAtt.getPkPd());
							if("1".equals(bdPdAtt.getValAtt())){
								map.put("isBA11","1");
							}else if("0".equals(bdPdAtt.getValAtt())){
								map.put("isBA11","0");
							}
							pkPdattdef = bdPdAtt.getPkPdattdef();
							mapList.add(map);
						}
					}
				}
			}
		}

		if(mapList.size() > 0){
			for(BdPd pkPd:pkPds){
				for(Map<String,Object> map:mapList){
					String pdName = pkPd.getName();
					String queSql = "select * from bd_pd_att where pk_pd = ? and pk_pdattdef = ?";
					BdPdAtt bdPdAtt = DataBaseHelper.queryForBean(queSql,BdPdAtt.class,pkPd.getPkPd(),pkPdattdef);
					if(pkPd.getPkPd().equals(map.get("pkPd")) && "1".equals(map.get("isBA11"))){
						pdName = "G"+pdName;
						if(!"1".equals(bdPdAtt.getValAtt())){
							String UpdSql = "update bd_pd set name = ? where pk_pd = ?";
							DataBaseHelper.update(UpdSql, new Object[] { pdName , pkPd.getPkPd()});
						}
					}else if(pkPd.getPkPd().equals(map.get("pkPd")) && "0".equals(map.get("isBA11"))){
						pdName = pdName.substring(1,pdName.length());
						if(!"0".equals(bdPdAtt.getValAtt())){
							String UpdSql = "update bd_pd set name = ? where pk_pd = ?";
							DataBaseHelper.update(UpdSql, new Object[] { pdName , pkPd.getPkPd()});
						}
					}

				}
			}
		}


		DataBaseHelper.batchUpdate(del.toArray(new String[0]));
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdAtt.class), insBdPdAtt);
	}

	/**
	 * 008001001101
	 * 仓库药品批量上传
	 * @param param
	 * @param user
	 * @return
	 */
	public void storeDrugBatchSend(String param, IUser user){
		List<BdPdStore> pdstoreList = JsonUtil.readValue(param, new TypeReference<List<BdPdStore>>() {});
		ExtSystemProcessUtils.processExtMethod("PackMachineOp", "getMedInfo",pdstoreList);
	}

	/***
	 * 008005001015
	 *查询药品各库存信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> queryForPdStockInfo(String param,IUser user){
		List<String> pdList=JsonUtil.readValue(param, new TypeReference<List<String>>() {});
		if(pdList==null || pdList.size()==0)return null;
		Set<String> pkPdSet = new HashSet<>(pdList);
		String pkPds = CommonUtils.convertSetToSqlInPart(pkPdSet, "stk.pk_pd");
		List<Map<String,Object>> reslist=scmPubMapper.queryForPdStockInfo(pkPds);
		Map<String ,Object> resMap=new HashMap<>();
		if(reslist!=null &&reslist.size()>0){
			for(Map<String,Object> map : reslist){
				String key=CommonUtils.getString(map.get("pkPd"));
				if(resMap.containsKey(key)){
					StringBuilder value=new StringBuilder(CommonUtils.getString(resMap.get(key)));
					value.append("||");
					value.append(map.get("nameStore"));
					value.append(map.get("quan"));
					value.append(map.get("unitName"));
					resMap.put(key,value);
				}else{
					StringBuilder value=new StringBuilder();
					value.append(map.get("nameStore"));
					value.append(map.get("quan"));
					value.append(map.get("unitName"));
					resMap.put(key,value);
				}
			}
		}
		return resMap;// scmPubMapper.queryForPdStockInfo(pdList);
	}
	public List<BdPdAtt> getPkPdAttByPkPdattdef(List<BdPdAtt> pdAttList,String pkPd){
		String pkPdattdefs="";
		for (int i=0;i<pdAttList.size();i++){
			if (i==pdAttList.size()-1){
				pkPdattdefs+="'"+pdAttList.get(i).getPkPdattdef()+"'";
			}else{
				pkPdattdefs+="'"+pdAttList.get(i).getPkPdattdef()+"',";
			}
		}
		List<BdPdAtt> pdAtts=DataBaseHelper.queryForList("select * from BD_PD_ATT where PK_PD='"+pkPd+"' and PK_PDATTDEF in ("+pkPdattdefs+")",BdPdAtt.class);
		if (pdAtts!=null&&pdAtts.size()>0){
			for (BdPdAtt BdPdAtt : pdAttList){
				for (BdPdAtt DbBdPdAtt : pdAtts){
					if (DbBdPdAtt.getPkPdattdef().equals(BdPdAtt.getPkPdattdef())){
						BdPdAtt.setPkPdatt(DbBdPdAtt.getPkPdatt());
					}
				}
			}
		}
		return pdAttList;
	}
}
