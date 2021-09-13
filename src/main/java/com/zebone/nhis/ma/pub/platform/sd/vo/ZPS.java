package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v21.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.ID;
import ca.uhn.hl7v2.model.v24.datatype.IS;
import ca.uhn.hl7v2.model.v24.datatype.SI;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ZPS extends AbstractSegment{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ZPS(Group parent, ModelClassFactory factory) {
		super(parent, factory);
		init(factory);
		// TODO Auto-generated constructor stub
	}

	private void init(ModelClassFactory factory) {
	    try {
	    	//1	 	Control		控制	ID	R	2	
	    	this.add(ID.class, false, 1, 2, new Object[]{ getMessage() }, "Control");
	    	//2	 	Set ID ZPS	ZPS的序号	SI	R	4	
	    	this.add(SI.class, false, 1, 4, new Object[]{ getMessage() }, "Set ID ZPS");
	    	//3	 	PGXM		评估项目	CE	R	30	e.g.BADL^基本生活活动能力
	    	//3.1	Identifier	评估项目编码	ST	R	10	e.g. BADL
	    	//3.2	Text		评估项目名称	ST	R	50	e.g. 基本生活活动能力
	    	this.add(CE.class, false, 1, 30, new Object[]{ getMessage() }, "PGXM");
	    	//4		PGZ		评估值	ST	R	10	e.g. 70
	    	this.add(ST.class, false, 1, 10, new Object[]{ getMessage()}, "PGZ");
	    	//5		Unit	  单位	 ST	O	50
	    	this.add(ST.class, false, 1, 50, new Object[]{ getMessage() }, "Unit");
	    	//6		PGMS	  评估描述	ST	R	100	e.g.生活基本自理
	    	this.add(ST.class, false, 1, 100, new Object[]{ getMessage() }, "PGMS");
	    	//7		LRHS	  录入护士	XCN	O	60	e.g. w001^王护士
	    	//7.1	IDNumber 录入护士编码	IS	O	6	e.g. w001
	    	//7.2	FamilyName.Surname	录入护士姓名	IS	O	50	e.g. 王护士
	    	this.add(XCN.class, false, 1, 60, new Object[]{ getMessage() }, "Breath");
	    	//8		LRSJ	   录入时间	TS	O	26	e.g. 20180413140701
	    	this.add(TS.class, false, 1, 2, new Object[]{ getMessage() }, "LRHS");
	    	//9		SHHS	   审核护士	XCN	O	60	e.g. w002^张护士
	    	//9.1	IDNumber 审核护士编码	IS	O	6	e.g. w002
	    	//9.2	FamilyName.Surname	审核护士姓名	IS	O	50	e.g. 张护士
	    	this.add(XCN.class, false, 1, 2, new Object[]{ getMessage() }, "LRSJ");
	    	//10	SHSJ	审核时间	TS	O	26	e.g. 20180413150701
	    	this.add(TS.class, false, 1, 2, new Object[]{ getMessage() }, "SHHS");
	    	//11	SCBZ	首次评估标识	IS	O	2	e.g. 1
	    	this.add(IS.class, false, 1, 2, new Object[]{ getMessage() }, "SHSJ");
	    } catch (HL7Exception e) {
	        log.error("Unexpected error creating ZOR - this is probably a bug in the source code generator.", e);
	    }
	 }
	/**
	 * 1	Control	控制	ID	R	2
	 * @return
	 */
	public ID getControl() { 
		ID retVal = this.getTypedField(1, 0);
		return retVal;
    }
	
	/**
	 * 2	Set ID ‑ ZPS	ZPS的序号	SI	R	4
	 * @return
	 */
	public SI getID() { 
		SI retVal = this.getTypedField(2, 0);
		return retVal;
    }
	/**
	 * 3	PGXM	评估项目	CE	R	30
		3.1	Identifier	评估项目编码	ST	R	10
		3.2	Text	评估项目名称	ST	R	50
	 * @return
	 */
	public CE getPGXM() { 
		CE retVal = this.getTypedField(3, 0);
		return retVal;
    }
	/**
	 * 4	PGZ	评估值	ST	R	10
	 * @return
	 */
	public ST getPGZ() { 
		ST retVal = this.getTypedField(4, 0);
		return retVal;
    }
	/**
	 * 5	Unit	单位	ST	O	50
	 * @return
	 */
	public ST getUnit() { 
		ST retVal = this.getTypedField(5, 0);
		return retVal;
    }
	/**
	 * 6	PGMS	评估描述	ST	R	100
	 * @return
	 */
	public ST getPGMS() { 
		ST retVal = this.getTypedField(6, 0);
		return retVal;
    }
	/**
	 * 7	LRHS	录入护士	XCN	O	60
	7.1	IDNumber	录入护士编码	IS	O	6
	7.2	FamilyName.Surname	录入护士姓名	IS	O	50
	 * @return
	 */
	public XCN getLRHS() { 
		XCN retVal = this.getTypedField(7, 0);
		return retVal;
    }
	/**
	 * 8	LRSJ	录入时间	TS	O	26
	 * @return
	 */
	public TS getLRSJ() { 
		TS retVal = this.getTypedField(8, 0);
		return retVal;
    }
	/**
	 * 9	SHHS	审核护士	XCN	O	60
	 * 9.1	IDNumber	审核护士编码	IS	O	6
	9.2	FamilyName.Surname	审核护士姓名	IS	O	50
	 * @return
	 */
	public XCN getSHHS() { 
		XCN retVal = this.getTypedField(9, 0);
		return retVal;
    }
	/**
	 * 10	SHSJ	审核时间	TS	O	26
	 * @return
	 */
	public TS getSHSJ() { 
		TS retVal = this.getTypedField(10, 0);
		return retVal;
    }
	/**
	 * 11	SCBZ	首次评估标识	IS	O	2
	 * @return
	 */
	public IS getSCBZ() { 
		IS retVal = this.getTypedField(11, 0);
		return retVal;
    }
	/** {@inheritDoc} */   
    protected Type createNewTypeWithoutReflection(int field) {
       switch (field) {
          case 0: return new ID(getMessage());
          case 1: return new ID(getMessage());
          case 2: return new SI(getMessage());
          case 3: return new CE(getMessage());
          case 4: return new ST(getMessage());
          
          case 5: return new ST(getMessage());
          case 6: return new ST(getMessage());
          case 7: return new XCN(getMessage());
          case 8: return new TS(getMessage());
          case 9: return new XCN(getMessage());
          case 10: return new TS(getMessage());
          case 11: return new IS(getMessage());

          default: return null;
       }
   }

}





