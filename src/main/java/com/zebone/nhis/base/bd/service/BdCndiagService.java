package com.zebone.nhis.base.bd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.CndiagMapper;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiag;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiagAs;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiagComp;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiagComt;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiagComtDt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class BdCndiagService {
	@Resource
	private CndiagMapper cndiagMapper;
	
	public Map<String, Object> qryCndiag(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int pageIndex = Integer.parseInt(map.get("pageIndex").toString());
		MyBatisPage.startPage(pageIndex, pageSize);
		
		List<BdCndiag> list = cndiagMapper.qryCndiag(map);
		Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
		
		Map<String, Object> result = new HashMap<>();
		result.put("list", list);
		result.put("totalCount", page.getTotalCount());
		
		return result;
	}
	
	/**
	 * 查询诊断
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public BdCndiag qryCndiagsOld(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if (map.get("pkCndiag")==null)return null; 
		String pkCndiag = (String)map.get("pkCndiag");
		BdCndiag  cndiag =cndiagMapper.qryCndiagBypk(pkCndiag);
		if (cndiag==null) {
			throw new BusException("该诊断不存在!");
		}
		cndiag.setBdCndiagAs(cndiagMapper.qryAs(pkCndiag));
		cndiag.setBdCndiagComps(cndiagMapper.qryCndiagComp(pkCndiag));
		List<BdCndiagComt> bdCndiagComts = cndiagMapper.qryCndiagComt(pkCndiag);
				if (bdCndiagComts != null && bdCndiagComts.size() > 0) {
					for (BdCndiagComt bdCndiagComt : bdCndiagComts) {
						bdCndiagComt.setBdCndiagComtDts(cndiagMapper.qrBdCndiagComtDt(bdCndiagComt.getPkCndiagcomt()));
					}
				}
		cndiag.setBdCndiagComts(bdCndiagComts);
		return cndiag;
	}
	
	/**
	 * 查询诊断
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public BdCndiag qryCndiags(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if (map.get("pkCndiag")==null)return null; 
		String pkCndiag = (String)map.get("pkCndiag");
		BdCndiag  cndiag =cndiagMapper.qryCndiagBypk(pkCndiag);
		if (cndiag==null) {
			throw new BusException("该诊断不存在!");
		}
		cndiag.setBdCndiagAs(cndiagMapper.qryAs(pkCndiag));
		cndiag.setBdCndiagComps(cndiagMapper.qryCndiagComp(pkCndiag));
		List<BdCndiagComt> bdCndiagComts = cndiagMapper.qryCndiagComt(pkCndiag);
		List<BdCndiagComtDt> bdCndiagComtDts = null;
		if (bdCndiagComts != null && bdCndiagComts.size() > 0) {
			ArrayList<String> pks = new ArrayList<String>();
			for (BdCndiagComt bdCndiagComt : bdCndiagComts) {
				pks.add(bdCndiagComt.getPkCndiagcomt());
			}
			bdCndiagComtDts = cndiagMapper.qryDtBylist(pks);
		}
		if (bdCndiagComtDts!=null&&bdCndiagComtDts.size()>0) {
			for (BdCndiagComt bdCndiagComt : bdCndiagComts) {
				ArrayList<BdCndiagComtDt> dts = new ArrayList<BdCndiagComtDt>();
				for (BdCndiagComtDt bdCndiagComtDt : bdCndiagComtDts) {
					if (bdCndiagComtDt.getPkCndiagcomt().equals(bdCndiagComt.getPkCndiagcomt())) {
						dts.add(bdCndiagComtDt);
					}
				}
				bdCndiagComt.setBdCndiagComtDts(dts);
			}
		}
		cndiag.setBdCndiagComts(bdCndiagComts);
		return cndiag;
	}
	/**
	 * 删除诊断
	 * 
	 * @param param
	 * @param user
	 */
	public void delCndiag(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if (map==null)
			return;
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("pkCndiag", (String)map.get("param"));
		cndiagMapper.delBdCndiagComtDt(hashMap);
		cndiagMapper.delAs(hashMap);
		cndiagMapper.delCndiagComp(hashMap);
		cndiagMapper.delCndiagComt(hashMap);
		cndiagMapper.delCndiag(hashMap);
	}

	/**
	 * 保存
	 * @param param
	 * @param user
	 * @return 
	 */
	public BdCndiag saveCndiag(String param,IUser user){
		BdCndiag cndiag = JsonUtil.readValue(param, BdCndiag.class);
		if (cndiag==null) return null;
		
		List<BdCndiagAs> bdCndiagAs = cndiag.getBdCndiagAss();
		List<BdCndiagComp> bdCndiagComps = cndiag.getBdCndiagComps();
		List<BdCndiagComt> bdCndiagComts = cndiag.getBdCndiagComts();
		List<BdCndiagComtDt> bdCndiagComtDts=null;
		HashMap hashMap = new HashMap();
		hashMap.put("codeCd", cndiag.getCodeCd());
		hashMap.put("nameCd", cndiag.getNameCd());
		hashMap.put("pkCndiag", cndiag.getPkCndiag());
		
		if (cndiagMapper.qryCountName(hashMap)>0) {
				throw new  BusException("名称不唯一!");
		}
		//新增
		if (!StringUtils.isNotBlank(cndiag.getPkCndiag())) {
			if (cndiagMapper.qryCountCode(hashMap)>0) {
				throw new  BusException("编码已被使用!");
			}
			DataBaseHelper.insertBean(cndiag);
			
			if (bdCndiagAs!=null&&bdCndiagAs.size()>0) {
				for (BdCndiagAs cndiagAs : bdCndiagAs) {
					cndiagAs.setPkCndiag(cndiag.getPkCndiag());
					cndiag.setPkOrg("~");
					
					DataBaseHelper.insertBean(cndiagAs);
				}
			}else {
				throw new  BusException("至少应有一条别名记录!");
			}
			
			if (bdCndiagComps!=null&&bdCndiagComps.size()>0) {
				for (BdCndiagComp cndiagAs : bdCndiagComps) {
					cndiagAs.setPkCndiag(cndiag.getPkCndiag());
					cndiagAs.setPkOrg("~");
					DataBaseHelper.insertBean(cndiagAs);
				}
			}
			
			if (bdCndiagComts!=null&&bdCndiagComts.size()>0) {
				for (BdCndiagComt cndiagAs : bdCndiagComts) {
					cndiagAs.setPkCndiag(cndiag.getPkCndiag());
					cndiagAs.setPkOrg("~");
					DataBaseHelper.insertBean(cndiagAs);
					bdCndiagComtDts = cndiagAs.getBdCndiagComtDts();
					if (bdCndiagComtDts!=null&&bdCndiagComtDts.size()>0) {
						for (BdCndiagComtDt bdCndiagComtDt : bdCndiagComtDts) {
							
							bdCndiagComtDt.setPkCndiagcomt(cndiagAs.getPkCndiagcomt());
							bdCndiagComtDt.setPkOrg("~");
							DataBaseHelper.insertBean(bdCndiagComtDt);
						}
					}
				}
			}
		}else {//更新
			DataBaseHelper.updateBeanByPk(cndiag,false);
			//保存as
			if (bdCndiagAs!=null&&bdCndiagAs.size()>0) {
				List<String> strs = new ArrayList<String>();
				for (BdCndiagAs cndiagAs : bdCndiagAs) {
					cndiagAs.setPkCndiag(cndiag.getPkCndiag());
					cndiagAs.setPkOrg("~");
					if (StringUtils.isNotBlank(cndiagAs.getPkCndiagas())) {
						DataBaseHelper.updateBeanByPk(cndiagAs,false);
					}else {
						
						DataBaseHelper.insertBean(cndiagAs);
					}
					strs.add(cndiagAs.getPkCndiagas());
				}
				HashMap<String, Object> map = new HashMap<>();
				map.put("list", strs);
				map.put("pkCndiag", cndiag.getPkCndiag());
				cndiagMapper.delAsByList(map);
			}else{
				throw new  BusException("至少应有一条别名记录!");
			}
			//保存comp
			if (bdCndiagComps!=null&&bdCndiagComps.size()>0) {
				List<String> strs = new ArrayList<String>();
				for (BdCndiagComp cndiagAs : bdCndiagComps) {
					cndiagAs.setPkCndiag(cndiag.getPkCndiag());
					cndiagAs.setPkOrg("~");
					if (StringUtils.isNotBlank(cndiagAs.getPkCndiagcomp())) {
						DataBaseHelper.updateBeanByPk(cndiagAs,false);
					}else {
						
						DataBaseHelper.insertBean(cndiagAs);
					}
					strs.add(cndiagAs.getPkCndiagcomp());
				}
				HashMap<String, Object> map = new HashMap<>();
				map.put("list", strs);
				map.put("pkCndiag", cndiag.getPkCndiag());
				cndiagMapper.delCompByList(map);
			}else{
				HashMap<String, Object> map = new HashMap<>();
				map.put("pkCndiag", cndiag.getPkCndiag());
				cndiagMapper.delCndiagComp(map);
			}
			
			if (bdCndiagComts!=null&&bdCndiagComts.size()>0) {
				List<String> strs = new ArrayList<String>();
				for (BdCndiagComt cndiagAs : bdCndiagComts) {
					
					if (StringUtils.isNotBlank(cndiagAs.getPkCndiagcomt())) {
						DataBaseHelper.updateBeanByPk(cndiagAs,false);
					}else {
						cndiagAs.setPkCndiag(cndiag.getPkCndiag());
						cndiagAs.setPkOrg("~");
						//cndiagAs.setPkCndiagcomt(UUID.randomUUID().toString().replace("-", ""));
						DataBaseHelper.insertBean(cndiagAs);
					}
					strs.add(cndiagAs.getPkCndiagcomt());
					
					
					bdCndiagComtDts = cndiagAs.getBdCndiagComtDts();
					List<String> dtStrs = new ArrayList<String>();
					if (bdCndiagComtDts!=null&&bdCndiagComtDts.size()>0) {
						for (BdCndiagComtDt bdCndiagComtDt : bdCndiagComtDts) {
							bdCndiagComtDt.setPkCndiagcomt(cndiagAs.getPkCndiagcomt());
							bdCndiagComtDt.setPkOrg("~");
							if (StringUtils.isNotBlank(bdCndiagComtDt.getPkCndiagcomtdt())) {
								DataBaseHelper.updateBeanByPk(bdCndiagComtDt,false);
							}else {
								DataBaseHelper.insertBean(bdCndiagComtDt);
							}
							dtStrs.add(bdCndiagComtDt.getPkCndiagcomtdt());
						}
						HashMap dtmap = new HashMap();
						dtmap.put("list", dtStrs);
						dtmap.put("pkCndiagcomt",cndiagAs.getPkCndiagcomt());
						
						cndiagMapper.delDtByList(dtmap);
					}else {
						cndiagMapper.delDtByComt(cndiagAs.getPkCndiagcomt());
					}
				}
				HashMap<String, Object> map = new HashMap<>();
				map.put("list", strs);
				map.put("pkCndiag", cndiag.getPkCndiag());
				
				cndiagMapper.delComtByList(map);
				
			}else{
				HashMap<String, Object> map = new HashMap<>();
				map.put("pkCndiag", cndiag.getPkCndiag());
				cndiagMapper.delCndiagComt(map);
			}
			
		}
		cndiagMapper.delUnnecessaryDt(); 
		return cndiag;
	}
	
	/**
	 * 获取codeCd接口 
	 * @param param
	 * @param user
	 * @return
	 */
	public String getCodeCd(String param,IUser user) {
//		Integer codeCd = cndiagMapper.qryCodeCd()+1;
//		return  codeCd.toString();
		return ApplicationUtils.getCode("0002");
	}
	
	//临床诊断维护查询术后备注字典列表：001002001011
	public List<Map<String, Object>> qryOpAfterDictionary(String param,IUser user){
		
		return cndiagMapper.qryOpAfterDictionary();
	}
}
