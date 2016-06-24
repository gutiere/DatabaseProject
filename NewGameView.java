/**
* @author  Edgardo Gutierrez Jr.
* @version 1.0
* @since   2016-6-1
*/

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

public class NewGameView extends View {

    private Label myDirections;
    private Label myError;
    private TextField myGameName;
    private Button myCreateButton;
    private Button myCancelButton;
    private GridPane myButtonLayout, myLayout;


    public NewGameView(String theUsername, int theWidth, int theHeight) {
        myWidth = theWidth;
        myHeight = theHeight;
        instantiations();
        design();
    }

    private void instantiations() {
        myDirections = new Label("Enter game name");
        myGameName = new TextField();
        myCreateButton = new Button("Create");
        myCancelButton = new Button("Cancel");
        myLayout = new GridPane();
        myError = new Label("");
        myButtonLayout = new GridPane();
    }

    private void design() {
        myError.setTextFill(Color.rgb(250, 0, 0));
        myButtonLayout.add(myCreateButton, 0, 0);
        myButtonLayout.add(myCancelButton, 1, 0);
        myLayout.add(myDirections, 0, 0);
        myLayout.add(myGameName, 0, 1);
        myLayout.add(myButtonLayout, 0, 2);
        myLayout.add(myError, 0, 3);
        myScene = new Scene(myLayout);
    }

    public String getGameName() {
        return myGameName.getText();
    }

    public void setGameNameHandle(EventHandler<KeyEvent> theEH) {
        myGameName.setOnKeyPressed(theEH);
    }

    public void setCancelButtonHandle(EventHandler<ActionEvent> theEH) {
        myCancelButton.setOnAction(theEH);
    }

    public void setCreateButtonHandle(EventHandler<ActionEvent> theEH) {
        myCreateButton.setOnAction(theEH);
    }

    public void setErrorMessage(String theError) {
        myError.setText(theError);
    }
}
