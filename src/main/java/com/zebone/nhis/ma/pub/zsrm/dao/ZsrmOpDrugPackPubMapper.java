package com.zebone.nhis.ma.pub.zsrm.dao;

import com.zebone.nhis.ma.pub.zsrm.vo.DrugQryVo;
import com.zebone.nhis.ma.pub.zsrm.vo.MachDrug;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 中山人民医院对外接口
 */
@Mapper
public interface ZsrmOpDrugPackPubMapper {

    /**
     * 获取处方性质，根据处方性质分发至各个系统
     * @param pkPresocces
     * @return
     */
    public List<Map<String,Object>> getPresTypeByPk(List<String> pkPresocces);

    String getWesOrder(DrugQryVo wesDrugQryVo);

    List<MachDrug> getBdPdStoreInfoByPk(DrugQryVo drugQryVo);
}
