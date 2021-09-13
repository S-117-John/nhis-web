package com.zebone.nhis.bl.pub.dao;

import java.util.List;

import com.zebone.nhis.bl.pub.vo.NssBlInvoiceListVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 自助清单
 * @author Administrator
 *
 */
@Mapper
public interface NssPatientListMapper {
	
	
	public List<NssBlInvoiceListVo> getChargeSchedule(String pkSettle);

}
