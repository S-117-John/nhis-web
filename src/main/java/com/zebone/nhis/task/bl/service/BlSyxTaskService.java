package com.zebone.nhis.task.bl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * 中二定时记费服务
 * @author c
 *
 */
@Service
public class BlSyxTaskService {
	@Resource
	private IpCgPubService ipCgPubService;
	
	private Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	
	/**
     * 启用定时任务放入
     * @param cfg
     */
    public void pvisExCgTask(QrtzJobCfg cfg){
    	try{
    		PivasChargeExec(cfg);
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * 静配定时记费
     */
    public void PivasChargeExec(QrtzJobCfg cfg){
    	List<Map<String,Object>> itemMap = (List<Map<String, Object>>) ExtSystemProcessUtils.processExtMethod("PIVAS", "qryPivasCgItem", null);
    	
    	//如果有待收费项目则组装记费数据进行记费
    	if(itemMap!=null && itemMap.size()>0){ 
    		logger.info("================调用静配定时任务开始================");
    		String pkOrg = cfg.getJgs();
			if(CommonUtils.isEmptyString(pkOrg))
				throw new BusException("未获取到机构信息！");
			
			String[] orgarr = pkOrg.split(",");
			if(orgarr!=null&&orgarr.length>0){
				for(int i = 0; i<orgarr.length;i++){
					User u = new User();  
					u.setPkOrg(orgarr[i]);
					UserContext.setUser(u);
					if(!CommonUtils.isEmptyString(u.getPkOrg()))
						break;
				}
			}
    		
    		//患者信息集合
    		List<Map<String,Object>> pvList = new ArrayList<>();
    		//收费项目集合
    		List<Map<String,Object>> itemList=  new ArrayList<>();
    		//执行科室、住院科室集合
    		List<Map<String,Object>> deptList = new ArrayList<>();
    		//人员集合
    		List<Map<String,Object>> empList=  new ArrayList<>();
    		
    		Set<String> codePvList = new HashSet<String>();
    		Set<String> deptIdList = new HashSet<String>();
    		Set<String> codeItemList = new HashSet<String>();
    		Set<String> codeEmpList = new HashSet<String>();
    		
    		for(Map<String,Object> map : itemMap){
    			codePvList.add(CommonUtils.getString(map.get("codePi")));
    			deptIdList.add(CommonUtils.getString(map.get("oldIdExdept")));
    			deptIdList.add(CommonUtils.getString(map.get("oldIdIpdept")));
    			codeItemList.add(CommonUtils.getString(map.get("codeItem")));
    			codeEmpList.add(CommonUtils.getString(map.get("codeEmp")));
        	}
    		
    		//根据患者codePi获取患者pk_pi,pk_pv
    		String pvListSql = "select pv.eu_status,pv.code_pv code_pi,pv.pk_pi,pv.pk_pv,pv.eu_pvtype,pv.pk_dept,pv.pk_dept_ns,pv.date_end from PV_ENCOUNTER pv "+
    				" where pv.code_pv in("+ CommonUtils.convertSetToSqlInPart(codePvList, "pv.code_pv")+")";
    		pvList = DataBaseHelper.queryForList(pvListSql, new Object[]{});
			//pvList = blSyxTaskMapper.qryPvList(codePiList);
    		//根据执行科室id，住院科室ID获取科室信息
    		String deptListSql = " select old_id,pk_dept,pk_org from bd_ou_dept dept where dept.old_id in ("+CommonUtils.convertSetToSqlInPart(deptIdList, "dept.old_id")+")";
    		deptList = DataBaseHelper.queryForList(deptListSql, new Object[]{});
    		//cc = blSyxTaskMapper.qryDeptList(deptIdList);
    		//根据收费项目code获取收费项目集合
    		String itemListSql = "select code as code_item,pk_item,pk_itemcate from bd_item where code in ("+CommonUtils.convertSetToSqlInPart(codeItemList, "code")+")";
    		itemList = DataBaseHelper.queryForList(itemListSql, new Object[]{});
    		//itemList = blSyxTaskMapper.qryItemList(codeItemList);
    		//根据人员编码获取人员信息集合
    		String empListSql = "select emp.code_emp,emp.pk_emp,emp.name_emp,empjob.pk_dept from bd_ou_employee emp "+
    				" inner join bd_ou_empjob empjob on emp.pk_emp = empjob.pk_emp where emp.code_emp in ("+CommonUtils.convertSetToSqlInPart(codeEmpList, "emp.code_emp")+")";
    		empList = DataBaseHelper.queryForList(empListSql, new Object[]{});
    		//empList = blSyxTaskMapper.qryEmpList(codeEmpList);
    		
    		//组装记费明细数据调用住院记费接口
    		List<String> accListID = new ArrayList<>();	//记账ID集合
    		Map<String,String> pvCgMap = new HashMap<>();	//待记费患者信息集合key:记账Id value:患者就诊主键
    		List<BlPubParamVo> blCgVos = new ArrayList<>();
    		for(Map<String,Object> map : itemMap){
    			Map<String,Object> pvMap = this.getMap(pvList,"codePi",CommonUtils.getString(map.get("codePi")));
    			Map<String,Object> cgItemMap = this.getMap(itemList,"codeItem",CommonUtils.getString(map.get("codeItem")));
    			Map<String,Object> empMap = this.getMap(empList,"codeEmp",CommonUtils.getString(map.get("codeEmp")));
    			Map<String,Object> exDeptMap = this.getMap(deptList, "oldId", CommonUtils.getString(map.get("oldIdExdept")));
    			Map<String,Object> ipDeptMap = this.getMap(deptList, "oldId", CommonUtils.getString(map.get("oldIdIpdept")));
    			
    			if((pvMap!=null && pvMap.size()>0) 
    					&& (cgItemMap!=null && cgItemMap.size()>0)
    					//&& (empMap!=null && empMap.size()>0)
    					//&& (ipDeptMap!=null && ipDeptMap.size()>0)
    					&& (exDeptMap!=null && exDeptMap.size()>0)){
    				BlPubParamVo vo = new BlPubParamVo();
    				
    				vo.setPkOrg(pkOrg);
    				vo.setPkPi(CommonUtils.getString(pvMap.get("pkPi")));
    				vo.setPkPv(CommonUtils.getString(pvMap.get("pkPv")));
    				vo.setEuPvType(CommonUtils.getString(pvMap.get("euPvtype")));
    				vo.setPkDeptApp(CommonUtils.getString(pvMap.get("pkDept")));
    				vo.setPkOrgApp(CommonUtils.getString(pvMap.get("pkOrg")));
    				vo.setPkDeptNsApp(CommonUtils.getString(pvMap.get("pkDeptNs")));
    				vo.setPkDeptEx(CommonUtils.getString(exDeptMap.get("pkDept")));
    				vo.setPkOrgEx(CommonUtils.getString(exDeptMap.get("pkOrg")));
    				vo.setPkEmpCg(CommonUtils.getString(empMap.get("pkEmp")));
    				vo.setNameEmpCg(CommonUtils.getString(empMap.get("nameEmp")));
    				vo.setPkItem(CommonUtils.getString(cgItemMap.get("pkItem")));
    				vo.setQuanCg(CommonUtils.getDoubleObject(map.get("quan")));
    				vo.setPrice(CommonUtils.getDoubleObject(map.get("price")));
    				vo.setEuBltype("99");
    				vo.setFlagPd("0");
    				//如果患者的eu_sattus=2,date_cg的值写出院时间-1秒
    				if(CommonUtils.getString(pvMap.get("euStatus")).equals("2") &&
    						pvMap.get("dateEnd")!=null){
    					Date dateEnd = DateUtils.strToDate(pvMap.get("dateEnd").toString(),"yyyy-MM-dd HH:mm:ss");
    					String strDate = DateUtils.addDate(dateEnd, -1, 6, "yyyy-MM-dd HH:mm:ss");
    					Date dateCg = DateUtils.strToDate(strDate,"yyyy-MM-dd HH:mm:ss");
    					vo.setDateCg(dateCg);
    				}
    				
    				blCgVos.add(vo);
    				//统计需要记费的记账ID
    				pvCgMap.put(CommonUtils.getString(map.get("accountlistid")), CommonUtils.getString(pvMap.get("pkPv")));
    			}
    			
    		}
    		
    		if(blCgVos!=null && blCgVos.size()>0){
    			//调用住院记费接口
    			BlPubReturnVo rtnVo = ipCgPubService.chargeIpBatch(blCgVos, true);
    			
    			List<BlIpDt> dtList = rtnVo.getBids();
    			
    			//根据记费返回的记费明细，来确定要更新的记账ID
    			for(String key : pvCgMap.keySet()){
    				for(int i=0; i<dtList.size(); i++){
    					if(pvCgMap.get(key).equals(dtList.get(i).getPkPv())){//校验待记费的pkpv和已记费的pkpv是否相同，相同则更新该记账ID
    						accListID.add(key);//统计需要更新的记账ID
    						break;
    					}
    				}
    			}
    			
    			if(accListID!=null && accListID.size()>0)
    				ExtSystemProcessUtils.processExtMethod("PIVAS", "updatePivas", accListID);	//更新中间表
    		}
    		logger.info("================调用静配定时任务结束================");
    	}
    	
    }
    
    /**
     * 根据传入的key,value对比ListMap里是否有相同的value，如果有返回此map
     * @param listMap
     * @param key
     * @return
     */
    private Map<String,Object> getMap(List<Map<String,Object>> listMap,String key,String value){
    	Map<String,Object> rtnMap = new HashMap<String, Object>();
    	if(listMap!=null && listMap.size()>0){
	    	for(Map<String,Object> map : listMap){
	    		if(CommonUtils.getString(map.get(key)).equals(value)){
	    			rtnMap = map;
	    			break;
	    		}
	    	}
    	}
    	return rtnMap;
    }
	
}
