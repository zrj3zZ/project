package com.iwork.plugs.resoucebook.util;

import java.util.HashMap;

public class RMConst {
	public static final HashMap<String, String> spacecycle = new HashMap<String, String>() {
		{
			put("7", "一周");
			put("14", "两周");
			put("21", "三周");
			put("28", "四周");
		}
	};
	public static final HashMap<String, String> spacetype = new HashMap<String, String>() {
		{
			put("1", "流程预定");
			put("2", "直接预订");
		}
	};
	public static final HashMap<String, String> spacestatus = new HashMap<String, String>() {
		{
			put("1", "开启");
			put("0", "关闭");
		}
	};
	public static final HashMap<String, String> spacestatus2 = new HashMap<String, String>() {
		{
			put("1", "有效");
			put("2", "作废");
		}
	};

}
