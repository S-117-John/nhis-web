package com.zebone.nhis.pro.zsba.tpserv.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.tpserv.dao.TpServItemUnitMapper;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemUnit;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 第三方服务项目单位(持久层)
 * @author ZhengRJ
 */
@Service
public class TpServItemUnitService {
	@Autowired
	private TpServItemUnitMapper tpServItemUnitMapper;
	
	/**
	 * 添加/修改第三方服务项目单位
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void saveServItemUnit(String param, IUser user){
		TpServItemUnit master = JsonUtil.readValue(param, TpServItemUnit.class);
		TpServItemUnit master2 = new TpServItemUnit();
			//根据code去查询项目类型
			master2.setCode(master.getCode());
			List<TpServItemUnit> list = tpServItemUnitMapper.getServItemUnitList(master2);	
			if(list.size()>0){
				if(master.getPkItemUnit()!=null){
					//修改的时候，查询是否重复要过滤掉自己的编码
					if(!master.getPkItemUnit().equals(list.get(0).getPkItemUnit())){
						throw new BusException("单位编码重复！");
					}
				}else{
					//添加的时候，查询是否重复要过滤掉自己的编码
					throw new BusException("单位编码重复！");
				}
			}
			master.setTs(new Date());
			if(master.getPkItemUnit()!=null){
				//master.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				//master.setDelFlag("0");
				DataBaseHelper.updateBeanByPk(master,false);
			}else{
				//master.setCreateTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				//master.setDelFlag("0");
				DataBaseHelper.insertBean(master);
			}
			
	} 
	
	/**
	 * 删除第三方服务项目单位(修改删除标志)
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void delServItemUnit(String param, IUser user){
		TpServItemUnit master = JsonUtil.readValue(param, TpServItemUnit.class);
		//master.setTs(new Date());
		//master.setModityTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
		master.setDelFlag("1");
		DataBaseHelper.updateBeanByPk(master,false);
	} 
}
