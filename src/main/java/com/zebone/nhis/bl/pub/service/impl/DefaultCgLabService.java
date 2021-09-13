package com.zebone.nhis.bl.pub.service.impl;

import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.service.ICgLabService;
import com.zebone.nhis.bl.pub.syx.dao.CgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.support.CgProcessHandler;
import com.zebone.nhis.bl.pub.syx.vo.ExOrdItemVo;
import com.zebone.nhis.bl.pub.syx.vo.OrdNumVo;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("defaultCgLabService")
public class DefaultCgLabService implements ICgLabService {

    @Resource
    private CgStrategyPubMapper cgStrategyPubMapper;

    @Autowired
    private CgProcessHandler cgProcessHandler;

    /**
     * 处理检验标本、容器费用记录
     * <br>默认策略检验标本，一次就诊，相同标本只收一次费用，相同容器只收一次
     * @param blCgPubParamVos
     * @return
     */
    @Override
    public void dealSampAndTubeItem(List<BlPubParamVo> blCgPubParamVos) {
        if(CollectionUtils.isNotEmpty(blCgPubParamVos)){
            Map<String,BlPubParamVo> sampMap = new HashMap<>();
            Map<String,BlPubParamVo> tupeMap = new HashMap<>();
            Map<String,BlPubParamVo> collMap = new HashMap<>();
            for (int i = blCgPubParamVos.size() - 1; i >= 0; i--) {
                BlPubParamVo vo = blCgPubParamVos.get(i);
                if(vo !=null && vo.getSampTube()!=null && vo.getSampTube()){
                    String stuff = "_$_$_"+vo.getPkItem();
                    if(StringUtils.isNotBlank(vo.getDtSamptype())){
                        sampMap.put(vo.getDtSamptype()+stuff,vo);
                        blCgPubParamVos.remove(i);
                    } else if(StringUtils.isNotBlank(vo.getDtTubetype())){
                        tupeMap.put(vo.getDtTubetype()+stuff,vo);
                        blCgPubParamVos.remove(i);
                    } else if(StringUtils.isNotBlank(vo.getDtColltype())){
                        collMap.put(vo.getDtColltype()+stuff,vo);
                        blCgPubParamVos.remove(i);
                    }
                }
            }
            if(sampMap.size()>0){
                blCgPubParamVos.addAll(sampMap.values());
            }
            if(tupeMap.size()>0){
                blCgPubParamVos.addAll(tupeMap.values());
            }
            if(collMap.size()>0){
                blCgPubParamVos.addAll(collMap.values());
            }
        }
    }

    /**
     * 检验医嘱收费特殊处理<br>
     * 	 检验医嘱特殊处理
     * 	 处理开始时间和执行科室相同检验医嘱下相同的收费项目：
     * 		(1).删除相同的收费项目，只保留一个(删除收费项目多的一组医嘱下的收费项目)
     * 		(2).如果医嘱项目下收费项目数量>8,则此医嘱下的收费项目单价打8折
     *
     */
    @Override
    public List<ItemPriceVo> filterOrdItem(List<ItemPriceVo> priceList,List<ItemPriceVo> itemList) {
        //查询检验医嘱对应的分类编码
        List<String> typeList = cgStrategyPubMapper.qryOrdType();
        //检验医嘱主键集合
        List<String> pkOrdList = new ArrayList<>();
        //检验医嘱ItemList集合
        List<ItemPriceVo> jYList = new ArrayList<>();

        for(ItemPriceVo vo : itemList){
            //校验是否是检验医嘱
            if(CommonUtils.isEmptyString(vo.getPkOrdOld()) || !typeList.contains(vo.getCodeOrdtype())) {
                continue;
            }
            pkOrdList.add(vo.getPkOrdOld());
            jYList.add(vo);
        }

        if(jYList!=null && jYList.size()>0){
            //读取参数BL0029（开立时间相同的检验类医嘱重叠项目合并记费）
            // 参数值为0时不做处理，参数值为1时做收费项目合并处理，参数值为2时只处理住院计费，参数值为3时只处理门诊计费
            String paramVal = ApplicationUtils.getSysparam("BL0029", false);
            //if the query does not return exactly one row, or does not return exactly one column in that row DataAccessException
            String euPvtype = DataBaseHelper.getJdbcTemplate().queryForObject("select EU_PVTYPE from PV_ENCOUNTER where PK_PV=?"
                    , new Object[]{jYList.get(0).getPkPv()}, String.class);
            if("1".equals(paramVal)
                    || ("2".equals(paramVal) && "3".equals(euPvtype))
                    || ("3".equals(paramVal) && ("1".equals(euPvtype)||"2".equals(euPvtype)))){
                //查询检验医嘱下的收费项目数量
                List<OrdNumVo> ordNumList = cgStrategyPubMapper.qryOrdItemNum(pkOrdList);;
                for(ItemPriceVo jyVo : jYList){
                    for(ItemPriceVo itemVo : jYList){
                        //校验两检验医嘱开始时间和执行科室是否相同
                        if(jyVo.getDateStart()!=null && itemVo.getDateStart()!=null
                                && jyVo.getDateStart().compareTo(itemVo.getDateStart())==0
                                && jyVo.getPkDeptEx().equals(itemVo.getPkDeptEx())
                                && !jyVo.getPkCnord().equals(itemVo.getPkCnord())
                                && jyVo.getDtSamptype().equals(itemVo.getDtSamptype())){
                            //相同标本参与合并
                            //合并检验医嘱下相同的收费项目
                            cgProcessHandler.hBOrdItem(jyVo,itemVo,priceList,ordNumList);
                        }
                    }
                }
            }

            /**重新计算医嘱下收费项目价格*/
            if(BlcgUtil.converToTrueOrFalse(ApplicationUtils.getSysparam("BL0028", false))){
                //读取参数BL0028（检验类医嘱下收费项目数超8项打8折）
                priceList = cgProcessHandler.jsOrdItemRrice(priceList, jYList);
            }
        }
        return priceList;
    }

