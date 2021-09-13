package com.zebone.nhis.common.support;

import java.math.BigDecimal;
/**
 * 数字操作工具类
 * @author 
 *
 */
public class MathUtils {
	
	private static final int DEF_DIV_SCALE = 10;
	
	/**
	 * 向上取整
	 * @param d
	 * @return
	 */
	public static Double upRound(Double d){
		return Math.ceil(d);
	}
	  
	/** 
	* * 两个Double数相加 * 
	*  
	* @param v1 * 
	* @param v2 * 
	* @return Double 
	*/  
	public static Double add(Double v1, Double v2) {  
	   BigDecimal b1 = new BigDecimal(v1.toString());  
	   BigDecimal b2 = new BigDecimal(v2.toString());  
	   return new Double(b1.add(b2).doubleValue());  
	}  
	  
	/** 
	* * 两个Double数相减 * 
	*  
	* @param v1 * 
	* @param v2 * 
	* @return Double 
	*/  
	public static Double sub(Double v1, Double v2) {  
	   BigDecimal b1 = new BigDecimal(v1.toString());  
	   BigDecimal b2 = new BigDecimal(v2.toString());  
	   return new Double(b1.subtract(b2).doubleValue());  
	}  
	  
	/** 
	* * 两个Double数相乘 * 
	*  
	* @param v1 * 
	* @param v2 * 
	* @return Double 
	*/  
	public static Double mul(Double v1, Double v2) {  
	   BigDecimal b1 = new BigDecimal(v1.toString());  
	   BigDecimal b2 = new BigDecimal(v2.toString());  
	   return new Double(b1.multiply(b2).doubleValue());  
	}  
	  
	/** 
	* * 两个Double数相除 * 
	*  
	* @param v1 * 
	* @param v2 * 
	* @return Double 
	*/  
	public static Double div(Double v1, Double v2) {  
	   BigDecimal b1 = new BigDecimal(v1.toString());  
	   BigDecimal b2 = new BigDecimal(v2.toString());  
	   return new Double(b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)  
	     .doubleValue());  
	}  
	  
	/** 
	* * 两个Double数相除，并保留scale位小数 * 
	*  
	* @param v1 * 
	* @param v2 * 
	* @param scale * 
	* @return Double 
	*/  
	public static Double div(Double v1, Double v2, int scale) {  
	   if (scale < 0) {  
	    throw new IllegalArgumentException(  
	      "保留小数位不能小于0");  
	   }  
	   BigDecimal b1 = new BigDecimal(v1.toString());  
	   BigDecimal b2 = new BigDecimal(v2.toString());  
	   return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue());  
	}  
	/**
	 * 取绝对值
	 * @param a
	 * @return
	 */
	public static Double abs(Double a){
		return new Double(Math.abs(a.doubleValue()));
	}
	/**
	 * 判断浮点型是否相等
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean equ(Double d1,Double d2){
		BigDecimal c1 = new BigDecimal(d1);
	    BigDecimal c2 = new BigDecimal(d2);
	    if(c1.compareTo(c2)==0){
	    	return true;
	    }else{
	    	 return false;
	    }
	   
	}
	/**
	 * 判断浮点型是否相等
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int compareTo(Double d1,Double d2){
		BigDecimal c1 = new BigDecimal(d1);
	    BigDecimal c2 = new BigDecimal(d2);
	    return c1.compareTo(c2);
	}

	/**
	 * 双精度向上四舍五入
	 * @param d
	 * @param n 四舍五入到小数点后多少位
	 * @return
	 */
	public static double upRound(Double d,int n) {
		BigDecimal bd = new BigDecimal(d);
		return d == null?0:bd.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
