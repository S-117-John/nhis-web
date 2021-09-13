package com.zebone.nhis.pro.zsrm.cn.service;

import com.zebone.nhis.common.module.cn.opdw.PvInfluenza;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 流感样病例上报
 */
@Service
public class CnInfluenzaService {

    /**
     * 保存流感上报
     * @param param
     * @param user
     */
    public void saveInfluenza(String param, IUser user){
        PvInfluenza influenza= JsonUtil.readValue(param,PvInfluenza.class);
        if(influenza==null) throw  new BusException("传入参数有误，请确认！");
        String sql="delete from PV_INFLUENZA where pk_pv=?";
        DataBaseHelper.execute(sql,new Object[]{influenza.getPkPv()});
        ApplicationUtils.setDefaultValue(influenza,true);
        influenza.setDateInflu(new Date());
        DataBaseHelper.insertBean(influenza);

    }

    /**
     * 查询流感上报
     * @param param
     * @param user
     * @return
     */
    public List<PvInfluenza> qryInfluenza(String param, IUser user){
        String pkPv=JsonUtil.getFieldValue(param,"pkPv");
        if(CommonUtils.isNull(pkPv)) {
            throw  new BusException("未传入就诊信息，请核实数据[pkPv]!");
        }
        String sql="select * from PV_INFLUENZA where pk_pv=?";
        List<PvInfluenza> resList=DataBaseHelper.queryForList(sql,PvInfluenza.class,new Object[]{pkPv});
        return resList;
    }
}
