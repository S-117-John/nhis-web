package com.zebone.nhis.task.compay.service;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsSignInQg;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全国医保定时任务类
 */
@Service
public class InsSdTaskService {

    private User u  = new User();

    /**
     *  启动自动签到签退
     * @param cfg
     */
    public void autoSignInOrBack(QrtzJobCfg cfg) {
        try {
            saveEmpSignInOrBack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动签到签退
     */
    private void saveEmpSignInOrBack(){
        //查询具有收费权限的人员信息
        List<BdOuEmployee> empList = DataBaseHelper.queryForList(
                "select * from BD_OU_EMPLOYEE where DT_EMPTYPE = '05' and FLAG_ACTIVE = '1' and del_flag = '0'"
        , BdOuEmployee.class,new Object[]{});

        if(empList!=null && empList.size()>0){
            ApplicationUtils apputil = new ApplicationUtils();

            //查询有效签到记录
            Set<String> codeList = empList.parallelStream().map(m -> m.getCodeEmp()).collect(Collectors.toSet());
            String sql = "select * from INS_SIGN_IN_QG where opter_no in ("+ CommonUtils.convertSetToSqlInPart(codeList, "opter_no")+") and status = '1' and del_flag = '0'";
            List<InsSignInQg> signList = DataBaseHelper.queryForList(
                    sql , InsSignInQg.class, new Object[]{});

            empList.stream()
                    .forEach(empVo->{
                        //组装用户信息
                        u.setPkOrg(empVo.getPkOrg());
                        u.setPkEmp(empVo.getPkEmp());
                        u.setNameEmp(empVo.getNameEmp());
                        u.setCodeEmp(empVo.getCodeEmp());

                        UserContext.setUser(u);

                        //循环调用签退、签到接口
                        List<InsSignInQg> list = signList.stream()
                                .filter(sign->sign.getOpterNo().equals(empVo.getCodeEmp()))
                                .collect(Collectors.toList());

                        Map<String,Object> signMap = new HashMap<>(16);

                        if(list!=null&& list.size()>0){
                            signMap.put("opterNo",empVo.getCodeEmp());
                            signMap.put("signNo",list.get(0).getSignNo());

                            //调用签退接口
                            try{
                                apputil.execService("COMPAY", "NationalInsuranceService", "signBackOper", signMap,u);
                            }catch(Exception ex){

                            }

                        }

                        //调用签到接口
                        signMap.clear();
                        signMap.put("opterNo",empVo.getCodeEmp());
                        signMap.put("mac", ApplicationUtils.getCurrHostName());
                        signMap.put("ip",ApplicationUtils.getCurrIp());

                        try{
                            apputil.execService("COMPAY", "NationalInsuranceService", "signOper", signMap,u);
                        }catch(Exception ex){

                        }
                    });
        }
    }

}
