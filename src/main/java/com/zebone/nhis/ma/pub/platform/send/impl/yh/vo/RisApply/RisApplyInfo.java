//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.19 at 01:30:34 PM CST 
//


package com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.RisApply;

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
 *         &lt;element ref="{}EXAM_SERIAL"/>
 *         &lt;element ref="{}EXAM_TYPE"/>
 *         &lt;element ref="{}EXAM_SUB_TYPE"/>
 *         &lt;element ref="{}PATIENT_ID"/>
 *         &lt;element ref="{}TIMES"/>
 *         &lt;element ref="{}EXEC_UNIT"/>
 *         &lt;element ref="{}APPLY_DATE"/>
 *         &lt;element ref="{}APPLY_DOCTOR"/>
 *         &lt;element ref="{}APPLY_UNIT"/>
 *         &lt;element ref="{}CHARGE_NO"/>
 *         &lt;element ref="{}CHARGE_AMOUNT"/>
 *         &lt;element ref="{}INPATIENT_NO"/>
 *         &lt;element ref="{}YZ_ACT_ORDER_NO"/>
 *         &lt;element ref="{}SCHEDULED_DATE"/>
 *         &lt;element ref="{}ORDER_CODE"/>
 *         &lt;element ref="{}ORDER_NAME"/>
 *         &lt;element ref="{}ENTER_TIME"/>
 *         &lt;element ref="{}CONFIRM_TIME"/>
 *         &lt;element ref="{}START_TIME"/>
 *         &lt;element ref="{}ORDER_TYPE"/>
 *         &lt;element ref="{}WARD_SN"/>
 *         &lt;element ref="{}CONFIRM_OPERA"/>
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
    "examserial",
    "examtype",
    "examsubtype",
    "patientid",
    "times",
    "execunit",
    "applydate",
    "applydoctor",
    "applyunit",
    "chargeno",
    "chargeamount",
    "inpatientno",
    "yzactorderno",
    "scheduleddate",
    "ordercode",
    "ordername",
    "entertime",
    "confirmtime",
    "starttime",
    "ordertype",
    "wardsn",
    "confirmopera"
})
@XmlRootElement(name = "Msg")
public class RisApplyInfo {

