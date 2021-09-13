package com.zebone.nhis.pv.hd.service;

import com.zebone.nhis.bl.hd.dao.HdCgMapper;
import com.zebone.nhis.common.module.cn.ipdw.PvDiag;
import com.zebone.nhis.common.module.pi.PiHd;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvHd;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.pv.hd.dao.HdPvMapper;
import com.zebone.nhis.pv.hd.vo.PiHdVo;
import com.zebone.nhis.pv.hd.vo.SavePiVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 透析治疗登记服务
 * @author yangxue
 *
 */
@Service
public class HdPvService {


   @Autowired
    private HdPvMapper hdPvMapper;

   @Autowired
	public HdCgMapper hdCgMapper;
	/**
	 * 查询透析排班信息
	 * @param param
	 * @param user
	 * @return
	 */
    public List<Map<String,Object>> queryHdSch(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	//pkOrg 当前机构  
    	if(paramMap==null||CommonUtils.isNull(paramMap.get("pkOrg")))
			throw new BusException("未获取到要查询的机构");
    	//查询排班信息
    	List<Map<String,Object>> result=hdPvMapper.queryHdSch(paramMap);
    	return result;
    }
    /**
     * 查询患者基本信息
     * @param param
     * @param user
     * @return
     */
    public List<PiHdVo> queryHdPiMaster(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	//pkPi 患者标识
    	if(paramMap==null||CommonUtils.isNull(paramMap.get("pkPi")))
			throw new BusException("未获取到要查询的患者标识");
    	//查询患者信息
    	List<PiHdVo> result=hdPvMapper.queryHdPiMaster(paramMap);
    	return result;
    }

    /**
     * 档案管理- 查询透析档案信息
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> queryHdPiMasterFile(String param,IUser user){
        Map paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap ==null){
            throw new BusException("未获取到患者信息");
        }
        List<Map<String,Object>> ListPiMasterFile = hdPvMapper.queryHdPiMasterPkPi(paramMap);
        return ListPiMasterFile;
    }

    /**
     * 查询治疗记录
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> queryAcography(String param,IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
        if(paramMap==null||paramMap.get("pkPi")==null||paramMap.get("pkPi").equals("")){
            throw new BusException("未获取到患者就诊标识pkPi");
        }
        List<Map<String,Object>> list = hdPvMapper.queryAcographyPkPi(paramMap);
        return list;
    }

    /**
     * 保存患者登记信息
     * @param param
     * @param user
     */
	public void savePvHd(String param,IUser user){
		SavePiVo vo=JsonUtil.readValue(param, SavePiVo.class);
		if(vo==null)
			throw new BusException("未获取到要保存患者透析登记信息");	
		Map<String,Object> bedHd=new HashMap<String,Object>();
		Date date=new Date();
		User u=UserContext.getUser();
		//患者信息
		PiMaster pi=vo.getPi();
		//查询原来床位信息
		Map<String, Object> oldMap = DataBaseHelper.queryForMap("select pk_hdbed,pk_pi from BD_RES_HDBED  where pk_pi = ?", pi.getPkPi());
		if(oldMap != null){
			//查询要修改的床位信息
			Map<String, Object> newMap = DataBaseHelper.queryForMap("select pk_hdbed,code_bed,eu_status,pk_pi from BD_RES_HDBED where pk_hdbed=?", vo.getPvd().getPkHdbed());
			//验证床位是否被占用
			if(!(oldMap.get("pkHdbed").toString().equals(newMap.get("pkHdbed").toString()))){
				if("1".equals(newMap.get("euStatus").toString())){
					throw new BusException("床位:"+ newMap.get("euStatus").toString() +"被占用，保存失败！");
				}
			}	
		}
		
		if(pi!=null){
			if(pi.getPkPi()!=null) hdPvMapper.updatePiMaster(pi);
		}
		//就诊记录
		PvEncounter pve=vo.getPve();
		if(pve!=null){
			pve.setEuPvtype("1");
			pve.setEuPvmode("21");
			pve.setEuStatus("0");
			pve.setFlagIn("0");
			pve.setFlagSettle("0");
			pve.setFlagCancel("0");
			pve.setEuStatusFp("0");
			pve.setDateBegin(date);
			pve.setDateAdmit(date);
			DataBaseHelper.insertBean(pve);
			bedHd.put("pkPi", pve.getPkPi());
		}
		//门诊属性
		PvOp pvo = vo.getPvo();
		if(pvo != null){
			/** 根据患者主键获取最大的就诊次数(门诊) */
			String sql = "select nvl(max(i.op_times),0) as op_times from pv_op i inner join pv_encounter e on i.pk_pv = e.pk_pv and e.del_flag = '0' where i.del_flag = '0' and e.pk_pi = ? ";
			Integer opTimes = DataBaseHelper.queryForScalar(sql, Integer.class,pve.getPkPi());
			pvo.setOpTimes(new Long(opTimes + 1));
			pvo.setDateBegin(date);
			pvo.setDateEnd(ApplicationUtils.getPvDateEnd(date));
			pvo.setPkPv(pve.getPkPv());
			pvo.setFlagFirst("0");
			pvo.setFlagNorelease("0");
			pvo.setEuRegtype("0");
			DataBaseHelper.insertBean(pvo);
		}
		
		//透析记录
		PvHd pvd=vo.getPvd();
		if(pvd!=null){
			pvd.setEuStatusHd("0");
			pvd.setPkPv(pve.getPkPv());
			DataBaseHelper.insertBean(pvd);
			bedHd.put("pkHdbed", pvd.getPkHdbed());
			//床位状态更新
			DataBaseHelper.update("update BD_RES_HDBED set eu_status='1',pk_pi=:pkPi where pk_hdbed=:pkHdbed ", bedHd);
		}
		//透析档案
		PiHd pid=vo.getPid();
		if(pid!=null){
			//DataBaseHelper.insertBean(pid);
			if(pid.getDateFirst()==null || pid.getDateFirst().equals("")){
				pid.setDateFirst(date);
			}
			DataBaseHelper.update("update pi_hd set date_first=:dateFirst,eu_status='1' where pk_pi=:pkPi ", pid);
		}
		
		//诊断信息
		PvDiag diagvo=vo.getDiagVo();
		if(diagvo!=null){
			String code=diagvo.getCodeIcd();
			String name=diagvo.getNameDiag();
			String sql="select pk_diag from bd_term_diag where del_flag='0' and diagcode=? and diagname=?";
			List<Map<String,Object>> termDiag=DataBaseHelper.queryForList(sql, new Object[]{code,name});
			if(termDiag!=null && termDiag.get(0)!=null &&termDiag.get(0).get("pkDiag")!=null){
				diagvo.setPkDiag(termDiag.get(0).get("pkDiag").toString());
			}
			diagvo.setPkPvdiag(NHISUUID.getKeyId());
			diagvo.setPkOrg(u.getPkOrg());
			diagvo.setPkPv(pve.getPkPv());
			diagvo.setSortNo(1L);
			diagvo.setDtDiagtype("0000");
			diagvo.setDescDiag(name);
			diagvo.setFlagMaj("1");
			diagvo.setFlagSusp("0");
			diagvo.setFlagContagion("0");
			diagvo.setDateDiag(date);
			diagvo.setPkEmpDiag(u.getPkEmp());
			diagvo.setNameEmpDiag(u.getNameEmp());
			diagvo.setFlagFinally("0");
			diagvo.setFlagCure("0");
			diagvo.setCreator(u.getNameEmp());
			diagvo.setCreateTime(date);
			diagvo.setDelFlag("0");
			diagvo.setTs(date);
			DataBaseHelper.insertBean(diagvo);
		}
	}
	
