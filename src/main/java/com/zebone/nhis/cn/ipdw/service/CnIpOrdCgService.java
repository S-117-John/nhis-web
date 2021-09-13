package com.zebone.nhis.cn.ipdw.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.support.CgProcessUtils;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlCgPdParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.cn.ipdw.dao.CnOrderMapper;
import com.zebone.nhis.cn.ipdw.dao.CnOrderSyncMapper;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnOrderSync;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 住院医嘱计费服务-同步接口用
 * @author chengjia
 *
 */
@Service
public class CnIpOrdCgService {

	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	
	@Autowired
	private PdStOutPubService pdStOutPubService;
	
	@Autowired
	private PriceStrategyService priceStrategyService;
	

	/**
	 * 住院批量记费统一入口，支持药品、医嘱项目、收费项目(单患者)
	 * @param blIpCgPubParamVos
	 * @return
	 * @throws BusException
	 */
	public BlPubReturnVo blIpCgBatch(List<BlPubParamVo> blIpCgPubParamVos) throws BusException {
		//先判断是否存在个性化记费策略，存在则使用个性化记费服务记费，否则使用默认服务记费
		Map<String,Object> rtn = CgProcessUtils.processOpCg(blIpCgPubParamVos);
		if(rtn!=null&&"true".equals(rtn.get("enable"))){
			return rtn.get("result")==null?null:(BlPubReturnVo)rtn.get("result");
		}
		BlPubReturnVo blCgPubReturnVo = new BlPubReturnVo();
		if (blIpCgPubParamVos.size() <= 0)
			return blCgPubReturnVo;
		String codeCg = ApplicationUtils.getCode("0601");
		Map<String, Object> mapParam = new HashMap<String, Object>();
		//取患者就诊属性
		String pkPv =  blIpCgPubParamVos.get(0).getPkPv();
		String pkOrg = blIpCgPubParamVos.get(0).getPkOrg(); 
		String dateHap = DateUtils.getDateTimeStr(new Date());//记费日期
		mapParam.put("pkPv", pkPv);
		mapParam.put("pkOrg", pkOrg);
		PvEncounter pvVo = cgQryMaintainService.qryPvEncounterInfo(mapParam);
		mapParam.put("pkHp", pvVo.getPkInsu());
		BdHp hp = cgQryMaintainService.qryBdHpInfo(mapParam);
		if(hp==null)
			throw new BusException("未找到患者本次就诊对应的主医保信息！");
		mapParam.put("euPvType", pvVo.getEuPvtype());
		BdHp cateHp = null;
		//若存在患者分类，查询患者优惠信息
		if(pvVo.getPkPicate()!=null&&!pvVo.getPkPicate().equals("")){
			Map<String,Object> paramTemp = new HashMap<String,Object>();
			paramTemp.put("pkPicate", pvVo.getPkPicate());// 患者类别
			cateHp = cgQryMaintainService.qryBdHpByPiCate(paramTemp);
			if (cateHp == null)
				throw new BusException("未找到患者类别对应的优惠内容，请核对患者分类对应的医保计划！");
		}
		//分组药品与非药品（药品走库存询价流程，非药品走价格策略）
		List<BlPubParamVo> pdList = new ArrayList<BlPubParamVo>();
		//针对药品，存放执行药房及申请的药品以及申请数量(基本单位对应的数量)集合，用来询价
		Map<String,BlCgPdParamVo> storeMap = new HashMap<String,BlCgPdParamVo>();
		
		//医嘱项目集合
		List<BlPubParamVo> ordList = new ArrayList<BlPubParamVo>();
		List<String> pkOrdList = new ArrayList<String>();
		//收费项目集合
		List<BlPubParamVo> itemList = new ArrayList<BlPubParamVo>();
		List<String> pkItemList = new ArrayList<String>();
		
		//医嘱及收费项目集合
		List<BlPubParamVo> ordItemAllList = new ArrayList<BlPubParamVo>();
		
		for(BlPubParamVo blOpCgPubParamVo : blIpCgPubParamVos){
			if(blOpCgPubParamVo.getQuanCg()==null) blOpCgPubParamVo.setQuanCg(new Double("1"));
			//如果是物品，组装询价集合
			if(BlcgUtil.converToTrueOrFalse(blOpCgPubParamVo.getFlagPd())){
				if(CommonUtils.isEmptyString(blOpCgPubParamVo.getPkOrd()))
					throw new BusException("调用保存记费明细接口时未传入记费药品主键！");
				if(blOpCgPubParamVo.getPackSize()<=0)
					throw new BusException("调用保存记费明细接口时未传入记费药品对应记费数量的包装量！");
				if(CommonUtils.isEmptyString(blOpCgPubParamVo.getPkDeptEx()))
					throw new BusException("调用保存记费明细接口时未传入记费药品对应的发药科室！");
				BigDecimal quanMin = new BigDecimal(MathUtils.mul(blOpCgPubParamVo.getQuanCg(),CommonUtils.getDouble(blOpCgPubParamVo.getPackSize())));
				boolean hasExDept = false;
				for(Map.Entry<String, BlCgPdParamVo> entry: storeMap.entrySet())
		        {
					if(blOpCgPubParamVo.getPkDeptEx().equals(entry.getKey())){
						//遍历该集合内容
						BlCgPdParamVo paramVo = entry.getValue();
						List<String> pds = paramVo.getPkPds();
						if(pds==null){
							pds = new ArrayList<String>();
						}
						pds.add(blOpCgPubParamVo.getPkOrd());
						paramVo.setPkPds(pds);
						Map<String, BigDecimal> quanMap = paramVo.getQuanMinMap();
						if(quanMap == null) quanMap = new HashMap<String,BigDecimal>();
						boolean hasPd = false;
						for(Map.Entry<String, BigDecimal> quanMinEntry: quanMap.entrySet())
				        {
							if(blOpCgPubParamVo.getPkOrd().equals(quanMinEntry.getKey())){
								quanMinEntry.setValue(quanMinEntry.getValue().add(quanMin));
								hasPd = true;
								break;
							}
				        }
						if(!hasPd){
							quanMap.put(blOpCgPubParamVo.getPkOrd(), quanMin);
						}
						hasExDept = true;
						break;
					}
		        }
				if(!hasExDept){
					BlCgPdParamVo newpdvo = new BlCgPdParamVo();
					List<String> newpds = new ArrayList<String>();
					newpds.add(blOpCgPubParamVo.getPkOrd());
					Map<String,BigDecimal> newquanMap = new HashMap<String,BigDecimal>(); 
					newquanMap.put(blOpCgPubParamVo.getPkOrd(), quanMin);
					
					newpdvo.setPkPds(newpds);
					newpdvo.setQuanMinMap(newquanMap);
					
					storeMap.put(blOpCgPubParamVo.getPkDeptEx(), newpdvo);
				}
				pdList.add(blOpCgPubParamVo);
				
			}else{
				if(CommonUtils.isEmptyString(blOpCgPubParamVo.getPkOrd())&&CommonUtils.isEmptyString(blOpCgPubParamVo.getPkItem()))
					throw new BusException("调用保存记费明细接口时未传入收费项目或医嘱项目主键！");
				if(blOpCgPubParamVo.getPkOrd()!=null&&!"".equals(blOpCgPubParamVo.getPkOrd())){
					ordList.add(blOpCgPubParamVo);//通过医嘱项目进行记费的集合
					pkOrdList.add(blOpCgPubParamVo.getPkOrd());
				}else{
					itemList.add(blOpCgPubParamVo);//通过收费项目进行记费的集合
					pkItemList.add(blOpCgPubParamVo.getPkItem());
				}
				ordItemAllList.add(blOpCgPubParamVo);
			}
		}
		//获取价格后的全部记费明细集合
		List<ItemPriceVo> allPriceList = new ArrayList<ItemPriceVo>();
		//取药品价格
		List<ItemPriceVo> pdPrices = setPdPrice(pdList,storeMap);
		if(pdPrices!=null&&pdPrices.size()>0){
			allPriceList.addAll(pdPrices);
		}
		//取非药品价格
		List<ItemPriceVo> itemvos = setOrdAndItemPrice(ordItemAllList,hp,pkOrg,dateHap,pkOrdList,pkItemList);
		if(itemvos!=null&&itemvos.size()>0){
			allPriceList.addAll(itemvos);
		}
		//计算患者优惠比例
		if(cateHp!=null&&allPriceList!=null&&allPriceList.size()>0){
			allPriceList = priceStrategyService.batchChargePatientFavorable(allPriceList,cateHp, pkOrg,dateHap);
		}
		//设置患者自付比例或价格（被分摊后的价格）
		if(allPriceList!=null&&allPriceList.size()>0&&(hp.getDtExthp() == null || hp.getDtExthp().equals("0"))){//内部医保 
			allPriceList = priceStrategyService.batchChargeAmountWithinHealthCare(allPriceList,hp,pkOrg,dateHap);
		}
		//转换为住院记费明细
		List<BlIpDt> bids = constructBlIpDt(allPriceList,codeCg,pkOrg);
		//保存住院记费明细
		if(bids!=null&&bids.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class), bids);
		
