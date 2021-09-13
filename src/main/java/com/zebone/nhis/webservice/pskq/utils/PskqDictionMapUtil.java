package com.zebone.nhis.webservice.pskq.utils;


import java.util.Map;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * 处理坪山口腔医院平台与HIS之间数据字典映射工具类
 */
public class PskqDictionMapUtil {

    static{
        PKORG=ApplicationUtils.getPropertyValue("msg.constant.pkOrg", "6ee27846343c4776abea7e30b7b96d94");
    }
    //机构
    public static String PKORG ;


    /**
     * 性别编码转换--平台转HIS
     * @param sexCode
     * @return
     */
    public static String pingToHisSexMap(String sexCode){
        String code = "04";
        switch(sexCode){
            case "0" ://未知的性别
                code = "04";
                break;
            case "1" ://男性
                code = "02";
                break;
            case "2" ://女性
                code = "03";
                break;
            case "9" ://未说明的性别
                code = "04";
                break;

        }
        return code;
    }

    /**
     * 性别编码转换--HIS转平台
     * @param sexCode
     * @return
     */
    public static String hisToPingSexMap(String sexCode){
        String code = "0";
        switch(sexCode){
            case "04" ://未知的性别
                code = "0";
                break;
            case "02" ://男性
                code = "1";
                break;
            case "03" ://女性
                code = "2";
                break;
        }
        return code;
    }


    /**
     * 证件类型编码转换--平台转HIS
     * @param sexCode
     * @return
     */
    public static String pingToHisIdnoTypeMap(String sexCode){
        String code = "99";
        switch(sexCode){
            case "01" ://居民身份证
                code = "01";
                break;
            case "02" ://居民户口簿
                code = "11";
                break;
            case "03" ://护照
                code = "10";
                break;
            case "04" ://军官证
                code = "02";
                break;
            case "05" ://驾驶证
                code = "12";
                break;
            case "06" ://港澳居民来往内地通行证
                code = "06";
                break;
            case "07" ://台湾居民来往内地通行证
                code = "08";
                break;
            case "99" ://其他法定有效证件
                code = "99";
                break;
        }
        return code;
    }
    /**
     * 证件类型编码转换--HIS转平台
     * @param sexCode
     * @return
     */
    public static String hisToPingIdnoTypeMap(String sexCode){
        String code = "99";
        switch(sexCode){
            case "01" ://居民身份证
                code = "01";
                break;
            case "11" ://居民户口簿
                code = "02";
                break;
            case "10" ://护照
                code = "03";
                break;
            case "02" ://军官证
                code = "04";
                break;
            case "12" ://驾驶证
                code = "05";
                break;
            case "06" ://港居民来往内地通行证
                code = "06";
                break;
            case "07" ://澳居民来往内地通行证
                code = "06";
                break;
            case "08" ://台湾居民来往内地通行证
                code = "07";
                break;
            case "99" ://其他法定有效证件
                code = "99";
                break;
        }
        return code;
    }

    /**
        * 国籍编码转换--平台转HIS
	* @param nationalityCode
	* @return
	*/
	public static String pingToHisNationalityMap(String nationalityCode){
	   String code = "2";
	   switch(nationalityCode){
	       case "156" ://中国
	           code = "2";
	           break;
	       case "344" ://香港
	           code = "350";
	           break;
	       case "446" ://澳门
	           code = "377";
	           break;
	       case "897" ://台湾
	           code = "483";
	           break;
	   }
	   return code;
	}
    
	/**
        *   职业编码转换--平台转HIS
	* @param occupationCode
	* @return
	*/
	public static String pingToHisOccupationMap(String occupationCode){
	   String code = "99";
	   switch(occupationCode){
	       case "11" ://国家公务员
	           code = "00";
	           break;
	       case "13" ://专业技术人员
	           code = "01";
	           break;
	       case "17" ://职员
	           code = "02";
	           break;
	       case "21" ://企业管理人员
	           code = "03";
	           break;
	       case "24" ://工人
	           code = "04";
	           break;
	       case "27" ://农民
	           code = "05";
	           break;
	       case "31" ://学生
	           code = "06";
	           break;
	       case "37" ://现役军人
	           code = "07";
	           break;
	       case "51" ://自由职业者
	           code = "08";
	           break;
	       case "54" ://个体经营者
	           code = "09";
	           break;
	       case "70" ://无业人员
	           code = "10";
	           break;
	       case "80" ://退（离）休人员
	           code = "11";
	           break;
	       case "90" ://其他
	           code = "99";
	           break;
	   }
	   return code;
	}
	
