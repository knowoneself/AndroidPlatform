package com.comtop.app.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理�?
 * 
 * 2014-04-14
 * 
 * @author by xxx
 * 
 */
public class ThreadPoolManager {
	private ExecutorService service;

	private ThreadPoolManager() {
		// 返回手机当前可用的处理器核心数，至少�?
		int num = Runtime.getRuntime().availableProcessors();
		service = Executors.newFixedThreadPool(num * 2);
	}

	private static ThreadPoolManager manager;

	public static ThreadPoolManager getInstance() {
		if (manager == null) {
			manager = new ThreadPoolManager();
		}
		return manager;
	}

	public void addTask(Runnable runnable) {
		service.submit(runnable);
	}

}
