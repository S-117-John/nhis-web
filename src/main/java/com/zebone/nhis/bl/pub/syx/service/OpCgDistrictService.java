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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.MapUtils;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.syx.dao.CgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.support.CgProcessHandler;
import com.zebone.nhis.bl.pub.syx.vo.HpVo;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlCgPdParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyBl;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CnPiPubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 门诊记费--公医记费接口
 * @author c
 *
 */
@Service
public class OpCgDistrictService {
	
	@Resource
	private CgStrategyPubService cgStrategyPubService;
	@Resource
	private CgStrategyPubMapper cgStrategyPubMapper;
	@Resource
	private CgProcessHandler cgProcessHandler;
	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	@Autowired
	private OgCgStrategyPubService ogCgStrategyPubService;
	
	private Logger logger = LoggerFactory.getLogger("com.zebone");
	
	public  BlPubReturnVo chargeOpBatch(List<BlPubParamVo> blOpCgPubParamVos)
			throws BusException{
		String codeCg = ApplicationUtils.getCode("0601");
		Map<String, Object> mapParam = new HashMap<String, Object>();
		//取患者就诊属性
		String pkPv =  blOpCgPubParamVos.get(0).getPkPv();
		String pkOrg = blOpCgPubParamVos.get(0).getPkOrg(); 
		if(CommonUtils.isEmptyString(pkPv))
			throw new BusException("调用门诊记费方法时，参数中未传入pkPv的值！");
		mapParam.put("pkPv", pkPv);
		mapParam.put("pkOrg", pkOrg);
		PvEncounter pvVo = cgQryMaintainService.qryPvEncounterInfo(mapParam);
		mapParam.put("pkHp", pvVo.getPkInsu());
		//BdHp mainHp = cgQryMaintainService.qryBdHpInfo(mapParam);
		HpVo mainHp = null;
		List<HpVo> mainHpList = cgStrategyPubMapper.queryHpList(mapParam);
		if(mainHpList==null || mainHpList.size()<=0)
			throw new BusException("未找到患者本次就诊对应的主医保信息！");
		else
			mainHp = mainHpList.get(0);
		
		/**获取当前医保扩展属性值*/
		String valAttr = cgStrategyPubMapper.qryHpValAttr(mainHp.getPkHp());
		mainHp.setValAttr(valAttr);
		
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
		//医嘱及收费项目集合
		List<ItemPriceVo> ordItemAllList = new ArrayList<ItemPriceVo>();
		//公医计费明细集合
		List<InsGzgyBl> gyBlList = new ArrayList<>();
		
		//获取诊区
		String pkDeptAreas = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept_area From pv_encounter where pk_pv=?", pkPv), "pkDeptArea");
		for(BlPubParamVo blOpCgPubParamVo : blOpCgPubParamVos){
			if(StringUtils.isBlank(blOpCgPubParamVo.getPkDeptAreaapp()) && StringUtils.isNotBlank(pkDeptAreas)) {
				blOpCgPubParamVo.setPkDeptAreaapp(pkDeptAreas);
			}
			if(!pkPv.equals(blOpCgPubParamVo.getPkPv()))
				throw new BusException("调用保存记费明细时传入的记费内容并非同一次就诊，无法保存");
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
				ordItemAllList.add(cgProcessHandler.constructParam(blOpCgPubParamVo));
			}
		}
		//获取价格后的全部记费明细集合
		List<ItemPriceVo> allPriceList = new ArrayList<ItemPriceVo>();
		
		//2.取价格并设置主医保及优惠内容

		//2.2获取物品价格
		if(pdList!=null&&pdList.size()>0){
			List<ItemPriceVo> pdPrices = cgStrategyPubService.getPdPrice(pdList, storeMap);
			if(pdPrices!=null&&pdPrices.size()>0){
				allPriceList.addAll(pdPrices);
			}
		}
		BigDecimal  age = new BigDecimal(0);
		if(!CommonUtils.isEmptyString(pvVo.getPkPi())){
			age = CnPiPubService.getPvAge(pvVo.getPkPi());//计算患者年龄
		}
		//2.3根据价格策略取非药品价格
		if(ordItemAllList!=null&&ordItemAllList.size()>0){
			List<ItemPriceVo> pricelist = cgStrategyPubService.getItemAndOrdPrice(mainHp, pvVo.getPkOrg(), new Date(), ordItemAllList,pvVo,age);
			if(pricelist!=null&&pricelist.size()>0){
				allPriceList.addAll(pricelist);
			}
		}
		
