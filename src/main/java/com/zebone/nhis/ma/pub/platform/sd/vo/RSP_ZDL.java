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

public class RSP_ZDL extends AbstractMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RSP_ZDL() { 
	    this(new DefaultModelClassFactory());
	}

	public RSP_ZDL(ModelClassFactory theFactory) {
		super(theFactory);
		init(theFactory);
		// TODO Auto-generated constructor stub
	}
	
	private void init(ModelClassFactory factory) {
	       try {
	    	   this.add(MSH.class, true, false);
	    	   this.add(MSA.class, true, false);
	    	   this.add(ERR.class, true, false);
	    	   this.add(QAK.class, true, false);
	    	   this.add(QPD.class, true, false);
	    	   this.add(PID.class, true, false);
	    	   this.add(PV1.class, true, true);
	    	   this.add(ZIV.class, false, true);
	    	   this.add(ZDL.class, true, true);
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

    public MSH getMSH() { 
       return getTyped("MSH", MSH.class);
    }

    public MSA getMSA() { 
        return getTyped("MSA", MSA.class);
     }
    
    public ERR getERR() { 
        return getTyped("ERR", ERR.class);
     }
    
    public QAK getQAK() { 
        return getTyped("QAK", QAK.class);
     }
    
    public QPD getQPD() { 
        return getTyped("QPD", QPD.class);
     }
 	
 	public PID getPID() { 
        return getTyped("PID", PID.class);
     }
 	
 	public PV1 getPV1() { 
        return getTyped("PV1", PV1.class);
     }

 	public ZIV getZIV() { 
        return getTyped("ZIV", ZIV.class);
     }
 	public ZDL getZDL() { 
        return getTyped("ZDL", ZDL.class);
     }
}
