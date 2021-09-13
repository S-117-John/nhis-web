package com.zebone.nhis.pro.zsba.compay.ins.ydyb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubPvOut;
import com.zebone.nhis.pro.zsba.compay.ins.ydyb.vo.InsZsYdybStSnyb;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InputDetail0534;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsYdybStService {

	/**
	 * 获取取消医保结算的入参
	 * @param param
	 * @param user
	 */
/*	public Map<String,String> getCancelYbSettleParam(String param , IUser user){
		
		Map<String,String> returnMap = new HashMap<String,String>();
		
		//根据结算主键获取医保结算记录，没有则不需要取消医保结算
		//判断是否是当天的结算记录，否则不能取消医保结算
		//然后判断是否为出院结算、结算结果为存款结算，如果是的话判断预存金是否足够冲销，不够就不能取消医保结算
		//判断是否是银行缴费，是的不能取消医保结算，因为银行缴费的现在只支持现金退款
		JSONObject jo = JSONObject.fromObject(param);
		String pkSettle = jo.getString("pkSettle");
		
		InsSt insSt = DataBaseHelper.queryForBean(
				"select * from ins_st where pk_settle = ? and del_flag = '0'",
				InsSt.class, pkSettle);
		if(insSt!=null&&!insSt.getStatus().equals("13")){
			returnMap.put("isCencel", "true");  //需要进行医保的取消结算
			//医保费用只能撤销当天的
			if(DateUtils.dateToStr("yyyyMMdd", insSt.getJsrq()).equals(DateUtils.dateToStr("yyyyMMdd", new Date()))){
				returnMap.put("sureCencel", "true"); 
				returnMap.put("jzjlh", insSt.getJzjlh());
				returnMap.put("jsywh", insSt.getJsywh());
				returnMap.put("posSn", insSt.getPosSn());
				
				//判断是否是银行卡缴费
				BlDeposit blDeposit = DataBaseHelper.queryForBean("select * from bl_deposit where pk_settle = ?",BlDeposit.class, pkSettle);
				if(blDeposit==null||!blDeposit.getDtPaymode().equals("3")){
					BlSettle stVo = blIpPubMapper.QryBlSettleByPk(pkSettle);
					if("10".equals(stVo.getDtSttype()) && EnumerateParameter.TWO.equals(stVo.getEuStresult())){
				    	 //如果被取消的结算为出院结算，结算结果为存款结算（eu_stresult=2），要将存入患者账户的存款冲销回原状，冲销前要调用查询患者账户服务，确认患者账户金额是否足以冲销，如果患者账户金额不足，将不允许取消结算。
				    	 List<BlDepositPi> deposRe = blIpPubMapper.QryBlDepositPiBySt(pkSettle);
				    	 Double amtAcc = 0.0;
				    	 for(BlDepositPi depo : deposRe){				
								amtAcc+=depo.getAmount();
						}
				    	Map<String,Object> amtAccMap = DataBaseHelper.queryForMap("select  Amt_acc from pi_acc piacc  inner join pv_encounter pv on piacc.pk_pi = pv.pk_pi where  pv.pk_pv = ?", stVo.getPkPv());
				  		BigDecimal amtAccNow = (BigDecimal)amtAccMap.get("amtAcc");
				  		if(amtAccNow.doubleValue()>=amtAcc){
				  			returnMap.put("sureCencel", "false"); //是否可以进行医保的取消结算
				  			returnMap.put("message", "账户余额:"+amtAccNow.doubleValue()+"不足，小于冲销所需:"+amtAcc+",取消结算失败!"); //
				  		}
					}
				}else{
					//银行卡缴费
		  			returnMap.put("sureCencel", "false"); //不可以进行医保的取消结算
		  			returnMap.put("message", "银行卡缴费的需要去人工窗口撤销费用!"); 
				}
			}else{
	  			returnMap.put("sureCencel", "false"); //不可以进行医保的取消结算
	  			returnMap.put("message", "医保只能撤销当天的，请到人工窗口退费!"); 
			}
		}else{
			returnMap.put("isCencel", "false");  //不需要进行医保的取消结算
		}
		return returnMap;
	}*/
	
	/**
	 * 取消医保结算
	 * @param param
	 * @param user
	 */
	public void cancelSnybSettlement(String param , IUser user){
		InsZsYdybStSnyb st = JsonUtil.readValue(param, InsZsYdybStSnyb.class);
		
		//修改入院登记表的状态
		InsZsPubPvOut pv = DataBaseHelper.queryForBean("select * from ins_pv_out where del_flag = '0' and ykc700 = ?", InsZsPubPvOut.class, st.getYkc700());
		pv.setStatus("13");
		DataBaseHelper.updateBeanByPk(pv, false);
		
		InsZsYdybStSnyb st2 = DataBaseHelper.queryForBean("select * from ins_st_snyb where del_flag = '0' and ykc700 = ?", InsZsYdybStSnyb.class, st.getYkc700());
		if(st2!=null){
			st.setPkInsst(st2.getPkInsst());
			st.setDelFlag("1");
			DataBaseHelper.updateBeanByPk(st, false);
		}
	}
	
	/**
	 * 获取月度申报、险种汇总、月度汇总显示数据
	 * @param param
	 * @param user
	 */
	public List<InsZsYdybStSnyb> getYdsbData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String yzz060 = jo.getString("yzz060");
		String yzz061 = jo.getString("yzz061");
		String aae140 = jo.getString("aae140");
		String type = jo.getString("type"); //1：月度申报  2：险种汇总 3：月度汇总
		
		StringBuffer sql = new StringBuffer();
		if(type.equals("1")){
			sql.append("select * from ins_st_snyb WHERE del_flag = '0' and yzz060 = ? and yzz061 = ? ");
			if(aae140!=null&&!aae140.equals("null")&&aae140.trim().length()!=0){
				sql.append(" and aae140=?");
			}
			sql.append(" and yzz062 is not null  and transid0521 is not null and transid0521 != ''");
		}else if(type.equals("2")){
			sql.append("select * from ins_st_snyb WHERE del_flag = '0' and yzz060 = ? and yzz061 = ?");
			if(aae140!=null&&!aae140.equals("null")&&aae140.trim().length()!=0){
				sql.append(" and aae140=? ");
			}
			sql.append(" and yzz062 is not null  and transid0536 is not null and transid0536 != ''");
		}else if(type.equals("3")){
			sql.append( "select * from ins_st_snyb WHERE del_flag = '0' and yzz060 = ? and yzz061 = ? and yzz062 is not null  and transid0534 is not null and transid0534 != ''");
		}
		List<InsZsYdybStSnyb> snybList = new ArrayList<InsZsYdybStSnyb>();
		if(type.equals("1")||type.equals("2")){
			if(aae140!=null&&!aae140.equals("null")&&aae140.trim().length()!=0){
				snybList = DataBaseHelper.queryForList(sql.toString(), InsZsYdybStSnyb.class,  yzz060, yzz061, aae140);
			}else{
				snybList = DataBaseHelper.queryForList(sql.toString(), InsZsYdybStSnyb.class,  yzz060, yzz061);
			}
		}else if(type.equals("3")){
				snybList = DataBaseHelper.queryForList(sql.toString(), InsZsYdybStSnyb.class,  yzz060, yzz061);
		}
		return snybList;
	}
	
	/**
	 * 
	 * 保存提交月度申报结果
	 * @param param
	 * @param user
	 */
	public void saveYdsbData(String param , IUser user){
		InsZsYdybStSnyb st = JsonUtil.readValue(param, InsZsYdybStSnyb.class);
		String sql = "select * from ins_st_Snyb where del_flag='0' and  substr(to_char(akc194, 'yyyyMMdd'),1,6) =? and aae140 = ? and (transid0521 is null or transid0521 = '')";
		List<InsZsYdybStSnyb> stList = DataBaseHelper.queryForList(sql, InsZsYdybStSnyb.class, st.getYzz060()+st.getYzz061(), st.getAae140());
		for(InsZsYdybStSnyb snyb : stList){
			InsZsYdybStSnyb stSnyb = new InsZsYdybStSnyb();
			stSnyb = st;
			stSnyb.setPkInsst(snyb.getPkInsst());
			stSnyb.setTransid0522("");
			
			DataBaseHelper.updateBeanByPk(stSnyb, false);
		}
	}
	
	/**
	 * 批量保存月度申报回退交易流水号
	 * @param param
	 * @param user
	 */
	public void saveYdsbhtData(String param , IUser user){
/*		JSONArray ja =  new JSONArray(param);
		for(int i=0 ; i < ja.length() ;i++){
			JSONObject jo = ja.getJSONObject(i);
			String sql = "select * from ins_st_Snyb where del_flag='0' and transid0521 = ?";
			List<InsStSnyb> stList = DataBaseHelper.queryForList(sql, InsStSnyb.class, jo.getString("otransid"));
			for(InsStSnyb st : stList){
				st.setTransid0522(jo.getString("transid"));
				st.setErrorcode(jo.getInt("errorcode"));
				st.setErrormsg(jo.getString("errormsg"));
				DataBaseHelper.updateBeanByPk(st, false);
			}
		}*/
		//不批量
		JSONObject jo = JSONObject.fromObject(param);
		String transid = jo.getString("transid");
		String otransid = jo.getString("otransid");
		String errorcode = jo.getString("errorcode");
		String errormsg = jo.getString("errormsg");
		
		String sql = "select * from ins_st_Snyb where del_flag='0' and transid0521 = ?";
		List<InsZsYdybStSnyb> stList = DataBaseHelper.queryForList(sql, InsZsYdybStSnyb.class, otransid);
		for(InsZsYdybStSnyb st : stList){
			st.setTransid0522(transid);
			st.setTransid0521("");
			st.setYzz062("");
			st.setErrorcode(Integer.parseInt(errorcode));
			st.setErrormsg(errormsg);
			DataBaseHelper.updateBeanByPk(st, false);
		}
	}
	
	/**
	 * 获取待提交月度申报汇总数据
	 * @param param
	 * @param user
	 */
	public Map<String, Object> getYdsbhzData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String yzz060 = jo.getString("yzz060");
		String yzz061 = jo.getString("yzz061");
		String sql = "select yzz062 from ins_st_snyb WHERE del_flag = '0' and yzz060 = ? and yzz061 = ? and yzz062 is not null and (transid0534 is null or transid0534 = '') group by yzz062";
		Map<String,Object> ywhMap = DataBaseHelper.queryForMap(sql, yzz060, yzz061);
		if(ywhMap==null||ywhMap.get("yzz062").toString().trim().length()==0){
			throw new BusException("该月度还未申报！");
		}
		String yzz062 = ywhMap.get("yzz062").toString();
		
		sql = "select aab301, count(distinct aac002) ake096, count(pk_insst) ake098, sum(akc264) akc264, "+
					"sum(akb068) akb068,  sum(ykc630) ykc630, sum(yzz139) yzz139 "+
					"from ins_st_snyb WHERE del_flag = '0' and yzz060 = ? and yzz061 = ? and yzz062 is not null and (transid0534 is null or transid0534 = '') group by aab301";
		List<InputDetail0534> detail = DataBaseHelper.queryForList(sql, InputDetail0534.class, yzz060, yzz061);
		
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("yzz062", yzz062);
	    map.put("detail", detail);
		return map;
	}

	
	/**
	 * 保存提交月度申报汇总表结果
	 * @param param
	 * @param user
	 */
	public void saveYdsbhzData(String param , IUser user){
		System.out.println(param);
		InsZsYdybStSnyb st = JsonUtil.readValue(param, InsZsYdybStSnyb.class);
		
		String sql = "select * from ins_st_Snyb where del_flag='0' and yzz062 = ? and (transid0534 is null or transid0534 = '')";
		List<InsZsYdybStSnyb> stList = DataBaseHelper.queryForList(sql, InsZsYdybStSnyb.class, st.getYzz062());
		for(InsZsYdybStSnyb snyb : stList){
			InsZsYdybStSnyb stSnyb = new InsZsYdybStSnyb();
			stSnyb = st;
			stSnyb.setPkInsst(snyb.getPkInsst());
			stSnyb.setTransid0535("");
			DataBaseHelper.updateBeanByPk(stSnyb, false);
		}
	}
	
	/**
	 * 批量保存月度申报汇总回退交易流水号
	 * @param param
	 * @param user
	 */
	public void saveYdsbhzhtData(String param , IUser user){
		//批量
/*		JSONArray ja =  new JSONArray(param);
		for(int i=0 ; i < ja.length() ;i++){
			JSONObject jo = ja.getJSONObject(i);
			String sql = "select * from ins_st_Snyb where del_flag='0' and transid0534 = ?";
			List<InsStSnyb> stList = DataBaseHelper.queryForList(sql, InsStSnyb.class, jo.getString("otransid"));
			for(InsStSnyb st : stList){
				st.setTransid0535(jo.getString("transid"));
				st.setErrorcode(jo.getInt("errorcode"));
				st.setErrormsg(jo.getString("errormsg"));
				DataBaseHelper.updateBeanByPk(st, false);
			}
		}*/
		
		//不批量
		JSONObject jo = JSONObject.fromObject(param);
		String transid = jo.getString("transid");
		String otransid = jo.getString("otransid");
		String errorcode = jo.getString("errorcode");
		String errormsg = jo.getString("errormsg");
		
		String sql = "select * from ins_st_Snyb where del_flag='0' and transid0534 = ?";
		List<InsZsYdybStSnyb> stList = DataBaseHelper.queryForList(sql, InsZsYdybStSnyb.class, otransid);
		for(InsZsYdybStSnyb st : stList){
			st.setTransid0535(transid);
			st.setErrorcode(Integer.parseInt(errorcode));
			st.setErrormsg(errormsg);
			st.setTransid0534("");
			DataBaseHelper.updateBeanByPk(st, false);
		}
	}
	
	/**
	 * 获取待提交月度申报分险种汇总数据
	 * @param param
	 * @param user
	 */
	public Map<String,Object> getYdsbfxzhzData(String param , IUser user){
		System.out.println(param);
		JSONObject jo = JSONObject.fromObject(param);
		String yzz060 = jo.getString("yzz060");
		String yzz061 = jo.getString("yzz061");
		String aae140 = jo.getString("aae140");
		
		String sql = "select yzz062, count(distinct aab301) yzz133 from ins_st_snyb WHERE del_flag = '0' and yzz060 = ? and yzz061 = ? and aae140=? and yzz062 is not null  and (transid0536 is null or transid0536 = '') group by yzz062";
		Map<String,Object> ywhMap = DataBaseHelper.queryForMap(sql, yzz060, yzz061, aae140);
		if(ywhMap==null||ywhMap.get("yzz062").toString().trim().length()==0){
			throw new BusException("该月度还未申报！");
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("yzz062", ywhMap.get("yzz062").toString());
		map.put("yzz133", ywhMap.get("yzz133").toString());
		
		sql = "select aab301, count(distinct aac002) ake096, count(pk_insst) ake098, sum(akc264) akc264, "+
					"sum(akb068) akb068,  sum(ykc630) ykc630, sum(yzz139) yzz139 "+
					"from ins_st_snyb WHERE del_flag = '0' and yzz060 = ? and yzz061 = ? and aae140 = ? and yzz062 is not null and (transid0536 is null or transid0536 = '') group by aab301";
		List<Map<String,Object>> detail = DataBaseHelper.queryForList(sql, yzz060, yzz061, aae140);

		map.put("detail", detail);
		return map;
	}
	
	/**
	 * 保存提交月度申报分险种汇总数据
	 * @param param
	 * @param user
	 */
	public void saveYdsbfxzhzData(String param , IUser user){
		System.out.println(param);
		InsZsYdybStSnyb st = JsonUtil.readValue(param, InsZsYdybStSnyb.class);
		
		String sql = "select * from ins_st_Snyb where del_flag='0' and yzz062 = ? and aae140 = ? and (transid0536 is null or transid0536 = '')";
		List<InsZsYdybStSnyb> stList = DataBaseHelper.queryForList(sql, InsZsYdybStSnyb.class, st.getYzz062(), st.getAae140());
		for(InsZsYdybStSnyb snyb : stList){
			InsZsYdybStSnyb stSnyb = new InsZsYdybStSnyb();
			stSnyb = st;
			stSnyb.setPkInsst(snyb.getPkInsst());
			stSnyb.setTransid0537("");
			DataBaseHelper.updateBeanByPk(stSnyb, false);
		}
	}
	
	/**
	 * 批量保存月度申报分险种汇总回退交易流水号
	 * @param param
	 * @param user
	 */
	public void saveYdsbfxzhzhtData(String param , IUser user){
		//批量
/*		JSONArray ja =  new JSONArray(param);
		for(int i=0 ; i < ja.length() ;i++){
			JSONObject jo = ja.getJSONObject(i);
			String sql = "select * from ins_st_Snyb where del_flag='0' and transid0536 = ?";
			List<InsStSnyb> stList = DataBaseHelper.queryForList(sql, InsStSnyb.class, jo.getString("otransid"));
			for(InsStSnyb st : stList){
				st.setTransid0537(jo.getString("transid"));
				st.setErrorcode(jo.getInt("errorcode"));
				st.setErrormsg(jo.getString("errormsg"));
				DataBaseHelper.updateBeanByPk(st, false);
			}
		}*/
		
		//不批量
		JSONObject jo = JSONObject.fromObject(param);
		String transid = jo.getString("transid");
		String otransid = jo.getString("otransid");
		String errorcode = jo.getString("errorcode");
		String errormsg = jo.getString("errormsg");
		
		String sql = "select * from ins_st_Snyb where del_flag='0' and transid0536 = ?";
		List<InsZsYdybStSnyb> stList = DataBaseHelper.queryForList(sql, InsZsYdybStSnyb.class, otransid);
		for(InsZsYdybStSnyb st : stList){
			st.setTransid0537(transid);
			st.setErrorcode(Integer.parseInt(errorcode));
			st.setErrormsg(errormsg);
			st.setTransid0536("");
			DataBaseHelper.updateBeanByPk(st, false);
		}
	}
	
	/**
	 * 月度申报、分险种汇总、月度汇总回退时根据结算申报业务号获取原交易流水号
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getOtransidList(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String yzz062 = jo.getString("yzz062");
		String type =  jo.getString("type");
		String sql = "";
		if(type.equals("1")){
			sql = "select transid0521, aae140 from ins_st_snyb where del_flag = '0' and transid0521 is not null and transid0521 !='' " +
					" and (transid0534 is null or transid0534 ='') and (transid0536 is null or transid0536 ='')" +
					" and yzz062 = ? group by transid0521, aae140";
		}else if(type.equals("2")){
			sql = "select transid0536, aae140 from ins_st_snyb where del_flag = '0' and transid0536 is not null and transid0536 !='' " +
					" and (transid0534 is null or transid0534 ='') and yzz062 = ? group by transid0536, aae140";
		}else if(type.equals("3")){
			sql = "select transid0534 from ins_st_snyb where del_flag = '0' and transid0534 is not null and transid0534 !='' " +
					" and yzz062 = ? group by transid0534";
		}else{
			throw new BusException("type值错误");
		}
		List<Map<String,Object>> mapList = DataBaseHelper.queryForList(sql, yzz062);
		if(mapList.size()==0){
			if(type.equals("1")){
				throw new BusException("该结算申报业务号已汇总，不能进行申报回退！");
			}else if(type.equals("2")){
				throw new BusException("该结算申报业务号已月度汇总，不能进行分险种汇总回退！");
			}
		}
		return mapList;
	}
}