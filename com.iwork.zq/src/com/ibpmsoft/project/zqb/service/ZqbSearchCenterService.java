package com.ibpmsoft.project.zqb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.ibpmsoft.project.zqb.common.ZQBConstants;
import com.ibpmsoft.project.zqb.dao.ZqbProjectManageDAO;
import com.ibpmsoft.project.zqb.util.CheckCurrentUserTypeUtil;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;

public class ZqbSearchCenterService {
	
	private static final String ZQB_CUSTOMER_PURVIEW_UUID = "84ff70949eac4051806dc02cf4837bd9";
	private static final String ZQB_CUSTOMER_DATA_UUID = "a243efd832bf406b9caeaec5df082e28";
	private static final String ZQB_COMPANY_DATA_UUID = "d1885d90bfff4cdc8423778bca98e273";
	private static final String ZQB_INUSER_DATA_UUID = "e7b402f4d9124214a4d30ea17d52ccd6";
	private static final String ZQB_OUTUSER_DATA_UUID = "499f1b08aadd4d1399ba202fb939b298";
	private static final String ZQB_STOCK_DATA1_UUID = "d5e1195c66f24242b9703506ca410702";
	private static final String ZQB_STOCK_DATA2_UUID = "06182b506282440fbc30a08e5d024317";
	private static final String ZQB_STOCK_DATA3_UUID = "626bedb24cba4c099b53bf82dd5a96ab";
	private static final String ZQB_STOCK_DATA4_UUID = "3169931af8cf42e7800d9f41ff7919b9";
	private static final String ZQB_EVENT_DATA_UUID="3bcf5cecda9942bb8b9cda55afb76d16";
	
	private ZqbProjectManageDAO zqbProjectManageDAO;
	/**
	 * 获得客户数据列表
	 * @return
	 */
	public List<HashMap> getCustomerListData(){
		List<HashMap> datalist = new ArrayList();
		//判断当前用户身份
		List<HashMap> list = null;
		String roleType = CheckCurrentUserTypeUtil.getInstance().getCurrentRoleType();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();//角色id
				if(roleType.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_CHANG)||Integer.parseInt(roleid)==9){
					list = this.getAllCustomerList();
				}else{
					list = this.getCurrentCustomerList();
				}
			
