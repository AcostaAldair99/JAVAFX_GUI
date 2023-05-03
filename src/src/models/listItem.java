package models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class listItem {

	private final String name;
    private final BooleanProperty isDone = new SimpleBooleanProperty(false);

    


	public listItem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + (isDone.get() ? " DONE" : "");
    }

	public BooleanProperty isDoneProperty() {
        return isDone;
    }
    
}
