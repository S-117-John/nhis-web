package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_RIS  - bd_ord_ris 
 *
 * @since 2016-09-08 02:02:26
 */
@Table(value="BD_ORD_RIS")
public class BdOrdRis extends BaseModule  {

	@PK
	@Field(value="PK_ORDRIS",id=KeyId.UUID)
    private String pkOrdris;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="DT_TYPE")
    private String dtType;

	@Field(value="DT_BODY")
    private String dtBody;

	@Field(value="DESC_ATT")
	private String descAtt;
	
	@Field(value="REPORT_HEADER")
    private String reportHeader;

    /** REPORT_FOOTER - 注意事项 */
	@Field(value="REPORT_FOOTER")
    private String reportFooter;

    public String getPkOrdris(){
        return this.pkOrdris;
    }
    public void setPkOrdris(String pkOrdris){
        this.pkOrdris = pkOrdris;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getDtType(){
        return this.dtType;
    }
    public void setDtType(String dtType){
        this.dtType = dtType;
    }

    public String getDtBody(){
        return this.dtBody;
    }
    public void setDtBody(String dtBody){
        this.dtBody = dtBody;
    }

    public String getDescAtt() {
		return descAtt;
	}
	public void setDescAtt(String descAtt) {
		this.descAtt = descAtt;
	}
	public String getReportHeader(){
        return this.reportHeader;
    }
    public void setReportHeader(String reportHeader){
        this.reportHeader = reportHeader;
    }

    public String getReportFooter(){
        return this.reportFooter;
    }
    public void setReportFooter(String reportFooter){
        this.reportFooter = reportFooter;
    }

}