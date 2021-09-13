package com.zebone.nhis.pro.zsba.mz.bl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.PiLockDt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.pro.zsba.mz.bl.dao.ZsBaOpCgSettleMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 中山市博爱医院门诊结算客户化服务
 */
@Service
public class ZsBaOpCgSettleService {

    @Resource
    private ZsBaOpCgSettleMapper zsBaOpCgSettleMapper;
    
    /**
     * 欠费解锁
     * 交易码：022006006010->022003027006
     * @param param
     * @param user  操作用户
     */
    public void unlockPi(String param, IUser user){
    	Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPi = CommonUtils.getPropValueStr(paramMap, "pkPi");
        if(CommonUtils.isEmptyString(pkPi)) {
        	return;
        }
        User u = (User)user;
        int cnt = zsBaOpCgSettleMapper.getUnSettleRecordNum(pkPi);
        //不存在48小时内未缴费记录，则解锁
        if(cnt<=0){
            int count = DataBaseHelper.execute("delete from pi_lock where pk_pi = ? And eu_locktype = '3'",pkPi);
            //解锁成功后写入解锁明细
            if(count > 0) {
            	//写入解锁明细
                PiLockDt lockDt = new PiLockDt();
                lockDt.setDateLock(new Date());
                lockDt.setPkPi(pkPi);
                lockDt.setEuDirect("-1");
                lockDt.setEuLocktype("3");
                lockDt.setPkEmpOpera(u.getPkEmp());
                lockDt.setNameEmpOpera(u.getNameEmp());
                lockDt.setPkOrg(u.getPkOrg());
                DataBaseHelper.insertBean(lockDt);
            }   
        }

    }

    /**
     * 查询患者的退费结算记录
     * 交易号：022006006004->022003027001
     * @return
     */
    public List<Map<String, Object>> qryPatiRefSettle(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPi = CommonUtils.getPropValueStr(paramMap, "pkPi");
        if(CommonUtils.isEmptyString(pkPi)) {
        	return null;
        }
        return zsBaOpCgSettleMapper.qryPatiRefSettle(pkPi);
    }
    
    /**
     * 交易号：022006006005->022003027002
     * 查询患者的退费结算明细
     * @return
     */
    public List<Map<String, Object>> qryPatiRefSettleDt(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkSettle = CommonUtils.getPropValueStr(paramMap, "pkSettle");
        if(CommonUtils.isEmptyString(pkSettle)) {
        	return null;
        }
        return zsBaOpCgSettleMapper.qryPatiRefSettleDt(pkSettle);
    }
    
