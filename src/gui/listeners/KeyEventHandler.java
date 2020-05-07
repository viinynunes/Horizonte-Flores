package gui.listeners;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public interface KeyEventHandler {
    EventHandler addEventHandler();
    void removeEventHandler();
}
