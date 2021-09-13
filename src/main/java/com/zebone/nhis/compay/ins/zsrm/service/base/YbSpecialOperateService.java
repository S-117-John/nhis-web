package com.zebone.nhis.compay.ins.zsrm.service.base;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.zsrm.dao.base.YbSpecialOperateMapper;
import com.zebone.nhis.compay.ins.zsrm.vo.base.HisInfo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class YbSpecialOperateService {
    @Autowired
    private YbSpecialOperateMapper ybSpecialOperateMapper;
    /**
     * 根据医保登记信息,查询his就诊信息
     * @param param
     * @param user
     * @return
     */
    public List<HisInfo> queryHisInfo(String param, IUser user){
        Map paramMap = JsonUtil.readValue(param, Map.class);
        String ybPkReg = CommonUtils.getString(paramMap.get("pkReg"));
        String qgybFlag = CommonUtils.getString(paramMap.get("qgybFlag"));
        List<HisInfo> list = new ArrayList<>();
        if("1".equals(qgybFlag)){
//            查询全国医保,his信息
            list = ybSpecialOperateMapper.queryHisInfoByQgyb(ybPkReg);
        }else{
//            查询工伤医保,his信息
            list = ybSpecialOperateMapper.queryHisInfoByGsyb(ybPkReg);
        }
        return list;
    }
}
