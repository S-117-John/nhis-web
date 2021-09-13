package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InsPvWi;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InsStWi;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InsStWiDetails;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InsStWiList;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.FundList120206;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 外部医保-省内工伤医保住院结算
 * @author lipz
 *
 */
@Service
public class InsStWiService {
	
	
	/**
	 * 省内工伤医保 - 获取就医登记号进行医保住院结算
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> getWiJydjh(String param , IUser user){
		Map<String,Object> result = new HashMap<String, Object>();
		
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		
		if(StringUtils.isEmpty(pkPv)){
			throw new BusException("就诊主键pkPv不能为空！");
		}
		
		InsPvWi insPvWi = DataBaseHelper.queryForBean("select * from ins_pv_wi where del_flag='0' and pk_pv=?", InsPvWi.class, new Object[]{pkPv});
		if(insPvWi==null){
			throw new BusException("通过主键pkPv="+pkPv+"，未找到登记记录！");
		}
		result.put("aaz218", insPvWi.getAaz218());//就医登记号
		result.put("pkInsu", insPvWi.getPkInsu());//医保主计划主键 
		result.put("startTime", insPvWi.getBka017());//住院时间
		result.put("endTime", insPvWi.getBka032());//出院日期
		result.put("pkInspvwi", insPvWi.getPkInspvwi());//登记主键
		return result;
	}

	/**
	 * 省内工伤医保 - 保存医保结算数据<出院登记时也会保存预结算数据>
	 * @param param
	 * @param user
	 * @return
	 */
	public InsStWi saveWiStData(String param , IUser user){
		InsStWi st = JsonUtil.readValue(param, InsStWi.class);
		if(st==null || StringUtils.isEmpty(st.getAaz218())){
			throw new BusException("就医登记号aaz218不能为空！");
		}
		InsPvWi insPvWi = DataBaseHelper.queryForBean("select * from ins_pv_wi where del_flag='0' and aaz218=?", InsPvWi.class, new Object[]{st.getAaz218()});
		if(insPvWi==null){
			throw new BusException("通过主键aaz218="+st.getAaz218()+"，未找到登记记录！");
		}
		// 根据 就医登记号 先删除已结算过的数据
		InsStWi oldSt = DataBaseHelper.queryForBean("select * from ins_st_wi where del_flag='0' and aaz218=?", InsStWi.class, new Object[]{st.getAaz218()});
		if(oldSt!=null){
			//因为正式结算没有这两项，所有需从上一条记录中取值
			st.setBka031(oldSt.getBka031());
			st.setBkf004(oldSt.getBkf004());
			
			DataBaseHelper.execute(" delete from ins_st_wi where pk_insstwi=?", new Object[]{oldSt.getPkInsstwi()});
		}
		
		// 根据  登记记录 设置结算数据
		st.setPkInsu(insPvWi.getPkInsu());
		st.setPkPi(insPvWi.getPkPi());
		st.setPkPv(insPvWi.getPkPv());
		st.setEuPvtype(insPvWi.getEuPvtype());
		
		ApplicationUtils.setDefaultValue(st, true);
		DataBaseHelper.insertBean(st);
		insPvWi.setStatus("11");
		DataBaseHelper.updateBeanByPk(insPvWi);
		return st;
	}
	
	/**
	 * 省内工伤医保 - 获取结算数据
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InsStWi queryWiStData(String param , IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkPv = paramMap.get("pkPv");
		if(StringUtils.isEmpty(pkPv)){
			throw new BusException("患者就诊主键不能为空！");
		}
		InsStWi st = DataBaseHelper.queryForBean("select * from ins_st_wi where pk_pv=? and del_flag = '0'", InsStWi.class, new Object[]{pkPv});
		return st;
	}
	
	/**
	 * 保存结算单数据
	 * @param param
	 * @param user
	 */
	public void saveWiStListData(String param , IUser user){
		InsStWiList insStWiList = JsonUtil.readValue(param, InsStWiList.class);
		List<InsStWiDetails> fundList = insStWiList.getFundInfo().getFundList();
		
		if(StringUtils.isEmpty(insStWiList.getAaz218())){
			throw new BusException("就医登记号aaz218不能为空！");
		}
		InsStWi st = DataBaseHelper.queryForBean("select * from ins_st_wi where aaz218=? and del_flag = '0'", InsStWi.class, new Object[]{insStWiList.getAaz218()});
		InsStWiList old = DataBaseHelper.queryForBean("select * from ins_st_wi_list where aaz218=? and del_flag = '0'", InsStWiList.class, new Object[]{insStWiList.getAaz218()});
		if(old!=null){
			DataBaseHelper.execute("delete from ins_st_wi_list where pk_insstwilist=?", new Object[]{old.getPkInsstwilist()});
			DataBaseHelper.execute("delete from ins_st_wi_details where pk_insstwilist=?", new Object[]{old.getPkInsstwilist()});
		}
		insStWiList.setPkInsu(st.getPkInsu());
		insStWiList.setPkPi(st.getPkPi());
		insStWiList.setPkPv(st.getPkPv());
		insStWiList.setPkSettle(st.getPkSettle());
		insStWiList.setEuPvtype(st.getEuPvtype());
		ApplicationUtils.setDefaultValue(insStWiList, true);
		DataBaseHelper.insertBean(insStWiList);
		
		for(InsStWiDetails d : fundList){
			d.setPkInsstwilist(insStWiList.getPkInsstwilist());
			ApplicationUtils.setDefaultValue(d, true);
			DataBaseHelper.insertBean(d);
		}
	}
	
	
}
