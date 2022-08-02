package com.schedulerpro.content;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReportRecord {

	private LinkedHashMap<String, String> data;

	public ReportRecord() {
		data = new LinkedHashMap<>();
	}

	/**
	 * Get all Column names from the list of keys in the data HashMap
	 * @return
	 */
	public List<String> getColumns() {
		return data.keySet().stream().collect(Collectors.toList());
	}

	/**
	 * Add column k,v pairs to the data HashMap
	 * @param k
	 * @param v
	 */
	public void addData(String k, String v) {
		data.put(k, v);
	}

	/**
	 * Get the value for a specific column (k) and return as a StringProperty
	 * for display in a TableView
	 * @param k
	 * @return
	 */
	public StringProperty getData(String k) {
		return new SimpleStringProperty(data.get(k));
	}

}
