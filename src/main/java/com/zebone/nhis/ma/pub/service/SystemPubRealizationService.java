package com.zebone.nhis.ma.pub.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOccDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.dao.SystemPubRealizationMapper;
import com.zebone.nhis.ma.pub.vo.AssistOccVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname SystemPubRealizationService
 * @Description 1.对应SystemPubRealizationHandler基本实现服务
 * @Date 2020-05-25 10:28
 * @Created by wuqiang
 */
@Service
public class SystemPubRealizationService {


    @Autowired
    public SystemPubRealizationMapper systemPubRealizationMapper;

    public void builtAssistOcc(Object... args) {
        if (args != null && args.length > 0) {
            @SuppressWarnings("unchecked")
            List<String> pkCnords = (List<String>) args[0]; // 医嘱主键
            // 根据医嘱主键查询可生成的医技执行单
            List<AssistOccVo> assistOccVos = systemPubRealizationMapper.qryAssistOcc(pkCnords);
            // 组装医技执行单主子表的数据
            List<ExAssistOccDt> assistOccDts = new ArrayList<>(6);
            List<ExAssistOcc> assistOccs = new ArrayList<>(6);
            if (assistOccVos != null && assistOccVos.size() > 0) {
                //关联主子表
                for (AssistOccVo vo : assistOccVos) {
                    vo.setPkAssoccdt(NHISUUID.getKeyId());//设置子表主键
                    if ("1".equals(vo.getFlagMaj())) {
                        vo.setPkAssocc(NHISUUID.getKeyId());//设置主表主键
                        for (AssistOccVo voo : assistOccVos) {
                            if (vo.getOrdsn().equals(voo.getOrdsnParent())) {
                                voo.setPkAssocc(vo.getPkAssocc());//关联主子表
                            }
                        }
                    }
                }
                // 组装数据
                for (AssistOccVo vo : assistOccVos) {
                    ExAssistOccDt assistOccDt = new ExAssistOccDt();
                    vo.setNameEmpOrd(UserContext.getUser().getNameEmp());
                    vo.setPkEmpOrd(UserContext.getUser().getPkEmp());
                    vo.setPkDept(UserContext.getUser().getPkDept());
                    vo.setCodeOcc(ApplicationUtils.getCode("0503"));
                    vo.setDateOrd(new Date());
                    try {
                        BeanUtils.copyProperties(vo, assistOccDt);
                        assistOccDts.add(assistOccDt);
                        // 主医嘱标志，表示该执行单对应主医嘱
                        if ("1".equals(vo.getFlagMaj())) {
                            ExAssistOcc assistOcc = new ExAssistOcc();
                            BeanUtils.copyProperties(vo, assistOcc);
                            assistOccs.add(assistOcc);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExAssistOcc.class), assistOccs);
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExAssistOccDt.class), assistOccDts);
        }
    }

    /***
     * @Description
     * 手麻医嘱是否生成执行单，医技是否自动执行实现服务，原有现状不进行生成，只是单纯的计费
     * @auther wuqiang
     * @Date 2020-05-25
     * @Param [args]
     * @return void
     */
    public void handAnesthesiaExeRec(Object[] args) {
        return;
    }

    /***
     * @Description  查询包药机未发送药品通用实现类
     * @auther wuqiang
     * @Date 2020-06-01
     * @Param [args]
     * @return java.lang.Object
     */
    public  Object querySendDrugAgainData(Object[] args) {
        Map<String, Object> qryParam = (Map<String, Object>) args[0];
        Map<String, Object> decateMap = DataBaseHelper.queryForMap("select pk_pddecate from BD_PD_DECATE where code_decate =? ", qryParam.get("pkPddecate").toString());
        if (decateMap != null && decateMap.get("pkPddecate") != null) {
            qryParam.put("pkPddecate", decateMap.get("pkPddecate").toString());
        }
        List<Map<String, Object>> drugInfoList = systemPubRealizationMapper.querySendDrugAgainData(qryParam);
        return drugInfoList;

    }


}
