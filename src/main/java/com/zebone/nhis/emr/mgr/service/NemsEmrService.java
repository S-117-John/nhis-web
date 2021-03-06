package com.zebone.nhis.emr.mgr.service;


import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.cn.opdw.vo.CnOrderBlOpDtVo;
import com.zebone.nhis.common.module.emr.mgr.EmrBorrowDoc;
import com.zebone.nhis.common.module.emr.mgr.EmrBorrowRec;
import com.zebone.nhis.common.module.emr.mgr.EmrRecallRec;
import com.zebone.nhis.common.module.emr.mgr.EmrSealItem;
import com.zebone.nhis.common.module.emr.mgr.EmrSealRec;
import com.zebone.nhis.common.module.emr.qc.EmrEventRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRule;
import com.zebone.nhis.common.module.emr.qc.EmrGradeStandard;
import com.zebone.nhis.common.module.emr.qc.EmrGradeType;
import com.zebone.nhis.common.module.emr.qc.EmrMedRecTask;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.emr.common.EmrConstants;
import com.zebone.nhis.emr.mgr.dao.EmrMgrMapper;
import com.zebone.nhis.emr.mgr.dao.NemsEmrMapper;
import com.zebone.nhis.emr.mgr.vo.EmrSealItemParam;
import com.zebone.nhis.emr.mgr.vo.EmrSealParam;
import com.zebone.nhis.emr.mgr.vo.EmrSealVo;
import com.zebone.nhis.emr.qc.dao.EmrQCMapper;
import com.zebone.nhis.emr.qc.vo.EmrMedRecTaskVo;
import com.zebone.nhis.emr.rec.rec.dao.EmrRecMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * ????????????????????????
 * @author yuanxinan
 *
 */
@Service
public class NemsEmrService {

	@Resource
	private	NemsEmrMapper nemsMapper;


	/**
	 * ????????????????????????????????????(?????????????????????)
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map> listEmrPatArch(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);

		List<Map> emrpaList=nemsMapper.listEmrPatArch(map);
		return emrpaList;
	}


	/**
	 * ????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public void applyRecall(String param , IUser user){
		List<Map> list = JSON.parseArray(param,
				Map.class);

		//??????????????????
		for (Map map : list) {
			EmrRecallRec emrRecallRec=new EmrRecallRec();
			emrRecallRec.setPkRecall((String) map.get("pkRecall"));
			emrRecallRec.setPkPv((String) map.get("pkPv"));
			emrRecallRec.setPkPi((String) map.get("pkPi"));
			emrRecallRec.setPkPatrec((String) map.get("pkPatrec"));
			emrRecallRec.setPkEmpApp((String) map.get("pkEmpApp"));
			emrRecallRec.setApplyReason((String) map.get("applyReason"));
			emrRecallRec.setEuStatus((String) map.get("euStatus"));
			emrRecallRec.setDateApply(new Date());
			DataBaseHelper.insertBean(emrRecallRec);
		}


	}


	/**
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map> listEmrPat(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);

		List<Map> emrpaList=nemsMapper.listEmrPat(map);

		return emrpaList;

	}  

	/**
	 * ????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public void agreeRecall(String param , IUser user){
		List<Map> params = JSON.parseArray(param,
				Map.class);
		for (Map map : params) {
			DataBaseHelper.update("update emr_recall_rec set eu_status = ? where pk_recall = ?", map.get("euStatus"),map.get("pkRecall"));
			map.put("pkEmpArchive",((User) user).getId());
			map.put("archiveDate",new Date());
			nemsMapper.agreeRecall(map);
		}

	}

	/**
	 * ????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public void refuseRrecall(String param , IUser user){
		List<Map> params = JSON.parseArray(param,
				Map.class);

		for (Map map : params) {
			DataBaseHelper.update("update emr_recall_rec set eu_status = ?,apply_reason = ? where pk_recall = ?",map.get("euStatus"),map.get("applyReason"), map.get("pkRecall"));
		}
	}

	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrSealVo> searchMothPat(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		return nemsMapper.searchMothPat(map);
	}

	/**
	 * ????????????
	 * @param param
	 * @param user
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void medicalRecord(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		List<EmrSealParam> sealParams = JSON.parseArray(param,
				EmrSealParam.class);
		if(sealParams!=null&&sealParams.size()>0){
		for (EmrSealParam sealParam : sealParams) {
				EmrSealRec emrSealRec=DataBaseHelper.queryForBean("select * from emr_seal_rec where pk_pv = ? and del_flag='0'", EmrSealRec.class, sealParam.getPkPv());
				if(emrSealRec!=null){//??????
					nemsMapper.updateEmrSealRec(sealParam);
				}else{//??????
					EmrSealRec emrRec=new EmrSealRec();
					BeanUtils.copyProperties(emrRec, sealParam);
					//emrRec.setPkSealrec(NHISUUID.getKeyId());
					DataBaseHelper.insertBean(emrRec);
	
					EmrSealItem emrItem=new EmrSealItem();
					BeanUtils.copyProperties(emrItem, sealParam);
					emrItem.setPkSealrec(emrRec.getPkSealrec());
					DataBaseHelper.insertBean(emrItem);
				}
			}
		}
	}

	/**
	 * ???????????????????????????
	 * @param param
	 * @param use
	 * @return
	 */
	public List<Map> listArchPat(String param , IUser use){
		Map map = JsonUtil.readValue(param,Map.class);

		return nemsMapper.listArchPat(map);

	}
	/**
	 * ????????????
	 * @param param
	 * @param use
	 * @return
	 */
	public void unlockRec(String param , IUser use){
		List<Map> params = JSON.parseArray(param,
				Map.class);
		for (Map map : params) {
			nemsMapper.unlockRec(map);
		}
		//DataBaseHelper.batchUpdate("update emr_seal_rec set del_flag ='1' where pk_pv = :pkPv",pkPvs);
	}
}

