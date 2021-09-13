package com.zebone.nhis.pro.zsba.tpserv.service;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.tpserv.dao.TpServItemDeviceMapper;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemDevice;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 第三方服务项目单位(持久层)
 * @author ZhengRJ
 */
@Service
public class TpServItemDeviceService {
	@Autowired
	private TpServItemDeviceMapper tpServItemDeviceMapper;
	
	/**
	 * 添加/修改第三方服务项目单位
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void saveServItemDevice(String param, IUser user){
		
			TpServItemDevice master = JsonUtil.readValue(param, TpServItemDevice.class);
			TpServItemDevice master2 = new TpServItemDevice();
			//根据code去查询项目类型
			master2.setCode(master.getCode());
			List<TpServItemDevice> list = tpServItemDeviceMapper.getServItemDeviceList(master2);	
			if(list.size()>0){
				if(master.getPkItemDevice()!=null){
					//修改的时候，查询是否重复要过滤掉自己的编码
					if(!master.getPkItemDevice().equals(list.get(0).getPkItemDevice())){
						throw new BusException("单位编码重复！");
					}
				}else{
					//添加的时候，查询是否重复要过滤掉自己的编码
					throw new BusException("单位编码重复！");
				}
			}
			master.setTs(new Date());
			if(master.getPkItemDevice()!=null){
				//master.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				//master.setDelFlag("0");
				DataBaseHelper.updateBeanByPk(master,false);
			}else{
				//master.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				master.setRentedAlreadyNum(0);
				//master.setDelFlag("0");
				DataBaseHelper.insertBean(master);
			}
			
	} 
	
	/**
	 * 删除第三方服务项目设备(修改删除标志)
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void delServItemDevice(String param, IUser user){
		TpServItemDevice master = JsonUtil.readValue(param, TpServItemDevice.class);
		//master.setTs(new Date());
		//master.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
		master.setDelFlag("1");
		DataBaseHelper.updateBeanByPk(master,false);
	} 
}
