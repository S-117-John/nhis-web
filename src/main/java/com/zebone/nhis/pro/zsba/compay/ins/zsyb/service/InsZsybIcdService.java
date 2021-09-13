package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbIcd;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSt;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybIcdService {

	/**
	 * 保存
	 * @param param 实体对象数据
	 * @param user  登录用户
	 * @return InsZsybIcd 中山医保icd编码修改
	 */
	public InsZsBaYbIcd save(String param , IUser user){
		InsZsBaYbIcd entry = JsonUtil.readValue(param, InsZsBaYbIcd.class);
		if(entry!=null){
			if(entry.getFhz()==1){
				InsZsBaYbSt st = DataBaseHelper.queryForBean("select * from ins_st where del_flag = '0' and jzjlh = ?", InsZsBaYbSt.class, entry.getJzjlh());
				entry.setPkPi(st.getPkPi());
				entry.setPkPv(st.getPkPv());
				entry.setPkSettle(st.getPkSettle());
				entry.setEuPvtype(st.getEuPvtype());
				entry.setJsywh(st.getJsywh());
				DataBaseHelper.insertBean(entry);
			}else{
				throw new BusException("fhz为0，即医保修改失败，本地不保存！");
			}
		}else{
			throw new BusException("入参未null！");
		}
		return entry;
	}
	
	/**
	 * 汇总
	 * @param param 实体对象数据
	 * @param user  登录用户
	 * @return InsZsybIcd 中山医保icd编码修改
	 */
	public void Summary(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String ksrq = jo.getString("ksrq");
		String zzrq = jo.getString("zzrq");
		String yybh = jo.getString("yybh");
		String jbr = jo.getString("jbr");
		String hzywh = jo.getString("hzywh");
		String fhz = jo.getString("fhz");
		String msg = jo.getString("msg");
		
		JSONArray jsonArray = JSONArray.fromObject(jo.getString("insZsybIcdList"));
		
		for(int i=0; i<jsonArray.size(); i++){
			JSONObject obj = jsonArray.getJSONObject(i);
			InsZsBaYbIcd z = DataBaseHelper.queryForBean("select * from ins_zsyb_icd where del_flag = '0' and jzjlh = ?", InsZsBaYbIcd.class, obj.get("jzjlh"));
			z.setKsrq(DateUtils.strToDate(ksrq, "yyyyMMddHHmmss"));
			z.setZzrq(DateUtils.strToDate(zzrq, "yyyyMMddHHmmss"));
			z.setYybh(yybh);
			z.setJbr(jbr);
			z.setHzywh(hzywh);
			z.setFhz(Integer.parseInt(fhz));
			z.setMsg(msg);
			z.setFhbz(obj.getString("fhbz"));
			z.setFhrq(obj.getString("fhrq"));
			DataBaseHelper.updateBeanByPk(z, false);
		}
	}
}