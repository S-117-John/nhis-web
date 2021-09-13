package com.zebone.nhis.ma.pub.platform.zb.model;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v21.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.IS;
import ca.uhn.hl7v2.model.v24.datatype.NM;
import ca.uhn.hl7v2.model.v24.datatype.SI;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.datatype.TM;
import ca.uhn.hl7v2.model.v24.datatype.TX;
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 *<p>Represents an HL7 ZCR message segment 
 * This segment has the following fields:</p>
 * <ul>
     * <li>ZCR-1: Set ID - ZCR (SI) <b>optional </b>
     * <li>ZCR-2: Tumour Stage Type (TX) <b> </b>
     * <li>ZCR-3: Tumour stage (TX) <b>optional </b>
     * <li>ZCR-4: Radiation way (NM) <b>optional </b>
     * <li>ZCR-5: Radiation therapy programs (NM) <b>optional </b>
     * 
     * <li>ZCR-6: Radiation device (NM) <b>optional </b>
     * <li>ZCR-7: Original tumor dose (TX) <b>optional repeating</b>
     * <li>ZCR-8: Original tumor start time (TM) <b>optional repeating</b>
     * <li>ZCR-9: Original tumor stop time (TM) <b>optional repeating</b>
     * <li>ZCR-10: regional lymph node dose (TX) <b>optional </b>
     * 
     * <li>ZCR-11: regional lymph node start time (TM) <b>optional </b>
     * <li>ZCR-12: regional lymph node stop time (TM) <b>optional </b>
     * <li>ZCR-13: Metastasis dose (TX) <b>optional </b>
     * <li>ZCR-14: Metastasis start time (TM) <b>optional </b>
     * <li>ZCR-15: Metastasis stop time (TM) <b>optional </b>
     * 
     * <li>ZCR-16: Chemotherapy way (NM) <b>optional </b>
     * <li>ZCR-17: Chemotherapy method (NM) <b>optional </b>
     * <li>ZCR-18: Chemotherapy drug treatment (TX) <b>optional </b>
     * 
 * </ul>
 */
/**
 * 肿瘤治疗记录ZCR
 * @author chengjia
 *
 */
