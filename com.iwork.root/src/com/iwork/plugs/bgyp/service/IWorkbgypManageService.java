package com.iwork.plugs.bgyp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.plugs.bgyp.dao.IWorkbgypManageDao;
import com.iwork.plugs.bgyp.model.IWorkbgypModel;
import com.iwork.plugs.bgyp.model.IWorkbgypTypeModel;
import com.iwork.plugs.bgyp.model.ShopCarModel;
import com.iwork.sdk.FileUploadAPI;



public class IWorkbgypManageService {
	private IWorkbgypManageDao iworkbgypManageDao;
	
	/**
	 * 加载预定页面
	 * @return
	 */
	public String showPage(String lbbh,String content){
		if(lbbh==null || lbbh.equals("all") || "".equals(lbbh)){
			lbbh = null;
		}
		StringBuffer html = new StringBuffer();
		html.append("<table width='98%' style=\"border:1px solid #CCCCCC;\" cellspacing=0 cellpadding=0 align=center>").append("\n");
		List<IWorkbgypModel> list = iworkbgypManageDao.getDataList(lbbh,content);
		for(int i=0;i<list.size();i++){
			IWorkbgypModel model = list.get(i);
			html.append("	<tr>").append("\n");
			if(model!=null){
				html.append("	<td style=\"border-bottom:1px dotted #CCCCCC;padding:3px;\" width=50%>").append("\n");
				html.append(this.buildItem(model));
				html.append("	</td>").append("\n");
			}else{
				html.append("	<td>&nbsp</td>").append("\n");
			}
			i++;
			if(i<list.size()){
				model = list.get(i);
				html.append("	<td style=\"border-bottom:1px dotted #CCCCCC;padding:3px;\" width=50%>").append("\n");
				html.append(this.buildItem(model));
				html.append("	</td>").append("\n");
			}else{
				html.append("	<td>&nbsp</td>").append("\n");
			}
			html.append("	</tr>").append("\n");
		}
		html.append("	</table>").append("\n");
		return html.toString();
	}
	
	
	/**
	 * 加载类别标签
	 * @param spaceId
	 * @param lbbh
	 * @return
	 */
	public String showTypeTabHTML(String spaceId,String lbbh){
		StringBuffer html = new StringBuffer();
		if(lbbh==null || lbbh.equals("all") || "".equals(lbbh)){
			lbbh = null;
		}
		List<IWorkbgypTypeModel> list = iworkbgypManageDao.getTypeDataList(spaceId);
		html.append("		<table id=\"typelist\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" >").append("\n");
		html.append("			<tr> ").append("\n");
		html.append("				<td align=\"center\"   class=\"typeTitle\">领用品分类</td>").append("\n");
		html.append("			</tr>").append("\n");
		html.append("			<tr > ").append("\n");
		html.append("				<td height=\"15\" align=\"center\"  style=\"border-bottom:solid 1px #CCCCCC;border-left:solid 1px #CCCCCC;border-right:solid 1px #CCCCCC;\" >").append("\n");
		html.append("					<table width=\"85%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" >").append("\n");
		html.append("					<tr> ").append("\n");
		if(lbbh==null){
			html.append("						<td class=\"selectTab\" >全部</td>\n");
		}else{
			html.append("						<td class=\"unselectTab\" onclick=\"selectTab('").append("all").append("')\">").append("全部</a></td>\n");
		}
		 
		html.append("					</tr>").append("\n");
		for(int i=0;i<list.size();i++){
			IWorkbgypTypeModel model = list.get(i);
			html.append("					<tr> ").append("\n");
			if(lbbh!=null&&model.getLbbh().equals(lbbh)){
				html.append("						<td class=\"selectTab\"  >").append(model.getLbmc()).append("</td>\n"); 
			}else{
				html.append("						<td class=\"unselectTab\" onclick=\"selectTab('").append(model.getLbbh()).append("')\" >").append(model.getLbmc()).append("</td>\n"); 
			}
			html.append("					</tr>").append("\n");
		}
		html.append("			<tr> ").append("\n");
		html.append("				<td align=\"center\" height=12>&nbsp;</td>").append("\n");
		html.append("			</tr>						").append("\n");
		html.append("					</table>").append("\n");
		html.append("			  </td>").append("\n");
		html.append("			</tr>	").append("\n");
		
		html.append("		</table>").append("\n");
		return html.toString();
	}
	/**
	 * 构建预定单元
	 * @param model
	 * @return
	 */
	private String buildItem(IWorkbgypModel model){
		StringBuffer item = new StringBuffer();
		String pic = "";
		String tp = model.getTp();
		if(tp!=null){
			String[] list = tp.split(",");
			for(String id:list){
				if(id.indexOf(".")>0){
					pic = "iwork_img/bgyp/"+id;
				}else{
					FileUpload fu = FileUploadAPI.getInstance().getFileUpload(id);
					if(fu!=null){
						pic = fu.getFileUrl();
					}
				}
			}
		}
		
		item.append("	<table width=100% border=0 cellspacing=0 cellpadding=0 align=center>").append("\n");
		item.append("		<tr>").append("\n");
		item.append("			<td width=20%  align=middle> ").append("\n");
		item.append("				<img border=0 onload=fixPic(this,120) class=le_pic style='width:60px' src='").append(pic).append("' onerror=this.src=\"iwork_img/co.big.gif\" >").append("\n");
		item.append("			</td>").append("\n");
		item.append("			<td width=80%>").append("\n");
		item.append("			<table width=100% border=0 cellspacing=0 cellpadding=0 align=center>").append("\n");
		item.append("			<tr>").append("\n");
		item.append("				<td width=20% colspan=\"2\" class=\"objItem\" align=left>").append("\n");
		item.append("					物品名称：").append(model.getName()).append("(").append(model.getNo()).append(")").append("\n");
		item.append("				</td>").append("\n");
		item.append("			</tr>").append("\n");
		item.append("			<tr>").append("\n");
		item.append("				<td width=20% colspan=\"2\"  class=\"objItem\" align=left>").append("\n");
		item.append("				规格型号：").append(model.getGg()).append("</td>").append("\n");
		item.append("			</tr>").append("\n");
		item.append("			<tr>").append("\n");
		item.append("			<td width=20%   class=\"objItem\" align=left>参考单价：￥").append(model.getDj()).append("</td>").append("\n");
		item.append("			</tr>").append("\n");
		item.append("			<tr>").append("\n"); 
		item.append("			<td width=20%   class=\"objItem\" align=left>库 存 数：").append(model.getKcsl()).append(model.getJldw()).append("</td>").append("\n");
		item.append("			</tr>").append("\n");
		item.append("			<tr>").append("\n");
		item.append("			<td  class=\"objItem\" align=right>").append("\n");
		item.append("				[<img src=\"iwork_img/bullet_add_2.png\" style='width:7px;height:7px;border=0px;padding-right:5px;'><a href='' onclick=\"myShopCar(").append(model.getId()).append(");return false;\">添加到领用车</a>]").append("\n");
		item.append("			</td>").append("\n");
		item.append("		</tr>").append("\n"); 
		item.append("	</table>").append("\n");
		item.append("			</td>").append("\n");
		item.append("		</tr>").append("\n");
		item.append("	</table>").append("\n");
		return item.toString();
	}
	/**
	 * 添加到购物车
	 * @param id
	 * @return
	 */
	public boolean addShopCar(Long id){
		boolean flag = false;
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		IWorkbgypModel model = iworkbgypManageDao.getDataModel(id);
		
		if(model!=null){
			boolean ischeck = iworkbgypManageDao.isCheckShopCarNum(userid, model.getNo());
			if(ischeck){
				flag = iworkbgypManageDao.updateShopCarNum(userid, model.getNo());
			}else{
				ShopCarModel scm = new ShopCarModel();
				scm.setNo(model.getNo());
				scm.setName(model.getName());
				scm.setNum(new Long(0));
				scm.setUserid(userid);
				flag = iworkbgypManageDao.addShopCar(scm);
			}
			
		}
		return flag;
	}
	/**
	 * 根据当前用户查找对应的办公用品领用记录
	 * @return
	 */
	public List<HashMap> selectTabs(){
		List<HashMap> list = new ArrayList<HashMap>();
		list = iworkbgypManageDao.selectTabs();
		return list;
	}
	public String selectTabByDJBH(String djbh){
		StringBuffer html = new StringBuffer();
		if(djbh!=null){
			List<HashMap> list = iworkbgypManageDao.selectTabByDJBH(djbh);
			List<HashMap> list1=new ArrayList<HashMap>();
			for(HashMap map:list){
				list1.add(map);
			}
			List<IWorkbgypModel> list2 = iworkbgypManageDao.selectTabByLBBH(list1);
			StringBuffer html1 = new StringBuffer();
			html.append("<table width='98%' style=\"border:1px solid #CCCCCC;\" cellspacing=0 cellpadding=0 align=center>").append("\n");
			
			for(int i=0;i<list2.size();i++){
				IWorkbgypModel model = list2.get(i);
				html.append("	<tr>").append("\n");
				if(model!=null){
					html.append("	<td style=\"border-bottom:1px dotted #CCCCCC;padding:3px;\" width=50%>").append("\n");
					html.append(this.selectTabByDJBHs(model,djbh));
					html.append("	</td>").append("\n");
				}else{
					html.append("	<td>&nbsp</td>").append("\n");
				}
				i++;
				if(i<list.size()){
					model = list2.get(i);
					html.append("	<td style=\"border-bottom:1px dotted #CCCCCC;padding:3px;\" width=50%>").append("\n");
					html.append(this.selectTabByDJBHs(model,djbh));
					html.append("	</td>").append("\n");
				}else{
					html.append("	<td>&nbsp</td>").append("\n");
				}
				html.append("	</tr>").append("\n");
			}
			html.append("	</table>").append("\n");
			
		}
		return html.toString();
		
	}
	private String selectTabByDJBHs(IWorkbgypModel model,String djbh){
		StringBuffer item = new StringBuffer();
		item.append("	<table width=100% border=0 cellspacing=0 cellpadding=0 align=center>").append("\n");
		item.append("		<tr>").append("\n");
		item.append("			<td width=20%  align=middle> ").append("\n");
		item.append("				<img border=0 onload=fixPic(this,120) class=le_pic   src='iwork_img/bgyp/").append(model.getTp()).append("' onerror=this.src=\"iwork_img/co.big.gif\" >").append("\n");
		item.append("			</td>").append("\n");
		item.append("			<td width=80%>").append("\n");
		item.append("			<table width=100% border=0 cellspacing=0 cellpadding=0 align=center>").append("\n");
		item.append("			<tr>").append("\n");
		item.append("				<td width=20% colspan=\"2\" class=\"objItem\" align=left>").append("\n");
		item.append("					物品名称：").append(model.getName()).append("(").append(model.getNo()).append(")").append("\n");
		item.append("				</td>").append("\n");
		item.append("			</tr>").append("\n");
		item.append("			<tr>").append("\n");
		item.append("				<td width=20% colspan=\"2\"  class=\"objItem\" align=left>").append("\n");
		item.append("				规格型号：").append(model.getGg()).append("</td>").append("\n");
		item.append("			</tr>").append("\n");
		item.append("			<tr>").append("\n");
		item.append("			<td width=20%   class=\"objItem\" align=left>参考单价：￥").append(model.getDj()).append("</td>").append("\n");
		item.append("			</tr>").append("\n");
		item.append("			<tr>").append("\n");
		HashMap map= iworkbgypManageDao.getSQSL(djbh,model.getNo());
		item.append("			<td width=20%   class=\"objItem\" align=left>领 取 数：").append(map.get("SQSL")).append(map.get("DW")).append("</td>").append("\n");
		item.append("			</tr>").append("\n");
		item.append("			<tr>").append("\n");
		item.append("		</tr>").append("\n"); 
		item.append("	</table>").append("\n");
		item.append("			</td>").append("\n");
		item.append("		</tr>").append("\n");
		item.append("	</table>").append("\n");
		return item.toString();
	}
	
	
	public IWorkbgypManageDao getIworkbgypManageDao() {
		return iworkbgypManageDao;
	}

	public void setIworkbgypManageDao(IWorkbgypManageDao iworkbgypManageDao) {
		this.iworkbgypManageDao = iworkbgypManageDao;
	}
	
	
	
}
