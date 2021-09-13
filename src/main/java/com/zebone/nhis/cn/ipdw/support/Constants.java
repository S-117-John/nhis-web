package com.zebone.nhis.cn.ipdw.support;

public class Constants {

	public static final String TRUE = "1";
	public static final String FALSE = "0";
	public static final String RIS_SRVTYPE = "02" ; //检查类型服务
	public static final String LIS_SRVTYPE = "03" ; //检验类型服务
	public static final String OP_SRVTYPE = "04" ; //检验类型服务
	
	public static final String RT_NEW = "N";		//数据状态：新建
	public static final String RT_UPDATE = "U";		//数据状态：更新
	public static final String RT_REMOVE = "R";		//数据状态：删除
	public static final String RT_LOAD = "L";		//数据状态：装载
	
	public static final String RT_NEW_ZR = "-1";			//中山二院平台使用  数据状态：新建
	public static final String RT_UPDATE_ZR = "1";		//中山二院平台使用数据状态：更新
	public static final String RT_REMOVE_ZR = "2";		//中山二院平台使用数据状态：删除
	public static final String RT_STOP_ZR = "3";		//中山二院平台使用数据状态：停医嘱/取消停医嘱
	
	public static final String TR_EMP = "EMP";		//平台数据字段转换--人员
	public static final String TR_DEPT = "DEPT";	//平台数据字段转换--科室
	public static final String TR_DIAG = "DIAG";	//平台数据字段转换--诊断
	public static final boolean FLAG_TROWS = false;  //平台字段与NHIS字段未对应是否抛出异常
	
}
