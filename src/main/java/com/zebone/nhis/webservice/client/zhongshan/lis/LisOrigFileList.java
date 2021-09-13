
package com.zebone.nhis.webservice.client.zhongshan.lis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="str_patient" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="str_times" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="str_visit_flag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "strPatient",
    "strTimes",
    "strVisitFlag"
})
@XmlRootElement(name = "LisOrigFileList")
public class LisOrigFileList {

    @XmlElement(name = "str_patient")
    protected String strPatient;
    @XmlElement(name = "str_times")
    protected String strTimes;
    @XmlElement(name = "str_visit_flag")
    protected String strVisitFlag;

    /**
     * 获取strPatient属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrPatient() {
        return strPatient;
    }

    /**
     * 设置strPatient属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrPatient(String value) {
        this.strPatient = value;
    }

    /**
     * 获取strTimes属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrTimes() {
        return strTimes;
    }

    /**
     * 设置strTimes属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrTimes(String value) {
        this.strTimes = value;
    }

    /**
     * 获取strVisitFlag属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrVisitFlag() {
        return strVisitFlag;
    }

    /**
     * 设置strVisitFlag属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrVisitFlag(String value) {
        this.strVisitFlag = value;
    }

}
