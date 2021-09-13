package com.zebone.nhis.common.support;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.handler.ApplicationHandler;
import com.zebone.nhis.common.module.BaseModule;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.service.ApplicationService;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.exception.JonException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.platform.web.support.ResponseJson;
import com.zebone.rpc.Rpc;

public class ApplicationUtils {
	
	static Logger logger = LogManager.getLogger(ApplicationUtils.class.getName());
	
	private static String frameworkType;
	
	private static ApplicationService applicationService;
	
	private static ApplicationHandler applicationHandler;
	
	/**
	 * 根据name获取etc/config下properties中 值 
	 * @param name
	 * @param dv      如果值为空返回 dv
	 * @return
	 */
    public static String getPropertyValue(String name ,String dv){
    	return Application.getApplicationSettings().getProperty(name,dv);
    }
	
	/**
	 * 根据编码规则获取各类编码
	 * @param code  
	 * @return
	 */
	public static String getCode(String code){
		return getApplicationHandler().operEncod(code);
	}
	
	
	/**
	 * 批量获取 根据编码规则获取各类编码
	 * @param code  
	 * @param size  
	 * @return   一次性获取多条编码
	 */
	public static String[] getCode(String code,int size){
		return getApplicationHandler().operEncod(code,size);
	}
	
	
	/**
	 * 查找系统参数code获取 系统参数值  
	 * @param code
	 * @param isPub  是否全部参数
	 * @return
	 */
	public static String getSysparam(String code , boolean isPub){
		return getApplicationService().getSysparam(code,isPub);
	}

	/**
	 * 查找系统参数code获取 系统参数值  
	 * @param code
	 * @param isPub  是否全部参数
	 * @param msg 未获取到参数的提示
	 * @return
	 */
	public static String getSysparam(String code , boolean isPub , String msg){
		String param = getApplicationService().getSysparam(code,isPub);
		if(StringUtils.isEmpty(param))
			throw new BusException(msg);
		return param;
	}
	/**
	 * 查找系统参数code获取 系统参数值  
	 * @param code isPub  是否全部参数 msg 未获取到参数的提示
	 * @return
	 */
	public static String getDeptSysparam(String code ,String pkDept){
		return  getApplicationService().getDeptSysparam(code,pkDept);
	}

	public static ApplicationService getApplicationService(){
		
		if(applicationService == null)
			applicationService =  ServiceLocator.getInstance().getBean("applicationService", ApplicationService.class);
			
		return applicationService;
	}
	
	
	public static ApplicationHandler getApplicationHandler(){
		
		if(applicationHandler == null)
			applicationHandler =  ServiceLocator.getInstance().getBean("applicationHandler", ApplicationHandler.class);
			
		return applicationHandler;
	}
	
	
	/**
	 * 设置公共属性值
	 * @param bean
	 * @param isInsert 判断数据操作    true：新增  false：修改   如果是修改的话  创建人、创建时间、机构、删除标志不需要重新设值 
	 */
	public static void setBeanComProperty(BaseModule bean , boolean isInsert){

		if(isInsert){  // 修改时不需要重新设值
			bean.setDelFlag("0");
			bean.setCreateTime(new Date());
			bean.setCreator(UserContext.getUser().getPkEmp());
			bean.setPkOrg(UserContext.getUser().getPkOrg());
		}		
		bean.setTs(new Date());
		bean.setModifier(UserContext.getUser().getPkEmp());
	}


	/**
	 * NHIS 设置持久层对象PO模型的默认值字段（针对后台使用batchUpdate方法时需要自己手动设置默认值，用到的地方较多，故产生此方法）
	 * @param obj设置属性的对象
	 * @param flag   判断数据操作    true：新增  false：修改   如果是修改的话  创建人、创建时间、机构、删除标志不需要重新设值 
	 */
	/**
	 * NHIS 持久层PO默认值字段
	 */

