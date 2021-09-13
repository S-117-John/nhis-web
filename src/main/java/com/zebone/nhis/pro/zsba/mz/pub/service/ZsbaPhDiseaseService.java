package com.zebone.nhis.pro.zsba.mz.pub.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.pro.lb.PhDisease;
import com.zebone.nhis.common.module.pro.lb.PhDiseaseFoodborne;
import com.zebone.nhis.common.module.pro.lb.PhDiseaseFoodborneDt;
import com.zebone.nhis.common.module.pro.lb.PhDiseaseSample;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.mz.pub.dao.ZsbaPhDiseaseMapper;
import com.zebone.nhis.pro.zsba.mz.pub.vo.PhDiseaseFoodVo;
import com.zebone.nhis.pro.zsba.mz.pub.vo.PhDiseaseHarm;
import com.zebone.nhis.pro.zsba.mz.pub.vo.PhDiseaseHarmVo;
import com.zebone.nhis.pro.zsba.mz.pub.vo.PhDiseaseVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.build.BuildSql;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description 博爱项目报卡服务
 * @auther wuqiang
 * @Date 2021-04-12
 */
@Service
public class ZsbaPhDiseaseService {

    @Resource
    private ZsbaPhDiseaseMapper diseaseMapper;

    public void addDiseaseInfo(String param, IUser user){
        PhDiseaseVo diseaseVo = JsonUtil.readValue(param,PhDiseaseVo.class);
        Objects.requireNonNull(diseaseVo,"diseaseVo不能为空");
        Objects.requireNonNull(diseaseVo.getBorne(),"borne不能为空");
        Objects.requireNonNull(diseaseVo.getPhDisease(),"phDisease不能为空");
        //if(CollectionUtils.isEmpty(diseaseVo.getBorneDtList()) || CollectionUtils.isEmpty(diseaseVo.getSampleList())){
        //    throw new BusException("进食症状、采集生物标本。不能为空");
        //}

        if(StringUtils.isBlank(diseaseVo.getPhDisease().getPkPhdise())){
            ApplicationUtils.setDefaultValue(diseaseVo.getPhDisease(), true);
            DataBaseHelper.update(BuildSql.buildInsertSqlWithClass(PhDisease.class), diseaseVo.getPhDisease());
        } else{
            ApplicationUtils.setDefaultValue(diseaseVo.getPhDisease(), false);
            DataBaseHelper.updateBeanByPk(diseaseVo.getPhDisease(),false);
        }

        diseaseVo.getBorne().setPkPhdise(diseaseVo.getPhDisease().getPkPhdise());
        if(StringUtils.isBlank(diseaseVo.getBorne().getPkFoodborne())){
            ApplicationUtils.setDefaultValue(diseaseVo.getBorne(), true);
            DataBaseHelper.update(BuildSql.buildInsertSqlWithClass(PhDiseaseFoodborne.class), diseaseVo.getBorne());
        } else{
            ApplicationUtils.setDefaultValue(diseaseVo.getBorne(), false);
            DataBaseHelper.updateBeanByPk(diseaseVo.getBorne(),false);
        }

        if(CollectionUtils.isNotEmpty(diseaseVo.getSampleList())){
            List<PhDiseaseSample> modSampleList = Lists.newArrayList();
            for (PhDiseaseSample vo:diseaseVo.getSampleList()){
                vo.setPkFoodborne(diseaseVo.getBorne().getPkFoodborne());
                boolean flagAdd = StringUtils.isBlank(vo.getPkSample());
                if(!flagAdd){
                    modSampleList.add(vo);
                }
                ApplicationUtils.setDefaultValue(vo, flagAdd);
            }
            if(modSampleList.size()>0) {
                DataBaseHelper.batchUpdate(BuildSql.buildUpdateSqlWithClass(PhDiseaseSample.class), diseaseVo.getSampleList());
                diseaseVo.getSampleList().removeAll(modSampleList);
            }
            if(diseaseVo.getSampleList().size()>0) {
                DataBaseHelper.batchUpdate(BuildSql.buildInsertSqlWithClass(PhDiseaseSample.class), diseaseVo.getSampleList());
            }
        }

        if(CollectionUtils.isNotEmpty(diseaseVo.getBorneDtList())){
            List<PhDiseaseFoodborneDt> modBorneList = Lists.newArrayList();
            for (PhDiseaseFoodborneDt vo:diseaseVo.getBorneDtList()){
                vo.setPkFoodborne(diseaseVo.getBorne().getPkFoodborne());
                boolean flagAdd = StringUtils.isBlank(vo.getPkFoodbornedt());
                if(!flagAdd){
                    modBorneList.add(vo);
                }
                ApplicationUtils.setDefaultValue(vo, flagAdd);
            }
            if(modBorneList.size()>0) {
                DataBaseHelper.batchUpdate(BuildSql.buildUpdateSqlWithClass(PhDiseaseFoodborneDt.class), diseaseVo.getBorneDtList());
                diseaseVo.getBorneDtList().removeAll(modBorneList);
            }
            if(diseaseVo.getBorneDtList().size()>0) {
                DataBaseHelper.batchUpdate(BuildSql.buildInsertSqlWithClass(PhDiseaseFoodborneDt.class), diseaseVo.getBorneDtList());
            }
        }
    }