    /**
	 * 交易号：022006006006->022003027003
	 * 复制退费信息
	 * @param param
	 * @param user
	 */
	public void copyBlOpDtInfo(String param,IUser user){
		List<String> pkCgopList =JsonUtil.readValue(param, new TypeReference<List<String>>(){});
		if(pkCgopList == null || pkCgopList.size() < 1){
            return;
        }
		
		//1、根据记费主键查询记费信息
		List<BlOpDt> blOpDtList = zsBaOpCgSettleMapper.qryblOpDtList(pkCgopList);

		List<BlOpDt> blOpDtNews = new ArrayList<>();
		if(blOpDtList != null && blOpDtList.size() > 0) {
			//2、根据原始记费记录，生成新的记费记录
			for(BlOpDt oriOpDt : blOpDtList){
				BlOpDt blOpDtNew = new BlOpDt();
				ApplicationUtils.copyProperties(blOpDtNew, oriOpDt);
				blOpDtNew.setQuan(oriOpDt.getQuan());
				blOpDtNew.setAmountPi(Math.abs(oriOpDt.getAmountPi()));
				blOpDtNew.setAmount(Math.abs(oriOpDt.getAmount()));
				blOpDtNew.setAmountHppi(Math.abs(oriOpDt.getAmountHppi()));
				blOpDtNew.setFlagSettle(EnumerateParameter.ZERO);
				blOpDtNew.setPkSettle(null);
				blOpDtNew.setFlagRecharge("1");
				blOpDtNew.setPkInvoice(null);
				blOpDtNew.setPkCgopBack(null);
				blOpDtNew.setPkCgopOld(oriOpDt.getPkCgop());
				blOpDtNew.setDateCg(new Date());
				ApplicationUtils.setDefaultValue(blOpDtNew, true);
				blOpDtNews.add(blOpDtNew);
			}
		}
		
		//3、插入新的记费记录
		if(blOpDtNews!=null && blOpDtNews.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), blOpDtNews);
		}
	}
	
	/**
	 * 交易号：022006006016->022003027004
	 * 查询交款记录
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> getPaymentRecords(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String  dateBegin = CommonUtils.getPropValueStr(paramMap, "dateBegin");
		if(CommonUtils.isNotNull(dateBegin)){
			paramMap.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		}
		String  dateEnd = CommonUtils.getPropValueStr(paramMap, "dateEnd");
		if(CommonUtils.isNotNull(dateEnd)){
			paramMap.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		}
		if(EnumerateParameter.NINE.equals(CommonUtils.getPropValueStr(paramMap, "euRemove"))) {
			paramMap.remove("euRemove");
		}
        return zsBaOpCgSettleMapper.getPaymentRecords(paramMap);
	}
	
	 /**
	 * 交易号：022006006017->022003027005
	 * 保存挂账销账
	 * @param param
	 * @param user
	 */
	public void saveWriteOffAccount(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String sql = "update bl_deposit  set eu_remove='1', pk_emp_remove=?,date_remove=?, note_remove=?  where pk_depo=? ";
		String pkDepo = CommonUtils.getPropValueStr(paramMap, "pkDepo");
		String pkEmpRemove = CommonUtils.getPropValueStr(paramMap, "pkEmpRemove");
		String dateRemove = CommonUtils.getPropValueStr(paramMap, "dateRemove");
		String noteRemove = CommonUtils.getPropValueStr(paramMap, "noteRemove");
		Date strToDate = DateUtils.strToDate(dateRemove);
		DataBaseHelper.update(sql, new Object[]{pkEmpRemove,strToDate,noteRemove,pkDepo});
	}
	
	/**
	 * 
	 * 收费员增加就诊
	 * ZsBaOpCgSettleService.addPvEncounter
	 * 交易号：022003027136
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> addPvEncounter(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (!paramMap.containsKey("codeOp") || StringUtils.isEmpty(CommonUtils.getString(paramMap.get("codeOp")))) {
			throw new BusException("参数【codeOp】不能为空。");
		}
		if (!paramMap.containsKey("codeEmp") || StringUtils.isEmpty(CommonUtils.getString(paramMap.get("codeEmp")))) {
			throw new BusException("参数【codeEmp】不能为空。");
		}
		if (!paramMap.containsKey("codeDept") || StringUtils.isEmpty(CommonUtils.getString(paramMap.get("codeDept")))) {
			throw new BusException("参数【codeDept】不能为空。");
		}
		if (!paramMap.containsKey("pkInsu") || StringUtils.isEmpty(CommonUtils.getString(paramMap.get("pkInsu")))) {
			throw new BusException("参数【pkInsu】不能为空。");
		}
		User currUser = UserContext.getUser();
		//获取博爱机构信息
		BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select * from BD_OU_ORG where CODE_ORG=?",BdOuOrg.class,new Object[]{"202"});
		//查询患者信息
		PiMaster piMaster = DataBaseHelper.queryForBean("select * from PI_MASTER where CODE_OP=?",PiMaster.class,new Object[]{CommonUtils.getString(paramMap.get("codeOp"))});
		if(piMaster!=null){
			//查询科室和医生信息
			BdOuDept bdOuDept = DataBaseHelper.queryForBean("select * from BD_OU_DEPT where CODE_DEPT=?",BdOuDept.class,new Object[]{CommonUtils.getString(paramMap.get("codeDept"))});
			BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean("select * from BD_OU_EMPLOYEE where CODE_EMP=?",BdOuEmployee.class,new Object[]{CommonUtils.getString(paramMap.get("codeEmp"))});
			//获取医保计划
			BdHp bdHp = DataBaseHelper.queryForBean("select * from BD_HP where PK_HP=?",BdHp.class,new Object[]{CommonUtils.getString(paramMap.get("pkInsu"))});
			//患者就诊-就诊记录
			PvEncounter pvInfo = new PvEncounter();
			pvInfo.setPkOrg(bdOuOrg.getPkOrg());
			pvInfo.setPkPi(piMaster.getPkPi());
			pvInfo.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
			pvInfo.setEuPvtype("1");//门诊
			pvInfo.setDateBegin(new Date());
			pvInfo.setDateEnd(new Date());
			pvInfo.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_2);//结束
			pvInfo.setFlagIn("0");
			pvInfo.setFlagSettle("0");
			pvInfo.setNamePi(piMaster.getNamePi());
			pvInfo.setDtSex(piMaster.getDtSex());
			pvInfo.setAgePv(ApplicationUtils.getAgeFormat(piMaster.getBirthDate(), null));
			pvInfo.setPkInsu(bdHp.getPkHp());
			pvInfo.setDtMarry(piMaster.getDtMarry());
			pvInfo.setPkPicate(piMaster.getPkPicate());
			pvInfo.setDtPvsource(piMaster.getDtSource());
			pvInfo.setAddress(piMaster.getAddress());
			pvInfo.setPkDept(bdOuDept.getPkDept());
			pvInfo.setDateClinic(new Date());
			pvInfo.setPkEmpPhy(bdOuEmployee.getPkEmp());
			pvInfo.setNameEmpPhy(bdOuEmployee.getNameEmp());
			pvInfo.setEuPvmode("0");
			pvInfo.setEuDisetype("0");
			pvInfo.setFlagSpec("0");
			pvInfo.setPkEmpReg(currUser.getPkEmp());
			pvInfo.setNameEmpReg(currUser.getNameEmp());
			pvInfo.setDateReg(new Date());
			pvInfo.setFlagCancel("0");
			pvInfo.setEuLocked("0");
			DataBaseHelper.insertBean(pvInfo);
			//患者就诊-门诊属性
			PvOp pvOp = new PvOp();
			//获取患者最大就诊次数(本次=最大+1)
			String qryTimesSql = "SELECT ISNULL(MAX(po.OP_TIMES),0)+1 times FROM PV_OP po "+
			"INNER JOIN PV_ENCOUNTER pv ON pv.PK_PV= po.PK_PV "+
			"INNER JOIN PI_MASTER pi ON pi.PK_PI= pv.PK_PI "+
			"WHERE pi.CODE_OP=?";
			Map<String, Object> pvHpMap = DataBaseHelper.queryForMap(qryTimesSql, piMaster.getCodeOp());
			Long opTimes = pvHpMap.get("times")!=null?Long.parseLong(String.valueOf(pvHpMap.get("times"))):1l;
			pvOp.setPkOrg(bdOuOrg.getPkOrg());
			pvOp.setPkPv(pvInfo.getPkPv());
			pvOp.setOpTimes(opTimes);
			pvOp.setPkDeptPv(bdOuDept.getPkDept());
			pvOp.setPkEmpPv(bdOuEmployee.getPkEmp());
			pvOp.setNameEmpPv(bdOuEmployee.getNameEmp());
			pvOp.setDateBegin(new Date());
			pvOp.setDateEnd(new Date());
			pvOp.setTicketno(0l);
			pvOp.setFlagFirst("0");
			pvOp.setPkResCn(null);
			pvOp.setPkSchsrvCn(null);
			pvOp.setDtApptype("0");
			pvOp.setOrderidExt("0");
			pvOp.setOpTimesRel(opTimes);
			pvOp.setFlagAdd("0");
			DataBaseHelper.insertBean(pvOp);
			//患者就诊-保险计划
			PvInsurance pvInsurance = new PvInsurance();
			pvInsurance.setPkOrg(bdOuOrg.getPkOrg());
			pvInsurance.setPkPv(pvInfo.getPkPv());
			pvInsurance.setPkHp(bdHp.getPkHp());
			DataBaseHelper.insertBean(pvInsurance);
			
			Map<String,Object> returnMap = new HashMap<String,Object>();
			returnMap.put("pkPi", piMaster.getPkPi());
			returnMap.put("pkPv", pvInfo.getPkPv());
			return returnMap;
		}else{
			throw new BusException("找不到患者基本信息！");
		}
	}
	
	/**
	 * 查询患者信息
	 * 交易号：022003027137
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryPatientInfo(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = zsBaOpCgSettleMapper.qryPatientInfo(paramMap);
		return list;
	}
}
