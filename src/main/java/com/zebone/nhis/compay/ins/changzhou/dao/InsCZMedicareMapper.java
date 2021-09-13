package com.zebone.nhis.compay.ins.changzhou.dao;

import com.zebone.nhis.compay.ins.changzhou.vo.*;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InsCZMedicareMapper {
    /**
     * 根据条件，查询医保项目
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> queryYBItem(@Param("paramMap") Map<String, Object> paramMap);

    /**
     * 查询所有已匹配的项目
     * @param paramMap
     * @return
     */
    List<CompareSearchResult> queryALLMatchedItem(@Param("paramMap") Map<String, Object> paramMap);

    /**
     * 查询所有匹配的药品
     * @param paramMap
     * @return
     */
    List<CompareSearchResult> queryMatchedDrug(@Param("paramMap") Map<String, Object> paramMap);

    /**
     * 查询所有匹配的收费项目
     * @param paramMap
     * @return
     */
    List<CompareSearchResult> queryMatchedSFXM(@Param("paramMap") Map<String, Object> paramMap);

    /**
     * 查询所有未匹配的项目
     * @param paramMap
     * @return
     */
    List<CompareSearchResult> queryALLNotMatchedItem(@Param("paramMap") Map<String, Object> paramMap);

    /**
     * 查询未匹配的药品
     * @param paramMap
     * @return
     */
    List<CompareSearchResult> queryNotMatchedDrug(@Param("paramMap") Map<String, Object> paramMap);

    /**
     * 查询未匹配的收费项目
     * @param paramMap
     * @return
     */
    List<CompareSearchResult> queryNotMatchedSFXM(@Param("paramMap") Map<String, Object> paramMap);

    /**
     * 根据匹配字典id查询记录数
     * @param id
     * @return
     */
    Integer queryCountById(@Param("id") String id);

    /**
     * 更新医保匹配信息
     * @param ybCompareInfo
     */
    void updateCompareInfo(@Param("ybCompareInfo") YBCompareInfo ybCompareInfo);

    /**
     * 新增医保匹配信息
     * @param ybCompareInfo
     */
    void addCompareInfo(@Param("ybCompareInfo") YBCompareInfo ybCompareInfo);

    /**
     * 删除匹配信息
     * @param ybCompareInfo
     */
    void deleteCompareInfo(@Param("ybCompareInfo") YBCompareInfo ybCompareInfo);

    void insertTest(YBCompareInfo ybCompareInfo);

    /**
     * 查询用户的签到信息
     * @param info
     * @return
     */
    String queryLoginInfo(LogInOutInfo info);

    /**
     * 保存登录信息
     * @param info
     */
    void insertLoginInfo(LogInOutInfo info);

    /**
     * 更新签退信息
     * @param info
     */
    void updateLogoutInfo(LogInOutInfo info);

    /**
     * 查询签到签退信息
     * @param param
     * @return
     */
    List<LogInOutInfo> queryLogInOutRecord(ParamSearchLogInOutRecord param);

    /**
     * 查询病种目录信息
     * @param paramMap
     * @return
     */
    List<CompareSearchResult> queryDiseaseList(@Param("paramMap") Map<String, Object> paramMap);

    /**
     * 保存医保登记信息
     * @param info
     */
    void insertYbRegisterRecord(YBRegisterInfo info);

    /**
     * 更新医保登记信息，撤销时调用
     * @param info
     */
    void updateYbRegisterRecord(YBRegisterInfo info);

    /**
     * 查询医保登记信息
     * @param pkPv
     * @return
     */
    YBRegisterInfo queryRegisterInfoByPkPv(@Param("pkPv") String pkPv);

    /**
     * 根据hisId集合，查询匹配项目
     * @param yblx
     * @param idList
     * @return
     */
    List<YBItem> queryComparedInfo(@Param("yblx") String yblx, @Param("idList") List<String> idList);

    /**
     * 保存医保上传的处方明细
     * @param item
     */
    void insertYbUpLoadInfo(@Param("item") YBUpLoadInfo item);

    /**
     * 更新处方上报状态
     * @param info
     */
    void updateYbUpLoadInfo(YBUpLoadInfo info);

    /**
     * 查询医保结算信息
     * @param ybRelationship
     * @return
     */
    List<YBSettleInfo> queryYbSettleInfo(YBRelationship ybRelationship);

    /**
     * 更新医保结算状态
     * @param ybSettleInfo
     */
    void updateYbSettle(YBSettleInfo ybSettleInfo);

    /**
     * 查询需要上传的处方明细
     * @param list
     * @return
     */
    List<YBCfmx> queryCfmxByCGOPS(List<String> list);

    /**
     * 查询匹配的诊断信息
     * @param codeDiag
     * @return
     */
    ComparedDiagInfo queryYbDiagCode(@Param("codeDiag") String codeDiag);

    /**
     * 查询医保流水号
     * @param pkPv
     * @return
     */
    String queryYbLshByPkPv(@Param("pkPv") String pkPv);

    /**
     * 根据医保流水号，查询门诊登记信息
     * @param ybLsh
     * @return
     */
    YBRegisterInfo queryRegisterByYbLsh(@Param("ybLsh") String ybLsh);

    /**
     * 根据结算主键，查询处方明细
     * @param fyjsid
     * @return
     */
    List<ParamUpLoad> queryCFMXByYbPkSettle(@Param("fyjsid") String fyjsid);

    /**
     * 根据患者就诊信息，查询未上传的明细
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> queryBdItemAndOrderByPkPv(Map<String, Object> paramMap);

    /**
     * 更新医保登记状态
     * @param ybPkSettle
     */
    void updateYbRegisterByFyjsid(String ybPkSettle);

    void updateYbCfmxByJsid(@Param("fyjsid") String fyjsid, @Param("pkCgOld") String pkCgOld, @Param("pkCgNew") String pkCgNew);

    /**
     * 查询医保单边处方明细数据
     * @param lsh
     * @return
     */
    List<String> queryErrorFsfjylshByLsh(@Param("lsh") String lsh);

    /**
     * 根据流水号查询医保登记信息
     * @param lsh
     * @return
     */
    YBRegisterInfo queryRegisterInfoByLsh(String lsh);

    /**
     * 根据门诊流水号，查询登记信息
     * @param lsh
     * @return
     */
    YBRegisterInfo queryRegInfoByMzLsh(@Param("lsh") String lsh);
}
