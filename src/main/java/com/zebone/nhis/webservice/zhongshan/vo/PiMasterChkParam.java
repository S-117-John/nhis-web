package com.zebone.nhis.webservice.zhongshan.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.adt.vo.PiMasterBa;
import com.zebone.nhis.webservice.zhongshan.support.ErrorMsg;

public class PiMasterChkParam extends PiMasterBa {
	
		private String func_id;//功能编号
		
		private String ope_time;//操作时间【文本】
		
		private String ope_code;//操作人工号 
		
		private Date opeTime;//操作时间【时间】

		private String birthDateTxt;//操作时间【文本】
		
		public String toChkString() {
			if(StringUtils.isEmpty(func_id)) return ErrorMsg.func_id_Null;
			if(StringUtils.isEmpty(ope_code)) return "操作人工号 ope_code 为空!";
			if(null == ope_time) return "操作时间 ope_time 为空 / 时间格式不对";
			if(StringUtils.isEmpty(getNamePi())|| StringUtils.isEmpty(getNamePi().trim())) return "患者姓名 namePi 为空!";		
			if(StringUtils.isEmpty(getDtSex())) return "患者性别 dtSex 为空!";		
			if(StringUtils.isEmpty(getDtIdtype())) return "证件类型 dtIdtype 为空!";		
			if(StringUtils.isEmpty(getIdNo()) || StringUtils.isEmpty(getIdNo().trim())) return "证件号 idNo 为空!";	
			if(StringUtils.isEmpty(birthDateTxt)) return "出生日期 birthDate 为空 / 时间格式不对";

			// 1-男【02】，2-女【03】, 9-未知【】转换性别
			if("1".endsWith(getDtSex()))
				setDtSex("02");
			else if("2".endsWith(getDtSex()))
				setDtSex("03");
			else if("9".endsWith(getDtSex()))
				setDtSex("04");
			else
				return "患者性别 dtSex 不对【1-男，2-女，9-未知】";
			
			//2 - 处理出生年月日
			try {
				Date da = DateUtils.parseDate(this.birthDateTxt, "yyyy-MM-dd");
				setBirthDate(da);
			} catch (ParseException e) {
				e.printStackTrace();
				return "出生日期，格式不对【正确格式：yyyy-MM-dd】";
			}
			
			//3-校验身份证、出生日期、性别
			String birTxt = "";
			String sexTxt = "";
			if(getIdNo().trim().length() == 18){
				birTxt = getIdNo().substring(6,14);
				sexTxt = getIdNo().substring(16,17);
			}else if(getIdNo().trim().length() == 15){
				birTxt = getIdNo().substring(6,14);
				sexTxt = getIdNo().substring(14,15);
			}else 
				return "身份证格式不对，请核查！！！";
			if(!birTxt.equals(birthDateTxt.replaceAll("-", "")))
				return "出生日期 与身份证提取出生日期不一致";
			int sexT = Integer.parseInt(sexTxt);
			if((sexT%2 == 0 && !"03".endsWith(getDtSex())) //双-女，单-男
					|| (sexT%2 != 0 && !"02".endsWith(getDtSex())) )
				return "性别 与身份证提取不一致";
			
			//4-处理操作日期时间
			try {
				Date daOp = DateUtils.parseDate(this.ope_time, "yyyy-MM-dd HH:mm");
				setOpeTime(daOp); 
			} catch (ParseException e) {
				e.printStackTrace();
				return "操作时间格式不对【正确：yyyy-MM-dd HH:mm】";
			}
			return null;
		}
		
		/**
		 * 返回某种格式的时间
		 * @param formatStyle 时间的格式
		 * @param dateTime 时间
		 * @return
		 */
		public String getFormatDate( String formatStyle, Date dateTime){
			SimpleDateFormat format = new SimpleDateFormat(formatStyle);
			return format.format(dateTime);
		}

		public String getFunc_id() {
			return func_id;
		}
		public void setFunc_id(String func_id) {
			this.func_id = func_id;
		}
		public String getOpe_code() {
			return ope_code;
		}
		public void setOpe_code(String ope_code) {
			this.ope_code = ope_code;
		}

		public Date getOpeTime() {
			return opeTime;
		}

		public void setOpeTime(Date opeTime) {
			this.opeTime = opeTime;
		}

		public String getBirthDateTxt() {
			return birthDateTxt;
		}

		public void setBirthDateTxt(String birthDateTxt) {
			this.birthDateTxt = birthDateTxt;
		}

		public void setOpe_time(String ope_time) {
			this.ope_time = ope_time;
		}
		
}
