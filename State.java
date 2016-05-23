import java.util.Observable;
import javafx.scene.Scene;
import java.awt.event.ActionListener;
import javafx.event.ActionEvent;

public abstract class State extends Observable {
    private Scene myScene;
    private String myErrorMessage;

    public Scene getScene() {
        return myScene;
    }

    public void setErrorMessage(String theError) {
        myErrorMessage = theError;
    }
}
