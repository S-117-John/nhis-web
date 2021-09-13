package com.zebone.nhis.base.pub.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Table(value = "BD_DATADUMP_DIC")
public class BdDatadumpDic extends BaseModule {

    @PK
    @Field(value="PK_DATADUMP_DIC",id= Field.KeyId.UUID)
    private String pkDatadumpDic;

    @Field(value = "SORT_NO")
    private String sortNo;

    @Field(value = "OWNER")
    private String owner;

    @Field(value = "TABLE_SOURCE")
    private String tableSource;

    @Field(value = "TABLE_TARGET")
    private String tableTarget;

    @Field(value = "MODITY_TIME")
    private Date modityTime;

    public String getPkDatadumpDic() {
        return pkDatadumpDic;
    }

    public void setPkDatadumpDic(String pkDatadumpDic) {
        this.pkDatadumpDic = pkDatadumpDic;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTableSource() {
        return tableSource;
    }

    public void setTableSource(String tableSource) {
        this.tableSource = tableSource;
    }

    public String getTableTarget() {
        return tableTarget;
    }

    public void setTableTarget(String tableTarget) {
        this.tableTarget = tableTarget;
    }

    public Date getModityTime() {
        return modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
    }
}
