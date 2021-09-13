package com.zebone.nhis.pro.zsrm.cn.service;

import com.zebone.nhis.cn.pub.vo.OrdBlVo;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.PvDiag;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendProcessor;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service.ZsphPlatFormSendOpHandler;
import com.zebone.nhis.pro.zsrm.cn.dao.CnOpOrdMapper;
import com.zebone.nhis.pro.zsrm.cn.vo.BlCnOpDt;
import com.zebone.nhis.pro.zsrm.cn.vo.OpOrdVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class CnOpOrdService {

    @Autowired
    private CnOpOrdMapper ordDao;

    /**
     * 查询允许非本院执行的医嘱
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryOpOrd(String param, IUser user){
        Map<String,Object> paramMap= JsonUtil.readValue(param,Map.class);
        User u = (User)user;
        if(paramMap==null || !paramMap.containsKey("pkPv") || paramMap.get("pkPv")==null || "".equals(paramMap.get("pkPv").toString()) ){
            throw new BusException("请传入患者就诊主键！");
        }
        StringBuffer sql=new StringBuffer();
        sql.append("  SELECT pk_cnord FROM cn_order ord ");
        sql.append("  INNER JOIN bd_supply sup ON ord.code_supply = sup.code AND sup.eu_attrexten = '1' ");
        sql.append("  WHERE ord.pk_pv = ? ");
        sql.append(" AND EXISTS ( SELECT 1 FROM bl_op_dt cg WHERE ord.pk_cnord = cg.pk_cnord AND cg.flag_settle = '0' AND cg.eu_additem = '1' )  ");

        List<Map<String,Object>> rtnMap=DataBaseHelper.queryForList(sql.toString(),new Object[]{paramMap.get("pkPv").toString()});
        return rtnMap;
    }

    /**
     * 存在非本院执行的医嘱则弹框让医生选择是否本院执行
     * @param param
     * @param user
     */
    public void saveOrd(String param, IUser user){
        OpOrdVo opOrdVo= JsonUtil.readValue(param,OpOrdVo.class);
        if(opOrdVo==null &&  StringUtils.isEmpty(opOrdVo.getPkCnord())){
            throw new BusException("请传入医嘱主键！");
        }

        //查询是否有费用
        String sql=" select * FROM bl_op_dt WHERE pk_cnord in ("+opOrdVo.getPkCnord()+") AND eu_additem = '1' AND flag_settle = '0' ";
        List<Map<String,Object>> list=DataBaseHelper.queryForList(sql);
        if(list==null || list.size()==0) return;

        sql=" DELETE FROM bl_op_dt WHERE pk_cnord in ("+opOrdVo.getPkCnord()+") AND eu_additem = '1' AND flag_settle = '0' ";
        DataBaseHelper.execute(sql);
        sql="UPDATE cn_order SET note_supply = '带走' WHERE pk_cnord in ("+opOrdVo.getPkCnord()+") ";
        DataBaseHelper.update(sql);
    }

    public int qryOrd(String param, IUser user){
        OpOrdVo opOrdVo= JsonUtil.readValue(param,OpOrdVo.class);
        if(opOrdVo==null && StringUtils.isNotEmpty(opOrdVo.getPkPv())){
            throw new BusException("请传入患者就诊主键！");
        }
        String sql="";

        //是否有医嘱
        sql = "select * from CN_ORDER where PK_PV = ?  ";
        List<CnOrder> ords=DataBaseHelper.queryForList(sql,CnOrder.class,new Object[]{opOrdVo.getPkPv()});
        if(ords!=null && ords.size()>0) return 1;

        //判断是否诊断
        sql = "select * from PV_DIAG where PK_PV = ?";
        List<PvDiag> pvDiags=DataBaseHelper.queryForList(sql,PvDiag.class,new Object[]{opOrdVo.getPkPv()});
        if(ords!=null && ords.size()>0) return 1;

        return 0;
    }

    /**
     * 查询患者病历信息
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> qryPvEmr(String param, IUser user){
        OpOrdVo opOrdVo= JsonUtil.readValue(param,OpOrdVo.class);
        if(opOrdVo==null && StringUtils.isNotEmpty(opOrdVo.getPkPv())){
            throw new BusException("请传入患者就诊主键！");
        }
        String sql="";
        //是否有医嘱
        sql = "select * from CN_EMR_OP where PK_PV = ?  ";

        Map<String,Object> rtn=DataBaseHelper.queryForMap(sql,new Object[]{opOrdVo.getPkPv()});

        return  rtn;
    }

    /**
     * 修改病情描述信息
     * @param param
     * @param user
     */
    public void saveNoteDise(String param, IUser user) {
        OpOrdVo opOrdVo = JsonUtil.readValue(param, OpOrdVo.class);
        if (opOrdVo == null && StringUtils.isNotEmpty(opOrdVo.getPkPv())) {
            throw new BusException("请传入患者就诊主键！");
        }
        String sql="";
        //是否有医嘱
        sql = "select * from CN_EMR_OP where PK_PV = ?  ";
        Map<String,Object> rtn=DataBaseHelper.queryForMap(sql,new Object[]{opOrdVo.getPkPv()});

        if(rtn!=null && StringUtils.isNotEmpty(rtn.get("problem").toString())){
            sql="update CN_RIS_APPLY set note_dise=? where pk_ordris in (select b.pk_ordris from CN_ORDER a inner join CN_RIS_APPLY b on a.pk_cnord=b.pk_cnord where a.PK_PV=? and ( b.note_dise is null or TRIM(b.note_dise)='') )";
            DataBaseHelper.update(sql,new Object[]{rtn.get("problem").toString(),opOrdVo.getPkPv()});
        }

    }


    /**
     * 查询药品开立权限
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,String>> qryPdPower(String param, IUser user) {
        OpOrdVo opOrdVo = JsonUtil.readValue(param, OpOrdVo.class);
        List<Map<String,String>> rtnMap=new ArrayList<Map<String,String>>();
        String pkEmp=opOrdVo.getPkEmp();
        if(StringUtils.isEmpty(pkEmp)){
            throw new BusException("请传入开立医生！");
        }
        String pkDept=opOrdVo.getPkDept();
        if(StringUtils.isEmpty(pkDept)){
            throw new BusException("请传入开立科室！");
        }
        if(opOrdVo.getPkPds()==null||opOrdVo.getPkPds().size()==0){
            throw new BusException("请传入查询药品！");
        }
        //查询当前医生抗菌药权限
        String dtAnti="00";//默认无抗菌药权限
        String sql="select * from bd_ou_employee where pk_emp=?";
        BdOuEmployee employee=DataBaseHelper.queryForBean(sql,BdOuEmployee.class,new Object[]{opOrdVo.getPkEmp()});
        if(employee!=null && StringUtils.isNotEmpty(employee.getDtAnti())){
            dtAnti=employee.getDtAnti();
        }

        for (String pkPd:opOrdVo.getPkPds()){
            Map<String,String> pdMap=qryPower(pkPd,qryPd(pkPd,pkDept),qryPdAnti(pkPd),dtAnti);
            rtnMap.add(pdMap);
        }
        return rtnMap;

    }
    private Map<String,String> qryPower(String pkPd,String deptPow,String pdAnti,String dtAnti){
        Map<String,String> powMap=new HashMap<String,String>();
        powMap.put("pkPd",pkPd);
        String flag="0";
        String msg=null;
        //采用或的关系，科室权限或者抗菌药权限任意一个达标则可以开立
        if("1".equals(deptPow) || "3".equals(deptPow)){
            //药品为2限二线 ，医生为00无抗生素权限,01一线
            if("2".equals(pdAnti) && ("00".equals(dtAnti)||"01".equals(dtAnti))){
                flag="1";
                msg="1";
            }
            //药品为 3限三线 ,医生为00无抗生素权限,01一线 , 02二线
            if("3".equals(pdAnti) && ("00".equals(dtAnti)||"01".equals(dtAnti) || "02".equals(dtAnti))){
                flag="1";
                msg="2";
            }
        }

        /********************************************双重限制逻辑**********************************************/
        //科室不控制,当药品限制科室使用时并且当前科室有权限时
//        if("1".equals(deptPow) || "2".equals(deptPow)){
//            //药品为2限二线 ，医生为00无抗生素权限,01一线
//            if("2".equals(pdAnti) && ("00".equals(dtAnti)||"01".equals(dtAnti))){
//                flag="1";
//                msg="1";
//            }
//            //药品为 3限三线 ,医生为00无抗生素权限,01一线 , 02二线
//            if("3".equals(pdAnti) && ("00".equals(dtAnti)||"01".equals(dtAnti) || "02".equals(dtAnti))){
//                flag="1";
//                msg="2";
//            }
//        }
//        //当药品限制科室使用时并且当前科室无权限时
//        if("3".equals(deptPow)){
//            //药品不限制
//            if("1".equals(pdAnti)){
//                flag="1";
//                msg="5";
//            }
//            //药品为2限二线 ，医生为00无抗生素权限,01一线
//            if("2".equals(pdAnti) && ("00".equals(dtAnti)||"01".equals(dtAnti))){
//                flag="1";
//                msg="3";
//            }
//            //药品为 3限三线 ,医生为00无抗生素权限,01一线 , 02二线
//            if("3".equals(pdAnti) && ("00".equals(dtAnti)||"01".equals(dtAnti) || "02".equals(dtAnti))){
//                flag="1";
//                msg="4";
//            }
//        }
        /********************************************双重限制逻辑**********************************************/

        powMap.put("msg",msg);
        powMap.put("flag",flag);
        return powMap;
    }

    /**
     * 查询药品是否能被当前抗菌药权限开立
     * @param pkPd 药品主键
     * @return
     */
    private String qryPdAnti(String pkPd){
        String isItAllowed="1";//是否允许开立
        String dtAntiPd="1";//药品限制类型
        String sql="SELECT  bat.pk_pd,bat.val_att FROM bd_pd_att bat INNER JOIN bd_pd_att_define def ON bat.pk_pdattdef=def.pk_pdattdef " +
                " WHERE def.code_att = '0518' AND bat.del_flag = '0' AND def.del_flag = '0'"+
                " and bat.pk_pd ='"+pkPd+"'";
        List<Map<String,Object>> list=DataBaseHelper.queryForList(sql);
        if(list!=null && list.size()>0){
            dtAntiPd= MapUtils.getString(list.get(0),"valAtt");
        }
        dtAntiPd=StringUtils.isEmpty(dtAntiPd)?"1":dtAntiPd;
        return dtAntiPd;
    }

    /**
     * 科室限制用药
     * @param pkPd
     * @param pkDept
     * @return
     */
    private String qryPd(String pkPd,String pkDept){
        String isItAllowed="1";//是否允许开立,默认不控制
        String sql="select (case when count(1) > 0 then (case when \n" +
                "(select count(1) from bd_pd_rest where eu_ctrltype = '1' and pk_pd = bpr.pk_pd and pk_dept = ? ) > 0 \n" +
                " then '2' else '3' end) else '1' end) flag\n" +
                "from bd_pd_rest bpr where bpr.eu_ctrltype = '1'  and bpr.pk_pd = ?  group by bpr.pk_pd";

        List<Map<String,Object>> list=DataBaseHelper.queryForList(sql,new Object[]{pkDept,pkPd});
        if(list!=null && list.size()>0){
            isItAllowed=MapUtils.getString(list.get(0),"flag");
        }
        isItAllowed=StringUtils.isEmpty(isItAllowed)?"1":isItAllowed;
        return isItAllowed;
    }

    /**
     * 申请医嘱退费
     * @param param
     * @param user
     */
    public void saveOrdErase(String param, IUser user) {
        OpOrdVo opOrdVo = JsonUtil.readValue(param, OpOrdVo.class);
        User u=(User)user;
        List<CnOrder> orders=opOrdVo.getOrders();
        if(orders==null||orders.size()==0){
            throw new BusException("请传入需要作废的医嘱数据");
        }
        //反查当前申请退费医嘱有无一代支付
        //List<CnOrder> newOrds = qryOrds(orders,u);
        //重新赋值，防止空数据
        if("1".equals(opOrdVo.getFlagRefund())){
            for (CnOrder ord : orders){
                ord.setPkEmpErase(u.getPkEmp());
                ord.setNameEmpErase(u.getNameEmp());
                ord.setDateErase(new Date());
                ord.setFlagErase("1");
            }
            DataBaseHelper.batchUpdate(" update cn_order set pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,flag_erase=:flagErase,quan_back=:quanBack,DATE_ERASE=:dateErase,DT_REASON_ERASE=:dtReasonErase where pk_cnord=:pkCnord",orders);

        }else{
            DataBaseHelper.batchUpdate(" update cn_order set pk_emp_erase=null,name_emp_erase=null,flag_erase='0',quan_back='0',DATE_ERASE=null,DT_REASON_ERASE=null where pk_cnord=:pkCnord",orders);

        }

    }

    private List<CnOrder> qryOrds(List<CnOrder> orders,User u){
        //1.先查询含有一代支付的支付主键
        String sql = "select ord.* from CN_ORDER ord INNER JOIN BL_OP_DT dt on dt.CN_ORDER=pay.CN_ORDER where dt.pk_settle= ? ";
        List<String> pkCnords=new ArrayList<String>();
        List<CnOrder> newOrds=new ArrayList<CnOrder>();

        newOrds.addAll(orders);//全部加进去
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("orders",orders);
        List<BlOpDt> blOpDts=ordDao.qryBlOrd(map);
        //存在一代支付类型
        map.put("blOpDts",blOpDts);
        if(blOpDts!=null && blOpDts.size()>0){
            map.put("blOpDts",blOpDts);
            List<CnOrder> newOrds1=ordDao.qryOrdBySett(map);
            if(newOrds1!=null && newOrds1.size()>0){
                for (CnOrder ord :newOrds1){
                    ord.setPkEmpErase(u.getPkEmp());
                    ord.setNameEmpErase(u.getNameEmp());
                    ord.setFlagErase("1");
                    ord.setQuanBack(ord.getQuanCg().toString());
                }
                newOrds.addAll(newOrds1);
            }
        }

        return newOrds;

    }
    /**
     * 查询医嘱收费方式
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryPayType(String param, IUser user) {
        OpOrdVo opOrdVo = JsonUtil.readValue(param, OpOrdVo.class);
        List<CnOrder> orders=opOrdVo.getOrders();
        if(orders==null||orders.size()==0){
            throw new BusException("请传入需要退费的医嘱数据");
        }

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("orders",orders);
        List<BlOpDt> blOpDts=ordDao.qryBlOrd(map);
        List<Map<String,Object>> rtnMap=new ArrayList<Map<String,Object>>();
        if(blOpDts!=null && blOpDts.size()>0){
            boolean ydk=false;
            for (BlOpDt dt :blOpDts){
                Map<String,Object> map1=new HashMap<String,Object>();
                map.put("pkConrd",dt.getPkCnord());
                map.put("ydk","1");
                rtnMap.add(map);
            }
        }
        return rtnMap;
    }

    private boolean qrySettle(String pkConrd){
        boolean ydk=false;
        //人医-一代社保卡支付订单校验
        String sql = "select pay.* from bl_ext_pay pay INNER JOIN BL_OP_DT dt on dt.pk_settle=pay.pk_settle where dt.PK_CNORD= ? ";
        List<BlExtPay> payList = DataBaseHelper.queryForList(sql, BlExtPay.class,pkConrd);
        if(payList != null && payList.size() > 0) {
            for (BlExtPay blExtPay : payList) {
                String sysname = "";
                switch (blExtPay.getEuPaytype()) {
                    case "12":
                        sysname = "一代社保卡";
                        break;
                    case "0601":
                        sysname = "一代社保卡(自助机)";
                        break;
                    default:
                        break;
                }
                if (!CommonUtils.isEmptyString(sysname)) {
                    ydk=true;
                }
            }
        }
        return ydk;
    }

    /***患者综合查询逻辑***/

    /**
     * 查询患者本次就诊医嘱信息
     * @param param
     * @param user
     * @return
     */
    public List<OrdBlVo> qryOrdAllInf(String param, IUser user){
        OpOrdVo opOrdVo = JsonUtil.readValue(param, OpOrdVo.class);
        if(opOrdVo==null || StringUtils.isEmpty(opOrdVo.getPkPv())){
            throw new BusException("请传入当前患者就诊主键！！！");
        }
        List<OrdBlVo> ordBlVos=new ArrayList<OrdBlVo>();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("pkPv",opOrdVo.getPkPv());
        //1.查询当前患者的医嘱
        ordBlVos=ordDao.qryOrd(map);

        if(ordBlVos==null||ordBlVos.size()==0) return null;

        //2.查询收费状态及费用
        List<OrdBlVo> ordBlinfos=ordDao.qryBlByPv(map);

        map.put("orders",ordBlVos);
        //3.查询收费状态及费用
        List<OrdBlVo> ordBls=ordDao.qryBlByOrd(map);

        //4.查询医嘱执行状态
        List<OrdBlVo> ordExinfos=ordDao.qryOrdEx(map);

        //5.查询当前医嘱有效收费主键
        List<BlCnOpDt> blOpDts=ordDao.qryBlSettle(map);

        //6.处理业务逻辑，拼接数据
        handlingBusinessOrd(ordBlVos,ordBlinfos,ordBls,ordExinfos,blOpDts);

        return ordBlVos;
    }

    private void handlingBusinessOrd(List<OrdBlVo> ordBlVos,List<OrdBlVo> ordBlinfos,List<OrdBlVo> ordBls,List<OrdBlVo> ordExinfos,List<BlCnOpDt> blOpDts ){
        //以医嘱表作为主表循环
        double zero=0;
        for (OrdBlVo vo :ordBlVos){
            vo.setAmountCg(0d);//默认给0
            vo.setFlagSettle("0");//默认未收费
            vo.setFlagEx("0");//默认未执行
            //收费金额
            if(ordBlinfos!=null && ordBlinfos.size()>0){
                for (OrdBlVo bl :ordBlinfos){
                    if(vo.getPkCnord().equals(bl.getPkCnord())){
                        vo.setAmountCg(bl.getAmount());
                        break;
                    }
                }
            }
            //收费状态
            if(ordBls!=null && ordBls.size()>0){
                for (OrdBlVo bl :ordBls){
                    if(vo.getPkCnord().equals(bl.getPkCnord())){
                        //部分退费，或未退费，则为收费状态
                        if(bl.getExSum()!=0 && bl.getExSum()>bl.getRefund() || bl.getRefund()==zero){
                            vo.setFlagSettle("1");
                            break;
                        }else{ //其他情况为退费状态
                            vo.setFlagSettle("3");
                            break;
                        }
                    }
                }
            }
            //执行状态
            if(ordExinfos!=null && ordExinfos.size()>0){
                for (OrdBlVo ex :ordExinfos){
                    if(vo.getPkCnord().equals(ex.getPkCnord())){
                        if("1".equals(vo.getFlagDurg())){//药品类型
                            if(ex.getExSum()>zero){
                                vo.setFlagEx("1");//已发药
                                break;
                            }
                            //退药与发药记录同时存在的情况，以退药状态为准
                            if(ex.getRefund()>zero){
                                vo.setFlagEx("2");//已退药
                                break;
                            }
                        }else{ //非药品类型
                            if(ex.getExSum()==zero){
                                vo.setFlagEx("0");//未执行
                                break;
                            }
                            if(ex.getExSum()!=zero && ex.getRefund()==zero && ex.getOcc()==zero){
                                vo.setFlagEx("0");//未执行
                                break;
                            }
                            if(ex.getExSum()!=zero && ex.getRefund()==zero && ex.getOcc()==ex.getExSum()){
                                vo.setFlagEx("1");//已执行
                                break;
                            }
                            if(ex.getExSum()!=zero && ex.getOcc()==zero && ex.getRefund()==ex.getExSum()){
                                vo.setFlagEx("2");//已取消
                                break;
                            }

                        }
                        vo.setFlagEx("3");//其他情况

                        break;
                    }
                }
            }
            //收费主键
            if(blOpDts!=null && blOpDts.size()>0){
                for(BlCnOpDt blOp:blOpDts){
                    if(blOp.getPkCnord().equals(vo.getPkCnord())){
                        vo.setPkSettle(blOp.getPkSettle());
                        vo.setDtPaymode(blOp.getDtPaymode());
                        break;
                    }
                }
            }
        }
    }
    /**
     * 查询患者历史就诊医嘱信息
     * @param param
     * @param user
     * @return
     */
    public List<OrdBlVo> qryOrdAllHis(String param, IUser user){
        OpOrdVo opOrdVo = JsonUtil.readValue(param, OpOrdVo.class);
        if(opOrdVo==null || StringUtils.isEmpty(opOrdVo.getPkPv())){
            throw new BusException("请传入当前患者就诊主键！！！");
        }
        List<OrdBlVo> ordBlVos=new ArrayList<OrdBlVo>();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("pkPv",opOrdVo.getPkPv());
        //1.查询当前患者的医嘱
        ordBlVos=ordDao.qryOrdHis(map);

        if(ordBlVos==null||ordBlVos.size()==0) return null;

        //2.查询收费状态及费用
        List<OrdBlVo> ordBlinfos=ordDao.qryBlByPv(map);

        map.put("orders",ordBlVos);
        //3.查询收费状态及费用
        List<OrdBlVo> ordBls=ordDao.qryBlByOrd(map);

        //4.查询医嘱执行状态
        List<OrdBlVo> ordExinfos=ordDao.qryOrdEx(map);

        //5.查询当前医嘱有效收费主键
        List<BlCnOpDt> blOpDts=ordDao.qryBlSettle(map);

        //6.处理业务逻辑，拼接数据
        handlingBusinessOrd(ordBlVos,ordBlinfos,ordBls,ordExinfos,blOpDts);

        return ordBlVos;
    }

    /***患者综合查询逻辑***/

    /**患者药品查询逻辑**/
    /**
     * 根据患者主键和药品判断查询当前药品是否属于患者谈判抗癌药品
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryPdByPiKA(String param,IUser user){
        OpOrdVo opOrdVo = JsonUtil.readValue(param, OpOrdVo.class);
        if(opOrdVo==null || StringUtils.isEmpty(opOrdVo.getPkPi())){
            throw new BusException("请传入当前患者就诊主键！！！");
        }

        if(opOrdVo.getPkPds()==null || opOrdVo.getPkPds().size()==0){
            throw new BusException("请传入需要查询的药品信息！！！");
        }
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("pkPi",opOrdVo.getPkPi());
        map.put("pkPds",opOrdVo.getPkPds());
        //1.查询当前患者的医嘱
        return  ordDao.qryPdKA(map);
    }
    /**患者药品查询逻辑**/

    /*****************患者费用查询  start ****************/
    public Map<String,Object> qryPiBlOrd(String param , IUser user){
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        String pkPv=MapUtils.getString(paramap,"pkPv");
        //无参数不查询
        if(StringUtils.isEmpty(pkPv)){
            return null;
        }

        int pageIndex = org.apache.commons.collections.MapUtils.getIntValue(paramap, "pageIndex",1);
        int pageSize = org.apache.commons.collections.MapUtils.getIntValue(paramap, "pageSize",50);

        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> searchPv = ordDao.qryBlOrdDetil(paramap);//cnOpPiDao.searchPv(paramap);
        Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
        Map<String, Object> result = new HashMap<>();
        result.put("list", searchPv);
        result.put("totalCount", page.getTotalCount());
        return result;
    }

    public Map<String,Object> qryBlPv(String param , IUser user){
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        String pkPv=MapUtils.getString(paramap,"pkPv");
        //无参数不查询
        if(StringUtils.isEmpty(pkPv)){
            return null;
        }
        //查询总费用
        Map<String, Object> pvSum=ordDao.qryBlSumPv(paramap);
        return pvSum;
    }
    /*****************患者费用查询  end ****************/

    /************************* 模板 start **************************/
    /**
     * 查询医嘱模板明细
     * trans code
     *
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<Map<String, Object>> getTempDtList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pid = org.apache.commons.collections.MapUtils.getString(paramMap, "pid");
        String codeSet = org.apache.commons.collections.MapUtils.getString(paramMap, "codeSet");
        if (StringUtils.isEmpty(pid) && StringUtils.isEmpty(codeSet)) {
            throw new BusException("请传入模板主键！！！");
        }

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        //查询非药品
        List<Map<String, Object>> ll = ordDao.qryBdOrd(paramMap);
        ret.addAll(ll);
        //查询药品
        List<Map<String, Object>> mm = ordDao.qryBdOrdDrug(paramMap);
        ret.addAll(mm);
        return ret;
    }

    /************************* 模板 end **************************/

    /***
     * 门诊接诊校验患者是否死亡
     * @param param{"patientNo":"患者Id"，"orderCode":"死亡医嘱项目编码（010006）"}
     * @param user
     * @return
     */
    public boolean checkPiDeathOrder(String param,IUser user){
        Map<String,Object> paramMap=JsonUtil.readValue(param,Map.class);
        boolean isDeath= ServiceLocator.getInstance().getBean(ZsphPlatFormSendOpHandler.class).sendMedicalAdviceInfo(paramMap);
        return isDeath;
    }
}
