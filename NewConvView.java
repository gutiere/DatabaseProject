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

public class NewConvView extends View {

    private Label myDirections;
    private Label myError;
    private TextField myConvName;
    private Button myCreateButton;
    private Button myCancelButton;
    private GridPane myButtonLayout, myLayout;


    public NewConvView(String theUsername, int theWidth, int theHeight) {
        myWidth = theWidth;
        myHeight = theHeight;
        instantiations();
        design();
    }

    private void instantiations() {
        myDirections = new Label("Enter conversation name");
        myConvName = new TextField();
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
        myLayout.add(myConvName, 0, 1);
        myLayout.add(myButtonLayout, 0, 2);
        myLayout.add(myError, 0, 3);
        myScene = new Scene(myLayout);
    }

    public String getConvName() {
        return myConvName.getText();
    }

    public void setConvNameHandle(EventHandler<KeyEvent> theEH) {
        myConvName.setOnKeyPressed(theEH);
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