	public static void setDefaultValue(Object obj, boolean flag) {
		
		User user = UserContext.getUser();

		Map<String,Object> default_v = new HashMap<String,Object>();
		if(flag){  // 如果新增
			default_v.put("pkOrg", user.getPkOrg());
			default_v.put("creator", user.getPkEmp());
			default_v.put("createTime",new Date());
			default_v.put("delFlag", "0");
		}
		
		default_v.put("ts", new Date());
		default_v.put("modifier",  user.getPkEmp());
		
		Set<String> keys = default_v.keySet();
		
		for(String key : keys){
			Field field = ReflectHelper.getTargetField(obj.getClass(), key);
			if (field != null) {
				ReflectHelper.setFieldValue(obj, key, default_v.get(key));
			}
		}
	
		if (flag) { 
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				PK pKAnnotation = field.getAnnotation(PK.class);
				if (pKAnnotation != null) {
					ReflectHelper.setFieldValue(obj, field.getName(), NHISUUID.getKeyId());
					break;
				}
			}
		}
	}
	/**
	 * 复制属性
	 * @param dest
	 * @param orig
	 */
	public static void copyProperties(Object dest,Object orig){
		try {
			PropertyUtils.copyProperties(dest, orig);
		} catch (IllegalAccessException e) {
			throw new BusException("复制属性异常");
		} catch (InvocationTargetException e) {
			throw new BusException("复制属性异常");
		} catch (NoSuchMethodException e) {
			throw new BusException("复制属性异常");
		}
	}
	/**
	 * map 转 bean
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	 public static <T> T mapToBean(Map<String, Object> map, Class<?> beanClass) throws Exception {    
	        if (map == null)  
	            return null;     
	         T  bean = (T) beanClass.newInstance();  
	            BeanUtils.populate(bean, map);  
	        return bean;  
	}  
	 /**
	  * bean 转 Map
	  * @param obj
	  * @return
	  */
	 public static Map<?, ?> beanToMap(Object obj) {  
	    if(obj == null)   return null; 
	     return new BeanMap(obj);  
	 }  
	 
	 
	/**
	 * 根据部署方式调用服务
	 * 如果集中部署 ，直接调用具体java服务
	 * 如果是dubbo部署，通过代理调用dubbo服务
	 * 
	 * @param proxname  代理名称
	 * @param service   服务名称
	 * @param method    方法名称
	 * @param paramo    参数对象
	 * @param user      用户信息
	 * @return
	 */
	public static ResponseJson  execService(String proxname , String service , String method , Object paramo,IUser user){
		if(frameworkType == null){
			frameworkType = Application.getApplicationSettings().getProperty("framework.type");
		}
		ResponseJson rjson  = new ResponseJson();
		DataOption actionoption = new DataOption();
		actionoption.setType("java");
		actionoption.setJclass(service);
		actionoption.setJmethod(method);
		
		try{
			String param = JsonUtil.writeValueAsString(paramo);
		
			if(frameworkType != null && frameworkType.equals("2") ){
				String rpcname = "W"+proxname+"60";
				actionoption.setIsdubbo(true);
				Rpc rcp = (Rpc) ServiceLocator.getInstance().getBean(rpcname.toUpperCase());
				actionoption.setSql(rpcname);
			
				String str =  rcp.handel(actionoption, param, user);
				
			     rjson = JsonUtil.readValue(str, ResponseJson.class);
				return  rjson;
				
			}else{
				actionoption.setIsdubbo(false);
				actionoption.setSql("java:"+service+"."+method);
			    rjson =(ResponseJson) DataSupport.doJava(actionoption,param,user);
				return  rjson;
			}
		} 
		catch(JonException e){
			logger.error(e);
			rjson.setStatus(-60);
			rjson.setDesc("参数格式不符合规范！");
			return  rjson;
		}catch(RpcException e){
			if(e.isTimeout()){
				rjson.setStatus(-50);
				rjson.setDesc("服务超时,请联系管理员或者刷新数据!");
			}else if(e.isNetwork()){
				rjson.setStatus(-102);
				rjson.setDesc("网络异常,请联系管理员!");
			}else if(e.isForbidded()){
				rjson.setStatus(-50);
				rjson.setDesc("服务禁止访问,请联系管理员!");
			}else{
				rjson.setStatus(-50);
				rjson.setDesc("Dubbo服务异常！");
				rjson.setErrorMessage(e.getMessage()+"   "+DataSupport.getErrorMessage(e));
			}
			logger.error(e);
			return  rjson;
		}
		catch(Exception e){
			logger.error(e);
			rjson.setStatus(-62);
			rjson.setDesc("服务处理失败！");
			rjson.setErrorMessage(e.getMessage()+"   "+DataSupport.getErrorMessage(e));
			return  rjson;
		}

	}
	/**
	 * 对象转json
	 * @param obj
	 * @return
	 */
	 public static String objectToJson(Object obj){
		 if(obj==null) return null;
		 return JSON.toJSONString(obj);
	 }
	 /**
	  * 获取计算机IP地址
	  * @return
	  */
	 public static String getCurrIp(){
		 try {
			return InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			throw new BusException("获取当前计算机IP异常！");
		} //获取本机ip  
	 }
	 /**
	  * 获取计算机名
	  * @return
	  */
	 public static String getCurrHostName(){
         try {
			return InetAddress.getLocalHost().getHostName().toString().toUpperCase();
		} catch (UnknownHostException e) {
			throw new BusException("获取当前计算机名异常！");
		} 
	 }
	 
	 /**
	  * 对象转json
	  * @param obj
	  * @return
	  */
	 public static String beanToJson(Object obj){
		 if(obj==null) return null;
		 return JSON.toJSONString(obj);
	 }
	 
	 /**
	  *功能描述：对象转json（带时间格式转换）
	  * @author wuqiang
	  * @param formate 时间格式 示例："yyyyMMddHHmmss"
	  * @date 2019/3/24
	  */
	public static String objectToJsonWithTimeFormate(Object obj,String formate){

			if(obj==null) return null;
			return  JSON.toJSONStringWithDateFormat(obj,formate);
		}

	/**
	 * 根据日期获取格式化年龄(使用数据库函数，兼容oracle、sqlserver)
	 * @param birth
	 * @param dateBegin
	 * @return
	 */
	public static String getAgeFormat(Date birth,Date dateBegin){
		if(birth == null) {
			return null;
		}
		return getAgeFormat(DateFormatUtils.format(birth, "yyyyMMddHHmmss"),dateBegin==null?null:DateFormatUtils.format(dateBegin, "yyyyMMddHHmmss"));
	}

	/**
	 * @param birth (yyyymmddhh24miss格式日期）
	 * @param dateBegin
	 * @return
	 */
	public static String getAgeFormat(String birth,String dateBegin){
		if(StringUtils.isBlank(birth)) {
			return null;
		}
		return MapUtils.getString(DataBaseHelper.queryForMap("select GETPVAGE(to_date(?,'yyyymmddhh24miss'),to_date(?,'yyyymmddhh24miss')) age_format" + (Application.isSqlServer() ? "" : " from dual")
				, new Object[]{birth,dateBegin}), "ageFormat");
	}

	/**
	 * 依据参数配置，获取门诊有效时间<br>
	 *     使用session中机构获取参数
	 * @param dateBegin
	 * @return
	 */
	public static Date getPvDateEnd(Date dateBegin){
		String key = SysConstant.SYS_PARAM_PV0003;
		String val = getApplicationService().getSysparam(key, false);
		if (val == null) {
			throw new BusException("未在本机构下发布或维护系统参数【" + key + "】");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateBegin);
		if(val.toUpperCase().startsWith("H")){
			if(!NumberUtils.isNumber(val = val.substring(1))){
				throw new BusException("系统参数【" + key + "】格式配置错误");
			}
			cal.add(Calendar.HOUR, NumberUtils.toInt(val));
		} else {
			cal.add(Calendar.DATE, Integer.valueOf(val));
			cal.add(Calendar.DATE, -1);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		return cal.getTime();
	}

	/**
	 * 依据参数配置，获取门诊有效时间<br>
	 *     使用session中机构获取参数
	 * @param dateEnd
	 * @return
	 */
	public static Date getPvDateBegin(Date dateEnd){
		String key = SysConstant.SYS_PARAM_PV0003;
		String val = getApplicationService().getSysparam(key, false);
		if (val == null) {
			throw new BusException("未在本机构下发布或维护系统参数【" + key + "】");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateEnd);
		if(val.toUpperCase().startsWith("H")){
			if(!NumberUtils.isNumber(val = val.substring(1))){
				throw new BusException("系统参数【" + key + "】格式配置错误");
			}
			cal.add(Calendar.HOUR, -NumberUtils.toInt(val));
		} else {
			cal.add(Calendar.DATE, -Integer.valueOf(val));
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		return cal.getTime();
	}
	/**
	 * 依据参数配置，获取门诊有效时间<br>
	 *     使用session中机构获取参数
	 * @param dateBegin
	 * @return
	 */
	public static Date getPvDateEndEr(Date dateBegin){
		String key = SysConstant.SYS_PARAM_PV0004;
		String val = getApplicationService().getSysparam(key, false);
		if (val == null) {
			throw new BusException("未在本机构下发布或维护系统参数【" + key + "】");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateBegin);
		if(val.toUpperCase().startsWith("H")){
			if(!NumberUtils.isNumber(val = val.substring(1))){
				throw new BusException("系统参数【" + key + "】格式配置错误");
			}
			cal.add(Calendar.HOUR, NumberUtils.toInt(val));
		} else {
			cal.add(Calendar.DATE, Integer.valueOf(val));
			cal.add(Calendar.DATE, -1);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		return cal.getTime();
	}
	/**
	 * 依据参数配置，获取门诊有效时间<br>
	 *     使用session中机构获取参数
	 * @param dateEnd
	 * @return
	 */
	public static Date getPvDateBeginEr(Date dateEnd){
		String key = SysConstant.SYS_PARAM_PV0004;
		String val = getApplicationService().getSysparam(key, false);
		if (val == null) {
			throw new BusException("未在本机构下发布或维护系统参数【" + key + "】");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateEnd);
		if(val.toUpperCase().startsWith("H")){
			if(!NumberUtils.isNumber(val = val.substring(1))){
				throw new BusException("系统参数【" + key + "】格式配置错误");
			}
			cal.add(Calendar.HOUR, -NumberUtils.toInt(val));
		} else {
			cal.add(Calendar.DATE, -Integer.valueOf(val));
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		return cal.getTime();
	}
	/**
	 * 根据表名，字段名获取序号
	 * @param nameFd 表名
	 * @param nameTb  字段名
	 * @param count 取号数
	 * @author yangxue
	 */
	public static int getSerialNo(String nameTb,String nameFd,int count){
		return getApplicationHandler().operSerialno(nameTb,nameFd,count);
	}

}
