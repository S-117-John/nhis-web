package com.zebone.nhis.compay.ins.qgyb.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.compay.ins.qgyb.dao.NationalInsuChkMapper;
import com.zebone.nhis.compay.ins.qgyb.vo.CheckAccDetailVo;
import com.zebone.nhis.compay.pub.service.NationalInsuranceChkHandler;
import com.zebone.nhis.compay.pub.service.NationalInsuranceUpDownService;
import com.zebone.nhis.compay.pub.support.NationalDict;
import com.zebone.nhis.compay.pub.support.NationalTool;
import com.zebone.nhis.compay.pub.vo.InsChkAccSumVo;
import com.zebone.nhis.compay.pub.vo.InsDownFileParamVo;
import com.zebone.nhis.compay.pub.vo.InsFileResponse;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 全国医保业务服务类
 */
@Service
public class NationalInsuChkService {

    @Resource
    private NationalInsuChkMapper nationalInsuChkMapper;
    @Autowired
    private NationalInsuranceChkHandler insuranceChkHandler;
    @Resource
    private NationalInsuranceUpDownService upDownService;

    /**
     * 查询对账列表
     * @param param {"dateBegin":"yyyy-MM-dd","dateEnd":"yyyy-MM-dd","insuType":""}
     * @param user
     * @return
     */
    public List<InsChkAccSumVo> queryAccSum(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
        List<InsChkAccSumVo> sumVos = nationalInsuChkMapper.qryAccSum(paramMap);
        sumVos.stream().forEach(sumVo -> {
            sumVo.setDateBegin(MapUtils.getString(paramMap,"dateBegin"));
            sumVo.setDateEnd(MapUtils.getString(paramMap,"dateEnd"));
            sumVo.setInsuType(MapUtils.getString(paramMap,"insuType"));
        });
        return sumVos;
    }
    /**
     * 对账-获取总对总账结果
     * @param param
     * @param user
     * @return
     */
    public List<InsChkAccSumVo> getAccSumOfCheck(String param, IUser user){
        validateAccParam(JsonUtil.readValue(param,Map.class));
        List<InsChkAccSumVo> inList = queryAccSum(param, user);
        for (InsChkAccSumVo sumVo : inList) {
            Map<String, Object> map = insuranceChkHandler.checkAccSum(sumVo);
            sumVo.setStmtRslt(MapUtils.getString(map,"stmtRslt"));
            sumVo.setStmtRsltDes(MapUtils.getString(map,"stmtRsltDes"));
            sumVo.setStmtRsltDscr(MapUtils.getString(map,"stmtRsltDscr"));
        }
        return inList;
    }

    /**
     * 获取明细待对账数据
     * @param param
     * @param user
     * @return
     */
    public List<CheckAccDetailVo> getAccDetail(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        return nationalInsuChkMapper.qryAccDetail(paramMap);
    }

