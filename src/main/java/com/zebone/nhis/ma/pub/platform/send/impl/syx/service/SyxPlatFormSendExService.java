package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zebone.nhis.ma.pub.platform.send.impl.syx.vo.SysEsbmsg;
import com.zebone.nhis.ma.pub.service.SysLogService;

@Service
public class SyxPlatFormSendExService {

	public void save(List<SysEsbmsg> vos4save){
		SysLogService.saveSysEsbMsg(vos4save);
	}
	
}