		//2.4添加不走价格策略的物品
		if(pdHasPriceList!=null&&pdHasPriceList.size()>0)
			allPriceList.addAll(pdHasPriceList);
		
		//检验医嘱特殊处理
		if((ordItemAllList!=null&&ordItemAllList.size()>0)
			&& (allPriceList!=null&&allPriceList.size()>0)){
			allPriceList = cgStrategyPubService.filterOrdItem(allPriceList,ordItemAllList); 	
		}
		
		//检查医嘱特殊处理
		if(allPriceList!=null&&allPriceList.size()>0){ 
			cgStrategyPubService.filterExOrdItem(allPriceList);  
		}
		
		//计费前校验收费项目数量是否超过单日最大记费数量
		if(allPriceList!=null&&allPriceList.size()>0)
			ogCgStrategyPubService.checkItemOpCg(allPriceList,pvVo);
		
		
		//2.6调用医保记费策略
		/**当医保扩展属性‘0301’为1,调用广州公医策略*/
		if("1".equals(mainHp.getValAttr()))
			allPriceList = cgStrategyPubService.gzGyDistCharge(mainHp, pvVo, allPriceList,true);	//调用公医医保记费策略
		else 
			allPriceList = cgStrategyPubService.getItemPriceByCgDiv(mainHp, pvVo, allPriceList);
		
		//2.7调用优惠记费策略
		if(cateHp!=null)
			allPriceList = cgStrategyPubService.getItemDiscRatioByCgDiv(cateHp, pvVo, allPriceList);
		
		//草药特殊处理(单味草药是否自费逻辑)
		cgStrategyPubService.HerbDisp(allPriceList);

		//判断年龄加收和儿童加收是否同时生效，0特诊加收不再叠加年龄加收策略，1年龄加收基础上叠加特诊加收策略
		String sysParamBd0016 = ApplicationUtils.getSysparam("BD0016", false);

