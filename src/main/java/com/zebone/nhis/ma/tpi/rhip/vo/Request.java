package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "body"
})
@XmlRootElement(name = "Request")
public class Request {

    @XmlElement(name = "Header", required = true)
    public Request.Header header;
    @XmlElement(name = "Body", required = true)
	public Request.Body body;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link Request.Header }
     *     
     */
    public Request.Header getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request.Header }
     *     
     */
    public void setHeader(Request.Header value) {
        this.header = value;
    }

    /**
     * Gets the value of the body property.
     * 
     * @return
     *     possible object is
     *     {@link Request.Body }
     *     
     */
    public Request.Body getBody() {
        return body;
    }

    /**
     * Sets the value of the body property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request.Body }
     *     
     */
    public void setBody(Request.Body value) {
        this.body = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(propOrder = { "iptRecord","iptAdmissionNote","iptAdviceDetail","iptSignsRecord","iptLeaveRecord","iptMedicalRecordPage","ptOperation","iptFee","iptFeeDetail","optRegister","optRecord","optRecipe","optFee","optFeeDetail","ptExamReport","ptLabReport","ptDiagnosis","bsPatient","optBL","pmcDrugStore","pmcDrugEntry","pmcDrugStock","pmcPharmacyEntry","pmcPharmacyStock"})  
    public static class Body {

    	//1
        @XmlElement(name = "Ipt_AdmissionNote")
        protected IptAdmissionNote iptAdmissionNote;
        //2
        @XmlElement(name = "Ipt_Record")
        protected IptRecord iptRecord;
        //3
        @XmlElement(name = "Ipt_AdviceDetail")
        protected IptAdviceDetail iptAdviceDetail;
        //4
        @XmlElement(name = "Ipt_SignsRecord")
        protected IptSignsRecord iptSignsRecord;
        //5
        @XmlElement(name = "Ipt_LeaveRecord")
        protected IptLeaveRecord iptLeaveRecord;
        //6
        @XmlElement(name = "Ipt_MedicalRecordPage")
        protected IptMedicalRecordPage iptMedicalRecordPage;
        //7
        @XmlElement(name = "Pt_Operation")
        protected  PtOperation ptOperation;    
        //8
        @XmlElement(name = "Ipt_Fee")
        protected  IptFee iptFee;
        //9
        @XmlElement(name = "Ipt_FeeDetail")
        protected  IptFeeDetail iptFeeDetail;  
        
        //clinic
        //1
        @XmlElement(name = "Opt_Register")
        protected  OptRegister optRegister;       
        //2
        @XmlElement(name = "Opt_Record")
        protected  OptRecord optRecord;       
        //3
        @XmlElement(name = "Opt_Recipe")
        protected  OptRecipe optRecipe; 
        //4
        @XmlElement(name = "Opt_Fee")
        protected  OptFee optFee;
        //5
        @XmlElement(name = "Opt_FeeDetail")
        protected  OptFeeDetail optFeeDetail;          
        
        //3-1
        @XmlElement(name = "Pt_ExamReport")
        protected  PtExamReport ptExamReport;          
        //3-2
        @XmlElement(name = "Pt_LabReport")
        protected  PtLabReport ptLabReport;          
        //3-5
        @XmlElement(name = "Pt_Diagnosis")
        protected  PtDiagnosis ptDiagnosis;          
        //
        @XmlElement(name = "Bs_Patient")
        protected  BsPatient bsPatient;  
        @XmlElement(name = "Opt_BL")
        protected  OptBL optBL;  
        
        @XmlElement(name = "PMC_DrugStore")
        protected  PMCDrugStore pmcDrugStore;  
        
        @XmlElement(name = "PMC_DrugEntry")
        protected  PMCDrugEntry pmcDrugEntry;  
        
        @XmlElement(name = "PMC_DrugStock")
        protected  PMCDrugStock pmcDrugStock;  
        
        @XmlElement(name = "PMC_PharmacyEntry")
        protected  PMCPharmacyEntry pmcPharmacyEntry;  
        
        @XmlElement(name = "PMC_PharmacyStock")
        protected  PMCPharmacyStock pmcPharmacyStock;  
        
        @XmlAttribute(name = "DocFormat", required = true)
        protected String docFormat;
        
        public IptMedicalRecordPage getIptMedicalRecordPage() {
			return iptMedicalRecordPage;
		}

		public void setIptMedicalRecordPage(IptMedicalRecordPage iptMedicalRecordPage) {
			this.iptMedicalRecordPage = iptMedicalRecordPage;
		}

		public IptSignsRecord getIptSignsRecord() {
			return iptSignsRecord;
		}

		public void setIptSignsRecord(IptSignsRecord iptSignsRecord) {
			this.iptSignsRecord = iptSignsRecord;
		}

		public IptLeaveRecord getIptLeaveRecord() {
			return iptLeaveRecord;
		}

		public void setIptLeaveRecord(IptLeaveRecord iptLeaveRecord) {
			this.iptLeaveRecord = iptLeaveRecord;
		}

		public IptFee getIptFee() {
			return iptFee;
		}

		public void setIptFee(IptFee iptFee) {
			this.iptFee = iptFee;
		}

		public IptFeeDetail getIptFeeDetail() {
			return iptFeeDetail;
		}

		public void setIptFeeDetail(IptFeeDetail iptFeeDetail) {
			this.iptFeeDetail = iptFeeDetail;
		}


		public OptRegister getOptRegister() {
			return optRegister;
		}

		public void setOptRegister(OptRegister optRegister) {
			this.optRegister = optRegister;
		}

		public OptRecord getOptRecord() {
			return optRecord;
		}

		public void setOptRecord(OptRecord optRecord) {
			this.optRecord = optRecord;
		}

		public OptRecipe getOptRecipe() {
			return optRecipe;
		}

		public void setOptRecipe(OptRecipe optRecipe) {
			this.optRecipe = optRecipe;
		}
		

		
		public OptFee getOptFee() {
			return optFee;
		}

		public void setOptFee(OptFee optFee) {
			this.optFee = optFee;
		}

		public OptFeeDetail getOptFeeDetail() {
			return optFeeDetail;
		}

		public void setOptFeeDetail(OptFeeDetail optFeeDetail) {
			this.optFeeDetail = optFeeDetail;
		}

		
		public PtExamReport getPtExamReport() {
			return ptExamReport;
		}

		public void setPtExamReport(PtExamReport ptExamReport) {
			this.ptExamReport = ptExamReport;
		}

		public PtLabReport getPtLabReport() {
			return ptLabReport;
		}

		public void setPtLabReport(PtLabReport ptLabReport) {
			this.ptLabReport = ptLabReport;
		}

		public PtDiagnosis getPtDiagnosis() {
			return ptDiagnosis;
		}

		public void setPtDiagnosis(PtDiagnosis ptDiagnosis) {
			this.ptDiagnosis = ptDiagnosis;
		}
		
		/**
         * Gets the value of the iptAdmissionNote property.
         * 
         * @return
         *     possible object is
         *     {@link Request.Body.IptAdmissionNote }
         *     
         */
        public IptAdmissionNote getIptAdmissionNote() {
            return iptAdmissionNote;
        }

        /**
         * Sets the value of the iptAdmissionNote property.
         * 
         * @param value
         *     allowed object is
         *     {@link Request.Body.IptAdmissionNote }
         *     
         */
        public void setIptAdmissionNote(IptAdmissionNote value) {
            this.iptAdmissionNote = value;
        }

        public IptAdviceDetail getIptAdviceDetail() {
			return iptAdviceDetail;
		}

		public void setIptAdviceDetail(IptAdviceDetail iptAdviceDetail) {
			this.iptAdviceDetail = iptAdviceDetail;
		}

		/**
         * Gets the value of the docFormat property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDocFormat() {
            return docFormat;
        }

        /**
         * Sets the value of the docFormat property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocFormat(String value) {
            this.docFormat = value;
        }

		public IptRecord getIptRecord() {
			return iptRecord;
		}

		public void setIptRecord(IptRecord iptRecord) {
			this.iptRecord = iptRecord;
		}

		public PtOperation getPtOperation() {
			return ptOperation;
		}

		public void setPtOperation(PtOperation ptOperation) {
			this.ptOperation = ptOperation;
		}

		public BsPatient getBsPatient() {
			return bsPatient;
		}

		public void setBsPatient(BsPatient bsPatient) {
			this.bsPatient = bsPatient;
		}

		public OptBL getOptBL() {
			return optBL;
		}

		public void setOptBL(OptBL optBL) {
			this.optBL = optBL;
		}

		public PMCDrugStore getPmcDrugStore() {
			return pmcDrugStore;
		}

		public void setPmcDrugStore(PMCDrugStore pmcDrugStore) {
			this.pmcDrugStore = pmcDrugStore;
		}

		public PMCDrugEntry getPmcDrugEntry() {
			return pmcDrugEntry;
		}

		public void setPmcDrugEntry(PMCDrugEntry pmcDrugEntry) {
			this.pmcDrugEntry = pmcDrugEntry;
		}

		public PMCDrugStock getPmcDrugStock() {
			return pmcDrugStock;
		}

		public void setPmcDrugStock(PMCDrugStock pmcDrugStock) {
			this.pmcDrugStock = pmcDrugStock;
		}

		public PMCPharmacyEntry getPmcPharmacyEntry() {
			return pmcPharmacyEntry;
		}

		public void setPmcPharmacyEntry(PMCPharmacyEntry pmcPharmacyEntry) {
			this.pmcPharmacyEntry = pmcPharmacyEntry;
		}

		public PMCPharmacyStock getPmcPharmacyStock() {
			return pmcPharmacyStock;
		}

		public void setPmcPharmacyStock(PMCPharmacyStock pmcPharmacyStock) {
			this.pmcPharmacyStock = pmcPharmacyStock;
		}

		
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "docInfo",
        "patient"
    })
    public static class Header {

        @XmlElement(name = "DocInfo", required = true)
        protected DocInfo docInfo;
        @XmlElement(name = "Patient", required = true)
        protected Patient patient;

        /**
         * Gets the value of the docInfo property.
         * 
         * @return
         *     possible object is
         *     {@link DocInfo }
         *     
         */
        public DocInfo getDocInfo() {
            return docInfo;
        }

        /**
         * Sets the value of the docInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link DocInfo }
         *     
         */
        public void setDocInfo(DocInfo value) {
            this.docInfo = value;
        }

        /**
         * Gets the value of the patient property.
         * 
         * @return
         *     possible object is
         *     {@link Patient }
         *     
         */
        public Patient getPatient() {
            return patient;
        }

        /**
         * Sets the value of the patient property.
         * 
         * @param value
         *     allowed object is
         *     {@link Patient }
         *     
         */
        public void setPatient(Patient value) {
            this.patient = value;
        }

    }

}
