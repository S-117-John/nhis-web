package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.*;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

import java.util.List;
/**
 * 
 * @author maijiaxing
 *
 */
public class RSP_ZDI extends AbstractMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RSP_ZDI() { 
	    this(new DefaultModelClassFactory());
	}

	public RSP_ZDI(ModelClassFactory theFactory) {
		super(theFactory);
		init(theFactory);
		// TODO Auto-generated constructor stub
	}
	
	private void init(ModelClassFactory factory) {
	       try {
	    	   this.add(MSH.class, true, false);
	    	   this.add(MSA.class, true, false);
	    	   this.add(PID.class, true, false);
	    	   this.add(PV1.class, true, true);
	    	   this.add(ZIV.class, false, true);
	    	   this.add(DG1.class, true, true);
		       } catch(HL7Exception e) {
	          log.error("Unexpected error creating QBP_Z11 - this is probably a bug in the source code generator.", e);
	       }
    }
	
	/** 
     * Returns "2.4"
     */
    @Override
    public String getVersion() {
       return "2.4";
    }

    public MSH getMSH() { 
       return getTyped("MSH", MSH.class);
    }

    public MSA getMSA() { 
        return getTyped("MSA", MSA.class);
     }
 	
 	public PID getPID() { 
        return getTyped("PID", PID.class);
     }
     
    /**
     * 
     * @return
     */
 	public PV1 getPV1() { 
        return getTyped("PV1", PV1.class);
     }
 	
 	public PV1 getPV1(int rep) { 
        return getTyped("PV1", rep, PV1.class);
     }
 	
 	public int getPV1Reps() { 
    	return getReps("PV1");
    } 
    
    public java.util.List<PV1> getPV1All() throws HL7Exception {
    	return getAllAsList("PV1", PV1.class);
    } 
    
    public void insertPV1(PV1 structure, int rep) throws HL7Exception { 
        super.insertRepetition( "PV1", structure, rep);
     }

    public PMI insertPV1(int rep) throws HL7Exception { 
        return (PMI)super.insertRepetition("PV1", rep);
     }
    public PMI removePV1(int rep) throws HL7Exception { 
        return (PMI)super.removeRepetition("PV1", rep);
     }
 	/**
 	 * 
 	 * @return
 	 */
 	public ZIV getZIV() { 
        return getTyped("ZIV", ZIV.class);
     }
 	public ZIV getZIV(int rep) { 
        return getTyped("ZIV", rep, ZIV.class);
     }
 	public int getZIVReps() { 
    	return getReps("ZIV");
    } 
    
    public List<ZIV> getZIVAll() throws HL7Exception {
    	return getAllAsList("ZIV", ZIV.class);
    } 
    
    public void insertZIV(ZIV structure, int rep) throws HL7Exception { 
        super.insertRepetition( "ZIV", structure, rep);
     }

    public ZIV insertZIV(int rep) throws HL7Exception { 
        return (ZIV)super.insertRepetition("ZIV", rep);
     }
    public ZIV removeZIV(int rep) throws HL7Exception { 
        return (ZIV)super.removeRepetition("ZIV", rep);
     }
 	
 	/**
 	 * 
 	 * @return
 	 */
 	public DG1 getDG1() { 
        return getTyped("DG1", DG1.class);
     }
 	
 	public DG1 getDG1(int rep) { 
        return getTyped("DG1", rep, DG1.class);
     }
 	public int getDG1Reps() { 
    	return getReps("DG1");
    } 
    
    public List<DG1> getDG1All() throws HL7Exception {
    	return getAllAsList("DG1", DG1.class);
    } 
    
    public void insertDG1(DG1 structure, int rep) throws HL7Exception { 
        super.insertRepetition( "DG1", structure, rep);
     }

    public DG1 insertDG1(int rep) throws HL7Exception { 
        return (DG1)super.insertRepetition("DG1", rep);
     }
    public DG1 removeDG1(int rep) throws HL7Exception { 
        return (DG1)super.removeRepetition("DG1", rep);
     }
 	
}
