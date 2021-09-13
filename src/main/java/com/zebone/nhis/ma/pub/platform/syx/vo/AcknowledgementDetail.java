package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("acknowledgementDetail")
public class AcknowledgementDetail {
	private Text text ;

	public Text getText() {
		if(text==null)text=new Text();
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

}
