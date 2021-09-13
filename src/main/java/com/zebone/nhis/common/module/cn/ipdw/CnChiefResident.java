package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CN_CHIEF_RESIDENT - 临床-住院总值班 
 * @author ds
 * @since 2020-12-21 09:51:57
 */
@Table(value="CN_CHIEF_RESIDENT")
public class CnChiefResident extends BaseModule  {

    /** PK_CR - 总值班主键 */
	@PK
	@Field(value="PK_CR",id=KeyId.UUID)
    private String pkCr;

    /** PK_EMP - 医生主键 */
	@Field(value="PK_EMP")
    private String pkEmp;

    /** PK_DEPT - 值班科室主键 */
	@Field(value="PK_DEPT")
    private String pkDept;

    /** DATE_BEGIN - 开始日期 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;

    /** DATE_END - 结束日期 */
	@Field(value="DATE_END")
    private Date dateEnd;

    public String getPkCr(){
        return this.pkCr;
    }
    public void setPkCr(String pkCr){
        this.pkCr = pkCr;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

}