package com.zebone.nhis.pro.zsba.cn.ipdw.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * @Classname CnOrdAntiExpre
 * @Description CN_ORD_ANTI_EXPRE - 临床-医嘱-特殊级抗菌药物专家库
 * @Date 2021-01-19 9:30
 * @Created by wuqiang
 */
@Table(value="CN_ORD_ANTI_EXPRE")
public class CnOrdAntiExpre extends BaseModule {

    /** PK_EXPRE - 主键 */
    @PK
    @Field(value="PK_EXPRE",id= Field.KeyId.UUID)
    private String pkExpre;

    /** PK_EMP_EXPRE - 专家主键 */
    @Field(value="PK_EMP_EXPRE")
    private String pkEmpExpre;

    /** DATE_INPUT - 添加日期 */
    @Field(value="DATE_INPUT")
    private Date dateInput;

    /** PK_EMP_ADD - 操作人员主键 */
    @Field(value="PK_EMP_ADD")
    private String pkEmpAdd;

    /** MODITY_TIME - 修改时间 */
    @Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkExpre(){
        return this.pkExpre;
    }
    public void setPkExpre(String pkExpre){
        this.pkExpre = pkExpre;
    }

    public String getPkEmpExpre(){
        return this.pkEmpExpre;
    }
    public void setPkEmpExpre(String pkEmpExpre){
        this.pkEmpExpre = pkEmpExpre;
    }

    public Date getDateInput(){
        return this.dateInput;
    }
    public void setDateInput(Date dateInput){
        this.dateInput = dateInput;
    }

    public String getPkEmpAdd(){
        return this.pkEmpAdd;
    }
    public void setPkEmpAdd(String pkEmpAdd){
        this.pkEmpAdd = pkEmpAdd;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}
