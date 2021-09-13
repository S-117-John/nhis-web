package com.zebone.nhis.pro.zsba.ex.service;

import com.zebone.nhis.common.module.pv.PvIpDaily;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.ex.dao.ZsbaPvIpdailyMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @Classname ZsbaPvIpdailyService
 * @Description 中山生成病区日报
 * @Date 2020-12-12 12:39
 * @Created by wuqiang
 */
@Service
public class ZsbaPvIpdailyService {
    @Resource
    private ZsbaPvIpdailyMapper zsbaPvIpdailyMapper;
    //统计病区日报
    public void statDeptNsReport(String[] pkOrgArr) throws ParseException {
        Map<String, Object> map = doInparamDeptReport(pkOrgArr);
        List<String> pkDeptArr= (List<String>) map.get("pkDepts");
        List<Map<String, Object>> bednumList = zsbaPvIpdailyMapper.getBedNumByDept(map);//额定，实际，病区
        List<Map<String, Object>> inhospYdList = zsbaPvIpdailyMapper.getQichuNumByDept(map);//期初=昨日留院，病区
        List<Map<String, Object>> admitList = zsbaPvIpdailyMapper.getDayInNumByDept(map);//今日入院
        List<Map<String, Object>> dischargeList = zsbaPvIpdailyMapper.getDayOutNumByDept(map);//今日出院
        List<Map<String, Object>> transOutList = zsbaPvIpdailyMapper.getDeptAdtOutNumByDept(map);//转往他科
        List<Map<String, Object>> transInList = zsbaPvIpdailyMapper.getDeptAdtInByDept(map);//他科转入
        List<Map<String, Object>> severenumList = zsbaPvIpdailyMapper.getBzNumByDept(map);//病重人数
        List<Map<String, Object>> riskynumList = zsbaPvIpdailyMapper.getBwNumByDept(map);//病危人数
        List<Map<String, Object>> deathnumList = zsbaPvIpdailyMapper.getDeathByDept(map);//死亡人数
        map.put("hldj", ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_I", ""));
        List<Map<String, Object>> nurseFirstList = zsbaPvIpdailyMapper.getHLNumByDept(map);//一级护理
        map.put("hldj", ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_T", ""));
        List<Map<String, Object>> nurseSpecList = zsbaPvIpdailyMapper.getHLNumByDept(map);//特级护理
        List<Map<String, Object>> accomnumList = zsbaPvIpdailyMapper.getAttendNumByDept(map);//留陪人数
        List<PvIpDaily> pvIpDailyLis = new ArrayList<PvIpDaily>(16);
        for (int i = 0; i < pkDeptArr.size(); i++) {
            PvIpDaily pvIpDaily = new PvIpDaily();
            pvIpDaily.setPkIpdaily(NHISUUID.getKeyId());
            pvIpDaily.setPkOrg(pkOrgArr[0]);
            pvIpDaily.setTs(new Date());
            pvIpDaily.setCreateTime(new Date());
            pvIpDaily.setPkDeptNs(pkDeptArr.get(i));
            if (bednumList.size() != 0 && bednumList != null) {
                for (Map<String, Object> bednum : bednumList) {
                    if (bednum.get("pkDept").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setBednum(new BigDecimal(bednum.get("bednum").toString()));
                        pvIpDaily.setBednumOpen(new BigDecimal(bednum.get("bednumOpen").toString()));
                        break;
                    }
                }
            }
            if (inhospYdList.size() != 0 && inhospYdList != null) {
                for (Map<String, Object> inhospyd : inhospYdList) {
                    if (inhospyd.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setInhospYd(new BigDecimal(inhospyd.get("inhospyd").toString()));
                        break;
                    }
                }
            }
            if (admitList.size() != 0 && admitList != null) {
                for (Map<String, Object> admit : admitList) {
                    if (admit.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setAdmit(new BigDecimal(admit.get("admit").toString()));
                        break;
                    }
                }
            }
            if (dischargeList.size() != 0 && dischargeList != null) {
                for (Map<String, Object> discharge : dischargeList) {
                    if (discharge.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setDischarge(new BigDecimal(discharge.get("discharge").toString()));
                        break;
                    }
                }
            }
            if (transOutList.size() != 0 && transOutList != null) {
                for (Map<String, Object> transOut : transOutList) {
                    if (transOut.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setTransOut(new BigDecimal(transOut.get("transout").toString()));
                        break;
                    }
                }
            }
            if (transInList.size() != 0 && transInList != null) {
                for (Map<String, Object> transIn : transInList) {
                    if (transIn.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setTransIn(new BigDecimal(transIn.get("transin").toString()));
                        break;
                    }
                }
            }
            if (severenumList.size() != 0 && severenumList != null) {
                for (Map<String, Object> severenum : severenumList) {
                    if (severenum.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setSeverenum(new BigDecimal(severenum.get("severenum").toString()));
                        break;
                    }
                }
            }
            if (riskynumList.size() != 0 && riskynumList != null) {
                for (Map<String, Object> riskynum : riskynumList) {
                    if (riskynum.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setRiskynum(new BigDecimal(riskynum.get("riskynum").toString()));
                        break;
                    }
                }
            }
            if (deathnumList.size() != 0 && deathnumList != null) {
                for (Map<String, Object> deathnum : deathnumList) {
                    if (deathnum.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setDeathnum(new BigDecimal(deathnum.get("deathnum").toString()));
                        break;
                    }
                }
            }
            if (nurseFirstList.size() != 0 && nurseFirstList != null) {
                for (Map<String, Object> nurseFirst : nurseFirstList) {
                    if (nurseFirst.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setNurseFirst(new BigDecimal(nurseFirst.get("hlnum").toString()));
                        break;
                    }
                }
            }
            if (nurseSpecList.size() != 0 && nurseSpecList != null) {
                for (Map<String, Object> nurseSpec : nurseSpecList) {
                    if (nurseSpec.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setNurseSpec(new BigDecimal(nurseSpec.get("hlnum").toString()));
                        break;
                    }
                }
            }
            if (accomnumList.size() != 0 && accomnumList != null) {
                for (Map<String, Object> accomnum : accomnumList) {
                    if (accomnum.get("pkDeptNs").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setAccomnum(new BigDecimal(accomnum.get("accomnum").toString()));
                        break;
                    }
                }
            }
            if (pvIpDaily != null) {
                BigDecimal inhospYd = pvIpDaily.getInhospYd() != null ? pvIpDaily.getInhospYd() : new BigDecimal(0.00);
                BigDecimal admit = pvIpDaily.getAdmit() != null ? pvIpDaily.getAdmit() : new BigDecimal(0.00);
                BigDecimal transIn = pvIpDaily.getTransIn() != null ? pvIpDaily.getTransIn() : new BigDecimal(0.00);
                BigDecimal transOut = pvIpDaily.getTransOut() != null ? pvIpDaily.getTransOut() : new BigDecimal(0.00);
                BigDecimal discharge = pvIpDaily.getDischarge() != null ? pvIpDaily.getDischarge() : new BigDecimal(0.00);
                pvIpDaily.setInhosp(inhospYd.add(admit).add(transIn).subtract(transOut).subtract(discharge));
                pvIpDaily.setDateSa(DateUtils.getDateMorning(new Date(), 0));
               // pvIpDaily.setDateSa(DateUtils.parseDate(String.valueOf(map.get("dateCurBegin")),"yyyyMMddHHmmss"));
                pvIpDailyLis.add(pvIpDaily);
            }
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvIpDaily.class), pvIpDailyLis);
    }

    /*
     * @Description 统计科室日报
     * @auther wuqiang
     * @Date 2020-12-12
     * @Param [pkOrgArr]
     * @return void
     */
    public void statDeptReport(String[] pkOrgArr) throws ParseException {
        Map<String, Object> map = doInparamDeptReport(pkOrgArr);
        List<String> pkDeptArr= (List<String>) map.get("pkDeptList");
        List<Map<String, Object>> inhospYdList = zsbaPvIpdailyMapper.getQichuNumByDeptDe(map);//期初=昨日留院，科室
        List<Map<String, Object>> admitList = zsbaPvIpdailyMapper.getDayInNumByDeptDe(map);//今日入院
        List<Map<String, Object>> dischargeList = zsbaPvIpdailyMapper.getDayOutNumByDeptDe(map);//今日出院
        List<Map<String, Object>> transOutList = zsbaPvIpdailyMapper.getDeptAdtOutNumByDeptDe(map);//转往他科
        List<Map<String, Object>> transInList = zsbaPvIpdailyMapper.getDeptAdtInByDeptDe(map);//他科转入
        List<PvIpDaily> pvIpDailyLis = new ArrayList<PvIpDaily>(16);
        for (int i = 0; i < pkDeptArr.size(); i++) {
            PvIpDaily pvIpDaily = new PvIpDaily();
            pvIpDaily.setPkDept(pkDeptArr.get(i));
            pvIpDaily.setPkIpdaily(NHISUUID.getKeyId());
            pvIpDaily.setPkOrg(pkOrgArr[0]);
            pvIpDaily.setTs(new Date());
            pvIpDaily.setCreateTime(new Date());
            if (inhospYdList.size() != 0 && inhospYdList != null) {
                for (Map<String, Object> inhospyd : inhospYdList) {
                    if (inhospyd.get("pkDept").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setInhospYd(new BigDecimal(inhospyd.get("inhospyd").toString()));
                        break;
                    }
                }
            }
            if (admitList.size() != 0 && admitList != null) {
                for (Map<String, Object> admit : admitList) {
                    if (admit.get("pkDept").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setAdmit(new BigDecimal(admit.get("admit").toString()));
                        break;
                    }
                }
            }
            if (dischargeList.size() != 0 && dischargeList != null) {
                for (Map<String, Object> discharge : dischargeList) {
                    if (discharge.get("pkDept").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setDischarge(new BigDecimal(discharge.get("discharge").toString()));
                        break;
                    }
                }
            }
            if (transOutList.size() != 0 && transOutList != null) {
                for (Map<String, Object> transOut : transOutList) {
                    if (transOut.get("pkDept").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setTransOut(new BigDecimal(transOut.get("transout").toString()));
                        break;
                    }
                }
            }
            if (transInList.size() != 0 && transInList != null) {
                for (Map<String, Object> transIn : transInList) {
                    if (transIn.get("pkDept").equals(pkDeptArr.get(i))) {
                        pvIpDaily.setTransIn(new BigDecimal(transIn.get("transin").toString()));
                        break;
                    }
                }
            }

            if (pvIpDaily != null) {
                BigDecimal inhospYd = pvIpDaily.getInhospYd() != null ? pvIpDaily.getInhospYd() : new BigDecimal(0.00);
                BigDecimal admit = pvIpDaily.getAdmit() != null ? pvIpDaily.getAdmit() : new BigDecimal(0.00);
                BigDecimal transIn = pvIpDaily.getTransIn() != null ? pvIpDaily.getTransIn() : new BigDecimal(0.00);
                BigDecimal transOut = pvIpDaily.getTransOut() != null ? pvIpDaily.getTransOut() : new BigDecimal(0.00);
                BigDecimal discharge = pvIpDaily.getDischarge() != null ? pvIpDaily.getDischarge() : new BigDecimal(0.00);
                pvIpDaily.setInhosp(inhospYd.add(admit).add(transIn).subtract(transOut).subtract(discharge));
                pvIpDaily.setDateSa(DateUtils.getDateMorning(new Date(), 0));
                //pvIpDaily.setDateSa(DateUtils.parseDate(String.valueOf(map.get("dateCurBegin")),"yyyyMMddHHmmss"));
                pvIpDailyLis.add(pvIpDaily);
            }
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvIpDaily.class), pvIpDailyLis);
    }

