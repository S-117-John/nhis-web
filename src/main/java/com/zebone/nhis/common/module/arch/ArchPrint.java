package com.zebone.nhis.common.module.arch;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;

@Table(value="ARCH_PRINT")
public class ArchPrint extends BaseModule {
    @PK
    @Field(value="PK_ARCHPRINT")
    private String pkArchprint;

    @Field(value="PK_ARCHDOC")
    private String pkArchdoc;

    @Field(value="DATE_PRINT")
    private Date datePrint;

    @Field(value="PK_EMP_PRINT")
    private String pkEmpPrint;

    @Field(value="NAME_EMP_PRINT")
    private String nameEmpPrint;

    public String getPkArchprint() {
        return pkArchprint;
    }

    public void setPkArchprint(String pkArchprint) {
        this.pkArchprint = pkArchprint == null ? null : pkArchprint.trim();
    }

    public String getPkArchdoc() {
        return pkArchdoc;
    }

    public void setPkArchdoc(String pkArchdoc) {
        this.pkArchdoc = pkArchdoc == null ? null : pkArchdoc.trim();
    }

    public Date getDatePrint() {
        return datePrint;
    }

    public void setDatePrint(Date datePrint) {
        this.datePrint = datePrint;
    }

    public String getPkEmpPrint() {
        return pkEmpPrint;
    }

    public void setPkEmpPrint(String pkEmpPrint) {
        this.pkEmpPrint = pkEmpPrint == null ? null : pkEmpPrint.trim();
    }

    public String getNameEmpPrint() {
        return nameEmpPrint;
    }

    public void setNameEmpPrint(String nameEmpPrint) {
        this.nameEmpPrint = nameEmpPrint == null ? null : nameEmpPrint.trim();
    }
}