package com.zebone.nhis.webservice.support;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zebone.nhis.common.support.*;
import com.zebone.nhis.webservice.vo.LbzyResponseVo;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.time.DateFormatUtils;

import com.zebone.nhis.common.module.bl.BlPi;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.webservice.vo.LbBmptResponse;
import com.zebone.nhis.webservice.vo.LbSHResponseVo;
import com.zebone.nhis.webservice.vo.RespCommonVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

public class LbSelfUtil {

	public static User getDefaultUser(String deviceId) {
		User user=null;
		Map<String, Object> bdOuMap = DataBaseHelper
				.queryForMap(
						"SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.CODE_EMP = ?",
						deviceId);
		if (bdOuMap != null) {
			user = new User();
			user.setPkOrg(getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkOrg(getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkEmp(getPropValueStr(bdOuMap, "pkEmp"));
			user.setPkDept(getPropValueStr(bdOuMap, "pkDept"));
			user.setCodeEmp(getPropValueStr(bdOuMap, "codeEmp"));
		}
		return user;
	}

	public static void setDefaultValue(Object obj, boolean flag, User user) {

		Map<String, Object> default_v = new HashMap<String, Object>();
		if (flag) { // 如果新增
			default_v.put("pkOrg", user.getPkOrg());
			default_v.put("creator", user.getPkEmp());
			default_v.put("createTime", new Date());
			default_v.put("delFlag", "0");
		}

		default_v.put("ts", new Date());
		default_v.put("modifier", user.getPkEmp());

		Set<String> keys = default_v.keySet();

		for (String key : keys) {
			Field field = ReflectHelper.getTargetField(obj.getClass(), key);
			if (field != null) {
				ReflectHelper.setFieldValue(obj, key, default_v.get(key));
			}
		}

	}

	public static String selfLbzyError(String errorMsg) {
		LbzyResponseVo response = new LbzyResponseVo();
		response.setResultCode(Constant.UNUSUAL);
		response.setErrorMsg(errorMsg);
		return XmlUtil.beanToXml(response, LbzyResponseVo.class, false);
	}

 	 // 响应公共方法
 	public static LbSHResponseVo common(String resultCode, String result) {
 	    	LbSHResponseVo response = new LbSHResponseVo();
 			RespCommonVo commonVo = new RespCommonVo();
 			commonVo.setResultCode(resultCode);
 			commonVo.setResultMsg(result);
 			commonVo.setSystemTime(ArchUtils.dateTrans(new Date(),
 					"yyyy-MM-dd HH:mm:ss"));
 			response.setCommonVo(commonVo);
 			return response;
 		}
 	
	 //Lb-便民服务 -成功响应公共方法
	public static LbBmptResponse success(LbBmptResponse esponse) {
		    esponse.setResultCode("0000");
		    esponse.setResultMsg("成功");
			return esponse;
		}
	 //Lb-便民服务 -成功响应公共方法
	public static LbBmptResponse failed(LbBmptResponse esponse,String result) {
		    esponse.setResultCode("-1");
		    esponse.setResultMsg(result);
			return esponse;
		}
 		
 	 // 响应公共方法
 	public static LbSHResponseVo common(String resultCode, String result,String reserved) {
 	    	LbSHResponseVo response = new LbSHResponseVo();
 			RespCommonVo commonVo = new RespCommonVo();
 			commonVo.setResultCode(resultCode);
 			commonVo.setResultMsg(result);
 			commonVo.setSystemTime(ArchUtils.dateTrans(new Date(),
 					"yyyy-MM-dd HH:mm:ss"));
 			commonVo.setReserved(reserved);
 			response.setCommonVo(commonVo);
 			return response;
 		}
 	//map to Bean
 	public static Object mapToBean(Map<String, Object> map,Object obj){
 			String jsonBean = JsonUtil.writeValueAsString(map);
 			obj = JsonUtil.readValue(jsonBean, obj.getClass());
 			return obj;
 		}
 	
 

	// 响应公共方法
	public static RespCommonVo commonVo(String resultCode, String result) {
		// 响应是否正常
		RespCommonVo commonVo = new RespCommonVo();
		commonVo.setResultCode(resultCode);
		commonVo.setResultMsg(result);
		commonVo.setSystemTime(ArchUtils.dateTrans(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		return commonVo;
	}

	// Bean to map
	public static Object beanToMap1(Object obj) {
		String stringBean = JsonUtil.writeValueAsString(obj);
		return JsonUtil.readValue(stringBean, Map.class);
	}

	/**
	 * bean 转 Map
	 * 
	 * @param obj
	 * @return
	 */
	private static Map<?, ?> beanToMap(Object obj) {
		if (obj == null){
			return null;
		}
		return new BeanMap(obj);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object obj) {
		return (Map<String, Object>) beanToMap(obj);
	}

	/**
	 * 查找系统参数code获取 系统参数值
	 * 
	 * @param code
	 * @param isPub
	 *            是否全部参数
	 * @return
	 */
	public static String getSysparam(String code, boolean isPub, User user) {

		String sql = "select val from bd_sysparam where code = ? and del_flag = '0' and pk_org=?";
		String org = user.getPkOrg();
		String name = "sys:sysparam:";

		if (isPub) {
			org = "~                               "; // 全局机构主键
			name += "public";
		} else {
			name += org;
		}

		String val_o = RedisUtils.getCacheHashObj(name, code, String.class);

		if (val_o == null) {
			List<Map<String, Object>> datas = DataBaseHelper.queryForList(sql,
					code, org);
			if (datas.size() > 0) {
				String val = CommonUtils.getString(datas.get(0).get("val"));
				if (val != null) {
					RedisUtils.setCacheHashObj(name, code, val);
				}
				return val;
			}

		} else {
			return val_o;
		}
		return null;
	}

	/**
	 * @param user
	 * @param blPi
	 *            非就诊计费接口
	 */
	public void nonTreatment(BlPi blPi, IUser user) {
		blPi.setCodeBl(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
		blPi.setDateBl(new Date());
		blPi.setPkDeptBl(((User) user).getPkDept());
		blPi.setPkEmpBl(((User) user).getPkEmp());
		blPi.setNameEmpBl(((User) user).getNameEmp());
		ApplicationUtils.setDefaultValue(blPi, true);
		DataBaseHelper.insertBean(blPi);
	}
	
	/**
	 * 将字符串boolean类型，转换成java的boolean
	 * @param flag
	 * @return
	 */
	public static boolean converToTrueOrFalse(String flag) {

		if ("1".equals(flag)) {
			return true;
		} else if ("0".equals(flag)) {
			return false;
		} else {
			//throw new BusException("Flag 值错误");
			return false;
		}
	}
	
	/**
	 * 取文本内容
	 * @param map
	 * @return
	 */
	public static String getPropValueStr(Map<String, Object> map,String key) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		String value="" ;
		if(map.containsKey(key)){
			Object obj=map.get(key);
			value=obj==null?"":obj.toString();
		}
		return value;
	}
	
	
	/**
	 * 时间格式转换
	 * @return
	 * @throws ParseException 
	 */
	public static String formatDate(String strDate,Object... pattern) throws ParseException {
		String formatDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(strDate);

		if(date==null){
			return "";
		}
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
     * 通过身份证号码获取出生日期、性别、年龄
     * @param certificateNo
     * @return 返回的出生日期格式：1990-01-01   性别格式：03-女，02-男
     */
    public static Map<String, Object> getBirAgeSex(String certificateNo) {
        String birthday = "";
        String age = "";
        String sexCode = "";
 
        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag){
					return new HashMap<String, Object>();
				}
                flag = Character.isDigit(number[x]);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag){
					return new HashMap<String, Object>();
				}
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-"
                    + certificateNo.substring(8, 10) + "-"
                    + certificateNo.substring(10, 12);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3, certificateNo.length())) % 2 == 0 ? "03" : "02";
            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        } else if (flag && certificateNo.length() == 18) {
            birthday = certificateNo.substring(6, 10) + "-"
                    + certificateNo.substring(10, 12) + "-"
                    + certificateNo.substring(12, 14);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4, certificateNo.length() - 1)) % 2 == 0 ? "03" : "02";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("birthday", birthday);
        map.put("age", age);
        map.put("sexCode", sexCode);
        return map;
    }

}
