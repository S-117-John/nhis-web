package com.zebone.nhis.pro.zsba.cn.opdw.service;

import com.zebone.nhis.arch.support.ArchUtil;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.cn.pub.vo.OrdBlVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.InsQgybPV;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.pi.pub.vo.PiMasterVo;
import com.zebone.nhis.pro.zsba.cn.opdw.dao.CnPiMapper;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.BlConsultationFree;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.PiPhyVo;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.PiPhysiological;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.PvClinicRecord;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.PiOpVo;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.SchOrPv;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.OpOrdVo;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.service.qgyb.ZsbaQGService;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.build.BuildSql;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CnPiService {

    @Resource
    private CnPiMapper ordDao;
    @Resource
    private  ZsbaQGService zsbaQGService;

    @Resource
    private OpCgPubService opCgPubService;

    public static final String reqCodeCg = "cn.order.codeCg";
    /***患者综合查询逻辑***/

    /**
     * 查询患者本次就诊医嘱信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<OrdBlVo> qryOrdAllInf(String param, IUser user) {
        OpOrdVo opOrdVo = JsonUtil.readValue(param, OpOrdVo.class);
        if (opOrdVo == null || StringUtils.isEmpty(opOrdVo.getPkPv())) {
            throw new BusException("请传入当前患者就诊主键！！！");
        }
        List<OrdBlVo> ordBlVos = new ArrayList<OrdBlVo>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pkPv", opOrdVo.getPkPv());
        //1.查询当前患者的医嘱
        ordBlVos = ordDao.qryOrd(map);
        if (ordBlVos == null || ordBlVos.size() == 0) return null;
        //2.查询收费状态及费用
        List<OrdBlVo> ordBlinfos = ordDao.qryBlByPv(map);
        map.put("orders", ordBlVos);
        //3.查询收费状态及费用
        List<OrdBlVo> ordBls = ordDao.qryBlByOrd(map);
        //4.查询医嘱执行状态
        List<OrdBlVo> ordExinfos = ordDao.qryOrdEx(map);
        //6.处理业务逻辑，拼接数据
        handlingBusinessOrd(ordBlVos, ordBlinfos, ordBls, ordExinfos);
        return ordBlVos;
    }

    private void handlingBusinessOrd(List<OrdBlVo> ordBlVos, List<OrdBlVo> ordBlinfos, List<OrdBlVo> ordBls, List<OrdBlVo> ordExinfos) {
        //以医嘱表作为主表循环
        double zero = 0;
        for (OrdBlVo vo : ordBlVos) {
            vo.setAmountCg(0d);//默认给0
            vo.setFlagEx("0");//默认未执行
            //收费金额
            if (ordBlinfos != null && ordBlinfos.size() > 0) {
                for (OrdBlVo bl : ordBlinfos) {
                    if (vo.getPkCnord().equals(bl.getPkCnord())) {
                        vo.setAmountCg(bl.getAmount());
                        vo.setFlagSettle(bl.getFlagSettle());
                        vo.setSettle(Double.parseDouble(bl.getFlagSettle()));
                        break;
                    }
                }
            }
            //收费状态
            if (ordBls != null && ordBls.size() > 0) {
                for (OrdBlVo bl : ordBls) {
                    if (vo.getPkCnord().equals(bl.getPkCnord())) {
                        if ("1".equals(vo.getFlagDurg())) {//药品类型
                            //部分退费，或未退费，则为收费状态
                            if (bl.getExSum() != 0 && bl.getExSum() > bl.getRefund() || bl.getRefund() == zero) {
                                vo.setFlagSettle("1");
                                break;
                            } else { //其他情况为退费状态
                                vo.setFlagSettle("3");
                                break;
                            }
                        }
                    }
                }
            }
            //执行状态
            if (ordExinfos != null && ordExinfos.size() > 0) {
                for (OrdBlVo ex : ordExinfos) {
                    if (vo.getPkCnord().equals(ex.getPkCnord())) {
                        if ("1".equals(vo.getFlagDurg())) {//药品类型
                            if (ex.getExSum() > zero) {
                                vo.setFlagEx("1");//已发药
                                break;
                            }
                            //退药与发药记录同时存在的情况，以退药状态为准
                            if (ex.getRefund() > zero) {
                                vo.setFlagEx("2");//已退药
                                break;
                            }
                        } else { //非药品类型
                            if (ex.getExSum() == zero) {
                                vo.setFlagEx("0");//未执行
                                break;
                            }
                            if (ex.getExSum() != zero && ex.getRefund() == zero && ex.getOcc() == zero) {
                                vo.setFlagEx("0");//未执行
                                break;
                            }
                            if (ex.getExSum() != zero && ex.getRefund() == zero && ex.getOcc() == ex.getExSum()) {
                                vo.setFlagEx("1");//已执行
                                break;
                            }
                            if (ex.getExSum() != zero && ex.getOcc() == zero && ex.getRefund() == ex.getExSum()) {
                                vo.setFlagEx("2");//已取消
                                break;
                            }
                        }
                        vo.setFlagEx("3");//其他情况
                        break;
                    }
                }
            }
        }
    }
    /***患者综合查询逻辑***/

    /***
     * 根据Pkpi查询当前患者是否之前有欠费
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryHistoryNoSettle(String param, IUser user) {
        //pkPi
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPi = paramMap.get("pkPi") != null ? paramMap.get("pkPi").toString() : "";
        if (StringUtils.isBlank(pkPi)) throw new BusException("传参pkPi为空！");
        List<Map<String, Object>> histOrderlist = ordDao.qryHistoryNoSettle(paramMap);
        return histOrderlist;
    }

    /***
     * 根据Pkpv处理患者是否免收就诊费用逻辑
     * @param param
     * @param user
     * @return
     */
    public void qryPvNonSettleInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = MapUtils.getString(paramMap, "pkPv");
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("查询当前患者是否符合免接诊费-传参pkPv为空！");
        }
        BlOpDt blOpDt = DataBaseHelper.queryForBean("select PK_CGOP ,PK_ITEM  " +
                "from  BL_OP_DT BOD  where PK_PV=? and FLAG_PV='1' and PK_SETTLE is null", BlOpDt.class, pkPv);
        if (blOpDt == null) {
            return;
        }
        //需求7334
