package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v21.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.IS;
import ca.uhn.hl7v2.model.v24.datatype.SI;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 *<p>Represents an HL7 ZMS message segment 
 * This segment has the following fields:</p>
 * <ul>
     * <li>ZMS-1: Set ID - ZMS (SI) <b>optional </b>
     * <li>ZMS-2: Procedure Code (CE) <b> </b>
     * <li>ZMS-3: Procedure Description (ST) <b>optional </b>
     * <li>ZMS-4: Procedure Date/Time (TS) <b>optional </b>
     * <li>ZMS-5: Anesthesiologist (XCN) <b>optional </b>
     * 
     * <li>ZMS-6: Anesthesia Code (IS) <b>optional </b>
     * <li>ZMS-7: Procedure Practitioner (XCN) <b>optional repeating</b>
     * <li>ZMS-8: First Asistant (IS) <b>optional repeating</b>
     * <li>ZMS-9: Second Asistant (IS) <b>optional repeating</b>
     * <li>ZMS-10: Operation Code (CE) <b>optional </b>
     * 
     * <li>ZMS-11: Emergency Indicator (CE) <b>optional </b>
     * <li>ZMS-12: Cut Heal Grade (ST) <b>optional </b>
 * </ul>
 */
/**
 * 病案手术自定义段ZMS
 * @author chengjia
 *
 */
public class ZMS extends AbstractSegment {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZMS(Group parent, ModelClassFactory factory) {
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
	    	this.add(SI.class, false, 1, 4, new Object[]{ getMessage() }, "Set ID - ZMS");
	    	this.add(CE.class, false, 1, 250, new Object[]{ getMessage() }, "Procedure Code");
	    	this.add(ST.class, false, 1, 40, new Object[]{ getMessage() }, "Procedure Description");
	    	this.add(TS.class, false, 1, 26, new Object[]{ getMessage() }, "Procedure Date Time");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Anesthesiologist");
	    	
	    	this.add(IS.class, false, 1, 5, new Object[]{ getMessage(), new Integer(78) }, "Anesthesia Code");
	    	this.add(XCN.class, false, 1, 250, new Object[]{ getMessage() }, "Procedure Practitioner ");
	    	this.add(IS.class, false, 1, 5, new Object[]{ getMessage(), new Integer(78) }, "First Asistant");
	    	this.add(IS.class, false, 1, 5, new Object[]{ getMessage(), new Integer(78) }, "Second Asistant");
	    	this.add(CE.class, true, 1, 250, new Object[]{ getMessage() }, "Operation Code");
	    	
	    	this.add(CE.class, true, 1, 250, new Object[]{ getMessage() }, "Emergency Indicator");
	    	this.add(ST.class, false, 1, 6, new Object[]{ getMessage() }, "Cut Heal Grade");
	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZMS - this is probably a bug in the source code generator.", e);
	    }
	 }
	
	    /**
	     * Returns
	     * ZMS-1: "Set ID" - creates it if necessary
	     */
	    public SI getSetIDZMS() { 
			SI retVal = this.getTypedField(1, 0);
			return retVal;
	    }

	    
	    /**
	     * Returns
	     * ZMS-2: "Procedure Code" - creates it if necessary
	     */
	    public CE getProcedureCode() { 
			CE retVal = this.getTypedField(2, 0);
			return retVal;
	    }



	    /**
	     * Returns
	     * ZMS-3: "Procedure Description" - creates it if necessary
	     */
	    public ST getProcedureDescription() { 
			ST retVal = this.getTypedField(3, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMS-4: "Procedure Date/Time"" - creates it if necessary
	     */
	    public TS getProcedureDateTime() { 
	    	TS retVal = this.getTypedField(4, 0);
			return retVal;
	    }

	    /**
	     * Returns
	     * ZMS-5: "Anesthesiologist" - creates it if necessary
	     */
	    public XCN getAnesthesiologist() { 
	    	XCN retVal = this.getTypedField(5, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMS-6: "Anesthesia Code" - creates it if necessary
	     */
	    public IS getAnesthesiaCode() { 
	    	IS retVal = this.getTypedField(6, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZMS-7: "Procedure Practitioner " - creates it if necessary
	     */
	    public XCN getProcedurePractitioner () { 
	    	XCN retVal = this.getTypedField(7, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZMS-8: "First Asistant" - creates it if necessary
	     */
	    public IS getFirstAsistant() { 
			IS retVal = this.getTypedField(8, 0);
			return retVal;
	    }
	    

	    /**
	     * Returns 
	     * ZMS-9: "Second Asistant" - creates it if necessary
	     */
	    public IS getSecondAsistant() { 
	    	IS retVal = this.getTypedField(9, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZMS-10: "Operation Code" - creates it if necessary
	     */
	    public CE getOperationCode() { 
	    	CE retVal = this.getTypedField(10, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZMS-11: "Emergency Indicator" - creates it if necessary
	     */
	    public CE getEmergencyIndicator() { 
	    	CE retVal = this.getTypedField(11, 0);
			return retVal;
	    }

	    /**
	     * Returns
	     * ZMS-12: "Cut Heal Grade" - creates it if necessary
	     */
	    public TS getCutHealGrade() { 
			TS retVal = this.getTypedField(12, 0);
			return retVal;
	    }
	    
	    /** {@inheritDoc} */   
	    protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {
	          case 0: return new SI(getMessage());
	          case 1: return new CE(getMessage());
	          case 2: return new ST(getMessage());
	          case 3: return new TS(getMessage());
	          case 4: return new XCN(getMessage());
	          
	          case 5: return new IS(getMessage(), new Integer( 4 ));
	          case 6: return new XCN(getMessage());
	          case 7: return new IS(getMessage(), new Integer( 20 ));
	          case 8: return new IS(getMessage(), new Integer( 20 ));
	          case 9: return new CE(getMessage());
	          
	          case 10: return new CE(getMessage());
	          case 11: return new ST(getMessage());
	          default: return null;
	       }
	   }
}
