package com.zebone.nhis.ma.pub.zsba.service;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.service.CreateExecListService;
import com.zebone.nhis.ma.pub.service.SystemPubRealizationService;
import com.zebone.nhis.ma.pub.zsba.dao.BaExtOwnBusinessMapper;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Classname BaPersonalizedService
 * @Description 博爱项目个性化实现类
 * @Date 2020-05-23 16:56
 * @Created by wuqiang
 */

@Service
public class BaExtOwnBusinessService {


    @Resource
    private BaExtOwnBusinessMapper baExtOwnBusinessMapper;
    @Autowired
    private CreateExecListService createExecListService;
    @Autowired
    private SystemPubRealizationService systemPubRealizationService;

    /***
     * @Description 手麻开立医嘱，检查检验，自动生成执行记录，并且执行单状态改为提交
     * 只有检查检验进行自动生成执行记录
     * @auther wuqiang
     * @Date 2020-05-25
     * @Param [args：医嘱主键]
     * @return void
     */
    public void handAnesthesiaExeRec(Object[] args) {
        if (args == null && args.length <= 0) {
            return;
        }
        List<String> pkCnords = (List<String>) args[0];
        pkCnords = baExtOwnBusinessMapper.queryRisAndLisPkCnords(pkCnords);
        if (pkCnords == null || pkCnords.size() <= 0) {
            return;
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkOrds", pkCnords);
        //更新检查检验输血申请单
        DataBaseHelper.update(getUpdateSql("cn_ris_apply"), paramMap);
        DataBaseHelper.update(getUpdateSql("cn_lab_apply"), paramMap);
        DataBaseHelper.update(getUpdateSql("CN_TRANS_APPLY"), paramMap);
        //生成医嘱执行记录
        createExecListService.createExecList(paramMap);
        completeMIssOrdData(pkCnords);
        //丢失个别记录填写
        //生成医技执行记录
        systemPubRealizationService.builtAssistOcc(pkCnords);
    }

    private String getUpdateSql(String tableName) {
        return "update " + tableName + "  set eu_status = 1 where pk_cnord  in (:pkOrds) ";
    }


    private void completeMIssOrdData(List<String> pkCnords) {
        List<BlIpDt> blIpDts = baExtOwnBusinessMapper.queryBlIpDt(pkCnords);
        List<ExOrderOcc> exOrderOccs = baExtOwnBusinessMapper.queryExOrderOcc(pkCnords);
        //批量更新List
        List<String> updateOrderList = new ArrayList<String>(16);
        boolean setExo = false;
        for (ExOrderOcc exO : exOrderOccs) {
            StringBuilder updateSql = new StringBuilder("update EX_ORDER_OCC  set ");
            updateSql.append("EU_STATUS = '" + 1 + "' ");
            updateSql.append(",DATE_OCC = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + "','YYYYMMDDHH24MISS') ");
            if (blIpDts != null && blIpDts.size() > 0) {
                for (BlIpDt bl : blIpDts) {
                    if (bl.getPkPv().equals(exO.getPkPv()) && bl.getPkCnord().equals(exO.getPkCnord())) {
                        StringBuilder updateSql2 = new StringBuilder("update BL_IP_DT  set ");
                        updateSql2.append(" PK_ORDEXDT = '" + exO.getPkExocc() + "' ");
                        updateSql2.append("  where  PK_CGIP='" + bl.getPkCgip() + "'");
                        updateOrderList.add(updateSql2.toString());
                        if (!setExo) {
                            setExo = true;
                            updateSql.append(" ,PK_EMP_OCC ='" + bl.getPkEmpApp() + "'");
                            updateSql.append(" ,NAME_EMP_OCC ='" + UserContext.getUser().getNameEmp() + "'");
                            updateSql.append(" ,PK_CG ='" + bl.getPkCgip() + "'");
                        }
                    }
                }
            } else {
                updateSql.append(" ,PK_EMP_OCC ='" + UserContext.getUser().getPkEmp() + "'");
                updateSql.append(" ,NAME_EMP_OCC ='" + UserContext.getUser().getNameEmp() + "'");
            }
            updateSql.append("  where  PK_EXOCC='" + exO.getPkExocc() + "'");
            updateOrderList.add(updateSql.toString());
            setExo = false;
        }
        DataBaseHelper.batchUpdate(updateOrderList.toArray(new String[0]));
    }
}
