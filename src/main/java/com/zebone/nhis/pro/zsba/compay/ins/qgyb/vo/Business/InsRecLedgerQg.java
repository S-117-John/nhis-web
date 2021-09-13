package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 3202 医药机构费用结算对总账表    下载明细账时才会保存此表
 * @author Administrator
 *
 */
@Table(value="INS_REC_LEDGER_QG")
public class InsRecLedgerQg extends BaseModule{
	
	@PK
	@Field(value="PK_RECLEDGERQG",id=KeyId.UUID)
	private String pkRecledgerqg;//
	
	@Field(value="SETL_OPTINS")
	private String setlOptins;//结算经办机构
	
	@Field(value="FILE_QURY_NO")
	private String fileQuryNo;//文件查询号
	
	@Field(value="STMT_BEGNDATE")
	private String stmtBegndate;//对账开始日期
	
	@Field(value="STMT_ENDDATE")
	private String stmtEnddate;//对账结束日期
	
	@Field(value="MEDFEE_SUMAMT")
	private String medfeeSumamt;//医疗费总额
	
	@Field(value="FUND_PAY_SUMAMT")
	private String fundPaySumamt;//基金支付总额
	
	@Field(value="CASH_PAYAMT")
	private String cashPayamt;//现金支付金额
	
	@Field(value="FIXMEDINS_SETL_CNT")
	private int fixmedinsSetlCnt;//定点医药机构结算笔数
	
	@Field(value="FILE_QURY_NO_FILEINFO")
	private String fileQuryNoFileinfo;//文件查询号 用于下载明细对账结果文件
	
	@Field(value="FILENAME")
	private String filename;//文件名称
	
	@Field(value="DLD_ENDTIME")
	private Date dldEndtime;//下载截止时间
	
	@Field(value="INSUTYPE")
	private String insutype;//险种类型，这个字段先留着，后面不需要再删了
	
	@Field(value="FILE_ADDRESS")
	private String fileAddress;//文件在本地服务器的地址



	public String getPkRecledgerqg() {
		return pkRecledgerqg;
	}

	public void setPkRecledgerqg(String pkRecledgerqg) {
		this.pkRecledgerqg = pkRecledgerqg;
	}

	public String getSetlOptins() {
		return setlOptins;
	}

	public void setSetlOptins(String setlOptins) {
		this.setlOptins = setlOptins;
	}

	public String getFileQuryNo() {
		return fileQuryNo;
	}

	public void setFileQuryNo(String fileQuryNo) {
		this.fileQuryNo = fileQuryNo;
	}

	public String getStmtBegndate() {
		return stmtBegndate;
	}

	public void setStmtBegndate(String stmtBegndate) {
		this.stmtBegndate = stmtBegndate;
	}

	public String getStmtEnddate() {
		return stmtEnddate;
	}

	public void setStmtEnddate(String stmtEnddate) {
		this.stmtEnddate = stmtEnddate;
	}

	public String getMedfeeSumamt() {
		return medfeeSumamt;
	}

	public void setMedfeeSumamt(String medfeeSumamt) {
		this.medfeeSumamt = medfeeSumamt;
	}

	public String getFundPaySumamt() {
		return fundPaySumamt;
	}

	public void setFundPaySumamt(String fundPaySumamt) {
		this.fundPaySumamt = fundPaySumamt;
	}

	public String getCashPayamt() {
		return cashPayamt;
	}

	public void setCashPayamt(String cashPayamt) {
		this.cashPayamt = cashPayamt;
	}

	public int getFixmedinsSetlCnt() {
		return fixmedinsSetlCnt;
	}

	public void setFixmedinsSetlCnt(int fixmedinsSetlCnt) {
		this.fixmedinsSetlCnt = fixmedinsSetlCnt;
	}

	public String getFileQuryNoFileinfo() {
		return fileQuryNoFileinfo;
	}

	public void setFileQuryNoFileinfo(String fileQuryNoFileinfo) {
		this.fileQuryNoFileinfo = fileQuryNoFileinfo;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Date getDldEndtime() {
		return dldEndtime;
	}

	public void setDldEndtime(Date dldEndtime) {
		this.dldEndtime = dldEndtime;
	}

	public String getInsutype() {
		return insutype;
	}

	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}

	public String getFileAddress() {
		return fileAddress;
	}

	public void setFileAddress(String fileAddress) {
		this.fileAddress = fileAddress;
	}
	
	
	
}
