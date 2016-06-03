import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewGameState extends State {

    private NewGameView myNewGameView;

    public NewGameState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myNewGameView = new NewGameView(myUser.getUsername(), theWidth, theHeight);
        generateControllers();
        myScene = myNewGameView.getScene();
    }

    private void generateControllers() {
        myNewGameView.setGameNameHandle(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    create(myNewGameView.getGameName());
                }
            }
        });

        myNewGameView.setCreateButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                create(myNewGameView.getGameName());
            }
        });

        myNewGameView.setCancelButtonHandle(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                changeState("home");
            }
        });
    }

    private void createGame(String theName) {
        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT COUNT(games.name) FROM games WHERE games.name='" + theName + "';");
            if (rs.next()) {
                if (Integer.parseInt(rs.getString(1)) == 0) {
                    myDB.DML_Statement("INSERT INTO games (`name`, `partyleader`) VALUES ('" + theName + "', '" + myUser.getUsername() + "');");
                    myDB.DML_Statement("INSERT INTO `gutierrez_edgardo_db`.`players` (`game`, `player`) VALUES ('" + theName + "', '" + myUser.getUsername() + "');");
                    myUser.setGameName(theName);
                    changeState("home");
                } else myNewGameView.setErrorMessage("Game exists");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void create(String theName) {
        if (theName.length() > 0) {
            createGame(theName);
        } else myNewGameView.setErrorMessage("Name field is empty");
    }
}
