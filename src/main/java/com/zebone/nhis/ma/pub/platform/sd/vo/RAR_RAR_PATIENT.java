package com.zebone.nhis.ma.pub.platform.sd.vo;

/*     */ import java.util.List;

/*     */ import ca.uhn.hl7v2.HL7Exception;
/*     */ import ca.uhn.hl7v2.model.AbstractGroup;
/*     */ import ca.uhn.hl7v2.model.Group;
/*     */ import ca.uhn.hl7v2.model.v24.segment.NTE;
/*     */ import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
/*     */ import ca.uhn.hl7v2.parser.ModelClassFactory;

/*     */
/*     */ public class RAR_RAR_PATIENT
/*     */   extends AbstractGroup
/*     */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*     */   public RAR_RAR_PATIENT(Group parent, ModelClassFactory factory)
/*     */   {
/*  59 */     super(parent, factory);
/*  60 */     init(factory);
/*     */   }
/*     */
/*     */   private void init(ModelClassFactory factory) {
/*     */     try {
/*  65 */       add(PID.class, true, false, false);
			    add(PV1.class, true, false, false);
/*  66 */       add(NTE.class, false, true, false);
/*     */     } catch (HL7Exception e) {
/*  68 */       log.error("Unexpected error creating RAR_RAR_PATIENT - this is probably a bug in the source code generator.", e);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   public String getVersion()
/*     */   {
/*  76 */     return "2.4";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public PID getPID()
/*     */   {
/*  86 */     PID retVal = getTyped("PID", PID.class);
/*  87 */     return retVal;
/*     */   }


/*     */ 	public PV1 getPV1()
/*     */   {
/*  86 */     PV1 retVal = getTyped("PV1", PV1.class);
/*  87 */     return retVal;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public NTE getNTE()
/*     */   {
/*  99 */     NTE retVal = getTyped("NTE", NTE.class);
/* 100 */     return retVal;
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
/*     */   public NTE getNTE(int rep)
/*     */   {
/* 113 */     NTE retVal = getTyped("NTE", rep, NTE.class);
/* 114 */     return retVal;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public int getNTEReps()
/*     */   {
/* 121 */     return getReps("NTE");
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public List<NTE> getNTEAll()
/*     */     throws HL7Exception
/*     */   {
/* 134 */     return getAllAsList("NTE", NTE.class);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void insertNTE(NTE structure, int rep)
/*     */     throws HL7Exception
/*     */   {
/* 142 */     super.insertRepetition("NTE", structure, rep);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public NTE insertNTE(int rep)
/*     */     throws HL7Exception
/*     */   {
/* 151 */     return (NTE)super.insertRepetition("NTE", rep);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public NTE removeNTE(int rep)
/*     */     throws HL7Exception
/*     */   {
/* 160 */     return (NTE)super.removeRepetition("NTE", rep);
/*     */   }
/*     */ }

/* Location:           H:\java7work\HISGL\WebRoot\WEB-INF\lib\hapi-structures-v24-2.2.jar
 * Qualified Name:     ca.uhn.hl7v2.model.v24.group.RAR_RAR_PATIENT
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.1
 */
