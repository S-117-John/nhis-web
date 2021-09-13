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

    // 用来控制手动事物
    @Resource(name = "transactionManager")
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 从HIS中获取患者编码
     *
     * @return
     */
    public String getCodePiFromHis() {
        String codePi;
        codePi = DataBaseHelper.queryForScalar("select patient_sn from gh_config", String.class);
        if (CommonUtils.isEmptyString(codePi))
            throw new BusException("未获取到新患者编码！");
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
     * 从HIS中获取住院号
     *
     * @return
     */
    public String getCodeIpFromHis() {
        String codeIp;
        DataBaseHelper.update("update zy_configure set inpatient_no =inpatient_no + 1");
        codeIp = DataBaseHelper.queryForScalar("select inpatient_no from zy_configure", String.class);
        if (CommonUtils.isEmptyString(codeIp))
            throw new BusException("未获取到新住院号！");
        return codeIp;
    }

    /**
     * 从 HIS 中获取历史类似就诊记录
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
     * 从 NHIS 中获取历史类似就诊记录
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
     * 从 NHIS 中获取历史类似就诊记录
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
     * 从 旧his系统获取门诊待缴费清单
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
     * 从 Nhis系统获取门诊待缴费清单
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
     * 从 旧his系统获取科室列表
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
     * 从 Nhis系统获取已对照科室列表
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
     * 6.往 Nhis系统 保存对照科室记录
     *
     * @param param
     * @param user
     * @return
     */
    public void saveNhisDeptMapList(List<NhisDeptMap> listAdd, List<NhisDeptMap> listUp, List<NhisDeptMap> listDel) {
        int cntErr = 0;
        //1、批量删除
        if (null != listDel && listDel.size() > 0) {
            for (NhisDeptMap del : listDel) {
                cntErr = DataBaseHelper.execute("delete from nhis_dept_map where pk_nhis_dept_map = ? and to_char(ts,'yyyyMMddHH24Miss') = ? "
                        , del.getPkNhisDeptMap(), DateUtil.formatDate(del.getTs(), "yyyyMMddHHmmss"));
                if (cntErr != 1)
                    throw new BusException("科室[" + del.getHisWardCode()
                            + " - " + del.getHisWardName() + "]的对照记录，删除失败！");
            }
        }
        //2、批量插入
        if (null != listAdd && listAdd.size() > 0) {
            //批量插入【前提-主键不为空】
            //DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(NhisDeptMap.class), listUp);
            for (NhisDeptMap add : listAdd) {
                ApplicationUtils.setDefaultValue(add, true);
                cntErr = DataBaseHelper.insertBean(add);
                if (cntErr != 1)
                    throw new BusException("科室[" + add.getHisWardCode()
                            + " - " + add.getHisWardName() + "]的对照记录，插入失败！");
            }
        }
        //3、批量更新
        if (null != listUp && listUp.size() > 0) {
            //批量更新
            //DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(NhisDeptMap.class), listUp);
            for (NhisDeptMap up : listUp) {
                up.setModifier(UserContext.getUser().getPkEmp());
                up.setModityTime(new Date());
                cntErr = DataBaseHelper.updateBeanByPk(up, true);
                if (cntErr != 1)
                    throw new BusException("科室[" + up.getHisWardCode()
                            + " - " + up.getHisWardName() + "]的对照记录，更新失败！");
            }
        }
    }

    /**
     * 从 his 系统获取微信列表
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
     * 从 nhis 系统获取微信列表
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
     * 对比 his、nhis 系统同步微信列表
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
            //批量新增
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
            //从旧的中循环查询新的不存在的，予以新增
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
            //从新的中循环查询旧的不存在的，予以删除
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
     * 住院患者分页
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
            throw new BusException("查询参数有问题！");
        }
        int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
        int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
        // 分页操作
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
     * 从 his系统获取患者列表
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
     * 根据医嘱主键计费明细列表
     *
     * @param param{pkOrd}
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryBlCgIpDetailsByOrd(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //查询医嘱相关费用
        List<Map<String, Object>> ords = zsAdtRegMapper.queryBlCgIpDetailsByOrd(map);
        if (null != ords && ords.size() > 0)
            list.addAll(ords);
        //查询试管费用
        List<Map<String, Object>> samps = zsAdtRegMapper.querySampFeeByOrd(map);
        if (null != samps && samps.size() > 0)
            list.addAll(samps);
        return list;
    }

    /**
     * 查询患者费用核查信息
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
     * 查询患者费用核查明细信息
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
     * 出院患者校验数据查询
     * 2020-08-04 博爱项目：出院独立版本
     *
     * @param param{pkPv}
     * @param user
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> deptChangeVerfyByBoAi(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if ((null == paramMap.get("pkPv") || CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
                && (null == paramMap.get("pkPvMa") || CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString()))) {
            throw new BusException("pkPv为空，请确认是否选择了需要转科的患者！");
        }
        if (null != paramMap.get("pkPvMa") && !CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString())) {
            List<String> pkPvs = getPkpvOfMaAndInfByPkpv(paramMap);
            paramMap.put("pkPvMa", pkPvs);
        }
        return getDeptOutVerfyDataBySyx(paramMap, user);
    }

    /**
     * 通过母亲主键，获取同病区婴儿就诊主键
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
     * 出院或转科需要校验的数据
     * 【2020-08-04】博爱出院独立版本
     * @param param{pkPv}
     * @return
     */
    public Map<String, Object> getDeptOutVerfyDataBySyx(Map<String, Object> param, IUser u) {
        User user = (User) u;
        param.put("pkDeptOcc", user.getPkDept());
        param.put("dbType", "sqlserver");
        if (null != param.get("dateEnd") && !CommonUtils.isEmptyString(param.get("dateEnd").toString()))
            param.put("dateEndDay", param.get("dateEnd").toString().substring(0, 8));
        List<Map<String, Object>> ordData = adtPubMapper.queryOrdByPkPv(param);//未停未作废医嘱
        List<Map<String, Object>> ordReaseData = adtPubMapper.queryOrdReaseByPkPv(param);//作废未核对医嘱
        if (ordReaseData.size() > 0) {
            ordData.addAll(ordReaseData);
        }
        List<Map<String, Object>> risData = adtPubMapper.queryRisByPkPvForBoAi(param);//未执行执行单以及计费未做执行单
        //根据医嘱附加属性BA001判断，如果BA001=3 申请单状态<3不允许出院；如果BA001=2  申请单状态<2不允许出院；其他 申请单状态<1不允许出院。
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
        List<Map<String, Object>> apData = adtPubMapper.queryPdapByPkPv(param);//未完全请领单
        List<Map<String, Object>> opData = adtPubMapper.queryOpByPkPv(param);//未完成手术申请单
        List<Map<String, Object>> packBedData = adtPubMapper.queryPacketBedByPv(param);//包床记录
        List<ExlistPubVo> exListData = adtPubMapper.queryExlistByPv(param);//未执行执行单
        List<Map<String, Object>> hpCgChkData = new ArrayList<>();
        List<Map<String, Object>> nsCgChkData = adtPubMapper.queryNsCgChkByPv(param);//不符合医保规范的护理费用
        if (param.get("isDeptChg") != null && !"1".equals(param.get("isDeptChg"))) {
            hpCgChkData = adtPubMapper.queryHpCgChkByPv(param);//出院日不可收取费用
        }
        if (nsCgChkData.size() > 0) {
            hpCgChkData.addAll(nsCgChkData);
        }
        List<Map<String, Object>> groupCgChkData = adtPubMapper.queryGroupByPvForSql(param);//校验同组的收费项目不能超收
        //同组排序
        new ExListSortByOrdUtil().ordGroup(exListData);
        List<Map<String, Object>> cpData = adtPubMapper.queryCpByPv(param);//未完成/退出在径记录;
        List<Map<String, Object>> infData = adtPubMapper.queryInfByPv(param);//未退诊的同病区婴儿信息 --2020-04-09 处理bug-25689 不判断病区条件
        List<Map<String, Object>> numDate = adtPubMapper.queryNumByPk(param);//查询数量大于50的收费项目（博爱项目---出院，仅做展示，不做拦截校验）
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
     * 校验是否为空
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
     * @Description 查询患者计费未做的检验项目
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
     * @Description 查询患者计费未做的检查项目
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
     * @Description 构造查询条件
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
     * @Description 根据第三方数据更新我们的状态
     * @auther wuqiang
     * @Date 2020-09-25
     * @Param [labTripartiteSystemVos三方检验数据, cnlabApplyVoNotDone Nhis检验数据,
     * risTripartiteSystemVos 三方检查数据, cnRisApplyVoNotDone Nhis检查数据]
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
                        exLabOcc.setNameEmpChk("查询写入");
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
                        exRisOcc.setNameEmpChk("查询写入");
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
     * 转科患者校验数据查询
     * 2020-11-17 博爱独立版本
     *
     * @param param{pkPv}
     * @param user
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> deptChangeVerfyByZSBA(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if ((null == paramMap.get("pkPv") || CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
                && (null == paramMap.get("pkPvMa") || CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString()))) {
            throw new BusException("pkPv为空，请确认是否选择了需要转科的患者！");
        }
        if (null != paramMap.get("pkPvMa") && !CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString())) {
            List<String> pkPvs = getPkpvOfMaAndInfByPkpv(paramMap);
            paramMap.put("pkPvMa", pkPvs);
        }
        return getDeptOutVerfyDataBySyx(paramMap, user);
    }

    /**
     * 获取旧住院系统 - 患者住院主索引表
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
     * 根据患者主键，更新当前患者的患者编码
     *
     * @param listFromHis
     * @param paramMap
     */
    public void updatePiCodePiFromHis(Map<String, Object> paramMap) {
        int cnt = DataBaseHelper.update("update pi_master set code_pi =:codePi where pk_pi =:pkPi and code_ip =:codeIp", paramMap);
        if (cnt != 1)
            throw new BusException("根据住院号【" + CommonUtils.getString(paramMap.get("codeIp"), "")
                    + "】 获取到旧系统患者编码【" + CommonUtils.getString(paramMap.get("codePi"), "") + "】时存在多条数据，更新失败！！！");
    }

    /**
     * 根据住院号，同步患者信息至旧住院系统
     *
     * @param paramMap
     */
    public void updatePiNamePiFromNhis(Map<String, Object> paramMap) {
        int cnt = DataBaseHelper.update("update a_patient_mi set name =:namePi where inpatient_no =:codeIp", paramMap);
        if (cnt != 1)
            throw new BusException("根据住院号【" + CommonUtils.getString(paramMap.get("codeIp"), "")
                    + "】 同步到旧系统患者姓名【" + CommonUtils.getString(paramMap.get("namePi"), "") + "】时存在多条数据，更新失败！！！");
    }

    /**
     * 查询住院-在院病人 ：待同步三方状态的患者列表
     */
    public List<Map<String, Object>> getNeedSysStatusPatis(Map<String, Object> paramMap) {
        List<Map<String, Object>> list = zsAdtRegMapper.queryNeedSysStatusPatis(paramMap);
        return list;
    }

    /**
     * 查询患者信息【从旧系统，获取门诊患者信息列表】
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> getPiListFromHis(Map<String, Object> paramMap) {
        List<Map<String, Object>> list = zsAdtRegMapper.getPiListFromHis(paramMap);
        return list;
    }

    /**
     * 查询患者信息【从新系统，获取门诊患者信息列表】
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> getPiListFromNhis(Map<String, Object> paramMap) {
        List<Map<String, Object>> list = zsAdtRegMapper.getPiListFromNhis(paramMap);
        return list;
    }

    /**
     * 保存患者基本信息【插入、更新】
     *
     * @param pi
     * @param piParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePiInfo(PiMasterBa pi, PiMaster pim, Map<String, Object> piParam) {
        //1、新增 - 保存
        if (CommonUtils.isEmptyString(pi.getPkPi())) {
            if (!CommonUtils.isEmptyString(pi.getCodeOp())) {
                int cnt = DataBaseHelper.queryForScalar("select count(*) from pi_master where id_no = ? and code_op = ? ", Integer.class, new Object[]{pi.getIdNo(), pi.getCodeOp()});
                if (cnt > 0) {
                    return;
//					throw new BusException("门诊号：【"+pi.getCodeOp()+"】 已存在，导入失败！！！");
                }
            }
            if (!CommonUtils.isEmptyString(CommonUtils.getString(piParam.get("wxOpenId"), ""))) {
                pi.setWechatNo(CommonUtils.getString(piParam.get("wxOpenId"), ""));
            }
        }
        savePiMasterParam(pi, pim);//调用原有逻辑保存患者基本信息
    }

    /**
     * 更新旧系统，患者主索引 - 更新导入标志【导入新住院系统】
     *
     * @param paramMap
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateHisInsertFlag(Map<String, Object> paramMap) {
        int cnt = DataBaseHelper.update("update mz_patient_mi set flag_to_his = '1' , name=:name , sex=:dtSex , birthday=:birthday where patient_id =:codeOp", paramMap);
        int cntb = DataBaseHelper.update("update mz_patient_mi_b set flag_to_his = '1', name=:name , sex=:dtSex , birthday=:birthday where patient_id =:codeOp", paramMap);
//		if(cnt != 1 && cntb != 1)
//			throw new BusException("门诊id【"+CommonUtils.getString(paramMap.get("codeOp"), "") +"】导入新系统失败！！！");
    }

    /**
     * 更新旧系统，患者主索引 - 更新导入标志【导入新住院系统】
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
     * 根据门诊号，获取最近的住院号、住院患者主索引
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
     * 保存患者基本信息
     *
     * @param mParam
     * @param isAnonymous
     * @return
     */
    public PiMasterBa savePiMasterParam(PiMasterBa master, PiMaster pim, String... isAnonymous) {
        // 关闭事务自动提交
        try {
            if (master == null) {
                throw new BusException("参数错误,无法保存患者！");
            } else {
                //健康卡是否需要读取外部接口
                String extHealth = ApplicationUtils.getSysparam("PI0019", false);
                if ("1".equals(extHealth)) {
                    //如果不存在健康码进行健康码注册
                    if (CommonUtils.isEmptyString(master.getHealthCardId())) {
                        Map<String, Object> ehealthMap = new HashMap<>(16);
                        ehealthMap.put("piMaster", pim);
                        //电子健康码注册
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
            int count_code = 0;//患者编码的数量
            int count_op = 0;//住院号
            int count_ip = 0;//门诊号
            int count_card = 0;//证件号码
            //证件号码字母转大写
            if (CommonUtils.isNotNull(master.getIdNo())) {
                master.setIdNo(master.getIdNo().toUpperCase());
            }
            // 患者编码、门诊号、住院号、身份证不能重复
            if (StringUtils.isEmpty(master.getPkPi())) // 新增保存
            {
                //1、校验患者编码
                count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and code_pi = ?", Integer.class, master.getCodePi());
                if (count_code != 0) {
                    throw new BusException("患者编码重复"
                    		+ "，\n该患者已存在 住院号【"+master.getCodeIp()+"】的索引信息"
                    		+ "，\n请根据住院号【"+master.getCodeIp()+"】查询后完善个人信息"
                    		+ "，\n引导患者使用该住院号对应【新】门诊id！");
                }
                //患者建档医保卡号唯一校验-ZSRM_任务[4779]_20201230_tjq
                if (StringUtils.isNotEmpty(master.getInsurNo())) {
                    int countInsurNo = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and insur_no = ?", Integer.class, master.getInsurNo());
                    if (countInsurNo != 0) {
                        throw new BusException("医保卡号重复,无法保存！");
                    }
                }
                // 判断是否为无名氏保存患者
                if (isAnonymous.length == 0) // 普通患者保存
                {
                    //2、校验门诊号
                    count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and code_op = ?", Integer.class, master.getCodeOp());
                    if (count_op != 0) {
                        throw new BusException("门诊号重复,无法保存！");
                    }
                    //3、校验住院号
//					if(!CommonUtils.isEmptyString(master.getCodeIp())){
//						count_ip = DataBaseHelper.queryForScalar( "select count(1) from pi_master "
//								+ "where del_flag = '0' and code_ip = ?", Integer.class, master.getCodeIp());
//						if (count_ip != 0) throw new BusException("住院号重复,无法保存！");
//					}
                    //4、校验证件号码，证件类型 = 身份证时
                    if ("01".equals(master.getDtIdtype())) {
                        count_card = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and dt_idtype = '01' and id_no = ?", Integer.class, master.getIdNo());
                    }
                    if (StringUtils.isNotBlank(master.getIdNo())) {
                        if (count_card != 0) {
                            throw new BusException("身份证号重复,无法保存！");
                        }
                    }
                    master.setPkPi(NHISUUID.getKeyId());
                    DataBaseHelper.insertBean(master);
                    savePiAccByNew(master);//新增时，插入一条PiAcc记录
                } else {// 无名氏患者保存
                    String name = master.getNamePi();
                    // 查询无名氏是否存在
                    int count_name = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and name_pi = ?", Integer.class, name);
                    if (count_name == 0) {
                        DataBaseHelper.insertBean(master);
                    } else {
                        name = getAnonymous();
                        master.setNamePi(name);
                        DataBaseHelper.insertBean(master);
                    }
                    savePiAccByNew(master);//新增时，插入一条PiAcc记录
                }
            } else {
                //1、校验编码是否重复
                count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and code_pi = ? and pk_pi != ? ", Integer.class, master.getCodePi(), master.getPkPi());
                if (count_code != 0) {
                    throw new BusException("患者编码重复，无法更新！");
                }
                //患者建档医保卡号唯一校验-ZSRM_任务[4779]_20201230_tjq
                if (StringUtils.isNotEmpty(master.getInsurNo())) {
                    int countInsurNo = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and insur_no = ? and pk_pi != ? ", Integer.class, master.getInsurNo(), master.getPkPi());
                    if (countInsurNo != 0) {
                        throw new BusException("医保卡号重复,无法更新！");
                    }
                }
                if (isAnonymous.length == 0) // 判断是否为无名氏保存患者
                {
                    // 普通患者保存
                    //2、检验门诊号是否重复
                    if (StringUtils.isNotBlank(master.getCodeOp())) {
                        count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and code_op = ? and pk_pi != ? ", Integer.class, master.getCodeOp(), master.getPkPi());
                    }
                    if (count_op != 0) {
                        throw new BusException("门诊号重复，无法更新！");
                    }
                    //3、校验住院号是否重复
                    if (StringUtils.isNotBlank(master.getCodeIp())) {
                        count_ip = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and code_ip = ? and pk_pi != ? ", Integer.class, master.getCodeIp(), master.getPkPi());
                    }
                    if (count_ip != 0) {
                        throw new BusException("住院号重复，无法更新！");
                    }
                    //4、校验身份证是否重复
                    if (StringUtils.isNotBlank(master.getIdNo())) {
                        count_card = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and dt_idtype = '01' and id_no = ? and pk_pi != ? ", Integer.class, master.getIdNo(), master.getPkPi());
                    }
                    if ("01".equals(master.getDtIdtype()) && count_card != 0) {
                        throw new BusException("身份证号重复，无法更新！");
                    }
                    //5、更新患者信息
                    DataBaseHelper.updateBeanByPk(master, false);
                    //6、更新账户表账户编码(CODE_ACC = code_ip)
                    DataBaseHelper.update("update pi_acc set code_acc = ? where pk_pi = ?",
                            new Object[]{master.getCodeIp(), master.getPkPi()});
                    //7、更新最近一次就诊记录的相关
                    if (master.getBirthDate() != null) {
                        // 修改患者就诊表冗余字段，包括年龄，但只改当前就诊记录，不改历史就诊记录 2017-06-14
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
                            throw new BusException("查询当前有效就诊记录失败，更新患者信息失败！");
                        }
                    }
                } else {
                    String name = master.getNamePi();
                    // 查询无名氏是否存在
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
            //微信主键不为空，则同步保存微信绑定信息
            if (!CommonUtils.isEmptyString(master.getWechatNo())) {
                int count_wx = DataBaseHelper.queryForScalar("select count(1) from t_patient_wx_mapper "
                        + "where del_flag = '0' and patient_id = ? and pk_pi = ? ", Integer.class, master.getCodeOp(), master.getPkPi());
                if (count_wx < 1) {
                    NHISPatientWxMapper wx = new NHISPatientWxMapper();
                    wx.setPatientId(master.getCodeOp());
                    wx.setPkPi(master.getPkPi());
                    wx.setCreator("人工办卡时同步插入");
                    if (!CommonUtils.isEmptyString(master.getCodeIp()))
                        wx.setIpPatientId(master.getCodeIp());
                    wx.setPkOrg(master.getPkOrg());
                    wx.setRelationCode("00");
                    wx.setWxOpenId(master.getWechatNo());
                    wx.setPkPatientWxMapper(NHISUUID.getKeyId());
                    DataBaseHelper.insertBean(wx);
                }
            }
            //插入患者卡信息
            setSavePiCardListParam(master, pkPi);
        } catch (Exception e) {
            if (e instanceof BusException) {
                throw e;
            }
            e.printStackTrace();//打印非bus异常堆栈
            throw new BusException("保存患者信息失败：" + e.getMessage());
        }
        return master;
    }

    /**
     * 新增的时候保存一条账户信息
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
     * 查询无名氏
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
        String name = "无名氏" + num;
        return name;
    }

    /**
     * java 实体类转 Map
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
     * 保存账户信息
     */
    public List<PiCard> setSavePiCardListParam(PiMasterBa pi, String pkPi) {
        List<PiCard> oldList = new ArrayList<PiCard>();
        List<PiCard> list = new ArrayList<PiCard>();
        PiCard cardAdd = null;
        String pkEmp = UserContext.getUser().getPkEmp();
        //1、设置院内诊疗卡[01]
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
        //2、设置一代医保卡
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
                cardAdd.setInnerNo("1");//用于标识一代卡
                cardAdd.setEuStatus("0");
                DataBaseHelper.insertBean(cardAdd);
            }
            DataBaseHelper.execute("delete from pi_card where pk_pi = ? and del_flag = '1'"
            		+ " and dt_cardtype = '03' and inner_no = '1' and card_no = ? ", new Object[]{pkPi, pi.getInsurNo()});
        }
        //3、设置三代医保卡
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
                cardAdd.setInnerNo("3");//用于标识三代卡
                cardAdd.setEuStatus("0");
                DataBaseHelper.insertBean(cardAdd);
            }
            DataBaseHelper.execute("delete from pi_card where pk_pi = ? and del_flag = '1'"
            		+ " and dt_cardtype = '03' and inner_no = '3' and card_no = ? ", new Object[]{pkPi, pi.getThirdSocialNo()});
        }
        //4、设置妇幼保健号
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
     * 查询患者干部级别
     * @param piMaster
     * @return
     */
    public Map<String, String> geteHealthCadreLevel (PiMaster piMaster) {
    	Map<String, Object> ehealthMap = new HashMap<>(16);
		ehealthMap.put("piMaster", piMaster);
    	 //查询持卡人档案信息
         Map<String, String> hicInfo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHCA1006", new Object[]{ehealthMap});
         return hicInfo;
    }
}
