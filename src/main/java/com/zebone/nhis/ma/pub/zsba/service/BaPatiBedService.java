package com.zebone.nhis.ma.pub.zsba.service;


import com.zebone.nhis.base.pub.service.BdResPubService;
import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.module.pv.PvBed;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.pi.service.PvStaffManagerService;
import com.zebone.nhis.ex.pub.service.ExPubService;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.PatiCardVo;
import com.zebone.nhis.labor.pub.vo.LabPatiCardVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;


/**
 * 博爱床位卡服务（含产房换床，产科婴儿换床）
 * @author leiboxun
 *
 */
@Service
public class BaPatiBedService {
    @Resource
    private ExPubService exPubService;
    @Resource
    private BdResPubService bdResPubService;
    @Resource
    private PvStaffManagerService pvStaffManagerService;




    /**
     * 母亲不在本病区的婴儿单独换床
     * @param param{bednoSrc,bednoDes,pkPv,pkDeptNs,pkPi,pkDept,pkBedDes,pkBedSrc,pkPvDes,pkPiDes}
     * @param user
     * @return
     */
    public PatiCardVo exChangeBedForLabor(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null ||"".equals(CommonUtils.getString(paramMap.get("pkPv")))||"".equals(CommonUtils.getString(paramMap.get("bednoDes")))
                ||"".equals(CommonUtils.getString(paramMap.get("bednoSrc")))){
            throw new BusException("换床患者或目标床位号为空，请确认后重新操作！");
        }
        User u = (User)user;
        String bedno_des = CommonUtils.getString(paramMap.get("bednoDes"));
        String pkbed_des = CommonUtils.getString(paramMap.get("pkBedDes"));
        String pk_dept = CommonUtils.getString(paramMap.get("pkDept"));
        String pk_dept_ns = CommonUtils.getString(paramMap.get("pkDeptNs"));
        String pkPi = CommonUtils.getString(paramMap.get("pkPi"));
        String pkPiDes = CommonUtils.getString(paramMap.get("pkPiDes"));
        String bedno_src = "";
        //床位模式-0手动分床模式 1-自动分成床模式
        String BD0013 = ApplicationUtils.getSysparam("BD0013",false);
        String pk_org = u.getPkOrg();
        //换床:更新原床位及就诊记录
        paramMap.put("dateEnd", new Date());
        paramMap.put("pkOrg", pk_org);
        paramMap.put("pkEmp",u.getPkEmp());
        paramMap.put("nameEmp",u.getNameEmp());
        Map<String,Object> flagOcupy = DataBaseHelper.queryForMap(" select flag_ocupy from bd_res_bed where code = ? and pk_org = ? " +
                                                                       " and pk_ward = ? ", new Object[]{bedno_des,pk_org,pk_dept_ns});
        String sql = "";
        //手动分床模式不处理婴儿床删除标志
        if(!"0".equals(BD0013)){
            sql = " ,flag_active = (case when '09' = dt_bedtype then '1' else flag_active end) ,del_flag = (case when '09' = dt_bedtype then '0' " +
                  " else del_flag end) ";
        }

        //根据婴儿主键查询占用床位下的其他婴儿
        String queSrcChdSql = " select pi.pk_bed_an,pv.pk_pv,pv.bed_no,pv.pk_pi,inf.sort_no,bed.pk_bed from BD_RES_BED bed " +
                              " inner join (select * from PV_INFANT where PK_PV = (select PK_PV from PV_INFANT where PK_PI_INFANT = ?)) inf " +
                              " on inf.pk_pi_infant = bed.pk_pi " +
                              " left join pv_ip pi on pi.pk_pv = inf.pk_pv_infant " +
                              " left join PV_ENCOUNTER pv on pv.pk_pi = inf.pk_pi_infant and pv.EU_STATUS = '1' " +
                              " where pv.PK_DEPT = ?";
        List<Map<String,Object>> mapSrc = DataBaseHelper.queryForList(queSrcChdSql,new Object[]{pkPi,pk_dept});


