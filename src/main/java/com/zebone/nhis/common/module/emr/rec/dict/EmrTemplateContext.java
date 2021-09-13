package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.List;

import com.zebone.nhis.common.module.emr.rec.rec.EmrDeptList;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTemplate;

/***
 * 模板初始化信息时所包含的信息
 * @author fangbo
 *
 */
public class EmrTemplateContext {
	//病历文档分类
	private List<EmrDocType> docTypeList;
	//病历文档段落
	private List<EmrParagraph> paraList;
	//病历数据元素列表
	private List<EmrDataElement> dataEleList;
	//病历参数列表
	private List<EmrParameter> parameterList;
	//病历系统开关
	private List<EmrSwitch> switchList;
	//病历科室列表
	private List<EmrDeptList> deptList;
	//页面样式设置
	private EmrTemplatePageStyle pageStyle;
	//科室模板列表
	private List<EmrTemplate> tmpList;
	//病历元素列表（按类别）
	private List<EmrDataElement> elementListCodeType;
	//当前病历医师设置
	private EmrDoctor emrDoctor;
	//当前病历人员设置
	private EmrEmpSet emrEmpSet;
	
	public List<EmrDocType> getDocTypeList() {
		return this.docTypeList;
	}
	public void setDocTypeList(List<EmrDocType> docTypeList) {
		this.docTypeList = docTypeList;
	}
	public List<EmrParagraph> getParaList() {
		return this.paraList;
	}
	public void setParaList(List<EmrParagraph> list) {
		this.paraList = list;
	}
	public List<EmrDataElement> getDataEleList() {
		return this.dataEleList;
	}
	public void setDataEleList(List<EmrDataElement> dataEleList) {
		this.dataEleList = dataEleList;
	}
	public List<EmrParameter> getParameterList() {
		return this.parameterList;
	}
	public void setParameterList(List<EmrParameter> parameterList) {
		this.parameterList = parameterList;
	}
	public List<EmrSwitch> getSwitchList() {
		return this.switchList;
	}
	public void setSwitchList(List<EmrSwitch> switchList) {
		this.switchList = switchList;
	}
	public List<EmrDeptList> getDeptList() {
		return this.deptList;
	}
	public void setDeptList(List<EmrDeptList> deptList) {
		this.deptList = deptList;
	}
	public EmrTemplatePageStyle getPageStyle() {
		return this.pageStyle;
	}
	public void setPageStyle(EmrTemplatePageStyle pageStyle) {
		this.pageStyle = pageStyle;
	}
	public List<EmrTemplate> getTmpList() {
		return tmpList;
	}
	public void setTmpList(List<EmrTemplate> tmpList) {
		this.tmpList = tmpList;
	}
	public List<EmrDataElement> getElementListCodeType() {
		return elementListCodeType;
	}
	public void setElementListCodeType(List<EmrDataElement> elementListCodeType) {
		this.elementListCodeType = elementListCodeType;
	}
	public EmrDoctor getEmrDoctor() {
		return emrDoctor;
	}
	public void setEmrDoctor(EmrDoctor emrDoctor) {
		this.emrDoctor = emrDoctor;
	}
	public EmrEmpSet getEmrEmpSet() {
		return emrEmpSet;
	}
	public void setEmrEmpSet(EmrEmpSet emrEmpSet) {
		this.emrEmpSet = emrEmpSet;
	}
	
}