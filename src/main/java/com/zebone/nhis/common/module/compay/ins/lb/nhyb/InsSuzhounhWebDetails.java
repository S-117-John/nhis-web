package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNH_WEB_DETAILS 
 *
 * @since 2019-04-25 04:49:51
 */
@Table(value="INS_SUZHOUNH_WEB_DETAILS")
public class InsSuzhounhWebDetails extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** INPATIENTSN - INPATIENTSN-12 */
	@Field(value="INPATIENTSN")
    private String inpatientsn;

    /** HISDETAILCODE - HISDETAILCODE-100 */
	@Field(value="HISDETAILCODE")
    private String hisdetailcode;

    /** HISMEDICINECODE - HISMEDICINECODE-100 */
	@Field(value="HISMEDICINECODE")
    private String hismedicinecode;

    /** MEDICINECODE - MEDICINECODE-24 */
	@Field(value="MEDICINECODE")
    private String medicinecode;

    /** MEDICINENAME - MEDICINENAME-100 */
	@Field(value="MEDICINENAME")
    private String medicinename;

    /** SPEC - SPEC-200 */
	@Field(value="SPEC")
    private String spec;

    /** CONF - CONF-100 */
	@Field(value="CONF")
    private String conf;

    /** UNIT - UNIT-40 */
	@Field(value="UNIT")
    private String unit;

    /** PRICE - PRICE-12,4 */
	@Field(value="PRICE")
    private String price;

    /** QUANTITY - QUANTITY-12,2 */
	@Field(value="QUANTITY")
    private String quantity;

    /** USEDATE - USEDATE-YYYY-MM-DD */
	@Field(value="USEDATE")
    private String usedate;

    /** WRITEOFF - WRITEOFF-1 */
	@Field(value="WRITEOFF")
    private String writeoff;

    /** DETAILNO - DETAILNO-12 */
	@Field(value="DETAILNO")
    private String detailno;

    /** ENABLERATIO - ENABLERATIO-5,2 */
	@Field(value="ENABLERATIO")
    private String enableratio;

    /** ENABLEMONEY - ENABLEMONEY-12,2 */
	@Field(value="ENABLEMONEY")
    private String enablemoney;

    /** ESSENTIALMEDICINE - ESSENTIALMEDICINE-1 */
	@Field(value="ESSENTIALMEDICINE")
    private String essentialmedicine;

    /** CHINESEMEDICINE - CHINESEMEDICINE-1 */
	@Field(value="CHINESEMEDICINE")
    private String chinesemedicine;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getInpatientsn(){
        return this.inpatientsn;
    }
    public void setInpatientsn(String inpatientsn){
        this.inpatientsn = inpatientsn;
    }

    public String getHisdetailcode(){
        return this.hisdetailcode;
    }
    public void setHisdetailcode(String hisdetailcode){
        this.hisdetailcode = hisdetailcode;
    }

    public String getHismedicinecode(){
        return this.hismedicinecode;
    }
    public void setHismedicinecode(String hismedicinecode){
        this.hismedicinecode = hismedicinecode;
    }

    public String getMedicinecode(){
        return this.medicinecode;
    }
    public void setMedicinecode(String medicinecode){
        this.medicinecode = medicinecode;
    }

    public String getMedicinename(){
        return this.medicinename;
    }
    public void setMedicinename(String medicinename){
        this.medicinename = medicinename;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public String getConf(){
        return this.conf;
    }
    public void setConf(String conf){
        this.conf = conf;
    }

    public String getUnit(){
        return this.unit;
    }
    public void setUnit(String unit){
        this.unit = unit;
    }

    public String getPrice(){
        return this.price;
    }
    public void setPrice(String price){
        this.price = price;
    }

    public String getQuantity(){
        return this.quantity;
    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }

    public String getUsedate(){
        return this.usedate;
    }
    public void setUsedate(String usedate){
        this.usedate = usedate;
    }

    public String getWriteoff(){
        return this.writeoff;
    }
    public void setWriteoff(String writeoff){
        this.writeoff = writeoff;
    }

    public String getDetailno(){
        return this.detailno;
    }
    public void setDetailno(String detailno){
        this.detailno = detailno;
    }

    public String getEnableratio(){
        return this.enableratio;
    }
    public void setEnableratio(String enableratio){
        this.enableratio = enableratio;
    }

    public String getEnablemoney(){
        return this.enablemoney;
    }
    public void setEnablemoney(String enablemoney){
        this.enablemoney = enablemoney;
    }

    public String getEssentialmedicine(){
        return this.essentialmedicine;
    }
    public void setEssentialmedicine(String essentialmedicine){
        this.essentialmedicine = essentialmedicine;
    }

    public String getChinesemedicine(){
        return this.chinesemedicine;
    }
    public void setChinesemedicine(String chinesemedicine){
        this.chinesemedicine = chinesemedicine;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}