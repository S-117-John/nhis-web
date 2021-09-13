package com.zebone.nhis.pro.zsba.mz.pub.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.bl.pub.pay.service.ThirdPartyPaymentService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.support.CgProcessUtils;
import com.zebone.nhis.bl.pub.syx.support.CgProcessHandler;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlCgPdParamVo;
import com.zebone.nhis.bl.pub.vo.BlOpPubRebackFeesVo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubPriceVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.BlPubSettleVo;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdItem;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pv.BlExtPayBankVo;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.pro.zsba.mz.pub.support.ZsbaInvPrintProcessUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 门诊记费结算统一服务
 * @author Administrator
 * 
 */
@Service
public class ZsbaOpCgPubService {
	private Logger logger = LoggerFactory.getLogger("nhis.ZsbaOpCgPubServiceLog");
	
	@Autowired private ZsbaCgQryMaintainService cgQryMaintainService;
	@Autowired private PriceStrategyService priceStrategyService;
	@Autowired private BalAccoutService balAccoutService;
	@Autowired private ThirdPartyPaymentService thirdPartyPaymentService;
	@Autowired private OpcgPubHelperService opcgPubHelperService;
	@Autowired private PdStOutPubService pdStOutPubService;
	@Autowired private  CgProcessHandler cgProcessHandler;
	
	
	
