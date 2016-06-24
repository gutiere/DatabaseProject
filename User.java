/**
* @author  Edgardo Gutierrez Jr.
* @version 1.0
* @since   2016-6-1
*/

public class User {
    private String myUsername, myConvName, myGameName;

    public User() {
        myUsername = "Username";
        myConvName = "NoConv";
        myGameName = "NoGame";
    }

    public void setUsername(String theUsername) {
        myUsername = theUsername;
    }

    public void setConvName(String theConvName) {
        myConvName = theConvName;
    }

    public void setGameName(String theGameName) {
        myGameName = theGameName;
    }

    public String getUsername() {
        return myUsername;
    }

    public String getConvName() {
        return myConvName;
    }

    public String getGameName() {
        return myGameName;
    }

    public String toString() {
        return "Username " + myUsername + "\n" + "ConvName " + myConvName + "\n";
    }

}
