package com.zebone.nhis.scm.ipdedrug.service;

import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdDe;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.PiAddress;
import com.zebone.nhis.common.module.scm.pub.BdPdDecate;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.scm.ipdedrug.dao.IpDeDrugMapper;
import com.zebone.nhis.scm.ipdedrug.dao.IpPdDrugPackMapper;
import com.zebone.nhis.scm.ipdedrug.vo.IpDeDrugDto;
import com.zebone.nhis.scm.pub.service.DeDrugExtPubService;
import com.zebone.nhis.scm.pub.service.IpDeDrugPubService;
import com.zebone.nhis.scm.pub.service.PdStInPubService;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.nhis.scm.pub.vo.PdInParamVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <a href="http://email.163.com/"><font color="red"
 * size="3">网易邮箱gongxingyao2012@163.com</font></a> <br/>
 * 住院医嘱发药服务
 *
 * @author Administrator <br />
 */
@Service
public class IpPdDeDrugService {

    @Autowired
    private IpDeDrugMapper ipDeDrugMapper;

    @Autowired
    private IpDeDrugPubService ipDeDrugPubService;

    @Autowired
    private DeDrugExtPubService deDrugExtPubService;

    @Autowired
    private IpPdDrugPackMapper drugPackMapper;

    @Autowired
    private PdStInPubService pdStInPubService;

    private Logger logger = LoggerFactory.getLogger("nhis.scm");

    private static final String ChrtCodeDecate ="115";

    private static final String TpnCodeDecate ="126";
    /**
     * 临时放在此处，因为就一个方法没有单独定义service
     * <p>
     * 保存物品发放分类信息
     *
     * @param param
     * @param user
     * @return
     */
    public int savePdDeCate(String param, IUser user) {
        BdPdDecate bdPdDecate = JsonUtil.readValue(param, BdPdDecate.class);
        int count = 0;
        if (bdPdDecate.getPkPddecate() != null) {
            ApplicationUtils.setDefaultValue(bdPdDecate, false);
            count = DataBaseHelper.updateBeanByPk(bdPdDecate, false);
        } else {
            ApplicationUtils.setDefaultValue(bdPdDecate, true);
            count = DataBaseHelper.insertBean(bdPdDecate);
        }
        return count;
    }

