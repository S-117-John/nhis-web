package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("reusableDevice")
public class ReusableDevice {
    private Time time;
    
    private AssignedDevice assignedDevice;

	public Time getTime() {
		if(time == null) {
			time = new Time();
		}
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public AssignedDevice getAssignedDevice() {
		if(assignedDevice == null) {
			assignedDevice = new AssignedDevice();
		}
		return assignedDevice;
	}

	public void setAssignedDevice(AssignedDevice assignedDevice) {
		this.assignedDevice = assignedDevice;
	}
    
    
}
