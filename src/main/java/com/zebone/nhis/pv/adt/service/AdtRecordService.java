package com.zebone.nhis.pv.adt.service;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pv.adt.dao.AdtRecordMapper;
import com.zebone.nhis.pv.adt.vo.PvAdtRecord;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class AdtRecordService {

	@Autowired
	public AdtRecordMapper recordMapper;
	
	public Map<String,Object> qryPvMessage(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){});
		if(map==null || (CommonUtils.isNull(map.get("codeIp"))&&CommonUtils.isNull(map.get("pkPv"))))
			throw new BusException("至少传入一个参数！");
		Map<String,Object> rtnMap =null;
		List<Map<String,Object>> list = recordMapper.qryPvMessage(map);
		if(list!=null&&list.size()>0) rtnMap= list.get(0);
		return rtnMap;
	}
    public List<Map<String,Object>> qryPvAdtRecord(String param, IUser user) {
    	Map<String,Object> map = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){});
		if(map==null || CommonUtils.isNull(map.get("pkPv"))) throw new BusException("传参pkPv为空！");
		String pkPv = map.get("pkPv").toString();
		List<Map<String,Object>> list = recordMapper.qryPvAdtRecord(pkPv);
		return list;
	}
    public void updatePvAdtFlagNone(String param, IUser user){
    	
    	List<PvAdtRecord> adtRecords = JsonUtil.readValue(param, new TypeReference<List<PvAdtRecord>>(){});
    	if(adtRecords==null || adtRecords.size()<=0) throw new BusException("传参为空！");
    	DataBaseHelper.batchUpdate("update pv_adt set flag_none=:flagNone where pk_adt=:pkAdt ", adtRecords);
    }
}
