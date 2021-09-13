package com.zebone.nhis.compay.ins.shenzhen.service.yd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.compay.ins.shenzhen.vo.yd.InsSzybStclear;
import com.zebone.nhis.compay.ins.shenzhen.vo.yd.InsSzybStclresult;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class YdYbClearService {

	/**
	 * 查询异地医保月度结算清分相关信息
	 * 015001011016
	 * @param param
	 * @param user
	 * @return
	 */
	 public List<Map<String,Object>> getClearData(String param, IUser user){
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = JsonUtil.readValue(param, List.class);
		List<String> ins = new ArrayList<String>();
		Map<String,List<String>> paramMap = new HashMap<String,List<String>>();
		Map<String,Integer> idexMap = new HashMap<String, Integer>();
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		if(list!=null&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				String ykc700 = (String) list.get(i).get("ykc700");
				ins.add(ykc700);
				idexMap.put(ykc700, i);
			}
			paramMap.put("ins", ins);
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append(" select v.pvcode_ins ykc700,pi.code_ip,case when pi.dt_idtype='01' then pi.id_no else null end card_no,pi.name_pi from ins_szyb_visit v ");
			sqlBuffer.append(" inner join pv_encounter pv on pv.pk_pv=v.pk_pv inner join pi_master pi on pi.pk_pi=v.pk_pi where v.pvcode_ins in ( :ins ) ");
			res = DataBaseHelper.queryForList(sqlBuffer.toString(), paramMap);
			if(res!=null&&res.size()>0){
				for (Map<String, Object> map : res) {
					String ykc700 = (String) map.get("ykc700");
					Integer index = idexMap.get(ykc700);
					if(index!=null){
						list.get(index).putAll(map);
					}
				}
			}		
		}		
		return list;
	 }
	 /**
	  * 保存异地医保清分结算信息
	  * 015001011019
	  * @param param
	  * @param user
	  */
	 public void saveYdYbStclear(String param, IUser user){
		 InsSzybStclear[] list = JsonUtil.readValue(param, InsSzybStclear[].class);
		 for (InsSzybStclear insSzybStclear : list) {
			 DataBaseHelper.insertBean(insSzybStclear);
		}
		 
	 }
	 /**
	  * 回退异地医保清分结算信息
	  * 015001011020
	  * @param param
	  * @param user
	  */
	 public void deleteYdYbStclear(String param, IUser user){
		 @SuppressWarnings("unchecked")
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 DataBaseHelper.execute("delete ins_szyb_stclear t where t.yzz060=? and t.yzz061=?", map.get("yzz060"),map.get("yzz061"));
	 }
	 
	 /**
	  * 保存审核说明汇总
	  * 015001011043
	  * @param param
	  * @param user
	  */
	 public void saveYdYbStclresult(String param, IUser user){
		 @SuppressWarnings("unchecked")
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 String flag = (String) map.get("flag");
		 //省外异地
		 if("03".equals(flag)){
			 String listStr = JsonUtil.writeValueAsString(map.get("list"));
			 InsSzybStclresult[] list = JsonUtil.readValue(listStr, InsSzybStclresult[].class);
			 for (InsSzybStclresult insSzybStclresult : list) {
				 insSzybStclresult.setEuHptype(flag);
				 DataBaseHelper.insertBean(insSzybStclresult);
			 }
		 }
		 
	 }
	 
	 public List<Map<String,Object>> getYdYbStclearData(String param, IUser user){
		 @SuppressWarnings("unchecked")
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select s.*,case when pi.dt_idtype='01' then pi.id_no else null end card_no,pi.name_pi,pi.code_ip,s.AAC044 as aac002 from ins_szyb_stclear s LEFT JOIN ins_szyb_visit v on s.YKC700=v.pvcode_ins ");
			sqlBuffer.append("LEFT join pv_encounter pv on pv.pk_pv=v.pk_pv LEFT join pi_master pi on pi.pk_pi=v.pk_pi  where s.yzz060=? and s.yzz061=?");
			list = DataBaseHelper.queryForList(sqlBuffer.toString(), map.get("yzz060"),map.get("yzz061"));
			if(list!=null&&list.size()>0){
			return list;}else
			{return null;}
	 }
}
