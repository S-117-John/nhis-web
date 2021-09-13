package com.zebone.nhis.pro.zsba.scm.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.scm.dao.IpPdDeDrugBaMapper;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 博爱-外接第三方系统发药服务
 *
 */
@Service
public class BaDeDrugExtService {

	@Autowired
    private IpPdDeDrugBaMapper ipPdDeDrugBaMapper;
	
    /**
     * 门诊外部配药对接电子药篮
     * @param param
     * @param user
     * @throws InterruptedException 
     */
    public void opDosageBasket(String param, IUser user) throws IOException, InterruptedException {
        Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
        //状态为0时可以启用
        if("0".equals(MapUtils.getString(paramMap, "type",""))){
        	//判断入参不能为空
            if(MapUtils.isNotEmpty(paramMap)){
            	//获取科室参数
            	String BasketUrl = ApplicationUtils.getDeptSysparam("EX0081", UserContext.getUser().getPkDept());//使用科室级别系统参数
    			//判断获取菜参数不能为空
            	if(StringUtils.isNotBlank(BasketUrl)){
            		//根据参数格式进行截取
    				String Baskcard[] = BasketUrl.split(":");
    				//效验参截取后是否时两个
    				if(Baskcard.length == 2) {
    					List<Map<String, Object>> preaList = ipPdDeDrugBaMapper.qryPresBasketPkPvInfo(paramMap);
    					//判断lis集合是否为空
						if(null != preaList && !preaList.isEmpty()) 
						{
							StringBuffer dateString = new StringBuffer("");
							//遍历
							for (Map<String, Object> map : preaList) {
								//判断药蓝编码是否为空
								if(StringUtils.isNotBlank(MapUtils.getString(map, "codeBasket","")) ) {
									// 创建数据包对象，封装要发送的数据，接收端IP，端口
									dateString.append(String.format("*%s#", MapUtils.getString(map,"codeBasket")));
								}
							}
							if(StringUtils.isNotEmpty(dateString)) {
					            byte[] date = dateString.toString().getBytes();
					            //创建InetAdress对象，封装自己的IP地址
					            //InetAddress inet = InetAddress.getByName(MapUtils.getString(paramMap,"basketIp","192.168.35.110"));
					            //DatagramPacket dp = new DatagramPacket(date, date.length, inet,Integer.parseInt(MapUtils.getString(paramMap,"basketPort","11502")));
					            InetAddress inet = InetAddress.getByName(Baskcard[0]);
					            DatagramPacket dp = new DatagramPacket(date, date.length, inet,Integer.parseInt(Baskcard[1]));
					            //创建DatagramSocket对象，数据包的发送和接收对象
					            DatagramSocket ds = new DatagramSocket();
					            //调用ds对象的方法send，发送数据包
					            ds.send(dp);
					            ds.close();
							}
						}
    				}else {
    					throw new BusException("当前科室参数EX0081数据格式不是：“ip:端口”");
    				}
    			}
            }
        }
        
    }
    
    /**
     * 门诊外部配药对接电子药篮
     * @param param
     * @param user
     */
    public void opDosageBasketTest(String param, IUser user) throws Exception {
    	List<Map<String,Object>> paramList= JsonUtil.readValue(param,new TypeReference<List<Map<String,Object>>>(){});
    	StringBuffer dateString = new StringBuffer("");
    	for(Map<String,Object> map:paramList) {
    		// 创建数据包对象，封装要发送的数据，接收端IP，端口
    		dateString.append(String.format("*%s#", MapUtils.getString(map,"codeBasket")));
    	}
        byte[] date = dateString.toString().getBytes();
        //创建InetAdress对象，封装自己的IP地址
        InetAddress inet = InetAddress.getByName(MapUtils.getString(paramList.get(0),"basketIp","192.168.35.110"));
        DatagramPacket dp = new DatagramPacket(date, date.length, inet,Integer.parseInt(MapUtils.getString(paramList.get(0),"basketPort","11502")));
        //创建DatagramSocket对象，数据包的发送和接收对象
        DatagramSocket ds = new DatagramSocket();
        //调用ds对象的方法send，发送数据包
        ds.send(dp);
        ds.close();
    }
    
    /**
     * ba-根本pkSettle查询已配药药蓝信息
     * 交易号:022003022004
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryBaPresCodeBasket(String param, IUser user) {
    	Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
    	return ipPdDeDrugBaMapper.queyPresCodeBasket(paramMap);
    }
    
    /**
     * 根据条件统计处方数量
     * 交易号:022003022005
     * @param param
     * @param user
     * @return
     */
    public Integer queyPresConfNumber(String param,IUser user){
    	Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
    	return ipPdDeDrugBaMapper.queyPresConfNumber(paramMap);
    }
	
