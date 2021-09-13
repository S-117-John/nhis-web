package com.zebone.nhis.pro.zsba.ex.service;

import com.zebone.nhis.common.module.bl.CnOrderBar;
import com.zebone.nhis.pro.zsba.ex.dao.MedApplyBaMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname MedApplyBaHandler
 * @Description 高值耗材计费
 * @Date 2021-04-15 15:59
 * @Created by wuqiang
 */
@Service
public class MedApplyBaHandler {

    @Resource
    public MedApplyBaMapper medApplyBaMapper;
    @Resource
    private MedApplyBaService medApplyBaService;
    /**
     * 记耗材
     * @param param
     * @param user
     * @return
     */
    public void saveRecordCon(String param, IUser user) {
        User u = (User) user;
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkCnord = (String) paramMap.get("pkCnord");
        String barcode = (String) paramMap.get("barcode");
        if (StringUtils.isBlank(pkCnord) || StringUtils.isBlank(barcode)) {
            throw new BusException("医嘱主键或耗材条码为空！");
        }
        //查询耗材是否存在
        try {
            Map<String, Object> barMap = new HashMap<>();
            barMap.put("bar_code", barcode);
            DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
            medApplyBaMapper.wzQuery(barMap);//根据条形码，查询耗材
            String bSuccess = String.valueOf(barMap.get("iRetValue"));
            String cErrmsg = String.valueOf(barMap.get("sRetString"));
            if ("0".equals(bSuccess)) {
                Map<String, Object> wzbarMap = new HashMap<>();
                wzbarMap.put("bar_code", barcode);
                wzbarMap.put("patient_id", MapUtils.getString(paramMap, "codeOp"));
                wzbarMap.put("times", "1");
                wzbarMap.put("ledger_sn", "0");
                wzbarMap.put("mz_zy_flag", "0");
                wzbarMap.put("dept_sn", MapUtils.getString(paramMap, "codeDept"));
                wzbarMap.put("exec_unit", MapUtils.getString(paramMap, "codeDeptExec"));
                wzbarMap.put("doctor_code", MapUtils.getString(paramMap, "codeEmp"));
                wzbarMap.put("input_opera", u.getCodeEmp());
                medApplyBaMapper.wzConsume(wzbarMap);//记耗材
            } else {
                throw new BusException("查询耗材储过程失败！" + cErrmsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("查询耗材和消耗耗材存储过程失败！\n" + e.getMessage());
        } finally {
            DataSourceRoute.putAppId("default");//切换到 NHIS
        }
        CnOrderBar cnOrderBar = new CnOrderBar();
        cnOrderBar.setPkCnordBar(NHISUUID.getKeyId());
        cnOrderBar.setPkOrg(u.getPkOrg());
        cnOrderBar.setBarcode(barcode);
        cnOrderBar.setPkCnord(pkCnord);
        cnOrderBar.setCreator(u.getPkEmp());
        cnOrderBar.setCreateTime(new Date());
        cnOrderBar.setTs(new Date());
        cnOrderBar.setDelFlag("0");
        medApplyBaService.saveCnOrdBar(cnOrderBar);
    }
    /**
     * 取消耗材
     *
     * @param param
     * @param user
     * @return
     */
    public void cancelRecordCon(String param, IUser user) {
        User u = (User) user;
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkCnord = (String) paramMap.get("pkCnord");
        String barcode = (String) paramMap.get("barcode");
        if (StringUtils.isBlank(pkCnord) || StringUtils.isBlank(barcode)) {
            throw new BusException("医嘱主键或耗材条码为空！");
        }
        //查询耗材是否存在
        try {
            DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
            Map<String, Object> barMap = new HashMap<>();
            barMap.put("bar_code", barcode);
            medApplyBaMapper.wzCancelconsume(barMap);//取消耗材
            String bSuccess = String.valueOf(barMap.get("iRetValue"));
            String cErrmsg = String.valueOf(barMap.get("sRetString"));
            if ("1".equals(bSuccess)) {
                throw new BusException("查询耗材储过程失败！" + cErrmsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("查询耗材和消耗耗材存储过程失败！\n" + e.getMessage());
        } finally {
            DataSourceRoute.putAppId("default");// 切换数据源
        }
        medApplyBaService.updateCnOrderBar(pkCnord,barcode);
    }

}
