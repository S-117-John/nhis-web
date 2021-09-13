package com.zebone.nhis.common.module.mybatis;

import com.zebone.platform.Application;
import com.zebone.platform.modules.dao.dialect.Dialect;
import com.zebone.platform.modules.dao.dialect.spi.OracleDialect;
import com.zebone.platform.modules.dao.dialect.spi.SQLServerDialect;
import com.zebone.platform.modules.dao.support.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Intercepts( { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class }) })
public class MyBatisPage implements Interceptor {
	private Properties properties;
	
	public static final ThreadLocal<PageInfo> localPage = new ThreadLocal<PageInfo>();
	public static final ThreadLocal<Page> endPage = new ThreadLocal<Page>();
	
	// 日志对象
	private static Log log = LogFactory.getLog(MyBatisPage.class);

	public Object intercept(Invocation invocation) throws Throwable {
		
		if(localPage.get() == null){
			return invocation.proceed();
		}
		PageInfo page = localPage.get();
		
		this.clear();
		
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);


		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
	
		Dialect dialect = null;
		
		if(Application.isSqlServer()){
			dialect = new SQLServerDialect();
		}else{
			dialect = new OracleDialect();
		}
		
		int startIndex = Page.getStartOfPage(page.getPageNum(), page.getPageSize());

		String page_sql = dialect.getLimitString(originalSql, startIndex, startIndex +  page.getPageSize());
		
		metaStatementHandler.setValue("delegate.boundSql.sql", page_sql);
		metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
		metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		
		BoundSql boundSql = statementHandler.getBoundSql();
		
	    
		int dataCount = 0;
		if(page.isHasCount()){
		
			Connection connection = (Connection) invocation.getArgs()[0];  
			
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
			dataCount = setPageParameter(dialect.getCountString(originalSql),connection,mappedStatement,boundSql,page);
			
			endPage.set(new Page(startIndex,dataCount,page.getPageSize(),null));
		}
		
		return invocation.proceed();
	}
	
	
	
	private int setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
                                 BoundSql boundSql, PageInfo page) {
		
			PreparedStatement countStmt = null;  
			ResultSet rs = null;  
			try {  
				countStmt = connection.prepareStatement(sql);  
				BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), sql,
						boundSql.getParameterMappings(), boundSql.getParameterObject());  
				setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());  
				rs = countStmt.executeQuery();  
				int totalCount = 0;  
				if (rs.next()) {  
					totalCount = rs.getInt(1);  
				}  
				return totalCount;
			} catch (SQLException e) {  
				log.error("Mybiate分页处理异常："+sql);
				return 0;
			} finally {  
				try {  
					rs.close();  
				} catch (SQLException e) {  }  
				try {  
					countStmt.close();  
				} catch (SQLException e) {  }  
			}  
			
}  
	
	
	  /** 
     * 代入参数值 
     * @param ps 
     * @param mappedStatement 
     * @param boundSql 
     * @param parameterObject 
     * @throws SQLException 
     */  
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {  
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);  
    }  
  
    
    public static Page getPage(){
    	return endPage.get();
    }
	
	/** 
     * 开始分页 
     * @param pageNum 
     * @param pageSize 
     */  
    public static void startPage(int pageNum, int pageSize) { 
        localPage.set(new PageInfo(pageNum,pageSize));
    }  
    
	/** 
     * 开始分页 
     * @param pageNum 
     * @param pageSize 
     */  
    public static void startPage(int pageNum, int pageSize,boolean haspage) { 
        localPage.set(new PageInfo(pageNum,pageSize,haspage));
    }  


	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
   
	
	public void clear(){
		localPage.remove();
		localPage.set(null);
		endPage.remove();
		endPage.set(null);
	}

}

class PageInfo{
	private int pageSize;
	private int pageNum;
	private boolean hasCount = true;
	public PageInfo(int pageNum, int pageSize,boolean hasCount){
		this.pageSize = pageSize;
		this.pageNum = pageNum;
		this.hasCount = hasCount;
	}
	
	public boolean isHasCount() {
		return hasCount;
	}

	public void setHasCount(boolean hasCount) {
		this.hasCount = hasCount;
	}

	public PageInfo(int pageNum, int pageSize){
		this.pageSize = pageSize;
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
}
