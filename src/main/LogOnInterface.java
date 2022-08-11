package main;

import javafx.scene.control.Alert;

/** Ensures the alert for upcoming appointments only displays one time on log in and not everytime the schedule controller is accessed */
public interface LogOnInterface {

    int logOnAdder(int logOnNumber);
}
