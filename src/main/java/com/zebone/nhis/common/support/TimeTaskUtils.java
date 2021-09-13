package com.zebone.nhis.common.support;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class TimeTaskUtils {

	/**
	 * 执行一个有时间限制的任务
	 * 
	 * @param task
	 *            待执行的任务
	 * @param seconds
	 *            超时时间(单位: 秒)
	 * @return
	 */
	public static Boolean execute(Callable<Boolean> task, int seconds) {
		Boolean result = Boolean.FALSE;
		ExecutorService threadPool = Executors.newCachedThreadPool();
		Future<Boolean> future = null;
		try {
			 future = threadPool.submit(task);
			result = future.get(seconds, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			result = Boolean.FALSE;
			e.printStackTrace();
		}catch (ExecutionException e) {
			result = Boolean.FALSE;
			e.printStackTrace();
		}catch (TimeoutException e) {
			result = Boolean.FALSE;
			e.printStackTrace();
		}catch (Exception e) {
			result = Boolean.FALSE;
			e.printStackTrace();
		} finally {
			if(future!=null){
				future.cancel(true); 
			}
			threadPool.shutdownNow();
		}



		return result;
	}
}