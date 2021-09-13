package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v21.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;



public class ZAI  extends AbstractSegment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZAI(Group parent, ModelClassFactory factory) {
		super(parent, factory);
		// TODO Auto-generated constructor stub
		init(factory);
	}


	 private void init(ModelClassFactory factory) {
	    try {
	    	//TO_DATE	出诊日期	TS	R	26
	    	this.add(TS.class, false, 1, 26, new Object[]{ getMessage() }, "ToDate");
	    	//TIME_TYPE_DESC	班别：上午、下午	ST	O	4
	    	this.add(ST.class, false, 1, 4, new Object[]{ getMessage() }, "TimeTypeTesc");
	    	//BEGIN_TIME	开始时间	ST	O	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "BeginTime");
	    	//END_TIME	结束时间	ST	O	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "EndTime");
	    	//PAY_STATE	支付状态	ST	O	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "PayMtate");
	    	//PAY_METHOD	支付方式	ST	O	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "PayMethod");
	    	//YUYUE_STATE	订单状态	ST	O	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "YuYueTtate");
	    	//ORDER_TIME	订单时间	TS	O	26
	    	this.add(TS.class, false, 1, 26, new Object[]{ getMessage() }, "OrderTime");
	    	//PRINTED	打印状态: 0 未打印; 1 已打印	ST	O	1
	    	this.add(ST.class, false, 1, 1, new Object[]{ getMessage() }, "Printed");
	    	//CHN	预约渠道	ST	O	250
	    	this.add(CE.class, false, 1, 250, new Object[]{ getMessage() }, "CHN");
	    	/*//Identifier	预约渠道编号	ST	O	10
	    	this.add(CE.class, false, 1, 10, new Object[]{ getMessage() }, "Identifier");
	    	//Text	预约渠道名称	ST	O	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "Text");*/
	    	//Zfzje	自费金额	ST	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Zfzje");
	    	//Sbzje	社保金额	ST	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Sbzje");
	    	//Mzlsh	6位门诊流水号	ST	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Mzlsh");
	    	//Seeno	ST	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Seeno");
	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZFI - this is probably a bug in the source code generator.", e);
	    }
	 }

	 /**
		 * 1  remType	提醒类型	20
		 * @return
		 */
		public TS getToDate() {
			TS retVal = this.getTypedField(1, 0);
			return retVal;
		}
		/**
		 *
		 * @return
		 */
		public ST getTimeTypeTesc() {
			ST retVal = this.getTypedField(2, 0);
			return retVal;
		}
		/**
		 * BeginTime
		 * @return
		 */
		public ST getBeginTime() {
			ST retVal = this.getTypedField(3, 0);
			return retVal;
		}
		/**
		 * EndTime
		 */
		public ST getEndTime() {
			ST retVal = this.getTypedField(4, 0);
			return retVal;
		}
		/*8
		 * PayMtate
		 */
		public ST getPayMtate() {
			ST retVal = this.getTypedField(5, 0);
			return retVal;
		}
		/**
		 * PayMethod
		 * @return
		 */
		public ST getPayMethod() {
			ST retVal = this.getTypedField(6, 0);
			return retVal;
		}
		/**
		 * YuYueTtate
		 * @return
		 */
		public ST getYuYueTtate() {
			ST retVal = this.getTypedField(7, 0);
			return retVal;
		}
		/**
		 * OrderTime
		 * @return
		 */
		public TS getOrderTime() {
			TS retVal = this.getTypedField(8, 0);
			return retVal;
		}
		/**
		 * Printed
		 * @return
		 */
		public ST getPrinted() {
			ST retVal = this.getTypedField(9, 0);
			return retVal;
		}
		/**
		 * CHN
		 * @return
		 */
		public CE getCHN() {
			CE retVal = this.getTypedField(10, 0);
			return retVal;
		}
	/*	*//**
		 * Identifier
		 * @return
		 *//*
		public ST getIdentifier() {
			ST retVal = this.getTypedField(11, 0);
			return retVal;
		}
		*//**
		 * Text
		 * @return
		 *//*
		public ST getText() {
			ST retVal = this.getTypedField(12, 0);
			return retVal;
		}*/
		/*
		 * Zfzje
		 */
		public ST getZfzje() {
			ST retVal = this.getTypedField(11, 0);
			return retVal;
		}
		/**
		 * Sbzje
		 * @return
		 */
		public ST getSbzje() {
			ST retVal = this.getTypedField(12, 0);
			return retVal;
		}
		/**
		 * Mzlsh
		 * @return
		 */
		public ST getMzlsh() {
			ST retVal = this.getTypedField(13, 0);
			return retVal;
		}
		/**
		 * Seeno
		 * @return
		 */
		public ST getSeeno() {
			ST retVal = this.getTypedField(14, 0);
			return retVal;
		}





	    /** {@inheritDoc} */
	    @Override
		protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {

	          case 0: return new TS(getMessage());
	          case 1: return new ST(getMessage());
	          case 2: return new ST(getMessage());
	          case 3: return new ST(getMessage());
	          case 4: return new ST(getMessage());
	          case 5: return new ST(getMessage());
	          case 6: return new ST(getMessage());
	          case 7: return new ST(getMessage());
	          case 8: return new TS(getMessage());
	          case 9: return new CE(getMessage());
	          case 10: return new ST(getMessage());
	          case 11: return new ST(getMessage());
	          case 12: return new ST(getMessage());
	          case 13: return new ST(getMessage());
	         /* case 15: return new ST(getMessage());
	          case 16: return new ST(getMessage());
	          case 17: return new ST(getMessage());*/
	          default: return null;
	       }
	   }
}
