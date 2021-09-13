package com.zebone.nhis.pro.zsba.rent.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.rent.dao.NmRentDetailsMapper;
import com.zebone.nhis.pro.zsba.rent.dao.NmRentRecordMapper;
import com.zebone.nhis.pro.zsba.rent.vo.NmRentDetails;
import com.zebone.nhis.pro.zsba.rent.vo.NmRentRecord;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 非医疗费用-设备出租记录
 * @author lipz
 *
 */
@Service
public class NmRentRecordService {

	
	@Autowired NmRentRecordMapper rentRecordMapper;
	@Autowired NmRentDetailsMapper rentDetailsMapper;
	
	/**
	 * 获取出租明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public NmRentRecord getNmRentRecord(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkRent = paramMap.get("pkRent");
		
		NmRentRecord entity = rentRecordMapper.getById(pkRent.trim());
		
		return entity;
	}
	

    /**
     * 查询出对应患者的所有启用中的记录 022003010030
     * @param param 
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> findByCodeIp(String param , IUser user){
    	param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String codeIp = paramMap.get("codeIp");
    	return rentRecordMapper.findByCodeIp(codeIp);
    }
	
	/**
	 * 新增保存出租记录 022003010029
	 * @param param
	 * @param user
	 * @return 
	 */
	public void saveRecord(String param , IUser user){
		NmRentRecord entity = JsonUtil.readValue(param, NmRentRecord.class);
		NmRentDetails details = JsonUtil.readValue(param, NmRentDetails.class);
		if(entity!=null && details!=null){
			if(StringUtils.isNotEmpty(entity.getPkApp()) && StringUtils.isNotEmpty(entity.getAppNo())
					&& StringUtils.isNotEmpty(entity.getPkDept()) && StringUtils.isNotEmpty(entity.getPvType())
					&& StringUtils.isNotEmpty(entity.getNamePi()) && StringUtils.isNotEmpty(entity.getCodeIp()) 
					&& entity.getTimes()!=null && entity.getDepoAmt()!=null){
				/*
				 * 保存出租数据
				 */
				entity.setIsPay("0");//未付款
				entity.setIsRefund("0");//未归还
				ApplicationUtils.setDefaultValue(entity, true);
				entity.setModityTime(entity.getCreateTime());
				rentRecordMapper.saveEntity(entity);
				/*
				 * 保存出租明细数据
				 */
				if(details.getTimes()!=null && details.getDayNum()!=null && details.getDateBegin() !=null){
					try {
						details.setPkRent(entity.getPkRent());
					//	details.setDateEnd(DateUtils.addDate(DateUtils.parseDate(details.getDateBegin()), details.getDayNum(), 3, "yyyy-MM-dd"));
						ApplicationUtils.setDefaultValue(details, true);
						details.setModityTime(details.getCreateTime());
												
						rentDetailsMapper.saveEntity(details);
				
						
					} catch (Exception e) {
						throw new BusException("转换结束日期异常：{0}", e.getMessage());
					}
				}else{
					throw new BusException("出租明细必填参数不能为空！");
				}
			}else{
				throw new BusException("出租必填参数不能为空！");
			}
		}else{
			throw new BusException("解析参数未能获得收费项对象数据！");
		}
	}

	/**
	 * 批量删除<逻辑删除> 022003010035
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void deleteBatch(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkRents = paramMap.get("pkRents");
		
		if(StringUtils.isNotEmpty(pkRents)){
			String[] ids = pkRents.split(",");
			
			for(String pkRent : ids){
				NmRentRecord selItem = rentRecordMapper.getById(pkRent.trim());
				if(selItem.getIsPay().equals("1")){
					throw new BusException("已收款记录不能删除！");
				}
				NmRentRecord entity = new NmRentRecord();
				entity.setPkRent(pkRent.trim());
				entity.setDelFlag("1");
				ApplicationUtils.setDefaultValue(entity, false);
				rentRecordMapper.updateEntity(entity);
			}
			
		}else{
			throw new BusException("必填参数不能为空！");
		}
	}
	
	
	/**
	 * 根据出租记录后取所有详情 022003010031
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findDetailsByPkRent(String param , IUser user){
    	param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkRent = paramMap.get("pkRent");
		List<Map<String,Object>> list = rentDetailsMapper.findByPkRent(pkRent);
    	return list;
    }
	
	/**
	 * 设备续租 	022003010032
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveRelet(String param , IUser user){
		NmRentDetails details = JsonUtil.readValue(param, NmRentDetails.class);
		if(StringUtils.isNotEmpty(details.getPkRent()) && details.getDateBegin()!=null
				&& details.getTimes()!=null && details.getDayNum()!=null){
			NmRentRecord record = rentRecordMapper.getById(details.getPkRent());
			if(record==null)
			{
				throw new BusException("找不到出租记录！");
			}
			else if(record.getIsRefund().equals("1")|| record.getIsPay().equals("0")){
				throw new BusException("出租设备未收押金或已退回，不能续租！");				
			}
			
			try {
				
			//	details.setDateEnd(DateUtils.addDate(DateUtils.parseDate(details.getDateBegin()), details.getDayNum(), 3, "yyyy-MM-dd"));
				ApplicationUtils.setDefaultValue(details, true);
				details.setModityTime(details.getCreateTime());
				rentDetailsMapper.saveEntity(details);
			} catch (Exception e) {
				throw new BusException("转换结束日期异常：{0}", e.getMessage());
			}
		}else{
			throw new BusException("出租明细必填参数不能为空！");
		}
    }
	
	/**
	 * 删除出租详情
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void deleteDetails(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkRentDetails = paramMap.get("pkRentDetails");
		
		if(StringUtils.isNotEmpty(pkRentDetails)){
			NmRentDetails entity = new NmRentDetails();
			entity.setPkRentDetails(pkRentDetails.trim());
			entity.setDelFlag("1");
			ApplicationUtils.setDefaultValue(entity, false);
			rentDetailsMapper.updateEntity(entity);
		}else{
			throw new BusException("参数不能为空！");
		}
	}


}
