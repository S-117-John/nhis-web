
package com.zebone.nhis.webservice.client.zhongshan.lis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="dt_sdate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="dt_edate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
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
    "dtSdate",
    "dtEdate"
})
@XmlRootElement(name = "LisReportList")
public class LisReportList {

    @XmlElement(name = "dt_sdate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtSdate;
    @XmlElement(name = "dt_edate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtEdate;

    /**
     * 获取dtSdate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtSdate() {
        return dtSdate;
    }

    /**
     * 设置dtSdate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtSdate(XMLGregorianCalendar value) {
        this.dtSdate = value;
    }

    /**
     * 获取dtEdate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtEdate() {
        return dtEdate;
    }

    /**
     * 设置dtEdate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtEdate(XMLGregorianCalendar value) {
        this.dtEdate = value;
    }

}
