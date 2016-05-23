import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import java.sql.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.Timer;
import java.awt.event.ActionListener;

public class HomeState extends State {

    // private static final String URL = "jdbc:mysql://localhost:3306/gutierrez_edgardo_db?useSSL=false";
    private static final String URL = "jdbc:mysql://192.168.1.102:3306/gutierrez_edgardo_db?useSSL=false";
    private static final String USERNAME = "UWTuser";
    private static final String PASSWORD = "something";
    private GridPane myLayout;
    private Scene myScene;
    private Label eLabel;
    private TextField myTextBox;
    private String myConvName;
    private int myUserID;
    private String myUsername;
    private int myConvID;
    private Timer myTimer;

    public HomeState(int theWidth, int theHeight) {
        myLayout = new GridPane();



    }

    private void generateLoginScene() {
        Label welcome = new Label(getMessages(5));
        myTextBox = new TextField();

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuChat = new Menu("Chat");

        String[] conversations = getConversations();
        if (conversations != null) {
            for (int i = 0; i < conversations.length; i++) {
                menuChat.getItems().add(menuCreateConvMenuItem(conversations[i]));
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

    private MenuItem menuCreateConvMenuItem(String theName) {
        MenuItem temp = new MenuItem();
        temp.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    DBAdapter db = new DBAdapter(URL, USERNAME, PASSWORD);
                    String countQuery = "SELECT conversations.idconversations FROM conversations WHERE conversations.name='" + theName + "';";
                    ResultSet rs = db.DML_ResultSet(countQuery);
                    if (rs.next()) {
                        myConvID = Integer.parseInt(rs.getString(1));
                    }
                } catch (SQLException exception) {
                    System.out.println(exception);
                }
                myConvName = theName;
                // Update State Manager
                Object[] info = {myUserID, myConvID, myConvName};
                setChanged();
                notifyObservers(info);
                setChanged();
                notifyObservers("refresh");
            }
        });

        temp.setText(theName);
        return temp;
    }

    private void submitMessage(String theMessage) {
        try {
            DBAdapter db = new DBAdapter(URL, USERNAME, PASSWORD);
            String countQuery = "SELECT COUNT(*) FROM messages WHERE messages.conversation=" + myConvID + ";";
            ResultSet rs = db.DML_ResultSet(countQuery);
            int totalMessages = 0;
            if (rs.next()) {
                totalMessages = Integer.parseInt(rs.getString(1));
            }
            int counter = totalMessages + 1;
            String submitQuery = "INSERT INTO `gutierrez_edgardo_db`.`messages` "
              + "(`conversation`, `counter`, `sender`, `message`) "
              + "VALUES ('" + myConvID + "', '" + counter + "', '" + myUserID + "', '" + theMessage + "');";
            db.DML_Statement(submitQuery);
        } catch (SQLException e) {
            System.out.println("Exception: " + e);
        }
    }

    private String[] getConversations() {
        String countQuery = "SELECT COUNT(*) FROM conversations";
        String convsQuery = "SELECT conversations.name FROM gutierrez_edgardo_db.conversations;";

        try {
            DBAdapter db = new DBAdapter(URL, USERNAME, PASSWORD);
            ResultSet rs = db.DML_ResultSet(countQuery);
            int totalConvs = 0;
            if (rs.next()) {
                totalConvs = Integer.parseInt(rs.getString(1));
            }

            // At least 1 conversation
            if (totalConvs > 0) {
                rs = db.DML_ResultSet(convsQuery);
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
            String countQuery = "SELECT COUNT(*) FROM messages WHERE messages.conversation=" + myConvID + ";";
            DBAdapter db = new DBAdapter(URL, USERNAME, PASSWORD);
            ResultSet rs = db.DML_ResultSet(countQuery);
            int totalMessages = 0;
            if (rs.next()) {
                totalMessages = Integer.parseInt(rs.getString(1));
            }
            String messagesQuery = "SELECT messages.message FROM messages WHERE messages.conversation=" + myConvID + ";";
            rs = db.DML_ResultSet(messagesQuery);
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

    public void setErrorMessage(String theError) {
        eLabel.setText(theError);
    }

    public void setInfo(int theUserID, int theConvID, String theConvName) {
        myUserID = theUserID;
        myConvID = theConvID;
        myConvName = theConvName;
    }

    public Scene getScene() {
        generateLoginScene();
        return myScene;
    }
}
