package com.zebone.nhis.webservice.zsrm.dao;

import com.zebone.nhis.ma.pub.zsrm.vo.DrugQryVo;
import com.zebone.nhis.ma.pub.zsrm.vo.MachDrug;
import com.zebone.nhis.webservice.zsrm.vo.pack.MachItem;
import com.zebone.nhis.webservice.zsrm.vo.pack.MachOrder;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmDrugPackMapper {

    /**
     * 查询配药完成的处方记录
     * @param paramMap
     * @return
     */
    List<MachOrder> getPresInfo(Map<String,Object> paramMap);

    /**
     * 查询西药药品信息
     * @param paramMap
     * @return
     */
    List<MachItem> getPresItems(Map<String,Object> paramMap);

    /**
     * 查询仓库药品信息
     * @param wesDrugQryVo
     * @return
     */
    List<MachDrug> getBdPdStoreInfo(DrugQryVo wesDrugQryVo);
}
