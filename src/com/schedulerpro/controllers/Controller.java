package com.schedulerpro.controllers;

import java.io.IOException;

import javafx.event.EventTarget;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public abstract class Controller {

	private String file;
	private FXMLLoader loader;
	private Scene scene;
	private Stage stage;

	public Controller(String file) {
		this.file = file;
		loader = new FXMLLoader(getClass().getClassLoader().getResource(getFile()));
		loader.setController(this);
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get controller file name
	 * 
	 * @return
	 */
	public String getFile() {
		return this.file;
	}

	/**
	 * Get stage
	 * 
	 * @return
	 */
	public Stage getStage() {
		return this.stage;
	}

	/**
	 * Load scene using EventTarget
	 * 
	 * @param event
	 */
	public void loadScene(EventTarget event) {
		loadScene(event, null);
	}

	/**
	 * Load scene using Stage
	 * 
	 * @param stage
	 */
	public void loadScene(Stage stage) {
		loadScene(null, stage);
	}

	/**
	 * Load correct scene using event or current stage
	 * 
	 * @param event
	 * @param stage
	 */
	private void loadScene(EventTarget event, Stage stage) {
		// Determine originating source
		Stage app_stage = event != null
				? event instanceof MenuItem
						? (Stage) (((MenuItem) event).getParentPopup().getOwnerWindow().getScene().getWindow())
						: event instanceof Stage ? (Stage) event : (Stage) ((Node) event).getScene().getWindow()
				: stage;
		this.stage = app_stage;

		// Hide current scene if one is showing
		if (app_stage.isShowing())
			app_stage.hide();

		// Set stages scene
		app_stage.setScene(scene);
		app_stage.show();
	}

}
