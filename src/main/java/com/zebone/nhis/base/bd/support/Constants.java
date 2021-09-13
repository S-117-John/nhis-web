package com.zebone.nhis.base.bd.support;

public class Constants {

	/**资源类型--部门*/
	public static final int euRestype_dept = 0;
	/**资源类型--人员*/
	public static final int euRestype_emp = 1;
	/**资源类型--手术台*/
	public static final int euRestype_opt = 2;
	/**资源类型--床位*/
	public static final int euRestype_bed = 3;
	/**资源类型-- 医技*/
	public static final int euRestype_msp = 4;
	
	
	//医疗组状态: 0:保存未提交 1:提交 2:审批结束 8:发布或不使用审批流
    public static final String EU_STATUS_START = "0";// 未提交(发起节点) 
    public static final String EU_STATUS_SUBMIT  = "1";// 提交(提交后处于审批流中)
    public static final String EU_STATUS_END= "2";// 审批结束(终审)
    public static final String EU_STATUS_PUBLISH= "8";// 发布或不使用审批流
    
    //审批流状态: 0:未审批 1:审批通过 9:退回 
    public static final String FLOW_STATUS_WAIT = "0";// 未审批
    public static final String FLOW_STATUS_PASS  = "1";// 审批通过
    public static final String FLOW_STATUS_BACK= "9";// 退回
	
	
	
}
