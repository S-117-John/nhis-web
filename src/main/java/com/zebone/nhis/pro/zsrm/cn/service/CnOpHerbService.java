package com.zebone.nhis.pro.zsrm.cn.service;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.pro.zsrm.cn.dao.CnOpHerbMapper;
import com.zebone.nhis.pro.zsrm.cn.vo.HerbVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CnOpHerbService {

    @Resource
    private CnOpHerbMapper opHerDao;

    /***
     * 查询草药明细对应的转换列表
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryHerPd(String param, IUser user){
        HerbVo paramVo= JsonUtil.readValue(param,HerbVo.class);
        if(paramVo==null || paramVo.getPkPds()==null || paramVo.getPkPds().size()==0)
            throw new BusException("请传入需要转换的物品主键！");
        List<Map<String,Object>> rtn=opHerDao.qryHerPd(paramVo);
        return rtn;
    }

    /**
     * 获取草药医嘱对应的明细
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>>  getHerbList(String param, IUser user){
        Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
        List<Map<String,Object>> ret = opHerDao.getHerbItems(paramMap);

        //特殊用法
        List<String> code=new ArrayList<>();
        if(ret!=null && ret.size()>0){
            //查询所有的特殊用法
            for (Map<String,Object> map:ret) {
                if(StringUtils.isNotEmpty(MapUtils.getString(map,"dtHerbusage"))){
                    String[] codes=MapUtils.getString(map,"dtHerbusage").split(",");
                    for (String s:codes){
                        if(!code.contains(s)){
                            code.add(s);
                        }
                    }
                }
            }

            if(code.size()>0){
                Map<String,Object> map=new HashMap<>();
                map.put("code",code);
                List<BdDefdoc> def=opHerDao.qryBdDefdoc(map);
                if(def!=null && def.size()>0){
                    for (Map<String,Object> vo:ret) {
                        if(StringUtils.isNotEmpty(MapUtils.getString(vo,"dtHerbusage"))){
                            String dtHerbusageName="";
                            String[] codes=MapUtils.getString(vo,"dtHerbusage").split(",");
                            for (String ss:codes) {
                                for(BdDefdoc name:def){
                                    if(name.getCode().equals(ss)){
                                        if(dtHerbusageName.length()!=0)
                                            dtHerbusageName=dtHerbusageName+","+name.getName();
                                        else
                                            dtHerbusageName=name.getName();
                                        break;
                                    }
                                }
                            }
                            vo.put("dtHerbusageName",dtHerbusageName);
                        }
                    }
                }
            }
        }

        return ret;
    }

}
