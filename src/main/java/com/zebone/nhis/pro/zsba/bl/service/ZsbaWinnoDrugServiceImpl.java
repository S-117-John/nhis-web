package com.zebone.nhis.pro.zsba.bl.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.service.IWinnoDrugService;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsba.bl.dao.ZsbaWinnoDrugMapper;
import com.zebone.nhis.pro.zsba.bl.vo.DrugPresPdUsecateVo;
import com.zebone.nhis.pro.zsba.bl.vo.WinoConfVo;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service("zsBaWinnoDrugService")
public class ZsbaWinnoDrugServiceImpl implements IWinnoDrugService {

    @Resource
    private ZsbaWinnoDrugMapper zsbaWinnoDrugMapper;

    @Override
    public Object getDrugWinno(Object... obj) {
        List<ExPresOcc> presOccList=(List<ExPresOcc>)obj[0];
        if(CollectionUtils.isEmpty(presOccList))
            return null;
        ServiceLocator.getInstance().getBean(ZsbaWinnoDrugServiceImpl.class).setWinConf(presOccList);
        return null;
    }

    /**
     * 设置窗口号
     * @param exPresOccList
     */
    public void setWinConf(List<ExPresOcc> exPresOccList){
        if(CollectionUtils.isEmpty(exPresOccList))
            return;
        List<String> pkPresList = exPresOccList.stream().map(ExPresOcc::getPkPres).collect(Collectors.toList());
        //获取用法分类，准备和发药窗口做匹配
        List<DrugPresPdUsecateVo> useCateList = zsbaWinnoDrugMapper.getPresPdUsecate(pkPresList);
        Map<String,WinoConfVo> wino = new HashMap<>();
        //同一次结算中,相同执行科室(不同处方），匹配到任意一种用法分类的窗口号，那么患者本次所有处方全部是同一个窗口号
        for (ExPresOcc occ:exPresOccList){
            String pkDept = occ.getPkDeptEx();
            WinoConfVo presWino = wino.get(pkDept);
            //根据执行科室为获取到窗口信息或这isFlagCate为false时就获取相关窗口
            //反之presWino不为null和isFlagCate为true时就不进行获取相关窗口
            if(presWino == null || !presWino.isFlagCate()) {
                Map<String,Object> paramWin = new HashMap<>();
                paramWin.put("pkDeptEx",occ.getPkDeptEx());
                paramWin.put("pkDeptAp",occ.getPkDeptPres());
                List<String> multiCates = Lists.newArrayList();
                useCateList.stream().filter(m->occ.getPkPres().equals(m.getPkPres()))
                        .map(vo -> Arrays.asList(vo.getEuUsecate().split(","))).forEach(vo ->multiCates.addAll(vo));
                presWino = getWinNo(paramWin,multiCates);
                wino.put(pkDept, presWino);
            }
            //occ.setWinnoConf(presWino.getWino());
            //occ.setWinnoPrep(presWino.getWinoPrep());
        }
        
        exPresOccList.forEach(occ ->{
        	occ.setWinnoConf(wino.get(occ.getPkDeptEx()).getWino());
            occ.setWinnoPrep(wino.get(occ.getPkDeptEx()).getWinoPrep());
        });
        
        DataBaseHelper.batchUpdate("update ex_pres_occ set winno_conf=:winnoConf,winno_prep=:winnoPrep where pk_presocc=:pkPresocc"
                ,exPresOccList);
    }

    public WinoConfVo getWinNo(Map<String,Object> paramMap,List<String> multiCates) {
        String pkDeptEx = MapUtils.getString(paramMap,"pkDeptEx");
        //门诊药房窗口分配方式：0 按业务量，1 按开立科室
        String winType = ApplicationUtils.getDeptSysparam("EX0030", pkDeptEx);
        //门诊药房发配药模式：1-配药发药模式，2-直接发药模式
        String giveType =ApplicationUtils.getDeptSysparam("EX0001", pkDeptEx);
        WinoConfVo winoConfVo = null;
        if(CommonUtils.isEmptyString(winType)){
            throw new BusException("请维护系统参数【EX0030】！");
        }
        if(CommonUtils.isEmptyString(giveType)){
            throw new BusException("请维护系统参数【EX0001】！");
        }

        List<WinoConfVo> confVoList;
        if (EnumerateParameter.ZERO.equals(winType)) {//按业务量
            paramMap.put("euObjtype", null);
        }else if(EnumerateParameter.ONE.equals(winType)){//按开立科室
            paramMap.put("euObjtype", EnumerateParameter.ZERO);
        }else{//诊区
            paramMap.put("euObjtype", EnumerateParameter.ONE);
        }

        Map<String,Object> deptMap=DataBaseHelper.queryForMap("select name_dept from bd_ou_dept where pk_dept=?"
                ,new Object[]{MapUtils.getString(paramMap,"pkDeptEx")});
        String nameDept = MapUtils.getString(deptMap,"nameDept");
        //依据条件查询窗口信息
        confVoList = zsbaWinnoDrugMapper.getWino(paramMap);
        if(confVoList==null || confVoList.size()==0){
            throw new BusException("科室【"+nameDept+"】,【EX0030="+winType+"】,未获取到有效在线窗口！");
        }
        confVoList.sort(Comparator.comparing(WinoConfVo::getCnt));
        //按开立科室时做特殊处理逻辑
        if(EnumerateParameter.ONE.equals(winType)){
            //依据用法分类匹配是否有可用窗口号，由于用法给为多选，查出来是字符串用 逗号 分隔的
            List<WinoConfVo> cateConf = confVoList.stream().filter(vo -> StringUtils.isNotBlank(vo.getEuUsecate()))
                    .filter(vo -> Arrays.asList(vo.getEuUsecate().split(",")).stream().filter(cate -> multiCates.contains(cate)).count() > 0)
                    .map(vo ->{vo.setFlagCate(true);return vo;})
                    .collect(Collectors.toList());
            //依据优先级匹配是否有可用,没有优先级
            winoConfVo = CollectionUtils.isNotEmpty(cateConf)?getByLevel(cateConf):getByLevel(confVoList);
        }
        //其他按照业务量取小
        if(winoConfVo == null) {
            winoConfVo = confVoList.get(0);
        }
        boolean flagPF = EnumerateParameter.ONE.equals(giveType);//配药发药
        if(flagPF && StringUtils.isBlank(winoConfVo.getWino())){
            throw new BusException("【科室【"+nameDept+"】,【EX0001】配药发药时，未找到在线发药窗口，请维护！");
        }
        if(flagPF && StringUtils.isBlank(winoConfVo.getWinoPrep())){
            throw new BusException("【科室【"+nameDept+"】,【EX0001】配药发药时，未找到发药窗口【"+winoConfVo.getWino()+"】关联在线配药窗口，请维护！");
        }
        if(!flagPF && StringUtils.isBlank(winoConfVo.getWino())){
            throw new BusException("【科室【"+nameDept+"】,【EX0001】直接发药时，未找到在线发药窗口，请维护！");
        }

        return winoConfVo;
    }

    private WinoConfVo getByLevel(List<WinoConfVo> root){
        List<WinoConfVo> levelConf = root.stream().filter(vo -> vo.getLevelNum() != null && vo.getLevelNum() > 0).sorted(Comparator.comparing(WinoConfVo::getLevelNum)).collect(Collectors.toList());
        return levelConf.size()>0?levelConf.stream().sorted(Comparator.comparing(WinoConfVo::getLevelNum)).findFirst().get():root.get(0);
    }
}
