package com.zebone.nhis.webservice.zhongshan.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.emr.comm.service.EmrCommService;
import com.zebone.nhis.emr.comm.vo.EmrMedRecPatVo;
import com.zebone.nhis.webservice.zhongshan.dao.TaiKangMapper;
import com.zebone.nhis.webservice.zhongshan.support.EmrDataUtils;
import com.zebone.nhis.webservice.zhongshan.vo.*;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author wq
 * @Classname TaiKangService
 * @Description 泰康对外HIS业务实现类
 * @Date 2020-11-24 9:59
 */
@Service
public class TaiKangService {
    @Autowired
    private TaiKangMapper taiKangMapper;
    private Logger logger = LoggerFactory.getLogger("nhis.zsbaWebSrv");


    @Autowired
    private EmrCommService emrCommService;

    /**
     * @return java.lang.String
     * @Description 查询病人入院信息数据
     * @auther wuqiang
     * @Date 2020-11-24
     * @Param [reqXml]
     */
    public String getPatientAdmInf(String reqXml) {
        try {
            TaiKangRequestAdmOutVo requestAdmOutVo = (TaiKangRequestAdmOutVo) XmlUtil.XmlToBean(reqXml, TaiKangRequestAdmOutVo.class);
            boolean boolQueryA = StringUtils.isNotBlank(requestAdmOutVo.getCodeIp())
                    && requestAdmOutVo.getDateAdmit() != null;
            boolean boolQueryB = StringUtils.isNotBlank(requestAdmOutVo.getDtIdtype())
                    && StringUtils.isNotBlank(requestAdmOutVo.getIdNo())
                    && StringUtils.isNotBlank(requestAdmOutVo.getName())
                    && requestAdmOutVo.getDataBegin() != null
                    && requestAdmOutVo.getDataEnd() != null
                    && requestAdmOutVo.getDataBegin().compareTo(requestAdmOutVo.getDataEnd()) <= 0;
            boolean boolQueryC = StringUtils.isNotBlank(requestAdmOutVo.getCodeIp())
                    && requestAdmOutVo.getDateAdmit() != null
                    && StringUtils.isNotBlank(requestAdmOutVo.getDtIdtype())
                    && StringUtils.isNotBlank(requestAdmOutVo.getIdNo())
                    && StringUtils.isNotBlank(requestAdmOutVo.getName());
            if (!(boolQueryA || boolQueryB || boolQueryC)) {
                logger.error("getPatientAdmInf 查询条件不符合约定的形式，请检查 " + reqXml);
                return "";
            }
            TaiKangPatientAdmInf patientAdmInf = taiKangMapper.getPatientAdmInf(requestAdmOutVo);
            if (patientAdmInf == null) {
                logger.error("getPatientAdmInf 未找到有效记录 " + reqXml);
                return "";
            }
            return XmlUtil.beanToXml(patientAdmInf, TaiKangPatientAdmInf.class);
        } catch (Exception e) {
            logger.error("getPatientAdmInf报错" + reqXml + e.getMessage());
            return "";
        }
    }

    /**
     * @return java.lang.String
     * @Description 获取病人费用信息数据
     * @auther wuqiang
     * @Date 2020-11-24
     * @Param [reqXml]
     */
    public String getPatientCostInfList(String reqXml) {
        try {
            TaiKangRequestCostVo requestCostVo = (TaiKangRequestCostVo) XmlUtil.XmlToBean(reqXml, TaiKangRequestCostVo.class);
            boolean boolQueryA = StringUtils.isNotBlank(requestCostVo.getCodeIp())
                    && requestCostVo.getDateAdmit() != null;
            if (!boolQueryA) {
                logger.error("getPatientCostInfList/业务流水号-入院日期不允许为空" + reqXml);
                return "";
            }
            List<TaiKangPatientCostInf> taiKangPatientCostInfList = taiKangMapper.getPatientCostInfList(requestCostVo);
            TaiKangPatientCostInfList taiKangPatientCostInfList1 = new TaiKangPatientCostInfList();
            taiKangPatientCostInfList1.setTaiKangPatientCostInfList(taiKangPatientCostInfList);
            if (taiKangPatientCostInfList1 == null) {
                logger.error("getPatientCostInfList 未找到有效记录" + reqXml);
                return "";
            }
            return XmlUtil.beanToXml(taiKangPatientCostInfList1, TaiKangPatientCostInfList.class);
        } catch (Exception e) {
            logger.error("getPatientCostInfList 报错" + reqXml + e.getMessage());
            return "";
        }
    }

