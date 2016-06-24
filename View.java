/**
* @author  Edgardo Gutierrez Jr.
* @version 1.0
* @since   2016-6-1
*/

import javafx.scene.Scene;

public abstract class View {
    protected Scene myScene;
    protected int myWidth;
    protected int myHeight;
    public Scene getScene() {
        return myScene;
    }
}
