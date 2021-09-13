package com.zebone.nhis.common.module.mybatis;

import com.zebone.platform.Application;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.support.SqlDialect;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;


@Intercepts({
		@Signature(type= StatementHandler.class,method="prepare",args={Connection.class, Integer.class})
})
public class TimeStampInterceptor implements Interceptor {
	private Properties properties;
	private String dialect = null;
	private static final Log log = LogFactory.getLog(TimeStampInterceptor.class);
	private String funTime = "sysdate ";
	private String PROP_TS = "TS";
	private String PROP_CREATOR = "CREATOR";
	private String PROP_CREATE_TIME = "CREATE_TIME";
	private String FIELD_CREATE_TIME = "createTime";

	//statementHandler拦截器
	public Object intercept(Invocation invocation) throws Throwable {
		if(dialect == null){
			setDialect(Application.getDatabaseType().replace("Dialect", ""));
		}

		StatementHandler stHandler = (StatementHandler)invocation.getTarget();
		BoundSql boundSql = stHandler.getBoundSql();
		String sql = boundSql.getSql().trim();

		String upcSql = sql.toUpperCase();
		if(upcSql.startsWith("SELECT") || upcSql.startsWith("DELETE")){
			sql = SqlDialect.getSql(sql);
			ReflectHelper.setFieldValue(boundSql, "sql", sql);
		}else{
			Object obj = boundSql.getParameterObject();
			boolean withTs = false;
			boolean noTs = false;
			String keyName = "NOKEY";
			if(obj instanceof MapperMethod.ParamMap){
				@SuppressWarnings("unchecked")
				MapperMethod.ParamMap<Object> mapObjs = (MapperMethod.ParamMap<Object>)obj;
				noTs = mapObjs.containsKey("noTs") ? (Boolean)mapObjs.get("noTs") : false;	//数据库无ts等四个字段
				obj = mapObjs.get("param1");		//更新vo放在第一个参数中
				withTs = mapObjs.containsKey("withTs") ? (Boolean)mapObjs.get("withTs") : false;	//使用ts更新
				keyName = mapObjs.containsKey("keyName") ? (String)mapObjs.get("keyName") : "NOKEY" ;
			}
			if(!noTs){
				if(upcSql.startsWith("UPDATE") ){
					String newSql = this.splitUpdateSql(sql, obj, withTs, keyName);
					newSql = SqlDialect.getSql(newSql);
					ReflectHelper.setFieldValue(boundSql, "sql", newSql);
				}else if(upcSql.startsWith("INSERT") ){
					String newSql = this.splitInsertSql(sql, obj);
					newSql = SqlDialect.getSql(newSql);
					ReflectHelper.setFieldValue(boundSql, "sql", newSql);
				}
				List<ParameterMapping> params = boundSql.getParameterMappings();
				List<ParameterMapping> tmpParams = new ArrayList<ParameterMapping>();
				List<ParameterMapping> tsParams = new ArrayList<ParameterMapping>();
				boolean updateRmTs = upcSql.startsWith("UPDATE") && upcSql.indexOf("TS",upcSql.indexOf("WHERE"))>0;
				if(params != null && params.size()>0 ){
					for(ParameterMapping mp : params){
						if("creator".equals(mp.getProperty()) || "createTime".equals(mp.getProperty())) {
							tmpParams.add(mp);
						}else if( "ts".equals(mp.getProperty())){
							tsParams.add(mp);
						}
					}
					//对于update语句中，where条件使用ts更新的，不能剔除ts
					params.removeAll(updateRmTs?(tsParams.size()>1?tsParams.subList(0,tsParams.size()-1):new ArrayList<>()):tsParams);
					params.removeAll(tmpParams);
					tmpParams = tsParams = null;
				}
			}
			//log.info("-----------boundSql.newSql=="+boundSql.getSql());
		}
		return invocation.proceed();
	}

	/**
	 * 拦截器对应的封装原始对象的方法
	 */
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		this.properties = p;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {

		this.dialect = dialect.toUpperCase();
		if(this.dialect.equals("MYSQL"))
			this.funTime = " now() ";
		else if(this.dialect.equals("SQLSERVER"))
			this.funTime = " getdate() ";
		else
			this.funTime = " sysdate ";

	}

