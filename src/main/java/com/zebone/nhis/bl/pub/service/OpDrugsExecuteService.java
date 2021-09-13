package com.zebone.nhis.bl.pub.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zebone.nhis.bl.pub.dao.OpDrugsExecuteMapper;
import com.zebone.nhis.bl.pub.vo.CnOrderVo;
import com.zebone.nhis.bl.pub.vo.ExStOccVo;
import com.zebone.nhis.common.module.ex.nis.ns.ExStOcc;
import com.zebone.nhis.common.module.ex.oi.ExInfusionOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊药品执行（中二）
 */
@Service
public class OpDrugsExecuteService {

    @Resource
    private OpDrugsExecuteMapper opDrugsExecuteMapper;

    /**
     * 根据患者查询医嘱信息
     * 007004001016
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryOrdInfoByPv(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        String bd0011 = ApplicationUtils.getSysparam("BD0011", true, "请联系管理员维护系统参数：BD0011");
        String bd0012 = ApplicationUtils.getSysparam("BD0012", true, "请联系管理员维护系统参数：BD0012");

        List<String> bdParams = Lists.newArrayList();
        bdParams.addAll(Arrays.asList(bd0011.split(",")));
        bdParams.addAll(Arrays.asList(bd0012.split(",")));
        Map<String, Object> qryMap = Maps.newHashMap();
        qryMap.put("bdParams", bdParams);
        qryMap.put("pkPv", pkPv);
        List<Map<String, Object>> ordInfos = opDrugsExecuteMapper.qryOrdInfoByPv(qryMap);

        return ordInfos;
    }

    /**
     * 根据医嘱查询附加项目和查询执行记录
     * 007004001017
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qryAdditItem(String param, IUser user) {
        //父医嘱主键
        String pkCnord = JsonUtil.getFieldValue(param, "pkCnord");
        //查询附加项目
        List<Map<String, Object>> items = opDrugsExecuteMapper.qryAdditItem(pkCnord);

        //查询执行记录
        List<Map<String, Object>> records = opDrugsExecuteMapper.qryExeRecord(pkCnord);

        Map<String, Object> resMap = Maps.newHashMap();
        resMap.put("items", items);
        resMap.put("records", records);
        return resMap;
    }

    /***
     * 根据条件查询执行记录
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPerformRecord(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkDept", UserContext.getUser().getPkDept());
        return opDrugsExecuteMapper.qryPerformRecord(paramMap);
    }


    /***
     * 取消医嘱执行
     * @param param
     * @param user
     * @return
     */
    public void cancelOrd(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("dateCanc", new Date());
        paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
        paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
        opDrugsExecuteMapper.updateInfusionOcc(paramMap);
        Object pkCnordObj = paramMap.get("pkCnord");
        if (pkCnordObj != null) {
            String pkCnord = (String) pkCnordObj;
            if (StringUtils.isNotBlank(pkCnord)) {
                opDrugsExecuteMapper.updateOrd(pkCnord);
            }
        }
    }

