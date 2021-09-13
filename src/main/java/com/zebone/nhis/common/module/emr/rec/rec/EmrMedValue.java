package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: EMR_MED_VALUE 
 *
 * @since 2020-09-16 05:09:28
 */
@Table(value="EMR_MED_VALUE")
public class EmrMedValue  extends BaseModule {

	@PK
	@Field(value="Pk_Id",id=KeyId.UUID)
    private String pkId;

	@Field(value="CODE")
    private String code;

	@Field(value="TEXT")
    private String text;

	@Field(value="NAME")
    private String name;

	@Field(value="FATHER")
    private String father;

	@Field(value="CATEGORY")
    private String category;

	@Field(value="ITEM")
    private String item;

	@Field(value="BIAG_DATA")
    private String biagData;

//	@Field(value="pkEmp")
//    private String creator;
//
//	@Field(value="CREATE_TIME")
//    private Date createTime;
//
//	@Field(userfield="pkEmp")
//    private String modifier;
//
//	@Field(value="DEL_FLAG")
//    private String delFlag;
//
//	@Field(date=FieldType.ALL)
//    private Date ts;


    public String getPkId(){
        return this.pkId;
    }
    public void setPkId(String pkId){
        this.pkId = pkId;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getText(){
        return this.text;
    }
    public void setText(String text){
        this.text = text;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getFather(){
        return this.father;
    }
    public void setFather(String father){
        this.father = father;
    }

    public String getCategory(){
        return this.category;
    }
    public void setCategory(String category){
        this.category = category;
    }

    public String getItem(){
        return this.item;
    }
    public void setItem(String item){
        this.item = item;
    }

    public String getBiagData(){
        return this.biagData;
    }
    public void setBiagData(String biagData){
        this.biagData = biagData;
    }

//    public String getCreator(){
//        return this.creator;
//    }
//    public void setCreator(String creator){
//        this.creator = creator;
//    }
//
//    public Date getCreateTime(){
//        return this.createTime;
//    }
//    public void setCreateTime(Date createTime){
//        this.createTime = createTime;
//    }
//
//    public String getModifier(){
//        return this.modifier;
//    }
//    public void setModifier(String modifier){
//        this.modifier = modifier;
//    }
//
//    public String getDelFlag(){
//        return this.delFlag;
//    }
//    public void setDelFlag(String delFlag){
//        this.delFlag = delFlag;
//    }
//
//    public Date getTs(){
//        return this.ts;
//    }
//    public void setTs(Date ts){
//        this.ts = ts;
//    }
}