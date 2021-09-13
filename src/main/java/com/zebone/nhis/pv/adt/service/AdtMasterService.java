package com.zebone.nhis.pv.adt.service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.*;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pi.pub.vo.PiMasterAndAddr;
import com.zebone.nhis.pv.adt.dao.AdtMasterMapper;
import com.zebone.nhis.pv.adt.vo.PvDaycgAndDetailParam;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * ADT 住院患者管理服务
 *
 * @author wangpeng
 * @date 2016年12月08日
 */
@Service
public class AdtMasterService {

    @Resource
    private AdtMasterMapper adtMasterMapper;

    @Resource
    private IpCgPubService ipCgPubService;
    
    
    /**  
    * <p>Desc:住院患者查询重复记录,两条及以上返回true,否则false</p>  
    * 交易号: 003004002020
    * @param  : 
    * @author : wangpengyong  
    * @date   : 2021年6月21日  
    */  
    public List<PiMasterAndAddr> getRepeatPatient(String param, IUser user) {
    	List<PiMasterAndAddr> patientList = new ArrayList<PiMasterAndAddr>();
        Map<String, String> params = JsonUtil.readValue(param, Map.class);
        if(null != params && params.size() >= 3) {//至少三个有效参数
        	patientList = adtMasterMapper.getRepeatPatientList(params);
        }
        return patientList;
    }

