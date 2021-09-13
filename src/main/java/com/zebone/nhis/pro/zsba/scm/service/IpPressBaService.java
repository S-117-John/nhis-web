package com.zebone.nhis.pro.zsba.scm.service;

import com.zebone.nhis.pro.zsba.scm.dao.IpPdDeDrugBaMapper;
import com.zebone.nhis.pro.zsba.scm.vo.IpDeDrugBaDto;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IpPressBaService {

    @Autowired
    private IpPdDeDrugBaMapper ipDeDrugMapper;
    /**
     * 根据条件处方
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryPressList(String param, IUser user) {
        IpDeDrugBaDto ipDeDrugBaDto = JsonUtil.readValue(param, IpDeDrugBaDto.class);
        User userCur = (User) user;
        ipDeDrugBaDto.setPkDept(userCur.getPkDept());
        ipDeDrugBaDto.setPkOrg(userCur.getPkOrg());
        List<Map<String, Object>> mapResult = ipDeDrugMapper.queryPressList(ipDeDrugBaDto);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }
}
