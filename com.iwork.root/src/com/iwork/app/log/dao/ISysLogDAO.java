package com.iwork.app.log.dao;


/**
 * 操作日志数据操作接口
 * @author LuoChuan
 *
 */
public interface ISysLogDAO {

	/**
	 * 添加一条操作日志记录到数据库表Sys_Log_Record中
	 * @param functionName			功能名称
	 * @param tableName				操作表名
	 * @param logType				日志类型，"0"代表操作日志、"1"为代表错误日志
	 * @param operateType			操作类型，"0"代表增加，"1"代表删除，"2"代表修改，"3"代表查询
	 * @param dataPk				添加的数据的主键
	 * @param logText				日志内容，以能准备表达进行的操作为准
	 */
	public void addOperationLog(String functionName,String tableName,String operateType,String dataPk,String logText);
	
	/**
	 * 添加一条操作日志记录到数据库表Sys_Log_Record中
	 * @param functionName			功能名称
	 * @param tableName				操作表名
	 * @param logType				日志类型，"0"代表操作日志、"1"为代表错误日志
	 * @param operateType			操作类型，"0"代表增加，"1"代表删除，"2"代表修改，"3"代表查询
	 * @param dataPk				添加的数据的主键
	 * @param model					存储数据的实体bean，里面存放了页面上的数据
	 */
	public void addOperationLog(String functionName,String tableName,String operateType,String dataPk,Object model);
	
	/**
	 * 添加一条操作日志记录到数据库表Sys_Log_Record中
	 * @param functionName			功能名称
	 * @param tableName				操作表名
	 * @param logType				日志类型，"0"代表操作日志、"1"为代表错误日志
	 * @param operateType			操作类型，"0"代表增加，"1"代表删除，"2"代表修改，"3"代表查询
	 * @param dataPk				添加的数据的主键
	 * @param logText				日志内容，以能准备表达进行的操作为准
	 * @param extend1				扩展字段1
	 * @param extend2				扩展字段2				
	 * @param extend3				扩展字段3
	 * @param extend4				扩展字段4
	 * @param extend5				扩展字段5
	 */
	public void addOperationLog(String functionName,String tableName,String operateType,String dataPk,String logText,String extend1,String extend2,String extend3,String extend4,String extend5);
	
	/**
	 * 添加一条操作日志记录到数据库表Sys_Log_Record中
	 * @param functionName			功能名称
	 * @param tableName				操作表名
	 * @param logType				日志类型，"0"代表操作日志、"1"为代表错误日志
	 * @param operateType			操作类型，"0"代表增加，"1"代表删除，"2"代表修改，"3"代表查询
	 * @param dataPk				添加的数据的主键
	 * @param model					存储数据的实体bean，里面存放了页面上的数据
	 * @param extend1				扩展字段1
	 * @param extend2				扩展字段2				
	 * @param extend3				扩展字段3
	 * @param extend4				扩展字段4
	 * @param extend5				扩展字段5
	 */
	public void addOperationLog(String functionName,String tableName,String operateType,String dataPk,Object model,String extend1,String extend2,String extend3,String extend4,String extend5);
	
	/**
	 * 添加一条错误日志记录到数据库表Sys_Log_Record中
	 * @param logText				错误信息
	 */
	public void addErrorLog(String logText);
	
	/**
	 * 添加一条错误日志记录到数据库表Sys_Log_Record中
	 * @param logText
	 * @param extend1
	 * @param extend2
	 * @param extend3
	 * @param extend4
	 * @param extend5
	 */
	public void addErrorLog(String logText, String extend1,String extend2,String extend3,String extend4,String extend5);
}
