package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v21.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.NM;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class Z11 extends AbstractSegment {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public Z11(Group parent, ModelClassFactory factory) {
		super(parent, factory);
		// TODO Auto-generated constructor stub
		init(factory);
	}


	 private void init(ModelClassFactory factory) {
	    try {
	    	//ID	序号	TS	R	26
	    	this.add(ST.class, false, 1, 26, new Object[]{ getMessage() }, "ID");
	    	//SCHEMA_TYPE  排班类型   0科室/1医生	ST	O	4
	    	this.add(ST.class, false, 1, 1, new Object[]{ getMessage() }, "SCHEMA_TYPE");
	    	//SEE_DATE	看诊日期	ST	O	20
	    	this.add(TS.class, false, 1, 20, new Object[]{ getMessage() }, "SEE_DATE");
	    	//WEEK	星期	ST	O	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "WEEK");
	    	//BEGIN_TIME	开始时间	TS	O	20
	    	this.add(TS.class, false, 1, 20, new Object[]{ getMessage() }, "BEGIN_TIME");


	    	//END_TIME	结束时间	TS	O	20
	    	this.add(TS.class, false, 1, 20, new Object[]{ getMessage() }, "END_TIME");
	    	//DEPT_CODE	科室代号	ST	O	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "DEPT_CODE");
	    	//DEPT_NAME	科室名称	ST	O	26
	    	this.add(ST.class, false, 1, 26, new Object[]{ getMessage() }, "DEPT_NAME");
	    	//DOCT_CODE	医师代号	ST	O	1
	    	this.add(ST.class, false, 1, 1, new Object[]{ getMessage() }, "DOCT_CODE");
	    	//DOCT_NAME	医生姓名	ST	O	250
	    	this.add(ST.class, false, 1, 250, new Object[]{ getMessage() }, "DOCT_NAME");



	    	//DOCT_TYPE	医生类型	ST	O	10
	    	this.add(ST.class, false, 1, 10, new Object[]{ getMessage() }, "DOCT_TYPE");
	    	//REG_LMT	来人挂号限额	NM	O	20
	    	this.add(NM.class, false, 1, 20, new Object[]{ getMessage() }, "REG_LMT");
	    	//REGED	挂号已挂	NM	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "REGED");
	    	//TEL_LMT	来电挂号限额	NM	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "TEL_LMT");
	    	//TEL_REGED  来电已挂	NM	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "TEL_REGED");




	    	//TEL_REGING 来电已预约	NM	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "TEL_REGING");
	    	//SPE_LMT 特诊挂号限额	NM	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "SPE_LMT");
	    	//SPE_REGED  特诊已挂	NM	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "SPE_REGED");
	    	//VALID_FLAG 1正常/0停诊	ST	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "VALID_FLAG");
	    	//APPEND_FLAG	ST  1加号/0否	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "APPEND_FLAG");



	    	//REASON_NO  停诊原因	ST	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "REASON_NO");
	    	//REASON_NAME	ST  停诊原因名称	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "REASON_NAME");
	    	//STOP_OPCD	  停止人   ST  	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "STOP_OPCD");
	    	//STOP_DATE	停止时间    TS	O	50
	    	this.add(TS.class, false, 1, 50, new Object[]{ getMessage() }, "STOP_DATE");
	    	//ORDER_NO 顺序号	NM	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "ORDER_NO");



	    	//REGLEVL_CODE	 挂号级别代码  ST 	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "REGLEVL_CODE");
	    	//REGLEVL_NAME   挂号级别名称	ST 	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "REGLEVL_NAME");
	    	//REMARK   备注	ST  O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "REMARK");
	    	//OPER_CODE   操作员	ST  O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "OPER_CODE");
	    	//OPER_DATE    最近改动日期	TS 	O	50
	    	this.add(TS.class, false, 1, 50, new Object[]{ getMessage() }, "OPER_DATE");


	      	//NOON_ID	午别   ST 	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "NOON_ID");
	    	//INTERVAL  排班时间间隔	NM 	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "INTERVAL");
	    	//WEB_LMT   网站挂号限额	NM  O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "WEB_LMT");
	    	//WEIXIN_LMT   微信挂号限额	NM  O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "WEIXIN_LMT");
	    	//OTHER_LMT    预留挂号限额	NM 	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "OTHER_LMT");
	     	
	    	//RmngNum    剩余号源	NM 	O	50
	    	this.add(NM.class, false, 1, 50, new Object[]{ getMessage() }, "RmngNum");
	    	//CODE_RES	资源编码	ST	O	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "CODE_RES");
	    	//NAME_RES	资源名称	ST	O	26
	    	this.add(ST.class, false, 1, 26, new Object[]{ getMessage() }, "NAME_RES");

	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZFI - this is probably a bug in the source code generator.", e);
	    }
	 }

	 /**
		 * 1  ID	提醒类型	20
		 * @return
		 */
		public ST getID() {
			ST retVal = this.getTypedField(1, 0);
			return retVal;
		}
		/**
		 *
		 * @return
		 */
		public ST getSCHEMA_TYPE() {
			ST retVal = this.getTypedField(2, 0);
			return retVal;
		}
		/**
		 * BeginTime
		 * @return
		 */
		public TS getSEE_DATE() {
			TS retVal = this.getTypedField(3, 0);
			return retVal;
		}
		/**
		 * EndTime
		 */
		public ST getWEEK() {
			ST retVal = this.getTypedField(4, 0);
			return retVal;
		}
		/*8
		 * PayMtate
		 */
		public TS getBEGIN_TIME() {
			TS retVal = this.getTypedField(5, 0);
			return retVal;
		}
		/**
		 * PayMethod
		 * @return
		 */
		public TS getEND_TIME() {
			TS retVal = this.getTypedField(6, 0);
			return retVal;
		}
		/**
		 * YuYueTtate
		 * @return
		 */
		public ST getDEPT_CODE() {
			ST retVal = this.getTypedField(7, 0);
			return retVal;
		}
		/**
		 * OrderTime
		 * @return
		 */
		public ST getDEPT_NAME() {
			ST retVal = this.getTypedField(8, 0);
			return retVal;
		}
		/**
		 * Printed
		 * @return
		 */
		public ST getDOCT_CODE() {
			ST retVal = this.getTypedField(9, 0);
			return retVal;
		}
		/**
		 * CHN
		 * @return
		 */
		public ST getDOCT_NAME() {
			ST retVal = this.getTypedField(10, 0);
			return retVal;
		}






		public ST getDOCT_TYPE() {
			ST retVal = this.getTypedField(11, 0);
			return retVal;
		}

		public NM getREG_LMT() {
			NM retVal = this.getTypedField(12, 0);
			return retVal;
		}
		public NM getREGED() {
			NM retVal = this.getTypedField(13, 0);
			return retVal;
		}

		public NM getTEL_LMT() {
			NM retVal = this.getTypedField(14, 0);
			return retVal;
		}

		public NM getTEL_REGED() {
			NM retVal = this.getTypedField(15, 0);
			return retVal;
		}

		public NM getTEL_REGING() {
			NM retVal = this.getTypedField(16, 0);
			return retVal;
		}
		public NM getSPE_LMT() {
			NM retVal = this.getTypedField(17, 0);
			return retVal;
		}

		public NM getSPE_REGED() {
			NM retVal = this.getTypedField(18, 0);
			return retVal;
		}

		public ST getVALID_FLAG() {
			ST retVal = this.getTypedField(19, 0);
			return retVal;
		}

		public ST getAPPEND_FLAG() {
			ST retVal = this.getTypedField(20, 0);
			return retVal;
		}







		public ST getREASON_NO() {
			ST retVal = this.getTypedField(21, 0);
			return retVal;
		}

		public ST getREASON_NAME() {
			ST retVal = this.getTypedField(22, 0);
			return retVal;
		}
		public ST getSTOP_OPCD() {
			ST retVal = this.getTypedField(23, 0);
			return retVal;
		}

		public TS getSTOP_DATE() {
			TS retVal = this.getTypedField(24, 0);
			return retVal;
		}

		public NM getORDER_NO() {
			NM retVal = this.getTypedField(25, 0);
			return retVal;
		}

		public ST getREGLEVL_CODE() {
			ST retVal = this.getTypedField(26, 0);
			return retVal;
		}
		public ST getREGLEVL_NAME() {
			ST retVal = this.getTypedField(27, 0);
			return retVal;
		}

		public ST getREMARK() {
			ST retVal = this.getTypedField(28, 0);
			return retVal;
		}

		public ST getOPER_CODE() {
			ST retVal = this.getTypedField(29, 0);
			return retVal;
		}

		public TS getOPER_DATE() {
			TS retVal = this.getTypedField(30, 0);
			return retVal;
		}





		public ST getNOON_ID() {
			ST retVal = this.getTypedField(31, 0);
			return retVal;
		}

		public NM getINTERVAL() {
			NM retVal = this.getTypedField(32, 0);
			return retVal;
		}
		public NM getWEB_LMT() {
			NM retVal = this.getTypedField(33, 0);
			return retVal;
		}

		public NM getWEIXIN_LMT() {
			NM retVal = this.getTypedField(34, 0);
			return retVal;
		}

		public NM getOTHER_LMT() {
			NM retVal = this.getTypedField(35, 0);
			return retVal;
		}

		public NM getRmngNum() {
			NM retVal = this.getTypedField(36, 0);
			return retVal;
		}
		
		public ST getCODE_RES() {
			ST retVal = this.getTypedField(37, 0);
			return retVal;
		}

		public ST getNAME_RES() {
			ST retVal = this.getTypedField(38, 0);
			return retVal;
		}








	    /** {@inheritDoc} */
	    @Override
		protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {

	          case 0: return new ST(getMessage());
	          case 1: return new ST(getMessage());
	          case 2: return new TS(getMessage());
	          case 3: return new ST(getMessage());
	          case 4: return new TS(getMessage());


	          case 5: return new TS(getMessage());
	          case 6: return new ST(getMessage());
	          case 7: return new ST(getMessage());
	          case 8: return new ST(getMessage());
	          case 9: return new ST(getMessage());


	          case 10: return new ST(getMessage());
	          case 11: return new NM(getMessage());
	          case 12: return new NM(getMessage());
	          case 13: return new NM(getMessage());
	          case 14: return new NM(getMessage());


	          case 15: return new NM(getMessage());
	          case 16: return new NM(getMessage());
	          case 17: return new NM(getMessage());
	          case 18: return new ST(getMessage());
	          case 19: return new ST(getMessage());


	          case 20: return new ST(getMessage());
	          case 21: return new ST(getMessage());
	          case 22: return new ST(getMessage());
	          case 23: return new TS(getMessage());
	          case 24: return new NM(getMessage());


	          case 25: return new ST(getMessage());
	          case 26: return new ST(getMessage());
	          case 27: return new ST(getMessage());
	          case 28: return new ST(getMessage());
	          case 29: return new TS(getMessage());


	          case 30: return new ST(getMessage());
	          case 31: return new NM(getMessage());
	          case 32: return new NM(getMessage());
	          case 33: return new NM(getMessage());
	          case 34: return new NM(getMessage());
	          case 35: return new NM(getMessage());
	          
	          case 36: return new ST(getMessage());
	          case 37: return new ST(getMessage());

	          default: return null;
	       }
	   }
}
