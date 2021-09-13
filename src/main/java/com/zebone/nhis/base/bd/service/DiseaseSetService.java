package com.zebone.nhis.base.bd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.DiseaseSetMapper;
import com.zebone.nhis.base.bd.vo.DiseaseDtlsVo;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyDisease;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyDiseaseOrd;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyHpDiv;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.scm.pub.vo.BdPdStoreEmpVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病种设置服务
 * @author c
 *
 */
@Service
public class DiseaseSetService {
	
	@Autowired
	private DiseaseSetMapper diseaseSetMapper;
	
	/**
	 * 交易号：001002002086
	 * 查询病种定义信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryDiseaseInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		return diseaseSetMapper.qryDiseaseInfo(paramMap);
	}
	
	/**
	 * 交易号：001002002087
	 * 根据病种信息主键查询关联的医保信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryHpByPkDiseae(String param,IUser user){
		String pkGzgydisease = JsonUtil.getFieldValue(param, "pkGzgydisease");
		
		return diseaseSetMapper.qryHpByPkDiseae(pkGzgydisease);
	}
	
	/**
	 * 交易码：001002002088
	 * 根据病种主键查询病种明信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryDiseaseDtls(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		//查询参数
		String pkGzgydisease = paramMap.get("pkGzgydisease").toString();
		//病种信息
		List<Map<String,Object>> diseaseMap = diseaseSetMapper.qryDiseaseInfo(paramMap);
		//关联医保信息
		List<Map<String,Object>> diseaseHp = diseaseSetMapper.qryHpByPkDiseae(pkGzgydisease);
		//关联药品信息
		List<Map<String,Object>> diseaseOrd = diseaseSetMapper.qryOrdByPkDiseae(pkGzgydisease);
		
		//组装明细信息
		Map<String,Object> dtlMap = new HashMap<String,Object>();
		dtlMap.put("diseaseInfo", diseaseMap.get(0));
		dtlMap.put("diseaseHpInfo", diseaseHp);
		dtlMap.put("diseaseOrdInfo", diseaseOrd);
		
		return dtlMap;
	}
	
	/**
	 * 交易号：001002002089
	 * 保存病种明细信息
	 * @param param
	 * @param user
	 */
	public void saveDiseaseInfo(String param,IUser user){
		DiseaseDtlsVo dtlsVo = JsonUtil.readValue(param, DiseaseDtlsVo.class);
		
		InsGzgyDisease diseaseInfo = dtlsVo.getDiseaseInfo();
		List<InsGzgyDiseaseOrd> ordList = dtlsVo.getDiseaseOrdInfo();
		List<InsGzgyHpDiv> HpList = dtlsVo.getDiseaseHpInfo();
		
		//病种主键
		String pkGzgydisease = "";
		
		if(diseaseInfo!=null){
			/**保存病种信息*/
			if(!CommonUtils.isEmptyString(diseaseInfo.getPkGzgydisease())){
				int count = DataBaseHelper.queryForScalar(
						"select count(1) from ins_gzgy_disease"
								+ " where del_flag='0' and PK_DIAG = ? and dt_diseasetype = ? and PK_GZGYDISEASE!=?",
						Integer.class, diseaseInfo.getPkDiag(),diseaseInfo.getDtDiseasetype(),diseaseInfo.getPkGzgydisease());
				if(count>0)
					throw new BusException("病种编码["+diseaseInfo.getCodeDiag()+"]已经存在！");
				
				DataBaseHelper.updateBeanByPk(diseaseInfo, false);
				pkGzgydisease = diseaseInfo.getPkGzgydisease();
			}else{
				int count = DataBaseHelper.queryForScalar(
						"select count(1) from ins_gzgy_disease"
								+ " where del_flag='0' and PK_DIAG = ? and dt_diseasetype = ?",
						Integer.class, diseaseInfo.getPkDiag(),diseaseInfo.getDtDiseasetype());
				if(count>0)
					throw new BusException("病种编码["+diseaseInfo.getCodeDiag()+"]已经存在！");
				
				DataBaseHelper.insertBean(diseaseInfo);
				pkGzgydisease = diseaseInfo.getPkGzgydisease();
			}
		}
		
		/**更新或新增关联药品信息*/
		if(ordList!=null && ordList.size()>0){
			// 校验药品信息（医保类别pk_ord）是否重复
			Map<String, String> pkOrdMap = new HashMap<String, String>();
			int len = ordList.size();
			for (int i = 0; i < len; i++) {
				String pkOrd = ordList.get(i).getPkOrd();
				if (pkOrdMap.containsKey(pkOrd)) {
					throw new BusException("该病种下关联药品信息重复！");
				}
				pkOrdMap.put(pkOrd, ordList.get(i).getPkDiseaseord());
			}
			
			// 先全删再恢复的方式（软删除）
			DataBaseHelper
					.update("update INS_GZGY_DISEASE_ORD set del_flag = '1' where PK_GZGYDISEASE = ?",
							new Object[] { pkGzgydisease });
			
			for (InsGzgyDiseaseOrd ord : ordList) {
				if (ord.getPkDiseaseord() != null) {
					ord.setDelFlag("0");// 恢复
					ord.setPkGzgydisease(pkGzgydisease);
					DataBaseHelper.updateBeanByPk(ord, false);
				} else {
					ord.setPkGzgydisease(pkGzgydisease);
					DataBaseHelper.insertBean(ord);
				}
			}
		}else{
			DataBaseHelper
			.update("update INS_GZGY_DISEASE_ORD set del_flag = '1' where PK_GZGYDISEASE = ?",
					new Object[] { pkGzgydisease });
		}
		
		
		/**更新或新增关联医保信息*/
		if(HpList!=null && HpList.size()>0){
			// 校验医保信息（医保类别pk_hp）是否重复
			Map<String, String> pkHpMap = new HashMap<String, String>();
			int len = HpList.size();
			for (int i = 0; i < len; i++) {
				String pkHp = HpList.get(i).getPkHp();
				if (pkHpMap.containsKey(pkHp)) {
					throw new BusException("该病种下关联医保信息重复！");
				}
				pkHpMap.put(pkHp, HpList.get(i).getPkHpdiv());
			}
			
			// 先全删再恢复的方式（软删除）
			DataBaseHelper
					.update("update INS_GZGY_HP_DIV set del_flag='1' where pk_div = ?",
							new Object[] { pkGzgydisease });
			
			for (InsGzgyHpDiv hp : HpList) {
				if (hp.getPkHpdiv() != null) {
					hp.setDelFlag("0");// 恢复
					hp.setPkDiv(pkGzgydisease);
					DataBaseHelper.updateBeanByPk(hp, false);
				} else {
					hp.setPkDiv(pkGzgydisease);
					hp.setEuDivtype("10");	//病种
					DataBaseHelper.insertBean(hp);
				}
			}
		}else{
			DataBaseHelper
			.update("update INS_GZGY_HP_DIV set del_flag='1' where pk_div = ?",
					new Object[] { pkGzgydisease });
		}
		//delete语句删除del_flag='1'的数据
		DataBaseHelper.execute("delete from INS_GZGY_HP_DIV where del_flag = '1' and pk_div = ?",
				new Object[] { pkGzgydisease });
	}
	
