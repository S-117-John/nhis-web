package com.zebone.nhis.labor.nis.service;

import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.module.labor.nis.PvLabor;
import com.zebone.nhis.common.module.labor.nis.PvLaborStaff;
import com.zebone.nhis.common.module.pv.PvInfant;
import com.zebone.nhis.common.module.pv.support.PvFunction;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.dao.AdtPubMapper;
import com.zebone.nhis.ex.pub.support.ExListSortByOrdUtil;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.labor.nis.dao.PvLaborAdtMapper;
import com.zebone.nhis.labor.nis.vo.PvLaborVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 出入产房业务
 * @author yangxue
 *
 */
@Service
public class PvLaborAdtService {
	@Resource
	private AdtPubMapper adtPubMapper;
	@Resource
	private PvLaborAdtMapper pvLaborAdtMapper;
    /**
     * 出产房校验数据查询
     * @param param{pkPv，pkDept，pkDeptNs,pkPvlabor(可选)}
     * @param user
     */
	public Map<String,Object> queryOutVerfyData(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkOrg", ((User)user).getPkOrg());
    	paramMap.put("euStatus", "1");
    	paramMap.put("flagIn", "1");
    	paramMap.put("pkDeptOcc",((User)user).getPkDept());
    	List<PvLaborVo> pvLabor = pvLaborAdtMapper.queryPvLabor(paramMap);//查询产房就诊信息
    	if(pvLabor!=null&&pvLabor.size()>0){
    		paramMap.put("pkDept", pvLabor.get(0).getPkDept());
    		paramMap.put("pkDeptNs", pvLabor.get(0).getPkDeptNs());
    	}else{
    		throw new BusException("未获取到产房就诊记录，无法办理出产房！");
    	}
    	List<Map<String,Object>> ordData = adtPubMapper.queryOrdByPkPv(paramMap);//未停未作废医嘱
    	List<Map<String,Object>> risData = adtPubMapper.queryRisByPkPv(paramMap);//未执行执行单
    	List<ExlistPubVo> exListData = adtPubMapper.queryExlistByPv(paramMap);//未执行执行单
    	 new ExListSortByOrdUtil().ordGroup(exListData);
    	List<Map<String,Object>> opData = adtPubMapper.queryOpByPkPv(paramMap);//未完成手术
    	
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
    	if (!isEmpty(opData)) {
    		result.put("opData", opData);
		}
    	if(pvLabor!=null&&pvLabor.size()>0){
    		result.put("pvLabor", pvLabor.get(0));
    	}
    	return result;
	}
	/**
	 * 保存出产房信息
	 * @param param{pkPv,pkPvlabor,note,outReason,bedNo,dateOut,pkDeptNs}
	 * @param user
	 */
	public void saveOutInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//更新孕产就诊记录
		String update_labor = "update pv_labor set date_out = to_date('"+paramMap.get("dateOut")+"','YYYYMMDDHH24MISS') ,flag_in = '0',"
				+ "eu_status = '2',note = :note ,out_reason = :outReason where pk_pvlabor = :pkPvlabor";
		DataBaseHelper.update(update_labor, paramMap);
		//更新孕产医护人员
		DataBaseHelper.update("update pv_labor_staff set date_end = to_date('"+paramMap.get("dateOut")+"','YYYYMMDDHH24MISS')  where date_end is null and pk_pvlabor = :pkPvlabor", paramMap);
		/* 更新bd_res_bed 相关信息*/
		paramMap.put("pkDeptNs", ((User)user).getPkDept());
    	String updateBed = " update bd_res_bed set pk_pi = null , pk_dept_used = null , flag_ocupy = '0' , eu_status = '01' where code = :bedNo and pk_ward = :pkDeptNs";
    	DataBaseHelper.update(updateBed, paramMap);

