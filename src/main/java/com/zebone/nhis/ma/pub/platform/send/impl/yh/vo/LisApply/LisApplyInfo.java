//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.19 at 01:42:57 PM CST 
//


package com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.LisApply;

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
 *         &lt;element ref="{}ORDER_NO"/>
 *         &lt;element ref="{}PATIENT_ID"/>
 *         &lt;element ref="{}ADMISS_TIMES"/>
 *         &lt;element ref="{}ORDER_CODE"/>
 *         &lt;element ref="{}SERIAL"/>
 *         &lt;element ref="{}ENTER_TIME"/>
 *         &lt;element ref="{}CONFIRM_TIME"/>
 *         &lt;element ref="{}START_TIME"/>
 *         &lt;element ref="{}END_TIME"/>
 *         &lt;element ref="{}ORDER_DOCTOR"/>
 *         &lt;element ref="{}STOP_DOCTOR"/>
 *         &lt;element ref="{}CONFIRM_OPERA"/>
 *         &lt;element ref="{}STOP_CONFIRM_OPERA"/>
 *         &lt;element ref="{}FREQU_CODE"/>
 *         &lt;element ref="{}ORDER_TYPE"/>
 *         &lt;element ref="{}SUPPLY_CODE"/>
 *         &lt;element ref="{}DRUG_SPECIFICATION"/>
 *         &lt;element ref="{}DOSEAGE"/>
 *         &lt;element ref="{}DOSEAGE_UNIT"/>
 *         &lt;element ref="{}CHARGE_AMOUNT"/>
 *         &lt;element ref="{}DRUG_OCC"/>
 *         &lt;element ref="{}MINI_UNIT"/>
 *         &lt;element ref="{}PARENT_NO"/>
 *         &lt;element ref="{}FIT_FLAG"/>
 *         &lt;element ref="{}SELF_BUY"/>
 *         &lt;element ref="{}INSTRUCTION"/>
 *         &lt;element ref="{}DEPT_SN"/>
 *         &lt;element ref="{}WARD_SN"/>
 *         &lt;element ref="{}ORDER_NAME"/>
 *         &lt;element ref="{}EXEC_UNIT"/>
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
    "orderno",
    "patientid",
    "admisstimes",
    "ordercode",
    "serial",
    "entertime",
    "confirmtime",
    "starttime",
    "endtime",
    "orderdoctor",
    "stopdoctor",
    "confirmopera",
    "stopconfirmopera",
    "frequcode",
    "ordertype",
    "supplycode",
    "drugspecification",
    "doseage",
    "doseageunit",
    "chargeamount",
    "drugocc",
    "miniunit",
    "parentno",
    "fitflag",
    "selfbuy",
    "instruction",
    "deptsn",
    "wardsn",
    "ordername",
    "execunit"
})
@XmlRootElement(name = "Msg")
public class LisApplyInfo {

