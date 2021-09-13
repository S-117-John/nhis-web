package com.zebone.nhis.common.module.cn.ipdw;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CN_SIGN_CA 
 *
 * @since 2017-05-12 09:24:31
 */
@Table(value="CN_SIGN_CA")
public class CnSignCa extends BaseModule  {

	@PK
	@Field(value="PK_SIGNCA",id=KeyId.UUID)
    private String pkSignca;

	@Field(value="EU_BUTYPE")
    private String euButype;

	@Field(value="PK_BU")
    private String pkBu;

	@Field(value="EU_OPTYPE")
    private String euOptype;

	@Field(value="CONTENT")
    private String content;

	@Field(value="SIGN")
    private String sign;
	
	@Field(value="CERT")
    private String cert;
	
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;

    public String getPkSignca(){
        return this.pkSignca;
    }
    public void setPkSignca(String pkSignca){
        this.pkSignca = pkSignca;
    }

    public String getEuButype(){
        return this.euButype;
    }
    public void setEuButype(String euButype){
        this.euButype = euButype;
    }

    public String getPkBu(){
        return this.pkBu;
    }
    public void setPkBu(String pkBu){
        this.pkBu = pkBu;
    }

    public String getEuOptype(){
        return this.euOptype;
    }
    public void setEuOptype(String euOptype){
        this.euOptype = euOptype;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public String getSign(){
        return this.sign;
    }
    public void setSign(String sign){
        this.sign = sign;
    }
	public String getCert() {
		return cert;
	}
	public void setCert(String cert) {
		this.cert = cert;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
}