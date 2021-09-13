package com.zebone.nhis.pro.zsba.tpserv.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.module.pv.PvAdt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pi.pub.vo.CommonParam;
import com.zebone.nhis.pi.pub.vo.PibaseVo;
import com.zebone.nhis.pro.zsba.tpserv.dao.TpServItemDeviceMapper;
import com.zebone.nhis.pro.zsba.tpserv.dao.TpServItemMapper;
import com.zebone.nhis.pro.zsba.tpserv.dao.TpServItemRentMapper;
import com.zebone.nhis.pro.zsba.tpserv.vo.PreSettleData;
import com.zebone.nhis.pro.zsba.tpserv.vo.SaveParam;
import com.zebone.nhis.pro.zsba.tpserv.vo.SettleData;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItem;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemDevice;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemRent;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemType;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServPayment;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServRentPayment;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServSettle;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServUnionpayTrading;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 第三方服务项目出租(持久层)
 * @author ZhengRJ
 */
@Service
public class TpServItemRentService {
	
	@Autowired
	private TpServItemRentMapper tpServItemRentMapper;
	@Autowired
	private TpServItemMapper tpServItemMapper;
	@Autowired
	private TpServItemDeviceMapper tpServItemDeviceMapper;

	/**
	 * 录入服务费
	 * @param param
	 * @param user
	 */
	public void saveServCharge(String param, IUser user){
		TpServItemRent rent = JsonUtil.readValue(param, TpServItemRent.class);
		if(rent.getPkRent()==null){
			rent.setRentState("0");
			rent.setPledgeState("0");
			rent.setDelFlag("0");
			rent.setSettleFlag("0");
			rent.setDateCg(new Date());
			DataBaseHelper.insertBean(rent);
		}else{
			DataBaseHelper.updateBeanByPk(rent, false);
		}
	}

	/**
	 * 第三方服务退费
	 * @param param
	 * @param user
	 */
	public void rentRefund(String param, IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkRent = jo.getString("pkRent");
		TpServItemRent rent = DataBaseHelper.queryForBean("select * from tp_serv_item_rent where pk_rent = ?", TpServItemRent.class, pkRent);
		rent.setPkRentBack(rent.getPkRent());
		setDefaultValue(rent, true);
		rent.setPkRent(NHISUUID.getKeyId());
		rent.setNum(rent.getNum()-rent.getNum()-rent.getNum());
		rent.setAmountTotal(rent.getAmountTotal().subtract(rent.getAmountTotal()).subtract(rent.getAmountTotal()));
		rent.setDateCg(new Date());
		User u = (User)user;
		rent.setPkDeptCg(u.getPkDept());
		rent.setPkEmpCg(user.getId());
		rent.setNameEmpCg(user.getUserName());
		DataBaseHelper.insertBean(rent);
	}
	
	/**
	 * 获取患者的历史住院信息
	 * @param cparam
	 * @return
	 */
	public List<Map<String,Object>> getPibaseAndAmountVo(CommonParam cparam) {
		String fieldName = cparam.getFieldName();
		String fieldValue = cparam.getFieldValue();
        String pkDeptNs = cparam.getPkDeptNs();
		
		StringBuffer sb =new StringBuffer();
		sb.append("select  ");
		if ("id_no".equals(fieldName)) {// 证件号码
			sb.append(" t2.id_no as query_no");
		} else if ("insur_no".equals(fieldName)) {// 医保卡号
			sb.append(" t2.insur_no as query_no");
		} else if ("card_no".equals(fieldName)) { // 就诊卡号
			sb.append(" t3.card_no as query_no");
		}  else if ("code_ip".equals(fieldName)) {// 住院号
			sb.append(" t2.code_ip as query_no");
		} else if ("bed_no".equals(fieldName)) { // 当前床位
			sb.append(" t1.bed_no as query_no");
		} else if ("code_pi".equals(fieldName)) { // 患者编号
			sb.append(" t2.code_pi as query_no");
		} else {
			throw new BusException("参数错误！");
		}
		sb.append(",t4.ip_times,t1.name_pi,t1.dt_sex,t1.pk_dept, t1.pk_pv");
		sb.append(" from PV_ENCOUNTER t1 inner join PI_MASTER t2 on t1.pk_pi = t2.pk_pi");
		sb.append(" left join pi_card t3 on t2.pk_pi = t3.pk_pi and t3.del_flag = '0' and t3.EU_STATUS = '0' and t3.FLAG_ACTIVE = '1' ");
		sb.append(" inner join pv_ip t4 on t4.pk_pv = t1.pk_pv");
		sb.append(" where 1=1");
		if ("id_no".equals(fieldName)) {// 证件号码
			sb.append(" and t2.id_no like '%"+fieldValue+"%'");
		} else if ("insur_no".equals(fieldName)) {// 医保卡号
			sb.append(" and t2.insur_no like '%"+fieldValue+"%'");
		} else if ("card_no".equals(fieldName)) { // 就诊卡号
			sb.append(" and t2.code_ip like '%"+fieldValue+"%'");
		}  else if ("code_ip".equals(fieldName)) {// 住院号
			sb.append(" and t3.card_no like '%"+fieldValue+"%'");
		} else if ("bed_no".equals(fieldName)) { // 当前床位
			sb.append(" and t1.bed_no like '%"+fieldValue+"%'");
		} else if ("code_pi".equals(fieldName)) { // 患者编号
			sb.append(" and t2.code_pi like '%"+fieldValue+"%'");
		} else {
			throw new BusException("参数错误！");
		}
		if(pkDeptNs!=null){
			sb.append(" and t1.pk_dept_ns = '"+pkDeptNs +"'");
		}
		sb.append(" order by t1.name_pi,t4.ip_times asc");
		List<Map<String,Object>> mapList = DataBaseHelper.queryForList(sb.toString());
		return mapList;
	}
	
