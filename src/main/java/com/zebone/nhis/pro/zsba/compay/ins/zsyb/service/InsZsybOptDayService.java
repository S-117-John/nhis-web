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
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybPvMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsOptDayData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbOptDay;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbPv;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybOptDayService {

	@Autowired
	private InsZsybPvMapper insPvMapper;

	@Autowired
	private InsZsybOptPbMapper insOptPbMapper;
	/**
	 * 获取日间手术维护初始数据
	 * @param param
	 * @param user
	 * @return
	 */
	public InsOptDayData getInsOptDay(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkInspv = jo.getString("pkInspv");
		String pkPv = jo.getString("pkPv");

		InsZsBaYbPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_inspv = ?", InsZsBaYbPv.class, pkInspv);	
		if(insPv==null){
			throw new BusException("参数错误，查不到医保入院信息！");
		}
		if(!insPv.getPkHp().trim().equals("2A")){
			throw new BusException("该医保不是日间手术类型，不需要维护日间手术资料！");
		}
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		InsOptDayData insOptDayData  = DataBaseHelper.queryForBean("select * from ins_opt_day where del_flag = '0' and pk_inspv = ?", InsOptDayData.class, pkInspv);
		if(insOptDayData==null){ //判断是否已维护
			insOptDayData = new InsOptDayData();
			insOptDayData.setJzjlh(insPv.getJzjlh());
			//获取出院ICD-10编码	
/*			List<Map<String,Object>> diagDataList = insPvMapper.getCyDiagData(param_h);
			for(int i=0; i<diagDataList.size(); i++){
				if(diagDataList.get(i).get("sort_no").toString().equals("1")){
					insOptDayData.setCyzdgjdm(diagDataList.get(i).get("diagcode").toString());//出院ICD-10编码	
				}
			}*/
		}
		//,获取手术日期
		List<Map<String,Object>> ssrqList = insOptPbMapper.getOperationDateData(param_h);
		List<String> list = new ArrayList();
		for(int i=0; i<ssrqList.size(); i++){
			if(ssrqList.get(i).get("date_plan")!=null){
				list.add(ssrqList.get(i).get("date_plan").toString().substring(0, 19));
			}
		}
		insOptDayData.setSsrqList(list);
		return insOptDayData;
	}
	
	/**
	 * 保存日间手术维护数据
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsBaYbOptDay saveInsOptDay(String param , IUser user){
		InsZsBaYbOptDay insOptDay = JsonUtil.readValue(param, InsZsBaYbOptDay.class);
		JSONObject jo = JSONObject.fromObject(param);
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ); 
		try {
			insOptDay.setSsrq(sdf.parse(jo.getString("ssrq")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(insOptDay.getPkOptday()==null){
			insOptDay.setEuPvtype("3");
			DataBaseHelper.insertBean(insOptDay);
		}else{
			insOptDay.setEuPvtype("3");
			DataBaseHelper.updateBeanByPk(insOptDay, false);
		}
		InsZsBaYbPv insPv = new InsZsBaYbPv();
		insPv.setPkInspv(insOptDay.getPkInspv());
		if(insOptDay.getFhz().equals("1")){
			insPv.setStatus("3");
		}else{
			insPv.setStatus("4");
		}
		DataBaseHelper.updateBeanByPk(insPv, false);
		return insOptDay;
	}
	
	/**
	 * 日间转普通
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsBaYbPv saveOptDayToPt(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String jzjlh = jo.getString("jzjlh");
		String pkHp = jo.getString("pkHp");
		
		//删除日间手术维护记录
		InsZsBaYbOptDay optDay = DataBaseHelper.queryForBean("select * from ins_opt_day where del_flag = '0' and jzjlh = ?", InsZsBaYbOptDay.class, jzjlh);	
		if(optDay!=null){
			optDay.setDelFlag("1");
			DataBaseHelper.updateBeanByPk(optDay, false);
		}
		
		Map<String, Object> hpMap = DataBaseHelper.queryForMap("select pk_hp from bd_hp where code='00021'");
		
		//修改医保登记信息的就诊类别
		InsZsBaYbPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and jzjlh= ?", InsZsBaYbPv.class, jzjlh);	
		insPv.setPkHp(pkHp);
		insPv.setPkInsu(hpMap.get("pkHp").toString());
		DataBaseHelper.updateBeanByPk(insPv, false);
		return insPv;
	}
}