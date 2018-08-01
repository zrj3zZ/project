package com.iwork.app.weixin.process.action.qy.pojo.resp;
/**
 * 响应消息之音乐消息
 * @author sunlight
 *
 */
public class MusicMessage extends BaseMessage {
	/**
	 * 音乐  
	 */
    private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}  
    
    
}
