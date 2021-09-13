package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.vo.CnBlIpDtVo;
import com.zebone.nhis.cn.ipdw.vo.CnOrderSyncVo;
import com.zebone.nhis.cn.ipdw.vo.PvDiagVo;
import com.zebone.nhis.cn.ipdw.vo.ReqTmpItemVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ma.tpi.ems.YjSq01;
import com.zebone.nhis.common.module.ma.tpi.ems.YjSq02;
import com.zebone.platform.modules.mybatis.Mapper;

/**医嘱同步Mapper
 * @author chengjia
 *
 */
@Mapper
public interface CnOrderSyncMapper {

   
   /**查询医嘱列表（同步）
	 * @param map
	 * @return
	 */
   public List<CnOrderSyncVo> queryCnOrderSync(Map<String,Object> map);
   
   /**查询医嘱费用列表（同步）
	 * @param map
	 * @return
	 */
   public List<CnBlIpDtVo> queryCnOrderIpBlDtSync(Map<String,Object> map);
   
   /**查询同步医嘱状态
	 * @param map
	 * @return
	 */
   public List<CnOrder> querySyncOrderStatus(Map<String,Object> map);
   
   /**查询同步医嘱已停止
	 * @param map
	 * @return
	 */
   public List<CnOrder> querySyncOrderStops(Map<String,Object> map);  
   
   /**
    * 调用医嘱同步存储过程
    * @param map
    */
   public void syncProcOrders(Map<String,Object> map);
   
   /**
    * 调用病人信息同步存储过程
    * @param map
    */
   public void syncPatEncInfo(Map<String,Object> map);
   
   /**查询同步诊断信息
	 * @param map
	 * @return
	 */
   public List<PvDiagVo> querySyncPvDiags(Map<String,Object> map);  
   
   
   /**
    * 调用HIS同步取序列号存储过程
    * @param map
    */
   public void createSyncHisSn(Map<String,Object> map);
   
   /**
    * 调用HIS同步取序列号存储过程(dt_getmax_ys)
    * @param map
    */
   public void createSyncHisSnYs(Map<String,Object> map);
   /**
    * 调用HIS同步取序列号存储过程(dt_getmax)
    * @param map
    */
   public void createSyncHisSnMax(Map<String,Object> map);
   
   
   /**获取申请单模板明细
	 * @param map
	 * @return
	 */
   public List<ReqTmpItemVo> queryReqTmpItems(Map<String,Object> map);
   
   /** 查询医技申请记录
	 * @param map
	 * @return
	 */
   public List<YjSq01> queryReqRecs(Map<String,Object> map);
   
   /** 查询医技申请明细
	 * @param map
	 * @return
	 */
   public List<YjSq02> queryReqItems(Map<String,Object> map);
   
   /**
    * 查询患者信息
    * @param map
    * @return
    */
   public List<Map<String,Object>> queryPatListSync(Map<String,Object> map); 

   
}