    /**
     * 交易号：003004002003<> 保存患者就诊信息<br>
     *
     * @param param
     * @param user
     * @return
     * @throws
     * @author wangpeng
     * @date 2016年12月09日
     */
    public PvEncounter updateAdtPvEncounter(String param, IUser user) {
        User u = UserContext.getUser();
        PiMaster master = JsonUtil.readValue(param, PiMaster.class);
        PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
        PvIp ip = JsonUtil.readValue(param, PvIp.class);
        // 查询患者原主医保pk
        Map<String, Object> oldPv = DataBaseHelper.queryForMap(
                "select pk_insu,pk_picate from pv_encounter where pk_pv = ?",
                encounter.getPkPv());
        if (master.getBirthDate() != null) {
            encounter
                    .setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(), encounter.getDateBegin())); // 年龄
        }
        // master.setPkPicate(encounter.getPkPicate());//患者分类
        master.setAddress(null); // 患者信息表地址不变，只更新就诊表的
        // 修改就诊表、患者信息表、就诊住院属性表
        if ("0".equals(encounter.getEuStatus())) {
            encounter.setPkDept(ip.getPkDeptAdmit());
            encounter.setPkDeptNs(ip.getPkDeptNsAdmit());
        }
        // 查询患者分类特诊标志
        String flagSpec = "";
        String stSql = "select flag_spec from pi_cate where PK_PICATE = ?";
        Map<String, Object> flagSpecMap = DataBaseHelper.queryForMap(stSql,
                encounter.getPkPicate());
        if (flagSpecMap != null && flagSpecMap.get("flagSpec") != null) {
            flagSpec = flagSpecMap.get("flagSpec").toString();
        }
        String euStatus = "";
        String sql = "select EU_STATUS from pv_encounter where pk_pv = ?";
        Map<String, Object> euStatusMap = DataBaseHelper.queryForMap(sql,
                encounter.getPkPv());
        if (euStatusMap != null && euStatusMap.get("euStatus") != null) {
            euStatus = euStatusMap.get("euStatus").toString();
            encounter.setEuStatus(euStatus);
        }
        encounter.setFlagSpec(flagSpec);
        DataBaseHelper.updateBeanByPk(master, false);
        DataBaseHelper.updateBeanByPk(encounter, false);
        //入院登记时是否同步修改dt_level_dise字段，0修改，1不修改
        String dtLevelDise = ApplicationUtils.getSysparam("PV0041", false);
        if ("1".equals(dtLevelDise)) {
            ip.setDtLevelDise(null);
        } else {
            ip.setDtLevelDise(ip.getDtLevelDiseInit());
        }
        DataBaseHelper.updateBeanByPk(ip, false);
        Date ts = new Date();
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("pkHp", encounter.getPkInsu());
        mapParam.put("pkPv", encounter.getPkPv());
        mapParam.put("pkOrg", u.getPkOrg());
        mapParam.put("pkEmp", u.getPkEmp());
        mapParam.put("ts", ts);
        // 主医保
        DataBaseHelper
                .update("update pv_insurance set pk_hp =:pkHp, modifier =:pkEmp, ts =:ts where pk_pv =:pkPv and del_flag = '0' and flag_maj = '1'",
                        mapParam);
        // 辅助医保
        String[] pkHps = encounter.getPkHps();
        if (pkHps != null) {
            DataBaseHelper
                    .execute(
                            "delete from pv_insurance where pk_pv = ? and flag_maj = '0'",
                            new Object[]{encounter.getPkPv(),});
            int length = encounter.getPkHps().length;
            if (length > 0) {
                List<PvInsurance> insuList = new ArrayList<PvInsurance>();
                for (int i = 0; i < length; i++) {
                    PvInsurance insu = new PvInsurance();
                    insu.setPkOrg(u.getPkOrg());
                    insu.setPkPv(encounter.getPkPv());
                    insu.setFlagMaj("0");
                    insu.setCreator(u.getPkEmp());
                    insu.setCreateTime(ts);
                    insu.setModifier(u.getPkEmp());
                    insu.setDelFlag("0");
                    insu.setTs(ts);
                    insu.setPkPvhp(NHISUUID.getKeyId());
                    insu.setSortNo(i + 1);
                    insu.setPkHp(pkHps[i]);
                    insuList.add(insu);
                }
                DataBaseHelper.batchUpdate(
                        DataBaseHelper.getInsertSql(PvInsurance.class),
                        insuList);
            }
        }
        // TODO 医保变化费用重算
        // TODO 患者分类费用重算
        List<BlIpDt> dtList = adtMasterMapper.getBlIpDtListByPkPv(encounter
                .getPkPv());
        // ipCgPubService.RechargeToInus(dtList, encounter);
        ipCgPubService.updateBlIpDtCgRate(encounter.getPkPv(), encounter
                .getPkInsu(), encounter.getPkPicate(), oldPv.get("pkInsu")
                .toString(), oldPv.get("pkPicate").toString());
        // 2019-01-06 更新患者主诊断信息
        Map<String, Object> diagMap = JsonUtil.readValue(param, Map.class);
        boolean isNull = MapUtils.getBooleanValue(diagMap, "pkDiag") && MapUtils.getBooleanValue(diagMap, "codeDiag")
                && MapUtils.getBooleanValue(diagMap, "codeIcd");
        if (null != diagMap && isNull && ("0".equals(encounter.getEuStatus()) || "1".equals(encounter.getEuStatus()) || "2".equals(encounter.getEuStatus()))) {
            diagMap.put("pkEmp", u.getPkEmp());
            diagMap.put("ts", ts);
            DataBaseHelper
                    .update("update pv_diag set pk_diag =:pkDiag,code_icd=:codeIcd,name_diag=:nameDiag,desc_diag=:descDiag, "
                                    + " pk_emp_diag=:pkEmpTre,name_emp_diag=:nameEmpTre, modifier =:pkEmp, ts =:ts , date_diag=:ts"
                                    + " where pk_pv =:pkPv and del_flag = '0' and flag_maj = '1' ",
                            diagMap);
            DataBaseHelper
                    .update("update pv_ip set code_diag=:codeIcd,name_diag=:nameDiag,modifier=:pkEmp,ts=:ts "
                            + "where pk_pvip=:pkPvip and pk_pv=:pkPv and del_flag='0'", diagMap);
        }
        // 发送修改住院就诊信息至平台
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkPv", encounter.getPkPv());
        paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
        paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("STATUS", "_UPDATE");
        PlatFormSendUtils.sendPvInfoMsg(paramMap);
        paramMap = null;
        return encounter;
    }

    /**
     * 交易号：003004002010<> 修改患者固定费用<br>
     *
     * @param param
     * @param user
     * @return
     * @throws
     * @author wangpeng
     * @date 2016年12月09日
     */
    public void updatePvDaycgAndDetail(String param, IUser user) {
        User u = UserContext.getUser();
        PvDaycgAndDetailParam cgParam = JsonUtil.readValue(param,
                PvDaycgAndDetailParam.class);
        PvDaycg cg = cgParam.getPvDaycg();
        List<PvDaycgDetail> daycgDetailList = cgParam.getDaycgDetailList();
        String pkDaycg = cg.getPkDaycg();
        // 更新就诊患者固定费用
        DataBaseHelper.updateBeanByPk(cg, false);
        // 更新就诊患者固定费用明细：daycgDetailList为null时不修改；daycgDetailList.size=0时全删
        if (daycgDetailList != null) {
            DataBaseHelper.execute(
                    "delete from pv_daycg_detail where pk_daycg = ?",
                    new Object[]{pkDaycg});
            if (daycgDetailList.size() > 0) {
                for (PvDaycgDetail d : daycgDetailList) {
                    if (StringUtils.isEmpty(d.getPkDaycgdt())) {
                        d.setPkDaycgdt(NHISUUID.getKeyId());
                    }
                    d.setPkOrg(u.getPkOrg());
                    d.setPkDaycg(pkDaycg);
                    d.setCreator(u.getPkEmp());
                    d.setCreateTime(new Date());
                    d.setModifier(u.getPkEmp());
                    d.setTs(new Date());
                }
                DataBaseHelper.batchUpdate(
                        DataBaseHelper.getInsertSql(PvDaycgDetail.class),
                        daycgDetailList);
            }
        }
    }

    /**
     * 交易号：003004002012<> 查询结算记录明细<br>
     *
     * @param param
     * @param user
     * @return
     * @throws
     * @author wangpeng
     * @date 2016年12月09日
     */
    public Map<String, Object> getSettleDetailList(String param, IUser user) {
        BlSettle settle = JsonUtil.readValue(param, BlSettle.class);
        Map<String, Object> voMap = new HashMap<String, Object>();
        // 查询结算明细-表头
        Map<String, Object> stMap = DataBaseHelper
                .queryForMap(
                        "select "
                                + " sttype.name,st.date_st,st.eu_stresult,nvl(st.amount_st,0) as amount_st,nvl(st.amount_pi,0) as amount_pi "
                                + "from bl_settle st "
                                + "inner join bd_defdoc sttype on sttype.del_flag = '0' and st.dt_sttype=sttype.code and sttype.code_defdoclist='110102' "
                                + "where st.del_flag = '0' and  st.pk_settle = ?",
                        new Object[]{settle.getPkSettle()});
        if (stMap != null) {
            voMap.putAll(stMap);
        }
        // 结算发票号码
        List<Map<String, Object>> invMapList = DataBaseHelper
                .queryForList(
                        "select "
                                + " inv.code_inv "
                                + "from bl_st_inv stinv "
                                + "inner join bl_invoice inv on stinv.pk_invoice=inv.pk_invoice and inv.del_flag = '0' "
                                + "where stinv.del_flag = '0' and stinv.pk_settle = ?",
                        new Object[]{settle.getPkSettle()});
        if (invMapList != null) {
            voMap.put("invCodeList", invMapList);
        }
        // 结算明细
        List<Map<String, Object>> detailList = DataBaseHelper
                .queryForList(
                        "select "
                                + "dt.code_bill,dt.name_bill,nvl(sum(dt.amount),0) amt "
                                + "from bl_st_inv stinv "
                                + "inner join bl_invoice inv on stinv.pk_invoice=inv.pk_invoice and inv.del_flag = '0' "
                                + "inner join bl_invoice_dt dt on inv.pk_invoice=dt.pk_invoice and (dt.del_flag = '0' or dt.del_flag is null)"
                                + "where stinv.del_flag='0' and stinv.pk_settle=? "
                                + "group by dt.code_bill, dt.name_bill "
                                + "order by dt.code_bill",
                        new Object[]{settle.getPkSettle()});
        voMap.put("blInvoiceDtList", detailList);
        return voMap;
    }

    /**
     * 交易号：003004002014<> 删除患者<br>
     *
     * @param param
     * @param user
     * @return
     * @throws
     * @author wangpeng
     * @date 2017年01月11日
     */
    public void deletePiMaster(String param, IUser user) {
        User u = (User)user;
        PiMaster master = JsonUtil.readValue(param, PiMaster.class);
        String pkPi = master.getPkPi();
        int pv_count = DataBaseHelper.queryForScalar(
                "select count(1) from pv_encounter "
                        + "where del_flag = '0' and pk_pi = ?", Integer.class,
                pkPi);
        if (pv_count > 0) {
            throw new BusException("该患者存在就诊记录，无法删除！");
        } else {

            DataBaseHelper.update("Update pi_master set del_flag='1', modifier=? where pk_pi = ?",
                    u.getPkEmp(),pkPi);
            DataBaseHelper.update("Update pi_family set del_flag='1', modifier=? where pk_pi = ?",
                    u.getPkEmp(),pkPi);
            DataBaseHelper.update("Update pi_insurance set del_flag='1',modifier=? where pk_pi = ?",
                    u.getPkEmp(),pkPi);
            DataBaseHelper.update("Update pi_address set del_flag='1',modifier=? where pk_pi = ?",
                    u.getPkEmp(),pkPi);
            DataBaseHelper.update("Update pi_allergic set del_flag='1',modifier=? where pk_pi = ?",
                    u.getPkEmp(),pkPi);
            DataBaseHelper.update("Update pi_dise set del_flag='1',modifier=? where pk_pi = ?",
                    u.getPkEmp(),pkPi);

           /* DataBaseHelper.execute("Delete from pi_master where pk_pi = ?",
                    new Object[]{pkPi});
            DataBaseHelper.execute("Delete from pi_family where pk_pi = ?",
                    new Object[]{pkPi});
            DataBaseHelper.execute("Delete from pi_insurance where pk_pi = ?",
                    new Object[]{pkPi});
            DataBaseHelper.execute("Delete from pi_address where pk_pi = ?",
                    new Object[]{pkPi});
            DataBaseHelper.execute("Delete from pi_allergic where pk_pi = ?",
                    new Object[]{pkPi});
            DataBaseHelper.execute("Delete from pi_dise where pk_pi = ?",
                    new Object[]{pkPi});*/
        }
    }
}
