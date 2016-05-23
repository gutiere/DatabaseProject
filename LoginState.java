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

public class LoginState extends State {
    private GridPane myLayout;
    private Scene myScene;
    private Label eLabel;
    private TextField myUsername;

    public LoginState(int theWidth, int theHeight) {
        myLayout = new GridPane();
        myUsername = new TextField("Username");
    }

    private void generateLoginScene() {
        PasswordField password = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        myUsername.setStyle("-fx-text-inner-color: grey;");
        eLabel = new Label("");
        eLabel.setTextFill(Color.rgb(250, 0, 0));

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                handleLoginButton(myUsername.getText(), password.getText());
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
        myLayout.add(myUsername, 0, 1);
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
                setChanged();
                notifyObservers(iduser);
            } else {
                eLabel.setText("Invalid login info");
            }
        } catch (SQLException e) {
            eLabel.setText("No database connection");
        }
    }

    public void setUsername(String theUsername) {
        myUsername.setText(theUsername);
    }

    public void setErrorMessage(String theError) {
        eLabel.setText(theError);
    }

    public Scene getScene() {
        generateLoginScene();
        return myScene;
    }
}
