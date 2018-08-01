package com.iwork.core.organization.impl;

import net.sf.json.JSONObject;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.weixin.core.common.URLConstants;
import com.iwork.app.weixin.org.model.WeiXinDepartment;
import com.iwork.core.organization.interceptor.OrgExtendDepartmentInterface;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.wechat.pojo.AccessToken;
import com.iwork.wechat.util.WeixinUtil;

public class OrgExtendDeptForWeiXinImpl implements OrgExtendDepartmentInterface {

	public void addDepartment(OrgDepartment model){
		if(SystemConfig._weixinConf.getServer().equals("on")){
			AccessToken at = WeixinUtil.getInstance().getAccessToken(SystemConfig._weixinConf.getCorpId(), SystemConfig._weixinConf.getConfigSecret());
			String url = URLConstants.WEIXIN_POST_ORG_DEPARTMENT_CREATE.replace("ACCESS_TOKEN", at.getToken());
			if(model!=null){
				WeiXinDepartment dept = new WeiXinDepartment();
				dept.setId(model.getId()+"");
				dept.setName(model.getDepartmentname());
				dept.setOrder(model.getId()+"");
				if(model.getParentdepartmentid().equals(new Long(0))){
					dept.setParentid("1");
				}else{
					dept.setParentid(model.getParentdepartmentid()+"");
				}
				String outputStr = JSONObject.fromObject(dept).toString();
				JSONObject josnObject =  WeixinUtil.getInstance().httpRequest(url, "POST", outputStr);
			}
		}
	}
	public void updateDepartment(OrgDepartment model){
		
	}
	public void removeDepartment(OrgDepartment model){
		
	}

}
