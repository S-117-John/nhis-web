package com.zebone.nhis.scm.pub.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.module.scm.st.PdStock;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.dao.PdStPubMapper;
import com.zebone.nhis.scm.pub.vo.PdBatchVo;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 批量确认批次模式出库
 *
 * @author yangxue
 */
@Service
public class PdStOutBatchPubService {
    @Resource
    private PdStOutHandler stOutHandler;

    @Resource
    private PdStPubMapper pdstPubMapper;

    /**
     * 药房发药执行出库
     *
     * @param pdList
     * @param pkDept    发药科室（与pk_store 二选一）
     * @param pkStore   发药仓库
     * @param euOuttype
     * @param pkOrgAp   请领机构（必填）
     * @param pkDeptAp  请领科室（必填）
     * @param flag_ip 是否是住院发药
     * @return PdOutDtParamVo:出库明细
     */
    public List<PdOutDtParamVo> execStOut(List<PdOutParamVo> pdList, String pkOrgAp, String pkDeptAp, String pkDept, String pkStore, String euOuttype, boolean flag_ip) {
        if(CollectionUtils.isEmpty(pdList)){
            return Lists.newArrayList();
        }
        if (CommonUtils.isEmptyString(pkStore))
            throw new BusException("未获取到出库仓库主键！");
        // 确定出库方式
        if (CommonUtils.isEmptyString(euOuttype)) {
            euOuttype = stOutHandler.queryEuOutType(pkStore);
        }
        List<String> pkPds = new ArrayList<String>();
        //校验申请数量是否大于0，不大于0全部出库失败
        StringBuffer pds = new StringBuffer("");
        for (PdOutParamVo paramVo : pdList) {
            String pk_pd = paramVo.getPkPd();
            if (paramVo.getQuanMin() <= 0)
                throw new BusException("药品" + stOutHandler.getPdNameByPk(pk_pd) + "的基本单位申请数量小于或等于0，无法进行出库！");
            pds.append("'").append(pk_pd).append("',");
            pkPds.add(pk_pd);
        }
        //TODO 锁定符合条件的药品批次,(但是下面使用的结果集并不一定是这里锁定的数据，其他地方插入新的条目且提交，下面就会查出来）
//        stOutHandler.lockConfirmPdSt(pkPds, pkStore);processStoreOut对结果集做了判断，等于将锁定延后到对入库扣减时处理
        // 校验库存是否够,库存不够全部出库失败
        List<Map<String, Object>> stklist = getPdStoreList(pds.substring(0, pds.length() - 1).toString(), pkStore, flag_ip);
        if (stklist == null || stklist.size() <= 0)
            throw new BusException("未获取到本次发放的物品所有的可用库存，无法完成出库！");
        for (Map<String, Object> stk : stklist) {
            for (PdOutParamVo paramVo : pdList) {
                if (paramVo.getPkPd().equals(stk.get("pkPd")) && MathUtils.compareTo(paramVo.getQuanMin(), CommonUtils.getDouble(stk.get("quanMin"))) > 0) {
                    throw new BusException("物品" + stOutHandler.getPdNameByPk(paramVo.getPkPd()) + "可用库存量不足，现库存数量为"
                            + MathUtils.div(CommonUtils.getDouble(stk.get("quanMin")), CommonUtils.getDouble(paramVo.getPackSize())));
                }
            }
        }
        //确认入库明细中的药品批次
        List<PdOutDtParamVo> dtlist = stOutHandler.confirmBatchPdSt(pds.substring(0, pds.length() - 1).toString(), pkDept, pkStore, euOuttype, pdList, flag_ip);
        // 创建出库单，并更新库存
        if (dtlist != null && dtlist.size() > 0) {
            List<PdOutDtParamVo> list = processStoreOut(pkOrgAp, pkDeptAp, pds, dtlist, pkStore, IDictCodeConst.DT_STTYPE_ORDOUT);// 医嘱出库
            return list;
        }
        return Lists.newArrayList();
    }

