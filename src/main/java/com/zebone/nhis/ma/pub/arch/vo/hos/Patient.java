package com.zebone.nhis.ma.pub.arch.vo.hos;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}baseinfo"/>
 *         &lt;element ref="{}inhospital"/>
 *       &lt;/sequence>
 *       &lt;attribute name="pid" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="pwd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "baseinfo",
    "inhospital"
})
@XmlRootElement(name = "patient")
public class Patient {

    @XmlElement(required = true)
    protected Baseinfo baseinfo;
    @XmlElement(required = true)
    protected Inhospital inhospital;
    @XmlAttribute(name = "pid", required = true)
    protected BigInteger pid;
    @XmlAttribute(name = "pwd", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String pwd;

    /**
     * 获取baseinfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Baseinfo }
     *     
     */
    public Baseinfo getBaseinfo() {
        return baseinfo;
    }

    /**
     * 设置baseinfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Baseinfo }
     *     
     */
    public void setBaseinfo(Baseinfo value) {
        this.baseinfo = value;
    }

    /**
     * 获取inhospital属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Inhospital }
     *     
     */
    public Inhospital getInhospital() {
        return inhospital;
    }

    /**
     * 设置inhospital属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Inhospital }
     *     
     */
    public void setInhospital(Inhospital value) {
        this.inhospital = value;
    }

    /**
     * 获取pid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPid() {
        return pid;
    }

    /**
     * 设置pid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPid(BigInteger value) {
        this.pid = value;
    }

    /**
     * 获取pwd属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置pwd属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPwd(String value) {
        this.pwd = value;
    }

}
