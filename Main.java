// Run Command: "java -classpath .:mysql-connector-java-5.1.38-bin.jar Main"

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import java.util.Observer;
import java.util.Observable;
import java.lang.Integer;
import java.sql.*;

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
