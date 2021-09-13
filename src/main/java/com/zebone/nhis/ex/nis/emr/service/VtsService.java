package com.zebone.nhis.ex.nis.emr.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOccDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.emr.dao.VtsMapper;
import com.zebone.nhis.ex.nis.emr.vo.ExSynVtsOccVo;
import com.zebone.nhis.ex.nis.emr.vo.ExVtsOccByPv;
import com.zebone.nhis.ex.nis.emr.vo.ExVtsOccVo;
import com.zebone.nhis.ex.nis.emr.vo.NorNurseVo;
import com.zebone.nhis.ex.nis.emr.vo.SynExVtsOcc;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 生命体征服务
 * @author yangxue
 *
 */
@Service
public class VtsService {
	
	@Resource
	private VtsMapper vtsMapper;
	/**
	 * 查询患者体征数据
	 * @param param{pkPv,dateBegin,dateEnd,timeType}
	 * @param user
	 * @return
	 */
    public List<ExVtsOccVo> getVtsDetail(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	try {
    		Date endDate = DateUtils.getDefaultDateFormat().parse(CommonUtils.getString(paramMap.get("dateEnd")));
		    //过滤条件忽略时间部分
			endDate = DateUtils.getSpecifiedDay(endDate, 1);
			Date beginDate = DateUtils.getDefaultDateFormat().parse(CommonUtils.getString(paramMap.get("dateBegin")));
			paramMap.put("dateEnd", endDate);
			paramMap.put("dateBegin", beginDate);
    	} catch (ParseException e) {
			throw new BusException("转换查询时间错误！");
		}
    	List<ExVtsOccVo> vtsList = vtsMapper.queryVtsByPV(paramMap);
    	if(vtsList==null||vtsList.size()<=0) return null;
    	
    	Set<String> pkVtsoccs = new HashSet<String>();
    	List<ExVtsOccVo> result = new ArrayList<ExVtsOccVo>();
    	Map<String,Object> paramT = new HashMap<String,Object>();
    	for(ExVtsOccVo vts : vtsList){
    		pkVtsoccs.add(vts.getPkVtsocc());
    	}
    	paramT.put("pkVtsoccs", CommonUtils.convertSetToSqlInPart(pkVtsoccs, "pk_vtsocc"));
    	List<ExVtsOccDt> detailList = vtsMapper.queryVtsDetailsByPV(paramT);
    	for(ExVtsOccVo vts : vtsList){
			List<ExVtsOccDt> exVtsList = new ArrayList<ExVtsOccDt>(); 
			for(ExVtsOccDt dt :detailList){
				if(dt.getPkVtsocc().equals(vts.getPkVtsocc())){
					if("24".equals(CommonUtils.getString(paramMap.get("timeType")))){
		    			if("1".equals(dt.getEuDateslot())){
	    					dt.setHourVts(CommonUtils.getInteger(dt.getHourVts())+12);
						}
		    		}
		    		exVtsList.add(dt);
				}
			}
    		vts.setDetails(exVtsList);
    		result.add(vts);
    	}
    	return result;
    }
    
