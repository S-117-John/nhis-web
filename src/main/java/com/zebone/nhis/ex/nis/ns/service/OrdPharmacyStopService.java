package com.zebone.nhis.ex.nis.ns.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.dao.OrdPharmacyStopMapper;
import com.zebone.nhis.ex.nis.ns.vo.OrdVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 药房医嘱停发处理
 *
 * @Auther: wuqiang
 * @Date: 2018/10/31 17:37
 * @Description:
 */
@Service
public class OrdPharmacyStopService {
    @Autowired
    private OrdPharmacyStopMapper stopMapper;

    /**
     * 功能描述：查询所有医嘱
     * 交易号：005002002076
     *
     * @param param
     * @param user
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author wuqiang
     * @date 2018/10/31
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> ordSelect(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (!CommonUtils.isNotNull(map)) {
            throw new BusException("请绑定机构");
        }
        String pkDept = map.get("pkDept").toString();
        List<Map<String, Object>> mapList = null;
        mapList = stopMapper.ordQuery(pkDept);
        return mapList;
    }

    /**
     * 功能描述：请领单处理，交易号005002002077
     * 由euResult进行判断
     * 1 无误处理，更新请领
     * 2 停止请领单处理，更新请领并取消执行单，清空请领明细
     * 3 作废请领单处理，更新请领并取消执行单，清空请领明细
     * @param param
     * @param user
     * @return void
     * @author wuqiang
     * @date 2018/10/31
     */
    public void ordStopUpdate(String param, IUser user) {
    	OrdVo ordVo = JsonUtil.readValue(param, OrdVo.class);
    	if (!CommonUtils.isNotNull(ordVo)) 
    		throw new BusException("未接收到前台医嘱数据！！！");
    	
    	if(CommonUtils.isNull(ordVo.getPkPdapdt()) || ordVo.getPkPdapdt().size() < 1)
    		throw new BusException("未接收到待处理的请领单明细！！！");
    	
    	User u = (User)user;
    	ordVo.setNameEmp(u.getNameEmp());
    	ordVo.setPkEmp(u.getPkEmp());
    	ordVo.setDateOpe(DateUtils.getDateTimeStr(new Date()));
    	Set<String> pdapdtSet = new HashSet<>(ordVo.getPkPdapdt());
    	ordVo.setPkPdapdts(CommonUtils.convertSetToSqlInPart(pdapdtSet, "pk_pdapdt"));
    	stopMapper.ordStopUpdate(ordVo);//更新请领明细
    	
    	//作废|停止时需要更新执行单相关信息
    	if(CommonUtils.isNotNull(ordVo.getPkExocc()) && ordVo.getPkExocc().size() > 0){
    		Set<String> occSet = new HashSet<>(ordVo.getPkExocc());
        	ordVo.setPkExoccs(CommonUtils.convertSetToSqlInPart(occSet, "pk_exocc"));  
        	stopMapper.updateExlist(ordVo);//更新执行单相关信息
    	}
    		
    }

}
