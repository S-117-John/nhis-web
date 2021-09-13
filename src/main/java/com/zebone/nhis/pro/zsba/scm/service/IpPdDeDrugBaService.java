package com.zebone.nhis.pro.zsba.scm.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.scm.pub.BdPdDecate;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.zsba.scm.dao.IpPdDeDrugBaMapper;
import com.zebone.nhis.pro.zsba.scm.vo.IpDeDrugBaDto;
import com.zebone.nhis.scm.ipdedrug.vo.IpDeDrugDto;
import com.zebone.nhis.scm.pub.service.DeDrugExtPubService;
import com.zebone.nhis.scm.pub.service.IpDeDrugPubService;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
public class IpPdDeDrugBaService {
    @Autowired
    private IpDeDrugPubService ipDeDrugPubService;

    @Autowired
    private DeDrugExtPubService deDrugExtPubService;

    @Autowired
    private IpPdDeDrugBaMapper ipPdDeDrugBaMapper;

    private Logger logger = LoggerFactory.getLogger("nhis.scm");


    /**
     * 住院(医嘱，处方)发药处理---博爱版本
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> mergeIpDeDrug(String param, IUser user) {
        // 前台传过来是勾选的请领明细
        List<ExPdApplyDetail> exPdAppDetails = JsonUtil.readValue(param, new TypeReference<List<ExPdApplyDetail>>() {
        });
        List<ExPdApplyDetail> dtlist = new ArrayList<ExPdApplyDetail>();
        List<ExPdApplyDetail> extDtlist = new ArrayList<ExPdApplyDetail>();
        List<Map<String, Object>> codeDecateList = new ArrayList<Map<String, Object>>();
        User u = (User) user;
        String codeDe = null;
        //1.根据发放分类循环:根据发放分类分组
        Map<String, List<ExPdApplyDetail>> map = groupByDecate(exPdAppDetails);
        for (Map.Entry<String, List<ExPdApplyDetail>> exPdAppDetail : map.entrySet()) {
            List<ExPdApplyDetail> exPdApplyDetail = exPdAppDetail.getValue();
            if (CollectionUtils.isEmpty(exPdApplyDetail)) {
                throw new BusException("未获取到本次发药明细信息！");
            }
            //2.根据请领明细中的发放方式循环
            for (ExPdApplyDetail dt : exPdApplyDetail) {
                if ("9".equals(dt.getEuDetype())) {
                    extDtlist.add(dt);
                } else {
                    dtlist.add(dt);
                }
            }
            if (dtlist.size() > 0) {
                List<PdDeDrugVo> drugVo = ipDeDrugPubService.mergeIpDeDrug(exPdApplyDetail, "0", "1", null, new Date());
                if (drugVo != null && drugVo.size() != 0)
                    codeDe = drugVo.get(0).getCodeDe();
                try {
                    //调用接口发送药品至包药机
                    ExtSystemProcessUtils.processExtMethod("PackMachine", "sendToMah", drugVo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (extDtlist.size() > 0) {
                if (CommonUtils.isEmptyString(codeDe))
                    codeDe = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_IPDS);
                deDrugExtPubService.ipDeDrug(extDtlist, u.getPkStore(), codeDe, new Date(), u, param);
            }
            //调用接口住院静配接口
            ExtSystemProcessUtils.processExtMethod("PIVAS", "saveTorders", exPdAppDetails);
            Map<String, Object> codeDecate = new HashMap<String, Object>();
            codeDecate.put("pkPddecate", exPdAppDetail.getKey());
            codeDecate.put("codeDe", codeDe);
            codeDecateList.add(codeDecate);
        }
        return codeDecateList;
    }

    /**
     * 将请领明细根据发放分类分组
     *
     * @param exPdAppDetails
     * @return
     */
    private Map<String, List<ExPdApplyDetail>> groupByDecate(List<ExPdApplyDetail> exPdAppDetails) {
        Map<String, List<ExPdApplyDetail>> map = new HashMap<String, List<ExPdApplyDetail>>();
        for (ExPdApplyDetail apdt : exPdAppDetails) {
            if (map.containsKey(apdt.getPkPddecate())) {
                List<ExPdApplyDetail> pdDt = map.get(apdt.getPkPddecate());
                pdDt.add(apdt);
                map.put(apdt.getPkPddecate(), pdDt);
            } else {
                List<ExPdApplyDetail> pdDt = Lists.newArrayList();
                pdDt.add(apdt);
                map.put(apdt.getPkPddecate(), pdDt);
            }
        }
        return map;
    }

