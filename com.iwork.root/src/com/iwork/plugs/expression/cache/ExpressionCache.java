package com.iwork.plugs.expression.cache;

import com.iwork.app.conf.SystemConfig;
import com.iwork.core.cache.BaseCache;

public class ExpressionCache {

	private BaseCache sysNavDirectoryCache;
	private static ExpressionCache instance;
	private static Object lock = new Object();
	private final String key = "ExpressionCache";

	private ExpressionCache() {

		// 这个根据配置文件来，初始BaseCache而已;
		String time = SystemConfig._iworkServerConf.getCacheTime();
		if (!time.equals("")) {
			sysNavDirectoryCache = new BaseCache(key, Integer.parseInt(time));
		} else {
			sysNavDirectoryCache = new BaseCache(key, 1800);
		}

	}

	public static ExpressionCache getInstance() {
		if (instance == null) {
			instance = new ExpressionCache();
		}
		return instance;
	}
	
	

}