	private String toFieldName(String name){
		StringBuilder result = new StringBuilder();
		if ((name != null) && (name.length() > 0)) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); ++i) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				} else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}

	private String splitInsertSql(String sql, Object param){
		List<String> itemList = new ArrayList<String>();
		String[] items = sql.split("\\,|\\(|\\)");
		int cnt = 0;
		for(int i=0; i<items.length; i++){
			items[i] = items[i].trim();
			if(items[i].equalsIgnoreCase(PROP_CREATOR) ||
					items[i].equalsIgnoreCase(PROP_CREATE_TIME) ||
					items[i].equalsIgnoreCase(PROP_TS) ){
				cnt ++;
				continue;
			}
			if(items[i].equals("?") && cnt>0){
				cnt--;
				continue;
			}
			itemList.add(items[i]);
		}
		StringBuffer buf = new StringBuffer();
		buf.append(itemList.get(0)+" ( ");
		for(int i=1; i<itemList.size(); i++){
			if(!itemList.get(i).equalsIgnoreCase("VALUES")){
				buf.append(itemList.get(i));
				buf.append( ", ");
			}else{
				buf.append(PROP_TS +", ");
				buf.append(PROP_CREATOR+", ");
				buf.append(PROP_CREATE_TIME+" ) VALUES( ");
			}
		}
		String createTime = this.funTime;
		Date dt = null;
		if(param!=null)
			dt = (Date) ReflectHelper.getFieldValue(param , FIELD_CREATE_TIME);
		if(dt!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			createTime = "'"+sdf.format(dt)+"'";
			if(this.dialect.equals("ORACLE"))
				createTime = "to_date("+createTime+", 'yyyy-MM-dd hh24:mi:ss') ";
		}
		String creator = null;
		if(param!=null)
			creator = (String) ReflectHelper.getFieldValue(param , PROP_CREATOR.toLowerCase());
		buf.append(this.funTime + ", ");
		buf.append("'" + (creator==null ? UserContext.getUser().getPkEmp() : creator )+ "', ");
		buf.append(createTime + " ) ");
		return buf.toString();
	}

	@SuppressWarnings("unchecked")
	private String splitUpdateSql(String sql, Object voParam, boolean withTs, String keyName){
		List<Object> voList = new ArrayList<Object>();
		if(voParam != null){
			if(voParam instanceof List){
				voList = (List<Object>)voParam;
			}else{
				voList.add(voParam);
			}
		}
		keyName = this.toFieldName(keyName).toUpperCase();
		StringBuffer buf = new StringBuffer();
		Pattern p = Pattern.compile("\\bSET\\b|\\bWHERE\\b" ,Pattern.CASE_INSENSITIVE);
		String[] parts = p.split(sql);
		buf.append(parts[0]);
		buf.append(" SET ");
		buf.append(PROP_TS+"="+this.funTime+",");
		String[] items = parts[1].split(",");
		for(int i=0; i<items.length; i++){
			String tm = StringUtils.trimToEmpty(items[i]).replaceAll("\\r\\n","").toUpperCase();
			if(StringUtils.isBlank(tm) || tm.startsWith(PROP_CREATOR) ||
					tm.startsWith(PROP_CREATE_TIME) ||
					tm.startsWith(PROP_TS) ){
				continue;
			}
			buf.append(tm).append(",");
		}
		buf.replace(buf.lastIndexOf(","),buf.lastIndexOf(",")+1," ");
		if(parts.length>2){
			buf.append(" WHERE ");
			if(withTs){
				String[] wheres = (parts[2]+" ").split(keyName+"\\s*(=)\\s*\\?");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000");
				for(int i=0; i<wheres.length; i++){
					if(i==0)
						buf.append(wheres[i]);
					else{
						Date dt = (Date) ReflectHelper.getFieldValue(voList.get(i-1) , PROP_TS.toLowerCase() );
						if(this.dialect.equals("ORACLE")){
							buf.append(" (TO_CHAR("+PROP_TS);
							buf.append(",'yyyy-MM-dd hh24:mi:ss.ff')");
						}else{
							buf.append(" ("+PROP_TS);
						}
						buf.append("='"+sdf.format(dt)+"' and ");
						buf.append(keyName +"=?) ");
						buf.append(wheres[i]);
					}
				}
			}else{
				buf.append(parts[2]);
			}
			if(parts.length>3){
				for(int i=3;i<parts.length;i++){
					buf.append(" where ").append(parts[i]);
				}
			}
		}
		return buf.toString();
	}

}