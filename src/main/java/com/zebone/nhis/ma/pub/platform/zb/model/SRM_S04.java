package com.zebone.nhis.ma.pub.platform.zb.model;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.ARQ;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * 灵璧自助机退号
 * @author cuixiaoshan
 *
 */
public class SRM_S04 extends AbstractMessage {

	public SRM_S04() { 
       this(new DefaultModelClassFactory());
    }

    /** 
     * Creates a new SRM_S04 message with custom ModelClassFactory.
     */
    public SRM_S04(ModelClassFactory factory) {
       super(factory);
       init(factory);
    }

    private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
           this.add(ARQ.class, true, false);
           this.add(PID.class, true, false);
           this.add(PV1.class, true, false);
	       } catch(HL7Exception e) {
          log.error("Unexpected error creating SRM_S04 - this is probably a bug in the source code generator.", e);
       }
    }
    
    /** 
     * Returns "2.4"
     */
    public String getVersion() {
       return "2.4";
    }
    
    public ARQ getARQ() { 
       return getTyped("ARQ", ARQ.class);
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
}
