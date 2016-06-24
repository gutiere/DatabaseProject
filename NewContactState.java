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

public class NewContactState extends State {

    NewContactView myNewContactView;

    public NewContactState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myNewContactView = new NewContactView(myUser.getUsername(), theWidth, theHeight);
        generateControllers();
        myScene = myNewContactView.getScene();
    }

    private void generateControllers() {
        myNewContactView.setContactNameHandle(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    create(myNewContactView.getContactName());
                }
            }
        });

        myNewContactView.setCreateButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                create(myNewContactView.getContactName());
            }
        });

        myNewContactView.setCancelButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                changeState("home");
            }
        });
    }

    private void create(String theName) {
        if (theName.length() > 0) {
            if (!theName.equals(myUser.getUsername())) {
                try {
                    String usernameQuery = "SELECT COUNT(users.username) FROM users WHERE users.username='" + theName + "';";
                    ResultSet rs = myDB.DML_ResultSet(usernameQuery);
                    if (rs.next()) {
                        if (Integer.parseInt(rs.getString(1)) > 0) {
                            myDB.DML_Statement("INSERT INTO `gutierrez_edgardo_db`.`contacts` (`username`, `contact`) VALUES ('" + myUser.getUsername() + "', '" + theName + "');");
                            changeState("home");
                        } myNewContactView.setErrorMessage("User does not exist");
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else myNewContactView.setErrorMessage("You cannot add yourself");
        } else myNewContactView.setErrorMessage("Username field is empty");
    }
}
