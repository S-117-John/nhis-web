package com.zebone.nhis.webservice.syx.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.vo.piAddr.AddrReq;
import com.zebone.nhis.webservice.syx.vo.piAddr.PiAddrReq;
import com.zebone.nhis.webservice.syx.vo.piAddr.ResponseXML;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * 康美（中药饮片调配） 患者地址注册 接口
 */
@Service
public class PiAddrKMService {

	public String registerAddr(String requestXML) {
		// 返回码    5，数据存储异常 ；0，成功；
		int res = 5;
		String description="";
		String status="";
		
		// XML转对象
		PiAddrReq piAddrReq = (PiAddrReq) XmlUtil.XmlToBean(requestXML, PiAddrReq.class);
		AddrReq addrReq = piAddrReq.getAddrReq();
		
		// 执行SQL
		String sql = "insert into pi_address(dt_addrtype,pk_pi,pk_addr,name_prov,name_city,name_dist,addr,name_rel,tel,amt_fee,ts,create_time)" + 
		"select (select code from bd_defdoc where code_defdoclist='000014' and flag_def='1'),pi.pk_pi," 
		+ "?,?,?,?,?,?,?,?,?,? from pi_master pi where pi.code_pi=? and pi.name_pi=?";
		Object[] obj = { NHISUUID.getKeyId(), addrReq.getProvinces(), addrReq.getCity(), addrReq.getZone(), addrReq.getAddrDetail(), addrReq.getConsignee(), addrReq.getConTel(), addrReq.getFee(), new Date(), new Date(), addrReq.getTreatCard(), addrReq.getUsername()};
		try {
			int count = DataBaseHelper.execute(sql, obj);
			res = count > 0 ? 0 : res;
			description="成功";
			status="sucess";
		} catch (Exception e) {
			description="失败";
			status="failed";
		}
		
		//组装响应xml
		ResponseXML responseXML = new ResponseXML();
		responseXML.setDescription(description);
		responseXML.setResultCode(res+"");
		responseXML.setStatus(status);
		String xml = XmlUtil.beanToXml(responseXML, ResponseXML.class);
		return xml;
	}
}
