package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
/**
 * 住院催缴预交金
 * @author maijiaxing
 *
 */
public class ZDL_TXL extends AbstractMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZDL_TXL() { 
       this(new DefaultModelClassFactory());
	}

    /** 
     * Creates a new SRM_S04 message with custom ModelClassFactory.
     */
    public ZDL_TXL(ModelClassFactory factory) {
       super(factory);
       init(factory);
    }

    private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
           this.add(PID.class, true, false);
           this.add(PV1.class, true, false);
           this.add(TXD.class, true, false);
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
    
    public TXD getTXD() { 
        return getTyped("TXD", TXD.class);
     }
	
}
