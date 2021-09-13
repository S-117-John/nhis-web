package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * PRPA_IN201311UV02 标签中的<interactionId> 内容对象
 * 
 * @author yx
 * 
 */
@XStreamAlias("interactionId")
public class InteractionId {
	@XStreamAsAttribute
	private String root;
	@XStreamAsAttribute
	private String extension;

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
