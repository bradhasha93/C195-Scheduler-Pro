package com.schedulerpro.content;

import java.sql.Timestamp;

public class City {
	
	private int cityId;
	private String city;
	private Country country;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpdateBy;
	
	public City() {
		
	}
	
	public City(String city, Country country, Timestamp createDate, String createdBy, Timestamp lastUpdate,
			String lastUpdateBy) {
		this.city = city;
		this.country = country;
		this.createDate = createDate;
		this.createdBy = createdBy;
		this.lastUpdate = lastUpdate;
		this.lastUpdateBy = lastUpdateBy;
	}

	/**
	 * Get city id
	 * @return
	 */
	public int getCityId() {
		return cityId;
	}

	/**
	 * Get city
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Get Country object
	 * @return
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * Get created date
	 * @return
	 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/**
	 * Get created by
	 * @return
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Get last updated date
	 * @return
	 */
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * Get last updated by
	 * @return
	 */
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	/**
	 * Set city id
	 * @param cityId
	 */
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	/**
	 * Set city
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Set Country object
	 * @param country
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * Set created date
	 * @param createDate
	 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * Set created by
	 * @param createdBy
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Set last updated date
	 * @param lastUpdate
	 */
	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * Set last updated by
	 * @param lastUpdateBy
	 */
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}	

}