		blCgPubReturnVo.setBids(bids);
		return blCgPubReturnVo;
	}
	/**
	 * 构造住院记费明细
	 * @param items 收费项目集合
	 * @return
	 */
	private List<BlIpDt> constructBlIpDt(List<ItemPriceVo> items,String codeCg,String pkOrg){
		List<BlIpDt> bids = new ArrayList<BlIpDt>();
		if(items!=null&&items.size()>0){
		List<String> itemcates = new ArrayList<String>();
		  for(ItemPriceVo pricevo:items){
			  BlIpDt bid = new BlIpDt();
			//相同属性进行拷贝
			try {
				BeanUtils.copyProperties(bid, pricevo);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			bid.setPkCgip(NHISUUID.getKeyId());
			if(bid.getRatioDisc()==null){
				bid.setRatioDisc(1.0);//优惠比例
			}
			if(bid.getRatioSelf()==null){
				bid.setRatioSelf(1.0);//自付比例
			}
			if("1".equals(bid.getFlagPd())){
				bid.setPkPd(pricevo.getPkOrdOld());
			}
			bid.setAmount(MathUtils.mul(bid.getPriceOrg(), bid.getQuan()));// 金额=单价*数量
			double amountMed = MathUtils.mul(MathUtils.add(MathUtils.sub(bid.getPriceOrg(), bid.getPrice()), MathUtils.mul(bid.getPriceOrg(), MathUtils.sub(new Double(1), bid.getRatioSelf()))), bid.getQuan());
			double amountDis = MathUtils.mul(bid.getAmount(), MathUtils.sub(new Double(1),  bid.getRatioDisc()));// 金额（原始单价*数量）*折扣比率
			double tempAmount = MathUtils.add(amountMed, amountDis);
			double amountPi = MathUtils.compareTo(tempAmount, bid.getAmount())>=0 ? 0.0 : MathUtils.sub(bid.getAmount(), tempAmount);
			bid.setAmountPi(amountPi);
			
			bid.setFlagSettle(EnumerateParameter.ZERO);
			bid.setFlagInsu(EnumerateParameter.ZERO);
			
			bid.setCodeCg(codeCg);// 记费编码
			bid.setDateCg(new Date());// 记费日期
			ApplicationUtils.setDefaultValue(bid, true);// 设置默认字段
			bids.add(bid);
			itemcates.add(bid.getPkItemcate());
		 }
            //批量根据费用分类设置发票分类
		    if((itemcates == null||itemcates.size()<=0)&&bids!=null&&bids.size()>0)
		    	throw new BusException("收费项目未维护对应的费用分类，无法完成记费操作！");
		    Map<String,Object> paramMap = new HashMap<String,Object>();
		    paramMap.put("euType", "0");//门诊
		    paramMap.put("pkOrg", pkOrg);
		    paramMap.put("pkItemcates", itemcates);
			List<Map<String, Object>> billCodeList = cgQryMaintainService.qryBillCodeByItemCate(paramMap);
			if (billCodeList == null)
				throw new BusException("未维护费用分类对应发票项目，无法完成记费操作！");
			
			for(BlIpDt boddt :bids){
				for(Map<String, Object> billMap:billCodeList){
					if(boddt.getPkItemcate().equals(billMap.get("pkItemcate").toString())){
						boddt.setCodeBill(billMap.get("code").toString());
						break;
					}
				}
				if(CommonUtils.isEmptyString(boddt.getCodeBill()))
					throw new BusException("【"+boddt.getNameCg()+"】未维护其费用分类对应发票项目，无法完成记费操作！");
			}
			
			List<Map<String, Object>> auditCodeList = cgQryMaintainService.qryAuditCodeByItemcate(paramMap);
			if(auditCodeList!=null&&auditCodeList.size()>0){
				for(BlIpDt boddt :bids){
					for(Map<String, Object> auditMap:billCodeList){
						if(boddt.getPkItemcate().equals(auditMap.get("pkItemcate").toString())){
							boddt.setCodeAudit(auditMap.get("code").toString());
							break;
						}
					}
				}
			}
		}
		return bids;
	}
	
	/**
	 * 设置药品价格
	 * @param pdList
	 * @param storeMap
	 * @return
	 */
	private List<ItemPriceVo> setPdPrice(List<BlPubParamVo> pdList,Map<String,BlCgPdParamVo> storeMap){
		List<ItemPriceVo> pdPrices = new ArrayList<ItemPriceVo>();
		if(pdList.size()>0){
			for(Map.Entry<String, BlCgPdParamVo> entry: storeMap.entrySet())
	        {
				Map<String,Object> storePriceMap = new HashMap<String,Object>();
				storePriceMap.put("pkStore", "");
				storePriceMap.put("pkDeptExec", entry.getKey());
				storePriceMap.put("quanMap", entry.getValue().getQuanMinMap());
				storePriceMap.put("pkPd", entry.getValue().getPkPds());
				List<PdOutDtParamVo> pdOutDtParamVos = pdStOutPubService.getPdStorePrice(storePriceMap,false);
				for(BlPubParamVo co : pdList){
					for(PdOutDtParamVo pdOutDtParamVo:pdOutDtParamVos){
						if(co.getPkOrd().equals(pdOutDtParamVo.getPkPd())){
							ItemPriceVo pdPricevo = new ItemPriceVo();
							ApplicationUtils.copyProperties(pdPricevo, co);
							//pdPricevo.setFlagPd("1");
							pdPricevo.setPkItemcate(pdOutDtParamVo.getPkItemcate());
							pdPricevo.setPkOrdOld(pdOutDtParamVo.getPkPd());
							pdPricevo.setSpec(pdOutDtParamVo.getSpec());
							pdPricevo.setNameCg(pdOutDtParamVo.getName());
							pdPricevo.setPrice(pdOutDtParamVo.getPriceStore());
							pdPricevo.setPkUnit(pdOutDtParamVo.getPkUnitPack());
							pdPricevo.setPkUnitPd(pdOutDtParamVo.getPkUnitPack());
							pdPricevo.setPackSize(pdOutDtParamVo.getPackSize());
							//数量转换为仓库单位对应数量
							pdPricevo.setQuan(MathUtils.div(MathUtils.mul(co.getQuanCg(),CommonUtils.getDouble(co.getPackSize())),CommonUtils.getDouble(pdPricevo.getPackSize())));
							pdPricevo.setPriceOrg(pdOutDtParamVo.getPriceStore());
							pdPricevo.setBatchNo(pdOutDtParamVo.getBatchNo());
							pdPricevo.setDateExpire(pdOutDtParamVo.getDateExpire());
							pdPricevo.setPriceCost(MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPriceCost(), CommonUtils.getDouble(pdOutDtParamVo.getPackSizePd())),CommonUtils.getDouble(pdOutDtParamVo.getPackSize())));
							pdPrices.add(pdPricevo);
							break;
						}
					}
				}
	        }
		}
		return pdPrices;
	}
	
	/**
	 * 设置医嘱项目或收费项目对应的价格、优惠比例、自付比例 yangxue新增
	 * @param ordItemList
	 * @param hp
	 * @param cateHp
	 * @param pkOrg
	 * @param dateHap
	 * @param pkOrdList
	 * @param pkItemList
	 * @return
	 */
	private List<ItemPriceVo> setOrdAndItemPrice(List<BlPubParamVo> ordItemList,BdHp hp,String pkOrg,String dateHap,List<String> pkOrdList,List<String> pkItemList){
		//获取主医保下收费项目对应的价格
		List<ItemPriceVo> itemPriceList = priceStrategyService.batchChargePriceModelByOrdAndItem(hp,pkOrg,dateHap,pkItemList,pkOrdList);
		List<ItemPriceVo> newItemPriceList = new ArrayList<ItemPriceVo>();
		//计算医嘱或收费项目对应数量及单价
		for(BlPubParamVo blOpCgPubParamVo :ordItemList){
			List<ItemPriceVo> prices = new ArrayList<ItemPriceVo>();
			if(blOpCgPubParamVo.getPkOrd()!=null&&!"".equals(blOpCgPubParamVo.getPkOrd())){
				//通过医嘱项目计算
				prices = calPriceAndQuan(itemPriceList,blOpCgPubParamVo.getQuanCg(),blOpCgPubParamVo.getPkOrd(),true);
			}else{
				//通过收费项目计算
				prices = calPriceAndQuan(itemPriceList,blOpCgPubParamVo.getQuanCg(),blOpCgPubParamVo.getPkItem(),false);
			}
			if(prices!=null&&prices.size()>0){
				//设置相应的记费信息,为了转换为记费明细对象，否则经过医保分摊计算后无法还原至原医嘱或收费对象
		    	for(ItemPriceVo pricevo:prices){
		    		pricevo.setPkCnord(blOpCgPubParamVo.getPkCnord());
		    		pricevo.setPkOrgApp(blOpCgPubParamVo.getPkOrgApp());
		    		pricevo.setPkDeptApp(blOpCgPubParamVo.getPkDeptApp());
		    		pricevo.setPkDeptEx(blOpCgPubParamVo.getPkDeptEx());
		    		pricevo.setPkOrgEx(blOpCgPubParamVo.getPkOrgEx());
		    		pricevo.setPkDeptCg(blOpCgPubParamVo.getPkDeptCg());
		    		pricevo.setPkEmpApp(blOpCgPubParamVo.getPkEmpApp());
		    		pricevo.setPkEmpCg(blOpCgPubParamVo.getPkEmpCg());
		    		pricevo.setDateHap(blOpCgPubParamVo.getDateHap());
		    		pricevo.setFlagPv(blOpCgPubParamVo.getFlagPv());
		    		pricevo.setPkPv(blOpCgPubParamVo.getPkPv());
		    		pricevo.setPkPi(blOpCgPubParamVo.getPkPi());
		    		pricevo.setPkPres(blOpCgPubParamVo.getPkPres());
		    		pricevo.setNameEmpApp(blOpCgPubParamVo.getNameEmpApp());
		    		pricevo.setNameEmpCg(blOpCgPubParamVo.getNameEmpCg());
		    		pricevo.setEuAdditem(blOpCgPubParamVo.getEuAdditem());
		    	}
		    	newItemPriceList.addAll(prices);
		    }
			
		}
		return newItemPriceList;
	}
	
	/**
	 * 计算医嘱或收费项目数量及价格 yangxue 新增
	 * @param itemPriceList
	 * @param opOrdDtLists
	 * @param isOrd 是否是医嘱项目，不是默认按收费项目合计计算
	 * @return
	 */
	private List<ItemPriceVo> calPriceAndQuan(List<ItemPriceVo> itemPriceList,Double quan,String pk,boolean isOrd){
		List<ItemPriceVo> resultList = new ArrayList<ItemPriceVo>();
		//计算所有医嘱项目对应价格及数量
		for(ItemPriceVo item:itemPriceList){
			//默认按收费项目为依据
			boolean sameFlag = pk.equals(item.getPkItemOld());
			if(isOrd){
				sameFlag = pk.equals(item.getPkOrdOld());
			}
			if(sameFlag&&item.getPkItemOld().equals(item.getPkItem())){
				//非组套,默认按本服务定价，理论上不存在非组套按组套收费的情况
				if(EnumerateParameter.ZERO.equals(item.getFlagSetOld())){
					if (EnumerateParameter.ZERO.equals(item.getEuPricemodeOld())) {
						item.setQuan(MathUtils.mul(quan==null?1:quan, item.getQuan()));//医嘱开立数量*收费项目设置的数量
						resultList.add(item);
						break;
					}else if(EnumerateParameter.ONE.equals(item.getEuPricemodeOld())){//服务套成员合计定价
						//理论上不存在这种情况，因此暂不写实现
						break;
					}
				}else{//组套
					//该医嘱对应子项集合
					List<ItemPriceVo> childList = new ArrayList<ItemPriceVo>();
					//子项合计价格
					Double sumChildPrice = 0.0;
					//取当前医嘱对应所有子项
					for(ItemPriceVo childitem:itemPriceList){
						//默认按收费项目为依据
						boolean sameFlagChild = pk.equals(childitem.getPkItemOld());
						if(isOrd){
							sameFlagChild = pk.equals(childitem.getPkOrdOld());
						}
						if(sameFlagChild&&!childitem.getPkItemOld().equals(childitem.getPkItem())){
							childList.add(childitem);
							sumChildPrice = MathUtils.add(sumChildPrice,MathUtils.mul(childitem.getQuan(), childitem.getPriceOrg()));
						}
					}
					if(childList == null||childList.size()<=0)
						throw new BusException("组套项目【"+item.getNameItemOld()+"】未获取对应的组套子项及其价格！");
					// 定价模式是本服务定价
					if (EnumerateParameter.ZERO.equals(item.getEuPricemodeOld())) {
						if(sumChildPrice.compareTo(item.getPriceOrg())==0){//子项合计价格 = 本服务价格,直接使用子项价格及数量
							for(ItemPriceVo child:childList){
								child.setQuan(MathUtils.mul(quan==null?0:quan,child.getQuan()));//变更子项数量为医嘱开立数量*子项数量
								resultList.add(child);
							}
						}else{//子项价格与本服务价格不等，需要计算权重
							for(ItemPriceVo child:childList){
								Double weight = (new BigDecimal((MathUtils.mul(child.getPriceOrg(), child.getQuan()))).divide(new BigDecimal(sumChildPrice), 4, BigDecimal.ROUND_HALF_UP)).doubleValue();
								child.setPriceOrg(MathUtils.div(MathUtils.mul(item.getPriceOrg(), weight), child.getQuan()));
								child.setQuan(MathUtils.mul(quan, child.getQuan()));//变更子项数量为医嘱开立数量*子项数量
								resultList.add(child);
							}
						}
					}else if(EnumerateParameter.ONE.equals(item.getEuPricemodeOld())){//服务套成员合计定价
						for(ItemPriceVo child:childList){
							child.setQuan(MathUtils.mul(quan, child.getQuan()));//变更子项数量为医嘱开立数量*子项数量
							resultList.add(child);
						}
					}
				}
			}
		}
	     return resultList;
	}
}
