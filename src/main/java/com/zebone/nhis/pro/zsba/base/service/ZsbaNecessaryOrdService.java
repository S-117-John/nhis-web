package com.zebone.nhis.pro.zsba.base.service;

import com.zebone.nhis.pro.zsba.base.dao.ZsbaNecessaryOrdMapper;
import com.zebone.nhis.pro.zsba.base.vo.DeptNecOrdDtVo;
import com.zebone.nhis.pro.zsba.base.vo.DeptNecOrdGroupVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ZsbaNecessaryOrdService {
    @Resource
    private ZsbaNecessaryOrdMapper zsbaNecessaryOrdMapper;

    /**
     * 查询科室必要医嘱组
     * @param param
     * @param user
     * @return
     */
    public List<DeptNecOrdGroupVo> qryNecOrdGroup(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
        List<DeptNecOrdGroupVo> deptNecOrdVoList = zsbaNecessaryOrdMapper.getDeptNecOrdGroupInfo(paramMap);
        return deptNecOrdVoList;
    }

    /**
     * 查询医嘱明细
     * @param param
     * @param user
     * @return
     */
    public List<DeptNecOrdDtVo> qryOrddt(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
        List<DeptNecOrdDtVo> deptNecOrdDtVoList = zsbaNecessaryOrdMapper.getDeptNecOrdDtInfo(paramMap);
//        if (CollectionUtils.isEmpty(deptNecOrdDtVoList)) throw new BusException("未查询到医嘱明细");
        return deptNecOrdDtVoList;
    }

    /**
     * 保存医嘱组
     * @param param
     * @param user
     */
    public void saveOrdGroup(String param, IUser user){
        List<DeptNecOrdGroupVo> deptNecOrdGroupVoList = JsonUtil.readValue(param, new TypeReference<List<DeptNecOrdGroupVo>>() {});
        if (CollectionUtils.isEmpty(deptNecOrdGroupVoList)) throw new BusException("Failed to get the contents to be saved");
        for (DeptNecOrdGroupVo deptNecOrdGroupVo : deptNecOrdGroupVoList) {
            if (StringUtils.isBlank(deptNecOrdGroupVo.getPkOrdnec())){
                deptNecOrdGroupVo.setPkOrdnec(NHISUUID.getKeyId());
                DataBaseHelper.insertBean(deptNecOrdGroupVo);
            }else {
                DataBaseHelper.updateBeanByPk(deptNecOrdGroupVo,false);
            }
            List<DeptNecOrdDtVo> deptNecOrdDtVoList = deptNecOrdGroupVo.getOrdDtList();
            if (CollectionUtils.isNotEmpty(deptNecOrdDtVoList)){
                for (DeptNecOrdDtVo deptNecOrdDtVo : deptNecOrdDtVoList) {
                    if (StringUtils.isBlank(deptNecOrdDtVo.getPkOrdnecdt())){
                        deptNecOrdDtVo.setPkOrdnecdt(NHISUUID.getKeyId());
                        deptNecOrdDtVo.setPkOrdnec(deptNecOrdGroupVo.getPkOrdnec());
                        DataBaseHelper.insertBean(deptNecOrdDtVo);
                    }else {
                        DataBaseHelper.updateBeanByPk(deptNecOrdDtVo,false);
                    }

                }
            }
        }

    }

    /**
     * 保存医嘱明细
     * @param param
     * @param user
     */
    public void saveOrddt(String param, IUser user){
        List<DeptNecOrdDtVo> deptNecOrdDtVoList = JsonUtil.readValue(param, new TypeReference<List<DeptNecOrdDtVo>>() {});
        if (CollectionUtils.isEmpty(deptNecOrdDtVoList)) throw new BusException("Failed to get the contents to be saved");
        for (DeptNecOrdDtVo deptNecOrdDtVo : deptNecOrdDtVoList) {
            if (StringUtils.isBlank(deptNecOrdDtVo.getPkOrdnecdt())){
                deptNecOrdDtVo.setPkOrdnecdt(NHISUUID.getKeyId());
                DataBaseHelper.insertBean(deptNecOrdDtVo);
            }else {
                DataBaseHelper.updateBeanByPk(deptNecOrdDtVo,false);
            }
        }
//        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(DeptNecOrdDtVo.class),deptNecOrdDtVoList);
    }

    /**
     * 删除医嘱组
     * @param param
     * @param user
     */
    public void deleteOrd(String param, IUser user){
        String pkOrdnec = JsonUtil.getFieldValue(param,"pkOrdNec");
        if (pkOrdnec == null || "".equals(pkOrdnec)){
            throw new BusException("未获取到需要删除的医嘱组信息");
        }else {
            int i = DataBaseHelper.execute("delete from BD_ORD_NEC where pk_ordnec = ?",pkOrdnec);
            if (i == 0) throw new BusException("删除医嘱组失败，请联系管理员");
            DataBaseHelper.execute("delete from BD_ORD_NEC_DT where pk_ordnec = ?",pkOrdnec);
        }
    }
    /**
     * 删除医嘱明细
     * @param param
     * @param user
     */
    public void deleteOrdDt(String param, IUser user){
        String pkOrdnecdt = JsonUtil.getFieldValue(param,"pkOrdnecdt");
        if (pkOrdnecdt == null || "".equals(pkOrdnecdt)){
            throw new BusException("未获取到需要删除的医嘱组信息");
        }else {
            int i = DataBaseHelper.execute("delete from BD_ORD_NEC_DT where pk_Ordnecdt = ?",pkOrdnecdt);
            if (i == 0) throw new BusException("删除医嘱明细失败，请联系管理员");
        }
    }

}
