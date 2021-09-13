package com.zebone.nhis.ma.pub.platform.zsba.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOccDt;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.zsba.dao.VtsInpatientMapper;
import com.zebone.nhis.ma.pub.platform.zsba.utils.DateUtil;
import com.zebone.nhis.ma.pub.platform.zsba.vo.*;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static com.zebone.nhis.ma.pub.platform.zsba.utils.DateUtil.getHour;

@Service
public class XicooVitalSignsService {

    @Autowired
    private VtsInpatientMapper vtsInpatientMapper;


    /**
     * 查询单个患者或护士信息接口
     *
     * @param dn
     * @param _id
     * @param type
     * @return
     */
    public String getPatInfo(String dn, String _id, Integer type) {
        PatientInfoDto patInfo = new PatientInfoDto();
        patInfo.setStatus(200);
        //查询患者信息
        if (type == null || type == 0) {
            List<Inpatient> patients = queryPatientInfo(_id, null);
            if (patients.isEmpty()) {
                patInfo.setStatus(400);
                patInfo.setError("未查询到该患者信息！");
            } else {
                Inpatient p = patients.get(0);
                patInfo.setMsg(p.getName() + "," + p.getBed_no() + "床$住院号:" + p.getInpatient_no());
            }
        } else {//查询护士信息
            List<Employee> nurses = queryNurseInfo(_id);
            if (nurses.isEmpty()) {
                patInfo.setStatus(400);
                patInfo.setError("未查询到该护士信息！");
            } else {
                Employee n = nurses.get(0);
                patInfo.setMsg(n.getUser_name() + "$" + "工号:" + n.getUser_id());
            }
        }
        return JsonUtil.writeValueAsString(patInfo);
    }


    /**
     * 获取单科室或者多科室（科室编码用逗号隔开）患者信息接口
     *
     * @param dn
     * @param _id
     * @return
     */
    public String getDeptInfo(String dn, String _id) {
        DeptInfoDto deptInfo = new DeptInfoDto();
        deptInfo.setStatus(200);
        //获取科室患者信息
        List<Inpatient> patients = queryPatientInfo(null, _id);
        deptInfo.getMsg().setTotalCount(patients.size());
        for (Inpatient p : patients) {
            DeptInfoDto.PatientInfo patientInfo = new DeptInfoDto.PatientInfo();
            patientInfo.setDeptId(p.getDept_code());
            patientInfo.setDeptInfo(p.getDept_name());
            patientInfo.setWardCode(p.getWard_code());
            patientInfo.setWardName(p.getWard_name());
            patientInfo.setPatientId(p.getInpatient_no());
            patientInfo.setPatientInfo(p.getName() + "，" + p.getBed_no() + "床$住院号:" + p.getInpatient_no());
            patientInfo.setBedNo(p.getBed_no());
            patientInfo.setName(p.getName());
            deptInfo.getMsg().getPatientsInfo().add(patientInfo);
        }
        return JsonUtil.writeValueAsString(deptInfo);
    }

