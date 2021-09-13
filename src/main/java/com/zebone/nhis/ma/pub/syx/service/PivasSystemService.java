package com.zebone.nhis.ma.pub.syx.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.ma.pub.syx.dao.PivasSystemMapper;
import com.zebone.nhis.ma.pub.syx.vo.Torders;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class PivasSystemService {
	
	@Autowired
	private PivasSystemMapper pivasSystemMapper;
	
	
	public void saveTorders(List<Torders> tordList){
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(Torders.class), tordList);
	}
	
	public List<Map<String,Object>> qryPivasCgItem(){
		return pivasSystemMapper.qryPivasCgItem();
	}
	
	public void updatePivas(List<String> pkList){
		String pkStr = "";
		int i = 0;
		for (String pkcgip : pkList) {
			if (i == 0) {
				pkStr += "'" + pkcgip.trim() + "'";
			} else {
				pkStr += ",'" + pkcgip.trim() + "'";
			}
			i++;
		}
		DataBaseHelper.execute(
				"update tAccountList_Pivas SET ReadFlag='1', ReadDateTime=? WHERE AccountListID in ("+pkStr+")"
				, new Date());
	}
	
	/**
	 * 获取已经入仓的静配数据返回前台，做提示用途
	 * @param args{args[0]:"医嘱序号的集合",args[1]:"医嘱序号+计划执行时间"}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> checkPivasOut(Object...args){
		List<String> ordsnKeys=(List<String>)args[0];
		List<String> ordsnGroups=(List<String>)args[1];
		//为节省响应时间，只将ordsn传入接口中，通过组装好的数据与返回值做对比
		List<Map<String,Object>> qryPivasInfo=pivasSystemMapper.checkPivasOut(ordsnKeys);
		List<Map<String,Object>> resPivas=new ArrayList<Map<String,Object>>();
		for (String ordsnPk : ordsnGroups) {
			for (Map<String, Object> map : qryPivasInfo) {
				if(ordsnPk.equals(map.get("ordsnPk"))){
					resPivas.add(map);
					break;
				}
			}
		}
		return resPivas;
	}

	/**
	 * 住院静配退药接口，更新取消标志
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public void updatePivasOut(Object...args){
		List<Map<String,Object>> exOrdOccList=(List<Map<String,Object>>)args[0];
		StringBuffer sql=new StringBuffer();
		sql.append("update tOrders set CancelFlag='1'");
		sql.append(" ,CancelTime=:dateNow ");
		sql.append(" where ExecDAListID=:ordsn and CONVERT(varchar(10), ExecDate, 23)=:datePlan and TakingMedicineTime=:timePlan");
		DataBaseHelper.batchUpdate(sql.toString(),exOrdOccList);
	}
}
