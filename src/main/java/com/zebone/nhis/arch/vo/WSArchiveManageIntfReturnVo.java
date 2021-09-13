package com.zebone.nhis.arch.vo;

import java.util.List;

/**
 * 归档管理调用其他系统时返回的参数
 * 
 * 2017-5-15 11:06:20
 * 
 * 起在此声明作用
 * @author gongxy
 * 
 */
public class WSArchiveManageIntfReturnVo {

	/**
	 * 文件总数
	 */
	private String fileTotalCount;

	/**
	 * 患者ID
	 */

	private String patId;

	/**
	 * 患者姓名
	 */
	private String patName;

	/**
	 * 类型：住院还是门诊还是体检
	 */
	private String inOrOut;

	/**
	 * 文件列表
	 */

	private List<FileVo> files;

	/**
	 * 系统标识
	 */
	private String codeSys;

	public WSArchiveManageIntfReturnVo(String fileTotalCount, String patId, String patName, String inOrOut, List<FileVo> files, String codeSys) {

		super();
		this.fileTotalCount = fileTotalCount;
		this.patId = patId;
		this.patName = patName;
		this.inOrOut = inOrOut;
		this.files = files;
		this.codeSys = codeSys;
	}

	public WSArchiveManageIntfReturnVo() {

		super();
	}

	public String getFileTotalCount() {

		return fileTotalCount;
	}

	public void setFileTotalCount(String fileTotalCount) {

		this.fileTotalCount = fileTotalCount;
	}

	public String getPatId() {

		return patId;
	}

	public void setPatId(String patId) {

		this.patId = patId;
	}

	public String getPatName() {

		return patName;
	}

	public void setPatName(String patName) {

		this.patName = patName;
	}

	public String getInOrOut() {

		return inOrOut;
	}

	public void setInOrOut(String inOrOut) {

		this.inOrOut = inOrOut;
	}

	public List<FileVo> getFiles() {

		return files;
	}

	public void setFiles(List<FileVo> files) {

		this.files = files;
	}

	public String getCodeSys() {

		return codeSys;
	}

	public void setCodeSys(String codeSys) {

		this.codeSys = codeSys;
	}

}
