package com.zebone.nhis.ex.nis.pub.service;

import com.zebone.nhis.cn.ipdw.vo.CnOrderVO;
import com.zebone.nhis.common.module.base.transcode.SysApplog;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.ex.nis.ns.vo.ExStOccVo;
import com.zebone.nhis.ex.nis.pub.dao.NsPubMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 护理菜单公共类
 * @author yangxue
 *
 */
@Service
public class NsPubService {
	  @Resource
	  private NsPubMapper nsPubMapper;
     
     /**
  	 * 查询患者住院诊疗信息
  	 * @param param{pkPv}
  	 * @param user
  	 * @return 患者信息,转科记录,床位变化记录,手术记录
  	 * 
  	 */
      public Map<String,Object> queryPvIpInfo(String param,IUser user){
    	  Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	  Map<String,Object>  result = new HashMap<String,Object>();
    	  Map<String,Object> patientInfo = nsPubMapper.queryPatiInfoByPV(paramMap);
    	  List<Map<String,Object>> listOps=new ArrayList<Map<String,Object>>();
    	  Map<String,Object> patientInfoNew = new HashMap<String,Object>();
    	  
    	  result.put("adt", nsPubMapper.queryPatiAdtByPV(paramMap));
    	  result.put("beds", nsPubMapper.queryPatiBedByPV(paramMap));
    	  
    	  List<String> codes=new ArrayList<>();
    	  //出院
    	  String outhos=ApplicationUtils.getPropertyValue("emr.cn.ordCode.outhos", "107222");
    	  //手术
    	  String ops=ApplicationUtils.getPropertyValue("emr.cn.ordCode.ops", "107303");
    	  //死亡
    	  String death=ApplicationUtils.getPropertyValue("emr.cn.ordCode.death", "107304");
    	  codes.add(outhos);
    	  codes.add(ops);
    	  codes.add(death);
    	  Object dateEnd=null;
    	  for (String key : patientInfo.keySet()) { 
			  if(key.equals("dateEnd")){
				  dateEnd=patientInfo.get(key);
				  continue;
			  }
			  patientInfoNew.put(key, patientInfo.get(key));
	 
	      } 
    	  List<Map<String,Object>> opso = nsPubMapper.queryPatiOpByPV(paramMap);
    	  
    	  result.put("opso", opso);  
    	  List<CnOrder> list = nsPubMapper.queryPatiOrdsByPv(paramMap.get("pkPv").toString(),codes);
    	  boolean bDeath=false; 
    	  if(list!=null&&list.size()>0){
    		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		  
			  for (CnOrder order : list) {
				  if(order.getCodeOrd()==null) continue;
				  //出院医嘱/术后医嘱/死亡医嘱
				  if(outhos.indexOf(order.getCodeOrd())>=0){
					  patientInfoNew.put("dateEnd", sdf.format(order.getDateStart()));
				  }else if(ops.indexOf(order.getCodeOrd())>=0){
					  Map<String,Object> opsMap=new HashMap<String,Object>();
					  opsMap.put("datePlan", sdf.format(order.getDateStart()));
					  listOps.add(opsMap);
				  }else if(death.indexOf(order.getCodeOrd())>=0){
					  bDeath=true;
					  result.put("dateDeath", sdf.format(order.getDateStart()));
				  }
			  }
    	  }
    	  if(!patientInfoNew.containsKey("dateEnd")&&!bDeath){
    		  patientInfoNew.put("dateEnd", dateEnd);
    	  }
    	  result.put("patientInfo", patientInfoNew);
    	  result.put("ops", listOps);
    	  
    	  return result;
      }
      