    /**
     * 药品/皮试医嘱执行
     * 007004001020
     *
     * @param param
     * @param user
     */
    public void OrdExecute(String param, IUser user) {
        User u = (User) user;
        List<CnOrderVo> cnOrderVos = JsonUtil.readValue(JsonUtil.getJsonNode(param, "selOrds"), new TypeReference<List<CnOrderVo>>() {
        }); //医嘱信息
        ExStOccVo exStOccVo = JsonUtil.readValue(JsonUtil.getJsonNode(param, "exStOcc"), ExStOccVo.class);//皮试结果
        List<ExInfusionOcc> executeVo = JsonUtil.readValue(JsonUtil.getJsonNode(param, "execRecord"), new TypeReference<List<ExInfusionOcc>>() {
        });//待执行的执行记录

        List<CnOrderVo> stOrds = Lists.newArrayList();//皮试医嘱
        List<CnOrderVo> pdOrds = Lists.newArrayList();//药品医嘱
        CnOrderVo mainOrd = new CnOrderVo();//同组医嘱中的主医嘱

        for (CnOrderVo ord : cnOrderVos) {
            if ("1".equals(ord.getEuSt())) {
                stOrds.add(ord);
            } else {
                pdOrds.add(ord);
            }
            if (ord.getOrdsn().equals(ord.getOrdsnParent())) {
                mainOrd = ord;
            }
        }
        //医嘱执行记录
        Set<String> pkInfuoccs = Sets.newHashSet();
        for (ExInfusionOcc exe : executeVo) {
            pkInfuoccs.add(exe.getPkInfuocc());
        }
        List<ExInfusionOcc> infusionOcc = DataBaseHelper.queryForList("select * from ex_infusion_occ where pk_cnord = ? and eu_status='0' and pk_infuocc in (" + CommonUtils.convertSetToSqlInPart(pkInfuoccs, "pk_infuocc") + ") ", ExInfusionOcc.class, mainOrd.getPkCnord());
        if (pdOrds.size() > 0) {//药品医嘱执行
            exOrd(u, mainOrd, infusionOcc,"");
        }
        if (stOrds.size() > 0) {//皮试医嘱执行
            List<ExStOcc> exStOccs = new ArrayList<ExStOcc>();
            if (exStOccVo.getRlOrds() == null || exStOccVo.getRlOrds().size() <= 0) {//皮试结果未关联医嘱则绑定主医嘱
                exStOccVo.setRlOrds(Lists.<CnOrderVo>newArrayList());
                exStOccs.add(setExStOcc(mainOrd, exStOccVo));
            } else {
                for (CnOrderVo ord : exStOccVo.getRlOrds()) {
                	exStOccVo.setEuStmode("0");  //皮试方法为皮内实验
                	exStOccVo.setFlagChk("1");
                	exStOccVo.setDateChk(new Date());
                    exStOccs.add(setExStOcc(ord, exStOccVo));
                }
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExStOcc.class), exStOccs);            
            exOrd(u, mainOrd, infusionOcc,exStOccs.get(0).getPkStocc());
            exStOccVo.getRlOrds().addAll(stOrds);
            
            if(stOrds.size() > 0){
            	for (int i = 0; i < stOrds.size(); i++) {           		
            		stOrds.get(i).setEuSt("0".equals(exStOccVo.getResult()) ? "2" : "3");
				}
            }
           
            DataBaseHelper.batchUpdate("update cn_order set eu_st = :euSt where pk_cnord = :pkCnord", exStOccVo.getRlOrds());
        }
    }

    /**
     * 执行
     *
     * @param u
     * @param mainOrd
     * @param infusionOcc
     */
    private void exOrd(User u, CnOrderVo mainOrd, List<ExInfusionOcc> infusionOcc,String pkStocc) {
        if (infusionOcc == null) {
            throw new BusException("没有可执行的记录,请先打印瓶贴后再执行!");
        } else {
            //执行
            Integer exCount = DataBaseHelper.queryForScalar("select count(1) from ex_infusion_occ where eu_status='3' and pk_cnord=?", Integer.class, mainOrd.getPkCnord());
            for (ExInfusionOcc occ : infusionOcc) {
                occ.setTimesOcc(exCount += 1);
                occ.setDateBegin(new Date());
                occ.setPkEmpBegin(u.getPkEmp());
                occ.setNameEmpBegin(u.getNameEmp());
                occ.setPkDeptOcc(u.getPkDept());
                if(StringUtils.isNotBlank(mainOrd.getEuSt()) && !"0".equals(mainOrd.getEuSt())) occ.setFlagSt("1");
                occ.setPkStocc(pkStocc);
                occ.setBarcode(mainOrd.getOrdsnParent()==null ? "" : mainOrd.getOrdsnParent().toString());
            }
            DataBaseHelper.batchUpdate("update ex_infusion_occ set eu_status='3', times_occ = :timesOcc, " +
            		"date_begin = :dateBegin, pk_emp_begin = :pkEmpBegin, name_emp_begin = :nameEmpBegin, " +
            		"pk_dept_occ=:pkDeptOcc, flag_st=:flagSt , pk_stocc=:pkStocc ,barcode=:barcode " +
            		" where pk_infuocc = :pkInfuocc", infusionOcc);
        }
    }

    private ExStOcc setExStOcc(CnOrderVo cnOrderVo, ExStOcc ex) {
        ExStOcc exStOcc = new ExStOcc();
        exStOcc.setPkPi(ex.getPkPi());
        exStOcc.setPkPv(ex.getPkPv());
        exStOcc.setPkCnord(cnOrderVo.getPkCnord());
        exStOcc.setPkOrgOcc(UserContext.getUser().getPkOrg());
        exStOcc.setPkDeptOcc(UserContext.getUser().getPkDept());
        exStOcc.setNameEmpOcc(UserContext.getUser().getNameEmp());
        exStOcc.setPkEmpOcc(UserContext.getUser().getPkEmp());
        exStOcc.setDateOcc(new Date());
        exStOcc.setDateBeginSt(ex.getDateBeginSt()); //皮试开始时间
        exStOcc.setDuartion(ex.getDuartion());  //观察时长
        exStOcc.setEuStmode(ex.getEuStmode());
        exStOcc.setPkDeptAp(ex.getPkDeptAp());  //患者就诊科室
        exStOcc.setFlagChk(ex.getFlagChk()); //审核标志
        exStOcc.setDateChk(ex.getDateChk()); //审核日期
        exStOcc.setPkEmpChk(ex.getPkEmpChk());  //审核人
        exStOcc.setNameEmpChk(ex.getNameEmpChk());  //审核人姓名

        exStOcc.setPkStocc(NHISUUID.getKeyId());
        exStOcc.setCreateTime(new Date());
        exStOcc.setCreator(UserContext.getUser().getPkEmp());
        exStOcc.setPkOrg(UserContext.getUser().getPkOrg());
        return exStOcc;
    }


    /**
     * 保存皮试结果，不关联医嘱
     *
     * @param param
     * @param user
     */
    public void saveStOccIrrelevancyOrd(String param, IUser user) {
        ExStOccVo exStOcc = JsonUtil.readValue(param, ExStOccVo.class);
        if (exStOcc == null)
            return;
        exStOcc.setPkOrg(UserContext.getUser().getPkOrg());
        exStOcc.setPkOrgOcc(UserContext.getUser().getPkOrg());
        DataBaseHelper.insertBean(exStOcc);
    }

    /**
     * 保存皮试结果，关联医嘱
     *
     * @param param
     * @param user
     */
    public void saveStOcc(String param, IUser user) {
        ExStOccVo exStOcc = JsonUtil.readValue(param, ExStOccVo.class);
        if (exStOcc == null)
            return;
        exStOcc.setPkOrg(UserContext.getUser().getPkOrg());
        exStOcc.setPkOrgOcc(UserContext.getUser().getPkOrg());
        List<CnOrderVo> ords = exStOcc.getRlOrds();
        for (CnOrderVo cnOrderVo : ords) {
            cnOrderVo.setEuSt(exStOcc.getEuSt());
        }
        DataBaseHelper.insertBean(exStOcc);
        String sql = "update cn_order set eu_st=:euSt where pk_cnord=:pkCnord";
        DataBaseHelper.batchUpdate(sql, ords);
    }


    /**
     * 查询打印信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnOrderVo> qryPrintInfo(String param, IUser user) {
        List<CnOrderVo> orderVos = JsonUtil.readValue(param, new TypeReference<List<CnOrderVo>>() {
        });
        List<CnOrderVo> cnOrderVos = opDrugsExecuteMapper.qryPrintInfo(orderVos);
        return opDrugsExecuteMapper.qryPrintInfo(orderVos);
    }

    /**
     * 打印瓶贴
     *
     * @param param
     * @param user
     */
    public void savePrint(String param, IUser user) {
        User u = (User) user;
        List<CnOrderVo> orderVos = JsonUtil.readValue(param, new TypeReference<List<CnOrderVo>>() {
        });

        CnOrderVo mainOrd = null;
        for (CnOrderVo ord : orderVos) {
            if (ord.getOrdsn().equals(ord.getOrdsnParent())) {
                mainOrd = ord;
            }
        }
        if (mainOrd == null) {
            throw new BusException("未查询到主医嘱信息！");
        }
        //需要执行的注射类药品医嘱的用法分类编码
        String bd0011 = ApplicationUtils.getSysparam("BD0011", true, "请联系管理员维护系统参数：BD0011");
        //需要执行的输液类药品医嘱的用法分类编码
        String bd0012 = ApplicationUtils.getSysparam("BD0012", true, "请联系管理员维护系统参数：BD0012");

        ExInfusionOcc exInfusionOcc = new ExInfusionOcc();
        exInfusionOcc.setPkPv(mainOrd.getPkPv());
        exInfusionOcc.setPkCnord(mainOrd.getPkCnord());
        exInfusionOcc.setOrdsnParent(mainOrd.getOrdsnParent());
        exInfusionOcc.setBarcode("");
        exInfusionOcc.setEuType(StringUtils.contains(bd0011, mainOrd.getClsCode()) ? "0" : StringUtils.contains(bd0012, mainOrd.getClsCode()) ? "2" : "9");
        exInfusionOcc.setPkDeptOcc(u.getPkDept());
        exInfusionOcc.setFlagPrint("1");
        exInfusionOcc.setDatePrint(new Date());
        exInfusionOcc.setEuStatus("0");
        exInfusionOcc.setFlagSt("0");
        exInfusionOcc.setFlagInc("0");
        exInfusionOcc.setFlagReact("0");

        DataBaseHelper.insertBean(exInfusionOcc);
    }

    /**
     * 待皮试医嘱查询
     * 007004001025
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryStOrd(String param, IUser user) {
        List<CnOrderVo> cnOrderVos = JsonUtil.readValue(param, new TypeReference<List<CnOrderVo>>() {
        });
        //原液皮试医嘱主键
        List<String> liquidTestOrds = Lists.newArrayList();
        for (CnOrderVo temp : cnOrderVos) {
            if ("1".equals(temp.getSupaSt())) {
                liquidTestOrds.add(temp.getPkCnord());
            }
        }
        List<Map<String, Object>> stOrds = Lists.newArrayList();
        if (liquidTestOrds.size() > 0) {
            List<Map<String, Object>> liquidSts = opDrugsExecuteMapper.qryLiquidSt(liquidTestOrds);//查询原液皮试
            stOrds.addAll(liquidSts);
        } else {
            List<Map<String, Object>> doseSt = opDrugsExecuteMapper.qryDoseSt(cnOrderVos.get(0).getPkPv());//查询皮试剂皮试
            stOrds.addAll(doseSt);
        }
        return stOrds;
    }
}
