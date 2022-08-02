package com.schedulerpro.main;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.schedulerpro.content.DataManager;
import com.schedulerpro.content.ValidatedTextField;
import com.schedulerpro.controllers.SchedulerProCreateAccountController;
import com.schedulerpro.controllers.SchedulerProLoginController;
import com.schedulerpro.controllers.SchedulerProMainController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SchedulerProApplication extends Application {

	private DatabaseManager databaseManager = new DatabaseManager();
	private DataManager dataManager = new DataManager(this);
	private SchedulerProMainController mainController = new SchedulerProMainController(this);
	private SchedulerProLoginController loginController = new SchedulerProLoginController(this);
	private SchedulerProCreateAccountController createAccountController = new SchedulerProCreateAccountController(this);

	public SchedulerProApplication() throws Exception {
		init();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("C195 - SchedulerPro");
		loginController.loadScene(primaryStage);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						try {
							exit();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	/**
	 * Get databaseManager
	 * 
	 * @return
	 */
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	/**
	 * Get datamManager
	 * 
	 * @return
	 */
	public DataManager getDataManager() {
		return dataManager;
	}

	/**
	 * Get main controller
	 * 
	 * @return
	 */
	public SchedulerProMainController getMainController() {
		return mainController;
	}

	/**
	 * Get login controller
	 * 
	 * @return
	 */
	public SchedulerProLoginController getLoginController() {
		return loginController;
	}

	/**
	 * Get create account controller
	 * 
	 * @return
	 */
	public SchedulerProCreateAccountController getCreateAccountController() {
		return createAccountController;
	}

	/**
	 * Close database connection and exit application
	 * 
	 * @throws SQLException
	 */
	public void exit() throws SQLException {
		if (getDataManager().getUser() != null)
			getDataManager().writeToLog(getDataManager().getUser().getUsername() + " has logged out.");
		databaseManager.close();
		System.out.println("Application closed.");
		System.exit(0);
	}

	/**
	 * Handle prompts for info, warning, confirmation, and error
	 * 
	 * @param type
	 * @param title
	 * @param content
	 * @return prompt
	 */
	public boolean alert(AlertType type, String title, String content) {
		Alert alert = new Alert(type);
		alert.setHeaderText(title);

		switch (type) {
		case CONFIRMATION:
		case INFORMATION:
		case NONE:
		case WARNING:
			alert.setContentText(content);
			break;
		case ERROR:
			// Create expandable Exception.
			TextArea textArea = new TextArea(content);
			textArea.setEditable(false);
			textArea.setWrapText(true);

			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(textArea, 0, 1);

			// Set expandable Exception into the dialog pane.
			alert.getDialogPane().setExpandableContent(expContent);
			break;

		default:
			break;

		}
		Optional<ButtonType> bt = alert.showAndWait();
		return type.equals(AlertType.CONFIRMATION) ? bt.get() == ButtonType.OK : false;
	}

	/**
	 * Validate validatedTextFields and return any errors found
	 * 
	 * @return string of errors
	 */
	public String getErrorMsgs(ValidatedTextField... fields) {
		String msg = "";

		for (ValidatedTextField field : fields) {
			if (field.hasError()) {
				msg += field.getAlertMsg() + "\n\n";
			}
		}
		return msg.length() == 0 ? null : msg;
	}
}
