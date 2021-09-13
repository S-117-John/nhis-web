package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.PdRepriceHistVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.st.vo.PdStoreInfoVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface StoreManagerMapper {
    /**
     * 查询仓库物品
     * @param map
     * @return
     */
    public List<PdStoreInfoVo> queryPdStoreInfoList(Map<String,Object> map);
    /**
     * 查看可用批次
     * @param map
     * @return
     */
    public List<PdStDtVo> queryStDtList(Map<String,Object> map);
    /**
     * 调价历史
     * @param map
     * @return
     */
    public List<PdRepriceHistVo> queryHistList(Map<String,Object> map);
    
    /**
     * 根据选择药品查看所有仓库库存
     * @param pkPd
     * @return
     */
    public List<Map<String,Object>> qryStockAll(String pkPd);
    
    /**
     * 查询当前仓库药品汇总信息
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> qryPdStockSum(Map<String,Object> paramMap);
}
