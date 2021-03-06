package com.zebone.nhis.pro.zsba.adt.service;

import com.foxinmy.weixin4j.util.DateUtil;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.pub.vo.CnLabApplyVo;
import com.zebone.nhis.ex.pub.dao.AdtPubMapper;
import com.zebone.nhis.ex.pub.support.ExListSortByOrdUtil;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pi.pub.dao.PiPubMapper;
import com.zebone.nhis.pro.zsba.adt.dao.BAPvAdtMapper;
import com.zebone.nhis.pro.zsba.adt.dao.ZsAdtRegMapper;
import com.zebone.nhis.pro.zsba.adt.vo.*;
import com.zebone.nhis.pv.pub.vo.PagePvListVo;
import com.zebone.nhis.pv.pub.vo.PageQryPvParam;
import com.zebone.nhis.pv.pub.vo.PvEncounterListVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ZsbaAdtService {

    @Resource
    private ZsAdtRegMapper zsAdtRegMapper;
    @Autowired
    private BAPvAdtMapper baPvAdtMapper;
    @Autowired
    private AdtPubMapper adtPubMapper;
    @Resource
    private PiPubMapper piPubMapper;

    // ????????????????????????
    @Resource(name = "transactionManager")
    private PlatformTransactionManager platformTransactionManager;

    /**
     * ???HIS?????????????????????
     *
     * @return
     */
    public String getCodePiFromHis() {
        String codePi;
        codePi = DataBaseHelper.queryForScalar("select patient_sn from gh_config", String.class);
        if (CommonUtils.isEmptyString(codePi))
            throw new BusException("??????????????????????????????");
        DataBaseHelper.update("update gh_config set patient_sn = patient_sn + 1");
        codePi += "00";
        int length = 12 - codePi.length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                codePi = "0" + codePi;
            }
        }
        return codePi;
    }

    /**
     * ???HIS??????????????????
     *
     * @return
     */
    public String getCodeIpFromHis() {
        String codeIp;
        DataBaseHelper.update("update zy_configure set inpatient_no =inpatient_no + 1");
        codeIp = DataBaseHelper.queryForScalar("select inpatient_no from zy_configure", String.class);
        if (CommonUtils.isEmptyString(codeIp))
            throw new BusException("???????????????????????????");
        return codeIp;
    }

    /**
     * ??? HIS ?????????????????????????????????
     *
     * @param map
     * @return
     */
    public List<ZsbaHisAdtPv> getHisPvList(Map<String, Object> map) {
        List<ZsbaHisAdtPv> list = new ArrayList<ZsbaHisAdtPv>();
        list = zsAdtRegMapper.getHisPvList(map);
        return list;
    }

    /**
     * ??? NHIS ?????????????????????????????????
     *
     * @param map
     * @return
     */
    public List<ZsbaHisAdtPv> getNhisPvList(Map<String, Object> map) {
        List<ZsbaHisAdtPv> list = new ArrayList<ZsbaHisAdtPv>();
        list = zsAdtRegMapper.getNhisPvList(map);
        return list;
    }

    /**
     * ??? NHIS ?????????????????????????????????
     *
     * @param map
     * @return
     */
    public List<ZsbaHisAdtPv> getBaglPvList(Map<String, Object> map) {
        List<ZsbaHisAdtPv> list = new ArrayList<ZsbaHisAdtPv>();
        list = zsAdtRegMapper.getBaglPvList(map);
        return list;
    }

    /**
     * ??? ???his?????????????????????????????????
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getOpFeeListFromHis(Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = zsAdtRegMapper.getOpFeeListFromHis(map);
        return list;
    }

    /**
     * ??? Nhis?????????????????????????????????
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getOpFeeListFromNhis(Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = zsAdtRegMapper.getOpFeeListFromNhis(map);
        return list;
    }

    /**
     * ??? ???his????????????????????????
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getHisDeptList(Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = zsAdtRegMapper.getHisDeptList(map);
        return list;
    }

    /**
     * ??? Nhis?????????????????????????????????
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getNhisDeptMapList(Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = zsAdtRegMapper.getNhisDeptMapList(map);
        return list;
    }

    /**
     * 6.??? Nhis?????? ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public void saveNhisDeptMapList(List<NhisDeptMap> listAdd, List<NhisDeptMap> listUp, List<NhisDeptMap> listDel) {
        int cntErr = 0;
        //1???????????????
        if (null != listDel && listDel.size() > 0) {
            for (NhisDeptMap del : listDel) {
                cntErr = DataBaseHelper.execute("delete from nhis_dept_map where pk_nhis_dept_map = ? and to_char(ts,'yyyyMMddHH24Miss') = ? "
                        , del.getPkNhisDeptMap(), DateUtil.formatDate(del.getTs(), "yyyyMMddHHmmss"));
                if (cntErr != 1)
                    throw new BusException("??????[" + del.getHisWardCode()
                            + " - " + del.getHisWardName() + "]?????????????????????????????????");
            }
        }
        //2???????????????
        if (null != listAdd && listAdd.size() > 0) {
            //?????????????????????-??????????????????
            //DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(NhisDeptMap.class), listUp);
            for (NhisDeptMap add : listAdd) {
                ApplicationUtils.setDefaultValue(add, true);
                cntErr = DataBaseHelper.insertBean(add);
                if (cntErr != 1)
                    throw new BusException("??????[" + add.getHisWardCode()
                            + " - " + add.getHisWardName() + "]?????????????????????????????????");
            }
        }
        //3???????????????
        if (null != listUp && listUp.size() > 0) {
            //????????????
            //DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(NhisDeptMap.class), listUp);
            for (NhisDeptMap up : listUp) {
                up.setModifier(UserContext.getUser().getPkEmp());
                up.setModityTime(new Date());
                cntErr = DataBaseHelper.updateBeanByPk(up, true);
                if (cntErr != 1)
                    throw new BusException("??????[" + up.getHisWardCode()
                            + " - " + up.getHisWardName() + "]?????????????????????????????????");
            }
        }
    }

    /**
     * ??? his ????????????????????????
     *
     * @param map
     * @return
     */
    public List<TPatientWxMapper> getPatientWxList(Map<String, Object> map) {
        List<TPatientWxMapper> list = new ArrayList<TPatientWxMapper>();
        list = zsAdtRegMapper.getPatientWxList(map);
        return list;
    }

    /**
     * ??? nhis ????????????????????????
     *
     * @param map
     * @return
     */
    public List<NHISPatientWxMapper> getNPatientWxList(Map<String, Object> map) {
        List<NHISPatientWxMapper> list = new ArrayList<NHISPatientWxMapper>();
        list = zsAdtRegMapper.getNPatientWxList(map);
        return list;
    }

    /**
     * ?????? his???nhis ????????????????????????
     *
     * @param map
     * @return
     */
    public void updatePatientWxList(List<TPatientWxMapper> oldList, Map<String, Object> map) {
        NHISPatientWxMapper tpw = null;
        String codeIp = CommonUtils.getString(map.get("ipPatientId"), "");
        String pkPi = CommonUtils.getString(map.get("pkPi"), "");
        List<NHISPatientWxMapper> newList = getNPatientWxList(map);
        List<NHISPatientWxMapper> addList = new ArrayList<NHISPatientWxMapper>();
        String delPkList = "";
        if ((null == newList && newList.size() < 1) || (newList.size() > 0 && !newList.get(0).getPkPi().equals(pkPi))) {
            //????????????
            for (TPatientWxMapper otp : oldList) {
                tpw = new NHISPatientWxMapper();
                ApplicationUtils.setDefaultValue(tpw, true);
                tpw.setId(otp.getId());
                tpw.setPatientId(otp.getPatientId());
                tpw.setRelationCode(otp.getRelationCode());
                tpw.setWxOpenId(otp.getWxOpenId());
                tpw.setPkPi(pkPi);
                tpw.setIpPatientId(codeIp);
                addList.add(tpw);
            }
        } else {
            int cntHas = 0;
            //?????????????????????????????????????????????????????????
            for (TPatientWxMapper otp : oldList) {
                cntHas = 0;
                for (NHISPatientWxMapper ntp : newList) {
                    if (ntp.getPatientId().equals(otp.getPatientId())
                            && ntp.getWxOpenId().equals(otp.getWxOpenId())
                            && ntp.getPkPi().equals(pkPi)) {
                        cntHas = 1;
                        break;
                    }
                }
                if (cntHas < 1) {
                    tpw = new NHISPatientWxMapper();
                    ApplicationUtils.setDefaultValue(tpw, true);
                    tpw.setId(otp.getId());
                    tpw.setPatientId(otp.getPatientId());
                    tpw.setRelationCode(otp.getRelationCode());
                    tpw.setWxOpenId(otp.getWxOpenId());
                    tpw.setPkPi(pkPi);
                    tpw.setIpPatientId(codeIp);
                    addList.add(tpw);
                }
            }
            //?????????????????????????????????????????????????????????
            for (NHISPatientWxMapper ntp : newList) {
                for (TPatientWxMapper otp : oldList) {
                    if (ntp.getPatientId().equals(otp.getPatientId())
                            && ntp.getWxOpenId().equals(otp.getWxOpenId())
                            && ntp.getIpPatientId().equals(codeIp)) {
                        cntHas = 1;
                        break;
                    }
                }
                if (cntHas < 1) {
                    delPkList += "'" + ntp.getPkPatientWxMapper() + "',";
                }
            }
        }
        if (addList.size() > 0)
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(NHISPatientWxMapper.class), addList);
        if (delPkList.length() > 0) {
            delPkList = delPkList.substring(0, delPkList.length() - 1);
            DataBaseHelper.update("delete from t_patient_wx_mapper where pk_patient_wx_mapper in (" + delPkList + ")", new Object[]{});
        }
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public PagePvListVo getPvEncounterListPaging(String param, IUser user) {
        PagePvListVo piListAndTotal = new PagePvListVo();
        PageQryPvParam qryparam = JsonUtil.readValue(param,
                PageQryPvParam.class);
        if (qryparam == null) {
            throw new BusException("????????????????????????");
        }
        int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
        int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
        // ????????????
        String pkOrg = UserContext.getUser().getPkOrg();
        qryparam.setPkOrg(pkOrg);
        MyBatisPage.startPage(pageIndex, pageSize);
        List<PvEncounterListVo> list = baPvAdtMapper.getPvEncounterVoList(qryparam);
        Page<List<PvEncounterListVo>> page = MyBatisPage.getPage();
        piListAndTotal.setPatiList(list);
        piListAndTotal.setTotalCount(page.getTotalCount());
        return piListAndTotal;
    }


    /**
     * ??? his????????????????????????
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryPatiListByWg(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = zsAdtRegMapper.queryPatiListByWg(map);
        return list;
    }

    /**
     * ????????????????????????????????????
     *
     * @param param{pkOrd}
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryBlCgIpDetailsByOrd(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //????????????????????????
        List<Map<String, Object>> ords = zsAdtRegMapper.queryBlCgIpDetailsByOrd(map);
        if (null != ords && ords.size() > 0)
            list.addAll(ords);
        //??????????????????
        List<Map<String, Object>> samps = zsAdtRegMapper.querySampFeeByOrd(map);
        if (null != samps && samps.size() > 0)
            list.addAll(samps);
        return list;
    }

    /**
     * ??????????????????????????????
     *
     * @param param{pkPvs,dateBegin,dateEnd,type,pkDept,pkDeptEx,codeItemcate}
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryPatiDetailsByPv(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        return zsAdtRegMapper.queryPatiDetails(map);
    }

    /**
     * ????????????????????????????????????
     *
     * @param param{pkPv,pkItem}
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryCgDetaileds(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        return zsAdtRegMapper.queryCgDetaileds(map);
    }


    /**
     * ??????????????????????????????
     * 2020-08-04 ?????????????????????????????????
     *
     * @param param{pkPv}
     * @param user
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> deptChangeVerfyByBoAi(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if ((null == paramMap.get("pkPv") || CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
                && (null == paramMap.get("pkPvMa") || CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString()))) {
            throw new BusException("pkPv?????????????????????????????????????????????????????????");
        }
        if (null != paramMap.get("pkPvMa") && !CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString())) {
            List<String> pkPvs = getPkpvOfMaAndInfByPkpv(paramMap);
            paramMap.put("pkPvMa", pkPvs);
        }
        return getDeptOutVerfyDataBySyx(paramMap, user);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param map
     * @return
     */
    private List<String> getPkpvOfMaAndInfByPkpv(Map<String, Object> map) {
        List<String> list = new ArrayList<String>();
        list.add(map.get("pkPvMa").toString());
        List<PvEncounter> listInf = DataBaseHelper.queryForList("select inf.pk_pv_infant pk_pv from pv_infant inf "
                        + "inner join pv_encounter pvi on pvi.pk_pv = inf.pk_pv_infant "
                        + "where pvi.flag_in = '1' and inf.pk_pv =:pkPvMa and pvi.pk_dept_ns =:pkDeptNs "
                , PvEncounter.class, map);
        if (null != listInf && listInf.size() > 0) {
            for (PvEncounter pv : listInf) {
                list.add(pv.getPkPv());
            }
        }
        return list;
    }


    /***
     * ????????????????????????????????????
     * ???2020-08-04???????????????????????????
     * @param param{pkPv}
     * @return
     */
    public Map<String, Object> getDeptOutVerfyDataBySyx(Map<String, Object> param, IUser u) {
        User user = (User) u;
        param.put("pkDeptOcc", user.getPkDept());
        param.put("dbType", "sqlserver");
        if (null != param.get("dateEnd") && !CommonUtils.isEmptyString(param.get("dateEnd").toString()))
            param.put("dateEndDay", param.get("dateEnd").toString().substring(0, 8));
        List<Map<String, Object>> ordData = adtPubMapper.queryOrdByPkPv(param);//?????????????????????
        List<Map<String, Object>> ordReaseData = adtPubMapper.queryOrdReaseByPkPv(param);//?????????????????????
        if (ordReaseData.size() > 0) {
            ordData.addAll(ordReaseData);
        }
        List<Map<String, Object>> risData = adtPubMapper.queryRisByPkPvForBoAi(param);//?????????????????????????????????????????????
        //????????????????????????BA001???????????????BA001=3 ???????????????<3????????????????????????BA001=2  ???????????????<2???????????????????????? ???????????????<1??????????????????
        if (risData.size() > 0) {
            List<Map<String, Object>> risDataNew = new ArrayList<>();
            for (Map<String, Object> map : risData) {
                String BA001 = map.containsKey("valAttr") ? map.get("valAttr") != null ? map.get("valAttr").toString() : "" : "";
                String euStatus = map.containsKey("euStatusApply") ? map.get("euStatusApply") != null ? map.get("euStatusApply").toString() : "" : "";
                if (BA001 != null && euStatus != null && !"".equals(euStatus)) {
                    int status = Integer.parseInt(euStatus);
                    if ("3".equals(BA001) && status < 3) {
                        risDataNew.add(map);
                    } else if ("2".equals(BA001) && status < 2) {
                        risDataNew.add(map);
                    } else if (!"3".equals(BA001) && !"2".equals(BA001) && status < 1) {
                        risDataNew.add(map);
                    }
                } else {
                    risDataNew.add(map);
                }
            }
            risData = risDataNew;
        }
        List<Map<String, Object>> apData = adtPubMapper.queryPdapByPkPv(param);//??????????????????
        List<Map<String, Object>> opData = adtPubMapper.queryOpByPkPv(param);//????????????????????????
        List<Map<String, Object>> packBedData = adtPubMapper.queryPacketBedByPv(param);//????????????
        List<ExlistPubVo> exListData = adtPubMapper.queryExlistByPv(param);//??????????????????
        List<Map<String, Object>> hpCgChkData = new ArrayList<>();
        List<Map<String, Object>> nsCgChkData = adtPubMapper.queryNsCgChkByPv(param);//????????????????????????????????????
        if (param.get("isDeptChg") != null && !"1".equals(param.get("isDeptChg"))) {
            hpCgChkData = adtPubMapper.queryHpCgChkByPv(param);//???????????????????????????
        }
        if (nsCgChkData.size() > 0) {
            hpCgChkData.addAll(nsCgChkData);
        }
        List<Map<String, Object>> groupCgChkData = adtPubMapper.queryGroupByPvForSql(param);//???????????????????????????????????????
        //????????????
        new ExListSortByOrdUtil().ordGroup(exListData);
        List<Map<String, Object>> cpData = adtPubMapper.queryCpByPv(param);//?????????/??????????????????;
        List<Map<String, Object>> infData = adtPubMapper.queryInfByPv(param);//????????????????????????????????? --2020-04-09 ??????bug-25689 ?????????????????????
        List<Map<String, Object>> numDate = adtPubMapper.queryNumByPk(param);//??????????????????50??????????????????????????????---?????????????????????????????????????????????
        Map<String, Object> result = new HashMap<String, Object>();
        if (!isEmpty(ordData)) {
            result.put("ordData", ordData);
        }
        if (!isEmpty(risData)) {
            result.put("risData", risData);
        }
        if (!isEmpty(apData)) {
            result.put("apData", apData);
        }
        if (!isEmpty(opData)) {
            result.put("opData", opData);
        }
        if (!isEmpty(packBedData)) {
            result.put("packBedData", packBedData);
        }
        if (!isEmpty(exListData)) {
            result.put("exListData", exListData);
        }
        if (!isEmpty(cpData)) {
            result.put("cpData", cpData);
        }
        if (!isEmpty(numDate)) {
            result.put("numDate", numDate);
        }
        if (!isEmpty(infData)) {
            result.put("infData", infData);
        }
        if (!isEmpty(hpCgChkData)) {
            double amount = 0.0;
            for (Map<String, Object> map : hpCgChkData) {
                amount += Double.parseDouble(map.get("amount").toString());
            }
            if (amount > 0)
                result.put("hpCgChkData", hpCgChkData);
        }
        if (!isEmpty(groupCgChkData)) {
            result.put("groupCgChkData", groupCgChkData);
        }
        return result;
    }

    /**
     * ??????????????????
     *
     * @param obj
     * @return
     */
    private boolean isEmpty(Object obj) {
        if (obj == null) return true;
        return false;
    }

    /**
     * @return java.util.List<com.zebone.nhis.ex.nis.pub.vo.CnLabApplyVo>
     * @Description ???????????????????????????????????????
     * @auther wuqiang
     * @Date 2020-09-25
     * @Param [paramMap]
     */
    public List<CnLabApplyVo> getCnlabApplyVoNotDone(Map<String, Object> paramMap) {
        getQueryMap(paramMap);
        return adtPubMapper.getCnlabApplyVoNotDone(paramMap);
    }

    /**
     * @return java.util.List<com.zebone.nhis.ex.nis.pub.vo.CnLabApplyVo>
     * @Description ???????????????????????????????????????
     * @auther wuqiang
     * @Date 2020-09-25
     * @Param [paramMap]
     */
    public List<CnRisApplyBaVo> getCnRisApplyVoNotDone(Map<String, Object> paramMap) {
        getQueryMap(paramMap);
        return adtPubMapper.getCnRisApplyVoNotDone(paramMap);
    }


    /**
     * @return void
     * @Description ??????????????????
     * @auther wuqiang
     * @Date 2020-09-25
     * @Param [paramMap]
     */
    private void getQueryMap(Map<String, Object> paramMap) {
        if (null != paramMap.get("pkPvMa") && !CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString())) {
            List<String> pkPvs = getPkpvOfMaAndInfByPkpv(paramMap);
            paramMap.put("pkPvMa", pkPvs);
        }
        if (null != paramMap.get("dateEnd") && !CommonUtils.isEmptyString(paramMap.get("dateEnd").toString()))
            paramMap.put("dateEndDay", paramMap.get("dateEnd").toString().substring(0, 8));
    }

    /**
     * @return void
     * @Description ??????????????????????????????????????????
     * @auther wuqiang
     * @Date 2020-09-25
     * @Param [labTripartiteSystemVos??????????????????, cnlabApplyVoNotDone Nhis????????????,
     * risTripartiteSystemVos ??????????????????, cnRisApplyVoNotDone Nhis????????????]
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateEustus(List<LabAndRisTripartiteSystemVo> labTripartiteSystemVos, List<CnLabApplyVo> cnlabApplyVoNotDone, List<LabAndRisTripartiteSystemVo> risTripartiteSystemVos, List<CnRisApplyBaVo> cnRisApplyVoNotDone, User user) {
        boolean lab = !CollectionUtils.isEmpty(labTripartiteSystemVos) && !CollectionUtils.isEmpty(cnlabApplyVoNotDone);
        boolean ris = !CollectionUtils.isEmpty(risTripartiteSystemVos) && !CollectionUtils.isEmpty(cnRisApplyVoNotDone);
        if (lab) {
            List<String> sqlC = new ArrayList<>(16);
            List<ExLabOcc> labOccs = new ArrayList<>(16);
            for (CnLabApplyVo cnLabApplyVo : cnlabApplyVoNotDone) {
                for (LabAndRisTripartiteSystemVo labT : labTripartiteSystemVos) {
                    boolean isSameOne = cnLabApplyVo.getCodeIp().equals(labT.getPatientId()) && cnLabApplyVo.getSampNo().equals(labT.getSampNo());
                    if (isSameOne) {
                        sqlC.add("update cn_lab_apply set eu_status='" + labT.getEuStatu() + "' where PK_ORDLIS='" + cnLabApplyVo.getPkOrdlis() + "'");
                        ExLabOcc exLabOcc = new ExLabOcc();
                        exLabOcc.setPkPi(cnLabApplyVo.getPkPi());
                        exLabOcc.setPkPv(cnLabApplyVo.getPkPv());
                        exLabOcc.setPkCnord(cnLabApplyVo.getPkCnord());
                        exLabOcc.setCodeApply(cnLabApplyVo.getCodeApply());
                        exLabOcc.setPkOrgOcc(cnLabApplyVo.getPkOrgOcc());
                        exLabOcc.setPkDeptOcc(cnLabApplyVo.getPkDeptOcc());
                        exLabOcc.setPkEmpOcc(user.getPkEmp());
                        exLabOcc.setNameEmpOcc(user.getNameEmp());
                        exLabOcc.setDateOcc(new Date());
                        exLabOcc.setDateRpt(null != labT && null != labT.getDateRpt() ? labT.getDateRpt() : new Date());
                        exLabOcc.setFlagChk("1");
                        exLabOcc.setDateChk(new Date());
                        exLabOcc.setNameEmpChk("????????????");
                        exLabOcc.setCodeRpt(null != labT && null != labT.getCodeRpt() ? labT.getCodeRpt().trim() : null);
                        ApplicationUtils.setDefaultValue(exLabOcc, true);
                        labOccs.add(exLabOcc);
                        break;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(sqlC)) {
                DataBaseHelper.batchUpdate(sqlC.toArray(new String[0]));
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExLabOcc.class), labOccs);
            }
        }
        if (ris) {
            List<String> sqlC = new ArrayList<>(16);
            List<ExRisOcc> risOccs = new ArrayList<>(16);
            for (CnRisApplyBaVo cnRisApplyBaVo : cnRisApplyVoNotDone) {
                for (LabAndRisTripartiteSystemVo labT : risTripartiteSystemVos) {
                    boolean isSameOne = cnRisApplyBaVo.getCodeApply().equals(labT.getCodeApply());
                    if (isSameOne) {
                        sqlC.add("update CN_RIS_APPLY set eu_status='" + labT.getEuStatu() + "' where PK_ORDRIS='" + cnRisApplyBaVo.getPkOrdris() + "'");
                        ExRisOcc exRisOcc = new ExRisOcc();
                        exRisOcc.setPkPi(cnRisApplyBaVo.getPkPi());
                        exRisOcc.setPkPv(cnRisApplyBaVo.getPkPv());
                        exRisOcc.setPkCnord(cnRisApplyBaVo.getPkCnord());
                        exRisOcc.setCodeApply(cnRisApplyBaVo.getCodeApply());
                        exRisOcc.setPkOrgOcc(cnRisApplyBaVo.getPkOrgOcc());
                        exRisOcc.setPkDeptOcc(cnRisApplyBaVo.getPkDeptOcc());
                        exRisOcc.setPkEmpOcc(user.getPkEmp());
                        exRisOcc.setNameEmpOcc(user.getNameEmp());
                        exRisOcc.setDateOcc(new Date());
                        exRisOcc.setDateRpt(new Date());
                        exRisOcc.setFlagChk("1");
                        exRisOcc.setDateChk(new Date());
                        exRisOcc.setNameEmpChk("????????????");
                        exRisOcc.setCodeRpt(labT.getCodeRpt());
                        exRisOcc.setResultSub(labT.getDescRpt());
                        ApplicationUtils.setDefaultValue(exRisOcc, true);
                        risOccs.add(exRisOcc);
                        break;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(sqlC)) {
                DataBaseHelper.batchUpdate(sqlC.toArray(new String[0]));
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExRisOcc.class), risOccs);
            }
        }
    }


    /**
     * ??????????????????????????????
     * 2020-11-17 ??????????????????
     *
     * @param param{pkPv}
     * @param user
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> deptChangeVerfyByZSBA(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if ((null == paramMap.get("pkPv") || CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
                && (null == paramMap.get("pkPvMa") || CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString()))) {
            throw new BusException("pkPv?????????????????????????????????????????????????????????");
        }
        if (null != paramMap.get("pkPvMa") && !CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString())) {
            List<String> pkPvs = getPkpvOfMaAndInfByPkpv(paramMap);
            paramMap.put("pkPvMa", pkPvs);
        }
        return getDeptOutVerfyDataBySyx(paramMap, user);
    }

    /**
     * ????????????????????? - ????????????????????????
     *
     * @param paramO
     * @return
     */
    public List<Map<String, Object>> getHisInPatientInfo(
            Map<String, Object> paramO) {
        List<Map<String, Object>> list = DataBaseHelper.queryForList("select * from a_patient_mi where inpatient_no =:codeIp ", paramO);
        return list;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param listFromHis
     * @param paramMap
     */
    public void updatePiCodePiFromHis(Map<String, Object> paramMap) {
        int cnt = DataBaseHelper.update("update pi_master set code_pi =:codePi where pk_pi =:pkPi and code_ip =:codeIp", paramMap);
        if (cnt != 1)
            throw new BusException("??????????????????" + CommonUtils.getString(paramMap.get("codeIp"), "")
                    + "??? ?????????????????????????????????" + CommonUtils.getString(paramMap.get("codePi"), "") + "????????????????????????????????????????????????");
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param paramMap
     */
    public void updatePiNamePiFromNhis(Map<String, Object> paramMap) {
        int cnt = DataBaseHelper.update("update a_patient_mi set name =:namePi where inpatient_no =:codeIp", paramMap);
        if (cnt != 1)
            throw new BusException("??????????????????" + CommonUtils.getString(paramMap.get("codeIp"), "")
                    + "??? ?????????????????????????????????" + CommonUtils.getString(paramMap.get("namePi"), "") + "????????????????????????????????????????????????");
    }

    /**
     * ????????????-???????????? ???????????????????????????????????????
     */
    public List<Map<String, Object>> getNeedSysStatusPatis(Map<String, Object> paramMap) {
        List<Map<String, Object>> list = zsAdtRegMapper.queryNeedSysStatusPatis(paramMap);
        return list;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> getPiListFromHis(Map<String, Object> paramMap) {
        List<Map<String, Object>> list = zsAdtRegMapper.getPiListFromHis(paramMap);
        return list;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> getPiListFromNhis(Map<String, Object> paramMap) {
        List<Map<String, Object>> list = zsAdtRegMapper.getPiListFromNhis(paramMap);
        return list;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param pi
     * @param piParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePiInfo(PiMasterBa pi, PiMaster pim, Map<String, Object> piParam) {
        //1????????? - ??????
        if (CommonUtils.isEmptyString(pi.getPkPi())) {
            if (!CommonUtils.isEmptyString(pi.getCodeOp())) {
                int cnt = DataBaseHelper.queryForScalar("select count(*) from pi_master where id_no = ? and code_op = ? ", Integer.class, new Object[]{pi.getIdNo(), pi.getCodeOp()});
                if (cnt > 0) {
                    return;
//					throw new BusException("???????????????"+pi.getCodeOp()+"??? ?????????????????????????????????");
                }
            }
            if (!CommonUtils.isEmptyString(CommonUtils.getString(piParam.get("wxOpenId"), ""))) {
                pi.setWechatNo(CommonUtils.getString(piParam.get("wxOpenId"), ""));
            }
        }
        savePiMasterParam(pi, pim);//??????????????????????????????????????????
    }

    /**
     * ????????????????????????????????? - ?????????????????????????????????????????????
     *
     * @param paramMap
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateHisInsertFlag(Map<String, Object> paramMap) {
        int cnt = DataBaseHelper.update("update mz_patient_mi set flag_to_his = '1' , name=:name , sex=:dtSex , birthday=:birthday where patient_id =:codeOp", paramMap);
        int cntb = DataBaseHelper.update("update mz_patient_mi_b set flag_to_his = '1', name=:name , sex=:dtSex , birthday=:birthday where patient_id =:codeOp", paramMap);
//		if(cnt != 1 && cntb != 1)
//			throw new BusException("??????id???"+CommonUtils.getString(paramMap.get("codeOp"), "") +"?????????????????????????????????");
    }

    /**
     * ????????????????????????????????? - ?????????????????????????????????????????????
     *
     * @param paramMap
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePiInfoToHis(PiMasterBa pi) {
        MzPatientMi mi = new MzPatientMi();
        mi.setSex("02" == pi.getDtSex() ? "1" : ("03" == pi.getDtSex() ? "2" : "9"));
        mi.setRelationCode(pi.getDtRalation());
        mi.setLvData(new Date());
        mi.setPatientId(pi.getCodeOp());
        if (pi.getNamePi().length() > 20)
            mi.setName(pi.getNamePi().substring(0, 20));
        else
            mi.setName(pi.getNamePi());
        mi.setBirthday(pi.getBirthDate());
        if ("01" == pi.getDtIdtype())
            mi.setSocialNo(pi.getIdNo());
        mi.setSelfCardType(pi.getDtIdtype());
        mi.setpBarCode(pi.getpCarCode());
        mi.setHomeTel(pi.getMobile());
        mi.setInpatientNo(pi.getCodeIp());
        mi.setIcCardId(pi.getIcCardId());
        mi.setHealthCardId(pi.getHealthCardId());
        mi.setEleHealthCarId(pi.getEleHearthCard());
        mi.setAdditionNo1(pi.getInsurNo());
        mi.setNewYbCard(pi.getThirdSocialNo());
        mi.setHomeDistrict(pi.getAddrcodeCur());
        mi.setHomeStreet(pi.getAddrCurDt());
        mi.setBirthPlace(pi.getAddrBirth());
        mi.setRelationTel(pi.getTelRel());
        mi.setRelationName(pi.getNameRel());
        mi.setRelationCardNo(pi.getIdnoRel());
        //mi.setCountryCode(pi.getDtCountry());
        mi.setNationCode(pi.getDtNation());
        mi.setWxOpenId(pi.getWechatNo());
        mi.setFlagToHis("1");
        DataBaseHelper.insertBean(mi);
    }


    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param paramMap
     * @return
     */
    public Map<String, Object> getHisCodePiAndIp(Map<String, Object> paramMap) {
        Map<String, Object> rs = new HashMap<String, Object>();
        List<Map<String, Object>> list = DataBaseHelper.queryForList("select mz_patient_id, max(zy_patient_id) patient_id, max(mi.inpatient_no) inpatient_no"
                + "  from mz_to_zy zy "
                + " inner join a_patient_mi mi on mi.patient_id = zy.zy_patient_id "
                + " where zy.confirm_flag = '1' "
                + "   and zy.mz_patient_id =:codeOp "
                + " group by zy.mz_patient_id ", paramMap);
        if (null != list && list.size() > 0)
            rs = list.get(0);
        return rs;
    }

    /**
     * ????????????????????????
     *
     * @param mParam
     * @param isAnonymous
     * @return
     */
    public PiMasterBa savePiMasterParam(PiMasterBa master, PiMaster pim, String... isAnonymous) {
        // ????????????????????????
        try {
            if (master == null) {
                throw new BusException("????????????,?????????????????????");
            } else {
                //???????????????????????????????????????
                String extHealth = ApplicationUtils.getSysparam("PI0019", false);
                if ("1".equals(extHealth)) {
                    //?????????????????????????????????????????????
                    if (CommonUtils.isEmptyString(master.getHealthCardId())) {
                        Map<String, Object> ehealthMap = new HashMap<>(16);
                        ehealthMap.put("piMaster", pim);
                        //?????????????????????
                        Map<String, String> hicNo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC01", new Object[]{ehealthMap});
                        if (hicNo != null) {
                            master.setHicNo(hicNo.get("hicNo"));
                            master.setEleHearthCard(hicNo.get("eleHealthCarId"));
                            master.setHealthCardId(MapUtils.getString(hicNo, "hicNo", ""));
                            master.setIcCardId(MapUtils.getString(hicNo, "icCardId", ""));
                            master.setNote(master.getNote() + hicNo.get("note"));
                        }
                    }
                }
                if (StringUtils.isEmpty(master.getPkPicate())) {
                    List<PiCate> picates = DataBaseHelper.queryForList("select * from pi_cate "
                            + "where flag_def='1' and del_flag='0'", PiCate.class);
                    master.setPkPicate(picates.get(0).getPkPicate());
                }
            }
            int count_code = 0;//?????????????????????
            int count_op = 0;//?????????
            int count_ip = 0;//?????????
            int count_card = 0;//????????????
            //???????????????????????????
            if (CommonUtils.isNotNull(master.getIdNo())) {
                master.setIdNo(master.getIdNo().toUpperCase());
            }
            // ????????????????????????????????????????????????????????????
            if (StringUtils.isEmpty(master.getPkPi())) // ????????????
            {
                //1?????????????????????
                count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and code_pi = ?", Integer.class, master.getCodePi());
                if (count_code != 0) {
                    throw new BusException("??????????????????"
                    		+ "???\n?????????????????? ????????????"+master.getCodeIp()+"??????????????????"
                    		+ "???\n?????????????????????"+master.getCodeIp()+"??????????????????????????????"
                    		+ "???\n???????????????????????????????????????????????????id???");
                }
                //????????????????????????????????????-ZSRM_??????[4779]_20201230_tjq
                if (StringUtils.isNotEmpty(master.getInsurNo())) {
                    int countInsurNo = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and insur_no = ?", Integer.class, master.getInsurNo());
                    if (countInsurNo != 0) {
                        throw new BusException("??????????????????,???????????????");
                    }
                }
                // ????????????????????????????????????
                if (isAnonymous.length == 0) // ??????????????????
                {
                    //2??????????????????
                    count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and code_op = ?", Integer.class, master.getCodeOp());
                    if (count_op != 0) {
                        throw new BusException("???????????????,???????????????");
                    }
                    //3??????????????????
//					if(!CommonUtils.isEmptyString(master.getCodeIp())){
//						count_ip = DataBaseHelper.queryForScalar( "select count(1) from pi_master "
//								+ "where del_flag = '0' and code_ip = ?", Integer.class, master.getCodeIp());
//						if (count_ip != 0) throw new BusException("???????????????,???????????????");
//					}
                    //4???????????????????????????????????? = ????????????
                    if ("01".equals(master.getDtIdtype())) {
                        count_card = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and dt_idtype = '01' and id_no = ?", Integer.class, master.getIdNo());
                    }
                    if (StringUtils.isNotBlank(master.getIdNo())) {
                        if (count_card != 0) {
                            throw new BusException("??????????????????,???????????????");
                        }
                    }
                    master.setPkPi(NHISUUID.getKeyId());
                    DataBaseHelper.insertBean(master);
                    savePiAccByNew(master);//????????????????????????PiAcc??????
                } else {// ?????????????????????
                    String name = master.getNamePi();
                    // ???????????????????????????
                    int count_name = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and name_pi = ?", Integer.class, name);
                    if (count_name == 0) {
                        DataBaseHelper.insertBean(master);
                    } else {
                        name = getAnonymous();
                        master.setNamePi(name);
                        DataBaseHelper.insertBean(master);
                    }
                    savePiAccByNew(master);//????????????????????????PiAcc??????
                }
            } else {
                //1???????????????????????????
                count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and code_pi = ? and pk_pi != ? ", Integer.class, master.getCodePi(), master.getPkPi());
                if (count_code != 0) {
                    throw new BusException("????????????????????????????????????");
                }
                //????????????????????????????????????-ZSRM_??????[4779]_20201230_tjq
                if (StringUtils.isNotEmpty(master.getInsurNo())) {
                    int countInsurNo = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and insur_no = ? and pk_pi != ? ", Integer.class, master.getInsurNo(), master.getPkPi());
                    if (countInsurNo != 0) {
                        throw new BusException("??????????????????,???????????????");
                    }
                }
                if (isAnonymous.length == 0) // ????????????????????????????????????
                {
                    // ??????????????????
                    //2??????????????????????????????
                    if (StringUtils.isNotBlank(master.getCodeOp())) {
                        count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and code_op = ? and pk_pi != ? ", Integer.class, master.getCodeOp(), master.getPkPi());
                    }
                    if (count_op != 0) {
                        throw new BusException("?????????????????????????????????");
                    }
                    //3??????????????????????????????
                    if (StringUtils.isNotBlank(master.getCodeIp())) {
                        count_ip = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and code_ip = ? and pk_pi != ? ", Integer.class, master.getCodeIp(), master.getPkPi());
                    }
                    if (count_ip != 0) {
                        throw new BusException("?????????????????????????????????");
                    }
                    //4??????????????????????????????
                    if (StringUtils.isNotBlank(master.getIdNo())) {
                        count_card = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and dt_idtype = '01' and id_no = ? and pk_pi != ? ", Integer.class, master.getIdNo(), master.getPkPi());
                    }
                    if ("01".equals(master.getDtIdtype()) && count_card != 0) {
                        throw new BusException("????????????????????????????????????");
                    }
                    //5?????????????????????
                    DataBaseHelper.updateBeanByPk(master, false);
                    //6??????????????????????????????(CODE_ACC = code_ip)
                    DataBaseHelper.update("update pi_acc set code_acc = ? where pk_pi = ?",
                            new Object[]{master.getCodeIp(), master.getPkPi()});
                    //7??????????????????????????????????????????
                    if (master.getBirthDate() != null) {
                        // ????????????????????????????????????????????????????????????????????????????????????????????????????????? 2017-06-14
                        try {
                            Map<String, Object> param = objectToMap(master);
                            String birthDate = DateUtils.getDateTimeStr(master.getBirthDate());
                            StringBuffer str = new StringBuffer();
                            str.append("update pv_encounter set pk_picate =:pkPicate, name_pi  =:namePi, "
                                    + "dt_sex =:dtSex, address =:address, dt_marry =:dtMarry, ");
                            if (Application.isSqlServer()) {
                                str.append("age_pv = dbo.GetPatAge (to_date(" + birthDate + ",'YYYYMMDDHH24MISS'),date_begin),");
                            } else {
                                str.append("age_pv = GetPatAge (to_date(" + birthDate + ",'YYYYMMDDHH24MISS'),date_begin),");
                            }
                            str.append("unit_work =:unitWork, tel_work =:telWork, postcode_work =:postcodeWork,"
                                    + "name_rel =:nameRel, tel_rel =:telRel, dt_ralation =:dtRalation, addr_rel =:addrRel,"
                                    + "addrcode_regi =:addrcodeRegi, addr_regi =:addrRegi, addr_regi_dt =:addrRegiDt, postcode_regi =:postcodeRegi,"
                                    + "addrcode_cur =:addrcodeCur, addr_cur =:addrCur, addr_cur_dt =:addrCurDt, postcode_cur =:postcodeCur"
                                    + " where 1=1 and pk_pi =:pkPi"
                                    + " and (eu_status = '0' or eu_status = '1')");
                            DataBaseHelper.update(str.toString(), param);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new BusException("??????????????????????????????????????????????????????????????????");
                        }
                    }
                } else {
                    String name = master.getNamePi();
                    // ???????????????????????????
                    int count_name = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and name_pi = ? and pk_pi != ?", Integer.class, name, master.getPkPi());
                    if (count_name == 0) {
                        DataBaseHelper.updateBeanByPk(master, false);
                    } else {
                        name = getAnonymous();
                        master.setNamePi(name);
                        DataBaseHelper.updateBeanByPk(master, false);
                    }
                }
            }
            String pkPi = master.getPkPi();
            master = DataBaseHelper.queryForBean("select * from pi_master where del_flag = '0' and pk_pi = ?", PiMasterBa.class, pkPi);
            //?????????????????????????????????????????????????????????
            if (!CommonUtils.isEmptyString(master.getWechatNo())) {
                int count_wx = DataBaseHelper.queryForScalar("select count(1) from t_patient_wx_mapper "
                        + "where del_flag = '0' and patient_id = ? and pk_pi = ? ", Integer.class, master.getCodeOp(), master.getPkPi());
                if (count_wx < 1) {
                    NHISPatientWxMapper wx = new NHISPatientWxMapper();
                    wx.setPatientId(master.getCodeOp());
                    wx.setPkPi(master.getPkPi());
                    wx.setCreator("???????????????????????????");
                    if (!CommonUtils.isEmptyString(master.getCodeIp()))
                        wx.setIpPatientId(master.getCodeIp());
                    wx.setPkOrg(master.getPkOrg());
                    wx.setRelationCode("00");
                    wx.setWxOpenId(master.getWechatNo());
                    wx.setPkPatientWxMapper(NHISUUID.getKeyId());
                    DataBaseHelper.insertBean(wx);
                }
            }
            //?????????????????????
            setSavePiCardListParam(master, pkPi);
        } catch (Exception e) {
            if (e instanceof BusException) {
                throw e;
            }
            e.printStackTrace();//?????????bus????????????
            throw new BusException("???????????????????????????" + e.getMessage());
        }
        return master;
    }

    /**
     * ???????????????????????????????????????
     */
    public void savePiAccByNew(PiMasterBa master) {
        PiAcc acc = new PiAcc();
        acc.setPkPi(master.getPkPi());
        acc.setCodeAcc(master.getCodeIp());
        acc.setAmtAcc(BigDecimal.ZERO);
        acc.setCreditAcc(BigDecimal.ZERO);
        acc.setEuStatus(PiConstant.ACC_EU_STATUS_1);
        DataBaseHelper.insertBean(acc);
    }

    /**
     * ???????????????
     *
     * @return
     */
    public String getAnonymous() {
        Integer maxNum = 0;
        if (Application.isSqlServer()) {
            maxNum = piPubMapper.getAnonymousMaxNumberSqlServer();
        } else {
            maxNum = piPubMapper.getAnonymousMaxNumberOracle();
        }
        String num = "";
        if (maxNum == null) {
            num = "001";
        } else {
            int number = maxNum + 1;
            num = String.valueOf(number);
            if (num.length() < 3) {
                while (num.length() < 3) {
                    num = "0" + num;
                }
            }
        }
        String name = "?????????" + num;
        return name;
    }

    /**
     * java ???????????? Map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    /**
     * ??????????????????
     */
    public List<PiCard> setSavePiCardListParam(PiMasterBa pi, String pkPi) {
        List<PiCard> oldList = new ArrayList<PiCard>();
        List<PiCard> list = new ArrayList<PiCard>();
        PiCard cardAdd = null;
        String pkEmp = UserContext.getUser().getPkEmp();
        //1????????????????????????[01]
        if (!CommonUtils.isEmptyString(pi.getpCarCode())) {
            oldList = DataBaseHelper.queryForList("select * from pi_card where pk_pi = ? and del_flag = '0' "
                    + "and dt_cardtype = '01' and card_no <> ? order by sort_no", PiCard.class, new Object[]{pkPi, pi.getpCarCode()});
            if (null != oldList || oldList.size() > 0) {
                DataBaseHelper.update("update pi_card set del_flag = '1', flag_active = '0' , date_end = getdate() , ts = getdate() , modifier = ?"
                        + " where pk_pi = ? and del_flag = '0' and dt_cardtype = '01' and card_no !=  ? ",  new Object[]{pkEmp, pkPi, pi.getpCarCode()});
            }
            oldList = DataBaseHelper.queryForList("select * from pi_card where pk_pi = ? and del_flag = '0' "
                    + "and dt_cardtype = '01' and card_no = ? order by sort_no", PiCard.class, new Object[]{pkPi, pi.getpCarCode()});
            if (null == oldList || oldList.size() < 1) {
            	cardAdd = new PiCard();
            	cardAdd.setPkPicard(NHISUUID.getKeyId());
            	cardAdd.setCardNo(pi.getpCarCode());
            	cardAdd.setPkPi(pi.getPkPi());
            	cardAdd.setDtCardtype("01");
            	cardAdd.setPkOrg(pi.getPkOrg());
            	cardAdd.setFlagActive("1");
            	cardAdd.setEuStatus("0");
            	DataBaseHelper.insertBean(cardAdd);
            }
            DataBaseHelper.execute("delete from pi_card where pk_pi = ? and del_flag = '1'"
            		+ "  and dt_cardtype = '01' and card_no = ? ", new Object[]{pkPi, pi.getpCarCode()});
        }
        //2????????????????????????
        if (!CommonUtils.isEmptyString(pi.getInsurNo())) {
            oldList = DataBaseHelper.queryForList("select * from pi_card where pk_pi = ? and del_flag = '0' "
                    + "and dt_cardtype = '03' and inner_no = '1' "
                    + "and card_no <> ? order by sort_no", PiCard.class, new Object[]{pkPi, pi.getInsurNo()});
            if (null != oldList || oldList.size() > 0) {
                DataBaseHelper.update("update pi_card set del_flag = '1', flag_active = '0' "
                		+ ", date_end = getdate() , ts = getdate() , modifier = ?"
                        + " where pk_pi = ? and del_flag = '0' "
                        + "and dt_cardtype = '03' and inner_no = '1' and card_no <> ? ", new Object[]{pkEmp, pkPi, pi.getInsurNo()});
            }
            oldList = DataBaseHelper.queryForList("select * from pi_card where pk_pi = ? and del_flag = '0' "
                    + "and dt_cardtype = '03' and inner_no = '1' and card_no = ? order by sort_no", PiCard.class, new Object[]{pkPi, pi.getInsurNo()});
            if (null == oldList || oldList.size() < 1) {
            	cardAdd = new PiCard();
                cardAdd.setPkPicard(NHISUUID.getKeyId());
                cardAdd.setCardNo(pi.getInsurNo());
                cardAdd.setPkPi(pi.getPkPi());
                cardAdd.setDtCardtype("03");
                cardAdd.setPkOrg(pi.getPkOrg());
                cardAdd.setFlagActive("1");
                cardAdd.setInnerNo("1");//?????????????????????
                cardAdd.setEuStatus("0");
                DataBaseHelper.insertBean(cardAdd);
            }
            DataBaseHelper.execute("delete from pi_card where pk_pi = ? and del_flag = '1'"
            		+ " and dt_cardtype = '03' and inner_no = '1' and card_no = ? ", new Object[]{pkPi, pi.getInsurNo()});
        }
        //3????????????????????????
        if (!CommonUtils.isEmptyString(pi.getThirdSocialNo())) {
            oldList = DataBaseHelper.queryForList("select * from pi_card where pk_pi = ? and del_flag = '0' "
                    + "and dt_cardtype = '03' and inner_no = '3' and card_no <> ? order by sort_no", PiCard.class, new Object[]{pkPi, pi.getThirdSocialNo()});
            if (null != oldList || oldList.size() > 0) {
                DataBaseHelper.update("update pi_card set del_flag = '1', flag_active = '0' "
                		+ ", date_end = getdate() , ts = getdate() , modifier = ?"
                        + " where pk_pi = ? and del_flag = '0' "
                        + " and dt_cardtype = '03' and inner_no = '3' and card_no <> ? ",  new Object[]{pkEmp, pkPi, pi.getThirdSocialNo()});
            }
            oldList = DataBaseHelper.queryForList("select * from pi_card where pk_pi = ? and del_flag = '0' "
                    + "and dt_cardtype = '03' and inner_no = '3' and card_no = ? order by sort_no", PiCard.class, new Object[]{pkPi, pi.getThirdSocialNo()});
            if (null == oldList || oldList.size() < 1) {
                cardAdd = new PiCard();
                cardAdd.setPkPicard(NHISUUID.getKeyId());
                cardAdd.setCardNo(pi.getThirdSocialNo());
                cardAdd.setPkPi(pi.getPkPi());
                cardAdd.setDtCardtype("03");
                cardAdd.setPkOrg(pi.getPkOrg());
                cardAdd.setFlagActive("1");
                cardAdd.setInnerNo("3");//?????????????????????
                cardAdd.setEuStatus("0");
                DataBaseHelper.insertBean(cardAdd);
            }
            DataBaseHelper.execute("delete from pi_card where pk_pi = ? and del_flag = '1'"
            		+ " and dt_cardtype = '03' and inner_no = '3' and card_no = ? ", new Object[]{pkPi, pi.getThirdSocialNo()});
        }
        //4????????????????????????
        if (!CommonUtils.isEmptyString(pi.getFyPno())) {
            oldList = DataBaseHelper.queryForList("select * from pi_card where pk_pi = ? and del_flag = '0' "
                    + "and dt_cardtype = '06' and card_no <> ? order by sort_no", PiCard.class, new Object[]{pkPi, pi.getFyPno()});
            if (null != oldList || oldList.size() > 0) {
                DataBaseHelper.update("update pi_card set del_flag = '1', flag_active = '0' "
                		+ ", date_end = getdate() , ts = getdate() , modifier = ?"
                        + " where pk_pi = ? and del_flag = '0' "
                        + " and dt_cardtype = '06' and card_no <> ? ",  new Object[]{pkEmp, pkPi, pi.getFyPno()});
            }
            oldList = DataBaseHelper.queryForList("select * from pi_card where pk_pi = ? and del_flag = '0' "
                    + "and dt_cardtype = '06' and card_no = ? order by sort_no", PiCard.class, new Object[]{pkPi, pi.getFyPno()});
            if (null == oldList || oldList.size() < 1) {
                cardAdd = new PiCard();
                cardAdd.setPkPicard(NHISUUID.getKeyId());
                cardAdd.setCardNo(pi.getFyPno());
                cardAdd.setPkPi(pi.getPkPi());
                cardAdd.setDtCardtype("06");
                cardAdd.setPkOrg(pi.getPkOrg());
                cardAdd.setFlagActive("1");
                cardAdd.setEuStatus("0");
                DataBaseHelper.insertBean(cardAdd);
            }
            DataBaseHelper.execute("delete from pi_card where pk_pi = ? and del_flag = '1'"
            		+ " and dt_cardtype = '06' and card_no = ? ", new Object[]{pkPi, pi.getFyPno()});
        }
        return list;
    }
    
    /**
     * ????????????????????????
     * @param piMaster
     * @return
     */
    public Map<String, String> geteHealthCadreLevel (PiMaster piMaster) {
    	Map<String, Object> ehealthMap = new HashMap<>(16);
		ehealthMap.put("piMaster", piMaster);
    	 //???????????????????????????
         Map<String, String> hicInfo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHCA1006", new Object[]{ehealthMap});
         return hicInfo;
    }
}
