package com.zebone.platform.manage.transcode.service;

import com.zebone.nhis.common.module.base.transcode.SysLog;
import com.zebone.nhis.common.module.base.transcode.SysServiceRegister;
import com.zebone.nhis.common.module.base.transcode.SysTestCase;
import com.zebone.nhis.common.module.base.transcode.TransCode;
import com.zebone.platform.manage.transcode.vo.QueryForm;
import com.zebone.platform.manage.transcode.vo.SysLogQueryForm;
import com.zebone.platform.modules.dao.support.Page;

import java.util.List;
import java.util.Map;

public interface TranscodeService {

	public Page<?> getTranscodePage(int pageIndex, int pageSize, QueryForm queform);
	
	public List<Map<String, Object>> fuzzySearchTranscodes(SysServiceRegister sysregister);
	
	public void saveTranscode(SysServiceRegister sysregister);
	
	public void updateTranscode(SysServiceRegister sysregister);
	
	public void delTranscode(List<SysServiceRegister> registerlist);
	
	public Map<String, Object> getTranscode(SysServiceRegister sysregister);
	
	List<Map<String, Object>> gettranscodeSet();
	
	List<Map<String, Object>> loadAllProxyNames();
	
	List<Map<String, Object>> getRelativeProxyNames(TransCode transCode);
	
	Map<String, Object> getSelfProxy(TransCode transCode);
	
	public void saveProxyNames(TransCode transCode);
	
	public void updateProxyNames(TransCode transCode);
	
	public List<Map<String, Object>> gettranscodeSetNoTwoLevelLeaf();
	
	List<Map<String, Object>> gettranscodeSetByTj(String id);
	
	void saveTransCodeModul(List<TransCode> tr);
	
	boolean delTransCodeModul(List<TransCode> id);
	
	void changeTransCodeModul(List<TransCode> tr);
	
	Map<String, Object> gettranscodeSetById(TransCode transcode);
	List<Map<String,Object>> getTransCodeSetByText(String text);
	
	List<Map<String, Object>> getTestCasesByTranscode(SysServiceRegister sysregister);
	
	public String addCaseYlmc(SysTestCase cas);
	
	public String updateCaseYlmc(SysTestCase cas);
	
	public void delTestCase(SysTestCase cas);
	
	public void addCaseRcsj(SysTestCase cas);
	
	public void updateCaseRcsj(SysTestCase cas);
	
	public void resetRegisterProxy();
	
	/****************************系统日志  sys_log  部分********************************/
	public Page<?> getSysLogPage(int pageIndex, int pageSize, SysLogQueryForm queform);
	
	public Map<String, Object> getSysLogByPklog(SysLog syslog);
}
