package com.zebone.nhis.task.scm.service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.task.pub.support.WSUtil;
import com.zebone.nhis.task.reg.dao.SughRegOrderMapper;
import com.zebone.nhis.task.scm.entity.MidDrugstoreUploadHis;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 深大药库药品库存定时上传到药监平台-定时任务
 *
 * @author xia_jiancheng
 */
@Service
public class SughDrugStockUploadTaskService {

    @Resource
    private SughRegOrderMapper regOrderMapper;

    private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");

    /**
     * 深大药库药品库存定时上传到药监平台主方法
     *
     * @param cfg
     */
    public void uploadDrugStockInfoTask(QrtzJobCfg cfg) {
        doUpload(cfg);
    }

    /**
     * 库存查询及上传
     *
     * @param cfg
     */
    @SuppressWarnings("unchecked")
	private void doUpload(QrtzJobCfg cfg) {
    	logger.info("【药监平台库存上传】-开始定时任务");
        //Map<String, Object> paramMap = new HashMap<>();
        Date curDate = new Date();
        //String dateStart = DateUtils.formatDate(curDate, "yyyyMMddHHmmss");
        String kcrq = DateUtils.addDate(curDate, -1, 3, "yyyy-MM-dd");//前一日        
        
        //准备数据
        String pkStore = ApplicationUtils.getPropertyValue("scm.sugh.yjpt.ykPkStore", "");//"d95621f4c26b4e01b9f4873b5ee9110d";//要放入配置
        String ptdm = ApplicationUtils.getPropertyValue("scm.sugh.yjpt.ptdm", "");//要放入配置
        String yybm = ApplicationUtils.getPropertyValue("scm.sugh.yjpt.yybm", "");//要放入配置
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(ps.amount_cost) as amount_cost_real,SUM(ps.quan_min) as quan_min_real,bp.code ");
        sb.append("FROM PD_STOCK ps ");
        sb.append("INNER JOIN bd_pd bp ON ps.pk_pd = bp.pk_pd ");
        sb.append("WHERE  ps.PK_STORE = ? ");
        sb.append("and bp.del_flag = '0' ");
        sb.append("GROUP  BY bp.code");
        String sql = sb.toString();
		List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql, new Object[]{pkStore});
		if(mapList == null || mapList.size() == 0)
		{
			logger.info("【药监平台库存上传】-药库药品无数据，不用上传.");
			return;
		}
		//查询对照表数据，获取药品在药监系统的编码	
		sql = "SELECT bp.code,rsp.ypbm FROM reg_szyj_pdmap rsp INNER JOIN bd_pd bp ON bp.pk_pd = rsp.pk_pd where bp.del_flag='0'";
		List<Map<String, Object>> mapYpbmList = DataBaseHelper.queryForList(sql, new Object[]{});
		if(mapYpbmList == null || mapYpbmList.size() == 0)
		{
			logger.info("【药监平台库存上传】-reg_szyj_pdmap对照表无数据，无法获取药品在药监平台上的药品编码");
			return;
		}
		//list转map,方便通过code找到ypbm
		Map<String, String> mapYpbm = new HashMap<String, String>();
        for (Map<String, Object> itemMap : mapYpbmList) {
        	String key = (String)itemMap.get("code");
        	if(key != null && !"".equals(key))
        	{
        		if(!mapYpbm.containsKey(key))
        		{
        			mapYpbm.put(key, (String)itemMap.get("ypbm"));
        		}
        	}
        }
        //补充值-ypbm
        String ypbm = "";
        int jls = 0;
        //补充值-上次的库存和价格,查履历表确定
        sql = "select CODE,AMOUNT_COST,QUAN_MIN from MID_DRUGSTORE_UPLOAD_HIS where date_upload = (select max(date_upload) as date_upload from MID_DRUGSTORE_UPLOAD_HIS)";
		List<Map<String, Object>> mapLastValueList = DataBaseHelper.queryForList(sql, new Object[]{});
		if(mapLastValueList == null || mapLastValueList.size() == 0)
		{
			logger.info("【药监平台库存上传】-无上次上传记录,期初库存数量和期初库存金额设为当前值");
	        for (Map<String, Object> item : mapList) 
			{    
	        	ypbm = "";
				if(mapYpbm.containsKey((String)item.get("code")))
				{
					ypbm = mapYpbm.get((String)item.get("code"));
				}
				item.put("ypbm", ypbm);
				if(!"".equals(ypbm))
				{
					jls++;
				}
				else
				{
					logger.info("【药监平台库存上传】-药库药品code:" + (String)item.get("code") + "在reg_szyj_pdmap表无对应药品编码.");
				}
				item.put("amountCostRealLast", item.get("amountCostReal"));//设置期初库存金额
				item.put("quanMinRealLast", item.get("quanMinReal"));//设置期初库存数量
	        }
		}
		else
		{
			//list转map,方便通过code找到对应对象
			Map<String, Object> mapLastValue = new HashMap<String, Object>();
			for (Map<String, Object> itemMap : mapLastValueList) {
            	String key = (String)itemMap.get("code");
            	if(key != null && !"".equals(key))
            	{
            		if(!mapLastValue.containsKey(key))
            		{
            			mapLastValue.put(key, itemMap);
            		}
            	}
            }
			//补充值
			for (Map<String, Object> item : mapList) 
			{    
	        	ypbm = "";
				if(mapYpbm.containsKey((String)item.get("code")))
				{
					ypbm = mapYpbm.get((String)item.get("code"));
				}
				item.put("ypbm", ypbm);
				if(!"".equals(ypbm))
				{
					jls++;
				}
				else
				{
					logger.info("【药监平台库存上传】-药库药品code:" + (String)item.get("code") + "在reg_szyj_pdmap表无对应药品编码.");
				}
				if(mapLastValue.containsKey((String)item.get("code")))
				{
					Map<String, Object> lastValueItemMap = (Map<String, Object>)mapLastValue.get((String)item.get("code"));
					item.put("amountCostRealLast", lastValueItemMap.get("amountCost"));//设置期初库存金额
					item.put("quanMinRealLast", lastValueItemMap.get("quanMin"));//设置期初库存数量
				}
				else
				{
					item.put("amountCostRealLast", item.get("amountCostReal"));//设置期初库存金额
					item.put("quanMinRealLast", item.get("quanMinReal"));//设置期初库存数量
				}
	        }
		}
		if(jls == 0)
		{
			logger.info("【药监平台库存上传】-药库所有药品在药监平台上无对应ypbm，不用上传.");
			return;
		}
        //转换成json begin,暂用拼接字符串形式实现
		//采用分批上传模式,每批最多100条
		int UPLOAD_PERSIZE = 100;
		int nCount = jls / UPLOAD_PERSIZE;
		if(nCount * UPLOAD_PERSIZE < jls)
		{
			nCount++;
		}
		//去除ypbm为空的数据
		for(int k=0; k<mapList.size(); k++)
		{
			Map<String, Object> item = mapList.get(k);
			ypbm = (String)item.get("ypbm");
			if("".equals(ypbm))
			{
				mapList.remove(k);
				k--;
			}
		}
		int ncurJls = 0;
		List<MidDrugstoreUploadHis> addList = new ArrayList<MidDrugstoreUploadHis>();
		
