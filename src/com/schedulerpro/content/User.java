package com.schedulerpro.content;

import java.sql.Timestamp;

public class User {
	
	private int userId;
	private String username;
	private String password;
	private int active;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpatedBy;
	
	public User() {
	}
	
	public User(String username, String password, int active, Timestamp createDate, String createdBy, Timestamp lastUpdate,
			String lastUpatedBy) {
		this.username = username;
		this.password = password;
		this.active = active;
		this.createDate = createDate;
		this.createdBy = createdBy;
		this.lastUpdate = lastUpdate;
		this.lastUpatedBy = lastUpatedBy;
	}

	/**
	 * Get user id
	 * @return
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Get username
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get password
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Get active
	 * @return
	 */
	public int getActive() {
		return active;
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
	public String getLastUpatedBy() {
		return lastUpatedBy;
	}

	/**
	 * Set user id
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Set username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Set password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Set active
	 * @param active
	 */
	public void setActive(int active) {
		this.active = active;
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
	 * @param lastUpatedBy
	 */
	public void setLastUpatedBy(String lastUpatedBy) {
		this.lastUpatedBy = lastUpatedBy;
	}
}
