package com.zebone.nhis.cn.ipdw.service;

import com.zebone.nhis.cn.ipdw.dao.CnIndicationMapper;
import com.zebone.nhis.cn.ipdw.vo.CnConsultResponseCaVO;
import com.zebone.nhis.cn.ipdw.vo.CnIndicationVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 适应症--leiminjian 20200427
 */
@Service
public class CnIndicationService {
    @Autowired
    private CnIndicationMapper indicatMapper;

    /**
     * 查询医嘱
     * @param param
     * @param user
     * @return
     * @throws BusException
     */
    public List<Map<String,Object>> qryPvCnOrder(String param, IUser user) throws BusException {
        Map<String,Object> pam=JsonUtil.readValue(param, Map.class);
        if(pam.get("pkPv")==null){
            throw  new BusException("未获到患者信息！");
        }
        List<Map<String,Object>> rtn = indicatMapper.qryPvCnOrder(pam);
        return rtn;
    }

    /**
     * 更新医嘱
     * @param param
     * @param user
     * @throws BusException
     */
    public void upPvCnOrder(String param, IUser user) throws BusException {
        CnIndicationVo vo=JsonUtil.readValue(param, CnIndicationVo.class);
        if(vo==null){
            throw  new BusException("未获到患者医嘱信息！");
        }
        List<CnOrder> ord=vo.getOrder();
        if(ord==null || ord.size()==0){
            throw  new BusException("未获到患者医嘱信息！");
        }

        String sql=" update CN_ORDER set FLAG_FIT=:flagFit where PK_CNORD=:pkCnord";
        DataBaseHelper.batchUpdate(sql,ord);
    }
}
