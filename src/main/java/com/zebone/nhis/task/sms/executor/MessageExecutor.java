package com.zebone.nhis.task.sms.executor;

import com.zebone.nhis.task.sms.service.MsgService;

/**
 * 信息执行接口，不同的方式自行实现即可
 */
public interface MessageExecutor {

    /**
     * 具体的执行操作
     * @param service 具体的信息处理类（比如短信，邮件，等可以使用不通的实现）。
     * @param data 自行组装的需要传入的数据
     */
    void execute(MsgService service, Object data);
}
