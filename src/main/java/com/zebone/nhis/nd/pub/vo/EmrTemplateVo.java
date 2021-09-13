package com.zebone.nhis.nd.pub.vo;

import java.util.List;
import java.util.Map;

public class EmrTemplateVo {
    private String code;
    private String name;
    private String docXml;
    //模板对应的动态列列表[{id:动态列id,name:动态列名称},...]
    private List<Map<String,Object>> dtlist;
    
	public List<Map<String, Object>> getDtlist() {
		return dtlist;
	}
	public void setDtlist(List<Map<String, Object>> dtlist) {
		this.dtlist = dtlist;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDocXml() {
		return docXml;
	}
	public void setDocXml(String docXml) {
		this.docXml = docXml;
	}
    
    
}
