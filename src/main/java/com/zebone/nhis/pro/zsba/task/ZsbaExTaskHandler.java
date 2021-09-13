package com.zebone.nhis.pro.zsba.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.pro.zsba.task.dao.ZsbaOrderExecAndCgListMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

public class ZsbaExTaskHandler {
	
	private Logger logger = LoggerFactory.getLogger("nhis.quartz");
	@Resource
	private ZsbaOrderExecAndCgListMapper zsbaOrderExecAndCgListMapper;
	 @Resource
	private BlCgExPubService blCgExPubService;
	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	/**
	 * 自动执行本病区执行的医嘱执行单并记费
	 * @param cfg
	 */
	public Map autoCg(QrtzJobCfg cfg){
		String pkOrg = cfg.getJgs();
		if(CommonUtils.isEmptyString(pkOrg)){
			throw new BusException("未获取到机构信息！");
		}
		String [] orgArr=pkOrg.split(",");
		StringBuilder errMsg = new StringBuilder("");
		
		
		if(orgArr!=null&&orgArr.length>0){
			for(int i = 0; i<orgArr.length;i++){
				 List<Map<String,Object>> pvlist = getAllPatiList(orgArr[i]);
				if(pvlist==null||pvlist.size()<=0)
					continue;
				for(Map<String,Object> pvMap : pvlist){
					 this.execCg(pkOrg, CommonUtils.getString(pvMap.get("pkPv")), errMsg, CommonUtils.getString(pvMap.get("nameDeptNs")), CommonUtils.getString(pvMap.get("bedNo")), CommonUtils.getString(pvMap.get("namePi")),CommonUtils.getString(pvMap.get("pkDeptNs")),cfg);
				}
			   
			}
		}
		Map<String,String> result = new HashMap<String,String>();
		if(errMsg != null && !errMsg.toString().equals("")){
			 result.put("msg", StringUtils.substring(errMsg.toString(),0,1900));
		}
		return result;
	}
	/**
     * 根据用户编码查询用户信息
     * @param pkEmpChk
     * @param pkOrg
     * @return
     */
    private User findUserByPk(String pkEmpChk, String pkOrg) {
       String  sql = " select emp.*,job.pk_dept from  bd_ou_employee emp  left join bd_ou_empjob job on job.pk_emp = emp.pk_emp and job.is_main='1' where emp.pk_emp = ? and emp.pk_org = ? " ;
        User u = DataBaseHelper.queryForBean(sql, User.class, new Object[]{pkEmpChk,pkOrg});
        return u;
    }
    
    private StringBuilder execCg(String pkOrg, String pkPv, StringBuilder errMsg, String nameDeptNs, String bedNo, String namePi,String pkDeptNs,QrtzJobCfg cfg){
    	 Map<String,Object> map = new HashMap<String,Object>();
		 //map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
		 map.put("dateEnd", DateUtils.getDateTimeStr(new Date()));
		 map.put("euStatus", "0");//默认查询未执行执行单
		 map.put("pkOrg", pkOrg);
		 map.put("confirmFlag", "1");
		 map.put("pkPv", pkPv);
		 List<ExlistPubVo> result = new ArrayList<ExlistPubVo>();
		 //查询执行科室为本病区护理和治疗类医嘱的未执行执行单
		 result =  zsbaOrderExecAndCgListMapper.queryExecListByCon(map);
		 if(result==null||result.size()<=0)
			 return errMsg;
		 //由于传进去的user是核对人，需要根据核对人分组
		 Map<String,List<ExlistPubVo>> chkMap = new HashMap<>();
		 //Set<String> ordSet = new HashSet<String>();
		 Set<String> pkEmpChkSet = new HashSet<String>();
		 String isDatePlan="";
			if(CommonUtils.isNotNull(cfg.getJobparam())){
				Map<String,Object> jobParam=JsonUtil.readValue(cfg.getJobparam(), Map.class);
				if(jobParam!=null){
					isDatePlan= jobParam.get("isDatePlan")!=null ? jobParam.get("isDatePlan").toString():"";
				}
			}
		 for(ExlistPubVo ex : result){
			 
			 if("1".equals(isDatePlan)){
				 //ordSet.add(ex.getPkCnord());
				 ex.setDateOcc(ex.getDatePlan());
			 }
			 pkEmpChkSet.add(ex.getPkEmpChk());
		 }
		 for(String st : pkEmpChkSet){
			 List<ExlistPubVo> voList = new ArrayList<>();
			 for(ExlistPubVo ex : result){
			 	if(st.equals(ex.getPkEmpChk())){
			 		ex.setEuBltype("99");
					voList.add(ex);
				}
			 }
			 if(voList.size() > 0){
				 chkMap.put(st,voList);
			 }
		 }
		 // 关闭事务自动提交
		 DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		 TransactionStatus status = platformTransactionManager.getTransaction(def);
		 try{
		 	for(Map.Entry<String,List<ExlistPubVo>> entry : chkMap.entrySet()){
				User u = new User();
				u.setPkOrg(pkOrg);
				UserContext.setUser(u);
				u = findUserByPk(entry.getKey(),pkOrg);
				u.setPkDept(pkDeptNs);//执行科室设置为病区
				UserContext.setUser(u);
				//调用公共执行计费服务
				blCgExPubService.execAndCg(entry.getValue(), u);
			}
			//DataBaseHelper.update("UPDATE CN_TRANS_APPLY SET eu_status='3'where eu_status < '3' and pk_cnord in ("+ CommonUtils.convertSetToSqlInPart(ordSet, "pk_cnord")+")");
			platformTransactionManager.commit(status); // 提交事务

		 }catch(Exception e){
		 	logger.error("自动执行医嘱失败:",e);
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			errMsg.append("病区【"+nameDeptNs+"】【"+bedNo+"】床患者【"+namePi+"】自动执行医嘱失败:" + ExceptionUtils.getRootCauseMessage(e));
		}
		 return errMsg;
    }
    
    /**
	 * 获取所有住院患者
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> getAllPatiList(String pk_org) throws BusException {
		StringBuilder sql = new StringBuilder("select pv.bed_no, pv.name_pi, pv.pk_pv, pv.date_admit, pv.pk_dept_ns,deptns.name_dept as name_dept_ns ");
		sql.append(" from pv_encounter pv left join bd_ou_dept deptns on deptns.pk_dept = pv.pk_dept_ns ");
		sql.append(" where pv.pk_org = ? ");
		sql.append(" and pv.flag_in = '1' and pv.eu_status = '1' order by pv.pk_dept_ns, pv.bed_no ");
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sql.toString(), new Object[]{pk_org});
		return list;
	}
	
}
