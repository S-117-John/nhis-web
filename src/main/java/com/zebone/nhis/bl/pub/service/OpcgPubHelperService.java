package com.zebone.nhis.bl.pub.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.bl.pub.dao.OpcgQryWrapMapper;
import com.zebone.nhis.bl.pub.support.CgProcessUtils;
import com.zebone.nhis.bl.pub.syx.dao.CgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.service.OpCgDistrictService;
import com.zebone.nhis.bl.pub.syx.vo.HpVo;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;


@Service
public class OpcgPubHelperService {

	@Autowired
	private OpcgQryWrapMapper opcgQryWrapMapper;
	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	@Autowired
	private PriceStrategyService priceStrategyService;
	@Autowired
	private OpCgDistrictService opCgDistrictService;
	@Resource
	private CgStrategyPubMapper cgStrategyPubMapper;
	
	/**
	 * ??????????????????????????????????????????????????????(?????????????????????????????????)
	 * ?????????:007002003001
	 * @param mapParam
	 *            [pkPi,pkPv,curDate]
	 * @return
	 * @author yangxue
	 */
	public List<BlPatiCgInfoNotSettleVO> queryPatiCgInfoNotSettle(Map<String, Object> paramMap) {
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkPv")))
			throw new BusException("??????????????????????????????????????????pkPv???");
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		List<BlPatiCgInfoNotSettleVO> dtlist =  opcgQryWrapMapper.queryNoSettleInfoForCg(paramMap);
        if("1".equals(CommonUtils.getString(paramMap.get("notDisplayFlagPv")))){
			 return dtlist;
		} //????????????????????????????????????
		StringBuilder sql = new StringBuilder("select count(1)  From bl_op_dt back where back.pk_pv=? and ");
		sql.append(" back.flag_pv='1' and back.flag_settle='1'");
		sql.append(" and back.quan<0  and  not exists (select 1 ");
		sql.append(" from bl_op_dt dt where back.pk_cgop_back=dt.pk_cgop ) ");
		//???????????????????????????
		int pvcnt = DataBaseHelper.queryForScalar(sql.toString(),Integer.class,new Object[]{paramMap.get("pkPv")});
		if("0".equals(ApplicationUtils.getSysparam("BL0041", false)) || pvcnt>0){
			//????????????????????????????????????
//			paramMap.put("flagPv", "1".equals(CommonUtils.getString(
//					paramMap.get("isNotShowPv"))) ? "0" : "1");
			paramMap.put("flagPv", "1");
			dtlist =  opcgQryWrapMapper.queryNoSettleInfoForCg(paramMap);
			return dtlist;
		}
		
		//????????????????????????????????????BL0041,??????????????????????????????
		if("1".equals(ApplicationUtils.getSysparam("BL0041", false))){
//			 List<BlPatiCgInfoNotSettleVO> pvlist =  opcgQryWrapMapper.querySettlePvInfo(paramMap);
//			 if(pvlist!=null&&pvlist.size()>0)
//			     dtlist.addAll(pvlist);
			//?????????????????????????????????
			Integer cnt = DataBaseHelper.queryForScalar(
					"select count(1) from bl_op_dt where FLAG_SETTLE = '0' and flag_pv='1' and quan<0 and pk_pv = ?",
					Integer.class, new Object[]{paramMap.get("pkPv")});
			//?????????????????????
			Integer stCnt = DataBaseHelper.queryForScalar(
					"select count(1) from bl_settle where dt_sttype = '01' and pk_pv = ?",
					Integer.class, new Object[]{paramMap.get("pkPv")});
			if(cnt>0 || stCnt>0){
				//????????????????????????????????????
				paramMap.put("flagPv", "1".equals(CommonUtils.getString(
						paramMap.get("isNotShowPv"))) ? "0" : "1");
				dtlist =  opcgQryWrapMapper.queryNoSettleInfoForCg(paramMap);
				return dtlist;
			}
				
			
			/**???BL0041????????????1??????????????????????????????????????????????????????????????????????????????????????????*/
			//??????????????????????????????????????????
			List<BlOpDt> ipcFeeList = opcgQryWrapMapper.qryStIpcFeeByPv(paramMap);
			if(ipcFeeList!=null && ipcFeeList.size()>0){
				List<BlOpDt> newPvOpList = new ArrayList<>();
				for(BlOpDt pvDt : ipcFeeList){
					BlOpDt feeVo = pvDt;
					BlOpDt zOpvo = new BlOpDt();
					BlOpDt fOpvo = new BlOpDt();
					try {
						zOpvo = (BlOpDt)feeVo.clone();
						fOpvo = (BlOpDt)feeVo.clone();
					} catch (CloneNotSupportedException e) {
						throw new BusException("????????????BlOpDt?????????????????????????????????!");
					}
					Date dateHap = DateUtils.strToDate(DateUtils.getDateTime(),"yyyy-MM-dd");
					//??????????????????
					zOpvo.setFlagPv("1");
					zOpvo.setPkCgop(null);
					zOpvo.setFlagSettle("0");
					zOpvo.setPkSettle(null);
					zOpvo.setDateHap(dateHap);
					zOpvo.setDateCg(new Date());
					zOpvo.setAmountAdd(zOpvo.getAmountAdd()==null?0D:zOpvo.getAmountAdd());
					
					//??????????????????
					fOpvo.setFlagPv("1");
					fOpvo.setPkCgop(null);
					fOpvo.setPkCgopBack(pvDt.getPkCgop()); //?????????????????????
					fOpvo.setFlagSettle("0");
					fOpvo.setPkSettle(null);
					fOpvo.setDateHap(dateHap);
					fOpvo.setDateCg(new Date());
					fOpvo.setQuan(fOpvo.getQuan()*-1);
					fOpvo.setAmount(fOpvo.getAmount()*-1);
					fOpvo.setAmountPi(fOpvo.getAmountPi()*-1);
					fOpvo.setAmountHppi(fOpvo.getAmountHppi()*-1);
					fOpvo.setAmountAdd(fOpvo.getAmountAdd()==null?0D:fOpvo.getAmountAdd()*-1);
					ApplicationUtils.setDefaultValue(zOpvo, true);
					ApplicationUtils.setDefaultValue(fOpvo, true);
					
					//?????????????????????
					newPvOpList.add(zOpvo);
					newPvOpList.add(fOpvo);
				}
				
				//??????????????????????????????
				if("1".equals(ApplicationUtils.getSysparam("BL0023", false))){
					//????????????????????????
					PvEncounter pvvo = DataBaseHelper.queryForBean(
							"select * from pv_encounter where pk_pv = ?",
							PvEncounter.class, CommonUtils.getString(paramMap.get("pkPv")));
					
					//1.?????????????????????????????????????????????
					BdHp cateHp = null; 
					if(!CommonUtils.isEmptyString(pvvo.getPkPicate())){
						Map<String,Object> cateMap = new HashMap<>();
						cateMap.put("pkPicate", pvvo.getPkPicate());
						List<HpVo> hpList = cgStrategyPubMapper.queryHpList(cateMap);
						if(hpList!=null && hpList.size()>0)
							cateHp = hpList.get(0);
					}
					
					//2.?????????????????????????????????
					HpVo mainHp = null;
					if(!CommonUtils.isEmptyString(pvvo.getPkInsu())){
						Map<String,Object> hpMap = new HashMap<>();
						hpMap.put("pkHp", pvvo.getPkInsu());//????????????????????????????????????
						List<HpVo> hpList = cgStrategyPubMapper.queryHpList(hpMap);
						/**?????????????????????????????????*/
						String valAttr = cgStrategyPubMapper.qryHpValAttr(pvvo.getPkInsu());
						
						if(hpList!=null && hpList.size()>0){
							mainHp = hpList.get(0);
							mainHp.setValAttr(valAttr);
						}
							
					}
					//???blOpDt????????????????????????????????????????????????????????????????????????Bl_op_dt???
					opCgDistrictService.updateChargeOp(pvvo,mainHp,cateHp,newPvOpList,true,pvvo.getPkInsu(),pvvo.getPkPicate());
				}else{
					//??????bl_op_dt???
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class),newPvOpList);
				}
			}
			//????????????????????????????????????
			paramMap.put("flagPv", "1".equals(CommonUtils.getString(
					paramMap.get("isNotShowPv"))) ? "0" : "1");
			//???????????????????????????,??????????????????
			//paramMap.put("flagPv", "1".equals(CommonUtils.getString(paramMap.get("notDisplayFlagPv")))?"0":"1");
			dtlist =  opcgQryWrapMapper.queryNoSettleInfoForCg(paramMap);
		}
		return dtlist;
	}
	
	/**
	 * ??????????????????????????????????????????????????????(???????????????)
	 * @param mapParam [pkPi,curDate]
	 * @return
	 */
	public List<BlPatiCgInfoNotSettleVO> queryPatiCgInfoNotSettleOnlyPv(Map<String, Object> mapParam) {
		List<BlPatiCgInfoNotSettleVO> resultList = new ArrayList<>();
		
		/**
		 * ??????????????????????????????????????????????????????
		 */
		List<PvEncounter> pvEncounters =  new ArrayList<>();
		if(mapParam.get("pkPv") != null){
		String pvSql = "select * from pv_encounter where eu_pvtype < '3' and eu_status <> '9' and "
				+ " pk_pv = ? and pk_org= ?";
	    pvEncounters = DataBaseHelper.queryForList(pvSql, PvEncounter.class, 
				new Object[]{CommonUtils.getString(mapParam.get("pkPv")), CommonUtils.getString(mapParam.get("pkOrg"))});
	
		}else {
		String pvSql = "select * from pv_encounter where eu_pvtype < '3' and eu_status <> '9' and "
					+ " pk_pi = ? and pk_org= ?";
		pvEncounters = DataBaseHelper.queryForList(pvSql, PvEncounter.class, 
					new Object[]{CommonUtils.getString(mapParam.get("pkPi")), CommonUtils.getString(mapParam.get("pkOrg"))});
		}
		/**
		 * ???????????????????????????????????????????????????,???????????????
		 */
		//?????????????????????????????????
		Set<String> keySet = new HashSet<>();
		for (PvEncounter pvEncounter : pvEncounters) {
			if(!StringUtils.isEmpty(pvEncounter.getPkPv())){
				keySet.add(pvEncounter.getPkPv());
			}
		}
		
		String blOpDtSql = "select * from bl_op_dt where flag_settle = '0' and flag_acc = '0' and flag_pv = '1'"
				+ " and  pk_pv in (" + CommonUtils.convertSetToSqlInPart(keySet, "pk_pv") + ") ";
		
		List<BlOpDt> blOpdtLists = DataBaseHelper.queryForList(blOpDtSql, BlOpDt.class, new Object[]{});
		//???????????????????????????????????????
		List<PiCate> piCates = getPiCates(pvEncounters);
		
		/**
		 * ??????????????????????????????????????????????????????????????????????????????????????????
		 */
		double herbQuan = 0.00;
		for (BlOpDt blOpdtList : blOpdtLists) {
			BlPatiCgInfoNotSettleVO settleVO = new BlPatiCgInfoNotSettleVO();
			// ????????????
			for (PvEncounter pvEncounter : pvEncounters) {
				for(PiCate piCate : piCates){
					if(!StringUtils.isEmpty(pvEncounter.getPkPicate()) &&
							pvEncounter.getPkPicate().equalsIgnoreCase(piCate.getPkPicate())){
						settleVO.setPicate(piCate.getName());
						break;
					}
				}
				if(!StringUtils.isEmpty(pvEncounter.getPkPv()) &&
						pvEncounter.getPkPv().equalsIgnoreCase(blOpdtList.getPkPv())){
					settleVO.setPkInsu(pvEncounter.getPkInsu());
					settleVO.setPkPv(pvEncounter.getPkPv());
					break;
				}
			}
			//???????????????
			//settleVO.setPkCnord(blOpdtList.getPkCnord());
			settleVO.setPkCgop(blOpdtList.getPkCgop());
			settleVO.setNameCg(blOpdtList.getNameCg());
			//settleVO.setSpec(blOpdtList.getSpec());
			settleVO.setQuanCg(blOpdtList.getQuan());
			settleVO.setPriceOrg(new BigDecimal(blOpdtList.getPriceOrg()));
			settleVO.setAmount(new BigDecimal(blOpdtList.getAmount()));
			
			//?????????????????????????????????
			/* 	private String pkDeptApp; // ??????????????????
				private String pkEmpApp; // ????????????
				private String pkDeptEx; // ????????????
				private String pkItem; // ??????????????????
				private String pkPd; // ????????????
				private String flagPd; // ????????????
			 */
			settleVO.setPkDeptApp(blOpdtList.getPkDeptApp());
			settleVO.setPkEmpApp(blOpdtList.getPkEmpApp());
			settleVO.setPkDeptEx(blOpdtList.getPkDeptEx());
			settleVO.setPkItem(blOpdtList.getPkItem());
			//settleVO.setPkPd(blOpdtList.getPkPd());
			settleVO.setFlagPd(blOpdtList.getFlagPd());

			if(settleVO.getQuan() != null && settleVO.getQuan()==0.0000){
				settleVO.setQuan(herbQuan);
			}
			resultList.add(settleVO);
		}
		return resultList;
	}
	
	
