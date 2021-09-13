package com.zebone.nhis.base.bd.service;

import com.zebone.nhis.base.bd.dao.MKMapper;
import com.zebone.nhis.base.bd.vo.BdSupplyAndItemParam;
import com.zebone.nhis.base.bd.vo.BdTermDiagAndAlias;
import com.zebone.nhis.base.bd.vo.BdTermFreqAndTimeParam;
import com.zebone.nhis.common.module.base.bd.mk.*;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医疗规范服务
 *
 * @author wangpeng
 * @date 2016年8月26日
 */
@Service
public class MkService {

    /**
     * 添加状态
     **/
    public static final String AddState = "_ADD";

    /**
     * 更新状态
     */
    public static final String UpdateState = "_UPDATE";

    /**
     * 删除状态
     */
    public static final String DelState = "_DELETE";

    @Autowired
    private MKMapper mkMapper;

    //增删改标志
    private String rleCode = "";

    /**
     * @param param
     * @param user
     * @return BdTermDiag
     * @throws
     * @Description: 根据标准诊断编码主键判断保存或者修改标准诊断编码，需要校验诊断编码、诊断名称是否唯一
     * @author wangpeng
     * @date 2016年8月26日
     */
    public BdTermDiagAndAlias saveTermDiag(String param, IUser user) {
        BdTermDiagAndAlias list = JsonUtil.readValue(param, BdTermDiagAndAlias.class);
        BdTermDiag diag = list.diag;
        if (diag.getPkDiag() == null) {// 保存
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_term_diag " + "where del_flag = '0' and diagcode = ?", Integer.class,
                    diag.getDiagcode());
            int count_name = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_term_diag " + "where del_flag = '0' and diagname = ?", Integer.class,
                    diag.getDiagname());
            if (count_code != 0) {
                throw new BusException("诊断编码重复！");
            } else if (count_name != 0) {
                throw new BusException("诊断名称重复！");
            } else {
                if (diag.getFlagStop() == null) {
                    diag.setFlagStop("0");
                }
                DataBaseHelper.insertBean(diag);
                if (list.alias != null) {
                    for (BdTermDiagAlias alias : list.alias) {
                        if (alias.getPkDiag() == null) {
                            alias.setPkDiag(diag.getPkDiag());
                            alias.setPkDiagAlias(NHISUUID.getKeyId());
                            DataBaseHelper.insertBean(alias);
                        } else {
                            DataBaseHelper.updateBeanByPk(alias, false);
                        }
                    }
                }
                //'添加'标志
                rleCode = AddState;
            }
        } else {
            int count_code = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_term_diag " + "where del_flag = '0' and diagcode = ? and pk_diag != ? ",
                    Integer.class, diag.getDiagcode(), diag.getPkDiag());
            int count_name = DataBaseHelper.queryForScalar(
                    "select count(1) from bd_term_diag " + "where del_flag = '0' and diagname = ? and pk_diag != ? ",
                    Integer.class, diag.getDiagname(), diag.getPkDiag());
            if (count_code != 0) {
                throw new BusException("诊断编码重复！");
            } else if (count_name != 0) {
                throw new BusException("诊断名称重复！");
            } else {
                if (diag.getFlagStop() == null) {
                    diag.setFlagStop("0");
                }
                DataBaseHelper.updateBeanByPk(diag, false);
                //DataBaseHelper.execute("delete from bd_term_diag_alias where pk_diag = ?", diag.getPkDiag());
                if (list.alias != null && list.alias.size() > 0) {
                    for (BdTermDiagAlias alias : list.alias) {
                        if (alias.getPkDiag() == null) {
                            alias.setPkDiag(diag.getPkDiag());
                            alias.setPkDiagAlias(NHISUUID.getKeyId());
                            DataBaseHelper.insertBean(alias);
                        } else {
                            DataBaseHelper.updateBeanByPk(alias, false);
                        }
                    }
                }
                //'修改'标志
                rleCode = this.UpdateState;
            }
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("diag", diag);
        paramMap.put("STATUS", rleCode);//状态
        //发送消息
        PlatFormSendUtils.sendBdTermDiagMsg(paramMap);
        return list;
    }

    public void delAlias(String param, IUser user) {
        String pkDiagAlias = JsonUtil.getFieldValue(param, "pkDiagAlias");
        DataBaseHelper.execute("delete from bd_term_diag_alias where pk_diagalias = ?", pkDiagAlias);
    }

    /**
     * @param param
     * @param user
     * @throws
     * @Description: 逻辑删除标准诊断编码信息（将del_flag置为1）
     * @author wangpeng
     * @date 2016年8月26日
     */
    public void deleteTermDiag(String param, IUser user) {
        BdTermDiag diag = JsonUtil.readValue(param, BdTermDiag.class);
        DataBaseHelper.update("update bd_term_diag set del_flag = '1' where pk_diag = ? ",
                new Object[]{diag.getPkDiag()});
        //'删除'标志
        rleCode = this.DelState;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("diag", diag);
        paramMap.put("STATUS", rleCode);//状态
        //发送消息
        PlatFormSendUtils.sendBdTermDiagMsg(paramMap);
    }

    /**
     * @param param
     * @return user
     * @throws
     * @Description: 根据是否存在频次主键保存或者修改医嘱频次以及时刻信息<br>
     * 对子表频次时刻的修改、删除操作均通过该方法的修改实现
     * @author wangpeng
     * @date 2016年8月29日
     */
    public BdTermFreq saveBdTermFreqAndTime(String param, IUser user) {
        BdTermFreqAndTimeParam freqAndTime = JsonUtil.readValue(param, BdTermFreqAndTimeParam.class);
        BdTermFreq freq = freqAndTime.getFreq();
        List<BdTermFreqTime> timeList = freqAndTime.getTimeList();
        /** 保存或修改频次 */
        if (freq.getPkFreq() == null) {
            int count_code = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_freq " + "where del_flag = '0' and code = ? and pk_org like ?||'%'",
                    Integer.class, freq.getCode(), freq.getPkOrg());
            int count_name = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_freq " + "where del_flag = '0' and name = ? and pk_org like ?||'%'",
                    Integer.class, freq.getName(), freq.getPkOrg());
            if (count_code != 0) {
                throw new BusException("频次编码重复！");
            } else if (count_name != 0) {
                throw new BusException("频次名称重复！");
            } else {
                DataBaseHelper.insertBean(freq);
                //'增加'标志
                rleCode = this.AddState;
            }
        } else {
            int count_code = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_freq "
                            + "where del_flag = '0' and code = ? and pk_org like ?||'%' and pk_freq != ?",
                    Integer.class, freq.getCode(), freq.getPkOrg(), freq.getPkFreq());
            int count_name = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_freq "
                            + "where del_flag = '0' and name = ? and pk_org like ?||'%' and pk_freq != ?",
                    Integer.class, freq.getName(), freq.getPkOrg(), freq.getPkFreq());
            if (count_code != 0) {
                throw new BusException("频次编码重复！");
            } else if (count_name != 0) {
                throw new BusException("频次名称重复！");
            } else {
                DataBaseHelper.updateBeanByPk(freq, false);
                //清除频次类型
                if (null == freq.getDtFreqtype()) {
                    String updSql = "update BD_TERM_FREQ set dt_freqtype = null where PK_FREQ = ?";
                    DataBaseHelper.update(updSql, new Object[]{freq.getPkFreq()});
                }
                //'修改'标志
                rleCode = this.UpdateState;
            }
        }
        /** 保存或修改频次时刻 */
        // 先全部删除
        String pkFreq = freq.getPkFreq();
        DataBaseHelper.execute("delete from bd_term_freq_time where pk_freq = ?", new Object[]{pkFreq});
        if (CollectionUtils.isNotEmpty(timeList)) {
            for (BdTermFreqTime freqTime : timeList) {
                freqTime.setPkFreq(pkFreq);
                DataBaseHelper.insertBean(freqTime);
            }
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("freq", freq);
        paramMap.put("STATUS", rleCode);//状态
        //发送消息
        PlatFormSendUtils.sendBdTermFreqMsg(paramMap);
        return freq;
    }

    /**
     * @param param
     * @return user
     * @throws
     * @Description: 逻辑删除医嘱频次, 物理删除医嘱频次时刻信息
     * @author wangpeng
     * @date 2016年8月29日
     */
    public void deleteBdTermFreq(String param, IUser user) {
        BdTermFreq freq = JsonUtil.readValue(param, BdTermFreq.class);
        //'删除'标志
        rleCode = this.DelState;
        //执行删除前获取将要删除的频次字典信息
        if ("0".equals(freq.getDelFlag())) {
            //DataBaseHelper.execute("delete from bd_term_freq_time where pk_freq = ?", new Object[] { freq.getPkFreq() });
            DataBaseHelper.update("update bd_term_freq set del_flag='1' where pk_freq= ? and del_flag='0'",
                    new Object[]{freq.getPkFreq()});
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
            paramMap.put("freq", freq);
            paramMap.put("STATUS", rleCode);//状态
            //发送消息
            PlatFormSendUtils.sendBdTermFreqMsg(paramMap);
        } else {
            DataBaseHelper.update("update bd_term_freq set del_flag='0' where pk_freq= ? and del_flag='1'",
                    new Object[]{freq.getPkFreq()});
        }
    }

    /**
     * @param param
     * @return user
     * @throws
     * @Description: 根据医嘱用法主键保存或者修改医嘱用法以及医嘱用法附加费用
     * @author wangpeng
     * @date 2016年8月29日
     */
    public BdSupply saveBdSupplyAndItem(String param, IUser user) {
        BdSupplyAndItemParam supplyAndItem = JsonUtil.readValue(param, BdSupplyAndItemParam.class);
        BdSupply supply = supplyAndItem.getSupply();
        //默认设置为集团级数据，与机构无关
        supply.setPkOrg("~");
        List<BdSupplyItem> itemList = supplyAndItem.getItemList();
        String pk = null;//用于存储新增后的主键，调平台时传入
        /** 保存或修改医嘱用法 */
        if (StringUtils.isEmpty(supply.getPkSupply())) {// 保存
            int count_code = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_supply " + "where code = ? and del_flag='0'", Integer.class,
                    supply.getCode());
            int count_name = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_supply " + "where name = ? and del_flag='0'", Integer.class,
                    supply.getName());
            if (count_code != 0) {
                throw new BusException("医嘱用法编码重复！");
            } else if (count_name != 0) {
                throw new BusException("医嘱用法名称重复！");
            } else {
                DataBaseHelper.insertBean(supply);
                //'新增'标志
                rleCode = this.AddState;
                pk = supply.getPkSupply();
            }
        } else {
            int count_code = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_supply where code = ?  and pk_supply != ? and del_flag='0'",
                    Integer.class, supply.getCode(), supply.getPkSupply());
            int count_name = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_supply where name = ?  and pk_supply != ? and del_flag='0'",
                    Integer.class, supply.getName(), supply.getPkSupply());
            int count_order = DataBaseHelper.queryForScalar(
                    "select Count(1) from cn_order where code_supply = ? and del_flag = '0'",
                    Integer.class, supply.getCode());
            BdSupply bdSupply = DataBaseHelper.queryForBean(
                    "select * from bd_supply where pk_supply = ? and del_flag='0'",
                    BdSupply.class, supply.getPkSupply());
            if (count_code != 0) {
                throw new BusException("医嘱用法编码重复！");
            } else if (count_name != 0) {
                throw new BusException("医嘱用法名称重复！");
            } else if (count_order != 0 && bdSupply != null && !"0".equals(supply.getEuRange()) && !supply.getEuRange().equals(bdSupply.getEuRange())) {
                throw new BusException("医嘱用法已经被使用，范围不可修改！");
            } else {
                DataBaseHelper.updateBeanByPk(supply, false);
                //'修改'标志
                rleCode = this.UpdateState;
            }
        }
        /** 保存或修改医嘱用法附加费用 */
        // 没有删除标志，硬删除后再保存
        String pkSupply = supply.getPkSupply();
        DataBaseHelper.execute("delete from bd_supply_item where pk_supply = ?", new Object[]{pkSupply});
        if (CollectionUtils.isNotEmpty(itemList)) {
            for (BdSupplyItem item : itemList) {
                item.setPkSupply(pkSupply);
                DataBaseHelper.insertBean(item);
            }
        }
        /**用法存在缓存中，1.门诊医生站使用，无需查询数据库，2.其他地方大家也可以用，避免交互数据库
		 * */
        String supplySql = "SELECT  BD_SUPPLY.* " +
                "from BD_SUPPLY  " +
                "left join BD_SUPPLY_CLASS on BD_SUPPLY.PK_SUPPLYCATE = BD_SUPPLY_CLASS.PK_SUPPLYCATE  " +
                "where BD_SUPPLY.del_flag='0' and BD_SUPPLY_CLASS.del_flag='0' and BD_SUPPLY_CLASS.code='01' ";
        List<BdSupply> supplyList = DataBaseHelper.queryForList(supplySql, BdSupply.class);
        RedisUtils.setCacheObj("cnop:bdsupply:allinfo", supplyList, 360);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("supply", supply);
        paramMap.put("STATUS", rleCode);//状态
        //发送消息
        PlatFormSendUtils.sendBdSupplyMsg(paramMap);
        return supply;
    }

    /**
     * @param param
     * @param user
     * @return void
     * @throws
     * @Description: 逻辑删除医嘱用法以及医嘱用法附加费用
     * @author wangpeng
     * @date 2016年8月29日
     */
    public void deleteBdSupply(String param, IUser user) {
        BdSupply supply = JsonUtil.readValue(param, BdSupply.class);
        int count_order = DataBaseHelper.queryForScalar(
                "select Count(1) from cn_order where code_supply = ? and del_flag = '0'",
                Integer.class, supply.getCode());
        if (count_order != 0) {
            throw new BusException("该医嘱用法已被引用不可删除！");
        }
        //'删除'标志
        rleCode = this.DelState;
        //删除之前获取将要删除的参数集合
        if ("0".equals(supply.getDelFlag())) {
            //DataBaseHelper.execute("delete from bd_supply_item where pk_supply = ?", new Object[]{supply.getPkSupply()});
            DataBaseHelper.update("update bd_supply set del_flag='1' where pk_supply=? and del_flag='0'",
                    new Object[]{supply.getPkSupply()});
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
            paramMap.put("supply", supply);
            paramMap.put("STATUS", rleCode);//状态
            //发送消息
            PlatFormSendUtils.sendBdSupplyMsg(paramMap);
        } else {
            DataBaseHelper.update("update bd_supply set del_flag='0' where pk_supply=? and del_flag='1'",
                    new Object[]{supply.getPkSupply()});
        }
    }

    /**
     * @param param
     * @param user
     * @return
     * @throws
     * @Description: 根据医嘱用法主键获取医嘱用法及其对应的附加费用列表信息
     * @author wangpeng
     * @date 2016年8月29日
     */
    public BdSupplyAndItemParam getBdSupplyAndItemParam(String param, IUser user) {
        BdSupply supply = JsonUtil.readValue(param, BdSupply.class);
        BdSupplyAndItemParam supplyAndItem = new BdSupplyAndItemParam();
        supply = mkMapper.getBdSupplyById(supply.getPkSupply());
        if (supply != null) {
            supplyAndItem.setSupply(supply);
            List<BdSupplyItem> itemList = new ArrayList<BdSupplyItem>();
            itemList = mkMapper.getBdSupplyItemListBySupplyId(supply.getPkSupply());
            supplyAndItem.setItemList(itemList);
        } else {
            throw new BusException("无法获取医嘱用法信息！");
        }
        return supplyAndItem;
    }

    /**
     * 交易号：001002004014<br>
     *
     * @param param
     * @param user
     * @return
     * @throws
     * @Description: 保存医嘱用法分类信息
     * @author wangpeng
     * @date 2016年8月29日
     */
    public BdSupplyClass saveBdSupplyClass(String param, IUser user) {
        BdSupplyClass bsClass = JsonUtil.readValue(param, BdSupplyClass.class);
        bsClass.setPkOrg("~");//默认设置为集团级数据
        User u = UserContext.getUser();
        if (StringUtils.isEmpty(bsClass.getPkSupplycate())) { // 保存
            int count_code = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_supply_class " + "where code = ? and del_flag='0' ", Integer.class,
                    bsClass.getCode());
            int count_name = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_supply_class " + "where name = ? and del_flag='0' ", Integer.class,
                    bsClass.getName());
            if (count_code != 0) {
                throw new BusException("医嘱用法分类编码重复！");
            } else if (count_name != 0) {
                throw new BusException("医嘱用法分类名称重复！");
            } else {
                bsClass.setDelFlag("0"); // ?
                DataBaseHelper.insertBean(bsClass);
            }
        } else {
            int count_code = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_supply_class " + "where code = ? and del_flag='0' and pk_supplycate != ?",
                    Integer.class, bsClass.getCode(), bsClass.getPkSupplycate());
            int count_name = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_supply_class " + "where name = ? and del_flag='0' and pk_supplycate != ?",
                    Integer.class, bsClass.getName(), bsClass.getPkSupplycate());
            if (count_code != 0) {
                throw new BusException("医嘱用法分类编码重复！");
            } else if (count_name != 0) {
                throw new BusException("医嘱用法分类名称重复！");
            } else {
                bsClass.setDelFlag("0");
                DataBaseHelper.updateBeanByPk(bsClass, false);
            }
        }
        return bsClass;
    }

    /**
     * 交易号：001002004015<br>
     *
     * @param param
     * @param user
     * @return
     * @throws
     * @Description: 根据主键删除医嘱用法分类
     * @author wangpeng
     * @date 2016年8月29日
     */
    public void deleteBdSupplyClass(String param, IUser user) {
        BdSupplyClass bsClass = JsonUtil.readValue(param, BdSupplyClass.class);
        User u = UserContext.getUser();
        String pkSupplycate = bsClass.getPkSupplycate();
        int count = DataBaseHelper.queryForScalar(
                "select Count(1) from bd_supply " + "where pk_supplycate = ? and pk_org = ? and del_flag = '0'",
                Integer.class, pkSupplycate, u.getPkOrg());
        if (count != 0) {
            throw new BusException("存在医嘱用法，无法删除！！");
        }
        DataBaseHelper.execute("delete from bd_supply_class where pk_supplycate = ?", new Object[]{pkSupplycate});
    }

    /**
     * 交易码：001002004017
     *
     * @param param
     * @param user
     * @return
     * @throws
     * @Descciption:根据主键删除科室常用诊断
     * @author xujian
     * @date 2017年7月31日
     */
    public void delCommonDiag(String param, IUser user) {
        BdTermDiagDept diagDept = JsonUtil.readValue(param, BdTermDiagDept.class);
        User u = UserContext.getUser();
        String pkDiagdept = diagDept.getPkDiagdept();
        DataBaseHelper.execute("delete from bd_term_diag_dept where PK_DIAGDEPT = ?", new Object[]{pkDiagdept});
    }

    /**
     * 交易码：001002004016
     *
     * @param param
     * @param user
     * @return
     * @throws
     * @Descciption:保存科室常用诊断维护信息
     * @author xujian
     * @date 2017年7月31日
     */
    public BdTermDiagDept saveCommonDiag(String param, IUser user) {
        BdTermDiagDept diagDept = JsonUtil.readValue(param, BdTermDiagDept.class);
        User u = UserContext.getUser();
        if (StringUtils.isEmpty(diagDept.getPkDiagdept())) {// 保存
            int count_sortno = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_diag_dept where Sortno=? and pk_org=? and pk_dept=?", Integer.class,
                    diagDept.getSortno(), u.getPkOrg(), u.getPkDept());
            int count_nameDiag = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_diag_dept where name_diag=? and pk_org=? and pk_dept=?", Integer.class,
                    diagDept.getNameDiag(), u.getPkOrg(), u.getPkDept());
            int count_pkDiag = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_diag_dept where pk_diag=? and pk_org=? and pk_dept=?", Integer.class,
                    diagDept.getPkDept(), u.getPkOrg(), u.getPkDept());
            int count_spcode = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_diag_dept where spcode=? and pk_org=? and pk_dept=?", Integer.class,
                    diagDept.getSpcode(), u.getPkOrg(), u.getPkDept());
            if (count_sortno != 0) {
                throw new BusException("科室诊断序号重复！");
            } else if (count_nameDiag != 0) {
                throw new BusException("科室诊断名称重复！");
            } else if (count_pkDiag != 0) {
                throw new BusException("对应标准诊断编码重复！");
            } else if (count_spcode != 0) {
                throw new BusException("拼音码重复！");
            } else {
                diagDept.setDelFlag("0");
                diagDept.setPkOrg(u.getPkOrg());
                diagDept.setPkDept(u.getPkDept());
                DataBaseHelper.insertBean(diagDept);
            }
        } else {// 更新
            int count_sortno = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_diag_dept where Sortno=? and pk_org=? and pk_diagdept !=? and pk_dept=?",
                    Integer.class, diagDept.getSortno(), u.getPkOrg(), diagDept.getPkDiagdept(), u.getPkDept());
            int count_nameDiag = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_diag_dept where name_diag=? and pk_org=? and pk_diagdept !=? and pk_dept=?",
                    Integer.class, diagDept.getNameDiag(), u.getPkOrg(), diagDept.getPkDiagdept(), u.getPkDept());
            int count_pkDiag = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_diag_dept where pk_diag=? and pk_org=? and pk_diagdept !=? and pk_dept=?",
                    Integer.class, diagDept.getPkDept(), u.getPkOrg(), diagDept.getPkDiagdept(), u.getPkDept());
            int count_spcode = DataBaseHelper.queryForScalar(
                    "select Count(1) from bd_term_diag_dept where spcode=? and pk_org=? and pk_diagdept !=? and pk_dept=?",
                    Integer.class, diagDept.getSpcode(), u.getPkOrg(), diagDept.getPkDiagdept(), u.getPkDept());
            if (count_sortno != 0) {
                throw new BusException("科室诊断序号重复！");
            } else if (count_nameDiag != 0) {
                throw new BusException("科室诊断名称重复！");
            } else if (count_pkDiag != 0) {
                throw new BusException("对应标准诊断编码重复！");
            } else if (count_spcode != 0) {
                throw new BusException("拼音码重复！");
            } else {
                diagDept.setDelFlag("0");
                DataBaseHelper.updateBeanByPk(diagDept, false);
            }
        }
        return diagDept;
    }

    /**
     * 查询当前科室的诊断数
     * 001002004031
     *
     * @param param
     * @param user
     * @return
     */
    public int getDiagQuantity(String param, IUser user) {
        BdOuDept bdOuDept = JsonUtil.readValue(param, BdOuDept.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pkDept", bdOuDept.getPkDept());
        int count = mkMapper.getDiagQuantity(params);
        return count;
    }

    /**
     * 删除诊断别名
     *
     * @param param
     * @param user
     */
    public void delTermDiagAlias(String param, IUser user) {
        Map map = JsonUtil.readValue(param, Map.class);
        if (map == null) return;
        if (map.get("pkDiagAlias") == null) return;
        mkMapper.delTermDiagAlias(map);
    }

    /**
     * 查询标准诊断编码
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qryTermDiags(String param, IUser user) {
        Map map = JsonUtil.readValue(param, Map.class);
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int pageIndex = Integer.parseInt(map.get("pageIndex").toString());
        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> list = mkMapper.qryTermDiag(map);
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", page.getTotalCount());
        return result;
    }
}
