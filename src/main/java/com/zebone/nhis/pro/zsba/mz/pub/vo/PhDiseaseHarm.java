package com.zebone.nhis.pro.zsba.mz.pub.vo;

/**
 * @Classname PhDiseaseHarm
 * @Description 全国伤害性报卡
 * @Date 2021-04-12 10:19
 * @Created by wuqiang
 */

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * Table: PH_DISEASE_HARM
 *
 * @since 2021-04-12 10:18:36
 */
@Table(value="PH_DISEASE_HARM")
public class PhDiseaseHarm extends BaseModule {

    @PK
    @Field(value="PK_HARM",id= Field.KeyId.UUID)
    private String pkHarm;

    @Field(value="PK_PI")
    private String pkPi;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="EU_STATUS")
    private String euStatus;

    @Field(value="VC_ID")
    private String vcId;

    @Field(value="VC_JCYYBH")
    private String vcJcyybh;

    @Field(value="VC_KBBH")
    private String vcKbbh;

    @Field(value="VC_XM")
    private String vcXm;

    @Field(value="VC_XB")
    private String vcXb;

    @Field(value="NB_NL")
    private String nbNl;

    @Field(value="VC_CSRQ")
    private Date vcCsrq;

    @Field(value="VC_SFZHM")
    private String vcSfzhm;

    @Field(value="VC_LXDH")
    private String vcLxdh;

    @Field(value="VC_HJ")
    private String vcHj;

    @Field(value="VC_WHCD")
    private String vcWhcd;

    @Field(value="VC_ZY")
    private String vcZy;

    @Field(value="VC_SHFSSJ")
    private Date vcShfssj;

    @Field(value="VC_SHJZSJ")
    private Date vcShjzsj;

    @Field(value="VC_SHFSYY")
    private String vcShfsyy;

    @Field(value="VC_SHFSYYQT")
    private String vcShfsyyqt;

    @Field(value="VC_SHFSDD")
    private String vcShfsdd;

    @Field(value="VC_SHFSDDQT")
    private String vcShfsddqt;

    @Field(value="VC_SHFSSHD")
    private String vcShfsshd;

    @Field(value="VC_SHFSSHDQT")
    private String vcShfsshdqt;

    @Field(value="VC_SFGY")
    private String vcSfgy;

    @Field(value="VC_SFGYQT")
    private String vcSfgyqt;

    @Field(value="VC_YJQK")
    private String vcYjqk;

    @Field(value="VC_SHXZ")
    private String vcShxz;

    @Field(value="VC_SHXZQT")
    private String vcShxzqt;

    @Field(value="VC_SHBW")
    private String vcShbw;

    @Field(value="VC_SHBWQT")
    private String vcShbwqt;

    @Field(value="VC_SHLJXT")
    private String vcShljxt;

    @Field(value="VC_SHLJXTQT")
    private String vcShljxtqt;

    @Field(value="VC_SHYZCD")
    private String vcShyzcd;

    @Field(value="VC_SHLCZD")
    private String vcShlczd;

    @Field(value="VC_SHJJ")
    private String vcShjj;

    @Field(value="VC_SHJJQT")
    private String vcShjjqt;

    @Field(value="VC_SHSJWPMCA")
    private String vcShsjwpmca;

    @Field(value="VC_SHSJWPPPA_XX")
    private String vcShsjwpppaXx;

    @Field(value="VC_SHSJWPPPA")
    private String vcShsjwpppa;

    @Field(value="VC_SHSJWPMCB")
    private String vcShsjwpmcb;

    @Field(value="VC_SHSJWPPPB_XX")
    private String vcShsjwpppbXx;

    @Field(value="VC_SHSJWPPPB")
    private String vcShsjwpppb;

    @Field(value="VC_CPZLSFYG")
    private String vcCpzlsfyg;

    @Field(value="VC_CPZSJS")
    private String vcCpzsjs;

    @Field(value="VC_DXALLX")
    private String vcDxallx;

    @Field(value="VC_TBR")
    private String vcTbr;

    @Field(value="VC_TKRQ")
    private Date vcTkrq;

    @Field(value="VC_CJSJ")
    private Date vcCjsj;


    public String getPkHarm(){
        return this.pkHarm;
    }
    public void setPkHarm(String pkHarm){
        this.pkHarm = pkHarm;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getVcId(){
        return this.vcId;
    }
    public void setVcId(String vcId){
        this.vcId = vcId;
    }

    public String getVcJcyybh(){
        return this.vcJcyybh;
    }
    public void setVcJcyybh(String vcJcyybh){
        this.vcJcyybh = vcJcyybh;
    }

    public String getVcKbbh(){
        return this.vcKbbh;
    }
    public void setVcKbbh(String vcKbbh){
        this.vcKbbh = vcKbbh;
    }

    public String getVcXm(){
        return this.vcXm;
    }
    public void setVcXm(String vcXm){
        this.vcXm = vcXm;
    }

    public String getVcXb(){
        return this.vcXb;
    }
    public void setVcXb(String vcXb){
        this.vcXb = vcXb;
    }

    public String getNbNl(){
        return this.nbNl;
    }
    public void setNbNl(String nbNl){
        this.nbNl = nbNl;
    }

    public Date getVcCsrq(){
        return this.vcCsrq;
    }
    public void setVcCsrq(Date vcCsrq){
        this.vcCsrq = vcCsrq;
    }

    public String getVcSfzhm(){
        return this.vcSfzhm;
    }
    public void setVcSfzhm(String vcSfzhm){
        this.vcSfzhm = vcSfzhm;
    }

    public String getVcLxdh(){
        return this.vcLxdh;
    }
    public void setVcLxdh(String vcLxdh){
        this.vcLxdh = vcLxdh;
    }

    public String getVcHj(){
        return this.vcHj;
    }
    public void setVcHj(String vcHj){
        this.vcHj = vcHj;
    }

    public String getVcWhcd(){
        return this.vcWhcd;
    }
    public void setVcWhcd(String vcWhcd){
        this.vcWhcd = vcWhcd;
    }

    public String getVcZy(){
        return this.vcZy;
    }
    public void setVcZy(String vcZy){
        this.vcZy = vcZy;
    }

    public Date getVcShfssj(){
        return this.vcShfssj;
    }
    public void setVcShfssj(Date vcShfssj){
        this.vcShfssj = vcShfssj;
    }

    public Date getVcShjzsj(){
        return this.vcShjzsj;
    }
    public void setVcShjzsj(Date vcShjzsj){
        this.vcShjzsj = vcShjzsj;
    }

    public String getVcShfsyy(){
        return this.vcShfsyy;
    }
    public void setVcShfsyy(String vcShfsyy){
        this.vcShfsyy = vcShfsyy;
    }

    public String getVcShfsyyqt(){
        return this.vcShfsyyqt;
    }
    public void setVcShfsyyqt(String vcShfsyyqt){
        this.vcShfsyyqt = vcShfsyyqt;
    }

    public String getVcShfsdd(){
        return this.vcShfsdd;
    }
    public void setVcShfsdd(String vcShfsdd){
        this.vcShfsdd = vcShfsdd;
    }

    public String getVcShfsddqt(){
        return this.vcShfsddqt;
    }
    public void setVcShfsddqt(String vcShfsddqt){
        this.vcShfsddqt = vcShfsddqt;
    }

    public String getVcShfsshd(){
        return this.vcShfsshd;
    }
    public void setVcShfsshd(String vcShfsshd){
        this.vcShfsshd = vcShfsshd;
    }

    public String getVcShfsshdqt(){
        return this.vcShfsshdqt;
    }
    public void setVcShfsshdqt(String vcShfsshdqt){
        this.vcShfsshdqt = vcShfsshdqt;
    }

    public String getVcSfgy(){
        return this.vcSfgy;
    }
    public void setVcSfgy(String vcSfgy){
        this.vcSfgy = vcSfgy;
    }

    public String getVcSfgyqt(){
        return this.vcSfgyqt;
    }
    public void setVcSfgyqt(String vcSfgyqt){
        this.vcSfgyqt = vcSfgyqt;
    }

    public String getVcYjqk(){
        return this.vcYjqk;
    }
    public void setVcYjqk(String vcYjqk){
        this.vcYjqk = vcYjqk;
    }

    public String getVcShxz(){
        return this.vcShxz;
    }
    public void setVcShxz(String vcShxz){
        this.vcShxz = vcShxz;
    }

    public String getVcShxzqt(){
        return this.vcShxzqt;
    }
    public void setVcShxzqt(String vcShxzqt){
        this.vcShxzqt = vcShxzqt;
    }

    public String getVcShbw(){
        return this.vcShbw;
    }
    public void setVcShbw(String vcShbw){
        this.vcShbw = vcShbw;
    }

    public String getVcShbwqt(){
        return this.vcShbwqt;
    }
    public void setVcShbwqt(String vcShbwqt){
        this.vcShbwqt = vcShbwqt;
    }

    public String getVcShljxt(){
        return this.vcShljxt;
    }
    public void setVcShljxt(String vcShljxt){
        this.vcShljxt = vcShljxt;
    }

    public String getVcShljxtqt(){
        return this.vcShljxtqt;
    }
    public void setVcShljxtqt(String vcShljxtqt){
        this.vcShljxtqt = vcShljxtqt;
    }

    public String getVcShyzcd(){
        return this.vcShyzcd;
    }
    public void setVcShyzcd(String vcShyzcd){
        this.vcShyzcd = vcShyzcd;
    }

    public String getVcShlczd(){
        return this.vcShlczd;
    }
    public void setVcShlczd(String vcShlczd){
        this.vcShlczd = vcShlczd;
    }

    public String getVcShjj(){
        return this.vcShjj;
    }
    public void setVcShjj(String vcShjj){
        this.vcShjj = vcShjj;
    }

    public String getVcShjjqt(){
        return this.vcShjjqt;
    }
    public void setVcShjjqt(String vcShjjqt){
        this.vcShjjqt = vcShjjqt;
    }

    public String getVcShsjwpmca(){
        return this.vcShsjwpmca;
    }
    public void setVcShsjwpmca(String vcShsjwpmca){
        this.vcShsjwpmca = vcShsjwpmca;
    }

    public String getVcShsjwpppaXx(){
        return this.vcShsjwpppaXx;
    }
    public void setVcShsjwpppaXx(String vcShsjwpppaXx){
        this.vcShsjwpppaXx = vcShsjwpppaXx;
    }

    public String getVcShsjwpppa(){
        return this.vcShsjwpppa;
    }
    public void setVcShsjwpppa(String vcShsjwpppa){
        this.vcShsjwpppa = vcShsjwpppa;
    }

    public String getVcShsjwpmcb(){
        return this.vcShsjwpmcb;
    }
    public void setVcShsjwpmcb(String vcShsjwpmcb){
        this.vcShsjwpmcb = vcShsjwpmcb;
    }

    public String getVcShsjwpppbXx(){
        return this.vcShsjwpppbXx;
    }
    public void setVcShsjwpppbXx(String vcShsjwpppbXx){
        this.vcShsjwpppbXx = vcShsjwpppbXx;
    }

    public String getVcShsjwpppb(){
        return this.vcShsjwpppb;
    }
    public void setVcShsjwpppb(String vcShsjwpppb){
        this.vcShsjwpppb = vcShsjwpppb;
    }

    public String getVcCpzlsfyg(){
        return this.vcCpzlsfyg;
    }
    public void setVcCpzlsfyg(String vcCpzlsfyg){
        this.vcCpzlsfyg = vcCpzlsfyg;
    }

    public String getVcCpzsjs(){
        return this.vcCpzsjs;
    }
    public void setVcCpzsjs(String vcCpzsjs){
        this.vcCpzsjs = vcCpzsjs;
    }

    public String getVcDxallx(){
        return this.vcDxallx;
    }
    public void setVcDxallx(String vcDxallx){
        this.vcDxallx = vcDxallx;
    }

    public String getVcTbr(){
        return this.vcTbr;
    }
    public void setVcTbr(String vcTbr){
        this.vcTbr = vcTbr;
    }

    public Date getVcTkrq(){
        return this.vcTkrq;
    }
    public void setVcTkrq(Date vcTkrq){
        this.vcTkrq = vcTkrq;
    }

    public Date getVcCjsj(){
        return this.vcCjsj;
    }
    public void setVcCjsj(Date vcCjsj){
        this.vcCjsj = vcCjsj;
    }
}
