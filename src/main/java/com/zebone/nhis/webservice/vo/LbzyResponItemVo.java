package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbzyResponItemVo {
    //对账信息
    @XmlElement(name="TransNO")
    private String transNO;
    @XmlElement(name="PatientNO")
    private String patientNO;
    @XmlElement(name="PatientName")
    private String patientName;
    @XmlElement(name="InvoiceNO")
    private String invoiceNO;
    @XmlElement(name="OutTransNO")
    private String outTransNO;
    @XmlElement(name="BankCardNO")
    private String bankCardNO;
    @XmlElement(name="PosNO")
    private String posNO;
    @XmlElement(name="OperCode")
    private String operCode;
    @XmlElement(name="TransDate")
    private String transDate;
    @XmlElement(name="PayTypeID")
    private String PayTypeID;
    @XmlElement(name="TotalFee")
    private String totalFee;
    @XmlElement(name="YBTC")
    private String ybtc;
    @XmlElement(name="YBZF")
    private String ybzf;
    @XmlElement(name="QKJE")
    private String qkje;
    @XmlElement(name="ZFJE")
    private String zfje;
    @XmlElement(name="BussTypeID")
    private String bussTypeID;
    @XmlElement(name="OpTypeID")
    private String opTypeID;
    @XmlElement(name="OperTypeID")
    private String operTypeID;
    @XmlElement(name="OutTransNoSurce")
    private String outTransNoSurce;
    @XmlElement(name="TerminalTypeID")
    private String terminalTypeID;
    @XmlElement(name="TermianlCode")
    private String termianlCode;
    @XmlElement(name="DepartmentID")
    private String departmentID;
    @XmlElement(name="DepartmentName")
    private String departmentName;
    @XmlElement(name="DoctorID")
    private String doctorID;
    @XmlElement(name="DoctorName")
    private String doctorName;
    @XmlElement(name="BakString")
    private String bakString;

    //就诊卡查询
    //卡号
    @XmlElement(name="CardNO")
    private String CardNO;
    //卡状态	卡状态：0正常 ，1挂失 ，2注销 ，3未注册
    @XmlElement(name="CardStatus")
    private String CardStatus;

    //挂号服务分类
    //类别代码
    @XmlElement(name="TypeID")
    private String typeID;
    //类别名称
    @XmlElement(name="TypeName")
    private String typeName;
    //金额
    @XmlElement(name="Cost")
    private String cost;
    //有效性1表示有效 0表示无效
    @XmlElement(name="ReglevlValid")
    private String reglevlValid;


    //科室编码
    //@XmlElement(name="DepartmentID")
    //private String departmentID;
    //科室名称
    //@XmlElement(name="DepartmentName")
    //private String departmentName;
    //金额	（变更1）
    //@XmlElement(name="Cost")
    //private String cost;
    //是否是终结点	1是，0不是（变更1）
    @XmlElement(name="IsEndPoint")
    private String isEndPoint;
    //是否有医生	1是，0不是（变更1）
    @XmlElement(name="IsChooseDoctor")
    private String isChooseDoctor;

    //医生排班信息响应
    //医生编码
    //@XmlElement(name="DoctorID")
    //private String doctorID;
    //医生姓名
    //@XmlElement(name="DoctorName")
    //private String doctorName;
    //收费类别
    @XmlElement(name="FeeType")
    private String feeType;
    //出诊安排
    @XmlElement(name="ScheduleTime")
    private String scheduleTime;
    //挂号数
    @XmlElement(name="RegCount")
    private String regCount;
    //挂号剩余数
    @XmlElement(name="LimitCount")
    private String limitCount;
    //医生职务
    @XmlElement(name="DoctorLevel")
    private String doctorLevel;
    //总价
    @XmlElement(name="TotalCost")
    private String totalCost;
    //排班编码
    @XmlElement(name="ScheduleCode")
    private String scheduleCode;

    //排班时间段信息
	//班次代码		
    //@XmlElement(name="Code")
    //private String code;
	//班次名称		
    //@XmlElement(name="Name")
    //private String name;
	//开始时间		
    @XmlElement(name="BeginTime")
    private String beginTime;
	//结束时间		
    @XmlElement(name="EndTime")
    private String endTime;
    
    //预约时间段信息
    //班次代码		
    //@XmlElement(name="Code")
    //private String code;
	//班次名称		
    //@XmlElement(name="Name")
    //private String name;
    //时间段		
    @XmlElement(name="TimeRange")
    private String timeRange;
    //限号数		
    //@XmlElement(name="RegLimits")
    //private String RegLimits;
    //时段剩余号数		
    @XmlElement(name="RemainNum")
    private String  remainNum;
    
    //就诊记录信息查询
    //挂号单据号
    @XmlElement(name="TotalCost")
    private String regFlow;
    //患者ID
    @XmlElement(name="TotalCost")
    private String patientID;
    //参保编码
    //@XmlElement(name="TotalCost")
    //private String pactCode;
    //挂号科室编码
    //@XmlElement(name="TotalCost")
    //private String departmentID;
    //挂号科室名称
    //@XmlElement(name="TotalCost")
    //private String departmentName;
    //医生编号
    //@XmlElement(name="TotalCost")
    //private String doctorID;
    //医生姓名
    //@XmlElement(name="TotalCost")
    //private String doctorName;
    //单据号
    @XmlElement(name="TotalCost")
    private String recipeNO;
    //处方号
    //@XmlElement(name="TotalCost")
    //private String recipeSEQ;
    //日期
    @XmlElement(name="TotalCost")
    private String seeTime;
    //总额
    //@XmlElement(name="TotalCost")
    //private String totalCost;

    //预约挂号记录查询
    //科室名称
    //@XmlElement(name="DepartmentName")
    //private String departmentName;
    //排班号
    @XmlElement(name="ScheduleID")
    private String scheduleID;
    //预约日期
    @XmlElement(name="BookDate")
    private String bookDate;
    //上下午
    @XmlElement(name="Noon")
    private String noon;
    //医生姓名
    //@XmlElement(name="DoctorName")
    //private String doctorName;
    //挂号级别
    @XmlElement(name="ReglevelName")
    private String reglevelName;
    //挂号费
    @XmlElement(name="RegCost")
    private String regCost;
    //检查费
    @XmlElement(name="DiagCost")
    private String diagCost;
    //其他费用
    @XmlElement(name="OtherCost")
    private String otherCost;
    //预约的时间段
    @XmlElement(name="ClinicTime")
    private String clinicTime;
    //流水号	取号、取消时用
    //@XmlElement(name="RegFlow")
    //private String regFlow;
    //预约挂号取号用
    @XmlElement(name="MedType")
    private String medType;
    //预约挂号取号用
    @XmlElement(name="DiagCode")
    private String diagCode;
    //预约挂号取号用
    @XmlElement(name="DiagName")
    private String diagName;

    //处方明细
    //处方号
    @XmlElement(name="RecipeSEQ")
    private String recipeSEQ;
    //项目序号
    @XmlElement(name="Num")
    private String num;
    //项目编码
    @XmlElement(name="Code")
    private String code;
    //项目名称
    @XmlElement(name="Name")
    private String name;
    //项目单位
    @XmlElement(name="Unit")
    private String unit;
    //项目规格
    @XmlElement(name="Spec")
    private String spec;
    //单价
    @XmlElement(name="Price")
    private String price;
    //数量
    @XmlElement(name="Quantity")
    private String quantity;
    //金额
    //@XmlElement(name="Cost")
    //private String cost;
    //备注
    @XmlElement(name="Remark")
    private String remark;


    public String getRecipeSEQ() {
        return recipeSEQ;
    }

    public void setRecipeSEQ(String recipeSEQ) {
        this.recipeSEQ = recipeSEQ;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getNoon() {
        return noon;
    }

    public void setNoon(String noon) {
        this.noon = noon;
    }

    public String getReglevelName() {
        return reglevelName;
    }

    public void setReglevelName(String reglevelName) {
        this.reglevelName = reglevelName;
    }

    public String getRegCost() {
        return regCost;
    }

    public void setRegCost(String regCost) {
        this.regCost = regCost;
    }

    public String getDiagCost() {
        return diagCost;
    }

    public void setDiagCost(String diagCost) {
        this.diagCost = diagCost;
    }

    public String getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(String otherCost) {
        this.otherCost = otherCost;
    }

    public String getClinicTime() {
        return clinicTime;
    }

    public void setClinicTime(String clinicTime) {
        this.clinicTime = clinicTime;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getDiagCode() {
        return diagCode;
    }

    public void setDiagCode(String diagCode) {
        this.diagCode = diagCode;
    }

    public String getDiagName() {
        return diagName;
    }

    public void setDiagName(String diagName) {
        this.diagName = diagName;
    }

    public String getTransNO() {
        return transNO;
    }

    public void setTransNO(String transNO) {
        this.transNO = transNO;
    }

    public String getPatientNO() {
        return patientNO;
    }

    public void setPatientNO(String patientNO) {
        this.patientNO = patientNO;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getInvoiceNO() {
        return invoiceNO;
    }

    public void setInvoiceNO(String invoiceNO) {
        this.invoiceNO = invoiceNO;
    }

    public String getOutTransNO() {
        return outTransNO;
    }

    public void setOutTransNO(String outTransNO) {
        this.outTransNO = outTransNO;
    }

    public String getBankCardNO() {
        return bankCardNO;
    }

    public void setBankCardNO(String bankCardNO) {
        this.bankCardNO = bankCardNO;
    }

    public String getPosNO() {
        return posNO;
    }

    public void setPosNO(String posNO) {
        this.posNO = posNO;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getPayTypeID() {
        return PayTypeID;
    }

    public void setPayTypeID(String payTypeID) {
        PayTypeID = payTypeID;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getYbtc() {
        return ybtc;
    }

    public void setYbtc(String ybtc) {
        this.ybtc = ybtc;
    }

    public String getYbzf() {
        return ybzf;
    }

    public void setYbzf(String ybzf) {
        this.ybzf = ybzf;
    }

    public String getQkje() {
        return qkje;
    }

    public void setQkje(String qkje) {
        this.qkje = qkje;
    }

    public String getZfje() {
        return zfje;
    }

    public void setZfje(String zfje) {
        this.zfje = zfje;
    }

    public String getBussTypeID() {
        return bussTypeID;
    }

    public void setBussTypeID(String bussTypeID) {
        this.bussTypeID = bussTypeID;
    }

    public String getOpTypeID() {
        return opTypeID;
    }

    public void setOpTypeID(String opTypeID) {
        this.opTypeID = opTypeID;
    }

    public String getOperTypeID() {
        return operTypeID;
    }

    public void setOperTypeID(String operTypeID) {
        this.operTypeID = operTypeID;
    }

    public String getOutTransNoSurce() {
        return outTransNoSurce;
    }

    public void setOutTransNoSurce(String outTransNoSurce) {
        this.outTransNoSurce = outTransNoSurce;
    }

    public String getTerminalTypeID() {
        return terminalTypeID;
    }

    public void setTerminalTypeID(String terminalTypeID) {
        this.terminalTypeID = terminalTypeID;
    }

    public String getTermianlCode() {
        return termianlCode;
    }

    public void setTermianlCode(String termianlCode) {
        this.termianlCode = termianlCode;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getBakString() {
        return bakString;
    }

    public void setBakString(String bakString) {
        this.bakString = bakString;
    }

    public String getCardNO() {
        return CardNO;
    }

    public void setCardNO(String cardNO) {
        CardNO = cardNO;
    }

    public String getCardStatus() {
        return CardStatus;
    }

    public void setCardStatus(String cardStatus) {
        CardStatus = cardStatus;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getReglevlValid() {
        return reglevlValid;
    }

    public void setReglevlValid(String reglevlValid) {
        this.reglevlValid = reglevlValid;
    }

    public String getIsEndPoint() {
        return isEndPoint;
    }

    public void setIsEndPoint(String isEndPoint) {
        this.isEndPoint = isEndPoint;
    }

    public String getIsChooseDoctor() {
        return isChooseDoctor;
    }

    public void setIsChooseDoctor(String isChooseDoctor) {
        this.isChooseDoctor = isChooseDoctor;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getRegCount() {
        return regCount;
    }

    public void setRegCount(String regCount) {
        this.regCount = regCount;
    }

    public String getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(String limitCount) {
        this.limitCount = limitCount;
    }

    public String getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(String doctorLevel) {
        this.doctorLevel = doctorLevel;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getRegFlow() {
        return regFlow;
    }

    public void setRegFlow(String regFlow) {
        this.regFlow = regFlow;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getRecipeNO() {
        return recipeNO;
    }

    public void setRecipeNO(String recipeNO) {
        this.recipeNO = recipeNO;
    }

    public String getSeeTime() {
        return seeTime;
    }

    public void setSeeTime(String seeTime) {
        this.seeTime = seeTime;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public String getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(String remainNum) {
		this.remainNum = remainNum;
	}
    
}

