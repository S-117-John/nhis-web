package com.zebone.nhis.ex.nis.qry.service;

import com.zebone.nhis.common.module.pv.PvIpDaily;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.qry.dao.DeptDayReportMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 病区日报
 * @author yangxue
 *
 */
@Service
public class QueryDeptNsReportService  {
	
	@Resource
	private DeptDayReportMapper deptDayReportMapper;
	/**
	 * 查询病区日报
	 * @param param{dateCur,pkDeptNs}
	 * @param user
	 * @return
	 */
	public PvIpDaily queryReportInfo(String param,IUser user){
		Map<String, Object> inMap=JsonUtil.readValue(param, Map.class);
		if(inMap.get("dateSa")==null){
			throw new BusException("当前时间未得到！");
		}else if(inMap.get("pkDeptNs")==null){
			throw new BusException("当前病区未得到！");
		}else{
			String dateSa=CommonUtils.getString(inMap.get("dateSa"));
			String infantCode = CommonUtils.getString(inMap.get("InfantCode"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhmmss");
			SimpleDateFormat dateSaFormat=new SimpleDateFormat("yyyyMMdd");
			Date dateTime = null;
            String yearStart = "";
            String yearEnd = "";
			try {
				dateTime = sdf.parse(dateSa);
			} catch (ParseException e) {
				throw new BusException("转换传入的日期异常");
			}
            if(infantCode != null && !"".equals(infantCode)){
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(dateTime);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                calendar.add (Calendar.SECOND, 1);
                Date eneDtate  = calendar.getTime();
                yearStart = format.format(dateTime);
                yearEnd = format.format(eneDtate);
            }else{
            	String date = dateSaFormat.format(dateTime);
                yearStart=date.substring(0,8)+"000000";
                yearEnd=date.substring(0,8)+"235959";
            }

			inMap.put("yearStart", yearStart);
			inMap.put("yearEnd", yearEnd);
			inMap.remove("dateSa");
			PvIpDaily pvIpDaily=deptDayReportMapper.getDeptDayReportByPkAndDate(inMap);
			return pvIpDaily;
		}
		
		
	}
	/**
	 *根据时间和所属病区统计该病区日报
	 * @param param [dateSa,pkDeptNs}
	 * @param user 
	 * @return
	 */
	public PvIpDaily statDeptDayReport(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String dateCur = CommonUtils.getString(map.get("dateSa"));
        String infantCode = CommonUtils.getString(map.get("InfantCode"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhmmss");
		SimpleDateFormat dateSaFormat=new SimpleDateFormat("yyyyMMdd");
		Date dateTime = null;
        String beginDateStr = "";
        String endDateStr = "";
		try {
			dateTime = sdf.parse(dateCur);
		} catch (ParseException e) {
			throw new BusException("转换传入的日期异常");
		}
        if(infantCode != null && !"".equals(infantCode)){
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(dateTime);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            calendar.add (Calendar.SECOND, 1);
            Date eneDtate = calendar.getTime();
            beginDateStr = format.format(dateTime);
            endDateStr = format.format(eneDtate);
        }else{
			String date = dateSaFormat.format(dateTime);
            beginDateStr = date.substring(0,8)+"000000";
            endDateStr = date.substring(0,8)+"235959";
        }

		PvIpDaily pvIpDaily=new PvIpDaily();
		//前前一天，用来统计期初人数
		String yesBeforeBegin = "";
		String yesBeforeEnd = "";
		if(!"".equals(dateCur)){
			map.put("yesBegin", beginDateStr);
			map.put("yesEnd", endDateStr);
			try {
				yesBeforeBegin = DateUtils.getSpecifiedDateStr(DateUtils.getDefaultDateFormat().parse(dateCur), -1);
			} catch (ParseException e) {
				throw new BusException("转换传入的日期异常");
			}
			yesBeforeEnd = yesBeforeBegin+"235959";
			map.put("yesBeforeEnd", yesBeforeEnd);
		}
		//获取额定床位及开放床位数
		Map<String,Object> bedMap = deptDayReportMapper.getBedNumByDept(map);
		BigDecimal inhospYd=deptDayReportMapper.getQichuNumByDept(map);//期初人数=昨日留院
		BigDecimal admit=deptDayReportMapper.getDayInNumByDept(map);//今日入院
		BigDecimal discharge=deptDayReportMapper.getDayOutNumByDept(map);//今日出院
		BigDecimal transIn=deptDayReportMapper.getDeptAdtInByDept(map);//他科转入
		BigDecimal transOut=deptDayReportMapper.getDeptAdtOutNumByDept(map);//他科转出
		BigDecimal inhosp=inhospYd.add(admit).add(transIn).subtract(discharge).subtract(transOut);//计算今日留院
		
		pvIpDaily.setBednum(bedMap==null?new BigDecimal(0):(BigDecimal)bedMap.get("BED"));//额定床位
		pvIpDaily.setBednumOpen(bedMap==null?new BigDecimal(0):(BigDecimal)bedMap.get("BEDNUMOPEN"));
		pvIpDaily.setInhospYd(inhospYd);//期初人数=昨日留院
		pvIpDaily.setAdmit(admit);//今日入院
		pvIpDaily.setDischarge(discharge);//今日出院
		pvIpDaily.setTransIn(transIn);//他科转入
		pvIpDaily.setTransOut(transOut);//转往他科
		pvIpDaily.setSeverenum(deptDayReportMapper.getBzNumByDept(map));//病重人数
		pvIpDaily.setRiskynum(deptDayReportMapper.getBwNumByDept(map));//病危人数
		pvIpDaily.setInhosp(inhosp);//今日留院
		pvIpDaily.setDeathnum(deptDayReportMapper.getDeathByDept(map));//死亡人数
		map.put("hldj",ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_I", ""));
		pvIpDaily.setNurseFirst(deptDayReportMapper.getHLNumByDept(map));//一级护理
		map.put("hldj",ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_T", ""));
		pvIpDaily.setNurseSpec(deptDayReportMapper.getHLNumByDept(map));//特级护理
		pvIpDaily.setAccomnum(deptDayReportMapper.getAttendNumByDept(map));//留陪人数
		return pvIpDaily;
	}
	/**
	 * 录入病区ADT数据日报
	 * @param
	 * @return
	 */
	public PvIpDaily saveDeptDayReport(String param,IUser user){
		PvIpDaily pvIpDaily= getPvIpDaity(param, user);
		User newUser=(User)user;
		if(pvIpDaily.getPkIpdaily()!=null && !"".equals(pvIpDaily.getPkIpdaily())){//执行更新
			deptDayReportMapper.modifyDeptDayReport(pvIpDaily);
		}else{//执行插入
			String userId=newUser.getId();
			pvIpDaily.setCreator(userId);//创建人
			pvIpDaily.setCreateTime(new Date());//创建时间
			DataBaseHelper.insertBean(pvIpDaily);
		}
		return pvIpDaily;
	}
	
	/**
	 *@param [PvIpDaily的字段:BedNum,BedNumOpen,Admit,TransIn,
	 * 		  Discharge,TransOut,Inhosp,DeathNum,RiskyNum,ServerNum,NurseSpec,
	 * 		  NurseFirst,AccomNum,Node]
	 * @return
	 */
	public PvIpDaily getPvIpDaity(String param,IUser user){
		PvIpDaily pvIpDaily=JsonUtil.readValue(param, PvIpDaily.class);
		if(pvIpDaily!=null){
			User newUser=(User)user;
			String pkOrg=newUser.getPkOrg();//所属机构
			String pkDept=newUser.getPkDept();//所属科室
			BigDecimal daysTotal=new BigDecimal(0);//住院总天数<未完成>
			String delFlag="1";//删除标志
			pvIpDaily.setPkOrg(pkOrg);//机构
			pvIpDaily.setPkDept(pkDept);//科室
			pvIpDaily.setDaysTotal(daysTotal);//住院总天数
			pvIpDaily.setModifier(newUser.getId());//获取当前修改人信息
			pvIpDaily.setModityTime(new Date());//得到当前修改时间
			pvIpDaily.setDelFlag(delFlag);//删除标志
			pvIpDaily.setTs(new Date());
		}
		return pvIpDaily;
	}

}
