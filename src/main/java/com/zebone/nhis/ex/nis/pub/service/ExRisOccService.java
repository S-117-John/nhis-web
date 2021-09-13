package com.zebone.nhis.ex.nis.pub.service;

import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.nhis.ex.nis.pub.dao.ExRisOccMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医疗结果-检查项目
 */
@Service
public class ExRisOccService {

    @Autowired
    private ExRisOccMapper exRisOccMapper;

    /**
     * 根据条件查询检查结果集
     * ExRisOccService.selectAll
     * 005001001007
     * @param param
     * @param user
     * @return
     */
    public List<ExRisOcc> selectAll(String param, IUser user){
        ExRisOcc exRisOcc = JsonUtil.readValue(param, ExRisOcc.class);
        List<ExRisOcc> result = exRisOccMapper.selectAll(exRisOcc);
        return result;
    }

}
