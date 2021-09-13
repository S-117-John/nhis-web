package com.zebone.nhis.ma.pub.platform.sd.vo;


/*     */ import java.util.List;

/*     */
/*     */ import ca.uhn.hl7v2.HL7Exception;
/*     */ import ca.uhn.hl7v2.model.AbstractGroup;
/*     */ import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.v24.group.RAR_RAR_ORDER;
/*     */ import ca.uhn.hl7v2.model.v24.segment.QRD;
/*     */ import ca.uhn.hl7v2.model.v24.segment.QRF;
/*     */ import ca.uhn.hl7v2.parser.ModelClassFactory;

/*     */
/*     */ public class RAR_RAR_DEFINITION
/*     */   extends AbstractGroup
/*     */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*     */   public RAR_RAR_DEFINITION(Group parent, ModelClassFactory factory)
/*     */   {
/*  61 */     super(parent, factory);
/*  62 */     init(factory);
/*     */   }
/*     */
/*     */   private void init(ModelClassFactory factory) {
/*     */     try {
/*  67 */       add(QRD.class, true, false, false);
/*  68 */       add(QRF.class, false, false, false);
/*  69 */       add(RAR_RAR_PATIENT.class, false, false, false);
/*  70 */       add(RAR_RAR_ORDER.class, true, true, false);
/*     */     } catch (HL7Exception e) {
/*  72 */       log.error("Unexpected error creating RAR_RAR_DEFINITION - this is probably a bug in the source code generator.", e);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   public String getVersion()
/*     */   {
/*  80 */     return "2.4";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public QRD getQRD()
/*     */   {
/*  90 */     QRD retVal = getTyped("QRD", QRD.class);
/*  91 */     return retVal;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public QRF getQRF()
/*     */   {
/* 102 */     QRF retVal = getTyped("QRF", QRF.class);
/* 103 */     return retVal;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public RAR_RAR_PATIENT getPATIENT()
/*     */   {
/* 114 */     RAR_RAR_PATIENT retVal = getTyped("PATIENT", RAR_RAR_PATIENT.class);
/* 115 */     return retVal;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public RAR_RAR_ORDER getORDER()
/*     */   {
/* 127 */     RAR_RAR_ORDER retVal = getTyped("ORDER", RAR_RAR_ORDER.class);
/* 128 */     return retVal;
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
/*     */   public RAR_RAR_ORDER getORDER(int rep)
/*     */   {
/* 141 */     RAR_RAR_ORDER retVal = getTyped("ORDER", rep, RAR_RAR_ORDER.class);
/* 142 */     return retVal;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public int getORDERReps()
/*     */   {
/* 149 */     return getReps("ORDER");
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public List<RAR_RAR_ORDER> getORDERAll()
/*     */     throws HL7Exception
/*     */   {
/* 162 */     return getAllAsList("ORDER", RAR_RAR_ORDER.class);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void insertORDER(RAR_RAR_ORDER structure, int rep)
/*     */     throws HL7Exception
/*     */   {
/* 170 */     super.insertRepetition("ORDER", structure, rep);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public RAR_RAR_ORDER insertORDER(int rep)
/*     */     throws HL7Exception
/*     */   {
/* 179 */     return (RAR_RAR_ORDER)super.insertRepetition("ORDER", rep);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public RAR_RAR_ORDER removeORDER(int rep)
/*     */     throws HL7Exception
/*     */   {
/* 188 */     return (RAR_RAR_ORDER)super.removeRepetition("ORDER", rep);
/*     */   }
/*     */ }

/* Location:           H:\java7work\HISGL\WebRoot\WEB-INF\lib\hapi-structures-v24-2.2.jar
 * Qualified Name:     ca.uhn.hl7v2.model.v24.group.RAR_RAR_DEFINITION
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.1
 */