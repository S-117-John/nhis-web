package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("queryAck")
public class QueryAck {
	private QueryId queryId;
	private QueryResponseCode queryResponseCode;

	public QueryId getQueryId() {
		return queryId;
	}

	public void setQueryId(QueryId queryId) {
		this.queryId = queryId;
	}

	public QueryResponseCode getQueryResponseCode() {
		if(queryResponseCode==null) queryResponseCode=new QueryResponseCode();
		return queryResponseCode;
	}

	public void setQueryResponseCode(QueryResponseCode queryResponseCode) {
		this.queryResponseCode = queryResponseCode;
	}
	
}
