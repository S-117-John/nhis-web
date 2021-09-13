package com.zebone.nhis.cn.ipdw.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.drg.vo.BdTermDcdtVo;
import com.zebone.nhis.base.ou.vo.BdOuOrgArea;
import com.zebone.nhis.cn.ipdw.dao.CnDiagMapper;
import com.zebone.nhis.cn.ipdw.vo.BdTermDiagDeptVo;
import com.zebone.nhis.cn.ipdw.vo.PvDrgDiag;
import com.zebone.nhis.common.module.base.bd.code.BdSysparam;
import com.zebone.nhis.common.module.base.bd.code.BdTermDiagTreatway;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiag;
import com.zebone.nhis.common.module.cn.ipdw.CnDiag;
import com.zebone.nhis.common.module.cn.ipdw.CnDiagDt;
import com.zebone.nhis.common.module.cn.ipdw.PvDiag;
import com.zebone.nhis.common.module.cn.ipdw.PvDiagDt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * drg诊断
 * @author dell
 *
 */
@Service
public class CnDrgDiagService {

	@Autowired
	private CnDiagMapper  cnDiagMapper;
	
	@Autowired
	private PatiListQryService patiListQryService;
	
	/**
	 * 查询已开立的诊断
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> queryPvDiagOld(String param,IUser user){
		String pkPv = (String) JsonUtil.readValue(param, Map.class).get("pkPv");
		List<Map<String,Object>> rtn = cnDiagMapper.qryPvDiag(pkPv);
		for (Map<String, Object> map : rtn) {
			String pkPvdiag = (String)map.get("pkPvdiag");
			List<PvDiagDt> pvdiagDt = cnDiagMapper.qryPvdiagDt(pkPvdiag);
			map.put("pvdiagdts", pvdiagDt);
		}
		return rtn;
	}
	
	/**
	 * 查询医生常用诊断
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryDoctorCommDiag(String param,IUser user){
		User us = (User)user;
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		MyBatisPage.startPage(1, 20);
		paramMap.put("pkEmp",us.getPkEmp());
		paramMap.put("pkDept", us.getPkDept());
		List<Map<String,Object>> list=cnDiagMapper.qryDoctorCommDiagDrg(paramMap);
		return list;
	}
	
	/**
	 * 查询诊断的备注信息
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> qryCnDiagComt(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtn = cnDiagMapper.qryCnDiagComt(paramMap);
		return rtn;
	}
	
	/**
	 * 查询所有的pvdiag
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPvDiag(String param,IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		Object object = map.get("pkPv");
		if (object==null)return null;
		String pkpv=(String)object;
		List<Map<String,Object>> rtn = cnDiagMapper.qryPvDiagDrg(pkpv);
		ArrayList<String> pkList = new ArrayList<String>();
		for (Map<String, Object> PvdiagMap : rtn) {
			String pkPvdiag = (String)PvdiagMap.get("pkPvdiag");
			pkList.add(pkPvdiag);
		}
		if (null!=pkList&&pkList.size()>0) {
			List<PvDiagDt> pvDiagDts = cnDiagMapper.qryPvDiagDtNew(pkList);
			if (null==pvDiagDts ||pvDiagDts.size()==0) return rtn;
 			for (Map<String, Object> pvdiagMap : rtn) {
				ArrayList<PvDiagDt> pvDiagDtList = new ArrayList<PvDiagDt>();
				String pkPvdiag = (String)pvdiagMap.get("pkPvdiag");
			for (PvDiagDt pvDiagDt : pvDiagDts) {
				
					if (StringUtils.isNotBlank(pkPvdiag)&& pkPvdiag.equals(pvDiagDt.getPkPvdiag())) {
						pvDiagDtList.add(pvDiagDt);
					}
				}
				pvdiagMap.put("pvdiagdts", pvDiagDtList);
			}
		}
		
		return rtn;
	}

	
	/**
	 * 保存诊断
	 * @param param
	 * @param user
	 */
	public void savePvDiag(String param,IUser user) {
		Map readValue = JsonUtil.readValue(param, Map.class);
		JsonNode node = JsonUtil.getJsonNode(param, "source");
		
		//发送删除诊断信息
		Map<String ,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String sql = "select * from pv_diag where pk_pv=?";
		List<Map<String, Object>> paramList = DataBaseHelper.queryForList(sql, readValue.get("pkPv") );
		paramMap.put("paramList", paramList);//已删除的诊断信息
		paramMap.put("pkPv", readValue.get("pkPv"));
		
		List<PvDrgDiag> pvDiags = null;
		List<CnDiagDt> cnDiagDts = null;
		if (node==null) return;
			pvDiags=JsonUtil.readValue(node, new TypeReference<List<PvDrgDiag>>() {
			});
			cnDiagDts=JsonUtil.readValue(node, new TypeReference<List<CnDiagDt>>() {
			});
		
		User us=(User)user;
		//保存cn_diag
		CnDiag cnDiag = new CnDiag();
		cnDiag.setPkOrg(us.getPkOrg());
		cnDiag.setPkEmpDiag(us.getPkEmp());
		cnDiag.setNameEmpDiag(us.getNameEmp());
		cnDiag.setDateDiag(new Date());
		cnDiag.setPkPv((String) readValue.get("pkPv"));
		cnDiag.setEuPvtype(cnDiagMapper.qryEuPvtype((String) readValue.get("pkPv")));
		String cnDes="";
		//取出诊断详细
		if (pvDiags!=null&&pvDiags.size()>0) {
			for (PvDrgDiag pvDiag : pvDiags) {
				if(StringUtils.isBlank(pvDiag.getCodeDcdt()) || StringUtils.isBlank(pvDiag.getPkDcdt())
						|| StringUtils.isBlank((pvDiag.getCodeCcdt())) || StringUtils.isBlank(pvDiag.getPkCcdt())){
					throw new BusException("地区诊断编码及名称不能为空！");
				}
				if (StringUtils.isNotBlank(pvDiag.getNameDiag())) {
					cnDes+=pvDiag.getNameDiag();
					cnDes+=",";
				}
			}
		}
		if (StringUtils.isNotBlank(cnDes)) {
			cnDes=cnDes.substring(0, cnDes.length()-1);
		}
		cnDiag.setDescDiags(cnDes);
		DataBaseHelper.insertBean(cnDiag);
		//删除多余的数据
		List<String> pkDiagList = cnDiagMapper.qryPkDiags((String) readValue.get("pkPv"));
		if (pkDiagList!=null&&pkDiagList.size()>0) {
			cnDiagMapper.delpvDiagDtByPkpvdiag(pkDiagList);
		}
		HashMap praMap = new HashMap<>();
		praMap.put("pkPv", (String) readValue.get("pkPv"));
		cnDiagMapper.delPvdiagByList(praMap);
		//保存诊断详细
		if (cnDiagDts!=null&&cnDiagDts.size()>0) {
			for (CnDiagDt cnDiagDt : cnDiagDts) {
				cnDiagDt.setPkOrg(us.getPkOrg());
				
				cnDiagDt.setPkDiag(cnDiag.getPkCndiag());
			}
		}
		
		if (pvDiags!=null&&pvDiags.size()>0) {
			ArrayList<String> pkDiags = new ArrayList<String>();
			
			for (PvDrgDiag pvDiag : pvDiags) {
				pvDiag.setPkOrg(us.getPkOrg());
				
				//ApplicationUtils.setDefaultValue(pvDiag, true);
				DataBaseHelper.insertBean(pvDiag);
				pkDiags.add(pvDiag.getPkDiag());
				
				//保存诊断备注明细
				List<PvDiagDt> pvDiagDts = pvDiag.getPvdiagdts();
				if (pvDiagDts!=null&&pvDiagDts.size()>0) {
					List<String> pks = new ArrayList<String>();
					HashMap<String, Object> map = new HashMap<>();
					for (PvDiagDt pvDiagDt : pvDiagDts) {
						pvDiagDt.setPkPvdiag(pvDiag.getPkPvdiag());
						pvDiagDt.setPkOrg(us.getPkOrg());
						
						DataBaseHelper.insertBean(pvDiagDt); 
						pks.add(pvDiagDt.getPkPvdiagdt());
					}
					map.put("pkPvdiag", pvDiag.getPkPvdiag());
					map.put("pks", pks);
					cnDiagMapper.deldiagdtByList(map);
			}
		}
		}
		
        //发送诊断信息
		PlatFormSendUtils.sendCnDiagMsg(paramMap);
	}
	
