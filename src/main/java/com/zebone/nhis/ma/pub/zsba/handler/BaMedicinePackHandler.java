package com.zebone.nhis.ma.pub.zsba.handler;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.service.SystemPubRealizationService;
import com.zebone.nhis.ma.pub.zsba.service.BaMedicinePackService;
import com.zebone.nhis.ma.pub.zsba.vo.*;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname BaMedicinePackHandler
 * @Description 博爱包药机
 * @Date 2020-05-29 10:43
 * @Created by wuqiang
 */
@Service
public class BaMedicinePackHandler {
    private Logger logger = LoggerFactory.getLogger("nhis.BaWebServiceLog");

    @Autowired
    private BaMedicinePackService baMedicinePackService;
    @Autowired
    private BdSnService bdSnService;
    @Autowired
    private SystemPubRealizationService systemPubRealizationService;

    public Object invokeMethod(String methodName, Object... args) {
        Object result = null;
        switch (methodName) {
            case "sendToMah":
                //发送包药机
                result = this.sendToMah(args);
                break;
            case "sendDrugInfoToMachine":
                //重发包药机
                result = this.sendDrugInfoToMachine(args);
                break;
            case "querySendDrugAgainData":
                //查询重发包药机数据
                result = baMedicinePackService.querySendDrugAgainData(args);
                break;
            case "queryExMedBag":
                //查询药袋相关数据
                result = baMedicinePackService.queryExMedBag(args);
                break;
        }
        return result;
    }

    /**
     * 发送包药机
     * 1.查询出摆药机数据，人共打印药袋数据，分别进行处理
     * 2.组装记费，记费，保存药袋数据，（摆药机数据要发送到第三方表）
     *
     * @param args
     * @return
     */
    public Object sendToMah(Object... args) {
        if (args == null) {
            logger.info("发送包药机入参为空");
            return null;
        }
        // 当前科室包药机参数 EX0027 0 不使用包药机，1 使用包药机，2所有口服药使用人共包药
        String pcakParam = ScmPubUtils.getPackParam(UserContext.getUser().getPkDept());
        if ("0".equals(pcakParam)) {
            return null;
        }
        List<PdDeDrugVo> drugList = (List<PdDeDrugVo>) args[0];
        if (drugList == null || drugList.size() <= 0) {
            return null;
        }
        PackPdVoList packPdVoList = baMedicinePackService.drugBreakRecord(drugList, pcakParam);
        if (packPdVoList == null) {
            return null;
        }
        //摆药机
        List<PackPdMedVo> packPdVos = packPdVoList.getPackPdVoList();
        //人共长期
        List<PackPdMedVo> altogetherPachPdVolistLong = packPdVoList.getAltogetherPachPdVolistLong();
        //人共临时
        List<PackPdMedVo> altogetherPachPdVolistTemporary = packPdVoList.getAltogetherPachPdVolistTemporary();

        if (altogetherPachPdVolistLong != null && altogetherPachPdVolistLong.size() > 0) {
            //摆药机处理：生成摆药单-生成计费数据-计费-记录药袋-更新EX-pd-de表
            List<ExMedBag> exMedBagList = null;
            ExMedBegAndDEetialPatams exMedBegAndDEetialPatams = baMedicinePackService.calculateNumOfMedBags(altogetherPachPdVolistLong, BagEuStatus.ARTI.getStatus());
            if (exMedBegAndDEetialPatams == null || exMedBegAndDEetialPatams.getExMedBags().size() <= 0) {
                return null;
            }
            exMedBagList = exMedBegAndDEetialPatams.getExMedBags();
            List<BlPubParamVo> blPubParamVoList = baMedicinePackService.medBagsCharge(exMedBagList, BagEuStatus.ARTI.getStatus());
            baMedicinePackService.saveMedBag(blPubParamVoList, exMedBagList);
            baMedicinePackService.saveMedicineBagDetail(exMedBegAndDEetialPatams.getExMedBagDetails());
        }
        if (altogetherPachPdVolistTemporary != null && altogetherPachPdVolistTemporary.size() > 0) {
            //摆药机处理：生成摆药单-生成计费数据-计费-记录药袋-更新EX-pd-de表
            List<ExMedBag> exMedBagList = null;
            ExMedBegAndDEetialPatams exMedBegAndDEetialPatams = baMedicinePackService.calculateNumOfMedBags(altogetherPachPdVolistTemporary);
            if (exMedBegAndDEetialPatams == null || exMedBegAndDEetialPatams.getExMedBags().size() <= 0) {
                return null;
            }
            exMedBagList = exMedBegAndDEetialPatams.getExMedBags();
            List<BlPubParamVo> blPubParamVoList = baMedicinePackService.medBagsCharge(exMedBagList, BagEuStatus.ARTI.getStatus());
            baMedicinePackService.saveMedBag(blPubParamVoList, exMedBagList);
            baMedicinePackService.saveMedicineBagDetail(exMedBegAndDEetialPatams.getExMedBagDetails());
        }
        if (packPdVos != null && packPdVos.size() > 0) {
            //摆药机处理：生成药袋-生成计费数据-计费-发送摆药机-记录药袋-更新EX-pd-de表
            List<ExMedBag> exMedBagList = null;
            ExMedBegAndDEetialPatams exMedBegAndDEetialPatams = baMedicinePackService.calculateNumOfMedBags(packPdVos, BagEuStatus.MACH.getStatus());
            if (exMedBegAndDEetialPatams == null || exMedBegAndDEetialPatams.getExMedBags().size() <= 0) {
                return null;
            }
            exMedBagList = exMedBegAndDEetialPatams.getExMedBags();
            List<BlPubParamVo> blPubParamVoList = baMedicinePackService.medBagsCharge(exMedBagList, BagEuStatus.MACH.getStatus());
            baMedicinePackService.saveMedBag(blPubParamVoList, exMedBagList);
            baMedicinePackService.saveMedicineBagDetail(exMedBegAndDEetialPatams.getExMedBagDetails());
            baMedicinePackService.updateFlagSend(packPdVos, "1");
            try {
                List<PackPdVo> packPdVos1 = convertToPackList(packPdVos);
                DataSourceRoute.putAppId("HIS_bayy");// 切换数据源
                baMedicinePackService.packPd(packPdVos1);
            } catch (Exception e) {
                logger.error("包药机报错:{} 错误结束" + e.getMessage());
                baMedicinePackService.updateFlagSend(packPdVos, "0");
            } finally {
                DataSourceRoute.putAppId("default");// 切换数据源
            }
        }
        return null;
    }

