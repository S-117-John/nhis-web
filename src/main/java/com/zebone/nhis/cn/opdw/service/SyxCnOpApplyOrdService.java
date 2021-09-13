package com.zebone.nhis.cn.opdw.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.service.ExAssistPubService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.opdw.dao.SyxCnOpApplyOrdMapper;
import com.zebone.nhis.cn.opdw.vo.OrdBdPdAttVo;
import com.zebone.nhis.cn.opdw.vo.SyxBlOpDt;
import com.zebone.nhis.cn.opdw.vo.SyxCnOrderVo;
import com.zebone.nhis.cn.opdw.vo.SyxOpReqOrderVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.bl.CnOrderBar;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.pi.PatInfo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.pskq.model.ExamApply;
import com.zebone.nhis.ma.pub.platform.pskq.model.LabApply;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderOutpat;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendCnMapper;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SyxCnOpApplyOrdService {
    private Logger logger = LoggerFactory.getLogger("nhis.cnOrder");
    @Autowired
    public SyxCnOpApplyOrdMapper reqDao;
    @Autowired
    private OpCgPubService opCgPubService;
    @Autowired
    private BdSnService bdSnService;
    @Autowired
    private CnPubService cnPubService;
    @Autowired
    public SyxCnOpPatiPvService syxCnOpPatiPvService;
    @Autowired
    private ExAssistPubService settlePdOutService; // 处理医技执行单

    //查询医生常用检查检验治疗项目
    public List<Map<String, Object>> qryPreferOrders(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> commonOrdlist = reqDao.qryPreferOrders(paramMap);
        return commonOrdlist;
    }

    //查询检查全院项目树
    public List<Map<String, Object>> queryRisLabTreeList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = reqDao.queryRisLabTreeList(paramMap);
        return list;
    }

    //查询检验全院项目树
    public List<Map<String, Object>> queryLisLabTreeList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = reqDao.queryLisLabTreeList(paramMap);
        return list;
    }

    public List<Map<String, Object>> qryReqOrders(String param, IUser user) {
        //pkPv
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = paramMap.get("pkPv") != null ? paramMap.get("pkPv").toString() : "";
        if (StringUtils.isBlank(pkPv)) throw new BusException("传参pkPv为空！");
        String isRis = paramMap.get("isRis") != null ? paramMap.get("isRis").toString() : "";
        if (StringUtils.isBlank(pkPv)) throw new BusException("传参isRis为空！");
        Boolean flagEx = "1".equals(MapUtils.getString(paramMap, "flagEx"));
        List<Map<String, Object>> reqOrderlist = null;
        if (flagEx) {
            reqOrderlist = reqDao.qryReqOrdersEx(paramMap);
        } else {
            reqOrderlist = reqDao.qryReqOrders(paramMap);
        }
        if (reqOrderlist != null && reqOrderlist.size() > 0) {
            setOrdSupplyBl(pkPv, reqOrderlist);
            setOrdBar(reqOrderlist);

        }
        cnPubService.setHpRateFormat(paramMap,reqOrderlist);
        return reqOrderlist;
    }

    private void setOrdSupplyBl(String pkPv, List<Map<String, Object>> reqOrderlist) {
        List<SyxBlOpDt> dt = DataBaseHelper.queryForList("select dt.eu_additem, dt.pk_pres ,dt.pk_cnord,dt.pk_cgop,dt.pk_item,dt.name_cg as item_name,dt.quan as quan_cg,dt.price,dt.amount from bl_op_dt dt where  dt.pk_pv=? and dt.eu_additem !='0' and dt.pk_cnord is not null  ", SyxBlOpDt.class, new Object[]{pkPv});
        if (dt != null && dt.size() > 0) {
            List<List<SyxBlOpDt>> cnOrdGroup = dividerList(dt, new Comparator<SyxBlOpDt>() {
                @Override
                public int compare(SyxBlOpDt o1, SyxBlOpDt o2) {
                    return o1.getPkCnord().equals(o2.getPkCnord()) ? 0 : -1;
                }
            });
            for (List<SyxBlOpDt> group : cnOrdGroup) {
                for (Map<String, Object> ord : reqOrderlist) {
                    if (ord.get("pkCnord").toString().equals(group.get(0).getPkCnord())) {
                        ord.put("addItems", group);
                        break;
                    }
                }
            }
        }
    }

    private void setOrdBar(List<Map<String, Object>> reqOrderlist) {
        String sql="";
        if(Application.isSqlServer()) {
            sql = "select bar.*,pdorg.PK_ORD as pk_pd,pdorg.price as quanprice,pd.code,pd.SPEC  \n" +
                    "from CN_ORDER_BAR bar\n" +
                    "INNER JOIN BD_ORD pd on pd.PK_ORD=bar.PK_ITEM\n" +
                    "INNER JOIN BD_ORD_ORG pdorg on bar.PK_ITEM=pdorg.PK_ORD " +
                    "where bar.PK_CNORD=? and pdorg.pk_org=?";
        }else{
            sql = "select bar.*,pdorg.PK_ORD as pk_pd,pdorg.price as quanprice,pd.code,pd.SPEC  \n" +
                    "from CN_ORDER_BAR bar\n" +
                    "INNER JOIN BD_ORD pd on pd.PK_ORD=bar.PK_ITEM\n" +
                    "INNER JOIN BD_ORD_ORG pdorg on bar.PK_ITEM=pdorg.PK_ORD " +
                    "where bar.PK_CNORD=? and pdorg.pk_org=?";
        }
        for (Map<String, Object> vo : reqOrderlist){
            String pkCnord=MapUtils.getString(vo,"pkCnord");
            String pkOrg=MapUtils.getString(vo,"pkOrg");
            if(StringUtils.isNotEmpty(pkCnord)){
                List<OrdBdPdAttVo> listbar=DataBaseHelper.queryForList(sql,OrdBdPdAttVo.class,new Object[]{pkCnord,pkOrg});
                vo.put("blPdItems",listbar);
            }
        }

    }

    public static <T> List<List<T>> dividerList(List<T> list, Comparator<? super T> comparator) {
        List<List<T>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            boolean isContain = false;
            for (int j = 0; j < lists.size(); j++) {
                if (lists.get(j).size() == 0 || comparator.compare(lists.get(j).get(0), list.get(i)) == 0) {
                    lists.get(j).add(list.get(i));
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                List<T> newList = new ArrayList<>();
                newList.add(list.get(i));
                lists.add(newList);
            }
        }
        return lists;
    }

    public List<Map<String, Object>> getLisLabCount(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = reqDao.getLisLabCount(paramMap);
        return list;
    }
    
    //================保存检查检验===========
    public List<SyxCnOrderVo> saveRisLabApply(String param, IUser user) {
        Date dateNow = new Date();
        User userInfo = (User) user;
        SyxOpReqOrderVo reqParam = JsonUtil.readValue(param, SyxOpReqOrderVo.class);
        PatInfo patInfo = reqParam.getPatInfo();
        boolean flagEX = "1".equals(reqParam.getFlagEX());
            boolean flagBl="1".equals(reqParam.getFlagBl());
        //判断患者是否已收费，以收费不允许保存
        String euLocked = syxCnOpPatiPvService.getPvEuLocked(patInfo.getPkPv());
        String pv0057 = ApplicationUtils.getSysparam("PV0057", false);
        if(!"0".equals(pv0057)){
            pv0057 = "1";
        }
        if ("2".equals(euLocked) && "1".equals(pv0057)) throw new BusException("该患者正在做收费处理，不允许操作！请等待收费完成！");
        //查询当前医生考勤科室
        BdOuEmpjob emp = DataBaseHelper.queryForBean(
                "SELECT * FROM bd_ou_empjob WHERE pk_emp =? AND is_main = '1' ", BdOuEmpjob.class, new Object[]{userInfo.getPkEmp()});
        String pkDeptJob = emp != null && StringUtils.isNotBlank(emp.getPkDept()) ? emp.getPkDept() : null;
        //查询患者就诊所在诊区
        String pkDeptAreas = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept_area From pv_encounter where pk_pv=?", patInfo.getPkPv()), "pkDeptArea");
        //List<SyxCnOrderVo> modifyRisLabList = reqParam.getModifyList();
        List<SyxCnOrderVo> deleteRisLabList = reqParam.getDeletingList();
        List<CnOrder> addOrdList = new ArrayList<CnOrder>();
        List<CnOrder> updateOrdList = new ArrayList<CnOrder>();
        List<CnRisApply> addRisList = new ArrayList<CnRisApply>();
        List<CnRisApply> updateRisList = new ArrayList<CnRisApply>();
        List<CnLabApply> addLabList = new ArrayList<CnLabApply>();
        List<CnLabApply> updateLabList = new ArrayList<CnLabApply>();
        List<BlPubParamVo> blOpCgList = new ArrayList<BlPubParamVo>();
        List<Map<String, Object>> delMapList = new ArrayList<>();
        //标本容器费用
        Map<String, SyxCnOrderVo> dtSampType = new HashMap<>();
        Map<String, SyxCnOrderVo> dtTubetype = new HashMap<>();
        //采集方法费用
        Map<String, SyxCnOrderVo> dtColtype = new HashMap<>();
        List<SyxCnOrderVo> labCnOrdVos = new ArrayList<SyxCnOrderVo>();
        List<CnOrder> signCnOrderList = new ArrayList<CnOrder>();
        List<CnOrder> qryOrderMsg = new ArrayList<>();

        //附加药品
        List<CnOrderBar> ordbarListAdd=new ArrayList<>();
        List<CnOrderBar> ordbarListUpdate=new ArrayList<>();
        //匹配费用数据
        List<String> pkCnordDelete=new ArrayList<String>();

        //检查费用查询
        List<BlPubParamVo> itemlists=new ArrayList<>();
        if(flagBl){
            Map map=new HashMap();
            itemlists=reqDao.qryDiagItem(map);
        }


        if (reqParam.getModifyList() != null && reqParam.getModifyList().size() > 0) {
            for (SyxCnOrderVo ord : reqParam.getModifyList()) {
                //判断是否有收费操作
                if (ord.getPkCnord() != null) {
                    int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord =? and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{ord.getPkCnord()});
                    if (hadSettleCgCount > 0) {
                        continue;
                    }
                }
                CnRisApply cnRisApply = null;
                CnLabApply cnLabApply = null;
                if (reqParam.getFlagQuan() == null || (reqParam.getFlagQuan() != null && "0".equals(reqParam.getFlagQuan()))) {
                    ord.setQuan(ord.getQuanCg());
                }
                ord.setPkDeptJob(pkDeptJob);//考勤科室

                //判断是否需要计费
                ord.setFlagBl("1"); //默认计费
                if(StringUtils.isNotEmpty(ord.getPkOrd())){
                    BdOrd bdOrd=DataBaseHelper.queryForBean("select * from bd_ord where pk_ord=? and del_flag='0' ",BdOrd.class,new Object[]{ord.getPkOrd()});
                    if(bdOrd!=null && StringUtils.isNotEmpty(bdOrd.getFlagCg())){
                        ord.setFlagBl(bdOrd.getFlagCg());
                    }
                }

                //查询执行诊区-2021.1.29-tjq-zsrm任务[5217]
                String pkDeptArea = null;
                //根据首先根据业务线类型12获取到医嘱执行科室对应的诊区，如果没有则默认患者就诊所在诊区
                if (StringUtils.isNotBlank(ord.getPkDeptExec())) {
                    List<Map<String, Object>> list = reqDao.qeryDeptArea(ord.getPkDeptExec());
                    if (list != null && list.size() > 0) {
                        pkDeptArea = MapUtils.getString(list.get(0), "pkDept");
                    }
                }
                if (StringUtils.isBlank(pkDeptArea)) {
                    pkDeptArea = pkDeptAreas;
                }
                ord.setPkDeptArea(pkDeptArea);//执行诊区
                ord.setPkDeptAreaapp(pkDeptAreas);//开立诊区（患者就诊所在诊区）
                if (ord.getCodeOrdtype().startsWith(Constants.RIS_SRVTYPE)) {
                    cnRisApply = new CnRisApply();
                    cnRisApply.setPkOrdris(ord.getPkOrdris());
                    cnRisApply.setPkCnord(ord.getPkCnord());
                    cnRisApply.setDtRistype(ord.getDtRistype());
                    cnRisApply.setDescBody(ord.getDescBody());
                    cnRisApply.setDelFlag("0");
                    cnRisApply.setEuStatus("0");//申请单状态
                    cnRisApply.setFlagPrint2("0");
                    cnRisApply.setFlagPrint("0");
                    cnRisApply.setNote(ord.getNote()); //备注
                    cnRisApply.setPurpose(ord.getPurpose());//检查目的
                    cnRisApply.setRisNotice(ord.getRisNotice());//检查注意事项
                    cnRisApply.setNoteDise(ord.getNoteDise());//病情描述
                    cnRisApply.setPkOrg(ord.getPkOrg());
                    cnRisApply.setTs(dateNow);
                } else if (ord.getCodeOrdtype().startsWith(Constants.LIS_SRVTYPE)) {
                    cnLabApply = new CnLabApply();
                    cnLabApply.setPkOrdlis(ord.getPkOrdlis());
                    cnLabApply.setPkCnord(ord.getPkCnord());
                    cnLabApply.setDtColtype(ord.getDtColltype()); //采集方法
                    cnLabApply.setDtSamptype(ord.getDtSamptype());
                    cnLabApply.setDtTubetype(ord.getDtTubetype());
                    cnLabApply.setDelFlag("0");
                    cnLabApply.setEuStatus("0");//申请单状态
                    cnLabApply.setNote(ord.getNote());//备注
                    cnLabApply.setPurpose(ord.getPurpose());//检验目的
                    cnLabApply.setPkOrg(ord.getPkOrg());
                    cnLabApply.setFormApp(ord.getFormApp());
                    cnLabApply.setTs(dateNow);
                    labCnOrdVos.add(ord);
                }
                if (StringUtils.isBlank(ord.getPkCnord())) {
                    String pkCnord = NHISUUID.getKeyId();
                    ord.setPkCnord(pkCnord);
                    ord.setTs(dateNow);
                    ord.setModityTime(null);
                    ord.setCreateTime(dateNow);
                    ord.setCreator(userInfo.getPkEmp());
                    ord.setDateEffe(getDateEffec(ord.getEuPvtype()));// 有效日期
                    //高值耗材
                    ord.setBarcode(ord.getBarcode());
                    addOrdList.add(ord);
                    if (cnRisApply != null) {
                        cnRisApply.setPkCnord(pkCnord);
                        cnRisApply.setPkOrdris(NHISUUID.getKeyId());
                        ord.setPkOrdris(cnRisApply.getPkOrdris());
                        addRisList.add(cnRisApply);
                    }
                    if (cnLabApply != null) {
                        cnLabApply.setPkCnord(pkCnord);
                        cnLabApply.setPkOrdlis(NHISUUID.getKeyId());
                        ord.setPkOrdlis(cnLabApply.getPkOrdlis());
                        addLabList.add(cnLabApply);
                    }
                    if(ord.getBlPdItems()!=null && ord.getBlPdItems().size()>0){
                        for(OrdBdPdAttVo ordbar: ord.getBlPdItems()){
                            CnOrderBar bar = new CnOrderBar();
                            bar.setPkCnordBar(NHISUUID.getKeyId());
                            bar.setPkCnord(ord.getPkCnord());
                            bar.setPkOrg(ord.getPkOrg());
                            bar.setEuAdditem("3");
                            bar.setPkItem(ordbar.getPkPd());
                            bar.setName(ordbar.getName());
                            bar.setQuan(ordbar.getQuan());
                            bar.setPkEmpAdd(userInfo.getPkEmp());
                            bar.setCreator(userInfo.getPkEmp());
                            bar.setCreateTime(new Date());
                            ordbarListAdd.add(bar);
                        }
                    }
                } else {
                    ord.setDateEnter(dateNow);
                    ord.setModityTime(dateNow);
                    //ord.setPkEmpOrd(userInfo.getPkEmp());
                    //ord.setNameEmpOrd(userInfo.getUserName());
                    updateOrdList.add(ord);
                    if (cnRisApply != null) {
                        if (cnRisApply.getPkOrdris() == null) cnRisApply.setPkOrdris(NHISUUID.getKeyId());
                        updateRisList.add(cnRisApply);
                    }
                    if (cnLabApply != null) {
                        if (cnLabApply.getPkOrdlis() == null) cnLabApply.setPkOrdlis(NHISUUID.getKeyId());
                        updateLabList.add(cnLabApply);
                    }
                    if(ord.getBlPdItems()!=null && ord.getBlPdItems().size()>0){
                        for(OrdBdPdAttVo ordbar: ord.getBlPdItems()){
                            CnOrderBar bar = new CnOrderBar();
                            bar.setPkCnordBar(NHISUUID.getKeyId());
                            bar.setPkCnord(ord.getPkCnord());
                            bar.setPkOrg(ord.getPkOrg());
                            bar.setEuAdditem("3");
                            bar.setPkItem(ordbar.getPkPd());
                            bar.setName(ordbar.getName());
                            bar.setQuan(ordbar.getQuan());
                            bar.setPkEmpAdd(userInfo.getPkEmp());
                            bar.setCreator(userInfo.getPkEmp());
                            bar.setCreateTime(new Date());
                            ordbarListUpdate.add(bar);
                        }
                    }
                }
                //获得记费VoList(新增、修改操作)  计费标记为0的不需要计费
                if(!"0".equals(ord.getFlagBl())){
                    opOrdToOpCg(ord, blOpCgList);

                }
                //药品费用
                opOrdToOpCgBd(ord, blOpCgList);
                if(itemlists!=null && itemlists.size()>0 && ord.getBlPdItems()!=null && ord.getBlPdItems().size()>0){
                    for(BlPubParamVo vo : itemlists){
                        blOpCgList.add(setCgVoValue(vo, ord, patInfo, userInfo, "诊断用药的记费项目"));
                    }
                }
                if (ord.getCodeOrdtype().startsWith(Constants.LIS_SRVTYPE)) {
                    dtSampType.put(ord.getDtSamptype(), ord);
                    dtTubetype.put(ord.getDtTubetype(), ord);
                    dtColtype.put(ord.getDtColltype(), ord);
                }
            }
        }
        //新增、修改Cn_order
        if (addOrdList != null && addOrdList.size() > 0) {

            logger.error("SyxCnOpApplyOrdService.saveRisLabApply删除处方相关医嘱：{}", JSON.toJSONString(addOrdList));
            //先删除重复医嘱
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord in ( select pk_cnord from cn_order ord where ord.ordsn=:ordsn)", addOrdList);
            DataBaseHelper.batchUpdate("delete from cn_order where ordsn=:ordsn  ", addOrdList);
            DataBaseHelper.batchUpdate("delete from CN_ORDER_BAR where pk_cnord in ( select pk_cnord from cn_order ord where ord.ordsn=:ordsn)  ", addOrdList);
            //重新插入
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), addOrdList);
            if(ordbarListAdd.size()>0)
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrderBar.class),ordbarListAdd);
            //保存CA签名信息
            cnPubService.caRecordByOrd(false, addOrdList);
            for (CnOrder ord : addOrdList){
                pkCnordDelete.add(ord.getPkCnord());
            }
        }
        if (updateOrdList != null && updateOrdList.size() > 0) {
            int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord in (" + getPkCnordStr(updateOrdList) + " ) and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{});
            if (hadSettleCgCount > 0) {
                throw new BusException("有收费项已进行缴费，数据更改失败，请重新接诊该患者！");
            }

            int applyCount = DataBaseHelper.queryForScalar("select count(1) from CN_RIS_APPLY where pk_cnord in (" + getPkCnordStr(updateOrdList) + " ) and EU_STATUS>2 and del_flag='0'", Integer.class, new Object[]{});
            if (applyCount > 0) {
                throw new BusException("有项目申请单已执行，数据不允许修改！");
            }
            applyCount = DataBaseHelper.queryForScalar("select count(1) from CN_LAB_APPLY where pk_cnord in (" + getPkCnordStr(updateOrdList) + " ) and EU_STATUS>2 and del_flag='0'", Integer.class, new Object[]{});
            if (applyCount > 0) {
                throw new BusException("有项目申请单已执行，数据不允许修改！");
            }
            if (flagEX) {
                hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from EX_ASSIST_OCC where pk_cnord in (" + getPkCnordStr(updateOrdList) + " ) and eu_status ='1' and del_flag='0'", Integer.class, new Object[]{});
                if (hadSettleCgCount > 0) {
                    throw new BusException("有项目已执行，数据更改失败，请重新接诊该患者！");
                }
            }
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord=:pkCnord and pk_pv=:pkPv and flag_settle='0'", updateOrdList);
            if (flagEX) {
                DataBaseHelper.batchUpdate("delete from EX_ASSIST_OCC_DT where pk_cnord = :pkCnord  ", updateOrdList);
                DataBaseHelper.batchUpdate("delete from EX_ASSIST_OCC where pk_cnord = :pkCnord and eu_status !='1' ", updateOrdList);
            }
            cnPubService.vaildUpdateCnOrdts(updateOrdList);
            DataBaseHelper.batchUpdate("delete from CN_ORDER_BAR where  pk_cnord = :pkCnord  ", updateOrdList);
            //重新插入
            DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOrder.class), updateOrdList);
            if(ordbarListUpdate.size()>0)
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrderBar.class), ordbarListUpdate);
            for (CnOrder ord : updateOrdList){
                pkCnordDelete.add(ord.getPkCnord());
            }
            //修改为null的数据
        }
        //新增、修改CnRisApply
        if (addRisList != null && addRisList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnRisApply.class), addRisList);
        }
        if (updateRisList != null && updateRisList.size() > 0) {
            //全部删除
            DataBaseHelper.batchUpdate("delete from cn_ris_apply where pk_cnord =:pkCnord", updateRisList);
            DataBaseHelper.batchUpdate("delete from cn_lab_apply where pk_cnord =:pkCnord", updateRisList);
            //重新插入
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnRisApply.class), updateRisList);
            //DataBaseHelper.batchUpdate("update cn_ris_apply set ts=:ts, pk_cnord=:pkCnord,dt_ristype=:dtRistype,desc_body=:descBody,pk_org=:pkOrg,purpose=:purpose,note=:note,ris_notice=:risNotice,note_dise=:noteDise where pk_ordris = :pkOrdris ", updateRisList);
        }
        //新增、修改CnLabApply
        if (addLabList != null && addLabList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnLabApply.class), addLabList);
        }
        if (updateLabList != null && updateLabList.size() > 0) {
            //全部删除
            DataBaseHelper.batchUpdate("delete from cn_lab_apply where pk_cnord =:pkCnord ", updateLabList);
            DataBaseHelper.batchUpdate("delete from cn_ris_apply where pk_cnord =:pkCnord", updateLabList);
            //重新插入
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnLabApply.class), updateLabList);
            //DataBaseHelper.batchUpdate("update cn_lab_apply set ts=:ts,dt_samptype=:dtSamptype,dt_tubetype=:dtTubetype,dt_coltype=:dtColtype,pk_org=:pkOrg, pk_cnord=:pkCnord,purpose=:purpose,note=:note where pk_ordlis = :pkOrdlis ", updateLabList);
        }
        //深大平台发送消息开关
        String processClass = ApplicationUtils.getPropertyValue("msg.processClass", "");
        if ("SDPlatFormSendService".equals(processClass)) {//获取删除检验检验处方消息值
            String delPkCnords = "";
            if (deleteRisLabList.size() > 0) {
                String sql = "SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always, "
                        + " dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer, "
                        + " co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input FROM cn_order co "
                        + " LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where 1=1 "
                        + "and co.pk_cnord  in ( ";
                int pkCnOrdSize = deleteRisLabList.size();
                for (int i = 0; i < pkCnOrdSize; i++) {
                    if (i == pkCnOrdSize - 1) {
                        if (StringUtils.isNotBlank(deleteRisLabList.get(i).getPkCnord())) {
                            delPkCnords += "'" + deleteRisLabList.get(i).getPkCnord() + "')";
                        }
                    } else {
                        if (StringUtils.isNotBlank(deleteRisLabList.get(i).getPkCnord())) {
                            delPkCnords += "'" + deleteRisLabList.get(i).getPkCnord() + "',";
                        }
                    }
                }
                delMapList = DataBaseHelper.queryForList(sql + delPkCnords);
            }
        }
        //深圳坪山口腔平台发送消息开关
        List<Map<String, Object>> delPSKQO09MapList = new ArrayList<>();//口腔删除检查检验医嘱所需的消息集合
        if ("PskqPlatFormSendService".equals(processClass)) {//获取删除检验检验处方消息值
            if (deleteRisLabList.size() > 0) {
                List<String> qryUpdatePacsList = Lists.newArrayList();
                List<String> qryUpdateLisList = Lists.newArrayList();
                for (int i = 0; i < deleteRisLabList.size(); i++) {
                    if ("02".equals(deleteRisLabList.get(i).getCodeOrdtype().substring(0, 2)))
                        qryUpdatePacsList.add(deleteRisLabList.get(i).getPkCnord());
                    if ("03".equals(deleteRisLabList.get(i).getCodeOrdtype().substring(0, 2)))
                        qryUpdateLisList.add(deleteRisLabList.get(i).getPkCnord());
                }
                ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper = applicationContext.getBean("pskqPlatFormSendCnMapper", PskqPlatFormSendCnMapper.class);
                List<ExamApply> pacsUpList = new ArrayList<>();
                List<LabApply> lisUpList = new ArrayList<>();
                if (qryUpdatePacsList != null && qryUpdatePacsList.size() > 0) {
                    pacsUpList = pskqPlatFormSendCnMapper.queryPacsApplyInfoById(qryUpdatePacsList);
                    for (int i = 0; i < pacsUpList.size(); i++) {
                        pacsUpList.get(i).setCancelOperaId(((User) user).getCodeEmp());//主键
                        pacsUpList.get(i).setCancelOperaName(user.getUserName());
                    }
                }
                if (qryUpdateLisList != null && qryUpdateLisList.size() > 0) {
                    lisUpList = pskqPlatFormSendCnMapper.queryLisApplyInfoById(qryUpdateLisList);
                    for (int i = 0; i < lisUpList.size(); i++) {
                        lisUpList.get(i).setCancelOperaId(((User) user).getCodeEmp());//主键
                        lisUpList.get(i).setCancelOperaName(user.getUserName());
                    }
                }
                Map<String, Object> pacsMap = new HashMap<>();
                pacsMap.put("pacsUpList", pacsUpList);
                Map<String, Object> lisMap = new HashMap<>();
                lisMap.put("lisUpList", lisUpList);
                delMapList.add(pacsMap);//注意前后新增保持一致
                delMapList.add(lisMap);
                List<String> pkCnords = new ArrayList<>();
                for (int i = 0; i < deleteRisLabList.size(); i++) {
                    if (StringUtils.isNotBlank(deleteRisLabList.get(i).getPkCnord())) {
                        pkCnords.add(deleteRisLabList.get(i).getPkCnord());
                    }
                }
                if (pkCnords.size() > 0) {
                    List<OrderOutpat> OrderOutpatList = pskqPlatFormSendCnMapper.queryOpCnorderDrugs(pkCnords);
                    OrderOutpatList.addAll(pskqPlatFormSendCnMapper.queryOpCnorderProject(pkCnords));
                    for (OrderOutpat orderOutpat : OrderOutpatList) {
                        orderOutpat.setCancelFlag("1");
                        orderOutpat.setCancelOperaId(((User) user).getPkUser());
                        orderOutpat.setCancelOperaName(user.getUserName());
                        String stringBean = JsonUtil.writeValueAsString(orderOutpat);
                        delPSKQO09MapList.add(JsonUtil.readValue(stringBean, Map.class));
                    }
                }
            }
        }
        //删除操作
        if (deleteRisLabList.size() > 0) {

            List<BlOpDt> delOpDtlist= DataBaseHelper.queryForList("select * from bl_op_dt where pk_pv='" + patInfo.getPkPv() + "' and pk_cnord in ("+getPkCnordStr(deleteRisLabList)+") and barcode is not null", BlOpDt.class, new Object[]{});


            int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord in (" + getPkCnordStr(deleteRisLabList) + " ) and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{});
            if (hadSettleCgCount > 0) {
                throw new BusException("有收费项已进行缴费，数据更改失败，请重新接诊该患者！");
            }
            if (flagEX) {
                hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from EX_ASSIST_OCC where pk_cnord in (" + getPkCnordStr(deleteRisLabList) + " ) and eu_status ='1' and del_flag='0'", Integer.class, new Object[]{});
                if (hadSettleCgCount > 0) {
                    throw new BusException("有项目已执行，数据更改失败，请重新接诊该患者！");
                }
            }
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_pv='" + patInfo.getPkPv() + "' and pk_cnord = :pkCnord and flag_settle='0'", deleteRisLabList);
            DataBaseHelper.batchUpdate("delete from cn_ris_apply where pk_cnord = :pkCnord ", deleteRisLabList);
            DataBaseHelper.batchUpdate("delete from cn_lab_apply where pk_cnord = :pkCnord ", deleteRisLabList);
            logger.error("SyxCnOpApplyOrdService.saveRisLabApply删除处方相关医嘱：{}",JSON.toJSONString(deleteRisLabList));
            DataBaseHelper.batchUpdate("delete from cn_order where pk_pv='" + patInfo.getPkPv() + "' and pk_cnord = :pkCnord ", deleteRisLabList);
            DataBaseHelper.batchUpdate("delete from CN_ORDER_BAR where  pk_cnord = :pkCnord  ", deleteRisLabList);
            if (flagEX) {
                DataBaseHelper.batchUpdate("delete from EX_ASSIST_OCC_DT where pk_cnord = :pkCnord  ",deleteRisLabList);
                DataBaseHelper.batchUpdate("delete from EX_ASSIST_OCC where pk_cnord = :pkCnord and eu_status !='1' ", deleteRisLabList);
            }
            if(delOpDtlist!=null&&delOpDtlist.size()>0)
            {
                delOpDtlist.forEach(v->{v.setPkCgop(null);});
                BlPubReturnVo vo=new BlPubReturnVo();
                vo.setBods(delOpDtlist);
                //清除中间库sd_high_value高值耗材码但是不发送消息
                ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "savaOpReturnConsumable", "",vo);
            }
        }
        //检验标本、容器费用 构造计费数据（这里是按照医嘱来构造，算法规则下沉到下层）
        if (labCnOrdVos.size() > 0) {
            Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("euPvtype", patInfo.getEuPvtype());
            if (dtSampType.size() > 0) {
                mapParam.put("doCodes", dtSampType.keySet());
                mapParam.put("doclistCode", "030200");//检验标本
                List<BlPubParamVo> labSampCgVos = reqDao.qryDocItemCgvo(mapParam);
                if (CollectionUtils.isNotEmpty(labSampCgVos)) {
                    //		Map<String, List<BlPubParamVo>> codeGroup = labSampCgVos.stream().collect(Collectors.groupingBy(BlPubParamVo::getDtSamptype));
                    labCnOrdVos.stream().filter(vo -> vo != null
                            && StringUtils.isNotBlank(vo.getCodeOrdtype())
                            && vo.getCodeOrdtype().startsWith(Constants.LIS_SRVTYPE)
                            && StringUtils.isNotBlank(vo.getDtSamptype()))
                            .forEach(samp -> {
                                //List<BlPubParamVo> paramVos = codeGroup.get(samp.getDtSamptype());
                                for (BlPubParamVo vo : labSampCgVos) {
                                    vo.setDtSamptype(vo.getCodeDefdoc());
                                    if (vo.getDtSamptype().equals(samp.getDtSamptype()))
                                        blOpCgList.add(setCgVoValue(vo, samp, patInfo, userInfo, "标本收费"));
                                }
                            });
                }
            }
            if (dtTubetype.size() > 0) {
                mapParam.put("doCodes", dtTubetype.keySet());
                mapParam.put("doclistCode", "030203");//检验容器
                List<BlPubParamVo> labContCgVos = reqDao.qryDocItemCgvo(mapParam);
                if (CollectionUtils.isNotEmpty(labContCgVos)) {
                    //Map<String, List<BlPubParamVo>> codeGroup = labContCgVos.stream().collect(Collectors.groupingBy(BlPubParamVo::getDtTubetype));
                    labCnOrdVos.stream().filter(vo -> vo != null
                            && StringUtils.isNotBlank(vo.getCodeOrdtype())
                            && vo.getCodeOrdtype().startsWith(Constants.LIS_SRVTYPE)
                            && StringUtils.isNotBlank(vo.getDtTubetype()))
                            .forEach(tube -> {
                                //List<BlPubParamVo> paramVos = codeGroup.get(tube.getDtTubetype());
                                for (BlPubParamVo vo : labContCgVos) {
                                    vo.setDtTubetype(vo.getCodeDefdoc());
                                    if (vo.getDtTubetype().equals(tube.getDtTubetype()))
                                        blOpCgList.add(setCgVoValue(vo, tube, patInfo, userInfo, "容器收费"));
                                }
                            });
                }
            }
            if (dtColtype.size() > 0) {
                //采集方法收费构造
                mapParam.put("doCodes", dtColtype.keySet());
                mapParam.put("doclistCode", "030201");
                List<BlPubParamVo> labCollCgVos = reqDao.qryDocItemCgvo(mapParam);
                if (CollectionUtils.isNotEmpty(labCollCgVos)) {
                    Map<String, List<BlPubParamVo>> collect = labCollCgVos.parallelStream().filter(v -> StringUtils.isNotBlank(v.getCodeDefdoc())).collect(Collectors.groupingBy(BlPubParamVo::getCodeDefdoc));
                    labCnOrdVos.stream().filter(vo -> vo != null
                            && StringUtils.isNotBlank(vo.getCodeOrdtype())
                            && vo.getCodeOrdtype().startsWith(Constants.LIS_SRVTYPE)
                            && StringUtils.isNotBlank(vo.getDtColltype())
                            && collect.keySet().contains(vo.getDtColltype()))
                            .map(vo -> collect.get(vo.getDtColltype()).stream().map(dt -> {
                                dt.setDtColltype(dt.getCodeDefdoc());
                                return setCgVoValue(dt, vo, patInfo, userInfo, "采集方法收费");
                            }).collect(Collectors.toList()))
                            .collect(Collectors.toList()).forEach(vo -> blOpCgList.addAll(vo));
                }
            }
        }
        //新增记费记录
        if (blOpCgList.size() > 0) {

            List<BlPubParamVo> blOpItemNew=new ArrayList<BlPubParamVo>();
            for (BlPubParamVo vo : blOpCgList){
                if(pkCnordDelete.contains(vo.getPkCnord())){
                    blOpItemNew.add(vo);
                }
            }
            logger.error("SyxCnOpApplyOrdService.saveRisLabApply新增检查、检验、治疗医嘱：{}", JSON.toJSONString(blOpItemNew));
            BlPubReturnVo vo = opCgPubService.blOpCgBatch(blOpItemNew);

            if (flagEX) {
                List<BlOpDt> exDtList = vo.getBods();
                if (exDtList != null && exDtList.size() > 0) {
                    exDtList.forEach(m->{
                        m.setFlagPd("0");
                    });
                    settlePdOutService.generateExAssistOcc(exDtList);
                }
            }
        }


        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("addOrdList", addOrdList);
        paramMap.put("updateOrdList", updateOrdList);
        paramMap.put("delMapList", delMapList);
        paramMap.put("delPSKQO09MapList", delPSKQO09MapList);//坪山口腔所需检查检验医嘱集合
        PlatFormSendUtils.sendCnOpAppMsg(paramMap);
        return reqParam.getModifyList();
    }
    
   
    //获得拼接条件--pkCnord
    private String getPkCnordStr(List<? extends CnOrder> ordList) {
        StringBuffer sbfSql = new StringBuffer();
        String rtnStr = "";
        if (ordList != null && ordList.size() > 0) {
            for (CnOrder order : ordList) {
                sbfSql.append("'" + order.getPkCnord() + "',");
            }
            sbfSql.deleteCharAt(sbfSql.length() - 1);
            rtnStr = sbfSql.toString();
        }
        return rtnStr;
    }

    //vo转换--cnord转cg
    public void opOrdToOpCg(CnOrder opOrd, List<BlPubParamVo> blOpItem) {
        User u = UserContext.getUser();
        String pkOrg = u.getPkOrg();
        String pkDept = u.getPkDept();
        BlPubParamVo bpb = new BlPubParamVo();
        bpb.setPkOrg(pkOrg);
        bpb.setEuPvType(opOrd.getEuPvtype());
        bpb.setPkPv(opOrd.getPkPv());
        bpb.setPkPi(opOrd.getPkPi());
        bpb.setDateStart(opOrd.getDateStart());
        bpb.setDtSamptype(opOrd.getDtSamptype());
        bpb.setCodeOrdtype(opOrd.getCodeOrdtype());
        bpb.setPkOrd(opOrd.getPkOrd());
        bpb.setPkCnord(opOrd.getPkCnord());
        bpb.setOrdsn(opOrd.getOrdsn());
        bpb.setOrdsnParent(opOrd.getOrdsnParent());
        bpb.setPkPres(opOrd.getPkPres());
        bpb.setPkItem(null);
        bpb.setQuanCg(opOrd.getQuanCg());
        bpb.setPkOrgEx(opOrd.getPkOrgExec());
        bpb.setPkOrgApp(opOrd.getPkOrg());
        bpb.setPkDeptEx(opOrd.getPkDeptExec());
        bpb.setPkDeptApp(opOrd.getPkDept());
        bpb.setPkEmpApp(opOrd.getPkEmpOrd());
        bpb.setNameEmpApp(opOrd.getNameEmpOrd());
        bpb.setFlagPd("0");
        bpb.setNamePd(opOrd.getNameOrd());
        bpb.setFlagPv("0");
        //bpb.setDateExpire(opOrd.getDateExpire());
        bpb.setPkUnitPd(opOrd.getPkUnitCg());
        bpb.setPackSize(opOrd.getPackSize().intValue());
        bpb.setPrice(opOrd.getPriceCg());
        //bpb.setPriceCost(opOrd.getPriceCost());
        bpb.setDateHap(new Date());
        bpb.setPkDeptCg(pkDept);
        bpb.setPkEmpCg(u.getPkEmp());
        bpb.setNameEmpCg(u.getNameEmp());
        bpb.setEuAdditem("0"); //非附加费
        bpb.setPkDeptJob(opOrd.getPkDeptJob());
        bpb.setPkDeptAreaapp(opOrd.getPkDeptAreaapp());//开立诊区
        bpb.setPkCnordRl(opOrd.getPkCnordRl());//关联医嘱
        bpb.setBarcode(opOrd.getBarcode());//高值耗材
        blOpItem.add(bpb);
    }

    //标本容器、vo字段赋值--cg
    private BlPubParamVo setCgVoValue(BlPubParamVo blPubVo, SyxCnOrderVo labVo, PatInfo patInfo, User u, String spec) {
        BlPubParamVo blVo = new BlPubParamVo();
        ApplicationUtils.copyProperties(blVo, blPubVo);
        blVo.setPkPv(labVo.getPkPv());
        blVo.setPkPi(labVo.getPkPi());
        blVo.setPkCnord(labVo.getPkCnord());//设置医嘱主键，用于绑定相关记费项目
        blVo.setSpec(spec);//设置规格，注明是标本 | 容器费用
        blVo.setDateExpire(labVo.getDateStart());//标本合费时借该字段用于缓存计划执行时间
        blVo.setPkOrg(u.getPkOrg());
        blVo.setEuPvType(patInfo.getEuPvtype());
        blVo.setFlagPd("0");
        blVo.setFlagPv("0");
        blVo.setEuBltype("0");
        blVo.setPkOrgApp(u.getPkOrg());
        blVo.setPkDeptApp(labVo.getPkDept());
        blVo.setPkOrgEx(u.getPkOrg());
        //blVo.setPkDeptEx(u.getPkDept());//标本|容器的记费科室为当前执行科室
        blVo.setPkDeptEx(labVo.getPkDeptExec());//标本|容器的记费科室为检验执行科室
        blVo.setDateHap(new Date());//费用发生时间
        blVo.setPkDeptCg(u.getPkDept());
        blVo.setPkEmpCg(u.getPkEmp());
        blVo.setNameEmpCg(u.getNameEmp());
        blVo.setPkEmpApp(labVo.getPkEmpOrd());
        blVo.setNameEmpApp(labVo.getNameEmpOrd());
        blVo.setEuAdditem("1");//附加费用
        blVo.setSampTube(true);
        blVo.setPkDeptJob(labVo.getPkDeptJob());
        blVo.setPkOrdChk(labVo.getPkOrd());
        blVo.setPkDeptJob(labVo.getPkDeptJob());
        blVo.setPkDeptAreaapp(labVo.getPkDeptAreaapp());
        return blVo;
    }


    //查询检验附加费用
    public List<Map<String, Object>> queryLabAddItems(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = reqDao.queryLabAddItems(paramMap);
        return list;
    }

    private Date getDateEffec(String euPvType) {

        String val = ApplicationUtils.getSysparam("CN0004", false);
        if (org.apache.commons.lang3.StringUtils.isEmpty(val)) {
            if (EnumerateParameter.TWO.equals(euPvType))// 急诊
            {
                val = EnumerateParameter.TWO;
            } else {// 门诊
                val = EnumerateParameter.THREE;
            }

        }
        Date dateEffec = DateUtils.getSpecifiedDay(new Date(), Integer.parseInt(val));
        return dateEffec;
    }

    /***************检查项目药品计费  博爱***************/
    private void opOrdToOpCgBd(SyxCnOrderVo Ord, List<BlPubParamVo> blOpItem) {
        User u = UserContext.getUser();
        List<OrdBdPdAttVo> opOrds=Ord.getBlPdItems();
        if(opOrds==null || opOrds.size()==0) return;
        //BdPdAttVo opOrd=
        for(OrdBdPdAttVo opOrd:opOrds){
            String pkOrg = u.getPkOrg();
            String pkDept = u.getPkDept();
            BlPubParamVo bpb = new BlPubParamVo();
            bpb.setPkOrg(pkOrg);
            bpb.setEuPvType(Ord.getEuPvtype());
            bpb.setPkPv(Ord.getPkPv());
            bpb.setPkPi(Ord.getPkPi());
            bpb.setDateStart(Ord.getDateStart());
            bpb.setDtSamptype(Ord.getDtSamptype());
            bpb.setCodeOrdtype("99");//Ord.getCodeOrdtype()
            bpb.setPkOrd(opOrd.getPkPd());
            bpb.setPkCnord(Ord.getPkCnord());
            bpb.setOrdsn(Ord.getOrdsn());
            bpb.setOrdsnParent(Ord.getOrdsnParent());
            bpb.setPkPres(Ord.getPkPres());
            bpb.setPkItem(null);
            bpb.setQuanCg(opOrd.getQuan());
            bpb.setPkOrgEx(Ord.getPkOrgExec());
            bpb.setPkOrgApp(Ord.getPkOrg());
            bpb.setPkDeptEx(Ord.getPkDeptExec());
            bpb.setPkDeptApp(Ord.getPkDept());
            bpb.setPkEmpApp(Ord.getPkEmpOrd());
            bpb.setNameEmpApp(Ord.getNameEmpOrd());
            bpb.setFlagPd("0");
            bpb.setNamePd(opOrd.getName());
            bpb.setFlagPv("0");
            bpb.setPkUnitPd(opOrd.getPkUnitMin());
            bpb.setPackSize(1);
            bpb.setPrice(opOrd.getQuanprice());
            bpb.setDateHap(new Date());
            bpb.setPkDeptCg(pkDept);
            bpb.setPkEmpCg(u.getPkEmp());
            bpb.setNameEmpCg(u.getNameEmp());
            bpb.setEuAdditem("1"); //非附加费
            bpb.setPkDeptJob(Ord.getPkDeptJob());
            bpb.setPkDeptAreaapp(Ord.getPkDeptAreaapp());//开立诊区
            bpb.setPkCnordRl(Ord.getPkCnordRl());//关联医嘱
            bpb.setBarcode(opOrd.getBarcode());//高值耗材
            blOpItem.add(bpb);
        }

    }
    /***************检查项目药品计费  博爱***************/
}
