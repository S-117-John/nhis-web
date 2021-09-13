package com.zebone.nhis.compay.ins.lb.vo.szyb;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;

public class Settlement {

	    /** DDYLJGBH - DDYLJGBH-定点医疗机构编号 */
		@Field(value="DDYLJGBH")
	    private String ddyljgbh;

	    /** DDYLJGMC - DDYLJGMC-定点医疗机构名称 */
		@Field(value="DDYLJGMC")
	    private String ddyljgmc;

	    /** JYLX - JYLX-交易类型 */
		@Field(value="JYLX")
	    private String jylx;

	    /** DWBH - DWBH-单位编号 */
		@Field(value="DWBH")
	    private String dwbh;

	    /** HZXM - HZXM-患者姓名 */
		@Field(value="HZXM")
	    private String hzxm;

	    /** GRBH - GRBH-个人编号 */
		@Field(value="GRBH")
	    private String grbh;

	    /** SFZH - SFZH-身份证号 */
		@Field(value="SFZH")
	    private String sfzh;

	    /** XB - XB-性别 */
		@Field(value="XB")
	    private String xb;

	    /** CSNY - CSNY-出生年月 */
		@Field(value="CSNY")
	    private String csny;

	    /** NL - NL-年龄 */
		@Field(value="NL")
	    private String nl;

	    /** YLLB - YLLB-医疗类别 */
		@Field(value="YLLB")
	    private String yllb;

	    /** YYDJ - YYDJ-医院等级 */
		@Field(value="YYDJ")
	    private String yydj;

	    /** YLRYLB - YLRYLB-医疗人员类别 */
		@Field(value="YLRYLB")
	    private String ylrylb;

	    /** RYRQ - RYRQ-入院日期 */
		@Field(value="RYRQ")
	    private String ryrq;

	    /** RYZDJBBH - RYZDJBBH-入院诊断疾病编号 */
		@Field(value="RYZDJBBH")
	    private String ryzdjbbh;

	    /** RYZDJBMC - RYZDJBMC-入院诊断疾病名称 */
		@Field(value="RYZDJBMC")
	    private String ryzdjbmc;

	    /** CYRQ - CYRQ-出院日期 */
		@Field(value="CYRQ")
	    private String cyrq;

	    /** CYYY - CYYY-出院原因 */
		@Field(value="CYYY")
	    private String cyyy;

	    /** CYZDJBBH - CYZDJBBH-出院诊断疾病编号 */
		@Field(value="CYZDJBBH")
	    private String cyzdjbbh;

	    /** CYZDJBMC - CYZDJBMC-出院诊断疾病名称 */
		@Field(value="CYZDJBMC")
	    private String cyzdjbmc;

	    /** ZYZT - ZYZT-在院状态 */
		@Field(value="ZYZT")
	    private String zyzt;

	    /** YBQFBZE - YBQFBZE-一般起付标准额 */
		@Field(value="YBQFBZE")
	    private String ybqfbze;

	    /** BXBZ - BXBZ-报销标志 */
		@Field(value="BXBZ")
	    private String bxbz;

	    /** SCTCZFLJ - SCTCZFLJ-上次统筹支付累计 */
		@Field(value="SCTCZFLJ")
	    private String sctczflj;

	    /** SCJZJZFLJ - SCJZJZFLJ-上次救助金支付累计 */
		@Field(value="SCJZJZFLJ")
	    private String scjzjzflj;

	    /** SCGWYZFLJ - SCGWYZFLJ-上次公务员支付累计 */
		@Field(value="SCGWYZFLJ")
	    private String scgwyzflj;

	    /** SCJRTCJBYLFYLJ - SCJRTCJBYLFYLJ-上次进入统筹基本医疗费用累计 */
		@Field(value="SCJRTCJBYLFYLJ")
	    private String scjrtcjbylfylj;

	    /** SCJRJZJJBYLFYLJ - SCJRJZJJBYLFYLJ-上次进入救助金基本医疗费用累计 */
		@Field(value="SCJRJZJJBYLFYLJ")
	    private String scjrjzjjbylfylj;

	    /** SCGRZHZFLJ - SCGRZHZFLJ-上次个人帐户支付累计 */
		@Field(value="SCGRZHZFLJ")
	    private String scgrzhzflj;

	    /** SCGRZHYE - SCGRZHYE-上次个人帐户余额 */
		@Field(value="SCGRZHYE")
	    private String scgrzhye;

	    /** BCJRTCFY - BCJRTCFY-本次进入统筹费用 */
		@Field(value="BCJRTCFY")
	    private String bcjrtcfy;

	    /** BCJRJZJFY - BCJRJZJFY-本次进入救助金费用 */
		@Field(value="BCJRJZJFY")
	    private String bcjrjzjfy;

