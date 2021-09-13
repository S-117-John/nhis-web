package com.zebone.nhis.webservice.pskq.service.impl;

import ca.uhn.hl7v2.model.Segment;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.ExOpSch;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.Employee;
import com.zebone.nhis.webservice.pskq.model.SurgeryRecord;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.service.SurgeryRecordService;
import com.zebone.nhis.webservice.pskq.utils.PskqMesUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.ParseException;
import java.util.*;

@Service
public class SurgeryRecordServiceImpl implements SurgeryRecordService {

    @Override
    public void update(RequestBody requestBody, ResultListener listener){
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
        try{
            List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("SURGERY_RECORD");
            List<SurgeryRecord> surgeryRecordList = (List<SurgeryRecord>) requestBody.getMessage().get("SURGERY_RECORD");
            SurgeryRecord surgeryRecord = (SurgeryRecord) MessageFactory.deserialization(list, new SurgeryRecord());
            //申请单号
            String applyNo = surgeryRecord.getSurgeryApplyNo();
            //事件码
            String eventCode = requestBody.getEvent().getEventCode();

            String opApplyState = "0";

            //入手术间
            if("E008001".equalsIgnoreCase(eventCode)){
                opApplyState = "3";
            }

            //开始麻醉
            if("E008002".equalsIgnoreCase(eventCode)){
                opApplyState = "3";
            }
            //开始手术
            if("E008003".equalsIgnoreCase(eventCode)){
                opApplyState = "3";
            }
            //j结束手术
            if("E008004".equalsIgnoreCase(eventCode)){
                opApplyState = "5";
            }
            //j结束麻醉
            if("E008005".equalsIgnoreCase(eventCode)){
                opApplyState = "5";
            }

            //出手术间
            if("E008006".equalsIgnoreCase(eventCode)){
                opApplyState = "5";
            }

            //查询pk_cnord
            String sql = "select * from CN_ORDER where CODE_APPLY = ?";
            CnOrder cnOrder = DataBaseHelper.queryForBean(sql,CnOrder.class,new Object[]{applyNo});
            if(cnOrder==null){
                listener.error(String.format("根据申请单号【%s】没有查询到手术医嘱",applyNo));
                return;
            }
            sql = "select * from BD_OU_EMPLOYEE where CODE_EMP = ?";
            BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,new Object[]{surgeryRecord.getEnterOperaId()});
            if(bdOuEmployee==null){
                listener.error(String.format("根据操作员id【%s】没有查询到手术医嘱",surgeryRecord.getEnterOperaId()));
                return;
            }
            //更新cn_op_apply
            sql = "update cn_op_apply set eu_status= ? where pk_cnord = ?";
            int result = DataBaseHelper.update(sql,new Object[]{opApplyState,cnOrder.getPkCnord()});

            //更新ex_order_occ
            sql = "update ex_order_occ set date_occ=?,eu_status='1',pk_emp_occ=?,name_emp_occ=? where pk_cnord = ? and eu_status = '0'";
            result = DataBaseHelper.update(sql,new Object[]{new Date(),bdOuEmployee.getPkEmp(),bdOuEmployee.getNameEmp(),cnOrder.getPkCnord()});


            listener.success("消息处理成功");
            dataSourceTransactionManager.commit(transStatus);
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transStatus);
            listener.exception(e.getMessage());
        }
    }

    @Override
    public void addExOpSch(RequestBody requestBody, ResultListener listener) {
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
        ExOpSch exOpSch = new ExOpSch();
        try {
            List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("SURGERY_RECORD");
            SurgeryRecord surgeryRecord = (SurgeryRecord) MessageFactory.deserialization(list, new SurgeryRecord());
            int exist = DataBaseHelper.execute("select * from cn_op_apply where pk_cnord = (select pk_cnord from cn_order where  code_apply = ? and code_ordtype = '04' and del_flag = '0') and del_flag = '0'", surgeryRecord.getSurgeryApplyNo());
            if (exist == 0) {
                throw new BusException("未查询到编号为:" + surgeryRecord.getSurgeryApplyNo() + "手术申请单信息");
            }

            Map<String, Object> patiInfo = new HashMap<>();
            patiInfo = DataBaseHelper.queryForMap("select ord.pk_org,ord.pk_pv,ord.pk_cnord,ord.pk_dept,dept.name_dept,ord.date_sign,pv.dt_sex,pv.age_pv,pi.code_ip,pv.name_pi,bed.bedno from cn_order ord left join pv_encounter pv on pv.pk_pv =ord.pk_pv left join pv_bed bed on pv.pk_pv =bed.pk_pv left join bd_ou_dept dept on dept.pk_dept = ord.pk_dept left join pi_master pi on pi.pk_pi = pv.pk_pi   where ord.code_apply = ? and ord.code_ordtype = '04'", surgeryRecord.getSurgeryApplyNo());
            exOpSch.setPkOpsch(PskqMesUtils.getPk());
            exOpSch.setPkOrg(PskqMesUtils.getPropValueStr(patiInfo, "pkOrg"));
            exOpSch.setPkPv(PskqMesUtils.getPropValueStr(patiInfo, "pkPv"));
            exOpSch.setPkCnord(PskqMesUtils.getPropValueStr(patiInfo, "pkCnord"));
            exOpSch.setPkDept(PskqMesUtils.getPropValueStr(patiInfo, "pkDept"));
            exOpSch.setNameDept(PskqMesUtils.getPropValueStr(patiInfo, "nameDept"));
            String dateApply = (PskqMesUtils.getPropValueStr(patiInfo, "dateSign"));
            if (!"".equals(dateApply)) {
                try {
                    exOpSch.setDateApply(PskqMesUtils.simDatFor.parse(dateApply));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String sexCode = PskqMesUtils.getPropValueStr(patiInfo, "dtSex");
            if ("02".equals(sexCode)) {
                sexCode = "男";
            } else if ("03".equals(sexCode)) {
                sexCode = "女";
            } else if ("04".equals(sexCode)) {
                sexCode = "未说明的性别";
            }
            exOpSch.setDtSex(sexCode);
            exOpSch.setAgePv(PskqMesUtils.getPropValueStr(patiInfo, "agePv"));
            exOpSch.setCodeIp(PskqMesUtils.getPropValueStr(patiInfo, "codeIp"));
            exOpSch.setNamePi(PskqMesUtils.getPropValueStr(patiInfo, "namePi"));
            exOpSch.setBedNo(PskqMesUtils.getPropValueStr(patiInfo, "bedno"));
            exOpSch.setCodeApply(surgeryRecord.getSurgeryApplyNo());

            String creatorname = surgeryRecord.getEnterOperaName();
            exOpSch.setCreator(creatorname);
            exOpSch.setCreateTime(new Date());

            String opBigRoom = surgeryRecord.getRoomCode();
            String opSmallRoom = surgeryRecord.getRoomNo();
            Map<String, Object> empMap1 = new HashMap<>();
            empMap1 = DataBaseHelper.queryForMap("select pk_dept,name_dept from bd_ou_dept where code_dept = ?", opBigRoom);
            Map<String, Object> empMap2 = new HashMap<>();
            empMap2 = DataBaseHelper.queryForMap("SELECT name,name_place FROM bd_res_opt WHERE code= ?", opSmallRoom);
            exOpSch.setPkDeptOp(PskqMesUtils.getPropValueStr(empMap1, "pkDept"));//手术室
            exOpSch.setNameDeptOp(PskqMesUtils.getPropValueStr(empMap1, "nameDept"));//手术室名称
            exOpSch.setRoomNo(PskqMesUtils.getPropValueStr(empMap2, "name"));//手术室间
            exOpSch.setRoomPlace(PskqMesUtils.getPropValueStr(empMap2, "namePlace"));//手术位置
            //--暂定写死院区  前台展示是否有必要加两个查询条件
            exOpSch.setNameOrgarea("院本部");
            exOpSch.setPkOrgarea("cac9330a926945c59332b064e7e7ff9e");

            //手术台次
            exOpSch.setTicketno(Integer.parseInt(surgeryRecord.getSurgerySeqNo()));

            //紧急标志
            String flag = surgeryRecord.getEmerFlag();
            if ("1".equals(flag)) {
                flag = "3";
            } else if ("0".equals(flag)) {
                flag = "1";
            } else {
                flag = "2";
            }
            exOpSch.setEuOptype(flag);

            //手术医生
            if (!"".equals(surgeryRecord.getSurgeonId())){
                Map<String, Object> empMap = DataBaseHelper.queryForMap("SELECT pk_emp,name_emp FROM bd_ou_employee WHERE code_emp= ? and del_flag = '0'", surgeryRecord.getSurgeonId());
                exOpSch.setNameEmp(PskqMesUtils.getPropValueStr(empMap, "nameEmp"));
                exOpSch.setPkEmp(PskqMesUtils.getPropValueStr(empMap, "pkEmp"));
            }else{
                dataSourceTransactionManager.rollback(transStatus);
                listener.error("未传入手术医生信息编码，请检查");
                return;
            }

            //手术一助
            if (!"".equals(surgeryRecord.getAssistantFirstId())){
                Map<String, Object> empMap = DataBaseHelper.queryForMap("SELECT pk_emp,name_emp FROM bd_ou_employee WHERE code_emp= ? and del_flag = '0'", surgeryRecord.getAssistantFirstId());
                exOpSch.setNameEmpAsst(PskqMesUtils.getPropValueStr(empMap,"nameEmp"));
                exOpSch.setPkEmpAsst(PskqMesUtils.getPropValueStr(empMap,"pkEmp"));
            }


            exOpSch.setNote(surgeryRecord.getDocumentCmmt());
            exOpSch.setNameOpt(surgeryRecord.getSurgeryName());
            exOpSch.setDatePlan(PskqMesUtils.simDatFor.parse(surgeryRecord.getScheduleDateTime()));//计划时间
            exOpSch.setDateSch(PskqMesUtils.simDatFor.parse(surgeryRecord.getSurgeryStartDateTime()));//排班日期

            Map<String, Object> empMapSch = new HashMap<>();
            empMapSch = DataBaseHelper.queryForMap("select job.pk_emp,job.pk_dept from bd_ou_employee emp left join bd_ou_empjob job on emp.pk_emp = job.pk_emp   where emp.name_emp = ? ",creatorname);
            exOpSch.setPkEmpSch(PskqMesUtils.getPropValueStr(empMapSch,"pkEmp"));//排班人
            exOpSch.setPkDeptSch(PskqMesUtils.getPropValueStr(empMapSch,"pkDept"));//排班部门
            exOpSch.setNameEmpSch(creatorname);
            exOpSch.setEuStatus("1");
            exOpSch.setDelFlag("0");
            Map<String,Object>   opMap = new HashMap<>();
            StringBuffer sqlBuf  = new StringBuffer();
            sqlBuf.append("update cn_op_apply set eu_status ='2' ");

            //麻醉师
            if (!"".equals(surgeryRecord.getAnesthetistId())){
                Map<String, Object> empMap = DataBaseHelper.queryForMap("SELECT pk_emp,name_emp FROM bd_ou_employee WHERE code_emp= ? and del_flag = '0'", surgeryRecord.getAnesthetistId());
                sqlBuf.append(" , pk_emp_anae = '"+PskqMesUtils.getPropValueStr(empMap,"pkEmp")+"'");
                sqlBuf.append(" , name_emp_anae = '"+PskqMesUtils.getPropValueStr(empMap,"nameEmp")+"'");
            }
            //巡回护士
            if (!"".equals(surgeryRecord.getTourNurseId())){
                Map<String, Object> empMap = DataBaseHelper.queryForMap("SELECT pk_emp,name_emp FROM bd_ou_employee WHERE code_emp= ? and del_flag = '0'", surgeryRecord.getTourNurseId());
                sqlBuf.append(" , pk_emp_circul = '"+PskqMesUtils.getPropValueStr(empMap,"pkEmp")+"'");
                sqlBuf.append(" , name_emp_cricul = '"+PskqMesUtils.getPropValueStr(empMap,"nameEmp")+"'");

            }
            //器械护士
            if (!"".equals(surgeryRecord.getHandWashingNurseId())){
                Map<String, Object> empMap = DataBaseHelper.queryForMap("SELECT pk_emp,name_emp FROM bd_ou_employee WHERE code_emp= ? and del_flag = '0'", surgeryRecord.getHandWashingNurseId());
                sqlBuf.append(" , pk_emp_scrub = '"+PskqMesUtils.getPropValueStr(empMap,"pkEmp")+"'");
                sqlBuf.append(" , name_emp_scrub = '"+PskqMesUtils.getPropValueStr(empMap,"nameEmp")+"'");

            }
            //麻醉方法
            if (!"".equals(surgeryRecord.getAnesthMethodCode())){
                sqlBuf.append(" , dt_anae = '"+surgeryRecord.getAnesthMethodCode()+"'");
            }
            sqlBuf.append(" where pk_cnord in (select pk_cnord from cn_order ord where ord.code_ordtype = '04' and ord.code_apply  = '"+surgeryRecord.getSurgeryApplyNo()+"')  and eu_status !='5' ");

            //更新手术申请状态
            DataBaseHelper.update(sqlBuf.toString());
            //允许手术排班覆盖
            DataBaseHelper.execute("delete from ex_op_sch where pk_cnord in (select pk_cnord from cn_order ord where ord.code_ordtype = '04' and ord.code_apply = '"+surgeryRecord.getSurgeryApplyNo()+"') ");
            DataBaseHelper.insertBean(exOpSch);
            listener.success("消息处理成功");
            dataSourceTransactionManager.commit(transStatus);
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transStatus);
            listener.exception(e.getMessage());
        }
    }



}
