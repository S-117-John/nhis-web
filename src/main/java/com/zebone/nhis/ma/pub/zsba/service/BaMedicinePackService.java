package com.zebone.nhis.ma.pub.zsba.service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.zsba.dao.BaMedicinePackMapper;
import com.zebone.nhis.ma.pub.zsba.vo.*;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 包药机业务接口
 *
 * @author yangxue
 */
@Service
public class BaMedicinePackService {

    private Logger logger = LoggerFactory.getLogger("nhis.BaWebServiceLog");

    @Resource
    private BdSnService bdSnService;
    @Resource
    private IpCgPubService cgService;
    @Resource
    private BaMedicinePackMapper medicinePackMapper;

    /**
     * @return com.zebone.nhis.ma.pub.zsba.vo.PackPdVoList
     * @Description 查询摆药机摆药数据和人共摆药长期，临时数据
     * @auther wuqiang
     * @Date 2020-06-01
     * @Param [pdDeDrugVos：查询参数, pcakParam 科室包药机参数]
     */
    public PackPdVoList drugBreakRecord(List<PdDeDrugVo> pdDeDrugVos, String pcakParam) {
        PackPdVoList packPdVoList = null;
        if (pdDeDrugVos.isEmpty()) {
            return packPdVoList;
        }
        //查询需要处理的药品明细
        List<PackPdMedVo> packPdVos = medicinePackMapper.qryPackPdVoList(pdDeDrugVos);
        if (packPdVos.isEmpty()) {
            return packPdVoList;
        }


        /* 设计文档规则如下
        1)所有口服药（含长期、临时）均需要打印药袋。
        2)长期口服药袋由包药机和手工药袋共同构成，其中的手工药袋需要按药品+执行时间进行分组合并，即多个药品的计划执行时间一致的话，则打印一个药袋。
        3)临时口服药需要包括发药日期18:00以前的长期口服药（因为长期口服药单处理的是发药当日18:00至次日18:00之间的用药，因此在发药当日18:00前的长期口服药无法及时处理，因此归入到临时口服药单中）
        4)临时口服药按药品种类打印药袋，即一个药品一个药袋，而不是按计划执行时间合并。并且药袋要打印出药品使用的总数和每次执行数量。
        5)毒麻药单、精神类用药单中如果有口服药，也需要打印手工药袋，规则与临时口服药一致
        6)出院带药处方中如果有口服药，则需要打印手工药袋，规则同临时口服药一致。
        *  基于上面使用如下参数区分 1.当EX0027=2时所有药均走人工摆药，
        * 其中 codeDecate=0001/长期口服药，0002/临时口服药  0005/毒麻药品 0004/二类精神药品， 0003/统领药 空and pkPres 不为空处方药
        *   统领药不走包药机，
        *  当EX0027=1时，当 euAlways=val=1时走摆药机摆药 val=0时走人共摆药
        *1)所有口服药（含长期、临时）均需要打印药袋。
        *
        * */
        String strPd = ScmPubUtils.getPdDecate(UserContext.getUser().getPkDept());
        String[] pdDecates = strPd.split(",");
        // 博爱这个参数只维护1个，并且必须是长期医嘱发放分类
        if (pdDecates == null || pdDecates.length <= 0 || pdDecates.length > 1) {
            logger.info("发放分类维护错误，不走包药机");
            return packPdVoList;
        }
        //摆药机数据
        List<PackPdMedVo> packPdMedVoList = new ArrayList<>(16);
        //人工长期
        List<PackPdMedVo> altogetherPachPdVolistLong = new ArrayList<>(16);
        //人工临时
        List<PackPdMedVo> altogetherPachPdVolistTemporary = new ArrayList<>(16);
        if ("2".equals(pcakParam)) {
            for (PackPdMedVo pdMedVo : packPdVos) {
                if (strPd.equals(pdMedVo.getCodeDecate())) {
                    altogetherPachPdVolistLong.add(pdMedVo);
                } else {
                    altogetherPachPdVolistTemporary.add(pdMedVo);
                }
            }
        }
        if ("1".equals(pcakParam)) {
            for (PackPdMedVo pdMedVo : packPdVos) {
                if (strPd.equals(pdMedVo.getCodeDecate())) {
                    if ("1".equals(pdMedVo.getVal())&&isInDate(new Date(),"08:00:00","18:00:00")) {
                        packPdMedVoList.add(pdMedVo);
                    } else {
                        altogetherPachPdVolistLong.add(pdMedVo);
                    }
                } else {
                    altogetherPachPdVolistTemporary.add(pdMedVo);
                }
            }
        }
        packPdVoList=new PackPdVoList();
        packPdVoList.setPackPdVoList(packPdMedVoList);
        packPdVoList.setAltogetherPachPdVolistLong(altogetherPachPdVolistLong);
        packPdVoList.setAltogetherPachPdVolistTemporary(altogetherPachPdVolistTemporary);
        return packPdVoList;
    }

