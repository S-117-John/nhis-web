package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class RoomListRes {

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
     * 诊室编号
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

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
    
    
}
