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

public class HomeState extends State {
    private TextField myTextBox;

    public HomeState(DBAdapter theDB, User theUser, int theWidth, int theHeight) {
        super(theDB, theUser);
        myLayout = new GridPane();
        generateHomeScene();
    }

    private void generateHomeScene() {
        Label welcome = new Label(getMessages(5));
        myTextBox = new TextField();

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuChat = new Menu("Chat");

        String[] conversations = getConversations();
        if (conversations != null) {
            for (int i = 0; i < conversations.length; i++) {
                menuChat.getItems().add(menuCreateConvMenu(conversations[i]));
            }
        }

        MenuItem menuItemAdd = new MenuItem("  +");
        menuItemAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                setChanged();
                notifyObservers("newconv");
            }
        });

        MenuItem menuItemRefresh = new MenuItem("(Refresh)");

        menuItemRefresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                setChanged();
                notifyObservers("refresh");
            }
        });

        MenuItem menuItemSignOut = new MenuItem("(Sign out)");
        menuItemSignOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                setChanged();
                notifyObservers("login");
            }
        });

        menuChat.getItems().add(menuItemAdd);
        menuChat.getItems().add(menuItemRefresh);
        menuFile.getItems().add(menuItemSignOut);
        menuBar.getMenus().addAll(menuFile, menuChat);
        myLayout.add(menuBar, 0, 0);
        myLayout.add(welcome, 0, 1);
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
                        setChanged();
                        notifyObservers("refresh");
                    }
                    event.consume();
                }
            }
        });
    }

    private Menu menuCreateConvMenu(String theName) {
        Menu menu = new Menu(theName);

        MenuItem select = new MenuItem("Select");
        MenuItem add = new MenuItem("Add user");

        select.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    String countQuery = "SELECT conversations.idconversations FROM conversations WHERE conversations.name='" + theName + "';";
                    ResultSet rs = myDB.DML_ResultSet(countQuery);
                    if (rs.next()) {
                        myUser.setConvID(Integer.parseInt(rs.getString(1)));
                    }
                } catch (SQLException exception) {
                    System.out.println(exception);
                }
                myUser.setConvName(theName);
                setChanged();
                notifyObservers("refresh");
            }
        });

        // add.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override public void handle(ActionEvent e) {
        //         try {
        //             String addQuery = "INSERT INTO `gutierrez_edgardo_db`.`conversants` (`conversation`, `conversant`) VALUES ('" + conv + "', '+');";
        //             ResultSet rs = myDB.DML_ResultSet(addQuery);
        //         } catch (SQLException exception) {
        //             System.out.println(exception);
        //         }
        //     }
        // });

        menu.getItems().add(select);
        menu.getItems().add(add);

        return menu;
    }

    private void submitMessage(String theMessage) {
        try {
            String countQuery = "SELECT COUNT(*) FROM messages WHERE messages.conversation=" + myUser.getConvID() + ";";
            ResultSet rs = myDB.DML_ResultSet(countQuery);
            int totalMessages = 0;
            if (rs.next()) {
                totalMessages = Integer.parseInt(rs.getString(1));
            }
            int counter = totalMessages + 1;
            String submitQuery = "INSERT INTO `gutierrez_edgardo_db`.`messages` "
              + "(`conversation`, `counter`, `sender`, `message`) "
              + "VALUES ('" + myUser.getConvID() + "', '" + counter + "', '" + myUser.getUserID() + "', '" + theMessage + "');";
            myDB.DML_Statement(submitQuery);
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
    }

    private String[] getConversations() {
        try {
            String countQuery = "SELECT COUNT(`conversants`.conversation) FROM `conversants` WHERE `conversants`.conversant=" + myUser.getUserID() + ";";
            String conversantQuery = "SELECT `conversants`.conversation FROM `conversants` WHERE `conversants`.conversant=" + myUser.getUserID() + ";";
            ResultSet rs = myDB.DML_ResultSet(conversantQuery);
            String set = "(";
            while (rs.next()) {
                set += rs.getString(1) + ", ";
            }
            if (set.length() >= 2) {
                set = set.substring(0, set.length() - 2);
                set += ");";
            }
            String convsQuery = "SELECT conversations.name FROM conversations WHERE conversations.idconversations in " + set;

            rs = myDB.DML_ResultSet(countQuery);
            int totalConvs = 0;
            if (rs.next()) {
                totalConvs = Integer.parseInt(rs.getString(1));
            }

            // At least 1 conversation
            if (totalConvs > 0) {
                rs = myDB.DML_ResultSet(convsQuery);
                String[] convNames = new String[totalConvs];
                rs.next();
                for (int i = 0; i < totalConvs; i++) {
                    convNames[i] = rs.getString(1);
                    rs.next();
                }
                return convNames;
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
        return null;
    }

    private String getMessages(int theAmount) {
        try {
            String countQuery = "SELECT COUNT(*) FROM messages WHERE messages.conversation=" + myUser.getConvID() + ";";
            ResultSet rs = myDB.DML_ResultSet(countQuery);
            int totalMessages = 0;
            if (rs.next()) {
                totalMessages = Integer.parseInt(rs.getString(1));
            }
            String messagesQuery = "SELECT messages.message FROM messages WHERE messages.conversation=" + myUser.getConvID() + ";";
            rs = myDB.DML_ResultSet(messagesQuery);
            rs.next();
            String messages = "";
            int threshhold = -1;
            if (totalMessages > theAmount) threshhold = totalMessages - theAmount;

            for (int i = 0; i < totalMessages; i++) {
                if (i > threshhold) {
                    messages += rs.getString(1) + '\n';
                }
                rs.next();
            }
            return messages;
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
        return "ERROR";
    }

    public void setInfo(int theUserID, int theConvID, String theConvName) {
        myUser.setUserID(theUserID);
        myUser.setConvID(theConvID);
        myUser.setConvName(theConvName);
    }
}
