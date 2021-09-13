package com.zebone.nhis.cn.ipdw.service;

import com.zebone.nhis.cn.ipdw.dao.CnOrderMapper;
import com.zebone.nhis.cn.ipdw.dao.CnReqMapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.ApplyCanlParam;
import com.zebone.nhis.cn.ipdw.vo.CnLabApplyVo;
import com.zebone.nhis.cn.ipdw.vo.CnRisApplyVo;
import com.zebone.nhis.cn.ipdw.vo.OrdCaVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.cn.pub.service.OrderPubService;
import com.zebone.nhis.common.module.cn.ipdw.*;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTemplateVo;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class CnReqService {
    @Autowired
    private CnReqMapper cnReqDao;
    @Autowired
    private BdSnService bdSnService;
    @Autowired
    private CnPubService cnPubService;
    @Autowired
    private OrderPubService orderPubService;
    @Autowired
    private CnIpOrdProcService ordProcService;
    // 用来控制手动事物
    @Resource(name = "transactionManager")
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private CnOrderMapper cnOrderDao;

    @Autowired
    private CnNoticeService cnNoticeService;

    /**
     * 查询已开立的检查项
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryCnOrderRisByPv(String param, IUser user) {
        String pk_pv = checkPkpv(param);
        List<Map<String, Object>> list = cnReqDao.queryCnOrderRisByPv(pk_pv);
//		//获取当前用户图片签名信息
//		Map<String,Object> qryMap = new HashMap<>(16);
//		qryMap.put("pkEmp", UserContext.getUser().getPkEmp());
//		OrdCaVo retVo = cnReqDao.qryImgCaByPkEmp(qryMap);
//		if(retVo!=null && retVo.getImgSign()!=null &&
//				list!=null && list.size()>0){
//			for(Map<String,Object> mapInfo : list){
//				//添加图片信息
//				mapInfo.put("imgSign", retVo.getImgSign());
//			}
//		}
        return list;
    }

    /**
     * 根据pkpv查询已开的检验项目
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryCnOrderLisByPv(String param, IUser user) {
        String pk_pv = checkPkpv(param);
        List<Map<String, Object>> list = cnReqDao.queryCnOrderLisByPv(pk_pv);
        return list;
    }

    private String checkPkpv(String param) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String pk_pv = (String) map.get("pk_pv");
        if (StringUtils.isEmpty(pk_pv)) throw new BusException("前台传就诊主键Pkpv为空！");
        return pk_pv;
    }

    /**
     * 查询全院检查项目
     *
     * @param param
     * @param user
     * @return
     */
    @Cacheable(value = "his:CnReqService:queryRisUhSrvtype#60*5", key = "#param")
    public List<Map<String, Object>> queryRisUhSrvtype(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = cnReqDao.queryRisUhSrvtype(paramMap);
        return list;
    }

    /**
     * 清除检查/检验缓存
     * CnReqService.clearCacheRis
     * 004004006017*
     *
     * @param param
     * @param user
     */
//	@CacheEvict(value = "his:CnReqService:*",allEntries = true)
    public void clearCacheRis(String param, IUser user) {
        Set<String> keys = RedisUtils.getRedisTemplate().keys("*");
        for (String key : keys) {
            if (StringUtils.isNotEmpty(key)) {
                if (key.contains("CnReqService")) {
                    RedisUtils.getRedisTemplate().delete(key);
                }
            }
        }
    }

    /**
     * 查询全院检验项目
     *
     * @param param
     * @param user
     * @return
     */
    @Cacheable(value = "his:CnReqService:queryLisUhSrvtype#60*5", key = "#param")
    public List<Map<String, Object>> queryLisUhSrvtype(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = cnReqDao.queryLisUhSrvType(paramMap);
        return list;
    }

    public List<Map<String, Object>> queryItemBySrv(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String pk_ord = (String) map.get("pk_ord");
        if (StringUtils.isEmpty(pk_ord)) return new ArrayList<Map<String, Object>>();
        String pk_org = ((User) user).getPkOrg();
        List<Map<String, Object>> list = cnReqDao.queryItemBySrv(pk_org, pk_ord);
        return list;
    }

    public List<Map<String, Object>> queryRisBdUhSrvPsn(String param, IUser user) {
        User u = (User) user;
        List<Map<String, Object>> list = cnReqDao.queryRisBdUhSrvPsn(u.getPkOrg(), u.getPkEmp());
        return list;
    }

    public List<Map<String, Object>> queryLisBdUhSrvPsn(String param, IUser user) {
        User u = (User) user;
        List<Map<String, Object>> list = cnReqDao.queryLisBdUhSrvPsn(u.getPkOrg(), u.getPkEmp());
        return list;
    }

    /**
     * 查询个人检查项目
     *
     * @param param
     * @param user
     * @return
     */
//	@Cacheable(value = "com:zebone:nhis:cn:ipdw:service:CnReqService:qryRisLisTopUseOrd#60*3",key = "#user.id")
    public List<Map<String, Object>> qryRisLisTopUseOrd(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = cnReqDao.qryRisLisTopUseOrd(paramMap);
        return list;
    }

    /**
     * 签署检查申请
     *
     * @param param
     * @param user
     */
    public void signRisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        List<CnRisApplyVo> saveRisList = (List<CnRisApplyVo>) JsonUtil.readValue(param, new TypeReference<List<CnRisApplyVo>>() {
        });
        if (saveRisList.size() <= 0) return;
        String pkPv = saveRisList.get(0).getPkPv();
        List<CnOrder> signCnOrderList = saveRis(user, saveRisList);
        List<CnOrder> qryOrderMsg = null;
        try {
            //获取更新前的数据
            qryOrderMsg = cnOrderDao.qryOrderMsg(signCnOrderList);
        } catch (Exception e) {
            // TODO: handle exception
        }
        signApply(signCnOrderList, (User) user);
        cnPubService.updateDateStart(signCnOrderList);
        //医嘱同步---注释掉，2020.05.20
        //sysSendOrds(signCnOrderList,(User)user,"N");
        //ordProcService.syncSendOrds(signCnOrderList, (User)user, "N","PACS");
        //发送检查申请单至平台
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPv", pkPv);
        paramMap.put("type", "ris");
        paramMap.put("risList", signCnOrderList);
        paramMap.put("isAdd", "0");
        paramMap.put("Control", "NW");
        //发送医嘱信息到平台（现已保存到sysesbmsg表）
        PlatFormSendUtils.sendCnMedApplyMsg(paramMap);
        paramMap = null;
        for (int i = 0; i < signCnOrderList.size(); i++) {
            signCnOrderList.get(i).setPkPv(pkPv);
            signCnOrderList.get(i).setNameEmpInput(((User) user).getNameEmp());
        }
        cnNoticeService.saveCnNotice(signCnOrderList);
    }

    /**
     * 签署检验申请
     *
     * @param param
     * @param user
     */
    public void signLisApplyList(String param, IUser user) {
        List<CnLabApplyVo> saveLisList = (List<CnLabApplyVo>) JsonUtil.readValue(param, new TypeReference<List<CnLabApplyVo>>() {
        });
        if (saveLisList.size() <= 0) return;
        String pkPv = saveLisList.get(0).getPkPv();
        // 关闭事务自动提交
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        List<CnOrder> signCnOrderList = new ArrayList<CnOrder>();
        List<CnOrder> qryOrderMsg = null;
        try {
            signCnOrderList = saveLis(user, saveLisList);
            //获取取消签署医嘱信息
            qryOrderMsg = cnOrderDao.qryOrderMsg(signCnOrderList);
            signApply(signCnOrderList, (User) user);
            cnPubService.updateDateStart(signCnOrderList);
            //医嘱同步---注释掉，2020.05.20
            //ordProcService.syncSendOrds(signCnOrderList,(User)user,"N","LIS");
            platformTransactionManager.commit(status); // 提交事务
        } catch (Exception e) {
            platformTransactionManager.rollback(status); // 添加失败 回滚事务；
            e.printStackTrace();
            throw new RuntimeException("签署检验信息信息失败：" + e);
        }
        //发送检验申请至平台
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPv", pkPv);
        paramMap.put("type", "lis");
        paramMap.put("lisList", signCnOrderList);
        paramMap.put("isAdd", "0");
        paramMap.put("Control", "NW");
        //发送医嘱信息到平台
        PlatFormSendUtils.sendCnMedApplyMsg(paramMap);
        paramMap = null;
        //保存临床提醒消息
        for (int i = 0; i < signCnOrderList.size(); i++) {
            signCnOrderList.get(i).setPkPv(pkPv);
            signCnOrderList.get(i).setNameEmpInput(((User) user).getNameEmp());
        }
        cnNoticeService.saveCnNotice(signCnOrderList);
    }

    public void saveRisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        List<CnRisApplyVo> saveRisList = (List<CnRisApplyVo>) JsonUtil.readValue(param, new TypeReference<List<CnRisApplyVo>>() {
        });
        if (saveRisList.size() <= 0) return;
        //科研医嘱标记
