package com.zebone.nhis.ma.pub.platform.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.group.MFN_M01_MF;
import ca.uhn.hl7v2.model.v24.message.MFN_M01;
import ca.uhn.hl7v2.model.v24.segment.MFE;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemPrice;
import com.zebone.nhis.common.module.base.ou.*;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.scm.pub.BdFactory;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.Constant;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 说明：字典消息处理
 *
 * @author maijiaxing
 * @version 1.0
 * @date 2021/1/29
 */
@Service
public class SDBaseDataService {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    @Resource
    private SDQueryUtils sDQueryUtils;

    /**
     *   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>保存科室<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * @param mfn
     * @throws HL7Exception
     */
    public void saveDept(MFN_M01 mfn) throws HL7Exception {
        int len = mfn.getMFReps();
        MFE mfe;
        for (int i = 0; i < len; i++) {
            MFN_M01_MF mf = mfn.getMF(i);
            mfe =mf.getMFE();
            // MAD 新增   MUP修改
            String status = mfe.getRecordLevelEventCode().getValue();
            Segment Z01 =(Segment) mf.get("Z01");
            Map<String, Object> paramMap = new HashMap<>();
            //系统名字
            paramMap.put("app",mfn.getMSH().getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
            //科室编码
            paramMap.put("deptCode", Z01.getField(1, 0).encode());
            //科室名称
            String deptName = Z01.getField(2, 0).encode();
            deptName = deptName.startsWith("住院")||deptName.endsWith("住院")? StringUtils.split(deptName, "住院")[0]:deptName;
            deptName = deptName.startsWith("门诊")||deptName.endsWith("门诊")?StringUtils.split(deptName, "门诊")[0]:deptName;
            paramMap.put("deptName",deptName );
            //英文
            paramMap.put("pyCode",Z01.getField(3, 0).encode() );
            //科室简称
            String simpleName = Z01.getField(4, 0).encode();
            paramMap.put("simpleName",simpleName );
            //科室类型
            String dtDeptType = "";
            switch (Z01.getField(5, 0).encode()) {
                case "C":dtDeptType = "01";break;
                case "I":dtDeptType = "01";break;
                case "F":dtDeptType = "08";break;
                case "PI":dtDeptType = "0401";break;
                case "T":dtDeptType = "03";break;
                case "P":dtDeptType = "0402";break;
                case "N":dtDeptType = "02";break;
                case "L":dtDeptType = "0802";break;
                default:dtDeptType = "0802";break;
            }
            paramMap.put("deptType",dtDeptType);
            // pkfather
            String pkFather = "9c40cca52bf247d099bccfeba5ff66bf";
            if(dtDeptType.startsWith("01")||dtDeptType.startsWith("02")){
                //临床科室
                pkFather = "e89c6503ef084bf7b74e308b269e1811";
            }else if(dtDeptType.startsWith("03")){
                //医技科室
                pkFather = "1cf734580f3f43a4b1c365aa250501d5";
            }else if(dtDeptType.startsWith("04")){
                //药学部
                pkFather = "4ed59e8f650a46228607c46dc9f2c048";
            }else {
                //财务部
                pkFather = "9c40cca52bf247d099bccfeba5ff66bf";
            }
            paramMap.put("pkFather",pkFather );
            //是否挂号科室
            paramMap.put("regdeptFlag", Z01.getField(6, 0).encode());
            //	是否核算科室 010203
            paramMap.put("dtAcctdept",Z01.getField(7, 0).encode() );
            //特殊科室属性  0 普通, 1 手术,  2 麻醉, 3 ICU,  4 CCU, C 产科,E急诊留观,T特诊
            String dtMedicaltype = "";
            switch (Z01.getField(8, 0).encode()){
                case "0":dtMedicaltype = "01";break;
                case "1":dtMedicaltype = "02";break;
                case "2":dtMedicaltype = "02";break;
                case "3":dtMedicaltype = "2199";break;
                case "4":dtMedicaltype = "2101";break;
                case "C":dtMedicaltype = "04";break;
                case "E":dtMedicaltype = "20";break;
                case "T":dtMedicaltype = "99";break;
                default:dtMedicaltype = "99";break;
            }
            paramMap.put("dtMedicaltype", dtMedicaltype);
            //有效性标志
            String flagActive = "1".equalsIgnoreCase(Z01.getField(9, 0).encode())?"1":"0";
            paramMap.put("flagActive", flagActive);
            //操作员
            paramMap.put("creator", Z01.getField(13, 0).encode());
            //操作时间
            paramMap.put("createDate",Z01.getField(14, 0).encode());

            //保存逻辑 ！！！！
            saveBdOuDept(paramMap);
        }
    }

    /**
     * 由平台传入 - 新增科室信息
     * @param paramMap
     */
    private void saveBdOuDept(Map<String, Object> paramMap){
        BdOuDept bdOuDept = DataBaseHelper.queryForBean("select * from BD_OU_DEPT where CODE_DEPT=?", BdOuDept.class, SDMsgUtils.getPropValueStr(paramMap, "deptCode"));
        if(bdOuDept==null){
            bdOuDept = new BdOuDept();
            bdOuDept.setPkDept(SDMsgUtils.getPk());
            bdOuDept.setPkOrg(Constant.PKORG);
            bdOuDept.setCodeDept(SDMsgUtils.getPropValueStr(paramMap,"deptCode"));
            bdOuDept.setNameDept(SDMsgUtils.getPropValueStr(paramMap,"deptName"));
            bdOuDept.setShortname(SDMsgUtils.getPropValueStr(paramMap,"simpleName"));
            bdOuDept.setCreator(SDMsgUtils.getPropValueStr(paramMap,"app"));
            bdOuDept.setCreateTime(new Date());
            bdOuDept.setFlagActive(SDMsgUtils.getPropValueStr(paramMap,"flagActive"));
            bdOuDept.setDtDepttype(SDMsgUtils.getPropValueStr(paramMap,"dtDeptType"));
            bdOuDept.setPkFather(SDMsgUtils.getPropValueStr(paramMap,"pkFather"));
            bdOuDept.setEuLevel("1");
            bdOuDept.setDeptType("N");
            bdOuDept.setDelFlag("0");
            bdOuDept.setFlagEr("0");
            bdOuDept.setFlagIp("0");
            bdOuDept.setFlagPe("0");
            bdOuDept.setFlagHm("0");
            //bdOuDept.setPkOrgarea();
            //核算部门 bdOuDept.setDtAcctdept();
            //标准部门 bdOuDept.setDtStdepttype();
            bdOuDept.setDtMedicaltype(SDMsgUtils.getPropValueStr(paramMap,"dtMedicaltype"));
            bdOuDept.setPyCode(SDMsgUtils.getPropValueStr(paramMap,"pyCode"));
            bdOuDept.setdCode(SDMsgUtils.getPropValueStr(paramMap,"deptCode"));
            DataBaseHelper.insertBean(bdOuDept);
        }else {
            bdOuDept.setCodeDept(SDMsgUtils.getPropValueStr(paramMap,"deptCode"));
            bdOuDept.setNameDept(SDMsgUtils.getPropValueStr(paramMap,"deptName"));
            bdOuDept.setShortname(SDMsgUtils.getPropValueStr(paramMap,"simpleName"));
            bdOuDept.setModifier(SDMsgUtils.getPropValueStr(paramMap,"app"));
            bdOuDept.setModityTime(new Date());
            bdOuDept.setFlagActive(SDMsgUtils.getPropValueStr(paramMap,"flagActive"));
            bdOuDept.setDtDepttype(SDMsgUtils.getPropValueStr(paramMap,"dtDeptType"));
            bdOuDept.setPkFather(SDMsgUtils.getPropValueStr(paramMap,"pkFather"));
            bdOuDept.setDtMedicaltype(SDMsgUtils.getPropValueStr(paramMap,"dtMedicaltype"));
            bdOuDept.setPyCode(SDMsgUtils.getPropValueStr(paramMap,"pyCode"));
            bdOuDept.setdCode(SDMsgUtils.getPropValueStr(paramMap,"deptCode"));
            bdOuDept.setDelFlag("0");
            DataBaseHelper.updateBeanByPk(bdOuDept);
        }
    }

    /**
     * 人员逻辑删除
     * @param paramMap
     */
    private void deleteEmployee(Map<String, Object> paramMap) {
        String sql = "update BD_OU_EMPLOYEE set DEL_FLAG='1' , FLAG_ACTIVE='0' where CODE_EMP=? ";
        int execute = DataBaseHelper.execute(sql, SDMsgUtils.getPropValueStr(paramMap, "codeEmp"));
        if(execute<1){
            throw new BusException("删除人员失败！");
        }
    }
    /**
     *  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>保存人员信息<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * @param mfn
     * @throws HL7Exception
     */
    public void saveEmployee(MFN_M01 mfn) throws HL7Exception, ParseException {
        int len = mfn.getMFReps();
        MFE mfe;
        for (int i = 0; i < len; i++) {
            Map<String, Object> paramMap = new HashMap<>(16);
            MFN_M01_MF mf =mfn.getMF(i);
            mfe =mf.getMFE();
            Segment Z02 =(Segment) mf.get("Z02");
            // MAD 新增   MUP修改
            paramMap.put("status", mfe.getRecordLevelEventCode().getValue());
            //1、员工代码
            paramMap.put("codeEmp", Z02.getField(1, 0).encode());
            //2、员工姓名
            paramMap.put("nameEmp", Z02.getField(2, 0).encode());
            //3、性别  来自码表
            String dtSex = Z02.getField(3, 0).encode();
            if("F".equals(dtSex)){//女
                dtSex = "03";
            }else if("M".equals(dtSex)){
                dtSex = "02";
            }else {//未说明性别
                dtSex = "04";
            }
            paramMap.put("dtSex", dtSex);
            //BD_OU_EMPLOYEE
            //4、出生日期
            paramMap.put("birthday", Z02.getField(4, 0).encode());
            //5、职务代号
            paramMap.put("", Z02.getField(5, 0).encode());
            //6、职级代号
            paramMap.put("euDrtype", Z02.getField(6, 0).encode());
            //7、学历
            paramMap.put("", Z02.getField(7, 0).encode());
            //8、身份证号
            paramMap.put("idno", Z02.getField(8, 0).encode());
            //9、所属科室号
            String dept = Z02.getField(9, 0).encode();
            if(dept.contains("^")){
                String[] split = dept.split("\\^");
                String deptCode = split[0];
                paramMap.put("deptCode", deptCode);
            }else{
                paramMap.put("deptCode", dept);
            }
            //10、所属护理站
            paramMap.put("", Z02.getField(10, 0).encode());
            //11、人员类型 D 为医疗，N 为护理
            paramMap.put("dtEmptype", Z02.getField(11, 0).encode());
            //12、是否专家
            paramMap.put("flagSpec", Z02.getField(12, 0).encode());
            //13、有效性标志
            String flagActive = "1".equals(Z02.getField(13, 0).encode())?"1":"0";
            paramMap.put("flagActive", flagActive);
            //14、顺序号
            paramMap.put("", Z02.getField(14, 0).encode());
            //15、拼音码
            paramMap.put("pyCode", Z02.getField(15, 0).encode());
            //16、五笔码
            paramMap.put("dCode", Z02.getField(16, 0).encode());
            //17、操作员
            paramMap.put("modifier", Z02.getField(17, 0).encode());
            //18、操作时间（修改时间）
            paramMap.put("modityTime", sdf.parse(Z02.getField(18, 0).encode()));
            //19、电话号码
            paramMap.put("workphone", Z02.getField(19, 0).encode());

            //构造用户参数，更新数据库 ！！！！
            if("MDL".equals(SDMsgUtils.getPropValueStr(paramMap, "status"))){
                //删除逻辑
                deleteEmployee(paramMap);
            }else{
                //查询消息科室的相关数据信息
                Map<String, Object> deptMap = sDQueryUtils.queryDeptByCode(SDMsgUtils.getPropValueStr(paramMap, "deptCode"));
                if(deptMap==null || deptMap.size()<=0){
                    throw new BusException("科室不存在！！");
                }
                //判断数据库是否有此用户 and FLAG_ACTIVE='1'
                String sql = "select * from BD_OU_EMPLOYEE where CODE_EMP=? and DEL_FLAG='0'";
                BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, SDMsgUtils.getPropValueStr(paramMap, "codeEmp"));
                if(bdOuEmployee == null){
                    //新增人员信息
                    bdOuEmployee = insertEmployee(deptMap, paramMap);
                }else{
                    //更新人员信息
                    updateEmployee(bdOuEmployee, deptMap, paramMap);
                }
                //判断pi_master是否有数据
                String sqlPi = "select * from pi_master  where ID_NO in (lower(?),upper(?)) and DEL_FLAG='0' ";
                List<PiMaster> list = DataBaseHelper.queryForList(sqlPi, PiMaster.class, SDMsgUtils.getPropValueStr(paramMap, "idno"),SDMsgUtils.getPropValueStr(paramMap, "idno"));
                if(list !=null && list.size()>0){
                    for(PiMaster piMaster : list){
                        //不相等则更新
                        if(!(bdOuEmployee.getPkEmp().equalsIgnoreCase(piMaster.getPkEmp()))){
                            piMaster.setPkEmp(bdOuEmployee.getPkEmp());
                        }
                    }
                }
            }
        }
    }

