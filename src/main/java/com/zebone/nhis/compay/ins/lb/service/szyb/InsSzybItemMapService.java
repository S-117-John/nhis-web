package com.zebone.nhis.compay.ins.lb.service.szyb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.lb.dao.szyb.InsSzybItemMapMapper;
import com.zebone.nhis.compay.ins.lb.service.pub.dao.LbYbPubMapper;
import com.zebone.nhis.compay.ins.lb.service.pub.service.LbYbPubService;
import com.zebone.nhis.compay.ins.lb.vo.szyb.InsSzybItemMap;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsSzybItemMapService {
	
	@Resource
	private InsSzybItemMapMapper inSzybItemMapMapper;
	@Resource
	private LbYbPubMapper lbYbPubMapper;
	
	/**
	 * 交易号：020001001021
	 * 查询宿州医保药品与医院药品信息比对
	 * @param param{"euMatch":"匹配状态（1：匹配，2：未匹配）", "pkHp":"医保主键", "xmlb":"项目类别","info":"文本框搜索信息（暂未使用）"}
	 * @param user
	 * @return
	 */
	public Page<Map<String,Object>> qrySzybPdDicWithInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String euActive = paramMap.get("euActive").toString();
		int pageIndex = CommonUtils.getInteger(paramMap.get("pageIndex"));
		int pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
		MyBatisPage.startPage(pageIndex, pageSize);	
		List<Map<String,Object>> mapList=new ArrayList<>();
		if(paramMap.get("euMatch").equals("1")){
			if(euActive.equals("1")){//启用
				paramMap.put("euActive", 0);
			}else{//停用
				paramMap.put("euActive", 1);
			}
			 mapList=inSzybItemMapMapper.qrySzybPdDicWithInfo(paramMap);
		}else{
			if(euActive.equals("1")){//启用
				paramMap.put("euActive", 0);
			}else{//停用
				paramMap.put("euActive", 1);
			}
			mapList=lbYbPubMapper.qryYbPdDicNoWithInfo(paramMap);
		}
		Page<Map<String,Object>> page = MyBatisPage.getPage();
		page.setRows(mapList);		
		return page;
	}
	/**
	 * 交易号：020001001022
	 * 保存医保对照相关信息
	 * @param param
	 * @param user
	 */
	public String saveSzybItemMapInfo(String param,IUser user){
		InsSzybItemMap itemMap=JsonUtil.readValue(param, InsSzybItemMap.class);
		if(itemMap==null){
			throw new BusException("未得到相关信息！");
		}
		String sql="select count(1) from ins_szyb_item_map where pk_insitemmap=?";
		int resultCount=DataBaseHelper.queryForScalar(sql, Integer.class, itemMap.getPkInsitemmap());

		//查询医保是否为离休医保
		BdHp hpVo = DataBaseHelper.queryForBean(
				"select * from bd_hp where pk_hp = ?"
				,BdHp.class,new Object[]{itemMap.getPkHp()}
		);

		if(resultCount<=0){
			if(!"04".equals(hpVo.getDtExthp())){//非离休处理逻辑
				List<Map<String,Object>> pkHpList = DataBaseHelper.queryForList(
						"select pk_hp from bd_hp where pk_hp in (select PK_HP from ins_szyb_item_map where DEL_FLAG = '0' group by PK_HP ) and DT_EXTHP!='04'",
						new Object[]{}
				);
				//新增对照时同时对照所有医保
				if(pkHpList!=null && pkHpList.size()>0){
					List<InsSzybItemMap> itemMapList = new ArrayList<>();
					for(Map<String,Object> hpMap : pkHpList){
						InsSzybItemMap itemVo = (InsSzybItemMap)itemMap.clone();
						itemVo.setPkHp(CommonUtils.getPropValueStr(hpMap,"pkHp"));
						ApplicationUtils.setDefaultValue(itemVo,true);
						itemVo.setPkInsitemmap(NHISUUID.getKeyId());
						itemMapList.add(itemVo);
					}

					if(itemMapList!=null && itemMapList.size()>0){
						//保存前先删除已经存在的对照信息，再做保存操作
						DataBaseHelper.execute(
								"delete from INS_SZYB_ITEM_MAP where code=? and name=? and fylb=? and xmlb=? and pk_item = ?",
								new Object[]{itemMap.getCode(),itemMap.getName(),itemMap.getFylb(),itemMap.getXmlb(),itemMap.getPkItem()}
						);

						DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybItemMap.class),itemMapList);
					}
				}else{
					DataBaseHelper.insertBean(itemMap);
				}
			}else{//离休处理
				itemMap.setPkHp(hpVo.getPkHp());
				ApplicationUtils.setDefaultValue(itemMap,true);
				DataBaseHelper.insertBean(itemMap);
			}
		}else{
			InsSzybItemMap itemMapVo = DataBaseHelper.queryForBean(
					"select * from INS_SZYB_ITEM_MAP where PK_INSITEMMAP = ?",
					InsSzybItemMap.class,
					new Object[]{itemMap.getPkInsitemmap()}
			);

			if(!"04".equals(hpVo.getDtExthp())){//非离休处理逻辑
				if(itemMapVo!=null && !CommonUtils.isEmptyString(itemMapVo.getPkInsitemmap())){
					DataBaseHelper.execute(
							"update INS_SZYB_ITEM_MAP set code=?,name=?,fylb=? where code=? and name=? and fylb=? and xmlb=? and pk_item = ?",
							new Object[]{itemMap.getCode(),itemMap.getName(),itemMap.getFylb(),itemMapVo.getCode(),itemMapVo.getName(),itemMapVo.getFylb(),itemMapVo.getXmlb(),itemMapVo.getPkItem()});
				}else{
					sql="update INS_SZYB_ITEM_MAP set CODE=?, NAME=?, FYLB=? where  pk_insitemmap=?";
					DataBaseHelper.execute(sql, new Object[]{itemMap.getCode(),itemMap.getName(),itemMap.getFylb(),itemMap.getPkInsitemmap()});
				}
			}else{
				sql="update INS_SZYB_ITEM_MAP set CODE=?, NAME=?, FYLB=? where  pk_insitemmap=?";
				DataBaseHelper.execute(sql, new Object[]{itemMap.getCode(),itemMap.getName(),itemMap.getFylb(),itemMap.getPkInsitemmap()});
			}
		}
		 return itemMap.getPkInsitemmap();
	}
	/**
	 * 交易号：020001001023
	 * 删除医保对照相关信息
	 * @param param
	 * @param user
	 */
	public void deleteSzybItemMapInfo(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null){
			throw new BusException("未得到相关信息！");
		}

		InsSzybItemMap itemMapVo = DataBaseHelper.queryForBean(
				"select * from INS_SZYB_ITEM_MAP where PK_INSITEMMAP = ?",
				InsSzybItemMap.class,
				new Object[]{map.get("pkInsitemmap")}
		);
		if(itemMapVo!=null && !CommonUtils.isEmptyString(itemMapVo.getPkInsitemmap())){
			DataBaseHelper.execute(
					"update ins_szyb_item_map set del_flag='1' where code=? and name=? and fylb=? and xmlb=?",
					new Object[]{itemMapVo.getCode(),itemMapVo.getName(),itemMapVo.getFylb(),itemMapVo.getXmlb()});
		}else{
			String sql="update ins_szyb_item_map set del_flag='1' where pk_insitemmap=?";
			DataBaseHelper.execute(sql, map.get("pkInsitemmap"));
		}
	}
	
	/**
	 * 交易号：020001001024
	 * 查询宿州医保收费项目与医院信息比对
	 * @param param {"euMatch":"匹配状态（1：匹配，2：未匹配）", "pkHp":"医保主键", "xmlb":"项目类别", "info":"文本框搜索信息（暂未使用）"}
	 * @param user
	 * @return
	 */
