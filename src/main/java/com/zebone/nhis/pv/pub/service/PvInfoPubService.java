package com.zebone.nhis.pv.pub.service;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.pv.pub.dao.PvInfoPubMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取就诊公共信息服务
 *
 * @author yangxue
 */
@Service
public class PvInfoPubService {
    @Resource
    private PvInfoPubMapper pvInfoPubMapper;

    /**
     * 根据就诊记录提供医保需要的出院相关信息
     * paramMap{dtDiagtype:诊断类型，pkPv:就诊主键}
     *
     * @return
     */
    public Map<String, Object> getPvOutInfoForIns(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || paramMap.get("pkPv") == null || paramMap.get("pkPv").equals(""))
            throw new BusException("未获取到患者就诊标识pkPv");
        List<Map<String, Object>> list = pvInfoPubMapper.getPvOutInfoForIns(paramMap);
        if (list != null && list.size() >= 1)
            return list.get(0);
        return null;
    }

    /**
     * 查询就诊患者指定医保对应的患者分类
     *
     * @param pkPv
     * @param pkInsu
     * @return
     */
    public String getPiCateCodeByPvAndCodeHp(String pkPv) {
        if (CommonUtils.isEmptyString(pkPv)) {
            throw new BusException("未获取患者就诊标识pkPv");
        }
        List<Map<String, Object>> list = pvInfoPubMapper.getPiCateCodeByPv(pkPv);
        if (list != null && list.size() >= 1) {
            return CommonUtils.getString(list.get(0).get("code"));
        }
        return null;
    }

    /**
     * 贫困患者是否控费
     *
     * @return
     */
    public boolean isLimiteFee(String pkPv) {

        //是否控费
        String nhhpcode = ApplicationUtils.getSysparam("PI0016", false);
        if (CommonUtils.isEmptyString(nhhpcode) || "1".equals(nhhpcode)) {
            return true;
        }
        //贫困患者编码
        String pkhzcode = ApplicationUtils.getSysparam("PI0015", true);
        if (CommonUtils.isEmptyString(pkhzcode)) {
            return true;
        }
         pkhzcode.trim();
        String [] poorCodeStr=pkhzcode.split(",");
        //查询患者对应的患者分类
        String codePicate = this.getPiCateCodeByPvAndCodeHp(pkPv);
        for (String poor:poorCodeStr){
            if ( poor.equals(codePicate)) {
                return false;
            } else {
                return true;
            }
        }
       return true;
    }

    /**
     * 更新患者锁定信息
     *
     * @param param
     * @param user
     */
    public void updatePvLockStatus(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap != null && CommonUtils.isNotNull(paramMap.get("pkPv"))) {
            DataBaseHelper.update(" update pv_encounter set eu_locked=:euLocked where pk_pv=:pkPv and eu_locked=:whereLocked ", paramMap);
        }
    }

    /***
     * @Description 根据就诊主键查询患者就诊信息
     * @auther wuqiang
     * @Date 2019-12-02
     * @Param [param, user]
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     */
    public List<Map<String, Object>> queryPvEnByPkPv(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        String sql = "select * from PV_ENCOUNTER where 1=0 or PK_PV=? ";
        return DataBaseHelper.queryForList(sql, pkPv);
    }

    public List<Map<String, Object>> queryPvEnByPkPv1(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkItem = (String) paramMap.get("pkPi");
        String sql = "SELECT date_op as modify_time,name_emp_op as oper_emp_name,custip as custom_ip,objname as obj_name,content,content_bf as content_new FROM SYS_APPLOG WHERE pk_obj = ? AND objname = 'pi_master' order by date_op desc";
        List<Map<String, Object>> mapItemList = DataBaseHelper.queryForList(sql, new Object[]{pkItem});
        return mapItemList;
    }

    /**
     * 新增患者就诊记录
     *
     * @param param
     * @param user
     */
    public Map<String, String> savePiMedicalRecord(String param, IUser user) {
//        生成就诊记录,写pv_encounter
        Map map = JsonUtil.readValue(param, Map.class);
        User u = (User) user;
        PvEncounter pvEncounter = null;
        try {
            pvEncounter = savePvEncounter(map, u);
        } catch (ParseException e) {
            throw new BusException("日期转化出错,"+e.getMessage());
        }
//        生成门诊就诊信息,写pv_op
        map.put("pkPv",pvEncounter.getPkPv());
        PvOp pvOp = savePvOp(map);
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("pkPv",pvEncounter.getPkPv());
        resultMap.put("pkSchsrv", pvOp.getPkSchsrv());
        return resultMap;
    }
    private PvEncounter savePvEncounter(Map<String,Object> map, User user) throws ParseException {
        PvEncounter pvEncounter = new PvEncounter();
        Date date = new Date(System.currentTimeMillis());
        pvEncounter.setPkPi(CommonUtils.getString(map.get("pkPi")));
        pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
        pvEncounter.setEuPvtype("1");
        String dateBegin = CommonUtils.getString(map.get("dateBegin"));
        if(StringUtils.isNotBlank(dateBegin)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date parse = simpleDateFormat.parse(dateBegin);
            pvEncounter.setDateBegin(parse);
        }else {
            pvEncounter.setDateBegin(date);
        }
        pvEncounter.setEuStatus("2");
        pvEncounter.setFlagIn("0");
        pvEncounter.setFlagSettle("0");
        pvEncounter.setNamePi(CommonUtils.getString(map.get("namePi")));
        pvEncounter.setDtSex(CommonUtils.getString(map.get("dtSex")));
        pvEncounter.setAgePv(map.get("age").toString());
        pvEncounter.setPkInsu(CommonUtils.getString(map.get("pkInsu")));
        pvEncounter.setPkPicate(CommonUtils.getString(map.get("pkPicate")));
        pvEncounter.setDtMarry(CommonUtils.getString(map.get("dtMarry")));
        pvEncounter.setAddress(CommonUtils.getString(map.get("address")));
        pvEncounter.setPkDept(CommonUtils.getString(map.get("pkDept")));
        pvEncounter.setDateClinic(date);
        pvEncounter.setPkEmpPhy(CommonUtils.getString(map.get("pkEmpPhy")));
        pvEncounter.setNameEmpPhy(CommonUtils.getString(map.get("nameEmpPhy")));
        pvEncounter.setEuPvmode("0");
        pvEncounter.setEuDisetype("0");
        pvEncounter.setPkEmpReg(user.getPkEmp());
        pvEncounter.setNameEmpReg(user.getNameEmp());
        pvEncounter.setDateReg(date);
        pvEncounter.setFlagCancel("0");
        pvEncounter.setEuStatusFp("0");
        pvEncounter.setDtIdtypeRel("01");
        pvEncounter.setFlagSpec("0");
        pvEncounter.setEuLocked("2");
        ApplicationUtils.setDefaultValue(pvEncounter,true);
        
        String pkDeptArea = getPkDeptArea(CommonUtils.getString(map.get("pkRes")));
   		pvEncounter.setPkDeptArea(pkDeptArea);
        DataBaseHelper.insertBean(pvEncounter);
        return pvEncounter;
    }
    private PvOp savePvOp(Map<String,Object> map){
        // 保存门诊属性
        PvOp pvOp = new PvOp();
        pvOp.setPkPv(CommonUtils.getString(map.get("pkPv")));
        pvOp.setOpTimes(1L);
        pvOp.setPkSchsrv(CommonUtils.getString(map.get("pkSchsrv")));
        pvOp.setPkRes(CommonUtils.getString(map.get("pkRes")));
        pvOp.setPkDeptPv(CommonUtils.getString(map.get("pkDept")));
        pvOp.setPkEmpPv(CommonUtils.getString(map.get("pkEmpPhy")));
        pvOp.setNameEmpPv(CommonUtils.getString(map.get("nameEmpPhy")));
        pvOp.setTicketno(0L);
        pvOp.setFlagFirst("1");
        pvOp.setCntPrint(0);
        pvOp.setEuRegtype("0");
        pvOp.setDtApptype("1");
        pvOp.setOrderidExt("0");
        Date dateBegin = new Date(System.currentTimeMillis());
        pvOp.setDateBegin(dateBegin);
        pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(dateBegin));
        pvOp.setFlagNorelease("0");
        ApplicationUtils.setDefaultValue(pvOp,true);
        DataBaseHelper.insertBean(pvOp);
        return pvOp;
    }
    
    /**  
     * <p>Desc: 增加诊区属性  </p>  
     * @author : wangpengyong  
     * @date   : 2021年7月15日  
     */  
     public void addPkDeptArea(String pkSchres, PvEncounter pvEncount) {
     	 if(StringUtils.isNotBlank(pkSchres)) {
     		 String pkDeptArea = getPkDeptArea(pkSchres);
     		 if(StringUtils.isNotBlank(pkDeptArea)) {
              	pvEncount.setPkDeptArea(pkDeptArea);//保存费用使用
              	//更新就诊记录表
                 DataBaseHelper.update(" UPDATE pv_encounter set pk_dept_area = ? where pk_pv = ? ",new Object[]{pkDeptArea,pvEncount.getPkPv()});
              }
     	 }
     }
     
     /**  
      * <p>Desc: 根据排班资源获取诊区 </p>  
      * @author : wangpengyong 
      * @param  : pkSchres 排班资源主键
      * @date   : 2021年7月15日  
      */  
      public String getPkDeptArea(String pkSchres) {
    	  String pkDeptArea = null;
      	  if(StringUtils.isNotBlank(pkSchres)) {
      		 pkDeptArea = MapUtils.getString(DataBaseHelper.queryForMap("SELECT pk_dept_area from SCH_RESOURCE where pk_schres = ? ", pkSchres ), "pkDeptArea");
      	  }
      	  return pkDeptArea;
      }
    
}
