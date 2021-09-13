package com.zebone.nhis.ma.pub.platform.sd.vo;


/*     */ import java.util.List;

/*     */
/*     */ import ca.uhn.hl7v2.HL7Exception;
/*     */ import ca.uhn.hl7v2.model.AbstractMessage;
/*     */ import ca.uhn.hl7v2.model.v24.segment.DSC;
/*     */ import ca.uhn.hl7v2.model.v24.segment.ERR;
/*     */ import ca.uhn.hl7v2.model.v24.segment.MSA;
/*     */ import ca.uhn.hl7v2.model.v24.segment.MSH;
/*     */ import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
/*     */ import ca.uhn.hl7v2.parser.ModelClassFactory;

/*     */
/*     */
/*     */ public class RAR_RAR
/*     */   extends AbstractMessage
/*     */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*     */   public RAR_RAR()
/*     */   {
/*  63 */     this(new DefaultModelClassFactory());
/*     */   }
/*     */
/*     */
/*     */
/*     */   public RAR_RAR(ModelClassFactory factory)
/*     */   {
/*  70 */     super(factory);
/*  71 */     init(factory);
/*     */   }
/*     */
/*     */   private void init(ModelClassFactory factory) {
/*     */     try {
/*  76 */       add(MSH.class, true, false);
/*  77 */       add(MSA.class, true, false);
/*  78 */       add(ERR.class, false, false);
/*  79 */       add(RAR_RAR_DEFINITION.class, true, true);
/*  80 */       add(DSC.class, false, false);
/*     */     } catch (HL7Exception e) {
/*  82 */       log.error("Unexpected error creating RAR_RAR - this is probably a bug in the source code generator.", e);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   @Override
public String getVersion()
/*     */   {
/*  91 */     return "2.4";
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
/* 106 */     return getTyped("MSH", MSH.class);
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
/*     */   public MSA getMSA()
/*     */   {
/* 122 */     return getTyped("MSA", MSA.class);
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
/*     */   public ERR getERR()
/*     */   {
/* 138 */     return getTyped("ERR", ERR.class);
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
/*     */
/*     */   public RAR_RAR_DEFINITION getDEFINITION()
/*     */   {
/* 155 */     return getTyped("DEFINITION", RAR_RAR_DEFINITION.class);
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
/*     */   public RAR_RAR_DEFINITION getDEFINITION(int rep)
/*     */   {
/* 171 */     return getTyped("DEFINITION", rep, RAR_RAR_DEFINITION.class);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public int getDEFINITIONReps()
/*     */   {
/* 181 */     return getReps("DEFINITION");
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
/*     */   public List<RAR_RAR_DEFINITION> getDEFINITIONAll()
/*     */     throws HL7Exception
/*     */   {
/* 195 */     return getAllAsList("DEFINITION", RAR_RAR_DEFINITION.class);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void insertDEFINITION(RAR_RAR_DEFINITION structure, int rep)
/*     */     throws HL7Exception
/*     */   {
/* 207 */     super.insertRepetition("DEFINITION", structure, rep);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public RAR_RAR_DEFINITION insertDEFINITION(int rep)
/*     */     throws HL7Exception
/*     */   {
/* 220 */     return (RAR_RAR_DEFINITION)super.insertRepetition("DEFINITION", rep);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public RAR_RAR_DEFINITION removeDEFINITION(int rep)
/*     */     throws HL7Exception
/*     */   {
/* 233 */     return (RAR_RAR_DEFINITION)super.removeRepetition("DEFINITION", rep);
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
/*     */   public DSC getDSC()
/*     */   {
/* 248 */     return getTyped("DSC", DSC.class);
/*     */   }
/*     */ }

/* Location:           H:\java7work\HISGL\WebRoot\WEB-INF\lib\hapi-structures-v24-2.2.jar
 * Qualified Name:     ca.uhn.hl7v2.model.v24.message.RAR_RAR
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.1
 */