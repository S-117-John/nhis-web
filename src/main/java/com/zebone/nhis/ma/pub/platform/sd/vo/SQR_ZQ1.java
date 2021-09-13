package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.ARQ;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class SQR_ZQ1 extends AbstractMessage  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SQR_ZQ1() {
	    this(new DefaultModelClassFactory());
	}

	public SQR_ZQ1(ModelClassFactory theFactory) {
		super(theFactory);
		init(theFactory);
		// TODO Auto-generated constructor stub
	}

	private void init(ModelClassFactory factory) {
	       try {
	    	   this.add(MSH.class, true, false);
	    	   this.add(MSA.class, true, false);
	    	   this.add(ARQ.class, true, true);
	           this.add(ZAI.class, true, true);
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




 	/**
 	 *
 	 * @param rep
 	 * @return
 	 */
 	public ARQ getARQ() {
        return getTyped("ARQ", ARQ.class);
     }

    public ARQ getARQ(int rep) {
       return getTyped("ARQ", rep, ARQ.class);
    }

    public int getARQReps() {
    	return getReps("ARQ");
    }

    public java.util.List<ARQ> getARQAll() throws HL7Exception {
    	return getAllAsList("ARQ", ARQ.class);
    }

    public void insertARQ(ARQ structure, int rep) throws HL7Exception {
       super.insertRepetition( "ARQ", structure, rep);
    }

    public ARQ insertARQ(int rep) throws HL7Exception {
       return (ARQ)super.insertRepetition("ARQ", rep);
    }

    public ARQ removeARQ(int rep) throws HL7Exception {
       return (ARQ)super.removeRepetition("ARQ", rep);
    }


    /**
	 *
	 * @param rep
	 * @return
	 */
	public ZAI getZAI() {
	   return getTyped("ZAI", ZAI.class);
	}

	public ZAI getZAI(int rep) {
	   return getTyped("ZAI", rep, ZAI.class);
	}

	public int getZAIReps() {
	  return getReps("ZAI");
	}

	public java.util.List<ZAI> getZAIAll() throws HL7Exception {
	  return getAllAsList("ZAI", ZAI.class);
	}

	public void insertZAI(ZAI structure, int rep) throws HL7Exception {
	   super.insertRepetition( "ZAI", structure, rep);
	}

	public ZAI insertZAI(int rep) throws HL7Exception {
	   return (ZAI)super.insertRepetition("ZAI", rep);
	}

	public ZAI removeZAI(int rep) throws HL7Exception {
	   return (ZAI)super.removeRepetition("ZAI", rep);
	}




}
