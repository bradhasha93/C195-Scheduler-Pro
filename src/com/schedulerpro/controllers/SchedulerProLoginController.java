package com.schedulerpro.controllers;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import com.schedulerpro.main.SchedulerProApplication;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SchedulerProLoginController extends Controller implements Initializable {

	private SchedulerProApplication app;

	@FXML
	private TitledPane loginPane;

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Button createBtn, loginBtn;

	public SchedulerProLoginController(SchedulerProApplication app) {
		super("SchedulerProLogin.fxml");
		this.app = app;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void loadScene(Stage stage) {
		super.loadScene(stage);
		getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event1) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						try {
							app.exit();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		// Convert the login prompts to German if the user's locale is German
		loginPane.setText(Locale.getDefault().getCountry().equalsIgnoreCase("de") ? "SchedulerPro - Anmeldung"
				: "SchedulerPro - Login");
		username.setPromptText(Locale.getDefault().getCountry().equalsIgnoreCase("de")
				? "Geben Sie den Benutzernamen ein" : "Enter username");
		password.setPromptText(
				Locale.getDefault().getCountry().equalsIgnoreCase("de") ? "Passwort eingeben" : "Enter password");
		createBtn.setText(
				Locale.getDefault().getCountry().equalsIgnoreCase("de") ? "Ein Konto erstellen" : "Create an account");
		loginBtn.setText(Locale.getDefault().getCountry().equalsIgnoreCase("de") ? "Anmeldung" : "Login");
	}

	/**
	 * Take user to the Create an Account screen
	 * 
	 * @param event
	 */
	public void create(ActionEvent event) {
		app.getCreateAccountController().loadScene(event.getTarget());
	}

	/**
	 * Attempt to authenticate the user
	 * 
	 * @param event
	 * @throws FileNotFoundException
	 */
	public void login(ActionEvent event) throws FileNotFoundException {
		if (app.getDataManager().authenticate(username.getText().toLowerCase(), password.getText())) {
			app.getDataManager()
					.writeToLog(username.getText().toLowerCase() + " has successfully logged in.");
			app.getMainController().loadScene(event.getTarget());
			app.getMainController().initialLoad();
		} else {
			app.getDataManager()
					.writeToLog(username.getText().toLowerCase() + " has failed to log in.");
		}
	}

}
