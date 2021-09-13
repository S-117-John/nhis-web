package com.zebone.nhis.labor.nis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.support.ExListSortByOrdUtil;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.labor.nis.dao.PvLaborExListMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 产房取消医嘱执行
 * @author yangxue
 *
 */
@Service
public class PvLaborExListCancelService {
	 @Resource
	 private PvLaborExListMapper pvLaborExListMapper;
	 @Resource
	 private BlCgExPubService blCgExPubService;
	 /**
	  * 查询医嘱执行单列表
	  * @param param{dateBegin,cancelFlag,dateEnd,nameOrd,pkPvs,pkDeptNs}
	  * @param user
	  * @return
	  */
	 public List<ExlistPubVo> queryExlist(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 String  dateBegin = CommonUtils.getString(map.get("dateBegin"));
		 if(dateBegin == null){
			 map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
		 }else{
			 map.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		 }
		 String  dateEnd = CommonUtils.getString(map.get("dateEnd"));
		 if(dateEnd == null){
			 map.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		 }else{
			 map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		 }
		 //设置为取消执行功能点标志
		 map.put("cancelFlag", "1");
		 List<ExlistPubVo> result =  pvLaborExListMapper.queryExecListByCon(map);
		 new ExListSortByOrdUtil().ordGroup(result);
		 return result;
	 }
	 /**
	  * 取消执行
	  * @param param
	  * @param user
	  */
	 public void cancelEx(String param,IUser user){
		 //前台将整行记录的数据都传回来
		 List<ExlistPubVo>  exList = JsonUtil.readValue(param, new TypeReference<List<ExlistPubVo>>(){});
		 //默认将药品医嘱设置为基数药，取消时退费
		 for(ExlistPubVo vo:exList){
			 if("1".equals(vo.getFlagDurg())){
				 vo.setFlagBase("1");
			 }
		 }
		 blCgExPubService.cancelExAndRtnCg(exList, (User)user);
	 }
	 
	 
}
