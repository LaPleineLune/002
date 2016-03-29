package com.android.linglan.http.bean;

import java.util.List;

public class CityModel {
	public String city;
	List<CountryModel> county_list;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public List<CountryModel> getCounty_list() {
		return county_list;
	}
	public void setCounty_list(List<CountryModel> county_list) {
		this.county_list = county_list;
	}
	
	@Override
	public String toString() {
		return "CityModel [city=" + city + ", county_list=" + county_list + "]";
	}
	
	
	
}