	    /** BCJRGWYBZJE - BCJRGWYBZJE-本次进入公务员补助金额 */
		@Field(value="BCJRGWYBZJE")
	    private String bcjrgwybzje;

	    /** YLFZE - YLFZE-医疗费总额 */
		@Field(value="YLFZE")
	    private String ylfze;

	    /** FHJBYLFYZE - FHJBYLFYZE-符合基本医疗费用总额 */
		@Field(value="FHJBYLFYZE")
	    private String fhjbylfyze;

	    /** GRZHZF - GRZHZF-个人帐户支付 */
		@Field(value="GRZHZF")
	    private String grzhzf;

	    /** GRXJZF - GRXJZF-个人现金支付 */
		@Field(value="GRXJZF")
	    private String grxjzf;

	    /** TCZFJE - TCZFJE-统筹支付金额 */
		@Field(value="TCZFJE")
	    private String tczfje;

	    /** JZJZFJE - JZJZFJE-救助金支付金额 */
		@Field(value="JZJZFJE")
	    private String jzjzfje;

	    /** GWYBZZFJE - GWYBZZFJE-公务员补助支付金额 */
		@Field(value="GWYBZZFJE")
	    private String gwybzzfje;

	    /** DWTCFDJE - DWTCFDJE-单位统筹分担金额 */
		@Field(value="DWTCFDJE")
	    private String dwtcfdje;

	    /** DDYLJGFD - DDYLJGFD-定点医疗机构分担 */
		@Field(value="DDYLJGFD")
	    private String ddyljgfd;

	    /** JZJBZCRBT - JZJBZCRBT-救助金补助床日补贴 */
		@Field(value="JZJBZCRBT")
	    private String jzjbzcrbt;

	    /** BLZF - BLZF-丙类自费 */
		@Field(value="BLZF")
	    private String blzf;

	    /** YLYPZL - YLYPZL-乙类药品自理 */
		@Field(value="YLYPZL")
	    private String ylypzl;

	    /** YLYPFYZE - YLYPFYZE-乙类药品费用总额 */
		@Field(value="YLYPFYZE")
	    private String ylypfyze;

	    /** TJGRZL - TJGRZL-特检个人自理 */
		@Field(value="TJGRZL")
	    private String tjgrzl;

	    /** TJFYZE - TJFYZE-特检费用总额 */
		@Field(value="TJFYZE")
	    private String tjfyze;

	    /** TZGRZL - TZGRZL-特治个人自理 */
		@Field(value="TZGRZL")
	    private String tzgrzl;

	    /** TZFYZE - TZFYZE-特治费用总额 */
		@Field(value="TZFYZE")
	    private String tzfyze;

	    /** ZZXZFJE - ZZXZFJE-转诊先自付金额 */
		@Field(value="ZZXZFJE")
	    private String zzxzfje;

	    /** CCZLFAZFJE - CCZLFAZFJE-超出治疗方案自付金额 */
		@Field(value="CCZLFAZFJE")
	    private String cczlfazfje;

	    /** QFBZZFJE - QFBZZFJE-起付标准自付金额 */
		@Field(value="QFBZZFJE")
	    private String qfbzzfje;

	    /** QFBZZHZF - QFBZZHZF-起付标准帐户支付 */
		@Field(value="QFBZZHZF")
	    private String qfbzzhzf;

	    /** QFBZXJZF - QFBZXJZF-起付标准现金支付 */
		@Field(value="QFBZXJZF")
	    private String qfbzxjzf;

	    /** TCFDZFJE - TCFDZFJE-统筹分段自付金额 */
		@Field(value="TCFDZFJE")
	    private String tcfdzfje;

	    /** TCFDZFZHZF - TCFDZFZHZF-统筹分段自付帐户支付 */
		@Field(value="TCFDZFZHZF")
	    private String tcfdzfzhzf;

	    /** TCFDZFXJZF - TCFDZFXJZF-统筹分段自付现金支付 */
		@Field(value="TCFDZFXJZF")
	    private String tcfdzfxjzf;

	    /** JZJZFJE1 - JZJZFJE-救助金自付金额 */
		@Field(value="JZJZFJE1")
	    private String jzjzfje1;

	    /** JZJZFZHZF - JZJZFZHZF-救助金自付帐户支付 */
		@Field(value="JZJZFZHZF")
	    private String jzjzfzhzf;

	    /** JZJZFXJZF - JZJZFXJZF-救助金自付现金支付 */
		@Field(value="JZJZFXJZF")
	    private String jzjzfxjzf;

	    /** CGJZJFDXGRZFJE - CGJZJFDXGRZFJE-超过救助金封顶线个人自付金额 */
		@Field(value="CGJZJFDXGRZFJE")
	    private String cgjzjfdxgrzfje;

