package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Map;



public class TransfTool {

    /***
     * 转义就诊状态
     * @param euStatus
     * @return
     */
    public static  String getEncStatus(String euStatus){
        if(StringUtils.isBlank(euStatus)){
            return "planned";
        }
        switch (euStatus){
            case "0":
                return "planned";
            case "1":
                return "in-progress";
            case "2":
                return "finished";
            case "3":
                return "finished";
            case "9":
                return "cancelled";
            default:
                return "unknown";
        }
    }

    /**
     * 转义 就诊类型代码
     * @param euPvtype
     * @return
     */
    public static Coding getPvtype(String euPvtype){
        //AMB|EMER|IMP 门诊|急诊|住院
        Coding coding = new Coding();
        if(StringUtils.isBlank(euPvtype) ||StringUtils.indexOfAny(euPvtype, EnumerateParameter.ONE,EnumerateParameter.FOUR)>=0){
            coding.setCode("AMB");
            coding.setDisplay("门诊");
        } else if(EnumerateParameter.TWO.equals(euPvtype)){
            coding.setCode("EMER");
            coding.setDisplay("急诊");
        } else if(EnumerateParameter.THREE.equals(euPvtype)){
            coding.setCode("IMP");
            coding.setDisplay("住院");
        } else {
            coding.setCode("AMB");
            coding.setDisplay("门诊");
        }
        return coding;
    }

    /**
     * 转义 自定义机构类型
     * @param euPvtype
     * @return
     */
    public static Coding getOrgtype(String system, String euPvtype){
        //中山平台：1集团医院2医院3院区4公司5政府机构6其他医疗机构9其他机构
        //nhis：01：集团医院成员02市级直属医院03：区级04：社区中心及乡镇医院05:大学06：军队07：私立
        Coding coding = new Coding();
        coding.setSystem(system);
        if("01".equals(euPvtype)){
            coding.setCode(EnumerateParameter.ONE);
            coding.setDisplay("集团医院");
        } else if("02".equals(euPvtype) || "03".equals(euPvtype) || "04".equals(euPvtype)){
            coding.setCode(EnumerateParameter.FIVE);
            coding.setDisplay("政府机构");
        } else if("06".equals(euPvtype) || "05".equals(euPvtype)){
            coding.setCode(EnumerateParameter.SIX);
            coding.setDisplay("其他医疗机构");
        } else {
            coding.setCode(EnumerateParameter.NINE);
            coding.setDisplay("其他机构");
        }
        return coding;
    }

    /**
     * 转义 就诊类型转义
     * @param euPvtype
     * @return
     */
    public static Coding getEuPvtype(String euPvtype){
        //中山平台：就诊类别；AMB:门诊、IMP:住院、EMER:急诊
        //nhis：1门诊，2急诊，3住院，4体检，5家庭病床
        Coding coding = new Coding();
        switch (euPvtype){
            case "1":
                coding.setCode("AMB");
                coding.setDisplay("门诊");
                break;
            case "2":
                coding.setCode("EMER");
                coding.setDisplay("急诊");
                break;
            case "3":
                coding.setCode("IMP");
                coding.setDisplay("住院");
                break;
            default:
                coding.setCode("");
                coding.setDisplay("");
                break;
        }
        return coding;
    }

    public static String getSrvText(String euSrvtype){
        if(EnumerateParameter.ZERO.equals(euSrvtype)){
            return "普通";
        }
        if(EnumerateParameter.ONE.equals(euSrvtype)){
            return "'专家'";
        }
        if(EnumerateParameter.TWO.equals(euSrvtype)){
            return "'特诊'";
        }
        if(EnumerateParameter.NINE.equals(euSrvtype)){
            return "'急诊'";
        }
        return "其他";
    }

    public static <T> T mapToBean(Class<T> classOfT, Map<String, ? extends Object> properties) {
        //先使用gson快速转，不行用jackson，否则异常
        if(MapUtils.isNotEmpty(properties)){
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            String json = gson.toJson(properties);
            try {
                return gson.fromJson(json,classOfT);
            } catch (Exception e){
                return JSON.parseObject(json,classOfT);
            }
        }
        return null;
    }


