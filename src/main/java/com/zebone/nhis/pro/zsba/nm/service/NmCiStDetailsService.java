package com.zebone.nhis.pro.zsba.nm.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.nm.dao.NmChargeItemMapper;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStDetailsMapper;
import com.zebone.nhis.pro.zsba.nm.vo.NmChargeItem;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiStDetails;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class NmCiStDetailsService {
	
	
	@Autowired
	private NmChargeItemMapper itemMapper;
	@Autowired
	private NmCiStDetailsMapper stDetailsMapper;
	
	
	/**
	 * 获取
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public NmCiStDetails getNmCiStDetails(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkCiStd = paramMap.get("pkCiStd");
		
		NmCiStDetails sd = stDetailsMapper.getById(pkCiStd.trim());
		
		return sd;
	}
	

	/**
	 * 根据查询条件筛选数据集
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<NmCiStDetails> findByPropertys(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		if(StringUtils.isEmpty(params.get("pkOrg"))){
			params.put("pkOrg", UserContext.getUser().getPkOrg());
		}
		return stDetailsMapper.findByPropertys(params);
	}
	
	/**
	 * 保存或修改
	 * @param param
	 * @param user
	 * @return 
	 */
	public void saveOrUpdate(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		NmCiStDetails entity = JsonUtil.readValue(param, NmCiStDetails.class);
		if(entity!=null){
			if(StringUtils.isNotEmpty(entity.getPkDept())
					&& StringUtils.isNotEmpty(entity.getPkPv()) && StringUtils.isNotEmpty(entity.getNamePi())
					&& StringUtils.isNotEmpty(entity.getCodePv()) && entity.getTimes()!=null
					&& StringUtils.isNotEmpty(entity.getDateAnnal()) && entity.getNumOrd()!=null
							&& StringUtils.isNotEmpty(entity.getPkCi()) && StringUtils.isNotEmpty(entity.getPvType())){
				
				NmChargeItem ci = itemMapper.getById(entity.getPkCi());
				if(ci==null){
					throw new BusException("收费项目不存在！");
				}
				
				//录入科室为空，则将患者所在科室设置到录入科室；
				if(StringUtils.isEmpty(entity.getInputDept())){
					entity.setInputDept(entity.getPkDept());
				}
				
				entity.setCiPrice(ci.getPrice());
				entity.setIsSett("0");
				entity.setIsPay("0");
				entity.setAnnalCode(UserContext.getUser().getCodeEmp());
				entity.setAnnalName(UserContext.getUser().getNameEmp());
				entity.setPkOrg(UserContext.getUser().getPkOrg());
				
				BigDecimal total = entity.getNumOrd().multiply(entity.getCiPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
				entity.setTotal(total);
				
				if(StringUtils.isNotEmpty(entity.getPkCiStd())){
					NmCiStDetails std = stDetailsMapper.getById(entity.getPkCiStd());
					if(std!=null && "0".equals(std.getIsSett())){
						ApplicationUtils.setDefaultValue(entity, false);
						entity.setModityTime(new Date());
						stDetailsMapper.updateCiStd(entity);
					}else{
						throw new BusException("该计费不存在或不是未结算状态，不能修改！");
					}
				}else{
					ApplicationUtils.setDefaultValue(entity, true);
					entity.setModityTime(entity.getCreateTime());
					stDetailsMapper.saveCiStd(entity);
				}
			}else{
				throw new BusException("必填参数不能为空！");
			}
		}else{
			throw new BusException("解析参数未能获得收费明细对象数据！");
		}
	}
	
	/**
	 * 批量删除
	 * {"pkCiStds":"计费主键集"}
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void deleteBatch(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkCiStds = paramMap.get("pkCiStds");
		
		if(StringUtils.isNotEmpty(pkCiStds)){
			String[] ids = pkCiStds.split(",");
			
			for(String pkCiStd : ids){
				stDetailsMapper.deleteCiStd(pkCiStd);
			}
			
		}else{
			throw new BusException("必填参数不能为空！");
		}
	}

}
