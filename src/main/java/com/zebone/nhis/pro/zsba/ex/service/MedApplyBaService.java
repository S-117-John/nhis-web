package com.zebone.nhis.pro.zsba.ex.service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.base.bd.wf.BdOrdAutoexec;
import com.zebone.nhis.common.module.bl.CnOrderBar;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOccDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.pro.zsba.ex.dao.MedApplyBaMapper;
import com.zebone.nhis.pro.zsba.ex.vo.ExAssistOccPar;
import com.zebone.nhis.pro.zsba.ex.vo.MedAppBaVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedApplyBaService {

    @Resource
    public MedApplyBaMapper medApplyBaMapper;
    @Autowired
    private IpCgPubService ipCgPubService;

    @Resource
    private BlCgExPubService blCgExPubService;

    /**
     * ?????????????????????
     *
     * @param param{pkPvs,apptype,codeApply}
     * @param user
     * @return
     */
    public List<MedAppBaVo> queryMedApplist(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        List<String> pvlist = (List) map.get("pkPvs");
        if (pvlist == null || pvlist.size() <= 0)
            throw new BusException("?????????????????????????????????");
        String flagAll = CommonUtils.getString(map.get("flagAll"), "0");
        String pk_dept_cur = ((User) user).getPkDept();
        if ("0".equals(flagAll))
            map.put("pkDeptNs", pk_dept_cur);
        List<MedAppBaVo> list = null;
        //??????????????????????????????????????????????????????
        String flagFunDept = map.containsKey("flagFunDept") ? map.get("flagFunDept") != null ? map.get("flagFunDept").toString() : "0" : "0";
        // ??????????????????????????????????????????????????????????????????
        Map<String, Object> mapParam = DataBaseHelper.queryForMap("select dt_depttype from bd_ou_dept where pk_dept =?", UserContext.getUser().getPkDept());
        if (mapParam.get("dtDepttype").equals("0310") || "1".equals(flagFunDept)) {
            list = medApplyBaMapper.queryBaLabAppList(map);
        } else {
            list = medApplyBaMapper.queryBaAppList(map);
        }
        //2019-07-23 ??????????????????????????????????????????????????????
        if (null != list && list.size() > 0) {
            Map<String, Object> qryMap = new HashMap<String, Object>();
            qryMap.put("pkOrg", ((User) user).getPkOrg());
            List<BdOrdAutoexec> autoList = medApplyBaMapper.queryAutoExec(qryMap);//?????????????????????????????????????????????
            if (null != autoList && autoList.size() > 0) {
                for (MedAppBaVo app : list) {
                    for (BdOrdAutoexec auto : autoList) {
                        if (app.getPkDeptExec().equals(auto.getPkDept())) {
                            if ("0".equals(auto.getEuType()))/**0 ??????*/
                                app.setFlagAutoCg("1");
                            else if ("1".equals(auto.getEuType()) && app.getApptype().equals(auto.getCodeOrdtype()))/**1????????????*/
                                app.setFlagAutoCg("1");
                            else if ("2".equals(auto.getEuType()) && app.getPkOrd().equals(auto.getPkOrd()))/**2????????????*/
                                app.setFlagAutoCg("1");
                            break;
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * ?????????????????????
     *
     * @param param{pkOrds}
     * @param user
     */
    public void cancelSubmitApply(String param, IUser user) {
        List<String> pkOrds = JsonUtil.readValue(param, List.class);
        if (pkOrds != null && pkOrds.size() > 0) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("pkOrds", pkOrds);
            //??????????????????????????????
            String flag = medApplyBaMapper.queryBaAppExList(paramMap);
            if (flag != null && !flag.equals("0")) {
                throw new BusException("???????????????????????????????????????????????????????????????????????????");
            }
            DataBaseHelper.update("update cn_lab_apply set eu_status = '0' where pk_cnord in (:pkOrds) and eu_status = '1'", paramMap);
            //2020-03-31 ?????????????????? - ??????pacs????????????????????????flag_print2 = 1,????????? flag_print2 = 2[????????????????????????????????? pacs ?????????],pacs???????????????????????????????????????
            DataBaseHelper.update("update cn_ris_apply set eu_status = '0',flag_print2 = (case flag_print2 when '1' then '2' else flag_print2 end) where pk_cnord in (:pkOrds) and eu_status = '1' and flag_print2 < '2'", paramMap);
            DataBaseHelper.update("update cn_pa_apply set eu_status = '0' where pk_cnord in (:pkOrds) and eu_status = '1'", paramMap);
            DataBaseHelper.update("update cn_op_apply set eu_status = '0' where pk_cnord in (:pkOrds) and eu_status = '1'", paramMap);
            DataBaseHelper.update("update cn_trans_apply set eu_status = '0' where pk_cnord in (:pkOrds) and eu_status = '1'", paramMap);
        }
    }

    /**
     * @return void
     * @Description ??????????????????
     * @auther wuqiang
     * @Date 2020-08-10
     * @Param [param, user]
     */
    public void medApplyBachesCharge(String param, IUser user) {
        List<ExAssistOccPar> exAssistOccPars = JsonUtil.readValue(param, new TypeReference<List<ExAssistOccPar>>() {
        });
        if (CollectionUtils.isEmpty(exAssistOccPars)) {
            return;
        }
        List<String> pkExoccs = exAssistOccPars.stream().map(m -> m.getPkExocc()).distinct().collect(Collectors.toList());
        List<Map<String, Object>> list1 = getMedBlIpdtInfo(pkExoccs);
        boolean notEm = org.apache.commons.collections.CollectionUtils.isNotEmpty(list1);
        if (notEm) {
            list1.forEach(m -> {
                Integer cnt =  Double.valueOf(MapUtils.getString(m, "cnt")).intValue();
                if (cnt>0){
                    throw new BusException("????????????????????????????????????????????????????????????????????????????????????");
                }

            });
        }
        User u = (User) user;
        List<BlPubParamVo> blPubParamVos = medApplyBaMapper.getApplyBlItem(exAssistOccPars);
        // ?????????????????????
        List<ExAssistOccDt> exAssistOccDtList = new ArrayList<>(16);
        List<ExOrderOcc> orderOccs = new ArrayList<>(16);
        List<ExAssistOcc> exAssistOccList = new ArrayList<>(16);
        List<BlPubParamVo> blPubParamVoList = new ArrayList<>(16);
        List<String> labPkCnord = new ArrayList<>(5);
        List<String> risPkCnord = new ArrayList<>(5);
        List<String> transPkCnord = new ArrayList<>(5);
//        ??????????????????
        for (ExAssistOccPar exAssistOccPar : exAssistOccPars) {
            for (BlPubParamVo blPubParamVo : blPubParamVos) {
                if (!blPubParamVo.getPkCnord().equals(exAssistOccPar.getPkCnord())) {
                    continue;
                }
                BlPubParamVo blPubParamVo1 = new BlPubParamVo();
                BeanUtils.copyProperties(blPubParamVo, blPubParamVo1);
                blPubParamVo1.setPkOrgApp(blPubParamVo.getPkOrg());
                // ?????????????????????????????????????????????????????????????????????
                blPubParamVo1.setPkOrgApp(blPubParamVo.getPkOrg());
                blPubParamVo1.setPkDeptEx(blPubParamVo.getPkDeptEx());
                blPubParamVo1.setPkDeptNsApp(exAssistOccPar.getPkDeptNs());
                blPubParamVo1.setEuBltype("2");
                blPubParamVo1.setEuPvType("3");
                blPubParamVo1.setPkDeptCg(UserContext.getUser().getPkDept());
                // ????????????????????????????????????
                blPubParamVo1.setPkOrdexdt(exAssistOccPar.getPkExocc());
                // ???????????????????????????
                blPubParamVo1.setOrdsnParent(exAssistOccPar.getOrdsnParent());
                blPubParamVo1.setOrdsn(exAssistOccPar.getOrdsn());
                // ?????????????????????????????????
                blPubParamVo1.setDateStart(DateUtils.strToDate(exAssistOccPar.getDateStart().toString()));
                blPubParamVo1.setCodeOrdtype(exAssistOccPar.getCodeOrdtype());
                if (exAssistOccPar.getDatePlan() == null) {
                    blPubParamVo1.setDateHap(new Date());
                } else {
                    blPubParamVo1.setDateHap(exAssistOccPar.getDatePlan());
                }
                // ????????????????????????????????????pk_pd??????pkOrd???
                if (BlcgUtil.converToTrueOrFalse(blPubParamVo.getFlagPd())) {
                    blPubParamVo1.setPkOrd(blPubParamVo.getPkItem());
                    blPubParamVo1.setPkItem(null);
                }
                blPubParamVoList.add(blPubParamVo1);
            }
            ExAssistOcc exAssistocc = new ExAssistOcc();
            exAssistocc.setPkOrg(u.getPkOrg());
            exAssistocc.setPkCnord(exAssistOccPar.getPkCnord());
            exAssistocc.setPkPv(exAssistOccPar.getPkPv());
            exAssistocc.setPkPi(exAssistOccPar.getPkPi());
            exAssistocc.setEuPvtype("3");
            // ????????????
            exAssistocc.setCodeOcc(ApplicationUtils.getCode("0503"));
            exAssistocc.setPkEmpOrd(u.getPkEmp());// ??????????????????
            exAssistocc.setNameEmpOrd(u.getNameEmp());// ??????????????????
            exAssistocc.setPkDept(u.getPkDept());
            exAssistocc.setDateOrd(new Date());
            exAssistocc.setQuanOcc(Double.valueOf(exAssistOccPar.getQuan()));
            exAssistocc.setTimesOcc(1);
            exAssistocc.setTimesTotal(1);
            exAssistocc.setPkOrgOcc(exAssistOccPar.getPkOrgOcc());
            exAssistocc.setPkDeptOcc(exAssistOccPar.getPkDeptOcc());
            exAssistocc.setFlagOcc("1");
            exAssistocc.setFlagCanc("0");
            exAssistocc.setPkExocc(exAssistOccPar.getPkExocc());
            exAssistocc.setInfantNo("0");
            exAssistocc.setEuStatus("1");
            exAssistocc.setFlagPrt("0");
            exAssistocc.setFlagRefund("0");
            exAssistocc.setPkAssocc(NHISUUID.getKeyId());
            exAssistocc.setDateOcc(new Date());
            exAssistocc.setPkEmpOcc(u.getPkEmp());
            exAssistocc.setNameEmpOcc(u.getNameEmp());
            exAssistOccList.add(exAssistocc);
            //??????????????????????????????
            ExAssistOccDt exAssistOccDt = new ExAssistOccDt();
            if (exAssistOccPar.getOrdsn() == exAssistOccPar.getOrdsnParent()) {
                exAssistOccDt.setFlagMaj("1");
                // ???????????????
            } else {
                exAssistOccDt.setFlagMaj("0");
            }
            exAssistOccDt.setPkAssoccdt(NHISUUID.getKeyId());
            exAssistOccDt.setPkAssocc(exAssistocc.getPkAssocc());
            exAssistOccDt.setPkCnord(exAssistOccPar.getPkCnord());
            exAssistOccDt.setPkExocc(exAssistOccPar.getPkExocc());
            exAssistOccDt.setPkOrd(exAssistOccPar.getPkOrd());
            exAssistOccDtList.add(exAssistOccDt);
            ExOrderOcc ex = new ExOrderOcc();
            ex.setPkExocc(exAssistOccPar.getPkExocc());
            ex.setDateOcc(new Date());
            ex.setPkEmpOcc(u.getPkEmp());
            ex.setNameEmpOcc(u.getNameEmp());
            ex.setPkOrgOcc(u.getPkOrg());
            ex.setPkDeptOcc(u.getPkDept());
            ex.setTs(exAssistOccPar.getTs());
            ex.setModityTime(new Date());
            orderOccs.add(ex);
            if (StringUtils.isNoneBlank(exAssistOccPar.getCodeOrdtype())) {
                String codeOrdtype = exAssistOccPar.getCodeOrdtype().substring(0, 2);
                switch (codeOrdtype) {
                    case "02":
                        risPkCnord.add(exAssistOccPar.getPkCnord());
                        break;
                    case "03":
                        labPkCnord.add(exAssistOccPar.getPkCnord());
                        break;
                    case "12":
                        transPkCnord.add(exAssistOccPar.getPkCnord());
                        break;
                    default:
                        break;
                }
            }
        }
        // ???????????????????????????????????????
        if (!CollectionUtils.isEmpty(blPubParamVoList) && blPubParamVoList.size() > 0) {
            BlPubReturnVo chargeIpBatch = ipCgPubService.chargeIpBatch(blPubParamVoList, false);
        }
        if (orderOccs.size() > 0) {
            List<String> sqls = new ArrayList<>(orderOccs.size());
            orderOccs.forEach(m -> {
                StringBuilder sql = new StringBuilder("update EX_ORDER_OCC set  DATE_OCC=getdate()");
                String dateTs = DateUtils.getDateTimeStr(m.getTs());
                sql.append(" , PK_DEPT_OCC='" + m.getPkDeptOcc() + "'");
                sql.append(" , PK_ORG_OCC='" + m.getPkOrgOcc() + "'");
                sql.append(" , PK_EMP_OCC='" + m.getPkEmpOcc() + "'");
                sql.append(" , eu_status='1' ");
                sql.append(" , NAME_EMP_OCC='" + m.getNameEmpOcc() + "'");
                sql.append(" ,MODITY_TIME=getdate()");
                sql.append(" ,TS=to_date('" + m.getModityTime() + "','YYYYMMDDHH24MISS')");
                sql.append("  where  to_char(ts,'YYYYMMDDHH24MISS')='" + dateTs + "'");
                sql.append(" and PK_EXOCC ='" + m.getPkExocc() + "'");
                sqls.add(sql.toString());
            });
            int[] cout = DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
            int num = Arrays.stream(cout).sum();
            if (num != orderOccs.size()) {
                throw new BusException("????????????????????????????????????????????????????????????????????????????????????");
            }
        }
        if (exAssistOccList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExAssistOcc.class), exAssistOccList);
        }
        if (exAssistOccDtList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExAssistOccDt.class), exAssistOccDtList);
        }
        Set<String> pkCnordSet = null;
        if (risPkCnord.size() > 0) {
            pkCnordSet = new HashSet<String>(risPkCnord);
            DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") and eu_status<'3'");
        }
        if (labPkCnord.size() > 0) {
            pkCnordSet = new HashSet<String>(labPkCnord);
            DataBaseHelper.execute("update cn_lab_apply set eu_status='3' where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") and eu_status<'3'");
        }
        if (transPkCnord.size() > 0) {
            pkCnordSet = new HashSet<String>(transPkCnord);
            DataBaseHelper.execute("update cn_trans_apply set eu_status='3' where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") and eu_status<'3'");
        }
    }

    /**
     * @return void
     * @Description ??????????????????????????????????????????
     * @auther wuqiang
     * @Date 2020-08-27
     * @Param [param, user]
     */
    public void medApplyBachesReturn(String param, IUser user) {
        List<ExAssistOccPar> exAssistOccPars = JsonUtil.readValue(param, new TypeReference<List<ExAssistOccPar>>() {
        });
        List<ExlistPubVo> exList = new ArrayList<>(16);
        User u = (User) user;
        for (ExAssistOccPar exAssistOccPar : exAssistOccPars) {
            ExlistPubVo exlistPubVo = new ExlistPubVo();
            exlistPubVo.setEuStatus(exAssistOccPar.getEuStatusOcc());
            exlistPubVo.setFlagDurg("0");
            exlistPubVo.setFlagSelf("0");
            exlistPubVo.setFlagBase("0");
            exlistPubVo.setPkEmpCanc(u.getPkEmp());
            exlistPubVo.setNameEmpCanc(u.getNameEmp());
            exlistPubVo.setPkExocc(exAssistOccPar.getPkExocc());
            exlistPubVo.setFlagBl(exAssistOccPar.getEuStatusOcc());
            if (exAssistOccPar.getPriceCg() > 0) {
                
            } else {
                //exlistPubVo.setFlagBl("0");
            }
            exlistPubVo.setPkCnord(exAssistOccPar.getPkCnord());
            exlistPubVo.setPkOrd(exAssistOccPar.getPkOrd());
            exlistPubVo.setNameOrd(exAssistOccPar.getNameOrd());
            exList.add(exlistPubVo);
        }
        blCgExPubService.cancelExAndRtnCg(exList, (User) user);
    }

    /**
     * ??????????????? ????????????
     *
     * @param param
     * @param user
     */
    public void confrimMedApp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String sampNo = CommonUtils.getString(paramMap.get("sampNo"), "");
        String dateNow = DateUtils.getDateTimeStr(new Date());
        //3??????????????????
        DataBaseHelper.update("update cn_ris_apply set eu_status='3', modifier ='???????????????????????????' ,ts = to_date('" + dateNow + "','yyyyMMddHH24MISS') "
                + " where pk_cnord =: pkCnord and eu_status > '0'", paramMap);//?????????????????????
        DataBaseHelper.update("update cn_lab_apply set eu_status='3', modifier ='???????????????????????????' ,ts = to_date('" + dateNow + "','yyyyMMddHH24MISS') "
                + (!CommonUtils.isEmptyString(sampNo) ? ",samp_no =: sampNo " : "")
                + "where pk_cnord =: pkCnord and eu_status > '0'", paramMap);//?????????????????????
    }

    /**
     * @return java.lang.String
     * @Description ???????????????????????????????????????????????????
     * @auther wuqiang
     * @Date 2020-09-22
     * @Param [param, user]
     */
    public int getCnordExeCount(String param, IUser user) {
        String pkCnord = JsonUtil.getFieldValue(param, "pkCnord");
        ;
        return DataBaseHelper.queryForScalar("select count(*) " +
                "from EX_ORDER_OCC EOO where PK_CNORD= ?  and EOO.EU_STATUS='1'", Integer.class, pkCnord);
    }

    /**
     * ????????????????????????-??????????????????-??????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queryProBaOrderDept(String param, IUser user) {
        return medApplyBaMapper.queryProBaOrderDept(JsonUtil.readValue(param, Map.class));
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCnOrdBar(CnOrderBar cnOrderBar) {
        DataBaseHelper.insertBean(cnOrderBar);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCnOrderBar(String pkCnord, String barcode) {
        DataBaseHelper.update("update cn_order_bar set del_flag = '1' where pk_cnord = ? and barcode = ?", new Object[]{pkCnord, barcode});
    }

    /**
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Description sqlServer??????????????????????????????2100???
     * @auther wuqiang
     * @Date 2021-04-26
     * @Param [pkExoccs]
     */
    private List<Map<String, Object>> getMedBlIpdtInfo(List<String> pkExoccs) {
        List<Map<String, Object>> list1 = new ArrayList<>(pkExoccs.size() * 2);
        String sql = "select nvl(sum(dt.AMOUNT_PI) ,0) cnt " +
                "        from bl_ip_dt dt " +
                "        where  " +
                "            dt.pk_ordexdt in  (" + CommonUtils.convertSetToSqlInPart(new HashSet<>(pkExoccs), "pk_ordexdt") + ")" +
                "        group by dt.pk_ordexdt ";
        list1 = DataBaseHelper.queryForList(sql);
        return list1;
    }
}