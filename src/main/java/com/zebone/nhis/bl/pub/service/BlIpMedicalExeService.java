package com.zebone.nhis.bl.pub.service;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.ExListVo;
import com.zebone.nhis.bl.pub.vo.MedExeIpParam;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.service.QueryRemainFeeService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * 医疗执行服务（住院）
 */
@Service
public class BlIpMedicalExeService {


    @Autowired
    private IpCgPubService ipCgPubService;
    @Autowired
    private BlIpPubMapper blIpPubMapper;
    @Autowired
    private QueryRemainFeeService queryFeeServcie;

    /**
     * 执行前校验医技申请单信息
     *
     * @param param{pkCnord,ordsnParent,nameOrd,namePi,pkPv}
     * @param user
     */
    public void checkMedicalApp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || paramMap.get("pkCnord") == null || paramMap.get("ordsnParent") == null)
            throw new BusException("未获取到申请单正确的校验参数！");
        Integer cnt = blIpPubMapper.queryMedAppSubmitCount(paramMap);
        if (cnt <= 0)
            throw new BusException(paramMap.get("nameOrd") + "已取消提交，请检查！");
        Integer ordcnt = blIpPubMapper.queryMedAppOrdCount(paramMap);
        if (ordcnt > 0)
            throw new BusException(paramMap.get("nameOrd") + "存在未签署医嘱，签署后方可执行！");
        //查询患者余额是否不足
        if (!queryFeeServcie.isArrearage(CommonUtils.getString(paramMap.get("pkPv")), "", BigDecimal.ZERO)) {
            throw new BusException("患者" + paramMap.get("namePi") + "已欠费，缴费后方可执行！");
        }
    }

    /**
     * @param param
     * @param user
     * @return
     */

    public void ipExe(String param, IUser user) {
        List<MedExeIpParam> params = JsonUtil.readValue(param, new TypeReference<List<MedExeIpParam>>() {
        });
        User u = (User) user;
        ipExe4Inner(params, u.getPkOrg());


    }


    public void ipExe4Inner(List<MedExeIpParam> params, String pkOrg) {
        List<BlPubParamVo> cgParams = new ArrayList<BlPubParamVo>();
        if (params == null || params.size() < 1) return;

        //1.生成执行单表
        List<ExAssistOcc> exAssists = new ArrayList<ExAssistOcc>();
        List<String> PkCnords = new ArrayList<String>();
        Set<String> cnordPks = new HashSet<String>();//无需记费待批量更新的医嘱主键
        //父医嘱号集合，用于医技医嘱记费
        List<String> ordsnParents = new ArrayList<String>();
        Map<String, String> mapOrdEx = new HashMap<String, String>();
        for (MedExeIpParam vo : params) {
            ExAssistOcc assit = new ExAssistOcc();
            setAssit(assit, vo);
            exAssists.add(assit);
            if ("1".equals(vo.getFlagBl()))
                cgParams.add(vo);
            cnordPks.add(vo.getPkCnord());
            PkCnords.add(vo.getPkCnord());
            ordsnParents.add(vo.getOrdsnParent().toString());
            mapOrdEx.put(vo.getOrdsnParent().toString(), vo.getExOrdOcc() == null ? "" : vo.getExOrdOcc().getPkExocc());
        }

        List<CnOrder> ordList = blIpPubMapper.queryMedOrdList(ordsnParents);
        if (ordList != null && ordList.size() > 0) {
            for (CnOrder ord : ordList) {
                if ("1".equals(ord.getEuStatusOrd()) && "1".equals(ord.getFlagBl())) {//只收签署状态的记费医技医嘱费用
                    cgParams.add(constructBlVo(ord, pkOrg, mapOrdEx));
                }
                if ("1".equals(ord.getEuStatusOrd()))//签署状态医嘱，组装参数用于更新执行单
                {
                    cnordPks.add(ord.getPkCnord());
                    continue;
                }
            }
        }
        if (null != cgParams && cgParams.size() > 0) {
            //2.住院患者记费，调用住院患者记费接口，写表bl_ip_dt，并返回记费主键pk_cgip
            BlPubReturnVo cgRtns = ipCgPubService.chargeIpBatch(cgParams, false);
            System.out.println(ApplicationUtils.objectToJson(cgParams));
            if (cgRtns == null || cgRtns.getBids() == null) {
                throw new BusException(" 计费调用失败，执行失败！");
            }
            //3.更新医嘱执行单；
            Object[] args = new Object[5];
            args[0] = new Date();
            args[1] = UserContext.getUser().getPkEmp();
            args[2] = UserContext.getUser().getNameEmp();
            for (BlIpDt vo : cgRtns.getBids()) {
                args[3] = vo.getPkCgip();
                args[4] = vo.getPkOrdexdt();
                for (String pk_cnord : PkCnords) {
                    if (pk_cnord.equals(vo.getPkCnord())) {
                        DataBaseHelper.execute("update ex_order_occ set date_occ=?, pk_emp_occ=?,name_emp_occ=?,pk_cg=?,eu_status=1 where pk_exocc=? ", args);
                        break;
                    }
                }
            }
        }

        //更新医技医嘱状态为执行状态，并设置执行时间；更新执行单状态
        if (ordList != null && ordList.size() > 0) {
            Map<String, Object> paramMaps = new HashMap<String, Object>();
            paramMaps.put("ordList", ordList);
            paramMaps.put("curDate", DateUtils.getDateTimeStr(new Date()));
            blIpPubMapper.updateOrderToExec(paramMaps);
        }

        //无记费记录，直接更新执行单状态
        if (null != cnordPks && !cnordPks.isEmpty()) {
            String pkCnords = CommonUtils.convertSetToSqlInPart(cnordPks, "pk_cnord");
            String dateOcc = DateUtils.getDateTimeStr(new Date());

            int cnt = DataBaseHelper.execute("update ex_order_occ set date_occ= to_date('" + dateOcc + "','yyyyMMddHH24MiSS'), pk_emp_occ=?,name_emp_occ=?,"
                            + " eu_status=1,ts = to_date('" + dateOcc + "','yyyyMMddHH24MiSS') where pk_cnord in (" + pkCnords + ") "
                    , new Object[]{UserContext.getUser().getPkEmp(), UserContext.getUser().getNameEmp()});
            if (cnt != cnordPks.size())
                throw new BusException(" 执行单更新失败，执行失败！");
        }

        //生成医技执行单
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExAssistOcc.class), exAssists);

        //5	更新医嘱表
        List<CnOrder> cnOrds = blIpPubMapper.qryOrdByPkAss(PkCnords);
        if (cnOrds != null && cnOrds.size() > 0) {
            //4.1更新医嘱cn_order
            //blIpPubMapper.updOrder(cnOrds);
            List<String> applyPks = new ArrayList<String>();
            List<String> paPks = new ArrayList<String>();
            for (CnOrder cnord : cnOrds) {
                if (cnord.getCodeOrdtype() != null && cnord.getCodeOrdtype().equals("0204")) {
                    paPks.add(cnord.getPkCnord());
                }
                if (cnord.getCodeOrdtype() != null && cnord.getCodeOrdtype().startsWith("02")) {
                    applyPks.add(cnord.getPkCnord());
                }
            }
            //4.2 如果检查医嘱，更新检查申请表
            if (applyPks.size() > 0) {
                blIpPubMapper.updOrderApply(applyPks);
            }
            //4.2  如果病理医嘱，更新病理申请表；
            if (paPks.size() > 0) {
                blIpPubMapper.updOrderPa(paPks);
            }
        }
    }

    private void setAssit(ExAssistOcc assit, MedExeIpParam vo) {
        assit.setPkOrg(UserContext.getUser().getPkOrg());//当前机构；
        assit.setPkCnord(vo.getPkCnord());//医嘱主键
        assit.setPkPv(vo.getPkPv());//患者就诊
        assit.setPkPi(vo.getPkPi());//患者主键
        assit.setEuPvtype("3");//就诊类型；
        assit.setCodeOcc(ApplicationUtils.getCode("0503"));//执行单号，调用编码规则接口获取；
        assit.setPkDept(vo.getPkDeptApp());//开立科室；
        assit.setPkEmpOrd(vo.getPkEmpApp());//开立医生；
        assit.setNameEmpOrd(vo.getNameEmpApp());//开立医生姓名；
        assit.setDateOrd(vo.getDateHap());//开立日期；
        assit.setDatePlan(vo.getExOrdOcc().getDatePlan());//计划执行日期；
        assit.setQuanOcc(vo.getExOrdOcc().getQuanOcc());//每次执行数量；
        assit.setTimesOcc(1);
        assit.setTimesTotal(1);
        assit.setPkOrgOcc(vo.getPkOrgEx());// 执行机构；
        assit.setPkDeptOcc(vo.getPkDeptEx());// 执行科室；
        assit.setDateOcc(new Date());
        assit.setPkEmpOcc(UserContext.getUser().getPkEmp());
        assit.setNameEmpOcc(UserContext.getUser().getNameEmp());
        assit.setFlagOcc("1");
        assit.setFlagCanc("0");
        assit.setPkExocc(vo.getExOrdOcc().getPkExocc());//关联医嘱执行单主键；
        assit.setInfantNo(vo.getInfantNo());//婴儿序号；
        assit.setEuStatus("1");
        assit.setFlagPrt("0");
        assit.setNote(vo.getNote());
        assit.setPkAssocc(NHISUUID.getKeyId());
        setDefaultValue(assit, true);
    }

    /**
     * 取消医疗执行
     *
     * @param param
     * @param user
     */
    @SuppressWarnings("unchecked")
    public void ExecuteCancel(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String euPvtype = (String) map.get("euPvtype");//就诊类型
        String pkAssocc = (String) map.get("pkAssocc");//医疗记录主键
        String pkExocc = (String) map.get("pkExocc");//住院医嘱执行单记录
        String pkEmpCanc = UserContext.getUser().getPkEmp();//取消人
        String nameEmpCanc = UserContext.getUser().getNameEmp();

        Integer type = Integer.parseInt(euPvtype);

        Object[] args = new Object[4];
        args[0] = new Date();
        args[1] = pkEmpCanc;
        args[2] = nameEmpCanc;
        args[3] = pkAssocc;
        DataBaseHelper.execute("update ex_assist_occ  set flag_canc=1,date_canc=?," +
                "pk_emp_canc=?,name_emp_canc=?,eu_status=9" +
                " where pk_assocc=? and flag_canc=0", args);
        if (type < 3) return;
        //退费
        retCg(pkExocc, (User) user, args, "0");

    }

    /**
     * 医技重新开启执行
     *
     * @param param
     * @param user
     */
    @SuppressWarnings("unchecked")
    public void executeRestart(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String pkAssocc = CommonUtils.getString(map.get("pkAssocc"));
        String sql = "select count(1) from ex_assist_occ where pk_assocc=? and flag_refund='1'";
        //判断该医技是否产生退费记录
        int pk_cg_cancel = DataBaseHelper.queryForScalar(sql, Integer.class, pkAssocc);
        if (pk_cg_cancel == 0) {
            String sql_up = "update ex_assist_occ set flag_canc='0',flag_occ='0',eu_status='0',date_canc=null,"
                    + "pk_emp_canc=null,name_emp_canc=null where pk_assocc=?";
            DataBaseHelper.execute(sql_up, pkAssocc);
        } else {
            throw new BusException("患者已经退费，无法重新开启医技");
        }

    }

    /**
     * 退费公共方法
     *
     * @param pkExocc  医疗执行主键
     * @param user     用户管理信息
     * @param args     参数
     * @param flagCanc 0：默认退未取消执行的记录 1：用于起前一天已取消执行的退费
     */
    public void retCg(String pkExocc, User user, Object[] args, String flagCanc) {

        //2.患者退费
        RefundVo voOrg = new RefundVo();
        voOrg.setNameEmp(user.getNameEmp());
        voOrg.setPkDept(user.getPkDept());
        voOrg.setPkEmp(user.getPkEmp());
        voOrg.setPkOrg(user.getPkOrg());

        List<RefundVo> refunds = new ArrayList<RefundVo>();
        List<BlIpDt> vos = blIpPubMapper.qryByPkExocc(pkExocc);// 调用患者住院退费接口，返回退费主键pk_cg_cancel；
        if (vos == null || vos.size() <= 0)
            throw new BusException("没有可退费的数据！");
        for (BlIpDt vo : vos) {
            RefundVo reVo = (RefundVo) voOrg.clone();
            reVo.setPkCgip(vo.getPkCgip());
            reVo.setQuanRe(vo.getQuan());
            refunds.add(reVo);
        }
        BlPubReturnVo refundRes = ipCgPubService.refundInBatch(refunds);
        //3.更新医嘱执行单
        Object[] args2 = Arrays.copyOf(args, 7);
        for (BlIpDt vo : refundRes.getBids()) {
            args2[4] = vo.getPkCgip();
            args2[5] = pkExocc;
            args2[6] = flagCanc;
            DataBaseHelper.execute("update ex_order_occ set flag_canc=1,date_canc=?, " +
                    "  pk_emp_canc=?,name_emp_canc=?,pk_dept_canc=?,eu_status=9,pk_cg_cancel=?" +
                    " where pk_exocc=? and flag_canc=?", args2);
        }

    }

    /**
     * 退费公共方法/取消登记临时用，后期修改
     *
     * @param pkExocc  医疗执行主键
     * @param user     用户管理信息
     * @param args     参数
     * @param flagCanc 0：默认退未取消执行的记录 1：用于起前一天已取消执行的退费
     */
    //todo 博爱项目临时添加，后期定好流程后进行统一的合并
    public void retCgTem(String pkExocc, User user, Object[] args, String flagCanc) {

        //2.患者退费
        RefundVo voOrg = new RefundVo();
        voOrg.setNameEmp(user.getNameEmp());
        voOrg.setPkDept(user.getPkDept());
        voOrg.setPkEmp(user.getPkEmp());
        voOrg.setPkOrg(user.getPkOrg());

        List<RefundVo> refunds = new ArrayList<RefundVo>();
        List<BlIpDt> vos = blIpPubMapper.qryByPkExocc(pkExocc);// 调用患者住院退费接口，返回退费主键pk_cg_cancel；
        if (vos == null || vos.size() <= 0) {
            throw new BusException("没有可退费的数据！");
        }
        for (BlIpDt vo : vos) {
            RefundVo reVo = (RefundVo) voOrg.clone();
            reVo.setPkCgip(vo.getPkCgip());
            reVo.setQuanRe(vo.getQuan());
            refunds.add(reVo);
        }
        BlPubReturnVo refundRes = ipCgPubService.refundInBatch(refunds);
        //直接删除医嘱执行单
       String sql="  delete from ex_assist_occ_dt where  pk_exocc=?";
        DataBaseHelper.execute(sql,new Object[]{pkExocc});
        sql="delete from ex_assist_occ  where  pk_exocc=?";
        DataBaseHelper.execute(sql,new Object[]{pkExocc});
         sql="update ex_order_occ set  PK_CG=null, " +
                "  eu_status=0,PK_EMP_OCC = NULL ,NAME_EMP_OCC = null ,DATE_OCC=null," +
                "   MODIFIER=?, MODITY_TIME=? " +
                "  where pk_exocc=?  ";
        DataBaseHelper.execute(sql,new Object[]{args[1],args[0],pkExocc});
    }

    public static void setDefaultValue(Object obj, boolean flag) {
        User user = UserContext.getUser();
        Map<String, Object> default_v = new HashMap<String, Object>();
        if (flag) {  // 如果新增
            default_v.put("pkOrg", user.getPkOrg());
            default_v.put("creator", user.getPkEmp());
            default_v.put("createTime", new Date());
            default_v.put("delFlag", "0");
        }
        default_v.put("ts", new Date());
        default_v.put("modifier", user.getPkEmp());

        Set<String> keys = default_v.keySet();

        for (String key : keys) {
            Field field = ReflectHelper.getTargetField(obj.getClass(), key);
            if (field != null) {
                ReflectHelper.setFieldValue(obj, key, default_v.get(key));
            }
        }
    }


    private BlPubParamVo constructBlVo(CnOrder ord, String pkOrg, Map<String, String> ordExMap) {
        BlPubParamVo blvo = new BlPubParamVo();
        blvo.setEuPvType("3");
        blvo.setFlagPd(ord.getFlagDurg());
        blvo.setFlagPv("0");
        blvo.setInfantNo(ord.getInfantNo() + "");
        blvo.setNameEmpApp(ord.getNameEmpInput());
        blvo.setNamePd(ord.getNameOrd());
        blvo.setPackSize(ord.getPackSize() == null ? 1 : ord.getPackSize().intValue());
        blvo.setPkCnord(ord.getPkCnord());
        blvo.setPkDeptApp(ord.getPkDept());
        blvo.setPkOrd(ord.getPkOrd());
        blvo.setPkDeptEx(ord.getPkDeptExec());
        blvo.setPkDeptNsApp(ord.getPkDeptNs());
        blvo.setPkEmpApp(ord.getPkEmpInput());
        blvo.setPkOrg(pkOrg);
        blvo.setPkOrgApp(ord.getPkOrg());
        blvo.setPkOrgEx(ord.getPkOrgExec());
        blvo.setPkPi(ord.getPkPi());
        blvo.setPkPv(ord.getPkPv());
        blvo.setPrice(ord.getPriceCg());
        blvo.setDateHap(new Date());
        //将执行单主键写为申请单的执行单主键
        for (Map.Entry<String, String> entry : ordExMap.entrySet()) {
            if (entry.getKey().equals(ord.getOrdsnParent().toString())) {
                blvo.setPkOrdexdt(entry.getValue());
                break;
            }
        }

        Integer cnt = 1;
        //根据频次计算周期执行次数
        if (!CommonUtils.isEmptyString(ord.getCodeFreq())) {
            cnt = DataBaseHelper.queryForScalar("select cnt from bd_term_freq where code = ? and del_flag = '0'", Integer.class, ord.getCodeFreq());
        }
        if (cnt == null) cnt = 1;
        //如果是物品，取零售单位，重新计算数量
        if ("1".equals(blvo.getFlagPd())) {
            blvo.setPkUnitPd(ord.getPkUnitCg());
            blvo.setQuanCg(MathUtils.mul(ord.getQuan(), cnt.doubleValue()));
            //blvo.setQuanCg(MathUtils.mul(ord.getQuan(),cnt.doubleValue());
            blvo.setBatchNo("~");
            blvo.setDateExpire(new Date());
            blvo.setPriceCost(ord.getPriceCg());
            blvo.setPkItem(ord.getPkOrd());
        } else {
            blvo.setQuanCg(MathUtils.mul(ord.getQuan(), cnt.doubleValue()));
        }
        return blvo;
    }

    /**
     * 查询待处理执行记录
     *
     * @param param{namePi,codeIp,dateBegin,dateEnd,pkDeptKL}
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryToCancelExList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) {
            paramMap = new HashMap<String, Object>();
        }
        paramMap.put("pkDept", ((User) user).getPkDept());
        return blIpPubMapper.queryMedToDoExList(paramMap);
    }

    /**
     * 已取消执行单-退费
     *
     * @param param
     * @param user
     */
    public void cancelCg(String param, IUser user) {
        List<ExListVo> list = JsonUtil.readValue(param, new TypeReference<List<ExListVo>>() {
        });
        if (list == null || list.size() <= 0) return;
        User u = (User) user;
        Object[] args = new Object[4];
        args[0] = new Date();
        args[1] = u.getPkEmp();
        args[2] = u.getNameEmp();

        for (ExListVo vo : list) {
            //退费
            args[3] = vo.getPkAssocc();
            retCg(vo.getPkExocc(), u, args, "1");
        }
    }

    /**
     * 查询医技申请单对应的收费项目
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpExItems(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) {
            throw new BusException("查询收费项目时未获取到医嘱项目主键！");
        }
        return blIpPubMapper.queryItemListByOrd(paramMap);
    }

    /**
     * 查询医技申请单
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryMedApp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) {
            throw new BusException("查询医技申请单时未获取到查询条件！");
        }
        return blIpPubMapper.queryMedAppList(paramMap);
    }


}
