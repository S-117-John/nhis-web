package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v21.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.DT;
import ca.uhn.hl7v2.model.v24.datatype.MO;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ZPO extends AbstractSegment {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZPO(Group parent, ModelClassFactory factory) {
	    super(parent, factory);
	    init(factory);
	 }
	
	private void init(ModelClassFactory factory) {
	    try {
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Mzhm");
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Fph");
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Ybkh");
	    	this.add(TS.class, false, 1, 8, new Object[]{ getMessage() }, "Mzrq");
	    	this.add(ST.class, false, 1, 1, new Object[]{ getMessage() }, "Cfsb");
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Brxm");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "Sflb");
	    	this.add(MO.class, true, 1, 50, new Object[]{ getMessage() }, "Jzje");
	    	this.add(MO.class, true, 1, 50, new Object[]{ getMessage() }, "Grjf");
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Jsfs");
	    	
	    	this.add(ST.class, true, 1, 50, new Object[]{ getMessage() }, "Fyyf");
	    	this.add(ST.class, true, 1, 50, new Object[]{ getMessage() }, "Fyck");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "DepartmentPlace");
	    	this.add(MO.class, true, 1, 12, new Object[]{ getMessage() }, "Xyf");
	    	this.add(MO.class, true, 1, 12, new Object[]{ getMessage() }, "Zcy");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Zcay");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Zcf");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Jyf");
	    	
	    	this.add(MO.class, true, 1, 12, new Object[]{ getMessage() }, "Jcf");
	    	this.add(MO.class, true, 1, 12, new Object[]{ getMessage() }, "Fsf");    	
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Zlf");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Ssf");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Xzcl");
	    	
	    	this.add(MO.class, true, 1, 12, new Object[]{ getMessage() }, "Qtf");
	    	this.add(MO.class, true, 1, 12, new Object[]{ getMessage() }, "Txfwf");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Jzlgcwf");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Ct");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Sxf");
	    	
	    	this.add(MO.class, true, 1, 12, new Object[]{ getMessage() }, "Syf");
	    	this.add(MO.class, true, 1, 12, new Object[]{ getMessage() }, "Ycf");	    	
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Hlf");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Sr");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Zje");
	    	
	    	this.add(ST.class, false, 1, 250, new Object[]{ getMessage() }, "Zhxx");
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Sfy");
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Clf");

			this.add(ST.class, false, 1, 64, new Object[]{ getMessage() }, "EbillCode");
			this.add(ST.class, false, 1, 64, new Object[]{ getMessage() }, "EbillNo");
			this.add(ST.class, false, 1, 64, new Object[]{ getMessage() }, "CheckCode");
			this.add(DT.class, false, 1, 26, new Object[]{ getMessage() }, "EbillDate");
			this.add(ST.class, false, 1, 5000, new Object[]{ getMessage() }, "EbillQRCode");

			this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Jzlsh");
	    	
	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZCR - this is probably a bug in the source code generator.", e);
	    }
	 }
	
	public ST getMzhm() { 
    	ST retVal = this.getTypedField(1, 0);
		return retVal;
    }
	public ST getFPh() { 
		ST retVal = this.getTypedField(2, 0);
		return retVal;
    }
	public ST getYbkh() { 
		ST retVal = this.getTypedField(3, 0);
		return retVal;
    }
	public TS getMzrq() { 
		TS retVal = this.getTypedField(4, 0);
		return retVal;
    }
	public ST getCfsb () { 
		ST retVal = this.getTypedField(5, 0);
		return retVal;
    }
	public ST getBrxm() { 
		ST retVal = this.getTypedField(6, 0);
		return retVal;
    }
	public ST getSflb() { 
    	ST retVal = this.getTypedField(7, 0);
		return retVal;
    }
	public MO getJzje() { 
    	MO retVal = this.getTypedField(8, 0);
		return retVal;
    }
	public MO getGrjf() { 
    	MO retVal = this.getTypedField(9, 0);
		return retVal;
    }
	public ST getJsfs() { 
    	ST retVal = this.getTypedField(10, 0);
		return retVal;
    }
	public ST getFyyf() { 
    	ST retVal = this.getTypedField(11, 0);
		return retVal;
    }
	public ST getFyck() { 
    	ST retVal = this.getTypedField(12, 0);
		return retVal;
    }
	public ST getDepartmentPlace() { 
    	ST retVal = this.getTypedField(13, 0);
		return retVal;
    }
	public ST getXyf() {
		ST retVal = this.getTypedField(14, 0);
		return retVal;
    }
	public ST getZcy() {
		ST retVal = this.getTypedField(15, 0);
		return retVal;
    }
	public ST getZcay() {
		ST retVal = this.getTypedField(16, 0);
		return retVal;
    }
	public ST getZcf() {
		ST retVal = this.getTypedField(17, 0);
		return retVal;
    }
	public ST getJyf() {
		ST retVal = this.getTypedField(18, 0);
		return retVal;
    }
	public ST getJcf() {
		ST retVal = this.getTypedField(19, 0);
		return retVal;
    }
	public MO getFsf() {
    	MO retVal = this.getTypedField(20, 0);
		return retVal;
    }public MO getZlf() {
    	MO retVal = this.getTypedField(21, 0);
		return retVal;
    }
	public ST getSsf() {
		ST retVal = this.getTypedField(22, 0);
		return retVal;
    }
	public MO getXzcl() {
    	MO retVal = this.getTypedField(23, 0);
		return retVal;
    }
	public ST getQtf() {
		ST retVal = this.getTypedField(24, 0);
		return retVal;
    }
	public MO getTxfwf() {
    	MO retVal = this.getTypedField(25, 0);
		return retVal;
    }
	public MO getJzlgcwf() { 
    	MO retVal = this.getTypedField(26, 0);
		return retVal;
    }
	public MO getCt() { 
    	MO retVal = this.getTypedField(27, 0);
		return retVal;
    }
	public MO getSxf() { 
    	MO retVal = this.getTypedField(28, 0);
		return retVal;
    }
	public MO getSyf() { 
    	MO retVal = this.getTypedField(29, 0);
		return retVal;
    }
	public MO getYcf() { 
    	MO retVal = this.getTypedField(30, 0);
		return retVal;
    }
	public MO getHlf() { 
    	MO retVal = this.getTypedField(31, 0);
		return retVal;
    }
	public MO getSr() { 
    	MO retVal = this.getTypedField(32, 0);
		return retVal;
    }
	public ST getZje() {
		ST retVal = this.getTypedField(33, 0);
		return retVal;
    }
	public ST getZhxx() { 
    	ST retVal = this.getTypedField(34, 0);
		return retVal;
    }
	public ST getSfy() { 
    	ST retVal = this.getTypedField(35, 0);
		return retVal;
    }
	public MO getClf() { 
    	MO retVal = this.getTypedField(36, 0);
		return retVal;
    }

	/**
	 * EbillCode	电子票据代码
	 */
	public ST getEbillCode() {
		ST retVal = this.getTypedField(37, 0);
		return retVal;
	}
	/**
	 * EbillNo	电子票据号码
	 */
	public ST getEbillNo() {
		ST retVal = this.getTypedField(38, 0);
		return retVal;
	}
	/**
	 * CheckCode	电子校验码
	 */
	public ST getCheckCode() {
		ST retVal = this.getTypedField(39, 0);
		return retVal;
	}
	/**
	 * EbillDate	电子票据生成时间
	 */
	public DT getEbillDate() {
		DT retVal = this.getTypedField(40, 0);
		return retVal;
	}
	/**
	 * EbillQRCode	电子票据二维码效验数据
	 */
	public ST getEbillQRCode() {
		ST retVal = this.getTypedField(41, 0);
		return retVal;
	}

	/**
	 * jzlsh 就诊流水号
	 */
	public ST getJzlsh() {
		ST retVal = this.getTypedField(42, 0);
		return retVal;
	}


	/** {@inheritDoc} */   
    @Override
	protected Type createNewTypeWithoutReflection(int field) {
       switch (field) {
          case 0: return new ST(getMessage());
          case 1: return new ST(getMessage());
          case 2: return new ST(getMessage());
          case 3: return new TS(getMessage());
          case 4: return new ST(getMessage());
          
          case 5: return new ST(getMessage());
          case 6: return new ST(getMessage());
          case 7: return new MO(getMessage());
          case 8: return new MO(getMessage());
          case 9: return new ST(getMessage());
          
          case 10: return new ST(getMessage());
          case 12: return new ST(getMessage());
          case 13: return new ST(getMessage());
          case 14: return new ST(getMessage());
          case 15: return new ST(getMessage());
          
          case 16: return new ST(getMessage());
          case 17: return new ST(getMessage());
          case 18: return new ST(getMessage());
          
          case 19: return new ST(getMessage());
          case 20: return new ST(getMessage());
          case 21: return new ST(getMessage());
          case 22: return new ST(getMessage());
          case 23: return new ST(getMessage());
          
          case 24: return new ST(getMessage());
          case 25: return new MO(getMessage());
          case 26: return new MO(getMessage());
          case 27: return new MO(getMessage());
          case 28: return new MO(getMessage());
          
          case 29: return new MO(getMessage());
          case 30: return new MO(getMessage());
          case 31: return new MO(getMessage());
          case 32: return new ST(getMessage());
          case 33: return new MO(getMessage());
          
          case 34: return new ST(getMessage());
          case 35: return new ST(getMessage());

          case 36: return new ST(getMessage());
          case 37: return new ST(getMessage());
          case 38: return new ST(getMessage());
          case 39: return new DT(getMessage());
          case 40: return new ST(getMessage());
          case 41: return new ST(getMessage());

          default: return null;
       }
   }
}
