import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginState extends State {
    public LoginState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myLayout = new GridPane();
        generateLoginScene();
    }

    private void generateLoginScene() {
        TextField username = new TextField(myUser.getUsername());
        PasswordField password = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        username.setStyle("-fx-text-inner-color: grey;");
        eLabel = new Label("");
        eLabel.setTextFill(Color.rgb(250, 0, 0));

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                handleLoginButton(username.getText(), password.getText());
            }
        });

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                setChanged();
                notifyObservers("register");
            }
        });

        GridPane buttonLayout = new GridPane();
        buttonLayout.add(registerButton, 1, 0);
        buttonLayout.add(loginButton, 0, 0);

        myLayout.add(eLabel, 0, 0);
        myLayout.add(username, 0, 1);
        myLayout.add(password, 0, 2);
        myLayout.add(buttonLayout, 0, 3);
        myScene = new Scene(myLayout);
    }

    private void handleLoginButton(String username, String password) {
        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT users.idusers FROM users WHERE users.username='" + username + "' AND users.password='" + password + "';");
            int iduser = 0;
            if (rs.next()) {
                iduser = Integer.parseInt(rs.getString(1));
            }
            if (iduser != 0) {
                myUser.setUserID(iduser);
                myUser.setUsername(username);
                setChanged();
                notifyObservers("home");
            } else {
                eLabel.setText("Invalid login info");
            }
        } catch (SQLException e) {
            eLabel.setText("No database connection");
        }
    }
}
