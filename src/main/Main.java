package main;

import database.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;

/**Loads the initial log on screen*/
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

              Parent root = FXMLLoader.load(getClass().getResource("/view/LogOnScreen.fxml"));
              stage.setTitle("Scheduling Application");
              stage.setScene(new Scene(root, 500, 400));
              stage.show();
          }

/** Launches program and connects to database*/
    public static void main(String[] args) throws SQLException {

        JDBC.openConnection();

        launch(args);


    }
}
