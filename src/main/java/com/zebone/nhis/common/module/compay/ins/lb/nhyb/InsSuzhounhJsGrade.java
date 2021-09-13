package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * table
 * INS_SUZHOUNH_JS_GRADE
 * @Auther: wuqiang
 * @Date: 2019/4/15 10:15
 * @Description:
 */
@Table(value ="INS_SUZHOUNH_JS_GRADE" )
public class InsSuzhounhJsGrade extends BaseModule {
    /** ID - 主键 */
    @PK
    @Field(value="PK_JS_GRADE",id= Field.KeyId.UUID)
    private String pkJsGrade;
    /** PKJS-外键（INS_SUZHOUNH_JS表主键） */
    @Field(value="PK_JS")
    private String  pkJs;
    /** PK_SETTLE- 结算主键*/
    @Field(value="PK_SETTLE")
    private String  pkSettle;
    /** BBDH -报补单号 */
    @Field(value="BBDH ")
    private String bbdh;
    /** fdbs-分段标识 */
    @Field(value="FDBS")
    private String fdbs;
    /** bxbl-报表比例*/
    @Field(value="BXBL")
    private String bxbl;
    /** rdje-入段金额 */
    @Field(value="RDJE")
    private String   rdje;
    /** bxje-报销金额 */
    @Field(value="BXJE")
    private String  bxje;
    /** bz-备注 */
    @Field(value="BZ")
    private String  bz;
    public String getPkJsGrade() {
        return pkJsGrade;
    }

    public void setPkJsGrade(String pkJsGrade) {
        this.pkJsGrade = pkJsGrade;
    }

    public String getPkJs() {
        return pkJs;
    }

    public void setPkJs(String pkJs) {
        this.pkJs = pkJs;
    }

    public String getPkSettle() {
        return pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

    public String getBbdh() {
        return bbdh;
    }

    public void setBbdh(String bbdh) {
        this.bbdh = bbdh;
    }

    public String getFdbs() {
        return fdbs;
    }

    public void setFdbs(String fdbs) {
        this.fdbs = fdbs;
    }

    public String getBxbl() {
        return bxbl;
    }

    public void setBxbl(String bxbl) {
        this.bxbl = bxbl;
    }

    public String getRdje() {
        return rdje;
    }

    public void setRdje(String rdje) {
        this.rdje = rdje;
    }

    public String getBxje() {
        return bxje;
    }

    public void setBxje(String bxje) {
        this.bxje = bxje;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }



}
