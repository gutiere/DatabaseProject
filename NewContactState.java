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

public class NewContactState extends State {
    public NewContactState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        generateNewContactScene(theWidth, theHeight);
    }

    private void generateNewContactScene(int theWidth, int theHeight) {
        Label directions = new Label("Enter friend's username");
        TextField contactName = new TextField();
        Button createButton = new Button("Create");
        Button cancelButton = new Button("Cancel");
        myLayout = new GridPane();
        eLabel = new Label("");
        eLabel.setTextFill(Color.rgb(250, 0, 0));

        contactName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    create(contactName.getText());
                }
            }
        });

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                create(contactName.getText());
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                changeState("home");
            }
        });

        GridPane buttonLayout = new GridPane();
        buttonLayout.add(createButton, 0, 0);
        buttonLayout.add(cancelButton, 1, 0);

        myLayout.add(directions, 0, 0);
        myLayout.add(contactName, 0, 1);
        myLayout.add(buttonLayout, 0, 2);
        myLayout.add(eLabel, 0, 3);
        myScene = new Scene(myLayout);
    }

    private void create(String theName) {
        if (theName.length() > 0) {
            try {
                String usernameQuery = "SELECT COUNT(users.username) FROM users WHERE users.username='" + theName + "';";
                ResultSet rs = myDB.DML_ResultSet(usernameQuery);
                if (rs.next()) {
                    if (Integer.parseInt(rs.getString(1)) > 0) {
                        myDB.DML_Statement("INSERT INTO `gutierrez_edgardo_db`.`contacts` (`username`, `contact`) VALUES ('" + myUser.getUsername() + "', '" + theName + "');");
                        changeState("home");
                    } else eLabel.setText("User does not exist");
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        } else eLabel.setText("Username field is empty");
    }
}
