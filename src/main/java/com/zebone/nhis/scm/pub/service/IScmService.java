package com.zebone.nhis.scm.pub.service;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;

public interface IScmService {
	/**
	 * 对外发药接口业务接口
	 * @param obj
	 */
    public void processExtIpDe(List<ExPdApplyDetail> exPdAppDetails,String param);
    
    /**
     * 对外发药业务接口（门诊）
     * @param exPres
     * @param presList
     */
    public void processExOpDe(ExPresOcc exPres,Map<String,Object> paramMap);
}
