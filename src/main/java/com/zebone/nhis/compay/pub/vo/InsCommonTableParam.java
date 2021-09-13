package com.zebone.nhis.compay.pub.vo;

import java.util.List;

/**
 * 通用医保表参数
 */
public class InsCommonTableParam {

	private String tableName; //"需要新增的表名",
	private String primaryKey; //"主键字段列名",
	private String uniqueKey; //"唯一键，例如中心编码列名，用于update操作",
	private List<String> dataColumn; //["column1","column2",{}] 有效的列名集合（排除了主键和 通用的列）
	private List<List<String>> dataValue; //[{"value1","value2"},{},{}]有效列的值集合
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public List<String> getDataColumn() {
		return dataColumn;
	}
	public void setDataColumn(List<String> dataColumn) {
		this.dataColumn = dataColumn;
	}
	public List<List<String>> getDataValue() {
		return dataValue;
	}
	public void setDataValue(List<List<String>> dataValue) {
		this.dataValue = dataValue;
	}
	
}
