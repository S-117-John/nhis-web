package com.zebone.nhis.pro.zsrm.sch.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.pi.PiLock;
import com.zebone.nhis.common.module.pi.PiLockDt;
import com.zebone.nhis.common.module.pi.PiLockRecord;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsrm.sch.dao.PiBlackMapper;
import com.zebone.nhis.pro.zsrm.sch.vo.PiBlackVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 患者黑名单识别处理类
 */
@Service
public class PiBlackService {

    @Autowired
    private PiBlackMapper piBlackMapper;

    private final int FEE_DAYS_AGO = -2;
    private final String DATE_FORMAT="yyyyMMddHHmmss";

//     * 门诊逃费 拉黑准备数据
//     * 1.判断患者48小时前已就诊但是未缴纳诊金的情况下置为欠费黑名单
//     * 2.判断患者48小时前存在医生开立的已确认执行的医嘱，但是患者未缴费的情况下置为欠费黑名单


    /**
     * 生成黑名单数据
     * @param limitDay
     */
    public void genPiLock(int limitDay){
        List<PiBlackVo> voList = getPiBlackNew(limitDay);
        List<PiLockRecord> recordList = buildPiLockPre(voList);
        if(addPiLockPre(recordList)){
            List<PiBlackVo> blackVoList = buildPiLock();
            saveAutoLock(blackVoList);
            updatePiLockRecord(blackVoList);
        }
    }

    public List<PiBlackVo> getPiBlackNew(int limitDay){
        limitDay = -Math.abs(limitDay);
        //默认都是查询前limitDay的数据
        List<PiBlackVo> blackVoList = new ArrayList<>();
        Map<String,Object> paramMap = new HashMap<>();
        //违约：[前limitDay,当前)
        paramMap.put("limitTime", DateUtils.addDate(new Date(),limitDay,3,DATE_FORMAT));
        paramMap.put("nowTime", DateUtils.formatDate(new Date(),DATE_FORMAT));
        blackVoList.addAll(piBlackMapper.getPiOfAppt(paramMap));
        //未缴费：[前limitDay + 48/24,blackTime)
        paramMap.put("limitTime", DateUtils.addDate(new Date(),limitDay-2,3,DATE_FORMAT));
        paramMap.put("blackTime", DateUtils.addDate(new Date(),FEE_DAYS_AGO,3,DATE_FORMAT));
        blackVoList.addAll(piBlackMapper.getPiOfNoPayRegfee(paramMap));
        blackVoList.addAll(piBlackMapper.getPiOfNoPayExeFee(paramMap));

        return blackVoList;
    }