		for(HashMap map:list){
			if(map.get("KHBH")!=null){
				String bh = map.get("KHBH").toString();
				HashMap data = this.getCustomerData(bh);
				if(data!=null){
					datalist.add(data);
				}
			}
		}
		return datalist;
	}
	
	/**
	 * 获得客户数据列表
	 * @return
	 */
	public List<HashMap> getCompanyListData(String customerno){
		
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("KHBH", customerno); 
		List<HashMap> ll=new ArrayList<HashMap>();
		List<HashMap> slist=new ArrayList<HashMap>();
		List<HashMap> subList = DemAPI.getInstance().getList(ZQB_COMPANY_DATA_UUID, conditionMap, "GSMC");
		for (HashMap hashMap : subList) {
			if(customerno.equals(hashMap.get("KHBH").toString())){
				ll.add(hashMap);
			}
		}
		slist.addAll(ll);
				
		return slist;
	}
	
	/**
	 * 获得客户数据列表
	 * @return
	 */
	public List<HashMap> getInUserListData(String customerno){
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("KHBH", customerno); 
		List<HashMap> ll=new ArrayList<HashMap>();
		List<HashMap> slist=new ArrayList<HashMap>();
		List<HashMap> subList = DemAPI.getInstance().getList(ZQB_INUSER_DATA_UUID, conditionMap, "XM");
		for(HashMap map:subList){
			if(map.get("ZP")!=null&&!map.get("ZP").toString().equals("")){
				String zpUUID = map.get("ZP").toString();
				FileUpload fileUpload = FileUploadAPI.getInstance().getFileUpload(zpUUID);
				map.put("PIC_PATH", fileUpload.getFileUrl());
			}
		}
		for (HashMap hashMap : subList) {
			if(customerno.equals(hashMap.get("KHBH").toString())){
				ll.add(hashMap);
			}
		}
		slist.addAll(ll);
		return slist;
	}
	/**
	 * 获得股份数据列表
	 * @return
	 */
	public List<HashMap> getInStockData1(String customerno){
		HashMap conditionMap = new HashMap(); 
		//conditionMap.put("KHBH", customerno); 
		List<HashMap> slist = DemAPI.getInstance().getList(ZQB_STOCK_DATA1_UUID, conditionMap, "ID DESC");
		List<HashMap> returnlist=new ArrayList<HashMap>();
		for(HashMap map:slist){
			if(map.get("KHBH")!=null&&map.get("KHBH").toString().equals(customerno)){
				returnlist.add(map);
			}
		}
		return returnlist;
	}
	/**
	 * 获得股份数据列表
	 * @return
	 */
	public List<HashMap> getInStockData2(String customerno){
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("KHBH", customerno); 
		List<HashMap> slist = DemAPI.getInstance().getList(ZQB_STOCK_DATA2_UUID, conditionMap, "ID DESC");
		List<HashMap> returnlist=new ArrayList<HashMap>();
		for(HashMap map:slist){
			if(map.get("KHBH")!=null&&map.get("KHBH").toString().equals(customerno)){
				returnlist.add(map);
			}
		}
		return returnlist;
	}
	/**
	 * 获得股份数据列表
	 * @return
	 */
	public List<HashMap> getInStockData3(String customerno){
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("KHBH", customerno); 
		List<HashMap> slist = DemAPI.getInstance().getList(ZQB_STOCK_DATA3_UUID, conditionMap, "ID DESC");
		List<HashMap> returnlist=new ArrayList<HashMap>();
		for(HashMap map:slist){
			if(map.get("KHBH")!=null&&map.get("KHBH").toString().equals(customerno)){
				returnlist.add(map);
			}
		}
		return returnlist;
	}
	/**
	 * 获得股份数据列表
	 * @return
	 */
	public List<HashMap> getInStockData4(String customerno){
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("KHBH", customerno); 
		List<HashMap> slist = DemAPI.getInstance().getList(ZQB_STOCK_DATA4_UUID, conditionMap, "ID DESC");
		List<HashMap> returnlist=new ArrayList<HashMap>();
		for(HashMap map:slist){
			if(map.get("KHBH")!=null&&map.get("KHBH").toString().equals(customerno)){
				returnlist.add(map);
			}
		}
		return returnlist;
	}
	
	
	/**
	 * 获得客户数据列表
	 * @return
	 */
	public List<HashMap> getOutListData(String customerno){
		
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("KHBH", customerno); 
		List<HashMap> ll=new ArrayList<HashMap>();
		List<HashMap> slist=new ArrayList<HashMap>();
		List<HashMap> subList = DemAPI.getInstance().getList(ZQB_OUTUSER_DATA_UUID, conditionMap, "XM");
		for (HashMap hashMap : subList) {
			if(customerno.equals(hashMap.get("KHBH").toString())){
				List<HashMap> list = DemAPI.getInstance().getFromSubData(
						Long.parseLong(hashMap.get("INSTANCEID").toString()), "SUBFORM_RZXX");// 获取参与人员
				Collections.reverse(list);
				for (HashMap hashMap2 : list) {
					if(hashMap2.get("RZZT").toString().equals("现任")){
						if (hashMap2.get("DWMC") != null)
							hashMap.put("DWMC", hashMap2.get("DWMC").toString());
						if (hashMap2.get("ZW") != null)
							hashMap.put("ZW", hashMap2.get("ZW").toString());
					}
					break;
				}
				ll.add(hashMap);
			}
		}
		
		slist.addAll(ll);
		return slist;
	}
	
	/**
	 * 获得指定编号的客户数据信息
	 * @param custmerno
	 * @return
	 */
	public HashMap getCustomerData(String custmerno){
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("CUSTOMERNO", custmerno); 
		List<HashMap> list = DemAPI.getInstance().getAllList(ZQB_CUSTOMER_DATA_UUID, conditionMap, null);
		HashMap data = null;
		if(list!=null&&list.size()>0){
			data = list.get(0);
		}
		return data;
	}
	/** 
	 * 获得当前客户列表
	 * @return
	 */
	private List<HashMap> getCurrentCustomerList(){
		HashMap conditionMap = new HashMap(); 
		List<HashMap>  returnList=new ArrayList<HashMap>();
		String userFullName = UserContextUtil.getInstance().getCurrentUserFullName();
//		
//		if(SecurityUtil.isSuperManager()){
//			conditionMap = null;
//		}else{
//			conditionMap.put("KHFZR", userFullName); 
//		}
		List<HashMap> list = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();//角色id
		List<OrgUserMap> usermaplist=UserContextUtil.getInstance().getCurrentUserContext().get_userMapList();
		boolean isjrdd=false;
		if(usermaplist.size()>0){
			for(OrgUserMap oum:usermaplist){
				String rolejrid=oum.getOrgroleid();
				if(rolejrid.equals("4")){
					isjrdd=true;
					break;
				}
			}
		}
		 if(Integer.parseInt(roleid)==4||isjrdd){//持续督导或者兼任持续督导的角色或质控部负责人
			  for(HashMap map:list){
					//督导角色所查询到的客户
					if((map.get("KHFZR")!=null&&!map.get("KHFZR").toString().equals("")&&map.get("KHFZR").toString().equals(userFullName))
							||(map.get("GGFBR")!=null&&!map.get("GGFBR").toString().equals("")&&map.get("GGFBR").toString().equals(userFullName))
							||(map.get("ZZCXDD")!=null&&!map.get("ZZCXDD").toString().equals("")&&map.get("ZZCXDD").toString().equals(userFullName))
							||(map.get("FHSPR")!=null&&!map.get("FHSPR").toString().equals("")&&map.get("FHSPR").toString().equals(userFullName))
							||(map.get("ZZSPR")!=null&&!map.get("ZZSPR").toString().equals("")&&map.get("ZZSPR").toString().equals(userFullName))
							||(map.get("GGFBR")!=null&&!map.get("GGFBR").toString().equals("")&&map.get("GGFBR").toString().equals(userFullName))
							){
						returnList.add(map);
					}
				}
		  }
		for (int i = 0; i < returnList.size(); i++)
		{
			for (int j = returnList.size() - 1 ; j > i; j--) 
			{
				
				if (returnList.get(i).get("KHMC").equals(returnList.get(j).get("KHMC")))
				{
					returnList.remove(j);
				}
				
			}
		}
		return returnList;
	}
	/** 
	 * 获得当前客户列表
	 * @return
	 */
	private List<HashMap> getAllCustomerList(){
		HashMap conditionMap = new HashMap(); 
		String userFullName = UserContextUtil.getInstance().getCurrentUserFullName();
		//List<HashMap> list = DemAPI.getInstance().getAllList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
		if(zqbProjectManageDAO==null){
			zqbProjectManageDAO=(ZqbProjectManageDAO) SpringBeanUtil.getBean("zqbProjectManageDAO");
		}
		List<HashMap> list = zqbProjectManageDAO.getChiXU();
		for (int i = 0; i < list.size(); i++)
        {
            for (int j = list.size() - 1 ; j > i; j--) 
            {

                if (list.get(i).get("KHMC").equals(list.get(j).get("KHMC")))
                {
                    list.remove(j);
                }

            }
        }
		return list;
	}
	/**
	 * 获得信息披露列表
	 * @return
	 */
	public List<HashMap> getInEventListData(String customerno){
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("KHBH", customerno); 
		List<HashMap> slist = DemAPI.getInstance().getList(ZQB_EVENT_DATA_UUID, conditionMap, "SBRQ DESC");
		List<HashMap> list=null;
		for (HashMap hashMap : slist) {
			StringBuffer sb=new StringBuffer();
			String jydx="";
			list = DemAPI.getInstance().getFromSubData(Long.parseLong(hashMap.get("INSTANCEID").toString()), "SUBFORM_JYDX");
			for (HashMap map : list) {
				sb.append(map.get("JYDXMC")+", ");
			}
			if(sb.length()>0){
				jydx=sb.substring(0,sb.lastIndexOf(", "));
			}
			hashMap.put("JYDX", jydx);
		}
      return slist;
	}
}













