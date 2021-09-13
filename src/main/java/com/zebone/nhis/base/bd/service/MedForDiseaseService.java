package com.zebone.nhis.base.bd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.MedForDiseaseMapper;
import com.zebone.nhis.base.bd.vo.DisRefPdVo;
import com.zebone.nhis.base.bd.vo.DiseaseRefPdvVo;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyDiseaseOrd;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 医保病种用药维护
 * @author jd_em
 *
 */
@Service
public class MedForDiseaseService {

	@Resource
	private MedForDiseaseMapper medForDiseaseMapper;
	
	/**
	 * 001002002094
	 * 查询药品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryBdPdInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		return medForDiseaseMapper.qryPdInfo(paramMap);
	}
	
	/**
	 * 001002002095
	 * 查询药品关联的病种信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryBdPdRefDisease(String param,IUser user){
		String pkPd=JsonUtil.getFieldValue(param, "pkPd");
		if(CommonUtils.isEmptyString(pkPd))throw new BusException("未获得药品信息！");
		return medForDiseaseMapper.qryBdPdRefDisease(pkPd);
	}
	
	/**
	 * 按药品：保存病种关联药品信息
	 * 001002002096
	 * @param param
	 * @param user
	 */
	public void saveDiseaseRefPd(String param,IUser user){
    	DiseaseRefPdvVo disRefPd=JsonUtil.readValue(param, DiseaseRefPdvVo.class);
    	if(disRefPd==null)return;
    	List<InsGzgyDiseaseOrd> disOrdList=disRefPd.getDisOrdList();
    	User toUser=(User)user;
    	
    	String delSql="delete from ins_gzgy_disease_ord ord where ord.eu_ordtype='1' and ord.pk_ord=?";
    	DataBaseHelper.execute(delSql, new Object[]{disRefPd.getPkPd()});
    	
    	for (int i = 0; i < disOrdList.size(); i++) {
    		InsGzgyDiseaseOrd disord=disOrdList.get(i);
			disord.setPkDiseaseord(NHISUUID.getKeyId());
			disord.setCodeOrd(disRefPd.getPdCode());
			disord.setNameOrd(disRefPd.getPdName());
			disord.setPkOrd(disRefPd.getPkPd());
			disord.setEuOrdtype("1");
			disord.setDelFlag("0");
			disord.setTs(new Date());
			disord.setCreator(toUser.getId());
			disord.setCreateTime(new Date());
		}
    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsGzgyDiseaseOrd.class), disOrdList);
    }
	
	/**
	 * 001002002097
	 * 按病种：查询病种信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryDiseaseInfo(String param,IUser user){
		return medForDiseaseMapper.qryDiseaseInfo();
	}
	
	/**
	 * 001002002098
	 * 按病种：根据病种查询关联药品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryDisRefPdInfo(String param,IUser user){
		String pkGzgydisease=JsonUtil.getFieldValue(param, "pkGzgydisease");
		if(CommonUtils.isEmptyString(pkGzgydisease))return null;
		return medForDiseaseMapper.qryDisRefPdInfo(pkGzgydisease);
	}
	
	/**
	 * 001002002099
	 * 按病种：按病种保存病种关联药品信息
	 * @param param
	 * @param user
	 */
	public void saveDisRefPdIfno(String param,IUser user){
		DiseaseRefPdvVo disRefPd=JsonUtil.readValue(param, DiseaseRefPdvVo.class);
		if(disRefPd==null)return;
		
		User toUser=(User)user;
		List<DisRefPdVo> disRefPdList=disRefPd.getDisRefPdList();
		List<InsGzgyDiseaseOrd> addData=new ArrayList<InsGzgyDiseaseOrd>();
		List<InsGzgyDiseaseOrd> updateData=new ArrayList<InsGzgyDiseaseOrd>();
		for (int i = 0; i < disRefPdList.size(); i++) {
			DisRefPdVo pdVo=disRefPdList.get(i);
			InsGzgyDiseaseOrd disOrd=new InsGzgyDiseaseOrd();
			disOrd.setCodeOrd(pdVo.getCode());
			disOrd.setNameOrd(pdVo.getName());
			disOrd.setPkOrd(pdVo.getPkPd());
			disOrd.setDays(pdVo.getDays());
			disOrd.setEuOrdtype("1");
			disOrd.setPkGzgydisease(disRefPd.getPkGzgydisease());
			disOrd.setTs(new Date());
			if(CommonUtils.isEmptyString(pdVo.getPkDiseaseord())){
				disOrd.setPkDiseaseord(NHISUUID.getKeyId());
				disOrd.setCreator(toUser.getId());
				disOrd.setCreateTime(new Date());
				addData.add(disOrd);
			}else{
				disOrd.setPkDiseaseord(pdVo.getPkDiseaseord());
				updateData.add(disOrd);
			}
		}
		if(addData.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsGzgyDiseaseOrd.class), addData);
		}
		if(updateData.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(InsGzgyDiseaseOrd.class), updateData);
		}
	}
	
	/**
	 * 001002002100
	 * 删除病种关联药品信息
	 * @param param
	 * @param user
	 */
	public void delDisRefPd(String param,IUser user){
		List<String> pkDiseaseords=JsonUtil.readValue(param, new TypeReference<List<String>>(){});
		if(pkDiseaseords==null ||pkDiseaseords.size()<=0)return;
		medForDiseaseMapper.delDisRefPd(pkDiseaseords);
	}
}
