package com.zebone.nhis.webservice.syx.cxf;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.syx.service.EmrSyxRecService;
import com.zebone.nhis.webservice.syx.utils.EmrWsUtil;
import com.zebone.nhis.webservice.syx.vo.EmrAdmitRecRtn;
import com.zebone.nhis.webservice.syx.vo.EmrPatInfoRtn;
import com.zebone.nhis.webservice.syx.vo.EmrRequest;
import com.zebone.nhis.webservice.syx.vo.EmrRequestData;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfAdmitRec;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfCourseRec;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfHomePage;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfMessage;
import com.zebone.nhis.webservice.syx.vo.emr.EmrHmCourseRslt;
import com.zebone.nhis.webservice.syx.vo.emr.EmrHmFirstCourseRslt;
import com.zebone.nhis.webservice.syx.vo.emr.EmrHmReq;
import com.zebone.nhis.webservice.syx.vo.emr.EmrHmRespCourse;
import com.zebone.nhis.webservice.syx.vo.emr.EmrHmRespFirstCourse;
import com.zebone.platform.modules.utils.JsonUtil;

@WebService
@SOAPBinding(style = Style.RPC)
public class EmrSyxWebServiceImpl implements IEmrSyxWebService {

	@Resource
	private EmrSyxRecService emrRecService;//????????????

	private SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String sdfstr= "yyyy-MM-dd HH:mm:ss";
	/**
     * Xml????????????????????????
     * @throws JAXBException 
     */
	@SuppressWarnings("unchecked")
	@Override
	public String EmrInterfaceXml(@WebParam(name = "param")String param) {
		EmrRequest request=null;
		String result="";
		try {
			request = (EmrRequest) XmlUtil.XmlToBean(param, EmrRequest.class);
			//System.out.println(XmlUtil.beanToXml(request, EmrRequest.class));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return EmrWsUtil.getEmrResponse("000400", e.getMessage(), null);
		}
		
		if(request==null||request.getCode()==null||request.getReqData()==null||request.getReqData().getEmrRequestData()==null){
			return EmrWsUtil.getEmrResponse("000400", "???????????????", null);
		}
		String serviceCode=request.getCode();
		EmrRequestData reqData=request.getReqData().getEmrRequestData();
		Map<String,Object> map=new HashMap<>();
		map.put("codePi", reqData.getCodePi());
		map.put("codeIp", reqData.getCodeIp());
		map.put("ipTimes", reqData.getIpTimes());
		map.put("name", reqData.getName());
		//System.out.println(request.toString());
		EmrAdmitRecRtn rtnData=new EmrAdmitRecRtn();
		EmrPatInfoRtn infoRtnData=new EmrPatInfoRtn();
		String rtnCode="00000";
		String rtnMsg="??????";
		try {
			switch(serviceCode) {  
	         case "EMR11"://????????????????????????????????????
	        	 rtnData = emrRecService.queryPatEncList(map);
	        	 result = EmrWsUtil.getEmrResponse(rtnCode, rtnMsg, rtnData);
	        	 break;
	         case "EMR12"://????????????????????????????????????
	        	 infoRtnData = emrRecService.queryPatInfoList(map);
	        	 result = EmrWsUtil.getEmrResponsePatInfo(rtnCode, rtnMsg, infoRtnData);
	        	 break;
	         case "EMR21"://????????????-????????????
	        	 map.put("reqData", reqData);
	        	 result=emrRecService.genEmrBloolTransRec(map);
	        	 break;	 
	         case "EMR22"://????????????-???????????????
	        	 map.put("reqData", reqData);
	        	 result=emrRecService.genEmrBloolTransEst(map);
	        	 break;	 
	         case "EMR23"://????????????-?????????????????????
	        	 map.put("reqData", reqData);
	        	 result=emrRecService.genEmrBloolTransEff(map);
	        	 break;	 	         
	        default: 
	        	rtnCode="000500";
	        	rtnMsg = "??????????????????:"+serviceCode;
	        	rtnData=null;
	            break;  
	        } 
		} catch (Exception e) {
			e.printStackTrace();
			return EmrWsUtil.getEmrResponse("000500", e.getMessage(), null);
		}
		if(serviceCode.equals("EMR21") || serviceCode.equals("EMR22") || serviceCode.equals("EMR23")){
			if(result!=null){
				String mess=EmrWsUtil.getEmrResponse("000500", result, null);
				return mess;
			}
			String succmess=EmrWsUtil.getEmrResponse("000000", "??????????????????", null);
			return succmess;
			
		}
		return result;
	}

