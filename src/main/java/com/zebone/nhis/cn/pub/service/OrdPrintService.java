package com.zebone.nhis.cn.pub.service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.zebone.nhis.cn.pub.dao.OrdPrintMapper;
import com.zebone.nhis.cn.pub.vo.CnOrderPrintVo;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tjq
 */
@Service
public class OrdPrintService {
    @Autowired
    private OrdPrintMapper printDao;

    /**
     * 医嘱打印查询
     * @param param
     * @param user
     * @return
     */
    public List<CnOrderPrintVo> qryPrint(String param, IUser user){
        Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
        if(mapParam==null) {
            throw new BusException("未传入有效参数");
        }
        if(mapParam.get("pkPv")==null) {
            throw new BusException("未传入患者主键");
        }
        if(mapParam.get("euAlways")==null) {
            throw new BusException("未传入医嘱类型");
        }
        List<CnOrderPrintVo> printList = printDao.qryPrint(mapParam);
        return printList;
    }

    public List<Map<String,Object>> qryOrd(String param, IUser user){
        Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
        if(mapParam.get("pkPv")==null) {
            throw new BusException("未传入患者主键");
        }
        List<Map<String,Object>> orderList = printDao.qryOrd(mapParam);
        setImgSign(orderList);
        return orderList;
    }

    public void setImgSign(List<Map<String,Object>> orderList){
        //组装人员pk
        if(orderList!=null && orderList.size()>0){
            List<String> empList = new ArrayList<String>();
            for (Map<String,Object> ord : orderList) {
                //判断PK_EMP_ORD  签署医生
                if(ord.get("pkEmpOrd")!=null && !empList.contains(ord.get("pkEmpOrd").toString())){
                    empList.add(ord.get("pkEmpOrd").toString());
                }
                //判断PK_EMP_CHK  核对护士
                if(ord.get("pkEmpChk")!=null && !empList.contains(ord.get("pkEmpChk").toString())){
                    empList.add(ord.get("pkEmpChk").toString());
                }
                //判断PK_EMP_EX  执行人
                if(ord.get("pkEmpEx")!=null && !empList.contains(ord.get("pkEmpEx").toString())){
                    empList.add(ord.get("pkEmpEx").toString());
                }
                //判断PK_EMP_STOP  停止人
                if(ord.get("pkEmpStop")!=null && !empList.contains(ord.get("pkEmpStop").toString())){
                    empList.add(ord.get("pkEmpStop").toString());
                }
                //判断PK_EMP_STOP_CHK  停止核对人
                if(ord.get("pkEmpStopChk")!=null && !empList.contains(ord.get("pkEmpStopChk").toString())){
                    empList.add(ord.get("pkEmpStopChk").toString());
                }
            }
            //查询所有人员的签名
            if(empList.size()>0){
                List<BdOuEmployee> empListPic=qryEmpPic(empList);
                ImmutableMap<String, BdOuEmployee> empMapPic = Maps.uniqueIndex(empListPic, new Function<BdOuEmployee, String>() {
                    @Override
                    public String apply(BdOuEmployee bdOuEmployee) {
                        return bdOuEmployee!=null?bdOuEmployee.getPkEmp():null;
                    }
                });
                for (Map<String,Object> ord : orderList) {
                    putImgSign("pkEmpOrd","pkEmpOrdPic",ord,empMapPic);
                    putImgSign("pkEmpChk","pkEmpChkPic",ord,empMapPic);
                    putImgSign("pkEmpEx","pkEmpExPic",ord,empMapPic);
                    putImgSign("pkEmpStop","pkEmpStopPic",ord,empMapPic);
                    putImgSign("pkEmpStopChk","pkEmpStopChkPic",ord,empMapPic);
                }
            }

        }
    }
    private void putImgSign(String gKey,String pKey,Map<String,Object> ord,ImmutableMap<String, BdOuEmployee> empMapPic){
        BdOuEmployee emp = empMapPic.get(MapUtils.getString(ord,gKey));
        if(emp!=null && emp.getImgSign()!=null){
            ord.put(pKey,emp.getImgSign());
        }
    }

    /**
     * 查询人员信息《只返回了签名、pk两个字段》
     * @param empList
     * @return
     */
    public List<BdOuEmployee> qryEmpPic(List<String> empList){
        Map<String, Object> param = new HashMap<String,Object>();
        if(empList==null || empList.size()==0) {
            return null;
        }
        param.put("pkEmp",empList);
        return printDao.qryEmpPic(param);
    }

}