    @XmlElement(name = "ORDER_NO", required = true)
    protected String orderno;
    @XmlElement(name = "PATIENT_ID", required = true)
    protected String patientid;
    @XmlElement(name = "ADMISS_TIMES", required = true)
    protected String admisstimes;
    @XmlElement(name = "ORDER_CODE", required = true)
    protected String ordercode;
    @XmlElement(name = "SERIAL", required = true)
    protected String serial;
    @XmlElement(name = "ENTER_TIME", required = true)
    protected String entertime;
    @XmlElement(name = "CONFIRM_TIME", required = true)
    protected String confirmtime;
    @XmlElement(name = "START_TIME", required = true)
    protected String starttime;
    @XmlElement(name = "END_TIME", required = true)
    protected String endtime;
    @XmlElement(name = "ORDER_DOCTOR", required = true)
    protected String orderdoctor;
    @XmlElement(name = "STOP_DOCTOR", required = true)
    protected String stopdoctor;
    @XmlElement(name = "CONFIRM_OPERA", required = true)
    protected String confirmopera;
    @XmlElement(name = "STOP_CONFIRM_OPERA", required = true)
    protected String stopconfirmopera;
    @XmlElement(name = "FREQU_CODE", required = true)
    protected String frequcode;
    @XmlElement(name = "ORDER_TYPE", required = true)
    protected String ordertype;
    @XmlElement(name = "SUPPLY_CODE", required = true)
    protected String supplycode;
    @XmlElement(name = "DRUG_SPECIFICATION", required = true)
    protected String drugspecification;
    @XmlElement(name = "DOSEAGE", required = true)
    protected String doseage;
    @XmlElement(name = "DOSEAGE_UNIT", required = true)
    protected String doseageunit;
    @XmlElement(name = "CHARGE_AMOUNT", required = true)
    protected String chargeamount;
    @XmlElement(name = "DRUG_OCC", required = true)
    protected String drugocc;
    @XmlElement(name = "MINI_UNIT", required = true)
    protected String miniunit;
    @XmlElement(name = "PARENT_NO", required = true)
    protected String parentno;
    @XmlElement(name = "FIT_FLAG", required = true)
    protected String fitflag;
    @XmlElement(name = "SELF_BUY", required = true)
    protected String selfbuy;
    @XmlElement(name = "INSTRUCTION", required = true)
    protected String instruction;
    @XmlElement(name = "DEPT_SN", required = true)
    protected String deptsn;
    @XmlElement(name = "WARD_SN", required = true)
    protected String wardsn;
    @XmlElement(name = "ORDER_NAME", required = true)
    protected String ordername;
    @XmlElement(name = "EXEC_UNIT", required = true)
    protected String execunit;
    @XmlAttribute(name = "action", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String action;

    /**
     * Gets the value of the orderno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERNO() {
        return orderno;
    }

    /**
     * Sets the value of the orderno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERNO(String value) {
        this.orderno = value;
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
     * Gets the value of the admisstimes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADMISSTIMES() {
        return admisstimes;
    }

    /**
     * Sets the value of the admisstimes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADMISSTIMES(String value) {
        this.admisstimes = value;
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
     * Gets the value of the serial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSERIAL() {
        return serial;
    }

    /**
     * Sets the value of the serial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSERIAL(String value) {
        this.serial = value;
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
     * Gets the value of the endtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENDTIME() {
        return endtime;
    }

    /**
     * Sets the value of the endtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENDTIME(String value) {
        this.endtime = value;
    }

    /**
     * Gets the value of the orderdoctor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERDOCTOR() {
        return orderdoctor;
    }

    /**
     * Sets the value of the orderdoctor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERDOCTOR(String value) {
        this.orderdoctor = value;
    }

    /**
     * Gets the value of the stopdoctor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTOPDOCTOR() {
        return stopdoctor;
    }

    /**
     * Sets the value of the stopdoctor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTOPDOCTOR(String value) {
        this.stopdoctor = value;
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
     * Gets the value of the stopconfirmopera property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTOPCONFIRMOPERA() {
        return stopconfirmopera;
    }

    /**
     * Sets the value of the stopconfirmopera property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTOPCONFIRMOPERA(String value) {
        this.stopconfirmopera = value;
    }

    /**
     * Gets the value of the frequcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFREQUCODE() {
        return frequcode;
    }

    /**
     * Sets the value of the frequcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFREQUCODE(String value) {
        this.frequcode = value;
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
     * Gets the value of the supplycode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUPPLYCODE() {
        return supplycode;
    }

    /**
     * Sets the value of the supplycode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUPPLYCODE(String value) {
        this.supplycode = value;
    }

    /**
     * Gets the value of the drugspecification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDRUGSPECIFICATION() {
        return drugspecification;
    }

    /**
     * Sets the value of the drugspecification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDRUGSPECIFICATION(String value) {
        this.drugspecification = value;
    }

    /**
     * Gets the value of the doseage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOSEAGE() {
        return doseage;
    }

    /**
     * Sets the value of the doseage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOSEAGE(String value) {
        this.doseage = value;
    }

    /**
     * Gets the value of the doseageunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOSEAGEUNIT() {
        return doseageunit;
    }

    /**
     * Sets the value of the doseageunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOSEAGEUNIT(String value) {
        this.doseageunit = value;
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
     * Gets the value of the drugocc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDRUGOCC() {
        return drugocc;
    }

    /**
     * Sets the value of the drugocc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDRUGOCC(String value) {
        this.drugocc = value;
    }

    /**
     * Gets the value of the miniunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMINIUNIT() {
        return miniunit;
    }

    /**
     * Sets the value of the miniunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMINIUNIT(String value) {
        this.miniunit = value;
    }

    /**
     * Gets the value of the parentno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPARENTNO() {
        return parentno;
    }

    /**
     * Sets the value of the parentno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPARENTNO(String value) {
        this.parentno = value;
    }

    /**
     * Gets the value of the fitflag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFITFLAG() {
        return fitflag;
    }

    /**
     * Sets the value of the fitflag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFITFLAG(String value) {
        this.fitflag = value;
    }

    /**
     * Gets the value of the selfbuy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSELFBUY() {
        return selfbuy;
    }

    /**
     * Sets the value of the selfbuy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSELFBUY(String value) {
        this.selfbuy = value;
    }

    /**
     * Gets the value of the instruction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSTRUCTION() {
        return instruction;
    }

    /**
     * Sets the value of the instruction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSTRUCTION(String value) {
        this.instruction = value;
    }

    /**
     * Gets the value of the deptsn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPTSN() {
        return deptsn;
    }

    /**
     * Sets the value of the deptsn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPTSN(String value) {
        this.deptsn = value;
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