	/**
	 * 查询当前患者的透析治疗记录，修改并保存数据
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> queryTreatmentRecord(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null ||(CommonUtils.isNull(map.get("pkPvhd"))&&CommonUtils.isNull(map.get("pkHdbed"))))
			throw new BusException("未获取到要查询的患者信息");
		List<Map<String,Object>> result=hdPvMapper.queyrPiRecord(map);
		return result;
	}
	
	public void saveTreatmentRecord(String param,IUser user){
		PvHd pvhd = JsonUtil.readValue(param, PvHd.class);
		if(pvhd==null)
			throw new BusException("未获取到要保存的信息");
		
		if(!StringUtils.isBlank(pvhd.getPkHdbed())){//结束治疗-取消治疗-不修改允许床位
			//查询原来床位信息
			Map<String, Object> oldMap = DataBaseHelper.queryForMap("select a.pk_hdbed,b.pk_pi,b.pk_pv from BD_RES_HDBED a inner join pv_encounter b on  a.pk_pi=b.pk_pi and b.pk_pv=?", pvhd.getPkPv());
			//查询要修改的床位信息
			Map<String, Object> newMap = DataBaseHelper.queryForMap("select pk_hdbed,code_bed,eu_status,pk_pi from BD_RES_HDBED where pk_hdbed=?", pvhd.getPkHdbed());
			newMap.put("bedNo", newMap.get("codeBed").toString());
			newMap.put("pkPi", oldMap.get("pkPi").toString());
			newMap.put("pkPv", oldMap.get("pkPv").toString());
			//验证床位是否被占用
			if(!(oldMap.get("pkHdbed").toString().equals(newMap.get("pkHdbed").toString()))){
				if("1".equals(newMap.get("euStatus").toString())){
					throw new BusException("床位已被占用，保存失败！");
				}
				//床位发生变化后修改
				//更新新床位信息
				DataBaseHelper.update("update BD_RES_HDBED set pk_pi=:pkPi, eu_status='1' where pk_hdbed=:pkHdbed", newMap);
				//清空原床位占用信息
				DataBaseHelper.update("update BD_RES_HDBED set pk_pi=null, eu_status='0' where pk_hdbed=:pkHdbed", oldMap);
			}
			//更新患者就诊记录信息
			DataBaseHelper.update("update pv_encounter set bed_no=:bedNo where pk_pv=:pkPv", newMap);
			DataBaseHelper.update("update pv_hd set pk_dept=:pkDept, pk_dept_ns=:pkDeptNs,pk_hdbed=:pkHdbed,pk_dateslot=:pkDateslot,pk_emp_hd=:pkEmpHd,name_emp_hd=:nameEmpHd,pk_emp_ns=:pkEmpNs,name_emp_ns=:nameEmpNs,dt_hdtype=:dtHdtype,date_begin=:dateBegin where pk_pvhd=:pkPvhd", pvhd);
		}else{
			//更新透析记录信息
			DataBaseHelper.update("update pv_hd set pk_dept=:pkDept, pk_dept_ns=:pkDeptNs,pk_dateslot=:pkDateslot,pk_emp_hd=:pkEmpHd,name_emp_hd=:nameEmpHd,pk_emp_ns=:pkEmpNs,name_emp_ns=:nameEmpNs,dt_hdtype=:dtHdtype,date_begin=:dateBegin where pk_pvhd=:pkPvhd", pvhd);
		}
	}
	
	/**
	 * 查询患者就诊信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryMedicalInformation(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null ||(CommonUtils.isNull(map.get("pkPvhd"))&&CommonUtils.isNull(map.get("pkHdbed"))))
			throw new BusException("未获取到要查询的患者信息");
		List<Map<String,Object>> result=hdPvMapper.queryPiVis(map);
		return result;
	}
	
	/**
	 * 查询患者就诊信息（含结束治疗）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryMedicalInformationOrEnd(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null ||(CommonUtils.isNull(map.get("pkPvhd"))&&CommonUtils.isNull(map.get("pkHdbed"))))
			throw new BusException("未获取到要查询的患者信息");
		List<Map<String,Object>> result=hdPvMapper.queryPiVisOrEnd(map);
		return result;
	}
	
	/**
	 * 结束治疗
	 * @param param
	 * @param user
	 */
	public void saveEndTherapy(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null)
			throw new BusException("未获取到要查询的患者信息");
		//更新患者状态
		if(CommonUtils.isNull(map.get("pkPvhd")))
			throw new BusException("未获取到要查询的患者信息");

