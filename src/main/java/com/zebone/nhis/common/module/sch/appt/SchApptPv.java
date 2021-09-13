package com.zebone.nhis.common.module.sch.appt;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_APPT_PV  - 排班预约-预约就诊 
 *
 * @since 2016-09-19 08:57:44
 */
@Table(value="SCH_APPT_PV")
public class SchApptPv extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** PK_APPTPV - 就诊预约主键 */
	@PK
	@Field(value="PK_APPTPV",id=KeyId.UUID)
    private String pkApptpv;

    /** PK_SCHAPPT - 预约主键 */
	@Field(value="PK_SCHAPPT")
    private String pkSchappt;

    /** EU_APPTMODE - 预约方式:0 正常预约；1 诊间加号 */
	@Field(value="EU_APPTMODE")
    private String euApptmode;

    /** PK_EMP_PHY - 对应医生 */
	@Field(value="PK_EMP_PHY")
    private String pkEmpPhy;

    /** NAME_EMP_PHY - 医生姓名 */
	@Field(value="NAME_EMP_PHY")
    private String nameEmpPhy;

    /** PK_DEPT - 对应科室 --数据库无该字段*/
//	@Field(value="PK_DEPT")
//    private String pkDept;

    /** FLAG_PV - 入院标志 */
	@Field(value="FLAG_PV")
    private String flagPv;

    /** PK_PV - 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;

    public String getPkApptpv(){
        return this.pkApptpv;
    }
    public void setPkApptpv(String pkApptpv){
        this.pkApptpv = pkApptpv;
    }

    public String getPkSchappt(){
        return this.pkSchappt;
    }
    public void setPkSchappt(String pkSchappt){
        this.pkSchappt = pkSchappt;
    }

    public String getEuApptmode(){
        return this.euApptmode;
    }
    public void setEuApptmode(String euApptmode){
        this.euApptmode = euApptmode;
    }

    public String getPkEmpPhy(){
        return this.pkEmpPhy;
    }
    public void setPkEmpPhy(String pkEmpPhy){
        this.pkEmpPhy = pkEmpPhy;
    }

    public String getNameEmpPhy(){
        return this.nameEmpPhy;
    }
    public void setNameEmpPhy(String nameEmpPhy){
        this.nameEmpPhy = nameEmpPhy;
    }

//    public String getPkDept(){
//        return this.pkDept;
//    }
//    public void setPkDept(String pkDept){
//        this.pkDept = pkDept;
//    }

    public String getFlagPv(){
        return this.flagPv;
    }
    public void setFlagPv(String flagPv){
        this.flagPv = flagPv;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

}