package controller;

import database.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**Controls the log on screen to validate username and password. Information is translated to french if that is the users language. */
public class LogOnScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Label CurrentLocation;

    @FXML
    private Button ExitButton;

    @FXML
    private Button LogInButton;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField UsernameField;

    @FXML
    private Label locationText;

    @FXML
    private Label passwordText;

    @FXML
    private Label titleText;

    @FXML
    private Label usernameText;


    @FXML
    void ExitEvent(ActionEvent event) {

        Locale userLocale = Locale.getDefault();
        String userLanguage = userLocale.getLanguage();

        if (userLanguage == "fr") {
            Alert alertFrench = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir quitter le programme?");
            Optional<ButtonType> resultFrench = alertFrench.showAndWait();
            if (resultFrench.isPresent() && resultFrench.get() == ButtonType.OK) {
                System.exit(0);
                JDBC.closeConnection();
            }
        }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to exit the program?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    System.exit(0);
                    JDBC.closeConnection();
                }
            }
        }


    @FXML
    void LogInEvent(ActionEvent event) throws IOException, SQLException {

        Locale userLocale = Locale.getDefault();
        String userLanguage = userLocale.getLanguage();

        String username =  UsernameField.getText();
        String password = PasswordField.getText();
        boolean loginAttempt;
        LocalDateTime loginTime = LocalDateTime.now();

        if(User.checkUsername(username, password)) {
            loginAttempt = true;
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            loginAttempt = false;
            if (userLanguage == "fr") {
                Alert alertFrench = new Alert(Alert.AlertType.INFORMATION, "Nom d'utilisateur ou mot de passe invalide");
                alertFrench.show();
            } else {
                Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION, "Invalid Username or Password");
                emptyAlert.show();
            }
        }

        User.loginAttempts(loginAttempt, username, loginTime);
    }

    /**First view that is initialized. Checks user location and language and adjusts as necessary. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        CurrentLocation.setText(ZoneId.systemDefault().toString());

            try {
                resourceBundle = ResourceBundle.getBundle("main/Lan", Locale.getDefault());
                LogInButton.setText(resourceBundle.getString("Login"));
                ExitButton.setText(resourceBundle.getString("Exit"));
                locationText.setText(resourceBundle.getString("Location"));
                passwordText.setText(resourceBundle.getString("Password"));
                titleText.setText(resourceBundle.getString("Schedulingapplication"));
                usernameText.setText(resourceBundle.getString("username"));
            }
            catch (MissingResourceException e){
                //empty
            }

        }

}
