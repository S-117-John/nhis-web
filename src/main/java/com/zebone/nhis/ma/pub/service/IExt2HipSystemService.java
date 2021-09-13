package com.zebone.nhis.ma.pub.service;

import java.util.Map;

public interface IExt2HipSystemService {
	/**
	    * 处理外部系统业务公共方法
	    * @param className
	    * @param method
	    * @param args
	    */
    public Object processExtSystem(String methodName,Map<String,Object> paramMap);
}