	/**
	 * 门诊批量记费统一入口，支持药品、医嘱项目、收费项目(单患者)-yangxue(不含挂号结算)
	 * @param blOpCgPubParamVos
	 * @return
	 * @throws BusException
	 */
	public BlPubReturnVo blOpCgBatch(List<BlPubParamVo> blOpCgPubParamVos) throws BusException {
		//先判断是否存在个性化记费策略，存在则使用个性化记费服务记费，否则使用默认服务记费
		Map<String,Object> rtn = CgProcessUtils.processOpCg(blOpCgPubParamVos);
		if(rtn!=null&&"true".equals(rtn.get("enable"))){
			return rtn.get("result")==null?null:(BlPubReturnVo)rtn.get("result");
		}
		BlPubReturnVo blCgPubReturnVo = new BlPubReturnVo();
		if (blOpCgPubParamVos.size() <= 0)
			return blCgPubReturnVo;
		String codeCg = ApplicationUtils.getCode("0601");
		Map<String, Object> mapParam = new HashMap<String, Object>();
		//取患者就诊属性
		String pkPv =  blOpCgPubParamVos.get(0).getPkPv();
		String pkOrg = blOpCgPubParamVos.get(0).getPkOrg(); 
		String dateHap = DateUtils.getDateTimeStr(new Date());//记费日期
		if(CommonUtils.isEmptyString(pkPv))
			throw new BusException("调用门诊记费方法时，参数中未传入pkPv的值！");
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
		//不走询价流程的药品费用明细集合
		List<ItemPriceVo> pdHasPriceList = new ArrayList<ItemPriceVo>();
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
		
		for(BlPubParamVo blOpCgPubParamVo : blOpCgPubParamVos){
			//如果是物品，组装询价集合
			if(BlcgUtil.converToTrueOrFalse(blOpCgPubParamVo.getFlagPd())){
				if(CommonUtils.isEmptyString(blOpCgPubParamVo.getPkOrd()))
					throw new BusException("调用保存记费明细接口时未传入记费药品主键！");
				if(blOpCgPubParamVo.getPackSize()<=0)
					throw new BusException("调用保存记费明细接口时未传入记费药品对应记费数量的包装量！");
				if(CommonUtils.isEmptyString(blOpCgPubParamVo.getPkDeptEx()))
					throw new BusException("调用保存记费明细接口时未传入记费药品对应的发药科室！");
				if("1".equals(blOpCgPubParamVo.getFlagHasPdPrice())){//已存在价格药品，不走询价流程
					pdHasPriceList.add(cgProcessHandler.constructParam(blOpCgPubParamVo));
				}else{
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
			   }
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
		if(pdHasPriceList!=null&&pdHasPriceList.size()>0){
			allPriceList.addAll(pdHasPriceList);
		}
		//计算患者优惠比例
		if(cateHp!=null&&allPriceList!=null&&allPriceList.size()>0){
			allPriceList = priceStrategyService.batchChargePatientFavorable(allPriceList,cateHp, pkOrg,dateHap);
		}
		//设置患者自付比例或价格（被分摊后的价格）
		if(allPriceList!=null&&allPriceList.size()>0&&(hp.getDtExthp() == null || hp.getDtExthp().equals("0"))){//内部医保 
			allPriceList = priceStrategyService.batchChargeAmountWithinHealthCare(allPriceList,hp,pkOrg,dateHap);
		}
		//转换为门诊记费明细
		//判断年龄加收和儿童加收是否同时生效，0特诊加收不再叠加年龄加收策略，1年龄加收基础上叠加特诊加收策略
		String sysParamBd0016 = ApplicationUtils.getSysparam("BD0016", false);
		int cgSortNo = 1;
		List<BlOpDt> bods = cgProcessHandler.constructBlOpDt(allPriceList,codeCg,cgSortNo,pvVo,sysParamBd0016);
		//保存门诊记费明细
		if(bods!=null&&bods.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), bods);
		
		blCgPubReturnVo.setBods(bods);
		return blCgPubReturnVo;
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
	 * @param pkOrg
	 * @param dateHap
	 * @param pkOrdList
	 * @param pkItemList
	 * @return
	 */
	private List<ItemPriceVo> setOrdAndItemPrice(List<BlPubParamVo> ordItemList,BdHp hp,String pkOrg,String dateHap,List<String> pkOrdList,List<String> pkItemList){
		//获取主医保下收费项目对应的价格
		List<ItemPriceVo> itemPriceList = priceStrategyService.batchChargePriceModelByOrdAndItem(hp,pkOrg,dateHap,pkItemList,pkOrdList);
		if(itemPriceList==null||itemPriceList.size()<=0)
			return null;
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
		    		ItemPriceVo itemvo = new ItemPriceVo();
		    		ApplicationUtils.copyProperties(itemvo, pricevo);
		    		itemvo.setPkCnord(blOpCgPubParamVo.getPkCnord());
		    		itemvo.setPkOrgApp(blOpCgPubParamVo.getPkOrgApp());
		    		itemvo.setPkDeptApp(blOpCgPubParamVo.getPkDeptApp());
		    		itemvo.setPkDeptEx(blOpCgPubParamVo.getPkDeptEx());
		    		itemvo.setPkOrgEx(blOpCgPubParamVo.getPkOrgEx());
		    		itemvo.setPkDeptCg(blOpCgPubParamVo.getPkDeptCg());
		    		itemvo.setPkEmpApp(blOpCgPubParamVo.getPkEmpApp());
		    		itemvo.setPkEmpCg(blOpCgPubParamVo.getPkEmpCg());
		    		itemvo.setDateHap(blOpCgPubParamVo.getDateHap());
		    		itemvo.setFlagPv(blOpCgPubParamVo.getFlagPv());
		    		itemvo.setPkPv(blOpCgPubParamVo.getPkPv());
		    		itemvo.setPkPi(blOpCgPubParamVo.getPkPi());
		    		itemvo.setPkPres(blOpCgPubParamVo.getPkPres());
		    		itemvo.setNameEmpApp(blOpCgPubParamVo.getNameEmpApp());
		    		itemvo.setNameEmpCg(blOpCgPubParamVo.getNameEmpCg());
		    		itemvo.setEuAdditem(blOpCgPubParamVo.getEuAdditem());
		    		itemvo.setPkDeptAreaapp(blOpCgPubParamVo.getPkDeptAreaapp());
		    		newItemPriceList.add(itemvo);
		    	}
		    	
		    }
			
		}
		return newItemPriceList;
	}
	
	/**
	 * 计算医嘱或收费项目数量及价格 yangxue 新增
	 * @param itemPriceList
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
						item.setQuan(MathUtils.mul(quan, item.getQuan()));//医嘱开立数量*收费项目设置的数量
						resultList.add(item);
						//break;
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
								child.setQuan(MathUtils.mul(quan,child.getQuan()));//变更子项数量为医嘱开立数量*子项数量
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
	
	/**
	 * 门诊记费统一入口
	 * @param blOpCgPubParamVos
	 * @return
	 * @throws BusException
	 */
	public BlPubReturnVo blOpCg(List<BlPubParamVo> blOpCgPubParamVos) throws BusException {

		int size = blOpCgPubParamVos.size();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		BlPubReturnVo blCgPubReturnVo = new BlPubReturnVo();
		if (size == 0)
			return blCgPubReturnVo;
		String codeCg = ApplicationUtils.getCode("0601");
		int cgSort = 1;
		List<BlOpDt> bods = new ArrayList<BlOpDt>();
		for (BlPubParamVo blOpCgPubParamVo : blOpCgPubParamVos) {
			BlPubParamVo blOpCgPubParamVoGlobal = blOpCgPubParamVo;
			String pkOrg = blOpCgPubParamVoGlobal.getPkOrg();
			String pkPv = blOpCgPubParamVoGlobal.getPkPv();
			mapParam.put("pkPv", pkPv);
			mapParam.put("pkOrg", pkOrg);
			String flagPd = blOpCgPubParamVo.getFlagPd();
			String pkOrd = blOpCgPubParamVo.getPkOrd();
			PvEncounter pvVo = cgQryMaintainService.qryPvEncounterInfo(mapParam);
			
			
			// 针对不同的方法入口，进行数据加工。门诊医生站调用时传pkord,门诊收费传过来是pkItem
			List<Map<String, Object>> bdItems = new ArrayList<Map<String, Object>>();
			if (pkOrd != null) {
				mapParam.put("pkOrd", pkOrd);
				if (BlcgUtil.converToTrueOrFalse(flagPd)) {// 如果是药品,pkord就是pkpd
					bdItems = cgQryMaintainService.qryPdInfoByPkPd(mapParam);
					if (bdItems == null || bdItems.size() == 0) {
						throw new BusException(pkOrd + " 未维护对应的药品信息");
					}
				} else {
					Map<String,Object> name=DataBaseHelper.queryForMap(
							"select name from  BD_ORD where pk_ord=? and del_flag='0'", 
							pkOrd);
					bdItems = cgQryMaintainService.qryPkItemsByPkOrd(mapParam);
					if (bdItems == null || bdItems.size() == 0) {
						throw new BusException("【" + name.get("name")==null?"":name.get("name").toString()+ "】" + " 未维护对应的收费项目");
					}
				}
			} else {
				mapParam.put("pkItem", blOpCgPubParamVo.getPkItem());
				Map<String, Object> bdItem = cgQryMaintainService.qryBdItemByPk(mapParam);
				bdItem.put("quan", 1);
				bdItems.add(bdItem);
			}
			BlOpDt bod = null;
			
			// 组键记费VO
			for (Map<String, Object> bdItem : bdItems) {
				// 根据收费项目主键查询对应的价格以及组套信息
				List<BlPubPriceVo> blPubPriceVos = priceStrategyService.getItemPriceAllInfo(bdItem.get("pkItem").toString(), flagPd,
						blOpCgPubParamVo.getPrice()==null?0.0:blOpCgPubParamVo.getPrice(), pvVo, blOpCgPubParamVo.getDateHap());
				for (BlPubPriceVo blPubPriceVo : blPubPriceVos) {
					mapParam.clear();
					mapParam.put("pkItem", blPubPriceVo.getPkItem());
					mapParam.put("pkOrg", pkOrg);
					bod = new BlOpDt();
					bod.setPkOrg(pkOrg);
					bod.setPkPi(blOpCgPubParamVo.getPkPi());
					bod.setPkPv(blOpCgPubParamVo.getPkPv());
					bod.setFlagPd(flagPd);
					bod.setPkCnord(blOpCgPubParamVo.getPkCnord());
					bod.setPkPres(blOpCgPubParamVo.getPkPres());

					Map<String, Object> bdItemUnit = null;
					if (BlcgUtil.converToTrueOrFalse(flagPd)) {
						bdItemUnit = cgQryMaintainService.qryBdPdyPk(mapParam);
						// 药品信息供应链取
						bod.setPkPd(pkOrd);
						bod.setDateExpire(blOpCgPubParamVo.getDateExpire());
						bod.setPkUnitPd(blOpCgPubParamVo.getPkUnitPd());
						bod.setPkUnit(blOpCgPubParamVo.getPkUnitPd());
						bod.setPackSize(blOpCgPubParamVo.getPackSize());
						bod.setPriceCost(blOpCgPubParamVo.getPriceCost());
					} else {
						mapParam.put("flagActive", "1");
						mapParam.put("delFlag", "0");
						bdItemUnit = cgQryMaintainService.qryBdItemByPk(mapParam);
						bod.setPkUnit(CommonUtils.getString(bdItemUnit.get("pkUnit")));
					}
					bod.setSpec(bdItemUnit.get("spec") == null ? "" : bdItemUnit.get("spec").toString());
					// 查询收费项目对应的账单码
					mapParam.clear();
					mapParam.put("pkItem", blPubPriceVo.getPkItem());
					mapParam.put("pkOrg", pkOrg);
					mapParam.put("euType", EnumerateParameter.ZERO);// 目前只分门诊发票和住院发票
					mapParam.put("flagPd", flagPd);
					@SuppressWarnings("unused")
					String code = "";
					if (EnumerateParameter.ONE.equals(blOpCgPubParamVo.getFlagPv())) {
						code = ApplicationUtils.getSysparam("BL0002", false);
					} else {
						code = ApplicationUtils.getSysparam("BL0008", false);
					}
					//修改code为1时打印发票
//					if (EnumerateParameter.ONE.equals(code)) {
//						Map<String, Object> mapBillCode = cgQryMaintainService.qryBillCodeByPkItem(mapParam);
//						if (mapBillCode == null)
//							throw new BusException("【" + bdItemUnit.get("name") + "】" + "  未维护费用分类对应发票分类的账单码");
//						bod.setCodeBill(mapBillCode.get("code").toString());
//					}
					
					Map<String, Object> mapBillCode = cgQryMaintainService.qryBillCodeByPkItem(mapParam);
					if (mapBillCode == null)
						throw new BusException("【" + bdItemUnit.get("name") + "】" + "  未维护费用分类对应发票分类的账单码");
					bod.setCodeBill(mapBillCode.get("code").toString());
					
					Map<String, Object> mapAccountCode = cgQryMaintainService.qryAccountCodeByPkItem(mapParam);
					// if (mapAccountCode == null)
					// throw new BusException("【" + bdItemUnit.get("name") + "】"
					// + "  未维护对应的核算码");
					bod.setCodeAudit(mapAccountCode == null ? null : mapAccountCode.get("code").toString());
					bod.setPkItemcate(bdItemUnit.get("pkItemcate").toString());
					bod.setPkItem(blPubPriceVo.getPkItem());
					bod.setNameCg(bdItemUnit.get("name").toString());
					bod.setSortno(cgSort);
					cgSort++;
					bod.setPriceOrg(blPubPriceVo.getPriceOrg().doubleValue());// 原始单价
					bod.setPrice(blPubPriceVo.getPrice().doubleValue());// 当前单价
					bod.setQuan(MathUtils.mul(MathUtils.mul(blOpCgPubParamVo.getQuanCg(),CommonUtils.getDouble(bdItem.get("quan"))),blPubPriceVo.getQuanChild().doubleValue()));// 数量要乘以子项目地数量和收费项目对应的数量
					bod.setPkDisc(blPubPriceVo.getPkDisc());// 优惠类型
					bod.setRatioDisc(blPubPriceVo.getRatioDisc().doubleValue());// 优惠比例
					bod.setRatioSelf(blPubPriceVo.getRatioSelf().doubleValue());
					// && BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())
					bod.setRatioAdd(0D);	//特诊加收比例
					//计算项目原始单价
					Double priceOld = MathUtils.div(bod.getPriceOrg(), MathUtils.add(1D, bod.getRatioAdd()), 6);
					//特诊加收金额 price_org*quan*ratio_add
					bod.setAmountAdd(MathUtils.mul(MathUtils.mul(priceOld,bod.getQuan()), bod.getRatioAdd()));
					//amount，金额，price_org*quan+amount_add
					bod.setAmount(MathUtils.mul(bod.getQuan(), bod.getPriceOrg()));
					//amount_hppi，患者支付的医保金额，price*quan*ratio_self；
					if("1".equals(blPubPriceVo.getFlagHppi())){
						bod.setAmountHppi(blPubPriceVo.getAmtHppi().doubleValue());
					}else{
						bod.setAmountHppi(MathUtils.mul(MathUtils.mul(bod.getPrice(), bod.getQuan()),bod.getRatioSelf()));
					}
					//amount_pi，amount_hppi-[price_org*(1-ratio_disc)*quan]+amount_add，计算结果小于0时为0；
		            //Double amt = MathUtils.add(MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(MathUtils.mul(vo.getPriceOrg(), MathUtils.sub(1D, vo.getRatioDisc())), vo.getQuan())), vo.getAmountAdd());
					Double amt = MathUtils.sub(bod.getAmountHppi(), MathUtils.mul(MathUtils.mul(bod.getPriceOrg(), MathUtils.sub(1D, bod.getRatioDisc())), bod.getQuan()));
					//因静配自动记费可能会传数量<0的计费项目，所以判断amt_pi字段赋值时增加校验条件。
					if(MathUtils.compareTo(amt, 0D)<0 && bod.getQuan()>0){
						bod.setAmountPi(0D);
					}else{
						bod.setAmountPi(amt);
					}
					
//					double amountMed = ((bod.getPriceOrg() - bod.getPrice()) + (bod.getPriceOrg() * (1 - bod.getRatioSelf()))) * bod.getQuan();
//					double amountDis = bod.getAmount() * (1 - bod.getRatioDisc());// 金额（原始单价*数量）*折扣比率
//					double tempAmount = amountMed + amountDis;
//					double amountPi = (tempAmount >= bod.getAmount()) ? 0.0 : (bod.getAmount() - tempAmount);
//					bod.setAmountPi(amountPi);
					bod.setPkOrgApp(blOpCgPubParamVo.getPkOrgApp());
					bod.setPkDeptApp(blOpCgPubParamVo.getPkDeptApp());
					bod.setPkEmpApp(blOpCgPubParamVo.getPkEmpApp());
					bod.setNameEmpApp(blOpCgPubParamVo.getNameEmpApp());
					bod.setPkOrgEx(blOpCgPubParamVo.getPkOrgEx());
					bod.setPkDeptEx(blOpCgPubParamVo.getPkDeptEx());
					bod.setDateHap(blOpCgPubParamVo.getDateHap());// 费用发生日期
					bod.setFlagSettle(EnumerateParameter.ZERO);
					bod.setFlagAcc(EnumerateParameter.ZERO);
					bod.setFlagInsu(EnumerateParameter.ZERO);
					bod.setFlagPv(blOpCgPubParamVo.getFlagPv());// 挂号费用标志
					bod.setPkPres(blOpCgPubParamVo.getPkPres() == null ? null : blOpCgPubParamVo.getPkPres());// 处方主键
					bod.setCodeCg(codeCg);// 记费编码
					bod.setDateCg(new Date());// 记费日期
					bod.setPkDeptCg(blOpCgPubParamVo.getPkDeptCg());// 计费部门
					bod.setPkEmpCg(blOpCgPubParamVo.getPkEmpCg());// 记费人员
					bod.setNameEmpCg(blOpCgPubParamVo.getNameEmpCg());// 记费人员名称
					bod.setEuAdditem(blOpCgPubParamVo.getEuAdditem());
					bod.setPkDeptJob(blOpCgPubParamVo.getPkDeptJob());//开立医生考勤科室
					ApplicationUtils.setDefaultValue(bod, true);// 设置默认字段
					bods.add(bod);
				}
			}

		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), bods);
		blCgPubReturnVo.setBods(bods);
		return blCgPubReturnVo;
	}

	/**
	 * 门诊退费统一接口 
	 * 方法思路： zhangmenghao
	 * 		  1 --根据前台传递的pk_settle查询当前结算下的所有记录，根据前台传递的pk_cgop结算主键查询要退费的记录，将二者比对。
	 * 		  2 --如果前台传递的结算记录中某条结算明细是部分退（数量比总数少），加入blOpDtNew。
	 * 		  3 --将所有结算记录全部冲负，批量插入；将blOpDtNew中未退费的部分重新插入结算表中。
	 * 		  4 --遍历当前结算下的所有记录，和前台传递的结算记录对比，找出剩余不退费的部分，和blOpDtNew一起返回。
   	 * 示例：总收费明细中有A(10)B(8)C(7),前台传递部分退A(6)B(8),此方法返回（old:[负的总明细全部]，new:[A(4)C(7)]）
	 * @return BlOpPubRebackFeesVo
	 * @throws BusException
	 */
	public BlOpPubRebackFeesVo blOpRefound(List<BlOpDt> blOpDts) throws BusException {

		BlOpPubRebackFeesVo returnObj = new BlOpPubRebackFeesVo();
		String pkSettle = blOpDts.get(0).getPkSettle();
		// 1 查询出本次结算的所有明细
		List<BlOpDt> blOpdtAlls = DataBaseHelper.queryForList("select * from bl_op_dt where pk_settle=?", 
											BlOpDt.class, pkSettle);
		// 2 所有记录冲负
		List<BlOpDt> blOpDtNots = createBlopDtNots(blOpdtAlls);
		returnObj.setBlOpDtOlds(blOpDtNots);
		
		// 3 是否是部分退费的情况(抽取成公共方法，供预退费接口调用)
		List<BlOpDt> blOpDtNews = blOpDtPart(blOpDts, blOpdtAlls);

		if (blOpDtNews.size() > 0) {
			// 重新给剩余部分生成明细主键  全退后再次结算
			String codeCg = ApplicationUtils.getCode("0602");
			int sortNo = 0;
			for (BlOpDt blOpDtNew : blOpDtNews) {
				sortNo++;
				blOpDtNew.setCodeCg(codeCg);
				blOpDtNew.setSortno(sortNo);
				blOpDtNew.setFlagRecharge("1");//退费重收标志
				ApplicationUtils.setDefaultValue(blOpDtNew, true);
			}
			returnObj.setBlOpDtNews(blOpDtNews);
		}
		
		// 4 批量插入冲负记录
		String sqlInsertBlOpDt = DataBaseHelper.getInsertSql(BlOpDt.class);
		DataBaseHelper.batchUpdate(sqlInsertBlOpDt, blOpDtNots);
		
		// 5 重新插入未退费的记录
		if (blOpDtNews.size() > 0) {
			DataBaseHelper.batchUpdate(sqlInsertBlOpDt, blOpDtNews);
		}
		return returnObj;
	}

	/**
	 * 判断传入的bl_op_dt是否是部分退费
	 * @param blOpDts
	 * @param blOpdtAlls
	 * @return 
	 */
	public List<BlOpDt> blOpDtPart(List<BlOpDt> blOpDts, List<BlOpDt> blOpdtAlls) {
		List<BlOpDt> blOpDtNews = new ArrayList<BlOpDt>();
		for (BlOpDt blOpDt : blOpDts) {
			double quanDiff = MathUtils.sub(blOpDt.getCanBack(), blOpDt.getQuanBack());
			if(EnumerateParameter.ZERO.equals(blOpDt.getFlagPd())) {
				quanDiff = MathUtils.sub(blOpDt.getQuanCg(), blOpDt.getQuanBack());
			}
			//被部分退药标志
			boolean flagPartReturnDurg = false;
			if(MathUtils.equ(quanDiff,0.0)&&MathUtils.compareTo(blOpDt.getQuanCg(),blOpDt.getCanBack())>0&&"1".equals(blOpDt.getFlagPd())){
				flagPartReturnDurg = true;
			}
			if (MathUtils.compareTo(quanDiff, 0.0) > 0) {// 部分退,生成新的计费明细
				// 处方药品的只能全退
				if (BlcgUtil.converToTrueOrFalse(blOpDt.getFlagPd())) {
					throw new BusException("处方不能部分退");
				}
				BlOpDt blOpDtNew = new BlOpDt();
				//根据原始记费记录，计算患者自付金额
				for(BlOpDt oriOpDt : blOpdtAlls){
					if(oriOpDt.getPkCgop().equals(blOpDt.getPkCgop())){
						ApplicationUtils.copyProperties(blOpDtNew, oriOpDt);
						blOpDtNew.setQuan(quanDiff);
						//自付金额=单个数量的自付金额*差异数量
						blOpDtNew.setAmountPi(MathUtils.mul(MathUtils.div(oriOpDt.getAmountPi(), oriOpDt.getQuan()), quanDiff));
						blOpDtNew.setAmount(MathUtils.mul(quanDiff, blOpDtNew.getPrice()));
						blOpDtNew.setAmountAdd(MathUtils.mul(quanDiff,MathUtils.sub(oriOpDt.getPrice(),oriOpDt.getPriceOrg())));
						blOpDtNew.setFlagSettle(EnumerateParameter.ZERO);
						blOpDtNew.setPkSettle(null);
						blOpDtNew.setPkCgopOld(blOpDtNew.getPkCgop());
						blOpDtNews.add(blOpDtNew);
						break;
					}
				}
			}else if(flagPartReturnDurg){
				//差异数量
				quanDiff = MathUtils.sub(blOpDt.getQuanCg(), blOpDt.getQuanBack());
				BlOpDt blOpDtNew = new BlOpDt();
				//根据原始记费记录，计算患者自付金额
				for(BlOpDt oriOpDt : blOpdtAlls){
					if(oriOpDt.getPkCgop().equals(blOpDt.getPkCgop())){
						ApplicationUtils.copyProperties(blOpDtNew, oriOpDt);
						blOpDtNew.setQuan(quanDiff);
						//自付金额=单个数量的自付金额*差异数量
						blOpDtNew.setAmountPi(MathUtils.mul(MathUtils.div(oriOpDt.getAmountPi(), oriOpDt.getQuan()), quanDiff));
						blOpDtNew.setAmount(MathUtils.mul(quanDiff, blOpDtNew.getPrice()));
						blOpDtNew.setAmountAdd(MathUtils.mul(quanDiff,MathUtils.sub(oriOpDt.getPrice(),oriOpDt.getPriceOrg())));
						blOpDtNew.setFlagSettle(EnumerateParameter.ZERO);
						blOpDtNew.setPkSettle(null);
						blOpDtNew.setPkCgopOld(blOpDtNew.getPkCgop());
						blOpDtNews.add(blOpDtNew);
						break;
					}
				}
				
			}
		}
		
		// 如果本次结算还存在其他执行单的记费明细，在下次结算时重新结算
		Iterator<BlOpDt> it = blOpdtAlls.iterator();
		while (it.hasNext()) {
			BlOpDt blOpDtTemp = it.next();
			for (BlOpDt blOpDt : blOpDts) {
				if (blOpDt.getPkCgop().equals(blOpDtTemp.getPkCgop())) {
					it.remove();
					break;
				}
			}
		}
		
		//处理明细的flag_settle和pk_settle
		if(blOpdtAlls!=null && blOpdtAlls.size()>0){
			for(BlOpDt opdt : blOpdtAlls){
				opdt.setPkSettle(null);
				opdt.setFlagSettle(EnumerateParameter.ZERO);
				opdt.setPkCgopOld(opdt.getPkCgop());
			}
		}
		
		blOpDtNews.addAll(blOpdtAlls);// 取并集
		return blOpDtNews;
	}
	
	/**
	 * 所有收费明细产生负的记录
	 * @param blOpdtAlls
	 * @return blOpDtNots
	 */
	public List<BlOpDt> createBlopDtNots(List<BlOpDt> blOpdtAlls) {
		List<BlOpDt> blOpDtNots = new ArrayList<BlOpDt>();
		// 所有的记录都产生负的记费明细
		String codeCg = ApplicationUtils.getCode("0602");
		int sortNo = 0;
		for (BlOpDt blOpDt : blOpdtAlls) {
			sortNo++;
			BlOpDt blOpDtNot = new BlOpDt();
			ApplicationUtils.copyProperties(blOpDtNot, blOpDt);
			blOpDtNot.setQuan(-1 * blOpDt.getQuan());
			blOpDtNot.setAmount(-1 * blOpDt.getAmount());
			blOpDtNot.setAmountPi(-1 * blOpDt.getAmountPi());
			blOpDtNot.setPkCgopBack(blOpDt.getPkCgop());
			blOpDtNot.setCodeCg(codeCg);
			blOpDtNot.setSortno(sortNo);
			ApplicationUtils.setDefaultValue(blOpDtNot, true);
			blOpDtNots.add(blOpDtNot);
		}
		return blOpDtNots;
	}

	/**
	 * 门诊挂号预结算后台计算接口
	 * @param param
	 *            入口参数为单个挂号的全部信息，单病人
	 * @return aggregateAmount-合计金额，patientsPay-患者自付，medicarePayments-医保支付
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, BigDecimal> registerPreParedSettlement(Map<String, Object> param) throws BusException {

		Map<String, Object> paramTemp = new HashMap<String, Object>();
		String pkOrg = param.get("pkOrg").toString();// 机构主键
		String pkSchsrv = param.get("pkSchsrv").toString();// 排班服务主键
		String pkPicate = param.get("pkPicate").toString();// 患者分类主键
		BigDecimal amtInsuThird = param.get("amtInsuThird")==null?BigDecimal.ZERO:new BigDecimal(param.get("amtInsuThird").toString());//外部第三方医保支付金额

		List<Map<String, Object>> insuLists = (List<Map<String, Object>>) param.get("insuList");// 医保计划主键(包括辅医保主键)
		List<Map<String, Object>> opDtLists = (List<Map<String, Object>>) param.get("opDtList");// 附加费用主键(包括对应的数量)
		paramTemp.put("pkOrg", pkOrg);
		paramTemp.put("pkSchsrv", pkSchsrv);
		Map<String, Object> mapSchsrv = cgQryMaintainService.qrySchSrvByPkSchsrv(paramTemp);
		// 根据排班服务主键查询对应的收费项目主键
		List<String> pkItems = cgQryMaintainService.qrySchSrvOrdsByPkSchsrv(paramTemp);
		// 将前台传过来的（勾选的附加费的pkitem加入进去），将list转换成map
		Map<String, BigDecimal> mapOpDtLists = new HashMap<String, BigDecimal>();
		for (Map<String, Object> mapTemp : opDtLists) {
			//pkItems.add(mapTemp.get("pkItem").toString());
			mapOpDtLists.put(mapTemp.get("pkItem").toString(), new BigDecimal(mapTemp.get("quan").toString()));
		}
		//将排班服务对应的收费项目添加到记费明细表,并将重复的收费项目进行合并，数量累加
		for(String pkItem:pkItems){
			if(mapOpDtLists.get(pkItem)!=null){
				mapOpDtLists.put(pkItem, BigDecimal.ONE.add(mapOpDtLists.get(pkItem)));
			}else{
				mapOpDtLists.put(pkItem, BigDecimal.ONE);
			}
		}
		// 9代表急诊 1代表门诊
		String euPvType = EnumerateParameter.NINE.equals(mapSchsrv.get("euSrvtype").toString()) ? EnumerateParameter.TWO : EnumerateParameter.ONE;
       
		
		//List<PvEncounter> pvVos = new ArrayList<PvEncounter>();
		// 杨雪修改为直接使用挂号时选择的患者分类
		//PiMaster piInfo = cgQryMaintainService.qryPiMasterByPk(paramTemp);
		/*
		 * 因为预结算时未生成就诊PV信息所以调用价格策略时需自己构建PvEncounter信息
		 * 实际上是组建多个医保信息
		 */
		String flagMaj = "";
		List<String> hpList = new ArrayList<String>();
		String pkInsuMain = "";
		for (Map<String, Object> mapTemp : insuLists) {
			if (mapTemp.get("pkHp") == null) {
				throw new BusException("医保计划主键为空");
			}
			if (EnumerateParameter.ONE.equals(mapTemp.get("flagMaj").toString())) {
				flagMaj = EnumerateParameter.ONE;
				pkInsuMain = mapTemp.get("pkHp").toString();
			}
			hpList.add(mapTemp.get("pkHp").toString());
		}
		if (!EnumerateParameter.ONE.equals(flagMaj))
			throw new BusException("没有选择主医保，不能进行挂号");
		Set<String> itemSet = new HashSet<String>(pkItems);
		for (Map<String, Object> mapTemp : opDtLists) {
			itemSet.add(mapTemp.get("pkItem").toString());
		}
		pkItems = new ArrayList<String>(itemSet);
		return calPreSettle(pkOrg,pkPicate,pkInsuMain,amtInsuThird,euPvType,hpList, pkItems, mapOpDtLists);
		
