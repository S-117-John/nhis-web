package com.zebone.nhis.pro.zsrm.bl.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.bl.ExCgopOcc;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsrm.bl.dao.ZsrmBlMedicalExMapper;
import com.zebone.nhis.pro.zsrm.bl.vo.CnOrderVo;
import com.zebone.nhis.pro.zsrm.bl.vo.ExOrderVo;
import com.zebone.nhis.pro.zsrm.bl.vo.ExtCostOccVo;
import com.zebone.nhis.pro.zsrm.bl.vo.PageMedListVo;
import com.zebone.nhis.pv.pub.vo.PagePvListVo;
import com.zebone.nhis.pv.pub.vo.PvEncounterListVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 医疗执行
 */
@Service
public class ZsrmBlMedicalExService {

    @Resource
    private ZsrmBlMedicalExMapper zsrmBlMedicalExMapper;
    @Autowired
    private OpCgPubService opCgPubService;


    /**
     * 医技业务-门诊页签-查询功能：查询门急诊执行申请单
     *
     * @param param
     * @param user
     * @return
     */
    public PageMedListVo queryOpMedicalApp(String param, IUser user) {
        PageMedListVo pageMedListVo = new PageMedListVo();
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        if (CommonUtils.isNotNull(paramMap.get("dateBegin"))) {
            paramMap.put("dateBegin", CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8) + "000000");
        }else{
            throw new BusException("请选择开始时间!");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
            paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8) + "235959");
        } else {
            throw new BusException("请选择结束时间!");
        }
        Date dateBegin = DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateBegin")));
        Date dateEnd = DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateEnd")));
        int day = DateUtils.getDateSpace(dateBegin, dateEnd);
        if (paramMap.get("codeOp") == null || StringUtils.isBlank(paramMap.get("codeOp").toString())) {
            if (day > 31) {
                throw new BusException("按时间查询范围不能超过31天!");
            }
        } else {
            if (day > 90) {
                throw new BusException("按患者信息查询时间范围不能超过90天!");
            }
        }
        String flagCurrentAndAll = CommonUtils.getString(paramMap.get("flagCurrentAndAll"));
        String codeOp = CommonUtils.getString(paramMap.get("codeOp"));
        if (CommonUtils.isNotNull(codeOp) && "1".equals(flagCurrentAndAll)) {
            paramMap.put("pkDeptExec", "");
        } else {
            List<String> pkDeptAreaList = zsrmBlMedicalExMapper.qeryDeptAreaZsrm(UserContext.getUser().getPkDept());
            if (pkDeptAreaList.size() > 0) {
                paramMap.put("pkDeptArea", pkDeptAreaList.get(0));
                paramMap.put("pkDeptExec", "");
                //判断初始化查询标志，返回数据为null不进行提示
            }else {
                paramMap.put("pkDeptExec", UserContext.getUser().getPkDept());
            }
        }
        int pageIndex = CommonUtils.getInteger(CommonUtils.getString(paramMap.get("pageIndex")));
        int pageSize = CommonUtils.getInteger(CommonUtils.getString(paramMap.get("pageSize")));
        // 分页操作
        String pkOrg = UserContext.getUser().getPkOrg();
        paramMap.put("pkOrg",pkOrg);
        MyBatisPage.startPage(pageIndex, pageSize);
        list = zsrmBlMedicalExMapper.qryOpMedAppInfo(paramMap);
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        pageMedListVo.setMedList(list);
        pageMedListVo.setTotalCount(page.getTotalCount());
        return pageMedListVo;
    }

    /**
     * 医技业务管理-执行功能：医技执行
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> excList(String param, IUser user) {

        List<Map<String, Object>> paramMapList = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        String pkDeptJob = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from BD_OU_EMPJOB where IS_MAIN='1' and DEL_FLAG = '0' and PK_EMP=? ", MapUtils.getString(paramMapList.get(0),"pkEmpOcc")), "pkDept");
        //查询当前科室是否是自定义参数0601日间手术科室
        String pkDeptOper = zsrmBlMedicalExMapper.qeryDeptOperationZsrm(UserContext.getUser().getPkDept());
        //当前科室不属于日间科室时，执行科室根据执行医师考勤科室一致
        if(CommonUtils.isNull(pkDeptOper)){
            pkDeptOper=pkDeptJob;
        }
        String pkCnord = "";
        String pkDeptArea;
        List<String> pkDeptAreaList = zsrmBlMedicalExMapper.qeryDeptAreaZsrm(UserContext.getUser().getPkDept());
        //当诊区为null时默认当前登陆科室
        if(pkDeptAreaList.size()<=0){
            pkDeptArea = UserContext.getUser().getPkDept();
        }else{
            pkDeptArea = pkDeptAreaList.get(0);
        }
        for (Map<String, Object> paramMap : paramMapList) {
            paramMap.put("dateOcc", DateUtils.getDateTime());
            //paramMap.put("pkEmpOcc", UserContext.getUser().getPkEmp());
            //paramMap.put("nameEmpOcc", UserContext.getUser().getNameEmp());
            paramMap.put("pkOrgOcc", UserContext.getUser().getPkOrg());
            paramMap.put("pkDeptOcc", pkDeptOper);
            paramMap.put("pkDeptArea",pkDeptArea);//执行诊区
            paramMap.put("pkDeptJob",pkDeptJob);//执行人考勤科室
            //2021.2.22_任务[5479]-增加确认人信息
            paramMap.put("pkEmpConf", UserContext.getUser().getPkEmp());//确认人
            paramMap.put("nameEmpConf",UserContext.getUser().getNameEmp());//确认人姓名
            // 数据校验
            // 门急诊：
            pkCnord = paramMap.get("pkCnord").toString();
            int count = zsrmBlMedicalExMapper.opDataCheck(paramMap.get("pkCnord").toString());
            if (count > 0) {
                throw new BusException("有结算费用，不能执行!");
            }
            // 医技执行
            try {
                zsrmBlMedicalExMapper.medExeOcc(paramMap);
            } catch (Exception e) {
                throw new BusException("执行失败！");
            }

            // 如果该执行单关联了预约信息，同时更新预约记录状态为“到达”
            List<Map<String, Object>> occlist = zsrmBlMedicalExMapper.queryExAssistOccList(paramMap);
            for (Map<String, Object> map : occlist) {
                if (map.get("pkMsp") != null) {
                    map.put("pkMsp", map.get("pkMsp").toString().trim());
                }
                if (map.get("dateAppt") != null) {
                    // 如果该执行单关联了预约信息(预约时间不为空)，同时更新预约记录状态为“到达”
                    DataBaseHelper.execute("update sch_appt  set eu_status='1' where pk_schappt in(select pk_schappt from sch_appt_ord where pk_assocc=?) and eu_status='0'", map.get("pkAssocc"));
                    // 更新医嘱预约记录的执行标志；
                    DataBaseHelper.execute("update sch_appt_ord set flag_exec='1' where pk_assocc=? and flag_exec='0'", map.get("pkAssocc"));
                }
            }
             //在拓展属性【0113】属性值为1的项目则更新收费项目执行科室到当前执行科室
            DataBaseHelper.execute("update bl_op_dt cg set cg.pk_dept_ex=?,cg.pk_emp_ex=?,cg.name_emp_ex=? where pk_cnord =? and " +
                    " flag_settle='1' And  Exists(Select 1 From bd_dictattr attr where attr.code_attr='0113' and " +
                    " cg.pk_item=attr.pk_dict)",new Object[]{UserContext.getUser().getPkDept(),UserContext.getUser().getPkEmp(),UserContext.getUser().getNameEmp(),paramMap.get("pkCnord").toString()});
            // 更新医嘱状态为3（检查）
            DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});
            DataBaseHelper.execute("update cn_lab_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});
            //发送检查检验记费信息至平台
            Map<String, Object> paramListMap = new HashMap<String, Object>();
            paramListMap.put("dtlist", paramMapList);
            paramListMap.put("type", "I");
            paramListMap.put("Control", "OK");
            PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
            paramListMap = null;
        }
        if(StringUtils.isNotEmpty(pkCnord)){
            Map<String, Object> map = new HashMap<>();
            map.put("pkCnord",pkCnord);
            return zsrmBlMedicalExMapper.queryExAssistOccList(map);
        }
        return null;
    }

    /**
     * 批量执行医技医嘱
     * 022006006020
     * @param param
     * @param user
     */
    public String batchExOrder(String param,IUser user) {
        List<ExOrderVo> exOrders = JsonUtil.readValue(JsonUtil.getJsonNode(param, "exOrders"), new TypeReference<List<ExOrderVo>>() {
        });
        //同一医嘱存在多条执行执行记录时需要操作员勾选执行，不勾选则不执行
        List<ExAssistOcc> exAssistOccs = JsonUtil.readValue(JsonUtil.getJsonNode(param, "occList"), new TypeReference<List<ExAssistOcc>>() {
        });

        //组织并校验数据
        StringBuffer rtnMsg = new StringBuffer();//返回前端提示信息
        Set<String> pkCnords = exOrders.stream().map(ExOrderVo::getPkCnord).collect(Collectors.toSet());
        String blSql = "select cg.NAME_CG, pi.NAME_PI,pi.code_op,ord.pk_cnord from BL_OP_DT cg " +
                "inner join CN_ORDER ord on ord.PK_CNORD = cg.PK_CNORD " +
                "inner join PI_MASTER pi on pi.PK_PI = cg.PK_PI " +
                "where flag_settle='0' and cg.PK_CNORD in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "PK_CNORD") + ")";
        List<Map<String, Object>> blMaps = DataBaseHelper.queryForList(blSql);
        if (CollectionUtils.isNotEmpty(blMaps)) {
            blMaps.forEach(m ->
                    rtnMsg.append("以下项目存在未缴费记录,执行失败!\n").
                            append(MapUtils.getString(m, "nameCg")).append(" ").
                            append(MapUtils.getString(m, "codeOp")).append(" ").
                            append(MapUtils.getString(m, "namePi"))
            );
            //过滤未缴费记录
            pkCnords = pkCnords.stream().filter(pk -> !blMaps.stream().map(map -> MapUtils.getString(map, "pkCnord")).collect(Collectors.toList()).contains(pk)).collect(Collectors.toSet());
        }

        //查询执行记录等于1条的数据
        List<ExAssistOcc> occListEqOne = DataBaseHelper.queryForList(
                "select * from EX_ASSIST_OCC where PK_CNORD in (select PK_CNORD from EX_ASSIST_OCC where PK_CNORD " +
                        "in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ") group by PK_CNORD having count(1)=1)", ExAssistOcc.class);
        //过滤未勾选的医嘱
        Set<String> finalPkCnords = pkCnords;
        exAssistOccs = exAssistOccs.stream().filter(occ -> finalPkCnords.contains(occ.getPkCnord())).collect(Collectors.toList());
        occListEqOne.addAll(exAssistOccs);

        //执行医嘱
        if (CollectionUtils.isNotEmpty(occListEqOne)) {
            String pkEmpOcc=exOrders.get(0).getPkEmpOcc();
            if(CommonUtils.isNull(pkEmpOcc)){
                pkEmpOcc=UserContext.getUser().getPkEmp();
            }
            String pkDeptJob = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from BD_OU_EMPJOB where IS_MAIN='1' and DEL_FLAG = '0' and PK_EMP=? ", new Object[]{pkEmpOcc}), "pkDept");
            //查询当前科室是否是自定义参数0601日间手术科室
            String pkDeptOper = zsrmBlMedicalExMapper.qeryDeptOperationZsrm(UserContext.getUser().getPkDept());
            //当前科室不属于日间科室时，执行科室根据执行医师考勤科室一致
            if (CommonUtils.isNull(pkDeptOper)) {
                pkDeptOper = UserContext.getUser().getPkDept();
            }

            String pkDeptArea;//诊区
            List<String> pkDeptAreaList = zsrmBlMedicalExMapper.qeryDeptAreaZsrm(UserContext.getUser().getPkDept());
            //当诊区为null时默认当前登陆科室
            if (pkDeptAreaList.size() <= 0) {
                pkDeptArea = UserContext.getUser().getPkDept();
            } else {
                pkDeptArea = pkDeptAreaList.get(0);
            }

            List<Map<String, Object>> paramMapList = Lists.newArrayList();  //消息发送入参
            for (ExAssistOcc occ : occListEqOne) {
                Map<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("dateOcc", DateUtils.getDateTime());
                paramMap.put("pkEmpOcc", StringUtils.isEmpty(exOrders.get(0).getPkEmpOcc()) ? UserContext.getUser().getPkEmp() : exOrders.get(0).getPkEmpOcc());
                paramMap.put("nameEmpOcc", StringUtils.isEmpty(exOrders.get(0).getPkEmpOcc()) ? UserContext.getUser().getNameEmp() : exOrders.get(0).getNameEmpOcc());
                paramMap.put("pkOrgOcc", UserContext.getUser().getPkOrg());
                paramMap.put("pkDeptOcc", pkDeptOper);
                paramMap.put("pkDeptArea", pkDeptArea);//执行诊区
                paramMap.put("pkDeptJob", pkDeptJob);//执行人考勤科室
                //2021.2.22_任务[5479]-增加确认人信息
                paramMap.put("pkEmpConf", UserContext.getUser().getPkEmp());//确认人
                paramMap.put("nameEmpConf", UserContext.getUser().getNameEmp());//确认人姓名
                paramMap.put("pkAssocc", occ.getPkAssocc());
                paramMap.put("pkCnord", occ.getPkCnord());

                // 医技执行
                try {
                    zsrmBlMedicalExMapper.medExeOcc(paramMap);
                } catch (Exception e) {
                    throw new BusException("执行失败！");
                }

                // 如果该执行单关联了预约信息，同时更新预约记录状态为“到达”
                List<Map<String, Object>> occlist = zsrmBlMedicalExMapper.queryExAssistOccList(paramMap);
                for (Map<String, Object> map : occlist) {
                    if (map.get("pkMsp") != null) {
                        map.put("pkMsp", map.get("pkMsp").toString().trim());
                    }
                    if (map.get("dateAppt") != null) {
                        // 如果该执行单关联了预约信息(预约时间不为空)，同时更新预约记录状态为“到达”
                        DataBaseHelper.execute("update sch_appt  set eu_status='1' where pk_schappt in(select pk_schappt from sch_appt_ord where pk_assocc=?) and eu_status='0'", map.get("pkAssocc"));
                        // 更新医嘱预约记录的执行标志；
                        DataBaseHelper.execute("update sch_appt_ord set flag_exec='1' where pk_assocc=? and flag_exec='0'", map.get("pkAssocc"));
                    }
                }
                //在拓展属性【0113】属性值为1的项目则更新收费项目执行科室到当前执行科室
                DataBaseHelper.execute("update bl_op_dt cg set cg.pk_dept_ex=?,cg.pk_emp_ex=?,cg.name_emp_ex=? where pk_cnord =? and " +
                        " flag_settle='1' And  Exists(Select 1 From bd_dictattr attr where attr.code_attr='0113' and " +
                        " cg.pk_item=attr.pk_dict)", new Object[]{UserContext.getUser().getPkDept(), UserContext.getUser().getPkEmp(), UserContext.getUser().getNameEmp(), paramMap.get("pkCnord").toString()});
                // 更新医嘱状态为3（检查）
                DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});
                DataBaseHelper.execute("update cn_lab_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});

                paramMapList.add(paramMap);
            }
            //发送检查检验记费信息至平台
            Map<String, Object> paramListMap = new HashMap<String, Object>();

            paramListMap.put("dtlist", paramMapList);
            paramListMap.put("type", "I");
            paramListMap.put("Control", "OK");
            PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
            paramListMap = null;
        }
        return rtnMsg.toString();
    }

    /**
     * 药品附加费用执行--查询患者医嘱信息
     * 022006006021
     *
     * @param param
     * @param userpibaseService
     * @return
     */
    public List<CnOrderVo> getPvOrdInfo(String param, IUser userpibaseService) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");

