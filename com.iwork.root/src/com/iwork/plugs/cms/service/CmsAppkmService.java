package com.iwork.plugs.cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.plugs.cms.dao.CmsAppkmDAO;
import com.iwork.plugs.cms.model.IworkCmsAppkm;

public class CmsAppkmService {

	private CmsAppkmDAO cmsAppkmDAO;

	public String getList() {
		StringBuffer sb = new StringBuffer();
		List<Map<Object, Object>> item = new ArrayList<Map<Object, Object>>();
		List<IworkCmsAppkm> list = cmsAppkmDAO.getList();
		for (int i = 0; i < list.size(); i++) {
			IworkCmsAppkm model = list.get(i);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", model.getId());
			map.put("title", model.getTitle());
			map.put("url", model.getUrl());
			map.put("sequence", model.getSequence());
			item.add(map);
		}
		JSONArray json = JSONArray.fromObject(item);
		sb.append("{\"total\":200,\"rows\":" + json.toString() + "}");
		return sb.toString();
	}

	public String edit(String id, String title, String url, String sequence, String type) {
		IworkCmsAppkm model = new IworkCmsAppkm();
		if (id != null && !"".equals(id.trim())) {
			model.setId(Long.parseLong(id));
		}
		if (sequence != null && !"".equals(sequence.trim())) {
			model.setSequence(Long.parseLong(sequence));
		}
		model.setTitle(title);
		model.setUrl(url);
		if ("add".equals(type.trim())) {
			long idSequence = cmsAppkmDAO.getIdSequcence();
			long index = cmsAppkmDAO.getSequence();
			model.setId(idSequence);
			model.setSequence(index);
			cmsAppkmDAO.addBoData(model);
		} else if ("edit".equals(type.trim())) {
			cmsAppkmDAO.updateBoData(model);
		} else if ("delete".equals(type.trim())) {
			cmsAppkmDAO.deleteBoData(model);
		}
		return type;
	}

	/*------------------GET/SET--------------------*/

	public CmsAppkmDAO getCmsAppkmDAO() {
		return cmsAppkmDAO;
	}

	public void setCmsAppkmDAO(CmsAppkmDAO cmsAppkmDAO) {
		this.cmsAppkmDAO = cmsAppkmDAO;
	}

}