//		// ????????????
//		private List<Map<String, Object>> getBdUnitLists(List<CnOrder> cnOrderLists) {
//			Set<String> keySet = new HashSet<>();
//			for (CnOrder cnOrderList : cnOrderLists) {
//				if(!StringUtils.isEmpty(cnOrderList.getPkUnitDos())){
//					keySet.add(cnOrderList.getPkUnitDos());
//				}
//			}
//			List<Map<String, Object>> bdUnitLists = DataBaseHelper.queryForList("select * from bd_unit where pk_unit in ("
//					+ CommonUtils.convertSetToSqlInPart(keySet, "pk_unit_dos") + ")", new Object[]{});
//			return bdUnitLists;
//		}

		// ??????
//		private List<BdTermFreq> getBdTermFreqLists(List<CnOrder> cnOrderLists) {
//			Set<String> keySet = new HashSet<>();
//			for (CnOrder cnOrderList : cnOrderLists) {
//				if(!StringUtils.isEmpty(cnOrderList.getCodeFreq())){
//					keySet.add(cnOrderList.getCodeFreq());
//				}
//			}
//			List<BdTermFreq> bdTermFreqLists = DataBaseHelper.queryForList("select * from  bd_term_freq where code in (" 
//					+ CommonUtils.convertSetToSqlInPart(keySet, "code_freq") + ")", 
//					BdTermFreq.class, new Object[]{});
//			return bdTermFreqLists;
//		}

		// ????????????
