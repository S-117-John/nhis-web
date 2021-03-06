//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.11 at 04:51:20 PM CST 
//


package com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.MessOuterResponse;

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
 *         &lt;element ref="{}MessageHeader"/>
 *         &lt;element ref="{}ResponseOption"/>
 *         &lt;element ref="{}MsgInfo"/>
 *         &lt;element ref="{}RetInfo"/>
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
    "messageHeader",
    "responseOption",
    "msgInfo",
    "retInfo"
})
@XmlRootElement(name = "ESBEntry")
public class ResponseESBEntry {

    @XmlElement(name = "MessageHeader", required = true)
    private ResponseMessageHeader messageHeader;
    @XmlElement(name = "ResponseOption", required = true)
    private  ResponseOption responseOption;
    @XmlElement(name = "MsgInfo", required = true)
    private ResponseMsgInfo msgInfo;
    @XmlElement(name = "RetInfo", required = true)
    private ResponseRetInfo retInfo;

    /**
     * Gets the value of the messageHeader property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseMessageHeader }
     *     
     */
    public ResponseMessageHeader getMessageHeader() {
        return messageHeader;
    }

    /**
     * Sets the value of the messageHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseMessageHeader }
     *     
     */
    public void setMessageHeader(ResponseMessageHeader value) {
        this.messageHeader = value;
    }

    /**
     * Gets the value of the responseOption property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseOption }
     *     
     */
    public ResponseOption getResponseOption() {
        return responseOption;
    }

    /**
     * Sets the value of the responseOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseOption }
     *     
     */
    public void setResponseOption(ResponseOption value) {
        this.responseOption = value;
    }

    /**
     * Gets the value of the msgInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseMsgInfo }
     *     
     */
    public ResponseMsgInfo getMsgInfo() {
        return msgInfo;
    }

    /**
     * Sets the value of the msgInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseMsgInfo }
     *     
     */
    public void setMsgInfo(ResponseMsgInfo value) {
        this.msgInfo = value;
    }

    /**
     * Gets the value of the retInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseRetInfo }
     *     
     */
    public ResponseRetInfo getRetInfo() {
        return retInfo;
    }

    /**
     * Sets the value of the retInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseRetInfo }
     *     
     */
    public void setRetInfo(ResponseRetInfo value) {
        this.retInfo = value;
    }

}
