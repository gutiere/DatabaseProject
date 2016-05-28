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

public class HomeView extends View {

    private TextField myTextField;
    private int myContactID;
    private Label myChatRoom;
    private Menu myChatMenu;
    private MenuBar myMenuBar;
    private Menu myFileMenu;
    private MenuItem mySignoutMenuItem;
    private GridPane myLayout;

    public HomeView(String theUsername, String theMessages, Menu theChatMenu, Menu theContactsMenu, int theWidth, int theHeight) {
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
    }

    private void design(Menu theChatMenu, Menu theContactsMenu) {
        myChatMenu = theChatMenu;
        myFileMenu.getItems().add(mySignoutMenuItem);
        myMenuBar.getMenus().addAll(myFileMenu, theContactsMenu, myChatMenu);
        myLayout.add(myMenuBar, 0, 0);
        myLayout.add(myChatRoom, 0, 1);
        myLayout.add(myTextField, 0, 2);
        myScene = new Scene(myLayout);
    }

    public void setSignoutHandle(EventHandler<ActionEvent> theEH) {
        mySignoutMenuItem.setOnAction(theEH);
    }

    public void setEnterHandle(EventHandler<KeyEvent> theEH) {
        myTextField.setOnKeyPressed(theEH);
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
