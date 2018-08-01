package com.iwork.asset.constant;



/**
 * 状态常量
 * @author yanglianfeng
 *
 */
public class StatusConstant {

	/**********************审批状态开始 *********************/
	
	/** 起草 */
	public static final String PROCESS_STATUS_QICAO = "起草";
	/** 审批中 */
	public static final String PROCESS_STATUS_SHENPIZHONG = "审批中";
	/** 审批中未归档 */
	public static final String PROCESS_STATUS_SHENPIZHONGWEIGUIDANG = "审批中未归档";
	/** 已归档 */
	public static final String PROCESS_STATUS_YIGUIDANG = "已归档";
	
	/**********************审批状态结束 *********************/
	public static final String ZYKP_GLZTKX = "空闲";
	public static final String ZYKP_GLZTYSY = "已使用";
	public static final String ZYKP_GLZTYLY = "已领用";
	public static final String ZYKP_UUID = "de88c23443554475bfb8bd1612987d1d";//资源卡片主数据UUID
	public static final String ZYKP_GLZTKPDJ = "卡片登记";
	
	/**********************zhangruibo固定资产管理状态常量 开始*********************/
	public static final String ZYKP_GLZTWHZ = "维护中";
	public static final String ZYKP_GLZTYWH = "已维护";
	public static final String ZYKP_GLZTSQBF = "申请报废";
	public static final String ZYKP_GLZTDSZ = "申请丢失";
	
	/**********************zhangruibo固定资产管理状态常量  结束*********************/
	
	public static final String ZYKP_GLZTWBF = "已报废";
	public static final String ZYKP_GLZTWDS = "已丢失";

    /**********************zhangtian固定资产管理状态 开始*********************/
	public static final String ZYKP_GLZTGHZ = "归还中";//固定资产归还流程
	public static final String ZYKP_GLZTZYZ = "转移中";//固定资产转移他人流程
	public static final String ZYKP_GLZTZQZ = "增强中";//固定资产增强配置流程
	/**********************zhangtian固定资产管理状态 结束*********************/
	public static final String ZYKP_GLZTYGH = "已归还";//固定资产归还流程
	public static final String ZYKP_GLZTYZY = "已转移";//固定资产转移他人流程
	public static final String ZYKP_GLZTYZQ = "已增强";//固定资产增强配置流程
	
	
}
