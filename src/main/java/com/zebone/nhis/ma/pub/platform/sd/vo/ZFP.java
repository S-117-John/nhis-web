package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ZFP extends AbstractSegment{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param parent
	 * @param factory
	 */
	public ZFP(Group parent, ModelClassFactory factory) {
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
	    	this.add(CE.class, false, 1, 250, new Object[]{ getMessage() }, "Fph");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Ybje");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Grjf");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Zffs");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Sr");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Zje");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Sfybm");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Sfy");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Jssj");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Fpbz");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Xfph");
	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZFI - this is probably a bug in the source code generator.", e);
	    }
	 }
	
	public CE getFph() { 
		CE retVal = this.getTypedField(1, 0);
		return retVal;
	}
	public ST getYbje() { 
		ST retVal = this.getTypedField(2, 0);
		return retVal;
	}
	public ST getGrjf() { 
		ST retVal = this.getTypedField(3, 0);
		return retVal;
	}
	public ST getZffs() { 
		ST retVal = this.getTypedField(4, 0);
		return retVal;
	}
	public ST getSr() { 
		ST retVal = this.getTypedField(5, 0);
		return retVal;
	}
	public ST getZje() { 
		ST retVal = this.getTypedField(6, 0);
		return retVal;
	}
	public ST getSfybm() { 
		ST retVal = this.getTypedField(7, 0);
		return retVal;
	}
	public ST getSfy() { 
		ST retVal = this.getTypedField(8, 0);
		return retVal;
	}
	public ST getJssj() { 
		ST retVal = this.getTypedField(9, 0);
		return retVal;
	}
	public ST getFpbz() { 
		ST retVal = this.getTypedField(10, 0);
		return retVal;
	}
	public ST getXfph() { 
		ST retVal = this.getTypedField(11, 0);
		return retVal;
	}
	
	protected Type createNewTypeWithoutReflection(int field) {
       switch (field) {
          case 0: return new CE(getMessage());
          case 1: return new ST(getMessage());
          case 2: return new ST(getMessage());
          case 3: return new ST(getMessage());
          case 4: return new ST(getMessage());
          case 5: return new ST(getMessage());
          case 6: return new ST(getMessage());
          case 7: return new ST(getMessage());
          case 8: return new ST(getMessage());
          case 9: return new ST(getMessage());
          case 10: return new ST(getMessage());
          case 11: return new ST(getMessage());
          
          default: return null;
       }
	}
}
