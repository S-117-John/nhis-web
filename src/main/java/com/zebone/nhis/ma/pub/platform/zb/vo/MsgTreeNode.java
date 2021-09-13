package com.zebone.nhis.ma.pub.platform.zb.vo;

import java.util.List;

public class MsgTreeNode {
	private String id;
	private String label;
	private String permission;
	private List<MsgTreeNode> children;
	private Object tag;
	private Boolean open;
	
	
	public List<MsgTreeNode> getChildren() {
		return children;
	}
	
	public MsgTreeNode(String label) {
		super();
		this.label = label;
	}
	
	public void setChildren(List<MsgTreeNode> children) {
		this.children = children;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Object getTag() {
		return tag;
	}
	public void setTag(Object tag) {
		this.tag = tag;
	}


}
