package com.zebone.nhis.ex.nis.ns.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.cn.cp.CpRecDt;
import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.nhis.common.module.cn.cp.CpRecExpDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.dao.OrderCpExMapper;
import com.zebone.nhis.ex.nis.ns.vo.CpRecExpVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 临床路径护士确认服务
 * @author yangxue
 */
@Service
public class OrderCpExService {
	
	@Resource
	private OrderCpExMapper orderCpExMapper;
	
	/**
	 * 根据病区获取在院患者列表
	 * @param param-pkDeptNs:当前病区
	 * @param user
	 * @return{name_pi,name_bed,code_bed,pv_code,pk_pv}
	 */
	 public List<Map<String,Object>> queryPatiList(String param, IUser user) throws ParseException{
    	 String pkDeptNs = JsonUtil.readValue(param, String.class);
    	 if(CommonUtils.isEmptyString(pkDeptNs))
    		 throw new BusException("未获取到病区主键！");
 		 return  orderCpExMapper.queryPatiList(pkDeptNs);
	 }
	 
	 /**
	  * 查询所选患者工作记录
	  * @param param-List<String> 患者就诊主键列表
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryCpOrderList(String param, IUser user){
		 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		 if(paramMap == null)
			 throw new BusException("未获取到患者就诊主键！");
		 return orderCpExMapper.queryOrderCpList(paramMap);
	 }
	 
	 /**
	  * 确认处理
	  * @param param - List<CpRecDt>
	  * @param user
	  */
	 public void confirmCpOrder(String param,IUser user){
		 List<CpRecDt> dtlist =  JsonUtil.readValue(param,new TypeReference<List<CpRecDt>>(){});
		 if(dtlist == null || dtlist.size()<=0)
			 throw new BusException("未获取到工作记录明细！");
		 for(CpRecDt dt:dtlist){
			 StringBuilder sql = new StringBuilder("update cp_rec_dt  set date_ex = :dateEx,pk_emp_ex=:pkEmpEx, name_emp_ex=:nameEmpEx,");
			 sql.append("note=:note,eu_status='1' where pk_recdt=:pkRecdt and eu_cpordtype='2' and eu_status='0'");
			 DataBaseHelper.update(sql.toString(),dt);
		 }
	 }
	 
	 /**
	  * 弃用处理
	  * @param param
	  * @param user
	  */
	 public void giveUpCpOrder(String param,IUser user){
		 List<CpRecDt> dtlist =  JsonUtil.readValue(param,new TypeReference<List<CpRecDt>>(){});
		 if(dtlist == null || dtlist.size()<=0)
			 throw new BusException("未获取到工作记录明细！");
		 for(CpRecDt dt:dtlist){
			 StringBuilder sql = new StringBuilder("update cp_rec_dt  set date_ex = :dateEx,pk_emp_ex=:pkEmpEx,name_emp_ex=:nameEmpEx,");
			 sql.append("note=:note,eu_status='2' where pk_recdt=:pkRecdt and eu_cpordtype='2' and eu_status='0'");
			 DataBaseHelper.update(sql.toString(),dt);
		 }
	 }

	/**
	 * 保存差异信息
	 * @param param
	 * @param user
	 */
	public void saveDiffInfo(String param, IUser user) {
		CpRecExpVo exp = JsonUtil.readValue(param, CpRecExpVo.class);
		if (exp == null || exp.getDtlist() == null || exp.getDtlist().size() <= 0)
			throw new BusException("未获取到变异记录！");
		User u = (User) user;
		exp.setPkRecexp(NHISUUID.getKeyId());
		exp.setDateExp(new Date());
		exp.setPkEmpExp(u.getPkEmp());
		exp.setPkOrg(u.getPkOrg());
		exp.setCreateTime(new Date());
		exp.setCreator(u.getPkEmp());
		exp.setDelFlag("0");
		exp.setDtCpexptype(exp.getDtCpexptype());
		List<CpRecExpDt> recExpDts = new ArrayList<CpRecExpDt>();

		for (CpRecExpDt dt : exp.getDtlist()) {
			dt.setPkRecexp(exp.getPkRecexp());
			dt.setPkOrg(u.getPkOrg());
			dt.setPkRecexpdt(NHISUUID.getKeyId());
			dt.setCreateTime(new Date());
			dt.setCreator(u.getPkEmp());
			dt.setDelFlag("0");
			dt.setEuType("3");
			recExpDts.add(dt);
		}
		CpRecExp expvo = new CpRecExp();
		ApplicationUtils.copyProperties(expvo, exp);
		DataBaseHelper.insertBean(expvo);
		if (recExpDts.size() > 0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecExpDt.class), recExpDts);
	}
	 
}
