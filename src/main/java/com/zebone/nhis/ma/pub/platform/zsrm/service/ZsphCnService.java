package com.zebone.nhis.ma.pub.platform.zsrm.service;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Entry;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医生服务类
 *
 */
@Service
public interface ZsphCnService {
	/**
	 * 手麻状态回传
	 * @param param
	 */
	List<Entry> updataOpStateInfo(String param);

	/**
	 * 检验报告发布
	 * @param param
	 */
	void saveLisReportRelease(String param);

	/**
	 * 检验申请单查询
	 * @param param
	 * @param listener
	 */
	void getLisReportInfo(String param, ResultListener listener);

	/**
	 * 查询检查申请
	 * @param param
	 * @param listener
	 */
	void getRisReportInfo(String param, ResultListener listener);

	/**
	 * 检验报告回收
	 * @param param
	 */
	void deleteLisReportRecovery(String param);

	/**
	 * 发布微生物报告
	 * @param param
	 */
	void saveBactReportRelease(String param);

	/**
	 * 回收微生物报告
	 * @param param
	 */
	void deleteBactReportRecovery(String param);

	/**
	 * 检查报告发布
	 * @param param
	 */
	void saveRisReportRelease(String param);

	/**
	 * 检查报告回收
	 * @param param
	 */
	void deleteRisReportRecovery(String param);

	/**
	 * 检查预约新增
	 * @param param
	 */
	void updateMedicalMake(String param);

	/**
	 *3.16 门诊处方信息查询
	 * @param param
	 * @return
	 */
	List<Entry> getOpCnorder(String param);

	/**
	 *3.71 入院申请变更接口
	 * @param param
	 * @return
	 */
	void pushStatus(String param);


	/**
	 *3.69 检查预约查询
	 * @param param
	 * @return
	 */
	List<Entry> getRisAppointList(String param);

	/**
	 * 3.101.查询门诊病历信息
	 */
	List<Entry> getMzRecordInfo(String param);

	/**
	 * 3.105.物价查询
	 */
	List<Entry> getPriceInquiry(String param);

}
