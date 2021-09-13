package com.zebone.nhis.ex.oi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.oi.ExInfusionOcc;
import com.zebone.nhis.common.module.ex.oi.ExInfusionOccDt;
import com.zebone.nhis.common.module.ex.oi.ExInfusionReaction;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ex.oi.dao.OiOccMapper;
import com.zebone.nhis.ex.oi.vo.OiPatientInfoVo;
import com.zebone.nhis.ex.oi.vo.OiSaveOccVO;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊输液执行
 * @author chy
 *
 */
@Service
public class OiOccService {
	
	@Autowired   
	public OiRegisterService oiRegisterService;
	
	@Autowired   
	public OiOccMapper OiOcc;
	/***
	 * 获得单次输液汇总列表（按类型分组）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getOccOrderSumList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if (map.get("infuMode") == null || map.get("infuMode").toString().equals(""))
			map.put("infuMode", "0,1,2,3,4,5,6,7,8,9");
		return OiOcc.getOrderSumList(map);
	}	
	
	/***
	 * 获得单次输液处方列表（按类型获取）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getOccOrderList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OiOcc.getOrderList(map);
	}	
	
	/***
	 * 获得执行明细列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getOccExecList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OiOcc.getExecList(map);
	}		
	
	/***
	 * 获得执行明细列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getOccExecDetailList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OiOcc.getExecDetailList(map);
	}			
	/***
	 * 获得不良反应列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getOccReactionList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OiOcc.getReactionList(map);
	}			
	/***
	 * 获得打印信息列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getInfusionPrintList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if	(map.get("printMode").toString().equals("Card"))
			return OiOcc.getPrintCardList(map);
		else
		    return OiOcc.getPrintLabelList(map);
	}			
	/***
	 * 获得执行明细列表
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveOcc(String param, IUser user){
		OiSaveOccVO SaveVo = JsonUtil.readValue(param, new TypeReference<OiSaveOccVO>(){} );
		String sMode = SaveVo.getSaveMode();
		OiPatientInfoVo RegInfo = SaveVo.getRegisterInfo();
		ExInfusionOcc occ = SaveVo.getOccInfo();
		Map<String, Object> SaveParam = SaveVo.getParamMap();
		User user1 = UserContext.getUser();
		String sql = "";
		int iCount = 0;
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.clear();
		mapParam.put("pkPi", RegInfo.getPkPi());
		mapParam.put("pkPv", RegInfo.getPkPv());
		mapParam.put("pkInfuocc", occ.getPkInfuocc());
		mapParam.put("pkBed", occ.getPkBed());
		mapParam.put("pkInfureg", occ.getPkInfureg());	
		mapParam.put("opDate", new Date());
		mapParam.put("pkEmp", user1.getPkEmp());
		mapParam.put("sMode", sMode);
		mapParam.put("note", occ.getEyreRecord());	
		mapParam.put("skinTestRet", occ.getSkinTestRet());
		mapParam.put("datePlan", occ.getDatePlan());
		mapParam.put("commentStr", occ.getCommentStr());
		mapParam.put("regDtNo", occ.getRegDtNo());
		mapParam.put("pkNewBed", (String)SaveParam.get("pkNewBed"));
		if	(sMode.equals("1".toString()))  //保存座位
		{
			if	(occ.getPkBed() != null)
			{
				sql = "update bd_place_iv set pk_pi = null, pk_pv = null, pk_infuocc = null, eu_status = '0' "
						+ "where pk_placeiv = (select pk_bed from ex_infusion_occ where pk_infuocc = :pkInfuocc) ";
				iCount = DataBaseHelper.update(sql, mapParam);
				if	(iCount == 0)
				{
					throw new BusException("更新座位字典时出现错误(=0)，请刷新后核对！");
				}
				if	(iCount > 1)
				{
					throw new BusException("更新座位字典时出现错误(>1)，请刷新后核对！");
				}
			}
			sql = "update bd_place_iv  set pk_pi = :pkPi, pk_pv = :pkPv, pk_infuocc = :pkInfuocc, eu_status = '1' "
					+ "where bd_place_iv.pk_placeiv = :pkNewBed ";

			iCount = DataBaseHelper.update(sql, mapParam);
			if	(iCount == 0)
			{
				throw new BusException("更新座位字典时出现错误(新座位被占用)，请刷新后核对！");
			}			
			sql = "update ex_infusion_occ set pk_bed = :pkNewBed where pk_infuocc = :pkInfuocc ";
			DataBaseHelper.update(sql, mapParam);
			sql = "update ex_infusion_register set pk_bed = :pkNewBed where pk_infureg = :pkInfureg ";
			DataBaseHelper.update(sql, mapParam);
		}
		else if (sMode.equals("2".toString())) //保存皮试结果、失败原因、计划执行时间, 写表cn_order
		{
			sql = "update ex_infusion_occ set skin_test_ret = :skinTestRet, comment_str = :commentStr, date_plan = :datePlan where pk_infuocc = :pkInfuocc";
			DataBaseHelper.update(sql, mapParam);
			sql = "";
			if (mapParam.get("skinTestRet") != null)
			{
				if (mapParam.get("skinTestRet").toString().equals("0")) //皮试结果是阴性 写表cn_order
					sql = "update cn_order set eu_st = '2' where pk_cnord in ("
							+ "  select pk_cnord from ex_infusion_reg_dt "
							+ "  where pk_infureg = :pkInfureg and reg_dt_no = :regDtNo ) ";
				else if (mapParam.get("skinTestRet").toString().equals("1")) //皮试结果是阳性 写表cn_order
					sql = "update cn_order set eu_st = '3' where pk_cnord in ("
							+ "  select pk_cnord from ex_infusion_reg_dt "
							+ "  where pk_infureg = :pkInfureg and reg_dt_no = :regDtNo ) ";
			    if (!sql.equals(""))
			    	DataBaseHelper.update(sql, mapParam);
			}
		}
		else 
		{
			OiOcc.UpdateOcc(mapParam);
		}
		if (sMode.equals("3".toString())) //收药时，设置occ表的排队号 
		{
			mapParam.put("sortNo", oiRegisterService.getInfuSn("ex_infusion_occ", "sort_no", "1", 1, user1));  
			sql = "update ex_infusion_occ set sort_no = :sortNo "
					+ " where pk_infuocc = :pkInfuocc";
			DataBaseHelper.update(sql, mapParam);
		}
		if	(sMode.equals("8".toString())) //完成时，设置登记表的remain_times
		{
			sql = "update ex_infusion_reg_dt set remain_times = remain_times - 1  "
					+ " where pk_infureg = :pkInfureg and reg_dt_no = :regDtNo ";
			//判断 更新登记主表的 eu_status = ‘2’  （完成）
			DataBaseHelper.update(sql, mapParam);
			sql = "update ex_infusion_register set eu_status = case when (select count(*) from ex_infusion_reg_dt reg_dt "
					+ "where ex_infusion_register.pk_infureg = reg_dt.pk_infureg and reg_dt.remain_times > 0) = 0 then '2' else eu_status end "
					+ "where pk_infureg = :pkInfureg"; 
			DataBaseHelper.update(sql, mapParam);
			//清空座位表
			sql = "update bd_place_iv set pk_pi = null, pk_pv = null, pk_infuocc = null, eu_status = '0' "
					+ " where pk_placeiv = :pkBed ";
			DataBaseHelper.update(sql, mapParam);
		}
		//接瓶处理
		if (sMode.equals("9".toString()))
		{
			ExInfusionOccDt occDt = new ExInfusionOccDt();
			occDt.setPkInfuoccdt(NHISUUID.getKeyId());
			occDt.setPkInfuocc(occ.getPkInfuocc());
			occDt.setPkInfureg(occ.getPkInfureg());
			occDt.setRegDtNo(occ.getRegDtNo());
			occDt.setOccNo(occ.getOccNo());
			occDt.setDateOpera(new Date());
			occDt.setEmpOpera(user1.getPkEmp());
			occDt.setEuType("0"); //接瓶操作
			occDt.setCommentStr(SaveParam.get("note").toString());
			ApplicationUtils.setDefaultValue(occDt, true);
			DataBaseHelper.insertBean(occDt);
		}
		//不良反应处理
		else if (sMode.equals("10"))
		{
			ExInfusionReaction rea = new ExInfusionReaction();
			rea.setPkInfurea(NHISUUID.getKeyId());
			rea.setPkInfuocc(occ.getPkInfuocc());
			rea.setPkInfureg(occ.getPkInfureg());
			rea.setDateRecord(new Date());
			rea.setEmpRecord(user1.getPkEmp());
			rea.setPkPi(RegInfo.getPkPi());
			rea.setPkPv(RegInfo.getPkPv());
			rea.setNote(SaveParam.get("note").toString());
			rea.setEuStatus(SaveParam.get("euStatus").toString());
			rea.setPkOrd(null);
			rea.setSpec(null);
			ApplicationUtils.setDefaultValue(rea, true);
			DataBaseHelper.insertBean(rea);
		}
	}			
}
