package com.iwork.plugs.sysletters.service;

import java.util.ArrayList;
import java.util.List;

import com.iwork.plugs.sysletters.dao.SysLettersControlsDao;
import com.iwork.plugs.sysletters.model.SysLettersContent;
import com.iwork.plugs.sysletters.model.SysLettersDetailInfo;

public class SysLettersControlsService {
	private SysLettersControlsDao sysletterscontrolsdao;
	
	public SysLettersContent getLettersContent(Long letterId,String receiveUserId){
		SysLettersContent model = sysletterscontrolsdao.getLettersContent(letterId,receiveUserId);
		return model;
	}
	public int changeLetterFlag(Long letterId,String receiveUserId,String ownerId,String flag){
		int count = sysletterscontrolsdao.changeLetterFlag( letterId, receiveUserId, ownerId, flag);
		return count;
	}
	public Long isExitsLettersReply(Long letterId ,String receiveUserId,Long beforeId){
		Long afterId = sysletterscontrolsdao.isExitsLettersReply(letterId, receiveUserId, beforeId);
		return afterId;
	}
	public Long getLastLettersReply(Long letterId ,String receiveUserId){
		Long lastReplyId = sysletterscontrolsdao.getLastLettersReply(letterId, receiveUserId);
		return lastReplyId;
	}
	public int changeLetterMoreFlag(String[] letterIds,String receiveUserId,String ownerId,String flag){
		List<Long> list = new ArrayList<Long>();
		for(int i=0;i<letterIds.length;i++){
			list.add(Long.valueOf(letterIds[i]));
		}
		int count = sysletterscontrolsdao.changeLetterMoreFlag(list, receiveUserId, ownerId, flag);
		return count;
	}
	public List<SysLettersDetailInfo> list(String userId){
		List<SysLettersDetailInfo> list = new ArrayList<SysLettersDetailInfo>();
		list = sysletterscontrolsdao.list(userId);
		return list;
	}
	public int delLetterReply(Long id){
		int count = sysletterscontrolsdao.delLetterReply(id);
		return count;
	}
	public List<SysLettersDetailInfo> getLettersReply(Long letterId ,String receiveUserId){
		List<SysLettersDetailInfo> list = new ArrayList<SysLettersDetailInfo>();;
		list = sysletterscontrolsdao.getLettersReply(letterId, receiveUserId);
		return list;
	}
	public void createLetter(SysLettersDetailInfo syslettersdetailinfo,SysLettersContent sysletterscontent){
		if(syslettersdetailinfo!=null){
			String tempReceiveId = syslettersdetailinfo.getReceiveUserId();
			String tempReceiveName = syslettersdetailinfo.getReceiveUserName();
			if(tempReceiveId!=null&&tempReceiveName!=null){
				String ReceiveIds[] = tempReceiveId.split(",");
				String ReceiveNames[] = tempReceiveName.split(",");
				sysletterscontrolsdao.createLetter(syslettersdetailinfo,sysletterscontent,ReceiveIds,ReceiveNames);
			} 
		}
	}
	/**
	 * 删除站内信详细信息
	 * @param letterIds
	 * @param userId
	 * @return
	 */
	public int delLettersList(String letterIds,String userId){
		int count = 0;
		List<Long> list = new ArrayList<Long>();
		if(letterIds!=null&&!"".equals(letterIds)){
			String[] strArr = letterIds.split(",");
			for(int i=0;i<strArr.length;i++){
				list.add(Long.valueOf(strArr[i]));
			}
		}
		count = sysletterscontrolsdao.delLettersList(list, userId);
		return count;
	}
	public void createLetterReply(SysLettersDetailInfo syslettersdetailinfo,String[] ReceiveIds,String[] ReceiveNames,String content){
		sysletterscontrolsdao.createLetterReply(syslettersdetailinfo, ReceiveIds, ReceiveNames,content);
	}
	/**
	 * 用于现有模式，一对一聊天
	 * @param syslettersdetailinfo
	 * @param ReceiveIds
	 * @param ReceiveNames
	 * @param content
	 */
	public void createLetterReply_single(SysLettersDetailInfo syslettersdetailinfo,String ReceiveIds,String ReceiveNames,String content){
		sysletterscontrolsdao.createLetterReply_single(syslettersdetailinfo, ReceiveIds, ReceiveNames, content);
	}
	public SysLettersControlsDao getSysletterscontrolsdao() {
		return sysletterscontrolsdao;
	}
	public void setSysletterscontrolsdao(SysLettersControlsDao sysletterscontrolsdao) {
		this.sysletterscontrolsdao = sysletterscontrolsdao;
	}
	
}