		//更新患者状态
		String sql="update pv_hd set EU_STATUS_HD='8' where pk_pvhd=:pkPvhd";
		DataBaseHelper.update(sql,map);
		//床位状态更新
		DataBaseHelper.update("update BD_RES_HDBED set eu_status='0' , pk_pi=null where pk_hdbed=:pkHdbed ", map);
		
	}
	
	/**
	 * 取消治疗
	 * @param param
	 * @param user
	 */
	public void saveCancelTherapy(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null)
			throw new BusException("未获取到要查询的患者信息");
		//查询未结费用
		if(CommonUtils.isNull(map.get("pkPv")))
			throw new BusException("未获取到要查询的患者信息");
		List<Map<String,Object>> useAmt=hdCgMapper.queryUseAmt(map);
		Double amount=(double)0;		
		if(useAmt!=null && useAmt.size()!=0 && useAmt.get(0)!=null && !CommonUtils.isNull(useAmt.get(0).get("amount")))
			amount=Double.parseDouble(useAmt.get(0).get("amount").toString());
		if(amount>0)
			throw new BusException("该患者已有费用发生，无法取消！");
		
		//更新患者状态
		if(CommonUtils.isNull(map.get("pkPvhd")))
			throw new BusException("未获取到要查询的患者信息");
		String sql="update pv_hd set EU_STATUS_HD='9' where pk_pvhd=:pkPvhd";
		DataBaseHelper.update(sql,map);
		//床位状态更新
		DataBaseHelper.update("update BD_RES_HDBED set eu_status='0' , pk_pi=null where pk_hdbed=:pkHdbed ", map);
	}
	
	/**
     * 查询最近一次透析治疗记录  交易号【003004001034】
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> queryLatelyTreatmentRecord(String param,IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
        if(paramMap==null||paramMap.get("pkPi")==null||paramMap.get("pkPi").equals("")){
            throw new BusException("未获取到患者就诊标识pkPi");
        }
        Map<String,Object>  map = hdPvMapper.queryLatelyTreatmentRecord(paramMap);
        return map;
    }
}
