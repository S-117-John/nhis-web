package com.zebone.nhis.ma.pub.syx.service;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.ma.pub.syx.dao.QryOldSysPiMapper;
import com.zebone.nhis.ma.pub.syx.vo.OldPiInfo;
import com.zebone.nhis.ma.pub.syx.vo.Tippatientseqno;
import com.zebone.nhis.ma.pub.syx.vo.Tpatient;
import com.zebone.nhis.pi.pub.service.PiPubService;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class QryOldSystemPiToHisService {

	@Autowired
	private QryOldSysPiMapper qryOldSysPiMapper;

	@Autowired
	private PiPubService piPubService;

	/**
	 * 1.查询旧系统患者 
	 * @param qryMap
	 * @return
	 */
	public List<OldPiInfo> qryPiInfo(Map<String, Object> qryMap) {
		String dtSex = (String)qryMap.get("dtSex");
		if("02".equals(dtSex)){
			qryMap.put("dtSex", "1");
		}else if("03".equals(dtSex)){
			qryMap.put("dtSex", "2");
		}else if("04".equals(dtSex)){
			qryMap.put("dtSex", "0");
		}
		if (qryMap.get("codeIp") != null && !"".equals(qryMap.get("codeIp").toString())) {
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
			if (!pattern.matcher(qryMap.get("codeIp").toString()).matches()) {
				throw new BusException("旧系统住院号为纯数字，请输入正确的住院号查询！");
			}
		}
		List<OldPiInfo> piInfo = qryOldSysPiMapper.queryPiInfo(qryMap);
		for(PiMaster temp:piInfo){
			if(StringUtils.isEmpty(temp.getInsurNo().trim()))
				temp.setInsurNo("");
		}
		return piInfo;
	}
	/**
	 * 2.判断HIS系统是否存在该患者，不存在则插入
	 * @param piInfo
	 */
	public void savePiToHis(List<PiMaster> piInfo) {
		if (piInfo != null && piInfo.size() > 0) {
			for (PiMaster item : piInfo) {
				int count = DataBaseHelper.queryForScalar("select count(*) as count from pi_master where code_pi=?", Integer.class, item.getCodePi());
				if (count <= 0) {
					PiMasterParam mParam = new PiMasterParam();
					mParam.setMaster(item);
					piPubService.savePiMasterParam(mParam);
				}
			}
		}
	}

	/**
	 * 保存患者就诊信息至旧系统,患者新增
	 * 
	 * @param tpatient 
	 * @param tippatientseqno 
	 * 
	 * @param regParam
	 */
	public String savePvToOldSys(Tippatientseqno tippatientseqno, Tpatient tpatient) {
		String codeIp = null;
		//查询旧系统是否存在该患者
			Integer count = DataBaseHelper.queryForScalar("select count(1) from tIPPatientSeqNO where IPSeqNo=?", Integer.class, new Object[] {tippatientseqno.getIpseqno()});
			if(count>0){
				throw new BusException("该住院号在旧系统中已被使用，请清空住院号重新保存患者！");
			}
			DataBaseHelper.insertBean(tippatientseqno);
			DataBaseHelper.insertBean(tpatient);
		return codeIp;
	}

	/**
	 * 查询新增患者住院号保存至新系统中
	 * 
	 * @return
	 */
	public Object qryCodeIp4OldSys(PiMaster pi) {
		Map<String,Object> saveParam = Maps.newHashMap();
		Tippatientseqno tippatientseqno = DataBaseHelper.queryForBean(" select * from tIPPatientSeqNO where PatientID=? and IPSeqNo=?", Tippatientseqno.class, new Object[]{pi.getCodePi(),pi.getCodeIp()});
		//如果旧系统不存在该患者，查詢患者住院号,调用新增方法
		if(tippatientseqno == null){
			saveParam.put("method", "savePvToOldSys"); //保存患者至旧系统方法
		}else{
			saveParam.put("ipTimes", tippatientseqno.getIptimes());
			saveParam.put("method", "updateOldSysPi");//更新旧系统患者
		}
		return saveParam;
	}
	
	/**
	 * 更新旧系统患者
	 * @param piMaster
	 */
	public Map<String, Object> updateOldSysPi(PiMaster piMaster) {
		int count = DataBaseHelper.execute("update tIPPatientSeqNO set IPTimes=IPTimes+1 where PatientID=?", piMaster.getCodePi());
		Map<String, Object> ipTimes = null;
		if (count > 0) {
			ipTimes = DataBaseHelper.queryForMap("select IPTimes from tIPPatientSeqNO where PatientID=? and IPSeqNoText=?", new Object[]{piMaster.getCodePi(), piMaster.getCodeIp()});
		}
		return ipTimes;
	}
	
	/**
	 * 取消入院，更新旧系统患者
	 * @param pi
	 */
	public void cancelInhospital(PiMaster pi) {
		int piCount = DataBaseHelper.queryForScalar("SELECT count(*) FROM tIPPatientSeqNO WHERE IPSeqNoText=?", Integer.class, pi.getCodeIp());
		if(piCount > 0){
			DataBaseHelper.execute("update tIPPatientSeqNO set IPTimes=IPTimes-1 where IPSeqNoText=? and IPTimes>0", pi.getCodeIp());
		}
	}
	
	/**
	 * 删除新HIS系统的患者
	 * @param qryPiInfo
	 */
	public void delNhisPi(List<OldPiInfo> qryPiInfo) {
		for(OldPiInfo pi : qryPiInfo){
			DataBaseHelper.execute("delete from pi_master pi where NOT exists(SELECT 1 FROM PV_ENCOUNTER pv WHERE pi.PK_PI=pv.PK_PI) and pi.code_pi=? ", pi.getCodePi());
		}
	}
	
	public void delNhisPi(String CodeIp) {
			DataBaseHelper.execute("delete from pi_master pi where NOT exists(SELECT 1 FROM PV_ENCOUNTER pv WHERE pi.PK_PI=pv.PK_PI) and pi.code_ip=? ", CodeIp);
	}
	
	/**
	 * 中山二院需求，保存旧系统患者信息至新系统（读卡器读取身份证保存） 
	 * 010005002006
	 * @param param
	 * @param user
	 */
	public PiMaster saveOldPi(String param ,IUser user){
		PiMaster pi = piPubService.savePiMasterParam(param ,user);
		return pi;
	}
}
