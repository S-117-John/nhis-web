package com.zebone.nhis.pro.zsba.cn.opdw.service;

import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.MchVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ZsbaOrdHandler {

    private Logger logger = LoggerFactory.getLogger("com.zebone");
    @Resource
    private CnOrderOpService cnOrderOpService;

    /**
     * 查询患者历史就诊信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> serschPv(String param, IUser user) {
        Map<String, Object> serParam = JsonUtil.readValue(param, Map.class);
        // 校验条件
        String codePi = MapUtils.getString(serParam, "codePi");
        if (StringUtils.isEmpty(codePi)) {
            throw new BusException("未获取到查患者ID！");
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Callable<List<Map<String, Object>>> task = new Callable<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> call() throws Exception {
                String sql = "select * from [192.168.0.9].his_cxdb_bayy.dbo.view_nhis_query_visit where p_id= ? ";
                List<Map<String, Object>> list = DataBaseHelper.queryForList(sql, new Object[]{codePi});
                return list;
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Future<List<Map<String, Object>>> future = executorService.submit(task);
            result = future.get(7, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("查询患者{}就诊历史信息出错/超时！！！", codePi);
        } finally {
            executorService.shutdownNow();
        }
        return result;
    }

    /**
     * 查询患者就诊医嘱信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> serschOrdByPv(String param, IUser user) {
        Map<String, Object> serParam = JsonUtil.readValue(param, Map.class);
        // 校验条件
        String pId = MapUtils.getString(serParam, "codePi");
        if (StringUtils.isEmpty(pId)) {
            throw new BusException("未获取到查患者ID！");
        }
        String times = MapUtils.getString(serParam, "times");
        if (StringUtils.isEmpty(times)) {
            throw new BusException("未获取到查患者就诊次数！");
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Callable<List<Map<String, Object>>> task = new Callable<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> call() throws Exception {
                List<Map<String, Object>> list = new ArrayList<>();
                list = DataBaseHelper.queryForList(
                        "select * from [192.168.0.9].his_cxdb_bayy.dbo.view_nhis_query_order where p_id= ? and times = ? ",
                        new Object[]{pId, times});
                return list;
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Future<List<Map<String, Object>>> future = executorService.submit(task);
            result = future.get(7, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("查询患者{}{}就诊医嘱信息出/超时！！！", pId, times);
        } finally {
            executorService.shutdownNow();
        }
        return result;
    }

    /*
     * @Description 从三方库中查询患者是否开立产前相关检查
     * 1、试图查不到记录  说明：不符合免费政策要求，请核查妇幼建档信息！
     * 2、试图中根据是否免费婚检标识 =1的：  开地贫电泳、地贫基因是给予提示： 已进行过免费婚孕检 不能再次享受免费地贫项目，请开自费项目！或核查妇幼建档信息！
     *3、试图中 预产年龄>=35岁的，开早、中期唐氏时 提示：预产年龄>=35岁，不符合免费唐筛条件！或核查妇幼建档信息！
     *4、唐氏中早期互斥的提示：已经做过免费早(中)期唐筛，请开自费项目！或核查妇幼信息！
     *5、剩下的就是既有的有没有做过的 之类的判断了
     *6、试图中 预产年龄>=35岁的，开NIPT补助时 提示：预产年龄>=35岁，不符合NIPT补助条件，请开自费项目！或核查妇幼建档信息！
     * @auther wuqiang
     * @Date 2021-05-24
     * @Param [param, user]
     * @return java.lang.Integer
     */
    public MchVo getMchResult(String param, IUser user) {
        MchVo mchVo = JsonUtil.readValue(param, MchVo.class);
        if (StringUtils.isBlank(mchVo.getMchCode())) {
            throw new BusException("温馨提示：未获取到妇幼保健项目编码,请联系管理员维护医嘱项目拓展属性BA0003！");
        }
        PiCard piCard = cnOrderOpService.getPicard(mchVo.getCodeOp());
        if (piCard != null) {
            mchVo.setHealthNo(piCard.getCardNo());
        }
        try {
            DataSourceRoute.putAppId("HIS_MCH");// 切换数据源
            String sql = "select  top 1   HealthNo, isnull(IsMarrycheck,'0') IsMarrycheck,isnull(DueAge,'0') DueAge  " +
                    "from view_Csqxbz With (NoLock) where  patientid=? or WidCard =? or MidCard=? or  HealthNo=?   ";
            MchVo mchVo1 = DataBaseHelper.queryForBean(sql, MchVo.class, new Object[]{mchVo.getCodeOp(), mchVo.getIdNo(), mchVo.getIdNo(), mchVo.getHealthNo()});
            if (mchVo1 == null) {
                mchVo.setState(-1);
                mchVo.setMessage("温馨提示：该患者不符合补助条件，不允许开立此医嘱,请 开立自费项目/登录妇幼系统核对该患者是否符合补助项目条件！");
                return mchVo;
            }
            mchVo.setHealthNo(mchVo1.getHealthNo());
            //保存妇幼保健号
            DataSourceRoute.putAppId("default");// 切换数据源
            cnOrderOpService.saveHealthNo(mchVo);
            DataSourceRoute.putAppId("HIS_MCH");// 切换数据源
            String mchCode = mchVo.getMchCode();
            String[] marryXm = new String[]{"A01", "A03", "A04", "A02", "A06", "A05"};
            Boolean isMarry = "1".equals(mchVo1.getIsMarrycheck()) && Arrays.stream(marryXm).anyMatch(m -> m.equals(mchVo.getMchCode()));
            if (isMarry) {
                mchVo.setState(-1);
                mchVo.setMessage(String.format("温馨提示：已进行过免费婚孕检 不能再次享受免费地贫项目[%s]，请开自费项目！或核查妇幼建档信息！", mchVo.getMchCode()));
                return mchVo;
            }
            if ("35".compareTo(mchVo1.getDueAge()) <= 0) {
                String[] age = new String[]{"B01", "B02"};
                if (Arrays.stream(age).anyMatch(m -> m.equals(mchCode))) {
                    mchVo.setState(-1);
                    mchVo.setMessage(String.format("温馨提示：预产年龄>=35岁，不符合免费唐筛 [%s] 条件！或核查妇幼建档信息！", mchVo.getMchCode()));
                    return mchVo;
                }
                if ("B05".equals(mchCode)) {
                    mchVo.setState(-1);
                    mchVo.setMessage(String.format("温馨提示：预产年龄>=35岁，不符合不符合NIPT [%s] 补助条件！或核查妇幼建档信息！", mchVo.getMchCode()));
                    return mchVo;
                }
            }
            if ("B01".equals(mchCode) || "B02".equals(mchCode)) {
                sql = "select top 1 IsCheck, ChkOrgan, ChkDate, SysId,ItemName ,ItemCode mchCode  " +
                        "from Woman_Subsidies With (NoLock) " +
                        "where HealthNo = ? " +
                        "  and ItemCode ='B01' ";
                MchVo mchVo2 = DataBaseHelper.queryForBean(sql, MchVo.class, new Object[]{mchVo1.getHealthNo()});
                sql = "select top 1 IsCheck, ChkOrgan, ChkDate, SysId ,ItemName ,ItemCode mchCode  " +
                        "from Woman_Subsidies  With (NoLock) " +
                        "where HealthNo = ? " +
                        "  and ItemCode ='B02' ";
                MchVo mchVo3 = DataBaseHelper.queryForBean(sql, MchVo.class, new Object[]{mchVo1.getHealthNo()});
                if (mchVo2 == null && mchVo3 == null) {
                    mchVo.setState(-1);
                    mchVo.setMessage("温馨提示：在妇幼保健系统未找到符合的条件的项目，无法开立,请开立自费项目/登录妇幼系统核查档案");
                    return mchVo;
                }
                boolean ismchB01 = mchVo2 != null && mchVo2.getIsCheck() == 1;
                boolean ismchB02 = mchVo3 != null && mchVo3.getIsCheck() == 1;
                if (ismchB01) {
                    mchVo.setState(-1);
                    mchVo.setMessage(String.format("温馨提示：该患者已于 [%s] 在 [%s] 已经做过该项目[%s][%s]的检查，请开立自费项目",
                            new Object[]{mchVo2.getChkDate().substring(0, 11), mchVo2.getChkOrgan(), mchVo2.getMchCode(), mchVo2.getItemName()}));
                    return mchVo;
                }
                if (ismchB02) {
                    mchVo.setState(-1);
                    mchVo.setMessage(String.format("温馨提示：该患者已于 [%s] 在 [%s] 已经做过[%s][%s]该项目的检查，请开立自费项目",
                            new Object[]{mchVo3.getChkDate().substring(0, 11), mchVo3.getChkOrgan(), mchVo3.getMchCode(), mchVo3.getItemName()}));
                    return mchVo;
                }
                mchVo1 = mchVo2;
            } else {
                sql = "select top 1 IsCheck, ChkOrgan, ChkDate, SysId ,ItemName ,ItemCode mchCode " +
                        "from Woman_Subsidies With (NoLock) " +
                        "where HealthNo = ? " +
                        "  and ItemCode  = ?";
                mchVo1 = DataBaseHelper.queryForBean(sql, MchVo.class, new Object[]{mchVo1.getHealthNo(), mchVo.getMchCode()});
            }
            if (mchVo1 == null) {
                mchVo.setState(-1);
                mchVo.setMessage("温馨提示：在妇幼保健系统未找到符合的条件的项目，无法开立,请开立自费项目/登录妇幼系统核查档案");
                return mchVo;
            }
            if (mchVo1.getIsCheck() == 1) {
                mchVo.setState(-1);
                mchVo.setMessage(String.format("温馨提示：该患者已于 [%s] 在 [%s] 已经做过该项目[%s][%s] 的检查，请开立自费项目",
                        new Object[]{mchVo1.getChkDate().substring(0, 11), mchVo1.getChkOrgan(), mchVo1.getMchCode(), mchVo1.getItemName()}));
                return mchVo;
            }
            mchVo.setSysId(mchVo1.getSysId());
            mchVo.setState(1);
            return mchVo;
        } catch (Exception e) {
            logger.error("调用妇幼异常：{}", e.getMessage());
        } finally {
            DataSourceRoute.putAppId("default");// 切换数据源
        }
        mchVo.setState(-1);
        mchVo.setMessage("温馨提示：不允许开立此医嘱,请开立自费项目/登录妇幼系统核查档案！");
        return mchVo;
    }

    /**
     * @return java.lang.Integer
     * @Description 更新三方系统
     * @auther wuqiang
     * @Date 2021-05-24
     * @Param [param, user]
     */
    public void updateMch(String param, IUser user) {
        List<MchVo> mchVo = JsonUtil.readValue(param, new TypeReference<List<MchVo>>() {
        });
        if (CollectionUtils.isEmpty(mchVo)) {
            return;
        }
        try {
            List<String> codeOp = mchVo.stream().filter(m -> StringUtils.isBlank(m.getHealthNo())).map(m -> m.getCodeOp()).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(codeOp)) {
                PiCard piCard = cnOrderOpService.getPicard(codeOp.get(0));
                if (piCard == null) {
                    logger.error("删除妇幼保健系统数据时未找到有效妇幼保健号{}！！！", codeOp.get(0));
                    throw new BusException("温馨提示：删除妇幼保健系统数据时未找到有效妇幼保健号，删除失败，请告知管理员");
                }
                mchVo.forEach(m -> {
                            if (StringUtils.isBlank(m.getHealthNo())) {
                                m.setHealthNo(piCard.getCardNo());
                            }
                        }
                );
            }
            DataSourceRoute.putAppId("HIS_MCH");// 切换数据源
            cnOrderOpService.updateMch(mchVo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataSourceRoute.putAppId("default");// 切换数据源
        }
    }
}
