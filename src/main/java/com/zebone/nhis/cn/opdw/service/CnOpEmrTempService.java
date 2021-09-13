package com.zebone.nhis.cn.opdw.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.opdw.vo.BdOpEmrTempCateVo;
import com.zebone.nhis.common.support.EnumerateParameter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.opdw.dao.CnOpEmrTempMapper;
import com.zebone.nhis.cn.opdw.dao.CnOpHerbPresMapper;
import com.zebone.nhis.cn.opdw.vo.BdOpEmrTempVo;
import com.zebone.nhis.cn.opdw.vo.bdOpEmrVo;
import com.zebone.nhis.common.module.cn.opdw.BdOpEmrTemp;
import com.zebone.nhis.common.module.cn.opdw.BdOpEmrTempCate;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnOpEmrTempService {
	
	@Autowired
	private CnOpEmrTempMapper cnOpEmrTempMapper;
	 /**
	  * 查询当前用户的门诊模板分类
	  * @param param
	  * @param user
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public List<BdOpEmrTempCate> qryTempCate(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
 		 String euRange = JsonUtil.getFieldValue(param, "euRange");
		 String val = JsonUtil.getFieldValue(param, "val");
		 //String pkFather = JsonUtil.getFieldValue(param, "pkFather");
		 String sql = "select * from bd_opemr_tempcate cate ";
		 
		 //if(StringUtils.isEmpty(pkFather) || "null".equalsIgnoreCase(pkFather)) sql += " where pk_father is null ";
		 //else sql += " where pk_father='"+pkFather+"'";
		 
		 if(StringUtils.isEmpty(euRange)){
			 return execQryTempCate(sql);
		 }
		 
		 if(StringUtils.isEmpty(val)){
			 return execQryTempCate(sql);
		 }
		 
		 if("0".equals(euRange)){
			 sql += " where cate.pk_org='"+val+"' and  cate.eu_range=0";
			 return execQryTempCate(sql);
		 }
		 
		 if("1".equals(euRange)){
			 sql += " where cate.pk_dept='"+val+"' and cate.eu_range=1";
			 return execQryTempCate(sql);
		 }
		 
		 if("2".equals(euRange)){
			 sql += " where cate.pk_emp='"+val+"' and cate.eu_range=2";
			 return execQryTempCate(sql);
		 }		 
		 
		 return null;		 
	 }
	 
	 /**
	  * 查询模板的code和name是否重复
	  * @param param
	  * @param user
	  * @return 0正常 1code重复 2name重复
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public int  qryUniqueTemp(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String euRange = JsonUtil.getFieldValue(param, "euRange");
		 String code = JsonUtil.getFieldValue(param, "code");
		 String name = JsonUtil.getFieldValue(param, "name");
		//String pkFather = JsonUtil.getFieldValue(param, "pkFather");
		 String sql = "select * from bd_opemr_tempcate cate inner join bd_opemr_temp t on t.pk_tempcate = cate.pk_tempcate ";
		 
		 //if(StringUtils.isEmpty(pkFather) || "null".equalsIgnoreCase(pkFather)) sql += " where pk_father is null ";
		 //else sql += " where pk_father='"+pkFather+"'";
		 
		 User u = (User)user;
		 
		 if(StringUtils.isEmpty(euRange)){
			 return 0;
		 }
		 
		 if(!StringUtils.isEmpty(code)){
			  if("0".equals(euRange)){
				  String ss = sql + " where cate.pk_org='"+u.getPkOrg()+"' and  cate.eu_range=0"+" and t.code='"+code+"'";
				 if(DataBaseHelper.queryForList(ss).size() > 0) return 1;
			 }
			 
			 if("1".equals(euRange)){
				 String ss = sql + " where cate.pk_dept='"+u.getPkDept()+"' and cate.eu_range=1"+" and t.code='"+code+"'";
				 if(DataBaseHelper.queryForList(ss).size() > 0) return 1;
			 }
			 
			 if("2".equals(euRange)){
				 String ss = sql + " where cate.pk_emp='"+u.getPkEmp()+"' and cate.eu_range=2" +" and t.code='"+code+"'";
				 if(DataBaseHelper.queryForList(ss).size() > 0) return 1;
			 }				 
		 }		 
		 
		 if(!StringUtils.isEmpty(name)){
			  if("0".equals(euRange)){
				 String ss = sql + " where cate.pk_org='"+u.getPkOrg()+"' and  cate.eu_range=0"+" and t.name='"+name+"'";
				 if(DataBaseHelper.queryForList(ss).size() > 0) return 2;
			 }
			 
			 if("1".equals(euRange)){
				 String ss = sql + " where cate.pk_dept='"+u.getPkDept()+"' and cate.eu_range=1"+" and t.name='"+name+"'";
				 if(DataBaseHelper.queryForList(ss).size() > 0) return 2;
			 }
			 
			 if("2".equals(euRange)){
				 String ss = sql + " where cate.pk_emp='"+u.getPkEmp()+"' and cate.eu_range=2" +" and t.name='"+name+"'";
				 if(DataBaseHelper.queryForList(ss).size() > 0) return 2;
			 }				 
		 }		 
		 
		 
		 return 0;		 
	 }
	 
	 /**
	  * 执行查询当前用户的门诊模板分类
	  * @param sql
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 private List<BdOpEmrTempCate> execQryTempCate(String sql) throws IllegalAccessException, InvocationTargetException{
		 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		 
		 List<BdOpEmrTempCate> ret = new ArrayList<BdOpEmrTempCate>();		 
				 
		 for(Map<String,Object> map : ps){
			 BdOpEmrTempCate bet = new BdOpEmrTempCate();
			 BeanUtils.copyProperties(bet, map);	
			 
			 ret.add(bet);
		 }
		 
		 return ret;
	 }
	 
	 /**
	  * 查询当前分类的父级分类
	  * @param param
	  * @param user
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public List<BdOpEmrTempCate> qryParentCate(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String pkFather = JsonUtil.getFieldValue(param, "pkFather");
		 List<BdOpEmrTempCate> fs = new ArrayList<BdOpEmrTempCate>();
		 
		 if(StringUtils.isEmpty(pkFather) || "null".equalsIgnoreCase(pkFather)){
			 String sql = "select * from bd_opemr_tempcate cate  where cate.pk_father is null";
			 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
			 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
			 
			 for(Map<String,Object> map : ps){
				 BdOpEmrTempCate bot = new BdOpEmrTempCate();
				 BeanUtils.copyProperties(bot, map);	
				 if(StringUtils.isEmpty(pkFather)) pkFather = bot.getPkFather();
				 
				 fs.add(bot);
			 }			 
			 
			 return fs;
		 }
		 
		
		 return qryParentCateExec(pkFather,fs);
	 }
	 
	 
	 public List<BdOpEmrTempCate> qryParentCateExec(String pkFather,List<BdOpEmrTempCate> fs) throws IllegalAccessException, InvocationTargetException{
		 String sql = "select * from bd_opemr_tempcate cate  where cate.pk_father='"+pkFather+"'";
		 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		  
		 if(null == ps || ps.size() <= 0) return fs;		 
		 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);		
		 
		 for(Map<String,Object> map : ps){
			 BdOpEmrTempCate bot = new BdOpEmrTempCate();
			 BeanUtils.copyProperties(bot, map);	
			 if(StringUtils.isEmpty(pkFather)) pkFather = bot.getPkFather();
			 
			 fs.add(bot);
		 }
		 
		 sql = "select cate.pk_father from bd_opemr_tempcate cate  where cate.pk_tempcate='"+pkFather+"'";
		 
		 pkFather = null;
		 ps = DataBaseHelper.queryForList(sql);
		 
		 if(null == ps || ps.size() <= 0) return fs;
		 pkFather = (String)ps.get(0).get("pkFather");
		 
		 if(StringUtils.isEmpty(pkFather)) return fs;
		 
		 return qryParentCateExec(pkFather,fs);	 
	 }
	 
	 /**
	  * 保存更新门诊病历模板分类
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public void  saveOpEmrCateTemp(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 List<BdOpEmrTempCate> emrs = JsonUtil.readValue(param,new TypeReference<List<BdOpEmrTempCate>>() {});
				 
		 if(null == emrs || emrs.size() <= 0) return;
		 
		 User u = (User)user;
		 
		 for(BdOpEmrTempCate bot : emrs){
			 if(StringUtils.isEmpty(bot.getPkTempcate())){
				 bot.setCreateTime(new Date());
				 bot.setCreator(u.getPkEmp());
			 }else{
				 bot.setModifier(u.getPkEmp());
				 bot.setModityTime(new Date());
			 }
			  
			 bot.setDelFlag("0");	
			 
			 if(StringUtils.isEmpty(bot.getPkTempcate())) DataBaseHelper.insertBean(bot);
			 else{
				 DataBaseHelper.updateBeanByPk(bot, false);
				 if(StringUtils.isEmpty(bot.getPkFather())){
					 String sql = "update bd_opemr_tempcate  set pk_father= null where pk_tempcate='"+bot.getPkTempcate()+"'"; 
					 DataBaseHelper.update(sql, new HashMap<String,Object>());
					 //DataBaseHelper.updateBeanByColumns(bot, new String[]{"pkFather"});
				 }
			 }
		 }		
	 }
	 
	 /**
	  * 查询指定分类下的模板
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public  List<BdOpEmrTempVo>  qryOpEmrTemp(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String cateId = JsonUtil.getFieldValue(param, "cateId");
		 String sql = "select temp.*,diag.diagname as diag_name ,emp.name_emp as author from bd_opemr_temp temp left join BD_TERM_DIAG diag "
		 		+ " on temp.pk_diag=diag.pk_diag inner join BD_OU_EMPLOYEE emp on temp.creator=emp.pk_emp "
		 		+ " where   temp.pk_tempcate='"+cateId+"' order by temp.name";
		 
		 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		 if(null == ps || ps.size() <= 0) return null;
		 
		 List<BdOpEmrTempVo> ret = new ArrayList<BdOpEmrTempVo>();
		 
		 for(Map<String,Object> map : ps){
			 BdOpEmrTempVo boe = new BdOpEmrTempVo();
			 BeanUtils.copyProperties(boe, map);
			 
			 ret.add(boe);
		 }		
		 
		 return ret;	 
	 }
	 
	 /**
	  * 获取实时查询结果
	  * 004003001013
	  * @param param
	  * @param user
	  * @return
	  */
	 public List<bdOpEmrVo> queryListNow(String param,IUser user)  throws IllegalAccessException, InvocationTargetException {
		 BdOpEmrTempCate bdOpEmrTempCate = JsonUtil.readValue(param, BdOpEmrTempCate.class);
		 String sql = "SELECT temp.*,diag.diagname as diag_name,cate.name as cate_Name FROM BD_OPEMR_TEMP temp"+
 			" LEFT JOIN BD_TERM_DIAG diag ON diag.PK_DIAG = temp.PK_DIAG"+
 			" inner  join  BD_OPEMR_TEMPCATE cate on cate.pk_tempcate= temp.pk_tempcate"+
		    " WHERE cate.DEL_FLAG = '0' AND cate.NAME LIKE '%"+ bdOpEmrTempCate.getName()+"%'";
		 String ss = null;
		 if(!StringUtils.isEmpty(bdOpEmrTempCate.getEuRange())){
			  if("0".equals(bdOpEmrTempCate.getEuRange())){   
				 ss = sql + " and cate.pk_org = '"+bdOpEmrTempCate.getPkOrg()+"' and  cate.eu_range = '"+bdOpEmrTempCate.getEuRange()+"'";
			 }
			 
			 if("1".equals(bdOpEmrTempCate.getEuRange())){
				 //AND cate.pk_org = #{pkOrg,jdbcType=VARCHAR} AND cate.pk_dept = #{pkDept,jdbcType=VARCHAR} AND cate.eu_range = #{euRange,jdbcType=VARCHAR}; 
				 ss = sql + " and cate.pk_org = '"+bdOpEmrTempCate.getPkOrg()+"'AND cate.pk_dept = '"+ bdOpEmrTempCate.getPkDept() +"' and  cate.eu_range = '"+bdOpEmrTempCate.getEuRange()+"'";
			 }
			 
			 if("2".equals(bdOpEmrTempCate.getEuRange())){
				 //AND cate.pk_emp = #{pkEmp,jdbcType=VARCHAR} AND cate.eu_range = #{euRange,jdbcType=VARCHAR}   
				 ss = sql + " and cate.pk_emp = '"+bdOpEmrTempCate.getPkEmp()+"' and  cate.eu_range = '"+bdOpEmrTempCate.getEuRange()+"'";
			 }				 
		 }
		 String sqlAll = ss+"AND temp.FLAG_ACTIVE = '1'";
		 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sqlAll);
		 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		 if(null == ps || ps.size() <= 0) return null;
		 
		 List<bdOpEmrVo> ret = new ArrayList<bdOpEmrVo>();
		 
		 for(Map<String,Object> map : ps){
			 bdOpEmrVo boe = new bdOpEmrVo();
			 BeanUtils.copyProperties(boe, map);
			 
			 ret.add(boe);
		 }		
		 
		 return ret;	
		 
	 }
	 
	 
	 /**
	  * 删除模板分类
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * 0未成功，分类下有模板 1成功
	  */
	 public  int  delOpEmrcate(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String cateId = JsonUtil.getFieldValue(param, "cateId");
		 String sql = "select * from bd_opemr_tempcate temp where temp.pk_father='"+cateId+"'";
		 
		 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		 
		 if(ps.size() > 0) return 0;
		 
		 
		 BdOpEmrTempCate bot = new BdOpEmrTempCate();
		 bot.setPkTempcate(cateId);
		 DataBaseHelper.deleteBeanByPk(bot);
		 
		 return 1;	 
	 }
	 
	 /**
	  * 保存更新门诊病历模板
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public BdOpEmrTemp  saveOpEmrTemp(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 List<BdOpEmrTemp> emrs = JsonUtil.readValue(param,new TypeReference<List<BdOpEmrTemp>>() {});
		 
		 if(null == emrs || emrs.size() <= 0) return null;
		 
		 for(BdOpEmrTemp bot : emrs){
			 if(StringUtils.isEmpty(bot.getPkTemp())){
				 bot.setCreateTime(new Date());
				 //bot.setCreator(user.get);
			 }else{
				 //bot.setModifier(user.getId());
				 bot.setModityTime(new Date());
			 }
			 
			 		  
			 bot.setDelFlag("0");	
			 if(StringUtils.isEmpty(bot.getPkTemp())){
				 DataBaseHelper.insertBean(bot);
				 return bot;
			 }
			 else{
				 DataBaseHelper.updateBeanByPk(bot, false);
				 return bot;
			 }
		 }
		 
		 return null;
	 }
	 
	 /**
	  * 删除模板分类
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * 0未成功，分类下有模板 1成功
	  */
	 public  void  delOpEmrTemp(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String tId = JsonUtil.getFieldValue(param, "tid");
		 		 
		 BdOpEmrTemp bot = new BdOpEmrTemp();
		 bot.setPkTemp(tId);
		 DataBaseHelper.deleteBeanByPk(bot);
	 }

	/**
	 * 查询当前用户的门诊模板分类以及模板详细
	 * @param param
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<BdOpEmrTempCateVo> qryTempCateDetail(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		String euRange = JsonUtil.getFieldValue(param, "euRange");
		String val = JsonUtil.getFieldValue(param, "val");
		String sql = "select * from bd_opemr_tempcate cate ";
		List<BdOpEmrTempCate> bdopemrList = new ArrayList<BdOpEmrTempCate>();
		List<BdOpEmrTempCateVo> bdopemrList1 = new ArrayList<BdOpEmrTempCateVo>();
		//List<BdOpEmrTempCateVo> bdopemrList2 = new ArrayList<BdOpEmrTempCateVo>();
		if(StringUtils.isEmpty(euRange)){
			bdopemrList = execQryTempCate(sql);
		}

		if(StringUtils.isEmpty(val)){
			bdopemrList = execQryTempCate(sql);
		}

		if("0".equals(euRange)){
			sql += " where cate.pk_org='"+val+"' and  cate.eu_range=0 order by cate.name";
			bdopemrList = execQryTempCate(sql);
		}

		if("1".equals(euRange)){
			sql += " where cate.pk_dept='"+val+"' and cate.eu_range=1 order by cate.name";
			bdopemrList = execQryTempCate(sql);
		}

		if("2".equals(euRange)){
			sql += " where cate.pk_emp='"+val+"' and cate.eu_range=2 order by cate.name";
			bdopemrList = execQryTempCate(sql);
		}

		//BeanUtils.copyProperties(bdopemrList1, bdopemrList);
		if(bdopemrList!=null&&bdopemrList.size()>0){
			for (BdOpEmrTempCate bdop:bdopemrList){
				BdOpEmrTempCateVo bdopvo = new BdOpEmrTempCateVo();
				BeanUtils.copyProperties(bdopvo, bdop);
				Map<String,Object> paramMap = new HashMap<>();
				paramMap.put("cateId", bdop.getPkTempcate());
				String strJson = JsonUtil.writeValueAsString(paramMap);
				List<BdOpEmrTempVo> bdopemttemp= qryOpEmrTemp(strJson,user);
				bdopvo.setBdOpEmrTempVo(bdopemttemp);
				bdopemrList1.add(bdopvo);
			}
			return bdopemrList1;
		}else {
			return null;
			}
	}
}
