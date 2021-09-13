package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("controlActProcess")
public class ControlActProcess {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	private Code code;
	private DataEnterer dataEnterer;
	private Subject subject;
	@XStreamImplicit
	private List<Subject> subjects;
	private QueryByParameter queryByParameter;
	private QueryAck queryAck;
	
	public DataEnterer getDataEnterer() {
		if(dataEnterer==null) {
			dataEnterer = new DataEnterer();
		}		
		return dataEnterer;
	}

	public void setDataEnterer(DataEnterer dataEnterer) {
		this.dataEnterer = dataEnterer;
	}
	
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getMoodCode() {
		return moodCode;
	}

	public void setMoodCode(String moodCode) {
		this.moodCode = moodCode;
	}

	public Subject getSubject() {
		if(subject==null)subject=new Subject();
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public List<Subject> getSubjects() {
		if(subjects==null) subjects=new ArrayList<Subject>();
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public QueryAck getQueryAck() {
		if(queryAck==null) queryAck=new QueryAck();
		return queryAck;
	}

	public void setQueryAck(QueryAck queryAck) {
		this.queryAck = queryAck;
	}

	public QueryByParameter getQueryByParameter() {
		if(queryByParameter==null)queryByParameter=new QueryByParameter();
		return queryByParameter;
	}

	public void setQueryByParameter(QueryByParameter queryByParameter) {
		this.queryByParameter = queryByParameter;
	}

	public Code getCode() {
		if(code==null)code = new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}			
		
}
