//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.01 at 05:20:51 PM CST 
//


package com.zebone.nhis.ma.pub.platform.receive.yh.vo.PatiCg;

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
 *         &lt;element ref="{}AccessControl"/>
 *         &lt;element ref="{}MessageHeader"/>
 *         &lt;element ref="{}MsgInfo"/>
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
    "accessControl",
    "messageHeader",
    "msgInfo"
})
@XmlRootElement(name = "ESBEntry")
public class PatiCgESBEntry {

    @XmlElement(name = "AccessControl", required = true)
    protected AccessControl accessControl;
    @XmlElement(name = "MessageHeader", required = true)
    protected MessageHeader messageHeader;
    @XmlElement(name = "MsgInfo", required = true)
    protected MsgInfo msgInfo;

    /**
     * Gets the value of the accessControl property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControl }
     *     
     */
    public AccessControl getAccessControl() {
        return accessControl;
    }

    /**
     * Sets the value of the accessControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControl }
     *     
     */
    public void setAccessControl(AccessControl value) {
        this.accessControl = value;
    }

    /**
     * Gets the value of the messageHeader property.
     * 
     * @return
     *     possible object is
     *     {@link MessageHeader }
     *     
     */
    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    /**
     * Sets the value of the messageHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageHeader }
     *     
     */
    public void setMessageHeader(MessageHeader value) {
        this.messageHeader = value;
    }

    /**
     * Gets the value of the msgInfo property.
     * 
     * @return
     *     possible object is
     *     {@link MsgInfo }
     *     
     */
    public MsgInfo getMsgInfo() {
        return msgInfo;
    }

    /**
     * Sets the value of the msgInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link MsgInfo }
     *     
     */
    public void setMsgInfo(MsgInfo value) {
        this.msgInfo = value;
    }

}
