package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.MO;
import ca.uhn.hl7v2.model.v24.datatype.NM;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ZDL extends AbstractSegment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZDL(Group parent, ModelClassFactory factory) {
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
	    	//Drug	编码及名称	CE	R	64
	    	this.add(CE.class, false, 1, 32, new Object[]{ getMessage() }, "Drug");
		    	//Ypbm	费用编码	ST	R	32
		    	//this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Ypbm");
		    	//Ypmc	费用项目	ST	R	32
		    	//this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Ypmc");
	    	//Gg	规格	ST	R	32
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Gg");
	    	//Dw	单位	ST	R	32
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Dw");
	    	//Sl	数量	NM	R	12
	    	this.add(NM.class, false, 1, 12, new Object[]{ getMessage() }, "Sl");
	    	//Dj	单价	MO	R	12
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Dj");
	    	//Hjje	费用	MO	R	12
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Hjje");

	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZFI - this is probably a bug in the source code generator.", e);
	    }
	 }
	 //Drug	编码及名称	CE	R	64
	 public CE getDrug() {
		 CE retVal = this.getTypedField(1, 0);
		return retVal;
	 }
	 //Ypbm	费用编码	ST	R	32
//	 public ST getYpbm() {
//		 ST retVal = this.getTypedField(1, 0);
//		return retVal;
//	 }
	 //Ypmc	费用项目	ST	R	32
//	 public ST getYpmc() {
//		 ST retVal = this.getTypedField(1, 0);
//		return retVal;
//	 }
	 //Gg	规格	ST	R	32
	 public ST getGg() {
		 ST retVal = this.getTypedField(2, 0);
		return retVal;
	 }
	 //Dw	单位	ST	R	32
	 public ST getDw() {
		 ST retVal = this.getTypedField(3, 0);
			return retVal;
	 }
	 //Sl	数量	NM	R	12
	 public NM getSl() {
		 NM retVal = this.getTypedField(4, 0);
			return retVal;
	 }
	 //Dj	单价	MO	R	12
	 public MO getDj() {
		 MO retVal = this.getTypedField(5, 0);
			return retVal;
	 }
	 //Hjje	费用	MO	R	12
	 public MO getHjje() {
		 MO retVal = this.getTypedField(6, 0);
			return retVal;
	 }

	 /** {@inheritDoc} */
	    @Override
		protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {
	          case 0: return new CE(getMessage());
	          case 1: return new ST(getMessage());
	          case 2: return new ST(getMessage());
	          case 3: return new NM(getMessage());
	          case 4: return new MO(getMessage());
	          case 5: return new MO(getMessage());
	          //case 6: return new MO(getMessage());

	          default: return null;
	       }
	   }



}
