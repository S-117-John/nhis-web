package com.zebone.nhis.webservice.support;
/**
 * 第三方接口相应数据结构 
 * @author frank
 *
 */
public class OtherRespJson {
    private String status; //响应状态：(0：成功,-1:业务异常，-2:程序错误) 
    private String desc; //结果描述
    private int total; //总数
    private String errorMessage;//错误信息 
    private String data;//响应结果 
    
    
	public OtherRespJson(){
	}
	
	public OtherRespJson(String response, boolean flag){
		String[] str = response.split("\\|");
		if(str.length>=5){
			this.status=str[0];
			this.desc=str[1];
			this.total = Integer.valueOf(str[2]);
			if(flag){
				setErrorMessage(str[3]);
			}
		    this.data=str[4];
		}
	}
	
	@Override
	public String toString() {
			return "{ \"status\":\"" + status + "\", \"desc\":\"" + desc + "\" , \"total\":\"" + total + "\" ,\"errorMessage\":\"" + errorMessage + "\""
					+ " ,\"data\":" + data + "}";
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
    
    
}