package com.zebone.nhis.bl.pub.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.DoctorMaintainMapper;
import com.zebone.nhis.bl.pub.vo.DoctorMaintainVo;
import com.zebone.nhis.common.module.bl.support.BdOrdMt;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class DoctorMaintainService {
	@Resource
	private DoctorMaintainMapper doctorMaintainMapper;
	
	public List<DoctorMaintainVo> QueryList(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		String pkdept=(String)map.get("pkdept");
		List<DoctorMaintainVo> list=doctorMaintainMapper.QueryList(pkdept);
		return list;
	}
	
	public void AddList(String param,IUser user){
		List<DoctorMaintainVo> list = JsonUtil.readValue(param,new TypeReference<List<DoctorMaintainVo>>(){});
		for (DoctorMaintainVo doctorMaintainVo : list) {
				BdOrdMt bd=Assignment(doctorMaintainVo);
				DataBaseHelper.insertBean(bd);
		}
	}
	public void UpdateList(String param,IUser user){
		List<DoctorMaintainVo> list = JsonUtil.readValue(param,new TypeReference<List<DoctorMaintainVo>>(){});
		for (DoctorMaintainVo doctorMaintainVo : list) {
				BdOrdMt bd=Assignment(doctorMaintainVo);
				DataBaseHelper.updateBeanByPk(bd, false);
		}
	}
	public void DeleteList(String param,IUser user){
		List<DoctorMaintainVo> list = JsonUtil.readValue(param,new TypeReference<List<DoctorMaintainVo>>(){});
		for (DoctorMaintainVo doctorMaintainVo : list) {
			DataBaseHelper.update("update bd_ord_mt set del_flag = '1' ,ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') where pk_ordmt = ? ", new Object[]{doctorMaintainVo.getPkOrdmt()});
		}
	}
	
	public List<DoctorMaintainVo> QueryListByCode(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map.get("pycode")!=null){
			map.put("pycode",map.get("pycode").toString().toUpperCase());
		}
		List<DoctorMaintainVo> list=doctorMaintainMapper.QueryListByCode(map);
		return list;
	}
	
	public BdOrdMt Assignment(DoctorMaintainVo doctorMaintainVo){
		BdOrdMt bd=new BdOrdMt();
		if(doctorMaintainVo.getPkOrdmt()!=null && doctorMaintainVo.getPkOrdmt()!=""){
			bd.setPkOrdmt(doctorMaintainVo.getPkOrdmt());
		}
		bd.setPkOrg(doctorMaintainVo.getPkOrg());
		bd.setPkOrd(doctorMaintainVo.getPkOrd());
		bd.setFlagPd(doctorMaintainVo.getFlagPd());
		bd.setPkDept(doctorMaintainVo.getPkDept());
		bd.setDosage(doctorMaintainVo.getDosage());
		bd.setPkUnitDos(doctorMaintainVo.getPkUnitDos());
		bd.setCodeSupply(doctorMaintainVo.getCodeSupply());
		bd.setCodeFreq(doctorMaintainVo.getCodeFreq());
		bd.setQuan(doctorMaintainVo.getQuan());
		bd.setPkUnit(doctorMaintainVo.getPkUnit());
		bd.setPrice(doctorMaintainVo.getPrice());
		bd.setNote(doctorMaintainVo.getNote());
		bd.setFlagStop(doctorMaintainVo.getFlagStop());
		return bd;
	}
}
