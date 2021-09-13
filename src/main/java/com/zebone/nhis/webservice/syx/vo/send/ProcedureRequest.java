package com.zebone.nhis.webservice.syx.vo.send;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("procedureRequest")
public class ProcedureRequest {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	private Id id;
	private Code code;
	private Text text;
	private StatusCode statusCode;
	private EffectiveTime effectiveTime;
	@XStreamAsAttribute
	private String XSI_TYPE;
	private Low low;
	private MethodCode methodCode;
	private Author author;
	private Verifier verifier;
	private PertinentInformation pertinentInformation;
	private PriorityCode priorityCode;
	private Performer performer;
	@XStreamImplicit
	private List<Component2> component2;
	private SubjectOf6 subjectOf6;
	private ComponentOf1 componentOf1;
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
	public Text getText() {
		if(text==null) text=new Text();
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public StatusCode getStatusCode() {
		if(statusCode==null)statusCode=new StatusCode();
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public EffectiveTime getEffectiveTime() {
		if(effectiveTime==null)effectiveTime=new EffectiveTime();
		return effectiveTime;
	}
	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public String getXSI_TYPE() {
		return XSI_TYPE;
	}
	public void setXSI_TYPE(String xSI_TYPE) {
		XSI_TYPE = xSI_TYPE;
	}
	public Low getLow() {
		if(low==null) low=new Low();
		return low;
	}
	public void setLow(Low low) {
		this.low = low;
	}
	public MethodCode getMethodCode() {
		if(methodCode==null)methodCode=new MethodCode();
		return methodCode;
	}
	public void setMethodCode(MethodCode methodCode) {
		this.methodCode = methodCode;
	}
	public Author getAuthor() {
		if(author==null)author=new Author();
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Verifier getVerifier() {
		if(verifier==null)verifier=new Verifier();
		return verifier;
	}
	public void setVerifier(Verifier verifier) {
		this.verifier = verifier;
	}
	public PriorityCode getPriorityCode() {
		if(priorityCode==null)priorityCode=new PriorityCode();
		return priorityCode;
	}
	public void setPriorityCode(PriorityCode priorityCode) {
		this.priorityCode = priorityCode;
	}
	public Performer getPerformer() {
		if(performer==null) performer=new Performer();
		return performer;
	}
	public void setPerformer(Performer performer) {
		this.performer = performer;
	}
	
	public List<Component2> getComponent2() {
		if(component2==null) component2=new ArrayList<Component2>();
		return component2;
	}
	public void setComponent2(List<Component2> component2) {
		this.component2 = component2;
	}
	public SubjectOf6 getSubjectOf6() {
		if(subjectOf6==null) subjectOf6=new SubjectOf6();
		return subjectOf6;
	}
	public void setSubjectOf6(SubjectOf6 subjectOf6) {
		this.subjectOf6 = subjectOf6;
	}
	public ComponentOf1 getComponentOf1() {
		if(componentOf1==null) componentOf1=new ComponentOf1();
		return componentOf1;
	}
	public void setComponentOf1(ComponentOf1 componentOf1) {
		this.componentOf1 = componentOf1;
	}
	public PertinentInformation getPertinentInformation() {
		if(pertinentInformation==null)pertinentInformation = new PertinentInformation();
		return pertinentInformation;
	}
	public void setPertinentInformation(PertinentInformation pertinentInformation) {
		this.pertinentInformation = pertinentInformation;
	}
	
}
