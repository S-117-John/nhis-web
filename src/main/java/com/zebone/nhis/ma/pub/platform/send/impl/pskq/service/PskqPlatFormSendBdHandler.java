package com.zebone.nhis.ma.pub.platform.send.impl.pskq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.base.bd.vo.BdItemVO;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdOrg;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.BdOrdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.factory.MessageFactory;
import com.zebone.nhis.ma.pub.platform.pskq.model.DrugItem;
import com.zebone.nhis.ma.pub.platform.pskq.model.MainData;
import com.zebone.nhis.ma.pub.platform.pskq.model.MaterialItem;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderItem;
import com.zebone.nhis.ma.pub.platform.pskq.model.PriceItem;
import com.zebone.nhis.ma.pub.platform.pskq.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ResponseBody;
import com.zebone.nhis.ma.pub.platform.pskq.utils.RestTemplateUtil;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendBdMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.BdDefdocVO;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * @author lijin
 * 基础数据
 */
@Service
public class PskqPlatFormSendBdHandler {
    /**
     * 添加状态
     **/
    public static final String AddState = "ADD";

    /**
     * 更新状态
     */
    public static final String UpdateState = "UPDATE";

    /**
     * 删除状态
     */
    public static final String DelState = "DELETE";
	
	@Autowired
    private PskqPlatFormSendBdMapper pskqPlatFormSendBdMapper;


    @Autowired
    private HttpRestTemplate httpRestTemplate;

