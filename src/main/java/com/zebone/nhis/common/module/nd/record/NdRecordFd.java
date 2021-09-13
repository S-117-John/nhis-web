package com.zebone.nhis.common.module.nd.record;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_RECORD_FD 
 *
 * @since 2017-06-08 11:29:24
 */
@Table(value="ND_RECORD_FD")
public class NdRecordFd extends BaseModule  {
	@PK
	@Field(value="PK_RECORDFD",id=KeyId.UUID)
    private String pkRecordfd;

	@Field(value="PK_RECORD")
    private String pkRecord;

	@Field(value="DATA_FD")
    private byte[] dataFd;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkRecordfd(){
        return this.pkRecordfd;
    }
    public void setPkRecordfd(String pkRecordfd){
        this.pkRecordfd = pkRecordfd;
    }

    public String getPkRecord(){
        return this.pkRecord;
    }
    public void setPkRecord(String pkRecord){
        this.pkRecord = pkRecord;
    }

    public byte[] getDataFd(){
        return this.dataFd;
    }
    public void setDataFd(byte[] dataFd){
        this.dataFd = dataFd;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}