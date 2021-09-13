package com.zebone.nhis.ex.nis.pi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.pi.dao.PvInfantMapper;
import com.zebone.nhis.ex.pub.dao.AdtPubMapper;
import com.zebone.nhis.ex.pub.support.ExListSortByOrdUtil;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.pi.pub.service.PiPubService;
import com.zebone.nhis.pv.pub.service.PvAdtPubService;
import com.zebone.nhis.pv.pub.vo.AdtRegParam;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 婴儿转出入院业务处理类
 * @author yangxue
 *
 */
@Service
public class PvInfantAdtService {
	@Resource
	private AdtPubMapper adtPubMapper;
	@Resource
	private PvAdtPubService pvAdtPubService;
	@Resource
	private PiPubService piPubService;
	@Resource
	private PvInfantMapper pvInfantMapper;	
	
	/***
     * 转入院需要校验的数据
     * @param param{pkPv，pkInfant，sortNo}
     * @return
     */
    public Map<String,Object> getHospInVerfyData(String param,IUser u){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	Map<String,Object> infant = pvInfantMapper.queryInfantAndPV(paramMap);//婴儿及就诊信息
    	paramMap.put("pkDept", infant.get("pkDept"));
    	paramMap.put("pkDeptOcc", infant.get("pkDeptNs"));
    	//paramMap.put("pkDeptNs", infant.get("pkDeptNs"));
    	paramMap.put("sortNo", null);
    	List<Map<String,Object>> ordData = adtPubMapper.queryOrdByPkPv(paramMap);//未停未作废医嘱
    	List<Map<String,Object>> risData = adtPubMapper.queryRisByPkPv(paramMap);//未执行执行单
    	List<ExlistPubVo> exListData = adtPubMapper.queryExlistByPv(paramMap);//未执行执行单
    	new ExListSortByOrdUtil().ordGroup(exListData);
    	Map<String,Object> result = new HashMap<String,Object>();
    	if (!isEmpty(ordData)) {
    		result.put("ordData", ordData);
		}
    	if (!isEmpty(risData)) {
    		result.put("risData", risData);
		}
    	if (!isEmpty(exListData)) {
    		result.put("exListData", exListData);
		}
    	result.put("infData", infant);
    	return result;
    }
    /**
     * 保存转入院的入院登记信息
     * @param param{pkPv,pkInfant,pkPiInfant,pkPvInfant,bedNo,reasonAdt,adtParam中的内容}
     * @param user
     */
    public void saveHospInData(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	paramMap.put("dateNow", new Date());
    	paramMap.put("pkEmp", ((User)user).getPkEmp());
    	paramMap.put("nameEmp", ((User)user).getNameEmp());
    	//完成患者注册
    	AdtRegParam adt = JsonUtil.readValue(param, AdtRegParam.class);
    	//调用入院登记接口
    	adt.setPkPi(CommonUtils.getString(paramMap.get("pkPiInfant")));
    	pvAdtPubService.saveAdtReg(adt);
    	//更新婴儿信息表
    	String infant_sql = "update pv_infant set eu_status_adt ='1',reason_adt =:reasonAdt,date_adt =:dateNow where pk_infant = :pkInfant ";
    	DataBaseHelper.update(infant_sql,paramMap);
    	//更新母亲住院信息中的婴儿相关字段
    	String sqln = "select quan_infant from pv_ip where pk_pv = :pkPv ";
		 List<Map<String,Object>> listn =DataBaseHelper.queryForList(sqln, paramMap);
		 if(listn!=null&&listn.size()>0&&CommonUtils.getInteger(listn.get(0).get("quanInfant"))>1){
			 DataBaseHelper.update("update pv_ip set quan_infant = quan_infant - 1 where pk_pv = :pkPv ", paramMap);
		 }else{
			 DataBaseHelper.update("update pv_ip set flag_infant = 0, quan_infant = 0 where pk_pv = :pkPv ", paramMap);
		 }
    	//更新婴儿就诊床位信息
    	String pv_bed_sql = "update pv_bed set date_end = :dateNow,pk_emp_end = :pkEmp,name_emp_end = :nameEmp  "
    			+ " where bedno = :bedNo and flag_maj ='0' and eu_hold = '0' and pk_pv = :pkPv  and date_end is null";
    	DataBaseHelper.update(pv_bed_sql,paramMap);
    	paramMap.put("pkDeptNs", ((User)user).getPkDept());
    	//更新床位表信息
    	String bed_sql = "update bd_res_bed set eu_status='01',flag_ocupy='0',pk_dept_used = null,pk_pi= null"
    				+ " ,flag_active = (case when '09' = dt_bedtype then '0' else flag_active end)"
    				+ " ,del_flag = (case when '09' = dt_bedtype then '1' else del_flag end) where code=:bedNo and pk_ward =:pkDeptNs";
    	DataBaseHelper.update(bed_sql,paramMap);
    }
    /**
     * 保存转出院信息
     * @param param{pkPvM,pkPi,dateEnd,dtOutcomes,dtOuttype,pkPv,flagCheck}
     * @param user
     */
    public void saveHospOutData(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	//调用办理出院接口
    	pvAdtPubService.outHospital(param, user);
    	//更新婴儿信息
    	 DataBaseHelper.update("update pv_infant set eu_status_adt = '2' where pk_pi_infant = :pkPi ", paramMap);
    	//更新婴儿数量
    	DataBaseHelper.update("update pv_ip set flag_infant = 1, quan_infant = quan_infant + 1  where pk_pv = :pkPvM ", paramMap);    	 
    	
    	//查询患者之前的就诊号
    	List<Map<String, Object>> list = DataBaseHelper.queryForList("select pk_pv from pv_encounter where pk_pi =:pkPi and"
    					+ " pk_dept_ns = :pkDeptTo order by ts desc", paramMap);
    	paramMap.put("pkPv", list.get(0).get("pkPv"));
    	//查询婴儿之前病区的床位
    	Map<String, Object> map = DataBaseHelper.queryForMap("select bedno from pv_bed where pk_pv = :pkPv and"
    			+ " pk_dept_ns= :pkDeptTo", paramMap);	 
    	paramMap.put("bedNo", map.get("bedno"));
    	
    	//new Object[]{pvLaborRec.getPkPv(),((User)user).getPkOrg()}
    	//更新床位表信息
    	 DataBaseHelper.update("update pv_bed set date_end = null , pk_emp_end = null,name_emp_end = null"
    	 		+ " where bedno = :bedNo and flag_maj ='0' and eu_hold = '0' and pk_pv = :pkPv ", paramMap);
    	 
    	 DataBaseHelper.update("update bd_res_bed set eu_status='02',flag_ocupy='1',pk_dept_used =:pkDeptTo, "
    	 		+ "pk_pi= :pkPi, flag_active = (case when '09' = dt_bedtype then '1' else flag_active end), del_flag = "
    	 		+ "(case when '09' = dt_bedtype then '0' else del_flag end) where code=:bedNo and pk_ward =:pkDeptTo", paramMap);
    }
    /**
     * 根据婴儿就诊信息查询婴儿
     * @param param--pkPv
     * @param user
     * @return
     */
    public Map<String,Object> queryInfantByPv(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	if(paramMap == null||paramMap.get("pkPv") == null)
    		throw new BusException("未获取患者就诊主键！");
    	return pvInfantMapper.queryInfantByPv(paramMap);
    }
    /**
     * 校验是否为空
     * @param obj
     * @return
     */
    private boolean isEmpty(Object obj){
    	if(obj == null ) return true;
    	return false;
    }
    
   
}
