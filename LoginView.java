import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class LoginView extends View {

    private TextField myUsername;
    private PasswordField myPassword;
    private GridPane myButtonLayout, myLayout;
    private Button myRegisterButton;
    private Button myLoginButton;
    private Label myError;

    public LoginView(String theUsername, int theWidth, int theHeight) {
        myWidth = 200;
        myHeight = 98;
        instantiations(theUsername);
        design();
    }

    private void instantiations(String theUsername) {
        myUsername = new TextField(theUsername);
        myPassword = new PasswordField();
        myButtonLayout = new GridPane();
        myLayout = new GridPane();
        myLoginButton = new Button("Login");
        myRegisterButton = new Button("Register");
        myError = new Label("");
    }

    private void design() {
        myUsername.setPrefWidth(myWidth);
        myPassword.setPrefWidth(myWidth);
        myLoginButton.setPrefWidth(myWidth / 2);
        myRegisterButton.setPrefWidth(myWidth / 2);


        myError.setTextFill(Color.rgb(250, 0, 0));
        myButtonLayout.add(myRegisterButton, 1, 0);
        myButtonLayout.add(myLoginButton, 0, 0);
        myLayout.add(myError, 0, 0);
        myLayout.add(myUsername, 0, 1);
        myLayout.add(myPassword, 0, 2);
        myLayout.add(myButtonLayout, 0, 3);
        myScene = new Scene(myLayout);
    }

    public void setLoginButtonHandle(EventHandler<ActionEvent> theEH) {
        myLoginButton.setOnAction(theEH);
    }

    public void setRegisterButtonHandle(EventHandler<ActionEvent> theEH) {
        myRegisterButton.setOnAction(theEH);
    }
    public void setEnterHandle(EventHandler<KeyEvent> theEH) {
        myUsername.setOnKeyPressed(theEH);
        myPassword.setOnKeyPressed(theEH);
    }

    public String getUsername() {
        return myUsername.getText();
    }

    public String getPassword() {
        return myPassword.getText();
    }

    public void setErrorMessage(String theError) {
        myError.setText(theError);
    }
}