	    /** CGJZJFDXGRZFZHZF - CGJZJFDXGRZFZHZF-超过救助金封顶线个人自付帐户支付 */
		@Field(value="CGJZJFDXGRZFZHZF")
	    private String cgjzjfdxgrzfzhzf;

	    /** CGJZJFDXGRZFXJZF - CGJZJFDXGRZFXJZF-超过救助金封顶线个人自付现金支付 */
		@Field(value="CGJZJFDXGRZFXJZF")
	    private String cgjzjfdxgrzfxjzf;

	    /** FD1ZF - FD1ZF-分段1自付 */
		@Field(value="FD1ZF")
	    private String fd1zf;

	    /** FD2ZF - FD2ZF-分段2自付 */
		@Field(value="FD2ZF")
	    private String fd2zf;

	    /** FD3ZF - FD3ZF-分段3自付 */
		@Field(value="FD3ZF")
	    private String fd3zf;

	    /** FD4ZF - FD4ZF-分段4自付 */
		@Field(value="FD4ZF")
	    private String fd4zf;

	    /** FD5ZF - FD5ZF-分段5自付 */
		@Field(value="FD5ZF")
	    private String fd5zf;

	    /** FD1JRTCYLFY - FD1JRTCYLFY-分段1进入统筹医疗费用 */
		@Field(value="FD1JRTCYLFY")
	    private String fd1jrtcylfy;

	    /** FD2JRTCYLFY - FD2JRTCYLFY-分段2进入统筹医疗费用 */
		@Field(value="FD2JRTCYLFY")
	    private String fd2jrtcylfy;

	    /** FD3JRTCYLFY - FD3JRTCYLFY-分段3进入统筹医疗费用 */
		@Field(value="FD3JRTCYLFY")
	    private String fd3jrtcylfy;

	    /** FD4JRTCYLFY - FD4JRTCYLFY-分段4进入统筹医疗费用 */
		@Field(value="FD4JRTCYLFY")
	    private String fd4jrtcylfy;

	    /** FD5JRTCYLFY - FD5JRTCYLFY-分段5进入统筹医疗费用 */
		@Field(value="FD5JRTCYLFY")
	    private String fd5jrtcylfy;

	    /** FD1ZFBL - FD1ZFBL-分段1自付比例 */
		@Field(value="FD1ZFBL")
	    private String fd1zfbl;

	    /** FD2ZFBL - FD2ZFBL-分段2自付比例 */
		@Field(value="FD2ZFBL")
	    private String fd2zfbl;

	    /** FD3ZFBL - FD3ZFBL-分段3自付比例 */
		@Field(value="FD3ZFBL")
	    private String fd3zfbl;

	    /** FD4ZFBL - FD4ZFBL-分段4自付比例 */
		@Field(value="FD4ZFBL")
	    private String fd4zfbl;

	    /** FD5ZFBL - FD5ZFBL-分段5自付比例 */
		@Field(value="FD5ZFBL")
	    private String fd5zfbl;

	    /** BNJRTCYLFZE - BNJRTCYLFZE-本年进入统筹医疗费总额 */
		@Field(value="BNJRTCYLFZE")
	    private String bnjrtcylfze;

	    /** BNJRJZJYLFYZE - BNJRJZJYLFYZE-本年进入救助金医疗费用总额 */
		@Field(value="BNJRJZJYLFYZE")
	    private String bnjrjzjylfyze;

	    /** BNYLFLJ - BNYLFLJ-本年医疗费累计 */
		@Field(value="BNYLFLJ")
	    private String bnylflj;

	    /** BNXJZCLJ - BNXJZCLJ-本年现金支出累计 */
		@Field(value="BNXJZCLJ")
	    private String bnxjzclj;

	    /** BNZHZCLJ - BNZHZCLJ-本年帐户支出累计 */
		@Field(value="BNZHZCLJ")
	    private String bnzhzclj;

	    /** BNTCZCLJ - BNTCZCLJ-本年统筹支出累计 */
		@Field(value="BNTCZCLJ")
	    private String bntczclj;

	    /** BNGWYBZZCLJ - BNGWYBZZCLJ-本年公务员补助支出累计 */
		@Field(value="BNGWYBZZCLJ")
	    private String bngwybzzclj;

	    /** BNJZJZCLJ - BNJZJZCLJ-本年救助金支出累计 */
		@Field(value="BNJZJZCLJ")
	    private String bnjzjzclj;

	    /** BNZFLJ - BNZFLJ-本年自费累计 */
		@Field(value="BNZFLJ")
	    private String bnzflj;

	    /** BNYLZLLJ - BNYLZLLJ-本年乙类自理累计 */
		@Field(value="BNYLZLLJ")
	    private String bnylzllj;

