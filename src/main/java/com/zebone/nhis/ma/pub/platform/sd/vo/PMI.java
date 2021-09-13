package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

@SuppressWarnings("serial")
public class PMI extends AbstractSegment{

	public PMI(Group parent, ModelClassFactory factory) {
		super(parent, factory);
		// TODO Auto-generated constructor stub
		init(factory);
	}
	/**
	 * 
	 * @param factory
	 */
	private void init(ModelClassFactory factory) {
	    try {
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Fpxbm");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Fpx");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Je");

	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZFI - this is probably a bug in the source code generator.", e);
	    }
	 }

	/**
	 * 
	 * @return
	 */
	public ST getFpxbm() { 
			ST retVal = this.getTypedField(1, 0);
			return retVal;
    }
	
	/**
	 * 
	 * @return
	 */
	public ST getFpx() { 
		ST retVal = this.getTypedField(2, 0);
		return retVal;
    }
	/**
	 * 
	 * @return
	 */
	public ST getJe() { 
		ST retVal = this.getTypedField(3, 0);
		return retVal;
    }
	
	 protected Type createNewTypeWithoutReflection(int field) {
       switch (field) {
          case 0: return new ST(getMessage());
          case 1: return new ST(getMessage());
          case 2: return new ST(getMessage());
          case 3: return new ST(getMessage());
          
          default: return null;
       }
	}
	
}
