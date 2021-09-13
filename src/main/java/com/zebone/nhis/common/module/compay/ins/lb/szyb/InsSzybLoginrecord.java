package com.zebone.nhis.common.module.compay.ins.lb.szyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_LOGINRECORD 
 *
 * @since 2018-05-04 09:53:07
 */
@Table(value="INS_SZYB_LOGINRECORD")
public class InsSzybLoginrecord extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** JZID - JZID-就诊ID */
	@Field(value="JZID")
    private String jzid;

    /** RY - RY-人员 */
	@Field(value="RY")
    private String ry;

    /** QDYBLX - QDYBLX-签到医保类型 */
	@Field(value="QDYBLX")
    private String qdyblx;

    /** QDSJ - QDSJ-签到时间 */
	@Field(value="QDSJ")
    private Date qdsj;

    /** QDYWZQH - QDYWZQH-签到业务周期号 */
	@Field(value="QDYWZQH")
    private String qdywzqh;

    /** QTSJ - QTSJ-签退时间 */
	@Field(value="QTSJ")
    private Date qtsj;

    /** QTYWZQH - QTYWZQH-签退业务周期号 */
	@Field(value="QTYWZQH")
    private String qtywzqh;

    /** QDZT - QDZT-签到状态 */
	@Field(value="QDZT")
    private String qdzt;

    /** MODIFIER - 最后操作人 */
	@Field(value="MODIFIER")
    private String modifier;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getJzid(){
        return this.jzid;
    }
    public void setJzid(String jzid){
        this.jzid = jzid;
    }

    public String getRy(){
        return this.ry;
    }
    public void setRy(String ry){
        this.ry = ry;
    }

    public String getQdyblx(){
        return this.qdyblx;
    }
    public void setQdyblx(String qdyblx){
        this.qdyblx = qdyblx;
    }

    public Date getQdsj(){
        return this.qdsj;
    }
    public void setQdsj(Date qdsj){
        this.qdsj = qdsj;
    }

    public String getQdywzqh(){
        return this.qdywzqh;
    }
    public void setQdywzqh(String qdywzqh){
        this.qdywzqh = qdywzqh;
    }

    public Date getQtsj(){
        return this.qtsj;
    }
    public void setQtsj(Date qtsj){
        this.qtsj = qtsj;
    }

    public String getQtywzqh(){
        return this.qtywzqh;
    }
    public void setQtywzqh(String qtywzqh){
        this.qtywzqh = qtywzqh;
    }

    public String getQdzt(){
        return this.qdzt;
    }
    public void setQdzt(String qdzt){
        this.qdzt = qdzt;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}