package com.zebone.nhis.ma.pub.platform.zb.model;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v21.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.CX;
import ca.uhn.hl7v2.model.v24.datatype.IS;
import ca.uhn.hl7v2.model.v24.datatype.NM;
import ca.uhn.hl7v2.model.v24.datatype.PL;
import ca.uhn.hl7v2.model.v24.datatype.SI;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 *<p>Represents an HL7 ZMI message segment 
 * This segment has the following fields:</p>
 * <ul>
     * <li>ZMI-1: Set ID - ZMI (SI) <b>optional </b>
     * <li>ZMI-2: Diagnosis Date/Time (TS) <b> </b>
     * <li>ZMI-3: Transfre Dept (PL) <b>optional </b>
     * <li>ZMI-4: Discharge Dept (PL) <b>optional </b>
     * <li>ZMI-5: Discharge Ward (PL) <b>optional </b>
     * 
     * <li>ZMI-6: Length of Inpatient Stay (NM) <b>optional </b>
     * <li>ZMI-7: Re Admission Indicator (IS) <b>optional repeating</b>
     * <li>ZMI-8: Transfer Reason (CE) <b>optional repeating</b>
     * <li>ZMI-9: Actual Length of Inpatient Stay (NM) <b>optional repeating</b>
     * <li>ZMI-10: Dept Director (XCN) <b>optional </b>
     * 
     * <li>ZMI-11: Chief Doctor (XCN) <b>optional </b>
     * <li>ZMI-12: Doctor in Charge of A Case (XCN) <b>optional </b>
     * <li>ZMI-13: Refer Physician Doctor (XCN) <b>optional </b>
     * <li>ZMI-14: Primary Nurse (XCN) <b>optional </b>
     * <li>ZMI-15: Learn Doctor (XCN) <b>optional </b>
     * 
     * <li>ZMI-16: Practice Doctor (XCN) <b>optional </b>
     * <li>ZMI-17: Encoder Doctor (XCN) <b>optional </b>
     * <li>ZMI-18: Quality Control Doctor (XCN) <b>optional </b>
     * <li>ZMI-19: Quality Control Nurse (XCN) <b>optional </b>
     * <li>ZMI-20: Pathology Diagnosis Code - DG1 (CE) <b>optional </b>
     * 
     * <li>ZMI-21: Practice Doctor (XCN) <b>optional </b>
     * <li>ZMI-22: Encoder Doctor (XCN) <b>optional </b>
     * <li>ZMI-23: Quality Control Doctor (XCN) <b>optional </b>
     * <li>ZMI-24: Quality Control Nurse (XCN) <b>optional </b>
     * <li>ZMI-25: Pathology Diagnosis Code - DG1 (CE) <b>optional </b>     
     * 
     * <li>ZMI-26: Blood Type (ST) <b>optional </b>
     * <li>ZMI-27: RH Type (ST) <b>optional </b>
     * <li>ZMI-28: Medical Records Management (ST) <b>optional </b>
     * <li>ZMI-29: Admiss Road (ST) <b>optional </b>
     * <li>ZMI-30: Before Admiss Stupor Days (ST) <b>optional </b>     
     *  
     * <li>ZMI-31: Before Admiss Stupor Hours (ST) <b>optional </b>
     * <li>ZMI-32: Before Admiss Stupor Minutes (ST) <b>optional </b>
     * <li>ZMI-33: After Admiss Stupor Days (ST) <b>optional </b>
     * <li>ZMI-34: After Admiss Stupor Hours (ST) <b>optional </b>
     * <li>ZMI-35: After Admiss Stupor Minutes (ST) <b>optional </b>     
     *  
     * <li>ZMI-36: Profession (ST) <b>optional </b>
     * <li>ZMI-37: Case Class (ST) <b>optional </b>
     * <li>ZMI-38: Clinical Pathway Case (ST) <b>optional </b>
     * <li>ZMI-39: Rescue Times (ST) <b>optional </b>
     * <li>ZMI-40: Rescue Success Times (ST) <b>optional </b>
     * 
     * <li>ZMI-41: Autopsy Of Death (ST) <b>optional </b>
     * <li>ZMI-42: Quality Control Time (TS) <b>optional </b>
     * <li>ZMI-43: Treatment Category (ST) <b>optional </b>
     * <li>ZMI-44: Homemade Preparation CM(ST) <b>optional </b>
     * <li>ZMI-45: Way Out Of The Hospital (ST) <b>optional </b>
     * 
     * <li>ZMI-46: Receive Medical Name2 (ST) <b>optional </b>
     * <li>ZMI-47: Receive Medical Name3 (ST) <b>optional </b>
     * <li>ZMI-48: Purpose 31 Days Of Discharge (ST) <b>optional </b>
     * <li>ZMI-49: Clinical Diagnosis Code (ST) <b>optional </b>
     * <li>ZMI-50: Clinical Diagnosis Name (ST) <b>optional </b>
     * 
     * <li>ZMI-51: Clinical Doctor (ST) <b>optional </b>
     * <li>ZMI-52: Clinical Diagnosis Name CM (ST) <b>optional </b>
     * <li>ZMI-53: Birth Weight Of Newborns (ST) <b>optional </b>
     * <li>ZMI-54: Hospitalized Weight Of Newborns (ST) <b>optional </b>
     * <li>ZMI-55: Medical Institution (ST) <b>optional </b>
     * 
     * <li>ZMI-56: Medical Institution Code (ST) <b>optional </b>
     * <li>ZMI-57: Medical Pay Mode (ST) <b>optional </b>
     * <li>ZMI-58: Medical Certificate Number (ST) <b>optional </b>
     * 
 * </ul>
 */
