package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("annotation")
public class Annotation {
	private Text text=new Text();
	private StatusCode statusCode;
	private Author author;
	
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public StatusCode getStatusCode() {
		if(statusCode==null)statusCode=new StatusCode();
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public Author getAuthor() {
		if(author==null)author=new Author();
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	
}
