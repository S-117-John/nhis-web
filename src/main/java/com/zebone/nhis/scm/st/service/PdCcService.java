package com.zebone.nhis.scm.st.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdCc;
import com.zebone.nhis.common.module.scm.st.PdCcDetail;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.st.dao.PdCcMapper;
import com.zebone.nhis.scm.st.vo.PdBaseParam;
import com.zebone.nhis.scm.st.vo.PdCcVo;
import com.zebone.nhis.scm.st.vo.PdLedgerVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 库存结账
 * 
 * @author wangpeng
 * @date 2016年12月5日
 *
 */
@Service
public class PdCcService {
	
	/***
	 * 库存结账mapper
	 */
	@Resource
	private PdCcMapper pdCcMapper;
	
	/***
	 * 交易号：008006010002<br>
	 * 查询库存结账明细
	 *  
	 * @param  param
	 * @param  user
	 * @return PdCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年12月5日
	 */
	public PdCcVo getPdCcVo(String param, IUser user) {
		PdCc cc = JsonUtil.readValue(param, PdCc.class);		
		PdCcVo vo = new PdCcVo();
		PdCc pdCc =pdCcMapper.getPdCcById(cc.getPkPdcc());
		List<PdCcDetail> dList = pdCcMapper.getPdCcDetailListById(cc.getPkPdcc());
		
		vo.setPdCc(pdCc);
		vo.setPdCcDetailList(dList);
		return vo;
	}
	
