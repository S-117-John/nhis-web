package com.zebone.nhis.cn.ipdw.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.cn.ipdw.dao.CnIpOpMagementMapper;
import com.zebone.nhis.cn.ipdw.dao.CnOrderMapper;
import com.zebone.nhis.cn.ipdw.factory.AbstractCnOrderFactory;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.*;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.cn.pub.service.OrderPubService;
import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.*;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Service
public class CnOrderService {
    @Autowired
    private CnIpOpMagementMapper medicalDao;
    @Autowired
    private CnOrderMapper CnOrderDao;
    @Autowired
    private CnPubService cnPubService;

    @Autowired
    private OrderPubService orderPubService;
    @Autowired
    private IpCgPubService cgDao;
    @Autowired
    private CnIpOrdProcService ordProcService;

    @Autowired
    private CnNoticeService cnNoticeService;

    private static String FLAG_PD = "1";//??????
    private static String FlAG_lONG = "0";//??????

    public List<Map<String, Object>> qryCnOrderRef(String param, IUser user) {
        return CnOrderDao.qryCnOrderRef(new HashMap());
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     */
    public void saveOrder(String param, IUser user) {
        OrderParam list = JsonUtil.readValue(param, OrderParam.class);
        List<CnOrder> addList = list.getNewOrdList();
        List<CnOrder> updList = list.getUpdateOrdList();
        List<CnOrder> delList = list.getDelOrdList();
        List<CnOrder> updNewList = new ArrayList<CnOrder>();
        List<CnRisApply> addRisList = new ArrayList<CnRisApply>();
        List<CnRisApply> delRisList = new ArrayList<CnRisApply>();
        List<CnLabApply> addLisList = new ArrayList<CnLabApply>();
        List<CnLabApply> delLisList = new ArrayList<CnLabApply>();
        List<CnOrder> antiDelList = new ArrayList<CnOrder>();
        CnOrdAntiApply antiItemApply = list.getAntiItemApply();

        /**?????????????????????**/
        boolean ifRY= "1".equals(list.getIfry());

        User loginUser = (User) user;
        Date d = new Date();
        String pkPv=null;
        String pkWg=null;
        String pkWgOrg=null;
        if(addList!=null && addList.size()>0 && StringUtils.isNotEmpty(addList.get(0).getPkPv())){
            pkPv=addList.get(0).getPkPv();
            //??????????????????????????????
            if(checkPvInfo(pkPv)){
                throw  new BusException("???????????????????????????????????????,?????????????????????????????????????????????????????????????????????");
            }

            PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{pkPv});
            pkWg=pvInfo.getPkWg();
            pkWgOrg=pvInfo.getPkWgOrg();
        }else if(updList!=null && updList.size()>0 && StringUtils.isNotEmpty(updList.get(0).getPkPv())){
            pkPv=updList.get(0).getPkPv();
            //??????????????????????????????
            if(checkPvInfo(pkPv)){
                throw  new BusException("???????????????????????????????????????,?????????????????????????????????????????????????????????????????????");
            }
            PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{pkPv});
            pkWg=pvInfo.getPkWg();
            pkWgOrg=pvInfo.getPkWgOrg();
        }

        //??????????????????--??????
        if(ifRY){
            if(addList.size()>0) setOrdPres(addList);
            if(updList.size()>0) setOrdPres(updList);
        }

        for (CnOrder addCnOrder : addList) {
            addCnOrder.setPkWg(pkWg);
            addCnOrder.setPkWgOrg(pkWgOrg);//??????????????????
            addNewCnOrder(addRisList, addLisList, loginUser, d, addCnOrder);
        }
        for (CnOrder delCnOrder : delList) {
            String codeOrdtype = delCnOrder.getCodeOrdtype();
            if (codeOrdtype.startsWith(Constants.RIS_SRVTYPE)) {
                CnRisApply cnRisApply = new CnRisApply();
                cnRisApply.setPkCnord(delCnOrder.getPkCnord());
            } else if (codeOrdtype.startsWith(Constants.LIS_SRVTYPE)) {
                CnLabApply cnLabApply = new CnLabApply();
                cnLabApply.setPkCnord(delCnOrder.getPkCnord());
            }
        }
        for (CnOrder upCnOrder : updList) {
            upCnOrder.setPkWg(pkWg);
            upCnOrder.setPkWgOrg(pkWgOrg);//??????????????????
            if (StringUtils.isBlank(upCnOrder.getPkCnord())) {
                addNewCnOrder(addRisList, addLisList, loginUser, d, upCnOrder);
                addList.add(upCnOrder);
            } else {
                if (upCnOrder.getTs() != null) {
                    updNewList.add(upCnOrder);
                }
            }
            if ("0".equals(upCnOrder.getFlagPrev()) && "0".equals(upCnOrder.getFlagThera()) && StringUtils.isNotBlank(upCnOrder.getPkOrdanti())) {
                antiDelList.add(upCnOrder);
            }
        }
        if (addList != null && addList.size() > 0) {
            orderPubService.CheckOrd(addList); //????????????
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), addList);
            saveOrdAnti(addList, (User) user, false);
            //?????????????????????????????????????????????--begin----
