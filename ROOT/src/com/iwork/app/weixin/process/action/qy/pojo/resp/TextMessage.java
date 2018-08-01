package com.iwork.app.weixin.process.action.qy.pojo.resp;
/**
 * 响应消息之文本消息
 * @author sunlight
 *
 */
public class TextMessage extends BaseMessage {
	 /**
	  *  回复的消息内容  
	  */
    private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	} 
    
    
}