    /*
     * @Description 根据发药明细计算药袋
     * 根据就诊主键、医嘱编号、执行时间进行分组，相同则属于同一药袋
     * @auther wuqiang
     * @Date 2020-04-08
     * @Param [pdVoList] euBagTyp:药袋类型
     * @return java.util.List<com.zebone.nhis.ma.pub.zsba.vo.ExMedBag>
     */
    public ExMedBegAndDEetialPatams calculateNumOfMedBags(List<PackPdMedVo> pdVoList, String euBagType) {
        List<ExMedBag> medBagList = null;
        List<ExMedBagDetail> exMedBagDetails = null;
        if (pdVoList == null || pdVoList.size() == 0) {
            return null;
        }
        medBagList = new ArrayList<ExMedBag>(16);
        exMedBagDetails = new ArrayList<>(16);
        int num=1;
        for (int i = 0; i < pdVoList.size(); i++) {
            if (pdVoList.get(i).getStatue() == 1) {
                continue;
            }

            pdVoList.get(i).setStatue(1);
            ExMedBag exMedBag = new ExMedBag();
            exMedBag.setCodeDe(pdVoList.get(i).getCodeDe());
            exMedBag.setPkPv(pdVoList.get(i).getPkPv());
            exMedBag.setEuBag(euBagType);
            exMedBag.setPkPi(pdVoList.get(i).getPkPi());
            Integer id = bdSnService.getSerialNo("EX_MED_BAG", "CODE_BAG", 1, UserContext.getUser());
            exMedBag.setCodeBag(String.valueOf(id));
            ApplicationUtils.setDefaultValue(exMedBag, true);
            ExMedBagDetail exMedBagDetail = new ExMedBagDetail();
            exMedBagDetail.setPkMebd(NHISUUID.getKeyId());
            exMedBagDetail.setPkMedbag(exMedBag.getPkMedbag());
            exMedBagDetail.setPkExocc(pdVoList.get(i).getPkExocc());
            pdVoList.get(i).setCodeBag(exMedBag.getCodeBag());
            for (int j = 0; j < pdVoList.size(); j++) {
                if(num==5){
                    num=1;
                    break;
                }
                if (pdVoList.get(i).getPkPv().equals(pdVoList.get(j).getPkPv()) &&
                        pdVoList.get(i).getDatePlan().equals(pdVoList.get(j).getDatePlan()) &&
                        pdVoList.get(j).getStatue() == 0
                ) {
                    num++;
                    pdVoList.get(j).setStatue(1);
                    pdVoList.get(j).setCodeBag(exMedBag.getCodeBag());
                    ExMedBagDetail exMedBagDetail1 = new ExMedBagDetail();
                    exMedBagDetail1.setPkMebd(NHISUUID.getKeyId());
                    exMedBagDetail1.setPkMedbag(exMedBag.getPkMedbag());
                    exMedBagDetail1.setPkExocc(pdVoList.get(j).getPkExocc());
                    exMedBagDetails.add(exMedBagDetail1);
                }
            }
            medBagList.add(exMedBag);
            exMedBagDetails.add(exMedBagDetail);
        }
        return new ExMedBegAndDEetialPatams(medBagList, exMedBagDetails);
    }

