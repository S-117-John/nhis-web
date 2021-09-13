package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl;

import com.alibaba.fastjson.annotation.JSONField;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * 收费费用补录消息请求体
 */

public class OperationFee {
    //就诊唯一号|住院流水号|门诊流水号
    @JsonProperty("episodeid")
    private String EpisodeID;
    //门诊号
    @JsonProperty("outmedno")
    private String OutMedNO;
    //住院号
    @JsonProperty("inmedno")
    private String InMedNO;
    //就诊次数
    @JsonProperty("visitnum")
    private String VisitNum;
    //就诊类别编码
    @JsonProperty("admtypecode")
    private String AdmTypeCode;
    //就诊类别名称
    @JsonProperty("admtypedesc")
    private String AdmTypeDesc;
    //医嘱目标人（患者唯一编号）--患者ID
    @JsonProperty("patientid")
    private String patientid;
    //患者姓名
    @JsonProperty("patientname")
    private String PatientName;
    //循环医嘱明细
    @JsonProperty("orderdetaillist")
    private List<OrderDetail> OrderDetailList;

    public String getEpisodeID() {
        return EpisodeID;
    }

    public void setEpisodeID(String episodeID) {
        EpisodeID = episodeID;
    }

    public String getOutMedNO() {
        return OutMedNO;
    }

    public void setOutMedNO(String outMedNO) {
        OutMedNO = outMedNO;
    }

    public String getInMedNO() {
        return InMedNO;
    }

    public void setInMedNO(String inMedNO) {
        InMedNO = inMedNO;
    }

    public String getVisitNum() {
        return VisitNum;
    }

    public void setVisitNum(String visitNum) {
        VisitNum = visitNum;
    }

    public String getAdmTypeCode() {
        return AdmTypeCode;
    }

    public void setAdmTypeCode(String admTypeCode) {
        AdmTypeCode = admTypeCode;
    }

    public String getAdmTypeDesc() {
        return AdmTypeDesc;
    }

    public void setAdmTypeDesc(String admTypeDesc) {
        AdmTypeDesc = admTypeDesc;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public List<OrderDetail> getOrderDetailList() {
        return OrderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        OrderDetailList = orderDetailList;
    }
}