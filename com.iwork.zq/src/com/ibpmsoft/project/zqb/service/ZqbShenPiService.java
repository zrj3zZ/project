package com.ibpmsoft.project.zqb.service;

import java.util.Map;
import com.ibpmsoft.project.zqb.util.ConfigUtil;

public class ZqbShenPiService {
	// 公告UUDI
	public static final String DB_FILENAME = "/sp.properties";
	
	public String index(){
		Map<String,String> conn=ConfigUtil.readAllProperties(DB_FILENAME);
		return conn.get("status").toString();
	}

}
