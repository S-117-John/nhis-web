package com.zebone.nhis.ma.pub.syx.service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.service.SystemPubRealizationService;
import com.zebone.nhis.ma.pub.syx.dao.SendDrugsToMachineMapper;
import com.zebone.nhis.ma.pub.syx.vo.AtfYpPageNo;
import com.zebone.nhis.ma.pub.syx.vo.AtfYpxxSyx;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SendDrugsToMachineHandler {

    @Autowired
    private SendDrugsToMachineMapper toMachineMapper;
    @Autowired
    private SendDrugsToMachineService toMachineService;
    @Autowired
    private SystemPubRealizationService systemPubRealizationService;
    private Logger logger = LoggerFactory.getLogger("ma.syxInterface");

    public Object invokeMethod(String methodName, Object... args) {
        Object result = null;
        switch (methodName) {
            case "sendToMah":
                result = this.sendToMah(args);
                break;
            case "sendDrugInfoToMachine":
                result=this.sendDrugInfoToMachine(args);
                break;
            case "querySendDrugAgainData":
                //查询重发包药机数据
                result=systemPubRealizationService.querySendDrugAgainData(args);
                break;
        }
        return result;
    }



	/**
     * 发药并发送包药机接口数据   中二项目（东山） 重发包药机
     *
     * @param args
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> sendDrugInfoToMachine(Object[] args)  {
        Map<String, Object> sendParmaMap = (Map<String, Object>) args[0];;
        AtfYpPageNo pageNo = toMachineMapper.generateRecord(sendParmaMap);
        if (pageNo != null) {
            pageNo.setSubmitTime(DateUtils.getCDayInCurrentYear());
        }
        List<AtfYpxxSyx> atfYpxxSyx = toMachineMapper.generateRecordDetail(sendParmaMap);
        //查询当前发药科室所属院区
        Map<String, Object> codeArea = DataBaseHelper.queryForMap("select code_area from bd_ou_dept dept inner join bd_ou_org_area area on dept.pk_orgarea=area.pk_orgarea where dept.pk_dept=?",
                UserContext.getUser().getPkDept());
        pageNo.setGroupNo("");
        try {
            String atfNo = ScmPubUtils.getMachineNo(UserContext.getUser().getPkDept());
            pageNo.setAtfNo(atfNo);//读取科室级参数，包药机设备编号
            for (AtfYpxxSyx temp : atfYpxxSyx) {
                temp.setAtfNo(atfNo);
            }
            DataSourceRoute.putAppId("default_byj");//切换数据源
            //调用service保存，并提交事务
            toMachineService.saveDrugToMachine(pageNo, atfYpxxSyx, codeArea.get("codeArea") == null ? "" : codeArea.get("codeArea").toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("发送包药机失败！\n" + e.getMessage());
        } finally {
            DataSourceRoute.putAppId("default");//切换数据源
        }
        //完成发药，更新发药表数据
        toMachineService.updateFlagSend(sendParmaMap);
        return null;
    }

    /**
     * 发送包药机
     *
     * @param args
     * @return
     */
    private Object sendToMah(Object... args) {
        if (args != null) {
            // 当前科室是否可发送包药机
            if (ScmPubUtils.HasPackParam(UserContext.getUser().getPkDept())) {
                //获取系统参数设置的包药机药品发放分类
                String strPd = ScmPubUtils.getPdDecate(UserContext.getUser().getPkDept());
                String[] pdDecates = strPd.split(",");

                // 如果没有设置发放分类，或者发放分类设置错误，不走包药机
                if (pdDecates == null || pdDecates.length <= 0)
                    return null;

                // 获取发药返回数据
                List<PdDeDrugVo> drugList = (List<PdDeDrugVo>) args[0];
                if (drugList == null || drugList.size() <= 0) return null;

                //查询发送药品的发放分类，该药品若为非医嘱发药，则返回
                Map<String, Object> param = toMachineMapper.qryParam(drugList.get(0).getPkPdde());
                if (param == null) return null;

                // 如果系统设置的发放分类和发药的分类不匹配，则返回
                boolean isExit = false;
                for (int i = 0; i < pdDecates.length; i++) {
                    if (param.get("codeDecate").equals(pdDecates[i])) {
                        isExit = true;
                        break;
                    }
                }
                if (!isExit) return null;

                //查询当前发药科室所属院区
                Map<String, Object> codeArea = DataBaseHelper.queryForMap("select code_area from bd_ou_dept dept inner join bd_ou_org_area area on dept.pk_orgarea=area.pk_orgarea where dept.pk_dept=?",
                        UserContext.getUser().getPkDept());

                AtfYpPageNo pageNo = toMachineMapper.generateRecord(param);
                if (pageNo != null) {
                    pageNo.setSubmitTime(DateUtils.getCDayInCurrentYear());
                }
                pageNo.setGroupNo("");
                List<AtfYpxxSyx> atfYpxxsSyx = toMachineMapper.generateRecordDetail(param);

                //插入中间库数据，药品名称一直为空加此代码打印日志分析问题
                for (AtfYpxxSyx temp : atfYpxxsSyx) {
                    if (StringUtils.isEmpty(temp.getDrugname())) {
                        logger.info("药品名称为空！发药单号：" + temp.getPageNo() + ",药品编码：" + temp.getDrugCode());
                    }
                }
                // 读取科室级参数，包药机设备编号
                pageNo.setAtfNo(ScmPubUtils.getMachineNo(UserContext.getUser().getPkDept()));
                try {
                    DataSourceRoute.putAppId("default_byj");// 切换数据源
                    // 调用service保存，并提交事务
                    toMachineService.saveDrugToMachine(pageNo, atfYpxxsSyx, codeArea.get("codeArea") == null ? "" : codeArea.get("codeArea").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                    // throw new BusException("发送包药机失败！\n" + e.getMessage());
                } finally {
                    DataSourceRoute.putAppId("default");// 切换数据源
                }
                toMachineService.updateFlagSend(param);
            }
        }
        return null;
    }
}
