package com.zebone.nhis.pro.zsba.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.pub.vo.CnLabApplyVo;
import com.zebone.nhis.ex.pub.dao.AdtPubMapper;
import com.zebone.nhis.pro.zsba.adt.service.ZsbaAdtService;
import com.zebone.nhis.pro.zsba.adt.vo.CnRisApplyBaVo;
import com.zebone.nhis.pro.zsba.adt.vo.LabAndRisTripartiteSystemVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

@Service
public class ZsbaSysThirdHandler {
	
	@Resource
	private ZsbaAdtService zsbaAdtService;

	private Logger logger = LoggerFactory.getLogger("com.zebone");
	
	@Autowired
	private AdtPubMapper adtPubMapper;
	
	public void sysAppStatusByInHospital(QrtzJobCfg cfg) {
		String pkOrg = cfg.getJgs();
        if (CommonUtils.isEmptyString(pkOrg))
            throw new BusException("未获取到机构信息！");       
        Map<String, Object> paramMap = new HashMap<String, Object>();
        
        //获取待同步的患者就诊主键清单
        List<Map<String, Object>> list = zsbaAdtService.getNeedSysStatusPatis(paramMap);
        if(null == list || list.size() < 1) return;
        
        List<String> pkPvRis = new ArrayList<String>();//待同步的检查患者列表
        List<String> pkPvLis = new ArrayList<String>();//待同步的检验患者列表
        
        for (Map<String, Object> map : list) {
			if(!"0".equals(map.get("cntRis")))
				pkPvRis.add(CommonUtils.getString(map.get("pkPv"), ""));
			if(!"0".equals(map.get("cntLab")))
				pkPvLis.add(CommonUtils.getString(map.get("pkPv"), ""));
		}
        
        BdOuEmployee emp = DataBaseHelper.queryForBean(" select * from bd_ou_employee where code_emp = '00000' ", BdOuEmployee.class, null);
		if(emp == null)
			throw new BusException(" 请添加系统人员【00000】！");
				
		User user = new User();
		user.setPkOrg(emp.getPkOrg());
		user.setNameEmp(emp.getNameEmp());
		user.setPkEmp(emp.getPkEmp());
		user.setCodeEmp(emp.getCodeEmp());
		UserContext.setUser(user);
        
		List<LabAndRisTripartiteSystemVo> labTripartiteSystemVos = null;
		List<LabAndRisTripartiteSystemVo> risTripartiteSystemVos = null;
		List<CnLabApplyVo> cnlabApplyVoNotDone = null;
		List<CnRisApplyBaVo> cnRisApplyVoNotDone = null;
		
		//处理检验
		if(null != pkPvLis && pkPvLis.size() > 0){
			paramMap.remove("pkPvs");
			paramMap.put("pkPvs", pkPvLis);
			cnlabApplyVoNotDone = zsbaAdtService.getCnlabApplyVoNotDone(paramMap);
			if (!CollectionUtils.isEmpty(cnlabApplyVoNotDone)) {
				DataSourceRoute.putAppId("LIS_bayy");
				try {
					labTripartiteSystemVos = adtPubMapper.getLabTripartiteSystemVos(cnlabApplyVoNotDone);
				} catch (Exception e) {
					logger.info("出院查询三方检验报错" + e.getMessage(), cnlabApplyVoNotDone);
				} finally {
					DataSourceRoute.putAppId("default");
				}
			}
		}
		
		//处理检查
		if(null != pkPvRis && pkPvRis.size() > 0){
			paramMap.remove("pkPvs");
			paramMap.put("pkPvs", pkPvRis);
			cnRisApplyVoNotDone = zsbaAdtService.getCnRisApplyVoNotDone(paramMap);
			if (!CollectionUtils.isEmpty(cnRisApplyVoNotDone)) {
				DataSourceRoute.putAppId("baPacs");
				try {
					risTripartiteSystemVos = adtPubMapper.getRisTripartiteSystemVos(cnRisApplyVoNotDone);
				} catch (Exception e) {
					logger.info("出院查询三方检查报错" + e.getMessage(), cnRisApplyVoNotDone);
				} finally {
					DataSourceRoute.putAppId("default");
				}
			}
		}
		zsbaAdtService.updateEustus(labTripartiteSystemVos, cnlabApplyVoNotDone, risTripartiteSystemVos, cnRisApplyVoNotDone,user);
	}
}
