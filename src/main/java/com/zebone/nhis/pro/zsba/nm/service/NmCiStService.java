package com.zebone.nhis.pro.zsba.nm.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStDetailsMapper;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStMapper;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiSt;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiStDetails;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class NmCiStService {
	
	@Autowired
	private NmCiStMapper stMapper;
	@Autowired
	private NmCiStDetailsMapper detailsMapper;
	
	/**
	 * 获取
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
 	public NmCiSt getNmCiSt(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkCist = paramMap.get("pkCist");
		
		NmCiSt sd = stMapper.getById(pkCist.trim());
		
		return sd;
	}
	

	/**
	 * 生成结算数据
	 * @param param
	 * @param user
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public void saveSett(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkCiStds = paramMap.get("pkCiStds");
		if(StringUtils.isEmpty(pkCiStds)){
			throw new BusException("必填参数不能为空，请联系管理员！");
		}
		String sql = "select * from nm_ci_st_details where pk_ci_std in ('"+ pkCiStds.replaceAll(",", "','") +"')";
		List<NmCiStDetails> stds = DataBaseHelper.queryForList(sql, NmCiStDetails.class, new Object[]{});
		if(stds.isEmpty()){
			throw new BusException("未找到计费数据，请联系管理员！");
		}
		boolean isSett = false;
		for (NmCiStDetails std : stds) {
			if("1".equals(std.getIsSett()) && std.getPkCiSt()!=null){
				isSett = true;
				break;
			}
		}
		if(isSett){
			throw new BusException("选中的计费明细数据有已结算，请重新查询后再结算。");
		}
		/*
		 * 设置结算数据
		 */
		NmCiSt sett = new NmCiSt();
		BigDecimal amount = new BigDecimal("0.00");
		for(int i=0; i<stds.size(); i++){
			NmCiStDetails std = stds.get(i);
			if(i==0){
				sett.setPkOrg(std.getPkOrg());
				sett.setPkDept(std.getPkDept());
				sett.setInputDept(std.getInputDept());
				sett.setPkPv(std.getPkPv());
				sett.setPvType(std.getPvType());
				sett.setNamePi(std.getNamePi());
				sett.setCodePv(std.getCodePv());
				sett.setTimes(std.getTimes());
			}
			amount = amount.add(std.getTotal()).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		sett.setAmount(amount);
		sett.setIsPush("0");
		sett.setIsPay("0");
		sett.setSettCode(UserContext.getUser().getCodeEmp());
		sett.setSettName(UserContext.getUser().getNameEmp());
		sett.setModityTime(new Date());
		ApplicationUtils.setDefaultValue(sett, true);
		stMapper.saveCiSt(sett);
		/*
		 * 更新计费记录为已结算未支付
		 */
		for (NmCiStDetails std : stds) {
			std.setIsPay("0");
			std.setIsSett("1");
			std.setPkCiSt(sett.getPkCiSt());
			ApplicationUtils.setDefaultValue(std, false);
			detailsMapper.updateCiStd(std);
		}
	}
	
	/**
	 * 批量删除
	 * {"pkCis":"收费项目主键集"}
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void deleteBatch(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkCiSts = paramMap.get("pkCiSts");
		
		if(StringUtils.isNotEmpty(pkCiSts)){
			String[] ids = pkCiSts.split(",");
			
			for(String pkCiSt : ids){
				stMapper.deleteCiSt(pkCiSt);
			}
			
		}else{
			throw new BusException("必填参数不能为空！");
		}
	}

	
}
