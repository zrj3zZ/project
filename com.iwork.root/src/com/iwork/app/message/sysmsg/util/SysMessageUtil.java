package com.iwork.app.message.sysmsg.util;

public class SysMessageUtil {
	
	private static Object lock = new Object();  
    private static SysMessageUtil instance;  
    public static SysMessageUtil getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new SysMessageUtil();  
                }
            }  
        }  
        return instance;  
    } 
    
//    /**
//     * 
//     * @param sysMessage
//     * @return
//     */
//    public boolean createSysMsg(String userid,String type,String title,String content,String url,String priority){
//    	SysMessageDAO dao = new SysMessageDAO();
//    	SysMessage sysMsg = new SysMessage();
//    	sysMsg.setId(String.valueOf(SequenceUtil.getInstance().getSequenceIndex(SequenceConstant.SYS_System)));
//    	sysMsg.setType(type);
//    	sysMsg.setPriority(priority);
//    	sysMsg.setStatus("0");
//    	sysMsg.setReceiver(userid);
//    	sysMsg.setUrl(url);
//    	boolean flag = dao.createSysMessage(sysMsg);
//    	return flag;
//    }
//    /**
//     * 
//     * @param sysMessage
//     * @return
//     */
//    public boolean createSysMsg(String userid,String title,String content,String url){
//    	SysMessageDAO dao = new SysMessageDAO();
//    	SysMessage sysMsg = new SysMessage();
//    	sysMsg.setId(String.valueOf(SequenceUtil.getInstance().getSequenceIndex(SequenceConstant.SYS_System)));
//    	sysMsg.setType("系统通知");
//    	sysMsg.setPriority("1");
//    	sysMsg.setStatus("0");
//    	sysMsg.setReceiver(userid);
//    	sysMsg.setUrl(url);
//    	boolean flag = dao.createSysMessage(sysMsg);
//    	return flag;
//    }
//    /**
//     * 
//     * @param sysMessage
//     * @return
//     */
//    public boolean createSysMsg(String userid,String title,String content){
//    	SysMessageDAO dao = new SysMessageDAO();
//    	SysMessage sysMsg = new SysMessage();
//    	sysMsg.setId(String.valueOf(SequenceUtil.getInstance().getSequenceIndex(SequenceConstant.SYS_System)));
//    	sysMsg.setType("系统通知");
//    	sysMsg.setPriority("1");
//    	sysMsg.setStatus("0");
//    	sysMsg.setReceiver(userid);
//    	sysMsg.setUrl("");
//    	boolean flag = dao.createSysMessage(sysMsg);
//    	return flag;
//    }
}
