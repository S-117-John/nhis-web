package com.zebone.nhis.ex.nis.pub.service;

import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.ex.nis.pub.dao.ExLabOccMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ExLabOccService{

    @Resource
    private ExLabOccMapper exLabOccMapper;

    /**
     * 条件查询检验结果
     * ExLabOccService.selectAll
     * 005001001008
     * @param param
     * @param user
     * @return
     */
    public List<ExLabOcc> selectAll(String param, IUser user){
        ExLabOcc exLabOcc = JsonUtil.readValue(param, ExLabOcc.class);
        List<ExLabOcc> result = exLabOccMapper.selectAll(exLabOcc);
        return result;
    }
}
