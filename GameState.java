import chess.Board;
import chess.Chess;

import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.awt.event.ActionListener;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import javafx.geometry.Point2D;

import javafx.util.Duration;

public class GameState extends State {

    private GameView myGameView;
    private Timeline myTimer;
    private Chess myChess;
    private Point2D myOrigin;
    private Point2D myDest;

    public GameState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        // Am I the party leader?
        // myLeader = leaderStatus();
        myChess = new Chess();
        // true only if party leader v
        myGameView = new GameView(myUser, false, myUser.getUsername(), getMessages(5), createChatMenu(), createPlayMenu(), createContactsMenu(), theWidth, theHeight);
        generateControllers();
        myScene = myGameView.getScene();
        startTimer();
        myChess.update(pollBoard());
        myGameView.setBoardChars(myChess.getBoardChars());
    }

    private void generateControllers() {

        myGameView.setSignoutHandle(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myTimer.stop();
                changeState("login");
            }
        });

        myGameView.setEnterHandle(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    String text = myGameView.getTextField();
                    if (text.length() > 0) {
                        submitMessage(text);
                        myGameView.setTextField("");
                    }
                }
            }
        });

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final int row = i;
                final int col = j;
                myGameView.setBoardHandle(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (myOrigin == null) {
                            myOrigin = new Point2D(col, row);
                        } else {
                            if (true) {
                                myChess.move((int) myOrigin.getX(), 7 - (int) myOrigin.getY(), col, 7 - row);
                            } else {
                                myChess.move((int) myOrigin.getX(), (int) myOrigin.getY(), col, row);
                            }
                            myOrigin = null;
                            myDest = null;
                            submitBoard(myChess.getBoard());
                        }
                        myGameView.setBoardChars(myChess.getBoardChars());
                    }
                }, i, j);
            }
        }
    }

    private char[][] retrieveBoardChars() {
        char[][] board = new char[8][8];
        int counter = 0;
        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT contacts.contact, gutierrez_edgardo_db.online.online FROM contacts RIGHT JOIN gutierrez_edgardo_db.online ON contacts.contact = gutierrez_edgardo_db.online.username WHERE contacts.username='" + myUser.getUsername() + "' ORDER BY contacts.contact;");
            if (rs != null) {
                rs.next();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        board[i][j] = rs.getString(1).charAt(counter++);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return board;
    }

    private Menu createPlayMenu() {
        Menu playMenu = new Menu("Play");
        String[] games = getGames();
        if (games != null) {
            for (int i = 0; i < games.length; i++) {
                playMenu.getItems().add(createGameMenu(games[i]));
            }
        }

        MenuItem menuItemAdd = new MenuItem("Create game");
        menuItemAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myTimer.stop();
                changeState("newgame");
            }
        });

        MenuItem menuItemRefresh = new MenuItem("Refresh");
        menuItemRefresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myTimer.stop();
                changeState("refresh");
            }
        });

        playMenu.getItems().addAll(menuItemAdd, menuItemRefresh);
        return playMenu;
    }

    private Menu createChatMenu() {
        Menu chatMenu = new Menu("Chat");
        String[] conversations = getConversations();
        if (conversations != null) {
            for (int i = 0; i < conversations.length; i++) {
                chatMenu.getItems().add(createConvMenu(conversations[i]));
            }
        }

        MenuItem menuItemAdd = new MenuItem("Create conversation");
        menuItemAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myTimer.stop();
                changeState("newconv");
            }
        });

        MenuItem menuItemRefresh = new MenuItem("Refresh");
        menuItemRefresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myTimer.stop();
                changeState("refresh");
            }
        });

        chatMenu.getItems().addAll(menuItemAdd, menuItemRefresh);
        return chatMenu;
    }

    private Menu createContactsMenu() {
        Menu contactMenu = new Menu("Contacts");
        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT contacts.contact, gutierrez_edgardo_db.online.online FROM contacts RIGHT JOIN gutierrez_edgardo_db.online ON contacts.contact = gutierrez_edgardo_db.online.username WHERE contacts.username='" + myUser.getUsername() + "' ORDER BY contacts.contact;");
            while (rs.next()) {
                String contactName = rs.getString(1);
                Menu contact = new Menu(contactName);
                MenuItem delete = new MenuItem("Delete");
                delete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        String dropQuery = "DELETE FROM contacts WHERE contacts.username = '" + myUser.getUsername() + "' AND contacts.contact = '" + contactName + "';";
                        myDB.DML_Statement(dropQuery);
                        myTimer.stop();
                        changeState("refresh");
                    }
                });
                contact.getItems().add(delete);
                contact.setDisable(Integer.parseInt(rs.getString(2)) == 0);
                contactMenu.getItems().add(contact);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        MenuItem addContact = new MenuItem("Add");
        addContact.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myTimer.stop();
                changeState("newcontact");
            }
        });

        contactMenu.getItems().add(addContact);
        return contactMenu;
    }

    private Menu createGameMenu(String theGameName) {
        Menu menu = new Menu(theGameName);
        MenuItem select = new MenuItem("Select");
        MenuItem delete = new MenuItem("Delete");
        Menu add = new Menu("Add contact");

        select.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myUser.setGameName(theGameName);
                myTimer.stop();
                changeState("refresh");
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String dropQuery = "DELETE FROM players WHERE players.player = '" + myUser.getUsername() + "' AND players.game = '" + theGameName + "';";
                myDB.DML_Statement(dropQuery);
                if (myUser.getGameName().equals(theGameName)) {
                    myUser.setGameName("NoGame");
                }
                myTimer.stop();
                changeState("refresh");
            }
        });

        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT contacts.contact, gutierrez_edgardo_db.online.online FROM contacts RIGHT JOIN gutierrez_edgardo_db.online ON contacts.contact = gutierrez_edgardo_db.online.username WHERE contacts.username='" + myUser.getUsername() + "' ORDER BY contacts.contact;");
            if (rs != null) {
                while (rs.next()) {
                    MenuItem player = createPlayerItem(theGameName,rs.getString(1));
                    player.setDisable(Integer.parseInt(rs.getString(2)) == 0);
                    add.getItems().add(player);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        menu.getItems().add(select);
        menu.getItems().add(add);
        menu.getItems().add(delete);

        return menu;
    }

    private Menu createConvMenu(String theConvName) {
        Menu menu = new Menu(theConvName);
        MenuItem select = new MenuItem("Select");
        MenuItem delete = new MenuItem("Delete");
        Menu add = new Menu("Add contact");

        select.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myUser.setConvName(theConvName);
                myTimer.stop();
                changeState("refresh");
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String dropQuery = "DELETE FROM conversants WHERE conversants.conversant = '" + myUser.getUsername() + "' AND conversants.conversation = '" + theConvName + "';";
                myDB.DML_Statement(dropQuery);
                if (myUser.getConvName().equals(theConvName)) {
                    myUser.setConvName("NoConv");
                }
                myTimer.stop();
                changeState("refresh");
            }
        });

        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT contacts.contact, gutierrez_edgardo_db.online.online FROM contacts RIGHT JOIN gutierrez_edgardo_db.online ON contacts.contact = gutierrez_edgardo_db.online.username WHERE contacts.username='" + myUser.getUsername() + "' ORDER BY contacts.contact;");
            while (rs.next()) {
                MenuItem contact = createConversantItem(theConvName,rs.getString(1));
                contact.setDisable(Integer.parseInt(rs.getString(2)) == 0);
                add.getItems().add(contact);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        menu.getItems().add(select);
        menu.getItems().add(add);
        menu.getItems().add(delete);

        return menu;
    }

    private MenuItem createConversantItem(String theConv, String theContact) {
        MenuItem contact = new MenuItem(theContact);
        contact.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String addQuery = "INSERT INTO conversants (conversation, conversant) VALUES ('" + theConv + "', '" + theContact + "');";
                myDB.DML_Statement(addQuery);
            }
        });
        return contact;
    }

    private MenuItem createPlayerItem(String theGame, String thePlayer) {
        MenuItem player = new MenuItem(thePlayer);
        player.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String addQuery = "INSERT INTO players (game, player) VALUES ('" + theGame + "', '" + thePlayer + "');";
                myDB.DML_Statement(addQuery);
            }
        });
        return player;
    }

    private void submitMessage(String theMessage) {
        String message = myUser.getUsername() + ": " + theMessage;
        String submitQuery = "INSERT INTO `gutierrez_edgardo_db`.`messages` "
          + "(`sender`, `conversation`, `message`) "
          + "VALUES ('" + myUser.getUsername() + "', '" + myUser.getConvName() + "', '" + message + "');";
        myDB.DML_Statement(submitQuery);
    }

    private void submitBoard(String theBoard) {
            String submitQuery = "INSERT INTO `gutierrez_edgardo_db`.`plays` "
              + "(`game`, `player`, `play`) "
              + "VALUES ('" + myUser.getGameName() + "', '" + myUser.getUsername() + "', '" + theBoard + "');";
            myDB.DML_Statement(submitQuery);
    }

    private String[] getGames() {
        String[] games = null;
        try {
            String countQuery = "SELECT COUNT(games.name) FROM games, players WHERE games.name = players.game AND players.player = '" + myUser.getUsername() + "';";
            String gameQuery = "SELECT games.name FROM games, players WHERE games.name = players.game AND players.player = '" + myUser.getUsername() + "';";
            ResultSet rs = myDB.DML_ResultSet(countQuery);
            if (rs != null) {
                if (rs.next()) {
                    games = new String[Integer.parseInt(rs.getString(1))];
                    rs = myDB.DML_ResultSet(gameQuery);
                    int i = 0;
                    while (rs.next()) {
                        games[i++] = rs.getString(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
        return games;
    }

    private String[] getConversations() {
        String[] conversations = null;
        try {
            String countQuery = "SELECT COUNT(conversations.name) FROM conversations, conversants WHERE conversations.name = conversants.conversation AND conversants.conversant = '" + myUser.getUsername() + "';";
            String convQuery = "SELECT conversations.name FROM conversations, conversants WHERE conversations.name = conversants.conversation AND conversants.conversant = '" + myUser.getUsername() + "';";
            ResultSet rs = myDB.DML_ResultSet(countQuery);
            if (rs != null) {
                if (rs.next()) {
                    conversations = new String[Integer.parseInt(rs.getString(1))];
                    rs = myDB.DML_ResultSet(convQuery);
                    int i = 0;
                    while (rs.next()) {
                        conversations[i++] = rs.getString(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
        return conversations;
    }

    private String getMessages(int theAmount) {
        String messages = "";
        try {
            String messagesQuery = "SELECT messages.message FROM messages WHERE messages.conversation='" + myUser.getConvName() + "' ORDER BY messages.id DESC LIMIT " + theAmount + ";";
            ResultSet rs = myDB.DML_ResultSet(messagesQuery);
            if (rs != null) {
                while (rs.next()) {
                    messages = rs.getString(1) + '\n' + messages;
                }
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
        return messages;
    }

    public String pollBoard() {
        String play = "";
        try {
            String playQuery = "SELECT plays.play FROM plays WHERE plays.game='" + myUser.getGameName() + "' ORDER BY plays.id DESC LIMIT 1;";
            ResultSet rs = myDB.DML_ResultSet(playQuery);
            if (rs.next()) {
                play = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
        if (play.length() != 64) {
            return "rnbkqbnrpppppppp--------------------------------PPPPPPPPRNBKQBNR";
        }
        return play;
    }

    private void startTimer() {
        myTimer = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myGameView.setChatRoom(getMessages(5));
                // myChess.update(pollBoard());
                // myGameView.setBoardChars(myChess.getBoardChars());
            }
        }));
        myTimer.setCycleCount(Timeline.INDEFINITE);
        myTimer.play();
    }
}
