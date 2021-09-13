package com.zebone.nhis.ma.pub.zsba.service;


import com.zebone.nhis.cn.ipdw.vo.BdOrdSetDtVO;
import com.zebone.nhis.ma.pub.zsba.vo.BdOrdSetVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class BaBdOrdSetAndDtService {


    /**
     * 根据查询信息获取医嘱模板
     * @param param{pkDept,pkEmp,codeOrName,euRight}
     * @param user
     * @return
     */
    public List<BdOrdSetVo> queryOrdSet(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        String euRight = paramMap.get("euRight").toString();
        String name = paramMap.containsKey("name") ? paramMap.get("name") != null ? paramMap.get("name").toString() :"" : "";
        String pkEmp = paramMap.containsKey("pkEmp") ? paramMap.get("pkEmp") != null ? paramMap.get("pkEmp").toString() :"" : "";
        String pkDept = paramMap.containsKey("pkDept") ? paramMap.get("pkDept") != null ? paramMap.get("pkDept").toString() :"" : "";
        String queSql = " select case when ord.eu_right = '1' then dept.name_dept else emp.name_emp end belong_to,ord.* " +
                        " from bd_ord_set ord" +
                        " left join bd_ou_dept dept on dept.pk_dept = ord.pk_dept" +
                        " left join bd_ou_employee emp on emp.pk_emp = ord.pk_emp" +
                        " where ord.eu_right = '"+euRight+"' and ord.del_flag = '0'" +
                        " and ord.code is not null and ord.flag_ip= '1'";
        if(!"".equals(pkDept)){
            queSql += " and ord.pk_dept = '"+pkDept+"'";
        }
        if(!"".equals(pkEmp)){
            queSql += " and ord.pk_emp = '"+pkEmp+"'";
        }
        if(!"".equals(name)){
            queSql += " and (ord.name like '%"+name+"%' or ord.spcode like '%"+name+"%') ";
        }
        List<BdOrdSetVo> bdOrdSetVos = DataBaseHelper.queryForList(queSql,BdOrdSetVo.class);
        return bdOrdSetVos;
    }
}
