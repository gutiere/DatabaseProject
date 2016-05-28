// Run Command: "java -classpath .:mysql-connector-java-5.1.38-bin.jar Main"

import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private StateManager myStateManager;

    public static void Main(String[] theArgs) {
        launch(theArgs);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        myStateManager = new StateManager((Stage) primaryStage);
        primaryStage.setScene(myStateManager.getCurrentState());
        primaryStage.show();
    }
}