    /**
     * 住院发药查询-按科室汇总显示
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPdApplyListByDept(String param, IUser user) {
        IpDeDrugBaDto ipDeDrugBaDto = JsonUtil.readValue(param, IpDeDrugBaDto.class);
        User userCur = (User) user;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkDeptDe", userCur.getPkDept());
        paramMap.put("pkDeptAp", ipDeDrugBaDto.getPkAppDeptNs());
        paramMap.put("flagDept", ipDeDrugBaDto.getFlagDept());
        paramMap.put("isPres", ipDeDrugBaDto.getIsPres());
        paramMap.put("nameApDept", ipDeDrugBaDto.getNameApDept());
        paramMap.put("flagEmer", ipDeDrugBaDto.getFlagEmer());
        //paramMap.put("deCateWhereSql", ipDeDrugDto.getDeCateWhereSql());
        List<Map<String, Object>> mapResult = ipPdDeDrugBaMapper.qryPdApplyListByDept(paramMap);
        List<Map<String, Object>> mapEmer = ipPdDeDrugBaMapper.qryPdApplyListEmerByDept(paramMap);
        if (mapResult != null) {
            for (Map<String, Object> map : mapResult) {
                String pkDeptAp = MapUtils.getString(map, "pkDeptAp");
                if (StringUtils.isEmpty(pkDeptAp)) {
                    continue;
                }
                for (Map<String, Object> emer : mapEmer) {
                    if (pkDeptAp.equals(MapUtils.getString(emer, "pkDeptAp"))
                            && Integer.parseInt(emer.get("quanpack").toString()) > 0) {
                        map.put("quanEmer", 1);
                        break;
                    }
                }
            }
        }
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 根据方法分类获取打印类型信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryDecate(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        StringBuilder sql = new StringBuilder("select " +
                "PK_PDDECATE,PK_ORG,EU_RANGE,PK_ORG_USE,PK_DEPT_USE,CODE_DECATE,NAME_DECATE," +
                "NOTE,WHERESQL,CODE_DECATE as code,NAME_DECATE as name," +
                " '' as d_code,CODE_DECATE AS py_code,EU_DOCTYPE,FLAG_LABEL  from BD_PD_DECATE where (EU_TYPE='0' or EU_TYPE='1') ");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (!StringUtils.isEmpty(paramMap.get("pk"))) {
            sql.append(" and pk_pddecate=?");
            String pkDecate = paramMap.get("pk").toString();
            list = DataBaseHelper.queryForList(sql.toString(), pkDecate);
        } else {
            list = DataBaseHelper.queryForList(sql.toString(), new Object[]{});
        }
        return list;
    }

    /***
     * @Description 查询发放分类，去除不包含请领明细的分类
     * @auther wuqiang
     * @Date 2020-07-27
     * @Param [param, user]
     * @return java.util.List<com.zebone.nhis.common.module.scm.pub.BdPdcate>
     */
    public List<BdPdDecate> getBdPdCatesPdDetialNotEmpty(String param, IUser user) {
        IpDeDrugBaDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugBaDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkDept(userCur.getPkDept());
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        ipDeDrugDto.setPkStore(userCur.getPkStore());
        ipDeDrugDto.setDistributionType(CommonUtils.isNull(ipDeDrugDto.getDistributionType()) ? 0 : ipDeDrugDto.getDistributionType());
        List<BdPdDecate> bdPdcates = ipPdDeDrugBaMapper.getBdPdCates(ipDeDrugDto);
        if (CollectionUtils.isEmpty(bdPdcates)) {
            return new ArrayList<BdPdDecate>();
        }
        return getCnOrderNotEmpty(bdPdcates, ipDeDrugDto);
    }

