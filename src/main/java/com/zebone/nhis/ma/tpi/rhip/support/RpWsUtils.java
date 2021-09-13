package com.zebone.nhis.ma.tpi.rhip.support;


import com.zebone.nhis.ma.tpi.rhip.vo.Response;
import com.zebone.nhis.webservice.client.zhongshan.tpi.rhip.RhipWSInvoke;

/**
 * 区域接口-调用webservice方法工具
 * @author chengjia
 *
 */
public class RpWsUtils {

	//调用webservice接口
	public static String invokeWS(String xml) throws Exception {
		String rtnStr=RhipWSInvoke.execute(xml);
		if(rtnStr.isEmpty()){
			//throw new Exception("调用接口返回值为空！");
			return "调用接口返回值为空！";
		}
		if(rtnStr.indexOf("您没有接入权限，请和管理员联系")>=0){
			//throw new Exception("调用接口失败："+rtnStr);
			return "调用接口失败："+rtnStr;
		}
		//System.out.println("rtn:"+rtnStr);
		Response resp=XmlGenUtils.resolveResp(rtnStr);
		if(resp==null){
			//throw new Exception("取应答为空！");
			return "取应答为空！";
		}
		String code=resp.getCode();
		if(!code.isEmpty()&&code.equals("200")){
			//成功
			return "";
		}else{
			String msg="";
			if(resp.getMessage()!=null){
				msg="Describe："+resp.getMessage().getDescribe();
				msg+="\r\n";
				msg+="EventId："+resp.getMessage().getEventId();
			}
			//System.out.println("err:"+msg);
			//throw new Exception("调用接口失败："+msg);
			return "调用接口失败："+msg;
		}
	}
}
