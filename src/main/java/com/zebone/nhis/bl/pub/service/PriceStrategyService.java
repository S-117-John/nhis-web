package com.zebone.nhis.bl.pub.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.bl.pub.dao.CgQryMaintainMapper;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlOpDrugStorePriceInfo;
import com.zebone.nhis.bl.pub.vo.BlPubPriceVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdHpItemdiv;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.scm.pub.BdStore;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class PriceStrategyService {

	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	@Autowired
	private CgQryMaintainMapper cgQryMaintainMapper;
	/**
     * 批量获取某医保下医嘱项目或收费项目对应的某定价模式下的定价(杨雪添加)
     * @param pkItems 收费项目主键集合
     * @param pkOrds 医嘱项目主键集合
     * @param hp 患者使用的内部医保
     * @param pkOrg
     * @param dateHap
     * @return
     * @throws BusException
     */
	public List<ItemPriceVo> batchChargePriceModelByOrdAndItem(BdHp hp,String pkOrg,String dateHap,List<String> pkItems,List<String> pkOrds) throws BusException {
		//构造获取收费项目对应价格查询参数
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkOrg", pkOrg);
		paramMap.put("dateHap", dateHap);
		paramMap.put("pkHp", hp.getPkHp());
		List<ItemPriceVo> resultList = new ArrayList<ItemPriceVo>();
		if(pkItems!=null&&pkItems.size()>0){
			Set<String> itemSet = new HashSet<>(pkItems);
			paramMap.put("itemList", CommonUtils.convertSetToSqlInPart(itemSet, "pk_item"));
			//批量获取收费项目在医保下设置的价格
			List<ItemPriceVo> tempItemList  = cgQryMaintainMapper.queryItemAndChildPrice(paramMap);
			if(tempItemList == null|| tempItemList.size()<=0) {
				throw new BusException("未获取到医保【"+hp.getName()+"】下设置的本次计费的任何收费项目价格！");
			}
			//校验收费项目是否可用,存在不可用项目直接抛异常
			verfyItemStatus(tempItemList);
			//校验所有收费项目是否存在价格
			verfyItemHasPrice(pkItems,tempItemList,hp.getName());
			resultList.addAll(tempItemList);
		}
		if(pkOrds!=null&&pkOrds.size()>0){
			Set<String> set = new HashSet<>(pkOrds);
			paramMap.put("ordList", CommonUtils.convertSetToSqlInPart(set, "pk_ord"));
			//批量获取医嘱项目在医保下设置的价格
			List<ItemPriceVo> tempOrdList  = cgQryMaintainMapper.queryItemAndChildPriceByOrd(paramMap);
			if(tempOrdList != null && tempOrdList.size()>0) {
				//校验收费项目是否可用,存在不可用项目直接抛异常
			    verfyItemStatus(tempOrdList);
				//校验所有医嘱项目对应收费项目是否存在收费价格
			    verfyOrdItemHasPrice(pkOrds,tempOrdList,hp.getName());
			    //校验某条医嘱下是否有一条以上的收费项目
			    verfyOrdHasItem(pkOrds,tempOrdList);
				resultList.addAll(tempOrdList);
			}else{
				//校验某条医嘱下是否有一条以上的收费项目
			    verfyOrdHasItem(pkOrds,tempOrdList);
			}
		}
		//过滤掉价格为0的费用
		priceZeroFilter(resultList);
		return resultList;
	}

	
    /**
     * 批量获取收费项目对应的某定价模式下的定价(杨雪添加)
     * @param pkItems 收费项目主键集合
     * @param hp 患者使用的内部医保
     * @param pkOrg
     * @param dateHap
     * @param opDtLists 医嘱开立收费项目集合
     * @return
     * @throws BusException
     */
	public List<ItemPriceVo> batchChargePriceModel(BdHp hp,String pkOrg,String dateHap,List<String> pkItems,Map<String, BigDecimal> opDtLists) throws BusException {
		//构造获取收费项目对应价格查询参数
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkOrg", pkOrg);
		paramMap.put("dateHap", dateHap);
		Set<String> itemSet = new HashSet<>(pkItems);
		paramMap.put("itemList", CommonUtils.convertSetToSqlInPart(itemSet, "pk_item"));
		paramMap.put("pkHp", hp.getPkHp());
		//批量获取收费项目在医保下设置的价格
		List<ItemPriceVo> itemList = cgQryMaintainMapper.queryItemAndChildPrice(paramMap);
		if(itemList == null|| itemList.size()<=0) {
			throw new BusException("未获取到医保【"+hp.getName()+"】下设置的本次计费的任何收费项目价格！");
		}
		//校验收费项目是否可用,存在不可用项目直接抛异常
		verfyItemStatus(itemList);
		//校验所有收费项目是否存在价格
		verfyItemHasPrice(pkItems,itemList,hp.getName());
		//过滤价格为0的收费项目
		itemList = this.priceZeroFilter(itemList);
		if(itemList==null||itemList.size()<=0)
			return null;
		List<ItemPriceVo> resultList = new ArrayList<ItemPriceVo>();
		//计算所有收费项目对应价格及数量
		for(Map.Entry<String, BigDecimal> mapEntery : opDtLists.entrySet()){
			String pkItemOld = mapEntery.getKey();
			BigDecimal quan = mapEntery.getValue();
			for(ItemPriceVo item:itemList){
				if(pkItemOld.equals(item.getPkItemOld())&&item.getPkItemOld().equals(item.getPkItem())){
					//非组套,默认按本服务定价，理论上不存在非组套按组套收费的情况
					if(EnumerateParameter.ZERO.equals(item.getFlagSetOld())){
						if (EnumerateParameter.ZERO.equals(item.getEuPricemodeOld())) {
							item.setQuan(MathUtils.mul(quan.doubleValue(), item.getQuan()));//医嘱开立数量*收费项目设置的数量
							resultList.add(item);
							//break;
						}else if(EnumerateParameter.ONE.equals(item.getEuPricemodeOld())){//服务套成员合计定价
							//理论上不存在这种情况，因此暂不写实现
							break;
						}
					}else{//组套
						//该服务对应子项集合
						List<ItemPriceVo> childList = new ArrayList<ItemPriceVo>();
						//子项合计价格
						Double sumChildPrice = 0.0;
						//取当前服务对应所有子项
						for(ItemPriceVo childitem:itemList){
							if(childitem.getPkItemOld().equals(pkItemOld)&&!childitem.getPkItemOld().equals(childitem.getPkItem())){
								childList.add(childitem);
								sumChildPrice = MathUtils.add(sumChildPrice, MathUtils.mul(childitem.getQuan(),childitem.getPriceOrg()));
							}
						}
						if(childList == null||childList.size()<=0)
							throw new BusException("组套项目【"+item.getNameItemOld()+"】未获取对应的组套子项及其价格！");
						// 定价模式是本服务定价
						if (EnumerateParameter.ZERO.equals(item.getEuPricemodeOld())) {
							if(sumChildPrice.compareTo(item.getPriceOrg())==0){//子项合计价格 = 本服务价格,直接使用子项价格及数量
								for(ItemPriceVo child:childList){
									child.setQuan(MathUtils.mul(quan.doubleValue(), child.getQuan()));//变更子项数量为医嘱开立数量*子项数量
									resultList.add(child);
								}
							}else{//子项价格与本服务价格不等，需要计算权重
								for(ItemPriceVo child:childList){
									BigDecimal weight = (new BigDecimal(child.getPriceOrg()).multiply(new BigDecimal(child.getQuan()))).divide(new BigDecimal(sumChildPrice), 4, BigDecimal.ROUND_HALF_UP);
									child.setPriceOrg((new BigDecimal(item.getPriceOrg()).multiply(weight).divide(new BigDecimal(child.getQuan()))).doubleValue());
									child.setQuan(MathUtils.mul(quan.doubleValue(), child.getQuan()));//变更子项数量为医嘱开立数量*子项数量
									resultList.add(child);
								}
							}
						}else if(EnumerateParameter.ONE.equals(item.getEuPricemodeOld())){//服务套成员合计定价
							for(ItemPriceVo child:childList){
								child.setQuan(MathUtils.mul(quan.doubleValue(), child.getQuan()));//变更子项数量为医嘱开立数量*子项数量
								resultList.add(child);
							}
						}
					}
					
				}
				
			}
		}
		return resultList;
	}
	/**
	 * 过滤价格为0的收费项目
	 * @param itemList
	 * @return
	 */
	public List<ItemPriceVo> priceZeroFilter(List<ItemPriceVo> itemList){
		Iterator<ItemPriceVo> it = itemList.iterator();
		while(it.hasNext()){
			ItemPriceVo item = it.next();
		    if(MathUtils.equ(item.getPrice(), 0.0)){
		        it.remove();
		    }
		}
		return itemList;
	}
	/**
	 * 校验收费项目是否存在对应价格
	 * @param pkItems
	 * @param itemList
	 */
	public void verfyItemHasPrice(List<String> pkItems,List<ItemPriceVo> itemList,String hpname){
		//未取到价格的收费项目
		List<String> pkList = new ArrayList<String>();
		for(String pkItem:pkItems){
			//存在明细标志
			boolean hasFlag = false;
			for(ItemPriceVo pricevo:itemList){
				if(pkItem.equals(pricevo.getPkItemOld())){
					hasFlag = true;
					break;
				}
			}
			if(!hasFlag){
				pkList.add(pkItem);
			}
		}
		if(pkList!=null&&pkList.size()>0){
			StringBuilder sp = new StringBuilder();
			List<BdItem> list = cgQryMaintainMapper.queryItemsByPk(pkList);
			for(BdItem item : list){
				sp.append(item.getName()).append(",");
			}
			if(sp!=null&&sp.length()>1){
				throw new BusException("收费项目【"+sp.toString().substring(0,sp.length()-1)+"】未获取到医保【"+hpname+"】下对应的收费价格！");
			}
				
		}
		
	}
	/**
	 * 校验医嘱项目下的收费项目是否存在价格
	 */
	public void verfyOrdItemHasPrice(List<String> pkOrds,List<ItemPriceVo> ordItemList,String hpname){
		//根据医嘱项目，获取所有收费项目
		Map<String,Object> param = new HashMap<String,Object>();
		Set<String> ordSet = new HashSet<>(pkOrds);
		param.put("pkOrds", CommonUtils.convertSetToSqlInPart(ordSet, "pk_ord"));
		List<Map<String,Object>> pkordItems = cgQryMaintainMapper.queryItemByOrd(param);
		//根据所有收费项目，校验是否存在价格
		StringBuilder sb = new StringBuilder("");
		for(Map<String,Object> pkordItemMap:pkordItems){
			boolean isHas = false;
			String pkOrd = CommonUtils.getString(pkordItemMap.get("pkOrd"));
			String pkItem = CommonUtils.getString(pkordItemMap.get("pkItem"));
			for(ItemPriceVo pricevo: ordItemList){
				if(pkOrd.equals(pricevo.getPkOrdOld())&&pkItem.equals(pricevo.getPkItem())){
					isHas = true;
					break;
				}
			}
			if(!isHas){
				sb.append(CommonUtils.getString(pkordItemMap.get("name"))).append(",");
			}
		}
		if(sb!=null&&sb.length()>1){
			throw new BusException("收费项目【"+sb.toString().substring(0,sb.length()-1)+"】未获取到医保【"+hpname+"】下对应的收费价格！");
		}
	}
	
	/**
	 * 校验医嘱项目下是否存在对应的收费项目
	 */
	public void verfyOrdHasItem(List<String> pkOrds,List<ItemPriceVo> ordItemList){
	
		if(ordItemList==null || ordItemList.size()==0){
			Map<String, Object> ordMap = DataBaseHelper.queryForMap("select name from bd_ord where pk_ord=?", pkOrds.get(0).toString());
			throw new BusException("医嘱【"+ordMap.get("name")+"】下没有对应的有效收费项目!");
		}else{
			for(String pkOrd : pkOrds){
				List<ItemPriceVo> singleOrdItems=new ArrayList<ItemPriceVo>();
				for(ItemPriceVo itemVo : ordItemList){
					if(pkOrd.equals(itemVo.getPkOrdOld())){
						singleOrdItems.add(itemVo);
					}
				}
				if(singleOrdItems.size()==0){
					Map<String, Object> ordMap = DataBaseHelper.queryForMap("select name from bd_ord where pk_ord=?", pkOrd);
					throw new BusException("医嘱【"+ordMap.get("name")+"】下没有对应的收费项目!");
				}
			}
		}
		
	}
	
	/**
	 * 校验收费项目是否被删除或停用
	 * @param items
	 */
	public void verfyItemStatus(List<ItemPriceVo> items){
		StringBuilder itemName = new StringBuilder("");
		for(ItemPriceVo itemVo:items){
			//if("1".equals(itemVo.getDelFlag())||"0".equals(itemVo.getFlagActive())){杨雪注释
			if("1".equals(itemVo.getDelFlag())){
				itemName.append(itemVo.getNameCg()).append(",");
			}
		}
		if(itemName!=null&&itemName.length()>1){
			throw new BusException("收费项目【"+itemName.toString().substring(0, itemName.length()-1)+"】未启用或已删除！");
		}
	}
	/**
	 * 定价模式
	 * 
	 * 根据收费项目主键、就诊信息，服务日期、药品标志。获取对应的价格信息(原始单价，当前单价)
	 * @param pk_item
	 * @param pvVo
	 * @param date_hap
	 * @return Map
	 *         key值为收费项目主键(在组套的情况下有可能多个)，value值"price","quan"逗号前面是单价逗号后面是对应的子项的数量
	 */
	public Map<String, String> chargePriceModel(String pkItem, String pkHp, String pkOrg, Date dateHap) throws BusException {

		Map<String, String> mapResult = new HashMap<String, String>();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, Object> mapThisServicePrice = null;
		mapParam.put("pkOrg", pkOrg);
		mapParam.put("pkHp", pkHp);
		mapParam.put("pkItem", pkItem);
		mapParam.put("dateHap", dateHap);
		Map<String, Object> bdItem = cgQryMaintainService.qryBdItemByPk(mapParam);
		BdHp bdHap = cgQryMaintainService.qryBdHpInfo(mapParam);		
		if (bdItem == null){
			throw new BusException("【" + pkItem + "】" + ":未维护此收费项目");
		}
		if(bdItem.get("flagActive")==null||"0".equals(bdItem.get("flagActive").toString())){
			throw new BusException("【" + bdItem.get("name") + "】" + ":此收费项目未启用");
		}
		if(bdItem.get("delFlag")==null||"1".equals(bdItem.get("delFlag").toString())){
			throw new BusException("【" + bdItem.get("name") + "】" + ":此收费项目已删除");
		}
		// 定价模式是本服务定价
		if (EnumerateParameter.ZERO.equals(bdItem.get("euPricemode").toString())) {
			// 获取本服务定价下的价格
			mapThisServicePrice = cgQryMaintainService.qryThisServicePrice(mapParam);
			if (mapThisServicePrice == null){
				throw new BusException("【" + bdItem.get("name") + "】 未在患者的医保【" + bdHap.getName() + "】下维护价格！");
			}
			// 组套项目
			if (EnumerateParameter.ONE.equals(mapThisServicePrice.get("flagSet"))) {
				// 组套当前总价格
				BigDecimal groupPrice = new BigDecimal(mapThisServicePrice.get("price").toString());
				// 查询收费项目对应的子项目主键和数量
				List<Map<String, Object>> pkItemChild = cgQryMaintainService.qryPkItemChild(mapParam);
				if (pkItemChild == null || pkItemChild.size() == 0){
					throw new BusException("【" + bdItem.get("name") + "】:本收费项目为组套项目，但是未维护其子项目信息！");
				}
				Map<String, Map<String, Object>> itemChildPrice = cgQryMaintainService.qryThisServiceChildPrice(mapParam);
				// 获取每个子项目的定价
				BigDecimal priceSum = new BigDecimal(0);
				Map<String, String> childItemMap = new HashMap<String, String>();
				BigDecimal childPrice = null;// 子项价格
				BigDecimal childQuan = null;// 子项数量
				for (Map<String, Object> mapTemp : pkItemChild) {
					mapThisServicePrice = itemChildPrice.get(mapTemp.get("pkItemChild").toString());
					if (mapThisServicePrice == null){
						throw new BusException("【" + bdItem.get("name") + "】 未维护在 【 " + bdHap.getName() + "】医保下维护价格");
					}
					//因为没有组套嵌套，所以有两种模式：本服务定价，对应物品价格
					if (EnumerateParameter.TWO.equals(mapThisServicePrice.get("euPricemode"))) {//对应物品价格
						//TODO 取供应链相应的接口的价格,
						childPrice = new BigDecimal("");
					} else {//本服务定价
						childPrice = new BigDecimal(mapThisServicePrice.get("price").toString());
					}
					childQuan = new BigDecimal(mapTemp.get("quan").toString());
					childItemMap.put(mapTemp.get("pkItemChild").toString(), childPrice + "," + childQuan);
					priceSum = priceSum.add(childPrice.multiply(childQuan));
				}
				BigDecimal weight = null;// 权重
				BigDecimal curPriceChild = null;// 当前子项价格
				// 总价格等于当前服务套价格
				// 注意 不仅比较值，还比较精度
				if (priceSum.equals(groupPrice)) {
					// 直接返回价格
					mapResult.putAll(childItemMap);
					/*
					 * for (Map.Entry<String, String> entry :
					 * childItemMap.entrySet()) { // 直接返回价格
					 * mapResult.put(entry.getKey(),
					 * entry.getValue().toString()); }
					 */
				}
				// 不相等，需计算权重
				else {
					for (Map<String, Object> mapTemp : pkItemChild) {
						// 根据子项目主键获取子项目的价格
						String strTemp = childItemMap.get(mapTemp.get("pkItemChild").toString());
						childPrice = new BigDecimal(strTemp.split(",")[0]);
						childQuan = new BigDecimal(strTemp.split(",")[1]);
						//单价乘以数量除以以前总价格
						//TODO 此处是否需要乘以数量再除以数量？
						weight = (childPrice.multiply(childQuan)).divide(priceSum, 4, BigDecimal.ROUND_HALF_UP);
						// 目前单价就是组套价格乘以权重除以数量
						curPriceChild = (groupPrice.multiply(weight)).divide(childQuan);
						mapResult.put(mapTemp.get("pkItemChild").toString(), curPriceChild.toString() + "," + childQuan);
					}
				}
			} else {
				// 不是组套项目，就直接返回其单价
				mapResult.put(pkItem, mapThisServicePrice.get("price").toString() + ",1");
			}
		}
		// 定价模式是服务套成员合计
		else if (EnumerateParameter.ONE.equals(bdItem.get("euPricemode").toString())) {
			// 获取子成员主键和对应的数量
			List<Map<String, Object>> pkItemChild = cgQryMaintainService.qryPkItemChild(mapParam);
			if (pkItemChild.size() == 0){
				throw new BusException("【" + pkItem + "】" + ":本收费项目为组套项目，但是未维护其子项目信息！");
			}
			Map<String, Map<String, Object>> itemChildPrice = cgQryMaintainService.qryThisServiceChildPrice(mapParam);
			if(itemChildPrice == null ){
				throw new BusException("【" + pkItem + "】" + ":本收费项目为组套项目，但是未维护子项目价格信息！");
			}
			for (Map<String, Object> mapTemp : pkItemChild) {
				mapThisServicePrice = itemChildPrice.get(mapTemp.get("pkItemChild").toString());
				if(mapThisServicePrice == null ){
					throw new BusException("【" + pkItem + "】" + ":本收费项目为组套项目，但是未维护子项目价格信息！");
				}
				BigDecimal childPrice = null;// 子项价格
				BigDecimal childQuan = null;// 子项数量
				// 每个子项目的定价模式
				if (EnumerateParameter.FOUR.equals(mapThisServicePrice.get("euPricemode"))) {
					// 取供应链相应的接口的价格
					childPrice = new BigDecimal(",");
				} else {
					childPrice = new BigDecimal(mapThisServicePrice.get("price").toString());
				}
				childQuan = new BigDecimal(mapTemp.get("quan").toString());
				// 经测试多次提bug和需求确认，此处单价不乘数量
				// mapResult.put(mapTemp.get("pkItemChild").toString(),
				// childPrice.multiply(childQuan).toString() + "," + childQuan);
				mapResult.put(mapTemp.get("pkItemChild").toString(), childPrice.toString() + "," + childQuan);

			}

		}
		// 定价模式是对应物品价格
		else if (EnumerateParameter.TWO.equals(bdItem.get("euPricemode").toString())) {
			mapResult.put(pkItem, ",");
		}
		return mapResult;
	}

	/**
	 * 内部医保
	 * @param pkItem 收费项目主键
	 * @param price 项目单价
	 * @param euPvType
	 * @param flag_pd 物品标志
	 * @param euPvtype 就诊流程
	 * @return Map key:患者自付比例 value:自付金额(单价)
	 */
	public Map<BigDecimal, BigDecimal> chargeWithinHealthCare(String pkItem, BigDecimal priceD, String flagPd, String pkHp, String pkOrg, String euPvType, Date dateHap)
			throws BusException {

		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<BigDecimal, BigDecimal> mapResult = new HashMap<BigDecimal, BigDecimal>();
		mapParam.put("pkOrg", pkOrg);
		mapParam.put("pkHp", pkHp);
		mapParam.put("euPvtype", euPvType);
		//查询医保计划类型
		String euHptype = cgQryMaintainService.qryEuHpType(mapParam);
		// 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它
		if (euHptype.equals(EnumerateParameter.FOUR) || euHptype.equals(EnumerateParameter.NINE)) {
			mapParam.put("pkItem", pkItem);
			mapParam.put("flagPd", flagPd);
			mapParam.put("dateHap", dateHap);
			Map<String, Object> mapProjectShareDefine = cgQryMaintainService.qryProjectShareDefine(mapParam);
			// 如果在项目分摊里未找到应该向项目分类找
			if (mapProjectShareDefine == null) {
				Map<String, Object> mapProjectClassificationShareDefine = cgQryMaintainService.qryProjectClassificationShareDefine(mapParam);
				if (mapProjectClassificationShareDefine == null) {
					mapResult.put(BigDecimal.ONE, priceD);
				} else {
					mapResult.put(BigDecimal.ONE.subtract(new BigDecimal(mapProjectClassificationShareDefine.get("rate").toString())), priceD);
				}
			} else {
				//0按比例 1按金额
				String euDivde = mapProjectShareDefine.get("euDivide").toString();
				// 按分摊比例分摊
				if (EnumerateParameter.ZERO.equals(euDivde)) {
					mapResult.put(BigDecimal.ONE.subtract(new BigDecimal(mapProjectShareDefine.get("rate").toString())), priceD);
				}
				// 按分摊金额分摊
				else if (EnumerateParameter.ONE.equals(euDivde)) {
					mapResult.put(BigDecimal.ONE, priceD.subtract(new BigDecimal(mapProjectShareDefine.get("amount").toString())));
				}

			}
		} else {
			mapResult.put(BigDecimal.ONE, priceD);
		}
		return mapResult;
	}
	/**
	 * 批量设置内部医保支付金额（杨雪添加）
	 * @param itemList(含药品，若是药品，药品主键存放到pkItem字段中)
	 * @param pkHp
	 * @param pkOrg
	 * @param dateHap
	 * @return
	 */
	public List<ItemPriceVo> batchChargeAmountWithinHealthCare(List<ItemPriceVo> itemList,BdHp hp, String pkOrg,String dateHap){
		// 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它
		if (EnumerateParameter.FOUR.equals(hp.getEuHptype()) || EnumerateParameter.NINE.equals(hp.getEuHptype())) {
			
			List<BdHpItemdiv> mapProjectShareDefine = cgQryMaintainService.qryHpItemDivDefine(hp.getPkHp(), dateHap);
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("pkOrg", pkOrg);
			mapParam.put("pkHp", hp.getPkHp());
			try {
				mapParam.put("dateHap", DateUtils.getDefaultDateFormat().parse(dateHap));
			} catch (ParseException e) {
				throw new BusException("计算内部医保分摊费用时，传入的费用发生日期dateHap格式不合法！");
			}
			//确定每个项目的分摊价格
			for(ItemPriceVo itemvo:itemList){
				// 如果在项目分摊里未找到应该向项目分类找
				boolean hasDiv = false;//是否被匹配到分摊记录标志
				if(mapProjectShareDefine != null&&mapProjectShareDefine.size()>0){
					for(BdHpItemdiv itemdiv:mapProjectShareDefine){
						//增加药品标志为了区分存在药品主键与收费项目主键相同的情况
						if(itemvo.getPkItem().equals(itemdiv.getPkItem())&&itemvo.getFlagPd().equals(itemdiv.getFlagPd())){
							// 按分摊比例分摊
							if (EnumerateParameter.ZERO.equals(itemdiv.getEuDivide().toString())) {
								itemvo.setAmountMed(new BigDecimal(itemvo.getPriceOrg()).multiply(new BigDecimal(itemdiv.getRate())));
								itemvo.setPrice(itemvo.getPriceOrg().doubleValue());
								itemvo.setRatioSelf((BigDecimal.ONE.subtract(new BigDecimal(itemdiv.getRate()))).doubleValue());
							}
							// 按分摊金额分摊
							else if (EnumerateParameter.ONE.equals(itemdiv.getEuDivide().toString())) {
								itemvo.setAmountMed(new BigDecimal(itemdiv.getAmount()));
								itemvo.setRatioSelf(1.0);
								itemvo.setPrice(new BigDecimal(itemvo.getPriceOrg()).subtract(new BigDecimal(itemdiv.getAmount())).doubleValue());
							}
							hasDiv = true;
							break;
						}
					}
					if(!hasDiv){//未匹配到项目分摊记录，则继续匹配收费分类分摊记录
						mapParam.put("pkItem", itemvo.getPkItem());
						mapParam.put("flagPd", itemvo.getFlagPd());
						Map<String, Object> mapProjectClassificationShareDefine = cgQryMaintainService.qryProjectClassificationShareDefine(mapParam);
						if (mapProjectClassificationShareDefine == null) {
							itemvo.setAmountMed(BigDecimal.ZERO);
							itemvo.setRatioSelf(1.0);
							itemvo.setPrice(itemvo.getPriceOrg().doubleValue());
						} else {
							itemvo.setAmountMed(new BigDecimal(mapProjectClassificationShareDefine.get("rate").toString()).multiply(new BigDecimal(itemvo.getPriceOrg())));
							itemvo.setRatioSelf((BigDecimal.ONE.subtract(new BigDecimal(mapProjectClassificationShareDefine.get("rate").toString()))).doubleValue());
							itemvo.setPrice(itemvo.getPriceOrg().doubleValue());
						}
					}
				}else{
					mapParam.put("pkItem", itemvo.getPkItem());
					mapParam.put("flagPd", itemvo.getFlagPd());
					Map<String, Object> mapProjectClassificationShareDefine = cgQryMaintainService.qryProjectClassificationShareDefine(mapParam);
					if (mapProjectClassificationShareDefine == null) {
						itemvo.setAmountMed(BigDecimal.ZERO);
						itemvo.setRatioSelf(1.0);
						itemvo.setPrice(itemvo.getPriceOrg().doubleValue());
					} else {
						itemvo.setAmountMed(new BigDecimal(mapProjectClassificationShareDefine.get("rate").toString()).multiply(new BigDecimal(itemvo.getPriceOrg())));
						itemvo.setRatioSelf((BigDecimal.ONE.subtract(new BigDecimal(mapProjectClassificationShareDefine.get("rate").toString()))).doubleValue());
						itemvo.setPrice(itemvo.getPriceOrg().doubleValue());
					}
				}
			}
		} else {
			for(ItemPriceVo itemvo:itemList){
				itemvo.setAmountMed(BigDecimal.ZERO);
				itemvo.setRatioSelf(1.0);
				itemvo.setPrice(itemvo.getPriceOrg().doubleValue());
			}
		}
		return itemList;
	}
	/**
	 * 批量设置患者优惠比例(杨雪添加)
	 * @param pkItem 收费项目主键
	 * @param flagPd 物品标志
	 * @param pvVo 患者就诊信息
	 * @param dateHap 费用发生日期
	 * @return 患者自付比例
	 * @throws BusException
	 */
	public List<ItemPriceVo> batchChargePatientFavorable(List<ItemPriceVo> itemList,BdHp hp,String pkOrg,String dateHap) throws BusException {
		// 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它
		if ("5".equals(hp.getEuHptype())) {
			List<BdHpItemdiv> mapProjectShareDefine = cgQryMaintainService.qryHpItemDivDefine(hp.getPkHp(), dateHap);
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("pkOrg", pkOrg);
			mapParam.put("pkHp", hp.getPkHp());
			try {
				mapParam.put("dateHap", DateUtils.getDefaultDateFormat().parse(dateHap));
			} catch (ParseException e) {
				throw new BusException("计算内部医保分摊费用时，传入的费用发生日期dateHap格式不合法！");
			}
			//确定每个项目的优惠比例
			for(ItemPriceVo itemvo:itemList){
				// 如果在项目分摊里未找到应该向项目分类找
				boolean hasDiv = false;//是否被匹配到分摊记录标志
				if(mapProjectShareDefine != null&&mapProjectShareDefine.size()>0){
					for(BdHpItemdiv itemdiv:mapProjectShareDefine){
						if(itemvo.getPkItem().equals(itemdiv.getPkItem())&&itemvo.getFlagPd().equals(itemdiv.getFlagPd())){
							// 按分摊比例分摊
							if (EnumerateParameter.ZERO.equals(itemdiv.getEuDivide().toString())) {
								itemvo.setPkDisc(hp.getPkHp());
								itemvo.setRatioDisc((BigDecimal.ONE.subtract(new BigDecimal(itemdiv.getRate()))).doubleValue());
							}
							// 按分摊金额分摊
							else if (EnumerateParameter.ONE.equals(itemdiv.getEuDivide().toString())) {
								itemvo.setPkDisc(hp.getPkHp());
								itemvo.setRatioDisc(1.0);
							}
							hasDiv = true;
							break;
						}
					}
					if(!hasDiv){//未匹配到项目分摊记录，则继续匹配收费分类分摊记录
						mapParam.put("pkItem", itemvo.getPkItem());
						mapParam.put("flagPd", itemvo.getFlagPd());
						Map<String, Object> mapProjectClassificationShareDefine = cgQryMaintainService.qryProjectClassificationShareDefine(mapParam);
						if (mapProjectClassificationShareDefine == null) {
							itemvo.setPkDisc(hp.getPkHp());
							itemvo.setRatioDisc(1.0);
						} else {
							itemvo.setPkDisc(hp.getPkHp());
							itemvo.setRatioDisc((BigDecimal.ONE.subtract(new BigDecimal(mapProjectClassificationShareDefine.get("rate").toString()))).doubleValue());
						}
					}
				}else{
					mapParam.put("pkItem", itemvo.getPkItem());
					mapParam.put("flagPd", itemvo.getFlagPd());
					Map<String, Object> mapProjectClassificationShareDefine = cgQryMaintainService.qryProjectClassificationShareDefine(mapParam);
					if (mapProjectClassificationShareDefine == null) {
						itemvo.setPkDisc(hp.getPkHp());
						itemvo.setRatioDisc(1.0);
					} else {
						itemvo.setPkDisc(hp.getPkHp());
						itemvo.setRatioDisc((BigDecimal.ONE.subtract(new BigDecimal(mapProjectClassificationShareDefine.get("rate").toString()))).doubleValue());
					}
				}
			}
			
		} else {
			for(ItemPriceVo itemvo:itemList){
				itemvo.setPkDisc(hp.getPkHp());
				itemvo.setRatioDisc(1.0);
			}
		}
		return itemList;
	}
	/**
	 * 查询内部医保支付金额
	 * @param pkItem 收费项目主键
	 * @param price 项目单价
	 * @param flag_pd 物品标志
	 * @param euPvtype 就诊流程
	 * @return Double 医保支付金额
	 */
	public BigDecimal chargeAmountWithinHealthCare(String pkItem, String price, String flagPd, String pkHp, String pkOrg, PvEncounter pvVo, Date dateHap)
			throws BusException {

		Map<String, Object> mapParam = new HashMap<String, Object>();
		BigDecimal result = BigDecimal.ZERO;
		mapParam.put("pkOrg", pkOrg);
		mapParam.put("euPvtype", pvVo.getEuPvtype());
		mapParam.put("pkHp", pkHp);
		BigDecimal priceD = new BigDecimal(price);
		String euHptype = cgQryMaintainService.qryEuHpType(mapParam);
		// 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它
		if (euHptype.equals(EnumerateParameter.FOUR) || euHptype.equals(EnumerateParameter.NINE)) {
			mapParam.put("pkItem", pkItem);
			mapParam.put("flagPd", flagPd);
			mapParam.put("dateHap", dateHap);
			Map<String, Object> mapProjectShareDefine = cgQryMaintainService.qryProjectShareDefine(mapParam);
			// 如果在项目分摊里未找到应该向项目分类找
			if (mapProjectShareDefine == null) {
				Map<String, Object> mapProjectClassificationShareDefine = cgQryMaintainService.qryProjectClassificationShareDefine(mapParam);
				if (mapProjectClassificationShareDefine == null) {
					result = BigDecimal.ZERO;
				} else {
					result = new BigDecimal(mapProjectClassificationShareDefine.get("rate").toString()).multiply(priceD);
				}
			} else {
				String euDivde = mapProjectShareDefine.get("euDivide").toString();
				// 按分摊比例分摊
				if (EnumerateParameter.ZERO.equals(euDivde)) {
					result = priceD.multiply(new BigDecimal(mapProjectShareDefine.get("rate").toString()));
				}
				// 按分摊金额分摊
				else if (EnumerateParameter.ONE.equals(euDivde)) {
					result = new BigDecimal(mapProjectShareDefine.get("amount").toString());
				}
			}
		} else {
			result = BigDecimal.ZERO;
		}
		return result;
	}

	/**
	 * 患者优惠
	 * @param pkItem 收费项目主键
	 * @param flagPd 物品标志
	 * @param pvVo 患者就诊信息
	 * @param dateHap 费用发生日期
	 * @return 患者自付比例
	 * @throws BusException
	 */
	public BigDecimal chargePatientFavorable(String pkItem, String flagPd, String pkHp, PvEncounter pvVo, Date dateHap) throws BusException {

		BigDecimal result = BigDecimal.ZERO;
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", pvVo.getPkOrg());
		mapParam.put("euPvtype", pvVo.getEuPvtype());
		mapParam.put("pkHp", pkHp);
		String euHptype = cgQryMaintainService.qryEuHpType(mapParam);
		// 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它
		if (euHptype.equals("5")) {
			mapParam.put("pkItem", pkItem);
			mapParam.put("flagPd", flagPd);
			mapParam.put("dateHap", dateHap);
			Map<String, Object> mapProjectShareDefine = cgQryMaintainService.qryProjectShareDefine(mapParam);
			// 如果在项目分摊里未找到应该向项目分类找
			if (mapProjectShareDefine == null) {
				Map<String, Object> mapProjectClassificationShareDefine = cgQryMaintainService.qryProjectClassificationShareDefine(mapParam);
				if (mapProjectClassificationShareDefine == null) {
					result = BigDecimal.ONE;
				} else {
					result = BigDecimal.ONE.subtract(new BigDecimal(mapProjectClassificationShareDefine.get("rate").toString()));
				}
			} else {
				String euDivde = mapProjectShareDefine.get("euDivide").toString();
				// 按分摊比例分摊
				if (EnumerateParameter.ZERO.equals(euDivde)) {
					result = BigDecimal.ONE.subtract(new BigDecimal(mapProjectShareDefine.get("rate").toString()));
				}
				// 按分摊金额分摊
				else if ("1".equals(euDivde)) {
					result = BigDecimal.ONE;
				}
			}
			
		} else {
			result = BigDecimal.ONE;
		}
		return result;
	}

	/**
	 * 计算患者结算分摊信息
	 * @param totalAmount 结算金额
	 * @param pvVo 就诊信息
	 * @return Map 自付金额
	 */
	public BigDecimal qryPatiSettlementAmountAllocationInfo(BigDecimal totalAmount, String pkOrg, String euPvType, String pkHp) {

		BigDecimal result = BigDecimal.ZERO;
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, BigDecimal> mapTempPub = null;
		mapParam.put("pkOrg", pkOrg);
		mapParam.put("pkHp", pkHp);
		mapParam.put("dateCur", new Date());
		mapParam.put("euPvtype", euPvType);
		String euHptype = cgQryMaintainService.qryEuHpType(mapParam);
		if (euHptype.equals(EnumerateParameter.FOUR) || euHptype.equals(EnumerateParameter.NINE)) {
			Map<String, Object> mapHealthTotalShareDefine = cgQryMaintainService.qryHealthTotalShareDefine(mapParam);

			// 如果医保总额分摊为空，则查询支付段分摊定义
			if (mapHealthTotalShareDefine == null) {
				List<Map<String, Object>> mapPaymentPeriodShareDefine = cgQryMaintainService.qryPaymentPeriodShareDefine(mapParam);

				if (CollectionUtils.isNotEmpty(mapPaymentPeriodShareDefine)) {
					List<Map<String, BigDecimal>> tempMapList = new ArrayList<Map<String, BigDecimal>>();
					int tempI = 0;
					for (Map<String, Object> mapTemp : mapPaymentPeriodShareDefine) {
						BlcgUtil.validateEmptyValueMap(mapTemp);
						String euDivide = mapTemp.get("euDivide").toString();
						BigDecimal amt_min = new BigDecimal(mapTemp.get("amtMin").toString());
						BigDecimal amt_max = new BigDecimal(mapTemp.get("amtMax").toString());
						BigDecimal amount = new BigDecimal(mapTemp.get("amount").toString());
						BigDecimal rate = new BigDecimal(mapTemp.get("rate").toString());
						if (BlcgUtil.rangeInDefined(totalAmount.doubleValue(), amt_min.doubleValue(), amt_max.doubleValue())) {
							// 按优惠比例分摊
							if (EnumerateParameter.ZERO.equals(euDivide)) {
								if (tempI == 0) {
									//result = MathUtils.mul((totalAmount.doubleValue() - amt_min), (1D - rate));
									result = (totalAmount.subtract(amt_min)).multiply(BigDecimal.ONE.subtract(rate));
									
									break;
								} else {
									for (Map<String, BigDecimal> mapTempChild : tempMapList) {
										if (mapTempChild.get("euDivide").compareTo(BigDecimal.ZERO)==0) {
											//result += MathUtils.mul((mapTempChild.get("amtMax") - mapTempChild.get("amtMin")), 1 - mapTempChild.get("rate"));
											result = result.add((mapTempChild.get("amtMax").subtract(mapTempChild.get("amtMin"))).multiply(BigDecimal.ONE.subtract(mapTempChild.get("rate"))));
										} else {
											result = result.add((mapTempChild.get("amtMax").subtract(mapTempChild.get("amtMin"))).subtract(mapTempChild.get("amount")));
											//result += (mapTempChild.get("amtMax") - mapTempChild.get("amtMin")) - mapTempChild.get("amount");
										}
									}
									result = result.add((totalAmount.subtract(amt_min)).multiply(BigDecimal.ONE.subtract(rate)));
									//result += MathUtils.mul((totalAmount - amt_min), (1D - rate));
									break;
								}

							}
							// 按优惠金额分摊
							else if (EnumerateParameter.ONE.equals(euDivide)) {
								if (tempI == 0) {
									result = (totalAmount.subtract(amount).compareTo(BigDecimal.ZERO) <= 0) ? amt_min : totalAmount.subtract(amount);
									break;
								} else {
									for (Map<String, BigDecimal> mapTempChild : tempMapList) {
										if (mapTempChild.get("euDivide").compareTo(BigDecimal.ZERO)==0) {
											//result += MathUtils.mul((mapTempChild.get("amtMax") - mapTempChild.get("amtMin")), 1 - mapTempChild.get("rate"));
											result = result.add((mapTempChild.get("amtMax").subtract(mapTempChild.get("amtMin"))).multiply(new BigDecimal(1).subtract(mapTempChild.get("rate"))));
										} else {
											//result += (mapTempChild.get("amtMax") - mapTempChild.get("amtMin")) - mapTempChild.get("amount");
											result = result.add((mapTempChild.get("amtMax").subtract(mapTempChild.get("amtMin"))).subtract(mapTempChild.get("amount")));
										}
									}
									//result += (totalAmount - amt_min <= 0) ? amt_min : totalAmount - amt_min;
									result = result.add((totalAmount.subtract(amt_min).compareTo(BigDecimal.ZERO)<=0)?amt_min:totalAmount.subtract(amt_min));
									break;
								}
							}
						}
						mapTempPub = new HashMap<String, BigDecimal>();
						mapTempPub.put("amtMin", amt_min);
						mapTempPub.put("amtMax", amt_max);
						mapTempPub.put("amount", amount);
						mapTempPub.put("rate", rate);
						mapTempPub.put("euDivide", new BigDecimal(euDivide));
						tempMapList.add(mapTempPub);
						tempI++;
					}
				} else {
					// 如果支付段分摊也为空
					result = totalAmount;
				}
			} else {
				BlcgUtil.validateEmptyValueMap(mapHealthTotalShareDefine);
				String euDivide = mapHealthTotalShareDefine.get("euDivide").toString();
				BigDecimal amount = new BigDecimal(mapHealthTotalShareDefine.get("amount").toString());
				BigDecimal rate = new BigDecimal(mapHealthTotalShareDefine.get("rate").toString());
				// 按自付比例分摊
				if (EnumerateParameter.ZERO.equals(euDivide)) {
					//result = MathUtils.mul(totalAmount, 1D - rate);
					result = totalAmount.multiply(new BigDecimal(1).subtract(rate));
				} else if (EnumerateParameter.ONE.equals(euDivide)) {
					result = totalAmount.subtract(totalAmount.compareTo(amount)>0 ? amount : totalAmount);
				}
			}
		} else {
			result = totalAmount;
		}
		return result.compareTo(BigDecimal.ZERO) == 0 ? totalAmount : result;
	}

	/**
	 * 得到所有与价格相关的信息(门诊计费时不考虑辅医保，辅医保在结算时使用，并且只有总额、分段分摊有用)
	 * 
	 * @return
	 * @throws BusException
	 */
	public List<BlPubPriceVo> getItemPriceAllInfo(String pkItem, String flagPd, Double price, PvEncounter pvVo, Date dateHap) throws BusException {
		if (pvVo.getPkPicate() == null){
			throw new BusException("该患者本次就诊未设置患者类别！！！");
		}
		List<BlPubPriceVo> bpps = new ArrayList<BlPubPriceVo>();
		BlPubPriceVo bpp = null;
		Map<String, String> mapChargePriceModel = new HashMap<String, String>();
		// 如果是药品直接取外部传入价格
		if (BlcgUtil.converToTrueOrFalse(flagPd)) {
			if (price == 0.0) {
				throw new BusException(pkItem + " 药品计费时必须从外部传入价格");
			}
			mapChargePriceModel.put(pkItem, price + ",1");
		} else {
			// 非药品走定价策略
			mapChargePriceModel = chargePriceModel(pkItem, pvVo.getPkInsu(), pvVo.getPkOrg(), dateHap);
		}
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkPicate", pvVo.getPkPicate());// 患者类别
		mapParam.put("pkOrg", pvVo.getPkOrg());
		PiCate piCate = cgQryMaintainService.qryPiCateInfo(mapParam);
		boolean flagPicate = ((piCate == null) ? false : true);
		// 如果是药品,此循环只循环一次
		for (Map.Entry<String, String> entry : mapChargePriceModel.entrySet()) {
			bpp = new BlPubPriceVo();
			String pkItemTemp = entry.getKey();
			BigDecimal priceOrg = new BigDecimal(entry.getValue().split(",")[0]);
			bpp.setPkItem(pkItemTemp);// 收费项目主键
			bpp.setPriceOrg(priceOrg);// 原始单价
			bpp.setQuanChild(new BigDecimal(entry.getValue().split(",")[1]));// 子项数量
			// 计算患者优惠比例，设置与优惠相关字段
			if (flagPicate) {
				if (piCate.getPkHp() == null) {
					throw new BusException("患者类别:【" + piCate.getName() + "】对应的医保计划主键未维护");
				}
				BigDecimal rate = chargePatientFavorable(pkItemTemp, flagPd, piCate.getPkHp(), pvVo, dateHap);
				bpp.setRatioDisc(rate);// 患者优惠比例
				bpp.setPkDisc(piCate.getPkHp());// 患者优惠类型
			} else {
				bpp.setRatioDisc(BigDecimal.ONE);// 患者优惠比例
			}
			//内部医保（主医保），设置自付与当前单价相关字段
			Map<BigDecimal, BigDecimal> mapChargeWithinHealthCare = chargeWithinHealthCare(pkItemTemp, priceOrg, flagPd, pvVo.getPkInsu(), pvVo.getPkOrg(),
					pvVo.getEuPvtype(), dateHap);
			// 此循环只循环一次
			for (Map.Entry<BigDecimal, BigDecimal> entryTemp : mapChargeWithinHealthCare.entrySet()) {
				bpp.setRatioSelf(entryTemp.getKey());// 患者自付比例
				bpp.setPrice(entryTemp.getValue());// 当前单价
			}
			bpps.add(bpp);
		}

		return bpps;
	}

	/**
	 * 目前只限门诊收费代录医嘱使用
	 * 
	 * 获取药品在仓库所对应的药品信息
	 * @param blOpDrugStorePriceInfo
	 * @return
	 */
	public BlOpDrugStorePriceInfo queryDrugStorePriceInfo(BlOpDrugStorePriceInfo blOpDrugStorePriceInfo) {

		String pkDept = blOpDrugStorePriceInfo.getPkDept();
		if (StringUtils.isEmpty(pkDept)) {
			throw new BusException("取药品价格时未传入执行科室");
		}
		String pkPd = blOpDrugStorePriceInfo.getPkPd();
		if (StringUtils.isEmpty(pkPd)) {
			throw new BusException("取药品价格时未传入药品主键");
		}
		// 保留4位小数
		DecimalFormat df = new DecimalFormat(".####");
		String pkOrg = blOpDrugStorePriceInfo.getPkOrg();
		Double quanAp = blOpDrugStorePriceInfo.getQuanAp();
		String namePd = blOpDrugStorePriceInfo.getNamePd();
		StringBuffer sql = new StringBuffer();
		// 根据科室和仓库的一对一性，确定该药品所在的仓库
		sql.append("select * from BD_STORE where PK_DEPT=?");
		BdStore store = DataBaseHelper.queryForBean(sql.toString(), BdStore.class, pkDept);
		String euOuttype = store.getEuOuttype();// 获取次仓库的出库方式
		sql.setLength(0);
		// 根据药品和仓库确定药品在该仓库的相关属性
		sql.append("SELECT pd.PK_PD,pd.name,pd.pack_size pack_size_pd,con.PK_UNIT,con.PACK_SIZE,ps.BATCH_NO,ps.DATE_EXPIRE,ps.PRICE_COST,ps.PRICE,");
		sql.append(" ps.QUAN_MIN,ps.QUAN_PREP FROM BD_PD pd");
		sql.append(" INNER JOIN BD_PD_STORE pds ON pd.PK_PD = pds.PK_PD AND pds.PK_STORE = ? AND pds.FLAG_STOP !='1'  ");
		sql.append(" INNER JOIN BD_PD_CONVERT con ON con.PK_PDCONVERT = pds.PK_PDCONVERT");
		sql.append(" INNER JOIN PD_STOCK ps ON ps.PK_STORE = pds.PK_STORE and pds.PK_PD=ps.PK_PD");
		sql.append(" INNER JOIN PD_ST_DETAIL psd on psd.PK_PD=ps.PK_PD");
		sql.append(" INNER JOIN PD_ST pdst ON pdst.PK_PDST=psd.PK_PDST");
		sql.append(" WHERE pds.PK_PD=? and ps.PK_ORG=?  and ps.QUAN_MIN>0");
		if ("0".equals(euOuttype)) {// 先进先出
			sql = sql.append(" order by pdst.date_chk ");
		} else if ("1".equals(euOuttype)) {// 有效期
			sql = sql.append(" order by dt.date_expire  ");
		} else if ("2".equals(euOuttype)) {// 按后进先出
			sql = sql.append(" order by pdst.date_chk desc");
		}
		// 按道理说一个物品在物品仓库中只有一条记录，但是因为如果该药品在入库时某些值不一样，是不能按同一个药品对待，比如批号，失效日期,生产日期，零售单价..等等不一样是不能按同一个药品对待的
		// 所以在此该药品在此仓库中有可能有多条记录
		List<Map<String, Object>> mapInfo = DataBaseHelper.queryForList(sql.toString(), store.getPkStore(), pkPd, pkOrg);
		Double quanMin = 0D;
		for (Map<String, Object> map : mapInfo) {
			quanMin += Double.parseDouble(map.get("quanMin").toString());
			if(map.get("batchNo") == null){
				throw new BusException("该药品批号为空，无法保存");
			}
			String batchNo = map.get("batchNo").toString();
			Double priceCost = Double.parseDouble(map.get("priceCost").toString());
			Double price = Double.parseDouble(map.get("price").toString());
			Date dateExpire;
			try {
				dateExpire = map.get("dateExPire") == null ? null : DateUtils.getDefaultDateFormat().parse(map.get("dateExPire").toString());
			} catch (ParseException e) {
				throw new BusException("获取库存信息时转换有效日期异常！");
			}
			Integer packSize = Integer.parseInt(map.get("packSize").toString());
			Integer packSizePd = Integer.parseInt(map.get("packSizePd").toString());
			String pkUnit = map.get("pkUnit").toString();
			if (quanAp.compareTo(MathUtils.div(quanMin, packSize.doubleValue(), 2)) <= 0) {
				blOpDrugStorePriceInfo.setBatchNo(batchNo);
				blOpDrugStorePriceInfo.setPriceCost(Double.parseDouble(df.format((priceCost / packSizePd) * packSize)));
				blOpDrugStorePriceInfo.setPrice(Double.parseDouble(df.format((price / packSizePd) * packSize)));
				blOpDrugStorePriceInfo.setDateExpire(dateExpire);
				blOpDrugStorePriceInfo.setPackSize(packSize);
				blOpDrugStorePriceInfo.setPackSizePd(packSizePd);
				blOpDrugStorePriceInfo.setPkUnit(pkUnit);
				return blOpDrugStorePriceInfo;
			} else {
				continue;
			}
		}
		throw new BusException("药品[" + namePd + "]在[" + store.getName() + "]里没有库存记录或者已经停用，请检查！");
	}
}
