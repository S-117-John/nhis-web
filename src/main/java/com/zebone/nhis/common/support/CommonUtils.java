package com.zebone.nhis.common.support;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.shiro.codec.Base64;

import com.zebone.platform.modules.exception.BusException;

/**   
 * 基础处理工具类
 * @author think
 *
 */
public class CommonUtils {
	
	
	private static String  defalutKey = "zebonenhis654321";   //需要16为长度
	/**
	 * AES 加密
	 * @param value  加密值
	 * @param key    加密key建
	 * @return
	 */
	 @SuppressWarnings("static-access")
	public static String encode(byte[] value ,String key){
		 
		 if(key.length() != 16){
			 throw new BusException("加密Key必须是16位字母或者数字！");
		 }
		 try{
	       	 byte[] raw = key.getBytes("utf-8");
	        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	        byte[] encrypted = cipher.doFinal(value);
		 
		   return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。

		 }catch(Exception e){
			 throw new BusException("加密出现错误！" , e);
		 }
	 }
	 
    /**
	 * AES 加密
	 * @param value  加密值
	 * @return
	 */
	 public static String encode(byte[] value ){
		 
		return encode(value , defalutKey );
	 }
	
	 
	/**
	 * AES 解密
	 * @param value  密文
	 * @param key    加密key建
	 * @return
	 */
	 @SuppressWarnings("static-access")
	public static String decode(byte[] value ,String key){
		 
		 if(key.length() != 16){
			 throw new BusException("解密Key必须是16位字母或者数字！");
		 }
		 
		 try{
			 byte[] raw = key.getBytes("utf-8");
	         SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	         cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	        
	         byte[] original = cipher.doFinal(new Base64().decode(value));
	         String originalString = new String(original,"utf-8");
	         return originalString;
	   
	     } catch (Exception ex) {
	    	 throw new BusException("解密出现错误！" , ex);
	     }
	 }
	 
    /**
	 * AES 解密
	 * @param value  密文
	 * @return
	 */
	 public static String decode(byte[] value ){ 
		return decode(value , defalutKey );
	 }
	
	   
	
	/**
	 * 获取Long对象类型
	 * @param v
	 * @return
	 */
	public static Long getLongObject(Object v){
		if(v != null){
			return Long.valueOf(v.toString());
		}else{
			return null;
		}
	}

	
	/**
	 * 获取long类型值
	 * @param v
	 * @return
	 */
	public static long getLong(Object v){
		if(v != null){
			return Long.valueOf(v.toString());
		}else{
			return 0L;
		}
	}
	
	
	
	/**
	 * 获取Integer对象类型
	 * @param v
	 * @return
	 */
	public static Integer getInteger(Object v){
		if(v != null){
			return Integer.valueOf(v.toString());
		}else{
			return null;
		}
	}

	
	/**
	 * 获取int类型值
	 * @param v
	 * @return
	 */
	public static int getInt(Object v){
		if(v != null){
			return Integer.valueOf(v.toString());
		}else{
			return 0;
		}
	}
	
	/**
	 * 获取Integer对象类型
	 * @param v
	 * @return
	 */
	public static Double getDoubleObject(Object v){
		if(v != null){
			return Double.valueOf(v.toString());
		}else{
			return null;
		}
	}

	/**
	 * 获取int类型值
	 * @param v
	 * @return
	 */
	public static double getDouble(Object v){
		if(v != null){
			return Double.valueOf(v.toString());
		}else{
			return 0.0;
		}
	}
	
	/**
	 * 获取String对象类型
	 * @param v
	 * @return
	 */
	public static  String getString(Object v){
		if(v != null){
			return v.toString();
		}else{
			return null;
		}
	}

	/**
	 * 获取String对象类型 ,如果对象为null 返回 默认值dv
	 * @param v
	 * @return
	 */
	public static String getString(Object v,String dv){
		if(v != null){
			return v.toString();
		}else{
			return dv;
		}
	}
	
	/** 判断对象的值是否为空 */
	public static boolean isNull(Object obj) {
		boolean flag = false;
		if (null == obj || "".equals(obj.toString()))
			flag = true;
		return flag;

	}

