package com.zebone.nhis.ma.pub.platform.send.impl.syx.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.zebone.nhis.ma.pub.platform.syx.support.HIPMessageServerUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;

public class ThreadChkSend implements Callable<Map<String,Object>>{

	private Request request;
	
	private String action;
	
	private String resHead;
	
	private boolean flagSave;
	
	
	public ThreadChkSend(Request request,String action,String resHead,boolean flagSave){
		
		this.request = request;
		this.action = action;
		this.resHead = resHead;
		this.flagSave = flagSave;
		
	}
	
	@Override
	public Map<String, Object> call() throws Exception {
		HIPMessageServerUtils hipMessageServerUtils = new HIPMessageServerUtils();
		hipMessageServerUtils.sendHIPMsg(request, action, resHead, flagSave);
		Map<String,Object> res =  new HashMap<String,Object>();
		res.put("res", resHead);
		return res;
	}
	
	
	

}
