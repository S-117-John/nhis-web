package com.zebone.nhis.cn.ipdw.service;

import com.zebone.nhis.base.ou.vo.BdOuDeptType;
import com.zebone.nhis.cn.ipdw.dao.PathTemplateAuditMapper;
import com.zebone.nhis.common.module.base.bd.wf.BdFlow;
import com.zebone.nhis.common.module.base.bd.wf.BdFlowBp;
import com.zebone.nhis.common.module.base.bd.wf.BdFlowStep;
import com.zebone.nhis.common.module.cn.cp.SyxCpTemp;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PathTemplateAuditService {

    @Autowired
    private PathTemplateAuditMapper pathTemplateAuditMapper;

    //路径模板审核列表查询：004004012017
    public List<Map<String, Object>> qryAudit(String param, IUser user) {
        User u = (User) user;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pkDept", u.getPkDept());
        map.put("pkEmp", u.getPkEmp());
        return pathTemplateAuditMapper.qryAudit(map);
    }

    //路径模板审核通过：004004012028
    public void passAudit(String param, IUser user) {
        User u = (User) user;
        List<Map<String, Object>> list = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        if (list != null && list.size() > 0) {
            for (Map<String, Object> m : list) {
                String pkBppk = (String) m.get("pkCptemp");//主键
                String note = (String) m.get("opinion");//审核意见
                String pkDept = (String) m.get("pkDept");//科室主键
                //根据模板主键更新审核记录
                String updateSql = "update bd_flow_bp set pk_emp=?,name_emp=?,date_chk=?,"
                        + "eu_result='1',note=? where pk_bppk=? and pk_dept=? and eu_result='0'";
                DataBaseHelper.execute(updateSql, u.getPkEmp(), u.getNameEmp(), new Date(), note, pkBppk, pkDept);
                //查询关联的审批流定义
                String pkFlow = (String) m.get("pkFlow");
                BdFlow bdFlow = DataBaseHelper.queryForBean("select * from bd_flow where pk_flow = ?", BdFlow.class, pkFlow);
                List<BdFlowStep> stepList = DataBaseHelper.queryForList("select * from bd_flow_step where pk_flow = ? and DEL_FLAG='0'  order by sortno", BdFlowStep.class, pkFlow);
                String pkFlowstep = (String) m.get("pkFlowstep");//审批流步骤主键
                //按序号获取当前审批步骤的下一步
                BdFlowStep nextStep = getNextStep(stepList, pkFlowstep);
                if (nextStep == null) throw new BusException("审批流步骤获取失败！");
                String pkCptemp = (String) m.get("pkCptemp");//主键
                //药学部审核时，药理分类的内容不能为空
                List<BdOuDeptType> deptTypes = DataBaseHelper.queryForList("select * from bd_ou_dept_type where pk_dept = ?", BdOuDeptType.class, u.getPkDept());
                if (deptTypes != null && deptTypes.size() > 0 && isDepartmentOfMedicine(deptTypes)) {
                    Integer count = pathTemplateAuditMapper.existNullPharm(pkCptemp);
                    if (count > 0) {
                        throw new BusException("路径定义-重点医嘱-替代方式为药理分类的药理分类内容不能为空！");
                    }
                }
                //如果审核的节点为该业务流的最后节点（flag_end=1），不生成记录，同时更新cp_temp
                if (pkFlowstep.equals(nextStep.getPkFlowstep()) && "1".equals(nextStep.getFlagEnd())) {
                    String sql = "update cp_temp set eu_status='8',pk_emp_chk=?,name_emp_chk=?,date_chk=? ," +
                            "DATE_PUB=?,PK_EMP_PUB=?,NAME_EMP_PUB=?  where pk_cptemp=?";
                    DataBaseHelper.execute(sql, u.getPkEmp(), u.getNameEmp(), new Date(), new Date(), u.getPkEmp(), u.getNameEmp(), pkCptemp);
                } else {
                    BdFlowBp bp = new BdFlowBp();
                    bp.setPkFlow(pkFlow);
                    bp.setPkBppk(pkBppk);
                    bp.setCodeFlow(bdFlow.getCodeFlow());
                    bp.setNameFlow(bdFlow.getNameFlow());
                    bp.setDateSubmit(new Date());
                    //0-科室审批，1-人员审批
                    if ("0".equals(bdFlow.getEuType())) {
                        //0-其他科室，1-本科室
                        if ("0".equals(nextStep.getEuDepttype())) {
                            bp.setPkDept(nextStep.getPkDept());
                        } else {
                            SyxCpTemp syxCpTemp = DataBaseHelper.queryForBean("select PK_DEPT " +
                                    "from CP_TEMP CT where PK_CPTEMP=?", SyxCpTemp.class, pkBppk);
                            bp.setPkDept(syxCpTemp.getPkDept());
                        }
                    } else {
                        bp.setPkEmp(nextStep.getPkEmp());
                    }
                    bp.setPkFlowstep(nextStep.getPkFlowstep());
                    bp.setPkFlowstepPre(pkFlowstep);
                    bp.setNote(note);
                    bp.setEuResult("0");
                    DataBaseHelper.insertBean(bp);
                }
            }
        }
    }

    //判断登陆科室是否药学部
    private boolean isDepartmentOfMedicine(List<BdOuDeptType> list) {
        for (BdOuDeptType bdOuDeptType : list) {
            if (bdOuDeptType.getDtDepttype().indexOf("04") != -1) {
                return true;
            }
        }
        return false;
    }

    //按序号获取当前审批步骤的下一步
    private BdFlowStep getNextStep(List<BdFlowStep> list, String pkFlowstep) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                BdFlowStep step = list.get(i);
                if (pkFlowstep.equals(step.getPkFlowstep())) {
                    //如果审核的节点为该业务流的最后节点（flag_end=1），则返回审核的节点
                    if ("1".equals(step.getFlagEnd())) {
                        return step;
                    }
                    return list.get(i + 1);
                }
            }
        }
        return null;
    }

    //路径模板审核退回：004004012029
    public void backAudit(String param, IUser user) {
        User u = (User) user;
        List<Map<String, Object>> list = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        if (list != null && list.size() > 0) {
            for (Map<String, Object> m : list) {
                String pkFlowstep = (String) m.get("pkFlowstep");//当前步骤
                String pkFlowstepBack = (String) m.get("pkFlowstepBack");//退回步骤
                String pkFlowstepPre = (String) m.get("pkFlowstepPre");//上一步骤
                String note = (String) m.get("opinion");//审核意见
                String pkBppk = (String) m.get("pkCptemp");//主键
                while (true) {
                    //判断退回步骤pk_flowstep_back是否等于当前步骤的上一步骤pk_flowstep；
                    //如果为true，更新当前步骤为退回状态，并修改前一步骤pk_flowstep_pre为未审核状态
                    if (pkFlowstepPre.equals(pkFlowstepBack)) {
                        updateBackStatus(u, note, pkBppk, pkFlowstep);
                        updateNotAuditStatus(note, pkBppk, pkFlowstepPre);
                        break;
                    } else {
                        updateBackStatus(u, note, pkBppk, pkFlowstep);
                        updateNotAuditStatus(note, pkBppk, pkFlowstepPre);
                        String sql = "select * from bd_flow_bp where pk_bppk=? and pk_flowstep=? ";
                        //查询当前业务流的上一步骤并赋值
                        BdFlowBp bp = DataBaseHelper.queryForBean(sql, BdFlowBp.class, pkBppk, pkFlowstep);
                        pkFlowstep = bp.getPkFlowstepPre();
                        //查询当前步骤的上一步骤的上一步骤
                        BdFlowBp preBp = DataBaseHelper.queryForBean(sql, BdFlowBp.class, pkBppk, bp.getPkFlowstepPre());
                        pkFlowstepPre = preBp.getPkFlowstepPre();
                    }
                }
                //如果退回步骤为该业务流的起始节点（flag_start=1），同时更新模板为未提交状态
                BdFlowStep step = DataBaseHelper.queryForBean("select * from bd_flow_step where pk_flowstep=?", BdFlowStep.class, pkFlowstepBack);
                if ("1".equals(step.getFlagStart())) {
                    String update = "update cp_temp set eu_status='0',pk_emp_entry=null,name_emp_entry=null,date_chk=null where pk_cptemp=?";
                    DataBaseHelper.execute(update, pkBppk);
                }
            }
        }
    }

    //修改前一步骤pk_flowstep_pre为未审核状态
    private void updateNotAuditStatus(String note, String pkBppk,
                                      String pkFlowstepPre) {
        String preSql = "update bd_flow_bp set date_chk=null,eu_result='0',note=? "
                + "where pk_bppk=? and pk_flowstep=? and eu_result='1'";
        DataBaseHelper.execute(preSql, note, pkBppk, pkFlowstepPre);
    }

    //更新当前步骤为退回步骤
    private void updateBackStatus(User u, String note, String pkBppk, String pkFlowstep) {
        String currentSql = "update bd_flow_bp set pk_emp=?,name_emp=?,date_chk=?,"
                + "eu_result='9',note=? where pk_bppk=? and pk_flowstep=? and eu_result='0'";
        DataBaseHelper.execute(currentSql, u.getPkEmp(), u.getNameEmp(), new Date(), note, pkBppk, pkFlowstep);
    }

    //路径模板取消审核：004004012030（作废）
    public void cancelAudit(String param, IUser user) {
        List<Map<String, Object>> list = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        if (list != null && list.size() > 0) {
            for (Map<String, Object> m : list) {
                String pkBppk = (String) m.get("pkCptemp");//主键
                String pkDept = (String) m.get("pkDept");//科室主键
                String sql = "update bd_flow_bp set pk_emp=null,name_emp=null,date_chk=null,"
                        + "eu_result='0',note=null where pk_bppk=? and pk_dept=? and eu_result='1'";
                DataBaseHelper.execute(sql, pkBppk, pkDept);
            }
        }
    }

    //路径模板取消提交：004004012044
    public void cancelSubmitAudit(String param, IUser user) {
        User u = (User) user;
        List<Map<String, Object>> list = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        if (list != null && list.size() > 0) {
            for (Map<String, Object> m : list) {
                String pkFlowstep = (String) m.get("pkFlowstep");//当前步骤
                String note = (String) m.get("opinion");//审核意见
                String pkBppk = (String) m.get("pkCptemp");//主键
                //改为退回状态
                updateBackStatus(u, note, pkBppk, pkFlowstep);
                //模板表状态更新为保存
                String sql = "update cp_temp set eu_status = '0' where pk_cptemp = ? and eu_status = '1'";
                DataBaseHelper.execute(sql, pkBppk);
            }
        }
    }
}