//	public List<Map<String,Object>> qrySzybItemDicWithInfo(String param,IUser user){
//		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
//		if(paramMap==null) throw new BusException("未得到相关信息！");
//		List<Map<String,Object>> mapList=new ArrayList<>();
//		if(paramMap.get("euMatch").equals("1")){
//			 mapList=inSzybItemMapMapper.qrySzybItemDicWithInfo(paramMap);
//		}else{
//			 mapList=lbYbPubMapper.qryYbItemDicNoWithInfo(paramMap);
//		}
//		return mapList;
//	}
	
	
	public Page<Map<String,Object>> qrySzybItemDicWithInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) throw new BusException("未得到相关信息！");
		int pageIndex = CommonUtils.getInteger(paramMap.get("pageIndex"));
		int pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
		MyBatisPage.startPage(pageIndex, pageSize);	
		List<Map<String,Object>> mapList=new ArrayList<>();
		if(paramMap.get("euMatch").equals("1")){
			 mapList=inSzybItemMapMapper.qrySzybItemDicWithInfo(paramMap);
		}else{
			 mapList=lbYbPubMapper.qryYbItemDicNoWithInfo(paramMap);
		}
		Page<Map<String,Object>> page = MyBatisPage.getPage();
		page.setRows(mapList);
		
		return page;
	}
	
//	public List<Map<String,Object>> qrySzybItemDicWithInfo(String param,IUser user){
//	Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
//	if(paramMap==null) throw new BusException("未得到相关信息！");
//	List<Map<String,Object>> mapList=new ArrayList<>();
//
//     int pageIndex = 1;
//     int pageSize = 50;
//	//int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
//	//int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
//	// 分页操作
//	MyBatisPage.startPage(pageIndex, pageSize);			
//	
//	if(paramMap.get("euMatch").equals("1")){
//		 mapList=inSzybItemMapMapper.qrySzybItemDicWithInfo(paramMap);
//	}else{
//		mapList=lbYbPubMapper.qryYbItemDicNoWithInfo(paramMap);
//	}		
//	Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
//	//page.setRows(mapList);
//	}
}
