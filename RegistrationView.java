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


public class RegistrationView extends View {

    private Label myError;
    private TextField myUsername;
    private TextField myPassword1;
    private TextField myPassword2;
    private Button myRegistrationButton;
    private Button myBackButton;
    private GridPane myButtonLayout;
    private GridPane myLayout;



    public RegistrationView(String theUsername, int theWidth, int theHeight) {
        instantiations(theUsername);
        design();
    }

    private void instantiations(String theUsername) {
        myError = new Label("");
        myUsername = new TextField("Username");
        myPassword1 = new TextField("Password");
        myPassword2 = new TextField("Reenter password");
        myRegistrationButton = new Button("Register");
        myBackButton = new Button("Back");
        myButtonLayout = new GridPane();
        myLayout = new GridPane();
    }

    private void design() {
        myError.setTextFill(Color.rgb(250, 0, 0));
        myButtonLayout.add(myRegistrationButton, 0, 0);
        myButtonLayout.add(myBackButton, 1, 0);
        myLayout.add(myError, 0, 0);
        myLayout.add(myUsername, 0, 1);
        myLayout.add(myPassword1, 0, 2);
        myLayout.add(myPassword2, 0, 3);
        myLayout.add(myButtonLayout, 0, 4);
        myScene = new Scene(myLayout);
    }

    public void setRegisterButtonHandle(EventHandler<ActionEvent> theEH) {
        myRegistrationButton.setOnAction(theEH);
    }

    public void setBackButtonHandle(EventHandler<ActionEvent> theEH) {
        myBackButton.setOnAction(theEH);
    }

    public void setEnterHandle(EventHandler<KeyEvent> theEH) {
        myUsername.setOnKeyPressed(theEH);
        myPassword1.setOnKeyPressed(theEH);
        myPassword2.setOnKeyPressed(theEH);
    }

    public String getUsername() {
        return myUsername.getText();
    }

    public String getPassword1() {
        return myPassword1.getText();
    }

    public String getPassword2() {
        return myPassword2.getText();
    }

    public void setErrorMessage(String theError) {
        myError.setText(theError);
    }
}
