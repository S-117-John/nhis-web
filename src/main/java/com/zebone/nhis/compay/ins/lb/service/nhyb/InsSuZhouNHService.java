package com.zebone.nhis.compay.ins.lb.service.nhyb;

import org.apache.commons.lang3.StringUtils;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhJs;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhJsGrade;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhReginfo;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhTreat;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.lb.dao.nhyb.InsSuZhouNHMapper;
import com.zebone.nhis.compay.ins.lb.service.pub.dao.LbYbPubMapper;
import com.zebone.nhis.compay.ins.lb.vo.nhyb.CnOrderAndBdItem;
import com.zebone.nhis.compay.ins.lb.vo.nhyb.TreatInfo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.awt.AppContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InsSuZhouNHService {

    @Autowired
    private InsSuZhouNHMapper insSuZhouMapper;
    @Autowired
    private LbYbPubMapper lbYbPubMapper;

    /**
     * 保存单病种治疗方式
     *
     * @param param
     * @param user
     */
    public void saveTreatInfo(String param, IUser user) {
        List<TreatInfo> TreatInfoList = JsonUtil.readValue(param, List.class);
        if (TreatInfoList != null && TreatInfoList.size() > 0) {
            for (TreatInfo treatInfos : TreatInfoList) {
                InsSuzhounhTreat insSuzhounhTreat = new InsSuzhounhTreat();
                insSuzhounhTreat.setId(NHISUUID.getKeyId());
                insSuzhounhTreat.setDbzicdbm(treatInfos.getDBZICDBM());
                insSuzhounhTreat.setDbzicdmc(treatInfos.getDBZICDMC());
                insSuzhounhTreat.setZlfsbm(treatInfos.getZLFSBM());
                insSuzhounhTreat.setZlfsmc(treatInfos.getZLFSMC());
                insSuzhounhTreat.setYl1(treatInfos.getYL1());
                insSuzhounhTreat.setYl2(treatInfos.getYL2());
                insSuzhounhTreat.setYl3(treatInfos.getYL3());
                insSuzhounhTreat.setYl4(treatInfos.getYL4());
                insSuzhounhTreat.setYl5(treatInfos.getYL5());
                DataBaseHelper.insertBean(insSuzhounhTreat);
            }
        }
    }

    /**
     * 查询单病种治疗方式
     *
     * @param param
     * @param user
     */
    public List<InsSuzhounhTreat> getTreatInfo(String param, IUser user) {
        String key = JsonUtil.readValue(param, String.class);
        List<InsSuzhounhTreat> insSuzhounhTreatList = new ArrayList<InsSuzhounhTreat>();
        if (key != null) {
            insSuzhounhTreatList = insSuZhouMapper.getInsSuzhounhTreatList(key);
        } else {
            insSuzhounhTreatList = null;
        }
        return insSuzhounhTreatList;
    }

    /**
     * 标准保存宿州农合登记信息
     *
     * @param param
     * @param user
     * @return
     */
    public InsSuzhounhReginfo saveReginfo(String param, IUser user) {
        InsSuzhounhReginfo regInfo = JsonUtil.readValue(param, InsSuzhounhReginfo.class);
        if (regInfo != null) {
            if (!StringUtils.isBlank(regInfo.getId())) {
                DataBaseHelper.updateBeanByPk(regInfo, false);
            } else {
                DataBaseHelper.insertBean(regInfo);
            }
        }
        return regInfo;
    }

    /**
     * 撤销宿州农合登记信息
     *
     * @param param
     * @param user
     */
    public void deleteRegInfo(String param, IUser user) {
        InsSuzhounhReginfo regInfo = JsonUtil.readValue(param, InsSuzhounhReginfo.class);
        if (regInfo != null) {
            if (!StringUtils.isBlank(regInfo.getId())) {
                DataBaseHelper.deleteBeanByPk(regInfo);
            } else if (!StringUtils.isBlank(regInfo.getPkPv())) {
                insSuZhouMapper.deleteRegInfo(regInfo.getPkPv());
            }
        }
    }

    /**
     * 按照PKPV查询宿州农合登记信息
     *
     * @param param
     * @param user
     * @return
     */
    public InsSuzhounhReginfo getRegInfoByPkpv(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        InsSuzhounhReginfo regInfo = insSuZhouMapper.getRegInfoByPkPv(pkPv);
        if (regInfo != null) {
            return regInfo;
        } else {
            throw new BusException("查不到该患者的登记信息，无法获取数据！");
        }
    }

    /**
     * 保存宿州农保结算数据
     *
     * @param param
     * @param user
     * @return
     */
    public InsSuzhounhJs saveSuZhouNhJs(String param, IUser user) {
        InsSuzhounhJs jsInfo = JsonUtil.readValue(param, InsSuzhounhJs.class);
        if (CommonUtils.isNotNull(jsInfo)) {

            if (!StringUtils.isBlank(jsInfo.getId())) {
                DataBaseHelper.updateBeanByPk(jsInfo, false);
            } else {
                DataBaseHelper.insertBean(jsInfo);
            }
            if (CommonUtils.isNotNull(jsInfo.getGradeList())) {
                List<InsSuzhounhJsGrade> jsGradeList = jsInfo.getGradeList();
                for (int i = 0; i < jsGradeList.size(); i++) {
                    ApplicationUtils.setDefaultValue(jsGradeList.get(i), true);
                    jsGradeList.get(i).setPkJs(jsInfo.getId());
                }
                if (jsGradeList.size() > 0) {
                    System.out.println(DataBaseHelper.getInsertSql(InsSuzhounhJsGrade.class)
                    );
                    DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSuzhounhJsGrade.class)
                            , jsGradeList);
                }
            }
        }
        return jsInfo;
    }

    /**
     * 获取宿州农保结算信息
     *
     * @param param
     * @param user
     * @return
     */
    public InsSuzhounhJs getJsByPkSettle(String param, IUser user) {
        String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
        InsSuzhounhJs jsInfo = insSuZhouMapper.getJsInfoByPkSettle(pkSettle);
        if (jsInfo != null) {
            return jsInfo;
        } else {
            throw new BusException("查不到该次的医保结算记录，无法获取数据！");
        }
    }

    /**
     * 删除宿州农保结算信息
     *
     * @param param
     * @param user
     */
    public void deleteJsInfo(String param, IUser user) {
        InsSuzhounhJs jsInfo = JsonUtil.readValue(param, InsSuzhounhJs.class);
        if (jsInfo != null) {
            String sql = "update INS_SUZHOUNH_JS_GRADE set del_flag =? where pk_js=? ";

            if (!StringUtils.isBlank(jsInfo.getId())) {
                DataBaseHelper.deleteBeanByPk(jsInfo);
                DataBaseHelper.update(sql, new Object[]{"1", jsInfo.getId()});
            } else if (!StringUtils.isBlank(jsInfo.getPkSettle())) {
                insSuZhouMapper.deleteJsInfo(jsInfo.getPkSettle());
                sql = "update INS_SUZHOUNH_JS_GRADE set del_flag =? where pk_settle=? ";
                DataBaseHelper.update(sql, new Object[]{"1", jsInfo.getPkSettle()});
            }
        }
    }

    /**
     * 根据就诊主键获取bl_ip_dt中未上传至医保的收费项目和医嘱集合
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnOrderAndBdItem> qryBdItemAndOrderByPkPv(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map != null && map.size() > 0) {
            List<CnOrderAndBdItem> cBdItems = insSuZhouMapper.qryBdItemAndOrderByPkPv(map);
            return cBdItems;
        } else {
            return null;
        }

    }

    /**
     * 015001004012
     * 根据农合登记主键更改pkPv
     *
     * @param param
     * @param user
     */
    public void createNhisOrNhybKind(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || paramMap.size() == 0) return;
        String sql = "update INS_SUZHOUNH_REGINFO set pk_pv=? where del_flag='0' and id=?";
        DataBaseHelper.update(sql, new Object[]{paramMap.get("pkPv"), paramMap.get("id")});

    }

    /**
     * [交易号]015001004013
     * 根据农合结算主键更新pk_settle,建立与bl_settle的关系
     *
     * @param param
     * @param user
     */
    public void updateNhJsPkSettle(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return;
        String sql = "update ins_suzhounh_js set pk_settle=? where del_flag='0' and id=? ";
        DataBaseHelper.update(sql, new Object[]{paramMap.get("pkSettle"), paramMap.get("id")});
        String sql1 = "update INS_SUZHOUNH_JS_GRADE set pk_settle=? where pk_js=?";
        DataBaseHelper.update(sql1, new Object[]{paramMap.get("pkSettle"), paramMap.get("id")});
    }

    /**
     * 015001006012
     * 查询nhis与农合医保匹配未匹配信息
     *
     * @param param{"pkHp":"医保主键","euMatch":"1(匹配)/2(未匹配)","type":"1(药品)/2(收费项目)"}
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryNhItemAndPdWithInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        String euActive = paramMap.get("euActive").toString();
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (paramMap.get("euMatch") != null && "1".equals(paramMap.get("euMatch"))) {
            if (paramMap.get("type") != null && "1".equals(paramMap.get("type"))) {
                if (euActive.equals("1")) {//启用
                    paramMap.put("euActive", 0);
                } else {//停用
                    paramMap.put("euActive", 1);
                }
                mapList = insSuZhouMapper.qryNhPdWithInfo(paramMap);//查询药品对照
            } else if (paramMap.get("type") != null && "2".equals(paramMap.get("type"))) {
                mapList = insSuZhouMapper.qryNhItemWithInfo(paramMap);//查询项目对照
            }
        } else if (paramMap.get("euMatch") != null && "2".equals(paramMap.get("euMatch"))) {
            if (paramMap.get("type") != null && "1".equals(paramMap.get("type"))) {
                if (euActive.equals("1")) {//启用
                    paramMap.put("euActive", 0);
                } else {//停用
                    paramMap.put("euActive", 1);
                }
                mapList = lbYbPubMapper.qryYbPdDicNoWithInfo(paramMap);
            } else if (paramMap.get("type") != null && "2".equals(paramMap.get("type"))) {
                mapList = lbYbPubMapper.qryYbItemDicNoWithInfo(paramMap);
            }
        }
        return mapList;
    }

    /**
     * 宿州农合医保发票打印，需要的结算信息
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qrySettleInfoForFpPrint(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        
        Map<String,Object> retMap = new HashMap<>();
        if (paramMap == null) return null;
        List<String> pkInvoices = new ArrayList<>();
        if (paramMap.get("pkSettle") != null) {
        	retMap = insSuZhouMapper.qrySettleInfoForFpPrint(paramMap);
            pkInvoices = insSuZhouMapper.qrySettleInfoForFpid(paramMap);
            retMap.put("pkInvoices", pkInvoices);
        }
        return retMap;
    }
    
    /**
     * 根据就诊主键获取bl_op_dt中未结算的正的门诊挂号费
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnOrderAndBdItem> qryBlOpCgByPkPv(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map != null && map.size() > 0) {
        	map.put("pkOrg", UserContext.getUser().getPkOrg());
            List<CnOrderAndBdItem> cBdItems = insSuZhouMapper.qryBlOpCgByPkPv(map);
            return cBdItems;
        } else {
            return null;
        }

    }
}
