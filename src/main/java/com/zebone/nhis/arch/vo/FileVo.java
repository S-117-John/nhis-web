package com.zebone.nhis.arch.vo;

public class FileVo {

	/**
	 * 病历文档各个系统内部唯一编码
	 */
	private String code;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 报告名称
	 */
	private String name;

	/**
	 * Y:已经归档；N待归档
	 */
	private String arcYN;

	/**
	 * 报告单时间
	 */
	private String repTime;

	/**
	 * 报告的其他一些描述
	 */
	private String desc;

	public FileVo() {

		super();
		// TODO Auto-generated constructor stub
	}

	public FileVo(String code, String fileName, String name, String arcYN, String repTime, String desc) {

		super();
		this.code = code;
		this.fileName = fileName;
		this.name = name;
		this.arcYN = arcYN;
		this.repTime = repTime;
		this.desc = desc;
	}

	public String getCode() {

		return code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	public String getFileName() {

		return fileName;
	}

	public void setFileName(String fileName) {

		this.fileName = fileName;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getArcYN() {

		return arcYN;
	}

	public void setArcYN(String arcYN) {

		this.arcYN = arcYN;
	}

	public String getRepTime() {

		return repTime;
	}

	public void setRepTime(String repTime) {

		this.repTime = repTime;
	}

	public String getDesc() {

		return desc;
	}

	public void setDesc(String desc) {

		this.desc = desc;
	}

}
