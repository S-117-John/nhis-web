package com.zebone.nhis.pro.zsba.ex.dao;

import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
/**
 * 执行单处理类
 * @author zhangtao
 *
 */
@Mapper
public interface OrderBaMapper {
	/**
	 * 根据患者获取待核对医嘱列表
     * @param map
     * @return
     */
    public List<OrderCheckVO> queryOrderCheckList(Map<String, Object> map);

    /**
     * 根据条件查询已核对医嘱列表
     *
     * @param map
     * @return
     */
    public List<OrderCheckVO> queryOrderEcList(Map<String, Object> map);

    /**
     * 根据医嘱主键医技医嘱记费所需数据
     */
    List<OrderCheckVO> getOrder(@Param("pklist") List<String> pklist);
}
