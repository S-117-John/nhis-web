//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.11 at 05:10:19 PM CST 
//


package com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.CnLabApplyAndOccDetail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}ELECTR_REQUISITION_NO"/>
 *         &lt;element ref="{}REPORT_NO"/>
 *         &lt;element ref="{}TEST_ITEM_CODE"/>
 *         &lt;element ref="{}TEST_ITEM_NAME"/>
 *         &lt;element ref="{}TEST_RESULT_VALUE"/>
 *         &lt;element ref="{}TEST_RESULT_VALUE_UNIT"/>
 *         &lt;element ref="{}REFERENCE_RANGES"/>
 *         &lt;element ref="{}NORMAL_FLAG"/>
 *         &lt;element ref="{}SAMPLE_TYPE_CODE"/>
 *         &lt;element ref="{}SAMPLE_TYPE_NAME"/>
 *         &lt;element ref="{}NOTE"/>
 *         &lt;element ref="{}INVALID_FLAG"/>
 *         &lt;element ref="{}MICROBE_NAME"/>
 *         &lt;element ref="{}BACTERIAL_COLONY_COUNT"/>
 *         &lt;element ref="{}SMEAR_RESULT"/>
 *         &lt;element ref="{}MIC"/>
 *         &lt;element ref="{}DIAMETER"/>
 *         &lt;element ref="{}RECORD_DATE"/>
 *         &lt;element ref="{}UPDATE_DATE"/>
 *       &lt;/sequence>
 *       &lt;attribute name="action" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "electrrequisitionno",
    "reportno",
    "testitemcode",
    "testitemname",
    "testresultvalue",
    "testresultvalueunit",
    "referenceranges",
    "normalflag",
    "sampletypecode",
    "sampletypename",
    "note",
    "invalidflag",
    "microbename",
    "bacterialcolonycount",
    "smearresult",
    "mic",
    "diameter",
    "recorddate",
    "updatedate",
        "testdetailitemcname",
        "testdetailitemcode"
})
@XmlRootElement(name = "row")
/**
 * AS20004 检验项目明细 V3.10
 * @author gongyy
 * @date 2018-12-11 18:09:22
 */
