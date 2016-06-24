/**
* @author  Edgardo Gutierrez Jr.
* @version 1.0
* @since   2016-6-1
*/

import java.util.Observable;
import javafx.scene.Scene;
import java.awt.event.ActionListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public abstract class State extends Observable {
    protected Scene myScene;
    protected Label eLabel;
    protected DBAdapter myDB;
    protected GridPane myLayout;
    protected User myUser;

    public State(DBAdapter theDB, User theUser) {
        myDB = theDB;
        myUser = theUser;
    }

    public Scene getScene() {
        return myScene;
    }

    public void setErrorMessage(String theError) {
        eLabel.setText(theError);
    }

    public void setUser(User theUser) {
        myUser = theUser;
    }

    public void changeState(String theState) {
        setChanged();
        notifyObservers(theState);
    }
}
