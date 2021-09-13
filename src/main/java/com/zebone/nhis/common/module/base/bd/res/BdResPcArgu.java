package com.zebone.nhis.common.module.base.bd.res;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 计算机工作站参数
 * Table: BD_RES_PC_ARGU  - bd_res_pc_argu 
 *
 * @since 2016-08-23 10:39:20
 */
@Table(value="BD_RES_PC_ARGU")
public class BdResPcArgu extends BaseModule  {
	@PK
	@Field(value="PK_PCARGU",id=KeyId.UUID)
    private String pkPcargu;

	@Field(value="PK_PC")
    private String pkPc;

    /** PK_ARGU - 对应全局参数表的Initcode */
	@Field(value="PK_ARGU")
    private String pkArgu;

    /** CODE_ARGU - 对应全局参数表的initname */
	@Field(value="CODE_ARGU")
    private String codeArgu;
	
	@Field(value="NAME_ARGU")
    private String nameArgu;

    /** NOTE_ARGU - 对应全局参数表的remark */
	@Field(value="NOTE_ARGU")
    private String noteArgu;

    /** ARGUVAL - 支持单值与多值，多值时用;号隔离 */
	@Field(value="ARGUVAL")
    private String arguval;

    /** FLAG_STOP - Y：停用
N：启用 */
	@Field(value="FLAG_STOP")
    private String flagStop;
	
	/**
	 * 科室
	 */
	@Field(value = "PK_DEPT")
	private String pkDept;

    @Field(value = "note")
	private String note;

    public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkPcargu(){
        return this.pkPcargu;
    }
    public void setPkPcargu(String pkPcargu){
        this.pkPcargu = pkPcargu;
    }

    public String getPkPc(){
        return this.pkPc;
    }
    public void setPkPc(String pkPc){
        this.pkPc = pkPc;
    }

    public String getPkArgu(){
        return this.pkArgu;
    }
    public void setPkArgu(String pkArgu){
        this.pkArgu = pkArgu;
    }

    public String getCodeArgu(){
        return this.codeArgu;
    }
    public void setCodeArgu(String codeArgu){
        this.codeArgu = codeArgu;
    }

    public String getNameArgu() {
		return nameArgu;
	}
	public void setNameArgu(String nameArgu) {
		this.nameArgu = nameArgu;
	}
	public String getNoteArgu(){
        return this.noteArgu;
    }
    public void setNoteArgu(String noteArgu){
        this.noteArgu = noteArgu;
    }

    public String getArguval(){
        return this.arguval;
    }
    public void setArguval(String arguval){
        this.arguval = arguval;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}