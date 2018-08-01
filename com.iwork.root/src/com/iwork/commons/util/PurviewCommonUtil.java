package com.iwork.commons.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgGroupService;
import com.iwork.core.util.SpringBeanUtil;

public class PurviewCommonUtil extends HibernateDaoSupport {
	public static final String COMPANY_PREFIX = "COM";
	public static final String DEPARTMENT_PREFIX = "DEPT";
	public static final String ROLE_PREFIX = "ROLE";
	public static final String GROUP_PREFIX = "GROUP";
	public static final String USER_PREFIX = "USER";
	
	private OrgGroupService groupService;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private String code;
	private static PurviewCommonUtil instance;  
    private static Object lock = new Object();  
	 public static PurviewCommonUtil getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new PurviewCommonUtil();  
	                }
	            }
	        }  
	        return instance;  
	 }
	 
	public PurviewCommonUtil() {
		super();
		if(groupService==null){
			groupService = (OrgGroupService)SpringBeanUtil.getBean("orgGroupService");
		}
		if(sessionFactory==null){
			sessionFactory = (SessionFactory)SpringBeanUtil.getBean("sessionFactory");
		}
		if(hibernateTemplate==null){
			this.hibernateTemplate = new HibernateTemplate(sessionFactory);
		}
	}
	
	/**
	 * 解析地址编码，获取人员列表，地址编码code格式:company:{1} DEPT:{6,7} ROLE:{1,2} GROUP:{0006,0007} USER:{zhangshan}
	 * @param code
	 * @return 人员列表，List中存放OrgUser.userid
	 */
	public List getUserListFromAddressCode(String code) { 
		
		if (code == null) {
			return null;
		}
		List userIdList = new ArrayList(); //存放未去重的userid list
		List<OrgUser> userList = new ArrayList(); //存放部分OrgUser对象的list
		Map userMapFromGroup = new HashMap(); //存放从组（Group）中获得的<OrgUser.userid, OrgUser>映射 Map
		String[] fields = code.toUpperCase().trim().split(" ");
		String[] companys = null;
		String[] depts = null;
		String[] roles = null;
		String[] groups = null;
		String[] users = null;
		for(String field : fields) { // 分别获取不同类别编码的值数组
			String keys = field.trim().substring(field.indexOf('{') + 1, field.lastIndexOf('}'));
			if (field.startsWith(COMPANY_PREFIX + ":")) {
				companys = keys.split(",");				
			} else if (field.startsWith(DEPARTMENT_PREFIX + ":")) {
				depts = keys.split(",");
			} else if (field.startsWith(ROLE_PREFIX + ":")) {
				roles = keys.split(",");
			} else if (field.startsWith(GROUP_PREFIX + ":")) {
				groups = keys.split(",");
			} else if (field.startsWith(USER_PREFIX + ":")) {
				users = keys.split(",");
			}
		}
		List params = new ArrayList();
		StringBuffer cond = new StringBuffer();
		StringBuffer groupCond = new StringBuffer();
		if (companys != null) { // 公司 从OrgUser查询
			for (String company : companys) {
				cond.append(" or companyid = ?");
				params.add(company);
			}
		}
		if (depts != null) { // 部门直接从GroupService API调用，API递归查询
			for (String dept : depts) {
				userList.addAll(groupService.getDeptPersonList(Long.parseLong(dept))); // 添加从部门中解析获得的OrgUser对象List
			}
		}
		if (roles != null) { // 角色从OrgUser查询
			for (String role : roles) {
				cond.append(" or orgroleid = ?");
				params.add(role);
			}
		}
		if (users != null) { // 用户Id从OrgUser查询
			for (String user : users) {
				cond.append(" or upper(userid) = ?");
				params.add(user);
			}
		}
		
		if (groups != null) { // 团队从GroupService API调用，API递归查询
			for (String group : groups) {
				userMapFromGroup.putAll(groupService.getGroupPersonList(group));
			}
		}
		Object[] values = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			values[i] = params.get(i);
		}
		String sql = " FROM OrgUser where 1 <> 1 " + cond.toString();		
		HibernateTemplate ht = this.hibernateTemplate;
		List<OrgUser> list =  ht.find(sql,values);		
		userList.addAll(list); //添加从OrgUser中的的OrgUser对象List
		
		// 从userList中获得OrgUser对象的userid，并通过set去重
		Set userIdSet = new HashSet(); 
		for (OrgUser user : userList) {
			userIdSet.add(user.getUserid());
		}
		
		// 获得userid list
		userIdList.addAll(userIdSet); 
		userIdList.addAll(userMapFromGroup.keySet());		
		
		// 再次去重
		List<String> resultList = new ArrayList<String>(new LinkedHashSet<String>(userIdList));
		
		return resultList;
	}
	
	/**
	 * 查询指定userId是否在地址簿编码中
	 * @param userId
	 * @param addressCode
	 * @return
	 */
	public boolean checkUserInPurview(String userId, String addressCode) {
			List<String> resultList = this.getUserListFromAddressCode(addressCode);
			return !(new LinkedHashSet<String>(resultList)).add(userId); 
	}
	
	
	/**
	 * 根据编码反向生成页面选择列表
	 * @param code
	 * @return
	 */
	public static final String codeToSelectHtml(String code) {
		if(code==null||"".equals(code)){
			return "<select id='toSelect' name='to' size='10' multiple style='border-width:0px;width:210px;height:400px;' " +
					"ondblclick='deleteSelect(document.all.to);'></select>";
		}
		JSONObject json = JsonUtil.strToJson(code);
		if ("".equals(code.trim())||json==null) {
			return "<select id='toSelect' name='to' size='10' multiple style='border-width:0px;width:210px;height:400px;' " +
					"ondblclick='deleteSelect(document.all.to);'></select>";
		}
        StringBuffer html = new StringBuffer();
        html.append("<select id='toSelect' name='to' size='10' multiple style='border-width:0px;width:210px;height:400px;' ondblclick='deleteSelect(document.all.to);'>");
        if(json.containsKey(COMPANY_PREFIX)){
			List list = (List)json.get(COMPANY_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String id = (String)map.get("id");
				String name = (String)map.get("name");
				html.append("<option value='com_" + id + "'>");
				html.append("公司|" + name).append("</option>\n");
			}
		}
        if(json.containsKey(DEPARTMENT_PREFIX)){
			List list = (List)json.get(DEPARTMENT_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String id = (String)map.get("id");
				String name = (String)map.get("name");
				html.append("<option value='dept_" + id + "'>");
				html.append("部门|" + name).append("</option>\n");
			}
		}
        if(json.containsKey(ROLE_PREFIX)){
			List list = (List)json.get(ROLE_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String id = (String)map.get("id");
				String name = (String)map.get("name");
				html.append("<option value='role_" + id + "'>");
				html.append("角色|" + name).append("</option>\n");
			}
		}
        if(json.containsKey(GROUP_PREFIX)){
			List list = (List)json.get(GROUP_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String id = (String)map.get("id");
				String name = (String)map.get("name");
				html.append("<option value='group_" + id + "'>");
				html.append("团队|" + name).append("</option>\n");
			}
		}
        if(json.containsKey(USER_PREFIX)){
			List list = (List)json.get(USER_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String id = (String)map.get("id");
				String name = (String)map.get("name");
				html.append("<option value='user_" + id + "'>");
				html.append("人员|" + name).append("</option>\n");
			}
		}
		html.append("</select>");
		return html.toString();
	}
	
	
	/**
	 * 根据编码反向生成页面选择列表
	 * @param code
	 * @return
	 */
	public static final String userToSelectHtml(String code) {
		String[] users = code.split(",");     //以英文逗号相隔
		if (code == null || code.trim().equals("")&&users.length>0) {
			return "<select id='toSelect' name='to' size='10' multiple style='border-width:0px;width:210px;height:400px;' " +
					"ondblclick='deleteSelect(document.all.to);'></select>";
		}	
        StringBuffer html = new StringBuffer();
        html.append("<select id='toSelect' name='to' size='10' multiple style='border-width:0px;width:210px;height:400px;' ondblclick='deleteSelect(document.all.to);'>");
        for (String user : users) {
        	html.append("<option value='" + user + "'>");
			html.append(user).append("</option>\n");
        }
        html.append("</select>");
		return html.toString();
	}
	
	public static final List<String> getTypeList(String code, String prefix) {
		List<String> list = new ArrayList<String>();
		if(!list.isEmpty()){
			
			JSONObject json = JsonUtil.strToJson(code);
			if (json!=null) {
				if(json.containsKey(prefix)){
					List list2 = (List)json.get(prefix);
					for(int i=0;i<list2.size();i++){
						Map map = (Map)list2.get(i);
						String id = (String)map.get("id");
						String name = (String)map.get("name");
						list.add(id);
					}
				}
			}
		}
		return list;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
	