    /***
     * @Description 重发包药机
     * @auther wuqiang
     * @Date 2020-04-24
     * @Param [args]
     * @return java.lang.Object
     */
    private Object sendDrugInfoToMachine(Object[] args) {
        Map<String, Object> map = (Map<String, Object>) args[0];
        List<PdDeDrugVo> drugList = baMedicinePackService.queryPdDeDrugVo(map);
        if (drugList == null || drugList.size() <= 0) {
            throw new BusException("没有需要重发的数据！请核对");
        }
        return this.sendToMah(new Object[]{drugList});
    }

    /***
     * @Description 转换参数
     * @auther wuqiang
     * @Date 2020-04-10
     * @Param [packPdVos]
     * @return java.util.List<com.zebone.nhis.ma.pub.zsba.vo.PackPdVo>
     */
    private List<PackPdVo> convertToPackList(List<PackPdMedVo> packPdVos) {
        List<PackPdVo> pclist = new ArrayList<PackPdVo>();
        for (PackPdMedVo exvo : packPdVos) {
            PackPdVo pcvo = new PackPdVo();
            ApplicationUtils.copyProperties(pcvo, exvo);
            Integer id = bdSnService.getSerialNo("order_hist", "id", 1, UserContext.getUser());
            pcvo.setOrderNum("00" + pcvo.getOrderNum());
            pcvo.setMedCd(pcvo.getMedCd() + "00");
            pcvo.setId(id);
            pcvo.setOrderDt(DateUtils.getDateStr(new Date()));
            pcvo.setOrderDtm(DateUtils.getDateTimeStr(new Date()));
            pcvo.setPtntTel(exvo.getCodeBag());
            pcvo.setUseAtcYn("Y");
            pcvo.setTakeDays("1");
            pcvo.setDataClsf("N");
            pcvo.setInOutClsf("I");
            pcvo.setXmlFlag("N");
            pclist.add(pcvo);
        }
        return pclist;
    }
}
