package com.iwork.app.message.sysmsg.model;




import com.iwork.core.security.SecurityUtil;

/**
 * 系统消息模型
 * @author David.Yang
 *
 */
public class SysMessage implements java.io.Serializable {
	public static String DATABASE_ENTITY = "SysMessage";
	
	// 消息优先级
    public static final int MSG_PRIORITY_LOW = -1;
    public static final int MSG_PRIORITY_MED = 0;
    public static final int MSG_PRIORITY_HIGH = 1;
    
    // 消息类型 
    public static final int MSG_TYPE_SYSTEM = 0; //系统消息
    public static final int MSG_TYPE_BIRTH = 1;  //生日提醒
    public static final int MSG_TYPE_MEETINIG = 3; //OA知道
    public static final int MSG_TYPE_WORKFLOW = 2;  //流程通知
    //public static final int MSG_TYPE_DISCUSSION = 4; //流程讨论区

    // 点击链接打开方式，0：当前页面、1：新页面
    public static final int MSG_URL_TARGET_SELF = 0;
    public static final int MSG_URL_TARGET_BLANK = 1;
    
    // 消息状态
    public static final int MSG_STATUS_READ_NO = 0;   //未读
    public static final int MSG_STATUS_READ_YES = 1;  //已读
    public static final int MSG_STATUS_DELETED = -1;  //删除
    
    // 消息是否自动弹出，是为1，弹出后需更为为0，不再重弹
    //public static final int MSG_TOPOPUP_YES = 1;
    //public static final int MSG_TOPOPUP_NO = 0;
    
    // 消息接收范围，1为全员，0为个人或部分。接收方只需接收receiver和range为1的的消息
    //public static final int MSG_RCV_RANGE_ALL = 1;
    //public static final int MSG_RCV_RANGE_PART = 0;
    //public static final int MSG_RCV_RANGE_CITY = 2; //新增四地
    
    public static final String MSG_DEFAULT_SENDER = SecurityUtil.supermanager;
	
	// Fields
	private Long id;
	private int priority = MSG_PRIORITY_MED;  //medium
	private int type = MSG_TYPE_SYSTEM;  //消息类型
	private String title ; //消息标题
	private String content ; //消息正文
	private String receiver ;
	private String url ;
	private int status = MSG_STATUS_READ_NO;
	private String sendDate ;
	private String readDate = "";
	private String sender = MSG_DEFAULT_SENDER;
    //private int toPopUp = MSG_TOPOPUP_YES;
    private String rcvRange;
    private String uuid;
    private Long instanceId;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	//private String allSysMsgId = "0";    //全员系统消息ID
    //private String city = ""; // 城市，发送范围为四地时使用    
    private int urlTarget;// url target: mainFrame or _blank, mainFrame default
    
    public String getUrlTargetStr(int urlTarget) {
    	if (urlTarget == 0 ) {
    		return "_blank";
    	} else {
    		return "";
    	}
    }
	
	public static String getDATABASE_ENTITY() {
		return DATABASE_ENTITY;
	}
	public static void setDATABASE_ENTITY(String database_entity) {
		DATABASE_ENTITY = database_entity;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getReadDate() {
		return readDate;
	}
	public void setReadDate(String readDate) {
		this.readDate = readDate;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public int getUrlTarget() {
		return urlTarget;
	}
	public void setUrlTarget(int urlTarget) {
		this.urlTarget = urlTarget;
	}
	public String getRcvRange() {
		return rcvRange;
	}
	public void setRcvRange(String rcvRange) {
		this.rcvRange = rcvRange;
	}
	

}
