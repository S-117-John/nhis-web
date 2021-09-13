package com.zebone.nhis.scm.pub.support;

public class Constants {
	/**仓库物品被引用的业务类型--计划*/
	public static final int TYPE_PD_PLAN_DETAIL = 1;
	/**仓库物品被引用的业务类型--出入库*/
	public static final int TYPE_PD_ST_DETAIL = 2;
	/**仓库物品被引用的业务类型--库存*/
	public static final int TYPE_PD_STOCK = 3;
	/**仓库物品被引用的业务类型--盘点*/
	public static final int TYPE_PD_INV_DETAIL = 4;
	/**仓库物品被引用的业务类型--结账*/
	public static final int TYPE_PD_CC_DETAIL = 5;
	
	/**药品字典维护处包装规格--允许操作*/
	public static final String OPTFLAG_ZERO = "0";
	/**药品字典维护处包装规格--不允许删除*/
	public static final String OPTFLAG_ONE = "1";
	/**药品字典维护处包装规格--不允许修改*/
	public static final String OPTFLAG_TWO = "2";
}
