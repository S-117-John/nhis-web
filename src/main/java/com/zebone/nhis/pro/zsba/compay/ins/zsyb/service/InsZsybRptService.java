package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptCqjcMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptGsmzMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptGszyMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptJhsyMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptLxMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptMzMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptMzsdMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptMzsdMzMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptMzyfMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptSyzyMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptSyzybxMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptTdbzMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybRptZyMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.DelYbMxParam;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRpt;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptCqjc;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptGsmz;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptGszy;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptJhsy;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptLx;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptMz;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptMzsd;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptMzsdMz;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptMzyf;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptSyzy;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptSyzybx;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptTdbz;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptZy;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSt;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.MonthSettleData;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybRptService {
	
	@Autowired
	private InsZsybRptMapper insZsybRptMapper;
	@Autowired
	private InsZsybRptMzMapper insZsybRptMzMapper;
	@Autowired
	private InsZsybRptZyMapper insZsybRptZyMapper;
	@Autowired
	private InsZsybRptLxMapper insZsybRptLxMapper;
	@Autowired
	private InsZsybRptTdbzMapper insZsybRptTdbzMapper;
	@Autowired
	private InsZsybRptGsmzMapper insZsybRptGsmzMapper;
	@Autowired
	private InsZsybRptGszyMapper insZsybRptGszyMapper;
	@Autowired
	private InsZsybRptSyzyMapper insZsybRptSyzyMapper;
	@Autowired
	private InsZsybRptMzsdMapper insZsybRptMzsdMapper;
	@Autowired
	private InsZsybRptMzyfMapper insZsybRptMzyfMapper;
	@Autowired
	private InsZsybRptMzsdMzMapper insZsybRptMzsdMzMapper;
	@Autowired
	private InsZsybRptSyzybxMapper insZsybRptSyzybxMapper;
	@Autowired
	private InsZsybRptJhsyMapper insZsybRptJhsyMapper;
	@Autowired
	private InsZsybRptCqjcMapper insZsybRptCqjcMapper;


	/**
	 * 保存医保月结算明细
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveMonthSettleDetails(String param , IUser user){
		MonthSettleData monthSettleData = JsonUtil.readValue(param, MonthSettleData.class);
		
		InsZsBaYbRpt  rpt = new InsZsBaYbRpt();
		rpt.setTjlb(monthSettleData.getTjlb());
		rpt.setTjjzrq(monthSettleData.getTjjzrq());
		rpt.setFhz(monthSettleData.getFhz());
		rpt.setMsg(monthSettleData.getMsg());
		rpt.setJjywh(monthSettleData.getJjywh());
		
		DataBaseHelper.insertBean(rpt);
		
		String sql = "";
		List<Map<String, Object>> mapList = null;
		switch(monthSettleData.getTjlb())
		{
		case "1"://中山医保门诊月结明细   
			for(InsZsBaYbRptMz mx : monthSettleData.getInsZsybRptMzList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
			}
			break;
		case "2"://中山医保住院医疗月结明细
			sql = "select t1.pk_insst,  t1.jsywh, t2.grsxh, t2.cyrq, t1.ylfyze, (t1.gzzfuje+t1.gzzfeje) grzhzfje,  t1.xjzfuje, t1.xjzfeje, t1.tczfje from ins_st t1 inner join ins_pv t2 on t1.pk_inspv = t2.pk_inspv where substr(to_char(t1.jsrq,'YYYYMMDD'),1,6) = ?  and t1.del_flag = '0' and t1.jsbz = '1'";
			mapList = DataBaseHelper.queryForList(sql,  monthSettleData.getTjjzrq().substring(0, 6));
			for(InsZsBaYbRptZy mx : monthSettleData.getInsZsybRptZyList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
				int selected = 0;
				boolean flag = false;
				for(int i=0; i<mapList.size(); i++){
					Map<String, Object> map = mapList.get(i);
					if(map.get("grsxh").equals(mx.getGrsxh())
							&&map.get("cyrq").toString().replace("-", "").replace(":", "").replace(" ", "").substring(0, 14).equals(mx.getCyrq())
							&&Double.parseDouble(map.get("ylfyze").toString())==mx.getYlfyze().doubleValue()
							&&Double.parseDouble(map.get("grzhzfje").toString())==mx.getGrzhzfje().doubleValue()
							&&Double.parseDouble(map.get("xjzfuje").toString())==mx.getGrzfje().doubleValue()
							&&Double.parseDouble(map.get("xjzfeje").toString())==mx.getZfeije().doubleValue()
							&&Double.parseDouble(map.get("tczfje").toString())==mx.getTczfje().doubleValue()){
						selected = i;
						flag = true;
						break;
					}
				}
				if(flag == true){
					InsZsBaYbSt st = new InsZsBaYbSt();
					st.setPkInsst(mapList.get(selected).get("pkInsst").toString());
					st.setTjlb(monthSettleData.getTjlb());
					st.setTjjzrq(monthSettleData.getTjjzrq());
					st.setJjywh(monthSettleData.getJjywh());
					DataBaseHelper.updateBeanByPk(st, false);
				}else{
					//暂不处理
				}
			}
			break;
		case "4"://中山医保离休月结明细
			for(InsZsBaYbRptLx mx : monthSettleData.getInsZsybRptLxList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
			}
			sql = "update ins_st set tjlb = ?, tjjzrq = ?, jjywh=? where substr(to_char(jsrq,'YYYYMMDD'),1,6) = ?  and del_flag = '0' and rylb = '3' and jsbz = '1'";
			DataBaseHelper.execute(sql, monthSettleData.getTjlb(), monthSettleData.getTjjzrq(), monthSettleData.getJjywh(), monthSettleData.getTjjzrq());
			break;
		case "6"://中山医保特定病种月结明细
			for(InsZsBaYbRptTdbz mx : monthSettleData.getInsZsybRptTdbzList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
			}
			break;
			//门诊的 不用插数据到住院结算表
		case "9"://中山医保工伤门诊月结明细
			for(InsZsBaYbRptGsmz mx : monthSettleData.getInsZsybRptGsmzList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
			}
			break;
			//门诊的 不用插数据到住院结算表
		case "10"://中山医保工伤住院月结明细
			sql = "select t1.pk_insst,  t2.grsxh, t2.ryrq, t2.cyrq, t1.ylfyze,  t1.xjzfeje, t1.tczfje from ins_st t1 inner join ins_pv t2 on t1.pk_inspv = t2.pk_inspv where substr(to_char(t1.jsrq,'YYYYMMDD'),1,6) = ?  and t1.del_flag = '0' and t1.jsbz = '1'";
			mapList = DataBaseHelper.queryForList(sql,  monthSettleData.getTjjzrq().substring(0, 6));
			for(InsZsBaYbRptGszy mx : monthSettleData.getInsZsybRptGszyList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
				
				int selected = 0;
				boolean flag = false;
				for(int i=0; i<mapList.size(); i++){
					Map<String, Object> map = mapList.get(i);
					if(map.get("grsxh").equals(mx.getGrsxh())
							&&map.get("ryrq").toString().substring(0, 14).replace("-", "").replace(":", "").replace(" ", "").equals(mx.getRyrq())
							&&map.get("cyrq").toString().substring(0, 14).replace("-", "").replace(":", "").replace(" ", "").equals(mx.getCyrq())
							&&Double.parseDouble(map.get("ylfyze").toString())==mx.getYlfyze().doubleValue()
							&&Double.parseDouble(map.get("xjzfeje").toString())==mx.getZfeize().doubleValue()
							&&Double.parseDouble(map.get("tczfje").toString())==mx.getTczfje().doubleValue()){
						selected = i;
						flag = true;
					}
				}
				if(flag == true){
					InsZsBaYbSt st = new InsZsBaYbSt();
					st.setPkInsst(mapList.get(selected).get("pkInsst").toString());
					st.setTjlb(monthSettleData.getTjlb());
					st.setTjjzrq(monthSettleData.getTjjzrq());
					st.setJjywh(monthSettleData.getJjywh());
					DataBaseHelper.updateBeanByPk(st, false);
				}else{
					//暂不处理,一般都会找到对应的结算数据，这个else是预留的
				}
			}
			break;
		case "13"://中山医保生育住院月结明细
			sql = "select t1.pk_insst,  t2.grsxh, t2.ryrq, t2.cyrq, t1.ylfyze, (t1.gzzfuje+t1.gzzfeje) grzhzfje, t1.xjzfeje, t1.tczfje from ins_st t1 inner join ins_pv t2 on t1.pk_inspv = t2.pk_inspv where substr(to_char(t1.jsrq,'YYYYMMDD'),1,6) = ?  and t1.del_flag = '0' and t1.jsbz = '1'";
			mapList = DataBaseHelper.queryForList(sql,  monthSettleData.getTjjzrq().substring(0, 6));
			for(InsZsBaYbRptSyzy mx : monthSettleData.getInsZsybRptSyzyList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
				
				int selected = 0;
				boolean flag = false;
				for(int i=0; i<mapList.size(); i++){
					Map<String, Object> map = mapList.get(i);
					if(map.get("grsxh").equals(mx.getGrsxh())
							&&map.get("ryrq").toString().substring(0, 14).replace("-", "").replace(":", "").replace(" ", "").equals(mx.getRyrq())
							&&map.get("cyrq").toString().substring(0, 14).replace("-", "").replace(":", "").replace(" ", "").equals(mx.getCyrq())
							&&Double.parseDouble(map.get("ylfyze").toString())==mx.getYlfyze().doubleValue()
							&&Double.parseDouble(map.get("grzhzfje").toString())==mx.getGrzhzf().doubleValue()
							&&Double.parseDouble(map.get("xjzfeje").toString())==mx.getZfeize().doubleValue()
							&&Double.parseDouble(map.get("tczfje").toString())==mx.getTczfje().doubleValue()){
						selected = i;
						flag = true;
					}
				}
				if(flag == true){
					InsZsBaYbSt st = new InsZsBaYbSt();
					st.setPkInsst(mapList.get(selected).get("pkInsst").toString());
					st.setTjlb(monthSettleData.getTjlb());
					st.setTjjzrq(monthSettleData.getTjjzrq());
					st.setJjywh(monthSettleData.getJjywh());
					DataBaseHelper.updateBeanByPk(st, false);
				}else{
					//暂不处理,一般都会找到对应的结算数据，这个else是预留的
				}
			}
			break;
		case "81"://外部医保-中山医保民政双低月结明细
			sql = "select pk_insst,jsywh from ins_st where substr(to_char(jsrq,'YYYYMMDD'),1,6) = ?  and del_flag = '0' and jsbz = '1'";
			mapList = DataBaseHelper.queryForList(sql,  monthSettleData.getTjjzrq().substring(0, 6));
			for(InsZsBaYbRptMzsd mx : monthSettleData.getInsZsybRptMzsdList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
				
				int selected = 0;
				boolean flag = false;
				for(int i=0; i<mapList.size(); i++){
					Map<String, Object> map = mapList.get(i);
					if(map.get("jsywh").equals(mx.getSdywh().toString())){
						selected = i;
						flag = true;
					}
				}
				if(flag == true){
					InsZsBaYbSt st = new InsZsBaYbSt();
					st.setPkInsst(mapList.get(selected).get("pkInsst").toString());
					st.setTjlb(monthSettleData.getTjlb());
					st.setTjjzrq(monthSettleData.getTjjzrq());
					st.setJjywh(monthSettleData.getJjywh());
					DataBaseHelper.updateBeanByPk(st, false);
				}else{
					//暂不处理,一般都会找到对应的结算数据，这个else是预留的
				}
			}
			break;
		case "82"://中山医保民政优抚月结明细(重点优抚人员)
			sql = "select pk_insst,jsywh from ins_st where substr(to_char(jsrq,'YYYYMMDD'),1,6) = ?  and del_flag = '0' and jsbz = '1'";
			mapList = DataBaseHelper.queryForList(sql,  monthSettleData.getTjjzrq().substring(0, 6));
			for(InsZsBaYbRptMzyf mx : monthSettleData.getInsZsybRptMzyfList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
				
				int selected = 0;
				boolean flag = false;
				for(int i=0; i<mapList.size(); i++){
					Map<String, Object> map = mapList.get(i);
					if(map.get("jsywh").equals(mx.getSdywh().toString())){
						selected = i;
						flag = true;
					}
				}
				if(flag == true){
					InsZsBaYbSt st = new InsZsBaYbSt();
					st.setPkInsst(mapList.get(selected).get("pkInsst").toString());
					st.setTjlb(monthSettleData.getTjlb());
					st.setTjjzrq(monthSettleData.getTjjzrq());
					st.setJjywh(monthSettleData.getJjywh());
					DataBaseHelper.updateBeanByPk(st, false);
				}else{
					//暂不处理,一般都会找到对应的结算数据，这个else是预留的
				}
			}
			break;
		case "83"://中山医保民政优抚月结明细(一到六级残疾军人特殊医疗)
			sql = "select pk_insst,jsywh from ins_st where substr(to_char(jsrq,'YYYYMMDD'),1,6) = ?  and del_flag = '0' and jsbz = '1'";
			mapList = DataBaseHelper.queryForList(sql,  monthSettleData.getTjjzrq().substring(0, 6));
			for(InsZsBaYbRptMzyf mx : monthSettleData.getInsZsybRptMzyfList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
				
				int selected = 0;
				boolean flag = false;
				for(int i=0; i<mapList.size(); i++){
					Map<String, Object> map = mapList.get(i);
					if(map.get("jsywh").equals(mx.getSdywh().toString())){
						selected = i;
						flag = true;
					}
				}
				if(flag == true){
					InsZsBaYbSt st = new InsZsBaYbSt();
					st.setPkInsst(mapList.get(selected).get("pkInsst").toString());
					st.setTjlb(monthSettleData.getTjlb());
					st.setTjjzrq(monthSettleData.getTjjzrq());
					st.setJjywh(monthSettleData.getJjywh());
					DataBaseHelper.updateBeanByPk(st, false);
				}else{
					//暂不处理,一般都会找到对应的结算数据，这个else是预留的
				}
			}
			break;
		case "84"://中山医保民政双低普通门诊月结明细
			for(InsZsBaYbRptMzsdMz mx : monthSettleData.getInsZsybRptMzsdMzList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
			}
			break;
			//门诊的 不用插数据到住院结算表
		case "71"://中山医保生育保险住院月结明细
			sql = "select pk_insst,jsywh from ins_st where substr(to_char(jsrq,'YYYYMMDD'),1,6) = ?  and del_flag = '0' and jsbz = '1'";
			mapList = DataBaseHelper.queryForList(sql,  monthSettleData.getTjjzrq().substring(0, 6));
			for(InsZsBaYbRptSyzybx mx : monthSettleData.getInsZsybRptSyzybxList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
				int selected = 0;
				boolean flag = false;
				for(int i=0; i<mapList.size(); i++){
					Map<String, Object> map = mapList.get(i);
					if(map.get("jsywh").equals(mx.getSdywh().toString())){
						selected = i;
						flag = true;
					}
				}
				if(flag == true){
					InsZsBaYbSt st = new InsZsBaYbSt();
					st.setPkInsst(mapList.get(selected).get("pkInsst").toString());
					st.setTjlb(monthSettleData.getTjlb());
					st.setTjjzrq(monthSettleData.getTjjzrq());
					st.setJjywh(monthSettleData.getJjywh());
					DataBaseHelper.updateBeanByPk(st, false);
				}else{
					//暂不处理
				}
			}
			break;
		case "72"://中山医保计划生育月结明细
			sql = "select pk_insst,jsywh from ins_st where substr(to_char(jsrq,'YYYYMMDD'),1,6) = ?  and del_flag = '0' and jsbz = '1'";
			mapList = DataBaseHelper.queryForList(sql,  monthSettleData.getTjjzrq().substring(0, 6));
			for(InsZsBaYbRptJhsy mx : monthSettleData.getInsZsybRptJhsyList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
				int selected = 0;
				boolean flag = false;
				for(int i=0; i<mapList.size(); i++){
					Map<String, Object> map = mapList.get(i);
					if(map.get("jsywh").equals(mx.getSdywh().toString())){
						selected = i;
						flag = true;
					}
				}
				if(flag == true){
					InsZsBaYbSt st = new InsZsBaYbSt();
					st.setPkInsst(mapList.get(selected).get("pkInsst").toString());
					st.setTjlb(monthSettleData.getTjlb());
					st.setTjjzrq(monthSettleData.getTjjzrq());
					st.setJjywh(monthSettleData.getJjywh());
					DataBaseHelper.updateBeanByPk(st, false);
				}else{
					//暂不处理,一般都会找到对应的结算数据，这个else是预留的
				}
			}
			break;
		case "73"://中山医保产前检查月结明细
			for(InsZsBaYbRptCqjc mx : monthSettleData.getInsZsybRptCqjcList()){
				mx.setJjywh(rpt.getJjywh());
				mx.setBblb(rpt.getTjlb());
				DataBaseHelper.insertBean(mx);
			}
			break;
			//门诊的 不用插数据到住院结算表
		default:
			throw new BusException("统计类型错误！");
		}
	}
	
	/**
	 * 根据报表类型、时间段查询医保月结算明细
	 * @param param
	 * @param user
	 * @return
	 */
	public MonthSettleData queryMonthSettleDetails(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String tjlb = jo.getString("tjlb");
		String date = jo.getString("date");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("date", date);
		map.put("tjlb", tjlb);
		MonthSettleData monthSettleData = new MonthSettleData();
		
		switch(tjlb)
		{
		case "1"://中山医保门诊月结明细   
			monthSettleData.setInsZsybRptMzList(insZsybRptMzMapper.getYbMx(map));
			break;
		case "2"://中山医保住院医疗月结明细
			monthSettleData.setInsZsybRptZyList(insZsybRptZyMapper.getYbMx(map));
			break;
		case "4"://中山医保离休月结明细
			monthSettleData.setInsZsybRptLxList(insZsybRptLxMapper.getYbMx(map));
			break;
		case "6"://中山医保特定病种月结明细
			monthSettleData.setInsZsybRptTdbzList(insZsybRptTdbzMapper.getYbMx(map));
			break;
		case "9"://中山医保工伤门诊月结明细
			monthSettleData.setInsZsybRptGsmzList(insZsybRptGsmzMapper.getYbMx(map));
			break;
		case "10"://中山医保工伤住院月结明细
			monthSettleData.setInsZsybRptGszyList(insZsybRptGszyMapper.getYbMx(map));
			break;
		case "13"://中山医保生育住院月结明细
			monthSettleData.setInsZsybRptSyzyList(insZsybRptSyzyMapper.getYbMx(map));
			break;
		case "81"://外部医保-中山医保民政双低月结明细
			monthSettleData.setInsZsybRptMzsdList(insZsybRptMzsdMapper.getYbMx(map));
			break;
		case "82"://中山医保民政优抚月结明细(重点优抚人员)
			monthSettleData.setInsZsybRptMzyfList(insZsybRptMzyfMapper.getYbMx(map));
			break;
		case "83"://中山医保民政优抚月结明细(一到六级残疾军人特殊医疗)
			monthSettleData.setInsZsybRptMzyfList(insZsybRptMzyfMapper.getYbMx(map));
			break;
		case "84"://中山医保民政双低普通门诊月结明细
			monthSettleData.setInsZsybRptMzsdMzList(insZsybRptMzsdMzMapper.getYbMx(map));
			break;
		case "71"://中山医保生育保险住院月结明细
			monthSettleData.setInsZsybRptSyzybxList(insZsybRptSyzybxMapper.getYbMx(map));
			break;
		case "72"://中山医保计划生育月结明细
			monthSettleData.setInsZsybRptJhsyList(insZsybRptJhsyMapper.getYbMx(map));
			break;
		case "73"://中山医保产前检查月结明细
			monthSettleData.setInsZsybRptCqjcList(insZsybRptCqjcMapper.getYbMx(map));
			break;
		default:
			throw new BusException("统计类型错误！");
		}
		return monthSettleData;
	}
	
	/**
	 * 根据类型查询所有月结算数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsZsBaYbRpt> getRptList(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String tjlb = jo.getString("tjlb");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tjlb", tjlb);
		
		List<InsZsBaYbRpt> rpt = insZsybRptMapper.getRptList(map);
		return rpt;
	}
	
	/**
	 * 根据统计类型，交接业务号删除月结算数据
	 * @param param
	 * @param user
	 * @return
	 */
	public void delMxList(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String tjlb = jo.getString("tjlb");
		String jjywh = jo.getString("jjywh");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("jjywh", jjywh);
		
		DelYbMxParam delYbMxParam = JsonUtil.readValue(param, DelYbMxParam.class);
		
		//修改结算表中对应的月结数据
		String sql = "update ins_st set tjlb = null, tjjzrq = null, jjywh=null where jjywh = ?";
		DataBaseHelper.execute(sql, jjywh);
		switch(tjlb)
		{
		case "1"://中山医保门诊月结明细   

				insZsybRptMzMapper.delYbMx(map);
			break;
		case "2"://中山医保住院医疗月结明细
				insZsybRptZyMapper.delYbMx(map);
			break;
		case "4"://中山医保离休月结明细
				insZsybRptLxMapper.delYbMx(map);
			break;
		case "6"://中山医保特定病种月结明细
				insZsybRptTdbzMapper.delYbMx(map);
			break;
		case "9"://中山医保工伤门诊月结明细
				insZsybRptGsmzMapper.delYbMx(map);
			break;
		case "10"://中山医保工伤住院月结明细
				insZsybRptGszyMapper.delYbMx(map);
			break;
		case "13"://中山医保生育住院月结明细
				insZsybRptSyzyMapper.delYbMx(map);
			break;
		case "81"://外部医保-中山医保民政双低月结明细
				insZsybRptMzsdMapper.delYbMx(map);
			break;
		case "82"://中山医保民政优抚月结明细(重点优抚人员)
				insZsybRptMzyfMapper.delYbMx(map);
			break;
		case "83"://中山医保民政优抚月结明细(一到六级残疾军人特殊医疗)
				insZsybRptMzyfMapper.delYbMx(map);
			break;
		case "84"://中山医保民政双低普通门诊月结明细
				insZsybRptMzsdMzMapper.delYbMx(map);
			break;
		case "71"://中山医保生育保险住院月结明细
				insZsybRptSyzybxMapper.delYbMx(map);
			break;
		case "72"://中山医保计划生育月结明细
				insZsybRptJhsyMapper.delYbMx(map);
			break;
		case "73"://中山医保产前检查月结明细
				insZsybRptCqjcMapper.delYbMx(map);
			break;
		default:
			throw new BusException("统计类型错误！");
		}
		map = new HashMap<String,Object>();
		map.put("jjywh", jjywh);
		insZsybRptMapper.delYbMx(map);
		
		//批量
/*		switch(delYbMxParam.getTjlb())
		{
		case "1"://中山医保门诊月结明细   
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptMzMapper.delYbMx(map);
			}
			break;
		case "2"://中山医保住院医疗月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptZyMapper.delYbMx(map);
			}
			break;
		case "4"://中山医保离休月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptLxMapper.delYbMx(map);
			}
			break;
		case "6"://中山医保特定病种月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptTdbzMapper.delYbMx(map);
			}
			break;
		case "9"://中山医保工伤门诊月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptGsmzMapper.delYbMx(map);
			}
			break;
		case "10"://中山医保工伤住院月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptGszyMapper.delYbMx(map);
			}
			break;
		case "13"://中山医保生育住院月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptSyzyMapper.delYbMx(map);
			}
			break;
		case "81"://外部医保-中山医保民政双低月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptMzsdMapper.delYbMx(map);
			}
			break;
		case "82"://中山医保民政优抚月结明细(重点优抚人员)
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptMzyfMapper.delYbMx(map);
			}
			break;
		case "83"://中山医保民政优抚月结明细(一到六级残疾军人特殊医疗)
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptMzyfMapper.delYbMx(map);
			}
			break;
		case "84"://中山医保民政双低普通门诊月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptMzsdMzMapper.delYbMx(map);
			}
			break;
		case "71"://中山医保生育保险住院月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptSyzybxMapper.delYbMx(map);
			}
			break;
		case "72"://中山医保计划生育月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptJhsyMapper.delYbMx(map);
			}
			break;
		case "73"://中山医保产前检查月结明细
			for(String str : delYbMxParam.getJjywhList()){
				map = new HashMap<String,Object>();
				map.put("jjywh", str);
				insZsybRptCqjcMapper.delYbMx(map);
			}
			break;
		default:
			throw new BusException("统计类型错误！");
		}
		
		for(String str : delYbMxParam.getJjywhList()){
			map = new HashMap<String,Object>();
			map.put("jjywh", str);
			insZsybRptMapper.delYbMx(map);
		}*/
	}
}