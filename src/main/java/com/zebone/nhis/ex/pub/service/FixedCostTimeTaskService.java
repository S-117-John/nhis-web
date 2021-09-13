package com.zebone.nhis.ex.pub.service;

import java.util.*;
import java.util.concurrent.*;

import javax.annotation.Resource;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ex.pub.support.ExTaskLog;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.ex.pub.vo.DeptCgItemVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.exception.BusException;

/**
 * 固定计费定时任务--调用入口使用父类的exec方法
 * @author yangxue
 *
 */
@Service
public class FixedCostTimeTaskService extends TimeTaskService{

	private static Logger logger = LoggerFactory.getLogger(FixedCostTimeTaskService.class);

	@Resource
	private FixedCostService fixedCostService;

	private volatile String exeStrageVal;
	private static ThreadLocal<String> localPkpi = new ThreadLocal<>();
	/**
	 * 固定计费执行方法
	 */
	@Override
	protected String execute(String pk_org, String pk_dept) throws BusException {
		List<Map<String,Object>> list = this.getPvs(pk_org,pk_dept);
		ExTaskLog.log("开始计费任务：",list);
		//获取pv集合
		if(null == list||list.size()<=0){
			return null;
		}
		//获取pv集合
		Set<String> pk_pvs = new HashSet<String>();
		for(Map<String,Object> pvMap : list){
			String pk_pv = (String)pvMap.get("pkPv");
			pk_pvs.add(pk_pv);
		}
		ExTaskLog.log("得到就诊记录：",pk_pvs);
		if(pk_pvs==null||pk_pvs.size()<=0)
			return null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkPvs", pk_pvs);
		map.put("pkDeptNs", pk_dept);
		map.put("pkOrg", pk_org);
		User u = new User();
		u.setPkDept(pk_dept);
		u.setPkOrg(pk_org);
		u.setPkEmp(null);
		ExTaskLog.log("查询入参：",map,u);
		List<DeptCgItemVo> cgList = fixedCostService.execFixedCharge(map, true, u,null);
		logger.info("定时计费构造出计费条目：{}",cgList==null?0:cgList.size());
		if(cgList == null || cgList.size() == 0){
			return null;
		}
		//updateCgList(cgList,pk_org,pk_dept);
		//允许欠费记费
		return fixedCostService.saveFixedCost(pk_dept, 0, cgList,true,true,null);
	}
	
	@Override
	public Map<String, String> exec(String pk_org) {
		exeStrageVal = ApplicationUtils.getSysparam("EX0060", false);
		//保持原来逻辑不变
		if(StringUtils.isBlank(exeStrageVal) || EnumerateParameter.ZERO.equals(exeStrageVal)){
			return super.exec(pk_org);
		}
		return execByConfig(pk_org);
	}


	public Map<String, String> execByConfig(final String pkOrg) {
		Map<String, String> resultMap = Maps.newHashMap();
		StringBuilder msg = new StringBuilder();
		try{
			List<Map<String, Object>> listDept =  getNsDepts(pkOrg);
			if(CollectionUtils.isEmpty(listDept)){
				throw new BusException("没有查到病区集合信息");
			}

			List<Map<String, Object>> iterator = null;
			//按病区提交、回滚
			if(EnumerateParameter.ONE.equals(exeStrageVal)){
				logger.info("定时计费检索到病区数据条目：{}",listDept.size());
				iterator = listDept;
			} else if(EnumerateParameter.TWO.equals(exeStrageVal)){
			//按照患者提交,
				List<Map<String,Object>> piList = getAllPatiList(pkOrg);
				if(CollectionUtils.isEmpty(piList)){
					throw new BusException("没有获取到符合条件的患者信息！");
				}
				logger.info("定时计费检索到患者数据条目：{}",piList.size());
				iterator = piList;
			} else {
				throw new BusException("暂不支持系统参数配置值：EX0060！");
			}

			submit(pkOrg, msg, iterator);
			if(StringUtils.isNotBlank(msg)){
				resultMap.put("customStatus", StringUtils.substring(msg.toString(),0,3800));
			}
		} catch (Exception e){
			logger.error("调用计费服务execOfDept其他异常：",e);
			resultMap.put("msg",e.getMessage());
		}
		return resultMap;
	}

	private void submit(final String pkOrg, StringBuilder msg, List<Map<String, Object>> iterator) {
		ExecutorService executorService = Executors.newFixedThreadPool((Runtime.getRuntime().availableProcessors()/2));
		List<Callable<String>> listCall = new ArrayList<>();
		try{
			for (final Map<String,Object> mapNext:iterator){
				listCall.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						String str = "";
						String pkDept = MapUtils.getString(mapNext, "pkDept",MapUtils.getString(mapNext,"pkDeptNs"));
						String pkPi = MapUtils.getString(mapNext,"pkPi");
						if(StringUtils.isNotBlank(pkPi)){
							localPkpi.set(pkPi);
						}
						try{
							User u = new User();
							u.setPkDept(pkDept);
							u.setPkOrg(pkOrg);
							u.setPkEmp(null);
							UserContext.setUser(u);
							FixedCostTimeTaskService service = ServiceLocator.getInstance().getBean(FixedCostTimeTaskService.class);
							str = service.execute(pkOrg,pkDept);
						} catch (Exception e){
							str = MapUtils.getString(mapNext, "nameDept");
							String name = MapUtils.getString(mapNext, "namePi");
							if(StringUtils.isBlank(name)){
								str+= ":"+name;
							}
							logger.error("执行异常：{},{}",MapUtils.getString(mapNext, "nameDept"),MapUtils.getString(mapNext, "namePi"),e);
						}
						logger.info("定时计费本次执行已经提交{},{},{}",Thread.currentThread().getName(),MapUtils.getString(mapNext, "nameDept"),MapUtils.getString(mapNext, "namePi"));
						return str;
					}
				});
			}
			List<Future<String>> futureList = executorService.invokeAll(listCall);
			for(int i =0;i<futureList.size();i++){
				Future<String> future = futureList.get(i);
				String str = "";
				try {
					str = future.get(60, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.error("调用计费服务异常：",e);
					Map<String,Object> mapNext = iterator.get(i);
					str = MapUtils.getString(mapNext, "nameDept");
					String name = MapUtils.getString(mapNext, "namePi");
					if(StringUtils.isBlank(name)){
						str+= ":"+name;
					}
					str += e.getMessage();
				}
				if(StringUtils.isNotBlank(str)){
					msg.append(str).append("|");
				}
			}
		} catch (Exception e){
			logger.error("调用计费服务execOfDept其他异常：",e);
			throw new BusException(e.getMessage());
		} finally {
			executorService.shutdownNow();
		}
	}

	public List<Map<String,Object>> getPvs(String pkOrg, String pkDept){
		/**
		 * 为了保持统一不想改execute方法入参，这里使用线程本地变量来传参。
		 * 对于按照科室、患者来多线程提交和查询的，并未使用本地缓存，考虑到基本是空闲时间执行，每次都从数据库重新获取了
		 */

		return getPvList(pkOrg, pkDept,localPkpi.get(),null);
	}

}
