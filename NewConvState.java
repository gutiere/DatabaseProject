import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
// import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
// import javafx.scene.control.PasswordField;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import javafx.scene.input.KeyCode;
import javafx.event.ActionEvent;
// import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import java.sql.*;
import javafx.scene.control.*;


public class NewConvState extends State {
    // private static final String URL = "jdbc:mysql://localhost:3306/gutierrez_edgardo_db?useSSL=false";
    private static final String URL = "jdbc:mysql://192.168.1.102:3306/gutierrez_edgardo_db?useSSL=false";
    private static final String USERNAME = "UWTuser";
    private static final String PASSWORD = "something";
    private GridPane myLayout;
    private Scene myScene;
    private Label eLabel;
    private int myUserID;

    public NewConvState(int theWidth, int theHeight) {
        myLayout = new GridPane();
    }

    private void generateLoginScene() {
        Label directions = new Label("Enter conversation name");
        TextField conversationName = new TextField();
        Button createButton = new Button("Create");
        Button cancelButton = new Button("Cancel");
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
            DBAdapter db = new DBAdapter(URL, USERNAME, PASSWORD);
            ResultSet rs = db.DML_ResultSet("SELECT conversations.idconversations FROM conversations WHERE conversations.name='" + theName + "';");
            int idconversations = 0;
            if (rs.next()) {
                idconversations = Integer.parseInt(rs.getString(1));
            }
            if (idconversations == 0) {
                db.DML_Statement("INSERT INTO `gutierrez_edgardo_db`.`conversations` (`owner`, `name`) VALUES ('" + myUserID + "', '" + theName + "');");
                rs = db.DML_ResultSet("SELECT conversations.idconversations FROM conversations WHERE conversations.name='" + theName + "';");
                if (rs.next()) {
                    idconversations = Integer.parseInt(rs.getString(1));
                }
                System.out.println(idconversations);
                db.DML_Statement("INSERT INTO `gutierrez_edgardo_db`.`conversants` (`conversation`, `conversant`) VALUES ('" + idconversations + "', '" + myUserID + "');");
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

    public void setUserID(int theUserID) {
        myUserID = theUserID;
    }

    public Scene getScene() {
        generateLoginScene();
        return myScene;
    }
}
