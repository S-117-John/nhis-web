package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;

public class EmrHomePageZL {
	   /**
     * 主键
     */
    private String pkZl;
    /**
     * 机构
     */
    private String pkOrg;
    /**
     * 病案首页ID
     */
    private String pkPage;
    /**
     * 重症监护室名称
     */
    private String intensivecareZzjhsmc1;
    /**
     * 重症监护室名称
     */
    private String intensivecareZzjhsmc2;
    /**
     * 重症监护室名称
     */
    private String intensivecareZzjhsmc3;
    /**
     * 重症监护室进入时间
     */
    private Date intensivecareJrzzjhssj1;
    /**
     * 重症监护室进入时间
     */
    private Date intensivecareJrzzjhssj2;
    /**
     * 重症监护室进入时间
     */
    private Date intensivecareJrzzjhssj3;
    /**
     * 重症监护室转出时间
     */
    private Date intensivecareCzzjhssj1;
    /**
     * 重症监护室转出时间
     */
    private Date intensivecareCzzjhssj2;
    /**
     * 重症监护室转出时间
     */
    private Date intensivecareCzzjhssj3;
    /**
     * 危重病例
     */
    private String wzbl;
    /**
     * 疑难病例
     */
    private String ynbl;
    /**
     * MDT病历
     */
    private String mdtbl;
    /**
     * 单病种病例
     */
    private String dbzbl;
    /**
     * 日间手术病例
     */
    private String rjssbl;
    /**
     * 教学查房病例
     */
    private String jxcfbl;
    /**
     * 门诊与出院（诊断符合情况）
     */
    private String zdfhqkMzycy;
    /**
     * 入院与出院（诊断符合情况）
     */
    private String zdfhqkRyycy;
    /**
     * 术前与术后（诊断符合情况）
     */
    private String zdfhqkSqysh;
    /**
     * 临床与病理（诊断符合情况）
     */
    private String zdfhqkLcybl;
    /**
     * 放射与病理（诊断符合情况）
     */
    private String zdfhqkFsybl;
    /**
     * 抢救次数
     */
    private String zdfhqkQjcs;
    /**
     * 抢救成功次数
     */
    private String zdfhqkCgcs;
    /**
     * 同城互认_有无
     */
    private String tchrYw;
    /**
     * 同城互认_影像检查
     */
    private String tchrYxjc;
    /**
     * 同城互认_检验检查
     */
    private String tchrJyjc;
    /**
     * 同城互认_病理检查
     */
    private String tchrBljc;
    /**
     * 同城互认_其他
     */
    private String tchrQt;
    /**
     * 删除标志
     */
    private String delFlag;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date ts;
    
    private String status;

    /**
     * 主键
     */
    public String getPkZl(){
        return this.pkZl;
    }

    /**
     * 主键
     */
    public void setPkZl(String pkZl){
        this.pkZl = pkZl;
    }    
    /**
     * 机构
     */
    public String getPkOrg(){
        return this.pkOrg;
    }

    /**
     * 机构
     */
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    /**
     * 病案首页ID
     */
    public String getPkPage(){
        return this.pkPage;
    }

    /**
     * 病案首页ID
     */
    public void setPkPage(String pkPage){
        this.pkPage = pkPage;
    }    
    /**
     * 重症监护室名称
     */
    public String getIntensivecareZzjhsmc1(){
        return this.intensivecareZzjhsmc1;
    }

    /**
     * 重症监护室名称
     */
    public void setIntensivecareZzjhsmc1(String intensivecareZzjhsmc1){
        this.intensivecareZzjhsmc1 = intensivecareZzjhsmc1;
    }    
    /**
     * 重症监护室名称
     */
    public String getIntensivecareZzjhsmc2(){
        return this.intensivecareZzjhsmc2;
    }

    /**
     * 重症监护室名称
     */
    public void setIntensivecareZzjhsmc2(String intensivecareZzjhsmc2){
        this.intensivecareZzjhsmc2 = intensivecareZzjhsmc2;
    }    
    /**
     * 重症监护室名称
     */
    public String getIntensivecareZzjhsmc3(){
        return this.intensivecareZzjhsmc3;
    }

