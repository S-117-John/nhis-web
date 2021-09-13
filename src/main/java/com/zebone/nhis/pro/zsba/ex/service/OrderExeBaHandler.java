package com.zebone.nhis.pro.zsba.ex.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Classname OrderExeBaHandler
 * @Description 医嘱相关操作
 * @Date 2020-09-18 16:48
 * @Created by wuqiang
 */
@Service
public class OrderExeBaHandler {

    @Resource
    private OrderExecListBaService orderExecListBaService;
    @Resource
    private OrderAutoCgBaService orderAutoCgBaService;//自动记费
    @Resource
    private OrderExeBaService orderExeBaService;

    public String orderCheckChargeExe(String param, IUser user) {
        //核对医嘱,是否自动生成执行单，是否自动计费
        List<String> stringList = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        List<OrderCheckVO> checkList = orderExeBaService.getOrder(stringList);
        if (checkList == null || checkList.size() <= 0) {
            throw new BusException("未获取到记账数据！");
        }
        List<OrderCheckVO> check_list = new ArrayList<OrderCheckVO>();
        List<String> check_pklist = new ArrayList<String>();
        List<String> stop_pklist = new ArrayList<String>();
        List<String> erase_pklist = new ArrayList<String>();
        List<OrderCheckVO> stop_list = new ArrayList<OrderCheckVO>();
        List<OrderCheckVO> erase_list = new ArrayList<OrderCheckVO>();
        Set<String> dietPkPvs = new HashSet<String>();
        List<OrderCheckVO> cheargeList = new ArrayList<OrderCheckVO>(16);
        List<OrderCheckVO> check_listEex = new ArrayList<OrderCheckVO>();
        List<String> check_pklistExe = new ArrayList<String>();
        String[] NotExe = new String[]{"04", "07", "08", "01"};
        Set<String> noExe = new HashSet<String>(Arrays.asList(NotExe));
        //根据医嘱状态，区分医嘱是待核对，还是停止核对
        for (OrderCheckVO vo : checkList) {
            String pk_cnord = vo.getPkCnord();
            //饮食医嘱取患者饮食医嘱等级写入到pv_ip.dt_dietary
            if ("13".equals(vo.getCodeOrdtype())) {
                dietPkPvs.add(vo.getPkPv());
            }
            if ("0".equals(vo.getEuStatusOrd())) {
                check_list.add(vo);
                check_pklist.add(pk_cnord);
                boolean isExe = "0".equals(vo.getFlagDurg()) && !noExe.contains(vo.getCodeOrdtype().substring(0, 2));
                if (isExe) {
                    //药品耗材，手术不生成执行单
                    check_listEex.add(vo);
                    check_pklistExe.add(pk_cnord);
                }
            } else {
                if ("1".equals(vo.getFlagStop()) && "0".equals(vo.getFlagStopChk())) {//停止待核对
                    //stop_list.add(pno);
                    stop_pklist.add(pk_cnord);
                    stop_list.add(vo);
                } else if ("1".equals(vo.getFlagErase()) && "0".equals(vo.getFlagEraseChk())) {
                    erase_list.add(vo);
                    erase_pklist.add(pk_cnord);
                }
            }
        }
        User u = (User) user;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkOrg", u.getPkOrg());
        paramMap.put("pkDept", u.getPkDept());
        paramMap.put("pkDeptNs", u.getPkDept());
        paramMap.put("pkEmp", u.getPkEmp());
        paramMap.put("nameEmp", u.getNameEmp());
        orderExeBaService.updateOrdStateByTs(check_list, check_pklist, stop_list, stop_pklist, erase_list, u, checkList.size(), dietPkPvs);
        String msg = "";
        try {
            String msg1 = "";
            msg = msg + orderExecListBaService.processExecList(check_listEex, check_pklistExe, stop_pklist, stop_list, erase_pklist, paramMap, true);
        } catch (BusException e) {
            msg = msg + e.getMessage();
            msg = msg + " 核对时生成执行单失败，请根据提示信息修正后到医嘱执行计划中重新生成！";
        } catch (Exception e) {
            //ParseException e
            msg = msg + e.getMessage();
            msg = msg + " 核对时追加首日执行单转换日期失败，请根据提示信息修正后到医嘱执行计划中重新生成！";
        }
        //获取自动记费数据以及直接记费数据
        check_list.clear();
        for (OrderCheckVO vo : checkList) {
            if (!"0".equals(vo.getEuStatusOrd())) {
                continue;
            }
            if (u.getPkDept().equals(vo.getPkDeptExec())) {
                cheargeList.add(vo);
                continue;
            }
            check_list.add(vo);
        }
        //执行自动记费
        Set<String> pkCnords = new HashSet<>(check_pklistExe);
        orderAutoCgBaService.autoCgOrder(check_list, (User) user);
        orderExeBaService.OrderCharge(cheargeList, pkCnords, u);
        String sql = " update  EX_ORDER_OCC set EU_STATUS='1' where PK_CNORD in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")";
        DataBaseHelper.execute(sql);
        return msg;
    }
}
