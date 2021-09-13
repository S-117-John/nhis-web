package com.zebone.nhis.scm.material.service;

import java.util.*;

import javax.annotation.Resource;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.scm.pub.vo.MtlGenStParamVo;
import com.zebone.nhis.scm.pub.vo.*;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;
import com.zebone.nhis.common.module.scm.st.PdDeptusing;
import com.zebone.nhis.common.module.scm.st.PdSingle;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.scm.material.dao.MtlPdStMapper;
import com.zebone.nhis.scm.pub.service.MtlPdStOutHandler;
import com.zebone.nhis.scm.pub.service.MtlPdStPubService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 出库处理
 *
 * @author wj
 */

@Service
public class MtlPdOutStService {

    @Resource
    private MtlPdStMapper pdStoreMapper;

    @Resource
    private MtlPdStPubService mtlPdStPubService;

    @Resource
    private MtlPdStOutHandler stOutHandler;

    /**
     * 查询待出库列表
     *
     * @param param{codePlan,dateBegin,dateEnd,pkOrg,pkDept,pkStore}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryToOutList(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        map.put("pkStoreSrv", ((User) user).getPkStore());
        List<Map<String, Object>> list = pdStoreMapper.queryToOutPdStList(map);
        return list;
    }

    /**
     * 根据调拨申请单构造出库明细{pkPdplan,pkStore}
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PdStDtVo> createOutDtList(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map == null) throw
                new BusException("未获取到待出库单主键！");
        String pk_store = CommonUtils.getString(map.get("pkStore"));
        map.put("pkStoreSrv", pk_store);
        List<PdPlanDtVo> plandtlist = pdStoreMapper.queryToOutPdStDtList(map);
        if (plandtlist == null || plandtlist.size() <= 0)
            throw new BusException("未获取到待出库单明细，请核对申请物品是否正确设置了包装单位及生产厂商!");

        List<PdStDtVo> dtlist = new ArrayList<PdStDtVo>();
        List<String> pkPds = new ArrayList<String>();
        for (int i = 0, len = plandtlist.size(); i < len; i++) {
            pkPds.add(plandtlist.get(i).getPkPd());
        }

        //加锁物品
        stOutHandler.lockConfirmPdSt(pkPds, pk_store);

        //确认出库批次
        for (PdPlanDtVo plan : plandtlist) {
            MtlGenStParamVo genStParamVo = MtlGenStParamVo.newBuilder()
                    .pkPd(plan.getPkPd()).pkStore(pk_store).quanMin(plan.getQuanMin()).pdCode(plan.getCodePd())
                    .pdName(plan.getNamePd()).spec(plan.getSpec()).spcode(plan.getSpcode())
                    .factory(plan.getFactoryName()).unitName(plan.getUnitName()).pkPdplandt(plan.getPkPdplandt())
                    .unitPd(plan.getUnitPd()).flagUse(plan.getFlagUse()).build();
            dtlist.addAll(genStDt(genStParamVo));
        }
        dtlist = getPdStDtVos(map, dtlist);
        return dtlist;
    }

    /**
     *
     * 如果仓库的出库模式为“选择出库”时，且是在后端确认批次时，按照需求是在界面进行批次选择，
     * 所以这里相同物品，只保留其中一个记录.将数量叠加返回到界面。
     * 界面选择完具体的批次后，按照指定批次的价格重新处理出库明细
     */
    private List<PdStDtVo> getPdStDtVos(Map<String, Object> map, List<PdStDtVo> dtlist) {
        boolean batchConfirmInPage = MapUtils.getBooleanValue(map, "batchConfirmInPage", false);
        if (!batchConfirmInPage &&
                CollectionUtils.isNotEmpty(dtlist) &&
                EnumerateParameter.THREE.equals(stOutHandler.queryEuOutType(CommonUtils.getString(map.get("pkStore"))))) {
            List<PdStDtVo> batchList = Lists.newArrayList(dtlist.get(0));
            for (int i = 1; i < dtlist.size(); i++) {
                PdStDtVo stDt = dtlist.get(i);
                PdStDtVo match = null;
                if ((match = (PdStDtVo) CollectionUtils.find(batchList, new BeanPropertyValueEqualsPredicate("pkPd", stDt.getPkPd()))) == null) {
                    batchList.add(stDt);
                } else {
                    //如果库存是不同批次，将库存数量也加起来（完全一样的批次不能加）
                    if(!buildMtlPdBatchVo(stDt).equals(buildMtlPdBatchVo(match))){
                        match.setQuanStk(MathUtils.add(match.getQuanStk(), stDt.getQuanStk()));
                    }
                    //数量叠加
                    match.setQuanMin(MathUtils.add(match.getQuanMin(), stDt.getQuanMin()));
                    match.setQuanPack(MathUtils.div(match.getQuanMin(), match.getPackSize().doubleValue()));
                }
            }
            return batchList;
        }
        return dtlist;
    }

