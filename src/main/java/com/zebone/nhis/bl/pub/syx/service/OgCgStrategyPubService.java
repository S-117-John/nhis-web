package com.zebone.nhis.bl.pub.syx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.syx.dao.OgCgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.vo.BlOpDtVo;
import com.zebone.nhis.bl.pub.syx.vo.CheckItemCgVo;
import com.zebone.nhis.bl.pub.syx.vo.HpVo;
import com.zebone.nhis.bl.pub.syx.vo.ItemCgNumVo;
import com.zebone.nhis.bl.pub.syx.vo.OpGyAmtVo;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.GyHpDivInfo;
import com.zebone.nhis.bl.pub.vo.GyStItemVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.PvDiag;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgySt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class OgCgStrategyPubService {
	
	@Autowired
	private OgCgStrategyPubMapper ogCgStrategyPubMapper;
	
	/**
	 * 广州公医结算策略
	 * @param pv
	 */
	public OpGyAmtVo opSyxSettle(PvEncounter pv,List<BlOpDt> blOpDts){
		//查询患者主医保是否是广州公医
		Map<String,Object> qryMap = new HashMap<>();
		qryMap.put("pkPv", pv.getPkPv());
		Map<String,Object> hpMap = ogCgStrategyPubMapper.qryPvInsuAttrVal(qryMap);
		
		// 二、生成结算
		OpGyAmtVo amtVo = new OpGyAmtVo();
		
		//获取需要结算的记费主键
		List<String> pkCgOpList = new ArrayList<>();
		for(BlOpDt vo : blOpDts){
			if(vo.getRatioDisc()==null)
				vo.setRatioDisc(0D);
			pkCgOpList.add(vo.getPkCgop());
		}
		
		if(pkCgOpList==null || pkCgOpList.size()<=0)
			return amtVo;
		
		//校验患者主医保0301参数是否为1
		if(hpMap!=null && hpMap.size()>0 && 
				hpMap.get("valAttr")!=null &&
				EnumerateParameter.ONE.equals(CommonUtils.getString(hpMap.get("valAttr")))){
			amtVo.setHpCodeAttr(CommonUtils.getString(hpMap.get("valAttr")));//0301属性值
			/**校验患者是否是急诊，急诊不需要判断定点医院，按照按普通门诊策略处理*/
			//查询患者患者急诊就诊类型sql
			if(!CommonUtils.isEmptyString(pv.getEuPvtype()) && "2".equals(pv.getEuPvtype())){
				/**公医普通门诊策略*/
				amtVo = newGyGeneralOpSt(amtVo, pv,pkCgOpList);
			}else{
				/**判断当前机构是否为当前患者的定点医院，非定点医院按照自费处理*/
				//查询定点医院sql
				boolean disHosFlag = qryDesHosByInsu(pv);
				if(disHosFlag){
					//查询患者医保就诊模式（0普通门诊，1门诊慢病，2门诊特殊病）
					qryMap.put("pkHp", pv.getPkInsu());
					String hpStatus = ogCgStrategyPubMapper.qryHpStatusByPv(qryMap);
					if(CommonUtils.isEmptyString(hpStatus))
						throw new BusException("请维护患者主医保就诊模式！");
					//查询患者主诊断
					List<PvDiag> diagList = ogCgStrategyPubMapper.qryNamediagByPv(qryMap);
					//qryMap.put("nameDiag", nameDiag);
					//查询患者主医保类型(true:省公医 false:区公医)
					boolean proGyFlag = qryHpTypeByPv(pv.getPkInsu());
					//amtVo.setNameDiag(nameDiag);
					if(proGyFlag){
						switch (hpStatus) {
						case EnumerateParameter.ZERO://普通门诊
							amtVo = newGyGeneralOpSt(amtVo, pv,pkCgOpList);
							break;
						case EnumerateParameter.ONE://门诊慢病(按照普通门诊策略结算)
							amtVo = newGyGeneralOpSt(amtVo, pv,pkCgOpList);
							break;
						case EnumerateParameter.TWO://门诊特殊病(目前省公医门诊特殊病先按照普通门诊策略结算)
							amtVo.setNameDiag(qryNameDiag(diagList,EnumerateParameter.THREE));
							amtVo = newGyGeneralOpSt(amtVo, pv,pkCgOpList);
							break;
						default:
							break;
						}
					}else{
						switch (hpStatus) {
						case EnumerateParameter.ZERO://普通门诊
							amtVo = newGyGeneralOpSt(amtVo, pv,pkCgOpList);
							break;
						case EnumerateParameter.ONE://门诊慢病
							amtVo.setNameDiag(qryNameDiag(diagList,EnumerateParameter.TWO));
							amtVo = newGyChronicOpSt(amtVo, pv,pkCgOpList);
							break;
						case EnumerateParameter.TWO://门诊特殊病
							amtVo.setNameDiag(qryNameDiag(diagList,EnumerateParameter.THREE));
							amtVo = newGySpdisOpSt(amtVo, pv,pkCgOpList);
							break;
						default:
							break;
						}
					}
				}else{
					/**按照自费处理*/
					amtVo = gyExpenseOpSt(amtVo, pv,pkCgOpList);
				}
			}
		}else{ //默认按照正常情况处理
			for (BlOpDt bpt : blOpDts) {
				amtVo.setAmountSt(amtVo.getAmountSt().add(BigDecimal.valueOf(bpt.getAmount())));
				amtVo.setAmountPi(amtVo.getAmountPi().add(BigDecimal.valueOf(bpt.getAmountPi())));
				//医保优惠计费部分
				//amtVo.setAmountInsu(amtVo.getAmountInsu().add(BigDecimal.valueOf(((bpt.getPriceOrg() - bpt.getPrice()) + (bpt.getPriceOrg() * (1 - bpt.getRatioSelf()))) * bpt.getQuan())));
				amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
				if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
					//患者优惠
					amtVo.setDiscAmount(amtVo.getDiscAmount().add(BigDecimal.valueOf(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan()))));
				}
				if ("1".equals(bpt.getFlagAcc())) {
					amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(BigDecimal.valueOf(bpt.getAmountPi())));
				}
			}
			//amountInsu = amountSt - amountPi
			amtVo.setAmountInsu(amtVo.getAmountSt().subtract(amtVo.getAmountPi()));
		}
		
		return amtVo;
	}
	
	/**
	 * 获取对应就诊模式的疾病名称2：慢病 3特病
	 * @param diagList
	 * @param euSptype
	 * @return
	 */
	public String qryNameDiag(List<PvDiag> diagList,String euSptype){
		String nameDiag = "";
		for(PvDiag diag : diagList){
			if(diag.getEuSptype().equals(euSptype)){
				nameDiag = diag.getNameDiag();
				break;
			}
		}
		return nameDiag;
	}
	
	/**
	 * 查询是否是定点医院
	 * @param pkHp
	 * @return
	 */
	private boolean qryDesHosByInsu(PvEncounter pv){
		Map<String,Object> qryMap = new HashMap<>();
		qryMap.put("pkHp", pv.getPkInsu());
		qryMap.put("pkPv", pv.getPkPv());
		//获取医疗机构代码
		Map<String,Object> medMap = ogCgStrategyPubMapper.qryMedicalInfo(qryMap);
		boolean rtnFlag = false;
		if(medMap!=null && medMap.size()>0){
			//查询患者医疗证号
			String mcno = ogCgStrategyPubMapper.qryMcnoByPv(qryMap);
			if(CommonUtils.isEmptyString(mcno))
				mcno = "";
			qryMap.put("mcno", mcno);
			qryMap.put("medical", CommonUtils.getString(medMap.get("valAttr")));//医疗机构代码
			//判断是否定点医院
			Integer cnt = ogCgStrategyPubMapper.checkDesHos(qryMap);
			if(cnt>0)
				rtnFlag = true;
		}
		return rtnFlag;
	}
	
	/**
	 * 查询患者医保类型 
	 * @param pkPv
	 * @return true:省公医 false:区公医
	 */
	private boolean qryHpTypeByPv(String pkHp){
		boolean flag = false;
		
		//公医类型，读取参数BL0033（广州医保省公医上级编码）
		String sysCode = ApplicationUtils.getSysparam("BL0033", true);
		//查询患者主医保的上级医保编码
		Map<String, Object> qryMap = new HashMap<>();
		qryMap.put("pkHp", pkHp);
		HpVo hp = ogCgStrategyPubMapper.queryHp(qryMap);
		if(sysCode.equals(hp.getFaCode()))
			flag = true;
		else
			flag = false;
		
		return flag;
	}
	
	/**
	 * 公医普通门诊结算策略(新逻辑)
	 * @param amtVo
	 * @param pv
	 */
	private OpGyAmtVo newGyGeneralOpSt(OpGyAmtVo amtVo,PvEncounter pv,List<String> pkCgOpList){
		BigDecimal amtPi = new BigDecimal(0);	//记录患者自付
		BigDecimal amtHp = new BigDecimal(0);	//记录医保支付
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkPi", pv.getPkPi());
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("pkHp",pv.getPkInsu());
		paramMap.put("pkList", pkCgOpList);
		paramMap.put("nameDiag", amtVo.getNameDiag());
		paramMap.put("euPvtype", pv.getEuPvtype());
		paramMap.put("dateBegin", DateUtils.getDate("yyyyMMddHHmmss").substring(0, 8)+"000000");
		paramMap.put("dateEnd", DateUtils.getDate("yyyyMMddHHmmss").substring(0, 8)+"235959");
		
		
		//校验患者记费明细中的自付比例，如果有超过2种不同的自付比例弹出提示
		List<Map<String,Object>> countMap = ogCgStrategyPubMapper.qryRatioCountByPv(paramMap);
		if(countMap!=null && countMap.size()>=2)
			throw new BusException("项目自付比例设置有误，请修改!");
		
		/**1.1药品策略*/
		//查询患者待结算药费
		Map<String,Object> drugMap = ogCgStrategyPubMapper.qrygyGeneralOpDrugAmt(paramMap);
		if(drugMap!=null && drugMap.size()>0){
			//获取待结算药费信息
			BigDecimal amtDrug = BigDecimal.valueOf(CommonUtils.getDouble(drugMap.get("amtDrug")));//药费总金额
			BigDecimal amtHpdrug = BigDecimal.valueOf(CommonUtils.getDouble(drugMap.get("amtHpdrug")));//可报销药费
			BigDecimal rateOp = BigDecimal.valueOf(CommonUtils.getDouble(drugMap.get("rateOp")));//门诊自付比例
			BigDecimal drugquotaOp = BigDecimal.valueOf(999999D);//药费限额
			
			//查询是否是门诊特殊病
			Map<String,Object> diseMap = ogCgStrategyPubMapper.qrySpdisInfoByPv(paramMap);
			if(diseMap==null || diseMap.size()<=0 ||
					CommonUtils.getString(diseMap.get("flagValid")).equals("0")){
				if(drugMap.get("drugquotaOp")!=null){
					//获取字典里的限额
					BigDecimal drugqupta = BigDecimal.valueOf(CommonUtils.getDouble(drugMap.get("drugquotaOp")));
					if(drugqupta.compareTo(new BigDecimal(0))>0)
						drugquotaOp = drugqupta;
				}
			}else{
				//获取手工输入的限额
				if(drugMap.get("drugquota")!=null){
					//获取字典里的限额
					BigDecimal drugqupta = BigDecimal.valueOf(CommonUtils.getDouble(drugMap.get("drugquota")));
					if(drugqupta.compareTo(new BigDecimal(0))>0)
						drugquotaOp = drugqupta;
				}
			}
			
			if(drugquotaOp.compareTo(new BigDecimal(0))==0)
				drugquotaOp = new BigDecimal(999999D);
			
			//查询患者当日诊次记录以及诊次药品费用
			List<Map<String,Object>> stDrugList = ogCgStrategyPubMapper.qryTodayDrugAmtByPi(paramMap);
			
			//查询医保计划扩展属性“0302”（广州公医普通门诊报销限制包含药费的诊次数）值
			String strVisitCount = ogCgStrategyPubMapper.qryOpVisitCount(paramMap);
			Integer visitCount = 0;
			if(!CommonUtils.isEmptyString(strVisitCount))
				visitCount = Integer.valueOf(strVisitCount);
			
			boolean matchSuccess = false;//标记是否匹配成功
			Integer insDrugCount = 0;//标记amount_ins_drug>0的记录数
			if(stDrugList!=null && stDrugList.size()>0){
				for(Map<String,Object> stDrugMap : stDrugList){
					BigDecimal amtInsDrug =BigDecimal.valueOf(CommonUtils.getDouble(stDrugMap.get("amountInsDrug")));
					if(amtInsDrug.compareTo(new BigDecimal(0))>0)//获取amount_ins_drug>0的记录数
						insDrugCount++;
				}
				
				for(Map<String,Object> stDrugMap : stDrugList){
					BigDecimal amountInsDrug =BigDecimal.valueOf(CommonUtils.getDouble(stDrugMap.get("amountInsDrug")));
					BigDecimal insAmtHpdrug = BigDecimal.valueOf(CommonUtils.getDouble(stDrugMap.get("amtHpdrug")));//已结算药费
					String stPkPv = CommonUtils.getString(stDrugMap.get("pkPv"));
					
					//用入口参数pk_pv匹配返回诊次记录结果中的pk_pv
					if(CommonUtils.getString(drugMap.get("pkPv")).equals(stPkPv)){
						//如过amount_ins_drug>0或amount_ins_drug=0且其他amount_ins_drug>0的记录数<val_attr				
						if(amountInsDrug.compareTo(new BigDecimal(0))>0 ||
								(amountInsDrug.compareTo(new BigDecimal(0))==0 && insDrugCount<visitCount)){
							
							if(insAmtHpdrug.compareTo(drugquotaOp)>=0){//如果已结算amt_hpdrug>=药费限额drugquota_op
								amtPi = amtDrug;//患者支付 amt_pi=amt_drug
								amtHp = new BigDecimal(0);//医保支付 amt_hp=0
							}else if(insAmtHpdrug.add(amtHpdrug).compareTo(drugquotaOp)<0){//如果已结算amt_hpdrug+未结算amt_hpdrug<药费限额drugquota_op
								//患者支付amt_pi=未结amt_hpdrug*rate_op+ (amt_drug-amt_hpdrug)
								amtPi = (amtHpdrug.multiply(rateOp)).add(amtDrug.subtract(amtHpdrug));
								//医保支付amt_hp=amt_drug-amt_pi
								amtHp = amtDrug.subtract(amtPi);
							}else if(insAmtHpdrug.add(amtHpdrug).compareTo(drugquotaOp)>=0){//如果已结算amt_hpdrug+未结算amt_hpdrug>=药费限额drugquota_op
								//患者支付amt_pi=(未结amt_hpdrug+已结amt_hpdrug-drugquota_op)+((drugquota_op-已结amt_hpdrug)*rate_op) + (amt_drug-amt_hpdrug)
								amtPi = ((amtHpdrug.add(insAmtHpdrug)).subtract(drugquotaOp)).add((drugquotaOp.subtract(insAmtHpdrug)).multiply(rateOp)).add(amtDrug.subtract(amtHpdrug));
								//医保支付amt_hp=amt_drug -amt_pi
								amtHp = amtDrug.subtract(amtPi);
							}
							matchSuccess = true;
						}else if(amountInsDrug.compareTo(new BigDecimal(0))==0 && insDrugCount>=visitCount){
							amtPi = amtDrug;//患者支付 amt_pi=amt_drug
							amtHp = new BigDecimal(0);//医保支付 amt_hp=0
							matchSuccess = true;
						}
					}
				}
			}
			
			//无匹配到诊次记录结果中的pk_pv
			if(!matchSuccess){
				//判断返回amt_hpdrug>0的诊次记录数，如果大于等于val_attr
				if(insDrugCount>=visitCount){
					amtPi = amtDrug;//amt_pi=amt_drug
					amtHp = new BigDecimal(0);//amt_hp=0
				}else if(insDrugCount<visitCount){
					if(amtHpdrug.compareTo(drugquotaOp)>0){//如果未结amt_hpdrug>drugquota_op
						//amt_pi=(未结amt_hpdrug-drugquota_op)+drugquota_op*rate_op+ (amt_drug-amt_hpdrug)
						amtPi = amtHpdrug.subtract(drugquotaOp).add(drugquotaOp.multiply(rateOp)).add(amtDrug.subtract(amtHpdrug));
						//amt_hp=amt_drug-amt_pi
						amtHp = amtDrug.subtract(amtPi);
					}else if(amtHpdrug.compareTo(drugquotaOp)<=0){//如果未结amt_hpdrug<=drugquota_op
						//amt_pi=未结amt_hpdrug*rate_op+ (amt_drug-amt_hpdrug)
						amtPi = amtHpdrug.multiply(rateOp).add(amtDrug.subtract(amtHpdrug));
						//amt_hp=amt_drug-amt_pi
						amtHp = amtDrug.subtract(amtPi);
					}
				}
			}
			
			amtVo.setAmountDrug(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));//医保报销药金额
		}
		
		
		/**1.2非药品策略*/
		List<GyStItemVo> itemList = ogCgStrategyPubMapper.qryGyItemList(paramMap);//查询非药品待结算信息
		List<GyHpDivInfo> materList = ogCgStrategyPubMapper.qryMaterialsInfo(paramMap);//查询高值耗材策略信息
		HpVo hpInfo = ogCgStrategyPubMapper.queryHp(paramMap);//查询患者医保信息
		if(itemList!=null && itemList.size()>0){
			for(GyStItemVo itemVo : itemList){
				/**1.2.1自费费用策略*/
				if(itemVo.getRatioSelf().compareTo(BigDecimal.valueOf(1D))==0){//自负比例为1
					amtPi = amtPi.add(itemVo.getAmt());
					continue;
				}
				/**1.2.2诊查费策略*/
				if(itemVo.getDtItemtype().startsWith("01")){//01开头为诊查费
					if(itemVo.getPrice().compareTo(itemVo.getDtquotaOp())>0){//诊查费单价price>限价dtquota_op
						//自付金额=amt-dtquota_op+dtquota_op*hp.rate_op
						BigDecimal zcAmtPi = itemVo.getAmt().subtract(itemVo.getDtquotaOp()).add(itemVo.getDtquotaOp().multiply(itemVo.getRateOp()));
						amtPi = amtPi.add(zcAmtPi);
						//医保金额=amt-自付金额
						amtHp = amtHp.add(itemVo.getAmt().subtract(zcAmtPi));
					}else{
						//自付金额=amt*hp.rate_op
						BigDecimal zcAmtPi = itemVo.getAmt().multiply(itemVo.getRateOp());
						amtPi = amtPi.add(zcAmtPi);
						//医保金额=amt-自付金额
						amtHp = amtHp.add(itemVo.getAmt().subtract(zcAmtPi));
					}
				}else if(itemVo.getDtItemtype().startsWith("07")){/**1.1.3高值耗材策略*/
					BigDecimal gzAmtPi = new BigDecimal(0);
					//查询是否是省公医，非省公医不做处理
			    	String sysFaCode = ApplicationUtils.getSysparam("BL0033", true);//获取BL0033（广州医保省公医上级编码）
			    	if(!CommonUtils.isEmptyString(sysFaCode) && hpInfo.getFaCode().equals(sysFaCode)){
			    		if(materList!=null && materList.size()>0){
			    			boolean flagSucc = false;//标志是否匹配到策略信息
			    			for(GyHpDivInfo materVo : materList){
				    			//当eu_calcmode=0时，rate_pi=hp.rate_op，否则rate_pi=hv.ratio
				    			if(!CommonUtils.isEmptyString(materVo.getEuCalcmode()) && "0".equals(materVo.getEuCalcmode())){
				    				materVo.setRatioPi(itemVo.getRateOp());
				    			}else{
				    				materVo.setRatioPi(materVo.getRatio());
				    			}
				    			//price>=price_min and price<price_max
				    			if(itemVo.getPrice().compareTo(materVo.getPriceMin())>0 && 
				    					itemVo.getPrice().compareTo(materVo.getPriceMax())<0){
				    				flagSucc = true;
				    				//自付金额=amt*ratio_init+amt*(1-ratio_init)*rate_pi
				    				gzAmtPi = itemVo.getAmt().multiply(materVo.getRatioInit()).add(itemVo.getAmt().multiply(BigDecimal.valueOf(1D).subtract(materVo.getRatioInit())).multiply(materVo.getRatioPi()));
				    				amtPi = amtPi.add(gzAmtPi);
				    				//医保金额=amt-自付金额
				    				amtHp = amtHp.add(itemVo.getAmt().subtract(gzAmtPi));
				    			}
				    		}
			    			//没有匹配到符合的策略信息
			    			if(!flagSucc){
			    				//自付金额=amt
			    				amtPi = amtPi.add(itemVo.getAmt());
			    			}
			    		}else{
			    			//医保金额=amt*hp.rate_op
				    		gzAmtPi = itemVo.getAmt().multiply(itemVo.getRateOp());
				    		amtPi = amtPi.add(gzAmtPi);
				    		//医保金额=amt-自付金额
				    		amtHp = amtHp.add(itemVo.getAmt().subtract(gzAmtPi));
			    		}
			    	}else{
			    		//医保金额=amt*hp.rate_op
			    		gzAmtPi = itemVo.getAmt().multiply(itemVo.getRateOp());
			    		amtPi = amtPi.add(gzAmtPi);
			    		//医保金额=amt-自付金额
			    		amtHp = amtHp.add(itemVo.getAmt().subtract(gzAmtPi));
			    	}
				} else if("11".equals(itemVo.getDtItemtype())){/**1.2.4床位费策略*/
					BigDecimal bedAmtPi = new BigDecimal(0);
					//床位费单价price>限价bedquota
					if(itemVo.getPrice().compareTo(itemVo.getBedquota())>0){
						//自付金额=amt-bedquota+bedquota*hp.rate_op
						bedAmtPi = itemVo.getAmt().subtract(itemVo.getBedquota()).add(itemVo.getBedquota().multiply(itemVo.getRateOp()));
						amtPi = amtPi.add(bedAmtPi);
						//医保金额=amt-自负金额
						amtHp = amtHp.add(itemVo.getAmt().subtract(bedAmtPi));
					}else{
						//自付金额=amt*hp.rate_op
						bedAmtPi = itemVo.getAmt().multiply(itemVo.getRateOp());
						amtPi = amtPi.add(bedAmtPi);
						//医保金额=amt-自负金额
						amtHp = amtHp.add(itemVo.getAmt().subtract(bedAmtPi));
					}
				}else{/**1.2.5其他费用策略*/
					BigDecimal qtAmtPi = new BigDecimal(0);
					//医保金额=amt*hp.rate_op
					qtAmtPi = itemVo.getAmt().multiply(itemVo.getRateOp());
		    		amtPi = amtPi.add(qtAmtPi);
		    		//医保金额=amt-自付金额
		    		amtHp = amtHp.add(itemVo.getAmt().subtract(qtAmtPi));
				}
			}
		}
		
		//查询所有待结算的明细
		List<BlOpDt> blOpDts = ogCgStrategyPubMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				//患者优惠
				amtVo.setDiscAmount(amtVo.getDiscAmount().add(BigDecimal.valueOf(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}
		
		amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));
		amtPi = amtVo.getAmountSt().subtract(amtVo.getAmountInsu());
		amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return amtVo;
	}
	
	/**
	 * 公医普通门诊结算策略
	 * @param amtVo
	 * @param pv
	 */
	private OpGyAmtVo gyGeneralOpSt(OpGyAmtVo amtVo,PvEncounter pv,List<String> pkCgOpList){
		BigDecimal amtPi = new BigDecimal(0);	//记录患者自付
		BigDecimal amtHp = new BigDecimal(0);	//记录医保支付
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("pkList", pkCgOpList);
		//校验患者记费明细中的自付比例，如果有超过2种不同的自付比例弹出提示
		List<Map<String,Object>> countMap = ogCgStrategyPubMapper.qryRatioCountByPv(paramMap);
		if(countMap!=null && countMap.size()>=2)
			throw new BusException("项目自付比例设置有误，请修改!");
		
		//查询患者待结算药费
		Map<String,Object> drugMap = ogCgStrategyPubMapper.qrygyGeneralOpDrugAmt(paramMap);
		if(drugMap!=null && drugMap.size()>0){
			//获取待结算药费信息
			BigDecimal amtDrug = BigDecimal.valueOf(CommonUtils.getDouble(drugMap.get("amtDrug")));//药费总金额
			BigDecimal amtHpdrug = BigDecimal.valueOf(CommonUtils.getDouble(drugMap.get("amtHpdrug")));//可报销药费
			BigDecimal rateOp = BigDecimal.valueOf(CommonUtils.getDouble(drugMap.get("rateOp")));//门诊自付比例
			BigDecimal drugquotaOp = BigDecimal.valueOf(CommonUtils.getDouble(drugMap.get("drugquotaOp")));//药费限额
			if(drugquotaOp.compareTo(new BigDecimal(0))==0)
				drugquotaOp = new BigDecimal(999999D);
			
			paramMap.put("pkPi", pv.getPkPi());
			paramMap.put("pkPv", pv.getPkPv());
			paramMap.put("pkHp", pv.getPkInsu());
			paramMap.put("dateBegin", DateUtils.getDate("yyyyMMddHHmmss").substring(0, 8)+"000000");
			paramMap.put("dateEnd", DateUtils.getDate("yyyyMMddHHmmss").substring(0, 8)+"235959");
			//查询患者当日诊次记录以及诊次药品费用
			List<Map<String,Object>> stDrugList = ogCgStrategyPubMapper.qryTodayDrugAmtByPi(paramMap);
			
			//查询医保计划扩展属性“0302”（广州公医普通门诊报销限制包含药费的诊次数）值
			String strVisitCount = ogCgStrategyPubMapper.qryOpVisitCount(paramMap);
			Integer visitCount = 0;
			if(!CommonUtils.isEmptyString(strVisitCount))
				visitCount = Integer.valueOf(strVisitCount);
			
			boolean matchSuccess = false;//标记是否匹配成功
			Integer insDrugCount = 0;//标记amount_ins_drug>0的记录数
			if(stDrugList!=null && stDrugList.size()>0){
				//获取amount_ins_drug>0的记录数
				insDrugCount = getInsDrugCount(stDrugList);
				
				for(Map<String,Object> stDrugMap : stDrugList){
					BigDecimal amountInsDrug = BigDecimal.valueOf(CommonUtils.getDouble(stDrugMap.get("amountInsDrug")));
					BigDecimal insAmtHpdrug = BigDecimal.valueOf(CommonUtils.getDouble(stDrugMap.get("amtHpdrug")));//已结算药费
					
					//用入口参数pk_pv匹配返回诊次记录结果中的pk_pv
					if(CommonUtils.getString(drugMap.get("pkPv")).equals(CommonUtils.getString(stDrugMap.get("pkPv")))){
						//如过amount_ins_drug>0或amount_ins_drug=0且其他amount_ins_drug>0的记录数<val_attr				
						if(amountInsDrug.compareTo(new BigDecimal(0))>0 ||
								(amountInsDrug.compareTo(new BigDecimal(0))==0 && insDrugCount<visitCount)){
							
							if(insAmtHpdrug.compareTo(drugquotaOp)>=0){//如果已结算amt_hpdrug>=药费限额drugquota_op
								amtPi = amtDrug;//患者支付 amt_pi=amt_drug
								amtHp = new BigDecimal(0);//医保支付 amt_hp=0
							}else if(insAmtHpdrug.add(amtHpdrug).compareTo(drugquotaOp)<0){//如果已结算amt_hpdrug+未结算amt_hpdrug<药费限额drugquota_op
								//患者支付amt_pi=未结amt_hpdrug*rate_op+ (amt_drug-amt_hpdrug)
								amtPi = (amtHpdrug.multiply(rateOp)).add(amtDrug.subtract(amtHpdrug));
								//医保支付amt_hp=amt_drug-amt_pi
								amtHp = amtDrug.subtract(amtPi);
							}else if(insAmtHpdrug.add(amtHpdrug).compareTo(drugquotaOp)>=0){//如果已结算amt_hpdrug+未结算amt_hpdrug>=药费限额drugquota_op
								//患者支付amt_pi=(未结amt_hpdrug+已结amt_hpdrug-drugquota_op)+((drugquota_op-已结amt_hpdrug)*rate_op) + (amt_drug-amt_hpdrug)
								amtPi = ((amtHpdrug.add(insAmtHpdrug)).subtract(drugquotaOp)).add((drugquotaOp.subtract(insAmtHpdrug)).multiply(rateOp)).add(amtDrug.subtract(amtHpdrug));
								//医保支付amt_hp=amt_drug -amt_pi
								amtHp = amtDrug.subtract(amtPi);
							}
							matchSuccess = true;
						}else if(amountInsDrug.compareTo(new BigDecimal(0))==0 && insDrugCount>=visitCount){
							amtPi = amtDrug;//患者支付 amt_pi=amt_drug
							amtHp = new BigDecimal(0);//医保支付 amt_hp=0
							matchSuccess = true;
						}
						break;
					}
				}
			}
			
			//无匹配到诊次记录结果中的pk_pv
			if(!matchSuccess){
				//判断返回amt_hpdrug>0的诊次记录数，如果大于等于val_attr
				if(insDrugCount>=visitCount){
					amtPi = amtDrug;//amt_pi=amt_drug
					amtHp = new BigDecimal(0);//amt_hp=0
				}else if(insDrugCount<visitCount){
					if(amtHpdrug.compareTo(drugquotaOp)>0){//如果未结amt_hpdrug>drugquota_op
						//amt_pi=(未结amt_hpdrug-drugquota_op)+drugquota_op*rate_op+ (amt_drug-amt_hpdrug)
						amtPi = amtHpdrug.subtract(drugquotaOp).add(drugquotaOp.multiply(rateOp)).add(amtDrug.subtract(amtHpdrug));
						//amt_hp=amt_drug-amt_pi
						amtHp = amtDrug.subtract(amtPi);
					}else if(amtHpdrug.compareTo(drugquotaOp)<=0){//如果未结amt_hpdrug<=drugquota_op
						//amt_pi=未结amt_hpdrug*rate_op+ (amt_drug-amt_hpdrug)
						amtPi = amtHpdrug.multiply(rateOp).add(amtDrug.subtract(amtHpdrug));
						//amt_hp=amt_drug-amt_pi
						amtHp = amtDrug.subtract(amtPi);
					}
				}
			}
			
			amtVo.setAmountDrug(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));//医保报销药金额
		}
		
		/**挂号费处理*/
		//查询患者有无结算过(结算过不再处理挂号费)
		Integer stCnt = ogCgStrategyPubMapper.qryStInfoByPv(paramMap);
		if(stCnt>0)
			paramMap.put("flagSettle", "0");
		
		Map<String,Object> feeMap = ogCgStrategyPubMapper.qryExamFeeInfo(paramMap);//查询诊查费用集合 
		if(feeMap!=null && feeMap.size()>0){
			if(feeMap.get("amtPv")!=null && CommonUtils.getDouble(feeMap.get("amtPv"))>0D){
				BigDecimal amtPv = BigDecimal.valueOf(CommonUtils.getDouble(feeMap.get("amtPv")));//诊查费
				BigDecimal dtquotaOp = BigDecimal.valueOf(CommonUtils.getDouble(feeMap.get("dtquotaOp")));//限额
				BigDecimal rateOp = BigDecimal.valueOf(CommonUtils.getDouble(feeMap.get("rateOp")));//门诊自负比例
				
				if(dtquotaOp.compareTo(BigDecimal.valueOf(0D))==0)
					dtquotaOp = BigDecimal.valueOf(999999D);
				
				if(amtPv.compareTo(dtquotaOp)<=0){
					//amtPi = amtPi.add(amtPv.multiply(rateOp));
					amtHp = amtHp.add(amtPv.subtract(amtPv.multiply(rateOp)));
				}else if(amtPv.compareTo(dtquotaOp)>0){
					BigDecimal examPi = amtPv.subtract(dtquotaOp).add(dtquotaOp.multiply(rateOp));//自费部分
					//amtPi = amtPi.add(examPi);
					amtHp = amtHp.add(amtPv.subtract(examPi));
				}
				
				//查询诊查费用的主键
				List<String> pkList = ogCgStrategyPubMapper.qryExamPkList(paramMap);
				if(pkList!=null && pkList.size()>0)
					pkCgOpList.addAll(pkList);
			}
		}
		
		//查询非药品待结算金额
		Map<String,Object> itemAmtMap = ogCgStrategyPubMapper.qrygyGeneralOpItemAmt(paramMap);
		if(itemAmtMap!=null && itemAmtMap.size()>0){
			//amt_pi=amt_hppi
			amtPi = amtPi.add(BigDecimal.valueOf(CommonUtils.getDouble(itemAmtMap.get("amtHppi"))));
			//amt_hp=amt_hp
			amtHp = amtHp.add(BigDecimal.valueOf(CommonUtils.getDouble(itemAmtMap.get("amtHp"))));
		}
		
		//查询所有待结算的明细
		List<BlOpDt> blOpDts = ogCgStrategyPubMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				//患者优惠
				amtVo.setDiscAmount(amtVo.getDiscAmount().add(BigDecimal.valueOf(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}
		
		amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));
		amtPi = amtVo.getAmountSt().subtract(amtVo.getAmountInsu());
		amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return amtVo;
	}
	
	/**
	 * 公医慢病结算策略(新逻辑)
	 * @param amtVo
	 * @param pv
	 * @param pkCgOpList
	 * @return
	 */
	private OpGyAmtVo newGyChronicOpSt(OpGyAmtVo amtVo,PvEncounter pv,List<String> pkCgOpList){
		BigDecimal amtPi = new BigDecimal(0);	//记录患者自付
		BigDecimal amtHp = new BigDecimal(0);	//记录医保支付
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("pkHp", pv.getPkInsu());
		paramMap.put("nameDiag", amtVo.getNameDiag());
		paramMap.put("pkList", pkCgOpList);
		
		/**判断患者是否属于慢病类型,不属于按照自费处理*/
		Integer cnt = ogCgStrategyPubMapper.qryChronicTypeByDiag(paramMap);//查询诊断是否属于慢病类型
		if(cnt<=0)//自费策略
			return gyExpenseOpSt(amtVo, pv,pkCgOpList);
		
		//查询公医慢病门诊待结算金额
		Map<String,Object> amtMap = ogCgStrategyPubMapper.qrygyChronicOpAmt(paramMap);
		if(amtMap!=null && amtMap.size()>0){
			BigDecimal amt = BigDecimal.valueOf(CommonUtils.getDoubleObject(amtMap.get("amt")));//总金额
			BigDecimal amtHppi = BigDecimal.valueOf(CommonUtils.getDoubleObject(amtMap.get("amtHppi")));//患者支付
			BigDecimal amtHppi2 = BigDecimal.valueOf(CommonUtils.getDoubleObject(amtMap.get("amtHppi2")));//患者支付2
			//amt_pi=amt_hppi
			amtPi = amtHppi.add(amtHppi2);
			//amt_hp=amt-amt_hppi
			amtHp = amt.subtract(amtPi);
		}
		
		//查询所有待结算的明细
		List<BlOpDt> blOpDts = ogCgStrategyPubMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				//患者优惠
				amtVo.setDiscAmount(amtVo.getDiscAmount().add(BigDecimal.valueOf(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}
		
		amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));
		//重新结算医保支付(amountSt-amountPi)
		amtHp = amtVo.getAmountSt().subtract(amtVo.getAmountPi());
		amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return amtVo;
	}
	
	/**
	 * 公医慢病结算策略
	 * @param amtVo
	 * @param pv
	 */
	private OpGyAmtVo gyChronicOpSt(OpGyAmtVo amtVo,PvEncounter pv,List<String> pkCgOpList){
		BigDecimal amtPi = new BigDecimal(0);	//记录患者自付
		BigDecimal amtHp = new BigDecimal(0);	//记录医保支付
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("nameDiag", amtVo.getNameDiag());
		paramMap.put("pkList", pkCgOpList);
		
		/**判断患者是否属于慢病类型,不属于按照自费处理*/
		Integer cnt = ogCgStrategyPubMapper.qryChronicTypeByDiag(paramMap);//查询诊断是否属于慢病类型
		if(cnt<=0)//自费策略
			return gyExpenseOpSt(amtVo, pv,pkCgOpList);
		
		//查询公医慢病门诊待结算金额
		Map<String,Object> amtMap = ogCgStrategyPubMapper.qrygyChronicOpAmt(paramMap);
		if(amtMap!=null && amtMap.size()>0){
			BigDecimal amt = BigDecimal.valueOf(CommonUtils.getDoubleObject(amtMap.get("amt")));//总金额
			BigDecimal amtHppi = BigDecimal.valueOf(CommonUtils.getDoubleObject(amtMap.get("amtHppi")));//患者支付
			
			//amt_pi=amt_hppi
			amtPi = amtHppi;
			//amt_hp=amt-amt_hppi
			amtHp = amt.subtract(amtHppi);
		}
		
		//查询所有待结算的明细
		List<BlOpDt> blOpDts = ogCgStrategyPubMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				//患者优惠
				amtVo.setDiscAmount(amtVo.getDiscAmount().add(BigDecimal.valueOf(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}
		
		amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));
		//重新结算医保支付(amountSt-amountPi)
		amtHp = amtVo.getAmountSt().subtract(amtVo.getAmountPi());
		amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return amtVo;
	}
	
	/**
	 * 门诊特殊病逻辑
	 * @param amtVo
	 * @param pv
	 * @param pkCgOpList
	 * @return
	 */
	private OpGyAmtVo newGySpdisOpSt(OpGyAmtVo amtVo,PvEncounter pv,List<String> pkCgOpList){
		BigDecimal amtPi = new BigDecimal(0);	//记录患者自付
		BigDecimal amtHp = new BigDecimal(0);	//记录医保支付
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkPi", pv.getPkPi());
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("nameDiag", amtVo.getNameDiag());
		paramMap.put("dateBegin", DateUtils.getDate("yyyyMMddHHmmss").substring(0, 8)+"000000");
		paramMap.put("dateEnd", DateUtils.getDate("yyyyMMddHHmmss").substring(0, 8)+"235959");
		paramMap.put("pkList", pkCgOpList);
		
		BigDecimal diseAmt = new BigDecimal(0);//病种月限额
		BigDecimal diseAmtYear = new BigDecimal(0);//病种年限额
		BigDecimal amtCur = new BigDecimal(0);//待结算总金额
		BigDecimal amtPiCur = new BigDecimal(0);//待结算患者支付金额
		BigDecimal amtPiCur2 = new BigDecimal(0);//待结算患者支付金额(全自费金额)
		BigDecimal amtHpCur = new BigDecimal(0);//待结算医保支付金额
		BigDecimal stAmtTotal = new BigDecimal(0);//已结算总金额
		BigDecimal stAmtPiTotal = new BigDecimal(0);//已结算患者支付金额
		BigDecimal stAmtHpTotal = new BigDecimal(0);//已结算医保支付金额
		
		Map<String,Object> diseMap = ogCgStrategyPubMapper.qrySpdisInfoByPv(paramMap);
		//特殊病信息为空时或者门诊特殊病有效期已过（flag_valid=‘0’），按全自费处理
		if(diseMap==null || diseMap.size()<=0 ||
				CommonUtils.getString(diseMap.get("flagValid")).equals("0")){
			return gyExpenseOpSt(amtVo, pv,pkCgOpList);
		}else{
			diseAmt = BigDecimal.valueOf(CommonUtils.getDouble(diseMap.get("amount")));
			diseAmtYear = BigDecimal.valueOf(CommonUtils.getDouble(diseMap.get("amountYear")));
		}
		
		//查询公医特病门诊待结算金额
		Map<String,Object> amtMap = ogCgStrategyPubMapper.qrySpdisOpAmt(paramMap);
		
		if(amtMap!=null && amtMap.size()>0){
			amtCur = BigDecimal.valueOf(CommonUtils.getDouble(amtMap.get("amtCur")));
			amtPiCur = BigDecimal.valueOf(CommonUtils.getDouble(amtMap.get("amtPiCur")));
			amtPiCur2 = BigDecimal.valueOf(CommonUtils.getDouble(amtMap.get("amtPiCur2")));
			amtHpCur = BigDecimal.valueOf(CommonUtils.getDouble(amtMap.get("amtHpCur")));
		}
		
		//dise.amount>0时，获取本月已结算数据
		if(diseAmt.compareTo(BigDecimal.valueOf(0D))>0){
			//获取本月已结算信息
			paramMap.put("euPvtype", "1");
			paramMap.put("dateBegin", DateUtils.getFirstDayOfGiven(DateUtils.getDate("yyyyMMdd"),"yyyyMMdd",2));
			paramMap.put("dateEnd", DateUtils.getFirstDayOfNext(DateUtils.getDate("yyyyMMdd"),"yyyyMMdd",2));
		}else if(diseAmtYear.compareTo(BigDecimal.valueOf(0D))>0){//dise.amount_year>0时，获取本年度已结算数据
			//获取本年已结算信息
			paramMap.put("euPvtype", "3");
			paramMap.put("dateBegin", DateUtils.getFirstDayOfGiven(DateUtils.getDate("yyyyMMdd"),"yyyyMMdd",1));
			paramMap.put("dateEnd", DateUtils.getFirstDayOfNext(DateUtils.getDate("yyyyMMdd"),"yyyyMMdd",1));
		}else{
			//获取本月已结算信息
			paramMap.put("euPvtype", "1");
			paramMap.put("dateBegin", DateUtils.getFirstDayOfGiven(DateUtils.getDate("yyyyMMdd"),"yyyyMMdd",2));
			paramMap.put("dateEnd", DateUtils.getFirstDayOfNext(DateUtils.getDate("yyyyMMdd"),"yyyyMMdd",2));
		}
		//获取本月\本年已结算信息
		Map<String,Object> stAmtMap = ogCgStrategyPubMapper.qrySpdisStOpAmt(paramMap);
		if(stAmtMap!=null && stAmtMap.size()>0){
			stAmtTotal = BigDecimal.valueOf(CommonUtils.getDouble(stAmtMap.get("amtTotal")));
			stAmtPiTotal = BigDecimal.valueOf(CommonUtils.getDouble(stAmtMap.get("amtPiTotal")));
			stAmtHpTotal = BigDecimal.valueOf(CommonUtils.getDouble(stAmtMap.get("amtHpTotal")));
		}
		
		//dise.amount>0时
		if(diseAmt.compareTo(BigDecimal.valueOf(0D))>0){
			if(stAmtHpTotal.compareTo(diseAmt)>=0){//当amt_hp_total >=dise.amount时（本月记账额已超过病种限额）
				//amt_pi=amt_cur(本次结算总金额)
				amtPi = amtCur;
				//amt_hp=0
				amtHp = new BigDecimal(0);
			}else if((stAmtHpTotal.add(amtHpCur)).compareTo(diseAmt)>=0){//当amt_hp_total+amt_hp_cur>=dise.amount时（本月记账额+本次待记账额超过病种限额）
				//amt_pi=amt_pi_cur+(amt_hp_total+amt_hp_cur-dise.amount)
				amtPi = amtPiCur.add((stAmtHpTotal.add(amtHpCur).subtract(diseAmt)));
				//amt_pi = amtPi+amt_pi_cur2
				amtPi = amtPi.add(amtPiCur2);
				//amt_hp=amt_cur-amt_pi
				amtHp = amtCur.subtract(amtPi);
			}else if((stAmtHpTotal.add(amtHpCur)).compareTo(diseAmt)<0){//当amt_hp_total+amt_hp_cur<dise.amount时（本月记账额+本次待记账额未超过病种限额）
				//amt_pi=amt_pi_cur
				amtPi = amtPiCur;
				//amt_pi = amtPi+amt_pi_cur2
				amtPi = amtPi.add(amtPiCur2);
				//amt_hp=amt_hp_cur
				amtHp = amtHpCur;
			}
		}else if(diseAmtYear.compareTo(BigDecimal.valueOf(0D))>0){
			if(stAmtHpTotal.compareTo(diseAmtYear)>=0){//当amt_hp_total >=dise.amount时（本月记账额已超过病种限额）
				//amt_pi=amt_cur(本次结算总金额)
				amtPi = amtCur;
				//amt_hp=0
				amtHp = new BigDecimal(0);
			}else if((stAmtHpTotal.add(amtHpCur)).compareTo(diseAmtYear)>=0){//当amt_hp_total+amt_hp_cur>=dise.amount时（本月记账额+本次待记账额超过病种限额）
				//amt_pi=amt_pi_cur+(amt_hp_total+amt_hp_cur-dise.amount)
				amtPi = amtPiCur.add((stAmtHpTotal.add(amtHpCur).subtract(diseAmtYear)));
				//amt_pi = amtPi+amt_pi_cur2
				amtPi = amtPi.add(amtPiCur2);
				//amt_hp=amt_cur-amt_pi
				amtHp = amtCur.subtract(amtPi);
			}else if((stAmtHpTotal.add(amtHpCur)).compareTo(diseAmtYear)<0){//当amt_hp_total+amt_hp_cur<dise.amount时（本月记账额+本次待记账额未超过病种限额）
				//amt_pi=amt_pi_cur
				amtPi = amtPiCur;
				//amt_pi = amtPi+amt_pi_cur2
				amtPi = amtPi.add(amtPiCur2);
				//amt_hp=amt_hp_cur
				amtHp = amtHpCur;
			}
		}else{//月限额和年限额都为0
			//amt_pi=amt_pi_cur
			amtPi = amtPiCur;
			//amt_pi = amtPi+amt_pi_cur2
			amtPi = amtPi.add(amtPiCur2);
			//amt_hp=amt_hp_cur
			amtHp = amtHpCur;
		}
		
		//查询所有待结算的明细
		List<BlOpDt> blOpDts = ogCgStrategyPubMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				//患者优惠
				amtVo.setDiscAmount(amtVo.getDiscAmount().add(BigDecimal.valueOf(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}
		
		amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));
		//重新结算医保支付(amountSt-amountPi)
		amtHp = amtVo.getAmountSt().subtract(amtVo.getAmountPi());
		amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return amtVo;
		
	}
	
	/**
	 * 公医门诊特殊病策略
	 * @param amtVo
	 * @param pv
	 */
	private OpGyAmtVo gySpdisOpSt(OpGyAmtVo amtVo,PvEncounter pv,List<String> pkCgOpList){
		BigDecimal amtPi = new BigDecimal(0);	//记录患者自付
		BigDecimal amtHp = new BigDecimal(0);	//记录医保支付
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("nameDiag", amtVo.getNameDiag());
		paramMap.put("dateBegin", DateUtils.getDate("yyyyMMddHHmmss").substring(0, 8)+"000000");
		paramMap.put("dateEnd", DateUtils.getDate("yyyyMMddHHmmss").substring(0, 8)+"235959");
		paramMap.put("pkList", pkCgOpList);
		
		Map<String,Object> diseMap = ogCgStrategyPubMapper.qrySpdisInfoByPv(paramMap);
		//特殊病信息为空时或者门诊特殊病有效期已过（flag_valid=‘0’），按全自费处理
		if(diseMap==null || diseMap.size()<=0 ||
				CommonUtils.getString(diseMap.get("flagValid")).equals("0"))
			return gyExpenseOpSt(amtVo, pv,pkCgOpList);
		
		//按患者医保类型下的住院比例重新计算记费明细
		List<BlOpDtVo> opVoList = ogCgStrategyPubMapper.qryOpListByPv(paramMap);
		List<BlOpDt> upDtList = new ArrayList<>();
		if(opVoList!=null && opVoList.size()>0){
			for(BlOpDtVo vo : opVoList){
				vo.setRatioSelf(vo.getRetePi());
				//amount_hppi=price*quan*ratio_self
				vo.setAmountHppi(MathUtils.mul(MathUtils.mul(vo.getPrice(), vo.getQuan()), vo.getRatioSelf()));
				//amount_pi=amount_hppi-(price_org*quan*ratio_disc)
				Double amt = MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(MathUtils.mul(vo.getPriceOrg(), vo.getQuan()), vo.getRatioDisc()));
				if(MathUtils.compareTo(amt, 0D)<0 && vo.getQuan()>0){
					vo.setAmountPi(0D);
				}else{
					vo.setAmountPi(amt);
				}
				
				try {
					upDtList.add((BlOpDt)vo.clone());
				} catch (CloneNotSupportedException e) {
					throw new BusException("克隆转换BlOpDt对象时发生错误，请检查!");
				}
			}
			//更新费用明细
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(BlOpDt.class), upDtList);
		}
		
		/**特病策略计算，返回计算结果*/
		paramMap.put("pkPi", pv.getPkPi());
		paramMap.put("dateBegin", DateUtils.getFirstDayOfGiven(DateUtils.getDate("yyyyMMdd"),"yyyyMMdd",2));
		paramMap.put("dateEnd", DateUtils.getFirstDayOfNext(DateUtils.getDate("yyyyMMdd"),"yyyyMMdd",2));
		//查询公医特病门诊待结算金额
		Map<String,Object> amtMap = ogCgStrategyPubMapper.qrySpdisOpAmt(paramMap);
		//获取本月已结算信息
		Map<String,Object> stAmtMap = ogCgStrategyPubMapper.qrySpdisStOpAmt(paramMap);
		
		BigDecimal diseAmt = new BigDecimal(0);//病种限额
		BigDecimal amtCur = new BigDecimal(0);//待结算总金额
		BigDecimal amtPiCur = new BigDecimal(0);//待结算患者支付金额
		BigDecimal amtHpCur = new BigDecimal(0);//待结算医保支付金额
		BigDecimal stAmtTotal = new BigDecimal(0);//已结算总金额
		BigDecimal stAmtPiTotal = new BigDecimal(0);//已结算患者支付金额
		BigDecimal stAmtHpTotal = new BigDecimal(0);//已结算医保支付金额
		
		diseAmt = BigDecimal.valueOf(CommonUtils.getDouble(diseMap.get("amount")));
		if(amtMap!=null && amtMap.size()>0){
			amtCur = BigDecimal.valueOf(CommonUtils.getDouble(amtMap.get("amtCur")));
			amtPiCur = BigDecimal.valueOf(CommonUtils.getDouble(amtMap.get("amtPiCur")));
			amtHpCur = BigDecimal.valueOf(CommonUtils.getDouble(amtMap.get("amtHpCur")));
		}
		if(stAmtMap!=null && stAmtMap.size()>0){
			stAmtTotal = BigDecimal.valueOf(CommonUtils.getDouble(stAmtMap.get("amtTotal")));
			stAmtPiTotal = BigDecimal.valueOf(CommonUtils.getDouble(stAmtMap.get("amtPiTotal")));
			stAmtHpTotal = BigDecimal.valueOf(CommonUtils.getDouble(stAmtMap.get("amtHpTotal")));
		}
		
		if(stAmtHpTotal.compareTo(diseAmt)>=0){//当amt_hp_total >=dise.amount时（本月记账额已超过病种限额）
			//amt_pi=amt_cur(本次结算总金额)
			amtPi = amtCur;
			//amt_hp=0
			amtHp = new BigDecimal(0);
		}else if((stAmtHpTotal.add(amtHpCur)).compareTo(diseAmt)>=0){//当amt_hp_total+amt_hp_cur>=dise.amount时（本月记账额+本次待记账额超过病种限额）
			//amt_pi=amt_pi_cur+(amt_hp_total+amt_hp_cur-dise.amount)
			amtPi = amtPiCur.add((stAmtHpTotal.add(amtHpCur).subtract(diseAmt)));
			//amt_hp=amt_cur-amt_pi
			amtHp = amtCur.subtract(amtPi);
		}else if((stAmtHpTotal.add(amtHpCur)).compareTo(diseAmt)<0){//当amt_hp_total+amt_hp_cur<dise.amount时（本月记账额+本次待记账额未超过病种限额）
			//amt_pi=amt_pi_cur
			amtPi = amtPiCur;
			//amt_hp=amt_hp_cur
			amtHp = amtHpCur;
		}
		
		//查询所有待结算的明细
		List<BlOpDt> blOpDts = ogCgStrategyPubMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				//患者优惠
				amtVo.setDiscAmount(amtVo.getDiscAmount().add(BigDecimal.valueOf(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}
		
		amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));
		//重新结算医保支付(amountSt-amountPi)
		amtHp = amtVo.getAmountSt().subtract(amtVo.getAmountPi());
		amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return amtVo;
	}
	
	/**
	 * 公医自费策略
	 * @param amtVo
	 * @param pv
	 */
	private OpGyAmtVo gyExpenseOpSt(OpGyAmtVo amtVo,PvEncounter pv,List<String> pkCgOpList){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("pkList", pkCgOpList);
		
		//查询所有待结算的明细
		List<BlOpDt> blOpDts = ogCgStrategyPubMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setAmountPi(amtVo.getAmountPi().add(BigDecimal.valueOf(bpt.getAmountPi())));
			//医保优惠计费部分
			//amtVo.setAmountInsu(amtVo.getAmountInsu().add(BigDecimal.valueOf(((bpt.getPriceOrg() - bpt.getPrice()) + (bpt.getPriceOrg() * (1 - bpt.getRatioSelf()))) * bpt.getQuan())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				//患者优惠
				amtVo.setDiscAmount(amtVo.getDiscAmount().add(BigDecimal.valueOf(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}
		
		amtVo.setAmountPi(amtVo.getAmountSt());
		amtVo.setAmountInsu(new BigDecimal(0));
		
		return amtVo;
	}
	
	/**
	 * 获取amount_ins_drug>0的记录数
	 * @param stDrugList
	 * @return
	 */
	private Integer getInsDrugCount(List<Map<String,Object>> stDrugList){
		Integer count = 0;
		for(Map<String,Object> map : stDrugList){
			BigDecimal amountInsDrug = BigDecimal.valueOf(CommonUtils.getDouble(map.get("amountInsDrug")));
			if(amountInsDrug.compareTo(new BigDecimal(0))>0)
				count++;
		}
		return count;
	}
	
	/**
	 * 校验收费项目数量是否超过单日最大记费数量
	 * @param blCgPubParamVos
	 */
	public void checkItemOpCg(List<ItemPriceVo> itemlist,PvEncounter pv){
		CheckItemCgVo cgVo = this.checkItemOpCgNum(itemlist,pv);
		if(!cgVo.getErrFlag()){
			throw new BusException(cgVo.getErrMsg());
		}
	}
	
	/**
	 * 校验收费项目数量是否超过单日最大记费数量
	 * @param blCgPubParamVos
	 * @return
	 */
	public CheckItemCgVo checkItemOpCgNum(List<ItemPriceVo> itemlist,PvEncounter pv){
		if(itemlist==null||itemlist.size()<=0) return null;
		//查询收费项目及其数量限制属性
		List<String> pkList = itemlist.stream()
				.filter(m-> !CommonUtils.isEmptyString(m.getFlagPd()) && !"1".equals(m.getFlagPd()) && !CommonUtils.isEmptyString(m.getPkItemOld()))
				.map(ItemPriceVo :: getPkItemOld)
				.collect(Collectors.toList());
		List<ItemCgNumVo> itemCgList = ogCgStrategyPubMapper.qryItemCgNum(pkList);
		List<Map<String,Object>> mapList = new ArrayList<>();
		if(itemCgList!=null && itemCgList.size()>0){
			
			for(ItemPriceVo paramVo: itemlist){
				if(BlcgUtil.converToTrueOrFalse(paramVo.getFlagPd()))
					continue;
				for(ItemCgNumVo cgVo:itemCgList){
					Map<String,Object> map = new HashMap<>();
					if(paramVo.getPkItemOld().equals(cgVo.getPkItem()) &&
							!CommonUtils.isEmptyString(cgVo.getValAttr())){
						String dateHap =  DateUtils.getDate("yyyyMMdd");
						//获取患者指定日期下的已记费数量
						String cgNum = ogCgStrategyPubMapper.qryOpCgNumByPv(
								paramVo.getPkPv(),dateHap, paramVo.getPkItemOld());
						
						if(CommonUtils.isEmptyString(cgNum)) cgNum="0";
						if(paramVo.getQuanOld()==null) paramVo.setQuanOld(0D);
							
						//计费数量
						Double itemCged = Double.valueOf(cgNum) + paramVo.getQuanOld();
						Integer orderSn = paramVo.getOrdsn();
						
						boolean flag = itemCged > new Double(cgVo.getValAttr()) ? false : true;
						
						if(!flag){
							map.put("name",cgVo.getName());
							map.put("quanCg",new Double(paramVo.getQuanOld()).intValue());//本次记费数量
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
	
	/**
	 * 公医患者保存ins_gzgy_st表
	 * @param stVo
	 * @param amtVo
	 */
	public void saveGzgySt(BlSettle stVo,OpGyAmtVo amtVo){
		Map<String,Object> qryMap = new HashMap<>();
		qryMap.put("pkPv", stVo.getPkPv());
		Map<String,Object> hpMap = ogCgStrategyPubMapper.qryPvInsuAttrVal(qryMap);
		
		if(hpMap!=null && hpMap.size()>0 &&
				 hpMap.get("valAttr")!=null && EnumerateParameter.ONE.equals(CommonUtils.getString(hpMap.get("valAttr")))){
			Map<String,Object> codeOrgMap = DataBaseHelper.queryForMap("select code_org from bd_ou_org where pk_org = ?", 
					UserContext.getUser().getPkOrg());
			
			//组织结算数据写表ins_gzgy_st
			InsGzgySt insGzgySt = new InsGzgySt();
			
			if(codeOrgMap!=null && codeOrgMap.size()>0 && codeOrgMap.get("codeOrg")!=null)
				insGzgySt.setCodeOrg(CommonUtils.getString(codeOrgMap.get("codeOrg")));		//机构编码
			
			insGzgySt.setPkPv(stVo.getPkPv());	//就诊记录
			insGzgySt.setPkHp(stVo.getPkInsurance()); 	//医保类型
			insGzgySt.setPkSettle(stVo.getPkSettle()); 	//结算主键
			insGzgySt.setAmount(amtVo.getAmountSt().doubleValue()); 		//结算总额
			insGzgySt.setAmountPi(amtVo.getAmountPi().doubleValue());	//患者自付金额
			insGzgySt.setAmountIns(amtVo.getAmountInsu().doubleValue());	//医保支付金额
			insGzgySt.setAmountInsDrug(amtVo.getAmountDrug()==null ? 0D : amtVo.getAmountDrug().doubleValue());
			insGzgySt.setAmountUnit(0D);//单位支付金额
			ApplicationUtils.setDefaultValue(insGzgySt, true);
			
			/**保存ins_gzgy_st表*/
			DataBaseHelper.insertBean(insGzgySt);
		}
	}
}