//        需要执行的注射类药品医嘱的用法分类编码
        String bd0011 = ApplicationUtils.getSysparam("BD0011", true);
//        需要执行的输液类药品医嘱的用法分类编码
        String bd0012 = ApplicationUtils.getSysparam("BD0012", true);

        if (StringUtils.isEmpty(bd0011) && StringUtils.isEmpty(bd0012)) {
            throw new BusException("请联系管理员维护好系统参数：BD0011、BD0012！");
        }
        List<String> bd0011s = Lists.newArrayList();
        List<String> bd0012s = Lists.newArrayList();
        if (StringUtils.isNotBlank(bd0011)) {
            bd0011s = Arrays.asList(bd0011.split(","));
        }
        if (StringUtils.isNotBlank(bd0012)) {
            bd0012s = Arrays.asList(bd0012.split(","));
        }
        List<String> clsCode = Lists.newArrayList();
        clsCode.addAll(bd0012s);
        clsCode.addAll(bd0011s);

        String sysParam = CommonUtils.convertListToSqlInPart(clsCode);
        Map<String, Object> qryParam = Maps.newHashMap();
        qryParam.put("sysParam", sysParam);
        qryParam.put("pkPv", pkPv);
        List<CnOrderVo> ordInfo = zsrmBlMedicalExMapper.getPvOrdInfo(qryParam);

        if (CollectionUtils.isNotEmpty(ordInfo)) {
            List<String> finalBd0011s = bd0011s;
            List<String> finalBd0012s = bd0012s;
            ordInfo.forEach(ord -> {
                if (CollectionUtils.isNotEmpty(finalBd0011s) && finalBd0011s.contains(ord.getClsCode())) {
                    ord.setEuType("0");
                } else if (CollectionUtils.isNotEmpty(finalBd0012s) && finalBd0012s.contains(ord.getClsCode())) {
                    ord.setEuType("1");
                } else {
                    ord.setEuType("9");
                }
            });
        }

        return ordInfo;
    }

    /**
     * 药品附加费用执行--根据医嘱查询收费项目
     * 022006006022
     *
     * @return
     */
    public List<ExtCostOccVo> getExtCostByOrd(String param, IUser user) {
        List<String> pkCnords = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });

        return zsrmBlMedicalExMapper.getExtCostByOrd(pkCnords);
    }

    /**
     * 药品附加费用执行--保存新增费用
     * 022006006024
     * @param param
     * @param user
     */
    public void saveFeeRow(String param, IUser user) {
        List<ExtCostOccVo> newItem = JsonUtil.readValue(param, new TypeReference<List<ExtCostOccVo>>() {
        });
        List<BlPubParamVo> params = new ArrayList<BlPubParamVo>();
        for (ExtCostOccVo item : newItem) {
            params.add(constructBlParam(item));
        }
        if (params.size() > 0) {
            opCgPubService.blOpCgBatch(params);
        }
    }

    private BlPubParamVo constructBlParam(ExtCostOccVo dt) {

        // 添加至需记费参数集合
        BlPubParamVo dtparam = new BlPubParamVo();
        dtparam.setBatchNo(dt.getBatchNo());
        dtparam.setDateExpire(dt.getDateExpire());
        dtparam.setDateHap(new Date());
        dtparam.setEuAdditem("1");
        dtparam.setEuPvType("1");// 就诊类型默认门诊
        dtparam.setFlagPd(dt.getFlagPd());
        dtparam.setFlagPv("0");// 非挂号费
        dtparam.setNameEmpApp(dt.getNameEmpApp());// 由前台传入，避免覆盖原始用法附加费的开立人
        dtparam.setNameEmpCg(dt.getNameEmpCg());
        dtparam.setPkOrgApp(dt.getPkOrgApp());
        dtparam.setPkEmpApp(dt.getPkEmpApp());
        dtparam.setPkEmpCg(dt.getPkEmpCg());

        dtparam.setNamePd(dt.getNameCg());
        dtparam.setPackSize(dt.getPackSize());
        dtparam.setPkCnord(dt.getPkCnord());
        dtparam.setPkDeptApp(dt.getPkDeptApp());
        dtparam.setPkDeptCg(dt.getPkDeptCg());
        dtparam.setPkDeptEx(dt.getPkDeptEx());
        dtparam.setPkEmpApp(dt.getPkEmpApp());
        dtparam.setPkEmpCg(dt.getPkEmpCg());
        if (BlcgUtil.converToTrueOrFalse(dt.getFlagPd())) // 药品
            dtparam.setPkOrd(dt.getPkItem());
        dtparam.setPkItem(dt.getPkItem());
        dtparam.setPkOrg(UserContext.getUser().getPkOrg());
        dtparam.setPkOrgApp(UserContext.getUser().getPkOrg());
        dtparam.setPkOrgEx(UserContext.getUser().getPkOrg());
        dtparam.setPkPi(dt.getPkPi());
        dtparam.setPkPres(dt.getPkPres());
        dtparam.setPkPv(dt.getPkPv());
        dtparam.setPkUnitPd(dt.getPkUnitPd());
        dtparam.setPrice(dt.getPrice());
        dtparam.setPriceCost(dt.getPriceCost());
        dtparam.setQuanCg(dt.getQuanCg());
        dtparam.setSpec(dt.getSpec());
        dtparam.setFlagHasPdPrice("1");
        dtparam.setPkDeptJob(dt.getPkDeptJob());
        dtparam.setPkDeptAreaapp(dtparam.getPkDeptAreaapp());
        return dtparam;
    }

    /**
     * 药品附加费用执行--删除未缴费用
     * 022006006025
     */
    public void delFeeRow(String param, IUser user) {
        String pkCgop = JsonUtil.getFieldValue(param, "pkCgop");
        int cnt = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cgop=? and flag_settle='0' ", Integer.class, new Object[]{pkCgop});
        if (cnt > 0) {
            DataBaseHelper.execute("delete from bl_op_dt where pk_cgop=? ", new Object[]{pkCgop});
        }
    }

    /**
     * 药品附加费用执行--执行附加费
     * 022006006026
     * @param param
     * @param user
     */
    public void execFee(String param, IUser user) {
        List<ExtCostOccVo> costOccVos = JsonUtil.readValue(param, new TypeReference<List<ExtCostOccVo>>() {
        });
        for (ExtCostOccVo occ : costOccVos) {
            if (StringUtils.isNotBlank(occ.getPkCgopocc())) {
                ExCgopOcc cgopOcc = new ExCgopOcc();
                ApplicationUtils.copyProperties(cgopOcc, occ);
                ApplicationUtils.setDefaultValue(cgopOcc, false);
                cgopOcc.setDateOcc(new Date());
                cgopOcc.setPkEmpOcc(UserContext.getUser().getPkEmp());
                cgopOcc.setNameEmpOcc(UserContext.getUser().getNameEmp());
                cgopOcc.setPkDeptOcc(UserContext.getUser().getPkDept());
                cgopOcc.setEuStatus("1");

                DataBaseHelper.update("update ex_cgop_occ set " +
                        "date_occ=:dateOcc,pk_emp_occ=:pkEmpOcc,name_emp_occ=:nameEmpOcc,pk_dept_occ=:pkDeptOcc," +
                        "eu_status=:euStatus,modifier=:modifier,modity_time=:modityTime,ts=:ts where pk_cgopocc=:pkCgopocc", cgopOcc);
            }else {
                ExCgopOcc cgopOcc = new ExCgopOcc();
                ApplicationUtils.setDefaultValue(cgopOcc,true);
                cgopOcc.setPkCnord(occ.getPkCnord());
                cgopOcc.setPkPv(occ.getPkPv());
                cgopOcc.setPkOpcg(occ.getPkCgop());
                cgopOcc.setEuType(occ.getEuType());
                cgopOcc.setPkDeptOcc(UserContext.getUser().getPkDept());
                cgopOcc.setDateOcc(new Date());
                cgopOcc.setPkEmpOcc(UserContext.getUser().getPkEmp());
                cgopOcc.setNameEmpOcc(UserContext.getUser().getNameEmp());
                cgopOcc.setEuStatus("1");
                DataBaseHelper.insertBean(cgopOcc);
            }
        }
    }

    /**
     * 药品附加费用执行--取消执行附加费
     * 022006006027
     * @param param
     * @param user
     */
    public void cancelExec(String param, IUser user) {
        List<ExtCostOccVo> cancelOccVos = JsonUtil.readValue(param, new TypeReference<List<ExtCostOccVo>>() {
        });
        for (ExtCostOccVo occ : cancelOccVos) {
            DataBaseHelper.update("update ex_cgop_occ set eu_status='9',date_canc=?,pk_emp_canc=?,name_emp_canc=?,modifier=?,modity_time=?," +
                            "date_occ=null,pk_dept_occ=null,pk_emp_occ=null,name_emp_occ=null where pk_cgopocc=? ",
                    new Object[]{new Date(),UserContext.getUser().getPkEmp(),UserContext.getUser().getNameEmp(),UserContext.getUser().getPkEmp(),new Date(),occ.getPkCgopocc()});
        }
    }

}