	    /** BNQFBZZFLJ - BNQFBZZFLJ-本年起付标准自付累计 */
		@Field(value="BNQFBZZFLJ")
	    private String bnqfbzzflj;

	    /** BNZYCS - BNZYCS-本年住院次数 */
		@Field(value="BNZYCS")
	    private String bnzycs;

	    /** QTFYZE - QTFYZE-其它费用总额 */
		@Field(value="QTFYZE")
	    private String qtfyze;

	    /** QTZLJE - QTZLJE-其它自理金额 */
		@Field(value="QTZLJE")
	    private String qtzlje;

	    /** JKCLZE - JKCLZE-进口材料总额 */
		@Field(value="JKCLZE")
	    private String jkclze;

	    /** JKCLZL - JKCLZL-进口材料自理 */
		@Field(value="JKCLZL")
	    private String jkclzl;

	    /** SCYLFZE - SCYLFZE-上次医疗费总额 */
		@Field(value="SCYLFZE")
	    private String scylfze;

	    /** DWJZJFDJE - DWJZJFDJE-单位救助金分担金额 */
		@Field(value="DWJZJFDJE")
	    private String dwjzjfdje;

	    /** CDH - CDH-存档号 */
		@Field(value="CDH")
	    private String cdh;

	    /** ZHYE - ZHYE-帐户余额 */
		@Field(value="ZHYE")
	    private String zhye;

	    /** GSRDBH - GSRDBH-工伤认定编号 */
		@Field(value="GSRDBH")
	    private String gsrdbh;

	    /** TCXZFJE - TCXZFJE-统筹先自付金额 */
		@Field(value="TCXZFJE")
	    private String tcxzfje;

	    /** JZJXZFJE - JZJXZFJE-救助金先自付金额 */
		@Field(value="JZJXZFJE")
	    private String jzjxzfje;

	    /** SYPGCGRZF - SYPGCGRZF-生育刨宫产个人自付 */
		@Field(value="SYPGCGRZF")
	    private String sypgcgrzf;

	    /** YDZZGRZF - YDZZGRZF-异地转诊个人自付 */
		@Field(value="YDZZGRZF")
	    private String ydzzgrzf;

	    /** SYTES - SYTES-生育胎儿数 */
		@Field(value="SYTES")
	    private String sytes;

	    /** TCQH - TCQH-统筹区号 */
		@Field(value="TCQH")
	    private String tcqh;

	    /** DEBZDDXEBZ - DEBZDDXEBZ-大额补助达到限额标志 */
		@Field(value="DEBZDDXEBZ")
	    private String debzddxebz;

	    /** LQDEBZRQ - LQDEBZRQ-领取大额补助日期 */
		@Field(value="LQDEBZRQ")
	    private String lqdebzrq;

	    /** MODIFIER - 最后操作人 */
		@Field(value="MODIFIER")
	    private String modifier;

	    /** MODIFY_TIME - 最后操作时间 */
		@Field(value="MODIFY_TIME")
	    private Date modifyTime;

		@Field(value="YL1")
	    private String yl1;

		@Field(value="YL2")
	    private String yl2;

		@Field(value="YL3")
	    private String yl3;

		@Field(value="YL4")
	    private String yl4;

		@Field(value="YL5")
	    private String yl5;

		@Field(value="DJH")
	    private String djh;

	    /** JSRQ - 结算日期 */
		@Field(value="JSRQ")
	    private String jsrq;

		public String getDdyljgbh() {
			return ddyljgbh;
		}

		public void setDdyljgbh(String ddyljgbh) {
			this.ddyljgbh = ddyljgbh;
		}

		public String getDdyljgmc() {
			return ddyljgmc;
		}

		public void setDdyljgmc(String ddyljgmc) {
			this.ddyljgmc = ddyljgmc;
		}

		public String getJylx() {
			return jylx;
		}

		public void setJylx(String jylx) {
			this.jylx = jylx;
		}

		public String getDwbh() {
			return dwbh;
		}

		public void setDwbh(String dwbh) {
			this.dwbh = dwbh;
		}

		public String getHzxm() {
			return hzxm;
		}

		public void setHzxm(String hzxm) {
			this.hzxm = hzxm;
		}

		public String getGrbh() {
			return grbh;
		}

		public void setGrbh(String grbh) {
			this.grbh = grbh;
		}

		public String getSfzh() {
			return sfzh;
		}

		public void setSfzh(String sfzh) {
			this.sfzh = sfzh;
		}

		public String getXb() {
			return xb;
		}

		public void setXb(String xb) {
			this.xb = xb;
		}

		public String getCsny() {
			return csny;
		}

		public void setCsny(String csny) {
			this.csny = csny;
		}

		public String getNl() {
			return nl;
		}

		public void setNl(String nl) {
			this.nl = nl;
		}