    @XmlElement(name = "EXAM_SERIAL", required = true)
    protected String examserial;
    @XmlElement(name = "EXAM_TYPE", required = true)
    protected String examtype;
    @XmlElement(name = "EXAM_SUB_TYPE", required = true)
    protected String examsubtype;
    @XmlElement(name = "PATIENT_ID", required = true)
    protected String patientid;
    @XmlElement(name = "TIMES", required = true)
    protected String times;
    @XmlElement(name = "EXEC_UNIT", required = true)
    protected String execunit;
    @XmlElement(name = "APPLY_DATE", required = true)
    protected String applydate;
    @XmlElement(name = "APPLY_DOCTOR", required = true)
    protected String applydoctor;
    @XmlElement(name = "APPLY_UNIT", required = true)
    protected String applyunit;
    @XmlElement(name = "CHARGE_NO", required = true)
    protected String chargeno;
    @XmlElement(name = "CHARGE_AMOUNT", required = true)
    protected String chargeamount;
    @XmlElement(name = "INPATIENT_NO", required = true)
    protected String inpatientno;
    @XmlElement(name = "YZ_ACT_ORDER_NO", required = true)
    protected String yzactorderno;
    @XmlElement(name = "SCHEDULED_DATE", required = true)
    protected String scheduleddate;
    @XmlElement(name = "ORDER_CODE", required = true)
    protected String ordercode;
    @XmlElement(name = "ORDER_NAME", required = true)
    protected String ordername;
    @XmlElement(name = "ENTER_TIME", required = true)
    protected String entertime;
    @XmlElement(name = "CONFIRM_TIME", required = true)
    protected String confirmtime;
    @XmlElement(name = "START_TIME", required = true)
    protected String starttime;
    @XmlElement(name = "ORDER_TYPE", required = true)
    protected String ordertype;
    @XmlElement(name = "WARD_SN", required = true)
    protected String wardsn;
    @XmlElement(name = "CONFIRM_OPERA", required = true)
    protected String confirmopera;
    @XmlAttribute(name = "action", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String action;

    /**
     * Gets the value of the examserial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXAMSERIAL() {
        return examserial;
    }

    /**
     * Sets the value of the examserial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXAMSERIAL(String value) {
        this.examserial = value;
    }

    /**
     * Gets the value of the examtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXAMTYPE() {
        return examtype;
    }

    /**
     * Sets the value of the examtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXAMTYPE(String value) {
        this.examtype = value;
    }

    /**
     * Gets the value of the examsubtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXAMSUBTYPE() {
        return examsubtype;
    }

    /**
     * Sets the value of the examsubtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXAMSUBTYPE(String value) {
        this.examsubtype = value;
    }

    /**
     * Gets the value of the patientid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPATIENTID() {
        return patientid;
    }

    /**
     * Sets the value of the patientid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPATIENTID(String value) {
        this.patientid = value;
    }

    /**
     * Gets the value of the times property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIMES() {
        return times;
    }

    /**
     * Sets the value of the times property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIMES(String value) {
        this.times = value;
    }

    /**
     * Gets the value of the execunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXECUNIT() {
        return execunit;
    }

    /**
     * Sets the value of the execunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXECUNIT(String value) {
        this.execunit = value;
    }

    /**
     * Gets the value of the applydate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPPLYDATE() {
        return applydate;
    }

    /**
     * Sets the value of the applydate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPPLYDATE(String value) {
        this.applydate = value;
    }

    /**
     * Gets the value of the applydoctor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPPLYDOCTOR() {
        return applydoctor;
    }

    /**
     * Sets the value of the applydoctor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPPLYDOCTOR(String value) {
        this.applydoctor = value;
    }

    /**
     * Gets the value of the applyunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPPLYUNIT() {
        return applyunit;
    }

    /**
     * Sets the value of the applyunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPPLYUNIT(String value) {
        this.applyunit = value;
    }

    /**
     * Gets the value of the chargeno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCHARGENO() {
        return chargeno;
    }

    /**
     * Sets the value of the chargeno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCHARGENO(String value) {
        this.chargeno = value;
    }

    /**
     * Gets the value of the chargeamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCHARGEAMOUNT() {
        return chargeamount;
    }

    /**
     * Sets the value of the chargeamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCHARGEAMOUNT(String value) {
        this.chargeamount = value;
    }

    /**
     * Gets the value of the inpatientno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINPATIENTNO() {
        return inpatientno;
    }

    /**
     * Sets the value of the inpatientno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINPATIENTNO(String value) {
        this.inpatientno = value;
    }

    /**
     * Gets the value of the yzactorderno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYZACTORDERNO() {
        return yzactorderno;
    }

    /**
     * Sets the value of the yzactorderno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYZACTORDERNO(String value) {
        this.yzactorderno = value;
    }

    /**
     * Gets the value of the scheduleddate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCHEDULEDDATE() {
        return scheduleddate;
    }

    /**
     * Sets the value of the scheduleddate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCHEDULEDDATE(String value) {
        this.scheduleddate = value;
    }

    /**
     * Gets the value of the ordercode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERCODE() {
        return ordercode;
    }

    /**
     * Sets the value of the ordercode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERCODE(String value) {
        this.ordercode = value;
    }

    /**
     * Gets the value of the ordername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERNAME() {
        return ordername;
    }

    /**
     * Sets the value of the ordername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERNAME(String value) {
        this.ordername = value;
    }

    /**
     * Gets the value of the entertime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENTERTIME() {
        return entertime;
    }

    /**
     * Sets the value of the entertime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENTERTIME(String value) {
        this.entertime = value;
    }

    /**
     * Gets the value of the confirmtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONFIRMTIME() {
        return confirmtime;
    }

    /**
     * Sets the value of the confirmtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONFIRMTIME(String value) {
        this.confirmtime = value;
    }

    /**
     * Gets the value of the starttime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTARTTIME() {
        return starttime;
    }

    /**
     * Sets the value of the starttime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTARTTIME(String value) {
        this.starttime = value;
    }

    /**
     * Gets the value of the ordertype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERTYPE() {
        return ordertype;
    }

    /**
     * Sets the value of the ordertype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERTYPE(String value) {
        this.ordertype = value;
    }

    /**
     * Gets the value of the wardsn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWARDSN() {
        return wardsn;
    }

    /**
     * Sets the value of the wardsn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWARDSN(String value) {
        this.wardsn = value;
    }

    /**
     * Gets the value of the confirmopera property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONFIRMOPERA() {
        return confirmopera;
    }

    /**
     * Sets the value of the confirmopera property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONFIRMOPERA(String value) {
        this.confirmopera = value;
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

}
