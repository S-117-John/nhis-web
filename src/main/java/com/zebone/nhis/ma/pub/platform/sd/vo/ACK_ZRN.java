package com.zebone.nhis.ma.pub.platform.sd.vo;

import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * 已启用
 * @author FH
 *
 */
public class ACK_ZRN extends AbstractMessage {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new ACK_ZRN message with DefaultModelClassFactory.
     */
	public ACK_ZRN() {
	    this(new DefaultModelClassFactory());
	}

	/**
     * Creates a new ACK_ZRN message with custom ModelClassFactory.
     */
	public ACK_ZRN(ModelClassFactory factory) {
		super(factory);
		init(factory);
	}

	private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
           this.add(MSA.class, true, false);
           this.add(ACK_ZRN_Z11Loop.class, true, true);

	       } catch(HL7Exception e) {
          log.error("Unexpected error creating ACK_ZRN - this is probably a bug in the source code generator.", e);
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


	public MSA getMSA() {
       return getTyped("MSA", MSA.class);
    }


	 public ACK_ZRN_Z11Loop getZ11Loop()
	   {
		 return getTyped("Z11Loop", ACK_ZRN_Z11Loop.class);
	   }





	   public ACK_ZRN_Z11Loop getZ11Loop(int rep)
	   {
		   return getTyped("Z11Loop", rep, ACK_ZRN_Z11Loop.class);
	   }






	   public int getZ11LoopReps()
	   {
	 /* 257 */     return getReps("Z11Loop");
	   }









	   public List<ACK_ZRN_Z11Loop> getZ11LoopAll()
	     throws HL7Exception
	   {
	 /* 271 */     return getAllAsList("Z11Loop", ACK_ZRN_Z11Loop.class);
	   }







	   public void insertZ11Loop(ACK_ZRN_Z11Loop structure, int rep)
	     throws HL7Exception
	   {
	 /* 283 */     super.insertRepetition("Z11Loop", structure, rep);
	   }








	   public ACK_ZRN_Z11Loop insertZ11Loop(int rep)
	     throws HL7Exception
	   {
	 /* 296 */     return (ACK_ZRN_Z11Loop)super.insertRepetition("Z11Loop", rep);
	   }








	   public ACK_ZRN_Z11Loop removeZ11Loop(int rep)
	     throws HL7Exception
	   {
	 /* 309 */     return (ACK_ZRN_Z11Loop)super.removeRepetition("Z11Loop", rep);
	   }



}
