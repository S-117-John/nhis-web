package com.zebone.nhis.pro.sd.emr.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.sd.emr.dao.SughEmrDataUploadMapper;
import com.zebone.nhis.pro.sd.emr.vo.EmrDataQueryVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 深圳病案数据上传服务
 * @author jd_em
 *
 */
@Service
public class SughEmrDataUploadService {
	
	@Resource
	private SughEmrDataUploadMapper emrDataUploadMapper;
	
	/**
	 * 022004002001
	 * 查询病案数据
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> queryEmrDataUpList(String param,IUser user){
		EmrDataQueryVo queryVo=JsonUtil.readValue(param, EmrDataQueryVo.class);
		if(queryVo==null)throw new BusException("请至少输入一个有效条件进行查询！");
		int pageIndex = CommonUtils.getInteger(queryVo.getPageIndex());
		int pageSize = CommonUtils.getInteger(queryVo.getPageSize());
		User u=UserContext.getUser();
		queryVo.setPkOrg(u.getPkOrg());
		queryVo.setDateEndStart(subDate(queryVo.getDateEndStart(),"0"));
		queryVo.setDateEnd(subDate(queryVo.getDateEnd(),"1"));
		queryVo.setDateUpEnd(subDate(queryVo.getDateUpEnd(),"1"));
		queryVo.setDateUpStart(subDate(queryVo.getDateUpStart(),"0"));
		
		
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String, Object>> mapResult = emrDataUploadMapper.queryEmrDataUpList(queryVo);
		Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
		
		Map<String,Object> resMap=new HashMap<String,Object>();
		resMap.put("resList", mapResult);
		resMap.put("totalCount", page.getTotalCount());
		return resMap;
	} 
	
	/**
	 * 病案首页上传数据失败插入日志信息
	 * @param medRec
	 * @param now
	 * @param strCode
	 * @param paraCode
	 * @param docTxt
	 */
	public void saveEmrOperLogs(EmrOperateLogs emrOpeLogs) {
		String sql="select * from emr_operate_logs where pk_pv=? and del_flag='0'";
		EmrOperateLogs exist=DataBaseHelper.queryForBean(sql, EmrOperateLogs.class, emrOpeLogs.getPkPv());
		if(exist==null){
			DataBaseHelper.insertBean(emrOpeLogs);
		}
	}
	
	/**
	 * 更新pv_ip数据
	 * @param pkPv
	 */
	public void updatePvip(String pkPv){
		User user=UserContext.getUser();
		String sql="update pv_ip set eu_fpupload='1' , date_fpupload=?,pk_emp_fpupload=?,name_emp_fpupload=? where pk_pv=? ";
		DataBaseHelper.execute(sql, new Object[]{new Date(),user.getPkEmp(),user.getNameEmp(),pkPv});
	}
	
	/**
	 * 截取时间
	 * @param datetime yyyyMMddhh24miss
	 * @param type 0:000000;1:235959
	 * @return
	 */
	private String subDate(String datetime,String type){
		if(CommonUtils.isEmptyString(datetime))return "";
		if("0".equals(type)){
			return datetime.substring(0, 8)+"000000";
		}else{
			return datetime.substring(0, 8)+"235959";
		}
	}

	/**
	 * 重新赋值住院号
	 * @param codeIp
	 * @param itmes
	 */
	public void updateBaCodeIpAndTimes(String codeIp,String itmes){
		String codeIpNew=codeIp.substring(codeIp.length()-6);
	    String upBa1="update his_ba1 set fprn=?  where fprn=? and ftimes=?";
		String upBa2="update his_ba2 set fprn=?  where fprn=? and ftimes=?";
	    String upBa3="update his_ba3 set fprn=? where fprn=? and ftimes=?";
		String upBa4="update his_ba4 set fprn=?  where fprn=? and ftimes=?";
		String upBa5="update his_ba5 set fprn=?  where fprn=? and ftimes=?";
		DataBaseHelper.execute(upBa1, new Object[]{codeIpNew,codeIp,itmes});
		DataBaseHelper.execute(upBa2, new Object[]{codeIpNew,codeIp,itmes});
		DataBaseHelper.execute(upBa3, new Object[]{codeIpNew,codeIp,itmes});
		DataBaseHelper.execute(upBa4, new Object[]{codeIpNew,codeIp,itmes});
		DataBaseHelper.execute(upBa5, new Object[]{codeIpNew,codeIp,itmes});
	}
}
