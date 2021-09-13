package com.zebone.nhis.webservice.lbzy.model.ipin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryInHospitalDay {

    @XmlElement(name = "FeeDate")
    private String FeeDate;

    @XmlElement(name = "TotalCost")
    private Double totalCost;

    @XmlElement(name = "OutReportPrint")
    private String outReportPrint;

    public String getFeeDate() {
        return FeeDate;
    }

    public void setFeeDate(String feeDate) {
        FeeDate = feeDate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getOutReportPrint() {
        return outReportPrint;
    }

    public void setOutReportPrint(String outReportPrint) {
        this.outReportPrint = outReportPrint;
    }
}