		//2.8添加至最终记费集合
		//记费明细排序号(一次计费服务，共用序列从1开始，每个明细加1.（不分患者）)
		int cgSortNo = 1;
		List<BlOpDt> bods = cgProcessHandler.constructBlOpDt(allPriceList,codeCg,cgSortNo,pvVo,sysParamBd0016);
		//保存门诊记费明细
		if(bods!=null&&bods.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), bods);
			
			/**当医保扩展属性‘0301’为1,组装ins_gzgy_bl信息*/
			if("1".equals(mainHp.getValAttr())){
				gyBlList.addAll(cgProcessHandler.constructInsGzgyBlByOp(bods,pvVo));	////2.9组装计费信息到公医计费明细
				
				if(gyBlList!=null && gyBlList.size()>0)
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsGzgyBl.class),	////4.批量写入公医计费明细
						gyBlList);
			}
		}
			
		//数据校验已在公共接口中实现，此处不再添加校验
		BlPubReturnVo res = new BlPubReturnVo();
		res.setBods(bods);
		return res;
	}
	/**
	 * 更新bl_op_dt表，如果患者主医保为广州公医医保则一并更新ins_gzgy_bl
	 * @param pv
	 * @param mainHp
	 * @param cateHp
	 * @param itemList
	 * @param flagPrice (是否需要限价审批)
	 * @param oldPkInsu(患者改之前的主医保计划)
	 * @param oldPkPicate (患者改之前的优惠主键)
	 */
	public void updateChargeOp(PvEncounter pv,HpVo mainHp,BdHp cateHp,List<BlOpDt> opDtList,Boolean flagPriceAvrl,String oldPkInsu,String oldPkPicate){
		//获取参数Pv0018
		String isUpdateCg = ApplicationUtils.getSysparam("PV0018", false);
		
		//组装结算明细信息到ItemPriceVo
		List<ItemPriceVo> pricelist = assItemPriceInfo(pv,opDtList,oldPkPicate,isUpdateCg);
		
		if(mainHp!=null){
			//当医保扩展属性‘0301’为1，且BL0023参数启用时，调用广州公医策略
			//判断是否启用广州公医
			if("1".equals(ApplicationUtils.getSysparam("BL0023", false)) &&
					"1".equals(mainHp.getValAttr()))
				pricelist = cgStrategyPubService.gzGyDistCharge(mainHp, pv, pricelist,flagPriceAvrl);	//调用公医医保记费策略
			else 
				pricelist = cgStrategyPubService.getItemPriceByCgDiv(mainHp, pv, pricelist);	//调用医保记费策略
		}
		
		//Pv0018参数值!=1时，修改患者分类不调用优惠策略
		if(!CommonUtils.isEmptyString(isUpdateCg) &&  EnumerateParameter.ONE.equals(isUpdateCg)){
			if(cateHp!=null)
				pricelist = cgStrategyPubService.getItemDiscRatioByCgDiv(cateHp, pv, pricelist);	//调用优惠策略
		}
		
		//组装计费明细信息到bl_op_dt
		List<BlOpDt> newDtList = assBlIpDt(opDtList,pricelist);
		//更新bl_op_dt表
		if(checkOpDt(newDtList)){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlOpDt.class),newDtList);
		}else{
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class),newDtList);
		}
		
		String oldHpAttr = "";
		//修改住院患者主医保信息，需要获取到原主医保的扩展属性
		if(!CommonUtils.isEmptyString(oldPkInsu))
			oldHpAttr = cgStrategyPubMapper.qryHpValAttr(oldPkInsu);
		
		//当医保扩展属性‘0301’为1，且BL0023参数启用时,更新ins_gzgy_bl表
		if("1".equals(mainHp.getValAttr())){
			//根据就诊信息和计费信息查询ins_gzgy_bl表
			List<String> pkCgips = new ArrayList<>();
			for(BlOpDt opdt:opDtList){	//获取pkCgip
				pkCgips.add(opdt.getPkCgop());
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
				newGyBlList = cgProcessHandler.constructInsGzgyBlByOp(newDtList,pv);
				//新增ins_gzgy_bl
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsGzgyBl.class),newGyBlList);
			}
		}
		
	}
	
	private boolean checkOpDt(List<BlOpDt> opList){
		Set<String> pkList = new HashSet<>();
		for(BlOpDt op:opList){
			pkList.add(op.getPkCgop());
		}
		//根据患者codePi获取患者pk_pi,pk_pv
		String pvListSql = "select count(1) from bl_op_dt where pk_cgop in("+ CommonUtils.convertSetToSqlInPart(pkList, "pk_cgop")+")";
		Integer cnt = DataBaseHelper.queryForScalar(pvListSql, Integer.class,new Object[]{});
		if(cnt>0)
			return true;
		else
			return false;
	}
	
	/**
	 * 组装信息到ins_gzgy_bl
	 * @param blDtList
	 * @param priceList
	 * @return
	 */
	public List<InsGzgyBl> assGzgyBl(List<BlOpDt> blDtList,List<InsGzgyBl> gyBlList){
		//把重新计算的信息组装到ins_gzgy_bl
		for(InsGzgyBl gyBl : gyBlList){
			for(BlOpDt opDt : blDtList){
				
				if(gyBl.getPkCg().equals(opDt.getPkCgop())){
					gyBl.setPrice(opDt.getPrice()); 	//单价
					gyBl.setQuan(opDt.getQuan()); 		//数量
					gyBl.setAmount(opDt.getAmount()); 	//总金额
					gyBl.setRatio(opDt.getRatioSelf());			//患者自付比例
					gyBl.setAmountPi(opDt.getAmountHppi()); 	//患者自付金额
					gyBl.setAmountHp(MathUtils.sub(gyBl.getAmount(), gyBl.getAmountPi()));		//医保支付金额
					
					break;
				}
				
			}
		}
		return gyBlList;
	}
	
	/**
	 * 组装信息到bl_op_dt
	 * @param blDtList
	 * @param priceList
	 * @return
	 */
	private List<BlOpDt> assBlIpDt(List<BlOpDt> blDtList,List<ItemPriceVo> priceList){
		//把重新计算的信息组装到bl_ip_dt
		for(BlOpDt opDt : blDtList){
			for(ItemPriceVo item : priceList){
				
				if(opDt.getPkCgop().equals(item.getPkCgop())){
					
					opDt.setPrice(item.getPrice());			//单价
					opDt.setRatioDisc(item.getRatioDisc()); //优惠比例
					opDt.setRatioSelf(item.getRatioSelf());	//自费比例
					opDt.setRatioAdd(item.getRatioSpec()!=null?item.getRatioSpec():0D);	//加收比例
					
					//计算收费项目加收后的单价
					if(opDt.getAmountAdd()!=null && MathUtils.compareTo(opDt.getAmountAdd(), 0D)!=0){
						Double newPrice = MathUtils.add(item.getPriceOrg(), MathUtils.div(opDt.getAmountAdd(), opDt.getQuan(), 6));
						opDt.setPrice(newPrice);			//单价
					}
					
//					//计算收费项目加收后的单价
//					Double newPriceOrg = MathUtils.mul(opDt.getPriceOrg(), MathUtils.add(1D, opDt.getRatioAdd()));
//					//特诊加收金额 price_org*quan*ratio_add
//					opDt.setAmountAdd(MathUtils.mul(MathUtils.mul(opDt.getPriceOrg(),opDt.getQuan()), opDt.getRatioAdd()));
					//amount，金额，price_org*quan+amount_add
					opDt.setAmount(MathUtils.mul(opDt.getQuan(), opDt.getPrice()));
					//amount_hppi，患者支付的医保金额，price*quan*ratio_self；
					if("1".equals(item.getFlagHppi())){
						opDt.setAmountHppi(item.getAmtHppi().doubleValue());
					}else{
						opDt.setAmountHppi(MathUtils.mul(MathUtils.mul(opDt.getPrice(), opDt.getQuan()),opDt.getRatioSelf()));
					}
					//amount_pi，amount_hppi-[price_org*(1-ratio_disc)*quan]+amount_add，计算结果小于0时为0；
		            Double amt = MathUtils.sub(opDt.getAmountHppi(), MathUtils.mul(MathUtils.mul(opDt.getPrice(), MathUtils.sub(1D, opDt.getRatioDisc())), opDt.getQuan()));
		            opDt.setAmountPi(amt);
		            
		            //校验ratio_disc和ratio_self为1时，amount和amount_pi和amount_hppi是否相等，不相等时以amount_pi的值为准
					if(MathUtils.compareTo(opDt.getRatioDisc(), 1D)==0 &&
							MathUtils.compareTo(opDt.getRatioSelf(), 1D)==0 &&
							MathUtils.compareTo(opDt.getAmount(), opDt.getAmountPi())!=0){
						opDt.setAmount(opDt.getAmountPi());
						opDt.setAmountHppi(opDt.getAmountPi());
					}
		            break;
				}
				
			}
		}
		return blDtList;
	}
	
	/**
	 * 组装收费明细信息到ItempriceVo
	 * @param ipDtList
	 * @return
	 */
	private List<ItemPriceVo> assItemPriceInfo(PvEncounter pv,List<BlOpDt> opDtList,String oldPkPicate,String isUpdateCg){
		List<ItemPriceVo> priceList = new ArrayList<>();
//		//获取患者修改之前是否是特诊患者
//		Map<String,Object> retMap = DataBaseHelper.queryForMap("select flag_spec from pi_cate where pk_picate = ?",
//				oldPkPicate);
//		
//		String oldFlagSpec = "";
//		if(retMap!=null && retMap.size()>0 && retMap.get("flagSpec")!=null)
//			oldFlagSpec = CommonUtils.getString(retMap.get("flagSpec"));
			
//		//获取收费项目特诊加价比例
//		List<Map<String,Object>> rsList = new ArrayList<>();
//		//Pv0018参数值!=1时，修改患者分类时不重新获取已记费项目的特诊加收比例
//		if(!CommonUtils.isEmptyString(isUpdateCg) &&  EnumerateParameter.ONE.equals(isUpdateCg)){
//			Set<String> pkItems = new HashSet<>();
//			for(BlOpDt opdt:opDtList){
//				pkItems.add(opdt.getPkItem());
//			}
//			rsList =  cgStrategyPubMapper.qryRatioSpecList(pv.getEuPvtype(),pkItems);
//		}
		for(BlOpDt opdt : opDtList){
			ItemPriceVo item = new ItemPriceVo();
			item.setPkCgop(opdt.getPkCgop());
			item.setFlagPv(opdt.getFlagPv());
			item.setPkPv(opdt.getPkPv());
			item.setPkPi(opdt.getPkPi());
			item.setPkOrgApp(opdt.getPkOrgApp());
			item.setPkDeptApp(opdt.getPkDeptApp());
			item.setPkOrgEx(opdt.getPkOrgEx());
			item.setDateHap(opdt.getDateHap());
			item.setPkUnit(opdt.getPkUnit());
			item.setPkUnitPd(opdt.getPkUnitPd());
			item.setFlagPd(opdt.getFlagPd());
			if(BlcgUtil.converToTrueOrFalse(opdt.getFlagPd()))
			{
				item.setPkOrdOld(opdt.getPkPd());
				item.setPkItem(opdt.getPkPd());
			}	
			else
			{
				item.setPkItemOld(opdt.getPkItem());
				item.setPkItem(opdt.getPkItem());
			}	
			item.setPrice(opdt.getPrice());
			item.setPriceCs(opdt.getPrice());
			item.setPriceCost(opdt.getPriceCost());
			item.setPkItemcate(opdt.getPkItemcate());
			item.setPkPres(opdt.getPkPres());
			item.setPkCnord(opdt.getPkCnord());
			item.setQuan(opdt.getQuan());
			item.setPriceOrg(opdt.getPriceOrg());
			item.setPkDisc(opdt.getPkDisc());
			item.setRatioDisc(opdt.getRatioDisc());
			item.setRatioSelf(opdt.getRatioSelf());
			item.setPkItemcate(opdt.getPkItemcate());
			item.setSpec(opdt.getSpec());
			item.setNameCg(opdt.getNameCg());
			item.setName(opdt.getNameCg());
			item.setPkCnord(opdt.getPkCnord());
			item.setRatioSpec(opdt.getRatioAdd());
						
//			//项目加收比例
//			if(rsList!=null && rsList.size()>0){
//				for(Map<String,Object> map : rsList){
//					if(map.get("pkItem")!=null && 
//							opdt.getPkItem().equals(map.get("pkItem"))){
//						//如果修改之前的患者为特诊，则pricecs字段减去特诊加收费用
//						if(BlcgUtil.converToTrueOrFalse(oldFlagSpec))
//							item.setPriceCs(MathUtils.sub(item.getPriceCs(), MathUtils.mul(item.getPriceOrg(), CommonUtils.getDoubleObject(map.get("ratioSpec")))));
//						
//						//如果修改后的患者为特诊，则price_cs字段加上特诊加收费用
//						if(BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())){
//							Double price = MathUtils.div(MathUtils.sub(opdt.getAmount(), opdt.getAmountAdd()), opdt.getQuan(),6);
//							item.setPriceCs(MathUtils.add(price, MathUtils.mul(item.getPriceOrg(), CommonUtils.getDoubleObject(map.get("ratioSpec")))));
//							item.setRatioSpec(CommonUtils.getDoubleObject(map.get("ratioSpec")));
//						} else {
//							item.setRatioSpec(0D);
//						}
//						
//						if(item.getPriceCs()<0)
//							item.setPriceCs(item.getPrice());
//						
//						break;
//					}
//				}
//			} else {
//				item.setRatioSpec(opdt.getRatioAdd());
//			}
			
			priceList.add(item);
		}
		return priceList;
	}
	
	
	
	
	
	
	
	
	
}
