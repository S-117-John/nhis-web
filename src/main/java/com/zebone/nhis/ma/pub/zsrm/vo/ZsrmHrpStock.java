package com.zebone.nhis.ma.pub.zsrm.vo;

import java.util.List;

public class ZsrmHrpStock {
    private String operation;
    private String hospital;
    private List<String> medicineCode;
    private List<String> locationCode;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public List<String> getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(List<String> medicineCode) {
        this.medicineCode = medicineCode;
    }

    public List<String> getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(List<String> locationCode) {
        this.locationCode = locationCode;
    }
}
