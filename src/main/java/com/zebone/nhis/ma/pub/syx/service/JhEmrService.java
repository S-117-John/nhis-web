package com.zebone.nhis.ma.pub.syx.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.ma.pub.syx.dao.JhEmrMapper;

@Service
public class JhEmrService {
	@Autowired
	private JhEmrMapper jhEmrMapper ;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<Map<String,Object>> qryJhEmrList(Map<String,Object> paramMap) throws Exception{
		return jhEmrMapper.qryJhEmrList(paramMap);
	}
}
