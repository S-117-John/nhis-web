package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybBearMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbBear;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbPv;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybBearService {
	
	@Autowired
	private InsZsybBearMapper insBearMapper;

	/**
	 * 获取生育资料维护初始数据
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsBaYbBear getInsBear(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkInspv = jo.getString("pkInspv");
		InsZsBaYbPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_inspv = ?", InsZsBaYbPv.class, pkInspv);	
		if(insPv==null){
			throw new BusException("参数错误，查不到医保入院信息！");
		}
		if(!insPv.getPkHp().trim().equals("41")){
			throw new BusException("该医保不是生育住院，不需要维护生育资料！");
		}
		InsZsBaYbBear insBear = DataBaseHelper.queryForBean("select * from ins_bear where del_flag = '0' and pk_inspv = ?", InsZsBaYbBear.class, pkInspv);
		if(insBear==null){
			insBear = new InsZsBaYbBear();
			insBear.setJzjlh(insPv.getJzjlh());
			
			// TODO: 这里需要王哥提供妇幼系统的接口获取排胎时间
			PiMaster pm = DataBaseHelper.queryForBean("select * from pi_master where del_flag = '0' and pk_pi = ?", PiMaster.class, insPv.getPkPi());
			Map<String,Object> param_h = new HashMap<String,Object>();
			param_h.put("codeIp", pm.getCodeIp());
			Map<String,Object> map =insBearMapper.getWcFmrq(param_h);
			Date ptSj = new Date();
			if(map!=null && map.containsKey("fm_date")){
				String mapDte = map.get("fm_date").toString();
				ptSj = DateUtils.strToDate(mapDte, "yyyy-MM-dd HH:mm:ss");
			}
			insBear.setPtsj(ptSj);
		}
		return insBear;
	}
	
	/**
	 * 保存生育资料维护初始数据
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsBaYbBear saveInsBear(String param , IUser user){
		InsZsBaYbBear insBear = JsonUtil.readValue(param, InsZsBaYbBear.class);
		
		if(insBear.getPkInspv()==null){
			throw new BusException("参数错误，pkInspv不能为空！");
		}
		// 生成生育资料
		if(insBear.getPkInsbear()==null){
			DataBaseHelper.insertBean(insBear);
		}else{
			DataBaseHelper.updateBeanByPk(insBear, false);
		}
		// 更新中山住院医保登记
		InsZsBaYbPv insPv = new InsZsBaYbPv();
		insPv.setPkInspv(insBear.getPkInspv());
		if(insBear.getFhz().equals("1")){
			insPv.setStatus("3");
		}else{
			insPv.setStatus("4");
		}
		if(StringUtils.isNotEmpty(insBear.getSylb())){
			insPv.setSylb(insBear.getSylb());
		}
		DataBaseHelper.updateBeanByPk(insPv, false);
		
		return insBear;
	}

}