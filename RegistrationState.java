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
import javafx.scene.paint.Color;
import java.sql.*;


public class RegistrationState extends State {
    private GridPane myLayout;
    private Scene myScene;
    private Label eLabel;

    public RegistrationState(int theWidth, int theHeight) {
        myLayout = new GridPane();
    }

    private void generateLoginScene() {
        eLabel = new Label("");
        TextField username = new TextField("Username");
        TextField password1 = new TextField("Password");
        TextField password2 = new TextField("Reenter password");
        Button registerButton = new Button("Register");
        Button loginButton = new Button("Back");

        eLabel.setTextFill(Color.rgb(250, 0, 0));
        username.setStyle("-fx-text-inner-color: grey;");
        password1.setStyle("-fx-text-inner-color: grey;");
        password2.setStyle("-fx-text-inner-color: grey;");


        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                registerButtonHit(username.getText(), password1.getText(), password2.getText());
            }
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                setChanged();
                notifyObservers("Username");
            }
        });

        GridPane buttonLayout = new GridPane();
        buttonLayout.add(registerButton, 0, 0);
        buttonLayout.add(loginButton, 1, 0);

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
                        ResultSet rs = myDB.DML_ResultSet("SELECT users.idusers FROM users WHERE users.username='" + username + "';");
                        int iduser = 0;
                        if (rs.next()) {
                            iduser = Integer.parseInt(rs.getString(1));
                        }
                        if (iduser == 0) {
                            myDB.DML_Statement("INSERT INTO `gutierrez_edgardo_db`.`users` (`username`, `password`) VALUES ('" + username + "', '" + password1 + "')");
                            setChanged();
                            notifyObservers(username);
                        } else eLabel.setText("Username taken");
                    } catch (SQLException e) {
                        System.out.println("Exception: " + e);
                    }
                } else eLabel.setText("Passwords do not match");
            } else eLabel.setText("Password 0 - 25 chars");
        } else eLabel.setText("Username 1 - 15 chars");
    }

    public void setErrorMessage(String theError) {
        eLabel.setText(theError);
    }

    public Scene getScene() {
        generateLoginScene();
        return myScene;
    }
}
