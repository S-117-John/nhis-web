package com.zebone.nhis.pro.zsba.cn.ipdw.service;

import com.zebone.nhis.pro.zsba.cn.ipdw.dao.OpOrdBaMapper;
import com.zebone.nhis.pro.zsba.cn.ipdw.vo.OpOrderVO;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *手术
 */
@Service
public class OpOrdBaService {

    @Autowired
    private OpOrdBaMapper opOrder;

    public List<OpOrderVO> queryOrderCheckList(String param, IUser user) throws ParseException {
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap.get("codeIp")==null){
            throw new BusException("未获取到患者就诊信息！");
        }
        return  opOrder.queryOrderCheckList(paramMap);
    }

    /**
     * 查询功能科室患者列表
     * @param param
     * @param user
     * @return
     * @throws ParseException
     */
    public List<Map<String,Object>> queryPiFuncInfo(String param,IUser user)throws ParseException{
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        return  opOrder.queryPiFuncInfo(paramMap);
    }
    
    /**
	 * 获取第三方手术相关信息
	 * 交易号：022003026053
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getThiInfoOrdBa(Map<String,Object> paramMap) {
		return opOrder.queryPtopOperatInfo(paramMap);
	}
}
