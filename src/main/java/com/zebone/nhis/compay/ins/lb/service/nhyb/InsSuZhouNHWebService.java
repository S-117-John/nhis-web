package com.zebone.nhis.compay.ins.lb.service.nhyb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiag;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhouNHWebDisMap;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhJsGrade;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhWebDetails;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhWebGrade;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhWebJs;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhWebJsfee;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhWebReginfo;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.lb.dao.nhyb.InsSuZhouNHWebMapper;
import com.zebone.nhis.compay.ins.lb.service.pub.dao.LbYbPubMapper;
import com.zebone.nhis.compay.ins.lb.service.pub.vo.InsSuzhounhWebDetailsVo;
import com.zebone.nhis.compay.ins.lb.service.pub.vo.PageInsSuzhounhWebJsListVo;
import com.zebone.nhis.compay.ins.lb.service.pub.vo.PageQryJSParam;
import com.zebone.nhis.compay.ins.lb.vo.nhyb.BdHpExPro;
import com.zebone.nhis.compay.ins.lb.vo.nhyb.InsNhybItemMap;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsSuZhouNHWebService {

	@Resource
	private LbYbPubMapper lbYbPubMapper;

	@Autowired
	private InsSuZhouNHWebMapper insSuZhouNHWebMapper;

	/**
	 * 标准保存宿州农合登记信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public InsSuzhounhWebReginfo saveReginfo(String param, IUser user) {
		InsSuzhounhWebReginfo regInfo = JsonUtil.readValue(param,
				InsSuzhounhWebReginfo.class);
		if (regInfo != null) {
			if (!StringUtils.isBlank(regInfo.getId())) {
				DataBaseHelper.updateBeanByPk(regInfo, false);
			} else {
				DataBaseHelper.insertBean(regInfo);
			}
		}
		return regInfo;
	}

	/**
	 * 按照PKPV查询宿州农合登记信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public InsSuzhounhWebReginfo getRegInfoByPkpv(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		List<InsSuzhounhWebReginfo> regInfoList = insSuZhouNHWebMapper
				.getRegInfoByPkPv(pkPv);
		if (regInfoList != null && regInfoList.size() > 0) {
			return regInfoList.get(0);
		} else {
			throw new BusException("查不到该患者的登记信息，无法获取数据！");
		}
	}

	/**
	 * 撤销宿州农合登记信息
	 * 
	 * @param param
	 * @param user
	 */
	public void deleteRegInfo(String param, IUser user) {
		InsSuzhounhWebReginfo regInfo = JsonUtil.readValue(param,
				InsSuzhounhWebReginfo.class);
		if (regInfo != null) {
			if (!StringUtils.isBlank(regInfo.getId())) {
				insSuZhouNHWebMapper.deleteRegInfoById(regInfo.getId());
			} else if (!StringUtils.isBlank(regInfo.getPkpv())) {
				insSuZhouNHWebMapper.deleteRegInfo(regInfo.getPkpv());
			}
		}
	}

	/**
	 * 015001004012 根据农合登记主键更改pkPv
	 * 
	 * @param param
	 * @param user
	 */
	public void createNhisWebOrNhybWebKind(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || paramMap.size() == 0)
			return;
		String sql = "update INS_SUZHOUNH_WEB_REGINFO set PKPV=? where del_flag='0' and id=?";
		DataBaseHelper.update(sql, new Object[] { paramMap.get("pkPv"),
				paramMap.get("id") });
	}

	/**
	 * 015001009005 查询nhis与农合医保匹配未匹配信息
	 * 
	 * @param param
	 *            {"pkHp":"医保主键","euMatch":"1(匹配)/2(未匹配)","type":"1(药品)/2(收费项目)"
	 *            }
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryNhItemAndPdWithInfo(String param,
			IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			return null;
		String euActive = "";
		if(paramMap.get("euActive") == null)
		{
			paramMap.put("euActive", 1);			
		}
		else{			
			euActive = paramMap.get("euActive").toString();
		}
		
		    
		List<Map<String, Object>> mapList = new ArrayList<>();
		if (paramMap.get("euMatch") != null
				&& "1".equals(paramMap.get("euMatch").toString())) {  //匹配
			if (paramMap.get("type") != null
					&& "1".equals(paramMap.get("type"))) {
				if (euActive.equals("1")) {// 启用
					paramMap.put("euActive", 0);
				} else {// 停用
					paramMap.put("euActive", 1);
				}				
				if(paramMap.get("checkState").toString()!= null && "-1".equals(paramMap.get("checkState").toString())) //全部
				{
					paramMap.put("checkState", "");					
				}
				mapList = insSuZhouNHWebMapper.qryNhPdWithInfo(paramMap);// 查询药品对照
			} else if (paramMap.get("type") != null
					&& "2".equals(paramMap.get("type"))) {
				
				if (euActive.equals("1")) {// 启用
					paramMap.put("euActive", 0);
				} else {// 停用
					paramMap.put("euActive", 1);
				}	
				if(paramMap.get("checkState").toString()!= null && "-1".equals(paramMap.get("checkState").toString())) //全部
				{
					paramMap.put("checkState", "");					
				}
				mapList = insSuZhouNHWebMapper.qryNhItemWithInfo(paramMap);// 查询项目对照
			}
			else if (paramMap.get("type") != null
					&& "3".equals(paramMap.get("type"))) {
				mapList = insSuZhouNHWebMapper.qryNhDisWithInfo(paramMap);// 查询疾病对照
			}
			
		} else if (paramMap.get("euMatch") != null
				&& "2".equals(paramMap.get("euMatch").toString())) {  //未匹配
			if (paramMap.get("type") != null
					&& "1".equals(paramMap.get("type"))) {
				if (euActive.equals("1")) {// 启用
					paramMap.put("euActive", 0);
				} else {// 停用
					paramMap.put("euActive", 1);
				}
				mapList = insSuZhouNHWebMapper.qryYbPdDicNoWithInfo(paramMap);
			} else if (paramMap.get("type") != null
					&& "2".equals(paramMap.get("type"))) {
				mapList = insSuZhouNHWebMapper.qryYbItemDicNoWithInfo(paramMap);
			}
			else if (paramMap.get("type") != null
					&& "3".equals(paramMap.get("type"))) {
				mapList = insSuZhouNHWebMapper.qryYbDisDicNoWithInfo(paramMap);
			}
		}
		return mapList;
	}

	/**
	 * 标准保存宿州农合web收费明细信息(需优化)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsSuzhounhWebDetails> saveDetails(String param, IUser user) {
		InsSuzhounhWebDetailsVo insSuzhounhWebDetailsVo = JsonUtil.readValue(
				param, InsSuzhounhWebDetailsVo.class);
		if (insSuzhounhWebDetailsVo != null
				&& insSuzhounhWebDetailsVo.getList() != null) {
			for (InsSuzhounhWebDetails insSuzhounhWebDetails : insSuzhounhWebDetailsVo
					.getList()) {
				ApplicationUtils.setDefaultValue(insSuzhounhWebDetails, true);			
			}
				DataBaseHelper.batchUpdate(
						DataBaseHelper.getInsertSql(InsSuzhounhWebDetails.class),
				insSuzhounhWebDetailsVo.getList());
		}
		return insSuzhounhWebDetailsVo.getList();
	}

	/**
	 * 保存宿州农合web结算信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public InsSuzhounhWebJs saveJsInfo(String param, IUser user) {			
		InsSuzhounhWebJs jsInfo = JsonUtil.readValue(param, InsSuzhounhWebJs.class);
		if (CommonUtils.isNotNull(jsInfo)) {
			if (!StringUtils.isBlank(jsInfo.getId())) {
				DataBaseHelper.updateBeanByPk(jsInfo, false);
			} else {
				DataBaseHelper.insertBean(jsInfo);
			}
			//分段
			if (CommonUtils.isNotNull(jsInfo.getGradelist())) {
				List<InsSuzhounhWebGrade> jsGradeList = jsInfo.getGradelist();
				for (int i = 0; i < jsGradeList.size(); i++) {
					ApplicationUtils.setDefaultValue(jsGradeList.get(i), true);
					jsGradeList.get(i).setPkJs(jsInfo.getId());
				}
				if (jsGradeList.size() > 0) {
					System.out.println(DataBaseHelper
							.getInsertSql(InsSuzhounhJsGrade.class));
					DataBaseHelper.batchUpdate(DataBaseHelper
							.getInsertSql(InsSuzhounhWebGrade.class),
							jsGradeList);
				}
			}
			//住院分类
			if (CommonUtils.isNotNull(jsInfo.getFeelist())) {
				List<InsSuzhounhWebJsfee> jsFeeList = jsInfo.getFeelist();
				for (int i = 0; i < jsFeeList.size(); i++) {
					ApplicationUtils.setDefaultValue(jsFeeList.get(i), true);
					jsFeeList.get(i).setPkJs(jsInfo.getId());
				}
				if (jsFeeList.size() > 0) {
					System.out.println(DataBaseHelper
							.getInsertSql(InsSuzhounhWebJsfee.class));
					DataBaseHelper.batchUpdate(DataBaseHelper
							.getInsertSql(InsSuzhounhWebJsfee.class),
							jsFeeList);
				}
			}
		}
		return jsInfo;		
	}
	
	
	/**
	 * 获取宿州农保WEB结算信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public InsSuzhounhWebJs getJsByPkSettle(String param, IUser user) {
		String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
		InsSuzhounhWebJs webjsInfo = insSuZhouNHWebMapper.getJsInfoByPkSettle(pkSettle);
		if (webjsInfo != null) {
			return webjsInfo;
		} else {
			throw new BusException("查不到该次的医保结算记录，无法获取数据！");
		}
	}
	
	
//	/**
//	 * 保存宿州农合web分段信息
//	 * 
//	 * @param param
//	 * @param user
//	 * @return
//	 */
//	public InsSuzhounhWebGrade saveJsGradeInfo(String param, IUser user) {
//		InsSuzhounhWebGrade insSuzhounhWebGrade = JsonUtil.readValue(param,InsSuzhounhWebGrade.class);
//		if (insSuzhounhWebGrade != null) {
//			if (!StringUtils.isBlank(insSuzhounhWebGrade.getId())) {
//				DataBaseHelper.updateBeanByPk(insSuzhounhWebGrade, false);
//			} else {
//				DataBaseHelper.insertBean(insSuzhounhWebGrade);
//			}
//		}
//		return insSuzhounhWebGrade;
//	}
	
	
	/**
	 * 删除宿州农保web结算信息
	 * 
	 * @param param
	 * @param user
	 */
	public void deleteJsInfo(String param, IUser user) {
		InsSuzhounhWebJs jsInfo = JsonUtil.readValue(param, InsSuzhounhWebJs.class);
		if (jsInfo != null) {
			String sqlJS = "update INS_SUZHOUNH_WEB_JS set del_flag =? where ID=? ";
			String sql = "update INS_SUZHOUNH_WEB_GRADE set del_flag =? where pk_js=? ";
			String sql1 = "update INS_SUZHOUNH_WEB_JSFEE set del_flag =? where pk_js=? ";

			if (!StringUtils.isBlank(jsInfo.getId())) {
				DataBaseHelper.update(sqlJS, new Object[] { "1", jsInfo.getId() });				
				DataBaseHelper.update(sql, new Object[] { "1", jsInfo.getId() });
				DataBaseHelper.update(sql1, new Object[] { "1", jsInfo.getId() });
			} 
			else{
				throw new BusException("查不到结算信息，无法获取数据！");				
			}
		}
	}
	
	
	/**
	 * [交易号]015001004013 根据农合结算主键更新pk_settle,建立与bl_settle的关系
	 * 
	 * @param param
	 * @param user
	 */
	public void updateNhJsPkSettle(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			return;

			String sql = "update ins_suzhounh_web_js set pk_settle=? where del_flag='0' and id=? ";
			DataBaseHelper.update(sql, new Object[] { paramMap.get("pkSettle"),
					paramMap.get("id") });			
			String sql1 = "update INS_SUZHOUNH_WEB_GRADE set pk_settle=? where pk_js=?";
			DataBaseHelper.update(sql1, new Object[] { paramMap.get("pkSettle"),
					paramMap.get("id") });
			
		   if(paramMap.get("pkPv") != null && paramMap.get("pkPv").toString() != ""){
			String sql2 = "update ins_suzhounh_web_js set pk_pv=? where del_flag='0' and id=? ";
			DataBaseHelper.update(sql2, new Object[] { paramMap.get("pkPv"),
					paramMap.get("id") });
		   }
		
	}
	
	/**
	 * 查询结算信息列表[分页]
	 * 
	 * @param param
	 * @return user
	 * @throws
	 * 
	 * @author wangjie
	 * @date 2018年5月4日
	 */
	public PageInsSuzhounhWebJsListVo getJSListPaging(String param, IUser user) {
		PageInsSuzhounhWebJsListVo pageInsSuzhounhWebJsListVo = new PageInsSuzhounhWebJsListVo();
		PageQryJSParam qryparam = JsonUtil.readValue(param,PageQryJSParam.class);
		InsSuzhounhWebJs insSuzhounhWebJs = JsonUtil.readValue(param, InsSuzhounhWebJs.class);
		if (qryparam == null || insSuzhounhWebJs == null)
			throw new BusException("查询参数有问题！");

		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());

		// 分页操作
		MyBatisPage.startPage(pageIndex, pageSize);
		List<InsSuzhounhWebJs> list = insSuZhouNHWebMapper.getJSList(insSuzhounhWebJs);
		Page<List<InsSuzhounhWebJs>> page = MyBatisPage.getPage();
		pageInsSuzhounhWebJsListVo.setInsSuzhounhWebJs(list);
		pageInsSuzhounhWebJsListVo.setTotalCount(page.getTotalCount());
		return pageInsSuzhounhWebJsListVo;
	}
	
	/**
	 * 交易号：015001010013
	 * 删除医保对照相关信息
	 * @param param
	 * @param user
	 */
	public void deleteSzybItemMapInfo(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null){
			throw new BusException("未得到相关信息！");
		}
		String sql="update INS_SUZHOUNH_WEB_DISMAP set del_flag='1' where pk_insitemmap=?";
		DataBaseHelper.execute(sql, map.get("pkInsitemmap"));
	}
	
	/**
	 * 交易号：015001010012
	 * 保存医保对照相关信息
	 * @param param
	 * @param user
	 */
	public String saveSzybItemMapInfo(String param,IUser user){
		InsSuzhouNHWebDisMap itemMap=JsonUtil.readValue(param, InsSuzhouNHWebDisMap.class);
		if(itemMap==null){
			throw new BusException("未得到相关信息！");
		}
		String sql="select count(1) from INS_SUZHOUNH_WEB_DISMAP where pk_insitemmap=?";
		int resultCount=DataBaseHelper.queryForScalar(sql, Integer.class, itemMap.getPkInsitemmap());
		if(resultCount<=0){
			 DataBaseHelper.insertBean(itemMap);
		}else{
			sql="update INS_SUZHOUNH_WEB_DISMAP  "
					+ "set CODE=?, NAME=?, FYLB=?"
					+ "where  pk_insitemmap=?";
			DataBaseHelper.execute(sql, new Object[]{itemMap.getCode(),itemMap.getName(),itemMap.getFylb(),itemMap.getPkInsitemmap()});
		}
		 return itemMap.getPkInsitemmap();
	}
	
	/**
	 * 根据pkHp，List<pkitem>查询已匹配的疾病项目
	 * 015001010015
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsSuzhouNHWebDisMap> qrySzybItemMapInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		//List<InsSuzhouNHWebDisMap> mapList=insSuZhouNHWebMapper.qrySzybItemMapInfo(paramMap);
		List<String> pkItems=(List<String>)paramMap.get("pkItems");
//		if(mapList!=null &&pkItems!=null && mapList.size()<pkItems.size()){
//			throw new BusException("存在未匹配的疾病信息！");
//		}
		
		List<InsSuzhouNHWebDisMap> mapList1 = new ArrayList<>();
		if(pkItems!=null && pkItems.size()>0){
			 List<String> list = new ArrayList<>();
		     for (String pkitem : pkItems) {
		    	 paramMap.remove("pkItems");
		    	 List<String> list2 = new ArrayList<>();
		    	 list2.add(pkitem);
		    	 paramMap.put("pkItems", list2);
		    	 mapList1=insSuZhouNHWebMapper.qrySzybItemMapInfo(paramMap);
		    	 if(mapList1.size() <= 0)
		    	 {
		    		 List<BdCndiag> BdCndiag=insSuZhouNHWebMapper.qryYbDicNoWithInfo(paramMap);		    		 
		    		 if(BdCndiag.size() > 0)
		    		 { 
		    			 list.add(BdCndiag.get(0).getNameCd());
		    		 }		    		 
		    	 }
		     }		     		    		     
//		     if(list.size() >0 )
//	    	 {
//	    		 throw new BusException("以下疾病信息未匹配:" + "\n" +
//	    	 org.apache.commons.lang3.StringUtils.join(list.toArray()),"\n" );
//	    	 }
		}
	
		
		return mapList1;
	}

	/**
	 * 根据pkHp，List<pkitem>查询已匹配的项目对照项目
	 * 015001005024
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsNhybItemMap> qrySzybItemMapInfoForMZ(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		List<InsNhybItemMap> mapList=insSuZhouNHWebMapper.qrySzybItemMapInfoForMZ(paramMap);
		return mapList;
	}
	
	/**
	 * 交易号：015001005016
	 * 通过pkpv更新门诊或住院的flag_insu标记
	 * @param param
	 * @param user
	 */
	public void updateOPorIpForInsByPkpv(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null|| map.size()<=0) throw new BusException("未获得相关信息");
		if("i".equalsIgnoreCase(map.get("type").toString())){
			String sql="update bl_ip_dt set FLAG_INSU='0' where FLAG_SETTLE='0' and  pk_cgip=? and pk_settle is null";
			DataBaseHelper.execute(sql, map.get("pkcgip"));
		}else if("o".equalsIgnoreCase(map.get("type").toString())){
			String sql="update bl_op_dt set FLAG_INSU='0' where FLAG_SETTLE='0' and  pk_cgop=? and pk_settle is null";
			DataBaseHelper.execute(sql, map.get("pkcgop"));
		}
	}
	
	/**
	 * 查询医保计划扩展属性
	 * 015001005024
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdHpExPro> qryIpVisitCount(String param,IUser user){
		Map<String,String> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		List<BdHpExPro> mapList=insSuZhouNHWebMapper.qryIpVisitCount(paramMap);
		return mapList;
	}
	
	/**
	 * 根据主诊断编码查询医疗方式编码
	 * 交易码：015001010027
	 * @param param
	 * @param user
	 * @return
	 */
	public String qryTreatByDiag(String param,IUser user){
		String icdallno = JsonUtil.getFieldValue(param, "icdallno");
		String treatcode = null;
		if(!CommonUtils.isEmptyString(icdallno)){
			List<Map<String,Object>> resMap = DataBaseHelper.queryForList(
					"select d.code as code from INS_SUZHOUNH_WEB_TREAT d inner join INS_SUZHOUNH_WEB_TD td on d.Code = td.treatcode where  td.diseasecode = ?", 
					new Object[]{icdallno});
			if(resMap!=null && resMap.size()>0
					&& resMap.get(0)!=null && resMap.get(0).size()>0
					&& resMap.get(0).get("code")!=null && CommonUtils.isEmptyString(CommonUtils.getString(resMap.get(0).get("code")))){
				treatcode = CommonUtils.getString(resMap.get(0).get("code"));
			}
		}
		return treatcode;
	}
	
	
	

}
