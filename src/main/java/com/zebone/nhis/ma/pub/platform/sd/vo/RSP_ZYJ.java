package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.ERR;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.QAK;
import ca.uhn.hl7v2.model.v24.segment.QPD;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
/**
 * 查询押金缴入状态
 * @author maijaixing
 *
 */
public class RSP_ZYJ extends AbstractMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RSP_ZYJ() { 
	    this(new DefaultModelClassFactory());
	}
	
	public RSP_ZYJ(ModelClassFactory theFactory) {
		super(theFactory);
		init(theFactory);
		// TODO Auto-generated constructor stub
	}
	
	private void init(ModelClassFactory factory) {
	       try {
	    	   this.add(MSH.class, true, false);
	    	   this.add(MSA.class, true, false);
	    	   this.add(ERR.class, false, false);
	    	   this.add(QAK.class, false, false);
	    	   this.add(QPD.class, false, false);
	    	   this.add(PID.class, true, false);
	    	   this.add(PV1.class, true, false);
	    	   this.add(ZRI.class, true, true);
	       } catch(HL7Exception e) {
	          log.error("Unexpected error creating RSP_ZYJ - this is probably a bug in the source code generator.", e);
       }
	}
	
	/** 
     * Returns "2.4"
     */
    public String getVersion() {
       return "2.4";
    }
    
    /**
     * MSH
     * @return
     */
    public MSH getMSH() { 
       return getTyped("MSH", MSH.class);
    }

    /**
     * MSA
     * @return
     */
    public MSA getMSA() { 
        return getTyped("MSA", MSA.class);
     }
    /**
     * ERR
     * @return
     */
    public ERR getERR() { 
        return getTyped("ERR", ERR.class);
     }
    /**
     * QAK
     * @return
     */
    public QAK getQAK() { 
        return getTyped("QAK", QAK.class);
     }
    /**
     * QPD
     * @return
     */
    public QPD getQPD() { 
        return getTyped("QPD", QPD.class);
     }
    
 	/**
 	 * PID
 	 * @return
 	 */
 	public PID getPID() { 
        return getTyped("PID", PID.class);
     }
     
    /**
     * PV1
     * @return
     */
 	public PV1 getPV1() { 
        return getTyped("PV1", PV1.class);
     }
 	
 	/**
 	 * ZRI
 	 * @return
 	 */
 	public ZRI getZRI() { 
        return getTyped("ZRI", ZRI.class);
     }
 	public ZRI getZRI(int rep) { 
        return getTyped("ZRI", rep, ZRI.class);
     }
 	public int getZRIReps() { 
    	return getReps("ZRI");
    } 
    
    public java.util.List<ZRI> getZRIAll() throws HL7Exception {
    	return getAllAsList("ZRI", ZRI.class);
    } 
    
    public void insertZRI(ZRI structure, int rep) throws HL7Exception { 
        super.insertRepetition( "ZRI", structure, rep);
     }

    public ZRI insertZRI(int rep) throws HL7Exception { 
        return (ZRI)super.insertRepetition("ZRI", rep);
     }
    public ZRI removeZRI(int rep) throws HL7Exception { 
        return (ZRI)super.removeRepetition("ZRI", rep);
     }
 	
}
