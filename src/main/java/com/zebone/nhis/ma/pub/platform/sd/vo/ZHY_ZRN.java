package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ZHY_ZRN extends AbstractMessage {

	private static final long serialVersionUID = 1L;


	public ZHY_ZRN() {
	    this(new DefaultModelClassFactory());
	}

	public ZHY_ZRN(ModelClassFactory factory) {
		super(factory);
		init(factory);
	}

	private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
           this.add(ZRN.class, true, false);

	       } catch(HL7Exception e) {
          log.error("Unexpected error creating QBP_Z11 - this is probably a bug in the source code generator.", e);
       }
    }


    @Override
	public String getVersion() {
       return "2.4";
    }

    public MSH getMSH() {
       return getTyped("MSH", MSH.class);
    }

	public ZRN getZRN() {
       return getTyped("ZRN", ZRN.class);
    }

}
