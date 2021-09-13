package com.zebone.nhis.bl.pub.service;

import org.springframework.stereotype.Service;

import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.framework.support.IUser;

/**
 * 高值耗材计费服务
 * @author jd
 *
 */
@Service
public class HighConsumItemService {
	/**
	 * 010005002007
	 * 通过材料编码查询高值耗材中对应的收费项目
	 * @param param{"barcode":"材料编码"}
	 * @param user
	 * @return
	 */
	public String qryHightItemBybarcode(String param,IUser user){
		String code= "";
		Object res= ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "qryHightItemBybarcode", param);
		if(res!=null){
			code=(String)res;
		}
		return code;
	}
	
	
	/**
	 * 校验库存是否满足
	 * 010005002005
	 * @param param{paramList：{"oldId":"老系统项目编码";"pkDeptEx":"执行科室";"barcode":"材料编码";"quanCg":"需要数量"}}
	 * @param user
	 */
	public void checkHighValueConsum(String param,IUser user){
		ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "checkHighValueConsum", param);
	}
	
	/**
	 * 010005002013
	 * 校验执行科室是否启用高值耗材
	 * @param param
	 * @param user
	 * @return
	 */
	public int qryHighIsDo(String param,IUser user){
		int count=0;
	    Object res= ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "qryHighIsDo", param);
	    if(res!=null&&res instanceof Integer){
		   count=(int)res;
	    }
		return count;
	}
}