    public MtlPdBatchVo buildMtlPdBatchVo(PdStDtVo stDt){
        return new MtlPdBatchVo(stDt.getPkPd(),stDt.getPrice(),stDt.getPriceCost(),stDt.getBatchNo(),stDt.getDateExpire());
    }
    /**
     * 根据选择物品确认出库明细
     *
     * @param param{PdStDtVo}
     * @param user
     * @return
     */
    public List<PdStDtVo> confirmStoreInfoByPd(String param, IUser user) {
        PdStDtVo vo = JsonUtil.readValue(param, PdStDtVo.class);
        if (vo == null) throw new BusException("未获取到物品信息！");
        String pk_pd = vo.getPkPd();
        String pk_store = ((User) user).getPkStore();
        MtlGenStParamVo genStParamVo = MtlGenStParamVo.newBuilder()
                .pkPd(pk_pd).pkStore(pk_store).quanMin(vo.getQuanMin()).pdCode(vo.getPdcode())
                .pdName(vo.getPdname()).spec(vo.getSpec()).spcode(vo.getSpcode())
                .factory(vo.getFactory()).unitName(vo.getUnit())
                .unitPd(vo.getUnitPd()).build();
        List<PdStDtVo> stlist = genStDt(genStParamVo);
        return stlist;
    }

    /**
     * 删除出库单
     *
     * @param param{pkPdst值}
     * @param user
     */
    public void deletePdSt(String param, IUser user) {
        DataBaseHelper.execute("update pd_single set flag_out=0,pk_pdstdt_out=null where eu_status = '1' and pk_pdstdt_out in(select pk_pdstdt from pd_st_detail where pk_pdst=?)", new Object[]{JsonUtil.readValue(param, String.class)});
        mtlPdStPubService.deletePdst(param, user);
    }

