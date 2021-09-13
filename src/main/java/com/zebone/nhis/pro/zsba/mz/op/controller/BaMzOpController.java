package com.zebone.nhis.pro.zsba.mz.op.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.pi.pub.service.PiCodeFactory;
import com.zebone.nhis.pro.zsba.common.support.JsonResult;
import com.zebone.nhis.pro.zsba.common.support.StringTools;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/static/mz/op")
public class BaMzOpController {

	@Autowired
	private PiCodeFactory piCodeFactory;
	
	@RequestMapping("/getCodeOp")
	public void getCodeOp(HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		String codeOp = "";//piCodeFactory.getHandler().genCodeOp(new PiMaster());
    		String codeIp = "";//piCodeFactory.getHandler().genCodeIp(new PiMaster());//建档不取住院号
    		String codePi = piCodeFactory.getHandler().genCodePi(new PiMaster());
    		
    		System.out.println("门诊ID："+codeOp+", 住院ID："+codeIp+", 编码："+codePi);
    		
    		JSONObject data = new JSONObject();
    		data.put("codeOp", codeOp);
    		data.put("codeIp", codeIp);
    		data.put("codePi", codePi);
    		jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getCodeOp]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
}
