package com.iwork.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.service.OrgGroupService;
import com.iwork.core.util.SpringBeanUtil;

public class AddressBookUtil extends HibernateDaoSupport {
	private static Logger logger = Logger.getLogger(AddressBookUtil.class);
	public static final String COMPANY_PREFIX = "COMPANY";
	public static final String DEPARTMENT_PREFIX = "DEPT";
	public static final String ROLE_PREFIX = "ROLE";
	public static final String GROUP_PREFIX = "GROUP";
	public static final String USER_PREFIX = "USER";
	
	private OrgGroupService groupService;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	
	public AddressBookUtil() {
		super();
		groupService = (OrgGroupService)SpringBeanUtil.getBean("orgGroupService");
		sessionFactory = (SessionFactory)SpringBeanUtil.getBean("sessionFactory");
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}
	
	 /**
	 * �鿴Ȩ������
	 * @param code
	 * @return
	 */
	public  static final  String showAuthority(String code) {
		JSONObject json = JsonUtil.strToJson(code);
		if (code == null || code.trim().equals("")||json==null) {
			return "";
		}
        StringBuffer returnString = new StringBuffer();
        if(json.containsKey(PurviewCommonUtil.COMPANY_PREFIX)){
			List list = (List)json.get(PurviewCommonUtil.COMPANY_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String name = (String)map.get("name");
				returnString.append("��˾|").append(name).append("<br/>");
			}
		}
        if(json.containsKey(PurviewCommonUtil.DEPARTMENT_PREFIX)){
			List list = (List)json.get(PurviewCommonUtil.DEPARTMENT_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String name = (String)map.get("name");
				returnString.append("����|").append(name).append("<br/>");
			}
		}
        if(json.containsKey(PurviewCommonUtil.ROLE_PREFIX)){
			List list = (List)json.get(PurviewCommonUtil.ROLE_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String name = (String)map.get("name");				
				returnString.append("��ɫ|").append(name).append("<br/>");
			}
		}
        if(json.containsKey(PurviewCommonUtil.GROUP_PREFIX)){
			List list = (List)json.get(PurviewCommonUtil.GROUP_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String name = (String)map.get("name");
				returnString.append("�Ŷ�|").append(name).append("<br/>");
			}
		}
        if(json.containsKey(PurviewCommonUtil.USER_PREFIX)){
			List list = (List)json.get(PurviewCommonUtil.USER_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String name = (String)map.get("name");
				returnString.append("��Ա|").append(name).append("<br/>");
			}
		}
		return returnString.toString();
	}
	
	/**
	 * ��ݱ��뷴�����ҳ��ѡ���б�
	 * @param code
	 * @return
	 */
	public static final String userToSelectHtml(String code) {
		if (code == null || code.trim().equals("")) {
			return "<select id='toSelect' name='to' size='10' multiple style='border-width:0px;width:210px;height:400px;' " +
					"ondblclick='deleteSelect(document.all.to);'></select>";
		}
		
		
		
		String keys = code.toUpperCase().trim().substring(code.indexOf('{') + 1, code.lastIndexOf('}'));
		String[] users = null;	
		if (code.startsWith(USER_PREFIX + ":")) {
			users = keys.split(",");
		}		
		
		StringBuffer userCond = new StringBuffer();		
		
		java.sql.Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        java.sql.ResultSet rs = null;
        
        StringBuffer html = new StringBuffer();
        html.append("<select id='toSelect' name='to' size='10' multiple style='border-width:0px;width:210px;height:400px;' ondblclick='deleteSelect(document.all.to);'>");
        try {
        
	        
			
			if (users != null) { // 
				List<String> params = new ArrayList<String>();
				for (String user : users) {
					userCond.append(" or upper(userid) = ?");
					params.add(user);
				}
				StringBuffer sql = new StringBuffer();
				sql.append("select userid as id, username as name  FROM orguser where 1 <> 1 " + userCond.toString());
				conn = DBUtil.open();
				stmt = conn.prepareStatement(sql.toString());
				for (int i = 0; i < params.size(); i++) {
					stmt.setString(i+1, params.get(i));
				}
				rs = stmt.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						String id = rs.getString("id");
						html.append("<option value='" + id + "'>");
						html.append("" + rs.getString("name") + "(" + id + ")").append("</option>\n");
					}
				}
			}			
			html.append("</select>");
        } catch (Exception e) {        	
            logger.error(e,e);
        } finally {
        	DBUtil.close(conn, stmt, rs);
    	}
		return html.toString();
	}
	
	public static final List<String> getTypeList(String code, String prefix) {
		List<String> list = new ArrayList<String>();
		
		if (code != null && !code.trim().equals("")) {
			String[] fields = code.toUpperCase().trim().split(" ");		
			String[] depts = null;
			
			for(String field : fields) { // �ֱ��ȡ��ͬ�������ֵ����
				String keys = field.trim().substring(field.indexOf('{') + 1, field.lastIndexOf('}'));
				if (field.startsWith(prefix + ":")) {
					depts = keys.split(",");
				} 
			}
			
			if (depts != null) {
				for (String dept : depts) {
					list.add(dept);
				}
			}
		}
		
		return list;
	}
	/**
	 * ���id��name���id[name]��uid
	 * @param id
	 * @param name
	 * @return
	 */
	public static String generateUid(String id,String name){
		return id+"["+name+"]";
	}
}
