package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("substanceAdministrationRequest")
public class SubstanceAdministrationRequest {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moddCode;
	
	private Id id;
	
	private Code code;
	
	private Text text;
	
	private StatusCode statusCode;
	
	private EffectiveTime effectiveTime;
	
	private RouteCode routeCode;
	
	private DoseQuantity doseQuantity;
	
	private DoseCheckQuantity doseCheckQuantity;
	
	private AdministrationUnitCode administrationUnitCode;
	
	private Consumable2 consumable2;
	
	private Location location;
	
	private OccurrenceOf occurrenceOf;
	
	private PertinentInformation pertinentInformation;
	
	private Component2 component2;
	
	private SubjectOf6 subjectOf6;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getModdCode() {
		return moddCode;
	}

	public void setModdCode(String moddCode) {
		this.moddCode = moddCode;
	}

	public Id getId() {
		if(id==null) id=new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Code getCode() {
		if(code==null) code=new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Text getText() {
		if(text==null)text=new Text();
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public StatusCode getStatusCode() {
		if(statusCode==null) statusCode=new StatusCode();
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public EffectiveTime getEffectiveTime() {
		if(effectiveTime==null) effectiveTime=new EffectiveTime();
		return effectiveTime;
	}

	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public RouteCode getRouteCode() {
		if(routeCode==null) routeCode=new RouteCode();
		return routeCode;
	}

	public void setRouteCode(RouteCode routeCode) {
		this.routeCode = routeCode;
	}

	public DoseQuantity getDoseQuantity() {
		if(doseQuantity==null) doseQuantity=new DoseQuantity();
		return doseQuantity;
	}

	public void setDoseQuantity(DoseQuantity doseQuantity) {
		this.doseQuantity = doseQuantity;
	}

	public DoseCheckQuantity getDoseCheckQuantity() {
		if(doseCheckQuantity==null) doseCheckQuantity=new DoseCheckQuantity();
		return doseCheckQuantity;
	}

	public void setDoseCheckQuantity(DoseCheckQuantity doseCheckQuantity) {
		this.doseCheckQuantity = doseCheckQuantity;
	}

	public AdministrationUnitCode getAdministrationUnitCode() {
		if(administrationUnitCode==null) administrationUnitCode=new AdministrationUnitCode();
		return administrationUnitCode;
	}

	public void setAdministrationUnitCode(
			AdministrationUnitCode administrationUnitCode) {
		this.administrationUnitCode = administrationUnitCode;
	}

	public Consumable2 getConsumable2() {
		if(consumable2==null) consumable2=new Consumable2();
		return consumable2;
	}

	public void setConsumable2(Consumable2 consumable2) {
		this.consumable2 = consumable2;
	}

	public Location getLocation() {
		if(location==null)location=new Location();
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public OccurrenceOf getOccurrenceOf() {
		if(occurrenceOf==null) occurrenceOf=new OccurrenceOf();
		return occurrenceOf;
	}

	public void setOccurrenceOf(OccurrenceOf occurrenceOf) {
		this.occurrenceOf = occurrenceOf;
	}

	public PertinentInformation getPertinentInformation() {
		if(pertinentInformation==null) pertinentInformation=new PertinentInformation();
		return pertinentInformation;
	}

	public void setPertinentInformation(PertinentInformation pertinentInformation) {
		this.pertinentInformation = pertinentInformation;
	}

	public Component2 getComponent2() {
		if(component2==null) component2=new Component2();
		return component2;
	}

	public void setComponent2(Component2 component2) {
		this.component2 = component2;
	}

	public SubjectOf6 getSubjectOf6() {
		if(subjectOf6==null) subjectOf6=new SubjectOf6();
		return subjectOf6;
	}

	public void setSubjectOf6(SubjectOf6 subjectOf6) {
		this.subjectOf6 = subjectOf6;
	}
	
	
}
