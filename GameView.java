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
    private MenuBar myMenuBar;
    private Menu myFileMenu;
    private MenuItem mySignoutMenuItem;
    private GridPane myLayout;
    private GridPane myBoardLayout;
    private Button[][] myBoardButtons;
    private boolean myWhite;

    public GameView(boolean theWhitePlayer, String theUsername, String theMessages, Menu theChatMenu, Menu theContactsMenu, int theWidth, int theHeight) {
        myWhite = theWhitePlayer;
        instantiations(theUsername, theMessages);
        design(theChatMenu, theContactsMenu);
    }

    private void instantiations(String theUsername, String theMessages) {
        myLayout = new GridPane();
        myChatRoom = new Label(theMessages);
        myTextField = new TextField();
        myMenuBar = new MenuBar();
        myFileMenu = new Menu("File");
        myChatMenu = new Menu("Chat");
        mySignoutMenuItem = new MenuItem("(Sign out)");
        myBoardLayout = new GridPane();
        myBoardButtons = new Button[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                myBoardButtons[i][j] = new Button(i + ", " + j);
            }
        }

    }

    private void design(Menu theChatMenu, Menu theContactsMenu) {
        myChatMenu = theChatMenu;
        myFileMenu.getItems().add(mySignoutMenuItem);
        myMenuBar.getMenus().addAll(myFileMenu, theContactsMenu, myChatMenu);


        // col x row
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (myWhite) myBoardLayout.add(myBoardButtons[7 - row][7 - col], 7 - col, 7 - row);
                else myBoardLayout.add(myBoardButtons[row][col], col, row);
            }
        }
        System.out.println("ok");
        myLayout.add(myMenuBar, 0, 0);
        myLayout.add(myBoardLayout, 0, 1);
        myLayout.add(myChatRoom, 0, 2);
        myLayout.add(myTextField, 0, 3);
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
}