//			List<String> pkOrds=new ArrayList<String>();
//			for (CnRisApplyVo vo: saveRisList) {
//				if(vo.getPkOrd()!=null && !pkOrds.contains(vo.getPkOrd())){
//					pkOrds.add(vo.getPkOrd());
//				}
//			}
//			List<Map<String,Object>> retMap = orderPubService.qryFlagKy(pkOrds,user);
//			for (CnRisApplyVo vo: saveRisList) {
//				for (Map<String,Object> item:retMap) {
//					if(item.get("pkOrd") !=null){
//						if(vo.getPkOrd().equals(item.get("pkOrd").toString())) vo.setEuOrdtype(item.get("euOrdtype").toString());
//					}
//				}
//			}
        saveRis(user, saveRisList);
    }

    public void saveLisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        List<CnLabApplyVo> saveLisList = (List<CnLabApplyVo>) JsonUtil.readValue(param, new TypeReference<List<CnLabApplyVo>>() {
        });
        if (saveLisList.size() <= 0) return;
        saveLis(user, saveLisList);
    }

    private void signApply(List<CnOrder> signCnOrderList, User u) {
        if (signCnOrderList.size() <= 0) return;
        for (CnOrder order : signCnOrderList) {
            order.setPkEmpOrd(u.getPkEmp());
            order.setNameEmpOrd(u.getNameEmp());
            order.setEuStatusOrd("1");
            order.setFlagSign(Constants.TRUE);
            order.setDateSign(new Date());
        }
        DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd, name_emp_ord=:nameEmpOrd, eu_status_ord = :euStatusOrd , flag_sign = :flagSign , date_sign = :dateSign,note_ord=:noteOrd  where ordsn_parent =:ordsnParent ", signCnOrderList);
        cnPubService.caRecordByOrd(false, signCnOrderList);
        recExpOrder(signCnOrderList);
        cnPubService.sendMessage("新医嘱", signCnOrderList, false);
    }

    /**
     * 取消签署检查申请
     *
     * @param
     */
    public void cancelSignApply(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        List<CnRisApplyVo> saveRisList = (List<CnRisApplyVo>) JsonUtil.readValue(param, new TypeReference<List<CnRisApplyVo>>() {
        });
        if (saveRisList.size() <= 0) return;
        List<CnOrder> signCnOrderList = saveRis(user, saveRisList);
        if (signCnOrderList.size() <= 0) return;
        List<CnOrder> qryOrderMsg = null;
        try {
            //获取取消签署医嘱信息
            qryOrderMsg = cnOrderDao.qryOrderMsg(signCnOrderList);
        } catch (Exception e) {
            // TODO: handle exception
        }
        for (CnOrder order : signCnOrderList) {
            order.setPkEmpOrd(null);
            order.setNameEmpOrd(null);
            order.setEuStatusOrd("0");
            order.setFlagSign(Constants.FALSE);
            order.setDateSign(null);
        }
        DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd, name_emp_ord=:nameEmpOrd, eu_status_ord = :euStatusOrd , flag_sign = :flagSign , date_sign = :dateSign  where eu_status_ord='1' and ordsn_parent = :ordsnParent ", signCnOrderList);
        DataBaseHelper.batchUpdate("update cn_ris_apply set form_app=null  where pk_cnord = :pkCnord ", signCnOrderList);
        //取消签署时保存CA认证信息
        cnPubService.caRecordByOrd(false, signCnOrderList);
        //发送取消签署检查消息到平台
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPv", saveRisList.get(0).getPkPv());
        paramMap.put("type", "ris");
        paramMap.put("risList", signCnOrderList);
        paramMap.put("isAdd", "1");
        paramMap.put("Control", "OC");
        //发送医嘱信息取消签署服务到平台
        PlatFormSendUtils.sendCnMedApplyMsg(paramMap);
        paramMap = null;
    }

    private void recExpOrder(List<CnOrder> signCnOrderList) {
        if (signCnOrderList != null && signCnOrderList.size() > 0) {
            List<CnOrder> list = new ArrayList<CnOrder>();
            for (CnOrder order : signCnOrderList) {
                if (StringUtils.isNotBlank(order.getPkCprec())) {
                    list.add(order);
                }
            }
            cnPubService.recExpOrder(false, list, null);
        }
    }

    private List<CnOrder> saveRis(IUser user, List<CnRisApplyVo> saveRisList) throws IllegalAccessException, InvocationTargetException {
        List<CnOrder> signCnOrder = new ArrayList<CnOrder>();
        List<CnOrder> newOrdList = new ArrayList<CnOrder>();
        List<CnOrder> delOrdList = new ArrayList<CnOrder>();
        List<CnOrder> updOrdList = new ArrayList<CnOrder>();
        List<CnRisApply> newRisList = new ArrayList<CnRisApply>();
        List<CnRisApply> delRisList = new ArrayList<CnRisApply>();
        List<CnRisApply> updRisList = new ArrayList<CnRisApply>();
        User u = (User) user;
        Date d = new Date();

        String pkPv=null;
        String pkWgOrg=null;
        String pkWg=null;
        if(saveRisList!=null && saveRisList.size()>0 && StringUtils.isNotEmpty(saveRisList.get(0).getPkPv())){
            pkPv=saveRisList.get(0).getPkPv();
            //判断是否符合开立条件
            if(checkPvInfo(pkPv)){
                throw  new BusException("前患者就诊的科室为日间病房,住院超过允许开立医嘱的天数！不允许开立新医嘱！");
            }
            PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{pkPv});
            pkWgOrg=pvInfo.getPkWgOrg();
            pkWg=pvInfo.getPkWg();
        }

        for (CnRisApplyVo risVO1 : saveRisList) {
            String rowStatus = risVO1.getRowStatus();
            CnRisApply risVO = new CnRisApply();
            BeanUtils.copyProperties(risVO, risVO1);
            CnOrder orderVO = new CnOrder();
            if (rowStatus.equals(Constants.RT_NEW)) {
                risVO.setPkOrdris(NHISUUID.getKeyId());
                orderVO.setPkCnord(NHISUUID.getKeyId());
                orderVO.setOrdsn(risVO.getOrdsn());
                orderVO.setOrdsnParent(risVO.getOrdsnParent());
                risVO.setPkCnord(orderVO.getPkCnord());
                orderVO.setInfantNo(risVO.getInfantNo());
                orderVO.setPkPv(risVO.getPkPv());
                orderVO.setCodeOrdtype(risVO.getCodeOrdType());
                orderVO.setPkOrd(risVO.getPkOrd());
                orderVO.setCodeOrd(risVO.getCodeOrd());
                orderVO.setNameOrd(risVO.getNameOrd());
                orderVO.setPkDeptNs(risVO.getPkDeptNs());
                orderVO.setFlagBl(risVO.getFlagBl());
                orderVO.setFlagErase(Constants.FALSE);
                risVO.setPkOrg(u.getPkOrg());
                risVO.setEuStatus("0");
                risVO.setTs(d);
                orderVO.setTs(d);
                orderVO.setDateStart(risVO.getDateStart());
                orderVO.setPkOrg(risVO.getPkOrg());
                orderVO.setCodeApply(risVO.getCodeApply());
                orderVO.setPkPi(risVO.getPkPi());
                orderVO.setEuOrdtype(risVO1.getEuOrdtype()); //科研医嘱标志
                orderVO.setFlagFit(risVO1.getFlagFit());//适应症标志
                if (StringUtils.isBlank(risVO.getPkEmpInput())) {
                    orderVO.setPkEmpInput(u.getPkEmp());
                    orderVO.setNameEmpInput(u.getNameEmp());
                } else {
                    orderVO.setPkEmpInput(risVO.getPkEmpInput());
                    orderVO.setNameEmpInput(risVO.getNameEmpInput());
                }
                orderVO.setPkWgOrg(pkWgOrg);//医疗组
                orderVO.setPkWg(pkWg);
                setOrdList(user, u, orderVO);
                setRisOrdPubField(risVO, orderVO);
                if (StringUtils.isNotBlank(risVO.getPkDept())) {
                    orderVO.setPkDept(risVO.getPkDept());
                }  else {

                    orderVO.setPkDept(u.getPkDept());
                }
                orderVO.setCnSignCa(risVO.getCnSignCa());
                orderVO.setPkCprec(risVO.getPkCprec());
                orderVO.setPkCpexp(risVO.getPkCpexp());
                orderVO.setExpNote(risVO.getExpNote());
                orderVO.setNameExp(risVO.getNameExp());
                orderVO.setPkCpphase(risVO.getPkCpphase());
                orderVO.setPriceCg(StringUtils.isBlank(risVO.getPriceCg()) ? 0 : Double.parseDouble(risVO.getPriceCg()));
                orderVO.setEuIntern(risVO.getEuIntern());
                orderVO.setGroupno(risVO.getGroupno());
                newRisList.add(risVO);
                newOrdList.add(orderVO);
                signCnOrder.add(orderVO);
            } else if (rowStatus.equals(Constants.RT_UPDATE)) {
                orderVO.setCnSignCa(risVO.getCnSignCa());
                orderVO.setPkCnord(risVO.getPkCnord());
                orderVO.setPkOrg(risVO.getPkOrg());
                orderVO.setTs(risVO.getTs());
                orderVO.setGroupno(risVO.getGroupno());
                orderVO.setOrdsn(risVO.getOrdsn());
                orderVO.setOrdsnParent(risVO.getOrdsnParent());
                orderVO.setCodeApply(risVO.getCodeApply());
                orderVO.setDateStart(risVO.getDateStart());
                orderVO.setPkCprec(risVO.getPkCprec());
                orderVO.setPkCpexp(risVO.getPkCpexp());
                orderVO.setExpNote(risVO.getExpNote());
                orderVO.setNameExp(risVO.getNameExp());
                orderVO.setPkCpphase(risVO.getPkCpphase());
                orderVO.setEuOrdtype(risVO1.getEuOrdtype()); //科研医嘱标志
                orderVO.setFlagFit(risVO1.getFlagFit());//适应症标志
                orderVO.setPkWgOrg(pkWgOrg);//医疗组
                orderVO.setPkWg(pkWg);
                setRisOrdPubField(risVO, orderVO);
                updOrdList.add(orderVO);
                updRisList.add(risVO);
                if ("1".equals(risVO.getEuStatusOrd())) signCnOrder.add(orderVO);
            } else if (rowStatus.equals(Constants.RT_REMOVE)) {
                orderVO.setPkCnord(risVO.getPkCnord());
                orderVO.setTs(risVO.getTs());
                orderVO.setGroupno(risVO.getGroupno());
                orderVO.setOrdsn(risVO.getOrdsn());
                orderVO.setOrdsnParent(risVO.getOrdsnParent());
                delOrdList.add(orderVO);
                delRisList.add(risVO);
            }
        }
        saveOrdList(newOrdList, delOrdList, updOrdList, d);
        if (newRisList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnRisApply.class), newRisList);
        }
        if (updRisList.size() > 0) {
            DataBaseHelper.batchUpdate("update cn_ris_apply set " + cnPubService.setTs(d) + ", note_dise=:noteDise, desc_body =:descBody ,purpose =:purpose,name_diag=:nameDiag,ris_notice=:risNotice  where pk_ordris= :pkOrdris ", updRisList);
        }
        if (delRisList.size() > 0) {
            DataBaseHelper.batchUpdate("delete from cn_ris_apply where pk_ordris = :pkOrdris ", delRisList);
        }
        return signCnOrder;
    }

    private void setRisOrdPubField(CnRisApply risVO, CnOrder orderVO) {
        orderVO.setQuan(risVO.getQuan());
        orderVO.setDosage(risVO.getQuan());
        orderVO.setFlagEmer(risVO.getFlagEmer());
        orderVO.setPkOrgExec(risVO.getPkOrgExec());
        orderVO.setPkDeptExec(risVO.getPkDeptExec());
        orderVO.setNoteOrd(risVO.getNoteOrd());
        orderVO.setOrdsnChk(orderVO.getOrdsn());
    }

    private void setLisOrdPubField(CnLabApply lisVO, CnOrder orderVO) {
        orderVO.setQuan(lisVO.getQuan());
        orderVO.setDosage(lisVO.getQuan());
        orderVO.setFlagEmer(lisVO.getFlagEmer());
        orderVO.setPkOrgExec(lisVO.getPkOrgExec());
        orderVO.setPkDeptExec(lisVO.getPkDeptExec());
        orderVO.setNoteOrd(lisVO.getNoteOrd());
    }

    /**
     * 检查作废
     *
     * @param param
     * @param user
     */
    public void cancleRisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        ApplyCanlParam rcp = JsonUtil.readValue(param, ApplyCanlParam.class);
        List<String> pkCnOrds = rcp.getPkCnOrds();
        List<CnRisApplyVo> saveRisList = rcp.getSaveRisList();
        if (pkCnOrds.size() <= 0 && saveRisList.size() <= 0) return;
        saveRis(user, saveRisList);
        cancleApply(user, rcp);
        cnNoticeService.saveCnNoticeVoidForLis(param);
        //发送平台消息
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkCnList", pkCnOrds);
        PlatFormSendUtils.sendCancleRisApplyListMsg(paramMap);
    }

    /**
     * 医生站--检验作废
     *
     * @param param
     * @param user
     */
    public void cancleLisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        ApplyCanlParam rcp = JsonUtil.readValue(param, ApplyCanlParam.class);
        List<String> pkCnOrds = rcp.getPkCnOrds();
        List<CnLabApplyVo> saveLisList = rcp.getSaveLisList();
        if (pkCnOrds.size() <= 0 && saveLisList.size() <= 0) return;
        saveLis(user, saveLisList);
        cancleApply(user, rcp);
        //作废医嘱通知（界面右上角）
        cnNoticeService.saveCnNoticeVoidForLis(param);
        //发送平台消息
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkCnList", pkCnOrds);
        PlatFormSendUtils.sendCancleLisApplyListMsg(paramMap);
    }

    private void cancleApply(IUser user, ApplyCanlParam rcp) {
        List<String> pkCnOrds = rcp.getPkCnOrds();
        //作废非药品医嘱时
        cancleOrd(user, pkCnOrds);
        List<CnSignCa> cnSignCaList = rcp.getCnSignCaList();
        cnPubService.caRecord(cnSignCaList);
        List<CnOrder> cnOrds = DataBaseHelper.queryForList("select name_ord,pk_pv,pk_dept_ns,pk_cnord,ordsn from cn_order where pk_cnord=?", CnOrder.class, new Object[]{pkCnOrds.get(0)});
        cnPubService.sendMessage("作废医嘱", cnOrds, false);
        //同步
        //ordProcService.syncSendOrds(cnOrds, (User)user, "C","OBS");
    }

    private void cancleOrd(IUser user, List<String> pkCnOrds) {
        User u = (User) user;
        Date dNow = new Date();
        //作废医嘱是否需要护士核对(只处理作废标志、作废人、作废时间)
        String ifNeedNurseChk = ApplicationUtils.getSysparam("CN0028", false);
        if (ifNeedNurseChk == null) ifNeedNurseChk = "0";
        if (ifNeedNurseChk.equals("1")) {
            List<CnOrder> updateList = new ArrayList<CnOrder>();
            for (String pk_cnord : pkCnOrds) {
                CnOrder order = new CnOrder();
                order.setPkCnord(pk_cnord);
                order.setEuStatusOrd("9");
                order.setFlagErase(Constants.TRUE);
                order.setDateErase(new Date());
                order.setPkEmpErase(u.getPkEmp());
                order.setNameEmpErase(u.getNameEmp());
                order.setTs(new Date());
                updateList.add(order);
            }
            if (updateList.size() > 0) {
                DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord", updateList);
                //作废医嘱
                DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
                        + "where pk_cnord =:pkCnord and eu_status_ord not in ('0','1') ", updateList);
            }
            return;
        }
        List<CnOrder> canlList = new ArrayList<CnOrder>();
        for (String pk_cnord : pkCnOrds) {
            CnOrder cnOrd = new CnOrder();
            cnOrd.setPkCnord(pk_cnord);
            cnOrd.setEuStatusOrd("9");
            cnOrd.setFlagErase(Constants.TRUE);
            cnOrd.setDateErase(new Date());
            cnOrd.setPkEmpErase(u.getPkEmp());
            cnOrd.setNameEmpErase(u.getNameEmp());
            canlList.add(cnOrd);
        }
        if (canlList.size() > 0) {
            Map<String, Object> pkOrdMap = new HashMap<String, Object>();
            pkOrdMap.put("pk_cnord", pkCnOrds);
            pkOrdMap.put("dateCanc", dNow);
            pkOrdMap.put("pkEmpCanc", u.getPkEmp());
            pkOrdMap.put("nameEmpCanc", u.getNameEmp());
            String applySql = "select ord.name_ord ,dept.name_dept ,ris.eu_status as ris_status, lab.eu_status as lab_status from cn_order ord inner join bd_ou_dept dept on dept.pk_dept=ord.pk_dept_ns and dept.del_flag='0' left join cn_ris_apply ris on ris.pk_cnord = ord.pk_cnord and ris.del_flag='0' left join cn_lab_apply lab on lab.pk_cnord = ord.pk_cnord and lab.del_flag='0'  where ord.pk_cnord in (:pk_cnord) and ord.del_flag='0' and ord.flag_erase='0'  ";
            List<Map<String, Object>> applyList = DataBaseHelper.queryForList(applySql, pkOrdMap);
            String throwStr = "";
            for (Map<String, Object> map : applyList) {
                if (map.get("risStatus") != null && "1".compareTo(map.get("risStatus").toString()) <= 0 || map.get("labStatus") != null && "1".compareTo(map.get("labStatus").toString()) <= 0)
                    throwStr += "[" + map.get("nameOrd") + "]需要[" + map.get("nameDept") + "]先取消提交才能作废!\n";
            }
            if (StringUtils.isNotEmpty(throwStr)) throw new BusException(throwStr);
            String exListSql = "select ord.name_ord,ex.pk_exocc,ex.pk_cnord,ex.eu_status,dept.name_dept " +
                    "from ex_order_occ ex " +
                    "inner join cn_order ord on ex.pk_cnord = ord.pk_cnord  " +
                    "inner join bd_ou_dept dept on ex.pk_dept_occ = dept.pk_dept and dept.del_flag='0' " +
                    "where ex.eu_status=1 and ex.del_flag='0' and ex.flag_canc='0' and ord.pk_cnord in(:pk_cnord)  ";
            //查询执行单/非药品/未取消执行的/不包括护嘱 均不可以撤销
            List<Map<String, Object>> exList = DataBaseHelper.queryForList(exListSql, pkOrdMap);
            throwStr = "";
            for (Map<String, Object> map : exList) {
                throwStr += "[" + map.get("nameOrd") + "]需要[" + map.get("nameDept") + "]先取消执行才能作废!\n";
            }
            if (StringUtils.isNotEmpty(throwStr)) throw new BusException(throwStr);
            //作废非药品医嘱时，如果已生成医嘱执行单并且未执行，修改执行状态为取消执行（包括护嘱）
            String delNoPdExList = "update ex_order_occ set ex_order_occ.eu_status='9', flag_canc='1',date_canc=:dateCanc,pk_emp_canc=:pkEmpCanc, name_emp_canc=:nameEmpCanc " +   //delete  from ex_order_occ
                    "where exists (select 1 " +
                    "from cn_order   " +
                    "where ex_order_occ.pk_cnord = cn_order.pk_cnord " +
                    "and ex_order_occ.eu_status = 0 " +
                    "and cn_order.ordsn_parent in ( select ordsn_parent from cn_order where pk_cnord in (:pk_cnord) )  " +
                    "and cn_order.flag_durg='0')";
            DataBaseHelper.update(delNoPdExList, pkOrdMap);
            DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd , flag_erase= :flagErase , date_erase = :dateErase , pk_emp_erase= :pkEmpErase , name_emp_erase= :nameEmpErase  where pk_cnord= :pkCnord", canlList);
            DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd , flag_erase= :flagErase , date_erase = :dateErase , pk_emp_erase= :pkEmpErase , name_emp_erase= :nameEmpErase  where ordsn_parent in ( select ordsn_parent from cn_order where pk_cnord= :pkCnord) and flag_doctor='0' ", canlList);
            DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord", canlList);
        }
    }

    public void delRisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        List<CnRisApplyVo> saveRisList = (List<CnRisApplyVo>) JsonUtil.readValue(param, new TypeReference<List<CnRisApplyVo>>() {
        });
        if (saveRisList.size() <= 0) return;
        saveRis(user, saveRisList);
    }

    public void delLisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        List<CnLabApplyVo> saveLisList = (List<CnLabApplyVo>) JsonUtil.readValue(param, new TypeReference<List<CnLabApplyVo>>() {
        });
        if (saveLisList.size() <= 0) return;
        saveLis(user, saveLisList);
    }

    private List<CnOrder> saveLis(IUser user, List<CnLabApplyVo> saveLisList) throws IllegalAccessException, InvocationTargetException {
        List<CnOrder> signCnOrder = new ArrayList<CnOrder>();
        List<CnOrder> newOrdList = new ArrayList<CnOrder>();
        List<CnOrder> delOrdList = new ArrayList<CnOrder>();
        List<CnOrder> updOrdList = new ArrayList<CnOrder>();
        List<CnLabApply> newLisList = new ArrayList<CnLabApply>();
        List<CnLabApply> delLisList = new ArrayList<CnLabApply>();
        List<CnLabApply> updLisList = new ArrayList<CnLabApply>();
        User u = (User) user;
        Date d = new Date();

        String pkPv=null;
        String pkWgOrg=null;
        String pkWg=null;
        if(saveLisList!=null && saveLisList.size()>0 && StringUtils.isNotEmpty(saveLisList.get(0).getPkPv())){
            pkPv=saveLisList.get(0).getPkPv();
            //判断是否符合开立条件
            if(checkPvInfo(pkPv)){
                throw  new BusException("前患者就诊的科室为日间病房,住院超过允许开立医嘱的天数！不允许开立新医嘱！");
            }
            PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{pkPv});
            pkWgOrg=pvInfo.getPkWgOrg();
            pkWg=pvInfo.getPkWg();
        }

        for (CnLabApplyVo lisVO1 : saveLisList) {
            String rowStatus = lisVO1.getRowStatus();
            CnOrder orderVO = new CnOrder();
            CnLabApply lisVO = new CnLabApply();
            BeanUtils.copyProperties(lisVO, lisVO1);
            if (rowStatus.equals(Constants.RT_NEW)) {
                lisVO.setPkOrdlis(NHISUUID.getKeyId());
                orderVO.setPkCnord(NHISUUID.getKeyId());
                lisVO.setPkCnord(orderVO.getPkCnord());
                orderVO.setInfantNo(lisVO.getInfantNo());
                orderVO.setPkPv(lisVO.getPkPv());
                orderVO.setCodeOrdtype(lisVO.getCodeOrdType());
                orderVO.setPkOrd(lisVO.getPkOrd());
                orderVO.setCodeOrd(lisVO.getCodeOrd());
                orderVO.setNameOrd(lisVO.getNameOrd());
                orderVO.setPkDeptNs(lisVO.getPkDeptNs());
                orderVO.setFlagBl(lisVO.getFlagBl());
                orderVO.setEuOrdtype(lisVO1.getEuOrdtype());  //科研医嘱标志
                orderVO.setFlagFit(lisVO1.getFlagFit());//适应症标志
                lisVO.setPkOrg(u.getPkOrg());
                lisVO.setEuStatus("0");
                lisVO.setTs(d);
                orderVO.setTs(d);
                orderVO.setPkOrg(lisVO.getPkOrg());
                orderVO.setCodeApply(lisVO.getCodeApply());
                orderVO.setPkPi(lisVO.getPkPi());
                orderVO.setOrdsn(lisVO.getOrdsn());
                orderVO.setOrdsnParent(lisVO.getOrdsnParent());
                orderVO.setGroupno(lisVO.getGroupno());
                orderVO.setDateStart(lisVO.getDateStart());
                if (StringUtils.isBlank(lisVO.getPkEmpInput())) {
                    orderVO.setPkEmpInput(u.getPkEmp());
                    orderVO.setNameEmpInput(u.getNameEmp());
                } else {
                    orderVO.setPkEmpInput(lisVO.getPkEmpInput());
                    orderVO.setNameEmpInput(lisVO.getNameEmpInput());
                }
                setOrdList(user, u, orderVO);
                setLisOrdPubField(lisVO, orderVO);
                if (StringUtils.isNotBlank(lisVO1.getPkDept())) {
                    orderVO.setPkDept(lisVO.getPkDept());
                }else {
                    orderVO.setPkDept(u.getPkDept());
                }
                orderVO.setCnSignCa(lisVO.getCnSignCa());
                orderVO.setPkCprec(lisVO.getPkCprec());
                orderVO.setPkCpexp(lisVO.getPkCpexp());
                orderVO.setExpNote(lisVO.getExpNote());
                orderVO.setPkCpphase(lisVO.getPkCpphase());
                orderVO.setNameExp(lisVO.getNameExp());
                orderVO.setPriceCg(StringUtils.isBlank(lisVO.getPriceCg()) ? 0 : Double.parseDouble(lisVO.getPriceCg()));
                orderVO.setEuIntern(lisVO.getEuIntern());
                orderVO.setPkWgOrg(pkWgOrg);//医疗组
                orderVO.setPkWg(pkWg);
                newLisList.add(lisVO);
                newOrdList.add(orderVO);
                signCnOrder.add(orderVO);
            } else if (rowStatus.equals(Constants.RT_UPDATE)) {
                orderVO.setPkCnord(lisVO.getPkCnord());
                orderVO.setPkOrg(lisVO.getPkOrg());
                orderVO.setTs(lisVO.getTs());
                orderVO.setOrdsn(lisVO.getOrdsn());
                orderVO.setOrdsnParent(lisVO.getOrdsnParent());
                orderVO.setGroupno(lisVO.getGroupno());
                orderVO.setDateStart(lisVO.getDateStart());
                orderVO.setCodeApply(lisVO.getCodeApply());
                orderVO.setPkCprec(lisVO.getPkCprec());
                orderVO.setPkCpexp(lisVO.getPkCpexp());
                orderVO.setExpNote(lisVO.getExpNote());
                orderVO.setPkCpphase(lisVO.getPkCpphase());
                orderVO.setNameExp(lisVO.getNameExp());
                orderVO.setEuOrdtype(lisVO1.getEuOrdtype());  //科研医嘱标志
                orderVO.setFlagFit(lisVO1.getFlagFit());//适应症标志
                orderVO.setPkWgOrg(pkWgOrg);//医疗组
                orderVO.setPkWg(pkWg);
                setLisOrdPubField(lisVO, orderVO);
                updOrdList.add(orderVO);
                updLisList.add(lisVO);
                if ("1".equals(lisVO.getEuStatusOrd())) signCnOrder.add(orderVO);
            } else if (rowStatus.equals(Constants.RT_REMOVE)) {
                orderVO.setPkCnord(lisVO.getPkCnord());
                orderVO.setTs(lisVO.getTs());
                orderVO.setOrdsn(lisVO.getOrdsn());
                orderVO.setOrdsnParent(lisVO.getOrdsnParent());
                orderVO.setGroupno(lisVO.getGroupno());
                orderVO.setPkWgOrg(pkWgOrg);//医疗组
                orderVO.setPkWg(pkWg);
                delOrdList.add(orderVO);
                delLisList.add(lisVO);
            }
        }
        saveOrdList(newOrdList, delOrdList, updOrdList, d);
        if (newLisList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnLabApply.class), newLisList);
        }
        if (updLisList.size() > 0) {
            DataBaseHelper.batchUpdate("update cn_lab_apply set " + cnPubService.setTs(d) + ", purpose = :purpose, dt_samptype= :dtSamptype,dt_tubetype= :dtTubetype, dt_coltype= :dtColtype ,desc_diag=:descDiag  where pk_ordlis= :pkOrdlis ", updLisList);
        }
        if (delLisList.size() > 0) {
            DataBaseHelper.batchUpdate("delete from cn_lab_apply where pk_ordlis = :pkOrdlis ", delLisList);
        }
        return signCnOrder;
    }

    private void setOrdList(IUser user, User u, CnOrder orderVO) {
        if ((orderVO.getOrdsn() == null) || (orderVO.getOrdsn() != null && orderVO.getOrdsn() <= 0)) {
            orderVO.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));
            orderVO.setOrdsnParent(orderVO.getOrdsn());
        }
        orderVO.setDescOrd(orderVO.getNameOrd());
        orderVO.setEuAlways("1");
        orderVO.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
        orderVO.setEuStatusOrd("0");        //保存
        String sfalse = Constants.FALSE;
        orderVO.setFlagDurg(sfalse);
        orderVO.setFlagFirst(sfalse);
        orderVO.setFlagSign(sfalse);
        orderVO.setFlagSelf(sfalse);
        orderVO.setFlagNote(sfalse);
        orderVO.setFlagBase(sfalse);
        orderVO.setFlagStop(sfalse);
        orderVO.setFlagStopChk(sfalse);
        orderVO.setFlagErase(sfalse);
        orderVO.setFlagEraseChk(sfalse);
        orderVO.setFlagCp(sfalse);
        orderVO.setFlagDoctor(Constants.TRUE);
        orderVO.setFlagPrint(sfalse);
        orderVO.setFlagMedout(sfalse);
        orderVO.setFlagEmer(sfalse);
        orderVO.setFlagThera(sfalse);
        orderVO.setFlagPrev(sfalse);
        //orderVO.setFlagFit(sfalse);
        orderVO.setDelFlag(sfalse);
        orderVO.setEuPvtype("3");
        orderVO.setDateEnter(orderVO.getTs());
