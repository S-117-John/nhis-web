package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.PV2;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ZPI_PSI extends AbstractMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZPI_PSI() { 
	       this(new DefaultModelClassFactory());
		}
	
	public ZPI_PSI(ModelClassFactory theFactory) {
		super(theFactory);
		init(theFactory);
		// TODO Auto-generated constructor stub
	}
	
	private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
    	   this.add(EVN.class, true, false);
           this.add(PID.class, true, false);
           this.add(PV1.class, true, false);
           this.add(PV2.class, true, false);
           this.add(ZPS.class, true, true);
           this.add(NTE.class, true, false);
	       } catch(HL7Exception e) {
	    	   log.error("Unexpected error creating ZPI_PMI - this is probably a bug in the source code generator.", e);
       }
    }
	/** 
     * Returns "2.4"
     */
    public String getVersion() {
       return "2.4";
    }
    
    public PID getPID() { 
        return getTyped("PID", PID.class);
     }
    
    public PV1 getPV1() { 
        return getTyped("PV1", PV1.class);
     }
    
    public MSH getMSH() { 
        return getTyped("MSH", MSH.class);
     }
    
    public EVN getEVN() { 
        return getTyped("EVN", EVN.class);
     }
    
    public ZPS getZPS() { 
        return getTyped("ZPS", ZPS.class);
     }
 	
    public ZPS getZPS(int rep) { 
       return getTyped("ZPS", rep, ZPS.class);
    }

    public int getZPSReps() { 
    	return getReps("ZPS");
    } 

    public java.util.List<ZPS> getZPSAll() throws HL7Exception {
    	return getAllAsList("ZPS", ZPS.class);
    } 

    public void insertZPS(ZPS structure, int rep) throws HL7Exception { 
       super.insertRepetition( "ZPS", structure, rep);
    }

    public ZPS insertZPS(int rep) throws HL7Exception { 
       return (ZPS)super.insertRepetition("ZPS", rep);
    }

    public ZPS removeZPS(int rep) throws HL7Exception { 
       return (ZPS)super.removeRepetition("ZPS", rep);
    }

}
