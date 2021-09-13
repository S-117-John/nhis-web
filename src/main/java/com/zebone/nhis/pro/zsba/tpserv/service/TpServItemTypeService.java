package com.zebone.nhis.pro.zsba.tpserv.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.tpserv.dao.TpServItemTypeMapper;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemType;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 第三方服务项目类型(持久层)
 * @author ZhengRJ
 */
@Service
public class TpServItemTypeService {

	@Autowired
	private TpServItemTypeMapper tpServItemTypeMapper;
	
	/**
	 * 查询第三方服务项目类型列表
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public List<TpServItemType> getServItemTypeList(String param, IUser user){
		TpServItemType master = JsonUtil.readValue(param, TpServItemType.class);
		List<TpServItemType> list = tpServItemTypeMapper.getServItemTypeList(master);		
		return list;
	} 
	
	/**
	 * 查询第三方服务项目类型列表
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public List<TpServItemType> getTheTypeList(String param, IUser user){
		TpServItemType master = JsonUtil.readValue(param, TpServItemType.class);
		List<TpServItemType> list = tpServItemTypeMapper.getServItemTypeList(master);	
		List<TpServItemType> list2 = new ArrayList<TpServItemType>();
		for(int i=0; i<list.size(); i++){
			TpServItemType type = list.get(i);
			for(int j=0; j<list.size(); j++){
				if(list.get(j).getSpcode()!=null&&type.getCode()!=null&&list.get(j).getSpcode().indexOf("'"+type.getCode()+"'")!=-1){
					list2.add(type);
					break;
				}
			}
		}
		return list2;
	} 
	
	/**
	 * 添加第三方服务项目类型
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void saveServItemType(String param, IUser user){
		TpServItemType master = JsonUtil.readValue(param, TpServItemType.class);
		TpServItemType master2 = new TpServItemType();
			//根据code去查询项目类型
			master2.setCode(master.getCode());
			List<TpServItemType> list = tpServItemTypeMapper.getServItemTypeList(master2);	
			if(list.size()>0){
				//修改的时候，查询是否重复要过滤掉自己的编码
				if(master.getPkItemType()!=null){
					if(!master.getPkItemType().equals(list.get(0).getPkItemType())){
						throw new BusException("类型编码重复！");
					}
				}else{
					throw new BusException("类型编码重复！");
				}
			}
			master.setTs(new Date());
			if(master.getPkItemType()!=null){
				//master.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				DataBaseHelper.updateBeanByPk(master,false);
			}else{
				//master.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				master.setDelFlag("0");
				DataBaseHelper.insertBean(master);
			}
			
	} 
	
	/**
	 * 删除第三方服务项目类型(修改删除标志)
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void delServItemType(String param, IUser user){
		TpServItemType master = JsonUtil.readValue(param, TpServItemType.class);
		master.setTs(new Date());
		//master.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
		master.setDelFlag("1");
		DataBaseHelper.updateBeanByPk(master,false);
	} 
}
