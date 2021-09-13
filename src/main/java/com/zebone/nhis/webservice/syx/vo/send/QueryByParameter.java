package com.zebone.nhis.webservice.syx.vo.send;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("queryByParameter")
public class QueryByParameter {
	private QueryId queryId;
	private StatusCode statusCode;
	private InitialQuantity initialQuantity;
	private MatchCriterionList matchCriterionList;
	private ParameterList parameterList;
	private ValueSet valueSet;
	public QueryId getQueryId() {
		if(queryId==null)queryId=new QueryId();
		return queryId;
	}

	public void setQueryId(QueryId queryId) {
		this.queryId = queryId;
	}

	public StatusCode getStatusCode() {
		if(statusCode==null)statusCode=new StatusCode();
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public InitialQuantity getInitialQuantity() {
		if(initialQuantity==null) initialQuantity=new InitialQuantity();
		return initialQuantity;
	}

	public void setInitialQuantity(InitialQuantity initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	public MatchCriterionList getMatchCriterionList() {
		if (matchCriterionList==null)matchCriterionList=new MatchCriterionList(); 
		return matchCriterionList;
	}

	public void setMatchCriterionList(MatchCriterionList matchCriterionList) {
		this.matchCriterionList = matchCriterionList;
	}

	public ParameterList getParameterList() {
		if(parameterList==null)parameterList=new ParameterList();
		return parameterList;
	}

	public void setParameterList(ParameterList parameterList) {
		this.parameterList = parameterList;
	}

	public ValueSet getValueSet() {
		if(valueSet==null)valueSet=new ValueSet();
		return valueSet;
	}

	public void setValueSet(ValueSet valueSet) {
		this.valueSet = valueSet;
	}
	
	
}