	/** 判断对象的值是否为空 */
	public static boolean isNotNull(Object obj) {
		boolean flag = true;
		if (null == obj || "".equals(obj.toString()))
			flag = false;
		return flag;

	}
	/**
	 * 判断某字符串是否为空
	 * @param str
	 * @return
	 */
    public static boolean isEmptyString(String str){
    	if(str==null||str.equals("")) return true;
    	return false;
    }
   /**
    * 获取全局机构
    */
    public static String getGlobalOrg(){
    	return "~                               ";
    }
    /**
     * 获取汉字对应的首字母拼音
     * @param str
     * @return
     */
    public static String getPycode(String str){
    	if(str == null) return null;
    	String convert = "";  
	        for (int j = 0; j < str.length(); j++) {  
	            char word = str.charAt(j);  
	            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
	            if (pinyinArray != null) {  
	                convert += pinyinArray[0].charAt(0);  
	            } else {  
	                convert += word;  
	            }  
	         }  
        return convert.toUpperCase();  
    }
	/**
	 * <h1>此方法的使用在超过1000条时，会有SQL短路风险：</h1>
	 * <h2>多条件联立时，比如where n=1 and y in..最终会把语句变为：where n=1 and y in(..) or y in(...)</h2>
	 * <h1>解决办法（各数据库通用）： 在自己的in字段前加上括号比如上面改为 ..where n=1 and ( y in(convertSetToSqlInPart())) </h1>
	 * 将set<String>中的数据修改为： 'value1','value2','value3'
	 * @param setStr
	 *            放在in中的主键
	 * @param pkName
	 *            当上面的属性长度大于1000的时候做分割使用的数据
	 */
	public static String convertSetToSqlInPart(Set<String> setStr, String pkName) {

		StringBuffer sbf = new StringBuffer();
		String[] strArr = setStr.toArray(new String[0]);
		if (strArr.length > 999) {

			/*
			 * 自己使用不会出现这种状况 if(pkName == null || pkName.equals("")){ throw new
			 * BusinessException("传入的pkName为空请检查"); }
			 */

			int strArrLen = strArr.length;
			int fortimes = strArrLen / 1000;
			int subNums = strArrLen % 1000;
			// 循环每个1000的数据
			for (int x = 0; x < fortimes; x++) {
				for (int i = x * 1000, cnt = (2 * x + 1) * 500; i < cnt; i++) {
					sbf.append("'");
					sbf.append(strArr[i]);
					sbf.append("',");
				}
				sbf = new StringBuffer(sbf.substring(0, sbf.length() - 1));
				sbf.append(") or ");
				sbf.append(pkName);
				sbf.append(" in (");
				for (int i = (2 * x + 1) * 500, cnt = (x + 1) * 1000; i < cnt; i++) {
					sbf.append("'");
					sbf.append(strArr[i]);
					sbf.append("',");
				}
				sbf = new StringBuffer(sbf.substring(0, sbf.length()));
			}
			if (subNums != 0) {
				sbf = new StringBuffer(sbf.substring(0, sbf.length() - 1));
				sbf.append(") or ");
				sbf.append(pkName);
				sbf.append(" in (");
			}
			// 循环除1000后的余数
			for (int i = 1000 * fortimes; i < strArrLen; i++) {
				sbf.append("'");
				sbf.append(strArr[i]);
				sbf.append("',");
			}
			sbf = new StringBuffer(sbf.substring(0, sbf.length() - 1));
		} else {
			for (int i = 0, cnt = strArr.length; i < cnt; i++) {
				sbf.append("'");
				sbf.append(strArr[i]);
				sbf.append("',");
			}
			if (strArr.length > 0) {
				sbf = new StringBuffer(sbf.substring(0, sbf.length() - 1));
			} else {
				sbf = new StringBuffer("''");
			}
		}
		return sbf.toString();
	}
	/**
	 * 判断日期格式是否正确
	 * 
	 * @param date 待校验的日期
	 * @param type 需要校验的日期格式（yyyy-MM-dd / yyyy-MM-dd HH:mm:ss）
	 * @return
	 */
	public static boolean rightDate(String date, String type) {
		String regEx = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if ("yyyy-MM-dd HH:mm:ss".equals(type)) {
			regEx = "(((01[0-9]{2}|0[2-9][0-9]{2}|[1-9][0-9]{3})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|((01[0-9]{2}|0[2-9][0-9]{2}|[1-9][0-9]{3})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|((01[0-9]{2}|0[2-9][0-9]{2}|[1-9][0-9]{3})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((04|08|12|16|[2468][048]|[3579][26])00))-0?2-29))(20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d";
		}
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(date);
		boolean dateType = mat.matches();
		return dateType;
	}
	/**
	 * 将集合转换为sql的in中的内容，不考虑超过1000条情况
	 * @param pks
	 * @return
	 */
	public static String  convertListToSqlInPart(List<String> pks){
		if(pks==null||pks.size()<=0) return null;
		StringBuilder inpartStr = new StringBuilder("");
		for(String pk:pks){
			inpartStr.append("'").append(pk).append("',");
		}
		if(inpartStr!=null&&inpartStr.length()>0)
			return inpartStr.toString().substring(0,inpartStr.toString().length()-1);
		return null;
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

	/**
	 * 开始分页
	 * @param list
	 * @param pageNum 页码
	 * @param pageSize 每页多少条数据
	 * @return
	 */
	public static List startPage(List list, Integer pageNum,
								 Integer pageSize) {
		if (list == null) {
			return null;
		}
		if (list.size() == 0) {
			return null;
		}

		Integer count = list.size(); // 记录总数
		Integer pageCount = 0; // 页数
		if (count % pageSize == 0) {
			pageCount = count / pageSize;
		} else {
			pageCount = count / pageSize + 1;
		}

		int fromIndex = 0; // 开始索引
		int toIndex = 0; // 结束索引

		if (!pageNum.equals(pageCount)) {
			fromIndex = (pageNum - 1) * pageSize;
			toIndex = fromIndex + pageSize;
		} else {
			fromIndex = (pageNum - 1) * pageSize;
			toIndex = count;
		}

		List pageList = list.subList(fromIndex, toIndex);

		return pageList;
	}
}
