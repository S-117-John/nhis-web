package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.QPD;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * 住院费每日账单列表(Theme)
 * @author maijaixing
 *
 */
public class QBP_ZZL extends AbstractMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QBP_ZZL() {
	       this(new DefaultModelClassFactory());
	    }

	public QBP_ZZL(ModelClassFactory factory) {
		super(factory);
		// TODO Auto-generated constructor stub
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
    @Override
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
