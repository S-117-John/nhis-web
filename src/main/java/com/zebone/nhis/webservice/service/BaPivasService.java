package com.zebone.nhis.webservice.service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.dao.BaPivasDrugMapper;
import com.zebone.nhis.webservice.vo.bapivas.*;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BaPivasService {


    @Resource
    private BaPivasDrugMapper pivasDrugMapper;
    /**
     * 灵璧静配发药
     */
    public String saveIpPivasDrug(Map<String,Object> paramMap, String codeEmp){
        for (String code:paramMap.keySet()) {
            String sql="select dept.pk_dept,dept.pk_org,sto.pk_store from bd_store sto inner join bd_ou_dept dept on dept.pk_dept=sto.pk_dept where dept.code_dept=?";
            Map<String,Object> deptMap= DataBaseHelper.queryForMap(sql,new Object[]{code});

            String pkDept= MapUtils.getString(deptMap,"pkDept");
            if(CommonUtils.isNull(pkDept)){
                throw new BusException("执行科室[code_dept]数据传入有误，请核对！");
            }

            // 是否二次重复发药
            StringBuilder second = new StringBuilder();
            second.append("select ap.PK_PDAP from ex_pd_apply ap where ap.pk_pdap=? and ap.flag_finish = '1' and ap.eu_direct='1' and ap.flag_cancel='0'");
            second.append("and ap.del_flag='0' and ap.eu_status='1'");
            List<String> pkPdapList = (List)paramMap.get(code);
            for(String p : pkPdapList){
                List secondList = DataBaseHelper.queryForList(second.toString(),new Object[]{p});
                if(secondList != null && secondList.size() > 0){
                    return "2";
                }
            }
            Map<String,Object> qryMap=new HashMap<>();
            qryMap.put("pkDept",pkDept);
            qryMap.put("pkPdapdts", paramMap.get(code));

            List<Map<String,Object>> pivasDrugList=pivasDrugMapper.qryPivaDrugDtInfo(qryMap);
            if(pivasDrugList==null || pivasDrugList.size()==0){
                throw new BusException("未查询到发药明细，待审核！");
            }

            //TODO 用户信息构建
            String empSql="select PK_EMP,NAME_EMP from BD_OU_EMPLOYEE where CODE_EMP=?";
            Map<String,Object> empMap=DataBaseHelper.queryForMap(empSql,new Object[]{codeEmp});
            User user=new User();
            user.setPkEmp(MapUtils.getString(empMap,"pkEmp"));
            user.setPkDept(pkDept);
            user.setPkOrg(MapUtils.getString(deptMap,"pkOrg"));
            user.setNameEmp(MapUtils.getString(empMap,"nameEmp"));
            user.setPkStore(MapUtils.getString(deptMap,"pkStore"));
            UserContext.setUser(user);

            List<Map<String,Object>> formatList = new ArrayList<>();
            pivasDrugList.forEach(l -> {
                Map<String, Object> formatMap = new HashMap<>();
                l.forEach((k, v) -> {
                    if(k.equals("dateOcc") && l.get("dateOcc").toString().length() == 10){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        ParsePosition pos = new ParsePosition(8);
                        try {
                            formatMap.put(k, format.parse(v.toString(),pos));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        formatMap.put(k,v);
                    }
                });
                formatList.add(formatMap);
            });

            ApplicationUtils apputil = new ApplicationUtils();
            ResponseJson response = null;
            try{
                response =apputil.execService("SCM", "IpPdDeDrugService", "mergeIpDeDrug", formatList,user);
            }catch (Exception e){
                e.getMessage();
            }
            if (response.getStatus() != 0) {
                //发药接口调用失败，返回失败信息
                throw new BusException("调用发药接口服务失败，失败原因"+response.getErrorMessage());
            }

        }
        return "1";
    }

    /**
     * 灵璧静配退药
     */
    public String saveIpRetPivasDrug(Map<String,Object> paramMap,String codeEmp){
        for (String code:paramMap.keySet()) {
            String sql="select dept.pk_dept,dept.pk_org,sto.pk_store from bd_store sto inner join bd_ou_dept dept on dept.pk_dept=sto.pk_dept where dept.code_dept=?";
            Map<String,Object> deptMap= DataBaseHelper.queryForMap(sql,new Object[]{code});

            String pkDept=MapUtils.getString(deptMap,"pkDept");
            if(CommonUtils.isNull(pkDept)){
                throw new BusException("执行科室[code_dept]数据传入有误，请核对！");
            }

            // 是否二次重复退药
            String second = "select pk_pdap from ex_pd_apply where pk_pdap=? and eu_direct='-1' and flag_cancel='0' and flag_finish='1' and del_flag='0'";
            Set<String> pkPdapList = (Set)paramMap.get(code);
            for(String p : pkPdapList){
                List secondList = DataBaseHelper.queryForList(second,new Object[]{p});
                if(secondList != null && secondList.size() > 0){
                    return "2";
                }
                // 已配置不能退药
                StringBuilder packed = new StringBuilder();
                packed.append("select 1 from ex_order_occ occ");
                packed.append("where EXISTS(select 1 from EX_PD_APPLY_DETAIL where PK_PDAP=?");
                packed.append("and occ.eu_pivas_type='1'");
                List packedList = DataBaseHelper.queryForList(packed.toString(), new Object[]{p});
                if(packedList != null && packedList.size() > 0){
                    return "0";
                }
            }

            Map<String,Object> qryMap=new HashMap<>();
            qryMap.put("pkDept",pkDept);
            qryMap.put("pkPdapdts", Arrays.asList(paramMap.get(code)));

            List<Map<String,Object>> pivasDrugList=pivasDrugMapper.qryPivaRetDrugDtInfo(qryMap);
            if(pivasDrugList==null || pivasDrugList.size()==0){
                throw new BusException("未查询到退药明细，待审核！");
            }

            //TODO 用户信息构建
            String empSql="select PK_EMP,NAME_EMP from BD_OU_EMPLOYEE where CODE_EMP=?";
            Map<String,Object> empMap=DataBaseHelper.queryForMap(empSql,new Object[]{codeEmp});
            User user=new User();
            user.setPkEmp(MapUtils.getString(empMap,"pkEmp"));
            user.setPkDept(pkDept);
            user.setPkOrg(MapUtils.getString(deptMap,"pkOrg"));
            user.setNameEmp(MapUtils.getString(empMap,"nameEmp"));
            user.setPkStore(MapUtils.getString(deptMap,"pkStore"));
            UserContext.setUser(user);

            ApplicationUtils apputil = new ApplicationUtils();
            ResponseJson response =apputil.execService("SCM", "IpPdReBackDrugService", "mergeIpReBackDrug", pivasDrugList,user);
            if (response.getStatus() != 0) {
                //发药接口调用失败，返回失败信息
                throw new BusException("调用退药接口服务失败，失败原因"+response.getErrorMessage());
            }

        }
        return "1";
    }
    /**
     * 静配附加费用记费接口
     * @param cgList
     * @return
     */
    public void chargeIpBatch(List<BlCgVo> cgList) {

        if(cgList==null || cgList.size()<=0)
            throw new BusException("未传入记费数据，请检查！");

        Set<String> codeIpList = new HashSet<String>();//患者id集合
        Set<String> codePvList = new HashSet<String>();//就诊id集合
        Set<String> codeItemList = new HashSet<String>();//项目编码集合
        Set<String> codeDeptList = new HashSet<String>();//科室集合
        Set<String> codeEmpList = new HashSet<String>();//人员集合

        for(BlCgVo cgVo : cgList){
            codeIpList.add(cgVo.getCode_ip());
            codePvList.add(cgVo.getCode_pv());
            codeItemList.add(cgVo.getCode_item());
            codeDeptList.add(cgVo.getCode_dept());
            codeDeptList.add(cgVo.getCode_dept_ns());
            codeDeptList.add(cgVo.getCode_dept_ex());
            codeEmpList.add(cgVo.getCode_emp_cg());
            codeEmpList.add(cgVo.getCode_emp_phy());
        }

        //患者信息集合
        List<Map<String,Object>> piList = new ArrayList<>();
        //就诊信息集合
        List<Map<String,Object>> pvList = new ArrayList<>();
        //收费项目集合
        List<Map<String,Object>> itemList=  new ArrayList<>();
        //执行科室、住院科室集合
        List<Map<String,Object>> deptList = new ArrayList<>();
        //人员集合
        List<Map<String,Object>> empList=  new ArrayList<>();

        //根据患者codePi获取患者信息
        String piListSql = "select pi.PK_PI,pi.CODE_IP,pi.CODE_PI from PI_MASTER pi where pi.code_pi in ("
                + CommonUtils.convertSetToSqlInPart(codeIpList, "pi.code_ip")+")";
        piList = DataBaseHelper.queryForList(piListSql, new Object[]{});
        //根据患者code_pv获取患者就诊信息
        String pvListSql = "select pv.pk_org,pv.PK_PV,pv.PK_PI,pv.EU_STATUS,pv.CODE_PV,pv.eu_pvtype,pv.pk_dept,pv.pk_dept_ns from PV_ENCOUNTER pv where pv.code_pv in ("
                + CommonUtils.convertSetToSqlInPart(codePvList, "pv.code_pv")+")";
        pvList = DataBaseHelper.queryForList(pvListSql, new Object[]{});
        //根据code_item获取收费项目信息
        String itemListSql = "select code as code_item,pk_item,pk_itemcate from bd_item where code in ("+CommonUtils.convertSetToSqlInPart(codeItemList, "code")+")";
        itemList = DataBaseHelper.queryForList(itemListSql, new Object[]{});
        //根据执行科室id，住院科室ID获取科室信息
        String deptListSql = "select pk_dept,pk_org,code_dept from bd_ou_dept where code_dept in ("
                + CommonUtils.convertSetToSqlInPart(codeDeptList, "code_dept")+")";
        deptList = DataBaseHelper.queryForList(deptListSql, new Object[]{});
        //根据人员编码获取人员信息集合
        String empListSql = "select emp.pk_org,emp.code_emp,emp.pk_emp,emp.name_emp,empjob.pk_dept from bd_ou_employee emp "+
                " inner join bd_ou_empjob empjob on emp.pk_emp = empjob.pk_emp where emp.code_emp in ("+CommonUtils.convertSetToSqlInPart(codeEmpList, "emp.code_emp")+")";
        empList = DataBaseHelper.queryForList(empListSql, new Object[]{});

        //组装记费数据
        List<BlPubParamVo> blCgVos = new ArrayList<>();
        for(BlCgVo cgVo : cgList){
            Map<String,Object> piMap = getMap(piList,"codeIp",cgVo.getCode_ip());
            Map<String,Object> pvMap = getMap(pvList,"codePv",cgVo.getCode_pv());
            Map<String,Object> itemMap = getMap(itemList,"codeItem",cgVo.getCode_item());
            Map<String,Object> deptMap = getMap(deptList,"codeDept",cgVo.getCode_dept());
            Map<String,Object> deptNsMap = getMap(deptList,"codeDept",cgVo.getCode_dept_ns());
            Map<String,Object> deptExMap = getMap(deptList,"codeDept",cgVo.getCode_dept_ex());
            Map<String,Object> empPhyMap = getMap(empList,"codeEmp",cgVo.getCode_emp_phy());
            Map<String,Object> empCgMap = getMap(empList,"codeEmp",cgVo.getCode_emp_cg());

            BlPubParamVo vo = new BlPubParamVo();
            vo.setPkOrg(CommonUtils.getString(pvMap.get("pkOrg")));
            vo.setPkPi(CommonUtils.getString(piMap.get("pkPi")));
            vo.setPkPv(CommonUtils.getString(pvMap.get("pkPv")));
            vo.setEuPvType(CommonUtils.getString(pvMap.get("euPvtype")));
            vo.setFlagPd("0");
            vo.setPkItem(CommonUtils.getString(itemMap.get("pkItem")));
            vo.setQuanCg(cgVo.getQuan());
            vo.setPkOrgApp(CommonUtils.getString(deptMap.get("pkOrg")));
            vo.setPkDeptApp(CommonUtils.getString(deptMap.get("pkDept")));
            vo.setPkDeptNsApp(CommonUtils.getString(deptNsMap.get("pkDept")));
            vo.setPkEmpApp(CommonUtils.getString(empPhyMap.get("pkEmp")));
            vo.setNameEmpApp(CommonUtils.getString(empPhyMap.get("nameEmp")));
            vo.setPkOrgEx(CommonUtils.getString(deptExMap.get("pkOrg")));
            vo.setPkDeptEx(CommonUtils.getString(deptExMap.get("pkDept")));
            vo.setDateHap(DateUtils.strToDate(cgVo.getDate_hap(), "yyyy-MM-dd"));
            vo.setPkDeptCg(CommonUtils.getString(empCgMap.get("pkOrg")));
            vo.setPkEmpCg(CommonUtils.getString(empCgMap.get("pkEmp")));
            vo.setNameEmpCg(CommonUtils.getString(empCgMap.get("nameEmp")));
            // 科室查询报空值异常，新加以下两行代码
            vo.setPkEmpEx(CommonUtils.getString(empCgMap.get("pkEmp")));
            vo.setNameEmpEx(CommonUtils.getString(empCgMap.get("nameEmp")));
            blCgVos.add(vo);
        }

        if(blCgVos!=null && blCgVos.size()>0){
            ApplicationUtils apputil = new ApplicationUtils();
            //组装用户信息
            User u=new User();
            u.setPkOrg(blCgVos.get(0).getPkOrg());
            u.setPkEmp(blCgVos.get(0).getPkEmpEx());
            u.setNameEmp(blCgVos.get(0).getNameEmpEx());
            u.setPkDept(blCgVos.get(0).getPkDeptEx());

            UserContext.setUser(u);

            //组装记费参数
            BlIpCgVo cgVo = new BlIpCgVo();
            cgVo.setAllowQF(true);
            cgVo.setBlCgPubParamVos(blCgVos);

            ResponseJson  rs =  apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,u);
            if(!CommonUtils.isEmptyString(rs.getErrorMessage()))
                throw new BusException("记费失败，失败原因"+rs.getErrorMessage());

        }
        else
            throw new BusException("记费失败，失败原因:"+"组装记费数据有问题，请联系管理员！");


    }


    /**
     * 更新配置状态
     * @param updateVos
     */
    public void updatePivasStatus(List<BaPivasUpdateVo> updateVos){
        if(updateVos==null ||updateVos.size()==0){
            throw new BusException("未传入数据请核实！");
        }

        List<String> upSqlList=new ArrayList<>();
        updateVos.forEach(m->{
            String datePlan=m.getAsUseDate()+" "+m.getAsFreqCounterSub();
            StringBuffer sql=new StringBuffer();
            sql.append("update EX_ORDER_OCC set eu_pivas_type='");
            sql.append(m.getAsDrugPzfs());
            sql.append("' where exists(select 1 from CN_ORDER where CN_ORDER.PK_CNORD=EX_ORDER_OCC.PK_CNORD and CN_ORDER.ORDSN_PARENT='");
            sql.append(m.getAsOrderNo());
            sql.append("') and DATE_PLAN=to_date('");
            sql.append(datePlan);
            sql.append("','yyyy-MM-dd hh24:mi:ss')");
            upSqlList.add(sql.toString());
        });

        DataBaseHelper.batchUpdate(upSqlList.toArray(new String[]{}));

    }

    /**
     * 根据传入的key,value对比ListMap里是否有相同的value，如果有返回此map
     * @param listMap
     * @param key
     * @return
     */
    private Map<String,Object> getMap(List<Map<String,Object>> listMap,String key,String value){
        Map<String,Object> rtnMap = new HashMap<String, Object>();
        if(listMap!=null && listMap.size()>0){
            for(Map<String,Object> map : listMap){
                if(CommonUtils.getString(map.get(key)).equals(value)){
                    rtnMap = map;
                    break;
                }
            }
        }
        return rtnMap;
    }
}
