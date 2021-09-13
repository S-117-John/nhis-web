package com.zebone.nhis.pro.zsba.nm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.nm.dao.NmChargeItemMapper;
import com.zebone.nhis.pro.zsba.nm.vo.NmChargeItem;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class NmChargeItemService {
	
	@Autowired
	private NmChargeItemMapper chargeItemMapper;
	
	/**
	 * 获取
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public NmChargeItem getNmChargeItem(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkCi = paramMap.get("pkCi");
		
		NmChargeItem ci = chargeItemMapper.getById(pkCi.trim());
		
		return ci;
	}
	
	/**
     * 查询出所有记录
     */
    public List<NmChargeItem> findAll(String param , IUser user){
    	return chargeItemMapper.findAll();
    }
    
    /**
     * 查询出所有启用中的记录
     */
    @SuppressWarnings("unchecked")
    public List<NmChargeItem> findByUse(String param , IUser user){
    	param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkDept = paramMap.get("pkDept");
		String showSite = paramMap.get("showSite");
    	return chargeItemMapper.findByUse(pkDept, showSite);
    }
	
	/**
	 * 保存或修改
	 * @param param
	 * @param user
	 * @return 
	 */
	public void saveOrUpdate(String param , IUser user){
		NmChargeItem ci = JsonUtil.readValue(param, NmChargeItem.class);
		if(ci!=null){
			if(StringUtils.isNotEmpty(ci.getCodeItem()) && StringUtils.isNotEmpty(ci.getNameItem())
					 && StringUtils.isNotEmpty(ci.getPyCode()) && StringUtils.isNotEmpty(ci.getSpec()) 
					 && StringUtils.isNotEmpty(ci.getUnit())  && ci.getPrice()!=null
					 && StringUtils.isNotEmpty(ci.getShowSite()) ){ //&& StringUtils.isNotEmpty(ci.getAutoAnnal())
				
				if(StringUtils.isNotEmpty(ci.getPkCi())){
					ci.setModityTime(new Date());
					ApplicationUtils.setDefaultValue(ci, false);
					chargeItemMapper.updateCi(ci);
				}else{
					ApplicationUtils.setDefaultValue(ci, true);
					ci.setModityTime(ci.getCreateTime());
					chargeItemMapper.saveCi(ci);
				}
			}else{
				throw new BusException("必填参数不能为空！");
			}
		}else{
			throw new BusException("解析参数未能获得收费项对象数据！");
		}
	}
	
	/**
	 * 批量删除<逻辑删除>
	 * {"pkCis":"收费项目主键集","delFlag":"0启用/1禁用"}
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void deleteBatch(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkCis = paramMap.get("pkCis");
		String delFlag = paramMap.get("delFlag");
		
		if(StringUtils.isNotEmpty(pkCis) && StringUtils.isNotEmpty(delFlag)){
			String[] ids = pkCis.split(",");
			
			for(String pkCi : ids){
				
				NmChargeItem ci = chargeItemMapper.getById(pkCi.trim());
				ci.setDelFlag(delFlag);
				ApplicationUtils.setDefaultValue(ci, false);
				chargeItemMapper.updateCi(ci);
			}
			
		}else{
			throw new BusException("必填参数不能为空！");
		}
	}

	
	
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	/**
	 * 保存或修改,自动计费任务的科室
	 * @param param
	 * @param user
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public void saveOrUpdateTastDept(String param , IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap!=null){
			if(paramMap.containsKey("pkDepts") && paramMap.containsKey("nameDepts")){
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
				TransactionStatus status = platformTransactionManager.getTransaction(def);
				try {

					//先删
					String delSql = "delete from nm_tast_dept";
					DataBaseHelper.execute(delSql, new Object[]{});
					
					//后增
					String pkDepts = paramMap.get("pkDepts").toString();
					String nameDepts = paramMap.get("nameDepts").toString();
					String[] pkDeptArr = pkDepts.split(",");
					String[] nameDeptArr = nameDepts.split(",");
					String inSql = "INSERT INTO nm_tast_dept(pk_nm_tast, pk_dept, dept_name, creator, create_time, modifier, modity_time, del_flag) VALUES (?, ?, ?, '00000', ?, '00000', ?, '0');";
					for(int i=0; i<pkDeptArr.length; i++){
						DataBaseHelper.execute(inSql, new Object[]{String.valueOf(i+1), pkDeptArr[i], nameDeptArr[i], new Date(), new Date()});
					}
					platformTransactionManager.commit(status);
				} catch (Exception e) {
					platformTransactionManager.rollback(status);
					throw new BusException("保存数据发生异常：{}", e.getMessage());
				}
			}else{
				throw new BusException("必填参数不能为空！");
			}
		}else{
			throw new BusException("解析参数未能获得数据！");
		}
	}
	
	
}
