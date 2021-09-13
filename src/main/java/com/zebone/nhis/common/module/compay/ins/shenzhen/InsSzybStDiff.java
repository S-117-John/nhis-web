package com.zebone.nhis.common.module.compay.ins.shenzhen;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_ST_DIFF  - ins_szyb_st_diff 
 *
 * @since 2020-02-13 03:11:47
 */
@Table(value="INS_SZYB_ST_DIFF")
public class InsSzybStDiff extends BaseModule  {

    /** PK_INSSTDIFF - 主键 */
	@PK
	@Field(value="PK_INSSTDIFF",id=KeyId.UUID)
    private String pkInsstdiff;

	@Field(value="PK_INSST")
    private String pkInsst;

    /** EU_TREATTYPE - 交易类型 “1”：门诊挂号 “2”：收费  "3" 住院中途结算 “4“ 出院结算 */
	@Field(value="EU_TREATTYPE")
    private String euTreattype;

	@Field(value="AKB021")
    private String akb021;

	@Field(value="AAC002")
    private String aac002;

	@Field(value="AAB301")
    private String aab301;

	@Field(value="AAB299")
    private String aab299;

	@Field(value="AKC264")
    private Double akc264;

	@Field(value="AKC253")
    private Double akc253;

	@Field(value="AKC254")
    private Double akc254;

	@Field(value="YKA319")
    private Double yka319;

	@Field(value="YKC624")
    private Double ykc624;

	@Field(value="AKA151")
    private Double aka151;

	@Field(value="AKB066")
    private Double akb066;

	@Field(value="YKC631")
    private Double ykc631;

	@Field(value="AKB068")
    private Double akb068;

	@Field(value="AKE039")
    private Double ake039;

	@Field(value="YKC627")
    private Double ykc627;

	@Field(value="YKC630")
    private Double ykc630;

	@Field(value="YKC629")
    private Double ykc629;

	@Field(value="AKE035")
    private Double ake035;

	@Field(value="YKC635")
    private Double ykc635;

	@Field(value="YKC636")
    private Double ykc636;

	@Field(value="YKC637")
    private Double ykc637;

	@Field(value="YKC639")
    private Double ykc639;

	@Field(value="AKB063")
    private Integer akb063;

	@Field(value="YKC666")
    private String ykc666;

	@Field(value="AKC200")
    private Integer akc200;

	@Field(value="YKA430")
    private Double yka430;

	@Field(value="YKA431")
    private Double yka431;

	@Field(value="YKA432")
    private Double yka432;

	@Field(value="YKA433")
    private Double yka433;

	@Field(value="YKA434")
    private Double yka434;

	@Field(value="AAE140")
    private String aae140;

	@Field(value="YZZ139")
    private Double yzz139;

	@Field(value="YKC640")
    private Double ykc640;

	@Field(value="YKC751")
    private String ykc751;

	@Field(value="YKC641")
    private Double ykc641;

	@Field(value="YKC642")
    private Double ykc642;

	@Field(value="YKC752")
    private Double ykc752;

	@Field(value="YKC753")
    private Double ykc753;


    public String getPkInsstdiff(){
        return this.pkInsstdiff;
    }
    public void setPkInsstdiff(String pkInsstdiff){
        this.pkInsstdiff = pkInsstdiff;
    }

    public String getPkInsst(){
        return this.pkInsst;
    }
    public void setPkInsst(String pkInsst){
        this.pkInsst = pkInsst;
    }

    public String getEuTreattype(){
        return this.euTreattype;
    }
    public void setEuTreattype(String euTreattype){
        this.euTreattype = euTreattype;
    }

    public String getAkb021(){
        return this.akb021;
    }
    public void setAkb021(String akb021){
        this.akb021 = akb021;
    }

    public String getAac002(){
        return this.aac002;
    }
    public void setAac002(String aac002){
        this.aac002 = aac002;
    }

    public String getAab301(){
        return this.aab301;
    }
    public void setAab301(String aab301){
        this.aab301 = aab301;
    }

    public String getAab299(){
        return this.aab299;
    }
    public void setAab299(String aab299){
        this.aab299 = aab299;
    }

    public Double getAkc264(){
        return this.akc264;
    }
    public void setAkc264(Double akc264){
        this.akc264 = akc264;
    }

    public Double getAkc253(){
        return this.akc253;
    }
    public void setAkc253(Double akc253){
        this.akc253 = akc253;
    }

    public Double getAkc254(){
        return this.akc254;
    }
    public void setAkc254(Double akc254){
        this.akc254 = akc254;
    }

