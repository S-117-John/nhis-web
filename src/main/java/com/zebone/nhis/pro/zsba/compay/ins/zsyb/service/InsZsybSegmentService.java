package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSegment;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybSegmentService {


	/**
	 * 保存
	 * @param param 实体对象数据
	 * @param user  登录用户
	 * @return InsDict 医保结算返回段定义
	 */
	@Transactional
	public InsZsBaYbSegment save(String param , IUser user){
		InsZsBaYbSegment insSegment = JsonUtil.readValue(param, InsZsBaYbSegment.class);
		if(insSegment!=null){
			if(insSegment.getPkInssegment()!=null){
				String sql = "select * from ins_segment where  pk_inssegment = ? and del_flag = '0'";
				InsZsBaYbSegment entity = DataBaseHelper.queryForBean(sql, InsZsBaYbSegment.class, insSegment.getPkInssegment());
				if(!insSegment.getCode().equals(entity.getCode())){
				    sql = "select * from ins_segment where code = ? and del_flag = '0'";
					entity = DataBaseHelper.queryForBean(sql, InsZsBaYbSegment.class, insSegment.getCode());
					if(entity!=null){
						throw new BusException("编码已存在");
					}
				}
				DataBaseHelper.updateBeanByPk(insSegment, false);
			}else{
				String sql = "select * from ins_segment where code = ? and del_flag = '0'";
				InsZsBaYbSegment entity = DataBaseHelper.queryForBean(sql, InsZsBaYbSegment.class, insSegment.getCode());
				if(entity!=null){
					throw new BusException("编码已存在");
				}
				DataBaseHelper.insertBean(insSegment);
			}
		}
		return insSegment;
	}
}