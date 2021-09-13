package com.zebone.nhis.base.bd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.StrategySetMapper;
import com.zebone.nhis.base.bd.vo.StrategySetVo;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyHpDiv;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivBed;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivHvitem;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivSpitem;
import com.zebone.nhis.common.module.scm.pub.BdStore;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 *
 * 策略设置后台服务
 * @author c
 *
 */
@Service
public class StrategySetService {
	
	@Autowired
	private StrategySetMapper strategySetMapper;
	
	/**
	 * 交易号：001002005060
	 * 查询策略信息
	 * @param param
	 * @param user
	 * @return
	 */
	public StrategySetVo qryStrategyInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
//		/**查询床位费策略和关联医保信息*/
//		List<InsGzgyDivBed> bedList = strategySetMapper.qryDivBed(null);
//		if(bedList!=null && bedList.size()>0){
//			for(InsGzgyDivBed bed : bedList){
//				/**查询床位费策略关联医保信息*/
//				List<InsGzgyHpDiv> bedHpList = 
//						strategySetMapper.qryDivHpByPkdiv("0", bed.getPkBedItemDiv());
//				bed.setBedHpDiv(bedHpList);
//			}
//		}
		
		/**查询高值耗材策略和关联医保信息*/
		List<InsGzgyDivHvitem> hvitemList = strategySetMapper.qryDivHvitem(null);
		if(hvitemList!=null && hvitemList.size()>0){
			for(InsGzgyDivHvitem hvitem : hvitemList){
				/**查询高值策略关联医保信息*/
				List<InsGzgyHpDiv> hvitemHpList = 
						strategySetMapper.qryDivHpByPkdiv("2", hvitem.getPkHvitemDiv());
				hvitem.setHvItemHpDiv(hvitemHpList);
			}
		}
		
		/**查询特殊项目策略和关联医保信息*/
		List<InsGzgyDivSpitem> spitemList = strategySetMapper.qryDivSpitem(null);
		if(spitemList!=null && spitemList.size()>0){
			for(InsGzgyDivSpitem spitem : spitemList){
				/**查询特殊项目策略关联医保信息*/
				List<InsGzgyHpDiv> spitemHpList = 
						strategySetMapper.qryDivHpByPkdiv("1", spitem.getPkSpitemdiv());
				spitem.setSpitemHpDiv(spitemHpList);
			}
		}
		
		/**组装信息到vo*/
		StrategySetVo setVo = new StrategySetVo();
		setVo.setBedList(null);
		setVo.setHvitemList(hvitemList);
		setVo.setSpitemList(spitemList);
		