		for(int i=0; i<nCount; i++)
		{
			sb = new StringBuilder();
			sb.append("{");
			sb.append("\"ptdm\":\"" + ptdm + "\",");
			sb.append("\"yybm\":\"" + yybm + "\",");
			sb.append("\"kcrq\":\"" + kcrq + "\",");
			if(i == (nCount -1))
			{
				ncurJls = jls;
			}
			else
			{
				ncurJls =  UPLOAD_PERSIZE;
			}
			sb.append("\"jls\":\"" + ncurJls + "\",");
			jls -= UPLOAD_PERSIZE;
			
			sb.append("\"mx\":[");
			int sxh = 0;
			for (int j=i*UPLOAD_PERSIZE; j<mapList.size(); j++) 
			{
				Map<String, Object> item = mapList.get(j);
				ypbm = (String)item.get("ypbm");
				sxh++;
				StringBuilder subSb = new StringBuilder();
				if(sxh > 1)
				{
					subSb.append(",");
				}
				subSb.append("{");
				subSb.append("\"sxh\":\"" + sxh + "\",");
				subSb.append("\"ypbm\":\"" + ypbm + "\",");
				BigDecimal quanMinRealLast = (BigDecimal)item.get("quanMinRealLast");//期初库存数量
				double d1 = quanMinRealLast.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
				BigDecimal amountCostRealLast = (BigDecimal)item.get("amountCostRealLast");//期初库存金额
				double d2 = amountCostRealLast.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
				BigDecimal quanMinReal = (BigDecimal)item.get("quanMinReal");//期末库存数量
				double d3 = quanMinReal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
				BigDecimal amountCostReal = (BigDecimal)item.get("amountCostReal");//期末库存金额
				double d4 = amountCostReal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
				subSb.append("\"qckcsl\":\"" + String.valueOf(d1) + "\",");
				subSb.append("\"qckcje\":\"" + String.valueOf(d2) + "\",");
				subSb.append("\"qmkcsl\":\"" + String.valueOf(d3) + "\",");
				subSb.append("\"qmkcje\":\"" + String.valueOf(d4) + "\"");
				subSb.append("}");
				sb.append(subSb.toString());

	        	MidDrugstoreUploadHis midDrugstoreUploadHis = new MidDrugstoreUploadHis();
	        	midDrugstoreUploadHis.setPkMidDrugstoreUploadHis(NHISUUID.getKeyId());
	        	midDrugstoreUploadHis.setCode((String)item.get("code"));
	        	midDrugstoreUploadHis.setAmountCost(((BigDecimal)item.get("amountCostReal")).doubleValue());
	        	midDrugstoreUploadHis.setQuanMin(((BigDecimal)item.get("quanMinReal")).doubleValue());
	        	midDrugstoreUploadHis.setDateUpload(curDate);
				addList.add(midDrugstoreUploadHis);
				
				if(sxh == ncurJls)
				{
					break;
				}
	        }
			sb.append("]");
			sb.append("}");
			String json = sb.toString();
			logger.info("【药监平台库存上传】-ws json入参" + (i+1) + "/" + nCount + ":" + json);
			//转换成json end
			String iocode = ApplicationUtils.getPropertyValue("scm.sugh.yjpt.iocode", "");
	        String wsUrl = ApplicationUtils.getPropertyValue("scm.sugh.yjpt.drugstore.ws.url", "");
	        String dataType = "1";//json
	        String resJson = "";
	        try {
	            String sign = getMessageDigest(iocode+json, "SHA-1");
	            Object[] resObj = (Object[])WSUtil.invoke(wsUrl, "send", sign, dataType, json);
	            resJson = resObj[0].toString();
	    		logger.info("【药监平台库存上传】-ws 返回值" + (i+1) + "/" + nCount + ":" + resJson);
	        }catch (Exception e) {
	        	logger.error("【药监平台库存上传】调用药监平台药库药品库存上传接口失败", e);
	        	throw new BusException("【药监平台库存上传】调用药监平台药库药品库存上传接口失败");
	        }
	
	        //解析返回值,成功时,记录到履历表
	        JSONObject jsonObject = JSONObject.parseObject(resJson);
	        String retValue = jsonObject.getString("success");
	        if("true".equals(retValue))
	        {				
		    	logger.info("【药监平台库存上传】-成功" + (i+1) + "/" + nCount ); 
	        }
	        else
	        {
		    	logger.info("【药监平台库存上传】-失败" + (i+1) + "/" + nCount + ",msg:" + jsonObject.getString("msg")); 
	        	throw new BusException("\"【药监平台库存上传】-返回失败");
	        }
		}
		if(addList.size() > 0)
		{			
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(MidDrugstoreUploadHis.class), addList);
		}
        
    	logger.info("【药监平台库存上传】-结束定时任务");        
    }

    //字节数组转换为 16 进制的字符串
    private String byteArrayToHex(byte[] byteArray) {
	    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F'};
	    char[] resultCharArray = new char[byteArray.length * 2];
	    int index = 0;
	    for (byte b : byteArray) {
		    resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
		    resultCharArray[index++] = hexDigits[b & 0xf];
	    }
	    return new String(resultCharArray);
	}
	    
    //计算消息摘要
    private String getMessageDigest(String strOriginal, String encName) {
	    byte[] digest = null;
	    if (encName == null || "".equals(encName)) {
	    	encName = "SHA-1";
	    }
	    try {
		    MessageDigest md = MessageDigest.getInstance(encName);
		    md.update(strOriginal.getBytes("UTF-8"));
		    digest = md.digest();
	    } 
	    catch (NoSuchAlgorithmException e)
	    {
	    	logger.error("", e);
	    }
	    catch(UnsupportedEncodingException e){
	    	logger.error("", e);
	    }
	    return byteArrayToHex(digest);
    }

}
