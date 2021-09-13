
package com.zebone.nhis.webservice.client.zhongshan.op;

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
 *         &lt;element name="UpdatePatsInHospitalResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "updatePatsInHospitalResult"
})
@XmlRootElement(name = "UpdatePatsInHospitalResponse")
public class UpdatePatsInHospitalResponse {

    @XmlElement(name = "UpdatePatsInHospitalResult")
    protected String updatePatsInHospitalResult;

    /**
     * 获取updatePatsInHospitalResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdatePatsInHospitalResult() {
        return updatePatsInHospitalResult;
    }

    /**
     * 设置updatePatsInHospitalResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdatePatsInHospitalResult(String value) {
        this.updatePatsInHospitalResult = value;
    }

}
