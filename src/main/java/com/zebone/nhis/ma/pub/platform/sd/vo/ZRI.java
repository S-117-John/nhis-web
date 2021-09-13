package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v24.datatype.MO;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * 
 * @author maijiaxing
 *
 */
public class ZRI extends AbstractSegment{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ZRI(Group parent, ModelClassFactory factory) {
	    super(parent, factory);
	    init(factory);
	 }
	
	private void init(ModelClassFactory factory) {
	    try {
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Fphm");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Address");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Ghf");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Zlf");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Xj");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Jzje");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Zhye");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Zjje");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Sfy");
	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZRI - this is probably a bug in the source code generator.", e);
	    }
	 }
	/**
	 * 1	Fphm	发票号码	ST	R	32
	 * @return
	 */
	public ST getFphm() { 
    	ST retVal = this.getTypedField(1, 0);
		return retVal;
    }
	/**
	 * 2	Address	科室地址	ST	R	32
	 * @return
	 */
	public ST getAddress() { 
    	ST retVal = this.getTypedField(2, 0);
		return retVal;
    }
	/**
	 * 3	Ghf	挂号费	MO	R	12
	 * @return
	 */
	public MO getGhf() { 
		MO retVal = this.getTypedField(3, 0);
		return retVal;
    }
	/**
	 * 4	Zlf	诊疗费	MO	R	12
	 * @return
	 */
	public MO getZlf() { 
		MO retVal = this.getTypedField(4, 0);
		return retVal;
    }
	/**
	 * 5	Xj	现金	MO	R	12
	 * @return
	 */
	public MO getXj() { 
		MO retVal = this.getTypedField(5, 0);
		return retVal;
    }
	/**
	 * 6	Jzje	记账金额	MO	R	12
	 * @return
	 */
	public MO getJzje() { 
		MO retVal = this.getTypedField(6, 0);
		return retVal;
    }
	/**
	 * 7	Zhye	账户余额	MO	R	12
	 * @return
	 */
	public MO getZhye() { 
		MO retVal = this.getTypedField(7, 0);
		return retVal;
    }
	/**
	 * 8	Zjje	总计金额	MO	R	12
	 * @return
	 */
	public MO getZjje() { 
		MO retVal = this.getTypedField(8, 0);
		return retVal;
    }
	/**
	 * 9	Sfy	收费员	ST	R	32
	 * @return
	 */
	public ST getSfy() { 
    	ST retVal = this.getTypedField(9, 0);
		return retVal;
    }
	/** {@inheritDoc} */   
    protected Type createNewTypeWithoutReflection(int field) {
       switch (field) {
          case 0: return new ST(getMessage());
          case 1: return new ST(getMessage());
          case 2: return new MO(getMessage());
          case 3: return new MO(getMessage());
          case 4: return new MO(getMessage());
          
          case 5: return new MO(getMessage());
          case 6: return new MO(getMessage());
          case 7: return new MO(getMessage());
          case 8: return new ST(getMessage());
          //case 9: return new ST(getMessage());
          
          default: return null;
       }
   }
}
