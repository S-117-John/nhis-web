package com.zebone.nhis.ex.nis.qry.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.platform.Application;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.fee.dao.BlCgIpQueryMapper;
import com.zebone.nhis.ex.pub.dao.ExPubMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 费用分类查询
 * @author yangxue
 *
 */
@Service
public class QueryCgCateService {
	
	@Resource
	private BlCgIpQueryMapper blCgIpQueryMapper;
	@Resource
	private ExPubMapper exPubMapper;
    /**
     * 根据就诊科室，查询患者费用分类汇总金额
     * @param param{pkPv，pkDept}
     * @param user
     * @return
     */
	public Map<String,Object> queryCgCateFeeByPv(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> result = blCgIpQueryMapper.queryCgCateFeeByPv(map);
		BigDecimal yjj = processNull(exPubMapper.getYjFee(map));
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("yjj", yjj);//预交金
		paramMap.put("dbj", exPubMapper.getDbFee(map));//担保金
		paramMap.put("zf", exPubMapper.getZfFee(map));//自付金额
		paramMap.put("ye", calYe(yjj,map));//余额
		paramMap.put("catelist", result);
		return paramMap;
	}
	/**
	 * 根据就诊科室，项目名称，项目分类查询患者计费项目汇总信息
	 * @param param{pkPv,pkDept,nameCg,codeItemcate}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryCgItemFeeByPv(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		return blCgIpQueryMapper.queryCgItemFeeByPv(map);
	}
	/**
	 * 查询患者费用明细
	 * @param param{pkPvs,dateBegin,dateEnd,type,pkDept,pkDeptEx,codeItemcate}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryCgDetailsByPv(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String dateEnd = CommonUtils.getString(map.get("dateEnd"));
		if(dateEnd!=null&&!dateEnd.equals("")){
			map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		}
		return blCgIpQueryMapper.queryBlCgIpDetails(map);
	}
	
	/**
	 * 查询患者费用核查信息
	 * @param param{pkPvs,dateBegin,dateEnd,type,pkDept,pkDeptEx,codeItemcate}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPatiDetailsByPv(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String dateEnd = CommonUtils.getString(map.get("dateEnd"));
		if(dateEnd!=null&&!dateEnd.equals("")){
			map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		}
		return blCgIpQueryMapper.queryPatiDetails(map);
	}
	
	/**
	 * 查询患者费用核查明细信息
	 * @param param{pkPv,pkItem}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryCgDetaileds(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
        if(Application.isSqlServer()){
            map.put("dbType", "sqlserver");
        }else{
            map.put("dbType", "oracle");
        }
		return blCgIpQueryMapper.queryCgDetaileds(map);
	}

	
	
	/**
	 * 计算患者余额
	 * @param yjj
	 * @param map
	 * @return
	 */
	private BigDecimal calYe(BigDecimal yjj,Map<String,Object> map){
		BigDecimal zfy = processNull(exPubMapper.getTotalFee(map));
		//BigDecimal ztfy_n = processNull(exPubMapper.getZtNPdFee(map));
		//BigDecimal ztfy = processNull(exPubMapper.getZtPdFee(map));
		//BigDecimal zt = ztfy_n.add(ztfy);
		//余额 = 预交金-总费用(-在途费用) 
		BigDecimal ye = yjj.subtract(zfy);
		return ye;
	}
	
	/**
	 * 处理空值
	 * @param args
	 * @return
	 */
	private BigDecimal processNull(BigDecimal args){
		if(args == null){
			args = new BigDecimal(0);
		}
		return args;
	}
}
