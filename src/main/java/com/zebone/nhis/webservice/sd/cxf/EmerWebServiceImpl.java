package com.zebone.nhis.webservice.sd.cxf;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.webservice.sd.service.EmerService;
import com.zebone.nhis.webservice.sd.vo.OrderInfo;
import com.zebone.nhis.webservice.vo.ResponseMasterVo;
import com.zebone.platform.modules.utils.JsonUtil;

@WebService
@SOAPBinding(style = Style.RPC)
public class EmerWebServiceImpl implements IEmerWebService {

	private Logger logger = LoggerFactory.getLogger("com.zebone");

	@Resource
	private EmerService emerService;

	@Override
	public String saveOpOrderInfo(String param) {

		ResponseMasterVo ResponseMasterVo = new ResponseMasterVo();
		try {
			saveOpOrder(param);
		} catch (Exception e) {

			ResponseMasterVo.setStatus("-2");
			ResponseMasterVo.setErrorMessage(e.getMessage());
			ResponseMasterVo.setDesc("操作失败!");
			logger.error("急诊调用保存医嘱开始接口结束");
			return ApplicationUtils.objectToJson(ResponseMasterVo);

		}
		ResponseMasterVo.setStatus("0");
		ResponseMasterVo.setDesc("操作成功");
		logger.info("急诊调用保存医嘱开始接口结束");

		return ApplicationUtils.objectToJson(ResponseMasterVo);
	}

	private void saveOpOrder(String param) {
		logger.info("急诊调用保存医嘱开始 ");
		logger.info("急诊调用入参 param:" + param);
		OrderInfo info = JsonUtil.readValue(param, OrderInfo.class);
		emerService.saveOrders(info);
	}
}
