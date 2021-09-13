package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ZPM extends AbstractSegment{
	
	private static final long serialVersionUID = 1L;

	public ZPM(Group parent, ModelClassFactory factory) {
		super(parent, factory);
		init(factory);
	}
	
	
//	1	Mzhm	门诊号码	ST	O	50		
//	2	Fph		发票号	ST	R	50		
//	3	YjsId	预结算ID	ST	R	75		
//	4	Ybkh	医保卡号	ST	O	50		
//	5	Mzrq	门诊日期	DT	O	8		
//	6	OrderId	微信（第三方）订单号	ST	R	75		
//	7	Brxm	姓名	ST	O	50		
//	8	Sflb	收费类别	ST	O	1		0代表门诊 1 代表急诊
//	9	Ybje	医保金额	MO	O	12		
//	10	Zfje	自费金额	MO	O	12		
//	11	Jsfs	结算方式	ST	O	50		
//	12	sqdlx	申请单类型	ST	R	10		1.挂号退费    2、诊间支付退费  3、住院预交金退费-待定
//	34	Zje	总金额	MO	O	12
//	38	Paymethod	支付方式	e.g. WX
//	39	PayChannel	渠道		e.g.JY160

	private void init(ModelClassFactory factory) {
	    try {
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Mzhm");
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Fph");
	    	this.add(ST.class, false, 1, 75, new Object[]{ getMessage() }, "YjsId");
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Ybkh");
	    	this.add(ST.class, false, 1, 8, new Object[]{ getMessage() }, "Mzrq");
	    	this.add(ST.class, false, 1, 75, new Object[]{ getMessage() }, "OrderId");
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Brxm");
	    	this.add(ST.class, false, 1, 1, new Object[]{ getMessage() }, "Sflb");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "Ybje");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "Zfje");
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Jsfs");
	    	this.add(ST.class, false, 1, 10, new Object[]{ getMessage() }, "sqdlx");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "Zje");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//13
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//14
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//15
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//16
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//17
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//18
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//19
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//20
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//21
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//22
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//23
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//24
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//25
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//26
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//27
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//28
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//29
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//30
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//31
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//32
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//33
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//34
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//35
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//36
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "");//37
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "Paymethod");
	    	this.add(ST.class, false, 1, 12, new Object[]{ getMessage() }, "PayChannel");

	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZRI - this is probably a bug in the source code generator.", e);
	    }
	 }
	
	
	//	1	Mzhm	门诊号码	ST	O	50	
	public ST getMzhm() {
		ST retVal = (ST)this.getTypedField(1, 0);
		return retVal;
    }
	
	//	2	Fph		发票号	ST	R	50	
	public ST getFph() {
		ST retVal = (ST)this.getTypedField(2, 0);
		return retVal;
    }
	//	3	YjsId	预结算ID	ST	R	75	
	public ST getYjsId() {
		ST retVal = (ST)this.getTypedField(3, 0);
		return retVal;
    }
	//	4	Ybkh	医保卡号	ST	O	50		
	public ST getYbkh() {
		ST retVal = (ST)this.getTypedField(4, 0);
		return retVal;
    }
	//	5	Mzrq	门诊日期	DT	O	8		
	public ST getMzrq() {
		ST retVal = (ST)this.getTypedField(5, 0);
		return retVal;
    }
	//	6	OrderId	微信（第三方）订单号	ST	R	75	
	public ST getOrderId() {
		ST retVal = (ST)this.getTypedField(6, 0);
		return retVal;
    }
	//	7	Brxm	姓名	ST	O	50		
	public ST getBrxm() {
		ST retVal = (ST)this.getTypedField(7, 0);
		return retVal;
    }
	//	8	Sflb	收费类别	ST	O	1		0代表门诊 1 代表急诊
	public ST getSflb() {
		ST retVal = (ST)this.getTypedField(8, 0);
		return retVal;
    }
	//	9	Ybje	医保金额	MO	O	12		
	public ST getYbje() {
		ST retVal = (ST)this.getTypedField(9, 0);
		return retVal;
    }
	//	10	Zfje	自费金额	MO	O	12		
	public ST getZfje() {
		ST retVal = (ST)this.getTypedField(10, 0);
		return retVal;
    }
	//	11	Jsfs	结算方式	ST	O	50		
	public ST getJsfs() {
		ST retVal = (ST)this.getTypedField(11, 0);
		return retVal;
    }
	//	12	sqdlx	申请单类型	ST	R	10		1.挂号退费    2、诊间支付退费  3、住院预交金退费-待定
	public ST getsqdlx() {
		ST retVal = (ST)this.getTypedField(12, 0);
		return retVal;
    }
	//	34	Zje	总金额	MO	O	12
	public ST getZje() {
		ST retVal = (ST)this.getTypedField(13, 0);
		return retVal;
    }
	//	38	Paymethod	支付方式	e.g. WX
	public ST getPaymethod() {
		ST retVal = (ST)this.getTypedField(14, 0);
		return retVal;
    }
	//	39	PayChannel	渠道		e.g.JY160
	public ST getPayChannel() {
		ST retVal = (ST)this.getTypedField(39, 0);
		return retVal;
    }
	

	/** {@inheritDoc} */
   @Override
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
         case 8: return new ST(getMessage());
         case 9: return new ST(getMessage());
         case 10: return new ST(getMessage());
         case 11: return new ST(getMessage());
         case 12: return new ST(getMessage());
         case 34: return new ST(getMessage());
         case 38: return new ST(getMessage());
         case 39: return new ST(getMessage());
         

         default: return null;
      }
  }

}