    /*
     * @Description 根据发药明细计算药袋
     * 根据就诊主键药品 进行分组，每一个药一个药袋,
     * @auther wuqiang
     * @Date 2020-04-08
     * @return java.util.List<com.zebone.nhis.ma.pub.zsba.vo.ExMedBag>
     */
    public ExMedBegAndDEetialPatams calculateNumOfMedBags(List<PackPdMedVo> pdVoList ) {
        List<ExMedBag> medBagList = null;
        List<ExMedBagDetail> exMedBagDetails = null;
        if (pdVoList == null || pdVoList.size() == 0) {
            return null;
        }
        medBagList = new ArrayList<ExMedBag>(16);
        exMedBagDetails = new ArrayList<>(16);
        for (int i = 0; i < pdVoList.size(); i++) {
            if (pdVoList.get(i).getStatue() == 1) {
                continue;
            }

            pdVoList.get(i).setStatue(1);
            ExMedBag exMedBag = new ExMedBag();
            exMedBag.setCodeDe(pdVoList.get(i).getCodeDe());
            exMedBag.setPkPv(pdVoList.get(i).getPkPv());
            exMedBag.setEuBag(BagEuStatus.ARTI.getStatus());
            exMedBag.setPkPi(pdVoList.get(i).getPkPi());
            Integer id = bdSnService.getSerialNo("EX_MED_BAG", "CODE_BAG", 1, UserContext.getUser());
            exMedBag.setCodeBag(String.valueOf(id));
            ApplicationUtils.setDefaultValue(exMedBag, true);
            ExMedBagDetail exMedBagDetail = new ExMedBagDetail();
            exMedBagDetail.setPkMebd(NHISUUID.getKeyId());
            exMedBagDetail.setPkMedbag(exMedBag.getPkMedbag());
            exMedBagDetail.setPkExocc(pdVoList.get(i).getPkExocc());
            pdVoList.get(i).setCodeBag(exMedBag.getCodeBag());
            if (!"03".equals(pdVoList.get(i).getDtPois())){
                for (int j = 0; j < pdVoList.size(); j++) {
                    if (pdVoList.get(i).getPkPv().equals(pdVoList.get(j).getPkPv()) &&
                            pdVoList.get(i).getpKPD().equals(pdVoList.get(j).getpKPD()) &&
                            pdVoList.get(j).getStatue() == 0 &&  pdVoList.get(i).getPkCnord().equals(pdVoList.get(j).getPkCnord())
                    ) {
                        pdVoList.get(j).setStatue(1);
                        pdVoList.get(j).setCodeBag(exMedBag.getCodeBag());
                        ExMedBagDetail exMedBagDetail1 = new ExMedBagDetail();
                        exMedBagDetail1.setPkMebd(NHISUUID.getKeyId());
                        exMedBagDetail1.setPkMedbag(exMedBag.getPkMedbag());
                        exMedBagDetail1.setPkExocc(pdVoList.get(j).getPkExocc());
                        exMedBagDetails.add(exMedBagDetail1);
                    }
                }
            }
            medBagList.add(exMedBag);
            exMedBagDetails.add(exMedBagDetail);
        }
        return new ExMedBegAndDEetialPatams(medBagList, exMedBagDetails);
    }



    /***
     * @Description 根据药袋进行药袋费用明细组装
     * @auther wuqiang
     * @Date 2020-04-08
     * @Param [exMedBagList]
     * @return com.zebone.nhis.bl.pub.vo.BlPubReturnVo
     */
    public List<BlPubParamVo> medBagsCharge(List<ExMedBag> exMedBagList, String euBagType) {
        if (exMedBagList == null || exMedBagList.size() == 0) {
            return null;
        }
        //药袋费用编码
        String itemCode = null;
        if (BagEuStatus.MACH.getStatus().equals(euBagType)) {
            itemCode = ApplicationUtils.getSysparam("BL0019", false);
            if (StringUtils.isBlank(itemCode)) {
                throw new BusException("BL0019 药袋费用未维护，请维护药袋费!!!");
            }
        }
        if (BagEuStatus.ARTI.getStatus().equals(euBagType)) {
            itemCode = ApplicationUtils.getSysparam("BL0025", false);
        }
        if (StringUtils.isBlank(itemCode)) {
            return null;
        }
        BdItem item = DataBaseHelper.queryForBean("select pk_item,pk_unit from bd_item where code = ? and flag_active='1' ", BdItem.class, itemCode);
        if (item == null) {
            throw new BusException("费用编码:" + itemCode + "未找到，请查看其有校性");
        }
        //记费数量
        Double quanCg = 1.00;
        //价格总量
        List<BlPubParamVo> blPubParamVos = new ArrayList<>(16);
        for (int i = 0; i < exMedBagList.size(); i++) {
            if (exMedBagList.get(i).getStatue() == 1) {
                continue;
            }
            BlPubParamVo blPubParamVo = consCgParam(exMedBagList.get(i), item);
            exMedBagList.get(i).setStatue(1);
            exMedBagList.get(i).setPkCgip(blPubParamVo.getPkPres());
            for (int j = 0; j < exMedBagList.size(); j++) {
                if (exMedBagList.get(i).getPkPv().equals(exMedBagList.get(j).getPkPv()) && exMedBagList.get(j).getStatue() == 0) {
                    quanCg++;
                    exMedBagList.get(j).setStatue(1);
                    exMedBagList.get(j).setPkCgip(blPubParamVo.getPkPres());
                }
            }
            blPubParamVo.setQuanCg(quanCg);
            blPubParamVos.add(blPubParamVo);
            quanCg = 1.00;
        }
        return blPubParamVos;
    }

