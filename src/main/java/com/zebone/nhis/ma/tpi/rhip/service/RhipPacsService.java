package com.zebone.nhis.ma.tpi.rhip.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.ma.tpi.rhip.dao.RhipMapper;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ma.tpi.rhip.PacsRptInfo;
import com.zebone.nhis.ma.tpi.rhip.dao.PacsRptInfoMapper;
import com.zebone.nhis.ma.tpi.rhip.support.RpDataUtils;
import com.zebone.nhis.ma.tpi.rhip.support.RpWsUtils;
import com.zebone.nhis.ma.tpi.rhip.support.XmlGenUtils;
import com.zebone.nhis.ma.tpi.rhip.vo.PatListVo;
import com.zebone.nhis.ma.tpi.rhip.vo.PtExamReport;
import com.zebone.nhis.ma.tpi.rhip.vo.Response;
import com.zebone.nhis.webservice.client.zhongshan.tpi.rhip.RhipWSInvoke;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 第三方接口-区域卫生平台-检验
 * @author chengjia
 *
 */
@Service
public class RhipPacsService {
	
	@Resource
	private	PacsRptInfoMapper pacsMapper;



    /**
     * 检查数据上传
     * @param param
     * @param user
     * @return
     * @throws Exception 
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public String rpDataTrans(String param , IUser user,PatListVo pat) throws Exception{
    	Map map = JsonUtil.readValue(param,Map.class);
		String pkPv = (String) map.get("pkPv");
	    String codePi=pat.getCodePi();
		map.put("hospitalCardid",codePi);//@todo pk_pi
		//其他数据集
		//检查报告(Pt_ExamReport)@todo
		List<PacsRptInfo> list = pacsMapper.queryPacRptInfo(map);
		if(list!=null&&list.size()>0){
			for (PacsRptInfo rpt : list) {
				PtExamReport ptExamReport=RpDataUtils.createPtExamReport(user, pat, rpt);
				String xml=XmlGenUtils.create(user, pat, ptExamReport);
				String rtnStr=RpWsUtils.invokeWS(xml);
				if(!CommonUtils.isEmpty(rtnStr)){
					return "检查报告:"+rtnStr;
				}else{
					if(rtnStr.indexOf("error")>=0){
						throw new BusException(rtnStr);
					}
				}

			}
		}
		return "";
    }
	
//    //调用webservice接口
//	private void invokeWS(String xml) throws Exception {
//		String rtnStr=RhipWSInvoke.execute(xml);
//		if(rtnStr.isEmpty()){
//			throw new Exception("调用接口返回值为空！");
//		}
//		System.out.println("rtn:"+rtnStr);
//		Response resp=XmlGenUtils.resolveResp(rtnStr);
//		if(resp==null){
//			throw new Exception("取应答为空！");
//		}
//		String code=resp.getCode();
//		if(!code.isEmpty()&&code.equals("200")){
//			//成功
//		}else{
//			String msg="";
//			if(resp.getMessage()!=null){
//				msg="Describe："+resp.getMessage().getDescribe();
//				msg+="\r\n";
//				msg+="EventId："+resp.getMessage().getEventId();
//			}
//			System.out.println("err:"+msg);
//			throw new Exception("调用接口失败："+msg);
//		}
//	}


}
