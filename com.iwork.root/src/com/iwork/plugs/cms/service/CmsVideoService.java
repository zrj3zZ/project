package com.iwork.plugs.cms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.cms.dao.CmsVideoDAO;
import com.iwork.plugs.cms.model.IworkCmsVideo;

public class CmsVideoService {

    private CmsVideoDAO cmsVideoDAO;
    private static int pageSize = 2;

    /**
     * 视频播放
     *
     * @param model
     * @return
     */
    public String getVideoPlay(IworkCmsVideo model) {
        StringBuffer sb = new StringBuffer();
        String title;
        String videoFile;
        String picFile;

        if (model == null) {
            title = "";
            videoFile = "default.flv";
            picFile = "default.jpg";
        } else {
            title = model.getTitle();
            videoFile = model.getVideofile();
            picFile = model.getPicfile();
        }

        String video_path = "../iwork_video/" + videoFile;
        String pic_path = "../iwork_video/" + picFile;
        // sb.append("<div class='rgt_cnt videodiv'>").append("\n");
        sb.append("<div align=\"center\">").append("\n");
        sb.append("	<div id='container'><a href='http://www.macromedia.com/go/getflashplayer'>Get the Flash Player</a></div>").append("\n");
        sb.append("	<script type='text/javascript' src='../iwork_js/plugs/video/swfobject.js'></script>").append("\n");
        sb.append("	<script type='text/javascript'>").append("\n");
        sb.append("		var s1 = new SWFObject('../iwork_video/player.swf','ply','295','234','10','#FFFFFF');").append("\n");
        sb.append("		s1.addParam('allowfullscreen','true');").append("\n");
        sb.append("		s1.addParam('wmode','opaque');").append("\n");
        sb.append("		s1.addParam('allowscriptaccess','always');").append("\n");
        sb.append("		s1.addParam('flashvars','file=").append(video_path).append("&image=" + pic_path + "');").append("\n");
        sb.append("		s1.write('container');").append("\n");
        sb.append("	</script>").append("\n");
        sb.append("</div>").append("\n");

        sb.append("||");
        sb.append(title);
        return sb.toString();
    }

    /**
     * 视频列表--More页面
     *
     * @param pageNum
     * @return
     * @throws Exception
     */
    public String getVideoList(int pageNum) throws Exception {
        StringBuffer sb = new StringBuffer();
        int beginLine = (pageNum - 1) * pageSize;// 开始行数
        List<IworkCmsVideo> list = cmsVideoDAO.getVideoList(beginLine, pageSize);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                IworkCmsVideo model = list.get(i);
                Date date = model.getUploadtime();
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                String time = sd.format(date);
                sb.append("<tr >").append("\n");
                sb.append("<td align='center'>").append(i + 1).append("</td>").append("\n");
                sb.append("<td>");
                sb.append("<img src=\"../iwork_img/tvicon.png\" width=\"16\" height=\"15\" />");
                sb.append(model.getTitle()).append("</td>").append("\n");
                sb.append("<td align='center' nowrap>").append("<a href='videoPlay.action?iworkCmsVideo.title=").append(model.getTitle()).append("&iworkCmsVideo.videofile=").append(model.getVideofile()).append("&iworkCmsVideo.picfile=").append(model.getPicfile()).append("' target='video_play'>").append("播放&nbsp;").append("</a>&nbsp;&nbsp;").append("</td>").append("\n");
                sb.append("<td align='center'>" + model.getUploader() + "</td>").append("\n");
                sb.append("<td align='center'>" + time + "</td>").append("\n");
                sb.append("</tr>").append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 分页
     *
     * @param pageNum
     * @return
     */
    public String getPagination(int pageNum) {
        int sumColumn = cmsVideoDAO.getTotalNumberOfPages();
        int sumPage = (sumColumn % pageSize) == 0 ? (sumColumn / pageSize) : (sumColumn / pageSize + 1);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sumPage; i++) {
            sb.append("<a href='videoList.action?pageNum=").append(i + 1).append("'>").append(i == pageNum - 1 ? "<strong>" : "").append(i + 1).append(i == pageNum - 1 ? "</strong>" : "").append("</a>&nbsp;&nbsp;");
        }
        return sb.toString();
    }

    public String getList() {
        StringBuffer sb = new StringBuffer();
        String time = "";
        List<Map<String, Object>> item = new ArrayList<Map<String, Object>>();
        List<IworkCmsVideo> list = cmsVideoDAO.getList();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < list.size(); i++) {
            IworkCmsVideo model = (IworkCmsVideo) list.get(i);
            Map<String, Object> rows = new HashMap<String, Object>();
            Date date = model.getUploadtime();
            if (date != null) {
                time = sd.format(date);
            }
            rows.put("id", model.getId());
            rows.put("title", model.getTitle());
            rows.put("videofile", model.getVideofile());
            rows.put("picfile", model.getPicfile());
            rows.put("uploader", model.getUploader());
            rows.put("uploadtime", time);
            // rows.put("uploadtime", model.getUploadtime());
            rows.put("description", model.getDescription());
            item.add(rows);
        }
        JSONArray json = JSONArray.fromObject(item);
        sb.append("{\"total\":200,\"rows\":" + json.toString() + "}");
        return sb.toString();
    }

    public String videoEdit(String id, String title, String videofile, String picfile, String uploader, String uploadtime, String description, String type) throws Exception {
        IworkCmsVideo model = new IworkCmsVideo();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

        if (id != null && !"".equals(id.trim())) {
            model.setId(Long.parseLong(id));
        }
        model.setTitle(title);
        model.setVideofile(videofile);
        model.setPicfile(picfile);
        model.setDescription(description);
        model.setUploader(uploader);

        if (type.equals("edit")) {
            model.setUploadtime(sd.parse(uploadtime));
            cmsVideoDAO.updateBoData(model);
        } else if (type.equals("add")) {
            String uid = UserContextUtil.getInstance().getCurrentUserId();
            long idSequence = (long) cmsVideoDAO.getIdSequcence();
            model.setId(idSequence);
            model.setUploader(uid);
            model.setUploadtime(new Date());
            cmsVideoDAO.addBoData(model);
        } else if (type.equals("delete")) {
            cmsVideoDAO.deleteBoData(model);
        }
        return type;
    }

    // -------------------------------------------------
    public CmsVideoDAO getCmsVideoDAO() {
        return cmsVideoDAO;
    }

    public void setCmsVideoDAO(CmsVideoDAO cmsVideoDAO) {
        this.cmsVideoDAO = cmsVideoDAO;
    }

}
