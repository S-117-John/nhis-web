package com.zebone.nhis.pro.sd.emr.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.sd.emr.dao.SughEmrDataUploadMapper;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病案上传 数据源切换使用
 * @author jd_em
 *
 */
@Service
public class SughEmrDataUploadHandler {
	
	@Resource
	private SughEmrDataUploadMapper emrDataUploadMapper;
	
	@Resource
	private SughEmrDataUploadService uploadService;
	
	/**
	 * 022004002002
	 * 科室质控点击完成时调用省病案接口存储过程
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void updateProcba(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		updateProcba(map);
	}
	
	private void updateProcba(Map<String, Object> map) {
		// String hosCode=ApplicationUtils.getPropertyValue("emr.hos.code", "");
		String hosCode = "sugh";
		if (hosCode != null && hosCode.equals("sugh")) {
			// String val = ApplicationUtils.getSysparam("DeptQCExecMRInf", true);
			String val = "1";
			if (val != null && val.equals("1")) {
				PvEncounter pvEncounter = DataBaseHelper.queryForBean("select pk_pi,pk_pv from pv_encounter where code_pv=?",PvEncounter.class, map.get("codePv"));
				BigDecimal times = new BigDecimal(map.get("ipTimes").toString());
				DataSourceRoute.putAppId("sughproba");
				int result = 0;
				try {
					String codeIp=CommonUtils.getString(map.get("codeIp"));
					String codeIpNew=codeIp.substring(codeIp.length()-6);
					String sql="select count(1) from his_ba1 where FPRN=? and FCYDATE=?";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					Date dt = sdf.parse(map.get("dateEnd").toString());
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					Integer isnull=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{codeIpNew,sdf.format(dt)});
					if(isnull==0){
						//如果没有则查询上传的住院次数有没有重复
						String cfTimeSql="select count(1) from HIS_BA1 where FTIMES=? and FPRN=?";
						Integer isTime=DataBaseHelper.queryForScalar(cfTimeSql, Integer.class, new Object[]{map.get("ipTimes"),codeIpNew});
						if(isTime!=0){
							//如果重复则取最大的值加一
							String maxSql="select max(FTIMES) from his_ba1 where FPRN=?";
							Integer maxtime=DataBaseHelper.queryForScalar(maxSql, Integer.class,new Object[]{codeIpNew});
							
							map.put("ipTimes", maxtime+1);
							map.put("codeIp", codeIpNew);
							result = emrDataUploadMapper.updateProc(map);
						}else{
							map.put("codeIp", codeIpNew);
							result = emrDataUploadMapper.updateProc(map);
						}
						uploadService.updateBaCodeIpAndTimes(codeIp,CommonUtils.getString(map.get("ipTimes")));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					DataSourceRoute.putAppId("default");
					EmrOperateLogs emrOpeLogs = new EmrOperateLogs();
					emrOpeLogs.setCode("page_upload");
					emrOpeLogs.setName("病案上传-深大");
					emrOpeLogs.setDelFlag("0");
					emrOpeLogs.setEuStatus("0");
					emrOpeLogs.setPkOrg(UserContext.getUser().getPkOrg());
					emrOpeLogs.setPkPv(pvEncounter.getPkPv());
					emrOpeLogs.setPkPi(pvEncounter.getPkPi());
					emrOpeLogs.setTimes(times);
					if (e.getMessage().length() > 1000) {
						emrOpeLogs.setOperateTxt(e.getMessage().substring(0,
								1000));
					} else {
						emrOpeLogs.setOperateTxt(e.getMessage());
					}
					uploadService.saveEmrOperLogs(emrOpeLogs);
					throw new RuntimeException(e.getCause().getMessage());
				} finally {
					DataSourceRoute.putAppId("default");
					if (result != 0) {
						uploadService.updatePvip(pvEncounter.getPkPv());
					}
				}
			}
		}
	}

	/**
	 * 022004002003
	 * 科室质控点击完成时调用省病案接口存储过程-扫码
	 * @param param
	 * @param user
	 */
	public void updateProbaBybarcode(String param,IUser user){
		String codeIp=JsonUtil.getFieldValue(param, "codeIp");
		if(CommonUtils.isEmptyString(codeIp))throw new BusException("请输入病案号！");
		StringBuffer sql=new StringBuffer();
		sql.append(" select pi.code_ip,pv.code_pv,pv.name_pi,pvi.ip_times,pv.date_end from pi_master pi ");
		sql.append(" inner join pv_encounter pv on pv.pk_pi=pi.pk_pi");
		sql.append(" inner join pv_ip pvi on pvi.pk_pv=pv.pk_pv");
		sql.append(" where pi.code_ip=?  and pvi.eu_fpupload='0' and  pv.eu_status >=2");
		List<Map<String,Object>> paramList=DataBaseHelper.queryForList(sql.toString(), new Object[]{codeIp});
		if(paramList==null ||paramList.size()==0)throw new BusException("没有查询到需要上传的病案!");
		for (Map<String, Object> map : paramList) {
			updateProcba(map);
		}
	}
}
