package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.ID;
import ca.uhn.hl7v2.model.v24.datatype.IS;
import ca.uhn.hl7v2.model.v24.datatype.NM;
import ca.uhn.hl7v2.model.v24.datatype.SI;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 *<p>Represents an HL7 ZOR message segment 
 * This segment has the following fields:</p>
 * <ul>
     * <li>ZOR-1: SET ID - ZOR (SI) <b>optional </b>
     * <li>ZOR-2: Newborn Code (CE) <b> </b>
     * <li>ZOR-3: Administrative Sex (IS) <b>optional </b>
     * <li>ZOR-4: Stillborn Indicator (ID) <b>optional </b>
     * <li>ZOR-5: Newborn Weight (NM) <b>optional </b>
     * 
     * <li>ZOR-6: Discharge Disposition (IS) <b>optional </b>
     * <li>ZOR-7: Breath (IS) <b>optional repeating</b>
     * <li>ZOR-8: CriticalNo of times (NM) <b>optional repeating</b>
     * <li>ZOR-9: Rescue success number (NM) <b>optional repeating</b>

 * </ul>
 */
/**
 * 病案首页自定义段ZOR
 * @author chengjia
 *
 */
public class ZOR extends AbstractSegment {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ZOR(Group parent, ModelClassFactory factory) {
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
	    	 * the ZOR segment.
	    	 * 
	    	 * See here for information on the arguments to this method:
	    	 * http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/model/AbstractSegment.html#add%28java.lang.Class,%20boolean,%20int,%20int,%20java.lang.Object[],%20java.lang.String%29
	    	 */
	    	this.add(SI.class, false, 1, 4, new Object[]{ getMessage() }, "Set ID - ZOR");
	    	this.add(CE.class, false, 1, 250, new Object[]{ getMessage() }, "Procedure Code");
	    	this.add(IS.class, false, 1, 1, new Object[]{ getMessage(), new Integer(78) }, "Administrative Sex");
	    	this.add(ID.class, false, 1, 1, new Object[]{ getMessage(), new Integer(78) }, "Stillborn Indicator");
	    	this.add(NM.class, false, 1, 5, new Object[]{ getMessage() }, "Newborn Weight");
	    	
	    	this.add(IS.class, false, 1, 5, new Object[]{ getMessage(), new Integer(78) }, "Discharge Disposition");
	    	this.add(IS.class, false, 1, 1, new Object[]{ getMessage(), new Integer(78) }, "Breath");
	    	this.add(NM.class, false, 1, 2, new Object[]{ getMessage() }, "CriticalNo of times");
	    	this.add(NM.class, false, 1, 2, new Object[]{ getMessage() }, "Rescue success number");
	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZOR - this is probably a bug in the source code generator.", e);
	    }
	 }
	
	    /**
	     * Returns
	     * ZOR-1: "Set ID" - creates it if necessary
	     */
	    public SI getSetIDZOR() { 
			SI retVal = this.getTypedField(1, 0);
			return retVal;
	    }

	    
	    /**
	     * Returns
	     * ZOR-2: "Newborn Code" - creates it if necessary
	     */
	    public CE getNewbornCode() { 
			CE retVal = this.getTypedField(2, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZOR-3: "Administrative Sex" - creates it if necessary
	     */
	    public IS getAdministrativeSex() { 
	    	IS retVal = this.getTypedField(3, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZOR-4: "Stillborn Indicator"" - creates it if necessary
	     */
	    public ID getStillbornIndicator() { 
	    	ID retVal = this.getTypedField(4, 0);
			return retVal;
	    }

	    /**
	     * Returns
	     * ZOR-5: "Newborn Weight" - creates it if necessary
	     */
	    public NM getNewbornWeight() { 
	    	NM retVal = this.getTypedField(5, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZOR-6: "Discharge Disposition" - creates it if necessary
	     */
	    public IS getDischargeDisposition() { 
	    	IS retVal = this.getTypedField(6, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZOR-7: "Breath " - creates it if necessary
	     */
	    public IS getBreath () { 
	    	IS retVal = this.getTypedField(7, 0);
			return retVal;
	    }
	    
	    /**
	     * Returns
	     * ZOR-8: "CriticalNo of times" - creates it if necessary
	     */
	    public NM getCriticalNoOfTimes() { 
	    	NM retVal = this.getTypedField(8, 0);
			return retVal;
	    }
	    

	    /**
	     * Returns 
	     * ZOR-9: "Rescue success number" - creates it if necessary
	     */
	    public NM getRescueSuccessNumber() { 
	    	NM retVal = this.getTypedField(9, 0);
			return retVal;
	    }


	    /** {@inheritDoc} */   
	    protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {
	          case 0: return new SI(getMessage());
	          case 1: return new CE(getMessage());
	          case 2: return new IS(getMessage(), new Integer( 1 ));
	          case 3: return new ID(getMessage(), new Integer( 1 ));
	          case 4: return new NM(getMessage());
	          
	          case 5: return new IS(getMessage(), new Integer( 2 ));
	          case 6: return new IS(getMessage(), new Integer( 1 ));
	          case 7: return new NM(getMessage());
	          case 8: return new NM(getMessage());

	          default: return null;
	       }
	   }
}