    /**
     * 查询出库单
     *
     * @param param{dtSttype,pkOrgLk,pkDeptLk,pkStoreLk,codeSt,dateBegin,dateEnd,pkEmpOp,euStatus}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryPdStOutList(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map == null)
            map = new HashMap<String, Object>();

        map.put("pkStoreSt", ((User) user).getPkStore());
        map.put("euDirect", "-1");
        List<Map<String, Object>> list = pdStoreMapper.queryPdStByCon(map);
        return list;
    }

    /**
     * 查询出库明细
     *
     * @param param{pkPdst}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryPdStDtOutList(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map == null) throw new BusException("未获取到出库单主键！");
        map.put("pkStore", ((User) user).getPkStore());
        List<Map<String, Object>> list = pdStoreMapper.queryPdStDtOutList(map);
        return list;
    }

    /**
     * 保存出库信息
     *
     * @param param{PdStVo}
     * @param user
     */
    public PdStVo savePdSt(String param, IUser user) {
        PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
        String euDirect = JsonUtil.getFieldValue(param, "euDirect").toString();
        if (CommonUtils.isEmptyString(stvo.getPkPdst())) {
            //查询一下通业务类型下单号是否重复
            int isCodeDup = DataBaseHelper.queryForScalar("select count(1) from pd_st where dt_sttype=? and  code_st=?", Integer.class, new Object[]{stvo.getDtSttype(), stvo.getCodeSt()});
            if (isCodeDup != 0) {
                stvo.setCodeSt(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_CKCLD));
            }
        }
        return mtlPdStPubService.savePdSt(stvo, user, "", euDirect);
    }

    /**
     * 审核出库单
     *
     * @param param
     * @param user
     */
    public void submitPdSt(String param, IUser user) {
        PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
        User u = (User) user;
        if (stvo == null || stvo.getDtlist() == null || stvo.getDtlist().size() <= 0)
            throw new BusException("未获取到出库单信息！");
        String pkPdst = stvo.getPkPdst();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPdst", pkPdst);
        paramMap.put("pkEmp", u.getPkEmp());
        paramMap.put("nameEmp", u.getNameEmp());
        paramMap.put("dateChk", new Date());
        String pk_store = stvo.getPkStoreSt();

        //获取物品主键
        List<String> pkPds = Lists.transform(stvo.getDtlist(), new Function<PdStDtVo, String>() {
            public String apply(PdStDtVo pdStDtVo) {
                return pdStDtVo.getPkPd();
            }
        });
        //加锁物品
        stOutHandler.lockConfirmPdSt(pkPds, pk_store);

        if (!CommonUtils.isEmptyString(stvo.getPkPdplan())) {
            batchLookFor(stvo, pk_store, false);
            //审核前先保存待入库单的信息到入库单及明细表,同时将状态置为审核
            //科室领用不能设置 目的库存仓库，否则审核完出库，会出现在待入库，业务逻辑错误。
            if ("0202".equals(stvo.getDtSttype())) {
                stvo.setPkStoreLk(null);
            }
            mtlPdStPubService.savePdSt(stvo, user, "", "-1");//最后一个参数业务类型由前台设置
            pkPdst = stvo.getPkPdst();
            //更新调拨申请单明细对应的状态
            updatePlanDt(stvo.getDtlist(), u);
        } else {
            batchLookFor(stvo, pk_store, true);
            //更新出库信息
            mtlPdStPubService.updatePdSt(paramMap);
        }

        //更新库存量，并更新入库明细
        mtlPdStPubService.updateOutStore(stvo.getDtlist(), u.getPkStore());
        //更新单品状态
        mtlPdStPubService.updatePdSingleStatus(EnumerateParameter.NEGA, pkPdst, EnumerateParameter.NINE, true);

        savePdDeptUsing(stvo, stvo.getDtlist(), stvo.getDtSttype());
    }

    /**
     * 主动选择了对应入库批次的，重新构造出库批次明细；没有选择批次的自动寻批<br>
     * 新增模式下，支持部分自动、部分选择。<br>
     * 自动确认批次时，就按照界面传入来确认批次。在审核界面手动选择了批次的，就完全按照选择批次和数量来出。
     * 如果后面全部在前端选择确认批次，后台不用做处理，此方法什么都不做。autoBatch = true即可<br>
     * 1.由待出库到出库界面的，是已经自动寻批处理过的，所以不用再次处理“自动寻批”<br>
     * 2.新增的出库，如果没有手动“选择批次”，那么这里“自动寻批”<br>
     * 3.单品的关系变更已经处理<br>
     * 4.出库对应的入库相同批次记录已经合并（前台要控制不能传入相同PKPD？相同时批次？单品如何处理？这些都对应的是物品）
     *
     * @param stvo
     * @param pk_store
     * @param autoBatch 是否允许自动寻批
     */
    private void batchLookFor(PdStVo stvo, String pk_store, boolean autoBatch) {
        if (stvo.getBatchConfirmInPage() != null && stvo.getBatchConfirmInPage().booleanValue()) {
            return;
        }
        if (CollectionUtils.isNotEmpty(stvo.getDtlist())) {
            User user = UserContext.getUser();
            List<PdStDtVo> listDetail = stvo.getDtlist();
            List<PdStDtVo> newBatchList = Lists.newArrayList();
            Iterator<PdStDtVo> iterator = listDetail.iterator();
            int n = 0;
            List<PdStDetail> delList = Lists.newArrayList();
            List<PdStDtVo> newList = Lists.newArrayList();
            List<PdSingle> singleList = Lists.newArrayList();
            while (iterator.hasNext()) {
                PdStDtVo dt = iterator.next();
                n++;
                if (CollectionUtils.isNotEmpty(dt.getBatchList())) {
                    List<PdStDtVo> chBath = getStdtByBatch(n, delList, newList, dt);

                    //单品处理，注意单品的单位转换导致单品库存数和单品记录数不对应的，暂时不考虑~~~
                    boolean haveSource = StringUtils.isNotBlank(dt.getPkPdPlandt());
                    List<PdSingle> pdSingleList = haveSource ? dt.getSinList() : getStDtSingle(n, dt);
                    if(CollectionUtils.isNotEmpty(pdSingleList)){
                        int singleIndex = 0;
                        for(PdStDtVo dtVo:chBath){
                            if(haveSource){
                                int endIndex = dtVo.getQuanPack().intValue() > pdSingleList.size() ? pdSingleList.size() : dtVo.getQuanPack().intValue();
                                dtVo.setSinList(pdSingleList.subList(singleIndex, endIndex));
                                singleIndex += endIndex;
                            } else {
                                singleIndex += reBindStDtSingle(pdSingleList, dtVo, singleIndex);
                            }
                        }
                        if (CollectionUtils.isNotEmpty(pdSingleList)) {
                            singleList.addAll(pdSingleList);
                        }
                    }

                    newBatchList.addAll(chBath);
                    iterator.remove();
                } else {
                    if (autoBatch) {
                        //自动寻批 ~~ 只按照物品和数量去寻批
//                        MtlPdBatchVo batchVo = new MtlPdBatchVo();
//                        batchVo.setPrice(MathUtils.upRound(MathUtils.mul(MathUtils.div(dt.getPrice(), (double) dt.getPackSize()), (double) dt.getPackSizePd()), 6));
//                        batchVo.setPriceCost(MathUtils.upRound(MathUtils.mul(MathUtils.div(dt.getPriceCost(), (double) dt.getPackSize()), (double) dt.getPackSizePd()), 6));
//                        batchVo.setPkPd(dt.getPkPd());
//                        batchVo.setBatchNo(dt.getBatchNo());
//                        batchVo.setDateExpire(dt.getDateExpire());
                        MtlGenStParamVo genStParamVo = MtlGenStParamVo.newBuilder()
                                .pkPd(dt.getPkPd()).pkStore(pk_store).quanMin(dt.getQuanMin()).pdCode(dt.getPdcode())
                                .pdName(dt.getPdname()).spec(dt.getSpec()).spcode(dt.getSpcode())
                                .factory(dt.getFactory()).unitName(dt.getUnit()).unitPd(dt.getUnitPd())
//                                .mtlPdBatchVo(batchVo)
                                .build();
                        List<PdStDtVo> stlist = genStDt(genStParamVo);
                        //得到单品对应关系，自动寻批后重新绑定关系
                        List<PdSingle> pdSingleList = getStDtSingle(n, dt);
                        int singleIndex = 0;

                        for (int i = 0; i < stlist.size(); i++) {
                            final PdStDtVo dtVo = stlist.get(i);
                            dtVo.setCreateTime(new Date());
                            dtVo.setCreator(user.getPkEmp());
                            dtVo.setTs(new Date());
                            dtVo.setPkOrg(user.getPkOrg());
                            dtVo.setPkPdstdt(NHISUUID.getKeyId());
                            dtVo.setPkPdst(dt.getPkPdst());
                            dtVo.setFlagSingle(dt.getFlagSingle());
                            //将单品关系重新绑定
                            if (CollectionUtils.isNotEmpty(pdSingleList)) {
                                singleIndex += reBindStDtSingle(pdSingleList, dtVo, singleIndex);
                            }
                        }

                        PdStDetail pdStDetail = new PdStDetail();
                        pdStDetail.setPkPdstdt(dt.getPkPdstdt());
                        delList.add(pdStDetail);
                        newList.addAll(stlist);
                        if (CollectionUtils.isNotEmpty(pdSingleList)) {
                            singleList.addAll(pdSingleList);
                        }
                        newBatchList.addAll(stlist);
                        iterator.remove();
                    }
                }
            }
            if (delList.size() > 0) {
                //删除原来的记录,插入新的记录
                DataBaseHelper.batchUpdate("delete from pd_st_detail where pk_pdstdt = :pkPdstdt ", delList);
            }
            if (newList.size() > 0) {
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), newList);
            }
            if (CollectionUtils.isNotEmpty(singleList)) {
                //重绑单品关系
                DataBaseHelper.batchUpdate("update pd_single set pk_pdstdt_out=:pkPdstdtOut where pk_single=:pkSingle and eu_status=1", singleList);
            }
            if (newBatchList.size() > 0) {
                listDetail.addAll(newBatchList);
            }
        }
    }

    private List<PdStDtVo> getStdtByBatch(int n, List<PdStDetail> delList, List<PdStDtVo> newList,PdStDtVo dt) {
        boolean flag = StringUtils.isBlank(dt.getPkPdPlandt());
        List<PdStDtBatchVo> listBatch = dt.getBatchList();
        User user = UserContext.getUser();
        List<PdStDtVo> newBatchList = Lists.newArrayList();
        for (int i = 0; i < listBatch.size(); i++) {
            PdStDtBatchVo batchVo = listBatch.get(i);
            PdStDtVo dtInVo = DataBaseHelper.queryForBean("select dt.pk_pdstdt,dt.PK_PD,pd.code pdcode,pd.NAME pdname,pd.SPEC,pd.flag_single," +
                            " pd.SPCODE,pd.PK_FACTORY,pd.PK_UNIT_MIN unit_pd,dt.pk_unit_pack,dt.PK_UNIT_PACK unit_name_store,pd.PACK_SIZE pack_size_pd," +
                            " dt.price,dt.price_cost,sto.PACK_SIZE,dt.BATCH_NO,dt.DATE_EXPIRE,dt.DATE_FAC,dt.quan_min,dt.quan_outstore from PD_ST_DETAIL dt" +
                            " inner join pd_st st on dt.PK_PDST=st.PK_PDST" +
                            " inner join bd_pd pd on dt.PK_PD=pd.PK_PD " +
                            " inner join bd_pd_store sto on pd.pk_pd=sto.pk_pd and st.pk_store_st=sto.pk_store where dt.PK_PDSTDT=?"
                    , PdStDtVo.class, new Object[]{batchVo.getPkPdstdt()});
            if (dtInVo == null) {
                throw new BusException("第" + n + "行，没有找到选择的批次入库明细！");
            }
            if (batchVo.getQuanMin().doubleValue() > MathUtils.sub(dt.getQuanMin(), dt.getQuanOutstore()).doubleValue()) {
                throw new BusException("第" + n + "行，选择的批次【第" + (i + 1) + "行】批次库存不足！");
            }
            //出库单据的单位和包装量,,这样不管入库的什么单位，统一转为出库单位
            Integer packSize = dt.getPackSize();
            String pkUnit = dt.getPkUnitPack();
            PdStDtVo dtvo = new PdStDtVo();
            dtvo.setSortNo(i);
            dtvo.setPkPdPlandt(dt.getPkPdPlandt());
            dtvo.setPkPd(dtInVo.getPkPd());
            dtvo.setPdcode(dtInVo.getPdcode());
            dtvo.setPdname(dtInVo.getPdname());
            dtvo.setSpec(dtInVo.getSpec());
            dtvo.setSpcode(dtInVo.getSpcode());
            dtvo.setFactory(dtInVo.getFactory());
            dtvo.setUnit(dtInVo.getUnitNameStore());
            dtvo.setUnitPd(dtInVo.getUnitPd());
            dtvo.setPkUnitPack(pkUnit);
            dtvo.setPackSize(packSize);
            dtvo.setQuanMin(batchVo.getQuanMin());
            dtvo.setQuanPack(MathUtils.div(batchVo.getQuanMin(), packSize.doubleValue()));
            dtvo.setBatchNo(dtInVo.getBatchNo());
            dtvo.setDateExpire(dtInVo.getDateExpire());
            dtvo.setDateFac(dtInVo.getDateFac());
            dtvo.setPackSizePd(dtInVo.getPackSizePd());
            dtvo.setAmount(MathUtils.mul(MathUtils.div(dtInVo.getPrice(), (double) dtInVo.getPackSizePd()), dtvo.getQuanMin()));
            dtvo.setAmountCost(MathUtils.mul(MathUtils.div(dtInVo.getPriceCost(), (double) dtInVo.getPackSizePd()), dtvo.getQuanMin()));
            dtvo.setPkDtin(dtInVo.getPkPdstdt());
            dtvo.setFlagSingle(dtInVo.getFlagSingle());
            //转成当前仓库的，保存的方法会处理为零售价格
            dtvo.setPrice(MathUtils.mul(MathUtils.div(dtInVo.getPrice(), (double) dtInVo.getPackSizePd()), (double) dtInVo.getPackSize()));
            dtvo.setPriceCost(MathUtils.mul(MathUtils.div(dtInVo.getPriceCost(), (double) dtInVo.getPackSizePd()), (double) dtInVo.getPackSize()));
            if (flag) {
                //无来源的，如果选择了具体批次，也将单品重新绑定、然后删了对应明细，重新添加
                dtvo.setPrice(dtInVo.getPrice());
                dtvo.setPriceCost(dtInVo.getPriceCost());
                dtvo.setCreateTime(new Date());
                dtvo.setCreator(user.getPkEmp());
                dtvo.setTs(new Date());
                dtvo.setPkOrg(user.getPkOrg());
                dtvo.setPkPdstdt(NHISUUID.getKeyId());
                dtvo.setPkPdst(dt.getPkPdst());

                PdStDetail pdStDetail = new PdStDetail();
                pdStDetail.setPkPdstdt(dt.getPkPdstdt());
                delList.add(pdStDetail);
                newList.add(dtvo);
            }
            newBatchList.add(dtvo);
        }

        return newBatchList;
    }

    private List<PdSingle> getStDtSingle(int n, PdStDtVo dt) {
        boolean isSingle = PdStDtVo.FLAG_SINGLE_T.equals(dt.getFlagSingle());
        List<PdSingle> pdSingleList = null;
        if (isSingle) {
            pdSingleList = DataBaseHelper.queryForList("select * from pd_single where pk_pdstdt_out=?", PdSingle.class, new Object[]{dt.getPkPdstdt()});
            if (dt.getQuanPack() != pdSingleList.size()) {
                throw new BusException("第" + n + "行，物品数量和选择的单品记录数不匹配！");
            }
        }
        return pdSingleList;
    }


    private int reBindStDtSingle(List<PdSingle> pdSingleList, final PdStDtVo dtVo, int singleIndex) {
        int endIndex = singleIndex + dtVo.getQuanPack().intValue();
        List<PdSingle> subSingleList = pdSingleList.subList(singleIndex, endIndex);
        for (PdSingle pdSingle : subSingleList) {
            pdSingle.setPkPdstdtOut(dtVo.getPkPdstdt());
        }
        return endIndex;
    }

    /**
     * 如果出库业务类型为0202（科室领用），且物品属性为“在用”，同时记录pd_deptusing表（科室在用管理）；
     * 创建科室再用物品信息
     *
     * @param dtList
     * @param dtSttype
     */
    private void savePdDeptUsing(PdStVo stvo, List<PdStDtVo> dtList, String dtSttype) {
        if ("0202".equals(dtSttype)) {
            List<PdDeptusing> usPdList = new ArrayList<PdDeptusing>();
            for (PdStDtVo stdtvo : dtList) {
                Map<String, Object> flagUseMap = DataBaseHelper.queryForMap("select flag_use from bd_pd where pk_pd = ?", new Object[]{stdtvo.getPkPd()});
                if(!"1".equals(MapUtils.getString(flagUseMap,"flagUse"))) continue;
                PdDeptusing usPdvo = new PdDeptusing();
                usPdvo.setPkDeptusing(NHISUUID.getKeyId());
                usPdvo.setPkStore(UserContext.getUser().getPkStore());
                usPdvo.setPkDept(UserContext.getUser().getPkDept());
                usPdvo.setPkOrg(UserContext.getUser().getPkOrg());
                usPdvo.setPkOrgUse(stvo.getPkOrgLk());
                usPdvo.setPkDeptUse(stvo.getPkDeptLk());
                usPdvo.setPkPd(stdtvo.getPkPd());
                usPdvo.setBatchNo(stdtvo.getBatchNo());
                usPdvo.setDateExpire(stdtvo.getDateExpire());
                usPdvo.setPriceCost(stdtvo.getPriceCost());
                usPdvo.setPrice(stdtvo.getPrice());
                usPdvo.setQuan(stdtvo.getQuanPack());//最小单位数量
                usPdvo.setAmount(stdtvo.getAmount());
                usPdvo.setAmountCost(stdtvo.getAmountCost());
                usPdvo.setDateBeign(new Date());
                usPdvo.setPkPdstdtOut(stdtvo.getPkPdstdt());
                usPdList.add(usPdvo);
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdDeptusing.class), usPdList);
        }

    }

    /**
     * 生成缺药计划
     *
     * @param param{List<PdPlanDtVo>}
     * @param user
     */
    @SuppressWarnings("unchecked")
    public void genDrugPlan(String param, IUser user) {
        List<PdPlanDtVo> dtlist = JsonUtil.readValue(param, ArrayList.class);
        if (dtlist == null || dtlist.size() <= 0)
            throw new BusException("未获取到缺药物品明细!");
        User u = (User) user;
        //生成计划
        PdPlan plan = this.createPlan(u);
        DataBaseHelper.insertBean(plan);
        String pk_plan = plan.getPkPdplan();
        //生成计划明细
        List<PdPlanDetail> insert_list = new ArrayList<PdPlanDetail>();
        int i = 1;
        for (PdPlanDtVo dt : dtlist) {
            ApplicationUtils.setBeanComProperty(dt, true);
            dt.setPkPdplandt(NHISUUID.getKeyId());
            dt.setPkPdplan(pk_plan);
            dt.setFlagFinish("0");
            dt.setQuanDe(0.00);
            dt.setDateNeed(new Date());
            dt.setSortNo(i);
            i++;
            insert_list.add(dt);
        }
        if (insert_list != null && insert_list.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdPlanDetail.class), insert_list);
        }
    }

    /**
     * 构建采购计划单
     *
     * @param u
     * @return
     */
    private PdPlan createPlan(User u) {
        PdPlan plvo = new PdPlan();
        plvo.setCodePlan(ApplicationUtils.getCode("采购计划单"));//未注册
        plvo.setDatePlan(new Date());
        plvo.setDtPlantype(IDictCodeConst.DT_PLANTYPE_BUY);
        plvo.setEuStatus("0");
        plvo.setFlagAcc("0");
        plvo.setNameEmpMak(u.getNameEmp());
        plvo.setNamePlan(DateUtils.getDateStr(new Date()) + "缺药计划");
        plvo.setPkDept(u.getPkDept());
        plvo.setPkEmpMak(u.getPkEmp());
        plvo.setPkOrg(u.getPkOrg());
        plvo.setPkStore(u.getPkStore());
        return plvo;
    }

    private List<PdStDtVo> genStDt(MtlGenStParamVo genStParamVo) {
        String eu_outtype = stOutHandler.queryEuOutType(genStParamVo.getPkStore());
        List<PdStDtVo> dtlist = new ArrayList<PdStDtVo>();
        int i = 1;
        List<PdOutDtParamVo> list = null;
        if (genStParamVo.getMtlPdBatchVo() != null) {
            list = stOutHandler.confirmPdSt(genStParamVo.getPkStore(), eu_outtype, genStParamVo.getQuanMin(), genStParamVo.getMtlPdBatchVo());
        } else {
            list = stOutHandler.confirmPdSt(genStParamVo.getPkPd(), null, genStParamVo.getPkStore(), genStParamVo.getQuanMin(), eu_outtype);
        }
        if (list != null && list.size() > 0) {
            for (PdOutDtParamVo vo : list) {
                PdStDtVo dtvo = new PdStDtVo();
                dtvo.setSortNo(i);
                dtvo.setPkDtin(vo.getPkPdstdt());
                dtvo.setPkPdPlandt(genStParamVo.getPkPdplandt());
                i++;
                dtvo.setPkPd(genStParamVo.getPkPd());
                dtvo.setPdcode(genStParamVo.getPdCode());
                dtvo.setPdname(genStParamVo.getPdName());
                dtvo.setSpec(genStParamVo.getSpec());
                dtvo.setSpcode(genStParamVo.getSpcode());
                dtvo.setFactory(genStParamVo.getFactory());
                dtvo.setUnit(vo.getUnitName());
                dtvo.setUnitPd(genStParamVo.getUnitPd());
                dtvo.setPkUnitPack(vo.getPkUnitPack());
                dtvo.setPackSize(vo.getPackSize());
                dtvo.setPackSizePd(vo.getPackSizePd());
                dtvo.setPrice(vo.getPrice());
                dtvo.setPriceCost(vo.getPriceCost());
                dtvo.setQuanMin(vo.getQuanOutMin());
                dtvo.setQuanPack(vo.getQuanOutPack());
                dtvo.setBatchNo(vo.getBatchNo());
                dtvo.setDateExpire(vo.getDateExpire());
                dtvo.setDateFac(vo.getDateFac());
                double pack_size = CommonUtils.getDouble(stOutHandler.getPackSize(genStParamVo.getPkPd()));
                dtvo.setPackSizePd((int) pack_size);
                dtvo.setAmount(MathUtils.mul(MathUtils.div(dtvo.getPrice(), pack_size), dtvo.getQuanMin()));
                dtvo.setAmountCost(MathUtils.mul(MathUtils.div(dtvo.getPriceCost(), pack_size), dtvo.getQuanMin()));
                //求对应库存量的库量
                Map<String, Object> quanMap = mtlPdStPubService.verfiyExistPD(dtvo, genStParamVo.getPkStore(), "0");
                dtvo.setQuanStk(MathUtils.div(MathUtils.sub(MapUtils.getDoubleValue(quanMap, "quanMin"), MapUtils.getDoubleValue(quanMap, "quanPrep")),
                        CommonUtils.getDouble(vo.getPackSize())));
                dtvo.setFlagSingle(vo.getFlagSingle());
                dtvo.setFlagUse(genStParamVo.getFlagUse());
                dtlist.add(dtvo);
            }
        }
        return dtlist;
    }


    /**
     * 更新调拨明细
     *
     * @param stlist
     */
    private void updatePlanDt(List<PdStDtVo> stlist, User u) {
        if (stlist == null || stlist.size() < 0) return;
        String pk_plandt = "";
        List<String> update_list = new ArrayList<String>();
        double total = 0;
        Set<String> planSet = new HashSet<String>();
        for (PdStDtVo dtvo : stlist) {
            if (pk_plandt != null && pk_plandt.equals(dtvo.getPkPdPlandt())) {
                total = total + dtvo.getQuanMin();
                continue;
            } else {
                pk_plandt = dtvo.getPkPdPlandt();
                total = dtvo.getQuanMin();
            }
            String update_sql = " update pd_plan_detail set quan_de = " + total
                    + ",pk_emp_de = '" + u.getPkEmp() + "',flag_finish = '1'"
                    + ",name_emp_de = '" + u.getNameEmp() + "'"
                    + ",date_de = to_date('" + DateUtils.getDefaultDateFormat().format(new Date()) + "','YYYYMMDDHH24MISS') "
                    + ",ts = to_date('" + DateUtils.getDefaultDateFormat().format(new Date()) + "','YYYYMMDDHH24MISS') "
                    + " where pk_pdplandt = '" + pk_plandt + "'";
            update_list.add(update_sql);
            PdPlanDetail dt = DataBaseHelper.queryForBean("select pk_pdplan from pd_plan_detail where pk_pdplandt = ?", PdPlanDetail.class, new Object[]{pk_plandt});
            planSet.add("update pd_plan set eu_status='2',flag_acc='1'  where pk_pdplan ='" + dt.getPkPdplan() + "'");
        }
        if (update_list != null && update_list.size() > 0) {
            DataBaseHelper.batchUpdate(update_list.toArray(new String[0]));
        }
        if (planSet != null && planSet.size() > 0) {
            DataBaseHelper.batchUpdate((new ArrayList<String>(planSet)).toArray(new String[0]));
        }
    }

    /**
     * 查询单品明细
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<PdSingle> qrySinglePdOut(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) {
            return null;
        }
        List<PdSingle> singleList = DataBaseHelper.queryForList("select sl.pk_single,sl.pk_pd, sl.barcode, sl.eu_status, sl.note from pd_single sl "
                + "where sl.pk_pdstdt_in = ?", PdSingle.class, new Object[]{paramMap.get("PkPdstdt")});
        return singleList;
    }
}
