package com.zebone.nhis.ma.pub.zsrm.vo;


import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.zsrm.support.ZsrmMsgUtils;
import com.zebone.nhis.pro.zsba.common.support.MD5Util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class HeadVo {
	@XmlElement(name = "Head")
	private Head head;

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	/*public static void main(String[] args) {
		String qq = "ok你是谁你在哪里啊";
		//sign = ZsrmMsgUtils.encodeByMD5(qq);
		System.out.println(MD5Util.MD5Encode(qq.toString(),"UTF-8"));
	}*/
}