/**
 * 病案附加信息自定义段
 * @author chengjia
 *
 */
@SuppressWarnings("unused")
public class ZMI extends AbstractSegment {
	public ZMI(Group parent, ModelClassFactory factory) {
	    super(parent, factory);
	    
	    // By convention, an init() method is created which adds
	    // the specific fields to this segment class
	    init(factory);
	 }
	
	 private void init(ModelClassFactory factory) {
	    try {
	    	/*
	    	 * For each field in the custom segment, the add() method is
	    	 * called once. In this example, there are two fields in
	    	 * the ZMI segment.
	    	 * 
	    	 * See here for information on the arguments to this method:
	    	 * http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/model/AbstractSegment.html#add%28java.lang.Class,%20boolean,%20int,%20int,%20java.lang.Object[],%20java.lang.String%29
	    	 */
	    	this.add(SI.class, false, 1, 4, new Object[]{ getMessage() }, "Set ID - ZMI");
	    	this.add(TS.class, false, 1, 26, new Object[]{ getMessage() }, "Diagnosis Date Time");
	    	this.add(PL.class, false, 1, 80, new Object[]{ getMessage() }, "Transfre Dept");
	    	this.add(PL.class, false, 1, 80, new Object[]{ getMessage() }, "Discharge Dept");
	    	this.add(PL.class, false, 1, 80, new Object[]{ getMessage() }, "Discharge Ward");
	    	
	    	this.add(NM.class, false, 0, 3, new Object[]{ getMessage() }, "Length of Inpatient Stay");
	    	this.add(IS.class, false, 1, 2, new Object[]{ getMessage(), new Integer(78) }, "Re Admission Indicator");
	    	this.add(CE.class, true, 1, 250, new Object[]{ getMessage() }, "Transfer Reason");
	    	this.add(NM.class, false, 0, 3, new Object[]{ getMessage() }, "Actual Length of Inpatient Stay");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Dept Director");
	    	
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Chief Doctor");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Doctor in Charge of A Case");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Refer Physician Doctor");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Primary Nurse");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Learn Doctor");
	    	
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Practice Doctor");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Encoder Doctor");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Quality Control Doctor");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Quality Control Nurse");
	    	this.add(CE.class, true, 1, 250, new Object[]{ getMessage() }, "Pathology Diagnosis Code - DG1");
	    	
	    	this.add(SI.class, false, 1, 40, new Object[]{ getMessage() }, "Pathology Diagnosis Description");
	    	this.add(CX.class, false, 1, 250, new Object[]{ getMessage() }, "Pathology Number");
	    	this.add(CE.class, true, 1, 250, new Object[]{ getMessage() }, "Scathing Diagnosis Code - DG1");
	    	this.add(SI.class, false, 1, 40, new Object[]{ getMessage() }, "Scathing Pathology Diagnosis Description");
	    	this.add(SI.class, false, 1, 16, new Object[]{ getMessage() }, "SSN Number - Patient");
	    	
	    	this.add(ST.class, false, 1, 2, new Object[]{ getMessage() }, "Blood Type");
	    	this.add(ST.class, false, 1, 2, new Object[]{ getMessage() }, "RH Type");
	    	this.add(ST.class, false, 1, 1, new Object[]{ getMessage() }, "Medical Records Management");
	    	this.add(ST.class, false, 1, 1, new Object[]{ getMessage() }, "Admiss Road");
	    	this.add(ST.class, false, 1, 3, new Object[]{ getMessage() }, "Before Admiss Stupor Days");
	    	
	    	this.add(ST.class, false, 1, 2, new Object[]{ getMessage() }, "Before Admiss Stupor Hours");
	    	this.add(ST.class, false, 1, 2, new Object[]{ getMessage() }, "Before Admiss Stupor Minutes");
	    	this.add(ST.class, false, 1, 3, new Object[]{ getMessage() }, "After Admiss Stupor Days");
	    	this.add(ST.class, false, 1, 2, new Object[]{ getMessage() }, "After Admiss Stupor Hours");
	    	this.add(ST.class, false, 1, 2, new Object[]{ getMessage() }, "After Admiss Stupor Minutes");
	    	
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "Profession");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Case Class");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Clinical Pathway Case");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Rescue Times");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Rescue Success Times");
	    	
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Autopsy Of Death");
	    	this.add(TS.class, false, 1, 26, new Object[]{ getMessage() }, "Quality Control Time");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Treatment Category");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Homemade Preparation CM");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Way Out Of The Hospital");
	    	
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Receive Medical Name2");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Receive Medical Name3");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Purpose 31 Days Of Discharge");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Clinical Diagnosis Code");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Clinical Diagnosis Name");
	    	
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Clinical Doctor");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Clinical Diagnosis Name CM");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Birth Weight Of Newborns");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Hospitalized Weight Of Newborns");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Medical Institution");
	    	
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Medical Institution Code");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Medical Pay Mode");
	    	this.add(ST.class, false, 1, 80, new Object[]{ getMessage() }, "Medical Certificate Number");

	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZMI - this is probably a bug in the source code generator.", e);
	    }
	 }
	
	    /**
	     * Returns
	     * ZMI-1: "Set ID" - creates it if necessary
	     */
	    public SI getSetIDZMI() { 
			SI retVal = this.getTypedField(1, 0);
			return retVal;
	    }

	    
	    /**
	     * Returns
	     * ZMI-2: "Diagnosis Date/Time " - creates it if necessary
	     */
	    public TS getDiagnosisDateTime () { 
			TS retVal = this.getTypedField(2, 0);
			return retVal;
	    }



	    /**
	     * Returns
	     * ZMI-3: "Transfre Dept" - creates it if necessary
	     */
	    public PL getTransfreDept() { 
	    	PL retVal = this.getTypedField(3, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-4: "Discharge Dept"" - creates it if necessary
	     */
	    public PL getDischargeDept() { 
	    	PL retVal = this.getTypedField(4, 0);
			return retVal;
	    }

	    /**
	     * Returns
	     * ZMI-5: "Discharge Ward" - creates it if necessary
	     */
	    public PL getDischargeWard() { 
	    	PL retVal = this.getTypedField(5, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-6: "Length of Inpatient Stay" - creates it if necessary
	     */
	    public NM getLengthOfInpatientStay() { 
	    	NM retVal = this.getTypedField(6, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZMI-7: "Re Admission Indicator " - creates it if necessary
	     */
	    public IS getReAdmissionIndicator () { 
	    	IS retVal = this.getTypedField(7, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-8: "Transfer Reason" - creates it if necessary
	     */
	    public CE getTransferReason() { 
			CE retVal = this.getTypedField(8, 0);
			return retVal;
	    }
	    

	    /**
	     * Returns 
	     * ZMI-9: "Actual Length of Inpatient Stay" - creates it if necessary
	     */
	    public NM getActualLengthOfInpatientStay() { 
	    	NM retVal = this.getTypedField(9, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZMI-10: "Dept Director" - creates it if necessary
	     */
	    public XCN getDeptDirector() { 
	    	XCN retVal = this.getTypedField(10, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZMI-11: "Chief Doctor" - creates it if necessary
	     */
	    public XCN getChiefDoctor() { 
	    	XCN retVal = this.getTypedField(11, 0);
			return retVal;
	    }

	    /**
	     * Returns
	     * ZMI-12: "Doctor in Charge of A Case" - creates it if necessary
	     */
	    public XCN getDoctorInChargeOfACase() { 
	    	XCN retVal = this.getTypedField(12, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-13: "Refer Physician Doctor" - creates it if necessary
	     */
	    public XCN getReferPhysicianDoctor() { 
	    	XCN retVal = this.getTypedField(13, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-14: "Primary Nurse" - creates it if necessary
	     */
	    public XCN getPrimaryNurse() { 
	    	XCN retVal = this.getTypedField(14, 0);
			return retVal;
	    }
	    /**
	     * Returns
	     * ZMI-15: "Learn Doctor" - creates it if necessary
	     */
	    public XCN getLearnDoctor() { 
	    	XCN retVal = this.getTypedField(15, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-16: "Practice Doctor" - creates it if necessary
	     */
	    public XCN getPracticeDoctor() { 
	    	XCN retVal = this.getTypedField(16, 0);
			return retVal;
	    }
	    
	    
	    /**
	     * Returns
	     * ZMI-17: "Encoder Doctor" - creates it if necessary
	     */
	    public XCN getEncoderDoctor() { 
	    	XCN retVal = this.getTypedField(17, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-18: "Quality Control Doctor" - creates it if necessary
	     */
	    public XCN getQualityControlDoctor() { 
	    	XCN retVal = this.getTypedField(18, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-19: "Quality Control Nurse" - creates it if necessary
	     */
	    public XCN getQualityControlNurse() { 
	    	XCN retVal = this.getTypedField(19, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-20: "Pathology Diagnosis Code - DG1 " - creates it if necessary
	     */
	    public CE getPathologyDiagnosisCodeDG1() { 
	    	CE retVal = this.getTypedField(20, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-21: "Pathology Diagnosis Description " - creates it if necessary
	     */
	    public ST getPathologyDiagnosisDescription() { 
	    	ST retVal = this.getTypedField(21, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-22: "Pathology Number" - creates it if necessary
	     */
	    public CX getPathologyNumber() { 
	    	CX retVal = this.getTypedField(22, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-23: "Scathing Diagnosis Code - DG1 " - creates it if necessary
	     */
	    public CE getScathingDiagnosisCodeDG1() { 
	    	CE retVal = this.getTypedField(23, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-24: "Scathing Pathology Diagnosis Description" - creates it if necessary
	     */
	    public CE getScathingPathologyDiagnosisDescription() { 
	    	CE retVal = this.getTypedField(24, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-25: "SSN Number - Patient " - creates it if necessary
	     */
	    public ST getSSNNumberPatient() { 
	    	ST retVal = this.getTypedField(25, 0);
			return retVal;
	    }	    
	    
	    /**
	     * Returns
	     * ZMI-26: "Blood Type " - creates it if necessary
	     */
	    public ST getBloodType() { 
	    	ST retVal = this.getTypedField(26, 0);
			return retVal;
	    }	
	    /**
	     * Returns
	     * ZMI-27: "RH Type " - creates it if necessary
	     */
	    public ST getRHType() { 
	    	ST retVal = this.getTypedField(27, 0);
			return retVal;
	    }	
	    /**
	     * Returns
	     * ZMI-28: "Medical Records Management" - creates it if necessary
	     */
	    public ST getMedicalRecordsManagement() { 
	    	ST retVal = this.getTypedField(28, 0);
			return retVal;
	    }	

	    /**
	     * Returns
	     * ZMI-29: "Admiss Road " - creates it if necessary
	     */
	    public ST getAdmissRoad() { 
	    	ST retVal = this.getTypedField(29, 0);
			return retVal;
	    }	
	    /**
	     * Returns
	     * ZMI-30: "Before Admiss Stupor Days" - creates it if necessary
	     */
	    public ST getBeforeAdmissStuporDays() { 
	    	ST retVal = this.getTypedField(30, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-31: "Before Admiss Stupor Hours" - creates it if necessary
	     */
	    public ST getBeforeAdmissStuporHours() { 
	    	ST retVal = this.getTypedField(31, 0);
			return retVal;
	    }	
	    /**
	     * Returns
	     * ZMI-32: "Before Admiss Stupor Minutes" - creates it if necessary
	     */
	    public ST getBeforeAdmissStuporMinutes() { 
	    	ST retVal = this.getTypedField(32, 0);
			return retVal;
	    }	
	    /**
	     * Returns
	     * ZMI-33: "After Admiss Stupor Days" - creates it if necessary
	     */
	    public ST getAfterAdmissStuporDays() { 
	    	ST retVal = this.getTypedField(33, 0);
			return retVal;
	    }	
	    /**
	     * Returns
	     * ZMI-34: "After Admiss Stupor Hours" - creates it if necessary
	     */
	    public ST getAfterAdmissStuporHours() { 
	    	ST retVal = this.getTypedField(34, 0);
			return retVal;
	    }	
	    /**
	     * Returns
	     * ZMI-35: "After Admiss Stupor Minutes" - creates it if necessary
	     */
	    public ST getAfterAdmissStuporMinutes() { 
	    	ST retVal = this.getTypedField(35, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-36: "Profession" - creates it if necessary
	     */
	    public ST getProfession() { 
	    	ST retVal = this.getTypedField(36, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-37: "Case Class" - creates it if necessary
	     */
	    public ST getCaseClass() { 
	    	ST retVal = this.getTypedField(37, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-38: "Clinical Pathway Case" - creates it if necessary
	     */
	    public ST getClinicalPathwayCase() { 
	    	ST retVal = this.getTypedField(38, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-39: "Rescue Times" - creates it if necessary
	     */
	    public ST getRescueTimes() { 
	    	ST retVal = this.getTypedField(39, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-40: "Rescue Success Times" - creates it if necessary
	     */
	    public ST getRescueSuccessTimes() { 
	    	ST retVal = this.getTypedField(40, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-41: "Autopsy Of Death" - creates it if necessary
	     */
	    public ST getAutopsyOfDeath() { 
	    	ST retVal = this.getTypedField(41,0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-42: "Quality Control Time" - creates it if necessary
	     */
	    public TS getQualityControlTime() { 
	    	TS retVal = this.getTypedField(42, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-43: "Treatment Category" - creates it if necessary
	     */
	    public ST getTreatmentCategory() { 
	    	ST retVal = this.getTypedField(43, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-44: "Profession" - creates it if necessary
	     */
	    public ST getHomemadePreparationCM() { 
	    	ST retVal = this.getTypedField(44, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-45: "Profession" - creates it if necessary
	     */
	    public ST getWayOutOfTheHospital() { 
	    	ST retVal = this.getTypedField(45, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-46: "Receive Medical Name2" - creates it if necessary
	     */
	    public ST getReceiveMedicalName2() { 
	    	ST retVal = this.getTypedField(46, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-47: "Profession" - creates it if necessary
	     */
	    public ST getReceiveMedicalName3() { 
	    	ST retVal = this.getTypedField(47, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-48: "Purpose 31 Days Of Discharge" - creates it if necessary
	     */
	    public ST getPurpose31DaysOfDischarge() { 
	    	ST retVal = this.getTypedField(48, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-49: "Clinical Diagnosis Code" - creates it if necessary
	     */
	    public ST getClinicalDiagnosisCode() { 
	    	ST retVal = this.getTypedField(49, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-50: "Clinical Diagnosis Name" - creates it if necessary
	     */
	    public ST getClinicalDiagnosisName() { 
	    	ST retVal = this.getTypedField(50, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-51: "Clinical Doctor" - creates it if necessary
	     */
	    public ST getClinicalDoctor() { 
	    	ST retVal = this.getTypedField(51, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-52: "Profession" - creates it if necessary
	     */
	    public ST getClinicalDiagnosisNameCM() { 
	    	ST retVal = this.getTypedField(52, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-53: "Birth Weight Of Newborns" - creates it if necessary
	     */
	    public ST getBirthWeightOfNewborns() { 
	    	ST retVal = this.getTypedField(53, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-54: "Hospitalized Weight Of Newborns" - creates it if necessary
	     */
	    public ST getHospitalizedWeightOfNewborns() { 
	    	ST retVal = this.getTypedField(54, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-55: "Medical Institution" - creates it if necessary
	     */
	    public ST getMedicalInstitution() { 
	    	ST retVal = this.getTypedField(55, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-56: "Medical Institution Code" - creates it if necessary
	     */
	    public ST getMedicalInstitutionCode() { 
	    	ST retVal = this.getTypedField(56, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-57: "Medical Pay Mode" - creates it if necessary
	     */
	    public ST getMedicalPayMode() { 
	    	ST retVal = this.getTypedField(57, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMI-58: "Medical Certificate Number" - creates it if necessary
	     */
	    public ST getMedicalCertificateNumber() { 
	    	ST retVal = this.getTypedField(58, 0);
			return retVal;
	    }
	    

	    /** {@inheritDoc} */   
	    protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {
	          case 0: return new SI(getMessage());
	          case 1: return new TS(getMessage());
	          case 2: return new PL(getMessage());
	          case 3: return new PL(getMessage());
	          case 4: return new PL(getMessage());
	          
	          case 5: return new NM(getMessage());
	          case 6: return new IS(getMessage(), new Integer( 20 ));
	          case 7: return new CE(getMessage());
	          case 8: return new NM(getMessage());
	          case 9: return new XCN(getMessage());
	          
	          case 10: return new XCN(getMessage());
	          case 11: return new XCN(getMessage());
	          case 12: return new XCN(getMessage());
	          case 13: return new XCN(getMessage());
	          case 14: return new XCN(getMessage());
	          
	          case 15: return new XCN(getMessage());
	          case 16: return new XCN(getMessage());
	          case 17: return new XCN(getMessage());
	          case 18: return new XCN(getMessage());
	          case 19: return new CE(getMessage());
	          
	          case 20: return new ST(getMessage());
	          case 21: return new CX(getMessage());
	          case 22: return new CE(getMessage());
	          case 23: return new ST(getMessage());
	          case 24: return new ST(getMessage());
	          
	          case 25: return new ST(getMessage());
	          case 26: return new ST(getMessage());
	          case 27: return new ST(getMessage());
	          case 28: return new ST(getMessage());
	          case 29: return new ST(getMessage());

	          case 30: return new ST(getMessage());
	          case 31: return new ST(getMessage());
	          case 32: return new ST(getMessage());
	          case 33: return new ST(getMessage());
	          case 34: return new ST(getMessage());

	          case 35: return new ST(getMessage());
	          case 36: return new ST(getMessage());
	          case 37: return new ST(getMessage());
	          case 38: return new ST(getMessage());
	          case 39: return new ST(getMessage());
	          
	          case 40: return new ST(getMessage());
	          case 41: return new TS(getMessage());
	          case 42: return new ST(getMessage());
	          case 43: return new ST(getMessage());
	          case 44: return new ST(getMessage());
	          
	          case 45: return new ST(getMessage());
	          case 46: return new ST(getMessage());
	          case 47: return new ST(getMessage());
	          case 48: return new ST(getMessage());
	          case 49: return new ST(getMessage());
	          
	          case 50: return new ST(getMessage());
	          case 51: return new ST(getMessage());
	          case 52: return new ST(getMessage());
	          case 53: return new ST(getMessage());
	          case 54: return new ST(getMessage());
	          
	          case 55: return new ST(getMessage());
	          case 56: return new ST(getMessage());
	          case 57: return new ST(getMessage());
	          
	          default: return null;
	       }
	   }
}
