package com.iwork.plugs.weather.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.weather.bean.CmsWeatherModel;
import com.iwork.plugs.weather.dao.WeatherDao;
import com.iwork.plugs.weather.model.WeatherModel;
import org.apache.log4j.Logger;
public class WeatherUtil {
	private static Logger logger = Logger.getLogger(WeatherUtil.class);
	private static WeatherUtil instance = null;
	public static final String[] CITY_NAMES = { "北京", "珠海", "成都" };
	InputStream inStream;
	Element root;

	private WeatherUtil() {
	}

	public static WeatherUtil getInstance() {

		if (instance == null) {
			instance = new WeatherUtil();
		}

		return instance;
	}

	public WeatherUtil(InputStream inStream) {
		if (inStream != null) {
			this.inStream = inStream;
			DocumentBuilderFactory domfac = DocumentBuilderFactory
					.newInstance();
			try {
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();
				Document doc = domBuilder.parse(inStream);
				root = doc.getDocumentElement();
			} catch (ParserConfigurationException e) {
				logger.error(e,e);
			} catch (SAXException e) {
				logger.error(e,e);
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
	}

	private WeatherUtil(URL url) {
		InputStream inStream = null;
		try {
			inStream = url.openStream();
		} catch (Exception e) {
			logger.error(e,e);
		}
		if (inStream != null) {
			this.inStream = inStream;
			DocumentBuilderFactory domfac = DocumentBuilderFactory
					.newInstance();
			try {
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();
				Document doc = domBuilder.parse(inStream);
				root = doc.getDocumentElement();
			} catch (ParserConfigurationException e) {
				logger.error(e,e);
			} catch (SAXException e) {
				logger.error(e,e);
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
	}

	/**
	 * 
	 * @param nodes
	 * @return 单个节点多个值以分号分隔
	 */
	private Map<String, String> getValue(String[] nodes) {
		if (inStream == null || root == null) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		// 初始化每个节点的值为null
		for (int i = 0; i < nodes.length; i++) {
			map.put(nodes[i], null);
		}

		// 遍历第一节点
		NodeList topNodes = root.getChildNodes();
		if (topNodes != null) {
			for (int i = 0; i < topNodes.getLength(); i++) {
				Node book = topNodes.item(i);
				if (book.getNodeType() == Node.ELEMENT_NODE) {
					for (int j = 0; j < nodes.length; j++) {
						for (Node node = book.getFirstChild(); node != null; node = node
								.getNextSibling()) {
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								if (node.getNodeName().equals(nodes[j])) {
									String val = node.getNodeValue();
									// 如果原来已经有值则以分号分隔
									String temp = map.get(nodes[j]);
									if (temp != null && !temp.equals("")) {
										temp = temp + ";" + val;
									} else {
										temp = val;
									}
									map.put(nodes[j], temp);
								}
							}
						}
					}
				}
			}
		}
		return map;
	}

	public WeatherModel getWeatherModelByCityName(String CityName) {
		WeatherModel model = new WeatherModel();
		try {
			String link = "http://php.weather.sina.com.cn/xml.php?city="
					+ URLEncoder.encode(CityName, "GBK")
					+ "&password=DJOYnieT8234jlsK&day=0";
			URL url = new URL(link);
			WeatherUtil parser = new WeatherUtil(url);

			String[] nodes = { "status1", "temperature1", "temperature2" };
			Map<String, String> map = parser.getValue(nodes);

			model.setCity(CityName);
			model.setDay("今天");
			model.setCreatetime(new Date());
			model.setWeather(map.get(nodes[0]));
			model.setHighest(Long.parseLong(map.get(nodes[1])));
			model.setMinimum(Long.parseLong(map.get(nodes[2])));

		} catch (Exception e) {
			logger.error(e,e);
		}

		return model;
	}

	public CmsWeatherModel getCmsWeatherModelByCityName(String CityName) {
		CmsWeatherModel model = new CmsWeatherModel();
		try {
			String link = "http://php.weather.sina.com.cn/xml.php?city="
					+ URLEncoder.encode(CityName, "GBK")
					+ "&password=DJOYnieT8234jlsK&day=0";
			URL url = new URL(link);
			WeatherUtil parser = new WeatherUtil(url);

			String[] nodes = { "status1", "temperature1", "temperature2",
					"chy_shuoming", "zwx_s", "ssd_s", "xcz_s", "chy_l", "ktk_l" };
			Map<String, String> map = parser.getValue(nodes);

			model.setCity(CityName);
			if(map.get(nodes[0])!=null){
				model.setWeather(map.get(nodes[0]));
			}
			try{
				model.setHighest(Long.parseLong(map.get(nodes[1])));
				model.setMinimum(Long.parseLong(map.get(nodes[2])));
			}catch(Exception e){logger.error(e,e);}
			
			model.setChy_shuoming(map.get(nodes[3]));
			model.setZwx_s(map.get(nodes[4]));
			model.setSsd_s(map.get(nodes[5]));
			model.setXcz_s(map.get(nodes[6]));
			model.setChy_l(map.get(nodes[7]));
			model.setKtk_l(map.get(nodes[8]));

		} catch (Exception e) {
			logger.error(e,e);
		}

		return model;
	}

	public void updateWeatherInfo() {

		WeatherDao weatherDao = (WeatherDao) SpringBeanUtil
				.getBean("weatherDao");
		for (int i = 0; i < CITY_NAMES.length; i++) {
			WeatherModel model = getWeatherModelByCityName(CITY_NAMES[i]);
			weatherDao.saveWeatherModel(model);
		}
	}

	public static void main(String[] args) {
		String[] cityNames = { "北京", "珠海", "成都" };
		for (int i = 0; i < cityNames.length; i++) {
			WeatherModel model = WeatherUtil.getInstance()
					.getWeatherModelByCityName(cityNames[i]);
		}
	}

}
