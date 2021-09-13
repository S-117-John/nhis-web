package com.zebone.nhis.base.bd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.zebone.nhis.base.bd.dao.PriceMapper;
import com.zebone.nhis.base.bd.vo.BdDaycgSetAndItemParam;
import com.zebone.nhis.base.bd.vo.BdDaycgSetVo;
import com.zebone.nhis.base.bd.vo.HpAndAllParam;
import com.zebone.nhis.base.bd.vo.InvcateItemAndItemCatesParam;
import com.zebone.nhis.common.module.base.bd.code.BdDictattr;
import com.zebone.nhis.common.module.base.bd.price.BdDaycgSet;
import com.zebone.nhis.common.module.base.bd.price.BdDaycgSetItem;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdHpDept;
import com.zebone.nhis.common.module.base.bd.price.BdHpDivconfig;
import com.zebone.nhis.common.module.base.bd.price.BdHpItemcatediv;
import com.zebone.nhis.common.module.base.bd.price.BdHpItemdiv;
import com.zebone.nhis.common.module.base.bd.price.BdHpOrg;
import com.zebone.nhis.common.module.base.bd.price.BdHpSecdiv;
import com.zebone.nhis.common.module.base.bd.price.BdHpTotaldiv;
import com.zebone.nhis.common.module.base.bd.price.BdInvcate;
import com.zebone.nhis.common.module.base.bd.price.BdInvcateItem;
import com.zebone.nhis.common.module.base.bd.price.BdInvcateItemcate;
import com.zebone.nhis.common.module.base.bd.price.BdPayer;
import com.zebone.nhis.common.module.base.bd.price.BdPricetypeCfg;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 价格策略
 * @author Xulj
 *
 */
@Service
public class PriceService {

	@Autowired
	private PriceMapper priceMapper;
	
	public List<BdDaycgSetVo> queryDayCgSet(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String sql = "select s.*,dept.NAME_DEPT from BD_DAYCG_SET s left join BD_OU_DEPT dept on s.PK_DEPT=dept.PK_DEPT  where s.del_flag='0' " ;
		if(paramMap!=null&&CommonUtils.isNotNull(paramMap.get("pkOrg"))){
		  	sql = sql +" and s.pk_org = '"+CommonUtils.getString(paramMap.get("pkOrg"))+"' ";
		}
		sql = sql + " order by code";
		List<BdDaycgSetVo>  list = DataBaseHelper.queryForList(sql,BdDaycgSetVo.class,new Object[0]);
		if(list !=null&&list.size()>0){
			for(BdDaycgSetVo set : list){
				if(CommonUtils.isNotNull(set.getCodeHps())&&",".equals(set.getCodeHps().substring(0, 1))&&",".equals(set.getCodeHps().substring(set.getCodeHps().length()-1, set.getCodeHps().length()))){
					String codeHps = set.getCodeHps().substring(1, set.getCodeHps().length()-1);
					set.setCodeHps(codeHps);
				}
				   
			}
		}
		return list;
	}
	
