package com.zebone.nhis.webservice.zhongshan.vo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author chengjia
 * @Description 泰康人寿接口查询电子病历入院记录请求实体类
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MedicalHistoryInfo")
public class TaiKangReqEmrAdmitRec {
    /*
     * 住院号-业务流水号
     * */

    @XmlElement(name = "PatientNumber")
    private String codeIp;
    /*
     * 入院时间-住院日期
     * */

    @XmlElement(name = "InHosDate")
    private Date dateBegin;
 
    

    public String getCodeIp() {
		return codeIp;
	}


	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}




	public Date getDateBegin() {
		return dateBegin;
	}


	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}


	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }


}
