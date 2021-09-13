package com.zebone.nhis.cn.opdw.service;

import com.zebone.nhis.cn.opdw.dao.CnOpSettleInforMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Classname CnOpSettleInformation
 * 门诊结算费用查询
 * @Description TODO
 * @Date 2019-12-23 16:06
 * @Created by wuqiang
 */
@Service
public class CnOpSettleInforService {

    @Autowired
    private CnOpSettleInforMapper cnOpSettleInforMapper;
/**
 * @Description 查询门诊结算明细
 * @auther wuqiang
 * @Date 2019-12-23
 * @Param [param, user]
 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
 */
    public List<Map<String,Object>> qryChargeDetail(String param, IUser user){
        Map<String,Object> map = JsonUtil.readValue(param, Map.class);
        String itemcate = map.get("pkItemcate")==null?"":getItemcate(map.get("pkItemcate").toString());
        map.put("pkItemcate", itemcate);
        map.put("pkOrg", ((User)user).getPkOrg());
        return cnOpSettleInforMapper.qryChargeDetail(map);
    }
    /** *
     * @Description
     * 按开立科室项目查询汇总门诊结算
     * @auther wuqiang
     * @Date 2019-12-23
     * @Param [param, user]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public List<Map<String,Object>> qryChargeSum(String param,IUser user){

        Map<String,Object> map = JsonUtil.readValue(param, Map.class);
        String itemcate = map.get("pkItemcate")==null?"":getItemcate(map.get("pkItemcate").toString());
        map.put("priceOrg", map.get("priceOrg")==null?"":map.get("priceOrg").toString());
        map.put("pkItemcate", itemcate);
        map.put("pkOrg", ((User)user).getPkOrg());

        //对日期进行处理
        //map = setDate(map);
        return cnOpSettleInforMapper.qryChargeSum(map);
    }
    /**
     * @Description
     * 按项目汇总查询门诊结算费用
     * @auther wuqiang
     * @Date 2019-12-23
     * @Param [param, user]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public List<Map<String,Object>> qryItemSum(String param,IUser user){

        Map<String,Object> map = JsonUtil.readValue(param, Map.class);
        String itemcate = map.get("pkItemcate")==null?"":getItemcate(map.get("pkItemcate").toString());
        map.put("priceOrg", map.get("priceOrg")==null?"":map.get("priceOrg").toString());
        map.put("pkItemcate", itemcate);
        map.put("pkOrg", ((User)user).getPkOrg());

        return cnOpSettleInforMapper.qryItemSum(map);
    }


    private String getItemcate(String itemcates) {
        String rtn="";
        if(StringUtils.isNotBlank(itemcates)){
            String[] s = itemcates.split(",");
            if(s!=null&&s.length>0){
                for(String cate : s){
                    rtn+="'"+cate+"',";
                }
            }
            if(StringUtils.isNotBlank(rtn)) {rtn = "("+rtn.substring(0, rtn.length()-1)+")";}
        }
        return rtn;
    }

}
