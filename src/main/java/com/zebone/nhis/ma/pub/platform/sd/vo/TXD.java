package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class TXD extends AbstractSegment{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TXD(Group parent, ModelClassFactory factory) {
		super(parent, factory);
		// TODO Auto-generated constructor stub
		init(factory);
	}
	
	private void init(ModelClassFactory factory) {
	    try {
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "remType");
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "orderID");
	    	this.add(ST.class, false, 1, 10, new Object[]{ getMessage() }, "amount");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "level");
	    	this.add(ST.class, false, 1, 100, new Object[]{ getMessage() }, "warnMsg");
	    	this.add(ST.class, false, 1, 200, new Object[]{ getMessage() }, "extension");
	    	this.add(ST.class, false, 1, 15, new Object[]{ getMessage() }, "queue");

	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZFI - this is probably a bug in the source code generator.", e);
	    }
	 }
	
	/**
	 * 1  remType	提醒类型	20
	 * @return
	 */
	public ST getRemType() { 
		ST retVal = this.getTypedField(1, 0);
		return retVal;
	}
	/**
	 * 2	orderID	订单ID	32
	 * @return
	 */
	public ST getOrderID() { 
		ST retVal = this.getTypedField(2, 0);
		return retVal;
	}
	/**
	 * 3	amount	金额	10
	 * @return
	 */
	public ST getAmount() { 
		ST retVal = this.getTypedField(3, 0);
		return retVal;
	}
	/**
	 * 4	level	加急标志	12
	 * @return
	 */
	public ST getLevel() { 
		ST retVal = this.getTypedField(4, 0);
		return retVal;
	}
	/**
	 * 5	warnMsg	提醒内容	100
	 * @return
	 */
	public ST getWarnMsg() { 
		ST retVal = this.getTypedField(5, 0);
		return retVal;
	}
	/**
	 * 6	extension	扩展	200
	 * @return
	 */
	public ST getExtension() { 
		ST retVal = this.getTypedField(6, 0);
		return retVal;
	}
	/**
	 * 7	queue	队列	15
	 * @return
	 */
	public ST getQueue() { 
		ST retVal = this.getTypedField(7, 0);
		return retVal;
	}

	protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {
	          case 0: return new ST(getMessage());
	          case 1: return new ST(getMessage());
	          case 2: return new ST(getMessage());
	          case 3: return new ST(getMessage());
	          case 4: return new ST(getMessage());
	          case 5: return new ST(getMessage());
	          case 6: return new ST(getMessage());
	          case 7: return new ST(getMessage());
	          
	          default: return null;
	       }
		}

}