    //处理输入参数
    public Map<String, Object> doInparamDeptReport(String[] pkOrgArr) {
        String[] dateArr = DateUtils.getDate().split("-");
        String dateSa = dateArr[0] + dateArr[1] + dateArr[2] + "000000";
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> pkDeptArr = zsbaPvIpdailyMapper.queryDeptByOrg(pkOrgArr,"02");//查询该机构下有多少病区
        if (pkDeptArr.size() == 0 || pkDeptArr == null) {
            throw new BusException("未得到病区集合");
        }
        List<String > pkDept=zsbaPvIpdailyMapper.queryDeptByOrg(pkOrgArr, "01");//查询该机构下有多少临床科室
        if (pkDept.size() == 0 || pkDept == null) {
            throw new BusException("未得到科室集合");
        }
        map.put("pkDepts", pkDeptArr);
        map.put("pkDeptList", pkDept);
        String dateEnd = "";
        String yesBegin = "";
        String yesEnd = "";
        //前前一天，用来统计期初人数
        String yesBeforeBegin = "";
        String yesBeforeEnd = "";
        if (!"".equals(dateSa)) {
            dateEnd = dateSa.substring(0, 8) + "235959";
            map.put("dateCurEnd", dateEnd);
            map.put("dateCurBegin", dateSa);
            try {
                yesBegin = DateUtils.getSpecifiedDateStr(DateUtils.getDefaultDateFormat().parse(dateSa), -1);
                map.put("yesBegin", yesBegin + "000000");
                yesBeforeBegin = DateUtils.getSpecifiedDateStr(DateUtils.getDefaultDateFormat().parse(dateSa), -2);
            } catch (ParseException e) {
                throw new BusException("转换传入的日期异常");
            }
            yesEnd = yesBegin + "235959";
            yesBeforeEnd = yesBeforeBegin + "235959";
            map.put("yesEnd", yesEnd);
            map.put("yesBeforeEnd", yesBeforeEnd);
        }
        return map;
    }





}
