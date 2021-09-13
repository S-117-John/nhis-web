/**
  *
  */
package com.zebone.nhis.ma.pub.lb.vo.archInfoVo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 *病案信息上传参数实体
 *
 */
public class ArchInfoDataVo {
    @JSONField(name = "Medical")
    private Medical medical;
    @JSONField(name = "MedicalAttach")
    private MedicalAttach medicalAttach;
    @JSONField(name = "ListCheck")
    private List<ListCheck> listCheck;
    @JSONField(name = "ListOperation")
    private List<ListOperation> listOperation;
    @JSONField(name = "LeaveHospital")
    private LeaveHospital leaveHospital;
    @JSONField(name = "ListICU")
    private List<ListICU> listICU;

    public Medical getMedical() {
        return medical;
    }

    public void setMedical(Medical medical) {
        this.medical = medical;
    }

    public MedicalAttach getMedicalAttach() {
        return medicalAttach;
    }

    public void setMedicalAttach(MedicalAttach medicalAttach) {
        this.medicalAttach = medicalAttach;
    }

    public List<ListCheck> getListCheck() {
        return listCheck;
    }

    public void setListCheck(List<ListCheck> listCheck) {
        this.listCheck = listCheck;
    }

    public List<ListOperation> getListOperation() {
        return listOperation;
    }

    public void setListOperation(List<ListOperation> listOperation) {
        this.listOperation = listOperation;
    }

    public LeaveHospital getLeaveHospital() {
        return leaveHospital;
    }

    public void setLeaveHospital(LeaveHospital leaveHospital) {
        this.leaveHospital = leaveHospital;
    }

    public List<ListICU> getListICU() {
        return listICU;
    }

    public void setListICU(List<ListICU> listICU) {
        this.listICU = listICU;
    }
}