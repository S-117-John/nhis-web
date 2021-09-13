package com.zebone.nhis.webservice.pskq.utils;

import com.zebone.nhis.common.support.ApplicationUtils;
/**
 * 常量参数
 * 引用深大 待修改
 *
 */
public  class PskqConstant {
	
	static{
		PKINSUSELF=ApplicationUtils.getPropertyValue("msg.constant.pkInsu", "eb2baf4e9848486984516fe6a84d6136");
		PKORG=ApplicationUtils.getPropertyValue("msg.constant.pkOrg", "a41476368e2943f48c32d0cb1179dab8");
		NAMEORG=ApplicationUtils.getPropertyValue("msg.constant.nameOrg", "南方医科大学深圳口腔医院(坪山)");
		CODEORG=ApplicationUtils.getPropertyValue("msg.constant.codeOrg", "H2UD0");
		NOTE=ApplicationUtils.getPropertyValue("msg.constant.note", "接口数据");
		PKDEPT=ApplicationUtils.getPropertyValue("msg.constant.pkDept", "455507d0dd4744c0991a82b50a508d71");
		NAMEZZJ=ApplicationUtils.getPropertyValue("msg.constant.nameZzj", "自助机");
		PKZZJ=ApplicationUtils.getPropertyValue("msg.constant.pkEmpZjj", "183a810a34ff4e50944e1895fd843131");
		NAMEWECHAT=ApplicationUtils.getPropertyValue("msg.constant.nameWechat", "微信公众号");
		PKWECHAT=ApplicationUtils.getPropertyValue("msg.constant.pkEmpWeChat", "335a0ffae31b43888c1998cc05aba22b");
		//PKPI=ApplicationUtils.getPropertyValue("msg.constant.pkPi", "A23E0E4FC7931E01E0530C02000A6424");

	}
	//自费医保主键
	public static String PKINSUSELF ;
	
	//机构
	public static String PKORG ;
	//机构名称
	public static String NAMEORG;
	//医院编码
	public static String CODEORG ;
	
	//备注
	public static String NOTE ;
	
	//门诊计费科室(门诊收费科室)
	public static String PKDEPT ;
	
	//自助机
	public static String NAMEZZJ ;
	public static String PKZZJ ;
	
	//第三方退费预存固定pkpi
	//public static String PKPI;
	
	//微信
	public static String NAMEWECHAT;
	public static String PKWECHAT ;
	
	
	
}