//		orderVO.setPkEmpInput(u.getPkEmp());
//		orderVO.setNameEmpInput(u.getNameEmp());
//		orderVO.setPkEmpOrd(u.getPkEmp());  //签署时写入
//		orderVO.setNameEmpOrd(u.getNameEmp());
    }

    private void saveOrdList(List<CnOrder> newOrdList, List<CnOrder> delOrdList, List<CnOrder> updOrdList, Date d) {
        List<String> codeApplyList = new ArrayList<String>();
        if (newOrdList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), newOrdList); //新增医嘱
            //更新科研医嘱标记
            List<String> pkOrds = new ArrayList<String>();
            for (CnOrder vo : newOrdList) {
                codeApplyList.add(vo.getCodeApply());
                if (vo.getOrdsn() != null && !pkOrds.contains(vo.getOrdsn().toString())) {
                    pkOrds.add(vo.getOrdsn().toString());
                }
            }
            orderPubService.upFlagKy(pkOrds);
        }
        if (updOrdList.size() > 0) {
            cnPubService.vaildUpdateCnOrdts(updOrdList);
            DataBaseHelper.batchUpdate("update cn_order set " + cnPubService.setTs(d) + " ,date_start= :dateStart, quan = :quan, dosage = :dosage,flag_emer = :flagEmer, pk_org_exec = :pkOrgExec ,pk_dept_exec = :pkDeptExec, note_ord = :noteOrd,groupno=:groupno,ordsn_parent=:ordsnParent,code_apply=:codeApply   where pk_cnord = :pkCnord ", updOrdList);
        }
        if (delOrdList.size() > 0) {
            cnPubService.vaildUpdateCnOrdts(delOrdList);
            DataBaseHelper.batchUpdate("delete from cn_order where pk_cnord = :pkCnord ", delOrdList);
            //如果所删医嘱是父医嘱，则删同组医嘱（包含护嘱）
            List<CnOrder> delParentOrdList = new ArrayList<CnOrder>();
            for (CnOrder ordVo : delOrdList) {
                if (ordVo.getOrdsn().compareTo(ordVo.getOrdsnParent()) == 0) {
                    delParentOrdList.add(ordVo);
                }
            }
            if (delParentOrdList.size() > 0) {
                DataBaseHelper.batchUpdate("delete from cn_order where ordsn_parent =:ordsnParent ", delParentOrdList);
            }
        }
        //因存在数据重复问题，故加此判断
        if (codeApplyList != null && codeApplyList.size() > 0) {
            Map<String, Object> codeApplyMap = new HashMap<String, Object>();
            codeApplyMap.put("codeApply", codeApplyList);
            String sqlStr = "select code_apply from cn_order where code_ordtype like '03%'  and code_apply in (:codeApply) group by code_apply having count(1)>1";
            List<Map<String, Object>> repeartApplyList = DataBaseHelper.queryForList(sqlStr, codeApplyMap);
            if (repeartApplyList != null && repeartApplyList.size() > 0) {
                throw new BusException("数据重复，保存失败");
            }
        }
    }

    public void saveOrdComm(String param, IUser user) {
        List<BdOrdComm> list = JsonUtil.readValue(param, new TypeReference<List<BdOrdComm>>() {
        });
        User u = (User) user;
        if (list == null || list.size() == 0) return;
        List<String> pkOrds = new ArrayList<String>();
        for (BdOrdComm ordComm : list) {
            pkOrds.add(ordComm.getPkOrd());
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String valSql = "select pk_ord from bd_ord_comm where  pk_emp=:pkEmp and pk_org=:pkOrg and substr(code_ordtype, 1, 2)=:codeOrdtype and  pk_ord in(:pkOrd)";
        paramMap.put("pkEmp", u.getPkEmp());
        paramMap.put("pkOrg", u.getPkOrg());
        paramMap.put("codeOrdtype", list.get(0).getCodeOrdtype().substring(0, 2));
        paramMap.put("pkOrd", pkOrds);
        List<Map<String, Object>> result = (List<Map<String, Object>>) DataBaseHelper.queryForList(valSql, paramMap);
        List<BdOrdComm> saveCommList = new ArrayList<BdOrdComm>();
        for (BdOrdComm ordComm : list) {
            Boolean s = true;
            if (null != result && result.size() > 0) {
                for (Map<String, Object> commOrd : result) {
                    String pk_ord = (String) commOrd.get("pkOrd");
                    if (pk_ord.equals(ordComm.getPkOrd())) {
                        s = false;
                        break;
                    }
                }
            }
            if (s) {
                ordComm.setPkOrdcomm(NHISUUID.getKeyId());
                ordComm.setPkOrg(u.getPkOrg());
                ordComm.setPkEmp(u.getPkEmp());
                ordComm.setTs(new Date());
                ordComm.setDelFlag(Constants.FALSE);
                saveCommList.add(ordComm);
            }
        }
        if (saveCommList.size() > 0)
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdOrdComm.class), saveCommList);
    }

    public List<Map<String, Object>> qryDtBody(String param, IUser user) {
        return cnReqDao.qryDtBody();
    }

    /**
     * 查询检查申请Vo
     *
     * @param param
     * @param user
     * @return
     */
    public CnRisApply queryCnRisApply(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String sqlStr = "select ris.*,ord.code_apply from cn_ris_apply ris inner join cn_order ord on ord.pk_cnord = ris.pk_cnord  where ORD.ordsn=:ordsn";
        List<CnRisApply> risApplyList = DataBaseHelper.queryForList(sqlStr, CnRisApply.class, paramMap);
        return risApplyList.get(0);
    }

    /**
     * 查询检查申请单模板
     *
     * @param param
     * @param user
     * @return
     */
    public EmrTemplateVo qryRisFormTemp(String param, IUser user) {
        EmrTemplateVo rtnVo = new EmrTemplateVo();
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<EmrTemplateVo> emrTemplateVoLit = cnReqDao.qryRisForm(paramMap);
        if (emrTemplateVoLit != null && emrTemplateVoLit.size() > 0) {
            rtnVo = emrTemplateVoLit.get(0);
        }
        return rtnVo;
    }

    /**
     * 修改检查申请单
     *
     * @param param
     * @param user
     */
    public void modifyRisForm(String param, IUser user) {
        CnRisApply cnRisApply = JsonUtil.readValue(param, CnRisApply.class);
        //DataBaseHelper.update("update cn_ris_apply set form_app = :formApp where pk_cnord= :pkCnord", cnRisApply);
        //cnReqDao.modifyRisForm(cnRisApply);
        DataBaseHelper.update("update cn_ris_apply set flag_print=:flagPrint, form_app = :formApp,ts = :ts where pk_cnord= :pkCnord", cnRisApply);
    }

    /**
     * 查询患者检查报告的URL（中山二院项目）
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qryPacsReportUrl(String param, IUser user) {
        Map<String, Object> qryMap = JsonUtil.readValue(param, Map.class);
        String codePv = (String) qryMap.get("pvCode");
        Map<String, Object> res = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("PACS", "qryPacsReportUrl", codePv);
        return res;
    }

    /**
     * 取消签署检验申请
     *
     * @param
     */
    public void cancelSignLisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        List<CnLabApplyVo> saveLisList = (List<CnLabApplyVo>) JsonUtil.readValue(param, new TypeReference<List<CnLabApplyVo>>() {
        });
        if (saveLisList.size() <= 0) return;
        List<CnOrder> signCnOrderList = saveLis(user, saveLisList);
        List<CnOrder> qryOrderMsg = null;
        try {
            //获取取消签署医嘱信息
            qryOrderMsg = cnOrderDao.qryOrderMsg(signCnOrderList);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (signCnOrderList.size() <= 0) return;
        for (CnOrder order : signCnOrderList) {
            order.setPkEmpOrd(null);
            order.setNameEmpOrd(null);
            order.setEuStatusOrd("0");
            order.setFlagSign(Constants.FALSE);
            order.setDateSign(null);
        }
        DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd, name_emp_ord=:nameEmpOrd, eu_status_ord = :euStatusOrd , flag_sign = :flagSign , date_sign = :dateSign  where eu_status_ord='1' and ordsn_parent = :ordsnParent ", signCnOrderList);
        //发送取消签署检验申请消息到平台
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPv", saveLisList.get(0).getPkPv());
        paramMap.put("type", "lis");
        paramMap.put("lisList", signCnOrderList);
        paramMap.put("isAdd", "1");
        paramMap.put("pkPv", saveLisList.get(0).getPkPv());
        paramMap.put("Control", "OC");
        //发送医嘱信息到平台
        PlatFormSendUtils.sendCnMedApplyMsg(paramMap);
        paramMap = null;
    }

    /**
     * 根据pk_cnord查询用户CA电子签名
     * 交易码：004004006016
     *
     * @param param
     * @param user
     * @return
     */
    public OrdCaVo qryOrdCaByPkcnord(String param, IUser user) {
        Map<String, Object> qryMap = JsonUtil.readValue(param, Map.class);
        OrdCaVo retVo = new OrdCaVo();
        if (qryMap != null && qryMap.size() > 0) {
            if (qryMap.containsKey("pkEmp") && qryMap.get("pkEmp") != null &&
                    !CommonUtils.isEmptyString(CommonUtils.getString(qryMap.get("pkEmp")))) {
                //根据当前传入pkEmp查询该人员CA图片
                retVo = cnReqDao.qryImgCaByPkEmp(qryMap);
            } else {
                retVo = cnReqDao.qryOrdCaByPkcnord(qryMap);
            }
        }
        return retVo;
    }

    /*************************************医嘱属性 前端不需要处理 start**************************************/
    //处理日间病房医嘱开立期限的校验
    private boolean checkPvInfo(String pkPv){
        boolean ok=false;//默认通过校验
        String day=ApplicationUtils.getSysparam("CN0107", false);
        String sql="Select PV.* From pv_encounter pv \n" +
                "Inner Join bd_dictattr dict On pv.pk_dept=dict.pk_dict \n" +
                "Where  pv.pk_pv=? And dict.code_attr='0605' And dict.val_attr='1'";
        PvEncounter pvEncounter=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{pkPv});

        if(org.apache.commons.lang3.StringUtils.isNotEmpty(day) && pvEncounter!=null){
            int dayIp= DateUtils.getDateSpace(pvEncounter.getDateBegin(), new Date());
            int dayI= Integer.parseInt(day);
            //判断
            if(dayI>0 && dayI<dayIp) ok=true;
        }
        return ok;
    }
    /*************************************医嘱属性 前端不需要处理 end**************************************/
}