    /**
     * 新增人员信息
     * @param deptMap
     * @param paramMap
     */
    private BdOuEmployee insertEmployee(Map<String, Object> deptMap, Map<String, Object> paramMap) {
        //当前时间
        Date date = new Date();
        String pkEmp = SDMsgUtils.getPk();
        String pkUser = SDMsgUtils.getPk();
        //构造人员参数
        BdOuEmployee bdOuEmployee = new BdOuEmployee();
        bdOuEmployee.setPkEmp(pkEmp);
        //bdOuEmployee.setPkOrg(SDMsgUtils.getPropValueStr(SDMsgUtils.getPkOrg(), "pkOrg"));
        bdOuEmployee.setPkOrg(Constant.PKORG);
        bdOuEmployee.setCodeEmp(SDMsgUtils.getPropValueStr(paramMap, "codeEmp"));
        bdOuEmployee.setNameEmp(SDMsgUtils.getPropValueStr(paramMap, "nameEmp"));
        bdOuEmployee.setDtSex(SDMsgUtils.getPropValueStr(paramMap, "dtSex"));
        //	拼音码
        bdOuEmployee.setPyCode(SDMsgUtils.getPropValueStr(paramMap, "pyCode"));
        //	自定义码
        bdOuEmployee.setdCode(SDMsgUtils.getPropValueStr(paramMap, "dCode"));
        //工作日期
        bdOuEmployee.setWorkDate(date);
        bdOuEmployee.setAddr(SDMsgUtils.getPropValueStr(paramMap, "addr"));
        bdOuEmployee.setHomephone(SDMsgUtils.getPropValueStr(paramMap, "homephone"));
        bdOuEmployee.setMobile(SDMsgUtils.getPropValueStr(paramMap, "workphone"));
        //医疗项目权限
        bdOuEmployee.setDtEmpsrvtype(SDMsgUtils.getPropValueStr(paramMap, "dtEmpsrvtype"));
        //处方权
        bdOuEmployee.setFlagPres("0");
        //麻醉处方权
        bdOuEmployee.setFlagAnes("0");
        //精一处方权
        bdOuEmployee.setFlagSpirOne("0");
        //精二处方权
        bdOuEmployee.setFlagSpec("0");
        //毒药处方权
        bdOuEmployee.setFlagPoi("0");
        bdOuEmployee.setDelFlag("0");
        bdOuEmployee.setFlagActive("1");
        //启用状态
        //bdOuEmployee.setFlagActive(SDMsgUtils.getPropValueStr(paramMap, "flagActive"));
        if(StringUtils.isEmpty(SDMsgUtils.getPropValueStr(paramMap, "idno"))){
            //	证件类型
            bdOuEmployee.setDtIdentype("99");
            //证件号码
            bdOuEmployee.setIdno("0");
        }else{
            //	证件类型
            bdOuEmployee.setDtIdentype("01");
            //证件号码
            bdOuEmployee.setIdno(SDMsgUtils.getPropValueStr(paramMap, "idno"));
        }
        try {
            bdOuEmployee.setBirthday(sdf.parse(SDMsgUtils.getPropValueStr(paramMap,"birthday")));
        } catch (ParseException e) {
            log.info("新增人员出生日期处理失败！");
        }
        //人员职业类型
        switch (SDMsgUtils.getPropValueStr(paramMap, "dtEmptype")){
            case "D":{
                //医生权限
                bdOuEmployee.setDtEmptype("01");
                //住院医生
                DataBaseHelper.insertBean(getUserRole(pkUser, "2c6fcdf27f304f398aef0a25daa5f328", bdOuEmployee.getPkOrg()));
                //门诊医生
                DataBaseHelper.insertBean(getUserRole(pkUser, "81eca884467042cf84d1f9a381a3fde9", bdOuEmployee.getPkOrg()));
            }break;
            case "N":{
                //护士权限
                bdOuEmployee.setDtEmptype("02");
                //护士
                DataBaseHelper.insertBean(getUserRole(pkUser, "75878bd43d9b43518dd0afbf0c19bd09", bdOuEmployee.getPkOrg()));
            }break;
            default:break;
        }
        bdOuEmployee.setEmail(SDMsgUtils.getPropValueStr(paramMap, "email"));
        //专长
        bdOuEmployee.setSpec(SDMsgUtils.getPropValueStr(paramMap, "spec"));
        //医师类别
        bdOuEmployee.setEuDrtype(SDMsgUtils.getPropValueStr(paramMap, "euDrtype"));
        //手术分级
        bdOuEmployee.setDtOplevel(SDMsgUtils.getPropValueStr(paramMap, "dtOplevel"));
        //抗菌药物级别
        bdOuEmployee.setDtAnti(SDMsgUtils.getPropValueStr(paramMap, "dtAnti"));
        bdOuEmployee.setCreator("HRP");
        bdOuEmployee.setCreateTime(date);
        bdOuEmployee.setTs(date);
        //新增人员科室关系信息
        BdOuEmpjob bdOuEmpjob = new BdOuEmpjob();
        bdOuEmpjob.setPkEmpjob(SDMsgUtils.getPk());
        bdOuEmpjob.setPkOrg(bdOuEmployee.getPkOrg());
        bdOuEmpjob.setCodeEmp(SDMsgUtils.getPropValueStr(paramMap, "codeEmp"));
        bdOuEmpjob.setPkEmp(pkEmp);
        //人员类别是否在职
        bdOuEmpjob.setDtEmptype("1");
        //部门
        bdOuEmpjob.setPkDept(SDMsgUtils.getPropValueStr(deptMap, "pkDept"));
        //是否主职
        bdOuEmpjob.setIsMain("1");
        bdOuEmpjob.setDutyDate(date);
        bdOuEmpjob.setJobname(SDMsgUtils.getPropValueStr(paramMap, "euDrtype"));
        bdOuEmpjob.setTs(date);
        //新用户 bd_ou_user
        BdOuUser bdOuUser = new BdOuUser();
        bdOuUser.setPkUser(pkUser);
        bdOuUser.setPkOrg(bdOuEmployee.getPkOrg());
        bdOuUser.setCodeUser(bdOuEmployee.getCodeEmp());
        bdOuUser.setEuUsertype("0");
        bdOuUser.setPkEmp(pkEmp);
        bdOuUser.setNameEmp(bdOuEmployee.getNameEmp());
        bdOuUser.setNameUser(bdOuEmployee.getNameEmp());
        bdOuUser.setFlagActive(bdOuEmployee.getFlagActive());
        bdOuUser.setIsLock("0");
        bdOuUser.setEuCerttype("2");
        bdOuUser.setEuUsertype("0");
        bdOuUser.setTs(date);
        bdOuUser.setPwd("670b14728ad9902aecba32e22fa4f6bd");
        bdOuUser.setPkUsrgrp(SDMsgUtils.getPropValueStr(deptMap, "pkUsrgrp"));
        //新增角色
        int insertBean = DataBaseHelper.insertBean(bdOuEmployee);
        int insertBean2 = DataBaseHelper.insertBean(bdOuEmpjob);
        int insertBean3 = DataBaseHelper.insertBean(bdOuUser);
        if(insertBean<1 || insertBean2<1 || insertBean3<1){
            //新增失败
            throw new BusException("人员信息新增或人员部门信息新增失败！");
        }
        return bdOuEmployee;
    }