		public String getYllb() {
			return yllb;
		}

		public void setYllb(String yllb) {
			this.yllb = yllb;
		}

		public String getYydj() {
			return yydj;
		}

		public void setYydj(String yydj) {
			this.yydj = yydj;
		}

		public String getYlrylb() {
			return ylrylb;
		}

		public void setYlrylb(String ylrylb) {
			this.ylrylb = ylrylb;
		}

		public String getRyrq() {
			return ryrq;
		}

		public void setRyrq(String ryrq) {
			this.ryrq = ryrq;
		}

		public String getRyzdjbbh() {
			return ryzdjbbh;
		}

		public void setRyzdjbbh(String ryzdjbbh) {
			this.ryzdjbbh = ryzdjbbh;
		}

		public String getRyzdjbmc() {
			return ryzdjbmc;
		}

		public void setRyzdjbmc(String ryzdjbmc) {
			this.ryzdjbmc = ryzdjbmc;
		}

		public String getCyrq() {
			return cyrq;
		}

		public void setCyrq(String cyrq) {
			this.cyrq = cyrq;
		}

		public String getCyyy() {
			return cyyy;
		}

		public void setCyyy(String cyyy) {
			this.cyyy = cyyy;
		}

		public String getCyzdjbbh() {
			return cyzdjbbh;
		}

		public void setCyzdjbbh(String cyzdjbbh) {
			this.cyzdjbbh = cyzdjbbh;
		}

		public String getCyzdjbmc() {
			return cyzdjbmc;
		}

		public void setCyzdjbmc(String cyzdjbmc) {
			this.cyzdjbmc = cyzdjbmc;
		}

		public String getZyzt() {
			return zyzt;
		}

		public void setZyzt(String zyzt) {
			this.zyzt = zyzt;
		}

		public String getYbqfbze() {
			return ybqfbze;
		}

		public void setYbqfbze(String ybqfbze) {
			this.ybqfbze = ybqfbze;
		}

		public String getBxbz() {
			return bxbz;
		}

		public void setBxbz(String bxbz) {
			this.bxbz = bxbz;
		}

		public String getSctczflj() {
			return sctczflj;
		}

		public void setSctczflj(String sctczflj) {
			this.sctczflj = sctczflj;
		}

		public String getScjzjzflj() {
			return scjzjzflj;
		}

		public void setScjzjzflj(String scjzjzflj) {
			this.scjzjzflj = scjzjzflj;
		}

		public String getScgwyzflj() {
			return scgwyzflj;
		}

		public void setScgwyzflj(String scgwyzflj) {
			this.scgwyzflj = scgwyzflj;
		}

		public String getScjrtcjbylfylj() {
			return scjrtcjbylfylj;
		}

		public void setScjrtcjbylfylj(String scjrtcjbylfylj) {
			this.scjrtcjbylfylj = scjrtcjbylfylj;
		}

		public String getScjrjzjjbylfylj() {
			return scjrjzjjbylfylj;
		}

		public void setScjrjzjjbylfylj(String scjrjzjjbylfylj) {
			this.scjrjzjjbylfylj = scjrjzjjbylfylj;
		}

		public String getScgrzhzflj() {
			return scgrzhzflj;
		}

		public void setScgrzhzflj(String scgrzhzflj) {
			this.scgrzhzflj = scgrzhzflj;
		}

		public String getScgrzhye() {
			return scgrzhye;
		}

		public void setScgrzhye(String scgrzhye) {
			this.scgrzhye = scgrzhye;
		}

		public String getBcjrtcfy() {
			return bcjrtcfy;
		}

		public void setBcjrtcfy(String bcjrtcfy) {
			this.bcjrtcfy = bcjrtcfy;
		}

		public String getBcjrjzjfy() {
			return bcjrjzjfy;
		}

		public void setBcjrjzjfy(String bcjrjzjfy) {
			this.bcjrjzjfy = bcjrjzjfy;
		}

		public String getBcjrgwybzje() {
			return bcjrgwybzje;
		}

		public void setBcjrgwybzje(String bcjrgwybzje) {
			this.bcjrgwybzje = bcjrgwybzje;
		}

		public String getYlfze() {
			return ylfze;
		}

		public void setYlfze(String ylfze) {
			this.ylfze = ylfze;
		}

		public String getFhjbylfyze() {
			return fhjbylfyze;
		}

		public void setFhjbylfyze(String fhjbylfyze) {
			this.fhjbylfyze = fhjbylfyze;
		}

		public String getGrzhzf() {
			return grzhzf;
		}

		public void setGrzhzf(String grzhzf) {
			this.grzhzf = grzhzf;
		}

		public String getGrxjzf() {
			return grxjzf;
		}

