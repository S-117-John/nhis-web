package com.zebone.nhis.common.module.arch;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;

@Table(value="ARCH_PV")
public class ArchPv extends BaseModule {
    @PK
    @Field(value="PK_ARCHPV")
    private String pkArchpv;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="CODE_IP")
    private String codeIp;

    @Field(value="NAME_PI")
    private String namePi;

    @Field(value="EU_SEX")
    private String euSex;

    @Field(value="BIRTH_DATE")
    private Date birthDate;

    @Field(value="CNT_ARCH")
    private Integer cntArch;

    private String IpTimes;
    
    private String CodeRpt;
    
    private Date dateBegin;
    
    private Date dateEnd;
    public String getPkArchpv() {
        return pkArchpv;
    }

    public void setPkArchpv(String pkArchpv) {
        this.pkArchpv = pkArchpv == null ? null : pkArchpv.trim();
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv == null ? null : pkPv.trim();
    }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp == null ? null : codeIp.trim();
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi == null ? null : namePi.trim();
    }

    public String getEuSex() {
        return euSex;
    }

    public void setEuSex(String euSex) {
        this.euSex = euSex == null ? null : euSex.trim();
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getCntArch() {
        return cntArch;
    }

    public void setCntArch(Integer cntArch) {
        this.cntArch = cntArch;
    }

	public String getIpTimes() {
		return IpTimes;
	}

	public void setIpTimes(String ipTimes) {
		IpTimes = ipTimes;
	}

	public String getCodeRpt() {
		return CodeRpt;
	}

	public void setCodeRpt(String codeRpt) {
		CodeRpt = codeRpt;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	
    
}