	/**
	 * 新增和更新固定收费套定义
	 * @param param
	 * @param user
	 * @return
	 */
	public BdDaycgSet saveSet(String param, IUser user){
		
		BdDaycgSet set = JsonUtil.readValue(param, BdDaycgSet.class);
		
		String pkOrg = ((User) user).getPkOrg();
		
		if(set!=null&&CommonUtils.isNotNull(set.getCodeHps())){
			set.setCodeHps(","+set.getCodeHps()+",");
		}
			
		if(set.getPkDaycgset() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_daycg_set "
					+ "where del_flag='0' and code = ? and pk_org = ?", Integer.class, set.getCode(), pkOrg);
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_daycg_set "
					+ "where del_flag='0' and name = ? and pk_org = ?", Integer.class, set.getName(), pkOrg);
			if(count_code != 0){
				throw new BusException("机构内固定收费套编码重复！");				
			}else if(count_name != 0){
				throw new BusException("机构内固定收费套名称重复！");
			}else{
				DataBaseHelper.insertBean(set);
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_daycg_set "
					+ "where del_flag='0' and code = ? and pk_org = ? and pk_daycgset != ?", Integer.class, set.getCode(), pkOrg, set.getPkDaycgset());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_daycg_set "
					+ "where del_flag='0' and name = ? and pk_org = ? and pk_daycgset != ?", Integer.class, set.getName(), pkOrg, set.getPkDaycgset());
			if(count_code != 0){
				throw new BusException("机构内固定收费套编码重复！");				
			}else if(count_name != 0){
				throw new BusException("机构内固定收费套名称重复！");
			}else{
				DataBaseHelper.updateBeanByPk(set, false);
			}
		}
		
		return set;
	}
	
	/**
	 * 删除固定收费套定义
	 * @param param
	 * @param user
	 */
	public void delSet(String param, IUser user){		
		BdDaycgSet set = JsonUtil.readValue(param, BdDaycgSet.class);

		DataBaseHelper.update("update bd_daycg_set_item set del_flag='1' where pk_daycgset = ?", new Object[]{set.getPkDaycgset()});
		DataBaseHelper.update("update bd_daycg_set set del_flag='1' where pk_daycgset = ?", new Object[]{set.getPkDaycgset()});		
	}
	
	/**
	 * 新增和更新固定收费套服务项目
	 * @param param
	 * @param user
	 * @return
	 */
	public BdDaycgSet saveBdDaycgSetAndItem(String param, IUser user){
		BdDaycgSetAndItemParam setAndItem = JsonUtil.readValue(param, BdDaycgSetAndItemParam.class);
		//主要为了获取主表主键
		BdDaycgSet daycgSet = setAndItem.getDaycgSet();
		String pkDaycgset = daycgSet.getPkDaycgset();
		
		//全部删除再插入
		DataBaseHelper.update("update bd_daycg_set_item set del_flag='1' where pk_daycgset = ?", new Object[]{pkDaycgset});
		List<BdDaycgSetItem> itemList = setAndItem.getItemList();
		if(itemList!=null && itemList.size()!=0){
			for(BdDaycgSetItem item : itemList){
				if(item.getPkDaycgitem()==null){
					item.setPkDaycgset(pkDaycgset);
					DataBaseHelper.insertBean(item); //参数中有主键时不会生成新的主键
				}else{
					item.setDelFlag("0");
					item.setPkDaycgset(pkDaycgset);
					DataBaseHelper.updateBeanByPk(item, false);
				}
			}
		}
		return daycgSet;
	}
	
	/**
	 * 保存价格策略-院内票据分类
	 * @param param
	 * @param user
	 */
	public void saveBdInvcate(String param , IUser user){
		
		BdInvcate invcate = JsonUtil.readValue(param, BdInvcate.class);
		
		String pkOrg = ((User) user).getPkOrg();
		
		if(invcate.getPkInvcate() == null){
			int count_type = DataBaseHelper.queryForScalar("select count(1) from bd_invcate "
					+ "where del_flag = '0' and eu_type = ? and pk_org = ?", Integer.class, invcate.getEuType(),pkOrg);
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_invcate "
					+ "where del_flag = '0' and code = ? and pk_org = ?", Integer.class, invcate.getCode(),pkOrg);
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_invcate "
					+ "where del_flag = '0' and name = ? and pk_org = ?", Integer.class, invcate.getName(),pkOrg);
			if(count_code == 0 && count_name == 0 && count_type == 0){
				if(invcate.getLength() == 0){//任意位数--不填票号位数的情况，即可为空，前台传0表示
					invcate.setLength(null);
				}
				DataBaseHelper.insertBean(invcate);
			}else{
				if(count_type != 0){
					throw new BusException("该机构下票据分类类型重复！");
				}
				if(count_code != 0 && count_name == 0){
					throw new BusException("该机构下票据分类编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("该机构下票据分类名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("该机构下票据分类编码和名称都重复！");
				}
			}
		}else{
			int count_type = DataBaseHelper.queryForScalar("select count(1) from bd_invcate "
					+ "where del_flag = '0' and eu_type = ? and pk_org = ? and pk_invcate != ?", Integer.class, invcate.getEuType(),pkOrg,invcate.getPkInvcate());
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_invcate "
					+ "where del_flag = '0' and code = ? and pk_org = ? and pk_invcate != ?", Integer.class, invcate.getCode(),pkOrg,invcate.getPkInvcate());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_invcate "
					+ "where del_flag = '0' and name = ? and pk_org = ? and pk_invcate != ?", Integer.class, invcate.getName(),pkOrg,invcate.getPkInvcate());
			if(count_code == 0 && count_name == 0 && count_type == 0){
				DataBaseHelper.updateBeanByPk(invcate,false);
				if(invcate.getLength() == 0){//任意位数--不填票号位数的情况，即可为空，前台传0表示
					DataBaseHelper.update("update bd_invcate set length = null where pk_invcate = ?", new Object[]{invcate.getPkInvcate()});
				}
			}else{
				if(count_type != 0){
					throw new BusException("该机构下票据分类类型重复！");
				}
				if(count_code != 0 && count_name == 0){
					throw new BusException("该机构下票据分类编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("该机构下票据分类名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("该机构下票据分类编码和名称都重复！");
				}
			}
		}
	}
	
	/**
	 * 保存票据分类内容信息和对照信息
	 * @param param
	 * @param user
	 */
	public void saveInvcateItemAndItemCates(String param , IUser user){
		
		InvcateItemAndItemCatesParam invItemAndItemCates = JsonUtil.readValue(param, InvcateItemAndItemCatesParam.class);
		BdInvcateItem invItem = invItemAndItemCates.getInvItem();
		List<BdInvcateItemcate> invcateItemcateList = invItemAndItemCates.getInvcateItemcateList();
		
		String pkOrg = ((User) user).getPkOrg();
		
		/**保存票据分类项目**/
		if(invItem.getPkInvcateitem() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_invcate_item "
					+ "where del_flag = '0' and code = ? and pk_org = ? and pk_invcate = ?", Integer.class, invItem.getCode(),pkOrg,invItem.getPkInvcate());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_invcate_item "
					+ "where del_flag = '0' and name = ? and pk_org = ? and pk_invcate = ?", Integer.class, invItem.getName(),pkOrg,invItem.getPkInvcate());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(invItem);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("【票据分类项目】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("【票据分类项目】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("【票据分类项目】编码和名称都重复！");
				}
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_invcate_item "
					+ "where del_flag = '0' and code = ? and pk_org = ? and pk_invcateitem != ? and pk_invcate = ?", Integer.class, invItem.getCode(),pkOrg,invItem.getPkInvcateitem(),invItem.getPkInvcate());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_invcate_item "
					+ "where del_flag = '0' and name = ? and pk_org = ? and pk_invcateitem != ? and pk_invcate = ?", Integer.class, invItem.getName(),pkOrg,invItem.getPkInvcateitem(),invItem.getPkInvcate());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.updateBeanByPk(invItem,false);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("【票据分类项目】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("【票据分类项目】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("【票据分类项目】编码和名称都重复！");
				}
			}
		}
		
		/**保存票据分类项目与收费服务分类对照**/
		if(invcateItemcateList != null && invcateItemcateList.size() != 0){
			/**校验重复性**/
			Map<String, String> pkItemcateMap = new HashMap<String, String>();
			int len = invcateItemcateList.size();
			for(int i = 0; i < len; i++){
				String pkItemcate = invcateItemcateList.get(i).getPkItemcate();
				if(pkItemcateMap.containsKey(pkItemcate)){
					throw new BusException("对照的收费项目基础分类字典重复！");
				}
				pkItemcateMap.put(pkItemcate, invcateItemcateList.get(i).getPkInvitem());//put-value为主键
			}
			
			//先全删再恢复的方式（软删除）
			String pkInvcateItem = invItem.getPkInvcateitem();
			DataBaseHelper.update("update bd_invcate_itemcate set del_flag = '1' where pk_invcateitem = ?",new Object[]{pkInvcateItem});
			for(BdInvcateItemcate itemcate : invcateItemcateList){
				if(itemcate.getPkInvitem() != null){
					itemcate.setDelFlag("0");//恢复
					itemcate.setPkInvcateitem(pkInvcateItem);
					DataBaseHelper.updateBeanByPk(itemcate, false);
				}else{
					itemcate.setPkInvcateitem(pkInvcateItem);
					DataBaseHelper.insertBean(itemcate);
				}
			}
		}else{
			String pkInvcateItem = invItem.getPkInvcateitem();
			DataBaseHelper.update("update bd_invcate_itemcate set del_flag = '1' where pk_invcateitem = ?",new Object[]{pkInvcateItem});
		}
	}
	
	/**
	 * 删除票据分类
	 * @param param
	 * @param user
	 */
	public void delInvcate(String param , IUser user){
		
		String pkInvcate = JsonUtil.getFieldValue(param, "pkInvcate");
		/**校验是否被 票据分类项目 引用**/
		int count_item = DataBaseHelper.queryForScalar("select count(1) from bd_invcate_item where del_flag = '0' and pk_invcate = ?", Integer.class, pkInvcate);
		int count_empinvoice = DataBaseHelper.queryForScalar("select count(1) from bl_emp_invoice where del_flag = '0' and pk_invcate = ?", Integer.class, pkInvcate);
		
		if(count_item == 0 && count_empinvoice == 0){
			DataBaseHelper.update("update bd_invcate set del_flag = '1' where pk_invcate = ?", new Object[]{pkInvcate});
		}else{
			if(count_item > 0){
				throw new BusException("票据分类已被票据分类项目引用！");
			}
			if(count_empinvoice > 0){
				throw new BusException("票据分类已被票据领用引用！");
			}
		}
	}
	
	/**
	 * 删除票据分类项目及对照
	 * @param param
	 * @param user
	 */
	public void delInvcateItemcate(String param , IUser user){
		
		String pkInvcateitem = JsonUtil.getFieldValue(param, "pkInvcateitem");
		
		DataBaseHelper.update("update bd_invcate_itemcate set del_flag = '1' where pk_invcateitem = ?", new Object[]{pkInvcateitem});
		DataBaseHelper.update("update bd_invcate_item set del_flag = '1' where pk_invcateitem = ?", new Object[]{pkInvcateitem});
		
	}
	
	/**
	 * 删除付款方信息
	 * @param param
	 * @param user
	 */
	public void delPayer(String param , IUser user){
		
		String pkPayer = JsonUtil.getFieldValue(param, "pkPayer");
		/**校验是否被 医保计划 引用**/
		int count = DataBaseHelper.queryForScalar("select count(1) from bd_hp where del_flag = '0' and pk_payer = ?", Integer.class, pkPayer);
		
		if(count == 0){
			DataBaseHelper.update("update bd_payer set del_flag = '1' where pk_payer = ?", new Object[]{pkPayer});
		}else{
			throw new BusException("付款方信息已被医保计划引用！");
		}
	}
	
	/**
	 * 保存付款方信息
	 * @param param
	 * @param user
	 */
	public void savePayerlist(String param , IUser user){
		
		List<BdPayer> payerlist = JsonUtil.readValue(param, new TypeReference<List<BdPayer>>() {
		});
		
		Map<String, String> codemap = new HashMap<String, String>();
		Map<String, String> namemap = new HashMap<String, String>();
		
		/**校验---1.校验前台所传的list的每一条编码和名称的唯一性*/
		if(payerlist != null && payerlist.size() > 0){
			int len = payerlist.size();
			for(int i = 0; i<len; i++){
				String code = payerlist.get(i).getCode();
				String name = payerlist.get(i).getName();
				if(codemap.containsKey(code)){
					throw new BusException("付款方信息编码重复！");
				}
				if(namemap.containsKey(name)){
					throw new BusException("付款方信息名称重复！");
				}
			    codemap.put(code, payerlist.get(i).getPkPayer());
			    namemap.put(name, payerlist.get(i).getPkPayer());
				
			}

			/**查询数据库中所有*/
			String pkOrg = ((User) user).getPkOrg();
			List<BdPayer> allist = this.priceMapper.findAllPayers(pkOrg);
			
			/**校验---2.校验前台所传的list的每一条编码和名称是否和数据库重复*/
			for(BdPayer dataPayer : allist){
				if(codemap.containsKey(dataPayer.getCode())){
					String pkpayer = codemap.get(dataPayer.getCode());
					if(pkpayer == null){  //新增情况
						throw new BusException("付款方信息编码在数据库中已存在！");
					}else{                //修改情况
						if(!dataPayer.getPkPayer().equals(pkpayer)){
							throw new BusException("付款方信息编码在数据库中已存在！");
						}
					}
				}
				if(namemap.containsKey(dataPayer.getName())){
					String pkpayer = namemap.get(dataPayer.getName());
					if(pkpayer == null){  //新增情况
						throw new BusException("付款方信息名称在数据库中已存在！");
					}else{                //修改情况
						if(!dataPayer.getPkPayer().equals(pkpayer)){
							throw new BusException("付款方信息名称在数据库中已存在！");
						}
					}
				}
			}
	
			/**新增或更新到数据库*/
			for(BdPayer payer : payerlist){
				if(payer.getPkPayer() == null){
					payer.setPkOrg("~");
					DataBaseHelper.insertBean(payer);
				}else{
					DataBaseHelper.updateBeanByPk(payer,false);
				}
			}
		}
		
	}
	
	/**
	 * 保存医保计划及相关
	 * @param param
	 * @param user
	 */
	public void saveHpAndAll(String param , IUser user){
		
		HpAndAllParam hpAndAll = JsonUtil.readValue(param, HpAndAllParam.class);
		BdHp hp = hpAndAll.getHpInfo();
		BdHpTotaldiv totaldiv = hpAndAll.getTotaldivInfo();
		List<BdHpItemcatediv> itemcatedivList = hpAndAll.getItemcatedivList();
		List<BdHpItemdiv> itemdivList = hpAndAll.getItemdivList();
		List<BdHpSecdiv> secdivList = hpAndAll.getSecdivList();
		//List<BdHpDiagdiv> diagdivList = hpAndAll.getDiagdivList();
		//平台发消息 state 新增：_ADD 更新：_UPDATE 删除：_DELETE	
		String state = "";
		
		String pkOrg = ((User) user).getPkOrg();
		/**保存医保计划*/
		if(hp.getPkHp() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_hp "
					+ "where del_flag = '0' and code = ? and pk_org = ?", Integer.class, hp.getCode(),pkOrg);
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_hp "
					+ "where del_flag = '0' and name = ? and pk_org = ?", Integer.class, hp.getName(),pkOrg);
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(hp);
				state = "_ADD";//平台发消息
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("【医保计划】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("【医保计划】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("【医保计划】编码和名称都重复！");
				}
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_hp "
					+ "where del_flag = '0' and code = ? and pk_org = ? and pk_hp != ?", Integer.class, hp.getCode(),pkOrg,hp.getPkHp());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_hp "
					+ "where del_flag = '0' and name = ? and pk_org = ? and pk_hp != ?", Integer.class, hp.getName(),pkOrg,hp.getPkHp());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.updateBeanByPk(hp,false);
				state = "_UPDATE";//平台发消息
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("【医保计划】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("【医保计划】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("【医保计划】编码和名称都重复！");
				}
			}
		}
		
		/**保存总额策略*/
		if(totaldiv != null){
			if(totaldiv.getPkTotaldiv() == null){
				totaldiv.setPkHp(hp.getPkHp());
				DataBaseHelper.insertBean(totaldiv);
			}else{
				totaldiv.setPkHp(hp.getPkHp());
				DataBaseHelper.updateBeanByPk(totaldiv,false);
			}
		}else{
			//如果是Null.代表前台要删除（硬删除）
			DataBaseHelper.execute("delete from bd_hp_totaldiv where pk_hp= ?", hp.getPkHp());
		}
		
		/**保存费用分类策略*/
		if(!CollectionUtils.isEmpty(itemcatedivList)){
			if(itemcatedivList.size() != 0){
				/**校验重复性*/
				Map<String, String> pkItemcateMap = new HashMap<String, String>();
				int len = itemcatedivList.size();
				for(int i = 0; i < len; i++){
					String pkItemcate = itemcatedivList.get(i).getPkItemcate();
					if(pkItemcateMap.containsKey(pkItemcate)){
						throw new BusException("费用分类策略重复！");
					}
					pkItemcateMap.put(pkItemcate, itemcatedivList.get(i).getPkHpsrv());
				}
				
				//先全删再插入的方式（硬删除）
				String pkhp = hp.getPkHp();
				DataBaseHelper.execute("delete from bd_hp_itemcatediv where pk_hp = ?",new Object[]{pkhp});
				for(BdHpItemcatediv itemcatediv : itemcatedivList){
					itemcatediv.setPkHp(pkhp);
					DataBaseHelper.insertBean(itemcatediv);
				}
			}else{
				String pkhp = hp.getPkHp();
				DataBaseHelper.execute("delete from bd_hp_itemcatediv where pk_hp = ?",new Object[]{pkhp});
			}
		}else{
			//如果是Null.代表前台要删除（硬删除）
			DataBaseHelper.execute("delete from bd_hp_itemcatediv where pk_hp= ?", hp.getPkHp());
		}
		
		
		/**保存费用项目策略*/
		if(!CollectionUtils.isEmpty(itemdivList)){
			if(itemdivList.size() != 0){
				/**校验费用项目策略重复性*/
				Map<String, String> pkItemMap = new HashMap<String, String>();
				int len = itemdivList.size();
				for(int i = 0; i < len; i++){
					String pkItem = itemdivList.get(i).getPkItem();
					if(pkItemMap.containsKey(pkItem)){
						throw new BusException("费用项目策略重复！");
					}
					pkItemMap.put(pkItem, itemdivList.get(i).getPkHpitem());
				}
				//先全删再插入的方式（硬删除）
				String pkhp = hp.getPkHp();
				DataBaseHelper.execute("delete from bd_hp_itemdiv where pk_hp = ?",new Object[]{pkhp});
				for(BdHpItemdiv itemdiv : itemdivList){
					itemdiv.setPkHp(pkhp);
					DataBaseHelper.insertBean(itemdiv);
				}
			}else{
				String pkhp = hp.getPkHp();
				DataBaseHelper.execute("delete from bd_hp_itemdiv where pk_hp = ?",new Object[]{pkhp});
			}
		}else{
			//如果是Null.代表前台要删除（硬删除）
			DataBaseHelper.execute("delete from bd_hp_itemdiv where pk_hp= ?", hp.getPkHp());
		}
		
		/**保存分段支付策略*/
		if(!CollectionUtils.isEmpty(secdivList)){
			if(secdivList.size() != 0){
				String pkhp = hp.getPkHp();
				DataBaseHelper.execute("delete from bd_hp_secdiv where pk_hp = ?",new Object[]{pkhp});
				for(BdHpSecdiv secdiv : secdivList){
					secdiv.setPkHp(pkhp);
					DataBaseHelper.insertBean(secdiv);
				}
			}else{
				String pkhp = hp.getPkHp();
				DataBaseHelper.execute("delete from bd_hp_secdiv where pk_hp = ?",new Object[]{pkhp});
			}
		}else{
			//如果是Null.代表前台要删除（硬删除）
			DataBaseHelper.execute("delete from bd_hp_secdiv where pk_hp= ?", hp.getPkHp());
		}
		//发送新增或更新消息（深圳）
		Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select count(1) as count from bd_hp p where p.pk_parent is null and p.pk_hp =?", hp.getPkHp());
		if(queryForMap!=null && Integer.parseInt(queryForMap.get("count").toString())>0){
			Map<String, Object> map = JsonUtil.readValue(ApplicationUtils.objectToJson(hp), Map.class) ;
			map.put("state", state);
			PlatFormSendUtils.sendPactMsg(map);
		}
		
		/**保存单病种限价*/
		/*if(!CollectionUtils.isEmpty(diagdivList)){
			if(diagdivList.size() != 0){
				*//**校验单病种限价重复性*//*
				Map<String, String> pkDiagMap = new HashMap<String, String>();
				int len = diagdivList.size();
				for(int i=0; i<len; i++){
					String pkDiag = diagdivList.get(i).getPkDiag();
					if(pkDiagMap.containsKey(pkDiag)){
						throw new BusException("单病种限价重复！");
					}
					pkDiagMap.put(pkDiag, diagdivList.get(i).getPkTotaldiv());
				}
				
				
				String pkhp = hp.getPkHp();
				DataBaseHelper.execute("delete from bd_hp_diagdiv where pk_hp = ?",new Object[]{pkhp});
				for(BdHpDiagdiv diagdiv : diagdivList){
					diagdiv.setPkHp(pkhp);
					DataBaseHelper.insertBean(diagdiv);
				}
			}else{
				String pkhp = hp.getPkHp();
				DataBaseHelper.execute("delete from bd_hp_diagdiv where pk_hp = ?",new Object[]{pkhp});
			}
		}else{
			//如果是Null.代表前台要删除（硬删除）
			DataBaseHelper.execute("delete from bd_hp_diagdiv where pk_hp= ?", hp.getPkHp());
		}*/
		
	}

	/**
	 * 删除医保计划和相关
	 * @param param
	 * @param user
	 */
	public void delHpAndAll(String param, IUser user){
		String pkhp = JsonUtil.getFieldValue(param, "pkHp");
		
		//校验：是否被保险计划表引用
		int count = DataBaseHelper.queryForScalar("select count(*) from pi_insurance where del_flag='0' and pk_hp = ?", Integer.class, pkhp);
		
		//效验：是否被患者分类引用(pi_cate)
		int piCateCount = DataBaseHelper.queryForScalar("select count(1) from pi_cate where del_flag = '0' and pk_hp = ?", Integer.class, pkhp);
		
		if(count!=0){
			throw new BusException("该医保计划被保险计划引用！");
		} else if(piCateCount!=0){
			throw new BusException("该医保计划被患者分类引用！");
		}
		
		//发送医保删除消息（深圳）
		String sql = "select * from bd_hp where pk_parent is null and pk_hp= ?";
		Map<String, Object> map = DataBaseHelper.queryForMap(sql, pkhp);
		if(map!=null && map.size()>0){
			map.put("state", "_DELETE");
			PlatFormSendUtils.sendPactMsg(map);
		}
		
		DataBaseHelper.execute("delete from bd_hp_totaldiv where pk_hp = ?", new Object[]{pkhp});
		DataBaseHelper.execute("delete from bd_hp_itemcatediv where pk_hp = ?", new Object[]{pkhp});
		DataBaseHelper.execute("delete from bd_hp_itemdiv where pk_hp = ?", new Object[]{pkhp});
		DataBaseHelper.execute("delete from bd_hp_secdiv where pk_hp = ?", new Object[]{pkhp});
		DataBaseHelper.execute("delete from bd_hp_diagdiv_itemcate where pk_totaldiv in (select pk_totaldiv from BD_HP_DIAGDIV where pk_hp=? )", new Object[]{pkhp});
		DataBaseHelper.execute("delete from bd_hp_diagdiv where pk_hp = ?", new Object[]{pkhp});
		DataBaseHelper.execute("delete from bd_hp where pk_hp = ?", new Object[]{pkhp});
		
	}
	
	/**
	 * 删除医保计划（软删除）
	 * @param param
	 * @param user
	 */
	public void delHp(String param, IUser user){
		String pkhp = JsonUtil.getFieldValue(param, "pkHp");
		
		//校验：是否被保险计划表引用
		int count = DataBaseHelper.queryForScalar("select count(*) from pv_encounter where del_flag='0' and pk_insu = ?", Integer.class, pkhp);
		if(count!=0){
			throw new BusException("该医保计划被保险计划引用！");
		} 
		//发送医保删除消息（深圳）
		String sql = "select * from bd_hp where pk_parent is null and pk_hp= ?";
		Map<String, Object> map = DataBaseHelper.queryForMap(sql, pkhp);
		if(map!=null && map.size()>0){
			map.put("state", "_DELETE");
			PlatFormSendUtils.sendPactMsg(map);
		}
		
		DataBaseHelper.execute("update bd_hp set del_flag='1' where  del_flag='0' and pk_hp= ?", new Object[]{pkhp});
		DataBaseHelper.execute("update bd_hp_diagdiv set del_flag='1' where  del_flag='0' and pk_hp= ?", new Object[]{pkhp});
		DataBaseHelper.execute("update bd_hp_org set del_flag='1' where  del_flag='0' and pk_hp= ?", new Object[]{pkhp});
		DataBaseHelper.execute("update bd_hp_dept set del_flag='1' where  del_flag='0' and pk_hp= ?", new Object[]{pkhp});
	}
	
	
	/**
	 * 保存价格使用配置
	 * @param param
	 * @param user
	 */
	public void savePricetypeCfgList(String param, IUser user){
		List<BdPricetypeCfg> pricetypeCfgList = JsonUtil.readValue(param, new TypeReference<List<BdPricetypeCfg>>() {
		});
		if(pricetypeCfgList != null && pricetypeCfgList.size() != 0){
			for(BdPricetypeCfg pricetypeCfg : pricetypeCfgList){
				if(pricetypeCfg.getPkPricetypecfg()==null){
					DataBaseHelper.insertBean(pricetypeCfg);
				}else{
					DataBaseHelper.updateBeanByPk(pricetypeCfg, false);
				}
			}
		}
	}
	
	
	/**
	 * 保存医保计划及相关--新版
	 * @param param
	 * @param user
	 */
	public void saveHpNew(String param , IUser user){
		
		HpAndAllParam hpAndAll = JsonUtil.readValue(param, HpAndAllParam.class);
		BdHp hp = hpAndAll.getHpInfo();
		List<BdHpDivconfig> hpDivconfigList = hpAndAll.getHpDivConfigList();
		List<BdHpDivconfig> delHpDivconfigList = hpAndAll.getDelHpDivConfigList();//硬删除才用到，目前不用
		List<BdHpOrg> hdOrgList = hpAndAll.getHpOrgList();
		List<BdDictattr> dictAttrList = hpAndAll.getHpDictAttrList();
		List<BdHpDept> hdDeptList = hpAndAll.getHpDeptList();
		//平台发消息 state 新增：_ADD 更新：_UPDATE 删除：_DELETE	
		String state = "";
		
		String pkOrg = ((User) user).getPkOrg();
		/**保存医保计划*/
		if(hp.getPkHp() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_hp "
					+ "where del_flag = '0' and code = ? and pk_org = ?", Integer.class, hp.getCode(),pkOrg);
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_hp "
					+ "where del_flag = '0' and name = ? and pk_org = ?", Integer.class, hp.getName(),pkOrg);
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(hp);
				state = "_ADD"; //平台发消息
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("【医保计划】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("【医保计划】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("【医保计划】编码和名称都重复！");
				}
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_hp "
					+ "where del_flag = '0' and code = ? and pk_org = ? and pk_hp != ?", Integer.class, hp.getCode(),pkOrg,hp.getPkHp());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_hp "
					+ "where del_flag = '0' and name = ? and pk_org = ? and pk_hp != ?", Integer.class, hp.getName(),pkOrg,hp.getPkHp());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.updateBeanByPk(hp,false);
				state = "_UPDATE"; //平台发消息
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("【医保计划】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("【医保计划】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("【医保计划】编码和名称都重复！");
				}
			}
		}
		
		
		String pkHp = hp.getPkHp();
		
		/**新增修改或删除-医保计划下的策略配置
		先全删再恢复的方式（软删除）*/
		DataBaseHelper.update("update bd_hp_divconfig  set del_flag = '1' where pk_hp = ?",new Object[]{pkHp});
		if(hpDivconfigList != null && hpDivconfigList.size() != 0){
			for(BdHpDivconfig divConf : hpDivconfigList){
				if(divConf.getPkHpdivconfig()!= null){
					divConf.setDelFlag("0");//恢复
					divConf.setPkHp(pkHp);
					DataBaseHelper.updateBeanByPk(divConf, false);
				}else{
					divConf.setPkHp(pkHp);
					divConf.setDelFlag("0");
					DataBaseHelper.insertBean(divConf);
				}
			}
		}
		/**删除bd_hp_divconfig表中del_flag='1'的数据(真删除)*/
		DataBaseHelper.execute("delete from bd_hp_divconfig where del_flag = '1'");
		
		
		/**保存或更新使用机构
		先全删再恢复的方式（软删除）*/
		DataBaseHelper.update("update BD_HP_ORG  set del_flag = '1' where pk_hp = ?",new Object[]{pkHp});
		if(hdOrgList != null && hdOrgList.size() != 0){
			for(BdHpOrg org : hdOrgList){
				if(org.getPkHporg() != null){
					org.setDelFlag("0");//恢复
					org.setPkOrg("~");
					org.setPkHp(pkHp);
					DataBaseHelper.updateBeanByPk(org, false);
				}else{
					org.setPkHp(pkHp);
					org.setPkOrg("~");
					org.setDelFlag("0");
					DataBaseHelper.insertBean(org);
				}
			}
		}
		
		
		/**保存或更新拓展属性*/
		//先全删再恢复的方式（软删除）
		DataBaseHelper.update("update bd_dictattr  set del_flag = '1' where pk_dict = ?",new Object[]{pkHp});
		if(dictAttrList != null && dictAttrList.size() != 0){
			for(BdDictattr dictAttr : dictAttrList){
				if(dictAttr.getPkDictattr() != null){
					dictAttr.setDelFlag("0");//恢复
					dictAttr.setPkDict(pkHp);
					DataBaseHelper.updateBeanByPk(dictAttr, false);
				}else{
					dictAttr.setDelFlag("0");
					dictAttr.setPkDict(pkHp);
					DataBaseHelper.insertBean(dictAttr);
				}
			}
		}
		
		/**保存或更新使用机构
		先全删再恢复的方式（软删除）*/
		DataBaseHelper.update("update BD_HP_DEPT  set del_flag = '1' where pk_hp = ?",new Object[]{pkHp});
		if(hdDeptList != null && hdDeptList.size() != 0){
			for(BdHpDept dept : hdDeptList){
				if(dept.getPkHpdept() != null){
					dept.setDelFlag("0");//恢复
					dept.setPkOrg("~");
					dept.setPkHp(pkHp);
					DataBaseHelper.updateBeanByPk(dept, false);
				}else{
					dept.setPkHp(pkHp);
					dept.setPkOrg("~");
					dept.setDelFlag("0");
					DataBaseHelper.insertBean(dept);
				}
			}
		}
		
		//发送新增或更新消息（深圳）
		Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select count(1) as count from bd_hp p where p.pk_parent is null and p.pk_hp =?", hp.getPkHp());
		if(queryForMap!=null && Integer.parseInt(queryForMap.get("count").toString())>0){
			Map<String, Object> map = JsonUtil.readValue(ApplicationUtils.objectToJson(hp), Map.class) ;
			map.put("state", state);
			PlatFormSendUtils.sendPactMsg(map);
		}
	}
	
	
	public List<Map<String,Object>> queryHpOrg(String param, IUser user){
		/*
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String name = "";
		if(paramMap.get("name")!=null){
			name = paramMap.get("name").toString();
		}
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
		*/
		
		BdHp hpVo = JsonUtil.readValue(param, BdHp.class);
		List<Map<String,Object>>  hpOrg = this.priceMapper.queryHpOrg(hpVo.getPkHp());
		return hpOrg;
		
	
	}
	
	//查询医保计划 的  使用机构、拓展属性
	public Map<String,Object> queryHpInfos(String param, IUser user){
		BdHp hpVo = JsonUtil.readValue(param, BdHp.class);
		User u = (User)user;
		Map<String,Object> hpInfosMap=new HashMap<String,Object>();
		//List<BdHp>  hpList = this.priceMapper.queryHp();
		List<Map<String,Object>>  hpDivConfigList = this.priceMapper.queryHpDivConfig(hpVo.getPkHp());
		List<Map<String,Object>>  hpOrgList = this.priceMapper.queryHpOrg(hpVo.getPkHp());
		List<Map<String,Object>>  hpDictAttrList = this.priceMapper.queryHpDictAttr(u.getPkOrg(), hpVo.getPkHp());
		List<Map<String,Object>>  hpDeptList = this.priceMapper.queryHpDept(hpVo.getPkHp());
		
		//hpInfosMap.put("hpList", hpList);
		hpInfosMap.put("hpDivConfigList", hpDivConfigList);
		hpInfosMap.put("hpOrgList", hpOrgList);
		hpInfosMap.put("hpDictAttrList", hpDictAttrList);
		hpInfosMap.put("hpDeptList", hpDeptList);//使用科室集合
		
		return hpInfosMap;
		
	
	}

	/**
	 * 票据项目维护检查按钮
	 * @param param
	 * @param user
	 */
	public void check(String param, IUser user){
		List<Map<String, Object>> invcateList=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> invcateItemList=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> invcateItemCateList=new ArrayList<Map<String, Object>>();
		String pkOrg = ((User) user).getPkOrg();

		String checkEd = "";
		StringBuffer sb = new StringBuffer();
		invcateList = DataBaseHelper.queryForList("select pk_invcate,name from bd_invcate where del_flag = '0' and pk_org =?",new Object[]{pkOrg});
		for (Map<String, Object> invcate : invcateList) {

			invcateItemList = DataBaseHelper.queryForList("select pk_invcateitem,name from bd_invcate_item where del_flag = '0' and pk_invcate = ?  and pk_org = ?",new Object[]{invcate.get("pkInvcate"),pkOrg});
			for (Map<String, Object> invcateItem : invcateItemList) {
				int count = DataBaseHelper.queryForScalar("SELECT COUNT (*) FROM bd_itemcate i WHERE i.del_flag = '0' AND EXISTS " +
						"(SELECT ai.pk_itemcate FROM bd_invcate_itemcate ai WHERE ai.del_flag = '0' AND ai.pk_itemcate = i.pk_itemcate " +
						" AND ai.pk_invcateitem = ?  AND ai.pk_org = ?)",Integer.class,invcateItem.get("pkInvcateitem"),pkOrg);
				if (count == 0){

					sb.append( "["+invcate.get("name")+"]分类下["+invcateItem.get("name")+"]项目缺少费用分类！");
				}
			}

		}
		checkEd = sb.toString();
		if ("".equals(checkEd)){
			throw new BusException("检查完成！");
		}else{
			throw new BusException(checkEd);
		}
		
	}

	/**
	 * 保存医保计划使用科室
	 * 交易号 001002005073
	 * @param param
	 * @param user
	 */
	public void saveHpDept(String param, IUser user) 
	{
		BdHpDept dept = JsonUtil.readValue(param, BdHpDept.class);
		if(dept.getPkHpdept() != null){
			dept.setDelFlag("0");//恢复
			dept.setPkOrg("~");
			DataBaseHelper.updateBeanByPk(dept, false);
		}else{
			dept.setPkOrg("~");
			dept.setDelFlag("0");
			DataBaseHelper.insertBean(dept);
		}
	}
	
	/**
	 * 查询医保计划使用科室
	 * 交易号 001002005074
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryHpDept(String param, IUser user){
		String pkHp = JsonUtil.getFieldValue(param, "pkHp");
		List<Map<String,Object>>  hpDept = new ArrayList<>();
		if(StringUtils.isNotEmpty(pkHp)) {
			hpDept = this.priceMapper.queryHpDept(pkHp);
		}
		return hpDept;
	}
	
	/**
	 * 删除医保计划使用科室
	 * 交易号 001002005075
	 * @param param
	 * @param user
	 */
	public void delHpDept(String param, IUser user){
		String pkHpdept = JsonUtil.getFieldValue(param, "pkHpdept");
		if(StringUtils.isNotEmpty(pkHpdept)) {
			/**删除bd_hp_dept表中的数据(真删除)*/
			DataBaseHelper.execute("delete from bd_hp_dept where pk_hpdept = ? ",new Object[]{pkHpdept} );
		}
	}
	
}
