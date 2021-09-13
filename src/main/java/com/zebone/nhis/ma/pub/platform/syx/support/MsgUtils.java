package com.zebone.nhis.ma.pub.platform.syx.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class MsgUtils {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat sdfl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdl = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 将map中的对应key的值进行转换，没有为null
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getPropValueStr(Map<String, Object> map,String key) {
		if(map==null)return "";
		String value="" ;
		if(map.containsKey(key)){
			Object obj=map.get(key);
			value=obj==null?"":obj.toString();
		}
		return value;
	}
	/**
	 * 取日期内容
	 * @param map
	 * @return
	 */
	public static Date getPropValueDate(Map<String, Object> map,String key) {
		Date value=null;
		boolean have=map.containsKey(key);
		if(have){
			Object obj=map.get(key);
			if(obj==null) return null;
			if(obj instanceof Date )
			{
				return (Date)obj;//如果传入的就是Date类型的数据
			}
			String dateStr=obj.toString();
			try {
				if(dateStr.indexOf("-")>=0){
					dateStr=dateStr.substring(0, 19);
					value = sdfl.parse(dateStr);
				}else{
					value = sdf.parse(dateStr);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}			
		}
		return value;
	}
	/**
	 *创建请求公共节点
	 * @param req 请求对象
	 * @param action 报文分类
	 * @return
	 */
	public static Request createPubReq(Request req,String action){
		req.getId().setRoot("2.16.156.10011.2.5.1.1");
		req.getId().setExtension(NHISUUID.getKeyId());
		req.getCreationTime().setValue(sdf.format(new Date()));
		req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
		req.getInteractionId().setExtension(action);
		req.getProcessingCode().setCode("P");
		req.getProcessingModeCode();
		req.getAcceptAckCode().setCode("AL");
		req.getReceiver().setTypeCode("RCV");
		req.getReceiver().getDevice().setClassCode("DEV");
		req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
		req.getReceiver().getDevice().getId();
		req.getReceiver().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getReceiver().getDevice().getId().getItem().setExtension("192.168.8.234");
		req.getSender().setTypeCode("SND");
		req.getSender().getDevice().setClassCode("DEV");
		req.getSender().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		String codeEmp=MsgUtils.getPropValueStr(qryEmpinfo(UserContext.getUser().getPkEmp()), "oldCode");
		req.getSender().getDevice().getId().getItem().setExtension("HIS");
		return req;
	}
	
	/**
	 *创建请求公共节点
	 * @param req 请求对象
	 * @param action 报文分类
	 * @param acceptCode acceptAckCode节点的code属性值
	 * @return
	 */
	public static Request createPubReq(Request req,String action,String acceptCode){
		req.getId().setRoot("2.16.156.10011.2.5.1.1");
		req.getId().setExtension(NHISUUID.getKeyId());
		req.getCreationTime().setValue(sdf.format(new Date()));
		req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
		req.getInteractionId().setExtension(action);
		req.getProcessingCode().setCode("P");
		req.getProcessingModeCode();
		req.getAcceptAckCode().setCode(acceptCode);
		req.getReceiver().setTypeCode("RCV");
		req.getReceiver().getDevice().setClassCode("DEV");
		req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
		req.getReceiver().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getReceiver().getDevice().getId().getItem().setExtension("");//接收方待定
		req.getSender().setTypeCode("SND");
		req.getSender().getDevice().setClassCode("DEV");
		req.getSender().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		String codeEmp=MsgUtils.getPropValueStr(qryEmpinfo(UserContext.getUser().getPkEmp()), "oldCode");
		req.getSender().getDevice().getId().setExtension(codeEmp);
		return req;
	}
	/**
	 * 查询人员信息
	 * @param pkEmp
	 * @return
	 */
	public static Map<String,Object> qryEmpinfo(String pkEmp){
		String sql="select old_code from bd_ou_employee where pk_emp=?";
		Map<String,Object> resMap=DataBaseHelper.queryForMap(sql, new Object[]{pkEmp});
		return resMap;
	}

	
	 /**
     * 
     * @param map
     * @param key
     * 处理时间，如果时间为空发""
     */
    public static String dateFormatString(String format, Date propValueDate){
    	if (propValueDate == null||format == null) {
    		return "";
		}else {
			return sdf.format(propValueDate);
		}
    }
    /**
     * 使用多线程执行发送msg
     * @param listCall
     */
    public static void threadExecuteMsg(List<Callable<Map<String,Object>>> listCall){
    	//定义固定长度的线程池  防止线程过多
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        //Future用于获取结果
        List<Future<Map<String, Object>>> futures = null;
        try {
			 futures=executorService.invokeAll(listCall);
        } catch (Exception e) {
			e.printStackTrace();
			//throw new BusException("查询患者信息出错,请重试！");
		}
        executorService.shutdown();//关闭线程池
    }
    /**
	 * 
	 * @param addList
	 * 发送新增,更新医嘱信息（包含手术,检查，检验）
	 */
    public static void sendOrdRegMsg(List<CnOrder> addList) {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("control", "NW");
		paramMap.put("ordStatus", "2");
		paramMap.put("ordlistvo",addList);
		PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
	}
}