	/**
	 * 根据就诊主键获取患者信息和待缴费信息、已缴费信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> getPatientInfoAndServFeeList(String param, IUser user) {
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		StringBuffer sb = new StringBuffer();
		sb.append("select t1.pk_pv, t1.name_pi, t1.dt_sex, t3.code_ip, t2.ip_times, t1.date_begin, t1.date_end ");
		sb.append("from pv_encounter t1 inner join pv_ip t2 on t1.pk_pv = t2.pk_pv ");
		sb.append("inner join pi_master t3 on t1.pk_pi = t3.pk_pi where t1.pk_pv = ?");
		Map<String,Object> map = DataBaseHelper.queryForMap(sb.toString(), pkPv);
		if(map == null){
			throw new BusException("查不到患者的住院信息！");
		}
		// 住院天数 -- 出院则用出院时间计算，未出院用当前日期
		if( map.get("dateBegin") != null && map.get("dateEnd")!=null ){
			map.put("days", DateUtils.getDateSpace(DateUtils.strToDate(map.get("dateBegin").toString()), DateUtils.strToDate(map.get("dateEnd").toString())));
		}else if (map.get("dateBegin") != null) {
			Date hosDate = DateUtils.strToDate(map.get("dateBegin").toString().replace("-", "").replace(" ", "").replace(":", "").replace("/", ""));
			map.put("days", DateUtils.getDateSpace(hosDate, new Date()));
		} else {
			map.put("days", "0");
		}
		
		//未缴护工费
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String dateBegin = map.get("dateBegin").toString().substring(0, 10);
		String	dateEnd = map.get("dateEnd")==null?DateUtils.dateToStr("yyyy-MM-dd", new Date()):map.get("dateEnd").toString().substring(0, 10);
		Map<String, Object> hgfMap = calculationNursingFee(pkPv, dateBegin, dateEnd);
		if(hgfMap!=null&&hgfMap.size()>0){
			mapList.add(hgfMap);
		}
		if(hgfMap!=null&&hgfMap.size()>0){
			map.put("wjts", hgfMap.get("num"));
		}else{
			map.put("wjts", "0");
		}
		
		//新生儿科费用
		List<Map<String, Object>> xsekList = getNewbornPediatricsData(pkPv, dateBegin.replace("-", ""), dateEnd.replace("-", ""));
		for (Map<String, Object> xsekMap : xsekList) {
			BigDecimal decimal = new BigDecimal(xsekMap.get("price").toString());
			xsekMap.put("price", decimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
			decimal = new BigDecimal(xsekMap.get("amountTotal").toString());
			xsekMap.put("amountTotal", decimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
			mapList.add(xsekMap);
		}
		
		map.put("dateBegin", dateBegin);
		if(map.get("dateEnd")!=null){
			map.put("dateEnd", dateEnd);
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("patientInfo", map);
		returnMap.put("wjhgfList",mapList);
		
		//已缴费列表
		returnMap.put("yjfList",getSettleList(pkPv));
		return returnMap;
	}
	
	/**
	 * 获取未结算的非医疗费用列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getServFeeList(String param, IUser user) {
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		String dateBegin = jo.getString("dateBegin");
		String dateEnd = jo.getString("dateEnd");
		
		//未缴护工费
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPv", pkPv);
		paramMap.put("dateBegin", dateBegin);
		paramMap.put("dateEnd", dateEnd);
		Map<String, Object> hgfMap = calculationNursingFee(pkPv, dateBegin, dateEnd);
		if(hgfMap!=null&&hgfMap.size()!=0){
			mapList.add(hgfMap);
		}
		
		//新生儿科费用
		List<Map<String, Object>> xsekList = getNewbornPediatricsData(pkPv, dateBegin.replace("-", ""), dateEnd.replace("-", ""));
		for (Map<String, Object> xsekMap : xsekList) {
			BigDecimal decimal = new BigDecimal(xsekMap.get("price").toString());
			xsekMap.put("price", decimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
			decimal = new BigDecimal(xsekMap.get("amountTotal").toString());
			xsekMap.put("amountTotal", decimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
			mapList.add(xsekMap);
		}
		return mapList;
	}
	
	/**
	 * 非医疗费用预结算(住院用的：护工+新生儿科费用)
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public Map<String, Object> hosPreSettle(String param, IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String) paraMap.get("pkPv");
		String dateBegin = (String) paraMap.get("dateBegin");
		String dateEnd = (String) paraMap.get("dateEnd");
		if(dateEnd == null){
			dateEnd = DateUtils.getDateStr(new Date());
		}
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		BigDecimal totalCost = new BigDecimal(0);//总费用
		
		//护工费
		Map<String, Object> hgfMap = calculationNursingFee(pkPv,
				dateBegin.subSequence(0, 4)+"-"+dateBegin.subSequence(4, 6)+"-"+dateBegin.substring(6, 8),
				dateEnd.subSequence(0, 4)+"-"+dateEnd.subSequence(4, 6)+"-"+dateEnd.substring(6, 8) );
		if(hgfMap!=null&&hgfMap.size()>0){
			returnMap.put("hgdj", Double.parseDouble(hgfMap.get("price").toString()));
			returnMap.put("hzzyts", Integer.parseInt(hgfMap.get("num").toString()));
			returnMap.put("hgf", Double.parseDouble(hgfMap.get("amountTotal").toString()));
			totalCost = totalCost.add(new BigDecimal(hgfMap.get("amountTotal").toString()));
		}else{
			returnMap.put("hgdj", "0.00");
			returnMap.put("hzzyts", "0");
			returnMap.put("hgf", "0.00");
		}

		
/*		if(preSettleData.getHgf()==0){
			preSettleData.setIfPay("0");
		}else{
			preSettleData.setIfPay("1");
		}*/
		//新生儿科费用
		List<Map<String,Object>> mapList = getNewbornPediatricsData(pkPv, dateBegin.substring(0, 8), dateEnd.substring(0, 8));
		for (Map<String, Object> map : mapList) {
			totalCost = totalCost.add(new BigDecimal(map.get("amountTotal").toString()));
		}
		returnMap.put("wjsmxList", mapList);
		returnMap.put("totalCost", totalCost);
		return returnMap;
	}
	
	/**
	 * 非医疗费用保存结算数据（住院用的）
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void saveHosSettleData(String param, IUser user){
		SettleData master = JsonUtil.readValue(param, SettleData.class);
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", master.getPkPv());
		
		//获取患者各种信息
		Map<String,Object> piMap = tpServItemRentMapper.getPatientInfo(param_h);
		
		//第一步：生成结算记录
		TpServSettle settle = new TpServSettle();
		settle.setPkPi(piMap.get("pk_pi")==null?null:piMap.get("pk_pi").toString());
		settle.setPkPv(master.getPkPv());
		settle.setIpTimes(piMap.get("ip_times")==null?0:Integer.parseInt(piMap.get("ip_times").toString()));
		settle.setOpTimes(piMap.get("op_times")==null?0:Integer.parseInt(piMap.get("op_times").toString()));
		settle.setAmountSt(master.getPayAmount());
		settle.setDateSt(new Date());
		settle.setPkOrgSt(master.getPkOrgSt());
		settle.setPkDeptSt(master.getPkDept());
		settle.setPkEmpSt(master.getPkEmpPay());
		settle.setNameEmpSt(master.getNameEmpPay());
		settle.setCancFlag("0");
		DataBaseHelper.insertBean(settle);
		
		//第二步：生成交易记录和缴费记录
		TpServPayment sp = new TpServPayment(); //缴费记录
		if(master.getPayAmount().compareTo(BigDecimal.ZERO)!=0){ 
			//生成交易记录
			if(master.getPayType().equals("1")){//银联
				TpServUnionpayTrading ut = master.getUnionpayTrading();
				ut.setPkPv(master.getPkPv()); //就诊主键
				ut.setPkPatient(piMap.get("pk_pi")==null?null:piMap.get("pk_pi").toString()); //患者主键
				ut.setWorkid(master.getCodeEmp());
				ut.setHtime(new Date());
				DataBaseHelper.insertBean(ut);
				
				sp.setPkThirdParty(ut.getPkUnionpayTrading());
				sp.setPayMethod("1");
			}else if(master.getPayType().equals("2")){//支付宝
				//保存支付宝交易记录
				//将交易记录的主键set进sp
				sp.setPayMethod("2");
			}else if(master.getPayType().equals("3")){//龙闪付
				//保存龙闪付交易记录
				//将交易记录的主键set进sp
				sp.setPayMethod("3");
			}else if(master.getPayType().equals("0")){//现金
				sp.setPayMethod("0");
			}
			
			//第二步，生成缴费记录
			sp.setPkPv(master.getPkPv());
			sp.setPkPatient(piMap.get("pk_pi")==null?null:piMap.get("pk_pi").toString()); //缴费患者id
			sp.setAmount(master.getPayAmount());
			sp.setIsPledge("0");
			sp.setDatePay(new Date());
			sp.setPkDept(master.getPkDept());
			sp.setPkEmpPay(master.getPkEmpPay());
			sp.setNameEmpPay(master.getNameEmpPay());
			sp.setSettleFlag("1");
			sp.setPkServSettle(settle.getPkServSettle());
			DataBaseHelper.insertBean(sp);
		}

		//Map<String, Object> timesMap = DataBaseHelper.queryForMap("select max(settle_times) as settle_times from tp_serv_item_rent where fk_patient = ?", piMap.get("pk_pi"));
		
		//第三步，保存项目出租信息（护工费||新生儿科费用）
		if(master.getHzzyts()>0){//有护工费
			TpServItemRent hgfRent = new TpServItemRent();//护工费项目
			Map<String,Object> hgfMap = new HashMap<String, Object>();
			String sql = "select t1.name, t1.fk_depts, t1.price, t1.pk_item  " +
					" from tp_serv_item t1 inner join tp_serv_item_type t2 on  t1.fk_item_type = t2.pk_item_type  " +
					" and t1.del_flag = '0' and t2.del_flag = '0' and t1.use_flag = '0' and t2.use_flag = '0'" +
					" where t2.name = ?";
			List<Map<String,Object>> itemList = DataBaseHelper.queryForList(sql,  "护工费");
			for(int i=0; i<itemList.size(); i++){
				if(itemList.get(i).get("fkDepts").toString().indexOf(piMap.get("pk_dept_ns").toString())!=-1){
					hgfMap = itemList.get(i);
				}
			}
			
			//生成护工费的出租项目
			hgfRent.setPkPv(master.getPkPv());
			//hgfRent.setFkDept(piMap.get("pk_dept_admit")==null?null:piMap.get("pk_dept_admit").toString());//所属科室
			hgfRent.setFkPatient(piMap.get("pk_pi")==null?null:piMap.get("pk_pi").toString());//患者主键
			hgfRent.setPatientName(piMap.get("name_pi")==null?null:piMap.get("name_pi").toString());//患者名称
			hgfRent.setPhone(piMap.get("mobile")==null?null:piMap.get("mobile").toString());//联系人手机号码
			if(hgfMap!=null && hgfMap.containsKey("pkItem")){
				hgfRent.setFkItem(hgfMap.get("pkItem").toString());//项目主键  根据名称去项目表查
				hgfRent.setItemName(hgfMap.get("name").toString());//项目名称
				hgfRent.setPrice(new BigDecimal(hgfMap.get("price").toString()));//单价  
			}
			hgfRent.setNum(master.getHzzyts());//数量
			hgfRent.setPledgeState("0");//0 ：无押金
			hgfRent.setRentState("1");//出租状态  1:完结
			hgfRent.setSjDateNum(master.getHzzyts());//实际天数
			hgfRent.setAmountTotal(master.getHgf());//总费用
			hgfRent.setDifferAmount(master.getHgf());//差额
			hgfRent.setSettleFlag("1");
			hgfRent.setPkServSettle(settle.getPkServSettle());
			DataBaseHelper.insertBean(hgfRent);
		}
		
		//修改新生儿科费用结算状态
		if(master.getPayAmount().subtract(master.getHgf()).compareTo(BigDecimal.ZERO)==1){
			List<Map<String,Object>> mapList = getNewbornPediatricsData(master.getPkPv(), DateUtils.dateToStr("yyyy-MM-dd", master.getDateBegin()), DateUtils.dateToStr("yyyy-MM-dd", master.getDateEnd()));
			for (Map<String, Object> map : mapList) {
				TpServItemRent rent = new TpServItemRent();
				rent.setPkRent(map.get("pk_rent").toString());
				rent.setRentState("1");
				rent.setSettleFlag("1");
				rent.setPkServSettle(settle.getPkServSettle());
				rent.setDifferAmount(new BigDecimal(map.get("amountTotal").toString()));
				DataBaseHelper.updateBeanByPk(rent,false);
			}
		}
	}
	
	/**
	 * 根据结算主键获取结算明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getChargeInfoList(String param, IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkServSettle = jo.getString("pkServSettle");
		
		StringBuffer sb = new StringBuffer();
		sb.append("select t2.name as item_name, t4.name as unit, t2.price, t1.num, t1.amount_total as amount");
		sb.append(" from tp_serv_item_rent t1 inner join tp_serv_item t2 on t1.fk_item = t2.pk_item ");
		sb.append(" and t1.del_flag = '0' and t2.use_flag = '0' and t2.del_flag = '0'");
		sb.append(" inner join tp_serv_item_unit t4 on t4.pk_item_unit = t2.fk_item_unit and t4.use_flag = '0' and t4.del_flag = '0'");
		sb.append(" inner join tp_serv_settle t5 on t5.pk_serv_settle = t1.pk_serv_settle and t5.del_flag = '0'");
		sb.append(" where t1.del_flag = '0' and t5.pk_serv_settle = ?");
		
		List<Map<String,Object>> mapList = DataBaseHelper.queryForList(sb.toString(), pkServSettle);
		return mapList;
	}
	
	/**
	 * 获取撤销方法
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<Object, String> getUndoMethod(String param, IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkServSettle = jo.getString("pkServSettle");
		String deptType =  jo.getString("deptType");
		Map<Object, String> returnMap = new HashMap<Object, String>();
		//住院的不用考虑押金
		TpServPayment pay = DataBaseHelper.queryForBean("select * from tp_serv_payment where pk_serv_settle = ? and is_pledge = '0'", TpServPayment.class, pkServSettle);
		if(pay!=null){
			if(pay.getPayMethod().equals("2")){//银联
				if(isNow(pay.getCreateTime())){
					returnMap.put("payMethod", "2");
					//TpServUnionpayTrading tsut = DataBaseHelper.queryForBean("select * from tp_serv_unionpay_trading where pk_unionpay_trading = ?", TpServUnionpayTrading.class, pay.getFkUnionpayTrading());
					//returnMap.put("lsh", tsut.getSystracdno());
				}else{
					if(deptType.equals("08")){
						returnMap.put("payMethod", "0");
					}else{
						throw new BusException("银行卡缴费不是当天撤销的，请到收费处退现金!");
					}
				}
			}else if(pay.getPayMethod().equals("1")){//支付宝
				returnMap.put("payMethod", "1");
			}else if(pay.getPayMethod().equals("0")){//现金
				returnMap.put("payMethod", "0");
			}
		}
		return returnMap;
	}
	
	/**
	 * 取消结算
	 * @param param
	 * @param user
	 */
	public void cancelSettle(String param, IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkServSettle = jo.getString("pkServSettle");
		String payMethod = jo.getString("payMethod");
		String pkDept = jo.getString("pkDept");
		//1.添加一条负的结算记录
		TpServSettle settle = DataBaseHelper.queryForBean("select * from tp_serv_settle where pk_serv_settle = ?", TpServSettle.class, pkServSettle);
		settle.setCancFlag("1");
		settle.setPkServSettleCanc(settle.getPkServSettle());
		settle.setAmountSt(settle.getAmountSt().subtract(settle.getAmountSt()).subtract(settle.getAmountSt()));
		settle.setPkOrgSt(UserContext.getUser().getPkOrg());
		settle.setPkDeptSt(pkDept);
		settle.setPkEmpSt(user.getId());
		settle.setNameEmpSt(user.getUserName());
		setDefaultValue(settle, true);
		settle.setPkServSettle(NHISUUID.getKeyId());
		DataBaseHelper.insertBean(settle);
		
		//2.添加一条负的缴费记录，暂不做押金处理
		TpServPayment pay = DataBaseHelper.queryForBean("select * from tp_serv_payment where pk_serv_settle = ?", TpServPayment.class, pkServSettle);
		pay.setPayMethod(payMethod);
		pay.setAmount(pay.getAmount().subtract(pay.getAmount()).subtract(pay.getAmount()));
		pay.setDatePay(new Date());
		pay.setPkDept(pkDept);
		pay.setPkEmpPay(user.getId());
		pay.setNameEmpPay(user.getUserName());
		pay.setPkServSettle(settle.getPkServSettle());
		setDefaultValue(pay, true);
		pay.setPkPayment(NHISUUID.getKeyId());
		DataBaseHelper.insertBean(pay);
		
		//3.添加交易记录（银行卡交易数据等）
		
		//4.处理记费明细，区分护工费和新生儿科费用
		List<TpServItemRent> rentList = DataBaseHelper.queryForList("select * from tp_serv_item_rent where pk_serv_settle = ?", TpServItemRent.class, pkServSettle);
		for(TpServItemRent rent : rentList){
			StringBuffer sb = new StringBuffer();
			sb.append("select t3.code from tp_serv_item_rent t1 inner join tp_serv_item t2 on t1.fk_item = t2.pk_item");
			sb.append(" inner join tp_serv_item_type t3 on t2.fk_item_type = t3.pk_item_type where t1.pk_rent = ?");
			Map<String, Object> typeMap = DataBaseHelper.queryForMap(sb.toString(), rent.getPkRent());
			//护工费护理，插入负的记录;  
			//新生儿科费用， 插入负的记录并添加一条未结算的记录
			TpServItemRent rentTwo = new TpServItemRent();
			try {
				Copy(rent, rentTwo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				new BusException("程序转换实体类错误，请联系管理员");
			}
			
			rent.setNum(rent.getNum()-rent.getNum()-rent.getNum());
			rent.setAmountTotal(rent.getAmountTotal().subtract(rent.getAmountTotal()).subtract(rent.getAmountTotal()));
			rent.setDifferAmount(rent.getDifferAmount().subtract(rent.getDifferAmount()).subtract(rent.getDifferAmount()));
			rent.setPkServSettle(settle.getPkServSettle());
			setDefaultValue(rent, true);
			rent.setPkRent(NHISUUID.getKeyId());
			DataBaseHelper.insertBean(rent);
			if(typeMap.get("code").equals("002")){//新生儿科插入未结算记录
				rentTwo.setRentState("0");
				rentTwo.setSettleFlag("0");
				rentTwo.setPkServSettle(null);
				rentTwo.setTs(null);
				rentTwo.setPkRent(NHISUUID.getKeyId());
				DataBaseHelper.insertBean(rentTwo);
			}
		}
	}
	
	/**
	 * 计算护工费
	 * @param pkPv
	 * @param dateBegin（YYYY-MM-DD）
	 * @param dateEnd（YYYY-MM-DD）
	 * @return
	 */
	public Map<String, Object> calculationNursingFee(String pkPv, String dateBegin, String dateEnd){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		if(dateBegin.length()==0){
			throw new BusException("计算护工费的开始日期不能为空");
		}
		if(dateEnd.length()==0){
			dateEnd = DateUtils.dateToStr("yyyy-MM-dd", new Date());
		}
		
		PvEncounter pe = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ? and del_flag = '0'", PvEncounter.class, pkPv);
		if(pe==null){
			throw new BusException("查不到住院记录");
		}

		
		//护工费
		//分两种：成人护理、儿童护理，通过病区区分
		String sql = "select t1.name as item_name, t1.fk_depts, t1.price, t3.name as unit_name, t1.pk_item " +
				" from tp_serv_item t1 inner join tp_serv_item_type t2 on  t1.fk_item_type = t2.pk_item_type  " +
				" and t1.del_flag = '0' and t2.del_flag = '0' and t1.use_flag = '0' and t2.use_flag = '0'" +
				" inner join tp_serv_item_unit t3 on t1.fk_item_unit = t3.pk_item_unit " +
				" where t2.name = ?";
		List<Map<String,Object>> itemList = DataBaseHelper.queryForList(sql,  "护工费");
		for(Map<String, Object> item : itemList){
			String[] arr = item.get("fkDepts").toString().split(",");
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<arr.length; i++){
					sb.append("'"+arr[i]+"',");
			}
			sql = "select * from pv_adt where pk_pv = ? and pk_dept_ns not in ("+sb.toString().substring(0, sb.toString().length()-1)+")";
			List<PvAdt> adtList = DataBaseHelper.queryForList(sql, PvAdt.class, pkPv);
			int day = 0;//不收护工费天数
			for(int i=0; i<adtList.size(); i++){
				if(adtList.get(i).getDateEnd()!=null){
					if(adtList.get(i).getPkAdtSource()==null){
						//入院登记那条如果是当天开始当天结束算一天
						day = (int) (day+(getDaySubTwo(DateUtils.dateToStr("yyyy-MM-dd", adtList.get(i).getDateBegin()),DateUtils.dateToStr("yyyy-MM-dd", adtList.get(i).getDateEnd()))));
					}else{
						//转院的当天开始当天结束算0天
						day = (int) (day+(getDaySub(DateUtils.dateToStr("yyyy-MM-dd", adtList.get(i).getDateBegin()),DateUtils.dateToStr("yyyy-MM-dd", adtList.get(i).getDateEnd()))));
					}
				}else{
					if(adtList.get(i).getPkAdtSource()==null){
						day = (int) (day+(getDaySubTwo(DateUtils.dateToStr("yyyy-MM-dd", adtList.get(i).getDateBegin()),DateUtils.dateToStr("yyyy-MM-dd", new Date()))));
					}else{
						day = (int) (day+(getDaySub(DateUtils.dateToStr("yyyy-MM-dd", adtList.get(i).getDateBegin()),DateUtils.dateToStr("yyyy-MM-dd", new Date()))));
					}
				}
			}
			sql = "select date_admit from pv_encounter where pk_pv = ?";
			List<Map<String,Object>> encounterList = DataBaseHelper.queryForList(sql,  pkPv);
			int dayCount = 0;
			if(encounterList.size()!=0){
				if(encounterList.get(0).get("dateAdmit")!=null){
					dayCount = (int) getDaySubTwo(encounterList.get(0).get("dateAdmit").toString().substring(0, 10), dateEnd);//总天数
				}
			}

			Map<String,Object> param_h = new HashMap<String,Object>();
			param_h.put("pkPv", pkPv);
			param_h.put("pkItem", item.get("pkItem").toString());
			Map<String,Object> map = tpServItemRentMapper.getHgfNum(param_h);//已经结算的天数
			BigDecimal hgf = new BigDecimal(dayCount - day-(map==null?0:Integer.parseInt(map.get("num").toString()))).multiply(new BigDecimal(item.get("price").toString()));
			if(hgf.compareTo(BigDecimal.ZERO)==1){
				DecimalFormat df = new DecimalFormat("#.00");
				returnMap = new HashMap<String, Object>();
				returnMap.put("itemName", item.get("itemName").toString());
				returnMap.put("unit", item.get("unitName").toString());
				returnMap.put("price", df.format(Double.parseDouble(item.get("price").toString())));
				returnMap.put("num", dayCount - day-(map==null?0:Integer.parseInt(map.get("num").toString())));
				returnMap.put("amountTotal", hgf.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
		}
		//成人护理和儿科护理不会同时出现的，所以返回一个就行了
		return returnMap;
	}
	
	/**
	 * 获取新生儿科未结算数据明细
	 * @param pkPv
	 * @param dateBegin（YYYY-MM-DD）
	 * @param dateEnd（YYYY-MM-DD）
	 * @return
	 */
	public List<Map<String,Object>> getNewbornPediatricsData(String pkPv, String dateBegin, String dateEnd){
		if(dateBegin.length()==0){
			throw new BusException("开始日期不能为空");
		}
		if(dateEnd.length()==0){
			dateEnd = DateUtils.dateToStr("yyyyMMdd", new Date());
		}
		
		PvEncounter pe = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ? and del_flag = '0'", PvEncounter.class, pkPv);
		if(pe==null){
			throw new BusException("查不到住院记录");
		}

		//新生儿科费用  测试编码为002
		TpServItemType type = DataBaseHelper.queryForBean("select * from tp_serv_item_type where code = ?", TpServItemType.class, "002");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkItemType", type.getPkItemType());
		paramMap.put("pkPv", pkPv);
		paramMap.put("dateBegin", dateBegin);
		paramMap.put("dateEnd", dateEnd);
		List<Map<String,Object>> mapList  = tpServItemRentMapper.getNewbornPediatricsData(paramMap);
		List<Map<String,Object>> returnMapList = new ArrayList<Map<String,Object>>();
		//金额保留两位小数
		for (Map<String, Object> map : mapList) {
			BigDecimal decimal = new BigDecimal(map.get("price").toString());
			map.put("price", decimal.setScale(2,BigDecimal.ROUND_HALF_DOWN).toString());
			decimal = new BigDecimal(map.get("amountTotal").toString());
			map.put("amountTotal", decimal.setScale(2,BigDecimal.ROUND_HALF_DOWN).toString());
			returnMapList.add(map);
		}
		return returnMapList;
	}
	
	/**
	 * 获取新生儿科未结算总费用
	 * @param pkPv
	 * @param dateBegin（YYYY-MM-DD）
	 * @param dateEnd（YYYY-MM-DD）
	 * @return
	 */
	public List<Map<String,Object>> getNewbornPediatricsTotalCost(String pkPv, String dateBegin, String dateEnd){
		if(dateBegin.length()==0){
			throw new BusException("开始日期不能为空");
		}
		if(dateEnd.length()==0){
			dateEnd = DateUtils.dateToStr("yyyyMMdd", new Date());
		}
		
		PvEncounter pe = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ? and del_flag = '0'", PvEncounter.class, pkPv);
		if(pe==null){
			throw new BusException("查不到住院记录");
		}

		//新生儿科费用  测试编码为002
		TpServItemType type = DataBaseHelper.queryForBean("select * from tp_serv_item_type where code = ?", TpServItemType.class, "002");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkItemType", type.getPkItemType());
		paramMap.put("pkPv", pkPv);
		paramMap.put("dateBegin", dateBegin.replace("-", ""));
		paramMap.put("dateEnd", dateEnd.replace("-", ""));
		List<Map<String,Object>> mapList  = tpServItemRentMapper.getNewbornPediatricsTotalCost(paramMap);
		List<Map<String,Object>> returnMapList = new ArrayList<Map<String,Object>>();
		//金额保留两位小数
		for (Map<String, Object> map : mapList) {
			map.put("Name", map.get("name"));
			BigDecimal decimal = new BigDecimal(map.get("amountTotal").toString());
			map.put("Amt", decimal.setScale(2,BigDecimal.ROUND_HALF_DOWN).toString());
			map.put("AmtPi", decimal.setScale(2,BigDecimal.ROUND_HALF_DOWN).toString());
			returnMapList.add(map);
		}
		return returnMapList;
	}
	
	
	/**
	 * 获取已缴费列表
	 * @param pkPv
	 * @return
	 */
	public List<Map<String, Object>> getSettleList(String pkPv){
		StringBuffer sb = new StringBuffer();
		sb.append("select t4.name as type_name, t1.pk_serv_settle,t1.amount_st as amount,t1.DATE_ST as date_settle, CASE t5.canc_flag WHEN '1' THEN '1' ELSE '0' END as canc_flag");
		sb.append(" from TP_SERV_SETTLE t1 INNER JOIN tp_serv_item_rent t2 on t1.pk_serv_settle = t2.pk_serv_settle and t1.canc_flag = '0'");
		sb.append(" inner join tp_serv_item t3 on t3.pk_item = t2.fk_item");
		sb.append(" inner join tp_serv_item_type t4 on t4.pk_item_type = t3.fk_item_type");
		sb.append(" LEFT JOIN TP_SERV_SETTLE t5 on t5.pk_serv_settle_canc = t1.pk_serv_settle");
		sb.append(" where t1.pk_pv = ?");
		sb.append(" group by t1.pk_serv_settle, t1.DATE_ST,t4.name, t5.canc_flag, t1.amount_st");
		return DataBaseHelper.queryForList(sb.toString(), pkPv);
	}
	
	/**
	 * 中途、出院结算界面的费用分类汇总所需的非医疗费用明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> QryPatiCgCateTp(String param, IUser user){
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String) paraMap.get("pkPv");
		//String euSttype = (String) paraMap.get("euSttype");
		String dateBegin = (String) paraMap.get("dateBegin");
		String dateEnd = (String) paraMap.get("dateEnd");
		if(pkPv==null || pkPv.trim().length()==0){
			throw new BusException("入参不能为空！");
		}
		
		dateBegin = dateBegin.substring(0,4)+"-"+dateBegin.substring(4,6)+"-"+dateBegin.substring(6,8);
		dateEnd = dateEnd.substring(0,4)+"-"+dateEnd.substring(4,6)+"-"+dateEnd.substring(6,8);
		//新生儿科费用
		List<Map<String, Object>>  returnMapList = getNewbornPediatricsTotalCost(pkPv, dateBegin, dateEnd);
		
		//护工费
		Map<String,Object> hgfMap = calculationNursingFee(pkPv, dateBegin, dateEnd);
		if(hgfMap!=null && hgfMap.size()>0){
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("Name", hgfMap.get("itemName"));
			returnMap.put("Amt", hgfMap.get("amountTotal"));
			returnMap.put("AmtPi", hgfMap.get("amountTotal"));
			returnMapList.add(returnMap);
		}
		return returnMapList;
	}
	
	/**
	 * 获取非医疗费用总额
	 * @param param
	 * @param user
	 * @return
	 */
	public String getNonMedicalExpenses(String param, IUser user){
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String) paraMap.get("pkPv");
		if(pkPv==null || pkPv=="null" || pkPv.trim().length()==0){
			throw new BusException("pkPv不能为空！");
		}
		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);
		Date begin  = checkMidSettle(pkPv);
		begin = begin==null?pvvo.getDateBegin():begin;
		
		String dateBegin = DateUtils.getDateStr(begin);
		dateBegin = dateBegin.substring(0,4)+"-"+dateBegin.substring(4,6)+"-"+dateBegin.substring(6,8);
		String dateEnd = null;
		if(pvvo.getDateEnd()==null){
			dateEnd = DateUtils.getDate();
		}else{
			dateEnd = DateUtils.getDateStr(pvvo.getDateEnd());
			dateEnd = dateEnd.substring(0,4)+"-"+dateEnd.substring(4,6)+"-"+dateEnd.substring(6,8);
		}
		
		//新生儿科费用
		List<Map<String, Object>>  returnMapList = getNewbornPediatricsTotalCost(pkPv, dateBegin, dateEnd);
		
		BigDecimal totalCost = new BigDecimal(0);
		//护工费
		Map<String,Object> hgfMap = calculationNursingFee(pkPv, dateBegin, dateEnd);
		if(hgfMap!=null && hgfMap.size()>0){
			totalCost = totalCost.add(new BigDecimal(hgfMap.get("amountTotal").toString()));
		}
		
		for (Map<String, Object> map : returnMapList) {
			totalCost = totalCost.add(new BigDecimal(map.get("Amt").toString()));
		}
		return totalCost.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public List<PibaseVo> getPibaseVoList(String param, IUser user){
		PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
		String pkOrg = UserContext.getUser().getPkOrg();
		encounter.setPkOrg(pkOrg);
		//当就诊类型为3时，使用date_begin字段匹配参数dateClinic；其余情况下，使用date_clinic字段匹配参数dateClinic
		if("3".equals(encounter.getEuPvtype())){
			if(encounter.getDateClinic() != null){
				encounter.setDateBegin(encounter.getDateClinic());	
				encounter.setDateClinic(null);
			}
		}
		List<PibaseVo> voList = tpServItemRentMapper.getPibaseVoList(encounter);
		List<PibaseVo> voListTwo = new ArrayList<PibaseVo>();
		for(int i=0; i<voList.size(); i++){
			List<TpServPayment> paymentList = DataBaseHelper.queryForList("select * from tp_serv_payment where pk_pv = ? and is_pledge = '1' and del_flag = '0'", TpServPayment.class, voList.get(i).getPkPv());
			if(paymentList.size()>0){
				voListTwo.add(voList.get(i));
			}
		}
		return voListTwo;
	}
	
	public List<Map<String,Object>> getPibaseAndAmountVo(String param, IUser user) {
		CommonParam cparam = JsonUtil.readValue(param, CommonParam.class);
		List<Map<String,Object>> mapList = getPibaseAndAmountVo(cparam);
		return mapList;
	}

    
	/**
	 * 保存银联交易记录、缴费记录、项目出租信息(暂时不用)
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void saveServItemRent(String param, IUser user){
			SaveParam master = JsonUtil.readValue(param, SaveParam.class);
			
			TpServItemDevice device = null;
			if(master.getServItemRent().getFkItemDevice()!=null){
				device = new TpServItemDevice();
				device.setPkItemDevice(master.getServItemRent().getFkItemDevice());
				List<TpServItemDevice> list = tpServItemDeviceMapper.getServItemDeviceList(device);
				device = list.get(0);
				
				if(device.getNum()-device.getRentedAlreadyNum()<master.getServItemRent().getNum()){
					throw new BusException("设备数量不足，只剩"+(device.getNum()-device.getRentedAlreadyNum())+"台！");
				}
			}
			
			TpServItem servItem = new TpServItem();
			//根据主键去查询项目
			servItem.setPkItem(master.getServItemRent().getFkItem());
			List<TpServItem> list = tpServItemMapper.getServItemList(servItem);	
			
			TpServUnionpayTrading  servUnionpayTrading  = new TpServUnionpayTrading();
			
			if(master.getServUnionpayTrading()!=null){
				servUnionpayTrading = master.getServUnionpayTrading();
				//患者主键、操作员工号、操作时间
				//保存银联交易记录
				servUnionpayTrading.setHtime(new Date());
				servUnionpayTrading.setPkPatient(master.getServItemRent().getFkPatient());
				servUnionpayTrading.setWorkid(user.getId());//操作者工号，目前存的是登录者的id，后续再看是否需要修改
				DataBaseHelper.insertBean(servUnionpayTrading);
				

			}
			
			//保存出租信息
			TpServItemRent rent = master.getServItemRent();
			rent.setTs(new Date());
			if(rent.getPkRent()==null){
				if(rent.getCashPledge().compareTo(BigDecimal.ZERO)==1){
					rent.setPledgeState("1");
				}
				rent.setFkPatient(master.getServItemRent().getFkPatient());		
				//rent.setSettleTimes(0);
				rent.setDelFlag("0");
				//rent.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				rent.setItemName(list.get(0).getName());
				//只有护工费的所属科室会有多个，这里不涉及到护工费的结算
				rent.setPkDeptCg(list.get(0).getFkDepts());
				if(rent.getCashPledge()!=null && rent.getCashPledge().compareTo(BigDecimal.ZERO)==1){
					rent.setPledgeState("1");
				}else{
					rent.setPledgeState("0");
				}
				
				DataBaseHelper.insertBean(rent);
			}else{
				//rent.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				DataBaseHelper.updateBeanByPk(rent,false);
			}
			
			if(master.getServUnionpayTrading()!=null){
				//根据患者主键查询就诊次数
				
				
				//保存缴费记录
				TpServPayment sp = new TpServPayment();
				//sp.setFkDept(list.get(0).getFkDepts());
				//sp.setFkPatient(master.getServItemRent().getFkPatient());
				//sp.setFkUnionpayTrading(servUnionpayTrading.getPkUnionpayTrading());
				sp.setAmount(master.getServItemRent().getCashPledge());
				sp.setIsPledge("0");
				sp.setPayMethod(master.getPayMethod());
				//sp.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				//sp.setCreateTime(new Date());
				
				//sp.setPkPvip(rent.getPkPvip());
				//sp.setPkPv(rent.getPkPv());
				//sp.setIpTimes(rent.getIpTimes());
				//sp.setOpTimes(rent.getOpTimes());
				
				DataBaseHelper.insertBean(sp);
				
				//保存中间表
				TpServRentPayment rp = new TpServRentPayment();
				rp.setPkRent(rent.getPkRent());
				rp.setPkPayment(sp.getPkPayment());
				DataBaseHelper.insertBean(rp);
			}
			
			//有使用设备的时候，要修改对应设备的数量
			if(rent!=null&&rent.getFkItemDevice()!=null&&rent.getFkItemDevice().length()>0){
				
				device.setPkItemDevice(rent.getFkItemDevice());
				device.setRentedAlreadyNum(device.getRentedAlreadyNum()+rent.getNum());
				//device.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				device.setTs(new Date());
				DataBaseHelper.updateBeanByPk(device,false);
			}
	} 
	
	
	/**
	 * 第三方服务项目预结算(暂时不用)
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public TpServItemRent preSettlement(String param, IUser user){
  		//原本的单个项目预计算逻辑
		TpServItemRent rent = JsonUtil.readValue(param, TpServItemRent.class);
		TpServItemRent master = new TpServItemRent();
		master.setPkRent(rent.getPkRent());
		List<TpServItemRent> list = tpServItemRentMapper.getServItemRentList(master);
		rent = list.get(0);
		long numDay = getDaySub(DateUtils.dateToStr("yyyy-MM-dd", rent.getCreateTime()),DateUtils.dateToStr("yyyy-MM-dd", new Date()))+1;
		BigDecimal amountTotal = rent.getPrice().multiply(new BigDecimal(numDay)); 
		BigDecimal differAmount = amountTotal.subtract(rent.getCashPledge()); 
	    rent.setAmountTotal(amountTotal);
	    rent.setDifferAmount(differAmount);
		rent.setSjDateNum((int)numDay);
		
		return rent;
	}
	
	/**
	 * 保存结算数据(暂时不用)
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void saveSettlementData(String param, IUser user){
		
		SaveParam master = JsonUtil.readValue(param, SaveParam.class);
		TpServUnionpayTrading  servUnionpayTrading = new TpServUnionpayTrading();
		if(master.getServUnionpayTrading()!=null){
			servUnionpayTrading = master.getServUnionpayTrading();
			//患者主键、操作员工号、操作时间
			//保存银联交易记录
			servUnionpayTrading.setPkPatient(master.getRentSettlement().getPkDeptCg());
			servUnionpayTrading.setWorkid(user.getId());//这里存的是工号，先用id暂替
			servUnionpayTrading.setHtime(new Date());
			DataBaseHelper.insertBean(servUnionpayTrading);
		}
		
		//保存出租信息
		TpServItemRent rent = master.getRentSettlement();
		rent.setRentState("1");
		//rent.setTs(new Date());
		//rent.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
		DataBaseHelper.updateBeanByPk(rent,false);
		
		int r = rent.getDifferAmount().compareTo(BigDecimal.ZERO);
		if(r==1){//收费完结
			//保存缴费记录
			TpServPayment sp = new TpServPayment();
			//sp.setFkDept(rent.getFkDept());
			//sp.setFkPatient(rent.getFkPatient());
			//sp.setFkUnionpayTrading(servUnionpayTrading.getPkUnionpayTrading());
			sp.setAmount(rent.getDifferAmount());
			sp.setIsPledge("1");
			sp.setPayMethod(master.getPayMethod());
			//sp.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
			DataBaseHelper.insertBean(sp);
			
			//保存中间表
			TpServRentPayment rp = new TpServRentPayment();
			rp.setPkRent(rent.getPkRent());
			rp.setPkPayment(sp.getPkPayment());
			DataBaseHelper.insertBean(rp);
		}else if(r==-1){//退费完结
			//保存缴费记录
			TpServPayment sp = new TpServPayment();
			//sp.setFkDept(rent.getFkDept());
			//sp.setFkPatient(rent.getFkPatient());
			//sp.setFkUnionpayTrading(servUnionpayTrading.getPkUnionpayTrading());
			sp.setAmount(rent.getDifferAmount());
			sp.setIsPledge("1");
			sp.setPayMethod(master.getPayMethod());
			//sp.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
			DataBaseHelper.insertBean(sp);
			
			//保存中间表
			TpServRentPayment rp = new TpServRentPayment();
			rp.setPkRent(rent.getPkRent());
			rp.setPkPayment(sp.getPkPayment());
			DataBaseHelper.insertBean(rp);
		}
		
		
		//有使用设备的时候，要把设备的状态改为出租
		if(rent!=null&&rent.getFkItemDevice()!=null&&rent.getFkItemDevice().trim().length()>0){
			TpServItemDevice device = new TpServItemDevice();
			device.setPkItemDevice(rent.getFkItemDevice());
			List<TpServItemDevice> list = tpServItemDeviceMapper.getServItemDeviceList(device);
			device = list.get(0);
			device.setRentedAlreadyNum(device.getRentedAlreadyNum()-rent.getNum());
			DataBaseHelper.updateBeanByPk(device,false);
		}
	}

	/**
	 * 非医疗费用预结算(护工费加出租服务,后期去掉护工费给门诊用，现在没用)
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public PreSettleData preSettle(String param, IUser user){
		//出租项目预结算逻辑
		
/*		 	1.判断是否是中途结算
		 	2.是中途结算计算护工费就行
		 		在his取出护工数量-第三方服务的his护工数量 = 本次需要结的护工数量
		 	(中途结算完结)
		 	3.如果是正式结算
		 		查出所有出租项目，并计算项目总金额和总押金
		 		总金额(项目总金额+护工费）-总押金 = 需缴纳押金
		 	2.查出以前的缴纳的总费用（包括押金）和总结算金额，相减，等于所有项目的总押金*/
		 
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String) paraMap.get("pkPv");
		String euSttype = (String) paraMap.get("euSttype");
		String dateBegin = (String) paraMap.get("dateBegin");
		String dateEnd = (String) paraMap.get("dateEnd");
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		
		PreSettleData preSettleData  = new PreSettleData();
		double totalCost = 0d;//总费用
		double totalCashPledge = 0d; //总押金
		
		//护工费
		Map<String, Object> hgfMap = calculationNursingFee(pkPv,
				dateBegin.subSequence(0, 4)+"-"+dateBegin.subSequence(4, 6)+"-"+dateBegin.substring(6, 8),
				dateEnd.subSequence(0, 4)+"-"+dateEnd.subSequence(4, 6)+"-"+dateEnd.substring(6, 8) );
		preSettleData.setHgdj(Double.parseDouble(hgfMap.get("price").toString()));
		preSettleData.setHzzyts(Integer.parseInt(hgfMap.get("num").toString()));
		preSettleData.setHgf(Double.parseDouble(hgfMap.get("amountTotal").toString()));
		totalCost += preSettleData.getHgf();
		
		if(preSettleData.getHgf()==0){
			preSettleData.setIfPay("0");
		}else{
			preSettleData.setIfPay("1");
		}
		
		if(euSttype.equals("0")){//出院结算
			//第三方服务出租项目
			//根据就诊主键获取所有出租项目
			TpServItemRent master = new TpServItemRent();
			master.setPkPv(pkPv);
			master.setRentState("0");
			List<TpServItemRent> list = tpServItemRentMapper.getServItemRentList(master);
			for(int i=0; i<list.size(); i++){
				long numDay = getDaySub(DateUtils.dateToStr("yyyy-MM-dd", list.get(i).getCreateTime()),DateUtils.dateToStr("yyyy-MM-dd", new Date()))+1;
				BigDecimal amountTotal = list.get(i).getPrice().multiply(new BigDecimal(numDay)) ;   //这个有待区分  日、周、月
				BigDecimal differAmount = amountTotal.subtract(list.get(i).getCashPledge()); //差额
				list.get(i).setAmountTotal(amountTotal);
				list.get(i).setDifferAmount(differAmount);
				list.get(i).setSjDateNum((int)numDay);
				
				totalCost += amountTotal.doubleValue();
				totalCashPledge += list.get(i).getCashPledge().doubleValue();
				
				preSettleData.setRentList(list);
			}
			if(preSettleData.getHgf()==0&&list.size()==0){
				preSettleData.setIfPay("0");
			}else{
				preSettleData.setIfPay("1");
			}
		}
		preSettleData.setTotalCost(totalCost);
		preSettleData.setTotalCashPledge(totalCashPledge);
		preSettleData.setPayAmount(totalCost-totalCashPledge);
		return preSettleData;
	}

	/**
	 * 非医疗费用保存结算数据
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void saveSettleData(String param, IUser user){
		
		
/*		 新的结算数据保存逻辑
		 
		 1、根据pkpv获取患者各种信息
		 2、判断缴费费用是否大于0，大于0保存交易记录，不大于0就是出院结算中的押金抵消了费用获取押金大于费用，需要退费
		 	因为中途结算不进行第三方服务项目的结算，所以小于等于0一定是正式结算；抵消的话没有进行交易，所以不生成交易记录，只生成费用为0的缴费记录
		 	退费目前只用现金（日后可能会加上银联全退的），所以也不生成交易记录，只生成费用为负的缴费记录
		    注：交易记录指的是银联或支付宝等的交易记录         缴费记录指的是这次结算的记录
		  3、生成缴费记录
		  4、保存项目出租信息（护工费||第三方服务出租项目）和出租项目与缴费记录的中间表*/
		 
		
		SettleData master = JsonUtil.readValue(param, SettleData.class);
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", master.getPkPv());
		
		//获取患者各种信息
		Map<String,Object> piMap = tpServItemRentMapper.getPatientInfo(param_h);
		
		TpServPayment sp = new TpServPayment(); //缴费记录
		
		//第一步：生成交易记录
		if(master.getPayAmount().compareTo(BigDecimal.ZERO)!=0){ 
			//生成交易记录
			if(master.getPayType().equals("1")){//银联
				TpServUnionpayTrading ut = master.getUnionpayTrading();
				ut.setPkPv(master.getPkPv()); //就诊主键
				ut.setPkPatient(piMap.get("pk_pi")==null?null:piMap.get("pk_pi").toString()); //患者主键
				ut.setWorkid(user.getId());//这里存的是工号，先用id暂替
				ut.setHtime(new Date());
				DataBaseHelper.insertBean(ut);
				
				//sp.setFkUnionpayTrading(ut.getPkUnionpayTrading());
				sp.setPayMethod("1");
			}else if(master.getPayType().equals("2")){//支付宝
				//保存支付宝交易记录
				//将交易记录的主键set进sp
				sp.setPayMethod("2");
			}else if(master.getPayType().equals("3")){//龙闪付
				//保存龙闪付交易记录
				//将交易记录的主键set进sp
				sp.setPayMethod("3");
			}else if(master.getPayType().equals("0")){//现金
				sp.setPayMethod("0");
			}
			
			//第二步，生成缴费记录
			//sp.setFkDept(piMap.get("pk_dept_admit")==null?null:piMap.get("pk_dept_admit").toString());//所属科室
			//sp.setFkPatient(piMap.get("pk_pi")==null?null:piMap.get("pk_pi").toString()); //缴费患者id
			//sp.setPkPvip(piMap.get("pk_pvip")==null?null:piMap.get("pk_pvip").toString());
			//sp.setPkPv(master.getPkPv());
			//sp.setIpTimes(piMap.get("ip_times")==null?0:Integer.parseInt(piMap.get("ip_times").toString()));
			//sp.setOpTimes(piMap.get("op_times")==null?0:Integer.parseInt(piMap.get("op_times").toString()));
			sp.setAmount(master.getPayAmount());
			sp.setIsPledge("1");
			
			DataBaseHelper.insertBean(sp);
		}


		
		//第三步，保存项目出租信息（护工费||第三方服务出租项目）和出租项目与缴费记录的中间表
		
		if(master.getHzzyts()>0){//有护工费
			TpServItemRent hgfRent = new TpServItemRent();//护工费项目
			Map<String,Object> hgfMap = new HashMap<String, Object>();
			String sql = "select t1.name, t1.fk_depts, t1.price, t1.pk_item  " +
					" from tp_serv_item t1 inner join tp_serv_item_type t2 on  t1.fk_item_type = t2.pk_item_type  " +
					" and t1.del_flag = '0' and t2.del_flag = '0' and t1.use_flag = '0' and t2.use_flag = '0'" +
					" where t2.name = ?";
			List<Map<String,Object>> itemList = DataBaseHelper.queryForList(sql,  "护工费");
			for(int i=0; i<itemList.size(); i++){
				if(itemList.get(i).get("fkDepts").toString().indexOf(piMap.get("pk_dept_ns").toString())!=-1){
					hgfMap = itemList.get(i);
				}
			}
			
			//生成护工费的出租项目
			//hgfRent.setPkPvip(piMap.get("pk_pvip")==null?null:piMap.get("pk_pvip").toString());//住院属性
			hgfRent.setPkPv(master.getPkPv());
			hgfRent.setPkDeptCg(piMap.get("pk_dept_admit")==null?null:piMap.get("pk_dept_admit").toString());//所属科室
			//hgfRent.setIpTimes(piMap.get("ip_times")==null?0:Integer.parseInt(piMap.get("ip_times").toString()));//住院次数
			//hgfRent.setOpTimes(piMap.get("op_times")==null?0:Integer.parseInt(piMap.get("op_times").toString()));//就诊次数
			//hgfRent.setSettleTimes(1);//结算次数
			hgfRent.setFkPatient(piMap.get("pk_pi")==null?null:piMap.get("pk_pi").toString());//患者主键
			hgfRent.setPatientName(piMap.get("name_pi")==null?null:piMap.get("name_pi").toString());//患者名称
			hgfRent.setPhone(piMap.get("mobile")==null?null:piMap.get("mobile").toString());//联系人手机号码
			//hgfRent.setAddress("");//寄送地址
			//hgfRent.setExpressCompany("");//物流公司名称
			//hgfRent.setTrackingNumber("");//快递单号
			hgfRent.setFkItem(hgfMap.get("pkItem").toString());//项目主键  根据名称去项目表查
			hgfRent.setItemName(hgfMap.get("name").toString());//项目名称
			//项目设备主键为null
			hgfRent.setPrice(new BigDecimal(hgfMap.get("price").toString()));//单价  
			hgfRent.setNum(master.getHzzyts());//数量
			hgfRent.setDateNum(master.getHzzyts());//租赁天数(按天/按周/按月) 
			hgfRent.setRentMethod("0");//0：按天 1:按数量
			hgfRent.setPledgeState("0");//0 ：无押金
			hgfRent.setRentState("1");//出租状态  1:完结
			hgfRent.setSjDateNum(master.getHzzyts());//实际天数
			hgfRent.setAmountTotal(master.getHgf());//总费用
			hgfRent.setDifferAmount(master.getHgf());//差额
			if(master.getEuSttype().equals("1")){
				//hgfRent.setSettleTime(new Date());//中途结算时间
			}
			//hgfRent.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
			//sp.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
			DataBaseHelper.insertBean(hgfRent);
			
			//生成护工费项目与缴费记录之间的中间表
			TpServRentPayment rp = new TpServRentPayment();
			rp.setPkPayment(sp.getPkPayment());
			rp.setPkRent(hgfRent.getPkRent());
			DataBaseHelper.insertBean(rp);
/*			if(master.getPayAmount().compareTo(BigDecimal.ZERO)!=0){ 
				if(!sp.getPayMethod().equals("0")){  //测试用的，实际应用中不会有现金结算，所以不用加上这个判断

				}
			}*/
		}
		
		//修改出租项目和生成出租项目与缴费记录之间的中间表
		if(master.getRentList()!=null){
			for(int i=0; i<master.getRentList().size(); i++){
				TpServItemRent rent = master.getRentList().get(i);
				//rent.setSettleTimes(rent.getSettleTimes()+1);
				rent.setRentState("1");
				//rent.setTs(new Date());
				//rent.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				DataBaseHelper.updateBeanByPk(rent,false);
				if(master.getPayAmount().compareTo(BigDecimal.ZERO)!=0){ 
					TpServRentPayment srp = new TpServRentPayment();
					srp.setPkPayment(sp.getPkPayment());
					srp.setPkRent(rent.getPkRent());
					DataBaseHelper.insertBean(srp);
				}

				
				//有使用设备的时候，要把设备的状态改为出租
				if(rent!=null&&rent.getFkItemDevice()!=null&&rent.getFkItemDevice().length()>0){
					TpServItemDevice device = new TpServItemDevice();
					device.setPkItemDevice(rent.getFkItemDevice());
					List<TpServItemDevice> list = tpServItemDeviceMapper.getServItemDeviceList(device);
					device = list.get(0);
					device.setRentedAlreadyNum(device.getRentedAlreadyNum()-rent.getNum());
					DataBaseHelper.updateBeanByPk(device,false);
				}
			}
		}
