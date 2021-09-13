package com.zebone.nhis.cn.opdw.dao;

import java.util.Map;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnOperationMapper {

    /**
     * 获取手术申请对应的医嘱
     * @param pkCnord
     * @return
     */
    CnOrder selectOrder(String pkCnord);

    /**
     * 删除手术申请
     * @param pkOrdop
     */
    void removeOpApply(String pkOrdop);
    /**
     * 删除手术医嘱
     * @param pkCnord
     */
    void removeOpOrder(String pkCnord);
    /**
     * 删除手术感染
     * @param pkCnord
     */
    void removeOpInfect(String pkCnord);
    /**
     * 删除申请对应的附加申请
     */
    void removeChildApply(String pkOrdop);
    /**
     * 删除申请对应的费用
     */
    void removeOpCg(String pkOrdop);
    /**
     * 查询手术信息
     * @param pkCnord
     * @return
     */
    Map<String,Object> queryOperation(String pkCnord);

    /**
     * 查询手术医嘱计费信息
     * @param pkCnord
     * @return
     */
    Map<String, Object> queryOpOrdAmount(String pkCnord);

}
