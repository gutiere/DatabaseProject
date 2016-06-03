import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewConvState extends State {

    private NewConvView myNewConvView;

    public NewConvState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myNewConvView = new NewConvView(myUser.getUsername(), theWidth, theHeight);
        generateControllers();
        myScene = myNewConvView.getScene();
    }

    private void generateControllers() {
        myNewConvView.setConvNameHandle(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    create(myNewConvView.getConvName());
                }
            }
        });

        myNewConvView.setCreateButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                create(myNewConvView.getConvName());
            }
        });

        myNewConvView.setCancelButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                changeState("home");
            }
        });
    }

    private void createConversation(String theName) {
        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT COUNT(conversations.name) FROM conversations WHERE conversations.name='" + theName + "';");
            if (rs.next()) {
                if (Integer.parseInt(rs.getString(1)) == 0) {
                    myDB.DML_Statement("INSERT INTO conversations (`name`, `owner`) VALUES ('" + theName + "', '" + myUser.getUsername() + "');");
                    myDB.DML_Statement("INSERT INTO `gutierrez_edgardo_db`.`conversants` (`conversation`, `conversant`) VALUES ('" + theName + "', '" + myUser.getUsername() + "');");
                    myUser.setConvName(theName);
                    changeState("home");
                } else myNewConvView.setErrorMessage("Conversation exists");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void create(String theName) {
        if (theName.length() > 0) {
            createConversation(theName);
        } else myNewConvView.setErrorMessage("Name field is empty");
    }
}
