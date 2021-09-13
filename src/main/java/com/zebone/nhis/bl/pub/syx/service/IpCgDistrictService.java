package com.zebone.nhis.bl.pub.syx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.syx.dao.CgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.support.CgProcessHandler;
import com.zebone.nhis.bl.pub.syx.vo.HpVo;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyBl;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CnPiPubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.service.QueryRemainFeeService;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 广州区公医记费服务
 * @author 
 *
 */
@Service
public class IpCgDistrictService {
	
	@Resource
	private CgStrategyPubService cgStrategyPubService;
	@Resource
	private CgStrategyPubMapper cgStrategyPubMapper;
	@Resource
	private CgProcessHandler cgProcessHandler;
	@Resource
	private QueryRemainFeeService queryFeeServcie;
	
	/**
	 * 住院批量记费
	 * @param blCgPubParamVos
	 * @return
	 * @throws BusException
	 */
	public  BlPubReturnVo chargeIpBatch(List<BlPubParamVo> blCgPubParamVos,boolean isAllowQF,Logger logger)
			throws BusException{
		
		//数据校验已在公共接口中实现，此处不再添加校验
		BlPubReturnVo res = new BlPubReturnVo();
		String codeCg = ApplicationUtils.getCode("0602");
		//以患者为单位，进行批量记费 key:pkPv,value：{key:是否药品，记费信息list}
		Map<String,Map<String,List<ItemPriceVo>>> pvMap = new HashMap<String,Map<String,List<ItemPriceVo>>>(16);
		//就诊主键集合，批量查询本次记费所有患者就诊信息
		Set<String> pkPvSet = new HashSet<String>();
		//1.以患者为单位进行分组
		for(BlPubParamVo blvo:blCgPubParamVos){
			pkPvSet.add(blvo.getPkPv());
			boolean hasFlag = false;
			for(Map.Entry<String, Map<String,List<ItemPriceVo>>> entry:pvMap.entrySet()){
				 if(blvo.getPkPv().equals(entry.getKey())){
					 Map<String,List<ItemPriceVo>> itemMap = entry.getValue();
					 if(itemMap==null){
						 itemMap = new HashMap<String,List<ItemPriceVo>>();
					 }
					//药品不取价格
					if(EnumerateParameter.ONE.equals(blvo.getFlagPd())){
						List<ItemPriceVo> pdlist = itemMap.get("pd");
						if(pdlist==null){
							pdlist = new ArrayList<ItemPriceVo>();
						}
						pdlist.add(cgProcessHandler.constructParam(blvo));
						itemMap.put("pd", pdlist);
					}else{
						List<ItemPriceVo> itemlist = itemMap.get("item");
						if(itemlist==null){
							itemlist = new ArrayList<ItemPriceVo>();
						}
						itemlist.add(cgProcessHandler.constructParam(blvo));
						itemMap.put("item", itemlist);
					}
					pvMap.put(entry.getKey(), itemMap);
					hasFlag = true;
					break;
				 }
		      }
			//未匹配到存在的记录
			if(!hasFlag){
				List<ItemPriceVo> itemlist = new ArrayList<ItemPriceVo>();
				itemlist.add(cgProcessHandler.constructParam(blvo));
				Map<String,List<ItemPriceVo>> itemMap = new HashMap<String,List<ItemPriceVo>>(16);
				if(EnumerateParameter.ONE.equals(blvo.getFlagPd())){
					itemMap.put("pd", itemlist);
				}else{
					itemMap.put("item", itemlist);
				}
				
				pvMap.put(blvo.getPkPv(), itemMap);
			}
		}
		//取当前机构所有医保及关联的优惠信息,避免每个患者循环查询一次数据库
		Map<String,Object> paramMap = new HashMap<String,Object>(16);
		paramMap.put("euPvType", "3");//默认只查询住院使用的医保
		//取所有医保信息
		List<HpVo> hplist = cgStrategyPubMapper.queryHpList(paramMap);
		//取本次记费所有就诊信息
		List<PvEncounter> pvlist = cgStrategyPubMapper.queryPvList(new ArrayList<String>(pkPvSet));
		//最终记费明细集合
		List<BlIpDt> dtlist = new ArrayList<BlIpDt>();
		//公医计费明细集合
		List<InsGzgyBl> gyBlList = new ArrayList<>();
		//记费明细排序号(一次计费服务，共用序列从1开始，每个明细加1.（不分患者）)
		int cgSortNo = 1;
		//2.取价格并设置主医保及优惠内容
		for(Map.Entry<String, Map<String,List<ItemPriceVo>>> entry:pvMap.entrySet()){
			String pkPv = entry.getKey();
			for(PvEncounter pv:pvlist){
				if(pv.getPkPv().equals(pkPv)){
					//记费前校验此患者就诊状态是否可以记费，不满足记费条件则略过此患者记费。
					if(!cgStrategyPubService.checkPvStatus(pkPv,logger))
						continue;
					
					//2.2获取医保和优惠信息
					Map<String,List<ItemPriceVo>> itemMap = entry.getValue();
					if(itemMap == null)
						break;
					HpVo mainHp = null;
					BdHp cateHp = null; 
					for(HpVo hp:hplist){
						if(pv.getPkInsu().equals(hp.getPkHp())){
							/**获取当前医保扩展属性值*/
							String valAttr = cgStrategyPubMapper.qryHpValAttr(hp.getPkHp());
							hp.setValAttr(valAttr);
							
							mainHp = hp;
						}
						if(pv.getPkPicate().equals(hp.getPkPicate())){
							cateHp = hp;
						}
					}
					if(mainHp == null)
						throw new BusException("调用住院记费时，未获取到患者【"+pv.getNamePi()+"】的主医保信息！");
					List<ItemPriceVo> pdlist = itemMap.get("pd");
					List<ItemPriceVo> itemlist = itemMap.get("item");
					List<ItemPriceVo> pricelist = new ArrayList<ItemPriceVo>();
					BigDecimal  age = new BigDecimal(0);
					if(!CommonUtils.isEmptyString(pv.getPkPi())){
						age = CnPiPubService.getPvAge(pv.getPkPi());//计算患者年龄
					}
					//2.3根据价格策略取价格
					if(itemlist!=null&&itemlist.size()>0){
						pricelist = cgStrategyPubService.getItemAndOrdPrice(mainHp, pv.getPkOrg(), new Date(), itemlist,pv,age);
					}
					
					//检验医嘱特殊处理
					if((itemlist!=null&&itemlist.size()>0)
						&& (pricelist!=null&&pricelist.size()>=0)){
						pricelist = cgStrategyPubService.filterOrdItem(pricelist,itemlist); 	
					}
					
					//检查医嘱特殊处理
					if((itemlist!=null&&itemlist.size()>0)
							&& (pricelist!=null&&pricelist.size()>=0)){ 
						cgStrategyPubService.filterExOrdItem(pricelist);  
						
						//如果检查医嘱下有药品项目，则合并符合条件的药品项目
						if(pdlist!=null && pdlist.size()>0){
							cgStrategyPubService.filterExOrdItem(pdlist);  
						}
					}
					
					//计费前校验收费项目数量是否超过单日最大记费数量
					if((itemlist!=null&&itemlist.size()>0)
							&& (pricelist!=null&&pricelist.size()>0)){
						cgStrategyPubService.checkItemCg(pricelist,pv);
					}
					
					//2.4合并物品与收费项目
					if(pdlist!=null&&pdlist.size()>0){
						pricelist.addAll(pdlist);
					}
					if(pricelist==null||pricelist.size()<=0){
						break;
					}
					//2.5调用收费项目记费策略 --废弃
					//pricelist = cgStrategyPubService.getItemPriceByChap(pv, pv.getPkOrg(), pricelist);
					
					//记费前查询是否有床位费，如果有床位费则校验是否在费用发生日期当天记过费，记过费给出提示不可重复记费。
			    	cgStrategyPubService.qryBedItemIsCharge(pv,pricelist);
					
					/**当医保扩展属性‘0301’为1,调用广州公医策略*/
					if("1".equals(mainHp.getValAttr())){
						pricelist = cgStrategyPubService.gzGyDistCharge(mainHp, pv, pricelist,true);	//调用公医医保记费策略
					}else{
						pricelist = cgStrategyPubService.getItemPriceByCgDiv(mainHp, pv, pricelist);	//调用医保记费策略
					}
					
					//2.7调用优惠记费策略
					if(cateHp!=null){
						pricelist = cgStrategyPubService.getItemDiscRatioByCgDiv(cateHp, pv, pricelist);
					}
					//2.8添加至最终记费集合
					Map<String,Object> map = cgProcessHandler.constructBlIpDt(codeCg,pricelist,pv,cgSortNo,age);
					BigDecimal totalAmt = BigDecimal.ZERO;
					if(map!=null&&map.get("dtlist")!=null){ 	
						dtlist.addAll((List<BlIpDt>)map.get("dtlist"));
						totalAmt = (BigDecimal)map.get("curAmt");
						
						/**当医保扩展属性‘0301’为1,组装ins_gzgy_bl信息*/
						if("1".equals(mainHp.getValAttr())){
							gyBlList.addAll(cgProcessHandler.constructInsGzgyBl((List<BlIpDt>)map.get("dtlist"),pv));	////2.9组装计费信息到公医计费明细
						}
					}
					
					//2.8校验患者是否欠费
					if(!isAllowQF && QueryRemainFeeService.isControlFee()){ 
						if(!queryFeeServcie.isArrearage(pkPv, pv.getPkInsu(),totalAmt)){
							throw new BusException("患者【"+pv.getNamePi()+"】已欠费，无法完成本次记费!");
						}
					}
				}
			}
		}
		if(dtlist!=null&&dtlist.size()>0){
			//3.批量写入记费表
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class),
					dtlist);
			
