package com.zebone.nhis.ex.nis.fee.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ex.nis.fee.dao.PrepayMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 预交金管理
 *
 * @author yangxue
 */
@Service
public class PrepayService {

    @Resource
    private PrepayMapper prepayMapper;

    /**
     * 根据病区，预交金比率查询患者预交金列表
     *
     * @param param{pkDeptNs,num:小数形式}
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryPrepayList(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        //当参数设置为贫困患者无需缴纳预交金时，不查出此类患者
        String isLImPoorPre= ApplicationUtils.getSysparam("BL0049", false);
        String poorCode = ApplicationUtils.getSysparam("PI0015", true);
        boolean isSetQueryPar=(isLImPoorPre!=null&&"1".equals(isLImPoorPre))&&( poorCode!=null&&!"".equals(poorCode));
        if (isSetQueryPar){
            poorCode.trim();
           String [] poorCodeStr=poorCode.split(",");
            map.put("code", poorCodeStr);
        }
        String dbType = MultiDataSource.getCurDbType();
            if("sqlserver".equals(dbType)) {
                return prepayMapper.queryPrePaySql(map);
            }else{
                return prepayMapper.queryPrePay(map);
            }


    }
}