        String pkBedAnSrc ="";//原婴儿占床主键（真实的原床位）
        BdResBed bedSrc = new BdResBed();
        if(mapSrc.size() > 0){
            if(mapSrc.get(0).get("pkBedAn").toString() != null){
                pkBedAnSrc = mapSrc.get(0).get("pkBedAn").toString();
            }
        }
        //获取原床位
        if(!CommonUtils.isEmptyString(pkBedAnSrc)){
            bedSrc = DataBaseHelper.queryForBean("select * from BD_RES_BED where pk_bed = ?",BdResBed.class,pkBedAnSrc);
            bedno_src = bedSrc.getCode();
            paramMap.put("bednoSrc",bedSrc.getCode());
            paramMap.put("pkBedSrc",bedSrc.getPkBed());
        }

        if(mapSrc.size() > 0) {
            for (Map<String, Object> map : mapSrc) {
                if (map.get("pkBed").toString() != null) {//删除原床位自动生成的婴儿床位
                    DataBaseHelper.execute("delete from BD_RES_BED where pk_bed = ?",map.get("pkBed").toString());
                }
                if(!CommonUtils.isEmptyString(pkbed_des)){//将占床主键修改为目标床位主键
                    DataBaseHelper.update("update pv_ip set pk_bed_an = ? where pk_pv = ?",new Object[]{pkbed_des,map.get("pkPv")});
                }
            }
        }

        List<Map<String,Object>> mapDes = new ArrayList<>();
        //目标床位是否为空
        if(flagOcupy!=null&&"1".equals(CommonUtils.getString(flagOcupy.get("flagOcupy")))){
            //查询目标床位下是否有婴儿存在
            String queDesChdSql = " select pv.pk_pv,pv.bed_no,pv.pk_pi,inf.sort_no,bed.pk_bed from BD_RES_BED bed " +
                                  " left join (select * from PV_INFANT where PK_PI = ?) inf on inf.pk_pi_infant = bed.pk_pi " +
                                  " left join PV_ENCOUNTER pv on pv.pk_pi = inf.pk_pi_infant and pv.EU_STATUS = '1' " +
                                  " where pv.PK_DEPT = ?";
            mapDes = DataBaseHelper.queryForList(queDesChdSql,new Object[]{pkPiDes,pk_dept});

            DataBaseHelper.update("update pv_bed set date_end =:dateEnd,pk_emp_end =:pkEmp,name_emp_end =:nameEmp where pk_dept =:pkDept " +
                                       "and pk_dept_ns =:pkDeptNs and bedno =:bednoDes and pk_pv =:pkPvDes and date_end is null", paramMap);
            DataBaseHelper.update("update pv_encounter set bed_no = :bednoSrc where pk_pv = :pkPvDes", paramMap);

            int cnt = DataBaseHelper.update("update bd_res_bed set flag_ocupy = '1',pk_pi = :pkPiDes,pk_dept_used = :pkDeptNs,eu_status='02'"
                    + sql
                    + " where code = :bednoSrc and pk_org =:pkOrg and pk_ward=:pkDeptNs and pk_pi is null", paramMap);//互换|换空床皆可

            cnt = DataBaseHelper.update("update bd_res_bed set flag_ocupy = '0',pk_pi = null,pk_dept_used = null,eu_status='01'"
                    + sql
                    + " where code = :bednoDes and pk_org =:pkOrg and pk_ward=:pkDeptNs and flag_active='1'"
                    +(CommonUtils.isEmptyString(pkPiDes) ? " and pk_pi is null" : " and pk_pi = '"+pkPiDes+"'"), paramMap);

            if(cnt < 1)
                throw new BusException("目标床位信息已经发生变化，请先刷新！");
            //插入新的源床位记录
            saveSrcBed(paramMap,u);

            //删除目标床位自动生成的婴儿床位
            if(mapDes.size() > 0) {
                for (Map<String, Object> map : mapDes) {
                    if (map.get("pkBed").toString() != null) {
                        DataBaseHelper.execute("delete from BD_RES_BED where pk_bed = ?",map.get("pkBed").toString());
                    }
                }
                changedChdBed(mapDes, bedno_src, pk_dept_ns, u, "Des");
            }
        }