    public Double getYka319(){
        return this.yka319;
    }
    public void setYka319(Double yka319){
        this.yka319 = yka319;
    }

    public Double getYkc624(){
        return this.ykc624;
    }
    public void setYkc624(Double ykc624){
        this.ykc624 = ykc624;
    }

    public Double getAka151(){
        return this.aka151;
    }
    public void setAka151(Double aka151){
        this.aka151 = aka151;
    }

    public Double getAkb066(){
        return this.akb066;
    }
    public void setAkb066(Double akb066){
        this.akb066 = akb066;
    }

    public Double getYkc631(){
        return this.ykc631;
    }
    public void setYkc631(Double ykc631){
        this.ykc631 = ykc631;
    }

    public Double getAkb068(){
        return this.akb068;
    }
    public void setAkb068(Double akb068){
        this.akb068 = akb068;
    }

    public Double getAke039(){
        return this.ake039;
    }
    public void setAke039(Double ake039){
        this.ake039 = ake039;
    }

    public Double getYkc627(){
        return this.ykc627;
    }
    public void setYkc627(Double ykc627){
        this.ykc627 = ykc627;
    }

    public Double getYkc630(){
        return this.ykc630;
    }
    public void setYkc630(Double ykc630){
        this.ykc630 = ykc630;
    }

    public Double getYkc629(){
        return this.ykc629;
    }
    public void setYkc629(Double ykc629){
        this.ykc629 = ykc629;
    }

    public Double getAke035(){
        return this.ake035;
    }
    public void setAke035(Double ake035){
        this.ake035 = ake035;
    }

    public Double getYkc635(){
        return this.ykc635;
    }
    public void setYkc635(Double ykc635){
        this.ykc635 = ykc635;
    }

    public Double getYkc636(){
        return this.ykc636;
    }
    public void setYkc636(Double ykc636){
        this.ykc636 = ykc636;
    }

    public Double getYkc637(){
        return this.ykc637;
    }
    public void setYkc637(Double ykc637){
        this.ykc637 = ykc637;
    }

    public Double getYkc639(){
        return this.ykc639;
    }
    public void setYkc639(Double ykc639){
        this.ykc639 = ykc639;
    }

    public Integer getAkb063(){
        return this.akb063;
    }
    public void setAkb063(Integer akb063){
        this.akb063 = akb063;
    }

    public String getYkc666(){
        return this.ykc666;
    }
    public void setYkc666(String ykc666){
        this.ykc666 = ykc666;
    }

    public Integer getAkc200(){
        return this.akc200;
    }
    public void setAkc200(Integer akc200){
        this.akc200 = akc200;
    }

    public Double getYka430(){
        return this.yka430;
    }
    public void setYka430(Double yka430){
        this.yka430 = yka430;
    }

    public Double getYka431(){
        return this.yka431;
    }
    public void setYka431(Double yka431){
        this.yka431 = yka431;
    }

    public Double getYka432(){
        return this.yka432;
    }
    public void setYka432(Double yka432){
        this.yka432 = yka432;
    }

    public Double getYka433(){
        return this.yka433;
    }
    public void setYka433(Double yka433){
        this.yka433 = yka433;
    }

    public Double getYka434(){
        return this.yka434;
    }
    public void setYka434(Double yka434){
        this.yka434 = yka434;
    }

    public String getAae140(){
        return this.aae140;
    }
    public void setAae140(String aae140){
        this.aae140 = aae140;
    }

    public Double getYzz139(){
        return this.yzz139;
    }
    public void setYzz139(Double yzz139){
        this.yzz139 = yzz139;
    }

    public Double getYkc640(){
        return this.ykc640;
    }
    public void setYkc640(Double ykc640){
        this.ykc640 = ykc640;
    }

    public String getYkc751(){
        return this.ykc751;
    }
    public void setYkc751(String ykc751){
        this.ykc751 = ykc751;
    }

    public Double getYkc641(){
        return this.ykc641;
    }
    public void setYkc641(Double ykc641){
        this.ykc641 = ykc641;
    }

    public Double getYkc642(){
        return this.ykc642;
    }
    public void setYkc642(Double ykc642){
        this.ykc642 = ykc642;
    }

    public Double getYkc752(){
        return this.ykc752;
    }
    public void setYkc752(Double ykc752){
        this.ykc752 = ykc752;
    }

    public Double getYkc753(){
        return this.ykc753;
    }
    public void setYkc753(Double ykc753){
        this.ykc753 = ykc753;
    }
}