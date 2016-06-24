/**
* @author  Edgardo Gutierrez Jr.
* @version 1.0
* @since   2016-6-1
*/

import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.scene.layout.GridPane;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Button;

public class GameView extends View {

    private TextField myTextField;
    private int myContactID;
    private Label myChatRoom;
    private Menu myChatMenu;
    private Menu myPlayMenu;
    private MenuBar myMenuBar;
    private Menu myFileMenu;
    private MenuItem mySignoutMenuItem;
    private GridPane myLayout;
    private GridPane myBoardLayout;
    private Button[][] myBoardButtons;
    private boolean myWhite;
    private MenuItem myUsername;
    private User myUser;

    public GameView(User theUser, boolean theWhitePlayer, String theUsername, String theMessages, Menu theChatMenu, Menu theContactsMenu, Menu thePlayMenu, int theWidth, int theHeight) {
        myUser = theUser;
        myWidth = 400;
        myHeight = 535;
        myWhite = theWhitePlayer;
        instantiations(theUsername, theMessages);
        design(theChatMenu, theContactsMenu, thePlayMenu);
    }

    private void instantiations(String theUsername, String theMessages) {
        myLayout = new GridPane();
        myChatRoom = new Label(theMessages);
        myTextField = new TextField();
        myMenuBar = new MenuBar();
        myFileMenu = new Menu("File");
        myChatMenu = new Menu("Chat");
        myPlayMenu = new Menu("Play");
        mySignoutMenuItem = new MenuItem("(Sign out)");
        myUsername = new MenuItem(theUsername);
        myBoardLayout = new GridPane();
        myBoardButtons = new Button[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                myBoardButtons[i][j] = new Button();
                myBoardButtons[i][j].setPrefWidth(myWidth / 8);
                myBoardButtons[i][j].setPrefHeight(myWidth / 8);
            }
        }
    }

    private void design(Menu theChatMenu, Menu theContactsMenu, Menu thePlayMenu) {
        myChatMenu = theChatMenu;
        myPlayMenu = thePlayMenu;
        myFileMenu.getItems().add(myUsername);
        myFileMenu.getItems().add(mySignoutMenuItem);
        myMenuBar.getMenus().add(myFileMenu);
        myMenuBar.getMenus().add(theContactsMenu);
        myMenuBar.getMenus().add(myChatMenu);
        myMenuBar.getMenus().add(myPlayMenu);

        myChatRoom.setPrefHeight(87.0);
        myChatRoom.setPrefWidth(myWidth);


        // col x row
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                myBoardLayout.add(myBoardButtons[row][col], col, row);
            }
        }
        myLayout.add(myMenuBar, 0, 0);
        if (myUser.getGameName().equals("NoGame")) {
            myLayout.add(new Label("Use the 'Play' menu to join or add a game!"), 0, 1);
        } else {
            myLayout.add(myBoardLayout, 0, 1);
        }
        if (myUser.getConvName().equals("NoConv")) {
            myLayout.add(new Label("Use the 'Chat' menu to join or add a conversation!"), 0, 2);
        } else {
            myLayout.add(myChatRoom, 0, 2);
            myLayout.add(myTextField, 0, 3);
        }
        myScene = new Scene(myLayout);
    }

    public void setSignoutHandle(EventHandler<ActionEvent> theEH) {
        mySignoutMenuItem.setOnAction(theEH);
    }

    public void setEnterHandle(EventHandler<KeyEvent> theEH) {
        myTextField.setOnKeyPressed(theEH);
    }

    public void setBoardHandle(EventHandler<ActionEvent> theEH, int theRow, int theCol) {
        myBoardButtons[theRow][theCol].setOnAction(theEH);
    }

    public void setTextField(String theString) {
        myTextField.setText(theString);
    }

    public void setChatRoom(String theString) {
        myChatRoom.setText(theString);
    }

    public String getTextField() {
        return myTextField.getText();
    }

    public void setBoardChars(char[][] theChars) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!myWhite) {
                    if (theChars[i][j] == '-') {
                        myBoardButtons[7 - i][j].setText("");
                    } else {
                        myBoardButtons[7 - i][j].setText(theChars[i][j] + "");
                    }
                } else {
                    if (theChars[i][j] == '-') {
                        myBoardButtons[i][j].setText("");
                    } else {
                        myBoardButtons[i][j].setText(theChars[i][j] + "");
                    }
                }
            }
        }
    }
}