//		private List<BdSupply> getBdSupplyLists(List<CnOrder> cnOrderLists) {
//			Set<String> keySet = new HashSet<>();
//			for (CnOrder cnOrderList : cnOrderLists) {
//				if(!StringUtils.isEmpty(cnOrderList.getCodeSupply())){
//					keySet.add(cnOrderList.getCodeSupply());
//				}
//			}
//			List<BdSupply> bdSupplyLists = DataBaseHelper.queryForList("select * from  bd_supply where code in (" 
//					+ CommonUtils.convertSetToSqlInPart(keySet, "code_supply") + ")", 
//					BdSupply.class, new Object[]{});
//			return bdSupplyLists;
//		}

		// ??????
//		private List<BdOuDept> getBdOuDeptLists(List<CnOrder> cnOrderLists) {
//			Set<String> akeySet = new HashSet<>();
//			Set<String> bkeySet = new HashSet<>();
//			for (CnOrder cnOrderList : cnOrderLists) {
//				// ????????????
//				if(!StringUtils.isEmpty(cnOrderList.getPkDept())){
//					akeySet.add(cnOrderList.getPkDept());
//				}
//				// ????????????
//				if(!StringUtils.isEmpty(cnOrderList.getPkDeptExec())){
//					bkeySet.add(cnOrderList.getPkDeptExec());
//				}
//			}
//			List<BdOuDept> bdOuDeptLists = DataBaseHelper.queryForList("select * from  bd_ou_dept where pk_dept in (" 
//					+ CommonUtils.convertSetToSqlInPart(akeySet, "pk_dept") + ") or pk_dept in (" 
//					+ CommonUtils.convertSetToSqlInPart(bkeySet, "pk_dept_exec") + ")", 
//					BdOuDept.class, new Object[]{});
//			
//			return bdOuDeptLists;
//		}

		//bd_term_diag ??????????????????