		public void setGrxjzf(String grxjzf) {
			this.grxjzf = grxjzf;
		}

		public String getTczfje() {
			return tczfje;
		}

		public void setTczfje(String tczfje) {
			this.tczfje = tczfje;
		}

		public String getJzjzfje() {
			return jzjzfje;
		}

		public void setJzjzfje(String jzjzfje) {
			this.jzjzfje = jzjzfje;
		}

		public String getGwybzzfje() {
			return gwybzzfje;
		}

		public void setGwybzzfje(String gwybzzfje) {
			this.gwybzzfje = gwybzzfje;
		}

		public String getDwtcfdje() {
			return dwtcfdje;
		}

		public void setDwtcfdje(String dwtcfdje) {
			this.dwtcfdje = dwtcfdje;
		}

		public String getDdyljgfd() {
			return ddyljgfd;
		}

		public void setDdyljgfd(String ddyljgfd) {
			this.ddyljgfd = ddyljgfd;
		}

		public String getJzjbzcrbt() {
			return jzjbzcrbt;
		}

		public void setJzjbzcrbt(String jzjbzcrbt) {
			this.jzjbzcrbt = jzjbzcrbt;
		}

		public String getBlzf() {
			return blzf;
		}

		public void setBlzf(String blzf) {
			this.blzf = blzf;
		}

		public String getYlypzl() {
			return ylypzl;
		}

		public void setYlypzl(String ylypzl) {
			this.ylypzl = ylypzl;
		}

		public String getYlypfyze() {
			return ylypfyze;
		}

		public void setYlypfyze(String ylypfyze) {
			this.ylypfyze = ylypfyze;
		}

		public String getTjgrzl() {
			return tjgrzl;
		}

		public void setTjgrzl(String tjgrzl) {
			this.tjgrzl = tjgrzl;
		}

		public String getTjfyze() {
			return tjfyze;
		}

		public void setTjfyze(String tjfyze) {
			this.tjfyze = tjfyze;
		}

		public String getTzgrzl() {
			return tzgrzl;
		}

		public void setTzgrzl(String tzgrzl) {
			this.tzgrzl = tzgrzl;
		}

		public String getTzfyze() {
			return tzfyze;
		}

		public void setTzfyze(String tzfyze) {
			this.tzfyze = tzfyze;
		}

		public String getZzxzfje() {
			return zzxzfje;
		}

		public void setZzxzfje(String zzxzfje) {
			this.zzxzfje = zzxzfje;
		}

		public String getCczlfazfje() {
			return cczlfazfje;
		}

		public void setCczlfazfje(String cczlfazfje) {
			this.cczlfazfje = cczlfazfje;
		}

		public String getQfbzzfje() {
			return qfbzzfje;
		}

		public void setQfbzzfje(String qfbzzfje) {
			this.qfbzzfje = qfbzzfje;
		}

		public String getQfbzzhzf() {
			return qfbzzhzf;
		}

		public void setQfbzzhzf(String qfbzzhzf) {
			this.qfbzzhzf = qfbzzhzf;
		}

		public String getQfbzxjzf() {
			return qfbzxjzf;
		}

		public void setQfbzxjzf(String qfbzxjzf) {
			this.qfbzxjzf = qfbzxjzf;
		}

		public String getTcfdzfje() {
			return tcfdzfje;
		}

		public void setTcfdzfje(String tcfdzfje) {
			this.tcfdzfje = tcfdzfje;
		}

		public String getTcfdzfzhzf() {
			return tcfdzfzhzf;
		}

		public void setTcfdzfzhzf(String tcfdzfzhzf) {
			this.tcfdzfzhzf = tcfdzfzhzf;
		}

		public String getTcfdzfxjzf() {
			return tcfdzfxjzf;
		}

		public void setTcfdzfxjzf(String tcfdzfxjzf) {
			this.tcfdzfxjzf = tcfdzfxjzf;
		}

		public String getJzjzfje1() {
			return jzjzfje1;
		}

		public void setJzjzfje1(String jzjzfje1) {
			this.jzjzfje1 = jzjzfje1;
		}

		public String getJzjzfzhzf() {
			return jzjzfzhzf;
		}

		public void setJzjzfzhzf(String jzjzfzhzf) {
			this.jzjzfzhzf = jzjzfzhzf;
		}

		public String getJzjzfxjzf() {
			return jzjzfxjzf;
		}

		public void setJzjzfxjzf(String jzjzfxjzf) {
			this.jzjzfxjzf = jzjzfxjzf;
		}

		public String getCgjzjfdxgrzfje() {
			return cgjzjfdxgrzfje;
		}

		public void setCgjzjfdxgrzfje(String cgjzjfdxgrzfje) {
			this.cgjzjfdxgrzfje = cgjzjfdxgrzfje;
		}