//        PiMaster piMaster=DataBaseHelper.queryForBean("select ID_NO " +
//                "from PI_MASTER PM " +
//                "         inner join PV_ENCOUNTER PE on PM.PK_PI = PE.PK_PI  " +
//                "where datediff(yy, BIRTH_DATE, DATE_BEGIN) > 60 and DT_IDTYPE='01' and ID_NO is not null  and PE.PK_PV=? ",PiMaster.class,pkPv);
//
//        if (piMaster!=null&& "29c5814fa395421e97d50bd709134f91".equals(blOpDt.getPkItem())){
//           //查询医保是否符合，
//            Map<String, Object> stringObjectHashMap=new HashMap<>(1);
//            stringObjectHashMap.put("mdtrt_cert_no",piMaster.getIdNo());
//            if (zsbaQGService.isPersonInfoFromZs(JsonUtil.writeValueAsString(stringObjectHashMap),user)){
//                DataBaseHelper.execute("delete BL_OP_DT where PK_CGOP=?", blOpDt.getPkCgop());
//                return;
//            }
//
//        }

        //走缓存形式，避免每次循环数据
        //包含数据（诊断+医嘱=药品）
        Map<String,List<String>> dataMap=getDataMap( param, user);
        List<String> contaitainlists = dataMap.get("contaitainlists");
        //仅有数据（诊断）
        List<String> onlyDiags =dataMap.get("onlyDiags");
        //仅有数据（医嘱=药品）
        List<String> onlyCnords =dataMap.get("onlyCnords");
        Boolean isEmtry = CollectionUtils.isEmpty(contaitainlists)
                && CollectionUtils.isEmpty(onlyDiags)
                && CollectionUtils.isEmpty(onlyCnords);
        if (isEmtry) {
            return;
        }
        List<Map<String, Object>> cnInf = ordDao.qryCnInf(pkPv);
        //啥都没开返回
        if (CollectionUtils.sizeIsEmpty(cnInf)) {
            return;
        }
        //处理包含逻辑
        if (!CollectionUtils.sizeIsEmpty(contaitainlists)) {
            if (cnInf.stream().anyMatch(vo -> contaitainlists.contains(MapUtils.getString(vo, "pkOrd")))) {
                DataBaseHelper.execute("delete BL_OP_DT where PK_CGOP=?", blOpDt.getPkCgop());
                return;
            }
        }
        //处理仅有诊断
        if (!CollectionUtils.sizeIsEmpty(onlyDiags)) {
            int diagNum = cnInf.stream().filter(vo -> "DIAG".equals(MapUtils.getString(vo, "type")) && onlyDiags.contains(MapUtils.getString(vo, "pkOrd")))
                    .collect(Collectors.toList()).size();
            Boolean isDiag = diagNum > 0 && diagNum >= cnInf.stream().filter(vo -> "DIAG".equals(MapUtils.getString(vo, "type"))).collect(Collectors.counting());
            if (isDiag) {
                DataBaseHelper.execute("delete BL_OP_DT where PK_CGOP=?", blOpDt.getPkCgop());
                return;
            }
        }
        //处理仅有医嘱/药品逻辑
        if (!CollectionUtils.sizeIsEmpty(onlyCnords)) {
            int  conNum = cnInf.stream().filter(vo -> "CN".equals(MapUtils.getString(vo, "type")) && onlyCnords.contains(MapUtils.getString(vo, "pkOrd")))
                    .collect(Collectors.toList()).size();
            Boolean isContatins= conNum > 0 && conNum >= cnInf.stream().filter(vo -> "CN".equals(MapUtils.getString(vo, "type"))).collect(Collectors.counting());
            if (isContatins) {
                DataBaseHelper.execute("delete BL_OP_DT where PK_CGOP=?", blOpDt.getPkCgop());
                return;
            }
        }
    }

    private Map<String, List<String>> getDataMap(String param, IUser user) {
        List<String> contaitainlists = JsonUtil.readValue(String.valueOf(RedisUtils.getCacheObj("cnop:Frees:containslists")),new TypeReference<List<String>>() {});
        //仅有数据（诊断）
        List<String> onlyDiags =JsonUtil.readValue(String.valueOf(RedisUtils.getCacheObj("cnop:Frees:onlyDiags")),new TypeReference<List<String>>() {});
        //仅有数据（医嘱=药品）
        List<String> onlyCnords = JsonUtil.readValue(String.valueOf(RedisUtils.getCacheObj("cnop:Frees:onlyCnords")),new TypeReference<List<String>>() {});
        Boolean isEmtry = CollectionUtils.isEmpty(contaitainlists)
                && CollectionUtils.isEmpty(onlyDiags)
                && CollectionUtils.isEmpty(onlyCnords);
        if (isEmtry){
            qryBlConsultationFree( param, user);
            contaitainlists = JsonUtil.readValue(String.valueOf(RedisUtils.getCacheObj("cnop:Frees:containslists")),new TypeReference<List<String>>() {});
            //仅有数据（诊断）
           onlyDiags =JsonUtil.readValue(String.valueOf(RedisUtils.getCacheObj("cnop:Frees:onlyDiags")),new TypeReference<List<String>>() {});
            //仅有数据（医嘱=药品）
            onlyCnords = JsonUtil.readValue(String.valueOf(RedisUtils.getCacheObj("cnop:Frees:onlyCnords")),new TypeReference<List<String>>() {});
        }
        Map<String,List<String>> dataMap=new HashMap<>(3);
        dataMap.put("contaitainlists",contaitainlists);
        dataMap.put("onlyDiags",onlyDiags);
        dataMap.put("onlyCnords", onlyCnords);
        return dataMap;

    }

    /**
     * @return void
     * @Description 获取免诊查费设置表数据到缓存中
     * @auther wuqiang
     * @Date 2021-03-20
     * @Param [param, user]
     */
    public void qryBlConsultationFree(String param, IUser user) {
        List<BlConsultationFree> blConsultationFrees = ordDao.qryBlCon(UserContext.getUser().getPkOrg());
        if (CollectionUtils.sizeIsEmpty(blConsultationFrees)) {
            return;
        }
        List<String> contaitainlists = new ArrayList<>(8);
        contaitainlists.addAll(blConsultationFrees.stream().filter(vo -> "2".equals(vo.getEuType()) && "1".equals(vo.getEuRel())).
                map(BlConsultationFree::getPkDiag).collect(Collectors.toList()));
        contaitainlists.addAll(blConsultationFrees.stream().filter(vo -> !"2".equals(vo.getEuType()) && "1".equals(vo.getEuRel())).
                map(BlConsultationFree::getPkOrd).collect(Collectors.toList()));
        List<String> onlyDiags = blConsultationFrees.stream().filter(vo -> "2".equals(vo.getEuType()) && "0".equals(vo.getEuRel())).
                map(BlConsultationFree::getPkDiag).collect(Collectors.toList());
        List<String> onlyCnords = blConsultationFrees.stream().filter(vo -> !"2".equals(vo.getEuType()) && "0".equals(vo.getEuRel())).
                map(BlConsultationFree::getPkOrd).collect(Collectors.toList());
        //去掉空数据
        contaitainlists.removeIf(Objects::isNull);
        onlyDiags.removeIf(Objects::isNull);
        onlyCnords.removeIf(Objects::isNull);
        RedisUtils.setCacheObj("cnop:Frees:containslists", contaitainlists, 360);
        RedisUtils.setCacheObj("cnop:Frees:onlyDiags", onlyDiags, 360);
        RedisUtils.setCacheObj("cnop:Frees:onlyCnords", onlyCnords, 360);
    }

    /**
     * 修改人员就诊信息
     * @param param
     * @param user
     * @return
     */
    public void savePvInfo(String param, IUser user) {
        Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
        if(mapParam==null)throw new BusException("未传入有效参数");
        if(mapParam.get("pkPv") == null){
            throw new BusException("pkPv参数为空！");
        }
        if(mapParam.get("weight") == null){
            throw new BusException("患者体重为空！");
        }
        DataBaseHelper.update("update PV_ENCOUNTER set WEIGHT=:weight where PK_PV = :pkPv " ,mapParam);
    }

    /**
     * 查询欠费患者信息
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryArrearsPvInfo(String param , IUser user){
        User u = UserContext.getUser();
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> searchPv = ordDao.searchPv(paramap);
        if(searchPv != null && searchPv.size() > 0) {
            //查询医保信息
            Set<String> pkPvs = new HashSet<String>();
            for (Map<String, Object> map : searchPv) {
                pkPvs.add(map.get("pkPv").toString());
            }
            StringBuffer str = new StringBuffer();
            str.append(" select pv.pk_inspv,pv.pk_pv,pv.med_type,dict.name as med_name,pv.dise_codg,pv.dise_name, ");
            str.append(" pv.birctrl_type,dict1.name as birctrl_name,pv.birctrl_matn_date,pv.matn_type,  ");
            str.append(" dict2.name as matn_name,pv.geso_val from ins_qgyb_pv pv");
            str.append(" left join ins_qgyb_dict dict on pv.med_type=dict.code and dict.code_type='med_type' ");
            str.append(" left join ins_qgyb_dict dict1 on pv.birctrl_type=dict1.code and dict1.code_type='birctrl_type' ");
            str.append(" left join ins_qgyb_dict dict2 on pv.matn_type=dict2.code and dict2.code_type='matn_type' ");
            str.append(" where pv.pk_pv in (");
            str.append(ArchUtil.convertSetToSqlInPart(pkPvs, "pk_pv"));
            str.append(") ");
            List<Map<String, Object>> qgybPvList = DataBaseHelper.queryForList(str.toString(), new Object[] {});
            if(qgybPvList != null && qgybPvList.size() > 0) {
                for (Map<String, Object> searchPvMap : searchPv) {
                    String pkPv = searchPvMap.get("pkPv").toString();
                    for (Map<String, Object> qgybPvMap : qgybPvList) {
                        String qgybPv = qgybPvMap.get("pkPv").toString();
                        if(pkPv.equals(qgybPv)) {
                            searchPvMap.put("pkInspv", CommonUtils.getPropValueStr(qgybPvMap, "pkInspv"));
                            searchPvMap.put("medType", CommonUtils.getPropValueStr(qgybPvMap, "medType"));
                            searchPvMap.put("medName", CommonUtils.getPropValueStr(qgybPvMap, "medName"));
                            searchPvMap.put("diseCodg", CommonUtils.getPropValueStr(qgybPvMap, "diseCodg"));
                            searchPvMap.put("diseName", CommonUtils.getPropValueStr(qgybPvMap, "diseName"));
                            searchPvMap.put("birctrlType", CommonUtils.getPropValueStr(qgybPvMap, "birctrlType"));
                            searchPvMap.put("birctrlName", CommonUtils.getPropValueStr(qgybPvMap, "birctrlName"));
                            searchPvMap.put("birctrlMatnDate", CommonUtils.getPropValueStr(qgybPvMap, "birctrlMatnDate"));
                            searchPvMap.put("matnType", CommonUtils.getPropValueStr(qgybPvMap, "matnType"));
                            searchPvMap.put("matnName", CommonUtils.getPropValueStr(qgybPvMap, "matnName"));
                            searchPvMap.put("gesoVal", CommonUtils.getPropValueStr(qgybPvMap, "gesoVal"));
                        }
                    }

                }
            }
        }
        return searchPv;

    }

    /**
     * 查询患者信息-模糊查询
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getPiPageList(String param, IUser user) {
        Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
        if(paramMap==null){
            throw new BusException("请传入对应参数！");
        }
        if(CommonUtils.isNull(paramMap.get("codePi")) && CommonUtils.isNull(paramMap.get("codeOp"))
                && CommonUtils.isNull(paramMap.get("codeIp"))
                && CommonUtils.isNull(paramMap.get("namePi"))
                && CommonUtils.isNull(paramMap.get("dtSex"))
                && CommonUtils.isNull(paramMap.get("idNo"))
                && CommonUtils.isNull(paramMap.get("pkPi"))
                && CommonUtils.isNull(paramMap.get("insurNo"))
                && CommonUtils.isNull(paramMap.get("mobile"))
                && CommonUtils.isNull(paramMap.get("cardNo"))
                && CommonUtils.isNull(paramMap.get("hicNo"))) {
            throw new BusException("交易号：【022006004005】条件不足，请输入更具体的查询条件！");
        }
        int pageIndex = CommonUtils.getInteger(paramMap.get("pageIndex"));
        int pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
        // 分页操作
        MyBatisPage.startPage(pageIndex, pageSize);
        List<PiMasterVo> voList=ordDao.qryPimaster(paramMap);
        // 分页完再查询年龄函数
        if (voList.size() > 0) {
            Set<String> pkPiSet = voList.parallelStream().map(PiMaster::getPkPi).collect(Collectors.toSet());
            List<PiMasterVo> masterVos = DataBaseHelper.queryForList("select PK_PI,GETPVAGE(BIRTH_DATE,null) as age_format from PI_MASTER where PK_PI in (" + CommonUtils.convertSetToSqlInPart(pkPiSet, "pk_pi") + ")"
                    , PiMasterVo.class);
            Map<String, String> piMasterVoMap = masterVos.parallelStream().filter(vo -> StringUtils.isNotBlank(vo.getAgeFormat())).collect(Collectors.toMap(PiMasterVo::getPkPi, PiMasterVo::getAgeFormat));
            voList.stream().forEach(vo -> {
                vo.setAgeFormat(piMasterVoMap.get(vo.getPkPi()));
            });
        }
        Page<List<PiMasterVo>> page = MyBatisPage.getPage();
        Map<String,Object> ret = new HashMap<>();
        ret.put("list",voList);
        ret.put("Count",page.getTotalCount());
        return ret;
    }

    public void PatiSave(String param , IUser user){
        PiMaster pait = JsonUtil.readValue(param, PiMaster.class);
        PvEncounter pven = JsonUtil.readValue(param, PvEncounter.class);
        PvIp pvip = JsonUtil.readValue(param, PvIp.class);
        if (pven.getPkPv() == null) {
            throw new BusException("请传入患者就诊主键！");
        }
        if (pait.getPkPi() == null) {
            throw new BusException("请传入患者主键！");
        }
        //更新患者信息
        String editPiSql = " update pi_master set dt_idtype=:dtIdtype,id_no = :idNo,name_pi = :namePi,dt_sex=:dtSex,birth_date=:birthDate,mobile = :mobile," +
                " addrcode_cur=:addrcodeCur,addr_cur=:addrCur,name_rel=:nameRel,dt_ralation=:dtRalation,tel_rel=:telRel,addr_rel=:addrRel,modifier=:modifier," +
                " dt_marry=:dtMarry,dt_occu=:dtOccu,dt_country=:dtCountry,dt_nation=:dtNation,tel_no=:telNo,unit_work=:unitWork,tel_work=:telWork," +
                " postcode_work=:postcodeWork,addrcode_birth=:addrcodeBirth ,addr_birth=:addrBirth,addrcode_origin=:addrcodeOrigin," +
                " addr_origin=:addrOrigin,addrcode_regi=:addrcodeRegi,addr_cur_dt=:addrCurDt,addr_regi=:addrRegi,addr_regi_dt=:addrRegiDt,postcode_regi=:postcodeRegi," +
                " postcode_cur=:postcodeCur,dt_idtype_rel=:dtIdtypeRel,idno_rel=:idnoRel where pk_pi=:pkPi";
        DataBaseHelper.update(editPiSql, pait);
        //更新患者就诊信息
        String editPvSql = " update pv_encounter set name_pi = :namePi,dt_sex=:dtSex,addrcode_cur=:addrcodeCur,addr_cur=:addrCur,addr_cur_dt=:addrCurDt,name_rel=:nameRel," +
                " dt_ralation=:dtRalation,tel_rel=:telRel,addr_rel=:addrRel,height=:height,weight=:weight,dt_spcdtype=:dtSpcdtype,modifier=:modifier,dt_marry=:dtMarry," +
                " unit_work=:unitWork,tel_work=:telWork,postcode_work=:postcodeWork ,addrcode_regi=:addrcodeRegi,addr_regi=:addrRegi,addr_regi_dt=:addrRegiDt," +
                " postcode_regi=:postcodeRegi,postcode_cur=:postcodeCur where pk_pv=:pkPv" ;
        DataBaseHelper.update(editPvSql, pven);
    }

    /**
     * 患者信息查询---修改查询
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> patiInfor(String param , IUser user){
        Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
        if(paramMap.get("pkPv")==null) {
            throw new BusException("请传入患者就诊主键！");
        }
        return ordDao.queryPati(paramMap);
    }

    /**
     * @Description 修改或保存患者就诊日志信息
     * @auther dengfei
     * @Date 2021-04-16
     * @Param [param, user]
     */
    public void savePvClinicRecord(String param, IUser user ){
        PvClinicRecord pvClinicRecord = JsonUtil.readValue(param,PvClinicRecord.class);

        if(StringUtils.isBlank( pvClinicRecord.getPkPvclirec())){
            ApplicationUtils.setDefaultValue( pvClinicRecord, true);
            DataBaseHelper.update(BuildSql.buildInsertSqlWithClass(PvClinicRecord.class),  pvClinicRecord);
        } else {
            ApplicationUtils.setDefaultValue( pvClinicRecord, false);
            DataBaseHelper.updateBeanByPk( pvClinicRecord,false);
            DataBaseHelper.update("update PV_CLINIC_RECORD set temperature=:temperature,bp=:bp where pk_pvclirec=:pkPvclirec",pvClinicRecord);
        }
    }

    /**
     * 医保门诊就诊
     * 004001005046
     */
    public Map<String,Object> medicalInsuranceOutpatientVisit(String param,IUser user){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
        String pkPv = MapUtils.getString(paramMap,"pkPv");
        if(StringUtils.isEmpty(pkPv)){
            return resultMap;
//			throw new BusException("缺少参数【pkPv】");
        }
        String sql = "select * from PV_ENCOUNTER where PK_PV = ?";
        //判断是否为医保患者
        PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{pkPv});
        if(pvEncounter == null){
            return resultMap;
//			throw new BusException(String.format("根据就诊主键无法获取患者信息【%s】"),pkPv);
        }

        String msg="";
        //21天内有入院通知单//判断住院是否是日间手术科室
        sql = "select dept.*\n" +
                "from bd_ou_dept dept\n" +
                "         inner join bd_dictattr att\n" +
                "                    on dept.pk_dept = att.pk_dict\n" +
                "         inner join bd_dictattr_temp tmp\n" +
                "                    on att.pk_dictattrtemp = tmp.pk_dictattrtemp\n" +
                "         inner join pv_ip_notice nt\n" +
                "                    on nt.pk_dept_ip = dept.pk_dept\n" +
                "where tmp.code_attr='0601'\n" +
                "  and att.val_attr='1'\n" +
                "  and nt.eu_status ='3'\n" +
                "  and nt.pk_pi=?\n";
        if (Application.isSqlServer()) {
            sql+="  and nt.date_admit >= GETDATE() - 14 \n";
        }else{
            sql+="  and nt.date_admit >= sysdate - 14 \n";
        }

        BdOuDept bdOuDept = DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{pvEncounter.getPkPi()});
        if(bdOuDept!=null){
            //是日间手术科室
            resultMap.put("code","200");
            resultMap.put("msg","该患者14天内曾在日间手术病房住院，请按照日间手术术后随访流程进行诊疗!");
            msg="该患者14天内曾在日间手术病房住院，请按照日间手术术后随访流程进行诊疗!";
        }

        String pkInsu = pvEncounter.getPkInsu();
        if(StringUtils.isEmpty(pkInsu)){
            return resultMap;
//			throw new BusException("门诊医保就诊患者无医保主键PK_INSU");
        }
        sql = "select * from BD_HP where PK_HP = ?";
        BdHp bdHp = DataBaseHelper.queryForBean(sql,BdHp.class,new Object[]{pkInsu});
        if(bdHp == null){
            return resultMap;
//			throw new BusException(String.format("根据医保主键无法获取医保信息【%s】"),pkInsu);
        }
        if(StringUtils.isEmpty(bdHp.getDtExthp())){
            return resultMap;
//			throw new BusException("该患者非医保患者");
        }
        //查询21天内是否有入院通知单
        String pkPi = pvEncounter.getPkPi();
        if(StringUtils.isEmpty(pkPi)){
            return resultMap;
//			throw new BusException("患者主键为空");
        }
        String pkHp = bdHp.getPkHp();
        if(StringUtils.isEmpty(pkHp)){
            return resultMap;
//			throw new BusException("医保计划主键为空");
        }

        sql = "select *\n" +
                "from pv_ip_notice nt\n" +
                "where nt.pk_pi = ? and nt.date_admit >= GETDATE() - 21 order by date_admit desc";
        List<PvIpNotice> pvIpNoticeList = DataBaseHelper.queryForList(sql,PvIpNotice.class,new Object[]{pkPi});
        //查询21天内是否开立过入院通知单
        if(pvIpNoticeList.size() <=0){
            //21天内没有开立入院通知单//查询患者是否在院
            medicarePatients(pkPi);
        }else {
            //21天内有入院通知单//判断住院是否是日间手术科室
            sql = "select dept.*\n" +
                    "from bd_ou_dept dept\n" +
                    "         inner join bd_dictattr att\n" +
                    "                    on dept.pk_dept = att.pk_dict\n" +
                    "         inner join bd_dictattr_temp tmp\n" +
                    "                    on att.pk_dictattrtemp = tmp.pk_dictattrtemp\n" +
                    "         inner join pv_ip_notice nt\n" +
                    "                    on nt.pk_dept_ip = dept.pk_dept\n" +
                    "where tmp.code_attr='0601'\n" +
                    "  and att.val_attr='1'\n" +
                    "  and nt.eu_status not in ('0','9')\n" +
                    "  and nt.date_admit >= GETDATE() - 21 \n"+
                    "  and nt.pk_pi=?\n";
            bdOuDept = DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{pkPi});
            if(bdOuDept==null){
                //不是日间手术科室
                medicarePatients(pkPi);
            }else {
                //是日间手术科室
                resultMap.put("code","200");
                if("".equals(msg)){
                    resultMap.put("msg","该患者为日间手术病人，请勿开具日间手术相关的检查治疗处方；手术相关问题请让患者到手术专科就诊");
                }else{
                    resultMap.put("msg","该患者为日间手术病人，14天内曾在日间手术病房住院，请按照日间手术术后随访流程进行诊疗! 请勿开具日间手术相关的检查治疗处方；手术相关问题请让患者到手术专科就诊");
                }

            }
        }
        return resultMap;
    }


    /**
     * 医保患者逻辑
     * 根据入院通知单查询（不限时间，只查询是否有入院状态记录）患者是否在院
     */
    private void medicarePatients(String pkPi){

        String sql = "select * from PV_ENCOUNTER where EU_PVTYPE='3' and (EU_STATUS ='1' or flag_in='1') and pk_pi=?";
        List<PvEncounter> pvEncounters = DataBaseHelper.queryForList(sql,PvEncounter.class,new Object[]{pkPi});
        if(pvEncounters.size() > 0){
            //有在院的记录
            //判断是否是门诊特定病种住院(ins_qgyb_pv.med_type=14),计生（51），产前医保（5301）。

            String pkPvs = CommonUtils.convertListToSqlInPart(pvEncounters.stream().map(n->n.getPkPv()).collect(Collectors.toList()));
            sql = "select *  from ins_qgyb_pv where pk_pv in ("+pkPvs+") and med_type  in ('14','51','5301') ";
            List<InsQgybPV> insQgybPVList = DataBaseHelper.queryForList(sql,InsQgybPV.class);
            if(insQgybPVList.size()>0){
                //是门诊特定病种住院,正常就

            }else {
                throw new BusException("住院患者，住院期间禁止门诊就诊");
            }
        }
    }

    /***
     * 根据Pkpi查询当前患者的生理状态数据
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPhycalData(String param, IUser user) {
        //pkPi
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPi = paramMap.get("pkPi") != null ? paramMap.get("pkPi").toString() : "";
        if (StringUtils.isBlank(pkPi)) throw new BusException("传参pkPi为空！");
        List<Map<String, Object>> histOrderlist = ordDao.qryPhycalData(paramMap);
        return histOrderlist;
    }

    /**
     * @Description 保存
     * @Date 2021-04-12
     * @Param [param, user]
     */
    public void savePPhycalData(String param, IUser user ){
        PiPhyVo phyVo = JsonUtil.readValue(param, PiPhyVo.class);
        List<PiPhysiological> addList = phyVo.getAddList();
        List<PiPhysiological> editList = phyVo.getEditList();

        if(editList != null && editList.size()>0){
            for (PiPhysiological vo: editList) {
                ApplicationUtils.setDefaultValue(vo, false);
            }
            DataBaseHelper.batchUpdate(BuildSql.buildUpdateSqlWithClass(PiPhysiological.class),editList);
            //DataBaseHelper.batchUpdate("update PI_PHYSIOLOGICAL set dt_physi=:dtPhysi,desc_physi=:descPhysi,date_begin=:dateBegin,date_end=:dateEnd,date_rec=:dateRec,pk_emp_rec=:pkEmpRec,name_emp_rec=:nameEmpRec,note=:note where pk_piphysi=:pkPiphysi",editList);
        }
        if(addList != null && addList.size()>0){
            for (PiPhysiological vo: addList) {
                ApplicationUtils.setDefaultValue(vo, true);
            }
            DataBaseHelper.batchUpdate(BuildSql.buildInsertSqlWithClass(PiPhysiological.class),addList);
        }
    }

    public void delPhycal(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPiphysi = paramMap.get("pkPiphysi") != null ? paramMap.get("pkPiphysi").toString() : "";
        if (StringUtils.isBlank(pkPiphysi)) throw new BusException("传参pkPiphysi为空！");
        DataBaseHelper.update("update PI_PHYSIOLOGICAL set del_flag='1' where pk_piphysi='"+pkPiphysi+"'");
    }
    
    /**
     * 交易号：022003021007
     * 查询入院通知单集合
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> getPvIpNoticeList(String param,IUser user) {
    	Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
    	return ordDao.qryPvIpNoticeList(paramMap);
    }

    /**
     * 诊毕时患者信息保存
     * @param param
     * @param user
     */
    public void ClinicPatiSave(String param , IUser user){
        PiMaster pait=JsonUtil.readValue(param, PiMaster.class);
        PvEncounter pven=JsonUtil.readValue(param, PvEncounter.class);
        InsQgybPV ins=JsonUtil.readValue(param,InsQgybPV.class);
        Map<String,Object> pvOp=JsonUtil.readValue(param, Map.class);
        User u=(User) user;
        if(pven.getPkPv()==null) {
            throw new BusException("请传入患者就诊主键！");
        }
        if(pait.getPkPi()==null) {
            throw new BusException("请传入患者主键！");
        }

        String ybpvSql="select count(1) from ins_qgyb_pv where pk_pv=?";
        int countpv=DataBaseHelper.queryForScalar(ybpvSql,Integer.class,new Object[]{pven.getPkPv()});

        //医保信息
        if(StringUtils.isBlank(ins.getPkInspv()) && countpv==0){ //新增
            ins.setPkOrg(u.getPkOrg());
            ins.setPkPi(pven.getPkPi());
            ins.setPkPv(pven.getPkPv());
            ins.setPkHp(pven.getPkInsu());
            ins.setDelFlag("0");
            ins.setCreator(u.getPkEmp());
            ins.setCreateTime(new Date());
            ins.setTs(new Date());
            DataBaseHelper.insertBean(ins);
        }else{
            DataBaseHelper.update(" update ins_qgyb_pv set med_type=:medType,dise_codg=:diseCodg,dise_name=:diseName,birctrl_type=:birctrlType,birctrl_matn_date=:birctrlMatnDate,matn_type=:matnType,geso_val=:gesoVal where pk_inspv=:pkInspv",ins);
        }

        /*** 修改病情描述信息 start ***/
        String sql="";

        //是否有医嘱
        if(Application.isSqlServer()){
            sql = "select top 1 * from CN_EMR_OP where PK_PV = ? order by ts desc ";
        }else{
            sql = "select * from CN_EMR_OP where PK_PV = ? and rownum=1 order by ts desc ";
        }
        Map<String,Object> rtn=DataBaseHelper.queryForMap(sql,new Object[]{pven.getPkPv()});

        if(StringUtils.isNotEmpty(MapUtils.getString(rtn,"present")) ||  StringUtils.isNotEmpty(MapUtils.getString(rtn,"problem"))){
            sql="update CN_RIS_APPLY set note_dise=? where pk_ordris in (select b.pk_ordris from CN_ORDER a inner join CN_RIS_APPLY b on a.pk_cnord=b.pk_cnord where a.PK_PV=? and ( b.note_dise is null or   ltrim(rtrim(b.note_dise)) ='') )";
            String present=MapUtils.getString(rtn,"present");
            String problem = MapUtils.getString(rtn,"problem");
            if(StringUtils.isNotEmpty(present) && StringUtils.isNotEmpty(problem)){
                if(problem.contains(present)){
                    DataBaseHelper.update(sql,new Object[]{problem,pven.getPkPv()});
                }else {
                    DataBaseHelper.update(sql,new Object[]{problem+ "\r\n"+present,pven.getPkPv()});
                }
            }else if(StringUtils.isNotEmpty(present)){
                DataBaseHelper.update(sql,new Object[]{present,pven.getPkPv()});
            }else if(StringUtils.isNotEmpty(problem)){
                DataBaseHelper.update(sql,new Object[]{problem,pven.getPkPv()});
            }
        }
    }

    public List<Map<String,Object>> medicarePatients(String param,IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
        String pkPi = MapUtils.getString(paramMap,"pkPi");
        String sql ="select pv.pk_pv, pv.NAME_PI,pi.DT_SEX,pi.DT_IDTYPE,pi.ID_NO,pi.CODE_OP,pi.CODE_IP,pvip.IP_TIMES,pv.DATE_ADMIT,pv.PK_DEPT,pv.note from PV_ENCOUNTER pv inner join PI_MASTER pi on pv.PK_PI = pi.PK_PI " +
                "inner join PV_IP pvip on pvip.PK_PV = pv.PK_PV " +
                "where pi.PK_PI=? and pv.EU_PVTYPE='3' and (pv.EU_STATUS ='1' or pv.flag_in='1')";
        //String sql = "select * from PV_ENCOUNTER where EU_PVTYPE='3' and (EU_STATUS ='1' or flag_in='1') and pk_pi=?";
        List<Map<String,Object>> pvEncounters = DataBaseHelper.queryForList(sql,new Object[]{pkPi});
        if(pvEncounters.size() > 0){
            //有在院的记录
            //判断是否是门诊特定病种住院(ins_qgyb_pv.med_type=14),计生（51），产前医保（5301）。
            String pkPvs = CommonUtils.convertListToSqlInPart(pvEncounters.stream().map(n->n.get("pkPv").toString()).collect(Collectors.toList()));
            sql = "select *  from ins_qgyb_pv where pk_pv in ("+pkPvs+") and med_type  in ('14','51','5301') ";
            List<InsQgybPV> insQgybPVList = DataBaseHelper.queryForList(sql,InsQgybPV.class);
            if(insQgybPVList.size()>0){
                //是门诊特定病种住院,正常就
                return null;
            }
        }
        return pvEncounters;
    }

    /**
     * 根据接诊服务生成诊查费
     *
     * @param param
     * @param user
     */
    public void saveBlReg(String param, IUser user) throws ParseException {
        PiOpVo piOpVo = JsonUtil.readValue(param, PiOpVo.class);
        if (piOpVo == null || StringUtils.isEmpty(piOpVo.getPkPv()) || StringUtils.isEmpty(piOpVo.getPkSchsrv())) {
            throw new BusException("请传入患者就诊和资源主键！");
        }
        //查询患者就诊信息
        PvEncounter pvInfo = DataBaseHelper.queryForBean("SELECT pv.*,pi.ID_NO FROM PV_ENCOUNTER pv inner join pi_master pi on pv.pk_pi = pi.pk_pi WHERE pv.pk_pv=? and pv.del_flag ='0' ", PvEncounter.class, new Object[]{piOpVo.getPkPv()});
        if (pvInfo == null) {
            throw new BusException("未获取到患者的有效就诊信息！");
        }
        if (pvInfo.getPkDept() == null) {
            throw new BusException("未获取到患者就诊科室信息！");
        }
        if (pvInfo.getPkEmpPhy() == null) {
            throw new BusException("未获取到患者接诊医生信息！");
        }


        blReg(piOpVo,pvInfo);

    }

    public static List<String> getDictCode(String field) {
        String str = ApplicationUtils.getPropertyValue(field, "");
        if (org.springframework.util.StringUtils.isEmpty(str)) {
            return null;
        }
        List<String> list = Arrays.asList(str.split(","));
        return list;
    }
    private void blReg(PiOpVo piOpVo,PvEncounter pvInfo){
        if(StringUtils.isEmpty(pvInfo.getPkDeptArea())){
            pvInfo.setPkDeptArea(piOpVo.getPkDeptArea());
        }
        String endTime = ApplicationUtils.getSysparam("PV0055", false);//再次接诊免接诊费的有效时间;
        if (StringUtils.isEmpty(endTime)) {
            endTime = "48";
        }
        int endTimeInt = 48;
        try {
            endTimeInt = Integer.parseInt(endTime);
        } catch (Exception e) {
            throw new BusException("请正确维护PV0055参数格式！");
        }

        //bug 35226 病人ID 6681331， 3月30日就诊了李志荣医生,重复计算诊疗费问题
        //对比的时间不能是当前时间，首先应该为和就诊的开始时间进行对比      BL0061: 1,接诊时计费，2.诊毕时计费
        //判断48小时内有无收费记录
        Date  compareDate = pvInfo.getDateBegin();
        if(null == compareDate ) {
            compareDate = new Date();
        }
        String time = DateUtils.addDate(compareDate, -endTimeInt, 4, "yyyy-MM-dd HH:mm:ss");
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT BL.date_hap,PV.PK_DEPT,pv.PK_EMP_PHY FROM BL_OP_DT bl INNER JOIN PV_ENCOUNTER pv on PV.pk_pv=BL.pk_pv");
        sql.append(" WHERE bl.flag_pv='1' and bl.del_flag ='0'  and PV.PK_DEPT=? and PV.PK_EMP_PHY=? AND BL.PK_PI=? ");
        sql.append(" and BL.DATE_CG>= to_date(?,'YYYY-MM-DD HH24:MI:SS')");
        List<SchOrPv> listBl = DataBaseHelper.queryForList(sql.toString(), SchOrPv.class, new Object[]{pvInfo.getPkDept(), pvInfo.getPkEmpPhy(), pvInfo.getPkPi(), time});
        if (null != listBl && !listBl.isEmpty()) {
            return;
        }
        //判断当前患者是否只开立核酸检测
        List<String> list = getDictCode(reqCodeCg);
        if (list != null && list.size() > 0) {
            boolean onlyHS = true; //默认全是核酸检测
            sql.delete(0, sql.length());
            sql.append(" select DISTINCT b.CODE from BL_OP_DT a INNER JOIN BD_ITEM b on a.PK_ITEM=b.PK_ITEM where a.pk_pv=? ");
            List<Map<String, Object>> opDts = DataBaseHelper.queryForList(sql.toString(), new Object[]{pvInfo.getPkPv()});
            if (opDts != null && opDts.size() > 0) {
                for (Map<String, Object> opdt : opDts) {
                    if (!list.contains(MapUtils.getString(opdt, "code"))) {
                        onlyHS = false;
                        break;
                    }
                }
            } else { //没有收费项目，不过滤
                onlyHS = false;
            }
            //只有核酸检测
            if (onlyHS) {
                return;
            }
        }
        sql.delete(0, sql.length());
        sql.append("select PK_EMP from BD_OU_EMPLOYEE where del_flag='0' and IDNO =?");
        List<Map<String, Object>> emps = DataBaseHelper.queryForList(sql.toString(), new Object[]{pvInfo.getIdNo()});
        String dtsrvType = "0";
        if (emps != null && emps.size() > 0) {
            dtsrvType = "1";
        }
        sql.delete(0,sql.length());

        sql.append("select item.pk_item, ord.pk_ord,bdord.name,bdord.code_ordtype,srv.PK_ITEM_SPEC , bdord.FLAG_CG    from SCH_SRV srv ");
        sql.append("INNER JOIN sch_srv_ord ord on ORD.PK_SCHSRV=SRV.PK_SCHSRV INNER JOIN bd_ord bdord on bdord.pk_ord = ord.pk_ord and (ord.eu_type is null or ord.eu_type =?)");
        sql.append("INNER JOIN bd_ord_item item on ord.pk_ord=item.pk_ord and item.del_flag='0' ");
        sql.append("where srv.PK_SCHSRV=?");
        List<SchOrPv> srvMap = DataBaseHelper.queryForList(sql.toString(), SchOrPv.class, new Object[]{dtsrvType,piOpVo.getPkSchsrv()});
        if (srvMap == null || srvMap.size() <= 0) {
            throw new BusException("依据就诊信息未获取到就诊服务！");
        }
//        if ("0".equals(srvMap.getFlagCg())){
//            return;
//        }
        //查询当前医生考勤科室
        BdOuEmpjob emp = DataBaseHelper.queryForBean(
                "SELECT * FROM bd_ou_empjob WHERE pk_emp =? AND is_main = '1' ", BdOuEmpjob.class, new Object[]{UserContext.getUser().getPkEmp()});
        String pkDeptJob = emp != null && StringUtils.isNotBlank(emp.getPkDept()) ? emp.getPkDept() : null;

        //组装计费参数
        List<BlPubParamVo> blOpCgList = new ArrayList<BlPubParamVo>();
        //门诊特需服务费
        if(StringUtils.isNotEmpty(srvMap.get(0).getPkItemSpec())){
            BlPubParamVo vo1 = opItemToOpCg(pvInfo, srvMap.get(0), pkDeptJob);
            blOpCgList.add(vo1);
        }
        List<String> pkItems = new ArrayList<String>();
        for (SchOrPv sch:srvMap) {
            if("0".equals(sch.getFlagCg()))continue;
            if("1".equals(piOpVo.getFlagZshp()) && "1".equals(piOpVo.getFlagOldMan()) && "29c5814fa395421e97d50bd709134f91".equals(sch.getPkItem())){
                continue;
            }
            if(!pkItems.contains(sch.getPkItem())){
                BlPubParamVo vo = opOrdToOpCg(pvInfo, sch, pkDeptJob);
                blOpCgList.add(vo);
                pkItems.add(vo.getPkItem());
            }

        }
        if(null !=blOpCgList && !blOpCgList.isEmpty()) {
        	BlPubReturnVo retVo = opCgPubService.blOpCgBatch(blOpCgList);
            //修改诊查费用标记
            if (retVo != null && retVo.getBods() != null && retVo.getBods().size() > 0) {
                DataBaseHelper.batchUpdate("update BL_OP_DT set flag_pv='1' where PK_CGOP=:pkCgop", retVo.getBods());
            }
        }       
    }

    public BlPubParamVo opOrdToOpCg(PvEncounter pvInfo, SchOrPv srvMap, String pkDeptJob) {
        User u = UserContext.getUser();
        String pkOrg = u.getPkOrg();
        String pkDept = u.getPkDept();
        BlPubParamVo bpb = new BlPubParamVo();
        bpb.setPkOrg(pkOrg);
        bpb.setEuPvType(pvInfo.getEuPvtype());
        bpb.setPkPv(pvInfo.getPkPv());
        bpb.setPkPi(pvInfo.getPkPi());
        bpb.setDateStart(new Date());
        bpb.setCodeOrdtype(srvMap.getCodeOrdtype()); //编码类型
        bpb.setPkOrd(srvMap.getPkOrd()); //医嘱主键
        bpb.setPkItem(srvMap.getPkItem());
        bpb.setQuanCg(1d);
        bpb.setPkOrgEx(u.getPkOrg());
        bpb.setPkOrgApp(u.getPkOrg());
        bpb.setPkDeptEx(u.getPkDept());
        bpb.setPkDeptApp(u.getPkDept());
        bpb.setPkEmpApp(u.getPkEmp());
        bpb.setNameEmpApp(u.getNameEmp());
        bpb.setFlagPd("0");
        bpb.setNamePd(srvMap.getName()); //医嘱项目名称
        bpb.setFlagPv("1");
        //bpb.setPkUnitPd(opOrd.getPkUnitCg()); //医嘱计费单位
        //bpb.setPackSize(opOrd.getPackSize().intValue()); //解放包装
        // bpb.setPrice(opOrd.getPriceCg()); //价格
        bpb.setDateHap(new Date());
        bpb.setPkDeptCg(pkDept);
        bpb.setPkEmpCg(u.getPkEmp());
        bpb.setNameEmpCg(u.getNameEmp());
        bpb.setEuAdditem("0"); //非附加费
        bpb.setPkDeptJob(pkDeptJob);
        bpb.setPkDeptAreaapp(pvInfo.getPkDeptArea());
        return bpb;
    }

    public BlPubParamVo opItemToOpCg(PvEncounter pvInfo, SchOrPv srvMap, String pkDeptJob) {
        User u = UserContext.getUser();
        String pkOrg = u.getPkOrg();
        String pkDept = u.getPkDept();
        BlPubParamVo bpb = new BlPubParamVo();
        bpb.setPkOrg(pkOrg);
        bpb.setEuPvType(pvInfo.getEuPvtype());
        bpb.setPkPv(pvInfo.getPkPv());
        bpb.setPkPi(pvInfo.getPkPi());
        bpb.setDateStart(new Date());
        bpb.setCodeOrdtype(srvMap.getCodeOrdtype()); //编码类型
        bpb.setPkOrd(null); //医嘱主键
        bpb.setPkItem(srvMap.getPkItemSpec());//计费项目主键
        bpb.setQuanCg(1d);
        bpb.setPkOrgEx(u.getPkOrg());
        bpb.setPkOrgApp(u.getPkOrg());
        bpb.setPkDeptEx(u.getPkDept());
        bpb.setPkDeptApp(u.getPkDept());
        bpb.setPkEmpApp(u.getPkEmp());
        bpb.setNameEmpApp(u.getNameEmp());
        bpb.setFlagPd("0");
        bpb.setNamePd(srvMap.getName()); //医嘱项目名称
        bpb.setFlagPv("1");
        bpb.setDateHap(new Date());
        bpb.setPkDeptCg(pkDept);
        bpb.setPkEmpCg(u.getPkEmp());
        bpb.setNameEmpCg(u.getNameEmp());
        bpb.setEuAdditem("0"); //非附加费
        bpb.setPkDeptJob(pkDeptJob);
        bpb.setPkDeptAreaapp(pvInfo.getPkDeptArea());
        return bpb;
    }
}

