package com.zebone.nhis.webservice.pskq.service;

import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;

public interface SurgeryRecordService {


    /**
     * 修改手麻回传状态
     * @param
     * @param listener
     */
    void update(RequestBody requestBody, ResultListener listener);


    /**
     * 新增手术排班
     * @param requestBody
     * @param listener
     */
    void addExOpSch(RequestBody requestBody, ResultListener listener);



}