    /***
     * @Description 添加记费必要数据信息
     * @auther wuqiang
     * @Date 2020-04-09
     * @Param [vo, exMedBag]
     * @return void
     */
    private BlPubParamVo consCgParam(ExMedBag exMedBag, BdItem item) {
        User u = UserContext.getUser();
        BlPubParamVo vo = new BlPubParamVo();
        vo.setEuPvType("3");
        vo.setPkPv(exMedBag.getPkPv());
        vo.setFlagPv("0");
        vo.setFlagPd("0");
        vo.setNameEmpApp(u.getNameEmp());
        vo.setPkOrg(u.getPkOrg());
        vo.setPkEmpApp(u.getPkEmp());
        vo.setPkOrgApp(u.getPkOrg());
        //开立病区
        vo.setPkDeptNsApp(u.getPkDept());
        vo.setDateHap(new Date());
        //开立科室
        vo.setPkDeptApp(u.getPkDept());
        vo.setPkDeptEx(u.getPkDept());
        vo.setPkOrgEx(u.getPkOrg());
        vo.setPkPi(exMedBag.getPkPi());
        vo.setInfantNo("0");
        vo.setPkDeptCg(u.getPkDept());
        vo.setPkEmpCg(u.getPkEmp());
        vo.setPkItem(item.getPkItem());
        vo.setPkUnitPd(item.getPkUnit());
        //这个只用作与药袋临时关联，无实际意义，记费时候删除此内容
        vo.setPkPres(NHISUUID.getKeyId());
        return vo;
    }


    /***
     * @Description 保存药袋数据，进行药袋记费
     * @auther wuqiang
     * @Date 2020-04-09
     * @Param [blPubParamVoList, exMedBagList]
     * @return void
     * @Transactional(propagation=Propagation.REQUIRES_NEW)
     */

