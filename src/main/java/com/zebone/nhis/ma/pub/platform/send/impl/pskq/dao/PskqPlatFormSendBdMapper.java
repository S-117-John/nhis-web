package com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao;

import com.zebone.nhis.ma.pub.platform.pskq.model.DrugItem;
import com.zebone.nhis.ma.pub.platform.pskq.model.MaterialItem;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderItem;
import com.zebone.nhis.ma.pub.platform.pskq.model.PriceItem;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.BdDefdocVO;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PskqPlatFormSendBdMapper {
	
	/**
	 * 查询收费项目
	 * @param map
	 * @return
	 */
    public PriceItem getBdItemInfo(Map<String,Object> map);


    
    /**
	 * 查询医嘱项目
	 * @param map
	 * @return
	 */
    public OrderItem getBdOrdInfo(Map<String,Object> map);
    /**
	 * 查询医嘱项目
	 * @param map
	 * @return
	 */
    public DrugItem getBdPdInfo(Map<String,Object> map);
    /**
     * 查询物资项目
     * @param map
     * @return
     */
    public MaterialItem getBdMaterInfo(Map<String,Object> map);

    List<BdDefdocVO> getBdDefdocList(List<String> list);
}