		return setVo;
	}
	
	/**
	 * 交易号：001002005061
	 * 查询待选医保信息和已选医保信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryDivHpByPk(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		//待选医保信息
		List<InsGzgyHpDiv> waitselectHps = strategySetMapper.qryWaitChooseHp(paramMap);
		
		//组装vo信息
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("waitSelectHp", waitselectHps);
		
		return retMap;
	}
	
	/**
	 * 交易号：001002005062
	 * 保存策略信息
	 * @param param
	 * @param user
	 */
	public void saveStrategyInfo(String param,IUser user){
		StrategySetVo saveVo = JsonUtil.readValue(param, StrategySetVo.class);
		
		//床位策略信息
		List<InsGzgyDivBed> bedList = saveVo.getBedList();
		//高值策略信息
		List<InsGzgyDivHvitem> hvitemList = saveVo.getHvitemList();
		//特殊项目策略信息
		List<InsGzgyDivSpitem> spitemList = saveVo.getSpitemList();
		
		/**保存床位信息*/
		if(bedList!=null && bedList.size()>0){
			//for(InsGzgyDivBed bed : bedList){
			/**校验---1.校验前台所传的list的每一条编码的唯一性*/
			Map<String, String> codemap = new HashMap<String, String>();
			List<String> pkList = new ArrayList<>();
			int len = bedList.size();
			for(int i=0; i<len; i++){
				String code = bedList.get(i).getDictPsnlevel();
				if(codemap.containsKey(code)){
					throw new BusException("级别编码重复！");
				}
				codemap.put(code, bedList.get(i).getPkBedItemDiv());
				
				if(!CommonUtils.isEmptyString(bedList.get(i).getPkBedItemDiv()))
					pkList.add(bedList.get(i).getPkBedItemDiv());
			}
			
			/**查询数据库中所有(过滤已经修改的数据)*/
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkList", pkList);
			List<InsGzgyDivBed> allist = this.strategySetMapper.qryDivBed(paramMap);
			
			/**校验---2.bedList与数据库比较校验编码的重复性*/
			for(InsGzgyDivBed divBed : allist){
				if(codemap.containsKey(divBed.getDictPsnlevel())){
					String pkBedItemDiv = codemap.get(divBed.getDictPsnlevel());
					if(pkBedItemDiv == null){
						throw new BusException("级别编码在数据库中已存在！");
					}else{
						if(!divBed.getPkBedItemDiv().equals(pkBedItemDiv)){
							throw new BusException("级别编码在数据库中已存在！");
						}
					}
				}
			}
			//}
			
			/**新增或更新到数据库*/
			for(InsGzgyDivBed saveInfo : bedList){
				if(saveInfo.getPkBedItemDiv() == null){
					DataBaseHelper.insertBean(saveInfo);
				}else{
					DataBaseHelper.updateBeanByPk(saveInfo,false);
				}
				
				String pkBedItemDiv = saveInfo.getPkBedItemDiv();
				
				/**保存关联医保信息*/
				saveHps(saveInfo.getBedHpDiv(),pkBedItemDiv,"0");
			}
		}
		
		/**保存高值策略信息*/
		if(hvitemList!=null && hvitemList.size()>0){
			//List<String> pkList = new ArrayList<>();
			
			/**校验单价下限和单价上限有没有交集*/
			for(InsGzgyDivHvitem saveInfo : hvitemList){
				if(CommonUtils.isEmptyString(saveInfo.getPkHvitemDiv())){
					saveInfo.setPkHvitemDiv(NHISUUID.getKeyId());
					saveInfo.setIsAdd(true);
				} /*else{
					pkList.add(saveInfo.getPkHvitemDiv());
				}*/
			}
			
			/*for(InsGzgyDivHvitem hvitemOut : hvitemList){
				for(InsGzgyDivHvitem hvitemIn : hvitemList){
					if(!hvitemOut.getPkHvitemDiv().equals(hvitemIn.getPkHvitemDiv()))
					{
						if((hvitemOut.getPriceMin()>=hvitemIn.getPriceMin() && hvitemOut.getPriceMin()<=hvitemIn.getPriceMax())
								|| (hvitemOut.getPriceMax()>=hvitemIn.getPriceMin() && hvitemOut.getPriceMax()<=hvitemIn.getPriceMax())
								|| (hvitemIn.getPriceMin()>=hvitemOut.getPriceMin() && hvitemIn.getPriceMin()<=hvitemOut.getPriceMax())
								|| (hvitemIn.getPriceMax()>=hvitemOut.getPriceMax() && hvitemIn.getPriceMax()<=hvitemOut.getPriceMax())
								){
							throw new BusException("高值耗材单价下限、单价上限存在交集！");
						}
					}
				}
			}*/
			
			/**校验数据库中数据是否存在交集(过滤已经修改的数据)*/
			/*Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkList", pkList);
			List<InsGzgyDivHvitem> allist = this.strategySetMapper.qryDivHvitem(paramMap);
			for(InsGzgyDivHvitem hvitemOut : hvitemList){
				for(InsGzgyDivHvitem hvitemIn : allist){
					if(!hvitemOut.getPkHvitemDiv().equals(hvitemIn.getPkHvitemDiv()))
					{
						if((hvitemOut.getPriceMin()>=hvitemIn.getPriceMin() && hvitemOut.getPriceMin()<=hvitemIn.getPriceMax())
								|| (hvitemOut.getPriceMax()>=hvitemIn.getPriceMin() && hvitemOut.getPriceMax()<=hvitemIn.getPriceMax())
								|| (hvitemIn.getPriceMin()>=hvitemOut.getPriceMin() && hvitemIn.getPriceMin()<=hvitemOut.getPriceMax())
								|| (hvitemIn.getPriceMax()>=hvitemOut.getPriceMax() && hvitemIn.getPriceMax()<=hvitemOut.getPriceMax())
								){
							throw new BusException("高值耗材单价下限、单价上限存在交集！");
						}
					}
				}
			}*/
			
			/**新增或更新到数据库*/
			for(InsGzgyDivHvitem saveInfo : hvitemList){
				if(CommonUtils.isNotNull(saveInfo.getIsAdd()) && saveInfo.getIsAdd()){
					DataBaseHelper.insertBean(saveInfo);
				}else{
					DataBaseHelper.updateBeanByPk(saveInfo,false);
				}
				
				String pkHvitemDiv = saveInfo.getPkHvitemDiv();
				
				/**保存关联医保信息*/
				saveHps(saveInfo.getHvItemHpDiv(),pkHvitemDiv,"2");
			}
		}
		
		/**保存特殊项目策略信息*/
		if(spitemList!=null && spitemList.size()>0){
			
			/**校验---1.校验前台所传的list的每一条pkItem的唯一性*/
			Map<String, String> pkItemMap = new HashMap<String, String>();
			List<String> pkList = new ArrayList<>();
			int len = spitemList.size();
			for(int i=0; i<len; i++){
				String pkItem = spitemList.get(i).getPkItem();
				if(pkItemMap.containsKey(pkItem)){
					throw new BusException("收费项目编码重复！");
				}
				pkItemMap.put(pkItem, spitemList.get(i).getPkSpitemdiv());
				if(!CommonUtils.isEmptyString(spitemList.get(i).getPkSpitemdiv()))
					pkList.add(spitemList.get(i).getPkSpitemdiv());
			}
			
			/**查询数据库中所有*/
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkList", pkList);
			List<InsGzgyDivSpitem> allist = this.strategySetMapper.qryDivSpitem(paramMap);
			
			/**校验---2.spitemList与数据库比较校验pkItem的重复性*/
			for(InsGzgyDivSpitem divSpitem : allist){
				if(pkItemMap.containsKey(divSpitem.getPkItem())){
					String pkDivSpitem = pkItemMap.get(divSpitem.getPkItem());
					if(pkDivSpitem == null){
						throw new BusException("收费项目编码在数据库中已存在！");
					}else{
						if(!divSpitem.getPkSpitemdiv().equals(pkDivSpitem)){
							throw new BusException("收费项目编码在数据库中已存在！");
						}
					}
				}
			}
			
			/**保存或更新数据*/
			for(InsGzgyDivSpitem saveInfo : spitemList){
				if(saveInfo.getPkSpitemdiv() == null){
					DataBaseHelper.insertBean(saveInfo);
				}else{
					DataBaseHelper.updateBeanByPk(saveInfo,false);
				}
				
				String pkSpitemdiv = saveInfo.getPkSpitemdiv();
				
				/**保存关联医保信息*/
				saveHps(saveInfo.getSpitemHpDiv(),pkSpitemdiv,"1");
			}
		}
		
	}
	
	/**
	 * 交易号：001002005063
	 * 删除床位费策略信息
	 * @param param
	 * @param user
	 */
	public void batchDelStrategy(String param,IUser user){
		String euDivtype = JsonUtil.getFieldValue(param, "euDivtype");
		
		List<String> delList = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "delPks"),
				new TypeReference<List<String>>() {
				});
		
		if(delList!=null && delList.size()>0){
			switch (euDivtype) {
			case "0":		//床位费策略
				//批量删除
				strategySetMapper.batchDelDivBed(delList);
				break;
			case "1":		//特殊项目策略
				//批量删除
				strategySetMapper.batchDelDivSpitem(delList);
				break;
			case "2":		//高值策略
				//批量删除
				strategySetMapper.batchDelDivHvitem(delList);
				break;
			default:
				break;
			}
			
			//批量删除关联医保信息
			strategySetMapper.batchDelDivHp(delList);
		}
	}
	
	/**
	 * 保存关联医保信息
	 * @param hps 医保信息
	 * @param pkDiv 策略主键
	 * @param euDivtype 0 床位费策略，1特殊项目策略，2高值耗材策略
	 */
	private void saveHps(List<InsGzgyHpDiv> hps,String pkDiv,String euDivtype){
		if(hps!=null && hps.size()>0)
		{
			// 先全删再恢复的方式（软删除）
			DataBaseHelper
					.update("update INS_GZGY_HP_DIV set del_flag='1' where pk_div = ?",
							new Object[] { pkDiv });
			
			for (InsGzgyHpDiv hp : hps) {
				if (hp.getPkHpdiv() != null) {
					hp.setDelFlag("0");// 恢复
					hp.setPkDiv(pkDiv);
					DataBaseHelper.updateBeanByPk(hp, false);
				} else {
					hp.setPkDiv(pkDiv);
					hp.setEuDivtype(euDivtype);	//0 床位费策略，1特殊项目策略，2高值耗材策略
					DataBaseHelper.insertBean(hp);
				}
			}
		}
		else
		{
			DataBaseHelper
			.update("update INS_GZGY_HP_DIV set del_flag='1' where pk_div = ?",
					new Object[] { pkDiv });
		}
		//delete语句删除del_flag='1'的数据
		DataBaseHelper.execute("delete from INS_GZGY_HP_DIV where del_flag = '1' and pk_div = ?",
				new Object[] { pkDiv });
	}
	
}