		public String getCgjzjfdxgrzfzhzf() {
			return cgjzjfdxgrzfzhzf;
		}

		public void setCgjzjfdxgrzfzhzf(String cgjzjfdxgrzfzhzf) {
			this.cgjzjfdxgrzfzhzf = cgjzjfdxgrzfzhzf;
		}

		public String getCgjzjfdxgrzfxjzf() {
			return cgjzjfdxgrzfxjzf;
		}

		public void setCgjzjfdxgrzfxjzf(String cgjzjfdxgrzfxjzf) {
			this.cgjzjfdxgrzfxjzf = cgjzjfdxgrzfxjzf;
		}

		public String getFd1zf() {
			return fd1zf;
		}

		public void setFd1zf(String fd1zf) {
			this.fd1zf = fd1zf;
		}

		public String getFd2zf() {
			return fd2zf;
		}

		public void setFd2zf(String fd2zf) {
			this.fd2zf = fd2zf;
		}

		public String getFd3zf() {
			return fd3zf;
		}

		public void setFd3zf(String fd3zf) {
			this.fd3zf = fd3zf;
		}

		public String getFd4zf() {
			return fd4zf;
		}

		public void setFd4zf(String fd4zf) {
			this.fd4zf = fd4zf;
		}

		public String getFd5zf() {
			return fd5zf;
		}

		public void setFd5zf(String fd5zf) {
			this.fd5zf = fd5zf;
		}

		public String getFd1jrtcylfy() {
			return fd1jrtcylfy;
		}

		public void setFd1jrtcylfy(String fd1jrtcylfy) {
			this.fd1jrtcylfy = fd1jrtcylfy;
		}

		public String getFd2jrtcylfy() {
			return fd2jrtcylfy;
		}

		public void setFd2jrtcylfy(String fd2jrtcylfy) {
			this.fd2jrtcylfy = fd2jrtcylfy;
		}

		public String getFd3jrtcylfy() {
			return fd3jrtcylfy;
		}

		public void setFd3jrtcylfy(String fd3jrtcylfy) {
			this.fd3jrtcylfy = fd3jrtcylfy;
		}

		public String getFd4jrtcylfy() {
			return fd4jrtcylfy;
		}

		public void setFd4jrtcylfy(String fd4jrtcylfy) {
			this.fd4jrtcylfy = fd4jrtcylfy;
		}

		public String getFd5jrtcylfy() {
			return fd5jrtcylfy;
		}

		public void setFd5jrtcylfy(String fd5jrtcylfy) {
			this.fd5jrtcylfy = fd5jrtcylfy;
		}

		public String getFd1zfbl() {
			return fd1zfbl;
		}

		public void setFd1zfbl(String fd1zfbl) {
			this.fd1zfbl = fd1zfbl;
		}

		public String getFd2zfbl() {
			return fd2zfbl;
		}

		public void setFd2zfbl(String fd2zfbl) {
			this.fd2zfbl = fd2zfbl;
		}

		public String getFd3zfbl() {
			return fd3zfbl;
		}

		public void setFd3zfbl(String fd3zfbl) {
			this.fd3zfbl = fd3zfbl;
		}

		public String getFd4zfbl() {
			return fd4zfbl;
		}

		public void setFd4zfbl(String fd4zfbl) {
			this.fd4zfbl = fd4zfbl;
		}

		public String getFd5zfbl() {
			return fd5zfbl;
		}

		public void setFd5zfbl(String fd5zfbl) {
			this.fd5zfbl = fd5zfbl;
		}

		public String getBnjrtcylfze() {
			return bnjrtcylfze;
		}

		public void setBnjrtcylfze(String bnjrtcylfze) {
			this.bnjrtcylfze = bnjrtcylfze;
		}

		public String getBnjrjzjylfyze() {
			return bnjrjzjylfyze;
		}

		public void setBnjrjzjylfyze(String bnjrjzjylfyze) {
			this.bnjrjzjylfyze = bnjrjzjylfyze;
		}

		public String getBnylflj() {
			return bnylflj;
		}

		public void setBnylflj(String bnylflj) {
			this.bnylflj = bnylflj;
		}

		public String getBnxjzclj() {
			return bnxjzclj;
		}

		public void setBnxjzclj(String bnxjzclj) {
			this.bnxjzclj = bnxjzclj;
		}

		public String getBnzhzclj() {
			return bnzhzclj;
		}

		public void setBnzhzclj(String bnzhzclj) {
			this.bnzhzclj = bnzhzclj;
		}

		public String getBntczclj() {
			return bntczclj;
		}

		public void setBntczclj(String bntczclj) {
			this.bntczclj = bntczclj;
		}

		public String getBngwybzzclj() {
			return bngwybzzclj;
		}

