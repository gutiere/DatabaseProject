/**
* @author  Edgardo Gutierrez Jr.
* @version 1.0
* @since   2016-6-1
*/

import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.sql.ResultSet;
import java.sql.SQLException;


public class RegistrationState extends State {

    private RegistrationView myRegistrationView;

    public RegistrationState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myRegistrationView = new RegistrationView(myUser.getUsername(), theWidth, theHeight);
        generateControllers();
        myScene = myRegistrationView.getScene();
    }

    private void generateControllers() {
        myRegistrationView.setRegisterButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                handleRegistrationButton(myRegistrationView.getUsername(), myRegistrationView.getPassword1(), myRegistrationView.getPassword2());
            }
        });

        myRegistrationView.setBackButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                changeState("login");
            }
        });

        myRegistrationView.setEnterHandle(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    handleRegistrationButton(myRegistrationView.getUsername(), myRegistrationView.getPassword1(), myRegistrationView.getPassword2());
                }
            }
        });
    }

    private void handleRegistrationButton(String theUsername, String thePassword1, String thePassword2) {
        if (theUsername.length() > 0 && theUsername.length() <= 15) {
            if (thePassword1.length() <= 25) {
                if (thePassword1.equals(thePassword2)) {
                    try {
                        ResultSet rs = myDB.DML_ResultSet("SELECT COUNT(login.username) from login WHERE login.username='" + theUsername + "' AND login.password='" + thePassword1 + "';");
                        if (rs.next()) {
                            if (Integer.parseInt(rs.getString(1)) == 0) {
                                myDB.DML_Statement("INSERT INTO login (`username`, `password`) VALUES ('" + theUsername + "', '" + thePassword1 + "')");
                                myUser.setUsername(theUsername);
                                changeState("login");
                            } else myRegistrationView.setErrorMessage("Username taken");
                        }
                    } catch (SQLException e) {
                        myRegistrationView.setErrorMessage("No database connection");
                    }
                } else myRegistrationView.setErrorMessage("Passwords do not match");
            } else myRegistrationView.setErrorMessage("Password 0 - 25 chars");
        } else myRegistrationView.setErrorMessage("Username 1 - 15 chars");
    }
}