//			List<Integer> ordsnList=new ArrayList<Integer>();
//			for(CnOrder addCnOrder :addList){
//				ordsnList.add(addCnOrder.getOrdsn());
//			}
//		   if(ordsnList!=null && ordsnList.size()>0){
//			   Map<String, Object> ordsnMap=new HashMap<String,Object>();
//			   ordsnMap.put("ordsn", ordsnList);
//			   String sqlStr="select ordsn from cn_order where ordsn in (:ordsn) group by ordsn having count(1)>1";
//			   List<Map<String,Object>> repeartApplyList=DataBaseHelper.queryForList(sqlStr, ordsnMap);
//			   if(repeartApplyList!=null && repeartApplyList.size()>0){
//				   throw new BusException("?????????????????????????????????");
//			   }
//		   }
//		  //?????????????????????????????????????????????--end----
        }
        if (updNewList != null && updNewList.size() > 0) {
            orderPubService.CheckOrd(updNewList); //????????????
            cnPubService.vaildUpdateCnOrdts(updNewList);
            List<CnOrder> os = getOrd(updNewList); //??????????????????????????????????????????
            for (CnOrder upCnOrder : updNewList) {
                upCnOrder.setTs(d);
                for (CnOrder o : os) {
                    if (o.getPkCnord().equals(upCnOrder.getPkCnord()) && "1".equals(o.getFlagStop())) {
                        upCnOrder.setDateStop(o.getDateStop());
                        upCnOrder.setFlagStop(o.getFlagStop());
                        upCnOrder.setPkEmpStop(o.getPkEmpStop());
                        upCnOrder.setNameEmpStop(o.getNameEmpStop());
                        upCnOrder.setLastNum(o.getLastNum());
                        break;
                    }
                }
            }
            DataBaseHelper.batchUpdate("update cn_order set ts=:ts,ordsn_parent=:ordsnParent,eu_always = :euAlways,date_start = :dateStart,pk_ord=:pkOrd,code_ord=:codeOrd,name_ord = :nameOrd,dosage=:dosage,pk_unit_dos=:pkUnitDos,quan=:quan,code_supply=:codeSupply,code_freq=:codeFreq,drip_speed=:dripSpeed,flag_first=:flagFirst,pk_dept_exec=:pkDeptExec,pk_org_exec=:pkOrgExec,note_supply=:noteSupply,note_ord=:noteOrd,flag_thera=:flagThera,flag_prev=:flagPrev,flag_fit=:flagFit,flag_medout=:flagMedout,flag_self=:flagSelf,flag_stop=:flagStop, pk_emp_stop=:pkEmpStop, name_emp_stop=:nameEmpStop,date_stop=:dateStop,last_num=:lastNum,flag_durg=:flagDurg,flag_bl=:flagBl,code_ordtype=:codeOrdtype,spec=:spec,pk_unit=:pkUnit,flag_base=:flagBase,eu_st=:euSt,desc_ord=:descOrd,first_num=:firstNum,desc_fit=:descFit,pk_ord_exc=:pkOrdExc,eu_exctype=:euExctype,groupno=:groupno,quan_cg=:quanCg,pk_unit_cg=:pkUnitCg,flag_pivas=:flagPivas,flag_emer=:flagEmer,pk_emp_input=:pkEmpInput,name_emp_input=:nameEmpInput,flag_note=:flagNote,pack_size=:packSize,pk_dept_job=:pkDeptJob,pk_wg=:pkWg,PK_WG_ORG=:pkWgOrg,flag_plan=:flagPlan where pk_cnord = :pkCnord ", updNewList);
            saveOrdAnti(updNewList, (User) user, true);
            DataBaseHelper.batchUpdate("delete from cn_ord_anti_apply where pk_cnord = :pkCnord and not exists (select 1 from bd_pd pd where pd.pk_pd=:pkOrd and pd.dt_anti='03') ", updNewList);
        }
        if (delList != null && delList.size() > 0) {
            cnPubService.vaildUpdateCnOrdts(delList);
            DataBaseHelper.batchUpdate("delete from cn_order where pk_cnord = :pkCnord ", delList);
            DataBaseHelper.batchUpdate("delete from cn_ord_anti where pk_cnord = :pkCnord ", delList);
            DataBaseHelper.batchUpdate("delete from cn_ord_anti_apply where pk_cnord = :pkCnord ", delList);
            //??????????????????????????????????????????????????????????????????????????????????????????---begin--
            List<CnOrder> delParentOrdList = new ArrayList<CnOrder>();
            for (CnOrder ordVo : delList) {
                if (ordVo.getOrdsn().compareTo(ordVo.getOrdsnParent()) == 0) {
                    delParentOrdList.add(ordVo);
                }
            }
            if (delParentOrdList.size() > 0) {
                List<String> delOrdsnParentList = new ArrayList<String>();
                for (CnOrder order : delParentOrdList) {
                    delOrdsnParentList.add(order.getOrdsnParent().toString());
                }
                Map<String, Object> pkOrdMap = new HashMap<String, Object>();
                pkOrdMap.put("delOrdsnParentList", delOrdsnParentList);
                String sqlStr = "select * from cn_order where eu_status_ord>'0' and del_flag='0' and ordsn_parent in(:delOrdsnParentList)";
                List<Map<String, Object>> cancelDelOrdList = DataBaseHelper.queryForList(sqlStr, pkOrdMap);
                if (cancelDelOrdList != null && cancelDelOrdList.size() > 0) {
                    String throwStr = "";
                    for (Map<String, Object> map : cancelDelOrdList) {
                        throwStr += "?????????[" + map.get("nameOrd") + "]????????????????????????????????????????????????????????????????????????!" + "\n";
                    }
                    if (StringUtils.isNotEmpty(throwStr))
                        throw new BusException(throwStr);
                }
                sqlStr = "select * from cn_order where eu_status_ord='0' and del_flag='0' and ordsn_parent in(:delOrdsnParentList)";
                List<Map<String, Object>> delOrdList = DataBaseHelper.queryForList(sqlStr, pkOrdMap);
                if (delOrdList != null && delOrdList.size() > 0) {
                    DataBaseHelper.batchUpdate("delete from cn_order where pk_cnord =:pkCnord ", delOrdList);
                    DataBaseHelper.batchUpdate("delete from cn_ord_anti where pk_cnord = :pkCnord ", delOrdList);
                    DataBaseHelper.batchUpdate("delete from cn_ord_anti_apply where pk_cnord = :pkCnord ", delOrdList);
                }
            }
            //??????????????????????????????????????????????????????????????????????????????????????????---end--
        }
        if (addRisList != null && addRisList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnRisApply.class), addRisList);
        }
        if (delRisList != null && delRisList.size() > 0) {
            DataBaseHelper.execute("delete from cn_ris_apply where pk_cnord = :pkCnord", delRisList);
        }
        if (addLisList != null && addLisList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnLabApply.class), addLisList);
        }
        if (delLisList != null && delLisList.size() > 0) {
            DataBaseHelper.execute("delete from cn_lab_apply where pk_cnord = :pkCnord", delLisList);
        }
        if (antiDelList != null && antiDelList.size() > 0) {
            DataBaseHelper.batchUpdate("delete from cn_ord_anti where pk_cnord=:pkCnord", antiDelList);
        }
        if (antiItemApply != null) {
            DataBaseHelper.insertBean(antiItemApply);
        }
    }

    /**
     * ?????????????????????????????????
     * ????????????004004003066
     *
     * @param param
     * @param user
     */
    public void saveAntiItemApply(String param, IUser user) {
        CnOrdAntiApply antiItemApply = JsonUtil.readValue(param, CnOrdAntiApply.class);
        if (antiItemApply != null) {
            DataBaseHelper.insertBean(antiItemApply);
        }
    }

    /**
     * ???????????????????????????????????????????????? ???????????????????????????????????????
     * ????????????004004003065
     *
     * @param param
     * @param user
     * @return
     */
    public CnOrdAntiApply qryCnOrdAntiApply(String param, IUser user) {
        CnOrdAntiApply cnOrdAntiApply = null;
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap != null) {
            String pkOrd = (String) paramMap.get("pkOrd");
            String pkPv = (String) paramMap.get("pkPv");
            StringBuilder sb = new StringBuilder();
            Object[] objArray = null;
            sb.append("SELECT coap.* ");
            sb.append("FROM   CN_ORD_ANTI_APPLY coap ");
            sb.append("INNER  JOIN cn_order co ");
            sb.append("ON     co.pk_cnord = coap.pk_cnord ");
            sb.append("WHERE  coap.del_flag = '0' ");
            sb.append("AND    co.pk_ord = ? ");
            sb.append("AND    co.pk_pv = ? ");
            sb.append("ORDER  BY coap.create_time DESC ");
            objArray = new Object[]{pkOrd, pkPv};
            String sql = sb.toString();
            List<CnOrdAntiApply> objList = DataBaseHelper.queryForList(sql, CnOrdAntiApply.class, objArray);
            for (CnOrdAntiApply item : objList) {
                //??????????????????????????????
                cnOrdAntiApply = item;
                break;
            }
        }
        return cnOrdAntiApply;
    }

    //???????????????????????????
    public List<CnOrder> getOrd(List<CnOrder> cnOrds) {
        if (cnOrds == null || cnOrds.size() == 0) return null;
        List<CnOrder> ords = DataBaseHelper.queryForList("select * from cn_order ord where ord.pk_pv=? and flag_stop_chk='1' and  eu_always='0' ", CnOrder.class, new Object[]{cnOrds.get(0).getPkPv()});
        return ords;
    }

    /**
     * ??????????????????
     *
     * @param addRisList
     * @param addLisList
     * @param loginUser
     * @param d
     * @param addCnOrder
     */
    private void addNewCnOrder(List<CnRisApply> addRisList,
                               List<CnLabApply> addLisList, User loginUser, Date d,
                               CnOrder addCnOrder) {
        String pkCnord = StringUtils.isBlank(addCnOrder.getPkCnord()) ? NHISUUID.getKeyId() : addCnOrder.getPkCnord();
        addCnOrder.setPkCnord(pkCnord);
        addCnOrder.setEuStatusOrd("0");
        addCnOrder.setTs(d);
        addCnOrder.setDateEnter(d);
        Double packSize = addCnOrder.getPackSize() == 0.00 ? 1.00 : addCnOrder.getPackSize();
        addCnOrder.setPackSize(packSize);
        addCnOrder.setOrdsnChk(addCnOrder.getOrdsn());
        if (FlAG_lONG.equals(addCnOrder.getEuAlways())) {
            addCnOrder.setQuanCg(null);
        }
        String codeOrdtype = addCnOrder.getCodeOrdtype();
        if (codeOrdtype.startsWith(Constants.RIS_SRVTYPE)) {
            String CodeApply = ApplicationUtils.getCode("0402");
            addCnOrder.setCodeApply(CodeApply);
            CnRisApply cnRisApply = new CnRisApply();
            String pkOrdris = NHISUUID.getKeyId();
            cnRisApply.setPkOrdris(pkOrdris);
            cnRisApply.setPkCnord(addCnOrder.getPkCnord());
            cnRisApply.setPkOrg(loginUser.getPkOrg());
            cnRisApply.setEuStatus("0");//??????????????????0 ??????
            cnRisApply.setFlagBed(Constants.FALSE);
            cnRisApply.setFlagPrint(Constants.FALSE);
            cnRisApply.setTs(d);
            cnRisApply.setDescBody(addCnOrder.getDescBody());
            cnRisApply.setDtRistype(addCnOrder.getDtType());
            cnRisApply.setNoteDise(addCnOrder.getNoteDise());
            cnRisApply.setPurpose(addCnOrder.getPurpose());
            addRisList.add(cnRisApply);
        } else if (codeOrdtype.startsWith(Constants.LIS_SRVTYPE)) {
            String CodeApply = ApplicationUtils.getCode("0401");
            addCnOrder.setCodeApply(CodeApply);
            CnLabApply cnLabApply = new CnLabApply();
            String pkOrdlis = NHISUUID.getKeyId();
            cnLabApply.setPkOrdlis(pkOrdlis);
            cnLabApply.setPkCnord(addCnOrder.getPkCnord());
            cnLabApply.setPkOrg(loginUser.getPkOrg());
            cnLabApply.setEuStatus("0");//??????????????????0??????
            cnLabApply.setFlagPrt(Constants.FALSE);
            cnLabApply.setTs(d);
            cnLabApply.setDtColtype(addCnOrder.getDtColltype());
            cnLabApply.setDtSamptype(addCnOrder.getDtSamptype());
            cnLabApply.setDtTubetype(addCnOrder.getDtContype());
            cnLabApply.setDescDiag(addCnOrder.getDescDiag());
            addLisList.add(cnLabApply);
        }
    }

    public List<Map<String, Object>> qryCnOrder(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        //0??????????????????????????????????????????????????????
        String sysparam = ApplicationUtils.getSysparam("CN0063", false, "????????????????????????CN0063???");
        map.put("isOperation", sysparam);
        if (map.get("pkPv").toString().length() > 32) {
            return CnOrderDao.qryCnPatiOrder(map.get("pkPv").toString().substring(0, 32));
        }
        return CnOrderDao.qryCnOrder(map);
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryOpCnOrder(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map.get("pkPv").toString().length() > 32) {
            return CnOrderDao.qryCnPatiOrder(map.get("pkPv").toString().substring(0, 32));
        }
        return CnOrderDao.qryCnOrder(map);
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @param user
     */

    public void signOrder(String param, IUser user) {
        OrderParam list = JsonUtil.readValue(param, OrderParam.class);
        List<CnOrder> cnOrds = list.getChangeOrdList();
        List<CnOrder> cnOrdsList = null;
        vaildSignCnOrdts(cnOrds);
        saveOrder(param, user);
        String propertyValue = ApplicationUtils.getPropertyValue("msg.processClass", "");
        if (cnOrds != null && cnOrds.size() > 0) {
            boolean flagCanl = "1".equals(list.getFlagCancleStop());
            try {
                if (flagCanl) {
                    cnOrdsList = getCnOrderListMsg(cnOrds);
                    canlSignOrder(cnOrds, (User) user);
                    //?????????????????????
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    Map<String, List<Map<String, Object>>> map = JsonUtil.readValue(param, Map.class);
                    paramMap.put("control", "CA");//????????????
                    paramMap.put("ordStatus", "2");//????????????
                    paramMap.put("ordlist", map.get("changeOrdList"));
                    PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
                } else {
                    cnOrdsList = getOrderMsg(cnOrds);
                    signOrder(cnOrds, (User) user);
                    //????????????--????????????????????????
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    Map<String, List<Map<String, Object>>> map = JsonUtil.readValue(param, Map.class);
                    paramMap.put("control", "NW");//????????????
                    paramMap.put("ordStatus", "2");//????????????
                    paramMap.put("ordlist", map.get("changeOrdList"));
                    PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
                }
            } catch (Exception e) {
                throw new RuntimeException("?????????????????????" + e);
            }
        }
        updateOrdFit(list.getFitOrdList());//??????????????????????????????????????????????????????????????????????????????
        cnPubService.recExpOrder(true, list.getCpOrdList(), (User) user); //?????????????????????
        boolean opManagerCease = "1".equals(list.getFlagOpManager());
        if (!opManagerCease) {
            cnPubService.sendMessage("?????????", cnOrds, false);
        }
        if (cnOrdsList != null && cnOrdsList.size() > 0) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("type", "RisLis");
            paramMap.put("lisList", cnOrdsList);
            paramMap.put("isAdd", "0");
            paramMap.put("Control", "NW");
            paramMap.put("param", param);
            PlatFormSendUtils.sendCnMedApplyMsg(paramMap);
        }
        list.setChangeOrdList(cnOrdsList);
        String outParam=JSON.toJSONString(list);
        cnNoticeService.saveCnNotice(outParam, false);
    }

    private void updateOrdFit(List<CnOrder> fitOrdList) {
        if (fitOrdList != null && fitOrdList.size() > 0) {
            DataBaseHelper.batchUpdate("update cn_order set flag_fit=:flagFit where ordsn=:ordsn and flag_erase='0' and desc_fit is not null ", fitOrdList);
        }
    }

    public void updateOrdFit(String param, IUser user) {
        List<CnOrder> fitCnordList = JsonUtil.readValue(param, new TypeReference<List<CnOrder>>() {
        });
        updateOrdFit(fitCnordList);
    }

    private void vaildSignCnOrdts(List<CnOrder> cnOrds) {
        if (cnOrds == null || cnOrds.size() <= 0) return;
        List<CnOrder> haveTs = cnPubService.haveTsOrd(cnOrds);
        if (haveTs != null && haveTs.size() > 0) {
            int canSignCount = cnPubService.qryCnordByts(haveTs);
            if (haveTs.size() != canSignCount) {
                throw new BusException("?????????????????????????????????????????????????????????!");
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param cnOrds
     * @param u
     */
    private void signOrder(List<CnOrder> cnOrds, User u) {
        if (cnOrds == null || cnOrds.size() <= 0) return;
        List<CnOrder> nsList = new ArrayList<CnOrder>();
        List<Integer> pkCnords = new ArrayList<Integer>();
        for (CnOrder order : cnOrds) {
            order.setPkEmpOrd(StringUtils.isBlank(order.getPkEmpOrd()) ? u.getPkEmp() : order.getPkEmpOrd());
            order.setNameEmpOrd(StringUtils.isBlank(order.getNameEmpOrd()) ? u.getNameEmp() : order.getNameEmpOrd());
            order.setEuStatusOrd("1");
            order.setFlagSign(Constants.TRUE);
            order.setDateSign(new Date());
            order.setTs(order.getDateSign());
            if ("0".equals(order.getFlagDoctor())) {
                nsList.add(order);
            }
            pkCnords.add(order.getOrdsn());
        }
        //checkFreqAndQuan(pkCnords);
        DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd, name_emp_ord=:nameEmpOrd,eu_status_ord = :euStatusOrd , flag_sign = :flagSign , date_sign = :dateSign ,ts=:ts   where ordsn = :ordsn and eu_status_ord ='0' and flag_doctor='1' ", cnOrds);
        cnPubService.caRecordByOrd(true, cnOrds);
        if (nsList.size() > 0) {
            DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = :euStatusOrd , flag_sign = :flagSign ,ts=:ts   where ordsn = :ordsn and eu_status_ord ='0' and flag_doctor='0'  and ordsn!=ordsn_parent ", nsList);
        }
        //ordProcService.syncSendOrds(cnOrds, u,"N");
    }

    private void checkFreqAndQuan(List<Integer> pkCnOrds) {
        if (pkCnOrds != null && pkCnOrds.size() > 0) {
            List<Map<String, Object>> list = CnOrderDao
                    .checkFreqAndQuan(pkCnOrds);
            String mesg = "";
            if (list != null && list.size() > 0) {
                for (Map<String, Object> attrMap : list) {
                    mesg += "???" + attrMap.get("nameOrd").toString() + ", ????????????" + attrMap.get("ordsn").toString() + "???" + "\n";
                }
                if (StringUtils.isNotEmpty(mesg)) {
                    mesg += "?????????????????????1??????????????????????????????";
                    throw new BusException(mesg);
                }
            }
        }
    }

    /**
     * ????????????
     *
     * @param cnOrds
     * @param u
     */
    private void canlSignOrder(List<CnOrder> cnOrds, User u) {
        if (cnOrds == null || cnOrds.size() <= 0) return;
        //??????????????????????????????
        String rtnStr = ordProcService.checkCancelSign(cnOrds, u);
        if (rtnStr != null && !rtnStr.equals("")) {
            throw new BusException(rtnStr);
        }
        Date d = new Date();
        for (CnOrder order : cnOrds) {
            order.setPkEmpOrd(null);
            order.setNameEmpOrd(null);
            order.setEuStatusOrd("0");
            order.setFlagSign(Constants.FALSE);
            order.setDateSign(null);
            order.setTs(d);
            //if("0".equals(order.getFlagDoctor())) nsList.add(order);
        }
        cnPubService.caRecordByOrd(true, cnOrds);
        DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd, name_emp_ord=:nameEmpOrd,eu_status_ord = :euStatusOrd , flag_sign = :flagSign , date_sign = :dateSign ,ts=:ts   where pk_cnord = :pkCnord and eu_status_ord ='1' and flag_doctor='1' ", cnOrds);
        DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = :euStatusOrd , flag_sign = :flagSign ,ts=:ts   where ordsn_parent=:ordsnParent and  eu_status_ord ='1' and flag_doctor='0'  and ordsn!=ordsn_parent ", cnOrds);
        //ordProcService.syncSendOrds(cnOrds, u,"D");
    }

    /**
     * ????????????
     * @param param
     * @param user
     */
    public void cancleOrder(String param, IUser user) {
        OrderParam list = JsonUtil.readValue(param, OrderParam.class);
        boolean opManagerCease = "1".equals(list.getFlagOpManager());
        boolean isLabor = "1".equals(list.getIsLabor());
        List<CnOrder> cnOrds = list.getChangeOrdList();
        saveOrder(param, user);
        //????????????????????????
        if (opManagerCease) { //??????
            cancleOpManagerOrder(cnOrds, user);
        } else if (isLabor) { //??????
            cnPubService.cancleOrderLab(cnOrds, user);
        } else {
            cnPubService.cancleOrder(cnOrds, user);
        }
        cnPubService.caRecordByOrd(false, cnOrds);
        //???????????????????????????????????????????????????
        cnNoticeService.saveCnNoticeVoid(param);
        //??????--???????????????????????????
        //if(!opManagerCease) cnPubService.sendMessage("????????????",cnOrds);
        //ordProcService.syncSendOrds(cnOrds, (User)user,"C");
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        PlatFormSendUtils.sendOperaOrderCancelMsg(map);
    }

    private void cancleOpManagerOrder(List<CnOrder> cnOrds, IUser user) {
        if (cnOrds == null || cnOrds.size() <= 0) return;
        List<String> pkNoCurDeptExeCn = new ArrayList<String>();
        List<String> pkCurDeptExeCn = new ArrayList<String>();
        Date d = new Date();
        User u = (User) user;
        for (CnOrder ord : cnOrds) {
            String deptExec = ord.getPkDeptExec();
            pkCurDeptExeCn.add(ord.getPkCnord());
            if (deptExec.equals(u.getPkDept())) { //??????????????????
//				pkCurDeptExeCn.add(ord.getPkCnord());
            } else {
                pkNoCurDeptExeCn.add(ord.getPkCnord());
            }
            ord.setEuStatusOrd("9");
            ord.setFlagErase("1");
            ord.setFlagEraseChk("1");
            ord.setDateErase(d);
            ord.setPkEmpErase(u.getPkEmp());
            ord.setNameEmpErase(u.getNameEmp());
            ord.setTs(ord.getDateErase());
            ord.setDateEraseChk(d);
            ord.setPkEmpEraseChk(u.getPkEmp());
            ord.setNameEraseChk(u.getNameEmp());
        }
        if (pkCurDeptExeCn.size() > 0) {
            Map<String, Object> pkOrdMap = new HashMap<String, Object>();
            pkOrdMap.put("pk_cnord", pkCurDeptExeCn);
            List<RefundVo> refundVos = DataBaseHelper.queryForList(" select  bl.pk_cgip , bl.quan  as quan_re  from bl_ip_dt bl inner" +
                    " join cn_order ord on bl.pk_cnord = ord.pk_cnord and ord.del_flag='0'" +
                    " and ord.flag_erase='0'  where  bl.del_flag = '0' and bl.pk_cnord in (:pk_cnord) " +
                    " and AMOUNT_PI>=0  and  not  exists ( select 1 from BL_IP_DT where pk_cnord in (:pk_cnord) and PK_CGIP_BACK = bl.PK_CGIP )", RefundVo.class, pkOrdMap);
            if (refundVos != null && refundVos.size() > 0) {
                cgDao.refundInBatch(refundVos);
            }
        }
        if (pkNoCurDeptExeCn.size() > 0) {
            Map<String, Object> pkOrdMap = new HashMap<String, Object>();
            pkOrdMap.put("pk_cnord", pkNoCurDeptExeCn);
            cnPubService.ceaseNoPdOrd(pkOrdMap);
        }
        DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord", cnOrds);
        DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,FLAG_ERASE_CHK=:flagEraseChk,date_erase_chk=:dateEraseChk,pk_emp_erase_chk=:pkEmpEraseChk,name_erase_chk=:nameEraseChk, ts=:ts  where pk_cnord =:pkCnord and flag_erase='0' ", cnOrds);
    }

    /**
     * ??????
     * @param param
     * @param user
     */
    public void stopOrder(String param, IUser user) {
        OrderParam list = JsonUtil.readValue(param, OrderParam.class);
        List<CnOrder> stopOrds = list.getChangeOrdList();
        saveOrder(param, user);
        cnPubService.caRecordByOrd(true, stopOrds);
        boolean canlStop = "1".equals(list.getFlagCancleStop());
        if (canlStop) {
            cancleStopOrder(stopOrds, user);
            //??????????????????????????????????????????2?????????
            cnNoticeService.cancelStop(stopOrds);
        } else {
            stopOrder(stopOrds, user);
            cnNoticeService.saveCnNotice(param, true);
        }
        cnPubService.sendMessage(canlStop ? "??????????????????" : "????????????", stopOrds, true);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("param", param);
        paramMap.put("stop", "1");
        PlatFormSendUtils.sendCnMedApplyMsg(paramMap);
    }

    private void stopOrder(List<CnOrder> stopOrds, IUser user) {
        if (stopOrds.size() <= 0) return;
        User u = (User) user;
        //String useLastNum = this.cnPubService.getDeptArguvalBool(u.getPkDept(), "CN0017","1"); //0????????????1?????????
        for (CnOrder order : stopOrds) {
            //order.setLastNum(getLastNum(order,useLastNum));
            order.setLastNum(order.getLastNum());
            order.setFlagStop(Constants.TRUE);
            order.setPkEmpStop(u.getPkEmp());
            order.setNameEmpStop(u.getNameEmp());
            order.setTs(new Date());
        }
        DataBaseHelper.batchUpdate("update cn_order set last_num=:lastNum, flag_stop=:flagStop, pk_emp_stop=:pkEmpStop, name_emp_stop=:nameEmpStop,date_stop=:dateStop,ts=:ts,pk_ord_exc=:pkOrdExc  where ordsn_parent=:ordsnParent and  eu_status_ord not in ('4','9') ", stopOrds);
        //ordProcService.syncSendOrds(stopOrds, u,"S");
    }

    private Long getLastNum(CnOrder order, String useLastNum) {
        if ("0".equals(useLastNum)) {
            return null;//???????????????????????????????????????
        }
        String dateStart = DateUtils.getDateStr(order.getDateStart());
        String dateStop = DateUtils.getDateStr(order.getDateStop());
        if (dateStart.equals(dateStop)) {
            return null;//?????????????????????????????????????????????????????????
        }
        if (order.getLastNum() != null) {
            return order.getLastNum();
        }
        List<Map<String, Object>> timeOccList = DataBaseHelper.queryForList("select tm.time_occ from bd_term_freq_time tm inner join bd_term_freq freq on tm.pk_freq=freq.pk_freq and tm.del_flag='0' where freq.code=? and freq.del_flag='0' order by tm.time_occ ", new Object[]{order.getCodeFreq()});
        Long lastNum = new Long(0);
        if (timeOccList != null && timeOccList.size() > 5) {
            lastNum = getUseCount(timeOccList, order.getDateStop());
        }
        lastNum = lastNum == 0 ? null : lastNum; //@todo add ??????????????????null
        return lastNum;
    }

    private Long getUseCount(List<Map<String, Object>> timeOccList, Date endDate) {
        Integer useCount = 0;//????????????
        String dateDtr = DateUtils.getDateStr(endDate);
        //????????????????????????????????????
        for (Map<String, Object> vo : timeOccList) {
            String timeOcc = (String) vo.get("timeOcc");
            if (!CommonUtils.isEmptyString(timeOcc)) {
                timeOcc = timeOcc.replaceAll(":", "");
                Date exceTime = null;
                try {
                    exceTime = DateUtils.getDefaultDateFormat().parse(dateDtr + timeOcc);
                } catch (ParseException e) {
                    throw new BusException("????????????????????????!");
                }
                if (exceTime.before(endDate)) {
                    useCount = useCount + 1;
                } else {
                    break;
                }
            }
        }
        return new Long(useCount);
    }

    private void cancleStopOrder(List<CnOrder> cstopOrds, IUser user) {
        if (cstopOrds.size() <= 0) return;
        User u = (User) user;
        //??????????????????????????????
        String rtnStr = ordProcService.checkCancelStop(cstopOrds, u);
        if (rtnStr != null && !rtnStr.equals("")) {
            throw new BusException(rtnStr);
        }
        DataBaseHelper.batchUpdate("update cn_order set last_num=null, flag_stop='0', pk_emp_stop=null, name_emp_stop=null,date_stop=null  where pk_cnord=:pkCnord and flag_stop='1' and flag_stop_chk='0'  ", cstopOrds);
        //ordProcService.syncSendOrds(cstopOrds, u,"O");
    }

    public List<Map<String, Object>> qryOrdExcludeDt(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String pk_ord = (String) map.get("pk_ord");
        if (StringUtils.isBlank(pk_ord)) {
            throw new BusException("????????????????????????pk_ord??????!");
        }
        if (map.get("eu_exctype") == null || StringUtils.isBlank(map.get("eu_exctype").toString())) {
            map.put("eu_exctype", "1");
        }
        List<Map<String, Object>> list = CnOrderDao.qryOrdExcludeDt(map);
        return list;
    }

    public List<Map<String, Object>> qryOrdSet(String param, IUser user) {
        User u = (User) user;
        String pk_psn = u.getPkEmp();
        if (StringUtils.isBlank(pk_psn)) {
            throw new BusException("???????????????????????????!");
        }
        String pk_dept = u.getPkDept();
        if (StringUtils.isBlank(pk_dept)) {
            throw new BusException("??????????????????????????????!");
        }
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String isContainsFather = (String) map.get("isContainsFather");
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("pk_psn", pk_psn);
        paraMap.put("pk_dept", pk_dept);
        paraMap.put("isContainsFather", isContainsFather);
        List<Map<String, Object>> list = CnOrderDao.qryOrdSet(paraMap);
        return list;
    }

    public List<Map<String, Object>> qryOrdSetDt(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String pk_ordset = (String) map.get("pkOrdSet");
        String pkDeptExec = (String) map.get("pkDeptExec");
        String pkHp = map.get("pkHp") == null ? "" : map.get("pkHp").toString();
        map.put("pkHp", pkHp);
        //if(StringUtils.isBlank(pk_ordset)) throw new BusException("????????????????????????????????????pk_ordset??????!");
        if (StringUtils.isBlank(pkDeptExec)) {
            throw new BusException("???????????????????????????????????????!");
        }
        //?????????????????????
        String pkDeptAlready = map.get("pkDeptAlready") == null ? "" : map.get("pkDeptAlready").toString();
        String pkExecDept = "'" + pkDeptExec + "'";
        if (StringUtils.isNotBlank(pkDeptAlready)) {
            pkExecDept += ",'" + pkDeptAlready + "'";
        }
        map.put("pkDeptExec", pkExecDept);
        List<Map<String, Object>> list = CnOrderDao.qryOrdSetDt(map);
        //2020-11-11_SD-bug[30621]?????????????????????????????????????????????-tjq
        if (CollectionUtils.isNotEmpty(list)) {
            for (Map<String, Object> ordSetMap : list) {
                if ("0".equals(MapUtils.getString(ordSetMap, "flagPd"))) {
                    int count = DataBaseHelper.queryForScalar("select count(1) from bd_ord_item orditem INNER JOIN bd_item item on orditem.pk_item=item.pk_item where item.flag_active='0' and orditem.pk_ord=?", Integer.class, MapUtils.getString(ordSetMap, "pkOrd"));
                    if (count > 0) {
                        ordSetMap.put("itemActive", "1");
                    }
                }
            }
        }
        return list;
    }

    public void saveOrdSet(String param, IUser user) {
        BdOrdSet bdOrdSet = JsonUtil.readValue(param, BdOrdSet.class);
        String rowStatus = bdOrdSet.getRowStatus();
        User u = (User) user;
        if ("insert".equals(rowStatus)) {
            insertBdOrdSet(bdOrdSet, u);
        } else if ("update".equals(rowStatus)) {
            int count = DataBaseHelper.queryForScalar("select count(1) from bd_ord_set where flag_ip='1' and eu_right='2' and pk_ordset!=? and  pk_emp=? and  (code = ? or name = ?)", Integer.class, bdOrdSet.getPkOrdset(), u.getPkEmp(), bdOrdSet.getCode(), bdOrdSet.getName());
            if (count > 0) {
                throw new BusException("???????????????????????????????????????????????????!");
            }
            DataBaseHelper.updateBeanByPk(bdOrdSet);
        } else if ("delete".equals(rowStatus)) {
            DataBaseHelper.deleteBeanByWhere(new BdOrdSetDt(), " pk_ordset='" + bdOrdSet.getPkOrdset() + "'");
            DataBaseHelper.deleteBeanByPk(bdOrdSet);
        }
    }

    private void insertBdOrdSet(BdOrdSet bdOrdSet, User u) {
        int count = DataBaseHelper.queryForScalar("select count(1) from bd_ord_set where flag_ip='1' and eu_right='2' and pk_emp=? and  (code = ? or name = ?)", Integer.class, u.getPkEmp(), bdOrdSet.getCode(), bdOrdSet.getName());
        if (count > 0) {
            throw new BusException("???????????????????????????????????????????????????!");
        }
        bdOrdSet.setEuRight("2");//??????
        bdOrdSet.setEuOrdtype(StringUtils.isBlank(bdOrdSet.getEuOrdtype()) ? "0" : bdOrdSet.getEuOrdtype()); //??????
        bdOrdSet.setPkEmp(u.getPkEmp());
        bdOrdSet.setPkOrg(u.getPkOrg());
        DataBaseHelper.insertBean(bdOrdSet);
    }

    public void saveOrdSetDt(String param, IUser user) {
        List<BdOrdSetDt> list = (List<BdOrdSetDt>) JsonUtil.readValue(param, new TypeReference<List<BdOrdSetDt>>() {
        });
        if (list.size() <= 0) {
            return;
        }
        List<BdOrdSetDt> saveList = new ArrayList<BdOrdSetDt>();
        List<BdOrdSetDt> updList = new ArrayList<BdOrdSetDt>();
        List<BdOrdSetDt> delList = new ArrayList<BdOrdSetDt>();
        for (BdOrdSetDt ordSetDt : list) {
            String rowStatus = ordSetDt.getRowStatus();
            if (Constants.RT_NEW.equals(rowStatus) || StringUtils.isBlank(ordSetDt.getPkOrdsetdt())) {
                insertBdOrdSetDt(ordSetDt);
                saveList.add(ordSetDt);
            } else if (Constants.RT_UPDATE.equals(rowStatus)) {
                updList.add(ordSetDt);
            } else if (Constants.RT_REMOVE.equals(rowStatus)) {
                delList.add(ordSetDt);
            }
        }
        if (saveList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdOrdSetDt.class), saveList);
        }
        if (updList.size() > 0) {
            DataBaseHelper.batchUpdate("update bd_ord_set_dt set flag_self=:flagSelf,parent_no=:parentNo,pk_ord=:pkOrd,flag_pd=:flagPd, sort_no=:sortNo, name_ord=:nameOrd,dosage=:dosage,pk_unit_dos=:pkUnitDos,quan=:quan,pk_unit=:pkUnit,code_freq=:codeFreq,code_supply=:codeSupply,pk_dept_exec=:pkDeptExec,note=:note,days=:days,pk_org_exec=:pkOrgExec where pk_ordsetdt=:pkOrdsetdt", updList);
        }
        if (delList.size() > 0) {
            DataBaseHelper.batchUpdate("delete from bd_ord_set_dt where pk_ordsetdt=:pkOrdsetdt ", delList);
        }
    }

    private void insertBdOrdSetDt(BdOrdSetDt ordSetDt) {
        ordSetDt.setPkOrdsetdt(NHISUUID.getKeyId());
        ordSetDt.setDelFlag(Constants.FALSE);
        ordSetDt.setTs(new Date());
    }

    public void saveModule(String param, IUser user) {
        OrdModuleParam mParam = JsonUtil.readValue(param, OrdModuleParam.class);
        List<BdOrdSetDt> bdOrdSetDt = mParam.getBdOrdSetDt();
        if (bdOrdSetDt.size() <= 0) {
            return;
        }
        String pkOrdSet = mParam.getPkOrdSet();
        if (StringUtils.isEmpty(pkOrdSet)) {
            BdOrdSet bdOrdSet = mParam.getBdOrdSet();
            insertBdOrdSet(bdOrdSet, (User) user);
            pkOrdSet = bdOrdSet.getPkOrdset();
        }
        Integer sortNo = 1;
        Integer maxSortNo = DataBaseHelper.queryForScalar(" select nvl(max(sort_no),0)  from bd_ord_set_dt where pk_ordset=? ", Integer.class, pkOrdSet);
        if (maxSortNo != 0) {
            sortNo = maxSortNo + 1;
        }
        for (BdOrdSetDt dt : bdOrdSetDt) {
            insertBdOrdSetDt(dt);
            dt.setPkOrdset(pkOrdSet);
            dt.setSortNo(sortNo);
            sortNo++;
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdOrdSetDt.class), bdOrdSetDt);
    }

    public void saveAl(String param, IUser user) {
        SaveAlParam alParam = JsonUtil.readValue(param, SaveAlParam.class);
        String pkPi = alParam.getPk_pi();
        if (StringUtils.isEmpty(pkPi)) return;
        List<PiAllergic> list = alParam.getPiAlList();
        //???????????????????????????
        DataBaseHelper.update("update pi_allergic set del_flag = '1' where pk_pi = ?", new Object[]{pkPi});
        // ???????????????????????????????????????
        if (CollectionUtils.isEmpty(alParam.getPiAlList())){
            return;
        }
        for (PiAllergic allergic : list) {
            allergic.setDelFlag("0");
            if (StringUtils.isEmpty(allergic.getPkPial())) { //??????
                allergic.setEuMcsrc("0");//????????????
                DataBaseHelper.insertBean(allergic);
            } else {
                DataBaseHelper.updateBeanByPk(allergic, false);
            }
        }
    }

    public Map<String, String> saveDiag(String param, IUser user) {
        SaveDiagParam diagParam = JsonUtil.readValue(param, SaveDiagParam.class);
        String pkPv = diagParam.getPk_pv();
        //????????????????????????????????????????????????????????????????????????????????????
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String sql = "select * from pv_diag where pk_pv=?";
        List<Map<String, Object>> paramList = DataBaseHelper.queryForList(sql, pkPv);
        paramMap.put("paramList", paramList);//????????????????????????
        paramMap.put("pkPv", pkPv);
        if (StringUtils.isEmpty(pkPv)) {
            throw new BusException("????????????????????????!");
        }
        Map<String, String> rtnMap = new HashMap<String, String>();
        String flagToCp = "0";
        String flagDiagDiv = "0";
        if ("1".equals(diagParam.getFlagUpPvmode())) {
            DataBaseHelper.update("update pv_encounter  set eu_pvmode='2' where pk_pv=? and  del_flag='0' and nvl(eu_pvmode,'0')='0' ", new Object[]{pkPv});
            return rtnMap;
        }
        List<PvDiag> list = diagParam.getPvDiagList();
        User u = (User) user;
        DataBaseHelper.update("update pv_diag set del_flag='1' where pk_pv=?", new Object[]{pkPv});
        for (PvDiag pvDiag : list) {
            pvDiag.setDelFlag("0");
            if (StringUtils.isEmpty(pvDiag.getPkPvdiag())) {
                DataBaseHelper.insertBean(pvDiag);
            } else {
                DataBaseHelper.updateBeanByPk(pvDiag, false);
            }
        }
        List<PvDiag> qryList = DataBaseHelper.queryForList("select * from pv_diag where del_flag='0' and pk_pv=? order by ts desc", PvDiag.class, new Object[]{pkPv});
        if (qryList.size() == 0) {
            rtnMap.put("flagToCp", flagToCp);
            rtnMap.put("flagDiagDiv", flagDiagDiv);
            return rtnMap;
        }
        CnDiag cnDiag = new CnDiag();
        cnDiag.setPkCndiag(NHISUUID.getKeyId());
        cnDiag.setEuPvtype("3"); //??????
        cnDiag.setPkPv(pkPv);
        cnDiag.setDateDiag(new Date());
        cnDiag.setPkEmpDiag(u.getPkEmp());
        cnDiag.setNameEmpDiag(u.getNameEmp());
        String desc_diags = "";
        List<CnDiagDt> cnDiagDtList = new ArrayList<CnDiagDt>();
        List<String> pkDiags = new ArrayList<String>();
        for (PvDiag pvDiag : qryList) {
            pkDiags.add(pvDiag.getPkDiag());
            if (StringUtils.isEmpty(desc_diags)) {
                desc_diags += pvDiag.getDescDiag();
            } else {
                desc_diags += "," + pvDiag.getDescDiag();
            }
            CnDiagDt cnDiagDt = new CnDiagDt();
            cnDiagDt.setPkOrg(u.getPkOrg());
            cnDiagDt.setPkCndiagdt(NHISUUID.getKeyId());
            cnDiagDt.setPkCndiag(cnDiag.getPkCndiag());
            cnDiagDt.setSortNo(pvDiag.getSortNo());
            cnDiagDt.setDtTreatway(pvDiag.getDtTreatway());
            cnDiagDt.setDtDiagtype(pvDiag.getDtDiagtype());
            cnDiagDt.setPkDiag(pvDiag.getPkDiag());
            cnDiagDt.setDescDiag(pvDiag.getDescDiag());
            cnDiagDt.setFlagMaj(pvDiag.getFlagMaj());
            cnDiagDt.setFlagSusp(pvDiag.getFlagSusp());
            cnDiagDt.setFlagInfect(pvDiag.getFlagContagion());
            cnDiagDt.setFlagFinally(pvDiag.getFlagFinally());
            cnDiagDt.setFlagCure(pvDiag.getFlagCure());
            cnDiagDt.setTs(new Date());
            cnDiagDtList.add(cnDiagDt);
        }
        cnDiag.setDescDiags(desc_diags);
        DataBaseHelper.insertBean(cnDiag);
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnDiagDt.class), cnDiagDtList);
        String IsUseCp = ApplicationUtils.getSysparam("CN0012", false);
        if ("1".equals(IsUseCp)) { //??????????????????
            Map<String, Object> cpTempMap = new HashMap<String, Object>();
            cpTempMap.put("pkOrgUse", u.getPkOrg());
            cpTempMap.put("pkDeptUse", u.getPkDept());
            cpTempMap.put("pkDiag", pkDiags);
            String cpTempSql = "select cptemp.*  from cp_temp_diag  cpdiag  " +
                    " inner join  cp_temp cptemp on cpdiag.pk_cptemp = cptemp.pk_cptemp and cptemp.del_flag='0' and cptemp.eu_status='2' " +
                    " inner join cp_temp_dept cpdept on cpdept.pk_cptemp = cptemp.pk_cptemp and cpdept.del_flag='0' and (cpdept.pk_org_use='~' or (cpdept.pk_org_use=:pkOrgUse and cpdept.pk_dept_use=:pkDeptUse)) " +
                    " where  cpdiag.del_flag='0' and cpdiag.pk_diag in(:pkDiag) ";
            List<Map<String, Object>> cpTempList = DataBaseHelper.queryForList(cpTempSql, cpTempMap);
            if (cpTempList != null && cpTempList.size() > 0) {
                flagToCp = "1";
            }
        }
        String flagQry = diagParam.getFlagQryHp();
        if ("1".equals(flagQry)) {
            int diagDiv = DataBaseHelper.queryForScalar("select count(1) from bd_hp_diagdiv div inner join bd_hp_diagdiv_itemcate cate on div.pk_totaldiv=cate.pk_totaldiv and cate.del_flag='0'  where div.pk_hp=? and div.pk_diag=?  and div.del_flag='0'", Integer.class, new Object[]{diagParam.getPkHp(), diagParam.getPkDiag()});
            if (diagDiv > 0) {
                flagDiagDiv = "1";
            }
        }
        rtnMap.put("flagToCp", flagToCp);
        rtnMap.put("flagDiagDiv", flagDiagDiv);
        //??????????????????
        PlatFormSendUtils.sendCnDiagMsg(paramMap);
        //??????????????????
        ordProcService.syncDiagInfo(qryList, u);
        return rtnMap;
    }

    public DiagParam qryDiag(String param, IUser user) {
        DiagParam qryRtn = new DiagParam();
        String pk_pv = (String) JsonUtil.readValue(param, Map.class).get("pk_pv");
        if (StringUtils.isEmpty(pk_pv)) {
            return qryRtn;
        }
        if (pk_pv.length() > 32) {
            qryRtn.setPvDiagList(qryPvOutDiag(pk_pv));
            return qryRtn;
        }
        qryRtn.setPvDiagList(CnOrderDao.qryPvDiag(pk_pv));
        qryRtn.setCnDiagList(CnOrderDao.qryCnDiag(pk_pv));
        return qryRtn;
    }

    private List<Map<String, Object>> qryPvOutDiag(String pk_pv) {
        String sql = "  select pvdiag.*   from pv_diag pvdiag  where pvdiag.del_flag='0' and pvdiag.pk_pv = ? and pvdiag.dt_diagtype=?  order by pvdiag.sort_no ";
        String[] arry = pk_pv.split(",");
        List<Map<String, Object>> pvOutDiag = DataBaseHelper.queryForList(sql, new Object[]{arry[1], arry[0]});
        return pvOutDiag;
    }

    public List<Map<String, Object>> qrySortIv(String param, IUser user) {
        String pk_pv = (String) JsonUtil.readValue(param, Map.class).get("pkPV");
        if (StringUtils.isEmpty(pk_pv)) {
            return new ArrayList<Map<String, Object>>();
        }
        List<Map<String, Object>> rtn = CnOrderDao.qrySortIv(pk_pv);
        return rtn;
    }

    public void saveSortIv(String param, IUser user) {
        List<CnOrder> list = JsonUtil.readValue(param, new TypeReference<List<CnOrder>>() {
        });
        if (list != null && list.size() > 0) {
            DataBaseHelper.batchUpdate("update cn_order set sort_iv=:sortIv where pk_cnord=:pkCnord ", list);
        }
    }

    public void internSubmitOrd(String param, IUser user) {
        List<CnOrder> list = JsonUtil.readValue(param, new TypeReference<List<CnOrder>>() {
        });
        if (list != null && list.size() > 0) {
            DataBaseHelper.batchUpdate("update cn_order set flag_itc='1' , pk_emp_ord=null, name_emp_ord=null  where pk_cnord=:pkCnord  ", list);
        }
    }

    //??????????????????
    public void updateFlag(String param, IUser user) {
        UpdateFlagParam updateFlagParam = JsonUtil.readValue(param, UpdateFlagParam.class);
        List<String> strList = updateFlagParam.getStrList();
        if (strList != null && strList.size() > 0) {
            DataBaseHelper.update("UPDATE cn_order SET FLAG_PRINT=? WHERE pk_cnord in(" +
                    CommonUtils.convertSetToSqlInPart(new HashSet<String>(strList), "pk_cnord") + ")", new Object[]{updateFlagParam.getFlag()});
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @param user
     */
    public void saveOrderPrt(String param, IUser user) {
        List<CnOrderPrint> list = (List<CnOrderPrint>) JsonUtil.readValue(param, new TypeReference<List<CnOrderPrint>>() {
        });
        if (null == list || list.size() < 1) {
            throw new BusException("?????????????????????????????????????????????");
        }
        for (CnOrderPrint ordPrt : list) {
            if (CommonUtils.isEmptyString(ordPrt.getPkOrdprint())) {
                DataBaseHelper.insertBean(ordPrt);
            } else {
                DataBaseHelper.updateBeanByPk(ordPrt, false);
            }
        }
    }

    /**
     * ??????????????????
     * 004004003030
     *
     * @param param
     * @param user
     * @return
     */
    public String queryDrugCode(String param, IUser user) {
        CnOrder cnOrder = JsonUtil.readValue(param, CnOrder.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("code", cnOrder.getCodeOrd());
        params.put("name", cnOrder.getNameOrd());
        BdPd bdpd = CnOrderDao.getDrugCode(params);
        if (bdpd == null) {
            return null;
        }
        String pkpd = bdpd.getPkPd();
        return pkpd;
    }

    /**
     * ??????????????????
     * 004004003031
     *
     * @param param
     * @param user
     * @return
     */
    public List<PvIp> queryPvIpTimes(String param, IUser user) {
        PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
        String pkPv = encounter.getPkPv();
        List<PvIp> ipList = CnOrderDao.getIpTimes(pkPv);
        return ipList;
    }

    /**
     * ??????????????????
     * 004004003032
     *
     * @param param
     * @param user
     * @return
     */
    public List<PvOp> queryPvOpTimes(String param, IUser user) {
        PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
        String pkPv = encounter.getPkPv();
        List<PvOp> opList = CnOrderDao.getOpTimes(pkPv);
        return opList;
    }

    /**
     * ??????????????????
     * 004004003033
     *
     * @param param
     * @param user
     * @return
     */
    public List<PiMaster> queryPiBirth(String param, IUser user) {
        PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
        String pkPi = encounter.getPkPi();
        List<PiMaster> maList = CnOrderDao.getMaBirth(pkPi);
        return maList;
    }

    /**
     * ??????????????????
     * 004004003034
     *
     * @param param
     * @param user
     * @return
     */
    public List<PvEncounter> queryPvParam(String param, IUser user) {
        PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
        String pkPv = encounter.getPkPv();
        List<PvEncounter> enList = CnOrderDao.getEncounters(pkPv);
        return enList;
    }

    /**
     * ??????????????????
     * 004004003035
     *
     * @param param
     * @param user
     * @return
     */
    public List<PiAllergic> queryAllergic(String param, IUser user) {
        PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
        String pkPi = encounter.getPkPi();
        List<PiAllergic> allerList = CnOrderDao.getAllergics(pkPi);
        return allerList;
    }

    /**
     * ??????????????????
     * 004004003036
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdTermDiag> queryDiags(String param, IUser user) {
        PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
        String pkPv = encounter.getPkPv();
        List<BdTermDiag> diagList = CnOrderDao.getDiags(pkPv);
        return diagList;
    }

    /**
     * ??????????????????
     * 004004003037
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdUnit> queryBdUnit(String param, IUser user) {
        BdUnit bdUnit = JsonUtil.readValue(param, BdUnit.class);
        String pkUnit = bdUnit.getPkUnit();
        List<BdUnit> unitList = CnOrderDao.getBdUnits(pkUnit);
        return unitList;
    }

    /**
     * ????????????????????????
     * 004004003038
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdSupply> queryBdSupplies(String param, IUser user) {
        BdSupply bdSupply = JsonUtil.readValue(param, BdSupply.class);
        String code = bdSupply.getCode();
        List<BdSupply> suList = CnOrderDao.getBdSupplies(code);
        return suList;
    }

    /**
     * ??????????????????
     * 004004003039
     *
     * @param param
     * @param user
     * @return
     */
    public String queryDept(String param, IUser user) {
        BdOuDept bdOuDept = JsonUtil.readValue(param, BdOuDept.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pkDept", bdOuDept.getPkDept());
        params.put("pkOrg", bdOuDept.getPkOrg());
        BdOuDept dept = CnOrderDao.getBdOuDept(params);
        String deptName = dept.getNameDept();
        return deptName;
    }

    /**
     * ??????????????????
     * 004004003040
     *
     * @param param
     * @param user
     * @return
     */
    public List<EmpVo> queryEmpInfo(String param, IUser user) {
        EmpVo employee = JsonUtil.readValue(param, EmpVo.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pkEmp", employee.getPkEmp());
        String pkEmp = params.get("pkEmp");
        params.put("dtEmpsrvtype", employee.getDtEmpsrvtype());
        String dtEmpsrv = params.get("dtEmpsrvtype");
        List<EmpVo> emp = new ArrayList<EmpVo>();
        if (dtEmpsrv.equals("00")) {
            emp = CnOrderDao.getEmpInfo(pkEmp);
        } else {
            emp = CnOrderDao.getDocterInfo(params);
        }
        return emp;
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryDrugInfo(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map.get("pkPv") == "" || map.get("pkPv") == null) {
            throw new BusException("??????????????????????????????");
        }
        List<Map<String, Object>> qryDrugInfo = CnOrderDao.qryDrugInfo(map);
        return CnOrderDao.qryDrugInfo(map);
    }

    /**
     * ????????????????????????????????????
     * 010008002001
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnRisApply> queryRisInfo(String param, IUser user) {
        CnOrder cnOrder = JsonUtil.readValue(param, CnOrder.class);
        List<CnRisApply> risInfo = new ArrayList<CnRisApply>();
        String pkPv = cnOrder.getPkPv();
        risInfo = CnOrderDao.getRisInfo(pkPv);
        return risInfo;
    }

    /**
     * ????????????????????????????????????
     * 010008002002
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnLabApply> queryLabInfo(String param, IUser user) {
        CnOrder cnOrder = JsonUtil.readValue(param, CnOrder.class);
        List<CnLabApply> LabInfo = new ArrayList<CnLabApply>();
        String pkPv = cnOrder.getPkPv();
        LabInfo = CnOrderDao.getLabInfo(pkPv);
        return LabInfo;
    }

    /**
     * ??????????????????
     * 010008002003
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnOrder> queryCnOrdInfo(String param, IUser user) {
        CnOrder cnOrder = JsonUtil.readValue(param, CnOrder.class);
        List<CnOrder> OrdInfo = new ArrayList<CnOrder>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pkCnord", cnOrder.getPkCnord());
        OrdInfo = CnOrderDao.getOrdInfo(params);
        return OrdInfo;
    }

    /**
     * ??????????????????
     * 004004003044
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnOpEmrRecord> queryBodyInfo(String param, IUser user) {
        CnOpEmrRecord cnOpEmrRecord = JsonUtil.readValue(param, CnOpEmrRecord.class);
        List<CnOpEmrRecord> OpEmrList = new ArrayList<CnOpEmrRecord>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pkPv", cnOpEmrRecord.getPkPv());
        OpEmrList = CnOrderDao.getBodyInfo(params);
        return OpEmrList;
    }

    /**
     * ??????????????????????????????
     * 004004003045
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryDiagICDTenInfo(String param, IUser user) { //BdTermDiag
        //CnOpEmrRecord cnOpEmrRecord = JsonUtil.readValue(param, CnOpEmrRecord.class);
        Map<String, String> map = JsonUtil.readValue(param, Map.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pkPv", map.get("pkPv"));
        return CnOrderDao.getDiagICDTenInfo(params);
    }

    /**
     * ???????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryDescFit(String param, IUser user) {
        Map<String, Object> params = JsonUtil.readValue(param, Map.class);
        if (params.get("type") != null && "1".equals(params.get("type").toString())) { //???????????????????????????????????????
            List<Map<String, Object>> rtn = CnOrderDao.qryDescFitOlny(params);
            return rtn;
        } else { //??????????????????
            List<Map<String, Object>> rtn = CnOrderDao.qryDescFit(params);
            return rtn;
        }
    }

    /**
     * ?????????????????????
     *
     * @param param
     * @param user
     */
    public void updateDescFit(String param, IUser user) {
        List<CnOrder> params = JsonUtil.readValue(param, new TypeReference<List<CnOrder>>() {
        });
        DataBaseHelper.batchUpdate("update cn_order set  flag_fit=:flagFit   where pk_cnord = :pkCnord ", params);
    }

    /**
     * ????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryOrdAnti(String param, IUser user) {
        IntegrateParam integrateParam = JsonUtil.readValue(param, IntegrateParam.class);
        List<CnOrder> ordList = integrateParam.getOrdList();
        Map<String, Object> unListParamMap = integrateParam.getUnListParamMap();
        //List<CnOrder> ordList = JsonUtil.readValue(param, new TypeReference<List<CnOrder>>(){});
        String pkPv = "";
        if (unListParamMap.containsKey("pkPv")) {
            pkPv = unListParamMap.get("pkPv").toString();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> pks = new ArrayList<String>();
        for (CnOrder ord : ordList) {
            String pkCnOrd = ord.getPkCnord();
            if (StringUtils.isNotBlank(pkCnOrd)) {
                pks.add(pkCnOrd);
            }
        }
        //??????????????????/????????????
        if (StringUtils.isBlank(pkPv)) {
            map.put("pkCnords", pks);
        } else {
            map.put("pkPv", pkPv);
        }
        List<Map<String, Object>> rtn = CnOrderDao.qryOrdAnti(map);
        return rtn;
    }

    /**
     * ????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public void updateOrdAnti(String param, IUser user) {
        List<CnOrder> params = JsonUtil.readValue(param, new TypeReference<List<CnOrder>>() {
        });
        updateOrdAnti(params, (User) user);
    }

    //?????????????????????????????????
    public void updateOrdAnti(List<CnOrder> updList, User user) {
        if (updList == null || updList.size() <= 0) {
            return;
        }
        List<String> pks = new ArrayList<String>();
        for (CnOrder ord : updList) {
            String pkCnOrd = ord.getPkCnord();
            if (StringUtils.isNotBlank(pkCnOrd)) {
                pks.add(pkCnOrd);
            }
        }
        if (pks == null || pks.size() <= 0) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pkCnords", pks);
        DataBaseHelper.update("delete from cn_ord_anti where pk_cnord in(:pkCnords)", map);
        saveOrdAnti(updList, user, false);
        DataBaseHelper.batchUpdate("update cn_order set flag_prev=:flagPrev, flag_thera=:flagThera  where pk_cnord =:pkCnord ", updList);
    }

    private List<CnOrdAnti> saveOrdAnti(List<CnOrder> ordList, User user, boolean isUpdateSave) {
        if (ordList == null || ordList.size() <= 0) {
            return null;
        }
        List<CnOrdAnti> cnOrdAntis = new ArrayList<CnOrdAnti>();
        Date ts = new Date();
        List<String> pks = new ArrayList<String>();
        for (CnOrder ord : ordList) {
            if (StringUtils.isNotBlank(ord.getPkOrdanti()) && ("1".equals(ord.getFlagPrev()) || "1".equals(ord.getFlagThera()))) {
                if (StringUtils.isNotBlank(ord.getPkCnord())) {
                    pks.add(ord.getPkCnord());
                }
                CnOrdAnti anti = new CnOrdAnti();
                try {
                    BeanUtils.copyProperties(anti, ord);
                } catch (IllegalAccessException e) {
                    throw new BusException(e.getMessage());
                } catch (InvocationTargetException e) {
                    throw new BusException(e.getMessage());
                }
                anti.setEuType(ord.getEuMonitorType());
                anti.setPkOrg(user.getPkOrg());
                anti.setTs(ts);
                cnOrdAntis.add(anti);
            }
        }
        if (isUpdateSave) {
            if (pks != null && pks.size() > 0) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("pkCnords", pks);
                DataBaseHelper.update("delete from cn_ord_anti where pk_cnord in(:pkCnords)", map);
            }
        }
        if (cnOrdAntis != null && cnOrdAntis.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrdAnti.class), cnOrdAntis);
        }
        return cnOrdAntis;
    }

    /**
     * @return void
     * @Description ????????????????????????
     * @auther wuqiang
     * @Date 2020-09-18
     * @Param [param, user]
     */
    public void cgMedicalOrd(String param, IUser user) {
        OrderParam list = JsonUtil.readValue(param, OrderParam.class);
        saveOrder(param, user);
        List<CnOrder> cnOrds = list.getChangeOrdList();
        boolean flagCg = "0".equals(list.getFlagCancleStop());
        if (flagCg) {
            cgMedicalOrder(cnOrds, (User) user);
        } else {
            canlCgMedicalOrder(cnOrds, (User) user);
        }
    }

    private void canlCgMedicalOrder(List<CnOrder> cnOrds, User user) {
        if (cnOrds == null || cnOrds.size() <= 0) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> pks = new ArrayList<String>();
        for (CnOrder ord : cnOrds) {
            pks.add(ord.getPkCnord());
        }
        map.put("pkCns", pks);
        List<Map<String, Object>> cgList = DataBaseHelper.queryForList("select pk_cnord,  pk_cgip,  quan from bl_ip_dt where pk_cnord in (:pkCns) and pk_cgip is not null and quan is not null and quan!=0  and pk_cnord is not null ", map);
        // ??????????????????
        List<RefundVo> refundVoList = new ArrayList<RefundVo>();
        List<CnOrder> cgCnOrds = new ArrayList<CnOrder>();
        BlPubReturnVo cgvo = null;
        if (CollectionUtils.isNotEmpty(cgList)) {
            for (Map<String, Object> cg : cgList) {
                RefundVo refundVo = new RefundVo();
                refundVo.setPkCgip(cg.get("pkCgip").toString());
                refundVo.setQuanRe(Double.parseDouble(cg.get("quan").toString()));
                refundVoList.add(refundVo);
                cgCnOrds.add(setCeaseCnVO(cg.get("pkCnord").toString(), user));
            }
            // ?????????????????????
            cgvo = cgDao.refundInBatch(refundVoList);
        }
        List<String> consultSqlList = new ArrayList<String>();//?????????????????????sql??????--???????????????????????????????????????????????????
        for (CnOrder exMap : cnOrds) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("dateCanc", new Date());
            paramMap.put("pkDept", user.getPkDept());
			/*paramMap.put("pkEmp", u.getPkEmp());
			paramMap.put("nameEmp", u.getNameEmp());*/
            paramMap.put("pkEmp", user.getPkEmp());
            paramMap.put("nameEmp", user.getNameEmp());
            paramMap.put("pkCgRtn", "");
            String pkCnord = exMap.getPkCnord();
            if (cgvo != null && cgvo.getBids() != null && cgvo.getBids().size() > 0) {
                for (BlIpDt dt : cgvo.getBids()) {
                    if (pkCnord.equals(dt.getPkOrdexdt())) {
                        paramMap.put("pkCgRtn", dt.getPkCgip());
                    }
                }
            }
            if ("0".equals(exMap.getFlagDurg())) {
                String sql = "update ex_order_occ set flag_canc='1',pk_cg_cancel=:pkCgRtn,date_canc = :dateCanc,eu_status='9',"
                        + "pk_dept_canc=:pkDept,pk_emp_canc=:pkEmp,name_emp_canc=:nameEmp where pk_cnord = :pkCnord and (pk_cg_cancel is null or pk_cg_cancel='')";
                paramMap.put("pkCnord", pkCnord);
                // ?????????????????????
                DataBaseHelper.update(sql, paramMap);
            }
            consultSqlList.add("update cn_consult_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//?????????????????????
            consultSqlList.add("update cn_trans_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//?????????????????????
            consultSqlList.add("update cn_ris_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//?????????????????????
            consultSqlList.add("update cn_lab_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//?????????????????????
            String upExSql = "update EX_ASSIST_OCC set FLAG_CANC='1', DATE_CANC=to_date('" + DateUtils.getDefaultDateFormat().format(new Date()) + "','YYYYMMDDHH24MISS') " +
                    ", PK_EMP_CANC='" + user.getPkEmp() + "',NAME_EMP_CANC='" + user.getNameEmp() + "'" +
                    "    where PK_ASSOCC in ( " +
                    "    select EAOD.PK_ASSOCC " +
                    "    from EX_ASSIST_OCC_DT EAOD " +
                    "    where EAOD.pk_cnord='" + pkCnord +
                    "')";
            consultSqlList.add(upExSql);//?????????????????????
        }
        //???????????????????????????????????? ?????????????????????0
        if (null != consultSqlList && consultSqlList.size() > 0) {
            DataBaseHelper.batchUpdate(consultSqlList.toArray(new String[0]));
        }
        consultSqlList = null;
        //????????????
        ceaseOrdStatus(cgCnOrds);
    }

    //????????????????????????
    private void cgMedicalOrder(List<CnOrder> cnOrds, User u) {
        if (cnOrds == null || cnOrds.size() <= 0) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> pkpvs = new ArrayList<String>();
        List<Integer> ordsnList = new ArrayList<Integer>();
        map.put("pkDeptNs", u.getPkDept());
        map.put("isMedOrdCg", true);
        for (CnOrder cnord : cnOrds) {
            if (!pkpvs.contains(cnord.getPkPv())) {
                pkpvs.add(cnord.getPkPv());
            }
            if (!ordsnList.contains(cnord.getOrdsn())) {
                ordsnList.add(cnord.getOrdsn());
            }
        }
        map.put("pkPvs", pkpvs);
        map.put("ordsnList", ordsnList);
        if (ordsnList.size() <= 0) {
            return;
        }
        List<CnOpSureParam> list = medicalDao.qrySureOrd(map);
        if (list == null || list.size() <= 0) {
            return;
        }
        List<BlPubParamVo> blPubParamVos = new ArrayList<BlPubParamVo>();
        List<CnOrder> cgCnOrds = new ArrayList<CnOrder>();
        for (CnOpSureParam cgVo : list) {
            setCgVO(cgVo, u, blPubParamVos);
            cgCnOrds.add(setCnVO(cgVo, u));
        }
        if (blPubParamVos.size() > 0) {
            for (BlPubParamVo blP : blPubParamVos) {
                blP.setEuBltype("4");//????????????:????????????
            }
            cgDao.chargeIpBatch(blPubParamVos, false);
        }
        updateOrdStatus(cgCnOrds);
    }

    private CnOrder setCnVO(CnOpSureParam cgVo, User u) {
        CnOrder cn = new CnOrder();
        cn.setPkEmpOrd(cgVo.getPkEmpInput());
        cn.setNameEmpOrd(cgVo.getNameEmpInput());
        cn.setEuStatusOrd("3");
        cn.setPkEmpChk(u.getPkEmp());
        cn.setNameEmpChk(u.getNameEmp());
        cn.setDateChk(new Date());
        cn.setTs(cn.getDateChk());
        cn.setDateLastEx(cn.getDateChk());
        cn.setPkCnord(cgVo.getPkCnord());
        return cn;
    }

    private CnOrder setCeaseCnVO(String pkCnord, User u) {
        CnOrder order = new CnOrder();
        order.setPkCnord(pkCnord);
        order.setEuStatusOrd("9");
        order.setFlagErase("1");
        order.setDateErase(new Date());
        order.setPkEmpErase(u.getPkEmp());
        order.setNameEmpErase(u.getNameEmp());
        order.setTs(order.getDateErase());
        return order;
    }

    private void setCgVO(CnOpSureParam cgVo, User u, List<BlPubParamVo> blPubParamVos) {
        String pkDeptExec = cgVo.getPkDeptEx();
        if (StringUtils.isBlank(pkDeptExec)) {
            throw new BusException(cgVo.getNamePd() + "????????????????????????!?????????!");
        }
        if (u.getPkDept().equals(pkDeptExec)) { //1???	????????????????????????
            buildCgVO(u, cgVo);
            blPubParamVos.add(cgVo);
        }
    }

    private void buildCgVO(User userOp, CnOpSureParam blPubParamVo) {
        String pkOrg = userOp.getPkOrg();
        String pkEmp = userOp.getPkEmp();
        String nameEmp = userOp.getNameEmp();
        blPubParamVo.setPkOrg(pkOrg);
        blPubParamVo.setEuPvType("3");// ??????
        blPubParamVo.setPkOrgApp(blPubParamVo.getPkOrg());// ????????????
        blPubParamVo.setFlagPv("0"); // ?????????????????? false ??????????????????????????????1 ????????????0
        blPubParamVo.setDateHap(new Date()); //??????????????????
        blPubParamVo.setPkDeptCg(userOp.getPkDept()); //????????????
        blPubParamVo.setPkEmpCg(pkEmp); //????????????
        blPubParamVo.setNameEmpCg(nameEmp); //??????????????????
        //blPubParamVo.setPkPv(); //?????????VO???
        //blPubParamVo.setPkPi(); //?????????VO???
        //blPubParamVo.setPkOrd(); //?????????VO???
        //blPubParamVo.setPkOrgEx();// ?????????????????????VO???
        //blPubParamVo.setPkDeptEx();// ???????????? ?????????VO???
        //blPubParamVo.setFlagPd(); //?????????VO???
        //blPubParamVo.setQuanCg();// ??????????????????
        //blPubParamVo.setPkPres();//???????????? ?????????VO???
        //blPubParamVo.setPkCnOrd();// ????????????????????? ?????????VO???
        // blPubParamVo.setPkDeptApp();// ???????????? ?????????VO???
        //blPubParamVo.setPkEmpApp();// ???????????? ?????????VO???
        //blPubParamVo.setNameEmpApp();// ?????????????????? ?????????VO???
        // blPubParamVo.setPkOrdexdt(); //pkOrdexdt ??????
        //blPubParamVo.setInfantNo(); //?????????????????????VO???
        //blPubParamVo.setPkDeptNsApp(); //??????????????????VO???
        blPubParamVo.setPkEmpApp(blPubParamVo.getPkEmpInput());
        blPubParamVo.setNameEmpApp(blPubParamVo.getNameEmpInput());
        blPubParamVo.setPkEmpEx(blPubParamVo.getPkEmpInput()); //????????????,???????????????
        blPubParamVo.setNameEmpEx(blPubParamVo.getNameEmpInput()); //??????????????????????????????
        Integer cnt = 1;
        //????????????????????????????????????
        if (!CommonUtils.isEmptyString(blPubParamVo.getCodeFreq())) {
            cnt = DataBaseHelper.queryForScalar("select cnt from bd_term_freq where code = ? and del_flag = '0'", Integer.class, blPubParamVo.getCodeFreq());
        }
        if (cnt == null) cnt = 1;
        //??????????????????????????????????????????????????????
        if ("1".equals(blPubParamVo.getFlagPd())) {
            // blPubParamVo.setPkUnitPd();  //????????????(pd???????????????pk_unit_pack) ???????????????(pack_size) ????????????VO???
            Integer day = (blPubParamVo.getDays() == null || blPubParamVo.getDays() == 0) ? 1 : blPubParamVo.getDays();
            double quanCg = MathUtils.mul(blPubParamVo.getQuan(), MathUtils.mul(cnt.doubleValue(), day.doubleValue()));
            blPubParamVo.setQuanCg(Math.ceil(quanCg));
            blPubParamVo.setBatchNo("~");
            blPubParamVo.setDateExpire(new Date());
            double price = blPubParamVo.getPrice() == null ? 0 : blPubParamVo.getPrice();
            Integer packSize = blPubParamVo.getPackSize() == null ? 0 : blPubParamVo.getPackSize();
            Double priceMin = MathUtils.div(price, packSize.doubleValue());
            blPubParamVo.setPrice(priceMin);
            blPubParamVo.setPriceCost(priceMin); //???????????? ,????????????????????????????????????????????? ??????????????????
            blPubParamVo.setPkItem(blPubParamVo.getPkOrd()); // ?????????????????????pkPd,?????????????????????????????????pk_ord????????????
            blPubParamVo.setPackSize(1);//??????????????????????????????
        } else {
            blPubParamVo.setQuanCg(MathUtils.mul(blPubParamVo.getQuan(), cnt.doubleValue()));
        }
    }

    private void updateOrdStatus(List<CnOrder> cnOrds) {
        if (cnOrds != null && cnOrds.size() > 0) {
            DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd,name_emp_ord=:nameEmpOrd, eu_status_ord=:euStatusOrd, pk_emp_chk=:pkEmpChk,name_emp_chk=:nameEmpChk,date_chk=:dateChk,date_last_ex=:dateLastEx where pk_cnord=:pkCnord and del_flag='0' ", cnOrds);
        }
    }

    private void ceaseOrdStatus(List<CnOrder> cnOrds) {
        if (cnOrds != null && cnOrds.size() > 0) {
            DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  where pk_cnord =:pkCnord ", cnOrds);
        }
    }

    //??????????????????????????????
    public List<Map<String, Object>> qryOrdStReagent(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = CnOrderDao.qryOrdStReagent(paramMap);
        return list;
    }

    //????????????????????????
    public List<Map<String, Object>> qryPdNoStore(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        return CnOrderDao.qryPdNoStore(paramMap);
    }

    //????????????????????????--1-?????????2-?????????3-??????
    public void dealPdNoStore(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });
        String euResult = (String) paramMap.get("euResult");
        //?????????????????????
        String jasonStr = JsonUtil.writeValueAsString(paramMap.get("pkPdapdtList"));
        List<String> pkPdapdtList = JSONArray.parseArray(jasonStr, String.class);
        //???????????????
        jasonStr = JsonUtil.writeValueAsString(paramMap.get("pkPdapList"));
        List<String> pkPdapList = JSONArray.parseArray(jasonStr, String.class);
        //??????????????????
        if (pkPdapdtList != null && pkPdapdtList.size() > 0) {
            String sqlStr = "update ex_pd_apply_detail set eu_result = ?  where pk_pdapdt =? and flag_stop='1'";
            if ("1".equals(euResult)) {
                sqlStr = "update ex_pd_apply_detail set eu_result = ?,flag_stop='0'  where pk_pdapdt =? and flag_stop='1'";
            }
            for (String pkPdapdt : pkPdapdtList) {
                Map<String, Object> pkPdapdtMap = JsonUtil.readValue(pkPdapdt, Map.class);
                DataBaseHelper.execute(sqlStr, new Object[]{euResult, pkPdapdtMap.get("pkPdapdt")});
            }
        }
        //???????????????:
        if ("2".equals(euResult) || "3".equals(euResult)) {
            if (pkPdapList != null && pkPdapList.size() > 0) {
                for (String pkPdap : pkPdapList) {
                    Map<String, Object> pkPdapdtMap = JsonUtil.readValue(pkPdap, Map.class);
                    DataBaseHelper.execute("update ex_pd_apply set flag_finish='1',eu_status='1'where pk_pdap=? "
                            + " and NOT EXISTS ( SELECT	1 FROM ex_pd_apply_detail dt WHERE	ex_pd_apply.pk_pdap = dt.pk_pdap AND dt.flag_finish = '0' AND ((dt.flag_stop = '1' AND dt.eu_result < '2') or dt.flag_stop='0') AND dt.flag_canc = '0') ", new Object[]{pkPdapdtMap.get("pkPdap")});
                }
            }
        }
    }


    /**
     * @param cnOrds
     * @return
     * @para
     */
    private List<CnOrder> getOrderMsg(List<CnOrder> cnOrds) {
        StringBuilder ordsns = new StringBuilder("(");
        for (CnOrder ordTemp : cnOrds) {
            ordsns.append("'" + ordTemp.getOrdsn() + "'");
            ordsns.append(",");
        }
        //ordsns.append("'')");
        ordsns.deleteCharAt(ordsns.length() - 1);
        ordsns.append(")");
        List<CnOrder> ordsForSend = DataBaseHelper.queryForList(
                "select * from cn_order  where ordsn in " + ordsns.toString() + " and eu_status_ord ='0' and flag_doctor='1' ", CnOrder.class);
        return ordsForSend;
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param cnOrders
     * @return
     */
    public List<CnOrder> getCnOrderListMsg(List<CnOrder> cnOrders) {
        List<CnOrder> qryOrderMsg = CnOrderDao.qryOrderMsg(cnOrders);
        return qryOrderMsg;
    }

    //???????????????????????????
    public List<Map<String, Object>> qryPdMens(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = CnOrderDao.qryPdMens(paramMap);
        return list;
    }


    /**
     * ?????????????????????????????????????????????????????????
     * ????????????004004003064
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryOrderInfo(String param, IUser user) {        
        CnQryOrderParamVo qryParam = JsonUtil.readValue(param, CnQryOrderParamVo.class);
        
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        
        List<String> lstPkOrds = qryParam.getPkPdList();
        Set<String> setPkOrds = new HashSet<String>();
        for (String pkOrd : lstPkOrds) {
            setPkOrds.add(pkOrd);
        }
        //????????????map??????
		Map<String, Object> paramMap = qryParam.getParamMap();
        if(paramMap == null)
        {
	        if (setPkOrds.size() > 0) {
	            StringBuilder sb = new StringBuilder();
	            sb.append("SELECT bp.pk_pd as pkpd, bp.flag_stop as flagstop FROM BD_PD bp ");
	            sb.append(" where (bp.pk_pd IN ( ");
	            sb.append(CommonUtils.convertSetToSqlInPart(setPkOrds, "bp.pk_pd"));
	            sb.append(")) ");
	            String sql = sb.toString();
	            retList = DataBaseHelper.queryForList(sql, new Object[]{});
	        }
        }
        else
        {
        	paramMap.put("pkPds", new ArrayList<String>(setPkOrds));//Set???List
        	retList = CnOrderDao.qryOrdInfoFromPd(paramMap);
        }
        
        return retList;
    }

    /**
     * ????????????????????????
     * CnOrderService.findOrdList
     * 004004003052
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnOrderInputVO> findOrdList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        //????????????
        if (paramMap.get("flagSpec") == null) {
            throw new BusException("??????????????????????????????????????????");
        }
        if (paramMap.get("pkOrg") == null) {
            throw new BusException("??????????????????????????????????????????");
        }
        if (paramMap.get("pkOrgarea") == null) {
            throw new BusException("?????????????????????????????????????????????");
        }
        if (paramMap.get("pkCurDept") == null || paramMap.get("pkDept") == null) {
            throw new BusException("?????????????????????????????????????????????");
        }
        if (paramMap.get("pkDeptNs") == null) {
            throw new BusException("?????????????????????????????????????????????????????????");
        }
        if (paramMap.get("pkEmp") == null) {
            throw new BusException("?????????????????????????????????????????????");
        }
        if (paramMap.get("pkDiag") == null) {
            throw new BusException("???????????????????????????????????????????????????");
        }
//		if(paramMap.get("euSex")==null){
//			throw new BusException("?????????????????????????????????????????????");
//		}
//		if(paramMap.get("age")==null){
//			throw new BusException("?????????????????????????????????????????????");
//		}
//		if(paramMap.get("pyCode")==null && paramMap.get("name")==null){
//			throw new BusException("???????????????????????????????????????????????????");
//		}
//		if(paramMap.get("name")==null){
//			throw new BusException("?????????????????????????????????????????????");
//		}
        String dbType = MultiDataSource.getCurDbType();
        paramMap.put("dbType", dbType);
        int pageIndex = 1;
        int pageSize = 20;
        try {
            if (paramMap.containsKey("pageIndex")) {
                pageIndex = Integer.parseInt((String) paramMap.get("pageIndex"));
            }
            if (paramMap.containsKey("pageSize")) {
                pageSize = Integer.parseInt((String) paramMap.get("pageSize"));
            }
        } catch (NumberFormatException e) {
        }
        if (pageIndex == 0) {
            pageIndex = 1;
        }
        MyBatisPage.startPage(pageIndex - 1, pageSize);
        List<CnOrderInputVO> list = null;
        //??????????????????
        if ("true".equals(ApplicationUtils.getPropertyValue("cn.order.enable", ""))) {
            //???????????????????????????service?????????????????????cn.properties????????????
            String realizationClass = ApplicationUtils.getPropertyValue("cn.order.processClass", "");
            AbstractCnOrderFactory factory = ServiceLocator.getInstance().getBean(realizationClass, AbstractCnOrderFactory.class);
            list = factory.findOrdBaseList(paramMap);
        } else {
            list = CnOrderDao.findOrdBaseList(paramMap);
        }
        for (CnOrderInputVO vo : list) {
            try {
                BigDecimal b = new BigDecimal(vo.getPrice());
                vo.setPrice(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            } catch (Exception e) {
            }
        }
        return list;
    }


    /**
     * ????????????????????????
     * 004004003053
     * CnOrderService.findOrdListCache
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnOrderInputVO> findOrdListCache(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        int pageIndex = 1;
        int pageSize = 20;
        try {
            if (paramMap.containsKey("pageIndex")) {
                pageIndex = Integer.parseInt((String) paramMap.get("pageIndex"));
            }
            if (paramMap.containsKey("pageSize")) {
                pageSize = Integer.parseInt((String) paramMap.get("pageSize"));
            }
        } catch (NumberFormatException e) {
        }
        if (pageIndex == 0) {
            pageIndex = 1;
        }
        List<CnOrderInputVO> unPdList = (List<CnOrderInputVO>) RedisUtils.getCacheObj("CnOrderService:findOrdListCache:unPdList");
        if (unPdList == null || unPdList.size() < 1) {
            unPdList = CnOrderDao.findUnPdList(paramMap);
            RedisUtils.setCacheObj("CnOrderService:findOrdListCache:unPdList", unPdList, 360);
        }
        List<CnOrderInputVO> pdList = (List<CnOrderInputVO>) RedisUtils.getCacheObj("CnOrderService:findOrdListCache:pdList");
        if (pdList == null || pdList.size() < 1) {
            pdList = CnOrderDao.findPdList(paramMap);
            RedisUtils.setCacheObj("CnOrderService:findOrdListCache:pdList", pdList, 360);
        }
        for (CnOrderInputVO pd : pdList) {
            paramMap.put("pkPd", pd.getPkPd());
            List<Double> quanMin = CnOrderDao.findQuanMin(paramMap);
            if (quanMin != null && quanMin.size() > 0) {
                if (pd.getPackSize() > 0) {
                    pd.setQuanPack(quanMin.get(0) / pd.getPackSize());
                }
            }
        }
        unPdList.addAll(pdList);
        return unPdList;
    }

    /**
     * ????????????????????????flagMens
     * 004004003054
     * CnOrderService.getFlagMens
     *
     * @param param
     * @param user
     * @return
     */
    public String findFlagMens(String param, IUser user) {
        String result = "0";
        String pkPd = JsonUtil.getFieldValue(param, "pkPd");
        List<String> flagMens = CnOrderDao.getFlagMens(pkPd);
        if (flagMens != null && flagMens.size() > 0) {
            result = flagMens.get(0);
        }
        return result;
    }

    /**
     * ??????????????????
     * CnOrderService.findBdPdAtt
     * 004004003055
     *
     * @param param
     * @param user
     * @return
     */
    public String findBdPdAtt(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap.get("pkPd") == null) {
            throw new BusException("???????????????");
        }
        if (paramMap.get("codeAtt") == null) {
            throw new BusException("???????????????");
        }
        String result = "0";
        String pkPd = JsonUtil.getFieldValue(param, "pkPd");
        List<String> valAtt = CnOrderDao.findBdPdAtt(paramMap);
        if (valAtt != null && valAtt.size() > 0) {
            result = valAtt.get(0);
        }
        return result;
    }

    public List<Map<String, Object>> findBdPdAttByPk(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap.get("pkPv") == null) {
            throw new BusException("?????????????????????");
        }
        String startDate = DateUtils.getDateStr(new Date()) + "000000";
        String endDate = DateUtils.getDateStr(new Date()) + "235959";
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        //???????????????????????????
        List<Map<String, Object>> list = CnOrderDao.findBdPdAttByPk(paramMap);
        List<String> pkList = new ArrayList<String>();
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> item : list) {
            if (item.get("valAtt") != null && "1".equals(item.get("valAtt").toString()) && !pkList.contains(item.get("pkOrd").toString())) {
                pkList.add(item.get("pkOrd").toString());
                retList.add(item);
            }
        }
        return retList;
    }

    /**
     * ????????????????????????????????????
     * 004004003057
     * CnOrderService.findFlagKy
     *
     * @param param
     * @param user
     * @return
     */
    public String findFlagKy(String param, IUser user) {
        String result = "0";
        String pkOrd = JsonUtil.getFieldValue(param, "pkOrd");
        List<String> flagMens = CnOrderDao.getFlagKy(pkOrd);
        if (flagMens != null && flagMens.size() > 0) {
            result = flagMens.get(0);
        }
        return result;
    }

    /**
     * ??????????????????????????? 1?????????????????????????????????0?????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public String qryPdLimit(String param, IUser user) {
        String result = "0";
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap.get("pkPd") == null) {
            throw new BusException("???????????????");
        }
        List<Map<String, Object>> ord = CnOrderDao.qryPdLimit(paramMap);
        if (ord != null && ord.size() > 0) { //????????????????????????????????????
            Map<String, Object> limit = new HashMap<String, Object>();
            for (int i = 0; i < ord.size(); i++) {
                limit = ord.get(i);
                if (limit.get("euCtrltype") != null) {
                    if ("0".equals(limit.get("euCtrltype").toString())
                            && (paramMap.get("pkEmp") != null && limit.get("pkEmp") != null
                            && limit.get("pkEmp").toString().equals(paramMap.get("pkEmp").toString()))) {
                        result = "1";
                        break;
                    }
                    if ("1".equals(limit.get("euCtrltype").toString())
                            && (paramMap.get("pkDept") != null && limit.get("pkDept") != null
                            && limit.get("pkDept").toString().equals(paramMap.get("pkDept").toString()))) {
                        result = "1";
                        break;
                    }
                    if ("2".equals(limit.get("euCtrltype").toString())
                            && (paramMap.get("pkDiag") != null && limit.get("pkDiag") != null
                            && limit.get("pkDiag").toString().equals(paramMap.get("pkDiag").toString()))) {
                        result = "1";
                        break;
                    }
                }
            }
        } else {
            result = "1";
        }
        return result;
    }

    /**
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Description ?????????????????????????????????????????????????????????
     * @auther wuqiang
     * @Date 2020-12-31
     * @Param [param, user]
     */
    public List<Map<String, Object>> qryOrdInfo(String param, IUser user) {
        List<String> pkOrds = JsonUtil.readValue(param, List.class);
        return CnOrderDao.qryOrdInfo(pkOrds);
    }

    /*****************************?????????????????????????????????  start ******************************/
    public void saveOrderOther(String param, IUser user) {

        CnOtherVo par = JsonUtil.readValue(param, CnOtherVo.class);
        if(par==null)
            return;

        List<CnOrder> cnList=par.getCnList();
        List<CnOrder> cnListBack=par.getCnListBack();

        if(cnList!=null && cnList.size()>0){
            DataBaseHelper.batchUpdate("update cn_order set EU_INJURY=:euInjury  where pk_cnord =:pkCnord ", cnList);
        }

        if(cnListBack!=null && cnListBack.size()>0){
            DataBaseHelper.batchUpdate("update cn_order_b set EU_INJURY=:euInjury  where pk_cnord =:pkCnord ", cnListBack);
        }
    }
    /*****************************?????????????????????????????????  end  ******************************/

    /*************************************???????????? ????????????????????? start**************************************/
    private void setOrdPres(List<CnOrder> list){
        if(list==null || list.size()==0) return;
        String sql="Select pdatt.pk_pd,pdatt.val_att flag_plan \n" +
                "From bd_pd_att pdatt \n" +
                "Left Join bd_pd_att_define def On pdatt.pk_pdattdef=def.pk_pdattdef \n" +
                "where pdatt.pk_pd=? and def.code_att='0523' ";
        List<Map<String,Object>> attrs=new ArrayList<>();
        for(CnOrder vo :list){
            if(!"1".equals(vo.getFlagDurg())) continue;

            attrs=DataBaseHelper.queryForList(sql,new Object[]{vo.getPkOrd()});
            if(attrs.size()>0 && "1".equals(MapUtils.getString(attrs.get(0),"flagPlan"))){
                    vo.setFlagPlan("1");//????????????
                    vo.setFlagBase("1");//???????????????
            }
            attrs=new ArrayList<>();
        }
    }

    //?????????????????????????????????????????????
    private boolean checkPvInfo(String pkPv){
        boolean ok=false;//??????????????????
        String day=ApplicationUtils.getSysparam("CN0107", false);
        String sql="Select PV.* From pv_encounter pv \n" +
                "Inner Join bd_dictattr dict On pv.pk_dept=dict.pk_dict \n" +
                "Where  pv.pk_pv=? And dict.code_attr='0605' And dict.val_attr='1'";
        PvEncounter pvEncounter=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{pkPv});

        if(StringUtils.isNotEmpty(day) && pvEncounter!=null){
            int dayIp= DateUtils.getDateSpace(pvEncounter.getDateBegin(), new Date());
            int dayI= Integer.parseInt(day);
            //??????
            if(dayI>0 && dayI<dayIp) ok=true;
        }
        return ok;
    }
    /*************************************???????????? ????????????????????? end**************************************/


}