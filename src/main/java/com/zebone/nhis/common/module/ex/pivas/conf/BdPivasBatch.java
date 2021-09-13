package com.zebone.nhis.common.module.ex.pivas.conf;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PIVAS_BATCH - bd_pivas_batch 
 *
 * @since 2016-12-06 01:53:24
 */
@Table(value="BD_PIVAS_BATCH")
public class BdPivasBatch extends BaseModule  {

	@PK
	@Field(value="PK_PIVASBATCH",id=KeyId.UUID)
    private String pkPivasbatch;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** EU_TYPE - 0配置，1打包 */
	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="TIME_ADMIX")
    private String timeAdmix;

	@Field(value="TIME_BEGIN")
    private String timeBegin;

	@Field(value="TIME_END")
    private String timeEnd;

	@Field(value="NOTE")
    private String note;


    public String getPkPivasbatch(){
        return this.pkPivasbatch;
    }
    public void setPkPivasbatch(String pkPivasbatch){
        this.pkPivasbatch = pkPivasbatch;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getTimeAdmix(){
        return this.timeAdmix;
    }
    public void setTimeAdmix(String timeAdmix){
        this.timeAdmix = timeAdmix;
    }

    public String getTimeBegin(){
        return this.timeBegin;
    }
    public void setTimeBegin(String timeBegin){
        this.timeBegin = timeBegin;
    }

    public String getTimeEnd(){
        return this.timeEnd;
    }
    public void setTimeEnd(String timeEnd){
        this.timeEnd = timeEnd;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
}