		public void setBngwybzzclj(String bngwybzzclj) {
			this.bngwybzzclj = bngwybzzclj;
		}

		public String getBnjzjzclj() {
			return bnjzjzclj;
		}

		public void setBnjzjzclj(String bnjzjzclj) {
			this.bnjzjzclj = bnjzjzclj;
		}

		public String getBnzflj() {
			return bnzflj;
		}

		public void setBnzflj(String bnzflj) {
			this.bnzflj = bnzflj;
		}

		public String getBnylzllj() {
			return bnylzllj;
		}

		public void setBnylzllj(String bnylzllj) {
			this.bnylzllj = bnylzllj;
		}

		public String getBnqfbzzflj() {
			return bnqfbzzflj;
		}

		public void setBnqfbzzflj(String bnqfbzzflj) {
			this.bnqfbzzflj = bnqfbzzflj;
		}

		public String getBnzycs() {
			return bnzycs;
		}

		public void setBnzycs(String bnzycs) {
			this.bnzycs = bnzycs;
		}

		public String getQtfyze() {
			return qtfyze;
		}

		public void setQtfyze(String qtfyze) {
			this.qtfyze = qtfyze;
		}

		public String getQtzlje() {
			return qtzlje;
		}

		public void setQtzlje(String qtzlje) {
			this.qtzlje = qtzlje;
		}

		public String getJkclze() {
			return jkclze;
		}

		public void setJkclze(String jkclze) {
			this.jkclze = jkclze;
		}

		public String getJkclzl() {
			return jkclzl;
		}

		public void setJkclzl(String jkclzl) {
			this.jkclzl = jkclzl;
		}

		public String getScylfze() {
			return scylfze;
		}

		public void setScylfze(String scylfze) {
			this.scylfze = scylfze;
		}

		public String getDwjzjfdje() {
			return dwjzjfdje;
		}

		public void setDwjzjfdje(String dwjzjfdje) {
			this.dwjzjfdje = dwjzjfdje;
		}

		public String getCdh() {
			return cdh;
		}

		public void setCdh(String cdh) {
			this.cdh = cdh;
		}

		public String getZhye() {
			return zhye;
		}

		public void setZhye(String zhye) {
			this.zhye = zhye;
		}

		public String getGsrdbh() {
			return gsrdbh;
		}

		public void setGsrdbh(String gsrdbh) {
			this.gsrdbh = gsrdbh;
		}

		public String getTcxzfje() {
			return tcxzfje;
		}

		public void setTcxzfje(String tcxzfje) {
			this.tcxzfje = tcxzfje;
		}

		public String getJzjxzfje() {
			return jzjxzfje;
		}

		public void setJzjxzfje(String jzjxzfje) {
			this.jzjxzfje = jzjxzfje;
		}

		public String getSypgcgrzf() {
			return sypgcgrzf;
		}

		public void setSypgcgrzf(String sypgcgrzf) {
			this.sypgcgrzf = sypgcgrzf;
		}

		public String getYdzzgrzf() {
			return ydzzgrzf;
		}

		public void setYdzzgrzf(String ydzzgrzf) {
			this.ydzzgrzf = ydzzgrzf;
		}

		public String getSytes() {
			return sytes;
		}

		public void setSytes(String sytes) {
			this.sytes = sytes;
		}

		public String getTcqh() {
			return tcqh;
		}

		public void setTcqh(String tcqh) {
			this.tcqh = tcqh;
		}

		public String getDebzddxebz() {
			return debzddxebz;
		}

		public void setDebzddxebz(String debzddxebz) {
			this.debzddxebz = debzddxebz;
		}

		public String getLqdebzrq() {
			return lqdebzrq;
		}

		public void setLqdebzrq(String lqdebzrq) {
			this.lqdebzrq = lqdebzrq;
		}

		public String getModifier() {
			return modifier;
		}

		public void setModifier(String modifier) {
			this.modifier = modifier;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getYl1() {
			return yl1;
		}

		public void setYl1(String yl1) {
			this.yl1 = yl1;
		}

		public String getYl2() {
			return yl2;
		}

		public void setYl2(String yl2) {
			this.yl2 = yl2;
		}

		public String getYl3() {
			return yl3;
		}

		public void setYl3(String yl3) {
			this.yl3 = yl3;
		}

		public String getYl4() {
			return yl4;
		}

		public void setYl4(String yl4) {
			this.yl4 = yl4;
		}

		public String getYl5() {
			return yl5;
		}

		public void setYl5(String yl5) {
			this.yl5 = yl5;
		}

		public String getDjh() {
			return djh;
		}

		public void setDjh(String djh) {
			this.djh = djh;
		}

		public String getJsrq() {
			return jsrq;
		}

		public void setJsrq(String jsrq) {
			this.jsrq = jsrq;
		}
}
