package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.QPD;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/** 
 * 查询住院信息列表 
 * @author maijiaxing
 *
 */
public class QBP_ZDI extends AbstractMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new QBP_ZDI message with DefaultModelClassFactory. 
     */ 
    public QBP_ZDI() { 
       this(new DefaultModelClassFactory());
    }

    /** 
     * Creates a new QBP_ZDI message with custom ModelClassFactory.
     */
    public QBP_ZDI(ModelClassFactory factory) {
       super(factory);
       init(factory);
    }

    private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
           this.add(QPD.class, true, false);
	       } catch(HL7Exception e) {
          log.error("Unexpected error creating QBP_ZDI - this is probably a bug in the source code generator.", e);
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
     */
    public QPD getQPD() { 
       return getTyped("QPD", QPD.class);
    }
    
}