    /**
     * @return java.util.List<com.zebone.nhis.common.module.scm.pub.BdPdDecate>
     * @Description 查询明细不为空的发放分类
     * 公用不查明细，
     * @auther wuqiang
     * @Date 2020-07-30
     * @Param [bdPdcates, ipDeDrugDto]
     */
    private List<BdPdDecate> getCnOrderNotEmpty(List<BdPdDecate> bdPdcates, IpDeDrugBaDto ipDeDrugDto) {
        List<Map<String, Object>> maps = new ArrayList<>(5);
        if (EnumerateParameter.ZERO.equals(String.valueOf(ipDeDrugDto.getDistributionType()))) {
            return bdPdcates;
        }
        if (EnumerateParameter.ONE.equals(String.valueOf(ipDeDrugDto.getDistributionType()))) {
            maps = ipPdDeDrugBaMapper.getCnOrderNotEmpty(bdPdcates, ipDeDrugDto);
        }
        if (EnumerateParameter.TWO.equals(String.valueOf(ipDeDrugDto.getDistributionType()))) {
            maps = ipPdDeDrugBaMapper.getCnOrderDeptNotEmpty(bdPdcates, ipDeDrugDto);
        }
        List<BdPdDecate> bdPdDecates = new ArrayList<>(5);
        for (BdPdDecate bd : bdPdcates) {
            for (Map<String, Object> map : maps) {
                boolean isNotEmpty = bd.getPkPddecate().equals(map.get("pkPddecate")) && Integer.parseInt(String.valueOf(map.get("numCou"))) > 0;
                if (isNotEmpty) {
                    bdPdDecates.add(bd);
                }
            }
        }
        return bdPdDecates;
    }