    /**
     * @return java.lang.String
     * @Description获取医院医院医疗目录信息
     * @auther wuqiang
     * @Date 2020-11-25
     * @Param [reqXml]
     */
    public String getTaiKangMedicalDirectoryList(String reqXml) {
        try {
            TaiKangRequestMedVo taiKangRequestMedVo = (TaiKangRequestMedVo) XmlUtil.XmlToBean(reqXml, TaiKangRequestMedVo.class);
            if (CollectionUtils.isEmpty(taiKangRequestMedVo.getCode())) {
                logger.error("getTaiKangMedicalDirectoryList/请至少传入一个查询编码" + reqXml);
                return "";
            }
            List<TaiKangMedicalDirectory> medicalDirectories = taiKangMapper.getTaiKangMedicalDirectoryList(taiKangRequestMedVo);
            TaiKangMedicalDirectoryList medicalDirectoryList = new TaiKangMedicalDirectoryList();
            medicalDirectoryList.setTaiKangMedicalDirectoryList(medicalDirectories);
            if (medicalDirectoryList == null) {
                logger.error("getTaiKangMedicalDirectoryList /未找到有效记录" + reqXml);
                return "";
            }
            return XmlUtil.beanToXml(medicalDirectoryList, TaiKangMedicalDirectoryList.class);
        } catch (Exception e) {
            logger.error("getTaiKangMedicalDirectoryList报错" + reqXml + e.getMessage());
            return "";
        }
    }

    /**
     * @return java.lang.String
     * @Description 获取出院信息
     * @auther wuqiang
     * @Date 2020-11-25
     * @Param [reqXml]
     */
    public String getPatientOutInf(String reqXml) {
        try {
            TaiKangRequestAdmOutVo requestAdmOutVo = (TaiKangRequestAdmOutVo) XmlUtil.XmlToBean(reqXml, TaiKangRequestAdmOutVo.class);
            boolean boolQueryA = StringUtils.isNotBlank(requestAdmOutVo.getCodeIp())
                    && requestAdmOutVo.getDateAdmit() != null;
            if (!boolQueryA) {
                logger.error("getPatientOutInf/业务流水号-入院日期不允许为空" + reqXml);
                return "";
            }
            List<TaiKangPatientOutInf> taiKangPatientOutInfList = taiKangMapper.getPatientOutInf(requestAdmOutVo);
            if (CollectionUtils.isEmpty(taiKangPatientOutInfList)) {
                logger.error("getPatientOutInf/未找到有效记录" + reqXml);
                return "";
            }
            TaiKangPatientOutInf taiKangPatien = taiKangPatientOutInfList.get(0);
            //总金额-医保支付金额
            Double amouSt = 0.00;
            Double amouInsu = 0.00;
            List<TaiKangSettlementItem> taiKangSettlementItemLis = new ArrayList<TaiKangSettlementItem>(1);
            for (TaiKangPatientOutInf ta : taiKangPatientOutInfList) {
                String pkSettle = ta.getPkSettle();
                amouSt += ta.getTotalMedicalCost();
                amouInsu += ta.getBaseInsurance();
                if (StringUtils.isAnyBlank(pkSettle)) {
                    return XmlUtil.beanToXml(taiKangPatien, TaiKangPatientOutInf.class);
                }
                //查询二级费用以及发票信息
                List<TaiKangInvoiceCareStr> taiKangInvoiceCareStrs = taiKangMapper.getInvoiceCareStr(pkSettle);
                TaiKangSettlementItem taiKangSettlementItem = new TaiKangSettlementItem();
                taiKangSettlementItem.setInvoiceNo(taiKangInvoiceCareStrs.get(0).getInvoiceNo());
                taiKangSettlementItem.setSettleDate(taiKangInvoiceCareStrs.get(0).getSettleDate());
                taiKangSettlementItem.setTaiKangInvoiceCareStrs(taiKangInvoiceCareStrs);
                taiKangSettlementItemLis.add(taiKangSettlementItem);
            }
            taiKangPatien.setTaiKangSettlementItems(taiKangSettlementItemLis);
            taiKangPatien.setTotalMedicalCost(amouSt);
            taiKangPatien.setBaseInsurance(amouInsu);
            return XmlUtil.beanToXml(taiKangPatien, TaiKangPatientOutInf.class);
        } catch (Exception e) {
            logger.error("getPatientOutInf 报错" + reqXml + e.getMessage());
            return "";
        }
    }