	/**
	 * 查询收费项目
	 * @param paramMap
	 * @param listener
	 */
	public void getBdItemInfo(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        String responseBody="";
        try {
        	Map<String, Object> param=(Map<String, Object>) paramMap.get("item");
        	if(null==param){
        		listener.error(null,"","");
                return;
        	}
        	PriceItem priceItem = pskqPlatFormSendBdMapper.getBdItemInfo(param);
            if(null==priceItem){
                listener.error(null,"","");
                return;
            }
            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(priceItem);
            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
            //消息成功
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
            	listener.success(result, JSON.toJSONString(paramVo));
            }else {
            	listener.error("", result, JSON.toJSONString(paramVo));
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),result,responseBody);
        }
    }
    /**
	 * 查询医嘱项目
	 * @param paramMap
	 * @param listener
	 */
	public void getBdOrdInfo(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        try {
        	Map<String, Object> param=(Map<String, Object>) paramMap.get("bdOrd");
        	if(null==param){
        		listener.error(null,"","");
                return;
        	}
        	OrderItem orderItem = pskqPlatFormSendBdMapper.getBdOrdInfo(param);
            if(null==orderItem){
                listener.error(null,"","");
                return;
            }
            List<Map<String,Object>> listorg=DataBaseHelper.queryForList("select * from bd_ou_org where pk_org!=?", "~");
            Map<String,Object> org=listorg.get(0);
            orderItem.setOrgCode(org.get("codeOrg").toString());
            orderItem.setOrgName(org.get("nameOrg").toString());

            String sql = "select * from BD_ORD where CODE = ? and DEL_FLAG = '0'";
            BdOrd bdOrd = DataBaseHelper.queryForBean(sql,BdOrd.class,new Object[]{orderItem!=null?orderItem.getOrderItemId():""});
            sql = "select * from BD_ORD_ORG where PK_ORD = ? and DEL_FLAG = '0'";
            BdOrdOrg bdOrdOrg = DataBaseHelper.queryForBean(sql,BdOrdOrg.class,new Object[]{bdOrd!=null?bdOrd.getPkOrd():""});

            orderItem.setOrgOrderItemPrice(bdOrdOrg!=null?bdOrdOrg.getPrice()+"":"");

            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(orderItem);

            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            String responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
            //消息成功
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
            	listener.success( result, JSON.toJSONString(paramVo));
            }else {
            	listener.error("", result, JSON.toJSONString(paramVo));
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),result,"");
        }
    }
    /**
	 * 查询药品信息
	 * @param paramMap
	 * @param listener
	 */
	public void getBdPdInfo(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        try {
        	DrugItem drugItem = pskqPlatFormSendBdMapper.getBdPdInfo(paramMap);
            if(null==drugItem){
                listener.error(null,"","");
                return;
            }
            List<Map<String,Object>> listorg=DataBaseHelper.queryForList("select * from bd_ou_org where pk_org!=?", "~");
            Map<String,Object> org=listorg.get(0);
            drugItem.setOrgCode(org.get("codeOrg").toString());
            //drugItem.setOrgName(org.get("nameOrg").toString());
            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(drugItem);
            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            String responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
            //消息成功
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
            	listener.success( result, JSON.toJSONString(paramVo));
            }else {
            	listener.error("", result, JSON.toJSONString(paramVo));
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),result,"");
        }
    }
    /**
     * 查询物资
     * @param paramMap
     * @param listener
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void getBdMaterInfo(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        try {
        	MaterialItem materialItem = pskqPlatFormSendBdMapper.getBdMaterInfo(paramMap);
            if(null==materialItem){
                listener.error(null,"","");
                return;
            }
            List<Map<String,Object>> listorg=DataBaseHelper.queryForList("select * from bd_ou_org where pk_org!=?", "~");
            Map<String,Object> org=listorg.get(0);
            materialItem.setOrgCode(org.get("codeOrg").toString());
            materialItem.setOrgName(org.get("nameOrg").toString());
            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(materialItem);
            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            String responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
            //消息成功
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
            	listener.success( result, JSON.toJSONString(paramVo));
            }else {
            	listener.error("", result, JSON.toJSONString(paramVo));
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),result,"");
        }
    }

    /**
     * 主数据接口
     * @param
     * @throws
     */
    public void sendBdDefDocMsg(Map<String,Object> paramMap,ResultListener listener) {
        //发送消息
        List<BdDefdocVO> resultAddList = new ArrayList<>();
        List<BdDefdocVO> resultUpdateList = new ArrayList<>();
        String codeEmp = paramMap.get("codeEmp")==null?"":paramMap.get("codeEmp").toString();
        //获取公共字典集合信息
        List<Map<String,Object>> tempList = (List<Map<String,Object>>)paramMap.get("bdDefdoc");
        String json = JSON.toJSONString(tempList);
        List<BdDefdocVO> bdDefdocVOList = JSON.parseObject(json,new TypeReference<List<BdDefdocVO>>(){}.getType());
        if(bdDefdocVOList!=null && bdDefdocVOList.size()>0){
            //获取集合信息判断信息是'添加'还是'修改'
            for(BdDefdocVO bdDefdocVO:bdDefdocVOList){
                String pkDefdoc = bdDefdocVO.getPkDefdoc()==null?"":bdDefdocVO.getPkDefdoc();
                String isAdd = paramMap.get("isAdd")==null?"":paramMap.get("isAdd").toString();
                if(("true".equals(isAdd))){
                    bdDefdocVO.setEmpCode(codeEmp);
                    bdDefdocVO.setState(AddState);	//对添加的信息增加'添加'状态
                    resultAddList.add(bdDefdocVO);
                }
                else if("false".equals(isAdd)){//如果前台对数据做了修改则IsUpdate为'1'
                    bdDefdocVO.setEmpCode(codeEmp);
                    bdDefdocVO.setState(UpdateState);	//对修改的信息添加'修改'状态
                    resultUpdateList.add(bdDefdocVO);
                }
            }
        }
        //获取删除的集合信息
        List<String> pkDefdocs = (List<String>)paramMap.get("delPkDefdocs");
        if(pkDefdocs!=null && pkDefdocs.size()>0){
           List<BdDefdocVO> bdDefdocList = pskqPlatFormSendBdMapper.getBdDefdocList(pkDefdocs);
            if(bdDefdocList!=null && bdDefdocList.size()>0){
                for(BdDefdocVO bdDefdoc : bdDefdocList){
                    //删除的信息
                    bdDefdoc.setEmpCode(codeEmp);
                    bdDefdoc.setState(DelState);
                    resultUpdateList.add(bdDefdoc);
                }
            }
        }
        /**判断有没有做新增/修改/删除操作,只有当数据发生改变后才发送消息**/
        if(resultAddList!=null && resultAddList.size()>0){
        	for (BdDefdocVO bdDefdocVO : resultAddList) {
            	String result="";
            	try {
	            	MessageFactory messageFactory = new MessageFactory();
	            	MainData mainData=bdDocCopyMain(bdDefdocVO);
	                Message message = messageFactory.getInstance(mainData);
	                Map<String, Object> map=new HashMap<String, Object>();
	                map.put("serviceCode", "S0011");map.put("serviceName", "术语注册服务");
	                map.put("eventCode", "E001101");map.put("eventName", "新增主数据");
	                RequestBody requestBody = message.getRequestBody(map);
	                result =  JSON.toJSONString(requestBody);
	                //发送消息
	                String responseBody = httpRestTemplate.postForString(result);
	                ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
	                //消息成功
	                if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
	                	listener.success( result, JSON.toJSONString(paramVo));
	                }else {
	                	listener.error("", result, JSON.toJSONString(paramVo));
	                }
            	} catch (Exception e) {
					// TODO Auto-generated catch block
            		listener.exception(e.getMessage(),result,"");
					e.printStackTrace();
				}
    		}
        }
        if(resultUpdateList!=null && resultUpdateList.size()>0){
            for (BdDefdocVO bdDefdocVO : resultUpdateList) {
            	String result="";
            	try {
	            	MessageFactory messageFactory = new MessageFactory();
	            	MainData mainData=bdDocCopyMain(bdDefdocVO);
	                Message message = messageFactory.getInstance(mainData);
	                Map<String, Object> map=new HashMap<String, Object>();
	                map.put("serviceCode", "S0012");map.put("serviceName", "术语更新服务");
	                map.put("eventCode", "E001201");map.put("eventName", "更新主数据");
	                RequestBody requestBody = message.getRequestBody(map);
					
	                result =  JSON.toJSONString(requestBody);
	                //发送消息
	                String responseBody = httpRestTemplate.postForString(result);
	                ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
	                //消息成功
	                if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
	                	listener.success( result, JSON.toJSONString(paramVo));
	                }else {
	                	listener.error("", result, JSON.toJSONString(paramVo));
	                }
            	} catch (Exception e) {
					// TODO Auto-generated catch block
            		listener.exception(e.getMessage(),result,"");
				}
    		}
        }
        
    }
    private BdOuEmployee getUserById(String id){
    	BdOuEmployee bdOuMap = DataBaseHelper
				.queryForBean(
						"SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.CODE_EMP = ?",
						BdOuEmployee.class,id);
    	return bdOuMap;
    }
    private MainData bdDocCopyMain(BdDefdocVO bdDefdocVO){
    	MainData mainData=new MainData();
    	mainData.setMasterMemberId(bdDefdocVO.getCode());
    	mainData.setMasterMemberCode(bdDefdocVO.getCode());
    	mainData.setMasterMemberName(bdDefdocVO.getName());
    	mainData.setSortNo(bdDefdocVO.getDelFlag().equals("0")?"1":"0");
    	mainData.setDeleteFlag(bdDefdocVO.getDelFlag());
    	mainData.setSpellCode(bdDefdocVO.getSpcode());
    	BdOuEmployee creaUser=getUserById(bdDefdocVO.getCreator());
    	if (null!=creaUser) {
    		mainData.setEnterOperaId(creaUser.getCodeEmp());
    		mainData.setEnterOperaName(creaUser.getNameEmp());
    	}
    	if(bdDefdocVO.getCreateTime()!=null){
    		mainData.setEnterDateTime(DateUtils.getDateTimeStr(bdDefdocVO.getCreateTime()));
    	}
    	if (CommonUtils.isEmptyString(bdDefdocVO.getModifier())) {
    		BdOuEmployee upDateUser=getUserById(bdDefdocVO.getModifier());
    		if (null!=upDateUser) {
    			mainData.setModifyOperaId(upDateUser.getCodeEmp());
    			mainData.setModifyOperaName(upDateUser.getNameEmp());
    		}
    	}
    	if(bdDefdocVO.getModityTime()!=null){
    		mainData.setModifyDateTime(DateUtils.getDateTimeStr(bdDefdocVO.getModityTime()));
    	}
    	mainData.setMasterDefinitionCode(bdDefdocVO.getCodeDefdoclist());
    	return mainData;
    }

    public BdOrdVO findBdOrdById(String id){
        BdOrdVO bdOrdVO = new BdOrdVO();
        String sql = "select * from BD_ORD_ITEM where PK_ORD = ?";

        List<BdOrdItem> bdOrdItemList = DataBaseHelper.queryForList(sql,BdOrdItem.class,new Object[]{id});
        List<BdItemVO> bdItemVOList = new ArrayList<>();
        for (BdOrdItem bdOrdItem : bdOrdItemList) {
            BdItemVO bdItemVO = new BdItemVO();
            bdItemVO.setAmount(bdOrdItem.getQuan());
            bdItemVO.setPkItem(bdOrdItem.getPkItem());
            bdItemVO.setPkOrdItem(bdOrdItem.getPkOrditem());
            bdItemVOList.add(bdItemVO);
        }
        bdOrdVO.setPkOrd(id);
        bdOrdVO.setBdItemVOList(bdItemVOList);
        return bdOrdVO;
    }

}
