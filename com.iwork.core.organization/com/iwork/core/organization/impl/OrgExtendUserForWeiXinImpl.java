package com.iwork.core.organization.impl;

import net.sf.json.JSONObject;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.weixin.core.common.URLConstants;
import com.iwork.app.weixin.org.model.WeiXinUser;
import com.iwork.core.organization.interceptor.OrgExtendUserInterface;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.wechat.util.WeixinUtil;
import com.iwork.wechat.pojo.AccessToken;

public class OrgExtendUserForWeiXinImpl implements OrgExtendUserInterface {

	public void addOrgUser(OrgUser model) {
		AccessToken at = WeixinUtil.getInstance().getAccessToken(SystemConfig._weixinConf.getCorpId(), SystemConfig._weixinConf.getConfigSecret());
		String url = URLConstants.WEIXIN_POST_ORG_USER_CREATE.replace("ACCESS_TOKEN", at.getToken());
		if(model!=null){
			WeiXinUser user = new WeiXinUser();
			user.setUserid(model.getUserid());
			user.setName(model.getUsername());
			user.setPosition(model.getPostsname());
			user.setDepartment(model.getDepartmentid()+"");
			user.setEmail(model.getEmail());
			user.setMobile(model.getMobile());
			user.setGender("1");
			user.setWeixinid(model.getWeixinCode());
			user.setEnable(1);
			String outputStr = JSONObject.fromObject(user).toString();
			JSONObject josnObject =  WeixinUtil.getInstance().httpRequest(url, "POST", outputStr);
			if(josnObject!=null){
				if(josnObject.getInt("errcode")==0){
					/*System.out.println("创建成功");*/
					//发送通知
					String noticeUrl =  "https://qyapi.weixin.qq.com/cgi-bin/invite/send?access_token="+at.getToken();
					 String data = "{\"userid\":\""+model.getUserid()+"\"}";
					JSONObject josnObject1 = WeixinUtil.getInstance().httpRequest(noticeUrl, "POST",data);
				}
			}
		}
		
	}
	/*public void removeOrgUser(OrgUser model) {
		System.out.println("removeUser");
	}
	public void updateOrgUser(OrgUser model) {
		System.out.println("updateUser");
	}
*/
}
