package com.zebone.nhis.pro.zsba.ex.service;

import java.util.*;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Lis;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;




import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOccDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.ex.dao.VtMapper;
import com.zebone.nhis.pro.zsba.ex.vo.ExVtsOccByPv;
import com.zebone.nhis.pro.zsba.ex.vo.ExVtsOccVo;
import com.zebone.nhis.pro.zsba.ex.vo.ExVtsoccDtVo;
import com.zebone.nhis.pro.zsba.ex.vo.SkinTestVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class VtService {
	
	@Resource
	private VtMapper vtMapper;

	private Logger logger = LoggerFactory.getLogger("nhis.BaWebServiceLog");
	
	/**
	 * 查询生命体征信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExVtsOccVo> queryVts(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	String dateCur = "";
		dateCur = CommonUtils.getString(paramMap.get("dateCur")).substring(0, 8)+"000000";
		if(paramMap.get("dateBefore") != null){		
			String dateBefore = CommonUtils.getString(paramMap.get("dateBefore")).substring(0, 8)+"000000";			
			paramMap.put("dateBefore", dateBefore);
		}
		if(paramMap.get("dateAfter") != null){
			String dateAfter = CommonUtils.getString(paramMap.get("dateAfter")).substring(0, 8)+"235959";			
			paramMap.put("dateAfter", dateAfter);
		}
		paramMap.put("dateCur", dateCur);
	    Map<String,Object> map = DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where pk_dept = ? and dt_depttype = '0310'", new Object[]{paramMap.get("pkDeptNs")});
	    List<ExVtsOccVo> vts = new ArrayList<ExVtsOccVo>();
	    if(map == null || CommonUtils.isNull(map.get("pkDept"))){//不是产房
	    	vts = vtMapper.queryVtsByDate(paramMap);
    	}else{//产房
    		vts = vtMapper.queryLaborVtsByDate(paramMap);
    	}	    
    	if(vts==null || vts.size()<=0) return null;
    	
    	Set<String> pkVtsoccs = new HashSet<String>();
    	for(ExVtsOccVo vo :vts){
    		if(!CommonUtils.isEmptyString(vo.getPkVtsocc())){
    			pkVtsoccs.add(vo.getPkVtsocc());
    		}
    	}
    	paramMap.put("pkVtsoccs", CommonUtils.convertSetToSqlInPart(pkVtsoccs, "dt.pk_vtsocc"));
		List<ExVtsoccDtVo> queryVtsDetails = vtMapper.queryVtsDetailsByPk(paramMap);
		for(ExVtsOccVo vo :vts){
			// 住院天数 -- 出院则用出院时间计算，未出院用当前日期
			if( vo.getDateBegin() != null && vo.getDateEnd()!=null ){
				vo.setIpDays(DateUtils.getDateSpace(vo.getDateBegin(), vo.getDateEnd())+1);
			}else if (vo.getDateBegin() != null) {
				vo.setIpDays(DateUtils.getDateSpace(vo.getDateBegin(), new Date())+1);
			} else {
				vo.setIpDays(0);
			}

			List<ExVtsoccDtVo> exVtsList = new ArrayList<ExVtsoccDtVo>(); 
			for (ExVtsoccDtVo exVtsOccDt : queryVtsDetails) {
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
	 * 根据患者pkPv查询患者皮试结果
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SkinTestVo> querySkinTestByPkPv(String param,IUser user){
		String pkPv=JsonUtil.getFieldValue(param, "pkPv");		
		List<SkinTestVo> skinTestVos = vtMapper.querySkinTestByPkPv(pkPv);		
		return skinTestVos;		
	}
	
	/**
     * 保存生命体征信息
     * @param param 
     * @param user
     */
    public void saveVts(String param,IUser user){
    	List<ExVtsOccVo> vtslist = JsonUtil.readValue(param,new TypeReference<List<ExVtsOccVo>>(){});
		JSONArray json = JSONArray.fromObject(vtslist);
		String jsonStr = json.toString();
		logger.info("前台传参为"+jsonStr);
    	if(vtslist == null || vtslist.size() < 0 ){
    		throw new BusException("请确认是否录入了生命体征信息");
    	}
		ExVtsOccVo occVo = vtslist.get(0);
    	if(occVo.getDetails().size() > 0){
    		String queSql = "select * from pv_encounter where pk_pv = ?";
			PvEncounter pv = DataBaseHelper.queryForBean(queSql,PvEncounter.class,occVo.getPkPv());
			ExVtsoccDtVo vtsDt = occVo.getDetails().get(0);
			if(vtsDt != null){
				Date begin = pv.getDateBegin();
				Date dateVts = vtsDt.getDateVts();
				Calendar beginDate = Calendar.getInstance();
				Calendar vtsDate = Calendar.getInstance();
				beginDate.setTime(begin);
				vtsDate.setTime(dateVts);
				if((beginDate.get(Calendar.YEAR) == vtsDate.get(Calendar.YEAR))
				&& (beginDate.get(Calendar.MONTH) == vtsDate.get(Calendar.MONTH))
				&& (beginDate.get(Calendar.DAY_OF_MONTH) > vtsDate.get(Calendar.DAY_OF_MONTH))){
					throw new BusException("入院前生命体征信息不可录入！");
				}
			}
		}
    	for(ExVtsOccVo vts:vtslist){
    		ExVtsOcc occ = new ExVtsOcc();
        	ApplicationUtils.copyProperties(occ, vts);
        	occ.setPkDeptInput(UserContext.getUser().getPkDept());
        	occ.setModifier(UserContext.getUser().getPkEmp());
        	occ.setModityTime(new Date());
        	occ.setTs(new Date());
            occ.setInfantNo("0");
			JSONObject vtsJson = JSONObject.fromObject(vts);
        	if(CommonUtils.isEmptyString(vts.getPkVtsocc())){//新增
        		logger.info("新增参数为"+vtsJson.toString());
        		String queSql = "select * from ex_vts_occ where pk_pv = ? and date_vts = ? and del_flag = '0'";
				List<ExVtsOcc> exVtsOccs = DataBaseHelper.queryForList(queSql,ExVtsOcc.class,occ.getPkPv(),occ.getDateVts());
				if(exVtsOccs.size() > 0){
					throw new BusException("该患者生命体征信息已变动，请刷新后重新录入！");
				}
        		DataBaseHelper.insertBean(occ);
        		List<ExVtsoccDtVo>  details = vts.getDetails();
        		//加测体温明细
        		List<ExVtsOccDt>  addDetails = new ArrayList<>();
            	if(details!=null&&details.size()>0){
            		for(ExVtsoccDtVo dt:details){            			
            			dt.setPkVtsoccdt(NHISUUID.getKeyId());
            			dt.setPkVtsocc(occ.getPkVtsocc());
            			if(null != dt.getAddValTemp()){
            				ExVtsOccDt exVtsOccDt = new ExVtsOccDt();            				
            				ApplicationUtils.copyProperties(exVtsOccDt, dt);
            				exVtsOccDt.setPkVtsoccdt(NHISUUID.getKeyId());
            				exVtsOccDt.setPkVtsocc(occ.getPkVtsocc());
            				exVtsOccDt.setValTemp(dt.getAddValTemp());
            				exVtsOccDt.setFlagAdd("1");
            				dt.setPkVtsoccdtRel(exVtsOccDt.getPkVtsoccdt());
            				setProperty(null,exVtsOccDt);
            				addDetails.add(exVtsOccDt);
            			}
            			setProperty(dt,null);     			            			
            		}
            		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExVtsOccDt.class), details);
            		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExVtsOccDt.class), addDetails);
            	}
        	}else{//修改
        		//由于产品要求数字类型要可以修改为null,因此用sql的方式进行更新操作。
        		String sql = "update EX_VTS_OCC set PK_PI=:pkPi,PK_PV=:pkPv,DATE_VTS=:dateVts,"
        				+ "EU_STOOLTYPE=:euStooltype,VAL_STOOL=:valStool,FLAG_COLO=:flagColo,"
        				+ "VAL_STOOL_COLO=:valStoolColo,VAL_URINE=:valUrine,VAL_OUTPUT=:valOutput,"
        				+ "VAL_INPUT=:valInput,VAL_WEIGHT=:valWeight,VAL_SBP=:valSbp,VAL_DBP=:valDbp,"
        				+ "VAL_SBP_ADD=:valSbpAdd,VAL_DBP_ADD=:valDbpAdd,VAL_ST=:valSt,VAL_OTH=:valOth,"
        				+ "PK_DEPT_INPUT=:pkDeptInput,DATE_INPUT=:dateInput,PK_EMP_INPUT=:pkEmpInput,"
        				+ "NAME_EMP_INPUT=:nameEmpInput,MODITY_TIME=:modityTime,INFANT_NO=:infantNo,"
        				+ "DT_OUTPUTTYPE=:dtOutputtype,HOUR_OUTPUT=:hourOutput,HOUR_OUTPUT_TOTAL=:hourOutputTotal,"
        				+ "HOUR_INPUT=:hourInput,HOUR_URINE=:hourUrine,MODIFIER=:modifier,VAl_OUTPUT_TOTAL=:valOutputTotal,"
        				+ "del_flag=:delFlag,TS =:ts where PK_VTSOCC=:pkVtsocc";
        		DataBaseHelper.update(sql, occ);
        		List<ExVtsoccDtVo>  details = vts.getDetails();
        		//加测体温明细
        		List<ExVtsOccDt>  insert_addDetails = new ArrayList<>();
        		List<ExVtsOccDt>  update_addDetails = new ArrayList<>();
            	if(details!=null&&details.size()>0){
            		List<ExVtsoccDtVo> insert_list = new ArrayList<ExVtsoccDtVo>();
            		List<ExVtsoccDtVo> update_list = new ArrayList<ExVtsoccDtVo>();
            		for(ExVtsoccDtVo dt:details){
            			String querySql = "select * from EX_VTS_OCC_DT where pk_vtsocc = ? and eu_dateslot = ? and hour_vts = ?";
						List<ExVtsOccDt> occDtList = DataBaseHelper.queryForList(querySql,ExVtsOccDt.class,occ.getPkVtsocc(),dt.getEuDateslot(),dt.getHourVts());
            			 if(occDtList.size() < 1 && CommonUtils.isEmptyString(dt.getPkVtsoccdt())){//新增的明细
            				dt.setPkVtsoccdt(NHISUUID.getKeyId());
            				dt.setPkVtsocc(occ.getPkVtsocc());
            				if(null != dt.getAddValTemp()){
                				ExVtsOccDt exVtsOccDt = new ExVtsOccDt();
                				ApplicationUtils.copyProperties(exVtsOccDt, dt);
                				exVtsOccDt.setPkVtsoccdt(NHISUUID.getKeyId());
                				exVtsOccDt.setPkVtsocc(occ.getPkVtsocc());
                				exVtsOccDt.setValTemp(dt.getAddValTemp());
                				exVtsOccDt.setFlagAdd("1");
                				dt.setPkVtsoccdtRel(exVtsOccDt.getPkVtsoccdt());
                				setProperty(null,exVtsOccDt);
                				insert_addDetails.add(exVtsOccDt);
                				String dqlSql = "update ex_vts_occ_dt set val_temp = null,del_flag = '1' where flag_add = '1' and pk_vtsocc = ? and pk_org = ? and hour_vts = ? and eu_dateslot = ?";
                				DataBaseHelper.update(dqlSql,new Object[]{exVtsOccDt.getPkVtsocc(),exVtsOccDt.getPkOrg(),exVtsOccDt.getHourVts(),exVtsOccDt.getEuDateslot()});
                			}
            				setProperty(dt,null);  
            				insert_list.add(dt);
            			}else{//修改的数据
            			 	if(occDtList.size() > 0 ){
            			 		String delSql = "delete from EX_VTS_OCC_DT where pk_vtsocc = ? and eu_dateslot = ? and hour_vts = ?";
            			 		DataBaseHelper.execute(delSql,occ.getPkVtsocc(),dt.getEuDateslot(),dt.getHourVts());
								dt.setPkVtsoccdt(NHISUUID.getKeyId());
								dt.setPkVtsocc(occ.getPkVtsocc());
								if(null != dt.getAddValTemp()){
									ExVtsOccDt exVtsOccDt = new ExVtsOccDt();
									ApplicationUtils.copyProperties(exVtsOccDt, dt);
									exVtsOccDt.setPkVtsoccdt(NHISUUID.getKeyId());
									exVtsOccDt.setPkVtsocc(occ.getPkVtsocc());
									exVtsOccDt.setValTemp(dt.getAddValTemp());
									exVtsOccDt.setFlagAdd("1");
									dt.setPkVtsoccdtRel(exVtsOccDt.getPkVtsoccdt());
									setProperty(null,exVtsOccDt);
									insert_addDetails.add(exVtsOccDt);
									String dqlSql = "update ex_vts_occ_dt set val_temp = null,del_flag = '1' where flag_add = '1' and pk_vtsocc = ? and pk_org = ? and hour_vts = ? and eu_dateslot = ?";
									DataBaseHelper.update(dqlSql,new Object[]{exVtsOccDt.getPkVtsocc(),exVtsOccDt.getPkOrg(),exVtsOccDt.getHourVts(),exVtsOccDt.getEuDateslot()});
								}
								setProperty(dt,null);
								insert_list.add(dt);
							}else{
								setProperty(dt,null);
								update_list.add(dt);
								if(null != dt.getAddValTemp()){
									ExVtsOccDt exVtsOccDt = new ExVtsOccDt();
									if(CommonUtils.isEmptyString(dt.getPkVtsoccdtDt()))
									{
										ApplicationUtils.copyProperties(exVtsOccDt, dt);
										exVtsOccDt.setPkVtsoccdt(NHISUUID.getKeyId());
										exVtsOccDt.setPkVtsocc(occ.getPkVtsocc());
										exVtsOccDt.setValTemp(dt.getAddValTemp());
										exVtsOccDt.setFlagAdd("1");
										dt.setPkVtsoccdtRel(exVtsOccDt.getPkVtsoccdt());
										setProperty(null,exVtsOccDt);
										insert_addDetails.add(exVtsOccDt);
										String dqlSql = "update ex_vts_occ_dt set val_temp = null,del_flag = '1' where flag_add = '1' and pk_vtsocc = ? and pk_org = ? and hour_vts = ? and eu_dateslot = ?";
										DataBaseHelper.update(dqlSql,new Object[]{exVtsOccDt.getPkVtsocc(),exVtsOccDt.getPkOrg(),exVtsOccDt.getHourVts(),exVtsOccDt.getEuDateslot()});
									}else{
										ApplicationUtils.copyProperties(exVtsOccDt, dt);
										exVtsOccDt.setPkVtsoccdt(dt.getPkVtsoccdtDt());
										exVtsOccDt.setValTemp(dt.getAddValTemp());
										exVtsOccDt.setFlagAdd("1");
										dt.setPkVtsoccdtRel(exVtsOccDt.getPkVtsoccdt());
										setProperty(null,exVtsOccDt);
										update_addDetails.add(exVtsOccDt);
									}
								}else{
									ExVtsOccDt exVtsOccDt = new ExVtsOccDt();
									ApplicationUtils.copyProperties(exVtsOccDt, dt);
									exVtsOccDt.setPkVtsoccdt(dt.getPkVtsoccdtDt());
									exVtsOccDt.setValTemp(dt.getAddValTemp());
									exVtsOccDt.setFlagAdd("1");
									dt.setPkVtsoccdtRel(exVtsOccDt.getPkVtsoccdt());
									setProperty(null,exVtsOccDt);
									update_addDetails.add(exVtsOccDt);
								}
							}
            			} 
            		}
            		if(insert_list!=null&&insert_list.size()>0){
            			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExVtsOccDt.class), insert_list);
            		}
            		if(insert_addDetails!=null&&insert_addDetails.size()>0){
            		   DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExVtsOccDt.class), insert_addDetails);
            		}
            		if(update_list!=null&&update_list.size()>0){            			 
 						DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExVtsOccDt.class), update_list);
            		}
            		if(update_addDetails!=null&&update_addDetails.size()>0){
            		    DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExVtsOccDt.class), update_addDetails);
            		}
            	}
        	}
			Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
			if(vts.getValWeight() != null && pattern.matcher(vts.getValWeight()).matches()){
				String updSql = "update pv_encounter set weight = ? where pk_pv = ?";
				DataBaseHelper.update(updSql,vts.getValWeight(),vts.getPkPv());
			}


			Map<String,Object> paramMap = new HashMap<>(16);
			paramMap.put("pkPv",vts.getPkPv());
			PlatFormSendUtils.sendVitalSigns(paramMap);
    	}


    	
    }
    
    public void setProperty(ExVtsoccDtVo dt, ExVtsOccDt exVtsOccDt){
    	if(exVtsOccDt != null){
    		exVtsOccDt.setModifier(UserContext.getUser().getPkEmp());    		
    		exVtsOccDt.setModityTime(new Date());     		
    		exVtsOccDt.setTs(new Date());
    		exVtsOccDt.setPkOrg(UserContext.getUser().getPkOrg());
    		exVtsOccDt.setDelFlag("0");    		
    	}
    	if(dt != null){
    		dt.setModifier(UserContext.getUser().getPkEmp());    		
    		dt.setModityTime(new Date());     		
    		dt.setTs(new Date());
    		dt.setPkOrg(UserContext.getUser().getPkOrg());
    		dt.setDelFlag("0");    
    	}
    }
    
    
    /**
     * 查询单个患者生命体征信息
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
    	
    	List<ExVtsOccByPv> vtsList = vtMapper.queryVtsByPvAndDateByOne(paramMap);//查询患者录入日期的记录
    	if(vtsList == null || vtsList.size() < 1) 
    		throw new BusException("未查询到患者["+paramMap.get("pkPv")+"]在["+paramMap.get("dateOcc")+"]的生命体征信息！");
    	//else if(vtsList.size() > 1)
    		//throw new BusException("该患者["+paramMap.get("pkPv")+"]在["+paramMap.get("dateOcc")+"]存在多条生命体征信息！");
        ExVtsOccByPv vtsByPv = vtsList.get(0);
    	if(!CommonUtils.isEmptyString(vtsByPv.getPkVtsocc())){
    		paramMap.put("pkVtsocc", vtsByPv.getPkVtsocc());
    		List<ExVtsoccDtVo> dtList = vtMapper.queryVtsDetailsByPV(paramMap);
    		vtsByPv.setListDts(dtList);
    	}
    	return vtsByPv;
    }

	

}