		//根据母亲就诊记录获取新生儿就诊记录
		DelBedByMomPv(paramMap);
	}

	/**
	 * 根据母亲就诊记录获取新生儿就诊记录
	 * @param paramMap
	 */
	private void DelBedByMomPv(Map<String,Object> paramMap){
		PvLabor pvLaborM = DataBaseHelper.queryForBean("select * from pv_labor where pk_pvlabor = ?",PvLabor.class,paramMap.get("pkPvlabor"));
		PvInfant pvInfantM = DataBaseHelper.queryForBean("select * from pv_infant where pk_pv_infant = ?",PvInfant.class,paramMap.get("pkPv"));
		String pkPvLaborMom = "";
		String pkPvLabor = "";
		if(pvLaborM != null && pvInfantM == null){
			pkPvLaborMom = pvLaborM.getPkPvlabor();

			//更新新生儿就诊记录
			String updateSql = " update pv_labor set date_out = to_date('"+paramMap.get("dateOut")+"','YYYYMMDDHH24MISS')  ,flag_in = '0'," +
					" eu_status = '2',note = '"+paramMap.get("note")+"' ,out_reason = '"+paramMap.get("outReason")+"' " +
					" where pk_pvlabor_mother= '"+pkPvLaborMom+"'";//更新新生儿出入产房表
			DataBaseHelper.update(updateSql);
			//更新新生儿医护人员
			String updSql = " update pv_labor_staff set date_end = to_date('"+paramMap.get("dateOut")+"','YYYYMMDDHH24MISS') " +
					" where date_end is null " +
					" and pk_pvlabor in (select pk_pvlabor from pv_labor where pk_pvlabor_mother = '"+pkPvLaborMom+"') ";
			DataBaseHelper.update(updSql);

			//根据母亲就诊主键获取新生儿就诊主键，删除自动生成的婴儿床位；
			String queBeds = " select bed.* " +
					" from BD_RES_BED bed " +
					" inner join PV_LABOR lab on lab.BED_NO = bed.CODE and lab.PK_DEPT_NS = bed.PK_WARD" +
					" where lab.EU_STATUS='1' and  lab.PK_PVLABOR_MOTHER = ? ";
			List<BdResBed> bdResBeds = DataBaseHelper.queryForList(queBeds,BdResBed.class,pkPvLaborMom);
			if(bdResBeds.size() > 0){
				for(BdResBed bdResBed : bdResBeds){
					if(bdResBed.getPkItem() != null && "婴儿占用标志".equals(bdResBed.getPkItem().trim())){
						DataBaseHelper.update("update BD_RES_BED set eu_status = '01',flag_ocupy = '0',pk_item = null,pk_pi = null where pk_bed = '"+bdResBed.getPkBed()+"'");
					}else{
						DataBaseHelper.execute("delete from BD_RES_BED where pk_bed = '"+bdResBed.getPkBed()+"'");
					}
				}

			}
		}else{
			pkPvLabor = pvLaborM.getPkPvlabor();
			//更新新生儿就诊记录
			String updateSql = " update pv_labor set date_out = to_date('"+paramMap.get("dateOut")+"','YYYYMMDDHH24MISS')  ,flag_in = '0'," +
					" eu_status = '2',note = '"+paramMap.get("note")+"' ,out_reason = '"+paramMap.get("outReason")+"' " +
					" where pk_pvlabor= '"+pkPvLabor+"'";//更新新生儿出入产房表
			DataBaseHelper.update(updateSql);
			//更新新生儿医护人员
			String updSql = " update pv_labor_staff set date_end = to_date('"+paramMap.get("dateOut")+"','YYYYMMDDHH24MISS') " +
					" where date_end is null " +
					" and pk_pvlabor in (select pk_pvlabor from pv_labor where pk_pvlabor = '"+pkPvLabor+"') ";
			DataBaseHelper.update(updSql);

			//获取新生儿就诊主键，删除自动生成的婴儿床位；
			String queBeds = " select bed.* " +
					" from BD_RES_BED bed " +
					" inner join PV_LABOR lab on lab.BED_NO = bed.CODE and lab.PK_DEPT_NS = bed.PK_WARD" +
					" where lab.PK_PVLABOR = ? ";
			BdResBed bdResBed = DataBaseHelper.queryForBean(queBeds,BdResBed.class,pkPvLabor);
			if(bdResBed != null && bdResBed.getPkItem() != null && "婴儿占用标志".equals(bdResBed.getPkItem().trim())){
				DataBaseHelper.update("update BD_RES_BED set eu_status = '01',flag_ocupy = '0',pk_item = null,pk_pi = null where pk_bed = '"+bdResBed.getPkBed()+"'");
			}else{
				DataBaseHelper.execute("delete from BD_RES_BED where pk_bed = '"+bdResBed.getPkBed()+"'");
			}
		}


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
    
    /**
     * 查询产房待入科接收患者列表
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> queryPatisByIn(String param, IUser user){
    	Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String namePi = paramMap.containsKey("namePi") ? paramMap.get("namePi") != null ? paramMap.get("namePi").toString().trim() :"" : "";
		if (CommonUtils.isNull(paramMap.get("pkOrg"))) {
			paramMap.put("pkOrg", ((User) user).getPkOrg());
		}
		List< Map<String ,Object>> list=pvLaborAdtMapper.queryPatisByIn(paramMap);
        list.addAll(pvLaborAdtMapper.queryPatisByQuery(paramMap));
        if(list.size() <1 && !"".equals(namePi)){
			paramMap.put("namePi",namePi);
			list.addAll(pvLaborAdtMapper.quertOutPvInfo(paramMap));//查询患者是否出院
			if(list.size() <1){
				Map<String,Object> map = new HashMap<>();
				map.put("euStatus","-1");
				list.add(map);//系统不存在患者
			}else{
			    List<Map<String ,Object>> piList = new ArrayList<>();
			    for(Map<String ,Object> pi : list){
                    String status = pi.containsKey("status") ? pi.get("status") != null ? pi.get("status").toString().trim() :"" : "";
                    if(!"1".equals(status)){
                        piList.add(pi);
                    }
                }
                list.removeAll(list);
                list.addAll(piList);
            }
		}
        //去重
		List< Map<String ,Object>>  listAllDistinct = list.stream().distinct().collect(toList());
		return listAllDistinct;
    }
    
    /**
     * 保存入科接收信息
     * @param param{pkPv,pkPi,pkPvlabor,pkBed,bedNo,pkEmp01,nameEmp01,
     *  pkEmp02,nameEmp02,pkEmp03,nameEmp03,pkEmp04,nameEmp04,}
     *   角色  ：01：主治医生，02：责任护士，03：接生者，04：婴护者     
     * @param user
     */
    public void SavePatiIn(String param, IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		User u=(User) user;
    	String pkPvlabor = (String) paramMap.get("pkPvlabor"); // 孕产就诊主键
		String bedNo = (String)paramMap.get("bedNo");//床位号
		String pkWard = u.getPkDept();//申请病区
		String pkPv = (String) paramMap.get("pkPv");//患者就诊主键
		//判断该床位是否已被占用
		String queBedSql = "select * from bd_res_bed where pk_ward = ? and code = ?";
		BdResBed bdResBed = DataBaseHelper.queryForBean(queBedSql,BdResBed.class,pkWard,bedNo);
		if(bdResBed != null && "1".equals(bdResBed.getFlagOcupy()) && "02".equals(bdResBed.getEuStatus())){
			throw new BusException("该床位已被占用，请刷新后重新操作！");
		}
		//判断该患者是否已在产房
		String queLabSql = "select * from pv_labor where pk_pv = ?";
		PvLabor pvLab = DataBaseHelper.queryForBean(queLabSql,PvLabor.class,pkPv);
		if(pvLab != null && "1".equals(pvLab.getEuStatus())){
			throw new BusException("该患者已入产房，请勿重复录入！");
		}
		Date dateIn = new Date();
		paramMap.put("dateIn", dateIn);
    	if(CommonUtils.isNull(pkPvlabor)){
    		PvLabor pvLabor=JsonUtil.readValue(param, PvLabor.class);
			pvLabor.setDateIn(dateIn);
			pvLabor.setFlagIn("1");
			pvLabor.setEuStatus("1");
			pvLabor.setPkPvlabor(NHISUUID.getKeyId());
			pvLabor.setBedNo(paramMap.get("bedNo").toString());
    		pvLabor.setPkEmpDoctor(paramMap.get("pkEmp01").toString());
    		pvLabor.setNameEmpDoctor(paramMap.get("nameEmp01").toString());
    		pvLabor.setPkEmpNurse(paramMap.get("pkEmp02").toString());
    		pvLabor.setNameEmpNurse(paramMap.get("nameEmp02").toString());
    		pvLabor.setPkDept(u.getPkDept());
    		pvLabor.setPkDeptNs(u.getPkDept());
    		DataBaseHelper.insertBean(pvLabor);
			pkPvlabor=pvLabor.getPkPvlabor();
    		paramMap.put("pkPvlabor",pvLabor.getPkPvlabor());
    	}else {
			/* 1、 更新pv_labor 相关信息*/

			StringBuilder updatePvlabor = new StringBuilder("update pv_labor set date_in = :dateIn , flag_in = '1' ,");
			updatePvlabor.append(" pk_emp_doctor = :pkEmp01 , name_emp_doctor = :nameEmp01 ,");
			updatePvlabor.append(" pk_emp_Nurse = :pkEmp02 , name_emp_Nurse = :nameEmp02 ,");
			updatePvlabor.append(" eu_status = '1' , bed_no = :bedNo");
			updatePvlabor.append(" where pk_pvlabor = :pkPvlabor");
			DataBaseHelper.update(updatePvlabor.toString(), paramMap);



		}

    	//如果是已出产房的婴儿重新入产房占用的床位，费用主键字段设置占用标志，再次出产房时该床位不删除
		PvInfant pvInfant = DataBaseHelper.queryForBean("select * from pv_infant where pk_pi_infant = ?",PvInfant.class,paramMap.get("pkPi"));
		/* 2、 更新bd_res_bed 相关信息*/
		String updateBed = " update bd_res_bed set pk_pi = :pkPi , flag_ocupy = '1' , eu_status = '02' where pk_bed = :pkBed";
		if(pvInfant != null){
			updateBed = " update bd_res_bed set pk_pi = :pkPi ,flag_ocupy = '1',pk_item ='婴儿占用标志',eu_status = '02' where pk_bed = :pkBed";
		}
		DataBaseHelper.update(updateBed, paramMap);

    	
    	/* 3、 插入pv_labor_staff 表*/
    	List<PvLaborStaff> list_staffs = new ArrayList<PvLaborStaff>();
    	for(int i = 1 ; i <= 4 ; i++){
    		if(CommonUtils.isNull(paramMap.get("pkEmp0" + i)) 
    				|| CommonUtils.isEmptyString(paramMap.get("pkEmp0" + i).toString()))
    			continue;
    		PvLaborStaff pvLabStaff = new PvLaborStaff();
    		pvLabStaff.setPkPvlaborStaff(NHISUUID.getKeyId());
    		pvLabStaff.setPkOrg( ((User) user).getPkOrg());
    		pvLabStaff.setPkPv(pkPv);
    		pvLabStaff.setPkPvlabor(pkPvlabor);
    		pvLabStaff.setDateBegin(dateIn);
    		pvLabStaff.setDtRole("0" + i);
    		pvLabStaff.setPkEmp((String) paramMap.get("pkEmp0" + i));
    		pvLabStaff.setNameEmp((String) paramMap.get("nameEmp0" + i));
    		list_staffs.add(pvLabStaff);
    	}
    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvLaborStaff.class), list_staffs);
    }
    /**
     * @Description 保存患者功能呢科室就诊信息
     * @auther wuqiang
     * @Date 2020-12-20
     * @Param [param, user]
     * @return void
     */
    public  void savePvFunction(String param,IUser user){
		PvFunction pvFunction = JsonUtil.readValue(param, PvFunction.class);
		if (pvFunction==null){
			return;
		}
		int count_code = DataBaseHelper.queryForScalar(
				"select count(*) from PV_FUNCTION  where PK_PV=? and PK_DEPT=? ",
				Integer.class, new Object[]{pvFunction.getPkPv(),pvFunction.getPkDept()});
		if (count_code==0){
			ApplicationUtils.setDefaultValue(pvFunction,true);
			pvFunction.setDateFun(new Date());
			DataBaseHelper.insertBean(pvFunction);
		}

	}
}
