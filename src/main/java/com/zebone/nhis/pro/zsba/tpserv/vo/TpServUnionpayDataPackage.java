package com.zebone.nhis.pro.zsba.tpserv.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 杉德对账单汇总记录
 * Table: tp_serv_unionpay_data_package
 *
 * @since 2017-06-26 02:34:48
 */
@Table(value="TP_SERV_UNIONPAY_DATA_PACKAGE")
public class TpServUnionpayDataPackage  {

	/** PK_DATA_BILL_P - 对账编号 */
	@PK
	@Field(value="PK_DATA_BILL_P",id=KeyId.UUID)
    private String pkDataBillP;

	/** BILL_DATE - 账单日期 */
	@Field(value="BILL_DATE",date=FieldType.INSERT)
    private String billDate;

	/** DOWNLOAD_TIME - 下载日期 */
	@Field(value="DOWNLOAD_TIME",date=FieldType.INSERT)
    private String downloadTime;
	
	/** BILL_FILE_NAME - 对账单文件名 */
	@Field(value="BILL_FILE_NAME")
    private String billFileName;
	
	/** BILL_FILE_SIZE - 文件大小 */
	@Field(value="BILL_FILE_SIZE")
    private String billFileSize;

	/** BILL_DATE_BEGIN - 账单开始日期 */
	@Field(value="BILL_DATE_BEGIN",date=FieldType.INSERT)
    private String billDateBegin;

	/** BILL_DATE_END - 账单结算日期 */
	@Field(value="BILL_DATE_END",date=FieldType.INSERT)
    private String billDateEnd;
	
	/** BILL_RECORD - 入表记录数 */
	@Field(value="BILL_RECORD")
    private int billRecord;
	
	
	/** BILL_COUNT - 对账单总记录数 */
	@Field(value="BILL_COUNT")
    private int billCount;
	
	/** BILL_NOTBUZ - 对账单未找到业务单数 */
	@Field(value="BILL_NOTBUZ")
    private int billNotbuz;
	
	/** BILL_NOTCASH - 对账单与业务单现金 */
	@Field(value="BILL_NOTCASH")
    private int billNotcash;

	/** BILL_UPDATEBUZ - 对账单修改业务单为已经支付对应数 */
	@Field(value="BILL_UPDATEBUZ")
    private int billUpdatebuz;
	
	/** BILL_CASH_TOTAL - 对账单现金总收入 */
	@Field(value="BILL_CASH_TOTAL")
    private float billCashTotal;
	
	/** BILL_CASH_REFUND - 对账单现金退款 */
	@Field(value="BILL_CASH_REFUND")
    private float billCashRefund;
	
	/** BUZ_COUNT - 有效业务单总记录数 */
	@Field(value="BUZ_COUNT")
    private int buzCount;
	
	/** BUZ_NOTPROC - 业务单未入账数 */
	@Field(value="BUZ_NOTPROC")
    private int buzNotproc;
	
	/** BUZ_NOTBILL - 业务单未找到对账单数 */
	@Field(value="BUZ_NOTBILL")
    private int buzNotbill;
	
	/** BUZ_CASH_TOTAL - 业务单现金总收入 */
	@Field(value="BUZ_CASH_TOTAL")
    private float buzCashTotal;
	
	/** BUZ_CASH_REFUND - 业务单现金退款 */
	@Field(value="BUZ_CASH_REFUND")
    private float buzCashRefund;
	
	/** BILL_STATUS - 对账状态 */
	@Field(value="BILL_STATUS")
    private int billStatus;
	
	/** BILL_TIME - 对账时间 */
	@Field(value="BILL_TIME",date=FieldType.UPDATE)
    private String billTime;
	
	/** BILL_DESC - 对账描述 */
	@Field(value="BILL_DESC")
    private String billDesc;
	
	/** MERCHANTID - 商户号 */
	@Field(value="MERCHANTID")
    private String merchantid;

	public String getPkDataBillP() {
		return pkDataBillP;
	}

	public void setPkDataBillP(String pkDataBillP) {
		this.pkDataBillP = pkDataBillP;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}

	public String getBillFileName() {
		return billFileName;
	}

	public void setBillFileName(String billFileName) {
		this.billFileName = billFileName;
	}

	public String getBillFileSize() {
		return billFileSize;
	}

	public void setBillFileSize(String billFileSize) {
		this.billFileSize = billFileSize;
	}

	public String getBillDateBegin() {
		return billDateBegin;
	}

	public void setBillDateBegin(String billDateBegin) {
		this.billDateBegin = billDateBegin;
	}

	public String getBillDateEnd() {
		return billDateEnd;
	}

	public void setBillDateEnd(String billDateEnd) {
		this.billDateEnd = billDateEnd;
	}

	public int getBillRecord() {
		return billRecord;
	}

	public void setBillRecord(int billRecord) {
		this.billRecord = billRecord;
	}

	public int getBillCount() {
		return billCount;
	}

	public void setBillCount(int billCount) {
		this.billCount = billCount;
	}

	public int getBillNotbuz() {
		return billNotbuz;
	}

	public void setBillNotbuz(int billNotbuz) {
		this.billNotbuz = billNotbuz;
	}

	public int getBillNotcash() {
		return billNotcash;
	}

	public void setBillNotcash(int billNotcash) {
		this.billNotcash = billNotcash;
	}

	public int getBillUpdatebuz() {
		return billUpdatebuz;
	}

	public void setBillUpdatebuz(int billUpdatebuz) {
		this.billUpdatebuz = billUpdatebuz;
	}

	public float getBillCashTotal() {
		return billCashTotal;
	}

	public void setBillCashTotal(float billCashTotal) {
		this.billCashTotal = billCashTotal;
	}

	public float getBillCashRefund() {
		return billCashRefund;
	}

	public void setBillCashRefund(float billCashRefund) {
		this.billCashRefund = billCashRefund;
	}

	public int getBuzCount() {
		return buzCount;
	}

	public void setBuzCount(int buzCount) {
		this.buzCount = buzCount;
	}

	public int getBuzNotproc() {
		return buzNotproc;
	}

	public void setBuzNotproc(int buzNotproc) {
		this.buzNotproc = buzNotproc;
	}

	public int getBuzNotbill() {
		return buzNotbill;
	}

	public void setBuzNotbill(int buzNotbill) {
		this.buzNotbill = buzNotbill;
	}

	public float getBuzCashTotal() {
		return buzCashTotal;
	}

	public void setBuzCashTotal(float buzCashTotal) {
		this.buzCashTotal = buzCashTotal;
	}

	public float getBuzCashRefund() {
		return buzCashRefund;
	}

	public void setBuzCashRefund(float buzCashRefund) {
		this.buzCashRefund = buzCashRefund;
	}

	public int getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(int billStatus) {
		this.billStatus = billStatus;
	}

	public String getBillTime() {
		return billTime;
	}

	public void setBillTime(String billTime) {
		this.billTime = billTime;
	}

	public String getBillDesc() {
		return billDesc;
	}

	public void setBillDesc(String billDesc) {
		this.billDesc = billDesc;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}
}