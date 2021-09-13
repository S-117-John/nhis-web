package com.zebone.rpc.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XaszxmProvider {
	
	public static void main(String[] args) throws Exception{

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { 
				"classpath:/spring/applicationContext.xml", 
				"classpath:/spring/applicationContext-dataSource.xml",
				"classpath:/spring/applicationContext-mybatis.xml",
				"classpath:/dubbo/applicationContext-server-xxaszxm-base.xml",
				"classpath:/spring/applicationContext-redis.xml"});
		context.start();
		
//		System.in.read();
		synchronized (Provider.class) {
			   while (true) {
			    try {
			    	Provider.class.wait();
			    } catch (InterruptedException e) {
			    }
			   }
		  }
	}
}
