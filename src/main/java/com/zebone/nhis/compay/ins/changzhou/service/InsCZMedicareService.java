package com.zebone.nhis.compay.ins.changzhou.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.changzhou.dao.InsCZMedicareMapper;
import com.zebone.nhis.compay.ins.changzhou.vo.*;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InsCZMedicareService {
    @Autowired
    InsCZMedicareMapper insCZMedicareMapper;

    /**
     * 分页查询所有的医保项目
     * 交易码：015001013001
     *
     * @return
     */
    public Page<Map<String, Object>> queryYBItem(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) {
            throw new BusException("交易码：015001013001，缺少参数信息！");
        }
        int pageIndex = CommonUtils.getInteger(paramMap.get("pageIndex"));
        int pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
        MyBatisPage.startPage(pageIndex, pageSize);
//        根据查询条件获取医保收费项目
        List<Map<String, Object>> mapList = insCZMedicareMapper.queryYBItem(paramMap);
        Page<Map<String, Object>> page = MyBatisPage.getPage();
        page.setRows(mapList);
        return page;
    }

    /**
     * 分页查询医保匹配信息
     * 交易码：015001013002
     *
     * @param param:Match:1 匹配，2 未匹配；  fylb；
     * @param user
     * @return
     */
    public Page<CompareSearchResult> queryYBCompareInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) {
            throw new BusException("交易码：015001013002，缺少参数信息！");
        }
        int pageIndex = CommonUtils.getInteger(paramMap.get("pageIndex"));
        int pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
        MyBatisPage.startPage(pageIndex, pageSize);
        String match = paramMap.get("match").toString();
        String fylb = paramMap.get("fylb") == null ? "" : paramMap.get("fylb").toString();
        String dl = paramMap.get("dl").toString();
        String yplb = "01,02,03";
        List<CompareSearchResult> mapList = new ArrayList<>();
        if ("2".equals(dl)) {
//            查询病种匹配目录
            mapList = insCZMedicareMapper.queryDiseaseList(paramMap);
            Page<CompareSearchResult> page = MyBatisPage.getPage();
            page.setRows(mapList);
            return page;
        }
        switch (match) {
            case "1":
                if (StringUtils.isBlank(fylb)) {
//                    查询全部已匹配
                    mapList = insCZMedicareMapper.queryALLMatchedItem(paramMap);
                } else if (yplb.contains(fylb)) {
//                    查询已匹配的药品
                    mapList = insCZMedicareMapper.queryMatchedDrug(paramMap);
                } else {
//                    查询已匹配的收费项目
                    mapList = insCZMedicareMapper.queryMatchedSFXM(paramMap);
                }
                break;
            case "2":
                if (StringUtils.isBlank(fylb)) {
//                    查询全部未匹配
                    mapList = insCZMedicareMapper.queryALLNotMatchedItem(paramMap);
                } else if (yplb.contains(fylb)) {
//                    查询未匹配的药品
                    mapList = insCZMedicareMapper.queryNotMatchedDrug(paramMap);
                } else {
//                    查询未匹配的收费项目
                    mapList = insCZMedicareMapper.queryNotMatchedSFXM(paramMap);
                }
                break;
            default:
                break;
        }
        Page<CompareSearchResult> page = MyBatisPage.getPage();
        page.setRows(mapList);
        return page;
    }

    /**
     * 保存医保匹配信息
     * 交易码：015001013003
     *
     * @param param
     * @param user
     */
    public void saveCompareInfo(String param, IUser user) {
        YBCompareInfo ybCompareInfo = JsonUtil.readValue(param, YBCompareInfo.class);
        if (ybCompareInfo == null) {
            throw new BusException("交易码：015001013003，缺少参数信息！");
        }
//        查询是否存在匹配信息
        Integer count = insCZMedicareMapper.queryCountById(ybCompareInfo.getSfxmid());
//        添加或更新记录
        Date date = new Date(System.currentTimeMillis());
        ybCompareInfo.setModifyTime(date);
        if (count > 0) {
            insCZMedicareMapper.updateCompareInfo(ybCompareInfo);
        } else {
            ybCompareInfo.setDelFlag("0");
            insCZMedicareMapper.addCompareInfo(ybCompareInfo);
//            insCZMedicareMapper.insertTest(ybCompareInfo);
        }
    }

    /**
     * 删除医保匹配信息
     * 交易码：015001013004
     *
     * @param param
     * @param user
     */
    public void deleteCompareInfo(String param, IUser user) {
        YBCompareInfo ybCompareInfo = JsonUtil.readValue(param, YBCompareInfo.class);
        if (ybCompareInfo == null) {
            throw new BusException("交易码：015001013004，缺少参数信息！");
        }
        Date date = new Date(System.currentTimeMillis());
        ybCompareInfo.setModifyTime(date);
        insCZMedicareMapper.deleteCompareInfo(ybCompareInfo);
    }

    /**
     * 查询签到签退记录
     * 交易码：015001013005
     *
     * @param param:beginTime 起始时间，endTime 终止时间， status 签到状态, pageIndex 页码，pageSize 页面记录数， pkOrg 机构， 签到人 pkEmp
     * @param user
     * @return
     */
    public Page<LogInOutInfo> searchLogInOutRecord(String param, IUser user) {
        ParamSearchLogInOutRecord paramSearch = JsonUtil.readValue(param, ParamSearchLogInOutRecord.class);
        if (paramSearch == null) {
            throw new BusException("交易码：015001013005，缺少参数信息！");
        }
        int pageIndex = paramSearch.getPageIndex();
        int pageSize = paramSearch.getPageSize();
        MyBatisPage.startPage(pageIndex, pageSize);
        List<LogInOutInfo> rows = insCZMedicareMapper.queryLogInOutRecord(paramSearch);
        Page<LogInOutInfo> page = MyBatisPage.getPage();
        page.setRows(rows);
        return null;
    }

    /**
     * 保存签到签退信息
     * 交易码：015001013006
     *
     * @param param
     * @param user
     */
    public void saveLogInOutInfo(String param, IUser user) {
        LogInOutInfo info = JsonUtil.readValue(param, LogInOutInfo.class);
//        判断是否存在已签到信息
        String id = insCZMedicareMapper.queryLoginInfo(info);
        info.setDelFlag("0");
        if (StringUtils.isBlank(id)) {
            insCZMedicareMapper.insertLoginInfo(info);
        } else {
            info.setId(id);
            insCZMedicareMapper.updateLogoutInfo(info);
        }
    }

    /**
     * 保存医保登记信息
     * 交易码：015001013007
     *
     * @param param
     * @param user
     */
    public void saveYbRegister(String param, IUser user) {
        DataResult dataResult = JsonUtil.readValue(param, DataResult.class);
        if (dataResult == null || dataResult.getFsfjylsh() == null || dataResult.getZxjylsh() == null || dataResult.getValue() == null) {
            throw new BusException("交易码：015001013007，缺少医保登记信息");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        YBRegisterInfo info = objectMapper.convertValue(dataResult.getValue(), YBRegisterInfo.class);
        info.setId(UUID.randomUUID().toString().replace("-", ""));
        info.setFsfjylsh(dataResult.getFsfjylsh());
        info.setZxjylsh(dataResult.getZxjylsh());
        info.setModifier(user.getId());
        info.setModifyTime(new Date(System.currentTimeMillis()));
        insCZMedicareMapper.insertYbRegisterRecord(info);
    }

    /**
     * 更新医保登记状态，撤销登记时调用，更新del_flag,modifier,modify_Time字段
     * 交易码：015001013008
     *
     * @param param
     * @param user
     */
    public void updateYbRegister(String param, IUser user) {
        YBRegisterInfo info = JsonUtil.readValue(param, YBRegisterInfo.class);
        if (info == null) {
            throw new BusException("交易码：015001013008，缺少医保登记信息");
        }
        info.setModifyTime(new Date(System.currentTimeMillis()));
        info.setModifier(user.getId());
        insCZMedicareMapper.updateYbRegisterRecord(info);
    }

    /**
     * 保存医保结算信息
     * 交易码：015001013009
     *
     * @param param
     * @param user
     */
    public void saveYbSettle(String param, IUser user) {
        DataResult data = JsonUtil.readValue(param, DataResult.class);
        if (data == null) {
            throw new BusException("交易码：015001013009，缺少医保结算信息");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        YBSettleInfo ybSettleInfo = objectMapper.convertValue(data.getValue(), YBSettleInfo.class);
        ybSettleInfo.setFsfjylsh(data.getFsfjylsh());
        ybSettleInfo.setZxjylsh(data.getZxjylsh());
        ybSettleInfo.setModifyTime(new Date(System.currentTimeMillis()));
        ybSettleInfo.setModifier(user.getId());
        ApplicationUtils.setDefaultValue(ybSettleInfo, true);
        DataBaseHelper.insertBean(ybSettleInfo);
    }

    /**
     * 查询医保登记信息
     * 交易码：015001013010
     *
     * @param param
     * @param user
     * @return
     */
    public DataResult<YBRegisterInfo> queryYbRegisterInfo(String param, IUser user) {
//        根据流水号，查询医保登记记录
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Object pkPv = paramMap.get("pkPv");
        Object djly = paramMap.get("djly");
        if (paramMap == null || pkPv == null || djly == null) {
            throw new BusException("交易015001013010：参数中没有患者就诊信息，无法查询医保登记信息");
        }
        YBRegisterInfo info = new YBRegisterInfo();
        if ("1".equals(djly.toString())) {
//            查询门诊登记信息，需要通过关系表进行查询,先获取医保流水号
            String ybLsh = insCZMedicareMapper.queryYbLshByPkPv(pkPv.toString());
            info = insCZMedicareMapper.queryRegisterByYbLsh(ybLsh);
        } else {
//            查询住院登记信息,可以直接根据pv_encounter中的流水号查询
            info = insCZMedicareMapper.queryRegisterInfoByPkPv(pkPv.toString());
        }
        DataResult<YBRegisterInfo> result = new DataResult<>();
        result.setFsfjylsh(info.getFsfjylsh());
        result.setValue(info);
        return result;
    }

    /**
     * 根据his项目id列表查询医保匹配信息
     * 交易码：015001013011
     *
     * @param param
     * @param user
     * @return
     */
    public List<YBItem> queryComparedInfoByHisIds(String param, IUser user) {
        ParamSearchComparedInfo paramInfo = JsonUtil.readValue(param, ParamSearchComparedInfo.class);
        if (paramInfo == null) {
            throw new BusException("交易码：015001013011，缺少收费项目信息");
        }
        String yblx = paramInfo.getYblx();
        List<String> idList = paramInfo.getIdList();
        List<YBItem> list = insCZMedicareMapper.queryComparedInfo(yblx, idList);
        return list;
    }

    /**
     * 保存医保明细上传信息
     * 交易号：015001013012
     *
     * @param param
     * @param user
     */
    public void saveYbUpLoadInfo(String param, IUser user) {
        DataResult dataResult = JsonUtil.readValue(param, DataResult.class);
        if (dataResult == null) {
            throw new BusException("交易号：015001013012，缺少上报信息");
        }
        List<YBUpLoadInfo> ybUpLoadInfos = JSON.parseArray(JSON.toJSONString(dataResult.getValue()), YBUpLoadInfo.class);
        for (YBUpLoadInfo ybUpLoadInfo : ybUpLoadInfos) {
            ybUpLoadInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            ybUpLoadInfo.setFsfjylsh(dataResult.getFsfjylsh());
            ybUpLoadInfo.setZxjylsh(dataResult.getZxjylsh());
            ybUpLoadInfo.setModifier(user.getId());
            ybUpLoadInfo.setModifyTime(new Date(System.currentTimeMillis()));
            ybUpLoadInfo.setDelFlag("0");
            ybUpLoadInfo.setCreator(user.getId());
            ybUpLoadInfo.setCreateTime(new Date(System.currentTimeMillis()));
            ybUpLoadInfo.setTs(new Date(System.currentTimeMillis()));
//            因为有拦截器，暂时先这么写，之后再优化
            insCZMedicareMapper.insertYbUpLoadInfo(ybUpLoadInfo);
        }
    }

    /**
     * 更新处方明细上报状态
     * 交易号：015001013013
     *
     * @param param
     * @param user
     */
    public void updateYbUpLoadInfo(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (map == null) {
            throw new BusException("交易号：015001013013，缺少明细信息");
        }
        String lsh = map.get("lsh") == null ? null : map.get("lsh").toString();
        String fsfjylsh = map.get("fsfjylsh") == null ? null : map.get("fsfjylsh").toString();
        String cfmxid = map.get("cfmxid") == null ? null : map.get("cfmxid").toString();
        YBUpLoadInfo ybUpLoadInfo = new YBUpLoadInfo();
        ybUpLoadInfo.setLsh(lsh);
        ybUpLoadInfo.setFsfjylsh(fsfjylsh);
        ybUpLoadInfo.setCfmxid(cfmxid);
        ybUpLoadInfo.setModifyTime(new Date(System.currentTimeMillis()));
        ybUpLoadInfo.setModifier(user.getId());
        insCZMedicareMapper.updateYbUpLoadInfo(ybUpLoadInfo);
    }

    /**
     * 保存HIS和医保之间的关系
     * 交易号：015001013014
     *
     * @param param
     * @param user
     */
    public void saveYbRelationship(String param, IUser user) {
        YBRelationship ybRelationship = JsonUtil.readValue(param, YBRelationship.class);
        if(ybRelationship == null){
            throw new BusException("交易号：015001013014,参数中缺少保存信息");
        }
        ybRelationship.setPkOrg("~");
        ybRelationship.setModifier(user.getId());
        ybRelationship.setModifyTime(new Date(System.currentTimeMillis()));
        //设置使用默认值
        ApplicationUtils.setDefaultValue(ybRelationship, true);

        List<Map<String, String>> list = ybRelationship.getRelationPkCg();
        if (list != null && !list.isEmpty()) {
//            部分退费，存储his的费用明细之间对照信息，并且要更新ins_czyb_cfmx表中的字段
            for (Map<String, String> map : list) {
                String pkCgOld = map.get("pkCgOld");
                String pkCgNew = map.get("pkCgNew");
                ybRelationship.setPkCgOld(pkCgOld);
                ybRelationship.setPkCgNew(pkCgNew);
                ybRelationship.setId(UUID.randomUUID().toString().replace("-", ""));
                DataBaseHelper.insertBean(ybRelationship);
                insCZMedicareMapper.updateYbCfmxByJsid(ybRelationship.getPkPtmzjs(), pkCgOld, pkCgNew);
            }
        } else {
            DataBaseHelper.insertBean(ybRelationship);
        }
    }

    /**
     * 查询医保结算信息
     * 交易号：015001013015
     *
     * @param param
     * @param user
     * @return
     */
    public YBSettleInfo queryYbSettleInfo(String param, IUser user) {
        YBRelationship ybRelationship = JsonUtil.readValue(param, YBRelationship.class);
        if (ybRelationship == null) {
            throw new BusException("交易号：015001013015，参数中没有HIS结算信息");
        }
//        根据关系表中的信息，关联结算表，获取结算信息
        List<YBSettleInfo> infoList = insCZMedicareMapper.queryYbSettleInfo(ybRelationship);
        YBSettleInfo info = new YBSettleInfo();
        if (infoList != null && !infoList.isEmpty()) {
            info = infoList.get(0);
        }
        return info;
    }

    /**
     * 更新医保结算信息
     * 交易号：015001013016
     *
     * @param param
     * @param user
     */
    public void updateYbSettle(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap.get("fyjsid") == null || paramMap.get("sfcxdj") == null) {
            throw new BusException("交易号：015001013016，参数中缺少信息");
        }
        String ybPkSettle = paramMap.get("fyjsid").toString();
        String sfcxdj = paramMap.get("sfcxdj").toString();
        if ("1".equals(sfcxdj)) {
//            同步撤销医保登记
            insCZMedicareMapper.updateYbRegisterByFyjsid(ybPkSettle);
        }
        YBSettleInfo ybSettleInfo = new YBSettleInfo();
        ybSettleInfo.setFyjsid(ybPkSettle);
        ybSettleInfo.setModifier(user.getId());
        ybSettleInfo.setModifyTime(new Date(System.currentTimeMillis()));
        insCZMedicareMapper.updateYbSettle(ybSettleInfo);
    }

    /**
     * 根据就诊信息和pk_cgop集合查询需要上传到医保平台的处方明细
     * 交易号：015001013017
     *
     * @param param
     * @param user
     * @return
     */
    public List<YBCfmx> queryCfmxByCGOPS(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap.get("list") == null) {
            throw new BusException("交易号：015001013017，参数中没有收费信息");
        }
        List<String> list = JSON.parseArray(JSON.toJSONString(paramMap.get("list")), String.class);
        if (list == null || list.isEmpty()) {
            throw new BusException("交易号：015001013017，参数中没有明细信息");
        }
        List<YBCfmx> result = insCZMedicareMapper.queryCfmxByCGOPS(list);
        return result;
    }

    /**
     * 查询匹配的诊断信息
     * 交易号：015001013018
     *
     * @param param
     * @param user
     * @return
     */
    public ComparedDiagInfo queryYbDiagCode(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Object codeDiag = paramMap.get("codeDiag");
        if (codeDiag == null) {
            throw new BusException("交易号：015001013018，查询匹配诊断信息时，参数中未获取到医院诊断编码");
        }
        ComparedDiagInfo result = insCZMedicareMapper.queryYbDiagCode(codeDiag.toString());
        return result;
    }

    /**
     * 根据医保结算主键，查询处方明细
     * 交易号：015001013019
     *
     * @param param
     * @param user
     * @return
     */
    public List<ParamUpLoad> queryCFMXByYbPkSettle(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || paramMap.get("fyjsid") == null) {
            throw new BusException("交易号：015001013019，参数中没有结算信息");
        }
        String fyjsid = paramMap.get("fyjsid").toString();
        return insCZMedicareMapper.queryCFMXByYbPkSettle(fyjsid);
    }

    /**
     * 查询未提交的处方明细
     * 交易号：015001013020
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryBdItemAndOrderByPkPv(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null || paramMap.get("pkPV") == null){
            throw new BusException("交易号：015001013020，参数中缺少就诊主键信息！");
        }
        return insCZMedicareMapper.queryBdItemAndOrderByPkPv(paramMap);
    }

    /**
     * 查询医保单边处方明细数据
     * 交易号：015001013021
     *
     * @param param
     * @param user
     * @return
     */
    public List<String> queryErrorFsfjylshByLsh(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap.get("lsh") == null) {
            throw new BusException("交易号：015001013021，参数中未携带流水号信息");
        }
        return insCZMedicareMapper.queryErrorFsfjylshByLsh(paramMap.get("lsh").toString());
    }

    /**
     * 根据his流水号，查询医保登记信息
     * 交易号：015001013022
     *
     * @param param
     * @param user
     * @return
     */
    public YBRegisterInfo queryRegInfoByLsh(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        Object lsh = paramMap.get("lsh");
        if (lsh == null) {
            throw new BusException("交易号：015001013022，参数中没有lsh信息，查询医保登记信息失败");
        }
//        住院登记，医保和HIS的流水号相同
        YBRegisterInfo ybRegisterInfo = insCZMedicareMapper.queryRegisterInfoByLsh(lsh.toString());
        if (ybRegisterInfo != null) {
            return ybRegisterInfo;
        } else {
//        门诊登记的时候，医保流水号和HIS流水号不同。
            ybRegisterInfo = insCZMedicareMapper.queryRegInfoByMzLsh(lsh.toString());
            return ybRegisterInfo;
        }
    }
}