    /**
     * 查询发药明细
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queryDeDrugDetail(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkDept(userCur.getPkDept());
        if(!CommonUtils.isEmptyString(ipDeDrugDto.getQueryCode())){
            ipDeDrugDto.setQueryCode(ipDeDrugDto.getQueryCode().toUpperCase());
        }
        boolean isPage = StringUtils.isBlank(ipDeDrugDto.getPage()) || EnumerateParameter.ONE.equals(ipDeDrugDto.getPage());
        if(isPage){
            int pageIndex = CommonUtils.getInteger(ipDeDrugDto.getPageIndex());
            int pageSize = CommonUtils.getInteger(ipDeDrugDto.getPageSize());
            MyBatisPage.startPage(pageIndex, pageSize);
        }

        List<Map<String, Object>> mapResult = ipDeDrugMapper.queryDeDrugDetail(ipDeDrugDto);
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("deList", mapResult);

        if(isPage){
            Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
            resMap.put("TotalCount", page.getTotalCount());
        }
        return resMap;
    }

    /**
     * 查询请领单（领药单查询）
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Map<String, Object>> queryApDrugList(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        StringBuffer sql = new StringBuffer();
        sql.append("select ap.eu_direct, dept.name_dept,ap.date_ap,ap.flag_finish,ap.pk_pdap from bd_ou_dept dept inner join ex_pd_apply ap on dept.pk_dept=ap.pk_dept_ap");
        sql.append(" where ap.eu_aptype=? and ap.eu_status=? and  ap.pk_dept_de=?  and ap.date_ap >= ? and ap.date_ap <= ? ");
        List args = new ArrayList();
        args.add(ipDeDrugDto.getEuAptype());
        args.add(ipDeDrugDto.getEuStatus());
        args.add(userCur.getPkDept());
        args.add(ipDeDrugDto.getDateStart());
        args.add(ipDeDrugDto.getDateEnd());
        if (!StringUtils.isEmpty(ipDeDrugDto.getPkAppDeptNs())) {
            sql.append(" and pk_dept_ap= ? ");
            args.add(ipDeDrugDto.getPkAppDeptNs());
        }
        if (!StringUtils.isEmpty(ipDeDrugDto.getCodeAp())) {
            sql.append(" and code_apply= ? ");
            args.add(ipDeDrugDto.getCodeAp());
        }
        List<Map<String, Object>> mapResult = DataBaseHelper.queryForList(sql.toString(), args.toArray());
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 查询请领明细
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryApDrugDetail(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        List<Map<String, Object>> mapResult = ipDeDrugMapper.queryApDrugDetail(ipDeDrugDto);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 根据请领单查询发药明细
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryDeDetailByAp(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        if (null == ipDeDrugDto.getPkPdapDt()) {
            throw new BusException("未获取到申请单明细主键");
        }
        User userCur = (User) user;
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        List<Map<String, Object>> mapResult = ipDeDrugMapper.queryDeDetailByAp(ipDeDrugDto);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 发药请领单查询(处方，医嘱，科室)
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpAppListByCDT(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkDeptDe", userCur.getPkDept());
        if (ipDeDrugDto.getDateStart() != null) {
            paramMap.put("dateStart", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.getDateMorning(ipDeDrugDto.getDateStart(), 0)));
        }
        if (ipDeDrugDto.getDateEnd() != null) {
            paramMap.put("dateEnd", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.getDateMorning(ipDeDrugDto.getDateEnd(), 1)));
        }
        paramMap.put("pkDeptAp", ipDeDrugDto.getPkAppDeptNs());
        paramMap.put("flagDept", ipDeDrugDto.getFlagDept());
        paramMap.put("isPres", ipDeDrugDto.getIsPres());
        paramMap.put("euDirect", ipDeDrugDto.getEuDirect());
        paramMap.put("deCateWhereSql", ipDeDrugDto.getDeCateWhereSql());
        List<Map<String, Object>> mapResult = ipDeDrugMapper.qryExPdApplyList(paramMap);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 根据条件查询药品请领明细(医嘱和处方共用)
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpDeDrugDetailByCDT(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkPdStock(userCur.getPkStore());
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        List<Map<String, Object>> mapResult = null;
        if (EnumerateParameter.ONE.equals(ipDeDrugDto.getIsPres())) {
            mapResult = ipDeDrugMapper.qryIpDePresDrugDetailByCDT(ipDeDrugDto);
        } else {
            mapResult = ipDeDrugMapper.qryAppListDetail(ipDeDrugDto);
        }
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 根据条件查询发退药打印记录
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpDeDrugPrtRecordByCDT(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkDept(userCur.getPkDept());
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        //判断当前是医嘱发药记录还是处方发药记录
        String sql = "select count(1) from ex_pd_de epd inner join cn_order co on co.pk_cnord=epd.pk_cnord where co.pk_pres is not null and epd.code_de=?";
        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class, ipDeDrugDto.getCodeDe());
        if (count > 0) {//处方发药记录
            ipDeDrugDto.setIsPres("1");
        }
        List<Map<String, Object>> mapResult = ipDeDrugMapper.qryDeDrugPrintRecord(ipDeDrugDto);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 查询请领单主键（科室领药）
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryApDrugDetailDept(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkDept(userCur.getPkDept());
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        ipDeDrugDto.setPkStore(userCur.getPkStore());
        List<Map<String, Object>> mapResult = ipDeDrugMapper.queryApDrugDetailDept(ipDeDrugDto);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 根据条件查询发药单
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryDeDrugList(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkDept(userCur.getPkDept());
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        List<Map<String, Object>> mapResult = ipDeDrugMapper.queryDeDrugList(ipDeDrugDto);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 住院(医嘱，处方)发药处理
     *
     * @param param
     * @param user
     */
    public String mergeIpDeDrug(String param, IUser user) {
        // 前台传过来是勾选的请领明细
        List<ExPdApplyDetail> exPdAppDetails = JsonUtil.readValue(param, new TypeReference<List<ExPdApplyDetail>>() {
        });
        if (exPdAppDetails == null || exPdAppDetails.size() <= 0) {
            throw new BusException("未获取到本次发药明细信息！");
        }
        List<ExPdApplyDetail> extDtlist = new ArrayList<ExPdApplyDetail>();
        List<ExPdApplyDetail> dtlist = new ArrayList<ExPdApplyDetail>();
        for (ExPdApplyDetail dt : exPdAppDetails) {
            if ("9".equals(dt.getEuDetype())) {
                extDtlist.add(dt);
            } else {
                dtlist.add(dt);
            }
        }
        User u = (User) user;
        String codeDe = null;
        if (dtlist != null && dtlist.size() > 0) {
            List<PdDeDrugVo> drugVo = ipDeDrugPubService.mergeIpDeDrug(exPdAppDetails, "0", "1", null, new Date());
            if (drugVo != null && drugVo.size() != 0)
                codeDe = drugVo.get(0).getCodeDe();
            try {
                //调用接口发送药品至包药机
                ExtSystemProcessUtils.processExtMethod("PackMachine", "sendToMah", drugVo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (extDtlist != null && extDtlist.size() > 0) {
            if (CommonUtils.isEmptyString(codeDe))
                codeDe = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_IPDS);
            deDrugExtPubService.ipDeDrug(extDtlist, u.getPkStore(), codeDe, new Date(), u, param);
        }
        //调用接口住院静配接口
        ExtSystemProcessUtils.processExtMethod("PIVAS", "saveTorders", exPdAppDetails);
        return codeDe;
    }

    /**
     * 住院(医嘱，处方)发药处理
     * 根据科室拆分统一发药主要涉及出院处方发药
     * @param param
     * @param user
     */
    public String mergeIpDeDrugByDept(String param, IUser user) { // 前台传过来是勾选的请领明细
        List<ExPdApplyDetail> exPdAppDetails = JsonUtil.readValue(param, new TypeReference<List<ExPdApplyDetail>>() {});
        if (exPdAppDetails == null || exPdAppDetails.size() <= 0) {
            throw new BusException("未获取到本次发药明细信息！");
        }

        Map<String,List<ExPdApplyDetail>> deptApDtList=new HashMap<String, List<ExPdApplyDetail>>();

        List<ExPdApplyDetail> extDtlist = new ArrayList<ExPdApplyDetail>();
        for (ExPdApplyDetail dt : exPdAppDetails) {
            if ("9".equals(dt.getEuDetype())) {
                extDtlist.add(dt);
            } else {
                if(deptApDtList.containsKey(dt.getPkDeptAp())){
                    deptApDtList.get(dt.getPkDeptAp()).add(dt);
                }else{
                    List<ExPdApplyDetail> temp_list=new ArrayList<ExPdApplyDetail>();
                    temp_list.add(dt);
                    deptApDtList.put(dt.getPkDeptAp(),temp_list);
                }
            }
        }
        User u = (User) user;
        String codeDe = null;

        if(deptApDtList!=null && deptApDtList.size()>0){
            List<PdDeDrugVo> drugVo =new ArrayList<>();
            codeDe = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_IPDS);
            for(String pkDeptAp : deptApDtList.keySet()){
                if(deptApDtList.get(pkDeptAp)!=null && deptApDtList.get(pkDeptAp).size()>0){
                    drugVo=ipDeDrugPubService.mergeIpDeDrug(deptApDtList.get(pkDeptAp), "0", "1", codeDe, new Date());
                    if (drugVo != null && drugVo.size() != 0)
                        codeDe = drugVo.get(0).getCodeDe();
                }
            }
            try {
                //调用接口发送药品至包药机
                ExtSystemProcessUtils.processExtMethod("PackMachine", "sendToMah", drugVo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (extDtlist != null && extDtlist.size() > 0) {
            if (CommonUtils.isEmptyString(codeDe))
                codeDe = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_IPDS);
            deDrugExtPubService.ipDeDrug(extDtlist, u.getPkStore(), codeDe, new Date(), u, param);
        }
        //调用接口住院静配接口
        ExtSystemProcessUtils.processExtMethod("PIVAS", "saveTorders", exPdAppDetails);
        return codeDe;

    }

    /**
     * 住院(科室领药发药)发药处理
     *
     * @param param
     * @param user
     */
    public String mergeIpDeDrugDept(String param, IUser user) {
        // 前台传过来是勾选的请领明细
        List<ExPdApplyDetail> exPdAppDetails = JsonUtil.readValue(param, new TypeReference<List<ExPdApplyDetail>>() {
        });
        String codeDe = "";
        Date dateDe = new Date();
        //发药明细
        List<ExPdApplyDetail> aplist = new ArrayList<ExPdApplyDetail>();
        //退药明细
        List<ExPdApplyDetail> delist = new ArrayList<ExPdApplyDetail>();
        for(ExPdApplyDetail dt:exPdAppDetails){
            if(dt.getQuanPack()>=0){
                aplist.add(dt);
            }else if(dt.getQuanPack()<0){
                delist.add(dt);
            }
        }

        if(aplist!=null&&aplist.size()>0){
            codeDe = ipDeDrugPubService.mergeIpDeDrug(aplist, "0", "0",null,dateDe).get(0).getCodeDe();
        }
        if(delist!=null&&delist.size()>0){
            codeDe = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_IPDS);
            mergeIpReBackDrug(delist, codeDe,dateDe,(User)user);
        }
        return codeDe;
    }

    /**
     * 退药处理公共服务(科室退药不退费)
     * @param exPdAppDetails
     * @param codeRefund
     * @param userCur
     * @return
     */
    public String mergeIpReBackDrug(List<ExPdApplyDetail> exPdAppDetails, String codeRefund,Date dateDe,User userCur) {
		/*
		 * 1、调用供应链出库服务 2、调用住院退费服务 3、写退药表 4、更新请领明细
		 */
        if (exPdAppDetails == null || exPdAppDetails.size() <= 0)
            throw new BusException("传入的请领明细数据错误");
        String pkOrg = userCur.getPkOrg();
        String pkDept = userCur.getPkDept();
        String pkEmp = userCur.getPkEmp();
        String nameEmp = userCur.getNameEmp();
        String pkStock = userCur.getPkStore();
        // 利用set集合的不可重复性，存储本次发药请领单的主键
        Set<String> pkPdApSet = new HashSet<>();
        String pkOrgAp = "";
        String pkDeptAp = "";
        List<String> pkPdapdtBackList=new ArrayList<>();//退药申请明细主键

        for (ExPdApplyDetail exap : exPdAppDetails) {
            pkPdapdtBackList.add(exap.getPkPdapdtBack());
        }
        List<ExPdApplyDetail> retapdtPriceList=drugPackMapper.qryDeptRetDrugPrice(pkPdapdtBackList);
        // 调用出库服务
        // 将前台勾选的出库明细转化成出库服务需要的vo
        List<PdInParamVo> popvList = new ArrayList<PdInParamVo>();
        for (ExPdApplyDetail exPdApplyDetail : exPdAppDetails) {
            pkOrgAp = exPdApplyDetail.getPkOrgAp();
            pkDeptAp = exPdApplyDetail.getPkDeptAp();
            PdInParamVo popv = new PdInParamVo();
            popv.setPkPd(exPdApplyDetail.getPkPd());
            popv.setPkPdapdt(exPdApplyDetail.getPkPdapdt());
            for (ExPdApplyDetail retpd : retapdtPriceList) {
                if(exPdApplyDetail.getPkPdapdtBack().equals(retpd.getPkPdapdtBack())){
                    popv.setPriceCost(retpd.getPriceCost());
                    popv.setPrice(retpd.getPrice());
                    popv.setBatchNo(retpd.getBatchNo());
                    popv.setDateExpire(retpd.getDateExpire());
                }
            }
            if(CommonUtils.isNull(popv.getPriceCost())){
                throw new BusException("请先发药在进行退药！");
            }
            //以下逻辑是为兼容原始数据未写入问题
            if(CommonUtils.isNull(popv.getPrice())){
                popv.setPrice(exPdApplyDetail.getPrice());
            }
            if(CommonUtils.isNull(popv.getPriceCost())){
                popv.setPriceCost(exPdApplyDetail.getPriceCost());
            }
            if(CommonUtils.isNull(popv.getBatchNo())){
                popv.setBatchNo(exPdApplyDetail.getBatchNo());
            }
            if(CommonUtils.isNull(popv.getDateExpire())){
                popv.setDateExpire(exPdApplyDetail.getDateExpire());
            }
            popv.setQuanMin(exPdApplyDetail.getQuanMin());
            popv.setQuanPack(Math.abs(exPdApplyDetail.getQuanPack()));
            popv.setAmount(Math.abs(exPdApplyDetail.getAmount()));
            popv.setPackSize(exPdApplyDetail.getPackSize());
            popv.setPkPv(exPdApplyDetail.getPkPv());
            popv.setPkCnOrd(exPdApplyDetail.getPkCnord());
            popv.setPkUnitPack(exPdApplyDetail.getPkUnit());
            popvList.add(popv);
            // 存放请领单主键
            pkPdApSet.add(exPdApplyDetail.getPkPdap());
        }
        // 返回请领明细主键和入库明细主键
        Map<String, Object> mapInResult = pdStInPubService.execStIn(pkOrgAp,pkDeptAp,popvList, pkStock);
        List<RefundVo> refundVos = new ArrayList<RefundVo>();
        List<ExPdDe> exPdDes = new ArrayList<ExPdDe>();
        List<ExPdApplyDetail> exPdAppDetailsNew = new ArrayList<ExPdApplyDetail>();
        for (ExPdApplyDetail exPdApplyDetail : exPdAppDetails) {
            // 调用住院退费服务
            RefundVo refundVo = new RefundVo();
            refundVo.setPkOrg(pkOrg);
            refundVo.setPkDept(pkDept);
            refundVo.setNameEmp(nameEmp);
            refundVo.setPkEmp(pkEmp);
            refundVo.setPkCgip(exPdApplyDetail.getPkCgip());
            refundVo.setQuanRe(Math.abs(exPdApplyDetail.getQuanPack()));
            refundVos.add(refundVo);
            // 写退药表ex_pd_de
            ExPdDe exPdDe = new ExPdDe();
            // 设置默认值
            ApplicationUtils.setDefaultValue(exPdDe, true);
            exPdDe.setPkPdapdt(exPdApplyDetail.getPkPdapdt());
            exPdDe.setPkOrg(pkOrg);
            exPdDe.setCodeDe(codeRefund);// 调用编码生成规则获取退药单号
            exPdDe.setEuDirect("-1");
            exPdDe.setPkCnord(exPdApplyDetail.getPkCnord());
            exPdDe.setPkDeptAp(exPdApplyDetail.getPkDeptAp());
            exPdDe.setPkOrgAp(exPdApplyDetail.getPkOrgAp());
            exPdDe.setPkPv(exPdApplyDetail.getPkPv());
            exPdDe.setPkPd(exPdApplyDetail.getPkPd());
            exPdDe.setPkUnit(exPdApplyDetail.getPkUnit());
            exPdDe.setDateExpire(exPdApplyDetail.getDateExpire());
            exPdDe.setPackSize(exPdApplyDetail.getPackSize());
            exPdDe.setBatchNo(exPdApplyDetail.getBatchNo());
            exPdDe.setQuanPack(Math.abs(exPdApplyDetail.getQuanPack()));
            exPdDe.setQuanMin(exPdApplyDetail.getQuanMin());
            exPdDe.setPriceCost(exPdApplyDetail.getPriceCost());
            exPdDe.setPrice(exPdApplyDetail.getPrice());
            exPdDe.setAmount(Math.abs(exPdApplyDetail.getAmount()));// 包装数量乘以零售单价
            exPdDe.setPkPdstdt(mapInResult.get(exPdApplyDetail.getPkPdapdt()).toString());
            exPdDe.setPkPddecate(exPdApplyDetail.getPkPddecate());
            exPdDe.setNameDecate(exPdApplyDetail.getNameDecate());
            exPdDe.setPkDeptDe(pkDept);
            exPdDe.setPkStoreDe(pkStock);
            exPdDe.setDateDe(dateDe);
            exPdDe.setPkEmpDe(pkEmp);
            exPdDe.setNameEmpDe(nameEmp);
            exPdDe.setFlagPrt("0");
            exPdDe.setFlagPivas("0");
            exPdDe.setFlagBarcode("0");
            exPdDes.add(exPdDe);
            exPdApplyDetail.setFlagDe("1");
            exPdApplyDetail.setFlagFinish("1");
            ApplicationUtils.setDefaultValue(exPdApplyDetail, false);
            exPdAppDetailsNew.add(exPdApplyDetail);
        }
        // 所有数据库操作全部放在循环外边做
        // 批量更新请领明细
        Set<String> pkPdapdts=new HashSet<String>();
        for (ExPdApplyDetail exPdapDt : exPdAppDetailsNew) {
            pkPdapdts.add(exPdapDt.getPkPdapdt());
        }
        if(pkPdapdts.size()>0){
            String updateApDetail ="update ex_pd_apply_detail set flag_de='1',flag_finish='1',ts=? where pk_pdapdt in (" + CommonUtils.convertSetToSqlInPart(pkPdapdts, "pk_pdapdt") + ") and flag_de='0' and flag_finish='0' and nvl(flag_canc,'0') <> '1' and flag_stop='0'";
            int count=DataBaseHelper.execute(updateApDetail, new Date());
            if(count!=exPdAppDetailsNew.size()){
                throw new BusException("您本次提交的记录已变更，请刷新后重新操作！");
            }
        }

        // 批量插入发药记录表
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdDe.class), exPdDes);

        // 批量退费
/*        BlPubReturnVo rtnvo = ipCgPubService.refundInBatch(refundVos);
        int cgcnt = 0;
        if(rtnvo==null||rtnvo.getBids()==null||rtnvo.getBids().size()<=0){
            logger.info("================退药单【"+codeRefund+"】在退费时未产生退费明细================");
        }else{
            cgcnt = rtnvo.getBids().size();
        }
        logger.info("================退药单【"+codeRefund+"】对应"+exPdAppDetails.size()+"条退请领明细，共生成"+exPdDes.size()+"条退药记录,"+cgcnt+"条退费明细记录================");*/
        // 批量更新表ex_pd_apply
        List<ExPdApply> exPdApplys = new ArrayList<ExPdApply>();
        for (String pkPdAp : pkPdApSet) {
            ExPdApply exPdApply = new ExPdApply();
            exPdApply.setPkPdap(pkPdAp);
            exPdApplys.add(exPdApply);
        }
        String sqlTemp = "update ex_pd_apply set flag_finish='1' where pk_pdap=:pkPdap and not exists (select 1  from ex_pd_apply_detail detail  where ex_pd_apply.pk_pdap = detail.pk_pdap and detail.flag_finish = '0' and nvl(detail.flag_canc,'0')='0' and detail.flag_stop='0')";
        DataBaseHelper.batchUpdate(sqlTemp, exPdApplys);
        // 只要请领单有一条明细发放，那么请领单的状态就是发放状态
        String sqlTemp2 = "update ex_pd_apply set eu_status='1' where pk_pdap=:pkPdap and  exists (select 1  from ex_pd_apply_detail detail  where ex_pd_apply.pk_pdap = detail.pk_pdap and detail.flag_de = '1')";
        DataBaseHelper.batchUpdate(sqlTemp2, exPdApplys);
        /**
         * 发送住院退药信息
         */
        Map<String, Object> map = new HashMap<>();
        map.put("exPdAppDetails",exPdAppDetails);//请领明细
        map.put("orc", "CR");//撤销
        map.put("exPdDes", exPdDes);//
        PlatFormSendUtils.sendScmIpDeDrug(map);
        return codeRefund;

    }