	/***
	 * 交易号：008006010003<br>
	 * 保存库存结账
	 *  
	 * @param  param
	 * @param  user
	 * @return PdCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年12月5日
	 */
	public PdCcVo savePdCcAndDetail(String param, IUser user) {
		PdCc cc = JsonUtil.readValue(param, PdCc.class);
		User u = UserContext.getUser();
		PdCcVo vo = new PdCcVo();
		List<PdCcDetail> dList = new ArrayList<PdCcDetail>();
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkStore", u.getPkStore());
		mapParam.put("monthFin", cc.getMonthFin() + "");
		mapParam.put("dateBeginStr", DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateBegin()));
		mapParam.put("dateEndStr", DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateEnd()));
		
		//需要查询财务月份的上个月
		Integer monthFin = cc.getMonthFin();
		if(monthFin == null){
			throw new BusException("财务月份不能为空！");
		}
		String mFin = cc.getMonthFin() + "01";
		Date mFinD = DateUtils.strToDate(mFin, "yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(mFinD);
		cal.add(Calendar.MONTH, -1);
		String monthFinLast = cal.get(Calendar.YEAR) + "" + cal.get(Calendar.MONTH);
		mapParam.put("monthFinLast", monthFinLast);
		
		//首先获取物品基本信息（pk_pd、pk_unit_pack、pack_size确定一个物品）
		List<PdBaseParam> pdBaseList = pdCcMapper.getPdBaseParamList(mapParam);
		
		//查询上月结存数
		Map<String, Map<String, Object>> lastCcMaps = DataBaseHelper.queryListToMap(
				"select dt.pk_pd || '~' || dt.pk_unit as key_,dt.quan_min,dt.amount_cost,dt.amount "
				+ "from pd_cc_detail dt "
				+ "where dt.del_flag = '0' "
				+ " and dt.pk_pdcc=(select cc.pk_pdcc from pd_cc cc where cc.del_flag = '0' and cc.pk_store=? and cc.month_fin= ?)",new Object[]{u.getPkStore(), monthFinLast + ""});
		
		//查询本月入库数
		Map<String, Map<String, Object>> inStMaps = DataBaseHelper.queryListToMap(
				"select dt.pk_pd || '~' || dt.pk_unit_pack as key_,nvl(sum(dt.quan_min),0) quan_min,nvl(sum(dt.amount_cost),0) amt_cost,nvl(sum(dt.amount),0) amt "
				+ "from pd_st_detail dt "
				+ "inner join pd_st st on dt.pk_pdst=st.pk_pdst and st.del_flag = '0' "
				+ "where dt.del_flag = '0' and st.eu_direct = '1' "
				+ "	and st.pk_store_st = ? "
				+ "	and st.date_chk >= to_date(?, 'yyyymmddhh24miss') "
				+ "	and st.date_chk < to_date(?, 'yyyymmddhh24miss') "
				+ "group by dt.pk_pd,dt.pk_unit_pack",
				new Object[]{u.getPkStore(), DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateBegin()), DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateEnd())});
		
		//询本月出库数
		Map<String, Map<String, Object>> outStMaps = DataBaseHelper.queryListToMap(
				"select dt.pk_pd || '~' || dt.pk_unit_pack as key_,nvl(sum(dt.quan_min),0) quan_min,nvl(sum(dt.amount_cost),0) amt_cost,nvl(sum(dt.amount),0) amt "
				+ "from pd_st_detail dt "
				+ "inner join pd_st st on dt.pk_pdst=st.pk_pdst and st.del_flag = '0' "
				+ "where dt.del_flag = '0' and st.eu_direct = '-1' "
				+ "	and st.pk_store_st = ? "
				+ "	and st.date_chk >= to_date(?, 'yyyymmddhh24miss') "
				+ "	and st.date_chk < to_date(?, 'yyyymmddhh24miss') "
				+ "group by dt.pk_pd,dt.pk_unit_pack",
				new Object[]{u.getPkStore(), DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateBegin()), DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateEnd())});
		
		//查询本月调价数据
		Map<String, Map<String, Object>> rePriceMaps = DataBaseHelper.queryListToMap(
				"select dt.pk_pd || '~' || dt.pk_unit_pack as key_,nvl(sum(dt.amount_rep),0) amt "
				+ "from pd_reprice_hist dt "
				+ "where dt.del_flag = '0' "
				+ " and dt.pk_store = ? "
				+ " and dt.date_reprice >= to_date(?, 'yyyymmddhh24miss') "
				+ " and dt.date_reprice < to_date(?, 'yyyymmddhh24miss') "
				+ "group by dt.pk_pd, dt.pk_unit_pack",
				new Object[]{u.getPkStore(), DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateBegin()), DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateEnd())});
		
		//保存
		String pkPdcc = NHISUUID.getKeyId();
		
		Date ts = new Date();
		if(CollectionUtils.isNotEmpty(pdBaseList)){
			for(int i=0; i<pdBaseList.size(); i++){
				Double amountCost = new Double(0); //成本金额
				Double amount = new Double(0); //零售金额
				Double quanMin = new Double(0); //数量
				PdBaseParam p = pdBaseList.get(i);
				String key = p.getPkPd()+"~"+p.getPkUnitPack();
				Map<String, Object> lastCcMap = lastCcMaps.get(key);
				Map<String, Object> inStMap = inStMaps.get(key);
				Map<String, Object> outStMap = outStMaps.get(key);
				Map<String, Object> rePriceMap = rePriceMaps.get(key);
				//数量=期初+入库-出库
				//成本金额=期初+入库-出库
				//零售金额=期初+入库-出库+调价
				if(lastCcMap != null){
					quanMin = quanMin + Double.valueOf(lastCcMap.get("quanMin").toString());
					amountCost = amountCost + Double.valueOf(lastCcMap.get("amountCost").toString());
					amount = amount + Double.valueOf(lastCcMap.get("amount").toString());
				}
				if(inStMap != null){
					quanMin = quanMin + Double.valueOf(inStMap.get("quanMin").toString());
					amountCost = amountCost + Double.valueOf(inStMap.get("amtCost").toString());
					amount = amount + Double.valueOf(inStMap.get("amt").toString());
				}
				if(outStMap != null){
					quanMin = quanMin - Double.valueOf(outStMap.get("quanMin").toString());
					amountCost = amountCost - Double.valueOf(outStMap.get("amtCost").toString());
					amount = amount - Double.valueOf(outStMap.get("amt").toString());
				}
				if(rePriceMap != null){
					amount = amount + Double.valueOf(rePriceMap.get("amt").toString());
				}
				
				PdCcDetail detail = new PdCcDetail();
				detail.setPkPdccdt(NHISUUID.getKeyId());
				detail.setPkOrg(u.getPkOrg());
				detail.setPkPdcc(pkPdcc);
				detail.setPkPd(p.getPkPd());
				detail.setPkUnit(p.getPkUnitPack());
				detail.setPackSize(p.getPackSize());
				detail.setQuanMin(quanMin);
				//detail.setPriceCost(priceCost);
				//detail.setPrice(price);
				detail.setAmountCost(amountCost);
				detail.setAmount(amount);
				detail.setCreator(u.getPkEmp());
				detail.setCreateTime(ts);
				detail.setModifier(u.getPkEmp());
				detail.setDelFlag("0");
				detail.setTs(ts);
				dList.add(detail);
			}
			
			//保存pd_cc
			cc.setPkPdcc(pkPdcc);
			cc.setPkDept(u.getPkDept());
			cc.setPkStore(u.getPkStore());
			cc.setPkEmpCc(u.getPkEmp());
			cc.setNameEmpCc(u.getNameEmp());
			cc.setDateCc(ts);
			DataBaseHelper.insertBean(cc);
			
			//保存pd_cc_detail
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdCcDetail.class), dList);			
		}else{
			throw new BusException("该时间段内没有未结账物品！");
		}
		return vo;
	}
	
	/***
	 * 交易号：008006010004<br>
	 * 取消库存结账
	 *  
	 * @param  param
	 * @param  user
	 * @return PdCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年12月5日
	 */
	public void deletePdCc(String param, IUser user) {
		PdCc cc = JsonUtil.readValue(param, PdCc.class);
		String pkPdcc = cc.getPkPdcc();
		//先删除明细
		DataBaseHelper.execute("delete from pd_cc_detail  where pk_pdcc = ?", new Object[]{pkPdcc});
		DataBaseHelper.execute("delete from pd_cc where pk_pdcc = ?", new Object[]{pkPdcc});
	}
	
	/***
	 * 交易号：008006010005<br>
	 * 获取财务月份
	 *  
	 * @param  param
	 * @param  user
	 * @return PdCcVo
	 * @throws 
	 *
	 * @date 2017年8月14日
	 */
	public Map<String,Object> getPdCcMouth(String param, IUser user){
		 Map<String,Object> map = new HashMap<String, Object>();
		    User u = UserContext.getUser();
			PdCc pdCc = new PdCc();
			if(u.getPkDept().equals(null)){
				throw new BusException("请维护部门！");
			}
           if(u.getPkOrg().equals(null)){
           	throw new BusException("请维护机构！");
			}
           if(u.getPkStore().equals(null)){
           	throw new BusException("请维护仓库！");
			}
			if(Application.isSqlServer()){
				pdCc = DataBaseHelper.queryForBean("select top 1 * from PD_CC where pk_org = ? and pk_store = ? and pk_dept= ? and del_flag = '0' order by month_fin desc",PdCc.class, new Object[]{u.getPkOrg(), u.getPkStore(),u.getPkDept()});
			}else{
				List<PdCc> cclist = DataBaseHelper.queryForList("select * from PD_CC where pk_org = ? and pk_store = ? and pk_dept= ? and del_flag = '0' order by month_fin desc",PdCc.class, new Object[]{u.getPkOrg(), u.getPkStore(),u.getPkDept()});
				if(cclist!=null&&cclist.size()>0)
					pdCc = cclist.get(0);
				else 
				    pdCc = null;
			}
			if(pdCc == null){
				 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 String time = format.format(Calendar.getInstance().getTime());
			map.put("monthFin", time);
			}else{
				String obligate;
				Integer Monthtime=pdCc.getMonthFin();
				String monthtime=Monthtime.toString();
				obligate = monthtime.substring(4, 6);
				if(obligate.equals("12")){
					try {
						Integer a = Integer.parseInt(monthtime);
					    a+=89;
					    String sa=a.toString();
					    sa += "25000000";
						String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
						sa = sa.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
						map.put("monthFin", sa);
					} catch (NumberFormatException e) {
					    e.printStackTrace();
					}
				}else{
				Monthtime = Monthtime + 1;
				monthtime = Monthtime.toString();
				monthtime += "25000000";
				String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
				monthtime = monthtime.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
				map.put("monthFin", monthtime);
				}
		    }
			return map;
	 }
	
	
	
	/***
	 * 交易号：008006010006<br>
	 * 获取开始时间<br>
	 * 如果第一次月结时，取当前仓库业务类型为“0110（初始化入库）”的最小审核日期，否则为当前仓库上次月结终止日期+1秒
	 * @param  param
	 * @param  user
	 * @return PdCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年12月5日
	 */
	public Map<String,Object> getPdCcDateBegin(String param, IUser user) {
		User u = UserContext.getUser();
		Map<String,Object> map = new HashMap<String, Object>();
		Date dateBegin = null;
		//查询上次月结
		PdCc pdCc = new PdCc();
		if(Application.isSqlServer()){
			pdCc = DataBaseHelper.queryForBean("select top 1 * from pd_cc where del_flag = '0' and pk_org = ? and pk_store = ? and pk_dept = ? order by month_fin desc", PdCc.class, new Object[]{u.getPkOrg(), u.getPkStore(),u.getPkDept()});
		}else{
			pdCc = DataBaseHelper.queryForBean("select t.* from "
					+ "(select * from pd_cc where del_flag = '0' and pk_org = ? and pk_store = ? and pk_dept = ? order by month_fin desc) t "
					+ "where rownum = 1", PdCc.class, new Object[]{u.getPkOrg(), u.getPkStore(),u.getPkDept()});
		}
		if(pdCc != null){
			//为当前仓库上次月结终止日期+1秒
			Date begin = pdCc.getDateEnd();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(begin);
			calendar.add(Calendar.SECOND, 1);
			dateBegin = calendar.getTime();
		}else{
			Map<String,Object> dateMap=new HashMap<String,Object>();
			//没有月结时取当前仓库业务类型为“0110（初始化入库）”的最小审核日期
			dateMap = DataBaseHelper.queryForMap("select min(date_chk) date_chk from pd_st "
					+ "where dt_sttype = '0110' and del_flag = '0' and pk_org = ? and pk_store_st = ?", new Object[]{u.getPkOrg(), u.getPkStore()});
			if(dateMap!=null && dateMap.get("dateChk")!=null ){
				dateBegin=DateUtils.strToDate((dateMap.get("dateChk").toString()),"yyyy-MM-dd HH:mm:ss");
			}
		}
		map.put("dateBegin", dateBegin);
		return map;
	}
	

	/**
	 * 根据财务月份获取对应的开始时间和结束时间
	 * @param monBegin
	 * @param monEnd
	 * @return
	 */
	private Map<String,String> getDateByMonthFin(String pkStore,String monBegin,String monEnd){
		//存放查询到的开始时间和结束时间
		Map<String,String> dateMap = new HashMap<String, String>();
		
		//格式化查询到的时间
		SimpleDateFormat sbf = new SimpleDateFormat("yyyyMMddHHmmss");		
		
		/**查询开始时间*/
		//截取开始财务月份(例：201805):取当前月上一个月的结账结束日期+1秒；如果没有则提示用户
		Long mBegin = Long.valueOf(monBegin.substring(0, 6))-1;
		List<Map<String,Object>> qryDateBegin = DataBaseHelper.queryForList("select date_end from pd_cc where pk_store=? and month_fin=? order by date_end desc",pkStore, mBegin);
		
		if(qryDateBegin!=null && qryDateBegin.size()>0){
			String dateStr=sbf.format(qryDateBegin.get(0).get("dateEnd"));
			Date date=DateUtils.strToDate(dateStr, "yyyyMMddHHmmss");//2018-10-19 09:33:08.0
			dateStr=DateUtils.addDate(date, 1, 6, "yyyyMMddHHmmss");
			dateMap.put("dateBegin", dateStr);//20181030230909
		}
		else{
			throw new BusException("您录入的开始财务月份上月无结账记录！");
		}
		/**查询结束时间*/
		//截取结束财务月份(例：201807)
		String mEnd = monEnd.substring(0, 6);
		List<Map<String,Object>> qryDateEnd = DataBaseHelper
				.queryForList("select date_end from pd_cc where  pk_store=? and  month_fin=?", pkStore,Long.valueOf(mEnd));
		
		if(qryDateEnd!=null && qryDateEnd.size()>0){
			dateMap.put("dateEnd", sbf.format(qryDateEnd.get(0).get("dateEnd")));
		}
		else{
			Date date=DateUtils.strToDate(monEnd, "yyyyMMddHHmmss");
			String dateEnd="";
			dateEnd=DateUtils.addDate(date, 1, 2, "yyyyMMddHHmmss");
			date=DateUtils.strToDate(dateEnd,"yyyyMMddHHmmss");
			dateEnd=DateUtils.addDate(date, -1, 3, "yyyyMMddHHmmss").substring(0, 8)+"235959";
			
			dateMap.put("dataEnd", dateEnd);
		}
		return dateMap;
	}
	
	/**
	 * 库存台账查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PdLedgerVo> getPdLedger(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//获取当前仓库信息并添加到paramMap
		String pkStroe=UserContext.getUser().getPkStore();
		paramMap.put("pkStore", pkStroe);
		String monthBegin = paramMap.get("monthBegin").toString();
		String monthEnd = paramMap.get("monthEnd").toString();
		if(!CommonUtils.isEmptyString(monthBegin) && 
				!CommonUtils.isEmptyString(monthEnd)){
			 //获取起止财务月份的开始日期和结束日期
			 Map<String,String> dateMap = getDateByMonthFin(pkStroe,monthBegin,monthEnd);
			 //给声明的dateBegin、dateBegin字段赋值
			 if(dateMap!=null && dateMap.size()>0){
				/**把开始时间和结束时间添加到查询参数*/
				paramMap.put("dateEnd", dateMap.get("dateEnd"));
				paramMap.put("dateBegin", dateMap.get("dateBegin"));
			 }
		}
		
		if(!CommonUtils.isEmptyString(paramMap.get("calMonBegin").toString())){
			paramMap.put("calMonBegin", paramMap.get("calMonBegin").toString().substring(0, 6));
		}
		
		
		List<PdLedgerVo> pdLedgerList=new ArrayList<PdLedgerVo> ();
		Map<String,PdLedgerVo> map = new TreeMap<String, PdLedgerVo>();
		
		List<PdLedgerVo> ccList=pdCcMapper.getPdLedgerCCList(paramMap);
		List<PdLedgerVo> stInList=pdCcMapper.getPdLedgerSTInList(paramMap);
		List<PdLedgerVo> stOutList=pdCcMapper.getPdLedgerSTOutList(paramMap);
		List<PdLedgerVo> histList=pdCcMapper.getPdLedgerHistList(paramMap);
		String mapKey="";
		Long quan=new Long(0);
		Double amt = new Double(0);
		
		for(PdLedgerVo vo :ccList){
			mapKey = vo.getMapKey();
			if(map.containsKey(mapKey)){
				PdLedgerVo ledger=map.get(mapKey);
				
				if(ledger.getQuanCC() !=null) {
					quan=ledger.getQuanCC();
				}
				else {
					quan=new Long(0);
				}
				ledger.setQuanCC(quan+vo.getQuanCC());
				
				if(ledger.getAmtCC() !=null) {
					amt=ledger.getAmtCC();
				}
				else{
					amt=new Double(0);
				}
				ledger.setAmtCC(amt+vo.getAmtCC());
				
				if(ledger.getAmtCostCC() !=null){ 
					amt=ledger.getAmtCostCC();
				}
				else{
					amt=new Double(0);
				}
				ledger.setAmtCostCC(amt + vo.getAmtCostCC() );
				
				map.put(mapKey, ledger);
			}else{
				map.put(mapKey, vo);
			}
		}
		for(PdLedgerVo vo :stInList){
			mapKey = vo.getMapKey();
			if(map.containsKey(mapKey)){
				PdLedgerVo ledger=map.get(mapKey);
				
				if(ledger.getQuanIncom() !=null){ 
					quan=ledger.getQuanIncom();
				}
				else{
					quan=new Long(0);
				}
				ledger.setQuanIncom(quan+vo.getQuanIncom());
				
				if(ledger.getAmtInCom() !=null){ 
					amt=ledger.getAmtInCom();
				}
				else {
					amt=new Double(0);
				}
				ledger.setAmtInCom(amt+vo.getAmtInCom());
				
				if(ledger.getAmtCostInCom() !=null) {
					amt=ledger.getAmtCostInCom();
				}
				else {
					amt=new Double(0);
				}
				ledger.setAmtCostInCom(amt+vo.getAmtCostInCom());
				
				map.put(mapKey, ledger);
			}else{
				map.put(mapKey, vo);
			}
		}
		
		for(PdLedgerVo vo :stOutList){
			mapKey = vo.getMapKey();
			if(map.containsKey(mapKey)){
				PdLedgerVo ledger=map.get(mapKey);
				
				if(ledger.getQuanOutcom() !=null){ 
					quan=ledger.getQuanOutcom();
				}
				else {
					quan=new Long(0);
				}
				ledger.setQuanOutcom(quan+vo.getQuanOutcom());
				
				if(ledger.getAmtOutCom() !=null){ 
					amt=ledger.getAmtOutCom();
				}
				else{
					amt=new Double(0);
				}
				ledger.setAmtOutCom(amt+vo.getAmtOutCom());
				
				if(ledger.getAmtCostOutCom() !=null){ 
					amt=ledger.getAmtCostOutCom();
				}
				else{
					amt=new Double(0);
				}
				ledger.setAmtCostOutCom(amt+vo.getAmtCostOutCom());
				map.put(mapKey, ledger);
			}else{
				map.put(mapKey, vo);
			}
		}
		
		for(PdLedgerVo vo :histList){
			mapKey = vo.getMapKey();
			if(map.containsKey(mapKey)){
				PdLedgerVo ledger=map.get(mapKey);
				
				if(ledger.getAmtInCom() !=null){ 
					amt=ledger.getAmtInCom();
				}
				else {
					amt=new Double(0);
				}
				ledger.setAmtInCom(amt+vo.getAmtInCom());//调价收入合并到st收入 
				
				if(ledger.getAmtOutCom() !=null) {
					amt=ledger.getAmtOutCom();
				}
				else{
					amt=new Double(0);
				}
				ledger.setAmtOutCom(amt+vo.getAmtOutCom());//调价支出合并到st支出
				
				map.put(mapKey, ledger);
			}else{
				map.put(mapKey, vo);
			}
		}
		
		
		for(Map.Entry<String, PdLedgerVo> entry : map.entrySet()){
			pdLedgerList.add(entry.getValue());
		}
				
		return pdLedgerList;
		
	}
	
	/**
	 * 库存台账明细查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PdLedgerVo> getPdLedgerDt(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//获取当前仓库信息并添加到paramMap
		paramMap.put("pkStore", UserContext.getUser().getPkStore());
		List<PdLedgerVo> pdLedgerDtList = pdCcMapper.getPdLedgerDt(paramMap);
		Long quanTemp =new Long(0) ;
		Double amtTemp= new Double(0);
		Double amtCostTemp= new Double(0);

		for(PdLedgerVo vo :pdLedgerDtList){
			vo.setQuanBalance(quanTemp+ (vo.getQuanIncom()== null ? 0 : vo.getQuanIncom() )- (vo.getQuanOutcom() == null? 0 : vo.getQuanOutcom()));//结存数量= 期初(上条数据结存)+收入-支出
			vo.setAmtBalance(amtTemp + (vo.getAmtInCom()==null? 0: vo.getAmtInCom()) - (vo.getAmtOutCom()==null ? 0: vo.getAmtOutCom())); //结存零售金额 = 期初(上条数据结存)+收入-支出
			vo.setAmtCostBalance(amtCostTemp + (vo.getAmtCostInCom() ==null ? 0: vo.getAmtCostInCom() )- (vo.getAmtCostOutCom()==null? 0 :vo.getAmtCostOutCom())); //结存成本金额 = 期初(上条数据结存)+收入-支出
			quanTemp = (vo.getQuanBalance()==null ? new Long(0): vo.getQuanBalance());
			amtTemp = (vo.getAmtBalance()==null ? new Double(0) :vo.getAmtBalance());
			amtCostTemp =( vo.getAmtCostBalance()==null? new Double(0) :  vo.getAmtCostBalance());
			if("期初结存".equals(vo.getRemark())){//将期初结存字段复位（sql写在收入字段中）
				vo.setQuanIncom(new Long(0));
				vo.setAmtInCom(new Double(0));
				vo.setAmtCostInCom(new Double(0));
			}
		}
		
		return pdLedgerDtList;
	}
    
	
}
