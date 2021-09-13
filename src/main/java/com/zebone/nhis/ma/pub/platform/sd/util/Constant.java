package com.zebone.nhis.ma.pub.platform.sd.util;

import com.zebone.nhis.common.support.ApplicationUtils;

/**
 * 常量参数
 * @author JesusM
 *
 */
public  class Constant {
	
	static{
		PKINSUSELF=ApplicationUtils.getPropertyValue("msg.constant.pkInsu", "eb2baf4e9848486984516fe6a84d6136");
		PKORG=ApplicationUtils.getPropertyValue("msg.constant.pkOrg", "a41476368e2943f48c32d0cb1179dab8");
		NAMEORG=ApplicationUtils.getPropertyValue("msg.constant.nameOrg", "深圳大学总医院");
		CODEORG=ApplicationUtils.getPropertyValue("msg.constant.codeOrg", "H8880");
		NOTE=ApplicationUtils.getPropertyValue("msg.constant.note", "接口数据");
		PKDEPT=ApplicationUtils.getPropertyValue("msg.constant.pkDept", "36aa3afb1d3347c8be340ee3cc12ef83");
		NAMEZZJ=ApplicationUtils.getPropertyValue("msg.constant.nameZzj", "自助机2");
		PKZZJ=ApplicationUtils.getPropertyValue("msg.constant.pkEmpZjj", "a19d7dc6120b4b04bcdc1b9747b0e7ea");
		NAMEWECHAT=ApplicationUtils.getPropertyValue("msg.constant.nameWechat", "微信收费员");
		PKWECHAT=ApplicationUtils.getPropertyValue("msg.constant.pkEmpWeChat", "569e61e4ea42443cb0fa72b62494e3da");
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
