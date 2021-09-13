package com.zebone.nhis.cn.ipdw.service;

import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnChiefResidentMapper;
import com.zebone.nhis.cn.ipdw.vo.CnChiefResidentVo;
import com.zebone.nhis.common.module.cn.ipdw.CnChiefResident;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 临床-住院总值班 
 * @author ds
 * @since 2020-12-21 09:51:57
 */
@Service
public class CnChiefResidentService {
	
	@Autowired
	public CnChiefResidentMapper cnChiefResidentMapper;
	/**
	 * 保存住院总值班 
	 * @param param
	 * @param user
	 */
	public void saveChiefResident(String param, IUser user){
		CnChiefResident cnChiefResident = JsonUtil.readValue(param,CnChiefResident.class);
		if(StringUtils.isEmpty(cnChiefResident.getPkCr())){
			ApplicationUtils.setDefaultValue(cnChiefResident, true);
			DataBaseHelper.insertBean(cnChiefResident);
		}else{
			DataBaseHelper.updateBeanByPk(cnChiefResident,false);
		}
	}
	/**
	 * 删除住院总值班 
	 * @param param
	 * @param user
	 */
	public void delChiefResident(String param, IUser user) {
		CnChiefResident cnChiefResident = JsonUtil.readValue(param,CnChiefResident.class);
		if(StringUtils.isEmpty(cnChiefResident.getPkCr())){
			throw new BusException("住院总值班主键为空，请检查参数!");
		}
		DataBaseHelper.deleteBeanByPk(cnChiefResident);
	}
	/**
	 * 查询住院总值班列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public CnChiefResidentVo queryChiefResidentList(String param,IUser user){
		CnChiefResidentVo qryparam = JsonUtil.readValue(param,CnChiefResidentVo.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> list=cnChiefResidentMapper.queryChiefResidentList(paramMap);
		Page<List<CnChiefResidentVo>> page = MyBatisPage.getPage();
		CnChiefResidentVo paramPage =new CnChiefResidentVo();
		paramPage.setCnChiefResidentVoList(list);;
		paramPage.setTotalCount(page.getTotalCount());
		return paramPage;
	}
}
