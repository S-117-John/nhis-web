package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SetQueueListReq {

	/**
     * 队列号
     */
    @XmlElement(name = "queueNum")
    private String queueNum;
    
    /**
     * 病人ID
     */
    @XmlElement(name = "patientId")
    private String patientId;
    
    /**
     * 病人姓名
     */
    @XmlElement(name = "patientName")
    private String patientName;
    
    /**
     * 叫号时间： yyyy-MM-dd hh:mm:dd
     */
    @XmlElement(name = "callingTime")
    private String callingTime;
    
    /**
     * 叫号状态（0：等候，1：就诊中，2：过号，3：完成，4：取消）
     */
    @XmlElement(name = "callingFlag")
    private String callingFlag;
    
    /**
     * 出诊午别(2 上午，4 下午)
     */
    @XmlElement(name = "clinicTime")
    private String clinicTime;
    
    /**
     * 医生代码
     */
    @XmlElement(name = "doctorCode")
    private String doctorCode;
    
    /**
     * 医生名称
     */
    @XmlElement(name = "doctorName")
    private String doctorName;
    
    /**
     * 医生职称
     */
    @XmlElement(name = "title")
    private String title;
    
    /**
     * 诊室代码
     */
    @XmlElement(name = "roomCode")
    private String roomCode;
    
    /**
     * 诊室名称
     */
    @XmlElement(name = "roomName")
    private String roomName;
    
    /**
     * 诊室号
     */
    @XmlElement(name = "roomNo")
    private String roomNo;
    
    /**
     * 科室代码
     */
    @XmlElement(name = "deptCode")
    private String deptCode;
    
    /**
     * 科室名称
     */
    @XmlElement(name = "deptName")
    private String deptName;
}
