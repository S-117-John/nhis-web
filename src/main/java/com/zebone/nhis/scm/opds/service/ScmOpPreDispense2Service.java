package com.zebone.nhis.scm.opds.service;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.scm.opds.dao.ScmOpPreDispense2Mapper;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 中山二院门诊配药相关需求开发
 *
 * @author Administrator
 */
@Service
public class ScmOpPreDispense2Service {

    @Resource
    private ScmOpPreDispense2Mapper scmOpPreDispense2Mapper;

    /**
     * 008003001021
     * 查询本机关联的窗口号
     *
     * @param param {"namePc":"计算机名"}
     * @param user
     * @return {"winno":"当前窗口号" }
     */
    public Map<String, Object> qryLocalWinNo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || paramMap.get("namePc") == null) return null;
        paramMap.put("pkDept", UserContext.getUser().getPkDept());
        return scmOpPreDispense2Mapper.qryLocalWinNo(paramMap);
    }

    /**
     * 008003001022
     * 查询可选择配药窗口
     *
     * @param param {"pkDept":"药房","euButype":"业务类型  （0配药1发药）","pkDeptunit":"当前窗口"}
     * @param user
     * @return 可选择窗口
     */
    public List<Map<String, Object>> qryDosageFormInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        return scmOpPreDispense2Mapper.qryDosageFormInfo(paramMap);
    }

    /**
     * 008003001023
     * 查询待配药处方信息
     *
     * @param param { "pkDept":"发药药房","winnoPrep":"配药窗口"}
     * @param user
     * @return 待配药处方信息
     */
    public List<Map<String, Object>> qryUnMatchPres(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        List<Map<String, Object>> resList = scmOpPreDispense2Mapper.qryUnMatchPres(paramMap);
        if(resList!=null && resList.size()>0) {
            List<String> pkPresocces = new ArrayList<>();
            resList.stream().forEach(m -> pkPresocces.add(MapUtils.getString(m, "pkPresocc")));
            paramMap.put("pkPresocces", pkPresocces);

            List<Map<String, Object>> pivasList = scmOpPreDispense2Mapper.qryPresPivasCount(paramMap);
            if(pivasList!=null && pivasList.size()>0) {
                resList.stream().forEach(m -> {
                            String pkPresocc = MapUtils.getString(m, "pkPresocc");
                            List<Map<String,Object>> tempList=pivasList.stream().filter(j -> ObjectUtils.nullSafeEquals(pkPresocc, MapUtils.getString(j, "pkPresocc"))).collect(Collectors.toList());
                            if(tempList!=null && tempList.size()>0){
                                m.put("num", MapUtils.getString(tempList.get(0), "num"));
                            }
                        }
                );
            }
        }
        return resList;
    }

    /**
     * 008003001024
     * 查询已完成配药处方信息
     *
     * @param param {"pkDept":"发药药房","winnoPrep":"配药窗口"}
     * @param user
     * @return 已配药未发药处方信息
     */
    public List<Map<String, Object>> qryMatchPres(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        String date = DateUtils.getDate("yyyyMMdd");
        String dateStart = date + "000000";
        String DateEnd = date + "235959";
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", DateEnd);
        List<Map<String, Object>> resList = scmOpPreDispense2Mapper.qryMatchPres(paramMap);
        return resList;
    }

    /**
     * 008003001025
     * 查询未配药暂挂的处方信息
     *
     * @param param {"pkDept":"发药药房","winnoPrep":"配药窗口"}
     * @param user
     * @return 配药暂挂的处方信息
     */
    public List<Map<String, Object>> qryPresPending(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        List<Map<String, Object>> resList = scmOpPreDispense2Mapper.qryPresPending(paramMap);
        return resList;
    }

    /**
     * 008003001026
     * 查询处方明细
     *
     * @param param {"pkPresocc":"处方主键 集合","pkDept":"当前科室"}
     * @param user
     * @return 处方明细
     */
    public List<Map<String, Object>> qryPresDetail(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return null;
        List<Map<String, Object>> resList = scmOpPreDispense2Mapper.qryPresDetail(paramMap);
        return resList;
    }

    /**
     * 008003001027
     * 修改处方状态为打印
     *
     * @param param {"pkPresoccs":"处方"}
     * @param user
     */
    public void updatePresPrintInfo(String param, IUser user) {
        List<String> pkPresoccs = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        if (pkPresoccs == null || pkPresoccs.size() <= 0) throw new BusException("未获得取处方信息！");
        scmOpPreDispense2Mapper.updatePresPrintInfo(pkPresoccs);
    }

    /**
     * 008003001028
     * 确认配药
     *
     * @param param {"pkPresocc":"配药处方","datePrep":"配药时间"," pkEmp":"配药人","nameEmp":"配药人姓名"}
     * @param user
     */
    public void saveConfirmDosageInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return;
        /**电子药篮，采取重复绑定,同一个患者一次就诊允许药篮绑定多个处方，*/
        if (paramMap.containsKey("codeBasket")) {
            String codeBasket = MapUtils.getString(paramMap, "codeBasket");
            if (StringUtils.isNotBlank(codeBasket)) {
                scmOpPreDispense2Mapper.updateCodeBasketNull(paramMap);
            }
        }
        scmOpPreDispense2Mapper.saveConfirmDosageInfo(paramMap);
        paramMap.put("opType", "0");
        PlatFormSendUtils.execute(paramMap, "sendConfirmDosage");
    }

    /**
     * 008003001029
     * 取消配药
     *
     * @param param {"pkPresocc":"配药处方","pkEmp":"取消配药人" }
     * @param user
     */
    public Integer saveUnPresDosageInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return 0;
		/*List<ExPresOcc> presVos = DataBaseHelper.queryForList(
				"select * from ex_pres_occ where pk_presocc in (:pkPresoccs)",
				ExPresOcc.class, paramMap);
		for(int i=0;i<presVos.size();i++){
			ExPresOcc presVo=presVos.get(i);
			/*String winnoPrep = presVo.getWinnoPrep();
			String winnoConf = presVo.getWinnoConf();
			String pkDept=((User)user).getPkDept();
			//更新配药窗口工作量（增加处方数）更新发药窗口工作量（减少处方数）
			OpDrugPubUtils.updWinVol(1,pkDept,winnoConf,-1);
			OpDrugPubUtils.updWinVol(0,pkDept,winnoPrep,1);
		}*/
        int count = scmOpPreDispense2Mapper.cancelPresDosageInfo(paramMap);
        paramMap.put("opType", "2");//平台新增的状态，代表下屏可以继续上屏
        PlatFormSendUtils.execute(paramMap, "sendConfirmDosage");
        return count;
    }

    /**
     * 008003001030
     * 处理处方暂挂信息
     *
     * @param param {"pkPresoccs":"配药处方"}
     * @param user
     */
    public void doPresPending(String param, IUser user) {
        List<String> pkPresoccs = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        if (pkPresoccs == null || pkPresoccs.size() == 0) return;
        scmOpPreDispense2Mapper.doPresPending(pkPresoccs);
    }

    /**
     * 008003001031
     * 获取配药人员工号信息
     *
     * @param param
     * @param user
     * @return
     */
    public BdOuEmployee getPrepUserCode(String param, IUser user) {
        String codeEmp = JsonUtil.getFieldValue(param, "codeEmp");
        String sql = "select * from BD_OU_EMPLOYEE where code_emp=?";
        List<BdOuEmployee> empList = DataBaseHelper.queryForList(sql, BdOuEmployee.class, new Object[]{codeEmp});
        if (empList != null && empList.size() > 0) {
            return empList.get(0);
        } else {
            return null;
        }
    }

    /**
     *008003001037
     * 获取录入的配药人是否有配药的权限
     * @param param
     * @param user
     * @return
     */
    public Integer getUserDepartment(String param, IUser user){
        String pkEmp = JsonUtil.getFieldValue(param, "pkEmp");
        String pkDept = JsonUtil.getFieldValue(param, "pkDept");

        String sql = "select * from bd_ou_user where pk_emp=?";
        BdOuUser ouUser= DataBaseHelper.queryForBean(sql, BdOuUser.class, new Object[]{pkEmp});
        Map<String, Object> paramMap = new HashMap<String, Object>();
        int count=0;
        if(ouUser!=null)
        {
            paramMap.put("pkUsrGrp",ouUser.getPkUsrgrp());
            paramMap.put("pkUser",ouUser.getPkUser());
            paramMap.put("pkDept",pkDept);
            count=scmOpPreDispense2Mapper.qryUserDepartment(paramMap).size();
        }
        return count;
    }

    /**
     * 008003001032
     * 取消打印操作
     *
     * @param param
     * @param user
     */
    public void cancelPrepPrint(String param, IUser user) {
        List<String> pkPresoccs = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        if (pkPresoccs == null || pkPresoccs.size() == 0) return;
        Set<String> pkPresoccSets = new HashSet<String>();
        for (String pkPresocc : pkPresoccs) {
            pkPresoccSets.add(pkPresocc);
        }
        String sql = "update ex_pres_occ set flag_preprint='0' where (pk_presocc in (" + CommonUtils.convertSetToSqlInPart(pkPresoccSets, "ord.pk_cnord") + ")) and eu_status in ('0','1','8')";
        int count = DataBaseHelper.execute(sql, new Object[]{});
        if (count != pkPresoccSets.size()) {
            throw new BusException("您所选的数据可能已经发生变更，请刷新后重新操作处理！");
        }
    }

    /**
     * 008003001033
     * 变更配药打印状态
     *
     * @param param
     * @param user
     */
    public void updatePrepPrint(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) return;
        List<String> pkPresoccs = (List<String>) paramMap.get("pkPresoccs");
        String flagPreprint = (String) paramMap.get("flagPreprint");
        String euStatus = MapUtils.getString(paramMap, "euStatus");
        Set<String> pkPresoccSets = new HashSet<String>();
        for (String pkPresocc : pkPresoccs) {
            pkPresoccSets.add(pkPresocc);
        }
        StringBuilder sql = new StringBuilder();
        sql.append("update ex_pres_occ set cnt_print=cnt_print+1  ");
        if (!CommonUtils.isEmptyString(flagPreprint) && "1".equals(flagPreprint)) {
            sql.append(",flag_preprint='1'");
        }
        if ("0".equals(euStatus)) {
            sql.append(" , eu_status='1' ");
        }
        sql.append(" where (pk_presocc in (");
        sql.append(CommonUtils.convertSetToSqlInPart(pkPresoccSets, "pk_presocc"));
        sql.append(")) and eu_status in ('1','0','2','8')");
        int count = DataBaseHelper.execute(sql.toString(), new Object[]{});
        if (count != pkPresoccSets.size()) {
            throw new BusException("您所选的数据可能已经发生变更，请刷新后重新操作处理！");
        }
    }

    /**
     * 008003002024
     * 校验处方是否已经发药
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> checkPresInfo(String param, IUser user) {
        String presNo = JsonUtil.getFieldValue(param, "presNo");
        if (CommonUtils.isNull(presNo)) return null;
        String sql = "select eu_status from ex_pres_occ where pres_no=?";
        List<Map<String, Object>> resList = DataBaseHelper.queryForList(sql, new Object[]{presNo});
        if (resList == null || resList.size() == 0) return null;
        return resList.get(0);
    }
}
