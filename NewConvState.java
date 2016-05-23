import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class NewConvState extends State {

    public NewConvState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        generateNewConvScene(theWidth, theHeight);
    }

    private void generateNewConvScene(int theWidth, int theHeight) {
        Label directions = new Label("Enter conversation name");
        TextField conversationName = new TextField();
        Button createButton = new Button("Create");
        Button cancelButton = new Button("Cancel");
        myLayout = new GridPane();
        eLabel = new Label("");
        eLabel.setTextFill(Color.rgb(250, 0, 0));

        conversationName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    create(conversationName.getText());
                }
            }
        });

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                create(conversationName.getText());
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                setChanged();
                notifyObservers("home");
            }
        });

        GridPane buttonLayout = new GridPane();
        buttonLayout.add(createButton, 0, 0);
        buttonLayout.add(cancelButton, 1, 0);

        myLayout.add(directions, 0, 0);
        myLayout.add(conversationName, 0, 1);
        myLayout.add(buttonLayout, 0, 2);
        myLayout.add(eLabel, 0, 3);
        myScene = new Scene(myLayout);
    }

    private void createConversation(String theName) {
        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT conversations.idconversations FROM conversations WHERE conversations.name='" + theName + "';");
            int idconversations = 0;
            if (rs.next()) {
                idconversations = Integer.parseInt(rs.getString(1));
            }
            if (idconversations == 0) {
                myDB.DML_Statement("INSERT INTO `gutierrez_edgardo_db`.`conversations` (`owner`, `name`) VALUES ('" + myUser.getUserID() + "', '" + theName + "');");
                rs = myDB.DML_ResultSet("SELECT conversations.idconversations FROM conversations WHERE conversations.name='" + theName + "';");
                if (rs.next()) {
                    idconversations = Integer.parseInt(rs.getString(1));
                }
                myDB.DML_Statement("INSERT INTO `gutierrez_edgardo_db`.`conversants` (`conversation`, `conversant`) VALUES ('" + idconversations + "', '" + myUser.getUserID() + "');");
            } else eLabel.setText("Conversation exists");
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
    }

    private void create(String theName) {
        if (theName.length() > 0) {
            createConversation(theName);
            setChanged();
            notifyObservers("home");
        } else {
            eLabel.setText("Name field is empty");
        }
    }
}
