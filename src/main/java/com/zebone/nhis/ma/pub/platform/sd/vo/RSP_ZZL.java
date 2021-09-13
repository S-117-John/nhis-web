package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.QPD;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * RSP_ZZL
 * @author maijiaxing
 *
 */
public class RSP_ZZL extends AbstractMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RSP_ZZL() { 
	    this(new DefaultModelClassFactory());
	}

	public RSP_ZZL(ModelClassFactory theFactory) {
		super(theFactory);
		init(theFactory);
		// TODO Auto-generated constructor stub
	}
	
	private void init(ModelClassFactory factory) {
	       try {
	    	   this.add(MSH.class, true, false);
	    	   this.add(MSA.class, true, false);
	    	   this.add(QPD.class, false, false);
	    	   this.add(PID.class, true, false);
	    	   this.add(PV1.class, true, true);
	    	   this.add(ZIV.class, true, true);
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
  	
  	public PID getPID() { 
         return getTyped("PID", PID.class);
      }
  	
  	public QPD getQPD() { 
        return getTyped("QPD", QPD.class);
     }
      
     /**
      * 
      * @return
      */
  	public PV1 getPV1() { 
         return getTyped("PV1", PV1.class);
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
    
    public java.util.List<ZIV> getZIVAll() throws HL7Exception {
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
  	public ZDL getZDL() { 
         return getTyped("ZDL", ZDL.class);
      }
  	
  	public ZDL getZDL(int rep) { 
         return getTyped("ZDL", rep, ZDL.class);
      }
  	public int getZDLReps() { 
     	return getReps("ZDL");
     } 
     
     public java.util.List<ZDL> getZDLAll() throws HL7Exception {
     	return getAllAsList("ZDL", ZDL.class);
     } 
     
     public void insertZDL(ZDL structure, int rep) throws HL7Exception { 
         super.insertRepetition( "ZDL", structure, rep);
      }

     public ZDL insertZDL(int rep) throws HL7Exception { 
         return (ZDL)super.insertRepetition("ZDL", rep);
      }
     public ZDL removeZDL(int rep) throws HL7Exception { 
         return (ZDL)super.removeRepetition("ZDL", rep);
      }
}