    public void delDiseaseInfo(String param, IUser user){
        List<String> pkList = JsonUtil.readValue(param,List.class);
        if(CollectionUtils.isNotEmpty(pkList)){
            String pkStr = CommonUtils.convertListToSqlInPart(pkList);
            String whereFoodStr = " where PK_FOODBORNE in(select t.PK_FOODBORNE from PH_DISEASE_FOODBORNE t where t.PK_PHDISE in("+pkStr+"))";
            DataBaseHelper.update("update PH_DISEASE set del_flag = '1' where pk_phdise in("+ pkStr +")");
            DataBaseHelper.update("update PH_DISEASE_FOODBORNE set del_flag = '1' where PK_PHDISE in("+ pkStr +")");
            DataBaseHelper.update("update PH_DISEASE_SAMPLE set del_flag = '1'" +whereFoodStr);
            DataBaseHelper.update("update PH_DISEASE_FOODBORNE_DT set del_flag = '1'" + whereFoodStr);
        }
    }

    /**
     * 删除食源性标本或食品来源数据
     * 交易码：022003009015
     * @param param
     * @param user
     */
    public void delDiseaseSampleFoodInfo(String param, IUser user){
    	Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
    	if(StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkFoodbornedt"))) {//食品来源删除
    		DataBaseHelper.execute("delete from PH_DISEASE_FOODBORNE_DT where PK_FOODBORNEDT = ? ", MapUtils.getString(paramMap, "pkFoodbornedt"));
    	}else if(StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkSample"))) {//删除标本信息
    		DataBaseHelper.execute("delete from ph_disease_sample where PK_SAMPLE = ?", MapUtils.getString(paramMap, "pkSample"));
    	}
    }
    
    public List<PhDiseaseSample> searchDisease(String param, IUser user){
        List<String> list = JsonUtil.readValue(param,List.class);
        List<PhDiseaseSample> phDiseList = diseaseMapper.qrySample(list);
        if(null != phDiseList && !phDiseList.isEmpty()) 
        {
        	return phDiseList;
        }
        return null;
    }

    public List<PhDiseaseFoodVo> searchDiseaseFoods(String param, IUser user){
        List<String> list = JsonUtil.readValue(param,List.class);
        List<PhDiseaseFoodVo> phFoodList = diseaseMapper.qryFoodborne(list);
        if(null != phFoodList && !phFoodList.isEmpty()) 
        {
        	return phFoodList;
        }
        return null;
    }
    