    /**
     * 重症监护室名称
     */
    public void setIntensivecareZzjhsmc3(String intensivecareZzjhsmc3){
        this.intensivecareZzjhsmc3 = intensivecareZzjhsmc3;
    }    
    /**
     * 重症监护室进入时间
     */
    public Date getIntensivecareJrzzjhssj1(){
        return this.intensivecareJrzzjhssj1;
    }

    /**
     * 重症监护室进入时间
     */
    public void setIntensivecareJrzzjhssj1(Date intensivecareJrzzjhssj1){
        this.intensivecareJrzzjhssj1 = intensivecareJrzzjhssj1;
    }    
    /**
     * 重症监护室进入时间
     */
    public Date getIntensivecareJrzzjhssj2(){
        return this.intensivecareJrzzjhssj2;
    }

    /**
     * 重症监护室进入时间
     */
    public void setIntensivecareJrzzjhssj2(Date intensivecareJrzzjhssj2){
        this.intensivecareJrzzjhssj2 = intensivecareJrzzjhssj2;
    }    
    /**
     * 重症监护室进入时间
     */
    public Date getIntensivecareJrzzjhssj3(){
        return this.intensivecareJrzzjhssj3;
    }

    /**
     * 重症监护室进入时间
     */
    public void setIntensivecareJrzzjhssj3(Date intensivecareJrzzjhssj3){
        this.intensivecareJrzzjhssj3 = intensivecareJrzzjhssj3;
    }    
    /**
     * 重症监护室转出时间
     */
    public Date getIntensivecareCzzjhssj1(){
        return this.intensivecareCzzjhssj1;
    }

    /**
     * 重症监护室转出时间
     */
    public void setIntensivecareCzzjhssj1(Date intensivecareCzzjhssj1){
        this.intensivecareCzzjhssj1 = intensivecareCzzjhssj1;
    }    
    /**
     * 重症监护室转出时间
     */
    public Date getIntensivecareCzzjhssj2(){
        return this.intensivecareCzzjhssj2;
    }

    /**
     * 重症监护室转出时间
     */
    public void setIntensivecareCzzjhssj2(Date intensivecareCzzjhssj2){
        this.intensivecareCzzjhssj2 = intensivecareCzzjhssj2;
    }    
    /**
     * 重症监护室转出时间
     */
    public Date getIntensivecareCzzjhssj3(){
        return this.intensivecareCzzjhssj3;
    }

    /**
     * 重症监护室转出时间
     */
    public void setIntensivecareCzzjhssj3(Date intensivecareCzzjhssj3){
        this.intensivecareCzzjhssj3 = intensivecareCzzjhssj3;
    }    
    /**
     * 危重病例
     */
    public String getWzbl(){
        return this.wzbl;
    }

    /**
     * 危重病例
     */
    public void setWzbl(String wzbl){
        this.wzbl = wzbl;
    }    
    /**
     * 疑难病例
     */
    public String getYnbl(){
        return this.ynbl;
    }

    /**
     * 疑难病例
     */
    public void setYnbl(String ynbl){
        this.ynbl = ynbl;
    }    
    /**
     * MDT病历
     */
    public String getMdtbl(){
        return this.mdtbl;
    }

    /**
     * MDT病历
     */
    public void setMdtbl(String mdtbl){
        this.mdtbl = mdtbl;
    }    
    /**
     * 单病种病例
     */
    public String getDbzbl(){
        return this.dbzbl;
    }

    /**
     * 单病种病例
     */
    public void setDbzbl(String dbzbl){
        this.dbzbl = dbzbl;
    }    
    /**
     * 日间手术病例
     */
    public String getRjssbl(){
        return this.rjssbl;
    }

    /**
     * 日间手术病例
     */
    public void setRjssbl(String rjssbl){
        this.rjssbl = rjssbl;
    }    
    /**
     * 教学查房病例
     */
    public String getJxcfbl(){
        return this.jxcfbl;
    }

    /**
     * 教学查房病例
     */
    public void setJxcfbl(String jxcfbl){
        this.jxcfbl = jxcfbl;
    }    
    /**
     * 门诊与出院（诊断符合情况）
     */
    public String getZdfhqkMzycy(){
        return this.zdfhqkMzycy;
    }

