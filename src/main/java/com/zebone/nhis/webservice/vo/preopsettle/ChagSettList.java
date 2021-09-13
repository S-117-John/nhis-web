package com.zebone.nhis.webservice.vo.preopsettle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "chagSett"
})
@XmlRootElement(name = "chagSettList")
public class ChagSettList {

    @XmlElement(required = true)
    protected ChagSett chagSett;

    /**
     * Gets the value of the chagSett property.
     * 
     * @return
     *     possible object is
     *     {@link ChagSett }
     *     
     */
    public ChagSett getChagSett() {
        return chagSett;
    }

    /**
     * Sets the value of the chagSett property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChagSett }
     *     
     */
    public void setChagSett(ChagSett value) {
        this.chagSett = value;
    }

}
