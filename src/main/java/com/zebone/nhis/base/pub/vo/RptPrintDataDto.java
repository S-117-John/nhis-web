package com.zebone.nhis.base.pub.vo;

import java.util.List;
import java.util.Map;

/**
 * 报表打印数据传输层Dto
 * @author Administrator
 * 
 */
public class RptPrintDataDto {

	/********************* 传入数据 **************************************/
	// 报表打印查询数据sql
	private String querySql;

	// 查询数据需要操作sql片段的唯一标识符
	private String operateSqlFlag;

	// 替换sql中占位符的参数
	private Object[] args;

	/********************* 返回数据 **************************************/
	// 表格需要显示的数据
	private List<Map<String, Object>> gridData;

	public String getQuerySql() {

		return querySql;
	}

	public void setQuerySql(String querySql) {

		this.querySql = querySql;
	}

	public String getOperateSqlFlag() {

		return operateSqlFlag;
	}

	public void setOperateSqlFlag(String operateSqlFlag) {

		this.operateSqlFlag = operateSqlFlag;
	}

	public Object[] getArgs() {

		return args;
	}

	public void setArgs(Object[] args) {

		this.args = args;
	}

	public List<Map<String, Object>> getGridData() {

		return gridData;
	}

	public void setGridData(List<Map<String, Object>> gridData) {

		this.gridData = gridData;
	}

}
