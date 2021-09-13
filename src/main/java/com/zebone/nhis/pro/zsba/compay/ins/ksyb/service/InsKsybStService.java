package com.zebone.nhis.pro.zsba.compay.ins.ksyb.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.DayReconciliationData;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.InsZsKsybStKsyb;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.InsZsKsybStKsybJjfx;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubPvOut;
import com.zebone.nhis.pro.zsba.compay.ins.ydyb.vo.InsZsYdybStSnyb;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsKsybStService {

	@Autowired
	private BlIpPubMapper blIpPubMapper; 
	
	/**
	 * 取消医保结算
	 * @param param
	 * @param user
	 */
	public void cancelKsybSettlement(String param , IUser user){
		InsZsKsybStKsyb st = JsonUtil.readValue(param, InsZsKsybStKsyb.class);
		
		//修改入院登记表的状态
		InsZsPubPvOut pv = DataBaseHelper.queryForBean("select * from ins_pv_out where del_flag = '0' and ykc700 = ?", InsZsPubPvOut.class, st.getYkc700());
		pv.setStatus("13");
		DataBaseHelper.updateBeanByPk(pv, false);
		
		InsZsKsybStKsyb st2 = DataBaseHelper.queryForBean("select * from ins_st_ksyb where del_flag = '0' and ykc700 = ?", InsZsKsybStKsyb.class, st.getYkc700());
		if(st2!=null){
			st.setPkInsst(st2.getPkInsst());
			st.setStatus("13");
			st.setDelFlag("1");
			DataBaseHelper.updateBeanByPk(st, false);
			List<InsZsKsybStKsybJjfx> jjfxList = DataBaseHelper.queryForList("select * from ins_st_ksyb_jjfx where del_flag = '0' and pk_settle = ?", InsZsKsybStKsybJjfx.class, st2.getPkSettle());
			for(InsZsKsybStKsybJjfx jjfx : jjfxList){
				jjfx.setStatus("13");
				jjfx.setDelFlag("1");
				DataBaseHelper.updateBeanByPk(jjfx, false);
			}
		}
	}
	
	/**
	 * 获取取消结算入参
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,String> getCancelYdybParam(String param , IUser user){
		Map<String,String> returnMap = new HashMap<String,String>();
		JSONObject jo = JSONObject.fromObject(param);
		String pkSettle = jo.getString("pkSettle");
		String pkPv = jo.getString("pkPv");
		String dtDeptType = jo.getString("dtDeptType");
		
		String sql =  "select * from ins_pv_out where del_flag='0' and pk_pv = ? and eu_pvtype = '3'";
		InsZsPubPvOut pv = DataBaseHelper.queryForBean(sql, InsZsPubPvOut.class, pkPv);
		if(pv!=null && !pv.getStatus().equals("11")){
			returnMap.put("isCencel", "false");
			returnMap.put("message", "不需要取消医保结算"); 
			return returnMap;
		}
		if(pv.getInsType().equals("1")){
			sql = "select * from ins_st_snyb where del_flag='0' and pk_settle = ? and eu_pvtype = '3'";
			InsZsYdybStSnyb st = DataBaseHelper.queryForBean(sql, InsZsYdybStSnyb.class, pkSettle);
			if(st==null){
				returnMap.put("isCencel", "false");
				returnMap.put("message", "不需要取消医保结算"); 
				return returnMap;
			}else{
				returnMap.put("isCencel", "true");
				returnMap.put("otransid", st.getTransid0304());//原交易流水号
				returnMap.put("ykc700", st.getYkc700());//就诊登记号
				returnMap.put("aab301", st.getAab301());//行政区划代码（参保地）
				returnMap.put("yab060", st.getYab060());//参保地社保分支机构代码
				returnMap.put("aac002", st.getAac002());//社会保障号码
				returnMap.put("aac043", st.getAac043());//证件类型
				returnMap.put("aac044", st.getAac044());//证件号码
				returnMap.put("ykc618", st.getYkc618());//结算业务号
				returnMap.put("aae011", user.getUserName());//经办人
				returnMap.put("yzz021", st.getYzz021());//医院结算业务序列号
			}
		}else if(pv.getInsType().equals("2")){
			sql = "select * from ins_st_ksyb where del_flag='0' and pk_settle = ? and eu_pvtype = '3'";
			InsZsKsybStKsyb st = DataBaseHelper.queryForBean(sql, InsZsKsybStKsyb.class, pkSettle);
			if(st==null){
				returnMap.put("isCencel", "false");
				returnMap.put("message", "不需要取消医保结算"); 
				return returnMap;
			}else{
				returnMap.put("isCencel", "true");
				returnMap.put("otransid", st.getTransid0304());//原交易流水号
				returnMap.put("ykc700", st.getYkc700());//就诊登记号
				returnMap.put("aab301", st.getAab301());//行政区划代码（参保地）
				returnMap.put("yab060", st.getYab060());//参保地社保分支机构代码
				returnMap.put("aac002", st.getAac002());//社会保障号码
				returnMap.put("aac043", st.getAac043());//证件类型
				returnMap.put("aac044", st.getAac044());//证件号码
				returnMap.put("ykc618", st.getAaz216());//结算业务号
				returnMap.put("aae011", user.getUserName());//经办人
				returnMap.put("yzz021", st.getYzz021());//医院结算业务序列号
			}
		}
		
		returnMap.put("insType", pv.getInsType());//医保类型
		
		//判断是否是银行卡缴费
		BlDeposit blDeposit = DataBaseHelper.queryForBean("select * from bl_deposit where pk_settle = ?",BlDeposit.class, pkSettle);
		if(blDeposit==null||!blDeposit.getDtPaymode().equals("3")){
			returnMap.put("sureCencel", "true"); 
			BlSettle stVo = blIpPubMapper.QryBlSettleByPk(pkSettle);
			if("10".equals(stVo.getDtSttype()) && EnumerateParameter.TWO.equals(stVo.getEuStresult())){
		    	 //如果被取消的结算为出院结算，结算结果为存款结算（eu_stresult=2），要将存入患者账户的存款冲销回原状，冲销前要调用查询患者账户服务，确认患者账户金额是否足以冲销，如果患者账户金额不足，将不允许取消结算。
		    	 List<BlDepositPi> deposRe = blIpPubMapper.QryBlDepositPiBySt(pkSettle);
		    	 BigDecimal amtAcc = BigDecimal.ZERO;
		    	 for(BlDepositPi depo : deposRe){				
						amtAcc = amtAcc.add(depo.getAmount());
				}
/*		    	Map<String,Object> amtAccMap = DataBaseHelper.queryForMap("select  Amt_acc from pi_acc piacc  inner join pv_encounter pv on piacc.pk_pi = pv.pk_pi where  pv.pk_pv = ?", stVo.getPkPv());
		  		BigDecimal amtAccNow = (BigDecimal)amtAccMap.get("amtAcc");
		  		if(amtAccNow.compareTo(amtAcc)>=0){
		  			returnMap.put("sureCencel", "false"); //是否可以进行医保的取消结算
		  			returnMap.put("message", "账户余额:"+amtAccNow.doubleValue()+"不足，小于冲销所需:"+amtAcc+",取消结算失败!"); //
		  		}*/
			}
		}else{
			//银行卡缴费
			if("08".equals(dtDeptType)){//收费处
				returnMap.put("sureCencel", "true"); //可以进行医保的取消结算
			}else{
				returnMap.put("sureCencel", "false"); //不可以进行医保的取消结算
	  			returnMap.put("message", "银行卡缴费的需要去人工窗口撤销费用!"); 
			}
		}
		
		return returnMap;
	}
	
	/**
	 * 获取跨省外来就医月度结算清分明细
	 * @param param
	 * @param user
	 */
	public List<InsZsKsybStKsyb> getQfmxList(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String yzz060 = jo.getString("yzz060");
		String yzz061 = jo.getString("yzz061");
		
		String sql = "select * from ins_st_ksyb where del_flag='0' and yzz060=? and yzz061=?";
		List<InsZsKsybStKsyb> stList = DataBaseHelper.queryForList(sql, InsZsKsybStKsyb.class, yzz060, yzz061);
		return stList;
	}
	/**
	 * 保存跨省外来就医月度结算清分明细
	 * @param param
	 * @param user
	 */
	public void saveQfmx(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String transid = jo.getString("transid");
		String yab600 = jo.getString("yab600");
		String yzz060 = jo.getString("yzz060");
		String yzz061 = jo.getString("yzz061");
		String akb026 = jo.getString("akb026");
		String akb021 = jo.getString("akb021");
		int startrow = Integer.parseInt(jo.getString("startrow"));
		int errorcode = Integer.parseInt(jo.getString("errorcode"));
		String errormsg = jo.getString("errormsg");
		int totalrow = Integer.parseInt(jo.getString("totalrow"));
		//param = param.substring(param.indexOf("["), param.indexOf("]")+1);
		param = jo.getString("ksybList");
		List<InsZsKsybStKsyb> data = JsonUtil.readValue(param, new TypeReference<List<InsZsKsybStKsyb>>(){});
		for(InsZsKsybStKsyb st : data){
			InsZsKsybStKsyb st2 = DataBaseHelper.queryForBean("select * from ins_st_ksyb where del_flag = '0' and aaz216 = ?", InsZsKsybStKsyb.class, st.getAaz216());
			st.setTransid6527(transid);
			st.setYab600(yab600);
			st.setYzz060(yzz060);
			st.setYzz061(yzz061);
			st.setAkb026(akb026);
			st.setAkb021(akb021);
			st.setStartrow(startrow);
			st.setErrorcode(errorcode);
			st.setErrormsg(errormsg);
			st.setTotalrow(totalrow);
			if(st2==null){
				DataBaseHelper.insertBean(st);
			}else{
				st.setPkInsst(st2.getPkInsst());
				DataBaseHelper.updateBeanByPk(st, false);
			}
		}
	}

	/**
	 * 保存跨省外来就医月度结算清分明细确认结果
	 * @param param
	 * @param user
	 */
	public void saveQfqr(String param , IUser user){
		System.out.println(param);
		param = param.substring(param.indexOf("["), param.indexOf("]")+1);
		List<InsZsKsybStKsyb> data = JsonUtil.readValue(param, new TypeReference<List<InsZsKsybStKsyb>>(){});
		for(InsZsKsybStKsyb st : data){
			DataBaseHelper.updateBeanByPk(st, false);
		}
	}

	/**
	 * 保存清分确认结果回退
	 * @param param
	 * @param user
	 */
	public void saveQfqrjght(String param , IUser user){
		System.out.println(param);
		JSONObject jo = JSONObject.fromObject(param);
		String transid6521 = jo.getString("transid6521");
		String transid6522 = jo.getString("transid6522");
		
		String sql = "update ins_st_ksyb set transid6522=?, transid6521 = null, ykc707 =null where del_flag='0' and transid6521 = ?";
		DataBaseHelper.execute(sql,transid6522, transid6521);		
	}
	
	/**
	 * 获取需上传的日对账数据
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> getDRData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String ykc706 = jo.getString("ykc706"); //对账日期
		String euPvtype = jo.getString("euPvtype"); //就诊类型
		String sql = "select * from ins_st_ksyb where del_flag = '0' and to_char(ykc706,'YYYYMMDD') = ? and eu_pvtype = ?";
		
		List<InsZsKsybStKsyb> stList = DataBaseHelper.queryForList(sql, InsZsKsybStKsyb.class, ykc706, euPvtype);
		List<Map<String,Object>> mapList =  new ArrayList<Map<String,Object>>();
		for(int i=0; i<stList.size(); i++){
			Map<String, Object> map =new HashMap<String, Object>();
			map.put("seqno", i+1);
			map.put("aab299", stList.get(i).getAab299());
			map.put("aab301", stList.get(i).getAab301());
			map.put("akc264", stList.get(i).getAkc264());
			map.put("ake149", stList.get(i).getAke149());
			map.put("akc194", stList.get(i).getAkc194());
			//map.put("aaa113", stList.get(i).getAaa113());
			map.put("aaa113", "1");//1:结算  -1：结算回退
			map.put("ykc700", stList.get(i).getYkc700());
			map.put("aaz216", stList.get(i).getAaz216());
			map.put("otransid", stList.get(i).getTransid0304());
			mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 保存日对账返回值
	 * @param param
	 * @param user
	 */
