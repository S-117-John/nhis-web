package com.zebone.nhis.ma.pub.platform.send.impl.zb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.hl7v2.HL7Exception;

import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zb.dao.CnSendMapper;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendEx;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.vo.BdOuDeptVo;
/**
 * 发送EX领域消息
 * @author yangxue
 *
 */
@Service
public class ZBPlatFormSendExHandler {
	@Resource
	private MsgSendEx msgSendEx;
	@Resource
	private CnSendMapper cnSendMapper;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void  sendExConfirmMsg(Map<String,Object> paramMap) throws HL7Exception{
		List<Map<String,Object>> listMap = (List<Map<String,Object>>)paramMap.get("exlist");
		String typeStatus = paramMap.get("typeStatus").toString();
		msgSendEx.sendRASMsg("O17", listMap,typeStatus);
	}
	/**
	 *  发送医嘱核对信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendExOrderCheck(Map<String,Object> paramMap) throws HL7Exception{
		/**
		 * 当护士站站进行病区医嘱核对时paramMap.get("ordlist")中数没有根据患者进行分类，会导致消息推送中B患者的医嘱会组装到A患者下。
		 * 未处理造成上述问题要进行遍历分分类并组装成新的数据类型
		 */
		//获取传递[map]数据
		List<Map<String,Object>> listMap = (List<Map<String,Object>>)paramMap.get("ordlist");
		//根据pkPv分类组装new的list,数据格式 [pkpv1=[map],pkpv2=[map]]
		List<List<Map<String, Object>>> newlistsMap = new ArrayList<>();
		//循环前台传递的数据
		for (Map<String,Object> Map:listMap) {
			//获取pkpv
			String pkPv = MsgUtils.getPropValueStr(Map, "pkPv");
			//设置变量
			boolean condition = true;
            //判断new的lis是否有数据，保证第一次能add数据
			if(newlistsMap.size()>0){
				//当new的lis中有数据时进行遍历
				for(List<Map<String, Object>> newListMap: newlistsMap){
					//判断new的lis中有没有pkpv与第一个for遍历获取的pkpv相同
					if(pkPv.equals(LbSelfUtil.getPropValueStr(newListMap.get(0),"pkPv"))){
						//两个pkpv想同时修改变量状态，不让进行后续重新组装数据操作
						condition = false;
						//两个pkpv想同时,将第一个遍历map进行add操作
						newListMap.add(Map);
						break;
					}
				}
			}
			//判断参数进行组装新的数据格式
			if(condition){
				List<Map<String, Object>> newlistMap = new ArrayList<>();
				newlistMap.add(Map);
				newlistsMap.add(newlistMap);
			}
		}
		msgSendEx.sendOMPMsg("O09", newlistsMap);
	}
	
	/**
	 * 发送手术
	 * @param paramMap
	 * @throws Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendOpConfirmMsg(Map<String, Object> paramMap) throws Exception{
		//根据参数判断不进行推送手术申请消息
		if(paramMap.containsKey("LBcontrol") && ("NO").equals(paramMap.get("LBcontrol"))){
		   return;
		}
		//判断是否存在Key
		if(!paramMap.containsKey("ordlist")){
			if(paramMap.containsKey("pkCnList")){
				List<String> pkCnords=(List<String>)paramMap.get("pkCnList");
				Map<String, List<String>> map=new HashMap<String, List<String>>();
				map.put("pkCnords", pkCnords);
			    List<Map<String,Object>> mapList=cnSendMapper.qryLisOrderRLInfo(map);
			    paramMap.put("ordlist", mapList);	
			}
		}
		msgSendEx.createORM_O01Msg(paramMap);
		}
}
