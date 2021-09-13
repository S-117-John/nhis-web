package com.zebone.nhis.webservice.support;

public class RespJson {

	private String ret_code;// 状态值，参考异常状态列表  ,大于等于0成功 小于0失败异常
	private String ret_msg;// 描述
	private String datalist;// 业务数据
	private boolean qryOrUpdate; //true 查询 , false 更新 
	
	public RespJson (){};
	
	public RespJson (String response , boolean flag){
		String[] str = response.split("\\|");
		setRet_code(str[0]);
		setRet_msg(str[1]);
		setQryOrUpdate(flag);
		if(flag){
			if(str.length > 2)
				setDatalist(str[2]);
			else 
				setDatalist("\"\"");
		}
	}
	
	@Override
	public String toString() {
		if( this.qryOrUpdate ){
			return "{ \"ret_code\":\"" + ret_code + "\", \"ret_msg\":\"" + ret_msg + "\" ,"
					+ " \"datalist\":" + datalist + "}";
		}else{
			return "{ \"ret_code\":\"" + ret_code + "\", \"ret_msg\":\"" + ret_msg + "\" }";
		}
	}
	
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	
	public String getDatalist() {
		return datalist;
	}
	public void setDatalist(String datalist) {
		this.datalist = datalist;
	}

	public boolean isQryOrUpdate() {
		return qryOrUpdate;
	}

	public void setQryOrUpdate(boolean qryOrUpdate) {
		this.qryOrUpdate = qryOrUpdate;
	}
	
}
