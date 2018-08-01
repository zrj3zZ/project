package com.iwork.plugs.bgyp.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.bgyp.dao.IWorkbgypManageDao;
import com.iwork.plugs.bgyp.model.IWorkbgypModel;
import com.iwork.plugs.bgyp.model.ShopCarModel;
import com.iwork.plugs.resoucebook.dao.SpaceManageDao;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;

/**
 * 初始化购物车中的办公用品
 * @author YangDayong
 *
 */
public class InitBgypShopListTriggerEvent extends ProcessStepTriggerEvent {
	private UserContext _me;
	private SpaceManageDao spaceManageDao;
	private HashMap params;

	public InitBgypShopListTriggerEvent(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	} 
	
	/**
	 * 我被初始化了
	 */
	public boolean execute() {
		boolean flag = false;
		Long instanceId = new Long(0);
		Object obj = params.get(this.PROCESS_PARAMETER_INSTANCEID);
		String actDefid = (String)params.get(this.PROCESS_PARAMETER_ACTDEFID);
		Long formId = (Long)params.get(this.PROCESS_PARAMETER_FORMID);
		if(obj!=null){
			instanceId = (Long)obj;
			//获取 
			IWorkbgypManageDao dao = new IWorkbgypManageDao();
			List<HashMap> datalist = new ArrayList();
			List<ShopCarModel> list = dao.getCurrentSelectList();
			if(list!=null){
				for(ShopCarModel model:list){
					IWorkbgypModel ibm = dao.getDataModel(model.getNo());
					HashMap hash = new HashMap();
					hash.put("SPBH",model.getNo());
					hash.put("SPMC",model.getName());
					hash.put("LBBH",ibm.getLbbh());
					hash.put("LBMC",ibm.getLbmc());
					hash.put("DW",ibm.getJldw());
					hash.put("PRICE",ibm.getDj());
					hash.put("SQSL",model.getNum());
					hash.put("HJJG",model.getNum()*ibm.getDj());
					datalist.add(hash);
				} 
//				flag = ProcessAPI.getInstance().saveFormDatas(actDefid, instanceId,formId, "SUBFORM_BGYPSQDZB", datalist, false);
				
				//清空购物车  
				dao.removeShopAll();
			}
		}
		return flag;
	}
}