      /**
       * 核对时更新营养等级，护理等级，病情等级
       * @param ordcode
       * @param pk_pv
       */
      public void updatePvIpInfo(List<String> clist,List<String> stoplist,List<String> eralist){
    	  if (clist == null && stoplist == null && eralist == null)
  			return ;
  		//获取护理等级
//  		String istts1 = ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_T", "");
//  		String istts2 = ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_I", "");
//  		String istts3 = ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_II", "");
//  		String istts4 = ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_III", "");
    	 
    	//2018-12-12 获取护理等级: 修改为获取机构参数
		String istts1 = ApplicationUtils.getSysparam("EX0010", true);//特
		String istts2 = ApplicationUtils.getSysparam("EX0011", true);//一
		String istts3 = ApplicationUtils.getSysparam("EX0012", true);//二
		String istts4 = ApplicationUtils.getSysparam("EX0013", true);//三
  		
  		//获取病情等级
//  	String isser1 = ApplicationUtils.getPropertyValue("ORDCODE_BQDJ_BZ", "");
//  	String isser2 = ApplicationUtils.getPropertyValue("ORDCODE_BQDJ_BW", "");
  		//2018-12-12  获取病情等级: 修改为获取机构参数
  		String isser1 = ApplicationUtils.getSysparam("EX0014", true);//病重
  		String isser2 = ApplicationUtils.getSysparam("EX0015", true);//病危
  		
  	   //获取营养等级
//  		List<String> diet_arr = new ArrayList<String>();
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_PUSHI", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_JINSHI", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_DIYAN", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_TNB", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_DIZHI", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_DIDB", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_GAODB", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_WZ", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_SZ", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_LZ", ""));
//  		diet_arr.add(ApplicationUtils.getPropertyValue("ORDCODE_YYDJ_BLZ", ""));
  		
  		List<String> code_arr = new ArrayList<String>();
  		code_arr.add(istts1);
  		code_arr.add(istts2);
  		code_arr.add(istts3);
  		code_arr.add(istts4);
  		code_arr.add(isser1);
  		code_arr.add(isser2);
  		
  		List<CnOrder> list = new ArrayList<CnOrder>();
  		List<CnOrderVO> stop_list = new ArrayList<CnOrderVO>();
  		List<CnOrder> earse_list = new ArrayList<CnOrder>();
  		//营养等级按医嘱类型处理
  		//List<CnOrder> diet_list = new ArrayList<CnOrder>();
  		//List<CnOrder> diet_stop_list = new ArrayList<CnOrder>();
  		//List<CnOrder> diet_earse_list = new ArrayList<CnOrder>();
  		if(stoplist!=null&&stoplist.size()>0){
  			Map<String,Object> map = new HashMap<String,Object>();
  			map.put("pkCnord", stoplist);
  			map.put("code_arr",code_arr);
  			StringBuilder sql_stop = new StringBuilder(" select ord.code_ord,ord.pk_cnord,ord.pk_pv,pi.dt_level_dise  " );
  			sql_stop.append(" from cn_order ord  inner join pv_ip pi on ord.pk_pv = pi.pk_pv  ");
			sql_stop.append(" where ord.pk_cnord in (:pkCnord) ");
  			sql_stop.append(" and ord.code_ord in (:code_arr) and ord.eu_always = '0' and ord.flag_stop_chk = '1' ");
  			sql_stop.append(" order by  ord.date_start asc   ");
  			stop_list = DataBaseHelper.queryForList(sql_stop.toString(), CnOrderVO.class, map);
  			//直接更新营养等级
  			updateDietInfoByCon(map,"4");//停止
  		}
  		
  		if(eralist!=null&&eralist.size()>0){
  			String sql = " select ord.code_ord,ord.pk_cnord,ord.pk_pv  " +
  	  				" from cn_order ord where ord.pk_cnord in (:pkCnord) and ord.code_ord in (:yydj) and ord.eu_always = '0' and ord.flag_erase_chk = '1' " +
  	  				" order by  ord.date_start asc   ";
  			Map<String,Object> map = new HashMap<String,Object>();
  			map.put("pkCnord", eralist);
  			map.put("yydj", code_arr);
  			earse_list = DataBaseHelper.queryForList(sql,CnOrder.class, map);
  			 //直接更新营养等级
  			updateDietInfoByCon(map,"9");//作废
  		}
  		
  		if(clist!=null&&clist.size()>0){
  			StringBuilder sql = new StringBuilder(" select ord.code_ord,ord.pk_cnord,ord.pk_pv  ");
  	  		sql.append(" from cn_order ord where ord.pk_cnord in (:pkCnord) and  ord.code_ord in (:code_arr) and ord.eu_always = '0' and ord.flag_stop_chk = '0' ");
  	  		sql.append(" order by  ord.date_start asc   ");
  			Map<String,Object> map = new HashMap<String,Object>();
  			map.put("pkCnord", clist);
  			map.put("code_arr", code_arr);
  			list = DataBaseHelper.queryForList(sql.toString(),CnOrder.class, map);
  		   //直接更新营养等级
  			updateDietInfoByCon(map,"1");//新开
  		}
  		
  		
  		String bzsql = " set dt_level_dise = '"+IDictCodeConst.DT_BQDJ_BZ+"'";
  		String bwsql = " set dt_level_dise = '"+IDictCodeConst.DT_BQDJ_BW+"'";
  		String wdsql = " set dt_level_dise = '"+IDictCodeConst.DT_BQDJ_WD+"'";
  		
  		//停医嘱，修改病情等级 - 同时修改护理等级
  		if(null != stop_list && stop_list.size() > 0){
  			for(int i = 0; i < stop_list.size(); i++){
  				String code_ord = stop_list.get(i).getCodeOrd();
  				String pk_pv = stop_list.get(i).getPkPv();
  				String dtLevelDise = stop_list.get(i).getDtLevelDise();
  				//更新病情等级
  				if(isser1.equals(code_ord)||isser2.equals(code_ord)){//更新病重或者病危(当前核停病情医嘱与病人当前病情等级一致时在更新病情等级为稳定)
  					if((IDictCodeConst.DT_BQDJ_BZ.equals(dtLevelDise) && isser1.equals(code_ord)) || (IDictCodeConst.DT_BQDJ_BW.equals(dtLevelDise) && isser2.equals(code_ord))){
						updatePatiSerInfoByCon(true,wdsql,pk_pv,isser1,isser2);
					}
  				}else if (istts1.equals(code_ord) || istts2.equals(code_ord)
  	  	  			   || istts3.equals(code_ord) || istts4.equals(code_ord)) {//更新护理等级
  	  	  			updatePatiNurseInfoByCon(true,"",pk_pv,istts1,istts2,istts3,istts4);
  	  	  		}
  			}
  		}
  		
  		//作废的情况将病情等级更新为一般
  		if(null != earse_list && earse_list.size() > 0){
  			for(int i = 0; i < earse_list.size(); i++){
  				String code_ord = earse_list.get(i).getCodeOrd();
  				String pk_pv = earse_list.get(i).getPkPv();
  				//更新病情等级
  				if(isser1.equals(code_ord)||isser2.equals(code_ord)){//更新病重或者病危
  					updatePatiSerInfoByCon(true,wdsql,pk_pv,isser1,isser1);
  				}else 
  					//更新护理等级
  					if (istts1.equals(code_ord) || istts2.equals(code_ord)
  							|| istts3.equals(code_ord) || istts4.equals(code_ord)) {
  						updatePatiNurseInfoByCon(true,"",pk_pv,istts1,istts2,istts3,istts4);
  					}
  			}
  		}
  		
  		//新医嘱核对根据参数代码获取参数值 
  		for (int i = 0; i < list.size(); i++) {
  			String code_ord = list.get(i).getCodeOrd();
  			String pk_pv = list.get(i).getPkPv();
  			if (istts1.equals(code_ord) || istts2.equals(code_ord)
  					|| istts3.equals(code_ord) || istts4.equals(code_ord)) {
  				//更新护理等级
  				updatePatiNurseInfoByCon(false,code_ord,pk_pv,istts1,istts2,istts3,istts4);
  			} else if(isser1.equals(code_ord)||isser2.equals(code_ord)){//更新病重或者病危
  				if(code_ord.equals(isser1)){//更新为病重
  					//updatePatiSerInfoByCon(wdsql,pk_pv);
  					updatePatiSerInfoByCon(false,bzsql,pk_pv,isser1,isser1);
  				}else if(code_ord.equals(isser2)){//更新为病危
  					//updatePatiSerInfoByCon(wdsql,pk_pv);
  					updatePatiSerInfoByCon(false,bwsql,pk_pv,isser1,isser1);
  				}
  			}
  		}
  		
      }
      
      /**
  	 * 更新营养等级
  	 * 
  	 * @param ordStatus 医嘱状态
  	 * @param pk_pv
  	 * @param dao
  	 * @throws DAOException
  	 */
  	private void updateDietInfoByCon(Map<String,Object> paramMap,String ordStatus)  {
  	    if("1".equals(ordStatus)){//新开医嘱
  	       //查询本次核对医嘱饮食类医嘱信息
  	  		StringBuilder sql = new StringBuilder("select ord.code_ord, ord.pk_pv,ord.pk_cnord  from cn_order  ord ");
  	  		sql.append(" where ord.code_ordtype='13' and ord.pk_cnord in (:pkCnord) order by ord.ordsn desc");
  	  		List<CnOrder>  ordlist = DataBaseHelper.queryForList(sql.toString(), CnOrder.class, paramMap);
  	  		if(ordlist==null||ordlist.size()<=0)
  	  			return;
  	  		Map<String,String> pvMap = new HashMap<String,String>();
  	  		List<String> updateList = new ArrayList<String>();
  	  		for(CnOrder ord:ordlist){
  	  	     boolean hasFlag = false;
  	  		 //遍历核对医嘱的患者集合，存在则不再添加
  	  		 for (Map.Entry<String, String> entry : pvMap.entrySet()) {
  	  			 if(ord.getPkPv().equals(entry.getKey())){
  	  				hasFlag = true;
  	  				break;
  	  			 }
  	  		}
             if(!hasFlag){//不存在，则添加sql并添加pvMap记录
            	 pvMap.put(ord.getPkPv(), ord.getCodeOrd());
            	 updateList.add("update pv_ip  set dt_level_nutr ='"+ord.getCodeOrd()+"' where pk_pv = '"+ord.getPkPv()+"'");
             } 
  	  		}
  			DataBaseHelper.batchUpdate(updateList.toArray(new String[0]));
  		}else{//作废或停止医嘱
  		   //查询不在本次核对医嘱内的含有效饮食类医嘱的患者及饮食类医嘱信息
  	  		StringBuilder sql = new StringBuilder("select ord.code_ord, ord.pk_pv,ord.pk_cnord  from cn_order  ord ");
  	  		sql.append(" inner join (select pk_pv  from CN_ORDER where CODE_ORDTYPE='13' and PK_CNORD in (:pkCnord)) temp on temp.pk_pv = ord.pk_pv ");
  	  		sql.append(" where ord.code_ordtype='13' and ord.eu_status_ord = '3' and ord.pk_cnord  not in (:pkCnord) order by ord.ordsn desc");
  	  		List<CnOrder>  ordlist = DataBaseHelper.queryForList(sql.toString(), CnOrder.class, paramMap);
  	  	    Map<String,String> pvMap = new HashMap<String,String>();
	  		List<String> updateList = new ArrayList<String>();
	  		if(ordlist==null||ordlist.size()<=0){
	  			String sqlupdate = "update pv_ip set dt_level_nutr = '' where pk_pv in (select pk_pv from cn_order where code_ordtype='13' and pk_cnord in (:pkCnord))";
	  	  		DataBaseHelper.update(sqlupdate,paramMap);
	  	  		return;
	  		}
	  		for(CnOrder ord:ordlist){
	  	     boolean hasFlag = false;
	  		 //遍历核对医嘱的患者集合，存在则不再添加
	  		 for (Map.Entry<String, String> entry : pvMap.entrySet()) {
	  			 if(ord.getPkPv().equals(entry.getKey())){
	  				hasFlag = true;
	  				break;
	  			 }
	  		 }
             if(!hasFlag){//不存在，则添加sql并添加pvMap记录
        	   pvMap.put(ord.getPkPv(), ord.getCodeOrd());
        	   updateList.add("update pv_ip  set dt_level_nutr ='"+ord.getCodeOrd()+"' where pk_pv = '"+ord.getPkPv()+"'");
             } 
	  	   }
			DataBaseHelper.batchUpdate(updateList.toArray(new String[0]));//更新存在有效医嘱的饮食等级
			StringBuilder sqlpvAll = new StringBuilder("select distinct pk_pv from CN_ORDER where CODE_ORDTYPE='13' and PK_CNORD in (:pkCnord)");
  	  		List<CnOrder>  allPvlist = DataBaseHelper.queryForList(sqlpvAll.toString(), CnOrder.class, paramMap);
  	     	if(allPvlist==null||allPvlist.size()<=0)
  			return;
  	     	updateList = new ArrayList<String>();
  	     	for(CnOrder ord:allPvlist){
  		  	     boolean hasFlag = false;
  		  		 //遍历核对医嘱的患者集合，存在则不再添加
  		  		 for (Map.Entry<String, String> entry : pvMap.entrySet()) {
  		  			 if(ord.getPkPv().equals(entry.getKey())){
  		  				hasFlag = true;
  		  				break;
  		  			 }
  		  		 }
  	             if(!hasFlag){//不存在于存在有效医嘱的患者集合，则添加至更新为空的集合中
  	        	   updateList.add("update pv_ip  set dt_level_nutr ='' where pk_pv = '"+ord.getPkPv()+"'");
  	             } 
  		  	   }
  	     	if(updateList!=null&&updateList.size()>0){
  	     		DataBaseHelper.batchUpdate(updateList.toArray(new String[0]));//更新不存在有效医嘱的饮食等级	
  	     	}
  	     	
  		}
  	}
  	/**
  	 * 更新护理等级
  	 * 
  	 * @param pk_nurse
  	 * @param pk_pv
  	 * @param dao
  	 * @throws DAOException
  	 */
  	private void updatePatiNurseInfoByCon(boolean flagStop,String code_ord, String pk_pv,String istts1,String istts2,String istts3,String istts4)  {
  		String sql = "update pv_ip  set dt_level_ns = '" + code_ord+ "'  where pk_pv= ? ";
  		if(flagStop){
  			sql = sql+" and not exists (select 1 from cn_order where eu_status_ord='3' and flag_stop='0' and flag_erase='0' and pk_pv = '"+pk_pv+"' and code_ord in ('"+istts1+"','"+istts2+"','"+istts3+"','"+istts4+"'))";
  		}
  		DataBaseHelper.update(sql,new Object[]{pk_pv});
  	}
  	
  	/**
  	 * 更新患者病情等级
  	 * 
  	 * @param pk_ser
  	 * @param dt_ser
  	 * @param pk_pv
  	 * @param dao
  	 * @throws DAOException
  	 */
  	private void updatePatiSerInfoByCon(boolean flagStop,String update_con,String pk_pv,String isser1,String isser2) {
  		String sql = "update pv_ip "+update_con+"  where pk_pv= ?";
  		if(flagStop){
  			sql = sql+" and not exists (select 1 from cn_order  where eu_status_ord='3' and flag_stop='0' and pk_pv = '"+pk_pv+"' and code_ord in ('"+isser1+"','"+isser2+"'))";
  		}
  		DataBaseHelper.update(sql, new Object[]{pk_pv});
  	}
  	/**
  	 * 获取人员工作岗位
  	 * @param param
  	 * @param user
  	 * @return
  	 */
  	public String getUserJob(String param,IUser user){
  		Map<String,String> paramMap =  JsonUtil.readValue(param, Map.class);
  		if(paramMap==null||CommonUtils.isEmptyString(paramMap.get("pkEmp"))||CommonUtils.isEmptyString(paramMap.get("pkDept")))
  			return null;
  	    Map<String,Object> map = DataBaseHelper.queryForMap("select dt_empjob from bd_ou_empjob where pk_emp =:pkEmp and pk_dept =:pkDept", paramMap);
  	    if(map!=null&&map.get("dtEmpjob")!=null&&!"".equals(map.get("dtEmpjob")))
  	    	return map.get("dtEmpjob").toString();
  	    else
  	    	return null;
  	}

	/**
	 * 保存皮试结果日志
	 * @param stvo
	 * @param user
	 * @param euSt
	 */
	public void saveSysApplogInfo(ExStOccVo stvo, User user, String euSt){
		SysApplog log = new SysApplog();
		log.setPkOrg(user.getPkOrg());
		log.setDateOp(new Date());
		log.setPkEmpOp(user.getPkEmp());
		log.setNameEmpOp(user.getNameEmp());
		log.setObjname("皮试医嘱和关联皮试医嘱结果保存");
		log.setEuButype("99");
		log.setEuOptype("0");
		log.setPkObj(stvo.getPkCnord());//皮试医嘱主键
		log.setContentBf(euSt);//修改前的皮试结果
		log.setContent("-".equals(stvo.getResult())?"2":"3");//修改后的皮试结果
		if(stvo.getOrdList()!=null&&stvo.getOrdList().size()>0){
			log.setNote(stvo.getOrdList().toString());//保存关联医嘱主键，多个用逗号隔开
		}
		DataBaseHelper.insertBean(log);

	}
      
}

