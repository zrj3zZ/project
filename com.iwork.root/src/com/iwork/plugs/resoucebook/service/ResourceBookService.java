package com.iwork.plugs.resoucebook.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.UtilDate;
import com.iwork.plugs.resoucebook.dao.SpaceManageDao;
import com.iwork.plugs.resoucebook.model.DateModel;
import com.iwork.plugs.resoucebook.model.IworkRmBase;
import com.iwork.plugs.resoucebook.model.IworkRmSpace;
import com.iwork.plugs.resoucebook.model.IworkRmWeb;

public class ResourceBookService {
	private SpaceManageDao spaceManageDao;
	
	/**
	 * 空间ID
	 * @param spaceId
	 * @return
	 */
	public String spaceIndex(IworkRmSpace model,Date startdate){
		//获取资源预定 空间模型
		if(model==null)return "";
		
		List<IworkRmBase> list=spaceManageDao.getSpaceList(model.getId());
		if(model.getCycle()==null){
			model.setCycle(new Long(7));
		}
		int LENGHT = model.getCycle().intValue();
		int CYCLE= model.getCycle().intValue()/7;
		String cycleshow= this.getCycleStr(LENGHT);
		StringBuffer carList =new StringBuffer();
		carList.append("");
		if (list != null && list.size() > 0) {
			for (IworkRmBase irb:list) {
				//非空判断=======
				if(irb.getSpacename()==null)irb.setSpacename("");
				if(irb.getSpaceid()==null)irb.setSpaceid(new Long(0));
				if(irb.getResouceid()==null)irb.setSpacename("");
				if(irb.getResoucename()==null)irb.setSpacename("");
				if(irb.getPicture()==null)irb.setSpacename("");
				if(irb.getParameter1()==null)irb.setParameter1("");
				if(irb.getParameter2()==null)irb.setParameter2("");
				if(irb.getParameter3()==null)irb.setParameter3("");
				if(irb.getParameter4()==null)irb.setParameter4("");
				if(irb.getParameter5()==null)irb.setParameter5("");
				
				String imgURL=irb.getPicture();
				
				StringBuffer AddBtn = new StringBuffer(); 
				if(model.getType().equals(new Long(1))){ //流程预定
					AddBtn.append("<a href='#' onclick=\"createProcess(").append(model.getId()).append(",'").append(model.getProcessId()).append("','").append(irb.getResouceid()).append("','").append(irb.getResoucename()).append("','").append(irb.getParameter1()).append("','").append(irb.getParameter2()).append("','").append(irb.getParameter3()).append("')\" class='font'><img src='app/plugs/resoucebook/images/add_obj.gif' alt=''  border=0 />【申请】</a>");
				}else{
					AddBtn.append("<a href='#' onclick=\"addApply(").append(model.getId()).append(",'").append(irb.getResouceid()).append("','").append(irb.getResoucename()).append("','").append(irb.getParameter1()).append("','").append(irb.getParameter2()).append("','").append(irb.getParameter3()).append("')\" class='font'><img src='app/plugs/resoucebook/images/add_obj.gif' alt=''  border=0 />【申请】</a>");
				}
				carList.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' style='margin-bottom:10px'>");
				carList.append("<tr>");
				carList.append("<td width='20%' valign='top'>").append("<img class='pic' src='").append(imgURL).append("'/>").append("</td>");
				carList.append("<td width='15%' valign='top' class='border_right'><table width='100%' height='115' border='0' cellpadding='0' cellspacing='0'>");
				carList.append("<tr>");
				//carList.append("<td width='50%' class='font' nowrap>资源编号：</td>");
				carList.append("<td width='50%'  class='font'>").append("资源编号：").append(irb.getResouceid()).append("</td>");
				carList.append("</tr>");
				carList.append("<tr class='font'>");
			//	carList.append("<td  class='font'>车牌号码：</td>");
				carList.append("<td>").append("资源名称：").append(irb.getResoucename()).append("</td>");
				carList.append("</tr>");
				carList.append("<tr class='font'>");
				carList.append("<td  class='font' colspan='2'> ").append(irb.getParameter1()).append("</td>");
				carList.append("</tr>");
				carList.append("<tr class='font'>");
				//carList.append("<td>资源类型：</td>");
				
				//carList.append("<td>车身颜色：</td>");
				carList.append("<td colspan='2'>").append(irb.getParameter2()).append("</td>");
				carList.append("</tr>");
				carList.append("<tr class='font'>");
				//carList.append("<td>准乘人数：</td>");
				carList.append("<td colspan='2'>").append(irb.getParameter3()).append("</td>");
				carList.append("</tr>");
//				carList.append("<tr class='font'>");
//				carList.append("<td>限行日期：</td>");
//				carList.append("<td><font color='red'>").append(ht.get("PARA4").toString()).append("</font></td>");
//				carList.append("</tr>");
				carList.append("</table></td>");
				carList.append("<td width='58%' align='right' valign='top'><table width='98%' border='1' cellpadding='2' cellspacing='0' bordercolor='#CCD7DF' style='border-collapse:collapse'>");
				carList.append("<tr>");
				carList.append("<td colspan='8' align='left' bgcolor='#F5F8FA' class='font'><strong>最近"+cycleshow+irb.getSpacename()+"安排：</strong></td>");
				carList.append("</tr>");
				carList.append("<tr>");
				List listdate = getAfterWeekList(LENGHT,startdate);
				
				//=======循环状态=======
			if(CYCLE==1){
					for(int count=0;count<LENGHT;count++){
						DateModel day = (DateModel)listdate.get(count);				
						carList.append("<td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
					}
					carList.append("</tr>");
					carList.append("<tr>");
					
					for(int count=0;count<LENGHT;count++){
						DateModel day = (DateModel)listdate.get(count);
									
						List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
						if(statuslist.size()>0){
							carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

						}else{
							carList.append("<td align='center'>&nbsp;</td>");
						}
						
					}
					carList.append("</tr>");			
					carList.append("<tr>");
			}
			if(CYCLE==2){
				for(int count=0;count<LENGHT/2;count++){
					DateModel day = (DateModel)listdate.get(count);				
					carList.append("<td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
				}
				carList.append("</tr>");
				carList.append("<tr>");
				//判断预定信息是否在日期区间
				for(int count=0;count<LENGHT/2;count++){
					DateModel day = (DateModel)listdate.get(count);
					List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
					if(statuslist.size()>0){
						//carList.append("<td align='center'><a href='#'  onMouseOver=\"popupShow('" + this.getPopupInfo(statuslist) + "')\" onMouseOut=\"popupKill();\">").append("<img src = app/plugs/resoucebook/images/car_icon.gif border=0>").append("</a></td>");
						carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

					}else{
						carList.append("<td align='center'>&nbsp;</td>");
					}
				}
				carList.append("                          </tr>");			
				carList.append("                          <tr>");
				for(int count=LENGHT/2;count<LENGHT;count++){
					DateModel day = (DateModel)listdate.get(count);
					
					carList.append("                            <td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
				}
				carList.append("                          </tr>");						
				carList.append("                          <tr>");
				for(int begin =LENGHT/2;begin<LENGHT;begin++){
					DateModel day = (DateModel)listdate.get(begin);
					List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
					if(statuslist.size()>0){
						//carList.append("<td align='center'><a href=''  onMouseOver=\"popupShow('" + this.getPopupInfo(statuslist) + "')\" onMouseOut=\"popupKill();\">").append("<img src = app/plugs/resoucebook/images/car_icon.gif border=0>").append("</a></td>");
						carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

					}else{
						carList.append("                            <td align='center'>&nbsp;</td>");
					}
					
				 }
			}
			if(CYCLE==3){
				for(int count=0;count<LENGHT/3;count++){
					DateModel day = (DateModel)listdate.get(count);				
					carList.append("<td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
				}
				carList.append("</tr>");
				carList.append("<tr>");
				
				for(int count=0;count<LENGHT/3;count++){
					DateModel day = (DateModel)listdate.get(count);
								
					List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
					if(statuslist.size()>0){
						//carList.append("<td align='center'><a href='#'  onMouseOver=\"popupShow('" + this.getPopupInfo(statuslist) + "')\" onMouseOut=\"popupKill();\">").append("<img src = app/plugs/resoucebook/images/car_icon.gif border=0>").append("</a></td>");
						carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

					}else{
						carList.append("<td align='center'>&nbsp;</td>");
					}
					
				}
				carList.append("                          </tr>");			
				carList.append("                          <tr>");
				for(int count=LENGHT/3;count<2*LENGHT/3;count++){
					DateModel day = (DateModel)listdate.get(count);
					
					carList.append("                            <td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
				}
				carList.append("                          </tr>");						
				carList.append("                          <tr>");
				for(int begin =LENGHT/3;begin<2*LENGHT/3;begin++){
					DateModel day = (DateModel)listdate.get(begin);
					List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
					if(statuslist.size()>0){
						//carList.append("                            <td align='center'><a href=''  onMouseOver=\"popupShow('" + this.getPopupInfo(statuslist) + "')\" onMouseOut=\"popupKill();\">").append("<img src = app/plugs/resoucebook/images/car_icon.gif border=0>").append("</a></td>");
						carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

					}else{
						carList.append("                            <td align='center'>&nbsp;</td>");
					}
					
				 }
				carList.append("                          </tr>");						
				carList.append("                          <tr>");
				for(int count=2*LENGHT/3;count<LENGHT;count++){
					DateModel day = (DateModel)listdate.get(count);
					
					carList.append("                            <td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
				}
				carList.append("                          </tr>");						
				carList.append("                          <tr>");
				for(int begin =2*LENGHT/3;begin<LENGHT;begin++){
					DateModel day = (DateModel)listdate.get(begin);
					List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
					if(statuslist.size()>0){
						//carList.append("<td align='center'><a href=''  onMouseOver=\"popupShow('" + this.getPopupInfo(statuslist) + "')\" onMouseOut=\"popupKill();\">").append("<img src = app/plugs/resoucebook/images/car_icon.gif border=0>").append("</a></td>");
						carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

					}else{
						carList.append("                            <td align='center'>&nbsp;</td>");
					}
					
				 }
			}
			if(CYCLE==4){
				for(int count=0;count<LENGHT/4;count++){
					DateModel day = (DateModel)listdate.get(count);				
					carList.append("<td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
				}
				carList.append("</tr>");
				carList.append("<tr>");
				
				for(int count=0;count<LENGHT/4;count++){
					DateModel day = (DateModel)listdate.get(count);
								
					List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
					if(statuslist.size()>0){
						//carList.append("<td align='center'><a href='#'  onMouseOver=\"popupShow('" + this.getPopupInfo(statuslist) + "')\" onMouseOut=\"popupKill();\">").append("<img src = app/plugs/resoucebook/images/car_icon.gif border=0>").append("</a></td>");
						carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

					}else{
						carList.append("<td align='center'>&nbsp;</td>");
					}
					
				}
				carList.append("                          </tr>");			
				carList.append("                          <tr>");
				for(int count=LENGHT/4;count<2*LENGHT/4;count++){
					DateModel day = (DateModel)listdate.get(count);
					
					carList.append("<td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
				}
				carList.append("                          </tr>");						
				carList.append("                          <tr>");
				for(int begin =LENGHT/4;begin<2*LENGHT/4;begin++){
					DateModel day = (DateModel)listdate.get(begin);
					List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
					if(statuslist.size()>0){
					//carList.append("<td align='center'><a href=''  onMouseOver=\"popupShow('" + this.getPopupInfo(statuslist) + "')\" onMouseOut=\"popupKill();\">").append("<img src = app/plugs/resoucebook/images/car_icon.gif border=0>").append("</a></td>");
						carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

					}else{
						carList.append("                            <td align='center'>&nbsp;</td>");
					}
					
				 }
				carList.append("                          </tr>");						
				carList.append("                          <tr>");
				for(int count=2*LENGHT/4;count<3*LENGHT/4;count++){
					DateModel day = (DateModel)listdate.get(count);
					
					carList.append("                            <td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
				}
				carList.append("                          </tr>");						
				carList.append("                          <tr>");
				for(int begin =2*LENGHT/4;begin<3*LENGHT/4;begin++){
					DateModel day = (DateModel)listdate.get(begin);
					List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
					if(statuslist.size()>0){
						//carList.append("                            <td align='center'><a href=''  onMouseOver=\"popupShow('" + this.getPopupInfo(statuslist) + "')\" onMouseOut=\"popupKill();\">").append("<img src = app/plugs/resoucebook/images/car_icon.gif border=0>").append("</a></td>");
						carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

					}else{
						carList.append("                            <td align='center'>&nbsp;</td>");
					}
					
				 }
				carList.append("                          </tr>");						
				carList.append("                          <tr>");
				for(int count=3*LENGHT/4;count<LENGHT;count++){
					DateModel day = (DateModel)listdate.get(count);
					
					carList.append("                            <td align='center' bgcolor=\"#F5F9D7\" class='font'>").append(day.getWeek()).append("<br><font color=#000000>").append(day.getDate().substring(5)).append("</font></td>");
				}
				carList.append("                          </tr>");						
				carList.append("                          <tr>");
				for(int begin =3*LENGHT/4;begin<LENGHT;begin++){
					DateModel day = (DateModel)listdate.get(begin);
					List statuslist= spaceManageDao.isReserveStatus(model.getId(),irb.getResouceid(),day.getDate());
					if(statuslist.size()>0){
						//carList.append("<td align='center'><a href=''  onMouseOver=\"popupShow('" + this.getPopupInfo(statuslist) + "')\" onMouseOut=\"popupKill();\">").append("<img src = app/plugs/resoucebook/images/car_icon.gif border=0>").append("</a></td>");
						carList.append("<td align='center'>").append("<img src = app/plugs/resoucebook/images/resouce.jpg border=0 title='"+this.getPopupInfo(statuslist)+"'>").append("</td>");

					}else{
						carList.append("                            <td align='center'>&nbsp;</td>");
					}
					
				 }
				
			}
				carList.append("                          </tr>");
				carList.append("                      </table></td>");
				carList.append("                    </tr>");
				carList.append("                    <tr>");
				carList.append("                      <td height='33' colspan='3' align='left' class='border_bottom'><div align='right'>").append(AddBtn.toString()).append(" </div></td>");
				carList.append("                    </tr>");
				carList.append("                  </table>");
				 
			}
		} 
		return carList.toString();
	}
	
	/**
	 * @preserve 声明此方法不被JOC混淆
	 * @param model
	 * @return
	 */
	public String getPopupInfo(List list) {
		if (list != null) {
			
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<list.size();i++){
					HashMap model = (HashMap)list.get(i);
					String begin=UtilDate.dateFormat(UtilDate.StringToDate(model.get("BEGINTIME").toString(), "yyyy-MM-dd"));
					String end=UtilDate.dateFormat(UtilDate.StringToDate(model.get("ENDTIME").toString(), "yyyy-MM-dd"));
					sb.append("·").append(model.get("USERID")).append("/").append(model.get("USERNAME")).append("\n").append("预定时间&nbsp;").append(begin).append("到").append(end).append("");
					sb.append("\n");
				}
				return sb.toString();
			}
		return "";
	}
	/**
	 * 获得当前日期开始之后的7天
	 * @return
	 */
	private List getAfterWeekList(int LENGHT,Date startdate){
		List list = new ArrayList();
		Calendar c = Calendar.getInstance();
		if(startdate!=null){
			c.setTime(startdate);
		}
		
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] oneWeekDay={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		for(int i=0;i<LENGHT;i++){
			DateModel  dm = new DateModel();
			dm.setDate(sdf.format(c.getTime()));
			int week = c.get(Calendar.DAY_OF_WEEK)-1;
			dm.setWeek(oneWeekDay[week]);
			list.add(dm);
			c.add(Calendar.DATE, 1);
			
		}
		return list;
	}
	/**
	 * 获得下一循环周期
	 * @param LENGHT
	 * @param startdate
	 * @return
	 */
	public Date getNextDate(int LENGHT,Date startdate){
		ArrayList list = new ArrayList();
		Calendar c = Calendar.getInstance();
		if(startdate!=null){
			c.setTime(startdate);
		}
		
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<LENGHT;i++){
			DateModel  dm = new DateModel();
			dm.setDate(sdf.format(c.getTime()));
			int week = c.get(Calendar.DAY_OF_WEEK)-1;
			c.add(Calendar.DATE, 1);
			
		}
		return c.getTime();
	}
	/**
	 * 执行预定添加
	 * @param model
	 * @return
	 */
	
	public String executeAddRMWeb(IworkRmWeb model){
		//判断此时间端是否占用
		String msg = "";
		boolean isadd = false;
		
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
		String begintime = sdf.format(model.getBegintime());
		String endtime = sdf.format(model.getEndtime());
		List list  = spaceManageDao.isReserveStatus(model.getSpaceid(), model.getResouceid(),begintime);
		if(list!=null&&list.size()>0){
			isadd = true;
		}else{
			 list  = spaceManageDao.isReserveStatus(model.getSpaceid(), model.getResouceid(),endtime);
			 if(list!=null&&list.size()>0){
					isadd = true;
			}else{
				isadd = false;
			}
		}
		
		if(isadd){
			msg = "isadd";
		}else{
			spaceManageDao.addWeb(model);
			msg = "success";
		}
		return msg;
	}
	
	/**
	 * 获得前一循环周期
	 * @param LENGHT
	 * @param startdate
	 * @return
	 */
	public Date getPreviousDate(int LENGHT,Date startdate){
		ArrayList list = new ArrayList();
		Calendar c = Calendar.getInstance();
		if(startdate!=null){
			c.setTime(startdate);
		}
		
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<LENGHT;i++){
			DateModel  dm = new DateModel();
			dm.setDate(sdf.format(c.getTime()));
			int week = c.get(Calendar.DAY_OF_WEEK)-1;
			c.add(Calendar.DATE, -1);
		}
		return c.getTime();
	}
	
	/**
	 * 判断周期长度字符串
	 * @param length
	 * @return
	 */
	public String getCycleStr(int length){
		int CYCLE= length/7;
		String cycleshow="";
		switch(CYCLE){
		case 1:
			cycleshow="一周";
			break;
		case 2:
			cycleshow="两周";
			break;
		case 3:
			cycleshow="三周";
			break;
		case 4:
			cycleshow="四周";
			break;
		}
		return cycleshow;
	}
	
	public SpaceManageDao getSpaceManageDao() {
		return spaceManageDao;
	}

	public void setSpaceManageDao(SpaceManageDao spaceManageDao) {
		this.spaceManageDao = spaceManageDao;
	}
	
	
}
