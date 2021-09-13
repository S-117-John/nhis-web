package com.zebone.nhis.ma.pub.syx.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.syx.vo.TExamineItemSetListForIP;
import com.zebone.nhis.ma.pub.syx.vo.TExamineRequestForIP;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * lis系统业务处理服务(无事物)
 * @author yangxue
 *
 */
@Service
public class LisSystemHandler {
	
	@Autowired
	private LisSystemService lisSysService;

	/**
	 * 业务处理方法转换器
	 * @param methodName
	 * @param args
	 */
    public void invokeMethod(String methodName,Object...args){
    	switch(methodName){
    	case "saveLisApply":
    		this.sysLisApplyAndDt(args);
    		break;
    	case "delLisApply":
    		this.delLisApplyAndDt(args);
    		break;
    	}
    }
    
	/**
	 * 同步检验申请信息
	 */
	public void sysLisApplyAndDt(Object...args){
		List<String> pkCnords = null;
		pkCnords = (List<String>)args[0];// 医嘱主键
		if(null == pkCnords || pkCnords.size() < 1) return;
		try 
		{
			//获取传入的门诊标志
			boolean isOp = false;
			if(args.length > 1){
				isOp = (boolean)args[1];
			}
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("pkCnords", pkCnords);
			List<TExamineRequestForIP> appList;
			if(isOp){
				appList = lisSysService.queryOpLisAppList(map);
			}else{
				appList = lisSysService.queryLisAppList(map);				
			}
			List<TExamineItemSetListForIP> appDtList = lisSysService.queryLisFeeList(map);
					
			//1、切换至lis库
			if(isOp){
				//门诊
				DataSourceRoute.putAppId("syxlisreqop");
			}else{
				DataSourceRoute.putAppId("syxlisreq");				
			}
			//调用Service类保存数据并提交事务
			lisSysService.sysLisApplyAndDt(appList, appDtList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusException("数据上传至检查系统失败！\n错误信息：" + e.getMessage());
		}finally{
			DataSourceRoute.putAppId("default");//切换数据源
		}
		//2、更新申请单状态
		lisSysService.updateLabAppStatus(pkCnords);
	}
	
	/**
	 * 检验申请信息退费
	 */
	public void delLisApplyAndDt(Object...args){
		List<String> pkCnords = null;
		pkCnords = (List<String>)args[0];// 医嘱主键
		if(null == pkCnords || pkCnords.size() < 1) return;
		try 
		{
			//获取传入的门诊标志
			boolean isOp = false;
			if(args.length > 1){
				isOp = (boolean)args[1];
			}
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("pkCnords", pkCnords);
			
			List<TExamineRequestForIP> appList;
			if(isOp){
				appList = lisSysService.queryOpLisAppList(map);
			}else{
				appList = lisSysService.queryLisAppList(map);				
			}
			
			if(null == appList || appList.size() < 1) return;
			
			map.put("pkEmp", UserContext.getUser().getPkEmp());
			String empOldId = lisSysService.queryOldIdByPkEmp(map);
			if(CommonUtils.isEmptyString(empOldId))
				throw new BusException("未获取到当前操作人对应旧系统的人员ID");
			Long empId = Long.parseLong(empOldId);
			
			//1、切换至lis库         
			if(isOp){
				//门诊
				DataSourceRoute.putAppId("syxlisreqop");
			}else{
				DataSourceRoute.putAppId("syxlisreq");				
			}

			//调用Service类保存数据并提交事务
			lisSysService.delLisAppFromLis(appList,empId);//删除
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusException("从三方系统撤销检验申请失败，\n" + e.getMessage());
		}finally{
			DataSourceRoute.putAppId("default");//切换数据源
		}
	}
}
