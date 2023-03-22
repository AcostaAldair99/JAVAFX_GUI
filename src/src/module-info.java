module src {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.net.http;
	requires org.json;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;
	requires javafx.base;
	exports models;
	opens application to javafx.graphics, javafx.fxml;
}