    /**
     * 对账--获取对账明细文件信息（数据+上传操作）
     * @param param
     * @param user
     * @return
     */
    public InsFileResponse getAccDetailUpload(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
        validateAccParam(paramMap);
        String clrType = MapUtils.getString(paramMap, "detailClrType");
        paramMap.put("clrType", clrType);
        List<CheckAccDetailVo> detailList = nationalInsuChkMapper.qryAccDetail(paramMap);
        if(detailList.size()==0){
            throw new BusException("没有获取到数据");
        }
        List<Object[]> list = detailList.stream().map(vo -> {
            Object[] objects = new Object[7];
            objects[0] = vo.getSetlId();
            objects[1] = vo.getMdtrtId();
            objects[2] = vo.getPsnNo();
            objects[3] = vo.getMedfeeSumamt();
            objects[4] = vo.getFundPaySumamt();
            objects[5] = vo.getAcctPay();
            objects[6] = vo.getRefdSetlFlag();
            return objects;
        }).collect(Collectors.toList());

        List<InsChkAccSumVo> stList = Lists.newArrayList();
        detailList.stream().collect(Collectors.groupingBy(StringUtils.isBlank(clrType)?CheckAccDetailVo::getMockGroup:CheckAccDetailVo::getClrType))
                .forEach((k,v) ->{
                    Optional<CheckAccDetailVo> sum  = v.stream().reduce((v1, v2) ->{
                        CheckAccDetailVo vo = new CheckAccDetailVo();
                        vo.setCashPayamt(MathUtils.add(v1.getCashPayamt(),v2.getCashPayamt()));
                        vo.setFundPaySumamt(MathUtils.add(v1.getFundPaySumamt(),v2.getFundPaySumamt()));
                        vo.setMedfeeSumamt(MathUtils.add(v1.getMedfeeSumamt(),v2.getMedfeeSumamt()));
                        return vo;
                    });
                    CheckAccDetailVo vo = sum.get();
                    InsChkAccSumVo sumVo = new InsChkAccSumVo();
                    sumVo.setAmountCash(vo.getCashPayamt());
                    sumVo.setAmountInsu(vo.getFundPaySumamt());
                    sumVo.setAmountSt(vo.getMedfeeSumamt());
                    sumVo.setDataNum(v.size());
                    sumVo.setDateBegin(MapUtils.getString(paramMap,"dateBegin"));
                    sumVo.setDateEnd(MapUtils.getString(paramMap,"dateEnd"));
                    sumVo.setClrType(k);
                    stList.add(sumVo);
                });

        InsFileResponse infRes = upDownService.uploadFile("acd_", list);
        if(StringUtils.isBlank(infRes.getFileQuryNo())){
            throw new BusException("医保中心未返回文件编号");
        }
        return insuranceChkHandler.checkAccDetail(stList.get(0), infRes.getFileQuryNo());
    }

    /**
     * 对账--获取对账明细结果（下载操作）
     * @param param
     * @param user
     * @return
     */
    public List<CheckAccDetailVo> getAccDetailDownload(String param, IUser user) {
        InsFileResponse response = JsonUtil.readValue(param, InsFileResponse.class);
        if(StringUtils.isAnyBlank(response.getFileQuryNo(),response.getFilename())){
            throw new BusException("文件号、文件名都不能为空");
        }
        List<Object[]> dataList = upDownService.downAndGet("ac_down_", new InsDownFileParamVo(response.getFileQuryNo(), response.getFilename()));
        List<CheckAccDetailVo> detailVos = NationalTool.fillData(NationalDict.ACC_DETAIL, dataList, CheckAccDetailVo.class);
        detailVos.sort(Comparator.comparing(vo -> vo.getSetlId()));
        return detailVos;
    }

    /**
     * 冲正交易调用
     * @param param
     * @param user
     */
    public void doReverse(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
        String psnNo = MapUtils.getString(paramMap, "psNo");
        String omsgId = MapUtils.getString(paramMap, "omsgId");
        String oinfNo = MapUtils.getString(paramMap, "oinfNo");
        if(StringUtils.isAnyBlank(psnNo, omsgId,oinfNo)){
            throw new BusException("冲正方法psNo[人员编号]、omsgId[原发送方报文ID]、oinfNo[原交易编号]都不能为空");
        }
        insuranceChkHandler.reverse(psnNo, omsgId, oinfNo);
    }


    private void validateAccParam(Map<String,Object> paramMap){
        if(StringUtils.isAnyBlank(MapUtils.getString(paramMap, "dateBegin")
                ,MapUtils.getString(paramMap, "dateEnd"),MapUtils.getString(paramMap, "insuType"))){
            throw new BusException("开始时间、结束时间、险种类型都不能为空");
        }
    }

    /**
     * 查询其他系统订单
     * @param param
     * @param user
     * @return
     */
    public List<CheckAccDetailVo> getExtSysDetailInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<CheckAccDetailVo> checkAccDetailVo = Lists.newArrayList();
        List<CheckAccDetailVo> checkAccDetailVoZY = nationalInsuChkMapper.getExtSysDetailInfoZY(paramMap);
        List<CheckAccDetailVo> checkAccDetailVoMZ = nationalInsuChkMapper.getExtSysDetailInfoMZ(paramMap);
        if(checkAccDetailVoZY.size() != 0){
            checkAccDetailVo.addAll(checkAccDetailVoZY);
        }
        if (checkAccDetailVoMZ.size() != 0){
            checkAccDetailVo.addAll(checkAccDetailVoMZ);
        }
        return checkAccDetailVo;
    }

}
