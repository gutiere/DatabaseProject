import javafx.scene.Scene;

public abstract class View {
    protected Scene myScene;
    protected int myWidth;
    protected int myHeight;
    public Scene getScene() {
        return myScene;
    }
}
