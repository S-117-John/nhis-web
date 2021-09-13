package com.zebone.nhis.ma.pub.zsba.support;

import com.zebone.nhis.ma.pub.zsba.vo.outflow.AuditResult;
import com.zebone.nhis.ma.pub.zsba.vo.outflow.CommonResponse;
import com.zebone.nhis.ma.pub.zsba.vo.outflow.OutResponse;
import com.zebone.nhis.ma.pub.zsba.vo.outflow.PresOrder;
import com.zebone.nhis.pro.zsba.cn.opdw.service.PresOutflowService;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 博爱处方流转平台回调接口，按照平台格式和URL提供<br>
 *     目前我们并没有验签
 */
@Controller
@RequestMapping("/static/open/")
public class PresOutflowController {

    private Logger logger = LoggerFactory.getLogger("nhis.BaWebServiceLog");

    @Resource
    private PresOutflowService presOutflowService;

    /**
     * 2202 处方审核结果通知
     * http://hcnmindoc.bsoft.com.cn/docs/uop/cfshjgtz
     * @param param
     * @return
     */
    @RequestMapping(value = "prescription/recipeCheck", method = RequestMethod.POST)
    @ResponseBody
    public OutResponse<CommonResponse> noticeAudit(@RequestBody String param){
        AuditResult vo = JsonUtil.readValue(param,AuditResult.class);
        presOutflowService.modOutPresInfo(vo);
        OutResponse<CommonResponse> response = new OutResponse<>(200);
        response.setBody(new CommonResponse(true));
        return response;
    }

    /**
     * 2203 处方转回院内通知
     * http://hcnmindoc.bsoft.com.cn/docs/uop/cfzhyntz
     * @param param
     * @return
     */
    @RequestMapping(value = "prescription/recipe/toInner", method = RequestMethod.POST)
    @ResponseBody
    public OutResponse<CommonResponse> noticeBack(@RequestBody String param){
        JSONObject jsonObject = JSONObject.fromObject(param);
        String presNo = jsonObject.getString("recipeIdOutter");
        if(StringUtils.isBlank(presNo)) {
            throw new BusException("recipeIdOutter不能为空");
        }
        presOutflowService.modCnPresStatus(presNo);
        OutResponse<CommonResponse> response = new OutResponse<>(200);
        response.setBody(new CommonResponse(true));
        return response;
    }

    /**
     * 2204 处方/诊金状态更新通知
     * http://hcnmindoc.bsoft.com.cn/docs/uop/recipeOrderNotice
     * @param param
     * @return
     */
    @RequestMapping(value = "prescription/recipeOrder/notice", method = RequestMethod.POST)
    @ResponseBody
    public OutResponse<CommonResponse> noticeStatus(@RequestBody String param){
        PresOrder vo = JsonUtil.readValue(param,PresOrder.class);
        if(vo == null || StringUtils.isBlank(vo.getRecipeIdOutter())){
            throw new BusException("必填参数不能为空");
        }
        presOutflowService.modOutPresStatus(vo);
        OutResponse<CommonResponse> response = new OutResponse<>(200);
        response.setBody(new CommonResponse(true));
        return response;
    }


    /**
     * 2205 院内缴费状态查询
     * http://hcnmindoc.bsoft.com.cn/docs/uop/orgPayStatusQuery
     * @param param
     * @return
     */
    @RequestMapping(value = "prescription/recipe/orgPayStatusQuery", method = RequestMethod.POST)
    @ResponseBody
    public OutResponse<PresOrder> getPayInfo(@RequestBody String param){
        JSONObject jsonObject = JSONObject.fromObject(param);
        String codePv = jsonObject.getString("visitIdOutter");
        String bizType = jsonObject.getString("bizType");
        if(StringUtils.isBlank(codePv) || StringUtils.isBlank(bizType)) {
            throw new BusException("visitIdOutter|bizType都不能为空");
        }
        Map<String, Object> paySettle = presOutflowService.getPaySettle(codePv);
        PresOrder presOrder = new PresOrder();
        presOrder.setVisitIdOutter(codePv);
        presOrder.setPayAmt(MapUtils.getDouble(paySettle,"amountSt"));
        presOrder.setBizType(bizType);
        presOrder.setPayStatus(paySettle==null?0:1);
        presOrder.setPayTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format((Date) MapUtils.getObject(paySettle,"dateSt")));
        OutResponse<PresOrder> response = new OutResponse<>(200);
        response.setBody(presOrder);
        return response;
    }


    @ExceptionHandler
    @ResponseBody
    public OutResponse<CommonResponse> exceptionHandler(Exception e){
        if(!(ExceptionUtils.getRootCause(e) instanceof BusException)){
            logger.error("外购平台回调处理异常：",e);
        }
        OutResponse response = new OutResponse<CommonResponse>(999);
        response.setBody(new CommonResponse(false, ExceptionUtils.getRootCauseMessage(e)));
        return response;
    }
}
