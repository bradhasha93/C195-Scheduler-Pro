package com.schedulerpro.content;

import java.sql.Timestamp;

public class Appointment {

	private int appointmentId;
	private Customer customer;
	private User user;
	private String title;
	private String description;
	private String location;
	private String contact;
	private String type;
	private String url;
	private Timestamp start;
	private Timestamp end;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpdateBy;

	public Appointment() {

	}

	public Appointment(Customer customer, User user, String title, String description, String location, String contact,
			String type, String url, Timestamp start, Timestamp end, Timestamp createDate, String createdBy,
			Timestamp lastUpdate, String lastUpdateBy) {
		this.customer = customer;
		this.user = user;
		this.title = title;
		this.description = description;
		this.location = location;
		this.contact = contact;
		this.type = type;
		this.url = url;
		this.start = start;
		this.end = end;
		this.createDate = createDate;
		this.createdBy = createdBy;
		this.lastUpdate = lastUpdate;
		this.lastUpdateBy = lastUpdateBy;
	}

	/**
	 * Get appointment id
	 * 
	 * @return
	 */
	public int getAppointmentId() {
		return appointmentId;
	}

	/**
	 * Get Customer object
	 * 
	 * @return
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * Get User object
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Get title
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get location
	 * 
	 * @return
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Get contact
	 * 
	 * @return
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * Get type
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get url
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Get start date/time
	 * 
	 * @return
	 */
	public Timestamp getStart() {
		return start;
	}

	/**
	 * Get end date/time
	 * @return
	 */
	public Timestamp getEnd() {
		return end;
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
	 * Set appointment id
	 * @param appointmentId
	 */
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}

	/**
	 * Get Customer object
	 * @param customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * Set User object
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Set title
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Set description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Set location
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Set contact
	 * @param contact
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * Set type
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Set url
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Set start date/time
	 * @param start
	 */
	public void setStart(Timestamp start) {
		this.start = start;
	}

	/**
	 * Set end date/time
	 * @param end
	 */
	public void setEnd(Timestamp end) {
		this.end = end;
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
