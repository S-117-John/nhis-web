package com.zebone.nhis.common.module.sch.pub;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: SCH_SRV_ORD  - sch_srv_ord 
 *
 * @since 2016-09-12 09:33:21
 */
@Table(value="SCH_SRV_ORD")
public class SchSrvOrd extends BaseModule  {

	@PK
	@Field(value="PK_SCHSRVORD",id=KeyId.UUID)
    private String pkSchsrvord;

	@Field(value="PK_SCHSRV")
    private String pkSchsrv;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFY_TIME")
    private Date modityTime;

    @Field(value = "EU_TYPE")
    private String euType;

    @Field(value = "LENGTH")
    private Integer length;

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getPkSchsrvord(){
        return this.pkSchsrvord;
    }
    public void setPkSchsrvord(String pkSchsrvord){
        this.pkSchsrvord = pkSchsrvord;
    }

    public String getPkSchsrv(){
        return this.pkSchsrv;
    }
    public void setPkSchsrv(String pkSchsrv){
        this.pkSchsrv = pkSchsrv;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}