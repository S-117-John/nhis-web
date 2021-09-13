package com.zebone.nhis.ma.pub.service;


/**
 * 外部系统对接接口（各项目添加对应实现）
 * @author yangxue
 *
 */
public interface IExtSystemService {
	   /**
	    * 处理外部系统业务公共方法
	    * @param className
	    * @param method
	    * @param args
	    */
       public Object processExtSystem(String systemName,String methodName,Object...args);
}
