package com.zebone.nhis.cn.pub.dao;

import com.zebone.nhis.cn.pub.vo.PdStrockVo;
import com.zebone.nhis.cn.pub.vo.QueryCnEmyImgSign;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderPubMapper {
  /**
   * 查询医嘱信息列表
   * @param paramMap{pkPv必传}
   * @return
   * @throws BusException
   */
  public List<Map<String,Object>> queryOrderList(Map<String,Object> paramMap)throws BusException;
  
  /**
   * 打印医嘱时查询医嘱
   * @param map
   * @return
   */
  List<Map<String, Object>> printQryCnOrder(Map<String, Object> map);
  
  /**
   * 查询医嘱关联的执行科室
   * @param paramMap
   * @return
   */
  public List<Map<String,Object>> qryOrdExecDept(Map<String,Object> paramMap) ;

  List<Map<String,Object>> qryOrdExecDepts(Map<String,Object> paramMap) ;

  public  List<QueryCnEmyImgSign> queryCnEmyImgSignList(String pkPv);
  
  /**查询医嘱CA认证信息*/
  public List<Map<String,Object>> qryqryOrderCaInfo(@Param("pkList") List<String> pkList);
  
  /** 查询医嘱信息 **/
  public List<Map<String,Object>> qryOrdMes(Map<String,Object> paramMap) ;

  /**查询医嘱计费信息**/
  public  List<Map<String,Object>> qryOrdSettle(Map<String,Object> paramMap) ;

  /**查询医嘱附加属性信息**/
  public  List<Map<String,Object>> qryOrdAtt(Map<String,Object> paramMap) ;

  List<Map<String,Object>> qryPdInd(Map<String,Object> paramMap);

  List<Map<String,Object>> qryPdHerInd(Map<String,Object> paramMap);

  List<Map<String,Object>> qryPdIph(Map<String,Object> paramMap);
  /**药理分类**/
  List<PdStrockVo> qryPdType();
  /**药品查询*/
  List<Map<String,Object>> qryPd(Map<String,Object> paramMap);
}