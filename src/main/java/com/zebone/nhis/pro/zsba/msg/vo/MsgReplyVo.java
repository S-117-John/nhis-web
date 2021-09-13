package com.zebone.nhis.pro.zsba.msg.vo;

import java.util.Date;

import com.zebone.nhis.common.module.base.support.CvMsg;

public class MsgReplyVo extends CvMsg {
	private Date dateSend;

	public Date getDateSend() {
		return dateSend;
	}

	public void setDateSend(Date dateSend) {
		this.dateSend = dateSend;
	}
}
