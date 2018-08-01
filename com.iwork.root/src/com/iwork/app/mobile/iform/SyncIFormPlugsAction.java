package com.iwork.app.mobile.iform;

import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.addressbook.service.MultiAddressBookService;
import com.iwork.plugs.addressbook.service.RadioAddressBookService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 移动端显示的地址簿
 * @author YangDayong
 *
 */
public class SyncIFormPlugsAction extends ActionSupport {
	private RadioAddressBookService radioAddressBookService;
	private MultiAddressBookService multiAddressBookService;
	private String fieldName ;
	private String searchkey ;
	/**
	 * 加载单选地址簿页
	 * @return
	 */
	public String showRadioAddressPage(){
		
		
		return SUCCESS;
	}
	/**
	 * 加载单选地址簿页
	 * @return
	 */
	public void searchUserList(){
		if(fieldName!=null&&searchkey!=null){
			String list = radioAddressBookService.showRadioAddressList(fieldName,searchkey.toUpperCase());
			ResponseUtil.write(list); 
		} 
	}
	
	/**
	 * 加载多选地址簿页
	 * @return
	 */
	public String showMultiAddressPage(){
		return SUCCESS;
	}
	
	/**
	 * 加载单选地址簿页
	 * @return
	 */
	public void searchMultiUserList(){
		if(fieldName!=null&&searchkey!=null){ 
			String list = multiAddressBookService.showMultiAddressList(fieldName,searchkey.toUpperCase());
			ResponseUtil.write(list); 
		} 
	}
	public RadioAddressBookService getRadioAddressBookService() {
		return radioAddressBookService;
	}

	public void setRadioAddressBookService(
			RadioAddressBookService radioAddressBookService) {
		this.radioAddressBookService = radioAddressBookService;
	}
	public MultiAddressBookService getMultiAddressBookService() {
		return multiAddressBookService;
	}
	public void setMultiAddressBookService(
			MultiAddressBookService multiAddressBookService) {
		this.multiAddressBookService = multiAddressBookService;
	}
	public String getSearchkey() {
		return searchkey;
	}
	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	
}
