package com.zebone.nhis.webservice.pskq.utils;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class PskqMesUtils {

    public static SimpleDateFormat simDatFor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");


    //院部代码_
    public static final String pskqHisCode = "10_";

    //系统代码_
    public static final String pskqSysCode = "2000_";



    /**
     * 时间转换加  T
     * @param time
     * @return
     */
    public static String getTimeAddT(String time){


        return "";
    }


    public static String getPvTypeToPvTypeName(String pvcode){
       String pvName = "其他";
        switch(pvcode){
            case "1" ://门诊
                pvName = "门诊";
                break;
            case "2" ://急诊
                pvName = "急诊";
                break;
            case "3" ://住院
                pvName = "住院";
                break;
            case "9" ://其他
                pvName = "其他";
                break;
        }
        return pvName;
    }
    
    /**
     * 根据发送人id 获取登录信息
     * @param senderId
     * @return
     */
    public static User getUserExt(String senderId){
    	String codeEmp = "";
    	if("2500".equals(senderId)) {//自助机
    		codeEmp = "zzj";
    	}else if("2510".equals(senderId)) {//微信公众号
    		codeEmp = "wxgzh";
    	}else if("2520".equals(senderId)) {//健康160
    		codeEmp = "jk160";
    	}
    	User user = new User();
		Map<String, Object> bdOuMap = DataBaseHelper
				.queryForMap(
						"SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.CODE_EMP = ?",
						codeEmp);
		if (bdOuMap != null) {
			user.setPkOrg(CommonUtils.getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(CommonUtils.getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkOrg(CommonUtils.getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(CommonUtils.getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkEmp(CommonUtils.getPropValueStr(bdOuMap, "pkEmp"));
			user.setPkDept(CommonUtils.getPropValueStr(bdOuMap, "pkDept"));
			user.setCodeEmp(CommonUtils.getPropValueStr(bdOuMap, "codeEmp"));
		}else {
			user = null;
		}
		return user;
    }

    /**
     * 取文本内容
     * @param map
     * @return
     */
    public static String getPropValueStr(Map<String, Object> map,String key) {
        String value="" ;
        if(key==null||"".equals(key)||map==null||map.size()<=0){
            return "";
        }
        if(map.containsKey(key)){
            Object obj=map.get(key);
            value=obj==null?"":obj.toString();
        }
        return value;
    }

    public static String getPk() {
        String pk= UUID.randomUUID().toString().replace("-", "");
        return pk;
    }
}
