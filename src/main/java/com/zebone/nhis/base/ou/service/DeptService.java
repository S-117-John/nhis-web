package com.zebone.nhis.base.ou.service;

import com.zebone.nhis.base.ou.vo.BdOuDeptType;
import com.zebone.nhis.base.pub.dao.DeptMapper;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 科室服务
 *
 * @author Xulj
 */
@Service
public class DeptService {

    /**
     * 添加状态
     **/
    public static final String AddState = "_ADD";

    /**
     * 更新状态
     */
    public static final String UpdateState = "_UPDATE";

    /**
     * 删除状态
     */
    public static final String DelState = "_DELETE";

    @Resource
    private DeptMapper deptMapper;

    //增删改标志
    private String rleCode = "";


    /**
     * 科室的保存和更新
     *
     * @param param
     * @param user
     */
    public BdOuDept saveDept(String param, IUser user) {
        BdOuDept dept = JsonUtil.readValue(param, BdOuDept.class);
        BdOuDept updBeforeDept = null;
        if (dept.getPkDept() != null) {
            updBeforeDept = DataBaseHelper.queryForBean("select * from bd_ou_dept where pk_dept=?", BdOuDept.class, dept.getPkDept());
        }
        //保存和更新科室信息
        BdOuDept bd = saveDEPT(dept);
        List<BdOuDeptType> depts = dept.getDepts();
        if (CollectionUtils.isNotEmpty(depts)) {
            for (BdOuDeptType bdOuDeptType : depts) {
                bdOuDeptType.setPkDept(dept.getPkDept());
                if (bdOuDeptType.getPkDepttype() == null) {
                    DataBaseHelper.insertBean(bdOuDeptType);
                } else {
                    DataBaseHelper.updateBeanByPk(bdOuDeptType, false);
                }
            }
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dept", dept);
        //添加修改前查询数据，中山人民医院需要使用_2020-10-28
        paramMap.put("beforeModification", updBeforeDept);
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("STATUS", rleCode);//状态
        PlatFormSendUtils.sendBdOuDeptMsg(paramMap);
        paramMap = null;
        return bd;
    }

    public BdOuDept saveDEPT(BdOuDept dept) {
        String delFlag = "0";
        if (dept.getPkDept() == null) {
            int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
                    + "where code_dept=? and pk_org like ?||'%' and DEL_FLAG = '0'", Integer.class, dept.getCodeDept(), dept.getPkOrg());
            int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
                    + "where name_dept=? and pk_org like ?||'%' and DEL_FLAG = '0'", Integer.class, dept.getNameDept(), dept.getPkOrg());
            if (count_code == 0 && count_name == 0) {
                dept.setCodeDept(codeDeptHandle(dept.getCodeDept()));
                dept.setModityTime(new Date());
                if (dept.getDelFlag() == null) {
                    dept.setDelFlag(delFlag);
                }
                DataBaseHelper.insertBean(dept);
                rleCode = this.AddState;
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("科室编码重复！");
                } else if (count_code == 0 && count_name != 0) {
                    throw new BusException("科室名称重复！");
                } else {
                    throw new BusException("科室编码和名称都重复！");
                }
            }
        } else {
            int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
                    + "where code_dept=? and pk_org like ?||'%' and pk_dept != ? and DEL_FLAG = '0'", Integer.class, dept.getCodeDept(), dept.getPkOrg(), dept.getPkDept());
            int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
                    + "where name_dept=? and pk_org like ?||'%' and pk_dept != ? and DEL_FLAG = '0'", Integer.class, dept.getNameDept(), dept.getPkOrg(), dept.getPkDept());
            if (count_code == 0 && count_name == 0) {
                ApplicationUtils.setDefaultValue(dept, false);
                dept.setModityTime(new Date());
                DataBaseHelper.updateBeanByPk(dept, false);
                rleCode = this.UpdateState;
            } else {
                if (count_code != 0 && count_name == 0) {
                    throw new BusException("科室编码重复！");
                } else if (count_code == 0 && count_name != 0) {
                    throw new BusException("科室名称重复！");
                } else {
                    throw new BusException("科室编码和名称都重复！");
                }
            }
        }
        return dept;
    }

    /**
     * 科室的删除
     *
     * @param param
     * @param user
     */
    public void deleteDept(String param, IUser user) {

        BdOuDept dept = JsonUtil.readValue(param, BdOuDept.class);
        BdOuDept deptVo = DataBaseHelper.queryForBean("select * from bd_ou_dept where pk_dept=?", BdOuDept.class, dept.getPkDept());
        if ("0".equals(deptVo.delFlag)) {
            int count1 = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
                    + "where pk_father=? and DEL_FLAG = '0'", Integer.class, dept.getPkDept());

            int count2 = DataBaseHelper.queryForScalar("select count(1) from bd_ou_empjob "
                    + "where pk_dept=? and DEL_FLAG = '0'", Integer.class, dept.getPkDept());

            int count3 = DataBaseHelper.queryForScalar("select count(1) from bd_ou_usrgrp_dept "
                    + " where pk_dept=? ", Integer.class, dept.getPkDept());

            int count_dept = count1 + count2 + count3;

            if (count_dept == 0) {
                DataBaseHelper.update("update bd_ou_dept set del_flag = '1' , MODIFIER=? ,MODITY_TIME=? where pk_dept = ?", new Object[]{UserContext.getUser().getPkEmp(), new Date(), dept.getPkDept()});
                rleCode = this.DelState;
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("dept", dept);
                paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
                paramMap.put("STATUS", rleCode);//状态
                PlatFormSendUtils.sendBdOuDeptMsg(paramMap);
                paramMap = null;
            } else {
                throw new BusException("科室存在被引用！");
            }
        } else {
            DataBaseHelper.update("update bd_ou_dept set del_flag = '0', MODIFIER=? ,MODITY_TIME=? where pk_dept = ?", new Object[]{UserContext.getUser().getPkEmp(), new Date(), dept.getPkDept()});
            rleCode = this.DelState;
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("dept", dept);
            paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
            paramMap.put("STATUS", rleCode);//状态
            PlatFormSendUtils.sendBdOuDeptMsg(paramMap);
            paramMap = null;
        }
    }

    /**
     * 根据医疗类型获取科室
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdOuDept> searchDeptlist(String param, IUser user) {
        String euSchclass = JsonUtil.getFieldValue(param, "euSchclass");
        String jg = ((User) user).getPkOrg();
        String sql = "select dept.pk_dept,dept.code_dept,dept.name_dept from bd_ou_dept dept " +
                "where dept.pk_org = ? "
                + "and dept.del_flag = '0' "
                + "and exists (select * from sch_resource res "
                + "where dept.pk_dept = res.pk_dept_belong "
                + "and res.eu_schclass = ? "
                + "and res.pk_org = ? "
                + "and res.del_flag='0')";
        List<BdOuDept> deptlist = DataBaseHelper.queryForList(sql, BdOuDept.class, jg, euSchclass, jg);
        return deptlist;
    }

    /**
     * 查询科室信息
     *
     * @param param{pkOrg,pkDept,dtDepttype}
     * @param user
     * @return
     */
    public List<BdOuDept> queryDeptList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || CommonUtils.isNull(paramMap.get("pkOrg"))) {
            paramMap = new HashMap<String, Object>();
            paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
            //throw new BusException("未获取到当前机构主键！");
        }
        String deptParam = ApplicationUtils.getSysparam("BD0003", false);
        if ("1".equals(deptParam))
            return deptMapper.getDeptAndTypeInfos(paramMap);
        else
            return deptMapper.getDeptInfos(paramMap);
    }

    public List<BdOuDeptType> qryDeptType(String param, IUser user) {
        BdOuDept bod = JsonUtil.readValue(param, BdOuDept.class);
        String pkDept = bod.getPkDept();
        List<BdOuDeptType> bodt = DataBaseHelper.queryForList("SELECT dt.pk_depttype,dt.dt_depttype,dt.note ,bd.name FROM bd_ou_dept_type dt "
                + " inner join bd_defdoc bd on bd.code = dt.dt_depttype WHERE dt.pk_dept =?  AND dt.del_flag = '0' and bd.code_defdoclist = '010200'", BdOuDeptType.class, new Object[]{pkDept});
        return bodt;
    }

    public void delByPkDeptType(String param, IUser user) {
        List<BdOuDeptType> lbodt = JsonUtil.readValue(param, new TypeReference<List<BdOuDeptType>>() {
        });
        for (BdOuDeptType bdOuDeptType : lbodt) {
            String pkDepttype = bdOuDeptType.getPkDepttype();
            DataBaseHelper.execute("delete from bd_ou_dept_type where pk_depttype=?", new Object[]{pkDepttype});
        }


    }


    public static String codeDeptHandle(String codeDept) {
        String res = "";
        if (CommonUtils.isEmptyString(codeDept)) {
            res = codeDept;
        } else {
            int numCount = Integer.parseInt(ApplicationUtils.getSysparam("BD0009", false, "系统参数BD0009设置错误！"));
            if (codeDept.length() < numCount) {
                res = "00000000000000000000" + codeDept;
                res = res.substring(res.length() - numCount, res.length());
            } else {
                res = codeDept;
            }
        }
        return res;
    }

    public int getCountDept(String param, IUser user) {
        BdOuDept dept = JsonUtil.readValue(param, BdOuDept.class);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkDept", dept.getPkDept());
        int count = deptMapper.getCountDept(paramMap);
        return count;
    }

    /**
     * 查询本机构下护理单元
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BdOuDept> queryDeptNsList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || CommonUtils.isNull(paramMap.get("pkOrg"))) {
            paramMap = new HashMap<String, Object>();
            paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
        }
        return deptMapper.queryDeptNsList(paramMap);
    }

    /**
     * 根据主键查询科室，交易号：001001004020
     *
     * @param param 主键
     * @return com.zebone.nhis.common.module.base.ou.BdOuDept
     * @author jesse
     */
    public BdOuDept qryByPK(String param,IUser user) {
        String pkDept = JsonUtil.readValue(param, String.class);
        return deptMapper.qryByPK(pkDept);
    }
}
