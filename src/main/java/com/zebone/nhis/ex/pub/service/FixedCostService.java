package com.zebone.nhis.ex.pub.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zebone.nhis.ex.pub.support.ExTaskLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.dao.FixedCostPubMapper;
import com.zebone.nhis.ex.pub.vo.DeptCgItemVo;
import com.zebone.nhis.ex.pub.vo.PvBedVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 固定计费服务
 * @author yangxue
 *
 */
@Service
public class FixedCostService {
	@Resource
	private IpCgPubService  ipCgPubService;
	@Resource
	private FixedCostPubMapper fixedCostPubMapper;

	private Logger logger = LoggerFactory.getLogger("nhis.quartz");

	private ThreadLocal<CostDataArg> localArg = new ThreadLocal<>();

	private void initData(Date cg_end, Date cg_begin, User us,String pkDept,String pkOrg)
			throws BusException {
		CostDataArg costDataArg = new CostDataArg();
		costDataArg.setPkDept(pkDept);
		costDataArg.setPkOrg(pkOrg);
		costDataArg.setDateNow(new Date());
		if (us != null) {
			costDataArg.setUser(us);
		} else {
			costDataArg.setUser(UserContext.getUser());
		}
		if (cg_end != null) {
			costDataArg.setCgEnd(cg_end);
		} else {
			try {
				costDataArg.setCgEnd(DateUtils.getDefaultDateFormat().parse(DateUtils
						.getSpecifiedDateStr(costDataArg.getDateNow(), -1) + "235959"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (cg_begin != null) {
			costDataArg.setCgBegin(cg_begin);
		}
		localArg.set(costDataArg);
	}

	/**
	 * 固定计费服务
	 * @param pk_dept
	 * @param count
	 * @param list
	 * @param isAllowQF
	 * @return
	 */
	public String saveFixedCost(String pk_dept,Integer count,List<DeptCgItemVo> list,boolean isAllowQF,boolean isFixedCg,Date dateEnd){
		List<BlPubParamVo> chList = transfer(list,isFixedCg);
		if(chList == null){
			return null;
		}
		ipCgPubService.chargeIpBatch(chList,isAllowQF);//调用计费接口

		//更新固定记费
		this.updateDayCg(list,dateEnd);
		logger.info("================固定记费保存服务被调用================");
		return null;
	}
	//转换参数
	private List<BlPubParamVo> transfer(List<DeptCgItemVo> list,boolean isFixedCg){
		//非空校验
		if(null == list || list.size() < 1)
		return null;
		//配置记费服务的入口参数
		List<BlPubParamVo> cgList = new ArrayList<BlPubParamVo>();
		for(DeptCgItemVo vo : list){
			BlPubParamVo cgvo = new BlPubParamVo();
			cgvo.setEuPvType(vo.getEuPvtype());
			cgvo.setPkOrg(vo.getPkOrgSys());
			cgvo.setPkPres(null);
			cgvo.setPkPv(vo.getPkPv());
			cgvo.setPkCnord(null);
			cgvo.setFlagPd("0");
			cgvo.setFlagPv("0");
			cgvo.setEuBltype("99");
			cgvo.setIsFixedCg(isFixedCg);
			cgvo.setNameEmpApp(vo.getNameEmpSys());
			cgvo.setPkEmpApp(vo.getPkEmpSys());
			cgvo.setDateHap(vo.getDateItem());
			cgvo.setDateCg(DateUtils.strToDate(DateUtils.getDateStr(vo.getDateItem())+"235959"));//设置为床位日期的235959
			if(vo.getDateOut()!=null){
				cgvo.setDateCg(vo.getDateOut());
			}
			cgvo.setPkDeptApp(vo.getPkDeptPv());//开立科室
			cgvo.setPkOrgApp(vo.getPkOrgApp());//开立机构
			cgvo.setPkDeptNsApp(vo.getPkDeptNsApp());//开立病区
			//cgvo.setpkd
			cgvo.setPkDeptEx(vo.getPkDeptExec());
			cgvo.setPkItem(vo.getPkItem());
			//cgvo.setPkUnitCg(vo.getPkUnit());
			cgvo.setQuanCg(Double.valueOf(vo.getCount()));
			cgvo.setPkOrd(null);
			cgvo.setPkOrdexdt(null);
			cgvo.setPkOrgEx(vo.getPkOrgExec());
			cgvo.setPkPi(vo.getPkPi());
			cgvo.setPkEmpCg(vo.getPkEmpSys());
			cgvo.setNameEmpCg(vo.getNameEmpSys());
			cgvo.setPkDeptCg(vo.getPkDeptSys());
			cgvo.setInfantNo("0");
			cgvo.setSpec(vo.getSpec());
			cgList.add(cgvo);
		}
		return cgList;
	}
	/**
	 * 更新固定费用记录
	 * @param list
	 */
	private void updateDayCg(List<DeptCgItemVo> list,Date dateEnd){
		//当前时间
		//当天出院的写出院时间 (点击出院调用固定记费，传出院日期，其它地方传null)
		String date = "";
		if(dateEnd != null){
			date = DateUtils.getSpecifiedDateStr(dateEnd,0)+"235959";
		}else{
			
			date = DateUtils.getSpecifiedDateStr(new Date(),-1)+"235959";
		}
		
		//空值校验
		if(null == list || list.size() == 0){
			return;
		}
		//组合PK_PV
		Set<String> pvs = new HashSet<String>();
		//去重
		for(DeptCgItemVo vo : list){
			pvs.add(vo.getPkPv());
		}
		List<String> sqls = new ArrayList<String>();
		for(String pkPv:pvs){
			sqls.add("update pv_daycg set date_daycg = to_date('"+date+"','YYYYMMDDHH24MISS') , date_bed = to_date('"+date+"','YYYYMMDDHH24MISS') where pk_pv='"+pkPv+"'");
		}
		DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
	}
	/***
	 * 固定费用查询
	 * @param map
	 * @param iscgBed
	 * @param u
	 * @param cgEnd 记费截止时间，默认昨天的235959
	 * @return
	 * @throws BusException
	 */
	public List<DeptCgItemVo> execFixedCharge(Map<String, Object> map,
			boolean iscgBed, User u,Date cgEnd) throws BusException {
		initData(cgEnd, null, u,CommonUtils.getString(map.get("pkDeptNs")),CommonUtils.getString(map.get("pkOrg")));
		if (map.get("pkPvs") == null)
			throw new BusException("执行固定费用查询时未获取到患者就诊主键！");
		// 查询患者固定费用
		// daycg.date_daycg,
        //daycg.date_bed,
		List<Map<String, Object>> daycgList = fixedCostPubMapper.queryFixedCharge(map);
		//床位费费用分类编码
        String cateBed = ApplicationUtils.getSysparam("BD0001", true);
		// 分组,去掉相同pv的记录
		List<DeptCgItemVo> pv = new ArrayList<DeptCgItemVo>();
		List<Map<String, Object>> pvList = new ArrayList<Map<String, Object>>();
		Set<String> set = new HashSet<String>();
		Set<String> containsBedItemPv  = new HashSet<String>();//固定费用中含床位费的患者 
		boolean flagAdd = "1".equals(ApplicationUtils.getSysparam("BL0054", false))?true:false;//包床是否使用附加收费项目
		for (Map<String, Object> cgMap : daycgList) {
			DeptCgItemVo vo = transferToVO(null, cgMap,flagAdd);
			if(cgMap.get("beginMonth")!=null&&cgMap.get("endMonth")!=null){
				Map<String, Date> dateMap = getSrvDateInterval(cgMap);
				vo.setEuCgmode(cgMap.get("euCgmode").toString());// 1按床，0按人
				vo.setCgBegin(dateMap.get("begin"));
				vo.setCgEnd(dateMap.get("end"));
			}
			pv.add(vo);
			if(cateBed!=null&&cateBed.equals(vo.getCatecode())){
				containsBedItemPv.add(cgMap.get("pkPv").toString());
			}
			if (set.contains(cgMap.get("pkPv").toString())) {
				continue;
			} else {
				set.add(cgMap.get("pkPv").toString());
				pvList.add(cgMap);
			}
			
		}
		ExTaskLog.log("得到pvList：",pvList);
		// 加工计算数量，计算床位费，合并list
		List<DeptCgItemVo> resultList = new ArrayList<DeptCgItemVo>();
		//if(pvList == null || pvList.size()<=0){//无固定费用，只记床位费
			
		//}else{//有固定费用
			for (Map<String, Object> tmap : pvList) {//无重复的pv记录
				List<DeptCgItemVo> itemList = updateItemVO(tmap, pv, iscgBed,containsBedItemPv,flagAdd);
				if (null != itemList && itemList.size() > 0)
					resultList.addAll(itemList);
			}
		//}
		

		List<DeptCgItemVo> result = new ArrayList<DeptCgItemVo>();
		for (DeptCgItemVo vo : resultList) {
			Double quan = vo.getDosage();
			if (null != quan && !quan.equals(new Double(0))) {
				result.add(vo);
			}
		}
		localArg.remove();
		logger.info("================固定记费查询服务被调用================");
		// 返回结果list
		return result;
	}
	
	/**
	 * 查询数据转换成VO
	 * 
	 * @param map
	 * @param cgMap
	 * @return
	 */
	private DeptCgItemVo transferToVO(Map<String, Object> map,
			Map<String, Object> cgMap,boolean flagAdd) {
		DeptCgItemVo vo = new DeptCgItemVo();
		CostDataArg costDataArg = localArg.get();
		User user = costDataArg.getUser();
		if (null != map) {
			vo.setPkPi(CommonUtils.getString(map.get("pkPi")));
			vo.setBedNo(CommonUtils.getString(map.get("bedNo")));
			vo.setEuPvtype(CommonUtils.getString(map.get("euPvtype")));
			vo.setNamePi(CommonUtils.getString(map.get("namePi")));
			vo.setPkDeptOrd(CommonUtils.getString(map.get("pkDeptNs")));
			vo.setPkDeptPv(CommonUtils.getString(map.get("pkDept")));
			vo.setPkPv(CommonUtils.getString(map.get("pkPv")));
			vo.setDateBill((Date)map.get("dateBed"));
		} else {
			vo.setPkPi(CommonUtils.getString(cgMap.get("pkPi")));
			vo.setBedNo(CommonUtils.getString(cgMap.get("bedNo")));
			vo.setEuPvtype(CommonUtils.getString(cgMap.get("euPvtype")));
			vo.setNamePi(CommonUtils.getString(cgMap.get("namePi")));
			vo.setPkDeptOrd(CommonUtils.getString(cgMap.get("pkDeptNs")));
			vo.setPkPv(CommonUtils.getString(cgMap.get("pkPv")));
			vo.setPkDeptPv(CommonUtils.getString(cgMap.get("pkDept")));
			vo.setDateBill((Date)cgMap.get("dateDaycg"));
			vo.setCatecode(CommonUtils.getString(cgMap.get("catecode")));
		}
		vo.setPrice(CommonUtils.getDouble(cgMap.get("price")));
		vo.setItemname(CommonUtils.getString(cgMap.get("itemname")));
		vo.setCount(0.00);
		vo.setFlagPd(CommonUtils.getString(cgMap.get("flagPd")));
		vo.setPkItem(CommonUtils.getString(cgMap.get("pkItem")));
		vo.setItemcode(CommonUtils.getString(cgMap.get("itemcode")));
		vo.setPkUnit(CommonUtils.getString(cgMap.get("pkUnit")));
		if("1".equals(CommonUtils.getString(cgMap.get("euHold")))){
			vo.setSpec("包床");
			if(flagAdd){//如果是包床使用附加收费项目的情况，重新设置收费项目信息
				vo.setPrice(CommonUtils.getDouble(cgMap.get("priceAdd")));
				vo.setItemname(CommonUtils.getString(cgMap.get("itemnameAdd")));
				vo.setCount(0.00);
				vo.setFlagPd(CommonUtils.getString(cgMap.get("flagPdAdd")));
				vo.setPkItem(CommonUtils.getString(cgMap.get("pkItemAdd")));
				vo.setItemcode(CommonUtils.getString(cgMap.get("itemcodeAdd")));
				vo.setPkUnit(CommonUtils.getString(cgMap.get("pkUnitAdd")));
			}
		}
		vo.setPkDeptExec(costDataArg.getPkDept());
		vo.setPkDeptNsApp(costDataArg.getPkDept());
		vo.setPkOrgApp(costDataArg.getPkOrg());
		vo.setPkOrgExec(costDataArg.getPkOrg());
		vo.setFlagCg("0");
		vo.setEuDirect(1);
		

		// 系统参数
		vo.setPkOrgSys(user.getPkOrg());
		vo.setPkDeptSys(user.getPkDept());
		vo.setPkEmpSys(user.getPkEmp());
		vo.setNameEmpSys(user.getNameEmp());

		return vo;
	}
	
	/**
	 * 博爱版本床位费计费方式，查询对象转换为记费VO,2020.3.15
	 * @param map
	 * @param cgMap
	 * @param hasAddItem
	 * @return
	 */
	private DeptCgItemVo transferToVOs(Map<String, Object> map,
			Map<String, Object> cgMap,boolean hasAddItem) {
		DeptCgItemVo vo = new DeptCgItemVo();
		CostDataArg costDataArg = localArg.get();
		User user = costDataArg.getUser();
		//包床但不是包床收费项目时
		if(hasAddItem&&"1".equals(CommonUtils.getString(cgMap.get("euHold")))&&!"1".equals(CommonUtils.getString(cgMap.get("flagAdd")))){
			return null;
		}
		//非包床，但不是非包床收费项
		if(hasAddItem&&"0".equals(CommonUtils.getString(cgMap.get("euHold")))&&!"0".equals(CommonUtils.getString(cgMap.get("flagAdd")))){
			return null;
		}
		
		vo.setPkPi(CommonUtils.getString(map.get("pkPi")));
		vo.setBedNo(CommonUtils.getString(map.get("bedNo")));
		vo.setEuPvtype(CommonUtils.getString(map.get("euPvtype")));
		vo.setNamePi(CommonUtils.getString(map.get("namePi")));
		vo.setPkDeptOrd(CommonUtils.getString(map.get("pkDeptNs")));
		vo.setPkDeptPv(CommonUtils.getString(map.get("pkDept")));
		vo.setPkPv(CommonUtils.getString(map.get("pkPv")));
		vo.setDateBill((Date)map.get("dateBed"));
		
		vo.setPrice(CommonUtils.getDouble(cgMap.get("price")));
		vo.setItemname(CommonUtils.getString(cgMap.get("itemname")));
		vo.setCount(CommonUtils.getDouble(cgMap.get("quan")));
		vo.setFlagPd(CommonUtils.getString(cgMap.get("flagPd")));
		vo.setPkItem(CommonUtils.getString(cgMap.get("pkItem")));
		vo.setItemcode(CommonUtils.getString(cgMap.get("itemcode")));
		vo.setPkUnit(CommonUtils.getString(cgMap.get("pkUnit")));
		
		if("1".equals(CommonUtils.getString(cgMap.get("euHold")))){
			vo.setSpec("包床");
		}
		
		vo.setPkDeptExec(costDataArg.getPkDept());
		vo.setPkDeptNsApp(costDataArg.getPkDept());
		vo.setPkOrgApp(costDataArg.getPkOrg());
		vo.setPkOrgExec(costDataArg.getPkOrg());
		vo.setFlagCg("0");
		vo.setEuDirect(1);

		// 系统参数
		vo.setPkOrgSys(user.getPkOrg());
		vo.setPkDeptSys(user.getPkDept());
		vo.setPkEmpSys(user.getPkEmp());
		vo.setNameEmpSys(user.getNameEmp());
		
		return vo;
	}

	/**
	 * 获取记费时间区间
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Date> getSrvDateInterval(Map<String, Object> map) {
		int month_b = Integer.parseInt(map.get("beginMonth").toString());
		int month_e = Integer.parseInt(map.get("endMonth").toString());
		int day_b = Integer.parseInt(map.get("beginDay").toString());
		int day_e = Integer.parseInt(map.get("endDay").toString());
		int year_now = DateUtils.getYear(localArg.get().getDateNow());
		Date begin = null;
		Date end = getDateTime(year_now, month_e, day_e);
		if (month_e < month_b || (month_e == month_b && day_e <= day_b))
			begin = DateUtils.getSpecifiedDay(getDateTime(year_now - 1, month_b, day_b), -1);
		else
			begin = DateUtils.getSpecifiedDay(getDateTime(year_now, month_b, day_b), -1);
		Map<String, Date> timeMap = new HashMap<String, Date>();
		timeMap.put("begin", begin);
		timeMap.put("end", end);
		return timeMap;
	}

	/**
	 * 根据年，月，日获取时间点
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	private Date getDateTime(int year, int month, int day) {
		String dateStr = year + "" + (month > 10 ? month : "0" + month) + ""
				+ (day > 10 ? day : "0" + day) + "235959";
		try {
			return DateUtils.getDefaultDateFormat().parse(dateStr);
		} catch (ParseException e) {
			throw new BusException("转换固定费用有效年月日异常！");
		}
	}

	/**
	 * 更新数量，添加床位费
	 * 
	 * @param map
	 * @param dept
	 * @param flagAdd 包床使用附加收费项目标志
	 * @return
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	private List<DeptCgItemVo> updateItemVO(Map<String, Object> map,
			List<DeptCgItemVo> items, boolean isCgBed,Set<String> containsBedItemPv,boolean flagAdd) throws BusException {
		Date cg_end = localArg.get().getCgEnd();
		// 记床位费
		String pk_pv = CommonUtils.getString(map.get("pkPv"));
		boolean bedFlag = true;//是否记床位费，true 记， false 不记
		if(containsBedItemPv!=null&&containsBedItemPv.size()>0)
			bedFlag = !containsBedItemPv.contains(pk_pv);
		List<DeptCgItemVo> resultList = null;//床位费集合
		List<DeptCgItemVo> result = new ArrayList<DeptCgItemVo>();
		Map<String, Object> bedMap = null;
		Date date_ns = (Date)map.get("dateAdmit");// 入科时间

		Date bed_begin = null; // 床位费开始记费时间
		Object flag_stop_bed = map.get("flagStopBed");// 床位费停止标志
		if ("0".equals(flag_stop_bed == null ? "0" : flag_stop_bed) && isCgBed) {// 未停并且记床位费的情况
			bed_begin = getBeginTime(map, date_ns, "dateBed");
			bedMap = getBedCg(map, bed_begin, cg_end,flagAdd);
			resultList = (List<DeptCgItemVo>) bedMap.get("list");  
		}
		ExTaskLog.log("患者：",pk_pv,"床位费构造结果：",bedMap);
		// 空值校验，空不需要更新数据
		if (items == null)
			return resultList;
		//如果没床位费(即使用的是虚床的情况)，不记固定费用
        if(resultList == null || resultList.size()<=0){
        	return null;
        }
        
		
		// 获取当前患者固定费用
		List<DeptCgItemVo> list = new ArrayList<DeptCgItemVo>();//固定费用集合
		for (DeptCgItemVo vo : items) {
			if (pk_pv.equals(vo.getPkPv())) {
				list.add(vo);
			}
		}
		// 更新count
		for (DeptCgItemVo vo : list) {
			Date day_begin = getBeginTime(map, date_ns, "dateDaycg");
			Date begin = day_begin;
			if(vo.getCgBegin()!=null){
				day_begin = day_begin.before(vo.getCgBegin()) ? vo.getCgBegin():day_begin;
			}
			Date end = cg_end;
			if(vo.getCgEnd()!=null){
				end = cg_end.after(vo.getCgEnd()) ? vo.getCgEnd() : cg_end;
			}
			String eu_cgmode = vo.getEuCgmode();
			if ("0".equals(eu_cgmode)) {// 按人按天计费
				// 计算床位记费次数，按天清算
				int count = 0;
				//当天入院当天出院的情况
				if(DateUtils.getMinsBetween(begin, end)>0&&DateUtils.getMinsBetween(begin, end)<1440&&DateUtils.getDateStr(begin).equals(DateUtils.getDateStr(end))){
					count = 1; 
				}else{
					count = DateUtils.getMinsBetween(begin, end)/1440; 
				}
				for (int i = 0; i < count; i++) {
					DeptCgItemVo cg = new DeptCgItemVo();
					ApplicationUtils.copyProperties(cg, vo);
					cg.setCount(1.00);
					cg.setDosage(CommonUtils.getDouble(cg.getCount()));
					if(DateUtils.getMinsBetween(begin, end)>0&&DateUtils.getMinsBetween(begin, end)<1440){
						cg.setDateItem(end);
					}else{
						cg.setDateItem(DateUtils.getSpecifiedDay(begin, i + 1));
					}
					result.add(cg);
				}  
			} else {//按床按天计费
				if ("0".equals(flag_stop_bed == null ? "0" : flag_stop_bed)   
						&& null != bed_begin && begin.equals(bed_begin)
						&& end.equals(cg_end) && isCgBed) {
					calByDay(result, bedMap, vo);
				} else {
					Map<String, Object> bedMap_s = getBedCg(map, begin, end,flagAdd);
					calByDay(result, bedMap_s, vo);
				}
			}
		}
		
		List<DeptCgItemVo> all = new ArrayList<DeptCgItemVo>();
		//为了兼容没有固定费用的患者，也能记床位费增加的代码
        if(result!=null&&result.size()>0){
        	for(DeptCgItemVo vo : result){//去掉查询固定费用主表出来的空记录
        		if(vo.getCgBegin()!= null && vo.getCgEnd()!= null&&vo.getPkItem()!=null){
        			all.add(vo);
        		}
        	}
        }
        //获取虚床使用记录，为了去除虚床对应时间区间的固定费用
        map.put("dateenditem", DateUtils.getDefaultDateFormat().format(bed_begin));
        List<PvBedVo> list_bedcg = fixedCostPubMapper.queryPvVirtualBedItem(map);
    	//根据床位费对应的时间区间，去除固定费用列表中不在时间区间内的固定费用
        if(list_bedcg!=null&&list_bedcg.size()>0){
        	 Iterator<DeptCgItemVo> it = all.iterator();
              while (it.hasNext()) {
            	  DeptCgItemVo itemvo = it.next();
            	  for(PvBedVo bedvo:list_bedcg){
              		if (null == bedvo.getDateEnd()||bedvo.getDateEnd().after(cg_end))
              			bedvo.setDateEnd(cg_end);
              		if(itemvo.getDateItem().after(bedvo.getDateBegin())&&itemvo.getDateItem().getTime()<=bedvo.getDateEnd().getTime()){
              			it.remove();
              			break;
              		}
              	  }
              }
        }
       
        List<DeptCgItemVo> allList = new ArrayList<DeptCgItemVo>();
        
		// 添加床位费,固定费用里不含床位费的情况
		if (null != resultList && resultList.size() > 0 && bedFlag)
			allList.addAll(resultList);
		// 添加固定费用
		if (null != all && all.size() > 0)
			allList.addAll(all);
		ExTaskLog.log("患者：",pk_pv,"list_bedcg：",list_bedcg,"allList：",allList);
		return allList;
	}

	/**
	 * 获取起始时间
	 * 
	 * @param map
	 *            数据集合
	 * @param date_pv_ns
	 *            时间基准；入科时间
	 * @param dateField
	 *            实际字段名
	 * @return
	 * @throws BusinessException
	 */
	private Date getBeginTime(Map<String, Object> map, Date date_pv_ns,
			String dateField) {
		Date cg_begin = localArg.get().getCgBegin();
		if (null != cg_begin) {
			return cg_begin;
		}
		Date bed_begin;
		Date date_bedcg = (Date) map.get(dateField);
		if (null == date_bedcg) {
			if (null == date_pv_ns) {
				return new Date();
			}
			try {
				//当天入院，当天出院的患者
				if(DateUtils.getDateStr(new Date()).equals(DateUtils.getDateStr(date_pv_ns))){
					bed_begin = DateUtils.getDefaultDateFormat().parse(DateUtils.getDateStr(date_pv_ns)+ "000000");
				}else{
					bed_begin = DateUtils.getDefaultDateFormat().parse(DateUtils
							.getSpecifiedDateStr(date_pv_ns, -1) + "235959");
				}
			} catch (ParseException e) {
				throw new BusException("计算床位费计费开始时间异常");
			}
		} else{
			bed_begin = date_bedcg;
		}
		return bed_begin;
	}

	/**
	 * 床位记费
	 * 
	 * @param map
	 * @param date_begin
	 * @param date_end
	 * @param flagAdd 使用附加收费项目标志
	 * @return
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getBedCg(Map<String, Object> map,
			Date date_begin, Date date_end,boolean flagAdd) throws BusException {
		// 返回结果map(map：日期数量；list：床位记费数量)
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取床位计费项目
		map.put("dateenditem", DateUtils.getDefaultDateFormat().format(date_begin));
		List<Map<String, Object>> list_bedcg = fixedCostPubMapper.queryPvBedItem(map);
        // 如果通用床位费维护模式查不到数据，则查询博爱子表维护床位费模式 2020.3.15
		List<Map<String, Object>> listBedcgItems = null;
		if(list_bedcg==null||list_bedcg.size()<=0){
			listBedcgItems = fixedCostPubMapper.queryPvBedItems(map);
			return getBedCgItems(listBedcgItems,map,date_begin,date_end);
		}
		
		// 缓存初始化
		Map<String, Integer> bedMap = new LinkedHashMap<String, Integer>(); // 日期数量map
		List<DeptCgItemVo> voList = new ArrayList<DeptCgItemVo>(); // 床位voList
		List<DeptCgItemVo> resultList = new ArrayList<DeptCgItemVo>(); // 按天拆分后床位list
		// 计算床位记费次数，按天清算
		int count = 0;
		boolean isTodayOut = false;//当天入，当天出标志
		//当天入院当天出院的情况
		if(DateUtils.getMinsBetween(date_begin, date_end)>0
						&&DateUtils.getMinsBetween(date_begin, date_end)<1440
						&&DateUtils.getDateStr(date_begin).equals(DateUtils.getDateStr(date_end))){
			isTodayOut = true;
			count = 1;
		}else{
			isTodayOut = false;
			count = DateUtils.getMinsBetween(date_begin, date_end)/1440;
		}
		// 计算患者名下床位数
		for (Map<String, Object> cgMap : list_bedcg) {
			// 计算时间区间
			Date begin = (Date) cgMap.get("dateBegin");
			Date endStr = (Date)cgMap.get("dateEnd");
			//当天入，当天出且换了床的情况，不记之前的换床费用
			if(endStr!=null&&DateUtils.getDateStr(endStr).equals(DateUtils.getDateStr(begin))&&"0".equals(cgMap.get("euHold"))&&isTodayOut){
				continue;
			}
			// 转化成VO
			DeptCgItemVo vo = transferToVO(map, cgMap,flagAdd);
			Date end = null;
			if (null == endStr)
				end = date_end;
			else
				end = endStr;
			vo.setCgBegin(begin);
			vo.setCgEnd(end);
			voList.add(vo);
		}
		for (int i = 1; i <= count; i++) {
			Date cgDate = null;
			if(isTodayOut){
				cgDate = date_end;
			}else{
				cgDate = DateUtils.getSpecifiedDay(date_begin, i);
			}
			
			int count_day = 0;
			for (DeptCgItemVo vo : voList) {
				if ((!vo.getCgEnd().before(cgDate)
						&& !vo.getCgBegin().after(cgDate)&&!isTodayOut)||(isTodayOut&&!vo.getCgBegin().after(cgDate))) {
					DeptCgItemVo re = new DeptCgItemVo();
					ApplicationUtils.copyProperties(re, vo);	//	(DeptCgItemVo) vo.clone();
					re.setCount(1.00);
					re.setDateItem(cgDate);
					resultList.add(re);
					count_day++;
					vo.setCount(vo.getCount() + 1);
				}
			}
			bedMap.put(DateUtils.getDefaultDateFormat().format(cgDate), count_day);
		}
		for (DeptCgItemVo vo : resultList) {
			vo.setDosage(CommonUtils.getDouble(vo.getCount()));
		}
		resultMap.put("map", bedMap);
		resultMap.put("list", resultList);
		return resultMap;
	}
    /**
     * 获取床位费集合(针对博爱床位费维护方式2020.3.15)
     * @param listBedcgItems
     * @param map
     * @param date_begin
     * @param date_end
     * @return
     * @throws BusException
     */
	private  Map<String, Object> getBedCgItems(List<Map<String, Object>> listBedcgItems, Map<String, Object> map,
			Date date_begin, Date date_end) throws BusException {
		// 返回结果map(map：日期数量；list：床位记费数量)
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 缓存初始化
		Map<String, Integer> bedMap = new LinkedHashMap<String, Integer>(); // 日期数量map
		List<DeptCgItemVo> voList = new ArrayList<DeptCgItemVo>(); // 床位voList
		List<DeptCgItemVo> resultList = new ArrayList<DeptCgItemVo>(); // 按天拆分后床位list
		// 计算床位记费次数，按天清算
		int count = 0;
		boolean isTodayOut = false;//当天入，当天出标志
		//当天入院当天出院的情况
		if(DateUtils.getMinsBetween(date_begin, date_end)>0
						&&DateUtils.getMinsBetween(date_begin, date_end)<1440
						&&DateUtils.getDateStr(date_begin).equals(DateUtils.getDateStr(date_end))){
			isTodayOut = true;
			count = 1;
		}else{
			isTodayOut = false;
			count = DateUtils.getMinsBetween(date_begin, date_end)/1440;
		}
		//该床位设置的包床收费项目集合
		List<Map<String, Object>> listAddItem = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> beditem : listBedcgItems){
			if("1".equals(CommonUtils.getString(beditem.get("flagAdd")))){
				listAddItem.add(beditem);
			}
		}
		
		// 计算患者名下床位数
		for (Map<String, Object> cgMap : listBedcgItems) {
			// 计算时间区间
			Date begin = (Date) cgMap.get("dateBegin");
			Date endStr = (Date)cgMap.get("dateEnd");
			//当天入，当天出且换了床的情况，不记之前的换床费用
			if(endStr!=null&&DateUtils.getDateStr(endStr).equals(DateUtils.getDateStr(begin))&&"0".equals(cgMap.get("euHold"))&&isTodayOut){
				continue;
			}
			Date end = null;
			if (null == endStr)
				end = date_end;
			else
				end = endStr;
			// 转化成VO
			DeptCgItemVo vo = transferToVOs(map,cgMap,listAddItem==null||listAddItem.size()<=0?false:true);
			if(vo==null)
				continue;
			vo.setCgBegin(begin);
			vo.setCgEnd(end);
			voList.add(vo);
		}
		for (int i = 1; i <= count; i++) {
			Date cgDate = null;
			if(isTodayOut){
				cgDate = date_end;
			}else{
				cgDate = DateUtils.getSpecifiedDay(date_begin, i);
			}
			
			int count_day = 0;
			for (DeptCgItemVo vo : voList) {
				if ((!vo.getCgEnd().before(cgDate)
						&& !vo.getCgBegin().after(cgDate)&&!isTodayOut)||(isTodayOut&&!vo.getCgBegin().after(cgDate))) {
					DeptCgItemVo re = new DeptCgItemVo();
					ApplicationUtils.copyProperties(re, vo);	
					//re.setCount(1);
					re.setDateItem(cgDate);
					resultList.add(re);
					count_day++;
					//vo.setCount(vo.getCount() + 1);
				}
			}
			bedMap.put(DateUtils.getDefaultDateFormat().format(cgDate), count_day);
		}
		for (DeptCgItemVo vo : resultList) {
			vo.setDosage(CommonUtils.getDouble(vo.getCount()));
		}
		resultMap.put("map", bedMap);
		resultMap.put("list", resultList);
		resultMap.put("itemsFlag", "1");
		return resultMap;
	}
	
	/**
	 * 根据床位计算每天的按床记费明细
	 * 
	 * @param result
	 * @param bedMap
	 * @param vo
	 */
	@SuppressWarnings("unchecked")
	private void calByDay(List<DeptCgItemVo> result,
			Map<String, Object> bedMap, DeptCgItemVo vo) {
		Map<String, Integer> m = (Map<String, Integer>) bedMap.get("map");
		if (null == m) {
			return;
		}
		Set<String> s = m.keySet();
		for (String date : s) {
			DeptCgItemVo cg = new DeptCgItemVo();
			ApplicationUtils.copyProperties(cg, vo);
			cg.setCount(CommonUtils.getDouble(m.get(date)));
			cg.setDosage(CommonUtils.getDouble(cg.getCount()));
			try {
				cg.setDateItem(DateUtils.getDefaultDateFormat().parse(date));
			} catch (ParseException e) {

			}
			result.add(cg);
		}
	}

	private class CostDataArg {
		private Date dateNow;
		private Date cgEnd;// 默认昨天的235959
		private Date cgBegin;
		private String pkDept;//开立科室
		private String pkOrg;//开立机构
		private User user;

		public Date getDateNow() {
			return dateNow;
		}

		public void setDateNow(Date dateNow) {
			this.dateNow = dateNow;
		}

		public Date getCgEnd() {
			return cgEnd;
		}

		public void setCgEnd(Date cgEnd) {
			this.cgEnd = cgEnd;
		}

		public Date getCgBegin() {
			return cgBegin;
		}

		public void setCgBegin(Date cgBegin) {
			this.cgBegin = cgBegin;
		}

		public String getPkDept() {
			return pkDept;
		}

		public void setPkDept(String pkDept) {
			this.pkDept = pkDept;
		}

		public String getPkOrg() {
			return pkOrg;
		}

		public void setPkOrg(String pkOrg) {
			this.pkOrg = pkOrg;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}
}
