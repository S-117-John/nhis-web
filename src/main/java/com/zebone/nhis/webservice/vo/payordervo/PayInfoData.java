//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.07.15 at 08:56:47 PM CST 
//


package com.zebone.nhis.webservice.vo.payordervo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}pkPi"/>
 *         &lt;element ref="{}pkOrg"/>
 *         &lt;element ref="{}orderType"/>
 *         &lt;element ref="{}orderSummary"/>
 *         &lt;element ref="{}orderData"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pkPi",
    "namePi",
    "nameOrg",
    "pkOrg",
    "orderType",
    "orderSummary",
    "orderData"
})
@XmlRootElement(name = "data")
public class PayInfoData {


	@XmlElement(required = true)
    protected String pkPi;
    @XmlElement(required = true)
    protected String pkOrg;
    @XmlElement(required = true)
    protected String orderType;
    @XmlElement(required = true)
    protected String nameOrg;
    @XmlElement(required = true)
    protected String namePi;
    @XmlElement(required = true)
    protected String orderSummary;
    @XmlElement(required = true)
    protected PayInfoOrderData orderData;
    
     

    public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
    /**
     * Gets the value of the pkPi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPkPi() {
        return pkPi;
    }

    /**
     * Sets the value of the pkPi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPkPi(String value) {
        this.pkPi = value;
    }

    /**
     * Gets the value of the pkOrg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPkOrg() {
        return pkOrg;
    }

    /**
     * Sets the value of the pkOrg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPkOrg(String value) {
        this.pkOrg = value;
    }

    /**
     * Gets the value of the orderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderType(String value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the orderSummary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderSummary() {
        return orderSummary;
    }

    /**
     * Sets the value of the orderSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderSummary(String value) {
        this.orderSummary = value;
    }

    /**
     * Gets the value of the orderData property.
     * 
     * @return
     *     possible object is
     *     {@link PayInfoOrderData }
     *     
     */
    public PayInfoOrderData getOrderData() {
        return orderData;
    }

    /**
     * Sets the value of the orderData property.
     * 
     * @param value
     *     allowed object is
     *     {@link PayInfoOrderData }
     *     
     */
    public void setOrderData(PayInfoOrderData value) {
        this.orderData = value;
    }

}
