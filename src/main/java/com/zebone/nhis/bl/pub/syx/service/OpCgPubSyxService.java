package com.zebone.nhis.bl.pub.syx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyPv;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CnPiPubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 门诊记费公共服务-syx客户化
 * @author 
 *
 */
@Service
public class OpCgPubSyxService {
	@Resource
	private CgStrategyPubService cgStrategyPubService;
	@Resource
	private CgStrategyPubMapper cgStrategyPubMapper;
	@Resource
	private CgProcessHandler cgProcessHandler;
	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	@Resource
	private OpCgDistrictService opCgDistrictService;

	private Logger logger = LoggerFactory.getLogger("com.zebone");
	
	public  BlPubReturnVo chargeOpBatch(List<BlPubParamVo> blOpCgPubParamVos)
			throws BusException{
		//判断是否启用广州公医
		if("1".equals(ApplicationUtils.getSysparam("BL0023", false))){
			//调用公医记费接口
			return opCgDistrictService.chargeOpBatch(blOpCgPubParamVos);
		}
		
		String codeCg = ApplicationUtils.getCode("0601");
		Map<String, Object> mapParam = new HashMap<String, Object>();
		//取患者就诊属性
		String pkPv =  blOpCgPubParamVos.get(0).getPkPv();
		String pkOrg = blOpCgPubParamVos.get(0).getPkOrg(); 
		if(CommonUtils.isEmptyString(pkPv))
			throw new BusException("调用门诊记费方法时，参数中未传入pkPv的值！");
		if(CommonUtils.isEmptyString(pkOrg))
			throw new BusException("调用门诊记费方法时，参数中未传入pkOrg的值！");
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
		if(mainHp==null)
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
		//医嘱及收费项目集合
		List<ItemPriceVo> ordItemAllList = new ArrayList<ItemPriceVo>();
		
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
			cgStrategyPubService.filterOpExOrdItem(allPriceList);
		}
		
		//计费前校验收费项目数量是否超过单日最大记费数量
		if(allPriceList!=null&&allPriceList.size()>0)
			cgStrategyPubService.checkItemCg(allPriceList,pvVo);

		//2.6调用医保记费策略
		List<ItemPriceVo> filterOutItem = BlcgUtil.getCgOpCustomerService().filterIterPrice(allPriceList);
		allPriceList.removeAll(filterOutItem);
		allPriceList = cgStrategyPubService.getItemPriceByCgDiv(mainHp, pvVo, allPriceList);
		allPriceList.addAll(filterOutItem);
		//2.7调用优惠记费策略
		if(cateHp!=null)
			allPriceList = cgStrategyPubService.getItemDiscRatioByCgDiv(cateHp, pvVo, allPriceList);
		//2.8添加至最终记费集合
		//记费明细排序号(一次计费服务，共用序列从1开始，每个明细加1.（不分患者）)
		int cgSortNo = 1;
		//判断年龄加收和儿童加收是否同时生效，0特诊加收不再叠加年龄加收策略，1年龄加收基础上叠加特诊加收策略
		String sysParamBd0016 = ApplicationUtils.getSysparam("BD0016", false);
		List<BlOpDt> bods = cgProcessHandler.constructBlOpDt(allPriceList,codeCg,cgSortNo,pvVo,sysParamBd0016);
		//保存门诊记费明细
		if(bods!=null&&bods.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), bods);
		//数据校验已在公共接口中实现，此处不再添加校验
		BlPubReturnVo res = new BlPubReturnVo();
		res.setBods(bods);
		return res;
	}
	/**
     * 根据医保或患者分类更新患者费用明细
     * @param pkPv
     * @param pkHp
     * @param pkPicate
     */
	public void updateBlOpDtCgRate(String pkPv,String pkHp,String pkPicate,String oldPkInsu,String oldPkPicate){
		
		//3.查询患者对应未结算费用明细
		List<BlOpDt> opDtList = cgStrategyPubMapper.qryOpDtByPv(pkPv);
		
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
			//3.1查询此pkPv在ins_gzgy_pv的记录信息
			InsGzgyPv gyPv = DataBaseHelper.queryForBean("select * from ins_gzgy_pv where pk_pv = ?"
					, InsGzgyPv.class, new Object[]{pvvo.getPkPv()});
			//3.2删除此pkPv在ins_gzgy_pv的记录信息
			DataBaseHelper.execute("delete from ins_gzgy_pv where pk_pv = ?",new Object[]{pvvo.getPkPv()});
			//3.3查询患者主医保是否是公医患者，如果是则写ins_gzgy_pv表
			if ((!CommonUtils.isEmptyString(mainHp.getValAttr()) && ("1".equals(mainHp.getValAttr()))) 
					|| (!CommonUtils.isEmptyString(mainHp.getEuHptype()) && EnumerateParameter.FOUR.equals(mainHp.getEuHptype()))) { // 主医保是单位医保或是广州公医
				InsGzgyPv insGzgyPv = new InsGzgyPv();
				if(gyPv!=null){
					insGzgyPv.setPkPv(pvvo.getPkPv());
					insGzgyPv.setPkPi(pvvo.getPkPi());
					insGzgyPv.setPkHp(pvvo.getPkInsu());
					insGzgyPv.setEuPvtype(pvvo.getEuPvtype());
					insGzgyPv.setMcno(gyPv.getMcno());
					insGzgyPv.setDictPsnlevel(gyPv.getDictPsnlevel());
					insGzgyPv.setEuPvmodeHp(gyPv.getEuPvmodeHp());
					insGzgyPv.setDrugquota(gyPv.getDrugquota());
					insGzgyPv.setDictSpecunit(gyPv.getDictSpecunit());
				}else{
					insGzgyPv.setPkPv(pvvo.getPkPv());
					insGzgyPv.setPkPi(pvvo.getPkPi());
					insGzgyPv.setPkHp(pvvo.getPkInsu());
					insGzgyPv.setEuPvtype(pvvo.getEuPvtype());
					insGzgyPv.setEuPvmodeHp(pvvo.getEuPvmode());
					insGzgyPv.setDelFlag("0");
					insGzgyPv.setMcno("");//暂时写null
					insGzgyPv.setDictSpecunit("");
				}
				ApplicationUtils.setDefaultValue(insGzgyPv, true);
				DataBaseHelper.insertBean(insGzgyPv);
			}
		}
		
		//收费明细为空则不更新bl_op_dt表
		if(opDtList==null || opDtList.size()<=0)
			return;
		
		//4.依次调用记费策略设置自费比例、优惠比例及合计金额
		opCgDistrictService.updateChargeOp(pvvo,mainHp,cateHp,opDtList,true,oldPkInsu,oldPkPicate);
	}
	
}
