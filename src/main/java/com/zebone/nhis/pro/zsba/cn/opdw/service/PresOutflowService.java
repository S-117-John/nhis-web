package com.zebone.nhis.pro.zsba.cn.opdw.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.cn.opdw.OutpresDrug;
import com.zebone.nhis.common.module.cn.opdw.OutpresInfo;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.ma.pub.zsba.vo.outflow.*;
import com.zebone.nhis.pro.zsba.cn.opdw.dao.PresOutflowMapper;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.OutflowCheckPresVo;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.OutflowDownResultVo;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.OutflowPresVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.build.BuildSql;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PresOutflowService {

    @Resource
    private PresOutflowMapper presOutflowMapper;

    /**
     * 执行新增，有则覆盖
     *
     * @param voList
     */
    public OutflowDownResultVo addOutDrug(List<DrugVo> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return null;
        }
        User user = UserContext.getUser();
        Set<String> outIds = voList.stream().map(vo -> vo.getDrugContrastId().substring(0, vo.getDrugContrastId().length() - 2)).collect(Collectors.toSet());
        List<BdPd> pdList = DataBaseHelper.queryForList("select pd.PK_PD,pd.CODE,pd.SPEC,pd.CODE_EXT from BD_PD pd where pd.CODE_EXT in("
                + CommonUtils.convertSetToSqlInPart(outIds, "CODE") + ")", BdPd.class);
        if (CollectionUtils.isEmpty(pdList)) {
            throw new BusException("没有匹配到药品数据");
        }
        OutflowDownResultVo outflowDrugVo = new OutflowDownResultVo();
        Map<String, BdPd> pdMap = pdList.parallelStream().collect(Collectors.toMap(BdPd::getCodeExt, vo -> vo, (k1, k2) -> k1));
        StringBuilder sblNoExists = new StringBuilder();
        List<OutpresDrug> list = voList.stream().map(vo -> {
            BdPd bdPd = pdMap.get(vo.getDrugContrastId().substring(0, vo.getDrugContrastId().length() - 2));
            if (bdPd == null) {
                sblNoExists.append(vo.getDrugName()).append(":").append(vo.getDrugContrastId()).append(",");
                return null;
            }
            OutpresDrug drug = new OutpresDrug();
            drug.setCode(vo.getDrugContrastId());
            drug.setDrugId(vo.getDrugId());
            drug.setName(vo.getDrugName());
            drug.setSupplierName(vo.getSupplierName());
            drug.setPkPd(bdPd.getPkPd());
            drug.setSpec(bdPd.getSpec());
            ApplicationUtils.setDefaultValue(drug, true);
            return drug;
        }).filter(vo -> vo != null).collect(Collectors.toList());
        //删除已有的
        Set<String> collect = list.parallelStream().map(vo -> vo.getPkPd()).collect(Collectors.toSet());
        DataBaseHelper.execute("delete from OUTPRES_DRUG where PK_PD in(" + CommonUtils.convertSetToSqlInPart(collect, "PK_PD") + ")");
        //执行新增即可
        DataBaseHelper.batchUpdate(BuildSql.buildInsertSqlWithClass(OutpresDrug.class), list);
        outflowDrugVo.setFailString(sblNoExists.toString());
        outflowDrugVo.setSuccessCount(list.size());
        return outflowDrugVo;
    }

    public void modOutPresInfo(AuditResult auditResult) {
        DataBaseHelper.update("update outpres_info set result=?,fileId=?,fileUrl=?,checkdr=?,remark=? where pres_id=? "
                , new Object[]{auditResult.getResult(), auditResult.getFileId(), auditResult.getFileUrl()
                        , auditResult.getCheckDr(), auditResult.getRemark()
                        , auditResult.getRecipeIdOutter()});
    }

    /**
     * 修改处方类型、状态
     *
     * @param presNo
     */
    public void modCnPresStatus(String presNo) {
        DataBaseHelper.update("update CN_PRESCRIPTION set DT_PRESTYPE='01' where PRES_NO=? and DT_PRESTYPE='07'", new Object[]{presNo});
        DataBaseHelper.update("update outpres_info set eu_status='03' where pres_id=? and eu_status!='05'", new Object[]{presNo});
    }

    /**
     * 查询诊金支付状态
     *
     * @param codePv
     * @return
     */
    public Map<String, Object> getPaySettle(String codePv) {
        List<Map<String, Object>> list = DataBaseHelper.queryForList("select st.AMOUNT_ST,st.DATE_ST from BL_SETTLE st inner join BL_OP_DT dt on st.PK_SETTLE=dt.PK_SETTLE " +
                " inner join PV_ENCOUNTER pv on dt.PK_PV = pv.PK_PV inner join BD_ITEMCATE cate on cate.PK_ITEMCATE =dt.PK_ITEMCATE" +
                " where st.FLAG_CANC='0' and cate.CODE='0041' and pv.CODE_PV=?", new Object[]{codePv});
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 修改外流处方信息
     *
     * @param presOrder
     */
    public void modOutPresStatus(PresOrder presOrder) {
        //接入外部传入，不要用直接追加的sql,防止注入
        List<PresOrder> dataList = null;
        StringBuilder sbl = new StringBuilder();
        sbl.append(" from OUTPRES_INFO op inner join CN_PRESCRIPTION pres on op.PK_PRES=pres.PK_PRES inner join PV_ENCOUNTER pv on pres.PK_PV=pv.PK_PV ")
                .append("  where pv.CODE_PV=:visitIdOutter");
        if (StringUtils.isBlank(presOrder.getRecipeIdOutter())) {
            dataList = Lists.newArrayList(presOrder);
        } else {
            dataList = Stream.of(presOrder.getRecipeIdOutter().split(","))
                    .filter(v -> StringUtils.isNotBlank(v))
                    .map(vo -> {
                        PresOrder order = new PresOrder();
                        BeanUtils.copyProperties(presOrder, order);
                        order.setRecipeIdOutter(vo);
                        return order;
                    }).collect(Collectors.toList());
            sbl.append(" and pres.PRES_NO=:recipeIdOutter");
        }
        if ("10".equals(presOrder.getNoticeType())) {
            DataBaseHelper.batchUpdate("update op set op.EU_STATUS=:orderStatus " + sbl.toString(), dataList);
        } else if ("11".equals(presOrder.getNoticeType())) {
            DataBaseHelper.batchUpdate("update op set op.PAYSTATUS=:payStatus,op.PAYAMT=:payAmt,op.PAYTIME=to_date(:payTime,'yyyy-MM-dd hh24:mi:ss') " +
                    sbl.toString(), dataList);
        }
    }

    /**
     * 1.检查所有的处方是否全部外流
     * 2.外流，返回药店信息
     *
     * @param param
     * @param user
     */
    public OutflowCheckPresVo checkOutflow(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String drugType;
        if (StringUtils.isBlank(MapUtils.getString(paramMap, "pkPv"))
                || MapUtils.getObject(paramMap, "pkPresList") == null
                || StringUtils.isBlank(drugType = MapUtils.getString(paramMap, "drugType"))) {
            throw new BusException("请传入pkPv/pkPresList/drugType(21:西药 22:成药 23:中草药 24:其他)");
        }
        OutflowCheckPresVo checkVo = new OutflowCheckPresVo();
        if (Optional.ofNullable(presOutflowMapper.qryNotExists(paramMap)).orElse(0) > 0) {
            checkVo.setOutflow(0);
            return checkVo;
        }
        //全部是外流处方，调用接口获取药店信息{依据入参是否查询药店信息--目前又说不要了，o(╯□╰)o}
        checkVo.setOutflow(1);
        if ("1".equals(MapUtils.getString(paramMap, "isReqStore"))) {
            List<Map<String, Object>> listDrug = presOutflowMapper.qryPresBd(paramMap);
            if (CollectionUtils.isEmpty(listDrug)) {
                throw new BusException("依据处方未获取到药品信息");
            }
            List<StoreVo> storeList = (List<StoreVo>) ExtSystemProcessUtils.processExtMethod("preOutflow", "getStoreAndDrugByDrugId"
                    , new Object[]{drugType, listDrug});
            checkVo.setStoreList(storeList);
        }
        return checkVo;
    }

    /**
     * 上传处方信息操作
     *
     * @param param
     * @param user
     */
    public void uploadPres(String param, IUser user) {
        OutflowPresVo uploadPresVo = JsonUtil.readValue(param, OutflowPresVo.class);
        if (StringUtils.isBlank(uploadPresVo.getPkPv()) || CollectionUtils.isEmpty(uploadPresVo.getPkPresList())) {
            throw new BusException("请传入pkPv/pkPresList");
        }
        //添加逻辑，先判断是否已经上传如果上传则先撤销
        revocationOutflow(uploadPresVo);
        Map<String, String> patient = new HashMap<>();
        patient.put("patientName", uploadPresVo.getPatientName());
        patient.put("phone", uploadPresVo.getPhone());
        List<PresUpResponse> upResponseList = (List<PresUpResponse>) ExtSystemProcessUtils.processExtMethod("preOutflow", "uploadPreInfo"
                , new Object[]{uploadPresVo.getPkPv(), uploadPresVo.getPkPresList(), patient});
        if (CollectionUtils.isNotEmpty(upResponseList)) {
            String pkPresIn = CommonUtils.convertListToSqlInPart(uploadPresVo.getPkPresList());
            List<CnPrescription> listPres = DataBaseHelper.queryForList("select PK_PRES,PRES_NO,DATE_PRES from CN_PRESCRIPTION where PK_PRES in(" + pkPresIn + ") and pk_pv=?",
                    CnPrescription.class, new Object[]{uploadPresVo.getPkPv()});
            Map<String, CnPrescription> mapPres = listPres.parallelStream().collect(Collectors.toMap(CnPrescription::getPresNo, vo -> vo, (k1, k2) -> k1));
            List<OutpresInfo> infoList = upResponseList.parallelStream().map(vo -> {
                CnPrescription cn = mapPres.get(vo.getRecipeIdOutter());
                OutpresInfo info = new OutpresInfo();
                if (cn != null) {
                    info.setPkPres(cn.getPkPres());
                    info.setOrderTime(cn.getDatePres());
                }
                info.setPresId(vo.getRecipeIdOutter());
                info.setEuStatus("01");
                info.setFileid(vo.getFileId());
                info.setFileurl(vo.getFileUrl());
                info.setPaystatus(EnumerateParameter.ZERO);
                info.setOrderNo(vo.getRecipeIdOutter());
                info.setBuydrugcode(vo.getBuyDrugCode());
                info.setDrugstores(JSONObject.toJSONString(vo.getDrugStores()));
                UserContext.setUser(user);
                ApplicationUtils.setDefaultValue(info, true);
                return info;
            }).collect(Collectors.toList());
            DataBaseHelper.update("update CN_PRESCRIPTION set DT_PRESTYPE='07' where PK_PRES in(" + pkPresIn + ") and pk_pv=?", new Object[]{uploadPresVo.getPkPv()});
            DataBaseHelper.execute("delete from OUTPRES_INFO where PK_PRES in(" + pkPresIn + ")");
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(OutpresInfo.class), infoList);
        } else {
            throw new BusException("处方外流上传失败");
        }
    }

    /**
     * 撤销外流
     *
     * @param param
     * @param user
     */
    public void revocationOutflow(String param, IUser user) {
        OutflowPresVo presVo = JsonUtil.readValue(param, OutflowPresVo.class);
        if (StringUtils.isBlank(presVo.getPkPv()) || CollectionUtils.isEmpty(presVo.getPkPresList())) {
            throw new BusException("请传入pkPv/pkPresList");
        }
        List<CnPrescription> listPres = DataBaseHelper.queryForList("select PK_PRES,PRES_NO from CN_PRESCRIPTION where PK_PRES in(" + CommonUtils.convertListToSqlInPart(presVo.getPkPresList()) + ") and pk_pv=?",
                CnPrescription.class, new Object[]{presVo.getPkPv()});
        boolean flagDel = "0".equals(presVo.getStatus());
        CopyOnWriteArrayList fail = new CopyOnWriteArrayList();
        listPres.parallelStream().forEach(pre -> {
            try {
                ExtSystemProcessUtils.processExtMethod("preOutflow", "cancelUploadPres", new Object[]{pre.getPresNo()});
                if (!flagDel) {
                    DataBaseHelper.update("update CN_PRESCRIPTION set DT_PRESTYPE='01' where PK_PRES=? and pk_pv=?", new Object[]{pre.getPkPres(), presVo.getPkPv()});
                }
                DataBaseHelper.update("update outpres_info set eu_status='02' where PK_PRES=?", new Object[]{pre.getPkPres()});
            } catch (Exception e) {
                DataBaseHelper.update("update outpres_info set eu_status='06' where PK_PRES=?", new Object[]{pre.getPkPres()});
                fail.add(pre.getPresNo());
            }
        });
        if (fail.size() > 0) {
            throw new BusException("撤回失败处方号：" + StringUtils.join(fail, ","));
        }
    }

    /**
     * 缴费信息--诊察费状态更新
     *
     * @param param
     * @param user
     */
    public void modPresPayInfo(String param, IUser user) {
        Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
        String codePv = MapUtils.getString(paraMap, "codePv");
        Double amount = MapUtils.getDouble(paraMap, "amount");
        if (codePv == null || amount == null) {
            throw new BusException("请传入codePv、amount");
        }
        ExtSystemProcessUtils.processExtMethod("preOutflow", "updatePayInfo", new Object[]{codePv, amount});
    }

    /**
     * @return void
     * @Description 在保存前查询处方是否已经上传，上传则先撤销
     * @auther wuqiang
     * @Date 2021-03-11
     * @Param [presVo]
     */
    private void revocationOutflow(OutflowPresVo presVo) {
        List<CnPrescription> listPres = DataBaseHelper.queryForList("select CR.PK_PRES, CR.PRES_NO " +
                        "from CN_PRESCRIPTION CR " +
                        "         inner join outpres_info oi on oi.PK_PRES = CR.PK_PRES " +
                        "where oi.EU_STATUS in ('01', '06', '03') " +
                        "  and CR.PK_PRES in(" + CommonUtils.convertListToSqlInPart(presVo.getPkPresList()) + ") and pk_pv=?",
                CnPrescription.class, new Object[]{presVo.getPkPv()});
        boolean flagDel = "0".equals(presVo.getStatus());
        if (CollectionUtils.isEmpty(listPres)) {
            return;
        }
        CopyOnWriteArrayList fail = new CopyOnWriteArrayList();
        listPres.parallelStream().forEach(pre -> {
            try {
                ExtSystemProcessUtils.processExtMethod("preOutflow", "cancelUploadPres", new Object[]{pre.getPresNo()});
                if (!flagDel) {
                    DataBaseHelper.update("update CN_PRESCRIPTION set DT_PRESTYPE='01' where PK_PRES=? and pk_pv=?", new Object[]{pre.getPkPres(), presVo.getPkPv()});
                }
                DataBaseHelper.update("update outpres_info set eu_status='02' where PK_PRES=?", new Object[]{pre.getPkPres()});
            } catch (Exception e) {
                DataBaseHelper.update("update outpres_info set eu_status='06' where PK_PRES=?", new Object[]{pre.getPkPres()});
                fail.add(pre.getPresNo());
            }
        });
        if (fail.size() > 0) {
            throw new BusException("撤回失败处方号：" + StringUtils.join(fail, ","));
        }
    }
}
