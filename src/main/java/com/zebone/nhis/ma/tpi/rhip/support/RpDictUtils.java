package com.zebone.nhis.ma.tpi.rhip.support;

import com.zebone.nhis.ma.tpi.rhip.vo.PatListVo;

import java.util.Map;


public class RpDictUtils {

	//卡类型
	public static String getKlx(PatListVo pat){
		//01 健康卡 02 市民卡 03 社保卡 04 就诊卡 99 其他 
		String code="99";
		if(pat==null) return code;
		if(pat.getBarcode()!=null&&pat.getBarcode()!=""){
			code="02";//就诊卡
		}else if(pat.getInsurNo()!=null&&pat.getInsurNo()!=""){
			code="01";//社保卡
		}
		return code;
	}
	
	//卡号
	public static String getCardNo(PatListVo pat){
		String code="";
		if(pat==null) return code;
		if(pat.getBarcode()!=null&&pat.getBarcode()!=""){
			code=pat.getBarcode();//就诊卡
		}else if(pat.getHicNo()!=null&&pat.getHicNo()!=""){
			code=pat.getHicNo();//健康卡
		}else if(pat.getInsurNo()!=null&&pat.getInsurNo()!=""){
			code=pat.getInsurNo();//社保卡
		}
		return code;
	}

	/**
	 * 根据视图的科室类型获取科室的报告的编码和名称
	 * @param detpType
	 * @return
	 */
	public static String[] getbgksInfo(int detpType){

		String []bgskInfo = new String[2];
		switch(detpType){
		    case 0:
		    	bgskInfo[0] = "202040702";
		    	bgskInfo[1] = "CTMR影像科";
		        break;
		    case 1:
		    	bgskInfo[0] = "202040703";
				bgskInfo[1] = "放射介入科";
		        break;
		    case 2: bgskInfo[0] = "202040703";
				bgskInfo[1] = "放射介入科";
		        break;
		    case 3:
		    	bgskInfo[0] = "2020405";
				bgskInfo[1] = "病理科";
		        break;
		    case 5: bgskInfo[0] = "2020408";
				bgskInfo[1] = "超声科";
		        break;
		    case 6:  bgskInfo[0] = "2020302030302";
				bgskInfo[1] = "内镜中心";
		        break;
		    case 7:bgskInfo[0] = "2020409";
				bgskInfo[1] = "电生理科";
		        break;
		    case 9: bgskInfo[0] = "202040702";
				bgskInfo[1] = "CTMR影像科";
		        break;
		    case 13: bgskInfo[0] = "2020307";
				bgskInfo[1] = "产生诊断中心";
		        break;
		    case 14:bgskInfo[0] = "202030502";
				bgskInfo[1] = "生殖科";
		        break;
		    case 15: bgskInfo[0] = "202030502";
				bgskInfo[1] = "生殖科";
		        break;
		    case 17:  bgskInfo[0] = "2020403";
				bgskInfo[1] = "检验科";
		        break;
		}
		return bgskInfo;
	}
}