    /**
     * 008004001023
     * 医嘱停发处理--中山二院
     *
     * @param param {"pkPdapDts":"领药明细主键集合(List<String>)",
     *              "dtExdeptpdstop":"停发原因",
     *              "reasonStop":"原因描述",
     *              "pkEmpStop":"停发人Id",
     *              "nameEmpStop":"停发人姓名"}
     * @param user
     */
    @SuppressWarnings("unchecked")
    public void saveStopApplyReason(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap != null) {
            List<String> pkPdapdts = (List<String>) paramMap.get("pkPdapDts");
            Set<String> apdtSet = new HashSet<String>();
            for (String apdt : pkPdapdts) {
                apdtSet.add(apdt);
            }
            paramMap.put("pkPdapDts", apdtSet);
            int count = ipDeDrugMapper.saveStopApplyReason(paramMap);
            if (count != apdtSet.size()) {
                throw new BusException("您本次操作的记录已变更，请刷新后处理！");
            }
        }
    }

    /**
     * 008004001024
     * 查询停发医嘱--中山二院
     *
     * @param param {"pkPdaps":"领药申请单号（List）","codePv":"就诊号"}
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryStopApply(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        if (paramMap != null) {
            mapList = ipDeDrugMapper.qryStopApply(paramMap);
        }
        return mapList;
    }

    /**
     * 008004001025
     * 取消停发--中山二院
     *
     * @param param {"pkPdapDts":"领药明细主键list"}
     * @param user
     */
    public void cancelStopApply(String param, IUser user) {
        Set<String> pkPdapDts = JsonUtil.readValue(param, new TypeReference<Set<String>>() {
        });
        if (pkPdapDts != null) {
            List<String> apdtList = new ArrayList<String>();
            for (String pkPdapdt : pkPdapDts) {
                apdtList.add(pkPdapdt);
            }
            int count = ipDeDrugMapper.cancelStopApply(apdtList);
            if (count != apdtList.size()) {
                throw new BusException("您本次操作的记录已变更，请刷新后处理！");
            }
        }
    }

    /**
     * 008004001026
     * 查询处方汇总
     *
     * @param param{"pkStore":"仓库主键","pkPreses":"处方集合"}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> qrySumPres(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        List<String> pkPresList = (List<String>) paramMap.get("pkPreses");
        if (pkPresList == null || pkPresList.size() == 0) return null;
        return ipDeDrugMapper.qrySumPres(paramMap);
    }

    /**
     * 外部接口发药时查询患者配送地址
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPiAddress(String param, IUser user) {
        String pkPi = JsonUtil.getFieldValue(param, "pkPi");
        if (pkPi == null)
            throw new BusException("未查询到该患者地址信息，请手动录入！");
        List<Map<String, Object>> piAddress = DataBaseHelper.queryForList("select * from pi_address where pk_pi=?", new Object[]{pkPi});
        return piAddress;
    }

    /**
     * 保存患者发药地址
     *
     * @param param
     * @param user
     */
    public void savePiAddress(String param, IUser user) {
        List<PiAddress> piAddrs = JsonUtil.readValue(param, new TypeReference<List<PiAddress>>() {
        });
        if (piAddrs == null || piAddrs.size() == 0) throw new BusException("没有可保存的患者信息");
        String pkPi = piAddrs.get(0).getPkPi();
        //先删除再保存
        DataBaseHelper.execute("delete from pi_address where pk_pi=?", new Object[]{pkPi});
        //保存
        for (PiAddress temp : piAddrs) {
            temp.setPkAddr(NHISUUID.getKeyId());
            temp.setCreateTime(new Date());
            temp.setTs(new Date());
            temp.setCreator(((User) user).getId());
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiAddress.class), piAddrs);
    }

    /**
     * 删除患者发药地址
     * 008004001036
     *
     * @param param
     * @param user
     */
    public void deleteAddr(String param, IUser user) {
        String pkAddr = JsonUtil.getFieldValue(param, "pkAddr");
        DataBaseHelper.execute("delete from pi_address where pk_addr=?", pkAddr);
    }

    /**
     * 查询发药关联的执行明细
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryExOrderOccInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        List<Map<String, Object>> resList = ipDeDrugMapper.qryExOrderOccInfo(paramMap);
        return resList;
    }

    /**
     * 发退单查询双击明细记录调出本条药品的执行记录
     * 008004001039
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPdExOrderInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        List<Map<String, Object>> resList = ipDeDrugMapper.qryPdExOrderInfo(paramMap);
        return resList;
    }

    /**
     * 008004001040
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPdApdtRelExInfo(String param, IUser user) {
        List<String> dispensingMedicine = new ArrayList<>();
        dispensingMedicine.add(ChrtCodeDecate);
        dispensingMedicine.add(TpnCodeDecate);
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkPdStock(userCur.getPkStore());
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        List<Map<String, Object>> resList = ipDeDrugMapper.qryPdApdtRelExInfo(ipDeDrugDto);
        for (Map<String, Object> map : resList) {
            double quanPack = Math.ceil(Double.parseDouble(map.get("quanPackIn").toString()));
            double price = Double.parseDouble(map.get("price").toString());
            int packSize = Integer.parseInt(map.get("packSize").toString());
            double quanMin = quanPack * packSize;
            double amount = quanPack * price;
            map.put("quanPack", quanPack);
            map.put("quanMin", quanMin);
            map.put("amount", amount);
            map.put("actualQuanPack", quanPack);
        }
        // 深大配置是否进行数据隔离
        if ("1".equals(ipDeDrugDto.getIsolation()) && dispensingMedicine.contains(ipDeDrugDto.getCodeDecate())){
            List<Map<String, Object>> handleResult= new ArrayList<>();
            // 再根据ordsnparent 分组，然后假设分组中没有化疗，那么打上标志能否展示，不能就移除
            Map<Object, List<Map<String, Object>>> ordsnParent = resList.stream().collect(Collectors.groupingBy(m -> m.get("ordsnParent")));
            for (Map.Entry<Object, List<Map<String, Object>>> objectListEntry : ordsnParent.entrySet()) {

                if (TpnCodeDecate.equals(ipDeDrugDto.getCodeDecate())){
                    boolean b = objectListEntry.getValue().stream().anyMatch(o ->("1").equals(o.get("flagTpn")));
                    if (b){
                        handleResult.addAll(objectListEntry.getValue());
                    }
                    // 如果是化疗那么必须包含这个药品才能出去  可以改下成一个接口方法，然后不同的需求就自己实现
                    // 这样的好处是别人也可以调用，或者说假设还有其他相同需求可以不写if else 而是用
                    // 实现接口的方法，看起来更加优雅
                }else if(ChrtCodeDecate.equals(ipDeDrugDto.getCodeDecate())){
                    boolean b = objectListEntry.getValue().stream().anyMatch(o ->("1").equals(o.get("flagChrt")));
                    if (b){
                        handleResult.addAll(objectListEntry.getValue());
                    }

                }

            }

            return handleResult;
        }


        return resList;
    }

    /**
     * 008004001041
     * 查询静配的执行明细
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPivasPdExInfo(String param, IUser user) {
        List<String> pkPdapDts = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        List<Map<String, Object>> resList = ipDeDrugMapper.qryPivasPdExInfo(pkPdapDts);
        return resList;
    }

    /**
     * 008004001042
     * 住院医嘱/处方/静配的退回服务
     *
     * @param param
     * @param user
     */
    public void updateRetDrugForDept(String param, IUser user) {
        List<ExPdApplyDetail> exPdapdts = JsonUtil.readValue(param, new TypeReference<List<ExPdApplyDetail>>() {
        });
        if (exPdapdts == null) return;
        Set<String> pkPdapdtSet = new HashSet<String>();
        Set<String> pkPdapSet = new HashSet<String>();
        for (ExPdApplyDetail expdapdt : exPdapdts) {
            pkPdapdtSet.add(expdapdt.getPkPdapdt());
            pkPdapSet.add(expdapdt.getPkPdap());
        }
        User nuser = (User) user;
        String pkPdapdts = CommonUtils.convertSetToSqlInPart(pkPdapdtSet, "pk_pdapdt");
        //1.更新请领明细中flag_cancel,pk_emp_back,name_emp_back,date_bace,note
        StringBuffer upapdtSql = new StringBuffer("update ex_pd_apply_detail set flag_canc=1 ,");
        upapdtSql.append(" pk_emp_back=?,name_emp_back=? ,date_back=? ,note='药房退回',note_back=? ");
        upapdtSql.append(" where pk_pdapdt in (");
        upapdtSql.append(pkPdapdts);
        upapdtSql.append(") and (flag_canc='0' or flag_canc is null) and flag_de='0' and flag_stop='0'");
        int count = DataBaseHelper.update(upapdtSql.toString(), new Object[]{nuser.getPkEmp(), nuser.getNameEmp(), new Date(),exPdapdts.get(0).getNoteBack()});
        if (count != pkPdapdtSet.size()) {
            throw new BusException("您本次提交的药品发放明细中已有被其他人处理的记录，请刷新请领单后重新处理！");
        }
        //2.更新执行单，去除请领标志
        StringBuffer upExSql = new StringBuffer("update ex_order_occ ");
        upExSql.append(" set pk_pdapdt=null");
        upExSql.append(" where exists (select 1 from ex_pd_apply_detail dt");
        upExSql.append(" where dt.flag_de='0') and pk_pdapdt in (");
        upExSql.append(pkPdapdts);
        upExSql.append(")");
        DataBaseHelper.update(upExSql.toString());
        String pkPdaps = CommonUtils.convertSetToSqlInPart(pkPdapSet, "pk_pdap");
        //3.更新请领单--如果请领明细全部被退回，同时更新请领单为取消状态(不包含已经发药的)
        StringBuffer upPddts = new StringBuffer("update ex_pd_apply  set ex_pd_apply.flag_cancel='1',ex_pd_apply.eu_status='9' ");
        upPddts.append(" ,ex_pd_apply.pk_emp_cancel=? ,ex_pd_apply.name_emp_cancel=?,ex_pd_apply.date_cancel=? ");
        upPddts.append(" where not exists (select 1 from ex_pd_apply_detail dt ");
        upPddts.append(" where ex_pd_apply.pk_pdap=dt.pk_pdap and (dt.flag_canc='0' or dt.flag_canc is null ))");
        upPddts.append(" and not exists (select 1 from ex_pd_apply_detail dt where ex_pd_apply.pk_pdap=dt.pk_pdap  and dt.flag_finish='1'  )");
        upPddts.append(" and ex_pd_apply.pk_pdap in (");
        upPddts.append(pkPdaps);
        upPddts.append(")");
        DataBaseHelper.update(upPddts.toString(), new Object[]{nuser.getPkEmp(), nuser.getNameEmp(), new Date()});
        //4.更新请领单-如果请领单对应的明细即包含已经完成发药，或者退回处理的将请领单置为完成状态
        StringBuffer upPdaps = new StringBuffer("update ex_pd_apply   set ex_pd_apply.flag_finish='1',ex_pd_apply.eu_status='1' where ex_pd_apply.pk_pdap in (");
        upPdaps.append(pkPdaps);
        upPdaps.append(") and exists (select 1 from ex_pd_apply_detail dt where ex_pd_apply.pk_pdap=dt.pk_pdap and dt.flag_finish='1') ");
        upPdaps.append(" and not exists (select 1 from ex_pd_apply_detail dt1  where ex_pd_apply.pk_pdap=dt1.pk_pdap and dt1.flag_finish='0' and (dt1.flag_canc='0' or dt1.flag_canc is null) and dt1.flag_stop='0') ");
        DataBaseHelper.update(upPdaps.toString());
    }

    /**
     * 住院发药查询-按科室汇总显示
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryIpDrugAppListBySum(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkDeptDe", userCur.getPkDept());
        if (ipDeDrugDto.getDateStart() != null) {
            paramMap.put("dateStart", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.getDateMorning(ipDeDrugDto.getDateStart(), 0)));
        }
        if (ipDeDrugDto.getDateEnd() != null) {
            paramMap.put("dateEnd", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.getDateMorning(ipDeDrugDto.getDateEnd(), 1)));
        }
        paramMap.put("pkDeptAp", ipDeDrugDto.getPkAppDeptNs());
        paramMap.put("flagDept", ipDeDrugDto.getFlagDept());
        paramMap.put("isPres", ipDeDrugDto.getIsPres());
        paramMap.put("deCateWhereSql", ipDeDrugDto.getDeCateWhereSql());
        List<Map<String, Object>> mapResult = ipDeDrugMapper.qryExPdApplyListBySum(paramMap);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 008004001044
     * 根据条件查询药品请领明细(医嘱和处方共用)(科室汇总显示)
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpDeDrugDetailByDeptAp(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        User userCur = (User) user;
        paramMap.put("pkPdStock", userCur.getPkStore());
        paramMap.put("pkOrg", userCur.getPkOrg());
        paramMap.put("pkDept", userCur.getPkDept());
        paramMap.put("pkDeptApList", ipDeDrugDto.getPkDeptApList());
        paramMap.put("deCateWhereSql", ipDeDrugDto.getDeCateWhereSql());
        paramMap.put("pvIpCodes", ipDeDrugDto.getPvIpCodes());
        paramMap.put("pkPddecate", ipDeDrugDto.getPkPddecate());
        paramMap.put("nameDecate", ipDeDrugDto.getNameDecate());
        paramMap.put("codeDecate", ipDeDrugDto.getCodeDecate());
        paramMap.put("flagEmer", ipDeDrugDto.getFlagEmer());
        paramMap.put("euPrio",ipDeDrugDto.getEuPrio());
        if (ipDeDrugDto.getDateStart() != null) {
            paramMap.put("dateStart", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", ipDeDrugDto.getDateStart()));
        }
        if (ipDeDrugDto.getDateEnd() != null) {
            paramMap.put("dateEnd", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", ipDeDrugDto.getDateEnd()));
        }
        List<Map<String, Object>> mapResult = null;
        if (EnumerateParameter.ONE.equals(ipDeDrugDto.getIsPres())) {
            mapResult = ipDeDrugMapper.qryPresDrugApList(ipDeDrugDto);
        } else {
            mapResult = ipDeDrugMapper.qryDrugDtApList(paramMap);
        }
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 008004001045
     * 查询停发医嘱
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryStopApDtByDeptAp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        User nu = (User) user;
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        if (paramMap != null) {
            paramMap.put("pkDeptDe", nu.getPkDept());
            mapList = ipDeDrugMapper.qryStopApDtByDeptAp(paramMap);
        }
        return mapList;
    }

    /**
     * 008004001047
     * 发药单查询，医嘱发退药查询汇总数据，不采用分页方式
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryDeDrugSumList(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkDept(userCur.getPkDept());
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        List<Map<String, Object>> resList = ipDeDrugMapper.queryDeDrugSumList(ipDeDrugDto);
        return resList;
    }

    /**
     * 查询发药界面待发药申请单个数
     *
     * @param param{"pkDeptDe":"发药药房","isPres":"是否是处方"}
     * @param user
     * @return
     */
    public int getUnfinishDrugAp(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        if (ipDeDrugDto == null) return 0;
        User userCur = (User) user;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkDeptDe", userCur.getPkDept());
        paramMap.put("flagDept", ipDeDrugDto.getFlagDept());
        paramMap.put("isPres", ipDeDrugDto.getIsPres());
        Integer count = ipDeDrugMapper.getUnfinishDrugAp(paramMap);
        return count != null ? count : 0;
    }
    /**
     * 008004001046
     * 发药前校验患者是否欠费---暂时不使用
     * @param param
     * @param user
     * @return
     */
	/*public Map<String,Object> checkPvIsCg(String param,IUser user){
		List<ExPiInfo> exPiInfos = JsonUtil.readValue(param, new TypeReference<List<ExPiInfo>>() { });
		if(exPiInfos==null ||exPiInfos.size()==0)return null;
		List<String> pkPvs=new  ArrayList<>();
		for (ExPiInfo apdt : exPiInfos) {
			if(pkPvs.contains(apdt.getPkPv())) continue;
			pkPvs.add(apdt.getPkPv());
		}
		List<String> resPvList= cgPubService.checkPvArrears(pkPvs);
		String msg="";
		if(resPvList==null) return null;
		for (String pkPv : resPvList) {
			for (ExPiInfo apdt : exPiInfos) {
				String str="床号:"+apdt.getBedNo()+",姓名:"+apdt.getNamePi();
				if(apdt.getPkPv().equals(pkPv)&&!msg.contains(str)){
					msg+=str+"\n";
				}
			}
		}
		Map<String,Object> resMap=new HashMap<String,Object>();
		resMap.put("pvList", resPvList);
		resMap.put("msg", msg);
		return resMap;
	}*/

    /***
     * @Description 重发包药机
     * @auther wuqiang
     * @Date 2020-04-24
     * @Param [param, user]
     * @return void
     */
    public void sendDrugInfoToMachine(String param, IUser user) {
        Map<String, Object> sendParmaMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });
        if (sendParmaMap.isEmpty()) {
            throw new BusException("传入申请单为空，请核对！");
        }
        //调用接口发送药品至包药机
        try {
            ExtSystemProcessUtils.processExtMethod("PackMachine", "sendDrugInfoToMachine", sendParmaMap);
        } catch (Exception e) {
            throw new BusException("调用包药机接口失败");
        }
    }


    /**
     * @return void
     * @Description 查询重发包药机数据
     * @auther wuqiang
     * @Date 2020-06-01
     * @Param [param, user]
     */
    public List<Map<String, Object>> querySendDrugAgainData(String param, IUser user) {
        Map<String, Object> sendParmaMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });
        Object mapOb = ExtSystemProcessUtils.processExtMethod("PackMachine", "querySendDrugAgainData", sendParmaMap);
        if (mapOb != null) {
            return (List<Map<String, Object>>) mapOb;
        }
        return null;
    }


    /***
     * @Description 查询药袋明细
     * @auther wuqiang
     * @Date 2020-04-26
     * @Param [param, user]
     * @return void
     */
    public List<Map<String, Object>> queryExMedBag(String param, IUser user) {
        Map<String, Object> qryparam = JsonUtil.readValue(param, Map.class);
        Object mapOb = ExtSystemProcessUtils.processExtMethod("PackMachine", "queryExMedBag", qryparam);
        if (mapOb != null) {
            return (List<Map<String, Object>>) mapOb;
        }
        return null;
    }


    /**
     * 008004001049
     * 查询申请科室信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryExApplyData(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        User u = UserContext.getUser();
        if (paramMap == null) {
            paramMap = new HashMap<String, Object>();
        }
        paramMap.put("pkDeptDe", u.getPkDept());
        List<Map<String, Object>> resList = ipDeDrugMapper.queryExApplyForDept(paramMap);
        return resList;
    }

    /**
     * 008004001050
     * 查询申请单明细数据
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queryExApplyDtByDept(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        int pageIndex = CommonUtils.getInteger(paramMap.get("pageIndex"));
        int pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
        MyBatisPage.startPage(pageIndex, pageSize);
        User u = UserContext.getUser();
        paramMap.put("pkDeptDe", u.getPkDept());
        List<Map<String, Object>> mapResult = ipDeDrugMapper.queryExApplyDtForDept(paramMap);
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("deList", mapResult);
        resMap.put("TotalCount", page.getTotalCount());
        return resMap;
    }
}
