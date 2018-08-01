package com.iwork.plugs.weather.service;

import com.iwork.plugs.weather.dao.WeatherDao;
import com.iwork.plugs.weather.model.WeatherModel;

public class WeatherService {

	WeatherDao weatherDao;
	
	public WeatherModel getModelByCityName(String cityName) {

		return weatherDao.getWeatherModelByCityName(cityName);
	}

}