/*	public void savaDayReconciliationData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String transid = jo.getString("transid"); //交易流水号
		String ykc706 = jo.getString("ykc706"); //对账日期
		String euPvtype = jo.getString("euPvtype"); //就诊类型
		int totalrow = Integer.parseInt(jo.getString("totalrow")); //总行数
		int errorcode = Integer.parseInt(jo.getString("errorcode")); //错误代码
		String errormsg = jo.getString("errormsg"); //错误信息
		
		net.sf.json.JSONArray detail = jo.getJSONArray("detail");
		
		String sql = "select * ins_st_ksyb from where del_flag = '0' and to_char(ykc706,'YYYYMMDD') = ? and eu_pvtype = ?";
		List<InsKsybStKsyb> stList = DataBaseHelper.queryForList(sql, InsKsybStKsyb.class, ykc706, euPvtype);
		
		for(InsKsybStKsyb st : stList){
			for(int i=0; i<detail.length(); i++){
				if(st.getTransid0304().equals(detail.getJSONObject(i).get("otransid"))){
					st.setTransid0606(transid);
					st.setErrormsg(errormsg);
					st.setErrorcode(errorcode);
					st.setTotalrow0606(totalrow);
					st.setAae314(Integer.parseInt(detail.getJSONObject(i).get("aae314").toString()));//大于等于0成功，其他失败
					st.setAaa204(detail.getJSONObject(i).get("aaa204").toString());//失败原因
					DataBaseHelper.updateBeanByPk(st);
					break;
				}
			}
		}
	}*/
	
	public DayReconciliationData getDayReconciliationData(String param , IUser user){
/*		JSONObject jo = JSONObject.fromObject(param);
		String ykc706 = jo.getString("ykc706"); //对账日期
		String euPvtype = jo.getString("euPvtype"); //就诊类型
		String sql = "select * ins_st_ksyb from where del_flag = '0' and to_char(ykc706,'YYYYMMDD') = ? and eu_pvtype = ?";
		List<InsKsybStKsyb> stList = DataBaseHelper.queryForList(sql, InsKsybStKsyb.class, ykc706, euPvtype);
		int errorNum = 0; //失败数量
		for(InsKsybStKsyb st : stList){
			if(st.getAae314()<0){
				errorNum++;
			}
		}
		DayReconciliationData drData = new DayReconciliationData();
		drData.setKsybList(stList);
		drData.setErrorNum(errorNum);
		if(errorNum>0){
			drData.setDrStatus("0");
			drData.setMessage("对账失败，有"+errorNum+"条对不上，请根据提示进行人工处理");
		}else{
			drData.setDrStatus("1");
			drData.setMessage("对账成功!");
		}
		return drData;*/
		
		JSONObject jo = JSONObject.fromObject(param);
		String ykc706 = jo.getString("ykc706"); //对账日期
		String euPvtype = jo.getString("euPvtype"); //就诊类型
		String sql = "select  t1.pk_pv, t1.ykc700, t1.aab301, t1.yab060, t1.aac002, t1.aac043, t1.aac044, " +
				"to_char(t2.ykc706,'YYYYMMDD') as ykc706, t2.akc264, t2.aae011, t2.aae036, t2.yzz021, t2.aae314, t2.aaa204, t2.pk_insst " +
				"from ins_pv_out t1 inner join ins_st_ksyb t2 on t1.ykc700 = t2.ykc700 " +
				"where t2.del_flag = '0' and to_char(t2.ykc706,'YYYYMMDD') = ? and t2.eu_pvtype = ?";
		List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql,  ykc706, euPvtype);
		List<Map<String, Object>> returnList  = new ArrayList<Map<String,Object>>();
		int errorNum = 0; //失败数量
		for(int i=0; i<mapList.size(); i++){
			int aae314 = mapList.get(i).get("aae314")==null?-1:Integer.parseInt(mapList.get(i).get("aae314").toString());
			if(aae314<0){
				errorNum++;
			}
			returnList.add(mapList.get(i));
		}
		DayReconciliationData drData = new DayReconciliationData();
		drData.setMapList(returnList);
		drData.setErrorNum(errorNum);
		if(errorNum>0){
			drData.setDrStatus("0");
			drData.setMessage("对账失败，有"+errorNum+"条对不上，请根据提示进行人工处理");
		}else{
			drData.setDrStatus("1");
			drData.setMessage("对账成功!");
		}
		return drData;
	}
	
	/**
	 * 保存重新结算的数据
	 * @param param
	 * @param user
	 */
	public  void saveKsybSettleData(String param , IUser user){

		JSONObject jo = JSONObject.fromObject(param);
		InsZsKsybStKsyb st = JsonUtil.readValue(jo.getString("ksyb"), InsZsKsybStKsyb.class);
		net.sf.json.JSONArray ja = jo.getJSONArray("jjfxList");
		
		//先把原来的数据查出来，然后再做假删，再修改查出来的数据，重新添加一条
		String sql = "select * from ins_st_ksyb where pk_pv = ? and del_flag = '0'";
		InsZsKsybStKsyb ksyb = DataBaseHelper.queryForBean(sql, InsZsKsybStKsyb.class, st.getPkPv());
		
		//这里做一个防范措施，以后对数据可能用到
		sql = "update ins_st_ksyb set del_flag = '1' where pk_settle = ?";
		DataBaseHelper.execute(sql, ksyb.getPkSettle());
		
		ksyb.setAae036(st.getAae036());
		ksyb.setAae011(st.getAae011());
		ksyb.setYzz021(st.getYzz021());
		ksyb.setAaz216(st.getAaz216());
		ksyb.setAkc194(st.getAkc194());
		ksyb.setYkc706(st.getYkc706());
		ksyb.setAka151(st.getAka151());
		ksyb.setAke149(st.getAke149());
		ksyb.setAke039(st.getAke039());
		ksyb.setAke035(st.getAke035());
		ksyb.setAke026(st.getAke026());
		ksyb.setAae045(st.getAae045());
		ksyb.setAke032(st.getAke032());
		ksyb.setAke181(st.getAke181());
		ksyb.setAke173(st.getAke173());
		ksyb.setAkb067(st.getAkb067());
		ksyb.setAke038(st.getAke038());
		ksyb.setAae240(st.getAae240());
		ksyb.setAac002(st.getAac002());
		ksyb.setAac003(st.getAac003());
		ksyb.setAac004(st.getAac004());
		ksyb.setAac006(st.getAac006());
		ksyb.setAab001(st.getAab001());
		ksyb.setAab004(st.getAab001());
		ksyb.setAab019(st.getAab019());
		ksyb.setAab020(st.getAab020());
		ksyb.setAab021(st.getAab021());
		ksyb.setYkc021(st.getYkc021());
		ksyb.setAae140(st.getAae140());
		ksyb.setAke105(st.getAke105());
		ksyb.setAkc070(st.getAkc070());
		ksyb.setAkc071(st.getAkc071());
		ksyb.setAkc072(st.getAkc072());
		ksyb.setAke171(st.getAke171());
		ksyb.setAkc253(st.getAkc253());
		ksyb.setStatus("11");
		
		DataBaseHelper.insertBean(ksyb);
		
		//将原本的基金分项做假删
		sql = "update ins_st_ksyb_jjfx set del_flag = '1' where pk_settle = ?";
		DataBaseHelper.execute(sql, ksyb.getPkSettle());
		for(int i=0; i<ja.size(); i++){
			JSONObject obj = ja.getJSONObject(i);
			InsZsKsybStKsybJjfx jjfx = new InsZsKsybStKsybJjfx();
			jjfx.setYybh(ksyb.getYybh());
			jjfx.setPkHp(ksyb.getPkHp());
			jjfx.setPkPi(ksyb.getPkPi());
			jjfx.setPkPv(ksyb.getPkPv());
			jjfx.setPkSettle(ksyb.getPkSettle());
			jjfx.setEuPvtype(ksyb.getEuPvtype());
			jjfx.setAaa157(obj.getString("aaa157"));
			jjfx.setAaa160(obj.getString("aaa160"));
			jjfx.setAad006(obj.getString("aad006"));
			jjfx.setAae187(obj.getString("aae187"));
			jjfx.setStatus("11");
			DataBaseHelper.insertBean(jjfx);
		}
	}
}