package com.iwork.plugs.eam.action;



import java.util.List;
import com.iwork.plugs.eam.constant.EAM_Constants;
import com.iwork.plugs.eam.service.IWorkAssetTypeService;
import com.iwork.sdk.DemAPI;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 资产类别管理
 * @author ydytx_000
 *
 */
public class IWorkAssetTypeAction extends ActionSupport {
	private IWorkAssetTypeService iWorkAssetTypeService;
	private List typelist;
	private Long typeFormId;
	private Long typeDemId;
	/**
	 * 类别管理
	 * @return
	 */
	public String index(){
		typelist = DemAPI.getInstance().getAllList(EAM_Constants.EAM_TYPE_DEM_UUID, null,null);
		return SUCCESS;
	}
	
	public List getTypelist() {
		return typelist;
	}
	public void setTypelist(List typelist) {
		this.typelist = typelist;
	}
	public IWorkAssetTypeService getiWorkAssetTypeService() {
		return iWorkAssetTypeService;
	}
	public void setiWorkAssetTypeService(IWorkAssetTypeService iWorkAssetTypeService) {
		this.iWorkAssetTypeService = iWorkAssetTypeService;
	}
	
}