	/**
	 * ????????????-??????????????????????????????
	 * @param xml
	 * @return xml
	 */
	@WebMethod
	public String Hm_GetFisrtCourseInfo(@WebParam(name="param")String param){
		EmrHmReq request=null;
		String result="";
		try {
			request = (EmrHmReq) XmlUtil.XmlToBean(param, EmrHmReq.class);
			//System.out.println(XmlUtil.beanToXml(request, EmrRequest.class));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return result;
		}
		
		if(request==null||StringUtils.isEmpty(request.getMrn())||StringUtils.isEmpty(request.getReordTimeStart())||StringUtils.isEmpty(request.getReordTimeEnd())){
			return "";//????????????
		}
		String mrn=request.getMrn();
		String reordTimeBegin=request.getReordTimeStart();
		String reordTimeEnd=request.getReordTimeEnd();
		Map<String,Object> map=new HashMap<>();
		map.put("codeIp", mrn);
		map.put("beginDate", reordTimeBegin);
		map.put("endDate", reordTimeEnd);
		List<EmrHmFirstCourseRslt> rtnList = emrRecService.queryHmFirstCourse(map);
		EmrHmRespFirstCourse resp = new EmrHmRespFirstCourse();
		resp.setRstList(rtnList);
		try {
			result = XmlUtil.beanToXml(resp, resp.getClass());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * ????????????-??????????????????
	 * @param xml
	 * @return xml
	 */
	@WebMethod
	public String Hm_EmrCourse(@WebParam(name="param")String param){
		EmrHmReq request=null;
		String result="";
		try {
			request = (EmrHmReq) XmlUtil.XmlToBean(param, EmrHmReq.class);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return result;
		}
		
		if(request==null||StringUtils.isEmpty(request.getMrn())||StringUtils.isEmpty(request.getReordTimeStart())||StringUtils.isEmpty(request.getReordTimeEnd())){
			return "";//????????????
		}
		String mrn=request.getMrn();
		String reordTimeBegin=request.getReordTimeStart();
		String reordTimeEnd=request.getReordTimeEnd();
		Map<String,Object> map=new HashMap<>();
		map.put("codeIp", mrn);
		map.put("beginDate", reordTimeBegin);
		map.put("endDate", reordTimeEnd);
		List<EmrHmCourseRslt> rtnList = emrRecService.queryHmCourse(map);
		EmrHmRespCourse resp = new EmrHmRespCourse();
		resp.setRstList(rtnList);
		try {
			result = XmlUtil.beanToXml(resp, resp.getClass());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * ????????????
	 * @param param
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String EmrInterfaceAF(String param) {
		System.out.print("????????????:" + param +"\n");//????????????
		String result = CommonUtils.getString(new RespJson("99|???????????????|", true));
		String methodName = "";
		try {
			EmrAfMessage message=JsonUtil.readValue(param, EmrAfMessage.class,sdfstr);
			if(message==null||message.getEmrAfRequest()==null){
				return CommonUtils.getString(new RespJson("99|???????????????????????????", false));
			}
			methodName=message.getMethodName();
			if(CommonUtils.isEmptyString(methodName)){
				return CommonUtils.getString(new RespJson("99|???????????????????????????????????????MethodName???", false));
			}
			if(message.getEmrAfRequest().getStartime()==null){
				return CommonUtils.getString(new RespJson("99|???????????????????????????????????????startime???", false));
			}
			if(message.getEmrAfRequest().getEndtime()==null){
				return CommonUtils.getString(new RespJson("99|???????????????????????????????????????endtime???", false));
			}

			Map<String,Object> map=new HashMap<>();
			map.put("beginDate", sdf.format(message.getEmrAfRequest().getStartime()));
			map.put("endDate", sdf.format(message.getEmrAfRequest().getEndtime()));

			switch(methodName) {  
			case "home_page": //????????????????????????
				List<EmrAfHomePage> list = emrRecService.queryAfHomePage(map);
//				Gson gson = new Gson();
//				result = gson.toJson(list);
				
				//result = JsonUtil.writeValueAsString(list,sdfstr);
				
				result = getJsonStr(list); 
				
				System.out.println("result home_page:"+result);
				break; 
			case "inpatient_journal": //?????????????????????
				List<EmrAfAdmitRec> listAdmitRec = emrRecService.queryAfAdmitRec(map);
				result = getJsonStr(listAdmitRec); 
				System.out.println("result inpatient_journal:"+result);
				break; 
			case "inpatient_disease_course_journal": //?????????????????????
				List<EmrAfCourseRec> listCourseRec = emrRecService.queryAfCourseRec(map);
				result = getJsonStr(listCourseRec); 
				System.out.println("result inpatient_disease_course_journal:"+result);
				break; 
			
			default: 
				result = "99|???????????????"+methodName+"????????????????????????";
				return CommonUtils.getString(new RespJson(result, false));
			} 
		} catch (Exception e) {
			e.printStackTrace();
			//wslogService.InsertWsLogs(funcId, CommonUtils.isEmptyString(param) ? input_info : param,"99|" + e.toString(),"0");//??????????????????
			return CommonUtils.getString(new RespJson("99|" + e.toString().split(":")[1], false));
		} finally {
			//DataSourceRoute.putAppId("default");
		}
		return result;
	}

	private String getJsonStr(Object object) throws IOException,
			JsonGenerationException, JsonMappingException {
		String result;
		ObjectMapper mapper = new ObjectMapper();

		/*mapper.setSerializationInclusion(Inclusion.NON_NULL);*/
		mapper.setDateFormat((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
		//mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
		
		result = mapper.writeValueAsString(object);
		return result;
	}
	
	
	
}
