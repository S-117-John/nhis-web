package com.zebone.nhis.webservice.syx.dao;

import java.util.List;

import com.zebone.nhis.webservice.syx.vo.ExtSubject;

import com.zebone.nhis.webservice.syx.vo.ViewNisEncounter;
import com.zebone.nhis.webservice.syx.vo.ViewNisEncountersEvent;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ExternalInterfaceMapper {
	
	public Integer countPiByPk(String pkPi);
	
	public List<ViewNisEncounter> qryPvEncounter(ExtSubject subject);
	
	public List<ViewNisEncountersEvent> qryPvEncounterEvent(ExtSubject subject);
}
