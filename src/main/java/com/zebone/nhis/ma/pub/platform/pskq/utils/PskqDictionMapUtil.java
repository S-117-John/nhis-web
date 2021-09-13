package com.zebone.nhis.ma.pub.platform.pskq.utils;


import java.util.HashMap;
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
     * 性别名称转换--HIS转平台
     * @param sexCode
     * @return
     */
    public static String hisToPingSexNameMap(String sexCode){
        String code = "未说明的性别";
        switch(sexCode){
            case "02" ://男性
                code = "男性";
                break;
            case "03" ://女性
                code = "女性";
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
     *  学历编码转换--HIS转平台
	* @param educationCode
	* @return
	*/
	public static String hisToPingEducationMap(String educationCode){
	   String code = "";
	   switch(educationCode){
	       case "01" ://研究生教育
	           code = "10";
	           break;
	       case "02" ://大学本科
	           code = "20";
	           break;
	       case "03" ://专科教育
	           code = "30";
	           break;
	       case "04" ://中等职业教育
	           code = "40";
	           break;
	   }
	   return code;
	}
	
	/**
     *  医嘱类型编码转换--HIS转平台
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

    /**
     *  药品剂型转换--HIS转平台
	* @param dtRalation
	* @return
	*/
	public static Map<String,Object> hisToPingDosageFormMap(String dosageForm){
	   Map<String,Object> dosageFormMap = new HashMap<String,Object>(16);
	   if("00".equals(dosageForm)||"0000".equals(dosageForm)) {
		   dosageFormMap.put("code", "00");
		   dosageFormMap.put("name", "原料");
	   }else if("01".equals(dosageForm) ||"0110".equals(dosageForm) || "0111".equals(dosageForm) 
			   || "0113".equals(dosageForm)) {
		   dosageFormMap.put("code", "01");
		   dosageFormMap.put("name", "片剂(素片、压制片)，浸膏片,非包衣片");
	   }else if("0108".equals(dosageForm) || "0121".equals(dosageForm) || "0142".equals(dosageForm)) {
		   dosageFormMap.put("code", "02");
		   dosageFormMap.put("name", "糖衣片,包衣片,薄膜衣片");
	   }else if("0112".equals(dosageForm) || "0191".equals(dosageForm)) {
		   dosageFormMap.put("code", "03");
		   dosageFormMap.put("name", "咀嚼片,糖片,异型片,糖胶片");
	   }else if("0124".equals(dosageForm)) {
		   dosageFormMap.put("code", "04");
		   dosageFormMap.put("name", "肠溶片(肠衣片)");
	   }else if("0114".equals(dosageForm) || "0123".equals(dosageForm) || "0143".equals(dosageForm)
			  || "0151".equals(dosageForm) || "0152".equals(dosageForm)) {
		   dosageFormMap.put("code", "05");
		   dosageFormMap.put("name", "调释片,缓释片,控释片,速释片，长效片，多层片");
	   }else if("0182".equals(dosageForm) || "0192".equals(dosageForm)) {
		   dosageFormMap.put("code", "06");
		   dosageFormMap.put("name", "泡腾片");
	   }else if("0163".equals(dosageForm)) {
		   dosageFormMap.put("code", "07");
		   dosageFormMap.put("name", "舌下片");
	   }else if("0161".equals(dosageForm) || "0162".equals(dosageForm) || "0164".equals(dosageForm)) {
		   dosageFormMap.put("code", "08");
		   dosageFormMap.put("name", "含片,嗽口片(含嗽片),喉症片(喉片),口腔粘附片");
	   }else if("0198".equals(dosageForm)) {
		   dosageFormMap.put("code", "09");
		   dosageFormMap.put("name", "外用片,外用膜,坐药片,环型片");
	   }else if("0181".equals(dosageForm)) {
		   dosageFormMap.put("code", "10");
		   dosageFormMap.put("name", "阴道片,外用阴道膜,阴道用药,阴道栓片");
	   }else if("9933".equals(dosageForm)) {
		   dosageFormMap.put("code", "11");
		   dosageFormMap.put("name", "水溶片,眼药水片");
	   }else if("0131".equals(dosageForm)) {
		   dosageFormMap.put("code", "12");
		   dosageFormMap.put("name", "分散片(适应片)");
	   }else if("9932".equals(dosageForm)) {
		   dosageFormMap.put("code", "13");
		   dosageFormMap.put("name", "纸片(纸型片),膜片(薄膜片)");
	   }else if("07".equals(dosageForm) || "0711".equals(dosageForm) || "0712".equals(dosageForm)
			   || "0713".equals(dosageForm) || "0714".equals(dosageForm) || "0715".equals(dosageForm)
			   || "0716".equals(dosageForm) || "0722".equals(dosageForm) || "0724".equals(dosageForm)
			   || "0731".equals(dosageForm) || "0761".equals(dosageForm) || "0762".equals(dosageForm)
			   || "0763".equals(dosageForm) || "0764".equals(dosageForm) || "0765".equals(dosageForm)
			   || "2261".equals(dosageForm)) {
		   dosageFormMap.put("code", "14");
		   dosageFormMap.put("name", "丸剂,药丸,眼丸,耳丸,糖丸,糖衣丸,浓缩丸,调释丸,水丸");
	   }else if("0231".equals(dosageForm) || "0232".equals(dosageForm) || "9910".equals(dosageForm)
			   || "9915".equals(dosageForm)) {
		   dosageFormMap.put("code", "15");
		   dosageFormMap.put("name", "粉针剂(冻干粉针剂),冻干粉");
	   }else if("02".equals(dosageForm) || "0210".equals(dosageForm) || "0211".equals(dosageForm) 
			   || "0215".equals(dosageForm) || "0212".equals(dosageForm) || "0221".equals(dosageForm)
			   || "0223".equals(dosageForm) || "2123".equals(dosageForm)) {
		   dosageFormMap.put("code", "16");
		   dosageFormMap.put("name", "注射液(水针剂),油针剂,混悬针剂");
	   }else if("9931".equals(dosageForm)) {
		   dosageFormMap.put("code", "17");
		   dosageFormMap.put("name", "注射溶媒，(在16有冲突时,可代油针剂、混悬针剂) ");
	   }else if("9930".equals(dosageForm)) {
		   dosageFormMap.put("code", "18");
		   dosageFormMap.put("name", "输液剂,血浆代用品");
	   }else if("04".equals(dosageForm) || "0411".equals(dosageForm) || "0434".equals(dosageForm)
			   || "0441".equals(dosageForm)) {
		   dosageFormMap.put("code", "19");
		   dosageFormMap.put("name", "胶囊剂,硬胶囊");
	   }else if("0412".equals(dosageForm) || "0413".equals(dosageForm) || "0423".equals(dosageForm)
			   || "0751".equals(dosageForm)) {
		   dosageFormMap.put("code", "20");
		   dosageFormMap.put("name", "软胶囊,滴丸,胶丸");
	   }else if("0421".equals(dosageForm) || "0433".equals(dosageForm) || "0741".equals(dosageForm)) {
		   dosageFormMap.put("code", "21");
		   dosageFormMap.put("name", "肠溶胶囊,肠溶胶丸");
	   }else if("0431".equals(dosageForm) || "0432".equals(dosageForm)) {
		   dosageFormMap.put("code", "22");
		   dosageFormMap.put("name", "调释胶囊,控释胶囊,缓释胶囊");
	   }else if("0167".equals(dosageForm) || "1131".equals(dosageForm) || "1142".equals(dosageForm)
			   || "1422".equals(dosageForm) || "1511".equals(dosageForm) || "1542".equals(dosageForm)
			   || "1544".equals(dosageForm) || "2223".equals(dosageForm)) {
		   dosageFormMap.put("code", "23");
		   dosageFormMap.put("name", "溶液剂,含漱液,内服混悬液");
	   }else if("1412".equals(dosageForm) || "1415".equals(dosageForm)) {
		   dosageFormMap.put("code", "24");
		   dosageFormMap.put("name", "合剂");
	   }else if("0222".equals(dosageForm) || "0532".equals(dosageForm)) {
		   dosageFormMap.put("code", "25");
		   dosageFormMap.put("name", "乳剂,乳胶");
	   }else if("05".equals(dosageForm) || "0531".equals(dosageForm) || "0534".equals(dosageForm)
			  || "0535".equals(dosageForm) || "2133".equals(dosageForm) || "2233".equals(dosageForm)
			  || "2333".equals(dosageForm) || "9901".equals(dosageForm)) {
		   dosageFormMap.put("code", "26");
		   dosageFormMap.put("name", "凝胶剂,胶剂(胶体),胶冻,胶体微粒");
	   }else if("0533".equals(dosageForm)) {
		   dosageFormMap.put("code", "27");
		   dosageFormMap.put("name", "胶浆剂");
	   }else if("1414".equals(dosageForm)) {
		   dosageFormMap.put("code", "28");
		   dosageFormMap.put("name", "芳香水剂(露剂)");
	   }else if("1431".equals(dosageForm) || "1432".equals(dosageForm)) {
		   dosageFormMap.put("code", "29");
		   dosageFormMap.put("name", "滴剂");
	   }else if("1413".equals(dosageForm)) {
		   dosageFormMap.put("code", "30");
		   dosageFormMap.put("name", "糖浆剂(蜜浆剂)");
	   }else if("14".equals(dosageForm) || "1411".equals(dosageForm) || "1416".equals(dosageForm)
			   || "1417".equals(dosageForm) || "1421".equals(dosageForm) || "1426".equals(dosageForm)) {
		   dosageFormMap.put("code", "31");
		   dosageFormMap.put("name", "口服液");
	   }else if("1441".equals(dosageForm)) {
		   dosageFormMap.put("code", "32");
		   dosageFormMap.put("name", "浸膏剂");
	   }else if("1442".equals(dosageForm)) {
		   dosageFormMap.put("code", "33");
		   dosageFormMap.put("name", "流浸膏剂");
	   }else if("1012".equals(dosageForm)) {
		   dosageFormMap.put("code", "34");
		   dosageFormMap.put("name", "酊剂");
	   }else if("1013".equals(dosageForm)) {
		   dosageFormMap.put("code", "35");
		   dosageFormMap.put("name", "醑剂");
	   }else if("1014".equals(dosageForm)) {
		   dosageFormMap.put("code", "36");
		   dosageFormMap.put("name", "酏剂");
	   }else if("1541".equals(dosageForm)) {
		   dosageFormMap.put("code", "37");
		   dosageFormMap.put("name", "洗剂,阴道冲洗剂");
	   }else if("1512".equals(dosageForm) || "1521".equals(dosageForm) || "1522".equals(dosageForm)) {
		   dosageFormMap.put("code", "38");
		   dosageFormMap.put("name", "搽剂(涂剂,擦剂),外用混悬液剂");
	   }else if("1531".equals(dosageForm) || "1532".equals(dosageForm)) {
		   dosageFormMap.put("code", "39");
		   dosageFormMap.put("name", "油剂,甘油剂");
	   }else if("9929".equals(dosageForm)) {
		   dosageFormMap.put("code", "40");
		   dosageFormMap.put("name", "棉胶剂(火棉胶剂)");
	   }else if("1524".equals(dosageForm)) {
		   dosageFormMap.put("code", "41");
		   dosageFormMap.put("name", "涂膜剂");
	   }else if("1523".equals(dosageForm)) {
		   dosageFormMap.put("code", "42");
		   dosageFormMap.put("name", "涂布剂");
	   }else if("2122".equals(dosageForm) || "2124".equals(dosageForm)) {
		   dosageFormMap.put("code", "43");
		   dosageFormMap.put("name", "滴眼剂,洗眼剂,粉剂眼药");
	   }else if("2321".equals(dosageForm) || "2322".equals(dosageForm)) {
		   dosageFormMap.put("code", "44");
		   dosageFormMap.put("name", "滴鼻剂,洗鼻剂");
	   }else if("2221".equals(dosageForm) || "2222".equals(dosageForm)) {
		   dosageFormMap.put("code", "45");
		   dosageFormMap.put("name", "滴耳剂,洗耳剂");
	   }else if("9928".equals(dosageForm)) {
		   dosageFormMap.put("code", "46");
		   dosageFormMap.put("name", "口腔药剂,口腔用药,牙科用药");
	   }else if("1543".equals(dosageForm)) {
		   dosageFormMap.put("code", "47");
		   dosageFormMap.put("name", "灌肠剂");
	   }else if("0521".equals(dosageForm) || "2231".equals(dosageForm) || "2331".equals(dosageForm)) {
		   dosageFormMap.put("code", "48");
		   dosageFormMap.put("name", "软膏剂(油膏剂,水膏剂)");
	   }else if("0522".equals(dosageForm) || "2132".equals(dosageForm) || "2232".equals(dosageForm)
			   || "2332".equals(dosageForm)) {
		   dosageFormMap.put("code", "49");
		   dosageFormMap.put("name", "霜剂(乳膏剂)");
	   }else if("0523".equals(dosageForm)) {
		   dosageFormMap.put("code", "50");
		   dosageFormMap.put("name", "糊剂");
	   }else if("1632".equals(dosageForm) || "1636".equals(dosageForm)) {
		   dosageFormMap.put("code", "51");
		   dosageFormMap.put("name", "硬膏剂,橡皮膏");
	   }else if("2131".equals(dosageForm)) {
		   dosageFormMap.put("code", "52");
		   dosageFormMap.put("name", "眼膏剂");
	   }else if("06".equals(dosageForm) || "0611".equals(dosageForm) || "0612".equals(dosageForm)
			   || "0613".equals(dosageForm) || "2251".equals(dosageForm) || "2351".equals(dosageForm)
			   || "9909".equals(dosageForm)) {
		   dosageFormMap.put("code", "53");
		   dosageFormMap.put("name", "散剂(内服散剂,外用散剂,粉剂,撒布粉)");
	   }else if("13".equals(dosageForm) || "1311".equals(dosageForm) || "0233".equals(dosageForm) 
			   || "1313".equals(dosageForm) || "1321".equals(dosageForm) || "1351".equals(dosageForm)
			   || "152".equals(dosageForm)) {
		   dosageFormMap.put("code", "54");
		   dosageFormMap.put("name", "颗粒剂(冲剂),晶剂(结晶,晶体),干糖浆");
	   }else if("1331".equals(dosageForm) || "1332".equals(dosageForm)) {
		   dosageFormMap.put("code", "55");
		   dosageFormMap.put("name", "泡腾颗粒剂");
	   }else if("1341".equals(dosageForm) || "1342".equals(dosageForm)) {
		   dosageFormMap.put("code", "56");
		   dosageFormMap.put("name", "调释颗粒剂,缓释颗粒剂");
	   }else if("1121".equals(dosageForm)) {
		   dosageFormMap.put("code", "57");
		   dosageFormMap.put("name", "气雾剂,水雾剂,(加抛射剂)");
	   }else if("1122".equals(dosageForm) || "2241".equals(dosageForm) || "2340".equals(dosageForm)
			   || "2342".equals(dosageForm)) {
		   dosageFormMap.put("code", "58");
		   dosageFormMap.put("name", "喷雾剂,(不加抛射剂)");
	   }else if("9927".equals(dosageForm)) {
		   dosageFormMap.put("code", "59");
		   dosageFormMap.put("name", "混悬雾剂,(水、气、粉三相)");
	   }else if("1123".equals(dosageForm) || "2341".equals(dosageForm)) {
		   dosageFormMap.put("code", "60");
		   dosageFormMap.put("name", "吸入药剂(鼻吸式),粉雾剂");
	   }else if("12".equals(dosageForm) || "1211".equals(dosageForm)) {
		   dosageFormMap.put("code", "61");
		   dosageFormMap.put("name", "膜剂(口腔膜)");
	   }else if("9904".equals(dosageForm)) {
		   dosageFormMap.put("code", "62");
		   dosageFormMap.put("name", "海绵剂");
	   }else if("03".equals(dosageForm) || "0311".equals(dosageForm) || "0312".equals(dosageForm)
			   || "2271".equals(dosageForm)) {
		   dosageFormMap.put("code", "63");
		   dosageFormMap.put("name", "栓剂,痔疮栓,耳栓");
	   }else if("09".equals(dosageForm) || "0911".equals(dosageForm)) {
		   dosageFormMap.put("code", "64");
		   dosageFormMap.put("name", "植入栓");
	   }else if("16".equals(dosageForm) || "1611".equals(dosageForm) || "1612".equals(dosageForm)
			   || "1621".equals(dosageForm) || "1631".equals(dosageForm)) {
		   dosageFormMap.put("code", "65");
		   dosageFormMap.put("name", "透皮剂,贴剂(贴膏,贴膜),贴片");
	   }else if("0165".equals(dosageForm) || "1613".equals(dosageForm) || "1622".equals(dosageForm)) {
		   dosageFormMap.put("code", "66");
		   dosageFormMap.put("name", "控释透皮剂,控释贴片,控释口颊片");
	   }else if("9926".equals(dosageForm)) {
		   dosageFormMap.put("code", "67");
		   dosageFormMap.put("name", "划痕剂");
	   }else if("9925".equals(dosageForm)) {
		   dosageFormMap.put("code", "68");
		   dosageFormMap.put("name", "珠链(泥珠链)");
	   }else if("9902".equals(dosageForm)) {
		   dosageFormMap.put("code", "69");
		   dosageFormMap.put("name", "锭剂,糖锭");
	   }else if("0422".equals(dosageForm) || "0721".equals(dosageForm)) {
		   dosageFormMap.put("code", "70");
		   dosageFormMap.put("name", "微囊胶囊(微丸胶囊)");
	   }else if("08".equals(dosageForm) || "0811".equals(dosageForm)) {
		   dosageFormMap.put("code", "71");
		   dosageFormMap.put("name", "干混悬剂(干悬乳剂、口服乳干粉)");
	   }else if("1141".equals(dosageForm)) {
		   dosageFormMap.put("code", "72");
		   dosageFormMap.put("name", "吸入剂(气体)");
	   }else if("9913".equals(dosageForm) || "9914".equals(dosageForm)) {
		   dosageFormMap.put("code", "90");
		   dosageFormMap.put("name", "试剂盒(诊断用试剂),药盒");
	   }else {
		   dosageFormMap.put("code", "99");
		   dosageFormMap.put("name", "其它剂型(空心胶囊,绷带,纱布,胶布)");
	   }
	   return dosageFormMap;
	}
	

}
