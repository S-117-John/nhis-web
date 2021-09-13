package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v21.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.MO;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ZIV extends AbstractSegment{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZIV(Group parent, ModelClassFactory factory) {
		super(parent, factory);
		init(factory);
		// TODO Auto-generated constructor stub
	}
	
	
	 private void init(ModelClassFactory factory) {
	    try {
	    	//Fph	发票号	ST	O	32
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Fph");
	    	//Id	处方单序号或检查单序号	ST	O	32
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Id");
	    	//Jsfs	结算方式	CE	O	32
	    	this.add(CE.class, false, 1, 32, new Object[]{ getMessage() }, "Jsfs");
	    	//Jzje	记账金额	MO	O	12
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Jzje");
	    	//Grjf	个人缴费	MO	O	12
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Grjf");
	    	//Sr	舍入金额	MO	O	12
	    	this.add(MO.class, false, 1, 12, new Object[]{ getMessage() }, "Sr");
	    	//Sfy	收费员	ST	O	32
	    	this.add(ST.class, false, 1, 32, new Object[]{ getMessage() }, "Sfy");
	    	//Ghrq	记账日期	TS	O	26
	    	this.add(TS.class, false, 1, 26, new Object[]{ getMessage() }, "Ghrq");
	    	//Fprq	发票日期	TS	O	10
	    	this.add(TS.class, false, 1, 10, new Object[]{ getMessage() }, "Fprq");
	    	//Yjze	押金总额	ST	R	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "Yjze");
	    	//Yjkyye	押金可用余额	ST	R	20
	    	this.add(ST.class, false, 1, 20, new Object[]{ getMessage() }, "Yjkyye");
	    	//YjisLow	押金余额是否低	ST	R	30
	    	this.add(ST.class, false, 1, 30, new Object[]{ getMessage() }, "YjisLow");
	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZMS - this is probably a bug in the source code generator.", e);
	    }
	 }
	 
	 /**
	  * 1	Fph	发票号	ST
	  * @return
	  */
	 public ST getFph() { 
		 ST retVal = this.getTypedField(1, 0);
		return retVal;
    }
	 /**
	  * 2	Id	处方单序号或检查单序号	ST
	  * @return
	  */
	 public ST getId() { 
		 ST retVal = this.getTypedField(2, 0);
		return retVal;
    }	
	 
	 /**
	  * 3	Jsfs	结算方式	CE
	  * @return
	  */
	 public CE getJsfs() { 
		 CE retVal = this.getTypedField(3, 0);
		return retVal;
    }
	/**
	 *  4	Jzje	记账金额	MO
	 * @return
	 */
	 public MO getJzje() { 
		 MO retVal = this.getTypedField(4, 0);
		return retVal;
    }
	 /**
	  * 5	Grjf	个人缴费	MO
	  * @return
	  */
	 public MO getGrjf() { 
		 MO retVal = this.getTypedField(5, 0);
		return retVal;
    }
	 /**
	  * 6	Sr	舍入金额	MO
	  * @return
	  */
	 public MO getSr() { 
		 MO retVal = this.getTypedField(6, 0);
		return retVal;
    }
	 /**
	  * 7	Sfy	收费员	ST
	  * @return
	  */
	 public ST getSfy() { 
		 ST retVal = this.getTypedField(7, 0);
		return retVal;
    }
	 /**
	  * 8	Ghrq	记账日期	TS
	  * @return
	  */
	 public TS getGhrq() { 
		 TS retVal = this.getTypedField(8, 0);
		return retVal;
    }
	 /**
	  * 9	Fprq	发票日期	TS
	  * @return
	  */
	 public TS getFprq() { 
		 TS retVal = this.getTypedField(9, 0);
		return retVal;
    }
	 /**
	  * 10	Yjze	押金总额	ST
	  * @return
	  */
	 public ST getYjze() { 
		 ST retVal = this.getTypedField(10, 0);
		return retVal;
    }
	 /**
	  * 11	Yjkyye	押金可用余额	ST
	  * @return
	  */
	 public ST getYjkyye() { 
		 ST retVal = this.getTypedField(11, 0);
		return retVal;
    }
	 /**
	  * 12	YjisLow	押金余额是否低	ST
	  * @return
	  */
	 public ST getYjisLow() { 
		 ST retVal = this.getTypedField(12, 0);
		return retVal;
    }

	 protected Type createNewTypeWithoutReflection(int field) {
	       switch (field) {
	          case 0: return new ST(getMessage());
	          case 1: return new ST(getMessage());
	          case 2: return new CE(getMessage());
	          case 3: return new MO(getMessage());
	          case 4: return new MO(getMessage());
	          case 5: return new MO(getMessage());
	          case 6: return new ST(getMessage());
	          case 7: return new TS(getMessage());
	          case 8: return new TS(getMessage());
	          case 9: return new ST(getMessage());
	          case 10: return new ST(getMessage());
	          case 11: return new ST(getMessage());
	          
	          default: return null;
	       }
	   }

}
