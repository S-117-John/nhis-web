package com.zebone.nhis.compay.ins.shenzhen.service.yd;

import com.zebone.nhis.common.module.compay.ins.shenzhen.*;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.shenzhen.dao.yd.ShenZhenYdMapper;
import com.zebone.nhis.compay.ins.shenzhen.vo.city.SettleInfoMore;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShenZhenYdService {
    @Autowired
    private ShenZhenYdMapper shenZhenYdMapper;

    /**
     * 深圳异地医保-保存身份验证接口返回数据
     * 015001011003
     *
     * @param param
     * @param user
     */
    public String saveVisitInfo(String param, IUser user) {
        InsSzybVisit insSzybVisit = JsonUtil.readValue(param, InsSzybVisit.class);
        DataBaseHelper.insertBean(insSzybVisit);
        return insSzybVisit.getPkVisit();
    }

    /**
     * 深圳异地医保-根据医保登记主键查询医保登记数据
     * 015001011004
     *
     * @param param
     * @param user
     */
    public InsSzybVisit getVisitInfo(String param, IUser user) {
        String pkVisit = JsonUtil.getFieldValue(param, "ybRegPk");
        return DataBaseHelper.queryForBean("select * from ins_szyb_visit where pk_visit=?", InsSzybVisit.class, new Object[]{pkVisit});
    }

    /**
     * 深圳异地医保-根据就诊登记主键查询医保登记数据
     * 015001011009
     *
     * @param param
     * @param user
     */
    public InsSzybVisit getVisitInfoByPv(String param, IUser user) {
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");

        InsSzybVisit rtnVo = null;

        List<InsSzybVisit> visitList = DataBaseHelper.queryForList("select * from ins_szyb_visit where pk_pv=? order by create_time asc", InsSzybVisit.class, new Object[]{pkPv});

        if(visitList!=null && visitList.size()>0){
            //如果是医保结算业务使用，则返回最新的一条医保登记信息，否则获取第一次的医保登记信息
            if(paramMap.containsKey("flagJs") && !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"flagJs"))
                && "1".equals(CommonUtils.getPropValueStr(paramMap,"flagJs"))){
                rtnVo = visitList.get(visitList.size()-1);
            }else{
                rtnVo = visitList.get(0);
            }
        }

        return rtnVo;
    }

    /**
     * 深圳异地医保-更新医保登记数据
     * 015001011005
     *
     * @param param
     * @param user
     */
    public void updateVisitInfo(String param, IUser user) {
        InsSzybVisit insSzybVisit = JsonUtil.readValue(param, InsSzybVisit.class);
        String sql = "update ins_szyb_visit set pk_hp=:pkHp,pk_pv=:pkPv where pk_visit=:pkVisit";
        DataBaseHelper.update(sql, insSzybVisit);
    }

    /**
     * 深圳异地医保-根据就诊主键pk_pv查询住院收费明细bl_ip_dt的医保上传标志flag_insu为0的记录
     * 015001011013
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> qryBdItemAndOrderByPkPv(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
//        List<Map<String, Object>> maps = shenZhenYdMapper.qryUploadDetails(paramMap);
        return shenZhenYdMapper.qryUploadDetails(paramMap);
    }

    /**
     * 深圳异地医保-保存上传费用明细返回的交易号
     * 015001011033
     *
     * @param param
     * @param user
     */
    public void saveTransid(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<InsSzybCitycg> cgList = JsonUtil.readValue(
                JsonUtil.getJsonNode(param, "dtList"),
                new TypeReference<List<InsSzybCitycg>>() {
                });

        Set<String> cgCodeSet=new HashSet<String>((List<String>)paramMap.get("cgCode"));
        CommonUtils.convertSetToSqlInPart(cgCodeSet,"CODE_CG || SORTNO");
        int count = DataBaseHelper.update("update bl_ip_dt set name_itemset=:transid,flag_insu='1' where pk_pv=:pkPv and del_flag='0' " +
                "and (flag_settle='0' or flag_settle is null) and flag_insu='0' " +
                "and (CODE_CG || SORTNO) in ("+CommonUtils.convertSetToSqlInPart(cgCodeSet,"CODE_CG || SORTNO")+")",new Object[]{paramMap.get("transid"),paramMap.get("pkPv")});

        if(cgList!=null && cgList.size()>0){
            //先删除重复上传的数据
            DataBaseHelper.execute(
                    "delete from ins_szyb_citycg where pk_pv = ? and YKC610 in ("+CommonUtils.convertSetToSqlInPart(cgCodeSet,"YKC610")+")",
                    new Object[]{paramMap.get("pkPv")});

            cgList.stream()
                    .forEach(dt->{
                        dt.setPkPv(CommonUtils.getPropValueStr(paramMap,"pkPv"));
                        ApplicationUtils.setDefaultValue(dt,true);
                    });

            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybCitycg.class),cgList);
        }
    }

    /**
     * 深圳异地医保-查询已上传医保的费用的交易号
     * 015001011034
     *
     * @param param
     * @param user
     * @return
     */
    public String getUpMedicareData(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        List<Map<String, Object>> maps = DataBaseHelper.queryForList("select * from bl_ip_dt where pk_pv=? and (flag_settle='0' or flag_settle is null)", new Object[]{pkPv});
        String transid = null;
        if (maps != null && maps.size() > 0) {
            for (Map<String, Object> temp : maps) {
                if (temp.get("nameItemset") != null) {
                    transid = temp.get("nameItemset").toString();
                    break;
                }
            }
        }
        return transid;
    }

    /**
     * 深圳异地医保-费用回退更新flag_insu
     * 015001011036
     *
     * @param param
     * @param user
     */
    public void updateCancelCg(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        int count = DataBaseHelper.update("update bl_ip_dt set flag_insu='0' where pk_pv=? and del_flag='0' and (flag_settle='0' or flag_settle is null) and flag_insu='1'", new Object[]{pkPv});
    }

    /**
     * 深圳异地医保-查询患者未结费用总额
     * 015001011037
     *
     * @param param
     * @param User
     * @return
     */
    public String getPiAmt(String param, IUser User) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        Map<String, Object> amtMap = DataBaseHelper.queryForMap("select sum(amount) amt from bl_ip_dt where pk_pv=? and (flag_settle='0' or flag_settle is null)", pkPv);
        if (amtMap.get("amt") != null) {
            return amtMap.get("amt").toString();
        }
        return "0";
    }

    /**
     * 深圳异地医保-保存结算数据
     * 015001011040
     *
     * @param param
     * @param user
     */
    public void saveSt(String param, IUser user) {
        SettleInfoMore settleInfoMore = JsonUtil.readValue(param, SettleInfoMore.class);
        InsSzybSt st = settleInfoMore.getInsSzybSt();
        InsSzybStCity stCity = settleInfoMore.getInsSzybStCity();
        InsSzybStDiff stDiff = settleInfoMore.getInsSzybStDiff();
        User u = (User) user;
        if (st == null)
            throw new BusException("未获得结算基本信息！");
        if (stCity == null)
            throw new BusException("未获得异地医保结算信息！");

        //先逻辑删除本次就诊中之前结算插入的数据
        String sql = "update ins_szyb_st set del_flag='1' where pk_visit=? and pk_settle is null";
        DataBaseHelper.update(sql, new Object[]{st.getPkVisit()});

        sql = "update ins_szyb_st_city set del_flag='1' where pk_insst in (select pk_insst from ins_szyb_st where pk_visit=? )";
        DataBaseHelper.update(sql, new Object[]{st.getPkVisit()});

        //插表ins_szyb_st
        st.setPkOrg(u.getPkOrg());
        st.setDelFlag("0");
        st.setCreateTime(new Date());
        st.setCreator(u.getPkUser());
        st.setTs(new Date());
        DataBaseHelper.insertBean(st);

        //插表ins_szyb_st_city
//        stCity.setPkInsst(st.getPkInsst());
//        stCity.setPkOrg(u.getPkOrg());
//        stCity.setDelFlag("0");
//        stCity.setCreateTime(new Date());
//        stCity.setCreator(u.getPkUser());
//        stCity.setTs(new Date());
//        DataBaseHelper.insertBean(stCity);

        stDiff.setPkInsst(st.getPkInsst());
        DataBaseHelper.insertBean(stDiff);
    }

    /**
     * 深圳异地医保-查询医保结算数据
     * 015001011041
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getStInfo(String param, IUser user) {
        String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
        String sql = "select ist.* from ins_szyb_st ist where ist.pk_visit=? and ist.del_flag='0'";
        List<Map<String, Object>> stMaps = DataBaseHelper.queryForList(sql, pkSettle);
        if (stMaps != null && stMaps.size() > 0) {
            return stMaps.get(0);
        } else {
            throw new BusException("未查询到结算记录，请联系维护人员！");
        }
    }

    /**
     * 深圳异地医保-保存关联关系,更新INS_*_ST表中的关联关系, 将PkSettle结算主键更新到INS_*_ST表
     * 015001011042
     *
     * @param param
     * @param user
     */
    public void updatePkSettle(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = paramMap.get("pkPv").toString();
        String pkSettle = paramMap.get("pkSettle").toString();

        if (pkPv != null && pkSettle != null) {
            if ("1".equals(pkSettle)) {
                DataBaseHelper.execute("update ins_szyb_st set PK_SETTLE='' where PK_PV=? and del_flag='0' and pk_settle is null", pkPv);
                DataBaseHelper.execute("update ins_szyb_visit set EU_STATUS_ST='0' where PK_PV=? and del_flag='0'", pkPv);
            } else {
                DataBaseHelper.execute("update ins_szyb_st set PK_SETTLE=? where PK_PV=? and del_flag='0' and pk_settle is null ", pkSettle, pkPv);
                DataBaseHelper.execute("update ins_szyb_visit set EU_STATUS_ST='1' where PK_PV=? and del_flag='0' ", pkPv);
            }
        } else {
            throw new BusException("传入参数有空值");
        }
    }

    /**
     * 深圳异地医保-获取医保科室对照字典
     * 015001011048
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getHpDictmap(String param, IUser user) {
        String pkDept = JsonUtil.getFieldValue(param, "pkDept");
        return shenZhenYdMapper.getHpDictmap(pkDept);
    }

    /**
     * 深圳异地医保-获取操作员备案信息
     * 015001011091
     *
     * @param param
     * @param use
     * @return
     */
    public Map<String, Object> getEmpRecord(String param, IUser use) {
        String pkEmp = JsonUtil.getFieldValue(param, "pkEmp");
        String sql = "select code_insur as usercode,password as userpass from BD_OU_EMPLOYEE emp inner join INS_SZYB_DICTMAP dict on dict.PK_HIS=emp.PK_EMP where EU_HPDICTTYPE='01' and PK_EMP = ?";
        return DataBaseHelper.queryForMap(sql, new Object[]{pkEmp});
    }

}
