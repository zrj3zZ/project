package com.iwork.plugs.sysletters.constants;
/**
 * 站内信常量类
 * @author WangJianhui
 *
 */
public class SysLettersConstants {
 
	//刚进页面列表初始化,查询每天记录的最大值为最后发送信息,当最后一条为未读，则此信为未读
	// public final static String SQL_LIST = "select max(u.id)as id,u.letterId,u.receiveUserId,u.receiveUserName,c.letterTitle,u.ownerId,u.createUserName from SysLettersDetailInfo  u , SysLettersContent  c where u.letterId=c.id  group by u.letterId,u.receiveUserId,u.receiveUserName,c.letterTitle,u.ownerId,u.createUserName having u.receiveUserId=:receiveUserId or u.ownerId=:ownerId";
	public final static String SQL_LIST = "select max(u.id)as id,u.letterId,u.receiveUserId,u.receiveUserName,c.letterTitle,u.ownerId,u.createUserName from SysLettersDetailInfo  u , SysLettersContent  c where u.letterId=c.id  group by u.letterId,u.receiveUserId,u.receiveUserName,c.letterTitle,u.ownerId,u.createUserName having u.ownerId=:ownerId";
	//由于SQL_LIST分组所以把标记状态单查 
	public final static String SQL_BY_ID = "FROM SysLettersDetailInfo WHERE id in (:listId)"; 
	//删除回复内容SQL
	public final static String SQL_DEL_REPLY = "delete SysLettersDetailInfo where id=:id ";
	public final static String SQL_DEL_REPLY_CONTENT = "delete SysLettersDetailReply where detailDataId=:detailDataId";
	//删除站内信列表
	public final static String SQL_DEL_LIST_DETAIL = " delete from SysLettersDetailInfo where letterId in(:letterIds) and ownerId=:ownerId";
	public final static String SQL_DEL_LIST_REPLY = " delete from SysLettersDetailReply where letterId in(:letterIds) and userId=:userId";
	//更改站内信标记状态
	// public final static String SQL_UPDATE_FLAG = " update SysLettersDetailInfo set checkStatus=:flag where letterId in(:letterIds) and ownerId=:ownerId and receiveUserId=:receiveUserId";
	public final static String SQL_UPDATE_FLAG = " update SysLettersDetailInfo set checkStatus=:flag where letterId in(:letterIds) and ownerId=:ownerId ";
	//已读状态
	public final static String CHECKSTATUS_TURE= "1";
	//未读状态
	public final static String CHECKSTATUS_FALSE= "0"; 
	//批量提交数量标准,每次提交50条
	public final static int  SPLIT_COMMIT_VALUE = 50;	
}

 