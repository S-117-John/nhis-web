package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("observationRequest")
public class ObservationRequest {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	
	private Id id;
	private Code code;
	private String orderno;
	private Text text;
	private StatusCode statusCode;
	private EffectiveTime effectiveTime;
	private MethodCode methodCode;
	private PriorityCode priorityCode;
	private Specimen specimen;
	private Author author;
	private Verifier verifier;
	private Reason reason;
	private Component2 component2;
	private SubjectOf6 subjectOf6;
	private ComponentOf1 componentOf1;
	
	private TargetSiteCode targetSiteCode;
	private Location location;
	
	private Component1 component1;
	
	
		
	public Component1 getComponent1() {
		return component1;
	}
	
	public void setComponent1(Component1 component1) {
		this.component1 = component1;
	}
	public Reason getReason() {
		if(reason == null) {
			reason = new Reason();
		}
		return reason;
	}
	public void setReason(Reason reason) {
		this.reason = reason;
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
	public Id getId() {
		if(id==null)id = new Id();
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public Code getCode() {
		if(code==null)code = new Code();
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public Text getText() {
		if(text == null)text = new Text();
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public StatusCode getStatusCode() {
		if(statusCode == null)statusCode = new StatusCode();
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public EffectiveTime getEffectiveTime() {
		if(effectiveTime == null)effectiveTime = new EffectiveTime();
		return effectiveTime;
	}
	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public PriorityCode getPriorityCode() {
		if(priorityCode == null)priorityCode = new PriorityCode();
		return priorityCode;
	}
	public void setPriorityCode(PriorityCode priorityCode) {
		this.priorityCode = priorityCode;
	}
	public Specimen getSpecimen() {
		if(specimen==null)specimen=new Specimen();
		return specimen;
	}
	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}
	public Author getAuthor() {
		if(author==null)author = new Author();
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Verifier getVerifier() {
		if(verifier==null)verifier= new Verifier();
		return verifier;
	}
	public void setVerifier(Verifier verifier) {
		this.verifier = verifier;
	}
	public Component2 getComponent2() {
		if(component2==null)component2 = new Component2();
		return component2;
	}
	public void setComponent2(Component2 component2) {
		this.component2 = component2;
	}
	public SubjectOf6 getSubjectOf6() {
		if(subjectOf6 == null)subjectOf6=new SubjectOf6();
		return subjectOf6;
	}
	public void setSubjectOf6(SubjectOf6 subjectOf6) {
		this.subjectOf6 = subjectOf6;
	}
	public ComponentOf1 getComponentOf1() {
		if(componentOf1 == null)componentOf1 = new ComponentOf1();
		return componentOf1;
	}
	public void setComponentOf1(ComponentOf1 componentOf1) {
		this.componentOf1 = componentOf1;
	}
	public MethodCode getMethodCode() {
		if(methodCode==null)methodCode=new MethodCode();
		return methodCode;
	}
	public void setMethodCode(MethodCode methodCode) {
		this.methodCode = methodCode;
	}
	public Location getLocation() {
		if(location==null)location=new Location();
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public TargetSiteCode getTargetSiteCode() {
		if(targetSiteCode==null)targetSiteCode = new TargetSiteCode();
		return targetSiteCode;
	}
	public void setTargetSiteCode(TargetSiteCode targetSiteCode) {
		this.targetSiteCode = targetSiteCode;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	
	
}