    /**
     * 门诊与出院（诊断符合情况）
     */
    public void setZdfhqkMzycy(String zdfhqkMzycy){
        this.zdfhqkMzycy = zdfhqkMzycy;
    }    
    /**
     * 入院与出院（诊断符合情况）
     */
    public String getZdfhqkRyycy(){
        return this.zdfhqkRyycy;
    }

    /**
     * 入院与出院（诊断符合情况）
     */
    public void setZdfhqkRyycy(String zdfhqkRyycy){
        this.zdfhqkRyycy = zdfhqkRyycy;
    }    
    /**
     * 术前与术后（诊断符合情况）
     */
    public String getZdfhqkSqysh(){
        return this.zdfhqkSqysh;
    }

    /**
     * 术前与术后（诊断符合情况）
     */
    public void setZdfhqkSqysh(String zdfhqkSqysh){
        this.zdfhqkSqysh = zdfhqkSqysh;
    }    
    /**
     * 临床与病理（诊断符合情况）
     */
    public String getZdfhqkLcybl(){
        return this.zdfhqkLcybl;
    }

    /**
     * 临床与病理（诊断符合情况）
     */
    public void setZdfhqkLcybl(String zdfhqkLcybl){
        this.zdfhqkLcybl = zdfhqkLcybl;
    }    
    /**
     * 放射与病理（诊断符合情况）
     */
    public String getZdfhqkFsybl(){
        return this.zdfhqkFsybl;
    }

    /**
     * 放射与病理（诊断符合情况）
     */
    public void setZdfhqkFsybl(String zdfhqkFsybl){
        this.zdfhqkFsybl = zdfhqkFsybl;
    }    
    /**
     * 抢救次数
     */
    public String getZdfhqkQjcs(){
        return this.zdfhqkQjcs;
    }

    /**
     * 抢救次数
     */
    public void setZdfhqkQjcs(String zdfhqkQjcs){
        this.zdfhqkQjcs = zdfhqkQjcs;
    }    
    /**
     * 抢救成功次数
     */
    public String getZdfhqkCgcs(){
        return this.zdfhqkCgcs;
    }

    /**
     * 抢救成功次数
     */
    public void setZdfhqkCgcs(String zdfhqkCgcs){
        this.zdfhqkCgcs = zdfhqkCgcs;
    }    
    /**
     * 同城互认_有无
     */
    public String getTchrYw(){
        return this.tchrYw;
    }

    /**
     * 同城互认_有无
     */
    public void setTchrYw(String tchrYw){
        this.tchrYw = tchrYw;
    }    
    /**
     * 同城互认_影像检查
     */
    public String getTchrYxjc(){
        return this.tchrYxjc;
    }

    /**
     * 同城互认_影像检查
     */
    public void setTchrYxjc(String tchrYxjc){
        this.tchrYxjc = tchrYxjc;
    }    
    /**
     * 同城互认_检验检查
     */
    public String getTchrJyjc(){
        return this.tchrJyjc;
    }

    /**
     * 同城互认_检验检查
     */
    public void setTchrJyjc(String tchrJyjc){
        this.tchrJyjc = tchrJyjc;
    }    
    /**
     * 同城互认_病理检查
     */
    public String getTchrBljc(){
        return this.tchrBljc;
    }

    /**
     * 同城互认_病理检查
     */
    public void setTchrBljc(String tchrBljc){
        this.tchrBljc = tchrBljc;
    }    
    /**
     * 同城互认_其他
     */
    public String getTchrQt(){
        return this.tchrQt;
    }

    /**
     * 同城互认_其他
     */
    public void setTchrQt(String tchrQt){
        this.tchrQt = tchrQt;
    }    
    /**
     * 删除标志
     */
    public String getDelFlag(){
        return this.delFlag;
    }

    /**
     * 删除标志
     */
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }    
    /**
     * 备注
     */
    public String getRemark(){
        return this.remark;
    }

    /**
     * 备注
     */
    public void setRemark(String remark){
        this.remark = remark;
    }    
    /**
     * 创建者
     */
    public String getCreator(){
        return this.creator;
    }

    /**
     * 创建者
     */
    public void setCreator(String creator){
        this.creator = creator;
    }    
    /**
     * 创建时间
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }    
    /**
     * 修改时间
     */
    public Date getTs(){
        return this.ts;
    }

    /**
     * 修改时间
     */
    public void setTs(Date ts){
        this.ts = ts;
    }    

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
