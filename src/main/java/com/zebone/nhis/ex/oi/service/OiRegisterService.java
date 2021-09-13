package com.zebone.nhis.ex.oi.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.oi.BdPlaceIv;
import com.zebone.nhis.common.module.ex.oi.ExInfusionOcc;
import com.zebone.nhis.common.module.ex.oi.ExInfusionRegDt;
import com.zebone.nhis.common.module.ex.oi.ExInfusionRegister;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ex.oi.dao.OiRegisterMapper;
import com.zebone.nhis.ex.oi.vo.BdPacleIvVo;
import com.zebone.nhis.ex.oi.vo.OiBlOpDtVO;
import com.zebone.nhis.ex.oi.vo.OiOrderChargeVO;
import com.zebone.nhis.ex.oi.vo.OiPatientInfoVo;
import com.zebone.nhis.ex.oi.vo.OiRegisterVO;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊输液登记 --功能已废弃
 * @author chy
 *
 */
@Service
public class OiRegisterService {

	@Autowired      
	private BdSnService bdSnService;
	
	@Autowired   
	public OiRegisterMapper OiRegister;
	
	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	@Resource
	private OpCgPubService opCgPubService;
	
	public String getInfuSn(String TbName, String FdName, String Mode, Integer Step, User u)
	{
		String ret = "";
		String retFormat0 = "0000000000";
		//"ex_infusion_register", "register_no",
		String sql = "";
		Integer iSqlCount = 0;
		Integer id = 0;
		if (Mode.equals("0"))
		{
			id = bdSnService.getSerialNo(TbName, FdName, Step, u);
			ret = retFormat0 + id.toString();
			ret = ret.substring(ret.length() - retFormat0.length() + 1, ret.length());
		}
		else if (Mode.equals("1"))
		{
			sql = "select count(*) from ex_infusion_occ where to_char(date_receive, 'YYYYMMDD') = to_char(?, 'YYYYMMDD')";
			iSqlCount = DataBaseHelper.queryForScalar(sql, Integer.class, new Date());
			if (iSqlCount == 0)
			{
				ret = Step.toString();
				sql = "update bd_serialno set val = 2, val_init = 0 where name_tb = ? and name_fd = ?";
				DataBaseHelper.update(sql, TbName, FdName);
			}
			else
			{
				id = bdSnService.getSerialNo(TbName, FdName, Step, u);
				ret = id.toString();
			}
		}
		return ret;
	}
	/***
	 * 获得门诊患者输液登记列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getRegisterPatientList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		//先调用医嘱信息插入到输液临时表数据
		//OiRegister.InsertOiRegisterTempList(map);
		
		if (map.get("infuMode") == null || map.get("infuMode").toString().equals(""))
			map.put("infuMode", "0,1,2,3,4,5,6,7,8,9");
		if	(map.get("qryType").toString().equals("1")) 
			return OiRegister.getOiRegisterPatientList(map);
		else 
			return OiRegister.getRegisteredPatientList(map);	
	}
	
	/***
	 * 获得门诊患者列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getPatientList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OiRegister.getPatientList(map);//获得未登记患者列表
	}	
	/***
	 * 获得未登记处方列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getOrderList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if (map.get("infuMode") == null || map.get("infuMode").toString().equals(""))
			map.put("infuMode", "0,1,2,3,4,5,6,7,8,9");
		//获得患者列表
		return OiRegister.getOrderList(map);
	}		
	/***
	 * 获得处方收费列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getPresChargeList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		//获得患者列表
		if (map.get("infuMode") == null || map.get("infuMode").toString().equals(""))
			map.put("infuMode", "0,1,2,3,4,5,6,7,8,9");
		return OiRegister.getPresChargeList(map);
	}	
	/**
	 * 获取处方费用信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> getPresChargeVo(String param,IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null || map.get("pkCgop") == null ||"".equals(map.get("pkCgop"))){
			throw new BusException("未获取到费用明细主键！");
		}
		return OiRegister.getPresChargeVo(map);
	}
	/***
	 * 获得输液大厅的可用床位列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getRegPlaceIvList(String param, IUser user){
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null||map.get("pkDeptiv")==null)
			throw new BusException("未获取到输液大厅！");
		if(map.get("pkPi") ==null){
			map.put("pkPi", "");
		}
		List<Map<String, Object>> res = OiRegister.getRegPlaceIvList(map);
		return res;
	}		
	
	
	/***)
	 * 输液登记
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionOcc> saveRegister(String param, IUser user){
		OiRegisterVO RegisterVo = JsonUtil.readValue(param, new TypeReference<OiRegisterVO>(){} );
		OiPatientInfoVo PatientInfo = RegisterVo.getRegisterInfo();
		String sql = "";
		//取消登记；
		if (!StringUtils.isEmpty(PatientInfo.getUnRegisterFlag()) && PatientInfo.getUnRegisterFlag().equals("1"))  
		{
			sql = "select count(*) from ex_infusion_reg_dt where pk_infureg = :pkInfureg and remain_times < exec_times ";
			int iSqlCount = DataBaseHelper.queryForScalar(sql, Integer.class, PatientInfo.getPkInfureg());
			if (iSqlCount > 0)
			{
				throw new BusException("登记明细中已经存在已执行完毕的项目，无法取消登记，请核对！");
			}
			sql = "select count(*) from ex_infusion_occ where pk_infureg = :pkInfureg and date_exec is not null ";
			iSqlCount = DataBaseHelper.queryForScalar(sql, Integer.class, PatientInfo.getPkInfureg());
			if (iSqlCount > 0)
			{
				throw new BusException("执行明细中已经存在已开始执行的项目，无法取消登记，请核对！");
			}			
			sql = "select count(*) from ex_infusion_occ where pk_infureg = :pkInfureg and date_assign is not null ";
			iSqlCount = DataBaseHelper.queryForScalar(sql, Integer.class, PatientInfo.getPkInfureg());
			if (iSqlCount > 0)
			{
				throw new BusException("执行明细中已经存在已配药的项目，无法取消登记，请核对！");
			}		
			sql = "delete from ex_infusion_occ where pk_infureg = :pkInfureg ";
			DataBaseHelper.execute(sql, PatientInfo.getPkInfureg());
			sql = "delete from ex_infusion_reg_dt where pk_infureg = :pkInfureg ";
			DataBaseHelper.execute(sql, PatientInfo.getPkInfureg());
			sql = "delete from ex_infusion_register where pk_infureg = :pkInfureg ";
			DataBaseHelper.execute(sql, PatientInfo.getPkInfureg());
			sql = "update bd_place_iv set eu_status = '0', pk_pi = null, pk_pv = null, pk_infuocc = null "
					+ "where pk_placeiv in (select pk_bed from ex_infusion_occ where pk_infureg = :pkInfureg) ";
			DataBaseHelper.execute(sql, PatientInfo.getPkInfureg());
			return null;
		}
		//以下为登记
		List<CnOrder> lstOrder = RegisterVo.getOrderList();
		sql = "select * from ex_infusion_register where pk_pres = ?";
		List<ExInfusionRegister> lstOiRegister = DataBaseHelper.queryForList(sql, ExInfusionRegister.class, PatientInfo.getPkPres());
		ExInfusionRegister OiRegister = new ExInfusionRegister();
		if (lstOiRegister.size() > 0)
		{
			OiRegister = lstOiRegister.get(0);
			OiRegister.setPkBed(PatientInfo.getPkBed());	
		}
		else  //产生登记表
			OiRegister = SetOiRegInfo(PatientInfo);
		List<ExInfusionRegister> lstSaveReg  = new ArrayList<ExInfusionRegister>();
		lstSaveReg.add(OiRegister);
		//产生登记明细表
		List<ExInfusionRegDt> lstOiRegisterDt = SetOiRegDt(OiRegister, lstOrder);
		//产生输液明细表
		List<ExInfusionOcc> lstOiOcc = SetOiOcc(OiRegister, lstOiRegisterDt);
		
		//提交表
		sql = "update bd_place_iv set eu_status = '1', pk_pi = ?, pk_pv = ?, pk_infuocc = ? where pk_placeiv = ?";
		DataBaseHelper.update(sql, OiRegister.getPkPi(), OiRegister.getPkPv(), lstOiOcc.get(0).getPkInfuocc(), OiRegister.getPkBed());
		if (lstOiRegister.size() == 0)
		    DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExInfusionRegister.class), lstSaveReg);
		else
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExInfusionRegister.class), lstSaveReg);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExInfusionRegDt.class), lstOiRegisterDt);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExInfusionOcc.class), lstOiOcc);
		return lstOiOcc;
	}	
	
	//生成执行明细表
	private List<ExInfusionOcc> SetOiOcc(ExInfusionRegister RegisterInfo,
			List<ExInfusionRegDt> lstRegDt) {
		User user = UserContext.getUser();
		List<ExInfusionOcc> ret = new ArrayList<ExInfusionOcc>();
		List<ExInfusionRegDt> lstRegDt_m = new ArrayList<ExInfusionRegDt>();
		for (ExInfusionRegDt coReg: lstRegDt)
		{
			if	(coReg.getOrdsn().equals(coReg.getOrdsnParent()))
			{
				lstRegDt_m.add(coReg);
			}
		}       
		String sql = "select a.pk_freq, b.code, b.name, a.time_occ, a.week_no, a.sort_no "
				  + "from bd_term_freq_time a inner join bd_term_freq b on a.pk_freq = b.pk_freq "
		          + "where code = ? "
				  + "order by b.code, a.sort_no";
        List<Map<String,Object>> lstFreqTime = DataBaseHelper.queryForList(sql, lstRegDt_m.get(0).getCodeFreq());
		Date StartDate = new Date(); //第一次登记时，置收药时间及收药人  和 排队号
		ExInfusionOcc occOrg = new ExInfusionOcc();
		for (ExInfusionRegDt item: lstRegDt_m)
		{
			for (int i = 1; i <= item.getExecTimes(); i++)
			{
				ExInfusionOcc ExOcc = new ExInfusionOcc();
				ExOcc.setPkInfuocc(NHISUUID.getKeyId());
				ExOcc.setPkInfureg(RegisterInfo.getPkInfureg());
				ExOcc.setRegDtNo(item.getRegDtNo());
				ExOcc.setOccNo(getInfuSn("ex_infusion_occ", "occ_no", "0", 1, user));
				//ExOcc.setOccNo(ApplicationUtils.getCode("0505"));  //输液执行明细号
				ExOcc.setEuStatus("1");  //登记时  收药  并开始治疗
				ExOcc.setExecTimes((long) i);
				ExOcc.setDatePlan(SetOccDatePlan(i, item.getDays(), StartDate, lstFreqTime, occOrg));  //置计划执行时间
				occOrg = ExOcc;
				if	(i == 1)
				{
					//第一次登记时，置收药时间及收药人  和 排队号
					ExOcc.setPkBed(RegisterInfo.getPkBed());
					ExOcc.setDateReceive(StartDate);   
					ExOcc.setEmpReceive(user.getPkEmp());
					ExOcc.setSortNo(getInfuSn("ex_infusion_occ", "sort_no", "1", 1, user));
					//ExOcc.setSortNo(ApplicationUtils.getCode("0507"));  //输液排队号
				}
				ApplicationUtils.setDefaultValue(ExOcc, true);
				ret.add(ExOcc);
			}
		}
		return ret;
	}

	//设置计划执行时间函数
	private Date SetOccDatePlan(int ExTimes, Long Days, Date StartDate, List<Map<String, Object>> lstFreqTime,
			ExInfusionOcc occ) {
		if(1 == ExTimes) return StartDate;
		
		Date rd = null;
		
		for(int i=0;i<lstFreqTime.size();i++){
			Map<String,Object> m = lstFreqTime.get(i);
			String val = (String)m.get("timeOcc");
			Date cd = occ.getDatePlan();
			Date vald = DateUtils.strToDate(DateUtils.dateToStr("yyyy-MM-dd", cd)+" "+val, "yyyy-MM-dd hh:mm:ss");
			if	(cd.getTime() == vald.getTime()){
				if(i == lstFreqTime.size()-1){
					String rds = DateUtils.addDate(occ.getDatePlan(), 1, 3, "yyyy-MM-dd")+ " "+lstFreqTime.get(0).get("timeOcc");
					rd = DateUtils.strToDate(rds,"yyyy-MM-dd hh:mm:ss");
					return rd;
				}else{
					String rds = DateUtils.addDate(occ.getDatePlan(), 0, 3, "yyyy-MM-dd")+ " "+lstFreqTime.get(i + 1).get("timeOcc");
					rd = DateUtils.strToDate(rds,"yyyy-MM-dd hh:mm:ss");
					return rd;
				} 
			}
		}
		
		for(int i=0;i<lstFreqTime.size();i++){
			Map<String,Object> m = lstFreqTime.get(i);
			String val = (String)m.get("timeOcc");
			Date cd = occ.getDatePlan();
			
			String sfd = DateUtils.dateToStr("yyyy-MM-dd", occ.getDatePlan()) + " "+val;
			Date fd = DateUtils.strToDate(sfd, "yyyy-MM-dd hh:mm:ss");
			
			if(fd.getTime() > cd.getTime()){
				if(i == lstFreqTime.size()-1){
					String rds = DateUtils.addDate(occ.getDatePlan(), 1, 3, "yyyy-MM-dd")+ " "+lstFreqTime.get(0).get("timeOcc");
					rd = DateUtils.strToDate(rds,"yyyy-MM-dd hh:mm:ss");
					return rd;
				}else{
					String rds = DateUtils.addDate(occ.getDatePlan(), 0, 3, "yyyy-MM-dd")+ " "+lstFreqTime.get(i+1).get("timeOcc");
					rd = DateUtils.strToDate(rds,"yyyy-MM-dd hh:mm:ss");
					return rd;
				}
			}
			
		}
		
		String rds = DateUtils.addDate(occ.getDatePlan(), 1, 3, "yyyy-MM-dd")+ " "+lstFreqTime.get(0).get("timeOcc");
		rd = DateUtils.strToDate(rds,"yyyy-MM-dd hh:mm:ss");
		return rd;
		
		
		/*double dDay = ExTimes / Days;
		double iIndex = ExTimes % Days;
		if ((int)iIndex == 0)
			iIndex = lstFreqTime.size();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");   
		String sDate = df.format(new Date(StartDate.getTime() + (long)dDay * 24 * 60 * 60 * 1000)) + " "
								+ lstFreqTime.get((int)iIndex - 1).get("timeOcc").toString();
		return DateUtils.strToDate(sDate);*/
	}

	//产生输液登记明细表
	private List<ExInfusionRegDt> SetOiRegDt(ExInfusionRegister RegInfo, List<CnOrder> lstOrder) {
		User user = UserContext.getUser();
		List<ExInfusionRegDt> ret = new ArrayList<ExInfusionRegDt>();
		String sql = "select a.pk_freq, b.code, b.name, count(*) times from bd_term_freq_time a " +
						  "inner join bd_term_freq b on a.pk_freq = b.pk_freq group by  a.pk_freq, b.code, b.name";
		List<Map<String,Object>> lstExecTimes = DataBaseHelper.queryForList(sql);
		sql = "select  a.code,  case when a.dt_excardtype = '3' then '1'  when a.dt_excardtype = '4' then '0' " +
		         "when a.code = '0499' then  '2'  when a.code = '0201' then '3' else '9' end eu_type from bd_supply a";
		List<Map<String,Object>> lstSupplyMode = DataBaseHelper.queryForList(sql);
		List<CnOrder> lstOrderReg = new ArrayList<CnOrder>();
		for (CnOrder coReg: lstOrder)
		{
			if	(coReg.getOrdsn().equals(coReg.getOrdsnParent()))
			{
				CnOrder item = coReg;
				item.setPkDeptExec(getInfuSn("ex_infusion_reg_dt", "reg_dt_no", "0", 1, user));
				//item.setPkDeptExec(ApplicationUtils.getCode("0504"));//输液登记明细号
				lstOrderReg.add(item);
			}
		}
		for (CnOrder co: lstOrder)
		{
			ExInfusionRegDt OiRegisterDt = new ExInfusionRegDt();
			if (co.getOrdsn().equals(co.getOrdsnParent()))
			{
				for (CnOrder coNo: lstOrderReg)
				{
					if	(co.getOrdsn().equals(coNo.getOrdsn()))
					{
						OiRegisterDt.setRegDtNo(coNo.getPkDeptExec()); 
						break;
					}
				}
			}
			else
			{
				for (CnOrder coNo: lstOrderReg)
				{
					if	(co.getOrdsnParent().equals(coNo.getOrdsn()))
					{
						OiRegisterDt.setRegDtNo(coNo.getPkDeptExec()); 
						break;
					}
				}
			}
			OiRegisterDt.setPkInfuregdt(NHISUUID.getKeyId());
			OiRegisterDt.setPkInfureg(RegInfo.getPkInfureg());
			OiRegisterDt.setEuType(GetOiEuType(co, lstSupplyMode));  //0  输液  1 注射  2 皮试 3雾化
			OiRegisterDt.setPkCnord(co.getPkCnord());
			OiRegisterDt.setPkOrd(co.getPkOrd());
			OiRegisterDt.setCodeOrd(co.getCodeOrd());
			OiRegisterDt.setNameOrd(co.getNameOrd());
			OiRegisterDt.setOrdsn(co.getOrdsn());
			OiRegisterDt.setOrdsnParent(co.getOrdsnParent());
			OiRegisterDt.setCodeFreq(co.getCodeFreq());
			OiRegisterDt.setSpec(co.getSpec());
			OiRegisterDt.setDosage(co.getDosage());
			OiRegisterDt.setPkUnitDos(co.getPkUnitDos());
			OiRegisterDt.setCodeSupply(co.getCodeSupply());
			OiRegisterDt.setQuan(co.getQuan());
			OiRegisterDt.setDays(co.getDays());
			OiRegisterDt.setNoteSupply(co.getNoteSupply());
			Long iExecTimes = GetExecTimes(co, lstExecTimes);
			OiRegisterDt.setExecTimes(iExecTimes);
			OiRegisterDt.setRemainTimes(iExecTimes);
			ApplicationUtils.setDefaultValue(OiRegisterDt, true);
			ret.add(OiRegisterDt);
		}
		return ret;
	}

	//获得执行次数（通过频次、天数）
	private Long GetExecTimes(CnOrder co, List<Map<String,Object>> lstExecTimes) {
		Long ret = (long) 1.0;
		for (Map<String,Object> item: lstExecTimes)
		{
			if	(item.get("code").toString().equals(co.getCodeFreq().toString()))
			{
				ret = Long.valueOf(item.get("times").toString()) * co.getDays();
				break;
			}
		}
		return ret;
	}

	//获得输液类型0  输液  1 注射  2 皮试 3雾化
	private String GetOiEuType(CnOrder co, List<Map<String,Object>> lstSupplyMode) {
		String ret = "0";
		for (Map<String,Object> item: lstSupplyMode)
		{
			if	(item.get("code").toString().equals(co.getCodeSupply().toString()))
			{
				ret = item.get("euType").toString();
				break;
			}
		}
		if (co.getCodeSupply() == "0")
			ret = "0";
		return ret;
	}

	//产生输液登记表
	private ExInfusionRegister SetOiRegInfo(OiPatientInfoVo PatientVo)
	{
		User user = UserContext.getUser();
		ExInfusionRegister OiRegister = new ExInfusionRegister();
		OiRegister.setPkInfureg(NHISUUID.getKeyId());
		OiRegister.setRegisterNo(getInfuSn("ex_infusion_register", "register_no", "0", 1, user));
		//OiRegister.setRegisterNo(ApplicationUtils.getCode("0506"));  //输液登记号
		OiRegister.setPkPv(PatientVo.getPkPv());
		OiRegister.setPkPi(PatientVo.getPkPi());
		OiRegister.setPkPres(PatientVo.getPkPres());
		//OiRegister.setPkSettle(PatientVo.getPkSettle());
		OiRegister.setEuStatus("1");  //新登记时  收药，状态为已治疗
		OiRegister.setDateReg(new Date());
		OiRegister.setEmpReg(user.getPkEmp());
		OiRegister.setPkBed(PatientVo.getPkBed());	
		ApplicationUtils.setDefaultValue(OiRegister, true);
		return OiRegister;
	}
	/***
	 * 保存输液新增的费用
	 * @param param
	 * @param user
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void savePresCharge(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		OiOrderChargeVO ChargeListAll = JsonUtil.readValue(param, new TypeReference<OiOrderChargeVO>(){} );
		List<OiBlOpDtVO> ChargeList = ChargeListAll.getOrderChargeList();
		List<OiBlOpDtVO> ChargeDelList = ChargeListAll.getOrderChargeDelList();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		if (ChargeDelList!=null && ChargeDelList.size() > 0){
			for (OiBlOpDtVO item: ChargeDelList)
			{
				BlOpDt bod = new BlOpDt();
				BeanUtils.copyProperties(bod, item);
				DataBaseHelper.deleteBeanByPk(bod);
			}
		}
		List<BlPubParamVo> bldtlist = new ArrayList<BlPubParamVo>();
		for (OiBlOpDtVO item: ChargeList){
			if (Constants.RT_NEW.equals(item.getRowStatus()) ){
				BlPubParamVo paramvo = new BlPubParamVo();
				paramvo.setBatchNo(item.getBatchNo());
				paramvo.setDateExpire(item.getDateExpire());
				paramvo.setDateHap(item.getDateHap());
				paramvo.setEuPvType("0");//就诊类型设置为门诊
				paramvo.setFlagPd(item.getFlagPd());
				paramvo.setFlagPv(item.getFlagPv());
				paramvo.setSpec(item.getSpec());
				paramvo.setQuanCg(item.getQuanCg());
				paramvo.setPriceCost(item.getPriceCost());
				paramvo.setPrice(item.getPrice());
				paramvo.setPkUnitPd(item.getPkUnitPd());
				paramvo.setPkPv(item.getPkPv());
				paramvo.setPkPres(item.getPkPres());
				paramvo.setPkPi(item.getPkPi());
				paramvo.setPkOrgEx(item.getPkOrgEx());
				paramvo.setPkOrgApp(item.getPkOrgApp());
				paramvo.setPkOrg(item.getPkOrg());
				paramvo.setPkOrd(item.getPkPd());
				paramvo.setPkItem(item.getPkItem());
				paramvo.setPkEmpCg(item.getPkEmpCg());
				paramvo.setPkEmpApp(item.getPkEmpApp());
				paramvo.setPkDeptEx(item.getPkDeptEx());
				paramvo.setPkDeptCg(item.getPkDeptCg());
				paramvo.setPkDeptApp(item.getPkDeptApp());
				paramvo.setPkCnord(item.getPkCnord());
				paramvo.setPackSize(item.getPackSize());
				paramvo.setNamePd(item.getNameCg());
				paramvo.setNameEmpApp(item.getNameEmpApp());
				paramvo.setNameEmpCg(item.getNameEmpCg());
				bldtlist.add(paramvo);
			}else if(Constants.RT_UPDATE.equals(item.getRowStatus())){
				mapParam.clear();
				if(item.getPkItem()==null||"".equals(item.getPkItem()))
					throw new BusException("未获取到收费项目主键，无法完成保存操作，请删除界面空行后重试！");
				mapParam.put("pkItem", item.getPkItem());
				mapParam.put("pkOrg", item.getPkOrg());
				mapParam.put("euType", EnumerateParameter.ZERO);// 目前只分门诊发票和住院发票
				mapParam.put("flagPd", item.getFlagPd());
				Map<String, Object> mapBillCode = cgQryMaintainService.qryBillCodeByPkItem(mapParam);
				if (mapBillCode != null)
				{
					//throw new BusException("【" + item.getNameCg() + "】" + "  未维护费用分类对应发票分类的账单码");
					item.setCodeBill(mapBillCode.get("code").toString());
		  		    Map<String, Object> mapAccountCode = cgQryMaintainService.qryAccountCodeByPkItem(mapParam);
		  		    item.setCodeAudit(mapAccountCode == null ? null : mapAccountCode.get("code").toString());
				}
			}
			
			BlOpDt bod = new BlOpDt();
			BeanUtils.copyProperties(bod, item);
			DataBaseHelper.updateBeanByPk(bod, false);
		}
		if(bldtlist!=null&&bldtlist.size()>0){
			BlPubReturnVo blrtn = opCgPubService.blOpCgBatch(bldtlist);
			if(blrtn.getBods()!=null&&blrtn.getBods().size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), blrtn.getBods());
			}
		}
		
	}				
}
