package com.iwork.plugs.weather.model;

import java.util.Date;

/**
 * BdInfTqybxxjlb entity. @author MyEclipse Persistence Tools
 */

public class WeatherModel implements java.io.Serializable {

	// Fields

	private Long id;
	private String city;
	private Date createtime;
	private String weather;
	private Long highest;
	private Long minimum;
	private String day;

	// Constructors

	/** default constructor */
	public WeatherModel() {
	}

	/** minimal constructor */
	public WeatherModel(Long id) {
		this.id = id;
	}

	/** full constructor */
	public WeatherModel(Long id, String city, Date createtime,
			String weather, Long highest, Long minimum, String day) {
		this.id = id;
		this.city = city;
		this.createtime = createtime;
		this.weather = weather;
		this.highest = highest;
		this.minimum = minimum;
		this.day = day;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getWeather() {
		return this.weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public Long getHighest() {
		return this.highest;
	}

	public void setHighest(Long highest) {
		this.highest = highest;
	}

	public Long getMinimum() {
		return this.minimum;
	}

	public void setMinimum(Long minimum) {
		this.minimum = minimum;
	}

	public String getDay() {
		return this.day;
	}

	public void setDay(String day) {
		this.day = day;
	}

}