    /**
     * 生命体征数据推送接口
     *
     * @param info
     */
    public void postVitalSigns(VitalSignsInfoDto info) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        VitalSignsData vitalSignsData = info.getData();
        List<Inpatient> patients = queryPatientInfo(vitalSignsData.getTag(), null);
        Inpatient patient = patients.isEmpty() ? null : patients.get(0);
        List<Employee> nurses = queryNurseInfo(vitalSignsData.getTag2());
        Employee nurse = nurses.isEmpty() ? null : nurses.get(0);
        //保存本次推送的患者生命体征数据
        saveTemperature(vitalSignsData, patient, nurse);
    }



    /**
     * 查询患者信息
     *
     * @param id
     * @param deptId
     * @return
     */
    private List<Inpatient> queryPatientInfo(String id, String deptId) {
        Map<String, Object> map = new HashMap<>();
        if (id != null) {
            map.put("codeIp", id);
        } else {
            map.put("codeDept", deptId);
        }
        List<Inpatient> inpatients = vtsInpatientMapper.selectInpatients(map);
        return inpatients;
    }

    /**
     * 获取护士信息
     *
     * @param id
     * @return
     */
    private List<Employee> queryNurseInfo(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", id);
        List<Employee> employees = vtsInpatientMapper.selectEmployees(map);
        return employees;
    }

    /**
     * 保存本次推送的患者生命体征数据
     *
     * @param vitalSignsData
     * @param patient
     * @param nurse
     */
    private void saveTemperature(VitalSignsData vitalSignsData, Inpatient patient, Employee nurse) {
        long specifyTime = adjustTempSpecifyTime(vitalSignsData.getSpecifyTime(), 2);//归档时间
        long timestamp = adjustTempSpecifyTime(vitalSignsData.getTimestamp(), 2);//录入时间
        //生命体征归档时间
        Date timeVts = new Date(specifyTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeVts);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        //生命体征归档时间点(明细表用)
        Date dateVtsDt = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //生命体征归档日期，可连同患者就诊主键做唯一约束
        Date dateVts = calendar.getTime();
        //生命体征测量时点（明细表用）
        int hour = getHourVts(timeVts.getHours());
        if (patient == null || nurse == null) {
            return;
        }
        String pkPv = patient.getPk_pv();//患者当前就诊主键
        String dateVtStr = DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", dateVts);
        String dateVtsDtStr = DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", dateVtsDt);
        //判断该患者在当天是否已经录入过生命体征数据
        ExVtsOcc exVtsOcc = DataBaseHelper.queryForBean("select * from ex_vts_occ where pk_pv = ? and date_vts = ?",
                ExVtsOcc.class, pkPv, dateVtStr);
        if (exVtsOcc != null) {
        	String sql = null;
        	if (hour < 12) {
        		sql = "select * from ex_vts_occ_dt where pk_vtsocc = ? and eu_dateslot = '0' and hour_vts = '"+hour+"' and (flag_add is null or flag_add = '0') and del_flag = '0'";
            } else {
            	int hours =  hour - 12;
            	sql = "select * from ex_vts_occ_dt where pk_vtsocc = ? and eu_dateslot = '1' and hour_vts = '"+hours+"' and (flag_add is null or flag_add = '0') and del_flag = '0'";
            }
            //该患者当天已经录入过生命体征信息
            ExVtsOccDt exVtsOccDt = DataBaseHelper.queryForBean(sql,
                    ExVtsOccDt.class, exVtsOcc.getPkVtsocc());
            //组装修改后的生命体征数据
            exVtsOcc = setExVtsOccvO(exVtsOcc, vitalSignsData, patient, nurse, hour, true);
            DataBaseHelper.updateBeanByPk(exVtsOcc, false);
            if (exVtsOccDt != null) {
                //该患者本次推送的归档时间点已经录过生命体征信息----修改
                exVtsOccDt = setExVtsOccDt(exVtsOccDt, vitalSignsData, patient, nurse, hour, exVtsOcc, true);
                DataBaseHelper.updateBeanByPk(exVtsOccDt, false);
            } else {
                //该患者本次推送的归档时间点未曾录过生命体征信息----新增
                exVtsOccDt = new ExVtsOccDt();
                exVtsOccDt.setDateVts(dateVtsDt);
                exVtsOccDt.setPkVtsocc(exVtsOcc.getPkVtsocc());
                exVtsOccDt = setExVtsOccDt(exVtsOccDt, vitalSignsData, patient, nurse, hour, null, false);
                DataBaseHelper.insertBean(exVtsOccDt);
            }
        } else {
            exVtsOcc = new ExVtsOcc();
            exVtsOcc.setDateVts(dateVts);//归档日期
            exVtsOcc.setDateInput(new Date(timestamp));//录入时间
            exVtsOcc = setExVtsOccvO(exVtsOcc, vitalSignsData, patient, nurse, hour, false);
            //新增当天生命体征数据
            DataBaseHelper.insertBean(exVtsOcc);
            //新增该患者本次归档时间点推送的生命体征详细信息
            ExVtsOccDt exVtsOccDt = new ExVtsOccDt();
            exVtsOccDt.setDateVts(dateVtsDt);
            exVtsOccDt.setPkVtsocc(exVtsOcc.getPkVtsocc());
            exVtsOccDt = setExVtsOccDt(exVtsOccDt, vitalSignsData, patient, nurse, hour, null, false);
            DataBaseHelper.insertBean(exVtsOccDt);
        }
    }

    private int getHourVts(int hours) {
    	//取小时进行计算
    	//1:00~4:59区间归档到3点上。后面以此类推
    	//归档时间取小时差的绝对值最小的为对应时间
        int[] hour = new int[]{3, 7, 11, 15, 19, 23};
        int ret=hour[5];
        //0点归档到23点
        if(0==hours) {
        	return ret;
        }
        int abs=Math.abs(hours-hour[5]);
        for (int i = hour.length - 1; i >= 0; i--) {
            int a =Math.abs(hours-hour[i]);
            if (a<abs){
                abs=a;
                ret=hour[i];
            };
        }
        return ret;
    }

    private long adjustTempSpecifyTime(long specifyTime, int off) {
        specifyTime = specifyTime + (2 - off) * 3600000L;
        specifyTime = DateUtil.getDayStartTime(specifyTime) + (getHour(specifyTime) / 4 * 4 + off) * 3600000L;
        return specifyTime;
    }

    /**
     * 组装生命体征数据
     *
     * @param exVtsOcc
     * @param vitalSignsData
     * @param patient
     * @param nurse
     * @param hour
     * @param isEdit
     * @return
     */
    private ExVtsOcc setExVtsOccvO(ExVtsOcc exVtsOcc, VitalSignsData vitalSignsData, Inpatient patient, Employee nurse, int hour, boolean isEdit) {
        //排便类型:0 正常，1 失禁，2 人工肛门（EuStooltype）、大便次数（ValStool）、灌肠后大便测试（ValStoolColo）
        String stoolCountStr = String.valueOf(vitalSignsData.getStoolCount());
        if (vitalSignsData.getStoolCount() > 0 && vitalSignsData.getStoolCount() < 10) {
            exVtsOcc.setEuStooltype("0");
            exVtsOcc.setValStool(Long.valueOf(stoolCountStr));
        } else if (vitalSignsData.getStoolCount() > 0 && vitalSignsData.getStoolCount() == 10) {
            exVtsOcc.setEuStooltype("1");
            exVtsOcc.setValStool(0l);
        } else if (vitalSignsData.getStoolCount() > 0 && vitalSignsData.getStoolCount() == 11) {
            exVtsOcc.setEuStooltype("2");
            exVtsOcc.setValStool(0l);
        } else if (vitalSignsData.getStoolCount() > 0 && vitalSignsData.getStoolCount() > 12 && stoolCountStr.startsWith("1")) {
            exVtsOcc.setEuStooltype("0");
            exVtsOcc.setValStool(Long.valueOf(stoolCountStr.substring(3, 4)));
            exVtsOcc.setValStoolColo(Long.valueOf(stoolCountStr.substring(5, 6)));
        }
        //exVtsOcc.setFlagColo("0");//灌肠标志
        if (vitalSignsData.getUPD() > 0) {//尿量
            exVtsOcc.setValUrine(vitalSignsData.getUPD() + "");
        }
        if (vitalSignsData.getWeight() > 0) {//体重
            exVtsOcc.setValWeight(vitalSignsData.getWeight() + "");
        } else if (vitalSignsData.getWeight() == 0 && vitalSignsData.getRefuse() != null) {
            exVtsOcc.setValWeight(vitalSignsData.getRefuse());
        }
        if (hour < 12) {//上午血压
            if (vitalSignsData.getSystolic() > 0) {//收缩压
                exVtsOcc.setValSbp(Long.valueOf(vitalSignsData.getSystolic() + ""));
            }
            if (vitalSignsData.getDiastolic() > 0) {//舒张压
                exVtsOcc.setValDbp(Long.valueOf(vitalSignsData.getDiastolic() + ""));
            }
        } else {//下午血压
            if (vitalSignsData.getSystolic() > 0) {//收缩压
                exVtsOcc.setValSbpAdd(Long.valueOf(vitalSignsData.getSystolic() + ""));
            }
            if (vitalSignsData.getDiastolic() > 0) {//舒张压
                exVtsOcc.setValDbpAdd(Long.valueOf(vitalSignsData.getDiastolic() + ""));
            }
        }
        exVtsOcc.setPkDeptInput(patient.getPk_dept_ns());//录入病区
        Date date = new Date();
        exVtsOcc.setTs(date);//时间戳
        if (!isEdit) {
            exVtsOcc.setPkVtsocc(NHISUUID.getKeyId());//设置主键
            exVtsOcc.setPkOrg(patient.getPk_org());//所属组织
            exVtsOcc.setPkPi(patient.getPatient_id());//患者pk_pi
            exVtsOcc.setPkPv(patient.getPk_pv());//患者本次就诊主键
            exVtsOcc.setPkEmpInput(nurse.getPk_emp());//录入人主键
            exVtsOcc.setNameEmpInput(nurse.getUser_name());//录入人名称
            exVtsOcc.setCreateTime(date);//创建时间
            exVtsOcc.setCreator(nurse.getPk_emp());//创建人
        } else {
            exVtsOcc.setModifier(nurse.getPk_emp());//修改人
            exVtsOcc.setModityTime(date);//修改时间
        }
        return exVtsOcc;
    }

    /**
     * 组装生命体征明细数据
     *
     * @param exVtsOccDt
     * @param vitalSignsData
     * @param patient
     * @param nurse
     * @param hour
     * @param exVtsOcc
     * @param isEdit
     * @return
     */
    private ExVtsOccDt setExVtsOccDt(ExVtsOccDt exVtsOccDt, VitalSignsData vitalSignsData, Inpatient patient,
                                     Employee nurse, int hour, ExVtsOcc exVtsOcc, boolean isEdit) {
        if (hour < 12) {
            exVtsOccDt.setEuDateslot("0");//上午
            exVtsOccDt.setHourVts(hour);//时间点（3、7、11）
        } else {
            exVtsOccDt.setEuDateslot("1");//下午
            exVtsOccDt.setHourVts(hour - 12);//时间点（3、7、11）
        }
        exVtsOccDt.setFlagAdd(null);
        //测温位置
        if (vitalSignsData.getTemperatureMP() != null && "腋下".equals(vitalSignsData.getTemperatureMP())) {
            exVtsOccDt.setEuTemptype("1");
        } else if (vitalSignsData.getTemperatureMP() != null && "口腔".equals(vitalSignsData.getTemperatureMP())) {
            exVtsOccDt.setEuTemptype("0");
        } else if (vitalSignsData.getTemperatureMP() != null && "直肠".equals(vitalSignsData.getTemperatureMP())) {
            exVtsOccDt.setEuTemptype("2");
        }
        //体温测量值
        if (vitalSignsData.getTemperature() > 0.0) {
            exVtsOccDt.setValTemp(BigDecimal.valueOf(vitalSignsData.getTemperature()));
        }
        //脉搏
        if (vitalSignsData.getHeartrate() > 0) {
            exVtsOccDt.setValPulse(BigDecimal.valueOf(vitalSignsData.getHeartrate()));
        }
        //心率
        if (vitalSignsData.getHeartrate2() > 0) {
            exVtsOccDt.setValHr(BigDecimal.valueOf(vitalSignsData.getHeartrate2()));
            exVtsOccDt.setEuHrtype("0");
        }
        //呼吸
        if (vitalSignsData.getRespRate() > 0) {
            exVtsOccDt.setValBre(BigDecimal.valueOf(vitalSignsData.getRespRate()));
            exVtsOccDt.setEuBrtype("0");
        }
        Date date = new Date();
        exVtsOccDt.setTs(date);///时间戳
        if (!isEdit) {
            exVtsOccDt.setPkVtsoccdt(NHISUUID.getKeyId());//生命体征明细主键
            exVtsOccDt.setPkOrg(patient.getPk_org());//所属机构
            exVtsOccDt.setCreator(nurse.getPk_emp());//创建人
            exVtsOccDt.setCreateTime(date);//创建时间
        } else {
            exVtsOccDt.setModifier(nurse.getPk_emp());//修改人
            exVtsOccDt.setModityTime(date);//修改时间
        }
        //采集类型
        List<BdDefdoc> bdDefdocs = DataBaseHelper.queryForList("select * from bd_defdoc where code_defdoclist = '120303'", BdDefdoc.class);
        if (vitalSignsData.getRefuse() != null && bdDefdocs.size() > 0) {
            for (BdDefdoc bdDefdoc : bdDefdocs) {
                if (vitalSignsData.getRefuse().equals(bdDefdoc.getName()) && !"正常".equals(bdDefdoc.getName())) {
                    exVtsOccDt.setDtVtscolltype(bdDefdoc.getCode());
                }
            }
        }
        return exVtsOccDt;
    }
}
