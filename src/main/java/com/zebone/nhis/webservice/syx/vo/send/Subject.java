package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("subject")
public class Subject {
	@XStreamAsAttribute
	private String typeCode;

	@XStreamAsAttribute
	private String contextConductionInd;
	
	@XStreamAsAttribute
	private String XSI_NIL;
	
	private RegistrationRequest registrationRequest ;
	
	private ProcedureRequest procedureRequest;
	
	private Patient patient;
	
	private PatientPerson patientPerson;
	
	private RegistrationEvent registrationEvent;
	
	private PlacerGroup placerGroup;
	
	private EncounterEvent encounterEvent;
	@XStreamAlias("encounterEvent")
	private EncounterEventVo encounterEventVo;
	
	private ObservationRequest observationRequest;
	
	private Schedule schedule;
	
	private EncounterAppointment encounterAppointment;
	
	private SpecimenInContainer specimenInContainer;
	
	public String getXSI_NIL() {
		return XSI_NIL;
	}

	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}

	public EncounterAppointment getEncounterAppointment() {
		if(encounterAppointment == null) {
			encounterAppointment = new EncounterAppointment();
		}
		return encounterAppointment;
	}

	public void setEncounterAppointment(EncounterAppointment encounterAppointment) {
		this.encounterAppointment = encounterAppointment;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public RegistrationRequest getRegistrationRequest() {
		if(registrationRequest==null)registrationRequest=new RegistrationRequest();
		return registrationRequest;
	}

	public void setRegistrationRequest(RegistrationRequest registrationRequest) {
		this.registrationRequest = registrationRequest;
	}

	public ProcedureRequest getProcedureRequest() {
		return procedureRequest;
	}

	public void setProcedureRequest(ProcedureRequest procedureRequest) {
		this.procedureRequest = procedureRequest;
	}

	public Patient getPatient() {
		if(patient==null) patient=new Patient();
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public PatientPerson getPatientPerson() {
		if(patientPerson==null) patientPerson=new PatientPerson();
		return patientPerson;
	}

	public void setPatientPerson(PatientPerson patientPerson) {
		this.patientPerson = patientPerson;
	}

	public String getContextConductionInd() {
		return contextConductionInd;
	}

	public void setContextConductionInd(String contextConductionInd) {
		this.contextConductionInd = contextConductionInd;
	}

	public RegistrationEvent getRegistrationEvent() {
		if(registrationEvent==null) registrationEvent=new RegistrationEvent();
		return registrationEvent;
	}

	public void setRegistrationEvent(RegistrationEvent registrationEvent) {
		this.registrationEvent = registrationEvent;
	}

	public PlacerGroup getPlacerGroup() {
		if(placerGroup==null)placerGroup=new PlacerGroup();
		return placerGroup;
	}

	public void setPlacerGroup(PlacerGroup placerGroup) {
		this.placerGroup = placerGroup;
	}

	public EncounterEvent getEncounterEvent() {
		if(encounterEvent==null)encounterEvent=new EncounterEvent();
		return encounterEvent;
	}

	public void setEncounterEvent(EncounterEvent encounterEvent) {
		this.encounterEvent = encounterEvent;
	}

	public ObservationRequest getObservationRequest() {
		if(observationRequest==null)observationRequest=new ObservationRequest();
		return observationRequest;
	}

	public void setObservationRequest(ObservationRequest observationRequest) {
		this.observationRequest = observationRequest;
	}

	public Schedule getSchedule() {
		if(schedule == null) {
			schedule = new Schedule();
		}
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public SpecimenInContainer getSpecimenInContainer() {
		if(specimenInContainer == null )specimenInContainer =new SpecimenInContainer();
		return specimenInContainer;
	}

	public void setSpecimenInContainer(SpecimenInContainer specimenInContainer) {
		this.specimenInContainer = specimenInContainer;
	}

	public EncounterEventVo getEncounterEventVo() {
		if(encounterEventVo == null) encounterEventVo = new EncounterEventVo();
		return encounterEventVo;
	}

	public void setEncounterEventVo(EncounterEventVo encounterEventVo) {
		this.encounterEventVo = encounterEventVo;
	}
	
	
	
}
