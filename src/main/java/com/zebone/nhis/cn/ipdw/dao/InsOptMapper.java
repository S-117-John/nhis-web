package com.zebone.nhis.cn.ipdw.dao;

import com.zebone.nhis.common.module.cn.ipdw.InsBear;
import com.zebone.nhis.common.module.cn.ipdw.InsOptDay;
import com.zebone.nhis.common.module.cn.ipdw.InsOptPb;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InsOptMapper {
	 /**
	 * 外部医保-计生手术资料
	 * @param PkPv
	 * @return
	 */
	public InsOptPb qryInsOptPb(String PkPv);
	/**
	 *外部医保-生育资料修改
	 * @param PkPv
	 * @return
	 */
	public InsBear qryInsBear(String PkPv);
	/**
	 * 外部医保-日间手术
	 * @param PkPv
	 * @return
	 */
	public InsOptDay qryInsOptDay(String PkPv);
}
