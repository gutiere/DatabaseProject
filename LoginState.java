import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginState extends State {
    private LoginView myLoginView;
    public LoginState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myLoginView = new LoginView(myUser.getUsername(), theWidth, theHeight);
        generateControllers();
        myScene = myLoginView.getScene();
    }

    private void generateControllers() {
        myLoginView.setLoginButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                handleLoginButton(myLoginView.getUsername(), myLoginView.getPassword());
            }
        });

        myLoginView.setRegisterButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                changeState("register");
            }
        });

        myLoginView.setEnterHandle(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    handleLoginButton(myLoginView.getUsername(), myLoginView.getPassword());
                }
            }
        });
    }

    private void handleLoginButton(String username, String password) {
        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT COUNT(login.username) from login WHERE login.username='" + username + "' AND login.password='" + password + "';");
            if (rs.next()) {
                if (Integer.parseInt(rs.getString(1)) != 0) {
                    changeState("home");
                } else myLoginView.setErrorMessage("Invalid login info");
            }
        } catch (SQLException e) {
            myLoginView.setErrorMessage("No database connection");
        }
    }
}
