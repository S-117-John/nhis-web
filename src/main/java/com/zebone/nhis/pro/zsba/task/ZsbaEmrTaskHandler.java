package com.zebone.nhis.pro.zsba.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.emr.rec.dict.ViewEmrPatList;
import com.zebone.nhis.emr.rec.rec.dao.EmrRecMapper;
import com.zebone.nhis.emr.rec.rec.service.ReqRsltQryService;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

@Service
public class ZsbaEmrTaskHandler {

	@Resource
	private ReqRsltQryService reqRsltQryService;

	@Resource
	private EmrRecMapper recMapper;
	
	/**
	 * 病案上传
	 * @param param
	 * @param user
	 */
	public void updateProcbaSubTask(QrtzJobCfg cfg) {
		String sql="select pat.code_ip,pat.name,pat.code_pv,pat.IP_TIMES,pat.pk_pv from VIEW_EMR_PAT_LIST pat left outer join EMR_PAT_REC patrec on pat.PK_PV=patrec.PK_PV where pat.flag_in='0' and pat.DATE_END>='2021-03-01' and patrec.del_flag='0' and (patrec.flag_code='1' and patrec.code_date is null) ";
		List<ViewEmrPatList> patList =DataBaseHelper.queryForList(sql,ViewEmrPatList.class);
		for (ViewEmrPatList viewEmrPatList : patList) {
			Map<String,Object> map =new HashMap<String, Object>();
			map.put("codeIp", viewEmrPatList.getCodeIp());
			map.put("codePv", viewEmrPatList.getCodePv());
			map.put("name", viewEmrPatList.getName());
			map.put("pkPv", viewEmrPatList.getPkPv());
			String sql2="select times from emr_home_page where pk_pv=? and del_flag='0'";
			String times=DataBaseHelper.queryForScalar(sql2, String.class, map.get("pkPv"));
			map.put("times", times);
			DataSourceRoute.putAppId("BAGL_bayy");
			int result=0;
			try {
				result=recMapper.updateProcbaSubIcd(map);
				result=recMapper.updateProcbaSub(map);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				DataSourceRoute.putAppId("default");
				if(result==0){
					reqRsltQryService.updPatRecFlagCode(map.get("pkPv").toString(),null);
				}
			}
		}
		
	}
}
