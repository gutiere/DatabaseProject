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

public class HomeState extends State {
    private TextField myTextBox;
    private int myContactID;
    private Timeline myTimer;
    private Label myChatRoom;
    private Menu myChatMenu;

    public HomeState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myLayout = new GridPane();
        generateHomeScene();
        startTimer();
    }

    private void generateHomeScene() {
        myChatRoom = new Label(getMessages(5));
        myTextBox = new TextField();

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        myChatMenu = new Menu("Chat");

        MenuItem menuItemSignOut = new MenuItem("(Sign out)");
        menuItemSignOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myTimer.stop();
                changeState("login");
            }
        });

        myChatMenu = createChatMenu();
        menuFile.getItems().add(menuItemSignOut);
        menuBar.getMenus().addAll(menuFile, createContactsMenu(), myChatMenu);
        myLayout.add(menuBar, 0, 0);
        myLayout.add(myChatRoom, 0, 1);
        myLayout.add(myTextBox, 0, 2);
        myScene = new Scene(myLayout);

        myTextBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    String text = myTextBox.getText();
                    if (text.length() > 0) {
                        submitMessage(text);
                        myTextBox.setText("");
                        myTimer.stop();
                        changeState("refresh");
                    }
                    event.consume();
                }
            }
        });
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
                // System.out.println("'" + rs.getString(2) + "'");
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
                myChatRoom.setText(getMessages(5));
            }
        }));
        myTimer.setCycleCount(Timeline.INDEFINITE);
        myTimer.play();
    }
}
