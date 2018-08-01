package com.iwork.plugs.resoucebook.service;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.plugs.resoucebook.dao.SpaceManageDao;
import com.iwork.plugs.resoucebook.model.IworkRmBase;
import com.iwork.plugs.resoucebook.util.RMConst;

public class ResourceBookBaseInfoService {
	private static Logger logger = Logger.getLogger(ResourceBookBaseInfoService.class);
	private SpaceManageDao spaceManageDao;
	public final RMConst rmconst=new RMConst();
	public boolean save(IworkRmBase model){
		boolean flag = false;
		if(model!=null){
			if(model.getId()==null){
				spaceManageDao.addBase(model);
			}else{
				spaceManageDao.updateBase(model);
			}
			flag = true;
		}else{
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 删除基础信息
	 * @param rms
	 */
	public void remove(String ids){
		String[] idlist = ids.split(",");
		if(idlist!=null&&idlist.length>0){
			for(String item:idlist){
				if(!"".equals(item.trim())){
					IworkRmBase model=spaceManageDao.getBaseModel(new Long(item));
					if(model!=null)
					spaceManageDao.removeBases(model);
				}
			}
			
		}
	}

	
	/**
	 * 加载基本信息JSON
	 * @param spaceId
	 * @return
	 */
	public String showListJson(Long spaceId){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			List list = spaceManageDao.getBaseList(spaceId);
			for(int i = 0;i<list.size();i++){
				IworkRmBase base= (IworkRmBase)list.get(i);	
					Map<String,Object> item = new HashMap<String,Object>();
					long cid=base.getId();
					long spaceid=base.getSpaceid();
					String spacename=base.getSpacename();
					String resouceid=base.getResouceid();
					String resoucename=base.getResoucename()==null?"":base.getResoucename();
					String pictureurl=base.getPicture()==null?"":base.getPicture();
					String picture="<img src="+pictureurl+"  width=16 height=16 onmouseout=\"hiddenPic();\"  onMouseOver=\"showPic('"+pictureurl+"');\" /> ";
					String para1=base.getParameter1()==null?"":base.getParameter1();
					String para2=base.getParameter2()==null?"":base.getParameter2();
					String para3=base.getParameter3()==null?"":base.getParameter3();
					String para4=base.getParameter4()==null?"":base.getParameter4();
					String para5=base.getParameter5()==null?"":base.getParameter5();
					long status=base.getStatus();
					String statusshow=rmconst.spacestatus.get(String.valueOf(status)).toString();
					String memo=base.getMemo()==null?"":base.getMemo();
					item.put("id", cid);
					item.put("spaceid",spaceid );	
					item.put("spacename",spacename );		
					item.put("resouceid",resouceid);
					item.put("resoucename", resoucename);
					item.put("picture",picture);
					item.put("para1",para1);
					item.put("para2",para2);
					item.put("para3",para3); 
					item.put("para4",para4);
					item.put("para5",para5);
					item.put("status",statusshow);
					item.put("memo", memo);
					item.put("opera", "<a href=\"javascript:editBase("+cid+");\">编辑</a>");
					items.add(item);			
			}
		}catch(Exception e){
			logger.error(e,e);
		}		
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}

	public SpaceManageDao getSpaceManageDao() {
		return spaceManageDao;
	}

	public void setSpaceManageDao(SpaceManageDao spaceManageDao) {
		this.spaceManageDao = spaceManageDao;
	}
	
}
