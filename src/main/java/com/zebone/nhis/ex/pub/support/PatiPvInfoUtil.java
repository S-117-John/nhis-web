package com.zebone.nhis.ex.pub.support;


import java.util.Map;

import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class PatiPvInfoUtil {
	/**
	 * 根据当前科室，就诊主键获取患者当前就诊的科室信息
	 * @return{pkDept,pkDeptNs}
	 */
//    public static Map<String,Object> getPvDeptInfo(String pkDept,String pkPv){
//    	if(CommonUtils.isEmptyString(pkDept)||CommonUtils.isEmptyString(pkPv))
//    		return null;
//    	//获取科室类型
//    	Map<String,Object> map = new HashMap<String,Object>();
//    	if(isDeptCF(pkDept)){
//    		//就诊科室和病区从产房就诊表中获取
//    		map = DataBaseHelper.queryForMap("select pk_dept,pk_dept_ns,bed_no from pv_labor where flag_in = '1' and eu_status = '1' and pk_pv = ? ", new Object[]{pkPv});
//    	    map.put("flagCF", "1");
//    	}else{
//    		//就诊科室和病区从就诊表中获取
//    		map = DataBaseHelper.queryForMap("select pk_dept,pk_dept_ns from pv_encounter where flag_in = '1' and eu_status = '1' and pk_pv = ? ", new Object[]{pkPv});
//    	    map.put("flagCF", "0");
//    	}
//    	return map;
//    }
    /**
     * 查询产房类型的部门列表
     * @return
     */
//    public static List<String> getDeptByCFType(String pk_org){
//    	String sql = " select pk_dept from bd_ou_dept where del_flag = '0' and dt_depttype  = '"+IDictCodeConst.DT_DEPTTYPE_CF+"' and pk_org = ? ";
//        List<Map<String,Object>> list =  DataBaseHelper.queryForList(sql, new Object[]{pk_org});
//        if(list!=null&&list.size()>0){
//        	List<String> result = new ArrayList<String>();
//        	for(Map<String,Object> map : list){
//        		result.add(CommonUtils.getString(map.get("pkDept")));
//        	}
//        	return result;
//        }
//        return null;
//    }
    /**
     * 判断科室是否是产房
     * @param pk_dept
     * @return
     */
    public static boolean isDeptCF(String pk_dept){
    	Map<String,Object> deptMap = DataBaseHelper.queryForMap("select dt_depttype from bd_ou_dept where pk_dept = ? ", new Object[]{pk_dept});
    	if(deptMap!=null&&deptMap.get("dtDepttype")!=null)
    		return IDictCodeConst.DT_DEPTTYPE_CF.equals(deptMap.get("dtDepttype").toString());
    	else
    		return false;
    }
    /**
     * 分类患者（将在产房的患者与不在产房的患者区分出来）
     * @return
     */
//    public static Map<String,Object> getDiffPvList(List<String> pvList){
//    	if(pvList == null || pvList.size() <=0) return null;
//    	 String sql = "select pk_dept,pk_pv from pv_labor where flag_in = '1' and eu_status = '1' and pk_pv in ("+listToStr(pvList).substring(0, listToStr(pvList).length()-1)+") ";
//    	 List<Map<String,Object>> list = DataBaseHelper.queryForList(sql);
//    	 Map<String,Object> result = new HashMap<String,Object>();
//    	 if(list == null || list.size() <= 0){
//    		 result.put("pv", pvList);
//    		 return result;
//    	 }
//    		
//    	 List<String> labpv = new ArrayList<String>();
//    	 //Set<String> pv = new HashSet<String>();
//    	
//    	for(Map<String,Object> map :list){
//            String pkPv = CommonUtils.getString(map.get("pkPv"));
//            labpv.add(pkPv);
//            for(String pk:pvList){
//              if(pkPv.equals(pk)){//如果相等
//            	  pvList.remove(pk);
//            	  break;
//              }
//        	}
//    	 }
//    	 result.put("pv", pvList);//不在产房的患者
//    	 result.put("labpv", labpv);//在产房的患者
//    	 result.put("pkDept", list.get(0).get("pkDept"));//产房的主键
//    	 return result;
//    }
    
//    public static String listToStr(List<String> pvList){
//    	if(pvList!=null && pvList.size() > 0){
//    		String strpk = "";
//    		for(String pk:pvList){
//    			strpk = strpk+"'"+pk+"',";
//    		}
//    		return strpk;
//    	}
//    	return null;
//    }
}
