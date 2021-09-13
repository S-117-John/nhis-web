package com.zebone.nhis.pro.zsba.compay.up.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 第三方支付对账包
 * @author songs
 * @date 2017-7-19
 */
@Table(value = "PAY_BILL_BACKAGE")
public class PayBillBackage {

	@PK
	@Field(value = "PK_W_BILL_BACKAGE", id = KeyId.UUID)
	private String pkWBillBackage;//对账包主键

	@Field(value = "DATA_PACHAGE_TYPE")
	private String dataPachageType;//数据包类型(1:微信，2：支付，3：银联，4：其他)

	@Field(value = "DOWNLOAD_TIME")
	private Date downloadTime;//下载时间

	@Field(value = "FILE_PATH")
	private String filePath;//文件存放路径

	@Field(value = "FILE_SIZE")
	private Integer fileSize;//文件大小

	@Field(value = "BILL_RECORD")
	private Integer billRecord;//入表记录数

	@Field(value = "BILL_COUNT")
	private Integer billCount;//对帐单总记录数

	@Field(value = "BILL_NOTBUZ")
	private Integer billNotbuz;//对帐单未找到业务单数

	@Field(value = "BILL_NOTCASH")
	private Integer billNotcash;//对账单与业务单现金不对应数

	@Field(value = "BILL_UPDATEBUZ")
	private Integer billUpdatebuz;//对账单修改业务单为已经支付对应数

	@Field(value = "BILL_CASH_TOTAL")
	private BigDecimal billCashTotal;//对账单现金总收入

	@Field(value = "BILL_CASH_REFUND")
	private BigDecimal billCashRefund;//对账单现金退款

	@Field(value = "BUZ_COUNT")
	private Integer buzCount;//有效业务单总记录数

	@Field(value = "BUZ_NOTPROC")
	private Integer buzNotproc;//业务单未入帐数

	@Field(value = "BUZ_NOTBILL")
	private Integer buzNotbill;//业务单未找到对账单数

	@Field(value = "BUZ_CASH_TOTAL")
	private BigDecimal buzCashTotal;//业务单现金总收入

	@Field(value = "BUZ_CASH_REFUND")
	private BigDecimal buzCashRefund;//业务单现金退款
	
	@Field(value = "STATUS")
	private String status;//对账状态(null：未处理，1：数据拆分完成, 2：处理成功，3：处理结果有异常)
	
	@Field(value = "BILL_TIME")
	private Date billTime;//对账完成时间


	
	public String getPkWBillBackage() {
		return pkWBillBackage;
	}

	public void setPkWBillBackage(String pkWBillBackage) {
		this.pkWBillBackage = pkWBillBackage;
	}

	public String getDataPachageType() {
		return dataPachageType;
	}

	public void setDataPachageType(String dataPachageType) {
		this.dataPachageType = dataPachageType;
	}

	public Date getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Date downloadTime) {
		this.downloadTime = downloadTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getBillRecord() {
		return billRecord;
	}

	public void setBillRecord(Integer billRecord) {
		this.billRecord = billRecord;
	}

	public Integer getBillCount() {
		return billCount;
	}

	public void setBillCount(Integer billCount) {
		this.billCount = billCount;
	}

	public Integer getBillNotbuz() {
		return billNotbuz;
	}

	public void setBillNotbuz(Integer billNotbuz) {
		this.billNotbuz = billNotbuz;
	}

	public Integer getBillNotcash() {
		return billNotcash;
	}

	public void setBillNotcash(Integer billNotcash) {
		this.billNotcash = billNotcash;
	}

	public Integer getBillUpdatebuz() {
		return billUpdatebuz;
	}

	public void setBillUpdatebuz(Integer billUpdatebuz) {
		this.billUpdatebuz = billUpdatebuz;
	}

	public BigDecimal getBillCashTotal() {
		return billCashTotal;
	}

	public void setBillCashTotal(BigDecimal billCashTotal) {
		this.billCashTotal = billCashTotal;
	}

	public BigDecimal getBillCashRefund() {
		return billCashRefund;
	}

	public void setBillCashRefund(BigDecimal billCashRefund) {
		this.billCashRefund = billCashRefund;
	}

	public Integer getBuzCount() {
		return buzCount;
	}

	public void setBuzCount(Integer buzCount) {
		this.buzCount = buzCount;
	}

	public Integer getBuzNotproc() {
		return buzNotproc;
	}

	public void setBuzNotproc(Integer buzNotproc) {
		this.buzNotproc = buzNotproc;
	}

	public Integer getBuzNotbill() {
		return buzNotbill;
	}

	public void setBuzNotbill(Integer buzNotbill) {
		this.buzNotbill = buzNotbill;
	}

	public BigDecimal getBuzCashTotal() {
		return buzCashTotal;
	}

	public void setBuzCashTotal(BigDecimal buzCashTotal) {
		this.buzCashTotal = buzCashTotal;
	}

	public BigDecimal getBuzCashRefund() {
		return buzCashRefund;
	}

	public void setBuzCashRefund(BigDecimal buzCashRefund) {
		this.buzCashRefund = buzCashRefund;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBillTime() {
		return billTime;
	}

	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}
}