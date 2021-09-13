package com.zebone.nhis.ma.pub.zsba.service;


import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.zsba.dao.BaPatiInfoMapper;
import com.zebone.nhis.ma.pub.zsba.vo.PvFunctionVo;
import com.zebone.nhis.ma.pub.zsba.vo.PvInfantLab;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class BaPatiInfoService {

    @Resource
    private BaPatiInfoMapper baPatiInfoMapper;

    /**
     * 根据患者就诊主键获取患者基本信息
     * @param param{pkPv}
     * @param user
     * @return
     */
    public PvInfantLab queryPvInfantByPkPv(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null ||"".equals(CommonUtils.getString(paramMap.get("pkPv")))){
            throw new BusException("请选择患者后重新操作！");
        }
        String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
        //查询患者信息
        PvInfantLab inf = baPatiInfoMapper.quePvInfantLab(pkPv);
        // 住院天数 -- 出院则用出院时间计算，未出院用当前日期
        if( inf.getDateBegin() != null && inf.getDateEnd() != null ){
            inf.setDays(DateUtils.getDateSpace(inf.getDateBegin(), inf.getDateEnd())+"");
        }else if (inf.getDateBegin() != null) {
            inf.setDays(DateUtils.getDateSpace(inf.getDateBegin(), new Date())+"");
        } else {
            inf.setDays("0");
        }
        //过敏史
        List<Map<String,Object>> piAl = DataBaseHelper.queryForList("select name_al from pi_allergic where  pk_pi=? and del_flag='0'", new Object[]{inf.getPkPi()});
        if(piAl!=null&&piAl.size()>=0){
            String strPiAl="";
            for(Map<String,Object> al : piAl){
                if(al!=null){
                    strPiAl+=al.get("nameAl")!=null ?al.get("nameAl").toString()+",":"";
                }
            }
            if(StringUtils.isNotBlank(strPiAl)){
                strPiAl=strPiAl.substring(0, strPiAl.length()-1);
                inf.setNameAl(strPiAl);
            }
        }
        return inf;
    }


    /**
     * 获取患者操作信息
     * @param param{dateBegin,dateEnd,codeOrName}
     * @param user
     * @return
     */
    public List<PvFunctionVo> queryPvFunctionByPkPv(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        //查询医生站操作记录
        List<PvFunctionVo> functionVos = baPatiInfoMapper.queryPvFunctionList(paramMap);
        return functionVos;
    }
}
