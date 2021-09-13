package com.zebone.nhis.pro.zsba.pv.service;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 12370
 * @Classname ZsbaPvIpNurseService
 * @Description 住院护士站相关内容
 * @Date 2021-06-07 9:50
 * @Created by wuqiang
 */
@Service
public class ZsbaPvIpNurseService {

    /**
     * @return void
     * @Description 出院时刷新记费时间超出出院时间的费用
     * 刷新锚定点出院医嘱 的开始时间
     * @auther wuqiang
     * @Date 2021-06-07
     * @Param [param, user]
     */

    public void updateDataHapBeforeLeaveHos(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        if (StringUtils.isBlank(pkPv)) {
            return;
        }
        String sql = "select top 1 DATE_START " +
                "from CN_ORDER CO where CODE_ORDTYPE='1102' and EU_STATUS_ORD='3' and PK_PV=? order by  DATE_START desc ";
        CnOrder cnOrder = DataBaseHelper.queryForBean(sql, CnOrder.class, pkPv);
        if (cnOrder == null) {
            return;
        }
        sql = "select PK_CGIP " +
                "from BL_IP_DT BID where DATE_HAP>? and PK_PV=? ";
        List<BlIpDt> blIpDts = DataBaseHelper.queryForList(sql, BlIpDt.class, new Object[]{cnOrder.getDateStart(), pkPv});
        if (blIpDts==null||blIpDts.size()<1){
            return;
        }
         blIpDts.forEach(m-> m.setDateHap(DateUtils.getDateMorning(cnOrder.getDateStart(),0)));
        DataBaseHelper.batchUpdate( " update  BL_IP_DT set DATE_HAP=:dateHap where PK_CGIP=:pkCgip",blIpDts);
    }
}
