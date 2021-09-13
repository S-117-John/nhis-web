package com.zebone.nhis.ma.pub.platform.zb.model;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v24.group.ADT_A01_INSURANCE;
import ca.uhn.hl7v2.model.v24.group.ADT_A01_PROCEDURE;
import ca.uhn.hl7v2.model.v24.segment.ACC;
import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.DG1;
import ca.uhn.hl7v2.model.v24.segment.DRG;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.FT1;
import ca.uhn.hl7v2.model.v24.segment.GT1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NK1;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.PDA;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.UB1;
import ca.uhn.hl7v2.model.v24.segment.UB2;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;



public class ZMR_ZH1 extends AbstractMessage {
	/**
     * Creates a new ZMR_ZH1 message with DefaultModelClassFactory. 
     */ 
    public ZMR_ZH1() { 
       this(new DefaultModelClassFactory());
    }

    /** 
     * Creates a new ZMR_ZH1 message with custom ModelClassFactory.
     */
    public ZMR_ZH1(ModelClassFactory factory) {
       super(factory);
       init(factory);
    }

    private void init(ModelClassFactory factory) {
       try {
    	   this.add(MSH.class, true, false);
           this.add(EVN.class, true, false);
           this.add(PID.class, true, false);
           this.add(NK1.class, false, false);
           this.add(PV1.class, true, false);
           this.add(AL1.class, false, true);
           this.add(DG1.class, false, true);
           this.add(OBX.class, false, true);
           this.add(ZMS.class, false, true);
           this.add(ZMI.class, false, false);
           this.add(ZOR.class, false, true);
           this.add(ZCR.class, false, false);
           this.add(FT1.class, false, false);
           this.add(ZFI.class, false, true);
	       } catch(HL7Exception e) {
          log.error("Unexpected error creating ZMR_ZH1 - this is probably a bug in the source code generator.", e);
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
    public EVN getEVN() { 
       return getTyped("EVN", EVN.class);
    }

    /**
     * <p>
     * Returns
     * PID (Patient identification) - creates it if necessary
     * </p>
     * 
     *
     */
    public PID getPID() { 
       return getTyped("PID", PID.class);
    }


    /**
     * <p>
     * Returns
     * the first repetition of 
     * NK1 (Next of kin / associated parties) - creates it if necessary
     * </p>
     * 
     *
     */
    public NK1 getNK1() { 
       return getTyped("NK1", NK1.class);
    }


    /**
     * <p>
     * Returns
     * PV1 (Patient visit) - creates it if necessary
     * </p>
     * 
     *
     */
    public PV1 getPV1() { 
       return getTyped("PV1", PV1.class);
    }


    /**
     * <p>
     * Returns
     * the first repetition of 
     * AL1 (Patient allergy information) - creates it if necessary
     * </p>
     * 
     *
     */
    public AL1 getAL1() { 
       return getTyped("AL1", AL1.class);
    }


    /**
     * <p>
     * Returns a specific repetition of
     * AL1 (Patient allergy information) - creates it if necessary
     * </p>
     * 
     *
     * @param rep The repetition index (0-indexed, i.e. the first repetition is at index 0)
     * @throws HL7Exception if the repetition requested is more than one 
     *     greater than the number of existing repetitions.
     */
    public AL1 getAL1(int rep) { 
       return getTyped("AL1", rep, AL1.class);
    }

    /** 
     * <p>
     * Returns the number of existing repetitions of AL1 
     * </p>
     * 
     */ 
    public int getAL1Reps() { 
    	return getReps("AL1");
    } 

    /** 
     * <p>
     * Returns a non-modifiable List containing all current existing repetitions of AL1.
     * <p>
     * <p>
     * Note that unlike {@link #getAL1()}, this method will not create any reps
     * if none are already present, so an empty list may be returned.
     * </p>
     * 
     */ 
    public java.util.List<AL1> getAL1All() throws HL7Exception {
    	return getAllAsList("AL1", AL1.class);
    } 

    /**
     * <p>
     * Inserts a specific repetition of AL1 (Patient allergy information)
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public void insertAL1(AL1 structure, int rep) throws HL7Exception { 
       super.insertRepetition( "AL1", structure, rep);
    }


    /**
     * <p>
     * Inserts a specific repetition of AL1 (Patient allergy information)
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public AL1 insertAL1(int rep) throws HL7Exception { 
       return (AL1)super.insertRepetition("AL1", rep);
    }


    /**
     * <p>
     * Removes a specific repetition of AL1 (Patient allergy information)
     * </p>
     * 
     *
     * @see AbstractGroup#removeRepetition(String, int) 
     */
    public AL1 removeAL1(int rep) throws HL7Exception { 
       return (AL1)super.removeRepetition("AL1", rep);
    }



    /**
     * <p>
     * Returns
     * the first repetition of 
     * DG1 (Diagnosis) - creates it if necessary
     * </p>
     * 
     *
     */
    public DG1 getDG1() { 
       return getTyped("DG1", DG1.class);
    }


    /**
     * <p>
     * Returns a specific repetition of
     * DG1 (Diagnosis) - creates it if necessary
     * </p>
     * 
     *
     * @param rep The repetition index (0-indexed, i.e. the first repetition is at index 0)
     * @throws HL7Exception if the repetition requested is more than one 
     *     greater than the number of existing repetitions.
     */
    public DG1 getDG1(int rep) { 
       return getTyped("DG1", rep, DG1.class);
    }

    /** 
     * <p>
     * Returns the number of existing repetitions of DG1 
     * </p>
     * 
     */ 
    public int getDG1Reps() { 
    	return getReps("DG1");
    } 

    /** 
     * <p>
     * Returns a non-modifiable List containing all current existing repetitions of DG1.
     * <p>
     * <p>
     * Note that unlike {@link #getDG1()}, this method will not create any reps
     * if none are already present, so an empty list may be returned.
     * </p>
     * 
     */ 
    public java.util.List<DG1> getDG1All() throws HL7Exception {
    	return getAllAsList("DG1", DG1.class);
    } 

    /**
     * <p>
     * Inserts a specific repetition of DG1 (Diagnosis)
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public void insertDG1(DG1 structure, int rep) throws HL7Exception { 
       super.insertRepetition( "DG1", structure, rep);
    }


    /**
     * <p>
     * Inserts a specific repetition of DG1 (Diagnosis)
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public DG1 insertDG1(int rep) throws HL7Exception { 
       return (DG1)super.insertRepetition("DG1", rep);
    }


    /**
     * <p>
     * Removes a specific repetition of DG1 (Diagnosis)
     * </p>
     * 
     *
     * @see AbstractGroup#removeRepetition(String, int) 
     */
    public DG1 removeDG1(int rep) throws HL7Exception { 
       return (DG1)super.removeRepetition("DG1", rep);
    }




    /**
     * <p>
     * Returns
     * the first repetition of 
     * OBX (Observation/Result) - creates it if necessary
     * </p>
     * 
     *
     */
    public OBX getOBX() { 
       return getTyped("OBX", OBX.class);
    }


    /**
     * <p>
     * Returns a specific repetition of
     * OBX (Observation/Result) - creates it if necessary
     * </p>
     * 
     *
     * @param rep The repetition index (0-indexed, i.e. the first repetition is at index 0)
     * @throws HL7Exception if the repetition requested is more than one 
     *     greater than the number of existing repetitions.
     */
    public OBX getOBX(int rep) { 
       return getTyped("OBX", rep, OBX.class);
    }

    /** 
     * <p>
     * Returns the number of existing repetitions of OBX 
     * </p>
     * 
     */ 
    public int getOBXReps() { 
    	return getReps("OBX");
    } 

    /** 
     * <p>
     * Returns a non-modifiable List containing all current existing repetitions of OBX.
     * <p>
     * <p>
     * Note that unlike {@link #getOBX()}, this method will not create any reps
     * if none are already present, so an empty list may be returned.
     * </p>
     * 
     */ 
    public java.util.List<OBX> getOBXAll() throws HL7Exception {
    	return getAllAsList("OBX", OBX.class);
    } 

    /**
     * <p>
     * Inserts a specific repetition of OBX (Observation/Result)
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public void insertOBX(OBX structure, int rep) throws HL7Exception { 
       super.insertRepetition( "OBX", structure, rep);
    }


    /**
     * <p>
     * Inserts a specific repetition of OBX (Observation/Result)
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public OBX insertOBX(int rep) throws HL7Exception { 
       return (OBX)super.insertRepetition("OBX", rep);
    }


    /**
     * <p>
     * Removes a specific repetition of OBX (Observation/Result)
     * </p>
     * 
     *
     * @see AbstractGroup#removeRepetition(String, int) 
     */
    public OBX removeOBX(int rep) throws HL7Exception { 
       return (OBX)super.removeRepetition("OBX", rep);
    }



    /**
     * <p>
     * Returns
     * the first repetition of 
     * ZMS - creates it if necessary
     * </p>
     * 
     *
     */
    public ZMS getZMS() { 
       return getTyped("ZMS", ZMS.class);
    }


    /**
     * <p>
     * Returns a specific repetition of
     * ZMS - creates it if necessary
     * </p>
     * 
     *
     * @param rep The repetition index (0-indexed, i.e. the first repetition is at index 0)
     * @throws HL7Exception if the repetition requested is more than one 
     *     greater than the number of existing repetitions.
     */
    public ZMS getZMS(int rep) { 
       return getTyped("ZMS", rep, ZMS.class);
    }

    /** 
     * <p>
     * Returns the number of existing repetitions of ZMS 
     * </p>
     * 
     */ 
    public int getZMSReps() { 
    	return getReps("ZMS");
    } 

    /** 
     * <p>
     * Returns a non-modifiable List containing all current existing repetitions of ZMS.
     * <p>
     * <p>
     * Note that unlike {@link #getZMS()}, this method will not create any reps
     * if none are already present, so an empty list may be returned.
     * </p>
     * 
     */ 
    public java.util.List<ZMS> getZMSAll() throws HL7Exception {
    	return getAllAsList("ZMS", ZMS.class);
    } 

    /**
     * <p>
     * Inserts a specific repetition of NK1 (Next of kin / associated parties)
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public void insertZMS(ZMS structure, int rep) throws HL7Exception { 
       super.insertRepetition( "ZMS", structure, rep);
    }


    /**
     * <p>
     * Inserts a specific repetition of NK1 (Next of kin / associated parties)
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public ZMS insertZMS(int rep) throws HL7Exception { 
       return (ZMS)super.insertRepetition("ZMS", rep);
    }


    /**
     * <p>
     * Removes a specific repetition of NK1 (Next of kin / associated parties)
     * </p>
     * 
     *
     * @see AbstractGroup#removeRepetition(String, int) 
     */
    public ZMS removeZMS(int rep) throws HL7Exception { 
       return (ZMS)super.removeRepetition("ZMS", rep);
    }

 
    /**
     * <p>
     * Returns
     * ZMI  - creates it if necessary
     * </p>
     * 
     *
     */
    public ZMI getZMI() { 
       return getTyped("ZMI", ZMI.class);
    }

    /**
     * <p>
     * Returns
     * the first repetition of 
     * ZOR  - creates it if necessary
     * </p>
     * 
     *
     */
    public ZOR getZOR() { 
       return getTyped("ZOR", ZOR.class);
    }


    /**
     * <p>
     * Returns a specific repetition of
     * ZOR - creates it if necessary
     * </p>
     * 
     *
     * @param rep The repetition index (0-indexed, i.e. the first repetition is at index 0)
     * @throws HL7Exception if the repetition requested is more than one 
     *     greater than the number of existing repetitions.
     */
    public ZOR getZOR(int rep) { 
       return getTyped("ZOR", rep, ZOR.class);
    }

    /** 
     * <p>
     * Returns the number of existing repetitions of GT1 
     * </p>
     * 
     */ 
    public int getZORReps() { 
    	return getReps("ZOR");
    } 

    /** 
     * <p>
     * Returns a non-modifiable List containing all current existing repetitions of GT1.
     * <p>
     * <p>
     * Note that unlike {@link #getGT1()}, this method will not create any reps
     * if none are already present, so an empty list may be returned.
     * </p>
     * 
     */ 
    public java.util.List<ZOR> getZORAll() throws HL7Exception {
    	return getAllAsList("ZOR", ZOR.class);
    } 

    /**
     * <p>
     * Inserts a specific repetition of ZOR
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public void insertZOR(ZOR structure, int rep) throws HL7Exception { 
       super.insertRepetition( "ZOR", structure, rep);
    }


    /**
     * <p>
     * Inserts a specific repetition of ZOR
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public ZOR insertZOR(int rep) throws HL7Exception { 
       return (ZOR)super.insertRepetition("ZOR", rep);
    }


    /**
     * <p>
     * Removes a specific repetition of GT1 (Guarantor)
     * </p>
     * 
     *
     * @see AbstractGroup#removeRepetition(String, int) 
     */
    public ZOR removeZOR(int rep) throws HL7Exception { 
       return (ZOR)super.removeRepetition("ZOR", rep);
    }

    
    
    /**
     * <p>
     * Returns
     * ZCR  - creates it if necessary
     * </p>
     * 
     *
     */
    public ZCR getZCR() { 
       return getTyped("ZCR", ZCR.class);    
    }
    
    /**
     * <p>
     * Returns
     * FT1  - creates it if necessary
     * </p>
     * 
     *
     */
    public FT1 getFT1() { 
       return getTyped("FT1", FT1.class);    
    }
    
    /**
     * <p>
     * Returns
     * the first repetition of 
     * ZFI  - creates it if necessary
     * </p>
     * 
     *
     */
    public ZFI getZFI() { 
       return getTyped("ZFI", ZFI.class);
    }


    /**
     * <p>
     * Returns a specific repetition of
     * ZOR - creates it if necessary
     * </p>
     * 
     *
     * @param rep The repetition index (0-indexed, i.e. the first repetition is at index 0)
     * @throws HL7Exception if the repetition requested is more than one 
     *     greater than the number of existing repetitions.
     */
    public ZFI getZFI(int rep) { 
       return getTyped("ZFI", rep, ZFI.class);
    }

    /** 
     * <p>
     * Returns the number of existing repetitions of GT1 
     * </p>
     * 
     */ 
    public int getZFIReps() { 
    	return getReps("ZFI");
    } 

    /** 
     * <p>
     * Returns a non-modifiable List containing all current existing repetitions of GT1.
     * <p>
     * <p>
     * Note that unlike {@link #getZFI()}, this method will not create any reps
     * if none are already present, so an empty list may be returned.
     * </p>
     * 
     */ 
    public java.util.List<ZFI> getZFIAll() throws HL7Exception {
    	return getAllAsList("ZFI", ZFI.class);
    } 

    /**
     * <p>
     * Inserts a specific repetition of ZOR
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public void insertZFI(ZFI structure, int rep) throws HL7Exception { 
       super.insertRepetition( "ZFI", structure, rep);
    }


    /**
     * <p>
     * Inserts a specific repetition of ZOR
     * </p>
     * 
     *
     * @see AbstractGroup#insertRepetition(Structure, int) 
     */
    public ZFI insertZFI(int rep) throws HL7Exception { 
       return (ZFI)super.insertRepetition("ZFI", rep);
    }


    /**
     * <p>
     * Removes a specific repetition of GT1 (Guarantor)
     * </p>
     * 
     *
     * @see AbstractGroup#removeRepetition(String, int) 
     */
    public ZFI removeZFI(int rep) throws HL7Exception { 
       return (ZFI)super.removeRepetition("ZFI", rep);
    }

}
