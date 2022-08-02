package com.schedulerpro.controllers;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.schedulerpro.content.Address;
import com.schedulerpro.content.Appointment;
import com.schedulerpro.content.City;
import com.schedulerpro.content.Country;
import com.schedulerpro.content.Customer;
import com.schedulerpro.content.Day;
import com.schedulerpro.content.ReportRecord;
import com.schedulerpro.content.ValidatedTextField;
import com.schedulerpro.main.SchedulerProApplication;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SchedulerProMainController extends Controller implements Initializable {

	private SchedulerProApplication app;
	private LocalDate ld;
	private ArrayList<Day> days;

	// Customer
	private Customer selectedCustomer;

	// Appointment
	private Appointment selectedAppointment;

	@FXML
	private Pane mainPane;

	@FXML
	private TitledPane calendarPane, manageCustomersPane, manageAppointmentsPane, reportsPane;

	@FXML
	private GridPane calendarPane_byTheMonth, calendarPane_byTheWeek;

	@FXML
	private CheckMenuItem viewMenu_byMonth, viewMenu_byWeek;

	@FXML
	private Button nextWeekBtn, previousWeekBtn;

	@FXML
	private Label monthLbl, yearLbl;

	@FXML
	private TableView<Customer> customersTbl;

	@FXML
	private TableColumn<Customer, String> customerIdCol, customerNameCol, customerAddressCol, customerActiveCol;

	// Add customer tab
	@FXML
	private TextField addAddress2Field;
	@FXML
	private ValidatedTextField addNameField, addAddressField, addCityField, addZipCodeField, addPhoneField;
	@FXML
	private ComboBox<Country> addCountryField;

	// Update customer tab
	@FXML
	private TextField updateAddress2Field, searchCustomersField;
	@FXML
	private ValidatedTextField updateNameField, updateAddressField, updateCityField, updateZipCodeField,
			updatePhoneField;

	@FXML
	private ComboBox<Country> updateCountryField;
	@FXML
	private CheckBox updateActiveField;

	private ObservableList<Customer> customers;

	// Manage Appointments
	@FXML
	private TabPane manageAppointmentsTabPane;

	// Add appointment tab
	@FXML
	private Tab addAppointmentTab;
	@FXML
	private ComboBox<Customer> addAppointmentCustomerField;
	@FXML
	private ComboBox<String> addAppointmentTypeField;
	@FXML
	private TextField addAppointmentUrlField;
	@FXML
	private ValidatedTextField addAppointmentTitleField, addAppointmentLocationField;
	@FXML
	private DatePicker addAppointmentStartDate;
	@FXML
	private Spinner<String> addAppointmentStartHour, addAppointmentStartMinute, addAppointmentStartAmPm,
			addAppointmentEndHour, addAppointmentEndMinute, addAppointmentEndAmPm;

	// Update appointment tab
	@FXML
	private Tab updateAppointmentTab;
	@FXML
	private ComboBox<Customer> updateAppointmentCustomerField;
	@FXML
	private ComboBox<String> updateAppointmentTypeField;
	@FXML
	private TextField updateAppointmentUrlField;
	@FXML
	private ValidatedTextField updateAppointmentTitleField, updateAppointmentLocationField;
	@FXML
	private DatePicker updateAppointmentStartDate;
	@FXML
	private Spinner<String> updateAppointmentStartHour, updateAppointmentStartMinute, updateAppointmentStartAmPm,
			updateAppointmentEndHour, updateAppointmentEndMinute, updateAppointmentEndAmPm;

	// Manage Reports
	@FXML
	private TableView<ReportRecord> reportsTbl;
	@FXML
	private ComboBox<String> reports;

	public SchedulerProMainController(SchedulerProApplication app) {
		super("SchedulerProMain.fxml");
		this.app = app;
	}

	@Override
	public void loadScene(Stage stage) {
		super.loadScene(stage);
		loadAllData();
	}

	@Override
	public void loadScene(EventTarget event) {
		super.loadScene(event);
		loadAllData();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		days = new ArrayList<>();
		ld = LocalDate.now();

		// Setup Manage Customers
		customerIdCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerId"));
		customerNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
		customerAddressCol
				.setCellValueFactory(next -> new ReadOnlyStringWrapper(next.getValue().getAddress().getAddress()));
		customerActiveCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("active"));

		customersTbl.setRowFactory(tv -> {
			TableRow<Customer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {

					selectedCustomer = row.getItem();
					updateNameField.setText(selectedCustomer.getCustomerName());
					updateAddressField.setText(selectedCustomer.getAddress().getAddress());
					updateAddress2Field.setText(selectedCustomer.getAddress().getAddress2());
					updateCityField.setText(selectedCustomer.getAddress().getCity().getCity());
					updateZipCodeField.setText(selectedCustomer.getAddress().getPostalCode());
					updateCountryField.getSelectionModel().select(selectedCustomer.getAddress().getCity().getCountry());
					updatePhoneField.setText(selectedCustomer.getAddress().getPhone());
					updateActiveField.setSelected(selectedCustomer.getActive() == 1 ? true : false);
				}
			});
			return row;
		});

		searchCustomersField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1) {
				loadCustomersData();
			}
		});

		// Setup Manage Appointments
		addAppointmentStartDate.setValue(LocalDate.now());
		final String[] hours = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		final String[] minutes = { "00", "15", "30", "45" };
		final String[] amPm = { "AM", "PM" };
		final String timeStyle = "-fx-font-size: 7pt;";

		SpinnerValueFactory<String> addStartHoursFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(hours));
		SpinnerValueFactory<String> addStartMinutesFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(minutes));
		SpinnerValueFactory<String> addStartAmPmFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(amPm));

		SpinnerValueFactory<String> addEndHoursFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(hours));
		SpinnerValueFactory<String> addEndMinutesFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(minutes));
		SpinnerValueFactory<String> addEndAmPmFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(amPm));

		SpinnerValueFactory<String> updateStartHoursFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(hours));
		SpinnerValueFactory<String> updateStartMinutesFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(minutes));
		SpinnerValueFactory<String> updateStartAmPmFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(amPm));

		SpinnerValueFactory<String> updateEndHoursFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(hours));
		SpinnerValueFactory<String> updateEndMinutesFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(minutes));
		SpinnerValueFactory<String> updateEndAmPmFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				FXCollections.observableArrayList(amPm));

		addAppointmentStartHour.setStyle(timeStyle);
		addAppointmentStartMinute.setStyle(timeStyle);
		addAppointmentStartAmPm.setStyle(timeStyle);
		addAppointmentEndHour.setStyle(timeStyle);
		addAppointmentEndMinute.setStyle(timeStyle);
		addAppointmentEndAmPm.setStyle(timeStyle);

		updateAppointmentStartHour.setStyle(timeStyle);
		updateAppointmentStartMinute.setStyle(timeStyle);
		updateAppointmentStartAmPm.setStyle(timeStyle);
		updateAppointmentEndHour.setStyle(timeStyle);
		updateAppointmentEndMinute.setStyle(timeStyle);
		updateAppointmentEndAmPm.setStyle(timeStyle);

		addAppointmentStartHour.setValueFactory(addStartHoursFactory);
		addAppointmentStartMinute.setValueFactory(addStartMinutesFactory);
		addAppointmentStartAmPm.setValueFactory(addStartAmPmFactory);
		addAppointmentEndHour.setValueFactory(addEndHoursFactory);
		addAppointmentEndMinute.setValueFactory(addEndMinutesFactory);
		addAppointmentEndAmPm.setValueFactory(addEndAmPmFactory);

		updateAppointmentStartHour.setValueFactory(updateStartHoursFactory);
		updateAppointmentStartMinute.setValueFactory(updateStartMinutesFactory);
		updateAppointmentStartAmPm.setValueFactory(updateStartAmPmFactory);
		updateAppointmentEndHour.setValueFactory(updateEndHoursFactory);
		updateAppointmentEndMinute.setValueFactory(updateEndMinutesFactory);
		updateAppointmentEndAmPm.setValueFactory(updateEndAmPmFactory);

		// Setup Manage Reports
		reports.setItems(FXCollections.observableArrayList(Arrays.asList(new String[] { "Appointment Types by Month",
				"Consultant Schedule", "Appointment Types by Consultant, Year, Month" })));
	}

	/**
	 * Perform initial load of application
	 */
	public void initialLoad() {
		// Create default calendar view by month
		createCalendar_byMonth(null);

		// Alert user of all appointments in the upcoming 15 minutes
		List<Appointment> upcoming = app.getDataManager().getLoadedAppointments(true).stream().filter(next -> {
			LocalDateTime ldt = LocalDateTime.now();
			Timestamp nowMinusFifteen = Timestamp.valueOf(ldt.minusMinutes(15));
			Timestamp nowPlusFifteen = Timestamp.valueOf(ldt.plusMinutes(15));
			return next.getStart().before(nowPlusFifteen) && next.getStart().after(nowMinusFifteen);
		}).collect(Collectors.toList());

		final StringBuilder sb = new StringBuilder();
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
		upcoming.forEach(next -> {
			final LocalTime start = next.getStart().toLocalDateTime().toLocalTime();
			final LocalTime end = next.getEnd().toLocalDateTime().toLocalTime();
			final String location = next.getLocation();
			sb.append("Meeting with " + next.getCustomer().getCustomerName() + " from " + start.format(dtf) + " to "
					+ end.format(dtf) + " at " + location + "\n");
		});

		if (upcoming.size() > 0)
			app.alert(AlertType.INFORMATION, "Recent and Upcoming Appointments", sb.toString());

		addCountryField.setItems(FXCollections.observableArrayList(app.getDataManager().getLoadedCountries()));
		updateCountryField.setItems(FXCollections.observableArrayList(app.getDataManager().getLoadedCountries()));
	}

	/**
	 * Load all data
	 */
	public void loadAllData() {
		loadCustomersData();
		loadAppointmentsData();
	}

	/**
	 * Load customer data
	 */
	public void loadCustomersData() {
		customers = FXCollections
				.observableArrayList(app.getDataManager().getLoadedCustomers(false).stream().filter(next -> {
					final String data = next.getCustomerId() + next.getCustomerName() + next.getAddress().getAddress()
							+ next.getActive();
					return data.toLowerCase().contains(searchCustomersField.getText().toLowerCase());
				}).collect(Collectors.toList()));
		customersTbl.setItems(customers);
	}

	/**
	 * Load appointment data
	 */
	public void loadAppointmentsData() {
		addAppointmentCustomerField
				.setItems(FXCollections.observableArrayList(app.getDataManager().getLoadedCustomers(true)));
		addAppointmentTypeField.setItems(FXCollections.observableArrayList(app.getDataManager().getAppointmentTypes()));
		updateAppointmentCustomerField
				.setItems(FXCollections.observableArrayList(app.getDataManager().getLoadedCustomers(true)));
		updateAppointmentTypeField
				.setItems(FXCollections.observableArrayList(app.getDataManager().getAppointmentTypes()));
		if (viewMenu_byMonth.isSelected()) {
			createCalendar_byMonth(null);
		} else {
			createCalendar_byWeek(null);
		}
	}

	/**
	 * Create the calendar view by week
	 * 
	 * @param event
	 */
	public void createCalendar_byWeek(ActionEvent event) {
		// Set state and visibility
		viewMenu_byMonth.setSelected(false);
		viewMenu_byWeek.setSelected(true);

		nextWeekBtn.setVisible(true);
		previousWeekBtn.setVisible(true);

		calendarPane_byTheMonth.setVisible(false);
		calendarPane_byTheWeek.setVisible(true);

		// Clear all children
		calendarPane_byTheWeek.getChildren().clear();
		days.clear();

		// Add padding
		calendarPane_byTheWeek.setPadding(new Insets(10, 10, 10, 10));

		IntStream.range(0, 7).forEach(nextDay -> {
			Day day = new Day(132, 553, app);

			// Set calendar styles
			day.getStyleClass().add("-fx-border-color: black;");
			if (nextDay % 2 == 0)
				day.getStyleClass().add("-fx-background-color: #d1d1e0;");
			StringBuilder sb = new StringBuilder();
			day.getStyleClass().stream().collect(Collectors.toList()).forEach(next -> sb.append(next));
			day.setDefaultStyle(sb.toString());

			// Add node to grid and days arraylist
			days.add(day);
			calendarPane_byTheWeek.add(day, nextDay, 0);
		});

		// Populate the dates on the calendar
		setCalendar(ld, ld.getDayOfMonth());
	}

	/**
	 * Create the calendar view by month
	 * 
	 * @param event
	 */
	public void createCalendar_byMonth(ActionEvent event) {
		// Set state and visibility
		viewMenu_byWeek.setSelected(false);
		viewMenu_byMonth.setSelected(true);

		nextWeekBtn.setVisible(false);
		previousWeekBtn.setVisible(false);

		calendarPane_byTheWeek.setVisible(false);
		calendarPane_byTheMonth.setVisible(true);

		// Clear all children
		calendarPane_byTheMonth.getChildren().clear();
		days.clear();

		// Add padding
		calendarPane_byTheMonth.setPadding(new Insets(10, 10, 10, 10));

		// Efficiently use lambda to build out the calendar by month
		IntStream.range(0, 5).forEach(nextWeek -> {

			IntStream.range(0, 7).forEach(nextDay -> {
				Day day = new Day(132, 85, app);

				// Set calendar styles
				day.getStyleClass().add("-fx-border-color: black;");
				if (nextDay % 2 == 0)
					day.getStyleClass().add("-fx-background-color: #d1d1e0;");
				StringBuilder sb = new StringBuilder();
				day.getStyleClass().stream().collect(Collectors.toList()).forEach(next -> sb.append(next));
				day.setDefaultStyle(sb.toString());

				// Add node to grid and days arraylist
				days.add(day);
				calendarPane_byTheMonth.add(day, nextDay, nextWeek);
			});
		});

		// Populate the dates on the calendar
		setCalendar(ld, 1);
	}

	/**
	 * Set the calendar with the month, year, and days
	 * 
	 * @param ld
	 */
	private void setCalendar(LocalDate ld, int startDay) {
		LocalDate date = LocalDate.of(ld.getYear(), ld.getMonthValue(), startDay);
		monthLbl.setText(ld.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
		yearLbl.setText(String.valueOf(ld.getYear()));
		while (date.getDayOfWeek().ordinal() != 6) {
			date = date.minusDays(1);
		}

		// Loop through all of the day nodes we added and set the config for
		// each
		for (int day = 0; day < days.size(); day++) {

			Day curr = days.get(day);
			// Clear children so date text from the
			// previous month is removed
			curr.setDefaultStyle(curr.getDefaultStyle());
			curr.getChildren().clear();

			// Set text for current date and label the first row with the day
			// abbreviations
			Text dayOfMonth = new Text(
					(day < 7 ? date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault())
							+ (date.getDayOfMonth() > 10 ? "	        " : "	           ") : "")
							+ String.valueOf(date.getDayOfMonth()));

			// Anchor the text onto the calendar node
			AnchorPane.setTopAnchor(dayOfMonth, 5.0);
			AnchorPane.setRightAnchor(dayOfMonth, 5.0);

			// Add the date to the node
			curr.setDate(date);

			// Set the color of the current date that is selected to light
			// blue
			if (curr.getDate().equals(ld)) {
				curr.getStyleClass().clear();
				curr.getStyleClass().add("-fx-border-color: black;");
				curr.getStyleClass().add("-fx-background-color: #99e6ff;");
				StringBuilder sb = new StringBuilder();
				// Efficiently uses lamba and streams to add additional styles
				// to our current day on the calendar
				curr.getStyleClass().stream().collect(Collectors.toList()).forEach(next -> sb.append(next));
				curr.setStyle(sb.toString());
			}

			// Add all the necessary children nodes to the current day node and
			// load the table
			curr.getChildren().addAll(dayOfMonth, curr.getTable());
			curr.loadTable(app.getDataManager().getLoadedAppointments(true));

			// Increment the date by 1
			date = date.plusDays(1);
		}
	}

	/**
	 * Set the calendar to the previous week
	 * 
	 * @param event
	 */
	public void previousWeek(ActionEvent event) {
		ld = ld.minusDays(7);
		setCalendar(ld, ld.getDayOfMonth());
	}

	/**
	 * Set the calendar to the next week
	 * 
	 * @param event
	 */
	public void nextWeek(ActionEvent event) {
		ld = ld.plusDays(7);
		setCalendar(ld, ld.getDayOfMonth());
	}

	/**
	 * Set the calendar to the previous month
	 * 
	 * @param event
	 */
	public void previousMonth(ActionEvent event) {
		ld = ld.minusMonths(1);
		setCalendar(ld, 1);
	}

	/**
	 * Set the calendar to the next month
	 * 
	 * @param event
	 */
	public void nextMonth(ActionEvent event) {
		ld = ld.plusMonths(1);
		setCalendar(ld, 1);
	}

	/**
	 * Set the calendar to the previous year
	 * 
	 * @param event
	 */
	public void previousYear(ActionEvent event) {
		ld = ld.minusYears(1);
		setCalendar(ld, 1);
	}

	/**
	 * Set the calendar to the next year
	 * 
	 * @param event
	 */
	public void nextYear(ActionEvent event) {
		ld = ld.plusYears(1);
		setCalendar(ld, 1);
	}

	/**
	 * Only allow one pane at a time to be shown due to space constraints
	 * 
	 * @param event
	 */
	public void switchPane(MouseEvent event) {
		final TitledPane[] panes = { calendarPane, manageCustomersPane, manageAppointmentsPane, reportsPane };
		for (TitledPane pane : panes) {
			if (pane.getId().equals(((Control) event.getSource()).getId())) {
				pane.setExpanded(true);
			} else {
				pane.setExpanded(false);
			}
		}
	}

	/**
	 * Only allow one pane at a time to be shown due to space constraints
	 * 
	 * @param selection
	 */
	public void switchPane(TitledPane selection) {
		final TitledPane[] panes = { calendarPane, manageCustomersPane, manageAppointmentsPane, reportsPane };
		for (TitledPane pane : panes) {
			if (pane.equals(selection)) {
				pane.setExpanded(true);
			} else {
				pane.setExpanded(false);
			}
		}
	}

	/**
	 * Reset add customer fields
	 * 
	 * @param event
	 */
	public void resetAddCustomer(ActionEvent event) {
		addNameField.setText("");
		addAddressField.setText("");
		addAddress2Field.setText("");
		addCityField.setText("");
		addZipCodeField.setText("");
		addCountryField.getSelectionModel().clearSelection();
		addPhoneField.setText("");
	}

	/**
	 * Add customer
	 * 
	 * @param event
	 */
	public void addCustomer(ActionEvent event) {
		final String errorMsgs = app.getErrorMsgs(addNameField, addAddressField, addCityField, addZipCodeField,
				addPhoneField);
		if (errorMsgs != null) {
			app.alert(AlertType.ERROR, "Invalid Data", errorMsgs);
			return;
		}

		if (addCountryField.getSelectionModel().getSelectedItem() == null) {
			app.alert(AlertType.ERROR, "Invalid Data", "You must select a valid Country.");
			return;
		}

		Customer addCustomer = new Customer();
		addCustomer.setCustomerName(addNameField.getText());

		Address addAddress = new Address();
		addAddress.setAddress(addAddressField.getText());
		addAddress.setAddress2(addAddress2Field.getText());
		addAddress.setPostalCode(addZipCodeField.getText());
		addAddress.setPhone(addPhoneField.getText());

		City addCity = new City();
		addCity.setCity(addCityField.getText());
		addCity.setCountry(addCountryField.getSelectionModel().getSelectedItem());

		addAddress.setCity(addCity);
		addCustomer.setAddress(addAddress);
		app.getDataManager().manageCustomer(addCustomer);
	}

	/**
	 * Update customer
	 * 
	 * @param event
	 */
	public void updateCustomer(ActionEvent event) {
		if (selectedCustomer != null) {
			final String errorMsgs = app.getErrorMsgs(updateNameField, updateAddressField, updateCityField,
					updateZipCodeField, updatePhoneField);
			if (errorMsgs != null) {
				app.alert(AlertType.ERROR, "Invalid Data", errorMsgs);
				return;
			}

			if (updateCountryField.getSelectionModel().getSelectedItem() == null) {
				app.alert(AlertType.ERROR, "Invalid Data", "You must select a valid Country.");
				return;
			}

			selectedCustomer.setCustomerName(updateNameField.getText());
			selectedCustomer.getAddress().setAddress(updateAddressField.getText());
			selectedCustomer.getAddress().setAddress2(updateAddress2Field.getText());
			selectedCustomer.getAddress().getCity().setCity(updateCityField.getText());
			selectedCustomer.getAddress().setPostalCode(updateZipCodeField.getText());
			selectedCustomer.getAddress().getCity()
					.setCountry(updateCountryField.getSelectionModel().getSelectedItem());
			selectedCustomer.getAddress().setPhone(updatePhoneField.getText());
			selectedCustomer.setActive(updateActiveField.isSelected() ? 1 : 0);
			if (!app.getDataManager().manageCustomer(selectedCustomer)) {
				selectedCustomer.setActive(1);
			}
		} else {
			app.alert(AlertType.ERROR, "Error", "You must select a customer in order to perform an update.");
		}
	}

	/**
	 * Add appointment
	 * 
	 * @param event
	 */
	public void addAppointment(ActionEvent event) {
		Appointment addAppointment = new Appointment();

		final Customer customer = addAppointmentCustomerField.getSelectionModel().getSelectedItem();
		final String type = addAppointmentTypeField.getSelectionModel().getSelectedItem();
		if (customer == null) {
			app.alert(AlertType.ERROR, "Invalid Data", "You must choose a customer to add an appointment.");
			return;
		}

		if (type == null) {
			app.alert(AlertType.ERROR, "Invalid Data", "You must choose a type for the appointment.");
			return;
		}

		final String errorMsgs = app.getErrorMsgs(addAppointmentTitleField, addAppointmentLocationField);
		if (errorMsgs != null) {
			app.alert(AlertType.ERROR, "Invalid Data", errorMsgs);
			return;
		}

		addAppointment.setCustomer(customer);
		addAppointment.setType(type);
		addAppointment.setDescription(type);
		addAppointment.setTitle(addAppointmentTitleField.getText());
		addAppointment.setLocation(addAppointmentLocationField.getText());
		addAppointment.setUrl(addAppointmentUrlField.getText());

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
		final String startDate = addAppointmentStartDate.getValue().toString();

		if (startDate == null) {
			app.alert(AlertType.ERROR, "Invalid Data", "You must choose a date for the appointment.");
			return;
		}

		final String startHour = addAppointmentStartHour.getValue();
		final String startMinute = addAppointmentStartMinute.getValue();
		final String startAmPm = addAppointmentStartAmPm.getValue();
		final String startDateTime = startDate + " " + startHour + ":" + startMinute + " " + startAmPm;
		addAppointment.setStart(Timestamp.valueOf(LocalDateTime.parse(startDateTime, dtf)));

		final String endDate = addAppointmentStartDate.getValue().toString();
		final String endHour = addAppointmentEndHour.getValue();
		final String endMinute = addAppointmentEndMinute.getValue();
		final String endAmPm = addAppointmentEndAmPm.getValue();
		final String endDateTime = endDate + " " + endHour + ":" + endMinute + " " + endAmPm;
		addAppointment.setEnd(Timestamp.valueOf(LocalDateTime.parse(endDateTime, dtf)));

		if (app.getDataManager().manageAppointment(addAppointment)) {
			switchPane(calendarPane);
		}
	}
	
	/**
	 * Reset add appointment
	 * @param event
	 */
	public void resetAddAppointment(ActionEvent event) {
		addAppointmentCustomerField.getSelectionModel().clearSelection();
		addAppointmentTypeField.getSelectionModel().clearSelection();
		addAppointmentTitleField.setText("");
		addAppointmentLocationField.setText("");
		addAppointmentUrlField.setText("");
	}

	/**
	 * Update appointment
	 * 
	 * @param event
	 */
	public void updateAppointment(ActionEvent event) {
		if (selectedAppointment != null) {
			final Customer customer = updateAppointmentCustomerField.getSelectionModel().getSelectedItem();
			final String type = updateAppointmentTypeField.getSelectionModel().getSelectedItem();
			if (customer == null) {
				app.alert(AlertType.ERROR, "Invalid Data", "You must choose a customer to add an appointment.");
				return;
			}

			if (type == null) {
				app.alert(AlertType.ERROR, "Invalid Data", "You must choose a type for the appointment.");
				return;
			}

			final String errorMsgs = app.getErrorMsgs(updateAppointmentTitleField, updateAppointmentLocationField);
			if (errorMsgs != null) {
				app.alert(AlertType.ERROR, "Invalid Data", errorMsgs);
				return;
			}

			selectedAppointment.setCustomer(customer);
			selectedAppointment.setType(type);
			selectedAppointment.setDescription(type);
			selectedAppointment.setTitle(updateAppointmentTitleField.getText());
			selectedAppointment.setLocation(updateAppointmentLocationField.getText());
			selectedAppointment.setUrl(updateAppointmentUrlField.getText());

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
			final String startDate = updateAppointmentStartDate.getValue().toString();

			if (startDate == null) {
				app.alert(AlertType.ERROR, "Invalid Data", "You must choose a date for the appointment.");
				return;
			}

			final String startHour = updateAppointmentStartHour.getValue();
			final String startMinute = updateAppointmentStartMinute.getValue();
			final String startAmPm = updateAppointmentStartAmPm.getValue();
			final String startDateTime = startDate + " " + startHour + ":" + startMinute + " " + startAmPm;
			selectedAppointment.setStart(Timestamp.valueOf(LocalDateTime.parse(startDateTime, dtf)));

			final String endDate = updateAppointmentStartDate.getValue().toString();
			final String endHour = updateAppointmentEndHour.getValue();
			final String endMinute = updateAppointmentEndMinute.getValue();
			final String endAmPm = updateAppointmentEndAmPm.getValue();
			final String endDateTime = endDate + " " + endHour + ":" + endMinute + " " + endAmPm;
			selectedAppointment.setEnd(Timestamp.valueOf(LocalDateTime.parse(endDateTime, dtf)));

			if (app.getDataManager().manageAppointment(selectedAppointment)) {
				switchPane(calendarPane);
			}
		} else {
			app.alert(AlertType.ERROR, "Error",
					"You must load an appointment from the calendar in order to perform an update.");
		}
	}

	/**
	 * View the chosen report
	 * 
	 * @param event
	 */
	public void viewReport(ActionEvent event) {
		reportsTbl.getColumns().clear();
		ObservableList<ReportRecord> records = null;
		switch (reports.getSelectionModel().getSelectedIndex()) {
		case 0:
			records = app.getDataManager().report_getAppointmentTypesByMonth();
			break;
		case 1:
			records = app.getDataManager().report_getConsultantsReport();
			break;
		case 2:
			records = app.getDataManager().report_getAppointmentTypesByConsultantByYearMonth();
			break;
		}
		List<TableColumn<ReportRecord, String>> columns = new ArrayList<>();

		if (records.size() > 0) {
			records.get(0).getColumns().forEach(next -> {
				TableColumn<ReportRecord, String> col = new TableColumn<>(next);
				col.setCellValueFactory(record -> record.getValue().getData(next));
				columns.add(col);
			});
			reportsTbl.getColumns().addAll(columns);
			reportsTbl.setItems(records);
		}
	}

	/**
	 * Get ld
	 * 
	 * @return
	 */
	public LocalDate getLd() {
		return ld;
	}

	/**
	 * Get selected customer
	 * 
	 * @return
	 */
	public Customer getSelectedCustomer() {
		return selectedCustomer;
	}

	/**
	 * Set selected appointment
	 * 
	 * @param appointment
	 */
	public void setSelectedAppointment(Appointment appointment) {
		this.selectedAppointment = appointment;
	}

	/**
	 * Get selected appointment
	 * 
	 * @return
	 */
	public Appointment getSelectedAppointment() {
		return selectedAppointment;
	}

	/**
	 * Get calendar pane
	 * 
	 * @return
	 */
	public TitledPane getCalendarPane() {
		return calendarPane;
	}

	/**
	 * Get manage customers pane
	 * 
	 * @return
	 */
	public TitledPane getManageCustomersPane() {
		return manageCustomersPane;
	}

	/**
	 * Get manage appointments pane
	 * 
	 * @return
	 */
	public TitledPane getManageAppointmentsPane() {
		return manageAppointmentsPane;
	}

	/**
	 * Get reports pane
	 * 
	 * @return
	 */
	public TitledPane getReportsPane() {
		return reportsPane;
	}

	/**
	 * Get manage appointments tab pane
	 * 
	 * @return
	 */
	public TabPane getManageAppointmentsTabPane() {
		return manageAppointmentsTabPane;
	}

	/**
	 * Get add appointment start date
	 * 
	 * @return
	 */
	public DatePicker getAddAppointmentStartDate() {
		return addAppointmentStartDate;
	}

	/**
	 * Get update appointment customer field
	 * 
	 * @return
	 */
	public ComboBox<Customer> getUpdateAppointmentCustomerField() {
		return updateAppointmentCustomerField;
	}

	/**
	 * Get update appointment type field
	 * 
	 * @return
	 */
	public ComboBox<String> getUpdateAppointmentTypeField() {
		return updateAppointmentTypeField;
	}

	/**
	 * Get update appointment title field
	 * 
	 * @return
	 */
	public TextField getUpdateAppointmentTitleField() {
		return updateAppointmentTitleField;
	}

	/**
	 * Get update appointment location field
	 * 
	 * @return
	 */
	public TextField getUpdateAppointmentLocationField() {
		return updateAppointmentLocationField;
	}

	/**
	 * Get update appointment url field
	 * 
	 * @return
	 */
	public TextField getUpdateAppointmentUrlField() {
		return updateAppointmentUrlField;
	}

	/**
	 * Get update appointment start date field
	 * 
	 * @return
	 */
	public DatePicker getUpdateAppointmentStartDate() {
		return updateAppointmentStartDate;
	}

	/**
	 * Get update appointment start hour field
	 * 
	 * @return
	 */
	public Spinner<String> getUpdateAppointmentStartHour() {
		return updateAppointmentStartHour;
	}

	/**
	 * Get update appointment start minute field
	 * 
	 * @return
	 */
	public Spinner<String> getUpdateAppointmentStartMinute() {
		return updateAppointmentStartMinute;
	}

	/**
	 * Get update appointment start am/pm field
	 * 
	 * @return
	 */
	public Spinner<String> getUpdateAppointmentStartAmPm() {
		return updateAppointmentStartAmPm;
	}

	/**
	 * Get update appointment end hour field
	 * 
	 * @return
	 */
	public Spinner<String> getUpdateAppointmentEndHour() {
		return updateAppointmentEndHour;
	}

	/**
	 * Get update appointment end minute field
	 * 
	 * @return
	 */
	public Spinner<String> getUpdateAppointmentEndMinute() {
		return updateAppointmentEndMinute;
	}

	/**
	 * Get update appointment end am/pm field
	 * 
	 * @return
	 */
	public Spinner<String> getUpdateAppointmentEndAmPm() {
		return updateAppointmentEndAmPm;
	}
}
