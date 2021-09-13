package com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo;

/**
 * 资源池响应类
 * @author zhangtao
 *
 */
public class ResourcePoolResVO<T> {
	
	/**
     * 处理开始
     */
    public String btime;

    /**
     * 处理结束
     */
    public String etime;
    
    /**
          * 处理结果值，0正常，不是0异常
     */
    public String code;

    /**
     * 异常详细信息
	*/
	public String msg;

	/**
	 * data空时为0，类型为JSON Object则为1，类型为JSON Array时是数组的大小
	*/
	public String dataLen;
	
	/**
	 * 业务对象
	*/
	public T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
