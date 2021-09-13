package com.zebone.nhis.pro.sd.cn.service;

import com.zebone.nhis.pro.sd.cn.dao.CnPaitQryMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 深大，患者查询客户化逻辑
 *@author leiminjian
 */
@Service
public class CnPaitQryService {

    @Resource
    private CnPaitQryMapper cnQryDao;

    /**
     * 检验/检查列表
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryPaitBs(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("pkPv")==null){
            throw new BusException("未获取到查患者的主键");
        }
        return cnQryDao.qryLabRis(paramMap);
    }
    //查询检查
    public List<Map<String,Object>> qryLab(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("pkCnord")==null){
            throw new BusException("未获取到查询主键");
        }
        return cnQryDao.qryLab(paramMap);
    }
    //查询检验
    public List<Map<String,Object>> qryRis(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("pkCnord")==null){
            throw new BusException("未获取到查询主键");
        }
        return cnQryDao.qryRis(paramMap);
    }

    /**
     * 检验微生物
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryBact(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("pkCnord")==null){
            throw new BusException("未获取到查询主键");
        }
        return cnQryDao.qryBact(paramMap);
    }

    /**
     * 入院通知单
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryAdmissionNotice(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("pkPv")==null){
            throw new BusException("未获取到查询主键");
        }
        return cnQryDao.qryAdmNotice(paramMap);
    }

    /**
     * 会诊申请信息--列表
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryConsultApp(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("pkPv")==null){
            throw new BusException("未获取到查询主键");
        }
        return cnQryDao.qryConsultApp(paramMap);
    }

    /**
     * 申请单详细信息
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryConsultAppDetailed(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("pkCnord")==null){
            throw new BusException("未获取到查询主键");
        }
        return cnQryDao.qryConsultAppDetailed(paramMap);
    }

    /**
     * 受邀科室信息
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryBeInvited(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("pkCons")==null){
            throw new BusException("未获取到查询主键");
        }
        return cnQryDao.qryBeInvited(paramMap);
    }

    /**
     * 会诊应答列表
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryConsultAnswer(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("pkCons")==null){
            throw new BusException("未获取到查询主键");
        }
        return cnQryDao.qryConsultAnswer(paramMap);
    }

    /**
     * 应答明细
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryResponseDetails(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null){
            throw new BusException("未获取到查询的条件");
        }
        //校验其他条件
        if(paramMap.get("consrep")==null){
            throw new BusException("未获取到查询主键");
        }
        return cnQryDao.qryResponseDetails(paramMap);
    }
    
    /**
	 * 
	 * 查询患者就诊挂号医保类型 (深大)
	 * 
	 * 
	 * @param param
	 * @return user
	 * @throws
	 * 
	 * @author zhouyi
	 * @date 2021年1月25日
	 */
	public String getPibasePersonType(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = null;
        String personName = null;
        if (paramMap.get("pkPv") != null) {
        	pkPv = paramMap.get("pkPv").toString();
        }
		List<Map<String, Object>> personTypeName = DataBaseHelper.queryForList(
				" select dict.NAME from  ins_szyb_visit visit " + 
				" left join INS_SZYB_DICT dict on visit.PERSONTYPE = dict.CODE and code_type='AKA130' and EU_HPDICTTYPE ='01' " + 
				" WHERE visit.PK_PV = ? order by visit.DATE_REG asc ",
				pkPv);
		if (personTypeName != null && personTypeName.size()>0) {
			personName = (personTypeName.get(0).get("name").toString());
		} 

		return personName;
	}

}
