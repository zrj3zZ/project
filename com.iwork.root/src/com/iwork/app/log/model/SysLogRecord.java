package com.iwork.app.log.model;


/**
 * SysLogRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysLogRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2296767294356956135L;
	public static String DATABASE_ENTITY = "SysLogRecord";
	public static String DATABASE_ENTITY_PACKAGE_PATH = "com.iwork.app.log.model.SysLogRecord";
	
	// Fields

	private Long logId;
	private String tableName;
	private String functionName;
	private String dataPk;
	private String createUser;
	private String createTime;
	private Long operateType;
	private Long logType;
	private String logText;
	private String standby1;
	private String standby2;
	private String standby3;
	private String standby4;
	private String standby5;

	// Constructors

	/** default constructor */
	public SysLogRecord() {
	}

	/** minimal constructor */
	public SysLogRecord(Long logId, Long logType) {
		this.logId = logId;
		this.logType = logType;
	}

	/** full constructor */
	public SysLogRecord(Long logId, String tableName, String functionName,
			String dataPk, String createUser, String createTime,
			Long operateType, Long logType, String logText, String standby1,
			String standby2, String standby3, String standby4, String standby5) {
		this.logId = logId;
		this.tableName = tableName;
		this.functionName = functionName;
		this.dataPk = dataPk;
		this.createUser = createUser;
		this.createTime = createTime;
		this.operateType = operateType;
		this.logType = logType;
		this.logText = logText;
		this.standby1 = standby1;
		this.standby2 = standby2;
		this.standby3 = standby3;
		this.standby4 = standby4;
		this.standby5 = standby5;
	}

	// Property accessors

	public Long getLogId() {
		return this.logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFunctionName() {
		return this.functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getDataPk() {
		return this.dataPk;
	}

	public void setDataPk(String dataPk) {
		this.dataPk = dataPk;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Long getOperateType() {
		return this.operateType;
	}

	public void setOperateType(Long operateType) {
		this.operateType = operateType;
	}

	public Long getLogType() {
		return this.logType;
	}

	public void setLogType(Long logType) {
		this.logType = logType;
	}

	public String getLogText() {
		return this.logText;
	}

	public void setLogText(String logText) {
		this.logText = logText;
	}

	public String getStandby1() {
		return this.standby1;
	}

	public void setStandby1(String standby1) {
		this.standby1 = standby1;
	}

	public String getStandby2() {
		return this.standby2;
	}

	public void setStandby2(String standby2) {
		this.standby2 = standby2;
	}

	public String getStandby3() {
		return this.standby3;
	}

	public void setStandby3(String standby3) {
		this.standby3 = standby3;
	}

	public String getStandby4() {
		return this.standby4;
	}

	public void setStandby4(String standby4) {
		this.standby4 = standby4;
	}

	public String getStandby5() {
		return this.standby5;
	}

	public void setStandby5(String standby5) {
		this.standby5 = standby5;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
