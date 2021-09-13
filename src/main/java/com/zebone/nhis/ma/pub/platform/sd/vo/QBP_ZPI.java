package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.QPD;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class QBP_ZPI extends AbstractMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QBP_ZPI() { 
	       this(new DefaultModelClassFactory());
	    }
	
	public QBP_ZPI(ModelClassFactory theFactory) {
		super(theFactory);
		init(theFactory);
		// TODO Auto-generated constructor stub
	}
	
	private void init(ModelClassFactory factory) {
	       try {
	    	   this.add(MSH.class, true, false);
	           this.add(QPD.class, true, false);
		       } catch(HL7Exception e) {
	          log.error("Unexpected error creating QBP_ZPI - this is probably a bug in the source code generator.", e);
	       }
	    }

}
