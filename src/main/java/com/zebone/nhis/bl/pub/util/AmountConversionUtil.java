package com.zebone.nhis.bl.pub.util;

public class AmountConversionUtil {
	
	/**
	 * 金额转大写
	 * @param 
	 * @return
	 */
	public static String change(double v) {  
		String UNIT = "佰拾万仟佰拾元角分";  
		String DIGIT = "零壹贰叁肆伍陆柒捌玖";  
		double MAX_VALUE = 9999999.99D;  
        if (v < 0 || v > MAX_VALUE) {  
            return "参数非法!";  
        }  
        long l = Math.round(v * 100);  
        if (l == 0) {  
            return "零元";  
        }  
        String strValue = l + "";  
        // i用来控制数  
        int i = 0;  
        // j用来控制单位  
        int j = UNIT.length() - strValue.length();  
        String rs = "";  
        boolean isZero = false;  
        for (; i < strValue.length(); i++, j++) {  
            char ch = strValue.charAt(i);  
            if (ch == '0') {  
                isZero = true;  
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万'  
                        || UNIT.charAt(j) == '元') {  
                    rs = rs + UNIT.charAt(j);  
                    isZero = false;  
                }  
            } else {  
                if (isZero) {  
                    rs = rs + "零";  
                    isZero = false;  
                }  
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);  
            }  
        }  
/*      if (!rs.endsWith("分")) {  
          rs = rs + "整";  
      }  */
        rs = rs.replaceAll("亿万", "亿");  
        return rs;  
    }  
	
	/**
	 * 金额转大写，仅限住院发票使用，上限不得>=一百万
	 * @param v
	 * @return
	 */
	public static String changeInv(double v) {  
		String UNIT = "拾万仟佰拾元角分";  
		String DIGIT = "零壹贰叁肆伍陆柒捌玖";  
		double MAX_VALUE = 999999.99D;  
        if (v < 0 || v > MAX_VALUE) {  
            return "参数非法!";  
        }  
        long l = Math.round(v * 100);  
        if (l == 0) {  
            return "零拾零万零仟零佰零拾零元零角零分";  
        }  
        String strValue = l + "";  
        // i用来控制数  
        int i = 0;  
        // j用来控制单位  
        int j = UNIT.length() - strValue.length();  
        String rs = "";  
        //boolean isZero = false;  
        for (; i < strValue.length(); i++, j++) {  
            char ch = strValue.charAt(i);  
            if (ch == '0') {  
            	rs = rs + "零" + UNIT.charAt(j);  
/*                isZero = true;  
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万'  
                        || UNIT.charAt(j) == '元') {  
                    rs = rs + UNIT.charAt(j);  
                    isZero = false;  
                }  */
            } else {  
/*                if (isZero) {  
                    rs = rs + "零";  
                    isZero = false;  
                }  */
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);  
            }  
        }  
        //补数，前补到十万，后补到分
        if(rs.substring(rs.length()-1, rs.length()).equals("元")){
        	rs = rs+"零角零分";
        }
        if(rs.substring(rs.length()-1, rs.length()).equals("角")){
        	rs = rs+"零分";
        }
        if(rs.substring(1, 2).equals("拾")&&rs.substring(3, 4).equals("万")){
        	//十万不用处理
        }else if(rs.substring(1, 2).equals("万")){
        	rs = "零拾"+rs;
        }else if(rs.substring(1, 2).equals("仟")){
        	rs = "零拾零万"+rs;
        }else if(rs.substring(1, 2).equals("佰")){
        	rs = "零拾零万零仟"+rs;
        }else if(rs.substring(1, 2).equals("拾")){
        	rs = "零拾零万零仟零佰"+rs;
        }else if(rs.substring(1, 2).equals("元")){
        	rs = "零拾零万零仟零佰零拾"+rs;
        }else if(rs.substring(1, 2).equals("角")){
        	rs = "零拾零万零仟零佰零拾零元"+rs;
        }else if(rs.substring(1, 2).equals("分")){
        	rs = "零拾零万零仟零佰零拾零元零角"+rs;
        }
//      if (!rs.endsWith("分")) {  
//          rs = rs + "整";  
//      }  
        rs = rs.replaceAll("亿万", "亿");  
        return rs;  
    }  
	
	/**
	 * 金额转大写，仅限住院押金单使用，上限不得>=一千万
	 * @param v
	 * @return
	 */
	public static String changePrePay(double v) {  
		String UNIT = "佰拾万仟佰拾元角分";  
		String DIGIT = "零壹贰叁肆伍陆柒捌玖";  
		double MAX_VALUE = 9999999.99D;  
        if (v < 0 || v > MAX_VALUE) {  
            return "参数非法!";  
        }  
        long l = Math.round(v * 100);  
        if (l == 0) {  
            return "零佰零拾零万零仟零佰零拾零元零角零分";  
        }  
        String strValue = l + "";  
        // i用来控制数  
        int i = 0;  
        // j用来控制单位  
        int j = UNIT.length() - strValue.length();  
        String rs = "";  
        //boolean isZero = false;  
        for (; i < strValue.length(); i++, j++) {  
            char ch = strValue.charAt(i);  
            if (ch == '0') {  
            	rs = rs + "零" + UNIT.charAt(j);  
/*                isZero = true;  
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万'  
                        || UNIT.charAt(j) == '元') {  
                    rs = rs + UNIT.charAt(j);  
                    isZero = false;  
                }  */
            } else {  
/*                if (isZero) {  
                    rs = rs + "零";  
                    isZero = false;  
                }  */
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);  
            }  
        }  
        //补数，前补到百万，后补到分
        if(rs.substring(rs.length()-1, rs.length()).equals("元")){
        	rs = rs+"零角零分";
        }
        if(rs.substring(rs.length()-1, rs.length()).equals("角")){
        	rs = rs+"零分";
        }
        if(rs.substring(1, 2).equals("拾")&&rs.substring(3, 4).equals("万")){
        	rs = "零佰"+rs;
        }else if(rs.substring(1, 2).equals("万")){
        	rs = "零佰零拾"+rs;
        }else if(rs.substring(1, 2).equals("仟")){
        	rs = "零佰零拾零万"+rs;
        }else if(rs.substring(1, 2).equals("佰")){
        	rs = "零佰零拾零万零仟"+rs;
        }else if(rs.substring(1, 2).equals("拾")){
        	rs = "零佰零拾零万零仟零佰"+rs;
        }else if(rs.substring(1, 2).equals("元")){
        	rs = "零佰零拾零万零仟零佰零拾"+rs;
        }else if(rs.substring(1, 2).equals("角")){
        	rs = "零佰零拾零万零仟零佰零拾零元"+rs;
        }else if(rs.substring(1, 2).equals("分")){
        	rs = "零佰零拾零万零仟零佰零拾零元零角"+rs;
        }
//      if (!rs.endsWith("分")) {  
//          rs = rs + "整";  
//      }  
        rs = rs.replaceAll("亿万", "亿");  
        return rs;  
    }  
}