    /**
     * 处理库存 1）写表pd_st,pd_st_detail，生成出库记录
     * 2）更新pd_st_detail对应的入库记录；更新已出库数量quan_outstore=quan_outstore+出库数量；
     * 如果入库数量quan_min=quan_outstore，更新完成出库flag_finish=1； 3） 写表pd_stock；
     * 按照“仓库+物品+批号+失效日期+成本单价+零售单价”查询，查询到匹配记录后，
     * 更新库存数量quan_min=quan_min-对应批次数量，如果quan_min=0，且quan_prep=0，删除该库存记录。
     *
     * @param dtlist   ：出库明细
     * @param pk_store ：出库仓库
     * @param dttype   :出库类型
     * @return
     */
    public List<PdOutDtParamVo> processStoreOut(String pkOrgAp, String pkDeptAp, StringBuffer pds, List<PdOutDtParamVo> dtlist, String pk_store, String dttype) {
        User user = UserContext.getUser();
        // 查询是否当天生成过的发药出库单
        String pk_pdst = stOutHandler.getPdStIn(pkOrgAp, pkDeptAp, dttype, user, pk_store, "-1");
        if (CommonUtils.isEmptyString(pk_pdst)) {
            // 创建出入库单
            PdSt st = stOutHandler.createPdst(pkOrgAp, pkDeptAp, null, user, dttype, "-1");
            DataBaseHelper.insertBean(st);
            pk_pdst = st.getPkPdst();
        }
        List<PdStDetail> insert_list = new ArrayList<PdStDetail>();
        List<String> update_list = new ArrayList<String>();
        List<String> updateBatchStr = new ArrayList<>();
        List<String> delete_list = new ArrayList<String>();
        int i = 1;
        //确认库存表中的物品批次
        List<PdStock> stklist = queryPdStock(pds.substring(0, pds.length() - 1).toString(), pk_store);
        //按批次组装为hashMap，同时判断重复；
        Map<PdBatchVo, PdStock> batchStockMap = new HashMap<>();
        for (PdStock pdStock : stklist) {
            PdStock existKey = batchStockMap.put(new PdBatchVo(pdStock.getPkPd(), pdStock.getPrice(), pdStock.getPriceCost(), pdStock.getBatchNo(), pdStock.getDateExpire()), pdStock);
            if (existKey != null) {
                throw new BusException("药品【" + stOutHandler.getPdNameByPk(existKey.getPkPd()) + "(" + existKey.getBatchNo() + ")" + "】相同批次在库存表中出现多条，请先处理后重试！");
            }
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        for (PdOutDtParamVo dt : dtlist) {
           List<PdBatchVo> pdStockTempList= batchStockMap.keySet().stream().filter(m->dt.getPkPd().equals(m.getPkPd())
                   && ObjectUtils.nullSafeEquals(dt.getBatchNo(),m.getBatchNo())
                   && ObjectUtils.nullSafeEquals(dt.getDateExpire(),m.getDateExpire())
                   && MathUtils.abs(MathUtils.sub(m.getPrice(),dt.getPrice())).doubleValue()<0.01
                   && MathUtils.abs(MathUtils.sub(m.getPriceCost(),dt.getPriceCost())).doubleValue()<0.01
                   && MathUtils.sub(batchStockMap.get(m).getQuanMin(),dt.getQuanOutMin()).doubleValue()>=0
            ).collect(Collectors.toList());

            if (pdStockTempList==null || pdStockTempList.size()==0) {
                throw new BusException("未匹配到药品【" + dt.getName() + "(" + dt.getBatchNo() + ")" + "】库存记录！");
            }

            PdStock stk = batchStockMap.get(pdStockTempList.get(0));
            // 如果出库数量和库存量相等，并且quan_prep=0，删除该条库存记录，否则更新库存量
            paramMap.put("quan", dt.getQuanOutMin());
            paramMap.put("pkPdstock",stk.getPkPdstock());
            paramMap.put("packSize", dt.getPackSizePd());
            paramMap.put("dateNow", new Date());
            int cnt = DataBaseHelper.update("update pd_stock set quan_min =  quan_min - :quan " + ",amount =  (quan_min - :quan)/:packSize*price "
                            + ",amount_cost =  (quan_min - :quan)/:packSize*price_cost " + ",ts = :dateNow " + "where pk_pdstock = :pkPdstock and quan_min>=:quan ",
                    paramMap);
            if (cnt <= 0)
                throw new BusException("更新药品【" + dt.getName() + "(" + dt.getBatchNo() + ")" + "】,时，\n库存量【" + stk.getQuanMin() + "】小于出库数量【" + dt.getQuanOutMin() + "】，更新失败，请核对数据！");
            PdStDetail stdt = stOutHandler.createPdstdt(dt, null, user, pk_pdst, i);
            i++;
            // 更新原入库单的库存数量
            String pk_stdt = dt.getPkPdstdt();// 原入库单主键
            String update_sql=null;
            if (Application.isSqlServer()){
				update_sql = "update pd_st_detail set quan_outstore = isnull(quan_outstore,0) + " + dt.getQuanOutMin()
						+ ",ts = to_date('"
						+ DateUtils.getDefaultDateFormat().format(new Date()) + "','YYYYMMDDHH24MISS')"
						+ ",flag_finish=(case when (isnull(quan_outstore,0)+" + dt.getQuanOutMin()
						+ ")>=quan_min then '1' else '0' end) where pk_pdstdt = '"
						+ pk_stdt + "' and quan_min>=quan_outstore+" + dt.getQuanOutMin();
			}else {
				update_sql = "update pd_st_detail set quan_outstore = cast(nvl(quan_outstore,0) as DECIMAL(14,4)) + " + dt.getQuanOutMin()
						+ ",ts = to_date('"
						+ DateUtils.getDefaultDateFormat().format(new Date()) + "','YYYYMMDDHH24MISS')"
						+ ",flag_finish=(case when (cast(nvl(quan_outstore,0) as DECIMAL(14,4))+" + dt.getQuanOutMin()
						+ ")>=quan_min then '1' else '0' end) where pk_pdstdt = '"
						+ pk_stdt + "' and quan_min>=quan_outstore+" + dt.getQuanOutMin();
			}

            update_list.add(update_sql);
            updateBatchStr.add(dt.getName() + "(" + dt.getBatchNo() + ")");
            dt.setPkPdstdt(stdt.getPkPdstdt());// 更新为出库明细主键返回发药明细
            insert_list.add(stdt);
            delete_list.add("delete from pd_stock where pk_pdstock = '" + stk.getPkPdstock() + "' and quan_min<=0");
        }
        if (insert_list != null && insert_list.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), insert_list);
        }
        if (update_list != null && update_list.size() > 0) {
            int[] ints = DataBaseHelper.batchUpdate(update_list.toArray(new String[0]));
            for (int j = 0; j < ints.length; j++) {
                if (ints[j] == 0) {
                    throw new BusException("更新药品【" + updateBatchStr.get(j) + "】时,批次库存不足，请核对数据！");
                }
            }
        }
        if (delete_list != null && delete_list.size() > 0) {
            DataBaseHelper.batchUpdate(delete_list.toArray(new String[0]));
        }
        return dtlist;
    }

    /**
     * 查询物品库存信息
     *
     * @param pds
     * @param pk_store
     * @return
     */
    private List<PdStock> queryPdStock(String pds, String pk_store) {
        String sql = " select pk_pdstock,quan_min,quan_prep,price,pk_pd,price,date_expire,price_cost,batch_no "
                + " from  pd_stock where pk_pd in (" + pds + ") and pk_store = ? and quan_min>0 "
                + " order by pk_pd,quan_min-quan_prep desc";
        List<PdStock> stklist = DataBaseHelper.queryForList(sql, PdStock.class, new Object[]{pk_store});
        return stklist;
    }

    /**
     * 获取物品的库存量
     *
     * @param pk_pds
     * @param pk_store
     * @return
     */
    public List<Map<String, Object>> getPdStoreList(String pk_pds, String pk_store, boolean flag_ip) {
        StringBuffer sql = new StringBuffer("select sum(quan_min-quan_prep) quan_min ,bps.pk_pd from bd_pd_store bps");
        sql.append(" left join pd_stock ps on ps.pk_store=bps.pk_store and bps.pk_pd=ps.pk_pd");
        if (flag_ip) {
            sql.append(" and ps.flag_stop='0'");
        } else if (flag_ip == false) {
            sql.append(" and ps.flag_stop_op='0'");
        } else {
            sql.append(" and ps.flag_stop_er='0'");
        }
        sql.append(" where bps.pk_pd in (" + pk_pds + ") and bps.pk_store = ?");
        sql.append(" group by bps.pk_pd");
        List<Map<String, Object>> map = DataBaseHelper.queryForList(sql.toString(), new Object[]{pk_store});
        return map;
    }
}
