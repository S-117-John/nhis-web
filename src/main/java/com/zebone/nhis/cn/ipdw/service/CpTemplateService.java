package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CpTemplateMapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.common.module.cn.cp.BdCpCateord;
import com.zebone.nhis.common.module.cn.cp.BdCpCateordDt;
import com.zebone.nhis.common.module.cn.cp.CpTemp;
import com.zebone.nhis.common.module.cn.cp.CpTempDept;
import com.zebone.nhis.common.module.cn.cp.CpTempDiag;
import com.zebone.nhis.common.module.cn.cp.CpTempOrd;
import com.zebone.nhis.common.module.cn.cp.CpTempPhase;
import com.zebone.nhis.common.module.cn.cp.CpTempReason;
import com.zebone.nhis.common.module.cn.cp.CpTempWork;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CpTemplateService {
	static org.apache.logging.log4j.Logger log = LogManager.getLogger(CnOpService.class);
	 
	@Autowired      
	private CpTemplateMapper cpTemplateMapper;  
	
	/**
	 * 查询符合条件的临床路径，用于路径维护使用
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CpTemp> queryTemplateList(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkOrg = paramMap.get("pkOrg");
		String pkDept = paramMap.get("pkDept");
		String pkDiag = paramMap.get("pkDiag");
		String cpName = paramMap.get("cpName");
		
		return cpTemplateMapper.getTemplateList(pkOrg, pkDept, pkDiag, cpName);
	}
	public List<CpTempOrd> qryTempPhaseOrd(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkTemp = paramMap.get("pkTemp");
		List<CpTempOrd> ordList = this.cpTemplateMapper.getOrders(pkTemp);
		return ordList; 
	}

	public CpTemp getTemplate(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkTemp = paramMap.get("pkTemp");
		return this.getTemplate(pkTemp);
	}
	private CpTemp getTemplate(String pkTemp){
		List<CpTemp> rs = this.cpTemplateMapper.getTemplate(pkTemp);
		if(rs.size()!=1)
			//throw new RuntimeException("没有找到模板数据！");
			return null;
		CpTemp ret = rs.get(0);
		List<CpTempPhase> phaseList = this.cpTemplateMapper.getTemplatePhases(pkTemp); 
		ret.setPhaseList(phaseList);
		List<CpTempOrd> ordList = this.cpTemplateMapper.getOrders(pkTemp);
		List<CpTempWork> workList = this.cpTemplateMapper.getWorks(pkTemp);
		int idxOrd = 0;
		int idxWork = 0;
		for(int i=0; i<phaseList.size(); i++){
			CpTempPhase phase = phaseList.get(i);
			phase.setOrderList(new ArrayList<CpTempOrd>());
			for(int h=idxOrd; h<ordList.size(); h++){
				idxOrd = h;
				if(!phase.getPkCpphase().equals(ordList.get(h).getPkCpphase()))
					break;
				phase.getOrderList().add(ordList.get(h));
			}
			phase.setWorkList(new ArrayList<CpTempWork>());
			for(int h=idxWork; h<workList.size(); h++){
				idxWork = h;
				if(!phase.getPkCpphase().equals(workList.get(h).getPkCpphase()))
					break;
				phase.getWorkList().add(workList.get(h));
			}
		}
		return ret;
	}
	/**
	 * 获取路径模板的使用原因
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CpTempReason> getReasons(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		List<CpTempReason> ret = this.cpTemplateMapper.getReasons(paramMap.get("pkTemp"));
		return ret;
	}
	/**
	 * 获取路径模板的使用科室
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CpTempDept> getDepts(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		List<CpTempDept> ret = this.cpTemplateMapper.getDepts(paramMap.get("pkTemp"));
		return ret;
	}
	/**
	 * 获取路径模板的对应诊断
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CpTempDiag> getDiags(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		List<CpTempDiag> ret =  this.cpTemplateMapper.getDiags(paramMap.get("pkTemp"));
		return ret;
	}
	
	/**
	 * 保存路径基本信息、阶段及阶段明细
	 * @return
	 */
	public CpTemp saveTemplate(String param , IUser user){
		CpTemp tpl = JsonUtil.readValue(param, new TypeReference<CpTemp>(){});
		User u = (User)user;
		//编码未用，暂时空
		if("2".equals(tpl.getRowStatus())){
			throw new BusException("已经审核的模板不能被更改！");
		}else if("1".equals(tpl.getEuStatus())){
			tpl.setPkEmpEntry(u.getPkEmp());
			tpl.setNameEmpEntry(u.getNameEmp());
			tpl.setDateEntry(new Date());
		}
		if(Constants.RT_NEW.equals(tpl.getRowStatus())){
			tpl.setVersion(this.getVersion(tpl.getNameCp()));
			DataBaseHelper.insertBean(tpl);
		}else if(Constants.RT_UPDATE.equals(tpl.getRowStatus())){
			DataBaseHelper.updateBeanByPk(tpl, false);
		}
		batchSave(tpl.getAllPhases());
		batchSave(tpl.getAllOrders());
		batchSave(tpl.getAllWorks());
		return this.getTemplate(tpl.getPkCptemp());
	}
	
	private <T> void batchSave(List<T> objList){
		String st = "";
		for(T p : objList){
			if(p instanceof CpTempPhase)
				st = ((CpTempPhase)p).getRowStatus();
			else if(p instanceof CpTempOrd)
				st = ((CpTempOrd) p).getRowStatus();
			else if(p instanceof CpTempWork)
				st = ((CpTempWork)p).getRowStatus();
			else 
				continue;
			if(Constants.RT_NEW.equals(st))
				DataBaseHelper.insertBean(p);
			else if(Constants.RT_UPDATE.equals(st))
				DataBaseHelper.updateBeanByPk(p, false);
			else if(Constants.RT_REMOVE.equals(st))
				DataBaseHelper.deleteBeanByPk(p);
		}
		
	}
	
	public void removeTemplate(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkTemp = paramMap.get("pkTemp");
		List<CpTemp> tplList = this.cpTemplateMapper.getTemplate(pkTemp);
		log.info("------------ret.size==="+tplList.size());
		if(tplList==null || tplList.size()!=1)
			return;
		if("2".equals(tplList.get(0).getEuStatus()))
			throw new BusException("已经审核的模板不允许删除!");
		this.cpTemplateMapper.removeTemplate(pkTemp);
		
	}
	
	public void auditTemplate(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		User u = (User)user;
		String pkTemp = paramMap.get("pkTemp");
		String euStatus = paramMap.get("euStatus");
		Date nowDate = new Date();
		this.cpTemplateMapper.auditTemplate(pkTemp, euStatus, u.getPkEmp(), u.getNameEmp(),nowDate);
	}
	public void canlAuditTemplate(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		User u = (User)user;
		String pkTemp = paramMap.get("pkTemp");
		int count = DataBaseHelper.queryForScalar("select  count(pk_cptemp) from cp_rec  where pk_cptemp=? and pk_org=? and del_flag='0' and eu_status='0' ", Integer.class, pkTemp,u.getPkOrg());
		if(count>0){
			throw new BusException("模板正在使用中，无法取消审核!");
		}else{
			DataBaseHelper.update("update cp_temp set eu_status=?, pk_emp_chk=null, name_emp_chk=null, date_chk=null  where pk_cptemp=? and eu_status in ('2','9') ","1",pkTemp);
		}
	}
	
	public void canlSubmitTemplate(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		User u = (User)user;
		String pkTemp = paramMap.get("pkTemp");
		DataBaseHelper.update("update cp_temp set eu_status=?, pk_emp_entry=null, name_emp_entry=null, date_entry=null  where pk_cptemp=? and eu_status ='1' ","0",pkTemp);
	}
	
	public void saveReasons(String param , IUser user){
		List<CpTempReason> reasons = JsonUtil.readValue(param, new TypeReference<List<CpTempReason>>(){});
		for(int i=0; i<reasons.size(); i++){
			CpTempReason reason = reasons.get(i);
			if(Constants.RT_NEW.equals(reason.getRowStatus())){
				reason.setDelFlag("0");
				DataBaseHelper.insertBean(reason);
			}
			else if(Constants.RT_UPDATE.equals(reason.getRowStatus()))
				DataBaseHelper.updateBeanByPk(reason, false);
			else if(Constants.RT_REMOVE.equals(reason.getRowStatus()))
				DataBaseHelper.deleteBeanByPk(reason);
		}
	}
	
	public void saveDepts(String param , IUser user){
		List<CpTempDept> depts = JsonUtil.readValue(param, new TypeReference<List<CpTempDept>>(){});
		for(int i=0; i<depts.size(); i++){
			CpTempDept dept = depts.get(i);
			if(Constants.RT_NEW.equals(dept.getRowStatus())){
				dept.setDelFlag("0");
				DataBaseHelper.insertBean(dept);
			}
			else if(Constants.RT_UPDATE.equals(dept.getRowStatus()))
				DataBaseHelper.updateBeanByPk(dept, false);
			else if(Constants.RT_REMOVE.equals(dept.getRowStatus()))
				DataBaseHelper.deleteBeanByPk(dept);
		}
	}
	
	public void saveDiags(String param , IUser user){
		List<CpTempDiag> diags = JsonUtil.readValue(param, new TypeReference<List<CpTempDiag>>(){});
		for(int i=0; i<diags.size(); i++){
			CpTempDiag diag = diags.get(i);
			if(Constants.RT_NEW.equals(diag.getRowStatus())){
				diag.setDelFlag("0");
				DataBaseHelper.insertBean(diag);	
			}
			else if(Constants.RT_UPDATE.equals(diag.getRowStatus()))
				DataBaseHelper.updateBeanByPk(diag, false);
			else if(Constants.RT_REMOVE.equals(diag.getRowStatus()))
				DataBaseHelper.deleteBeanByPk(diag);
		}
	}
	
	private double getVersion(String cpName) {
		Double ret = this.cpTemplateMapper.getMaxVersion(cpName);
		return ret==null ? 1 : ret+1;
	}
	
	public CpTemp cloneTemplate(String param, IUser user){
		CpTemp tpl = JsonUtil.readValue(param, new TypeReference<CpTemp>(){});
		if(tpl==null)
			return null;
		String pkTemp = tpl.getPkCptemp();
		tpl.setPkCptemp(NHISUUID.getKeyId());
		tpl.setEuStatus("0");		//保存
		tpl.setVersion("1".equals(tpl.getCopyTemp())?1:this.getVersion(tpl.getNameCp()));
		DataBaseHelper.insertBean(tpl);
		HashMap<String, String> hmParent = new HashMap<String, String>();
		for(CpTempPhase p : tpl.getPhaseList()){
			p.setPkCpphase(NHISUUID.getKeyId());
			p.setPkCptemp(tpl.getPkCptemp());
			DataBaseHelper.insertBean(p);
			for(int h=0; h<p.getOrderList().size(); h++){
				CpTempOrd d = p.getOrderList().get(h);
				String oldPk = d.getPkCpord();
				d.setPkCpphase(p.getPkCpphase());
				d.setPkCpord(NHISUUID.getKeyId());
				hmParent.put(oldPk, d.getPkCpord());
				d.setPkParent(hmParent.get(d.getPkParent()));
				DataBaseHelper.insertBean(d);
			}
			for(CpTempWork w : p.getWorkList()){
				w.setPkTempwork(NHISUUID.getKeyId());
				w.setPkCpphase(p.getPkCpphase());
				DataBaseHelper.insertBean(w);
			}
		}
		List<CpTempReason> reasons = this.cpTemplateMapper.getReasons(pkTemp);
		for(CpTempReason r : reasons){
			r.setPkCpreason(NHISUUID.getKeyId());
			r.setPkCptemp(tpl.getPkCptemp());
			DataBaseHelper.insertBean(r);
		}
		List<CpTempDept> depts = this.cpTemplateMapper.getDepts(pkTemp);
		for(CpTempDept d : depts){
			d.setPkCpdept(NHISUUID.getKeyId());
			d.setPkCptemp(tpl.getPkCptemp());
			DataBaseHelper.insertBean(d);
		}
		List<CpTempDiag> diags = this.cpTemplateMapper.getDiags(pkTemp);
		for(CpTempDiag d : diags){
			d.setPkCpdiag(NHISUUID.getKeyId());
			d.setPkCptemp(tpl.getPkCptemp());
			DataBaseHelper.insertBean(d);
		}
		
		return tpl;
	}
	
	public List<BdCpCateord> queryCateordList(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String nameOrd = paramMap.get("nameOrd")+"%";
		String typeOrd = paramMap.get("typeOrd");
		
		List<BdCpCateord> ret = this.cpTemplateMapper.queryCateOrdList(nameOrd, typeOrd);
		return ret;
	}
	
	public BdCpCateord getCateord(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkOrd = paramMap.get("pkOrd");
		BdCpCateord ret = qryCateord(pkOrd);
		return ret;
	}

	private BdCpCateord qryCateord(String pkOrd) {
		BdCpCateord ret = this.cpTemplateMapper.getCateord(pkOrd);
		if(ret!=null){
			List<BdCpCateordDt> details = this.cpTemplateMapper.getCateordDetails(pkOrd, 
					ret.getEuOrdtype() == null? "-1" : ret.getEuOrdtype());
			ret.setDetails(details);
		}
		return ret;
	}
	
	public BdCpCateord saveCateord(String param, IUser user){
		BdCpCateord ord = JsonUtil.readValue(param, new TypeReference<BdCpCateord>(){});
		User u = (User)user;
		boolean isDelDetails=true;
		String delMessage="";
		//编码未用，暂时空
		if(Constants.RT_NEW.equals(ord.getRowStatus())){
			int count = DataBaseHelper.queryForScalar("select count(1) from bd_cp_cateord where pk_org=? and (code_ord=? or name_ord=?) and del_flag='0'", Integer.class, u.getPkOrg(),ord.getCodeOrd(),ord.getNameOrd());
			if(count>0) throw new BusException("编码或名称已存在，请检查不能重复！");
			ord.setDelFlag("0");
			DataBaseHelper.insertBean(ord);
		}else if(Constants.RT_UPDATE.equals(ord.getRowStatus())){
			int count = DataBaseHelper.queryForScalar("select count(1) from bd_cp_cateord where pk_org=? and (code_ord=? or name_ord=?) and del_flag='0' and pk_cateord!=?", Integer.class, u.getPkOrg(),ord.getCodeOrd(),ord.getNameOrd(),ord.getPkCateord());
			if(count>0) throw new BusException("编码或名称已存在，请检查不能重复！");
			DataBaseHelper.updateBeanByPk(ord, false);
		} if(Constants.RT_REMOVE.equals(ord.getRowStatus())){
			if( !qryUseCateOrd(ord.getPkCateord())) {
				DataBaseHelper.deleteBeanByPk(ord);
			}else{
				isDelDetails = false;
				delMessage="分类医嘱已经使用，无法删除！";
			}
		}
		if (ord.getDetails() != null)
		{
			for(BdCpCateordDt dt : ord.getDetails())
			{
				if(Constants.RT_NEW.equals(dt.getRowStatus())){
					DataBaseHelper.insertBean(dt);
				}else if(Constants.RT_UPDATE.equals(dt.getRowStatus())){
					DataBaseHelper.updateBeanByPk(dt, false);
				}else if(isDelDetails && Constants.RT_REMOVE.equals(dt.getRowStatus())){
					if(qryUseCateOrdDt(dt.getPkOrd())) {
						delMessage="分类医嘱明细已经使用，无法删除！";
						continue;
					} 
					DataBaseHelper.deleteBeanByPk(dt);
				}
			}
		}
		BdCpCateord rtn = new BdCpCateord();
		rtn.setDelMessage(delMessage);
		return rtn;
	}
	public boolean qryUseCateOrd(String pkCateord){
	    
		int i =  DataBaseHelper.queryForScalar("select count(1) from bd_cp_cateord where exists (select * from cp_temp_ord where cp_temp_ord.pk_ord=bd_cp_cateord.pk_cateord) and pk_cateord=? ", Integer.class , pkCateord); 
	    return i>0?true:false;
	}
	public boolean qryUseCateOrdDt(String pkOrd){
		
		int i =  DataBaseHelper.queryForScalar("select count(1) from bd_cp_cateord_dt catedt"+
				 " inner join cn_order cn on cn.pk_ord = catedt.pk_ord "+ 
				 " inner join cp_rec_dt recdt  on recdt.pk_cnord = cn.pk_cnord "+
				 " inner join cp_temp_ord tempord on tempord.pk_cpord = recdt.pk_cpord"+
				 " inner join bd_cp_cateord cateord on cateord.pk_cateord = tempord.pk_ord and tempord.eu_ordtype='2'"+
				 " where catedt.pk_ord=?", Integer.class , pkOrd); 
		return i>0?true:false;
	}
}
