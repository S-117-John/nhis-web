package com.zebone.nhis.pro.zsba.rent.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.rent.dao.NmRentAppMapper;
import com.zebone.nhis.pro.zsba.rent.vo.NmRentApp;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 非医疗费用-出租设备
 * @author lipz
 *
 */
@Service
public class NmRentAppService {
	
	@Autowired
	private NmRentAppMapper nmRentAppMapper;
	
	
	
	/**
	 * 获取
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public NmRentApp getNmRentApp(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkApp = paramMap.get("pkApp");
		
		NmRentApp ci = nmRentAppMapper.getById(pkApp.trim());
		
		return ci;
	}
	
	/**
     * 查询出所有记录
     */
    public List<NmRentApp> findAll(String param , IUser user){
    	return nmRentAppMapper.findAll();
    }
    
    /**
     * 查询出所有启用中的记录
     */
    public List<NmRentApp> findByUse(String param , IUser user){
    	return nmRentAppMapper.findByUse();
    }
	
	/**
	 * 保存或修改 022003010025
	 * @param param
	 * @param user
	 * @return 
	 */
	public void saveOrUpdate(String param , IUser user){
		NmRentApp app = JsonUtil.readValue(param, NmRentApp.class);
		User u = UserContext.getUser();
		if(app!=null){
			if(StringUtils.isNotEmpty(app.getCodeApp()) && StringUtils.isNotEmpty(app.getNameApp())
					 && StringUtils.isNotEmpty(app.getPyCode()) && app.getDepoAmt()!=null ){ 
				
				if(StringUtils.isNotEmpty(app.getPkApp())){
					app.setModityTime(new Date());
					app.setModifier(u.getPkEmp());
					ApplicationUtils.setDefaultValue(app, false);
					DataBaseHelper.updateBeanByPk(app,false);
				}else{
					String pkApp = NHISUUID.getKeyId(); 
					app.setPkApp(pkApp);
					ApplicationUtils.setDefaultValue(app, true);
					app.setModityTime(app.getCreateTime());
					app.setModifier(app.getCreator());					
			
					DataBaseHelper.insertBean(app);
				}
			}else{
				throw new BusException("必填参数不能为空！");
			}
		}else{
			throw new BusException("解析参数未能获得设备对象数据！");
		}
	}
	
	
	/**
	 * 上传或修改协议文件路径
	 * @param param
	 * @param user
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public void updateFilePath(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkApp = paramMap.get("pkApp");
		String filePath = paramMap.get("filePath");

		if(StringUtils.isNotEmpty(pkApp) && StringUtils.isNotEmpty(filePath)){ 
			NmRentApp app = new NmRentApp();
			app.setPkApp(pkApp);
			app.setFilePath(filePath);
			app.setModityTime(new Date());
			ApplicationUtils.setDefaultValue(app, false);
			nmRentAppMapper.updateApp(app);
		}else{
			throw new BusException("必填参数不能为空！");
		}
	}
	
	
	/**
	 * 批量删除 022003010026<逻辑删除>
	 * {"pkApps":"设备主键集","delFlag":"0启用/1禁用"}
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void deleteBatch(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkApps = paramMap.get("pkApps");
		String delFlag = paramMap.get("delFlag");
		
		if(StringUtils.isNotEmpty(pkApps) && StringUtils.isNotEmpty(delFlag)){
			String[] ids = pkApps.split(",");
			
			for(String pkApp : ids){
				NmRentApp app = new NmRentApp(); 
				app.setPkApp(pkApp.trim());
				app.setDelFlag(delFlag);
				ApplicationUtils.setDefaultValue(app, false);
				nmRentAppMapper.updateApp(app);
			}
			
		}else{
			throw new BusException("必填参数不能为空！");
		}
	}

	
}
