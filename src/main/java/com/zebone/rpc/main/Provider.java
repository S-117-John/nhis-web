package com.zebone.rpc.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {
	
	public static void main(String[] args) throws Exception{

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { 
				"classpath:/spring/applicationContext.xml",
				"classpath:/spring/applicationContext-amq.xml", 
				"classpath:/spring/applicationContext-mina.xml", 
				"classpath:/spring/applicationContext-dataSource.xml",
				"classpath:/spring/applicationContext-mybatis.xml",
				"classpath:/dubbo/applicationContext-server-base.xml",
				"classpath:/spring/applicationContext-redis.xml"});
		context.start();
//       try{
//		System.in.read();
//       }catch(Exception e){
//    	   
//       }
		
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
