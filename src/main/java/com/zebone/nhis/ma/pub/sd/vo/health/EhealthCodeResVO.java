package com.zebone.nhis.ma.pub.sd.vo.health;

/**
 * 电子健康码响应公用参数
 * @author zhangtao
 *
 */
public class EhealthCodeResVO {
	
	/**
     * 接收请求时间
     */
    public String btime;

    /**
     * 交易返回时间
     */
    public String etime;

    /**
     * 交易处理结果，定义为“0：成功，其他：失败”
     */
    public String code;

    /**
     * 失败信息描述
     */
    public String msg;

    /**
     * 交易返回数据的类型，定义为：0-返回数据（data）为空；1-返回数据（data）为JSON Object类型；>1-返回数据（data）为JSON Array类型；数值为Array数组的大小
     */
    public String dataLen;
    
    /**
     * 业务对象
     */
    public Object data;

	public String getBtime() {
		return btime;
	}

	public void setBtime(String btime) {
		this.btime = btime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDataLen() {
		return dataLen;
	}

	public void setDataLen(String dataLen) {
		this.dataLen = dataLen;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
}