    /**
     * 查询食源性疾病上报列表
     * 交易码：022003009005
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> querSearchDataList(String param, IUser user){
    	Map<String,Object> serParam= JsonUtil.readValue(param, Map.class);
    	return diseaseMapper.qrySearchData(serParam);
    }
    
    /**
     * 新增食源性疾病查询基本相关信息
     * 交易码：022003009010
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> querPkPvSearchData(String param, IUser user){
    	Map<String,Object> serParam= JsonUtil.readValue(param, Map.class);
    	return diseaseMapper.qrySearchDataPkPv(serParam);
    }
    
    /**
     * @Description 修改或保存全国伤害保卡
     * @auther wuqiang
     * @Date 2021-04-12
     * @Param [param, user]
     * @return com.zebone.nhis.pro.zsba.mz.pub.vo.PhDiseaseHarm
     */
    public void savePhDiseaseHaram(String param, IUser user ){
        PhDiseaseHarmVo diseaseHarm = JsonUtil.readValue(param,PhDiseaseHarmVo.class);

        if(StringUtils.isBlank(diseaseHarm.phDisease.getPkPhdise())){
            ApplicationUtils.setDefaultValue(diseaseHarm.getPhDisease(), true);
            DataBaseHelper.update(BuildSql.buildInsertSqlWithClass(PhDisease.class),  diseaseHarm.getPhDisease());
        } else {
            ApplicationUtils.setDefaultValue(diseaseHarm.getPhDisease(), false);
            DataBaseHelper.updateBeanByPk( diseaseHarm.getPhDisease(),false);
        }

        if(StringUtils.isBlank(diseaseHarm.phDiseaseHarm.getPkHarm())){
            ApplicationUtils.setDefaultValue(diseaseHarm.getPhDiseaseHarm(), true);
            DataBaseHelper.update(BuildSql.buildInsertSqlWithClass(PhDiseaseHarm.class),  diseaseHarm.getPhDiseaseHarm());
        } else {
            ApplicationUtils.setDefaultValue(diseaseHarm.getPhDiseaseHarm(), false);
            DataBaseHelper.updateBeanByPk( diseaseHarm.getPhDiseaseHarm(),false);
            if(diseaseHarm.getPhDiseaseHarm().getVcDxallx() == null){
                DataBaseHelper.update("update PH_DISEASE_HARM set vc_dxallx=null where pk_harm=:pkHarm",diseaseHarm.getPhDiseaseHarm());
            }
        }
    }
      /**
       * @Description 获取全国伤害报卡列表
       * @auther wuqiang
       * @Date 2021-04-12
       * @Param [param, user]
       * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
       */
      public PhDiseaseHarmVo getPhDiseaseHaramList(String param, IUser user){
          Map<String,Object> serParam= JsonUtil.readValue(param, Map.class);
          List<PhDiseaseHarm> searchList= diseaseMapper.getPhDiseaseHaramList(serParam);
          List<PhDisease> phDiseases= diseaseMapper.getPhDiseaseList(serParam);
          PhDiseaseHarmVo vo = new PhDiseaseHarmVo();
          if(phDiseases !=null && phDiseases.size() > 0){
              vo.setPhDisease(phDiseases.get(0));
          }
          if(searchList != null && searchList.size() > 0){
              vo.setPhDiseaseHarm(searchList.get(0));
          }
          return vo;
      }

    /**
     * @Description 获取全国伤害报卡列表
     * @auther wuqiang
     * @Date 2021-04-12
     * @Param [param, user]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public List<Map<String, Object>> getPhDiseaseHaramLists(String param, IUser user){
        Map<String,Object> serParam= JsonUtil.readValue(param, Map.class);
        return diseaseMapper.getPhDiseaseHarams(serParam);

    }
      /**
       * @Description 获取全国伤害报卡详情
       * @auther wuqiang
       * @Date 2021-04-12
       * @Param [param, user]
       * @return com.zebone.nhis.pro.zsba.mz.pub.vo.PhDiseaseHarm
       */
      public PhDiseaseHarm  getPhDiseaseHarm(String param, IUser user){
          String pkHarm=JsonUtil.getFieldValue(param,"pkHarm");

          return DataBaseHelper.queryForBean("select *" +
                  "from  PH_DISEASE_HARM where PK_HARM=?",PhDiseaseHarm.class,pkHarm);
      }

    /**
     * @Description 获取全国伤害报卡详情
     * @auther wuqiang
     * @Date 2021-04-12
     * @Param [param, user]
     * @return com.zebone.nhis.pro.zsba.mz.pub.vo.PhDiseaseHarm
     */
    public void delPhDiseaseHarm(String param, IUser user){

        String pkHarm=JsonUtil.getFieldValue(param,"pkHarm");
        DataBaseHelper.execute("update PH_DISEASE_HARM set del_flag='1' where PK_HARM=?",pkHarm);
    }
    
    /**
     * 交易号：022003009016
     * @Description 获取未完成的食源性上报数量
     * @Date 2021-04-21
     * @Param [param, user]
     * @return Integer
     */
    public Integer qryPhDiseaseHarmNum(String param, IUser user){
    	String pkEmp =JsonUtil.getFieldValue(param,"pkEmp");
        String sql ="select count(1) from  ph_disease where eu_status ='0' and creator=? ";
        return DataBaseHelper.queryForScalar(sql, Integer.class, pkEmp);
    }
}
