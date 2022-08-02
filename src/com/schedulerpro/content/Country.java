package com.schedulerpro.content;

import java.sql.Timestamp;

public class Country {

	private int countryId;
	private String country;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpdateBy;
	
	public Country() {
		
	}

	public Country(String country, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
		this.country = country;
		this.createDate = createDate;
		this.createdBy = createdBy;
		this.lastUpdate = lastUpdate;
		this.lastUpdateBy = lastUpdatedBy;
	}

	/**
	 * Get country id
	 * @return
	 */
	public int getCountryId() {
		return countryId;
	}

	/**
	 * Get country
	 * @return
	 */
	public String getCountry() {
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
	 * Set country id
	 * @param countryId
	 */
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	/**
	 * Set country
	 * @param country
	 */
	public void setCountry(String country) {
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
	 * @param lastUpdatedBy
	 */
	public void setLastUpdateBy(String lastUpdatedBy) {
		this.lastUpdateBy = lastUpdatedBy;
	}

	@Override
	public String toString() {
		return country;
	}
}
