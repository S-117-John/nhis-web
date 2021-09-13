package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * 门诊退费申请
 * @author JesusM
 *
 */
public class ZAM_ZRB extends AbstractMessage{


	private static final long serialVersionUID = 6917809454167883659L;

	public ZAM_ZRB() { 
	    this(new DefaultModelClassFactory());
	}
	
	public ZAM_ZRB(ModelClassFactory factory) {
		super(factory);
		init(factory);
	}
	
	private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
    	   this.add(PID.class, true, false);
    	   this.add(PV1.class, true, false);
    	   this.add(ZPM.class, true, true);
	       } catch(HL7Exception e) {
          log.error("Unexpected error creating QBP_Z11 - this is probably a bug in the source code generator.", e);
       }
    }
	
	public MSH getMSH() { 
        return getTyped("MSH", MSH.class);
    }
	
	public PID getPID() { 
        return getTyped("PID", PID.class);
    }
	
	public PV1 getPV1() { 
        return getTyped("PV1", PV1.class);
    }
	
	public ZPM getZPM() { 
        return getTyped("ZPM", ZPM.class);
    }
	public ZPM getZPM(int req) { 
        return getTyped("ZPM",req, ZPM.class);
     }
	public int getZPMReps() { 
    	return getReps("ZPM");
    } 
    
    public java.util.List<ZPM> getZPMAll() throws HL7Exception {
    	return getAllAsList("ZPM", ZPM.class);
    } 
    
    public void insertZPM(ZPM structure, int rep) throws HL7Exception { 
        super.insertRepetition( "ZPM", structure, rep);
     }

    public ZPM insertZPM(int rep) throws HL7Exception { 
        return (ZPM)super.insertRepetition("ZPM", rep);
     }
    public ZPM removeZPM(int rep) throws HL7Exception { 
        return (ZPM)super.removeRepetition("ZPM", rep);
     }
}
