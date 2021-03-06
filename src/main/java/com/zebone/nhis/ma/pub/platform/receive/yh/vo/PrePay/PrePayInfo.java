//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.21 at 02:04:39 PM CST 
//


package com.zebone.nhis.ma.pub.platform.receive.yh.vo.PrePay;

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
 *         &lt;element ref="{}DEPT_INDEX_NO"/>
 *         &lt;element ref="{}RECEIPT_INDEX_NO"/>
 *         &lt;element ref="{}CHARGE_FLAG"/>
 *         &lt;element ref="{}PAY_METHOD_NAME"/>
 *         &lt;element ref="{}PAY_CATEG_NAME"/>
 *         &lt;element ref="{}PAT_INDEX_NO"/>
 *         &lt;element ref="{}INHOSP_INDEX_NO"/>
 *         &lt;element ref="{}PROS_PAY"/>
 *         &lt;element ref="{}RECORD_DR_CODE"/>
 *         &lt;element ref="{}RECEIPT_NO"/>
 *         &lt;element ref="{}PAY_TIME"/>
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
    "deptindexno",
    "receiptindexno",
    "chargeflag",
    "paymethodname",
    "paycategname",
    "patindexno",
    "inhospindexno",
    "prospay",
    "recorddrcode",
    "receiptno",
    "paytime"
})
@XmlRootElement(name = "Msg")
public class PrePayInfo {

    @XmlElement(name = "DEPT_INDEX_NO", required = true)
    protected String deptindexno;
    @XmlElement(name = "RECEIPT_INDEX_NO", required = true)
    protected String receiptindexno;
    @XmlElement(name = "CHARGE_FLAG", required = true)
    protected String chargeflag;
    @XmlElement(name = "PAY_METHOD_NAME", required = true)
    protected String paymethodname;
    @XmlElement(name = "PAY_CATEG_NAME", required = true)
    protected String paycategname;
    @XmlElement(name = "PAT_INDEX_NO", required = true)
    protected String patindexno;
    @XmlElement(name = "INHOSP_INDEX_NO", required = true)
    protected String inhospindexno;
    @XmlElement(name = "PROS_PAY", required = true)
    protected String prospay;
    @XmlElement(name = "RECORD_DR_CODE", required = true)
    protected String recorddrcode;
    @XmlElement(name = "RECEIPT_NO", required = true)
    protected String receiptno;
    @XmlElement(name = "PAY_TIME", required = true)
    protected String paytime;
    @XmlAttribute(name = "action", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String action;

    /**
     * Gets the value of the deptindexno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPTINDEXNO() {
        return deptindexno;
    }

    /**
     * Sets the value of the deptindexno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPTINDEXNO(String value) {
        this.deptindexno = value;
    }

    /**
     * Gets the value of the receiptindexno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECEIPTINDEXNO() {
        return receiptindexno;
    }

    /**
     * Sets the value of the receiptindexno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECEIPTINDEXNO(String value) {
        this.receiptindexno = value;
    }

    /**
     * Gets the value of the chargeflag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCHARGEFLAG() {
        return chargeflag;
    }

    /**
     * Sets the value of the chargeflag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCHARGEFLAG(String value) {
        this.chargeflag = value;
    }

    /**
     * Gets the value of the paymethodname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAYMETHODNAME() {
        return paymethodname;
    }

    /**
     * Sets the value of the paymethodname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAYMETHODNAME(String value) {
        this.paymethodname = value;
    }

    /**
     * Gets the value of the paycategname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAYCATEGNAME() {
        return paycategname;
    }

    /**
     * Sets the value of the paycategname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAYCATEGNAME(String value) {
        this.paycategname = value;
    }

    /**
     * Gets the value of the patindexno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPATINDEXNO() {
        return patindexno;
    }

    /**
     * Sets the value of the patindexno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPATINDEXNO(String value) {
        this.patindexno = value;
    }

    /**
     * Gets the value of the inhospindexno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINHOSPINDEXNO() {
        return inhospindexno;
    }

    /**
     * Sets the value of the inhospindexno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINHOSPINDEXNO(String value) {
        this.inhospindexno = value;
    }

    /**
     * Gets the value of the prospay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROSPAY() {
        return prospay;
    }

    /**
     * Sets the value of the prospay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROSPAY(String value) {
        this.prospay = value;
    }

    /**
     * Gets the value of the recorddrcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECORDDRCODE() {
        return recorddrcode;
    }

    /**
     * Sets the value of the recorddrcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECORDDRCODE(String value) {
        this.recorddrcode = value;
    }

    /**
     * Gets the value of the receiptno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECEIPTNO() {
        return receiptno;
    }

    /**
     * Sets the value of the receiptno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECEIPTNO(String value) {
        this.receiptno = value;
    }

    /**
     * Gets the value of the paytime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAYTIME() {
        return paytime;
    }

    /**
     * Sets the value of the paytime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAYTIME(String value) {
        this.paytime = value;
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