    /**
     * 取文本内容
     * @param map
     * @return
     */
    public static String getPropValueStr(Map<String, Object> map, String key) {
        String value="" ;
        if(MapUtils.isNotEmpty(map)){
            if(map.containsKey(key)){
                Object obj=map.get(key);
                value=obj==null?"":obj.toString();
            }
        }
        return value;
    }

    /**
     * 取日期内容
     * @param map
     * @return
     */
    public static Date getPropValueDates(Map<String, Object> map, String key) {
        Date value=null;
        boolean have=map.containsKey(key);
        if(have){
            Object obj=map.get(key);
            if(obj==null) return null;
            if(obj instanceof Date )
            {
                return (Date)obj;//如果传入的就是Date类型的数据
            }
            String dateStr=obj.toString();
            value = DateUtils.strToDate(dateStr, "yyyyMMddHHmmss");
        }
        return value;
    }

    /**
     * 转换性别
     * @param dtSex
     * @return
     */
    public static String getSex(String dtSex){
        //性别  male:男性;female:女性;other:其他;unknown:未说明的性别
        //NHIS 02:男,03:女,04：未知
        if("02".equals(dtSex)){
            return "male";
        }else if("03".equals(dtSex)){
            return "female";
        }else if("04".equals(dtSex)){
            return "unknown";
        }else
            return "other";
    }
    /**
     * 转义 处方类型转义
     * @param euStatusOrd
     * @return
     */
    public static String getEuStatusOrd(String euStatusOrd){
        //中山平台：N-开立 V-审核 S-收费 E-执行 C-取消
        //nhis：0 开立；1 签署；2 核对；3 执行；4 停止；9 作废
        switch (euStatusOrd){
            case "0":
                return "N";
            case "2":
                return "V";
            case "3":
                return "E";
            case "9":
                return "C";
            default:
                return "";
        }
    }
    /**
     * 根据code查询对于公共字典名称
     * @param codeDefdoclist
     * @param code
     * @return
     */
    public static String getBdDefdocName(String codeDefdoclist,String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        Map<String,Object> defdocMap = DataBaseHelper.queryForMap("select name from bd_defdoc where DEL_FLAG = '0' and code_defdoclist=? and code=?",codeDefdoclist,code);
        return getPropValueStr(defdocMap,"name");
    }

    /**
     * 转义 科室类型转义
     * @param DeptType
     * @return
     */
    public static Coding getDeptType(String DeptType){
        //中山平台：00|01|02|03|04|05|06|07|08
        //无|医疗|护理|医技|药学|职能|工勤辅助|附设机构|非实体机构
        Coding coding = new Coding();
        switch (DeptType){
            case "01":
                coding.setCode("01");
                coding.setDisplay("医疗");
                break;
            case "02":
                coding.setCode("02");
                coding.setDisplay("护理");
                break;
            case "03":
                coding.setCode("03");
                coding.setDisplay("医技");
                break;
            case "04":
                coding.setCode("04");
                coding.setDisplay("药学");
                break;
            case "05":
                coding.setCode("05");
                coding.setDisplay("职能");
                break;
            case "06":
                coding.setCode("06");
                coding.setDisplay("工勤辅助");
                break;
            case "07":
                coding.setCode("07");
                coding.setDisplay("附设机构");
                break;
            case "08":
                coding.setCode("08");
                coding.setDisplay("非实体机构");
                break;
            default:
                coding.setCode("00");
                coding.setDisplay("无");
                break;
        }
        return coding;
    }

    public static String getPtInsuName(String ptInsuCode){
        if(StringUtils.isBlank(ptInsuCode))
            ptInsuCode = "04";
        switch (ptInsuCode){
            case "04":
                return "普通病人";
            case "81":
                return "特定医保病人";
            case "90":
                return "医保";
            case "96":
                return "省异地医保";
            case "97":
                return "全国医保";
            case "98":
                return "全国工伤";
            case "99":
                return "新国家医保";
        }
        return "普通病人";
    }
}
