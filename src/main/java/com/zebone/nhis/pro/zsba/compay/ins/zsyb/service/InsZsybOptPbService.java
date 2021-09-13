package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybOptPbMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsOptPbData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbOptPb;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbPv;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybOptPbService {
	
	@Autowired
	private InsZsybOptPbMapper insOptPbMapper;


	/**
	 * 获取计生手术维护初始数据
	 * @param param
	 * @param user
	 * @return
	 */
	public InsOptPbData getInsOptPb(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkInspv = jo.getString("pkInspv");
		//判断是否已入院登记
		InsZsBaYbPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_inspv = ?", InsZsBaYbPv.class, pkInspv);	
		if(insPv==null){
			throw new BusException("参数错误，查不到医保入院信息！");
		}
		if(!insPv.getPkHp().trim().equals("47")){
			throw new BusException("该医保不是生育住院，不需要维护生育资料！");
		}
		//判断是否已维护	
		InsOptPbData insOptPbData = DataBaseHelper.queryForBean("select * from ins_opt_pb where del_flag = '0' and pk_inspv = ?", InsOptPbData.class, pkInspv);	
		if(insOptPbData==null){
			insOptPbData = new InsOptPbData();
			insOptPbData.setJzjlh(insPv.getJzjlh());
		}
		//未维护,获取手术日期
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkPv", insPv.getPkPv());
		List<Map<String,Object>> ssrqList = insOptPbMapper.getOperationDateData(map);
		List<String> list = new ArrayList();
		for(int i=0; i<ssrqList.size(); i++){
			if(ssrqList.get(i).get("date_plan")!=null){
				list.add(ssrqList.get(i).get("date_plan").toString().substring(0,19));
			}
		}
		insOptPbData.setSsrqList(list);
		return insOptPbData;
	}
	
	/**
	 * 保存计生手术维护初始数据
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsBaYbOptPb saveInsOptPb(String param , IUser user){
		InsZsBaYbOptPb insOptPb = JsonUtil.readValue(param, InsZsBaYbOptPb.class);
		JSONObject jo = JSONObject.fromObject(param);
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ); 
		try {
			insOptPb.setSsrq(sdf.parse(jo.getString("ssrq")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(insOptPb.getPkOptpb()==null){
			DataBaseHelper.insertBean(insOptPb);
		}else{
			DataBaseHelper.updateBeanByPk(insOptPb, false);
		}
		InsZsBaYbPv insPv = new InsZsBaYbPv();
		insPv.setPkInspv(insOptPb.getPkInspv());
		if(insOptPb.getFhz().equals("1")){
			insPv.setStatus("3");
		}else{
			insPv.setStatus("4");
		}
		DataBaseHelper.updateBeanByPk(insPv, false);
		return insOptPb;
	}
}