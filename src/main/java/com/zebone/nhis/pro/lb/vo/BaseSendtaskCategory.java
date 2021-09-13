package com.zebone.nhis.pro.lb.vo;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BASE_SENDTASK_CATEGORY
 *
 * @since 2019-12-19 11:11:54
 */
@Table(value="BASE_SENDTASK_CATEGORY")
public class BaseSendtaskCategory   {
    @PK
    @Field(value="id")
    private Long id;

    @Field(value="companyId")
    private String companyid;

    @Field(value="mobiles")
    private String mobiles;

    @Field(value="typeName")
    private String typename;

    @Field(value="autoCheck")
    private Long autocheck;

    @Field(value="timeToSend")
    private String timetosend;

    @Field(value="temp")
    private String temp;


    public Long getId(){
        return this.id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getCompanyid(){
        return this.companyid;
    }
    public void setCompanyid(String companyid){
        this.companyid = companyid;
    }

    public String getMobiles(){
        return this.mobiles;
    }
    public void setMobiles(String mobiles){
        this.mobiles = mobiles;
    }

    public String getTypename(){
        return this.typename;
    }
    public void setTypename(String typename){
        this.typename = typename;
    }

    public Long getAutocheck(){
        return this.autocheck;
    }
    public void setAutocheck(Long autocheck){
        this.autocheck = autocheck;
    }

    public String getTimetosend(){
        return this.timetosend;
    }
    public void setTimetosend(String timetosend){
        this.timetosend = timetosend;
    }

    public String getTemp(){
        return this.temp;
    }
    public void setTemp(String temp){
        this.temp = temp;
    }
}
