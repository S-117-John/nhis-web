//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.07.15 at 08:56:47 PM CST 
//


package com.zebone.nhis.webservice.vo.payordervo;

import java.util.List;

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
 *         &lt;element ref="{}settleData"/>
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
    "settleData"
})
@XmlRootElement(name = "settleDatas")
public class SettleDatas {

    @XmlElement(required = true)
    protected List<SettleData> settleData;

    /**
     * Gets the value of the settleData property.
     * 
     * @return
     *     possible object is
     *     {@link SettleData }
     *     
     */
    public List<SettleData> getSettleData() {
        return settleData;
    }

    /**
     * Sets the value of the settleData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SettleData }
     *     
     */
    public void setSettleData(List<SettleData> value) {
        this.settleData = value;
    }

}