    /**
     * ba-门诊配药处方信息查询
     * 交易号：022003022007
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queyPresDetials(String param,IUser user){
    	Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
    	String pkSettle = MapUtils.getString(paramMap, "pkSettle");
    	Map<String, Object> presMap = new HashedMap();
    	//查询处方信息
    	List<Map<String, Object>> presList = ipPdDeDrugBaMapper.qryPresPresNo(paramMap);

    	if(presList.size() == 1) {
    		//判断是否携带上一个配药处方的pkSettle并且判断是否和查询的处方pkSettle是否是同一个
    		if(StringUtils.isNotBlank(pkSettle) && !pkSettle.equals(MapUtils.getString(presList.get(0), "pkSettle"))) {
    			//查询上一个配药处方同一次结算有未配药处方
				int num = ipPdDeDrugBaMapper.qryPresPending(paramMap);
				if(num >0 ) {
					//响应添加状态
					presMap.put("BaSurplus", "1");
				}
			}
    		//判断处方是否是打印状态并且药篮编码是否为空
    		if("1".equals(MapUtils.getString(presList.get(0), "euStatus")) && StringUtils.isBlank(MapUtils.getString(presList.get(0), "codeBasket"))) {
    			Map<String,Object> paramBasketMap = new HashedMap();
        		paramBasketMap.put("pkSettle", MapUtils.getString(presList.get(0), "pkSettle"));
        		paramBasketMap.put("basketEuSt", "1");
        		paramBasketMap.put("euStatus", "2");
        		paramBasketMap.put("pkDept", MapUtils.getString(paramMap, "pkDept"));
        		//根据pkSettle查询同一次结算已配药的药蓝编码
        		List<Map<String, Object>> codeBaskList = ipPdDeDrugBaMapper.queyPresCodeBasket(paramBasketMap);
        		if(null !=  codeBaskList && codeBaskList.size()>0) {
        			//将药篮编码添加到处方中
        			presList.get(0).put("codeBasket", MapUtils.getString(codeBaskList.get(0), "codeBasket"));
        		}
    		}
			getPvDiag(presList);
			paramMap.put("pkPresocc", MapUtils.getString(presList.get(0), "pkPresocc"));
    		//根据处方主键查询相关处方明细
    		List<Map<String,Object>> resList=ipPdDeDrugBaMapper.qryPresDetail(paramMap);
    		presMap.put("PresList", presList);
    		presMap.put("PresDetailList", resList);
    	}
    	return presMap;
    }

	private void getPvDiag(List<Map<String, Object>> presList) {
		List<String> pkpvList = new ArrayList<>();
		presList.forEach(pres->{
			pkpvList.add(MapUtils.getString(pres, "pkPv"));
		});
		if(pkpvList.size()>0) {
			ipPdDeDrugBaMapper.qryPvDiag(pkpvList).forEach(diag ->{
				presList.forEach(pre ->{
					if(MapUtils.getString(diag, "pkPv").equals(MapUtils.getString(pre, "pkPv"))) {
						pre.putAll(diag);
					}
				});
			});
		}
		
	}

	/**
     * 门诊发药-查询处方以及处方明细
     * 022003022008
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryPresDetailInfo(String param,IUser user){
    	Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
    	List<Map<String,Object>> PresDetailList = new ArrayList<Map<String,Object>>();
		if(MapUtils.isNotEmpty(paramMap)) {
			List<String> pkpvList = new ArrayList<>();
			if(StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkPvs"))) {
				//获取前台传递字符串组装list便于查询
				String pkpvs[] = MapUtils.getString(paramMap, "pkPvs").split(",");
				for (String pkpv : pkpvs) {
					pkpvList.add(pkpv);
				}
				paramMap.put("pkPvs", pkpvList);
			}
			if (Application.isSqlServer()) {
				paramMap.put("dbType", "sqlserver");
			}else{
				paramMap.put("dbType", "oralce");
			}
			//查询处方信息-条件有处方号
			List<Map<String,Object>> presInfoList=ipPdDeDrugBaMapper.qryUnFinishedPresInfo(paramMap);
			if(presInfoList.size()>0) {
				getPvDiag(presInfoList);
				
				List<String> presOccList = new ArrayList<>();
				presInfoList.forEach( pres->{
					presOccList.add(MapUtils.getString(pres, "pkPresocc"));
				});
				
				//查询处方相关处方明细
				paramMap.put("pkPresoccs", presOccList);
				PresDetailList = ipPdDeDrugBaMapper.qryPresDetialInfo(paramMap);
				PresDetailList.forEach(dt ->{
					presInfoList.forEach(occ ->{
						if(MapUtils.getString(dt, "pkPresocc").equals(MapUtils.getString(occ, "pkPresocc"))){
							dt.put("exPresOcc", occ);
						}
					});
				});
			}
		} 
		return PresDetailList;
    }
    
    /**
     * 查询药蓝编码汇总集合根据CodeOp
     * 交易号：022003022009
     * @param param
     * @param user
     * @return
     */
    public String qryPresBasketPkPvInfo(String param,IUser user){
    	Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
    	List<Map<String, Object>> baskSumList = ipPdDeDrugBaMapper.qryPresBasketPkPvInfo(paramMap);
    	String namePi = null;
    	String Basket = null;
    	for (Map<String, Object> bask : baskSumList) {
    		if(StringUtils.isEmpty(namePi)) {
    			namePi = MapUtils.getString(bask, "namePi");
    		}
    		if(StringUtils.isEmpty(Basket)) {
    			Basket = MapUtils.getString(bask, "codeBasket");
    		}else {
    			Basket += "、"+MapUtils.getString(bask, "codeBasket");
    		}
		}
    	if(StringUtils.isNotEmpty(namePi)) {
    		return namePi+":"+Basket;
    	}else {
    		return null;
    	}
      	
    }
    
