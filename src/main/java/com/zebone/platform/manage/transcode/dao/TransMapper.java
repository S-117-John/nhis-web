package com.zebone.platform.manage.transcode.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TransMapper {

	/**
	 * 获取rpc类型的所有交易号（包含'代理id'）:
	 *    trans_code    pproxy  proxy    address
	 * 如：001001003001  001     001001   RBASE15/empService.getEmpInfos
	 */
	public List<Map> queryRpcList();
	
	/**
	 * 获取rpc类型所有交易号 的 整个代理范围:
	 *    id       proxyname       pid
	 *    001      BASE            0
	 *    001001                   001
	 *    001002                   001
	 */
	public List<Map> queryProxyRange();
}
