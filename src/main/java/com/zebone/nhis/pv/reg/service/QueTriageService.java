package com.zebone.nhis.pv.reg.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.common.module.arch.BdArchSrvconf;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.transcode.SysServiceRegister;
import com.zebone.nhis.common.module.pv.PvQue;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pv.reg.dao.QueTriageMapper;
import com.zebone.nhis.pv.reg.vo.AdjustePosiVo;
import com.zebone.nhis.pv.reg.vo.ChangeQueVo;
import com.zebone.nhis.pv.reg.vo.PvQueVO;
import com.zebone.nhis.pv.reg.vo.TransParamVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 分诊排队
 *
 * @author haohan
 */
@Service
public class QueTriageService {
	
	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
    @Autowired
    private QueTriageMapper triageMapper;

    /**
     * 查询当前用户所属分诊台 003003001028
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPlatformDef(String param, IUser user) {

        @SuppressWarnings("unchecked")
        Map<String, Object> qryParam = JsonUtil.readValue(param, Map.class);
        String sql = "select pf.*,pe.pk_user,br.flag_dise,br.flag_appt,br.sortno_over,br.cnt_wait,br.cnt_conte " + "from BD_QC_PLATFORM pf " + "inner join BD_QC_PERM pe on pe.PK_QCPLATFORM=pf.PK_QCPLATFORM " + "left join bd_qc_rule br on br.pk_qcrule=pf.pk_qcrule_def " + "where pe.PK_USER=? and pf.pk_orgarea=?";
        if ("1".equals(qryParam.get("flagDef"))) {// 默认分诊台
            sql += " and pe.flag_def=1 ";
        }
        List<Map<String, Object>> qcPlatform = DataBaseHelper.queryForList(sql, UserContext.getUser().getId(), qryParam.get("pkOrgarea"));
        return qcPlatform;
    }

    /**
     * 查询当前诊台所属的队列 003003001014
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> qryQueByplatForm(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> qryParam = JsonUtil.readValue(param, Map.class);
        qryParam.put("dateWork", new Date());
        List<Map<String, Object>> queInfos = triageMapper.qryQueByplatForm(qryParam);
        return queInfos;
    }

    /**
     * 查询挂号信息 003003001015
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> qryRegisterInfo(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> qryParam = JsonUtil.readValue(param, Map.class);
        
        //获取系统参数PV0023
        String pv0023 = ApplicationUtils.getSysparam("PV0023", false, "请维护好系统参数PV0023！");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, Integer.parseInt(pv0023));
        Date dateBegin = calendar.getTime();
        
        qryParam.put("dateBegin", DateUtils.getDateTimeStr(dateBegin));
        qryParam.put("dateEnd", DateUtils.getDateTimeStr(new Date()));
        
        List<Map<String, Object>> registerInfos = triageMapper.qryRegisterInfo(qryParam);
        return registerInfos;
    }

    /**
     * 分诊签到 003003001017
     *
     * @param param
     * @param user
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void piSignIn(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        PvQueVO pvQueVO = JsonUtil.readValue(param, PvQueVO.class);
        Integer count = DataBaseHelper.queryForScalar("select count(1) from pv_que where pk_pvque=? and flag_cancel='0' ", Integer.class, pvQueVO.getPkPvque());
        if (count > 0) {
            throw new BusException("该患者已分诊，请刷新页面！");
        }
        chkAdvanceTime(pvQueVO);

        // 如果资源类型为科室，根据科室下出诊医生待诊人数自动分配
        qryDr(pvQueVO);

        // 根据病情评估等级生成“排序号”和“调整排队序号”
        pvQueVO.setDateBegin(DateUtils.getDateStr(new Date()) + "000000");
        pvQueVO.setDateEnd(DateUtils.getDateStr(new Date()) + "235959");
        
        Map<String, Object> noMap = triageMapper.qryNo(pvQueVO);
        if ("1".equals(pvQueVO.getLevelDise()) || "2".equals(pvQueVO.getLevelDise())) {
            pvQueVO.setSortnoAdj(100);
        } else {
            pvQueVO.setSortnoAdj(Integer.parseInt(noMap.get("sortnoAdj").toString()));
        }
        pvQueVO.setSortno(Integer.parseInt(noMap.get("sortno").toString()));

        PvQue inPam = new PvQue();
        BeanUtils.copyProperties(inPam, pvQueVO);
        //关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def); 
	    try{
	    	DataBaseHelper.insertBean(inPam);
		    platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
		    e.printStackTrace();
			throw new RuntimeException("分诊签到失败：" + e.getMessage());
		}
    }

    /**
     * 校验分诊签到可提前的时间
     */
    private void chkAdvanceTime(PvQueVO pvQueVO) {
        //获取系统参数PV0023
        String pv0023 = ApplicationUtils.getSysparam("PV0023", false, "请维护好系统参数PV0023！");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pvQueVO.getExpectTime());
        calendar.add(Calendar.MINUTE, -Integer.parseInt(pv0023));//Integer.parseInt(pv0023)
        Date expTime = calendar.getTime();
        if (new Date().compareTo(expTime) < 0) {
            throw new BusException("仅允许提前" + pv0023 + "分钟签到！");
        }
    }

    /**
     * 资源类型为科室时，自动分配出诊医生
     *
     * @param pvQueVO
     */
    private void qryDr(PvQueVO pvQueVO) {
        if ("0".equals(pvQueVO.getEuRestype())) {
            List<Map<String, Object>> empInfos = triageMapper.qryEmpToAutoDistribute(pvQueVO.getPkSchres(), pvQueVO.getPkDateslot(), (DateUtils.getDateStr(new Date()) + "000000"));
            if (empInfos == null || empInfos.size() <= 0) {
                throw new BusException("未查询到该科室下的出诊医生！");
            } else {
                pvQueVO.setPkEmpPhy(empInfos.get(0).get("pkEmp").toString());
                pvQueVO.setNameEmpPhy(empInfos.get(0).get("nameEmp").toString());
                pvQueVO.setPkSchres(empInfos.get(0).get("pkSchres").toString());
                pvQueVO.setPkSch(empInfos.get(0).get("pkSch").toString());
            }
        }
    }

    /**
     * 查询当前院区下的分诊台 003003001029
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPlatFormUnderArea(String param, IUser user) {
        String pkOraArea = JsonUtil.getFieldValue(param, "pkOrgarea");
        return DataBaseHelper.queryForList("select * from bd_qc_platform where pk_orgarea=? ", pkOraArea);
    }

    /**
     * 查询分诊台下的排班 003003001018
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> qrySchByPlatForm(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> pkQcplatform = JsonUtil.readValue(param, Map.class);
        pkQcplatform.put("dateWork", DateUtils.getDateStr(new Date()) + "000000");
        pkQcplatform.put("timeBegin", DateUtils.getTimeStr(new Date()));
        pkQcplatform.put("timeEnd", DateUtils.getTimeStr(new Date()));
        List<Map<String, Object>> schs = triageMapper.qrySchByPlatForm(pkQcplatform);
        return schs;
    }

    /**
     * 查询转科列表
     * 003003001034
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryTransDept(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> pkQcplatform = JsonUtil.readValue(param, Map.class);
        pkQcplatform.put("dateWork", DateUtils.getDateStr(new Date()) + "000000");
        pkQcplatform.put("timeBegin", DateUtils.getTimeStr(new Date()));
        pkQcplatform.put("timeEnd", DateUtils.getTimeStr(new Date()));
        List<Map<String, Object>> schs = triageMapper.qryTransDept(pkQcplatform);
        return schs;
    }

    /**
     * 转科处理 003003001019
     *
     * @param param
     * @param user
     */
    public void transDept(String param, IUser user) {
        TransParamVo transParam = JsonUtil.readValue(param, TransParamVo.class);
        // 先根据资源获取关联的医生
        BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean("select res.pk_emp,emp.name_emp from sch_resource res inner join bd_ou_employee emp on res.pk_emp=emp.pk_emp where res.pk_schres=?", BdOuEmployee.class, new Object[]{transParam.getPkSchres()});

        // 然后执行转科；
        // 更新pv_encounter表
        DataBaseHelper.execute("update pv_encounter set pk_dept=? where pk_pv=?", new Object[]{transParam.getPkDept(), transParam.getPkPv()});

        // 更新门诊就诊pv_op
        triageMapper.updatePvOp(transParam, bdOuEmployee);
    }

    /**
     * 查询排队信息 003003001020
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> qryQueinfo(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> qryParam = JsonUtil.readValue(param, Map.class);
        qryParam.put("dateBegin", DateUtils.getDateTimeStr(new Date()));
        qryParam.put("dateEnd", DateUtils.getDateTimeStr(new Date()));
        List<Map<String, Object>> queInfos = triageMapper.qryQueinfo(qryParam);
        return queInfos;
    }

    /**
     * 分诊叫号处理 003003001021
     *
     * @param param
     * @param user
     */
    public void callNumber(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        // 1.调用接口输出叫号数据

        // 2.分诊处理，更新pv_que
        DataBaseHelper.execute("update pv_que set eu_status='1',date_arr=?,pk_emp_arr=?,name_emp_arr=? where pv_que.pk_pv=? and eu_status='0'", new Object[]{new Date(), UserContext.getUser().getPkEmp(), UserContext.getUser().getNameEmp(), pkPv});
    }

    /**
     * 过号处理 003003001022
     *
     * @param param
     * @param user
     */
    public void overNumber(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        DataBaseHelper.execute("update pv_que set eu_status='8',date_over=?,cnt_over=cnt_over+1 where pv_que.pk_pv=? and eu_status='1' ", new Object[]{new Date(), pkPv});
    }

    /**
     * 调整位置 003003001023
     *
     * @param param
     * @param user
     */
    public void adjustePosition(String param, IUser user) {
        AdjustePosiVo pvParam = JsonUtil.readValue(param, AdjustePosiVo.class);

        String sql = "select count(1) from pv_que where pk_pvque=? and ts=?";
        Integer curCount = DataBaseHelper.queryForScalar(sql, Integer.class, pvParam.getCurPvQue().getPkPvque(), pvParam.getCurPvQue().getTs());
        if (curCount <= 0) {
            throw new BusException("该患者的排队信息已发生改变，请刷新页面！");
        }
        Integer aimCount = DataBaseHelper.queryForScalar(sql, Integer.class, pvParam.getAimPvQue().getPkPvque(), pvParam.getAimPvQue().getTs());
        if (aimCount <= 0) {
            throw new BusException("目标患者的排队信息已发生改变，请刷新页面！");
        }

        // 1） 取目标位置-1的sortno_adj值x；
        // 2） 判断目标位置的sortno_adj-x是否大于1，大于1可调整位置，否则不能调整；
        // 3） 计算目标位置的sortno_adj值y=sortno_adj+(sortno_adj-x)/2；
        // 4） 更新原位置的sortno_adj=y
        int x = pvParam.getPrePvQue() == null ? 0 : pvParam.getPrePvQue().getSortnoAdj();
        if (pvParam.getAimPvQue().getSortnoAdj() - x > 1) {
            int y = (int) (pvParam.getAimPvQue().getSortnoAdj() - Math.ceil((pvParam.getAimPvQue().getSortnoAdj() - x) / 2.0));
            DataBaseHelper.execute("update pv_que set sortno_adj=? where pk_pvque=?", new Object[]{y, pvParam.getCurPvQue().getPkPvque()});
        } else {
            throw new BusException("此位置已无空档，请修改要调整的位置！");
        }
    }

    /**
     * 取消签到 003003001024
     *
     * @param param
     * @param user
     */
    public void cancelSignIn(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        DataBaseHelper.execute("update pv_que set eu_status='9',flag_cancel='1' where pk_pv=? and eu_status='0' and flag_cancel='0'", pkPv);
    }

    /**
     * 查询可转发的队列 003003001025
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> qrychangeQue(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> qryChangeParam = JsonUtil.readValue(param, Map.class);
        qryChangeParam.put("dateWork", DateUtils.getDateStr(new Date()) + "000000");
        qryChangeParam.put("timeBegin", DateUtils.getTimeStr(new Date()));
        qryChangeParam.put("timeEnd", DateUtils.getTimeStr(new Date()));
        List<Map<String, Object>> canChangeQues = triageMapper.qryChangeQue(qryChangeParam);
        return canChangeQues;
    }

    /**
     * 转发队列 基于pk_pv更新pv_que表； 1.重新生成获取所选队列的排序号； 2.重新生成所选队列的调整排序号；
     * 3.更新出诊资源pk_schres； 4.更新出诊医生 pk_emp_phy； 5.更新出诊医生姓名 name_emp_phy； 6.修改状态
     * eu_status=0； 7.更新签到日期date_sign为当前日期； 8.更新签到人员pk_emp_sign为当前用户； 9.更新签到人员姓名
     * name_emp_phy。 003003001026
     *
     * @param param
     * @param user
     */
    public void changeQue(String param, IUser user) {
        ChangeQueVo changeParam = JsonUtil.readValue(param, ChangeQueVo.class);
        
        Map<String, Object> updateParam = triageMapper.qryNo(changeParam.getPvQueVo());
        if ("1".equals(changeParam.getPvQueVo().getLevelDise()) || "2".equals(changeParam.getPvQueVo().getLevelDise())) {
            updateParam.put("sortnoAdj", 100);
        }
        qryDr(changeParam.getPvQueVo());
        updateParam.put("pkSchres", changeParam.getDeptAndResource().getPkSchres());
        updateParam.put("pkEmpPhy", changeParam.getPvQueVo().getPkEmpPhy());
        updateParam.put("nameEmpPhy", changeParam.getPvQueVo().getNameEmpPhy());
        updateParam.put("pkPv", changeParam.getPvQueVo().getPkPv());
        updateParam.put("pkPvque", changeParam.getPvQueVo().getPkPvque());
        updateParam.put("dateSign", new Date());
        updateParam.put("pkEmpSign", UserContext.getUser().getPkEmp());
        updateParam.put("nameEmpSign", UserContext.getUser().getNameEmp());
        triageMapper.changeQue(updateParam);
    }

    /**
     * 查询就诊信息 003003001027
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPvInfo(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> qryParam = JsonUtil.readValue(param, Map.class);
        qryParam.put("dateBegin", DateUtils.getDateTimeStr(new Date()));
        qryParam.put("dateEnd", DateUtils.getDateTimeStr(new Date()));
        List<Map<String, Object>> pvInfos = triageMapper.qryPvInfo(qryParam);
        return pvInfos;
    }

    /**
     * 续诊签到 1）读取关联的分诊规则属性值：cnt_wait（候诊基数）和cn_conte（续诊人数）；
     * 2）按照间隔cnt_wait个候诊人数，插入cnt_conte个续诊人数的规则插入续诊患者。 写表pv_que 003003001032
     *
     * @param param
     * @param user
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void signInAgain(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        PvQueVO pvQueVO = JsonUtil.readValue(param, PvQueVO.class);
        Integer count = DataBaseHelper.queryForScalar("select count(1) from pv_que where pk_pv=? and ts=?", Integer.class, pvQueVO.getPkPv(), pvQueVO.getTs());
        if (count <= 0) {
            throw new BusException("该患者就诊信息改变，请刷新页面！");
        }
        Integer pvCount = triageMapper.isSeek(pvQueVO.getPkPv(), new Date());
        if (pvCount > 0) {
            throw new BusException("该患者已续诊，请刷新页面！");
        }

        pvQueVO.setDateBegin(DateUtils.getDateStr(new Date()) + "000000");
        pvQueVO.setDateEnd(DateUtils.getDateStr(new Date()) + "235959");
        
        String sqlPart = "";
        //是否启用预约优先
        if ("1".equals(pvQueVO.getFlagAppt())) {
            sqlPart = " order by que.eu_source desc,que.SORTNO_ADJ,que.SORTNO ";
        } else {
            sqlPart = " order by que.SORTNO_ADJ,que.SORTNO ";
        }
        String sql = "select * from PV_QUE que inner join pv_op pp on pp.pk_pv=que.pk_pv and pp.date_begin <= ? and pp.date_end >= ?"
                + " where que.pk_qcque=? and que.eu_status='0' and que.flag_cancel='0'" + sqlPart;
        List<PvQueVO> pvInfos = DataBaseHelper.queryForList(sql, PvQueVO.class, new Object[]{new Date(), new Date(), pvQueVO.getPkQcque()});
        
        int cntWait = pvQueVO.getCntWait();//候诊基数
        int cntConte = pvQueVO.getCntConte();//续诊人数
        int cntWatiNext = 0;//下一个候诊基数（默认第一段为0） 下一段cntWait+cntWatiNext....依此类推
        int cntConteNext = 0;//下一段续诊人数（默认第一段为0）下一段cntConte+cntConteNext....依此类推
        //根据规则获取目标位置（按照间隔cnt_wait个候诊人数，插入cnt_conte个续诊人数的规则插入续诊患者）如果为0插入队伍最后
        int targetPosition = targetPosition(pvInfos,cntWatiNext,cntWait,cntConteNext,cntConte);
        	
        if(targetPosition == 0){//取已排队最后一人排序号+10(排最后一位按签到规则生成)
            Map<String, Object> noMap = triageMapper.qryNo(pvQueVO);
            pvQueVO.setSortnoAdj(Integer.parseInt(noMap.get("sortnoAdj").toString()));
        }else{
        	//目标位置上一个排队人号码
        	int preAimAdjNo = pvInfos.get(targetPosition - 1).getSortnoAdj();
        	pvQueVO.setSortnoAdj(preAimAdjNo + 1);
        }
        /*
        Map<String, Object> noMap = triageMapper.qryNo(pvQueVO);
        if ("1".equals(pvQueVO.getLevelDise()) || "2".equals(pvQueVO.getLevelDise())) {
            pvQueVO.setSortnoAdj(100);
        } else {
            pvQueVO.setSortnoAdj(Integer.parseInt(noMap.get("sortnoAdj").toString()));
        }
        pvQueVO.setSortno(Integer.parseInt(noMap.get("sortno").toString()));
        pvQueVO.setSortnoAdj(Integer.parseInt(noMap.get("sortnoAdj").toString()));
        */
        pvQueVO.setSortno(pvQueVO.getSortno());

        pvQueVO.setDateSign(new Date());
        pvQueVO.setPkEmpSign(UserContext.getUser().getPkEmp());
        pvQueVO.setNameEmpSign(UserContext.getUser().getNameEmp());
        
        // 如果资源类型为科室，根据科室下出诊医生待诊人数自动分配
        qryDr(pvQueVO);
        PvQue inPam = new PvQue();
        BeanUtils.copyProperties(inPam, pvQueVO);
        inPam.setEuStatus("0");
        inPam.setEuType("1");
        
        //关闭事务自动提交
  		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
  		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
  		TransactionStatus status = platformTransactionManager.getTransaction(def); 
  	    try{
  	    	DataBaseHelper.insertBean(inPam);
  		    platformTransactionManager.commit(status); // 提交事务
  		} catch (Exception e) {
  			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
  		    e.printStackTrace();
  			throw new RuntimeException("续诊签到失败：" + e.getMessage());
  		}
    }
    
    /**
     * 
     * @param pvInfos 目前排队候诊人数
     * @param cntWati 候诊基数
     * @param cntWaitNext 下一段候诊基数（第一次默认为0）
     * @param cntConte 续诊人数
     * @param cntConteNext 下一段续诊人数（第一次默认为0）
     * 根据规则获取目标位置（按照间隔cnt_wait个候诊人数，插入cnt_conte个续诊人数的规则插入续诊患者）如果为0插入队伍最后
     * @return 目标位置
     */
    public int targetPosition(List<PvQueVO> pvInfos,int cntWatiNext, int cntWait,int cntConteNext,int cntConte){
    	int number = 0;//已插入续诊人数
    	int sumNumber = cntWait + cntWatiNext + cntConte + cntConteNext;//每一段的候诊基数和续诊人数总和
    	if(pvInfos != null && pvInfos.size()>0){
        	if(sumNumber  <= pvInfos.size()){//如果本段的候诊基数和续诊人数总和大于排队人数，插入队伍末尾
        		for (int i = 0; i <  sumNumber; i++) {
        			if (cntWatiNext == 0){//表示第一段
        				if("1".equals(pvInfos.get(i).getEuType())){//是否续诊人
    						number += 1;//第一段已续诊人数
    					}
        			}else{
        				if(i >=  cntWatiNext + cntConteNext){//第一段之后的段数。。。。
        					if("1".equals(pvInfos.get(i).getEuType())){//是否续诊人
        						if(i >= (cntWatiNext + cntConteNext) +  (cntWait + cntConte) ){//超过本段的排队人数
        							break;
        						}
        						number += 1;//本段已续诊人数
        					}
        				}
        			}	
				}
        		if(number < cntConte){//如果已续诊人数<续诊人数，返回目标位置
                	int i = cntWatiNext + cntConteNext + cntWait + number;//目标位置
                	return i;
                }else{//继续判断下一段排队人数
                	cntWatiNext  = cntWatiNext + cntWait;
                	cntConteNext = cntConteNext + cntConte;
                	number = 0;
                	targetPosition(pvInfos,cntWatiNext,cntWait,cntConteNext,cntConte);			
                }
        	}else{
        		return 0;
        	}
    	}
    	return 0 ;
    }

    /**
     * 查询过号信息 003003001030
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryOverPvInfo(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> qryParam = JsonUtil.readValue(param, Map.class);
        qryParam.put("dateBegin", DateUtils.getDateTimeStr(new Date()));
        qryParam.put("dateEnd", DateUtils.getDateTimeStr(new Date()));
        List<Map<String, Object>> overPvInfos = triageMapper.qryOverPvInfo(qryParam);
        return overPvInfos;
    }

    /**
     * 过号入队 003003001031 过号入队规则：读取关联的分诊规则属性值：sortno_over（过号入队位置），为0排在最后
     *
     * @param param
     * @param user
     * @return
     */
    public void joinInQue(String param, IUser user) {
        PvQueVO pvQueVo = JsonUtil.readValue(param, PvQueVO.class);
        //查询该队列所有排队患者
        Integer count = DataBaseHelper.queryForScalar("select count(1) from pv_que where eu_status='0' and flag_cancel = '0' and pk_qcque=?", Integer.class, pvQueVo.getPkQcque());
        List<PvQueVO> pvQues = null;
        //关闭事务自动提交
  		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
  		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
  		TransactionStatus status = platformTransactionManager.getTransaction(def); 
        //如果过号重排为0，或者排队患者大于过号重排则排在最后
        if (pvQueVo.getSortnoOver() == 0 || count < pvQueVo.getSortnoOver()) {
            pvQueVo.setDateBegin(DateUtils.getDateStr(new Date()) + "000000");
            pvQueVo.setDateEnd(DateUtils.getDateStr(new Date()) + "235959");
            
            //排序号+10(排最后一位按签到规则生成)
            Map<String, Object> qryNo = triageMapper.qryNo(pvQueVo);
            pvQueVo.setSortno(Integer.parseInt(qryNo.get("sortno").toString()));
            pvQueVo.setSortnoAdj(Integer.parseInt(qryNo.get("sortnoAdj").toString()));
            try{
                DataBaseHelper.execute("update pv_que set sortno_adj=? , eu_status='0' , date_sign=? , pk_emp_sign=? , name_emp_sign=? , level_dise=? , flag_cancel=0 where pk_pvque=?", new Object[]{pvQueVo.getSortnoAdj(), new Date(), UserContext.getUser().getPkEmp(), UserContext.getUser().getNameEmp(), pvQueVo.getLevelDise(), pvQueVo.getPkPvque()});
      		    platformTransactionManager.commit(status); // 提交事务
      		} catch (Exception e) {
      			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
      		    e.printStackTrace();
      			throw new RuntimeException("续诊签到失败：" + e.getMessage());
      		}
        } else {
            String sqlPart = "";
            //是否启用预约优先
            if ("1".equals(pvQueVo.getFlagAppt())) {
                sqlPart = " order by que.eu_source desc,que.SORTNO_ADJ,que.SORTNO ";
            } else {
                sqlPart = " order by que.SORTNO_ADJ,que.SORTNO ";
            }
            String sql = "select * from PV_QUE que inner join pv_op pp on pp.pk_pv=que.pk_pv and pp.date_begin <= ? and pp.date_end >= ?"
                    + " where que.pk_qcque=? and que.eu_status='0' and que.flag_cancel='0' " + sqlPart;
            List<PvQueVO> pvInfos = DataBaseHelper.queryForList(sql, PvQueVO.class, new Object[]{new Date(), new Date(), pvQueVo.getPkQcque()});

            int preAimAdjNo = 0;//目标位置前一位的调整序号
            int nowAdjNo = 1;//入队患者的调整序号
            if (pvQueVo.getSortnoOver() - 1 > 0) { //判断过号入队位置是否为第一位
                if (pvInfos != null && pvInfos.size() > 0) {//判断此时队列中是否存在排队患者
                    if (pvQueVo.getSortnoOver() <= pvInfos.size()) {//判断入队位置是否大于排队人数
                        preAimAdjNo = pvInfos.get(pvQueVo.getSortnoOver() - 2).getSortnoAdj();
                        //判断目标位置的下一位，是否是过号入队的患者
                        //签到规则100 110 每位10的规则：目的就是留10个空位，过号入队使用
                        if(pvInfos.get(pvQueVo.getSortnoOver() - 1).getSortnoAdj() % 10 != 0){
                        	sql = "select * from PV_QUE que inner join pv_op pp on pp.pk_pv=que.pk_pv and pp.date_begin <= ? and pp.date_end >= ?"
                                    + " where que.pk_qcque=? and que.eu_status='0' and que.flag_cancel='0' and que.SORTNO_ADJ > ? and que.SORTNO_ADJ < ? " + sqlPart;
                            pvQues = DataBaseHelper.queryForList(sql, PvQueVO.class, new Object[]{new Date(), new Date(), pvQueVo.getPkQcque(), preAimAdjNo ,preAimAdjNo + 10 });
                        
                        }
                        if(pvQues != null && pvQues.size()>0){
                        	preAimAdjNo = pvQues.get(pvQues.size()-1).getSortnoAdj();
                        }
                        nowAdjNo = preAimAdjNo + 1;
                    }else{
                    	//入队位置大于排队人数,序号+10(排最后一位按签到规则生成)
                        Map<String, Object> noMap = triageMapper.qryNo(pvQueVo);
                        nowAdjNo = Integer.parseInt(noMap.get("sortnoAdj").toString()) ;
                    }
                } else {//不存在排队患者按签到规则生成调整,序号+10(排最后一位按签到规则生成)
                    Map<String, Object> noMap = triageMapper.qryNo(pvQueVo);
                    nowAdjNo = Integer.parseInt(noMap.get("sortnoAdj").toString()) ;
                }
            }
            try{
                DataBaseHelper.execute("update pv_que set sortno_adj=? , eu_status='0' , date_sign=? , pk_emp_sign=? , name_emp_sign=? , level_dise=? , flag_cancel=0 where pk_pvque=?", new Object[]{nowAdjNo, new Date(), UserContext.getUser().getPkEmp(), UserContext.getUser().getNameEmp(), pvQueVo.getLevelDise(), pvQueVo.getPkPvque()});
      		    platformTransactionManager.commit(status); // 提交事务
      		} catch (Exception e) {
      			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
      		    e.printStackTrace();
      			throw new RuntimeException("入队失败：" + e.getMessage());
      		}
        }
    }
    public static void main(String[] args) {
		int a = 100 % 10 ;
		System.out.print(a);
	}
}
