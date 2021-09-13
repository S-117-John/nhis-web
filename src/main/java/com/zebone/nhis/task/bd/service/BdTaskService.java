package com.zebone.nhis.task.bd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.task.bd.dao.BdTaskDao;
import com.zebone.nhis.task.bd.vo.InvCheckVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
/**
 * 校验基础数据完整性定时任务
 * @author 李继鹏
 *
 */
@Service
public class BdTaskService {

	private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	@Autowired
	private BdTaskDao bdTaskDao;
	
	/**
     * 启用定时任务放入
     * @param cfg
     */
    public void checkBdTask(QrtzJobCfg cfg){
    	try{
    		checkBdInfo(cfg);
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * 1.监控是否有票据项目没有设置费用分类
     * 2.监控基础数据字典的必填项是否有值  医嘱字典、收费项目字典、药品字典
     * @param cfg
     */
    public void checkBdInfo(QrtzJobCfg cfg){
    	logger.info("================调用校验基础数据完整性定时任务开始================");
    	
    	//短信提示内容
    	StringBuffer warnMsg = new StringBuffer("");
    	
    	/**1.查询是否有票据项目没有设置费用分类*/
    	String invChkStr = checkInvCateInfo();
    	if(!CommonUtils.isEmptyString(invChkStr.toString())){
			logger.info("监控票据票据项目数据结果：{}",invChkStr.toString());
			warnMsg.append(invChkStr);
		}
    	
    	/**2.监控基础数据字典的必填项是否有值  医嘱字典、收费项目字典、药品字典*/
    	//2.1查询收费项目字典数据完整性
    	String itemChkStr = checkBdItemInfo();
    	if(!CommonUtils.isEmptyString(itemChkStr.toString())){
			logger.info(itemChkStr.toString());
			warnMsg.append("---------------------------------");
			warnMsg.append(itemChkStr);
		}
    	
    	//2.2查询医嘱字典数据完整性
    	String ordChkStr = checkBdOrdInfo();
    	if(!CommonUtils.isEmptyString(ordChkStr.toString())){
			logger.info(ordChkStr.toString());
			warnMsg.append("---------------------------------");
			warnMsg.append(ordChkStr);
		}
    	
    	//2.3查询药品字典数据完整性
    	String pdChkStr = checkBdPdInfo();
    	if(!CommonUtils.isEmptyString(pdChkStr.toString())){
			logger.info(pdChkStr.toString());
			warnMsg.append("---------------------------------");
			warnMsg.append(pdChkStr);
		}
    	logger.info("================调用校验基础数据完整性定时任务结束================");
    	
    	//发送消息
    	if(!CommonUtils.isEmptyString(warnMsg.toString())){
    		Map<String,Object> paramMap = new HashMap<>(16);
    		paramMap.put("content", warnMsg.toString());
			paramMap.put("type", "JCSJJY");
			PlatFormSendUtils.sendCallPayMsg(paramMap);
			
			//如果有未维护完整的数据则抛出异常
			throw new BusException(warnMsg.toString());
    	}
    }
	
    /**
     * 查询是否有票据项目没有设置费用分类
     * @return
     */
    private String checkInvCateInfo(){
    	StringBuffer invChkStr = new StringBuffer("");
    	List<InvCheckVo> invChkList = bdTaskDao.checkInvCateInfo();
    	if(invChkList!=null && invChkList.size()>0){
    		for(int i=0; i<invChkList.size(); i++){
    			
    			if(i==0 ||
    					invChkStr.indexOf(invChkList.get(i).getInvName())==-1){
    				invChkStr.append(String.format("票据分类[%s]未维护的费用分类：",
    						invChkList.get(i).getInvName()));
    			}
    			
    			invChkStr.append(String.format("%s(编码:%s)",
    					invChkList.get(i).getName(), invChkList.get(i).getCode()));
    			
    			if(i==invChkList.size()-1 ||
    					(i+1<invChkList.size() && !invChkList.get(i).getInvName().equals(invChkList.get(i+1).getInvName()))
    				){
    				invChkStr.append(";");
    			}else{
    				invChkStr.append("、");
    			}
    		}
    		
    	}
    	return invChkStr.toString();
    }
    
    /**
     * 查询收费项目字典数据完整性
     * @return
     */
    private String checkBdItemInfo(){
    	List<BdItem> itemList = bdTaskDao.checkBdItemInfo();
    	StringBuffer itemStr = new StringBuffer("");
    	if(itemList!=null && itemList.size()>0){
    		for(int i=0; i<itemList.size(); i++){
    			
    			if(i==0){
    				itemStr.append("监控收费项目数据结果：");
    			}
    			
    			itemStr.append(String.format("名称:%s(编码:%s):", itemList.get(i).getName(),itemList.get(i).getCode()));
    			
    			if(CommonUtils.isEmptyString(itemList.get(i).getCode())){
    				itemStr.append("[项目编码]");
    			}
    			
    			if(CommonUtils.isEmptyString(itemList.get(i).getName())){
    				itemStr.append("[项目名称]");
    			}
    			
    			if(CommonUtils.isEmptyString(itemList.get(i).getPkUnit())){
    				itemStr.append("[单位]");
    			}
    			
    			if(CommonUtils.isEmptyString(itemList.get(i).getSpcode())){
    				itemStr.append("[拼音码]");
    			}
    			
    			if(CommonUtils.isEmptyString(itemList.get(i).getDtItemtype())){
    				itemStr.append("[项目类型]");
    			}
    			
    			if(CommonUtils.isEmptyString(itemList.get(i).getPkItemcate())){
    				itemStr.append("[费用分类]");
    			}
    			
    			if(CommonUtils.isEmptyString(itemList.get(i).getDtChcate())){
    				itemStr.append("[病案分类]");
    			}
    			
    			if(CommonUtils.isEmptyString(itemList.get(i).getEuPricemode())){
    				itemStr.append("[定价模式]");
    			}
    			
    			if(CommonUtils.isEmptyString(itemList.get(i).getCodeHp())){
    				itemStr.append("[上传编码]");
    			}
    			
    			itemStr.append("未维护;");
    		}
    	}
    	
    	return itemStr.toString();
    }
    
    /**
     * 查询医嘱字典数据完整性
     * @return
     */
    private String checkBdOrdInfo(){
    	List<BdOrd> ordList = bdTaskDao.checkBdOrdInfo();
    	StringBuffer ordStr = new StringBuffer("");
    	if(ordList!=null && ordList.size()>0){
    		for(int i=0; i<ordList.size(); i++){
    			if(i==0){
    				ordStr.append("监控医嘱项目数据结果：");
    			}
    			
    			ordStr.append(String.format("名称:%s(编码:%s):", ordList.get(i).getName(),ordList.get(i).getCode()));
    			
    			if(CommonUtils.isEmptyString(ordList.get(i).getCode())){
    				ordStr.append("[编码]");
    			}
    			
    			if(CommonUtils.isEmptyString(ordList.get(i).getName())){
    				ordStr.append("[名称]");
    			}
    			
    			if(CommonUtils.isEmptyString(ordList.get(i).getCodeOrdtype())){
    				ordStr.append("[类型]");
    			}
    			
    			if(CommonUtils.isEmptyString(ordList.get(i).getSpcode())){
    				ordStr.append("[助记码]");
    			}
    			
    			if(CommonUtils.isEmptyString(ordList.get(i).getDtOrdcate())){
    				ordStr.append("[医嘱分类]");
    			}
    			
    			if(CommonUtils.isEmptyString(ordList.get(i).getEuSex())){
    				ordStr.append("[使用性别]");
    			}
    			
    			ordStr.append("未维护;");
    		}
    	}
    	
    	return ordStr.toString();
    }
    
    /**
     * 查询药品字典数据完整性
     * @return
     */
    private String checkBdPdInfo(){
    	List<BdPd> pdList = bdTaskDao.checkBdPdInfo();
    	StringBuffer pdStr = new StringBuffer("");
    	if(pdList!=null && pdList.size()>0){
    		for(int i=0; i<pdList.size(); i++){
    			if(i==0){
    				pdStr.append("监控药品字典数据结果：");
    			}
    			
    			pdStr.append(String.format("名称:%s(编码:%s):", pdList.get(i).getName(),pdList.get(i).getCode()));
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getCode())){
    				pdStr.append("[药品编码]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getName())){
    				pdStr.append("[商品名称]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getSpec())){
    				pdStr.append("[药品规格]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getDtDosage())){
    				pdStr.append("[药品剂型]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getDtPdtype())){
    				pdStr.append("[物品类别]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getSpcode())){
    				pdStr.append("[拼音码]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getPkFactory())){
    				pdStr.append("[生产厂商]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getEuDrugtype())){
    				pdStr.append("[药品类别]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getPkUnitMin())){
    				pdStr.append("[基本单位]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getPkUnitPack())){
    				pdStr.append("[包装单位]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getEuSex())){
    				pdStr.append("[适用年龄]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getEuPdprice())){
    				pdStr.append("[计价模式]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getPkUnitVol())){
    				pdStr.append("[医疗含量]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getDtAnti())){
    				pdStr.append("[抗菌药物]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getEuUsecate())){
    				pdStr.append("[用法分类]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getDtPois())){
    				pdStr.append("[毒麻分类]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getDtAbrd())){
    				pdStr.append("[来源分类]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getPkItemcate())){
    				pdStr.append("[费用分类]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getDtChcate())){
    				pdStr.append("[病案分类]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getPkOrdtype())){
    				pdStr.append("[医嘱类型]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getEuMuputype())){
    				pdStr.append("[取整模式]");
    			}
    			
    			if(CommonUtils.isEmptyString(pdList.get(i).getCodeHp())){
    				pdStr.append("[医保上传编码]");
    			}
    			pdStr.append("未维护;");
    		}
    	}
    	
    	return pdStr.toString();
    }
    
    
}
