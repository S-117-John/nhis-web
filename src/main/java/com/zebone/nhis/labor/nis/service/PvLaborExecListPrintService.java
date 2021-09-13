package com.zebone.nhis.labor.nis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOccPrt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.support.ExlistPrintSortByOrdUtil;
import com.zebone.nhis.ex.pub.support.PatiPvInfoUtil;
import com.zebone.nhis.labor.nis.dao.PvLaborExecListPrintMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 产房执行单打印
 * @author yangxue
 *
 */
@Service
public class PvLaborExecListPrintService {
	@Resource
	private PvLaborExecListPrintMapper pvLaborExecListPrintMapper;
    /**
     * 
     * @param param
     * {
     * type:执行单类型（0：变更单，1：护理，2：口服，3：注射，4：输液，5：饮食，6：治疗，7：其他）
        date:查询日期
        time:查询班次
        euAlways:0长期，1临时（其他情况不传）
        flagPrint:0未打印，1已打印
        pkPvs:就诊主键
        pkDeptNs:病区主键
        }
     * @param user
     * @return
     */
	public List<Map<String,Object>> queryExlist(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<String> pvlist = (List)map.get("pkPvs");
		if(map == null || map.get("date")==null ||map.get("type")==null)
				throw new BusException("未获取到查询条件");
    	if(pvlist == null || pvlist.size()<=0)
    		 throw new BusException("未获取到患者就诊信息！");
		
		if(map.get("time")!=null){
			convertTime(CommonUtils.getString(map.get("date")),qryBanCiInfo(CommonUtils.getString(map.get("time"))),map);
		}
		String type = CommonUtils.getString(map.get("type"));
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		switch (type)
		{
		case "0":
			list = pvLaborExecListPrintMapper.queryChangedOrderList(map);
			break;
		case "1":
			list = pvLaborExecListPrintMapper.queryNsExlistList(map);
			break;
		case "2":
			list = pvLaborExecListPrintMapper.queryKFExlistList(map);
			break;
		case "3":
			list = pvLaborExecListPrintMapper.queryZSExlistList(map);
			break;
		case "4":
			list = pvLaborExecListPrintMapper.querySYExlistList(map);
			break;
		case "5":
			list = pvLaborExecListPrintMapper.queryYSExlistList(map);
			break;
		case "6":
			list = pvLaborExecListPrintMapper.queryZLExlistList(map);
			break;
		case "7":
			list = pvLaborExecListPrintMapper.queryQTExlistList(map);
			break;
		}
		if(list!=null&&list.size()>0){
			new ExlistPrintSortByOrdUtil().ordGroup(list);
		}
		return list;
	}
	//查询班次对应的时间区间 
	private Map<String,Object> qryBanCiInfo(String code_dateslot) {
		Map<String,Object> map=new HashMap<String,Object>();
		if("0200".equals(code_dateslot)){
			map.put("begint", "00:00:00");
			map.put("endt", "23:59:59");
			return map;
		}
		String sql = "select slot.time_begin as begint,slot.time_end as endt from bd_defdoc doc  inner join bd_defdoclist lst on lst.code=doc.code_defdoclist"
		+" inner join bd_code_dateslot slot on slot.dt_dateslottype=doc.code where lst.code ='020005' and doc.code ='02' and slot.code_dateslot = '"+code_dateslot+"' ";
		map = DataBaseHelper.queryForMap(sql, new HashMap<String,Object>());
		return map;
	}
	/**
	 * 转换查询日期
	 * @param date
	 * @param time
	 * @param paramMap
	 * @return
	 */
	private void convertTime(String date,Map<String,Object> time,Map<String,Object> paramMap){
		String begin = CommonUtils.getString(time.get("begint"));
		String beginTime = begin == null?"000000":begin.replaceAll(":", "");
		String end = CommonUtils.getString(time.get("endt"));
		String endTime = end == null?"595959":end.replaceAll(":", "");	
		paramMap.put("dateBegin", date+beginTime);
		paramMap.put("dateEnd", date+endTime);
	}
	
}