    /**
     * 检查医嘱特殊处理
     * 对单次就诊未结算的检查医嘱做记费处理时，如果未结算的医嘱下关联的收费项目有重复，且“参与合并”的标志为true，那么记费时去掉重复收费项。
     * @param priceList
     */
    @Override
    public void filterExOrdItem(List<ItemPriceVo> priceList) {
        if(priceList==null || priceList.size()<0){
            return;
        }

        //获取系统参数BL0062校验同时开立检查类组套重叠项目是否合并记费
        String flagHbCharge = ApplicationUtils.getSysparam("BL0062", false);
        if(CommonUtils.isEmptyString(flagHbCharge) || !"1".equals(flagHbCharge)){
            return;
        }

        for(int i =priceList.size() - 1; i >= 0; i--){
            if(CommonUtils.isEmptyString(priceList.get(i).getPkCnord())
                    ||CommonUtils.isEmptyString(priceList.get(i).getCodeOrdtype())
                    ||!"02".equals(priceList.get(i).getCodeOrdtype().substring(0, 2))){		//判断code_ordtype值，以“02”开头执行以下逻辑，否则不做处理)
                continue;
            }

            for(int j =priceList.size() - 1; j >= 0; j--){
                if(CommonUtils.isEmptyString(priceList.get(j).getPkCnord())
                        || CommonUtils.isEmptyString(priceList.get(j).getCodeOrdtype())
                        || !"02".equals(priceList.get(j).getCodeOrdtype().substring(0, 2))	//判断code_ordtype值，以“02”开头执行以下逻辑，否则不做处理)
                        || priceList.get(i).getPkCnord().equals(priceList.get(j).getPkCnord())){
                    continue;
                }
                //比较两组医嘱下的收费项目主键是否相同
                if(priceList.get(i).getPkItem().equals(priceList.get(j).getPkItem())){
                    //检验两个收费项目“参与合并”标志是否为true
                    if(priceList.get(i).getFlagUnion()!=null
                        && priceList.get(j).getFlagUnion()!=null
                        && priceList.get(i).getFlagUnion() && priceList.get(j).getFlagUnion()
                    ){
                        //比较收费项目数量，删除值小的项目记录，如果值相同，删除ordsn值大的记录
                        if(Double.compare(priceList.get(i).getQuan(), priceList.get(j).getQuan())==1)
                            priceList.remove(priceList.get(j));
                        else if(Double.compare(priceList.get(i).getQuan(), priceList.get(j).getQuan())==-1)
                            priceList.remove(priceList.get(i));
                        else if(Double.compare(priceList.get(i).getQuan(), priceList.get(j).getQuan())==0){
                            //如果值相同，删除ordsn值大的记录
                            if(priceList.get(i).getOrdsn()>priceList.get(j).getOrdsn())
                                priceList.remove(priceList.get(i));
                            else
                                priceList.remove(priceList.get(j));
                        }

                        break;
                    }
                }

            }
        }
    }

}
