package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("encounterEvent")
public class EncounterEventVo {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	
	private Id id;
	private Code code;
	private String statusCode;
	private EffectiveTime effectiveTime;
	private ReasonCode reasonCode;
	private AdmissionReferralSourceCode admissionReferralSourceCode;
	private LengthOfStayQuantity lengthOfStayQuantity;
	private Subject subject;
	private Discharger discharger;
	private Reason reason;
	private DepartedBy departedBy;
	private Admitter admitter;
	private Location location;
	private Location1 location1;
	private Location2 location2;
	@XStreamImplicit
	private List<Reason> reasons;
	
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
	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public Code getCode() {
		if(code==null)code=new Code();
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public EffectiveTime getEffectiveTime() {
		if(effectiveTime==null)effectiveTime=new EffectiveTime();
		return effectiveTime;
	}
	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public ReasonCode getReasonCode() {
		if(reasonCode==null)reasonCode=new ReasonCode();
		return reasonCode;
	}
	public void setReasonCode(ReasonCode reasonCode) {
		this.reasonCode = reasonCode;
	}
	public AdmissionReferralSourceCode getAdmissionReferralSourceCode() {
		if(admissionReferralSourceCode==null)admissionReferralSourceCode=new AdmissionReferralSourceCode();
		return admissionReferralSourceCode;
	}
	public void setAdmissionReferralSourceCode(
			AdmissionReferralSourceCode admissionReferralSourceCode) {
		this.admissionReferralSourceCode = admissionReferralSourceCode;
	}
	public Subject getSubject() {
		if(subject==null)subject=new Subject();
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public Discharger getDischarger() {
		if(discharger==null) discharger=new Discharger();
		return discharger;
	}
	public void setDischarger(Discharger discharger) {
		this.discharger = discharger;
	}
	
	public Reason getReason() {
		if(reason==null)reason=new Reason();
		return reason;
	}
	public void setReason(Reason reason) {
		this.reason = reason;
	}
	
	public DepartedBy getDepartedBy() {
		if(departedBy == null) {
			departedBy = new DepartedBy();
		}
		return departedBy;
	}
	public void setDepartedBy(DepartedBy departedBy) {
		this.departedBy = departedBy;
	}
	public Admitter getAdmitter() {
		if(admitter==null)admitter=new Admitter();
		return admitter;
	}
	public void setAdmitter(Admitter admitter) {
		this.admitter = admitter;
	}
	public Location getLocation() {
		if(location==null)location=new Location();
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Location1 getLocation1() {
		if(location1==null) location1=new Location1();
		return location1;
	}
	public void setLocation1(Location1 location1) {
		this.location1 = location1;
	}
	public Location2 getLocation2() {
		if(location2==null)location2=new Location2();
		return location2;
	}
	public void setLocation2(Location2 location2) {
		this.location2 = location2;
	}
	public LengthOfStayQuantity getLengthOfStayQuantity() {
		if(lengthOfStayQuantity==null)lengthOfStayQuantity=new LengthOfStayQuantity();
		return lengthOfStayQuantity;
	}
	public void setLengthOfStayQuantity(LengthOfStayQuantity lengthOfStayQuantity) {
		this.lengthOfStayQuantity = lengthOfStayQuantity;
	}
	public List<Reason> getReasons() {
		if(reasons==null)reasons=new ArrayList<Reason>();
		return reasons;
	}
	public void setReasons(List<Reason> reasons) {
		this.reasons = reasons;
	}
	
}