    /**
     * 保存生命体征信息
     * @param param 
     * @param user
     */
    public void saveVts(String param,IUser user){
    	List<ExVtsOccVo> vtslist = JsonUtil.readValue(param,new TypeReference<List<ExVtsOccVo>>(){});
    	if(vtslist == null || vtslist.size() < 0 ){
    		throw new BusException("请确认是否录入了生命体征信息");
    	}
    	List<ExVtsOcc> insertOccList = new ArrayList<ExVtsOcc>();
		List<ExVtsOcc> updateOccList = new ArrayList<ExVtsOcc>();
    	List<ExVtsOccDt> insertOccDtList = new ArrayList<ExVtsOccDt>();
		List<ExVtsOccDt> updateOccDtList = new ArrayList<ExVtsOccDt>();
    	for(ExVtsOccVo vts:vtslist){
    		ExVtsOcc occ = new ExVtsOcc();
        	ApplicationUtils.copyProperties(occ, vts);
        	if(CommonUtils.isEmptyString(vts.getPkVtsocc())){//新增
        		if(CommonUtils.isEmptyString(occ.getPkVtsocc())){
        			occ.setPkVtsocc(NHISUUID.getKeyId());
        		}
        		insertOccList.add(occ);
        		List<ExVtsOccDt>  details = vts.getDetails();
            	if(details!=null&&details.size()>0){
            		for(ExVtsOccDt dt:details){
            			dt.setPkVtsoccdt(NHISUUID.getKeyId());
            			dt.setPkVtsocc(occ.getPkVtsocc());
            			ApplicationUtils.setBeanComProperty(dt, true);
            			insertOccDtList.add(dt);
            		}
            	}
        	}else{//修改
        		updateOccList.add(occ);
        		List<ExVtsOccDt>  details = vts.getDetails();
            	if(details!=null&&details.size()>0){
            		for(ExVtsOccDt dt:details){
            			if(CommonUtils.isEmptyString(dt.getPkVtsoccdt())){//新增的明细
            				dt.setPkVtsoccdt(NHISUUID.getKeyId());
            				dt.setPkVtsocc(occ.getPkVtsocc());
                			ApplicationUtils.setBeanComProperty(dt, true);
            				insertOccDtList.add(dt);
            			}else{//修改的数据
            				updateOccDtList.add(dt);
            			}
            		}
            	}
        	}
    	}
    	if(insertOccList!=null&&insertOccList.size()>0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExVtsOcc.class), insertOccList);
		}
		if(updateOccList!=null&&updateOccList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExVtsOcc.class), updateOccList);
		}
    	if(insertOccDtList!=null&&insertOccDtList.size()>0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExVtsOccDt.class), insertOccDtList);
		}
		if(updateOccDtList!=null&&updateOccDtList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExVtsOccDt.class), updateOccDtList);
		}
    	
    }
    
    /**
     * 查询生命体征信息
     * @param param{pkDeptNs,dateCur,euDateslot,hourVts}
     * @param user
     * @return
     */
    public List<ExVtsOccVo> queryVts(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	String dateCur = "";
		dateCur = CommonUtils.getString(paramMap.get("dateCur")).substring(0, 8)+"000000";
		paramMap.put("dateCur", dateCur);
	    Map<String,Object> map = DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where pk_dept = ? and dt_depttype = '0310'", new Object[]{paramMap.get("pkDeptNs")});
	    List<ExVtsOccVo> vts = new ArrayList<ExVtsOccVo>();
	    if(map == null || CommonUtils.isNull(map.get("pkDept"))){//不是产房
	    	 vts = vtsMapper.queryVtsByDate(paramMap);
    	}else{//产房
    		vts = vtsMapper.queryLaborVtsByDate(paramMap);
    	}
    	if(vts==null || vts.size()<=0) return null;
    	
    	Set<String> pkVtsoccs = new HashSet<String>();
    	for(ExVtsOccVo vo :vts){
    		if(!CommonUtils.isEmptyString(vo.getPkVtsocc())){
    			pkVtsoccs.add(vo.getPkVtsocc());	
    		}
    	}
		paramMap.put("pkVtsoccs", CommonUtils.convertSetToSqlInPart(pkVtsoccs, "pk_vtsocc"));
		List<ExVtsOccDt> queryVtsDetails = vtsMapper.queryVtsDetailsByPk(paramMap);
		for(ExVtsOccVo vo :vts){
			List<ExVtsOccDt> exVtsList = new ArrayList<ExVtsOccDt>(); 
			for (ExVtsOccDt exVtsOccDt : queryVtsDetails) {
				if(exVtsOccDt.getPkVtsocc().equals(vo.getPkVtsocc())){
					exVtsList.add(exVtsOccDt);
				}
			}
			if(exVtsList != null && exVtsList.size() > 0){
				vo.setDetails(exVtsList);
			}
    	}
    	return vts;
    }
    
    /**
     * 一般护理信息 - 按科室
     * @param param
     * @param user
     */
	public List<NorNurseVo> queryNorNurse(String param, IUser user) throws Exception {
		Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
		Integer euDateslot = Integer.parseInt(paramMap.get("euDateslot"));
		Integer hourVts = Integer.parseInt(paramMap.get("hourVts"));
		String dateCur = paramMap.get("dateCur");
		// 如果午时为1 时点 +12
		if (euDateslot > 0) {
			hourVts += 12;
		}
		// 时间格式转换
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		formatter.setLenient(false);
		Date newDate = formatter.parse(dateCur);
		formatter = new SimpleDateFormat("yyyyMMdd");
		dateCur = formatter.format(newDate);
		String maxHour = dateCur + (hourVts < 10 ? "0" + hourVts : hourVts) + "5959";
		String minHour = dateCur + (hourVts < 10 ? "0" + hourVts : hourVts)  + "0000";
		paramMap.put("maxHour", maxHour);
		paramMap.put("minHour", minHour);
		List<NorNurseVo> list = vtsMapper.queryNorNurse(paramMap);
		return list;
	}

	/**
	 * 一般护理信息 - 按患者
	 * 
	 * @param param
	 * @param user
	 */
	public List<NorNurseVo> queryNorNurseByPv(String param, IUser user) throws Exception {
		Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap.get("pkPv") == null || CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
			throw new BusException("未获取到患者就诊主键！");
		if (paramMap.get("dateCur") == null || CommonUtils .isEmptyString(paramMap.get("dateCur").toString()))
			throw new BusException("未获取到体征录入日期！");

		String dateCur = paramMap.get("dateCur");
		// 时间格式转换
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		formatter.setLenient(false);
		Date newDate = formatter.parse(dateCur);
		formatter = new SimpleDateFormat("yyyyMMdd");
		dateCur = formatter.format(newDate);

		paramMap.put("maxHour", dateCur + "235959");
		paramMap.put("minHour", dateCur + "000000");
		List<NorNurseVo> list = vtsMapper.queryNorNurse(paramMap);
		return list;
	}
    
    /**
     * 根据患者
     * @param param
     * @param user
     * @return
     */
	public ExVtsOccByPv queryVtsByPv(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	if(paramMap == null)
    		throw new BusException("未获取到查询参数！");
    	if(paramMap.get("pkPv") == null|| CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
    		throw new BusException("未获取到患者就诊主键！");
    	if(paramMap.get("dateOcc") == null|| CommonUtils.isEmptyString(paramMap.get("dateOcc").toString()))
    		throw new BusException("未获取到体征录入日期！");
    	
    	List<ExVtsOccByPv> vtsList = vtsMapper.queryVtsByPvAndDateByOne(paramMap);//查询患者录入日期的记录
    	if(vtsList == null || vtsList.size() < 1) 
    		throw new BusException("未查询到患者["+paramMap.get("pkPv")+"]在["+paramMap.get("dateOcc")+"]的生命体征信息！");
    	else if(vtsList.size() > 1)
    		throw new BusException("该患者["+paramMap.get("pkPv")+"]在["+paramMap.get("dateOcc")+"]存在多条生命体征信息！");
    	
    	ExVtsOccByPv vtsByPv = vtsList.get(0);
    	if(!CommonUtils.isEmptyString(vtsByPv.getPkVtsocc())){
    		paramMap.put("pkVtsoccs", "'" + vtsByPv.getPkVtsocc() +"'");
    		List<ExVtsOccDt> dtList = vtsMapper.queryVtsDetailsByPV(paramMap);
    		vtsByPv.setListDts(dtList);
    	}
    	return vtsByPv;
    }
	
	/**
     * 保存生命体征信息
     * @param param 
     * @param user
     */
    public void saveVtsByNd(String param,IUser user){
    	List<ExVtsOccVo> vtslist = JsonUtil.readValue(param,new TypeReference<List<ExVtsOccVo>>(){});
    	if(vtslist == null || vtslist.size() < 0 ){
    		throw new BusException("请确认是否录入了生命体征信息");
    	}
    	ExVtsOcc vtsocc = new ExVtsOcc();
    	
		List<ExVtsOcc> insertOccList = new ArrayList<ExVtsOcc>();
    	List<ExVtsOccDt> insertOccDtList = new ArrayList<ExVtsOccDt>();
		List<ExVtsOccDt> updateOccDtList = new ArrayList<ExVtsOccDt>();
		
    	for(ExVtsOccVo vts:vtslist){
    		ExVtsOcc occ = new ExVtsOcc();
        	ApplicationUtils.copyProperties(occ, vts);
        	vtsocc = DataBaseHelper.queryForBean("select * from ex_vts_occ where pk_pv = ? and date_vts = ?", ExVtsOcc.class, occ.getPkPv(),occ.getDateVts());
        	if(vtsocc == null || CommonUtils.isEmptyString(vts.getPkVtsocc())){
        		insertOccList.add(occ);
        		List<ExVtsOccDt>  details = vts.getDetails();
        		if(details!=null&&details.size()>0){
        			for(ExVtsOccDt dt:details){
        				dt.setPkVtsoccdt(NHISUUID.getKeyId());
        				dt.setPkVtsocc(occ.getPkVtsocc());
        				ApplicationUtils.setBeanComProperty(dt, true);
        				insertOccDtList.add(dt);
        			}
        		}
        	}else{
        		List<ExVtsOccDt>  details = vts.getDetails();
        		if(details!=null&&details.size()>0){
        			for(ExVtsOccDt dt:details){
        				if(CommonUtils.isEmptyString(dt.getPkVtsoccdt())){//新增的明细
        					dt.setPkVtsoccdt(NHISUUID.getKeyId());
        					dt.setPkVtsocc(occ.getPkVtsocc());
        					ApplicationUtils.setBeanComProperty(dt, true);
        					insertOccDtList.add(dt);
        				}else{//修改的数据
        					updateOccDtList.add(dt);
        				}
        			}
        		}
        	}
    	}
    	if(insertOccList!=null&&insertOccList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExVtsOcc.class), insertOccList);
		}
    	if(insertOccDtList!=null&&insertOccDtList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExVtsOccDt.class), insertOccDtList);
		}
		if(updateOccDtList!=null&&updateOccDtList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExVtsOccDt.class), updateOccDtList);
		}
    }
    @SuppressWarnings("deprecation")
	public void synVtsByPatNoboai(String param,IUser user) throws ParseException{
    	SynExVtsOcc synExVtsOcc = JsonUtil.readValue(param, SynExVtsOcc.class);
    	Map<String,Object> paramMap = synExVtsOcc.getSynParam();
    	if(paramMap == null)
    		throw new BusException("未获取到查询参数！");
    	if(paramMap.get("pkPv") == null|| CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
    		throw new BusException("未获取到患者就诊主键！");
    	if(paramMap.get("time") == null|| CommonUtils.isEmptyString(paramMap.get("time").toString()))
    		throw new BusException("未获取到体征录入日期！");
    	if(paramMap.get("start") == null|| paramMap.get("end") == null)
    		throw new BusException("未获取到出入量日期！");
    	//先根据就诊主键获取患者的患者编码和此次就诊次数
    	String qryPatsql="select code_pi,ip_times from view_emr_pat_list where pk_pv=?";
    	Map<String,Object> patMap=DataBaseHelper.queryForMap(qryPatsql, paramMap.get("pkPv"));
    	/*if(patMap){//判断参数非空
    		
    	}*/
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date tmps = sdf.parse(paramMap.get("time").toString());//录入日期
		Date tmpstart = sdf.parse(paramMap.get("start").toString());//出入量开始日期
		Date tmpsend = sdf.parse(paramMap.get("end").toString());//出入量结束日期
		Calendar c = Calendar.getInstance(); 
		c.setTime(tmps);
		c.add(Calendar.DAY_OF_MONTH,1); 
		Date endDate=c.getTime();
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		paramMap.put("beTime", sdf.format(tmps));//录入日期
		paramMap.put("enTime", sdf.format(endDate));
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		paramMap.put("inOutTimeS", sdf.format(tmpstart));//出入量开始日期
		paramMap.put("inOutTimeE", sdf.format(tmpsend));//出入量结束日期
		
		ExVtsOcc vtsocc = JsonUtil.readValue(param, ExVtsOcc.class);
		List<ExVtsOccDt> occDtList=new ArrayList<ExVtsOccDt>();
		if(CommonUtils.isEmptyString(vtsocc.getPkVtsocc())){
			DataBaseHelper.insertBean(vtsocc);
		}else{
			//查询生命体征主表根据就诊主键和日期
			String occDtSql="select * from ex_vts_occ_dt where pk_vtsocc=? order by hour_vts";
			occDtList=DataBaseHelper.queryForList(occDtSql, ExVtsOccDt.class,vtsocc.getPkVtsocc());
		}
		//查询录入日期那天的所有体征数据
    	String synValSql="select * from view_emr_icu_sign where code_pi=? and ip_times=? and rec_date>=? and rec_date<? order by REC_DATE";
    	List<ExSynVtsOccVo> synVts=DataBaseHelper.queryForList(synValSql, ExSynVtsOccVo.class, patMap.get("codePi"),patMap.get("ipTimes"),paramMap.get("beTime"),paramMap.get("enTime"));
    	String inOutValSql="select * from view_emr_icu_sign where code_pi=? and ip_times=? and rec_date>=? and rec_date<=? and (rec_type='入量' or rec_type='出量')";
    	List<ExSynVtsOccVo> inOutValList=DataBaseHelper.queryForList(inOutValSql, ExSynVtsOccVo.class, patMap.get("codePi"),patMap.get("ipTimes"),paramMap.get("inOutTimeS"),paramMap.get("inOutTimeE"));
    	//查询血压
    	/*String bloodSql="select * from view_emr_icu_sign where code_pi=? and ip_times=? and rec_date>=? and rec_date<? and  rec_name like '血压%' order by rec_date,rec_name";
    	List<ExSynVtsOccVo> bloodList=DataBaseHelper.queryForList(bloodSql, ExSynVtsOccVo.class, patMap.get("codePi"),patMap.get("ipTimes"),paramMap.get("beTime"),paramMap.get("enTime"));*/
    	
    	sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
    	//处理血压
    	for (ExSynVtsOccVo blood : synVts) {
    		if(blood.getRecName().equals("血压High") || blood.getRecName().equals("血压Low")){
    			calendar.setTime(sdf.parse(blood.getRecDate()));
    			if(calendar.get(Calendar.HOUR_OF_DAY)==8){
    				if(blood.getRecName().equals("血压High")){
    					vtsocc.setValSbp(Long.parseLong(blood.getRecValue().split(".")[0]));
    				}
    				if(blood.getRecName().equals("血压Low")){
    					vtsocc.setValDbp(Long.parseLong(blood.getRecValue().split(".")[0]));
    				}
    			}
    			if(calendar.get(Calendar.HOUR_OF_DAY)==14){
    				if(blood.getRecName().equals("血压High")){
    					vtsocc.setValSbpAdd(Long.parseLong(String.valueOf(Double.valueOf(blood.getRecValue()).intValue())));
    				}
    				if(blood.getRecName().equals("血压Low")){
    					vtsocc.setValDbpAdd(Long.parseLong(String.valueOf(Double.valueOf(blood.getRecValue()).intValue())));
    				}
    			}
    		}
		}
    	//如果8点或者14点没有的话，重新去找一下上午8点到12的记录如果有插入到8点那次，下午是1点到24点，有记录则插入到14点那次
    	if(vtsocc.getValSbp()==null || vtsocc.getValSbpAdd()==null){
    		for (ExSynVtsOccVo blood : synVts) {
    			if(blood.getRecName().equals("血压High") || blood.getRecName().equals("血压Low")){
	    			calendar.setTime(sdf.parse(blood.getRecDate()));
	    			if(vtsocc.getValSbp()==null){
	    				if(calendar.get(Calendar.HOUR_OF_DAY)>8 && calendar.get(Calendar.HOUR_OF_DAY)<=12){
	        				if(blood.getRecName().equals("血压High")){
	        					vtsocc.setValSbp(Long.parseLong(blood.getRecValue().split(".")[0]));
	        				}
	        				if(blood.getRecName().equals("血压Low")){
	        					vtsocc.setValDbp(Long.parseLong(blood.getRecValue().split(".")[0]));
	        				}
	        			}
	    			}
	    			if(vtsocc.getValSbpAdd()==null){
	    				if(calendar.get(Calendar.HOUR_OF_DAY)>12 && calendar.get(Calendar.HOUR_OF_DAY)<24){
	        				if(blood.getRecName().equals("血压High")){
	        					vtsocc.setValSbpAdd(Long.parseLong(String.valueOf(Double.valueOf(blood.getRecValue()).intValue())));
	        				}
	        				if(blood.getRecName().equals("血压Low")){
	        					vtsocc.setValDbpAdd(Long.parseLong(String.valueOf(Double.valueOf(blood.getRecValue()).intValue())));
	        				}
	        			}
	    			}
    			}
    		}
    	}
    	//处理24小时出入量
    	String elseName="";//声明一个其他出量，记录名称
    	for (ExSynVtsOccVo inOut : inOutValList) {
    		//1.统计入量
    		if(inOut.getRecType().equals("入量")){
    			if(vtsocc.getValInput()==null){
    				vtsocc.setValInput(inOut.getRecValue());
    			}else{
    				vtsocc.setValInput(String.valueOf(Double.valueOf(vtsocc.getValInput())+Double.valueOf(inOut.getRecValue())));
    			}
    		}
    		//2.统计出量
    		if(inOut.getRecType().equals("出量")){
    			if(vtsocc.getValOutputTotal()==null){
    				vtsocc.setValOutputTotal(inOut.getRecValue());
    			}else{
    				vtsocc.setValOutputTotal(String.valueOf(Double.valueOf(vtsocc.getValOutputTotal())+Double.valueOf(inOut.getRecValue())));
    			}
    			
    		}
    		//3.统计尿量
    		if(inOut.getRecName().equals("尿量")){
    			if(vtsocc.getValUrine()==null){
    				vtsocc.setValUrine(inOut.getRecValue());
    			}else{
    				vtsocc.setValUrine(String.valueOf(Double.valueOf(vtsocc.getValUrine())+Double.valueOf(inOut.getRecValue())));
    			}
    			
    		}
    		//4.其他出量
    		if(!inOut.getRecName().equals("尿量") && inOut.getRecType().equals("出量")){
    			elseName=inOut.getRecName();
    			if(inOut.getRecName().equals(elseName)){
    				if(vtsocc.getValOutput()==null){
    					vtsocc.setValOutput(inOut.getRecValue());
    				}else{
    					vtsocc.setValOutput(String.valueOf(Double.valueOf(vtsocc.getValOutput()+Double.valueOf(inOut.getRecValue()))));
    				}
    			}
    		}
		}
    	vtsocc.setHourInput(Integer.parseInt(paramMap.get("hour").toString()));//入量小时
    	vtsocc.setHourUrine(Integer.parseInt(paramMap.get("hour").toString()));//尿量小时
    	vtsocc.setHourOutputTotal(Integer.parseInt(paramMap.get("hour").toString()));//总出量小时
    	vtsocc.setHourOutput(Integer.parseInt(paramMap.get("hour").toString()));//其他出量小时
    	//处理大便次数
    	for (ExSynVtsOccVo exSynVtsOccVo : synVts) {
			if(exSynVtsOccVo.getRecName().equals("大便")){
				if(vtsocc.getValStool()==null){
					vtsocc.setValStool(Long.parseLong(String.valueOf(1)));
				}else{
					vtsocc.setValStool(vtsocc.getValStool()+1);
				}
				
			}
    	}
    	//更新主表的数据
    	DataBaseHelper.updateBeanByPk(vtsocc,false);
    	//处理子表的数据
    	sdf = new SimpleDateFormat("yyyy-MM-dd");
    	List<ExVtsOccDt> insertList = new ArrayList<ExVtsOccDt>();
		List<ExVtsOccDt> updateList = new ArrayList<ExVtsOccDt>();
    	for (int i = 0; i < 6; i++) {
			int hours=i*4+3;
			ExVtsOccDt exVtsOccDt=null;
			if(occDtList!=null && occDtList.size()>0){
				for (int j = 0; j < occDtList.size(); j++) {
					if(occDtList.get(j).getHourVts()==hours){
						exVtsOccDt=occDtList.get(j);
						break;
					}
				}
			}
			if(exVtsOccDt==null){
				exVtsOccDt=new ExVtsOccDt();
			}
			String jlTime="";
			if(hours==3 || hours==7){
				jlTime=sdf.format(tmps)+" 0"+hours+":00:00.000";
			}else{
				jlTime=sdf.format(tmps)+" "+hours+":00:00.000";
			}
			
			for (ExSynVtsOccVo exSynVtsOccVo : synVts) {
				exVtsOccDt.setDateVts(sdf.parse(jlTime));
				if(exSynVtsOccVo.getRecDate().equals(jlTime)&&exSynVtsOccVo.getRecName().equals("体温")){
					exVtsOccDt.setValTemp(BigDecimal.valueOf(Double.parseDouble(exSynVtsOccVo.getRecValue())));
				}else if(exSynVtsOccVo.getRecDate().equals(jlTime)&&exSynVtsOccVo.getRecName().equals("呼吸")){
					exVtsOccDt.setValBre(BigDecimal.valueOf(Double.parseDouble(exSynVtsOccVo.getRecValue())));
	    		}else if(exSynVtsOccVo.getRecDate().equals(jlTime)&&exSynVtsOccVo.getRecName().equals("心率")){
	    			exVtsOccDt.setValPulse(BigDecimal.valueOf(Double.parseDouble(exSynVtsOccVo.getRecValue())));
	    		}
			}
			if(exVtsOccDt.getPkVtsoccdt()==null){
				insertList.add(exVtsOccDt);
			}else{
				updateList.add(exVtsOccDt);
			}
		}
    	if(insertList!=null&&insertList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExVtsOccDt.class), insertList);
		}
		if(updateList!=null&&updateList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExVtsOccDt.class), updateList);
		}
    }
}
