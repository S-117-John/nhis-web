package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PT_ExamPAC")
@XmlAccessorType(XmlAccessType.FIELD)
public class PTExamPACS {
    @XmlElement(name = "SOPClassUID")
    protected String sopClassUID;
    @XmlElement(name = "SeriesDescription")
    protected String seriesDescription;
    @XmlElement(name = "SeriesUID")
    protected String seriesUID;
    @XmlElement(name = "StudyDescription")
    protected String studyDescription;
    @XmlElement(name = "StudyUID", required = true)
    protected String studyUID;
    @XmlElement(name = "InstanceUID")
    protected String instanceUID;
    @XmlElement(name = "YXH")
    protected String yxh;
    @XmlElement(name = "JCH")
    protected String jch;
    @XmlElement(name = "JCBW")
    protected String jcbw;
    @XmlElement(name = "JCBWYFF")
    protected String jcbwyff;
    
    @XmlElement(name = "ZYJCXX")
    protected String zyjcxx;
    
    @XmlElement(name = "YYX_DM")
    protected String yyxdm;
    
    @XmlElement(name = "LCZD")
    protected String lczd;
    
    @XmlElement(name = "YXBXHJCSJ")
    protected String yxbxhjcsj;
    
    @XmlElement(name = "JCZDHTS1")
    protected String jczdhts1;
    
    @XmlElement(name = "YXFWQDZ")
    protected String yxfwqdz;  
    @XmlElement(name = "BZHJY")
    protected String bzhjy;

    /*
    SeriesDescription	影像序列描述	S1	AN..500		影像报告	可选
    SOPClassUID	影像类型	S1	AN..64		影像报告	必填项
    InstanceUID	影像唯一ID	S1	AN..100		影像报告	必填项
    SFFSX	是否放射性	S2	AN1	CVX_Right		必填项
    YXSL	影像数量	N	N..10,2			可选
    BGKS_BM	报告科室编码	S1	AN..50			必填项
    BGKSMC	报告科室名称	S1	AN..100			必填项
    BGYSGH	报告医生工号	S1	AN..50			必填项
    BGYSXM	报告医生姓名	S1	AN..50			必填项
    BGSJ	报告时间	DT	DT15			必填项
    SHYSGH	审核医生工号	S1	AN..50			必填项
    SHYSXM	审核医生姓名	S1	AN..50			必填项
    SHRQ	审核日期	D	D8			必填项
*/

    
	public String getSopClassUID() {
		return sopClassUID;
	}
	public void setSopClassUID(String sopClassUID) {
		this.sopClassUID = sopClassUID;
	}
	public String getSeriesDescription() {
		return seriesDescription;
	}
	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}
	public String getSeriesUID() {
		return seriesUID;
	}
	public void setSeriesUID(String seriesUID) {
		this.seriesUID = seriesUID;
	}
	public String getStudyDescription() {
		return studyDescription;
	}
	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}
	public String getStudyUID() {
		return studyUID;
	}
	public void setStudyUID(String studyUID) {
		this.studyUID = studyUID;
	}
	public String getInstanceUID() {
		return instanceUID;
	}
	public void setInstanceUID(String instanceUID) {
		this.instanceUID = instanceUID;
	}
	public String getYxh() {
		return yxh;
	}
	public void setYxh(String yxh) {
		this.yxh = yxh;
	}

	public String getJch() {
		return jch;
	}

	public void setJch(String jch) {
		this.jch = jch;
	}

	public String getJcbw() {
		return jcbw;
	}

	public void setJcbw(String jcbw) {
		this.jcbw = jcbw;
	}

	public String getJcbwyff() {
		return jcbwyff;
	}

	public void setJcbwyff(String jcbwyff) {
		this.jcbwyff = jcbwyff;
	}

	public String getZyjcxx() {
		return zyjcxx;
	}

	public void setZyjcxx(String zyjcxx) {
		this.zyjcxx = zyjcxx;
	}

	public String getYyxdm() {
		return yyxdm;
	}

	public void setYyxdm(String yyxdm) {
		this.yyxdm = yyxdm;
	}

	public String getLczd() {
		return lczd;
	}

	public void setLczd(String lczd) {
		this.lczd = lczd;
	}

	public String getYxbxhjcsj() {
		return yxbxhjcsj;
	}

	public void setYxbxhjcsj(String yxbxhjcsj) {
		this.yxbxhjcsj = yxbxhjcsj;
	}

	public String getJczdhts1() {
		return jczdhts1;
	}

	public void setJczdhts1(String jczdhts1) {
		this.jczdhts1 = jczdhts1;
	}

	public String getYxfwqdz() {
		return yxfwqdz;
	}

	public void setYxfwqdz(String yxfwqdz) {
		this.yxfwqdz = yxfwqdz;
	}

	public String getBzhjy() {
		return bzhjy;
	}

	public void setBzhjy(String bzhjy) {
		this.bzhjy = bzhjy;
	}
}
