package com.zebone.nhis.ma.pub.platform.sd.vo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ACK_ZRN_Z11Loop
extends AbstractGroup
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


public ACK_ZRN_Z11Loop(Group parent, ModelClassFactory factory)
  {
     super(parent, factory);
     init(factory);
  }

  private void init(ModelClassFactory factory) {
    try {
      add(Z11.class, true, false, false);

    } catch (HL7Exception e) {
       log.error("Unexpected error creating OMG_O19_ORDER - this is probably a bug in the source code generator.", e);
    }
  }



  public String getVersion()
  {
    return "2.4";
  }






  public Z11 getZ11()
  {
    Z11 retVal = getTyped("Z11", Z11.class);
    return retVal;
  }





}
