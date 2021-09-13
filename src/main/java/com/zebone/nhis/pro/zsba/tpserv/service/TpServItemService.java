package com.zebone.nhis.pro.zsba.tpserv.service;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.tpserv.dao.TpServItemMapper;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItem;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 第三方服务项目(持久层)
 * @author ZhengRJ
 */
@Service
public class TpServItemService {
	@Autowired
	private TpServItemMapper tpServItemMapper;
	
	/**
	 * 添加/修改第三方服务项目
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void saveServItem(String param, IUser user){
		
			TpServItem master = JsonUtil.readValue(param, TpServItem.class);
			TpServItem master2 = new TpServItem();
			//根据code去查询项目
			master2.setCode(master.getCode());
			List<TpServItem> list = tpServItemMapper.getServItemList(master2);	
			if(list.size()>0){
				if(master.getPkItem()!=null){
					//修改的时候，查询是否重复要过滤掉自己的编码
					if(!master.getPkItem().equals(list.get(0).getPkItem())){
						throw new BusException("单位编码重复！");
					}
				}else{
					//添加的时候，查询是否重复要过滤掉自己的编码
					throw new BusException("单位编码重复！");
				}
			}
			master.setTs(new Date());
			if(master.getPkItem()!=null){
				master.setModityTime(new Date());
				master.setDelFlag("0");
				System.out.println(master.getFkDepts().length());
				DataBaseHelper.updateBeanByPk(master,false);
			}else{
				master.setCreateTime(new Date());
				master.setDelFlag("0");
				DataBaseHelper.insertBean(master);
			}
			
	} 
	
	/**
	 * 删除第三方服务项目(修改删除标志)
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void delServItem(String param, IUser user){
		TpServItem master = JsonUtil.readValue(param, TpServItem.class);
		master.setTs(new Date());
		master.setModityTime(new Date());
		master.setDelFlag("1");
		DataBaseHelper.updateBeanByPk(master,false);
	} 
}