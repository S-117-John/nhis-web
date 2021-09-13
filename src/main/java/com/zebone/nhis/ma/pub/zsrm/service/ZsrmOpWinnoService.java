package com.zebone.nhis.ma.pub.zsrm.service;

import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.zsrm.dao.ZsrmOpWinnoMapper;
import com.zebone.nhis.ma.pub.zsrm.vo.DrugPresUsecateVo;
import com.zebone.nhis.ma.pub.zsrm.vo.PressAttInfo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 中山人医药房窗口分窗规则 nhis.scm
 */
@Service
public class ZsrmOpWinnoService {
    @Resource
    private ZsrmOpWinnoMapper zsrmOpWinnoMapper;

    @Resource
    private CommonService commonService;

    private Logger logger = LoggerFactory.getLogger("nhis.scm");


    /**
     * 根据处方用法，开立科室（业务线）获取执行科室
     * @param mapPres
     * @return
     */
    public Object geDeptExOpDrug(Map<String, List<BlOpDt>> mapPres) {
        if (mapPres == null || mapPres.size() == 0) return null;
        mapPres.remove(null);
        if (mapPres == null || mapPres.size() == 0) return null;
        Map<String, List<String>> useDosByPkPres = new HashMap<String, List<String>>();

        String isShowWinno = ApplicationUtils.getSysparam("EX0079",false);

        AtomicBoolean isWinnoExistNull= new AtomicBoolean(false);
        mapPres.forEach((m,n)-> {
            if(isWinnoExistNull.get())return;
            boolean is_temp = n.stream().filter(k -> CommonUtils.isNotNull(k.getPkPres()) && "1".equals(k.getFlagPd())
                    && CommonUtils.isNull(k.getWinnoConf())
                    && CommonUtils.isNull(k.getWinnoPrep())
            ).findAny().isPresent();
            if(is_temp){
                isWinnoExistNull.set(is_temp);
            }
        });

        if(!"1".equals(isShowWinno) || isWinnoExistNull.get()) {
            List<String> pkPresList = new ArrayList<String>();
            mapPres.keySet().forEach(m -> {
                pkPresList.add(m);
            });
            List<DrugPresUsecateVo> useCateList = zsrmOpWinnoMapper.getPresUsecate(pkPresList);

            mapPres = getPresDeptByBus(mapPres);
            mapPres = getDeptByUsecate(mapPres, useCateList);
        }
        List<BlOpDt> opdtList = new ArrayList<>();
        String ordSql = "update cn_order set PK_DEPT_PHARMACY=PK_DEPT_EXEC, pk_dept_exec=:pkDeptEx where pk_cnord=:pkCnord ";
        String opDtSql = "update bl_op_dt set PK_DEPT_PHARMACY=pk_dept_ex, pk_dept_ex=:pkDeptEx where pk_pres=:pkPres ";
        for (String pkPres : mapPres.keySet()) {
            for (BlOpDt opdt : mapPres.get(pkPres)) {
                //if ("1".equals(opdt.getFlagPd())) {
                    opdtList.add(opdt);
                //}
            }
        }
        if (opdtList != null && opdtList.size() > 0) {
            DataBaseHelper.batchUpdate(opDtSql, opdtList);
            DataBaseHelper.batchUpdate(ordSql, opdtList);
        }
        return mapPres;
    }

    /**
     * 窗口分窗匹配规则
     * @param args
     */
    public void getWinnoForAllRolue(Object... args){
        String isShowWinno = ApplicationUtils.getSysparam("EX0079",false);
        if("1".equals(isShowWinno) && args.length>1 && CommonUtils.isNotNull(args[1])){
            Map<String, List<BlOpDt>> mapPres=(Map<String, List<BlOpDt>>)args[1];
            getWinnoForUpdate((List<ExPresOcc>)args[0],mapPres);
        }else{
            getWinno((List<ExPresOcc>)args[0]);
        }

    }


