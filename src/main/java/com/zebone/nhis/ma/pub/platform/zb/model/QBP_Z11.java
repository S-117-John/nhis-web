package com.zebone.nhis.ma.pub.platform.zb.model;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.QPD;
import ca.uhn.hl7v2.model.v24.segment.RCP;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;



/** 排班资源信息查询
 * @author chengjia
 *
 */
public class QBP_Z11 extends AbstractMessage {
	/**
     * Creates a new QBP_Z11 message with DefaultModelClassFactory. 
     */ 
    public QBP_Z11() { 
       this(new DefaultModelClassFactory());
    }

    /** 
     * Creates a new QBP_Z11 message with custom ModelClassFactory.
     */
    public QBP_Z11(ModelClassFactory factory) {
       super(factory);
       init(factory);
    }

    private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
           this.add(QPD.class, true, false);
           this.add(RCP.class, true, false);
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

    /**
     * <p>
     * Returns
     * EVN (Event Type) - creates it if necessary
     * </p>
     * 
     *
     */
    public QPD getQPD() { 
       return getTyped("QPD", QPD.class);
    }

    /**
     * <p>
     * Returns
     * PID (Patient identification) - creates it if necessary
     * </p>
     * 
     *
     */
    public RCP getRCP() { 
       return getTyped("RCP", RCP.class);
    }



}