//		//添加内部医保集合
//		List<PvInsurance> pvInsurances = new ArrayList<PvInsurance>();
//		for (String pkItem : pkItems) {
//			// 单个项目医保优惠金额
//			BigDecimal amountMed = BigDecimal.ZERO;
//			// 单个项目优惠自付比率
//			BigDecimal piDisc = BigDecimal.ONE;
//
//			BigDecimal priceOrg = BigDecimal.ZERO;
//
//			// 对于每一个收费项目，都要循环所有的医保
//			PvEncounter pvVoMaj = null;
//			for (PvEncounter pvVo : pvVos) {
//				if (EnumerateParameter.ONE.equals(pvVo.getFlagCancel())) {
//					pvVoMaj = pvVo;
//				}
//			}
//			
//			/**
//			 * 判断是否是外部医保
//			 */
//			BdHp bdHp = cgQryMaintainService.qryBdHpInfo(insuLists.get(0));
//			if (bdHp.getDtExthp() == null || bdHp.getDtExthp().equals("0")){//没有第三方医保，走主医保
////				PvInsurance pvins = new PvInsurance();
////				pvins.setPkHp(bdHp.getPkHp());
////				pvInsurances.add(pvins);
//				// 获取其主医保下的价格
//				Map<String, String> mapChargePriceModel = priceStrategyService.chargePriceModel(pkItem, pvVoMaj.getPkInsu(), pkOrg, new Date());
//				for (String key : mapChargePriceModel.keySet()) {
//					priceOrg = new BigDecimal(mapChargePriceModel.get(key).split(",")[0]);
//				}
//				
//				// 查询主医保支付金额
//				amountMed = priceStrategyService.chargeAmountWithinHealthCare(pkItem, priceOrg.toString(), EnumerateParameter.ZERO, pvVoMaj.getPkInsu(), pkOrg,
//						pvVoMaj, new Date());
//				
//				if (pkPicate != null) {
//					paramTemp.put("pkPicate", pkPicate);// 患者类别
//					paramTemp.put("pkOrg", pkOrg);
//					PiCate piCate = cgQryMaintainService.qryPiCateInfo(paramTemp);
//					if (piCate == null)
//						throw new BusException("找不到患者类别");
//					// 查询患者的优惠比率
//					piDisc = priceStrategyService.chargePatientFavorable(pkItem, EnumerateParameter.ZERO, piCate.getPkHp(), pvVoMaj, new Date());
//					
//					// 取前台传过来的附加费的数量
//					BigDecimal quanTemp = BigDecimal.ONE;
//					for (Map.Entry<String, BigDecimal> mapEntery : mapOpDtLists.entrySet()) {
//						if (mapEntery.getKey().equals(pkItem)) {
//							quanTemp = mapEntery.getValue();
//							break;
//						}
//					}
//					BigDecimal medSingleTemp = amountMed.multiply(quanTemp);
//					BigDecimal disSingelTemp = priceOrg.multiply(BigDecimal.ONE.subtract(piDisc)).multiply(quanTemp);
//					BigDecimal tempSum = medSingleTemp.add(disSingelTemp);
//					BigDecimal totalTemp = priceOrg.multiply(quanTemp);
//					//BigDecimal piPaySingleTemp = (tempSum >= totalTemp ? 0D : (totalTemp - tempSum));
//					BigDecimal piPaySingleTemp = (tempSum.compareTo(totalTemp)>=0 ? BigDecimal.ZERO : (totalTemp.subtract(tempSum)));
//					// 医保支付包括患者优惠的金额
//					//medicarePayments += medSingleTemp + disSingelTemp;
//					medicarePayments = medicarePayments.add(medSingleTemp.add(disSingelTemp));
//					//patientsPay += piPaySingleTemp;
//					patientsPay = patientsPay.add(piPaySingleTemp);
//					//aggregateAmount += totalTemp;
//					aggregateAmount = aggregateAmount.add(totalTemp);
//				}
//				
//			}else{// 医保为对应的第三方医保
//				BigDecimal ybzje = new BigDecimal(param.get("ybzje").toString()); //医保总金额
//				BigDecimal ybzf = new BigDecimal(param.get("ybzf").toString()); //医保支付
//				BigDecimal xjzf = new BigDecimal(param.get("xjzf").toString()); //现金支付
//				
//				aggregateAmount = aggregateAmount.add(ybzje); //金额合计
//				medicarePayments = medicarePayments.add(ybzf); //医保支付
//				patientsPay = patientsPay.add(xjzf); //现金支付
//			}
//		}
//		//内部医保此处添加按总额及支付段分摊计算
//		// 计算内部医保分段分摊和总额分摊情况,拷贝门诊收费结算内容，是否正确需要根据需求确认
//		List<BlSettleDetail> blSettleDetails = countMedicare(pvInsurances, patientsPay, euPvType, null);
//		for (BlSettleDetail blSettleDetail : blSettleDetails) {
//			medicarePayments = medicarePayments.add(new BigDecimal(blSettleDetail.getAmount()));
//		}
//		result.put("aggregateAmount", aggregateAmount);// 合计金额
//		
//		//如果医保支付金额大于合计金额，则医保支付金额按合计金额计算
//		if(medicarePayments.compareTo(aggregateAmount)>0){
//			medicarePayments = aggregateAmount;
//		}
//		result.put("patientsPay", aggregateAmount.subtract(medicarePayments));// 患者自付 = 总金额 - 医保支付
//		result.put("medicarePayments", medicarePayments);// 医保支付
//
//		return result;
	}
   
	/**
	 * 结算门诊挂号预结算金额(杨雪添加)
	 * @param pkPicate
	 * @param pkInsuMain 主医保
	 * @param amtInsuThird 第三方医保支付金额
	 * @param insuLists
	 * @param mapOpDtLists
	 */
	public Map<String, BigDecimal> calPreSettle(String pkOrg,String pkPicate,String pkInsuMain,BigDecimal amtInsuThird,String euPvType,List<String> insuLists,List<String> pkItems,Map<String, BigDecimal> mapOpDtLists){
		BigDecimal aggregateAmount = BigDecimal.ZERO;// 金额合计
		BigDecimal patientsPay = BigDecimal.ZERO;// 患者支付
		BigDecimal medicarePayments = BigDecimal.ZERO;// 医保支付
		Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
		String dateHap = DateUtils.getDefaultDateFormat().format(new Date());
		List<BdHp> hplist = cgQryMaintainService.qryBdHpInfoList(insuLists,euPvType);
		BdHp cateHp = null;
		//若存在患者分类，查询患者优惠信息
		if(pkPicate!=null&&!pkPicate.equals("")){
			Map<String,Object> paramTemp = new HashMap<String,Object>();
			paramTemp.put("pkPicate", pkPicate);// 患者类别
			paramTemp.put("pkOrg", pkOrg);
			paramTemp.put("euPvType", euPvType);
			cateHp = cgQryMaintainService.qryBdHpByPiCate(paramTemp);
			if (cateHp == null)
				throw new BusException("找不到患者类别对应的优惠内容");
		}
		if(mapOpDtLists!=null&&!mapOpDtLists.isEmpty()){
			for(BdHp hp:hplist){
				//获取医保下收费项目对应的价格
			 List<ItemPriceVo> itemPriceList = priceStrategyService.batchChargePriceModel(hp,pkOrg,dateHap,pkItems,mapOpDtLists);
			 //获取患者优惠比例
			 if(cateHp!=null){
				itemPriceList = priceStrategyService.batchChargePatientFavorable(itemPriceList,cateHp, pkOrg,dateHap);
			 }
			 if(pkInsuMain.equals(hp.getPkHp())){//主医保
				 if(hp.getDtExthp() == null || hp.getDtExthp().equals("0")){//内部医保
						//获取内部医保设置的分摊政策
						itemPriceList = priceStrategyService.batchChargeAmountWithinHealthCare(itemPriceList,hp,pkOrg,dateHap);
						// 该医保下所有项目的内部医保支付金额
						BigDecimal totalAmountMed = BigDecimal.ZERO;
						// 该医保下所有项目的优惠金额
						BigDecimal totalAmountDisc = BigDecimal.ZERO;
						// 该医保下所有项目的总金额
						BigDecimal totalAmount = BigDecimal.ZERO;
						
						for(ItemPriceVo pricevo:itemPriceList){
							totalAmountMed  = totalAmountMed.add(new BigDecimal(pricevo.getQuan()).multiply(pricevo.getAmountMed()));
							totalAmountDisc = totalAmountDisc.add(new BigDecimal(pricevo.getPriceOrg()).multiply(BigDecimal.ONE.subtract(new BigDecimal(pricevo.getRatioDisc()))).multiply(new BigDecimal(pricevo.getQuan())));
							totalAmount = totalAmount.add(new BigDecimal(pricevo.getPriceOrg()).multiply(new BigDecimal(pricevo.getQuan())));
						}
						BigDecimal totalAmountMedTemp = BigDecimal.ZERO;
						//如果医保及优惠总金额大于项目总额，则医保+优惠支付金额 = 项目总额
						if(totalAmountMed.add(totalAmountDisc).compareTo(totalAmount)>0){
							totalAmountMedTemp = totalAmount;
						}else{
							totalAmountMedTemp = totalAmountMed.add(totalAmountDisc);
						}
						medicarePayments = medicarePayments.add(totalAmountMedTemp);
						patientsPay = patientsPay.add(totalAmount.subtract(totalAmountMedTemp));
						aggregateAmount = aggregateAmount.add(totalAmount);
				}else{// 医保为对应的第三方医保
					BigDecimal ybzf = amtInsuThird; //第三方医保支付
					// 该医保下所有项目的总金额
					BigDecimal totalAmount = BigDecimal.ZERO;
					// 该医保下所有项目的优惠金额
					BigDecimal totalAmountDisc = BigDecimal.ZERO;
					for(ItemPriceVo pricevo:itemPriceList){
						totalAmount = totalAmount.add(new BigDecimal(pricevo.getPriceOrg()).multiply(new BigDecimal(pricevo.getQuan())));
						totalAmountDisc = totalAmountDisc.add(new BigDecimal(pricevo.getPriceOrg()).multiply(BigDecimal.ONE.subtract(new BigDecimal(pricevo.getRatioDisc()))).multiply(new BigDecimal(pricevo.getQuan())));
					}
					ybzf = ybzf.add(totalAmountDisc);
					//优惠金额+医保支付金额大于总金额,则认为医保全部支付
					if(ybzf.compareTo(totalAmount)>0){
						ybzf = totalAmount;
					}
					aggregateAmount = aggregateAmount.add(totalAmount); //金额合计
					medicarePayments = medicarePayments.add(ybzf); //医保支付，由医保计算后返回
					patientsPay = patientsPay.add(totalAmount.subtract(ybzf)); //患者自付，医保部分由医保计算后扣除
				}
			  }
			}
			//计算辅医保及按总额或支付段分摊的医保（优先级为先总额，后支付段）
			List<BlSettleDetail> blSettleDetails = countTotalMedicare(hplist, patientsPay, euPvType, null);
			for (BlSettleDetail blSettleDetail : blSettleDetails) {
				medicarePayments = medicarePayments.add(new BigDecimal(blSettleDetail.getAmount()));
			}
			result.put("aggregateAmount", aggregateAmount);// 合计金额
			//如果医保支付金额大于合计金额，则医保支付金额按合计金额计算
			if(medicarePayments.compareTo(aggregateAmount)>0){
				medicarePayments = aggregateAmount;
			}
		}
		result.put("patientsPay", aggregateAmount.subtract(medicarePayments));// 患者自付 = 总金额 - 医保支付
		result.put("medicarePayments", medicarePayments);// 医保支付
		return result;
	}
	
	/**
	 * 计算医保分段和总额分摊情况(杨雪添加)
	 * @param totalAmount
	 * @return
	 */
	public List<BlSettleDetail> countTotalMedicare(List<BdHp> hplist, BigDecimal totalAmount, String euPvtype, String pkSettle) {

		List<BlSettleDetail> blSettleDetails = new ArrayList<BlSettleDetail>();
		// 循环所有的医保计划
		BigDecimal totalAmountTemp = BigDecimal.ZERO;
		User user = UserContext.getUser();
		String pkOrg = user.getPkOrg();
		for (BdHp hp: hplist){
			if(hp.getDtExthp() == null || hp.getDtExthp().equals("0")){
				totalAmountTemp = priceStrategyService.qryPatiSettlementAmountAllocationInfo(totalAmount, pkOrg, euPvtype, hp.getPkHp());
				BigDecimal tempDouble = totalAmount.subtract(totalAmountTemp);
				totalAmount = totalAmountTemp;
				if (tempDouble.compareTo(BigDecimal.ZERO) != 0) {
					BlSettleDetail blSettleDetail = new BlSettleDetail();
					blSettleDetail.setPkSettle(pkSettle);
					blSettleDetail.setPkPayer(hp.getPkPayer());// 支付方
					blSettleDetail.setPkInsurance(hp.getPkHp());// 医保主键
					blSettleDetail.setAmount(tempDouble.doubleValue());
					ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
					blSettleDetails.add(blSettleDetail);
				}
		    }
		}
		return blSettleDetails;
	}
	
	/**
	 * 门诊挂号结算
	 * @return
	 * @throws BusException
	 */
	public List<BlInvoiceDt> registerSettlement(Map<String, Object> mapParam, Map<String, Double> mapItemAndQuan) throws BusException {

		Map<String, Object> mapParamTemp = new HashMap<String, Object>();
		String pkOrg = mapParam.get("pkOrg").toString();// 当前机构主键
		String pkPi = mapParam.get("pkPi").toString();
		String pkPv = mapParam.get("pkPv").toString();
		//第三方医保支付金额                                          
		BigDecimal amtInsuThird = mapParam.get("amtInsuThird")==null?BigDecimal.ZERO:new BigDecimal(mapParam.get("amtInsuThird").toString());
		mapParamTemp.clear();
		mapParamTemp.put("pkPv", pkPv);
		mapParamTemp.put("pkOrg", pkOrg);
		PvEncounter pvVo = cgQryMaintainService.qryPvEncounterInfo(mapParamTemp);
		String pkSchsrv = mapParam.get("pkSchsrv").toString();// 排班服务主键
		@SuppressWarnings("unchecked")
		List<BlDeposit> depositList = (List<BlDeposit>) mapParam.get("depositList");// 支付方式
		String pkEmpinvoice = mapParam.get("pkEmpinv") == null ? null : mapParam.get("pkEmpinv").toString();// 票据领用主键
		String pkInvcate = mapParam.get("pkInvcate") == null ? null : mapParam.get("pkInvcate").toString();// 票据分类主键
		String codeInv = mapParam.get("codeInv") == null ? null : mapParam.get("codeInv").toString();// 发票号码
		List<BdOrdItem> bdOrdItems = new ArrayList<BdOrdItem>();
		for (Map.Entry<String, Double> entryTemp : mapItemAndQuan.entrySet()) {// 循环收费项目MAP
			BdOrdItem bdOrdItem = new BdOrdItem();
			String pkItem = entryTemp.getKey();// 收费项目主键
			double quan = entryTemp.getValue();// 收费项目数量
			bdOrdItem.setPkItem(pkItem);
			bdOrdItem.setQuan(quan);
			bdOrdItems.add(bdOrdItem);
		}
		User user = UserContext.getUser();
		String pkCurDept = user.getPkDept();// 当前科室
		String pkOpDoctor = user.getPkEmp();// 当前用户主键
		String nameUser = user.getNameEmp();// 当前用户名
		List<BlPubParamVo> blOpCgPubParamVos = new ArrayList<BlPubParamVo>();
		for (Map.Entry<String, Double> entryTemp : mapItemAndQuan.entrySet()) {// 循环收费项目MAP
			String pkItem = entryTemp.getKey();// 收费项目主键
			double quan = entryTemp.getValue();// 收费项目数量
			BlPubParamVo bppv = new BlPubParamVo();
			bppv.setPkOrg(pkOrg);
			bppv.setPkPv(pkPv);
			bppv.setPkPi(pkPi);
			bppv.setPkItem(pkItem);
			bppv.setQuanCg(quan);// 收费项目数量
			bppv.setPkOrgEx(pkOrg);// 执行机构
			bppv.setPkOrgApp(pkOrg);// 开立机构
			bppv.setPkDeptEx(pkCurDept);// 执行科室
			bppv.setPkDeptApp(pkCurDept);// 开立科室
			bppv.setPkEmpApp(pkOpDoctor);// 开立医生
			bppv.setNameEmpApp(nameUser);// 开立医生姓名
			bppv.setFlagPd(EnumerateParameter.ZERO);// 物品标志
			bppv.setFlagPv(EnumerateParameter.ONE);// 挂号费用标志
			bppv.setDateHap(new Date());// 费用发生日期
			bppv.setPkDeptCg(pkCurDept);// 记费科室
			bppv.setPkEmpCg(pkOpDoctor);// 记费人员
			bppv.setNameEmpCg(nameUser);// 记费人员名称
			blOpCgPubParamVos.add(bppv);
		}
		// 调用通用的计费方法
		BlPubReturnVo blPubReturnVo = blOpCg(blOpCgPubParamVos);
		mapParamTemp.put("pkSchsrv", pkSchsrv);
		mapParamTemp.put("pkOrg", pkOrg);
		Map<String, Object> mapSchsrv = cgQryMaintainService.qrySchSrvByPkSchsrv(mapParamTemp);
		// 调用结算服务
		BlPubSettleVo blPubSettleVo = new BlPubSettleVo();
		blPubSettleVo.setPkPi(pkPi);
		blPubSettleVo.setPkPv(pkPv);
		blPubSettleVo.setEuPvType(mapSchsrv.get("euSrvtype").toString().equals("9") ? "2" : EnumerateParameter.ONE);
		blPubSettleVo.setPkEmpinvoice(pkEmpinvoice);
		blPubSettleVo.setPkInvcate(pkInvcate);
		blPubSettleVo.setDepositList(depositList);
		blPubSettleVo.setCodeInv(codeInv);
		blPubSettleVo.setBlOpDts(blPubReturnVo.getBods());
		blPubSettleVo.setPvVo(pvVo);
		//blPubSettleVo.setPkSettle(NHISUUID.getKeyId());//预生成结算主键
		Map<String,Object> result =  accountedSettlement(blPubSettleVo, false,amtInsuThird, "", BigDecimal.ZERO, null, null);
		if(result!=null)
			return (List<BlInvoiceDt>)result.get("invoDt");
		return null;
	}

	/**
	 * 门诊预结算
	 * 考虑医保的金额计算
	 * @param user
	 * @param opCgTransforVo
	 * @return
	 */
	public OpCgTransforVo useInsComputSettle(IUser user, OpCgTransforVo opCgTransforVo)
	{
		opCgTransforVo.setCurDate(new Date());
		
		OpCgTransforVo rgv = new OpCgTransforVo();
		BigDecimal aggregateAmount = BigDecimal.ZERO;// 金额合计
		BigDecimal patientsPay = BigDecimal.ZERO;// 患者自付
		BigDecimal discAmount = BigDecimal.ZERO;// 优惠金额(计费时患者优惠)
		BigDecimal medicarePayments = BigDecimal.ZERO;// 医保支付(计费以及结算时内部医保优惠)
		BigDecimal accountPrepaid = BigDecimal.ZERO;// 账户已付
		BigDecimal amountPiTotal = BigDecimal.ZERO;// 收费明细中患者支付
		
		// 1.加工前台传过来的数据
		List<BlOpDt> opdtList = opCgTransforVo.getBlOpDts();
		
		List<BlOpDt> blOpDts = new ArrayList<BlOpDt>();
		if (CollectionUtils.isEmpty(opdtList)){
			if(StringUtils.isEmpty(opCgTransforVo.getPkPv())){
				throw new BusException("未传入需要记费的数据");
			}
			
			/*
			 * 2021-07-22 lipz 
			 * 1.从公共字典中获取已配置的就诊身份
			 * 2.判断当前就诊身份是否在配置中
			 * 3.存在配置中的提示 到人工窗口缴费
			 */
			String xzHpCodeSql = "select CODE from BD_DEFDOC where CODE_DEFDOCLIST='BACKSFHP'"; 
			List<Map<String, Object>> xzHpList = DataBaseHelper.queryForList(xzHpCodeSql);
			if(xzHpList!=null && xzHpList.size()>0) {
				StringJoiner xzHpjoin = new StringJoiner(",");
				for (Map<String, Object> hpMap : xzHpList) {
					xzHpjoin.add(hpMap.get("code").toString());
				}
				String xzHpStr = ","+xzHpjoin.toString()+",";
				
				String hpSql = "select hp.NAME,hp.CODE from PV_ENCOUNTER pv INNER JOIN BD_HP hp on hp.PK_HP=pv.PK_INSU where pv.PK_PV=?";
				Map<String, Object> hpMap = DataBaseHelper.queryForMap(hpSql, opCgTransforVo.getPkPv());
				if(hpMap!=null && hpMap.get("code")!=null) {
					String hpCode = hpMap.get("code").toString();
					if(xzHpStr.contains(","+hpCode+",")) {
						throw new BusException("就诊身份为【"+hpCode+"】【"+hpMap.get("name").toString()+"】，请到人工窗口缴费！");
					}
				}
			}
			
			// 1.1公众号、自助机结算时未传入明细，则根据就诊主键获取未结算的计费明细
			StringBuffer sql = new StringBuffer(" SELECT dt.* FROM BL_OP_DT dt ");
			sql.append(" LEFT OUTER JOIN cn_prescription pres ON dt.pk_pres=pres.pk_pres ");
			sql.append(" LEFT JOIN CN_ORDER ord ON ord.PK_CNORD = dt.PK_CNORD ");
			sql.append(" WHERE ");
			sql.append(" dt.FLAG_SETTLE='0' AND dt.DEL_FLAG='0' AND dt.FLAG_ACC='0' AND dt.PK_CGOP_BACK is null ");
//			sql.append(" AND (dt.PK_CNORD is null or ord.DATE_EFFE>=GETDATE()) ");//医嘱有效时间大于当前时间
			sql.append(" and (pres.DT_PRESTYPE!='07' or pres.DT_PRESTYPE is NULL) ");//过滤外流处方
			sql.append(" AND dt.pk_pv=? ");
			blOpDts = DataBaseHelper.queryForList(sql.toString(), BlOpDt.class, new Object[] {opCgTransforVo.getPkPv()});
		}else{
			Set<String> pkBlOpDt = new HashSet<String>();
			for (BlOpDt blOpDt : opdtList) {
				pkBlOpDt.add(blOpDt.getPkCgop());
			}
			// 1.1加工前台传过来的数据
			String pkBlOpDtInSql = CommonUtils.convertSetToSqlInPart(pkBlOpDt, "pk_cgop");
			String sql = "select * from bl_op_dt where del_flag='0' and pk_cgop in (" + pkBlOpDtInSql + ")";
			blOpDts = DataBaseHelper.queryForList(sql, BlOpDt.class, new Object[] {});
		}
		if(opdtList!=null && opdtList.size()>0) {
			if(opdtList.size() != blOpDts.size()) {
				throw new BusException("记费的数据发生变化，请刷新后重新结算！");
			}
		}
		
		Set<String> pkBlOpDt = new HashSet<String>();
		BlOpDt blOpDtFirst = blOpDts.get(0);
		String pkPv = blOpDtFirst.getPkPv();
		Map<String, Object> pvMap = DataBaseHelper.queryForMap("select pk_insu from PV_ENCOUNTER where pk_pv = ?", new Object[]{pkPv});
		String pkInsu =  CommonUtils.getString(pvMap.get("pkInsu"));
		// 2.判断是否是多次就诊一次结算,校验根据多个pkPv查询的辅医保是否一致
		Set<String> pkPvs = new HashSet<String>();
		pkPvs.add(pkPv);
		for (BlOpDt blOpDt : blOpDts) {
			pkBlOpDt.add(blOpDt.getPkCgop());
			if (!pkPv.equals(blOpDt.getPkPv())) {
				pkPvs.add(blOpDt.getPkPv());// 存在多次就诊
			}
			if (blOpDt.getPkInsu()!=null && !blOpDt.getPkInsu().equals(pkInsu)) {
				throw new BusException("该患者多次就诊使用的主医保不一致，不能一次性收费,请选择相同开立科室的进行结算");
			}
		}
		if (pkPvs.size() > 1) {
			String queryPvInsurance = "select 'pkPvhp' as PK_PVHP,PK_ORG,PK_HP,FLAG_MAJ from pv_insurance where pk_pv=? and pk_org=?";
			List<PvInsurance> pvInsurancesFirst = DataBaseHelper.queryForList(queryPvInsurance, PvInsurance.class, pkPv, ((User) user).getPkOrg());
			for (String pv : pkPvs) {
				List<PvInsurance> pvInsurances = DataBaseHelper.queryForList(queryPvInsurance, PvInsurance.class, pv, ((User) user).getPkOrg());
				if (!BlcgUtil.equalList(pvInsurancesFirst, pvInsurances)) {
					throw new BusException("该患者多次就诊使用的辅医保不一致，不能一次性收费,请选择相同开立科室的进行结算");
				}
			}
		}
		
		// 查为个人的付款方主键
		String paySql = "select PK_PAYER,CODE,NAME from BD_PAYER where CODE=?";
		Map<String, Object> payMap = DataBaseHelper.queryForMap(paySql, "01");
		if (payMap==null || !payMap.containsKey("pkPayer")) {
			throw new BusException("未维护付款方为{'个人':'01'}的数据");
		}
		String grPkPay = payMap.get("pkPayer").toString();
		
		// 3.查询主医保医保信息
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkHp", pkInsu);
		mapParam.put("pkPv", pkPv);
		BdHp bdHp = cgQryMaintainService.qryBdHpInfo(mapParam);
		if(bdHp==null){
			throw new BusException("未获取到患者就诊医保主计划，请维护患者就诊身份再试！");
		}
		if ((StringUtils.isEmpty(bdHp.getDtExthp()) || bdHp.getDtExthp().equals("00")) || 
				(opCgTransforVo.getAggregateAmount()==null || opCgTransforVo.getAggregateAmount().compareTo(new BigDecimal(0D))==0)) {// 没有第三方医保
			
			// 3.1查询是否按优惠总额减免
			BigDecimal amt_disc = BigDecimal.ZERO;
			Map<String, Object> pvDiscMap = getHpPayerDiscByPkPv(pkPv);
			if(pvDiscMap!=null && pvDiscMap.containsKey("amtDisc")){
				amt_disc = new BigDecimal(pvDiscMap.get("amtDisc").toString());
			}
			if(amt_disc.compareTo(BigDecimal.ZERO) > 0){
				// 优惠总额减免
				for (BlOpDt bpt : blOpDts) {
					aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(bpt.getAmount()));
				}
				if(amt_disc.compareTo(aggregateAmount) >= 0)
				{
					//优惠金额>=总金额时，优惠金额=总金额，患者自费金额=0
					discAmount = aggregateAmount;
					patientsPay = BigDecimal.ZERO;
				}else{
					//优惠金额<总金额时，优惠金额=优惠金额，患者自费金额=总金额-优惠金额
					discAmount = amt_disc;
					patientsPay = aggregateAmount.subtract(amt_disc).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
				rgv.setAggregateAmount(aggregateAmount); // 金额合计
				rgv.setMedicarePayments(medicarePayments.add(discAmount)); // 医保支付  = 医保+优惠金额
				rgv.setPatientsPay(patientsPay); // 患者支付
				rgv.setAccountPrepaid(accountPrepaid); // 账户已付
			}else{
				/*
				 * TODO: 2021-03-18 lipz
				 * 3.2计划生育 - 定项目，但一定要录入所在镇区
				 * 必须是计划生育身份并且就诊表pk_contraception不为空，而且有优惠金额的才能享受优惠
				 * hp-门诊计划生育-31
				 */
				if("31".equals(bdHp.getCode())){
					BigDecimal amt_disc_jhsy = BigDecimal.ZERO;
					Map<String, Object> jhsyDiscMap = getJhsyPayerDiscByPv(pkPv);
					if(jhsyDiscMap!=null && jhsyDiscMap.containsKey("amtDisc")){
						amt_disc_jhsy = new BigDecimal(jhsyDiscMap.get("amtDisc").toString());
					}
					if(amt_disc_jhsy.compareTo(BigDecimal.ZERO) > 0){
						discAmount = amt_disc_jhsy;
					}else{
						discAmount = BigDecimal.ZERO;
					}
					for (BlOpDt bpt : blOpDts) {
						aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(bpt.getAmount()));
					}
					rgv.setAggregateAmount(aggregateAmount); // 金额合计
					rgv.setMedicarePayments(discAmount); // 医保支付  = 医保+优惠金额
					patientsPay = aggregateAmount.subtract(discAmount);
					rgv.setPatientsPay(patientsPay); // 患者支付
					rgv.setAccountPrepaid(BigDecimal.ZERO); // 账户已付
					
				}else if("2000".equals(bdHp.getCode()) || "20".equals(bdHp.getCode()) || "24".equals(bdHp.getCode()) 
						|| "42".equals(bdHp.getCode()) || "25".equals(bdHp.getCode()) || "26".equals(bdHp.getCode()) 
						|| "27".equals(bdHp.getCode()) || "28".equals(bdHp.getCode()) 
						){	
					/*
					 *  全记账
					 *  儿童福利院\东区产检记账\五桂山产检记账\地贫产前诊断\新冠疫情记账\感染记账\科研记账\GCP记账
					 */
					for (BlOpDt bpt : blOpDts) {
						aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(bpt.getAmount()));
					}
					rgv.setAggregateAmount(aggregateAmount); // 金额合计
					rgv.setMedicarePayments(aggregateAmount); // 医保支付  = 医保+优惠金额
					rgv.setPatientsPay(BigDecimal.ZERO); // 患者支付
					rgv.setAccountPrepaid(BigDecimal.ZERO); // 账户已付
					
				}else {
					/*
					 *  按明细优惠
					 */
					String pkBlOpDtInSql = CommonUtils.convertSetToSqlInPart(pkBlOpDt, "pk_cgop");
					String sql = "select * from bl_op_dt where pk_cgop in (" + pkBlOpDtInSql + ")";
					blOpDts = DataBaseHelper.queryForList(sql, BlOpDt.class, new Object[] {});
					opCgTransforVo.setBlOpDts(blOpDts);
					
					for (BlOpDt blOpDt : blOpDts) {
						aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(blOpDt.getAmount())) ;
						amountPiTotal = amountPiTotal.add(BigDecimal.valueOf(blOpDt.getAmountPi()));
						// 2021-04-28 lipz : 只要有付款方即可享受优惠
						if(blOpDt.getPkPayer()!=null && StringUtils.isNotEmpty(blOpDt.getPkPayer().trim())){
							// 2021-06-08 lipz : 出现部分计费项目中的付款方为个人，个人不能享受优惠
							if(!blOpDt.getPkPayer().trim().equals(grPkPay)) {
								medicarePayments = medicarePayments.add(BigDecimal.valueOf(blOpDt.getAmount()).subtract(BigDecimal.valueOf(blOpDt.getAmountPi())));
							}
						}
						// 未结算，但是已经支付的费用（未打发票）
						if ((EnumerateParameter.ONE.equals(blOpDt.getFlagAcc())) && (EnumerateParameter.ZERO.equals(blOpDt.getFlagSettle()))) {
							accountPrepaid = accountPrepaid.add(BigDecimal.valueOf(blOpDt.getAmountPi()));
						}
					}
					
					rgv.setAggregateAmount(aggregateAmount); // 金额合计
					rgv.setMedicarePayments(medicarePayments.add(discAmount)); // 医保支付  = 医保+优惠金额
					patientsPay = aggregateAmount.subtract(medicarePayments).subtract(discAmount);
					rgv.setPatientsPay(patientsPay); // 患者支付
					rgv.setAccountPrepaid(accountPrepaid); // 账户已付
				}
			}
		} else {// 第三方医保(医保上传明细时去掉了加收；过滤了amount和amountPi不相等的部分)
			BigDecimal addAmount = BigDecimal.ZERO;//加收合计
			for (BlOpDt blOpDt : blOpDts) {
				aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(blOpDt.getAmount()));
				addAmount = addAmount.add(BigDecimal.valueOf(blOpDt.getAmountAdd()));
				// 2021-04-28 lipz : 只要有优惠付款方即可享受优惠
				if(blOpDt.getPkPayer()!=null && StringUtils.isNotEmpty(blOpDt.getPkPayer().trim())){
					// 2021-06-08 lipz : 出现部分计费项目中的付款方为个人，个人不能享受优惠
					if(!blOpDt.getPkPayer().trim().equals(grPkPay)) {
						discAmount = discAmount.add(BigDecimal.valueOf(blOpDt.getAmount()).subtract(BigDecimal.valueOf(blOpDt.getAmountPi())));
					}
				}else if(BigDecimal.valueOf(blOpDt.getAmount()).compareTo(BigDecimal.valueOf(blOpDt.getAmountPi()))==1) {
					discAmount = discAmount.add(BigDecimal.valueOf(blOpDt.getAmount()).subtract(BigDecimal.valueOf(blOpDt.getAmountPi())));
				}
				//未结算但是已经支付的费用（未打发票）
				if ((EnumerateParameter.ONE.equals(blOpDt.getFlagAcc())) && (EnumerateParameter.ZERO.equals(blOpDt.getFlagSettle()))) {
					accountPrepaid = accountPrepaid.add(BigDecimal.valueOf(blOpDt.getAmountPi()));
				}
			}
			
			/* 以下处理方式：避免上传医保时计算的金额有一分钱之类的出入  */
			rgv.setAggregateAmount(aggregateAmount);//医保总金额 = 重新取费用总额
			if(aggregateAmount.compareTo(opCgTransforVo.getAmtInsuThird()) < 0){//如果统筹大于本地总额，设置统筹等于本地总额
				opCgTransforVo.setAmtInsuThird(aggregateAmount);
			}
			// TODO: 2021-07-01 lipz : 全国医保接口 重点保护对象，剩余自费进行院内记账优惠
			String bxRatio = "";
			if("08".equals(bdHp.getDtExthp())) {
				//获取保健对象级别是否存在 7001、7002
				Map<String, Object> data = getQgybIdetInfo(opCgTransforVo.getPkPi());
				if(data!=null && data.containsKey("psnIdetType") && StringUtils.isNotEmpty(data.get("psnIdetType").toString().trim())) {
					Map<String, Object> bxblMap = getZdbjBl(data.get("psnIdetType").toString());
					if(bxblMap!=null && bxblMap.containsKey("name") && StringUtils.isNotEmpty(bxblMap.get("name").toString().trim())) {
						bxRatio = bxblMap.get("name").toString().trim();//对应级别的优惠比例，1或0.5
					}
				}
			}
			if(StringUtils.isNotEmpty(bxRatio)) {
				BigDecimal discAmt = opCgTransforVo.getAmtInsuThird().add(discAmount); //优惠 = 医保统筹 + 院内优惠金额
				BigDecimal piAmt = aggregateAmount.subtract(discAmt);//自费 = 费用总额 - 优惠
				BigDecimal zdbjAmt = piAmt.multiply(new BigDecimal(bxRatio));//重点保健记账 = 自费 * 报销比例
				BigDecimal yfAmt = piAmt.subtract(zdbjAmt);//应付金额 = 自费 - 重点保健记账
				
				rgv.setMedicarePayments(discAmt.add(zdbjAmt));//医保统筹 + 院内优惠金额 + 重点保健记账
				rgv.setPatientsPay(yfAmt);// 患者自费金额
			}else {
				rgv.setMedicarePayments(opCgTransforVo.getAmtInsuThird().add(discAmount));
				rgv.setPatientsPay(aggregateAmount.subtract(rgv.getMedicarePayments()));// 患者自费金额 = 费用总额 - 医保支付
			}
			rgv.setAccountPrepaid(accountPrepaid);//账户已付
		}
		return rgv;
	}
	
	/**
	 * 门诊正式结算
	 * @param blPubSettleVo	结算数据
	 * @param accountFlag	增加一个控制参数，控制是挂号结算还是门诊收费结算，false为挂号结算
	 * @param amtInsuThird	第三方医保支付金额
	 * @param insSettId		医保结算主键
	 * @param ybgzAmt		医保个帐金额
	 * @param pkExtPay		第三方支付主键
	 * @param amtSt			预结算时的结算总金额
	 * @return
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> accountedSettlement(BlPubSettleVo blPubSettleVo,boolean accountFlag,BigDecimal amtInsuThird, 
			String insSettId, BigDecimal ybgzAmt, String pkExtPay, BigDecimal amtSt) throws BusException {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("zdbjTipMsg", "");
		/*
		 * 两个收费员同时结算时，判断flag状态，flag=true代表存在已经被结算的收费条目
		 */
		List<BlOpDt> blOpDts = blPubSettleVo.getBlOpDts();
		if(checkItemBlStatus(blOpDts)){
			throw new BusException("费用数据已发生变化，请退款后刷新重新结算");
		}
		/*
		 *  查为政府记账的付款方主键
		 */
		String zfPaySql = "select PK_PAYER,NAME from bd_payer where code=?";
		Map<String, Object> zfPayMap = DataBaseHelper.queryForMap(zfPaySql, "10");
		if (zfPayMap == null) {
			throw new BusException("未维护付款方为{'政府记账':'10'}的数据");
		}
		/*
		 *  查为政府记账的付款方主键
		 */
		String zdbjPaySql = "select PK_PAYER,NAME from bd_payer where code=?";
		Map<String, Object> zdbjPayMap = DataBaseHelper.queryForMap(zdbjPaySql, "14");
		if (zdbjPayMap == null) {
			throw new BusException("未维护付款方为{'重点保健记账':'14'}的数据");
		}
		/*
		 *  查为个人的付款方主键
		 */
		String grPaySql = "select PK_PAYER,CODE,NAME from BD_PAYER where CODE=?";
		Map<String, Object> grPayMap = DataBaseHelper.queryForMap(grPaySql, "01");
		if (grPayMap==null || !grPayMap.containsKey("pkPayer")) {
			throw new BusException("未维护付款方为{'个人':'01'}的数据");
		}
		
		/*
		 * 兼容自助机、公众号没有登录的情况
		 */
		User user = null;
		if(StringUtils.isNotEmpty(blPubSettleVo.getMachineName()) && blPubSettleVo.getMachineName().startsWith("999")){
			String empSql = "SELECT emp.PK_ORG, job.PK_DEPT, emp.PK_EMP, emp.NAME_EMP FROM BD_OU_EMPLOYEE emp INNER JOIN BD_OU_EMPJOB job ON job.PK_EMP=emp.PK_EMP WHERE emp.CODE_EMP=?";
			Map<String, Object> empMap = DataBaseHelper.queryForMap(empSql, blPubSettleVo.getMachineName());
			 user = new User();
			 user.setPkOrg(empMap.get("pkOrg").toString());
			 user.setPkDept(empMap.get("pkDept").toString());
			 user.setPkEmp(empMap.get("pkEmp").toString());
			 user.setNameEmp(empMap.get("nameEmp").toString());
			 UserContext.setUser(user);
		}
		
		user = UserContext.getUser();
		String pkOrg = user.getPkOrg();
		String pkCurDept = user.getPkDept();// 当前科室
		String pkOpDoctor = user.getPkEmp();// 当前用户主键
		String nameUser = user.getNameEmp();// 当前用户名
		
		/*
		 * 患者就诊信息
		 */
		String pkPi = blPubSettleVo.getPkPi();
		String pkPv = blOpDts.get(0).getPkPv();
		
		Map<String, Object> mapParamTemp = new HashMap<String, Object>();
		mapParamTemp.put("pkPv", pkPv);
		mapParamTemp.put("pkOrg", user.getPkOrg());
		PvEncounter pvVo = cgQryMaintainService.qryPvEncounterInfo(mapParamTemp);
		blPubSettleVo.setEuPvType(pvVo.getEuPvtype());
		
		/**
		 *  1、构建结算VO
		 */
		BlSettle bs = new BlSettle();
		bs.setPkOrg(pkOrg);
		bs.setPkPi(pkPi);
		bs.setPkPv(pkPv);
		bs.setEuPvtype(blPubSettleVo.getEuPvType());
		bs.setPkInsurance(pvVo.getPkInsu());
		bs.setDtSttype(accountFlag? "01" : "00");// 结算类型 00:挂号结算, 01门诊收费结算
		bs.setEuStresult(EnumerateParameter.ZERO);// 结算结果分类
		bs.setAmountAdd(0.00);
		bs.setAmountDisc(0.00);
		bs.setAmountPrep(BigDecimal.ZERO);
		bs.setDateSt(new Date());
		bs.setPkOrgSt(pkOrg);
		bs.setPkDeptSt(pkCurDept);
		bs.setCodeSt(ApplicationUtils.getCode("0604"));
		bs.setPkEmpSt(pkOpDoctor);
		bs.setNameEmpSt(nameUser);
		bs.setFlagCc(EnumerateParameter.ZERO);
		bs.setFlagCanc(EnumerateParameter.ZERO);
		bs.setFlagArclare(EnumerateParameter.ZERO);
		bs.setAmountRound(blPubSettleVo.getAmtRound()==null?BigDecimal.ZERO:blPubSettleVo.getAmtRound());
		ApplicationUtils.setDefaultValue(bs, true);
		
		/**
		 *  2、生成结算金额信息
		 */
		BigDecimal amountSt = BigDecimal.ZERO;			// 结算金额
		BigDecimal amountPi = BigDecimal.ZERO;			// 患者自付金额
		BigDecimal discAmount = BigDecimal.ZERO;		// 患者优惠金额
		BigDecimal zfJzDiscAmount = BigDecimal.ZERO;	// 政府记账金额
		BigDecimal amt_add = BigDecimal.ZERO;			// 加收金额
		BigDecimal zdbjDiscAmount = BigDecimal.ZERO;	// 重点保健记账金额
		
		List<BlDeposit> depositList = blPubSettleVo.getDepositList();
		List<Map<String, Object>> discDetails = new ArrayList<Map<String, Object>>();
		
		/**
		 * 计算各项金额
		 *  根据主计划身份判断
		 *  1.按优惠总额减免
		 *  2.全记账
		 *  3.定项目
		 *  4.按计费明细汇总
		 */
		// 2.1 获取患者当次就诊的主计划身份、付款方、患者分类优惠金额
		BigDecimal amt_disc = BigDecimal.ZERO;
		Map<String, Object> pvMap = getHpPayerDiscByPkPv(pkPv);
		String pkPayer = pvMap.get("pkPayer").toString();
		String pkHp = pvMap.get("pkHp").toString();
		String dtExthp = pvMap.get("dtExthp").toString();
		String codeHp = pvMap.get("codeHp").toString();
		if(pvMap!=null && pvMap.containsKey("amtDisc")){
			amt_disc = new BigDecimal(pvMap.get("amtDisc").toString());
		}
		// 2.1.1按优惠总额减免
		if(amt_disc.compareTo(BigDecimal.ZERO) > 0){
			for (BlOpDt bpt : blOpDts) {
				amt_add = amt_add.add(new BigDecimal(bpt.getAmountAdd()));
				amountSt = amountSt.add(new BigDecimal(bpt.getAmount()));
			}
			bs.setAmountSt(amountSt.setScale(2,BigDecimal.ROUND_HALF_UP));
			if(amt_disc.compareTo(amountSt) >= 0)
			{
				//优惠金额>=总金额时，优惠金额=总金额，患者自费金额=0
				bs.setAmountDisc(amountSt.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
				amountPi = BigDecimal.ZERO;
			}else{
				//优惠金额<总金额时，优惠金额=优惠金额，患者自费金额=总金额-优惠金额
				bs.setAmountDisc(amt_disc.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
				amountPi = amountSt.subtract(amt_disc).setScale(2,BigDecimal.ROUND_HALF_UP);
			}
			bs.setAmountPi(amountPi);
			bs.setAmountInsu(BigDecimal.ZERO);
			bs.setAmountAdd(amt_add.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
			
		}else{
			/*
			 * TODO: 2021-03-18 lipz
			 * 2.1.2计划生育 - 定项目，但一定要录入所在镇区
			 * 必须是计划生育身份并且就诊表pk_contraception不为空，而且有优惠金额的才能享受优惠
			 */
			if("31".equals(codeHp)){
				BigDecimal amt_disc_jhsy = BigDecimal.ZERO;
				Map<String, Object> jhsyDiscMap = getJhsyPayerDiscByPv(pkPv);
				if(jhsyDiscMap!=null && jhsyDiscMap.containsKey("amtDisc")){
					amt_disc_jhsy = new BigDecimal(jhsyDiscMap.get("amtDisc").toString());
				}
				if(amt_disc_jhsy.compareTo(BigDecimal.ZERO) > 0){
					discAmount = amt_disc_jhsy;
					pkPayer = jhsyDiscMap.get("pkPayer").toString();
				}else{
					discAmount = BigDecimal.ZERO;
				}
				for (BlOpDt bpt : blOpDts) {
					amt_add = amt_add.add(new BigDecimal(bpt.getAmountAdd()));
					amountSt = amountSt.add(BigDecimal.valueOf(bpt.getAmount()));
				}
				bs.setAmountSt(amountSt.setScale(2,BigDecimal.ROUND_HALF_UP));
				bs.setAmountDisc(discAmount.doubleValue());
				amountPi = amountSt.subtract(discAmount);
				bs.setAmountPi(amountPi);
				bs.setAmountInsu(BigDecimal.ZERO);
				bs.setAmountAdd(amt_add.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
				
			}else if("2000".equals(codeHp) || "20".equals(codeHp) || "24".equals(codeHp) 
				|| "42".equals(codeHp) || "25".equals(codeHp) || "26".equals(codeHp) || "27".equals(codeHp) || "28".equals(codeHp) ){	
				/*
				 *  2.1.3全记账
				 *  儿童福利院\东区产检记账\五桂山产检记账\地贫产前诊断\新冠疫情记账\感染记账\科研记账\GCP记账
				 */
				for (BlOpDt bpt : blOpDts) {
					amt_add = amt_add.add(new BigDecimal(bpt.getAmountAdd()));
					amountSt = amountSt.add(BigDecimal.valueOf(bpt.getAmount()));
				}
				bs.setAmountSt(amountSt.setScale(2,BigDecimal.ROUND_HALF_UP));
				bs.setAmountDisc(amountSt.doubleValue());
				bs.setAmountPi(BigDecimal.ZERO);
				bs.setAmountInsu(BigDecimal.ZERO);
				bs.setAmountAdd(amt_add.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
								
			}else{
				/*
				 * 2.1.4按计费明细汇总
				 */
				for (BlOpDt bpt : blOpDts) {
					amt_add = amt_add.add(new BigDecimal(bpt.getAmountAdd()));
					amountSt = amountSt.add(new BigDecimal(bpt.getAmount()));
					amountPi = amountPi.add(new BigDecimal(bpt.getAmountPi()));
					// 2021-04-28 lipz : 只要有优惠付款方即可享受优惠
					if(bpt.getPkPayer()!=null && StringUtils.isNotEmpty(bpt.getPkPayer().trim())){
						// 2021-06-08 lipz : 出现部分计费项目中的付款方为个人，个人不能享受优惠
						if(!bpt.getPkPayer().trim().equals(grPayMap.get("pkPayer").toString())) {
							BigDecimal discAmt = BigDecimal.valueOf(bpt.getAmount()).subtract(BigDecimal.valueOf(bpt.getAmountPi()));
							discAmount = discAmount.add(discAmt);
							
							//加入结算明细
							BigDecimal payAmt = BigDecimal.ZERO;
							for(Map<String, Object> pa : discDetails) {
								if(pa.get("pkPayer").toString().equals(bpt.getPkPayer().trim())) {
									payAmt = new BigDecimal(pa.get("amt").toString());
									break;
								}
							}
							if(payAmt.compareTo(BigDecimal.ZERO)==0) {
								Map<String, Object> payAmtMap = new HashMap<String, Object>();
								payAmtMap.put("pkPayer", bpt.getPkPayer().trim());
								payAmtMap.put("amt", discAmt);
								discDetails.add(payAmtMap);
							}else {
								for(Map<String, Object> pa : discDetails) {
									if(pa.get("pkPayer").toString().equals(bpt.getPkPayer().trim())) {
										payAmt = discAmt.add(new BigDecimal(pa.get("amt").toString()));
										pa.put("amt", payAmt);
										break;
									}
								}
							}
							
						}
					}
				}
				bs.setAmountSt(amountSt.setScale(2,BigDecimal.ROUND_HALF_UP));//结算总额
				bs.setAmountInsu(amtInsuThird.setScale(2,BigDecimal.ROUND_HALF_UP));//第三方医保支付
				// TODO: 2021-07-01 lipz : 全国医保接口 重点保护对象，剩余自费进行院内记账优惠
				String bxRatio = "";
				if("08".equals(dtExthp)) {
					Map<String, Object> data = getQgybIdetInfo(pkPi);
					if(data!=null && data.containsKey("psnIdetType") && StringUtils.isNotEmpty(data.get("psnIdetType").toString().trim())) {
						Map<String, Object> bxblMap = getZdbjBl(data.get("psnIdetType").toString());
						if(bxblMap!=null && bxblMap.containsKey("name") && StringUtils.isNotEmpty(bxblMap.get("name").toString().trim())) {
							bxRatio = bxblMap.get("name").toString().trim();//对应级别的优惠比例，1或0.5
							BigDecimal bxRatioVal = new BigDecimal(bxRatio).multiply(new BigDecimal("100.0"));
							result.put("zdbjTipMsg", "结算成功。该病人是【重点保健对象】【"
									+("7001".equals(data.get("psnIdetType").toString())?"一级":"二级")+"】，享受自费部分院内记账比例【"
									+(bxRatioVal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue())+"%】。请打印电子发票让病人签名保留提交给财务。");
						}
					}
				}
				if(StringUtils.isNotEmpty(bxRatio)) {
					BigDecimal zfAmt = amountSt.subtract(amtInsuThird).subtract(discAmount);//自费 = 总额 - 医保统筹 - 院内优惠
					zdbjDiscAmount = zfAmt.multiply(new BigDecimal(bxRatio));//重点保健记账 = 自费 * 报销比例
					bs.setAmountDisc(discAmount.add(zdbjDiscAmount).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());//院内优惠总额 + 重点保健记账
					bs.setAmountPi(zfAmt.subtract(zdbjDiscAmount).setScale(2,BigDecimal.ROUND_HALF_UP));//患者自费 = 自费 - 重点保健记账
				}else {
					/*
					 * TODO: lipz 2021-05-12 加上：93产前诊断补助记账 的金额到优惠金额
					 */
					for (BlDeposit blDeposit : depositList) {
						if("93".equals(blDeposit.getDtPaymode())){
							zfJzDiscAmount = zfJzDiscAmount.add(blDeposit.getAmount());
						}
					}
					bs.setAmountDisc(discAmount.add(zfJzDiscAmount).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());//院内优惠总额 + 产前诊断补助记账
					amountPi = amountSt.subtract(amtInsuThird).subtract(discAmount).subtract(zfJzDiscAmount);
					bs.setAmountPi(amountPi.setScale(2,BigDecimal.ROUND_HALF_UP));//患者自费
				}
				bs.setAmountAdd(amt_add.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
			}
		}
		
		//已传入预结算总金额 并且不为0时，需要检验是否与当前结算总金额一致，不一致的抛出异常，提示需重新结算
		if(amtSt!=null && amtSt.compareTo(BigDecimal.ZERO)>0) {
			if(amtSt.compareTo(bs.getAmountSt())!=0) {
				throw new BusException("预结算总金额【"+amtSt.toString()+"】与正式结算总金额【"+bs.getAmountSt().toString()+"】不一致。费用发生了变化，请在退款后重新结算缴费！");
			}
		}
		
		// 2.3、保存结算
		DataBaseHelper.insertBean(bs);
		
		// 2.4.将结算主键反写到记费细目表
		String pkSettle = bs.getPkSettle();
		List<BlOpDt> blOpDtNews = new ArrayList<BlOpDt>();
		for (BlOpDt bpt : blOpDts) {
			BlOpDt bodNew = new BlOpDt();
			bodNew.setPkSettle(pkSettle);
			bodNew.setFlagSettle(EnumerateParameter.ONE);
			bodNew.setTs(new Date());
			bodNew.setPkCgop(bpt.getPkCgop());
			blOpDtNews.add(bodNew);
		}
		DataBaseHelper.batchUpdate("update bl_op_dt set pk_settle=:pkSettle,flag_settle=:flagSettle,ts=:ts where pk_cgop=:pkCgop ", blOpDtNews);
		
		/**
		 * 3、结算明细组成说明： 
		 * （1）记费表bl_op_dt内部医保支付金额
		 * （2）记费表bl_op_dt优惠比例金额
		 * （3）结算时第三方医保支付金额
		 * （4）结算时每次调动医保返回的报销金额
		 * （5）患者自付金额
		 * （6）付款方 优惠金额
		 * （7）院内 产前诊断补助记账 金额
		 */
		List<BlSettleDetail> blSettleDetails = new ArrayList<BlSettleDetail>();
		// 3.1优惠总额减免, 只需要生成一条优惠结算明细 + 3.6的个人结算明细
		if(amt_disc.compareTo(BigDecimal.ZERO) > 0){
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(bs.getPkSettle());
			blSettleDetail.setPkPayer(pkPayer);// 支付方
			blSettleDetail.setPkInsurance(pkHp);// 主医保计划
			blSettleDetail.setAmount(bs.getAmountDisc());
			ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
			blSettleDetails.add(blSettleDetail);
		}else{
			if("31".equals(codeHp)){
				/*
				 *  TODO: 2021-03-18 lipz
				 *  3.2计划生育 - 定项目，只有享受优惠金额大于0时才写入
				 *  只需要生成一条优惠结算明细 + 3.4.3的个人结算明细
				 */
				if(discAmount.compareTo(BigDecimal.ZERO) > 0){
					BlSettleDetail blSettleDetail = new BlSettleDetail();
					blSettleDetail.setPkSettle(bs.getPkSettle());
					blSettleDetail.setPkPayer(pkPayer);// 支付方
					blSettleDetail.setPkInsurance(pkHp);// 主医保计划
					blSettleDetail.setAmount(bs.getAmountDisc());
					ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
					blSettleDetails.add(blSettleDetail);
				}
			
			}else if("2000".equals(codeHp) || "20".equals(codeHp) || "24".equals(codeHp) 
					|| "42".equals(codeHp) || "25".equals(codeHp) || "26".equals(codeHp) || "27".equals(codeHp) || "28".equals(codeHp) ){	
				/*
				 *  3.3全记账：总额减免, 只需要生成一条优惠结算明细
				 *  儿童福利院\东区产检记账\五桂山产检记账\地贫产前诊断\新冠疫情记账\感染记账\科研记账\GCP记账
				 */
				BlSettleDetail blSettleDetail = new BlSettleDetail();
				blSettleDetail.setPkSettle(bs.getPkSettle());
				blSettleDetail.setPkPayer(pkPayer);// 支付方
				blSettleDetail.setPkInsurance(pkHp);// 主医保计划
				blSettleDetail.setAmount(bs.getAmountDisc());
				ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
				blSettleDetails.add(blSettleDetail);
								
			}else{
				/*
				 * 3.4 按明细
				 */
				// 3.4.1、添加主医保是外部医保的情况
				if(amtInsuThird.compareTo(BigDecimal.ZERO)==1){
					BlSettleDetail blSettleDetail = new BlSettleDetail();
					blSettleDetail.setPkSettle(bs.getPkSettle());
					blSettleDetail.setPkPayer(pkPayer);// 支付方
					blSettleDetail.setPkInsurance(pkHp);// 主医保计划
					blSettleDetail.setAmount(amtInsuThird.doubleValue());//
					ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
					blSettleDetails.add(blSettleDetail);
				}
				/*
				 * 3.4.2、患者的优惠比例金额
				 */
				if (discAmount.compareTo(BigDecimal.ZERO) == 1) {
					for(Map<String, Object> payAmtMap : discDetails) {
						BlSettleDetail blSettleDetail = new BlSettleDetail();
						blSettleDetail.setPkSettle(bs.getPkSettle());
						blSettleDetail.setPkPayer(payAmtMap.get("pkPayer").toString());// 支付方
						blSettleDetail.setPkInsurance(pkHp);// 主医保计划
						blSettleDetail.setAmount(Double.valueOf(payAmtMap.get("amt").toString()));
						ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
						blSettleDetails.add(blSettleDetail);
					}
				}
				/*
				 * TODO lipz 2021-07-01
				 * 3.4.3 重点保健对象记账金额
				 */
				if(zdbjDiscAmount.compareTo(BigDecimal.ZERO) == 1) {
					BlSettleDetail blSettleDetail = new BlSettleDetail();
					blSettleDetail.setPkSettle(bs.getPkSettle());
					blSettleDetail.setPkPayer(zdbjPayMap.get("pkPayer").toString());// 支付方(重点保健记账)
					blSettleDetail.setPkInsurance(pkHp);//主医保计划
					blSettleDetail.setAmount(zdbjDiscAmount.doubleValue());// 重点保健记账金额
					ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
					blSettleDetails.add(blSettleDetail);
				}
			}
		}
		/*
		 *  3.5、患者支付金额
		 */
		if (bs.getAmountPi().compareTo(BigDecimal.ZERO) == 1) {
			/*
			 * 要先处理掉以下支付方式的金额
			 * 93产前诊断补助记账
			 */
			for (BlDeposit blDeposit : depositList) {
				String codePayMode = blDeposit.getDtPaymode();
				if("93".equals(codePayMode)){
					BlSettleDetail blSettleDetail = new BlSettleDetail();
					blSettleDetail.setPkSettle(bs.getPkSettle());
					blSettleDetail.setPkPayer(zfPayMap.get("pkPayer").toString());// 支付方(政府记账)
					blSettleDetail.setPkInsurance(pkHp);//政府记账部分
					blSettleDetail.setAmount(blDeposit.getAmount().doubleValue());// 政府记账金额
					ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
					blSettleDetails.add(blSettleDetail);
				}
			}
			
			// 实际个人支付金额
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(bs.getPkSettle());
			blSettleDetail.setPkPayer(grPayMap.get("pkPayer").toString());// 支付方(个人)
			blSettleDetail.setPkInsurance(pkHp);//自费部分，全自费主键
			blSettleDetail.setAmount(bs.getAmountPi().doubleValue());// 患者自付金额
			ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
			blSettleDetails.add(blSettleDetail);
		}
		// 3.6、批量统一插入结算明细表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), blSettleDetails);
		
		/*
		 *  4、写结算交款记录表
		 */
		boolean flagExtPay = "1".equals(ApplicationUtils.getSysparam("BL0007", false));//第三方支付线上支付标志
		if(depositList!=null){
			List<Map<String,Date>> bankTimeList = new ArrayList<>();
			List<BlDeposit> depositListDao = new ArrayList<BlDeposit>();
			for (BlDeposit blDeposit : depositList) {
				net.sf.json.JSONObject bdObject = net.sf.json.JSONObject.fromObject(blDeposit);
				logger.info("结算交款记录==>>" + bdObject.toString());
				String codePayMode = blDeposit.getDtPaymode();
				
				//医保个帐的需要修改 INS_QGYB_ST、INS_SGSYB_ST表的amt_grzhzf个人账户支付字段 - 13二维码、14实体卡
				if("12".equals(blDeposit.getDtPaymode()) || "13".equals(blDeposit.getDtPaymode()) || "14".equals(blDeposit.getDtPaymode())){
					logger.info("结算医保个账记录==>>pk_pv：" + pkPv + "；setlId：" + insSettId + "；amount：" + blDeposit.getAmount());
					if(StringUtils.isEmpty(insSettId)){
						throw new BusException("有扣医保个账数据，但未传入医保结算主键[setlId]");
					}
					//回写医保个帐金额
					if(insSettId.startsWith("gs")){
						DataBaseHelper.execute("update ins_sgsyb_st set amt_grzhzf=? where PK_PV=? and yb_pksettle=?", blDeposit.getAmount(), pkPv, insSettId);
					}else{
						DataBaseHelper.execute("update ins_qgyb_st set amt_grzhzf=? where PK_PV=? and yb_pksettle=?", blDeposit.getAmount(), pkPv, insSettId);
					}
				}

				BlDeposit blDepositDao = new BlDeposit();
				ApplicationUtils.setDefaultValue(blDepositDao, true);// 设置默认字段
				blDepositDao.setPkOrg(pkOrg);
				blDepositDao.setEuDptype(EnumerateParameter.ZERO);
				blDepositDao.setEuDirect(EnumerateParameter.ONE);
				blDepositDao.setPkPi(pkPi);
				blDepositDao.setPkPv(pkPv);
				blDepositDao.setEuPvtype(blPubSettleVo.getEuPvType());
				// 交易金额
				blDepositDao.setAmount(blDeposit.getAmount());
				blDepositDao.setDtPaymode(codePayMode);
				// 1 现金、2 支票、3 银行卡、4 患者账户、7微信、8支付宝、11一代社保卡、12电子凭证、13三代社保卡、91内部转账、92财务记账、93产前诊断补助记账、99 其他
				switch (codePayMode) {
					case "2":
						break;
					case "3":
						if(flagExtPay){
							BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where serial_no=?", BlExtPay.class, blDeposit.getPayInfo());
							if (blExtPay == null) {
								throw new BusException("银行卡交易未传递商户订单号");
							}
							blExtPay.setPkDepo(blDepositDao.getPkDepo());
							blExtPay.setPkSettle(pkSettle);
							DataBaseHelper.updateBeanByPk(blExtPay, false);
							
							Map<String,Date> bankTime = new HashMap<>();
							bankTime.put(blDeposit.getPayInfo(), blDeposit.getBankTime());
							bankTimeList.add(bankTime);
							blDepositDao.setDtBank(blDeposit.getDtBank());// 对应银行
							blDepositDao.setBankNo(blDeposit.getBankNo());// 支票号码或银行卡号
						}
						break;
					case "4":
						// 账户支付
						break;
					case "7": // 微信
						if(flagExtPay){
							List<BlExtPay> blExtPayList = DataBaseHelper.queryForList("select * from bl_ext_pay where serial_no=? order by date_pay desc", 
									BlExtPay.class, blDeposit.getPayInfo());
							if(blExtPayList == null || blExtPayList.size()<=0){
								throw new BusException("微信交易未传递商户订单号");
							}
							BlExtPay blExtPay = blExtPayList.get(0);
							blExtPay.setPkPi(pkPi);
							blExtPay.setPkPv(pkPv);
							blExtPay.setTs(new Date());
							// 关联付款明细表 主键
							blExtPay.setPkDepo(blDepositDao.getPkDepo());
							blExtPay.setPkSettle(pkSettle);
							DataBaseHelper.updateBeanByPk(blExtPay, false);
							
							blDepositDao.setDtBank(blExtPay.getDtBank());
						}
						break;
					case "8": // 支付宝
						if(flagExtPay){
							BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where serial_no=?", 
														BlExtPay.class, blDeposit.getPayInfo());
							if(blExtPay == null){
								throw new BusException("支付宝交易未传递商户订单号");
							}
							blExtPay.setPkPi(pkPi);
							blExtPay.setPkPv(pkPv);
							blExtPay.setTs(new Date());
							// 关联付款明细表 主键
							blExtPay.setPkDepo(blDepositDao.getPkDepo());
							blExtPay.setPkSettle(pkSettle);
							DataBaseHelper.updateBeanByPk(blExtPay, false);
							
							blDepositDao.setDtBank(blExtPay.getDtBank());
						}
						break;
					default:
						if (flagExtPay && blDeposit.getPayInfo() != null) {
							BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where serial_no=?",
									BlExtPay.class, blDeposit.getPayInfo());
							if (blExtPay == null) {
								throw new BusException("第三方交易未传递商户订单号");
							}
							blExtPay.setPkPi(pkPi);
							blExtPay.setPkPv(pkPv);
							blExtPay.setTs(new Date());
							// 关联付款明细表 主键
							blExtPay.setPkDepo(blDepositDao.getPkDepo());
							blExtPay.setPkSettle(pkSettle);
							DataBaseHelper.updateBeanByPk(blExtPay, false);
							
							if(StringUtils.isNotEmpty(blDeposit.getDtBank())){
								blDepositDao.setDtBank(blDeposit.getDtBank());
							}
						}
						break;
				}
				blDepositDao.setPayInfo(blDeposit.getPayInfo()); //收付款方式信息 对应支票号，银行交易号码
				blDepositDao.setDatePay(new Date());
				blDepositDao.setPkDept(pkCurDept);
				blDepositDao.setPkEmpPay(pkOpDoctor);
				blDepositDao.setNameEmpPay(nameUser);
				blDepositDao.setFlagAcc(blDepositDao.getFlagAcc() == null ? EnumerateParameter.ZERO : EnumerateParameter.ONE);
				blDepositDao.setFlagSettle(EnumerateParameter.ONE);
				blDepositDao.setPkSettle(bs.getPkSettle());
				blDepositDao.setFlagCc(EnumerateParameter.ZERO);// 操作员结账标志
				blDepositDao.setNameWork(blDeposit.getNameWork());//单位名称
				blDepositDao.setNameOpera(blDeposit.getNameOpera());//经办人
				blDepositDao.setNote(blDeposit.getNote());//交款描述信息
				blDepositDao.setEuRemove(blDeposit.getEuRemove());// 核销状态
				blDepositDao.setPkEmpRemove(blDeposit.getPkEmpRemove());//核销人
				blDepositDao.setDateRemove(blDeposit.getDateRemove());//核销时间
				blDepositDao.setNoteRemove(blDeposit.getNoteRemove());//核销描述信息
				depositListDao.add(blDepositDao);

			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depositListDao);
		}
		
		/*
		 *  5、门诊缴费，门诊收费结算后是否直接打印发票
		 */
		if(accountFlag && "1".equals(ApplicationUtils.getSysparam("BL0008", false))){
			// 5、获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
			String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
			if("1".equals(eBillFlag)){
				// 开立电子票据
				// 是在该服务返回成功后客户端再调用功能号【007002003058->022003027098】进行开立
			}else{
				// 开立纸质票据
				List<BlInvoice> blInvoices = new ArrayList<>(); // 发票
				List<BlInvoiceDt> blInvoiceDts = new ArrayList<BlInvoiceDt>(); // 发票明细
				List<BlStInv> blStInvs = new ArrayList<BlStInv>(); // 写发票与结算的关系
				List<InvoiceInfo> invoiceInfos = blPubSettleVo.getInvoiceInfo(); // 发票票据号和明细
				if(invoiceInfos == null||invoiceInfos.size()<=0){
					throw new BusException("收费结算时，未传入发票信息！");
				}
				
				/**
				 * 因门诊发票保存逻辑每个项目的规则不一样，故各项目通过配置文件来写自己项目的逻辑分支
				 */
				invoiceInfos.get(0).setFlagPrint(CommonUtils.isEmptyString(blPubSettleVo.getFlagPrint())?"1":blPubSettleVo.getFlagPrint());//打印电子票据结算时使用
				invoiceInfos.get(0).setMachineName(blPubSettleVo.getMachineName());
				
				Map<String,Object> invMap = new HashMap<String, Object>();
				if(!EnumerateParameter.ONE.equals(eBillFlag)) {
					invMap = ZsbaInvPrintProcessUtils.saveOpInvInfo(bs, invoiceInfos);
					if(invMap==null || invMap.size()<=0){
						throw new BusException("收费结算时，发票信息组织失败，请检查！");
					}
				}	

				blInvoices = (List<BlInvoice>)invMap.get("inv");
				blInvoiceDts = (List<BlInvoiceDt>)invMap.get("invDt");
				blStInvs = (List<BlStInv>)invMap.get("stInv");
				
				if(blInvoices!=null && blInvoices.size() > 0){
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), blInvoices); // 批量插入发票
				}
				if(blInvoiceDts!=null && blInvoiceDts.size() > 0){
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), blInvoiceDts); // 批量插入发票明细
				}
				if(blStInvs!=null && blStInvs.size() > 0){
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), blStInvs); // 批量写发票与结算的关系
				}
				result.put("inv", blInvoices);
				result.put("invDt", blInvoiceDts);
			}
		}
		
		/*
		 * 6、关联医保与院内的结算
		 */
		if(StringUtils.isNotEmpty(insSettId)){
			if(ybgzAmt.compareTo(BigDecimal.ZERO)>0){
				if(insSettId.startsWith("gs")){
					DataBaseHelper.execute("update ins_sgsyb_st set amt_grzhzf=?, pk_settle=? where PK_PV=? and yb_pksettle=?", ybgzAmt, bs.getPkSettle(), bs.getPkPv(), insSettId);
				}else{
					DataBaseHelper.execute("update ins_qgyb_st set amt_grzhzf=?, pk_settle=? where PK_PV=? and yb_pksettle=?", ybgzAmt, bs.getPkSettle(), bs.getPkPv(), insSettId);
				}
			}else{
				if(insSettId.startsWith("gs")){
					DataBaseHelper.execute("update ins_sgsyb_st set pk_settle=? where PK_PV=? and yb_pksettle=?", bs.getPkSettle(), bs.getPkPv(), insSettId);
				}else{
					DataBaseHelper.execute("update ins_qgyb_st set pk_settle=? where PK_PV=? and yb_pksettle=?", bs.getPkSettle(), bs.getPkPv(), insSettId);
				}
			}
		}
		
		/*
		 * 7、公众号-自助机调用结算有第三方交易主键则关联相关记录结算主键
		 */
		if(StringUtils.isNotEmpty(pkExtPay)){
			String[] pkExtPays = pkExtPay.split(",");
			for(String pkExt : pkExtPays) {
				if(StringUtils.isEmpty(pkExt)) {
					continue;
				}
				DataBaseHelper.execute("update BL_EXT_PAY set PK_SETTLE=? WHERE PK_EXTPAY=?", pkSettle, pkExt);
				DataBaseHelper.execute("update BL_DEPOSIT set PK_SETTLE=? WHERE PK_DEPO IN (SELECT PK_DEPO FROM BL_EXT_PAY WHERE PK_EXTPAY=?)", pkSettle, pkExt);
			}
		}
		result.put("pkEmpSt", bs.getPkEmpSt());
		result.put("amtPi", bs.getAmountPi());
		result.put("codeSt", bs.getCodeSt());
		result.put("pkSettle", bs.getPkSettle());
		return result;
	}

	/**
	 * 退号预结算查询服务
	 * @return mapParam
	 */
	public OpCgTransforVo registrationPreparedRefound(Map<String, String> mapParamTemp) throws BusException {

		String pkPv = mapParamTemp.get("pkPv");
		String pkOrg = mapParamTemp.get("pkOrg");
//		String pkSch = mapParamTemp.get("pkSch");
		String pkEmp = mapParamTemp.get("pkEmp");
		String empName = mapParamTemp.get("empName");
		BigDecimal aggregateAmount = BigDecimal.ZERO;// 金额合计
		BigDecimal patientsPay = BigDecimal.ZERO;// 患者支付
		BigDecimal medicarePayments = BigDecimal.ZERO;// 医保支付
		OpCgTransforVo paramVo = new OpCgTransforVo();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkPv", pkPv);
		mapParam.put("pkOrg", pkOrg);
		/**
		 * 考虑外部医保的情况
		 */
		//==============================
		/* 前台传pk_hp查询主医保是否是外部医保
		 * 如果不是外部医保，走bl_op_dt表
		 * 如果是外部医保，走bl_settle表   */
		//===============================
		
		
		// 根据pkPv查询所有的挂号收费
		List<BlOpDt> blOpDts = cgQryMaintainService.qryRegCostInfoByPkpv(mapParam);
		if(blOpDts!=null&&blOpDts.size()>0){
			String pkSettle = blOpDts.get(0).getPkSettle();
			mapParam.put("pkSettle", pkSettle);
			for (BlOpDt blOpDt : blOpDts) {
				aggregateAmount = aggregateAmount.add(new BigDecimal(blOpDt.getAmount()));
				patientsPay = patientsPay.add(new BigDecimal(blOpDt.getAmountPi()));
			}
			List<BlDeposit> blDeposits = cgQryMaintainService.qryRecordCashierByPkSettle(mapParam);
			
			//如果以银行卡的方式结算退号时加入银行卡信息
			List<BlExtPay> blExtPays = new ArrayList<>();
			//从bl_ext_pay中获取银行交易方式时的交易时间
			for (BlDeposit blDeposit : blDeposits) {
				boolean flag = blDeposit.getDtPaymode().equals(EnumerateParameter.THREE) || 
							   blDeposit.getDtPaymode().equals(EnumerateParameter.SEVEN) ||
							   blDeposit.getDtPaymode().equals(EnumerateParameter.EIGHT);
				if(flag){
					BlExtPay blExtPay = thirdPartyPaymentService.qryBlExtPayByBlDeposit(blDeposit.getPkDepo());
					if(blExtPay != null){
						//将与银行的交易时间返回
						blDeposit.setBankTime(blExtPay.getDatePay());
						blDeposit.setSerialNum(blExtPay.getSerialNo());
						blDeposit.setPayResult(blExtPay.getPayResult());
						blDeposit.setOutTradeNo(blExtPay.getOutTradeNo());
						blDeposit.setExtAmount(blExtPay.getAmount());
						blExtPays.add(blExtPay);
					}
				}
			}
		
			// 门诊医生站未支付挂号费之后在挂号处退号
			if (blOpDts.get(0).getPkSettle() == null) {
				// 删除诊间挂号费用
				DataBaseHelper.execute("delete from bl_op_dt where pk_pv=?", blOpDts.get(0).getPkPv());
				// 退号
				String sql = "update pv_encounter set eu_status='9',flag_cancel='1', pk_emp_cancel=?, name_emp_cancel=?, date_cancel=? where pk_pv = ?";
				DataBaseHelper.execute(sql, new Object[] { pkEmp, empName, new Date(), pkPv });
				// 释放资源
				DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = (select pk_sch from pv_op where pk_pv = ? )", new Object[] { pkPv });
				paramVo.setFlagCalinc(EnumerateParameter.ONE);
				return paramVo;
			}
		
			paramVo.setAggregateAmount(aggregateAmount);
			paramVo.setPatientsPay(patientsPay);
			medicarePayments = aggregateAmount.subtract(patientsPay);
			paramVo.setMedicarePayments(medicarePayments);
			paramVo.setBlDeposits(blDeposits);
			paramVo.setBlExtPays(blExtPays);
			paramVo.setPkSettle(blOpDts.get(0).getPkSettle());//灵璧门诊退号需要返还pkSettle--jiangdong
			mapParam.put("pkSettle", blOpDts.get(0).getPkSettle());
			// 根据结算主键查询作废结算时对应的发票
			String code = ApplicationUtils.getSysparam("BL0002", false);
			if (EnumerateParameter.ONE.equals(code)) {
				List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
				if(blInvoices!=null&&blInvoices.size()>0){
					// 更新作废发票信息
					paramVo.setInVoiceNo(blInvoices.get(0).getCodeInv());// 发票号
				}
			}
		}
		return paramVo; 
	}

	/**
	 * 退号结算
	 */
	public void registrationRefound(Map<String, String> mapParamTemp, List<BlExtPayBankVo> blExtPayBank) throws BusException {

		String pkPv = mapParamTemp.get("pkPv");
		String pkOrg = mapParamTemp.get("pkOrg");
		Map<String, Object> mapParam = new HashMap<String, Object>();
		User user = UserContext.getUser();
		mapParam.put("pkPv", pkPv);
		mapParam.put("pkOrg", pkOrg);
		// 根据就诊主键查询挂号记费明细
		List<BlOpDt> blOpDts = cgQryMaintainService.qryRegCostInfoByPkpv(mapParam);
		if(blOpDts!=null&&blOpDts.size()>0){
			int size = blOpDts.size();
			String pkSettle = blOpDts.get(0).getPkSettle();
			String pkPi = blOpDts.get(0).getPkPi();
			mapParam.put("pkPi", pkPi);
			if (size > 1)
				for (int i = 1; i < size; i++) {
					if (!pkSettle.equals(blOpDts.get(i).getPkSettle())) {
						throw new BusException("此次挂号费用异常，形成了两笔结算信息。" + "pkPv:【" + blOpDts.get(i).getPkPv() + "】");
					}
				}
			// 根据结算主键查询结算信息
			mapParam.put("pkSettle", pkSettle);
			BlSettle blSettle = cgQryMaintainService.qryBlSettleInfoByPkpv(mapParam);
			// 生成退费结算信息
			String pkSettleCanc = opcgPubHelperService.generateRefoundSettle(blSettle);
			// 生成退费明细
			opcgPubHelperService.generateRefoundRecord(blOpDts, blSettle.getPkSettle());// 传入新的结算主键
			// 根据结算主键查询结算明细
			List<BlSettleDetail> blSettleDetail = cgQryMaintainService.qryBlSettleDetailInfoByBlSettle(mapParam);
			// 生成退费结算明细
			 opcgPubHelperService.generateRefoundSettleDetail(blSettleDetail,pkSettleCanc);

			// 根据结算主键查询交款记录信息
			List<BlDeposit> blDeposits = cgQryMaintainService.qryRecordCashierByPkSettle(mapParam);
			
			// 生成退费的交款记录信息
			List<BlDeposit> negaBlDeposits = opcgPubHelperService.generateRefoundBlDeposits(blDeposits,pkSettleCanc);
			
			// 生成付款方式冲负记录
			BlExtPayBankVo blExt = null;
			List<BlExtPay> blExtPays = new ArrayList<BlExtPay>();
			//加了非空判断
			if(negaBlDeposits != null){
				for (BlDeposit negaBlDeposit : negaBlDeposits) {
					if (negaBlDeposit.getDtPaymode().equals(IDictCodeConst.PATIACCOUNT)) {
						// 更新患者账户，调用患者账户消费服务
						BlDepositPi blDepositPi = new BlDepositPi();
						ApplicationUtils.setDefaultValue(blDepositPi, true);
						blDepositPi.setEuDirect(EnumerateParameter.ONE);
						blDepositPi.setPkPi(pkPi);
						blDepositPi.setAmount(negaBlDeposit.getAmount().abs());
						blDepositPi.setDtPaymode(EnumerateParameter.FOUR);
						blDepositPi.setPkEmpPay(user.getPkEmp());
						blDepositPi.setNameEmpPay(user.getNameEmp());
						PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(pkPi);// 患者账户信息
						ApplicationUtils.setDefaultValue(piAcc, false);
						piAcc.setAmtAcc((piAcc.getAmtAcc() == null ? BigDecimal.ZERO : piAcc.getAmtAcc()).add(blDepositPi.getAmount()));
						balAccoutService.piAccDetailVal(piAcc, blDepositPi, pkPv, null);
					}else if(negaBlDeposit.getDtPaymode().equals(IDictCodeConst.BANKCARD)){
						// 银行卡支付
						blExt = blExtPayBank.get(0);  // 新旧交易号
						BlExtPay refundBankCardBlExtPay = thirdPartyPaymentService.refundBankCardBlExtPay(negaBlDeposit, blExt);
						blExtPays.add(refundBankCardBlExtPay);
					}else if(negaBlDeposit.getDtPaymode().equals(IDictCodeConst.WECHAT) || 
							negaBlDeposit.getDtPaymode().equals(IDictCodeConst.ALI)){
						blExt = blExtPayBank.get(0);  // 新旧交易号
						// 微信、支付宝支付
						BlExtPay refundBankCardBlExtPay = thirdPartyPaymentService.refundWeixinOrAliPayBlExtPay(negaBlDeposit, blExt);
						blExtPays.add(refundBankCardBlExtPay);
					}
				}
			}
			
			// 批量更新外部表
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlExtPay.class), blExtPays);
			
