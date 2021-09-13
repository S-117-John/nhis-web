package com.zebone.nhis.nd.pub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.nd.pub.dao.NdPubMapper;
import com.zebone.nhis.nd.pub.vo.EmrTemplateVo;
import com.zebone.nhis.nd.pub.vo.NdRecordRowVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 护理文书对外服务
 * @author yangxue
 *
 */
@Service
public class NdPubService {
	@Resource
	private NdPubMapper ndPubMapper;
	
	/**
	 * 查询患者护理记录列表
	 * 
	 * @param param
	 *            --pkPv
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryRecordList(String param, IUser user) {
		String pkPv = JsonUtil.readValue(param, String.class);
		if (CommonUtils.isEmptyString(pkPv))
			throw new BusException("未获取到患者就诊主键！");
		return ndPubMapper.queryRecordList(pkPv);
	}
	/**
	 * 查询某一护理记录单对应的模板
	 * @param param{pkTmp,pkRecord}
	 * @param user
	 * @return
	 */
	public EmrTemplateVo queryRecordTemplateHeader(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || CommonUtils.isNull(paramMap.get("pkTmp")))
			throw new BusException("未获取到要查询的护理记录单模板主键！");
		EmrTemplateVo tmpvo= ndPubMapper.queryRecordXml(paramMap);
		if(tmpvo!=null){
			List<Map<String,Object>> list = ndPubMapper.queryRecordDcList(CommonUtils.getString(paramMap.get("pkRecord")));
		    tmpvo.setDtlist(list);
		}
		return tmpvo;
	}
	/**
	 * 查询护理记录单所有行数据
	 * 
	 * @param param
	 *            {pkRecord}
	 * @param user
	 * @return
	 */
	public List<NdRecordRowVo> queryRecordAllData(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || CommonUtils.isNull(paramMap.get("pkRecord")))
			throw new BusException("未获取到要查询的护理记录主键！");
		List<NdRecordRowVo> list = ndPubMapper.queryRecordRowList(paramMap);
		if (list == null || list.size() <= 0)
			return null;
		List<NdRecordDt> dtlist = new ArrayList<NdRecordDt>();
		for (NdRecordRowVo row : list) {
			dtlist = new ArrayList<NdRecordDt>();
			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("pkRecord", row.getPkRecord());
			pMap.put("pkRecordRow", row.getPkRecordrow());
			dtlist = ndPubMapper.queryRecordColDtList(pMap);
			row.setDtlist(dtlist);
		}
		return list;
	}
}
