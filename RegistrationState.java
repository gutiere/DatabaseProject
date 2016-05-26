import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import java.sql.*;


public class RegistrationState extends State {

    public RegistrationState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myLayout = new GridPane();
        generateRegistrationScene();
    }

    private void generateRegistrationScene() {
        eLabel = new Label("");
        TextField username = new TextField("Username");
        TextField password1 = new TextField("Password");
        TextField password2 = new TextField("Reenter password");
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                registerButtonHit(username.getText(), password1.getText(), password2.getText());
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                changeState("login");
            }
        });

        GridPane buttonLayout = new GridPane();
        buttonLayout.add(registerButton, 0, 0);
        buttonLayout.add(backButton, 1, 0);
        myLayout.add(eLabel, 0, 0);
        myLayout.add(username, 0, 1);
        myLayout.add(password1, 0, 2);
        myLayout.add(password2, 0, 3);
        myLayout.add(buttonLayout, 0, 4);
        myScene = new Scene(myLayout);
    }

    private void registerButtonHit(String username, String password1, String password2) {
        if (username.length() > 0 && username.length() <= 15) {
            if (password1.length() <= 25) {
                if (password1.equals(password2)) {
                    try {
                        ResultSet rs = myDB.DML_ResultSet("SELECT COUNT(login.username) from login WHERE login.username='" + username + "' AND login.password='" + password1 + "';");
                        if (rs.next()) {
                            if (Integer.parseInt(rs.getString(1)) == 0) {
                                System.out.println("TEST");
                                myDB.DML_Statement("INSERT INTO login (`username`, `password`) VALUES ('" + username + "', '" + password1 + "')");
                                myUser.setUsername(username);
                                changeState("login");
                            } else eLabel.setText("Username taken");
                        }
                    } catch (SQLException e) {
                        System.out.println("Exception: " + e);
                    }
                } else eLabel.setText("Passwords do not match");
            } else eLabel.setText("Password 0 - 25 chars");
        } else eLabel.setText("Username 1 - 15 chars");
    }
}
