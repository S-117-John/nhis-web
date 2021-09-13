package com.zebone.nhis.pro.zsba.bl.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.bl.dao.ZsbABlMedicalExeMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 医疗批量计费服务（博爱版）
 */
@Service
public class ZsbABlMedicalExeService {

    @Resource
    private ZsbABlMedicalExeMapper zsbABlMedicalExeMapper;

    /**
     * 医技批量计费-查询功能：查询住院医技申请单
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryMedicalAppZsba(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isNotNull(paramMap.get("dateBegin"))) {
            paramMap.put("dateBegin", CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8) + "000000");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
            paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8) + "235959");
        }
        List<Map<String, Object>> list = zsbABlMedicalExeMapper.qryIpMedAppInfo(paramMap);
        return list;
    }
    
    /**
     * 医技批量计费-点击每一行：查询住院医技申请对应已计费费用记录
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpBlDt(String param, IUser user) {
    	
    	Map<String, Object> paramMaps = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {});
    	String pkCnord = CommonUtils.getString(paramMaps.get("pkCnord"));
    	String pkExocc = CommonUtils.getString(paramMaps.get("pkExocc"));
    	// 费用明细列表
    	return zsbABlMedicalExeMapper.qryIpMedBlDtInfo(pkCnord,pkExocc);  
    }
    
    /**
     * 护士站-出院负金额处方信息查询
     * 交易号:022003018020
     * @param param
     * @param user
     * @return
     */
    public String getIpNegativeAnountOrder(String param, IUser user) {
    	Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
    	List<Map<String, Object>> ordList = zsbABlMedicalExeMapper.qryIpNegativeAnountOrder(paramMap);  
    	if(null != ordList && !ordList.isEmpty()) {
    		StringBuffer signSbf = new StringBuffer("");
    		ordList.forEach(map->{
    			signSbf.append(String.format("医嘱号:%s",MapUtils.getString(map, "ordsn")));
    			signSbf.append(String.format("医嘱内容:%s",MapUtils.getString(map, "nameOrd")));
    			signSbf.append(";");
    		});
    		return signSbf.toString();
    	}
    	 return null;
    }
    
}
