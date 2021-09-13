package com.zebone.nhis.scm.opds.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.util.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccPddt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.opds.dao.ScmOpPresQryMapper;
import com.zebone.nhis.scm.pub.dao.SchOpdsPreMapper;
import com.zebone.nhis.scm.pub.service.PdStInPubService;
import com.zebone.nhis.scm.pub.vo.ExPresOccPddtVO;
import com.zebone.nhis.scm.pub.vo.PdInParamVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ScmOpReturnDrugsService {
	
	@Autowired
	private SchOpdsPreMapper schOpdsPreMapper; 
	@Autowired
	private ScmOpPresQryMapper scmOpPresQryMapper;
	@Autowired
	private PdStInPubService pdStInPubService;
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryPresForRtn(String param, IUser user){
		List<Map<String,Object>> res =  null;
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		if(paraMap!=null){
			res = scmOpPresQryMapper.queryPresOccList(paraMap);
		}
		return res;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryPresDtForRtn(String param ,IUser user){
		List<Map<String,Object>> res =  null;
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		//2.查询
		if(paraMap!=null&&!CommonUtils.isEmptyString(paraMap.get("pkPresocc").toString())){
			res = scmOpPresQryMapper.qryPresDt(paraMap);
		}
		return res;	
	}
	/**
	 * 门诊退药，单条处方记录退药
	 * @param paraMap
	 */
	private void returnDrugs(Map<String,Object> paraMap) {
		String pkPresoccdt = (String) paraMap.get("pkPresoccdt");//处方执行单
		Double quanBack = (Double) paraMap.get("quanBack"); //退药数量
		Double ordBack = (Double) paraMap.get("ordsBack");//退药付数 === 修改之前：ordQuan

		ExPresOccDt presOccDt = schOpdsPreMapper.QryPresDtByPk(pkPresoccdt);
		ExPresOcc occ = DataBaseHelper.queryForBean(" select * from ex_pres_occ where pk_presocc = ?", ExPresOcc.class, presOccDt.getPkPresocc());
		if (!"02".equals(occ.getDtPrestype()) && (ordBack == null || ordBack == 0.0)) {
			ordBack = 1.0;
		}
		//2.退药
		List<ExPresOccPddtVO> pddts = schOpdsPreMapper.qryPresRecords(pkPresoccdt);
		if (pddts == null || pddts.size() <= 0) return;
		List<PdInParamVo> pdInParas = new ArrayList<PdInParamVo>(16);
		Map<String, ExPresOccPddt> pddtMap = new HashMap<String, ExPresOccPddt>();

		Double remainQuan = quanBack;
		for (ExPresOccPddtVO vo : pddts) {
			ExPresOccPddt occPddt = new ExPresOccPddt();
			if (!MathUtils.equ(remainQuan, 0.0)) {
				PdInParamVo pdIn = new PdInParamVo();
				pdIn.setAmount(vo.getAmount());
				pdIn.setBatchNo(vo.getBatchNo());
				pdIn.setDateExpire(vo.getDateExpire());
				// presOccDt.getPackSize() 为当前仓库的pack_size
				pdIn.setPackSize(presOccDt.getPackSize());
				pdIn.setPkCnOrd(vo.getPkCnord());
				pdIn.setPkPd(vo.getPkPd());
				pdIn.setPkPdapdt(vo.getPkOccpddt());
				pdIn.setPkPv(vo.getPkPv());
				pdIn.setPkUnitPack(presOccDt.getPkUnit());
				pdIn.setPrice(MathUtils.mul(MathUtils.div(vo.getPrice(),vo.getPackSize().doubleValue(),6),presOccDt.getPackSize().doubleValue()));
				pdIn.setPriceCost(MathUtils.mul(MathUtils.div(vo.getPriceCost(),vo.getPackSize().doubleValue(),6),presOccDt.getPackSize().doubleValue()));
				//如果某一批次的发药数量，大于或者等于退药数量，则使用该批
				if (MathUtils.compareTo(vo.getQuanMin(), MathUtils.mul(remainQuan, presOccDt.getPackSize() * 1.0)) >= 0) {
					pdIn.setQuanMin(MathUtils.mul(remainQuan, presOccDt.getPackSize() * 1.0));
					pdIn.setQuanPack(remainQuan);
					remainQuan = 0.0;
					if (MathUtils.compareTo(ordBack, 0.0) > 0) {//草药的这样计算可能有误
						pdIn.setQuanMin(MathUtils.mul(pdIn.getQuanMin(), 1D));
						pdIn.setQuanPack(MathUtils.mul(pdIn.getQuanPack(), 1D));
					}
					pdInParas.add(pdIn);
					ApplicationUtils.copyProperties(occPddt, vo);
					pddtMap.put(vo.getPkOccpddt(), occPddt);
					break;
				} else {//否则,执行明细全退，并更新可退数量
					pdIn.setQuanMin(vo.getQuanMin());
					pdIn.setQuanPack(MathUtils.div(vo.getQuanMin(), presOccDt.getPackSize().doubleValue()));
					remainQuan = MathUtils.sub(remainQuan, vo.getQuanPack());
					if (MathUtils.compareTo(ordBack, 0.0) > 0) {//草药的这样计算可能有误
						pdIn.setQuanMin(MathUtils.mul(pdIn.getQuanMin(), 1D));
						pdIn.setQuanPack(MathUtils.mul(pdIn.getQuanPack(), 1D));
					}
					pdInParas.add(pdIn);
					ApplicationUtils.copyProperties(occPddt, vo);
					pddtMap.put(vo.getPkOccpddt(), occPddt);
				}

			} else {
				break;
			}
		}
		Map<String, Object> inResMap = pdStInPubService.execStIn(occ.getPkOrgPres(), occ.getPkDeptPres(), pdInParas, UserContext.getUser().getPkStore());
//
//		2）更新处方明细ex_pres_occ_dt；
//		quan_back，退回数量=退回数量+本次退药数量；
//		ords_back，西药成药为1，草药为退药付数+本次退药付数；
//		amount_back，发药数量*付数*零售单价；
		presOccDt.setQuanMinBack(MathUtils.add(presOccDt.getQuanMinBack(),MathUtils.mul(quanBack,presOccDt.getPackSize().doubleValue())));
		presOccDt.setQuanBack(MathUtils.div(presOccDt.getQuanMinBack(),presOccDt.getPackSize().doubleValue(),4));

		/*
		if ("02".equals(occ.getDtPrestype())) {
			presOccDt.setOrdsBack(presOccDt.getOrdsBack() + ordBack);
		} else {
			presOccDt.setOrdsBack(1.0);
		}*/

		BigDecimal amount = new BigDecimal(MathUtils.mul(quanBack, presOccDt.getPrice().doubleValue()).toString());
		amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		presOccDt.setAmountBack(amount);

		StringBuffer occdtSql=new StringBuffer();
		occdtSql.append("update ex_pres_occ_dt set quan_min_back=?,quan_back=?,amount_back=?");
		occdtSql.append(" ,ords_back=? where pk_presoccdt=?");
		DataBaseHelper.update(occdtSql.toString(),new Object[]{presOccDt.getQuanMinBack(),presOccDt.getQuanBack(),presOccDt.getAmountBack(),presOccDt.getOrdsBack(),presOccDt.getPkPresoccdt()});

//		3）更新处方明细操作流水ex_pres_occ_pddt；
		List<ExPresOccPddt> exOccPddts = new ArrayList<ExPresOccPddt>();
		List<String> paramPdDt = new ArrayList<String>();
		for (Entry<String, Object> e : inResMap.entrySet()) {
			paramPdDt.add((String) e.getValue());
		}
		List<PdStDetail> pdStDts = schOpdsPreMapper.QryPdStDts(paramPdDt);
		Map<String, PdStDetail> pdStDtsMap = new HashMap<String, PdStDetail>();
		for (PdStDetail vo : pdStDts) {
			pdStDtsMap.put(vo.getPkPdstdt(), vo);
		}
		for (Entry<String, Object> e : inResMap.entrySet()) {
			String pkOccpddt = e.getKey();
			String pkPdstdt = (String) e.getValue();
			ExPresOccPddt voSou = pddtMap.get(pkOccpddt);
			PdStDetail pdStDt = pdStDtsMap.get(pkPdstdt);
			ExPresOccPddt vo = new ExPresOccPddt();
			vo.setPkOrg(UserContext.getUser().getPkOrg());
			vo.setPkPresoccdt(voSou.getPkPresoccdt());//请领明细
			vo.setDateDe(new Date());
			vo.setEuDirect("-1");
			vo.setPkPd(voSou.getPkPd());
			vo.setBatchNo(pdStDt.getBatchNo());
			vo.setDateExpire(pdStDt.getDateExpire());
			vo.setPkDeptDe(UserContext.getUser().getPkDept());
			vo.setPkStore(UserContext.getUser().getPkStore());
			vo.setPkUnit(pdStDt.getPkUnitPack());
			vo.setQuanPack(pdStDt.getQuanPack());
			vo.setPackSize(pdStDt.getPackSize());
			vo.setQuanMin(pdStDt.getQuanMin());
			vo.setPrice(pdStDt.getPrice());
			vo.setPriceCost(pdStDt.getPriceCost());
			vo.setAmount(pdStDt.getAmount());
			vo.setAmountCost(pdStDt.getAmountCost());
			vo.setPkPdstdt(pdStDt.getPkPdstdt());
			vo.setNote("");
			vo.setPkOccpddtBack(pkOccpddt);
			vo.setPresNo(voSou.getPresNo());
			ApplicationUtils.setDefaultValue(vo, true);
			exOccPddts.add(vo);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPresOccPddt.class), exOccPddts);
	}
	
	@SuppressWarnings("unchecked")
	public void returnDrugs(String param, IUser user){
		//1.参数获取
		List<Map<String,Object>> paraList = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>(){});
		if(paraList==null||paraList.size()<=0)
			return;
		for(Map<String,Object> paramMap:paraList){
			//不允许循环操作数据库，需要进一步优化
			returnDrugs(paramMap);
		}
		
	}
	
	public Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date temp = null;
		if(StringUtils.hasText(date)){
			try {
				temp = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return temp;
	}

}