/*		TpServUnionpayTrading  servUnionpayTrading = new TpServUnionpayTrading();
		if(master.getServUnionpayTrading()!=null){
			servUnionpayTrading = master.getServUnionpayTrading();
			//患者主键、操作员工号、操作时间
			//保存银联交易记录
			servUnionpayTrading.setFkPatient(master.getRentSettlement().getFkDept());
			servUnionpayTrading.setWorkid(user.getId());//这里存的是工号，先用id暂替
			servUnionpayTrading.setHtime(new Date());
			DataBaseHelper.insertBean(servUnionpayTrading);
		}
		
		//保存出租信息
		TpServItemRent rent = master.getRentSettlement();
		rent.setRentState("1");
		rent.setTs(new Date());
		rent.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
		DataBaseHelper.updateBeanByPk(rent,false);
		
		if(master.getServUnionpayTrading()!=null){
			//保存缴费记录
			TpServPayment sp = new TpServPayment();
			sp.setFkDept(rent.getFkDept());
			sp.setFkPatient(rent.getFkPatient());
			sp.setFkRent(rent.getPkRent());
			sp.setFkUnionpayTrading(servUnionpayTrading.getPkUnionpayTrading());
			sp.setAmount(rent.getCashPledge());
			sp.setIsPledge("1");
			sp.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
			DataBaseHelper.insertBean(sp);
		}
		
*/
	}

	/**
	 * 日期相减得到天数，算头不算尾
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 */
	public static long getDaySub(String beginDateStr,String endDateStr)
    {
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate;
        java.util.Date endDate;
        try
        {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);    
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
            //System.out.println("相隔的天数="+day);   
        } catch (Exception e) {
        	e.printStackTrace();
		}   
        return day;
    }

	/**
	 * 日期相减得到天数，算头不算尾,当天入当天出算一天，用于计算总天数
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 */
	public static long getDaySubTwo(String beginDateStr,String endDateStr)
    {
        long day=0;
        if(beginDateStr.equals(endDateStr)){
        	return (long)1;
        }else{
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date beginDate;
            java.util.Date endDate;
            try
            {
                beginDate = format.parse(beginDateStr);
                endDate= format.parse(endDateStr);    
                day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
                //System.out.println("相隔的天数="+day);   
            } catch (Exception e) {
            	e.printStackTrace();
    		}   
            return day;
        }
    }
	
    /**
     * 判断时间是不是今天
     * @param date
     * @return    是返回true，不是返回false
     */
    private static boolean isNow(Date date) {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        //获取今天的日期
        String nowDay = sf.format(now);
         
         
        //对比的时间
        String day = sf.format(date);
         
        return day.equals(nowDay);  
    }
    
    public static void Copy(Object source, Object dest) throws Exception {  
        // 获取属性  
        BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(),Object.class);  
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();  
  
        BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(),Object.class);  
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();  
  
        try {  
            for (int i = 0; i < sourceProperty.length; i++) {  
                  
                for (int j = 0; j < destProperty.length; j++) {  
                      
                    if (sourceProperty[i].getName().equals(destProperty[j].getName())  && sourceProperty[i].getPropertyType() == destProperty[j].getPropertyType()) {  
                        // 调用source的getter方法和dest的setter方法  
                        destProperty[j].getWriteMethod().invoke(dest,sourceProperty[i].getReadMethod().invoke(source));  
                        break;  
                    }  
                }  
            }  
        } catch (Exception e) {  
            throw new Exception("属性复制失败:" + e.getMessage());  
        }  
    } 
    
	public static void setDefaultValue(Object obj, boolean flag) {
		
		User user = UserContext.getUser();
	
		Map<String,Object> default_v = new HashMap<String,Object>();
		if(flag){  // 如果新增
			default_v.put("pkOrg", user.getPkOrg());
			default_v.put("creator", user.getPkEmp());
			default_v.put("createTime",new Date());
			default_v.put("delFlag", "0");
		}
		
		default_v.put("ts", new Date());
		default_v.put("modifier",  user.getPkEmp());
		
		Set<String> keys = default_v.keySet();
		
		for(String key : keys){
			Field field = ReflectHelper.getTargetField(obj.getClass(), key);
			if (field != null) {
				ReflectHelper.setFieldValue(obj, key, default_v.get(key));
			}
		}
	
	}
	
	/**
	 * 判断患者是否有过中途结算，如果有，返回上次的结算日期
	 * @param PkPv
	 * @return
	 */
	private Date checkMidSettle(String PkPv){
		
	Map<String,Object> map = DataBaseHelper.queryForMap("select count(1) amt from bl_settle where dt_sttype = 11 and flag_canc = 0 and pk_pv = ? and pk_settle not in (select pk_settle_canc from bl_settle where pk_pv = ?)", PkPv,PkPv);
	int cnt = 0;
	if(map.get("amt") instanceof BigDecimal){
		BigDecimal amt = amtTrans(map.get("amt"));
		cnt = amt.intValue();
	}else{
		 cnt = map.get("amt")==null?0:(Integer)map.get("amt");
	}
	
		if(cnt> 0){
			Map<String,Object> dateInfo = DataBaseHelper.queryForMap("select date_end dateEnd from bl_settle where  dt_sttype = 11 and flag_canc = 0 and  pk_pv = ? order by  date_end desc", PkPv);
			return (Date)dateInfo.get("dateend");
		}
		return null;
	}
	
	private BigDecimal amtTrans(Object amt) {
		if(amt == null){
			return BigDecimal.ZERO;
		}else{
			return (BigDecimal)amt;
		}
	}
}