	/**
	 * 查询历史诊断
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnDiag> qryDiags(String param,IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if (map==null)return null;
		if (map.get("pkPv")==null)return null;
		return cnDiagMapper.qryCnDiag((String)map.get("pkPv"));
	}
	
	/**
	 * 是否有传染病
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> countTermDiag(String param,IUser user) {
		List<String> bdTermDiags = JsonUtil.readValue(param, new TypeReference<List<String>>() {});
		if (bdTermDiags==null||bdTermDiags.size()<1)return null;
		return cnDiagMapper.countTermDiag(bdTermDiags);
	}
	
	/**
	 * 查询患者信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> qryPiInfo(String param,IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		//Map<String, Object> info = cnDiagMapper.qryPiInfo((String)map.get("pkPv"));
		Map<String, Object> info =patiListQryService.querySinglePat(param, user);
		if (info == null) {
			info = new HashMap<>();
		}
		String flagNewborn="0";
		if(null!=info.get("datepvbegin")){
			Date date=(Date) info.get("datepvbegin");
			Object birthDateobj =DataBaseHelper.queryForScalar("select BIRTH_DATE from PI_MASTER where PK_PI=?", Object.class, info.get("pkPi"));
			Date birthDate=(Date) birthDateobj;
			if(null!=birthDate){
				int count=DateUtils.getDateSpace(birthDate,date);
				if(count<=28){
					flagNewborn="1";
				}
			}
		}
		info.put("flagNewborn", flagNewborn);
		if(null!=info.get("agepv")){
			String ageStr=(String) info.get("agepv");
			info.put("age", ageStr.split("岁")[0]);
		}
		User us = (User)user;
		Map<String, Object> medicaltype = cnDiagMapper.qryDeptMedicaltype(us.getPkDept());
		if(medicaltype != null){
			info.put("dtMedicaltype",medicaltype.get("dtMedicaltype") );
			info.put("codeDept",medicaltype.get("codeDept") );
		}
		 if(medicaltype == null)
			 medicaltype = new HashMap<>();
		info.put("dept",medicaltype);
		
		return info;
	}
	
	public Map<String, Object> qryBdCndiag(String param,IUser user){
		if(!StringUtils.isNotBlank(param))
			return null; 
		Map map = JsonUtil.readValue(param, Map.class);
		if(map == null)
			return null;
		String labresult = getPropValueStr(map, "labresult");
		String abortype = getPropValueStr(map, "abortype");
		List<Map<String,Object>> codes = cnDiagMapper.qryBdCndiagByNameCd(map);
		Map<String, Object> retMap = new HashMap<>();
		if(codes != null && codes.size()>0){
			retMap = codes.get(0);
		}else {
			map.remove("abortype");
			codes = cnDiagMapper.qryBdCndiagByNameCd(map);
			if(StringUtils.isNotBlank(labresult)&&(codes == null || codes.size()<1)){
				codes = cnDiagMapper.qrySpecDiag(labresult);
			}
		}
		
		if(codes != null && codes.size()>0){
			retMap = codes.get(0);
		}
		String nameCd =  getPropValueStr(retMap, "codeIcd");
		String codeAdd = getPropValueStr(retMap,"codeAdd");
		String codeAdd2 = getPropValueStr(retMap,"codeAdd1");
		if(StringUtils.isNotBlank(codeAdd)){
			nameCd = nameCd + " " + codeAdd;
		}
		if(StringUtils.isNotBlank(codeAdd2)){
			nameCd = nameCd + " " + codeAdd2;
		}
		retMap.put("nameCd", nameCd);
		retMap.put("pkDiag", getPropValueStr(retMap, "pkLabrule"));
		return retMap;
	}
	
	private String getPropValueStr(Map<String, Object> map,String key) {
		if(map==null)return "";
		String value="" ;
		if(map.containsKey(key)){
			Object obj=map.get(key);
			value=obj==null?"":obj.toString();
		}
		return value;
	}
	
	/**
	 * 查询计算DRG入组测算结果
	 * 交易号：004002001015
	 * @param param 患者当次就诊主键,json格式{"pkPv":"pvValue"}
	 * @param user
	 * @return DRG入组测算结果
	 */
	public String qryInDrgTestValue(String param,IUser user) {
		String retInfo = "未能测算";		

		if(param == null || "".equals(param))
		{
			retInfo = "入参非法:" + param;
		}
		else
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
			if(paramMap == null)
			{
				retInfo = "入参非法:" + param;
			}
			else
			{
				String pkPv = (String)paramMap.get("pkPv");
				if(pkPv == null)
				{
					retInfo = "入参未包含就诊主键:" + param;
				}
				else
				{
					//查询患者主要诊断
					String sql = "SELECT Substr(diag.code_icd, 1, 5) as subicdcode FROM pv_diag diag WHERE diag.pk_pv=? AND diag.flag_maj='1'";
					List<Map<String, Object>> majorDiagList = DataBaseHelper.queryForList(sql, new Object[]{pkPv});
					if(majorDiagList == null || majorDiagList.size() == 0)
					{
						retInfo = "患者本次就诊没有主诊断";
					}
					else 
					{
						//取第一条主诊断
						String majorDiagIcdCode = "";
						Map<String,Object> map = majorDiagList.get(0);
						majorDiagIcdCode = map.get("subicdcode").toString();
						//查询患者手术操作
						StringBuilder sb = new StringBuilder();
						sb.append("SELECT op.pk_op as pkop,diag.diagcode as diagcode ");
						sb.append("FROM cn_op_apply op INNER  JOIN cn_order ord ");
						sb.append("ON   op.pk_cnord = ord.pk_cnord INNER  JOIN bd_term_diag diag ");
						sb.append("ON   op.pk_op = diag.pk_diag ");
						sb.append("WHERE  ord.pk_pv = ? ");
						sql = sb.toString();
						List<Map<String, Object>> opList = DataBaseHelper.queryForList(sql, new Object[]{pkPv});
						//附加手术操作
						sb = new StringBuilder();
						sb.append("SELECT subj.pk_diag_sub as pkop,subdiag.diagcode as diagcode ");
						sb.append("FROM   cn_op_apply op ");
						sb.append("INNER  JOIN cn_order ord ");
						sb.append("ON     op.pk_cnord = ord.pk_cnord ");
						sb.append("INNER  JOIN cn_op_subjoin subj ");
						sb.append("ON     op.pk_ordop = subj.pk_ordop ");
						sb.append("INNER  JOIN bd_term_diag subdiag ");
						sb.append("ON     subj.pk_diag_sub = subdiag.pk_diag ");
						sb.append("WHERE  ord.pk_pv = ? ");
						sql = sb.toString();
						List<Map<String, Object>> attachOpList = DataBaseHelper.queryForList(sql, new Object[]{pkPv});
						//手术和附加手术混合
						opList.addAll(attachOpList);
						//查询患者费用
						BigDecimal totalFee = DataBaseHelper.queryForScalar("SELECT SUM(amount) FROM bl_ip_dt cg WHERE cg.pk_pv = ?", BigDecimal.class, pkPv);
						//查询病种库
						sb = new StringBuilder();
						String sStartLetters = findLetter(majorDiagIcdCode);
						System.out.println("startLetter:"+sStartLetters+" majorDiagIcdCode:"+majorDiagIcdCode);
						sb.append("SELECT term.code_diag,term.name_diag,term.val,term.code_op,term.amount,term.eu_type ");
						sb.append("FROM   bd_term_diag_treatway term ");
						sb.append("WHERE  term.code_diag = ? ");
						sb.append("UNION ALL ");
						sb.append("SELECT term.code_diag,term.name_diag,term.val,term.code_op,term.amount,term.eu_type ");
						sb.append("FROM   bd_term_diag_treatway term ");
						sb.append("WHERE  term.code_diag = ? ");
						sql = sb.toString();
						List<BdTermDiagTreatway> btdtList = DataBaseHelper.queryForList(sql, BdTermDiagTreatway.class, new Object[]{majorDiagIcdCode, sStartLetters});
						//病种库分类 
						//常见病种库 0
						List<BdTermDiagTreatway> commonDiseasesList = new ArrayList<BdTermDiagTreatway>();
						//特别病种库 1
						List<BdTermDiagTreatway> specialDiseasesList = new ArrayList<BdTermDiagTreatway>();
						//基层病种库 2
						List<BdTermDiagTreatway> grassRootsDiseasesList = new ArrayList<BdTermDiagTreatway>();
						//中医病种库 3
						List<BdTermDiagTreatway> tcmDiseasesList = new ArrayList<BdTermDiagTreatway>();
						for(BdTermDiagTreatway bdTermDiagTreatway : btdtList) 
						{
							if("0".equals(bdTermDiagTreatway.getEuType()))
							{
								commonDiseasesList.add(bdTermDiagTreatway);
							}
							else if("1".equals(bdTermDiagTreatway.getEuType()))
							{
								specialDiseasesList.add(bdTermDiagTreatway);
							}
							else if("2".equals(bdTermDiagTreatway.getEuType()))
							{
								grassRootsDiseasesList.add(bdTermDiagTreatway);
							}
							else if("3".equals(bdTermDiagTreatway.getEuType()))
							{
								tcmDiseasesList.add(bdTermDiagTreatway);
							}
						}
						//患者本次就诊操作编码集合
						HashMap<String, String> opCodesOfPvMap = new HashMap<String, String>();
						for(Map<String, Object> mapItem : opList) 
						{
							if(mapItem.get("diagcode") != null)
							{
								opCodesOfPvMap.put(mapItem.get("diagcode").toString(), "");
							}
						}
						boolean bPatch = false;
						//优先匹配中医病种库
						if(tcmDiseasesList.size() > 0)
						{
							for(BdTermDiagTreatway bdTermDiagTreatway : tcmDiseasesList) 
							{
								HashMap<String, String> opCodesOfDiseaseMap = getOpCodesMap(bdTermDiagTreatway.getCodeOp());
								if(isKeyFullMatch(opCodesOfPvMap, opCodesOfDiseaseMap))
								{
									bPatch = true;
									retInfo = bdTermDiagTreatway.getNameDiag() + "," + bdTermDiagTreatway.getVal() + "," + bdTermDiagTreatway.getAmount();
									break;
								}
							}
						}
						//其次匹配基层病种库
						if(!bPatch && grassRootsDiseasesList.size() > 0)
						{
							for(BdTermDiagTreatway bdTermDiagTreatway : grassRootsDiseasesList) 
							{
								HashMap<String, String> opCodesOfDiseaseMap = getOpCodesMap(bdTermDiagTreatway.getCodeOp());
								if(isKeyFullMatch(opCodesOfPvMap, opCodesOfDiseaseMap))
								{
									bPatch = true;
									retInfo = bdTermDiagTreatway.getNameDiag() + "," + bdTermDiagTreatway.getVal() + "," + bdTermDiagTreatway.getAmount();
									break;
								}
							}
						}
						//再次匹配常见病种库
						if(!bPatch && commonDiseasesList.size() > 0)
						{
							List<BdTermDiagTreatway> commonDiseasesListForMaxMatch = new ArrayList<BdTermDiagTreatway>();
							int nMaxMatchCount = 0;
							for(BdTermDiagTreatway bdTermDiagTreatway : commonDiseasesList) 
							{
								HashMap<String, String> opCodesOfDiseaseMap = getOpCodesMap(bdTermDiagTreatway.getCodeOp());								
								if(isKeyInclude(opCodesOfPvMap, opCodesOfDiseaseMap))
								{
									//如果包含，则计算匹配数 
									int nMatchCount = getMatchCount(opCodesOfPvMap, opCodesOfDiseaseMap);
									if(nMatchCount > nMaxMatchCount)
									{
										commonDiseasesListForMaxMatch.clear();
										commonDiseasesListForMaxMatch.add(bdTermDiagTreatway);
										nMaxMatchCount = nMatchCount;
									}
									else if(nMatchCount == nMaxMatchCount && nMatchCount != 0)
									{
										commonDiseasesListForMaxMatch.add(bdTermDiagTreatway);
									}
								}
							}
							if(commonDiseasesListForMaxMatch.size() == 1)
							{
								//按操作编码数量降序比对病种组，入组操作编码重合数量最多且 Cn ⊆ Cm 的病种组
								bPatch = true;
								BdTermDiagTreatway bdTermDiagTreatway = commonDiseasesListForMaxMatch.get(0);
								retInfo = bdTermDiagTreatway.getNameDiag() + "," + bdTermDiagTreatway.getVal() + "," + bdTermDiagTreatway.getAmount();
							}
							else if(commonDiseasesListForMaxMatch.size() > 1)
							{
								//如同时出现多个操作编码重合数量相同且 Cn ⊆ Cm 的病种组，入组次均费用最接近的病例费用的病种组
								List<BdTermDiagTreatway> commonDiseasesListForFeeMinMatch = new ArrayList<BdTermDiagTreatway>();
								BigDecimal minFeeDifference = new BigDecimal(99999999.99);
								for(BdTermDiagTreatway bdTermDiagTreatway : commonDiseasesListForMaxMatch) 
								{
									BigDecimal curFeeDifference = totalFee.subtract(new BigDecimal(bdTermDiagTreatway.getAmount())).abs();
									if(curFeeDifference.compareTo(minFeeDifference) == -1)
									{
										//差距小
										commonDiseasesListForFeeMinMatch.clear();
										commonDiseasesListForFeeMinMatch.add(bdTermDiagTreatway);
										minFeeDifference = curFeeDifference;
									}
									else if(curFeeDifference.compareTo(minFeeDifference) == 0)
									{
										//差距相等
										commonDiseasesListForFeeMinMatch.add(bdTermDiagTreatway);
									}
								}
								if(commonDiseasesListForFeeMinMatch.size() == 1)
								{
									//如同时出现多个操作编码重合数量相同且 Cn ⊆ Cm 的病种组，入组次均费用最接近的病例费用的病种组
									bPatch = true;
									BdTermDiagTreatway bdTermDiagTreatway = commonDiseasesListForFeeMinMatch.get(0);
									retInfo = bdTermDiagTreatway.getNameDiag() + "," + bdTermDiagTreatway.getVal() + "," + bdTermDiagTreatway.getAmount();
								}
								else if(commonDiseasesListForFeeMinMatch.size() > 1)
								{
									//如同时出现多个操作编码重合数量相同、Cn ⊆ Cm 且费用接近程度一致的病种组，入组分值高的病种组
									List<BdTermDiagTreatway> commonDiseasesListForValMaxMatch = new ArrayList<BdTermDiagTreatway>();
									BigDecimal maxVal = new BigDecimal(0.0);
									for(BdTermDiagTreatway bdTermDiagTreatway : commonDiseasesListForFeeMinMatch) 
									{
										BigDecimal curVal = new BigDecimal(bdTermDiagTreatway.getVal());
										if(curVal.compareTo(maxVal) == 1)
										{
											//大分值
											commonDiseasesListForValMaxMatch.clear();
											commonDiseasesListForValMaxMatch.add(bdTermDiagTreatway);
											maxVal = curVal;
										}
										else if(curVal.compareTo(maxVal) == 0)
										{
											//分值相等
											commonDiseasesListForValMaxMatch.add(bdTermDiagTreatway);
										}
									}
									if(commonDiseasesListForValMaxMatch.size() == 1)
									{
										//如同时出现多个操作编码重合数量相同、Cn ⊆ Cm 且费用接近程度一致的病种组，入组分值高的病种组
										bPatch = true;
										BdTermDiagTreatway bdTermDiagTreatway = commonDiseasesListForValMaxMatch.get(0);
										retInfo = bdTermDiagTreatway.getNameDiag() + "," + bdTermDiagTreatway.getVal() + "," + bdTermDiagTreatway.getAmount();
									}
									else if(commonDiseasesListForValMaxMatch.size() > 1)
									{
										//如出现分值相同的，随机入其中一个病种组,这里选第一个
										bPatch = true;
										BdTermDiagTreatway bdTermDiagTreatway = commonDiseasesListForValMaxMatch.get(0);
										retInfo = bdTermDiagTreatway.getNameDiag() + "," + bdTermDiagTreatway.getVal() + "," + bdTermDiagTreatway.getAmount();									
									}
								}
							}
						}
						//最后入组特别病种库
						if(!bPatch && specialDiseasesList.size() > 0)
						{
							for(BdTermDiagTreatway bdTermDiagTreatway : specialDiseasesList) 
							{
								//无匹配条件，选第一条
								bPatch = true;
								retInfo = bdTermDiagTreatway.getNameDiag() + "," + bdTermDiagTreatway.getVal() + "," + bdTermDiagTreatway.getAmount();
								break;
							}
						}
						if(!bPatch)
						{
							retInfo = "根据患者此次就诊的主诊断未能匹配入组.";
						}
					}
				}
			}
		}
		return retInfo;
	}
	
	/**
	 * 计算map2里的key在map1里存在的数目
	 * 
	 * @param map1 
	 * @param map2 
	 * @return map2里的key在map1里存在的数目
	 */
	private int getMatchCount(HashMap<String, String> map1, HashMap<String, String> map2){
		int nCount = 0;
		
		String key = "";
		Iterator<String> it2 = map2.keySet().iterator();
        while(it2.hasNext()){
           key = (String)it2.next();
           if(!"".equals(key) && map1.containsKey(key))
			{
				nCount++;
			}
        }
        return nCount;
	}
	
	/**
	 * 判断map2里的key集合是否包含于map1里的key集合
	 * 
	 * @param map1 
	 * @param map2 
	 * @return map1里的key集合包含map2里的key集合时返回true,否则返回false
	 */
	private boolean isKeyInclude(HashMap<String, String> map1, HashMap<String, String> map2){
		boolean bRet = true;
		if(map1 == null && map2 == null)
		{
			//bRet = true;
		}
		else if(map1 == null && map2 != null)
		{
			bRet = false;
		}
		else if(map1 != null && map2 == null)
		{
			//bRet = true;
		}
		else
		{
			String key = "";
			Iterator<String> it2 = map2.keySet().iterator();
	        while(it2.hasNext()){
	           key = (String)it2.next();
	           if(!"".equals(key) && !map1.containsKey(key))
				{
					bRet = false;
					break;
				}
	        }
		}
		
		return bRet;
	}
	
	/**
	 * 判断map2里的key集合是否等于map1里的key集合
	 * 
	 * @param map1 
	 * @param map2 
	 * @return 一致返回true,否则返回false
	 */
	private boolean isKeyFullMatch(HashMap<String, String> map1, HashMap<String, String> map2){
		boolean bRet = true;
		if(map1 == null && map2 == null)
		{
			//bRet = true;
		}
		else if(map1 == null && map2 != null)
		{
			bRet = false;
		}
		else if(map1 != null && map2 == null)
		{
			bRet = false;
		}
		else
		{
			String key = "";
			Iterator<String> it1 = map1.keySet().iterator();
	        while(it1.hasNext()){
	           key = (String)it1.next();
	           if(!"".equals(key) && !map2.containsKey(key))
				{
					bRet = false;
					break;
				}
	           //String value = map.get(it.next());
	        }
			if(bRet)
			{
				Iterator<String> it2 = map2.keySet().iterator();
		        while(it2.hasNext()){
		           key = (String)it2.next();
		           if(!"".equals(key) && !map1.containsKey(key))
					{
						bRet = false;
						break;
					}
		        }
			}
		}
		
		return bRet;
	}
	
	/**
	 * 把用|分割的操作code拆分成单个code作为key存到HashMap里
	 * 
	 * @param opCodes 用|分割的操作code字符串
	 * @return HashMap
	 */
	private HashMap<String, String> getOpCodesMap(String opCodes){
		HashMap<String, String> opCodesOfDiseaseMap = new HashMap<String, String>();
		if(opCodes != null && !"".equals(opCodes))
		{
			String[] opCodeArray = opCodes.split("\\|");
			for(String opCode: opCodeArray){
				opCodesOfDiseaseMap.put(opCode, "");
	        }
		}
		
		return opCodesOfDiseaseMap;
	}
	
	/**
	 * 判断单个字符是否为英文大小写字母
	 * 
	 * @param c 单个字符
	 * @return 为英文字母返回true,否则返回false
	 */
	private boolean isAsciiLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
	
	/**
	 * 查找字符串中最前面连续的字母子串
	 * 
	 * @param str 原始字符串
	 * @return 原始字符串中最前面连续的字母子串
	 */
	private String findLetter(String str) {
        if(str == null || str.length() == 0) {
            return str;
        }
        char[] chs = str.toCharArray();
        int k = 0;
        for(int i = 0; i < chs.length; i++) {
            if(!isAsciiLetter(chs[i])) {
                break;
            }
            k++;
        }
        return new String(chs, 0, k);
    }
}
