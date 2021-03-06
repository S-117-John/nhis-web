//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.26 at 03:17:42 PM CST 
//


package com.zebone.nhis.webservice.vo.ticketvo;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{}beginTime"/>
 *         &lt;element ref="{}endTime"/>
 *         &lt;element ref="{}euPvtype"/>
 *         &lt;element ref="{}flagAppt"/>
 *         &lt;element ref="{}flagStop"/>
 *         &lt;element ref="{}flagUsed"/>
 *         &lt;element ref="{}pkSch"/>
 *         &lt;element ref="{}pkSchticket"/>
 *         &lt;element ref="{}ticketno"/>
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
    "beginTime",
    "endTime",
    "euPvtype",
    "flagAppt",
    "flagStop",
    "flagUsed",
    "pkSch",
    "pkSchticket",
    "ticketno",
    "price",
    "beginTimeStr",
    "endTimeStr",
    "cntAppt"
})
@XmlRootElement(name = "schsch")
public class Schsch {

    @XmlElement(required = true)
    @XmlSchemaType(name = "NCName")
    protected Date beginTime;
    @XmlElement(required = true)
    @XmlSchemaType(name = "NCName")
    protected Date endTime;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String euPvtype;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String flagAppt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String flagStop;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String flagUsed;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String pkSch;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String pkSchticket;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ticketno;
    @XmlElement(required = true)
    @XmlSchemaType(name = "NCName")
    protected Double price;

    @XmlElement(required = true)
    @XmlSchemaType(name = "NCName")
    protected String beginTimeStr;
    @XmlElement(required = true)
    @XmlSchemaType(name = "NCName")
    protected String endTimeStr;
    @XmlElement(required = true)
    @XmlSchemaType(name = "NCName")
    protected String cntAppt;
    /**
     * Gets the value of the beginTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getBeginTime() {
        return beginTime;
    }

    public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	/**
     * Sets the value of the beginTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeginTime(Date value) {
        this.beginTime = value;
    }

    /**
     * Gets the value of the endTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndTime(Date value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the euPvtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEuPvtype() {
        return euPvtype;
    }

    /**
     * Sets the value of the euPvtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEuPvtype(String value) {
        this.euPvtype = value;
    }

    /**
     * Gets the value of the flagAppt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagAppt() {
        return flagAppt;
    }

    /**
     * Sets the value of the flagAppt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagAppt(String value) {
        this.flagAppt = value;
    }

    /**
     * Gets the value of the flagStop property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagStop() {
        return flagStop;
    }

    /**
     * Sets the value of the flagStop property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagStop(String value) {
        this.flagStop = value;
    }

    /**
     * Gets the value of the flagUsed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagUsed() {
        return flagUsed;
    }

    /**
     * Sets the value of the flagUsed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagUsed(String value) {
        this.flagUsed = value;
    }

    /**
     * Gets the value of the pkSch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPkSch() {
        return pkSch;
    }

    /**
     * Sets the value of the pkSch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPkSch(String value) {
        this.pkSch = value;
    }

    /**
     * Gets the value of the pkSchticket property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPkSchticket() {
        return pkSchticket;
    }

    /**
     * Sets the value of the pkSchticket property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPkSchticket(String value) {
        this.pkSchticket = value;
    }

    /**
     * Gets the value of the ticketno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketno() {
        return ticketno;
    }

    /**
     * Sets the value of the ticketno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketno(String value) {
        this.ticketno = value;
    }

	public String getBeginTimeStr() {
		return beginTimeStr;
	}

	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getCntAppt() {
		return cntAppt;
	}

	public void setCntAppt(String cntAppt) {
		this.cntAppt = cntAppt;
	}
    

}
