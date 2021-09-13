package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.vo.BdTermDiagTreatwayVo;
import com.zebone.nhis.cn.ipdw.vo.DiagVo;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnDiagMapper;
import com.zebone.nhis.cn.ipdw.vo.BdTermDiagDeptVo;
import com.zebone.nhis.common.module.base.bd.code.BdTermDiagTreatway;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiag;
import com.zebone.nhis.common.module.cn.ipdw.CnDiag;
import com.zebone.nhis.common.module.cn.ipdw.CnDiagDt;
import com.zebone.nhis.common.module.cn.ipdw.PvDiag;
import com.zebone.nhis.common.module.cn.ipdw.PvDiagDt;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnDiagService {

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
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap.get("pkPv")==null){
			throw new BusException("患者主键为空！");
		}
		String pkPv = paramMap.get("pkPv").toString();
		String edition = paramMap.get("edition")==null?"DEF":paramMap.get("edition").toString();
		List<Map<String,Object>> rtn = new ArrayList<Map<String,Object>>();
		if("BA".equals(edition)){ //特殊版本 --博爱专版
			rtn = cnDiagMapper.qryPvDiagBA(pkPv);
		}else { //默认版本
			rtn = cnDiagMapper.qryPvDiag(pkPv);
		}

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
	public List<BdTermDiagDeptVo> qryDoctorCommDiag(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		return cnDiagMapper.qryDoctorCommDiag(paramMap);
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
		if(map.get("pkPv")==null){
			throw new BusException("患者主键为空！");
		}
		String pkPv = map.get("pkPv").toString();
		String edition = map.get("edition")==null?"DEF":map.get("edition").toString();
		List<Map<String,Object>> rtn = new ArrayList<Map<String,Object>>();
		if("BA".equals(edition)){ //特殊版本 --博爱专版
			rtn = cnDiagMapper.qryPvDiagBA(pkPv);
		}else { //默认版本
			rtn = cnDiagMapper.qryPvDiag(pkPv);
		}
		ArrayList<String> pkList = new ArrayList<String>();
		if("BA".equals(edition)){
			for (Map<String, Object> PvdiagMap : rtn) {
				String pkPvdiag = (String)PvdiagMap.get("pkPvdiag");
				pkList.add(pkPvdiag);
			}
		}else{
			for (Map<String, Object> PvdiagMap : rtn) {
				String pkPvdiag = (String)PvdiagMap.get("pkPvdiag");
				pkList.add(pkPvdiag);
				if(PvdiagMap.get("flagMaj")!=null && "1".equals(PvdiagMap.get("flagMaj").toString())
						|| PvdiagMap.get("flagMajChn")!=null && "1".equals(PvdiagMap.get("flagMajChn").toString())){
					PvdiagMap.put("flagMajShow", "1");
				}else {
					PvdiagMap.put("flagMajShow", "0");
				}
			}
		}

		if (pkList!=null&&pkList.size()>0) {
			List<PvDiagDt> pvDiagDts = cnDiagMapper.qryPvDiagDtNew(pkList);
			if (pvDiagDts==null||pvDiagDts.size()<1) return rtn;
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
	 * 查询诊断的备注信息明细
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> qryCnDiagComtDt(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtn = cnDiagMapper.qryCnDiagComtDt(paramMap);
		return rtn;
	}
	
	/**
	 * 判断子诊断是否为父诊断的并发症；
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> checkSonDiagIsComp(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtn = cnDiagMapper.qryCnDiagComtDt(paramMap);
		return rtn;
	}
	
	/**
	 * 保存诊断
	 * @param param
	 * @param user
	 */
	public void savePvDiag(String param,IUser user) {
		Map readValue = JsonUtil.readValue(param, Map.class);
		
		//发送删除诊断信息(发送平台消息所需数据)
		Map<String ,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String sql = "select * from pv_diag where pk_pv=?";
		List<Map<String, Object>> paramList = DataBaseHelper.queryForList(sql, readValue.get("pkPv") );
		paramMap.put("paramList", paramList);//已删除的诊断信息
		paramMap.put("pkPv", readValue.get("pkPv"));
		
		JsonNode node = JsonUtil.getJsonNode(param, "source");
		List<PvDiag> pvDiags = null;
		List<CnDiagDt> cnDiagDts = null;
		if (node==null) return;
			pvDiags=JsonUtil.readValue(node, new TypeReference<List<PvDiag>>() {
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
			for (PvDiag pvDiag : pvDiags) {
				if (StringUtils.isNotBlank(pvDiag.getDescDiag())) {
					cnDes+=pvDiag.getDescDiag();
					cnDes+=",";
				}
				if(StringUtils.isNotBlank(pvDiag.getCodeIcd())){
					pvDiag.setCodeIcd(pvDiag.getCodeIcd().trim());
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
			
			for (PvDiag pvDiag : pvDiags) {
				pvDiag.setPkOrg(us.getPkOrg());
				
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
	 * 查询分值
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdTermDiagTreatwayVo> qryVal(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if(map==null)return null;
		if(map.get("codeDiag")==null)return null;
		String codeDiag=(String)map.get("codeDiag");
		map.put("codeDiag", codeDiag.toUpperCase());
		return cnDiagMapper.qryVal(map);
	}
	
	/**
	 * 查询诊断备注明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PvDiagDt> qryPvdiagDt(String param,IUser user) {
		if(!StringUtils.isNotBlank(param))return null;
		return cnDiagMapper.qryPvdiagDt(param);
	}
	
	/**
	 * 存为常用诊断 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdTermDiagDeptVo> saveTermDiagDept(String param,IUser user) {
		BdTermDiagDeptVo termDiagDept = JsonUtil.readValue(param, BdTermDiagDeptVo.class);
		if (termDiagDept==null)return null;
		User us = (User)user;
		//保存常用诊断
		termDiagDept.setPkOrg(us.getPkOrg());
		termDiagDept.setEuType("1");
		termDiagDept.setPkDept(us.getPkDept());
		termDiagDept.setPkEmp(us.getPkEmp());
		termDiagDept.setNameEmp(us.getNameEmp());
		BdCndiag bdCndiag = cnDiagMapper.qryBdCndiag(termDiagDept.getPkDiag());
		termDiagDept.setPkDiag(bdCndiag.getPkDiag());
		termDiagDept.setNameDiag(bdCndiag.getNameCd());
		termDiagDept.setSpcode(bdCndiag.getSpcode());
		termDiagDept.setdCode(bdCndiag.getdCode());
		//termDiagDept.setPkDiagdept(termDiagDept.getPkDiag());
		termDiagDept.setPkDiag(bdCndiag.getPkCndiag());
		//查出当前科室或者当前人员下是否有这个常用诊断
		Map<String,Object> map = new HashMap<>();
		map.put("pkEmp", us.getPkEmp());
		map.put("pkDept", us.getPkDept());
		map.put("pkDiag", bdCndiag.getPkCndiag());
		List<BdTermDiagDeptVo> qryDoctorCommDiag = cnDiagMapper.qryDoctorCommDiag(map);
		if (qryDoctorCommDiag != null &&qryDoctorCommDiag.size()>0) {
			termDiagDept.setPkDiagdept(qryDoctorCommDiag.get(0).getPkDiagdept());
			DataBaseHelper.updateBeanByPk(termDiagDept,false);
		}else {
			DataBaseHelper.insertBean(termDiagDept);
		}
		//查询所有常用诊断
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkEmp", us.getPkEmp());
		paramMap.put("pkDept", us.getPkDept());
		return cnDiagMapper.qryDoctorCommDiag(paramMap);
	}

	/**
	 * 存为常用诊断 -- 批量保存
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdTermDiagDeptVo> saveTermDiagDepts(String param,IUser user) {
		DiagVo vo=JsonUtil.readValue(param, DiagVo.class);
		List<BdTermDiagDeptVo> diagDeptVoList = vo.getDiagDeptVoList();
		if (diagDeptVoList==null || diagDeptVoList.size()==0)return null;
		User us = (User)user;
		for (int i=0;i<diagDeptVoList.size();i++){
			//保存常用诊断
			diagDeptVoList.get(i).setSortno((long) (i+1));
		}
		DataBaseHelper.batchUpdate("update BD_TERM_DIAG_DEPT set SORTNO=:sortno where PK_DIAGDEPT=:pkDiagdept ",diagDeptVoList);
		//查询所有常用诊断
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkEmp", us.getPkEmp());
		paramMap.put("pkDept", us.getPkDept());
		return cnDiagMapper.qryDoctorCommDiag(paramMap);
	}
	
	/**
	 *删除常用诊断 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdTermDiagDeptVo> delTermDiagDept(String param,IUser user) {
		BdTermDiagDeptVo termDiagDept = JsonUtil.readValue(param, BdTermDiagDeptVo.class);
		DataBaseHelper.deleteBeanByPk(termDiagDept);
		User us = (User)user;
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkEmp", us.getPkEmp());
		paramMap.put("pkDept", us.getPkDept());
		return cnDiagMapper.qryDoctorCommDiag(paramMap);
	}
	
	/**
	 * 查询所有的诊断
	 * @return
	 */
	public List<BdTermDiagDeptVo> qryTermDiagDepts(String param,IUser user) {
		User us = (User)user;
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkEmp", us.getPkEmp());
		paramMap.put("pkDept", us.getPkDept());
		 List<BdTermDiagDeptVo> qryDoctorCommDiag = cnDiagMapper.qryDoctorCommDiag(paramMap);
		 return qryDoctorCommDiag;
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
}