    /**
     * @return string
     * @Description 电子病历入院记录接口
     * @auther chengjia
     * @Date 2020-12-03
     * @Param [reqXml]
     */
    public String getEmrAdmitRecs(String reqXml) {
        try {
            TaiKangReqEmrAdmitRec request = (TaiKangReqEmrAdmitRec) XmlUtil.XmlToBean(reqXml, TaiKangReqEmrAdmitRec.class);
            boolean boolQueryA = StringUtils.isNotBlank(request.getCodeIp()) && request.getDateBegin() != null;
            if (!boolQueryA) {
                logger.error("getEmrAdmitRecs/业务流水号-入院日期不允许为空" + reqXml);
                return "";
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("codeIp", request.getCodeIp());
            map.put("dateAdmit", request.getDateBegin());
            List<EmrMedRecPatVo> list = emrCommService.getEmrRecByType(map);
            TaiKangRespEmrAdmitRec resp = EmrDataUtils.genPatAdmitRec(list);
            if (resp == null) {
                logger.error("getEmrAdmitRecs/未找到有效记录" + reqXml);
                return "";
            }
            return XmlUtil.beanToXml(resp, TaiKangRespEmrAdmitRec.class);
        } catch (Exception e) {
            logger.error("getEmrAdmitRecs 报错" + reqXml + e.getMessage());
            return "";
        }
    }

    /**
     * @return java.lang.String
     * @Description 泰康人寿理赔金回写
     * @auther wuqiang
     * @Date 2020-12-04
     * @Param [inxml]
     */
    public String writeSettleInfomation(String reqXml) {
        try {
            TaiKangRequestSettleWriteVo requestSettleWriteVo = (TaiKangRequestSettleWriteVo) XmlUtil.XmlToBean(reqXml, TaiKangRequestSettleWriteVo.class);
            boolean boolQueryA = StringUtils.isNotBlank(requestSettleWriteVo.getPatientNumber())
                    && StringUtils.isNotBlank(requestSettleWriteVo.getInHosDate());
            if (!boolQueryA) {
                logger.error("getPatientOutInf/住院号/入院日期不允许为空" + reqXml);
                return "";
            }
            boolean boolQueryB = StringUtils.isNotBlank(requestSettleWriteVo.getCompensateMoney())
                    && requestSettleWriteVo.getCompensateMoney().compareTo("0") > 0;
            if (!boolQueryB) {
                logger.error("getPatientOutInf/ 金额不允许为空or负数" + reqXml);
                return "";
            }
            BigDecimal amount = BigDecimal.valueOf(Double.valueOf(requestSettleWriteVo.getCompensateMoney()));
            TkSettle tkSe = new TkSettle();
            tkSe.setTkAmount(amount);
            tkSe.setTkDateSe(DateUtils.parseDate(requestSettleWriteVo.getCompensateDate(), "yyyy-MM-dd"));
            tkSe.setTkId(requestSettleWriteVo.getCompensateId());
            tkSe.setCompanyCode(requestSettleWriteVo.getCompanyCode());
            tkSe.setCompanyName(requestSettleWriteVo.getCompanyName());
            List<Map<String, Object>> mapList = null;
            String pkPv = null;
            String pkSettle = null;
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                //写作废记录
                mapList = taiKangMapper.getPatientSettleCnacInfor(requestSettleWriteVo);
                if (CollectionUtils.isEmpty(mapList)) {
                    logger.error("writeSettleInfomation/ 找不到有效患者信息，无法作废回写" + reqXml);
                    return "";
                }
                pkPv = MapUtils.getString(mapList.get(0), "pkPv");
                pkSettle = MapUtils.getString(mapList.get(0), "pkSettle");
                String pkTksettle = MapUtils.getString(mapList.get(0), "pkTksettle");
                if (StringUtils.isNoneBlank(pkPv) && StringUtils.isNoneBlank(pkSettle) && StringUtils.isNoneBlank(pkTksettle)) {
                    logger.error("writeSettleInfomation/ 就诊主键，结算主键无效，无法作废回写" + reqXml);
                    return "";
                }
                tkSe.setPkPv(pkPv);
                tkSe.setPkSettle(pkSettle);
                tkSe.setFlagCanc("1");
                tkSe.setTkDateCnac(new Date());
                tkSe.setPkTksettleCnac(pkTksettle);
                tkSe.setNote(reqXml);
                ApplicationUtils.setDefaultValue(tkSe, true);
                UserContext.getUser();
                DataBaseHelper.insertBean(tkSe);
                String sql = " update  TK_SETTLE set FLAG_CANC='1' and TK_DATE_CNAC=? where PK_TKSETTLE=?";
                DataBaseHelper.execute(sql, new Object[]{new Date(), pkTksettle});
                return "";
            }
            mapList = taiKangMapper.getPatientSettleInfor(requestSettleWriteVo);
            if (CollectionUtils.isEmpty(mapList)) {
                logger.error("writeSettleInfomation/ 找不到有效患者信息，无法回写" + reqXml);
                return "";
            }
            if (mapList.size() > 1) {
                logger.error("writeSettleInfomation/ 找到多个重复患者信息，无法回写" + reqXml);
                return "";
            }
            pkPv = MapUtils.getString(mapList.get(0), "pkPv");
            pkSettle = MapUtils.getString(mapList.get(0), "pkSettle");
            if (StringUtils.isNoneBlank(pkPv) && StringUtils.isNoneBlank(pkSettle)) {
                logger.error("writeSettleInfomation/ 就诊主键，结算主键无效，无法回写" + reqXml);
                return "";
            }
            tkSe.setPkPv(pkPv);
            tkSe.setPkSettle(pkSettle);
            tkSe.setFlagCanc("0");
            tkSe.setNote(reqXml);
            ApplicationUtils.setDefaultValue(tkSe, true);
            UserContext.getUser();
            DataBaseHelper.insertBean(tkSe);
            return "";
        } catch (Exception e) {
            logger.error("writeSettleInfomation /回写异常" + e.getMessage() + reqXml);
            return "";
        }
    }
}
