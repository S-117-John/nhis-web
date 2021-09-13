package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.FT1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * 住院发票
 * @author maijiaxing
 *
 */
public class ZPI_PMI extends AbstractMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ZPI_PMI() { 
       this(new DefaultModelClassFactory());
    }

    /** 
     * Creates a new SRM_S04 message with custom ModelClassFactory.
     */
    public ZPI_PMI(ModelClassFactory factory) {
       super(factory);
       init(factory);
    }

    private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
           this.add(PID.class, true, false);
           this.add(PV1.class, true, false);
           this.add(FT1.class, false, false);
           this.add(ZFP.class, false, false);
           this.add(PMI.class, false, true);
           this.add(OBR.class, false, true);
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
    
    public FT1 getFT1() { 
        return getTyped("FT1", FT1.class);
     }
    
    public ZFP getZFP() { 
        return getTyped("ZFP", ZFP.class);
     }
    
    public PMI getPMI() { 
        return getTyped("PMI", PMI.class);
     }
    
    public PMI getPMI(int req) { 
        return getTyped("PMI",req, PMI.class);
     }
    
    public int getPMIReps() { 
    	return getReps("PMI");
    } 
    
    public java.util.List<PMI> getPMIAll() throws HL7Exception {
    	return getAllAsList("PMI", PMI.class);
    } 
    
    public void insertPMI(PMI structure, int rep) throws HL7Exception { 
        super.insertRepetition( "PMI", structure, rep);
     }

    public PMI insertPMI(int rep) throws HL7Exception { 
        return (PMI)super.insertRepetition("PMI", rep);
     }
    public PMI removePMI(int rep) throws HL7Exception { 
        return (PMI)super.removeRepetition("PMI", rep);
     }
    public PMI getOBR() { 
        return getTyped("OBR", PMI.class);
     }
    
    public OBR getOBR(int req) { 
        return getTyped("OBR",req, OBR.class);
     }
    
    public int getOBRReps() { 
    	return getReps("OBR");
    } 
    
    public java.util.List<OBR> getOBRAll() throws HL7Exception {
    	return getAllAsList("OBR", OBR.class);
    } 
    
    public void insertOBR(OBR structure, int rep) throws HL7Exception { 
        super.insertRepetition( "OBR", structure, rep);
     }

    public OBR insertOBR(int rep) throws HL7Exception { 
        return (OBR)super.insertRepetition("OBR", rep);
     }
    public OBR removeOBR(int rep) throws HL7Exception { 
        return (OBR)super.removeRepetition("OBR", rep);
     }
}
