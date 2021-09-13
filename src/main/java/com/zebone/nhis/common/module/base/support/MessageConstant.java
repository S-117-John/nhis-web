package com.zebone.nhis.common.module.base.support;

/***
 *消息常量
 */
public class MessageConstant {
	
	/** 接收范围：1-全员  */
	public static final String EU_SCOPE_ALL = "1";
	
	/** 接收范围：2-科室  */
	public static final String EU_SCOPE_DEPT = "2";
	
	/** 接收范围：3-指定人员  */
	public static final String EU_SCOPE_STAFF = "3";
	
	/** 发送类型:1-员工  */
	public static final String EU_TARGET_TYPE_STAFF = "1";
	
	/** 发送类型:2-部门  */
	public static final String EU_TARGET_TYPE_DEPT = "2";
	
	/** 发送类型:3-患者  */
	public static final String EU_TARGET_TYPE_PI = "3";
	
	/** 发送类型:9-IP */
	public static final String EU_TARGET_TYPE_IP = "9";
	
	/** 发送状态 0-未发送 */
	public static final String EU_SEND_STATUS_NO = "0";
	
	/** 发送状态 1-发送成功 */
	public static final String EU_SEND_STATUS_SUCCESS = "1";
	
	/** 发送状态 2-发送失败 */
	public static final String EU_SEND_STATUS_FAIL = "2";
	
	/** 处理状态 0-新消息 */
	public static final String EU_HANDLE_STATUS_NEW = "0";
	
	/** 处理状态  1-消息已缓存 */
	public static final String EU_HANDLE_STATUS_CACHE = "1";
	
	/** 处理状态 2-草稿 */
	public static final String EU_HANDLE_STATUS_MANUSCRIPT = "2";
	
	/** 处理状态 3-已接收 */
	public static final String EU_HANDLE_STATUS_RECEIVE = "3";

}
