package com.zebone.nhis.common.module.compay.ins.shenzhen;


import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: INS_SZYB_CITYCG - ins_szyb_citycg 
 *
 * @since 2020-03-21 09:13:53
 */
@Table(value="INS_SZYB_CITYCG")
public class InsSzybCitycg   {

    /** PK_INSCG - 主键 */
	@PK
	@Field(value="PK_INSCG",id=KeyId.UUID)
    private String pkInscg;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_PV")
    private String pkPv;

    /** PK_CGIP - 主键 */
	@Field(value="PK_CGIP")
    private String pkCgip;

	@Field(value="AKC190")
    private String akc190;

	@Field(value="AAE072")
    private String aae072;

	@Field(value="BKC369")
    private String bkc369;

	@Field(value="BKF500")
    private String bkf500;

	@Field(value="AKA111")
    private String aka111;

	@Field(value="AKE001")
    private String ake001;

	@Field(value="AKE002")
    private String ake002;

	@Field(value="BKM017")
    private String bkm017;

	@Field(value="AKA064")
    private String aka064;

	@Field(value="AKE005")
    private String ake005;

	@Field(value="AKE006")
    private String ake006;

	@Field(value="AKC225")
    private Double akc225;

	@Field(value="AKC226")
    private Double akc226;

	@Field(value="AKC264")
    private Double akc264;

	@Field(value="AKA065")
    private String aka065;

	@Field(value="AKC268")
    private Double akc268;

	@Field(value="CKA319")
    private Double cka319;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;

    @Field(value="YKC610")
    private String ykc610;

    @Field(value="YKA111")
    private String yka111;

    @Field(value="YKA112")
    private String yka112;

    @Field(value="AKE114")
    private String ake114;

    @Field(value="AKA185")
    private String aka185;

    @Field(value="AKA069")
    private Double aka069;

    @Field(value="AKC228")
    private Double akc228;

    @Field(value="AKC253")
    private Double akc253;

    @Field(value="AKC254")
    private Double akc254;

    @Field(value="YKA319")
    private Double yka319;

    @Field(value="YKC615")
    private String ykc615;

    public String getYkc610() {
        return ykc610;
    }

    public void setYkc610(String ykc610) {
        this.ykc610 = ykc610;
    }

    public String getYka111() {
        return yka111;
    }

    public void setYka111(String yka111) {
        this.yka111 = yka111;
    }

    public String getYka112() {
        return yka112;
    }

    public void setYka112(String yka112) {
        this.yka112 = yka112;
    }

    public String getAke114() {
        return ake114;
    }

    public void setAke114(String ake114) {
        this.ake114 = ake114;
    }

    public String getAka185() {
        return aka185;
    }

    public void setAka185(String aka185) {
        this.aka185 = aka185;
    }

    public Double getAka069() {
        return aka069;
    }

    public void setAka069(Double aka069) {
        this.aka069 = aka069;
    }

    public Double getAkc228() {
        return akc228;
    }

    public void setAkc228(Double akc228) {
        this.akc228 = akc228;
    }

    public Double getAkc253() {
        return akc253;
    }

    public void setAkc253(Double akc253) {
        this.akc253 = akc253;
    }

    public Double getAkc254() {
        return akc254;
    }

    public void setAkc254(Double akc254) {
        this.akc254 = akc254;
    }

    public Double getYka319() {
        return yka319;
    }

    public void setYka319(Double yka319) {
        this.yka319 = yka319;
    }

    public String getYkc615() {
        return ykc615;
    }

    public void setYkc615(String ykc615) {
        this.ykc615 = ykc615;
    }

    public String getPkInscg(){
        return this.pkInscg;
    }
    public void setPkInscg(String pkInscg){
        this.pkInscg = pkInscg;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkCgip(){
        return this.pkCgip;
    }
    public void setPkCgip(String pkCgip){
        this.pkCgip = pkCgip;
    }

    public String getAkc190(){
        return this.akc190;
    }
    public void setAkc190(String akc190){
        this.akc190 = akc190;
    }

    public String getAae072(){
        return this.aae072;
    }
    public void setAae072(String aae072){
        this.aae072 = aae072;
    }

    public String getBkc369(){
        return this.bkc369;
    }
    public void setBkc369(String bkc369){
        this.bkc369 = bkc369;
    }

    public String getBkf500(){
        return this.bkf500;
    }
    public void setBkf500(String bkf500){
        this.bkf500 = bkf500;
    }

    public String getAka111(){
        return this.aka111;
    }
    public void setAka111(String aka111){
        this.aka111 = aka111;
    }

    public String getAke001(){
        return this.ake001;
    }
    public void setAke001(String ake001){
        this.ake001 = ake001;
    }

    public String getAke002(){
        return this.ake002;
    }
    public void setAke002(String ake002){
        this.ake002 = ake002;
    }

    public String getBkm017(){
        return this.bkm017;
    }
    public void setBkm017(String bkm017){
        this.bkm017 = bkm017;
    }

    public String getAka064(){
        return this.aka064;
    }
    public void setAka064(String aka064){
        this.aka064 = aka064;
    }

    public String getAke005(){
        return this.ake005;
    }
    public void setAke005(String ake005){
        this.ake005 = ake005;
    }

    public String getAke006(){
        return this.ake006;
    }
    public void setAke006(String ake006){
        this.ake006 = ake006;
    }

    public Double getAkc225(){
        return this.akc225;
    }
    public void setAkc225(Double akc225){
        this.akc225 = akc225;
    }

    public Double getAkc226(){
        return this.akc226;
    }
    public void setAkc226(Double akc226){
        this.akc226 = akc226;
    }

    public Double getAkc264(){
        return this.akc264;
    }
    public void setAkc264(Double akc264){
        this.akc264 = akc264;
    }

    public String getAka065(){
        return this.aka065;
    }
    public void setAka065(String aka065){
        this.aka065 = aka065;
    }

    public Double getAkc268(){
        return this.akc268;
    }
    public void setAkc268(Double akc268){
        this.akc268 = akc268;
    }

    public Double getCka319(){
        return this.cka319;
    }
    public void setCka319(Double cka319){
        this.cka319 = cka319;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}