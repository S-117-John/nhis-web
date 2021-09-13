package com.zebone.nhis.task.pub.service;

/**
 * 定时任务公共接口
 */

public interface ITaskJobService {
    public Object sendSmsForTask(Object... obj);
}
