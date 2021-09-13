package com.zebone.nhis.common.module.emr.rec.dict;

/**
 * 纸张格式, 页面边距
 * @author fangbo
 *
 */
public class EmrTemplatePageStyle {

	// 文档字体大小
	private float docFontSize;
	// 字体名称
	private String docFontName;
	// 左边距
	private float pageLeft;
	// 右边距
	private float pageRight ;
	// 上边距
	private float pageTop;
	// 下边距
	private float pageBottom;
	// 打印页面格式, A3, A4
	private int pageFormat;
	// 打印纸张宽度
	private float pageWidth;
	// 打印纸张高度
	private float pageHeight;
	// 纸张方向,横向, 纵向
	private String horOrVer;
	// 页面版式
	private int pageLayOut;
	
	
	public float getDocFontSize() {
		return this.docFontSize;
	}
	public void setDocFontSize(float docFontSize) {
		this.docFontSize = docFontSize;
	}
	public String getDocFontName() {
		return this.docFontName;
	}
	public void setDocFontName(String docFontName) {
		this.docFontName = docFontName;
	}
	public float getPageLeft() {
		return this.pageLeft;
	}
	public void setPageLeft(float pageLeft) {
		this.pageLeft = pageLeft;
	}
	public float getPageRight() {
		return this.pageRight;
	}
	public void setPageRight(float pageRight) {
		this.pageRight = pageRight;
	}
	public float getPageTop() {
		return this.pageTop;
	}
	public void setPageTop(float pageTop) {
		this.pageTop = pageTop;
	}
	public float getPageBottom() {
		return this.pageBottom;
	}
	public void setPageBottom(float pageBottom) {
		this.pageBottom = pageBottom;
	}
	public int getPageFormat() {
		return this.pageFormat;
	}
	public void setPageFormat(int pageFormat) {
		this.pageFormat = pageFormat;
	}
	public float getPageWidth() {
		return this.pageWidth;
	}
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
	}
	public float getPageHeight() {
		return this.pageHeight;
	}
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
	}
	public String getHorOrVer() {
		return this.horOrVer;
	}
	public void setHorOrVer(String horOrVer) {
		this.horOrVer = horOrVer;
	}
	public int getPageLayOut() {
		return this.pageLayOut;
	}
	public void setPageLayOut(int pageLayOut) {
		this.pageLayOut = pageLayOut;
	}
}