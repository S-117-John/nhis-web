package com.zebone.nhis.task.sms.service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimaps;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.lb.service.SmsService;
import com.zebone.nhis.task.sms.executor.MessageExecutor;
import com.zebone.nhis.task.sms.executor.MessageExecutorFactory;
import com.zebone.nhis.task.sms.support.MsgTimerTools;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  sms消息任务，依赖任务的执行频率，不能大于RATE_HOURS<br>
 *  ①每次任务执行，是将符合条件的数据拉到内容列表中等待执行 <br>
 *  ②轮询线程不断的从列表中获取符合条件的数据，调用接口执行，执行完后要从列表中移除<br>
 *  ③当列表中没有数据，终止轮训，待下次列表中有数据再次执行
 * @author Alvin
 */
@Service
public class SmsTaskService {

    private final static Logger logger = LoggerFactory.getLogger(SmsTaskService.class);
    /**获取N分钟内的数据，和任务时间基本一致即可*/
    private static final int RATE_MINUTES = 35;

    private MessageExecutor messageExecutor;
    private MsgService msgService = new SmsMsgService();;
    private Thread localTask;

    private AtomicBoolean atomicBoolean = new AtomicBoolean(true);
    private volatile User localUser = new User();

    @PostConstruct
    public void init(){
        messageExecutor = MessageExecutorFactory.getInstance().getExecutor();
    }

    private void initThread(){
        localTask = new Thread(new MessageThread(),"messageThread");
        localTask.start();
    }

    public Map<String,Object> execMsTask(QrtzJobCfg cfg) {
        Map<String,Object> result = new HashMap<>();
        User user = UserContext.getUser();
        if(user ==null && StringUtils.isBlank(cfg.getJgs())){
            result.put("msg","请先授权机构!");
            return result;
        }
        initUser(user,cfg.getJgs());
        //然后获取已审核的待发送的数据
        buildSendMap();
        atomicBoolean.set(MsgTimerTools.getQueueSize() >0);
        //如果列表有数据，此时如果轮询线程已经终止，重新初始化
        if(localTask == null
                ||(atomicBoolean.get() && (localTask.getState() == Thread.State.TERMINATED || localTask.getState() == Thread.State.NEW))){
            initThread();
        }
        return result;
    }

    /**
     * 按照模板生成日、月待发送的数据（模板新增的时候会先生成一次数据）
     * <br>每天跑一次即可，将生成前一天或者上个月数据
     * @param cfg
     * @return
     */
    public Map<String,Object> execCreateMsTask(QrtzJobCfg cfg) {
        Map<String, Object> result = new HashMap<>();
        User user = UserContext.getUser();
        if (user == null && StringUtils.isBlank(cfg.getJgs())) {
            result.put("msg", "请先授权机构!");
            return result;
        }
        initUser(user, cfg.getJgs());
        SmsService smsService = ServiceLocator.getInstance().getBean(SmsService.class);
        smsService.autoSave();
        return result;
    }

    private User initUser(User user,String jgs){
        if(user== null){
            user = new User();
            user.setPkOrg(jgs);
            user.setPkEmp(CommonUtils.getGlobalOrg());
            UserContext.setUser(user);
        }
        localUser.setPkEmp(user.getPkEmp());
        localUser.setNameEmp(user.getNameEmp());
        localUser.setPkOrg(user.getPkOrg());
        return user;
    }

    public void buildSendMap(){
        Date date = new Date();
        String start = DateFormatUtils.format(new Date(),"yyyy-MM-dd") +" 00:00:00";
        String end = DateFormatUtils.format(DateUtils.addMinutes(date, RATE_MINUTES), "yyyy-MM-dd HH:mm:ss");
        //获取已经入库的，发送未完成的数据（只要调用发送完成，flag_finish会改为1，这里就查不到）
        List<Map<String,Object>> mapList = DataBaseHelper.queryForList("select sd.PK_SMSSEND,sd.CONTENT,sd.eu_status_chk,sd.mobile,sd.DATE_SEND from sms_send sd" +
                " where sd.flag_finish=0 and sd.eu_status_chk=1 and sd.DATE_SEND between to_date(?,'yyyy-MM-dd HH24:MI:SS') and to_date(?,'yyyy-MM-dd HH24:MI:SS')"
            ,new Object[]{start,end});
        if (CollectionUtils.isEmpty(mapList)) {
            logger.info("sms任务组装信息，集合为空");
            return;
        }

        //分组，注意dateSend不能为null
        ImmutableMap<String, Collection<Map<String,Object>>> immutableMap = Multimaps.index(mapList, new Function<Map<String,Object>, String>() {
            @Override
            public String apply(Map<String,Object> map) {
                return MapUtils.getString(map, "dateSend");
            }
        }).asMap();
        logger.info("sms任务按照发送时间分组完成");
        ImmutableSet<String> keySet = immutableMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        while(iterator.hasNext()){
            String timeSend = iterator.next();
            //下面的查询不会返回重复的，所以方法开始时获取一次即可
            List<Map<String,Object>> listQueue = MsgTimerTools.getListOfQueue(timeSend);
            if(CollectionUtils.isEmpty(listQueue)){
                MsgTimerTools.addQueue(timeSend, mapList);
            } else {
                for (Map<String,Object> smsSend : mapList) {
                    final String pk = MapUtils.getString(smsSend,"pkSmssend");
                    Object obj = CollectionUtils.find(listQueue, new Predicate() {
                        @Override
                        public boolean evaluate(Object object) {
                            Map<String,Object> mp = (Map<String,Object>)object;
                            return StringUtils.equals(pk, MapUtils.getString(mp,"pkSmssend"));
                        }
                    });
                    if(obj != null){
                        listQueue.remove((Map<String, Object>) obj);
                    }
                    listQueue.add(smsSend);
                }
            }
            logger.info("sms任务待执行数据{},条目：{}",timeSend,listQueue==null?0:listQueue.size());
        }

    }

    class MessageThread implements Runnable{
        @Override
        public void run() {
            //不断读取符合条件的数据，如果一直有数据随机休眠一段时间（所以执行会稍有接受内的延迟）
            UserContext.setUser(localUser);
            while(atomicBoolean.get()){
                atomicBoolean.set(MsgTimerTools.exeByQueue(msgService,messageExecutor));
                try {
                    Thread.sleep(600, RandomUtils.nextInt(10000, 999999));
                } catch (InterruptedException e) {
                    logger.error("ItEm异常：",e);
                }
            }
        }
    }
}
