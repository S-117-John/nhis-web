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
 * 
 * @author maijiaxing
 *
 */
public class RSP_ZPI extends AbstractMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RSP_ZPI() { 
	    this(new DefaultModelClassFactory());
	}
	
	public RSP_ZPI(ModelClassFactory theFactory) {
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
	    	   this.add(ZPO.class, true, true);
	       } catch(HL7Exception e) {
	          log.error("Unexpected error creating QBP_Z11 - this is probably a bug in the source code generator.", e);
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
 	 * ZPO
 	 * @return
 	 */
 	public ZPO getZPO() { 
        return getTyped("ZPO", ZPO.class);
     }
 	
 	public ZPO getZPO(int rep) { 
        return getTyped("ZPO", rep, ZPO.class);
     }
 	public int getZPOReps() { 
    	return getReps("ZPO");
    } 
    
    public java.util.List<ZPO> getZPOAll() throws HL7Exception {
    	return getAllAsList("ZPO", ZPO.class);
    } 
    
    public void insertDG1(ZPO structure, int rep) throws HL7Exception { 
        super.insertRepetition( "ZPO", structure, rep);
     }

    public ZPO insertDG1(int rep) throws HL7Exception { 
        return (ZPO)super.insertRepetition("ZPO", rep);
     }
    public ZPO removeDG1(int rep) throws HL7Exception { 
        return (ZPO)super.removeRepetition("ZPO", rep);
     }
	
}
