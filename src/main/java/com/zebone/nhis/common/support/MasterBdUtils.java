package com.zebone.nhis.common.support;

/**
 * 基础数据帮助类,与瑞联平台对接时将基础码表的codeDefDocList字段转换成平台识别的编码
 * @author c
 *
 */
public class MasterBdUtils {
	
	/**
	 * 转换基础数据字典分类编码
	 * @param type
	 * @return
	 */
	public static String conBdDefdocType(String type){
		
		String codeDefdoclist = "";
		
		switch (type) {
			case "1":
				codeDefdoclist = "000000";		//性别
				break;
			case "2":
				codeDefdoclist = "000009";		//国籍
				break;
			case "3":
				codeDefdoclist = "000007";		//证件类型
				break;
			case "4":
				codeDefdoclist = "000006";		//婚姻状况
				break;
			case "5":
				codeDefdoclist = "000010";		//职业
				break;
			case "6":
				codeDefdoclist = "000003";		//民族
				break;
			case "7":
				codeDefdoclist = "000013";		//联系人关系
				break;
			case "8":
				codeDefdoclist = "010301";		//职称
				break;
//			case "9":
//				codeDefdoclist = "010300";		//人员类型
//				break;
			case "10":
				codeDefdoclist = "030300";		//麻醉方法
				break;
			case "11":
				codeDefdoclist = "030306";		//切开愈合等级
				break;
			default:
				break;
		}
		
		return codeDefdoclist;
	}
	
	/**
	 * 转换分类编码为基础数据字典
	 * @param type
	 * @return
	 */
	public static String typeToCodeDefdoc(String codeDefdoc){
		
		String codeDefdoclist = "";
		
		switch (codeDefdoc) {
			case "bdUnit":
				codeDefdoclist = "12";		//计量单位
				break;
			case "bdItemcate":
				codeDefdoclist = "13";	//收费项目分类
				break;
			case "030400":
				codeDefdoclist = "14";		//药品剂型
				break;
			case "bdFactory":
				codeDefdoclist = "15";	//厂家
				break;
			case "16":
				codeDefdoclist = "";			//医嘱频次(未确定)
				break;
			case "bdSupplyer":
				codeDefdoclist = "17";			//供货单位
				break;
			case "bdAdminDivision":
				codeDefdoclist = "18";		//行政区划
				break;
			case "030402":
				codeDefdoclist = "19";		//药理分类
				break;
			case "030403":
				codeDefdoclist = "20";		//毒麻分类
				break;
			case "030406":
				codeDefdoclist = "21";		//抗生素分类
				break;
			case "030800":
				codeDefdoclist = "22";		//病案费用分类
				break;
			case "030200":
				codeDefdoclist = "23";		//检验标本类型
				break;
			case "030100":
				codeDefdoclist = "24";		//检查类型
				break;
			case "120100":
				codeDefdoclist = "25";		//医嘱类型
				break;
			case "030000":
				codeDefdoclist = "26";		//医嘱分类
				break;
			case "010305":
				codeDefdoclist = "27";		//医疗权限
				break;
			case "030203":
				codeDefdoclist = "28";		//试管类型
				break;
			case "030101":
				codeDefdoclist = "29";		//检查部位类型
				break;
			default:
				break;
		}
		
		return codeDefdoclist;
	}
	
}
