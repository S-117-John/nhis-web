package com.zebone.nhis.bl.pub.syx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.syx.dao.CgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.dao.IpCgStrategyDistrictMapper;
/**
 * 广州-区公医住院记费策略
 * @author 
 *
 */
@Service
public class IpCgStrategyDistrictService {
	
	@Autowired
	private IpCgStrategyDistrictMapper ipCgStrategyDistrictMapper;
	
	@Autowired
	private CgStrategyPubService cgStrategyPubService;
	
	@Autowired
	private CgStrategyPubMapper cgStrategyPubMapper;
	
	
	
}