    /**
     * 设置分窗规则，如果开启EX0079 则优先取收费结算录入时获取的窗口号
     * @param epoDaos 处方执行单
     * @param mapPres 门诊收费结算
     */
    private void getWinnoForUpdate(List<ExPresOcc> epoDaos,Map<String, List<BlOpDt>> mapPres){
        List<ExPresOcc> pres_templist=new ArrayList<>();
        List<ExPresOcc> presOccList=new ArrayList<>();
        for (ExPresOcc exocc :epoDaos){
            List<BlOpDt> cgDtList=mapPres.get(exocc.getPkPres());
            cgDtList=cgDtList.stream().filter(m->CommonUtils.isNotNull(m.getWinnoConf()) && CommonUtils.isNotNull(m.getWinnoPrep())).collect(Collectors.toList());
            if(cgDtList==null ||cgDtList.size()==0){
                pres_templist.add(exocc);
            }else{
                exocc.setWinnoPrep(cgDtList.get(0).getWinnoPrep());
                exocc.setWinnoConf(cgDtList.get(0).getWinnoConf());
                presOccList.add(exocc);
            }
        }
        if(pres_templist.size()>0){
            getWinno(pres_templist);
        }
        if(presOccList.size()>0){
            DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExPresOcc.class),presOccList);
        }
    }

    /**
     * 窗口分配
     * @param epoDaos
     */
    private void getWinno(List<ExPresOcc> epoDaos){
        if(epoDaos==null||epoDaos.size()==0)return;

        List<String> pkPresList=new ArrayList<>();
        epoDaos.forEach(m->{pkPresList.add(m.getPkPres());});
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("pkPresList",pkPresList);

        List<DrugPresUsecateVo> useCateList = zsrmOpWinnoMapper.getPresUsecate(pkPresList);
        //if(useCateList==null ||useCateList.size()==0)return;

        List<PressAttInfo> presAttList=zsrmOpWinnoMapper.getPresAtt(pkPresList);

        String sql="select pk_pres,PK_DEPT_AREAAPP from bl_op_dt where pk_pres in (" + CommonUtils.convertListToSqlInPart(pkPresList) + ") group by pk_pres,PK_DEPT_AREAAPP";
        List<Map<String,Object>> areaDeptList=DataBaseHelper.queryForList(sql,new Object[]{});

        for (ExPresOcc exocc :epoDaos){
            Map<String,Object> paramWin=new HashMap<>();
            paramWin.put("pkDeptEx",exocc.getPkDeptEx());
            paramWin.put("pkDeptAp",exocc.getPkDeptPres());
            List<Map<String,Object>> areaDept_temp=areaDeptList.stream().filter(m->exocc.getPkPres().equals(m.get("pkPres")) && CommonUtils.isNotNull(m.get("pkDeptAreaapp"))).collect(Collectors.toList());
            if(areaDept_temp!=null && areaDept_temp.size()>0){
                paramWin.put("pkDeptAreaapp",areaDept_temp.get(0).get("pkDeptAreaapp"));
            }
            List<DrugPresUsecateVo> temp_useList=useCateList.stream().filter(m->exocc.getPkPres().equals(m.getPkPres())).collect(Collectors.toList());
            if(temp_useList==null ||temp_useList.size()==0) {
                Map<String, Object> deptMap = getWinNo(paramWin, exocc.getPkPres(), presAttList);
                exocc.setWinnoConf(MapUtils.getString(deptMap, "winnoConf"));
                exocc.setWinnoPrep(MapUtils.getString(deptMap, "winnoPrep"));
            }else {
                temp_useList.sort(Comparator.comparing(DrugPresUsecateVo::getOrdsn));
                String euUsecate=temp_useList.get(0).getEuUsecate();
                paramWin.put("euUsecate",euUsecate);
                Map<String,Object> resWin=getWinNo(paramWin,exocc.getPkPres(),presAttList);
                exocc.setWinnoConf(MapUtils.getString(resWin,"winnoConf"));
                exocc.setWinnoPrep(MapUtils.getString(resWin,"winnoPrep"));
            }
        }

        DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExPresOcc.class),epoDaos);
    }

    /**
     * 获取待缴费 费用明细
     * @param notSettleVOS
     * @return
     */
    public Object getNotSettlePresInfo(List<BlPatiCgInfoNotSettleVO> notSettleVos) {
        if (notSettleVos == null || notSettleVos.size() == 0) return null;
        try {
            List<String> pkPresList = new ArrayList<>();
            notSettleVos.forEach(m -> {
                if (CommonUtils.isNotNull(m.getPkPres()) && "1".equals(m.getFlagPd())) {
                    pkPresList.add(m.getPkPres());
                }
            });

            if (pkPresList == null || pkPresList.size() == 0) return null;

            List<DrugPresUsecateVo> useCateList = zsrmOpWinnoMapper.getPresUsecate(pkPresList);

            logger.error("原始"+JsonUtil.writeValueAsString(notSettleVos));
            getNotSettleItemDeptExByBus(notSettleVos);
            logger.error("业务线"+JsonUtil.writeValueAsString(notSettleVos));
            getNotSettleForDeptExByDef(notSettleVos, useCateList);
            logger.error("特殊"+JsonUtil.writeValueAsString(notSettleVos));
            getNotSettleWinno(notSettleVos, useCateList);
            logger.error("分窗"+JsonUtil.writeValueAsString(notSettleVos));

        }catch (Exception e){
            logger.error("药品分窗结算时调用异常："+e.getMessage());
        }
        return notSettleVos;
    }

    /**
     * 获取药房窗口号
     * @param notSettleVos
     * @return
     */
    private List<BlPatiCgInfoNotSettleVO> getNotSettleWinno(List<BlPatiCgInfoNotSettleVO> notSettleVos,List<DrugPresUsecateVo> useCateList){
        List<String> pkPresList=new ArrayList<>();
        notSettleVos.forEach(m->{
            if(CommonUtils.isNotNull(m.getPkPres()) && "1".equals(m.getFlagPd())){
                pkPresList.add(m.getPkPres());
            }
        });

        List<PressAttInfo> pressAttInfos=zsrmOpWinnoMapper.getOpCgdtAtt(pkPresList);

        Map<String,List<BlPatiCgInfoNotSettleVO>> notsetvoGroup=notSettleVos.stream().
                filter(m->CommonUtils.isNotNull(m.getPkPres()) && "1".equals(m.getFlagPd())).
                collect(Collectors.groupingBy(BlPatiCgInfoNotSettleVO::getPkPres));

        for(String  temp_pres :notsetvoGroup.keySet()){
            BlPatiCgInfoNotSettleVO notsetvo=notsetvoGroup.get(temp_pres).get(0);
            List<DrugPresUsecateVo> temp_useList= new ArrayList<>();
            if(useCateList!=null && useCateList.size()>0) {
                temp_useList= useCateList.stream().filter(m -> temp_pres.equals(m.getPkPres())).collect(Collectors.toList());
            }
            Map<String, Object> paramWin = new HashMap<>();
            paramWin.put("pkDeptEx", notsetvo.getPkDeptEx());
            paramWin.put("pkDeptAp", notsetvo.getPkDeptApp());
            paramWin.put("pkDeptAreaapp",notsetvo.getPkDeptAreaapp());
            if(temp_useList==null ||temp_useList.size()==0){
                Map<String,Object> deptMap=getWinNo(paramWin,notsetvo.getPkPres(),pressAttInfos);
                notSettleVos.forEach(m->{
                        if(temp_pres.equals(m.getPkPres())){
                            m.setWinnoConf(MapUtils.getString(deptMap, "winnoConf"));
                            m.setWinnoPrep(MapUtils.getString(deptMap, "winnoPrep"));
                        }
                });
            }else {
                temp_useList.sort(Comparator.comparing(DrugPresUsecateVo::getOrdsn));
                String euUsecate = temp_useList.get(0).getEuUsecate();
                paramWin.put("euUsecate", euUsecate);
                Map<String, Object> resWin = getWinNo(paramWin,notsetvo.getPkPres(),pressAttInfos);
                notSettleVos.forEach(m->{
                    if(temp_pres.equals(m.getPkPres())){
                        m.setWinnoConf(MapUtils.getString(resWin, "winnoConf"));
                        m.setWinnoPrep(MapUtils.getString(resWin, "winnoPrep"));
                    }
                });
            }
        }
        return notSettleVos;
    }

    /**
     * 根据业务线获取执行科室
     * @param notSettleVos
     * @return
     */
    private List<BlPatiCgInfoNotSettleVO> getNotSettleItemDeptExByBus(List<BlPatiCgInfoNotSettleVO> notSettleVos) {
        Map<String, Object> deptPres = new HashMap<String, Object>();
        //如果药品处方的执行科室对应所有执行科室的药房无在线窗口则通过业务线重新获取执行科室
        Set<String> pkList = new HashSet<>();
        notSettleVos.forEach(m -> {
            if (CommonUtils.isNotNull(m.getPkPres()) && "1".equals(m.getFlagPd())) {
                pkList.add(m.getPkCgop());
            }
        });

        if (pkList == null || pkList.size() == 0) return notSettleVos;

        Map<Map<String, Object>, String> deptMap = new HashMap<>();
        //查询开立科室和执行科室
        StringBuffer sbfSql = new StringBuffer("");
        sbfSql.append(" select pk_dept_app,pk_dept_ex,PK_DEPT_AREAAPP from bl_op_dt where PK_CGOP in (" + CommonUtils.convertSetToSqlInPart(pkList, "pk_cgop") + ")  and flag_pd='1' group by pk_dept_app,pk_dept_ex,pk_dept_areaapp");
        List<Map<String, Object>> pkExList = DataBaseHelper.queryForList(sbfSql.toString(), new Object[]{});

        for (Map<String, Object> map : pkExList) {
            String pkDeptEx=MapUtils.getString(map,"pkDeptEx");
            String pkDeptApp=MapUtils.getString(map,"pkDeptApp");
            String pkDeptAreApp=MapUtils.getString(map,"pkDeptAreaapp");
            String isExist = isExistWinOnline(pkDeptEx ,pkDeptApp,pkDeptAreApp);
            //获取处方的开立科室和执行科室
            Map<String, Object> deMap = new HashMap<>();
            deMap.put(pkDeptApp, map);
            deptMap.put(deMap, isExist);
        }

        //获取不在线得药房窗口，构建参数获取业务线
        notSettleVos.forEach(m -> {
            for (Map<String, Object> mapKey : deptMap.keySet()) {
                Map<String,Object> map_temp= (Map<String, Object>) mapKey.get(m.getPkDeptApp());
                if (m.getPkDeptEx().equals(map_temp.get("pkDeptEx")) && "0".equals(deptMap.get(mapKey))) {
                    List<String> pkDept_temp_list=new ArrayList<>();
                    pkDept_temp_list.add(m.getPkDeptApp());
                    pkDept_temp_list.add(MapUtils.getString(map_temp,"pkDeptAreaapp"));
                    deptPres.put(m.getPkPres(), pkDept_temp_list);
                }
            }
        });


        if (deptPres != null && deptPres.size() > 0) {
            for (String pkPres : deptPres.keySet()) {
                Map<String, Object> mapParam = new HashMap<String, Object>();
                List<String> pkDept_res_list= (List<String>) deptPres.get(pkPres);
                mapParam.put("pkDept",pkDept_res_list.get(0) );
                mapParam.put("dtDepttype", "0402");
                mapParam.put("dtButype", "06");
                mapParam.put("pkDeptArea",pkDept_res_list.get(1));//诊区
                Map<String, Object> durgStore = getLinesBusiness(mapParam);
                String deptEx = CommonUtils.getString(durgStore.get("pkDept"), "");
                String sql="select code_dept,name_dept from bd_ou_dept where pk_dept=?";
                Map<String,Object> deptMap_temp=DataBaseHelper.queryForMap(sql,new Object[]{deptEx});
                notSettleVos.forEach(m ->
                {
                    if (pkPres.equals(m.getPkPres())) {
                        m.setPkDeptEx(deptEx);
                        m.setNameDeptEx(MapUtils.getString(deptMap_temp,"nameDept"));
                    }
                });
            }
        }
        return notSettleVos;
    }

    /**
     * 获取待缴费 费用明细 用法分类
     * @param notSettleVos
     * @param useCateList
     * @return
     */
    private List<BlPatiCgInfoNotSettleVO>  getNotSettleForDeptExByDef(List<BlPatiCgInfoNotSettleVO> notSettleVos,List<DrugPresUsecateVo> useCateList){
        //**机构级CN0103 诊区/科室参数控制 1：诊区；0：科室
        String areaApSysParam=ApplicationUtils.getSysparam("CN0103",false);
        Set<String> pkPresSets=new HashSet<>();
        notSettleVos.forEach(m->{ if(CommonUtils.isNotNull(m.getPkPres())){pkPresSets.add(m.getPkPres());}});

        Map<String,List<DrugPresUsecateVo>> cateMapByPres=new HashMap<>();
        if(useCateList!=null && useCateList.size()>0) {
            cateMapByPres = useCateList.stream().collect(Collectors.groupingBy(DrugPresUsecateVo::getPkPres));
        }

        Map<String,Object> paramUse=new HashMap<>();
        paramUse.put("areaSysparam",areaApSysParam);
        paramUse.put("pkOrg", UserContext.getUser().getPkOrg());
        List<Map<String,Object>> deptExList=zsrmOpWinnoMapper.getDeptExByUsecate(paramUse);

        List<PressAttInfo> pressAttInfoList=zsrmOpWinnoMapper.getOpCgdtAtt(new ArrayList<>(pkPresSets));

        if(deptExList==null || deptExList.size()==0)return notSettleVos;
        for (String pkPres:pkPresSets){

            List<BlPatiCgInfoNotSettleVO> notSettleItem_temp=notSettleVos.stream().filter(m->pkPres.equals(m.getPkPres())).collect(Collectors.toList());
            if(notSettleItem_temp==null||notSettleItem_temp.size()==0)continue;

            String pkDeptAp="";
            if("0".equals(areaApSysParam)){//诊区
                pkDeptAp= notSettleItem_temp.get(0).getPkDeptApp();
            }else{
                pkDeptAp= notSettleItem_temp.get(0).getPkDeptAreaapp();
            }
            String pkDept=notSettleItem_temp.get(0).getPkDeptEx();

            List<DrugPresUsecateVo> tempList=cateMapByPres.get(pkPres);

            Map<String,Object> deptMap=new HashMap<>();
            deptMap=setWinnoProRule(pkDeptAp,pkDept,pkPres,pressAttInfoList,deptExList,tempList);
            String pkDeptEx=MapUtils.getString(deptMap,"pkDept");
            String nameDeptEx=MapUtils.getString(deptMap,"nameDeptEx");

            notSettleVos.stream().forEach(m->{
                if(pkPres.equals(m.getPkPres()) && CommonUtils.isNotNull(pkDeptEx) && CommonUtils.isNotNull(nameDeptEx)){
                    m.setPkDeptEx(pkDeptEx);
                    m.setNameDeptEx(nameDeptEx);
                }
            });
        }
        return notSettleVos;
    }


    /**
     * 根据参数EX0030获取窗口分配方式
     * @param paraMap {"pkDeptEx":"发药药房","pkDeptAp":"开立科室","euUsecate":"用法分类"}
     * @return {"winnoConf":"发药窗口","winnoPrep":"配药窗口"}
     */
     private Map<String,Object> getWinNo(Map<String,Object> paramMap,String pkPres,List<PressAttInfo> presAttinfo) {
        String winType = ApplicationUtils.getDeptSysparam("EX0030", MapUtils.getString(paramMap,"pkDeptEx"));//门诊药房窗口分配方式：0 按业务量，1 按开立科室
        String giveType=ApplicationUtils.getDeptSysparam("EX0001",  MapUtils.getString(paramMap,"pkDeptEx"));//门诊药房发配药模式：1-配药发药模式，2-直接发药模式

        if(CommonUtils.isEmptyString(winType)){
            throw new BusException("请维护系统参数【EX0030】！");
        }

        if(CommonUtils.isEmptyString(giveType)){
            throw new BusException("请维护系统参数【EX0001】！");
        }

         List<Map<String,Object>> resMapList=new ArrayList<>();
        if (EnumerateParameter.ZERO.equals(winType)) {//按业务量
            //查询发药/配药窗口
            StringBuffer cntSql=new StringBuffer();
            cntSql.append("select count(1) cnt, du.code  winno_conf, dul.code winno_prep,du.eu_usecate");
            cntSql.append(" from bd_dept_unit du");
            cntSql.append(" left outer join ex_pres_occ pres on du.pk_dept = pres.pk_dept_ex and");
            cntSql.append(" du.code = pres.winno_conf and pres.flag_reg = '1' and  pres.flag_conf = '0' and pres.flag_canc = '0'");
            cntSql.append(" left outer join bd_dept_unit dul on du.pk_deptunit_rl = dul.pk_deptunit");
            cntSql.append(" where du.pk_dept =:pkDeptEx ");
            cntSql.append(" and du.eu_unittype='1' and du.eu_butype='1' and du.flag_online='1' and du.del_flag='0'");
            cntSql.append(" group by du.code, dul.code,du.eu_usecate order by count(1)");
            resMapList= DataBaseHelper.queryForList(cntSql.toString(), paramMap);
        }else if(EnumerateParameter.ONE.equals(winType)){//按开立科室
            StringBuffer deptSql=new StringBuffer();
            deptSql.append("SELECT  count(1), du.code  winno_conf,  dul.code winno_prep,du.eu_usecate");
            deptSql.append(" FROM bd_dept_unit du");
            deptSql.append(" INNER JOIN bd_dept_unit_obj ob ON du.pk_deptunit = ob.pk_deptunit");
            deptSql.append(" left JOIN ex_pres_occ pres ON du.pk_dept = pres.pk_dept_ex AND");
            deptSql.append(" du.code = pres.winno_conf AND pres.flag_reg = '1' AND pres.flag_conf = '0' AND  pres.flag_canc = '0'");
            deptSql.append(" LEFT OUTER JOIN bd_dept_unit dul ON du.pk_deptunit_rl = dul.pk_deptunit");
            deptSql.append(" WHERE du.pk_dept =:pkDeptEx AND du.eu_unittype='1' AND ");
            deptSql.append(" du.eu_butype='1' AND du.flag_online='1' AND du.del_flag='0' AND");
            deptSql.append(" ob.eu_objtype='0' AND ob.pk_dept=:pkDeptAp");
            deptSql.append(" group by du.code, dul.code,du.eu_usecate order by count(1)");
            //查询发药/配药窗口
            resMapList = DataBaseHelper.queryForList(deptSql.toString(), paramMap);
        }else{//诊区
            StringBuffer deptSql=new StringBuffer();
            deptSql.append("SELECT  count(1), du.code  winno_conf,  dul.code winno_prep,du.eu_usecate");
            deptSql.append(" FROM bd_dept_unit du");
            deptSql.append(" INNER JOIN bd_dept_unit_obj ob ON du.pk_deptunit = ob.pk_deptunit");
            deptSql.append(" left JOIN ex_pres_occ pres ON du.pk_dept = pres.pk_dept_ex AND");
            deptSql.append(" du.code = pres.winno_conf AND pres.flag_reg = '1' AND pres.flag_conf = '0' AND  pres.flag_canc = '0'");
            deptSql.append(" LEFT OUTER JOIN bd_dept_unit dul ON du.pk_deptunit_rl = dul.pk_deptunit");
            deptSql.append(" WHERE du.pk_dept =:pkDeptEx AND du.eu_unittype='1' AND ");
            deptSql.append(" du.eu_butype='1' AND du.flag_online='1' AND du.del_flag='0' AND");
            deptSql.append(" ob.eu_objtype='1' AND ob.pk_dept=:pkDeptAreaapp");
            deptSql.append(" group by du.code, dul.code,du.eu_usecate order by count(1)");
            //查询发药/配药窗口
            resMapList = DataBaseHelper.queryForList(deptSql.toString(), paramMap);
        }
         String deptSql="select name_dept from bd_ou_dept where pk_dept=?";
         Map<String,Object> deptMap=DataBaseHelper.queryForMap(deptSql,new Object[]{MapUtils.getString(paramMap,"pkDeptEx")});

         Map<String,Object> resMap=new HashMap<String,Object>();
        if(resMapList==null || resMapList.size()==0){
            throw new BusException("科室【"+MapUtils.getString(deptMap,"nameDept")+"】,【EX0030="+winType+"】,未获取到有效在线窗口！");
        }else{
            String euUsecate=MapUtils.getString(paramMap,"euUsecate");
            if(CommonUtils.isNotNull(euUsecate)) {
                List<Map<String, Object>> winList_temp = resMapList.stream().filter(m ->euUsecate.equals(m.get("euUsecate"))).collect(Collectors.toList());
                if(winList_temp==null || winList_temp.size()==0){
                    resMap=getWinnoForPresAtt(resMapList,pkPres,presAttinfo,null);
                }else{
                    resMap=winList_temp.get(0);
                    resMap=getWinnoForPresAtt(resMapList,pkPres,presAttinfo,euUsecate);
                }
            }else{
                resMap=getWinnoForPresAtt(resMapList,pkPres,presAttinfo,null);
            }
        }

        if(EnumerateParameter.ONE.equals(giveType)){//配药发药
            if(!CommonUtils.isNotNull(resMap.get("winnoConf"))){
                throw new BusException("【科室【"+MapUtils.getString(deptMap,"nameDept")+"】,EX0001】配药发药时，未找到在线发药窗口，请维护！");
            }else if(!CommonUtils.isNotNull(resMap.get("winnoPrep"))){
                throw new BusException("【科室【"+MapUtils.getString(deptMap,"nameDept")+"】,【EX0001】配药发药时，未找到发药窗口【"+resMap.get("winnoConf")+"】关联在线配药窗口，请维护！");
            }
        }else{//直接发药
            if(!CommonUtils.isNotNull(resMap.get("winnoConf"))){
                throw new BusException("【科室【"+MapUtils.getString(deptMap,"nameDept")+"】,【EX0001】直接发药时，未找到在线发药窗口，请维护！");
            }
        }
        return resMap;
    }

    /**
     * 单独对7号窗口做分配
     * @param winnoMapList
     * @param pkPres
     * @param pressAttInfoList
     * @return
     */
    private Map<String,Object> getWinnoForPresAtt(List<Map<String,Object>> winnoMapList,String pkPres,List<PressAttInfo> pressAttInfoList,String euUsecate) {
        if (pressAttInfoList == null || pressAttInfoList.size() == 0) return winnoMapList.get(0);
       // List<PressAttInfo> presAll = pressAttInfoList.stream().filter(m -> pkPres.equals(m.getPkPres())).collect(Collectors.toList());
        List<PressAttInfo> presAll = pressAttInfoList;
        /**获取结算中处方是上机标志的药品以及可以整包的药品处方信息*/
        List<PressAttInfo> pres_temp = pressAttInfoList.stream().filter(m ->m.getIsQuan().doubleValue() == 0 && m.getAttVal() == 1).collect(Collectors.toList());
        /**根据规则判断在线窗口是否存在7号窗口*/
        List<Map<String, Object>> existNum7Info = winnoMapList.stream().filter(m -> "7".equals(MapUtils.getString(m, "winnoConf"))).collect(Collectors.toList());
        /**获取同一处方中药品是否存在有指定特定窗口号的处方*/
        List<PressAttInfo> samePresWins=pressAttInfoList.stream().filter(m->pkPres.equals(m.getPkPres()) && CommonUtils.isNotNull(m.getAttWin())).collect(Collectors.toList());

        Map<String,Object> resMap=new HashMap<>();
        if(presAll!=null && presAll.size()>0 && pres_temp!=null &&pres_temp.size()>0
                && presAll.size()==pres_temp.size()
                && existNum7Info!=null && existNum7Info.size()>0){
            resMap= existNum7Info.get(0);
        }else{
            List<Map<String,Object>> temp_win_res=new ArrayList<>();
            List<Map<String, Object>> notWinNum7Info = winnoMapList.stream().filter(m -> !"7".equals(MapUtils.getString(m, "winnoConf"))).collect(Collectors.toList());
            if(notWinNum7Info!=null && notWinNum7Info.size()>0){
                /**用法分类*/
                List<Map<String, Object>> euUsecateList=notWinNum7Info.stream().filter(m->CommonUtils.isNotNull(euUsecate) && euUsecate.equals(MapUtils.getString(m,"euUsecate"))).collect(Collectors.toList());
                if(euUsecateList!=null &&euUsecateList.size()>0){
                    temp_win_res= euUsecateList;
                }else{
                    temp_win_res=notWinNum7Info;
                }
            }else{
                /**用法分类*/
                List<Map<String, Object>> euUsecateList=winnoMapList.stream().filter(m->CommonUtils.isNotNull(euUsecate) && euUsecate.equals(MapUtils.getString(m,"euUsecate"))).collect(Collectors.toList());
                if(euUsecateList!=null && euUsecateList.size()>0){
                    temp_win_res=euUsecateList;
                }else{
                    temp_win_res=winnoMapList;
                }
            }
            if(temp_win_res!=null && temp_win_res.size()>0){
                List<Map<String,Object>> noNinthlist=temp_win_res.stream().filter(m->!"9".equals(m.get("winnoConf"))).collect(Collectors.toList());
                if(noNinthlist!=null && noNinthlist.size()>0){
                    resMap=noNinthlist.get(0);
                }else{
                    resMap=temp_win_res.get(0);
                }
            }
        }

        if(samePresWins!=null && samePresWins.size()>0){
            for(Map<String,Object> m:winnoMapList){
                String winnoConf=MapUtils.getString(m,"winnoConf");
                List<PressAttInfo> preswin_temp=samePresWins.stream().filter(j->winnoConf.equals(j.getAttWin())).collect(Collectors.toList());
                if(preswin_temp!=null && preswin_temp.size()>0){
                    resMap= m;
                    break;
                }
            }
        }

        return resMap;
    }

    /**
     * 根据业务线变更处方执行科室
     * @param mapPres
     * @return
     */
    private Map<String,List<BlOpDt>> getPresDeptByBus(Map<String, List<BlOpDt>> mapPres){
        Map<String,Object> deptPres=new HashMap<String,Object>();
        //如果药品处方的执行科室对应所有执行科室的药房无在线窗口则通过业务线重新获取执行科室
        Set<String> pkList=new HashSet<>();
        for (List<BlOpDt> opdtList :mapPres.values()) {
            opdtList.forEach(m->{
                pkList.add(m.getPkCgop());
            });
        }

        Map<Map<String,Object>,String> deptMap=new HashMap<>();
        //查询开立科室和执行科室
        StringBuffer sbfSql = new StringBuffer("");
        sbfSql.append(" select pk_dept_app,pk_dept_ex,pk_dept_areaapp from bl_op_dt where PK_CGOP in (" + CommonUtils.convertSetToSqlInPart(pkList, "pk_cgop") + ") and flag_pd='1' group by pk_dept_app,pk_dept_ex,pk_dept_areaapp");
        List<Map<String,Object>> pkExList = DataBaseHelper.queryForList(sbfSql.toString(), new Object[] {});

        for (Map<String,Object> map :pkExList){
            String pkDeptEx=MapUtils.getString(map,"pkDeptEx");
            String pkDeptApp=MapUtils.getString(map,"pkDeptApp");
            String pkDeptAreApp=MapUtils.getString(map,"pkDeptAreaapp");
            String isExist = isExistWinOnline(pkDeptEx, pkDeptApp,pkDeptAreApp);
            //获取处方的开立科室和执行科室
            Map<String,Object> deMap = new HashMap<>();
            deMap.put(pkDeptApp, map);
            deptMap.put(deMap, isExist);
        }

        for (List<BlOpDt> opdtList :mapPres.values()) {
            opdtList.forEach(m->{
                for(Map<String,Object> mapKey:deptMap.keySet()){
                    Map<String,Object> map_temp= (Map<String, Object>) mapKey.get(m.getPkDeptApp());
                    if(m.getPkDeptEx().equals(map_temp.get("pkDeptEx")) && "0".equals(deptMap.get(mapKey))){
                        List<String> pkDept_temp_list=new ArrayList<>();
                        pkDept_temp_list.add(m.getPkDeptApp());
                        pkDept_temp_list.add(MapUtils.getString(map_temp,"pkDeptAreaapp"));
                        deptPres.put(m.getPkPres(), pkDept_temp_list);
                    }
                }
            });
        }

        if(deptPres!=null && deptPres.size()>0) {
            for(String pkPres : deptPres.keySet()){
                Map<String,Object> mapParam=new HashMap<String,Object>();
                List<String> pkDept_res_list= (List<String>) deptPres.get(pkPres);
                mapParam.put("pkDept",pkDept_res_list.get(0) );
                mapParam.put("dtDepttype", "0402");
                mapParam.put("dtButype", "06");
                mapParam.put("pkDeptArea",pkDept_res_list.get(1));//诊区
                Map<String,Object> durgStore = getLinesBusiness(mapParam);
                String deptEx= CommonUtils.getString(durgStore.get("pkDept"),"");
                for (List<BlOpDt> opdtList :mapPres.values()) {
                    opdtList.forEach(m ->
                    {
                        if (pkPres.equals(m.getPkPres()) && "1".equals(m.getFlagPd())) {
                            m.setPkDeptEx(deptEx);
                        }
                    });
                }
            }
        }
        return mapPres;
    }

    /**
     * 根据用法分类获取执行科室
     * @param mapPres
     * @param useCateList
     * @return
     */
    private Map<String, List<BlOpDt>> getDeptByUsecate(Map<String, List<BlOpDt>> mapPres,List<DrugPresUsecateVo> useCateList){
        //**机构级CN0103 诊区/科室参数控制 1：诊区；0：科室
        String areaApSysParam=ApplicationUtils.getSysparam("CN0103",false);

        Map<String,List<DrugPresUsecateVo>> cateMapByPres=new HashMap<>();
        if(useCateList!=null && useCateList.size()>0) {
            cateMapByPres = useCateList.stream().collect(Collectors.groupingBy(DrugPresUsecateVo::getPkPres));
        }

        List<PressAttInfo> pressAttInfoList=zsrmOpWinnoMapper.getOpCgdtAtt(new ArrayList<>(mapPres.keySet()));

        Map<String,Object> paramUse=new HashMap<>();
        paramUse.put("areaSysparam",areaApSysParam);
        paramUse.put("pkOrg",UserContext.getUser().getPkOrg());
        List<Map<String,Object>> deptExList=zsrmOpWinnoMapper.getDeptExByUsecate(paramUse);

        for (String pkPres:mapPres.keySet()){
            List<BlOpDt> opdtList_temp=mapPres.get(pkPres);
            if(opdtList_temp==null||opdtList_temp.size()==0)continue;

            String pkDeptAp="";
            if("0".equals(areaApSysParam)){//科室
                pkDeptAp=opdtList_temp.get(0).getPkDeptApp();
            }else{
                pkDeptAp=opdtList_temp.get(0).getPkDeptAreaapp();
            }
            String pkDept=opdtList_temp.get(0).getPkDeptEx();

            List<DrugPresUsecateVo> tempList=cateMapByPres.get(pkPres);

            Map<String,Object> deptMap=new HashMap<>();
            deptMap=setWinnoProRule(pkDeptAp,pkDept,pkPres,pressAttInfoList,deptExList,tempList);

            String pkDeptEx=MapUtils.getString(deptMap,"pkDept");
            String nameDeptEx=MapUtils.getString(deptMap,"nameDeptEx");

            opdtList_temp.stream().forEach(m-> {
                if(CommonUtils.isNotNull(pkDeptEx)) {
                    m.setPkDeptEx(pkDeptEx);
                }
            });

            mapPres.put(pkPres,opdtList_temp);
        }

        return mapPres;
    }

    /**
     * 通过窗口特殊属性控制发药执行科室
     * @param pkDeptAp 申请科室、诊区
     * @param pkDept 原始执行科室
     * @param pkPres 处方主键
     * @param pressAttInfoList 特殊属性处方明细
     * @param deptExList 所有执行科室汇总集合（中药，西药，成药）
     * @param tempList 用法分类限定，包含处方类型限定
     * @return
     */
    private Map<String,Object> setWinnoProRule(String pkDeptAp,String pkDept,String pkPres, List<PressAttInfo> pressAttInfoList, List<Map<String,Object>> deptExList, List<DrugPresUsecateVo> tempList) {
        /**处方中包含0519附加属性处方*/
        List<Map<String, Object>> sameWinnoChk = deptExList.stream().filter(m -> CommonUtils.isNotNull(MapUtils.getString(m, "winnoConf")) && pkDeptAp.equals(MapUtils.getString(m, "pkDeptAp"))
                        && pressAttInfoList.stream().filter(j -> pkPres.equals(j.getPkPres()) && CommonUtils.isNotNull(j.getAttWin())
                        && MapUtils.getString(m, "winnoConf").equals(j.getAttWin())
                ).findAny().isPresent()
        ).collect(Collectors.toList());

        /**此次结算所有处方明细药品包含上机标志，且为整数倍走7号窗口*/
        List<Map<String, Object>> number7Winno = deptExList.stream().filter(m -> "7".equals(MapUtils.getString(m, "winnoConf")) && pkDeptAp.equals(MapUtils.getString(m, "pkDeptAp"))
                        && pressAttInfoList.stream().filter(j -> j.getIsQuan().doubleValue() == 0 && j.getAttVal() == 1 && pkPres.equals(j.getPkPres())
                ).findAny().isPresent()
        ).collect(Collectors.toList());


        /**处方类型 临购处方匹配1号窗*/
        List<Map<String, Object>>  retailPresForOne = deptExList.stream().filter(m ->  "1".equals(MapUtils.getString(m, "winnoConf"))
                        && tempList.stream().filter(j -> pkPres.equals(j.getPkPres()) &&  j.getDtPrestype().equals("15")
                ).findAny().isPresent()
        ).collect(Collectors.toList());

        /**匹配用法规则*/

        List<Map<String, Object>> usecateChkList = new ArrayList<>();
        boolean isHerbPres = tempList.stream().filter(m -> "02".equals(m.getDtPrestype())).findAny().isPresent();
        if (isHerbPres) {
            usecateChkList = deptExList.stream().filter(m -> "07".equals(MapUtils.getString(m, "dtButype"))).collect(Collectors.toList());
        } else {
            usecateChkList = deptExList.stream().filter(m -> "06".equals(MapUtils.getString(m, "dtButype"))).collect(Collectors.toList());
        }

        Map<String, Object> deptMap = new HashMap<>();
        if (usecateChkList != null && usecateChkList.size() > 0) {
            List<Map<String, Object>> dept_temp = usecateChkList.stream().
                    filter(m -> pkDeptAp.equals(MapUtils.getString(m, "pkDeptAp"))).collect(Collectors.toList());
            if (dept_temp != null && dept_temp.size() > 0) {
                deptMap = dept_temp.get(0);
            }

            // 科室、诊区+执行科室匹配
            dept_temp = usecateChkList.stream().
                    filter(m -> pkDeptAp.equals(MapUtils.getString(m, "pkDeptAp")) && pkDept.equals(MapUtils.getString(m, "pkDept"))).collect(Collectors.toList());
            if (dept_temp != null && dept_temp.size() > 0) {
                deptMap = dept_temp.get(0);
            }

            //TODO 用法匹配规则
            if (tempList != null && tempList.size() > 0) {
                List<DrugPresUsecateVo> usecateList = tempList.stream().filter(m -> CommonUtils.isNotNull(m.getEuUsecate())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(usecateList)) {
                    usecateList.sort(Comparator.comparing(DrugPresUsecateVo::getOrdsn));

                    String euUsecate = usecateList.stream().findFirst().get().getEuUsecate();
                    List<Map<String, Object>> usecate_temp = usecateChkList.stream().filter(m -> euUsecate.equals(MapUtils.getString(m, "euUsecate"))).collect(Collectors.toList());

                    dept_temp = usecate_temp.stream().filter(m -> pkDeptAp.equals(MapUtils.getString(m, "pkDeptAp"))).collect(Collectors.toList());
                    if (dept_temp != null && dept_temp.size() > 0) {
                        deptMap = dept_temp.get(0);
                    }

                    dept_temp = usecate_temp.stream().filter(m -> pkDeptAp.equals(MapUtils.getString(m, "pkDeptAp")) && pkDept.equals(MapUtils.getString(m, "pkDept"))).collect(Collectors.toList());
                    if (dept_temp != null && dept_temp.size() > 0) {
                        deptMap = dept_temp.get(0);
                    }

                    if (deptMap == null || deptMap.size() == 0) {
                        deptMap = dept_temp.get(0);
                    }
                }
            }
        }
        if (number7Winno != null && number7Winno.size() > 0) {
            deptMap = number7Winno.get(0);
        }

        if(retailPresForOne !=null && retailPresForOne.size()>0){
            deptMap=retailPresForOne.get(0);
        }

        if (sameWinnoChk != null && sameWinnoChk.size() > 0) {
            deptMap = sameWinnoChk.get(0);
        }
        return deptMap;
    }

    /**
     * 根据参数EX0030获取原执行科室是否存在在线窗口
     * @param paraMap {"pkDept":"发药药房","pkDeptPres":"开立科室","pkDeptAreaapp":"科室所属诊区"}
     * @return 1:存在在线窗口，0 没有在线窗口
     */
    private  String isExistWinOnline(String pkDept,String pkDeptPres,String pkDeptAreaapp) {
        String winType = ApplicationUtils.getDeptSysparam("EX0030", pkDept);//门诊药房窗口分配方式：0 按业务量，1 按开立科室

        if(CommonUtils.isEmptyString(winType)){
            throw new BusException("请维护系统参数【EX0030】！");
        }

        Map<String,Object> resMap=new HashMap<String,Object>();
        if (EnumerateParameter.ZERO.equals(winType)) {//按业务量
            //查询发药/配药窗口
            StringBuffer cntSql=new StringBuffer();
            cntSql.append("select count(1) cnt, du.code  winno_conf, dul.code winno_prep");
            cntSql.append(" from bd_dept_unit du");
            cntSql.append(" left outer join ex_pres_occ pres on du.pk_dept = pres.pk_dept_ex and");
            cntSql.append(" du.code = pres.winno_conf and pres.flag_reg = '1' and  pres.flag_conf = '0' and pres.flag_canc = '0'");
            cntSql.append(" left outer join bd_dept_unit dul on du.pk_deptunit_rl = dul.pk_deptunit");
            cntSql.append(" where du.pk_dept =? ");
            cntSql.append(" and du.eu_unittype='1' and du.eu_butype='1' and du.flag_online='1' and du.del_flag='0'");
            cntSql.append(" group by du.code, dul.code order by count(1)");
            List<Map<String,Object>> qryAdPortfolio = DataBaseHelper.queryForList(cntSql.toString(), new Object[]{pkDept});
            if (qryAdPortfolio!=null&&qryAdPortfolio.size()>0) {
                return "1";
            }else {
                return "0";
            }
        }else if(EnumerateParameter.ONE.equals(winType)){//按开立科室
            StringBuffer deptSql = new StringBuffer();
            deptSql.append("SELECT  count(1), du.code  winno_conf,  dul.code winno_prep");
            deptSql.append(" FROM bd_dept_unit du");
            deptSql.append(" INNER JOIN bd_dept_unit_obj ob ON du.pk_deptunit = ob.pk_deptunit");
            deptSql.append(" left JOIN ex_pres_occ pres ON du.pk_dept = pres.pk_dept_ex AND");
            deptSql.append(" du.code = pres.winno_conf AND pres.flag_reg = '1' AND pres.flag_conf = '0' AND  pres.flag_canc = '0'");
            deptSql.append(" LEFT OUTER JOIN bd_dept_unit dul ON du.pk_deptunit_rl = dul.pk_deptunit");
            deptSql.append(" WHERE du.pk_dept =? AND du.eu_unittype='1' AND ");
            deptSql.append(" du.eu_butype='1' AND du.flag_online='1' AND du.del_flag='0' AND");
            deptSql.append(" ob.eu_objtype='0' AND ob.pk_dept=?");
            deptSql.append(" group by du.code, dul.code order by count(1)");
            //查询发药/配药窗口
            List<Map<String, Object>> qryAdPkDept = DataBaseHelper.queryForList(deptSql.toString(), new Object[]{pkDept, pkDeptPres});
            if (qryAdPkDept != null && qryAdPkDept.size() > 0) {
                return "1";
            } else {
                return "0";
            }
        }else{
            StringBuffer deptSql = new StringBuffer();
            deptSql.append("SELECT  count(1), du.code  winno_conf,  dul.code winno_prep");
            deptSql.append(" FROM bd_dept_unit du");
            deptSql.append(" INNER JOIN bd_dept_unit_obj ob ON du.pk_deptunit = ob.pk_deptunit");
            deptSql.append(" left JOIN ex_pres_occ pres ON du.pk_dept = pres.pk_dept_ex AND");
            deptSql.append(" du.code = pres.winno_conf AND pres.flag_reg = '1' AND pres.flag_conf = '0' AND  pres.flag_canc = '0'");
            deptSql.append(" LEFT OUTER JOIN bd_dept_unit dul ON du.pk_deptunit_rl = dul.pk_deptunit");
            deptSql.append(" WHERE du.pk_dept =? AND du.eu_unittype='1' AND ");
            deptSql.append(" du.eu_butype='1' AND du.flag_online='1' AND du.del_flag='0' AND");
            deptSql.append(" ob.eu_objtype='1' AND ob.pk_dept=?");
            deptSql.append(" group by du.code, dul.code order by count(1)");
            //查询发药/配药窗口
            List<Map<String, Object>> qryAdPkDept = DataBaseHelper.queryForList(deptSql.toString(), new Object[]{pkDept,pkDeptAreaapp});
            if (qryAdPkDept != null && qryAdPkDept.size() > 0) {
                return "1";
            } else {
                return "0";
            }
        }
    }

    /**
     * 获取业务线
     * @param mapParam
     *            { "pkDept":"科室主键", "dtButype":"业务线类型编码","dtDepttype":"科室医疗类型"
     *            }
     * @return {"pkDept":"关联业务线科室"}
     */
    private Map<String, Object> getLinesBusiness(Map<String, Object> mapParam) {

        //**机构级CN0103 诊区/科室参数控制 0：科室；1：诊区
        String areaApSysParam=ApplicationUtils.getSysparam("CN0103",false);
        if (mapParam.get("pkDept") == null || mapParam.get("dtButype") == null || mapParam.get("dtDepttype") == null) {
            throw new BusException("参数不正确");
        }
        StringBuffer sql = new StringBuffer();
        List<Map<String, Object>> resultList=null;
        if("1".equals(areaApSysParam)){
            sql.append("select busa.pk_dept,busa.pk_org,busa.time_begin,busa.time_end,busa.FLAG_DEF from bd_dept_bus bus inner join bd_dept_bu bu on bus.pk_deptbu=bu.pk_deptbu ");
            sql.append(" inner join bd_dept_bus busa on bus.pk_deptbu=busa.pk_deptbu  where busa.dt_depttype=? and bu.dt_butype= ? and  bus.pk_dept=? ");
            sql.append(" and busa.del_flag = '0' and bu.del_flag = '0' and bus.del_flag = '0' and bus.DT_DEPTTYPE='16'  order by busa.sort ");

            resultList= DataBaseHelper.queryForList(sql.toString(), mapParam.get("dtDepttype"), mapParam.get("dtButype"), mapParam.get("pkDeptArea"));
        }else {
            sql.append("select busa.pk_dept,busa.pk_org,busa.time_begin,busa.time_end,busa.FLAG_DEF from bd_dept_bus bus inner join bd_dept_bu bu on bus.pk_deptbu=bu.pk_deptbu ");
            sql.append(" inner join bd_dept_bus busa on bus.pk_deptbu=busa.pk_deptbu  where busa.dt_depttype=? and bu.dt_butype= ? and  bus.pk_dept=? ");
            sql.append(" and busa.del_flag = '0' and bu.del_flag = '0' and bus.del_flag = '0' and bus.DT_DEPTTYPE='01'  order by busa.sort ");

            resultList= DataBaseHelper.queryForList(sql.toString(), mapParam.get("dtDepttype"), mapParam.get("dtButype"), mapParam.get("pkDept"));
        }

        if (resultList == null || resultList.size()==0) throw new BusException("此科室未在业务线维护其对应的科室");
        Map<String, Object> ret=new HashMap<String, Object>();
        boolean ifTrue=false;
        /** 获取药房增加执行时刻判断 */
        List<Map<String, Object>> res=new ArrayList<Map<String, Object>>();
        Date nowTime=new Date();
        for (Map<String, Object> result: resultList) {
            Date timeBegin = DateUtils.getDateMorning(nowTime,0);
            Date timeEnd = DateUtils.getDateMorning(nowTime,1);
            if(result.get("timeBegin")!=null ){
                timeBegin = DateUtils.strToDate(DateUtils.getDate() +" "+ result.get("timeBegin").toString(),"yyyy-MM-dd HH:mm:ss");
            }
            if(result.get("timeEnd")!=null){
                timeEnd = DateUtils.strToDate(DateUtils.getDate() +" "+ result.get("timeEnd").toString(),"yyyy-MM-dd HH:mm:ss") ;
            }
            if(DateUtils.getSecondBetween(timeEnd,timeBegin)>0 ){ //开始时间大于结束时间
                if(DateUtils.getHour(nowTime)>DateUtils.getHour(timeEnd))
                    timeEnd=DateUtils.getSpecifiedDay(timeEnd,1);
                else
                    timeBegin = DateUtils.getSpecifiedDay(timeBegin, -1);
            }

            if(DateUtils.getSecondBetween(timeBegin,nowTime)>=0 && DateUtils.getSecondBetween(nowTime,timeEnd)>=0){
                ret=result;
                res.add(result);
                if(result.get("flagDef")!=null && "1".equals(result.get("flagDef").toString())){
                    ifTrue=true;
                    String pkDeptAp=MapUtils.getString(mapParam,"pkDept");
                    String pkDeptEx=MapUtils.getString(result,"pkDept");
                    String pkDeptArea=MapUtils.getString(mapParam,"pkDeptArea");
                    String isOnline= isExistWinOnline(pkDeptEx,pkDeptAp,pkDeptArea);
                    if("1".equals(isOnline)) {
                        break;
                    }else{
                        ifTrue=false;
                        continue;
                    }
                }
            }
            String pkDeptAp=MapUtils.getString(mapParam,"pkDept");
            String pkDeptEx=MapUtils.getString(result,"pkDept");
            String pkDeptArea=MapUtils.getString(mapParam,"pkDeptArea");
            String isOnline= isExistWinOnline(pkDeptEx,pkDeptAp,pkDeptArea);
            if("1".equals(isOnline)) {
                ret=result;
                break;
            }
        }

        if(ret==null || ret.size()==0){
            if(res.size()==0) throw new BusException("此科室未在业务线维护当前时间其对应的科室");
            ret=res.get(0);
        }
        return ret;
    }
}