	/**
	 * 交易号：001002002090
	 * 删除病种信息
	 * @param param
	 * @param user
	 */
	public void deleteDisease(String param,IUser user){
		String pkGzgydisease = JsonUtil.getFieldValue(param, "pkGzgydisease");
		
		//删除关联医保信息
		DataBaseHelper
		.update("delete from ins_gzgy_hp_div where pk_div = ?",
				new Object[] { pkGzgydisease });
		
		//删除关联药品信息
		DataBaseHelper
		.update("delete from ins_gzgy_disease_ord where pk_gzgydisease = ?",
				new Object[] { pkGzgydisease });
		
		//删除病种信息(软删除)
		DataBaseHelper
		.update("update ins_gzgy_disease set del_flag = '1' where pk_gzgydisease = ? and del_flag='0'",
				new Object[] { pkGzgydisease });
		
	}
	
	/**
	 * 交易号：001002002091
	 * 查询待选公费医保信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryWaitChooseHp(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap.get("spcode")!=null){
			paramMap.put("spcode", paramMap.get("spcode").toString().toUpperCase());
		}
		return diseaseSetMapper.qryWaitChooseHp(paramMap);
	}
	
	/**
	 * 交易号：001002002092
	 * 查询已选公费医保信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qrySelectedHp(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap.get("spcode")!=null){
			paramMap.put("spcode", paramMap.get("spcode").toString().toUpperCase());
		}
		return diseaseSetMapper.qrySelectedHp(paramMap);
	}
	
	
}
