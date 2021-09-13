package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("resourceSlot")
public class ResourceSlot {
	
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	
	private Id id;
	
	private Profession profession;
	
	private TotalNumber totalNumber;
	
	private DeptId deptId;
	
	private Code code;
    
	private StatusCode statusCode;
	
	private EffectiveTime effectiveTime;
	
	private PriorityCode priorityCode;
	
	private DirectTarget directTarget;
	
	private TimeFrame timeFrame;
	
	public TimeFrame getTimeFrame() {
		if(timeFrame == null) {
			timeFrame = new TimeFrame(); 
		}
		
		return timeFrame;
	}
	public void setTimeFrame(TimeFrame timeFrame) {
		this.timeFrame = timeFrame;
	}
	public Code getCode() {
		if(code == null) {
			code = new Code();
		}
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public StatusCode getStatusCode() {
		if(statusCode == null) {
			statusCode = new StatusCode();
		}
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public EffectiveTime getEffectiveTime() {
		if(effectiveTime == null) {
			effectiveTime = new EffectiveTime();
		}
		
		return effectiveTime;
	}
	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public PriorityCode getPriorityCode() {
		if(priorityCode == null) {
			priorityCode = new PriorityCode();
		}
		
		return priorityCode;
	}
	public void setPriorityCode(PriorityCode priorityCode) {
		this.priorityCode = priorityCode;
	}
	public DirectTarget getDirectTarget() {
		if(directTarget == null) {
			directTarget = new DirectTarget();
		}
		return directTarget;
	}
	public void setDirectTarget(DirectTarget directTarget) {
		this.directTarget = directTarget;
	}
	public DeptId getDeptId() {
		if(deptId == null) {
			deptId = new DeptId();
		}
		return deptId;
	}
	public void setDeptId(DeptId deptId) {
		this.deptId = deptId;
	}
	
	public TotalNumber getTotalNumber() {
		if(totalNumber == null) {
			totalNumber = new TotalNumber();
		}		
		return totalNumber;
	}
	public void setTotalNumber(TotalNumber totalNumber) {
		this.totalNumber = totalNumber;
	}
	public Profession getProfession() {
		if(profession == null) {
			profession = new Profession();
		}
		return profession;
	}
	public void setProfession(Profession profession) {
		this.profession = profession;
	}
	public Id getId() {
		if(id==null) {
			id = new Id();
		}
		
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getMoodCode() {
		return moodCode;
	}
	public void setMoodCode(String moodCode) {
		this.moodCode = moodCode;
	}
	
	

}
