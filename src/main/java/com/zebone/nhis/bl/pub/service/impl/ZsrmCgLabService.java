package com.zebone.nhis.bl.pub.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("zsrmCgLabService")
public class ZsrmCgLabService  implements ICgLabService {
    @Resource
    private CgStrategyPubMapper cgStrategyPubMapper;

    @Autowired
    private CgProcessHandler cgProcessHandler;

    @Override
    public void dealSampAndTubeItem(List<BlPubParamVo> blCgPubParamVos) {
        /*容器标本费用合并
         *检验试管合并记费规则：
         *   1）使用检验合并代码进行合并（合并代码生成规则：标本编码+试管编码+检验分组编码+自定义编码）进行合并记费；
         *   2）相同检验项目的试管不合并
         * */

        if (blCgPubParamVos!=null && blCgPubParamVos.size()>0) {
            //获取标本容器费用信息
            List<BlPubParamVo> sampList = blCgPubParamVos.stream()
                    .filter(m-> m.getSampTube()!=null && m.getSampTube() && !CommonUtils.isEmptyString(m.getPkCnord()))
                    .collect(Collectors.toList());

            if(sampList!=null && sampList.size()>0){
                //获取pkCnord信息
                List<String> pkCnordList = sampList.stream()
                        .map(BlPubParamVo::getPkCnord)
                        .collect(Collectors.toList());

                //查询医嘱合并代码
                List<Map<String,Object>> combList = cgStrategyPubMapper.qryOrdCombByPkCnord(pkCnordList);
                //查询本次各医嘱总记费数量
                List<Map<String,Object>> ordCgList = cgStrategyPubMapper.qryOrdChargeCnt(pkCnordList);

                if(ordCgList!=null && ordCgList.size()>0){
                    //将查出的医嘱记费数量存入到sampList集合
                    sampList.stream()
                            .forEach(dt->{
                                for(Map<String,Object> cntMap : ordCgList){
                                    //两个集合的pkOrd一样则把记费数量赋值到sampList集合
                                    if(cntMap.containsKey("pkOrd")
                                        && !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(cntMap,"pkOrd"))
                                        && CommonUtils.getPropValueStr(cntMap,"pkOrd").equals(dt.getPkOrdChk())){
                                        dt.setOrdCntChk(CommonUtils.getDouble(cntMap.get("cnt")));
                                    }
                                }
                            });
                }

                if(combList!=null && combList.size()>0){
                    //合并
                    cgProcessHandler.hbOrdInfoBycodeComb(blCgPubParamVos,sampList,combList);
                }
            }
        }
    }

    @Override
    public List<ItemPriceVo> filterOrdItem(List<ItemPriceVo> priceList, List<ItemPriceVo> itemList) {
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
            if(BlcgUtil.converToTrueOrFalse(ApplicationUtils.getSysparam("BL0029", false))){//读取参数BL0029（开立时间相同的检验类医嘱重叠项目合并记费）
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
