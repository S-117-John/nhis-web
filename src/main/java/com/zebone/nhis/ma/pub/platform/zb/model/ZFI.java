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
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 *<p>Represents an HL7 ZFI message segment  
 * This segment has the following fields:</p>
 * <ul>
     * <li>ZFI-1: FYBM (ST) <b>optional </b>
     * <li>ZFI-2: FYXM (ST) <b> </b>
     * <li>ZFI-3: FYJE (ST) <b>optional </b>
 * </ul>
 */
/**
 * ZFI-费用项目段
 * @author chengjia
 *
 */
@SuppressWarnings("unused")
public class ZFI extends AbstractSegment {
	public ZFI(Group parent, ModelClassFactory factory) {
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
	    	 * the ZFI segment.
	    	 * 
	    	 * See here for information on the arguments to this method:
	    	 * http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/model/AbstractSegment.html#add%28java.lang.Class,%20boolean,%20int,%20int,%20java.lang.Object[],%20java.lang.String%29
	    	 */
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "FYBM");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "FYXM");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "FYJE");

	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZFI - this is probably a bug in the source code generator.", e);
	    }
	 }
	
	    /**
	     * Returns
	     * ZFI-1: "FYBM" - creates it if necessary
	     */
	    public ST getFybm() { 
			ST retVal = this.getTypedField(1, 0);
			return retVal;
	    }

	    
	    /**
	     * Returns
	     * ZFI-2: "FYXM" - creates it if necessary
	     */
	    public ST getFyxm() { 
	    	ST retVal = this.getTypedField(2, 0);
			return retVal;
	    }


	    /**
	     * Returns
	     * ZFI-3: "FYJE" - creates it if necessary
	     */
	    public ST getFyje() { 
			ST retVal = this.getTypedField(3, 0);
			return retVal;
	    }
	    
	    
	    /** {@inheritDoc} */   
	    protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {
	          case 0: return new ST(getMessage());
	          case 1: return new ST(getMessage());
	          case 2: return new ST(getMessage());
	          
	          default: return null;
	       }
	   }
}
