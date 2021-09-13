package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ACK_P03 extends AbstractMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new ACK_P03 message with DefaultModelClassFactory. 
     */ 
	public ACK_P03() { 
	    this(new DefaultModelClassFactory());
	}
	
	/** 
     * Creates a new ACK_P03 message with custom ModelClassFactory.
     */
	public ACK_P03(ModelClassFactory factory) {
		super(factory);
		init(factory);
	}
	
	private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
           this.add(MSA.class, true, false);
           this.add(PID.class, true, false);
           this.add(PV1.class, true, false);
           this.add(ZPO.class, false, false);
           this.add(NTE.class, true, true);
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
     * <p>
     * Returns
     * MSH (Message Header) - creates it if necessary
     * </p>
     * 
     *
     */
    public MSH getMSH() { 
       return getTyped("MSH", MSH.class);
    }
    
    
	public MSA getMSA() { 
       return getTyped("MSA", MSA.class);
    }
	
	public PID getPID() { 
       return getTyped("PID", PID.class);
    }
    
    
	public PV1 getPV1() { 
       return getTyped("PV1", PV1.class);
    }
	
	public ZPO getZPO() { 
       return getTyped("ZPO", ZPO.class);
    }
    
	public NTE getNTE() { 
       return getTyped("NTE",NTE.class);
    }
    
	public NTE getNTE(int rep) { 
       return getTyped("NTE",rep, NTE.class);
    }
	
	public int getNTEReps() { 
    	return getReps("NTE");
    } 
	
	public java.util.List<NTE> getNTEAll() throws HL7Exception {
    	return getAllAsList("NTE", NTE.class);
    } 
	
	public void insertNTE(NTE structure, int rep) throws HL7Exception { 
       super.insertRepetition( "NTE", structure, rep);
    }
	
	public NTE insertNTE(int rep) throws HL7Exception { 
       return (NTE)super.insertRepetition("NTE", rep);
    }
	
	public NTE removeNTE(int rep) throws HL7Exception { 
       return (NTE)super.removeRepetition("NTE", rep);
    }
}