	/**
        *  婚姻编码转换--平台转HIS
	* @param maritalStatusCode
	* @return
	*/
	public static String pingToHisMaritalStatusMap(String maritalStatusCode){
	   String code = "9";
	   switch(maritalStatusCode){
	       case "10" ://未婚
	           code = "1";
	           break;
	       case "20" ://已婚
	           code = "2";
	           break;
	       case "22" ://再婚
	           code = "6";
	           break;
	       case "30" ://丧偶
	           code = "3";
	           break;
	       case "40" ://离婚
	           code = "4";
	           break;
	       case "90" ://未说明的婚姻状况
	           code = "9";
	           break;
	   }
	   return code;
	}
	
	/**
     *  学历编码转换--平台转HIS
	* @param educationCode
	* @return
	*/
	public static String pingToHisEducationMap(String educationCode){
	   String code = "";
	   switch(educationCode){
	       case "10" ://研究生教育
	           code = "01";
	           break;
	       case "20" ://大学本科
	           code = "02";
	           break;
	       case "30" ://专科教育
	           code = "03";
	           break;
	       case "40" ://中等职业教育
	           code = "04";
	           break;
	   }
	   return code;
	}
	
	/**
     *  医嘱类型编码转换--平台转HIS
	* @param dtRalation
	* @return
	*/
	public static String hisToPingOrdtypeCodeMap(String ordtypeCode){
	   String code = "99";
	   switch(ordtypeCode){
	       case "01" ://药品类医嘱
	           code = "01";
	           break;
	       case "02" ://检查类医嘱
	           code = "02";
	           break;
	       case "03" ://检验类医嘱
	           code = "03";
	           break;
	       case "04" ://手术类医嘱
	           code = "04";
	           break;
	       case "05" ://治疗类医嘱
	           code = "05";
	           break;
	       case "09" ://诊疗类医嘱
	           code = "05";
	           break;
	       case "06" ://护理类医嘱
	           code = "05";
	           break;
	       case "07" ://材料类医嘱
	           code = "06";
	           break;
	       case "08" ://嘱托医嘱  
	           code = "07";
	           break;
	       case "12" ://输血类医嘱
	           code = "08";
	           break; 
	   }
	   return code;
	}
	
	/**
     *  医嘱类型名称转换--HIS转平台
	* @param dtRalation
	* @return
	*/
	public static String hisToPingOrdtypeNameMap(String ordtypeName){
	   String code = "其他医嘱";
	   switch(ordtypeName){
	       case "01" :
	           code = "药品类医嘱";
	           break;
	       case "02" :
	           code = "检查类医嘱";
	           break;
	       case "03" :
	           code = "检验类医嘱";
	           break;
	       case "04" :
	           code = "手术类医嘱";
	           break;
	       case "05" :
	           code = "处置类医嘱";
	           break;
	       case "09" :
	           code = "处置类医嘱";
	           break;
	       case "06" :
	           code = "处置类医嘱";
	           break;
	       case "07" :
	           code = "材料类医嘱";
	           break;
	       case "08" :
	           code = "嘱托医嘱";
	           break;
	       case "12" :
	           code = "输血类医嘱";
	           break; 
	   }
	   return code;
	}
	
	
	/**
     *  关系编码转换--平台转HIS
	* @param dtRalation
	* @return
	*/
	public static String pingToHisDtRalationMap(String dtRalation){
	   String code = "109";
	   switch(dtRalation){
	       case "10" ://配偶
	           code = "101";
	           break;
	       case "20" ://子
	           code = "102";
	           break;
	       case "30" ://女
	           code = "103";
	           break;
	       case "40" ://孙子、孙女或外孙子、外孙女
	           code = "104";
	           break;
	       case "50" ://父母
	           code = "105";
	           break;
	       case "60" ://祖父母或外祖父母
	           code = "106";
	           break;
	       case "70" ://兄、弟、姐、妹
	           code = "107";
	           break;
	       case "80" ://其他
	           code = "109";
	           break;
	   }
	   return code;
	}

    /**
	 * 获取对照信息
	 * @param factory  要获取的哪一方的编码 
	 * @param codeType 对照的类型   DTSEX(性别)  NATION（民族）IDTYPE（证件类型）  后续可以自己加对照
	 * @param code 编码
	 * @return
	 */
    public static String getCollationInfo(String factory,String codeType,String code){
    	String resCode = ""; 
		if(CommonUtils.isEmptyString(code)){
			return resCode;
		}
		//获取his方code
		if("his".equals(factory)){
			
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap(
					"select code_his from INS_SZYB_DICTMAP where EU_HPDICTTYPE='03' and code_type = ?  "
					+ " and code_insur=? and flag_chd='1' and del_flag='0'",new Object[]{codeType,code});
			resCode = CommonUtils.getPropValueStr(queryForMap, "codeHis");
		}else{
			//获取国家标准code
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select code_insur from INS_SZYB_DICTMAP where EU_HPDICTTYPE='03' and code_type = ?  "
					+ " and code_his=? and flag_chd='1' and del_flag='0'",new Object[]{codeType,code});
			resCode = CommonUtils.getPropValueStr(queryForMap, "codeInsur");
		}
		return resCode;
	}


}
