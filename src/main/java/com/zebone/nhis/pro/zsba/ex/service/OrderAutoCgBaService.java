package com.zebone.nhis.pro.zsba.ex.service;

import com.zebone.nhis.common.module.base.bd.wf.BdOrdAutoexec;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.zsba.ex.dao.OrderAutoCgBaMapper;
import com.zebone.nhis.pro.zsba.ex.vo.AutoExOrdItemBaVo;
import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;
import com.zebone.platform.common.support.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Classname OrderAutoCgBaService
 * @Description 医嘱自动执行的记费项目服务 博爱版本
 * @Date 2020-10-14 16:25
 * @Created by wuqiang
 */
@Service
public class OrderAutoCgBaService {

    static final String EuType_ALL = "0";//全部医嘱
    static final String EuType_OrdType = "1";//按类型
    static final String EuType_Ord = "2";//按医嘱项目
    @Resource
    private OrderAutoCgBaMapper orderAutoCgBaMapper;
    @Resource
    private BlCgExPubService blCgExPubService;

    /**
     * 医嘱核对时自动记费,仅限临时医嘱自动记费
     *
     * @param checkOrdList
     */
    public void autoCgOrder(List<OrderCheckVO> checkOrdList, User user) {
        if (checkOrdList == null || checkOrdList.size() <= 0)
            return;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkOrg", user.getPkOrg());
        //1.查询医嘱自动记费配置
        List<BdOrdAutoexec> autoList = queryAutoOrdCg(paramMap, user);
        //2.根据记费配置内容选取需要记费医嘱(执行科室，医嘱类型，收费项目)
        if (autoList == null || autoList.size() <= 0)
            return;
        //List<OrderCheckVo> cgOrdList = new ArrayList<OrderCheckVo>();
        Set<String> ordSet = new HashSet<String>();
        List<String> pkCnords = new ArrayList<String>();
        for (OrderCheckVO ord : checkOrdList) {
            for (BdOrdAutoexec atex : autoList) {
                if (ord.getEuAlways().equals("1") && atex.getPkDept().equals(ord.getPkDeptExec())) {
                    boolean addFlag = false;
                    if (atex.getEuType().equals(EuType_ALL)) {
                        addFlag = true;
                    } else if (atex.getEuType().equals(EuType_OrdType) && atex.getCodeOrdtype().equals(ord.getCodeOrdtype())) {
                        addFlag = true;
                    } else if (atex.getEuType().equals(EuType_Ord) && atex.getPkOrd().equals(ord.getPkOrd())) {
                        addFlag = true;
                    }
                    if (addFlag) {
                        //cgOrdList.add(ord);
                        ordSet.add(ord.getPkCnord());
                    }
                }
            }
            pkCnords.add(ord.getPkCnord());
        }
        if (ordSet == null || ordSet.size() <= 0)
            return;
        paramMap.put("pkOrds", new ArrayList<String>(ordSet));
        //3.获取对应的收费项目
        List<AutoExOrdItemBaVo> orditems = orderAutoCgBaMapper.queryOrdItem(paramMap);
        if (orditems == null || orditems.size() <= 0)
            return;
        ordSet.clear();//清空原始医嘱主键集合
        for (AutoExOrdItemBaVo orditem : orditems) {
            ordSet.add(orditem.getPkCnord());
        }
        paramMap.put("pkOrds", new ArrayList<String>(ordSet));
        //4.获取执行单
        List<ExlistPubVo> exlist = orderAutoCgBaMapper.queryExecListByOrd(paramMap);
        //5.根据执行单记费
        if (exlist == null || exlist.size() <= 0)
            return;
        blCgExPubService.execAndCg(exlist, user);
        ExtSystemProcessUtils.processExtMethod("builtAssistOcc", "builtAssistOcc", pkCnords);
    }

    /**
     * 查询自动记费配置内容
     *
     * @param paramMap
     * @return
     */
    public List<BdOrdAutoexec> queryAutoOrdCg(Map<String, Object> paramMap, User user) {
        if (paramMap == null || paramMap.get("pkOrg") == null)
            paramMap.put("pkOrg", user.getPkOrg());
        return orderAutoCgBaMapper.queryAutoExec(paramMap);
    }
}
