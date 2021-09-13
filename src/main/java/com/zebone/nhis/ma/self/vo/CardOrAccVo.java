package com.zebone.nhis.ma.self.vo;

import java.util.Date;

public class CardOrAccVo {
private String PkPicard;
private String PkPi;
private String DtCardtype;
private String CardNo;
private String DateBegin;
private String DateEnd;
private String FlagActive;
private String EuCardStatus;
private String NamePi;
private String CodePi;
private String PkPiacc;
private String AmtAcc;
private String CreditAcc;
private String EuAccStatus;
private String pkPicate;//患者分类
private String dtSex;//性别编码
private Date birthDate;//出生日期
private String Mobile;
private String IdNo;
public String getMobile() {
	return Mobile;
}
public void setMobile(String mobile) {
	Mobile = mobile;
}

public String getIdNo() {
	return IdNo;
}
public void setIdNo(String idNo) {
	IdNo = idNo;
}
public String getPkPicate() {
	return pkPicate;
}
public void setPkPicate(String pkPicate) {
	this.pkPicate = pkPicate;
}
public String getDtSex() {
	return dtSex;
}
public void setDtSex(String dtSex) {
	this.dtSex = dtSex;
}
public Date getBirthDate() {
	return birthDate;
}
public void setBirthDate(Date birthDate) {
	this.birthDate = birthDate;
}
private CardOrAccVo sqzhxx;
public CardOrAccVo getSqzhxx() {
	return sqzhxx;
}
public void setSqzhxx(CardOrAccVo sqzhxx) {
	this.sqzhxx = sqzhxx;
}
public String getPkPicard() {
	return PkPicard;
}
public void setPkPicard(String pkPicard) {
	PkPicard = pkPicard;
}
public String getPkPi() {
	return PkPi;
}
public void setPkPi(String pkPi) {
	PkPi = pkPi;
}
public String getDtCardtype() {
	return DtCardtype;
}
public void setDtCardtype(String dtCardtype) {
	DtCardtype = dtCardtype;
}
public String getCardNo() {
	return CardNo;
}
public void setCardNo(String cardNo) {
	CardNo = cardNo;
}
public String getDateBegin() {
	return DateBegin;
}
public void setDateBegin(String dateBegin) {
	DateBegin = dateBegin;
}
public String getDateEnd() {
	return DateEnd;
}
public void setDateEnd(String dateEnd) {
	DateEnd = dateEnd;
}
public String getFlagActive() {
	return FlagActive;
}
public void setFlagActive(String flagActive) {
	FlagActive = flagActive;
}
public String getEuCardStatus() {
	return EuCardStatus;
}
public void setEuCardStatus(String euCardStatus) {
	EuCardStatus = euCardStatus;
}
public String getNamePi() {
	return NamePi;
}
public void setNamePi(String namePi) {
	NamePi = namePi;
}
public String getCodePi() {
	return CodePi;
}
public void setCodePi(String codePi) {
	CodePi = codePi;
}
public String getPkPiacc() {
	return PkPiacc;
}
public void setPkPiacc(String pkPiacc) {
	PkPiacc = pkPiacc;
}
public String getAmtAcc() {
	return AmtAcc;
}
public void setAmtAcc(String amtAcc) {
	AmtAcc = amtAcc;
}
public String getCreditAcc() {
	return CreditAcc;
}
public void setCreditAcc(String creditAcc) {
	CreditAcc = creditAcc;
}
public String getEuAccStatus() {
	return EuAccStatus;
}
public void setEuAccStatus(String euAccStatus) {
	EuAccStatus = euAccStatus;
}
}
