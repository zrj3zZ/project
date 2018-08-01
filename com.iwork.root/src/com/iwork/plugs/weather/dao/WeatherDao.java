package com.iwork.plugs.weather.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.plugs.weather.model.WeatherModel;

public class WeatherDao extends HibernateDaoSupport {

	/**
	 * 更新
	 * 
	 * @param model
	 */
	public void updateWeatherModel(WeatherModel model) {
		this.getHibernateTemplate().update(model);
	}

	/**
	 * 保存
	 * 
	 * @param model
	 */
	public void saveWeatherModel(WeatherModel model) {
		this.getHibernateTemplate().save(model);
	}
	
	/**
	 * 根据城市名称查询最新的一条数据 
	 * @param cityName
	 * @return
	 */
	public WeatherModel getWeatherModelByCityName(String cityName){
		
		String sql="FROM WeatherModel where city = ? order by id desc";
		Object[] values={cityName};
		List<WeatherModel> list = this.getHibernateTemplate().find(sql,values);
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
}
