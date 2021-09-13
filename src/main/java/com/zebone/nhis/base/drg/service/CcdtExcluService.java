package com.zebone.nhis.base.drg.service;

import com.zebone.nhis.base.drg.dao.CcdtExcluMapper;
import com.zebone.nhis.base.drg.vo.BdTermCcdtSaveParam;
import com.zebone.nhis.base.drg.vo.DelExclu;
import com.zebone.nhis.common.module.base.bd.drg.BdTermCcdt;
import com.zebone.nhis.common.module.base.bd.drg.BdTermCcdtExclu;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CCDT排斥规则
 */
@Service
public class CcdtExcluService {
    @Autowired
    private CcdtExcluMapper ccdtExcluMapper;

    /**
     * CCDT排斥规则保存
     *
     * @param param
     * @param user
     * @return
     */
    public void saveCcdt(String param, IUser user) {
        BdTermCcdtSaveParam saveParam = JsonUtil.readValue(param, BdTermCcdtSaveParam.class);

        BdTermCcdtExclu bdTermCcdtExclu = saveParam.getBdTermCcdtExclu();
        List<BdTermCcdtExclu> ccdtList = saveParam.getCcdts();
        List<BdTermCcdtExclu> delCcdts = saveParam.getDelCcdts();
        User users = UserContext.getUser();
        if (delCcdts != null) {
            DataBaseHelper.batchUpdate("delete from bd_term_ccdt_exclu where pk_ccdtexclu=:pkCcdtexclu",delCcdts);
        }
        if (StringUtils.isBlank(bdTermCcdtExclu.getPkCcdtexclu())) {   // 新增
            ApplicationUtils.setDefaultValue(bdTermCcdtExclu, true);
            bdTermCcdtExclu.setPkOrg("~");
            bdTermCcdtExclu.setFlagDel("0");
            bdTermCcdtExclu.setCreator(users.getPkEmp());
            bdTermCcdtExclu.setCreateTime(new Date());

            for (BdTermCcdtExclu b : ccdtList) {
                ApplicationUtils.setDefaultValue(b, true);
                if (bdTermCcdtExclu.getEuExclutype().equals("2")){ //验证相互排斥的明细
                    List<Map<String, Object>> checkList=new ArrayList<Map<String, Object>>();
                    checkList = DataBaseHelper.queryForList("select pk_ccdt,code_ccdt from BD_TERM_CCDT_EXCLU where eu_child = '1'and flag_del='0' and GROUPNO = ?",new Object[]{bdTermCcdtExclu.getGroupnoExclu()});
                    for(Map<String,Object> m : checkList){
                        if(b.getPkCcdt().equals(m.get("pkCcdt"))){
                            throw new BusException("双组排斥的明细不能重复!");
                        }
                    }
                }
                b.setPkOrg(bdTermCcdtExclu.getPkOrg());
                b.setFlagDel("0");
                b.setEuChild("1");
                b.setGroupno(bdTermCcdtExclu.getGroupno());
                DataBaseHelper.insertBean(b);
            }
            //修改关联排斥组的排斥组号为当前规则组号
            if (bdTermCcdtExclu.getEuExclutype().equals("2") && bdTermCcdtExclu.getGroupnoExclu()!=null){
                DataBaseHelper.update("update bd_term_ccdt_exclu set Groupno_Exclu = '"+bdTermCcdtExclu.getGroupno()+"' where eu_child = '0' and GROUPNO = ?", new Object[]{bdTermCcdtExclu.getGroupnoExclu()});
            }
            DataBaseHelper.insertBean(bdTermCcdtExclu);
//            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdTermCcdtExclu.class), ccdtList);

        } else {//修改
            bdTermCcdtExclu.setModifier(users.getPkEmp());

            //更新排斥规则
            for (BdTermCcdtExclu b : ccdtList) {
                List<BdTermCcdtExclu> addListSave =new ArrayList<BdTermCcdtExclu>();
                List<BdTermCcdtExclu> updateListSave =new ArrayList<BdTermCcdtExclu>();
                    if(StringUtils.isEmpty(b.getPkCcdtexclu())){
                        ApplicationUtils.setDefaultValue(b, true);
                        if (bdTermCcdtExclu.getEuExclutype().equals("2")){//验证相互排斥的明细
                            List<Map<String, Object>> checkList=new ArrayList<Map<String, Object>>();
                            checkList = DataBaseHelper.queryForList("select pk_ccdt,code_ccdt from BD_TERM_CCDT_EXCLU where eu_child = '1'and flag_del='0' and GROUPNO = ?",new Object[]{bdTermCcdtExclu.getGroupnoExclu()});
                            for(Map<String,Object> m : checkList){
                                if(b.getPkCcdt().equals(m.get("pkCcdt"))){
                                    throw new BusException("双组排斥的明细不能重复!");
                                }
                            }
                        }
                        b.setPkOrg(bdTermCcdtExclu.getPkOrg());
                        b.setFlagDel("0");
                        b.setEuChild("1");
                        b.setGroupno(bdTermCcdtExclu.getGroupno());
                        DataBaseHelper.insertBean(b);
                    }else{
                        b.setModifier(users.getPkEmp());
                        b.setPkOrg(bdTermCcdtExclu.getPkOrg());
                        b.setFlagDel("0");
                        b.setEuChild("1");
                        updateListSave.add(b);
                }
                if (bdTermCcdtExclu.getEuExclutype().equals("2")){
                    DataBaseHelper.update("update bd_term_ccdt_exclu set Groupno_Exclu = '"+bdTermCcdtExclu.getGroupno()+"' where GROUPNO_EXCLU = ?", new Object[]{bdTermCcdtExclu.getGroupnoExclu()});
                }
                DataBaseHelper.updateBeanByPk(bdTermCcdtExclu);
                if(null!=updateListSave && updateListSave.size()>0){
                    DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BdTermCcdtExclu.class), updateListSave);
                }
            }
        }

    }




    /**
     * 删除CCDT排斥规则
     *
     * @param param
     * @param user
     */
    public void delCcdt(String param, IUser user) {
        DelExclu delExclu = JsonUtil.readValue(param, DelExclu.class);
        if (delExclu != null) {
            //排斥类型等于2,
            if (delExclu.getEuExclutype().equals("2")) {
                if (delExclu.getYesOrNo().equals("1")) {//同步删除关联排斥组
                    DataBaseHelper.execute("delete from bd_term_ccdt_exclu where groupno=?", new Object[]{delExclu.getGroupnoExclu()});
                    DataBaseHelper.execute("delete from bd_term_ccdt_exclu where groupno=?", new Object[]{delExclu.getGroupno()});
                }else {//清空关联
                    DataBaseHelper.execute("update  bd_term_ccdt_exclu   set eu_exclutype='-1', groupno_exclu = null where   groupno_exclu = ?", new Object[]{delExclu.getGroupno()});
                    DataBaseHelper.execute("delete from bd_term_ccdt_exclu where groupno=?", new Object[]{delExclu.getGroupno()});
                }
            }else{//排斥类型不等于2，删除排斥组
                DataBaseHelper.execute("delete from bd_term_ccdt_exclu where groupno=?", new Object[]{delExclu.getGroupno()});
            }
        }
    }


    /**
     * 查找左侧树
     *
     * @param param
     * @param user
     */
    public List<BdTermCcdtSaveParam> qryCcdtExclu(String param, IUser user) {
        String temp = JsonUtil.getFieldValue(param, "qryParam");
        String name = "";
        String spcode = "";
        if (isEnglish(temp)) spcode = temp;
        else name = temp;

        return ccdtExcluMapper.qryCcdtExclu(name, spcode.toUpperCase());
    }

    /**
     * 判断字符串是否是英文
     *
     * @param charaString
     * @return
     */
    public boolean isEnglish(String charaString) {

        return charaString.matches("^[a-zA-Z]*");
    }

    /**
     * 返回最大序号
     * @param param
     * @param user
     * @return
     */
    public Integer getGroupno(String param, IUser user) {

        Integer groupnos = ccdtExcluMapper.getGroupno();
        if ( groupnos == null){
            return 1;
        }
        return (groupnos + 1);
    }

    /**
     * 查询右侧表格
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> searchCcdt(String param, IUser user) {
        Map<String, String> paramMap = JsonUtil.readValue(param, Map.class); //读数据，获取前台的参数
        List<Map<String, Object>> rtnList_ccdt = ccdtExcluMapper.getReleaseListByCcdtExclu(paramMap);  //查詢結果保存到rtnList

        Map map = new HashMap<String, Object>();
        map.put("ccdts", rtnList_ccdt);
        return map;
    }

}
