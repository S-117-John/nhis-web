package com.zebone.nhis.ma.pub.zsrm.service;

import com.zebone.nhis.common.module.base.transcode.SysApplog;
import com.zebone.nhis.ma.pub.zsrm.dao.ZsrmOpTransInHospitalMapper;
import com.zebone.nhis.ma.pub.zsrm.vo.ZsrmOpTransInHospitalVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 门急诊转入院服务接口
 */
@Service
public class ZsrmOpTransInHospitalService {

    @Resource
    private ZsrmOpTransInHospitalMapper opTransInHospitalMapper;

    /**
     * 保存老HIS入院通知单信息
     *
     * @param inHospitalVo
     */
    public void saveOldHisinHospital(ZsrmOpTransInHospitalVo inHospitalVo) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appId", inHospitalVo.getHisOrderId());
        List<ZsrmOpTransInHospitalVo> inHospitalVoList = opTransInHospitalMapper.getOldHisInfo(paramMap);
        if (inHospitalVoList == null || inHospitalVoList.size() == 0) {
            DataBaseHelper.insertBean(inHospitalVo);
        } else {
            ZsrmOpTransInHospitalVo oldopHospvo = inHospitalVoList.get(0);
            if ("1".equals(oldopHospvo.getStatus())) {
                throw new BusException("当前患者已办理入院，请到入院处处理！");
            } else {
                String sql="delete from zy_SyncMzPatInfo where hisOrderId=?";
                DataBaseHelper.execute(sql,new Object[]{inHospitalVo.getHisOrderId()});
                DataBaseHelper.insertBean(inHospitalVo);
            }
        }
    }

}