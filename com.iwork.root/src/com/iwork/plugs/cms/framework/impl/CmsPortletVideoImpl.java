package com.iwork.plugs.cms.framework.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.cms.dao.CmsVideoDAO;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.model.IworkCmsVideo;
import org.apache.log4j.Logger;
public class CmsPortletVideoImpl implements CmsPortletInterface {

	private CmsVideoDAO cmsVideoDAO;
	private static Logger logger = Logger.getLogger(CmsPortletVideoImpl.class);
	public String portletPage(UserContext me, DomainModel domainModel, IworkCmsPortlet infoModel, HashMap params) {
		String name = "default.flv";
		StringBuffer sb = new StringBuffer();
		Date timeNow = null;
		if (cmsVideoDAO == null) {
			cmsVideoDAO = (CmsVideoDAO) SpringBeanUtil.getBean("cmsVideoDAO");
		}
		List<IworkCmsVideo> list = cmsVideoDAO.getVideo(name);
		if (list == null || list.size() == 0) {
			sb.append("未发现视频");
		} else {
			try {
				for (int i = 0; i < list.size(); i++) {
					IworkCmsVideo model = list.get(i);
					String title = model.getTitle();
					String picFile = model.getPicfile();
					Date uploadTime = model.getUploadtime();
					if (title == null || "".equals(title)) {
						title = "空标题";
					}
					String video_path = "../iwork_video/" + name;
					String pic_path = "../iwork_video/" + picFile;

					Date date = new Date();
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String now = df.format(date);
					try {
						timeNow = df.parse(now);
					} catch (Exception e) {
						logger.error(e,e);
					}
					sb.append("<div>").append("\n");
					sb.append("	<div id='container'><a href='http://www.macromedia.com/go/getflashplayer'>Get the Flash Player</a></div>").append("\n");
					sb.append("	<script type='text/javascript' src='../iwork_js/plugs/video/swfobject.js'></script>").append("\n");
					sb.append("	<script type='text/javascript'>").append("\n");
					sb.append("		var s1 = new SWFObject('../iwork_video/player.swf','ply','100%','189','9','#FFFFFF');").append("\n");
					sb.append("		s1.addParam('allowfullscreen','true');").append("\n");
					sb.append("		s1.addParam('wmode','opaque');").append("\n");
					sb.append("		s1.addParam('allowscriptaccess','always');").append("\n");
					sb.append("		s1.addParam('flashvars','file=").append(video_path).append("&image=" + pic_path + "');").append("\n");
					sb.append("		s1.write('container');").append("\n");
					sb.append("	</script>").append("\n");
					sb.append("</div>").append("\n");
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
		return sb.toString();
	}

	public String portletWeiXinPage(UserContext me, DomainModel domainModel,IworkCmsPortlet infoModel, HashMap params) {
		
		return "";
	}
}
