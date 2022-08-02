package com.schedulerpro.controllers;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import com.schedulerpro.content.User;
import com.schedulerpro.content.ValidatedTextField;
import com.schedulerpro.main.SchedulerProApplication;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TitledPane;
import javafx.stage.WindowEvent;

public class SchedulerProCreateAccountController extends Controller implements Initializable {

	private SchedulerProApplication app;

	@FXML
	private TitledPane createPane;

	@FXML
	private ValidatedTextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Button createBtn;

	public SchedulerProCreateAccountController(SchedulerProApplication app) {
		super("SchedulerProCreateAccount.fxml");
		this.app = app;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void loadScene(EventTarget event) {
		super.loadScene(event);
		getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event1) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						app.getLoginController().loadScene(getStage());
					}
				});
			}
		});

		// Convert the create account prompts to German if the user's locale is German
		createPane.setText(Locale.getDefault().getCountry().equalsIgnoreCase("de")  ? "SchedulerPro - Ein Konto erstellen"
				: "SchedulerPro - Create an account");
		username.setPromptText(
				Locale.getDefault().getCountry().equalsIgnoreCase("de")  ? "Geben Sie den Benutzernamen ein" : "Enter username");
		password.setPromptText(Locale.getDefault().getCountry().equalsIgnoreCase("de")  ? "Passwort eingeben" : "Enter password");
		createBtn.setText(Locale.getDefault().getCountry().equalsIgnoreCase("de")  ? "Ein Konto erstellen" : "Create an account");
	}

	/**
	 * Attempt to create the new user account
	 * @param event
	 */
	public void create(ActionEvent event) {
		final String errorMsgs = app.getErrorMsgs(username);
		if (errorMsgs != null) {
			app.alert(AlertType.ERROR, "Invalid Data", errorMsgs);
			return;
		}
		User create = new User();
		create.setUsername(username.getText().toLowerCase());
		create.setPassword(password.getText());
		app.getDataManager().manageUser(create);
	}
}
