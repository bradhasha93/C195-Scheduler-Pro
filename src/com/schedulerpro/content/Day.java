package com.schedulerpro.content;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.schedulerpro.main.SchedulerProApplication;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class Day extends AnchorPane {

	private SchedulerProApplication app;

	private ObservableList<Appointment> appts;
	private LocalDate date;

	private TableView<Appointment> apptsTbl = new TableView<>();

	private TableColumn<Appointment, String> title = new TableColumn<>("Appt");
	private TableColumn<Appointment, String> time = new TableColumn<>("Time");
	private String defaultStyle;

	@SuppressWarnings("unchecked")
	public Day(int tablePrefWidth, int tablePrefHeight, SchedulerProApplication app) {
		this.app = app;
		// Configure appts table
		apptsTbl.setPrefSize(tablePrefWidth, tablePrefHeight);
		apptsTbl.setTranslateX(2);
		apptsTbl.setTranslateY(30);

		title.setPrefWidth(70);
		time.setPrefWidth(60);
		title.setStyle("-fx-font-size: 7pt;");
		time.setStyle("-fx-font-size: 7pt;");
		apptsTbl.getColumns().addAll(title, time);

		// Bind context menu options to each row record
		apptsTbl.setRowFactory(tv -> {
			TableRow<Appointment> row = new TableRow<>();

			row.setOnContextMenuRequested(me -> {
				ContextMenu cmu = getContextMenuForPopulatedDay();
				cmu.setY(me.getScreenY());
				cmu.setX(me.getScreenX());
				cmu.show(getScene().getWindow());
				app.getMainController().setSelectedAppointment(row.getItem());
			});
			return row;
		});

		// Bind parts table
		title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
		time.setCellValueFactory(next -> {
			return new ReadOnlyStringWrapper(next.getValue().getStart().toLocalDateTime().toLocalTime()
					.format(DateTimeFormatter.ofPattern("hh:mm a")));
		});
	}

	/**
	 * Get the context menu options for a day with no appointments
	 * 
	 * @return
	 */
	private ContextMenu getContextMenuForEmptyDay() {
		// Initialize a new ContextMenu
		final ContextMenu contextMenu = new ContextMenu();

		// Setup the options
		MenuItem addAppt = new MenuItem("Add Appointment");

		addAppt.setOnAction(this::addAppointment);
		contextMenu.getItems().addAll(addAppt);
		return contextMenu;
	}

	/**
	 * Get the context menu options for a day with appointments
	 * 
	 * @return
	 */
	private ContextMenu getContextMenuForPopulatedDay() {
		// Initialize a new ContextMenu
		final ContextMenu contextMenu = new ContextMenu();

		// Setup the options
		MenuItem addAppt = new MenuItem("Add Appointment");
		MenuItem updateAppt = new MenuItem("Update Appointment");
		MenuItem deleteAppt = new MenuItem("Delete Appointment");

		addAppt.setOnAction(this::addAppointment);
		updateAppt.setOnAction(this::updateAppointment);
		deleteAppt.setOnAction(this::deleteAppointment);
		contextMenu.getItems().addAll(addAppt, updateAppt, deleteAppt);
		return contextMenu;
	}

	/**
	 * Take the user to the Add Appointment section of Manage Appointments
	 * 	
	 * @param event
	 */
	private void addAppointment(ActionEvent event) {
		app.getMainController().getAddAppointmentStartDate().setValue(getDate());
		app.getMainController().switchPane(app.getMainController().getManageAppointmentsPane());
		app.getMainController().getManageAppointmentsTabPane().getSelectionModel().select(0);
	}

	/**
	 * Take the user to the Update Appointment section of Manage Appointments
	 * and bind the selected appointment data to the form
	 * 
	 * @param event
	 */
	private void updateAppointment(ActionEvent event) {
		app.getMainController().switchPane(app.getMainController().getManageAppointmentsPane());
		app.getMainController().getManageAppointmentsTabPane().getSelectionModel().select(1);

		DateTimeFormatter hours = DateTimeFormatter.ofPattern("hh");
		DateTimeFormatter minutes = DateTimeFormatter.ofPattern("mm");
		DateTimeFormatter amPm = DateTimeFormatter.ofPattern("a");

		Appointment selectedAppointment = app.getMainController().getSelectedAppointment();
		app.getMainController().getUpdateAppointmentCustomerField().setValue(selectedAppointment.getCustomer());
		app.getMainController().getUpdateAppointmentTypeField().setValue(selectedAppointment.getType());
		app.getMainController().getUpdateAppointmentTitleField().setText(selectedAppointment.getTitle());
		app.getMainController().getUpdateAppointmentLocationField().setText(selectedAppointment.getLocation());
		app.getMainController().getUpdateAppointmentUrlField()
				.setText(selectedAppointment.getUrl() == null ? "" : selectedAppointment.getUrl());

		LocalDateTime start = selectedAppointment.getStart().toLocalDateTime();
		app.getMainController().getUpdateAppointmentStartDate().setValue(start.toLocalDate());
		app.getMainController().getUpdateAppointmentStartHour().getValueFactory()
				.setValue(start.toLocalTime().format(hours));
		app.getMainController().getUpdateAppointmentStartMinute().getValueFactory()
				.setValue(start.toLocalTime().format(minutes));
		app.getMainController().getUpdateAppointmentStartAmPm().getValueFactory()
				.setValue(start.toLocalTime().format(amPm));

		LocalDateTime end = selectedAppointment.getEnd().toLocalDateTime();
		app.getMainController().getUpdateAppointmentEndHour().getValueFactory()
				.setValue(end.toLocalTime().format(hours));
		app.getMainController().getUpdateAppointmentEndMinute().getValueFactory()
				.setValue(end.toLocalTime().format(minutes));
		app.getMainController().getUpdateAppointmentEndAmPm().getValueFactory()
				.setValue(end.toLocalTime().format(amPm));
	}

	/**
	 * Delete the selected appointment
	 * 
	 * @param event
	 */
	private void deleteAppointment(ActionEvent event) {
		Appointment toDelete = app.getMainController().getSelectedAppointment();
		if (app.alert(AlertType.CONFIRMATION,
				"Are you sure you want to delete the appointment with " + toDelete.getCustomer().getCustomerName()
						+ " on "
						+ toDelete.getStart().toLocalDateTime().toLocalDate() + " at " + DateTimeFormatter
								.ofPattern("hh:mm a").format(toDelete.getStart().toLocalDateTime().toLocalTime())
						+ "?",
				null)) {
			app.getDataManager().deleteAppointment(app.getMainController().getSelectedAppointment());
		}
	}

	/**
	 * Get the appointments table
	 * 
	 * @return
	 */
	public TableView<Appointment> getTable() {
		return apptsTbl;
	}

	/**
	 * Get the default style for the Day
	 * 
	 * @return
	 */
	public String getDefaultStyle() {
		return defaultStyle;
	}

	/**
	 * Get the date binded to the Day
	 * 
	 * @return
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Set the date binded to the Day
	 * 
	 * @param date
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Set the default style of the Day
	 * 
	 * @param defaultStyle
	 */
	public void setDefaultStyle(String defaultStyle) {
		this.defaultStyle = defaultStyle;
		setStyle(defaultStyle);
	}

	/**
	 * Load the Day with the appointments for the date
	 * 
	 * @param all
	 */
	public void loadTable(List<Appointment> all) {
		// Filter appts down to ones that are on that day
		appts = FXCollections.observableArrayList(all.stream().filter(next -> {
			return Timestamp.valueOf(next.getStart().toLocalDateTime()).toLocalDateTime().toLocalDate().atStartOfDay()
					.equals(getDate().atStartOfDay());
		}).collect(Collectors.toList()));
		apptsTbl.setItems(appts);

		// Force sort by time ascending
		time.setSortType(SortType.DESCENDING);
		apptsTbl.getSortOrder().add(time);
		apptsTbl.sort();

		// Determine type of context menu for the day and set the action
		if (apptsTbl.getItems().size() == 0) {
			setOnContextMenuRequested(me -> {
				ContextMenu cmu = getContextMenuForEmptyDay();
				cmu.setY(me.getScreenY());
				cmu.setX(me.getScreenX());
				cmu.show(getScene().getWindow());
			});
		} else {
			setOnContextMenuRequested(me -> {
			});
		}
	}
}
