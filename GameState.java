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

import javafx.util.Duration;

public class GameState extends State {

    private GameView myGameView;
    private Timeline myTimer;
    private Chess myChess;

    public GameState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myChess = new Chess();
        // true only if party leader v
        myGameView = new GameView(false, myUser.getUsername(), getMessages(5), createChatMenu(), createContactsMenu(), theWidth, theHeight);
        generateControllers();
        myScene = myGameView.getScene();
        startTimer();
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
                        myTimer.stop();
                        changeState("refresh");
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
                        System.out.println(row + ", " + col);
                    }
                }, i, j);
            }
        }


    }

    private Menu createChatMenu() {
        Menu chatMenu = new Menu("Chat");
        String[] conversations = getConversations();
        if (conversations != null) {
            for (int i = 0; i < conversations.length; i++) {
                chatMenu.getItems().add(createConvMenu(conversations[i]));
            }
        }

        MenuItem menuItemAdd = new MenuItem("Add");
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
                MenuItem contact = new MenuItem(contactName);
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

    private Menu createConvMenu(String theConvName) {
        Menu menu = new Menu(theConvName);
        MenuItem select = new MenuItem("Select");
        Menu add = new Menu("Add user");

        select.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myUser.setConvName(theConvName);
                myTimer.stop();
                changeState("refresh");
            }
        });
        try {
            ResultSet rs = myDB.DML_ResultSet("SELECT contacts.contact, gutierrez_edgardo_db.online.online FROM contacts RIGHT JOIN gutierrez_edgardo_db.online ON contacts.contact = gutierrez_edgardo_db.online.username WHERE contacts.username='" + myUser.getUsername() + "' ORDER BY contacts.contact;");
            while (rs.next()) {
                MenuItem contact = createContactItem(theConvName,rs.getString(1));
                contact.setDisable(Integer.parseInt(rs.getString(2)) == 0);
                add.getItems().add(contact);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        menu.getItems().add(select);
        menu.getItems().add(add);

        return menu;
    }

    private MenuItem createContactItem(String theConv, String theContact) {
        MenuItem contact = new MenuItem(theContact);
        contact.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String addQuery = "INSERT INTO conversants (conversation, conversant) VALUES ('" + theConv + "', '" + theContact + "');";
                myDB.DML_Statement(addQuery);
            }
        });
        return contact;
    }

    private void submitMessage(String theMessage) {
            String submitQuery = "INSERT INTO `gutierrez_edgardo_db`.`messages` "
              + "(`sender`, `conversation`, `message`) "
              + "VALUES ('" + myUser.getUsername() + "', '" + myUser.getConvName() + "', '" + theMessage + "');";
            myDB.DML_Statement(submitQuery);
    }

    private String[] getConversations() {
        String[] conversations = null;
        try {
            String countQuery = "SELECT COUNT(conversations.name) FROM conversations, conversants WHERE conversations.name = conversants.conversation AND conversants.conversant = '" + myUser.getUsername() + "';";
            String convQuery = "SELECT conversations.name FROM conversations, conversants WHERE conversations.name = conversants.conversation AND conversants.conversant = '" + myUser.getUsername() + "';";
            ResultSet rs = myDB.DML_ResultSet(countQuery);
            if (rs.next()) {
                conversations = new String[Integer.parseInt(rs.getString(1))];
                rs = myDB.DML_ResultSet(convQuery);
                int i = 0;
                while (rs.next()) {
                    conversations[i++] = rs.getString(1);
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
            while (rs.next()) {
                messages = rs.getString(1) + '\n' + messages;
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
        return messages;
    }

    private void startTimer() {
        myTimer = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myGameView.setChatRoom(getMessages(5));
            }
        }));
        myTimer.setCycleCount(Timeline.INDEFINITE);
        myTimer.play();
    }
}
