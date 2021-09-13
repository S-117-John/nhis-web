package com.zebone.nhis.task.mz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class testrHandel {
	
	 @Transactional(propagation=Propagation.REQUIRES_NEW)
	public void testa(){
		
		DataBaseHelper.update("insert into test(a,b)values('112rrr','2244')");
		
		
	}

	 
	 public void testaa(){
		 try{
		   testa();
		 }catch(Exception e){}
			DataBaseHelper.update("insert into test(a,b)values('112','2244')");
			
			
		}
}