    public void saveMedBag(List<BlPubParamVo> blPubParamVoList, List<ExMedBag> exMedBagList) {
        if (blPubParamVoList != null) {
            for (int i = 0; i < blPubParamVoList.size(); i++) {
                String pkItem = blPubParamVoList.get(i).getPkPres();
                blPubParamVoList.get(i).setPkPres("");
                //记费
                List<BlPubParamVo> blPubParamVos = new ArrayList<>(16);
                blPubParamVos.add(blPubParamVoList.get(i));
                BlPubReturnVo vo = null;//调用计费接口
                try {
                    vo = cgService.chargeIpBatch(blPubParamVos, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info(e.getMessage());
                    throw new BusException("药袋费记费失败:请补收药袋费");
                } finally {
                    if (vo != null && vo.getBids() != null && vo.getBids().size() > 0) {
                        for (ExMedBag exMedBag : exMedBagList) {
                            if (pkItem.equals(exMedBag.getPkCgip())) {
                                exMedBag.setPkCgip(vo.getBids().get(0).getPkCgip());
                            }
                        }
                    } else {
                        for (ExMedBag exMedBag : exMedBagList) {
                            exMedBag.setPkCgip(null);
                        }
                    }
                }
            }
            saveMedicineBag(exMedBagList);
        } else {
            for (ExMedBag exMedBag : exMedBagList) {
                exMedBag.setPkCgip(null);
            }
            saveMedicineBag(exMedBagList);
        }
    }

    /**
     * 保存药袋数据
     *
     * @param list
     */
    public void saveMedicineBag(List<ExMedBag> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExMedBag.class), list);
    }

    /**
     * 保存药袋明细
     *
     * @param list
     */
    public void saveMedicineBagDetail(List<ExMedBagDetail> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExMedBagDetail.class), list);
    }

    /**
     * 包药服务（不打印执行单）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void packPd(List<PackPdVo> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PackPdVo.class), list);
    }

    /**
     * 完成发药，更新发药表数据
     *
     * @param packPdVos
     */
    public void updateFlagSend(List<PackPdMedVo> packPdVos, String euBagType) {
        if (packPdVos == null || packPdVos.size() <= 0) {
            return;
        }
        //去重
        List<String> list = new ArrayList<>(16);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (PackPdMedVo packPdMedVo : packPdVos) {
            if (packPdMedVo == null) {
                continue;
            }
            String pkPdapdt = packPdMedVo.getPkPdapdt();
            if (pkPdapdt != null) {
                String value = hashMap.get(pkPdapdt);
                if (StringUtils.isBlank(value)) { //如果value是空的  说明取到的这个userName是第一次取到
                    hashMap.put(pkPdapdt, pkPdapdt);
                    String sql = "update EX_PD_DE  set FLAG_SENDTOFA = '" + euBagType + "', CODE_BAG='" + packPdMedVo.getCodeBag() + "' where PK_PDAPDT ='" + pkPdapdt+ "'";
                    list.add(sql);
                } else {
                    continue;
                }
            }
        }
        if (list.size()>0){
            DataBaseHelper.batchUpdate(list.toArray(new String[0]));
        }

    }

    /***
     * @Description 查询重发包药机所需数据
     * @auther wuqiang
     * @Date 2020-04-25
     * @Param [map]
     * @return java.util.List<com.zebone.nhis.scm.pub.vo.PdDeDrugVo>
     */
    public List<PdDeDrugVo> queryPdDeDrugVo(Map<String, Object> map) {
        return medicinePackMapper.queryPdDeDrugVo(map);
    }

    /***
     * @Description 查询药袋
     * @auther wuqiang
     * @Date 2020-04-25
     * @Param [param, user]
     * @return java.util.List<com.zebone.nhis.ma.pub.zsba.vo.OrderExVo>
     */
    public List<OrderExVo> queryExMedBag(Object[] args) {
        Map<String, Object> paramMap = (Map<String, Object>) args[0];
        if (paramMap == null) {
            throw new BusException("未获取到查询参数！");
        }
        return medicinePackMapper.queryExMedBag(paramMap);
    }

    /***
     * @Description 查询药袋明细
     * @auther wuqiang
     * @Date 2020-04-26
     * @Param [param, user]
     * @return void
     */
    public List<OrderExVo> queryExMedBagDetial(String param, IUser user) {
        String codeBag = JsonUtil.getFieldValue(param, "codeBag");
        return medicinePackMapper.queryExMedBagDetial(codeBag);
    }


    /***
     * @Description 补收药袋费
     * @auther wuqiang
     * @Date 2020-04-25
     * @Param [param, user]
     * @return void
     */
    public void ExMedBagIpCg(String param, IUser user) {
        List<OrderExVo> exlist = JsonUtil.readValue(param, new TypeReference<List<OrderExVo>>() {
        });
        List<ExMedBag> exMedBagsMa = new ArrayList<>(16);
        List<ExMedBag> exMedBagsAr = new ArrayList<>(16);
        for (OrderExVo exVo : exlist) {
            ExMedBag ex = new ExMedBag();
            ex.setStatue(0);
            ex.setPkMedbag(exVo.getPkMedbag());
            ex.setPkPv(exVo.getPkPv());
            if (BagEuStatus.MACH.getStatus().equals(exVo.getEuBag())) {
                exMedBagsMa.add(ex);
            }
            if (BagEuStatus.ARTI.getStatus().equals(exVo.getEuBag())) {
                exMedBagsAr.add(ex);
            }
        }
        //摆药机药袋计费
        if (exMedBagsMa.size() > 0) {
            List<BlPubParamVo> blPubParamVoList = medBagsCharge(exMedBagsMa, BagEuStatus.MACH.getStatus());
            updateMedBag(blPubParamVoList, exMedBagsMa);
        }
        if (exMedBagsAr.size() > 0) {
            List<BlPubParamVo> blPubParamVoList = medBagsCharge(exMedBagsAr, BagEuStatus.MACH.getStatus());
            updateMedBag(blPubParamVoList, exMedBagsAr);
        }
    }


    /***
     * @Description 记药袋费用，并更新药袋表
     * @auther wuqiang
     * @Date 2020-04-26
     * @Param [blPubParamVoList, exMedBagList]
     * @return void
     */
    public void updateMedBag(List<BlPubParamVo> blPubParamVoList, List<ExMedBag> exMedBagList) {
        if (blPubParamVoList != null) {
            for (int i = 0; i < blPubParamVoList.size(); i++) {
                String pkItem = blPubParamVoList.get(i).getPkPres();
                blPubParamVoList.get(i).setPkPres("");
                //记费
                List<BlPubParamVo> blPubParamVos = new ArrayList<>(16);
                blPubParamVos.add(blPubParamVoList.get(i));
                BlPubReturnVo vo = null;//调用计费接口
                try {
                    vo = cgService.chargeIpBatch(blPubParamVos, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info(e.getMessage());
                    throw new BusException("药袋费记费失败:请补收药袋费");
                } finally {
                    if (vo != null && vo.getBids() != null && vo.getBids().size() > 0) {
                        for (ExMedBag exMedBag : exMedBagList) {
                            if (pkItem.equals(exMedBag.getPkCgip())) {
                                exMedBag.setPkCgip(vo.getBids().get(0).getPkCgip());
                            }
                        }
                    } else {
                        for (ExMedBag exMedBag : exMedBagList) {
                            exMedBag.setPkCgip(null);
                        }
                    }
                }
            }
            updateMedicineBag(exMedBagList);
        } else {
            for (ExMedBag exMedBag : exMedBagList) {
                exMedBag.setPkCgip(null);
            }
            updateMedicineBag(exMedBagList);
        }
    }


    /**
     * 更新药袋数据
     *
     * @param list
     */
    public void updateMedicineBag(List<ExMedBag> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        DataBaseHelper.batchUpdate("update  EX_MED_BAG set PK_CGIP=:pkCgip where PK_MEDBAG=:pkMedbag ", list);
    }

    /***
     * @Description 查询包药机未发送药品通用实现类
     * @auther wuqiang
     * @Date 2020-06-01
     * @Param [args]
     * @return java.lang.Object
     */
    public List<Map<String, Object>> querySendDrugAgainData(Object[] args) {
        Map<String, Object> qryParam = (Map<String, Object>) args[0];
        return  medicinePackMapper.querySendDrugAgainData(qryParam);
    }

  /***
   * @Description  判断时间是否处于某一时间段
   * @auther wuqiang
   * @Date 2020-06-06
   * @Param [date：需要判断的时间, strDateBegin HH:mm:ss 起始时间, strDateEnd结束时间 HH:mm:ss]
   * @return boolean
   */
    public static boolean isInDate(Date date, String strDateBegin,
                                   String strDateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
        // 截取当前时间时分秒
        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        int strDateM = Integer.parseInt(strDate.substring(14, 16));
        int strDateS = Integer.parseInt(strDate.substring(17, 19));
        // 截取开始时间时分秒
        int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
        int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
        int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
        // 截取结束时间时分秒
        int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
        int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
        int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
        if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
            // 当前时间小时数在开始时间和结束时间小时数之间
            if (strDateH > strDateBeginH && strDateH < strDateEndH) {
                return true;
                // 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM >= strDateBeginM
                    && strDateM <= strDateEndM) {
                return true;
                // 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM == strDateBeginM
                    && strDateS >= strDateBeginS && strDateS <= strDateEndS) {
                return true;
            }
            // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
            else if (strDateH >= strDateBeginH && strDateH == strDateEndH
                    && strDateM <= strDateEndM) {
                return true;
                // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数
            } else if (strDateH >= strDateBeginH && strDateH == strDateEndH
                    && strDateM == strDateEndM && strDateS <= strDateEndS) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
