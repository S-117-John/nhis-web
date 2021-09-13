package com.zebone.nhis.webservice.syx.cxf;

import java.util.Date;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.service.ScmHrService;
import com.zebone.nhis.webservice.syx.vo.scmhr.Request;
import com.zebone.nhis.webservice.syx.vo.scmhr.RequestDataVo;
import com.zebone.nhis.webservice.syx.vo.scmhr.ResponseDataVo;
import com.zebone.nhis.webservice.syx.vo.scmhr.Response;

/**
 * 孙逸仙医院门诊华润包药机接口实现处理
 * @author jd
 *
 */
@WebService
@SOAPBinding(style = Style.RPC)
public class ScmHrWebServiceImpl implements IScmHrWebService {
	
	private Logger logger = LoggerFactory.getLogger("syx.hrSyxConsis");
	
	@Resource
	private ScmHrService scmHrService;
	
	@Override
	public String scmHrInterfaceXml(String param) {
		logger.info("\n*********************************************************************************************************************************************");
		logger.info("====================华润包药机接口调用开始==============================================");
		Date dateBegin=new Date();
		ResponseDataVo resData=new ResponseDataVo();
		String resXml="";
		try {
			RequestDataVo reqData=(RequestDataVo) XmlUtil.XmlToBean(param, RequestDataVo.class);
			if(reqData!=null){
				String funId=reqData.getOptype();//获取功能号
				logger.info("======================华润包药机接口：功能号【"+funId+"】,请求XML:\n"+param);
				switch (funId) {
				case "300"://开始配药接口
					resData=scmHrService.drugPreqStart(reqData, dateBegin);
					break;
				case "301"://处方完成配药
					resData=scmHrService.drugPreqFinish(reqData, dateBegin);
					break;
				case "302"://处方取消配药
					resData=scmHrService.drugPreqCancel(reqData, dateBegin);
					break;
				case "303"://处方重新分配窗口信息
				    resData=scmHrService.drugResetWinNo(reqData, dateBegin);
				    break;
				case "311"://窗口状态修改
					resData=scmHrService.WinStatusMsg(reqData, dateBegin);
				    break;
				default:
					break;
				}
			}else{
				resData.setRetcode("0");
				resData.setRetmsg("调用接口失败，未获得请求数据！");
				resData.setRetval("0");
			}
			resXml = XmlUtil.beanToXml(resData, ResponseDataVo.class);
		} catch (Exception e) {
			logger.info("================华润包药机接口服务【华润】发生异常：时间【"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"】异常信息【"+e.getMessage()+"】，请求xml;【"+param+"】===================");
		}finally{
			logger.info("================华润包药机接口调用结束：响应XML:\n"+resXml);
		}
		return resXml;
	}

	@Override
	public String scmPtInterfaceXml(String param) {
		logger.info("====================华润包药机接口调用开始==============================================");
		Date dateBegin=new Date();
		Response response=new Response();
		String resXml="";
		try {
			Request req=(Request) XmlUtil.XmlToBean(param, Request.class);
			if(req!=null){
				String funId=req.getActionId();
				logger.info("======================华润包药机接口：功能号【"+funId+"】,请求XML:\n"+param);
				switch (funId) {
				case "PharmacyDrugSpaceQuery"://药房药品货位查询服务
					response=scmHrService.qryDrugSpacePosi(req, dateBegin);
					break;
				case "DispensingWindowBasicDataQuery"://药房窗口查询服务
					response=scmHrService.qryDrugWindows(req, dateBegin);
					break;
				case "DrugDictQuery"://药品字典查询服务
					response=scmHrService.qryDrugDict(req, dateBegin);
					break;
				case "PrescriptionQuery"://处方查询服务
					response=scmHrService.drugRegPres(req, dateBegin);
					break;
				case "PrescriptionTakeMedicineWindow"://分配窗口服务
					response=scmHrService.drugDoWinno(req, dateBegin);
					break;
				default:
					break;
				}
			}
			resXml = XmlUtil.beanToXml(response, Response.class);
		} catch (Exception e) {
			logger.info("================华润包药机接口服务【平台】发生异常：时间【"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"】异常信息【"+e.getMessage()+"】，请求xml;【"+param+"】===================");
		}finally{
			logger.info("================华润包药机接口调用结束：响应XML:\n"+resXml);
			logger.info("*********************************************************************************************************************************************");
		}
		return resXml;
	}

}
