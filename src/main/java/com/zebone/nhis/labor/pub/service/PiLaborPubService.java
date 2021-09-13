package com.zebone.nhis.labor.pub.service;

import com.zebone.nhis.common.module.labor.nis.PiLabor;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.labor.pub.dao.PiLaborPubMapper;
import com.zebone.nhis.labor.pub.vo.LabPatiCardVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 孕妇档案公共接口
 * @author yangxue
 *
 */
@Service
public class PiLaborPubService {
	
	@Resource
    private PiLaborPubMapper piLaborPubMapper;
    /**
     * 根据患者或孕妇档案主键查询患者信息
     * @param param(pk_pi,pk_pilabor)
     * @param user
     * @return
     */
    public List<PiLabor> queryPiLaborByPk(Map<String,Object> param,IUser user){
    	if(param == null){
    		param = new HashMap<String,Object>();
    	}
    	if(CommonUtils.isNull(param.get("pkOrg"))){
    		param.put("pkOrg", ((User)user).getPkOrg());
    	}
    	
    	// 添加<10月的校验 ,过滤出本次孕妇档案
//    	Calendar c = Calendar.getInstance();
//    	c.add(Calendar.MONTH, -10); // 目前时间  - 10个月    
//    	param.put("createTime", c.getTime());
    	String dateStr = DateUtils.addDate(new Date(), -10, 2, "yyyyMMddHHmmss"); 
    	param.put("createTime", dateStr);
    	
    	return piLaborPubMapper.queryPiLaborByPk(param);
    }
    
    /**
     * 根据不同条件查询患者信息
     * @param param(codeHp,codePv,namePi,idNo)
     * @param user
     * @return
     */
    public List<PiLabor> queryPiLaborByCon(Map<String,Object> param,IUser user){
    	if(param == null){
    		param = new HashMap<String,Object>();
    	}
    	if(CommonUtils.isNull(param.get("pkOrg"))){
    		param.put("pkOrg", ((User)user).getPkOrg());
    	}
    	
//    	Calendar c = Calendar.getInstance();
//    	c.add(Calendar.MONTH, -10); // 目前时间  - 10个月    
//    	param.put("createTime", c.getTime());
    	return piLaborPubMapper.queryPiLabor(param);
    }
    
    /**
     * 查询患者信息
     * @param param{pkPv，pkPi}
     * @param user
     * @return
     */
    public Map<String,Object> queryPatiInfo(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	
    	// 添加<10月的校验 ,过滤出本次孕妇档案
//    	Calendar c = Calendar.getInstance();
//    	c.add(Calendar.MONTH, -10); // 目前时间  - 10个月    
//    	paramMap.put("createTime", c.getTime());
//    	String dateStr = DateUtils.addDate(new Date(), -10, 2, "yyyyMMddHHmmss");
//    	paramMap.put("createTime", dateStr);
    	List<Map<String,Object>> pvlabors = piLaborPubMapper.queryPatiInfo(paramMap);
    	if(pvlabors != null && pvlabors.size() >0)
    		return pvlabors.get(0);
    	return null;
    }
    
    /**
	 * 查询病区床位及患者列表
	 * @param param{pkOrg,pkDeptNs}
	 * @param user
	 * @return
	 */
	public List<LabPatiCardVo> queryPatiBedList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		// 添加<10月的校验 ,过滤出本次孕妇档案
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MONTH, -10); // 目前时间  - 10个月   
//		paramMap.put("createTime", c.getTime()); 
		String dateStr = DateUtils.addDate(new Date(), -10, 2, "yyyyMMddHHmmss"); 
		paramMap.put("createTime", dateStr);
		//如果传入的科室是产房医生所在科室，则根据业务线配置对应转换为其所对应产房病区
		//仅仅支持产房的临床科室与护理单元为1对1的情况
//		List<Map<String,Object>> dulist = piLaborPubMapper.queryDeptBuByDept(paramMap);
//		if(dulist!=null&&dulist.size()>0){
//			Map<String,Object> buParam = dulist.get(0);
//			//查询对应业务线中设置的对应病区
//			List<Map<String,Object>> deptList = piLaborPubMapper.queryDeptByDeptBu(buParam);
//			if(deptList!=null&&deptList.size()>0){
//				paramMap.put("pkDeptNs", deptList.get(0).get("pkDept"));
//			}
//		}
		List<LabPatiCardVo> cardlist = piLaborPubMapper.getBedInfo(paramMap);
		return cardlist;
	}
	
	/**
	 * 根据病区获取在院患者列表
	 * @param param
	 * @param user
	 * @return{name_pi,name_bed,code_bed,pv_code,pk_pv}
	 */
	public List<Map<String,Object>> queryLaborPatiList(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		return piLaborPubMapper.getLabPatiList(paramMap);
	}
	/**
	 * 查询产房总人数及婴儿数量
	 * @param param - pkDeptNs
	 * @param user
	 * @return
	 */
	public Map<String,Integer> queryLaborPeopleNum(String param,IUser user){
		String pk_dept = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pk_dept)){
			throw new BusException("未获取到要查询的科室主键！");
		}
		String sql = " select count(1) as total from pv_labor where flag_in = '1' and eu_status = '1' and pk_dept = ? ";
		Integer total = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pk_dept});
		String sql1="select count(1) as newborn from pv_infant where pk_pv in (select pk_pv from pv_labor where flag_in = '1' and eu_status = '1' and pk_dept = ?)";
		Integer newborn = DataBaseHelper.queryForScalar(sql1, Integer.class, new Object[]{pk_dept});
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("total", total);
		map.put("newborn", newborn);
		return map;
	}

	/**
	 * 根据患者主键获取名下登记的婴儿信息
	 * @param param{pkPv}
	 * @param user
	 */
	public List<PvEncounter> queryPvInfo(String param, IUser user) {
		Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkPv = paramMap.get("pkPv");
		String queSql = "SELECT pi.PK_BED_AN, pv.*  FROM PV_ENCOUNTER pv LEFT JOIN PV_IP pi ON pv.pk_pv = pi.pk_pv " +
						"WHERE pv.pk_pv IN ( SELECT PK_PV_INFANT FROM pv_infant WHERE pk_pv = ? ) AND pv.eu_status = '1'";
		List<PvEncounter> pvEncounters = DataBaseHelper.queryForList(queSql,PvEncounter.class,pkPv);
		if(pvEncounters != null){
			return pvEncounters;
		}
		return null;
	}


	/**
	 * 获取本产科已被婴儿陪护占用的床位
	 * @param param
	 * @param user
	 * @return
	 */
	public String queryOccupyBed(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkDept = paramMap.get("pkDept");
		String queSql = "select PK_BED_AN from PV_IP where PK_BED_AN is not null and PK_DEPT_ADMIT = ? GROUP BY PK_BED_AN";
		String pkBedStr = "'";
		List<PvIp> pkBeds = DataBaseHelper.queryForList(queSql,PvIp.class,pkDept);
		if(pkBeds.size() > 0){
			for(int i=0; i<pkBeds.size(); i++){
				if(i < pkBeds.size() - 1){
					pkBedStr += pkBeds.get(i).getPkBedAn()+"','";
				}else{
					pkBedStr += pkBeds.get(i).getPkBedAn()+"'";
				}
			}
		}else{
			pkBedStr = "";
		}
		return pkBedStr;
	}
}