//			// 若退费方式为银行卡  更新bl_ext_pay表中字段
//			thirdPartyPaymentService.registeredRefundRecords(mapParam, blExtPayBank);

			String BL0002_code = ApplicationUtils.getSysparam("BL0002", false);
			// 如果挂号时打印的发票，要作废发票
			if (EnumerateParameter.ONE.equals(BL0002_code)) {
				// 根据结算主键查询作废结算时对应的发票
				List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
				if(blInvoices!=null&&blInvoices.size()>0){
					for(BlInvoice inv:blInvoices){
						// 更新作废发票信息
						opcgPubHelperService.updateRefoundBlInvoice(inv);
					}
				}
			}
		}
	}

	/**
	 * 计算医保分段和总额分摊情况(挂号不需要考虑辅医保)
	 * @param pvInsurances
	 * @param totalAmount
	 * @return
	 */
	public List<BlSettleDetail> countMedicare(List<PvInsurance> pvInsurances, BigDecimal totalAmount, String euPvtype, String pkSettle) {

		List<BlSettleDetail> blSettleDetails = new ArrayList<BlSettleDetail>();
		// 循环所有的医保计划
		BigDecimal totalAmountTemp = BigDecimal.ZERO;
		User user = UserContext.getUser();
		String pkOrg = user==null?"89ace0e12aba439991c0eb001aaf02f7":user.getPkOrg();
		for (PvInsurance pvInsurance : pvInsurances) {
			totalAmountTemp = priceStrategyService.qryPatiSettlementAmountAllocationInfo(totalAmount, pkOrg, euPvtype, pvInsurance.getPkHp());
			BigDecimal tempDouble = totalAmount.subtract(totalAmountTemp);
			totalAmount = totalAmountTemp;
			if (tempDouble.compareTo(BigDecimal.ZERO) != 0) {
				BlSettleDetail blSettleDetail = new BlSettleDetail();
				blSettleDetail.setPkSettle(pkSettle);
				BdHp hp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp=? ", BdHp.class, pvInsurance.getPkHp());
				blSettleDetail.setPkPayer(hp.getPkPayer());// 支付方
				blSettleDetail.setPkInsurance(pvInsurance.getPkHp());// 医保主键
				blSettleDetail.setAmount(tempDouble.doubleValue());
				ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
				blSettleDetails.add(blSettleDetail);
			}
		}
		return blSettleDetails;
	}
	
	/**
	 * 门诊退费预结算考虑医保的金额计算
	 * @param user
	 * @param opCgTransforVo
	 * @return
	 */
	public OpCgTransforVo returnInsComputSettle(IUser user, OpCgTransforVo opCgTransforVo)
	{
		OpCgTransforVo rgv = new OpCgTransforVo();
		BigDecimal aggregateAmount = BigDecimal.ZERO;	// 金额合计
		BigDecimal patientsPay = BigDecimal.ZERO;		// 患者自付
		BigDecimal discAmount = BigDecimal.ZERO;		// 优惠金额(计费时患者优惠)
		BigDecimal medicarePayments = BigDecimal.ZERO;	// 医保支付(计费以及结算时内部医保优惠)
		BigDecimal accountPrepaid = BigDecimal.ZERO;	// 账户已付
		BigDecimal amountPiTotal = BigDecimal.ZERO;		// 收费明细中患者支付
		
		opCgTransforVo.setCurDate(new Date());
		//部分退费的vo
		List<BlOpDt> blOpDts = opCgTransforVo.getBlOpDts();
		// 加工前台传过来的数据
		if (CollectionUtils.isEmpty(blOpDts)){
			throw new BusException("未传入需要记费的数据");
		}
		
		/**20210428退费重结预结算保持和收费预结算计费相同的逻辑处理**/
		Set<String> pkBlOpDt = new HashSet<String>();
		BlOpDt blOpDtFirst = blOpDts.get(0);
		String pkPv = blOpDtFirst.getPkPv();
		Map<String, Object> pvMap = DataBaseHelper.queryForMap("select pk_insu from PV_ENCOUNTER where pk_pv = ?", new Object[]{pkPv});
		String pkInsu =  CommonUtils.getString(pvMap.get("pkInsu"));
		// 2.判断是否是多次就诊一次结算,校验根据多个pkPv查询的辅医保是否一致
		Set<String> pkPvs = new HashSet<String>();
		pkPvs.add(pkPv);
		for (BlOpDt blOpDt : blOpDts) {
			pkBlOpDt.add(blOpDt.getPkCgop());
			if (!pkPv.equals(blOpDt.getPkPv())) {
				pkPvs.add(blOpDt.getPkPv());// 存在多次就诊
			}
			/*if (blOpDt.getPkInsu()!=null && !blOpDt.getPkInsu().equals(pkInsu)) {
				throw new BusException("该患者多次就诊使用的主医保不一致，不能一次性收费,请选择相同开立科室的进行结算");
			}*/
		}
		if (pkPvs.size() > 1) {
			String queryPvInsurance = "select 'pkPvhp' as PK_PVHP,PK_ORG,PK_HP,FLAG_MAJ from pv_insurance where pk_pv=? and pk_org=?";
			List<PvInsurance> pvInsurancesFirst = DataBaseHelper.queryForList(queryPvInsurance, PvInsurance.class, pkPv, ((User) user).getPkOrg());
			for (String pv : pkPvs) {
				List<PvInsurance> pvInsurances = DataBaseHelper.queryForList(queryPvInsurance, PvInsurance.class, pv, ((User) user).getPkOrg());
				if (!BlcgUtil.equalList(pvInsurancesFirst, pvInsurances)) {
					throw new BusException("该患者多次就诊使用的辅医保不一致，不能一次性收费,请选择相同开立科室的进行结算");
				}
			}
		}
		
		//查询GCP和科研记账付款方
		List<Map<String,Object>> payerList = DataBaseHelper.queryForList("SELECT pk_payer FROM BD_PAYER WHERE code IN ('05','06')", new Object[]{});
		List<String> payers = new ArrayList<String>();
		for(Map<String,Object> map : payerList){
			payers.add(map.get("pkPayer").toString());
		}
		
		// 3.查询主医保医保信息
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkHp", pkInsu);
		mapParam.put("pkPv", pkPv);
		BdHp bdHp = cgQryMaintainService.qryBdHpInfo(mapParam);
		if ((StringUtils.isEmpty(bdHp.getDtExthp()) || bdHp.getDtExthp().equals("00")) || 
				(opCgTransforVo.getAggregateAmount()==null || opCgTransforVo.getAggregateAmount().compareTo(new BigDecimal(0D))==0)) {// 没有第三方医保
			
			// 3.1查询是否按优惠总额减免
			BigDecimal amt_disc = BigDecimal.ZERO;
			Map<String, Object> pvDiscMap = getHpPayerDiscByPkPv(pkPv);
			if(pvDiscMap!=null && pvDiscMap.containsKey("amtDisc")){
				amt_disc = new BigDecimal(pvDiscMap.get("amtDisc").toString());
			}
			if(amt_disc.compareTo(BigDecimal.ZERO) > 0){
				// 优惠总额减免
				for (BlOpDt bpt : blOpDts) {
					aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(bpt.getAmount()));
				}
				if(amt_disc.compareTo(aggregateAmount) >= 0)
				{
					//优惠金额>=总金额时，优惠金额=总金额，患者自费金额=0
					discAmount = aggregateAmount;
					patientsPay = BigDecimal.ZERO;
				}else{
					//优惠金额<总金额时，优惠金额=优惠金额，患者自费金额=总金额-优惠金额
					discAmount = amt_disc;
					patientsPay = aggregateAmount.subtract(amt_disc).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
				rgv.setAggregateAmount(aggregateAmount); // 金额合计
				rgv.setAmtInsuThird(BigDecimal.ZERO);// 仅外部医保统筹支付
				rgv.setMedicarePayments(medicarePayments.add(discAmount)); // 医保支付  = 医保+优惠金额
				rgv.setPatientsPay(patientsPay); // 患者支付
				rgv.setAccountPrepaid(accountPrepaid); // 账户已付
			}else{
				/*
				 * TODO: 2021-03-18 lipz
				 * 3.2计划生育 - 定项目，但一定要录入所在镇区
				 * 必须是计划生育身份并且就诊表pk_contraception不为空，而且有优惠金额的才能享受优惠
				 * hp-门诊计划生育-31
				 */
				if("31".equals(bdHp.getCode())){
					BigDecimal amt_disc_jhsy = BigDecimal.ZERO;
					Map<String, Object> jhsyDiscMap = getJhsyPayerDiscByPv(pkPv);
					if(jhsyDiscMap!=null && jhsyDiscMap.containsKey("amtDisc")){
						amt_disc_jhsy = new BigDecimal(jhsyDiscMap.get("amtDisc").toString());
					}
					if(amt_disc_jhsy.compareTo(BigDecimal.ZERO) > 0){
						discAmount = amt_disc_jhsy;
					}else{
						discAmount = BigDecimal.ZERO;
					}
					for (BlOpDt bpt : blOpDts) {
						aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(bpt.getAmount()));
					}
					rgv.setAggregateAmount(aggregateAmount); // 金额合计
					rgv.setAmtInsuThird(BigDecimal.ZERO);// 仅外部医保统筹支付
					rgv.setMedicarePayments(discAmount); // 医保支付  = 医保+优惠金额
					patientsPay = aggregateAmount.subtract(discAmount);
					rgv.setPatientsPay(patientsPay); // 患者支付
					rgv.setAccountPrepaid(BigDecimal.ZERO); // 账户已付
				}else if("2000".equals(bdHp.getCode()) || "20".equals(bdHp.getCode()) || "24".equals(bdHp.getCode()) 
						|| "42".equals(bdHp.getCode()) || "25".equals(bdHp.getCode()) || "26".equals(bdHp.getCode())
						|| "27".equals(bdHp.getCode()) || "28".equals(bdHp.getCode())){	
					/*
					 *  全记账
					 *  儿童福利院\东区产检记账\五桂山产检记账\地贫产前诊断\新冠疫情记账\感染记账\科研记账\GCP记账
					 */
					for (BlOpDt bpt : blOpDts) {
						aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(bpt.getAmount()));
					}
					rgv.setAggregateAmount(aggregateAmount); // 金额合计
					rgv.setAmtInsuThird(BigDecimal.ZERO);// 仅外部医保统筹支付
					rgv.setMedicarePayments(aggregateAmount); // 医保支付  = 医保+优惠金额
					rgv.setPatientsPay(BigDecimal.ZERO); // 患者支付
					rgv.setAccountPrepaid(BigDecimal.ZERO); // 账户已付
				}else {
					// 按明细优惠
					for (BlOpDt blOpDt : blOpDts) {
						aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(blOpDt.getAmount())) ;
						amountPiTotal = amountPiTotal.add(BigDecimal.valueOf(blOpDt.getAmountPi()));
						// 2021-04-28 lipz : 只要有优惠付款方即可享受优惠
						if(blOpDt.getPkPayer()!=null && StringUtils.isNotEmpty(blOpDt.getPkPayer().trim())){
							medicarePayments = medicarePayments.add(BigDecimal.valueOf(blOpDt.getAmount()).subtract(BigDecimal.valueOf(blOpDt.getAmountPi())));
						}
						// 未结算，但是已经支付的费用（未打发票）
						if ((EnumerateParameter.ONE.equals(blOpDt.getFlagAcc())) && (EnumerateParameter.ZERO.equals(blOpDt.getFlagSettle()))) {
							accountPrepaid = accountPrepaid.add(BigDecimal.valueOf(blOpDt.getAmountPi()));
						}
					}
					rgv.setAggregateAmount(aggregateAmount); // 金额合计
					rgv.setAmtInsuThird(BigDecimal.ZERO);// 仅外部医保统筹支付
					rgv.setMedicarePayments(medicarePayments.add(discAmount)); // 医保支付  = 医保+优惠金额
					patientsPay = aggregateAmount.subtract(medicarePayments).subtract(discAmount);
					rgv.setPatientsPay(patientsPay); // 患者支付
					rgv.setAccountPrepaid(accountPrepaid); // 账户已付
				}
			}
		} else {// 第三方医保(医保上传明细时去掉了加收；过滤了amount和amountPi不相等的部分)
			BigDecimal addAmount = BigDecimal.ZERO;//加收合计
			for (BlOpDt blOpDt : blOpDts) {
				addAmount = addAmount.add(BigDecimal.valueOf(blOpDt.getAmountAdd()));
				aggregateAmount = aggregateAmount.add(BigDecimal.valueOf(blOpDt.getAmount()));
				// 2021-04-28 lipz : 只要有优惠付款方即可享受优惠
				if((blOpDt.getPkPayer()!=null && StringUtils.isNotEmpty(blOpDt.getPkPayer().trim())) 
						|| BigDecimal.valueOf(blOpDt.getAmount()).compareTo(BigDecimal.valueOf(blOpDt.getAmountPi()))==1){
					discAmount = discAmount.add(BigDecimal.valueOf(blOpDt.getAmount()).subtract(BigDecimal.valueOf(blOpDt.getAmountPi())));
					amountPiTotal = amountPiTotal.add(BigDecimal.valueOf(blOpDt.getAmountPi()));
				}
				//未结算但是已经支付的费用（未打发票）
				if ((EnumerateParameter.ONE.equals(blOpDt.getFlagAcc())) && (EnumerateParameter.ZERO.equals(blOpDt.getFlagSettle()))) {
					accountPrepaid = accountPrepaid.add(BigDecimal.valueOf(blOpDt.getAmountPi()));
				}
			}

			/* 以下处理方式：避免上传医保时计算的金额有一分钱之类的出入  */
			rgv.setAggregateAmount(aggregateAmount);//医保总金额 = 重新取费用总额
			if(aggregateAmount.compareTo(opCgTransforVo.getAmtInsuThird()) < 0){//如果统筹大于本地总额，设置统筹等于本地总额
				opCgTransforVo.setAmtInsuThird(aggregateAmount);
			}
			rgv.setAmtInsuThird(opCgTransforVo.getAmtInsuThird());// 仅外部医保统筹支付
			rgv.setMedicarePayments(opCgTransforVo.getAmtInsuThird().add(discAmount));//医保支付 = 医保统筹 + 院内优惠金额
			rgv.setPatientsPay(aggregateAmount.subtract(rgv.getMedicarePayments()));// 患者自费金额 = 费用总额 - 医保支付
			rgv.setAccountPrepaid(accountPrepaid);//账户已付
		}
		return rgv;
	}
	
	/**
     * 根据医保或患者分类更新患者费用明细
     * @param pkPv
     * @param pkHp
     * @param pkPicate
     * @param oldPkInsu 患者原主医保
     */
	public void updateBlOpDtCgRate(String pkPv,String pkHp,String pkPicate,String oldPkInsu,String oldPkPicate){
		Map<String,Object> rtn = CgProcessUtils.updateOpCg(pkPv, pkHp, pkPicate,oldPkInsu,oldPkPicate);;
		if(rtn!=null&&"true".equals(rtn.get("enable"))){
			return;
		}
		//1.医保不为空获取医保信息
		//2.患者分类不为空获取患者分类信息
		//3.查询患者对应未结算费用明细
		//4.依次调用记费策略设置自费比例、优惠比例及合计金额（不再处理单价）
		
	}
	
	/**
	 * 根据就诊主键获取医保主计划、付款方、患者分类优惠金额
	 * 用于总额减免
	 * @param pkPv
	 * @return
	 */
	private Map<String, Object> getHpPayerDiscByPkPv(String pkPv){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT hp.pk_hp, hp.code code_hp, hp.name name_hp, ISNULL(hp.DT_EXTHP, '') DT_EXTHP, ");
		sql.append(" ISNULL(cate.pk_payer, hp.PK_PAYER) pk_payer, ISNULL(pay.NAME, '无') name_payer, ISNULL(cate.AMOUNT_DISC, 0) amt_disc");
		sql.append(" FROM PV_ENCOUNTER pv ");
		sql.append(" INNER JOIN BD_HP hp ON hp.PK_HP = pv.PK_INSU ");
		sql.append(" LEFT JOIN PI_CATE cate ON cate.PK_PICATE = pv.PK_PICATE ");
		sql.append(" LEFT JOIN BD_PAYER pay ON pay.PK_PAYER = cate.PK_PAYER  ");
		sql.append(" WHERE pv.pk_pv=? ");
		return DataBaseHelper.queryForMap(sql.toString(), pkPv);
	}
	
	/**
	 * 根据就诊主键获取医保主计划、付款方、优惠金额
	 * 用于计划生育-定项目套餐优惠
	 * @param pkPv
	 * @return
	 */
	private Map<String, Object> getJhsyPayerDiscByPv(String pkPv){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT inspv.pk_hp, ico.pk_payer, ISNULL(ico.amount, 0) amt_disc ");
		sql.append(" FROM ins_qgyb_pv inspv ");
		sql.append(" LEFT JOIN ins_contraception_operation ico on ico.pk_contraception = inspv.pk_contraception ");
		sql.append(" WHERE inspv.pk_pv=? ");
		return DataBaseHelper.queryForMap(sql.toString(), pkPv);
	}
	
	public boolean checkItemBlStatus(List<BlOpDt> blOpDts){
		boolean blStatus = true;
		String sql = "select pk_cgop, flag_settle, amount, amount_pi from bl_op_dt where pk_cgop=? and del_flag='0'";
		//遍历每一笔未结算清单记录
		for (BlOpDt blOpDt : blOpDts) {
			BlOpDt dbaItem = DataBaseHelper.queryForBean(sql, BlOpDt.class, new Object[]{blOpDt.getPkCgop()});
			if(dbaItem!=null){
				//DataBaseHelper.execute("update bl_op_dt set del_flag='0' where pk_cgop=?", blOpDt.getPkCgop());//将当前收费的计费明细删除标记改为0
				//是否未结算
				boolean settStatus = dbaItem.getFlagSettle().equals(EnumerateParameter.ONE) ? true : false; 
				//总金额是否相等
				boolean amtStatus = new BigDecimal(dbaItem.getAmount().toString()).compareTo(new BigDecimal(blOpDt.getAmount().toString()))==0; 
				//自费金额是否相等
				boolean amtPitatus = new BigDecimal(dbaItem.getAmountPi().toString()).compareTo(new BigDecimal(blOpDt.getAmountPi().toString()))==0; 
				if(!settStatus || !amtStatus || !amtPitatus){
					blStatus = false;//只要有一条数据金额不一致都需要前端刷新数据后重新结算
					break;
				}
			}else{
				blStatus = false;//当前遍历的计费明细找不到也阻止结算
				break;
			}
		}
		return blStatus;
	} 
	
	/**
	 * 重点保健对象，在开始和结束时间范围内有效。
	 * @param pkPi
	 * @return
	 */
	private Map<String, Object> getQgybIdetInfo(String pkPi){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT TOP 1 im.PSN_NAME, im.CERTNO, idet.PSN_IDET_TYPE, ");
		sql.append(" CONVERT(VARCHAR(19), idet.BEGNTIME,120) BEGNT_IME, CONVERT(VARCHAR(19), idet.ENDTIME,120) END_IME ");
		sql.append(" FROM INS_QGYB_MASTER im ");
		sql.append(" LEFT JOIN INS_QGYB_IDET idet on idet.PK_INSUPI=im.PK_INSUPI ");
		sql.append(" WHERE ");
		sql.append(" im.PK_PI=? and idet.PSN_IDET_TYPE in ('7001','7002') ");
		sql.append(" and idet.BEGNTIME<=GETDATE() and idet.ENDTIME>=GETDATE() ");
		return DataBaseHelper.queryForMap(sql.toString(), pkPi);
	}
	/**
	 * 根据级别获取重点保健对象的报销比例
	 * @param idetType	级别
	 * @return
	 */
	private Map<String, Object> getZdbjBl(String idetType){
		String sql = "select NAME from BD_DEFDOC where CODE_DEFDOCLIST='BAST001' and CODE=?";
		return DataBaseHelper.queryForMap(sql, idetType);
	}

}