public class CnLabApplyAndOccDetailRow {
    @XmlElement(name = "TEST_DETAIL_ITEM_NAME", required = true)
    private  String testdetailitemcname;
    @XmlElement(name = "TEST_DETAIL_ITEM_CODE", required = true)
    private  String testdetailitemcode;
    @XmlElement(name = "ELECTR_REQUISITION_NO", required = true)
    private  String electrrequisitionno;
    @XmlElement(name = "REPORT_NO", required = true)
    private  String reportno;
    @XmlElement(name = "TEST_ITEM_CODE", required = true)
    private  String testitemcode;
    @XmlElement(name = "TEST_ITEM_NAME", required = true)
    private  String testitemname;
    @XmlElement(name = "TEST_RESULT_VALUE", required = true)
    private  String testresultvalue;
    @XmlElement(name = "TEST_RESULT_VALUE_UNIT", required = true)
    private  String testresultvalueunit;
    @XmlElement(name = "REFERENCE_RANGES", required = true)
    private  String referenceranges;
    @XmlElement(name = "NORMAL_FLAG", required = true)
    private  String normalflag;
    @XmlElement(name = "SAMPLE_TYPE_CODE", required = true)
    private  String sampletypecode;
    @XmlElement(name = "SAMPLE_TYPE_NAME", required = true)
    private  String sampletypename;
    @XmlElement(name = "NOTE", required = true)
    private  String note;
    @XmlElement(name = "INVALID_FLAG", required = true)
    private  String invalidflag;
    @XmlElement(name = "MICROBE_NAME", required = true)
    private  String microbename;
    @XmlElement(name = "BACTERIAL_COLONY_COUNT", required = true)
    private  String bacterialcolonycount;
    @XmlElement(name = "SMEAR_RESULT", required = true)
    private  String smearresult;
    @XmlElement(name = "MIC", required = true)
    private  String mic;
    @XmlElement(name = "DIAMETER", required = true)
    private  String diameter;
    @XmlElement(name = "RECORD_DATE", required = true)
    private  String recorddate;
    @XmlElement(name = "UPDATE_DATE", required = true)
    private  String updatedate;
    @XmlAttribute(name = "action", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    private  String action;

    /**
     * Gets the value of the electrrequisitionno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getELECTRREQUISITIONNO() {
        return electrrequisitionno;
    }

    /**
     * Sets the value of the electrrequisitionno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setELECTRREQUISITIONNO(String value) {
        this.electrrequisitionno = value;
    }

    /**
     * Gets the value of the reportno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREPORTNO() {
        return reportno;
    }

    /**
     * Sets the value of the reportno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREPORTNO(String value) {
        this.reportno = value;
    }

    /**
     * Gets the value of the testitemcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTESTITEMCODE() {
        return testitemcode;
    }

    /**
     * Sets the value of the testitemcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTESTITEMCODE(String value) {
        this.testitemcode = value;
    }

    /**
     * Gets the value of the testitemname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTESTITEMNAME() {
        return testitemname;
    }

    /**
     * Sets the value of the testitemname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTESTITEMNAME(String value) {
        this.testitemname = value;
    }

    /**
     * Gets the value of the testresultvalue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTESTRESULTVALUE() {
        return testresultvalue;
    }

    /**
     * Sets the value of the testresultvalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTESTRESULTVALUE(String value) {
        this.testresultvalue = value;
    }

    /**
     * Gets the value of the testresultvalueunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTESTRESULTVALUEUNIT() {
        return testresultvalueunit;
    }

    /**
     * Sets the value of the testresultvalueunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTESTRESULTVALUEUNIT(String value) {
        this.testresultvalueunit = value;
    }

    /**
     * Gets the value of the referenceranges property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREFERENCERANGES() {
        return referenceranges;
    }

    /**
     * Sets the value of the referenceranges property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREFERENCERANGES(String value) {
        this.referenceranges = value;
    }

    /**
     * Gets the value of the normalflag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNORMALFLAG() {
        return normalflag;
    }

    /**
     * Sets the value of the normalflag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNORMALFLAG(String value) {
        this.normalflag = value;
    }

    /**
     * Gets the value of the sampletypecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSAMPLETYPECODE() {
        return sampletypecode;
    }

    /**
     * Sets the value of the sampletypecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSAMPLETYPECODE(String value) {
        this.sampletypecode = value;
    }

    /**
     * Gets the value of the sampletypename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSAMPLETYPENAME() {
        return sampletypename;
    }

    /**
     * Sets the value of the sampletypename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSAMPLETYPENAME(String value) {
        this.sampletypename = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOTE() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOTE(String value) {
        this.note = value;
    }

    /**
     * Gets the value of the invalidflag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINVALIDFLAG() {
        return invalidflag;
    }

    /**
     * Sets the value of the invalidflag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINVALIDFLAG(String value) {
        this.invalidflag = value;
    }

    /**
     * Gets the value of the microbename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMICROBENAME() {
        return microbename;
    }

    /**
     * Sets the value of the microbename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMICROBENAME(String value) {
        this.microbename = value;
    }

    /**
     * Gets the value of the bacterialcolonycount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBACTERIALCOLONYCOUNT() {
        return bacterialcolonycount;
    }

    /**
     * Sets the value of the bacterialcolonycount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBACTERIALCOLONYCOUNT(String value) {
        this.bacterialcolonycount = value;
    }

    /**
     * Gets the value of the smearresult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMEARRESULT() {
        return smearresult;
    }

    /**
     * Sets the value of the smearresult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMEARRESULT(String value) {
        this.smearresult = value;
    }

    /**
     * Gets the value of the mic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIC() {
        return mic;
    }

    /**
     * Sets the value of the mic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIC(String value) {
        this.mic = value;
    }

    /**
     * Gets the value of the diameter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDIAMETER() {
        return diameter;
    }

    /**
     * Sets the value of the diameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDIAMETER(String value) {
        this.diameter = value;
    }

    /**
     * Gets the value of the recorddate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECORDDATE() {
        return recorddate;
    }

    /**
     * Sets the value of the recorddate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECORDDATE(String value) {
        this.recorddate = value;
    }

    /**
     * Gets the value of the updatedate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUPDATEDATE() {
        return updatedate;
    }

    /**
     * Sets the value of the updatedate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUPDATEDATE(String value) {
        this.updatedate = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    public String getTestdetailitemcname() {
        return testdetailitemcname;
    }

    public void setTestdetailitemcname(String testdetailitemcname) {
        this.testdetailitemcname = testdetailitemcname;
    }

    public String getTestdetailitemcode() {
        return testdetailitemcode;
    }

    public void setTestdetailitemcode(String testdetailitemcode) {
        this.testdetailitemcode = testdetailitemcode;
    }
}