//		private List<BdTermDiag> getBdTermDiagLists(List<CnPrescription> CnPrescriptionLists) {
//			Set<String> keySet = new HashSet<>();
//			for(CnPrescription cnPrescriptionList : CnPrescriptionLists){
//				if(!StringUtils.isEmpty(cnPrescriptionList.getPkDiag())){
//					keySet.add(cnPrescriptionList.getPkDiag());
//				}
//			}
//			List<BdTermDiag> bdTermDiagLists = DataBaseHelper.queryForList("select * from  bd_term_diag where pk_diag in (" 
//					+ CommonUtils.convertSetToSqlInPart(keySet, "pk_diag") + ")", BdTermDiag.class, new Object[]{});
//			return bdTermDiagLists;
//		}

		// ??????cn_ord_herb
//		private List<CnOrdHerb> getCnOrdHerbLists(List<BlOpDt> blOpdtLists) {
//			Set<String> akeySet = new HashSet<>();
//			Set<String> bkeySet = new HashSet<>();
//			for (BlOpDt blOpdtList : blOpdtLists) {
//				if(!StringUtils.isEmpty(blOpdtList.getPkPd())){
//					akeySet.add(blOpdtList.getPkPd()); // ??????pk_pd
//				}
//				if(!StringUtils.isEmpty(blOpdtList.getPkCnord())){
//					bkeySet.add(blOpdtList.getPkCnord()); // cg.pk_pd=herb.pk_pd and cg.pk_cnord=herb.pk_cnord 
//				}
//			}
//			List<CnOrdHerb> CnOrdHerbLists = DataBaseHelper.queryForList("select * from  cn_ord_herb where pk_pd in (" 
//					+ CommonUtils.convertSetToSqlInPart(akeySet, "pk_pd") + ") and pk_cnord in (" 
//					+ CommonUtils.convertSetToSqlInPart(bkeySet, "pk_cnord") + ")", 
//						CnOrdHerb.class, new Object[]{});
//			return CnOrdHerbLists;
//		}
//
//		// ????????????
//		private List<Map<String, Object>> getBdUnitDtLists(List<BlOpDt> blOpdtLists) {
//			Set<String> keySet = new HashSet<>();
//			for (BlOpDt blOpdtList : blOpdtLists) {
//				if(!StringUtils.isEmpty(blOpdtList.getPkUnitPd())){
//					keySet.add(blOpdtList.getPkUnitPd()); // ??????bd_unit
//				}
//			}
//			List<Map<String, Object>> bdUnitDtLists = DataBaseHelper.queryForList("select * from bd_unit where pk_unit in ("
//					+ CommonUtils.convertSetToSqlInPart(keySet, "pk_unit_pd") + ")", new Object[]{});
//			return bdUnitDtLists;
//		}

		// ????????????