    /**
     * 更新人员信息
     * @param bdOuEmployee
     * @param deptMap
     * @param paramMap
     */
    private void updateEmployee(BdOuEmployee bdOuEmployee, Map<String, Object> deptMap, Map<String, Object> paramMap) {
        //数据库已存在，更新操作
        bdOuEmployee.setDelFlag("0");
        bdOuEmployee.setFlagActive("1");
        bdOuEmployee.setModifier("HRP");
        bdOuEmployee.setFlagActive(SDMsgUtils.getPropValueStr(paramMap, "flagActive"));
        if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "nameEmp"))){
            bdOuEmployee.setNameEmp(SDMsgUtils.getPropValueStr(paramMap, "nameEmp"));
        }
        if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "dtSex"))){
            bdOuEmployee.setDtSex(SDMsgUtils.getPropValueStr(paramMap, "dtSex"));
        }
        if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "idno"))){
            //证件号码
            bdOuEmployee.setIdno(SDMsgUtils.getPropValueStr(paramMap, "idno"));
        }
        if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "pyCode"))){
            //拼音码
            bdOuEmployee.setPyCode(SDMsgUtils.getPropValueStr(paramMap, "pyCode"));
        }
        if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "dCode"))){
            //自定义码
            bdOuEmployee.setdCode(SDMsgUtils.getPropValueStr(paramMap, "dCode"));
        }
        if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "birthday"))){
            try {
                bdOuEmployee.setBirthday(sdf.parse(SDMsgUtils.getPropValueStr(paramMap,"birthday")));
            } catch (ParseException e) {
                log.info("新增人员出生日期处理失败！");
            }
        }
        if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "workphone"))){
            bdOuEmployee.setMobile(SDMsgUtils.getPropValueStr(paramMap, "workphone"));
        }
        switch (SDMsgUtils.getPropValueStr(paramMap, "dtEmptype")){
            case "D":bdOuEmployee.setDtEmptype("01");break;
            case "N":bdOuEmployee.setDtEmptype("02");break;
            default:break;
        }
        int updateBeanByPk = DataBaseHelper.updateBeanByPk(bdOuEmployee);
        if(updateBeanByPk<1){
            //更新失败
            throw new BusException("人员信息更新失败！");
        }
        //查询原有信息  是否主职 IS_MAIN='1'
        String sqlJob = "select * from BD_OU_EMPJOB where IS_MAIN='1' and PK_EMP=?";
        BdOuEmpjob bdOuEmpjob = DataBaseHelper.queryForBean(sqlJob, BdOuEmpjob.class, bdOuEmployee.getPkEmp());
        boolean isNew = false;
        //更新科室
        if(bdOuEmpjob == null){
            //新增 人员工作关系 表 bd_ou_empjob
            Date date = new Date();
            bdOuEmpjob = new BdOuEmpjob();
            bdOuEmpjob.setPkEmpjob(SDMsgUtils.getPk());
            bdOuEmpjob.setPkOrg(Constant.PKORG);
            bdOuEmpjob.setTs(date);
            bdOuEmpjob.setCreateTime(date);
            bdOuEmpjob.setDutyDate(date);
            bdOuEmpjob.setCreator("HRP");
            isNew = true;
        }
        //更新 人员工作关系 表 bd_ou_empjob
        bdOuEmpjob.setCodeEmp(SDMsgUtils.getPropValueStr(paramMap, "codeEmp"));
        //部门
        bdOuEmpjob.setPkDept(SDMsgUtils.getPropValueStr(deptMap, "pkDept"));
        //职称
        bdOuEmpjob.setJobname(SDMsgUtils.getPropValueStr(paramMap, "euDrtype"));
        //是否主职
        bdOuEmpjob.setIsMain("1");
        bdOuEmpjob.setDelFlag("0");
        //人员类别 在职
        bdOuEmpjob.setDtEmptype("1");
        bdOuEmpjob.setPkEmp(bdOuEmployee.getPkEmp());
        if(isNew){
            DataBaseHelper.insertBean(bdOuEmpjob);
        }else{
            DataBaseHelper.updateBeanByPk(bdOuEmpjob);
        }
        //判断是否需要更新用户组
        String sqlUser = "select * from BD_OU_USER where CODE_USER=?";
        List<BdOuUser> bdOuUsers = DataBaseHelper.queryForList(sqlUser, BdOuUser.class, bdOuEmployee.getCodeEmp());
        if(bdOuUsers!=null){
            for(BdOuUser bdOuUser:bdOuUsers){
                //if(bdOuUser.getPkUsrgrp() == null){
                    bdOuUser.setPkUsrgrp(SDMsgUtils.getPropValueStr(deptMap,"pkUsrgrp"));
                //}
                bdOuUser.setFlagActive("1");
                bdOuUser.setIsLock("0");
            }
            //更新数据
            DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BdOuUser.class),bdOuUsers);
        }

    }

    /**
     * 保存角色
     * @param pkUser
     * @param pkRole
     * @return
     */
    private BdOuUserRole getUserRole(String pkUser,String pkRole,String pkOrg){
        Date date = new Date();
        BdOuUserRole bdOuUserRole = new BdOuUserRole();
        bdOuUserRole.setPkUser(pkUser);
        bdOuUserRole.setPkRole(pkRole); //TODO
        bdOuUserRole.setPkUserrole(SDMsgUtils.getPk());
        bdOuUserRole.setCreateTime(date);
        bdOuUserRole.setCreator("HRP");
        bdOuUserRole.setDelFlag("0");
        bdOuUserRole.setPkOrg(pkOrg);
        bdOuUserRole.setTs(date);
        return bdOuUserRole;
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>保存简历<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * @param mfn
     */
    public void saveStaffProfile(MFN_M01 mfn) throws HL7Exception {
        int len = mfn.getMFReps();
        for (int i = 0; i < len; i++) {
            MFN_M01_MF mf =mfn.getMF(i);
            //String status = mfe.getRecordLevelEventCode().getValue();// MAD 新增   MUP修改
            Segment Z19 =(Segment) mf.get("Z19");

            Map<String, Object> mapParam = new HashMap<>(16);
            String emplCode = Z19.getField(1, 0).encode();//编码
            mapParam.put("emplCode", emplCode);
            String emplName = Z19.getField(2, 0).encode();//名称
            mapParam.put("emplName",emplName );
				/*String PICTURE_PATH = Z19.getField(3, 0).encode();//图片路径
				mapParam.put("PICTURE_PATH",PICTURE_PATH );*/
            String spec = Z19.getField(4, 0).encode();//特色、特长
            String INTRODUCTION = Z19.getField(5, 0).encode();//简介
            mapParam.put("spec","特长:"+spec+";简介:"+INTRODUCTION);
            String operCode = Z19.getField(7, 0).encode();//操作员
            mapParam.put("operCode",operCode );

            //保存人员简历
            updateEmpInfo(mapParam);
        }

    }
    /**
     * 由平台传入 - 修改人员信息
     * @param mapParam
     * @return
     */
    private void updateEmpInfo(Map<String, Object> mapParam){
        DataBaseHelper.update(" update bd_ou_employee  set  spec = ? ,  modifier = ? , MODITY_TIME = sysdate  where code_emp = ?",
                mapParam.get("spec"),mapParam.get("operCode"),mapParam.get("emplCode"));
    }

    /**
     *  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>保存材料<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * @param mfn
     */
    public void saveMate(MFN_M01 mfn) throws HL7Exception, ParseException {
        int len = mfn.getMFReps();
        MFE mfe;
        //查询服务分类编码 083
        Map<String, Object> queryBdItemcateByCode = sDQueryUtils.queryBdItemcateByCode("1001");
        for(int i=0;i<len;i++){
            MFN_M01_MF mf =mfn.getMF(i);
            mfe = mf.getMFE();
            Segment z =(Segment) mf.get("ZE0");
            Map<String, Object> paramMap = new HashMap<>(16);
            // MAD 新增   MUP修改
            paramMap.put("status", mfe.getRecordLevelEventCode().getValue());
            //编码(项目编码)
            paramMap.put("code", z.getField(1, 0).encode());
            //"codeHp":医保上传编码
            paramMap.put("codehp", z.getField(1, 0).encode());
            //name(项目名称)
            paramMap.put("name", z.getField(4, 0).encode());
            //拼音码
            paramMap.put("spcode", z.getField(5, 0).encode());
            //规格spec
            paramMap.put("spec", z.getField(6, 0).encode());
            //价格price
            paramMap.put("price", Double.parseDouble(z.getField(16, 0).encode()));
            //标准码
            paramMap.put("codestd", z.getField(1, 0).encode());
            //注册证号
            paramMap.put("codeext", z.getField(54, 0).encode());
            //单位
            paramMap.put("nameunit", z.getField(55, 0).encode());
            //自定义
            paramMap.put("dcode", z.getField(57, 0).encode());
            //生产厂商
            String z60 = z.getField(60, 0).encode();
            if(z60!=null && z60.contains("#")){
                String[] z60Arr = z60.split("#");
                paramMap.put("factorycode", z60Arr[0]);
                paramMap.put("factoryname", z60Arr[1]);
            }else{
                paramMap.put("factoryname", z60);
            }
            //z61产地 1：进口；0：国产  ~~~~ 国产是01，进口是02
            String z61 = z.getField(61, 0).encode();
            paramMap.put("dtsanitype", "1".equalsIgnoreCase(z61)?"02":"01");
            //是否高值(0：否，1：是)
            String z63 = z.getField(63, 0).encode();
            z63 = "1".equals(z63)?"0701":"07";
            paramMap.put("dtitemtype", z63);
            paramMap.put("pkitemcate", SDMsgUtils.getPropValueStr(queryBdItemcateByCode, "pkItemcate"));
            Map<String, Object> unitMap = DataBaseHelper.queryForMap("select t.pk_unit from bd_unit t where t.name=?", SDMsgUtils.getPropValueStr(paramMap, "nameunit"));
            paramMap.put("pkunit", SDMsgUtils.getPropValueStr(unitMap, "pkUnit"));
            //paramMap.put("dtitemtype", z.getField(9, 0).encode());//	项目类型
            //paramMap.put("pkunit", value);//计量单位主键（bd_unit）
            //paramMap.put("pkitemcate", value);//pk_itemcate	服务分类
            //paramMap.put("dtchcate", value);//dt_chcate	病案分类
            //paramMap.put("dtitemtype", value);//项目类型
            //保存工厂
            BdFactory bdFactory = saveFactory(paramMap);
            //定义bd_item_price
            List<BdItemPrice> bdItemPriceList = new ArrayList<>();
            //判断是更新或修改
            List<BdItem> bdItemList = DataBaseHelper.queryForList("select * from BD_ITEM b where b.code_std=?",BdItem.class, SDMsgUtils.getPropValueStr(paramMap, "codestd"));
            if(bdItemList==null || bdItemList.size()==0){
                //新增
                BdItem bdItem = saveBdItem(paramMap,bdFactory);
                //定义bd_item_price  插入新的价格信息数据
                BdItemPrice bdItemPrice = saveBdItemPrice(bdItem);
                //插入数据
                DataBaseHelper.insertBean(bdItem);
                DataBaseHelper.insertBean(bdItemPrice);
            }else{
                //修改
                for(BdItem bdItem :bdItemList){
                    bdItem.setDescItem("名称："+SDMsgUtils.getPropValueStr(paramMap, "name") +"规格："+ SDMsgUtils.getPropValueStr(paramMap, "spec"));
                    BdItemPrice bdItemPrice = saveBdItemPrice(bdItem);
                    bdItemPriceList.add(bdItemPrice);
                }
                //更新数据
                DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BdItem.class),bdItemList);
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdItemPrice.class),bdItemPriceList);
            }
        }
    }


    /**
     * 由平台传入 - 物资信息保存
     * @param paramMap
     * @throws ParseException
     */
    public BdItem saveBdItem(Map<String,Object> paramMap,BdFactory bdFactory){
        Date date = new Date();
        double price = (double) paramMap.get("price");
        //3、数据库里没有数据，新增数据，新增价格
        BdItem bdItem = new BdItem();
        bdItem.setPkItem(SDMsgUtils.getPk());
        //编码(项目编码)
        bdItem.setCode(SDMsgUtils.getPropValueStr(paramMap, "code"));
        //"codeHp":医保上传编码
        bdItem.setCodeHp(SDMsgUtils.getPropValueStr(paramMap, "codehp"));
        //name(项目名称)
        bdItem.setName(SDMsgUtils.getPropValueStr(paramMap, "name"));
        //拼音码
        bdItem.setSpcode(SDMsgUtils.getPropValueStr(paramMap, "spcode"));
        //规格spec
        bdItem.setSpec(SDMsgUtils.getPropValueStr(paramMap, "spec"));
        bdItem.setPrice(price);//价格price
        //标准码
        bdItem.setCodeStd(SDMsgUtils.getPropValueStr(paramMap, "codestd"));
        //	项目类型
        bdItem.setDtItemtype(SDMsgUtils.getPropValueStr(paramMap, "dtitemtype"));
        //pk_itemcate	服务分类
        bdItem.setPkItemcate(SDMsgUtils.getPropValueStr(paramMap, "pkitemcate"));
        //核算分类
        bdItem.setPkAudit("918d3896210c4a6ea37288b62f1a4633");
        //dt_chcate	病案分类
        bdItem.setDtChcate("01");
        //启用标志 0
        bdItem.setFlagActive("0");
        //注册证号
        bdItem.setCodeExt(SDMsgUtils.getPropValueStr(paramMap, "codeext"));
        //自定义码
        bdItem.setdCode(SDMsgUtils.getPropValueStr(paramMap, "dcode"));
        //生产厂家
        bdItem.setFactory(bdFactory.getName());
        bdItem.setPkFactory(bdFactory.getPkFactory());
        bdItem.setDtSanitype(SDMsgUtils.getPropValueStr(paramMap, "dtsanitype"));
        bdItem.setEuPricemode(EnumerateParameter.ZERO);
        bdItem.setTs(date);
        bdItem.setCreateTime(date);
        bdItem.setCreator("HRP"+sdf.format(date));
        //String name = SDMsgUtils.getPropValueStr(paramMap, "nameunit");//单位名字
        //Map<String, Object> unitMap = DataBaseHelper.queryForMap("select t.pk_unit from bd_unit t where t.name=?", name);
        //计量单位主键（表：bd_unit）
        bdItem.setPkUnit(SDMsgUtils.getPropValueStr(paramMap, "pkunit"));
        return bdItem;
    }

    /**
     * 保存厂家
     * @param paramMap
     */
    private BdFactory saveFactory(Map<String,Object> paramMap){
        //判断是否已有生产厂家
        String sql = "select PK_FACTORY,NAME from BD_FACTORY where code=?";
        BdFactory bdFactory = DataBaseHelper.queryForBean(sql, BdFactory.class, SDMsgUtils.getPropValueStr(paramMap, "factorycode"));
        if(bdFactory==null){
            //新增
            Date date = new Date();
            bdFactory = new BdFactory();
            bdFactory.setPkFactory(SDMsgUtils.getPk());
            bdFactory.setPkOrg(Constant.PKORG);
            bdFactory.setCode(SDMsgUtils.getPropValueStr(paramMap, "factorycode"));
            bdFactory.setFlagStop("0");
            bdFactory.setPkOrg(Constant.PKORG);
            bdFactory.setName(SDMsgUtils.getPropValueStr(paramMap, "factoryname"));
            bdFactory.setNote("HRP"+sdf.format(date));
            bdFactory.setShortName(SDMsgUtils.getPropValueStr(paramMap, "factoryname"));
            bdFactory.setCreateTime(date);
            bdFactory.setCreator("HRP"+sdf.format(date));
            DataBaseHelper.insertBean(bdFactory);
        }

        return bdFactory;
    }

    /**
     * 保存价格
     * @param bdItem
     * @return
     * @throws ParseException
     */
    private BdItemPrice saveBdItemPrice(BdItem bdItem) throws ParseException {
        Date date = new Date();
        BdItemPrice bdItemPrice = new BdItemPrice();
        bdItemPrice.setPkItemprice(SDMsgUtils.getPk());
        //	收费项目主键
        bdItemPrice.setPkItem(bdItem.getPkItem());
        //	所属机构（深圳大学总医院）
        bdItemPrice.setPkOrg(Constant.PKORG);
        //	价格类型
        bdItemPrice.setEuPricetype("1");
        //价格
        bdItemPrice.setPrice(bdItem.getPrice());
        //	停用标志
        bdItemPrice.setFlagStop("1");
        //开始时间
        bdItemPrice.setDateBegin(date);
        bdItemPrice.setDateEnd(sdf.parse("20990101000000"));
        bdItemPrice.setCreator("HRP");
        bdItemPrice.setTs(date);
        bdItemPrice.setCreateTime(date);
        return bdItemPrice;
    }

}
