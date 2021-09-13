package com.zebone.nhis.ma.pub.zsba.vo;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_MED_BAG_DETAIL - 药袋明细表 
 *
 * @since 2020-04-24 11:06:57
 */
@Table(value="EX_MED_BAG_DETAIL")
public class ExMedBagDetail   {

    /** PK_MEBD - 主键 */
	@PK
	@Field(value="PK_MEBD",id=KeyId.UUID)
    private String pkMebd;

    /** PK_MEDBAG - 药袋主键 */
	@Field(value="PK_MEDBAG")
    private String pkMedbag;

    /** PK_EXOCC - 执行单主键 */
	@Field(value="PK_EXOCC")
    private String pkExocc;

    public String getPkMebd(){
        return this.pkMebd;
    }
    public void setPkMebd(String pkMebd){
        this.pkMebd = pkMebd;
    }

    public String getPkMedbag(){
        return this.pkMedbag;
    }
    public void setPkMedbag(String pkMedbag){
        this.pkMedbag = pkMedbag;
    }

    public String getPkExocc(){
        return this.pkExocc;
    }
    public void setPkExocc(String pkExocc){
        this.pkExocc = pkExocc;
    }
}