package com.zebone.nhis.webservice.zsrm.vo.self;

import java.math.BigDecimal;
import java.util.List;

public class RequestYbWillSettleVo extends  CommonReqSelfVo {

	private String codeEmpSt;
	private String nameEmpSt;
	private String codeDeptSt;
	private String nameDeptSt;
	private BigDecimal hpCateBalance;
	
    private List<RequestSelfSettleDtVo>  dataList;

    public List<RequestSelfSettleDtVo> getDataList() {
        return dataList;
    }

    public void setDataList(List<RequestSelfSettleDtVo> dataList) {
        this.dataList = dataList;
    }

	public String getCodeEmpSt() {
		return codeEmpSt;
	}

	public String getNameEmpSt() {
		return nameEmpSt;
	}

	public String getCodeDeptSt() {
		return codeDeptSt;
	}

	public String getNameDeptSt() {
		return nameDeptSt;
	}

	public void setCodeEmpSt(String codeEmpSt) {
		this.codeEmpSt = codeEmpSt;
	}

	public void setNameEmpSt(String nameEmpSt) {
		this.nameEmpSt = nameEmpSt;
	}

	public void setCodeDeptSt(String codeDeptSt) {
		this.codeDeptSt = codeDeptSt;
	}

	public void setNameDeptSt(String nameDeptSt) {
		this.nameDeptSt = nameDeptSt;
	}

	public BigDecimal getHpCateBalance() {
		return hpCateBalance;
	}

	public void setHpCateBalance(BigDecimal hpCateBalance) {
		this.hpCateBalance = hpCateBalance;
	}
    
}