@SuppressWarnings("unused")
public class ZCR extends AbstractSegment {
	public ZCR(Group parent, ModelClassFactory factory) {
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
	    	 * the ZCR segment.
	    	 * 
	    	 * See here for information on the arguments to this method:
	    	 * http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/model/AbstractSegment.html#add%28java.lang.Class,%20boolean,%20int,%20int,%20java.lang.Object[],%20java.lang.String%29
	    	 */
	    	this.add(SI.class, false, 1, 4, new Object[]{ getMessage() }, "Set ID - ZCR");
	    	this.add(TX.class, false, 1, 200, new Object[]{ getMessage() }, "Tumour Stage Type");
	    	this.add(TX.class, false, 1, 200, new Object[]{ getMessage() }, "Tumour Stage");
	    	this.add(NM.class, false, 0, 1, new Object[]{ getMessage() }, "Radiation Way");
	    	this.add(NM.class, false, 0, 1, new Object[]{ getMessage() }, "Radiation Therapy Programs");
	    	
	    	this.add(NM.class, false, 0, 1, new Object[]{ getMessage() }, "Radiation Device");
	    	this.add(TX.class, false, 1, 200, new Object[]{ getMessage() }, "Original Tumor Dose");
	    	this.add(TM.class, true, 1, 20, new Object[]{ getMessage() }, "Original Tumor Start Time");
	    	this.add(TM.class, true, 1, 20, new Object[]{ getMessage() }, "Original Tumor Stop Time");
	    	this.add(TX.class, false, 1, 200, new Object[]{ getMessage() }, "Regional Lymph Node Dose");
	    	
	    	this.add(TM.class, true, 1, 20, new Object[]{ getMessage() }, "Regional Lymph Node Start Time");
	    	this.add(TM.class, true, 1, 20, new Object[]{ getMessage() }, "Regional Lymph Node Stop Time");
	    	this.add(TX.class, false, 1, 200, new Object[]{ getMessage() }, "Metastasis Dose");
	    	this.add(TM.class, true, 1, 20, new Object[]{ getMessage() }, "Metastasis Start Time");
	    	this.add(TM.class, true, 1, 20, new Object[]{ getMessage() }, "Metastasis Stop Time");
	    	
	    	this.add(NM.class, false, 0, 1, new Object[]{ getMessage() }, "Chemotherapy Way");
	    	this.add(NM.class, false, 0, 1, new Object[]{ getMessage() }, "Chemotherapy Method");
	    	this.add(TX.class, false, 1, 200, new Object[]{ getMessage() }, "Chemotherapy Drug Treatment");
	    	
	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZCR - this is probably a bug in the source code generator.", e);
	    }
	 }
	
	    /**
	     * Returns
	     * ZCR-1: "Set ID" - creates it if necessary
	     */
	    public SI getSetIDZCR() { 
			SI retVal = this.getTypedField(1, 0);
			return retVal;
	    }

	    
	    /**
	     * Returns
	     * ZCR-2: "Tumour Stage Type" - creates it if necessary
	     */
	    public TX getTumourStageType() { 
	    	TX retVal = this.getTypedField(2, 0);
			return retVal;
	    }



	    /**
	     * Returns
	     * ZCR-3: "Tumour Stage" - creates it if necessary
	     */
	    public TX getTumourStage() { 
	    	TX retVal = this.getTypedField(3, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZCR-4: "Radiation Way" - creates it if necessary
	     */
	    public NM getRadiationWay() { 
	    	NM retVal = this.getTypedField(4, 0);
			return retVal;
	    }

	    /**
	     * Returns
	     * ZCR-5: "Radiation Therapy Programs" - creates it if necessary
	     */
	    public NM getRadiationTherapyPrograms() { 
	    	NM retVal = this.getTypedField(5, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZCR-6: "Radiation Device" - creates it if necessary
	     */
	    public NM getRadiationDevice() { 
	    	NM retVal = this.getTypedField(6, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZCR-7: "Original Tumor Dose" - creates it if necessary
	     */
	    public TX getOriginalTumorDose() { 
	    	TX retVal = this.getTypedField(7, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZCR-8: "Original Tumor Start Time" - creates it if necessary
	     */
	    public TM getOriginalTumorStartTime() { 
	    	TM retVal = this.getTypedField(8, 0);
			return retVal;
	    }
	    

	    /**
	     * Returns 
	     * ZCR-9: "Original Tumor Stop Time" - creates it if necessary
	     */
	    public TM getOriginalTumorStopTime() { 
	    	TM retVal = this.getTypedField(9, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZCR-10: "Regional Lymph Node Dose" - creates it if necessary
	     */
	    public TX getRegionalLymphNodeDose() { 
	    	TX retVal = this.getTypedField(10, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZCR-11: "Regional Lymph Node Start Time" - creates it if necessary
	     */
	    public TM getRegionalLymphNodeStartTime() { 
	    	TM retVal = this.getTypedField(11, 0);
			return retVal;
	    }

	    /**
	     * Returns
	     * ZCR-12: "Regional Lymph Node Stop Time" - creates it if necessary
	     */
	    public TM getRegionalLymphNodeStopTime() { 
	    	TM retVal = this.getTypedField(12, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZCR-13: "Cut Heal Grade" - creates it if necessary
	     */
	    public TS getCutHealGrade() { 
			TS retVal = this.getTypedField(13, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZCR-14: "Cut Heal Grade" - creates it if necessary
	     */
	    public TM getMetastasisStartTime() { 
	    	TM retVal = this.getTypedField(14, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZCR-15: "Metastasis Stop Time" - creates it if necessary
	     */
	    public TM getMetastasisStopTime() { 
	    	TM retVal = this.getTypedField(15, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZCR-16: "Cut Heal Grade" - creates it if necessary
	     */
	    public TS getChemotherapyWay() { 
			TS retVal = this.getTypedField(16, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZCR-17: "Cut Heal Grade" - creates it if necessary
	     */
	    public NM getChemotherapyMethod() { 
	    	NM retVal = this.getTypedField(17, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZCR-18: "Cut Heal Grade" - creates it if necessary
	     */
	    public TS getChemotherapyDrugTreatment() { 
			TS retVal = this.getTypedField(18, 0);
			return retVal;
	    }
	    

	    /** {@inheritDoc} */   
	    protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {
	          case 0: return new SI(getMessage());
	          case 1: return new TX(getMessage());
	          case 2: return new TX(getMessage());
	          case 3: return new NM(getMessage());
	          case 4: return new NM(getMessage());
	          
	          case 5: return new NM(getMessage());
	          case 6: return new TX(getMessage());
	          case 7: return new TM(getMessage());
	          case 8: return new TM(getMessage());
	          case 9: return new TX(getMessage());
	          
	          case 10: return new TM(getMessage());
	          case 12: return new TM(getMessage());
	          case 13: return new TX(getMessage());
	          case 14: return new TM(getMessage());
	          case 15: return new TM(getMessage());
	          
	          case 16: return new NM(getMessage());
	          case 17: return new NM(getMessage());
	          case 18: return new TX(getMessage());
	          
	          default: return null;
	       }
	   }
}
