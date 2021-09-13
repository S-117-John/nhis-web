package com.zebone.nhis.pro.zsba.rent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.rent.dao.NmRentDetailsMapper;
import com.zebone.nhis.pro.zsba.rent.dao.NmRentRecordMapper;

/**
 * 设备出租-汇总统计
 * @author lipz
 *
 */
@Service
public class NmRentReportService {
	
private static Logger log = LoggerFactory.getLogger(NmRentReportService.class);
	
	@Autowired NmRentRecordMapper rentRecordMapper;
	@Autowired NmRentDetailsMapper rentDetailsMapper;

}