    /**
     * 发药请领单查询(处方，医嘱，科室)
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpAppListByCDT(String param, IUser user) {
        IpDeDrugBaDto ipDeDrugBaDto = JsonUtil.readValue(param, IpDeDrugBaDto.class);
        User userCur = (User) user;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkDeptDe", userCur.getPkDept());
        if (ipDeDrugBaDto.getDateStart() != null) {
            paramMap.put("dateStart", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.getDateMorning(ipDeDrugBaDto.getDateStart(), 0)));
        }
        if (ipDeDrugBaDto.getDateEnd() != null) {
            paramMap.put("dateEnd", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.getDateMorning(ipDeDrugBaDto.getDateEnd(), 1)));
        }
        paramMap.put("pkDeptAp", ipDeDrugBaDto.getPkAppDeptNs());
        paramMap.put("flagDept", ipDeDrugBaDto.getFlagDept());
        paramMap.put("isPres", ipDeDrugBaDto.getIsPres());
        paramMap.put("deCateWhereSql", ipDeDrugBaDto.getDeCateWhereSql());
        paramMap.put("codeIp", ipDeDrugBaDto.getCodeIp());
        paramMap.put("presNo", ipDeDrugBaDto.getPresNo());
        paramMap.put("dtPrestype", ipDeDrugBaDto.getDtPrestype());
        paramMap.put("euDirect", ipDeDrugBaDto.getEuDirect());//发退方向
        List<Map<String, Object>> mapResult = ipPdDeDrugBaMapper.qryExPdApplyList(paramMap);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }

    /**
     * 查询停发医嘱--博爱
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryStopApply(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        if (paramMap != null) {
            mapList = ipPdDeDrugBaMapper.qryStopApply(paramMap);
        }
        return mapList;
    }

    /**
     * 查询请领单数量
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Integer> queryApplyNumber(String param, IUser user) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        User userCur = (User) user;
        String pkDept = userCur.getPkDept();
        //1.查询医嘱请领数量
        int orderNumber = ipPdDeDrugBaMapper.queyOrderApplyNumber(pkDept);
        //2.查询处方请领数量
        int presNumber = ipPdDeDrugBaMapper.queyPresApplyNumber(pkDept);
        //3.查询科室领药数量
        int deptNumber = ipPdDeDrugBaMapper.queyDeptApplyNumber(pkDept);
        //4.查询加急数量
        int emerNumber = ipPdDeDrugBaMapper.queyEmerApplyNumber(pkDept);
        int deptBackNumber = ipPdDeDrugBaMapper.queyDeptApplyBackNumber(pkDept);
        map.put("orderNumber", orderNumber);
        map.put("presNumber", presNumber);
        map.put("deptNumber", deptNumber);
        map.put("emerNumber", emerNumber);
        map.put("deptBackNumber", deptBackNumber);
        return map;
    }

    /**
     * 科室医嘱停发处理--中山二院
     *
     * @param param {"pkPdapDts":"领药明细主键集合(List<String>)",
     *              "dtExdeptpdstop":"停发原因",
     *              "reasonStop":"原因描述",
     *              "pkEmpStop":"停发人Id",
     *              "nameEmpStop":"停发人姓名"}
     * @param user
     */
    public void saveStopApplyReason(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap != null) {
            List<String> pkPdapdts = (List<String>) paramMap.get("pkPdapDts");
            Set<String> apdtSet = new HashSet<String>();
            for (String apdt : pkPdapdts) {
                apdtSet.add(apdt);
            }
            paramMap.put("pkPdapDts", apdtSet);
            int count = ipPdDeDrugBaMapper.saveStopApplyReason(paramMap);
            if (count != apdtSet.size()) {
                throw new BusException("您本次操作的记录已变更，请刷新后处理！");
            }
            //博爱 2.2.8.6 如果此请领单在停发处理后没有可发放的药品明细，更新发药单主表完成标志
            StringBuilder sbl = new StringBuilder();
            sbl.append(" update EX_PD_APPLY set FLAG_FINISH='1' where PK_PDAP in ( select ad.pk_pdap from EX_PD_APPLY_DETAIL ad where ad.PK_PDAP in (")
                    .append("select ap.PK_PDAP from EX_PD_APPLY ap inner join EX_PD_APPLY_DETAIL dt on ap.PK_PDAP = dt.PK_PDAP where dt.PK_PDAPDT in(")
                    .append(CommonUtils.convertSetToSqlInPart(apdtSet, "dt.PK_PDAPDT"))
                    .append(")) group by ad.pk_pdap having count(1) = sum(case when (ad.FLAG_FINISH = '1' or ad.FLAG_STOP = '1') then 1 else 0 end)")
                    .append(") and nvl(FLAG_FINISH,'0')='0'");
            DataBaseHelper.update(sbl.toString());
        }
    }

    /**
     * 取消科室停发--中山二院
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
            int count = ipPdDeDrugBaMapper.cancelStopApply(apdtList);
            if (count != apdtList.size()) {
                throw new BusException("您本次操作的记录已变更，请刷新后处理！");
            }
            DataBaseHelper.update("update EX_PD_APPLY set FLAG_FINISH='0' where PK_PDAP in (select PK_PDAP from EX_PD_APPLY_DETAIL where PK_PDAPDT in ("
                    + CommonUtils.convertSetToSqlInPart(pkPdapDts, "PK_PDAPDT") + "))");
        }
    }
    
    /**
     * 根据条件查询发退药打印记录
     * 交易号:022003022006
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpDeDrugPrtRecordByCDT(String param, IUser user) {
        IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
        User userCur = (User) user;
        ipDeDrugDto.setPkDept(userCur.getPkDept());
        ipDeDrugDto.setPkOrg(userCur.getPkOrg());
        List<Map<String, Object>> mapResult = ipPdDeDrugBaMapper.qryDeDrugPrintRecord(ipDeDrugDto);
        return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
    }
}