/*		private List<CnOrder> getCnOrderLists(Map<String, Object> mapParam, List<BlOpDt> blOpdtLists) {
		Set<String> keySet = new HashSet<>();
			for (BlOpDt blOpdtList : blOpdtLists) {
				if(!StringUtils.isEmpty(blOpdtList.getPkCnord())){
					keySet.add(blOpdtList.getPkCnord());
				}
			}
			List<CnOrder> cnOrderLists = DataBaseHelper.queryForList("select * from  cn_order where PK_CNORD in (" 
							+ CommonUtils.convertSetToSqlInPart(keySet, "PK_CNORD") + ") and date_effe >= ?", 
								CnOrder.class, new Object[]{(Date) mapParam.get("curDate")});
			return cnOrderLists;
		}
		*/
		// ????????????????????????
//		private List<BdInvcateItem> getBdInvcateItemLists(List<BlOpDt> blOpdtLists){
//			String pkOrg = UserContext.getUser().getPkOrg();
//			Set<String> keySet = new HashSet<>();
//			for (BlOpDt blOpdtList : blOpdtLists) {
//				if(!StringUtils.isEmpty(blOpdtList.getCodeBill())){
//					keySet.add(blOpdtList.getCodeBill());
//				}
//			}
//			List<BdInvcateItem> bdInvcateItemLists = DataBaseHelper.queryForList("select * from  bd_invcate_item where pk_org=? and code in (" 
//					+ CommonUtils.convertSetToSqlInPart(keySet, "code_bill") + ")", BdInvcateItem.class, new Object[]{pkOrg});
//			
//			return bdInvcateItemLists;
//		}

		// ????????????
//		private List<CnPrescription> getCnPrescriptionLists(List<BlOpDt> blOpdtLists) {
//			Set<String> keySet = new HashSet<>();
//			for (BlOpDt blOpdtList : blOpdtLists) {
//				if(!StringUtils.isEmpty(blOpdtList.getPkPres())){
//					keySet.add(blOpdtList.getPkPres());
//				}
//			}
//			List<CnPrescription> CnPrescriptionLists = DataBaseHelper.queryForList("select * from  CN_PRESCRIPTION where PK_PRES in (" 
//					+ CommonUtils.convertSetToSqlInPart(keySet, "PK_PRES") + ")", CnPrescription.class, new Object[]{});
//			return CnPrescriptionLists;
//		}

		//?????????????????????????????????
