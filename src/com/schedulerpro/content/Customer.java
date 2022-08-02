package com.schedulerpro.content;

import java.sql.Timestamp;

public class Customer {
	
	private int customerId;
	private String customerName;
	private Address address;
	private int active = 1;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpdateBy;
	
	public Customer() {
	}
	
	public Customer(String customerName, Address address, int active, Timestamp createDate, String createdBy,
			Timestamp lastUpdate, String lastUpdateBy) {
		this.customerName = customerName;
		this.address = address;
		this.active = active;
		this.createDate = createDate;
		this.createdBy = createdBy;
		this.lastUpdate = lastUpdate;
		this.lastUpdateBy = lastUpdateBy;
	}

	/**
	 * Get id
	 * @return
	 */
	public int getCustomerId() {
		return customerId;
	}

	/**
	 * Get name
	 * @return
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * Get address
	 * @return
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * Get active flag
	 * @return
	 */
	public int getActive() {
		return active;
	}

	/**
	 * Get create date
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
	 * Get last update date
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
	 * Set id
	 * @param customerId
	 */
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	/**
	 * Set name
	 * @param customerName
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * Set address
	 * @param address
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * Set active flag
	 * @param active
	 */
	public void setActive(int active) {
		this.active = active;
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
	 * Set last update date
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
	
	@Override
	public String toString() {
		return this.customerName + " - " +this.getAddress().getPhone();
	}

}
