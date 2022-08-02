package com.schedulerpro.content;

import java.sql.Timestamp;

public class Address {

	private int addressId;
	private String address;
	private String address2;
	private City city;
	private String postalCode;
	private String phone;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpdateBy;

	public Address() {
	}

	public Address(String address, String address2, City city, String postalCode, String phone, Timestamp createDate,
			String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
		this.address = address;
		this.address2 = address2;
		this.city = city;
		this.postalCode = postalCode;
		this.phone = phone;
		this.createDate = createDate;
		this.createdBy = createdBy;
		this.lastUpdate = lastUpdate;
		this.lastUpdateBy = lastUpdateBy;
	}

	/**
	 * Get address id
	 * @return
	 */
	public int getAddressId() {
		return addressId;
	}

	/**
	 * Get address1
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Get address2
	 * @return
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * Get City object
	 * @return
	 */
	public City getCity() {
		return city;
	}

	/*
	 * Get postal code
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Get phone
	 * @return
	 */
	public String getPhone() {
		return phone;
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
	 * Set the address id
	 * @param addressId
	 */
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	/**
	 * Set address1
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Set address2
	 * @param address2
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * Set the City object
	 * @param city
	 */
	public void setCity(City city) {
		this.city = city;
	}

	/**
	 * Set postal code
	 * @param postalCode
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * Set phone
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Set create date
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
