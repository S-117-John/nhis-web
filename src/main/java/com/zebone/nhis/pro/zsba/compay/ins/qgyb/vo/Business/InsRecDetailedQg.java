package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 3202 医药机构费用结算对明细账表
 * @author Administrator
 *
 */
@Table(value="INS_REC_DETAILED_QG")
public class InsRecDetailedQg extends BaseModule{
	
	@PK
	@Field(value="PK_RECDETAILEDQG",id=KeyId.UUID)
	private String pkRecdetailedqg;//
	
	@Field(value="PK_RECLEDGERQG")
	private String pkRecledgerqg;//
	
	@Field(value="PSN_NO")
	private String psnNo;//人员编号
	
	@Field(value="MDTRT_ID")
	private String mdtrtId;//就诊ID
	
	@Field(value="SETL_ID")
	private String setlId;//结算ID
	
	@Field(value="MSG_ID")
	private String msgId;//发送方报文ID
	
	@Field(value="STMT_RSLT")
	private String stmtRslt;//对账结果
	
	@Field(value="REFD_SETL_FLAG")
	private String refdSetlFlag;//退费结算标志
	
	@Field(value="MEMO")
	private String memo;//备注
	
	@Field(value="HICENT_MEDFEE_SUMAMT")
	private String hicentMedfeeSumamt;//医保中心医疗费总额
	
	@Field(value="HIF_PAY_SUMAMT")
	private String hifPaySumamt;//医保基金支付总额
	
	@Field(value="HICENT_ACCT_PAY")
	private String hicentAcctPay;//医保中心个人账户支出

	public String getPkRecdetailedqg() {
		return pkRecdetailedqg;
	}

	public void setPkRecdetailedqg(String pkRecdetailedqg) {
		this.pkRecdetailedqg = pkRecdetailedqg;
	}

	public String getPkRecledgerqg() {
		return pkRecledgerqg;
	}

	public void setPkRecledgerqg(String pkRecledgerqg) {
		this.pkRecledgerqg = pkRecledgerqg;
	}

	public String getPsnNo() {
		return psnNo;
	}

	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}

	public String getMdtrtId() {
		return mdtrtId;
	}

	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}

	public String getSetlId() {
		return setlId;
	}

	public void setSetlId(String setlId) {
		this.setlId = setlId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getStmtRslt() {
		return stmtRslt;
	}

	public void setStmtRslt(String stmtRslt) {
		this.stmtRslt = stmtRslt;
	}

	public String getRefdSetlFlag() {
		return refdSetlFlag;
	}

	public void setRefdSetlFlag(String refdSetlFlag) {
		this.refdSetlFlag = refdSetlFlag;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getHicentMedfeeSumamt() {
		return hicentMedfeeSumamt;
	}

	public void setHicentMedfeeSumamt(String hicentMedfeeSumamt) {
		this.hicentMedfeeSumamt = hicentMedfeeSumamt;
	}

	public String getHifPaySumamt() {
		return hifPaySumamt;
	}

	public void setHifPaySumamt(String hifPaySumamt) {
		this.hifPaySumamt = hifPaySumamt;
	}

	public String getHicentAcctPay() {
		return hicentAcctPay;
	}

	public void setHicentAcctPay(String hicentAcctPay) {
		this.hicentAcctPay = hicentAcctPay;
	}

	
	
}
