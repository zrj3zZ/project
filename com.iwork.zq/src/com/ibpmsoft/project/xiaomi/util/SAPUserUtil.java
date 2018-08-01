package com.ibpmsoft.project.xiaomi.util;

import java.util.HashMap;
import java.util.Map;
import com.iwork.commons.util.DBUTilNew;

public class SAPUserUtil {
	
	
	
	/**
	 * 根据当前SAP账号，获取OA系统账号
	 * @param sapno
	 * @return
	 */
	public String getCurrentUserId(String sapno){
		Map params = new HashMap();
		params.put(1, sapno);
		String sql = "select userid  from orguser where Upper(extend1)= ? ";
		String userid = DBUTilNew.getDataStr("userid",sql,params);
		return userid;
	}

}
