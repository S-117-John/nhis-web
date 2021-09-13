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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.syx.dao.CgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.support.CgProcessHandler;
import com.zebone.nhis.bl.pub.syx.vo.HpVo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyBl;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyChk;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyPv;
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
 * 住院记费公共服务-syx客户化
 * @author yangxue
 *
 */
@Service
public class IpCgPubSyxService {
	@Resource
	private CgStrategyPubService cgStrategyPubService;
	@Resource
	private CgStrategyPubMapper cgStrategyPubMapper;
	@Resource
	private CgProcessHandler cgProcessHandler;
	@Resource
	private QueryRemainFeeService queryFeeServcie;
	@Resource
	private IpCgDistrictService ipCgDistrictService;
	
	private Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	private Logger nhisLogger = LoggerFactory.getLogger("com.zebone");
	
	
	/**
	 * 住院批量记费
	 * @param blCgPubParamVos
	 * @return
	 * @throws BusException
	 */
	public  BlPubReturnVo chargeIpBatch(List<BlPubParamVo> blCgPubParamVos,boolean isAllowQF)
			throws BusException{
		//判断是否启用广州公医
		if("1".equals(ApplicationUtils.getSysparam("BL0023", false))){
			return ipCgDistrictService.chargeIpBatch(blCgPubParamVos,isAllowQF,logger);
		}
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
		//记费明细排序号(一次计费服务，共用序列从1开始，每个明细加1.（不分患者）)
		int cgSortNo = 1;
		//2.取价格并设置主医保及优惠内容
		StringBuilder msgQF = new StringBuilder("");
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
					BdHp mainHp = null;
					BdHp cateHp = null; 
					for(HpVo hp:hplist){
						if(pv.getPkInsu().equals(hp.getPkHp())){
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
					//2.4未获取到价格，跳出循环
//					if(pricelist==null||pricelist.size()<=0)
//						continue;
					//检验医嘱特殊处理
					if((itemlist!=null&&itemlist.size()>0)
							&& (pricelist!=null&&pricelist.size()>0)){
						pricelist = cgStrategyPubService.filterOrdItem(pricelist,itemlist);
					}
					//检查医嘱特殊处理
					if((itemlist!=null&&itemlist.size()>0)
							&& (pricelist!=null&&pricelist.size()>0)){ 
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
					//校验是否有科研医嘱，科研医嘱不调用医保计费策略
			    	Map<String,List<ItemPriceVo>>  typeMap = cgStrategyPubService.filterEuOrdtype(pricelist);
			    	if(typeMap!=null){
			    		//非科研医嘱
			    		//pricelist = (typeMap.get("fky")!=null&&typeMap.get("fky").size()>0)?typeMap.get("fky"):pricelist;
			    		pricelist = typeMap.get("fky");
			    		if(pricelist!=null && pricelist.size()>0){
			    			//2.6调用医保记费策略
							pricelist = cgStrategyPubService.getItemPriceByCgDiv(mainHp, pv, pricelist);
			    		}else{
			    			pricelist = new ArrayList<>();
			    		}
			    		
			    		//科研医嘱(不走医保记费策略)
			    		List<ItemPriceVo> kypricelist = typeMap.get("ky");
			    		if(kypricelist!=null&&kypricelist.size()>0){
			    			pricelist.addAll(kypricelist);
			    		}
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
					}
					//2.8校验患者是否欠费
					if(!isAllowQF && QueryRemainFeeService.isControlFee()){
						if(!queryFeeServcie.isArrearage(pkPv, pv.getPkInsu(),totalAmt)){
							msgQF = msgQF.append(pv.getBedNo()+"床【").append(pv.getNamePi()).append("】,");
							//throw new BusException("患者【"+pv.getNamePi()+"】已欠费，无法完成本次记费!");
						}
					}
				}
			}
		}
		//提示欠费
		if(!CommonUtils.isEmptyString(msgQF.toString())) {
			throw new BusException(msgQF.toString() + "已欠费无法完成本次记费!");
		}
		
		if(dtlist!=null&&dtlist.size()>0){
			//3.批量写入记费表
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class),
					dtlist);
			//4.处理返回。
			res.setBids(dtlist);
		}
		return res;
	}
	
	/**
     * 根据医保或患者分类更新患者费用明细
     * @param pkPv
     * @param pkHp
     * @param pkPicate
     */
	public void updateBlIpDtCgRate(String pkPv,String pkHp,String pkPicate,String oldPkInsu,String oldPkPicate){
		
		//3.查询患者对应未结算费用明细
		List<BlIpDt> ipDtList = cgStrategyPubMapper.qryIpDtByPv(pkPv);
		
		//查询患者就诊信息
		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);
		
		//1.患者分类不为空获取患者分类信息
		BdHp cateHp = null; 
		if(!CommonUtils.isEmptyString(pkPicate)){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkPicate", pkPicate);
			List<HpVo> hpList = cgStrategyPubMapper.queryHpList(paramMap);
			if(hpList!=null && hpList.size()>0)
				cateHp = hpList.get(0);
		}
		
		//2.医保不为空获取医保信息
		HpVo mainHp = null;
		if(!CommonUtils.isEmptyString(pkHp)){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkHp", pkHp);//默认只查询住院使用的医保
			List<HpVo> hpList = cgStrategyPubMapper.queryHpList(paramMap);
			/**获取当前医保扩展属性值*/
			String valAttr = cgStrategyPubMapper.qryHpValAttr(pkHp);
			
			if(hpList!=null && hpList.size()>0){
				mainHp = hpList.get(0);
				mainHp.setValAttr(valAttr);
			}
				
		}
		//3.操作公医表
		if(!oldPkInsu.equals(pkHp)){//修改主医保则操作公医表
			//3.1删除此pkPv在ins_gzgy_pv的记录信息
			DataBaseHelper.execute("delete from ins_gzgy_pv where pk_pv = ?",new Object[]{pvvo.getPkPv()});
			//3.查询患者主医保是否是公医患者，如果是则写ins_gzgy_pv表
			if ((!CommonUtils.isEmptyString(mainHp.getValAttr()) && ("1".equals(mainHp.getValAttr()))) 
					|| (!CommonUtils.isEmptyString(mainHp.getEuHptype()) && EnumerateParameter.FOUR.equals(mainHp.getEuHptype()))) { // 主医保是单位医保或是广州公医
				InsGzgyPv insGzgyPv = new InsGzgyPv();
				insGzgyPv.setPkPv(pvvo.getPkPv());
				insGzgyPv.setPkPi(pvvo.getPkPi());
				insGzgyPv.setPkHp(pvvo.getPkInsu());
				insGzgyPv.setEuPvtype(pvvo.getEuPvtype());
				insGzgyPv.setDelFlag("0");
				insGzgyPv.setMcno("");//暂时写null
				insGzgyPv.setDictSpecunit("");
				DataBaseHelper.insertBean(insGzgyPv);
			}
		}
		
		//收费明细为空则不更新bl_ip_dt表
		if(ipDtList==null || ipDtList.size()<=0)
			return;
		
		//4.依次调用记费策略设置自费比例、优惠比例及合计金额
		ipCgDistrictService.updateChargeIp(pvvo,mainHp,cateHp,ipDtList,true,oldPkInsu,oldPkPicate);
	}
	
	/**
	 * 限额项目审批更新bl_ip_dt表
	 * @param gzgyChk
	 */
	public void itemApValCg(InsGzgyChk gzgyChk){
		if(CommonUtils.isEmptyString(gzgyChk.getPkCnord()))
			throw new BusException("未获取到医嘱主键,无法更新计费信息！");
		
		/**获取患者就诊信息*/
		PvEncounter pv = DataBaseHelper.queryForBean(
				"select * from PV_ENCOUNTER where pk_pv = ?",
				PvEncounter.class, gzgyChk.getPkPv());
		
		/**查询患者主医保信息*/
		HpVo mainHp = null;
		Map<String,Object> paramHpMap = new HashMap<String,Object>();
		paramHpMap.put("pkHp", pv.getPkInsu());//默认只查询住院使用的医保
		List<HpVo> mainHpList = cgStrategyPubMapper.queryHpList(paramHpMap);
		/**获取当前医保扩展属性值*/
		String valAttr = cgStrategyPubMapper.qryHpValAttr(pv.getPkInsu());
		
		if(mainHpList!=null &&mainHpList.size()>0){
			mainHp = mainHpList.get(0);
			mainHp.setValAttr(valAttr);
		}
		
		/**获取患者优惠信息*/
		HpVo cateHp = null;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPicate", pv.getPkPicate());
		List<HpVo> cateHpList = cgStrategyPubMapper.queryHpList(paramMap);
		if(cateHpList!=null && cateHpList.size()>0)
			cateHp = cateHpList.get(0);
		
		/**获取患者医嘱项目计费信息*/
		Map<String,Object> paramDtMap = new HashMap<String,Object>();
		paramDtMap.put("pkPv", gzgyChk.getPkPv());
		paramDtMap.put("pkCnord", gzgyChk.getPkCnord());
		paramDtMap.put("pkItem", gzgyChk.getPkItem());
		List<BlIpDt> ipDtList = cgStrategyPubMapper.qryIpDtList(paramDtMap);
		
		//4.依次调用记费策略设置自费比例、优惠比例及合计金额
		ipCgDistrictService.updateChargeIp(pv,mainHp,cateHp,ipDtList,false,null,null);
	}
	
	/**
	 * 取消审批限额项目
	 * @param gzgyChk
	 */
	public void cancelItemApValCg(InsGzgyChk gzgyChk){
		if(CommonUtils.isEmptyString(gzgyChk.getPkCnord()))
			throw new BusException("未获取到医嘱主键,无法更新计费信息！");
		
		/**获取患者项目结算信息*/
		Map<String,Object> paramDtMap = new HashMap<String,Object>();
		paramDtMap.put("pkPv", gzgyChk.getPkPv());
		paramDtMap.put("pkCnord", gzgyChk.getPkCnord());
		paramDtMap.put("pkItem", gzgyChk.getPkItem());
		List<BlIpDt> ipDtList = cgStrategyPubMapper.qryIpDtList(paramDtMap);
		
		for(BlIpDt blIpDt : ipDtList){
			//blIpDt.setPrice(blIpDt.getPriceOrg());
			blIpDt.setRatioSelf(1D);
			blIpDt.setAmountHppi(MathUtils.mul(blIpDt.getPriceOrg(), blIpDt.getQuan()));
			
			//amount_pi=price_org*quan*ratio_disc+amount_add，计算结果小于0时为0；
            Double amt = MathUtils.add(MathUtils.mul(MathUtils.mul(blIpDt.getPriceOrg(), blIpDt.getQuan()),blIpDt.getRatioDisc()), blIpDt.getAmountAdd());
            if(MathUtils.compareTo(amt, 0D)<0){
            	blIpDt.setAmountPi(0D);
			}else{
				blIpDt.setAmountPi(amt);
			}
		}
		
		//更新bl_ip_dt表
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlIpDt.class),ipDtList);
		
		//根据就诊信息和计费信息查询ins_gzgy_bl表
		List<String> pkCgips = new ArrayList<>();
		for(BlIpDt ipdt:ipDtList){	//获取pkCgip
			pkCgips.add(ipdt.getPkCgip());
		}
		
		List<InsGzgyBl> gyBlList = cgStrategyPubMapper.qryGzgyBlList(gzgyChk.getPkPv(), pkCgips);
		//如果查出公医计费明细更新ins_gzgy_bl,否则新增ins_gzgy_bl
		if(gyBlList!=null && gyBlList.size()>0){
			//组装公医计费明细
			List<InsGzgyBl> newGyBlList = ipCgDistrictService.assGzgyBl(ipDtList, gyBlList);
			//更新ins_gzgy_bl
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(InsGzgyBl.class),newGyBlList);
		}	
	}
	
	/**
	 * 住院退费个性化需求(中二)
	 * @param params
	 * @param dtlist
	 * @return
	 */
	public BlPubReturnVo refundInBatch(List<RefundVo> params,List<BlIpDt> dtlist){
		//退费前校验此患者就诊状态是否可以退费。
		String pkPv = dtlist.get(0).getPkPv();
		Map<String,Object> pv = cgStrategyPubMapper.qryPvStatus(pkPv);
		//eu_status=0,不能做记退费处理!
		if(EnumerateParameter.ZERO.equals(CommonUtils.getString(pv.get("euStatus"))))
			throw new BusException("患者【"+CommonUtils.getString(pv.get("namePi"))+"】未就诊，不能做退费处理!");
		
		//eu_status=3,不能做记退费处理!
		if(EnumerateParameter.THREE.equals(CommonUtils.getString(pv.get("euStatus"))))
			throw new BusException("患者【"+CommonUtils.getString(pv.get("namePi"))+"】已出院，不能做退费处理!");
		
		//eu_status=2，flag_frozen=1，患者就诊被冻结，不能做记退费处理!
		if(EnumerateParameter.TWO.equals(CommonUtils.getString(pv.get("euStatus")))
				&& EnumerateParameter.ONE.equals(CommonUtils.getString(pv.get("flagFrozen"))))
			throw new BusException("患者【"+CommonUtils.getString(pv.get("namePi"))+"】就诊被冻结，不能做退费处理!");
		
		
		//查询患者医保扩展属性
		String varAttr = cgStrategyPubMapper.qryHpValAttrByPv(dtlist.get(0).getPkPv()); 
		//当医保扩展属性‘0301’为1,操作表ins_gzgy_bl
		if(!CommonUtils.isEmptyString(varAttr) && "1".equals(varAttr)){
			List<InsGzgyBl> gyBlList = cgStrategyPubService.constructInsGzgyBl(dtlist);	//组装计费信息到公医计费明细
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsGzgyBl.class),	//批量写入公医计费明细
					gyBlList);
		}
		return null;
	}
		
	/**
	 * 医嘱自动执行记费
	 */
	public void ordAutoexecCharge(){
		//查询需要自动执行的医嘱数据
		List<BlPubParamVo> blCgPubParamVos = cgStrategyPubMapper.qryOrdExecBl();
		
		if(blCgPubParamVos==null ||blCgPubParamVos.size()<=0)
			return;
		
		/**调用记费接口*/
		//2018-12-31 中二记费 - 添加参数【BL0005】除住院医生外是否控制欠费
		String paramChkFee = ApplicationUtils.getSysparam("BL0005", false,"请维护系统参数【BL0005-住院患者是否控制欠费】;"
					+ "\n【机构】级别，取值范围【0/1】，默认值【0】;"
					+ "\n备注：除住院医生外的其他业务节点是否控制欠费（0不控制1控制）！");
		this.chargeIpBatch(blCgPubParamVos, !"1".equals(paramChkFee));
		
		/**更新医嘱执行单:通过计划任务自动执行时，执行人、执行人姓名为空*/
		List<String> pkList = new ArrayList<>();
		//循环获取执行pk_exocc
		for(BlPubParamVo vo : blCgPubParamVos){
			pkList.add(vo.getPkOrdexdt());
		}
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("dateOcc", new Date());//执行日期
		paramMap.put("pkList", pkList);
		//更新
		cgStrategyPubMapper.updateOrdExocc(paramMap);
	}
	
}
