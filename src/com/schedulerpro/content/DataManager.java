package com.schedulerpro.content;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.schedulerpro.main.SchedulerProApplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class DataManager {

	private SchedulerProApplication app;
	private List<Address> addresses;
	private List<City> cities;
	private List<Country> countries;
	private List<Customer> customers;
	private List<Appointment> appointments;
	private User currentUser;

	public DataManager(SchedulerProApplication app) {
		this.app = app;
		addresses = getAllAddresses();
		cities = getAllCities();
		countries = getAllCountries();
		customers = getAllCustomers();
		appointments = getAllAppointments();
	}

	/**
	 * Get the current User
	 * 
	 * @return
	 */
	public User getUser() {
		return currentUser;
	}

	/**
	 * Reload customer data
	 */
	private void reloadCustomerData() {
		addresses = getAllAddresses();
		cities = getAllCities();
		countries = getAllCountries();
		customers = getAllCustomers();
		app.getMainController().loadAllData();
	}

	/**
	 * Reload appointment data
	 */
	private void reloadAppointmentData() {
		appointments = getAllAppointments();
		app.getMainController().loadAppointmentsData();
	}

	/**
	 * Get loaded appointments
	 * 
	 * @param onlyCurrentUser
	 * @return
	 */
	public List<Appointment> getLoadedAppointments(boolean onlyCurrentUser) {
		return onlyCurrentUser
				? appointments.stream().filter(next -> next.getCreatedBy().equalsIgnoreCase(getUser().getUsername()))
						.collect(Collectors.toList())
				: appointments;
	}

	/**
	 * Get all appointment types
	 * 
	 * @return
	 */
	public List<String> getAppointmentTypes() {
		// Gets the distinct appointment types available based on current
		// appointments, efficiently uses lambda and streams
		return getLoadedAppointments(false).stream().map(Appointment::getDescription).collect(Collectors.toList())
				.stream().distinct().collect(Collectors.toList());
	}

	/**
	 * Authenticate user
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean authenticate(String username, String password) {
		try {
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM user WHERE userName = '" + username + "'";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				User user = buildUser(rs);
				if (user.getPassword().equals(password)) {
					currentUser = user;
					return true;
				} else {
					app.alert(AlertType.ERROR, Locale.getDefault().getCountry().equalsIgnoreCase("de")
							? "Ungültiges Passwort" : "Invalid password", "");
				}
			} else {
				app.alert(AlertType.ERROR,
						Locale.getDefault().equals(Locale.GERMAN)
								? "Ein Benutzer mit diesem Benutzernamen existiert nicht."
								: "A user with that username does not exist.",
						"");
			}
		} catch (Exception e) {
			app.alert(AlertType.ERROR, Locale.getDefault().equals(Locale.GERMAN)
					? "Nachschlagen des Benutzers fehlgeschlagen" : "Lookup User Unsuccessful", e.getMessage());
		}
		return false;
	}

	/**
	 * Get loaded customers
	 * 
	 * @return
	 */
	public List<Customer> getLoadedCustomers(boolean onlyActive) {
		return onlyActive ? customers.stream().filter(next -> next.getActive() == 1).collect(Collectors.toList())
				: customers;
	}

	/**
	 * Get loaded addresses
	 * 
	 * @return
	 */
	public List<Address> getLoadedAddresses() {
		return addresses;
	}

	/**
	 * Get loaded cities
	 * 
	 * @return
	 */
	public List<City> getLoadedCities() {
		return cities;
	}

	/**
	 * Get loaded countries
	 * 
	 * @return
	 */
	public List<Country> getLoadedCountries() {
		return countries;
	}

	/**
	 * Get a user by userId
	 * 
	 * @param appointmentId
	 * @return
	 */
	public User getUser(int userId) {
		try {
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM user WHERE userid = " + userId + ";";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				return buildUser(rs);
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Lookup User (ID:" + userId + ") Unsuccessful", e.getMessage());
		}
		return null;
	}

	/**
	 * Get a appointment by appointmentId
	 * 
	 * @param appointmentId
	 * @return
	 */
	public Appointment getAppointment(int appointmentId) {
		try {
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM appointment WHERE appointmentid = " + appointmentId + ";";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				return buildAppointment(rs);
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Lookup Appointment (ID:" + appointmentId + ") Unsuccessful", e.getMessage());
		}
		return null;
	}

	/**
	 * Get a customer by customerId
	 * 
	 * @param customerId
	 * @return
	 */
	public Customer getCustomer(int customerId) {
		try {
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM customer WHERE customerid = " + customerId + ";";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				return buildCustomer(rs);
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Lookup Customer (ID:" + customerId + ") Unsuccessful", e.getMessage());
		}
		return null;
	}

	/**
	 * Get a address by the addressId
	 * 
	 * @param addressId
	 * @return
	 */
	public Address getAddress(int addressId) {
		try {
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM address WHERE addressid = " + addressId + ";";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				return buildAddress(rs);
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Lookup Address (ID:" + addressId + ") Unsuccessful", e.getMessage());
		}
		return null;
	}

	/**
	 * Get a city by the cityId
	 * 
	 * @param cityId
	 * @return
	 */
	public City getCity(int cityId) {
		try {
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM city WHERE cityid = " + cityId + ";";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				return buildCity(rs);
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Lookup City (ID:" + cityId + ") Unsuccessful", e.getMessage());
		}
		return null;
	}

	/**
	 * Get a country by countryId
	 * 
	 * @param countryId
	 * @return
	 */
	public Country getCountry(int countryId) {
		try {
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM country WHERE countryid = " + countryId + ";";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				return buildCountry(rs);
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Lookup Country (ID:" + countryId + ") Unsuccessful", e.getMessage());
		}
		return null;
	}

	/**
	 * Get all available appointments
	 * 
	 * @return
	 */
	public List<Appointment> getAllAppointments() {
		try {
			List<Appointment> appts = new ArrayList<>();
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM appointment;";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				appts.add(buildAppointment(rs));
			}
			return appts;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get all available customers
	 * 
	 * @return
	 */
	public List<Customer> getAllCustomers() {
		try {
			List<Customer> customers = new ArrayList<>();
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM customer;";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				customers.add(buildCustomer(rs));
			}
			return customers;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get all available addresses
	 * 
	 * @return
	 */
	private List<Address> getAllAddresses() {
		try {
			List<Address> addresses = new ArrayList<>();
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM address;";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				addresses.add(buildAddress(rs));
			}
			return addresses;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get all available cities
	 * 
	 * @return
	 */
	public List<City> getAllCities() {
		try {
			List<City> cities = new ArrayList<>();
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM city;";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				cities.add(buildCity(rs));
			}
			return cities;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get all available countries
	 * 
	 * @return
	 */
	public List<Country> getAllCountries() {
		try {
			List<Country> countries = new ArrayList<>();
			Connection conn = app.getDatabaseManager().getConnection();

			// Prepare sql
			String sql = "SELECT * FROM country;";
			Statement stmt = conn.createStatement();

			// Execute sql and process result set
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				countries.add(buildCountry(rs));
			}
			return countries;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Take the result of a query and build a User object
	 * 
	 * @param rs
	 * @return
	 */
	private User buildUser(ResultSet rs) {
		try {
			final int userId = rs.getInt("userid");
			final String username = rs.getString("userName");
			final String password = rs.getString("password");
			final int active = rs.getInt("active");
			final String createdBy = rs.getString("createBy");
			final Timestamp createDate = rs.getTimestamp("createDate");
			final Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
			final String lastUpdatedBy = rs.getString("lastUpdatedBy");

			User next = new User();
			next.setUserId(userId);
			next.setUsername(username);
			next.setPassword(password);
			next.setActive(active);
			next.setCreatedBy(createdBy);
			next.setCreateDate(convertFromUtc(createDate));
			next.setLastUpdate(convertFromUtc(lastUpdate));
			next.setLastUpatedBy(lastUpdatedBy);
			return next;
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Error constructing User", "");
		}
		return null;
	}

	/**
	 * Take the result of a query and build a Appointment object
	 * 
	 * @param rs
	 * @return
	 */
	private Appointment buildAppointment(ResultSet rs) {
		try {
			final int appointmentId = rs.getInt("appointmentId");
			final int customerId = rs.getInt("customerId");
			final String title = rs.getString("title");
			final String description = rs.getString("description");
			final String location = rs.getString("location");
			final String contact = rs.getString("contact");
			final String url = rs.getString("url");
			final Timestamp start = rs.getTimestamp("start");
			final Timestamp end = rs.getTimestamp("end");
			final Timestamp createDate = rs.getTimestamp("createDate");
			final String createdBy = rs.getString("createdBy");
			final Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
			final String lastUpdateBy = rs.getString("lastUpdateBy");
			final String type = rs.getString("type");

			Appointment next = new Appointment();
			next.setAppointmentId(appointmentId);
			next.setCustomer(getCustomer(customerId));
			next.setTitle(title);
			next.setDescription(description);
			next.setLocation(location);
			next.setContact(contact);
			next.setUrl(url);
			next.setStart(convertFromUtc(start));
			next.setEnd(convertFromUtc(end));
			next.setCreateDate(convertFromUtc(createDate));
			next.setCreatedBy(createdBy);
			next.setLastUpdate(convertFromUtc(lastUpdate));
			next.setLastUpdateBy(lastUpdateBy);
			next.setType(type);
			return next;
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Error constructing appointment", "");
		}
		return null;
	}

	/**
	 * Take the result of a query and build a Customer object
	 * 
	 * @param rs
	 * @return
	 */
	private Customer buildCustomer(ResultSet rs) {
		try {
			final int customerId = rs.getInt("customerId");
			final String customerName = rs.getString("customerName");
			final int addressId = rs.getInt("addressId");
			final int active = rs.getInt("active");
			final Timestamp createDate = rs.getTimestamp("createDate");
			final String createdBy = rs.getString("createdBy");
			final Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
			final String lastUpdateBy = rs.getString("lastUpdateBy");

			Customer next = new Customer();
			next.setCustomerId(customerId);
			next.setCustomerName(customerName);
			next.setAddress(getAddress(addressId));
			next.setActive(active);
			next.setCreateDate(convertFromUtc(createDate));
			next.setCreatedBy(createdBy);
			next.setLastUpdate(convertFromUtc(lastUpdate));
			next.setLastUpdateBy(lastUpdateBy);
			return next;
		} catch (Exception e) {
			e.printStackTrace();
			app.alert(AlertType.ERROR, "Error constructing customer", e.getMessage());
		}
		return null;
	}

	/**
	 * Take the result from a query and build a Address object
	 * 
	 * @param rs
	 * @return
	 */
	private Address buildAddress(ResultSet rs) {
		try {
			final int addressId = rs.getInt("addressId");
			final String address = rs.getString("address");
			final String address2 = rs.getString("address2");
			final int cityId = rs.getInt("cityId");
			final String postalCode = rs.getString("postalCode");
			final String phone = rs.getString("phone");
			final Timestamp createDate = rs.getTimestamp("createDate");
			final String createdBy = rs.getString("createdBy");
			final Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
			final String lastUpdateBy = rs.getString("lastUpdateBy");

			Address next = new Address();
			next.setAddressId(addressId);
			next.setAddress(address);
			next.setAddress2(address2);
			next.setCity(getCity(cityId));
			next.setPostalCode(postalCode);
			next.setPhone(phone);
			next.setCreateDate(convertFromUtc(createDate));
			next.setCreatedBy(createdBy);
			next.setLastUpdate(convertFromUtc(lastUpdate));
			next.setLastUpdateBy(lastUpdateBy);
			return next;
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Error constructing address", "");
		}
		return null;
	}

	/**
	 * Take the result from a query and build a City object
	 * 
	 * @param rs
	 * @return
	 */
	private City buildCity(ResultSet rs) {
		try {
			final int cityId = rs.getInt("cityId");
			final String city = rs.getString("city");
			final int countryId = rs.getInt("countryId");
			final Timestamp createDate = rs.getTimestamp("createDate");
			final String createdBy = rs.getString("createdBy");
			final Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
			final String lastUpdateBy = rs.getString("lastUpdateBy");

			City next = new City();
			next.setCityId(cityId);
			next.setCity(city);
			next.setCountry(getCountry(countryId));
			next.setCreateDate(convertFromUtc(createDate));
			next.setCreatedBy(createdBy);
			next.setLastUpdate(convertFromUtc(lastUpdate));
			next.setLastUpdateBy(lastUpdateBy);
			return next;
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Error constructing city", "");
		}
		return null;
	}

	/**
	 * Take the result from a query and build a Country object
	 * 
	 * @param rs
	 * @return
	 */
	private Country buildCountry(ResultSet rs) {
		try {
			final int countryId = rs.getInt("countryId");
			final String country = rs.getString("country");
			final Timestamp createDate = rs.getTimestamp("createDate");
			final String createdBy = rs.getString("createdBy");
			final Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
			final String lastUpdateBy = rs.getString("lastUpdateBy");

			Country next = new Country();
			next.setCountryId(countryId);
			next.setCountry(country);
			next.setCreateDate(convertFromUtc(createDate));
			next.setCreatedBy(createdBy);
			next.setLastUpdate(convertFromUtc(lastUpdate));
			next.setLastUpdateBy(lastUpdateBy);
			return next;
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Error constructing country", "");
		}
		return null;
	}

	/**
	 * Manager User
	 * 
	 * @param user
	 * @return
	 */
	public boolean manageUser(User user) {
		try {
			Timestamp now = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
			Connection conn = app.getDatabaseManager().getConnection();

			// Check if the city exists
			String checkExistsSql = "SELECT * FROM user WHERE username = '" + user.getUsername() + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(checkExistsSql);
			if (rs.next()) {
				app.alert(AlertType.ERROR,
						Locale.getDefault().equals(Locale.GERMAN)
								? "Ein Benutzer mit diesem Benutzernamen existiert bereits."
								: "A user with that username already exists.  Please use a different username.",
						"");
			} else {

				// Create new user if a match was not found and return it's data
				String newEntrySql = "INSERT INTO user(userName, password, active, createBy, createDate, lastUpdate, lastUpdatedBy) VALUES(?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement newEntryStmt = conn.prepareStatement(newEntrySql);

				newEntryStmt.setString(1, user.getUsername().toLowerCase());
				newEntryStmt.setString(2, user.getPassword());
				newEntryStmt.setInt(3, 1);
				newEntryStmt.setString(4, "ADMIN");
				newEntryStmt.setString(5, now.toString());
				newEntryStmt.setString(6, now.toString());
				newEntryStmt.setString(7, "ADMIN");

				newEntryStmt.executeUpdate();
				app.alert(AlertType.INFORMATION,
						Locale.getDefault().equals(Locale.GERMAN)
								? "Benutzer erstellen (BENUTZERNAME: " + user.getUsername() + ") Erfolgreich"
								: "Create User (USERNAME: " + user.getUsername() + ") Successful",
						"");
			}
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Error managing user (USERNAME: " + user.getUsername().toLowerCase() + ")", "");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Manage customers
	 * 
	 * @param customer
	 */
	public boolean manageCustomer(Customer customer) {
		try {
			Timestamp now = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
			Connection conn = app.getDatabaseManager().getConnection();

			// Throw an error if attempting to set a customer to inactive who
			// has appointments scheduled
			if (customer.getActive() == 0) {
				String checkExistsSql = "SELECT * FROM appointment WHERE customerId = " + customer.getCustomerId();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(checkExistsSql);
				if (rs.next()) {
					app.alert(AlertType.ERROR, "Unable to deactive customer, customer has appointments scheduled",
							"Please delete appointments for the customer prior to deactivation.");
					return false;
				}
			}

			// Get current city and addr objects
			City city = customer.getAddress().getCity();
			Address addr = customer.getAddress();

			// Manage the city and address
			city = manageCity(city);
			addr.setCity(city);
			addr = manageAddress(addr);

			// Set the address for the customer again after mgmt completes
			customer.setAddress(addr);

			// Lookup current data for product to be updated
			Customer oldCustomer = getCustomer(customer.getCustomerId());

			// Update old customer record if the record exists
			if (oldCustomer != null) {
				String updateSql = "UPDATE customer SET customerName = ?, addressId = ?, active = ?, lastUpdate = ?, lastUpdateBy = ? WHERE customerid = ?";
				PreparedStatement updateStmt = conn.prepareStatement(updateSql);
				updateStmt.setString(1, customer.getCustomerName());
				updateStmt.setInt(2, customer.getAddress().getAddressId());
				updateStmt.setInt(3, customer.getActive());
				updateStmt.setTimestamp(4, now);
				updateStmt.setString(5, getUser().getUsername());
				updateStmt.setInt(6, customer.getCustomerId());
				updateStmt.executeUpdate();
			} else {
				// Create new customer if a match was not found and update the
				// tables
				String newEntrySql = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES("
						+ "?,?,?,?,?,?,?)";
				PreparedStatement newEntryStmt = conn.prepareStatement(newEntrySql, Statement.RETURN_GENERATED_KEYS);

				newEntryStmt.setString(1, customer.getCustomerName());
				newEntryStmt.setInt(2, customer.getAddress().getAddressId());
				newEntryStmt.setInt(3, customer.getActive());
				newEntryStmt.setTimestamp(4, now);
				newEntryStmt.setString(5, getUser().getUsername());
				newEntryStmt.setTimestamp(6, now);
				newEntryStmt.setString(7, getUser().getUsername());

				newEntryStmt.executeUpdate();
				ResultSet newId = newEntryStmt.getGeneratedKeys();
				newId.next();
				customer.setCustomerId(newId.getInt(1));
			}
			reloadCustomerData();
			app.alert(AlertType.INFORMATION, "Manage Customer (ID: " + customer.getCustomerId() + ") Sucessful", "");
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Manage Customer (ID:" + customer.getCustomerId() + ") Unsuccessful",
					e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Manage appointments
	 * 
	 * @param appointment
	 */
	public boolean manageAppointment(Appointment appointment) {
		try {
			Timestamp now = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
			Connection conn = app.getDatabaseManager().getConnection();

			// Validate that appointment end must be after appointment start
			if (appointment.getStart().toLocalDateTime().toLocalTime()
					.isAfter(appointment.getEnd().toLocalDateTime().toLocalTime())) {
				app.alert(AlertType.ERROR, "Appointment start cannot be after appointment end.", "");
				return false;
			}

			// Validate appointment start/end cannot be the same
			if (appointment.getStart().toLocalDateTime().equals(appointment.getEnd().toLocalDateTime())) {
				app.alert(AlertType.ERROR, "Appointment cannot start and end at the same time.", "");
				return false;
			}

			// Validate appointment start/end falls within business hours
			if (!isWithinBusinessHours(appointment.getStart()) || !isWithinBusinessHours(appointment.getEnd())) {
				app.alert(AlertType.ERROR, "Appointment start/end must be between business hours: 08:00 AM - 05:00 PM",
						"");
				return false;
			}

			// Validate appointment start/end does not overlap with an existing
			// appointment[
			String overlapping = getOverlappingAppointments(appointment);
			if (overlapping.length() > 0) {
				app.alert(AlertType.ERROR, "There are overlapping appointments, unable to manage appointment.",
						overlapping);
				return false;
			}

			// Lookup current data for product to be updated
			Appointment oldAppointment = getAppointment(appointment.getAppointmentId());

			// Update old appointment record if the record exists
			if (oldAppointment != null) {
				String updateSql = "UPDATE appointment SET customerId = ?, title = ?, description = ?, location = ?, "
						+ "contact = ?, url = ?, start = ?, end = ?, lastUpdate = ?, lastUpdateBy = ?, type = ? WHERE appointmentid = ?";
				PreparedStatement updateStmt = conn.prepareStatement(updateSql);
				updateStmt.setInt(1, appointment.getCustomer().getCustomerId());
				updateStmt.setString(2, appointment.getTitle());
				updateStmt.setString(3, appointment.getType());
				updateStmt.setString(4, appointment.getLocation());
				updateStmt.setString(5, appointment.getContact());
				updateStmt.setString(6, appointment.getUrl());
				updateStmt.setTimestamp(7, convertToUtc(appointment.getStart()));
				updateStmt.setTimestamp(8, convertToUtc(appointment.getEnd()));
				updateStmt.setTimestamp(9, now);
				updateStmt.setString(10, getUser().getUsername());
				updateStmt.setString(11, appointment.getType());
				updateStmt.setInt(12, appointment.getAppointmentId());
				updateStmt.executeUpdate();
			} else {
				// Create new appointment if a match was not found and update
				// the
				// tables
				String newEntrySql = "INSERT INTO appointment(customerId, title, description, location, "
						+ "contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy, type) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement newEntryStmt = conn.prepareStatement(newEntrySql, Statement.RETURN_GENERATED_KEYS);

				newEntryStmt.setInt(1, appointment.getCustomer().getCustomerId());
				newEntryStmt.setString(2, appointment.getTitle());
				newEntryStmt.setString(3, appointment.getType());
				newEntryStmt.setString(4, appointment.getLocation());
				newEntryStmt.setString(5, "");
				newEntryStmt.setString(6, appointment.getUrl());
				newEntryStmt.setTimestamp(7, convertToUtc(appointment.getStart()));
				newEntryStmt.setTimestamp(8, convertToUtc(appointment.getEnd()));
				newEntryStmt.setTimestamp(9, now);
				newEntryStmt.setString(10, getUser().getUsername());
				newEntryStmt.setTimestamp(11, now);
				newEntryStmt.setString(12, getUser().getUsername());
				newEntryStmt.setString(13, appointment.getType());

				newEntryStmt.executeUpdate();
				ResultSet newId = newEntryStmt.getGeneratedKeys();
				newId.next();
				appointment.setAppointmentId(newId.getInt(1));
			}
			reloadAppointmentData();
			app.alert(AlertType.INFORMATION,
					"Manage Appointment (ID: " + appointment.getAppointmentId() + ") Sucessful", "");
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Manage Appointment (ID:" + appointment.getAppointmentId() + ") Unsuccessful",
					e.getMessage());
		}
		return true;
	}

	/**
	 * Delete appointment
	 * 
	 * @param appointment
	 */
	public boolean deleteAppointment(Appointment appointment) {
		try {
			Connection conn = app.getDatabaseManager().getConnection();

			String updateSql = "DELETE FROM appointment WHERE appointmentid = ?";
			PreparedStatement updateStmt = conn.prepareStatement(updateSql);
			updateStmt.setInt(1, appointment.getAppointmentId());
			updateStmt.executeUpdate();
			reloadAppointmentData();

			app.alert(AlertType.INFORMATION,
					"Delete Appointment (ID: " + appointment.getAppointmentId() + ") Sucessful", "");
			return true;
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Delete Appointment (ID:" + appointment.getAppointmentId() + ") Unsuccessful",
					e.getMessage());
		}
		return false;
	}

	/**
	 * Manage addresses
	 * 
	 * @param address
	 * @return
	 */
	public Address manageAddress(Address address) {
		try {
			Timestamp now = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
			Connection conn = app.getDatabaseManager().getConnection();

			// Check if the address exists
			String checkExistsSql = "SELECT * FROM address WHERE address = '" + address.getAddress()
					+ "' AND address2 = '" + address.getAddress2() + "' AND postalCode = '" + address.getPostalCode()
					+ "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(checkExistsSql);
			if (rs.next()) {
				Address addr = getAddress(rs.getInt("addressid"));

				// Update phone if it has changed
				if (!rs.getString("phone").equals(address.getPhone())) {
					addr.setPhone(address.getPhone());
					String updateSql = "UPDATE address SET phone = ?, lastUpdate = ?, lastUpdateBy = ? WHERE addressid = ?";
					PreparedStatement updateStmt = conn.prepareStatement(updateSql);
					updateStmt.setString(1, addr.getPhone());
					updateStmt.setString(2, now.toString());
					updateStmt.setString(3, getUser().getUsername());
					updateStmt.setInt(4, addr.getAddressId());
					updateStmt.executeUpdate();
				}
				return addr;
			}

			// Create new address if a match was not found and return it's data
			String newEntrySql = "INSERT INTO address(address, address2, cityId, postalCode, phone, createdBy, "
					+ "createDate, lastUpdate, lastUpdateBy) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement newEntryStmt = conn.prepareStatement(newEntrySql, Statement.RETURN_GENERATED_KEYS);

			newEntryStmt.setString(1, address.getAddress());
			newEntryStmt.setString(2, address.getAddress2());
			newEntryStmt.setInt(3, address.getCity().getCityId());
			newEntryStmt.setString(4, address.getPostalCode());
			newEntryStmt.setString(5, address.getPhone());
			newEntryStmt.setString(6, getUser().getUsername());
			newEntryStmt.setString(7, now.toString());
			newEntryStmt.setString(8, now.toString());
			newEntryStmt.setString(9, getUser().getUsername());

			newEntryStmt.executeUpdate();
			ResultSet newId = newEntryStmt.getGeneratedKeys();
			newId.next();
			return getAddress(newId.getInt(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Manage cities
	 * 
	 * @param city
	 * @return
	 */
	public City manageCity(City city) {
		try {
			Timestamp now = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
			Connection conn = app.getDatabaseManager().getConnection();

			// Check if the city exists
			String checkExistsSql = "SELECT * FROM city WHERE city = '" + city.getCity() + "' AND countryId = "
					+ city.getCountry().getCountryId();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(checkExistsSql);
			if (rs.next())
				return getCity(rs.getInt("cityid"));

			// Create new city if a match was not found and return it's data
			String newEntrySql = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES(?, ?, ?, ?, ?, ?)";
			PreparedStatement newEntryStmt = conn.prepareStatement(newEntrySql, Statement.RETURN_GENERATED_KEYS);

			newEntryStmt.setString(1, city.getCity());
			newEntryStmt.setInt(2, city.getCountry().getCountryId());
			newEntryStmt.setString(3, now.toString());
			newEntryStmt.setString(4, getUser().getUsername());
			newEntryStmt.setString(5, now.toString());
			newEntryStmt.setString(6, getUser().getUsername());

			newEntryStmt.executeUpdate();
			ResultSet newId = newEntryStmt.getGeneratedKeys();
			newId.next();
			return getCity(newId.getInt(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retrieve the ObservableList of ReportRecord objects for Appointment Types
	 * by Month Report
	 * 
	 * @return
	 */
	public ObservableList<ReportRecord> report_getAppointmentTypesByMonth() {
		try {
			ObservableList<ReportRecord> data = FXCollections.observableArrayList();
			Connection conn = app.getDatabaseManager().getConnection();

			// Check if the city exists
			String reportSql = "SELECT monthname(start) as `Month`, description as `Type`, "
					+ "COUNT(description) as `Total` FROM appointment " + "GROUP BY `Month`, description";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(reportSql);

			while (rs.next()) {
				ReportRecord rr = new ReportRecord();
				rr.addData("Month", rs.getString("Month"));
				rr.addData("Type", rs.getString("Type"));
				rr.addData("Total", "" + rs.getInt("Total"));
				data.add(rr);
			}
			return data;
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Error retrieving report.  Please try again.", e.getMessage());
		}
		return null;
	}

	/**
	 * Retrieve the ObservableList of ReportRecord objects for the Consultant
	 * Schedule Report
	 * 
	 * @return
	 */
	public ObservableList<ReportRecord> report_getConsultantsReport() {
		try {
			ObservableList<ReportRecord> data = FXCollections.observableArrayList();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
			getLoadedAppointments(false).forEach(next -> {
				ReportRecord rr = new ReportRecord();
				rr.addData("Consultant", next.getCreatedBy());
				rr.addData("Customer", next.getCustomer().getCustomerName());
				rr.addData("Title", next.getTitle());
				rr.addData("Type", next.getDescription());
				rr.addData("Location", next.getLocation());
				rr.addData("Start", dtf.format(next.getStart().toLocalDateTime()));
				rr.addData("End", dtf.format(next.getEnd().toLocalDateTime()));
				data.add(rr);
			});
			return data;
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Error retrieving report.  Please try again.", e.getMessage());
		}
		return null;
	}

	/**
	 * Retrieve the ObservableList of ReportRecord objects for Appointment Types
	 * by Consultant Year Month Report
	 * 
	 * @return
	 */
	public ObservableList<ReportRecord> report_getAppointmentTypesByConsultantByYearMonth() {
		try {
			ObservableList<ReportRecord> data = FXCollections.observableArrayList();
			Connection conn = app.getDatabaseManager().getConnection();

			// Check if the city exists
			String reportSql = "SELECT createdBy as Consultant, year(start) as `Year`, "
					+ "monthname(start) as `Month`, description as `Type`, " + "COUNT(description) as `Total` "
					+ "FROM appointment " + "GROUP BY Consultant, `Year`,`Month`, description";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(reportSql);

			while (rs.next()) {
				ReportRecord rr = new ReportRecord();
				rr.addData("Consultant", rs.getString("Consultant"));
				rr.addData("Year", rs.getString("Year"));
				rr.addData("Month", rs.getString("Month"));
				rr.addData("Type", rs.getString("Type"));
				rr.addData("Total", "" + rs.getInt("Total"));
				data.add(rr);
			}
			return data;
		} catch (Exception e) {
			app.alert(AlertType.ERROR, "Error retrieving report.  Please try again.", e.getMessage());
		}
		return null;
	}

	/**
	 * Retrieve any overlapping appointments to display to the user when the
	 * user attempts to add/update existing appointments
	 * 
	 * @param appointment
	 * @return
	 */
	private String getOverlappingAppointments(Appointment appointment) {
		StringBuilder sb = new StringBuilder();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
		final LocalTime apptStartTime = appointment.getStart().toLocalDateTime().toLocalTime();
		final LocalTime apptEndTime = appointment.getEnd().toLocalDateTime().toLocalTime();
		List<Appointment> overlapping = getLoadedAppointments(true).stream()
				.filter(next -> next.getStart().toLocalDateTime().toLocalDate()
						.equals(appointment.getStart().toLocalDateTime().toLocalDate()))
				.collect(Collectors.toList()).stream().filter(next -> {
					if (next.getAppointmentId() != appointment.getAppointmentId()) {
						final LocalTime nextStartTime = next.getStart().toLocalDateTime().toLocalTime();
						final LocalTime nextEndTime = next.getEnd().toLocalDateTime().toLocalTime();

						return (((apptStartTime.isAfter(nextStartTime) || apptStartTime.equals(nextStartTime))
								&& apptStartTime.isBefore(nextEndTime))
								|| ((apptEndTime.isAfter(nextStartTime) || apptEndTime.equals(nextEndTime))
										&& apptEndTime.isBefore(nextEndTime)));
					}
					return false;
				}).collect(Collectors.toList());

		overlapping.forEach(next -> {
			sb.append(next.getTitle() + " with " + next.getCustomer().getCustomerName() + " from "
					+ dtf.format(next.getStart().toLocalDateTime()) + " to "
					+ dtf.format(next.getEnd().toLocalDateTime()) + "\n");
		});
		return sb.toString();
	}

	/**
	 * Check if the date/time falls within business hours for the user's local
	 * time zone
	 * 
	 * @param stamp
	 * @return
	 */
	private boolean isWithinBusinessHours(Timestamp stamp) {
		final LocalTime time = stamp.toLocalDateTime().toLocalTime();
		final LocalTime businessStart = LocalTime.parse("07:59");
		final LocalTime businessEnd = LocalTime.parse("17:01");
		return time.isAfter(businessStart) && !time.isAfter(businessEnd);
	}

	/**
	 * Convert a Timestamp from UTC into local
	 * 
	 * @param stamp
	 * @return
	 */
	public Timestamp convertFromUtc(Timestamp stamp) {
		if (stamp == null)
			return null;
		ZoneId newzid = ZoneId.systemDefault();
		ZonedDateTime newzdtStart = stamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
		ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);
		return Timestamp.valueOf(newLocalStart.toLocalDateTime());
	}

	/**
	 * Convert a Timestamp from local into UTC
	 * 
	 * @param stamp
	 * @return
	 */
	public Timestamp convertToUtc(Timestamp stamp) {
		if (stamp == null)
			return null;
		ZonedDateTime local = ZonedDateTime.of(stamp.toLocalDateTime(), ZoneId.systemDefault());
		ZonedDateTime utcDate = local.withZoneSameInstant(ZoneOffset.UTC);
		return Timestamp.valueOf(utcDate.toLocalDateTime());
	}

	/**
	 * Write to the log file
	 * 
	 * @param output
	 */
	public void writeToLog(String output) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File("log.txt"), true);
			fw.write(LocalDateTime.now(Clock.systemUTC()) + " - " + output + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
