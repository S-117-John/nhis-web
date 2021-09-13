package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_dept_map - 外部医保-医保科室对照 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_DEPT_MAP")
public class InsZsBaYbDeptMap extends BaseModule  {

	@PK
	@Field(value="PK_INSDEPTMAP",id=KeyId.UUID)
    private String pkInsdeptmap;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="PK_DEPT")
    private String pkDept;
	
	@Field(value="PK_INSDEPT")
	private String pkInsdept;
	
	@Field(value="CODE_INSDEPT")
    private String codeInsdept;

	@Field(value="NAME_INSDEPT")
    private String nameInsdept;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInsdeptmap(){
        return this.pkInsdeptmap;
    }
    public void setPkInsdeptmap(String pkInsdeptmap){
        this.pkInsdeptmap = pkInsdeptmap;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }
    
    public String getPkInsdept() {
		return pkInsdept;
	}
	public void setPkInsdept(String pkInsdept) {
		this.pkInsdept = pkInsdept;
	}
	
	public String getCodeInsdept(){
        return this.codeInsdept;
    }
    public void setCodeInsdept(String codeInsdept){
        this.codeInsdept = codeInsdept;
    }

    public String getNameInsdept(){
        return this.nameInsdept;
    }
    public void setNameInsdept(String nameInsdept){
        this.nameInsdept = nameInsdept;
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