        changedChdBed(mapSrc, bedno_des, pk_dept_ns, u, "Src");

        //2019-02-25 更新责任护士
        Map<String,Object> staMap = new HashMap<String,Object>();
        if(!CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkEmpNsSrc")))){
            staMap.put("pkPv", CommonUtils.getString(paramMap.get("pkPv")));
            staMap.put("pkDept", CommonUtils.getString(paramMap.get("pkDept")));
            staMap.put("pkEmp", CommonUtils.getString(paramMap.get("pkEmpNsSrc")));
            staMap.put("nameEmp", CommonUtils.getString(paramMap.get("nameEmpNsSrc")));
            pvStaffManagerService.saveChangedStaff(staMap, u);
        }
        if(!CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkEmpNsDes")))){
            staMap.put("pkPv", CommonUtils.getString(paramMap.get("pkPvDes")));
            staMap.put("pkDept", CommonUtils.getString(paramMap.get("pkDeptDes")));
            staMap.put("pkEmp", CommonUtils.getString(paramMap.get("pkEmpNsDes")));
            staMap.put("nameEmp", CommonUtils.getString(paramMap.get("nameEmpNsDes")));
            pvStaffManagerService.saveChangedStaff(staMap, u);
        }

        //发送换床消息至集成平台
        paramMap.put("bedOld", CommonUtils.getString(paramMap.get("bednoSrc")));
        paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
        paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        PlatFormSendUtils.sendBedChangeMsg(paramMap);
        return null;
    }



    /**
     * 插入源床位就诊记录
     * @param paramMap
     * @param user
     */
    private void saveBed(Map<String,Object> paramMap,User user){
        PvBed bed = new PvBed();
        bed.setBedno(CommonUtils.getString(paramMap.get("bednoDes")));
        bed.setDateBegin(new Date());
        bed.setFlagMaj("1");
        bed.setNameEmpBegin(user.getNameEmp());
        bed.setPkEmpBegin(user.getPkEmp());
        BdResBed bedvo = exPubService.getBedInfoByPk(CommonUtils.getString(paramMap.get("pkBedDes")), user);
        if(bedvo!=null){
            bed.setPkBedWard(bedvo.getPkWard());
        }
        bed.setPkPv(CommonUtils.getString(paramMap.get("pkPv")));
        bed.setPkDept(CommonUtils.getString(paramMap.get("pkDept")));
        bed.setPkDeptNs(CommonUtils.getString(paramMap.get("pkDeptNs")));
        bed.setEuHold("0");
        DataBaseHelper.insertBean(bed);
    }

    /**
     * 插入床位记录
     * @param paramMap
     * @param user
     */
    private void saveSrcBed(Map<String,Object> paramMap,User user){
        PvBed bed = new PvBed();
        bed.setBedno(CommonUtils.getString(paramMap.get("bednoSrc")));
        bed.setDateBegin(new Date());
        bed.setFlagMaj("1");
        bed.setNameEmpBegin(user.getNameEmp());
        bed.setPkEmpBegin(user.getPkEmp());
        BdResBed bedvo = exPubService.getBedInfoByPk(CommonUtils.getString(paramMap.get("pkBedSrc")), user);
        if(bedvo!=null){
            bed.setPkBedWard(bedvo.getPkWard());
        }
        bed.setPkPv(CommonUtils.getString(paramMap.get("pkPvDes")));
        bed.setPkDept(CommonUtils.getString(paramMap.get("pkDept")));
        bed.setPkDeptNs(CommonUtils.getString(paramMap.get("pkDeptNs")));
        bed.setEuHold("0");
        DataBaseHelper.insertBean(bed);
    }

    private void changedChdBed(List<Map<String,Object>> inflist,String bedno_des,String pk_dept_ns,User user, String type){
        if(inflist.size()>0){
            String bedSpec = ExSysParamUtil.getSpcOfCodeBed();
            if(CommonUtils.isEmptyString(bedSpec))
                throw new BusException("请维护系统参数【BD0007】婴儿的床位分隔符！");
            //2019-07-06 现逻辑：若当前床位不存在则自动补全，然后进行换床操作
            String bedNoList = "";
            List<BdResBed> bedNolist = new ArrayList<BdResBed>();

            for (Map<String, Object> map : inflist) {
                BdResBed bedInf = new BdResBed();
                bedNoList = bedNoList + (bedno_des + bedSpec + CommonUtils.getString(map.get("sortNo"))+",");
                bedInf.setCode(bedno_des + bedSpec + CommonUtils.getString(map.get("sortNo")));
                bedNolist.add(bedInf);
            }
            if(!CommonUtils.isEmptyString(bedNoList))
                bedNoList = bedNoList.substring(0,bedNoList.length() - 1);
            BdResBed bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where code=? and pk_ward =? ", BdResBed.class,
                                                            new Object[]{bedno_des,pk_dept_ns});
            if(null == bedMa)
                throw new BusException("未获取到床位【"+bedno_des+"】！");
            List<BdResBed> bedlist =  bdResPubService.isHaveBedAndAdd(pk_dept_ns, bedMa, null, bedNoList, user);
            if(null == bedlist || bedlist.size() < 1)
                updateInfantBed(inflist,bedNolist,pk_dept_ns,type);//更新床位
            else
                updateInfantBed(inflist,bedlist,pk_dept_ns,type);//更新床位
        }
    }


    private void updateInfantBed(List<Map<String,Object>> inflist,List<BdResBed> bedlist,String pk_dept_ns,String type){
        for(int i=0;i<inflist.size();i++){
            Map<String,Object> tempMap = inflist.get(i);
            tempMap.put("pkdeptns", pk_dept_ns);
            tempMap.put("codebed", bedlist.get(i).getCode());
            String update_pv = "update pv_encounter set bed_no = :codebed where pk_pv = :pkPv";
            DataBaseHelper.update(update_pv,tempMap);
            String update_pv_bed = "update pv_bed set bedno = :codebed where pk_pv=:pkPv and pk_dept_ns = :pkdeptns";
            DataBaseHelper.update(update_pv_bed,tempMap);
            String update_bed_des = "update bd_res_bed set pk_pi =:pkPi ,flag_ocupy='1',eu_status='02'"
                    + " ,flag_active = (case when '09' = dt_bedtype then '1' else flag_active end)"
                    + " ,del_flag = (case when '09' = dt_bedtype then '0' else del_flag end) where code=:codebed and pk_ward=:pkdeptns";
            DataBaseHelper.update(update_bed_des, tempMap);
            if("Des".equals(type)){
                String update_bed_src = "update bd_res_bed set pk_pi = null,flag_ocupy='0',eu_status='01'"
                        + " ,flag_active = (case when '09' = dt_bedtype then '0' else flag_active end)"
                        + " ,del_flag = (case when '09' = dt_bedtype then '1' else del_flag end)  where code=:bedNo and pk_ward=:pkdeptns";
                DataBaseHelper.update(update_bed_src, tempMap);
            }
        }
    }


    /**
     * 产房换床操作
     * @param param
     * @param user
     */
    public void exLaborChangedBed(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if(paramMap == null ||"".equals(CommonUtils.getString(paramMap.get("pkPv")))||"".equals(CommonUtils.getString(paramMap.get("bednoDes")))
                ||"".equals(CommonUtils.getString(paramMap.get("bednoSrc")))){
            throw new BusException("换床患者或目标床位号为空，请确认后重新操作！");
        }
        String bedSpec = ExSysParamUtil.getSpcOfCodeBed();
        if(CommonUtils.isEmptyString(bedSpec))
            throw new BusException("请维护系统参数【BD0007】婴儿的床位分隔符！");
        User u = (User)user;
        String pk_org = u.getPkOrg();
        String pk_dept_ns = CommonUtils.getString(paramMap.get("pkDeptNs"));//病区主键
        //目标床位
        String bedNoDes = CommonUtils.getString(paramMap.get("bednoDes"));//目标床位号
        String pkBedDes = CommonUtils.getString(paramMap.get("pkBedDes"));//目标床位主键
        String pkPiDes = CommonUtils.getString(paramMap.get("pkPiDes"));//目标床位所在患者信息
        String pkItemDes = CommonUtils.getString(paramMap.get("pkItemDes"));//目标床位的婴儿单独入产房标识
        String pkPvlaborDes = CommonUtils.getString(paramMap.get("pkPvlaborDes"));//目标床位患者在产房的就诊记录主键
        //目标床位下的婴儿
        List<LabPatiCardVo> chdDesList = new ArrayList<>();

        //原床位
        String pkPi = CommonUtils.getString(paramMap.get("pkPi"));//原床位所在患者信息
        String bedNoSrc = CommonUtils.getString(paramMap.get("bednoSrc"));  //原床位号
        String pkBedSrc = CommonUtils.getString(paramMap.get("pkBedSrc"));//原床位主键
        String pkItemSrc = CommonUtils.getString(paramMap.get("pkItemSrc"));//原床位的婴儿单独入产房标识
        String pkPvlaborSrc = CommonUtils.getString(paramMap.get("pkPvlaborSrc"));//原床位患者在产房的就诊记录主键
        //原床位下的婴儿
        List<Map<String,Object>> chdSrcList = (List<Map<String,Object>>)paramMap.get("srcBedChd");

        Map<String,Object> flagOcupy = DataBaseHelper.queryForMap(" select flag_ocupy from bd_res_bed where code = ? and pk_org = ? " +
                " and pk_ward = ? ", new Object[]{bedNoDes,pk_org,pk_dept_ns});
        //目标床位是否为空
        if(flagOcupy!=null&&"1".equals(CommonUtils.getString(flagOcupy.get("flagOcupy")))){
            //获取目标床位下是否有婴儿
            String queSql = " select bed.pk_bed,inf.sort_no,lab.* from pv_labor lab " +
                            " inner join bd_res_bed bed on bed.code = lab.bed_no and bed.pk_ward = lab.pk_dept_ns and bed.pk_item is null " +
                            " inner join pv_infant inf on lab.pk_pv = inf.pk_pv_infant " +
                            " where lab.pk_pvlabor_mother = ? and lab.eu_status = '1' ";
            chdDesList = DataBaseHelper.queryForList(queSql,LabPatiCardVo.class,pkPvlaborDes);
            //修改原床位的患者信息及床位占用标识
            String updateSql = " update BD_RES_BED set pk_pi = ? ";
            if(pkItemDes != null && !"".equals(pkItemDes)){
                updateSql += " ,pk_item = '" + pkItemDes + "'";
            }else{
                updateSql += " ,pk_item = null";
            }
            updateSql += " where pk_bed = ? and pk_pi = ? and del_flag = '0'";
            int cnt = DataBaseHelper.update(updateSql,pkPiDes,pkBedSrc,pkPi);
            if(cnt < 1){
                throw new BusException("原床位信息已经发生变化，请先刷新！");
            }
            //修改目标床位的患者信息及床位占用标识
            updateSql = " update BD_RES_BED set pk_pi = ? ";
            if(pkItemSrc != null && !"".equals(pkItemSrc)){
                updateSql += " ,pk_item = '" + pkItemSrc + "'";
            }else{
                updateSql += " ,pk_item = null";
            }
            updateSql += " where pk_bed = ? and pk_pi = ? and del_flag = '0'";
            cnt = DataBaseHelper.update(updateSql,pkPi,pkBedDes,pkPiDes);
            if(cnt < 1){
                throw new BusException("目标床位信息已经发生变化，请先刷新！");
            }
            //修改原床位患者在产房就诊记录信息的床位号
            DataBaseHelper.update("update PV_LABOR set bed_no = ? where pk_pvlabor = ? and eu_status = '1'",bedNoDes,pkPvlaborSrc);
            //修改目标床位患者在产房就诊记录信息的床位号
            DataBaseHelper.update("update PV_LABOR set bed_no = ? where pk_pvlabor = ? and eu_status = '1'",bedNoSrc,pkPvlaborDes);
            //如果原床位名下存在婴儿信息，同时给名下婴儿换床
            if(chdSrcList != null && chdSrcList.size() > 0){
                for(Map<String,Object> chdSrc:chdSrcList){
                    String chdBedNo = bedNoDes+bedSpec+chdSrc.get("sortNo");
                    //修改婴儿床位号
                    DataBaseHelper.update("update BD_RES_BED set code = ?,name = ? where pk_bed = ?",chdBedNo,chdBedNo,chdSrc.get("pkBed"));
                    //修改婴儿在产房的就诊记录床位信息
                    DataBaseHelper.update("update PV_LABOR set bed_no = ? where pk_pvlabor = ?",chdBedNo,chdSrc.get("pkPvlabor"));
                }
            }
            //如果目标床位名下存在婴儿信息，同时给名下婴儿换床
            if(chdDesList != null && chdDesList.size() > 0){
                for(LabPatiCardVo chdDes:chdDesList){
                    String chdBedNo = bedNoSrc+bedSpec+chdDes.getSortNo();
                    //修改婴儿床位号
                    DataBaseHelper.update("update BD_RES_BED set code = ?,name = ? where pk_bed = ?",chdBedNo,chdBedNo,chdDes.getPkBed());
                    //修改婴儿在产房的就诊记录床位信息
                    DataBaseHelper.update("update PV_LABOR set bed_no = ? where pk_pvlabor = ?",chdBedNo,chdDes.getPkPvlabor());
                }
            }
        }else{
            //释放原床位
            int cnt = DataBaseHelper.update(" update bd_res_bed set eu_status = '01',flag_ocupy = '0',pk_item = null,pk_pi = null " +
                                                 " where pk_bed = ? and pk_pi = ? and del_flag = '0'" ,pkBedSrc,pkPi);
            if(cnt < 1){
                throw new BusException("原床位信息已经发生变化，请先刷新！");
            }
            String updateSql = " update BD_RES_BED set EU_STATUS = '02',flag_ocupy = '1',pk_pi = ? ";
            if(pkItemSrc != null && !"".equals(pkItemSrc)){
                updateSql += " ,pk_item = '" + pkItemSrc + "'";
            }
            updateSql += " where pk_bed = ? and pk_pi is null and del_flag = '0'";
            //占用目标床位
            cnt = DataBaseHelper.update(updateSql,pkPi,pkBedDes);
            if(cnt < 1){
                throw new BusException("目标床位信息已经发生变化，请先刷新！");
            }
            //修改患者在产房就诊记录信息的床位号
            DataBaseHelper.update("update PV_LABOR set bed_no = ? where pk_pvlabor = ? and eu_status = '1'",bedNoDes,pkPvlaborSrc);
            //如果原床位名下存在婴儿信息，同时给名下婴儿换床
            if(chdSrcList != null && chdSrcList.size() > 0){
                for(Map<String,Object> chdSrc:chdSrcList){
                    String chdBedNo = bedNoDes+bedSpec+chdSrc.get("sortNo");
                    //修改婴儿床位号
                    DataBaseHelper.update("update BD_RES_BED set code = ?,name = ? where pk_bed = ?",chdBedNo,chdBedNo,chdSrc.get("pkBed"));
                    //修改婴儿在产房的就诊记录床位信息
                    DataBaseHelper.update("update PV_LABOR set bed_no = ? where pk_pvlabor = ?",chdBedNo,chdSrc.get("pkPvlabor"));
                }
            }
        }
    }
}
