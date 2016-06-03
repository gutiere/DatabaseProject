import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Observer;
import java.util.Observable;

public class StateManager implements Observer {

    private static final String URL = "jdbc:mysql://localhost:3306/gutierrez_edgardo_db?useSSL=false";
    // private static final String URL = "jdbc:mysql://10.16.1.20:3306/gutierrez_edgardo_db?useSSL=false";
    private static final String USERNAME = "UWTuser";
    private static final String PASSWORD = "something";
    private static final int WIDTH = 350;
    private static final int HEIGHT = 350;
    private Stage myStage;
    private State myCurrentState;
    private DBAdapter myDB;
    private User myUser;

    public StateManager(Stage parent) {
        myStage = parent;
        myUser = new User();
        myDB = new DBAdapter(URL, USERNAME, PASSWORD);
        generateState("login");
    }

    public Scene getCurrentState() {
        return myCurrentState.getScene();
    }

    private void generateState(String theState) {
        State state = null;
        if (theState.equals("login")) {
            state = new LoginState(myDB, myUser, WIDTH, HEIGHT);
        } else if (theState.equals("register")) {
            state = new RegistrationState(myDB, myUser, WIDTH, HEIGHT);
        } else if (theState.equals("home")) {
            state = new GameState(myDB, myUser, WIDTH, HEIGHT);
            // state = new HomeState(myDB, myUser, WIDTH, HEIGHT);
        } else if (theState.equals("newconv")) {
            state = new NewConvState(myDB, myUser, WIDTH, HEIGHT);
        } else if (theState.equals("newcontact")) {
            state = new NewContactState(myDB, myUser, WIDTH, HEIGHT);
        } else if (theState.equals("newgame")) {
            state = new NewGameState(myDB, myUser, WIDTH, HEIGHT);
        } else if (theState.equals("refresh")) {
            if (myCurrentState instanceof HomeState) {
                state = new HomeState(myDB, myUser, WIDTH, HEIGHT);
            } else if (myCurrentState instanceof GameState) {
                state = new GameState(myDB, myUser, WIDTH, HEIGHT);
            }
        }
        state.addObserver(this);
        myCurrentState = state;
        myStage.setScene(myCurrentState.getScene());
        myStage.sizeToScene();
    }

    @Override
    public void update(Observable obj, Object arg) {
        if (arg instanceof String) {
            generateState((String)arg);
        }
    }
}