    /**
     * 查询统计科室、门诊全药房未发药处方数量
     * 交易号：022003022010
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> qryPresConfNum(String param,IUser user){
    	Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
    	Map<String,Object> CobfNumMap = new HashMap<>();
    	List<Map<String, Object>> PresConfList = ipPdDeDrugBaMapper.qryPresConf(paramMap);
    	List<Map<String, Object>> PresConfAllList = ipPdDeDrugBaMapper.qryPresConfAll(paramMap);
    	CobfNumMap.put("deptWinno", PresConfList);
    	CobfNumMap.put("allDeptWinno", PresConfAllList);
    	return CobfNumMap;
    }
    
    /**
     * 查询处方信息
     * 交易号：022003022011
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> checkPresInfo(String param,IUser user){
		String presNo=JsonUtil.getFieldValue(param,"presNo");
		if(CommonUtils.isNull(presNo))return  null;
		String sql="select * from ex_pres_occ where pres_no=?";
		List<Map<String,Object>> resList=DataBaseHelper.queryForList(sql,new Object[]{presNo});
		if(resList==null || resList.size()==0)return null;
		return resList.get(0);
	}
    
    /**
     * 退药查询-查询退药处方以及明细
     * 交易号:022003022013
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryPresReturnDrugs(String param,IUser user){
    	Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
    	//处方查询
    	List<Map<String,Object>> presList = ipPdDeDrugBaMapper.queryPresOccList(paramMap);
    	List<Map<String,Object>> presDtList = new ArrayList<>();
    	List<String> presOccList = new ArrayList<>();
    	if(null != presList && !presList.isEmpty()) {
    		presList.forEach( pres->{
    			presOccList.add(MapUtils.getString(pres, "pkPresocc"));
    		});
        	paramMap.put("pkPresoccs", presOccList);
        	//处方明细查询
        	presDtList = ipPdDeDrugBaMapper.qryPresDt(paramMap);
        	presDtList.forEach(dt ->{
        		presList.forEach(occ ->{
        			if(MapUtils.getString(dt, "pkPresocc").equals(MapUtils.getString(occ, "pkPresocc"))){
    					dt.put("exPresOcc", occ);
    				}
    			});
    		});
    	}
    	return presDtList;
    }
    
	/**
	 * 查询患者信息
	 * 交易号:022003022014
	 * @param param{"pkDept":"发药药房","winno":"药房窗口","type":"0未完成，1已完成，2暂挂"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPiInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		List<Map<String,Object>> piInfoList=new ArrayList<Map<String,Object>>(10);
		Integer type=Integer.parseInt(paramMap.get("type").toString());
		
		if(!"1".equals(paramMap.get("change"))){//前台根据是否选择有效期
			String evDay=ApplicationUtils.getSysparam("CN0004", false);
			if(!CommonUtils.isEmptyString(evDay)){
				Date dateUp=DateUtils.getDateMorning(new Date(), 0);
				int numDay=1-CommonUtils.getInt(evDay);
				String datePres=DateUtils.addDate(dateUp, numDay, 3, "yyyy-MM-dd HH:mm:ss");
				paramMap.put("datePres", datePres);
			}
		}
		switch (type) {
		case 0://未完成
			piInfoList=ipPdDeDrugBaMapper.qryUnFinishedPiInfo(paramMap);
			break;
		case 1://已完成
			piInfoList=ipPdDeDrugBaMapper.qryFinishedPiInfo(paramMap);
			break;
		case 2://暂挂
			piInfoList=ipPdDeDrugBaMapper.qryPendingPiInfo(paramMap);	
			break;
		default:
			break;
		}
		return piInfoList;
	}
	
	/**
	 * 根据pkpi集合查询过敏信息
	 * 交易号:022003022015
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPiAllergicNameAll(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> piAllergicList = ipPdDeDrugBaMapper.qryPiAllergicNameAllInfo(paramMap);
		List<Map<String,Object>> paramList = new ArrayList<>();
		piAllergicList.forEach(All ->{
			boolean noExist = true;
			for (Map<String, Object> map : paramList) {
				if(MapUtils.getString(map, "pkPv").equals(MapUtils.getString(All, "pkPv"))) {
					noExist = false;
					String name = MapUtils.getString(map, "nameAl")+";"+MapUtils.getString(map, "nameAl");
					map.put("nameAl", name);
					break;
				}
			}
			if(noExist) {
				paramList.add(All);
			}
		});
		return paramList;
	}
}