			if(gyBlList!=null && gyBlList.size()>0)
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsGzgyBl.class),	////4.批量写入公医计费明细
					gyBlList);
					
			//4.处理返回。
			res.setBids(dtlist);
		}
		return res;
	}
	
	/**
	 * 修改计费信息
	 * @param pv
	 * @param mainHp
	 * @param cateHp
	 * @param dtList
	 */
	public void updateIpDt(PvEncounter pv,HpVo mainHp,BdHp cateHp,List<BlIpDt> dtList){
		
		
		
	}
	
	
	
	
	/**
	 * 更新bl_ip_dt表，如果患者主医保为广州公医医保则一并更新ins_gzgy_bl
	 * @param pv
	 * @param mainHp
	 * @param cateHp
	 * @param itemList
	 * @param flagPrice (是否需要限价审批)
	 * @param oldPkInsu(患者改之前的主医保计划)
	 * @param oldPkPicate (患者改之前的优惠主键)
	 */
	public void updateChargeIp(PvEncounter pv,HpVo mainHp,BdHp cateHp,List<BlIpDt> ipDtList,Boolean flagPriceAvrl,String oldPkInsu,String oldPkPicate){
		//获取参数Pv0018
		String isUpdateCg = ApplicationUtils.getSysparam("PV0018", false);
		
		//组装结算明细信息到ItemPriceVo
		List<ItemPriceVo> pricelist = assItemPriceInfo(pv,ipDtList,oldPkPicate,isUpdateCg);
		
		if(mainHp!=null){
			//当医保扩展属性‘0301’为1，且BL0023参数启用时，调用广州公医策略
			//判断是否启用广州公医
			if("1".equals(ApplicationUtils.getSysparam("BL0023", false)) &&
					"1".equals(mainHp.getValAttr())){
				pricelist = cgStrategyPubService.gzGyDistCharge(mainHp, pv, pricelist,flagPriceAvrl);	//调用公医医保记费策略
			} else {
				//查询医嘱项目科研医嘱标志信息
				List<CnOrder> cnordList = cgStrategyPubMapper.qryCnorderByPkPv(pv.getPkPv());
				if(cnordList!=null && cnordList.size()>0){
					for(CnOrder ordVo : cnordList){
						for(ItemPriceVo priceVo : pricelist){
							if(!CommonUtils.isEmptyString(priceVo.getPkCnord())
								&& priceVo.getPkCnord().equals(ordVo.getPkCnord())){
								priceVo.setEuOrdtype(ordVo.getEuOrdtype());
							}
						}
					}
				}

				//校验是否有科研医嘱，科研医嘱不调用医保计费策略
				Map<String,List<ItemPriceVo>>  typeMap = cgStrategyPubService.filterEuOrdtype(pricelist);
				if(typeMap!=null){
					//非科研医嘱
					pricelist = typeMap.get("fky");
					if(pricelist!=null && pricelist.size()>0){
						//2.6调用医保记费策略
						pricelist = cgStrategyPubService.getItemPriceByCgDiv(mainHp, pv, pricelist);	//调用医保记费策略
					}else{
						pricelist = new ArrayList<>();
					}

					//科研医嘱(不走医保记费策略)
					List<ItemPriceVo> kypricelist = typeMap.get("ky");
					if(kypricelist!=null&&kypricelist.size()>0){
						pricelist.addAll(kypricelist);
					}
				}
			}
		}
		
		//Pv0018参数值!=1时，修改患者分类不调用优惠策略
		if(!CommonUtils.isEmptyString(isUpdateCg) &&  EnumerateParameter.ONE.equals(isUpdateCg)){
			if(cateHp!=null)
				pricelist = cgStrategyPubService.getItemDiscRatioByCgDiv(cateHp, pv, pricelist);	//调用优惠策略
		}
		//组装计费明细信息到bl_Ip_dt
		List<BlIpDt> newDtList = assBlIpDt(ipDtList,pricelist);
		//更新bl_ip_dt表
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlIpDt.class),newDtList);
		
		String oldHpAttr = "";
		//修改住院患者主医保信息，需要获取到原主医保的扩展属性
		if(!CommonUtils.isEmptyString(oldPkInsu))
			oldHpAttr = cgStrategyPubMapper.qryHpValAttr(oldPkInsu);
		//当医保扩展属性‘0301’为1，且BL0023参数启用时,更新ins_gzgy_bl表
		if("1".equals(oldHpAttr) || 
				"1".equals(mainHp.getValAttr())){
			//根据就诊信息和计费信息查询ins_gzgy_bl表
			List<String> pkCgips = new ArrayList<>();
			for(BlIpDt ipdt:ipDtList){	//获取pkCgip
				pkCgips.add(ipdt.getPkCgip());
			}
			
			List<InsGzgyBl> gyBlList = cgStrategyPubMapper.qryGzgyBlList(pv.getPkPv(), pkCgips);
			List<InsGzgyBl> newGyBlList = new ArrayList<>();
			//如果查出公医计费明细更新ins_gzgy_bl,否则新增ins_gzgy_bl
			if(gyBlList!=null && gyBlList.size()>0){
				//组装公医计费明细
				newGyBlList = assGzgyBl(newDtList, gyBlList);
				//更新ins_gzgy_bl
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(InsGzgyBl.class),newGyBlList);
			} else {
				//组装计费信息到公医计费明细
				newGyBlList = cgProcessHandler.constructInsGzgyBl(newDtList,pv);
				//新增ins_gzgy_bl
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsGzgyBl.class),newGyBlList);
			}
		}
	}
	
	/**
	 * 组装收费明细信息到ItempriceVo
	 * @param ipDtList
	 * @return
	 */
	private List<ItemPriceVo> assItemPriceInfo(PvEncounter pv,List<BlIpDt> ipDtList,String oldPkPicate,String isUpdateCg){
		List<ItemPriceVo> priceList = new ArrayList<>();
		//获取患者修改之前是否是特诊患者
//		Map<String,Object> retMap = DataBaseHelper.queryForMap("select flag_spec from pi_cate where pk_picate = ?",
//				oldPkPicate);
		
//		String oldFlagSpec = "";
//		if(retMap!=null && retMap.size()>0 && retMap.get("flagSpec")!=null)
//			oldFlagSpec = CommonUtils.getString(retMap.get("flagSpec"));
			
		//获取收费项目特诊加价比例
//		List<Map<String,Object>> rsList = new ArrayList<>();
//		//Pv0018参数值!=1时，修改患者分类时不重新获取已记费项目的特诊加收比例
//		if(!CommonUtils.isEmptyString(isUpdateCg) &&  EnumerateParameter.ONE.equals(isUpdateCg)){
//			Set<String> pkItems = new HashSet<>();
//			for(BlIpDt ipdt:ipDtList){
//				pkItems.add(ipdt.getPkItem());
//			}
//			rsList =  cgStrategyPubMapper.qryRatioSpecList(pv.getEuPvtype(),pkItems);
//		}
		//查询vip病区主键
//		List<Map<String,Object>> vipDeptList = DataBaseHelper.queryForList(
//				"select ag.pk_dept  from bd_res_pc_argu ag where ag.code_argu='EX0047' and  ag.arguval='1'"
//				,new Object[]{});
		for(BlIpDt ipDt : ipDtList){
			ItemPriceVo item = new ItemPriceVo();
			item.setPkCgip(ipDt.getPkCgip());
			item.setPkOrdexdt(ipDt.getPkOrdexdt());
			item.setPkPv(ipDt.getPkPv());
			item.setPkPi(ipDt.getPkPi());
			item.setPkOrgApp(ipDt.getPkOrgApp());
			item.setPkDeptApp(ipDt.getPkDeptApp());
			item.setPkOrgEx(ipDt.getPkOrgEx());
			item.setDateHap(ipDt.getDateHap());
			item.setPkUnit(ipDt.getPkUnit());
			item.setPkUnitPd(ipDt.getPkUnitPd());
			item.setFlagPd(ipDt.getFlagPd());
			if(BlcgUtil.converToTrueOrFalse(ipDt.getFlagPd()))
			{
				item.setPkOrdOld(ipDt.getPkPd());
				item.setPkItem(ipDt.getPkPd());
			}	
			else
			{
				item.setPkItemOld(ipDt.getPkItem());
				item.setPkItem(ipDt.getPkItem());
			}	
			item.setPrice(ipDt.getPrice());
			item.setPriceCs(ipDt.getPrice());
			item.setPriceCost(ipDt.getPriceCost());
			item.setPkItemcate(ipDt.getPkItemcate());
			item.setPkPres(ipDt.getPkPres());
			item.setPkCnord(ipDt.getPkCnord());
			item.setQuan(ipDt.getQuan());
			item.setPriceOrg(ipDt.getPriceOrg());
			item.setPkDisc(ipDt.getPkDisc());
			item.setRatioDisc(ipDt.getRatioDisc());
			item.setRatioSelf(ipDt.getRatioSelf());
			item.setPkItemcate(ipDt.getPkItemcate());
			item.setSpec(ipDt.getSpec());
			item.setNameCg(ipDt.getNameCg());
			item.setName(ipDt.getNameCg());
			item.setPkCnord(ipDt.getPkCnord());
			item.setRatioSpec(ipDt.getRatioAdd());
						
			/**修改患者分类无需更改特诊加收 	20-05-26注释此逻辑*/
			//项目加收比例
//			if(rsList!=null && rsList.size()>0){
//				for(Map<String,Object> map : rsList){
//					if(map.get("pkItem")!=null && 
//							ipDt.getPkItem().equals(map.get("pkItem"))){
//						//如果修改之前的患者为特诊，则pricecs字段减去特诊加收费用
//						if(BlcgUtil.converToTrueOrFalse(oldFlagSpec)){
//							item.setPriceCs(MathUtils.sub(item.getPriceCs(), MathUtils.mul(item.getPriceOrg(), CommonUtils.getDoubleObject(map.get("ratioSpec")))));
//							ipDt.setRatioAdd(0D);
//						}
//							
//						//如果修改后的患者为特诊，则price_cs字段加上特诊加收费用
//						if(BlcgUtil.converToTrueOrFalse(pv.getFlagSpec()) && qryVipDtptByItem(vipDeptList,ipDt.getPkDeptApp())){
//							Double price = MathUtils.div(MathUtils.sub(ipDt.getAmount(), ipDt.getAmountAdd()), ipDt.getQuan(),6);
//							item.setPriceCs(MathUtils.add(price, MathUtils.mul(item.getPriceOrg(), CommonUtils.getDoubleObject(map.get("ratioSpec")))));
//							item.setRatioSpec(CommonUtils.getDoubleObject(map.get("ratioSpec")));
//						} else {
//							item.setRatioSpec(ipDt.getRatioAdd());
//						}
//						
//						if(item.getPriceCs()<0)
//							item.setPriceCs(item.getPrice());
//						
//						break;
//					}
//				}
//			} else {
//				item.setRatioSpec(ipDt.getRatioAdd());
//			}
			
			priceList.add(item);
		}
		return priceList;
	}
	
	/**
	 * 查询科室是否属于vip科室
	 * @param deptList
	 * @param pkDept
	 * @return
	 */
	private boolean qryVipDtptByItem(List<Map<String,Object>> deptList,String pkDept){
		boolean flag = false;
		if(deptList!=null && deptList.size()>0){
			for(Map<String,Object> map : deptList){
				//如果返回值等于记费病区，按vip逻辑记费
				if((map!=null && map.size()>0) 
						&& (map.get("pkDept")!=null && CommonUtils.getString(map.get("pkDept")).equals(pkDept))){
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 组装信息到bl_ip_dt
	 * @param blDtList
	 * @param priceList
	 * @return
	 */
	private List<BlIpDt> assBlIpDt(List<BlIpDt> blDtList,List<ItemPriceVo> priceList){
		//把重新计算的信息组装到bl_ip_dt
		for(BlIpDt ipDt : blDtList){
			for(ItemPriceVo item : priceList){
				
				if(ipDt.getPkCgip().equals(item.getPkCgip())){
					
					ipDt.setPrice(item.getPrice());			//单价
					ipDt.setRatioDisc(item.getRatioDisc()); //优惠比例
					ipDt.setRatioSelf(item.getRatioSelf());	//自费比例
					ipDt.setRatioAdd(item.getRatioSpec()!=null?item.getRatioSpec():0D);	//加收比例
					//计算收费项目加收后的单价
					if(ipDt.getAmountAdd()!=null && MathUtils.compareTo(ipDt.getAmountAdd(), 0D)!=0){
						//计算收费项目加收后的单价
						Double newPrice = MathUtils.add(item.getPriceOrg(), MathUtils.div(ipDt.getAmountAdd(), ipDt.getQuan(), 6));
						ipDt.setPrice(newPrice);			//单价
					}
					
					//计算收费项目加收后的单价
					//Double newPriceOrg = MathUtils.mul(ipDt.getPriceOrg(), MathUtils.add(1D, ipDt.getRatioAdd()));
					//特诊加收金额 price_org*quan*ratio_add
					//ipDt.setAmountAdd(MathUtils.mul(MathUtils.mul(ipDt.getPriceOrg(),ipDt.getQuan()), ipDt.getRatioAdd()));
					//amount，金额，price_org*quan+amount_add
					ipDt.setAmount(MathUtils.mul(ipDt.getQuan(), ipDt.getPrice()));
					//amount_hppi，患者支付的医保金额，price*quan*ratio_self；
					if("1".equals(item.getFlagHppi())){
						ipDt.setAmountHppi(item.getAmtHppi().doubleValue());
					}else{
						ipDt.setAmountHppi(MathUtils.mul(MathUtils.mul(ipDt.getPrice(), ipDt.getQuan()),ipDt.getRatioSelf()));
					}
		            Double amt = MathUtils.sub(ipDt.getAmountHppi(), MathUtils.mul(MathUtils.mul(ipDt.getPrice(), MathUtils.sub(1D, ipDt.getRatioDisc())), ipDt.getQuan()));
		            ipDt.setAmountPi(amt);
		            
		            //校验ratio_disc和ratio_self为1时，amount和amount_pi和amount_hppi是否相等，不相等时以amount_pi的值为准
					if(MathUtils.compareTo(ipDt.getRatioDisc(), 1D)==0 &&
							MathUtils.compareTo(ipDt.getRatioSelf(), 1D)==0 &&
							MathUtils.compareTo(ipDt.getAmount(), ipDt.getAmountPi())!=0){
						ipDt.setAmount(ipDt.getAmountPi());
						ipDt.setAmountHppi(ipDt.getAmountPi());
					}
		            
		            break;
				}
				
			}
		}
		return blDtList;
	}
	
	/**
	 * 组装信息到ins_gzgy_bl
	 * @param blDtList
	 * @param priceList
	 * @return
	 */
	public List<InsGzgyBl> assGzgyBl(List<BlIpDt> blDtList,List<InsGzgyBl> gyBlList){
		//把重新计算的信息组装到ins_gzgy_bl
		for(InsGzgyBl gyBl : gyBlList){
			for(BlIpDt ipDt : blDtList){
				
				if(gyBl.getPkCg().equals(ipDt.getPkCgip())){
					gyBl.setPrice(ipDt.getPrice()); 	//单价
					gyBl.setQuan(ipDt.getQuan()); 		//数量
					gyBl.setAmount(ipDt.getAmount()); 	//总金额
					gyBl.setRatio(ipDt.getRatioSelf());			//患者自付比例
					gyBl.setAmountPi(ipDt.getAmountHppi()); 	//患者自付金额
					gyBl.setAmountHp(MathUtils.sub(gyBl.getAmount(), gyBl.getAmountPi()));		//医保支付金额
					
					break;
				}
				
			}
		}
		return gyBlList;
	}
}