//		private List<BlOpDt> getBlOpdtLists(List<PvEncounter> pvEncounters) {
//			/*Set<String> keySet = new HashSet<>();
//			for (PvEncounter pvEncounter : pvEncounters) {
//				if(!StringUtils.isEmpty(pvEncounter.getPkPv())){
//					keySet.add(pvEncounter.getPkPv());
//				}
//			}
//			
//			String blOpDtSql = "select dt.*,(case when temp.del_flag='0' then 1 else 0 end ) as isPrintQrCode,"
//								+ "(case when dt.flag_pd='0' then bi.code else bp.code end) as itemCode from bl_op_dt dt "
//								+ "	LEFT JOIN TEMP_BL_OP_DT temp on dt.PK_CGOP = temp.PK_TEMP_CGOP "
//								+ " left join bd_item bi on bi.pk_item=dt.pk_item"
//								+ " left join bd_pd bp on bp.pk_pd=dt.pk_pd"
//								+ "	where dt.flag_settle = '0' and dt.flag_acc = '0'"
//								+ " and dt.pk_pv in (" + CommonUtils.convertSetToSqlInPart(keySet, "pk_pv") + ") ";
//			
//			List<BlOpDt> blOpdtLists = DataBaseHelper.queryForList(blOpDtSql, BlOpDt.class, new Object[]{});
//			*///?????????mybatis???
//			List<String> pkPvs=new ArrayList<String>();
//			for (PvEncounter pvEncounter : pvEncounters) {
//				if(!StringUtils.isEmpty(pvEncounter.getPkPv())){
//					pkPvs.add(pvEncounter.getPkPv());
//				}
//			}
//			List<BlOpDt> blOpdtLists=opcgQryWrapMapper.qryBlOpdtByPkPvs(pkPvs);
//			return blOpdtLists;
//		}
		
		//???????????????????????????????????????
		private List<PiCate> getPiCates(List<PvEncounter> pvEncounters) {
			Set<String> keySet = new HashSet<>();
			for (PvEncounter pvEncounter : pvEncounters) {
				if(!StringUtils.isEmpty(pvEncounter.getPkPicate())){
					keySet.add(pvEncounter.getPkPicate());
				}
			}
			List<PiCate> piCates = DataBaseHelper.queryForList("select * from pi_cate where "
					+ " pk_picate in (" + CommonUtils.convertSetToSqlInPart(keySet, "pk_picate") + ")", PiCate.class, new Object[]{});
			return piCates;
		}

		/**
		 * ?????????????????????????????????????????????????????????
		 * @param pkCgop
		 * @return ????????????
		 */
		public boolean checkItemBlStatus(List<BlOpDt> blOpDts){
			boolean blStatus = true;
			String sql = "select pk_cgop, flag_settle from bl_op_dt where pk_cgop =?  and DEL_FLAG = '0'";
			//?????????????????????????????????
			for (BlOpDt blOpDt : blOpDts) {
				BlOpDt dbaItem = DataBaseHelper.queryForBean(sql, BlOpDt.class, new Object[]{blOpDt.getPkCgop()});
				blStatus = dbaItem.getFlagSettle().equals(EnumerateParameter.ONE) ? true : false; //???????????????
				if(blStatus){
					break;
				}
			}
			return blStatus;
		} 
		
		/**
		 * ????????????????????????????????????
		 * @param blOpDts
		 * @param pkSettle
		 * @throws BusException
		 */
		public void generateRefoundRecord(List<BlOpDt> blOpDts, String pkSettle) throws BusException {
			generateRefoundRecord(blOpDts, pkSettle,null);
		}

		public void generateRefoundRecord(List<BlOpDt> blOpDts, String pkSettle,BlOpDt def) throws BusException {
			if (CollectionUtils.isEmpty(blOpDts))
				return;
			List<BlOpDt> blOpDtDaos = new ArrayList<BlOpDt>();
			for (BlOpDt blOpDt : blOpDts) {
				blOpDt.setPkCgopBack(blOpDt.getPkCgop());
				blOpDt.setQuan(-1 * blOpDt.getQuan());
				blOpDt.setAmount(-1 * blOpDt.getAmount());
				blOpDt.setAmountPi(-1 * blOpDt.getAmountPi());
				blOpDt.setAmountAdd(-1 * (blOpDt.getAmountAdd()==null?0D:blOpDt.getAmountAdd()));
				blOpDt.setAmountHppi(-1 * (blOpDt.getAmountHppi()==null?0D:blOpDt.getAmountHppi()));
				blOpDt.setPkSettle(pkSettle);
				if(def !=null && StringUtils.isNotBlank(def.getFlagRecharge()))
					blOpDt.setFlagRecharge(def.getFlagRecharge());
				// ???????????????
				ApplicationUtils.setDefaultValue(blOpDt, true);
				blOpDtDaos.add(blOpDt);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), blOpDtDaos);
		}
		/**
		 * ???????????????????????????
		 * @param blOpDts
		 * @throws BusException
		 */
		public String  generateRefoundSettle(BlSettle blSettle) throws BusException {
			return generateRefoundSettle(blSettle,"21");
		}

		public String  generateRefoundSettle(BlSettle blSettle,String dtSttype) throws BusException {
			if (blSettle == null)
				return null;
			User user = UserContext.getUser();
			BlSettle blSettleNot = new BlSettle();
			ApplicationUtils.copyProperties(blSettleNot, blSettle);
			blSettleNot.setDtSttype(dtSttype);// ????????????---->????????????
			blSettleNot.setAmountSt(new BigDecimal(-1).multiply(blSettleNot.getAmountSt()));// ???????????????
			blSettleNot.setAmountInsu(new BigDecimal(-1).multiply(blSettleNot.getAmountInsu()));// ??????????????????
			blSettleNot.setAmountPi(new BigDecimal(-1).multiply(blSettleNot.getAmountPi()));// ????????????
			blSettleNot.setAmountAdd(MathUtils.mul(-1D, blSettleNot.getAmountAdd()));
			blSettleNot.setAmountDisc(MathUtils.mul(-1D, blSettleNot.getAmountDisc()));
			blSettleNot.setAmountPrep(new BigDecimal(-1).multiply(blSettleNot.getAmountPrep()));
			blSettleNot.setAmountRound(new BigDecimal(-1).multiply(blSettleNot.getAmountRound()));
			blSettleNot.setPkSettleCanc(blSettle.getPkSettle());// ??????????????????
			blSettleNot.setDateSt(new Date());
			blSettleNot.setPkDeptSt(user.getPkDept());
			blSettleNot.setPkEmpSt(user.getPkEmp());
			blSettleNot.setNameEmpSt(user.getNameEmp());
			blSettleNot.setPkOrg(user.getPkOrg());
			blSettleNot.setFlagCanc(EnumerateParameter.ZERO);
			blSettleNot.setPkCc(null);
			blSettleNot.setFlagCc(EnumerateParameter.ZERO);
			blSettleNot.setCodeSt(ApplicationUtils.getCode("0604"));
			// ???????????????
			ApplicationUtils.setDefaultValue(blSettleNot, true);
			DataBaseHelper.insertBean(blSettleNot);
			// ????????????????????????
			blSettle.setFlagCanc(EnumerateParameter.ONE);
			ApplicationUtils.setDefaultValue(blSettle, false);
			DataBaseHelper.updateBeanByPk(blSettle, false);
			return blSettleNot.getPkSettle();
		}
		/**
		 * ?????????????????????????????????
		 * @param blOpDts
		 * @throws BusException
		 */
		public void generateRefoundSettleDetail(List<BlSettleDetail> blSettleDetails,String pkSettleCanc) throws BusException {

			if (CollectionUtils.isEmpty(blSettleDetails))
				return;
			List<BlSettleDetail> blSettleDetailDaos = new ArrayList<BlSettleDetail>();
			for (BlSettleDetail blSettleDetail : blSettleDetails) {
				blSettleDetail.setPkSettle(pkSettleCanc);//??????????????????
				blSettleDetail.setAmount(-1 * blSettleDetail.getAmount());
				// ???????????????
				ApplicationUtils.setDefaultValue(blSettleDetail, true);
				blSettleDetailDaos.add(blSettleDetail);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), blSettleDetailDaos);
		}

		/**
		 * ??????????????????????????????????????????
		 * @param blDeposits
		 * @throws BusException
		 */
		public List<BlDeposit> generateRefoundBlDeposits(List<BlDeposit> blDeposits,String pkSettleCanc) throws BusException {

			if (CollectionUtils.isEmpty(blDeposits)){
				//throw new BusException("??????????????????????????????????????????!");
				//????????????????????????????????????????????????????????????0??????????????????????????????
				return null;
			}
			List<BlDeposit> blDepositsDao = new ArrayList<BlDeposit>();
			for (BlDeposit blDeposit : blDeposits) {
				blDeposit.setEuDirect(EnumerateParameter.NEGA);// ???
				blDeposit.setAmount(new BigDecimal(-1).multiply(blDeposit.getAmount()));
				//blDeposit.setPkDepoBack(blDeposit.getPkDepo());
				//blDeposit.setDateReptBack(new Date());// ??????????????????
				blDeposit.setPkSettle(pkSettleCanc);
				User user = UserContext.getUser();// ????????????????????????
				//blDeposit.setPkEmpBack(user.getPkEmp());
				//blDeposit.setNameEmpBack(user.getNameEmp());
				blDeposit.setPkCc(null);
				blDeposit.setFlagCc(EnumerateParameter.ZERO);
				
				blDeposit.setPkEmpPay(user.getPkEmp());
				blDeposit.setDatePay(new Date());
				blDeposit.setNameEmpPay(user.getNameEmp());
				blDeposit.setPkDept(user.getPkDept());
				ApplicationUtils.setDefaultValue(blDeposit, true);
				blDepositsDao.add(blDeposit);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), blDepositsDao);
			
			return blDepositsDao;
		}

		/**
		 * ?????????????????????????????????
		 * @param blInvoice
		 * @throws BusException
		 */
		public void updateRefoundBlInvoice(BlInvoice blInvoice) throws BusException {

			if (blInvoice == null)
				return;
			blInvoice.setFlagCancel(EnumerateParameter.ONE);
			blInvoice.setDateCancel(new Date());
			User user = UserContext.getUser();// ????????????????????????
			blInvoice.setPkEmpCancel(user.getPkEmp());
			blInvoice.setNameEmpCancel(user.getNameEmp());
			DataBaseHelper.updateBeanByPk(blInvoice);
		}
		
		/**
		 * ???????????????????????????
		 * 
		 * ??????????????????????????????????????????????????????????????????
		 * @param opDts
		 * @return
		 */
		public List<BlOpDt> updateDtQuan(List<BlOpDt> opDts) {

			Set<String> pkCgOps = new HashSet<String>();
			for (BlOpDt blOpDt : opDts) {
				pkCgOps.add(blOpDt.getPkCgop());
			}
			String sqlQueryDt = "select * from bl_op_dt where pk_cgop in(" + CommonUtils.convertSetToSqlInPart(pkCgOps, "pk_cgop") + ")";
			List<BlOpDt> blOpDtOlds = DataBaseHelper.queryForList(sqlQueryDt, BlOpDt.class, new Object[] {});
			for (BlOpDt blOpDt : blOpDtOlds) {
				for (BlOpDt opDt : opDts) {
					if (blOpDt.getPkCgop().equals(opDt.getPkCgop())) {
						blOpDt.setQuan(opDt.getQuan());
						blOpDt.setAmount(blOpDt.getQuan() * blOpDt.getPriceOrg());
						double amountMed = ((blOpDt.getPriceOrg() - blOpDt.getPrice()) + (blOpDt.getPriceOrg() * (1 - blOpDt.getRatioSelf()))) * blOpDt.getQuan();
						double amountDis = blOpDt.getAmount() * (1 - blOpDt.getRatioDisc());// ?????????????????????*?????????*????????????
						double tempAmount = amountMed + amountDis;
						double amountPi = (tempAmount >= blOpDt.getAmount()) ? 0.0 : (blOpDt.getAmount() - tempAmount);
						blOpDt.setAmountPi(amountPi);
					}
				}
			}
			DataBaseHelper.batchUpdate("update bl_op_dt set quan=:quan,amount=:amount,amount_pi=:amountPi where pk_cgop=:pkCgop", blOpDtOlds);
			return blOpDtOlds;
		}
		/**
		 * ????????????????????????????????????????????????
		 * @param paramMap{codePv???pkPicate???pkPv,pkOrg}
		 */
	   public void updateOpCgNoSettleDtByPicate(Map<String,Object> paramMap){
		   //yangxue 2019.9.5??????
		   //1.?????????????????????????????????????????????
//		   BdHp cateHp = cgQryMaintainService.qryBdHpByPiCate(paramMap);
//		  if (cateHp == null)
//			  throw new BusException("??????????????????????????????????????????????????????????????????????????????????????????");
//		  //2.??????????????????????????????????????????????????????????????????
//		  List<ItemPriceVo> itemList = opcgQryWrapMapper.queryNoSettleInfo(paramMap);
//		  if(itemList==null||itemList.size()<=0)
//			  return;
//		  //3.?????????????????????????????????????????????
//		  List<ItemPriceVo> nitemList = priceStrategyService.batchChargePatientFavorable(itemList,cateHp,CommonUtils.getString(paramMap.get("pkOrg")),DateUtils.getDateTimeStr(new Date()));
//		  String[] sqls = new String[nitemList.size()];
//		  //4.???????????????????????????????????????????????????
//		  for(int i = 0;i<nitemList.size();i++){
//			  sqls[i] = "update bl_op_dt set pk_disc='"+nitemList.get(i).getPkDisc()+"',ratio_disc ="+nitemList.get(i).getRatioDisc()+" where pk_cgop='"+nitemList.get(i).getPkCgop()+"'";
//		  }
//		  DataBaseHelper.batchUpdate(sqls);
		   if(paramMap==null)
				throw new BusException("???????????????????????????????????????");
			if(CommonUtils.isNull(paramMap.get("pkPv")))
				throw new BusException("??????????????????????????????pkPv???");
			if(CommonUtils.isNull(paramMap.get("pkHp")))
				throw new BusException("??????????????????????????????pkHp???");
			if(CommonUtils.isNull(paramMap.get("pkPicate")))
				throw new BusException("??????????????????????????????pkPicate???");
			if(CommonUtils.isNull(paramMap.get("oldPkPicate")))
				throw new BusException("??????????????????????????????oldPkPicate???");
			String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
			String pkHp = CommonUtils.getString(paramMap.get("pkHp"));
			String pkPicate = CommonUtils.getString(paramMap.get("pkPicate"));
			String oldPkInsu = CommonUtils.getString(paramMap.get("oldPkInsu"));
			String oldPkPicate = CommonUtils.getString(paramMap.get("oldPkPicate"));
			if(CommonUtils.isNull(oldPkInsu))
				oldPkInsu = pkHp;
			CgProcessUtils.updateOpCg(pkPv,pkHp,pkPicate,oldPkInsu,oldPkPicate);
	   }

	/**
	 * @param ???????????????????????????????????????
	 * @return
	 */
	public int updateBlOpDtListByPk(List<BlOpDt> blOpDtList) {

		if (blOpDtList != null && blOpDtList.size() > 0) {
			
			String pkSettle=blOpDtList.get(0).getPkSettle();
			if(CommonUtils.isNotNull(pkSettle)){
				return cgStrategyPubMapper.updateBlOpDtListByPk(pkSettle,blOpDtList);
			}else{
				throw new BusException("????????????????????????????????????????????????");
			}
		} else {
			return 0;
		}

	}
	public List<CnOrder> getCnOrderLists(List<BlOpDt> blOpdtLists) {
	Set<String> keySet = new HashSet<>();
		for (BlOpDt blOpdtList : blOpdtLists) {
			if(!StringUtils.isEmpty(blOpdtList.getPkCnord())){
				keySet.add(blOpdtList.getPkCnord());
			}
		}
		List<CnOrder> cnOrderLists =DataBaseHelper.queryForList("select * from  cn_order where PK_CNORD in (" 
				+ CommonUtils.convertSetToSqlInPart(keySet, "PK_CNORD") + ") ",CnOrder.class);
		return cnOrderLists;
	}
}
