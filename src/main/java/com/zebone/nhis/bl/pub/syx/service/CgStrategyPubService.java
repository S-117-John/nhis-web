package com.zebone.nhis.bl.pub.syx.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.service.ICgLabService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.syx.dao.CgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.vo.*;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlCgPdParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdChap;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyBl;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 中山二院公共记费策略服务
 * @author yangxue 
 *
 */
@Service
public class CgStrategyPubService {
	@Resource
	private PriceStrategyService priceStrategyService;
	@Resource
	private CgStrategyPubMapper cgStrategyPubMapper;
	@Autowired
	private PdStOutPubService pdStOutPubService;
	@Resource
	private CgOpCustomerService cgOpCustomerService;

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
	public List<ItemPriceVo> batchChargePriceModelByOrdAndItem(PvEncounter pv,BdHp hp,String pkOrg,String dateHap,List<String> pkItems,List<String> pkOrds) throws BusException {
		//构造获取收费项目对应价格查询参数
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkOrg", pkOrg);
		paramMap.put("dateHap", dateHap);
		paramMap.put("pkHp", hp.getPkHp());
		if(pv!=null)
			paramMap.put("euPvtype", pv.getEuPvtype());
		List<ItemPriceVo> resultList = new ArrayList<ItemPriceVo>();
		if(pkItems!=null&&pkItems.size()>0){
			Set<String> itemSet = new HashSet<>(pkItems);
			paramMap.put("itemList", CommonUtils.convertSetToSqlInPart(itemSet, "pk_item"));
			//批量获取收费项目在医保下设置的价格
			List<ItemPriceVo> tempItemList  = cgStrategyPubMapper.queryItemAndChildPrice(paramMap);
			if(tempItemList == null|| tempItemList.size()<=0) {
				throw new BusException("未获取到医保【"+hp.getName()+"】下设置的本次计费的任何收费项目价格！");
			}
			//校验收费项目是否可用,存在不可用项目直接抛异常
			priceStrategyService.verfyItemStatus(tempItemList);
			//校验所有收费项目是否存在价格
			priceStrategyService.verfyItemHasPrice(pkItems,tempItemList,hp.getName());
			resultList.addAll(tempItemList);
		}
		if(pkOrds!=null&&pkOrds.size()>0){
		    Set<String> ordSet = new HashSet<>(pkOrds);
			paramMap.put("ordList", CommonUtils.convertSetToSqlInPart(ordSet, "pk_ord"));
			//批量获取医嘱项目在医保下设置的价格
			List<ItemPriceVo> tempOrdList  = cgStrategyPubMapper.queryItemAndChildPriceByOrd(paramMap);
			if(tempOrdList == null|| tempOrdList.size()<=0) {
				throw new BusException("未获取到医保【"+hp.getName()+"】下设置的本次计费的任何医嘱项目价格！");
			}
			if(tempOrdList != null && tempOrdList.size()>0) {
				//校验收费项目是否可用,存在不可用项目直接抛异常
				priceStrategyService.verfyItemStatus(tempOrdList);
				//校验所有医嘱项目对应收费项目是否存在收费价格
				priceStrategyService.verfyOrdItemHasPrice(pkOrds,tempOrdList,hp.getName());
				//设置患者自付比例或价格（被分摊后的价格）
				//tempOrdList=priceStrategyService.batchChargeAmountWithinHealthCare(tempOrdList,hp,pkOrg,dateHap);
				resultList.addAll(tempOrdList);
			}
		}
		//过滤价格为0的收费项目 
		if(!EnumerateParameter.ONE.equals(ApplicationUtils.getSysparam("BL0065", false))) {
			priceStrategyService.priceZeroFilter(resultList);
		}
		return resultList;
	}
	/**
	 * 获取指定医保下收费项目或医嘱项目对应设置的价格(不含药品)
	 * @param hp 使用医保
	 * @param pkOrg 所属机构
	 * @param dateHap 费用日期
	 * @param itemlist
	 * @return
	 */
	public List<ItemPriceVo> getItemAndOrdPrice(BdHp hp,String pkOrg,Date dateHap,List<ItemPriceVo> itemlist,PvEncounter pv,BigDecimal age){
		if(itemlist==null||itemlist.size()<=0){
			return null;
		}
		List<String> pkItems = new ArrayList<String>();
		List<String> pkOrds = new ArrayList<String>();
		for(ItemPriceVo item:itemlist){
			//优先使用医嘱项目
			if(!CommonUtils.isEmptyString(item.getPkOrdOld())){
				pkOrds.add(item.getPkOrdOld());
			}else if(!CommonUtils.isEmptyString(item.getPkItemOld())){
				pkItems.add(item.getPkItemOld());
			}
		}
		if(dateHap == null)
			dateHap = new Date();
		String dateStr = DateUtils.getDateTimeStr(dateHap);
		
		
		List<ItemPriceVo> itemPriceList =  this.batchChargePriceModelByOrdAndItem(pv,hp, pkOrg, dateStr, pkItems, pkOrds);
		if(itemPriceList==null||itemPriceList.size()<=0)
			return null;
		List<ItemPriceVo> newItemPriceList = new ArrayList<ItemPriceVo>();
		//计算医嘱或收费项目对应数量及单价
		for(ItemPriceVo itemvo :itemlist){
			List<ItemPriceVo> prices = new ArrayList<ItemPriceVo>();
			if(!CommonUtils.isEmptyString(itemvo.getPkOrdOld())){
				//通过医嘱项目计算
				prices = calPriceAndQuan(itemPriceList,itemvo.getQuanOld(),itemvo.getPkOrdOld(),true);
			}else{
				//通过收费项目计算
				prices = calPriceAndQuan(itemPriceList,itemvo.getQuanOld(),itemvo.getPkItemOld(),false);
			}
			if(prices!=null&&prices.size()>0){
				//设置相应的记费信息,为了转换为记费明细对象
		    	for(ItemPriceVo pricevo:prices){ 
		    		ItemPriceVo newitem = new ItemPriceVo();
		    		ApplicationUtils.copyProperties(newitem, pricevo);
		    		newitem.setPkCgip(itemvo.getPkCgip());
		    		newitem.setPkCnord(itemvo.getPkCnord());
		    		newitem.setPkOrgApp(itemvo.getPkOrgApp());
		    		newitem.setPkDeptApp(itemvo.getPkDeptApp());
		    		newitem.setPkDeptNsApp(itemvo.getPkDeptNsApp());
		    		newitem.setPkDeptEx(itemvo.getPkDeptEx());
		    		newitem.setPkOrgEx(itemvo.getPkOrgEx());
		    		newitem.setPkOrdexdt(itemvo.getPkOrdexdt());
		    		newitem.setPkDeptCg(itemvo.getPkDeptCg());
		    		newitem.setPkEmpApp(itemvo.getPkEmpApp());
		    		newitem.setPkEmpCg(itemvo.getPkEmpCg());
		    		newitem.setDateHap(itemvo.getDateHap());
		    		newitem.setFlagPv(itemvo.getFlagPv());
		    		newitem.setPkPv(itemvo.getPkPv());
		    		newitem.setPkWgOrg(itemvo.getPkWgOrg());
		    		newitem.setPkWg(itemvo.getPkWg());
		    		newitem.setPkWgEx(itemvo.getPkWgEx());
		    		//已经设置了规格的收费项目，以传入的规格为准
		    		if(!CommonUtils.isEmptyString(itemvo.getSpec())){
		    			newitem.setSpec(itemvo.getSpec());
		    		}
		    		newitem.setPkPi(itemvo.getPkPi());
		    		newitem.setPkPres(itemvo.getPkPres());
		    		newitem.setNameEmpApp(itemvo.getNameEmpApp());
		    		newitem.setNameEmpCg(itemvo.getNameEmpCg());
		    		newitem.setEuAdditem(itemvo.getEuAdditem());
		    		newitem.setDateExpire(itemvo.getDateExpire());
		    		newitem.setDateStart(itemvo.getDateStart());
		    		newitem.setCodeOrdtype(itemvo.getCodeOrdtype());
		    		newitem.setFlagFit(itemvo.getFlagFit());
		    		newitem.setDescFit(itemvo.getDescFit());
		    		newitem.setFlagSign(itemvo.getFlagSign());
		    		newitem.setOrdsn(itemvo.getOrdsn());
		    		newitem.setOrdsnParent(itemvo.getOrdsnParent());
		    		newitem.setDtSamptype(itemvo.getDtSamptype());
		    		newitem.setBarcode(itemvo.getBarcode());
		    		newitem.setPkEmpEx(itemvo.getPkEmpEx());
		    		newitem.setNameEmpEx(itemvo.getNameEmpEx());
		    		newitem.setEuBltype(itemvo.getEuBltype());
		    		newitem.setPkCnordRl(itemvo.getPkCnordRl());
		    		newitem.setDateCg(itemvo.getDateCg());
		    		newitem.setDateCg(itemvo.getDateCg());
		    		newitem.setSortno(itemvo.getSortno());
		    		newitem.setDosage(itemvo.getDosage());
		    		newitem.setUnitDos(itemvo.getUnitDos());
		    		newitem.setNameSupply(itemvo.getNameSupply());
		    		newitem.setNameFreq(itemvo.getNameFreq());
		    		newitem.setIsFixedCg(itemvo.getIsFixedCg());
		    		newitem.setPkDeptJob(itemvo.getPkDeptJob());
		    		newitem.setPkDeptAreaapp(itemvo.getPkDeptAreaapp());
		    		//外部传入了优惠比例，则以外部传入为准
		    		if(itemvo.getRatioDisc()!=null)
						newitem.setRatioDisc(itemvo.getRatioDisc());
		    		if(itemvo.getRatioSelf()!=null)
						newitem.setRatioSelf(itemvo.getRatioSelf());
		    		newItemPriceList.add(newitem);
		    	}
		    	
		    }
		}
		///添加儿童或特诊计价策略
		//获取患者信息
		if(pv!=null&&(!CommonUtils.isEmptyString(pv.getPkPv())||!CommonUtils.isEmptyString(pv.getAgePv()))){
			
			//查询vip病区主键
			List<Map<String,Object>> vipDeptList = DataBaseHelper.queryForList(
					"select ag.pk_dept  from bd_res_pc_argu ag where ag.code_argu='EX0047' and  ag.arguval='1'"
					,new Object[]{});

			//判断年龄加收和儿童加收是否同时生效，0特诊加收不再叠加年龄加收策略，1年龄加收基础上叠加特诊加收策略
			String flagChildSpec = ApplicationUtils.getSysparam("BD0016", false);
			
			for(ItemPriceVo item:newItemPriceList){
				//记费时根据患者的记费病区是否为vip病区进行记费处理 true:按照vip记费 false:按照非vip记费
				boolean flag = false;
				if(!CommonUtils.isEmptyString(pv.getEuPvtype()) && EnumerateParameter.THREE.equals(pv.getEuPvtype())){
					flag = isSpecCharge(vipDeptList,CommonUtils.isEmptyString(item.getPkDeptApp())?pv.getPkDept():item.getPkDeptApp());
				}else{
					flag = true;//除住院，其他就诊方式不判断vip病区
				}
				if(!flag || !BlcgUtil.converToTrueOrFalse(pv.getFlagSpec()))
					item.setRatioSpec(0D);
				if(!(new BigDecimal(6).compareTo(age)>0))
					item.setRatioChildren(0D);

				if(CommonUtils.isEmptyString(flagChildSpec) || !EnumerateParameter.ONE.equals(flagChildSpec)){
					/**特诊就诊模式和年龄小于6岁只允许有一种情况(特诊就诊模式优先)*/
					//flag && BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())
					if(flag && BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())){
						//获取特诊加收模式  0：比例	  1：金额
						if(!CommonUtils.isEmptyString(item.getEuSpmode())){
							if(EnumerateParameter.ZERO.equals(item.getEuSpmode()) && item.getRatioSpec()!=null){
								item.setPriceCs(MathUtils.mul(item.getPriceOrg(), MathUtils.add(1D, item.getRatioSpec())));
							}else if(EnumerateParameter.ONE.equals(item.getEuSpmode()) && item.getAmountSpec()!=null){
								item.setPriceCs(MathUtils.add(item.getPriceOrg(), item.getAmountSpec()));
							}
							item.setPriceOrg(item.getPriceCs());
						}
					}else if(new BigDecimal(6).compareTo(age)>0){	//年龄小于6岁 price= price*(1+ratio_children)
						//获取儿童加收模式  0：比例	  1：金额
						if(!CommonUtils.isEmptyString(item.getEuCdmode())){
							if(EnumerateParameter.ZERO.equals(item.getEuCdmode()) && item.getRatioChildren()!=null){
								item.setPriceCs(MathUtils.mul(item.getPriceOrg(), MathUtils.add(1D, item.getRatioChildren())));
							}else if(EnumerateParameter.ONE.equals(item.getEuCdmode()) && item.getAmountChildren()!=null){
								item.setPriceCs(MathUtils.add(item.getPriceOrg(), item.getAmountChildren()));
							}else if(EnumerateParameter.TWO.equals(item.getEuCdmode()) && item.getAmountChildren()!=null){
								item.setPriceCs(item.getAmountChildren());
							}
							item.setPriceOrg(item.getPriceCs());
						}
					}
				} else {
					/*参数值=1，在年龄价格基础上叠加特诊加收策略，执行以下处理逻辑：
						1）根据患者年龄计算收费项目单价；
						   结算年龄：当前日期-出生日期，单位为岁，精确到两位小数；
						   如果患者年龄小于6，根据年龄加收模式eu_cdmode获取单价
						加收模式eu_cdmode='0'，返回单价=price*(1+ratio_children)
						加收模式eu_cdmode='1'，返回单价=price+amount_children
						加收模式eu_cdmode='2'，返回单价=amount_children
						2）判断特诊标志：如果是患者年龄小于6且特诊标志为1，判断特诊加收模式eu_spmode获取单价
						加收模式eu_spmode='0'，返回单价price=price_children*(1+ratio_spec)
						加收模式eu_spmode='1'，返回单价price=price_children+amount_spec
						如果患者年龄不小于6且特诊标志为1，则判断特诊加收模式eu_spmode获取单价
						加收模式eu_spmode='0'，返回单价price=price*(1+ratio_spec)
						加收模式eu_spmode='1'，返回单价price=price+amount_spec
					*/

					if(new BigDecimal(6).compareTo(age)>0){	//年龄小于6岁 price= price*(1+ratio_children)
						//获取儿童加收模式  0：比例	  1：金额
						if(!CommonUtils.isEmptyString(item.getEuCdmode())){
							if(EnumerateParameter.ZERO.equals(item.getEuCdmode()) && item.getRatioChildren()!=null){
								item.setPriceCs(MathUtils.mul(item.getPriceOrg(), MathUtils.add(1D, item.getRatioChildren())));
							}else if(EnumerateParameter.ONE.equals(item.getEuCdmode()) && item.getAmountChildren()!=null){
								item.setPriceCs(MathUtils.add(item.getPriceOrg(), item.getAmountChildren()));
							} else if(EnumerateParameter.TWO.equals(item.getEuCdmode()) && item.getAmountChildren()!=null){
								item.setPriceCs(item.getAmountChildren());
							}
							item.setPriceOrg(item.getPriceCs());
						}
					}

					//增加医嘱打折处理逻辑：bd_ord_item表ratio_self
					if(item.getRatioSelf()!=null && item.getRatioSelf().compareTo(Double.valueOf(0d))>=0 && item.getRatioSelf().compareTo(Double.valueOf(1d))<1){
						item.setPriceCs(MathUtils.mul(item.getPriceOrg(), item.getRatioSelf()));
						item.setAmountPock(MathUtils.sub(item.getPriceOrg(), item.getPriceCs()));//自付比例打折金额；
						item.setRatioPock(item.getRatioSelf());
						item.setPriceOrg(item.getPriceCs());
					}

					//校验是否为特诊患者，特诊患者时更新原始单价为儿童加收后的价格
					if(flag && BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())){
						//获取特诊加收模式  0：比例	  1：金额
						if(!CommonUtils.isEmptyString(item.getEuSpmode())){
							if(EnumerateParameter.ZERO.equals(item.getEuSpmode()) && item.getRatioSpec()!=null){
								item.setPriceCs(MathUtils.mul(item.getPriceOrg(), MathUtils.add(1D, item.getRatioSpec())));
							}else if(EnumerateParameter.ONE.equals(item.getEuSpmode()) && item.getAmountSpec()!=null){
								item.setPriceCs(MathUtils.add(item.getPriceOrg(), item.getAmountSpec()));
							}
							item.setPriceOrg(item.getPriceCs());
						}
					}
					
					
				}


			}
			
		}
		
		
	    return newItemPriceList;
	}
	/**
	 * 按项目收费策略计算收费项目价格 --废弃
	 * @param pv 就诊信息
	 * @param pkOrg
	 * @param itemlist
	 * @return
	 */
	public List<ItemPriceVo> getItemPriceByChap(PvEncounter pv,String pkOrg,List<ItemPriceVo> itemlist){
		if(pv == null ||CommonUtils.isEmptyString(pv.getPkPi())||CommonUtils.isEmptyString(pv.getEuPvmode()))
			throw new BusException("调用项目收费策略时，未获取到就诊类型[euPvmode]或患者主键[pkPi]，无法完成收费项目价格计算！");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		//1.根据患者年龄计算收费项目单价
		//1.1获取患者年龄
		BigDecimal  age = DataBaseHelper.queryForScalar("select (to_date(?,'YYYYMMDDHH24MISS') - birth_date) from pi_master where pk_pi = ?", BigDecimal.class, new Object[]{DateUtils.getDefaultDateFormat().format(new Date()),pv.getPkPi()});
		//日期相减返回天数，转换为年
		age = age.divide(new BigDecimal(365),2,BigDecimal.ROUND_HALF_UP); //四舍五入保留两位小数
		//设置数据库类型
		paramMap.put("dataBaseType",Application.isSqlServer()?"sqlserver":"oracle");
		paramMap.put("dtChaptype", "01");
		paramMap.put("pkOrg", pkOrg);
		paramMap.put("valBegin",age);
		paramMap.put("valEnd", age);
		//1.2批量获取年龄模式下所有收费项目
		List<BdChap> chaplist1 = cgStrategyPubMapper.queryBdChap(paramMap);
		//1.3计算单价
        if(chaplist1 != null&&chaplist1.size()>0){
        	for(ItemPriceVo item:itemlist){
        		for(BdChap chap:chaplist1){
        		   if(item.getPkPv().equals(pv.getPkPv())&&chap.getPkItem().equals(item.getPkItem())){
        			   if("0".equals(chap.getEuCalcmode())){//比例
        				   //计算方式为比例时，年龄策略单价=原始单价*(1+比例*方向)；
        				   item.setPriceCs(MathUtils.mul(item.getPriceOrg(),MathUtils.add(1D, MathUtils.mul(chap.getRate(), Double.parseDouble(chap.getEuDirect())))));
        			   }else if("1".equals(chap.getEuCalcmode())){//金额
        				   //计算方式为金额时，年龄策略单价=原始单价+金额*方向；
        				   item.setPriceCs(MathUtils.add(item.getPriceOrg(),MathUtils.mul(chap.getAmount(),Double.parseDouble(chap.getEuDirect()))));
        			   }
        			   break;
        		   }
        		}
        	}
        }
		//2.根据患者就诊模式计算收费项目单价
        paramMap.put("dtChaptype", "02");
		paramMap.put("valBegin", pv.getEuPvmode());
		paramMap.put("valEnd", pv.getEuPvmode());
		//2.1批量获取就诊模式下所有收费项目
		List<BdChap> chaplist2 = cgStrategyPubMapper.queryBdChap(paramMap);
		if(chaplist2 != null&&chaplist2.size()>0){
        	for(ItemPriceVo item:itemlist){
        		for(BdChap chap:chaplist2){
        		   if(item.getPkPv().equals(pv.getPkPv())&&chap.getPkItem().equals(item.getPkItem())){
        			   if("0".equals(chap.getEuCalcmode())){//比例
        				 //计算方式为比例时，就诊模式策略单价=年龄策略单价*(1+比例*方向)；
        				   item.setPriceCs(MathUtils.mul(item.getPriceCs(),MathUtils.add(1D, MathUtils.mul(chap.getRate(), Double.parseDouble(chap.getEuDirect())))));
        			   }else if("1".equals(chap.getEuCalcmode())){//金额
        				 //计算方式为金额时，就诊模式策略单价=年龄策略单价+金额*方向。
        				   item.setPriceCs(MathUtils.add(item.getPriceCs(),MathUtils.mul(chap.getAmount(),Double.parseDouble(chap.getEuDirect()))));
        			   }
        			   break;
        		   }
        		}
        	}
        }
		return itemlist;
	}
	
	
	/**
	 * 按医保项目记费策略计算收费项目价格
	 * @param hp
	 * @param itemlist
	 * @return
	 */
	public List<ItemPriceVo> getItemPriceByCgDiv(BdHp hp,PvEncounter pv,List<ItemPriceVo> itemlist){
		if(itemlist == null||itemlist.size()<=0)
			return Lists.newArrayList();
		//判断是否适应症用药，如果为否（0），按自费处理
		List<ItemPriceVo> zFList = new ArrayList<>();
		for(int i =itemlist.size() - 1; i >= 0; i--){
			if(BlcgUtil.converToTrueOrFalse(itemlist.get(i).getFlagPd())){ //必需是药品
				if(CommonUtils.isEmptyString(itemlist.get(i).getDescFit()))
					itemlist.get(i).setFlagFit("9");
				else if(!CommonUtils.isEmptyString(itemlist.get(i).getDescFit()) 
						&& EnumerateParameter.ZERO.equals(itemlist.get(i).getFlagFit()))
					itemlist.get(i).setFlagFit("0");
				else if(!CommonUtils.isEmptyString(itemlist.get(i).getDescFit()) 
						&& EnumerateParameter.ONE.equals(itemlist.get(i).getFlagFit()))
					itemlist.get(i).setFlagFit("1");
				
				if(EnumerateParameter.ZERO.equals(itemlist.get(i).getFlagFit())){
					//非适应症用药按自费处理
					itemlist.get(i).setRatioSelf(1D);
					itemlist.get(i).setPrice(itemlist.get(i).getPriceCs());
					//把自费的项目添加到zFList,不需要再走医保记费策略。
					zFList.add(itemlist.get(i));
					itemlist.remove(i);
				}
			}
			
		}
		
		//医保类型为null 或者为 00时，调用医保记费策略
//		if(hp==null||(hp.getDtExthp()!=null&&!"00".equals(hp.getDtExthp())))
//			return itemlist;
		if(CommonUtils.isEmptyString(hp.getPkHp()))
			throw new BusException("调用医保记费策略时，未获取到医保主键");
		if(CommonUtils.isEmptyString(pv.getEuPvtype()))
			throw new BusException("调用医保记费策略时，未获取到患者就诊类型");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkHp", hp.getPkHp());
		paramMap.put("euPvtype", pv.getEuPvtype());
		List<Map<String,Object>> divitemlist = cgStrategyPubMapper.queryHpCgDivItem(paramMap);
		List<Map<String,Object>> divitemcatelist = cgStrategyPubMapper.queryHpCgDivItemcate(paramMap);
		for(ItemPriceVo item:itemlist){
			if(divitemlist!=null&&divitemlist.size()>0){
				boolean hasItemFlag = false;
				for(Map<String,Object> divitemMap : divitemlist){
					if(divitemMap==null) continue;
					String pkItem = CommonUtils.getString(divitemMap.get("pkItem"));
					if(item.getPkItem().equals(pkItem)){
						item.setMaxQuan(MapUtils.getDoubleValue(divitemMap,"maxQuan",0));
						item.setPkPayer(MapUtils.getString(divitemMap,"pkPayer",item.getPkPayer()));
						//String dtHptype = CommonUtils.getString("dtHptype");
						String euDivide =  CommonUtils.getString(divitemMap.get("euDivide"));
						if(!CommonUtils.isEmptyString(euDivide)){
							//if("1".equals(dtHptype)||"0".equals(dtHptype)){
							switch(euDivide){
							case "0":
								item.setPrice(CommonUtils.getDouble((BigDecimal)divitemMap.get("amount")));
								item.setRatioSelf(1D);
								break;
							case "1":
								item.setPrice(item.getPriceCs());
								item.setRatioSelf(CommonUtils.getDouble((BigDecimal)divitemMap.get("rate")));
								break;
							case "2":
								item.setPrice(item.getPriceCs());
								setRateByEuPvtype(pv.getEuPvtype(),divitemMap,item,false);
								break;
							case "3":
								item.setPrice(item.getPriceCs());
								setRateByEuPvtype(pv.getEuPvtype(),divitemMap,item,false);
								item.setRatioSelf(MathUtils.mul(CommonUtils.getDouble((BigDecimal)divitemMap.get("rate")), item.getRatioSelf()));
							default:break;
							}
						}else{
							item.setRatioSelf(1D);
							item.setPrice(item.getPriceCs());
						}
						//}else if(dtHptype==null||"2".equals(dtHptype)){
							//item.setRatioSelf(1D);
							//item.setPrice(item.getPriceCs());
						//}
						//item.setPkItemcate(CommonUtils.getString("pkItemcate"));
						item.setPkItemcate(CommonUtils.getString(divitemMap.get("pkItemcate")));
						hasItemFlag = true;
						break;
					}
				}
				//未匹配到合适的项目记费策略，则使用项目分类记费策略
				boolean hasItemcateFlag = false;
				if(!hasItemFlag){
					hasItemcateFlag = setDivInfoByItemcate(divitemcatelist,item,pv,false);
					
					//未匹配到合适的项目分类记费策略
					if(!hasItemcateFlag){
						item.setRatioSelf(1D);
						item.setPrice(item.getPriceCs());
					}
				}
			}else{//不存在按项目设置的记费策略，则使用按项目分类记费策略
				if(divitemcatelist==null||divitemcatelist.size()<=0){
					item.setRatioSelf(1D);
					item.setPrice(item.getPriceCs());
				}else{
					boolean hasItemcateFlag = setDivInfoByItemcate(divitemcatelist,item,pv,false);
					//未匹配到合适的项目分类记费策略
					if(!hasItemcateFlag){
						item.setRatioSelf(1D);
						item.setPrice(item.getPriceCs());
					}
				}
			}
			
		}
		//合并zFList和itemlist
		itemlist.addAll(zFList);
		
		return itemlist;
	}
	/**
	 * 按医保项目优惠策略计算收费项目优惠比例
	 * @param hp
	 * @param pv
	 * @param itemlist
	 * @return
	 */
	public List<ItemPriceVo> getItemDiscRatioByCgDiv(BdHp hp,PvEncounter pv,List<ItemPriceVo> itemlist){
		if(itemlist == null||itemlist.size()<=0)
			return null;
		if(hp==null||CommonUtils.isEmptyString(hp.getPkHp()))
			throw new BusException("调用医保记费策略时，未获取到医保主键");
		if(CommonUtils.isEmptyString(pv.getEuPvtype()))
			throw new BusException("调用医保记费策略时，未获取到患者就诊类型");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkHp", hp.getPkHp());
		paramMap.put("euPvtype", pv.getEuPvtype());
		List<Map<String,Object>> divitemlist = cgStrategyPubMapper.queryHpCgDivItem(paramMap);
		List<Map<String,Object>> divitemcatelist = cgStrategyPubMapper.queryHpCgDivItemcate(paramMap);
		for(ItemPriceVo item:itemlist){
			//若外部传入了优惠比例，则忽略医保记费策略设置的优惠比例
			if(item.getRatioDisc()!=null){
				item.setPkDisc("ratiodiscfromothersystem");
				continue;
			}

			if(divitemlist!=null&&divitemlist.size()>0){
				boolean hasItemFlag = false;
				for(Map<String,Object> divitemMap : divitemlist){
					if(divitemMap==null) continue;
					String pkItem = CommonUtils.getString(divitemMap.get("pkItem"));
					if(item.getPkItem().equals(pkItem)){
							String euDivide =  CommonUtils.getString(divitemMap.get("euDivide"));
							item.setPkPayer(MapUtils.getString(divitemMap,"pkPayer",item.getPkPayer()));
							switch(euDivide){
							case "0":
								item.setPrice(CommonUtils.getDouble((BigDecimal)divitemMap.get("amount")));
								item.setRatioDisc(1D);
								break;
							case "1":
								item.setRatioDisc(CommonUtils.getDouble((BigDecimal)divitemMap.get("rate")));
								break;
							case "2":
								setRateByEuPvtype(pv.getEuPvtype(),divitemMap,item,true);
								break;
							case "3":
								setRateByEuPvtype(pv.getEuPvtype(),divitemMap,item,true);
								item.setRatioDisc(MathUtils.mul(CommonUtils.getDouble((BigDecimal)divitemMap.get("rate")), item.getRatioDisc()));
							default:break;
							}
						hasItemFlag = true;
						break;
					}
				}
				//未匹配到合适的项目记费策略，则使用项目分类记费策略
				boolean hasItemcateFlag = false;
				if(!hasItemFlag){
					hasItemcateFlag = setDivInfoByItemcate(divitemcatelist,item,pv,true);
					
					//未匹配到合适的项目分类记费策略
					if(!hasItemcateFlag){
						item.setRatioDisc(1D);
					}
				}
				
			}else{//不存在按项目设置的记费策略，则使用按项目分类记费策略
				if(divitemcatelist==null||divitemcatelist.size()<=0){
					item.setRatioDisc(1D);
				}else{
					boolean hasItemcateFlag = setDivInfoByItemcate(divitemcatelist,item,pv,true);
					//未匹配到合适的项目分类记费策略
					if(!hasItemcateFlag){
						item.setRatioDisc(1D);
					}
				}
			}
			item.setPkDisc(hp.getPkHp());
		}
		return itemlist;
	}
	/**
	 * 过滤医嘱类型(1:科研,0普通)
	 * @param pricelist
	 * @return
	 */
	public Map<String,List<ItemPriceVo>> filterEuOrdtype(List<ItemPriceVo> pricelist){
		List<ItemPriceVo> kypricelist = new ArrayList<ItemPriceVo>();
		List<ItemPriceVo> fkypricelist = new ArrayList<ItemPriceVo>();
		for(ItemPriceVo item:pricelist){
			if("1".equals(item.getEuOrdtype())){
				kypricelist.add(item);
			}else{
				fkypricelist.add(item);
			}
		}
		Map<String,List<ItemPriceVo>>  result= new HashMap<String,List<ItemPriceVo>>();
		result.put("ky", kypricelist);
		result.put("fky", fkypricelist);
		return result;
	}
	
	/**
	 * 按医保结算策略计算收费项目价格
	 * @param hp
	 * @param pkOrg
	 * @param dateHap
	 * @param itemlist
	 * @return
	 */
	public List<ItemPriceVo> getItemPriceByStDiv(BdHp hp,String pkOrg,Date dateHap,List<ItemPriceVo> itemlist){
		//TODO
		return null;
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
		ItemPriceVo tempVo = null;	//临时Vo
		for(ItemPriceVo item:itemPriceList){
			//默认按收费项目为依据,并且医嘱项目为空
			boolean sameFlag = pk.equals(item.getPkItemOld())&&CommonUtils.isEmptyString(item.getPkOrdOld());
			if(isOrd){
				sameFlag = pk.equals(item.getPkOrdOld());
			}
			if(sameFlag&&item.getPkItemOld().equals(item.getPkItem())){
				//非组套,默认按本服务定价，理论上不存在非组套按组套收费的情况
				if(EnumerateParameter.ZERO.equals(item.getFlagSetOld())){
					tempVo = (ItemPriceVo)item.clone();	//为了计算数量时不覆盖原始数量，用临时vo来处理。
					if (EnumerateParameter.ZERO.equals(item.getEuPricemodeOld())) {
						tempVo.setQuan(MathUtils.mul(quan, item.getQuan()));//医嘱开立数量*收费项目设置的数量 
						resultList.add(tempVo);
						//itemPriceList.remove(item);	//删除已经询价的收费项目
						//continue;
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
								tempVo = (ItemPriceVo)child.clone();	//为了计算数量时不覆盖原始数量，用临时vo来处理。
								tempVo.setQuan(MathUtils.mul(quan,child.getQuan()));//变更子项数量为医嘱开立数量*子项数量
								//当组套项目的定价模式为“本服务定价”时，子项目的特诊加收比例=父项目的特诊加收比例。
								tempVo.setRatioSpec(item.getRatioSpec());
								tempVo.setAmountSpec(item.getAmountSpec());
								tempVo.setEuSpmode(item.getEuSpmode());
								resultList.add(tempVo);
							}
						}else{//子项价格与本服务价格不等，需要计算权重
							for(ItemPriceVo child:childList){
								tempVo = (ItemPriceVo)child.clone();	//为了计算数量时不覆盖原始数量，用临时vo来处理。
								Double weight = (new BigDecimal((MathUtils.mul(child.getPriceOrg(), child.getQuan()))).divide(new BigDecimal(sumChildPrice), 4, BigDecimal.ROUND_HALF_UP)).doubleValue();
								tempVo.setPriceOrg(MathUtils.div(MathUtils.mul(item.getPriceOrg(), weight), child.getQuan()));
								tempVo.setPriceCs(tempVo.getPriceOrg());
								tempVo.setQuan(MathUtils.mul(quan, tempVo.getQuan()));//变更子项数量为医嘱开立数量*子项数量
								//当组套项目的定价模式为“本服务定价”时，子项目的特诊加收比例=父项目的特诊加收比例。
								tempVo.setRatioSpec(item.getRatioSpec());
								tempVo.setAmountSpec(item.getAmountSpec());
								tempVo.setEuSpmode(item.getEuSpmode());
								resultList.add(tempVo);
							}
						}
						
						
						
					}else if(EnumerateParameter.ONE.equals(item.getEuPricemodeOld())){//服务套成员合计定价
						for(ItemPriceVo child:childList){
							tempVo = (ItemPriceVo)child.clone();	//为了计算数量时不覆盖原始数量，用临时vo来处理。
							tempVo.setQuan(MathUtils.mul(quan, child.getQuan()));//变更子项数量为医嘱开立数量*子项数量
							resultList.add(tempVo);
						}
					}
				}
				tempVo = null;
			}
			
		}
	     return resultList;
	}
	/**
	 * 根据就诊类型设置优惠比例
	 * @param euPvtype
	 * @param divobj
	 * @param item
	 */
	private void setRateByEuPvtype(String euPvtype,Map<String,Object> divobj,ItemPriceVo item,boolean isDisc){
		switch(euPvtype){
		case IDictCodeConst.EUPVTYPE_IP : 
			if(isDisc){
			  item.setRatioDisc(CommonUtils.getDouble((BigDecimal)divobj.get("rateIp")));
			}else{
			  item.setRatioSelf(CommonUtils.getDouble((BigDecimal)divobj.get("rateIp")));
			}
			break;
		case IDictCodeConst.EUPVTYPE_ER : 
			if(isDisc){
			  item.setRatioDisc(CommonUtils.getDouble((BigDecimal)divobj.get("rateEr")));
			}else{
			  item.setRatioSelf(CommonUtils.getDouble((BigDecimal)divobj.get("rateEr")));
			}
			break;
		case IDictCodeConst.EUPVTYPE_OP : 
			if(isDisc){
			  item.setRatioDisc(CommonUtils.getDouble((BigDecimal)divobj.get("rateOp")));
			}else{
			  item.setRatioSelf(CommonUtils.getDouble((BigDecimal)divobj.get("rateOp")));
			}
			break;
		case IDictCodeConst.EUPVTYPE_PE : 
			if(isDisc){
			  item.setRatioDisc(CommonUtils.getDouble((BigDecimal)divobj.get("ratePe")));
			}else{
			  item.setRatioSelf(CommonUtils.getDouble((BigDecimal)divobj.get("ratePe")));
			}
			break;
		default:break;
		}
		
	}
	/**
	 * 设置按分类记费策略信息
	 * @return
	 */
	private boolean setDivInfoByItemcate(List<Map<String,Object>> divitemcatelist,ItemPriceVo item,PvEncounter pv,boolean isDisc){
		boolean hasFlag = false; 
		for(Map<String,Object> divitemcateMap : divitemcatelist){
			if(divitemcateMap==null) continue;
			String pkItemcate = CommonUtils.getString(divitemcateMap.get("pkItemcate"));
			if(item.getPkItemcate().equals(pkItemcate)){
				item.setPkPayer(MapUtils.getString(divitemcateMap,"pkPayer",item.getPkPayer()));
				String euDivide =  CommonUtils.getString(divitemcateMap.get("euDivide"));
				if(!isDisc){
					item.setPrice(item.getPriceCs());
				}
				switch(euDivide){
				case "1":
					if(isDisc){
						item.setRatioDisc(CommonUtils.getDouble((BigDecimal)divitemcateMap.get("rate")));
					}else{
						item.setRatioSelf(CommonUtils.getDouble((BigDecimal)divitemcateMap.get("rate")));
					}
					break;
				case "2":
					setRateByEuPvtype(pv.getEuPvtype(),divitemcateMap,item,isDisc);
					break;
				case "3":
					setRateByEuPvtype(pv.getEuPvtype(),divitemcateMap,item,isDisc);
					if(isDisc){
						item.setRatioDisc(MathUtils.mul(CommonUtils.getDouble((BigDecimal)divitemcateMap.get("rate")), item.getRatioDisc()));
					}else{
						item.setRatioSelf(MathUtils.mul(CommonUtils.getDouble((BigDecimal)divitemcateMap.get("rate")), item.getRatioSelf()));
					}
				default:break;
				}
				hasFlag = true;
				break;
			}
		}
		return hasFlag;
	}
	
	 /**
     * 获取高值耗材策略信息
     * @param paramMap
     * @return
     */
    public List<BlHvItemStyVo> getHvItemPriceSty(HpVo bdHp,Map<String,Object> paramMap){
    	//读取参数BL0025（材料费编码）  --废弃
    	//paramMap.put("code", ApplicationUtils.getSysparam("BL0025", true));	
    	
    	/**
    	 * 获取高值耗材策略获取BL0033（广州医保省公医上级编码），校验患者医保上级医保编码为省公医或区公医
    	 * */
    	String sysFaCode = ApplicationUtils.getSysparam("BL0033", true);	
    	if(bdHp.getFaCode().equals(sysFaCode))
    		paramMap.put("dtItemtype", "07");	//dt_itemtype like '07%'
    	else
    		paramMap.put("dtItemtype", "0701");	//dt_itemtype='0701'
    	
    	List<BlHvItemStyVo> hvItemList = cgStrategyPubMapper.qryHvItemPriceSty(paramMap);
    	
    	return hvItemList;
    }
    
    /**
	 * 计算自付比例rate_pi
	 */
    public Double calRatePi(String dtHptype,String euDivide,Double amount,Double rateIp,Double rate,Double priceCs){
    	
    	Double ratePi = 0.0;
    	
    	if(CommonUtils.isEmptyString(euDivide)){	//当euDivide=null时
    		ratePi = new Double(1);
    	} else {
    		//计算方式(0自定义价格，1自定义自付比例，2患者自付比例，3患者自付乘以自定义比例)
        	switch (euDivide) {
    			case EnumerateParameter.ZERO:
    				ratePi = MathUtils.div(
    						amount, 
    						priceCs,
    						2);
    				break;
    			case EnumerateParameter.ONE:
    				ratePi = rate;
    				break;
    			case EnumerateParameter.TWO:
    				ratePi = rateIp;
    				break;
    			case EnumerateParameter.THREE:
    				ratePi = MathUtils.mul(rate, rateIp);
    				break;
    			default:
    				break;
    		}
    	}
    	return ratePi;
    }
    
    /**
     * 获取诊查费信息(区公医计费/省公医计费)
     * @param pv
     * @param bdHp
     * @param itemPriceVo
     * @param examPriceList
     * @return
     */
    public BlItemPriceVo qryExamFeeCharge(PvEncounter pv,BdHp bdHp,ItemPriceVo itemPriceVo,List<ExamFeeStyVo> examPriceList){
    	if(examPriceList==null || examPriceList.size()<=0)
    		return null;
    	
    	BlItemPriceVo priceVo = null;
    	
    	for(ExamFeeStyVo itemStyVo : examPriceList){
			if(bdHp.getPkHp().equals(itemStyVo.getPkHp()) &&
				itemPriceVo.getPkItem().equals(itemStyVo.getPkItem())){
				
				priceVo = new BlItemPriceVo();
				
		    	//项目单价
				Double priceCs = itemPriceVo.getPriceOrg();
				
				/**计算自付比例rate_pi*/
				//获取收费项目有效自付比例
				Double ratioSelf = getItemRatioSelf(pv.getEuPvtype(), itemStyVo.getRateIp(), itemStyVo.getRateOp(), itemStyVo.getRateEr(), itemStyVo.getRatePe());
				itemStyVo.setRatePi(
						calRatePi(itemStyVo.getDtHptype(),itemStyVo.getEuDivide(),itemStyVo.getAmount(),ratioSelf,itemStyVo.getRate(),priceCs)
						);
				
				//获取限额
				Double dtquota = 0D;
				Integer euPvtype = CommonUtils.getInteger(pv.getEuPvtype());
				if(euPvtype<3)
					dtquota = 999999D;
				else
					dtquota = itemStyVo.getDtquotaIp();
				
				/**计算床位费策略结果*/
				priceVo.setPrice(priceCs);						//项目单价
	        	priceVo.setPkItemcate(itemStyVo.getPkItemcate());//收费项目分类
	        	priceVo.setEuDivtype("0");	//床位费策略类型

	        	if(priceCs <= dtquota){	
	        		//rate=( price_cs*rate_pi)/price_cs
	        		priceVo.setRate(MathUtils.div(MathUtils.mul(priceCs, itemStyVo.getRatePi()), priceCs, 5));							//自付比例
	        		//amt_hppi=price*rate_pi
	        		priceVo.setAmtHppi(MathUtils.mul(MathUtils.mul(priceVo.getPrice(), itemStyVo.getRatePi()),itemPriceVo.getQuan())); 						//医保自付金额
	        	}else if(priceCs > dtquota){
	        		//自付比例
	        		//[(price_cs-bedquota)+bedquota*rate_pi] / price_cs
	        		priceVo.setRate(
	        				MathUtils.div(
	        						MathUtils.add(
	        								MathUtils.sub(priceCs, dtquota), 
	        								MathUtils.mul(dtquota, itemStyVo.getRatePi())
	        								), 
	    							priceCs, 
	        						5)
	        				);							
	        		//医保自付金额
	        		//(price-bedquato)+bedquota*rate_pi
	        		priceVo.setAmtHppi(MathUtils.mul(
	        				MathUtils.add(
	        						MathUtils.sub(priceVo.getPrice(), dtquota),
	        						MathUtils.mul(dtquota, itemStyVo.getRatePi())
	        						),
    						itemPriceVo.getQuan()
	        				));				
	        	}
	        	break;
			}
		}
    	return priceVo;
    }
    
    /**
     * 获取床位费用信息(区公医计费/省公医计费)
     * @return
     */
    public BlItemPriceVo qryBedCharge(PvEncounter pv,BdHp bdHp,ItemPriceVo itemPriceVo,List<BlBedItemStyVo> bedPriceList){
    	
    	if(bedPriceList==null || bedPriceList.size()<=0)
    		return null;
    	
    	BlItemPriceVo priceVo = null;
    	
    	for(BlBedItemStyVo itemStyVo : bedPriceList){
			if(bdHp.getPkHp().equals(itemStyVo.getPkHp()) &&
				itemPriceVo.getPkItem().equals(itemStyVo.getPkItem())){
				
				priceVo = new BlItemPriceVo();
				
		    	//项目单价
				Double priceCs = itemPriceVo.getPriceOrg();
				
				/**计算自付比例rate_pi*/
				//获取收费项目有效自付比例
				Double ratioSelf = getItemRatioSelf(pv.getEuPvtype(), itemStyVo.getRateIp(), itemStyVo.getRateOp(), itemStyVo.getRateEr(), itemStyVo.getRatePe());
				itemStyVo.setRatePi(
						calRatePi(itemStyVo.getDtHptype(),itemStyVo.getEuDivide(),itemStyVo.getAmount(),ratioSelf,itemStyVo.getRate(),priceCs)
						);
				
				/**计算床位费策略结果*/
				priceVo.setPrice(priceCs);						//项目单价
	        	priceVo.setPkItemcate(itemStyVo.getPkItemcate());//收费项目分类
	        	priceVo.setEuDivtype("0");	//床位费策略类型
	        	
	        	if(priceCs <= itemStyVo.getBedquota()){	
	        		//rate=( price_cs*rate_pi)/price_cs
	        		priceVo.setRate(MathUtils.div(MathUtils.mul(priceCs, itemStyVo.getRatePi()), priceCs, 5));							//自付比例
	        		//amt_hppi=price*rate_pi
	        		priceVo.setAmtHppi(MathUtils.mul(priceVo.getPrice(), itemStyVo.getRatePi())); 						//医保自付金额
	        	}else if(priceCs > itemStyVo.getBedquota()){
	        		//自付比例
	        		//[(price_cs-bedquota)+bedquota*rate_pi] / price_cs
	        		priceVo.setRate(
	        				MathUtils.div(
	        						MathUtils.add(
	        								MathUtils.sub(priceCs, itemStyVo.getBedquota()), 
	        								MathUtils.mul(itemStyVo.getBedquota(), itemStyVo.getRatePi())
	        								), 
	    							priceCs, 
	        						5)
	        				);							
	        		//医保自付金额
	        		//(price-bedquato)+bedquota*rate_pi
	        		priceVo.setAmtHppi(
	        				MathUtils.add(
	        						MathUtils.sub(priceVo.getPrice(), itemStyVo.getBedquota()),
	        						MathUtils.mul(itemStyVo.getBedquota(), itemStyVo.getRatePi())
	        						)
	        				); 						
	        	}
	        	break;
			}
		}
    		
    	return priceVo;
    }
    
    /**
     * 记费前查询是否有床位费，如果有床位费则校验是否在费用发生日期当天记过费，记过费给出提示不可重复记费
     */
    public void qryBedItemIsCharge(PvEncounter pv,List<ItemPriceVo> itemPvList){
    	if(itemPvList==null || itemPvList.size()<=0)
    		return;
    	
    	//查询床位费编码对应的主键
    	String bedCode = ApplicationUtils.getSysparam("BD0001", true);
    	Map<String,Object> pkMap = DataBaseHelper.queryForMap("select pk_itemcate from bd_itemcate where code = ?", bedCode);
    	
		for(ItemPriceVo itemPriceVo : itemPvList){
			if(CommonUtils.isEmptyString(itemPriceVo.getPkCgip()) 
					&& pkMap!=null && pkMap.size()>0
					&& itemPriceVo.getPkItemcate().equals(CommonUtils.getString(pkMap.get("pkItemcate"))))
	    	{
	    		/**限制每天只能发生一次床位费(数量为1)，使用date_hap约束*/
	    		String dateHap = "";
	    		if(itemPriceVo.getDateHap()!=null)
	    			dateHap = DateUtils.getDateStr(itemPriceVo.getDateHap());
	    		else
	    			dateHap = DateUtils.getDateTimeStr(DateUtils.strToDate(DateUtils.getDateTime(),"yyyy-MM-dd"));
	    		
	        	Double count = DataBaseHelper.queryForScalar("select sum(amount) from ins_gzgy_bl bl "+
															  " inner join bd_item item on bl.pk_item = item.pk_item "+
																	  " inner join bd_itemcate cate on  item.pk_itemcate = cate.pk_itemcate and cate.code = ? "+
																			  " where bl.pk_pv = ? and bl.pk_item = ? and bl.date_hap = to_date(?,'YYYYMMDDHH24MISS') "
	        			, Double.class
	        			, new Object[]{bedCode,itemPriceVo.getPkPv(),itemPriceVo.getPkItem(),dateHap});
	    		
	        	//如果count>=1说明今天已经发生过床位费计费,则抛出异常
	        	if(count!=null && count!=0D)
	        	{
	        		String errDate = itemPriceVo.getDateHap()!=null?DateUtils.formatDate(itemPriceVo.getDateHap(), "yyyy-MM-dd"):DateUtils.formatDate(new Date(), "yyyy年MM月dd日");
	            	throw new BusException(pv.getBedNo()+"床患者："+pv.getNamePi()+",收费项目【"+itemPriceVo.getNameCg()+"】在"+errDate+"已经记费，不可重复记费！");
	        	}
	    	}
		}
    	
    }
    
    public BlItemPriceVo qryHvItemCharge(PvEncounter pv,BdHp bdHp,ItemPriceVo itemPriceVo,List<BlHvItemStyVo> hvItemPriceList){
    	
    	if(hvItemPriceList==null || hvItemPriceList.size()<=0)
    		return null;
    	
    	//获取高值耗材策略
    	Map<String,Object> paramMap = new HashMap<>();
    	paramMap.put("pkHp", bdHp.getPkHp());
    	List<BlHvItemStyVo> styVoList = cgStrategyPubMapper.qryHvitemPriceLimit(paramMap);
    	
    	if(styVoList==null || styVoList.size()<=0)
    		return null;
    	
    	BlItemPriceVo priceVo = null;
    	
		//项目单价
		Double priceCs = itemPriceVo.getPriceOrg();
    	
		for(BlHvItemStyVo itemStyVo : hvItemPriceList){
			if(bdHp.getPkHp().equals(itemStyVo.getPkHp()) &&
					itemPriceVo.getPkItem().equals(itemStyVo.getPkItem())){
				
				/**计算自付比例rate_pi*/
				//获取收费项目有效自付比例
				Double ratioSelf = getItemRatioSelf(pv.getEuPvtype(), itemStyVo.getRateIp(), itemStyVo.getRateOp(), itemStyVo.getRateEr(), itemStyVo.getRatePe());
				itemStyVo.setRatePi(
						calRatePi(itemStyVo.getDtHptype(),itemStyVo.getEuDivide(),itemStyVo.getAmount(),ratioSelf,itemStyVo.getRate(),priceCs)
						);
				
				for(BlHvItemStyVo vo : styVoList){
					/**当price_cs>=price_min且price_cs<price_max时*/
					if((priceCs>=vo.getPriceMin()) && 
							(priceCs<vo.getPriceMax())){
						priceVo = new BlItemPriceVo();
						
						/**判断高值耗材收费策略eu_calcmode,计算结果*/
				    	priceVo.setPrice(priceCs); 						//项目单价
						priceVo.setPkItemcate(itemStyVo.getPkItemcate()); 	//收费项目分类
						priceVo.setEuDivtype("2");	//高值耗材策略类型
						
						if(EnumerateParameter.ZERO.equals(vo.getEuCalcmode())){
				    		//rate=[price_cs*ratio_init+price_cs*(1-ratio_init)*rate_pi]/price_cs
				    		priceVo.setRate(
				    				MathUtils.div(
				    						MathUtils.add(
				    								MathUtils.mul(priceCs, vo.getRatioInit()),
				    								MathUtils.mul(
				    					    				MathUtils.mul(priceCs,MathUtils.sub(1.0,vo.getRatioInit())), 
				    					    				itemStyVo.getRatePi()
				    					    				)
							    					),
				    						priceCs,
				    						5)
				    				);
				    		//amt_hppi=[price_cs*ratio_init+price_cs*(1-ratio_init)*rate_pi]*quan
				    		priceVo.setAmtHppi(
				    				MathUtils.mul(
				    						MathUtils.add(
				    								MathUtils.mul(priceCs, vo.getRatioInit()),
				    								MathUtils.mul(
				    										MathUtils.mul(priceCs, MathUtils.sub(1.0,vo.getRatioInit())),
				    										itemStyVo.getRatePi()
				    										)
													),
											itemPriceVo.getQuan()
				    						)
				    				);
				    	}else if(EnumerateParameter.ONE.equals(vo.getEuCalcmode())){
				    		//rate=[price_cs*ratio_init+price_cs*(1-ratio_init)*ratio]/price_cs
				    		priceVo.setRate(
				    				MathUtils.div(
				    						MathUtils.add(
				    								MathUtils.mul(priceCs, vo.getRatioInit()),
				    								MathUtils.mul(
				    					    				MathUtils.mul(priceCs,MathUtils.sub(1.0,vo.getRatioInit())), 
				    					    				vo.getRatio()
				    					    				)
							    					),
				    						priceCs,
				    						5)
				    				);
				    		//amt_hppi=[price_cs*ratio_init+price_cs*(1-ratio_init)*ratio]*quan
				    		priceVo.setAmtHppi(
				    				MathUtils.mul(
				    						MathUtils.add(
				    								MathUtils.mul(priceCs, vo.getRatioInit()),
				    								MathUtils.mul(
				    										MathUtils.mul(priceCs, MathUtils.sub(1.0,vo.getRatioInit())),
				    										vo.getRatio()
				    										)
													),
											itemPriceVo.getQuan()
				    						)
				    				);
				    	}
						break;
					}
				}
				break;
			}
		}
    	return priceVo;
    }
    
    /**
     * 获取特殊项目费用信息(区公医计费/省公医计费)
     * @param bdHp
     * @param itemPriceVo
     * @param spPriceList
     * @return
     */
    public BlItemPriceVo qrySpitemCharge(PvEncounter pv,BdHp bdHp,ItemPriceVo itemPriceVo,List<BlSpItemStyVo> spPriceList){    
    	
    	if(spPriceList==null || spPriceList.size()<=0)
    		return null;
    	
    	BlItemPriceVo priceVo = null;
    	
    	//项目单价
    	Double priceCs = itemPriceVo.getPriceOrg();
    	
    	for(BlSpItemStyVo itemStyVo : spPriceList){
    		if(bdHp.getPkHp().equals(itemStyVo.getPkHp()) &&
    				itemPriceVo.getPkItem().equals(itemStyVo.getPkItem())){
    			
    			priceVo = new BlItemPriceVo();
    			
    			/**计算自付比例rate_pi*/
    			//获取收费项目有效自付比例
				Double ratioSelf = getItemRatioSelf(pv.getEuPvtype(), itemStyVo.getRateIp(), itemStyVo.getRateOp(), itemStyVo.getRateEr(), itemStyVo.getRatePe());
				itemStyVo.setRatePi(
						calRatePi(itemStyVo.getDtHptype(),itemStyVo.getEuDivide(),itemStyVo.getAmount(),ratioSelf,itemStyVo.getRate(),priceCs)
						);
				
				/**判断特殊项目收费策略eu_calcmode,计算结果*/
		    	priceVo.setPrice(priceCs); 						//项目单价
				priceVo.setPkItemcate(itemStyVo.getPkItemcate()); 	//收费项目分类
				priceVo.setEuDivtype("1");	//特殊项目策略类型
				
		    	if(EnumerateParameter.ZERO.equals(itemStyVo.getEuCalcmode())){
		    		if(priceCs > itemStyVo.getAmountMax()){
		    			//rate=((price_cs*ratio_init-amount_max)+(price_cs*(1-ratio_init)*rate_pi)+(amount_max*rate_pi))/price_cs
		    			priceVo.setRate(
		    					MathUtils.div(
		    							MathUtils.add(
		    									MathUtils.add(
		    											MathUtils.sub(MathUtils.mul(priceCs,itemStyVo.getRatioInit()),itemStyVo.getAmountMax()),
		    											MathUtils.mul(
		    													MathUtils.mul(priceCs,MathUtils.sub(1.0, itemStyVo.getRatioInit())),
		    													itemStyVo.getRatePi()
		    													)
		    											),
		    									MathUtils.mul(itemStyVo.getAmountMax(), itemStyVo.getRatePi())
		    									), 
		    							priceCs,
		    							5)
		    					);
		    			//amt_hppi=((price_cs*ratio_init-amount_max)+(price_cs*(1-ratio_init)*rate_pi)+(amount_max*rate_pi))*quan
		    			priceVo.setAmtHppi(
		    					MathUtils.mul(
		    							MathUtils.add(
		    									MathUtils.add(
		    											MathUtils.sub(MathUtils.mul(priceCs,itemStyVo.getRatioInit()), itemStyVo.getAmountMax()),
		    											MathUtils.mul(
		    													MathUtils.mul(priceCs,MathUtils.sub(1.0,itemStyVo.getRatioInit())),
		    													itemStyVo.getRatePi()
		    													)
		    											),
		    									MathUtils.mul(itemStyVo.getAmountMax(),itemStyVo.getRatePi())
		    									), 
		    							itemPriceVo.getQuan()
		    							)
		    					);
		    		} else if(priceCs <= itemStyVo.getAmountMax()){
		    			//rate=(price_cs*rate_pi)/price_cs
		    			priceVo.setRate(
		    					MathUtils.div(
		    							MathUtils.mul(priceCs, itemStyVo.getRatePi()), 
		    							priceCs,
		    							5
		    							)
		    					);
		    			//amt_hppi=price_cs*rate_pi*quan
		    			priceVo.setAmtHppi(
		    					MathUtils.mul(
		    							MathUtils.mul(priceCs, itemStyVo.getRatePi()), 
		    							itemPriceVo.getQuan()
		    							)
		    					);
		    		}
		    		
		    	}else if(EnumerateParameter.ONE.equals(itemStyVo.getEuCalcmode())){
		    		if(priceCs > itemStyVo.getAmountMax()){
		    			//rate=((price_cs*ratio_init-amount_max)+(price_cs*(1-ratio_init)*ratio)+(amount_max*ratio))/price_cs
		    			priceVo.setRate(
		    					MathUtils.div(
		    							MathUtils.add(
		    									MathUtils.add(
		    											MathUtils.sub(MathUtils.mul(priceCs,itemStyVo.getRatioInit()),itemStyVo.getAmountMax()),
		    											MathUtils.mul(
		    													MathUtils.mul(priceCs,MathUtils.sub(1.0, itemStyVo.getRatioInit())),
		    													itemStyVo.getRatio()
		    													)
		    											),
		    									MathUtils.mul(itemStyVo.getAmountMax(), itemStyVo.getRatio())
		    									), 
		    							priceCs,
		    							5)
		    					);
		    			//amt_pi=[(price_cs-amount_max) +amount_max*ratio]*quan
		    			//amt_hppi=((price_cs*ratio_init-amount_max)+(price_cs*(1-ratio_init)*ratio)+(amount_max*ratio))*quan
		    			priceVo.setAmtHppi(
		    					MathUtils.mul(
		    							MathUtils.add(
		    									MathUtils.add(
		    											MathUtils.sub(MathUtils.mul(priceCs,itemStyVo.getRatioInit()), itemStyVo.getAmountMax()),
		    											MathUtils.mul(
		    													MathUtils.mul(priceCs,MathUtils.sub(1.0,itemStyVo.getRatioInit())),
		    													itemStyVo.getRatio()
		    													)
		    											),
		    									MathUtils.mul(itemStyVo.getAmountMax(),itemStyVo.getRatio())
		    									), 
		    							itemPriceVo.getQuan()
		    							)
		    					);
		    		} else if(priceCs<=itemStyVo.getAmountMax()){
		    			//rate=(price_cs*ratio)/price_cs
		    			priceVo.setRate(
		    					MathUtils.div(
		    							MathUtils.mul(priceCs, itemStyVo.getRatio()), 
		    							priceCs, 
		    							5
		    							)
		    					);
		    			//amt_hppi=price_cs*ratio*quan
		    			priceVo.setAmtHppi(
		    					MathUtils.mul(
		    							MathUtils.mul(priceCs, itemStyVo.getRatio()), 
		    							itemPriceVo.getQuan()
		    							)
		    					);
		    		}
		    		
		    	}
		    	break;
    		}
    	}
    	return priceVo;
    	
    }
    /**
	 * 设置药品价格
	 * @param pdList
	 * @param storeMap
	 * @return
	 */
	public List<ItemPriceVo> getPdPrice(List<BlPubParamVo> pdList,Map<String,BlCgPdParamVo> storeMap){
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
							pdPricevo.setPkItem(pdOutDtParamVo.getPkPd());
							pdPricevo.setSpec(pdOutDtParamVo.getSpec());
							pdPricevo.setNameCg(pdOutDtParamVo.getName());
							pdPricevo.setPrice(pdOutDtParamVo.getPriceStore());
							pdPricevo.setPkUnit(pdOutDtParamVo.getPkUnitPack());
							pdPricevo.setPkUnitPd(pdOutDtParamVo.getPkUnitPack());
							pdPricevo.setPackSize(pdOutDtParamVo.getPackSize());
							//数量转换为仓库单位对应数量
							pdPricevo.setQuan(MathUtils.div(MathUtils.mul(co.getQuanCg(),CommonUtils.getDouble(co.getPackSize())),CommonUtils.getDouble(pdPricevo.getPackSize())));
							pdPricevo.setPriceOrg(pdOutDtParamVo.getPriceStore());
							pdPricevo.setPriceCs(pdOutDtParamVo.getPriceStore());
							pdPricevo.setBatchNo(pdOutDtParamVo.getBatchNo());
							pdPricevo.setDateExpire(pdOutDtParamVo.getDateExpire());
							pdPricevo.setPriceCost(MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPriceCost(), CommonUtils.getDouble(pdOutDtParamVo.getPackSizePd())),CommonUtils.getDouble(pdOutDtParamVo.getPackSize())));
							pdPricevo.setPkDeptAreaapp(co.getPkDeptAreaapp());
							//外部传入了优惠比例，则以外部传入为准
							if(co.getRatioDisc()!=null)
								pdPricevo.setRatioDisc(co.getRatioDisc());
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
	 * 检查医嘱特殊处理
	 * 1.合并同组检查医嘱相同收费项目
	 * @param priceList
	 */
	public void filterExOrdItem(List<ItemPriceVo> priceList){
		if(priceList==null || priceList.size()<0)
			return;

		for(int i =priceList.size() - 1; i >= 0; i--){
			if(CommonUtils.isEmptyString(priceList.get(i).getPkCnord())
					||CommonUtils.isEmptyString(priceList.get(i).getCodeOrdtype())
					||!"02".equals(priceList.get(i).getCodeOrdtype().substring(0, 2))){		//判断code_ordtype值，以“02”开头执行以下逻辑，否则不做处理)
				continue;
			}
			//查询同组医嘱关联的收费项目
			List<ExOrdItemVo> list = cgStrategyPubMapper.qryExItem(priceList.get(i).getOrdsnParent());

			for(int j =priceList.size() - 1; j >= 0; j--){
				if(CommonUtils.isEmptyString(priceList.get(j).getPkCnord())
						|| CommonUtils.isEmptyString(priceList.get(j).getCodeOrdtype())
						|| !"02".equals(priceList.get(j).getCodeOrdtype().substring(0, 2))	//判断code_ordtype值，以“02”开头执行以下逻辑，否则不做处理)
						|| priceList.get(i).getPkCnord().equals(priceList.get(j).getPkCnord())
						|| !priceList.get(i).getOrdsnParent().equals(priceList.get(j).getOrdsnParent())){
					continue;
				}
				//比较两组医嘱下的收费项目主键是否相同
				if(priceList.get(i).getPkItem().equals(priceList.get(j).getPkItem())){
					//检验两个收费项目“参与合并”标志是否为true
					if(isUnion(list, priceList.get(i), priceList.get(j))){
						//比较收费项目数量，删除值小的项目记录，如果值相同，删除ordsn值大的记录
						if(Double.compare(priceList.get(i).getQuan(), priceList.get(j).getQuan())==1)
							priceList.remove(priceList.get(j));
						else if(Double.compare(priceList.get(i).getQuan(), priceList.get(j).getQuan())==-1)
							priceList.remove(priceList.get(i));
						else if(Double.compare(priceList.get(i).getQuan(), priceList.get(j).getQuan())==0){
							//如果值相同，删除ordsn值大的记录
							if(priceList.get(i).getOrdsn()>priceList.get(j).getOrdsn())
								priceList.remove(priceList.get(i));
							else
								priceList.remove(priceList.get(j));
						}
					}
					break;
				}

			}
		}
	}

	/**
	 * 门诊检查医嘱特殊处理
	 * 1.合并同组检查医嘱相同收费项目
	 * @param priceList
	 */
	public void filterOpExOrdItem(List<ItemPriceVo> priceList){
		if(priceList==null || priceList.size()<0)
			return;

		ICgLabService cgLabService = ServiceLocator.getInstance()
				.getBean(ApplicationUtils.getPropertyValue("cg.processLabClass","defaultCgLabService"), ICgLabService.class);
		cgLabService.filterExOrdItem(priceList);
	}
	
	/**
	 * 合并同组医嘱下相同收费项目
	 * @param priceList		收费项目集合
	 * @param exOrdItemVo	同组医嘱关联的收费项目
	 * @param oItemVo		比较医嘱01
	 * @param tItemVo		比较医嘱02
	 */
	private void hbExOrdItem(List<ItemPriceVo> priceList,List<ExOrdItemVo> exOrdItemVo,ItemPriceVo oItemVo,ItemPriceVo tItemVo){
		for(int i =priceList.size() - 1; i >= 0; i--){
			if(CommonUtils.isEmptyString(priceList.get(i).getPkCnord()) 
					|| !oItemVo.getPkCnord().equals(priceList.get(i).getPkCnord()))
				continue;
			
			for(int j =priceList.size() - 1; j >= 0; j--){
				if(CommonUtils.isEmptyString(priceList.get(j).getPkCnord())
						|| !tItemVo.getPkCnord().equals(priceList.get(j).getPkCnord()))
					continue;
				//比较两组医嘱下的收费项目主键是否相同
				if(priceList.get(i).getPkItem().equals(priceList.get(j).getPkItem())){
					//检验两个收费项目“参与合并”标志是否为true
					if(isUnion(exOrdItemVo, priceList.get(i), priceList.get(j))){
						//比较收费项目数量，删除值小的项目记录，如果值相同，删除ordsn值大的记录
						if(priceList.get(i).getQuan()>priceList.get(j).getQuan())
							priceList.remove(priceList.get(j));
						else if(priceList.get(i).getQuan()<priceList.get(j).getQuan())
							priceList.remove(priceList.get(i));
						else if(priceList.get(i).getQuan()==priceList.get(j).getQuan()){
							//如果值相同，删除ordsn值大的记录
							if(priceList.get(i).getOrdsn()>priceList.get(j).getOrdsn())
								priceList.remove(priceList.get(i));
							else
								priceList.remove(priceList.get(j));
						}
					}
					break;
				}
				
			}
		}
	}
	
	/**
	 * 检验两个收费项目“参与合并”标志是否为true
	 * @param exOrdItemVo	同组医嘱关联的收费项目集合
	 * @param oPriceVo		收费项目集合01
	 * @param tPriceVo		收费项目集合02
	 * @return
	 */
	private boolean isUnion(List<ExOrdItemVo> exOrdItemVo,ItemPriceVo oPriceVo,ItemPriceVo tPriceVo){
		boolean oFlagUnion = false;
		boolean tFlagUnion = false;
		
		for(ExOrdItemVo vo : exOrdItemVo){
			if(vo.getPkCnord().equals(oPriceVo.getPkCnord()) 
					&& vo.getPkItem().equals(oPriceVo.getPkItem())
					&& BlcgUtil.converToTrueOrFalse(vo.getFlagUnion()))
				oFlagUnion = true;
			
			if(vo.getPkCnord().equals(tPriceVo.getPkCnord())
					&& vo.getPkItem().equals(tPriceVo.getPkItem())
					&& BlcgUtil.converToTrueOrFalse(vo.getFlagUnion()))
				tFlagUnion = true;
		}
		
		//两个相同的收费项目合并标志必需都为true才可以合并
		if(oFlagUnion && tFlagUnion)
			return true;
		else 
			return false;
	}
	
	public List<ItemPriceVo> filterOrdItem(List<ItemPriceVo> priceList,List<ItemPriceVo> itemList){
		if(priceList==null || priceList.size()<=0) 
			return priceList;
		
		ICgLabService cgLabService = ServiceLocator.getInstance()
				.getBean(ApplicationUtils.getPropertyValue("cg.processLabClass","defaultCgLabService"), ICgLabService.class);
		priceList = cgLabService.filterOrdItem(priceList,itemList);
		return priceList;
	}

	public List<InsGzgyBl> constructInsGzgyBl(List<BlIpDt> blIpList){
		List<InsGzgyBl> gyBlList = new ArrayList<>();
		for(BlIpDt dt : blIpList){
			InsGzgyBl gyBl = new InsGzgyBl();
			gyBl.setPkGzgybl(NHISUUID.getKeyId());
			gyBl.setPkCnord(dt.getPkCnord());		 //关联医嘱主键
			gyBl.setPkCg(dt.getPkCgip()); 	//关联的记费(bl_ip_dt)主键
			gyBl.setPkPv(dt.getPkPv()); 	//关联的患者就诊
			gyBl.setEuItemtype(dt.getFlagPd());		 //0非药品，1药品
			gyBl.setPkItem(dt.getPkItem());	//关联的收费项目或药品项目主键
			gyBl.setPkItemcate(dt.getPkItemcate());  //关联的收费项目分类
			gyBl.setNameCg(dt.getNameCg());	//收费名称
			gyBl.setSpec(dt.getSpec()); 	//规格
			gyBl.setPrice(dt.getPrice()); 	//单价
			gyBl.setQuan(dt.getQuan()); 	//数量
			gyBl.setAmount(dt.getAmount()); //总金额
			gyBl.setRatio(dt.getRatioSelf());		//患者自付比例
			gyBl.setAmountPi(dt.getAmountHppi()); 	//患者自付金额
			gyBl.setAmountHp(MathUtils.sub(gyBl.getAmount(), gyBl.getAmountPi()));	//医保支付金额
			gyBl.setAmountUnit(new Double(0)); 		//单位支付金额
			//获取当前时间yyyy-MM-dd
			Date dateHap = DateUtils.strToDate(DateUtils.getDateTime(),"yyyy-MM-dd");
			gyBl.setDateHap(dateHap);		//费用发生日期
			gyBl.setDateCg(new Date()); 	//记费日期
			
			gyBlList.add(gyBl);
		}
		
		return gyBlList;
	}
	
	
	/**
	 * 校验患者就诊状态，判断是否可以记退费
	 * @param pkPv
	 * @param logger
	 * @return true:可记费 false:不可记费
	 */
	public boolean checkPvStatus(String pkPv,Logger logger){
		Map<String,Object> pv = cgStrategyPubMapper.qryPvStatus(pkPv);
		//是否可记费标志true:可记费 false:不可记费
		boolean isCg = true;
		if(pv!=null && pv.size()>0){
			//eu_status=0,不能做记退费处理!
			if(EnumerateParameter.ZERO.equals(CommonUtils.getString(pv.get("euStatus")))){
				isCg = false;
				logger.info("患者【"+CommonUtils.getString(pv.get("namePi"))+"】未就诊，不能做记退费处理!");
				//throw new BusException("患者【"+CommonUtils.getString(pv.get("namePi"))+"】未就诊，不能做记退费处理!");
			}
			//eu_status=3,不能做记退费处理!
			if(EnumerateParameter.THREE.equals(CommonUtils.getString(pv.get("euStatus")))){
				isCg = false;
				logger.info("患者【"+CommonUtils.getString(pv.get("namePi"))+"】已出院，不能做记退费处理!");
				//throw new BusException("患者【"+CommonUtils.getString(pv.get("namePi"))+"】已出院，不能做记退费处理!");
			}
			//eu_status=2，flag_frozen=1，患者就诊被冻结，不能做记退费处理!
			if(EnumerateParameter.TWO.equals(CommonUtils.getString(pv.get("euStatus")))
					&& EnumerateParameter.ONE.equals(CommonUtils.getString(pv.get("flagFrozen")))){
				isCg = false;
				logger.info("患者【"+CommonUtils.getString(pv.get("namePi"))+"】就诊被冻结，不能做记退费处理!");
				//throw new BusException("患者【"+CommonUtils.getString(pv.get("namePi"))+"】就诊被冻结，不能做记退费处理!");
			}
			 
		}
		
		return isCg;
	}
	
	/**
	 * 记费时根据患者的记费病区是否为vip病区进行记费处理
	 * @param vipDeptList vip病区集合
	 * @param pkDeptNs	记费科室
	 * @return true：按照vip记费，false：按照非vip记费
	 */
	private boolean isSpecCharge(List<Map<String,Object>> vipDeptList,String pkDeptNs){
		boolean isFlag = false;
		//校验vip病区集合或者记费病区主键是否为空，为空按照普通逻辑记费
		if(vipDeptList!=null && vipDeptList.size()>0 && !CommonUtils.isEmptyString(pkDeptNs)){
			for(Map<String,Object> map : vipDeptList){
				//如果返回值等于记费病区，按vip逻辑记费
				if((map!=null && map.size()>0) 
						&& (map.get("pkDept")!=null && CommonUtils.getString(map.get("pkDept")).equals(pkDeptNs))){
					isFlag = true;
					break;
				}
			}
		}
		return isFlag;
	}
	
	/**
	 * 获取当前项目的有效自付比例
	 * @param euPvtype
	 * @param rateIp
	 * @param rateOp
	 * @param rateEr
	 * @param ratePe
	 * @return
	 */
	private Double getItemRatioSelf(String euPvtype,Double rateIp,Double rateOp,Double rateEr,Double ratePe){
		Double ratioSelf = 1D;
		if(!CommonUtils.isEmptyString(euPvtype)){
			if(EnumerateParameter.ONE.equals(euPvtype))
				ratioSelf = rateOp;
			else if(EnumerateParameter.TWO.equals(euPvtype))
				ratioSelf = rateEr;
			else if(EnumerateParameter.THREE.equals(euPvtype))
				ratioSelf = rateIp;
			else if(EnumerateParameter.FOUR.equals(euPvtype))
				ratioSelf = ratePe;
		}
		return ratioSelf;
	}
	
	/**
	  * 广州区公医住院收费处理 -广州公医医保策略
     * 住院批量计费
	  * @param bdHp
	  * @param pv
	  * @param itemPvList
	  * @param flagPriceAvrl 是否需要验证限价审批
	  * @return
	  */
   public List<ItemPriceVo> gzGyDistCharge(HpVo bdHp,PvEncounter pv, List<ItemPriceVo> itemPvList,boolean flagPriceAvrl){
	   	if(itemPvList==null || itemPvList.size()<=0)
	   		return null;
	   	
			Map<String,Object> paramMap = new HashMap<>();	//查询参数
			
			paramMap.put("code", ApplicationUtils.getSysparam("BD0001", true));	//读取参数BD0001（床位费编码）
			paramMap.put("pkHp", bdHp.getPkHp());	//医保主键
			paramMap.put("euPvtype", pv.getEuPvtype());//就诊类型
			/**获取诊查费记费策略*/
			List<ExamFeeStyVo> examPriceList = cgStrategyPubMapper.qryExamFeeList(paramMap);
			
			/**获取床位费计费策略*/
			List<BlBedItemStyVo> bedPriceList = cgStrategyPubMapper.qryBedItemPriceSty(paramMap);
			
			/**获取特殊项目计费策略*/
			List<BlSpItemStyVo> spItemPriceList = cgStrategyPubMapper.qrySpItemPriceSty(paramMap);
			
			/**获取高值耗材计费策略*/
			List<BlHvItemStyVo> hvItemPriceList = this.getHvItemPriceSty(bdHp,paramMap); 
			
			//公医医保策略集合
			List<ItemPriceVo> gyHpPvList = new ArrayList<>();
			
			//医保策略集合
			List<ItemPriceVo> hpPvList = new ArrayList<>();
			
			//记录需要走医保计费策略的项目集合
			List<ItemPriceVo> saveHpPvList = new ArrayList<>();
			
			//最终返回集合
			List<ItemPriceVo> newItemPvList = new ArrayList<>();
			
	   	for(ItemPriceVo itemPriceVo : itemPvList){
	   		BlItemPriceVo bpVo = null;
	   		
	   		/**项目是否需要限价审批，true:按自费方式处理记费，false:按医保策略处理记费*/
	   		if(!isPriceAvrl(itemPriceVo,bdHp,flagPriceAvrl)){
	   			/**诊查费*/
	   			bpVo = this.qryExamFeeCharge(pv, bdHp, itemPriceVo, examPriceList);
	   			
	   			/**床位费*/
	   			if(bpVo==null)
	   				bpVo = this.qryBedCharge(pv,bdHp,itemPriceVo,bedPriceList);
	       		
	       		/**特殊项目*/
	       		if(bpVo==null)
	       			bpVo = this.qrySpitemCharge(pv,bdHp,itemPriceVo,spItemPriceList);
	       		
	       		/**高值耗材*/
	       		if(bpVo==null)
	       			bpVo = this.qryHvItemCharge(pv,bdHp,itemPriceVo,hvItemPriceList);
	   		}else{
	   			/**自费*/
	   			bpVo = itemSelfPay(bdHp,itemPriceVo);
	   		}
	   		
	   		/**组装信息到ItemPriceVo*/
	   		if(bpVo!=null){
	   			itemPriceVo.setFlagHppi("1");
	   			itemPriceVo.setPrice(bpVo.getPrice());
	   			itemPriceVo.setRatioSelf(bpVo.getRate());						//患者自付比例
	   			itemPriceVo.setAmtHppi(BigDecimal.valueOf(bpVo.getAmtHppi()));	//患者自付金额
	   			gyHpPvList.add(itemPriceVo);
	   		}else{
	   			//没有匹配到床位费、特殊项目费、高值耗材费则走医保计费策略
	   			saveHpPvList.add(itemPriceVo);
	   		}
	   	}
	   	
	   	/**判断是否有走医保计费策略的项目*/
	   	if(saveHpPvList!=null && saveHpPvList.size()>0){
	   		hpPvList = this.getItemPriceByCgDiv(bdHp,pv,saveHpPvList);
	   	}
	   	
	   	//组装公医医保策略项目集合和医保策略项目集合到newItemPvList
	   	if(gyHpPvList!=null && gyHpPvList.size()>0)
	   		newItemPvList.addAll(gyHpPvList);
	   	if(hpPvList!=null && hpPvList.size()>0)
	   		newItemPvList.addAll(hpPvList);
	   	
	   	return newItemPvList;
   }
   
   /**
    * 返回项目是否需要限价审批，true:按自费方式处理记费，false:按医保策略处理记费
    * @parmam flagPriceAvrl 是否需要走限价审批流程
    * @return
    */
   private boolean isPriceAvrl(ItemPriceVo priceVo,BdHp bdHp,boolean flagPriceAvrl){
   	
	   	/**校验是否需要走限价审批流程*/
	   	if(!flagPriceAvrl)
	   		return false;
	   	
	   	boolean isFlag  = true;
	   	
	   	/**1）	如果为药品项目，返回否*/
	   	if(BlcgUtil.converToTrueOrFalse(priceVo.getFlagPd()))
	   		isFlag = false;
	   	
	   	/**2）	读取参数BL0024（手术费分类编码），根据参数值获取分类主键pk_itemcate；
					如果入参pk_itemcate匹配手术费分类，返回否
	   	 */
	   	String type = ApplicationUtils.getSysparam("BL0024", true);
	   	if(isFlag && !CommonUtils.isEmptyString(type)){
	   		Map<String,Object> pkItemcate = DataBaseHelper.queryForMap(
	   				"select pk_Itemcate pkItemcate from bd_itemcate where CODE = ? and del_flag = '0'",type);
	   		if(pkItemcate!=null && pkItemcate.get("pkitemcate")!=null && priceVo.getPkItemcate().equals(pkItemcate.get("pkitemcate").toString()))
	   			isFlag = false;
	   	}
	   	
	   	
	   	/**如果price_cs>itemquota_app，返回是，否则返回否*/
	   	if(isFlag && (bdHp!=null && bdHp.getItemquotaApp()!=null))
	   	{
	   		if(priceVo.getPriceOrg()>bdHp.getItemquotaApp())
	   			isFlag = true;
	       	else
	       		isFlag = false;
	   	}
	   	
	   	return isFlag ;
   }
   
   /***
    * 自费方式处理收费项目
    * @param bdHp
    * @param itemPriceVo
    * @return
    */
   private BlItemPriceVo itemSelfPay(BdHp bdHp,ItemPriceVo itemPriceVo){
	   	if(itemPriceVo==null)
	   		return null;
	   	
	   	BlItemPriceVo priceVo = new BlItemPriceVo();
	   	//自付比例
	   	priceVo.setRate(new Double(1));
	   	priceVo.setPrice(itemPriceVo.getPriceCs());
	   	//自付金额
	   	priceVo.setAmtHppi(
	   			MathUtils.mul(MathUtils.mul(itemPriceVo.getPriceCs(),itemPriceVo.getQuan()),new Double(1))
	   			);
	   	
	   	return priceVo;
   }
   
   /**
	 * 校验收费项目数量是否超过单日最大记费数量
	 * @return
	 */
	public CheckItemCgVo checkItemCgNum(List<ItemPriceVo> itemlist,PvEncounter pv){
		if(itemlist==null||itemlist.size()<=0) return null;
		//查询收费项目及其数量限制属性
		List<String> pkList = itemlist.stream()
				.filter(m-> !CommonUtils.isEmptyString(m.getFlagPd()) && !"1".equals(m.getFlagPd()) && !CommonUtils.isEmptyString(m.getPkItemOld()))
				.map(ItemPriceVo :: getPkItemOld)
				.collect(Collectors.toList());
		List<ItemCgNumVo> itemCgList = cgStrategyPubMapper.qryItemCgNum(pkList);
		List<Map<String,Object>> mapList = new ArrayList<>();
		if(itemCgList!=null && itemCgList.size()>0){
			
			for(ItemPriceVo paramVo: itemlist){
				if(BlcgUtil.converToTrueOrFalse(paramVo.getFlagPd())){
					continue;
				}
				for(ItemCgNumVo cgVo:itemCgList){
					Map<String,Object> map = new HashMap<>();
					if(paramVo.getPkItemOld().equals(cgVo.getPkItem()) &&
							!CommonUtils.isEmptyString(cgVo.getValAttr())){
						//获取该项目本次记费总数量
						Double quanCg = getItemQuan(itemlist,paramVo);
						
						String dateHap =  paramVo.getDateHap()==null?DateUtils.getDate("yyyyMMdd"):DateUtils.formatDate(paramVo.getDateHap(), "yyyyMMdd");
						dateHap = dateHap+"000000";
						//获取患者指定日期下的已记费数量
						String cgNum = cgStrategyPubMapper.qryCgNumByPv(
								paramVo.getPkPv(),dateHap, paramVo.getPkItemOld());
						
						if(CommonUtils.isEmptyString(cgNum)) cgNum="0";
						//if(paramVo.getQuanOld()==null) paramVo.setQuanOld(0D);
							
						//计费数量
						Double itemCged = Double.valueOf(cgNum) + quanCg;
						Integer orderSn = paramVo.getOrdsn();
						boolean flag = itemCged > new Double(cgVo.getValAttr()) ? false : true;
						
						if(!flag){
							map.put("name",cgVo.getName());
							map.put("quanCg",new Double(quanCg).intValue());//本次记费数量
							map.put("cgNum",new Double(cgNum).intValue());//本日已记费数量
							map.put("valAttr",new Double(cgVo.getValAttr()).intValue());//计费上限
							map.put("orderSn", orderSn);
							map.put("patientName", pv.getNamePi());
							mapList.add(map);
						}
						break;
					}
				}
			}
			
		}
		
		CheckItemCgVo vo = new CheckItemCgVo();
		
		if(mapList!=null && mapList.size()>0){
			vo.setErrFlag(false);
			StringBuffer errMsg = new StringBuffer();
			for(Map<String,Object> map : mapList){
				errMsg.append("患者姓名:"+map.get("patientName")+" 医嘱号:"+map.get("orderSn")+"【"+map.get("name")+"】项目单日记费上限"+map.get("valAttr")+"，本日已记"+map.get("cgNum")+"，本次记费"+map.get("quanCg")+"，超过记费上限不允许记费！\r\n");
			}
			vo.setErrMsg(errMsg.toString());
		}
		return vo;
	}
	
	private Double getItemQuan(List<ItemPriceVo> itemlist,ItemPriceVo item){
		Double quan = 0D;
		String dateHap =  item.getDateHap()==null?DateUtils.getDate("yyyyMMdd"):DateUtils.formatDate(item.getDateHap(), "yyyy-MM-dd");
		
		for(ItemPriceVo vo : itemlist){
			String date =  vo.getDateHap()==null?DateUtils.getDate("yyyyMMdd"):DateUtils.formatDate(vo.getDateHap(), "yyyy-MM-dd");
			if(item.getPkItemOld().equals(vo.getPkItemOld()) && date.equals(dateHap)){
				quan = MathUtils.add(quan, vo.getQuan()!=null?vo.getQuan():0D);
			}
		}
		
		return quan+0D;
	}
	
	/**
	 * 校验收费项目数量是否超过单日最大记费数量
	 */
	public void checkItemCg(List<ItemPriceVo> itemlist,PvEncounter pv){
		CheckItemCgVo cgVo = this.checkItemCgNum(itemlist,pv);
		if(!cgVo.getErrFlag()){
			throw new BusException(cgVo.getErrMsg());
		}
	}
	
	/**
	 * 草药处理逻辑
	 * @param itemlist
	 * @return
	 */
	public List<ItemPriceVo> HerbDisp(List<ItemPriceVo> itemlist){
		if(itemlist==null || itemlist.size()<=0)
			return itemlist;
		
		Set<String> pkList = new HashSet<>();//医嘱号集合
		
		for(ItemPriceVo itemVo : itemlist){
			if(!CommonUtils.isEmptyString(itemVo.getPkCnord()))
				pkList.add(itemVo.getPkCnord());
		}
		
		if(pkList==null || pkList.size()<=0)
			return itemlist;
		
		for(String pkCnord : pkList){
			//查询草药医嘱信息
			List<String> herbList = cgStrategyPubMapper.qryHerbDispInfo(pkCnord);
			if(herbList!=null && herbList.size()>0 && herbList.size()==1){
				for(ItemPriceVo priceVo : itemlist){
					if(!CommonUtils.isEmptyString(priceVo.getPkCnord()) &&
							priceVo.getPkCnord().equals(pkCnord) &&
							herbList.get(0).equals("1")){
						priceVo.setRatioSelf(1D);
						priceVo.setRatioDisc(1D);
					}
				}
			}
		}
		
		return itemlist;
	}
}