    public boolean addPiLockPre(List<PiLockRecord> preList){
        if(CollectionUtils.isEmpty(preList))
            return false;
        Set<String> codeSet = preList.stream().map(PiLockRecord::getCode).collect(Collectors.toSet());
        List<String> list = DataBaseHelper.getJdbcTemplate().queryForList("select CODE from PI_LOCK_RECORD where CODE in("+CommonUtils.convertSetToSqlInPart(codeSet,"CODE")+")",String.class);
        if(list.size()>0){
            preList = preList.stream().filter(dt -> !list.contains(dt.getCode())).collect(Collectors.toList());
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLockRecord.class),preList);
        return preList.size()>0;
    }

    public List<PiBlackVo> buildPiLock(){
        List<PiBlackVo> blackVoList = new ArrayList<>();
        Map<String,Object> paramMap = Maps.newHashMap();
        //查询PiLockPre，依据条件构造满足条件的piLock记录:一年内违约4次；30内未缴费【限定时间是在执行插入PI_LOCK_RECORD时已经处理了，比如48小时之前的计费数据...这里直接查开始时间即可】
        Date date = new Date();
        paramMap.put("dateBegin",DateUtils.addDate(date,-1,1,DATE_FORMAT));
        blackVoList.addAll(piBlackMapper.getPiLockApptOfRecord(paramMap));
        paramMap.put("dateBegin",DateUtils.addDate(date,-30+FEE_DAYS_AGO,3,DATE_FORMAT));
        blackVoList.addAll(piBlackMapper.getPiLockFeeOfRecord(paramMap));
        return blackVoList;
    }

    public void updatePiLockRecord(List<PiBlackVo> list){
        if(CollectionUtils.isNotEmpty(list)){
            List<String> sqlList = list.stream().filter(dt -> StringUtils.isNotBlank(dt.getPkPilock()) && StringUtils.isNotBlank(dt.getPkRecordStr()))
                    .map(dt ->"update PI_LOCK_RECORD set PK_PILOCK='"+dt.getPkPilock()+"' where PK_PILOCKREC in('"+dt.getPkRecordStr().replaceAll(",","','")+"')")
                    .collect(Collectors.toList());
            DataBaseHelper.batchUpdate(sqlList.toArray(new String[]{}));
        }
    }

    public List<PiLockRecord> buildPiLockPre(List<PiBlackVo> blackVoList){
        String sp = "_";
        return blackVoList.stream().map(vo ->{
            PiLockRecord pre = new PiLockRecord();
            pre.setPkPilockrec(NHISUUID.getKeyId());
            pre.setPkOrg(vo.getPkOrg());
            pre.setCreateTime(new Date());
            pre.setTs(new Date());
            pre.setPkPi(vo.getPkPi());
            pre.setPkPv(vo.getPkPv());
            pre.setDateRecord(vo.getOccurredTime());
            pre.setEuLocktype(vo.getEuLockType());
            pre.setEuStatus(EnumerateParameter.ONE);
            pre.setCode(vo.getQryType()+sp+vo.getPkPi()+sp+(StringUtils.isBlank(vo.getPkPv())?"":vo.getPkPv())+sp+vo.getEuLockType()+sp+DateUtils.formatDate(vo.getOccurredTime(),"yyyyMMddHHmmss"));
            return pre;
        }).collect(Collectors.toList());
    }

    public void saveAutoLock(List<PiBlackVo> blackVoList) {
        if(CollectionUtils.isNotEmpty(blackVoList)){
            Date date = new Date();
            List<PiLock> piLockList = Lists.newArrayList();
            List<PiLockDt> piLockDtList = Lists.newArrayList();
            for (PiBlackVo vo : blackVoList) {
                String note= getNote(vo.getQryType()) + (StringUtils.isNotBlank(vo.getNote())?vo.getNote():"");
                PiLock piLock = new PiLock();
                piLock.setPkPilock(NHISUUID.getKeyId());
                piLock.setPkOrg(vo.getPkOrg());
                piLock.setPkPi(vo.getPkPi());
                piLock.setEuLocktype(vo.getEuLockType());
                piLock.setDateBegin(date);
                piLock.setDateEnd(EnumerateParameter.ZERO.equals(vo.getEuLockType()) ? DateUtils.strToDate(DateUtils.addDate(date, 14, 3, "yyyyMMddHHmmss"), "yyyyMMddHHmmss") : null);
                piLock.setDelFlag(EnumerateParameter.ZERO);
                piLock.setNote(note);
                piLock.setCreateTime(date);
                piLock.setTs(date);
                piLockList.add(piLock);
                vo.setPkPilock(piLock.getPkPilock());

                PiLockDt piLockDt = new PiLockDt();
                piLockDt.setPkPilockdt(NHISUUID.getKeyId());
                piLockDt.setPkOrg(vo.getPkOrg());
                piLockDt.setPkPi(vo.getPkPi());
                piLockDt.setEuDirect(EnumerateParameter.ONE);
                piLockDt.setDateLock(date);
                piLockDt.setEuLocktype(vo.getEuLockType());
                piLockDt.setFlagEffect(EnumerateParameter.ZERO);
                piLockDt.setDelFlag(EnumerateParameter.ZERO);
                piLockDt.setNote(note);
                piLockDt.setCreateTime(date);
                piLockDt.setTs(date);
                piLockDtList.add(piLockDt);
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLock.class), piLockList);
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLockDt.class), piLockDtList);
        }

    }

    public void delAutoLock(){
        List<PiBlackVo> pkPiList = DataBaseHelper.queryForList("select pk_pi,pk_org,lk.PK_PILOCK from pi_lock lk where EU_LOCKTYPE='0' and DATE_END is not null and DATE_END<?", PiBlackVo.class, new Object[]{new Date()});
        if(CollectionUtils.isNotEmpty(pkPiList)) {
            DataBaseHelper.update("delete from pi_lock lk where EU_LOCKTYPE='0' and DATE_END is not null and DATE_END<?",new Object[]{new Date()});
            List<PiLockDt> piLockDtList = pkPiList.stream().map(vo -> {
                Date date = new Date();
                PiLockDt piLockDt = new PiLockDt();
                piLockDt.setPkPilockdt(NHISUUID.getKeyId());
                piLockDt.setPkOrg(vo.getPkOrg());
                piLockDt.setPkPi(vo.getPkPi());
                piLockDt.setEuDirect(EnumerateParameter.NEGA);
                piLockDt.setDateLock(date);
                piLockDt.setEuLocktype("0");
                piLockDt.setFlagEffect(EnumerateParameter.ZERO);
                piLockDt.setDelFlag(EnumerateParameter.ZERO);
                piLockDt.setNote("预约拉黑到期解锁");
                piLockDt.setCreateTime(date);
                piLockDt.setTs(date);
                return piLockDt;
            }).collect(Collectors.toList());
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLockDt.class), piLockDtList);
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("piLocks",pkPiList.stream().map(PiBlackVo::getPkPilock).collect(Collectors.toList()));
            DataBaseHelper.update("update PI_LOCK_RECORD set EU_STATUS='0' where PK_PILOCK in(:piLocks)",paramMap);
        }
    }


    private String getNote(String type){
        switch (type){
            case EnumerateParameter.ZERO:
                return "累计失约次数上限";
            case EnumerateParameter.ONE:
                return "未按时缴纳诊费";
            case EnumerateParameter.TWO:
                return "未按时缴费";
        }
        return null;
    }


}
