package com.zebone.nhis.common.module.base.bd.res;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DEPT_UNIT  - bd_dept_unit 
 *
 * @since 2016-10-08 01:41:45
 */
@Table(value="BD_DEPT_UNIT")
public class BdDeptUnit extends BaseModule  {

	@PK
	@Field(value="PK_DEPTUNIT",id=KeyId.UUID)
    private String pkDeptunit;

	@Field(value="PK_DEPT")
    private String pkDept;

    /** EU_UNITTYPE - 0 诊室，1药房窗口 */
	@Field(value="EU_UNITTYPE")
    private String euUnittype;

	@Field(value="CODE")
    private String code;

	@Field(value="NOTE")
    private String note;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="FLAG_ONLINE")
    private String flagOnline;

	@Field(value="CNT_BU")
    private Long cntBu;

	@Field(value="NAME")
    private String name;

    /** EU_BUTYPE - 0 配药，1发药，9 其他 */
	@Field(value="EU_BUTYPE")
    private String euButype;

	//常开标志    0，标识该窗口不会关闭   1，在线窗口为1
	@Field(value="FLAG_OPEN")
    private String flagOpen;
	
	//关联单元
	@Field(value="PK_DEPTUNIT_RL")
    private String pkDeptunitRl;
	
	@Field(value="SCREEN")
	private String screen;
	
	@Field(value="VOICE")
	private String voice;

	@Field(value = "EU_USECATE")
	private String euUsecate;


    public String getScreen() {
		return screen;
	}
	public void setScreen(String screen) {
		this.screen = screen;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getFlagOpen() {
		return flagOpen;
	}
	public void setFlagOpen(String flagOpen) {
		this.flagOpen = flagOpen;
	}
	public String getPkDeptunitRl() {
		return pkDeptunitRl;
	}
	public void setPkDeptunitRl(String pkDeptunitRl) {
		this.pkDeptunitRl = pkDeptunitRl;
	}
	public String getPkDeptunit(){
        return this.pkDeptunit;
    }
    public void setPkDeptunit(String pkDeptunit){
        this.pkDeptunit = pkDeptunit;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getEuUnittype(){
        return this.euUnittype;
    }
    public void setEuUnittype(String euUnittype){
        this.euUnittype = euUnittype;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getFlagOnline(){
        return this.flagOnline;
    }
    public void setFlagOnline(String flagOnline){
        this.flagOnline = flagOnline;
    }

    public Long getCntBu(){
        return this.cntBu;
    }
    public void setCntBu(Long cntBu){
        this.cntBu = cntBu;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEuButype(){
        return this.euButype;
    }
    public void setEuButype(String euButype){
        this.euButype = euButype;
    }

    public String getEuUsecate() {
        return euUsecate;
    }

    public void setEuUsecate(String euUsecate) {
        this.euUsecate = euUsecate;
    }
}