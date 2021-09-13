package com.zebone.nhis.webservice.zhongshan.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.zebone.nhis.bl.pub.service.BlIpMedicalExeService;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.zhongshan.dao.MedMapper;
import com.zebone.nhis.webservice.zhongshan.vo.CheckType;
import com.zebone.nhis.webservice.zhongshan.vo.CnRisApplyVo;
import com.zebone.nhis.webservice.zhongshan.vo.PacsApplyRecordRisVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RISService {

    @Resource
    private MedMapper medMapper;
    @Resource
    private MtsService mtsService;//记录流水操作人
    @Resource
    private BlIpMedicalExeService blIpMedExSer;

    private Logger logger = LoggerFactory.getLogger("nhis.zsbaWebSrv");//日志

    /**
     * 1.1 查询【其他数据库】中的RIS申请单(pacs除去心电)
     *
     * @param param
     * @return
     */
    public List<PacsApplyRecordRisVo> queryRisAppList(List<PacsApplyRecordRisVo> rsList, String sendType) {
        List<PacsApplyRecordRisVo> list = new ArrayList<PacsApplyRecordRisVo>();// 查询的申请单列表结果集
        String curdate = DateUtils.dateToStr("yyyy-MM-dd", new Date());
        String checkTypeOfRis = ApplicationUtils.getPropertyValue("ris01.checkTypeOfPacs", "");
        if (!CommonUtils.isEmptyString(checkTypeOfRis)) {
            checkTypeOfRis = checkTypeOfRis.replace(",", "','");
        }
        String qrySql = "select app.*,dbo.ComputeAge(app.birthday,?) as age, 'O' as sys_flag from pacs_apply_record app "
                + "where app.accept_flag = '0' and app.check_type in ('" + checkTypeOfRis + "') ";
        if ("1".equals(sendType)) //HIS-门诊
        {
            qrySql += " and app.send_type ='" + sendType + "'"
                    + " and exists(select 1 from mz_visit_table mz"
                    + " where mz.patient_id = app.patient_id and mz.times = app.times "
                    + " and mz.visit_date > getdate() - 2)";//正式时，这个条件不应添加
        } else if ("2".equals(sendType)) //HIS-住院
        {
            qrySql += " and app.send_type ='" + sendType + "'"
                    + " and exists( select 1 from zy_actpatient zy "
                    + " where zy.patient_id  = app.patient_id and zy.admiss_times = app.times )";
        }
        list = DataBaseHelper.queryForList(qrySql, PacsApplyRecordRisVo.class, curdate);
        rsList = transApp(rsList, list, sendType);
        return rsList;
    }

    /**
     * 1.2 查询【NHIS库】中的RIS申请单(pacs除去心电)
     *
     * @param param
     * @return
     */
    public List<PacsApplyRecordRisVo> queryRisAppListFromNHIS(Map<String, Object> param) {
        String curdate = DateUtils.getDate("yyyyMMddHHmmss");
        param.put("curdate", curdate);
        //2020-03-09 处理查询NHIS系统中，能传入pacs系统的数据
        String checkTypeOfRis = ApplicationUtils.getPropertyValue("ris01.checkTypeOfPacs", "");
        if (!CommonUtils.isEmptyString(checkTypeOfRis)) {
            String[] checkTypes = checkTypeOfRis.split(",");
            param.put("checkTypeOfRis", checkTypes);
        }
        List<PacsApplyRecordRisVo> list = medMapper.queryRisAppFromNHIS(param);
        return list;
    }

    /**
     * 1.3 转换返回的数据字段【sendType】
     *
     * @param rsList
     * @param list
     * @param sendType
     * @return
     */
    public List<PacsApplyRecordRisVo> transApp(List<PacsApplyRecordRisVo> rsList, List<PacsApplyRecordRisVo> list, String sendType) {
        if (list == null || list.size() < 1) return rsList;
        for (PacsApplyRecordRisVo app : list) {
            if (!CommonUtils.isEmptyString(sendType))
                app.setSendType(sendType);
            rsList.add(app);
        }
        return rsList;
    }

    /**
     * 1.4 从HIS 库中查询 检查项目类型 对应的检查类型
     *
     * @return
     */
    public List<CheckType> getCheckTypeFromHis() {
        List<CheckType> list = new ArrayList<CheckType>();
        Map<String, Object> param = new HashMap<String, Object>();
        list = DataBaseHelper.queryForList(" select code , interface_code , flag from jc_interface_contract", CheckType.class, param);
        return list;
    }

    /**
     * 1.5 查询【检验病理库】中的RIS申请单(pacs除去心电)
     *
     * @param param
     * @return
     */
    public List<PacsApplyRecordRisVo> queryRisFromLisAppList(List<PacsApplyRecordRisVo> rsList, String sendType) {
        List<PacsApplyRecordRisVo> list = new ArrayList<PacsApplyRecordRisVo>();// 查询的申请单列表结果集
        String curdate = DateUtils.dateToStr("yyyy-MM-dd", new Date());
        String checkTypeOfRis = ApplicationUtils.getPropertyValue("ris01.checkTypeOfPacs", "");
        if (!CommonUtils.isEmptyString(checkTypeOfRis)) {
            checkTypeOfRis = checkTypeOfRis.replace(",", "','");
        }
        String qrySql = "select app.*"
        		+ " , dbo.ComputeAge(app.birthday,?) as age "
        		+ " , case when uid like 'CN%' then 'N' else null end sys_flag"
        		+ " from pacs_apply_record app "
                + "where app.accept_flag = '0' and app.check_type in ('" + checkTypeOfRis + "') ";
        list = DataBaseHelper.queryForList(qrySql, PacsApplyRecordRisVo.class, curdate);
        rsList = transApp(rsList, list, null);
        return rsList;
    }

    /**
     * 2.1 根据传入的 operType判断更新其他库的字段
     *
     * @param operType
     * @return sql
     */
    public int updateRisApp(Map<String, Object> param) {
        String operType = param.get("ope_type").toString();
        String sql = "update pacs_apply_record set ";
        switch (operType) {
            case "1"://ope_type=1 => 接收 accept_flag=1
                sql += " accept_flag = '1' ";
                break;
            case "2"://ope_type=2 => 登记状态，目前未处理
                return 1;
            case "3"://ope_type=3 => 更新报告 report_flag=1
                sql += " report_flag = '1' ";
                break;
            case "4"://ope_type=4 => 预约
                return 1;
            case "5"://ope_type=5 => 取消预约【2020-03-09 新增】
                return 1;
            case "6"://ope_type=6 => 取消登记【2020-03-09 新增】
                return 1;
            case "7"://ope_type=7 => 上机【2020-03-09 新增】
                return 1;
            case "9"://ope_type=4 => 撤销
                return 1;
            default:
                break;
        }
        sql += " where 1 = 1 and patient_id is not null";
        if (CommonUtils.isNotNull(param.get("record_sn")) && !CommonUtils.isEmptyString(param.get("record_sn").toString())) {
            sql += " and record_sn =:record_sn";
        }
        if (CommonUtils.isNotNull(param.get("uid")) && !CommonUtils.isEmptyString(param.get("uid").toString())) {
            sql += " and uid =:uid";
        }
        return DataBaseHelper.update(sql, param);
    }

    /**
     * 2.2 更新【NHIS库】的RIS申请单的状态
     *
     * @param param
     * @return
     */
    public int updateNhisRisApp(Map<String, Object> param) {
        BdOuDept dept = DataBaseHelper.queryForBean(" select * from bd_ou_dept where code_dept = ? ", BdOuDept.class, param.get("exec_dept"));
        if (dept == null) {
            throw new BusException(" 未查询到当前执行科室，回写状态失败！");
        }
        BdOuEmployee emp = DataBaseHelper.queryForBean(" select * from bd_ou_employee where code_emp = ? ", BdOuEmployee.class, param.get("opera"));
        if (emp == null) {
            throw new BusException(" 未查询到当前操作人，回写状态失败！");
        }
        User user = new User();
        user.setPkOrg(dept.getPkOrg());
        user.setPkDept(dept.getPkDept());
        user.setNameEmp(CommonUtils.getString(param.get("opera_name"), emp.getNameEmp()));
        user.setPkEmp(emp.getPkEmp());
        user.setCodeEmp(emp.getCodeEmp());
        UserContext.setUser(user);
        int resultCount = 0;
        String opeType = param.get("ope_type").toString();
        String uid = param.get("uid") == null ? null : param.get("uid").toString();
        param.put("pk_emp", emp.getPkEmp());//设置当前操作人
        param.put("ts", new Date());//设置当前操作时间
        String appTpe = "";
        String appName = "";
        String sql = "update cn_ris_apply set modifier =:pk_emp, modity_time =:ts, ts =:ts ";
        //1、根据操作编码，更新cn_ris_apply 的状态 eu_status
        switch (opeType) {
            case "1":// (接收) => flag_print2=1
                sql += " ,flag_print2 = (case flag_print2 when '0' then '1' when '2' then '3' else flag_print2 end) ";
                break;
            case "2":// (登记  + 记费) => eu_status=3;
                appTpe = "P1";
                appName = "登记";
                sql += " ,eu_status ='3' ";
                //非检验病理项目，仅记录操作日志即可
                if (!CommonUtils.isEmptyString(uid) && !uid.contains("CNLIS")) {
                    ipEx(param, user);//医技执行 + 记费
                }
                break;
            case "3":// (报告 + 更新结果) => eu_status=4
                sql += " ,eu_status ='4' ";
                resultCount = saveRisResult(param);//插入ex_ris_occ的报告结果result_sub、code_rpt/ex_lab_occ的检验结果 val、code_rpt;
                break;
            case "4":// (预约 + 更新相关参数) => eu_status=2
                appTpe = "P0";
                appName = "预约";
                sql += " ,eu_status ='2',date_exam =:date_exam, date_appo =:ope_time, name_emp_appo =:opera_name, pk_emp_appo =:pk_emp";
                break;
            case "5":// (取消预约 + 更新相关参数),仅预约状态可取消 => eu_status=1【2020-03-09 新增】
                appTpe = "P7";
                appName = "取消预约";
                sql += " ,eu_status ='1',date_exam = null, date_appo = null, name_emp_appo = null,pk_emp_appo = null";
                break;
            case "6":// (取消登记  + 退费),仅登记状态可撤销 => eu_status=1
                appTpe = "P3";
                appName = "取消登记";
                sql += " ,eu_status ='1' ";
                cancelIpExTem(param, user);// 取消执行 + 退费+把执行单修改为未执行状态
                break;
            case "9":// (撤销登记  + 退费),仅登记状态可撤销 => eu_status=0
                sql += " ,eu_status ='0' ";
                cancelIpEx(param, user);// 取消执行 + 退费
                break;
            default:
                break;
        }
        if (!CommonUtils.isEmptyString(appTpe)) {
            //插入操作日志
            addOpeRecord(param, user, "PACS", appTpe, appName , "3");
        }
        if (!CommonUtils.isEmptyString(uid) && !uid.contains("CNLIS"))
            resultCount = DataBaseHelper.update(sql + " where pk_cnord =:pkCnord ", param);
        else
            return 1;
        return resultCount;
    }

    /**
     * 2.2 更新【NHIS库-门诊】的RIS申请单的状态
     *
     * @param param
     * @return
     */
    public int updateOpNhisRisApp(Map<String, Object> param) {
        BdOuDept dept = DataBaseHelper.queryForBean(" select * from bd_ou_dept where code_dept = ? ", BdOuDept.class, param.get("exec_dept"));
        if (dept == null) {
            throw new BusException(" 未查询到当前执行科室，回写状态失败！");
        }
        BdOuEmployee emp = DataBaseHelper.queryForBean(" select * from bd_ou_employee where code_emp = ? ", BdOuEmployee.class, param.get("opera"));
        if (emp == null) {
            throw new BusException(" 未查询到当前操作人，回写状态失败！");
        }
        User user = new User();
        user.setPkOrg(dept.getPkOrg());
        user.setPkDept(dept.getPkDept());
        user.setNameEmp(CommonUtils.getString(param.get("opera_name"), emp.getNameEmp()));
        user.setPkEmp(emp.getPkEmp());
        user.setCodeEmp(emp.getCodeEmp());
        UserContext.setUser(user);
        int resultCount = 0;
        String opeType = param.get("ope_type").toString();
        String uid = param.get("uid") == null ? null : param.get("uid").toString();
        param.put("pk_emp", emp.getPkEmp());//设置当前操作人
        param.put("ts", new Date());//设置当前操作时间
        String appTpe = "";
        String appName = "";
        String sql = "update cn_ris_apply set modifier =:pk_emp, modity_time =:ts, ts =:ts ";
        //1、根据操作编码，更新cn_ris_apply 的状态 eu_status
        switch (opeType) {
            case "1":// (接收) => flag_print2=1
                sql += " ,flag_print2 = (case flag_print2 when '0' then '1' when '2' then '3' else flag_print2 end) ";
                break;
            case "2":// (登记  + 记费) => eu_status=3;
                appTpe = "P1";
                appName = "登记";
                sql += " ,eu_status ='3' ";
                //非检验病理项目，仅记录操作日志即可
                if (!CommonUtils.isEmptyString(uid) && !uid.contains("CNLIS")) {
                    opEx(param, user);//医技执行 + 记费
                }
                break;
            case "3":// (报告 + 更新结果) => eu_status=4
                sql += " ,eu_status ='4' ";
                resultCount = saveRisResult(param);//插入ex_ris_occ的报告结果result_sub、code_rpt/ex_lab_occ的检验结果 val、code_rpt;
                break;
            case "4":// (预约 + 更新相关参数) => eu_status=2
                appTpe = "P0";
                appName = "预约";
                sql += " ,eu_status ='2',date_exam =:date_exam, date_appo =:ope_time, name_emp_appo =:opera_name, pk_emp_appo =:pk_emp";
                break;
            case "5":// (取消预约 + 更新相关参数),仅预约状态可取消 => eu_status=1【2020-03-09 新增】
                appTpe = "P7";
                appName = "取消预约";
                sql += " ,eu_status ='1',date_exam = null, date_appo = null, name_emp_appo = null,pk_emp_appo = null";
                break;
            case "6":// (取消登记  + 退费),仅登记状态可撤销 => eu_status=1
                appTpe = "P3";
                appName = "取消登记";
                sql += " ,eu_status ='1' ";
                cancelOpExTem(param, user);// 取消执行 + 退费+把执行单修改为未执行状态
                break;
            case "9":// (撤销登记 ),仅登记状态可撤销 => eu_status=0
                sql += " ,eu_status ='0' ";
                cancelOpExTem(param, user);// 取消执行 + 退费
                break;
            default:
                break;
        }
        if (!CommonUtils.isEmptyString(appTpe)) {
            //插入操作日志
            addOpeRecord(param, user, "PACS", appTpe, appName , "1");
        }
        if (!CommonUtils.isEmptyString(uid) && !uid.contains("CNLIS"))
            resultCount = DataBaseHelper.update(sql + " where pk_cnord =:pkCnord ", param);
        else
        {return 1;}
        return resultCount;
    }
    
    /**
     * 2.3 根据传入的 operType判断更新其他库的字段
     *
     * @param operType
     * @return sql
     */
    public int updateTjNewRisApp(Map<String, Object> param) {
        String operType = param.get("ope_type").toString();
        String sql = "update hms_reg_checkapply ";
        switch (operType) {
            case "1"://SENDFALG=1 => 接收 -- 已经取走更新成1
                sql += " set SENDFALG='1' ";
                break;
            case "2"://ope_type=2 => 登记状态，目前未处理
                return 1;
            case "3"://ope_type=3 => 更新报告 report_flag=1
                sql += " set ISRPORT = '1' ";
                break;
            case "4"://ope_type=4 => 预约
                return 1;
            case "5"://ope_type=5 => 取消预约【2020-03-09 新增】
                return 1;
            case "6"://ope_type=6 => 取消登记【2020-03-09 新增】
                return 1;
            case "7"://ope_type=7 => 上机【2020-03-09 新增】
                return 1;
            case "9"://ope_type=4 => 撤销
                return 1;
            default:
                break;
        }
        sql += " where 1 = 1 ";
        if (CommonUtils.isNotNull(param.get("record_sn")) && !CommonUtils.isEmptyString(param.get("record_sn").toString())) {
            sql += " and applyid =:record_sn";
        }
        if (CommonUtils.isNotNull(param.get("uid")) && !CommonUtils.isEmptyString(param.get("uid").toString())) {
            sql += " and applyid =:uid";
        }
        return DataBaseHelper.update(sql, param);
    }

    /**
     * 医技执行
     * 根据医嘱主键，组装记费所需VO ，调用 记费（更新执行单 + 记费）
     *
     * @param param
     */
    protected void ipEx(Map<String, Object> param, User user) {
        //1.校验操作时间是否有误
        String dateOcc = param.get("ope_time").toString();
        Date dateEx = new Date();
        try {
            dateEx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateOcc);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusException(" 操作时间格式不对，登记失败！");
        }
        //查询医技执行单信息
        List<Map<String, Object>> list = medMapper.queryOrdRis(param);
        if (list == null && list.size() == 0) {
            logger.info("未找到医嘱" + JsonUtil.writeValueAsString(param));
            throw new BusException(" 未查询到相应医嘱:" + param.get("uid") + "，登记失败！");
        }
        //进行收费明细查询
        Map<String, Object> map = null;
        //组装查询收费明细入参
        List<Map<String, Object>> mapList = new ArrayList<>(16);
        // todo 转换医技执行单信息，因为塞值的时候大小写不敏感，暂时这么处理，找出优化方法再优化
        List<Map<String, Object>> list1 = new ArrayList<>(16);
        for (Map<String, Object> map1 : list) {
            map = new HashMap<>(1);
            map.put("pkCnord", map1.get("pkCnord"));
            map.put("pkExocc", map1.get("pkExocc"));
            map.put("charge", map1.get("charge"));
            map.put("isFather", map1.get("isFather"));
            mapList.add(map);
            map = new HashMap<>(16);
            map.put("dateOcc", new Date());
            map.putAll(map1);
            list1.add(map);
        }
        //查询计费信息
        ApplicationUtils apputil = new ApplicationUtils();
        UserContext.setUser(user);
        ResponseJson rs = apputil.execService("BL", "BlMedicalExe2Service", "queryIpBlDtAndOcc", mapList, user);
        Map<String, Object> blMap = (Map<String, Object>) rs.getData();
        //计费+执行计划：故未计费费用则计费，费用已计费则执行计划
        List<Map<String, Object>> dtlist = new ArrayList<>(16);
        if (rs.getStatus() != 0 || blMap == null || blMap.containsKey("dtlist")) {
            //费用明细
            for (Map<String, Object> map1 : (List<Map<String, Object>>) blMap.get("dtlist")) {
                if (map1.containsKey("flagSettle") || "0".equals(map1.get("flagSettle"))) {
                    continue;
                } else {
                    Map<String, Object> map2 = new HashMap<>(16);
                    map2.put("euPvType", "3");
                    map2.put("quanCg", map1.get("quancg"));
                    map2.putAll(map1);
                    dtlist.add(map2);
                }
            }
        }
        if (dtlist.size() == 0) {
            //已计费执行
            excOcc(blMap, user);
        } else {
            //计费执行
            List<Map<String, Object>> mapList1 = new ArrayList<>(8);
            for (int i = 0; i < list1.size(); i++) {
                String pkCnord = String.valueOf(list1.get(i).get("pkCnord"));
                Map<String, Object> cnMap = new HashMap<>(2);
                cnMap.put("exOrderOcc", list1.get(i));
                List<Map<String, Object>> mapList2 = new ArrayList<>(8);
                for (Map<String, Object> map1 : dtlist) {
                    if (pkCnord.equals(map1.get("pkCnord"))) {
                        mapList2.add(map1);
                    }
                }
                cnMap.put("dtlist", mapList2);
                mapList1.add(cnMap);
            }
            ResponseJson rs1 = apputil.execService("BL", "BlMedicalExe2Service", "savePatiCgInfo_refactor", mapList1, user);
            if (rs1.getStatus() != 0) {
                logger.info("计费失败：" + JsonUtil.writeValueAsString(mapList1) + rs.getDesc());
                throw new BusException(" 计费失败，登记失败！" + rs1.getDesc());
            }
            //执行
            ResponseJson rsb = apputil.execService("BL", "BlMedicalExe2Service", "queryIpBlDtAndOcc", mapList, user);
            if (rs1.getStatus() != 0) {
                logger.info("计费后查询执行单失败：" + JsonUtil.writeValueAsString(mapList1) + rs.getDesc());
                throw new BusException(" 计费后查询执行单失败，登记失败！ " + rs1.getDesc());
            }
            Map<String, Object> occMap = (Map<String, Object>) rsb.getData();
            excOcc(occMap, user);
        }
        //将医嘱执行科室、执行单执行科室、记费科室 回置为医嘱开立的执行科室【以解决pacs系统调用接口时，传入的科室非实际执行科室】
        if (null != list && list.size() > 0)
            for (Map<String, Object> map2 : list) {
                DataBaseHelper.update("UPDATE CN_ORDER SET PK_DEPT_EXEC =:pkDeptExec WHERE PK_CNORD =:pkCnord  and PK_DEPT_EXEC !=:pkDeptExec ", map2);
                DataBaseHelper.update("UPDATE EX_ORDER_OCC SET PK_DEPT_OCC =:pkDeptExec WHERE PK_CNORD =:pkCnord and PK_DEPT_OCC !=:pkDeptExec ", map2);
                DataBaseHelper.update("UPDATE BL_IP_DT SET PK_DEPT_CG = PK_DEPT_EX WHERE PK_CNORD =:pkCnord and PK_DEPT_CG != PK_DEPT_EX", map2);
            }
    }

    /**
     * 医技执行-门诊
     * 根据医嘱主键，进行确认
     *
     * @param param
     */
    protected void opEx(Map<String, Object> param, User user) {
        //1.校验操作时间是否有误
        String dateOcc = param.get("ope_time").toString();
        Date dateEx = new Date();
        try {
            dateEx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateOcc);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusException(" 操作时间格式不对，登记失败！");
        }
        param.put("dateOcc",dateOcc);
        param.put("pkEmpOcc",user.getPkEmp());
        param.put("nameEmpOcc",user.getNameEmp());
        param.put("pkOrgOcc",user.getPkOrg());
        param.put("pkDeptOcc",user.getPkDept());
        medMapper.updateExAssOccE(param);
        DataBaseHelper.execute("update cn_lab_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{param.get("pkCnord").toString()});
    }

    /***
     * @Description 掉用系统原有医技执行方法
     * @auther wuqiang
     * @Date 2020-03-04
     * @Param [list, user]
     * @return void
     */
    protected void excOcc(Map<String, Object> map, User user) {
        List<Map<String, Object>> occlist1 = (List<Map<String, Object>>) map.get("occlist");
        List<Map<String, Object>> occlist = new ArrayList<>(16);
        Map<String, Object> map1 = null;
        for (int i = occlist1.size() - 1; i >= 0; i--) {
            if (occlist1.get(i).containsKey("flagOcc") && "1".equals(occlist1.get(i).get("flagOcc"))) {
                occlist1.remove(i);
            } else {
                map1 = new HashMap<>(5);
                map1.put("pkEmpOcc", user.getPkEmp());
                map1.put("nameEmpOcc", user.getNameEmp());
                map1.put("euPvtype", "3");
                map1.put("dateOcc", null);
                map1.putAll(occlist1.get(i));
                occlist.add(map1);
            }
        }
        if (occlist.size() > 0) {
            ApplicationUtils apputil = new ApplicationUtils();
            ResponseJson rs = apputil.execService("BL", "BlMedicalExe2Service", "excList", occlist, user);
            if (rs.getStatus() != 0) {
                logger.info("执行失败：" + JsonUtil.writeValueAsString(occlist) + rs.getDesc());
                throw new BusException(" 执行失败，登记失败！ " + rs.getDesc());
            }
        } else {
            logger.info("项目已计费执行，登记失败！请勿重复登记：");
            throw new BusException(" 项目已计费执行，登记失败！请勿重复登记");
        }
    }


    /**
     * 取消医技执行
     * 根据医嘱主键，查询医技执行记录、执行单，调用 退费（更新执行记录，执行单  + 记费）
     *
     * @param param
     * @param user
     */
    private void cancelIpEx(Map<String, Object> param, User user) {
        //1.查询相应执行单
        ExOrderOcc exOrdOcc = DataBaseHelper.queryForBean(" select * from ex_order_occ where pk_cnord = ? ", ExOrderOcc.class, param.get("pkCnord"));
        if (exOrdOcc == null)
            throw new BusException(" 未查询到相应执行单，撤销登记失败！");
        //2.未执行||取消执行 则直接取消执行 + 回置申请单状态[2020-04-26]
        if ("0".equals(exOrdOcc.getEuStatus()) || "9".equals(exOrdOcc.getEuStatus())) {
            String dateTxt = DateUtils.getDateTime();
            int cnt = DataBaseHelper.execute("update ex_order_occ set flag_canc=1,"
                    + " date_canc=to_date('" + dateTxt + "','yyyy-MM-dd HH24:mi:ss'), pk_emp_canc=?,name_emp_canc=?,pk_dept_canc=?,eu_status=9, "
                    + " modifier=? , MODITY_TIME=to_date('" + dateTxt + "','yyyy-MM-dd HH24:mi:ss') , ts=to_date('" + dateTxt + "','yyyy-MM-dd HH24:mi:ss')" +
                    " where pk_exocc=?  and eu_status in ('0','9')", user.getPkEmp(), user.getNameEmp(), user.getPkDept(), user.getPkEmp(), exOrdOcc.getPkExocc());
            if (cnt < 1)
                throw new BusException(" 未查询到可撤销的医技执行记录，撤销登记失败！");
            return;
        }
        //3.查询 需退费的执行记录
        ExAssistOcc exAssOcc = DataBaseHelper.queryForBean(" select * from ex_assist_occ where pk_cnord = ?  ", ExAssistOcc.class, param.get("pkCnord"));
        if (exAssOcc == null)
            throw new BusException(" 未查询到可撤销的医技执行记录，撤销登记失败！");
        //4.更新医技执行记录
        Object[] args = new Object[4];
        args[0] = new Date();
        args[1] = UserContext.getUser().getPkEmp();
        args[2] = UserContext.getUser().getNameEmp();
        args[3] = exAssOcc.getPkAssocc();
        DataBaseHelper.execute("update ex_assist_occ  set flag_canc=1,date_canc=?," +
                "pk_emp_canc=?,name_emp_canc=?,eu_status=9" +
                " where pk_assocc=? and flag_canc=0", args);
        args[3] = UserContext.getUser().getPkDept();
        //5.退费
        blIpMedExSer.retCg(exOrdOcc.getPkExocc(), user, args, "0");
    }

    /**
     * 取消医技执行-博爱项目临时增加
     * 根据医嘱主键，查询医技执行记录、执行单，调用 退费（更新执行记录，执行单  + 记费）
     *
     * @param param
     * @param user
     */
    protected void cancelIpExTem(Map<String, Object> param, User user) {
        //1.查询 需退费的执行记录
        ExAssistOcc exAssOcc = DataBaseHelper.queryForBean(" select * from ex_assist_occ where pk_cnord = ? ", ExAssistOcc.class, param.get("pkCnord"));
        if (exAssOcc == null)
            throw new BusException(" 未查询到可撤销的医技执行记录，撤销登记失败！");
        //2.查询相应执行单
        ExOrderOcc exOrdOcc = DataBaseHelper.queryForBean(" select * from ex_order_occ where pk_cnord = ? ", ExOrderOcc.class, param.get("pkCnord"));
        if (exOrdOcc == null)
            throw new BusException(" 未查询到相应执行单，撤销登记失败！");
        //3.更新医技执行记录
        Object[] args = new Object[4];
        args[0] = new Date();
        args[1] = UserContext.getUser().getPkEmp();
        args[2] = UserContext.getUser().getNameEmp();
        args[3] = exAssOcc.getPkAssocc();
        //4.退费
        blIpMedExSer.retCgTem(exOrdOcc.getPkExocc(), user, args, "0");
    }
    /**
     * 门诊取消医技执行
     *
     * @param param
     * @param user
     */
    protected void cancelOpExTem(Map<String, Object> param, User user) {
    	//1.校验操作时间是否有误
        String dateOcc = param.get("ope_time").toString();
        Date dateEx = new Date();
        try {
            dateEx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateOcc);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusException(" 操作时间格式不对，登记失败！");
        }
        //2.查询 需取消的执行记录
        ExAssistOcc exAssOcc = DataBaseHelper.queryForBean(" select PK_ASSOCC  from ex_assist_occ where pk_cnord = ? ", ExAssistOcc.class, param.get("pkCnord"));
        if (exAssOcc == null)
        {throw new BusException(" 未查询到可撤销的医技执行记录，撤销登记失败！");}
        param.put("pkAssOcc",exAssOcc.getPkAssocc());
        param.put("dateCanc", dateOcc);
        param.put("pkEmpCanc",user.getPkEmp());
        param.put("nameEmpCanc",user.getNameEmp());
        medMapper.cancelOpExtem(param);
    }

    /**
     * 更新/插入 检查结果
     */
    private int saveRisResult(Map<String, Object> param) {
        int cnt = 0;
        //1.校验操作时间是否有误
        String dateOcc = param.get("ope_time").toString();
        Date dateEx = new Date();
        try {
            dateEx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateOcc);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusException(" 操作时间格式不对，登记失败！");
        }
        //2.查询 医嘱
        CnOrder ord = DataBaseHelper.queryForBean(" select * from cn_order where pk_cnord = ? ", CnOrder.class, param.get("pkCnord"));
        if (ord == null)
            throw new BusException(" 未查询到相应医嘱，更新结果失败！");
        if (param.get("rpt_id") == null || param.get("rpt_id").equals("")) {
            throw new BusException("报告审核时，报告单号不能为空！");
        }
        String uid = param.get("uid") == null ? null : param.get("uid").toString();
        if (CommonUtils.isNotNull(uid) && uid.contains("CNLIS"))
            cnt = updateLisRpt(param, dateEx, ord);//处理检验病理类型的医嘱
        else
            updateRisRpt(param, dateEx, ord);//处理原流程
        return cnt;
    }

    /**
     * 更新检查报告
     *
     * @param param
     * @param dateEx
     * @param ord
     */
    private void updateRisRpt(Map<String, Object> param, Date dateEx, CnOrder ord) {
        //3.查询 检查结果
        ExRisOcc exRisOcc = DataBaseHelper.queryForBean(" select * from ex_ris_occ where pk_cnord = ? and del_flag = '0' ", ExRisOcc.class, param.get("pkCnord"));
        if (exRisOcc != null) {
            DataBaseHelper.update("update ex_ris_occ set result_sub = ?,code_rpt=? where pk_cnord = ?", param.get("report_info"), param.get("rpt_id"), param.get("pkCnord"));
        } else {
            exRisOcc = new ExRisOcc();
            exRisOcc.setCodeApply(ord.getCodeApply());
            exRisOcc.setDateChk(dateEx);
            exRisOcc.setDateOcc(dateEx);
            exRisOcc.setDateRpt(dateEx);
            exRisOcc.setEuResult("0");
            exRisOcc.setFlagChk("1");
            exRisOcc.setDelFlag("0");
            exRisOcc.setNameEmpChk(UserContext.getUser().getNameEmp());
            exRisOcc.setNameEmpOcc(UserContext.getUser().getNameEmp());
            exRisOcc.setPkEmpChk(UserContext.getUser().getPkEmp());
            exRisOcc.setPkEmpOcc(UserContext.getUser().getPkEmp());
            exRisOcc.setPkOrg(UserContext.getUser().getPkOrg());
            exRisOcc.setPkOrgOcc(UserContext.getUser().getPkOrg());
            exRisOcc.setPkPi(ord.getPkPi());
            exRisOcc.setPkPv(ord.getPkPv());
            exRisOcc.setPkCnord(ord.getPkCnord());
            exRisOcc.setPkDeptOcc(ord.getPkDeptExec());
            //检查结果
            exRisOcc.setResultSub(param.get("report_info") == null ? null : param.get("report_info").toString());
            //申请单号
            exRisOcc.setCodeApply(param.get("uid") == null ? null : param.get("uid").toString());
            //报告单号
            exRisOcc.setCodeRpt(param.get("rpt_id") == null ? null : param.get("rpt_id").toString());
            DataBaseHelper.insertBean(exRisOcc);
        }
    }

    /**
     * 更新检验结果
     *
     * @param param
     * @param dateEx
     * @param ord
     */
    private int updateLisRpt(Map<String, Object> param, Date dateEx, CnOrder ord) {
        //更新检验申请单状态
        int cnt = DataBaseHelper.update("update cn_lab_apply set date_rpt=? ,eu_status='4' where pk_cnord = ?", param.get("ope_time"), ord.getPkCnord());
        //查询 检验结果
        ExLabOcc exLabOcc = DataBaseHelper.queryForBean(" select * from ex_lab_occ where pk_cnord=? and del_flag = '0' ", ExLabOcc.class, ord.getPkCnord());
        if (exLabOcc != null) {
            DataBaseHelper.update("update ex_lab_occ set code_rpt = ?,val = ? where pk_cnord = ?", param.get("rpt_id"), param.get("report_info"), ord.getPkCnord());
        } else {
            exLabOcc = new ExLabOcc();
            //申请单号
            exLabOcc.setCodeApply(ord.getCodeApply());
            exLabOcc.setDateChk(dateEx);
            exLabOcc.setDateOcc(dateEx);
            exLabOcc.setDateRpt(dateEx);
            exLabOcc.setEuResult("0");
            exLabOcc.setFlagChk("1");
            exLabOcc.setDelFlag("0");
            exLabOcc.setNameEmpChk(UserContext.getUser().getNameEmp());
            exLabOcc.setNameEmpOcc(UserContext.getUser().getNameEmp());
            exLabOcc.setPkEmpChk(UserContext.getUser().getPkEmp());
            exLabOcc.setPkEmpOcc(UserContext.getUser().getPkEmp());
            exLabOcc.setPkOrg(UserContext.getUser().getPkOrg());
            exLabOcc.setPkOrgOcc(UserContext.getUser().getPkOrg());
            exLabOcc.setPkPi(ord.getPkPi());
            exLabOcc.setPkPv(ord.getPkPv());
            exLabOcc.setPkCnord(ord.getPkCnord());
            exLabOcc.setPkDeptOcc(ord.getPkDeptExec());
            //检查结果
            exLabOcc.setVal(param.get("report_info") == null ? null : param.get("report_info").toString());
            //申请单号
            exLabOcc.setCodeApply(param.get("uid") == null ? null : param.get("uid").toString());
            //报告单号
            exLabOcc.setCodeRpt(param.get("rpt_id") == null ? null : param.get("rpt_id").toString());
            DataBaseHelper.insertBean(exLabOcc);
        }
        return cnt;
    }

    /**
     * 插入操作人
     *
     * @param chkParam
     * @param pv
     * @param operType 操作类型-检查：P0:预约P1:登记P2:上机P3取消登记P4:编写报告P5:审核报告P7:取消预约P8:取消申请P9:取消报告
     * @param operName
     * @param operTime
     * @param codeEmp
     */
    public void addOpeRecord(Map<String, Object> param, User u, String sysName, String operType, String operName , String pvType) {
        String qrySql = "select pi.code_pi "
        		+ ", case when pv.eu_pvtype = '3' then pi.code_ip when pv.eu_pvtype = '1' then pi.code_op else null end code"
        		+ ", case when pv.eu_pvtype = '3' then ip.ip_times  when pv.eu_pvtype = '1' then op.op_times else null end times"
                + ", ord.name_ord ord_name "
                + ", ord.code_apply code_app "
                + ", ord.pk_pv"
                + ", app.* "
                + " from cn_order ord ";
        String uid = CommonUtils.getString(param.get("uid"), "");
        if (uid.contains("LIS"))
            qrySql = qrySql + " inner join cn_lab_apply app on app.pk_cnord = ord.pk_cnord";
        else
            qrySql = qrySql + " inner join cn_ris_apply app on app.pk_cnord = ord.pk_cnord ";
        qrySql = qrySql + " inner join pi_master pi on pi.pk_pi = ord.pk_pi "
                + " inner join pv_encounter pv on pv.pk_pv = ord.pk_pv and pv.del_flag = '0'"
                + " left  join pv_ip ip on ip.pk_pv = ord.pk_pv and ip.del_flag = '0' "
                + " left  join pv_op op on op.pk_pv = ord.pk_pv and op.del_flag = '0' "
                + " where ord.del_flag = '0' and app.del_flag = '0' and ord.pk_cnord = ?";
        CnRisApplyVo risApp = DataBaseHelper.queryForBean(qrySql, CnRisApplyVo.class, new Object[]{param.get("pkCnord")});
        Map<String, Object> mtsParam = new HashMap<String, Object>();
        mtsParam.put("rec_no", risApp.getCodeApp());         //rec_no:医技系统记录号
        mtsParam.put("pk_pv", risApp.getPkPv());             //pk_pv：就诊id
        mtsParam.put("code_pi", risApp.getCodePi());         //code_pi：患者编码
        mtsParam.put("code", risApp.getCode());             //code:住院号、门诊号
        mtsParam.put("times", risApp.getTimes());       	//times:门诊、住院 就诊次数
        mtsParam.put("mts_type", sysName);                   //mts_type:医技类型(LIS:检验 PACS：检查 ECG:心电)
        mtsParam.put("oper_type", operType);                 //oper_type:操作类型-检查：P0:预约P1:登记P2:上机P3取消登记P4:编写报告P5:审核报告P7:取消预约P8:取消申请P9:取消报告P10取消预约
        mtsParam.put("oper_name", operName);                 //oper_name：操作名称
        mtsParam.put("oper_time", param.get("ope_time"));    //oper_time:操作时间
        mtsParam.put("emp_code", u.getCodeEmp());            //emp_code:人员编码
        mtsParam.put("emp_name", u.getNameEmp());            //emp_name:人员名称
        mtsParam.put("mts_name", risApp.getOrdName());       //mts_name:医技名称
        mtsParam.put("req_no", risApp.getCodeApp());         //req_no:申请单号
        mtsParam.put("pv_type", pvType);         			 //pv_type:就诊类型 1-门诊，2-急诊，3-住院
        mtsParam.put("pv_name", "1" == pvType ? "门诊" : "2" == pvType ? "急诊" : "3" == pvType ? "住院" : "");	//pv_name:就诊类型1-门诊，2-急诊，3-住院
        mtsService.updateMtsOperRec(mtsParam);
    }
    
  /**
   * @Description 查询NHIS门诊检查申请单
   * @auther wuqiang
   * @Date 2021-03-01
   * @Param [param]
   * @return java.util.List<com.zebone.nhis.webservice.zhongshan.vo.PacsApplyRecordRisVo>
   */
    public List<PacsApplyRecordRisVo> queryOpRisAppListFromNHIS(Map<String, Object> param ,List<PacsApplyRecordRisVo> rsList, String sendType ) {
    	List<PacsApplyRecordRisVo> list = new ArrayList<PacsApplyRecordRisVo>();// 查询的申请单列表结果集
    	String curdate = DateUtils.getDate("yyyyMMddHHmmss");
        param.put("curdate", curdate);
        //2020-03-09 处理查询NHIS系统中，能传入pacs系统的数据
        String checkTypeOfRis = ApplicationUtils.getPropertyValue("ris01.checkTypeOfPacs", "");
        if (!CommonUtils.isEmptyString(checkTypeOfRis)) {
            String[] checkTypes = checkTypeOfRis.split(",");
            param.put("checkTypeOfRis", checkTypes);
        }
        list = medMapper.queryOpRisAppFromNHIS(param);
        rsList = transApp(rsList, list, sendType);
        return rsList;
    }
    
}
