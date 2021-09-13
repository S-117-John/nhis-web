package com.zebone.nhis.ma.pub.platform.sd.vo;

/*     */ import java.util.List;

/*     */
/*     */ import ca.uhn.hl7v2.HL7Exception;
/*     */ import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MFE;
/*
/*     */ import ca.uhn.hl7v2.model.v24.segment.MFI;
/*     */ import ca.uhn.hl7v2.model.v24.segment.MSH;
/*     */ import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
/*     */ import ca.uhn.hl7v2.parser.ModelClassFactory;

/*     */ public class MFN_M01_Z11
/*     */   extends AbstractMessage
/*     */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*     */   public MFN_M01_Z11()
/*     */   {
/*  61 */     this(new DefaultModelClassFactory());
/*     */   }
/*     */
/*     */
/*     */
/*     */   public MFN_M01_Z11(ModelClassFactory factory)
/*     */   {
/*  68 */     super(factory);
/*  69 */     init(factory);
/*     */   }
/*     */
/*     */   private void init(ModelClassFactory factory) {
/*     */     try {
/*  74 */       add(MSH.class, true, false);
/*  75 */       add(MFI.class, true, false);
				add(MFE.class, true, false, false);
				add(ACK_ZRN_Z11Loop.class, true, true);
/*     */     } catch (HL7Exception e) {
/*  78 */       log.error("Unexpected error creating MFN_M01 - this is probably a bug in the source code generator.", e);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   @Override
public String getVersion()
/*     */   {
/*  87 */     return "2.4";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public MSH getMSH()
/*     */   {
/* 102 */     return getTyped("MSH", MSH.class);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public MFI getMFI()
/*     */   {
/* 118 */     return getTyped("MFI", MFI.class);
/*     */   }
/*     */
/*     */   public MFE getMFE()
/*     */   {
/* 118 */     return getTyped("MFE", MFE.class);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public ACK_ZRN_Z11Loop getACK_ZRN_Z11Loop()
/*     */   {
/* 135 */     return getTyped("ACK_ZRN_Z11Loop", ACK_ZRN_Z11Loop.class);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public ACK_ZRN_Z11Loop getACK_ZRN_Z11Loop(int rep)
/*     */   {
/* 151 */     return getTyped("ACK_ZRN_Z11Loop", rep, ACK_ZRN_Z11Loop.class);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public int getACK_ZRN_Z11LoopReps()
/*     */   {
/* 161 */     return getReps("ACK_ZRN_Z11Loop");
/*     */   }
/*     */
/*     */
/*     */   public List<ACK_ZRN_Z11Loop> getACK_ZRN_Z11LoopAll()
/*     */     throws HL7Exception
/*     */   {
/* 175 */     return getAllAsList("ACK_ZRN_Z11Loop", ACK_ZRN_Z11Loop.class);
/*     */   }
/*     */
/*     */   public void insertACK_ZRN_Z11Loop(ACK_ZRN_Z11Loop structure, int rep)
/*     */     throws HL7Exception
/*     */   {
/* 187 */     super.insertRepetition("ACK_ZRN_Z11Loop", structure, rep);
/*     */   }
/*     */
/*     */
/*     */   public ACK_ZRN_Z11Loop insertACK_ZRN_Z11Loop(int rep)
/*     */     throws HL7Exception
/*     */   {
/* 200 */     return (ACK_ZRN_Z11Loop)super.insertRepetition("ACK_ZRN_Z11Loop", rep);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public ACK_ZRN_Z11Loop removeACK_ZRN_Z11Loop(int rep)
/*     */     throws HL7Exception
/*     */   {
/* 213 */     return (ACK_ZRN_Z11Loop)super.removeRepetition("ACK_ZRN_Z11Loop", rep);
/*     */   }
/*     */ }
