package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v21.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * 未启用
 * @author FH
 *
 */
public class ZRN extends AbstractSegment{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public ZRN(Group parent, ModelClassFactory factory) {
	    super(parent, factory);
	    init(factory);
	 }

	private void init(ModelClassFactory factory) {
	    try {
	    	this.add(TS.class, false, 1, 32, new Object[]{ getMessage() }, "DateStart");
	    	this.add(TS.class, false, 1, 32, new Object[]{ getMessage() }, "DateEnd");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "OperatorType");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "DeptId");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "DrId");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "RegLevel");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "SchedulingID");

	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZRI - this is probably a bug in the source code generator.", e);
	    }
	 }


	 public String getVersion()
	  {
	     return "2.4";
	  }

	/**
	 * 1	DateStart	排班开始时间
	 * @return
	 */
	public TS getDateStart() {
		TS retVal = (TS)this.getTypedField(1, 0);
		return retVal;
    }
	/**
	 * 2	DateEnd	排班结束时间
	 * @return
	 */
	public TS getDateEnd() {
		TS retVal = (TS)this.getTypedField(2, 0);
		return retVal;
    }
	/**
	 * 3	OperatorType	坐诊类型
	 * @return
	 */
	public ST getOperatorType() {
		ST retVal = (ST)this.getTypedField(3, 0);
		return retVal;
    }
	/**
	 * 4	DeptId	科室ID
	 * @return
	 */
	public ST getDeptId() {
		ST retVal = (ST)this.getTypedField(4, 0);
		return retVal;
    }
	/**
	 * 5	DrId	医生ID
	 * @return
	 */
	public ST getDrId() {
		ST retVal = (ST)this.getTypedField(5, 0);
		return retVal;
    }
	/**
	 * 6	RegLevel	挂号级别
	 * @return
	 */
	public ST getRegLevel() {
		ST retVal = (ST)this.getTypedField(6, 0);
		return retVal;
    }
	/**
	 * 7	SchedulingID	排班ID
	 * @return
	 */
	public ST getSchedulingID() {
		ST retVal = (ST)this.getTypedField(7, 0);
		return retVal;
    }

	/** {@inheritDoc} */
    @Override
	protected Type createNewTypeWithoutReflection(int field) {
       switch (field) {
          case 0: return new TS(getMessage());
          case 1: return new TS(getMessage());
          case 2: return new ST(getMessage());
          case 3: return new ST(getMessage());
          case 4: return new ST(getMessage());

          case 5: return new ST(getMessage());
          case 6: return new ST(getMessage());

          default: return null;
       }
   }

}
