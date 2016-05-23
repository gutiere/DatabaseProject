import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Observer;
import java.util.Observable;

public class StateManager implements Observer {

    private static final String URL = "jdbc:mysql://localhost:3306/gutierrez_edgardo_db?useSSL=false";
    // private static final String URL = "jdbc:mysql://192.168.1.102:3306/gutierrez_edgardo_db?useSSL=false";
    private static final String USERNAME = "UWTuser";
    private static final String PASSWORD = "something";
    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private Stage myStage;
    private LoginState myLoginState;
    private RegistrationState myRegistrationState;
    private State myCurrentState;
    private int myUserID;
    private int myConvID;
    private String myConvName;
    private DBAdapter myDB;

    public StateManager(Stage parent) {
        myDB = new DBAdapter(URL, USERNAME, PASSWORD);
        // myCurrentState = generateState("home");
        myCurrentState = generateState("login");
        myStage = parent;
        myUserID = 2;
        myConvID = 1;
        myConvName = "Conversation";
    }

    public Scene getCurrentState() {
        return myCurrentState.getScene();
    }

    private State generateState(String theState) {
        State state = null;
        if (theState.equals("login")) {
            state = new LoginState(WIDTH, HEIGHT);
        } else if (theState.equals("registration")) {
            state = new RegistrationState(WIDTH, HEIGHT);
        } else if (theState.equals("home")) {
            state = new HomeState(WIDTH, HEIGHT);
            ((HomeState)state).setInfo(myUserID, myConvID, myConvName);
        } else if (theState.equals("newconv")) {
            state = new NewConvState(WIDTH, HEIGHT);
        }
        state.addObserver(this);
        state.addDatabase(myDB);
        return state;
    }

    @Override
    public void update(Observable obj, Object arg) {

        System.out.println("Update from " + obj.getClass() + ": " + arg.toString());


        // Login observations
        if (obj instanceof LoginState) {
            if (arg instanceof String) {
                if (((String)arg).equals("register")) {
                    myCurrentState = generateState("registration");
                    myStage.setScene(myCurrentState.getScene());
                }
            } else if (arg instanceof Integer) {
                myUserID = (int) arg;
                myCurrentState = generateState("home");
                myStage.setScene(myCurrentState.getScene());
            }
        }

        // Registration observations
        else if (obj instanceof RegistrationState) {
            if (arg instanceof String) {
                String name = (String)arg;
                myCurrentState = generateState("login");
                ((LoginState) myCurrentState).setUsername(name);
                myStage.setScene(myCurrentState.getScene());
            }
        }

        // Home observations
        else if (obj instanceof HomeState) {
            if (arg instanceof String) {
                String string = (String)arg;
                if (string.equals("newconv")) {
                    myCurrentState = generateState("newconv");
                    ((NewConvState) myCurrentState).setUserID(myUserID);
                    myStage.setScene(myCurrentState.getScene());
                } else if (string.equals("login")) {
                    myCurrentState = generateState("login");
                    ((LoginState) myCurrentState).setUsername(myConvName);
                    myStage.setScene(myCurrentState.getScene());
                } else if (string.equals("refresh")) {
                    myCurrentState = generateState("home");
                    myStage.setScene(myCurrentState.getScene());
                }
            } else {
                // Update of user
                Object[] info = (Object[])arg;
                myUserID = (int) info[0];
                myConvID = (int) info[1];
                myConvName = (String) info[2];
            }
        } else if (obj instanceof NewConvState) {
            if (((String)arg).equals("home")) {
                myCurrentState = generateState("home");
                myStage.setScene(myCurrentState.getScene());
            }
        }






        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        // if (arg instanceof String) {
        //     // Refreshable pages
        //     if (((String)arg).equals("refresh")) {
        //         if (myCurrentState instanceof HomeState) {
        //             myCurrentState = generateState("home");
        //             myStage.setScene(myCurrentState.getScene());
        //         }
        //     }
        //
        //     // Go to registration page
        //     else if (((String)arg).equals("register")) {
        //         myCurrentState = generateState("registration");
        //         myStage.setScene(myCurrentState.getScene());
        //     }
        //
        //     // Go to home page
        //     else if (((String)arg).equals("home")) {
        //         myCurrentState = generateState("home");
        //         myStage.setScene(myCurrentState.getScene());
        //     }
        //
        //     // Go to login page
        //     else if (((String)arg).equals("login")) {
        //         myCurrentState = generateState("login");
        //         myStage.setScene(myCurrentState.getScene());
        //     }
        //
        //     // Go to login page
        //     else if (((String)arg).equals("newconv")) {
        //         myCurrentState = generateState("newconv");
        //         ((NewConvState) myCurrentState).setUserID(1); // Make this the real user id.
        //         myStage.setScene(myCurrentState.getScene());
        //     }
        //
        //     // Go to login page with username
        //     else {
        //         if (obj instanceof RegistrationState) {
        //             myCurrentState = generateState("login");
        //             ((LoginState) myCurrentState).setUsername((String)arg);
        //             myStage.setScene(myCurrentState.getScene());
        //         }
        //     }
        // } else {
        //     if (obj instanceof HomeState) {
        //         // Update of user
        //         Object[] info = (Object[])arg;
        //         myUserID = (int) info[0];
        //         myConvID = (int) info[1];
        //         myConvName = (String) info[2];
        //     }
        // }

    }
}
