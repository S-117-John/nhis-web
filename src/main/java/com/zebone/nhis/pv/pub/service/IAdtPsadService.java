package com.zebone.nhis.pv.pub.service;

import java.util.Map;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.platform.modules.exception.BusException;

/**
 * ADT个性化业务接口，各项目请编写自己的具体业务实现类
 *
 */
public interface IAdtPsadService {
	
	public Map<String,Object> dealAdtPsadMethod (PiMaster pi,PvEncounter pv) throws BusException;
}
